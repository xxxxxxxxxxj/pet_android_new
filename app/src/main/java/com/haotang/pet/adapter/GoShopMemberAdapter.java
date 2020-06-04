package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MemberEntity;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * 店铺成员
 * @param <T>
 */
public class GoShopMemberAdapter<T> extends CommonAdapter<T>{
	public GoShopMemberAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MemberEntity memberEntities = (MemberEntity) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_go_shop_detail,position);
		ImageView imageView_item_go_shop_icon = viewHolder.getView(R.id.imageView_item_go_shop_icon);
		GlideUtil.loadCircleImg(mContext,memberEntities.avatar,imageView_item_go_shop_icon,0);
		viewHolder.setText(R.id.textView_item_go_shop_title,memberEntities.realName+"");
		viewHolder.setViewVisible(R.id.textView_item_go_shop_detail,View.VISIBLE);
		viewHolder.setText(R.id.textView_item_go_shop_detail,memberEntities.duty+"");
		return viewHolder.getConvertView();
	}
}
