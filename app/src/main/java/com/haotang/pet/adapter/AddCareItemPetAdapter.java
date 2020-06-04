package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterAddCareItem;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NiceImageView;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/12/19
 * @Description:寄养添加护理宠物
 */
public class AddCareItemPetAdapter extends BaseQuickAdapter<FosterAddCareItem.DataBean.DatasetBean, BaseViewHolder> {
    public AddCareItemPetAdapter(int layoutResId, List<FosterAddCareItem.DataBean.DatasetBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterAddCareItem.DataBean.DatasetBean item) {
        NiceImageView iv_careitempet_pethead = helper.getView(R.id.iv_careitempet_pethead);
        ImageView iv_careitempet_del = helper.getView(R.id.iv_careitempet_del);
        TextView tv_careitempet_petname = helper.getView(R.id.tv_careitempet_petname);
        LinearLayout ll_careitempet_root = helper.getView(R.id.ll_careitempet_root);
        if (item != null) {
            Utils.setText(tv_careitempet_petname, item.getNickname(), "", View.VISIBLE, View.VISIBLE);
            GlideUtil.loadImg(mContext, item.getAvatar(), iv_careitempet_pethead, R.drawable.icon_production_default);
            if (helper.getLayoutPosition() == 0) {
                iv_careitempet_del.setVisibility(View.GONE);
            } else {
                iv_careitempet_del.bringToFront();
                iv_careitempet_del.setVisibility(View.VISIBLE);
            }
            if (item.isPetSelected()) {
                ll_careitempet_root.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            } else {
                ll_careitempet_root.setBackgroundColor(mContext.getResources().getColor(R.color.aF0F5F9));
            }
            helper.addOnClickListener(R.id.ll_careitempet_root).addOnClickListener(R.id.iv_careitempet_del);
        }
    }
}
