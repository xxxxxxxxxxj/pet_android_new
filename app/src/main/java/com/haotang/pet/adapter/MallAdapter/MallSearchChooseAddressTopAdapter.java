package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.RegionChildren;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MallSearchChooseAddressTopAdapter<T> extends CommonAdapter<T> {
    private int clickPosttion=-1;
    public MallSearchChooseAddressTopAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegionChildren regionChildren = (RegionChildren) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_mall_search_choose_address,position);
        viewHolder.setText(R.id.show_top_title,regionChildren.region);
        if (clickPosttion==position){
            viewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            viewHolder.setTextColor(R.id.show_top_title,"#FF3A1E");
        }else {
            viewHolder.getView(R.id.view_line).setVisibility(View.INVISIBLE);
            viewHolder.setTextColor(R.id.show_top_title,"#333333");
        }
        return viewHolder.getConvertView();
    }
    public void setClickPosttion(int clickPosttion){
        this.clickPosttion = clickPosttion;
    }
}
