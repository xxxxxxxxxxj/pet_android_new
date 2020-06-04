package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallGoods;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

public class MallOrderPayTopAdapter<T> extends CommonAdapter<T> {
    public MallOrderPayTopAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MallGoods mallGoods = (MallGoods) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_mall_orderpay_top,position);
        viewHolder.setText(R.id.textview_mall_buy_name,mallGoods.title);
        viewHolder.setText(R.id.textview_mall_buy_price_num,"¥"+mallGoods.retailPrice+"x"+mallGoods.amount);
        return viewHolder.getConvertView();
    }
}
