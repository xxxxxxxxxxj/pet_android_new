package com.haotang.pet.encyclopedias.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.bean.CommentBean;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/8/2 0002.
 */

public class encyDetailEvaAdapter<T> extends CommonAdapter<T> {

    public encyDetailEvaAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentBean commentBean = (CommentBean) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_ency_eva,position);
//        if (TextUtils.isEmpty(commentBean.avatar)){
//            viewHolder.setViewVisible(R.id.ency_img_icon,View.GONE);
//        }else {
//            viewHolder.setViewVisible(R.id.ency_img_icon,View.VISIBLE);
            GlideUtil.loadCircleImg(mContext,commentBean.avatar,(ImageView)viewHolder.getView(R.id.ency_img_icon),R.drawable.icon_default);
//        }
        viewHolder.setText(R.id.textview_content_eva,commentBean.content+"");
        viewHolder.setText(R.id.textview_ency_username,commentBean.userName+"");
        viewHolder.setText(R.id.textview_ency_time,commentBean.dateStr+"");
        return viewHolder.getConvertView();
    }
}
