package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/11/28 0028.
 */

public class SwitchShopAdapter<T> extends CommonAdapter<T> {
    public SwitchShopAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServiceShopAdd serviceShopAdd = (ServiceShopAdd) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_choose_switch_shop,position);
        ImageView imgIcon = viewHolder.getView(R.id.imgIcon);
        GlideUtil.loadCircleImg(mContext,serviceShopAdd.shopImg,imgIcon,0);
        viewHolder.setText(R.id.textViewShopname,serviceShopAdd.shopName);
        viewHolder.setText(R.id.textViewDesc,"距您"+serviceShopAdd.dist+"  好评率"+serviceShopAdd.goodRate);
        viewHolder.setTextColor(R.id.textViewShopname,"#333333");
        viewHolder.setTextColor(R.id.textViewDesc,"#333333");
        if (serviceShopAdd.isChoose){
            viewHolder.setTextColor(R.id.textViewShopname,"#FFFFFF");
            viewHolder.setTextColor(R.id.textViewDesc,"#FFFFFF");
            viewHolder.setBackgroundResource(R.id.layoutChoose,R.drawable.main_local_bg_shade_choose);
        }else {
            viewHolder.setBackgroundResource(R.id.layoutChoose,R.drawable.main_local_bg_shade);
        }
        return viewHolder.getConvertView();
    }
}
