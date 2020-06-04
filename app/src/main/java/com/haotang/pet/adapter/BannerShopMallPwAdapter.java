package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/8 18:56
 */
public class BannerShopMallPwAdapter<T> extends CommonAdapter<T> {
    public BannerShopMallPwAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_banner_mallpw_adapter, position);
        ImageView iv_item_banner_pw_adapter = viewHolder
                .getView(R.id.iv_item_banner_pw_adapter);
        Banner banner = (Banner) mDatas.get(position);
        if (banner != null) {
            GlideUtil.loadImg(mContext, banner.picDomain, iv_item_banner_pw_adapter,
                    R.drawable.icon_production_default);
        }
        return viewHolder.getConvertView();
    }
}
