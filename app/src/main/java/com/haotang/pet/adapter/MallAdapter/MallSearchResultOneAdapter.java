package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallToListTopTwoIcon;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * 搜索结果页 顶部一级分类
 * Created by Administrator on 2017/8/31.
 */

public class MallSearchResultOneAdapter<T> extends CommonAdapter<T> {
    public int pos =-1;
    public MallSearchResultOneAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MallToListTopTwoIcon.MallToListThr mallToListThr = (MallToListTopTwoIcon.MallToListThr) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_search_result_top,position);
        viewHolder.setText(R.id.show_top_title,mallToListThr.title);
        viewHolder.getView(R.id.view_bottom_line).setVisibility(View.INVISIBLE);
        if (pos!=-1){
            if (pos==position){
                viewHolder.getView(R.id.view_bottom_line).setVisibility(View.VISIBLE);
            }
        }
        return viewHolder.getConvertView();
    }
    public void setChoosePos(int pos){
        this.pos = pos;
    }
}
