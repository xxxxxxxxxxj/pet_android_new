package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.ShopMallFragCuteDown;
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
 * @date XJ on 2017/8/30 11:33
 */
public class ShopMallCuteDownAdapter<T> extends CommonAdapter<T> {
    public ShopMallCuteDownAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShopMallFragCuteDown shopMallFragCuteDown = (ShopMallFragCuteDown) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_shopmall_cutedown, position);
        ImageView iv_item_shopmallcutedown = viewHolder
                .getView(R.id.iv_item_shopmallcutedown);
        TextView textview_price1 = viewHolder
                .getView(R.id.textview_price1);
        TextView textview_price = viewHolder
                .getView(R.id.textview_price);
        LinearLayout ll_price = viewHolder
                .getView(R.id.ll_price);
        TextView tv_item_shopmallcutedown_title = viewHolder
                .getView(R.id.tv_item_shopmallcutedown_title);
        LinearLayout ll_item_shopmallcutedown_root = viewHolder
                .getView(R.id.ll_item_shopmallcutedown_root);
        if (shopMallFragCuteDown != null) {
            GlideUtil.loadImg(mContext, shopMallFragCuteDown.getThumbnail(), iv_item_shopmallcutedown,
                    R.drawable.icon_production_default);
            if (shopMallFragCuteDown.getePrice() > 0) {
                ll_price.setVisibility(View.VISIBLE);
                String text = "";
                if (Utils.isDoubleEndWithZero(shopMallFragCuteDown.getePrice())) {
                    text = "" + Utils.formatDouble(shopMallFragCuteDown.getePrice());
                } else {
                    text = "" + shopMallFragCuteDown.getePrice();
                }
                textview_price.setText(text);
            } else {
                ll_price.setVisibility(View.GONE);
            }
            if (shopMallFragCuteDown.getRetailPrice() > 0) {
                textview_price1.setVisibility(View.VISIBLE);
                String text = "";
                if (Utils.isDoubleEndWithZero(shopMallFragCuteDown.getRetailPrice())) {
                    text = "¥" + Utils.formatDouble(shopMallFragCuteDown.getRetailPrice()) + "市场价";
                } else {
                    text = "¥" + shopMallFragCuteDown.getRetailPrice() + "市场价";
                }
                textview_price1.setText(text);
                textview_price1.getPaint().setAntiAlias(true);//抗锯齿
                textview_price1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                textview_price1.requestLayout();
            } else {
                textview_price1.setVisibility(View.GONE);
            }
            Utils.setText(tv_item_shopmallcutedown_title, shopMallFragCuteDown.getTitle(), "", View.VISIBLE, View.VISIBLE);
            ll_item_shopmallcutedown_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", shopMallFragCuteDown.getId()));
                }
            });
        }
        return viewHolder.getConvertView();
    }
}
