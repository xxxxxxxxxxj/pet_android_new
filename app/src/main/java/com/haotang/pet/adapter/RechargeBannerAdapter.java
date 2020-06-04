package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.RechargeBanner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/10/30 15:48
 */
public class RechargeBannerAdapter<T> extends CommonAdapter<T> {
    public RechargeBannerAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_rechargebanner, position);
        ImageView iv_item_rechargebanner = viewHolder
                .getView(R.id.iv_item_rechargebanner);
        final RechargeBanner rechargeBanner = (RechargeBanner) mDatas.get(position);
        if (rechargeBanner != null) {
            GlideUtil.loadImg(mContext, rechargeBanner.imgUrl, iv_item_rechargebanner,
                    R.drawable.icon_production_default);
            iv_item_rechargebanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Utils.goService(mContext, rechargeBanner.point,
                            rechargeBanner.backup);
                }
            });
        }
        return viewHolder.getConvertView();
    }
}
