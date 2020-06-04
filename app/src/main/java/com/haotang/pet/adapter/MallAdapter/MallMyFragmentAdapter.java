package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallSearchGoods;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TagTextView;
import com.jimi_wu.ptlrecyclerview.SimpleAdapter.SimpleAdapter;
import com.jimi_wu.ptlrecyclerview.SimpleAdapter.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/17 0017.
 */

public class MallMyFragmentAdapter<T> extends SimpleAdapter<T> {
    private int wh[] = null;

    public MallMyFragmentAdapter(Activity context, ArrayList<T> datas, int layoutId) {
        super(context, datas, layoutId);
        wh = Utils.getDisplayMetrics(mContext);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder viewHolder, T data) {
        final MallSearchGoods mallSearchGoods = (MallSearchGoods) data;
        viewHolder.getView(R.id.textview_tag).setVisibility(View.GONE);
        TextView textview_price1 = viewHolder
                .getView(R.id.textview_price1);
        TextView textview_price = viewHolder
                .getView(R.id.textview_price);
        TagTextView textview_mall_name = viewHolder
                .getView(R.id.textview_mall_name);
        TextView tv_price_fh = viewHolder
                .getView(R.id.tv_price_fh);
        if (Utils.isStrNull(mallSearchGoods.marketingTag)) {
            textview_mall_name.setSingleTagAndContent(mallSearchGoods.marketingTag, mallSearchGoods.title);
        } else {
            textview_mall_name.setText(mallSearchGoods.title);
        }/*

        if (!TextUtils.isEmpty(mallSearchGoods.marketingTag)){
            viewHolder.getView(R.id.textview_tag).setVisibility(View.VISIBLE);
            String tag = mallSearchGoods.marketingTag;
            viewHolder.setText(R.id.textview_tag,tag);
            int lengthTag = tag.length();
            int numCount = 0;
            int ABCCount = 0;
            int CHINACount = 0;
            char [] tagsLength = tag.toCharArray();
            for (int i = 0 ;i<tagsLength.length;i++){
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(String.valueOf(tagsLength[i]));
                if(m.matches()){
                    numCount++;
                }
                p=Pattern.compile("[a-zA-Z]");
                m=p.matcher(String.valueOf(tagsLength[i]));
                if(m.matches()){
                    numCount++;
                }
                p=Pattern.compile("[\u4e00-\u9fa5]");
                m=p.matcher(String.valueOf(tagsLength[i]));
                if(m.matches()){
                    CHINACount++;
                }
            }
            Utils.mLogError("==-->numCount "+numCount+" ABCCount "+ABCCount+" CHINACount "+CHINACount);

            StringBuilder st = new StringBuilder();
            for (int i = 0;i<CHINACount+1;i++){
                st.append("　");
            }
            for (int i = 0;i<numCount+1;i++){
                st.append(" ");
            }
            for (int i = 0;i<ABCCount+1;i++){
                st.append(" ");
            }
            String title = st + mallSearchGoods.title;
            viewHolder.setText(R.id.textview_mall_name,title);
        }else{
            viewHolder.getView(R.id.textview_tag).setVisibility(View.GONE);
            viewHolder.setText(R.id.textview_mall_name,mallSearchGoods.title.trim());
        }*/
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
        }
        ImageView img_icon = viewHolder.getView(R.id.img_icon);
        int w = 0;
        int h = 0;
        if (wh!=null){
            w = wh[0];
            h = wh[1];
        }
        int imgW = w/2 - 5;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgW,imgW);
        img_icon.setLayoutParams(params);

        GlideUtil.loadImg(mContext,mallSearchGoods.thumbnail,img_icon,0);
        LinearLayout layout_out = viewHolder.getView(R.id.layout_out);
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Utils.checkLogin(mContext)){
                goNext(CommodityDetailActivity.class,mallSearchGoods.id);
//                }else {
//                    goNext(LoginActivity.class,0);
//                }
            }
        });
    }

    private void goNext(Class cls,int id){
        Intent intent = new Intent(mContext,cls);
        intent.putExtra("commodityId",id);
        mContext.startActivity(intent);
    }
}
