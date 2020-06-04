package com.haotang.pet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.WashOrderDetailDiscountAdapter;
import com.haotang.pet.adapter.WashOrderDetailPayPricesItemAdapter;
import com.haotang.pet.adapter.WashOrderDetailPetAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.ExtraItem;
import com.haotang.pet.entity.Order;
import com.haotang.pet.entity.OrdersBean;
import com.haotang.pet.entity.PayPricesItem;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.WashDiscount;
import com.haotang.pet.entity.WashPetService;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Constant;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.haotang.pet.view.SyLinearLayoutManager;
import com.haotang.pet.view.WashOrderPopupWindow;
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
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 洗美特订单详情页
 */
public class WashOrderDetailActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.btn_washorderdetail_yfkts)
    Button btnWashorderdetailYfkts;
    @BindView(R.id.btn_washorderdetail_yfkpj)
    Button btnWashorderdetailYfkpj;
    @BindView(R.id.rl_washorderdetail_yfkts)
    LinearLayout rlWashorderdetailYfkts;
    @BindView(R.id.tv_washorderdetail_price)
    TextView tvWashorderdetailPrice;
    @BindView(R.id.rl_washorderdetail_dfk)
    RelativeLayout rlWashorderdetailDfk;
    @BindView(R.id.rl_washorderdetail_bottom)
    RelativeLayout rlWashorderdetailBottom;
    @BindView(R.id.tv_washorderdetail_dfkminute)
    TextView tvWashorderdetailDfkminute;
    @BindView(R.id.tv_washorderdetail_dfksecond)
    TextView tvWashorderdetailDfksecond;
    @BindView(R.id.tv_washorderdetail_dfkneedprice)
    TextView tvWashorderdetailDfkneedprice;
    @BindView(R.id.ll_washorderdetail_dfktime)
    LinearLayout llWashorderdetailDfktime;
    @BindView(R.id.tv_washorderdetail_statusname)
    TextView tvWashorderdetailStatusname;
    @BindView(R.id.rv_washorderdetail_pet)
    RecyclerView rvWashorderdetailPet;
    @BindView(R.id.rv_washorderdetail_discount)
    RecyclerView rv_washorderdetail_discount;
    @BindView(R.id.rv_washorderdetail_payway)
    RecyclerView rv_washorderdetail_payway;
    @BindView(R.id.iv_washorderdetail_mrsicon)
    ImageView ivWashorderdetailMrsicon;
    @BindView(R.id.tv_washorderdetail_mrsname)
    TextView tvWashorderdetailMrsname;
    @BindView(R.id.tv_washorderdetail_shop)
    TextView tvWashorderdetailShop;
    @BindView(R.id.iv_washorderdetail_call)
    ImageView ivWashorderdetailCall;
    @BindView(R.id.tv_washorderdetail_fwfs)
    TextView tvWashorderdetailFwfs;
    @BindView(R.id.tv_washorderdetail_js)
    TextView tvWashorderdetailJs;
    @BindView(R.id.tv_washorderdetail_time)
    TextView tvWashorderdetailTime;
    @BindView(R.id.iv_washorderdetail_fwbzright)
    ImageView ivWashorderdetailFwbzright;
    @BindView(R.id.tv_washorderdetail_fwbz)
    TextView tvWashorderdetailFwbz;
    @BindView(R.id.ll_washorderdetail_fwbz)
    LinearLayout llWashorderdetailFwbz;
    @BindView(R.id.iv_washorderdetail_mddzright)
    ImageView ivWashorderdetailMddzright;
    @BindView(R.id.tv_washorderdetail_mddz)
    TextView tvWashorderdetailMddz;
    @BindView(R.id.ll_washorderdetail_mddz)
    LinearLayout llWashorderdetailMddz;
    @BindView(R.id.iv_washorderdetail_yhxx_fz)
    ImageView ivWashorderdetailYhxxFz;
    @BindView(R.id.rl_washorderdetail_yhxx)
    RelativeLayout rlWashorderdetailYhxx;
    @BindView(R.id.tv_washorderdetail_lxr)
    TextView tvWashorderdetailLxr;
    @BindView(R.id.tv_washorderdetail_lxfs)
    TextView tvWashorderdetailLxfs;
    @BindView(R.id.tv_washorderdetail_lxdz)
    TextView tvWashorderdetailLxdz;
    @BindView(R.id.ll_washorderdetail_yhxx)
    LinearLayout llWashorderdetailYhxx;
    @BindView(R.id.tv_washorderdetail_totalprice)
    TextView tvWashorderdetailTotalprice;
    @BindView(R.id.tv_washorderdetail_jcfwprice)
    TextView tvWashorderdetailJcfwprice;
    @BindView(R.id.tv_washorderdetail_dxfwprice)
    TextView tvWashorderdetailDxfwprice;
    @BindView(R.id.tv_washorderdetail_zjdxprice)
    TextView tvWashorderdetailZjdxprice;
    @BindView(R.id.tv_washorderdetail_smjsprice)
    TextView tvWashorderdetailSmjsprice;
    @BindView(R.id.tv_washorderdetail_tkprice)
    TextView tvWashorderdetailTkprice;
    @BindView(R.id.rl_washorderdetail_tkprice)
    RelativeLayout rlWashorderdetailTkprice;
    @BindView(R.id.tv_washorderdetail_feedback)
    TextView tvWashorderdetailFeedback;
    @BindView(R.id.tv_washorderdetail_feedback_status)
    TextView tvWashorderdetailFeedbackStatus;
    @BindView(R.id.ll_washorderdetail_feedback)
    LinearLayout llWashorderdetailFeedback;
    @BindView(R.id.tv_washorderdetail_zffs)
    TextView tvWashorderdetailZffs;
    @BindView(R.id.tv_washorderdetail_ddbh)
    TextView tvWashorderdetailDdbh;
    @BindView(R.id.tv_washorderdetail_fz)
    TextView tvWashorderdetailFz;
    @BindView(R.id.tv_washorderdetail_tjsj)
    TextView tvWashorderdetailTjsj;
    @BindView(R.id.tv_washorderdetail_wcsj)
    TextView tvWashorderdetailWcsj;
    @BindView(R.id.rl_washorderdetail_dxfw)
    RelativeLayout rlWashorderdetailDxfw;
    @BindView(R.id.rl_washorderdetail_zjdx)
    RelativeLayout rlWashorderdetailZjdx;
    @BindView(R.id.rl_washorderdetail_js)
    RelativeLayout rlWashorderdetailJs;
    @BindView(R.id.rl_washorderdetail_smfwf)
    RelativeLayout rlWashorderdetailSmfwf;
    @BindView(R.id.ll_washorderdetail_wcsj)
    LinearLayout llWashorderdetailWcsj;
    @BindView(R.id.tv_washorderdetail_smfwfprice)
    TextView tvWashorderdetailSmfwfprice;
    @BindView(R.id.iv_washorderdetail_status)
    ImageView ivWashorderdetailStatus;
    @BindView(R.id.rl_washorderdetail_jcfw)
    RelativeLayout rl_washorderdetail_jcfw;
    private PopupWindow pWinBottomDialog;
    private Timer timer;
    private TimerTask task;
    private String minute = "";
    private String second = "";
    private TextView tv_pay_bottomdia_time_second;
    private TextView tv_pay_bottomdia_time_minute;
    private int myselfDummpNum;
    private StringBuilder listpayWays = new StringBuilder();
    private int oldpayway;
    private int paytype;
    private int orderNo;
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private long lastOverTime = 900000;
    private List<WashPetService> petServiceList = new ArrayList<WashPetService>();
    private WashOrderDetailPetAdapter washOrderDetailPetAdapter;
    private List<WashDiscount> localDiscountList = new ArrayList<WashDiscount>();
    private WashOrderDetailDiscountAdapter washOrderDetailDiscountAdapter;
    private List<PayPricesItem> payPricesItemList = new ArrayList<PayPricesItem>();
    private WashOrderDetailPayPricesItemAdapter washOrderDetailPayPricesItemAdapter;
    private List<ExtraItem> itemList = new ArrayList<ExtraItem>();
    private List<ExtraItem> itemList1 = new ArrayList<ExtraItem>();
    private List<ExtraItem> itemList2 = new ArrayList<ExtraItem>();
    private int serviceLoc;
    private String phoneNum;
    private boolean isShowUserInfo;
    private double shopLat;
    private double shopLng;
    private double pickupPrice;
    private int pickUp;
    private String orderNum;
    private String actualEndTime;
    private String remark;
    private String shopAddress;
    private String shopName;
    private double refund;
    private double extraServicePrice;
    private int status;
    private int refType;
    private int serviceCardId;
    private String evaluate;
    private String extraLogcountType;
    private String activityIdExtrax;
    private String activityIdUpdata;
    private String updateLogcountType;
    private Order order = new Order();
    private OrdersBean ordersBean = new OrdersBean();
    private WashOrderPopupWindow washOrderPopupWindow;
    private int couponId;
    private int payWay;
    private String servicePhone;
    private String modifyTip;
    private int modifyEnable;
    private String modifyDisableTip;
    private StringBuilder serviceIds = new StringBuilder();
    private String levelName;
    private int beautician_id;
    private int tid;
    private String realName;
    private String avatar;
    private String appointment;
    private int shopId;
    private int pageType;
    private ArrayList<String> discountList = new ArrayList<String>();
    private int homeCouponId;
    private StringBuilder reasonBuilder = new StringBuilder();
    private double payPrice;
    private double cardPayPrice;
    private double totalListPrice;
    private int updateOrderId;
    private int updateStatus;
    private String modifyOrderRuleUrl;
    private String cancelDisableTip;
    private int cancelEnable;
    private double grainGoldPrice;
    private double thirdPrice;

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    if (TextUtils.equals(resultStatus, "9000")) {
                        goPaySuccess();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShortBottom(WashOrderDetailActivity.this,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(WashOrderDetailActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(WashOrderDetailActivity.this, orderStr,
                            aliHandler, mPDialog);
                    break;
                case 0:
                    long lastTimerShowHMS = msg.arg1;
                    String time = Utils.formatTimeFM(lastTimerShowHMS);
                    minute = time.substring(0, 2);
                    second = time.substring(3, 5);
                    if (tv_pay_bottomdia_time_minute != null && tv_pay_bottomdia_time_second != null) {
                        tv_pay_bottomdia_time_minute.setText(minute);
                        tv_pay_bottomdia_time_second.setText(second);
                    }
                    tvWashorderdetailDfkminute.setText(minute);
                    tvWashorderdetailDfksecond.setText(second);
                    break;
                case 1:
                    if (lastOverTime <= 0) {
                        ToastUtil.showToastShortBottom(mContext, "抱歉您的订单已超时");
                    }
                    finish();
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
                        ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "支付失败");
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
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void initData() {
        Global.WXPAYCODE = -1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        orderNo = getIntent().getIntExtra("orderid", 0);
        oldpayway = spUtil.getInt("payway", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_wash_order_detail);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("订单详情");
        mPDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        ibTitlebarOther.setBackgroundResource(R.drawable.order_right_icon);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 40), Utils.dip2px(mContext, 22));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ibTitlebarOther.setLayoutParams(layoutParams);

        SyLinearLayoutManager linearLayoutManager = new SyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvWashorderdetailPet.setLayoutManager(linearLayoutManager);
        washOrderDetailPetAdapter = new WashOrderDetailPetAdapter(this, R.layout.item_washorderdetail_petservice, petServiceList);
        rvWashorderdetailPet.setAdapter(washOrderDetailPetAdapter);

        rv_washorderdetail_discount.setHasFixedSize(true);
        rv_washorderdetail_discount.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rv_washorderdetail_discount.setLayoutManager(noScollFullLinearLayoutManager);
        washOrderDetailDiscountAdapter = new WashOrderDetailDiscountAdapter(R.layout.item_washorderdetail_discount, localDiscountList);
        rv_washorderdetail_discount.setAdapter(washOrderDetailDiscountAdapter);

        rv_washorderdetail_payway.setHasFixedSize(true);
        rv_washorderdetail_payway.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager1 = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager1.setScrollEnabled(false);
        rv_washorderdetail_payway.setLayoutManager(noScollFullLinearLayoutManager1);
        washOrderDetailPayPricesItemAdapter = new WashOrderDetailPayPricesItemAdapter(R.layout.item_washorderdetail_paypriceitem, payPricesItemList);
        rv_washorderdetail_payway.setAdapter(washOrderDetailPayPricesItemAdapter);
    }

    private void setLinster() {

    }

    private void getData() {
        mPDialog.showDialog();
        petServiceList.clear();
        localDiscountList.clear();
        payPricesItemList.clear();
        itemList.clear();
        itemList1.clear();
        itemList2.clear();
        order.listMyPets.clear();
        reasonBuilder.setLength(0);
        serviceIds.setLength(0);
        updateOrderId = 0;
        grainGoldPrice = 0;
        cancelDisableTip = "";
        cancelEnable = 0;
        refType = 0;
        thirdPrice = 0;
        CommUtil.queryOrderDetailsNewTwo(this, orderNo, getOrderDetailsTwo);
    }

    private AsyncHttpResponseHandler getOrderDetailsTwo = new AsyncHttpResponseHandler() {
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
                        if (jData.has("grainGoldPrice") && !jData.isNull("grainGoldPrice")) {
                            grainGoldPrice = jData.getDouble("grainGoldPrice");
                        }
                        if (jData.has("serviceLoc") && !jData.isNull("serviceLoc")) {
                            serviceLoc = jData.getInt("serviceLoc");
                            ordersBean.setServiceLoc(serviceLoc);
                            order.serviceLoc = serviceLoc;
                        }
                        if (jData.has("discountItems") && !jData.isNull("discountItems")) {
                            JSONArray jarrdiscountItems = jData.getJSONArray("discountItems");
                            if (jarrdiscountItems.length() > 0) {
                                for (int i = 0; i < jarrdiscountItems.length(); i++) {
                                    WashDiscount washDiscount = new WashDiscount();
                                    JSONObject jdiscountItems = jarrdiscountItems.getJSONObject(i);
                                    if (jdiscountItems.has("id") && !jdiscountItems.isNull("id")) {
                                        washDiscount.setId(jdiscountItems.getString("id"));
                                    }
                                    if (jdiscountItems.has("name") && !jdiscountItems.isNull("name")) {
                                        washDiscount.setName(jdiscountItems.getString("name"));
                                    }
                                    if (jdiscountItems.has("amount") && !jdiscountItems.isNull("amount")) {
                                        washDiscount.setAmount(jdiscountItems.getString("amount"));
                                    }
                                    if (jdiscountItems.has("type") && !jdiscountItems.isNull("type")) {
                                        washDiscount.setType(jdiscountItems.getString("type"));
                                    }
                                    localDiscountList.add(washDiscount);
                                }
                                double amount = 0;
                                int cardNum = 0;
                                String name = "";
                                String id = "";
                                for (int i = 0; i < localDiscountList.size(); i++) {
                                    if (Integer.parseInt(localDiscountList.get(i).getType()) == 1) {
                                        name = localDiscountList.get(i).getName();
                                        id = localDiscountList.get(i).getId();
                                        cardNum++;
                                        amount = ComputeUtil.add(amount, Double.parseDouble(localDiscountList.get(i).getAmount()));
                                    }
                                }
                                if (cardNum > 1) {
                                    //循环删除集合中的元素
                                    Iterator<WashDiscount> it = localDiscountList.iterator();
                                    while (it.hasNext()) {
                                        WashDiscount washDiscount = it.next();
                                        if (Integer.parseInt(washDiscount.getType()) == 1) {
                                            it.remove();
                                        }
                                    }
                                    localDiscountList.add(new WashDiscount(id, name, String.valueOf(amount), "1"));
                                }
                            }
                        }
                        if (jData.has("payPrices") && !jData.isNull("payPrices")) {
                            JSONArray jarrpayPrices = jData.getJSONArray("payPrices");
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
                        if (jData.has("orders") && !jData.isNull("orders")) {
                            JSONArray jarrorders = jData.getJSONArray("orders");
                            if (jarrorders.length() > 0) {
                                for (int i = 0; i < jarrorders.length(); i++) {
                                    Pet pet = new Pet();
                                    WashPetService washPetService = new WashPetService();
                                    washPetService.setServiceLoc(serviceLoc);
                                    JSONObject jorders = jarrorders.getJSONObject(i);
                                    if (jorders.has("listPrice") && !jorders.isNull("listPrice")) {
                                        washPetService.setBasicServicePrice(jorders.getDouble("listPrice"));
                                    }
                                    if (jorders.has("service") && !jorders.isNull("service")) {
                                        JSONObject jservice = jorders.getJSONObject("service");
                                        if (jservice.has("id") && !jservice.isNull("id")) {
                                            washPetService.setServiceId(jservice.getInt("id"));
                                            pet.serviceid = jservice.getInt("id");
                                        }
                                        if (jservice.has("name") && !jservice.isNull("name")) {
                                            washPetService.setServiceName(jservice.getString("name"));
                                            order.servicename = jservice.getString("name");
                                        }
                                        if (jservice.has("serviceType") && !jservice.isNull("serviceType")) {
                                            washPetService.setServiceType(jservice.getInt("serviceType"));
                                        }
                                        if (jservice.has("items") && !jservice.isNull("items")) {
                                            washPetService.setJcfwName(jservice.getString("items"));
                                        }
                                    }
                                    if (jorders.has("pet") && !jorders.isNull("pet")) {
                                        JSONObject jpet = jorders.getJSONObject("pet");
                                        if (jpet.has("avatar") && !jpet.isNull("avatar")) {
                                            ordersBean.setAvatar(jpet.getString("avatar"));
                                        }
                                        if (jpet.has("name") && !jpet.isNull("name")) {
                                            washPetService.setPetName(jpet.getString("name"));
                                            ordersBean.setService(jpet.getString("name"));
                                        }
                                        if (jpet.has("id") && !jpet.isNull("id")) {
                                            washPetService.setPetId(jpet.getInt("id"));
                                            pet.id = jpet.getInt("id");
                                        }
                                        if (jpet.has("petKind") && !jpet.isNull("petKind")) {
                                            pet.kindid = jpet.getInt("petKind");
                                        }
                                    }
                                    if (jorders.has("customerPet") && !jorders.isNull("customerPet")) {
                                        JSONObject objectMyPet = jorders.getJSONObject("customerPet");
                                        if (objectMyPet.has("name") && !objectMyPet.isNull("name")) {
                                            washPetService.setPetName(objectMyPet.getString("name"));
                                            pet.nickName = objectMyPet.getString("name");
                                            ordersBean.setService(objectMyPet.getString("name"));
                                        }
                                        if (objectMyPet.has("id") && !objectMyPet.isNull("id")) {
                                            pet.customerpetid = objectMyPet.getInt("id");
                                            washPetService.setMyPetId(objectMyPet.getInt("id"));
                                        }
                                        if (objectMyPet.has("avatar") && !objectMyPet.isNull("avatar")) {
                                            pet.image = objectMyPet.getString("avatar");
                                        }
                                    }
                                    order.listMyPets.add(pet);
                                    if (jorders.has("extraItems") && !jorders.isNull("extraItems")) {
                                        JSONArray jarrextraItems = jorders.getJSONArray("extraItems");
                                        if (jarrextraItems.length() > 0) {
                                            itemList.clear();
                                            itemList1.clear();
                                            itemList2.clear();
                                            for (int j = 0; j < jarrextraItems.length(); j++) {
                                                ExtraItem extraItem = new ExtraItem();
                                                JSONObject jextraItems = jarrextraItems.getJSONObject(j);
                                                if (jextraItems.has("source") && !jextraItems.isNull("source")) {
                                                    extraItem.setSource(jextraItems.getInt("source"));
                                                }
                                                if (jextraItems.has("id") && !jextraItems.isNull("id")) {
                                                    extraItem.setItemId(jextraItems.getInt("id"));
                                                }
                                                if (jextraItems.has("name") && !jextraItems.isNull("name")) {
                                                    extraItem.setItemName(jextraItems.getString("name"));
                                                }
                                                if (jextraItems.has("listPrice") && !jextraItems.isNull("listPrice")) {
                                                    extraItem.setListPrice(jextraItems.getDouble("listPrice"));
                                                }
                                                itemList.add(extraItem);
                                            }
                                            for (int j = 0; j < itemList.size(); j++) {
                                                if (itemList.get(j).getSource() == 0) {//下单单项
                                                    itemList1.add(itemList.get(j));
                                                } else if (itemList.get(j).getSource() == 1) {//追加单项
                                                    itemList2.add(itemList.get(j));
                                                }
                                            }
                                            if (itemList1.size() > 0) {
                                                serviceIds.setLength(0);
                                                double extraPrice = 0;
                                                StringBuffer sbItemName = new StringBuffer();
                                                for (int j = 0; j < itemList1.size(); j++) {
                                                    sbItemName.append(itemList1.get(j).getItemName() + " ¥" + itemList1.get(j).getListPrice() + " ");
                                                    order.itemIds.add(itemList1.get(j).getItemId());
                                                    if (j == itemList1.size() - 1) {
                                                        serviceIds.append(itemList1.get(j).getItemId());
                                                    } else {
                                                        serviceIds.append(itemList1.get(j).getItemId() + ",");
                                                    }
                                                    extraPrice = ComputeUtil.add(extraPrice, itemList1.get(j).getListPrice());
                                                }
                                                washPetService.setExtraPrice(extraPrice);
                                                washPetService.setItemIds(serviceIds.toString());
                                                washPetService.setDxfwName(sbItemName.substring(0, sbItemName.toString().length() - 1));
                                            }
                                            if (itemList2.size() > 0) {
                                                double zjdxPrice = 0;
                                                StringBuffer sbItemName = new StringBuffer();
                                                for (int j = 0; j < itemList2.size(); j++) {
                                                    sbItemName.append(itemList2.get(j).getItemName() + " ¥" + itemList2.get(j).getListPrice() + " ");
                                                    zjdxPrice = ComputeUtil.add(zjdxPrice, itemList2.get(j).getListPrice());
                                                }
                                                washPetService.setZjdxName(sbItemName.toString());
                                                washPetService.setZjdxPrice(zjdxPrice);
                                            }
                                        }
                                    }
                                    petServiceList.add(washPetService);
                                }
                            }
                        }
                        if (jData.has("feedback") && !jData.isNull("feedback")) {
                            llWashorderdetailFeedback.setVisibility(View.VISIBLE);
                            JSONObject feedback = jData.getJSONObject("feedback");
                            if (feedback.has("reason") && !feedback.isNull("reason")) {
                                JSONArray arrayReason = feedback.getJSONArray("reason");
                                if (arrayReason.length() > 0) {
                                    for (int i = 0; i < arrayReason.length(); i++) {
                                        if (i == arrayReason.length() - 1) {
                                            reasonBuilder.append(arrayReason.getString(i));
                                            if (feedback.has("content") && !feedback.isNull("content")) {
                                                reasonBuilder.append("\n\n" + feedback.getString("content"));
                                            }
                                        } else {
                                            reasonBuilder.append(arrayReason.getString(i) + "\n\n");
                                        }
                                    }
                                } else {
                                    reasonBuilder.append(feedback.getString("content"));
                                }
                                tvWashorderdetailFeedback.setText(reasonBuilder.toString());
                            }
                            if (feedback.has("managerStatus") && !feedback.isNull("managerStatus")) {
                                int managerStatus = feedback.getInt("managerStatus");
                                if (managerStatus == 0) {
                                    tvWashorderdetailFeedbackStatus.setText("正在处理中,请您耐心等候");
                                } else if (managerStatus == 1) {
                                    tvWashorderdetailFeedbackStatus.setText("处理完成");
                                }
                            }
                            if (feedback.has("managerStatusName") && !feedback.isNull("managerStatusName")) {
                                tvWashorderdetailFeedbackStatus.setText(feedback.getString("managerStatusName"));
                            }
                        } else {
                            llWashorderdetailFeedback.setVisibility(View.GONE);
                        }
                        if (jData.has("status") && !jData.isNull("status")) {
                            status = jData.getInt("status");
                        }
                        if (jData.has("extraServicePrice") && !jData.isNull("extraServicePrice")) {
                            extraServicePrice = jData.getDouble("extraServicePrice");
                        }
                        if (jData.has("refund") && !jData.isNull("refund")) {
                            JSONObject jrefund = jData.getJSONObject("refund");
                            if (jrefund.has("price") && !jrefund.isNull("price")) {
                                refund = jrefund.getDouble("price");
                            }
                        }
                        if (jData.has("title") && !jData.isNull("title")) {
                            Utils.setText(tvTitlebarTitle, jData.getString("title"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("statusDesc") && !jData.isNull("statusDesc")) {
                            Utils.setText(tvWashorderdetailStatusname, jData.getString("statusDesc"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("residualTime") && !jData.isNull("residualTime")) {
                            lastOverTime = (long) ComputeUtil.mul(jData.getLong("residualTime"), 1000);
                        }
                        if (jData.has("pickup") && !jData.isNull("pickup")) {
                            pickUp = jData.getInt("pickup");
                        }
                        if (jData.has("pickupPrice") && !jData.isNull("pickupPrice")) {
                            pickupPrice = jData.getDouble("pickupPrice");
                        }
                        if (jData.has("evaluate") && !jData.isNull("evaluate")) {
                            evaluate = jData.getString("evaluate");
                        }
                        if (jData.has("appointment") && !jData.isNull("appointment")) {
                            appointment = jData.getString("appointment");
                            ordersBean.setAppointment(appointment);
                            Utils.setText(tvWashorderdetailTime, appointment, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("logcount") && !jData.isNull("logcount")) {
                            JSONObject objectInside = jData.getJSONObject("logcount");
                            if (objectInside.has("updateService") && !objectInside.isNull("updateService")) {
                                JSONObject objectUpdate = objectInside.getJSONObject("updateService");
                                if (objectUpdate.has("logcountType") && !objectUpdate.isNull("logcountType")) {
                                    updateLogcountType = objectUpdate.getString("logcountType");
                                }
                                if (objectUpdate.has("activeId") && !objectUpdate.isNull("activeId")) {
                                    activityIdUpdata = objectUpdate.getString("activeId");
                                }
                            }
                            if (jData.has("addExtraItem") && !jData.isNull("addExtraItem")) {
                                JSONObject objectExtra = jData.getJSONObject("addExtraItem");
                                if (objectExtra.has("logcountType") && !objectExtra.isNull("logcountType")) {
                                    extraLogcountType = objectExtra.getString("logcountType");
                                }
                                if (objectExtra.has("activeId") && !objectExtra.isNull("activeId")) {
                                    activityIdExtrax = objectExtra.getString("activeId");
                                }
                            }
                        }
                        if (jData.has("payWay") && !jData.isNull("payWay")) {
                            payWay = jData.getInt("payWay");
                        }
                        if (jData.has("cancelOrderMap") && !jData.isNull("cancelOrderMap")) {
                            JSONObject cancelObject = jData.getJSONObject("cancelOrderMap");
                            if (cancelObject.has("cancelDisableTip") && !cancelObject.isNull("cancelDisableTip")) {
                                cancelDisableTip = cancelObject.getString("cancelDisableTip");
                            }
                            if (cancelObject.has("cancelEnable") && !cancelObject.isNull("cancelEnable")) {
                                cancelEnable = cancelObject.getInt("cancelEnable");
                            }
                        }
                        if (jData.has("modifyOrder") && !jData.isNull("modifyOrder")) {
                            JSONObject jmodifyOrder = jData.getJSONObject("modifyOrder");
                            if (jmodifyOrder.has("modifyEnable") && !jmodifyOrder.isNull("modifyEnable")) {
                                modifyEnable = jmodifyOrder.getInt("modifyEnable");
                            }
                            if (jmodifyOrder.has("modifyDisableTip") && !jmodifyOrder.isNull("modifyDisableTip")) {
                                modifyDisableTip = jmodifyOrder.getString("modifyDisableTip");
                            }
                            if (jmodifyOrder.has("modifyTip") && !jmodifyOrder.isNull("modifyTip")) {
                                modifyTip = jmodifyOrder.getString("modifyTip");
                            }
                            if (jmodifyOrder.has("modifyOrderRuleUrl") && !jmodifyOrder.isNull("modifyOrderRuleUrl")) {
                                modifyOrderRuleUrl = jmodifyOrder.getString("modifyOrderRuleUrl");
                            }
                        }
                        if (jData.has("servicePhone") && !jData.isNull("servicePhone")) {
                            servicePhone = jData.getString("servicePhone");
                        }
                        if (jData.has("actualEndTime") && !jData.isNull("actualEndTime")) {
                            actualEndTime = jData.getString("actualEndTime");
                        }
                        if (jData.has("orderNum") && !jData.isNull("orderNum")) {
                            orderNum = jData.getString("orderNum");
                            ordersBean.setOrderNum(orderNum);
                            Utils.setText(tvWashorderdetailDdbh, orderNum, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("payWayDesc") && !jData.isNull("payWayDesc")) {
                            Utils.setText(tvWashorderdetailZffs, jData.getString("payWayDesc"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("created") && !jData.isNull("created")) {
                            Utils.setText(tvWashorderdetailTjsj, jData.getString("created"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("remark") && !jData.isNull("remark")) {
                            remark = jData.getString("remark");
                            Utils.setText(tvWashorderdetailFwbz, remark, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("worker") && !jData.isNull("worker")) {
                            JSONObject workerObject = jData.getJSONObject("worker");
                            if (workerObject.has("avatar") && !workerObject.isNull("avatar")) {
                                avatar = workerObject.getString("avatar");
                                GlideUtil.loadCircleImg(mContext, avatar, ivWashorderdetailMrsicon, R.drawable.user_icon_unnet_circle);
                            }
                            if (workerObject.has("name") && !workerObject.isNull("name")) {
                                realName = workerObject.getString("name");
                                Utils.setText(tvWashorderdetailMrsname, realName, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (workerObject.has("tid") && !workerObject.isNull("tid")) {
                                tid = workerObject.getInt("tid");
                            }
                            if (workerObject.has("id") && !workerObject.isNull("id")) {
                                beautician_id = workerObject.getInt("id");
                            }
                            if (workerObject.has("level") && !workerObject.isNull("level")) {
                                levelName = workerObject.getString("level");
                            }
                        }
                        if (jData.has("shop") && !jData.isNull("shop")) {
                            ServiceShopAdd LastShop = new ServiceShopAdd();
                            JSONObject jshop = jData.getJSONObject("shop");
                            if (jshop.has("lat") && !jshop.isNull("lat")) {
                                shopLat = jshop.getDouble("lat");
                            }
                            if (jshop.has("lng") && !jshop.isNull("lng")) {
                                shopLng = jshop.getDouble("lng");
                            }
                            if (jshop.has("name") && !jshop.isNull("name")) {
                                shopName = jshop.getString("name");
                                LastShop.shopName = shopName;
                                Utils.setText(tvWashorderdetailShop, shopName, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jshop.has("address") && !jshop.isNull("address")) {
                                shopAddress = jshop.getString("address");
                                LastShop.shopAddress = shopAddress;
                                Utils.setText(tvWashorderdetailMddz, shopAddress, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jshop.has("phone") && !jshop.isNull("phone")) {
                                phoneNum = jshop.getString("phone");
                                LastShop.shopPhone = phoneNum;
                            }
                            if (jshop.has("id") && !jshop.isNull("id")) {
                                shopId = jshop.getInt("id");
                                LastShop.shopId = shopId;
                            }
                            if (jshop.has("openTime") && !jshop.isNull("openTime")) {
                                LastShop.openTime = jshop.getString("openTime");
                            }
                            if (jshop.has("img") && !jshop.isNull("img")) {
                                LastShop.shopImg = jshop.getString("img");
                            }
                            order.LastShop = LastShop;
                        }
                        if (jData.has("payPrice") && !jData.isNull("payPrice")) {
                            payPrice = jData.getDouble("payPrice");
                        }
                        if (jData.has("listPrice") && !jData.isNull("listPrice")) {
                            totalListPrice = jData.getDouble("listPrice");
                            ordersBean.setPay_price(totalListPrice);
                        }
                        if (jData.has("serviceCardPrice") && !jData.isNull("serviceCardPrice")) {
                            cardPayPrice = jData.getDouble("serviceCardPrice");
                        }
                        if (jData.has("serviceAddress") && !jData.isNull("serviceAddress")) {
                            order.commAddr = CommAddr.json2Entity(jData.getJSONObject("serviceAddress"));
                            Utils.setText(tvWashorderdetailLxdz, order.commAddr.address, "", View.VISIBLE, View.VISIBLE);
                            Utils.setText(tvWashorderdetailLxr, order.commAddr.linkman, "", View.VISIBLE, View.VISIBLE);
                            Utils.setText(tvWashorderdetailLxfs, order.commAddr.telephone, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("updateOrderId") && !jData.isNull("updateOrderId")) {
                            updateOrderId = jData.getInt("updateOrderId");
                        }
                        if (jData.has("refType") && !jData.isNull("refType")) {
                            refType = jData.getInt("refType");
                        }
                        if (jData.has("thirdPrice") && !jData.isNull("thirdPrice")) {
                            thirdPrice = jData.getDouble("thirdPrice");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "数据异常");
            }
            washOrderDetailPetAdapter.notifyDataSetChanged();
            washOrderDetailPayPricesItemAdapter.notifyDataSetChanged();
            washOrderDetailDiscountAdapter.notifyDataSetChanged();
            if (localDiscountList.size() > 0) {
                for (int i = 0; i < localDiscountList.size(); i++) {
                    if (Integer.parseInt(localDiscountList.get(i).getType()) == 1) {
                        serviceCardId = Integer.parseInt(localDiscountList.get(i).getId());
                    } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 2) {
                        couponId = Integer.parseInt(localDiscountList.get(i).getId());
                    } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 3) {
                        homeCouponId = Integer.parseInt(localDiscountList.get(i).getId());
                    }
                }
            }
            Utils.setText(tvWashorderdetailTotalprice, "¥" + totalListPrice, "", View.VISIBLE, View.VISIBLE);
            if (serviceLoc == 1) {
                rlWashorderdetailSmfwf.setVisibility(View.GONE);
                if (pickUp == 1) {
                    rlWashorderdetailJs.setVisibility(View.VISIBLE);
                    tvWashorderdetailJs.setText("(需要接送)");
                    if (pickupPrice > 0) {
                        Utils.setText(tvWashorderdetailSmjsprice, "¥" + pickupPrice, "", View.VISIBLE, View.VISIBLE);
                    } else {
                        tvWashorderdetailSmjsprice.setText("免费");
                    }
                } else {
                    rlWashorderdetailJs.setVisibility(View.GONE);
                    tvWashorderdetailJs.setText("(不接送)");
                }
                Utils.setText(tvWashorderdetailFwfs, "到店服务", "", View.VISIBLE, View.VISIBLE);
            } else if (serviceLoc == 2) {
                rlWashorderdetailJs.setVisibility(View.GONE);
                Utils.setText(tvWashorderdetailFwfs, "上门服务", "", View.VISIBLE, View.VISIBLE);
                tvWashorderdetailJs.setVisibility(View.GONE);
                if (extraServicePrice > 0) {
                    rlWashorderdetailSmfwf.setVisibility(View.VISIBLE);
                    Utils.setText(tvWashorderdetailSmfwfprice, "¥" + extraServicePrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlWashorderdetailSmfwf.setVisibility(View.GONE);
                }
            }
            if (status == 1) {
                rlWashorderdetailDxfw.setVisibility(View.GONE);
                rl_washorderdetail_jcfw.setVisibility(View.GONE);
                Utils.setText(tvWashorderdetailDfkneedprice, "需付款 : ¥" + thirdPrice, "需付款 : ¥0", View.VISIBLE, View.VISIBLE);
                Utils.setText(tvWashorderdetailPrice, "¥" + thirdPrice, "", View.VISIBLE, View.VISIBLE);
                rlWashorderdetailYfkts.setVisibility(View.GONE);
                llWashorderdetailFeedback.setVisibility(View.GONE);
                llWashorderdetailWcsj.setVisibility(View.GONE);
                rlWashorderdetailZjdx.setVisibility(View.GONE);
                rlWashorderdetailTkprice.setVisibility(View.GONE);
                llWashorderdetailDfktime.setVisibility(View.VISIBLE);
                rlWashorderdetailDfk.setVisibility(View.VISIBLE);
                rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                tvTitlebarOther.setVisibility(View.VISIBLE);
                ibTitlebarOther.setVisibility(View.GONE);
                tvTitlebarOther.setText("取消订单");
                tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));
                timerDown();
                getPayWay();
            } else {
                if (task != null) {
                    task.cancel();
                }
                if (timer != null) {
                    timer.cancel();
                }
                double totalBasicPrice = 0;
                double totalExtraPrice = 0;
                double totalZJExtraPrice = 0;
                for (int i = 0; i < petServiceList.size(); i++) {
                    totalBasicPrice = ComputeUtil.add(totalBasicPrice, petServiceList.get(i).getBasicServicePrice());
                    totalExtraPrice = ComputeUtil.add(totalExtraPrice, petServiceList.get(i).getExtraPrice());
                    totalZJExtraPrice = ComputeUtil.add(totalZJExtraPrice, petServiceList.get(i).getZjdxPrice());
                }
                aliHandler.removeCallbacksAndMessages(null);
                rl_washorderdetail_jcfw.setVisibility(View.VISIBLE);
                Utils.setText(tvWashorderdetailJcfwprice, "¥" + totalBasicPrice, "", View.VISIBLE, View.VISIBLE);
                if (totalExtraPrice > 0) {
                    rlWashorderdetailDxfw.setVisibility(View.VISIBLE);
                    Utils.setText(tvWashorderdetailDxfwprice, "¥" + totalExtraPrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlWashorderdetailDxfw.setVisibility(View.GONE);
                }
                if (totalZJExtraPrice > 0) {
                    rlWashorderdetailZjdx.setVisibility(View.VISIBLE);
                    Utils.setText(tvWashorderdetailZjdxprice, "¥" + totalZJExtraPrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlWashorderdetailZjdx.setVisibility(View.GONE);
                }
                if (refund > 0) {
                    rlWashorderdetailTkprice.setVisibility(View.VISIBLE);
                    Utils.setText(tvWashorderdetailTkprice, "¥" + refund, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlWashorderdetailTkprice.setVisibility(View.GONE);
                }
                if (status == 4 || status == 5) {
                    llWashorderdetailWcsj.setVisibility(View.VISIBLE);
                    Utils.setText(tvWashorderdetailWcsj, actualEndTime, "", View.VISIBLE, View.VISIBLE);
                } else {
                    llWashorderdetailWcsj.setVisibility(View.GONE);
                }
                llWashorderdetailDfktime.setVisibility(View.GONE);
                rlWashorderdetailDfk.setVisibility(View.GONE);
                tvTitlebarOther.setVisibility(View.GONE);
                rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                switch (status) {
                    case 2://已付款
                        if (refType == 4) {//寄养下的洗美不能升级服务和追加单项
                            rlWashorderdetailBottom.setVisibility(View.GONE);
                            rlWashorderdetailYfkts.setVisibility(View.GONE);
                        } else {
                            rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                            rlWashorderdetailYfkts.setVisibility(View.VISIBLE);
                            btnWashorderdetailYfkts.setVisibility(View.VISIBLE);
                            btnWashorderdetailYfkpj.setVisibility(View.VISIBLE);
                            btnWashorderdetailYfkts.setText("升级服务");
                            btnWashorderdetailYfkpj.setText("追加单项");
                        }
                        ivWashorderdetailStatus.setImageResource(R.drawable.order_already_payover);
                        ibTitlebarOther.setVisibility(View.VISIBLE);
                        break;
                    case 3://待确认
                        ivWashorderdetailStatus.setImageResource(R.drawable.already_pay);
                        rlWashorderdetailYfkts.setVisibility(View.GONE);
                        ibTitlebarOther.setVisibility(View.VISIBLE);
                        break;
                    case 4://待评价
                        ivWashorderdetailStatus.setImageResource(R.drawable.to_wai_eva);
                        rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                        rlWashorderdetailYfkts.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkts.setVisibility(View.GONE);
                        btnWashorderdetailYfkpj.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkpj.setText(evaluate);
                        ibTitlebarOther.setVisibility(View.GONE);
                        break;
                    case 5://已关闭(结束)
                        rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                        rlWashorderdetailYfkts.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkts.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkpj.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkts.setText("我要投诉");
                        btnWashorderdetailYfkpj.setText("再来一单");
                        ivWashorderdetailStatus.setImageResource(R.drawable.order_already_over);
                        ibTitlebarOther.setVisibility(View.GONE);
                        if (reasonBuilder != null && reasonBuilder.length() > 0 && Utils.isStrNull(reasonBuilder.toString())) {
                            btnWashorderdetailYfkts.setVisibility(View.GONE);
                        } else {
                            btnWashorderdetailYfkts.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 6://已取消(已支付)
                        ivWashorderdetailStatus.setImageResource(R.drawable.order_already_cancle);
                        rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                        rlWashorderdetailYfkts.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkts.setVisibility(View.GONE);
                        btnWashorderdetailYfkpj.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkpj.setText("重新下单");
                        ibTitlebarOther.setVisibility(View.GONE);
                        break;
                    case 7:
                        ivWashorderdetailStatus.setImageResource(R.drawable.order_already_cancle);
                        rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                        rlWashorderdetailYfkts.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkts.setVisibility(View.GONE);
                        btnWashorderdetailYfkpj.setVisibility(View.VISIBLE);
                        btnWashorderdetailYfkpj.setText("重新下单");
                        ibTitlebarOther.setVisibility(View.GONE);
                        break;
                    case 21://已出发
                        ivWashorderdetailStatus.setImageResource(R.drawable.order_is_to_working);
                        ibTitlebarOther.setVisibility(View.VISIBLE);
                        rlWashorderdetailBottom.setVisibility(View.GONE);
                        rlWashorderdetailYfkts.setVisibility(View.GONE);
                        break;
                    case 22://开始服务
                        ivWashorderdetailStatus.setImageResource(R.drawable.order_is_working);
                        ibTitlebarOther.setVisibility(View.VISIBLE);
                        rlWashorderdetailBottom.setVisibility(View.GONE);
                        rlWashorderdetailYfkts.setVisibility(View.GONE);
                        break;
                    case 23://申请取消
                        break;
                }
            }
            if (updateOrderId > 0) {//有升级订单，请求升级订单信息
                mPDialog.showDialog();
                updateStatus = 0;
                CommUtil.queryOrderDetailsNewTwo(mContext, updateOrderId, getOrderDetailsTwoUpdate);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler getOrderDetailsTwoUpdate = new AsyncHttpResponseHandler() {
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
                        if (jData.has("status") && !jData.isNull("status")) {
                            updateStatus = jData.getInt("status");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "数据异常");
            }
            if (updateStatus == 1) {//订单升级去付款
                rlWashorderdetailYfkts.setVisibility(View.VISIBLE);
                rlWashorderdetailBottom.setVisibility(View.VISIBLE);
                btnWashorderdetailYfkts.setVisibility(View.GONE);
                btnWashorderdetailYfkpj.setVisibility(View.VISIBLE);
                btnWashorderdetailYfkpj.setText("订单升级-去付款");
                ivWashorderdetailStatus.setImageResource(R.drawable.to_wait_pay);
                ibTitlebarOther.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "请求失败");
        }
    };

    private void getPayWay() {
        listpayWays.setLength(0);
        mPDialog.showDialog();
        CommUtil.payWays(this, Global.ORDERKEY[8], 0, payWaysHandler);
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
                    ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "请求失败");
        }
    };

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

    private void timerDown() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (lastOverTime > 0) {
                    lastOverTime -= 1000;
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.arg1 = (int) lastOverTime;
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

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(WashOrderDetailActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(WashOrderDetailActivity.this, R.layout.appoint_pay_bottom_dialog, null);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(WashOrderDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        tv_pay_bottomdia_time_minute.setText(minute);
        tv_pay_bottomdia_time_second.setText(second);
        btn_pay_bottomdia.setText("确认支付¥" + thirdPrice);
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
                washChangePayWay();
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

    private void washChangePayWay() {
        mPDialog.showDialog();
        CommUtil.changePayWayTwo("", "", "", this, orderNo, 0, "", "", remark, pickUp, 0,
                0, payPrice, paytype, couponId,
                0, "", false, "", appointment + ":00", getPetID_ServiceId_MyPetId_ItemIds(), 0, homeCouponId, serviceCardId, changeHanler);
    }

    private String getPetID_ServiceId_MyPetId_ItemIds() {
        StringBuffer sb = new StringBuffer();
        if (petServiceList != null && petServiceList.size() > 0) {
            for (int i = 0; i < petServiceList.size(); i++) {
                WashPetService washPetService = petServiceList.get(i);
                if (washPetService != null) {
                    if (i < petServiceList.size() - 1) {
                        sb.append(washPetService.getPetId());
                        sb.append("_");
                        sb.append(washPetService.getServiceId());
                        sb.append("_");
                        sb.append(washPetService.getMyPetId());
                        sb.append("_");
                        String itemIds = washPetService.getItemIds();
                        if (Utils.isStrNull(itemIds)) {
                            sb.append(itemIds);
                        } else {
                            sb.append("0");
                        }
                        sb.append("_");
                    } else {
                        sb.append(washPetService.getPetId());
                        sb.append("_");
                        sb.append(washPetService.getServiceId());
                        sb.append("_");
                        sb.append(washPetService.getMyPetId());
                        sb.append("_");
                        String itemIds = washPetService.getItemIds();
                        if (Utils.isStrNull(itemIds)) {
                            sb.append(itemIds);
                        } else {
                            sb.append("0");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private AsyncHttpResponseHandler changeHanler = new AsyncHttpResponseHandler() {
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
                    ToastUtil.showToastShort(WashOrderDetailActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderDetailActivity.this, "请求失败");
        }
    };

    private void goPay() {
        if (paytype == 1) {// 微信支付
            mPDialog.showDialog();
            spUtil.saveInt("payway", 1);
            PayUtils.weChatPayment(WashOrderDetailActivity.this, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(WashOrderDetailActivity.this, aliHandler);
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
                if (Integer.parseInt(localDiscountList.get(i).getType()) == 1) {//服务卡减免类型标识
                    discountList.add("E卡优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 2) {//优惠券减免类型标识
                    discountList.add("优惠券优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 3) {//上门优惠券减免类型标识
                    discountList.add("上门优惠券优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 4) {//次卡减免类型标识
                    discountList.add("次卡优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 5) {//罐头币减免类型标识
                    discountList.add("罐头币优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 6) {//上门服务费减免类型标识
                    discountList.add("上门服务费减免优惠¥" + localDiscountList.get(i).getAmount());
                }
            }
            discountList.add(0, "共计优惠¥" + totalDiscountPrice);
            intent.putStringArrayListExtra("discountList", discountList);
        }
        intent.putExtra("orderId", orderNo);
        intent.putExtra("fx_price", grainGoldPrice);
        double sub = ComputeUtil.sub(payPrice, totalDiscountPrice);
        intent.putExtra("payPrice", sub < 0 ? 0 : sub);
        intent.putExtra("type", 1);
        intent.putExtra("myself", myselfDummpNum);
        String subString = getServiceName(petServiceList);
        if (subString.contains("1") && !subString.contains("2")) {
            pageType = 1;
        } else if (!subString.contains("1") && subString.contains("2")) {
            pageType = 1;
        } else if (subString.contains("1") && subString.contains("2")) {
            pageType = 1;
        }
        for (int i = 0; i < petServiceList.size(); i++) {
            if (petServiceList.get(i).getServiceType() == 3) {
                pageType = 2;
            }
        }
        intent.putExtra("pageType", pageType);
        startActivity(intent);
        finish();
    }

    public String getServiceName(List<WashPetService> list) {
        if (list.size() <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getServiceType() + ",");
        }
        return sb.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        aliHandler.removeCallbacksAndMessages(null);
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        toOrderFragment();
        super.onBackPressed();
    }

    /**
     * 跳转到洗美订单
     */
    private void toOrderFragment(){
        //取消订单
        if(status == Constant.ORDER_STATUS_WAIT_PAY || status == Constant.ORDER_STATUS_ALREADY_PAY){
            ActivityUtils.toOrderFragment(WashOrderDetailActivity.this);
        }
    }

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.btn_washorderdetail_yfkts, R.id.btn_washorderdetail_yfkpj,
            R.id.btn_washorderdetail_submit, R.id.iv_washorderdetail_call, R.id.ll_washorderdetail_fwbz,
            R.id.ll_washorderdetail_mddz, R.id.rl_washorderdetail_yhxx, R.id.tv_washorderdetail_fz, R.id.tv_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                //跳转到洗美订单
                toOrderFragment();
                finish();
                break;
            case R.id.ib_titlebar_other:
                showPop();
                break;
            case R.id.btn_washorderdetail_yfkts:
                if (status == 2) {//升级服务
                    ToastUtil.showToastShortCenter(mContext, "如需升级订单请与美容师沟通");
                    logcountAdd(updateLogcountType, activityIdUpdata);
                } else if (status == 5) {//我要投诉
                    Intent intent = new Intent(mContext, ComplaintActivity.class);
                    ordersBean.setType(1);//投诉订单对象
                    ordersBean.setId(orderNo);
                    ordersBean.setTypeName("洗护");
                    intent.putExtra("ordersBean", ordersBean);
                    startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
                }
                break;
            case R.id.btn_washorderdetail_yfkpj:
                if (status == 2) {//追加单项
                    ToastUtil.showToastShortCenter(mContext, "如需追加单项请到店与美容师沟通");
                    logcountAdd(extraLogcountType, activityIdExtrax);
                } else if (status == 4) {//评价晒单
                    Intent intent = new Intent(mContext, EvaluateNewActivity.class);
                    intent.putExtra("orderid", orderNo);
                    intent.putExtra("type", 1);
                    startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
                } else if (status == 5) {//再来一单
                    goAppointMent();
                } else if (status == 6) {//重新下单
                    goAppointMent();
                } else if (status == 7) {//重新下单
                    goAppointMent();
                }
                if (updateStatus == 1) {//升级待支付
                    Intent intent = new Intent(mContext, UpdateOrderConfirmNewActivity.class);
                    intent.putExtra("orderid", orderNo);
                    startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
                }
                break;
            case R.id.btn_washorderdetail_submit:
                if (listpayWays != null && listpayWays.length() > 0) {
                    if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                        showPayDialog();
                    }
                }
                break;
            case R.id.iv_washorderdetail_call:
                MDialog mDialog = new MDialog.Builder(mContext)
                        .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                        .setMessage("是否拨打电话?").setCancelStr("否")
                        .setOKStr("是")
                        .positiveListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 确认拨打电话
                                Utils.telePhoneBroadcast(mContext,
                                        phoneNum);
                            }
                        }).build();
                mDialog.show();
                break;
            case R.id.ll_washorderdetail_fwbz:
                Intent intent1 = new Intent(mContext, OrderDetailServiceNoteActivity.class);
                intent1.putExtra("note", remark);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_open, R.anim.bottom_silent);
                break;
            case R.id.ll_washorderdetail_mddz:
                Intent intent2 = new Intent(mContext, ShopLocationActivity.class);
                intent2.putExtra("shoplat", shopLat);
                intent2.putExtra("shoplng", shopLng);
                intent2.putExtra("shopaddr", shopAddress);
                intent2.putExtra("shopname", shopName);
                startActivity(intent2);
                break;
            case R.id.rl_washorderdetail_yhxx:
                if (isShowUserInfo) {
                    ivWashorderdetailYhxxFz.setImageResource(R.drawable.icon_arrow_down_beau);
                    llWashorderdetailYhxx.setVisibility(View.GONE);
                } else {
                    ivWashorderdetailYhxxFz.setImageResource(R.drawable.icon_arrow_up_beau);
                    llWashorderdetailYhxx.setVisibility(View.VISIBLE);
                }
                isShowUserInfo = !isShowUserInfo;
                break;
            case R.id.tv_washorderdetail_fz:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(orderNum);
                ToastUtil.showToastShortCenter(mContext, "复制成功");
                break;
            case R.id.tv_titlebar_other:
                if (cancelEnable == 0) {
                    ToastUtil.showToastShortBottom(mContext, cancelDisableTip);
                } else {
                    Intent intent = new Intent(mContext, OrderCancleReasonNewActivity.class);
                    intent.putExtra("orderid", orderNo);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
                break;
        }
    }

    private void showPop() {
        dismissPop();
        washOrderPopupWindow = new WashOrderPopupWindow(this, onClickListener,refType);
        washOrderPopupWindow.showAsDropDown(ibTitlebarOther, -10, -30);
    }

    private void showChangeAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog alertDialog = builder.create();
        final Window window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        View view = View.inflate(mContext, R.layout.alert_changeorder_layout, null);
        alertDialog.setView(view);
        alertDialog.show();
        TextView tvContent = (TextView) view.findViewById(R.id.changeorder_content);
        TextView tvRules = (TextView) view.findViewById(R.id.tv_look_rules);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvContent.setText(modifyDisableTip);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tvRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ADActivity.class);
                intent.putExtra("url", modifyOrderRuleUrl);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_washorder_xgdd:
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ModifyOrder);
                    if (modifyEnable == 0) {//不可以改单
                        showChangeAlert();
                    } else if (modifyEnable == 1) {
                        Beautician beautician = new Beautician();
                        beautician.image = avatar;
                        beautician.name = realName;
                        beautician.id = beautician_id;
                        beautician.appointment = appointment + ":00";
                        beautician.levelname = levelName;
                        beautician.levels = tid;
                        Intent intentNext = new Intent(mContext, ChangeOrderNewActivity.class);
                        intentNext.putExtra("lat", order.commAddr.lat);
                        intentNext.putExtra("lng", order.commAddr.lng);
                        intentNext.putExtra("serviceLoc", serviceLoc);
                        intentNext.putExtra("modifyOrderRuleUrl", modifyOrderRuleUrl);
                        intentNext.putExtra("shopId", shopId);
                        intentNext.putExtra("beautician", beautician);
                        intentNext.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds());
                        intentNext.putExtra("modifyTip", modifyTip);
                        intentNext.putExtra("OrderId", orderNo);
                        startActivity(intentNext);
                    }
                    dismissPop();
                    break;
                case R.id.tv_pop_washorder_sqtk:
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_PetHome_DogCard);
                    //取消订单
                    if (cancelEnable == 0) {
                        ToastUtil.showToastShortBottom(mContext, cancelDisableTip);
                    } else if (cancelEnable == 1) {
                        Intent intent = new Intent(mContext, OrderCancleActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("payWay", payWay);
                        intent.putExtra("orderid", orderNo);
                        intent.putExtra("couponId", couponId);
                        startActivity(intent);
                    }
                    dismissPop();
                    break;
                case R.id.tv_pop_washorder_lxkf:
                    MDialog mDialog = new MDialog.Builder(mContext)
                            .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                            .setMessage("是否拨打电话?").setCancelStr("否")
                            .setOKStr("是")
                            .positiveListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Utils.telePhoneBroadcast(mContext, servicePhone);
                                }
                            }).build();
                    mDialog.show();
                    dismissPop();
                    break;
            }
        }
    };

    private void dismissPop() {
        if (washOrderPopupWindow != null) {
            if (washOrderPopupWindow.isShowing()) {
                washOrderPopupWindow.dismiss();
            }
        }
    }

    private void goAppointMent() {
        Intent intent;
        intent = new Intent(mContext, AppointMentActivity.class);
        intent.putExtra("myPets", (Serializable) order.listMyPets);
        intent.putExtra("itemIds", (Serializable) order.itemIds);
        intent.putExtra("commAddr", order.commAddr);
        intent.putExtra("lat", order.commAddr.lat);
        intent.putExtra("lng", order.commAddr.lng);
        intent.putExtra("shop", order.LastShop);
        intent.putExtra("servicetype", petServiceList.get(0).getServiceType());
        intent.putExtra("petAddress", order.commAddr.address);
        intent.putExtra("addressId", order.commAddr.Customer_AddressId);
        intent.putExtra("serviceName", petServiceList.get(0).getServiceName());
        intent.putExtra("serviceId", petServiceList.get(0).getServiceId());
        intent.putExtra("serviceLoc", petServiceList.get(0).getServiceLoc());
        startActivity(intent);
    }

    public void logcountAdd(String typeid, String activeid) {
        CommUtil.logcountAdd(mContext, typeid, activeid, statisticsHandler);
    }

    private AsyncHttpResponseHandler statisticsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
}
