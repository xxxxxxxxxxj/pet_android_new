package com.haotang.pet.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.util.GlideUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-19 13:37
 */
class OrderPetAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OrderPetAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        ImageView niv_item_order_petimg = helper.getView(R.id.niv_item_order_petimg);
        GlideUtil.loadImg(mContext, item, niv_item_order_petimg, R.drawable.icon_production_default);
    }
}