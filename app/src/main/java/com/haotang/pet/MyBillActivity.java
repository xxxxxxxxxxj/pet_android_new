package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyBillAdapter;
import com.haotang.pet.entity.GoodsBill;
import com.haotang.pet.entity.MyBill;
import com.haotang.pet.entity.PayWay;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.BillInfoDialog;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
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
 * 我的账单界面
 */
public class MyBillActivity extends SuperActivity {
    private static final int MYBILL_TO_BILLTIME = 1111;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_mybill_time)
    TextView tvMybillTime;
    @BindView(R.id.iv_mybill_time)
    ImageView ivMybillTime;
    @BindView(R.id.rv_mybill)
    RecyclerView rvMybill;
    @BindView(R.id.srl_mybill)
    SwipeRefreshLayout srlMybill;
    @BindView(R.id.tv_emptyview_desc)
    TextView tv_emptyview_desc;
    @BindView(R.id.btn_emptyview)
    Button btn_emptyview;
    @BindView(R.id.ll_order_default)
    LinearLayout ll_order_default;
    private int page = 1;
    private int pageSize;
    private List<MyBill> list = new ArrayList<MyBill>();
    private List<MyBill> localList = new ArrayList<MyBill>();
    private MyBillAdapter myBillAdapter;
    private String startYear = "";
    private String startMonth = "";
    private String endYear = "";
    private String endMonth = "";
    private int selectedStartYearIndex = -1;
    private int selectedStartMonthIndex = -1;
    private int selectedEndYearIndex = -1;
    private int selectedEndMonthIndex = -1;
    private String beginDate;
    private String endDate;
    private int localBeginYear;
    private int localEndYear;
    private int currentMonth;

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
        setContentView(R.layout.activity_my_bill);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("我的账单");
        srlMybill.setRefreshing(true);
        srlMybill.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvMybill.setHasFixedSize(true);
        rvMybill.setLayoutManager(new LinearLayoutManager(this));
        myBillAdapter = new MyBillAdapter(R.layout.item_mybill, list, 1);
        rvMybill.setAdapter(myBillAdapter);
        rvMybill.addItemDecoration(new StickyRecyclerHeadersDecoration(myBillAdapter));
    }

    private void setLinster() {
        myBillAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list != null && list.size() > 0 && list.size() > position) {
                    MyBill myBill = list.get(position);
                    if (myBill != null) {
                        BillInfoDialog billInfoDialog = new BillInfoDialog.Builder(mContext)
                                .setData(myBill)
                                .build();
                        billInfoDialog.show();
                    }
                }
            }
        });
        myBillAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlMybill.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        myBillAdapter.setEnableLoadMore(false);
        srlMybill.setRefreshing(true);
        page = 1;
        queryTradeHistory();
    }

    private void loadMore() {
        queryTradeHistory();
    }

    private void queryTradeHistory() {
        mPDialog.showDialog();
        CommUtil.queryTradeHistory(spUtil.getString("cellphone", ""), Global.getIMEI(this), this, page, beginDate, endDate, -1, queryTradeHistory);
    }

    private AsyncHttpResponseHandler queryTradeHistory = new AsyncHttpResponseHandler() {

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
                        JSONObject jdata = jsonObject.getJSONObject("data");
                        if (jdata.has("beginYear") && !jdata.isNull("beginYear")) {
                            localBeginYear = jdata.getInt("beginYear");
                        }
                        if (jdata.has("endYear") && !jdata.isNull("endYear")) {
                            localEndYear = jdata.getInt("endYear");
                        }
                        if (jdata.has("currentMonth") && !jdata.isNull("currentMonth")) {
                            currentMonth = jdata.getInt("currentMonth");
                        }
                        if (jdata.has("dataset") && !jdata.isNull("dataset")) {
                            JSONArray jarrdataset = jdata.getJSONArray("dataset");
                            if (jarrdataset.length() > 0) {
                                for (int i = 0; i < jarrdataset.length(); i++) {
                                    localList.add(MyBill.json2Entity(jarrdataset
                                            .getJSONObject(i)));
                                }
                                for (int i = 0; i < localList.size(); i++) {
                                    MyBill myBill = localList.get(i);
                                    String tradeDate = myBill.getTradeDate();
                                    String[] split = tradeDate.split("-");
                                    myBill.setGroupTime(split[0] + "-" + split[1]);
                                    List<GoodsBill> goodsList = myBill.getGoodsList();
                                    List<PayWay> payWayList = myBill.getPayWayList();
                                    if (Utils.isStrNull(myBill.getDiscountPriceTitle())) {
                                        if (payWayList != null && payWayList.size() > 0) {
                                            payWayList.add(new PayWay(myBill.getDiscountPriceTitle(), myBill.getDiscountPrice()));
                                        } else {
                                            if (payWayList == null) {
                                                payWayList = new ArrayList<PayWay>();
                                            }
                                            payWayList.add(new PayWay(myBill.getDiscountPriceTitle(), myBill.getDiscountPrice()));
                                            myBill.setPayWayList(payWayList);
                                        }
                                    }
                                    if (Utils.isStrNull(myBill.getThirdPriceTitle())) {
                                        if (payWayList != null && payWayList.size() > 0) {
                                            payWayList.add(new PayWay(myBill.getThirdPriceTitle(), myBill.getThirdPrice()));
                                        } else {
                                            if (payWayList == null) {
                                                payWayList = new ArrayList<PayWay>();
                                            }
                                            payWayList.add(new PayWay(myBill.getThirdPriceTitle(), myBill.getThirdPrice()));
                                            myBill.setPayWayList(payWayList);
                                        }
                                    }
                                    if (goodsList != null && goodsList.size() > 0) {
                                        for (int j = 0; j < goodsList.size(); j++) {
                                            goodsList.get(j).setIcon(myBill.getIcon());
                                        }
                                    } else {
                                        if (goodsList == null) {
                                            goodsList = new ArrayList<GoodsBill>();
                                        }
                                        goodsList.add(new GoodsBill(myBill.getItem(), myBill.getAmount(), "", myBill.getIcon()));
                                        myBill.setGoodsList(goodsList);
                                    }
                                }
                            }
                        }
                    }
                    if (page == 1) {
                        srlMybill.setRefreshing(false);
                        myBillAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    myBillAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        setLayout(1, 0, "");
                        if (page == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                myBillAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        if (page == 1) {
                            setLayout(2, 2, "暂无明细～");
                            myBillAdapter.loadMoreEnd(true);
                        } else {
                            myBillAdapter.loadMoreEnd(false);
                        }
                    }
                    myBillAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        setLayout(2, 1, msg);
                        myBillAdapter.setEnableLoadMore(false);
                        srlMybill.setRefreshing(false);
                    } else {
                        myBillAdapter.setEnableLoadMore(true);
                        myBillAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    setLayout(2, 1, "数据异常");
                    myBillAdapter.setEnableLoadMore(false);
                    srlMybill.setRefreshing(false);
                } else {
                    myBillAdapter.setEnableLoadMore(true);
                    myBillAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        list.get(i).setMonth(true);
                        list.get(i).setHeaderId(i);
                    } else {
                        if (list.get(i - 1).getGroupTime().equals(list.get(i).getGroupTime())) {
                            list.get(i).setMonth(false);
                            list.get(i).setHeaderId(list.get(i - 1).getHeaderId());
                        } else {
                            list.get(i).setMonth(true);
                            list.get(i).setHeaderId(i);
                        }
                    }
                }
            }
            myBillAdapter.notifyDataSetChanged();
            if (Utils.isStrNull(startYear) && Utils.isStrNull(startMonth) && Utils.isStrNull(endYear) && Utils.isStrNull(endMonth)) {

            } else {
                tvMybillTime.setText(localEndYear + "-" + currentMonth);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                setLayout(2, 1, "请求失败");
                myBillAdapter.setEnableLoadMore(false);
                srlMybill.setRefreshing(false);
            } else {
                myBillAdapter.setEnableLoadMore(true);
                myBillAdapter.loadMoreFail();
            }
        }
    };

    private void setLayout(int type, int flag, String msg) {
        if (type == 1) {
            ll_order_default.setVisibility(View.GONE);
            srlMybill.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_order_default.setVisibility(View.VISIBLE);
            srlMybill.setVisibility(View.GONE);
            if (flag == 1) {
                btn_emptyview.setVisibility(View.VISIBLE);
                btn_emptyview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            } else if (flag == 2) {
                btn_emptyview.setVisibility(View.GONE);
            }
            Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        }
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

    @OnClick({R.id.ib_titlebar_back, R.id.ll_mybill_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ll_mybill_time:
                Intent intent = new Intent(MyBillActivity.this, BillTimeActivity.class);
                intent.putExtra("localBeginYear", localBeginYear);
                intent.putExtra("localEndYear", localEndYear);
                intent.putExtra("currentMonth", currentMonth);
                intent.putExtra("startYear", startYear);
                intent.putExtra("startMonth", startMonth);
                intent.putExtra("endYear", endYear);
                intent.putExtra("endMonth", endMonth);
                intent.putExtra("selectedStartYearIndex", selectedStartYearIndex);
                intent.putExtra("selectedStartMonthIndex", selectedStartMonthIndex);
                intent.putExtra("selectedEndYearIndex", selectedEndYearIndex);
                intent.putExtra("selectedEndMonthIndex", selectedEndMonthIndex);
                startActivityForResult(intent, MYBILL_TO_BILLTIME);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MYBILL_TO_BILLTIME) {//选择时间回来
                startYear = data.getStringExtra("startYear");
                startMonth = data.getStringExtra("startMonth");
                endYear = data.getStringExtra("endYear");
                endMonth = data.getStringExtra("endMonth");
                selectedStartYearIndex = data.getIntExtra("selectedStartYearIndex", -1);
                selectedStartMonthIndex = data.getIntExtra("selectedStartMonthIndex", -1);
                selectedEndYearIndex = data.getIntExtra("selectedEndYearIndex", -1);
                selectedEndMonthIndex = data.getIntExtra("selectedEndMonthIndex", -1);
                tvMybillTime.setText(startYear + "-" + startMonth + "----" + endYear + "-" + endMonth);
                beginDate = startYear + "-" + startMonth;
                endDate = endYear + "-" + endMonth;
                refresh();
            }
        }
    }
}
