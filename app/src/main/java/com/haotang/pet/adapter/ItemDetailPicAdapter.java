package com.haotang.pet.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.util.DisplayUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.ScaleImageView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/23 10:45
 */
public class ItemDetailPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final int windowWidth;

    public ItemDetailPicAdapter(int layoutResId, List<String> data, Activity mActivity) {
        super(layoutResId, data);
        windowWidth = DisplayUtil.getWindowWidth(mActivity);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        final ScaleImageView siv_item_itemdetail_pic = helper.getView(R.id.siv_item_itemdetail_pic);
        GlideUtil.loadImg(mContext, item, siv_item_itemdetail_pic, R.drawable.icon_production_default);
    }
}
