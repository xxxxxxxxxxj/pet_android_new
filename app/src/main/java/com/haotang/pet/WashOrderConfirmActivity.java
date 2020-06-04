package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.haotang.pet.adapter.OrderMyPetAdapter;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.entity.NoteTag;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.entity.WXPayResultEvent;
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
import com.haotang.pet.view.MListview;
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
 * 洗美特订单确认页
 */
public class WashOrderConfirmActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_washorderconfirm_price)
    TextView tvWashorderconfirmPrice;
    @BindView(R.id.ll_washorderconfirm_pet_more)
    LinearLayout ll_washorderconfirm_pet_more;
    @BindView(R.id.mlv_washorderconfirm_pet)
    MListview mlv_washorderconfirm_pet;
    @BindView(R.id.tv_washorderconfirm_pet_more)
    TextView tvWashorderconfirmPetMore;
    @BindView(R.id.iv_washorderconfirm_pet_more)
    ImageView ivWashorderconfirmPetMore;
    @BindView(R.id.iv_washorderconfirm_mrsicon)
    ImageView ivWashorderconfirmMrsicon;
    @BindView(R.id.tv_washorderconfirm_mrsname)
    TextView tvWashorderconfirmMrsname;
    @BindView(R.id.tv_washorderconfirm_shop)
    TextView tvWashorderconfirmShop;
    @BindView(R.id.tv_washorderconfirm_fwfs)
    TextView tvWashorderconfirmFwfs;
    @BindView(R.id.tv_washorderconfirm_js)
    TextView tvWashorderconfirmJs;
    @BindView(R.id.tv_washorderconfirm_time)
    TextView tvWashorderconfirmTime;
    @BindView(R.id.tv_washorderconfirm_address)
    TextView tvWashorderconfirmAddress;
    @BindView(R.id.tv_washorderconfirm_bz)
    TextView tvWashorderconfirmBz;
    @BindView(R.id.tv_washorderconfirm_fwf)
    TextView tvWashorderconfirmFwf;
    @BindView(R.id.ll_washorderconfirm_fwf)
    LinearLayout llWashorderconfirmFwf;
    @BindView(R.id.tv_washorderconfirm_card)
    TextView tvWashorderconfirmCard;
    @BindView(R.id.ll_washorderconfirm_card)
    LinearLayout llWashorderconfirmCard;
    @BindView(R.id.tv_washorderconfirm_yhq)
    TextView tvWashorderconfirmYhq;
    @BindView(R.id.tv_washorderconfirm_smyhq)
    TextView tvWashorderconfirmSmyhq;
    @BindView(R.id.ll_washorderconfirm_smyhq)
    LinearLayout llWashorderconfirmSmyhq;
    @BindView(R.id.tv_washorderconfirm_notice)
    TextView tvWashorderconfirmNotice;
    @BindView(R.id.tv_washorderconfirm_fwf_title)
    TextView tv_washorderconfirm_fwf_title;
    @BindView(R.id.tv_washorderconfirm_totalprice)
    TextView tv_washorderconfirm_totalprice;
    @BindView(R.id.rl_washorderconfirm_couponprice)
    RelativeLayout rl_washorderconfirm_couponprice;
    @BindView(R.id.tv_washorderconfirm_couponprice)
    TextView tv_washorderconfirm_couponprice;
    @BindView(R.id.rl_washorderconfirm_smcouponprice)
    RelativeLayout rl_washorderconfirm_smcouponprice;
    @BindView(R.id.tv_washorderconfirm_smcouponprice)
    TextView tv_washorderconfirm_smcouponprice;
    @BindView(R.id.rl_washorderconfirm_carddiscountprice)
    RelativeLayout rl_washorderconfirm_carddiscountprice;
    @BindView(R.id.tv_washorderconfirm_carddiscountprice)
    TextView tv_washorderconfirm_carddiscountprice;
    @BindView(R.id.rl_washorderconfirm_card)
    RelativeLayout rl_washorderconfirm_card;
    @BindView(R.id.tv_washorderconfirm_cardprice)
    TextView tv_washorderconfirm_cardprice;
    @BindView(R.id.tv_washorderconfirm_cardname_pay)
    TextView tv_washorderconfirm_cardname_pay;
    @BindView(R.id.ll_washorderconfirm_smjs)
    LinearLayout ll_washorderconfirm_smjs;
    @BindView(R.id.tv_washorderconfirm_smjs)
    TextView tv_washorderconfirm_smjs;
    @BindView(R.id.tv_washorderconfirm_fxprice)
    TextView tv_washorderconfirm_fxprice;
    private StringBuilder listpayWays = new StringBuilder();
    private int oldpayway;
    private int cardId = -1;
    private List<MyCard> cardList = new ArrayList<MyCard>();
    private String cardName;
    private int paytype;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private List<Coupon> couponSizeList = new ArrayList<Coupon>();
    private int couponId;
    private int canUseServiceCard;
    private List<Coupon> homeCouponList = new ArrayList<Coupon>();
    private int homeCouponId;
    private String homeCouponName;
    private int orderNo;
    private String strp;
    private int payPwd;
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
    private long lastOverTime = 900000;
    private PopupWindow pWinBottomDialog;
    private PopupWindow pSetPwdDialog;
    private Timer timer;
    private TimerTask task;
    private String minute = "";
    private String second = "";
    private TextView tv_pay_bottomdia_time_second;
    private TextView tv_pay_bottomdia_time_minute;
    private int myselfDummpNum;
    private boolean isPaySuccess;
    private String cellphone;
    private String username;
    private double lat;
    private double lng;
    private int serviceLoc;
    private String appointment;
    private double totalPrice;
    private AppointWorker selectedWorker;
    private CommAddr commAddr;
    private ServiceShopAdd shopEntity;
    private List<ApointMentPet> petList;
    private int pickup;
    private int tid;
    private int shopId;
    private List<ApointMentPet> localPetList = new ArrayList<ApointMentPet>();
    private boolean isOpen;
    private OrderMyPetAdapter<ApointMentPet> orderMyPetAdapter;
    private String address;
    private String supplement;
    private String discountText;
    private double extraFee;
    private int addressId;
    private boolean isNeedCard;
    private String couponName = "";
    private double pickupPrice;
    private int serviceType;
    private long pageType;
    private boolean isTime;
    private ArrayList<String> discountList = new ArrayList<String>();
    private AlertDialogNavAndPost builder;
    private int flag;
    private double cardPayPrice;
    private double thirdPrice;
    private double normalCouponDiscountPrice;
    private double cardDiscountPrice;
    private double homeCouponDiscountPrice;
    private List<NoteTag> noteTagList;
    private StringBuilder sbNote = new StringBuilder();
    private String note;
    private double grainGoldPrice;
    private double payPrice;
    private String grainGoldText;
    private int couponSize;
    private int homeCouponSize;
    private int cardSize;

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    Log.e("TAG", "resultObj = " + resultObj.toString());
                    if (TextUtils.equals(resultStatus, "9000")) {
                        isPaySuccess = true;
                        goPayResult();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(WashOrderConfirmActivity.this, orderStr,
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
                    if (builder != null) {
                        builder.setMsg("您的订单在" + minute + "分" + second + "秒内未支付将被取消,请尽快完成支付");
                    }
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
                    isPaySuccess = true;
                    if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1")) {
                        Log.e("TAG", "OPPO哦");
                    } else {
                        goPayResult();
                    }
                } else {
                    if (Utils.isStrNull(resp.errStr)) {
                        ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "支付失败");
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
        oldpayway = spUtil.getInt("payway", 0);
        username = spUtil.getString("username", "");
        cellphone = spUtil.getString("cellphone", "");
        boolean isSelectPickup = getIntent().getBooleanExtra("isSelectPickup", false);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        appointment = getIntent().getStringExtra("appointment");
        strp = getIntent().getStringExtra("strp");
        selectedWorker = (AppointWorker) getIntent().getSerializableExtra("selectedWorker");
        commAddr = (CommAddr) getIntent().getSerializableExtra("commAddr");
        shopEntity = (ServiceShopAdd) getIntent().getSerializableExtra("shop");
        petList = (List<ApointMentPet>) getIntent().getSerializableExtra("myPets");
        serviceType = getIntent().getIntExtra("servicetype", 0);
        shopId = shopEntity.shopId;
        address = commAddr.address;
        addressId = commAddr.Customer_AddressId;
        supplement = commAddr.supplement;
        tid = selectedWorker.getTid();
        if (isSelectPickup) {
            pickup = 1;
        } else {
            pickup = 0;
        }
        MApplication.listAppoint.add(this);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        if (serviceType == 1 || serviceType == 2) {
            pageType = 1;
        } else if (serviceType == 3) {
            pageType = 2;
        }
    }

    private void findView() {
        setContentView(R.layout.activity_wash_order_confirm);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("订单确认");
        mPDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if (petList.size() > 2) {
            ll_washorderconfirm_pet_more.setVisibility(View.VISIBLE);
            isOpen = false;
            tvWashorderconfirmPetMore.setText("展开更多");
            ivWashorderconfirmPetMore.setImageResource(R.drawable.icon_appoint_pet_zk);
            localPetList.clear();
            localPetList.addAll(petList.subList(0, 2));
        } else {
            ll_washorderconfirm_pet_more.setVisibility(View.GONE);
            localPetList.clear();
            localPetList.addAll(petList);
        }
        orderMyPetAdapter = new OrderMyPetAdapter<ApointMentPet>(mContext, localPetList);
        mlv_washorderconfirm_pet.setAdapter(orderMyPetAdapter);
        GlideUtil.loadCircleImg(mContext, selectedWorker.getAvatar(), ivWashorderconfirmMrsicon, R.drawable.icon_default);
        Utils.setText(tvWashorderconfirmMrsname, selectedWorker.getRealName(), "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvWashorderconfirmShop, shopEntity.shopName, "", View.VISIBLE, View.VISIBLE);
        if (serviceLoc == 1) {//到店
            llWashorderconfirmFwf.setVisibility(View.GONE);
            llWashorderconfirmSmyhq.setVisibility(View.GONE);
            tvWashorderconfirmFwfs.setText("到店服务");
        } else if (serviceLoc == 2) {//上门
            tvWashorderconfirmFwfs.setText("上门服务");
        }
        Utils.setText(tvWashorderconfirmTime, appointment, "", View.VISIBLE, View.VISIBLE);
        tvWashorderconfirmAddress.setText(address + supplement);
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
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getData() {
        mPDialog.showDialog();
        normalCouponDiscountPrice = 0;
        cardDiscountPrice = 0;
        homeCouponDiscountPrice = 0;
        extraFee = 0;
        totalPrice = 0;
        payPrice = 0;
        thirdPrice = 0;
        cardPayPrice = 0;
        pickupPrice = 0;
        CommUtil.confirmOrderPromptNew(null, mContext, 1, serviceLoc, strp, selectedWorker.getWorkerId(), tid, appointment, null
                , null, pickup, shopId
                , cardId, couponId, homeCouponId, 0, confirmOrderPrompt);
    }

    private AsyncHttpResponseHandler confirmOrderPrompt = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                String msg = object.getString("msg");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("normalCouponDiscountPrice") && !objectData.isNull("normalCouponDiscountPrice")) {
                            normalCouponDiscountPrice = objectData.getDouble("normalCouponDiscountPrice");
                        }
                        if (objectData.has("cardDiscountPrice") && !objectData.isNull("cardDiscountPrice")) {
                            cardDiscountPrice = objectData.getDouble("cardDiscountPrice");
                        }
                        if (objectData.has("homeCouponDiscountPrice") && !objectData.isNull("homeCouponDiscountPrice")) {
                            homeCouponDiscountPrice = objectData.getDouble("homeCouponDiscountPrice");
                        }
                        if (objectData.has("cardPayPrice") && !objectData.isNull("cardPayPrice")) {
                            cardPayPrice = objectData.getDouble("cardPayPrice");
                        }
                        if (objectData.has("thirdPrice") && !objectData.isNull("thirdPrice")) {
                            thirdPrice = objectData.getDouble("thirdPrice");
                        }
                        if (objectData.has("prompt") && !objectData.isNull("prompt")) {
                            tvWashorderconfirmNotice.setVisibility(View.VISIBLE);
                            Utils.setText(tvWashorderconfirmNotice, objectData.getString("prompt"), "", View.VISIBLE, View.VISIBLE);
                        } else {
                            tvWashorderconfirmNotice.setVisibility(View.GONE);
                        }
                        if (objectData.has("extraFeeTag") && !objectData.isNull("extraFeeTag")) {
                            String extraFeeTag = objectData.getString("extraFeeTag");
                            tv_washorderconfirm_fwf_title.setText(extraFeeTag);
                        }
                        if (objectData.has("extraFee") && !objectData.isNull("extraFee")) {
                            extraFee = objectData.getDouble("extraFee");
                        }
                        if (objectData.has("totalPrice") && !objectData.isNull("totalPrice")) {
                            totalPrice = objectData.getDouble("totalPrice");
                        }
                        if (objectData.has("pickupPrice") && !objectData.isNull("pickupPrice")) {
                            pickupPrice = objectData.getDouble("pickupPrice");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
            payPrice = ComputeUtil.add(thirdPrice, cardPayPrice);
            if (extraFee > 0) {
                tvWashorderconfirmFwf.setText("¥" + extraFee);
                llWashorderconfirmFwf.setVisibility(View.VISIBLE);
                llWashorderconfirmSmyhq.setVisibility(View.VISIBLE);
            } else {
                llWashorderconfirmFwf.setVisibility(View.GONE);
                llWashorderconfirmSmyhq.setVisibility(View.GONE);
            }
            getCoupon();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getCoupon() {
        mPDialog.showDialog();
        couponList.clear();
        couponId = 0;
        canUseServiceCard = 0;
        couponName = "";
        tvWashorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
        tvWashorderconfirmYhq.setText("无可用");
        CommUtil.getAvailableCoupon(this, spUtil.getString("cellphone", ""), Global.getIMEI(this),
                Global.getCurrentVersion(this), appointment, 1, serviceLoc,
                selectedWorker.getWorkerId(), pickup, username, cellphone, addressId, address, lat, lng,
                strp, 0, ComputeUtil.sub(totalPrice, pickupPrice), shopId,
                null, 1, cardId, 0, 0, couponHanler);
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
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
            if (couponList.size() > 0) {
                for (int i = 0; i < couponList.size(); i++) {
                    if (couponList.get(i).isAvali == 1) {
                        couponId = couponList.get(i).id;
                        canUseServiceCard = couponList.get(i).canUseServiceCard;
                        couponName = couponList.get(i).name;
                        break;
                    }
                }
            }
            if (canUseServiceCard == 1) {
                isNeedCard = false;
            } else {
                isNeedCard = true;
            }
            if (serviceLoc == 2 && extraFee > 0) {
                getHomeCoupon();
            } else {
                getPayWay();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getHomeCoupon() {
        mPDialog.showDialog();
        homeCouponList.clear();
        homeCouponId = 0;
        homeCouponSize = 0;
        homeCouponName = "";
        tvWashorderconfirmSmyhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
        tvWashorderconfirmSmyhq.setText("无可用");
        CommUtil.getAvailableCoupon(this, spUtil.getString("cellphone", ""), Global.getIMEI(this),
                Global.getCurrentVersion(this), appointment, 1, serviceLoc,
                selectedWorker.getWorkerId(), pickup, username, cellphone, addressId, address, lat, lng,
                strp, 0, extraFee, shopId,
                null, 2, cardId, 0, 0, homeCouponHanler);
    }

    private AsyncHttpResponseHandler homeCouponHanler = new AsyncHttpResponseHandler() {

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
                            homeCouponList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
            if (homeCouponList.size() > 0) {
                for (int i = 0; i < homeCouponList.size(); i++) {
                    if (homeCouponList.get(i).isAvali == 1) {
                        homeCouponId = homeCouponList.get(i).id;
                        homeCouponName = homeCouponList.get(i).name;
                        break;
                    }
                }
                for (int i = 0; i < homeCouponList.size(); i++) {
                    if (homeCouponList.get(i).isAvali == 1) {
                        homeCouponSize++;
                    }
                }
            }
            getPayWay();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
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
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
            if (listpayWays != null && listpayWays.length() > 0) {
                if (listpayWays.toString().contains("11")) {// E卡
                    llWashorderconfirmCard.setVisibility(View.VISIBLE);
                    if (isNeedCard) {
                        getMyCard();
                    } else {
                        flag = 0;
                        confirmOrderPromptNew();
                    }
                } else {
                    llWashorderconfirmCard.setVisibility(View.GONE);
                    flag = 0;
                    confirmOrderPromptNew();
                }
            } else {
                llWashorderconfirmCard.setVisibility(View.GONE);
                flag = 0;
                confirmOrderPromptNew();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getMyCard() {
        cardId = -1;
        cardName = "";
        discountText = "";
        tvWashorderconfirmCard.setTextColor(getResources().getColor(R.color.aBBBBBB));
        Utils.setText(tvWashorderconfirmCard, "无可用", "", View.VISIBLE, View.VISIBLE);
        mPDialog.showDialog();
        cardList.clear();
        CommUtil.serviceCardList(this, shopId, 0, totalPrice, Global.ORDERKEY[8], appointment, cardListHandler);
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
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
                e.printStackTrace();
            }
            if (cardList != null && cardList.size() > 0) {
                cardId = cardList.get(0).getId();
                cardName = cardList.get(0).getCardTypeName();
                discountText = cardList.get(0).getDiscountText();
            }
            flag = 0;
            confirmOrderPromptNew();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    private void confirmOrderPromptNew() {
        thirdPrice = 0;
        normalCouponDiscountPrice = 0;
        cardPayPrice = 0;
        cardDiscountPrice = 0;
        homeCouponDiscountPrice = 0;
        grainGoldPrice = 0;
        grainGoldText = "";
        totalPrice = 0;
        pickupPrice = 0;
        payPrice = 0;
        mPDialog.showDialog();
        CommUtil.confirmOrderPromptNew(null, mContext, 1, serviceLoc, strp, selectedWorker.getWorkerId(), tid, appointment, null
                , null, pickup, shopId
                , cardId, couponId, homeCouponId, 0, confirmOrderPromptHandler);
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
                        if (objectData.has("totalPrice") && !objectData.isNull("totalPrice")) {
                            totalPrice = objectData.getDouble("totalPrice");
                        }
                        if (objectData.has("pickupPrice") && !objectData.isNull("pickupPrice")) {
                            pickupPrice = objectData.getDouble("pickupPrice");
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
                        if (objectData.has("homeCouponDiscountPrice") && !objectData.isNull("homeCouponDiscountPrice")) {
                            homeCouponDiscountPrice = objectData.getDouble("homeCouponDiscountPrice");
                        }
                        if (objectData.has("grainGoldPrice") && !objectData.isNull("grainGoldPrice")) {
                            grainGoldPrice = objectData.getDouble("grainGoldPrice");
                        }
                        if (objectData.has("grainGoldText") && !objectData.isNull("grainGoldText")) {
                            grainGoldText = objectData.getString("grainGoldText");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            payPrice = ComputeUtil.add(thirdPrice, cardPayPrice);
            if (pickup == 1) {
                ll_washorderconfirm_smjs.setVisibility(View.VISIBLE);
                tvWashorderconfirmJs.setVisibility(View.VISIBLE);
                tvWashorderconfirmJs.setText("需要接送");
                if (pickupPrice > 0) {
                    Utils.setText(tv_washorderconfirm_smjs, "¥" + pickupPrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    Utils.setText(tv_washorderconfirm_smjs, "免费", "", View.VISIBLE, View.VISIBLE);
                }
            } else {
                ll_washorderconfirm_smjs.setVisibility(View.GONE);
                tvWashorderconfirmJs.setVisibility(View.GONE);
            }
            Utils.setText(tv_washorderconfirm_fxprice, grainGoldText, "", View.VISIBLE, View.GONE);
            if (cardId > 0 && cardPayPrice <= 0 && (couponId > 0 || homeCouponId > 0)) {
                if (flag == 0) {//默认进来
                    cardId = -1;
                } else {
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("提示").setMsg("当前选择的优惠券已完全抵扣订单金额，已为您清除选中的E卡").isOneBtn(true).setPositiveButton("我知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                    if (flag == 1) {//选择优惠券回来
                        cardId = -1;
                    }
                }
                //再调一次本接口
                confirmOrderPromptNew();
            }
            if (couponId > 0) {
                tvWashorderconfirmYhq.setText("-" + normalCouponDiscountPrice + "(" + couponName + ")");
                tvWashorderconfirmYhq.setTextColor(getResources().getColor(R.color.aD0021B));
                if (normalCouponDiscountPrice > 0) {
                    rl_washorderconfirm_couponprice.setVisibility(View.VISIBLE);
                    Utils.setText(tv_washorderconfirm_couponprice, "-¥" + normalCouponDiscountPrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rl_washorderconfirm_couponprice.setVisibility(View.GONE);
                }
            } else {
                rl_washorderconfirm_couponprice.setVisibility(View.GONE);
                couponName = "";
                canUseServiceCard = 0;
            }
            if (cardId > 0) {
                tvWashorderconfirmCard.setTextColor(getResources().getColor(R.color.aD0021B));
                Utils.setText(tvWashorderconfirmCard, cardName + (Utils.isStrNull(discountText) ? "(" + discountText.replace("@@", "") + ")" : ""), "", View.VISIBLE, View.VISIBLE);
                if (cardDiscountPrice > 0) {
                    rl_washorderconfirm_carddiscountprice.setVisibility(View.VISIBLE);
                    Utils.setText(tv_washorderconfirm_carddiscountprice, "-¥" + cardDiscountPrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rl_washorderconfirm_carddiscountprice.setVisibility(View.GONE);
                }
                if (cardPayPrice > 0) {
                    rl_washorderconfirm_card.setVisibility(View.VISIBLE);
                    Utils.setText(tv_washorderconfirm_cardprice, "¥" + cardPayPrice, "", View.VISIBLE, View.VISIBLE);
                    Utils.setText(tv_washorderconfirm_cardname_pay, cardName + "支付" + (Utils.isStrNull(discountText) ? "(" + discountText.replace("@@", "") + ")" : ""), "", View.VISIBLE, View.VISIBLE);
                } else {
                    rl_washorderconfirm_card.setVisibility(View.GONE);
                }
            } else {
                rl_washorderconfirm_carddiscountprice.setVisibility(View.GONE);
                rl_washorderconfirm_card.setVisibility(View.GONE);
                cardName = "";
                discountText = "";
            }
            if (homeCouponId > 0) {
                tvWashorderconfirmSmyhq.setText("-" + homeCouponDiscountPrice + "(" + homeCouponName + ")");
                tvWashorderconfirmSmyhq.setTextColor(getResources().getColor(R.color.aD0021B));
                if (homeCouponDiscountPrice > 0) {
                    rl_washorderconfirm_smcouponprice.setVisibility(View.VISIBLE);
                    Utils.setText(tv_washorderconfirm_smcouponprice, "-¥" + homeCouponDiscountPrice, "¥0", View.VISIBLE, View.VISIBLE);
                } else {
                    rl_washorderconfirm_smcouponprice.setVisibility(View.GONE);
                }
            } else {
                rl_washorderconfirm_smcouponprice.setVisibility(View.GONE);
                homeCouponName = "";
                if (homeCouponSize > 0) {
                    tvWashorderconfirmSmyhq.setText(homeCouponSize + "张可用");
                    tvWashorderconfirmSmyhq.setTextColor(getResources().getColor(R.color.a666666));
                } else {
                    tvWashorderconfirmSmyhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
                    Utils.setText(tvWashorderconfirmSmyhq, "无可用", "", View.VISIBLE, View.VISIBLE);
                }
            }
            Utils.setText(tv_washorderconfirm_totalprice, "¥" + totalPrice, "¥0", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvWashorderconfirmPrice, "¥" + thirdPrice, "¥0", View.VISIBLE, View.VISIBLE);

            //计算可用礼品卡数量
            if (listpayWays != null && listpayWays.length() > 0 && listpayWays.toString().contains("11") && cardId <= 0) {
                mPDialog.showDialog();
                cardSize = 0;
                CommUtil.serviceCardList(mContext, shopId, 0, totalPrice, Global.ORDERKEY[8], appointment, cardSizeHandler);
            }
            //计算可用优惠券数量
            if (couponId <= 0) {
                mPDialog.showDialog();
                couponSize = 0;
                couponSizeList.clear();
                CommUtil.getAvailableCoupon(mContext, spUtil.getString("cellphone", ""), Global.getIMEI(mContext),
                        Global.getCurrentVersion(mContext), appointment, 1, serviceLoc,
                        selectedWorker.getWorkerId(), pickup, username, cellphone, addressId, address, lat, lng,
                        strp, 0, ComputeUtil.sub(totalPrice, pickupPrice), shopId,
                        null, 1, cardId, 0, 0, couponSizeHanler);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
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
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
                e.printStackTrace();
            }
            if (cardSize > 0) {
                tvWashorderconfirmCard.setText(cardSize + "张可用");
                tvWashorderconfirmCard.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tvWashorderconfirmCard.setTextColor(getResources().getColor(R.color.aBBBBBB));
                Utils.setText(tvWashorderconfirmCard, "无可用", "", View.VISIBLE, View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
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
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
            if (couponSizeList.size() > 0) {
                if (cardId > 0) {
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
                tvWashorderconfirmYhq.setText(couponSize + "张可用");
                tvWashorderconfirmYhq.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tvWashorderconfirmYhq.setText("无可用");
                tvWashorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.ORDERDETAILREQUESTCODE_COUPON) {//选择优惠券回来
                couponId = data.getIntExtra("couponid", 0);
                canUseServiceCard = data.getIntExtra("canUseServiceCard", 0);
                couponName = data.getStringExtra("name");
                flag = 1;
                confirmOrderPromptNew();
            } else if (requestCode == Global.ORDERDETAILREQUESTCODE_HOMECOUPON) {//选择上门优惠券回来
                homeCouponId = data.getIntExtra("couponid", 0);
                homeCouponName = data.getStringExtra("name");
                confirmOrderPromptNew();
            } else if (requestCode == Global.FOSORDERCONFIRM_TO_MYCARD) {//选择E卡回来
                cardId = data.getIntExtra("id", -1);
                cardName = data.getStringExtra("cardTypeName");
                discountText = data.getStringExtra("discountText");
                int couponFlag = data.getIntExtra("couponFlag", 0);
                if (couponId > 0 && couponFlag == 1) {//清除选中的优惠券
                    couponId = 0;
                }
                confirmOrderPromptNew();
            } else if (requestCode == Global.ORDERDETAILREQUESTCODE_NOTE) {//填写订单备注回来
                note = data.getStringExtra("note");
                sbNote.setLength(0);
                if (Utils.isStrNull(note)) {
                    sbNote.append(note + ",");
                }
                noteTagList = (List<NoteTag>) data.getSerializableExtra("list");
                if (noteTagList != null && noteTagList.size() > 0) {
                    for (int i = 0; i < noteTagList.size(); i++) {
                        if (noteTagList.get(i).isSelected()) {
                            sbNote.append(noteTagList.get(i).getTag() + ",");
                        }
                    }
                }
                String str = "";
                if (sbNote.toString().endsWith(",")) {
                    str = sbNote.toString().substring(0, sbNote.toString().length() - 1);
                } else {
                    str = sbNote.toString();
                }
                Utils.setText(tvWashorderconfirmBz, str, "", View.VISIBLE, View.VISIBLE);
            } else if (requestCode == Global.SERVICE_NEW_TO_CHANGE_LINKMAN) {
                ToastUtil.showToastShortAddIconCenter(mContext, "修改成功", R.drawable.toast_choose);
                CommAddr commAddrChange = (CommAddr) data.getSerializableExtra("commAddrChange");
                commAddr.linkman = commAddrChange.linkman;
                commAddr.telephone = commAddrChange.telephone;
                tvWashorderconfirmBz.setText(commAddr.address + "");
            } else if (requestCode == Global.ORDERPAY_TO_STARTFINGER) {
                goPaySuccess();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //支付密码设置结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetPayPwdSuccessEvent event) {
        if (event != null && event.isSuccess()) {
            getBalance();
            if (pSetPwdDialog!=null){
                pSetPwdDialog.dismiss();
            }
            if (pWinPayPwdSystem!=null){
                pWinPayPwdSystem.dismiss();
            }
            showPayPwdSystem(0, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Log.e("TAG", "android.os.Build.MODEL = " + android.os.Build.MODEL);
        Log.e("TAG", "android.os.Build.VERSION.RELEASE = " + android.os.Build.VERSION.RELEASE);
        Log.e("TAG", "Global.WXPAYCODE = " + Global.WXPAYCODE);
        if (Global.WXPAYCODE == 0) {
            if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1")) {
                Global.WXPAYCODE = -1;
                Log.e("支付成功", "onResume");
                goPayResult();
            }
        } else {
            if (orderNo > 0 && !isPaySuccess) {
                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                    MApplication.listAppoint.get(i).finish();
                }
                MApplication.listAppoint.clear();
                for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                    MApplication.listAppoint1.get(i).finish();
                }
                MApplication.listAppoint.clear();
                MApplication.listAppoint1.clear();
                Intent intent = new Intent(mContext, WashOrderDetailActivity.class);
                intent.putExtra("orderid", orderNo);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            Global.ServerEvent(WashOrderConfirmActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_orderpay_back);
            goBack();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void goBack() {
        if (orderNo > 0) {
            builder = new AlertDialogNavAndPost(this)
                    .builder();
            builder.setTitle("确定要离开收银台？")
                    .setMsg("您的订单在" + minute + "分" + second + "秒内未支付将被取消,请尽快完成支付")
                    .setPositiveButton("继续支付", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).setNegativeButton("确认离开", new View.OnClickListener() {
                @Override
                public void onClick(View v) {//去待付款详情
                    for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                        MApplication.listAppoint.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                        MApplication.listAppoint1.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    MApplication.listAppoint1.clear();
                    Intent intent = new Intent(mContext, WashOrderDetailActivity.class);
                    intent.putExtra("orderid", orderNo);
                    startActivity(intent);
                    finish();
                }
            }).show();
        } else {
            finish();
        }
    }

    @OnClick({R.id.ib_titlebar_back, R.id.ll_washorderconfirm_submit, R.id.ll_washorderconfirm_pet_more, R.id.iv_washorderconfirm_call, R.id.ll_washorderconfirm_address, R.id.ll_washorderconfirm_bz, R.id.ll_washorderconfirm_card, R.id.ll_washorderconfirm_yhq, R.id.ll_washorderconfirm_smyhq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                goBack();
                break;
            case R.id.ll_washorderconfirm_submit:
                if (thirdPrice <= 0) {
                    if (cardId > 0) {
                        paytype = 11;
                    } else if (couponId > 0) {
                        paytype = 3;
                    }
                    if (cardPayPrice > 0) {
                        //判断是否设置支付密码
                        if (payPwd == 0) {//未设置过支付密码
                            showSetPwdPop();
                            /*boolean isNextSetPayPwd = spUtil.getBoolean("isNextSetPayPwd", false);
                            if (isNextSetPayPwd) {
                                if (orderNo > 0) {
                                    washChangePayWay();
                                } else {
                                    washNewOrder();
                                }
                            } else {
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        spUtil.saveBoolean("isNextSetPayPwd", true);
                                        if (orderNo > 0) {
                                            washChangePayWay();
                                        } else {
                                            washNewOrder();
                                        }
                                    }
                                }).setPositiveButton("设置", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityUtils.toSetPassword(mContext);
                                    }
                                }).show();
                            }*/
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
                    } else {
                        if (orderNo > 0) {
                            washChangePayWay();
                        } else {
                            washNewOrder();
                        }
                    }
                } else {
                    if (listpayWays != null && listpayWays.length() > 0) {
                        if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                            showPayDialog();
                        }
                    }
                }
                break;
            case R.id.ll_washorderconfirm_pet_more:
                setPetListIsOpen();
                break;
            case R.id.iv_washorderconfirm_call:
                MDialog mDialog = new MDialog.Builder(mContext)
                        .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                        .setMessage("是否拨打电话?").setCancelStr("否")
                        .setOKStr("是")
                        .positiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.telePhoneBroadcast(mContext, shopEntity.shopPhone + "");
                            }
                        }).build();
                mDialog.show();
                break;
            case R.id.ll_washorderconfirm_address:
                Intent intent = new Intent(mContext, OrderDetailChangeLinkManActivity.class);
                intent.putExtra("commAddr", commAddr);
                startActivityForResult(intent, Global.SERVICE_NEW_TO_CHANGE_LINKMAN);
                break;
            case R.id.ll_washorderconfirm_bz:
                Intent intent1 = new Intent(mContext, NoteNewActivity.class);
                if (noteTagList == null) {
                    noteTagList = new ArrayList<NoteTag>();
                }
                if (noteTagList.size() <= 0) {
                    noteTagList.add(new NoteTag(1, "经常去宠物店", false));
                    noteTagList.add(new NoteTag(2, "有点小淘气", false));
                    noteTagList.add(new NoteTag(3, "毛毛有结", false));
                    noteTagList.add(new NoteTag(4, "最近肠胃不太好", false));
                }
                intent1.putExtra("remark", note);
                intent1.putExtra("list", (Serializable) noteTagList);
                startActivityForResult(intent1, Global.ORDERDETAILREQUESTCODE_NOTE);
                break;
            case R.id.ll_washorderconfirm_card:
                Intent intent2 = new Intent(mContext, SelectMyCardActivity.class);
                intent2.putExtra("id", cardId);
                intent2.putExtra("type", 0);
                intent2.putExtra("shopId", shopId);
                intent2.putExtra("orderKey", Global.ORDERKEY[8]);
                intent2.putExtra("payPrice", totalPrice);
                intent2.putExtra("flag", 1);
                intent2.putExtra("serviceLoc", serviceLoc);
                intent2.putExtra("strp", strp);
                intent2.putExtra("workerId", selectedWorker.getWorkerId());
                intent2.putExtra("tid", tid);
                intent2.putExtra("appointment", appointment);
                intent2.putExtra("pickup", pickup);
                intent2.putExtra("couponId", couponId);
                intent2.putExtra("homeCouponId", homeCouponId);
                intent2.putExtra("canUseServiceCard", canUseServiceCard);
                startActivityForResult(intent2, Global.FOSORDERCONFIRM_TO_MYCARD);
                break;
            case R.id.ll_washorderconfirm_yhq:
                mPDialog.showDialog();
                couponList.clear();
                CommUtil.getAvailableCoupon(this, spUtil.getString("cellphone", ""), Global.getIMEI(this),
                        Global.getCurrentVersion(this), appointment, 1, serviceLoc,
                        selectedWorker.getWorkerId(), pickup, username, cellphone, addressId, address, lat, lng,
                        strp, 0, ComputeUtil.sub(totalPrice, pickupPrice), shopId,
                        null, 1, cardId, 0, 0, localCouponHanler);
                break;
            case R.id.ll_washorderconfirm_smyhq:
                Intent intent4 = new Intent(this, AvailableCouponActivity.class);
                intent4.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                intent4.putExtra("couponId", homeCouponId);
                intent4.putExtra("couponList", (Serializable) homeCouponList);
                startActivityForResult(intent4, Global.ORDERDETAILREQUESTCODE_HOMECOUPON);
                break;
        }
    }

    private void showSetPwdPop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(WashOrderConfirmActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        pSetPwdDialog = null;
        if (pSetPwdDialog==null){
            ViewGroup customView = (ViewGroup) View.inflate(WashOrderConfirmActivity.this, R.layout.pop_setpaypwd_layout, null);
            Button btnSetPwd = customView.findViewById(R.id.btn_go_setpwd);
            TextView tvClose = customView.findViewById(R.id.tv_setpwd_close);
            pSetPwdDialog = new PopupWindow(customView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            pSetPwdDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
            pSetPwdDialog.setWidth(Utils.getDisplayMetrics(this)[0]);
            pSetPwdDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
            btnSetPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.toSetPassword(mContext);
                }
            });
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pSetPwdDialog.dismiss();
                }
            });
            pSetPwdDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rl_commodity_black.setVisibility(View.GONE);
                }
            });
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
                    ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
            Intent intent3 = new Intent(mContext, AvailableCouponActivity.class);
            intent3.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
            getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
            intent3.putExtra("couponId", couponId);
            intent3.putExtra("couponList", (Serializable) couponList);
            startActivityForResult(intent3, Global.ORDERDETAILREQUESTCODE_COUPON);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    private void washChangePayWay() {
        mPDialog.showDialog();
        CommUtil.changePayWayTwo("", "", "", this, orderNo, spUtil.getInt("userid", 0), username, cellphone,
                tvWashorderconfirmBz.getText().toString(), pickup, 0,
                -1, payPrice, paytype, couponId,
                0, "", false, "", appointment, strp, 0, homeCouponId, cardId, changeHanler);
    }

    private void washNewOrder() {
        mPDialog.showDialog();
        CommUtil.newOrderApi(this, spUtil.getString("cellphone", ""), Global.getIMEI(this),
                Global.getCurrentVersion(this),
                shopId, 0, couponId, selectedWorker.getWorkerId(), addressId, username,
                cellphone, address + (Utils.isStrNull(supplement) ? supplement : ""), lat, lng, appointment, totalPrice, payPrice,
                paytype, tvWashorderconfirmBz.getText().toString(),
                serviceLoc, pickup, -1,
                strp, -1,
                null, null, 0, homeCouponId, 0, 0, cardId, orderHanler);
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
                    ToastUtil.showToastShort(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler orderHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                Log.e("TAG", "生成新订单 = " + new String(responseBody));
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
                            SecondTimeDown();
                            goPay();
                        }
                    }
                } else {
                    if (resultCode == Global.PAY_SUCCESS) {
                        goPayResult();
                    } else {
                        setPayLoadFail();
                    }
                    ToastUtil.showToastShort(WashOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
                setPayLoadFail();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
            setPayLoadFail();
        }
    };

    private void setPetListIsOpen() {
        localPetList.clear();
        if (isOpen) {
            localPetList.addAll(petList.subList(0, 2));
            tvWashorderconfirmPetMore.setText("展开更多");
            ivWashorderconfirmPetMore.setImageResource(R.drawable.icon_appoint_pet_zk);
        } else {
            localPetList.addAll(petList);
            tvWashorderconfirmPetMore.setText("点击收起");
            ivWashorderconfirmPetMore.setImageResource(R.drawable.icon_appoint_pet_sp);
        }
        isOpen = !isOpen;
        orderMyPetAdapter.notifyDataSetChanged();
    }

    private void SecondTimeDown() {
        mPDialog.showDialog();
        CommUtil.CareTimes(mContext, orderNo, handler);
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
                    ToastUtil.showToastShortCenter(WashOrderConfirmActivity.this,
                            msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
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

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(WashOrderConfirmActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(WashOrderConfirmActivity.this, R.layout.appoint_pay_bottom_dialog, null);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(WashOrderConfirmActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        if (!isTime) {
            isTime = true;
            timerDown();
        }
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
                if (orderNo > 0) {
                    washChangePayWay();
                } else {
                    washNewOrder();
                }
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
        isPaySuccess = false;
        if (paytype == 1) {// 微信支付
            mPDialog.showDialog();
            spUtil.saveInt("payway", 1);
            PayUtils.weChatPayment(WashOrderConfirmActivity.this, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(WashOrderConfirmActivity.this, aliHandler);
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
                                startActivityForResult(new Intent(WashOrderConfirmActivity.this, SetFingerprintActivity.class).putExtra("flag",1), Global.ORDERPAY_TO_STARTFINGER);
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
        discountList.clear();
        if (homeCouponDiscountPrice > 0) {
            discountList.add("上门优惠券优惠¥" + homeCouponDiscountPrice);
        }
        if (normalCouponDiscountPrice > 0) {
            discountList.add("优惠券优惠¥" + normalCouponDiscountPrice);
        }
        if (cardDiscountPrice > 0) {
            discountList.add("E卡优惠¥" + cardDiscountPrice);
        }
        if (discountList != null && discountList.size() > 0) {
            discountList.add(0, "共计优惠¥" + ComputeUtil.add(homeCouponDiscountPrice, normalCouponDiscountPrice, cardDiscountPrice));
            intent.putStringArrayListExtra("discountList", discountList);
        }
        intent.putExtra("fx_price", grainGoldPrice);
        intent.putExtra("orderId", orderNo);
        intent.putExtra("payPrice", payPrice);
        intent.putExtra("type", 1);
        intent.putExtra("myself", myselfDummpNum);
        intent.putExtra("pageType", pageType);
        startActivity(intent);
        finish();
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
                    if (orderNo > 0) {
                        washChangePayWay();
                    } else {
                        washNewOrder();
                    }
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
                ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(WashOrderConfirmActivity.this, "请求失败");
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
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(WashOrderConfirmActivity.this, R.anim.commodity_detail_show));//开始动画
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
            if (pwdType == 0) {//密码验证
                tv_paypwdsystem_pop_title.setText("请输入密码验证");
                btn_paypwdsystem_pop.setText("密码验证");
                rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                rl_paypwdsystem_pop_pwd.setVisibility(View.VISIBLE);
                tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);

            } else if (pwdType == 1) {//指纹验证
                pWinPayPwdSystem.dismiss();
                if (!mFingerprintCore.isAuthenticating()) {
                    mFingerprintCore.startAuthenticate();
                }
                if (fingerNum == 2) {//第二次验证失败
                    showPopZhiWen2();
                }else if (fingerNum>2){
                    mFingerprintCore.cancelAuthenticate();
                    showPayPwdSystem(0, true);
                }else {
                    showPopZhiWen();
                }
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
                        fingerNum = 0;
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
//                    finish();
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
//                    finish();
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
            popWinZhiWen2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    fingerNum++;
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
            if (orderNo > 0) {
                washChangePayWay();
            } else {
                washNewOrder();
            }
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
