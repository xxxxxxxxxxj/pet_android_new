package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.PetArchivesServies;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/16 11:19
 */
public class PetArchivesFwAdapter extends BaseQuickAdapter<PetArchivesServies, BaseViewHolder> {
    public PetArchivesFwAdapter(int layoutResId, List<PetArchivesServies> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PetArchivesServies item) {
        TextView tv_item_petarchives_fw = helper.getView(R.id.tv_item_petarchives_fw);
        ImageView iv_item_petarchives_fw = helper.getView(R.id.iv_item_petarchives_fw);
        if (item != null) {
            Utils.setText(tv_item_petarchives_fw, item.getName(), "", View.VISIBLE, View.VISIBLE);
            Glide.with(mContext).load(item.getImgId()).into(iv_item_petarchives_fw);
        }
    }
}
