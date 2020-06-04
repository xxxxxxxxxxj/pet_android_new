package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.fingerprintrecognition.FingerprintUtil;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 支付设置界面
 */
public class PaySettingActivity extends SuperActivity implements
        View.OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private RelativeLayout rl_paysetting_jymx;
    private RelativeLayout rl_paysetting_xgzfmm;
    private RelativeLayout rl_paysetting_wjzfmm;
    private RelativeLayout rl_paysetting_zwzf;
    private ImageView iv_paysetting_zwzf;
    private TextView tv_paysetting_zwzf_desc;
    private RelativeLayout rl_paysetting_bzzx;
    private int payPwd;
    private FingerprintCore mFingerprintCore;
    private SharedPreferenceUtil spUtil;
    private String url;
    private MProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pDialog.showDialog();
        CommUtil.getAccountBalance(this, SharedPreferenceUtil.getInstance(this).getString("cellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), getMoney);
    }

    private AsyncHttpResponseHandler getMoney = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
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
                            if (juser.has("payHelp") && !juser.isNull("payHelp")) {
                                url = juser.getString("payHelp");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(PaySettingActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(PaySettingActivity.this, "请求失败");
        }
    };

    private void setLinster() {
        ibBack.setOnClickListener(this);
        rl_paysetting_jymx.setOnClickListener(this);
        rl_paysetting_xgzfmm.setOnClickListener(this);
        rl_paysetting_wjzfmm.setOnClickListener(this);
        iv_paysetting_zwzf.setOnClickListener(this);
        rl_paysetting_bzzx.setOnClickListener(this);
    }

    private void setView() {
        tvTitle.setText("支付设置");
        if (mFingerprintCore.isSupport()) {
            if (payPwd == 0) {//未设置过支付密码
                rl_paysetting_zwzf.setVisibility(View.GONE);
                tv_paysetting_zwzf_desc.setVisibility(View.GONE);
            } else if (payPwd == 1) {//已设置过支付密码
                rl_paysetting_zwzf.setVisibility(View.VISIBLE);
                tv_paysetting_zwzf_desc.setVisibility(View.VISIBLE);
                //判断设备是否录入指纹
                boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                //判断是否开启指纹支付
                boolean isFinger = spUtil.getBoolean("isFinger", false);
                if (isFinger && hasEnrolledFingerprints) {
                    iv_paysetting_zwzf.setImageResource(R.drawable.noty_yes);
                } else {
                    iv_paysetting_zwzf.setImageResource(R.drawable.noty_no);
                }
            }
        } else {
            rl_paysetting_zwzf.setVisibility(View.GONE);
            tv_paysetting_zwzf_desc.setVisibility(View.GONE);
        }
    }

    private void findView() {
        setContentView(R.layout.activity_pay_setting);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        rl_paysetting_jymx = (RelativeLayout) findViewById(R.id.rl_paysetting_jymx);
        rl_paysetting_xgzfmm = (RelativeLayout) findViewById(R.id.rl_paysetting_xgzfmm);
        rl_paysetting_wjzfmm = (RelativeLayout) findViewById(R.id.rl_paysetting_wjzfmm);
        rl_paysetting_zwzf = (RelativeLayout) findViewById(R.id.rl_paysetting_zwzf);
        iv_paysetting_zwzf = (ImageView) findViewById(R.id.iv_paysetting_zwzf);
        tv_paysetting_zwzf_desc = (TextView) findViewById(R.id.tv_paysetting_zwzf_desc);
        rl_paysetting_bzzx = (RelativeLayout) findViewById(R.id.rl_paysetting_bzzx);
    }

    private void init() {
        pDialog = new MProgressDialog(this);
        MApplication.listAppoint.add(this);
        Intent intent = getIntent();
        payPwd = intent.getIntExtra("payPwd", 0);
        url = intent.getStringExtra("payHelp");
        mFingerprintCore = new FingerprintCore(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_paysetting_jymx:
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyBillActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.rl_paysetting_xgzfmm:
                //判断是否设置支付密码
                if (payPwd == 0) {//未设置过支付密码
                    MDialog mDialog = new MDialog.Builder(this)
                            .setType(MDialog.DIALOGTYPE_CONFIRM)
                            .setMessage("为了您的账户安全，请设置支付密码。").setCancelStr("下次再说")
                            .setOKStr("设置")
                            .setCancelTextColor(getResources().getColor(R.color.a999999))
                            .setOKTextColor(getResources().getColor(R.color.aBB996C))
                            .setMsgTextColor(getResources().getColor(R.color.black))
                            .positiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(PaySettingActivity.this, SetUpPayPwdActivity.class));
                                }
                            }).negativeListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).build();
                    mDialog.show();
                } else if (payPwd == 1) {//已设置过支付密码
                    startActivity(new Intent(PaySettingActivity.this, SetUpPayPwdActivity.class).putExtra("flag", 2));
                }
                break;
            case R.id.rl_paysetting_wjzfmm:
                //判断是否设置支付密码
                if (payPwd == 0) {//未设置过支付密码
                    MDialog mDialog = new MDialog.Builder(this)
                            .setType(MDialog.DIALOGTYPE_CONFIRM)
                            .setMessage("为了您的账户安全，请设置支付密码。").setCancelStr("下次再说")
                            .setOKStr("设置")
                            .setCancelTextColor(getResources().getColor(R.color.a999999))
                            .setOKTextColor(getResources().getColor(R.color.aBB996C))
                            .setMsgTextColor(getResources().getColor(R.color.black))
                            .positiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(PaySettingActivity.this, SetUpPayPwdActivity.class));
                                }
                            }).negativeListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).build();
                    mDialog.show();
                } else if (payPwd == 1) {//已设置过支付密码
                    ActivityUtils.toForgetPassword(mContext);
                }
                break;
            case R.id.iv_paysetting_zwzf:
                //判断是否开启指纹支付
                boolean isFinger = spUtil.getBoolean("isFinger", false);
                if (isFinger) {
                    ToastUtil.showToastShortBottom(this, "关闭成功");
                    iv_paysetting_zwzf.setImageResource(R.drawable.noty_no);
                    spUtil.saveBoolean("isFinger", false);
                } else {
                    //判断设备是否录入指纹
                    boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                    if (hasEnrolledFingerprints) {
                        startActivityForResult(new Intent(this, SetUpPayPwdActivity.class).putExtra("flag", 3), Global.PAYSETTING_TO_SETUPPAYPWD);
                    } else {
                        ToastUtil.showToastShort(this, "您还没有录制指纹，请录入！");
                        FingerprintUtil.openFingerPrintSettingPage(this);
                    }
                }
                break;
            case R.id.rl_paysetting_bzzx:
                startActivity(new Intent(this, ADActivity.class).putExtra("url", url));
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.PAYSETTING_TO_SETUPPAYPWD) {
                iv_paysetting_zwzf.setImageResource(R.drawable.noty_yes);
                spUtil.saveBoolean("isFinger", true);
            }
        }
    }
}
