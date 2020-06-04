package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ShopServices;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * 服务项目
 * @param <T>
 */
public class GoShopStepAdapter<T> extends CommonAdapter<T>{
	public GoShopStepAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShopServices shopServices = (ShopServices) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_go_shop_detail,position);
		ImageView imageView_item_go_shop_icon = viewHolder.getView(R.id.imageView_item_go_shop_icon);
		GlideUtil.loadCircleImg(mContext,shopServices.img,imageView_item_go_shop_icon,0);
		viewHolder.setText(R.id.textView_item_go_shop_title,shopServices.name+"");
		return viewHolder.getConvertView();
	}

/*
	private Context context;
//	private List<String> list;
	private ShopEntity shopEntity;
	public GoShopStepAdapter(Context context,ShopEntity shopEntity){
		this.context=context;
		this.shopEntity=shopEntity;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopEntity.list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return shopEntity.list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder=null;
		if (view==null) {
			mHolder = new ViewHolder();
			view=LayoutInflater.from(context).inflate(R.layout.item_go_shop_detail, null);
			mHolder.imageView_item_go_shop_icon=(SelectableRoundedImageView) view.findViewById(R.id.imageView_item_go_shop_icon);
			mHolder.textView_item_go_shop_title=(TextView) view.findViewById(R.id.textView_item_go_shop_title);
			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
		ImageLoaderUtil.loadImg(shopEntity.list.get(arg0).img, mHolder.imageView_item_go_shop_icon,0, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
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
					ImageView iv = (ImageView)view;
					iv.setImageBitmap(bitmap);
				}
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		mHolder.textView_item_go_shop_title.setText(shopEntity.list.get(arg0).name);
		return view;
	}

	class ViewHolder{
		SelectableRoundedImageView imageView_item_go_shop_icon;
		TextView textView_item_go_shop_title;
	}
*/
}
