package com.haotang.pet.encyclopedias.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.adapter.CollectEncyAdapter;
import com.haotang.pet.encyclopedias.bean.CollectEncyclopedias;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收藏百科界面
 */
public class MyCollectActivity extends SuperActivity {

    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.rv_my_collect)
    RecyclerView rvMyCollect;
    @BindView(R.id.srl_my_collect)
    SwipeRefreshLayout srlMyCollect;
    @BindView(R.id.rl_my_collect_bottom)
    RelativeLayout rl_my_collect_bottom;
    @BindView(R.id.iv_my_collect_select)
    ImageView iv_my_collect_select;
    private List<CollectEncyclopedias> localList = new ArrayList<CollectEncyclopedias>();
    private List<CollectEncyclopedias> list = new ArrayList<CollectEncyclopedias>();
    private int mNextRequestPage = 1;
    private CollectEncyAdapter collectEncyAdapter;
    private boolean isSelect;
    private int selectNum;
    private int pageSize;
    private String ids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorBar();
        initData();
        findView();
        setView();
        setLinster();
        refresh();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
    }

    private void setLinster() {
        collectEncyAdapter.setDelListener(new CollectEncyAdapter.DelClickListener() {
            @Override
            public void onItemDelClick(int position) {
                final CollectEncyclopedias collectEncyclopedias = list.get(position);
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("确定取消收藏吗？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectEncyclopedias.select = true;
                        mPDialog.showDialog();
                        CommUtil.deleteUserCollection(MyCollectActivity.this, collectEncyclopedias.getId() + "", deleteHandler);
                    }
                }).show();
            }
        });
        collectEncyAdapter.setLongClickListener(new CollectEncyAdapter.ItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                for (int i = 0; i < list.size(); i++) {
                    if (i==position){
                        list.get(i).setShowDel(true);
                    }else {
                        list.get(i).setShowDel(false);
                    }
                }
                collectEncyAdapter.notifyDataSetChanged();
            }
        });
        collectEncyAdapter.setGoneDelListener(new CollectEncyAdapter.GoneDelClickListener() {
            @Override
            public void onItemGoneDelClick(int position) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isShowDel){
                        list.get(i).setShowDel(false);
                        collectEncyAdapter.notifyItemChanged(i);
                    }
                }
            }
        });
        collectEncyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlMyCollect.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
        /*collectEncyAdapter.setOnSelectListener(new CollectEncyAdapter.OnSelectListener() {
            @Override
            public void OnSelect(int position) {
                if (list.size() > position) {
                    selectNum = 0;
                    CollectEncyclopedias collectEncyclopedias = list.get(position);
                    if (collectEncyclopedias != null) {
                        collectEncyclopedias.setSelect(!collectEncyclopedias.isSelect());
                        collectEncyAdapter.notifyDataSetChanged();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        CollectEncyclopedias collectEncyclopedias1 = list.get(i);
                        if (collectEncyclopedias1 != null && collectEncyclopedias1.isSelect()) {
                            selectNum++;
                        }
                    }
                    Log.e("TAG", "selectNum = " + selectNum);
                    if (selectNum == list.size()) {
                        isSelect = true;
                        iv_my_collect_select.setImageResource(R.drawable.complaint_reason);
                    } else {
                        isSelect = false;
                        iv_my_collect_select.setImageResource(R.drawable.complaint_reason_disable);
                    }
                }
            }
        });*/
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isShowDel){
                        list.get(i).setShowDel(false);
                        collectEncyAdapter.notifyItemChanged(i);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void refresh() {
        collectEncyAdapter.setEnableLoadMore(false);
        srlMyCollect.setRefreshing(true);
        mNextRequestPage = 1;
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void setView() {
        tvTitlebarTitle.setText("我的收藏");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("编辑");
        tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));

        srlMyCollect.setRefreshing(true);
        srlMyCollect.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvMyCollect.setHasFixedSize(true);
        rvMyCollect.setLayoutManager(new LinearLayoutManager(this));
        localList.clear();
        list.clear();
        collectEncyAdapter = new CollectEncyAdapter(R.layout.item_collect_ency, list);
        rvMyCollect.setAdapter(collectEncyAdapter);
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_f8_5));
        rvMyCollect.addItemDecoration(divider);
        rvMyCollect.setItemAnimator(null);
    }

    private void getData() {
        localList.clear();
        CommUtil.queryUserCollectionList(this, mNextRequestPage, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray jarrdata = jobj.getJSONArray("data");
                        if (jarrdata.length() > 0) {
                            for (int i = 0; i < jarrdata.length(); i++) {
                                localList.add(CollectEncyclopedias.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (mNextRequestPage == 1) {
                        srlMyCollect.setRefreshing(false);
                        collectEncyAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    collectEncyAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        if (mNextRequestPage == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                collectEncyAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        mNextRequestPage++;
                    } else {
                        if (mNextRequestPage == 1) {
                            collectEncyAdapter.setEmptyView(setEmptyViewBase(2, "您还没有收藏的文章哦~",
                                    R.drawable.icon_no_mypet, null));
                            collectEncyAdapter.loadMoreEnd(true);
                        } else {
                            collectEncyAdapter.loadMoreEnd(false);
                        }
                    }
                } else {
                    if (mNextRequestPage == 1) {
                        collectEncyAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        collectEncyAdapter.setEnableLoadMore(false);
                        srlMyCollect.setRefreshing(false);
                    } else {
                        collectEncyAdapter.setEnableLoadMore(true);
                        collectEncyAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (mNextRequestPage == 1) {
                    collectEncyAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    collectEncyAdapter.setEnableLoadMore(false);
                    srlMyCollect.setRefreshing(false);
                } else {
                    collectEncyAdapter.setEnableLoadMore(true);
                    collectEncyAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            if (mNextRequestPage == 1) {
                if (list.size() > 0) {
                    tvTitlebarOther.setVisibility(View.VISIBLE);
                    rl_my_collect_bottom.setVisibility(View.VISIBLE);
                } else {
                    tvTitlebarOther.setVisibility(View.GONE);
                    rl_my_collect_bottom.setVisibility(View.GONE);
                }
            }
            collectEncyAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            if (mNextRequestPage == 1) {
                collectEncyAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                collectEncyAdapter.setEnableLoadMore(false);
                srlMyCollect.setRefreshing(false);
            } else {
                collectEncyAdapter.setEnableLoadMore(true);
                collectEncyAdapter.loadMoreFail();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other, R.id.btn_my_collect_submit, R.id.ll_my_collect_bottom_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_my_collect_submit:
                if (list.size() > 0) {
                    ids = "";
                    for (int i = 0; i < list.size(); i++) {
                        CollectEncyclopedias collectEncyclopedias = list.get(i);
                        if (collectEncyclopedias != null && collectEncyclopedias.isSelect()) {
                            ids = ids + collectEncyclopedias.getId() + ",";
                        }
                    }
                    Log.e("TAG", "ids = " + ids);
                    if (Utils.isStrNull(ids)) {
                        new AlertDialogDefault(mContext).builder()
                                .setTitle("提示").setMsg("确定删除选中的百科吗？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPDialog.showDialog();
                                CommUtil.deleteUserCollection(MyCollectActivity.this, ids, deleteHandler);
                            }
                        }).show();
                    } else {
                        ToastUtil.showToastShortBottom(MyCollectActivity.this, "请先选择文章");
                    }
                }
                break;
            case R.id.ll_my_collect_bottom_select:
                if (isSelect) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            CollectEncyclopedias collectEncyclopedias = list.get(i);
                            if (collectEncyclopedias != null && collectEncyclopedias.isSelect()) {
                                collectEncyclopedias.setSelect(false);
                            }
                        }
                        iv_my_collect_select.setImageResource(R.drawable.complaint_reason_disable);
                    }
                } else {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            CollectEncyclopedias collectEncyclopedias = list.get(i);
                            if (collectEncyclopedias != null && !collectEncyclopedias.isSelect()) {
                                collectEncyclopedias.setSelect(true);
                            }
                        }
                        iv_my_collect_select.setImageResource(R.drawable.complaint_reason);
                    }
                }
                collectEncyAdapter.notifyDataSetChanged();
                isSelect = !isSelect;
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                collectEncyAdapter.setSelectVis(!collectEncyAdapter.isVisible());
                if (collectEncyAdapter.isVisible()) {
                    tvTitlebarOther.setText("完成");
                    rl_my_collect_bottom.setVisibility(View.VISIBLE);
                } else {
                    tvTitlebarOther.setText("编辑");
                    rl_my_collect_bottom.setVisibility(View.GONE);
                }
                break;
        }
    }

    private AsyncHttpResponseHandler deleteHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    ToastUtil.showDeleteToast(mContext);
                    Iterator<CollectEncyclopedias> it = list.iterator();
                    while (it.hasNext()) {
                        CollectEncyclopedias collectEncyclopedias = it.next();
                        if (collectEncyclopedias != null && collectEncyclopedias.isSelect()) {
                            it.remove();
                        }
                    }
                    collectEncyAdapter.notifyDataSetChanged();
                    if (list.size() <= 0) {
                        tvTitlebarOther.setVisibility(View.GONE);
                        rl_my_collect_bottom.setVisibility(View.GONE);
                        collectEncyAdapter.setEmptyView(setEmptyViewBase(2, "您还没有收藏的文章哦~",
                                R.drawable.icon_no_mypet, null));
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(MyCollectActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(MyCollectActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(MyCollectActivity.this, "请求失败");
        }
    };
}
