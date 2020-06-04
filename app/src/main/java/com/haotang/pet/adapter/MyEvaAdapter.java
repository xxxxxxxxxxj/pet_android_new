package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.BeauticianCommonPicActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.CustomerOrderComment;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;
import com.loveplusplus.demo.image.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

public class MyEvaAdapter<T> extends CommonAdapter<T>{

	public MyEvaAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CustomerOrderComment orderComment = (CustomerOrderComment) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_only_my_eva, position);
		viewHolder.setText(R.id.service_name,orderComment.petServiceName);
//		RatingBar ratingBar_major_grade = viewHolder.getView(R.id.ratingBar_major_grade);
//		ratingBar_major_grade.setRating(0);
//		ratingBar_major_grade.setRating(orderComment.grade);
		ImageView img_one = viewHolder.getView(R.id.img_one);
		ImageView img_two = viewHolder.getView(R.id.img_two);
		ImageView img_thr = viewHolder.getView(R.id.img_thr);
		ImageView img_four = viewHolder.getView(R.id.img_four);
		ImageView img_five = viewHolder.getView(R.id.img_five);
		List<ImageView> list = new ArrayList<>();
		list.add(img_one);
		list.add(img_two);
		list.add(img_thr);
		list.add(img_four);
		list.add(img_five);
		for (int i =0;i<list.size();i++){
			list.get(i).setBackgroundResource(R.drawable.star_empty);
		}
		for (int i=0;i<orderComment.grade;i++){
			list.get(i).setBackgroundResource(R.drawable.star_full);
		}
		viewHolder.setText(R.id.textview_eva_level,orderComment.commentGradeCopy);
		viewHolder.setText(R.id.textView_eva_content,orderComment.content);
		viewHolder.setText(R.id.textView_eva_time,orderComment.created);
		viewHolder.getView(R.id.ll_beauticiancommetslist_item_image).setVisibility(View.GONE);
		if (orderComment.imgLists.size()>0) {
			viewHolder.getView(R.id.ll_beauticiancommetslist_item_image).setVisibility(View.VISIBLE);
			for (int i = 0; i < orderComment.imgLists.size(); i++) {
				if (i==0) {
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image1).setVisibility(View.VISIBLE);
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image2).setVisibility(View.GONE);
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image3).setVisibility(View.GONE);
					GlideUtil.loadImg(mContext,orderComment.imgLists.get(0),(ImageView)viewHolder.getView(R.id.iv_beauticiancommentslist_item_image1),  R.drawable.icon_production_default);
				}else if (i==1) {
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image1).setVisibility(View.VISIBLE);
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image2).setVisibility(View.VISIBLE);
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image3).setVisibility(View.GONE);
					GlideUtil.loadImg(mContext,orderComment.imgLists.get(0),(ImageView)viewHolder.getView(R.id.iv_beauticiancommentslist_item_image1),  R.drawable.icon_production_default);
					GlideUtil.loadImg(mContext,orderComment.imgLists.get(1),(ImageView)viewHolder.getView(R.id.iv_beauticiancommentslist_item_image2),  R.drawable.icon_production_default);
				}else if (i==2) {
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image1).setVisibility(View.VISIBLE);
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image2).setVisibility(View.VISIBLE);
					viewHolder.getView(R.id.iv_beauticiancommentslist_item_image3).setVisibility(View.VISIBLE);
					GlideUtil.loadImg(mContext,orderComment.imgLists.get(0),(ImageView)viewHolder.getView(R.id.iv_beauticiancommentslist_item_image1),  R.drawable.icon_production_default);
					GlideUtil.loadImg(mContext,orderComment.imgLists.get(1),(ImageView)viewHolder.getView(R.id.iv_beauticiancommentslist_item_image2),  R.drawable.icon_production_default);
					GlideUtil.loadImg(mContext,orderComment.imgLists.get(2),(ImageView)viewHolder.getView(R.id.iv_beauticiancommentslist_item_image3),  R.drawable.icon_production_default);

				}
			}
		}
		viewHolder.getView(R.id.item_layout_reply).setVisibility(View.GONE);
		if (!TextUtils.isEmpty(orderComment.replyContent)) {
			viewHolder.getView(R.id.item_layout_reply).setVisibility(View.VISIBLE);
			viewHolder.setText(R.id.TextView_waiter_Reply, orderComment.replyUser+":"+orderComment.replyContent);
			viewHolder.setText(R.id.textview_waiter_reply_time, orderComment.replyTime);
		}
		viewHolder.getView(R.id.iv_beauticiancommentslist_item_image1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomerOrderComment orderCommentEvery = (CustomerOrderComment) mDatas.get(position);
				String [] pics = new String[orderCommentEvery.imgLists.size()];
				for (int i = 0; i < orderCommentEvery.imgLists.size(); i++) {
					pics[i] = orderCommentEvery.imgLists.get(i);
				}
//				goNext(0,pics);
				goImageDetail(0,pics);
			}
		});
		viewHolder.getView(R.id.iv_beauticiancommentslist_item_image2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomerOrderComment orderCommentEvery = (CustomerOrderComment) mDatas.get(position);
				String [] pics = new String[orderCommentEvery.imgLists.size()];
				for (int i = 0; i < orderCommentEvery.imgLists.size(); i++) {
					pics[i] = orderCommentEvery.imgLists.get(i);
				}
				goImageDetail(1,pics);
			}
		});
		viewHolder.getView(R.id.iv_beauticiancommentslist_item_image3).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomerOrderComment orderCommentEvery = (CustomerOrderComment) mDatas.get(position);
				String [] pics = new String[orderCommentEvery.imgLists.size()];
				for (int i = 0; i < orderCommentEvery.imgLists.size(); i++) {
					pics[i] = orderCommentEvery.imgLists.get(i);
				}
				goImageDetail(2,pics);
			}
		});
		return viewHolder.getConvertView();
	}
	
	private void goNext(int index,String[] pics){
		Intent intent = new Intent(mContext, BeauticianCommonPicActivity.class);
		intent.putExtra("index", index);
		intent.putExtra("pics", pics);
		
		mContext.startActivity(intent);
	}
	private void goImageDetail(int position,String[] urls) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

}
