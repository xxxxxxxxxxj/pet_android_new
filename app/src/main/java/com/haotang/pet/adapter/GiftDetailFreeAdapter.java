package com.haotang.pet.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.GiftCardDetail;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/25
 * @Description:赠送优惠券列表的适配器
 */
public class GiftDetailFreeAdapter extends BaseQuickAdapter<GiftCardDetail.DataBean.CouponListBean,BaseViewHolder>{
    public GiftDetailFreeAdapter(@LayoutRes int layoutResId, @Nullable List<GiftCardDetail.DataBean.CouponListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftCardDetail.DataBean.CouponListBean item) {
        TextView tv_freename = helper.getView(R.id.tv_item_giftdetail_freename);
        TextView tv_freenum = helper.getView(R.id.tv_item_giftdetail_num);
        if (item!=null){
            tv_freename.setText(item.getName()+"("+item.getAmount()+"张)");
            tv_freenum.setText("x"+item.getFreeNum());
        }
    }
}
