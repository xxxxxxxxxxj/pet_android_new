package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ShopMallFragIcon;
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
 * @date XJ on 2017/8/30 11:29
 */
public class ShopMallIconAdapter<T> extends CommonAdapter<T> {
    public ShopMallIconAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShopMallFragIcon shopMallFragIcon = (ShopMallFragIcon) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_shopmallicon_adapter, position);
        ImageView iv_item_shopmallicon = viewHolder
                .getView(R.id.iv_item_shopmallicon);
        TextView tv_item_shopmallicon = viewHolder
                .getView(R.id.tv_item_shopmallicon);
        TextView tv_item_shopmallicon_flag = viewHolder
                .getView(R.id.tv_item_shopmallicon_flag);
        if (shopMallFragIcon != null) {
            GlideUtil.loadCircleImg(mContext, shopMallFragIcon.getPicDomain(), iv_item_shopmallicon,
                    R.drawable.icon_production_default);
            Utils.setText(tv_item_shopmallicon, shopMallFragIcon.getName(), "", View.VISIBLE, View.VISIBLE);
            tv_item_shopmallicon_flag.bringToFront();
            Utils.setText(tv_item_shopmallicon_flag, shopMallFragIcon.getTag(), "", View.VISIBLE, View.GONE);
        }
        return viewHolder.getConvertView();
    }
}
