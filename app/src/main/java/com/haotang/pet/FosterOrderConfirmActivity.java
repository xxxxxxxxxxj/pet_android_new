package com.haotang.pet;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.widget.NestedScrollView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.FosterOrderDetailPetAdapter;
import com.haotang.pet.adapter.WashOrderDetailDiscountAdapter;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.FosterPet;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.WashDiscount;
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
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CustomStatusView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 寄养订单确认页
 */
public class FosterOrderConfirmActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_fosterorderconfirm_call)
    ImageView ivFosterorderconfirmCall;
    @BindView(R.id.tv_fosterorderconfirm_shopname)
    TextView tvFosterorderconfirmShopname;
    @BindView(R.id.iv_fosterorderconfirm_call1)
    ImageView ivFosterorderconfirmCall1;
    @BindView(R.id.tv_fosterorderconfirm_rzrq)
    TextView tvFosterorderconfirmRzrq;
    @BindView(R.id.tv_fosterorderconfirm_rztime)
    TextView tvFosterorderconfirmRztime;
    @BindView(R.id.tv_fosterorderconfirm_ldrq)
    TextView tvFosterorderconfirmLdrq;
    @BindView(R.id.tv_fosterorderconfirm_ldtime)
    TextView tvFosterorderconfirmLdtime;
    @BindView(R.id.iv_fosterorderconfirm_gotop)
    ImageView ivFosterorderconfirmGotop;
    @BindView(R.id.btn_fosterorderconfirm_submit)
    Button btnFosterorderconfirmSubmit;
    @BindView(R.id.tv_fosterorderconfirm_price)
    TextView tvFosterorderconfirmPrice;
    @BindView(R.id.rl_fosterorderconfirm_bottom)
    RelativeLayout rlFosterorderconfirmBottom;
    @BindView(R.id.tv_fosterorderconfirm_roomprice)
    TextView tvFosterorderconfirmRoomprice;
    @BindView(R.id.tv_fosterorderconfirm_roomtype)
    TextView tvFosterorderconfirmRoomtype;
    @BindView(R.id.tv_fosterorderconfirm_roomdj)
    TextView tvFosterorderconfirmRoomdj;
    @BindView(R.id.rv_fosterorderconfirm_pet)
    RecyclerView rvFosterorderconfirmPet;
    @BindView(R.id.ll_fosterorderconfirm_lxrtitle)
    LinearLayout llFosterorderconfirmLxrtitle;
    @BindView(R.id.tv_fosterorderconfirm_lxr)
    TextView tvFosterorderconfirmLxr;
    @BindView(R.id.rl_fosterorderconfirm_lxr)
    RelativeLayout rlFosterorderconfirmLxr;
    @BindView(R.id.tv_fosterorderconfirm_remarktitle)
    TextView tvFosterorderconfirmRemarktitle;
    @BindView(R.id.tv_fosterorderconfirm_remark)
    TextView tvFosterorderconfirmRemark;
    @BindView(R.id.rl_fosterorderconfirm_remark)
    RelativeLayout rlFosterorderconfirmRemark;
    @BindView(R.id.tv_fosterorderconfirm_lpktitle)
    TextView tvFosterorderconfirmLpktitle;
    @BindView(R.id.tv_fosterorderconfirm_lpk)
    TextView tvFosterorderconfirmLpk;
    @BindView(R.id.rl_fosterorderconfirm_lpk)
    RelativeLayout rlFosterorderconfirmLpk;
    @BindView(R.id.tv_fosterorderconfirm_yhqtitle)
    TextView tvFosterorderconfirmYhqtitle;
    @BindView(R.id.tv_fosterorderconfirm_yhq)
    TextView tvFosterorderconfirmYhq;
    @BindView(R.id.rl_fosterorderconfirm_yhq)
    RelativeLayout rlFosterorderconfirmYhq;
    @BindView(R.id.tv_fosterorderconfirm_totalprice)
    TextView tvFosterorderconfirmTotalprice;
    @BindView(R.id.rv_fosterorderconfirm_discount)
    RecyclerView rvFosterorderconfirmDiscount;
    @BindView(R.id.tv_fosterorderconfirm_cardprice)
    TextView tvFosterorderconfirmCardprice;
    @BindView(R.id.tv_fosterorderconfirm_cardname_pay)
    TextView tvFosterorderconfirmCardnamePay;
    @BindView(R.id.rl_fosterorderconfirm_card)
    RelativeLayout rlFosterorderconfirmCard;
    @BindView(R.id.iv_fosterorderconfirm_select)
    ImageView ivFosterorderconfirmSelect;
    @BindView(R.id.ll_fosterorderconfirm_select)
    LinearLayout llFosterorderconfirmSelect;
    @BindView(R.id.tv_fosterorderconfirm_xy)
    TextView tvFosterorderconfirmXy;
    @BindView(R.id.tv_fosterorderconfirm_num)
    TextView tvFosterorderconfirmNum;
    @BindView(R.id.nsv_fosterorderconfirm)
    NestedScrollView nsvFosterorderconfirmNum;
    @BindView(R.id.ll_fosterorderconfirm_bottom)
    LinearLayout llFosterorderconfirmBottom;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    private StringBuilder listpayWays = new StringBuilder();
    private int oldpayway;
    private int cardId = -1;
    private List<MyCard> cardList = new ArrayList<MyCard>();
    private String cardName;
    private List<Coupon> serviceCouponList = new ArrayList<Coupon>();
    private List<Coupon> fosterCouponList = new ArrayList<Coupon>();
    private int couponId;
    private int canUseServiceCard;
    private int orderNo;
    private boolean isSelect;
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
    private boolean isTime;
    private ArrayList<String> discountList = new ArrayList<String>();
    private AlertDialogNavAndPost builder;
    private int flag;
    private int playAnim = 0;
    private double totalPrice;
    private double cardPayPrice;
    private double thirdPrice;
    private boolean isNeedCard;
    private String couponName;
    private String discountText;
    private int paytype;
    private List<WashDiscount> localDiscountList = new ArrayList<WashDiscount>();
    private String agreementPage;
    private String servicePhone;
    private String contact = "";
    private String contactPhone = "";
    private List<FosterPet> petList = new ArrayList<FosterPet>();
    private FosterOrderDetailPetAdapter fosterOrderDetailPetAdapter;
    private WashOrderDetailDiscountAdapter washOrderDetailDiscountAdapter;
    private int roomType;
    private int mypetId;//主宠物ID
    private int shopId;
    private String startTime;
    private String endTime;
    private String strp;
    private String extraPetIds;//附加宠物ID
    private double roomUnitPrice;
    private int days;
    private int couponEnable;
    private double normalCouponDiscountPrice;
    private double cardDiscountPrice;
    private double payPrice;
    private int careShopId;
    private String appointment;
    private double bathPayPrice;
    private double fosterPayPrice;
    private double firstPayPrice;
    private int couponIndex;
    private int fosterCouponSize;
    private int serviceCouponSize;
    private int cardSize;
    private List<Coupon> fosterCouponSizeList = new ArrayList<Coupon>();
    private List<Coupon> serviceCouponSizeList = new ArrayList<Coupon>();
    private int couponSize;

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
                            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(FosterOrderConfirmActivity.this, orderStr,
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
                    if ((Build.MODEL.equals("OPPO R9m") || Build.MODEL.equals("OPPO R9s")) && Build.VERSION.RELEASE.equals("5.1")) {
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
    }

    private void initData() {
        Global.WXPAYCODE = -1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        oldpayway = spUtil.getInt("payway", 0);

        roomType = getIntent().getIntExtra("roomType", 0);
        mypetId = getIntent().getIntExtra("mypetId", 0);
        shopId = getIntent().getIntExtra("shopId", 0);
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        contact = getIntent().getStringExtra("contact");
        contactPhone = getIntent().getStringExtra("contactPhone");

        //选择了离店洗美才使用
        careShopId = getIntent().getIntExtra("careShopId", 0);
        strp = getIntent().getStringExtra("strp");
        extraPetIds = getIntent().getStringExtra("extraPetIds");
    }

    private void findView() {
        setContentView(R.layout.activity_foster_order_confirm);
        ButterKnife.bind(this);
    }

    private void setView() {
        String str = (Utils.isStrNull(contact) ? contact : "") + " " + (Utils.isStrNull(contactPhone) ? contactPhone : "");
        tvFosterorderconfirmLxr.setText(str.trim());
        tvTitlebarTitle.setText("确认订单");
        vTitleSlide.setVisibility(View.VISIBLE);
        tvFosterorderconfirmXy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvFosterorderconfirmXy.getPaint().setAntiAlias(true);//抗锯齿
        Utils.setText(tvFosterorderconfirmRzrq, startTime, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvFosterorderconfirmLdrq, endTime, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvFosterorderconfirmRztime, getIntent().getStringExtra("fosterTime") + "入住", "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvFosterorderconfirmLdtime, getIntent().getStringExtra("fosterTime") + "离店", "", View.VISIBLE, View.VISIBLE);

        rvFosterorderconfirmPet.setHasFixedSize(true);
        rvFosterorderconfirmPet.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager2 = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager2.setScrollEnabled(false);
        rvFosterorderconfirmPet.setLayoutManager(noScollFullLinearLayoutManager2);
        fosterOrderDetailPetAdapter = new FosterOrderDetailPetAdapter(R.layout.item_fosterorderdetail_pet, petList);
        rvFosterorderconfirmPet.setAdapter(fosterOrderDetailPetAdapter);

        rvFosterorderconfirmDiscount.setHasFixedSize(true);
        rvFosterorderconfirmDiscount.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvFosterorderconfirmDiscount.setLayoutManager(noScollFullLinearLayoutManager);
        washOrderDetailDiscountAdapter = new WashOrderDetailDiscountAdapter(R.layout.item_washorderdetail_discount, localDiscountList);
        rvFosterorderconfirmDiscount.setAdapter(washOrderDetailDiscountAdapter);
    }

    private void setLinster() {
        nsvFosterorderconfirmNum.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Rect scrollRect = new Rect();
                v.getHitRect(scrollRect);
                if (llFosterorderconfirmBottom.getLocalVisibleRect(scrollRect) && scrollY > 20) {
                    if (playAnim == 0) {
                        ivFosterorderconfirmGotop.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.topin_show));
                    }
                    ivFosterorderconfirmGotop.setVisibility(View.VISIBLE);
                    playAnim = 1;
                } else {
                    if (playAnim == 1) {
                        ivFosterorderconfirmGotop.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.bottomout_hind));
                    }
                    ivFosterorderconfirmGotop.setVisibility(View.GONE);
                    playAnim = 0;
                }
            }
        });
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
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getData() {
        mPDialog.showDialog();
        payPrice = 0;
        bathPayPrice = 0;
        fosterPayPrice = 0;
        CommUtil.getFosterOrderConfirmation(this, careShopId, roomType, mypetId, shopId, startTime, endTime, strp, extraPetIds, couponId, cardId, dataHanler);
    }

    private AsyncHttpResponseHandler dataHanler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("shop") && !jdata.isNull("shop")) {
                            JSONObject jshop = jdata.getJSONObject("shop");
                            if (jshop.has("hotelName") && !jshop.isNull("hotelName")) {
                                Utils.setText(tvFosterorderconfirmShopname, jshop.getString("hotelName"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jshop.has("phone") && !jshop.isNull("phone")) {
                                servicePhone = jshop.getString("phone");
                            }
                        }
                        if (jdata.has("roomPrice") && !jdata.isNull("roomPrice")) {
                            JSONObject jroomPrices = jdata.getJSONObject("roomPrice");
                            if (jroomPrices.has("total") && !jroomPrices.isNull("total")) {
                                Utils.setText(tvFosterorderconfirmRoomprice, "¥" + jroomPrices.getDouble("total"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jroomPrices.has("price") && !jroomPrices.isNull("price")) {
                                roomUnitPrice = jroomPrices.getDouble("price");
                            }
                            if (jroomPrices.has("days") && !jroomPrices.isNull("days")) {
                                days = jroomPrices.getInt("days");
                            }
                        }
                        if (jdata.has("roomType") && !jdata.isNull("roomType")) {
                            JSONObject jroomType = jdata.getJSONObject("roomType");
                            if (jroomType.has("name") && !jroomType.isNull("name")) {
                                Utils.setText(tvFosterorderconfirmRoomtype, jroomType.getString("name"), "", View.VISIBLE, View.VISIBLE);
                            }
                        }
                        if (jdata.has("totalPrice") && !jdata.isNull("totalPrice")) {
                            totalPrice = jdata.getDouble("totalPrice");
                        }
                        if (jdata.has("pets") && !jdata.isNull("pets")) {
                            JSONArray jarrpet = jdata.getJSONArray("pets");
                            for (int i = 0; i < jarrpet.length(); i++) {
                                petList.add(FosterPet.jsonToEntity(jarrpet.getJSONObject(i), 1));
                            }
                        }
                        if (jdata.has("couponEnable") && !jdata.isNull("couponEnable")) {
                            couponEnable = jdata.getInt("couponEnable");
                        }
                        if (jdata.has("agreementPage") && !jdata.isNull("agreementPage")) {
                            agreementPage = jdata.getString("agreementPage");
                        }
                        if (jdata.has("payPrice") && !jdata.isNull("payPrice")) {
                            payPrice = jdata.getDouble("payPrice");
                        }
                        if (jdata.has("appointment") && !jdata.isNull("appointment")) {
                            appointment = jdata.getString("appointment");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
            if (petList.size() > 0) {
                for (int i = 0; i < petList.size(); i++) {
                    bathPayPrice = ComputeUtil.add(bathPayPrice, petList.get(i).getListBathFee());
                }
            }
            fosterPayPrice = ComputeUtil.sub(payPrice, bathPayPrice);
            firstPayPrice = ComputeUtil.sub(payPrice, bathPayPrice);
            Utils.setText(tvFosterorderconfirmNum, "共" + days + "天", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvFosterorderconfirmRoomdj, "¥" + roomUnitPrice + "*" + days + "天", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvFosterorderconfirmTotalprice, "¥" + totalPrice, "", View.VISIBLE, View.VISIBLE);
            fosterOrderDetailPetAdapter.notifyDataSetChanged();
            if (couponEnable == 1) {
                rlFosterorderconfirmYhq.setVisibility(View.VISIBLE);
                getCoupon();
            } else if (couponEnable == 0) {
                rlFosterorderconfirmYhq.setVisibility(View.GONE);
                isNeedCard = true;
                getPayWay();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getCoupon() {
        mPDialog.showDialog();
        fosterCouponList.clear();
        couponId = 0;
        canUseServiceCard = 0;
        couponName = "";
        tvFosterorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
        tvFosterorderconfirmYhq.setText("无可用");
        CommUtil.getFosterAvailableCoupon(this, shopId, 2, mypetId, roomType, startTime, endTime, cardId, 7,
                fosterPayPrice, couponHanler);
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
                            fosterCouponList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
            boolean isAvail = false;
            if (fosterCouponList.size() > 0) {
                for (int i = 0; i < fosterCouponList.size(); i++) {
                    if (fosterCouponList.get(i).isAvali == 1) {
                        couponId = fosterCouponList.get(i).id;
                        canUseServiceCard = fosterCouponList.get(i).canUseServiceCard;
                        couponName = fosterCouponList.get(i).name;
                        isAvail = true;
                        break;
                    }
                }
            }
            if (isAvail) {
                couponIndex = 0;
            }
            if (!isAvail && Utils.isStrNull(strp)) {//请求服务优惠券
                getServiceCoupon();
            } else {
                if (canUseServiceCard == 1) {
                    isNeedCard = false;
                } else {
                    isNeedCard = true;
                }
                getPayWay();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getServiceCoupon() {
        mPDialog.showDialog();
        serviceCouponList.clear();
        couponId = 0;
        canUseServiceCard = 0;
        couponName = "";
        tvFosterorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
        tvFosterorderconfirmYhq.setText("无可用");
        CommUtil.getAvailableCoupon(this, spUtil.getString("cellphone", ""), Global.getIMEI(this),
                Global.getCurrentVersion(this), appointment, 1, 1,
                0, 0, "", "", 0, "", 0, 0,
                strp, 0, bathPayPrice, careShopId,
                null, 1, cardId, 0, 4, serviceCouponHanler);
    }

    private AsyncHttpResponseHandler serviceCouponHanler = new AsyncHttpResponseHandler() {

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
                            serviceCouponList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
            boolean isAvail = false;
            if (serviceCouponList.size() > 0) {
                for (int i = 0; i < serviceCouponList.size(); i++) {
                    if (serviceCouponList.get(i).isAvali == 1) {
                        couponId = serviceCouponList.get(i).id;
                        canUseServiceCard = serviceCouponList.get(i).canUseServiceCard;
                        couponName = serviceCouponList.get(i).name;
                        isAvail = true;
                        break;
                    }
                }
            }
            if (isAvail) {
                couponIndex = 1;
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
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getPayWay() {
        listpayWays.setLength(0);
        mPDialog.showDialog();
        CommUtil.payWays(this, Global.ORDERKEY[3], 0, payWaysHandler);
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
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
            if (listpayWays != null && listpayWays.length() > 0) {
                if (listpayWays.toString().contains("11")) {// E卡
                    rlFosterorderconfirmLpk.setVisibility(View.VISIBLE);
                    if (isNeedCard) {
                        getMyCard();
                    } else {
                        flag = 0;
                        confirmOrderPromptNew();
                    }
                } else {
                    rlFosterorderconfirmLpk.setVisibility(View.GONE);
                    flag = 0;
                    confirmOrderPromptNew();
                }
            } else {
                rlFosterorderconfirmLpk.setVisibility(View.GONE);
                flag = 0;
                confirmOrderPromptNew();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
        }
    };

    private void getMyCard() {
        cardId = -1;
        cardName = "";
        discountText = "";
        tvFosterorderconfirmLpk.setTextColor(getResources().getColor(R.color.aBBBBBB));
        Utils.setText(tvFosterorderconfirmLpk, "无可用", "", View.VISIBLE, View.VISIBLE);
        mPDialog.showDialog();
        cardList.clear();
        CommUtil.serviceCardList(this, 0, 1, payPrice, Global.ORDERKEY[3], appointment, cardListHandler);
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
                                    cardList.add(myCard);
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
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
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
        }
    };

    private void confirmOrderPromptNew() {
        thirdPrice = 0;
        normalCouponDiscountPrice = 0;
        cardPayPrice = 0;
        cardDiscountPrice = 0;
        payPrice = 0;
        bathPayPrice = 0;
        fosterPayPrice = 0;
        localDiscountList.clear();
        mPDialog.showDialog();
        CommUtil.getFosterOrderConfirmation(this, careShopId, roomType, mypetId, shopId, startTime, endTime, strp, extraPetIds, couponId, cardId, confirmOrderPromptHandler);
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
                        if (objectData.has("payPrice") && !objectData.isNull("payPrice")) {
                            payPrice = objectData.getDouble("payPrice");
                        }
                        if (objectData.has("cardPayPrice") && !objectData.isNull("cardPayPrice")) {
                            cardPayPrice = objectData.getDouble("cardPayPrice");
                        }
                        if (objectData.has("thirdPrice") && !objectData.isNull("thirdPrice")) {
                            thirdPrice = objectData.getDouble("thirdPrice");
                        }
                        if (objectData.has("couponAmount") && !objectData.isNull("couponAmount")) {
                            normalCouponDiscountPrice = objectData.getDouble("couponAmount");
                        }
                        if (objectData.has("discountPrice") && !objectData.isNull("discountPrice")) {
                            cardDiscountPrice = objectData.getDouble("discountPrice");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (petList.size() > 0) {
                for (int i = 0; i < petList.size(); i++) {
                    bathPayPrice = ComputeUtil.add(bathPayPrice, petList.get(i).getListBathFee());
                }
            }
            fosterPayPrice = ComputeUtil.sub(payPrice, bathPayPrice);
            Utils.mLogError("fosterPayPrice"+fosterPayPrice);
            if (cardId > 0 && cardPayPrice <= 0 && couponId > 0) {
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
                tvFosterorderconfirmYhq.setText("-" + normalCouponDiscountPrice + "(" + couponName + ")");
                tvFosterorderconfirmYhq.setTextColor(getResources().getColor(R.color.aD0021B));
            } else {
                couponName = "";
                canUseServiceCard = 0;
            }
            if (cardId > 0) {
                tvFosterorderconfirmLpk.setTextColor(getResources().getColor(R.color.aD0021B));
                if (Utils.isStrNull(strp)) {//有离店洗美
                    Utils.setText(tvFosterorderconfirmLpk, cardName + (Utils.isStrNull(discountText) ? "(" + discountText.replace("@@", "") + ")" : ""), "", View.VISIBLE, View.VISIBLE);
                } else {
                    Utils.setText(tvFosterorderconfirmLpk, cardName, "", View.VISIBLE, View.VISIBLE);
                }
                if (cardPayPrice > 0) {
                    rlFosterorderconfirmCard.setVisibility(View.VISIBLE);
                    Utils.setText(tvFosterorderconfirmCardprice, "¥" + cardPayPrice, "", View.VISIBLE, View.VISIBLE);
                    Utils.setText(tvFosterorderconfirmCardnamePay, cardName + "支付" + (Utils.isStrNull(discountText) ? "(" + discountText.replace("@@", "") + ")" : ""), "", View.VISIBLE, View.VISIBLE);
                } else {
                    rlFosterorderconfirmCard.setVisibility(View.GONE);
                }
            } else {
                rlFosterorderconfirmCard.setVisibility(View.GONE);
                cardName = "";
                discountText = "";
            }
            if (normalCouponDiscountPrice > 0) {
                localDiscountList.add(new WashDiscount("", "", String.valueOf(normalCouponDiscountPrice), "2"));
            }
            if (cardDiscountPrice > 0) {
                localDiscountList.add(new WashDiscount("", "", String.valueOf(cardDiscountPrice), "1"));
            }
            washOrderDetailDiscountAdapter.notifyDataSetChanged();
            Utils.setText(tvFosterorderconfirmPrice, "¥" + thirdPrice, "¥0", View.VISIBLE, View.VISIBLE);

            //计算可用礼品卡数量
            if (listpayWays != null && listpayWays.length() > 0 && listpayWays.toString().contains("11") && cardId <= 0) {
                mPDialog.showDialog();
                cardSize = 0;
                CommUtil.serviceCardList(mContext, 0, 1, payPrice, Global.ORDERKEY[3], appointment, cardSizeHandler);
            }
            //计算可用优惠券数量
            if (couponId <= 0) {
                mPDialog.showDialog();
                fosterCouponSize = 0;
                fosterCouponSizeList.clear();
                CommUtil.getFosterAvailableCoupon(mContext, shopId, 2, mypetId, roomType, startTime, endTime, cardId, 7,
                        fosterPayPrice, fosterCouponSizeHanler);
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
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                e.printStackTrace();
            }
            if (cardSize > 0) {
                tvFosterorderconfirmLpk.setText(cardSize + "张可用");
                tvFosterorderconfirmLpk.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tvFosterorderconfirmLpk.setTextColor(getResources().getColor(R.color.aBBBBBB));
                Utils.setText(tvFosterorderconfirmLpk, "无可用", "", View.VISIBLE, View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private AsyncHttpResponseHandler fosterCouponSizeHanler = new AsyncHttpResponseHandler() {

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
                            fosterCouponSizeList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
            if (fosterCouponSizeList.size() > 0) {
                if (cardId > 0) {
                    if (fosterCouponSizeList.size() > 0) {
                        for (int i = 0; i < fosterCouponSizeList.size(); i++) {
                            if (fosterCouponSizeList.get(i).isAvali == 1 && fosterCouponSizeList.get(i).canUseServiceCard == 0) {
                                fosterCouponSize++;
                            }
                        }
                    }
                } else {
                    if (fosterCouponSizeList.size() > 0) {
                        for (int i = 0; i < fosterCouponSizeList.size(); i++) {
                            if (fosterCouponSizeList.get(i).isAvali == 1) {
                                fosterCouponSize++;
                            }
                        }
                    }
                }
            }
            couponSize = fosterCouponSize;
            if (Utils.isStrNull(strp)) {//请求服务优惠券
                mPDialog.showDialog();
                serviceCouponSize = 0;
                serviceCouponSizeList.clear();
                CommUtil.getAvailableCoupon(mContext, spUtil.getString("cellphone", ""), Global.getIMEI(mContext),
                        Global.getCurrentVersion(mContext), appointment, 1, 1,
                        0, 0, "", "", 0, "", 0, 0,
                        strp, 0, bathPayPrice, careShopId,
                        null, 1, cardId, 0, 4, serviceCouponSizeHanler);
            } else {
                if (couponSize > 0) {
                    tvFosterorderconfirmYhq.setText(couponSize + "张可用");
                    tvFosterorderconfirmYhq.setTextColor(getResources().getColor(R.color.a666666));
                } else {
                    tvFosterorderconfirmYhq.setText("无可用");
                    tvFosterorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler serviceCouponSizeHanler = new AsyncHttpResponseHandler() {

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
                            serviceCouponSizeList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (serviceCouponSizeList.size() > 0) {
                if (cardId > 0) {
                    if (serviceCouponSizeList.size() > 0) {
                        for (int i = 0; i < serviceCouponSizeList.size(); i++) {
                            if (serviceCouponSizeList.get(i).isAvali == 1 && serviceCouponSizeList.get(i).canUseServiceCard == 0) {
                                serviceCouponSize++;
                            }
                        }
                    }
                } else {
                    if (serviceCouponSizeList.size() > 0) {
                        for (int i = 0; i < serviceCouponSizeList.size(); i++) {
                            if (serviceCouponSizeList.get(i).isAvali == 1) {
                                serviceCouponSize++;
                            }
                        }
                    }
                }
            }
            couponSize = fosterCouponSize + serviceCouponSize;
            if (couponSize > 0) {
                tvFosterorderconfirmYhq.setText(couponSize + "张可用");
                tvFosterorderconfirmYhq.setTextColor(getResources().getColor(R.color.a666666));
            } else {
                tvFosterorderconfirmYhq.setText("无可用");
                tvFosterorderconfirmYhq.setTextColor(getResources().getColor(R.color.aBBBBBB));
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

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
                    Intent intent = new Intent(mContext, FosterOrderDetailNewActivity.class);
                    intent.putExtra("orderid", orderNo);
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
                    ToastUtil.showToastShortCenter(FosterOrderConfirmActivity.this,
                            msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
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
            Global.ServerEvent(FosterOrderConfirmActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_orderpay_back);
            goBack();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 新寄养二次支付
     */
    private void hotelPay() {
        mPDialog.showDialog();
        CommUtil.hotelPay(mContext, careShopId, orderNo, shopId, 2, mypetId, strp, roomType, couponId, startTime, endTime, cardId, payPrice,
                paytype, tvFosterorderconfirmRemark.getText().toString().trim(), contact, contactPhone, extraPetIds, hotelPayHanler);
    }

    private AsyncHttpResponseHandler hotelPayHanler = new AsyncHttpResponseHandler() {
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
                    ToastUtil.showToastShort(FosterOrderConfirmActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
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
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
            setPayLoadFail();
            if (pWinBottomDialog != null && pWinBottomDialog.isShowing()) {
                pWinBottomDialog.dismiss();
            }
        }
    };

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(FosterOrderConfirmActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(FosterOrderConfirmActivity.this, R.layout.appoint_pay_bottom_dialog, null);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(FosterOrderConfirmActivity.this)[0]);
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
                hotelPay();
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
            spUtil.saveInt("payway", 1);
            mPDialog.showDialog();
            PayUtils.weChatPayment(FosterOrderConfirmActivity.this, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(FosterOrderConfirmActivity.this, aliHandler);
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
                                startActivityForResult(new Intent(FosterOrderConfirmActivity.this, SetFingerprintActivity.class).putExtra("flag",1), Global.ORDERPAY_TO_STARTFINGER);
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
        intent.putExtra("payPrice", payPrice);
        intent.putExtra("orderId", orderNo);
        intent.putExtra("type", 2);
        intent.putExtra("pageType", 5);
        intent.putExtra("myself", myselfDummpNum);
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
                    hotelPay();
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
                ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请求失败");
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
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(FosterOrderConfirmActivity.this, R.anim.commodity_detail_show));//开始动画
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
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
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
            hotelPay();
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
        Log.e("TAG", "android.os.Build.MODEL = " + Build.MODEL);
        Log.e("TAG", "android.os.Build.VERSION.RELEASE = " + Build.VERSION.RELEASE);
        Log.e("TAG", "Global.WXPAYCODE = " + Global.WXPAYCODE);
        if (Global.WXPAYCODE == 0) {
            if ((Build.MODEL.equals("OPPO R9m") || Build.MODEL.equals("OPPO R9s")) && Build.VERSION.RELEASE.equals("5.1")) {
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
                Intent intent = new Intent(mContext, FosterOrderDetailNewActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.ORDERDETAILREQUESTCODE_COUPON) {//选择优惠券回来
                couponIndex = data.getIntExtra("couponIndex", 0);
                couponId = data.getIntExtra("couponid", 0);
                couponName = data.getStringExtra("name");
                canUseServiceCard = data.getIntExtra("canUseServiceCard", 0);
                flag = 1;
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
                Utils.setText(tvFosterorderconfirmRemark, data.getStringExtra("note"), "", View.VISIBLE, View.VISIBLE);
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

    @OnClick({R.id.ib_titlebar_back, R.id.iv_fosterorderconfirm_call, R.id.iv_fosterorderconfirm_call1, R.id.iv_fosterorderconfirm_gotop, R.id.btn_fosterorderconfirm_submit, R.id.rl_fosterorderconfirm_lxr, R.id.rl_fosterorderconfirm_remark, R.id.rl_fosterorderconfirm_lpk, R.id.rl_fosterorderconfirm_yhq, R.id.ll_fosterorderconfirm_select, R.id.tv_fosterorderconfirm_xy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                goBack();
                break;
            case R.id.iv_fosterorderconfirm_call:
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.telePhoneBroadcast(mContext, servicePhone);
                    }
                }).show();
                break;
            case R.id.iv_fosterorderconfirm_call1:
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.telePhoneBroadcast(mContext, servicePhone);
                    }
                }).show();
                break;
            case R.id.iv_fosterorderconfirm_gotop:
                nsvFosterorderconfirmNum.fullScroll(NestedScrollView.FOCUS_UP);
                break;
            case R.id.btn_fosterorderconfirm_submit:
                if (!Utils.isStrNull(contact)) {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请填写联系人");
                    return;
                }
                if (!Utils.isStrNull(contactPhone)) {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请填写联系方式");
                    return;
                }
                if (!isSelect) {
                    ToastUtil.showToastShortCenter(this, "请同意《宠物家寄养协议》");
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
                            /*boolean isNextSetPayPwd = spUtil.getBoolean("isNextSetPayPwd", false);
                            if (isNextSetPayPwd) {
                                hotelPay();
                            } else {
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        spUtil.saveBoolean("isNextSetPayPwd", true);
                                        hotelPay();
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
                        hotelPay();
                    }
                } else {
                    if (listpayWays != null && listpayWays.length() > 0) {
                        if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                            showPayDialog();
                        }
                    }
                }
                break;
            case R.id.rl_fosterorderconfirm_lxr:
                showContactDialog();
                break;
            case R.id.rl_fosterorderconfirm_remark:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Order_AddRemarks);
                Intent intent2 = new Intent(this, NoteNewActivity.class);
                intent2.putExtra("remark", tvFosterorderconfirmRemark.getText().toString().trim());
                startActivityForResult(intent2, Global.ORDERDETAILREQUESTCODE_NOTE);
                break;
            case R.id.rl_fosterorderconfirm_lpk:
                Intent intent = new Intent(mContext, SelectMyCardActivity.class);
                intent.putExtra("id", cardId);
                intent.putExtra("type", 1);
                intent.putExtra("orderKey", Global.ORDERKEY[3]);
                intent.putExtra("payPrice", payPrice);
                intent.putExtra("flag", 2);
                intent.putExtra("careShopId", careShopId);
                intent.putExtra("roomType", roomType);
                intent.putExtra("mypetId", mypetId);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                intent.putExtra("appointment", appointment);
                intent.putExtra("strp", strp);
                intent.putExtra("shopId", shopId);
                intent.putExtra("couponId", couponId);
                intent.putExtra("extraPetIds", extraPetIds);
                intent.putExtra("canUseServiceCard", canUseServiceCard);
                startActivityForResult(intent, Global.FOSORDERCONFIRM_TO_MYCARD);
                break;
            case R.id.rl_fosterorderconfirm_yhq:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Order_SelectCoupon);
                mPDialog.showDialog();
                fosterCouponList.clear();
                CommUtil.getFosterAvailableCoupon(this, shopId, 2, mypetId, roomType, startTime, endTime, cardId, 7,
                        firstPayPrice, localFosterCouponHanler);
                break;
            case R.id.ll_fosterorderconfirm_select:
                isSelect = !isSelect;
                if (isSelect) {
                    ivFosterorderconfirmSelect.setImageResource(R.drawable.icon_petadd_select);
                } else {
                    ivFosterorderconfirmSelect.setImageResource(R.drawable.icon_petadd_unselect);
                }
                break;
            case R.id.tv_fosterorderconfirm_xy:
                startActivity(new Intent(FosterOrderConfirmActivity.this, ADActivity.class).putExtra("url", agreementPage));
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

    private AsyncHttpResponseHandler localFosterCouponHanler = new AsyncHttpResponseHandler() {

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
                            fosterCouponList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (Utils.isStrNull(strp)) {//请求服务优惠券
                mPDialog.showDialog();
                serviceCouponList.clear();
                CommUtil.getAvailableCoupon(mContext, spUtil.getString("cellphone", ""), Global.getIMEI(mContext),
                        Global.getCurrentVersion(mContext), appointment, 1, 1,
                        0, 0, "", "", 0, "", 0, 0,
                        strp, 0, bathPayPrice, careShopId,
                        null, 1, cardId, 0, 4, localServiceCouponHanler);
            } else {
                Intent intent3 = new Intent(FosterOrderConfirmActivity.this, SelectFosterCouponActivity.class);
                intent3.putExtra("couponId", couponId);
                if (serviceCouponList.size() > 0) {
                    intent3.putExtra("serviceCouponList", (Serializable) serviceCouponList);
                }
                if (fosterCouponList.size() > 0) {
                    intent3.putExtra("fosterCouponList", (Serializable) fosterCouponList);
                }
                intent3.putExtra("couponIndex", couponIndex);
                intent3.putExtra("strp", strp);
                startActivityForResult(intent3, Global.ORDERDETAILREQUESTCODE_COUPON);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private AsyncHttpResponseHandler localServiceCouponHanler = new AsyncHttpResponseHandler() {

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
                            serviceCouponList.add(Coupon.jsonToEntity(array.getJSONObject(i)));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            Intent intent3 = new Intent(FosterOrderConfirmActivity.this, SelectFosterCouponActivity.class);
            intent3.putExtra("couponId", couponId);
            if (serviceCouponList.size() > 0) {
                intent3.putExtra("serviceCouponList", (Serializable) serviceCouponList);
            }
            if (fosterCouponList.size() > 0) {
                intent3.putExtra("fosterCouponList", (Serializable) fosterCouponList);
            }
            intent3.putExtra("couponIndex", couponIndex);
            intent3.putExtra("strp", strp);
            startActivityForResult(intent3, Global.ORDERDETAILREQUESTCODE_COUPON);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void showContactDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(FosterOrderConfirmActivity.this, R.layout.foster_contact_dialog, null);
        TextView tv_fostercontactdialog_wc = (TextView) customView.findViewById(R.id.tv_fostercontactdialog_wc);
        final EditText et_fostercontactdialog_lxr = (EditText) customView.findViewById(R.id.et_fostercontactdialog_lxr);
        final EditText et_fostercontactdialog_lxfs = (EditText) customView.findViewById(R.id.et_fostercontactdialog_lxfs);
        RelativeLayout rl_fostercontactdialog_parent = (RelativeLayout) customView.findViewById(R.id.rl_fostercontactdialog_parent);
        final PopupWindow popupWindow = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setWidth(Utils.getDisplayMetrics(FosterOrderConfirmActivity.this)[0]);
        popupWindow.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        et_fostercontactdialog_lxr.setText(contact);
        et_fostercontactdialog_lxfs.setText(contactPhone);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (Utils.isStrNull(Utils.checkEditText(et_fostercontactdialog_lxr))) {
                    contact = Utils.checkEditText(et_fostercontactdialog_lxr);
                }
                if (Utils.isStrNull(Utils.checkEditText(et_fostercontactdialog_lxfs))) {
                    contactPhone = Utils.checkEditText(et_fostercontactdialog_lxfs);
                }
                String str = (Utils.isStrNull(contact) ? contact : "") + " " + (Utils.isStrNull(contactPhone) ? contactPhone : "");
                tvFosterorderconfirmLxr.setText(str.trim());
            }
        });
        tv_fostercontactdialog_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isStrNull(Utils.checkEditText(et_fostercontactdialog_lxr))) {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请填写联系人");
                    return;
                }
                if (!Utils.isStrNull(Utils.checkEditText(et_fostercontactdialog_lxfs))) {
                    ToastUtil.showToastShortBottom(FosterOrderConfirmActivity.this, "请填写联系方式");
                    return;
                }
                contact = Utils.checkEditText(et_fostercontactdialog_lxr);
                contactPhone = Utils.checkEditText(et_fostercontactdialog_lxfs);
                tvFosterorderconfirmLxr.setText(contact + " " + contactPhone);
                popupWindow.dismiss();
            }
        });
        rl_fostercontactdialog_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}
