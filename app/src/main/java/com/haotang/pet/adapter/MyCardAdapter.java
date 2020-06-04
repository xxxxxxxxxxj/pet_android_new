package com.haotang.pet.adapter;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.ECardUseDetailActivity;
import com.haotang.pet.MyUnAvialCardActivity;
import com.haotang.pet.R;
import com.haotang.pet.ToothCardUseDetailActivity;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/21 11:30
 */
public class MyCardAdapter extends BaseQuickAdapter<MyCard, BaseViewHolder> {
    private final int type;

    public MyCardAdapter(int layoutResId, List<MyCard> data, int type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyCard item) {
        TextView tv_item_mycard_amount = helper.getView(R.id.tv_item_mycard_amount);
        RoundedImageView iv_item_mycard_bg = helper.getView(R.id.iv_item_mycard_bg);
        TextView tv_item_mycard_name = helper.getView(R.id.tv_item_mycard_name);
        View vw_item_mycard_info = helper.getView(R.id.vw_item_mycard_info);
        LinearLayout ll_item_mycard_info = helper.getView(R.id.ll_item_mycard_info);
        RelativeLayout rl_item_mycard = helper.getView(R.id.rl_item_mycard);
        TextView tv_item_mycard_bky = helper.getView(R.id.tv_item_mycard_bky);
        TextView tv_item_mycard_discounttext = helper.getView(R.id.tv_item_mycard_discounttext);
        TextView tv_item_mycard_state = helper.getView(R.id.tv_item_mycard_state);
        vw_item_mycard_info.setVisibility(View.GONE);
        if (item != null) {
            if (item.isLast()) {
                if (type == 1) {
                    tv_item_mycard_bky.setVisibility(View.VISIBLE);
                    tv_item_mycard_bky.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(new Intent(mContext, MyUnAvialCardActivity.class));
                        }
                    });
                } else {
                    tv_item_mycard_bky.setVisibility(View.GONE);
                }
                rl_item_mycard.setBackgroundResource(R.drawable.bg_round_white10);
                ll_item_mycard_info.setVisibility(View.GONE);
                iv_item_mycard_bg.setVisibility(View.GONE);
            } else {
                vw_item_mycard_info.setVisibility(View.VISIBLE);
                tv_item_mycard_bky.setVisibility(View.GONE);
                iv_item_mycard_bg.setVisibility(View.VISIBLE);
                rl_item_mycard.setBackgroundResource(R.drawable.icon_card_bgshade);
                ll_item_mycard_info.setVisibility(View.VISIBLE);
                if (item.getCardType() == 1) {//E卡
                    tv_item_mycard_amount.setVisibility(View.VISIBLE);
                    SpannableString ss1 = new SpannableString("¥" + item.getAmount());
                    ss1.setSpan(new TextAppearanceSpan(mContext, R.style.style2), 1,
                            ss1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_mycard_amount.setText(ss1);
                    if (type == 2) {
                        tv_item_mycard_state.setVisibility(View.VISIBLE);
                        Utils.setText(tv_item_mycard_state, item.getState(), "", View.VISIBLE, View.VISIBLE);
                    } else {
                        tv_item_mycard_state.setVisibility(View.GONE);
                    }
                } else if (item.getCardType() == 2) {//刷牙卡
                    tv_item_mycard_state.setVisibility(View.VISIBLE);
                    Utils.setText(tv_item_mycard_state, item.getExpireTime(), "", View.VISIBLE, View.VISIBLE);
                    if (type == 1) {
                        tv_item_mycard_amount.setVisibility(View.GONE);
                    } else {
                        tv_item_mycard_amount.setVisibility(View.VISIBLE);
                        Utils.setText(tv_item_mycard_amount, item.getState(), "", View.VISIBLE, View.VISIBLE);
                    }
                }
                if (Utils.isStrNull(item.getDicountDesc())) {
                    if (item.getDicountDesc().contains("@@")) {
                        String[] split = item.getDicountDesc().split("@@");
                        if (split != null && split.length > 0 && split.length % 2 != 0) {
                            SpannableString ss = new SpannableString(item.getDicountDesc().replace("@@", ""));
                            if (split.length == 3) {
                                int startIndex = split[0].length();
                                int endIndex = split[0].length() + split[1].length();
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), startIndex,
                                        endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            } else if (split.length == 5) {
                                int startIndex = split[0].length();
                                int endIndex = startIndex + split[1].length();
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), startIndex,
                                        endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                int startIndex1 = endIndex + split[2].length();
                                int endIndex1 = startIndex1 + split[3].length();
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), startIndex1,
                                        endIndex1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex1, endIndex1,
                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                            tv_item_mycard_discounttext.setText(ss);
                        }
                    } else {
                        Utils.setText(tv_item_mycard_discounttext, item.getDicountDesc(), "", View.VISIBLE, View.VISIBLE);
                    }
                }
                GlideUtil.loadImg(mContext, item.getMineCardPic(), iv_item_mycard_bg, R.drawable.icon_production_default);
                Utils.setText(tv_item_mycard_name, item.getCardTypeName(), "", View.VISIBLE, View.VISIBLE);
                vw_item_mycard_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getCardType() == 1) {//E卡
                            Intent intent = new Intent(mContext, ECardUseDetailActivity.class);
                            intent.putExtra("id", item.getId());
                            mContext.startActivity(intent);
                        } else if (item.getCardType() == 2) {//刷牙卡
                            Intent intent = new Intent(mContext, ToothCardUseDetailActivity.class);
                            intent.putExtra("id", item.getId());
                            intent.putExtra("mineCardPic", item.getMineCardPic());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
