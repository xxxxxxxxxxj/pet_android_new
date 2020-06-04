package com.haotang.pet.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.GoShopDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceNewActivity;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.FluidLayout;
import com.haotang.pet.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/13 0013.
 */

public class ShopEvaAdapter<T> extends CommonAdapter<T> {
    public int acType = -1;//0 serviceNewActivity , 1 GoShopActivity 2 BeautiDetailActivity
    public ShopEvaAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }
    public void setAcType(int acType){
        this.acType = acType;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Comment comment = (Comment) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_shopevalist,position);
        viewHolder.setImageGlideCircle(mContext,R.id.img_user_icon,comment.avatar,R.drawable.icon_default);
        viewHolder.setText(R.id.textview_username,comment.userName+"");
        viewHolder.setText(R.id.textview_appointtime,comment.time+"");
        viewHolder.setText(R.id.textview_service_used_time,"服务时间:"+comment.appointment+"");
        if (TextUtils.isEmpty(comment.contents)){
            viewHolder.setViewVisible(R.id.textview_eva_content,View.GONE);
        }else {
            viewHolder.setViewVisible(R.id.textview_eva_content,View.VISIBLE);
            viewHolder.setText(R.id.textview_eva_content,comment.contents+"");
        }
        viewHolder.setText(R.id.textview_eva_servicename,comment.serviceName+"");
        List<ImageView> imageEvaList = new ArrayList<>();
        ImageView img_eva_one = viewHolder.getView(R.id.img_eva_one);
        ImageView img_eva_two = viewHolder.getView(R.id.img_eva_two);
        ImageView img_eva_thr = viewHolder.getView(R.id.img_eva_thr);
        ImageView img_eva_four = viewHolder.getView(R.id.img_eva_four);
        ImageView img_eva_five = viewHolder.getView(R.id.img_eva_five);
        imageEvaList.add(img_eva_one);
        imageEvaList.add(img_eva_two);
        imageEvaList.add(img_eva_thr);
        imageEvaList.add(img_eva_four);
        imageEvaList.add(img_eva_five);
        for (int i =0;i<5;i++){
            GlideUtil.loadImg(mContext,null,imageEvaList.get(i),R.drawable.star_empty);
        }
        for (int i =0;i<comment.grade;i++){
            GlideUtil.loadImg(mContext,null,imageEvaList.get(i),R.drawable.star_full);
        }
        List<ImageView> imageViewList = new ArrayList<>();
        imageViewList.clear();
        ImageView img_one = viewHolder.getView(R.id.img_one);
        ImageView img_two = viewHolder.getView(R.id.img_two);
        ImageView img_thr = viewHolder.getView(R.id.img_thr);
        ImageView img_four = viewHolder.getView(R.id.img_four);
        imageViewList.add(img_one);
        imageViewList.add(img_two);
        imageViewList.add(img_thr);
        imageViewList.add(img_four);
        for (int i =0;i<imageViewList.size();i++){
            imageViewList.get(i).setVisibility(View.GONE);
        }
        if (comment.images!=null){
            if (comment.images.length>0){
                for (int i =0;i<comment.images.length;i++){
                    imageViewList.get(i).setVisibility(View.VISIBLE);
                    GlideUtil.loadImg(mContext,comment.images[i],imageViewList.get(i),R.drawable.icon_default);
                }
            }else {
                for (int i =0;i<imageViewList.size();i++){
                    imageViewList.get(i).setVisibility(View.GONE);
                }
            }
        }else {
            for (int i =0;i<imageViewList.size();i++){
                imageViewList.get(i).setVisibility(View.GONE);
            }
        }
        viewHolder.setViewVisible(R.id.layout_single_open_or_close,View.GONE);
        if (!TextUtils.isEmpty(comment.extraItems)){
            viewHolder.setViewVisible(R.id.layout_single_open_or_close,View.VISIBLE);
            viewHolder.setText(R.id.textview_open_single_show,comment.extraItems+" ");
        }
        viewHolder.setViewVisible(R.id.textview_open_single_show,View.GONE);
        viewHolder.setBackgroundResource(R.id.img_is_open,R.drawable.icon_detail_open);
        if (comment.isOpen){
            viewHolder.setBackgroundResource(R.id.img_is_open,R.drawable.icon_detail_close);
            viewHolder.setViewVisible(R.id.textview_open_single_show,View.VISIBLE);
        }
        viewHolder.getView(R.id.layout_single_open_or_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acType==0){
                    ServiceNewActivity.act.setIsOpen(position,comment);
                }else if(acType==1){
                    GoShopDetailActivity.act.setIsOpen(position,comment);
                }/*else if (acType==2){
                    BeauticianDetailActivity.act.setIsOpen(position,comment);
                }*/
            }
        });
        img_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goNext(0,comment.images);
                Utils.goImageDetail(mContext,0,comment.images);
            }
        });

        img_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goNext(1,comment.images);
                Utils.goImageDetail(mContext,1,comment.images);
            }
        });
        img_thr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goNext(2,comment.images);
//                Utils.imageBrower(mContext,2,comment.images);
                Utils.goImageDetail(mContext,2,comment.images);
            }
        });
        img_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goNext(3,comment.images);
                Utils.goImageDetail(mContext,4,comment.images);
            }
        });
//        FluidLayout fluid_tag_layout = viewHolder.getView(R.id.fluid_tag_layout);
//        setTagList(fluid_tag_layout,list);
        viewHolder.setViewVisible(R.id.textViewTags,View.GONE);
        if (!TextUtils.isEmpty(comment.commentTags)){
            viewHolder.setViewVisible(R.id.textViewTags,View.VISIBLE);
            viewHolder.setText(R.id.textViewTags,comment.commentTags+"");
        }
        return viewHolder.getConvertView();
    }
    //这里传递标签
    private void setTagList(FluidLayout fluid_layout, final ArrayList<String> CommentStars) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(Gravity.TOP);
        for (int i = 0; i < CommentStars.size(); i++) {
            TextView tv = new TextView(mContext);
            tv.setTextColor(Color.parseColor("#BB996C"));
            tv.setText(CommentStars.get(i));
            tv.setTextSize(13);
            tv.setPadding(0, 0, 0, 0);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 20, 5);
            fluid_layout.addView(tv, params);
        }

    }
}
