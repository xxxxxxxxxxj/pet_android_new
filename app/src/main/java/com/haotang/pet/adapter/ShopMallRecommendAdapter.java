package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.ShopMallFragRecommend;
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
 * @date XJ on 2017/8/30 11:39
 */
public class ShopMallRecommendAdapter<T> extends CommonAdapter<T> {
    public ShopMallRecommendAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShopMallFragRecommend shopMallFragRecommend = (ShopMallFragRecommend) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_shopmallrecommend, position);
        ImageView iv_item_shopmallrecommend = viewHolder
                .getView(R.id.iv_item_shopmallrecommend);
        TextView tv_item_shopmallrecommend_xianjia = viewHolder
                .getView(R.id.tv_item_shopmallrecommend_xianjia);
        TextView tv_item_shopmallrecommend_title = viewHolder
                .getView(R.id.tv_item_shopmallrecommend_title);
        RelativeLayout rl_item_shopmallrecommend_root = viewHolder
                .getView(R.id.rl_item_shopmallrecommend_root);
        View vw_item_shopmallrecommend = viewHolder
                .getView(R.id.vw_item_shopmallrecommend);
        TextView textvie_price1 = viewHolder
                .getView(R.id.textvie_price1);
        if (shopMallFragRecommend != null) {
            if (shopMallFragRecommend.isOther()) {
                vw_item_shopmallrecommend.setVisibility(View.VISIBLE);
                rl_item_shopmallrecommend_root.setVisibility(View.GONE);
            } else {
                vw_item_shopmallrecommend.setVisibility(View.GONE);
                rl_item_shopmallrecommend_root.setVisibility(View.VISIBLE);
                if (position % 2 != 0 || position == mDatas.size() - 1) {
                    rl_item_shopmallrecommend_root.setBackgroundResource(R.drawable.rl_top_selector);
                } else {
                    rl_item_shopmallrecommend_root.setBackgroundResource(R.drawable.rl_top_right_selector);
                }
                GlideUtil.loadImg(mContext, shopMallFragRecommend.getThumbnail(), iv_item_shopmallrecommend,
                        R.drawable.icon_production_default);
                if (shopMallFragRecommend.getRetailPrice() > 0) {
                    textvie_price1.setVisibility(View.VISIBLE);
                    String text = "";
                    if (Utils.isDoubleEndWithZero(shopMallFragRecommend.getRetailPrice())) {
                        text = "¥" + Utils.formatDouble(shopMallFragRecommend.getRetailPrice()) + "市场价";
                    } else {
                        text = "¥" + shopMallFragRecommend.getRetailPrice() + "市场价";
                    }
                    textvie_price1.setText(text);
                    textvie_price1.getPaint().setAntiAlias(true);//抗锯齿
                    textvie_price1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                } else {
                    textvie_price1.setVisibility(View.GONE);
                }
                if (shopMallFragRecommend.getePrice() > 0) {
                    tv_item_shopmallrecommend_xianjia.setVisibility(View.VISIBLE);
                    String text = "";
                    if (Utils.isDoubleEndWithZero(shopMallFragRecommend.getePrice())) {
                        text = "¥" + Utils.formatDouble(shopMallFragRecommend.getePrice());
                    } else {
                        text = "¥" + shopMallFragRecommend.getePrice();
                    }
                    SpannableString ss = new SpannableString(text);
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.ninesp), 0,
                            1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_shopmallrecommend_xianjia.setText(ss);
                } else {
                    tv_item_shopmallrecommend_xianjia.setVisibility(View.GONE);
                }
                Utils.setText(tv_item_shopmallrecommend_title, shopMallFragRecommend.getTitle(), "", View.VISIBLE, View.GONE);
                rl_item_shopmallrecommend_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", shopMallFragRecommend.getId()));
                    }
                });
            }
        }
        return viewHolder.getConvertView();
    }
}