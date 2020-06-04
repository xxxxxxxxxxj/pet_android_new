package com.haotang.pet.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/23 11:47
 */
public class SelectCardAdapter extends BaseQuickAdapter<MyCard, BaseViewHolder> {

    public SelectCardAdapter(int layoutResId, List<MyCard> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MyCard item) {
        ImageView iv_item_selectcard_select = helper.getView(R.id.iv_item_selectcard_select);
        ImageView iv_item_selectcard_cardimg = helper.getView(R.id.iv_item_selectcard_cardimg);
        TextView tv_item_selectcard_cardname = helper.getView(R.id.tv_item_selectcard_cardname);
        TextView tv_item_selectcard_amount = helper.getView(R.id.tv_item_selectcard_amount);
        TextView tv_item_selectcard_zk = helper.getView(R.id.tv_item_selectcard_zk);
        if (item != null) {
            if (item.isSelect()) {
                iv_item_selectcard_select.setImageResource(R.drawable.icon_petadd_select);
            } else {
                iv_item_selectcard_select.setImageResource(R.drawable.icon_petadd_unselect);
            }
            GlideUtil.loadImg(mContext, item.getSmallPic(), iv_item_selectcard_cardimg, R.drawable.icon_production_default);
            Utils.setText(tv_item_selectcard_cardname, item.getCardTypeName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_selectcard_amount, "余额 ¥ " + item.getAmount(), "", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(item.getDiscountText())) {
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
                            tv_item_selectcard_zk.setText(ss);
                        }
                    }
                }
            }
        }
    }
}