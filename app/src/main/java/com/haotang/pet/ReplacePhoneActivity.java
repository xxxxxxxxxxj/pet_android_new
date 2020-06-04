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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MD5;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ShadowLayout;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更换绑定账号界面
 */
public class ReplacePhoneActivity extends SuperActivity {
    @BindView(R.id.et_replacephone)
    EditText etReplacephone;
    @BindView(R.id.tv_replacephone_error)
    TextView tvReplacephoneError;
    @BindView(R.id.ll_replacephone_phone)
    LinearLayout llReplacephonePhone;
    @BindView(R.id.sl_replacephone)
    ShadowLayout slReplacephone;

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
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_replace_phone);
        ButterKnife.bind(this);
    }

    private void setView() {

    }

    private void setLinster() {
        etReplacephone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    slReplacephone.setVisibility(View.GONE);
                    tvReplacephoneError.setVisibility(View.INVISIBLE);
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
                        etReplacephone.setText(sb.toString());
                        etReplacephone.setSelection(index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                    Utils.goneJP(mContext);
                    boolean bool = Utils.checkPhone1(mContext, etReplacephone);
                    if (bool) {
                        mPDialog.showDialog();
                        CommUtil.canReplacePhone(mContext,spUtil.getString("cellphone", ""),Utils.checkEditText(etReplacephone).replace(" ", ""),Global.getIMEI(mContext),spUtil.getInt("userid", 0),canReplacePhoneHandler);
                    } else {
                        slReplacephone.setVisibility(View.GONE);
                        llReplacephonePhone.startAnimation(shakeAnimation(5));
                        tvReplacephoneError.setVisibility(View.VISIBLE);
                    }
                } else {
                    slReplacephone.setVisibility(View.GONE);
                }
            }
        });
    }

    private AsyncHttpResponseHandler canReplacePhoneHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code==0){
                    slReplacephone.setVisibility(View.VISIBLE);
                }else {
                    ToastUtil.showToastShortBottom(mContext,msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void getData() {

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

    @OnClick({R.id.ib_replacephone_back, R.id.sl_replacephone, R.id.iv_replacephone_clearphone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_replacephone_clearphone:
                etReplacephone.setText("");
                llReplacephonePhone.startAnimation(shakeAnimation(5));
                break;
            case R.id.ib_replacephone_back:
                finish();
                break;
            case R.id.sl_replacephone:
                if (Utils.isStrNull(Utils.checkEditText(etReplacephone).replace(" ", ""))) {
                    String slat_md5 = MD5.md5(Global.MD5_STR, Utils.checkEditText(etReplacephone).replace(" ", ""));
                    Log.e("TAG", "slat_md5 = " + slat_md5);
                    mPDialog.showDialog();
                    CommUtil.genVerifyCode(this, Utils.checkEditText(etReplacephone).replace(" ", ""), slat_md5,
                            0, codeHandler);
                } else {
                    ToastUtil.showToastShortCenter(mContext, "请输入手机号码");
                }
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
                            .putExtra("phone", Utils.checkEditText(etReplacephone).replace(" ", ""))
                            .putExtra("previous", Global.REPLACEPHONE_TO_VERIFCODE));
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
}
