package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.BeauticianProductionDetailActivity;
import com.haotang.pet.BeauticianProductuonActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Production;
import com.haotang.pet.util.Global;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;
import com.loveplusplus.demo.image.ImagePagerActivity;

import java.util.ArrayList;
import java.util.List;

public class BeauticianProductionAdapter<T> extends CommonAdapter<T> {
	private ArrayList<Production> mList;
	public BeauticianProductionAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		mList = (ArrayList<Production>) mDatas;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Production production = (Production) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.beauticianproduction_item,position);
		viewHolder.setImageGlideFourRound(mContext,R.id.img_works_show,production.smallimage,R.drawable.icon_production_default,5);
		viewHolder.setText(R.id.textview_works_img_name,production.title+"");
		viewHolder.setText(R.id.good_nums,production.praiseCount+"");

		if (production.userPraise==1){
			viewHolder.setBackgroundResource(R.id.click_good,R.drawable.good_checked);
		}else {
			viewHolder.setBackgroundResource(R.id.click_good,R.drawable.good);
		}
		viewHolder.getView(R.id.layout_out).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				goNext(position);
				String pics [] = new String[mDatas.size()];
				for (int i =0;i<mDatas.size();i++){
					Production mp = (Production) mDatas.get(i);
					pics[i] = mp.image;
				}
				goImageDetail(mContext,position,pics,mList);
			}
		});
		viewHolder.getView(R.id.layout_right_good).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (production.userPraise==1){
					return;
				}
				BeauticianProductuonActivity.beauticianProductuonActivity.praiseAdd(production.id,position);
			}
		});
		return viewHolder.getConvertView();
	}

	private void goNext(int index){
		Intent intent = new Intent(mContext, BeauticianProductionDetailActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mContext.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("index", index);
		intent.putExtra("previous", Global.PRE_BEAUTICIANPRODUCTIONLIST_TO_PRODUCTIONDETAIL);
		mContext.startActivity(intent);
	}

	public void goImageDetail(Context mContext, int position, String[] urls, ArrayList<Production> mList) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra("mList", mList);
		intent.putExtra("type", 0);//
		mContext.startActivity(intent);
	}
	/*private Activity context;
	private ArrayList<Production> list;
	private LayoutInflater mInflater;
	private int imageWidth;
	
	public BeauticianProductionAdapter(Activity context,int imageWidth,
			ArrayList<Production> list) {
		super();
		this.context = context;
		this.list = list;
		this.imageWidth = imageWidth;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = list.size();
		return count%2==0?count/2:count/2+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.beauticianproduction_item, null);
			holder.srivItem1 = (SelectableRoundedImageView) convertView.findViewById(R.id.sriv_beauticianproduction_item_1);
			holder.srivItem2 = (SelectableRoundedImageView) convertView.findViewById(R.id.sriv_beauticianproduction_item_2);
			holder.tvItem1 = (TextView) convertView.findViewById(R.id.tv_beauticianproduction_item_1);
			holder.tvItem2 = (TextView) convertView.findViewById(R.id.tv_beauticianproduction_item_2);
			holder.rlItem1 = (RelativeLayout) convertView.findViewById(R.id.rl_beauticianproduction_item_1);
			holder.rlItem2 = (RelativeLayout) convertView.findViewById(R.id.rl_beauticianproduction_item_2);
			setProductionHeight(holder.srivItem1,imageWidth);
			setProductionHeight(holder.srivItem2,imageWidth);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		if(list.size()<2*(position+1)){
			//奇数个
			holder.rlItem2.setVisibility(View.INVISIBLE);
			holder.rlItem1.setVisibility(View.VISIBLE);
			holder.tvItem1.setText(list.get(position*2).title);
			
			holder.srivItem1.setTag(list.get(2*position).smallimage);
			holder.srivItem1.setImageResource(R.drawable.icon_production_default);
			
			if(list.get(2*position).smallimage!=null&&!"".equals(list.get(2*position).smallimage.trim())){
				ImageLoaderUtil.loadImg(list.get(2*position).smallimage, holder.srivItem1,0, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View view) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String path, View view, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if(view!=null&&bitmap!=null){
							ImageView iv = (ImageView) view;
							String imagetag = (String) iv.getTag();
							if(path!=null&&path.equals(imagetag)){
								iv.setImageBitmap(bitmap);
							}
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}else{
			//偶数个
			holder.rlItem2.setVisibility(View.VISIBLE);
			holder.rlItem1.setVisibility(View.VISIBLE);
			holder.tvItem1.setText(list.get(position*2).title);
			
			
			holder.srivItem1.setTag(list.get(2*position).smallimage);
			holder.srivItem1.setImageResource(R.drawable.icon_production_default);
			
			if(list.get(2*position).smallimage!=null&&!"".equals(list.get(2*position).smallimage.trim())){
				ImageLoaderUtil.loadImg(list.get(2*position).smallimage, holder.srivItem1,0, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View view) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String path, View view, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if(view!=null&&bitmap!=null){
							ImageView iv = (ImageView) view;
							String imagetag = (String) iv.getTag();
							if(path!=null&&path.equals(imagetag)){
								iv.setImageBitmap(bitmap);
							}
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			
			holder.srivItem2.setTag(list.get(2*position+1).smallimage);
			holder.srivItem2.setImageResource(R.drawable.icon_production_default);
			
			if(list.get(2*position+1).smallimage!=null&&!"".equals(list.get(2*position+1).smallimage.trim())){
				ImageLoaderUtil.loadImg(list.get(2*position+1).smallimage, holder.srivItem2,0, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View view) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String path, View view, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if(view!=null&&bitmap!=null){
							ImageView iv = (ImageView) view;
							String imagetag = (String) iv.getTag();
							if(path!=null&&path.equals(imagetag)){
								iv.setImageBitmap(bitmap);
							}
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			
			
			holder.tvItem2.setText(list.get(position*2+1).title);
		}
		holder.srivItem1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(2*position);
			}
		});
		holder.srivItem2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(2*position+1);
			}
		});
		
		return convertView;
	}
	
	private void goNext(int index){
		Intent intent = new Intent(context, BeauticianProductionDetailActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		context.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("index", index);
		intent.putExtra("previous", Global.PRE_BEAUTICIANPRODUCTIONLIST_TO_PRODUCTIONDETAIL);

		context.startActivity(intent);
	}
	
	private void setProductionHeight(ImageView iv, int height){
		LayoutParams lParams = iv.getLayoutParams();
		lParams.width = height;
		lParams.height = height;
		iv.setLayoutParams(lParams);
	}
	
	private class Holder{
		SelectableRoundedImageView srivItem1;
		SelectableRoundedImageView srivItem2;
		TextView tvItem1;
		TextView tvItem2;
		RelativeLayout rlItem1;
		RelativeLayout rlItem2;
	}
*/
}
