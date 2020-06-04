package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/12/17
 * @Description:寄养选择宠物
 */
public class ChooseFosterPetAdapter extends BaseQuickAdapter<Pet, BaseViewHolder> {
    private int previous;

    public ChooseFosterPetAdapter(int layoutResId, List<Pet> data) {
        super(layoutResId, data);
    }

    public ChooseFosterPetAdapter(int layoutResId, List<Pet> data, int previous) {
        super(layoutResId, data);
        this.previous = previous;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Pet item) {
        ImageView iv_item_choosemypet_select = helper.getView(R.id.iv_item_choosemypet_select);
        ImageView iv_item_choosemypet_img = helper.getView(R.id.iv_item_choosemypet_img);
        TextView tv_item_choosemypet_name = helper.getView(R.id.tv_item_choosemypet_name);
        TextView tv_item_choosemypet_age = helper.getView(R.id.tv_item_choosemypet_age);
        TextView tv_item_choosemypet_petname = helper.getView(R.id.tv_item_choosemypet_petname);
        TextView tv_item_choosemypet_desc = helper.getView(R.id.tv_item_choosemypet_desc);
        LinearLayout ll_item_choosemypet_unavil = helper.getView(R.id.ll_item_choosemypet_unavil);
        if (item != null) {
            GlideUtil.loadCircleImg(mContext, item.image, iv_item_choosemypet_img, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_choosemypet_name, item.nickName, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_choosemypet_age, item.month, "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_choosemypet_petname, item.name, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_choosemypet_desc, item.marked, "", View.VISIBLE, View.VISIBLE);
            if (item.isShow) {
                ll_item_choosemypet_unavil.setVisibility(View.VISIBLE);
            } else {
                ll_item_choosemypet_unavil.setVisibility(View.GONE);
            }
            if (previous != Global.BUYCARD_TO_CHOOSEPET){
                ll_item_choosemypet_unavil.setVisibility(View.GONE);
            }
            if (previous == Global.BUYCARD_TO_CHOOSEPET && item.selected == 2) {
                iv_item_choosemypet_select.setVisibility(View.GONE);
            } else {
                iv_item_choosemypet_select.setVisibility(View.VISIBLE);
                if (item.isSelect()) {
                    iv_item_choosemypet_select.setImageResource(R.drawable.icon_petadd_select);
                } else {
                    iv_item_choosemypet_select.setImageResource(R.drawable.icon_petadd_unselect);
                }
            }
        }
    }
}
