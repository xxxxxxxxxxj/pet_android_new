package com.haotang.pet.adapter;

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
 * @date zhoujunxia on 2019/3/22 17:59
 */
public class CardShopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CardShopAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        TextView tv_item_cardshop_name = helper.getView(R.id.tv_item_cardshop_name);
        Utils.setText(tv_item_cardshop_name, item, "", View.VISIBLE, View.VISIBLE);
    }
}