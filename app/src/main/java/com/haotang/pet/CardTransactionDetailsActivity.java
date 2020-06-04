package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyBillAdapter;
import com.haotang.pet.entity.GoodsBill;
import com.haotang.pet.entity.MyBill;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CardMenuPopupWindow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * E卡明细界面
 */
public class CardTransactionDetailsActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.btn_cardtrans_submit)
    Button btnCardtransSubmit;
    @BindView(R.id.iv_cardtrans_bg)
    RoundedImageView ivCardtransBg;
    @BindView(R.id.tv_cardtrans_yxq)
    TextView tvCardtransYxq;
    @BindView(R.id.tv_cardtrans_name)
    TextView tvCardtransName;
    @BindView(R.id.tv_cardtrans_balance)
    TextView tvCardtransBalance;
    @BindView(R.id.tv_cardtrans_payout)
    TextView tvCardtransPayout;
    @BindView(R.id.rv_cardtrans)
    RecyclerView rvCardtrans;
    @BindView(R.id.srl_cardtrans)
    SwipeRefreshLayout srlCardtrans;
    @BindView(R.id.tv_cardtrans_ye)
    TextView tvCardtransYe;
    @BindView(R.id.tv_cardtrans_zc)
    TextView tvCardtransZc;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    private int id;
    private String name;
    private String desc;
    private int page = 1;
    private int pageSize;
    private List<MyBill> list = new ArrayList<MyBill>();
    private List<MyBill> localList = new ArrayList<MyBill>();
    private MyBillAdapter myBillAdapter;
    private int useState;
    private int refundable;
    private String reason;
    private int type;
    private int transType;
    private String useText;
    public static SuperActivity act;
    private List<String> menuList = new ArrayList<String>();
    private CardMenuPopupWindow cardMenuPopupWindow;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefundCardEvent event) {
        if (event != null && event.isRefund()) {
            getData();
            refresh();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
        refresh();
    }

    private void getData() {
        mPDialog.showDialog();
        menuList.clear();
        CommUtil.cardRecord(this, id, cardInfoHandler);
    }

    private AsyncHttpResponseHandler cardInfoHandler = new AsyncHttpResponseHandler() {

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
                        JSONObject jdata = jsonObject.getJSONObject("data");
                        if (jdata.has("refundRule") && !jdata.isNull("refundRule")) {
                            JSONObject jrefundRule = jdata.getJSONObject("refundRule");
                            if (jrefundRule.has("refundable") && !jrefundRule.isNull("refundable")) {
                                refundable = jrefundRule.getInt("refundable");
                            }
                            if (jrefundRule.has("reason") && !jrefundRule.isNull("reason")) {
                                reason = jrefundRule.getString("reason");
                            }
                        }
                        if (transType == 1) {
                            if (jdata.has("payOut") && !jdata.isNull("payOut")) {
                                SpannableString ss = new SpannableString("¥" + jdata.getDouble("payOut"));
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style2), 1,
                                        ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                tvCardtransBalance.setText(ss);
                            }
                            if (jdata.has("discountAmount") && !jdata.isNull("discountAmount")) {
                                SpannableString ss = new SpannableString("¥" + jdata.getDouble("discountAmount"));
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style2), 1,
                                        ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                tvCardtransPayout.setText(ss);
                            }
                        } else if (transType == 0) {
                            if (jdata.has("totalAmount") && !jdata.isNull("totalAmount")) {
                                SpannableString ss = new SpannableString("¥" + jdata.getDouble("totalAmount"));
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style2), 1,
                                        ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                tvCardtransBalance.setText(ss);
                            }
                            if (jdata.has("payOut") && !jdata.isNull("payOut")) {
                                SpannableString ss = new SpannableString("¥" + jdata.getDouble("payOut"));
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style2), 1,
                                        ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                tvCardtransPayout.setText(ss);
                            }
                        }
                        if (jdata.has("accountText") && !jdata.isNull("accountText")) {
                            desc = jdata.getString("accountText");
                        }
                        if (jdata.has("mineCardPic") && !jdata.isNull("mineCardPic")) {
                            GlideUtil.loadImg(CardTransactionDetailsActivity.this, jdata.getString("mineCardPic"), ivCardtransBg, R.drawable.icon_production_default);
                        }
                        if (jdata.has("expireTime") && !jdata.isNull("expireTime")) {
                            Utils.setText(tvCardtransYxq, jdata.getString("expireTime") + "", "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("serviceCardTypeName") && !jdata.isNull("serviceCardTypeName")) {
                            name = jdata.getString("serviceCardTypeName");
                            Utils.setText(tvCardtransName, jdata.getString("serviceCardTypeName") + "", "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("useState") && !jdata.isNull("useState")) {
                            useState = jdata.getInt("useState");
                        }
                        if (jdata.has("useText") && !jdata.isNull("useText")) {
                            useText = jdata.getString("useText");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                e.printStackTrace();
            }
            if (transType == 1) {//申请退款界面过来
                tvCardtransYe.setText("支出(元)");
                tvCardtransZc.setText("优惠金额(元)");
                tvTitlebarTitle.setText("退款明细");
                btnCardtransSubmit.setVisibility(View.GONE);
                tvTitlebarOther.setVisibility(View.GONE);
                ibTitlebarOther.setVisibility(View.GONE);
            } else if (transType == 0) {//正常过来
                tvCardtransYe.setText("余额(元)");
                tvCardtransZc.setText("支出(元)");
                tvTitlebarTitle.setText("消费明细");
                if (type == 1) {//可用卡片
                    if (useState == 2) {//余额卡
                        tvTitlebarOther.setVisibility(View.VISIBLE);
                        tvTitlebarOther.setText("使用说明");
                        tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));
                        if (Utils.isStrNull(useText)) {
                            btnCardtransSubmit.setVisibility(View.VISIBLE);
                            Utils.setText(btnCardtransSubmit, useText, "", View.VISIBLE, View.VISIBLE);
                        } else {
                            btnCardtransSubmit.setVisibility(View.GONE);
                        }
                    } else {
                        tvTitlebarOther.setVisibility(View.GONE);
                        btnCardtransSubmit.setVisibility(View.GONE);
                        ibTitlebarOther.setVisibility(View.VISIBLE);
                        ibTitlebarOther.setBackgroundResource(R.drawable.order_right_icon);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 40), Utils.dip2px(mContext, 22));
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                        ibTitlebarOther.setLayoutParams(layoutParams);
                        menuList.add("使用说明");
                        if (Utils.isStrNull(useText)) {
                            menuList.add(useText);
                        }
                    }
                } else if (type == 2) {//不可用卡片
                    tvTitlebarOther.setVisibility(View.GONE);
                    btnCardtransSubmit.setVisibility(View.GONE);
                    ibTitlebarOther.setVisibility(View.VISIBLE);
                    ibTitlebarOther.setBackgroundResource(R.drawable.order_right_icon);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 40), Utils.dip2px(mContext, 22));
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    ibTitlebarOther.setLayoutParams(layoutParams);
                    menuList.add("使用说明");
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void initData() {
        act = this;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        id = getIntent().getIntExtra("id", 0);
        type = getIntent().getIntExtra("type", 0);
        transType = getIntent().getIntExtra("transType", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_card_transaction_details);
        ButterKnife.bind(this);
    }

    private void setView() {
        srlCardtrans.setRefreshing(true);
        srlCardtrans.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvCardtrans.setHasFixedSize(true);
        rvCardtrans.setLayoutManager(new LinearLayoutManager(this));
        myBillAdapter = new MyBillAdapter(R.layout.item_mybill, list, 2);
        rvCardtrans.setAdapter(myBillAdapter);
        rvCardtrans.addItemDecoration(new StickyRecyclerHeadersDecoration(myBillAdapter));
    }

    private void setLinster() {
        myBillAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlCardtrans.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        myBillAdapter.setEnableLoadMore(false);
        srlCardtrans.setRefreshing(true);
        page = 1;
        queryTradeHistory();
    }

    private void loadMore() {
        queryTradeHistory();
    }

    private void queryTradeHistory() {
        mPDialog.showDialog();
        CommUtil.queryTradeHistory(spUtil.getString("cellphone", ""), Global.getIMEI(this), this, page, "", "", id, queryTradeHistory);
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
                        srlCardtrans.setRefreshing(false);
                        myBillAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    myBillAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
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
                            myBillAdapter.setEmptyView(setEmptyViewBase(2, "暂无明细～",
                                    R.drawable.icon_no_mypet, null));
                            myBillAdapter.loadMoreEnd(true);
                        } else {
                            myBillAdapter.loadMoreEnd(false);
                        }
                    }
                    myBillAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        myBillAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        myBillAdapter.setEnableLoadMore(false);
                        srlCardtrans.setRefreshing(false);
                    } else {
                        myBillAdapter.setEnableLoadMore(true);
                        myBillAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    myBillAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    myBillAdapter.setEnableLoadMore(false);
                    srlCardtrans.setRefreshing(false);
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
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                myBillAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                myBillAdapter.setEnableLoadMore(false);
                srlCardtrans.setRefreshing(false);
            } else {
                myBillAdapter.setEnableLoadMore(true);
                myBillAdapter.loadMoreFail();
            }
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

    private void showPop() {
        dismissPop();
        cardMenuPopupWindow = new CardMenuPopupWindow(this,1, menuList, onClickListener);
        cardMenuPopupWindow.showAsDropDown(ibTitlebarOther, -10, -30);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_washorder_xgdd://使用说明
                    Utils.setCardDesc(mContext, name, desc, "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    break;
                case R.id.tv_pop_washorder_sqtk://申请退款
                    if (refundable == 0) {
                        new AlertDialogNavAndPost(CardTransactionDetailsActivity.this)
                                .builder()
                                .setTitle("")
                                .setMsg(reason)
                                .setNegativeButtonVisible(View.GONE)
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    } else {
                        if (Utils.isStrNull(reason)) {
                            new AlertDialogNavAndPost(CardTransactionDetailsActivity.this)
                                    .builder()
                                    .setTitle("")
                                    .setMsg(reason)
                                    .setNegativeButtonVisible(View.GONE)
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(CardTransactionDetailsActivity.this, ApplyForRefundActivity.class);
                                            intent.putExtra("id", id);
                                            intent.putExtra("flag", 1);
                                            startActivity(intent);
                                        }
                                    }).show();
                        } else {
                            Intent intent = new Intent(CardTransactionDetailsActivity.this, ApplyForRefundActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("flag", 1);
                            startActivity(intent);
                        }
                    }
                    break;
            }
            dismissPop();
        }
    };

    private void dismissPop() {
        if (cardMenuPopupWindow != null) {
            if (cardMenuPopupWindow.isShowing()) {
                cardMenuPopupWindow.dismiss();
            }
        }
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other, R.id.btn_cardtrans_submit, R.id.ib_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_other:
                showPop();
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                Utils.setCardDesc(mContext, name, desc, "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case R.id.btn_cardtrans_submit:
                startActivity(new Intent(CardTransactionDetailsActivity.this, BalanceExchangeActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
