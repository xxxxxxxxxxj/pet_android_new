package com.haotang.pet;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ShopMallLogisticsAdapter;
import com.haotang.pet.adapter.ShopMallRecommendAdapter;
import com.haotang.pet.entity.ShopMallFragRecommend;
import com.haotang.pet.entity.ShopMallLogistics;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshHeadGridView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.HeaderGridView;
import com.haotang.pet.view.TimeLineDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.haotang.pet.view.TimeLineDecoration.BEGIN;
import static com.haotang.pet.view.TimeLineDecoration.END;
import static com.haotang.pet.view.TimeLineDecoration.NORMAL;

/**
 * 物流详情页
 */
public class LogisticsDetailActivity extends SuperActivity implements
        View.OnClickListener {
    private MProgressDialog pDialog;
    private ImageButton ibBack;
    private TextView tvTitle;
    private LinearLayout ll_logisticsdetail_nonet;
    private Button btn_logisticsdetail_nonet;
    private LinearLayout ll_logisticsdetail;
    private ImageView img_scroll_top;
    private PullToRefreshHeadGridView ptrhgv_logisticsdetail;
    private int page = 1;
    private boolean isGridScroll;
    private List<ShopMallFragRecommend> listRecommend = new ArrayList<ShopMallFragRecommend>();
    private ShopMallRecommendAdapter<ShopMallFragRecommend> shopMallRecommendAdapter;
    private View header;
    private TextView tv_header_logisticsdetail_wlgs;
    private Button btn_item_logisticsdetail_ordercopy;
    private TextView tv_header_logisticsdetail_wldh;
    private RecyclerView rv_header_logisticsdetail;
    private TextView tv_header_logisticsdetail_nologistics;
    private LinearLayout ll_header_logisticsdetail_recommend;
    private List<ShopMallLogistics> listLogistics = new ArrayList<ShopMallLogistics>();
    private ShopMallLogisticsAdapter shopMallLogisticsAdapter;
    private String company;
    private String logisticsNumber;
    private String noLogistics;
    private int logisticsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
        getData();
        getrRecommendData();
    }

    private void init() {
        MApplication.listAppoint.add(this);
        pDialog = new MProgressDialog(this);
        Intent intent = getIntent();
        logisticsId = intent.getIntExtra("logisticsId", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_logistics_detail);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        img_scroll_top = (ImageView) findViewById(R.id.img_scroll_top);
        ptrhgv_logisticsdetail = (PullToRefreshHeadGridView) findViewById(R.id.ptrhgv_logisticsdetail);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        header = LayoutInflater.from(this).inflate(R.layout.header_logisticsdetail, null);
        tv_header_logisticsdetail_wlgs = (TextView) header.findViewById(R.id.tv_header_logisticsdetail_wlgs);
        btn_item_logisticsdetail_ordercopy = (Button) header.findViewById(R.id.btn_item_logisticsdetail_ordercopy);
        tv_header_logisticsdetail_wldh = (TextView) header.findViewById(R.id.tv_header_logisticsdetail_wldh);
        rv_header_logisticsdetail = (RecyclerView) header.findViewById(R.id.rv_header_logisticsdetail);
        tv_header_logisticsdetail_nologistics = (TextView) header.findViewById(R.id.tv_header_logisticsdetail_nologistics);
        ll_header_logisticsdetail_recommend = (LinearLayout) header.findViewById(R.id.ll_header_logisticsdetail_recommend);
        ll_logisticsdetail_nonet = (LinearLayout) header.findViewById(R.id.ll_logisticsdetail_nonet);
        btn_logisticsdetail_nonet = (Button) header.findViewById(R.id.btn_logisticsdetail_nonet);
        ll_logisticsdetail = (LinearLayout) header.findViewById(R.id.ll_logisticsdetail);
    }

    private void setView() {
        img_scroll_top.bringToFront();
        tvTitle.setText("物流详情");
        ptrhgv_logisticsdetail.getRefreshableView().addHeaderView(header);
        ptrhgv_logisticsdetail.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        listLogistics.clear();
        rv_header_logisticsdetail.setLayoutManager(new LinearLayoutManager(this));
        rv_header_logisticsdetail.setHasFixedSize(true);
        rv_header_logisticsdetail.setNestedScrollingEnabled(false);
        rv_header_logisticsdetail.setItemAnimator(new DefaultItemAnimator());
        final TimeLineDecoration decoration = new TimeLineDecoration(this)
                .setLineColor(R.color.aDDDDDD)
                .setLineWidth(1)
                .setLeftDistance(20)
                .setTopDistance(15)
                .setBeginMarker(R.drawable.begin_marker)
                .setMarkerRadius(4)
                .setMarkerColor(R.color.aDDDDDD)
                .setCallback(new TimeLineDecoration.TimeLineAdapter() {

                    @Nullable
                    @Override
                    public Rect getRect(int position) {
                        return new Rect(0, 0, 0, 0);
                    }

                    @Override
                    public int getTimeLineType(int position) {
                        if (position == 0) {
                            return BEGIN;
                        } else if (position == shopMallLogisticsAdapter.getItemCount() - 1) {
//                            return END_FULL;
                            return END;
                        } else {
                            return NORMAL;
                        }
                    }
                });
        rv_header_logisticsdetail.addItemDecoration(decoration);
        shopMallLogisticsAdapter = new ShopMallLogisticsAdapter(this);
        rv_header_logisticsdetail.setAdapter(shopMallLogisticsAdapter);

        listRecommend.clear();
        shopMallRecommendAdapter = new ShopMallRecommendAdapter<ShopMallFragRecommend>(this, listRecommend);
        shopMallRecommendAdapter.clearDeviceList();
        ptrhgv_logisticsdetail.setAdapter(shopMallRecommendAdapter);
    }

    private void setLinster() {
        ibBack.setOnClickListener(this);
        img_scroll_top.setOnClickListener(this);
        btn_logisticsdetail_nonet.setOnClickListener(this);
        btn_item_logisticsdetail_ordercopy.setOnClickListener(this);
        ptrhgv_logisticsdetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 0 && firstVisibleItem <= 10) {
                    isGridScroll = false;
                    img_scroll_top.setVisibility(View.GONE);
                } else {
                    isGridScroll = true;
                    img_scroll_top.setVisibility(View.VISIBLE);
                }
            }
        });
        ptrhgv_logisticsdetail
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshBase refreshView) {
                        PullToRefreshBase.Mode mode = refreshView
                                .getCurrentMode();
                        if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {// 下拉刷新
                            page = 1;
                            getData();
                            getrRecommendData();
                        } else {// 上拉加载更多
                            page++;
                            getrRecommendData();
                        }
                    }
                });
    }

    private void getData() {
        listLogistics.clear();
        pDialog.showDialog();
        CommUtil.getLogisticsData(this, logisticsId, getLogisticsDataHandler);
    }

    private AsyncHttpResponseHandler getLogisticsDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                ll_logisticsdetail.setVisibility(View.VISIBLE);
                ll_logisticsdetail_nonet.setVisibility(View.GONE);
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("company") && !jdata.isNull("company")) {
                                company = jdata.getString("company");
                            }
                            if (jdata.has("logisticsNumber") && !jdata.isNull("logisticsNumber")) {
                                logisticsNumber = jdata.getString("logisticsNumber");
                            }
                            if (jdata.has("noLogistics") && !jdata.isNull("noLogistics")) {
                                noLogistics = jdata.getString("noLogistics");
                            }
                            if (jdata.has("information") && !jdata.isNull("information")) {
                                JSONArray jarrlogisticsList = jdata.getJSONArray("information");
                                if (jarrlogisticsList.length() > 0) {
                                    for (int i = 0; i < jarrlogisticsList.length(); i++) {
                                        listLogistics.add(ShopMallLogistics.json2Entity(jarrlogisticsList
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                ll_logisticsdetail.setVisibility(View.GONE);
                ll_logisticsdetail_nonet.setVisibility(View.VISIBLE);
                e.printStackTrace();
                ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "数据异常");
            }
            writeData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ll_logisticsdetail.setVisibility(View.GONE);
            ll_logisticsdetail_nonet.setVisibility(View.VISIBLE);
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "请求失败");
        }
    };

    private void getrRecommendData() {
        if (page == 1) {
            listRecommend.clear();
            shopMallRecommendAdapter.clearDeviceList();
        }
        pDialog.showDialog();
        CommUtil.getrRecommendData(this, 0, page, 0, getrRecommendDataHandler);
    }

    private AsyncHttpResponseHandler getrRecommendDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            ptrhgv_logisticsdetail.onRefreshComplete();
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("commodityList") && !jdata.isNull("commodityList")) {
                                JSONArray jarrcommodityList = jdata.getJSONArray("commodityList");
                                if (jarrcommodityList.length() > 0) {
                                    if (page == 1) {
                                        listRecommend.clear();
                                        shopMallRecommendAdapter.clearDeviceList();
                                    }
                                    for (int i = 0; i < jarrcommodityList.length(); i++) {
                                        listRecommend.add(ShopMallFragRecommend.json2Entity(jarrcommodityList
                                                .getJSONObject(i)));
                                    }
                                } else {
                                    if (page > 1) {
                                        ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "没有更多数据了");
                                    }
                                }
                            } else {
                                if (page > 1) {
                                    ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "没有更多数据了");
                                }
                            }
                        } else {
                            if (page > 1) {
                                ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "没有更多数据了");
                            }
                        }
                    } else {
                        if (page > 1) {
                            ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "没有更多数据了");
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "物流详情加载更多e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "数据异常");
            }
            writeData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ptrhgv_logisticsdetail.onRefreshComplete();
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(LogisticsDetailActivity.this, "请求失败");
        }
    };

    private void writeData() {
        if (Utils.isStrNull(company)) {
            SpannableString ss1 = new SpannableString("物流公司：" + company);
            ss1.setSpan(
                    new ForegroundColorSpan(getResources().getColor(
                            R.color.a333333)), "物流公司：".length(), ss1.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_header_logisticsdetail_wlgs.setText(ss1);
        } else {
            tv_header_logisticsdetail_wlgs.setText("物流公司：");
        }
        if (Utils.isStrNull(logisticsNumber)) {
            btn_item_logisticsdetail_ordercopy.setVisibility(View.VISIBLE);
            SpannableString ss1 = new SpannableString("物流单号：" + logisticsNumber);
            ss1.setSpan(
                    new ForegroundColorSpan(getResources().getColor(
                            R.color.a333333)), "物流单号：".length(), ss1.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_header_logisticsdetail_wldh.setText(ss1);
        } else {
            btn_item_logisticsdetail_ordercopy.setVisibility(View.INVISIBLE);
            tv_header_logisticsdetail_wldh.setText("物流单号：");
        }
        if (listLogistics != null && listLogistics.size() > 0) {
            rv_header_logisticsdetail.setVisibility(View.VISIBLE);
            tv_header_logisticsdetail_nologistics.setVisibility(View.GONE);
            shopMallLogisticsAdapter.setItems(listLogistics);
        } else {
            rv_header_logisticsdetail.setVisibility(View.GONE);
            tv_header_logisticsdetail_nologistics.setVisibility(View.VISIBLE);
            tv_header_logisticsdetail_nologistics.setText(noLogistics);
        }
        if (listRecommend != null && listRecommend.size() > 0) {
            ll_header_logisticsdetail_recommend.setVisibility(View.VISIBLE);
            if (listRecommend.size() == 1) {
                ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(0);
                if (shopMallFragRecommend != null) {
                    if (shopMallFragRecommend.isOther()) {
                        ll_header_logisticsdetail_recommend.setVisibility(View.GONE);
                    }
                }
            }
            shopMallRecommendAdapter.setData(listRecommend);
        } else {
            if (page == 1) {
                listRecommend.add(new ShopMallFragRecommend("追加", 0, 0, "", 0, 0, true));
                shopMallRecommendAdapter.setData(listRecommend);
            }
            ll_header_logisticsdetail_recommend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_item_logisticsdetail_ordercopy:
                if (Utils.isStrNull(logisticsNumber)) {
                    Utils.copy(logisticsNumber, this);
                    ToastUtil.showToastShortBottom(this, "快递单号已复制");
                }
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_logisticsdetail_nonet:
                page = 1;
                getData();
                getrRecommendData();
                break;
            case R.id.img_scroll_top:
                if (isGridScroll) {
                    HeaderGridView refreshableView = ptrhgv_logisticsdetail.getRefreshableView();
                    if (!(refreshableView).isStackFromBottom()) {
                        refreshableView.setStackFromBottom(true);
                    }
                    refreshableView.setStackFromBottom(false);
                }
                break;
            default:
                break;
        }
    }
}
