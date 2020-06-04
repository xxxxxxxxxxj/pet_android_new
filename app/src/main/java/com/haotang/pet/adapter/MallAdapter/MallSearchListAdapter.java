package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallSearchGoods;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.TagTextView;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * 搜索结果页面 listview对应 adapter
 * Created by Administrator on 2017/8/29.
 */

public class MallSearchListAdapter<T> extends CommonAdapter<T> {
    public MallSearchListAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MallSearchGoods mallSearchGoods = (MallSearchGoods) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_mallsearch_list, position);
        viewHolder.setImageGlideNormal(mContext, R.id.img_icon, mallSearchGoods.thumbnail, 0);
        viewHolder.getView(R.id.textview_search_tag).setVisibility(View.GONE);
        TextView textview_price1 = viewHolder
                .getView(R.id.textview_price1);
        TextView textview_price = viewHolder
                .getView(R.id.textview_price);
        TagTextView textview_title = viewHolder
                .getView(R.id.textview_title);
        TextView tv_price_fh = viewHolder
                .getView(R.id.tv_price_fh);
        if (Utils.isStrNull(mallSearchGoods.marketingTag)) {
            textview_title.setSingleTagAndContent(mallSearchGoods.marketingTag, mallSearchGoods.title);
        } else {
            textview_title.setText(mallSearchGoods.title);
        }
        if (mallSearchGoods.ePrice > 0) {
            textview_price.setVisibility(View.VISIBLE);
            tv_price_fh.setVisibility(View.VISIBLE);
            String text = "";
            if (Utils.isDoubleEndWithZero(mallSearchGoods.ePrice)) {
                text = "" + Utils.formatDouble(mallSearchGoods.ePrice);
            } else {
                text = "" + mallSearchGoods.ePrice;
            }
            textview_price.setText(text);
        } else {
            textview_price.setVisibility(View.GONE);
            tv_price_fh.setVisibility(View.GONE);
        }
        if (mallSearchGoods.retailPrice > 0) {
            textview_price1.setVisibility(View.VISIBLE);
            String text = "";
            if (Utils.isDoubleEndWithZero(mallSearchGoods.retailPrice)) {
                text = "¥" + Utils.formatDouble(mallSearchGoods.retailPrice) + "市场价";
            } else {
                text = "¥" + mallSearchGoods.retailPrice + "市场价";
            }
            textview_price1.setText(text);
            textview_price1.getPaint().setAntiAlias(true);//抗锯齿
            textview_price1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        } else {
            textview_price1.setVisibility(View.GONE);
        }/*
        if (!TextUtils.isEmpty(mallSearchGoods.marketingTag)) {
            viewHolder.getView(R.id.textview_search_tag).setVisibility(View.VISIBLE);
            String tag = mallSearchGoods.marketingTag;
            viewHolder.setText(R.id.textview_search_tag, tag);
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
            viewHolder.setText(R.id.textview_title, title);
        } else {
            viewHolder.getView(R.id.textview_search_tag).setVisibility(View.GONE);
            viewHolder.setText(R.id.textview_title, mallSearchGoods.title.trim());
        }*/
        if (position == 0) {
            viewHolder.getView(R.id.view_line).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
        }
        return viewHolder.getConvertView();
    }
}
