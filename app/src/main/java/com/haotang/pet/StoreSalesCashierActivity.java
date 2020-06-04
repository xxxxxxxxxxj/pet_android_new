package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.RecyclerView;
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
import com.haotang.pet.adapter.OrderPayScanInfoAdapter;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.entity.OrderPayScanInfo;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.AlertDialogDefault;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店销收银台
 */
public class StoreSalesCashierActivity extends SuperActivity {
    private static final int STORESALESCASHIER_TO_MYCARD = 1111;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_store_sales_price)
    TextView tvStoreSalesPrice;
    @BindView(R.id.tv_store_sales_totalprice)
    TextView tvStoreSalesTotalprice;
    @BindView(R.id.rv_store_sales_goods)
    RecyclerView rvStoreSalesGoods;
    @BindView(R.id.tv_store_sales_lpk)
    TextView tvStoreSalesLpk;
    @BindView(R.id.ll_store_sales_lpk)
    LinearLayout llStoreSalesLpk;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    private String codeResult;
    private StringBuilder listpayWays = new StringBuilder();
    private List<OrderPayScanInfo> listItems = new ArrayList<OrderPayScanInfo>();
    private double totalPayPrice;
    private String remark;
    private String goods;
    public int shopId;
    private long orderId;
    private int paytype = -1;
    private OrderPayScanInfoAdapter orderPayScanInfoAdapter;
    private int cardId = -1;
    private List<MyCard> list = new ArrayList<MyCard>();
    private double selectCardAmount;
    private String cardName;
    private double needpayfee;
    private int oldpayway;
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
    public int isVip;
    public String compare_desc;
    public int myselfDummpNum;

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    if (TextUtils.equals(resultStatus, "9000")) {
                        goPayResult();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(StoreSalesCashierActivity.this, orderStr,
                            aliHandler, mPDialog);
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
                    if(Utils.isStrNull(resp.errStr)){
                        ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, resp.errStr);
                    }else{
                        ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "支付失败");
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
        getShopInfo();
        getBalance();
    }

    private void initData() {
        Global.WXPAYCODE = -1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        codeResult = getIntent().getStringExtra("codeResult");
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        oldpayway = spUtil.getInt("payway", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_store_sales_cashier);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("收银台");
        rvStoreSalesGoods.setHasFixedSize(true);
        rvStoreSalesGoods.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvStoreSalesGoods.setLayoutManager(noScollFullLinearLayoutManager);
        orderPayScanInfoAdapter = new OrderPayScanInfoAdapter(R.layout.item_scanshopinfo, listItems);
        rvStoreSalesGoods.setAdapter(orderPayScanInfoAdapter);
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
                    ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "请求失败");
        }
    };

    private void getPayWay() {
        mPDialog.showDialog();
        listpayWays.setLength(0);
        CommUtil.payWays(this, Global.ORDERKEY[4], 0, payWaysHandler);
    }

    private void getShopInfo() {
        listItems.clear();
        mPDialog.showDialog();
        CommUtil.ScanCodePayment(codeResult, this,
                spUtil.getString("cellphone", ""), ScanCodePaymentHandler);
    }

    private AsyncHttpResponseHandler ScanCodePaymentHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                String msg = object.getString("msg");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject jdata = object.getJSONObject("data");
                        if (jdata.has("items") && !jdata.isNull("items")) {
                            JSONArray items = jdata.getJSONArray("items");
                            if (items.length() > 0) {
                                for (int i = 0; i < items.length(); i++) {
                                    listItems.add(OrderPayScanInfo
                                            .json2Entity(items.getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("totalPayPrice")
                                && !jdata.isNull("totalPayPrice")) {
                            totalPayPrice = jdata.getDouble("totalPayPrice");
                            SpannableString ss = new SpannableString("¥" + totalPayPrice);
                            ss.setSpan(new TextAppearanceSpan(
                                            StoreSalesCashierActivity.this, R.style.sanshi_normal), 1, ss.length(),
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            tvStoreSalesTotalprice.setText(ss);
                        }
                        if (jdata.has("goods") && !jdata.isNull("goods")) {
                            goods = jdata.getString("goods");
                        }
                        if (jdata.has("shopId")
                                && !jdata.isNull("shopId")) {
                            shopId = jdata.getInt("shopId");
                        }
                        if (jdata.has("remark")
                                && !jdata.isNull("remark")) {
                            remark = jdata.getString("remark");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "数据异常");
            }
            orderPayScanInfoAdapter.notifyDataSetChanged();
            getPayWay();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "请求失败");
        }
    };

    private void getMyCard() {
        cardId = -1;
        selectCardAmount = 0;
        cardName = "";
        mPDialog.showDialog();
        list.clear();
        CommUtil.serviceCardList(this, 0, 2, totalPayPrice, Global.ORDERKEY[4],"", cardListHandler);
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
                                    list.add(myCard);
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "数据异常");
                e.printStackTrace();
            }
            if (list != null && list.size() > 0) {
                selectCardAmount = list.get(0).getAmount();
                cardId = list.get(0).getId();
                cardName = list.get(0).getCardTypeName();
                tvStoreSalesLpk.setTextColor(getResources().getColor(R.color.aD0021B));
                Utils.setText(tvStoreSalesLpk, cardName + "(余额¥" + selectCardAmount + ")", "", View.VISIBLE, View.VISIBLE);
                needpayfee = ComputeUtil.sub(totalPayPrice, selectCardAmount);
                needpayfee = needpayfee < 0 ? 0 : needpayfee;
                Utils.setText(tvStoreSalesPrice, "¥" + needpayfee, "", View.VISIBLE, View.VISIBLE);
                if (needpayfee <= 0) {
                    paytype = 11;
                }
            } else {
                cardName = "无可用E卡";
                selectCardAmount = 0;
                tvStoreSalesLpk.setTextColor(getResources().getColor(R.color.aBBBBBB));
                Utils.setText(tvStoreSalesLpk, cardName, "", View.VISIBLE, View.VISIBLE);
                needpayfee = ComputeUtil.sub(totalPayPrice, selectCardAmount);
                needpayfee = needpayfee < 0 ? 0 : needpayfee;
                Utils.setText(tvStoreSalesPrice, "¥" + needpayfee, "", View.VISIBLE, View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "请求失败");
        }
    };

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
                    ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "数据异常");
            }
            if (listpayWays != null && listpayWays.length() > 0) {
                if (listpayWays.toString().contains("11")) {// E卡
                    llStoreSalesLpk.setVisibility(View.VISIBLE);
                    getMyCard();
                } else {
                    llStoreSalesLpk.setVisibility(View.GONE);
                    cardId = -1;
                    cardName = "无可用E卡";
                    selectCardAmount = 0;
                    tvStoreSalesLpk.setTextColor(getResources().getColor(R.color.aBBBBBB));
                    Utils.setText(tvStoreSalesLpk, cardName, "", View.VISIBLE, View.VISIBLE);
                    needpayfee = ComputeUtil.sub(totalPayPrice, selectCardAmount);
                    needpayfee = needpayfee < 0 ? 0 : needpayfee;
                    Utils.setText(tvStoreSalesPrice, "¥" + needpayfee, "", View.VISIBLE, View.VISIBLE);
                }
            } else {
                llStoreSalesLpk.setVisibility(View.GONE);
                cardId = -1;
                cardName = "无可用E卡";
                selectCardAmount = 0;
                tvStoreSalesLpk.setTextColor(getResources().getColor(R.color.aBBBBBB));
                Utils.setText(tvStoreSalesLpk, cardName, "", View.VISIBLE, View.VISIBLE);
                needpayfee = ComputeUtil.sub(totalPayPrice, selectCardAmount);
                needpayfee = needpayfee < 0 ? 0 : needpayfee;
                Utils.setText(tvStoreSalesPrice, "¥" + needpayfee, "", View.VISIBLE, View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "请求失败");
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

    @OnClick({R.id.ib_titlebar_back, R.id.btn_store_sales_submit, R.id.ll_store_sales_lpk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_store_sales_submit:
                if (needpayfee > 0) {
                    if (listpayWays != null && listpayWays.length() > 0) {
                        if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                            showPayDialog();
                        }
                    }
                } else {
                    //判断是否设置支付密码
                    if (payPwd == 0) {//未设置过支付密码
                        boolean isNextSetPayPwd = spUtil.getBoolean("isNextSetPayPwd", false);
                        if (isNextSetPayPwd) {
                            ScanCodePay();
                        } else {
                            new AlertDialogDefault(mContext).builder()
                                    .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    spUtil.saveBoolean("isNextSetPayPwd", true);
                                    ScanCodePay();
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
                break;
            case R.id.ll_store_sales_lpk:
                Intent intent = new Intent(StoreSalesCashierActivity.this, SelectMyCardActivity.class);
                intent.putExtra("id", cardId);
                intent.putExtra("type", 2);
                intent.putExtra("orderKey", Global.ORDERKEY[4]);
                intent.putExtra("payPrice", totalPayPrice);
                startActivityForResult(intent, STORESALESCASHIER_TO_MYCARD);
                break;
        }
    }

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(StoreSalesCashierActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(StoreSalesCashierActivity.this, R.layout.appoint_pay_bottom_dialog, null);
        Button btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        LinearLayout ll_pay_bottomdia_time = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_time);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        final ImageView iv_pay_bottomdia_zfb_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_zfb_select);
        btn_pay_bottomdia.setText("确认支付¥" + needpayfee);
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
        ll_pay_bottomdia_time.setVisibility(View.GONE);
        tv_pay_title.setText("请选择支付方式");
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(StoreSalesCashierActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
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
                pWinBottomDialog.dismiss();
                ScanCodePay();
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

    private void ScanCodePay() {
        mPDialog.showDialog();
        CommUtil.ScanCodePay(remark, orderId, false,
                totalPayPrice, 0, paytype, goods,
                shopId, StoreSalesCashierActivity.this,
                spUtil.getString("cellphone", ""), cardId,
                ScanCodePayHandler);
    }

    private AsyncHttpResponseHandler ScanCodePayHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("give_can") && !objectData.isNull("give_can")) {
                            JSONObject obGiveCan = objectData.getJSONObject("give_can");
                            if (obGiveCan.has("myself") && !obGiveCan.isNull("myself")) {
                                myselfDummpNum = obGiveCan.getInt("myself");
                            }
                            if (obGiveCan.has("compare_desc") && !obGiveCan.isNull("compare_desc")) {
                                compare_desc = obGiveCan.getString("compare_desc");
                            }
                        }
                        if (objectData.has("isVip") && !objectData.isNull("isVip")) {
                            isVip = objectData.getInt("isVip");
                        }
                        if (objectData.has("orderId")
                                && !objectData.isNull("orderId")) {
                            orderId = objectData.getLong("orderId");
                        }
                        if (objectData.has("payInfo") && !objectData.isNull("payInfo")) {
                            JSONObject jpayInfo = objectData.getJSONObject("payInfo");
                            if (jpayInfo.has("appid") && !jpayInfo.isNull("appid")) {
                                appid = jpayInfo.getString("appid");
                            }
                            if (jpayInfo.has("noncestr")
                                    && !jpayInfo.isNull("noncestr")) {
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
                    if (needpayfee <= 0) {
                        ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "支付成功");
                        // 直接跳转到支付成功
                        goPayResult();
                    } else {
                        // 第三方支付
                        goPay();
                    }
                } else {
                    if (code == Global.PAY_SUCCESS) {
                        goPayResult();
                    } else {
                        setPayLoadFail();
                    }
                    if (object.has("msg") && !object.isNull("msg")) {
                        String msg = object.getString("msg");
                        ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "数据异常");
                setPayLoadFail();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            setPayLoadFail();
            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "请求失败");
        }
    };

    private void goPay() {
        if (paytype == 1) {// 微信支付
            mPDialog.showDialog();
            spUtil.saveInt("payway", 1);
            PayUtils.weChatPayment(StoreSalesCashierActivity.this, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(StoreSalesCashierActivity.this, aliHandler);
        }
    }

    private void goPayResult() {
        if (needpayfee <= 0 && pWinPayPwdSystem != null) {
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
                            if (mFingerprintCore.isSupport()) {
                                spUtil.saveBoolean("isFirstSetPwd", true);
                                startActivityForResult(new Intent(StoreSalesCashierActivity.this, SetFingerprintActivity.class).putExtra("flag",1), Global.ORDERPAY_TO_STARTFINGER);
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
        Intent intent = new Intent(this, PaySuccessActivity.class);
        intent.putExtra("previous", Global.MIPCA_TO_ORDERPAY);
        //显示获得多少罐头币
        intent.putExtra("compare_desc", compare_desc);
        intent.putExtra("myself", myselfDummpNum);
        intent.putExtra("isVip", isVip);
        intent.putExtra("listItems", (Serializable) listItems);
        intent.putExtra("totalPayPrice", totalPayPrice);
        startActivity(intent);
        finish();
    }

    private void setPayLoadFail() {
        if (needpayfee <= 0 && pWinPayPwdSystem != null && pWinPayPwdSystem.isShowing()) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == STORESALESCASHIER_TO_MYCARD) {//选择E卡回来
                cardName = data.getStringExtra("cardTypeName");
                cardId = data.getIntExtra("id", -1);
                selectCardAmount = data.getDoubleExtra("amount", 0);
                if (cardId > 0) {
                    tvStoreSalesLpk.setTextColor(getResources().getColor(R.color.aD0021B));
                    Utils.setText(tvStoreSalesLpk, cardName + "(余额¥" + selectCardAmount + ")", "", View.VISIBLE, View.VISIBLE);
                    needpayfee = ComputeUtil.sub(totalPayPrice, selectCardAmount);
                    needpayfee = needpayfee < 0 ? 0 : needpayfee;
                    Utils.setText(tvStoreSalesPrice, "¥" + needpayfee, "", View.VISIBLE, View.VISIBLE);
                    if (needpayfee <= 0) {
                        paytype = 11;
                    }
                } else {
                    cardName = "不使用E卡";
                    selectCardAmount = 0;
                    tvStoreSalesLpk.setTextColor(getResources().getColor(R.color.aBBBBBB));
                    Utils.setText(tvStoreSalesLpk, cardName, "", View.VISIBLE, View.VISIBLE);
                    needpayfee = ComputeUtil.sub(totalPayPrice, selectCardAmount);
                    needpayfee = needpayfee < 0 ? 0 : needpayfee;
                    Utils.setText(tvStoreSalesPrice, "¥" + needpayfee, "", View.VISIBLE, View.VISIBLE);
                }
            }else if (requestCode == Global.ORDERPAY_TO_STARTFINGER) {
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
                    ScanCodePay();
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
                ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(StoreSalesCashierActivity.this, "请求失败");
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
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(StoreSalesCashierActivity.this, R.anim.commodity_detail_show));//开始动画
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
            tv_paypwdsystem_pop_payprice.setText("¥" + totalPayPrice);
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
    public void onDestroy() {
        super.onDestroy();
        mFingerprintCore.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
