package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/9 15:13
 */
class CommentImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final String[] imgs;

    public CommentImgAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
        imgs = (String[]) mData.toArray();
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        ImageView iv_item_commentimg = helper.getView(R.id.iv_item_commentimg);
        GlideUtil.loadImg(mContext, item, iv_item_commentimg, R.drawable.icon_production_default);
        iv_item_commentimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.imageBrower(mContext, helper.getLayoutPosition(), imgs);
            }
        });
    }
}