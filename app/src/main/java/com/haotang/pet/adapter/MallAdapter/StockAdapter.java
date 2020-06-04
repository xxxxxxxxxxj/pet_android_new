package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallIcon;
import com.haotang.pet.view.*;

import java.util.List;

/**
 * 搜索界面 常用选项
 * Created by Administrator on 2017/8/29.
 */

public class StockAdapter<T> extends com.haotang.pet.view.CommonAdapter<T> {
    public StockAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MallIcon mallIcon = (MallIcon) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_mallsearch_bottom,position);
        String showTitle = mallIcon.name;
        StringBuilder str = new StringBuilder();
        if (showTitle.length()>2){
            String LeftTwoWord = showTitle.substring(0,2);
            str.append(LeftTwoWord+"\n");
            String RightWord = showTitle.substring(2,showTitle.length());
            str.append(RightWord);
        }else{
            str.append(showTitle);
        }
        viewHolder.setText(R.id.bottom_show_text,str.toString());
        return viewHolder.getConvertView();
    }
}
