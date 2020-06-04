package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.NavigationCondition;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * 品牌  适用对象分类点开gridview
 * Created by Administrator on 2017/8/29.
 */

public class MallOpenDetailAdapter<T> extends CommonAdapter<T> {
    private int index =-1;
    public MallOpenDetailAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavigationCondition.NavigationOpenDetail navigationOpenDetail = (NavigationCondition.NavigationOpenDetail) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_open_navigation_detail,position);
        viewHolder.setText(R.id.textview_open_nav_name,navigationOpenDetail.NavigationOpenDetailName);
        if (navigationOpenDetail.isChoose==1){
            viewHolder.setBackgroundResource(R.id.img_right,R.drawable.mall_area_choose);
            viewHolder.setTextColor(R.id.textview_open_nav_name,"#FF3A1E");
        }else if (navigationOpenDetail.isChoose ==0){
            viewHolder.setBackgroundResource(R.id.img_right,0);
            viewHolder.setTextColor(R.id.textview_open_nav_name,"#333333");
        }
        return viewHolder.getConvertView();
    }
    public void setIndex(int index){
        this.index = index;
    }
}
