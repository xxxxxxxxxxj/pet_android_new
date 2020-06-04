package com.haotang.pet.adapter;

import android.graphics.Typeface;
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
 * @date zhoujunxia on 2019/4/6 19:31
 */
public class PaysuccessDiscountInfoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PaysuccessDiscountInfoAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        TextView tv_item_paysuccess_discountinfo = helper.getView(R.id.tv_item_paysuccess_discountinfo);
        Utils.setText(tv_item_paysuccess_discountinfo, item, "", View.VISIBLE, View.VISIBLE);
        if (helper.getLayoutPosition() == 0) {
            tv_item_paysuccess_discountinfo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            tv_item_paysuccess_discountinfo.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
        } else {
            tv_item_paysuccess_discountinfo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
            tv_item_paysuccess_discountinfo.setTextColor(mContext.getResources().getColor(R.color.a999999));
        }
    }
}
