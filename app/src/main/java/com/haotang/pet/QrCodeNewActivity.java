package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SelectCardAdapter;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.AlertDialogOpenPayPwd;
import com.haotang.pet.view.DividerLinearItemDecoration;
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
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * 付款二维码页面
 */
public class QrCodeNewActivity extends SuperActivity {
    @BindView(R.id.iv_qrcodenew_img)
    ImageView ivQrcodenewImg;
    @BindView(R.id.tv_qrcodenew_username)
    TextView tvQrcodenewUsername;
    @BindView(R.id.iv_rqcodenew_cardimg)
    ImageView ivRqcodenewCardimg;
    @BindView(R.id.tv_rqcodenew_cardname)
    TextView tvRqcodenewCardname;
    @BindView(R.id.tv_rqcodenew_carddesc)
    TextView tvRqcodenewCarddesc;
    @BindView(R.id.iv_rqcodenew)
    ImageView ivRqcodenew;
    @BindView(R.id.tv_rqcodenew_fxprice)
    TextView tv_rqcodenew_fxprice;
    @BindView(R.id.tv_rqcodenew_yhqnum)
    TextView tv_rqcodenew_yhqnum;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    private List<MyCard> list = new ArrayList<MyCard>();
    private String authCode;
    private int payPwd;
    private FingerprintCore mFingerprintCore;
    private PopupWindow popWinZhiWen;
    private PopupWindow popWinZhiWen1;
    private PopupWindow popWinZhiWen2;
    private PopupWindow pWinPayPwdSystem;
    private ImageView iv_paypwdsystem_pop_close;
    private TextView tv_paypwdsystem_pop_pwdtype;
    private TextView tv_paypwdsystem_pop_title;
    private RelativeLayout rl_paypwdsystem_pop_select;
    private Button btn_paypwdsystem_pop;
    private RelativeLayout rl_paypwdsystem_pop_pwd;
    private RelativeLayout rl_paypwdsystem_pwd;
    private CodeView cv_paypwdsystem_pop_pwd;
    private TextView tv_paypwdsystem_pop_wjmm;
    private TextView tv_paypwdsystem_pop_pwderror;
    private KeyboardView kbv_paypwdsystem_pop;
    private int pwdType = 0;
    private LinearLayout ll_paypwdsystem_pricedesc;
    private LinearLayout ll_paypwdsystem_erweima;
    private AlertDialogOpenPayPwd alertDialogOpenPayPwd;
    private int fingerNum;

    //支付密码设置结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetPayPwdSuccessEvent event) {
//        Utils.mLogError("支付密码设置结果返回 "+event.isSuccess());
        if (event != null && event.isSuccess()) {
            if (alertDialogOpenPayPwd != null) {
                alertDialogOpenPayPwd.dismiss();
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

    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_qr_code_new);
        ButterKnife.bind(this);
    }

    private void setView() {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        String maskNumber = spUtil.getString("cellphone", "").substring(0, 3)
                + "****" + spUtil.getString("cellphone", "")
                .substring(7, spUtil.getString("cellphone", "").length());
        Utils.setText(tvQrcodenewUsername, maskNumber, "", View.VISIBLE, View.VISIBLE);

    }

    private void setLinster() {
    }

    private void getData() {
        mPDialog.showDialog();
        list.clear();
        CommUtil.loginAuto(this, spUtil.getString("cellphone", ""),
                spUtil.getString("IMEI", ""), Global.getCurrentVersion(this),
                spUtil.getInt("userid", 0), 0, 0, autoLoginHandler);
        CommUtil.serviceCardList(this, 0, 4, 0, "", "", cardListHandler);
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("user") && !jData.isNull("user")) {
                            JSONObject jUser = jData.getJSONObject("user");
                            if (jUser.has("btlCouponCount") && !jUser.isNull("btlCouponCount")) {
                                Utils.setText(tv_rqcodenew_yhqnum, jUser.getInt("btlCouponCount") + "张可用", "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jUser.has("cashback") && !jUser.isNull("cashback")) {
                                Utils.setText(tv_rqcodenew_fxprice, "¥" + jUser.getDouble("cashback"), "", View.VISIBLE, View.VISIBLE);

                            }
                            if (jUser.has("avatar")
                                    && !jUser.isNull("avatar")) {
                                GlideUtil.loadCircleImg(mContext, jUser.getString("avatar"),
                                        ivQrcodenewImg,
                                        R.drawable.user_icon_unnet_circle);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

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
                            JSONArray jarrCard = jdata.getJSONArray("available");
                            if (jarrCard.length() > 0) {
                                for (int i = 0; i < jarrCard.length(); i++) {
                                    MyCard myCard = new MyCard();
                                    JSONObject javailable = jarrCard.getJSONObject(i);
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
                                getBanlance();
                            } else {
                                new AlertDialogNavAndPost(mContext).builder().setTitle("").setMsg("暂无可用E卡，无法使用二维码付款！")
                                        .setNegativeButtonVisible(View.GONE)
                                        .setPositiveButton("确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                            }
                                        }).show();
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(QrCodeNewActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(QrCodeNewActivity.this, "数据异常");
                e.printStackTrace();
            }
            setCardInfo(0, true);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(QrCodeNewActivity.this, "请求失败");
        }
    };

    private void getBanlance() {
        payPwd = 0;
        mPDialog.showDialog();
        CommUtil.getAccountBalance(this, spUtil.getString("cellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), getMoney);
    }

    private AsyncHttpResponseHandler getMoney = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("user") && !object.isNull("user")) {
                            JSONObject juser = object.getJSONObject("user");
                            if (juser.has("payPwd") && !juser.isNull("payPwd")) {
                                payPwd = juser.getInt("payPwd");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (payPwd == 0) {//未设置过支付密码
                alertDialogOpenPayPwd = new AlertDialogOpenPayPwd(mContext);
                alertDialogOpenPayPwd.builder().setCloseButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setOpenButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, CheckStatusActivity.class)
                                .putExtra("flag", Global.FIRST_SET_PASSWORD)
                                .putExtra("previous", Global.PAYPWD_TO_VERIFCODE)
                                .putExtra("isGoSetFinger", true));
                    }
                }).show();
            } else if (payPwd == 1) {//已设置过支付密码
                //判断十五分钟内是否验证过
                long curTime = System.currentTimeMillis();
                long check_pwd_code_time = spUtil.getLong("check_pwd_code_time", 0);
                long minute = (curTime - check_pwd_code_time) / 60000;
                Log.e("TAG", "curTime = " + curTime);
                Log.e("TAG", "check_pwd_code_time = " + check_pwd_code_time);
                Log.e("TAG", "minute = " + minute);
                if (check_pwd_code_time > 0 && minute <= 15) {//验证过且在15分钟内，无需验证

                } else { ///未验证过或者验证过但是超过了15分钟，需要验证
                    //判断设备是否录入指纹
                    boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                    //判断是否开启指纹支付
                    boolean isFinger = spUtil.getBoolean("isFinger", false);
                    if (isFinger && hasEnrolledFingerprints) {//智能验证
                        showPayPwdSystem(1, false);
                    } else {//密码验证
                        showPayPwdSystem(0, false);
                    }
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
            rl_paypwdsystem_pop_pwd = (RelativeLayout) customView.findViewById(R.id.rl_paypwdsystem_pop_pwd);
            rl_paypwdsystem_pwd = (RelativeLayout) customView.findViewById(R.id.rl_paypwdsystem_pwd);
            cv_paypwdsystem_pop_pwd = (CodeView) customView.findViewById(R.id.cv_paypwdsystem_pop_pwd);
            tv_paypwdsystem_pop_wjmm = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_wjmm);
            tv_paypwdsystem_pop_pwderror = (TextView) customView.findViewById(R.id.tv_paypwdsystem_pop_pwderror);
            kbv_paypwdsystem_pop = (KeyboardView) customView.findViewById(R.id.kbv_paypwdsystem_pop);
            ll_paypwdsystem_pricedesc = (LinearLayout) customView.findViewById(R.id.ll_paypwdsystem_pricedesc);
            ll_paypwdsystem_erweima = (LinearLayout) customView.findViewById(R.id.ll_paypwdsystem_erweima);
            pWinPayPwdSystem = new PopupWindow(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            pWinPayPwdSystem.setBackgroundDrawable(null);
            pWinPayPwdSystem.setContentView(customView);
            pWinPayPwdSystem.setFocusable(false);// 不应该获取焦点
            //点击外部消失
            pWinPayPwdSystem.setOutsideTouchable(false);
            //设置可以点击
            pWinPayPwdSystem.setTouchable(true);
            //进入退出的动画
            pWinPayPwdSystem.setAnimationStyle(R.style.mypopwindow_anim_style);
            pWinPayPwdSystem.setWidth(Utils.getDisplayMetrics(this)[0]);
            pWinPayPwdSystem.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rl_commodity_black.setVisibility(View.GONE);
                }
            });
            customView.setFocusable(true);
            customView.setFocusableInTouchMode(true);
            customView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //点击back键关闭弹窗，关闭activity
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        pWinPayPwdSystem.dismiss();
                        finish();
                        return true;
                    }
                    return false;
                }
            });

            pWinPayPwdSystem.showAtLocation(customView, Gravity.BOTTOM, 0, 0);

            ll_paypwdsystem_pricedesc.setVisibility(View.GONE);
            ll_paypwdsystem_erweima.setVisibility(View.VISIBLE);
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
                showPopZhiWen();
            }
            cv_paypwdsystem_pop_pwd.setShowType(CodeView.SHOW_TYPE_PASSWORD);
            cv_paypwdsystem_pop_pwd.setLength(6);
            kbv_paypwdsystem_pop.setCodeView(cv_paypwdsystem_pop_pwd);
            btn_paypwdsystem_pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pwdType == 0) {//密码验证
                        rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                        rl_paypwdsystem_pop_pwd.setVisibility(View.VISIBLE);
                        tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);
                    } else if (pwdType == 1) {//指纹验证
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
                    if (pwdType == 0) {//密码验证
                        pwdType = 1;
                        tv_paypwdsystem_pop_title.setText("请录入指纹验证");
                        cv_paypwdsystem_pop_pwd.clear();
                        btn_paypwdsystem_pop.setText("指纹验证");
                        tv_paypwdsystem_pop_pwdtype.setText("密码验证");
                        rl_paypwdsystem_pop_select.setVisibility(View.VISIBLE);
                        rl_paypwdsystem_pop_pwd.setVisibility(View.GONE);
                    } else if (pwdType == 1) {//指纹验证
                        pwdType = 0;
                        tv_paypwdsystem_pop_title.setText("请输入密码验证");
                        tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);
                        btn_paypwdsystem_pop.setText("密码验证");
                        tv_paypwdsystem_pop_pwdtype.setText("指纹验证");
                        rl_paypwdsystem_pop_select.setVisibility(View.GONE);
                        rl_paypwdsystem_pop_pwd.setVisibility(View.VISIBLE);
                    }
                }
            });
            iv_paypwdsystem_pop_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinPayPwdSystem.dismiss();
                    finish();
                }
            });
            cv_paypwdsystem_pop_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kbv_paypwdsystem_pop.show();
                }
            });
            //忘记密码
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
                    ToastUtil.showSuccessToast(mContext, "验证成功");
                    spUtil.saveLong("check_pwd_code_time", System.currentTimeMillis());
                    pWinPayPwdSystem.dismiss();
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

    private void showPopZhiWen() {
        popWinZhiWen = null;
        if (popWinZhiWen == null) {
            final View view = View
                    .inflate(this, R.layout.fingerprintdialog, null);
            popWinZhiWen = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            popWinZhiWen.setFocusable(false);
            popWinZhiWen.setOutsideTouchable(false);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        popWinZhiWen.dismiss();
                        finish();
                    }
                    return false;
                }
            });
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
            popWinZhiWen1.setFocusable(false);
            popWinZhiWen1.setOutsideTouchable(false);
            popWinZhiWen1.setWidth(Utils.getDisplayMetrics(this)[0]);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        popWinZhiWen1.dismiss();
                        finish();
                    }
                    return false;
                }
            });
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
            popWinZhiWen2.setFocusable(false);
            popWinZhiWen2.setOutsideTouchable(false);
            popWinZhiWen2.setWidth(Utils.getDisplayMetrics(this)[0]);
            popWinZhiWen2.showAtLocation(view, Gravity.CENTER, 0, 0);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        popWinZhiWen2.dismiss();
                        finish();
                    }
                    return false;
                }
            });
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

    @OnClick({R.id.iv_qrcodenew_close, R.id.rl_rqcodenew_more, R.id.iv_rqcodenew, R.id.rl_rqcodenew_fx, R.id.rl_rqcodenew_yhq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_rqcodenew_fx:
                startActivity(new Intent(this, CashbackAmountActivity.class));
                break;
            case R.id.rl_rqcodenew_yhq:
                startActivity(new Intent(this, MyCouponNewActivity.class));
                break;
            case R.id.iv_qrcodenew_close:
                finish();
                break;
            case R.id.rl_rqcodenew_more:
                showCardDialog();
                break;
            case R.id.iv_rqcodenew:
                startActivity(new Intent(this, QrCodeBigActivity.class).putExtra("authCode", authCode));
                break;
        }
    }

    private void showCardDialog() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(QrCodeNewActivity.this, R.layout.shop_bottom_dialog, null);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        final RecyclerView rv_appointpetmx_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpetmx_bottomdia);
        ImageView iv_appointpetmx_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg);
        ImageView iv_appointpetmx_bottom_bg_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg_close);
        TextView tv_appointpetmx_bottomdia_title = (TextView) customView.findViewById(R.id.tv_appointpetmx_bottomdia_title);
        TextView tv_appointpetmx_bottomdia_desc = (TextView) customView.findViewById(R.id.tv_appointpetmx_bottomdia_desc);
        RelativeLayout rl_appointpetmx_bottomdia = (RelativeLayout) customView.findViewById(R.id.rl_appointpetmx_bottomdia);
        LinearLayout ll_appointpetmx_bottomdia = (LinearLayout) customView.findViewById(R.id.ll_appointpetmx_bottomdia);
        tv_appointpetmx_bottomdia_title.setText("选择E卡");
        ll_appointpetmx_bottomdia.bringToFront();
        rv_appointpetmx_bottomdia.setHasFixedSize(true);
        rv_appointpetmx_bottomdia.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        rv_appointpetmx_bottomdia.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.aEEEEEE)));
        final SelectCardAdapter selectCardAdapter = new SelectCardAdapter(R.layout.item_select_card, list);
        rv_appointpetmx_bottomdia.setAdapter(selectCardAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_vertical));
        rv_appointpetmx_bottomdia.addItemDecoration(divider);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_appointpetmx_bottomdia.scrollToPosition(0);
            }
        }, 500);
        float screenDensity = ScreenUtil.getScreenDensity(this);
        Log.e("TAG", "screenDensity = " + screenDensity);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        selectCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setCardInfo(position, false);
                selectCardAdapter.notifyDataSetChanged();
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(QrCodeNewActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        iv_appointpetmx_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        rl_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        ll_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void setCardInfo(int position, boolean isDefault) {
        if (list != null && list.size() > 0 && list.size() > position) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelect(false);
            }
            MyCard myCard = list.get(position);
            if (myCard != null) {
                myCard.setSelect(true);
                GlideUtil.loadRoundImg(QrCodeNewActivity.this, myCard.getSmallPic(), ivRqcodenewCardimg, R.drawable.icon_production_default, 10);
                Utils.setText(tvRqcodenewCardname, myCard.getCardTypeName(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tvRqcodenewCarddesc, "余额 ¥ " + myCard.getAmount() + "  " + (Utils.isStrNull(myCard.getDiscountText()) ? myCard.getDiscountText().replace("@@", "") : ""), "", View.VISIBLE, View.VISIBLE);
                getAuthCode(myCard.getId());
            }
        }
    }

    private void getAuthCode(int serviceCardId) {
        mPDialog.showDialog();
        CommUtil.getauthCode(this, 11, serviceCardId, authCodeHandler);
    }

    private AsyncHttpResponseHandler authCodeHandler = new AsyncHttpResponseHandler() {

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
                        authCode = jsonObject.getString("data");
                        setAuthCode();
                    }
                } else {
                    ToastUtil.showToastShortBottom(QrCodeNewActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(QrCodeNewActivity.this, "数据异常");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(QrCodeNewActivity.this, "请求失败");
        }
    };

    private void setAuthCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bingoogolapple = QRCodeEncoder.syncEncodeQRCode(authCode,
                        BGAQRCodeUtil.dp2px(QrCodeNewActivity.this, 180), Color.BLACK, Color.WHITE,
                        BitmapFactory.decodeResource(QrCodeNewActivity.this.getResources(), R.drawable.erweima_logo));
                if (bingoogolapple != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivRqcodenew.setImageBitmap(bingoogolapple);
                        }
                    });
                }
            }
        }).start();
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFingerprintCore.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
