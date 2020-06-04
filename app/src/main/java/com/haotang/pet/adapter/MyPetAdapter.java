package com.haotang.pet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.GlideUtil;

import java.util.ArrayList;

public class MyPetAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Pet> mypetList;
	private LayoutInflater mInflater;
	private int petkind;

	public MyPetAdapter(Context context, int petkind, ArrayList<Pet> mypetList) {
		this.context = context;
		this.mypetList = mypetList;
		this.petkind = petkind;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mypetList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mypetList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setPetKind(int petkind) {
		this.petkind = petkind;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_mypet, parent, false);
			holder.mypet_icon = (ImageView) convertView
					.findViewById(R.id.sriv_mypetitem_image);
			holder.mypet_name = (TextView) convertView
					.findViewById(R.id.tv_mypetitem_name);
			holder.vMask = convertView.findViewById(R.id.view_mypet_item_mask);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (mypetList.get(position).sa == 0) {
			holder.vMask.setVisibility(View.VISIBLE);
		} else {
			holder.vMask.setVisibility(View.GONE);
		}
//		holder.mypet_icon.setTag(mypetList.get(position).image);
		GlideUtil.loadCircleImg(context,mypetList.get(position).image,holder.mypet_icon,0);
		holder.mypet_name.setText(mypetList.get(position).nickName);
		return convertView;
	}

	private class Holder {
		ImageView mypet_icon;
		TextView mypet_name;
		View vMask;
	}

}
