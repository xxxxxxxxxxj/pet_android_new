package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallCommodity;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.TagTextView;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class PaySuccessAdapter<T> extends CommonAdapter<T> {
    private int wh[] = null;
    private String couponText;

    public PaySuccessAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
        wh = Utils.getDisplayMetrics(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MallCommodity mallSearchGoods = (MallCommodity) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_paysuccess_new, position);
        TextView textvie_price1 = viewHolder
                .getView(R.id.textvie_price1);
        TextView textvie_price = viewHolder
                .getView(R.id.textvie_price);
        TagTextView textview_mall_name = viewHolder
                .getView(R.id.textview_mall_name);
        TextView tv_price_fh = viewHolder
                .getView(R.id.tv_price_fh);
        if (mallSearchGoods.ePrice > 0) {
            textvie_price.setVisibility(View.VISIBLE);
            tv_price_fh.setVisibility(View.VISIBLE);
            String text = "";
            if (Utils.isDoubleEndWithZero(mallSearchGoods.ePrice)) {
                text = "" + Utils.formatDouble(mallSearchGoods.ePrice);
            } else {
                text = "" + mallSearchGoods.ePrice;
            }
            textvie_price.setText(text);
        } else {
            textvie_price.setVisibility(View.GONE);
            tv_price_fh.setVisibility(View.GONE);
        }
        if (mallSearchGoods.retailPrice > 0) {
            textvie_price1.setVisibility(View.VISIBLE);
            String text = "";
            if (Utils.isDoubleEndWithZero(mallSearchGoods.retailPrice)) {
                text = "¥" + Utils.formatDouble(mallSearchGoods.retailPrice) + "市场价";
            } else {
                text = "¥" + mallSearchGoods.retailPrice + "市场价";
            }
            textvie_price1.setText(text);
            textvie_price1.getPaint().setAntiAlias(true);//抗锯齿
            textvie_price1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        } else {
            textvie_price1.setVisibility(View.GONE);
        }
        if (Utils.isStrNull(mallSearchGoods.marketingTag)) {
            textview_mall_name.setSingleTagAndContent(mallSearchGoods.marketingTag, mallSearchGoods.title);
        } else {
            textview_mall_name.setText(mallSearchGoods.title);
        }/*
        if (!TextUtils.isEmpty(mallSearchGoods.marketingTag)) {
            viewHolder.getView(R.id.textview_tag).setVisibility(View.VISIBLE);
            String tag = mallSearchGoods.marketingTag;
            viewHolder.setText(R.id.textview_tag, tag);
            int lengthTag = tag.length();
            int numCount = 0;
            int ABCCount = 0;
            int CHINACount = 0;
            char[] tagsLength = tag.toCharArray();
            for (int i = 0; i < tagsLength.length; i++) {
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(String.valueOf(tagsLength[i]));
                if (m.matches()) {
                    numCount++;
                }
                p = Pattern.compile("[a-zA-Z]");
                m = p.matcher(String.valueOf(tagsLength[i]));
                if (m.matches()) {
                    numCount++;
                }
                p = Pattern.compile("[\u4e00-\u9fa5]");
                m = p.matcher(String.valueOf(tagsLength[i]));
                if (m.matches()) {
                    CHINACount++;
                }
            }
            Utils.mLogError("==-->numCount " + numCount + " ABCCount " + ABCCount + " CHINACount " + CHINACount);

            StringBuilder st = new StringBuilder();
            for (int i = 0; i < CHINACount + 1; i++) {
                st.append("　");
            }
            for (int i = 0; i < numCount + 1; i++) {
                st.append(" ");
            }
            for (int i = 0; i < ABCCount + 1; i++) {
                st.append(" ");
            }
            String title = st + mallSearchGoods.title;
            viewHolder.setText(R.id.textview_mall_name, title);
        } else {
            viewHolder.getView(R.id.textview_tag).setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mallSearchGoods.title)) {
                viewHolder.setText(R.id.textview_mall_name, mallSearchGoods.title);
            }

        }*/
        ImageView img_icon = viewHolder.getView(R.id.img_icon);

        int w = 0;
        int h = 0;
        if (wh != null) {
            w = wh[0];
            h = wh[1];
        }
        int imgW = w / 2 - 5;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imgW, imgW);
        img_icon.setLayoutParams(params);
        RelativeLayout layout_outside_img = viewHolder.getView(R.id.layout_outside_img);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imgW, imgW);
        layout_outside_img.setLayoutParams(lp);
        GlideUtil.loadImg(mContext, mallSearchGoods.thumbnail, img_icon, 0);
        LinearLayout layout_out = viewHolder.getView(R.id.layout_out);
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext(CommodityDetailActivity.class, mallSearchGoods.id);
            }
        });
        viewHolder.setViewVisible(R.id.textview_coupon, View.GONE);
        if (!TextUtils.isEmpty(couponText)) {
            viewHolder.setViewVisible(R.id.textview_coupon, View.VISIBLE);
            viewHolder.setText(R.id.textview_coupon, couponText);
        }
        return viewHolder.getConvertView();
    }

    private void goNext(Class cls, int id) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtra("commodityId", id);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public void setCouponText(String couponText) {
        this.couponText = couponText;
        notifyDataSetChanged();
    }
}
