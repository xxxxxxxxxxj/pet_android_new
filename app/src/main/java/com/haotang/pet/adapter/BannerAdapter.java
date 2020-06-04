package com.haotang.pet.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.rollviewpager.adapter.DynamicPagerAdapter;

import java.util.ArrayList;

public class BannerAdapter extends DynamicPagerAdapter {
	private ArrayList<Banner> list;

	public OnBannerItemClickListener onBannerItemClickListener = null;

	public interface OnBannerItemClickListener {
		public void OnBannerItemClick(int position);
	}

	public void setOnItemClickListener(
			OnBannerItemClickListener onBannerItemClickListener) {
		this.onBannerItemClickListener = onBannerItemClickListener;
	}

	public BannerAdapter(ArrayList<Banner> list) {
		super();
		this.list = list;
	}

	@Override
	public View getView(ViewGroup container, final int position) {
		final ImageView view = new ImageView(container.getContext());
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		final Banner banner = list.get(position);
		if (banner != null) {
			GlideUtil.loadImg(container.getContext(),banner.pic, view,
					R.drawable.icon_production_default);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (onBannerItemClickListener != null) {
						onBannerItemClickListener.OnBannerItemClick(position);
					}
				}
			});
		}
		return view;
	}

	public void clearDeviceList() {
		if (list != null) {
			list.clear();
		}
		notifyDataSetChanged();
	}

	public void setData(ArrayList<Banner> list) {
		if (list != null) {
			this.list = list;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

}
