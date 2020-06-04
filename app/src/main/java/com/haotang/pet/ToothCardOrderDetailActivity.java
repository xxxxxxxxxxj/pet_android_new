package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToothCardOrderDetailActivity extends SuperActivity {
    public static SuperActivity act;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.placeholder)
    ImageView placeholder;
    @BindView(R.id.tv_cardorder_state)
    TextView tvCardorderState;
    @BindView(R.id.rl_toothcard_bind)
    RelativeLayout rlToothcardBind;
    @BindView(R.id.iv_sure_order)
    ImageView ivSureOrder;
    @BindView(R.id.tv_toothcard_title)
    TextView tvToothcardTitle;
    @BindView(R.id.ll_giftcard_discount)
    LinearLayout llGiftcardDiscount;
    @BindView(R.id.rl_giftcard_title)
    RelativeLayout rlGiftcardTitle;
    @BindView(R.id.tv_toothcard_name)
    TextView tvToothcardName;
    @BindView(R.id.tv_thoothcard_faceprice)
    TextView tvThoothcardFaceprice;
    @BindView(R.id.tv_thoothcard_ordernum)
    TextView tvThoothcardOrdernum;
    @BindView(R.id.tv_copy_order)
    TextView tvCopyOrder;
    @BindView(R.id.tv_toothcard_bindcontent)
    TextView tvBindContent;
    @BindView(R.id.tv_card_paytime)
    TextView tvCardPaytime;
    @BindView(R.id.tv_card_paytype)
    TextView tvCardPaytype;
    @BindView(R.id.tv_card_phone)
    TextView tvCardPhone;
    @BindView(R.id.iv_orderdetail_call)
    ImageView ivOrderdetailCall;
    @BindView(R.id.tv_toothcard_payprice)
    TextView tvCardPayPrice;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_thoothcard_refundprice)
    TextView tvThoothcardRefundprice;
    @BindView(R.id.rl_toothcard_refund)
    RelativeLayout rlToothcardRefund;
    private int cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        getData();
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.extraCardOrderInfo(mContext, cardId, infoHandler);
    }

    private String tradeNo;
    private String phone;
    private AsyncHttpResponseHandler infoHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("smallPic") && !jdata.isNull("smallPic")) {
                            GlideUtil.loadImg(mContext, jdata.getString("smallPic"), ivSureOrder, R.drawable.icon_production_default);
                        }
                        if (jdata.has("cardName") && !jdata.isNull("cardName")) {
                            tvToothcardTitle.setText(jdata.getString("cardName"));
                            tvToothcardName.setText(jdata.getString("cardName"));
                        }
                        if (jdata.has("tagNames") && !jdata.isNull("tagNames")) {
                            JSONArray tagNames = jdata.getJSONArray("tagNames");
                            llGiftcardDiscount.removeAllViews();
                            for (int i = 0; i < tagNames.length(); i++) {
                                View view = View.inflate(mContext, R.layout.item_card_bq, null);
                                TextView tvTag = view.findViewById(R.id.tv_item_card_bq);
                                tvTag.setText(tagNames.getString(i));
                                llGiftcardDiscount.addView(view);
                            }
                        }
                        if (jdata.has("totalPrice") && !jdata.isNull("totalPrice")) {
                            tvThoothcardFaceprice.setText("¥" + jdata.getDouble("totalPrice"));
                        }
                        if (jdata.has("payPrice") && !jdata.isNull("payPrice")) {
                            tvCardPayPrice.setText("¥" + jdata.getDouble("payPrice"));
                        }
                        if (jdata.has("tradeNo") && !jdata.isNull("tradeNo")) {
                            tradeNo = jdata.getString("tradeNo");
                            tvThoothcardOrdernum.setText(tradeNo);
                        }
                        if (jdata.has("kfPhone") && !jdata.isNull("kfPhone")) {
                            phone = jdata.getString("kfPhone");
                            tvCardPhone.setText(phone);
                        }
                        if (jdata.has("payTime") && !jdata.isNull("payTime")) {
                            tvCardPaytime.setText(jdata.getString("payTime"));
                        }
                        if (jdata.has("payWay") && !jdata.isNull("payWay")) {
                            tvCardPaytype.setText(jdata.getString("payWay"));
                        }
                        if (jdata.has("stateTxt") && !jdata.isNull("stateTxt")) {
                            tvCardorderState.setText(jdata.getString("stateTxt"));
                        }
                        if (jdata.has("bindPetId") && !jdata.isNull("bindPetId")) {
                            if (jdata.getInt("bindPetId") > 0) {
                                rlToothcardBind.setClickable(false);
                                rlToothcardBind.setBackgroundResource(R.drawable.bg_f0f5f9_round);
                                tvBindContent.setTextColor(Color.parseColor("#FF3A1E"));
                                tvBindContent.setText(jdata.getString("petText"));
                            } else {
                                rlToothcardBind.setClickable(true);
                                rlToothcardBind.setBackgroundResource(R.drawable.bg_round22_ff3a1e);
                                tvBindContent.setTextColor(Color.parseColor("#FFFFFF"));
                            }
                        } else {
                            rlToothcardBind.setClickable(true);
                            rlToothcardBind.setBackgroundResource(R.drawable.bg_round22_ff3a1e);
                            tvBindContent.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        if (jdata.has("refundPrice")&&!jdata.isNull("refundPrice")){
                            double refundPrice = jdata.getDouble("refundPrice");                            if (refundPrice>0){
                                rlToothcardRefund.setVisibility(View.VISIBLE);
                                tvThoothcardRefundprice.setText("¥"+refundPrice);
                                rlToothcardBind.setClickable(false);
                                rlToothcardBind.setBackgroundResource(R.drawable.bg_f0f5f9_round);
                                tvBindContent.setTextColor(Color.parseColor("#FF3A1E"));
                            }else {
                                rlToothcardRefund.setVisibility(View.GONE);
                            }
                        }else {
                            rlToothcardRefund.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void initData() {
        act = this;
        MApplication.listAppoint.add(this);
        Intent intent = getIntent();
        cardId = intent.getIntExtra("id", 0);
    }

    private void setView() {
        setContentView(R.layout.activity_tooth_card_detail);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("9.9元刷牙年卡");
    }

    @OnClick({R.id.ib_titlebar_back, R.id.rl_toothcard_bind, R.id.tv_copy_order, R.id.iv_orderdetail_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.rl_toothcard_bind:
                Intent intent = new Intent(mContext, ChooseFosterPetActivity.class);
                intent.putExtra("cardId", cardId);
                intent.putExtra("previous", Global.BUYCARD_TO_CHOOSEPET);
                startActivity(intent);
                break;
            case R.id.tv_copy_order:
                Utils.copy(tradeNo, mContext);
                ToastUtil.showToastShortBottom(mContext,"复制成功");
                break;
            case R.id.iv_orderdetail_call:
                Utils.callToPhone(phone, mContext);
                break;
        }
    }
}
