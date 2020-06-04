package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MainService;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

public class MainServiceAdapter extends BaseAdapter {
	private ArrayList<MainService> list;
	private LayoutInflater mInflater;
	private SharedPreferenceUtil spUtil;
	private Context context;

	public MainServiceAdapter(Context context, ArrayList<MainService> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
		spUtil = SharedPreferenceUtil.getInstance(context);
	}

	public void clearDeviceList() {
		if (list != null) {
			list.clear();
		}
		notifyDataSetChanged();
	}

	public void setData(ArrayList<MainService> list) {
		if (list != null) {
			this.list = list;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		Holder holder = null;
		if (contentView == null) {
			holder = new Holder();
			contentView = mInflater.inflate(R.layout.mainserviceitem, null);
			holder.iv_mainserviceitem = (ImageView) contentView
					.findViewById(R.id.iv_mainserviceitem);
			holder.tv_mainserviceitem_hot = (TextView) contentView
					.findViewById(R.id.tv_mainserviceitem_hot);
			holder.tv_mainserviceitem_des = (TextView) contentView
					.findViewById(R.id.tv_mainserviceitem_des);
			holder.tv_mainserviceitem_name = (TextView) contentView
					.findViewById(R.id.tv_mainserviceitem_name);
			contentView.setTag(holder);
		} else {
			holder = (Holder) contentView.getTag();
		}
		MainService mainService = list.get(position);
		if (mainService != null) {
			Utils.setText(holder.tv_mainserviceitem_hot, mainService.tag, "",
					View.VISIBLE, View.GONE);
			Utils.setText(holder.tv_mainserviceitem_des, mainService.intro, "",
					View.VISIBLE, View.GONE);
			Utils.setText(holder.tv_mainserviceitem_name, mainService.txt, "",
					View.VISIBLE, View.GONE);
			GlideUtil.loadImg(context,mainService.pic, holder.iv_mainserviceitem,
					R.drawable.icon_production_default);
		}
		return contentView;
	}

	class Holder {
		ImageView iv_mainserviceitem;
		TextView tv_mainserviceitem_hot;
		TextView tv_mainserviceitem_des;
		TextView tv_mainserviceitem_name;
	}

}
