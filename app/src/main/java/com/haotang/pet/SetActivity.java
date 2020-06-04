package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.ExitLoginEvent;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.entity.UpdateUserPhoneEvent;
import com.haotang.pet.entity.WXLoginEvent;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.fingerprintrecognition.FingerprintUtil;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DataCleanUtils;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SetActivity extends SuperActivity {
    @BindView(R.id.ll_set_cellphone_right)
    LinearLayout llSetCellphoneRight;
    @BindView(R.id.rl_set_cellphone)
    RelativeLayout rlSetCellphone;
    @BindView(R.id.tv_set_wxcz)
    TextView tvSetWxcz;
    @BindView(R.id.tv_set_wxname)
    TextView tvSetWxname;
    @BindView(R.id.rl_set_zh)
    LinearLayout rlSetZh;
    @BindView(R.id.ll_set_mm_right)
    LinearLayout llSetMmRight;
    @BindView(R.id.rl_set_mm)
    RelativeLayout rlSetMm;
    @BindView(R.id.tv_set_zwopen)
    TextView tvSetZwopen;
    @BindView(R.id.ll_set_zw_right)
    LinearLayout llSetZwRight;
    @BindView(R.id.ll_set_zw)
    LinearLayout llSetZw;
    @BindView(R.id.iv_set_per)
    ImageView ivSetPer;
    @BindView(R.id.rl_set_per)
    RelativeLayout rlSetPer;
    @BindView(R.id.tv_set_cache)
    TextView tvSetCache;
    @BindView(R.id.ll_set_clear_right)
    LinearLayout llSetClearRight;
    @BindView(R.id.rl_set_clear)
    RelativeLayout rlSetClear;
    @BindView(R.id.tv_set_about_version)
    TextView tvSetAboutVersion;
    @BindView(R.id.ll_set_about_right)
    LinearLayout llSetAboutRight;
    @BindView(R.id.tv_set_exit)
    TextView tvSetExit;
    @BindView(R.id.tv_set_phone_qh)
    TextView tvSetPhoneQh;
    @BindView(R.id.tv_set_phone)
    TextView tvSetPhone;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    @BindView(R.id.nested_scroll)
    NestedScrollView nestedScroll;
    private FingerprintCore mFingerprintCore;
    private int payPwd = -1;
    private IWXAPI api;
    private String openId;
    private String wxNickname;
    private String wxAvatar;
    private String totalCacheSize;

    //修改手机号码结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateUserPhoneEvent event) {
        if (event != null && event.isSuccess()) {
            String maskNumber = spUtil.getString("cellphone", "").substring(0, 3)
                    + "****" + spUtil.getString("cellphone", "")
                    .substring(7, spUtil.getString("cellphone", "").length());
            Utils.setText(tvSetPhone, maskNumber, "", View.VISIBLE, View.VISIBLE);
        }
    }

    //支付密码设置结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetPayPwdSuccessEvent event) {
        if (event != null && event.isSuccess()) {
            getData();
        }
    }

    //微信登录结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXLoginEvent event) {

        if (event != null) {
            switch (event.getErrCode()) {
                //取消授权
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtil.showImageToast(SetActivity.this, "取消授权", R.drawable.icon_close_gary);
                    break;
                //用户拒绝授权
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    ToastUtil.showImageToast(SetActivity.this, "拒绝授权", R.drawable.icon_close_gary);
                    break;

                case BaseResp.ErrCode.ERR_OK:
                    openId = "";
                    wxNickname = "";
                    wxAvatar = "";
                    mPDialog.showDialog();
                    CommUtil.ticket(this, event.getCode(), ticketHandler);
                    break;
            }

        }
    }

    private AsyncHttpResponseHandler ticketHandler = new AsyncHttpResponseHandler() {

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
                        if (object.has("openId") && !object.isNull("openId")) {
                            openId = object.getString("openId");
                        }
                        if (object.has("wxNickname") && !object.isNull("wxNickname")) {
                            wxNickname = object.getString("wxNickname");
                        }
                        if (object.has("wxAvatar") && !object.isNull("wxAvatar")) {
                            wxAvatar = object.getString("wxAvatar");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            mPDialog.showDialog();
            //绑定微信
//            CommUtil.loginAu(mContext, spUtil.getString("cellphone", ""),
//                    Global.getIMEI(mContext),
//                    "", 0, 0, openId, "quick_login_app", wxAvatar, wxNickname,
//                    loginHandler);
            CommUtil.bindWxOpenId(mContext, spUtil.getString("cellphone", ""),
                    Global.getIMEI(mContext),
                    "", 0, 0, openId, "quick_login_app", wxAvatar, wxNickname,
                    bindHandler);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private AsyncHttpResponseHandler bindHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    spUtil.saveString("wxNickname", wxNickname);
                    ToastUtil.showToastShortCenter(mContext, "绑定成功");
                    updateBindWx();
                } else {
                    ToastUtil.showToastShortCenter(mContext, jsonObject.getString("msg"));
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

    private AsyncHttpResponseHandler unBindWxHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    spUtil.saveString("wxNickname", null);
                    ToastUtil.showToastShortCenter(mContext, "解绑成功");
                    //更新UI
                    updateBindWx();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorBar();
        initData();
        findView();
        setView();
        getData();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        mFingerprintCore = new FingerprintCore(this);
        api = WXAPIFactory.createWXAPI(this, Global.APP_ID, false);
        api.registerApp(Global.APP_ID);
    }

    private void findView() {
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
    }


    private void setView() {
        //刚进入页面不显示标题
        showHideTitle(false);

        if (Utils.checkLogin(mContext)) {
            rlSetZh.setVisibility(View.VISIBLE);
            tvSetExit.setVisibility(View.VISIBLE);
            tvSetPhoneQh.setText("+86");
            String maskNumber = spUtil.getString("cellphone", "").substring(0, 3)
                    + "****" + spUtil.getString("cellphone", "")
                    .substring(7, spUtil.getString("cellphone", "").length());
            Utils.setText(tvSetPhone, maskNumber, "", View.VISIBLE, View.VISIBLE);
            updateBindWx();
        } else {
            rlSetZh.setVisibility(View.GONE);
            tvSetExit.setVisibility(View.GONE);
        }
        try {
            totalCacheSize = DataCleanUtils.getTotalCacheSize(this);
            tvSetCache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvSetAboutVersion.setText(Global.getCurrentVersion(this));
        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //获取总滑动距离
                Utils.mLogError("滑动距离："+scrollY);
                //滑动超过40dp设置可见
                if (scrollY >54 && vTitleSlide.getVisibility() == View.GONE){
                    showHideTitle(true);
                }else if (scrollY < 54 && vTitleSlide.getVisibility() == View.VISIBLE){
                    showHideTitle(false);
                }
            }
        });
    }

    private void showHideTitle(boolean flag) {
        if (flag) {
            tvTitlebarTitle.setText("账户绑定与设置");
            vTitleSlide.setVisibility(View.VISIBLE);
        } else {
            tvTitlebarTitle.setText("");
            vTitleSlide.setVisibility(View.GONE);
        }
    }

    private void updateBindWx() {
        Utils.mLogError("微信信息： " + spUtil.getString("wxNickname", ""));
        if (Utils.isStrNull(spUtil.getString("wxNickname", ""))) {
            tvSetWxname.setVisibility(View.VISIBLE);
            tvSetWxname.setText("已绑定" + spUtil.getString("wxNickname", ""));
            tvSetWxcz.setText("解绑");
            tvSetWxcz.setTextColor(ContextCompat.getColor(mContext, R.color.a002241));
            tvSetWxcz.setBackgroundResource(R.drawable.bg_9fa7b9_round);
        } else {
            tvSetWxname.setVisibility(View.GONE);
            tvSetWxcz.setText("绑定");
            tvSetWxcz.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            tvSetWxcz.setBackgroundResource(R.drawable.bg_ff3a1e_round);
        }
    }

    private void getData() {
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
            if (mFingerprintCore.isSupport()) {
                if (payPwd == Global.NO_SET_PASSWORD) {//未设置过支付密码
                    tvPassword.setText("设置密码");
                    llSetZw.setVisibility(View.GONE);
                } else if (payPwd == Global.ALREADY_SET_PASSWORD) {//已设置过支付密码
                    tvPassword.setText("忘记/修改");
                    llSetZw.setVisibility(View.VISIBLE);
                    //判断设备是否录入指纹
                    boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                    //判断是否开启指纹支付
                    boolean isFinger = spUtil.getBoolean("isFinger", false);
                    if (isFinger && hasEnrolledFingerprints) {
                        tvSetZwopen.setText("已开启");
                    } else {
                        tvSetZwopen.setText("未开启");
                    }
                }
            } else {
                llSetZw.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    @OnClick({R.id.rl_set_cellphone, R.id.tv_set_wxcz, R.id.rl_set_mm, R.id.ll_set_zw, R.id.rl_set_per, R.id.rl_set_clear, R.id.rl_set_about, R.id.tv_set_exit, R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //更改手机号
            case R.id.rl_set_cellphone:
                ActivityUtils.toReplacePhone(mContext);
                break;
            case R.id.tv_set_wxcz:
                if (Utils.isStrNull(spUtil.getString("wxNickname", ""))) {
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("微信账户解绑").setMsg("确认您要从当前账户中解绑账户" + spUtil.getString("wxNickname", "") + "吗？").isOneBtn(false).setNegativeButton("再想想", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("确定解绑", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommUtil.unBindWxOpenId(SetActivity.this, spUtil.getString("cellphone", ""),
                                    Global.getIMEI(mContext),
                                    "", 0, 0, unBindWxHandler);
                        }
                    }).show();
                } else {
                    if (!api.isWXAppInstalled()) {
                        ToastUtil.showToastShortCenter(mContext, "您还没有安装微信!");
                        return;
                    }
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "weidu_wx_login";
                    api.sendReq(req);
                }
                break;
            case R.id.rl_set_mm:
                //没有设置密码
                if (payPwd == Global.NO_SET_PASSWORD) {
                    //跳转到设置密码
                    ActivityUtils.toSetPassword(SetActivity.this);
                }
                //修改密码 和 忘记密码
                else if (payPwd == Global.ALREADY_SET_PASSWORD) {
                    showSetPassWordDialog();
                }

                break;
            case R.id.ll_set_zw:
                //判断设备是否录入指纹
                boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                if (hasEnrolledFingerprints) {
                    startActivityForResult(new Intent(mContext, SetFingerprintActivity.class), Global.PAYSETTING_TO_SETUPPAYPWD);
                } else {
                    ToastUtil.showToastShort(this, "您还没有录制指纹，请录入！");
                    FingerprintUtil.openFingerPrintSettingPage(this);
                }
                break;
            case R.id.rl_set_per:
                startActivity(new Intent(mContext, SetPermissionsActivity.class));
                break;
            case R.id.rl_set_clear:
                boolean b = DataCleanUtils.clearAllCache(mContext);
                if (b) {
                    ToastUtil.showSuccessToast(mContext, "已清理" + totalCacheSize);
                    try {
                        totalCacheSize = DataCleanUtils.getTotalCacheSize(mContext);
                        tvSetCache.setText(totalCacheSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.rl_set_about:
                startActivity(new Intent(mContext, AboutUsMessageActivity.class));
                break;
            case R.id.tv_set_exit:
                new AlertDialogDefault(mContext).builder()
                        .setTitle("").setTitle("确定退出？").setMsg("").isOneBtn(false).setNegativeButton("再想想", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {// 退出登录
                        removeDataSp();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.mainactivity");
                        intent.putExtra("previous",
                                Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY);
                        sendBroadcast(intent);
                        //退出登录通知
                        EventBus.getDefault().post(new ExitLoginEvent());
                        finish();
                    }
                }).show();
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }

    private void removeDataSp() {
        spUtil.removeData("upShopName");
        spUtil.removeData("upRegionId");
        spUtil.removeData("isCerti");
        spUtil.removeData("city");
        spUtil.removeData("cellphone");
        spUtil.removeData("userid");
        spUtil.removeData("username");
        spUtil.removeData("userimage");
        spUtil.removeData("payway");
        spUtil.removeData("petid");
        spUtil.removeData("petkind");
        spUtil.removeData("petname");
        spUtil.removeData("petimage");
        spUtil.removeData("addressid");
        spUtil.removeData("lat");
        spUtil.removeData("lng");
        spUtil.removeData("address");
        spUtil.removeData("serviceloc");
        spUtil.removeData("shopid");
        spUtil.removeData("newshopid");
        spUtil.removeData("newaddr");
        spUtil.removeData("newlat");
        spUtil.removeData("newlng");
        spUtil.removeData("invitecode");
        spUtil.removeData("check_pwd_code_time");
    }

    /**
     * 修改 和 忘记 密码
     */
    private void showSetPassWordDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.setpassword_dialog, null);
        TextView tv_setpassworddialog_wj = (TextView) customView.findViewById(R.id.tv_setpassworddialog_wj);
        TextView tv_setpassworddialog_xg = (TextView) customView.findViewById(R.id.tv_setpassworddialog_xg);
        TextView tv_setpassworddialog_qx = (TextView) customView.findViewById(R.id.tv_setpassworddialog_qx);
        RelativeLayout rl_setpassworddialog_parent = (RelativeLayout) customView.findViewById(R.id.rl_setpassworddialog_parent);
        final PopupWindow popupWindow = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setWidth(Utils.getDisplayMetrics(mContext)[0]);
        popupWindow.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        //忘记密码
        tv_setpassworddialog_wj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //判断是否设置支付密码
                if (payPwd == Global.NO_SET_PASSWORD) {//未设置过支付密码
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置密码
                            ActivityUtils.toSetPassword(SetActivity.this);
                        }
                    }).show();
                } else if (payPwd == Global.ALREADY_SET_PASSWORD) {//已设置过支付密码
                    ActivityUtils.toForgetPassword(SetActivity.this);
                }
            }
        });
        //修改支付密码
        tv_setpassworddialog_xg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //判断是否设置支付密码
                if (payPwd == 0) {//未设置过支付密码
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("").setMsg("为了您的账户安全，请设置支付密码。").isOneBtn(false).setNegativeButton("下次再说", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置密码
                            ActivityUtils.toSetPassword(SetActivity.this);
                        }
                    }).show();
                } else if (payPwd == 1) {//已设置过支付密码
                    //修改密码
                    ActivityUtils.toUpdatePassword(SetActivity.this);
                }
            }
        });
        tv_setpassworddialog_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        rl_setpassworddialog_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.PAYSETTING_TO_SETUPPAYPWD) {
                //判断是否开启指纹支付
                boolean isFinger = spUtil.getBoolean("isFinger", false);
                if (isFinger) {
                    tvSetZwopen.setText("已开启");
                } else {
                    tvSetZwopen.setText("未开启");
                }
            }
        }
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
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
