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
 * @date zhoujunxia on 2019-12-16 11:53
 */
public class FosterLiveListPetAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public FosterLiveListPetAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        ImageView iv_item_fosterlivelistpet = helper.getView(R.id.iv_item_fosterlivelistpet);
        GlideUtil.loadCircleImg(mContext, item, iv_item_fosterlivelistpet, R.drawable.user_icon_unnet_circle);
    }
}