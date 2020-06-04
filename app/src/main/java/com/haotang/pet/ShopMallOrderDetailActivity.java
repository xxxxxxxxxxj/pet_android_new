package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ShopMallOrderDetailGoodAdapter;
import com.haotang.pet.adapter.ShopMallRecommendAdapter;
import com.haotang.pet.adapter.ShopRefundItemsAdapter;
import com.haotang.pet.adapter.WashOrderDetailDiscountAdapter;
import com.haotang.pet.adapter.WashOrderDetailPayPricesItemAdapter;
import com.haotang.pet.entity.CancelReasonBean;
import com.haotang.pet.entity.GoodsAddEvent;
import com.haotang.pet.entity.MallOrderDetailGoodItems;
import com.haotang.pet.entity.MallOrderDetailGoods;
import com.haotang.pet.entity.PayPricesItem;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.entity.ShopMallFragRecommend;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.WashDiscount;
import com.haotang.pet.entity.mallEntity.MallGoods;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshHeadGridView;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.AlertDialogMall;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.HeaderGridView;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.MallOrderPopupWindow;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 商品订单详情
 */
public class ShopMallOrderDetailActivity extends SuperActivity implements
        View.OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private Button btn_shopmallorderdetail_submit;
    private PullToRefreshHeadGridView ptrhgv_shopmallorderdetail;
    private int page = 1;
    private View header;
    private int orderId;
    private LinearLayout ll_header_shopmallorderdetail_ywc;
    private LinearLayout ll_header_shopmallorderdetail_yfk;
    private LinearLayout ll_header_shopmallorderdetail_dfk;
    private TextView tv_header_shopmallorderdetail_dfk_fen;
    private TextView tv_header_shopmallorderdetail_dfk_miao;
    private LinearLayout ll_header_shopmallorderdetail_psz;
    private TextView tv_header_shopmallorderdetail_psinfo;
    private TextView tv_header_shopmallorderdetail_phone;
    private TextView tv_header_shopmallorderdetail_lxr;
    private TextView tv_header_shopmallorderdetail_add;
    private MListview mlv_header_shopmallorderdetail;
    private TextView tv_header_shopmallorderdetail_totalprice;
    private TextView tv_header_shopmallorderdetail_yunfei;
    private TextView tv_header_shopmallorderdetail_payprice;
    private Button btn_header_shopmallorderdetail_ordercopy;
    private TextView tv_header_shopmallorderdetail_ordernum;
    private TextView tv_header_shopmallorderdetail_paytime;
    private TextView tv_header_shopmallorderdetail_payway;
    private Button btn_header_shopmallorderdetail_bddh;
    private TextView tv_header_shopmallorderdetail_shdh;
    private String stateDesc;
    private List<ShopMallFragRecommend> listRecommend = new ArrayList<ShopMallFragRecommend>();
    private ShopMallRecommendAdapter<ShopMallFragRecommend> shopMallRecommendAdapter;
    private List<MallOrderDetailGoods> listMallOrderDetailGoods = new ArrayList<MallOrderDetailGoods>();
    private List<MallOrderDetailGoodItems> listMallOrderDetailGoodItems = new ArrayList<MallOrderDetailGoodItems>();
    private int lastSecs;
    private String consigner;
    private String mobile;
    private double freight;
    private double payPrice;
    private String tradeNo;
    private String payTime;
    private String payWay;
    private String hotline;
    private String payedDetailTag;
    private int state = -2;
    private TextView tv_header_shopmallorderdetail_dfk;
    private TextView tv_header_shopmallorderdetail_yfk;
    private TextView tv_header_shopmallorderdetail_psz;
    private TextView tv_header_shopmallorderdetail_jywc;
    private TextView tv_header_shopmallorderdetail_yfkstr;
    private ShopMallOrderDetailGoodAdapter<MallOrderDetailGoodItems> shopMallOrderDetailGoodAdapter;
    private ShopRefundItemsAdapter<MallOrderDetailGoodItems> shopRefundItemsAdapter;
    private Timer timer;
    private TimerTask task;
    private Timer timer1;
    private TimerTask task1;
    private LinearLayout ll_header_shopmallorderdetail_recommend;
    private LinearLayout ll_header_shopmallorderdetail_yqx;
    private TextView tv_header_shopmallorderdetail_yqx;
    private boolean isGridScroll;
    private boolean isTimerDown = false;
    private ImageView img_scroll_top;
    private List<MallOrderDetailGoodItems> listRefundItems = new ArrayList<MallOrderDetailGoodItems>();
    private LinearLayout rl_header_shopmallRefundItems;
    private MListview mlv_header_shopmallRefundItems;
    private TextView tv_header_shopmallorderdetail_neigou;
    private String insideCopy;
    private int paytype;
    private int myselfDummpNum = -1;
    private int addressId;
    private String freightTag;
    private String freightNoTag;
    private TextView textview_mall_express_tag;
    private String addressCopy;
    private View vw_header_shopmallorderdetail;
    private ImageButton ibTitlebarOther;
    private MallOrderPopupWindow mallOrderPopupWindow;
    private List<CancelReasonBean> reasonList = new ArrayList<CancelReasonBean>();
    private List<CancelReasonBean> returnReasonList = new ArrayList<CancelReasonBean>();
    private ArrayList<String> tipList = new ArrayList<String>();
    private StringBuilder listpayWays = new StringBuilder();
    private RelativeLayout rl_commodity_black;
    private String minute = "";
    private String second = "";
    private TextView tv_pay_bottomdia_time_second;
    private TextView tv_pay_bottomdia_time_minute;
    private PopupWindow pWinBottomDialog;
    private int oldpayway;
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private List<WashDiscount> localDiscountList = new ArrayList<WashDiscount>();
    private ArrayList<String> discountList = new ArrayList<String>();
    private RecyclerView rv_mallorderdetail_payway;
    private RecyclerView rv_mallorderdetail_discount;
    private List<PayPricesItem> payPricesItemList = new ArrayList<PayPricesItem>();
    private WashOrderDetailDiscountAdapter washOrderDetailDiscountAdapter;
    private WashOrderDetailPayPricesItemAdapter washOrderDetailPayPricesItemAdapter;
    private int cardId;
    private double reserveDiscount;
    private String promoterCode;
    private int couponId;
    private double couponAmount;
    private String returnRule;
    private int reduceType;
    private String returnTips;
    private double mixThirdPrice;
    private double goodsPrice;
    private TextView tv_shopmallorderdetail_price;
    private RelativeLayout rl_shopmallorderdetail_dfk;
    private LinearLayout ll_shopmallorderdetail_price;
    private int orderExamineState;
    private int isReturnTime;

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    long lastTimerShowHMS = msg.arg1;
                    String time = Utils.formatTimeFM(lastTimerShowHMS);
                    String minute = time.substring(0, 2);
                    String second = time.substring(3, 5);
                    tv_header_shopmallorderdetail_dfk_fen.setText(minute);
                    tv_header_shopmallorderdetail_dfk_miao.setText(second);
                    if (tv_pay_bottomdia_time_minute != null && tv_pay_bottomdia_time_second != null) {
                        tv_pay_bottomdia_time_minute.setText(minute);
                        tv_pay_bottomdia_time_second.setText(second);
                    }
                    break;
                case 1:
                    if (lastSecs <= 0) {
                        ToastUtil.showToastShortBottom(mContext, "抱歉您的订单已超时");
                    }
                    finish();
                    break;
                case 2:
                    long lastTimerShowHMS1 = msg.arg1;
                    String time1 = Utils.formatDuring(lastTimerShowHMS1);
                    String days = time1.substring(0, 2);
                    String hours = time1.substring(3, 5);
                    String minutes = time1.substring(6, 8);
                    tv_header_shopmallorderdetail_psinfo.setText("还剩" + days + "天" + hours + "时" + minutes + "分自动确认收货");
                    break;
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    if (TextUtils.equals(resultStatus, "9000")) {
                        goPaySuccess();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(ShopMallOrderDetailActivity.this, orderStr,
                            aliHandler, mPDialog);
                    break;
            }
        }
    };

    //微信支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayResultEvent event) {
        if (event != null) {
            BaseResp resp = event.getResp();
            if (resp != null) {
                if (resp.errCode == 0) {
                    if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1")) {
                        Log.e("TAG", "OPPO哦");
                    } else {
                        goPaySuccess();
                    }
                } else {
                    if (Utils.isStrNull(resp.errStr)) {
                        ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "支付失败");
                    }
                }
            }
        }
    }

    //刷新订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshOrderEvent event) {
        if (event != null && event.isRefresh()) {
            getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_mall_order_detail);
        init();
        findView();
        setView();
        setLinster();
        getData();
        getrRecommendData();
    }

    private void getrRecommendData() {
        if (page == 1) {
            listRecommend.clear();
            shopMallRecommendAdapter.clearDeviceList();
        }
        mPDialog.showDialog();
        CommUtil.getrRecommendData(this, 0, page, 0, getrRecommendDataHandler);
    }

    private AsyncHttpResponseHandler getrRecommendDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            ptrhgv_shopmallorderdetail.onRefreshComplete();
            mPDialog.closeDialog();
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
                                        ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "没有更多数据了");
                                    }
                                }
                            } else {
                                if (page > 1) {
                                    ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "没有更多数据了");
                                }
                            }
                        } else {
                            if (page > 1) {
                                ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "没有更多数据了");
                            }
                        }
                    } else {
                        if (page > 1) {
                            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "没有更多数据了");
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "订单详情加载更多e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "数据异常");
            }
            isTimerDown = true;
            writeData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ptrhgv_shopmallorderdetail.onRefreshComplete();
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "请求失败");
        }
    };

    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        Global.WXPAYCODE = -1;
        oldpayway = spUtil.getInt("payway", 0);
        isTimerDown = false;
        MApplication.listAppoint.add(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", 0);
    }

    private void findView() {
        rl_commodity_black = (RelativeLayout) findViewById(R.id.rl_commodity_black);
        rl_shopmallorderdetail_dfk = (RelativeLayout) findViewById(R.id.rl_shopmallorderdetail_dfk);
        ll_shopmallorderdetail_price = (LinearLayout) findViewById(R.id.ll_shopmallorderdetail_price);
        tv_shopmallorderdetail_price = (TextView) findViewById(R.id.tv_shopmallorderdetail_price);
        img_scroll_top = (ImageView) findViewById(R.id.img_scroll_top);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        ibTitlebarOther = (ImageButton) findViewById(R.id.ib_titlebar_other);
        btn_shopmallorderdetail_submit = (Button) findViewById(R.id.btn_shopmallorderdetail_submit);
        ptrhgv_shopmallorderdetail = (PullToRefreshHeadGridView) findViewById(R.id.ptrhgv_shopmallorderdetail);

        header = LayoutInflater.from(this).inflate(R.layout.header_shopmallorderdetail, null);
        rv_mallorderdetail_discount = (RecyclerView) header.findViewById(R.id.rv_mallorderdetail_discount);
        rv_mallorderdetail_payway = (RecyclerView) header.findViewById(R.id.rv_mallorderdetail_payway);
        vw_header_shopmallorderdetail = (View) header.findViewById(R.id.vw_header_shopmallorderdetail);
        textview_mall_express_tag = (TextView) header.findViewById(R.id.textview_mall_express_tag);
        tv_header_shopmallorderdetail_neigou = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_neigou);
        ll_header_shopmallorderdetail_recommend = (LinearLayout) header.findViewById(R.id.ll_header_shopmallorderdetail_recommend);
        ll_header_shopmallorderdetail_ywc = (LinearLayout) header.findViewById(R.id.ll_header_shopmallorderdetail_ywc);
        ll_header_shopmallorderdetail_yfk = (LinearLayout) header.findViewById(R.id.ll_header_shopmallorderdetail_yfk);
        ll_header_shopmallorderdetail_dfk = (LinearLayout) header.findViewById(R.id.ll_header_shopmallorderdetail_dfk);
        tv_header_shopmallorderdetail_dfk_fen = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_dfk_fen);
        tv_header_shopmallorderdetail_dfk_miao = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_dfk_miao);
        ll_header_shopmallorderdetail_psz = (LinearLayout) header.findViewById(R.id.ll_header_shopmallorderdetail_psz);
        tv_header_shopmallorderdetail_psinfo = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_psinfo);
        tv_header_shopmallorderdetail_phone = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_phone);
        tv_header_shopmallorderdetail_lxr = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_lxr);
        tv_header_shopmallorderdetail_add = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_add);
        mlv_header_shopmallorderdetail = (MListview) header.findViewById(R.id.mlv_header_shopmallorderdetail);
        tv_header_shopmallorderdetail_totalprice = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_totalprice);
        tv_header_shopmallorderdetail_yunfei = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_yunfei);
        tv_header_shopmallorderdetail_payprice = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_payprice);
        btn_header_shopmallorderdetail_ordercopy = (Button) header.findViewById(R.id.btn_header_shopmallorderdetail_ordercopy);
        tv_header_shopmallorderdetail_ordernum = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_ordernum);
        tv_header_shopmallorderdetail_paytime = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_paytime);
        tv_header_shopmallorderdetail_payway = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_payway);
        btn_header_shopmallorderdetail_bddh = (Button) header.findViewById(R.id.btn_header_shopmallorderdetail_bddh);
        tv_header_shopmallorderdetail_shdh = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_shdh);
        tv_header_shopmallorderdetail_dfk = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_dfk);
        tv_header_shopmallorderdetail_yfk = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_yfk);
        tv_header_shopmallorderdetail_psz = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_psz);
        tv_header_shopmallorderdetail_jywc = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_jywc);
        tv_header_shopmallorderdetail_yfkstr = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_yfkstr);
        ll_header_shopmallorderdetail_yqx = (LinearLayout) header.findViewById(R.id.ll_header_shopmallorderdetail_yqx);
        tv_header_shopmallorderdetail_yqx = (TextView) header.findViewById(R.id.tv_header_shopmallorderdetail_yqx);
        rl_header_shopmallRefundItems = (LinearLayout) header.findViewById(R.id.rl_header_shopmallRefundItems);
        mlv_header_shopmallRefundItems = (MListview) header.findViewById(R.id.mlv_header_shopmallRefundItems);
    }

    private void setView() {
        ibTitlebarOther.setBackgroundResource(R.drawable.order_right_icon);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 40), Utils.dip2px(mContext, 22));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ibTitlebarOther.setLayoutParams(layoutParams);

        img_scroll_top.bringToFront();
        tvTitle.setText("订单详情");
        ptrhgv_shopmallorderdetail.getRefreshableView().addHeaderView(header);
        ptrhgv_shopmallorderdetail.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        listMallOrderDetailGoodItems.clear();
        shopMallOrderDetailGoodAdapter = new ShopMallOrderDetailGoodAdapter<MallOrderDetailGoodItems>(this, listMallOrderDetailGoodItems);
        shopMallOrderDetailGoodAdapter.clearDeviceList();
        mlv_header_shopmallorderdetail.setAdapter(shopMallOrderDetailGoodAdapter);

        listRecommend.clear();
        shopMallRecommendAdapter = new ShopMallRecommendAdapter<ShopMallFragRecommend>(this, listRecommend);
        shopMallRecommendAdapter.clearDeviceList();
        ptrhgv_shopmallorderdetail.setAdapter(shopMallRecommendAdapter);

        listRefundItems.clear();
        shopRefundItemsAdapter = new ShopRefundItemsAdapter<MallOrderDetailGoodItems>(this, listRefundItems);
        shopRefundItemsAdapter.clearDeviceList();
        mlv_header_shopmallRefundItems.setAdapter(shopRefundItemsAdapter);

        rv_mallorderdetail_discount.setHasFixedSize(true);
        rv_mallorderdetail_discount.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rv_mallorderdetail_discount.setLayoutManager(noScollFullLinearLayoutManager);
        washOrderDetailDiscountAdapter = new WashOrderDetailDiscountAdapter(R.layout.item_washorderdetail_discount, localDiscountList);
        rv_mallorderdetail_discount.setAdapter(washOrderDetailDiscountAdapter);

        rv_mallorderdetail_payway.setHasFixedSize(true);
        rv_mallorderdetail_payway.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager1 = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager1.setScrollEnabled(false);
        rv_mallorderdetail_payway.setLayoutManager(noScollFullLinearLayoutManager1);
        washOrderDetailPayPricesItemAdapter = new WashOrderDetailPayPricesItemAdapter(R.layout.item_washorderdetail_paypriceitem, payPricesItemList);
        rv_mallorderdetail_payway.setAdapter(washOrderDetailPayPricesItemAdapter);
    }

    private void setLinster() {
        ptrhgv_shopmallorderdetail.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        ptrhgv_shopmallorderdetail
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
        img_scroll_top.setOnClickListener(this);
        ibTitlebarOther.setOnClickListener(this);
        btn_shopmallorderdetail_submit.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        btn_header_shopmallorderdetail_bddh.setOnClickListener(this);
        btn_header_shopmallorderdetail_ordercopy.setOnClickListener(this);
    }

    private void getData() {
        localDiscountList.clear();
        payPricesItemList.clear();
        listMallOrderDetailGoods.clear();
        listMallOrderDetailGoodItems.clear();
        reasonList.clear();
        tipList.clear();
        returnReasonList.clear();
        couponId = 0;
        cardId = 0;
        couponAmount = 0;
        reduceType = 0;
        reserveDiscount = 0;
        mixThirdPrice = 0;
        orderExamineState = 0;
        isReturnTime = 0;
        goodsPrice = 0;
        state = -2;
        promoterCode = "";
        returnRule = "";
        returnTips = "";
        shopMallOrderDetailGoodAdapter.clearDeviceList();
        mPDialog.showDialog();
        CommUtil.mallOrderInfo(mContext, orderId, mallOrderInfoHandler);
    }

    private AsyncHttpResponseHandler mallOrderInfoHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata != null) {
                        if (jdata.has("returnReason") && !jdata.isNull("returnReason")) {
                            JSONObject returnReason = jdata.getJSONObject("returnReason");
                            if (returnReason.has("reasons") && !returnReason.isNull("reasons")) {
                                JSONArray jarrreasons = returnReason.getJSONArray("reasons");
                                if (jarrreasons != null && jarrreasons.length() > 0) {
                                    for (int i = 0; i < jarrreasons.length(); i++) {
                                        returnReasonList.add(new CancelReasonBean(i + 1, jarrreasons.getString(i), false));
                                    }
                                }
                            }

                        }
                        if (jdata.has("returnTips") && !jdata.isNull("returnTips")) {
                            returnTips = jdata.getString("returnTips");
                        }
                        if (jdata.has("returnRule") && !jdata.isNull("returnRule")) {
                            returnRule = jdata.getString("returnRule");
                        }
                        if (jdata.has("promoterCode") && !jdata.isNull("promoterCode")) {
                            promoterCode = jdata.getString("promoterCode");
                        }
                        if (jdata.has("isReturnTime") && !jdata.isNull("isReturnTime")) {
                            isReturnTime = jdata.getInt("isReturnTime");
                        }
                        if (jdata.has("goodsPrice") && !jdata.isNull("goodsPrice")) {
                            goodsPrice = jdata.getDouble("goodsPrice");
                        }
                        if (jdata.has("orderExamineState") && !jdata.isNull("orderExamineState")) {
                            orderExamineState = jdata.getInt("orderExamineState");
                        }
                        if (jdata.has("payPrices") && !jdata.isNull("payPrices")) {
                            JSONArray jarrpayPrices = jdata.getJSONArray("payPrices");
                            if (jarrpayPrices.length() > 0) {
                                for (int i = 0; i < jarrpayPrices.length(); i++) {
                                    PayPricesItem payPricesItem = new PayPricesItem();
                                    JSONObject jpayPrices = jarrpayPrices.getJSONObject(i);
                                    if (jpayPrices.has("payWayDesc") && !jpayPrices.isNull("payWayDesc")) {
                                        payPricesItem.setPayWayDesc(jpayPrices.getString("payWayDesc"));
                                    }
                                    if (jpayPrices.has("amount") && !jpayPrices.isNull("amount")) {
                                        payPricesItem.setAmount(jpayPrices.getString("amount"));
                                    }
                                    payPricesItemList.add(payPricesItem);
                                }
                            }
                        }
                        if (jdata.has("serviceCardId") && !jdata.isNull("serviceCardId")) {
                            cardId = jdata.getInt("serviceCardId");
                        }
                        if (jdata.has("mixThirdPrice") && !jdata.isNull("mixThirdPrice")) {
                            mixThirdPrice = jdata.getDouble("mixThirdPrice");
                        }
                        if (jdata.has("reserveDiscount") && !jdata.isNull("reserveDiscount")) {
                            reserveDiscount = jdata.getDouble("reserveDiscount");
                        }
                        if (jdata.has("coupon") && !jdata.isNull("coupon")) {
                            JSONObject object = jdata.getJSONObject("coupon");
                            if (object.has("reduceType") && !object.isNull("reduceType")) {
                                reduceType = object.getInt("reduceType");
                            }
                        }
                        if (jdata.has("couponId") && !jdata.isNull("couponId")) {
                            couponId = jdata.getInt("couponId");
                        }
                        if (jdata.has("couponDiscountAmount") && !jdata.isNull("couponDiscountAmount")) {
                            couponAmount = jdata.getDouble("couponDiscountAmount");
                        }
                        if (jdata.has("cancelReason") && !jdata.isNull("cancelReason")) {
                            JSONObject jcancelReason = jdata.getJSONObject("cancelReason");
                            if (jcancelReason.has("reasons") && !jcancelReason.isNull("reasons")) {
                                JSONArray jarrreasons = jcancelReason.getJSONArray("reasons");
                                if (jarrreasons != null && jarrreasons.length() > 0) {
                                    for (int i = 0; i < jarrreasons.length(); i++) {
                                        reasonList.add(new CancelReasonBean(i + 1, jarrreasons.getString(i), false));
                                    }
                                }
                            }
                            if (jcancelReason.has("tips") && !jcancelReason.isNull("tips")) {
                                JSONArray jarrrtips = jcancelReason.getJSONArray("tips");
                                if (jarrrtips != null && jarrrtips.length() > 0) {
                                    for (int i = 0; i < jarrrtips.length(); i++) {
                                        tipList.add(jarrrtips.getString(i));
                                    }
                                }
                            }
                        }
                        if (jdata.has("addressCopy") && !jdata.isNull("addressCopy")) {
                            addressCopy = jdata.getString("addressCopy");
                        }
                        if (jdata.has("address") && !jdata.isNull("address")) {
                            JSONObject joaddress = jdata.getJSONObject("address");
                            if (joaddress.has("id") && !joaddress.isNull("id")) {
                                addressId = joaddress.getInt("id");
                            }
                            if (joaddress.has("consigner") && !joaddress.isNull("consigner")) {
                                consigner = joaddress.getString("consigner");
                            }
                            if (joaddress.has("mobile") && !joaddress.isNull("mobile")) {
                                mobile = joaddress.getString("mobile");
                            }
                        }
                        if (jdata.has("insideCopy") && !jdata.isNull("insideCopy")) {
                            insideCopy = jdata.getString("insideCopy");
                        }
                        if (jdata.has("freightTag") && !jdata.isNull("freightTag")) {
                            freightTag = jdata.getString("freightTag");
                        }
                        if (jdata.has("freightNoTag") && !jdata.isNull("freightNoTag")) {
                            freightNoTag = jdata.getString("freightNoTag");
                        }
                        if (jdata.has("freight") && !jdata.isNull("freight")) {
                            freight = jdata.getDouble("freight");
                        }
                        if (jdata.has("payPrice") && !jdata.isNull("payPrice")) {
                            payPrice = jdata.getDouble("payPrice");
                        }
                        if (jdata.has("tradeNo") && !jdata.isNull("tradeNo")) {
                            tradeNo = jdata.getString("tradeNo");
                        }
                        if (jdata.has("payTime") && !jdata.isNull("payTime")) {
                            payTime = jdata.getString("payTime");
                        }
                        if (jdata.has("payWay") && !jdata.isNull("payWay")) {
                            payWay = jdata.getString("payWay");
                        }
                        if (jdata.has("hotline") && !jdata.isNull("hotline")) {
                            hotline = jdata.getString("hotline");
                        }
                        if (jdata.has("payedDetailTag") && !jdata.isNull("payedDetailTag")) {
                            payedDetailTag = jdata.getString("payedDetailTag");
                        }
                        if (jdata.has("state") && !jdata.isNull("state")) {
                            state = jdata.getInt("state");
                        }
                        if (jdata.has("lastSecs") && !jdata.isNull("lastSecs")) {
                            lastSecs = jdata.getInt("lastSecs") * 1000;
                        }
                        if (jdata.has("stateDesc") && !jdata.isNull("stateDesc")) {
                            stateDesc = jdata.getString("stateDesc");
                        }
                        if (jdata.has("refundItems") && !jdata.isNull("refundItems")) {
                            JSONArray jarrrrefundItems = jdata.getJSONArray("refundItems");
                            if (jarrrrefundItems.length() > 0) {
                                listRefundItems.clear();
                                shopRefundItemsAdapter.clearDeviceList();
                                for (int i = 0; i < jarrrrefundItems.length(); i++) {
                                    listRefundItems.add(MallOrderDetailGoodItems
                                            .json2Entity(jarrrrefundItems
                                                    .getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("goods") && !jdata.isNull("goods")) {
                            JSONArray jarrrgoods = jdata.getJSONArray("goods");
                            if (jarrrgoods.length() > 0) {
                                Log.e("TAG", "1");
                                listMallOrderDetailGoods.clear();
                                listMallOrderDetailGoodItems.clear();
                                shopMallOrderDetailGoodAdapter.clearDeviceList();
                                for (int i = 0; i < jarrrgoods.length(); i++) {
                                    listMallOrderDetailGoods.add(MallOrderDetailGoods.json2Entity(jarrrgoods
                                            .getJSONObject(i)));
                                }
                                for (int i = 0; i < listMallOrderDetailGoods.size(); i++) {
                                    MallOrderDetailGoods mallOrderDetailGoods = listMallOrderDetailGoods.get(i);
                                    if (mallOrderDetailGoods != null) {
                                        Log.e("TAG", "2");
                                        String express = mallOrderDetailGoods.getExpress();
                                        String expressNo = mallOrderDetailGoods.getExpressNo();
                                        List<MallOrderDetailGoodItems> mallOrderDetailGoodItemsList = mallOrderDetailGoods.getMallOrderDetailGoodItemsList();
                                        if (mallOrderDetailGoodItemsList != null && mallOrderDetailGoodItemsList.size() > 0) {
                                            Log.e("TAG", "3");
                                            for (int j = 0; j < mallOrderDetailGoodItemsList.size(); j++) {
                                                MallOrderDetailGoodItems mallOrderDetailGoodItems = mallOrderDetailGoodItemsList.get(j);
                                                if (mallOrderDetailGoodItems != null) {
                                                    Log.e("TAG", "4");
                                                    mallOrderDetailGoodItems.setLogId(mallOrderDetailGoods.getLogId());
                                                    mallOrderDetailGoodItems.setInfoMsg(mallOrderDetailGoods.getInfoMsg());
                                                    mallOrderDetailGoodItems.setMallOrderDetailGoodsLogisticsInfo(mallOrderDetailGoods.getMallOrderDetailGoodsLogisticsInfo());
                                                    mallOrderDetailGoodItems.setExpress(express);
                                                    mallOrderDetailGoodItems.setExpressNo(expressNo);
                                                    if (j == 0) {
                                                        mallOrderDetailGoodItems.setClassIndex(-1);
                                                    }
                                                    if (j == mallOrderDetailGoodItemsList.size() - 1) {
                                                        mallOrderDetailGoodItems.setLastIndex(-1);
                                                    }
                                                    listMallOrderDetailGoodItems.add(mallOrderDetailGoodItems);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "订单详情E = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "数据异常");
            }
            isTimerDown = false;
            writeData();
            if (couponId > 0) {
                String name = "";
                if (reduceType == 1) {
                    name = "满减";
                } else if (reduceType == 2) {
                    name = "折扣";
                } else if (reduceType == 3) {
                    name = "免单";
                }
                localDiscountList.add(new WashDiscount("", name, String.valueOf(couponAmount), String.valueOf(2)));
            }
            if (reserveDiscount > 0) {
                localDiscountList.add(new WashDiscount("", "", String.valueOf(reserveDiscount), String.valueOf(7)));
            }
            washOrderDetailDiscountAdapter.notifyDataSetChanged();
            washOrderDetailPayPricesItemAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "请求失败");
        }
    };

    private void getPayWay() {
        listpayWays.setLength(0);
        mPDialog.showDialog();
        CommUtil.payWays(this, Global.ORDERKEY[12], 0, payWaysHandler);
    }

    private AsyncHttpResponseHandler payWaysHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("payWays") && !jData.isNull("payWays")) {
                            JSONArray jsonArray = jData.getJSONArray("payWays");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listpayWays.append(jsonArray.getString(i));
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "请求失败");
        }
    };

    private void timerDown() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (lastSecs > 0) {
                    lastSecs -= 1000;
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.arg1 = (int) lastSecs;
                    aliHandler.sendMessage(msg);
                } else {
                    if (timer != null) {
                        aliHandler.sendEmptyMessage(1);
                        timer.cancel();
                        timer = null;
                    }
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private void timerDown1() {
        timer1 = new Timer();
        task1 = new TimerTask() {
            @Override
            public void run() {
                if (lastSecs > 0) {
                    lastSecs -= 1000;
                    Message msg = Message.obtain();
                    msg.what = 2;
                    msg.arg1 = (int) lastSecs;
                    aliHandler.sendMessage(msg);
                } else {
                    if (timer1 != null) {
                        aliHandler.sendEmptyMessage(1);
                        timer1.cancel();
                        timer1 = null;
                    }
                }
            }
        };
        timer1.schedule(task1, 0, 1000);
    }

    private void writeData() {
        ll_header_shopmallorderdetail_ywc.setVisibility(View.GONE);
        ll_header_shopmallorderdetail_yfk.setVisibility(View.GONE);
        ll_header_shopmallorderdetail_dfk.setVisibility(View.GONE);
        ll_header_shopmallorderdetail_psz.setVisibility(View.GONE);
        ll_header_shopmallorderdetail_yqx.setVisibility(View.GONE);
        rl_shopmallorderdetail_dfk.setVisibility(View.GONE);
        if (state == 0) {//待付款
            getPayWay();
            ibTitlebarOther.setVisibility(View.VISIBLE);
            if (!isTimerDown) {
                timerDown();
            }
            btn_shopmallorderdetail_submit.setText("去付款");
            ll_header_shopmallorderdetail_dfk.setVisibility(View.VISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_dfk, stateDesc, "", View.VISIBLE, View.INVISIBLE);
            rl_shopmallorderdetail_dfk.setVisibility(View.VISIBLE);
            ll_shopmallorderdetail_price.setVisibility(View.VISIBLE);
            Utils.setText(tv_shopmallorderdetail_price, "¥" + mixThirdPrice, "¥0", View.VISIBLE, View.VISIBLE);
        } else if (state == 1) {//待发货
            ibTitlebarOther.setVisibility(View.VISIBLE);
            ll_header_shopmallorderdetail_yfk.setVisibility(View.VISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_yfk, stateDesc, "", View.VISIBLE, View.INVISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_yfkstr, payedDetailTag, "", View.VISIBLE, View.INVISIBLE);
        } else if (state == 2) {//待收货
            ibTitlebarOther.setVisibility(View.VISIBLE);
            if (!isTimerDown) {
                timerDown1();
            }
            rl_shopmallorderdetail_dfk.setVisibility(View.VISIBLE);
            ll_shopmallorderdetail_price.setVisibility(View.GONE);
            btn_shopmallorderdetail_submit.setText("确认收货");
            ll_header_shopmallorderdetail_psz.setVisibility(View.VISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_psz, stateDesc, "", View.VISIBLE, View.INVISIBLE);
        } else if (state == 3) {//已完成
            ibTitlebarOther.setVisibility(View.VISIBLE);
            ll_header_shopmallorderdetail_ywc.setVisibility(View.VISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_jywc, stateDesc, "", View.VISIBLE, View.INVISIBLE);
        } else if (state == 4 || state == -1) {//已取消
            ibTitlebarOther.setVisibility(View.GONE);
            ll_header_shopmallorderdetail_yqx.setVisibility(View.VISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_yqx, stateDesc, "", View.VISIBLE, View.INVISIBLE);
        }
        if (Utils.isStrNull(insideCopy)) {
            tv_header_shopmallorderdetail_neigou.setVisibility(View.VISIBLE);
            tv_header_shopmallorderdetail_neigou.setText(insideCopy);
        } else {
            tv_header_shopmallorderdetail_neigou.setVisibility(View.GONE);
        }
        if (Utils.isStrNull(consigner)) {
            tv_header_shopmallorderdetail_lxr.setVisibility(View.VISIBLE);
            tv_header_shopmallorderdetail_lxr.setText("收货人：" + consigner);
        } else {
            tv_header_shopmallorderdetail_lxr.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(addressCopy)) {
            tv_header_shopmallorderdetail_add.setVisibility(View.VISIBLE);
            tv_header_shopmallorderdetail_add.setText("收货地址：" + addressCopy);
            tv_header_shopmallorderdetail_add.requestLayout();
        } else {
            tv_header_shopmallorderdetail_add.setVisibility(View.GONE);
        }
        Utils.setText(tv_header_shopmallorderdetail_phone, mobile, "", View.VISIBLE, View.INVISIBLE);
        Log.e("TAG", "SIZE = " + listMallOrderDetailGoodItems.size());
        if (listMallOrderDetailGoodItems.size() > 0) {
            shopMallOrderDetailGoodAdapter.setData(listMallOrderDetailGoodItems);
            mlv_header_shopmallorderdetail.setVisibility(View.VISIBLE);
            vw_header_shopmallorderdetail.setVisibility(View.VISIBLE);
        } else {
            vw_header_shopmallorderdetail.setVisibility(View.GONE);
            mlv_header_shopmallorderdetail.setVisibility(View.GONE);
        }
        if (freight > 0) {
            tv_header_shopmallorderdetail_yunfei.setText("¥" + freight);
            textview_mall_express_tag.setText(freightNoTag);
        } else {
            textview_mall_express_tag.setText(freightTag);
            tv_header_shopmallorderdetail_yunfei.setText("¥0");
        }
        Utils.setText(tv_header_shopmallorderdetail_payprice, "¥" + payPrice, "", View.VISIBLE, View.INVISIBLE);
        Utils.setText(tv_header_shopmallorderdetail_totalprice, "¥" + goodsPrice, "", View.VISIBLE, View.INVISIBLE);
        if (Utils.isStrNull(tradeNo)) {
            btn_header_shopmallorderdetail_ordercopy.setVisibility(View.VISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_ordernum, "订单编号：" + tradeNo, "", View.VISIBLE, View.INVISIBLE);
        } else {
            btn_header_shopmallorderdetail_ordercopy.setVisibility(View.INVISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_ordernum, "订单编号：无", "", View.VISIBLE, View.INVISIBLE);
        }

        if (Utils.isStrNull(payTime)) {
            Utils.setText(tv_header_shopmallorderdetail_paytime, "支付时间：" + payTime, "", View.VISIBLE, View.INVISIBLE);
        } else {
            Utils.setText(tv_header_shopmallorderdetail_paytime, "支付时间：无", "", View.VISIBLE, View.INVISIBLE);
        }

        if (Utils.isStrNull(payWay)) {
            Utils.setText(tv_header_shopmallorderdetail_payway, "支付方式：" + payWay, "", View.VISIBLE, View.INVISIBLE);
        } else {
            Utils.setText(tv_header_shopmallorderdetail_payway, "支付方式：无", "", View.VISIBLE, View.INVISIBLE);
        }

        if (Utils.isStrNull(hotline)) {
            btn_header_shopmallorderdetail_bddh.setVisibility(View.VISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_shdh, "售后电话：" + hotline, "", View.VISIBLE, View.INVISIBLE);
        } else {
            btn_header_shopmallorderdetail_bddh.setVisibility(View.INVISIBLE);
            Utils.setText(tv_header_shopmallorderdetail_shdh, "售后电话：无", "", View.VISIBLE, View.INVISIBLE);
        }
        if (listRefundItems != null && listRefundItems.size() > 0) {
            rl_header_shopmallRefundItems.setVisibility(View.VISIBLE);
            shopRefundItemsAdapter.setData(listRefundItems);
        } else {
            rl_header_shopmallRefundItems.setVisibility(View.GONE);
        }
        if (listRecommend != null && listRecommend.size() > 0) {
            ll_header_shopmallorderdetail_recommend.setVisibility(View.VISIBLE);
            if (listRecommend.size() == 1) {
                ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(0);
                if (shopMallFragRecommend != null) {
                    if (shopMallFragRecommend.isOther()) {
                        ll_header_shopmallorderdetail_recommend.setVisibility(View.GONE);
                    }
                }
            }
            shopMallRecommendAdapter.setData(listRecommend);
        } else {
            if (page == 1) {
                listRecommend.add(new ShopMallFragRecommend("追加", 0, 0, "", 0, 0, true));
                shopMallRecommendAdapter.setData(listRecommend);
            }
            ll_header_shopmallorderdetail_recommend.setVisibility(View.GONE);
        }
    }

    private void showPop() {
        dismissPop();
        mallOrderPopupWindow = new MallOrderPopupWindow(this, onClickListener, state, orderExamineState);
        mallOrderPopupWindow.showAsDropDown(ibTitlebarOther, -22, -20);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_mallorder_sqtk:
                    //判断是否已经申请退换
                    if (orderExamineState == 1) {
                        Intent intent = new Intent(ShopMallOrderDetailActivity.this, ShopRefundScheduleActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("mallRuleUrl", returnRule);
                        intent.putExtra("goodList", (Serializable) listMallOrderDetailGoodItems);
                        startActivity(intent);
                    } else if (orderExamineState == 0) {
                        if (state == 0) {//待付款
                            Intent intent = new Intent(ShopMallOrderDetailActivity.this, CancelShopOrderActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putStringArrayListExtra("tipList", tipList);
                            intent.putExtra("reasonList", (Serializable) reasonList);
                            startActivity(intent);
                        } else if (state == 1) {//待发货
                            new AlertDialogMall(ShopMallOrderDetailActivity.this).builder()
                                    .setTitle("您好,请联系客服核实发货状态")
                                    .setSubTitle("如包裹已发货,请联络快递员做拒收准备.")
                                    .setMsg("如包裹还未从仓库发出,我们会做拦截处理.")
                                    .setNegativeButton("再想想", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .setPositiveButton("联系客服", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.telePhoneBroadcast(mContext, hotline);
                                        }
                                    }).show();
                        } else if (state == 2) {//待收货
                            new AlertDialogMall(ShopMallOrderDetailActivity.this).builder()
                                    .setTitle("需联系客服核实商品是否发货")
                                    .setSubTitle("未发货情况可为您拦截订单操作退款")
                                    .setMsg("已发货情况需您收货后操作申请退货")
                                    .setNegativeButton("再想想", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .setPositiveButton("联系客服", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.telePhoneBroadcast(mContext, hotline);
                                        }
                                    }).show();
                        } else if (state == 3) {//已完成
                            //再判断是否超过退货日期
                            if (isReturnTime == 1) {
                                new AlertDialogNavAndPost(ShopMallOrderDetailActivity.this).builder()
                                        .setTitle("该商品已超过退货日期")
                                        .setSubTitle("")
                                        .setMsg("如有问题请联系客服")
                                        .setNegativeButton("取消", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .setPositiveButton("联系客服", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Utils.telePhoneBroadcast(mContext, hotline);
                                            }
                                        }).show();
                            } else if (isReturnTime == 0) {
                                Intent intent = new Intent(ShopMallOrderDetailActivity.this, ApplyShopRefundActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("refundTip", returnTips);
                                intent.putExtra("mallRuleUrl", returnRule);
                                intent.putExtra("reasonList", (Serializable) returnReasonList);
                                intent.putExtra("goodList", (Serializable) listMallOrderDetailGoodItems);
                                startActivity(intent);
                            }
                        }
                    }
                    dismissPop();
                    break;
                case R.id.tv_pop_mallorder_lxkf:
                    MDialog mDialog = new MDialog.Builder(mContext)
                            .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                            .setMessage("是否拨打电话?").setCancelStr("否")
                            .setOKStr("是")
                            .positiveListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Utils.telePhoneBroadcast(mContext, hotline);
                                }
                            }).build();
                    mDialog.show();
                    dismissPop();
                    break;
            }
        }
    };

    private void dismissPop() {
        if (mallOrderPopupWindow != null) {
            if (mallOrderPopupWindow.isShowing()) {
                mallOrderPopupWindow.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        toShopFragment();
        super.onBackPressed();
    }

    /**
     * 跳转到订单
     */
    public void toShopFragment(){
        ActivityUtils.toOrderFragment(this);
        //添加商品订单事件
        EventBus.getDefault().post(new GoodsAddEvent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_other:
                showPop();
                break;
            case R.id.img_scroll_top:
                if (isGridScroll) {
                    HeaderGridView refreshableView = ptrhgv_shopmallorderdetail.getRefreshableView();
                    if (!(refreshableView).isStackFromBottom()) {
                        refreshableView.setStackFromBottom(true);
                    }
                    refreshableView.setStackFromBottom(false);
                }
                break;
            case R.id.btn_shopmallorder_nonet:
                page = 1;
                getData();
                getrRecommendData();
                break;
            case R.id.ib_titlebar_back:
                toShopFragment();
                finish();
                break;
            case R.id.btn_shopmallorderdetail_submit:
                if (state == 0) {//待付款
                    if (listpayWays != null && listpayWays.length() > 0) {
                        if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                            showPayDialog();
                        }
                    }
                } else if (state == 2) {//待收货
                    new AlertDialogNavAndPost(ShopMallOrderDetailActivity.this).builder().setTitle("")
                            .setMsg("是否现在确认收货？").setPostTextColor(R.color.aD0021B).setNavTextColor(R.color.a666666)
                            .setPositiveButton("立即收货", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mPDialog.showDialog();
                                    CommUtil.shopMallOrderReceive(ShopMallOrderDetailActivity.this, orderId, shopMallOrderReceiveHandler);
                                }
                            }).setNegativeButton("还没收到", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
                break;
            case R.id.btn_header_shopmallorderdetail_ordercopy:
                if (Utils.isStrNull(tradeNo)) {
                    Utils.copy(tradeNo, mContext);
                    ToastUtil.showToastShortBottom(mContext, "订单编号已复制");
                }
                break;
            case R.id.btn_header_shopmallorderdetail_bddh:
                MDialog mDialog = new MDialog.Builder(mContext)
                        .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                        .setMessage("是否拨打电话?").setCancelStr("否")
                        .setOKStr("是")
                        .positiveListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Utils.telePhoneBroadcast(mContext, hotline);
                            }
                        }).build();
                mDialog.show();
                break;
            default:
                break;
        }
    }

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(ShopMallOrderDetailActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(ShopMallOrderDetailActivity.this, R.layout.appoint_pay_bottom_dialog, null);
        Button btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        tv_pay_bottomdia_time_minute = (TextView) customView.findViewById(R.id.tv_pay_bottomdia_time_minute);
        tv_pay_bottomdia_time_second = (TextView) customView.findViewById(R.id.tv_pay_bottomdia_time_second);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        final ImageView iv_pay_bottomdia_zfb_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_zfb_select);
        pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(ShopMallOrderDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        tv_pay_bottomdia_time_minute.setText(minute);
        tv_pay_bottomdia_time_second.setText(second);
        btn_pay_bottomdia.setText("确认支付¥" + mixThirdPrice);
        if (listpayWays.toString().contains("1")) {
            ll_pay_bottomdia_weixin.setVisibility(View.VISIBLE);
            if (oldpayway == 1) {
                paytype = 1;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_weixin.setVisibility(View.GONE);
        }
        if (listpayWays.toString().contains("2")) {
            ll_pay_bottomdia_zfb.setVisibility(View.VISIBLE);
            if (oldpayway == 2) {
                paytype = 2;
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_zfb.setVisibility(View.GONE);
        }
        tv_pay_title.setText("请选择支付方式");
        iv_pay_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        btn_pay_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paytype != 1 && paytype != 2) {
                    ToastUtil.showToastShortBottom(mContext, "请选择支付方式");
                    return;
                }
                mallOrderPay(-1);
            }
        });
        ll_pay_bottomdia_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytype = 1;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
            }
        });
        ll_pay_bottomdia_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytype = 2;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
            }
        });
    }

    private AsyncHttpResponseHandler shopMallOrderReceiveHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    getData();
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "请求失败");
        }
    };

    private String getStrp() {
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        ArrayList<MallGoods> goodsList = new ArrayList<>();
        goodsList.clear();
        for (int i = 0; i < listMallOrderDetailGoodItems.size(); i++) {
            MallOrderDetailGoodItems mallOr = listMallOrderDetailGoodItems.get(i);
            MallGoods mallGoods = new MallGoods();
            mallGoods.amount = mallOr.getAmount();
            mallGoods.title = mallOr.getTitle();
            mallGoods.retailPrice = mallOr.getRetailPrice();
            goodsList.add(mallGoods);
            sb.append(mallOr.getCommodityId() + "_" + mallOr.getAmount() + ",");
        }
        Log.e("TAG", "sb.toString() = " + sb.toString());
        int i = sb.toString().lastIndexOf(",");
        Log.e("TAG", "i = " + i);
        String substring = sb.toString().substring(0, sb.toString().length() - 1);
        Log.e("TAG", "substring = " + substring);
        return substring;
    }

    /**
     * 商城下单二次
     *
     * @param balance
     */
    private void mallOrderPay(double balance) {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
        boolean fx_isOpen = false;
        if (reserveDiscount > 0) {
            fx_isOpen = true;
        }
        CommUtil.mallOrderPay(mContext, promoterCode, cardId,
                spUtil.getInt("cityId", 0), fx_isOpen, reserveDiscount, payPrice,
                Utils.formatDouble(balance, 2), getStrp(), paytype, addressId, orderId, couponId, orderHanler);
    }

    private AsyncHttpResponseHandler orderHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            Utils.mLogError("二次支付商城订单：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("give_can") && !jdata.isNull("give_can")) {
                            JSONObject obGiveCan = jdata.getJSONObject("give_can");
                            if (obGiveCan.has("myself") && !obGiveCan.isNull("myself")) {
                                myselfDummpNum = obGiveCan.getInt("myself");
                            }
                        }
                        if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
                            JSONObject jpayInfo = jdata.getJSONObject("payInfo");
                            if (jpayInfo.has("appid") && !jpayInfo.isNull("appid")) {
                                appid = jpayInfo.getString("appid");
                            }
                            if (jpayInfo.has("noncestr")
                                    && !jpayInfo.isNull("noncestr")) {
                                noncestr = jpayInfo.getString("noncestr");
                            }
                            if (jpayInfo.has("package")
                                    && !jpayInfo.isNull("package")) {
                                packageValue = jpayInfo.getString("package");
                            }
                            if (jpayInfo.has("partnerid")
                                    && !jpayInfo.isNull("partnerid")) {
                                partnerid = jpayInfo.getString("partnerid");
                            }
                            if (jpayInfo.has("prepayid")
                                    && !jpayInfo.isNull("prepayid")) {
                                prepayid = jpayInfo.getString("prepayid");
                            }
                            if (jpayInfo.has("sign") && !jpayInfo.isNull("sign")) {
                                sign = jpayInfo.getString("sign");
                            }
                            if (jpayInfo.has("timestamp")
                                    && !jpayInfo.isNull("timestamp")) {
                                timestamp = jpayInfo.getString("timestamp");
                            }
                            if (jpayInfo.has("orderStr")
                                    && !jpayInfo.isNull("orderStr")) {
                                orderStr = jpayInfo.getString("orderStr");
                            }
                        }
                        goPay();
                    }
                } else {
                    if (resultCode == Global.PAY_SUCCESS) {
                        goPaySuccess();
                    }
                    ToastUtil.showToastShort(ShopMallOrderDetailActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShopMallOrderDetailActivity.this, "请求失败");
        }
    };

    private void goPay() {
        if (paytype == 1) {// 微信支付
            mPDialog.showDialog();
            spUtil.saveInt("payway", 1);
            PayUtils.weChatPayment(ShopMallOrderDetailActivity.this, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(ShopMallOrderDetailActivity.this, aliHandler);
        }
    }

    private void goPaySuccess() {
        for (int i = 0; i < MApplication.listAppoint.size(); i++) {
            MApplication.listAppoint.get(i).finish();
        }
        MApplication.listAppoint.clear();
        for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
            MApplication.listAppoint1.get(i).finish();
        }
        MApplication.listAppoint.clear();
        MApplication.listAppoint1.clear();
        Intent intent = new Intent(this, PaySuccessNewActivity.class);
        double totalDiscountPrice = 0;
        if (localDiscountList != null && localDiscountList.size() > 0) {
            discountList.clear();
            for (int i = 0; i < localDiscountList.size(); i++) {
                totalDiscountPrice = ComputeUtil.add(totalDiscountPrice, Double.parseDouble(localDiscountList.get(i).getAmount()));
                if (Integer.parseInt(localDiscountList.get(i).getType()) == 2) {//优惠券减免类型标识
                    discountList.add("优惠券优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 7) {//返现抵扣类型标识
                    discountList.add("返现抵扣优惠¥" + localDiscountList.get(i).getAmount());
                }
            }
            discountList.add(0, "共计优惠¥" + totalDiscountPrice);
            intent.putStringArrayListExtra("discountList", discountList);
        }
        intent.putExtra("orderId", orderId);
        double sub = ComputeUtil.sub(payPrice, totalDiscountPrice);
        intent.putExtra("payPrice", sub < 0 ? 0 : sub);
        intent.putExtra("type", 20);
        intent.putExtra("pageType", 3);
        intent.putExtra("myself", myselfDummpNum);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Log.e("TAG", "android.os.Build.MODEL = " + android.os.Build.MODEL);
        Log.e("TAG", "android.os.Build.VERSION.RELEASE = " + android.os.Build.VERSION.RELEASE);
        Log.e("TAG", "Global.WXPAYCODE = " + Global.WXPAYCODE);
        if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1") && Global.WXPAYCODE == 0) {
            Global.WXPAYCODE = -1;
            Log.e("支付成功", "onResume");
            goPaySuccess();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timer1 != null) {
            timer1.cancel();
            timer1 = null;
        }
        aliHandler.removeCallbacksAndMessages(null);
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
