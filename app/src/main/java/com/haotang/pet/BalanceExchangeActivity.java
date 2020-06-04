package com.haotang.pet;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.AppointRechargeEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ExchangeSuccessDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 兑换码界面
 */
public class BalanceExchangeActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.et_balanceexchange_dhm)
    EditText etBalanceexchangeDhm;
    @BindView(R.id.btn_balanceexchange)
    Button btnBalanceexchange;
    private String dhm;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_balance_exchange);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("兑换码充值");
        bankCardNumAddSpace(etBalanceexchangeDhm);
    }

    private void setLinster() {

    }

    /**
     * 银行卡四位加空格
     *
     * @param mEditText
     */
    protected void bankCardNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;
            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                dhm = buffer.toString().trim().replaceAll(" ", "");
                if (s.toString().trim().length() == 19) {
                    checkRedeemCode();
                    return;
                } else {
                    btnBalanceexchange.setText("立即充值");
                }
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }
                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }
                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }
                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }
                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    private void checkRedeemCode() {
        mPDialog.showDialog();
        CommUtil.checkDhmCode(spUtil.getString("cellphone", ""), dhm, this,
                checkDhmCodeHanler);
    }

    private AsyncHttpResponseHandler checkDhmCodeHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("amount") && !jdata.isNull("amount")) {
                            amount = jdata.getInt("amount");
                            btnBalanceexchange.setText("立即充值    " + amount
                                    + "元");
                        }
                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(BalanceExchangeActivity.this,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(BalanceExchangeActivity.this, "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.btn_balanceexchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_balanceexchange:
                if (Utils.isStrNull(dhm)) {
                    mPDialog.showDialog();
                    CommUtil.rechargeWithCard(
                            spUtil.getString("cellphone", ""), dhm,
                            BalanceExchangeActivity.this, rechargeWithCardHanler);
                } else {
                    ToastUtil.showToastShortBottom(this, "请输入兑换码");
                }
                break;
        }
    }

    private AsyncHttpResponseHandler rechargeWithCardHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    etBalanceexchangeDhm.setText("");
                    EventBus.getDefault().post(new AppointRechargeEvent(true));
                    new ExchangeSuccessDialog(BalanceExchangeActivity.this).builder().setTitle("兑换成功")
                            .setMsg("¥ " + amount)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(BalanceExchangeActivity.this,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(BalanceExchangeActivity.this, "请求失败");
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
