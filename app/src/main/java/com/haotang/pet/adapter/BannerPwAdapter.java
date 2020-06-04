package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

public class BannerPwAdapter<T> extends CommonAdapter<T> {
    public BannerPwAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_banner_pw_adapter, position);
        ImageView iv_item_banner_pw_adapter = viewHolder
                .getView(R.id.iv_item_banner_pw_adapter);
        Banner banner = (Banner) mDatas.get(position);
        if (banner != null) {
            if (Utils.isStrNull(banner.pic)) {
                GlideUtil.loadImg(mContext, banner.pic, iv_item_banner_pw_adapter,
                        R.drawable.icon_production_default);
            } else if (Utils.isStrNull(banner.picDomain)) {
                GlideUtil.loadImg(mContext, banner.picDomain, iv_item_banner_pw_adapter,
                        R.drawable.icon_production_default);
            }
        }
        return viewHolder.getConvertView();
    }
}
