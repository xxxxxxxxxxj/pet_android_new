package com.haotang.pet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.OrderPayTcAdapter;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.PetCardInfoCodeBean;
import com.haotang.pet.entity.PetCardInfoCodeBean.CertiCouponsBean;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CustomStatusView;
import com.haotang.pet.view.MListview;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 犬证支付收银台
 */
public class OrderPayActivity extends SuperActivity implements OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private TextView tvPayFee;
    private RelativeLayout rlBalance;
    private TextView tvBalance;
    private TextView tvBalanceHint;
    private ImageView ivBalance;
    private ImageButton ibHybirdPay;
    private TextView tv_orderpay_hybirdpay_fee_weixin;
    private LinearLayout ll_orderpay_hybirdpay_fee_weixin;
    private TextView tv_orderpay_hybirdpay_fee_zhifubao;
    private LinearLayout ll_orderpay_hybirdpay_fee_zhifubao;
    private RelativeLayout rlWXPay;
    private ImageView ivWXPay;
    private RelativeLayout rlAliPay;
    private ImageView ivAliPay;
    private Button btPay;
    private SharedPreferenceUtil spUtil;
    private int paytype = 1;// 1是微信，2为支付宝,3为优惠券，4为余额支付，9罐头币支付
    private int oldpayway;
    private double payfee;
    private double balance;
    private double needpayfee;
    private boolean isHybirdPay = true;// 是否开启混合支付
    private boolean isRecharge;
    private boolean isRechargeReturn;// 是否是充值返回刷新余额
    private int previous = 0;
    private int previous_liucheng = 0;
    private Intent fIntent;
    private int orderNo;
    private String servicename = "宠物家";
    private int type;
    public static OrderPayActivity act;
    private long lastOverTime = 900000;
    private Timer timer;
    private TimerTask task;
    private TextView textView_order_thr;// 分钟
    private TextView textView_order_time_two;// 秒
    private TextView tv_orderpay_paytitle;
    private LinearLayout ll_orderpay_tc;
    private MListview lv_orderpay_tc;
    private TextView tv_orderpay_des;
    private TextView tv_orderpay_ts;
    private RelativeLayout rl_orderpay_dhm;
    private TextView tv_orderpay_dhm;
    private EditText et_orderpay_dhm;
    private ImageView iv_orderpay_dhm;
    private LinearLayout layout_payway;
    private View vw_line3;
    private int CertiOrderId;
    private int tcid;
    private int orderFee;
    protected boolean isCheckRedeem;
    private StringBuilder listpayWays = new StringBuilder();
    private boolean isWeiXin;
    private boolean isZhiFuBao;
    private boolean isYuE;
    protected boolean isCiKa;
    private boolean isHunHePayWays;
    private LinearLayout rlHybirdPay;
    private RelativeLayout rl_orderpay_cika;
    private ImageView iv_orderpay_cika;
    private TextView tv_orderpay_nosetpaypwddesc;
    private TextView tv_orderpay_cika;
    private boolean isManZu;
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private OrderPayTcAdapter orderPayTcAdapter;
    private List<CertiCouponsBean> certiCoupons;
    private int certiId;
    private int myselfDummpNum = -1;
    private String compare_desc;
    private String can_purpose_url;
    private int isVip = -1;
    private LinearLayout layout_recharge_detail;
    private LinearLayout ll_recharge_detail;
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
    private RelativeLayout rl_paypwdsystem_pwd;
    private TextView tv_paypwdsystem_pop_wjmm;
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
    private RelativeLayout rl_commodity_black;
    private boolean isArousePay;//是否唤起第三方支付
    private boolean isPay;//是否支付
    private AlertDialogNavAndPost builder;
    private String yqm = "";
    private boolean isclickhybirdpay;
    private String minute = "";
    private String second = "";

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        isPay = true;
                        // ToastUtil.showToastShortBottom(OrderDetailActivity.this,
                        // "支付成功!");
                        goPayResult();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”
                        // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShortBottom(OrderPayActivity.this,
                                    "支付结果确认中!");
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            // 从支付宝返回的状态
                        } else {
                            ToastUtil.showToastShortBottom(OrderPayActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                        // 支付宝支付
                        if (!mPDialog.isShowing()) {
                            mPDialog.showDialog();
                        }
                        isArousePay = true;
                        isPay = false;
                        PayUtils.payByAliPay(OrderPayActivity.this, orderStr,
                                aliHandler, mPDialog);
                    } else {
                        ToastUtil.showToastShortBottom(OrderPayActivity.this,
                                "支付参数错误");
                    }
                    break;
                case 0:
                    long lastTimerShowHMS = msg.arg1;
                    String time = Utils.formatTimeFM(lastTimerShowHMS);
                    minute = time.substring(0, 2);
                    second = time.substring(3, 5);
                    textView_order_thr.setText(minute);
                    textView_order_time_two.setText(second);
                    if (builder != null) {
                        builder.setMsg("您的订单在" + minute + "分" + second + "秒内未支付将被取消,请尽快完成支付");
                    }
                    break;
                case 1:
                    if (lastOverTime <= 0) {
                        ToastUtil.showToastShortBottom(mContext, "抱歉您的订单已超时");
                    }
                    finishWithAnimation();
                    break;
            }
        }
    };

    //微信支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayResultEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            BaseResp resp = event.getResp();
            if (resp != null) {
                if (resp.errCode == 0) {
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
        setContentView(R.layout.orderpay);
        Global.WXPAYCODE = -1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        act = this;
        findView();
        setView();
    }

    private void findView() {
        tv_orderpay_cika = (TextView) findViewById(R.id.tv_orderpay_cika);
        tv_orderpay_nosetpaypwddesc = (TextView) findViewById(R.id.tv_orderpay_nosetpaypwddesc);
        rl_orderpay_cika = (RelativeLayout) findViewById(R.id.rl_orderpay_cika);
        iv_orderpay_cika = (ImageView) findViewById(R.id.iv_orderpay_cika);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        tvPayFee = (TextView) findViewById(R.id.tv_orderpay_payfee);

        rlBalance = (RelativeLayout) findViewById(R.id.rl_orderpay_balance);
        tvBalance = (TextView) findViewById(R.id.tv_orderpay_balance);
        tvBalanceHint = (TextView) findViewById(R.id.tv_orderpay_balance_hint);
        ivBalance = (ImageView) findViewById(R.id.iv_orderpay_balance);
        rl_commodity_black = (RelativeLayout) findViewById(R.id.rl_commodity_black);
        ibHybirdPay = (ImageButton) findViewById(R.id.ib_orderpay_hybirdpay);
        tv_orderpay_hybirdpay_fee_weixin = (TextView) findViewById(R.id.tv_orderpay_hybirdpay_fee_weixin);
        ll_orderpay_hybirdpay_fee_weixin = (LinearLayout) findViewById(R.id.ll_orderpay_hybirdpay_fee_weixin);

        tv_orderpay_hybirdpay_fee_zhifubao = (TextView) findViewById(R.id.tv_orderpay_hybirdpay_fee_zhifubao);
        ll_orderpay_hybirdpay_fee_zhifubao = (LinearLayout) findViewById(R.id.ll_orderpay_hybirdpay_fee_zhifubao);

        rlWXPay = (RelativeLayout) findViewById(R.id.rl_orderpay_paywx);
        ivWXPay = (ImageView) findViewById(R.id.iv_orderpay_paywx);
        rlAliPay = (RelativeLayout) findViewById(R.id.rl_orderpay_payali);
        ivAliPay = (ImageView) findViewById(R.id.iv_orderpay_payali);
        rlHybirdPay = (LinearLayout) findViewById(R.id.ll_orderpay_hybirdpay);
        btPay = (Button) findViewById(R.id.bt_orderpay_pay);
        textView_order_thr = (TextView) findViewById(R.id.textView_order_thr);
        textView_order_time_two = (TextView) findViewById(R.id.textView_order_time_two);
        tv_orderpay_paytitle = (TextView) findViewById(R.id.tv_orderpay_paytitle);

        ll_orderpay_tc = (LinearLayout) findViewById(R.id.ll_orderpay_tc);
        lv_orderpay_tc = (MListview) findViewById(R.id.lv_orderpay_tc);
        tv_orderpay_des = (TextView) findViewById(R.id.tv_orderpay_des);
        tv_orderpay_ts = (TextView) findViewById(R.id.tv_orderpay_ts);

        rl_orderpay_dhm = (RelativeLayout) findViewById(R.id.rl_orderpay_dhm);
        tv_orderpay_dhm = (TextView) findViewById(R.id.tv_orderpay_dhm);
        et_orderpay_dhm = (EditText) findViewById(R.id.et_orderpay_dhm);
        iv_orderpay_dhm = (ImageView) findViewById(R.id.iv_orderpay_dhm);
        layout_payway = (LinearLayout) findViewById(R.id.layout_payway);
        vw_line3 = (View) findViewById(R.id.vw_line3);
        layout_recharge_detail = (LinearLayout) findViewById(R.id.layout_recharge_detail);
        ll_recharge_detail = (LinearLayout) findViewById(R.id.ll_recharge_detail);
        et_orderpay_dhm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                yqm = s.toString().trim();
            }
        });
        et_orderpay_dhm.setImeOptions(EditorInfo.IME_ACTION_DONE);
        lv_orderpay_tc.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (orderPayTcAdapter != null) {
                    orderPayTcAdapter.setOnItemClick(position);
                    CertiCouponsBean certiCouponsBean = certiCoupons
                            .get(position);
                    setTc(certiCouponsBean);
                    isHybird();
                    setNeedFee();
                }
            }
        });
    }

    private void setNeedFee() {
        if (isHybirdPay) {
            needpayfee = ComputeUtil.sub(payfee, balance);
            btPay.setText("¥" + Utils.formatDouble(needpayfee, 2) + " 确认支付");
        } else {
            if (paytype != 4) {
                needpayfee = payfee;
            } else {
                needpayfee = 0;
            }
        }
    }

    private void setTc(CertiCouponsBean certiCouponsBean) {
        if (certiCouponsBean != null) {
            tcid = certiCouponsBean.getId();
            String caption = certiCouponsBean.getCaption();
            Utils.setStringText(tv_orderpay_paytitle, caption);
        }
    }

    private void setDHM(boolean bool, int fee, double reduce) {
        if (bool) {
            iv_orderpay_dhm.setBackgroundResource(R.drawable.icon_petadd_select);
            tv_orderpay_dhm.setVisibility(View.VISIBLE);
            tv_orderpay_dhm.setText("立减" + fee + "元");
            tv_orderpay_ts.setVisibility(View.VISIBLE);
            tv_orderpay_ts.setText("为您节省" + reduce + "元");
        } else {
            iv_orderpay_dhm.setBackgroundResource(R.drawable.icon_petadd_unselect);
            tv_orderpay_dhm.setVisibility(View.GONE);
            tv_orderpay_ts.setVisibility(View.GONE);
        }
    }

    private void setView() {
        tvTitle.setText("收银台");
        spUtil = SharedPreferenceUtil.getInstance(this);
        getBalance();
        fIntent = getIntent();
        previous = fIntent.getIntExtra("previous", 0);
        previous_liucheng = fIntent.getIntExtra("previous_liucheng", 0);
        orderNo = fIntent.getIntExtra("orderid", 0);
        layout_payway.setBackgroundResource(R.color.af8f8f8);
        if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 狗证支付
            layout_payway.setBackgroundResource(R.color.white);
            CertiOrderId = fIntent.getIntExtra("CertiOrderId", 0);
            orderNo = CertiOrderId;
            type = 12;
            // 获取订单信息
            getOrderInfo();
            servicename = "宠物家办证";
            payWays("certi");
            if (orderNo <= 0) {
                timerDown();
            } else {
                getTimeDown();
            }
            iv_orderpay_dhm.setBackgroundResource(R.drawable.icon_petadd_unselect);
            rl_orderpay_dhm.setVisibility(View.VISIBLE);
        }
        tv_orderpay_paytitle.setText(servicename);
        if (previous == Global.UPDATE_TO_ORDERPAY) {
            tv_orderpay_paytitle.setText("订单升级需付金额");
        }
        btPay.setText("¥" + Utils.formatDouble(payfee, 2) + " 确认支付");
        tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
        oldpayway = spUtil.getInt("payway", 0);
        if (oldpayway == 2) {
            oldpayway = 2;
            paytype = 2;
            needpayfee = payfee;
            ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
            ivBalance
                    .setBackgroundResource(R.drawable.icon_petadd_unselect);
            ivAliPay.setBackgroundResource(R.drawable.icon_petadd_select);
            iv_orderpay_cika
                    .setBackgroundResource(R.drawable.icon_petadd_unselect);
        } else {
            paytype = 1;
            needpayfee = payfee;
            iv_orderpay_cika
                    .setBackgroundResource(R.drawable.icon_petadd_unselect);
            ivWXPay.setBackgroundResource(R.drawable.icon_petadd_select);
            ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
            ivBalance
                    .setBackgroundResource(R.drawable.icon_petadd_unselect);
        }
        ibBack.setOnClickListener(this);
        ibHybirdPay.setOnClickListener(this);
        rlAliPay.setOnClickListener(this);
        rlBalance.setOnClickListener(this);
        rlWXPay.setOnClickListener(this);
        btPay.setOnClickListener(this);
        rl_orderpay_cika.setOnClickListener(this);
        tv_orderpay_nosetpaypwddesc.setOnClickListener(this);
        layout_recharge_detail.setOnClickListener(this);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                if (isCheckRedeem) {
                    payfee = localPayFee;
                    isCheckRedeem = false;
                }
                setDHM(false, orderFee, orderFee);
                isHybird();
                setNeedFee();
                btPay.setText("¥" + Utils.formatDouble(payfee, 2) + " 确认支付");
                tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
                if (!TextUtils.isEmpty(et_orderpay_dhm.getText())) {
                    checkRedeemCode();
                }
            }
        });
    }

    private void payWays(String type) {
        CommUtil.payWays(mContext, type, CertiOrderId, payWaysHandler);
    }

    private AsyncHttpResponseHandler payWaysHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("timeCardTag")
                                && !jData.isNull("timeCardTag")) {
                            Utils.setText(tv_orderpay_cika,
                                    jData.getString("timeCardTag"), "",
                                    View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("payWays") && !jData.isNull("payWays")) {
                            JSONArray jsonArray = jData.getJSONArray("payWays");
                            if (jsonArray.length() > 0) {
                                listpayWays.setLength(0);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listpayWays.append(jsonArray.getString(i));
                                }
                                if (listpayWays.toString().contains("1")) {// 微信
                                    isWeiXin = true;
                                    rlWXPay.setVisibility(View.VISIBLE);
                                } else {
                                    isWeiXin = false;
                                    rlWXPay.setVisibility(View.GONE);
                                }
                                if (listpayWays.toString().contains("2")) {// 支付宝
                                    isZhiFuBao = true;
                                    rlAliPay.setVisibility(View.VISIBLE);
                                } else {
                                    isZhiFuBao = false;
                                    rlAliPay.setVisibility(View.GONE);
                                }
                                if (listpayWays.toString().contains("10")) {// 混合支付
                                    isHunHePayWays = true;
                                    rlHybirdPay.setVisibility(View.VISIBLE);
                                } else {
                                    isHunHePayWays = false;
                                    rlHybirdPay.setVisibility(View.GONE);
                                    setHybird(0);
                                }
                                if (listpayWays.toString().contains("4")) {// 余额
                                    isYuE = true;
                                    rlBalance.setVisibility(View.VISIBLE);
                                } else {
                                    isYuE = false;
                                    rlBalance.setVisibility(View.GONE);
                                }
                                if (listpayWays.toString().contains("7")) {// 次卡
                                    isCiKa = true;
                                    rl_orderpay_cika.setVisibility(View.VISIBLE);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.topMargin = Utils.dip2px(mContext, 40);
                                    params.leftMargin = Utils.dip2px(mContext, 40);
                                    params.height = Utils.dip2px(mContext, 30.5f);
                                    params.addRule(RelativeLayout.BELOW, R.id.layout_payway);
                                    ll_recharge_detail.setLayoutParams(params);
                                } else {
                                    isCiKa = false;
                                    rl_orderpay_cika.setVisibility(View.GONE);//后期调试下有次卡的情况，，，，******************
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.topMargin = Utils.dip2px(mContext, -15);
                                    params.leftMargin = Utils.dip2px(mContext, 40);
                                    params.height = Utils.dip2px(mContext, 30.5f);
                                    params.addRule(RelativeLayout.BELOW, R.id.layout_payway);
                                    ll_recharge_detail.setLayoutParams(params);
                                }
                            }
                        }
                        getBalance();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utils.mLogError("==-->Exception " + e.getMessage());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {

        }
    };

    private void getTimeDown() {
        CommUtil.getResidualTimeOfPay(mContext,
                spUtil.getString("cellphone", ""), spUtil.getInt("userid", 0),
                orderNo, type, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("==-->倒计时  " + new String(responseBody));
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("residualTime")
                                && !objectData.isNull("residualTime")) {
                            lastOverTime = objectData.getLong("residualTime");
                            timerDown();
                        }
                    }
                } else {
                    if (object.has("msg") && !object.isNull("msg")) {
                        String msg = object.getString("msg");
                        ToastUtil.showToastShortCenter(OrderPayActivity.this,
                                msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Utils.mLogError("==-->Exception " + e.getMessage());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {

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
            Global.ServerEvent(OrderPayActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_orderpay_back);
            goBack();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_orderpay_nosetpaypwddesc:
                startActivityForResult(new Intent(OrderPayActivity.this, SetUpPayPwdActivity.class), Global.ORDERPAY_TO_SETUPPAYPWD_NEXT);
                break;
            case R.id.rl_orderpay_cika:// 次卡支付
                if (paytype != 7) {
                    isWeiXin = false;
                    isZhiFuBao = false;
                    isYuE = false;
                    isCiKa = true;
                    paytype = 7;
                    oldpayway = 7;
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_select);
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    needpayfee = 0;
                }
                break;
            case R.id.ib_titlebar_back:
                Global.ServerEvent(OrderPayActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_orderpay_back);
                goBack();
                break;
            case R.id.rl_orderpay_balance:
                // 余额支付
                if (isRecharge) {
                    // 余额不足，可以去充值
                    //goNextForData(RechargeNewActivity.class, Global.PRE_ORDERDETAIL_TO_RECHARGE);
                    getStatistics(Global.ServerEventID.choose_pay_page, Global.ServerEventID.click_balanceisnulltorecharge_pay);
                } else {
                    // 余额支付
                    if (paytype != 4) {
                        paytype = 4;
                        oldpayway = 4;
                        isWeiXin = false;
                        isZhiFuBao = false;
                        isYuE = true;
                        isCiKa = false;
                        ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                        iv_orderpay_cika.setBackgroundResource(R.drawable.icon_petadd_unselect);
                        ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                        ivBalance.setBackgroundResource(R.drawable.icon_petadd_select);
                        needpayfee = 0;
                    }
                }
                break;
            case R.id.ib_orderpay_hybirdpay:
                // 混合支付开关
                if (isHybirdPay) {
                    isclickhybirdpay = true;
                    isHybirdPay = false;
                    ibHybirdPay.setBackgroundResource(R.drawable.noty_no);
                    setHybird(0);
                    needpayfee = payfee;
                    btPay.setText("¥" + Utils.formatDouble(payfee, 2) + " 确认支付");
                } else {
                    isHybirdPay = true;
                    isclickhybirdpay = false;
                    ibHybirdPay.setBackgroundResource(R.drawable.noty_yes);
                    setHybird(1);
                    needpayfee = payfee - balance;
                    btPay.setText("¥" + Utils.formatDouble(needpayfee, 2) + " 确认支付");
                }
                break;
            case R.id.rl_orderpay_paywx:
                // 微信支付
                if (paytype != 1) {
                    paytype = 1;
                    oldpayway = 1;
                    isWeiXin = true;
                    isZhiFuBao = false;
                    isYuE = false;
                    isCiKa = false;
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_select);
                    iv_orderpay_cika.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    if (isHybirdPay) {
                        needpayfee = ComputeUtil.sub(payfee, balance);
                    } else {
                        needpayfee = payfee;
                    }
                }
                if (isHybirdPay) {
                    setHybird(1);
                } else {
                    setHybird(0);
                }
                break;
            case R.id.rl_orderpay_payali:
                // 支付宝支付
                if (paytype != 2) {
                    paytype = 2;
                    oldpayway = 2;
                    isWeiXin = false;
                    isZhiFuBao = true;
                    isYuE = false;
                    isCiKa = false;
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    iv_orderpay_cika.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_select);
                    ivBalance.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    if (isHybirdPay) {
                        needpayfee = ComputeUtil.sub(payfee, balance);
                    } else {
                        needpayfee = payfee;
                    }
                }
                if (isHybirdPay) {
                    setHybird(1);
                } else {
                    setHybird(0);
                }
                break;
            case R.id.bt_orderpay_pay:
                switch (paytype) {
                    case 1:
                        if (!isWeiXin) {
                            paytype = 0;
                        }
                        break;
                    case 2:
                        if (!isZhiFuBao) {
                            paytype = 0;
                        }
                        break;
                    case 4:
                        if (!isYuE) {
                            paytype = 0;
                        }
                        break;
                    case 7:
                        if (!isCiKa) {
                            paytype = 7;
                        }
                        break;

                    default:
                        break;
                }
                if (paytype <= 0) {
                    if (isManZu == false && isWeiXin == false
                            && isZhiFuBao == false && isHunHePayWays == false
                            && isCiKa == false && isYuE == true) {
                        ToastUtil.showToastShortBottom(mContext, "请先充值再付款哦~");
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "请选择支付方式");
                    }
                } else {
                    Global.ServerEvent(OrderPayActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_comfirm_pay);
                    if (paytype == 4) {
                        //判断是否设置支付密码
                        if (payPwd == 0) {//未设置过支付密码
                            boolean isNextSetPayPwd = spUtil.getBoolean("isNextSetPayPwd", false);
                            if (isNextSetPayPwd) {
                                goToPayApi();
                            } else {
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        spUtil.saveBoolean("isNextSetPayPwd", true);
                                        goToPayApi();
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
                    } else {
                        goToPayApi();
                    }
                }
                break;
        }
    }

    private void goToPayApi() {
        mPDialog.showDialog();
        if (needpayfee <= 0)
            needpayfee = 0;
        if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 办证
            changePayWay();
        }
    }

    private void checkRedeemCode() {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
        if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 狗证
            CommUtil.checkRedeemCode(yqm, tcid, 0, "PETCARD", 0, 0, null,
                    spUtil.getString("cellphone", ""), this, 0,
                    checkRedeemCodeHanler);
        }
    }

    private double localPayFee = 0;

    private AsyncHttpResponseHandler checkRedeemCodeHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            Utils.mLogError("校验邀请码：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject object = jobj.getJSONObject("data");
                        if (object.has("orderFee") && !object.isNull("orderFee")) {
                            isCheckRedeem = true;
                            if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
                                orderFee = object.getInt("orderFee") / 100;
                            }
                            localPayFee = payfee;
                            if (payfee <= orderFee) {
                                paytype = 3;
                                payfee = 0;
                            } else {
                                payfee = ComputeUtil.sub(payfee, orderFee);
                            }
                            setDHM(true, orderFee, orderFee);
                        }
                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShortBottom(OrderPayActivity.this, jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Utils.mLogError("==-->Exception " + e.getMessage());
            }
            isHybird();
            setNeedFee();
            btPay.setText("¥" + Utils.formatDouble(payfee, 2) + " 确认支付");
            tvPayFee.setText("¥" + Utils.formatDouble(payfee, 2));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(OrderPayActivity.this, "请求失败");
            btPay.setEnabled(true);
        }
    };

    private void changePayWay() {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
        CommUtil.changePayWay(yqm, isHybirdPay, balance * 100, payfee * 100,
                spUtil.getString("cellphone", ""), Global.getIMEI(this), this,
                orderNo, spUtil.getInt("userid", 0), paytype, tcid,
                changePayWayHanler);
    }

    private AsyncHttpResponseHandler changePayWayHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            appid = null;
            noncestr = null;
            packageValue = null;
            partnerid = null;
            prepayid = null;
            sign = null;
            timestamp = null;
            orderStr = null;
            mPDialog.closeDialog();
            btPay.setEnabled(true);
            Utils.mLogError("狗证订单二次支付：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    JSONObject jdata = null;
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        jdata = jobj.getJSONObject("data");
                        if (jdata.has("give_can") && !jdata.isNull("give_can")) {
                            JSONObject obGiveCan = jdata.getJSONObject("give_can");
                            if (obGiveCan.has("myself") && !obGiveCan.isNull("myself")) {
                                myselfDummpNum = obGiveCan.getInt("myself");
                            }
                            if (obGiveCan.has("compare_desc") && !obGiveCan.isNull("compare_desc")) {
                                compare_desc = obGiveCan.getString("compare_desc");
                            }
                            if (obGiveCan.has("can_purpose_url") && !obGiveCan.isNull("can_purpose_url")) {
                                can_purpose_url = obGiveCan.getString("can_purpose_url");
                            }
                        }
                        if (jdata.has("isVip") && !jdata.isNull("isVip")) {
                            isVip = jdata.getInt("isVip");
                        }
                        if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
                            JSONObject jpayInfo = jdata.getJSONObject("payInfo");
                            if (jpayInfo.has("appid") && !jpayInfo.isNull("appid")) {
                                appid = jpayInfo.getString("appid");
                            }
                            if (jpayInfo.has("noncestr") && !jpayInfo.isNull("noncestr")) {
                                noncestr = jpayInfo.getString("noncestr");
                            }
                            if (jpayInfo.has("package") && !jpayInfo.isNull("package")) {
                                packageValue = jpayInfo.getString("package");
                            }
                            if (jpayInfo.has("partnerid") && !jpayInfo.isNull("partnerid")) {
                                partnerid = jpayInfo.getString("partnerid");
                            }
                            if (jpayInfo.has("prepayid") && !jpayInfo.isNull("prepayid")) {
                                prepayid = jpayInfo.getString("prepayid");
                            }
                            if (jpayInfo.has("sign") && !jpayInfo.isNull("sign")) {
                                sign = jpayInfo.getString("sign");
                            }
                            if (jpayInfo.has("timestamp") && !jpayInfo.isNull("timestamp")) {
                                timestamp = jpayInfo.getString("timestamp");
                            }
                            if (jpayInfo.has("orderStr") && !jpayInfo.isNull("orderStr")) {
                                orderStr = jpayInfo.getString("orderStr");
                            }
                        }
                    }
                    if (needpayfee == 0) {
                        ToastUtil.showToastShortBottom(OrderPayActivity.this, "支付成功");
                        // 直接跳转到支付成功
                        goPayResult();
                    } else {
                        // 第三方支付
                        goPay();
                    }
                } else {
                    setPayLoadFail();
                    if (resultCode == Global.PAY_SUCCESS) {
                        goPayResult();
                    }
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShortBottom(OrderPayActivity.this, jobj.getString("msg"));
                }
            } catch (JSONException e) {
                setPayLoadFail();
                e.printStackTrace();
                Utils.mLogError("==-->Exception " + e.getMessage());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(OrderPayActivity.this, "请求失败");
            btPay.setEnabled(true);
            setPayLoadFail();
        }
    };

    private void setPayLoadFail() {
        if (paytype == 4 && pWinPayPwdSystem != null && pWinPayPwdSystem.isShowing()) {
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

    private void setBalanceAvaiable(boolean flag) {
        if (flag) {
            isRecharge = false;
            ivBalance.setVisibility(View.VISIBLE);
            tvBalanceHint.setVisibility(View.GONE);
        } else {
            isRecharge = true;
            ivBalance.setVisibility(View.GONE);
            if (balance <= 0) {
                tvBalanceHint.setVisibility(View.VISIBLE);
            } else {
                tvBalanceHint.setVisibility(View.GONE);
            }
        }
    }

    private void goPayResult() {
        if (paytype == 4 && pWinPayPwdSystem != null) {
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
                        goPayResult1();
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
                            goPayResult1();
                        }
                    }, 1500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pWinPayPwdSystem.dismiss();
                            if (mFingerprintCore.isSupport()) {
                                spUtil.saveBoolean("isFirstSetPwd", true);
                                startActivityForResult(new Intent(OrderPayActivity.this, SetFingerprintActivity.class).putExtra("flag",1), Global.ORDERPAY_TO_STARTFINGER);
                            } else {
                                goPayResult1();
                            }
                        }
                    }, 1500);
                }
            }
        } else {
            goPayResult1();
        }
    }

    private void goPayResult1() {
        if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 办证
            if (ADActivity.act != null) {
                ADActivity.act.finishWithAnimation();
            }
            goPaySuccess();
        }
        finishWithAnimation();
    }

    private void goPaySuccess() {
        Intent intent = new Intent(act, PaySuccessActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("previous", previous);
        intent.putExtra("previous_liucheng", previous_liucheng);
        Utils.mLogError("==-->流程购买次卡  " + getClass().getName() + " --- 000 --- " + previous_liucheng);

        /**
         * 新版洗美特色服务需要这些参数 支付成功界面接收用type判断过滤的。
         */
        intent.putExtra("compare_desc", compare_desc);
        intent.putExtra("myself", myselfDummpNum);
        intent.putExtra("can_purpose_url", can_purpose_url);
        intent.putExtra("isVip", isVip);
        startActivity(intent);
        finishWithAnimation();
    }

    private void goPay() {
        if (paytype == 1) {
            if (appid != null && !TextUtils.isEmpty(appid) && noncestr != null
                    && !TextUtils.isEmpty(noncestr) && packageValue != null
                    && !TextUtils.isEmpty(packageValue) && partnerid != null
                    && !TextUtils.isEmpty(partnerid) && prepayid != null
                    && !TextUtils.isEmpty(prepayid) && sign != null
                    && !TextUtils.isEmpty(sign) && timestamp != null
                    && !TextUtils.isEmpty(timestamp)) {
                // 微信支付
                if (!mPDialog.isShowing()) {
                    mPDialog.showDialog();
                }
                isArousePay = true;
                isPay = false;
                spUtil.saveInt("payway", 1);
                PayUtils.weChatPayment(OrderPayActivity.this, appid, partnerid,
                        prepayid, packageValue, noncestr, timestamp, sign,
                        mPDialog);
            } else {
                ToastUtil.showToastShortBottom(OrderPayActivity.this, "支付参数错误");
            }
        } else if (paytype == 2) {
            if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                // 判断是否安装支付宝
                spUtil.saveInt("payway", 2);
                PayUtils.checkAliPay(OrderPayActivity.this, aliHandler);
            } else {
                ToastUtil.showToastShortBottom(OrderPayActivity.this, "支付参数错误");
            }
        }
    }

    private void getBalance() {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
        CommUtil.getAccountBalance(this, spUtil.getString("cellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this),
                balanceHanler);
    }

    private AsyncHttpResponseHandler balanceHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("账户余额：" + new String(responseBody));
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata.has("user") && !jdata.isNull("user")) {
                        JSONObject juser = jdata.getJSONObject("user");
                        if (juser.has("payPwd") && !juser.isNull("payPwd")) {
                            payPwd = juser.getInt("payPwd");
                        }
                        if (juser.has("balance") && !juser.isNull("balance")) {
                            balance = juser.getDouble("balance");
                        }
                        tvBalance.setText(Double.toString(Utils.formatDouble(balance, 2)));
                    }
                    isHybird();
                    setNeedFee();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Utils.mLogError("==-->Exception " + e.getMessage());
            }
            if (payPwd == 0) {
                tv_orderpay_nosetpaypwddesc.setVisibility(View.VISIBLE);
            } else if (payPwd == 1) {
                tv_orderpay_nosetpaypwddesc.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void isHybird() {
        if (balance >= payfee) {
            isManZu = true;
            setBalanceAvaiable(true);
            isHybirdPay = false;
            rlHybirdPay.setVisibility(View.GONE);
            setHybird(0);
            if (isRechargeReturn) {
                // 充值返回，默认选余额
                oldpayway = 4;
                paytype = 4;
                needpayfee = 0;
                ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                iv_orderpay_cika
                        .setBackgroundResource(R.drawable.icon_petadd_unselect);
                ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                ivBalance.setBackgroundResource(R.drawable.icon_petadd_select);
            } else {
                paytype = oldpayway;
                if (oldpayway == 2) {
                    oldpayway = 2;
                    paytype = 2;
                    needpayfee = payfee;
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_select);
                } else if (oldpayway == 1) {
                    oldpayway = 1;
                    paytype = 1;
                    needpayfee = payfee;
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_select);
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                } else {
                    oldpayway = 4;
                    paytype = 4;
                    needpayfee = 0;
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_select);
                }
            }
        } else {
            isManZu = false;
            if (orderFee > 0) {
                setBalanceAvaiable(false);
                if (balance <= 0) {
                    isHybirdPay = false;
                    rlHybirdPay.setVisibility(View.GONE);
                    setHybird(0);
                } else {
                    if (isHunHePayWays) {
                        rlHybirdPay.setVisibility(View.VISIBLE);
                        if (!isclickhybirdpay) {
                            isHybirdPay = true;
                            setHybird(1);
                        } else {
                            isHybirdPay = false;
                            setHybird(0);
                        }
                    } else {
                        isHybirdPay = false;
                        rlHybirdPay.setVisibility(View.GONE);
                        setHybird(0);
                    }
                }
                paytype = oldpayway;
                if (oldpayway == 2) {
                    oldpayway = 2;
                    paytype = 2;
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_select);
                } else {
                    oldpayway = 1;
                    paytype = 1;
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_select);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                }
            } else {
                setBalanceAvaiable(false);
                if (balance <= 0) {
                    isHybirdPay = false;
                    rlHybirdPay.setVisibility(View.GONE);
                    setHybird(0);
                } else {
                    if (isHunHePayWays) {
                        rlHybirdPay.setVisibility(View.VISIBLE);
                        if (!isclickhybirdpay) {
                            isHybirdPay = true;
                            setHybird(1);
                        } else {
                            isHybirdPay = false;
                            setHybird(0);
                        }
                    } else {
                        isHybirdPay = false;
                        rlHybirdPay.setVisibility(View.GONE);
                        setHybird(0);
                    }
                }
                paytype = oldpayway;
                if (oldpayway == 2) {
                    oldpayway = 2;
                    paytype = 2;
                    needpayfee = payfee;
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_select);
                } else {
                    oldpayway = 1;
                    paytype = 1;
                    needpayfee = payfee;
                    iv_orderpay_cika
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivWXPay.setBackgroundResource(R.drawable.icon_petadd_select);
                    ivAliPay.setBackgroundResource(R.drawable.icon_petadd_unselect);
                    ivBalance
                            .setBackgroundResource(R.drawable.icon_petadd_unselect);
                }
            }
        }
        if (isHybirdPay && rlHybirdPay.getVisibility() == View.VISIBLE) {
            setHybird(1);
        } else {
            setHybird(0);
        }
    }

    private void setHybird(int flag) {
        if (flag == 0) {//全部隐藏
            ll_orderpay_hybirdpay_fee_weixin.setVisibility(View.GONE);
            ll_orderpay_hybirdpay_fee_zhifubao.setVisibility(View.GONE);
        } else if (flag == 1) {
            if (paytype == 1) {//微信混合支付
                ll_orderpay_hybirdpay_fee_weixin.setVisibility(View.VISIBLE);
                ll_orderpay_hybirdpay_fee_zhifubao.setVisibility(View.GONE);
                tv_orderpay_hybirdpay_fee_weixin.setText("¥" + Double.toString(Utils.formatDouble(
                        ComputeUtil.sub(payfee, balance), 2)));
            } else if (paytype == 2) {//支付宝混合支付
                ll_orderpay_hybirdpay_fee_weixin.setVisibility(View.GONE);
                ll_orderpay_hybirdpay_fee_zhifubao.setVisibility(View.VISIBLE);
                tv_orderpay_hybirdpay_fee_zhifubao.setText("¥" + Double.toString(Utils.formatDouble(
                        ComputeUtil.sub(payfee, balance), 2)));
            }
        }
    }

    private void goBack() {
        Log.e("TAG", "isArousePay = " + isArousePay);
        Log.e("TAG", "isPay = " + isPay);
        if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
            Intent data = new Intent();
            data.putExtra("payurl", CommUtil.getWebBaseUrl()
                    + "web/petcerti/register?certi_id=" + certiId);
            setResult(Global.RESULT_OK, data);
            finishWithAnimation();
        }
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
            goPayResult();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (mPDialog != null) {
            mPDialog.dimissDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFingerprintCore.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
        if (aliHandler != null) {
            aliHandler.removeCallbacksAndMessages(null);
        }
        if (mPDialog != null) {
            mPDialog.dimissDialog();
        }
        try {
            if (task != null) {
                task.cancel();
            }
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.PRE_ORDERDETAIL_TO_RECHARGE) {
                isRechargeReturn = true;
                getBalance();
            } else if (requestCode == Global.ORDERPAY_TO_STARTFINGER) {
                goPayResult1();
            } else if (requestCode == Global.ORDERPAY_TO_SETUPPAYPWD_NEXT) {
                getBalance();
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

    private void showPayPwdSystem(int flag, boolean isPwd) {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(OrderPayActivity.this, R.anim.commodity_detail_show));//开始动画
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
            tv_paypwdsystem_pop_payprice.setText("¥" + Utils.formatDouble(payfee, 2));
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
            iv_paypwdsystem_pop_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinPayPwdSystem.dismiss();
                }
            });
            btn_paypwdsystem_pop.setOnClickListener(new OnClickListener() {
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
            tv_paypwdsystem_pop_pwdtype.setOnClickListener(new OnClickListener() {
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
                    goToPayApi();
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
                ToastUtil.showToastShortBottom(OrderPayActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(OrderPayActivity.this, "请求失败");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getServiceName(ArrayList<MulPetService> list) {
        if (list.size() <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).serviceType + ",");
        }
        return sb.toString();

    }

    public String getServiceName(List<ApointMentPet> list) {
        if (list.size() <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getServiceId() + ",");
        }
        return sb.toString();

    }

    private void getOrderInfo() {
        CommUtil.getOrderInfo(this, spUtil.getString("cellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this),
                CertiOrderId, orderInfoHanler);
    }

    private AsyncHttpResponseHandler orderInfoHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            String result = new String(responseBody);
            Utils.mLogError("办理狗证订单信息：" + result);
            processData(result);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(OrderPayActivity.this, "网络异常，请重新提交");
        }
    };

    // 解析json数据
    private void processData(String result) {
        try {
            JSONObject jobj = new JSONObject(result);
            if (jobj.has("code") && !jobj.isNull("code")) {
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        Gson gson = new Gson();
                        PetCardInfoCodeBean data = gson.fromJson(
                                jdata.toString(), PetCardInfoCodeBean.class);
                        if (data != null) {
                            certiId = data.getCertiId();
                            payfee = ComputeUtil.div(data.getTotalprice(), 100, 2);
                            certiCoupons = data.getCertiCoupons();
                            String certiCouponDe = data.getCertiCouponDe();
                            if (certiCouponDe != null
                                    && !TextUtils.isEmpty(certiCouponDe)) {
                                tv_orderpay_des.setText(certiCouponDe);
                                vw_line3.setVisibility(View.VISIBLE);
                                tv_orderpay_des.setVisibility(View.VISIBLE);
                            } else {
                                vw_line3.setVisibility(View.GONE);
                                tv_orderpay_des.setVisibility(View.GONE);
                            }
                            btPay.setText("¥" + Utils.formatDouble(payfee, 2) + " 确认支付");
                            tvPayFee.setText("¥"
                                    + Utils.formatDouble(payfee, 2));
                            if (certiCoupons != null && certiCoupons.size() > 0) {
                                ll_orderpay_tc.setVisibility(View.VISIBLE);
                                orderPayTcAdapter = new OrderPayTcAdapter(this,
                                        certiCoupons);
                                lv_orderpay_tc.setAdapter(orderPayTcAdapter);
                                // 默认选择第一个套餐
                                orderPayTcAdapter.setOnItemClick(0);
                                CertiCouponsBean certiCouponsBean = certiCoupons
                                        .get(0);
                                setTc(certiCouponsBean);
                                isHybird();
                                setNeedFee();
                                if (certiCoupons.size() == 1) {
                                    String description = certiCoupons.get(0)
                                            .getDescription();
                                    if (description != null
                                            && !TextUtils.isEmpty(description)) {
                                        lv_orderpay_tc
                                                .setVisibility(View.VISIBLE);
                                    } else {
                                        lv_orderpay_tc.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                lv_orderpay_tc.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg")) {
                        String msg = jobj.getString("msg");
                        if (msg != null && !TextUtils.isEmpty(msg)) {
                            ToastUtil.showToastShortBottom(this, msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.mLogError("==-->Exception " + e.getMessage());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void getStatistics(String typeid, String activeid) {
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
