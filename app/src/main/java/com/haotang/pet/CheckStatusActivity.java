package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MD5;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 核验身份界面
 */
public class CheckStatusActivity extends SuperActivity {
    @BindView(R.id.tv_checkstatus_kfphone)
    TextView tvCheckstatusKfphone;
    @BindView(R.id.ll_checkstatus_bottom)
    LinearLayout llCheckstatusBottom;
    @BindView(R.id.tv_checkstatus_title)
    TextView tvCheckstatusTitle;
    @BindView(R.id.tv_checkstatus_miaoshu)
    TextView tvCheckstatusMiaoshu;
    @BindView(R.id.tv_checkstatus_phone)
    TextView tvCheckstatusPhone;
    private int flag;
    private int previous;

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
        MApplication.listAppoint1.add(this);
        flag = getIntent().getIntExtra("flag", 0);
        //获取是设置密码还是去更换手机号状态
        previous = getIntent().getIntExtra("previous",0);
//        if (flag == 0){
//            //更换手机号
//            previous = Global.SETREPLACEPHONE_TO_VERIFCODE;
//        }else {
//            previous = Global.PAYPWD_TO_VERIFCODE;
//        }
    }

    private void findView() {
        setContentView(R.layout.activity_check_status);
        ButterKnife.bind(this);
    }

    private void setView() {
        Utils.setText(tvCheckstatusPhone, "+86 " + spUtil.getString("cellphone", ""), "", View.VISIBLE, View.VISIBLE);
//        if (flag == 0 || flag == 2) {//初次设置密码核验身份
        if (flag == Global.FIRST_SET_PASSWORD || flag == Global.UPDATE_PASSWORD) {//初次设置密码核验身份
            tvCheckstatusTitle.setText("请校验手机号码");
            tvCheckstatusMiaoshu.setText("保证您的账户安全，请核验与宠物家账号绑定的手机号码");
            llCheckstatusBottom.setVisibility(View.VISIBLE);
        } else if (flag == Global.FORGET_PASSWORD) {//忘记密码核验身份
            tvCheckstatusTitle.setText("忘记支付密码");
            tvCheckstatusMiaoshu.setText("为了您的资金安全，请先校验手机号码，校验成功");
            llCheckstatusBottom.setVisibility(View.GONE);
        }
    }

    private void setLinster() {

    }

    private void getData() {

    }

    @OnClick({R.id.tv_checkstatus_kfphone, R.id.ib_checkstatus_back, R.id.sl_checkstatus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_checkstatus_kfphone:
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.telePhoneBroadcast(mContext, "4000300011");
                    }
                }).show();
                break;
            case R.id.ib_checkstatus_back:
                finish();
                break;
            case R.id.sl_checkstatus:
                String slat_md5 = MD5.md5(Global.MD5_STR, spUtil.getString("cellphone", ""));
                Log.e("TAG", "slat_md5 = " + slat_md5);
                mPDialog.showDialog();
                CommUtil.genVerifyCode(this, spUtil.getString("cellphone", ""), slat_md5,
                        0, codeHandler);
                break;
        }
    }

    private AsyncHttpResponseHandler codeHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObj = new JSONObject(new String(responseBody));
                int code = jsonObj.getInt("code");
                String msg = jsonObj.getString("msg");
                if (code == 0) {
                    startActivity(new Intent(mContext, VerificationCodeActivity.class)
                            .putExtra("phone", spUtil.getString("cellphone", ""))
                            .putExtra("flag", flag)
                            .putExtra("isGoSetFinger", getIntent().getBooleanExtra("isGoSetFinger", false))
                            .putExtra("previous",previous));
                    finish();
                } else {
                    ToastUtil.showToastShort(mContext, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShort(mContext, "数据异常");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(mContext, "请求失败");
        }
    };

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
