package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.ExtraItem;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.WashPetService;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CustomStatusView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 升级服务确认页面
 */
public class UpdateOrderConfirmNewActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_updateorderconfirmnew_payprice)
    TextView tvUpdateorderconfirmnewPayprice;
    @BindView(R.id.tv_updateorderconfirmnew_statusname)
    TextView tvUpdateorderconfirmnewStatusname;
    @BindView(R.id.tv_updateorderconfirmnew_sjprice)
    TextView tvUpdateorderconfirmnewSjprice;
    @BindView(R.id.tv_updateorderconfirmnew_sjname)
    TextView tvUpdateorderconfirmnewSjname;
    @BindView(R.id.tv_updateorderconfirmnew_sjinfo)
    TextView tvUpdateorderconfirmnewSjinfo;
    @BindView(R.id.tv_updateorderconfirmnew_extrafee)
    TextView tvUpdateorderconfirmnewExtrafee;
    @BindView(R.id.tv_updateorderconfirmnew_yddprice)
    TextView tvUpdateorderconfirmnewYddprice;
    @BindView(R.id.tv_item_updateorderconfirmnew_pet_serviceloc)
    TextView tvItemUpdateorderconfirmnewPetServiceloc;
    @BindView(R.id.tv_item_updateorderconfirmnew_pet_name)
    TextView tvItemUpdateorderconfirmnewPetName;
    @BindView(R.id.tv_item_updateorderconfirmnew_pet_jcfw)
    TextView tvItemUpdateorderconfirmnewPetJcfw;
    @BindView(R.id.tv_item_updateorderconfirmnew_pet_dxfw)
    TextView tvItemUpdateorderconfirmnewPetDxfw;
    @BindView(R.id.ll_item_updateorderconfirmnew_pet_dxfw)
    LinearLayout llItemUpdateorderconfirmnewPetDxfw;
    @BindView(R.id.tv_updateorderconfirmnew_totalprice)
    TextView tvUpdateorderconfirmnewTotalprice;
    @BindView(R.id.tv_updateorderconfirmnew_carddiscountprice)
    TextView tvUpdateorderconfirmnewCarddiscountprice;
    @BindView(R.id.rl_updateorderconfirmnew_carddiscountprice)
    RelativeLayout rlUpdateorderconfirmnewCarddiscountprice;
    @BindView(R.id.tv_updateorderconfirmnew_cardprice)
    TextView tvUpdateorderconfirmnewCardprice;
    @BindView(R.id.tv_updateorderconfirmnew_cardname_pay)
    TextView tvUpdateorderconfirmnewCardnamePay;
    @BindView(R.id.rl_updateorderconfirmnew_card)
    RelativeLayout rlUpdateorderconfirmnewCard;
    @BindView(R.id.tv_updateorderconfirmnew_cardname)
    TextView tvUpdateorderconfirmNewCardname;
    @BindView(R.id.iv_updateorderconfirmnew_card_more)
    ImageView ivUpdateorderconfirmCardNewMore;
    @BindView(R.id.iv_updateorderconfirmnew_mrsicon)
    ImageView ivUpdateorderconfirmnewMrsicon;
    @BindView(R.id.tv_updateorderconfirmnew_mrsname)
    TextView tvUpdateorderconfirmnewMrsname;
    @BindView(R.id.tv_updateorderconfirmnew_shop)
    TextView tvUpdateorderconfirmnewShop;
    @BindView(R.id.tv_updateorderconfirmnew_fwfs)
    TextView tvUpdateorderconfirmnewFwfs;
    @BindView(R.id.tv_updateorderconfirmnew_js)
    TextView tvUpdateorderconfirmnewJs;
    @BindView(R.id.tv_updateorderconfirmnew_time)
    TextView tvUpdateorderconfirmnewTime;
    @BindView(R.id.tv_updateorderconfirmnew_fwbz)
    TextView tvUpdateorderconfirmnewFwbz;
    @BindView(R.id.tv_updateorderconfirmnew_mddz)
    TextView tvUpdateorderconfirmnewMddz;
    @BindView(R.id.tv_updateorderconfirmnew_minute)
    TextView tv_updateorderconfirmnew_minute;
    @BindView(R.id.tv_updateorderconfirmnew_second)
    TextView tv_updateorderconfirmnew_second;
    @BindView(R.id.rl_updateorderconfirmnew_coupon)
    RelativeLayout rl_updateorderconfirmnew_coupon;
    @BindView(R.id.tv_updateorderconfirmnew_couponname)
    TextView tv_updateorderconfirmnew_couponname;
    @BindView(R.id.rl_updateorderconfirmnew_couponprice)
    RelativeLayout rl_updateorderconfirmnew_couponprice;
    @BindView(R.id.tv_updateorderconfirmnew_couponprice)
    TextView tv_updateorderconfirmnew_couponprice;
    @BindView(R.id.rl_updateorderconfirmnew_cardpay)
    RelativeLayout rl_updateorderconfirmnew_cardpay;
    @BindView(R.id.ll_item_updateorderconfirmnew_pet_zjdxfw)
    LinearLayout ll_item_updateorderconfirmnew_pet_zjdxfw;
    @BindView(R.id.tv_item_updateorderconfirmnew_pet_zjdxfw)
    TextView tv_item_updateorderconfirmnew_pet_zjdxfw;
    private int orderNo;
    private int oldpayway;
    private int serviceLoc;
    private double extraServicePrice;
    private int refType;
    private int pickUp;
    private String shopAddress;
    private String phoneNum;
    private double shopLat;
    private double shopLng;
    private int serviceCardId = -1;
    private String serviceCardName;
    private String shopName;
    private StringBuilder listpayWays = new StringBuilder();
    private List<MyCard> cardList = new ArrayList<MyCard>();
    private String discountText;
    private int shopId;
    private double cardPayPrice;
    private double thirdPrice;
    private double normalCouponDiscountPrice;
    private double cardDiscountPrice;
    private int payPwd;
    private String remark;
    private int paytype;
    private PopupWindow pWinPayPwdSystem;
    private ImageView iv_paypwdsystem_pop_close;
    private TextView tv_paypwdsystem_pop_pwdtype;
    private TextView tv_paypwdsystem_pop_title;
    private RelativeLayout rl_paypwdsystem_pop_select;
    private Button btn_paypwdsystem_pop;
    private TextView tv_paypwdsystem_pop_payprice;
    private RelativeLayout rl_paypwdsystem_pop_pwd;
    private CodeView cv_paypwdsystem_pop_pwd;
    private TextView tv_paypwdsystem_pop_wjmm;
    private RelativeLayout rl_paypwdsystem_pwd;
    private TextView tv_paypwdsystem_pop_pwderror;
    private KeyboardView kbv_paypwdsystem_pop;
    private FingerprintCore mFingerprintCore;
    private LinearLayout ll_paypwdsystem_pop_payresult;
    private CustomStatusView csv_paypwdsystem_pop;
    private TextView tv_paypwdsystem_pop_payresult;
    private int pwdType = 0;
    private PopupWindow popWinZhiWen;
    private PopupWindow popWinZhiWen1;
    private PopupWindow popWinZhiWen2;
    private int fingerNum;
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private int myselfDummpNum;
    private int updateOrderId;
    private ArrayList<String> discountList = new ArrayList<String>();
    private double updatePayPrice;
    private TextView tv_pay_bottomdia_time_second;
    private TextView tv_pay_bottomdia_time_minute;
    private String minute = "";
    private String second = "";
    private long lastOverTime = 900000;
    private Timer timer;
    private TimerTask task;
    private boolean isNeedCard;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private List<Coupon> couponSizeList = new ArrayList<Coupon>();
    private int couponId;
    private int canUseServiceCard;
    private String couponName = "";
    private String customerName;
    private int workerId;
    private String appointment;
    private int addressId;
    private String address;
    private String customerMobile;
    private double lat;
    private double lng;
    private String updatePetTips;
    private int flag;
    private List<ExtraItem> itemList = new ArrayList<ExtraItem>();
    private List<ExtraItem> itemList1 = new ArrayList<ExtraItem>();
    private List<ExtraItem> itemList2 = new ArrayList<ExtraItem>();
    private List<WashPetService> petServiceList = new ArrayList<WashPetService>();
    private List<WashPetService> updatePetServiceList = new ArrayList<WashPetService>();
    private StringBuilder serviceIds = new StringBuilder();
    private double yddTotalPrice;
    private double extraServicePriceUpdate;
    private double grainGoldPrice;
    private int pageType;
    private int couponSize;
    private int cardSize;

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    Log.e("TAG", "resultObj = " + resultObj.toString());
                    if (TextUtils.equals(resultStatus, "9000")) {
                        goPayResult();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(UpdateOrderConfirmNewActivity.this, orderStr,
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
                    tv_updateorderconfirmnew_minute.setText(minute);
                    tv_updateorderconfirmnew_second.setText(second);
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
                Log.e("TAG", "resp.errCode = " + resp.errCode);
                Log.e("TAG", "resp.errStr = " + resp.errStr);
                if (resp.errCode == 0) {
                    if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1")) {
                        Log.e("TAG", "OPPO哦");
                    } else {
                        goPayResult();
                    }
                } else {
                    if (Utils.isStrNull(resp.errStr)) {
                        ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "支付失败");
                    }
                }
            }
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
        getBalance();
    }

    private void initData() {
        Global.WXPAYCODE = -1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        orderNo = getIntent().getIntExtra("orderid", 0);
        oldpayway = spUtil.getInt("payway", 0);
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
    }

    private void findView() {
        setContentView(R.layout.activity_update_order_confirm_new);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("订单升级待确认");
        mPDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
    }

    private void setLinster() {

    }

    private void getBalance() {
        mPDialog.showDialog();
        CommUtil.getAccountBalance(this, spUtil.getString("cellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this),
                balanceHanler);
    }

    private AsyncHttpResponseHandler balanceHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("user") && !jdata.isNull("user")) {
                            JSONObject juser = jdata.getJSONObject("user");
                            if (juser.has("payPwd") && !juser.isNull("payPwd")) {
                                payPwd = juser.getInt("payPwd");
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "请求失败");
        }
    };

    private void getData() {
        mPDialog.showDialog();
        petServiceList.clear();
        itemList.clear();
        itemList1.clear();
        itemList2.clear();
        CommUtil.queryOrderDetailsNewTwo(this, orderNo, getOrderDetailsTwo);//拉取原订单数据
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
                        if (jData.has("totalPrice") && !jData.isNull("totalPrice")) {
                            yddTotalPrice = jData.getDouble("totalPrice");
                        }
                        if (jData.has("serviceLoc") && !jData.isNull("serviceLoc")) {
                            serviceLoc = jData.getInt("serviceLoc");
                        }
                        if (jData.has("extraServicePrice") && !jData.isNull("extraServicePrice")) {
                            extraServicePrice = jData.getDouble("extraServicePrice");
                        }
                        if (jData.has("orders") && !jData.isNull("orders")) {
                            JSONArray jarrorders = jData.getJSONArray("orders");
                            if (jarrorders.length() > 0) {
                                for (int i = 0; i < jarrorders.length(); i++) {
                                    WashPetService washPetService = new WashPetService();
                                    washPetService.setServiceLoc(serviceLoc);
                                    JSONObject jorders = jarrorders.getJSONObject(i);
                                    if (jorders.has("basicPrice") && !jorders.isNull("basicPrice")) {
                                        washPetService.setBasicServicePrice(jorders.getDouble("basicPrice"));
                                    }
                                    if (jorders.has("service") && !jorders.isNull("service")) {
                                        JSONObject jservice = jorders.getJSONObject("service");
                                        if (jservice.has("name") && !jservice.isNull("name")) {
                                            washPetService.setServiceName(jservice.getString("name"));
                                        }
                                        if (jservice.has("items") && !jservice.isNull("items")) {
                                            washPetService.setJcfwName(jservice.getString("items"));
                                        }
                                    }
                                    if (jorders.has("pet") && !jorders.isNull("pet")) {
                                        JSONObject jpet = jorders.getJSONObject("pet");
                                        if (jpet.has("name") && !jpet.isNull("name")) {
                                            washPetService.setPetName(jpet.getString("name"));
                                        }
                                    }
                                    if (jorders.has("customerPet") && !jorders.isNull("customerPet")) {
                                        JSONObject objectMyPet = jorders.getJSONObject("customerPet");
                                        if (objectMyPet.has("name") && !objectMyPet.isNull("name")) {
                                            washPetService.setPetName(objectMyPet.getString("name"));
                                        }
                                    }
                                    if (jorders.has("extraItems") && !jorders.isNull("extraItems")) {
                                        JSONArray jarrextraItems = jorders.getJSONArray("extraItems");
                                        if (jarrextraItems.length() > 0) {
                                            for (int j = 0; j < jarrextraItems.length(); j++) {
                                                ExtraItem extraItem = new ExtraItem();
                                                JSONObject jextraItems = jarrextraItems.getJSONObject(j);
                                                if (jextraItems.has("source") && !jextraItems.isNull("source")) {
                                                    extraItem.setSource(jextraItems.getInt("source"));
                                                }
                                                if (jextraItems.has("name") && !jextraItems.isNull("name")) {
                                                    extraItem.setItemName(jextraItems.getString("name"));
                                                }
                                                if (jextraItems.has("price") && !jextraItems.isNull("price")) {
                                                    extraItem.setPrice(jextraItems.getDouble("price"));
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
                                                double extraPrice = 0;
                                                StringBuffer sbItemName = new StringBuffer();
                                                for (int j = 0; j < itemList1.size(); j++) {
                                                    sbItemName.append(itemList1.get(j).getItemName() + " ¥" + itemList1.get(j).getPrice() + " ");
                                                    extraPrice = ComputeUtil.add(extraPrice, itemList1.get(j).getPrice());
                                                }
                                                washPetService.setExtraPrice(extraPrice);
                                                washPetService.setDxfwName(sbItemName.substring(0, sbItemName.toString().length() - 1));
                                            }
                                            if (itemList2.size() > 0) {
                                                double zjdxPrice = 0;
                                                StringBuffer sbItemName = new StringBuffer();
                                                for (int j = 0; j < itemList2.size(); j++) {
                                                    sbItemName.append(itemList2.get(j).getItemName() + " ¥" + itemList2.get(j).getPrice() + " ");
                                                    zjdxPrice = ComputeUtil.add(zjdxPrice, itemList2.get(j).getPrice());
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
                        if (jData.has("appointment") && !jData.isNull("appointment")) {
                            appointment = jData.getString("appointment");
                            Utils.setText(tvUpdateorderconfirmnewTime, appointment, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("customer") && !jData.isNull("customer")) {
                            JSONObject jcustomer = jData.getJSONObject("customer");
                            if (jcustomer.has("name") && !jcustomer.isNull("name")) {
                                customerName = jcustomer.getString("name");
                            }
                            if (jcustomer.has("phone") && !jcustomer.isNull("phone")) {
                                customerMobile = jcustomer.getString("phone");
                            }
                        }
                        if (jData.has("remark") && !jData.isNull("remark")) {
                            remark = jData.getString("remark");
                            Utils.setText(tvUpdateorderconfirmnewFwbz, remark, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("worker") && !jData.isNull("worker")) {
                            JSONObject workerObject = jData.getJSONObject("worker");
                            if (workerObject.has("id") && !workerObject.isNull("id")) {
                                workerId = workerObject.getInt("id");
                            }
                            if (workerObject.has("avatar") && !workerObject.isNull("avatar")) {
                                GlideUtil.loadCircleImg(mContext, workerObject.getString("avatar"), ivUpdateorderconfirmnewMrsicon, R.drawable.user_icon_unnet_circle);
                            }
                            if (workerObject.has("name") && !workerObject.isNull("name")) {
                                Utils.setText(tvUpdateorderconfirmnewMrsname, workerObject.getString("name"), "", View.VISIBLE, View.VISIBLE);
                            }
                        }
                        if (jData.has("shop") && !jData.isNull("shop")) {
                            JSONObject jshop = jData.getJSONObject("shop");
                            if (jshop.has("lat") && !jshop.isNull("lat")) {
                                shopLat = jshop.getDouble("lat");
                            }
                            if (jshop.has("lng") && !jshop.isNull("lng")) {
                                shopLng = jshop.getDouble("lng");
                            }
                            if (jshop.has("name") && !jshop.isNull("name")) {
                                shopName = jshop.getString("name");
                                Utils.setText(tvUpdateorderconfirmnewShop, shopName, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jshop.has("address") && !jshop.isNull("address")) {
                                shopAddress = jshop.getString("address");
                                Utils.setText(tvUpdateorderconfirmnewMddz, shopAddress, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jshop.has("phone") && !jshop.isNull("phone")) {
                                phoneNum = jshop.getString("phone");
                            }
                            if (jshop.has("id") && !jshop.isNull("id")) {
                                shopId = jshop.getInt("id");
                            }
                        }
                        if (jData.has("serviceAddress") && !jData.isNull("serviceAddress")) {
                            CommAddr serviceAddress = CommAddr.json2Entity(jData.getJSONObject("serviceAddress"));
                            if (serviceAddress != null) {
                                lat = serviceAddress.lat;
                                lng = serviceAddress.lng;
                                address = serviceAddress.address;
                                addressId = serviceAddress.Customer_AddressId;
                            }
                        }
                        if (jData.has("updateOrderId") && !jData.isNull("updateOrderId")) {
                            updateOrderId = jData.getInt("updateOrderId");
                        }
                        if (jData.has("updatePetTip") && !jData.isNull("updatePetTip")) {
                            updatePetTips = jData.getString("updatePetTip");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            //拉取升级订单数据
            mPDialog.showDialog();
            updatePetServiceList.clear();
            CommUtil.queryOrderDetailsNewTwo(mContext, updateOrderId, getOrderDetailsTwoUpdate);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private AsyncHttpResponseHandler getOrderDetailsTwoUpdate = new AsyncHttpResponseHandler() {
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
                        if (jdata.has("title") && !jdata.isNull("title")) {
                            Utils.setText(tvTitlebarTitle, jdata.getString("title"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("statusDesc") && !jdata.isNull("statusDesc")) {
                            Utils.setText(tvUpdateorderconfirmnewStatusname, jdata.getString("statusDesc"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("extraServicePrice") && !jdata.isNull("extraServicePrice")) {
                            extraServicePriceUpdate = jdata.getDouble("extraServicePrice");
                        }
                        if (jdata.has("pickup") && !jdata.isNull("pickup")) {
                            pickUp = jdata.getInt("pickup");
                        }
                        if (jdata.has("refType") && !jdata.isNull("refType")) {
                            refType = jdata.getInt("refType");
                        }
                        if (jdata.has("listPrice") && !jdata.isNull("listPrice")) {
                            updatePayPrice = jdata.getDouble("listPrice");
                        }
                        if (jdata.has("orders") && !jdata.isNull("orders")) {
                            JSONArray jarrorders = jdata.getJSONArray("orders");
                            if (jarrorders.length() > 0) {
                                for (int i = 0; i < jarrorders.length(); i++) {
                                    WashPetService washPetService = new WashPetService();
                                    JSONObject jorders = jarrorders.getJSONObject(i);
                                    if (jorders.has("listPrice") && !jorders.isNull("listPrice")) {
                                        washPetService.setBasicServicePrice(jorders.getDouble("listPrice"));
                                    }
                                    if (jorders.has("service") && !jorders.isNull("service")) {
                                        JSONObject jservice = jorders.getJSONObject("service");
                                        if (jservice.has("id") && !jservice.isNull("id")) {
                                            washPetService.setServiceId(jservice.getInt("id"));
                                        }
                                        if (jservice.has("name") && !jservice.isNull("name")) {
                                            washPetService.setServiceName(jservice.getString("name"));
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
                                        if (jpet.has("name") && !jpet.isNull("name")) {
                                            washPetService.setPetName(jpet.getString("name"));
                                        }
                                        if (jpet.has("id") && !jpet.isNull("id")) {
                                            washPetService.setPetId(jpet.getInt("id"));
                                        }
                                    }
                                    if (jorders.has("customerPet") && !jorders.isNull("customerPet")) {
                                        JSONObject objectMyPet = jorders.getJSONObject("customerPet");
                                        if (objectMyPet.has("name") && !objectMyPet.isNull("name")) {
                                            washPetService.setPetName(objectMyPet.getString("name"));
                                        }
                                        if (objectMyPet.has("id") && !objectMyPet.isNull("id")) {
                                            washPetService.setMyPetId(objectMyPet.getInt("id"));
                                        }
                                    }
                                    if (jorders.has("extraItems") && !jorders.isNull("extraItems")) {
                                        JSONArray jarrextraItems = jorders.getJSONArray("extraItems");
                                        if (jarrextraItems.length() > 0) {
                                            serviceIds.setLength(0);
                                            StringBuffer sbItemName = new StringBuffer();
                                            for (int j = 0; j < jarrextraItems.length(); j++) {
                                                JSONObject jextraItems = jarrextraItems.getJSONObject(j);
                                                if (jextraItems.has("id") && !jextraItems.isNull("id")) {
                                                    if (j == jarrextraItems.length() - 1) {
                                                        serviceIds.append(jextraItems.getInt("id"));
                                                    } else {
                                                        serviceIds.append(jextraItems.getInt("id") + ",");
                                                    }
                                                }
                                                if (jextraItems.has("name") && !jextraItems.isNull("name")) {
                                                    sbItemName.append(jextraItems.getString("name"));
                                                }
                                                if (jextraItems.has("listPrice") && !jextraItems.isNull("listPrice")) {
                                                    sbItemName.append("¥" + jextraItems.getDouble("listPrice"));
                                                }
                                            }
                                            washPetService.setItemIds(serviceIds.toString());
                                            washPetService.setZjdxName(sbItemName.toString());
                                        }
                                    }
                                    updatePetServiceList.add(washPetService);
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "数据异常");
            }
            if (extraServicePrice > 0 && (refType == 2 || refType == 3)) {
                if (Utils.isStrNull(petServiceList.get(0).getDxfwName())) {
                    petServiceList.get(0).setDxfwName(petServiceList.get(0).getDxfwName() + " 上门服务费¥" + extraServicePrice);
                } else {
                    petServiceList.get(0).setDxfwName("上门服务费¥" + extraServicePrice);
                }
            }
            Utils.setText(tvItemUpdateorderconfirmnewPetJcfw, petServiceList.get(0).getJcfwName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvUpdateorderconfirmnewYddprice, "¥" + yddTotalPrice, "", View.VISIBLE, View.VISIBLE);
            if (itemList2.size() > 0) {
                Utils.setText(tv_item_updateorderconfirmnew_pet_zjdxfw, petServiceList.get(0).getZjdxName(), "", View.VISIBLE, View.VISIBLE);
                ll_item_updateorderconfirmnew_pet_zjdxfw.setVisibility(View.VISIBLE);
            } else {
                ll_item_updateorderconfirmnew_pet_zjdxfw.setVisibility(View.GONE);
            }
            if (Utils.isStrNull(petServiceList.get(0).getDxfwName())) {
                llItemUpdateorderconfirmnewPetDxfw.setVisibility(View.VISIBLE);
                Utils.setText(tvItemUpdateorderconfirmnewPetDxfw, petServiceList.get(0).getDxfwName(), "", View.VISIBLE, View.VISIBLE);
            } else {
                llItemUpdateorderconfirmnewPetDxfw.setVisibility(View.GONE);
            }
            SpannableString ss = new SpannableString(petServiceList.get(0).getPetName() + "  " + petServiceList.get(0).getServiceName() + " ¥" + petServiceList.get(0).getBasicServicePrice());
            ss.setSpan(new TextAppearanceSpan(mContext, R.style.shisan_normal_333333), (petServiceList.get(0).getPetName() + "  ").length(),
                    ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tvItemUpdateorderconfirmnewPetName.setText(ss);
            if (serviceLoc == 1) {
                tvItemUpdateorderconfirmnewPetServiceloc.setText("到店");
                tvItemUpdateorderconfirmnewPetServiceloc.setBackgroundDrawable(Utils.getDW("FAA04A"));
                tvUpdateorderconfirmnewJs.setVisibility(View.VISIBLE);
                if (pickUp == 1) {
                    tvUpdateorderconfirmnewJs.setText("(需要接送)");
                } else {
                    tvUpdateorderconfirmnewJs.setText("(不接送)");
                }
                Utils.setText(tvUpdateorderconfirmnewFwfs, "到店服务", "", View.VISIBLE, View.VISIBLE);
            } else if (serviceLoc == 2) {
                tvItemUpdateorderconfirmnewPetServiceloc.setText("上门");
                tvItemUpdateorderconfirmnewPetServiceloc.setBackgroundDrawable(Utils.getDW("2FC0F0"));
                Utils.setText(tvUpdateorderconfirmnewFwfs, "上门服务", "", View.VISIBLE, View.VISIBLE);
                tvUpdateorderconfirmnewJs.setVisibility(View.GONE);
            }
            Utils.setText(tvUpdateorderconfirmnewSjprice, "¥" + updatePayPrice, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvUpdateorderconfirmnewTotalprice, "¥" + updatePayPrice, "", View.VISIBLE, View.VISIBLE);
            if (refType == 1) {//追加单项
                Utils.setText(tvUpdateorderconfirmnewSjinfo, updatePetServiceList.get(0).getZjdxName(), "", View.VISIBLE, View.VISIBLE);
                tvUpdateorderconfirmnewSjname.setText("追加单项");
                tvUpdateorderconfirmnewExtrafee.setVisibility(View.GONE);
                rl_updateorderconfirmnew_coupon.setVisibility(View.GONE);
                isNeedCard = true;
                getPayWay();
            } else if (refType == 2 || refType == 3) {//升级服务
                if (Utils.isStrNull(updatePetServiceList.get(0).getZjdxName())) {
                    Utils.setText(tvUpdateorderconfirmnewSjinfo, updatePetServiceList.get(0).getServiceName() + "¥" + updatePetServiceList.get(0).getBasicServicePrice() + updatePetServiceList.get(0).getZjdxName(), "", View.VISIBLE, View.VISIBLE);
                } else {
                    Utils.setText(tvUpdateorderconfirmnewSjinfo, updatePetServiceList.get(0).getServiceName() + "¥" + updatePetServiceList.get(0).getBasicServicePrice(), "", View.VISIBLE, View.VISIBLE);
                }
                rl_updateorderconfirmnew_coupon.setVisibility(View.VISIBLE);
                tvUpdateorderconfirmnewSjname.setText("升级服务");
                if (serviceLoc == 2 && extraServicePriceUpdate > 0) {
                    tvUpdateorderconfirmnewExtrafee.setVisibility(View.VISIBLE);
                    Utils.setText(tvUpdateorderconfirmnewExtrafee, "上门服务费¥" + extraServicePriceUpdate, "", View.VISIBLE, View.VISIBLE);
                } else {
                    tvUpdateorderconfirmnewExtrafee.setVisibility(View.GONE);
                }
                getCoupon();
            }
            SecondTimeDown();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "请求失败");
        }
    };

    private void SecondTimeDown() {
        mPDialog.showDialog();
        CommUtil.CareTimes(mContext, updateOrderId, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int resultCode = object.getInt("code");
                String msg = object.getString("msg");
                if (resultCode == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("residualTime")
                                && !objectData.isNull("residualTime")) {
                            lastOverTime = objectData.getLong("residualTime");
                            timerDown();
                        }
                    }
                } else {
                    ToastUtil.showToastShortCenter(mContext,
                            msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

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

    private String getPetID_ServiceId_MyPetId_ItemIds() {
        StringBuffer sb = new StringBuffer();
        if (updatePetServiceList != null && updatePetServiceList.size() > 0) {
            for (int i = 0; i < updatePetServiceList.size(); i++) {
                WashPetService washPetService = updatePetServiceList.get(i);
                if (washPetService != null) {
                    if (i < updatePetServiceList.size() - 1) {
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

    private void getCoupon() {
        mPDialog.showDialog();
        couponList.clear();
        couponId = 0;
        canUseServiceCard = 0;
        couponName = "";
        tv_updateorderconfirmnew_couponname.setText("无可用");
        tv_updateorderconfirmnew_couponname.setTextColor(getResources().getColor(R.color.aBBBBBB));
        double sub = 0;
        if (serviceLoc == 2 && extraServicePriceUpdate > 0 && (refType == 2 || refType == 3)) {
            sub = ComputeUtil.sub(updatePayPrice, extraServicePriceUpdate);
        } else {
            sub = updatePayPrice;
        }
        CommUtil.getAvailableCoupon(this, spUtil.getString("cellphone", ""), Global.getIMEI(this),
                Global.getCurrentVersion(this), appointment + ":00", 1, serviceLoc,
                workerId, pickUp, customerName, customerMobile, addressId, address, lat, lng, getPetID_ServiceId_MyPetId_ItemIds(),
                0, sub, shopId, null, 1, serviceCardId, updateOrderId, 0, couponHanler);
    }

    private AsyncHttpResponseHandler couponHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray array = jobj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            couponList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (couponList.size() > 0) {
                for (int i = 0; i < couponList.size(); i++) {
                    if (couponList.get(i).isAvali == 1) {
                        couponId = couponList.get(0).id;
                        canUseServiceCard = couponList.get(0).canUseServiceCard;
                        couponName = couponList.get(0).name;
                        break;
                    }
                }
            }
            if (canUseServiceCard == 1) {
                isNeedCard = false;
            } else {
                isNeedCard = true;
            }
            getPayWay();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
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
                    ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "数据异常");
            }
            if (listpayWays != null && listpayWays.length() > 0) {
                if (listpayWays.toString().contains("11")) {// E卡
                    rlUpdateorderconfirmnewCard.setVisibility(View.VISIBLE);
                    if (isNeedCard) {
                        getMyCard();
                    } else {
                        flag = 0;
                        confirmOrderPromptNew();
                    }
                } else {
                    rlUpdateorderconfirmnewCard.setVisibility(View.GONE);
                    flag = 0;
                    confirmOrderPromptNew();
                }
            } else {
                rlUpdateorderconfirmnewCard.setVisibility(View.GONE);
                flag = 0;
                confirmOrderPromptNew();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "请求失败");
        }
    };

    private void getMyCard() {
        serviceCardId = -1;
        serviceCardName = "";
        discountText = "";
        tvUpdateorderconfirmNewCardname.setTextColor(getResources().getColor(R.color.aBBBBBB));
        Utils.setText(tvUpdateorderconfirmNewCardname, "无可用", "", View.VISIBLE, View.VISIBLE);
        mPDialog.showDialog();
        cardList.clear();
        CommUtil.serviceCardList(this, shopId, 0, updatePayPrice, Global.ORDERKEY[8], appointment + ":00", cardListHandler);
    }

    private AsyncHttpResponseHandler cardListHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("available") && !jdata.isNull("available")) {
                            JSONArray jarravailable = jdata.getJSONArray("available");
                            if (jarravailable.length() > 0) {
                                for (int i = 0; i < jarravailable.length(); i++) {
                                    MyCard myCard = new MyCard();
                                    JSONObject javailable = jarravailable.getJSONObject(i);
                                    if (javailable.has("serviceCardTypeName") && !javailable.isNull("serviceCardTypeName")) {
                                        myCard.setCardTypeName(javailable.getString("serviceCardTypeName"));
                                    }
                                    if (javailable.has("smallPic") && !javailable.isNull("smallPic")) {
                                        myCard.setSmallPic(javailable.getString("smallPic"));
                                    }
                                    if (javailable.has("nowDiscountDescribe") && !javailable.isNull("nowDiscountDescribe")) {
                                        myCard.setDiscountText(javailable.getString("nowDiscountDescribe"));
                                    }
                                    if (javailable.has("availableBalance") && !javailable.isNull("availableBalance")) {
                                        myCard.setAmount(javailable.getDouble("availableBalance"));
                                    }
                                    if (javailable.has("id") && !javailable.isNull("id")) {
                                        myCard.setId(javailable.getInt("id"));
                                    }
                                    if (javailable.has("discount") && !javailable.isNull("discount")) {
                                        myCard.setDiscount(javailable.getDouble("discount"));
                                    }
                                    cardList.add(myCard);
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "数据异常");
                e.printStackTrace();
            }
            if (cardList != null && cardList.size() > 0) {
                serviceCardId = cardList.get(0).getId();
                serviceCardName = cardList.get(0).getCardTypeName();
                discountText = cardList.get(0).getDiscountText();
            }
            flag = 0;
            confirmOrderPromptNew();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "请求失败");
        }
    };

    private void confirmOrderPromptNew() {
        thirdPrice = 0;
        normalCouponDiscountPrice = 0;
        cardDiscountPrice = 0;
        grainGoldPrice = 0;
        mPDialog.showDialog();
        CommUtil.confirmOrderPromptNew(null, mContext, 1, serviceLoc, getPetID_ServiceId_MyPetId_ItemIds(),
                workerId, 0, appointment + ":00", null, null, pickUp, shopId, serviceCardId, couponId, 0, updateOrderId, confirmOrderPromptHandler);
    }

    private AsyncHttpResponseHandler confirmOrderPromptHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("grainGoldPrice") && !objectData.isNull("grainGoldPrice")) {
                            grainGoldPrice = objectData.getDouble("grainGoldPrice");
                        }
                        if (objectData.has("cardPayPrice") && !objectData.isNull("cardPayPrice")) {
                            cardPayPrice = objectData.getDouble("cardPayPrice");
                        }
                        if (objectData.has("thirdPrice") && !objectData.isNull("thirdPrice")) {
                            thirdPrice = objectData.getDouble("thirdPrice");
                        }
                        if (objectData.has("normalCouponDiscountPrice") && !objectData.isNull("normalCouponDiscountPrice")) {
                            normalCouponDiscountPrice = objectData.getDouble("normalCouponDiscountPrice");
                        }
                        if (objectData.has("cardDiscountPrice") && !objectData.isNull("cardDiscountPrice")) {
                            cardDiscountPrice = objectData.getDouble("cardDiscountPrice");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (couponId > 0 && serviceCardId > 0 && cardPayPrice <= 0) {
                if (flag == 1) {//选择优惠券回来
                    serviceCardId = -1;
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("提示").setMsg("当前选择的优惠券已完全抵扣订单金额，已为您清除选中的E卡").isOneBtn(true).setPositiveButton("我知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                } else if (flag == 0) {//清空E卡
                    serviceCardId = -1;
                }
                //再调一次本接口
                confirmOrderPromptNew();
            }
            if (couponId > 0) {
                rl_updateorderconfirmnew_couponprice.setVisibility(View.VISIBLE);
                Utils.setText(tv_updateorderconfirmnew_couponprice, "-¥" + normalCouponDiscountPrice, "", View.VISIBLE, View.VISIBLE);
                tv_updateorderconfirmnew_couponname.setText("-" + normalCouponDiscountPrice + "(" + couponName + ")");
                tv_updateorderconfirmnew_couponname.setTextColor(getResources().getColor(R.color.aD0021B));
            } else {
                rl_updateorderconfirmnew_couponprice.setVisibility(View.GONE);
                couponName = "";
                canUseServiceCard = 0;
            }
            if (serviceCardId > 0) {
                tvUpdateorderconfirmNewCardname.setTextColor(getResources().getColor(R.color.aD0021B));
                Utils.setText(tvUpdateorderconfirmNewCardname, serviceCardName + (Utils.isStrNull(discountText) ? "(" + discountText.replace("@@", "") + ")" : ""), "", View.VISIBLE, View.VISIBLE);
                if (cardDiscountPrice > 0) {
                    rlUpdateorderconfirmnewCarddiscountprice.setVisibility(View.VISIBLE);
                    Utils.setText(tvUpdateorderconfirmnewCarddiscountprice, "-¥" + cardDiscountPrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlUpdateorderconfirmnewCarddiscountprice.setVisibility(View.GONE);
                }
                if (cardPayPrice > 0) {
                    rl_updateorderconfirmnew_cardpay.setVisibility(View.VISIBLE);
                    Utils.setText(tvUpdateorderconfirmnewCardprice, "¥" + cardPayPrice, "", View.VISIBLE, View.VISIBLE);
                    Utils.setText(tvUpdateorderconfirmnewCardnamePay, serviceCardName + "支付" + (Utils.isStrNull(discountText) ? "(" + discountText.replace("@@", "") + ")" : ""), "", View.VISIBLE, View.VISIBLE);
                } else {
                    rl_updateorderconfirmnew_cardpay.setVisibility(View.GONE);
                }
            } else {
                rl_updateorderconfirmnew_cardpay.setVisibility(View.GONE);
                rlUpdateorderconfirmnewCarddiscountprice.setVisibility(View.GONE);
                serviceCardName = "";
                discountText = "";
            }
            Utils.setText(tvUpdateorderconfirmnewPayprice, "¥" + thirdPrice, "", View.VISIBLE, View.VISIBLE);

            //计算可用礼品卡数量
            if (listpayWays != null && listpayWays.length() > 0 && listpayWays.toString().contains("11") && serviceCardId <= 0) {
                mPDialog.showDialog();
                cardSize = 0;
                CommUtil.serviceCardList(mContext, shopId, 0, updatePayPrice, Global.ORDERKEY[8], appointment + ":00", cardSizeHandler);
            }
            //计算可用优惠券数量
            if (couponId <= 0) {
                mPDialog.showDialog();
                couponSize = 0;
                couponSizeList.clear();
                double sub = 0;
                if (serviceLoc == 2 && extraServicePriceUpdate > 0 && (refType == 2 || refType == 3)) {
                    sub = ComputeUtil.sub(updatePayPrice, extraServicePriceUpdate);
                } else {
                    sub = updatePayPrice;
                }
                CommUtil.getAvailableCoupon(mContext, spUtil.getString("cellphone", ""), Global.getIMEI(mContext),
                        Global.getCurrentVersion(mContext), appointment + ":00", 1, serviceLoc,
                        workerId, pickUp, customerName, customerMobile, addressId, address, lat, lng, getPetID_ServiceId_MyPetId_ItemIds(),
                        0, sub, shopId, null, 1, serviceCardId, updateOrderId, 0, couponSizeHanler);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private AsyncHttpResponseHandler cardSizeHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("available") && !jdata.isNull("available")) {
                            JSONArray jarravailable = jdata.getJSONArray("available");
                            if (jarravailable.length() > 0) {
                                cardSize = jarravailable.length();
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                e.printStackTrace();
            }
            if (cardSize > 0) {
                tvUpdateorderconfirmNewCardname.setText(cardSize + "张可用");
                tvUpdateorderconfirmNewCardname.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tvUpdateorderconfirmNewCardname.setTextColor(getResources().getColor(R.color.aBBBBBB));
                Utils.setText(tvUpdateorderconfirmNewCardname, "无可用", "", View.VISIBLE, View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private AsyncHttpResponseHandler couponSizeHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray array = jobj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            couponSizeList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (couponSizeList.size() > 0) {
                if (serviceCardId > 0) {
                    if (couponSizeList.size() > 0) {
                        for (int i = 0; i < couponSizeList.size(); i++) {
                            if (couponSizeList.get(i).isAvali == 1 && couponSizeList.get(i).canUseServiceCard == 0) {
                                couponSize++;
                            }
                        }
                    }
                } else {
                    if (couponSizeList.size() > 0) {
                        for (int i = 0; i < couponSizeList.size(); i++) {
                            if (couponSizeList.get(i).isAvali == 1) {
                                couponSize++;
                            }
                        }
                    }
                }
            }
            if (couponSize > 0) {
                tv_updateorderconfirmnew_couponname.setText(couponSize + "张可用");
                tv_updateorderconfirmnew_couponname.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tv_updateorderconfirmnew_couponname.setText("无可用");
                tv_updateorderconfirmnew_couponname.setTextColor(getResources().getColor(R.color.aBBBBBB));
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.bnt_updateorderconfirmnew_gopay,
            R.id.rl_updateorderconfirmnew_card, R.id.iv_updateorderconfirmnew_call, R.id.ll_updateorderconfirmnew_fwbz,
            R.id.ll_updateorderconfirmnew_mddz, R.id.rl_updateorderconfirmnew_coupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_updateorderconfirmnew_coupon:
                mPDialog.showDialog();
                couponList.clear();
                double sub = 0;
                if (serviceLoc == 2 && extraServicePriceUpdate > 0 && (refType == 2 || refType == 3)) {
                    sub = ComputeUtil.sub(updatePayPrice, extraServicePriceUpdate);
                } else {
                    sub = updatePayPrice;
                }
                CommUtil.getAvailableCoupon(this, spUtil.getString("cellphone", ""), Global.getIMEI(this),
                        Global.getCurrentVersion(this), appointment + ":00", 1, serviceLoc,
                        workerId, pickUp, customerName, customerMobile, addressId, address, lat, lng, getPetID_ServiceId_MyPetId_ItemIds(), 0, sub, shopId,
                        null, 1, serviceCardId, updateOrderId, 0, localCouponHanler);
                break;
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.bnt_updateorderconfirmnew_gopay:
                if (Utils.isStrNull(updatePetTips)) {
                    new AlertDialogNavAndPost(mContext)
                            .builder()
                            .setTitle("")
                            .setMsg(updatePetTips)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (thirdPrice <= 0) {
                                        if (serviceCardId > 0) {
                                            paytype = 11;
                                        } else if (couponId > 0) {
                                            paytype = 3;
                                        }
                                        if (cardPayPrice <= 0) {
                                            updateNewOrder();
                                        } else {
                                            //判断是否设置支付密码
                                            if (payPwd == 0) {//未设置过支付密码
                                                boolean isNextSetPayPwd = spUtil.getBoolean("isNextSetPayPwd", false);
                                                if (isNextSetPayPwd) {
                                                    updateNewOrder();
                                                } else {
                                                    new AlertDialogDefault(mContext).builder()
                                                            .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            spUtil.saveBoolean("isNextSetPayPwd", true);
                                                            updateNewOrder();
                                                        }
                                                    }).setPositiveButton("设置", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            ActivityUtils.toSetPassword(mContext);
                                                        }
                                                    }).show();
                                                }
                                            } else if (payPwd == 1) {//已设置过支付密码
                                                //判断设备是否录入指纹
                                                boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                                                //判断是否开启指纹支付
                                                boolean isFinger = spUtil.getBoolean("isFinger", false);
                                                if (isFinger && hasEnrolledFingerprints) {
                                                    showPayPwdSystem(1, false);
                                                } else {
                                                    showPayPwdSystem(0, false);
                                                }
                                            }
                                        }
                                    } else {
                                        if (listpayWays != null && listpayWays.length() > 0) {
                                            if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                                                showPayDialog();
                                            }
                                        }
                                    }
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                } else {
                    if (thirdPrice <= 0) {
                        if (serviceCardId >= 0) {
                            paytype = 11;
                        } else {
                            paytype = 3;
                        }
                        if (cardPayPrice <= 0) {
                            updateNewOrder();
                        } else {
                            //判断是否设置支付密码
                            if (payPwd == 0) {//未设置过支付密码
                                boolean isNextSetPayPwd = spUtil.getBoolean("isNextSetPayPwd", false);
                                if (isNextSetPayPwd) {
                                    updateNewOrder();
                                } else {
                                    new AlertDialogDefault(mContext).builder()
                                            .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            spUtil.saveBoolean("isNextSetPayPwd", true);
                                            updateNewOrder();
                                        }
                                    }).setPositiveButton("设置", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivityForResult(new Intent(UpdateOrderConfirmNewActivity.this, SetUpPayPwdActivity.class), Global.ORDERPAY_TO_SETUPPAYPWD);
                                        }
                                    }).show();
                                }
                            } else if (payPwd == 1) {//已设置过支付密码
                                //判断设备是否录入指纹
                                boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                                //判断是否开启指纹支付
                                boolean isFinger = spUtil.getBoolean("isFinger", false);
                                if (isFinger && hasEnrolledFingerprints) {
                                    showPayPwdSystem(1, false);
                                } else {
                                    showPayPwdSystem(0, false);
                                }
                            }
                        }
                    } else {
                        if (listpayWays != null && listpayWays.length() > 0) {
                            if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                                showPayDialog();
                            }
                        }
                    }
                }
                break;
            case R.id.rl_updateorderconfirmnew_card:
                Intent intent2 = new Intent(mContext, SelectMyCardActivity.class);
                intent2.putExtra("id", serviceCardId);
                intent2.putExtra("type", 0);
                intent2.putExtra("shopId", shopId);
                intent2.putExtra("orderKey", Global.ORDERKEY[8]);
                intent2.putExtra("payPrice", updatePayPrice);
                intent2.putExtra("flag", 3);
                intent2.putExtra("serviceLoc", serviceLoc);
                intent2.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds());
                intent2.putExtra("workerId", workerId);
                intent2.putExtra("appointment", appointment + ":00");
                intent2.putExtra("pickup", pickUp);
                intent2.putExtra("couponId", couponId);
                intent2.putExtra("updateOrderId", updateOrderId);
                intent2.putExtra("canUseServiceCard", canUseServiceCard);
                startActivityForResult(intent2, Global.FOSORDERCONFIRM_TO_MYCARD);
                break;
            case R.id.iv_updateorderconfirmnew_call:
                MDialog mDialog = new MDialog.Builder(mContext)
                        .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                        .setMessage("是否拨打电话?").setCancelStr("否")
                        .setOKStr("是")
                        .positiveListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 确认拨打电话
                                Utils.telePhoneBroadcast(mContext, phoneNum);
                            }
                        }).build();
                mDialog.show();
                break;
            case R.id.ll_updateorderconfirmnew_fwbz:
                Intent intent1 = new Intent(mContext, OrderDetailServiceNoteActivity.class);
                intent1.putExtra("note", remark);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_open, R.anim.bottom_silent);
                break;
            case R.id.ll_updateorderconfirmnew_mddz:
                Intent intent3 = new Intent(mContext, ShopLocationActivity.class);
                intent3.putExtra("shoplat", shopLat);
                intent3.putExtra("shoplng", shopLng);
                intent3.putExtra("shopaddr", shopAddress);
                intent3.putExtra("shopname", shopName);
                startActivity(intent3);
                break;
        }
    }

    private AsyncHttpResponseHandler localCouponHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray array = jobj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            couponList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            Intent intent3 = new Intent(mContext, AvailableCouponActivity.class);
            intent3.putExtra("couponId", couponId);
            intent3.putExtra("couponList", (Serializable) couponList);
            startActivityForResult(intent3, Global.ORDERDETAILREQUESTCODE_COUPON);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void updateNewOrder() {
        mPDialog.showDialog();
        CommUtil.workerUpdatePay(null, spUtil.getString("cellphone", ""),
                Global.getIMEI(this), this, updateOrderId,
                spUtil.getInt("userid", 0), null, null, null, -1, -1, -1, ComputeUtil.add(thirdPrice, cardPayPrice), paytype,
                couponId, -1, null, false, null, null, "", 0, 0, serviceCardId, changeHanler);
    }

    private AsyncHttpResponseHandler changeHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                Log.e("TAG", "升级支付" + new String(responseBody));
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("orderId") && !jData.isNull("orderId")) {
                            orderNo = Integer.parseInt(jData.getString("orderId"));
                        }
                        if (jData.has("payInfo") && !jData.isNull("payInfo")) {
                            JSONObject jpayInfo = jData.getJSONObject("payInfo");
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
                        if (jData.has("give_can") && !jData.isNull("give_can")) {
                            JSONObject obGiveCan = jData.getJSONObject("give_can");
                            if (obGiveCan.has("myself") && !obGiveCan.isNull("myself")) {
                                myselfDummpNum = obGiveCan.getInt("myself");
                            }
                        }
                        if (thirdPrice <= 0) {
                            //直接跳转到支付成功
                            goPayResult();
                        } else {
                            goPay();
                        }
                    }
                } else {
                    if (resultCode == Global.PAY_SUCCESS) {
                        goPayResult();
                    } else {
                        setPayLoadFail();
                    }
                    ToastUtil.showToastShort(UpdateOrderConfirmNewActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "数据异常");
                setPayLoadFail();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "请求失败");
            setPayLoadFail();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.ORDERDETAILREQUESTCODE_COUPON) {//选择优惠券回来
                couponId = data.getIntExtra("couponid", 0);
                canUseServiceCard = data.getIntExtra("canUseServiceCard", 0);
                couponName = data.getStringExtra("name");
                flag = 1;
                confirmOrderPromptNew();
            } else if (requestCode == Global.FOSORDERCONFIRM_TO_MYCARD) {//选择E卡回来
                serviceCardId = data.getIntExtra("id", -1);
                serviceCardName = data.getStringExtra("cardTypeName");
                discountText = data.getStringExtra("discountText");
                int couponFlag = data.getIntExtra("couponFlag", 0);
                if (couponId > 0 && couponFlag == 1) {//清除选中的优惠券
                    couponId = 0;
                }
                confirmOrderPromptNew();
            } else if (requestCode == Global.ORDERPAY_TO_STARTFINGER) {
                goPaySuccess();
            }
        }
    }

    //支付密码设置结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetPayPwdSuccessEvent event) {
        if (event != null && event.isSuccess()) {
            getBalance();
            showPayPwdSystem(0, false);
        }
    }

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(UpdateOrderConfirmNewActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(UpdateOrderConfirmNewActivity.this, R.layout.appoint_pay_bottom_dialog, null);
        Button btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        tv_pay_bottomdia_time_minute = (TextView) customView.findViewById(R.id.tv_pay_bottomdia_time_minute);
        tv_pay_bottomdia_time_second = (TextView) customView.findViewById(R.id.tv_pay_bottomdia_time_second);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        final ImageView iv_pay_bottomdia_zfb_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_zfb_select);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(UpdateOrderConfirmNewActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        btn_pay_bottomdia.setText("确认支付¥" + thirdPrice);
        tv_pay_bottomdia_time_minute.setText(minute);
        tv_pay_bottomdia_time_second.setText(second);
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
                updateNewOrder();
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

    private void goPay() {
        if (paytype == 1) {// 微信支付
            mPDialog.showDialog();
            spUtil.saveInt("payway", 1);
            PayUtils.weChatPayment(UpdateOrderConfirmNewActivity.this, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(UpdateOrderConfirmNewActivity.this, aliHandler);
        }
    }

    private void goPayResult() {
        if (thirdPrice <= 0 && pWinPayPwdSystem != null) {
            tv_paypwdsystem_pop_pwdtype.setVisibility(View.GONE);
            tv_paypwdsystem_pop_payresult.setText("支付成功");
            csv_paypwdsystem_pop.loadSuccess();
            //判断是否是首次设置
            boolean isFirstSetPwd = spUtil.getBoolean("isFirstSetPwd", false);
            if (isFirstSetPwd) {//不是首次设置
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pWinPayPwdSystem.dismiss();
                        goPaySuccess();
                    }
                }, 1500);
            } else {
                //判断是否开启指纹支付
                boolean isFinger = spUtil.getBoolean("isFinger", false);
                if (isFinger) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pWinPayPwdSystem.dismiss();
                            goPaySuccess();
                        }
                    }, 1500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pWinPayPwdSystem.dismiss();
                            goPaySuccess();
                            if (mFingerprintCore.isSupport()) {
                                spUtil.saveBoolean("isFirstSetPwd", true);
                                startActivityForResult(new Intent(UpdateOrderConfirmNewActivity.this, SetFingerprintActivity.class).putExtra("flag",1), Global.ORDERPAY_TO_STARTFINGER);
                            } else {
                                goPaySuccess();
                            }
                        }
                    }, 1500);
                }
            }
        } else {
            goPaySuccess();
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
        intent.putExtra("orderId", orderNo);
        intent.putExtra("fx_price", grainGoldPrice);
        intent.putExtra("type", 1);
        discountList.clear();
        if (normalCouponDiscountPrice > 0) {
            discountList.add("优惠券优惠¥" + normalCouponDiscountPrice);
        }
        if (cardDiscountPrice > 0) {
            discountList.add("E卡优惠¥" + cardDiscountPrice);
        }
        if (discountList.size() > 0) {
            discountList.add(0, "共计优惠¥" + ComputeUtil.add(normalCouponDiscountPrice, cardDiscountPrice));
            intent.putStringArrayListExtra("discountList", discountList);
        }
        String subString = getServiceName(updatePetServiceList);
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
        intent.putExtra("payPrice", ComputeUtil.add(thirdPrice, cardPayPrice));
        intent.putExtra("myself", myselfDummpNum);
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

    private void setPayLoadFail() {
        if (thirdPrice <= 0 && pWinPayPwdSystem != null && pWinPayPwdSystem.isShowing()) {
            tv_paypwdsystem_pop_pwdtype.setVisibility(View.GONE);
            tv_paypwdsystem_pop_payresult.setText("支付失败");
            csv_paypwdsystem_pop.loadFailure();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pWinPayPwdSystem.dismiss();
                }
            }, 1500);
        }
    }

    private AsyncHttpResponseHandler checkPayPwdHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    tv_paypwdsystem_pop_pwdtype.setVisibility(View.GONE);
                    tv_paypwdsystem_pop_payresult.setText("正在确认");
                    ll_paypwdsystem_pop_payresult.setVisibility(View.VISIBLE);
                    rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                    rl_paypwdsystem_pop_pwd.setVisibility(View.GONE);
                    tv_paypwdsystem_pop_title.setVisibility(View.GONE);
                    csv_paypwdsystem_pop.loadLoading();
                    //密码校验成功，调取支付接口
                    updateNewOrder();
                } else {
                    cv_paypwdsystem_pop_pwd.clear();
                    rl_paypwdsystem_pwd.startAnimation(shakeAnimation(5));
                    tv_paypwdsystem_pop_pwderror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_paypwdsystem_pop_pwderror.setText(msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(UpdateOrderConfirmNewActivity.this, "请求失败");
        }
    };

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    private Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    private void showPayPwdSystem(int flag, boolean isPwd) {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(UpdateOrderConfirmNewActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        pwdType = flag;
        pWinPayPwdSystem = null;
        if (pWinPayPwdSystem == null) {
            ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.paypwdsystem_pop, null);
            iv_paypwdsystem_pop_close = (ImageView) customView.findViewById(R.id.iv_paypwdsystem_pop_close);
            tv_paypwdsystem_pop_pwdtype = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_pwdtype);
            tv_paypwdsystem_pop_title = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_title);
            rl_paypwdsystem_pop_select = (RelativeLayout) customView.findViewById(R.id.rl_paypwdsystem_pop_select);
            btn_paypwdsystem_pop = (Button) customView.findViewById(R.id.btn_paypwdsystem_pop);
            tv_paypwdsystem_pop_payprice = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_payprice);
            rl_paypwdsystem_pop_pwd = (RelativeLayout) customView.findViewById(R.id.rl_paypwdsystem_pop_pwd);
            rl_paypwdsystem_pwd = (RelativeLayout) customView.findViewById(R.id.rl_paypwdsystem_pwd);
            cv_paypwdsystem_pop_pwd = (CodeView) customView.findViewById(R.id.cv_paypwdsystem_pop_pwd);
            tv_paypwdsystem_pop_wjmm = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_wjmm);
            tv_paypwdsystem_pop_pwderror = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_pwderror);
            kbv_paypwdsystem_pop = (KeyboardView) customView.findViewById(R.id.kbv_paypwdsystem_pop);
            ll_paypwdsystem_pop_payresult = (LinearLayout) customView.findViewById(R.id.ll_paypwdsystem_pop_payresult);
            csv_paypwdsystem_pop = (CustomStatusView) customView.findViewById(R.id.csv_paypwdsystem_pop);
            tv_paypwdsystem_pop_payresult = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_payresult);
            TextView tv_paypwdsystem_pop_paywaytitle = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_paywaytitle);
            tv_paypwdsystem_pop_paywaytitle.setText("E卡");
            pWinPayPwdSystem = new PopupWindow(customView,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            pWinPayPwdSystem.setFocusable(true);// 取得焦点
            //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
            pWinPayPwdSystem.setBackgroundDrawable(new BitmapDrawable());
            //点击外部消失
            pWinPayPwdSystem.setOutsideTouchable(true);
            //设置可以点击
            pWinPayPwdSystem.setTouchable(true);
            //进入退出的动画
            pWinPayPwdSystem.setAnimationStyle(R.style.mypopwindow_anim_style);
            pWinPayPwdSystem.setWidth(Utils.getDisplayMetrics(this)[0]);
            pWinPayPwdSystem.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
            pWinPayPwdSystem.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rl_commodity_black.setVisibility(View.GONE);
                }
            });
            tv_paypwdsystem_pop_payprice.setText("¥" + cardPayPrice);
            rl_paypwdsystem_pop_select.setVisibility(View.VISIBLE);
            rl_paypwdsystem_pop_pwd.setVisibility(View.GONE);
            if (pwdType == 0) {//密码支付
                btn_paypwdsystem_pop.setText("密码支付");
                if (isPwd) {
                    tv_paypwdsystem_pop_pwdtype.setVisibility(View.VISIBLE);
                    tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);
                    btn_paypwdsystem_pop.setText("密码支付");
                    tv_paypwdsystem_pop_pwdtype.setText("指纹支付");
                    rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                    rl_paypwdsystem_pop_pwd.setVisibility(View.VISIBLE);
                } else {
                    tv_paypwdsystem_pop_pwdtype.setVisibility(View.GONE);
                }
            } else if (pwdType == 1) {//指纹支付
                btn_paypwdsystem_pop.setText("指纹支付");
                tv_paypwdsystem_pop_pwdtype.setVisibility(View.VISIBLE);
                tv_paypwdsystem_pop_pwdtype.setText("密码支付");
            } else if (pwdType == 2) {//支付结果
                tv_paypwdsystem_pop_pwdtype.setVisibility(View.GONE);
                tv_paypwdsystem_pop_payresult.setText("正在确认");
                ll_paypwdsystem_pop_payresult.setVisibility(View.VISIBLE);
                rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                rl_paypwdsystem_pop_pwd.setVisibility(View.GONE);
                tv_paypwdsystem_pop_title.setVisibility(View.GONE);
                csv_paypwdsystem_pop.loadLoading();
            }
            cv_paypwdsystem_pop_pwd.setShowType(CodeView.SHOW_TYPE_PASSWORD);
            cv_paypwdsystem_pop_pwd.setLength(6);
            kbv_paypwdsystem_pop.setCodeView(cv_paypwdsystem_pop_pwd);
            iv_paypwdsystem_pop_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinPayPwdSystem.dismiss();
                }
            });
            btn_paypwdsystem_pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pwdType == 0) {//密码支付
                        tv_paypwdsystem_pop_title.setText("请输入密码支付");
                        rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                        rl_paypwdsystem_pop_pwd.setVisibility(View.VISIBLE);
                        tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);
                    } else if (pwdType == 1) {//指纹支付
                        pWinPayPwdSystem.dismiss();
                        if (!mFingerprintCore.isAuthenticating()) {
                            mFingerprintCore.startAuthenticate();
                        }
                        showPopZhiWen();
                    }
                }
            });
            tv_paypwdsystem_pop_pwdtype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pwdType == 0) {//密码支付
                        pwdType = 1;
                        cv_paypwdsystem_pop_pwd.clear();
                        btn_paypwdsystem_pop.setText("指纹支付");
                        tv_paypwdsystem_pop_pwdtype.setText("密码支付");
                        rl_paypwdsystem_pop_select.setVisibility(View.VISIBLE);
                        rl_paypwdsystem_pop_pwd.setVisibility(View.GONE);
                    } else if (pwdType == 1) {//指纹支付
                        pwdType = 0;
                        tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);
                        btn_paypwdsystem_pop.setText("密码支付");
                        tv_paypwdsystem_pop_pwdtype.setText("指纹支付");
                        rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                        rl_paypwdsystem_pop_pwd.setVisibility(View.VISIBLE);
                    }
                }
            });
            cv_paypwdsystem_pop_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kbv_paypwdsystem_pop.show();
                }
            });
            tv_paypwdsystem_pop_wjmm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.toForgetPassword(mContext);
                }
            });
            cv_paypwdsystem_pop_pwd.setListener(new CodeView.Listener() {
                @Override
                public void onValueChanged(String value) {
                    tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);
                }

                @Override
                public void onComplete(String value) {
                    mPDialog.showDialog();
                    CommUtil.disposePayPwd(mContext,
                            value, "", 1, checkPayPwdHandler);
                }
            });
        }
    }

    private void showPopZhiWen() {
        popWinZhiWen = null;
        if (popWinZhiWen == null) {
            final View view = View
                    .inflate(this, R.layout.fingerprintdialog, null);
            popWinZhiWen = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            popWinZhiWen.setFocusable(true);
            popWinZhiWen.setOutsideTouchable(false);
            popWinZhiWen.setWidth(Utils.getDisplayMetrics(this)[0]);
            popWinZhiWen.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }

    private void showPopZhiWen1() {
        popWinZhiWen1 = null;
        if (popWinZhiWen1 == null) {
            final View view = View
                    .inflate(this, R.layout.fingerprintdialog1, null);
            TextView tv_fingerdialog1_again = (TextView) view.findViewById(R.id.tv_fingerdialog1_again);
            TextView tv_fingerdialog1_qx = (TextView) view.findViewById(R.id.tv_fingerdialog1_qx);
            popWinZhiWen1 = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            popWinZhiWen1.setFocusable(true);
            popWinZhiWen1.setOutsideTouchable(false);
            popWinZhiWen1.setWidth(Utils.getDisplayMetrics(this)[0]);
            popWinZhiWen1.showAtLocation(view, Gravity.CENTER, 0, 0);
            tv_fingerdialog1_qx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (popWinZhiWen1 != null && popWinZhiWen1.isShowing()) {
                        popWinZhiWen1.dismiss();
                    }
                    popWinZhiWen1 = null;
                    finish();
                }
            });
            tv_fingerdialog1_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (popWinZhiWen1 != null && popWinZhiWen1.isShowing()) {
                        popWinZhiWen1.dismiss();
                    }
                    popWinZhiWen1 = null;
                    if (!mFingerprintCore.isAuthenticating()) {
                        mFingerprintCore.startAuthenticate();
                    }
                    showPopZhiWen();
                }
            });
        }
    }

    private void showPopZhiWen2() {
        popWinZhiWen2 = null;
        if (popWinZhiWen2 == null) {
            final View view = View
                    .inflate(this, R.layout.fingerprintdialog2, null);
            TextView tv_fingerdialog2_mm = (TextView) view.findViewById(R.id.tv_fingerdialog2_mm);
            TextView tv_fingerdialog2_qx = (TextView) view.findViewById(R.id.tv_fingerdialog2_qx);
            popWinZhiWen2 = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            popWinZhiWen2.setFocusable(true);
            popWinZhiWen2.setOutsideTouchable(false);
            popWinZhiWen2.setWidth(Utils.getDisplayMetrics(this)[0]);
            popWinZhiWen2.showAtLocation(view, Gravity.CENTER, 0, 0);
            tv_fingerdialog2_qx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (popWinZhiWen2 != null && popWinZhiWen2.isShowing()) {
                        popWinZhiWen2.dismiss();
                    }
                    popWinZhiWen2 = null;
                    finish();
                }
            });
            tv_fingerdialog2_mm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (popWinZhiWen2 != null && popWinZhiWen2.isShowing()) {
                        popWinZhiWen2.dismiss();
                    }
                    popWinZhiWen2 = null;
                    showPayPwdSystem(0, true);
                }
            });
        }
    }

    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
            mFingerprintCore.cancelAuthenticate();
            if (popWinZhiWen != null && popWinZhiWen.isShowing()) {
                popWinZhiWen.dismiss();
            }
            popWinZhiWen = null;
            if (popWinZhiWen1 != null && popWinZhiWen1.isShowing()) {
                popWinZhiWen1.dismiss();
            }
            popWinZhiWen1 = null;
            if (popWinZhiWen2 != null && popWinZhiWen2.isShowing()) {
                popWinZhiWen2.dismiss();
            }
            popWinZhiWen2 = null;
            ToastUtil.showSuccessToast(mContext, "验证成功");
            spUtil.saveLong("check_pwd_code_time", System.currentTimeMillis());
        }

        @Override
        public void onAuthenticateFailed(int helpId) {
            mFingerprintCore.cancelAuthenticate();
            Log.e("TAG", "onAuthenticateFailed");
            fingerNum++;
            if (popWinZhiWen != null && popWinZhiWen.isShowing()) {
                popWinZhiWen.dismiss();
            }
            popWinZhiWen = null;
            if (popWinZhiWen1 != null && popWinZhiWen1.isShowing()) {
                popWinZhiWen1.dismiss();
            }
            popWinZhiWen1 = null;
            if (popWinZhiWen2 != null && popWinZhiWen2.isShowing()) {
                popWinZhiWen2.dismiss();
            }
            popWinZhiWen2 = null;
            if (fingerNum == 1) {//第一次验证失败
                showPopZhiWen1();
            } else if (fingerNum == 2) {//第二次验证失败
                showPopZhiWen2();
            }
        }

        @Override
        public void onAuthenticateError(int errMsgId) {
            Log.e("TAG", "onAuthenticateError");
            mFingerprintCore.cancelAuthenticate();
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {

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
            goPayResult();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFingerprintCore.onDestroy();
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
}
