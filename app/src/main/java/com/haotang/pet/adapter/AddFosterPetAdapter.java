package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-21 18:05
 */
public class AddFosterPetAdapter extends BaseQuickAdapter<Pet, BaseViewHolder> {

    public AddFosterPetAdapter(int layoutResId, List<Pet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Pet item) {
        ImageView iv_item_choosemypet_select = helper.getView(R.id.iv_item_choosemypet_select);
        ImageView iv_item_choosemypet_img = helper.getView(R.id.iv_item_choosemypet_img);
        TextView tv_item_choosemypet_name = helper.getView(R.id.tv_item_choosemypet_name);
        TextView tv_item_choosemypet_age = helper.getView(R.id.tv_item_choosemypet_age);
        TextView tv_item_choosemypet_petname = helper.getView(R.id.tv_item_choosemypet_petname);
        TextView tv_item_choosemypet_desc = helper.getView(R.id.tv_item_choosemypet_desc);
        ImageView iv_item_choosemypet_no = helper.getView(R.id.iv_item_choosemypet_no);
        if (item != null) {
            GlideUtil.loadCircleImg(mContext, item.image, iv_item_choosemypet_img, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_choosemypet_name, item.nickName, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_choosemypet_age, item.month, "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_choosemypet_petname, item.name, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_choosemypet_desc, item.marked, "", View.VISIBLE, View.VISIBLE);
            if (item.selected == 2) {//不可选
                iv_item_choosemypet_no.setVisibility(View.VISIBLE);
                iv_item_choosemypet_no.bringToFront();
                iv_item_choosemypet_select.setImageResource(R.drawable.icon_petadd_unselect);
            } else {
                iv_item_choosemypet_no.setVisibility(View.GONE);
                if (item.isAdd) {
                    iv_item_choosemypet_select.setImageResource(R.drawable.icon_foster_select);
                } else {
                    if (item.isSelect()) {
                        iv_item_choosemypet_select.setImageResource(R.drawable.icon_petadd_select);
                    } else {
                        iv_item_choosemypet_select.setImageResource(R.drawable.icon_petadd_unselect);
                    }
                }
            }
        }
    }
}