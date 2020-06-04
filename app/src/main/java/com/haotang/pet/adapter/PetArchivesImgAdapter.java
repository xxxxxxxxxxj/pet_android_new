package com.haotang.pet.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.util.GlideUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/16 11:10
 */
public class PetArchivesImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PetArchivesImgAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv_item_petarchives_img = helper.getView(R.id.iv_item_petarchives_img);
        GlideUtil.loadRoundImg(mContext, item, iv_item_petarchives_img, R.drawable.icon_production_default, 5);
    }
}
