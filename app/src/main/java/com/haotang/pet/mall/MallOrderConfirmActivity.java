package com.haotang.pet.mall;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.os.CancellationSignal;
import androidx.recyclerview.widget.RecyclerView;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.AvailableCouponActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.PaySuccessNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.SelectMyCardActivity;
import com.haotang.pet.SetFingerprintActivity;
import com.haotang.pet.ShopMallOrderDetailActivity;
import com.haotang.pet.adapter.MallOrderConfirmAdapter;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.mallEntity.MallGoods;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CustomStatusView;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.haotang.pet.view.SoftKeyBoardListener;
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
 * 商品订单确认页
 */
public class MallOrderConfirmActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_mallorderconfirm_price)
    TextView tvMallorderconfirmPrice;
    @BindView(R.id.tv_mallorderconfirm_phone)
    TextView tvMallorderconfirmPhone;
    @BindView(R.id.tv_mallorderconfirm_shr)
    TextView tvMallorderconfirmShr;
    @BindView(R.id.tv_mallorderconfirm_address)
    TextView tvMallorderconfirmAddress;
    @BindView(R.id.rl_mallorderconfirm_address)
    RelativeLayout rlMallorderconfirmAddress;
    @BindView(R.id.rv_mallorderconfirm_goods)
    RecyclerView rvMallorderconfirmGoods;
    @BindView(R.id.et_mallorderconfirm_yqm)
    EditText etMallorderconfirmYqm;
    @BindView(R.id.tv_mallorderconfirm_lpk)
    TextView tvMallorderconfirmLpk;
    @BindView(R.id.ll_mallorderconfirm_lpk)
    LinearLayout llMallorderconfirmLpk;
    @BindView(R.id.tv_mallorderconfirm_yhq)
    TextView tvMallorderconfirmYhq;
    @BindView(R.id.ll_mallorderconfirm_yhq)
    LinearLayout llMallorderconfirmYhq;
    @BindView(R.id.tv_mallorderconfirm_totalprice)
    TextView tvMallorderconfirmTotalprice;
    @BindView(R.id.tv_mallorderconfirm_yf)
    TextView tvMallorderconfirmYf;
    @BindView(R.id.tv_mallorderconfirm_couponprice)
    TextView tvMallorderconfirmCouponprice;
    @BindView(R.id.rl_mallorderconfirm_couponprice)
    RelativeLayout rlMallorderconfirmCouponprice;
    @BindView(R.id.tv_mallorderconfirm_cardprice)
    TextView tvMallorderconfirmCardprice;
    @BindView(R.id.tv_mallorderconfirm_cardname_pay)
    TextView tvMallorderconfirmCardnamePay;
    @BindView(R.id.rl_mallorderconfirm_card)
    RelativeLayout rlMallorderconfirmCard;
    @BindView(R.id.rl_mallorderconfirm_yf)
    RelativeLayout rlMallorderconfirmYf;
    @BindView(R.id.tv_mallorderconfirm_yf_tag)
    TextView tvMallorderconfirmYfTag;
    @BindView(R.id.ll_mallorderconfirm_fx)
    LinearLayout ll_mallorderconfirm_fx;
    @BindView(R.id.iv_mallorderconfirm_fx)
    ImageView iv_mallorderconfirm_fx;
    @BindView(R.id.tv_mallorderconfirm_fxdk)
    TextView tv_mallorderconfirm_fxdk;
    @BindView(R.id.rl_mallorderconfirm_fxprice)
    RelativeLayout rl_mallorderconfirm_fxprice;
    @BindView(R.id.tv_mallorderconfirm_fxprice)
    TextView tv_mallorderconfirm_fxprice;
    @BindView(R.id.textview_insideCopy)
    TextView textview_insideCopy;
    private FingerprintCore mFingerprintCore;
    private String strp;
    private StringBuilder listpayWays = new StringBuilder();
    private int oldpayway;
    private int cardId = -1;
    private List<MyCard> cardList = new ArrayList<MyCard>();
    private String cardName;
    private int paytype;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private CancellationSignal mCancellationSignal = new CancellationSignal();
    private int couponId;
    private int canUseServiceCard;
    private int orderNo;
    private int payPwd;
    private PopupWindow pWinPayPwdSystem;
    private PopupWindow pSetPwdDialog;
    private ImageView iv_paypwdsystem_pop_close;
    private TextView tv_paypwdsystem_pop_pwdtype;
    private TextView tv_paypwdsystem_pop_title;
    private RelativeLayout rl_paypwdsystem_pop_select;
    private Button btn_paypwdsystem_pop;
    private TextView tv_paypwdsystem_pop_payprice;
    private RelativeLayout rl_paypwdsystem_pop_pwd;
    private RelativeLayout rl_paypwdsystem_pwd;
    private CodeView cv_paypwdsystem_pop_pwd;
    private TextView tv_paypwdsystem_pop_wjmm;
    private TextView tv_paypwdsystem_pop_pwderror;
    private KeyboardView kbv_paypwdsystem_pop;
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
    private AlertDialogNavAndPost builder;
    private Timer timer;
    private TimerTask task;
    private String minute = "";
    private String second = "";
    private TextView tv_pay_bottomdia_time_second;
    private TextView tv_pay_bottomdia_time_minute;
    private boolean isPaySuccess;
    private String couponName;
    private boolean isTime;
    private ArrayList<String> discountList = new ArrayList<String>();
    public double discount = 1;
    private int couponEnable;
    private ArrayList<MallGoods> goodsList = new ArrayList<>();
    private int addressId;
    private String areaName;
    private String address;
    private String consigner;
    private String mobile;
    private String freightTag;
    private String freightNoTag;
    private double freightPrice;
    private MallOrderConfirmAdapter mallOrderConfirmAdapter;
    private int myselfDummpNum = -1;
    private int reserveEnable;
    private double cashBackBalance;
    private double cashBackAvailable;
    private boolean fx_isOpen = true;
    private double cardPayPrice;
    private double thirdPrice;
    private double normalCouponDiscountPrice;
    private double reserveDiscountPrice;
    private double payPrice;
    private String insideCopy;
    private double cashBackLimit;
    private String reserveDiscountTip;
    private double goodsPrice;
    private int couponSize;
    private int cardSize;
    private int source;
    private List<Coupon> couponSizeList = new ArrayList<Coupon>();

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
                            ToastUtil.showToastShortBottom(mContext,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(mContext,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(mContext, orderStr,
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
                        ToastUtil.showToastShortBottom(mContext, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "支付失败");
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
        autoLogin();
    }

    private void initData() {
        oldpayway = spUtil.getInt("payway", 0);
        strp = getIntent().getStringExtra("strp");
        source = getIntent().getIntExtra("source",-1);
        Global.WXPAYCODE = -1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
    }

    private void findView() {
        setContentView(R.layout.activity_mall_order_confirm);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("订单确认");
        mPDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        etMallorderconfirmYqm.setImeOptions(EditorInfo.IME_ACTION_DONE);

        rvMallorderconfirmGoods.setHasFixedSize(true);
        rvMallorderconfirmGoods.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvMallorderconfirmGoods.setLayoutManager(noScollFullLinearLayoutManager);
        mallOrderConfirmAdapter = new MallOrderConfirmAdapter(R.layout.item_mallorderconfirm, goodsList);
        rvMallorderconfirmGoods.setAdapter(mallOrderConfirmAdapter);
    }

    private void setLinster() {
        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                etMallorderconfirmYqm.setGravity(Gravity.CENTER);
                etMallorderconfirmYqm.setBackgroundResource(R.drawable.bg_order_pay_et1);
            }

            @Override
            public void keyBoardHide(int height) {
                if (Utils.isStrNull(Utils.checkEditText(etMallorderconfirmYqm))) {
                    checkRedeemCode();
                }
            }
        });
    }

    private void autoLogin() {
        CommUtil.loginAuto(this, spUtil.getString("cellphone", ""),
                spUtil.getString("IMEI", ""), Global.getCurrentVersion(this),
                spUtil.getInt("userid", 0), 0, 0, autoLoginHandler);
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("user") && !jData.isNull("user")) {
                            JSONObject jUser = jData.getJSONObject("user");
                            if (jUser.has("cityId") && !jUser.isNull("cityId")) {
                                spUtil.saveInt("cityId",
                                        jUser.getInt("cityId"));
                            } else {
                                spUtil.removeData("cityId");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

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
                    ToastUtil.showToastShortBottom(mContext, msg);
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

    private void getData() {
        mPDialog.showDialog();
        goodsList.clear();
        payPrice = 0;
        CommUtil.mallConfirmOrder(mContext, strp, cardId, couponId, false, mallConfirmOrderHandler);
    }

    private AsyncHttpResponseHandler mallConfirmOrderHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("payPrice") && !objectData.isNull("payPrice")) {
                            payPrice = objectData.getDouble("payPrice");
                        }
                        //是否可用优惠券
                        if (objectData.has("couponEnable") && !objectData.isNull("couponEnable")) {
                            couponEnable = objectData.getInt("couponEnable");
                        }
                        //地址信息
                        if (objectData.has("address") && !objectData.isNull("address")) {
                            JSONObject objectAddress = objectData.getJSONObject("address");
                            if (objectAddress.has("id") && !objectAddress.isNull("id")) {
                                addressId = objectAddress.getInt("id");
                            }
                            if (objectAddress.has("areaName") && !objectAddress.isNull("areaName")) {
                                areaName = objectAddress.getString("areaName");
                            }
                            if (objectAddress.has("address") && !objectAddress.isNull("address")) {
                                address = objectAddress.getString("address");
                            }
                            if (objectAddress.has("consigner") && !objectAddress.isNull("consigner")) {
                                consigner = objectAddress.getString("consigner");
                            }
                            if (objectAddress.has("mobile") && !objectAddress.isNull("mobile")) {
                                mobile = objectAddress.getString("mobile");
                            }
                        }
                        //商品总金额
                        if (objectData.has("goodsPrice") && !objectData.isNull("goodsPrice")) {
                            goodsPrice = objectData.getDouble("goodsPrice");
                        }
                        if (objectData.has("insideCopy") && !objectData.isNull("insideCopy")) {
                            insideCopy = objectData.getString("insideCopy");
                        }
                        //商品信息
                        if (objectData.has("goods") && !objectData.isNull("goods")) {
                            JSONArray array = objectData.getJSONArray("goods");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    goodsList.add(MallGoods.json2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                    }
                }
                Utils.setText(textview_insideCopy, insideCopy, "", View.VISIBLE, View.GONE);
                tvMallorderconfirmTotalprice.setText("¥" + goodsPrice);
                if (addressId > 0) {
                    rlMallorderconfirmAddress.setVisibility(View.VISIBLE);
                    tvMallorderconfirmShr.setText("收货人：" + consigner);
                    tvMallorderconfirmPhone.setText("" + mobile);
                    tvMallorderconfirmAddress.setText("收货地址：" + areaName + " " + address);
                } else {
                    rlMallorderconfirmAddress.setVisibility(View.GONE);
                    tvMallorderconfirmAddress.setText("收货地址：请添加收货地址");
                }
                mallOrderConfirmAdapter.notifyDataSetChanged();
                if (couponEnable == 1) {
                    llMallorderconfirmYhq.setVisibility(View.VISIBLE);
                    getCoupon();
                } else if (couponEnable == 0) {
                    llMallorderconfirmYhq.setVisibility(View.GONE);
                    getPayWay();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void getCoupon() {
        mPDialog.showDialog();
        couponList.clear();
        couponId = 0;
        canUseServiceCard = 0;
        couponName = "";
        tvMallorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
        tvMallorderconfirmYhq.setText("无可用");
        CommUtil.myMallCoupons(mContext, strp, cardId, payPrice, couponHandler);
    }

    private AsyncHttpResponseHandler couponHandler = new AsyncHttpResponseHandler() {

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
                        JSONObject objectData = jobj.getJSONObject("data");
                        if (objectData.has("coupons") && !objectData.isNull("coupons")) {
                            JSONArray array = objectData.getJSONArray("coupons");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jcoupon = array.getJSONObject(i);
                                    couponList.add(Coupon.jsonToEntity(jcoupon));
                                }
                            }
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
                        couponId = couponList.get(i).id;
                        canUseServiceCard = couponList.get(i).canUseServiceCard;
                        couponName = couponList.get(i).name;
                        break;
                    }
                }
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
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (listpayWays != null && listpayWays.length() > 0) {
                if (listpayWays.toString().contains("11")) {// E卡
                    llMallorderconfirmLpk.setVisibility(View.VISIBLE);
                } else {
                    llMallorderconfirmLpk.setVisibility(View.GONE);
                }
            } else {
                llMallorderconfirmLpk.setVisibility(View.GONE);
            }
            confirmOrderPromptNew();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void confirmOrderPromptNew() {
        thirdPrice = 0;
        normalCouponDiscountPrice = 0;
        cardPayPrice = 0;
        reserveDiscountPrice = 0;
        freightPrice = 0;
        freightTag = "";
        freightNoTag = "";
        reserveDiscountPrice = 0;
        payPrice = 0;
        reserveEnable = 0;
        cashBackAvailable = 0;
        cashBackBalance = 0;
        cashBackLimit = 0;
        reserveDiscountTip = "";
        mPDialog.showDialog();
        CommUtil.mallConfirmOrder(mContext, strp, cardId, couponId, fx_isOpen, mallConfirmOrderNewHandler);
    }

    private AsyncHttpResponseHandler mallConfirmOrderNewHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        //运费标签
                        if (objectData.has("freightTag") && !objectData.isNull("freightTag")) {
                            freightTag = objectData.getString("freightTag");
                        }
                        //运费标签
                        if (objectData.has("freightNoTag") && !objectData.isNull("freightNoTag")) {
                            freightNoTag = objectData.getString("freightNoTag");
                        }
                        //运费
                        if (objectData.has("freight") && !objectData.isNull("freight")) {
                            freightPrice = objectData.getDouble("freight");
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
                        if (objectData.has("reserveDiscount") && !objectData.isNull("reserveDiscount")) {
                            reserveDiscountPrice = objectData.getDouble("reserveDiscount");
                        }
                        if (objectData.has("payPrice") && !objectData.isNull("payPrice")) {
                            payPrice = objectData.getDouble("payPrice");
                        }
                        //返现是否可用
                        if (objectData.has("reserveEnable") && !objectData.isNull("reserveEnable")) {
                            reserveEnable = objectData.getInt("reserveEnable");
                        }
                        //返现可用余额
                        if (objectData.has("cashBackAvailable") && !objectData.isNull("cashBackAvailable")) {
                            cashBackAvailable = objectData.getDouble("cashBackAvailable");
                        }
                        //满多少可用
                        if (objectData.has("cashBackLimit") && !objectData.isNull("cashBackLimit")) {
                            cashBackLimit = objectData.getDouble("cashBackLimit");
                        }
                        //文案
                        if (objectData.has("reserveDiscountTip") && !objectData.isNull("reserveDiscountTip")) {
                            reserveDiscountTip = objectData.getString("reserveDiscountTip");
                        }
                        //返现总余额
                        if (objectData.has("cashBackBalance") && !objectData.isNull("cashBackBalance")) {
                            cashBackBalance = objectData.getDouble("cashBackBalance");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (cashBackBalance > 0) {
                if (reserveEnable == 1) {//可用
                    tv_mallorderconfirm_fxdk.setTextColor(getResources().getColor(R.color.a666666));
                    tv_mallorderconfirm_fxdk.setText("共" + cashBackBalance + ",可用" + cashBackAvailable);
                } else if (reserveEnable == 0) {//不满足使用条件
                    fx_isOpen = false;
                    iv_mallorderconfirm_fx.setImageResource(R.drawable.icon_fx_unav);
                    tv_mallorderconfirm_fxdk.setTextColor(getResources().getColor(R.color.ab3b3b3));
                    tv_mallorderconfirm_fxdk.setText("共" + cashBackBalance + ",满" + cashBackLimit + "可用");
                } else if (reserveEnable == 2) {//无需使用
                    fx_isOpen = false;
                    iv_mallorderconfirm_fx.setImageResource(R.drawable.icon_fx_unav);
                    tv_mallorderconfirm_fxdk.setTextColor(getResources().getColor(R.color.ab3b3b3));
                    tv_mallorderconfirm_fxdk.setText("共" + cashBackBalance + ",可用" + cashBackAvailable);
                    if (cardId > 0) {
                        cardId = -1;
                        //再调一次本接口
                        confirmOrderPromptNew();
                    }
                }
            } else {
                fx_isOpen = false;
                iv_mallorderconfirm_fx.setImageResource(R.drawable.icon_fx_unav);
                tv_mallorderconfirm_fxdk.setTextColor(getResources().getColor(R.color.ab3b3b3));
                tv_mallorderconfirm_fxdk.setText("共0元");
            }
            if (freightPrice > 0) {
                tvMallorderconfirmYf.setText("¥" + freightPrice);
                tvMallorderconfirmYfTag.setText(freightNoTag);
            } else {
                tvMallorderconfirmYfTag.setText(freightTag);
                tvMallorderconfirmYf.setText("¥0");
            }
            if (reserveDiscountPrice > 0) {
                rl_mallorderconfirm_fxprice.setVisibility(View.VISIBLE);
                tv_mallorderconfirm_fxprice.setText("-¥" + reserveDiscountPrice);
            } else {
                rl_mallorderconfirm_fxprice.setVisibility(View.GONE);
            }
            if (couponId > 0) {
                tvMallorderconfirmYhq.setText("-" + normalCouponDiscountPrice + "(" + couponName + ")");
                tvMallorderconfirmYhq.setTextColor(getResources().getColor(R.color.aD0021B));
                if (normalCouponDiscountPrice > 0) {
                    rlMallorderconfirmCouponprice.setVisibility(View.VISIBLE);
                    Utils.setText(tvMallorderconfirmCouponprice, "-¥" + normalCouponDiscountPrice, "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlMallorderconfirmCouponprice.setVisibility(View.GONE);
                }
            } else {
                rlMallorderconfirmCouponprice.setVisibility(View.GONE);
                couponName = "";
                canUseServiceCard = 0;
            }
            if (cardId > 0) {
                tvMallorderconfirmLpk.setTextColor(getResources().getColor(R.color.aD0021B));
                Utils.setText(tvMallorderconfirmLpk, cardName, "", View.VISIBLE, View.VISIBLE);
                if (cardPayPrice > 0) {
                    rlMallorderconfirmCard.setVisibility(View.VISIBLE);
                    Utils.setText(tvMallorderconfirmCardprice, "¥" + cardPayPrice, "", View.VISIBLE, View.VISIBLE);
                    Utils.setText(tvMallorderconfirmCardnamePay, cardName + "支付", "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlMallorderconfirmCard.setVisibility(View.GONE);
                }
            } else {
                rlMallorderconfirmCard.setVisibility(View.GONE);
                cardName = "";
            }
            Utils.setText(tvMallorderconfirmPrice, "¥" + thirdPrice, "¥0", View.VISIBLE, View.VISIBLE);

            //计算可用礼品卡数量
            if (listpayWays != null && listpayWays.length() > 0 && listpayWays.toString().contains("11") && cardId <= 0) {
                mPDialog.showDialog();
                cardSize = 0;
                CommUtil.serviceCardList(mContext, 0, 5, payPrice, Global.ORDERKEY[12], "", cardSizeHandler);
            }
            //计算可用优惠券数量
            if (couponId <= 0) {
                mPDialog.showDialog();
                couponSize = 0;
                couponSizeList.clear();
                CommUtil.myMallCoupons(mContext, strp, cardId, payPrice, couponSizeHandler);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
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
                tvMallorderconfirmLpk.setText(cardSize + "张可用");
                tvMallorderconfirmLpk.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tvMallorderconfirmLpk.setTextColor(getResources().getColor(R.color.aBBBBBB));
                Utils.setText(tvMallorderconfirmLpk, "无可用", "", View.VISIBLE, View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private AsyncHttpResponseHandler couponSizeHandler = new AsyncHttpResponseHandler() {

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
                        JSONObject objectData = jobj.getJSONObject("data");
                        if (objectData.has("coupons") && !objectData.isNull("coupons")) {
                            JSONArray array = objectData.getJSONArray("coupons");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jcoupon = array.getJSONObject(i);
                                    couponSizeList.add(Coupon.jsonToEntity(jcoupon));
                                }
                            }
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
                tvMallorderconfirmYhq.setText(couponSize + "张可用");
                tvMallorderconfirmYhq.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tvMallorderconfirmYhq.setText("无可用");
                tvMallorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    /**
     * 商城下单二次
     *
     * @param balance
     */
    private void mallOrderPay(double balance) {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
        CommUtil.mallOrderPay(mContext, Utils.checkEditText(etMallorderconfirmYqm), cardId,
                spUtil.getInt("cityId", 0), fx_isOpen, reserveDiscountPrice, payPrice,
                Utils.formatDouble(balance, 2), strp, paytype, addressId, orderNo, couponId, changeOrderHanler);
    }

    private AsyncHttpResponseHandler changeOrderHanler = new AsyncHttpResponseHandler() {
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
                        if (jdata.has("orderId") && !jdata.isNull("orderId")) {
                            orderNo = Integer.parseInt(jdata.getString("orderId"));
                        }
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
                            if (orderNo <= 0) {
                                SecondTimeDown();
                            }
                            goPay();
                        }
                    }
                } else {
                    if (resultCode == Global.PAY_SUCCESS) {
                        goPayResult();
                    } else {
                        setPayLoadFail();
                    }
                    ToastUtil.showToastShort(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                setPayLoadFail();
            }
            if (pWinBottomDialog != null && pWinBottomDialog.isShowing()) {
                pWinBottomDialog.dismiss();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
            setPayLoadFail();
            if (pWinBottomDialog != null && pWinBottomDialog.isShowing()) {
                pWinBottomDialog.dismiss();
            }
        }
    };

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
//                startActivity(new Intent(this, ShopMallOrderActivity.class)); 不在打开这个界面
                Intent intent = new Intent(mContext, ShopMallOrderDetailActivity.class);
                intent.putExtra("orderId", orderNo);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.MALL_ORDER_ADDRESS) {
                consigner = data.getStringExtra("consigner");
                mobile = data.getStringExtra("mobile");
                areaName = data.getStringExtra("areaName");
                address = data.getStringExtra("address");
                addressId = data.getIntExtra("id", -1);
                rlMallorderconfirmAddress.setVisibility(View.VISIBLE);
                tvMallorderconfirmShr.setText("收货人：" + consigner);
                tvMallorderconfirmPhone.setText("" + mobile);
                tvMallorderconfirmAddress.setText("收货地址：" + areaName + " " + address);
            } else if (requestCode == Global.MALL_ORDER_CHANGE_ADDRESS) {
                consigner = data.getStringExtra("consigner");
                mobile = data.getStringExtra("mobile");
                areaName = data.getStringExtra("areaName");
                address = data.getStringExtra("address");
                addressId = data.getIntExtra("id", -1);
                rlMallorderconfirmAddress.setVisibility(View.VISIBLE);
                tvMallorderconfirmShr.setText("收货人：" + consigner);
                tvMallorderconfirmPhone.setText("" + mobile);
                tvMallorderconfirmAddress.setText("收货地址：" + areaName + " " + address);
            } else if (requestCode == Global.ORDERDETAILREQUESTCODE_COUPON) {//选择优惠券回来
                couponId = data.getIntExtra("couponid", 0);
                canUseServiceCard = data.getIntExtra("canUseServiceCard", 0);
                couponName = data.getStringExtra("name");
                confirmOrderPromptNew();
            } else if (requestCode == Global.MALLCONFIRM_TO_MYCARD) {//选择E卡回来
                cardId = data.getIntExtra("id", -1);
                cardName = data.getStringExtra("cardTypeName");
                int couponFlag = data.getIntExtra("couponFlag", 0);
                if (couponId > 0 && couponFlag == 1) {//清除选中的优惠券
                    couponId = 0;
                }
                confirmOrderPromptNew();
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
                    MApplication.listAppoint1.clear();
                    Intent intent = new Intent(mContext, ShopMallOrderDetailActivity.class);
                    intent.putExtra("orderId", orderNo);
                    startActivity(intent);
                    finish();
                }
            }).show();
        } else {
            finish();
        }
    }

    private void SecondTimeDown() {
        mPDialog.showDialog();
        CommUtil.getMallTimeDown(mContext, orderNo, handler);
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            Global.ServerEvent(mContext, Global.ServerEventID.typeid_149, Global.ServerEventID.click_orderpay_back);
            goBack();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.appoint_pay_bottom_dialog, null);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(mContext)[0]);
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
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
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

    private void checkRedeemCode() {
        mPDialog.showDialog();
        CommUtil.checkRedeemCodeMall(Utils.checkEditText(etMallorderconfirmYqm), this, checkRedeemCodeHanler);
    }

    private AsyncHttpResponseHandler checkRedeemCodeHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    etMallorderconfirmYqm.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    etMallorderconfirmYqm.setBackgroundColor(getResources().getColor(R.color.transparent));
                } else {
                    etMallorderconfirmYqm.setGravity(Gravity.CENTER);
                    etMallorderconfirmYqm.setBackgroundResource(R.drawable.bg_order_pay_et1);
                    etMallorderconfirmYqm.setText("");
                    ToastUtil.showToastShortCenter(mContext, msg);
                }
            } catch (JSONException e) {
                etMallorderconfirmYqm.setGravity(Gravity.CENTER);
                etMallorderconfirmYqm.setBackgroundResource(R.drawable.bg_order_pay_et1);
                etMallorderconfirmYqm.setText("");
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            etMallorderconfirmYqm.setGravity(Gravity.CENTER);
            etMallorderconfirmYqm.setBackgroundResource(R.drawable.bg_order_pay_et1);
            etMallorderconfirmYqm.setText("");
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void goPay() {
        isPaySuccess = false;
        if (paytype == 1) {// 微信支付
            spUtil.saveInt("payway", 1);
            mPDialog.showDialog();
            PayUtils.weChatPayment(mContext, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(mContext, aliHandler);
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
                                startActivityForResult(new Intent(mContext, SetFingerprintActivity.class).putExtra("flag",1), Global.ORDERPAY_TO_STARTFINGER);
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
        if (normalCouponDiscountPrice > 0) {
            discountList.add("优惠券优惠¥" + normalCouponDiscountPrice);
        }
        if (reserveDiscountPrice > 0) {
            discountList.add("返现抵扣优惠¥" + reserveDiscountPrice);
        }
        if (discountList != null && discountList.size() > 0) {
            discountList.add(0, "共计优惠¥" + ComputeUtil.add(normalCouponDiscountPrice, reserveDiscountPrice));
            intent.putStringArrayListExtra("discountList", discountList);
        }
        intent.putExtra("orderId", orderNo);
        intent.putExtra("payPrice", payPrice);
        intent.putExtra("type", 20);
        intent.putExtra("pageType", 3);
        intent.putExtra("myself", myselfDummpNum);
        switch (source){
            case Global.SOURCE_MALLSEARCH:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallSearch_PaySuccess);
                break;
            case Global.SOURCE_MALLCLASSONE:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassOne_PaySuccess);
                break;
            case Global.SOURCE_MALLCLASSTwo:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassTwo_PaySuccess);
                break;
            case Global.SOURCE_MALLCLASSThree:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassThree_PaySuccess);
                break;
            case Global.SOURCE_MALLCLASSIFY:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Classify_PaySuccess);
                break;
        }

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
                    mallOrderPay(-1);
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
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
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
            tv_paypwdsystem_pop_pwdtype.setVisibility(View.GONE);
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
            mCancellationSignal.cancel();
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
            mallOrderPay(-1);
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

    @OnClick({R.id.ib_titlebar_back, R.id.btn_mallorderconfirm_submit, R.id.ll_mallorderconfirm_address_root,
            R.id.ll_mallorderconfirm_lpk, R.id.ll_mallorderconfirm_yhq, R.id.iv_mallorderconfirm_fx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_mallorderconfirm_fx:
                if (cashBackBalance > 0) {
                    if (reserveEnable == 1) {
                        if (fx_isOpen) {
                            iv_mallorderconfirm_fx.setImageResource(R.drawable.icon_fx_close);
                        } else {
                            iv_mallorderconfirm_fx.setImageResource(R.drawable.icon_fx_open);
                        }
                        fx_isOpen = !fx_isOpen;
                        confirmOrderPromptNew();
                    } else if (reserveEnable == 0) {
                        new AlertDialogNavAndPost(mContext)
                                .builder()
                                .setTitle("")
                                .setMsg(reserveDiscountTip)
                                .setNegativeButtonVisible(View.GONE)
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                    } else if (reserveEnable == 2) {
                        new AlertDialogNavAndPost(mContext)
                                .builder()
                                .setTitle("")
                                .setMsg("当前优惠券已完全抵扣订单金额,不可使用返现")
                                .setNegativeButtonVisible(View.GONE)
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                    }
                } else {
                    new AlertDialogNavAndPost(mContext)
                            .builder()
                            .setTitle("")
                            .setMsg("送钱啦！快去完成洗澡、美容、特色服务，立返现金！")
                            .setNegativeButtonVisible(View.GONE)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_mallorderconfirm_submit:
                switch (source){
                    case Global.SOURCE_MALLSEARCH:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallSearch_ToPay);
                        break;
                    case Global.SOURCE_MALLCLASSONE:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassOne_ToPay);
                        break;
                    case Global.SOURCE_MALLCLASSTwo:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassTwo_ToPay);
                        break;
                    case Global.SOURCE_MALLCLASSThree:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassThree_ToPay);
                        break;
                    case Global.SOURCE_MALLCLASSIFY:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Classify_ToPay);
                        break;
                }
                if (addressId <= 0) {
                    ToastUtil.showToastShortCenter(mContext, "请添加地址信息");
                    return;
                }
                if (thirdPrice <= 0) {
                    if (cardId > 0) {
                        paytype = 11;
                    } else if (couponId > 0) {
                        paytype = 3;
                    }
                    if (cardId > 0) {
                        //判断是否设置支付密码
                        if (payPwd == 0) {//未设置过支付密码
                            showSetPwdPop();
                           /* boolean isNextSetPayPwd = spUtil.getBoolean("isNextSetPayPwd", false);
                            if (isNextSetPayPwd) {
                                mallOrderPay(-1);
                            } else {
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        spUtil.saveBoolean("isNextSetPayPwd", true);
                                        mallOrderPay(-1);
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
                        mallOrderPay(-1);
                    }
                } else {
                    if (listpayWays != null && listpayWays.length() > 0) {
                        if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                            showPayDialog();
                        }
                    }
                }
                break;
            case R.id.ll_mallorderconfirm_address_root:
                if (addressId > 0) {
                    Intent intent = new Intent(mContext, MallSelfAddressActivity.class);
                    intent.putExtra("previous", Global.MALL_ORDER_CHANGE_ADDRESS);
                    intent.putExtra("id", addressId);
                    startActivityForResult(intent, Global.MALL_ORDER_CHANGE_ADDRESS);
                } else {
                    Intent intent = new Intent(mContext, MallAddTackGoodsAddressActivity.class);
                    intent.putExtra("previous", Global.MALL_ORDER_ADDRESS);
                    startActivityForResult(intent, Global.MALL_ORDER_ADDRESS);
                }
                break;
            case R.id.ll_mallorderconfirm_lpk:
                if (reserveEnable == 2) {
                    new AlertDialogNavAndPost(mContext)
                            .builder()
                            .setTitle("")
                            .setMsg("当前优惠券已完全抵扣订单金额,不可使用E卡")
                            .setNegativeButtonVisible(View.GONE)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                } else {
                    Intent intent = new Intent(mContext, SelectMyCardActivity.class);
                    intent.putExtra("id", cardId);
                    intent.putExtra("type", 5);
                    intent.putExtra("orderKey", Global.ORDERKEY[12]);
                    intent.putExtra("payPrice", payPrice);
                    intent.putExtra("couponId", couponId);
                    intent.putExtra("flag", 4);
                    intent.putExtra("canUseServiceCard", canUseServiceCard);
                    startActivityForResult(intent, Global.MALLCONFIRM_TO_MYCARD);
                }
                break;
            case R.id.ll_mallorderconfirm_yhq:
                mPDialog.showDialog();
                couponList.clear();
                CommUtil.myMallCoupons(mContext, strp, cardId, payPrice, localCouponHandler);
                break;
        }
    }

    private void showSetPwdPop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        pSetPwdDialog = null;
        if (pSetPwdDialog==null){
            ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.pop_setpaypwd_layout, null);
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

    private AsyncHttpResponseHandler localCouponHandler = new AsyncHttpResponseHandler() {

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
                        JSONObject objectData = jobj.getJSONObject("data");
                        if (objectData.has("coupons") && !objectData.isNull("coupons")) {
                            JSONArray array = objectData.getJSONArray("coupons");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jcoupon = array.getJSONObject(i);
                                    couponList.add(Coupon.jsonToEntity(jcoupon));
                                }
                            }
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
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };
}
