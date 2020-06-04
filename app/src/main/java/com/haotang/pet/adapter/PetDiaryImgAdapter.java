package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.PetDiaryImg;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/17 18:28
 */
public class PetDiaryImgAdapter extends BaseQuickAdapter<PetDiaryImg, BaseViewHolder> {
    private String[] imgs;

    public PetDiaryImgAdapter(int layoutResId, List<PetDiaryImg> data) {
        super(layoutResId, data);
        imgs = new String[mData.size()];
        for (int i = 0; i < mData.size(); i++) {
            imgs[i] = mData.get(i).getImg();
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, PetDiaryImg item) {
        ImageView iv_item_petdiary_img = helper.getView(R.id.iv_item_petdiary_img);
        TextView tv_item_petdiary_img = helper.getView(R.id.tv_item_petdiary_img);
        if (item != null) {
            GlideUtil.loadImg(mContext, item.getImg(), iv_item_petdiary_img, R.drawable.icon_production_default);
            Utils.setText(tv_item_petdiary_img, item.getName(), "", View.GONE, View.GONE);
            iv_item_petdiary_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.imageBrower(mContext, helper.getLayoutPosition(), imgs);
                }
            });
        }
    }
}
