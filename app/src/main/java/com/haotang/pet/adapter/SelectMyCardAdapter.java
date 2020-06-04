package com.haotang.pet.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
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
 * @date zhoujunxia on 2019/3/23 18:24
 */
public class SelectMyCardAdapter extends BaseQuickAdapter<MyCard, BaseViewHolder> {
    private boolean isHaveUnAvailCard;

    public SelectMyCardAdapter(int layoutResId, List<MyCard> data, boolean haveUnAvailCard) {
        super(layoutResId, data);
        this.isHaveUnAvailCard = haveUnAvailCard;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyCard item) {
        TextView tv_item_selectmycard_amount = helper.getView(R.id.tv_item_selectmycard_amount);
        RoundedImageView iv_item_selectmycard_bg = helper.getView(R.id.iv_item_selectmycard_bg);
        TextView tv_item_selectmycard_name = helper.getView(R.id.tv_item_selectmycard_name);
        TextView tv_item_selectmycard_text = helper.getView(R.id.tv_item_selectmycard_text);
        TextView tv_item_mycard_discounttext = helper.getView(R.id.tv_item_mycard_discounttext);
        TextView tv_item_selectmycard_reason = helper.getView(R.id.tv_item_selectmycard_reason);
        ImageView iv_item_selectmycard_select = helper.getView(R.id.iv_item_selectmycard_select);
        LinearLayout ll_item_selectmycard_info = helper.getView(R.id.ll_item_selectmycard_info);
        RelativeLayout rl_item_selectmycard = helper.getView(R.id.rl_item_selectmycard);
        LinearLayout layout_is_show_unused = helper.getView(R.id.layout_is_show_unused);
        if (item != null) {
            if (item.isLast()) {
                Log.e("TAG", "isLast");
                layout_is_show_unused.setVisibility(View.GONE);
                rl_item_selectmycard.setBackgroundResource(R.drawable.bg_round_white10);
                ll_item_selectmycard_info.setVisibility(View.GONE);
                iv_item_selectmycard_bg.setVisibility(View.GONE);
                if (isHaveUnAvailCard) {
                    Log.e("TAG", "oj");
                    Log.e("TAG", "helper.getLayoutPosition() = " + helper.getLayoutPosition());
                    Log.e("TAG", "mData.size() = " + mData.size());
                    tv_item_selectmycard_reason.setVisibility(View.VISIBLE);
                    Utils.setText(tv_item_selectmycard_reason, "." + mData.get(mData.size() - 2).getReason(), "", View.VISIBLE, View.VISIBLE);
                } else {
                    tv_item_selectmycard_reason.setVisibility(View.GONE);
                }
            } else {
                if (item.isShow()) {
                    layout_is_show_unused.setVisibility(View.VISIBLE);
                } else {
                    layout_is_show_unused.setVisibility(View.GONE);
                }
                iv_item_selectmycard_bg.setVisibility(View.VISIBLE);
                ll_item_selectmycard_info.setVisibility(View.VISIBLE);
                if (item.isAvail()) {
                    rl_item_selectmycard.setBackgroundResource(R.drawable.icon_card_bgshade);
                    iv_item_selectmycard_bg.setAlpha(1.0f);
                    tv_item_selectmycard_amount.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_item_selectmycard_name.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_item_selectmycard_text.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_item_mycard_discounttext.setTextColor(mContext.getResources().getColor(R.color.white));
                    iv_item_selectmycard_select.setVisibility(View.VISIBLE);
                    tv_item_selectmycard_reason.setVisibility(View.GONE);
                    if (item.isSelect()) {
                        iv_item_selectmycard_select.setImageResource(R.drawable.icon_petadd_select);
                    } else {
                        iv_item_selectmycard_select.setImageResource(R.drawable.icon_petadd_unselect);
                    }
                } else {
                    rl_item_selectmycard.setBackgroundResource(R.drawable.bg_unvail_card);
                    iv_item_selectmycard_bg.setAlpha(0.3f);
                    tv_item_selectmycard_amount.setTextColor(mContext.getResources().getColor(R.color.a999999));
                    tv_item_selectmycard_name.setTextColor(mContext.getResources().getColor(R.color.a999999));
                    tv_item_selectmycard_text.setTextColor(mContext.getResources().getColor(R.color.a999999));
                    tv_item_mycard_discounttext.setTextColor(mContext.getResources().getColor(R.color.a999999));
                    iv_item_selectmycard_select.setVisibility(View.GONE);
                    if (item.getPosition() > 0) {
                        tv_item_selectmycard_reason.setVisibility(View.VISIBLE);
                        Utils.setText(tv_item_selectmycard_reason, "." + mData.get(helper.getLayoutPosition() - 1).getReason(), "", View.VISIBLE, View.VISIBLE);
                    } else {
                        tv_item_selectmycard_reason.setVisibility(View.GONE);
                    }
                }
                SpannableString ss1 = new SpannableString("¥" + item.getAmount());
                ss1.setSpan(new TextAppearanceSpan(mContext, R.style.style2), 1,
                        ss1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                tv_item_selectmycard_amount.setText(ss1);
                if (item.getId() == 1000) {
                    tv_item_mycard_discounttext.setVisibility(View.GONE);
                    if (Utils.isStrNull(item.getDiscountText())) {
                        tv_item_selectmycard_text.setVisibility(View.VISIBLE);
                        if (item.getDiscountText().contains("@@")) {
                            String[] split = item.getDiscountText().split("@@");
                            if (split != null && split.length > 0 && split.length % 2 != 0) {
                                SpannableString ss = new SpannableString(item.getDiscountText().replace("@@", ""));
                                if (split.length == 3) {
                                    int startIndex = split[0].length();
                                    int endIndex = split[0].length() + split[1].length();
                                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), startIndex,
                                            endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                    ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                }
                                tv_item_selectmycard_text.setText(ss);
                            }
                        }
                    } else {
                        tv_item_selectmycard_text.setVisibility(View.GONE);
                    }
                } else {
                    tv_item_selectmycard_text.setVisibility(View.GONE);
                    if (Utils.isStrNull(item.getDicountDesc())) {
                        tv_item_mycard_discounttext.setVisibility(View.VISIBLE);
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
                        }
                    } else {
                        tv_item_mycard_discounttext.setVisibility(View.GONE);
                    }
                }
                GlideUtil.loadImg(mContext, item.getMineCardPic(), iv_item_selectmycard_bg, R.drawable.icon_production_default);
                Utils.setText(tv_item_selectmycard_name, item.getCardTypeName(), "", View.VISIBLE, View.VISIBLE);
            }
        }
    }
}