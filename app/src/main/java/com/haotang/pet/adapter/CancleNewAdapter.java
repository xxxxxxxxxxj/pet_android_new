package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.CancleReson;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

public class CancleNewAdapter<T> extends CommonAdapter<T>{

	public int pos =-1;
	public CancleNewAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}
	
	public void setPostition(int pos){
		this.pos=pos;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CancleReson reson = (CancleReson) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.cancle_adapter_choose, position);
		viewHolder.setText(R.id.textView_item_cancle_title,reson.txt);
		viewHolder.setTextColor(R.id.textView_item_cancle_title,"#666666");
		viewHolder.getView(R.id.imageView_item_cancle_state).setVisibility(View.VISIBLE);
		viewHolder.setImageResource(R.id.imageView_item_cancle_state, R.drawable.icon_petadd_unselect);
		if (position==pos) {
			viewHolder.getView(R.id.imageView_item_cancle_state).setVisibility(View.VISIBLE);
			viewHolder.setImageResource(R.id.imageView_item_cancle_state, R.drawable.icon_petadd_select);
		}
		return viewHolder.getConvertView();
	}

}
