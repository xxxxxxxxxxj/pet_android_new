package com.haotang.pet.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ScaleImageView;

import java.util.List;

/**
 * <p>Title:${type_name}</p> com.bumptech.glide.request.animation.GlideAnimation
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/1 10:33
 */
public class CommodityDetailImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int screenWidth;

    public CommodityDetailImgAdapter(int layoutResId, List<String> data, Activity mActivity) {
        super(layoutResId, data);
        screenWidth = Utils.getDisplayMetrics(mActivity)[0];
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        final ScaleImageView iv_item_commodity_detailimg = helper.getView(R.id.iv_item_commodity_detailimg);
        /*GlideUtil.loadImg(mContext, item, iv_item_commodity_detailimg,
                R.drawable.icon_production_default);*/
        iv_item_commodity_detailimg.setDrawTopRid(false);
        if (Utils.isStrNull(item)) {
            /*iv_item_commodity_detailimg.post(new Runnable() {
                @Override
                public void run() {*/
            Glide.with(mContext)
                    .load(item)
                    .priority(Priority.NORMAL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_production_default)
                    .error(R.drawable.icon_production_default)
                    .centerCrop()
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int width = resource.getIntrinsicWidth();
                            int height = resource.getIntrinsicHeight();
                            iv_item_commodity_detailimg.setImageWidth(screenWidth);
                            iv_item_commodity_detailimg.setImageHeight(screenWidth * height / width);
                            iv_item_commodity_detailimg.setImageDrawable(resource);
                            iv_item_commodity_detailimg.getLayoutParams().height = screenWidth * height / width;
                        }

                    });
        }
    }
}