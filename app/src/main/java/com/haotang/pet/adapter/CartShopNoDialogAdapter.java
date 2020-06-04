package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ShoppingCart;
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
 * @date XJ on 2017/9/16 19:21
 */
public class CartShopNoDialogAdapter<T> extends CommonAdapter<T> {
    public CartShopNoDialogAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShoppingCart shoppingCart = (ShoppingCart) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_cartshopnodialogadapter, position);
        TextView tv_item_cartshopnodialogadapter = viewHolder
                .getView(R.id.tv_item_cartshopnodialogadapter);
        if (shoppingCart != null) {
            Utils.setText(tv_item_cartshopnodialogadapter, shoppingCart.getTitle(), "", View.VISIBLE, View.GONE);
        }
        return viewHolder.getConvertView();
    }
}
