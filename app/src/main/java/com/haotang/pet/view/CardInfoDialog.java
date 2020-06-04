package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/23 12:26
 */
public class CardInfoDialog extends Dialog {
    public static int DIALOGTYPE_ALERT = 1;
    public static int DIALOGTYPE_CONFIRM = 2;
    private int nDialogType = DIALOGTYPE_ALERT;
    private Context mContext;
    private TextView tvTitle;
    private MyCard myCard;
    private TextView tvOk;
    private ImageView iv_cardinfo_dialog_cardbg;
    private TextView tv_cardinfo_dialog_name;
    private TextView tv_cardinfo_dialog_zk;
    private TextView tv_cardinfo_dialog_yxq;
    private TextView tv_cardinfo_dialog_no;
    private TextView tv_cardinfo_dialog_amount;
    private android.view.View.OnClickListener positive_listener;
    private android.view.View.OnClickListener default_positive_listener = new android.view.View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CardInfoDialog.this.dismiss();
            if (null != positive_listener)
                positive_listener.onClick(v);
        }
    };

    public CardInfoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cardinfo_dialog);
        initEnvironment();
        initControls();
    }

    private void initEnvironment() {
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
    }

    private void initControls() {
        tvTitle = (TextView) findViewById(R.id.tv_cardinfo_dialog_title);
        tvOk = (TextView) findViewById(R.id.tv_cardinfo_dialog_qr);
        iv_cardinfo_dialog_cardbg = (ImageView) findViewById(R.id.iv_cardinfo_dialog_cardbg);
        tv_cardinfo_dialog_name = (TextView) findViewById(R.id.tv_cardinfo_dialog_name);
        tv_cardinfo_dialog_zk = (TextView) findViewById(R.id.tv_cardinfo_dialog_zk);
        tv_cardinfo_dialog_yxq = (TextView) findViewById(R.id.tv_cardinfo_dialog_yxq);
        tv_cardinfo_dialog_amount = (TextView) findViewById(R.id.tv_cardinfo_dialog_amount);
        tv_cardinfo_dialog_no = (TextView) findViewById(R.id.tv_cardinfo_dialog_no);
        if (myCard != null) {
            Utils.setText(tv_cardinfo_dialog_no, myCard.getCardNumber(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_cardinfo_dialog_yxq, myCard.getExpireTime(), "", View.VISIBLE, View.VISIBLE);
            SpannableString ss1 = new SpannableString("¥" + myCard.getAmount());
            ss1.setSpan(new TextAppearanceSpan(mContext, R.style.ershiliu_normal), 1,
                    ss1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_cardinfo_dialog_amount.setText(ss1);
            if (Utils.isStrNull(myCard.getDiscountText())) {
                if (myCard.getDiscountText().contains("@@")) {
                    String[] split = myCard.getDiscountText().split("@@");
                    if (split != null && split.length > 0 && split.length % 2 != 0) {
                        SpannableString ss = new SpannableString(myCard.getDiscountText().replace("@@", ""));
                        if (split.length == 3) {
                            int startIndex = split[0].length();
                            int endIndex = split[0].length() + split[1].length();
                            ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), startIndex,
                                    endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            tv_cardinfo_dialog_zk.setText(ss);
                        }
                    }
                }
            }
            GlideUtil.loadRoundImg(mContext, myCard.getMineCardPic(), iv_cardinfo_dialog_cardbg, R.drawable.icon_production_default, 20);
            Utils.setText(tv_cardinfo_dialog_name, myCard.getCardTypeName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvTitle, "确认E卡信息", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvOk, "确认绑定", "", View.VISIBLE, View.VISIBLE);
            tvOk.setOnClickListener(default_positive_listener);
        }
    }

    public void setDialogType(int nDialogType) {
        this.nDialogType = nDialogType;
    }

    private void setData(MyCard myCard) {
        this.myCard = myCard;
    }

    public void setPositiveListener(View.OnClickListener positive_listener) {
        this.positive_listener = positive_listener;
    }

    public static class Builder {
        private Context mContext;
        private int nDialogType = DIALOGTYPE_ALERT;
        private boolean cancelable = true;
        private View.OnClickListener positive_listener;
        private MyCard myCard;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setType(int nType) {
            this.nDialogType = nType;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder positiveListener(View.OnClickListener positiveListener) {
            this.positive_listener = positiveListener;
            return this;
        }

        public CardInfoDialog build() {
            if (null == mContext)
                return null;
            CardInfoDialog md = new CardInfoDialog(mContext);
            md.setDialogType(nDialogType);
            md.setData(myCard);
            md.setCancelable(cancelable);
            md.setPositiveListener(positive_listener);
            return md;
        }

        public Builder setData(MyCard myCard) {
            this.myCard = myCard;
            return this;
        }
    }
}