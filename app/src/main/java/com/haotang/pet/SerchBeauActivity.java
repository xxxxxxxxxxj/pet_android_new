package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SerchBeauAdapter;
import com.haotang.pet.adapter.SerchBeauResultAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SoftKeyBoardListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>
 * Title:SerchBeauActivity
 * </p>
 * <p>
 * Description:搜索美容师界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-8-1 上午11:48:20
 */
public class SerchBeauActivity extends SuperActivity {
    @BindView(R.id.editText_serch_beau)
    EditText editTextSerchBeau;
    @BindView(R.id.rv_serch_beau)
    RecyclerView rvSerchBeau;
    @BindView(R.id.srl_serch_beau)
    SwipeRefreshLayout srlSerchBeau;
    @BindView(R.id.vw_serch_beau_bg)
    View vwSerchBeauBg;
    @BindView(R.id.rv_serch_beauresult)
    RecyclerView rvSearchBeauResult;
    private int page = 1;
    public static SuperActivity act;
    private int pageSize = 10;
    private List<Beautician> list = new ArrayList<Beautician>();
    private List<Beautician> localList = new ArrayList<Beautician>();
    private SerchBeauAdapter serchBeauAdapter;
    private SerchBeauResultAdapter serchBeauResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        initWindows();
        setView();
        setLinster();
        refresh();
    }

    private void initData() {
        act = this;
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_serch_beau);
        ButterKnife.bind(this);
    }

    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(android.R.color.transparent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "1");
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("TAG", "2");
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    private void setView() {
        //默认隐藏软件盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        srlSerchBeau.setRefreshing(true);
        srlSerchBeau.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvSerchBeau.setHasFixedSize(true);
        rvSerchBeau.setLayoutManager(new GridLayoutManager(mContext, 3));
        serchBeauAdapter = new SerchBeauAdapter(R.layout.item_serch_beau, list);
        rvSerchBeau.setAdapter(serchBeauAdapter);
        rvSearchBeauResult.setVisibility(View.GONE);
        serchBeauResultAdapter = new SerchBeauResultAdapter(R.layout.item_searchbeauty_result,list);
        rvSearchBeauResult.setLayoutManager(new LinearLayoutManager(mContext));
        rvSearchBeauResult.setAdapter(serchBeauResultAdapter);
    }

    private void setLinster() {
        serchBeauAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlSerchBeau.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.e("TAG", "keyBoardShow height = " + height);
                vwSerchBeauBg.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                Log.e("TAG", "keyBoardHide height = " + height);
                vwSerchBeauBg.setVisibility(View.GONE);
                pageSize = 10;
                refresh();
            }
        });
        editTextSerchBeau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextSerchBeau.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Utils.goneJP(mContext);
                    return true;
                }
                return false;
            }
        });
        serchBeauAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.size() > position) {
                    Beautician beautician = list.get(position);
                    if (beautician != null) {
                        switch (view.getId()) {
                            case R.id.iv_item_serchbeau_yy:
                                Intent intent = new Intent(SerchBeauActivity.this, WorkerMenuItemActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("beautician", beautician);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case R.id.iv_item_serchbeau_img:
                                Intent intent1 = new Intent(SerchBeauActivity.this, BeauticianDetailActivity.class);
                                intent1.putExtra("id", beautician.id);
                                intent1.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
                                startActivity(intent1);
                                break;
                        }
                    }
                }
            }
        });
        serchBeauResultAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.size() > position) {
                    Beautician beautician = list.get(position);
                    if (beautician != null) {
                        switch (view.getId()) {
                            case R.id.iv_item_serchbeau_yy:
                                Intent intent = new Intent(SerchBeauActivity.this, WorkerMenuItemActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("beautician", beautician);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case R.id.nv_searchbeauty_head:
                                Intent intent1 = new Intent(SerchBeauActivity.this, BeauticianDetailActivity.class);
                                intent1.putExtra("id", beautician.id);
                                intent1.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
                                startActivity(intent1);
                                break;
                        }
                    }
                }
            }
        });
    }

    private void refresh() {
        page = 1;
        list.clear();
        serchBeauAdapter.setEnableLoadMore(false);
        srlSerchBeau.setRefreshing(true);
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void getData() {
        mPDialog.showDialog();
        if (!"".equals(editTextSerchBeau.getText().toString().trim())){
            rvSerchBeau.setVisibility(View.GONE);
            rvSearchBeauResult.setVisibility(View.VISIBLE);
        }else {
            rvSerchBeau.setVisibility(View.VISIBLE);
            rvSearchBeauResult.setVisibility(View.GONE);
        }
        CommUtil.getWorkerByName(this, 0, editTextSerchBeau.getText().toString().trim(), page, pageSize, getWorkerByNameHandler);
    }

    private AsyncHttpResponseHandler getWorkerByNameHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            localList.clear();
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONArray jarrdata = jsonObject.getJSONArray("data");
                        if (jarrdata != null && jarrdata.length() > 0) {
                            for (int i = 0; i < jarrdata.length(); i++) {
                                localList.add(Beautician.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (page == 1) {
                        srlSerchBeau.setRefreshing(false);
                        serchBeauAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    serchBeauAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        if (page == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                serchBeauAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        if (page == 1) {
                            serchBeauAdapter.setEmptyView(setEmptyViewBase(2, "暂无美容师", "", null,Color.WHITE,60));
                            serchBeauResultAdapter.setEmptyView(setEmptyViewBase(2, "未搜索到此美容师哦~", "", null,Color.WHITE,60));
                            serchBeauAdapter.loadMoreEnd(true);
                        } else {
                            serchBeauAdapter.loadMoreEnd(false);
                        }
                    }
                    serchBeauAdapter.notifyDataSetChanged();
                    serchBeauResultAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        serchBeauAdapter.setEmptyView(setEmptyViewBase(1, msg,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        serchBeauResultAdapter.setEmptyView(setEmptyViewBase(1, msg,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        serchBeauAdapter.setEnableLoadMore(false);
                        srlSerchBeau.setRefreshing(false);
                    } else {
                        serchBeauAdapter.setEnableLoadMore(true);
                        serchBeauAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    serchBeauAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    serchBeauAdapter.setEnableLoadMore(false);
                    srlSerchBeau.setRefreshing(false);
                } else {
                    serchBeauAdapter.setEnableLoadMore(true);
                    serchBeauAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            serchBeauAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                serchBeauAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                serchBeauResultAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                serchBeauAdapter.setEnableLoadMore(false);
                srlSerchBeau.setRefreshing(false);
            } else {
                serchBeauAdapter.setEnableLoadMore(true);
                serchBeauAdapter.loadMoreFail();
            }
        }
    };

    @OnClick({R.id.editText_serch_beau, R.id.iv_serch_beau_title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editText_serch_beau:
                break;
            case R.id.iv_serch_beau_title_back:
                finish();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Utils.hideKeyboard(ev, editTextSerchBeau, SerchBeauActivity.this);//调用方法判断是否需要隐藏键盘
//                break;
//            default:
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }
}
