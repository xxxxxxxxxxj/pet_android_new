package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/9/17 0017.
 */

public class JoinWorkAdapterRemind <T> extends CommonAdapter<T> {
    private int screenWidth;
    //	private int imageWidth;
    private int imageHeight;
    private LinearLayout.LayoutParams lp1;
    public JoinWorkAdapterRemind(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
        screenWidth = Utils.getDisplayMetrics((Activity) mContext)[0];
        imageHeight = screenWidth * 48 / 75;
        lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, imageHeight);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_join_work_adapter, position);
        ImageView img = viewHolder.getView(R.id.item_join_work_imageView);
        img.setLayoutParams(lp1);
        viewHolder.setBackgroundNormal(mContext,R.id.item_join_work_imageView,mDatas.get(position).toString(), R.drawable.icon_production_default);
        return viewHolder.getConvertView();
    }
}
