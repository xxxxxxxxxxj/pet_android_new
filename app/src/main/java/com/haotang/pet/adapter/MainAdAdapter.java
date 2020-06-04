package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class MainAdAdapter<T> extends CommonAdapter<T> {

	public MainAdAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Banner banner = (Banner) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_mainad_adapter, position);
		ImageView iv_item_mainad_adapter = viewHolder
				.getView(R.id.iv_item_mainad_adapter);
		if (banner != null) {
			GlideUtil.loadImg(mContext,banner.pic, iv_item_mainad_adapter,
					R.drawable.icon_production_default);
		}
		return viewHolder.getConvertView();
	}

}
