package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/20 18:38
 */
public class AppointMentPetAdapter extends BaseQuickAdapter<ApointMentPet, BaseViewHolder> {
    public OnSwitchServiceListener onSwitchServiceListener = null;
    private boolean isVip;

    public interface OnSwitchServiceListener {
        public void OnSwitchService(int position);
    }

    public void setOnSwitchServiceListener(
            OnSwitchServiceListener onSwitchServiceListener) {
        this.onSwitchServiceListener = onSwitchServiceListener;
    }

    public AppointMentPetAdapter(int layoutResId, List<ApointMentPet> data, boolean isVip) {
        super(layoutResId, data);
        this.isVip = isVip;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ApointMentPet item) {
        LinearLayout ll_item_appoint_pet_switch = helper.getView(R.id.ll_item_appoint_pet_switch);
        ImageView iv_item_appoint_pet_img = helper.getView(R.id.iv_item_appoint_pet_img);
        TextView tv_item_appoint_pet_name = helper.getView(R.id.tv_item_appoint_pet_name);
        TextView tv_item_appoint_pet_price = helper.getView(R.id.tv_item_appoint_pet_price);
        if (item != null) {
            GlideUtil.loadCircleImg(mContext, item.getPetImg(), iv_item_appoint_pet_img, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_appoint_pet_name, item.getPetNickName(), "", View.VISIBLE, View.VISIBLE);
            if (isVip) {
                Utils.setText(tv_item_appoint_pet_price, item.getServiceName() + "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
            } else {
                Utils.setText(tv_item_appoint_pet_price, item.getServiceName() + "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
            }
        }
        ll_item_appoint_pet_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSwitchServiceListener != null) {
                    onSwitchServiceListener.OnSwitchService(helper.getLayoutPosition());
                }
            }
        });
    }
}
