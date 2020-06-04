package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.AppointRechargeEvent;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CardInfoDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定新卡界面
 */
public class BindingCardActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.et_bindingcard)
    EditText etBindingcard;
    private int confirm;
    private String confirmTip;
    private MyCard myCard = new MyCard();

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
        setContentView(R.layout.activity_binding_card);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("绑定E卡");
    }

    private void setLinster() {

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

    @OnClick({R.id.ib_titlebar_back, R.id.btn_bindingcard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_bindingcard:
                if (Utils.isStrNull(Utils.checkEditText(etBindingcard))) {
                    confirm = 0;
                    bindCard();
                } else {
                    ToastUtil.showToastShortBottom(this, "请输入卡密码");
                }
                break;
        }
    }

    private void bindCard() {
        mPDialog.showDialog();
        CommUtil.bindCard(Utils.checkEditText(etBindingcard), confirm,
                BindingCardActivity.this, bindCardHanler);
    }

    private AsyncHttpResponseHandler bindCardHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (confirm == 0) {
                        if (jobj.has("data") && !jobj.isNull("data")) {
                            JSONObject jdata = jobj.getJSONObject("data");
                            if (jdata.has("confirmTip") && !jdata.isNull("confirmTip")) {
                                confirmTip = jdata.getString("confirmTip");
                            }
                            if (jdata.has("card") && !jdata.isNull("card")) {
                                JSONObject jcard = jdata.getJSONObject("card");
                                if (jcard.has("cardNum") && !jcard.isNull("cardNum")) {
                                    myCard.setCardNumber(jcard.getString("cardNum"));
                                }
                                if (jcard.has("expireTime") && !jcard.isNull("expireTime")) {
                                    myCard.setExpireTime(jcard.getString("expireTime"));
                                }
                                if (jcard.has("faceValue") && !jcard.isNull("faceValue")) {
                                    myCard.setAmount(jcard.getDouble("faceValue"));
                                }
                                if (jcard.has("discount") && !jcard.isNull("discount")) {
                                    myCard.setDiscountText(jcard.getString("discount"));
                                }
                                if (jcard.has("title") && !jcard.isNull("title")) {
                                    myCard.setCardTypeName(jcard.getString("title"));
                                }
                                if (jcard.has("mineCardPic") && !jcard.isNull("mineCardPic")) {
                                    myCard.setMineCardPic(jcard.getString("mineCardPic"));
                                }
                            }
                            if (Utils.isStrNull(confirmTip)) {
                                new AlertDialogNavAndPost(BindingCardActivity.this).builder()
                                        .setTitle("")
                                        .setMsg(confirmTip)
                                        .setNegativeButton("取消", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        }).setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CardInfoDialog cardInfoDialog = new CardInfoDialog.Builder(BindingCardActivity.this)
                                                .setType(MDialog.DIALOGTYPE_ALERT)
                                                .setData(myCard)
                                                .setCancelable(true).positiveListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm = 1;
                                                        bindCard();
                                                    }
                                                })
                                                .build();
                                        cardInfoDialog.show();
                                    }
                                }).show();
                            } else {
                                CardInfoDialog cardInfoDialog = new CardInfoDialog.Builder(BindingCardActivity.this)
                                        .setType(MDialog.DIALOGTYPE_ALERT)
                                        .setData(myCard)
                                        .setCancelable(true).positiveListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                confirm = 1;
                                                bindCard();
                                            }
                                        })
                                        .build();
                                cardInfoDialog.show();
                            }
                        }
                    } else if (confirm == 1) {
                        etBindingcard.setText("");
                        EventBus.getDefault().post(new AppointRechargeEvent(true));
                        ToastUtil.showToastShort(BindingCardActivity.this,
                                "绑卡成功");
                        finish();
                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(BindingCardActivity.this,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
                ToastUtil.showToastShort(BindingCardActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(BindingCardActivity.this, "请求失败");
        }
    };
}
