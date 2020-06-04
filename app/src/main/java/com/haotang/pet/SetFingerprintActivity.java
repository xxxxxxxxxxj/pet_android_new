package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.fingerprintrecognition.FingerprintCore;
import com.haotang.pet.fingerprintrecognition.FingerprintUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.AlertDialogDefault;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开启关闭指纹界面
 */
public class SetFingerprintActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_setfingerprint)
    ImageView ivSetfingerprint;
    @BindView(R.id.tv_setfingerprint_open)
    TextView tvSetfingerprintOpen;
    @BindView(R.id.tv_setfingerprint_desc)
    TextView tvSetfingerprintDesc;
    @BindView(R.id.tv_setfingerprint_submit)
    TextView tvSetfingerprintSubmit;
    @BindView(R.id.tv_setfingerprint_tg)
    TextView tvSetfingerprintTg;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibBack;
    private int flag;
    private FingerprintCore mFingerprintCore;

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
        mFingerprintCore = new FingerprintCore(this);
        flag = getIntent().getIntExtra("flag", 0);
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_set_fingerprint);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("指纹支付");
        if (flag == 0) {//设置界面过来
            tvSetfingerprintTg.setVisibility(View.GONE);
            ibBack.setVisibility(View.VISIBLE);
        } else if (flag == 1) {//支付逻辑中开通指纹支付
            tvSetfingerprintTg.setVisibility(View.VISIBLE);
            ibBack.setVisibility(View.GONE);
        }
        setFingerprint();
    }

    private void setFingerprint() {
        //判断是否开启指纹支付
        boolean isFinger = spUtil.getBoolean("isFinger", false);
        if (isFinger) {
            ivSetfingerprint.setImageResource(R.drawable.icon_zw_open);
            tvSetfingerprintOpen.setText("指纹支付已开通");
            tvSetfingerprintDesc.setText("验证系统指纹快速完成付款，更加安全便捷");
            tvSetfingerprintSubmit.setText("关闭指纹支付");
        } else {
            ivSetfingerprint.setImageResource(R.drawable.icon_zw_close);
            tvSetfingerprintOpen.setText("开通指纹支付，让付款更安全便捷");
            tvSetfingerprintDesc.setText("开启后，可通过验证指纹快速完成付款");
            tvSetfingerprintSubmit.setText("立即开通");
        }
    }

    private void setLinster() {

    }

    private void getData() {

    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_setfingerprint_submit, R.id.tv_setfingerprint_tg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_setfingerprint_tg:
                setResult(Global.RESULT_OK);
                finish();
                break;
            case R.id.ib_titlebar_back:
                setResult(Global.RESULT_OK);
                finish();
                break;
            case R.id.tv_setfingerprint_submit:
                //判断是否开启指纹支付
                boolean isFinger = spUtil.getBoolean("isFinger", false);
                if (isFinger) {
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("你确定要关闭指纹支付吗？").setMsg("关闭后每次支付都需要输入密码").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("关闭", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showToastShortBottom(mContext, "关闭成功");
                            spUtil.saveBoolean("isFinger", false);
                            setFingerprint();
                            setResult(Global.RESULT_OK);
                            finish();
                        }
                    }).show();
                } else {
                    if (flag == 0) {//设置界面过来
                        //验证支付密码后开通
                        startActivityForResult(new Intent(mContext, SetUpPayPwdActivity.class).putExtra("flag", 3), Global.PAYSETTING_TO_SETUPPAYPWD);
                    } else if (flag == 1) {//支付逻辑中开通指纹支付
                        if (mFingerprintCore.isHasEnrolledFingerprints()) {
                            spUtil.saveBoolean("isFinger", true);
                            ToastUtil.showToastShortBottom(mContext, "开启成功");
                            setFingerprint();
                            setResult(Global.RESULT_OK);
                            finish();
                        } else {
                            ToastUtil.showToastShort(this, "您还没有录制指纹，请录入！");
                            FingerprintUtil.openFingerPrintSettingPage(this);
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.PAYSETTING_TO_SETUPPAYPWD) {
                spUtil.saveBoolean("isFinger", true);
                ToastUtil.showToastShortBottom(mContext, "开启成功");
                setFingerprint();
                setResult(Global.RESULT_OK);
                finish();
            }
        }
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
