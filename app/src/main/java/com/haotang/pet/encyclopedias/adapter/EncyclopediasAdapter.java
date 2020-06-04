package com.haotang.pet.encyclopedias.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.activity.EncyclopediasDetail;
import com.haotang.pet.encyclopedias.bean.Encyclopedias;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.DisplayUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ScaleImageView;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/2 13:05
 */
public class EncyclopediasAdapter<T> extends CommonAdapter<T> {

    private final int windowWidth;

    public EncyclopediasAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
        windowWidth = DisplayUtil.getWindowWidth(mContext);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_encyclopedias, position);
        LinearLayout ll_item_encyclopedias_root = viewHolder.getView(R.id.ll_item_encyclopedias_root);
        RelativeLayout rl_item_encyclopedias_picorvideo = viewHolder.getView(R.id.rl_item_encyclopedias_picorvideo);
        final ScaleImageView siv_item_encyclopedias = viewHolder.getView(R.id.siv_item_encyclopedias);
        TextView tv_item_encyclopedias_txt = viewHolder.getView(R.id.tv_item_encyclopedias_txt);
        TextView tv_item_encyclopedias_num = viewHolder.getView(R.id.tv_item_encyclopedias_num);
        ImageView iv_item_encyclopedias_userimg = viewHolder.getView(R.id.iv_item_encyclopedias_userimg);
        ImageView iv_item_encyclopedias_video = viewHolder.getView(R.id.iv_item_encyclopedias_video);
        TextView tv_item_encyclopedias_name = viewHolder.getView(R.id.tv_item_encyclopedias_name);
        final Encyclopedias item = (Encyclopedias) mDatas
                .get(position);
        if (item != null) {
            if (position == 0 || position == 1) {
                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) ll_item_encyclopedias_root.getLayoutParams();
                layoutParams.topMargin = DensityUtil.dp2px(mContext, 10);
                ll_item_encyclopedias_root.setLayoutParams(layoutParams);
            }else{
                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) ll_item_encyclopedias_root.getLayoutParams();
                layoutParams.topMargin = DensityUtil.dp2px(mContext, 5);
                ll_item_encyclopedias_root.setLayoutParams(layoutParams);
            }
            Utils.setText(tv_item_encyclopedias_txt, item.getTitle(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_encyclopedias_num, item.getRealReadNum() + "", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_encyclopedias_name, item.getSource(), "", View.VISIBLE, View.VISIBLE);
            GlideUtil.loadCircleImg(mContext, item.getSourceIcon(), iv_item_encyclopedias_userimg, R.drawable.user_icon_unnet_circle);
            if (Utils.isStrNull(item.getListCover())) {
                siv_item_encyclopedias.setDrawTopRid(true);
                rl_item_encyclopedias_picorvideo.setVisibility(View.VISIBLE);
                int imgWidth = (windowWidth - DisplayUtil.dip2px(mContext, 25)) / 2;
                int imgHeight = 0;
                if(item.getListCoverWeight() > 0){
                    imgHeight = imgWidth * item.getListCoverHeight() / item.getListCoverWeight();
                }
                siv_item_encyclopedias.setImageWidth(imgWidth);
                siv_item_encyclopedias.setImageHeight(imgHeight);
                GlideUtil.loadImg(mContext, item.getListCover(), siv_item_encyclopedias,
                        R.drawable.icon_production_default,imgWidth,imgHeight);
                if (item.isVideo) {
                    iv_item_encyclopedias_video.setVisibility(View.VISIBLE);
                    iv_item_encyclopedias_video.bringToFront();
                } else {
                    iv_item_encyclopedias_video.setVisibility(View.GONE);
                }
            } else {
                rl_item_encyclopedias_picorvideo.setVisibility(View.GONE);
            }
            ll_item_encyclopedias_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, EncyclopediasDetail.class).putExtra("infoId", item.getId()));
                }
            });
        }
        return viewHolder.getConvertView();
    }
}