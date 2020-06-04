package com.haotang.pet;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.entity.SetPayPwdSuccessEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ShadowLayout;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import static com.haotang.pet.util.Global.FORGET_PASSWORD;

/**
 * 设置支付密码界面
 */
public class SetUpPayPwdActivity extends SuperActivity implements
        View.OnClickListener {
    LinearLayout LLAnimation;
    private ImageView ibBack;
    private TextView tvTitle;
    private MProgressDialog pDialog;
    private TextView tv_setuppaypwd_miaoshu;
    private CodeView cv_setuppaypwd_pwd;
    private ShadowLayout sl_setuppaypwd;
    private KeyboardView kbv_setuppaypwd;
    private String pwd;
    private int inputNum = 0;
    public static SetUpPayPwdActivity act;
    private int flag;
    private RelativeLayout rl_setuppaypwd_oldpwddesc;
    private TextView tv_setuppaypwd_oldpwd_wjmm;
    private TextView tv_setuppaypwd_oldpwderror;
    private RelativeLayout rl_setuppaypwd_pwd;
    private String oldPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
    }

    private void init() {
        act = this;
        MApplication.listAppoint.add(this);
        pDialog = new MProgressDialog(this);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_set_up_pay_pwd);
        ibBack = (ImageView) findViewById(R.id.ib_setuppaypwd_back);
        tvTitle = (TextView) findViewById(R.id.tv_setuppaypwd_title);
        tv_setuppaypwd_miaoshu = (TextView) findViewById(R.id.tv_setuppaypwd_miaoshu);
        cv_setuppaypwd_pwd = (CodeView) findViewById(R.id.cv_setuppaypwd_pwd);
        sl_setuppaypwd = (ShadowLayout) findViewById(R.id.sl_setuppaypwd);
        kbv_setuppaypwd = (KeyboardView) findViewById(R.id.kbv_setuppaypwd);
        rl_setuppaypwd_oldpwddesc = (RelativeLayout) findViewById(R.id.rl_setuppaypwd_oldpwddesc);
        tv_setuppaypwd_oldpwd_wjmm = (TextView) findViewById(R.id.tv_setuppaypwd_oldpwd_wjmm);
        tv_setuppaypwd_oldpwderror = (TextView) findViewById(R.id.tv_setuppaypwd_oldpwderror);
        rl_setuppaypwd_pwd = (RelativeLayout) findViewById(R.id.rl_setuppaypwd_pwd);
        LLAnimation = findViewById(R.id.animation_root);
    }

    private void showFirstSetPassword(){
        startAnimation();
        //初始化到第一次输入状态
        inputNum = 0;
        tv_setuppaypwd_oldpwderror.setVisibility(View.GONE);
        rl_setuppaypwd_oldpwddesc.setVisibility(View.GONE);
        cv_setuppaypwd_pwd.clear();
        sl_setuppaypwd.setVisibility(View.GONE);
        tv_setuppaypwd_miaoshu.setVisibility(View.VISIBLE);
        tv_setuppaypwd_miaoshu.setText("密码不要过于简单，容易被他人获取，建议区分设置");
        tvTitle.setText("设置新支付密码");
        ToastUtil.showImageToast(this,"两次输入密码不一致",R.drawable.icon_warn_gray);
    }

    /**
     * 确认密码输入错误动画
     */
    private void startAnimation(){
        //设置activity背景透明
        setBackgroundAlpha(0.6f);
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofFloat(LLAnimation, "translationX", 0.0F, ScreenUtil.getScreenWidth(this),0.0f)
                .setDuration(500);
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //动画结束
                        setBackgroundAlpha(1.0f);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                objectAnimator.start();
    }

    private void setView() {
        cv_setuppaypwd_pwd.setShowType(CodeView.SHOW_TYPE_PASSWORD);
        cv_setuppaypwd_pwd.setLength(6);
        kbv_setuppaypwd.setCodeView(cv_setuppaypwd_pwd);
        sl_setuppaypwd.setVisibility(View.GONE);
        rl_setuppaypwd_oldpwddesc.setVisibility(View.GONE);
        tv_setuppaypwd_oldpwderror.setVisibility(View.GONE);
        tv_setuppaypwd_oldpwd_wjmm.setVisibility(View.GONE);
        tv_setuppaypwd_miaoshu.setVisibility(View.VISIBLE);
        if (flag == Global.FIRST_SET_PASSWORD) {//初次设置支付密码
            tvTitle.setText("设置支付密码");
            tv_setuppaypwd_miaoshu.setText("密码过于简单，容易被他人获取，建议区分设置");
        } else if (flag == FORGET_PASSWORD) {//忘记密码
            tvTitle.setText("设置新支付密码");
            tv_setuppaypwd_miaoshu.setText("密码过于简单，容易被他人获取，建议区分设置");
        } else if (flag == Global.UPDATE_PASSWORD) {//修改密码
            tvTitle.setText("请输入原支付密码");
            tv_setuppaypwd_miaoshu.setVisibility(View.INVISIBLE);
        } else if (flag == Global.VERIFY_PASSWORD) {//校验支付密码
            tvTitle.setText("安全验证");
            tv_setuppaypwd_miaoshu.setText("请输入支付密码以验证身份");
            tv_setuppaypwd_oldpwd_wjmm.setVisibility(View.VISIBLE);
            rl_setuppaypwd_oldpwddesc.setVisibility(View.VISIBLE);
        }
    }

    private void setLinster() {
        tv_setuppaypwd_oldpwd_wjmm.setOnClickListener(this);
        sl_setuppaypwd.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        cv_setuppaypwd_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kbv_setuppaypwd.show();
            }
        });
        cv_setuppaypwd_pwd.setListener(new CodeView.Listener() {
            @Override
            public void onValueChanged(String value) {
                sl_setuppaypwd.setVisibility(View.GONE);
                tv_setuppaypwd_oldpwderror.setVisibility(View.GONE);
            }

            @Override
            public void onComplete(String value) {
                inputNum++;
                if (flag == Global.FIRST_SET_PASSWORD) {//初次设置密码或者忘记密码
                    if (inputNum == 1) {//第一次输入
                        pwd = value;
                        cv_setuppaypwd_pwd.clear();
                        tv_setuppaypwd_miaoshu.setVisibility(View.VISIBLE);
                        tv_setuppaypwd_miaoshu.setText("请对新设置密码进行二次校验");
                    } else {//第二次输入
                        if (pwd.equals(value)) {
                            sl_setuppaypwd.setVisibility(View.VISIBLE);
                            tv_setuppaypwd_oldpwderror.setVisibility(View.GONE);
                        } else {
                            cv_setuppaypwd_pwd.clear();
                            rl_setuppaypwd_pwd.startAnimation(shakeAnimation(5));
                            sl_setuppaypwd.setVisibility(View.GONE);
                            rl_setuppaypwd_oldpwddesc.setVisibility(View.VISIBLE);
                            tv_setuppaypwd_oldpwderror.setVisibility(View.VISIBLE);
                            tv_setuppaypwd_oldpwderror.setText("再次输入的密码不一致");
                        }
                    }
                } else if (flag == FORGET_PASSWORD) {//忘记密码
                    if (inputNum == 1) {//第一次输入
                        pwd = value;
                        cv_setuppaypwd_pwd.clear();
                        tvTitle.setText("请再次确认密码");
                        tv_setuppaypwd_miaoshu.setVisibility(View.VISIBLE);
                        tv_setuppaypwd_miaoshu.setText("请对新设置密码进行二次校验");
                    } else {//第二次输入
                        if (pwd.equals(value)) {
                            sl_setuppaypwd.setVisibility(View.VISIBLE);
                            tv_setuppaypwd_oldpwderror.setVisibility(View.GONE);
                        } else {
                            cv_setuppaypwd_pwd.clear();
//                            rl_setuppaypwd_pwd.startAnimation(shakeAnimation(5));
                            sl_setuppaypwd.setVisibility(View.GONE);
                            rl_setuppaypwd_oldpwddesc.setVisibility(View.VISIBLE);
                            tv_setuppaypwd_oldpwderror.setVisibility(View.VISIBLE);
//                            tv_setuppaypwd_oldpwderror.setText("再次输入的密码不一致");
                            //初始化到第一次输入状态
                            showFirstSetPassword();
                        }
                    }
                } else if (flag == Global.UPDATE_PASSWORD) {//修改密码
                    if (inputNum == 1) {//第一次输入
                        inputNum--;
                        pwd = value;
                        mPDialog.showDialog();
                        CommUtil.disposePayPwd(mContext,
                                value, "", 1, checkPayPwdHandler);
                    }
                } else if (flag == Global.VERIFY_PASSWORD) {//校验密码
                    mPDialog.showDialog();
                    CommUtil.disposePayPwd(mContext,
                            value, "", 1, checkPayPwdHandler);
                }
            }
        });
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
                    //修改密码
                    if (flag == Global.UPDATE_PASSWORD) {
                        oldPwd = pwd;
                        flag = FORGET_PASSWORD;//状态改为忘记密码
                        tv_setuppaypwd_oldpwderror.setVisibility(View.GONE);
                        rl_setuppaypwd_oldpwddesc.setVisibility(View.GONE);
                        cv_setuppaypwd_pwd.clear();
                        sl_setuppaypwd.setVisibility(View.GONE);
                        tv_setuppaypwd_miaoshu.setVisibility(View.VISIBLE);
                        tv_setuppaypwd_miaoshu.setText("密码不要过于简单，容易被他人获取，建议区分设置");
                        tvTitle.setText("设置新支付密码");
                    }
                    //校验设置密码
                    else if (flag == Global.VERIFY_PASSWORD) {
                        setResult(Global.RESULT_OK);
                        finish();
                    }
                } else {
                    cv_setuppaypwd_pwd.clear();
                    rl_setuppaypwd_pwd.startAnimation(shakeAnimation(5));
                    sl_setuppaypwd.setVisibility(View.GONE);
                    rl_setuppaypwd_oldpwddesc.setVisibility(View.VISIBLE);
                    tv_setuppaypwd_oldpwderror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_setuppaypwd_oldpwderror.setText(msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(SetUpPayPwdActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(SetUpPayPwdActivity.this, "请求失败");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //忘记密码
            case R.id.tv_setuppaypwd_oldpwd_wjmm:
                ActivityUtils.toForgetPassword(mContext);
                break;
            case R.id.ib_setuppaypwd_back:
                finish();
                break;
            case R.id.sl_setuppaypwd:
                if (flag == Global.FIRST_SET_PASSWORD) {//设置初始密码
                    pDialog.showDialog();
                    CommUtil.setPayPwd(mContext, pwd, spUtil.getString("cellphone", ""), setPayPwdHandler);
                } else if (flag == FORGET_PASSWORD) {//忘记密码设置新密码
                    pDialog.showDialog();
                    CommUtil.disposePayPwd
                            (SetUpPayPwdActivity.this, "",
                                    pwd, 3, setNewPayPwdHandler);
                } else if (flag == Global.UPDATE_PASSWORD) {//修改密码设置新密码
                    pDialog.showDialog();
                    CommUtil.disposePayPwd
                            (SetUpPayPwdActivity.this, oldPwd,
                                    pwd, 2, setNewPayPwdHandler);
                }
                break;
            default:
                break;
        }
    }

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
                    ToastUtil.showSuccessToast(mContext, "密码设置成功");
                    EventBus.getDefault().post(new SetPayPwdSuccessEvent(true));
                    //去开通指纹支付
                    if (getIntent().getBooleanExtra("isGoSetFinger", false)) {
                        startActivity(new Intent(mContext, SetFingerprintActivity.class).putExtra("flag", 1));
                    }
                    finish();
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
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
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private AsyncHttpResponseHandler setNewPayPwdHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    ToastUtil.showSuccessToast(mContext, "密码修改成功");
                    EventBus.getDefault().post(new SetPayPwdSuccessEvent(true));
                    finish();
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(SetUpPayPwdActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(SetUpPayPwdActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(SetUpPayPwdActivity.this, "请求失败");
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
