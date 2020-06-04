package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MallCommodityGroup;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/4 11:30
 */
public class CommodityPopSpecificationsAdapter<T> extends CommonAdapter<T> {
    public CommodityPopSpecificationsAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MallCommodityGroup mallCommodityGroup = (MallCommodityGroup) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_commoditypop_specifications, position);
        LinearLayout ll_commodity_popspec = viewHolder
                .getView(R.id.ll_commodity_popspec);
        TextView tv_commodity_popspec_name = viewHolder
                .getView(R.id.tv_commodity_popspec_name);
        if (mallCommodityGroup != null) {
            if (mallCommodityGroup.getFlag() == 1) {
                tv_commodity_popspec_name.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                ll_commodity_popspec.setBackgroundResource(R.drawable.bg_redline_round);
            } else {
                tv_commodity_popspec_name.setTextColor(mContext.getResources().getColor(R.color.a666666));
                ll_commodity_popspec.setBackgroundResource(R.drawable.bg_line_round666);
            }
            Utils.setText(tv_commodity_popspec_name, mallCommodityGroup.getSpecName(), "", View.VISIBLE, View.VISIBLE);
        }
        return viewHolder.getConvertView();
    }
}