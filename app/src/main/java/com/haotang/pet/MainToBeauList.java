package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.UnAvailableWorkerGridAdapter;
import com.haotang.pet.adapter.UnAvailableWorkerListAdapter;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页进入美容师列表
 */
public class MainToBeauList extends SuperActivity {
    public static MainToBeauList act;
    @BindView(R.id.tv_mainto_beaulist_titlebar_title)
    TextView tvMaintoBeaulistTitlebarTitle;
    @BindView(R.id.rv_mainto_beaulist)
    RecyclerView rvMaintoBeaulist;
    @BindView(R.id.mrl_mainto_beaulist)
    MaterialRefreshLayout mrlMaintoBeaulist;
    @BindView(R.id.ib_mainto_beaulist_titlebar_listorgrid)
    ImageView ib_mainto_beaulist_titlebar_listorgrid;
    private int page = 1;
    private ArrayList<AppointWorker> unAvailableWorkerList = new ArrayList<AppointWorker>();
    private ArrayList<AppointWorker> localUnAvailableWorkerList = new ArrayList<AppointWorker>();
    private UnAvailableWorkerListAdapter unAvailableWorkerListAdapter;
    private int workerNum;
    private boolean flag;
    private UnAvailableWorkerGridAdapter unAvailableWorkerGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void setLinster() {
        mrlMaintoBeaulist.setMaterialRefreshListener(new MaterialRefreshListener() {

            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                loadMore();
            }
        });
    }

    private void refresh() {
        page = 1;
        unAvailableWorkerList.clear();
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void initData() {
        act = this;
        MApplication.listAppoint.add(this);
    }

    private void setView() {
        unAvailableWorkerList.clear();
        flag = false;
        setListToGrid();
        mrlMaintoBeaulist.autoRefresh();
    }

    private void findView() {
        setContentView(R.layout.activity_main_to_beaulist);
        ButterKnife.bind(this);
    }

    private void getData() {
        workerNum = 0;
        localUnAvailableWorkerList.clear();
        CommUtil.getAllWorkers(this, page, spUtil.getInt("shopid", 0), getAllWorkers);
    }

    private AsyncHttpResponseHandler getAllWorkers = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("total") && !jdata.isNull("total")) {
                                workerNum = jdata.getInt("total");
                            }
                            if (jdata.has("dataset") && !jdata.isNull("dataset")) {
                                JSONArray jarrdataset = jdata.getJSONArray("dataset");
                                if (jarrdataset.length() > 0) {
                                    for (int i = 0; i < jarrdataset.length(); i++) {
                                        localUnAvailableWorkerList.add(AppointWorker.json2Entity(jarrdataset
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                    if (page == 1) {
                        if (Utils.isStrNull(spUtil.getString("upShopName", ""))) {
                            tvMaintoBeaulistTitlebarTitle.setText(spUtil.getString("upShopName", "") + "(" + workerNum + ")");
                        } else {
                            tvMaintoBeaulistTitlebarTitle.setText("美容师(" + workerNum + ")");
                        }
                    }
                    if (localUnAvailableWorkerList.size() > 0) {
                        if (page == 1) {
                            unAvailableWorkerList.clear();
                        }
                        unAvailableWorkerList.addAll(localUnAvailableWorkerList);
                        page++;
                    } else {
                        if (page == 1) {
                            if (flag) {//list
                                unAvailableWorkerListAdapter.setEmptyView(setEmptyViewBase(2, "暂无美容师哦~",
                                        R.drawable.icon_no_mypet, null));
                            } else {
                                unAvailableWorkerGridAdapter.setEmptyView(setEmptyViewBase(2, "暂无美容师哦~",
                                        R.drawable.icon_no_mypet, null));
                            }
                        }
                    }
                    if (flag) {//list
                        unAvailableWorkerListAdapter.notifyDataSetChanged();
                    } else {
                        unAvailableWorkerGridAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (page == 1 && unAvailableWorkerList.size() <= 0) {
                        if (flag) {//list
                            unAvailableWorkerListAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            refresh();
                                        }
                                    }));
                        } else {
                            unAvailableWorkerGridAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            refresh();
                                        }
                                    }));
                        }
                    }
                }
            } catch (Exception e) {
                if (page == 1 && unAvailableWorkerList.size() <= 0) {
                    if (flag) {//list
                        unAvailableWorkerListAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                refresh();
                            }
                        }));
                    } else {
                        unAvailableWorkerGridAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                refresh();
                            }
                        }));
                    }
                }
                e.printStackTrace();
            }
            mrlMaintoBeaulist.finishRefresh();
            mrlMaintoBeaulist.finishRefreshLoadMore();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mrlMaintoBeaulist.finishRefresh();
            mrlMaintoBeaulist.finishRefreshLoadMore();
            if (page == 1 && unAvailableWorkerList.size() <= 0) {
                if (flag) {//list
                    unAvailableWorkerListAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                } else {
                    unAvailableWorkerGridAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                }
            }
        }
    };

    @OnClick({R.id.ib_mainto_beaulist_titlebar_back, R.id.ib_mainto_beaulist_titlebar_serch, R.id.ib_mainto_beaulist_titlebar_listorgrid})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_mainto_beaulist_titlebar_back:
                finish();
                break;
            case R.id.ib_mainto_beaulist_titlebar_serch:
                startActivity(new Intent(this, SerchBeauActivity.class));
                break;
            case R.id.ib_mainto_beaulist_titlebar_listorgrid:
                flag = !flag;
                setListToGrid();
                break;
        }
    }

    private void setListToGrid() {
        if (flag) {//list
            ib_mainto_beaulist_titlebar_listorgrid.setImageResource(R.drawable.icon_workerlist_grid);
            rvMaintoBeaulist.setHasFixedSize(true);
            rvMaintoBeaulist.setLayoutManager(new LinearLayoutManager(this));
            unAvailableWorkerListAdapter = new UnAvailableWorkerListAdapter(R.layout.item_workerlist_list, unAvailableWorkerList);
            rvMaintoBeaulist.setAdapter(unAvailableWorkerListAdapter);
            unAvailableWorkerListAdapter.setOnWorkerInfoListener(new UnAvailableWorkerListAdapter.OnWorkerInfoListener() {
                @Override
                public void OnWorkerInfo(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        Intent intent1 = new Intent(MainToBeauList.this, BeauticianDetailActivity.class);
                        intent1.putExtra("id", appointWorker.getWorkerId());
                        intent1.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
                        startActivity(intent1);
                    }
                }
            });
            unAvailableWorkerListAdapter.setOnWorkerSelectListener(new UnAvailableWorkerListAdapter.OnWorkerSelectListener() {
                @Override
                public void OnWorkerSelect(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        if (appointWorker != null) {
                            Intent intent = new Intent(MainToBeauList.this, WorkerMenuItemActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("beautician", new Beautician(appointWorker.getWorkerId(), appointWorker.getRealName(), appointWorker.getTid(), appointWorker.getAvatar()));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                }
            });
        } else {//gird
            ib_mainto_beaulist_titlebar_listorgrid.setImageResource(R.drawable.icon_workerlist_list);
            rvMaintoBeaulist.setHasFixedSize(true);
            rvMaintoBeaulist.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
            unAvailableWorkerGridAdapter = new UnAvailableWorkerGridAdapter(R.layout.item_workerlist_gird, unAvailableWorkerList);
            rvMaintoBeaulist.setAdapter(unAvailableWorkerGridAdapter);
            unAvailableWorkerGridAdapter.setOnWorkerInfoListener(new UnAvailableWorkerGridAdapter.OnWorkerInfoListener() {
                @Override
                public void OnWorkerInfo(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        Intent intent1 = new Intent(MainToBeauList.this, BeauticianDetailActivity.class);
                        intent1.putExtra("id", appointWorker.getWorkerId());
                        intent1.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
                        startActivity(intent1);
                    }
                }
            });
            unAvailableWorkerGridAdapter.setOnWorkerSelectListener(new UnAvailableWorkerGridAdapter.OnWorkerSelectListener() {
                @Override
                public void OnWorkerSelect(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        if (appointWorker != null) {
                            Intent intent = new Intent(MainToBeauList.this, WorkerMenuItemActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("beautician", new Beautician(appointWorker.getWorkerId(), appointWorker.getRealName(), appointWorker.getTid(), appointWorker.getAvatar()));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}
