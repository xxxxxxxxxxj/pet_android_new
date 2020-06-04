package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.GoodsBill;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/27 12:00
 */
public class BillInfoDialogInfoAdapter extends BaseQuickAdapter<GoodsBill, BaseViewHolder> {

    public BillInfoDialogInfoAdapter(int layoutResId, List<GoodsBill> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, GoodsBill item) {
        TextView tv_item_billinfodialog_info_name = helper.getView(R.id.tv_item_billinfodialog_info_name);
        TextView tv_item_billinfodialog_info_price = helper.getView(R.id.tv_item_billinfodialog_info_price);
        ImageView iv_item_billinfodialog_info = helper.getView(R.id.iv_item_billinfodialog_info);
        if (item != null) {
            GlideUtil.loadCircleImg(mContext, item.getIcon(),iv_item_billinfodialog_info , R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_billinfodialog_info_name, item.getName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_billinfodialog_info_price, item.getPrice(), "", View.VISIBLE, View.VISIBLE);
        }
    }
}