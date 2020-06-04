package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.FosterLiveListAdapter;
import com.haotang.pet.entity.FosterLive;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 直播列表界面
 */
public class FosterLiveListActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_fosterlivelist)
    RecyclerView rvFosterlivelist;
    @BindView(R.id.srl_fosterlivelist)
    SwipeRefreshLayout srlFosterlivelist;
    private List<FosterLive> list = new ArrayList<FosterLive>();
    private FosterLiveListAdapter fosterLiveListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_foster_live_list);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("寄养直播");
        srlFosterlivelist.setRefreshing(true);
        srlFosterlivelist.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvFosterlivelist.setHasFixedSize(true);
        rvFosterlivelist.setLayoutManager(new LinearLayoutManager(this));
        fosterLiveListAdapter = new FosterLiveListAdapter(R.layout.item_fosterlivelist, list);
        rvFosterlivelist.setAdapter(fosterLiveListAdapter);
    }

    private void setLinster() {
        srlFosterlivelist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getData();
            }
        });
        fosterLiveListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (list != null && list.size() > 0 && list.size() > position) {
                    final FosterLive item = list.get(position);
                    switch (view.getId()) {
                        case R.id.iv_fosterlivelist_qp:
                            if (Utils.isStrNull(item.getLiveUrl()) && item.getLiveState() == 1 && item.getCameraState() == 0) {
                                startActivity(new Intent(mContext, FosterLiveActivity.class).putExtra("videoUrl", item.getLiveUrl()).putExtra("name", item.getRoomContent().replace("@@", "")));
                            } else {
                                ToastUtil.showToastShortBottom(mContext, item.getLiveContent());
                            }
                            break;
                        case R.id.sl_fosterlivelist_root:
                            if (Utils.isStrNull(item.getLiveUrl()) && item.getLiveState() == 1 && item.getCameraState() == 0) {
                                startActivity(new Intent(mContext, FosterLiveActivity.class).putExtra("videoUrl", item.getLiveUrl()).putExtra("name", item.getRoomContent().replace("@@", "")));
                            } else {
                                ToastUtil.showToastShortBottom(mContext, item.getLiveContent());
                            }
                            break;
                        case R.id.iv_fosterlivelist_call:
                            new AlertDialogDefault(mContext).builder()
                                    .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setPositiveButton("是", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.telePhoneBroadcast(mContext, item.getShopCellphone());
                                }
                            }).show();
                            break;
                    }
                }
            }
        });
    }

    private void getData() {
        mPDialog.showDialog();
        srlFosterlivelist.setRefreshing(true);
        list.clear();
        CommUtil.getFosterLiveList(this, getFosterLiveListHandler);
    }

    private AsyncHttpResponseHandler getFosterLiveListHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONArray jarrdata = jsonObject.getJSONArray("data");
                        if (jarrdata.length() > 0) {
                            for (int i = 0; i < jarrdata.length(); i++) {
                                list.add(FosterLive.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (list.size() <= 0) {
                        fosterLiveListAdapter.setEmptyView(setEmptyViewBaseFoster(1, "没有检查到寄养直播", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                                    MApplication.listAppoint.get(i).finish();
                                }
                                MApplication.listAppoint.clear();
                                for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                                    MApplication.listAppoint1.get(i).finish();
                                }
                                MApplication.listAppoint.clear();
                                MApplication.listAppoint1.clear();
                                startActivity(new Intent(mContext,FosterHomeActivity.class));
                                finish();
                            }
                        }));
                    }
                } else {
                    fosterLiveListAdapter.setEmptyView(setEmptyViewBaseFoster(2, msg, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getData();
                        }
                    }));
                }
            } catch (JSONException e) {
                fosterLiveListAdapter.setEmptyView(setEmptyViewBaseFoster(2, "数据异常", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                }));
                e.printStackTrace();
            }
            fosterLiveListAdapter.notifyDataSetChanged();
            srlFosterlivelist.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            srlFosterlivelist.setRefreshing(false);
            mPDialog.closeDialog();
            fosterLiveListAdapter.setEmptyView(setEmptyViewBaseFoster(2, "请求失败", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            }));
        }
    };

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

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }
}
