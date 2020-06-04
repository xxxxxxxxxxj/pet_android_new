package com.haotang.pet;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.codeview.CodeView;
import com.haotang.pet.codeview.KeyboardView;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.fingerprintrecognition.FingerprintUtil;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CustomStatusView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 开启指纹支付界面
 */
public class StartFingerprintActivity extends SuperActivity implements
        View.OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private Button btn_startfingerprint_qrkq;
    private Button btn_startfingerprint_yhzs;
    private FingerprintCore mFingerprintCore;
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
    private LinearLayout ll_paypwdsystem_pop_payresult;
    private CustomStatusView csv_paypwdsystem_pop;
    private TextView tv_paypwdsystem_pop_payresult;
    private SharedPreferenceUtil spUtil;
    private RelativeLayout rl_commodity_black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
    }

    private void setLinster() {
        btn_startfingerprint_qrkq.setOnClickListener(this);
        btn_startfingerprint_yhzs.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void setView() {
        tvTitle.setText("开启指纹支付");
    }

    private void findView() {
        setContentView(R.layout.activity_start_fingerprint);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        btn_startfingerprint_qrkq = (Button) findViewById(R.id.btn_startfingerprint_qrkq);
        btn_startfingerprint_yhzs = (Button) findViewById(R.id.btn_startfingerprint_yhzs);
        rl_commodity_black = (RelativeLayout) findViewById(R.id.rl_commodity_black);
    }

    private void init() {
        MApplication.listAppoint.add(this);
        mFingerprintCore = new FingerprintCore(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            setResult(Global.RESULT_OK);
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                setResult(Global.RESULT_OK);
                finish();
                break;
            case R.id.btn_startfingerprint_yhzs:
                setResult(Global.RESULT_OK);
                finish();
                break;
            case R.id.btn_startfingerprint_qrkq:
                if (mFingerprintCore.isHasEnrolledFingerprints()) {
                    showPayPwdSystem();
                } else {
                    ToastUtil.showToastShort(this, "您还没有录制指纹，请录入！");
                    FingerprintUtil.openFingerPrintSettingPage(this);
                }
                break;
            default:
                break;
        }
    }

    private void showPayPwdSystem() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
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
            tv_paypwdsystem_pop_pwderror.setVisibility(View.GONE);
            tv_paypwdsystem_pop_title.setText("请输入支付密码");
            rl_paypwdsystem_pop_select.setVisibility(View.GONE);
            rl_paypwdsystem_pop_pwd.setVisibility(View.VISIBLE);
            cv_paypwdsystem_pop_pwd.setShowType(CodeView.SHOW_TYPE_PASSWORD);
            cv_paypwdsystem_pop_pwd.setLength(6);
            kbv_paypwdsystem_pop.setCodeView(cv_paypwdsystem_pop_pwd);
            iv_paypwdsystem_pop_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinPayPwdSystem.dismiss();
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
                    spUtil.saveBoolean("isFinger", true);
                    ToastUtil.showToastShortBottom(StartFingerprintActivity.this, "开启成功");
                    pWinPayPwdSystem.dismiss();
                    setResult(Global.RESULT_OK);
                    finish();
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
                ToastUtil.showToastShortBottom(StartFingerprintActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(StartFingerprintActivity.this, "请求失败");
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

}
