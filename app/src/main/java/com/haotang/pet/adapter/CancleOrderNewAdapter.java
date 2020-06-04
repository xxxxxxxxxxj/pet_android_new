package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public class CancleOrderNewAdapter<T> extends CommonAdapter<T> {
    public int pos=-1;
    public CancleOrderNewAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_choose_reason,position);
        viewHolder.setText(R.id.textviewReason,mDatas.get(position).toString());
        if (pos==position){
            viewHolder.setBackgroundResource(R.id.img_choose,R.drawable.icon_petadd_select);
        }else {
            viewHolder.setBackgroundResource(R.id.img_choose,R.drawable.icon_petadd_unselect);
        }
        return viewHolder.getConvertView();
    }
    public void setPos(int pos){
        this.pos=pos;
        notifyDataSetChanged();
    }
}
