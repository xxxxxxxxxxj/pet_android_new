package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallAddress;
import com.haotang.pet.mall.MallAddTackGoodsAddressActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MallSelfAddressAdapter<T> extends CommonAdapter<T> {
    public int id = -1;

    public MallSelfAddressAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MallAddress mallAddress = (MallAddress) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_mall_self_address, position);
        viewHolder.setText(R.id.textview_self_name, mallAddress.consigner);
        viewHolder.setText(R.id.textview_self_tel, mallAddress.mobile);
        viewHolder.getView(R.id.textview_isdefault).setVisibility(View.GONE);
        viewHolder.setTextColor(R.id.textview_self_name, "#333333");
        viewHolder.setTextColor(R.id.textview_self_tel, "#333333");
        if (mallAddress.isDefault == 1) {
            viewHolder.getView(R.id.textview_isdefault).setVisibility(View.VISIBLE);
        }
        viewHolder.setText(R.id.textview_mall_address, mallAddress.areaName + " " + mallAddress.address);

        ImageView img_edit_mall = viewHolder.getView(R.id.img_edit_mall);
        LinearLayout layout_edit = viewHolder.getView(R.id.layout_edit);
        img_edit_mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MallAddTackGoodsAddressActivity.class);
                intent.putExtra("addressId", mallAddress.id);
                intent.putExtra("mallAddress", mallAddress);
                mContext.startActivityForResult(intent, Global.MALL_SELF_ADDRESS_CLICK);
            }
        });
        layout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MallAddTackGoodsAddressActivity.class);
                intent.putExtra("addressId", mallAddress.id);
                intent.putExtra("mallAddress", mallAddress);
                mContext.startActivityForResult(intent, Global.MALL_SELF_ADDRESS_CLICK);
            }
        });
        viewHolder.getView(R.id.imageView_isdefault).setVisibility(View.GONE);
        if (id != -1) {
            if (id == mallAddress.id) {
                viewHolder.getView(R.id.imageView_isdefault).setVisibility(View.VISIBLE);
                viewHolder.setBackgroundResource(R.id.imageView_isdefault, R.drawable.mall_area_choose);
                viewHolder.setTextColor(R.id.textview_self_name, "#FF3A1E");
                viewHolder.setTextColor(R.id.textview_self_tel, "#FF3A1E");
            }
        }
        return viewHolder.getConvertView();
    }

    public void setChooseState(int id) {
        this.id = id;
    }
}
