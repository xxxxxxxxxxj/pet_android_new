package com.haotang.pet.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.DeleteItemEvent;
import com.haotang.pet.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/22 13:45
 */
public class AppointMentPetItemMxAdapter extends BaseQuickAdapter<ApointMentItem, BaseViewHolder> {
    private final int myPetId;
    private final boolean isVip;

    public AppointMentPetItemMxAdapter(int layoutResId, List<ApointMentItem> data, int myPetId,boolean isVip) {
        super(layoutResId, data);
        this.myPetId = myPetId;
        this.isVip = isVip;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ApointMentItem item) {
        TextView tv_item_pet_mx_item_price = helper.getView(R.id.tv_item_pet_mx_item_price);
        ImageView iv_item_pet_mx_item_close = helper.getView(R.id.iv_item_pet_mx_item_close);
        TextView tv_item_pet_mx_itemname = helper.getView(R.id.tv_item_pet_mx_itemname);
        TextView tv_item_pet_mx_item_price_yj = helper.getView(R.id.tv_item_pet_mx_item_price_yj);
        View vw_item_pet_mx = helper.getView(R.id.vw_item_pet_mx);
        if (item != null) {
            if (helper.getLayoutPosition() == mData.size() - 1) {
                vw_item_pet_mx.setVisibility(View.GONE);
            } else {
                vw_item_pet_mx.setVisibility(View.VISIBLE);
            }
            Utils.setText(tv_item_pet_mx_itemname, item.getName(), "", View.VISIBLE, View.VISIBLE);
            if (item.getVipPrice() == item.getPrice()) {
                tv_item_pet_mx_item_price_yj.setVisibility(View.GONE);
                if (isVip) {
                    Utils.setText(tv_item_pet_mx_item_price, "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                } else {
                    Utils.setText(tv_item_pet_mx_item_price, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
                }
            } else {
                tv_item_pet_mx_item_price_yj.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_item_pet_mx_item_price_yj.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_pet_mx_item_price_yj, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_item_pet_mx_item_price, "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
            }
            iv_item_pet_mx_item_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DeleteItemEvent(myPetId,item.getId()));
                }
            });
        }
    }
}
