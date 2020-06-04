package com.haotang.pet.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-01-07 15:07
 */
public class CardDiscountAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CardDiscountAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        TextView tv_item_card_discount = helper.getView(R.id.tv_item_card_discount);
        if (Utils.isStrNull(item)) {
            tv_item_card_discount.setVisibility(View.VISIBLE);
            if (item.contains("@@")) {
                String[] split = item.split("@@");
                if (split != null && split.length > 0 && split.length % 2 != 0) {
                    SpannableString ss = new SpannableString(item.replace("@@", ""));
                    if (split.length == 3) {
                        int startIndex = split[0].length();
                        int endIndex = split[0].length() + split[1].length();
                        ss.setSpan(new TextAppearanceSpan(mContext, R.style.discount_style_yellow), startIndex,
                                endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                    tv_item_card_discount.setText(ss);
                }
            }
        } else {
            tv_item_card_discount.setVisibility(View.GONE);
        }
    }
}