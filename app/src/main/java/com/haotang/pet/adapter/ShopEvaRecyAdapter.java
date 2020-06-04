package com.haotang.pet.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.GoShopDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceNewActivity;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/11 0011.
 */

public class ShopEvaRecyAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public int acType = -1;//0 serviceNewActivity , 1 GoShopActivity 2 BeautiDetailActivity
    public ShopEvaRecyAdapter(@LayoutRes int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }
    public void setAcType(int acType){
        this.acType = acType;
    }
    @Override
    protected void convert(final BaseViewHolder helper, Comment item) {
        final Comment comment  = item;
        ImageView img_user_icon = helper.getView(R.id.img_user_icon);
        GlideUtil.loadCircleImg(mContext,comment.avatar,img_user_icon,R.drawable.icon_default);
//        TextView textview_username = helper.getView(R.id.textview_username);
//        TextView textview_appointtime = helper.getView(R.id.textview_appointtime);
//        TextView textview_service_used_time = helper.getView(R.id.textview_service_used_time);
//        TextView textview_eva_content = helper.getView(R.id.textview_eva_content);
//        TextView textview_eva_servicename = helper.getView(R.id.textview_eva_servicename);
        helper.setText(R.id.textview_username,comment.userName+"");
        helper.setText(R.id.textview_appointtime,comment.time+"");
        helper.setText(R.id.textview_service_used_time,"服务时间:"+comment.appointment+"");
        if (TextUtils.isEmpty(comment.contents)){
            helper.getView(R.id.textview_eva_content).setVisibility(View.GONE);
//            viewHolder.setViewVisible(R.id.textview_eva_content,View.GONE);
        }else {
            helper.getView(R.id.textview_eva_content).setVisibility(View.VISIBLE);
            helper.setText(R.id.textview_eva_content,comment.contents+"");
        }
        helper.setText(R.id.textview_eva_servicename,comment.serviceName+"");

        ImageView img_eva_one = helper.getView(R.id.img_eva_one);
        ImageView img_eva_two = helper.getView(R.id.img_eva_two);
        ImageView img_eva_thr = helper.getView(R.id.img_eva_thr);
        ImageView img_eva_four = helper.getView(R.id.img_eva_four);
        ImageView img_eva_five = helper.getView(R.id.img_eva_five);
        List<ImageView> imageEvaList = new ArrayList<>();
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
        ImageView img_one = helper.getView(R.id.img_one);
        ImageView img_two = helper.getView(R.id.img_two);
        ImageView img_thr = helper.getView(R.id.img_thr);
        ImageView img_four = helper.getView(R.id.img_four);
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

        LinearLayout layout_single_open_or_close = helper.getView(R.id.layout_single_open_or_close);
        layout_single_open_or_close.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(comment.extraItems)){
            layout_single_open_or_close.setVisibility(View.VISIBLE);
            TextView textview_open_single_show = helper.getView(R.id.textview_open_single_show);
            textview_open_single_show.setText(comment.extraItems+" ");
        }
        helper.getView(R.id.textview_open_single_show).setVisibility(View.GONE);
        helper.getView(R.id.img_is_open).setBackgroundResource(R.drawable.icon_detail_open);
        if (comment.isOpen){
            helper.getView(R.id.img_is_open).setBackgroundResource(R.drawable.icon_detail_close);
            helper.getView(R.id.textview_open_single_show).setVisibility(View.VISIBLE);
        }
        helper.getView(R.id.layout_single_open_or_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acType==0){
                    ServiceNewActivity.act.setIsOpen(helper.getLayoutPosition(),comment);
                }else if(acType==1){
                    GoShopDetailActivity.act.setIsOpen(helper.getLayoutPosition(),comment);
                }/*else if (acType==2){
                    BeauticianDetailActivity.act.setIsOpen(helper.getLayoutPosition(),comment);
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
        helper.getView(R.id.textViewTags).setVisibility(View.GONE);
//        viewHolder.setViewVisible(R.id.textViewTags,View.GONE);
        if (!TextUtils.isEmpty(comment.commentTags)){
            helper.getView(R.id.textViewTags).setVisibility(View.VISIBLE);
            helper.setText(R.id.textViewTags,comment.commentTags+"");
        }
    }
}
