package com.haotang.pet.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/6/22 17:42
 */
public class ChooseMyPetAdapter extends BaseQuickAdapter<Pet, BaseViewHolder> {
    private final int previous;

    public ChooseMyPetAdapter(int layoutResId, List<Pet> data, int previous) {
        super(layoutResId, data);
        this.previous = previous;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Pet item) {
        ImageView iv_item_choosemypet_select = helper.getView(R.id.iv_item_choosemypet_select);
        ImageView iv_item_choosemypet_img = helper.getView(R.id.iv_item_choosemypet_img);
        TextView tv_item_choosemypet_name = helper.getView(R.id.tv_item_choosemypet_name);
        TextView tv_item_choosemypet_age = helper.getView(R.id.tv_item_choosemypet_age);
        TextView tv_item_choosemypet_petname = helper.getView(R.id.tv_item_choosemypet_petname);
        TextView tv_item_choosemypet_desc = helper.getView(R.id.tv_item_choosemypet_desc);
        RelativeLayout rl_item_choosemypet_root = helper.getView(R.id.rl_item_choosemypet_root);
        RelativeLayout rl_item_choosemypet_del = helper.getView(R.id.rl_item_choosemypet_del);
//        SwipeMenuLayout sml_item_choosemypet = helper.getView(R.id.sml_item_choosemypet);
        if (helper.getLayoutPosition() == 0) {
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) rl_item_choosemypet_root.getLayoutParams();
            layoutParams.topMargin = DensityUtil.dp2px(mContext, 5);
            rl_item_choosemypet_root.setLayoutParams(layoutParams);
        }
        if (item != null) {
            GlideUtil.loadCircleImg(mContext, item.image, iv_item_choosemypet_img, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_choosemypet_name, item.nickName, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_choosemypet_age, item.month, "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_choosemypet_petname, item.name, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_choosemypet_desc, item.marked, "", View.VISIBLE, View.VISIBLE);
            if (item.isShowDel){
                rl_item_choosemypet_del.setVisibility(View.VISIBLE);
            }else {
                rl_item_choosemypet_del.setVisibility(View.GONE);
            }
            if (previous == Global.MY_CUSTOMERPET) {
                iv_item_choosemypet_select.setVisibility(View.GONE);
            } else {
                iv_item_choosemypet_select.setVisibility(View.VISIBLE);
                if (item.isSelect()) {
                    iv_item_choosemypet_select.setImageResource(R.drawable.icon_petadd_select);
                } else {
                    iv_item_choosemypet_select.setImageResource(R.drawable.icon_petadd_unselect);
                }
            }
            helper.addOnClickListener(R.id.rl_item_choosemypetinfo).addOnLongClickListener(R.id.rl_item_choosemypetinfo).addOnClickListener(R.id.iv_item_choosemypet_del).addOnClickListener(R.id.rl_item_choosemypet_del);
        }
    }
}