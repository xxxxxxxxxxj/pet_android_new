package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterAddCareItem;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NiceImageView;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/12/17
 * @Description:添加护理记录条目适配器
 */
public class AddCareItemAdapter extends BaseQuickAdapter<FosterAddCareItem.DataBean.DatasetBean.ServicesBean, BaseViewHolder> {
    public AddCareItemAdapter(int layoutResId, List<FosterAddCareItem.DataBean.DatasetBean.ServicesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterAddCareItem.DataBean.DatasetBean.ServicesBean item) {
        ImageView iv_addcareitem_select = helper.getView(R.id.iv_addcareitem_select);
        NiceImageView iv_addcareitem_icon = helper.getView(R.id.iv_addcareitem_icon);
        TextView tv_addcareitem_name = helper.getView(R.id.tv_addcareitem_name);
        TextView tv_addcareitem_desc = helper.getView(R.id.tv_addcareitem_desc);
        TextView tv_addcareitem_eprice = helper.getView(R.id.tv_addcareitem_eprice);
        TextView tv_addcareitem_price = helper.getView(R.id.tv_addcareitem_price);
        if (item != null) {
            Utils.setText(tv_addcareitem_name, item.getName(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_addcareitem_desc, item.getDesc(), "", View.VISIBLE, View.GONE);
            GlideUtil.loadImg(mContext, item.getImg(), iv_addcareitem_icon, R.drawable.icon_production_default);
            if (item.isServiceSelect()) {
                iv_addcareitem_select.setImageResource(R.drawable.icon_petadd_select);
                if (item.getSelectPrice() > 0) {
                    tv_addcareitem_price.setVisibility(View.VISIBLE);
                    tv_addcareitem_price.setText("¥" + item.getSelectPrice());
                } else {
                    tv_addcareitem_price.setVisibility(View.GONE);
                }
                if (item.getSelectEPrice() > 0) {
                    tv_addcareitem_eprice.setVisibility(View.VISIBLE);
                    tv_addcareitem_eprice.setText("E卡：¥" + item.getSelectEPrice());
                } else {
                    tv_addcareitem_eprice.setVisibility(View.GONE);
                }
            } else {
                iv_addcareitem_select.setImageResource(R.drawable.icon_petadd_unselect);
                if (item.getPrice() > 0) {
                    tv_addcareitem_price.setVisibility(View.VISIBLE);
                    tv_addcareitem_price.setText("¥" + item.getPrice());
                } else {
                    tv_addcareitem_price.setVisibility(View.GONE);
                }
                if (item.getEPrice() > 0) {
                    tv_addcareitem_eprice.setVisibility(View.VISIBLE);
                    tv_addcareitem_eprice.setText("E卡：¥" + item.getEPrice());
                } else {
                    tv_addcareitem_eprice.setVisibility(View.GONE);
                }
            }
        }
    }
}
