package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置密保手机号码界面
 */
public class PayPwdProtectionActivity extends SuperActivity implements
        View.OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private MProgressDialog pDialog;
    private EditText et_paypwdprotection_onephone;
    private TextView tv_paypwdprotection_onephoneerror;
    private EditText et_paypwdprotection_twophone;
    private TextView tv_paypwdprotection_twophoneerror;
    private TextView tv_paypwdprotection_desc;
    private TextView tv_paypwdprotection_mibaotishi;
    private Button btn_paypwdprotection;
    private String pwd;
    public String onePhone;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
    }

    private void setLinster() {
        btn_paypwdprotection.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        et_paypwdprotection_onephone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                et_paypwdprotection_twophone.setText("");
                tv_paypwdprotection_onephoneerror.setVisibility(View.GONE);
                et_paypwdprotection_twophone.setVisibility(View.GONE);
                tv_paypwdprotection_twophoneerror.setVisibility(View.GONE);
                btn_paypwdprotection.setVisibility(View.GONE);
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    et_paypwdprotection_onephone.setText(sb.toString());
                    et_paypwdprotection_onephone.setSelection(index);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim().replace(" ", "");
                Log.e("TAG", "phone = " + phone);
                if (phone.length() == 11) {
                    boolean bool = Utils.checkPhone(PayPwdProtectionActivity.this, et_paypwdprotection_onephone);
                    if (bool) {
                        if (flag == 0) {
                            if (!phone.equals(SharedPreferenceUtil.getInstance(PayPwdProtectionActivity.this).getString(
                                    "cellphone", ""))) {
                                onePhone = phone;
                                et_paypwdprotection_twophone.setVisibility(View.VISIBLE);
                                tv_paypwdprotection_onephoneerror.setVisibility(View.GONE);
                                tv_paypwdprotection_twophoneerror.setVisibility(View.GONE);
                                btn_paypwdprotection.setVisibility(View.GONE);
                                et_paypwdprotection_twophone.setText("");
                                et_paypwdprotection_twophone.requestFocus();
                            } else {
                                et_paypwdprotection_onephone.setAnimation(shakeAnimation(5));
                                tv_paypwdprotection_twophoneerror.setVisibility(View.GONE);
                                btn_paypwdprotection.setVisibility(View.GONE);
                                et_paypwdprotection_twophone.setVisibility(View.GONE);
                                tv_paypwdprotection_onephoneerror.setVisibility(View.VISIBLE);
                                tv_paypwdprotection_onephoneerror.setText("备用手机号码不能与登录手机号码一致");
                                Utils.goneJP(PayPwdProtectionActivity.this);
                            }
                        } else if (flag == 1) {
                            if (!phone.equals(SharedPreferenceUtil.getInstance(PayPwdProtectionActivity.this).getString(
                                    "cellphone", ""))) {
                                onePhone = phone;
                                btn_paypwdprotection.setVisibility(View.VISIBLE);
                                Utils.goneJP(PayPwdProtectionActivity.this);
                            } else {
                                et_paypwdprotection_onephone.setAnimation(shakeAnimation(5));
                                tv_paypwdprotection_twophoneerror.setVisibility(View.GONE);
                                btn_paypwdprotection.setVisibility(View.GONE);
                                et_paypwdprotection_twophone.setVisibility(View.GONE);
                                tv_paypwdprotection_onephoneerror.setVisibility(View.VISIBLE);
                                tv_paypwdprotection_onephoneerror.setText("备用手机号码不能与登录手机号码一致");
                                Utils.goneJP(PayPwdProtectionActivity.this);
                            }
                        }
                    } else {
                        et_paypwdprotection_onephone.setAnimation(shakeAnimation(5));
                        tv_paypwdprotection_twophoneerror.setVisibility(View.GONE);
                        btn_paypwdprotection.setVisibility(View.GONE);
                        et_paypwdprotection_twophone.setVisibility(View.GONE);
                        tv_paypwdprotection_onephoneerror.setVisibility(View.VISIBLE);
                        tv_paypwdprotection_onephoneerror.setText("请输入正确的手机号");
                        Utils.goneJP(PayPwdProtectionActivity.this);
                    }
                }
            }
        });

        et_paypwdprotection_twophone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tv_paypwdprotection_onephoneerror.setVisibility(View.GONE);
                tv_paypwdprotection_twophoneerror.setVisibility(View.GONE);
                btn_paypwdprotection.setVisibility(View.GONE);
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    et_paypwdprotection_twophone.setText(sb.toString());
                    et_paypwdprotection_twophone.setSelection(index);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim().replace(" ", "");
                if (phone.length() == 11) {
                    boolean bool = Utils.checkPhone(PayPwdProtectionActivity.this, et_paypwdprotection_twophone);
                    if (bool) {
                        if (onePhone.equals(phone)) {
                            tv_paypwdprotection_onephoneerror.setVisibility(View.GONE);
                            tv_paypwdprotection_twophoneerror.setVisibility(View.GONE);
                            btn_paypwdprotection.setVisibility(View.VISIBLE);
                            Utils.goneJP(PayPwdProtectionActivity.this);
                        } else {
                            et_paypwdprotection_twophone.setAnimation(shakeAnimation(5));
                            tv_paypwdprotection_onephoneerror.setVisibility(View.GONE);
                            tv_paypwdprotection_twophoneerror.setVisibility(View.VISIBLE);
                            tv_paypwdprotection_twophoneerror.setText("备用手机号不一致");
                            btn_paypwdprotection.setVisibility(View.GONE);
                            Utils.goneJP(PayPwdProtectionActivity.this);
                        }
                    } else {
                        et_paypwdprotection_twophone.setAnimation(shakeAnimation(5));
                        tv_paypwdprotection_onephoneerror.setVisibility(View.GONE);
                        tv_paypwdprotection_twophoneerror.setVisibility(View.VISIBLE);
                        tv_paypwdprotection_twophoneerror.setText("请输入正确的手机号");
                        btn_paypwdprotection.setVisibility(View.GONE);
                        Utils.goneJP(PayPwdProtectionActivity.this);
                    }
                }
            }
        });

    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    private void setView() {
        if (flag == 0) {
            tvTitle.setText("填写备用手机号");
            tv_paypwdprotection_desc.setText("您可以输入亲友手机号为备用手机号，当找回密码时可用于安全问答验证身份");
            tv_paypwdprotection_mibaotishi.setVisibility(View.GONE);
            btn_paypwdprotection.setText("提交");
        } else if (flag == 1) {
            tvTitle.setText("密保问题");
            tv_paypwdprotection_desc.setText("为了保护您的账户安全与信息找回，请输入您的备用手机号");
            tv_paypwdprotection_mibaotishi.setVisibility(View.VISIBLE);
            btn_paypwdprotection.setText("验证");
        }
        et_paypwdprotection_onephone.requestFocus();
    }

    private void findView() {
        setContentView(R.layout.activity_pay_pwd_protection);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        et_paypwdprotection_onephone = (EditText) findViewById(R.id.et_paypwdprotection_onephone);
        tv_paypwdprotection_onephoneerror = (TextView) findViewById(R.id.tv_paypwdprotection_onephoneerror);
        et_paypwdprotection_twophone = (EditText) findViewById(R.id.et_paypwdprotection_twophone);
        tv_paypwdprotection_twophoneerror = (TextView) findViewById(R.id.tv_paypwdprotection_twophoneerror);
        btn_paypwdprotection = (Button) findViewById(R.id.btn_paypwdprotection);
        tv_paypwdprotection_desc = (TextView) findViewById(R.id.tv_paypwdprotection_desc);
        tv_paypwdprotection_mibaotishi = (TextView) findViewById(R.id.tv_paypwdprotection_mibaotishi);
    }

    private void init() {
        MApplication.listAppoint.add(this);
        pDialog = new MProgressDialog(this);
        Intent intent = getIntent();
        pwd = intent.getStringExtra("pwd");
        flag = intent.getIntExtra("flag", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_paypwdprotection:
                if (flag == 0) {
                    pDialog.showDialog();
                    CommUtil.setPayPwd(PayPwdProtectionActivity.this,
                            pwd,"", setPayPwdHandler);
                } else if (flag == 1) {//验证备用手机号
                    pDialog.showDialog();
                    CommUtil.resetSecurityCard(PayPwdProtectionActivity.this, onePhone, checkSparePayPhoneHandler);
                }
                break;
            default:
                break;
        }
    }

    private AsyncHttpResponseHandler checkSparePayPhoneHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    Intent intent = new Intent(PayPwdProtectionActivity.this, SetUpPayPwdActivity.class);
                    intent.putExtra("flag", 1);
                    intent.putExtra("onePhone", "onePhone");
                    startActivity(intent);
                    finish();
                } else {
                    tv_paypwdprotection_onephoneerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_paypwdprotection_onephoneerror.setText(msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(PayPwdProtectionActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(PayPwdProtectionActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler setPayPwdHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    setResult(Global.RESULT_OK, new Intent().putExtra("isSetPayPwdSuccess", true));
                    finish();
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(PayPwdProtectionActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(PayPwdProtectionActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(PayPwdProtectionActivity.this, "请求失败");
        }
    };

}
