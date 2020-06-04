package com.haotang.pet.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.CardOrderDetail;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/25
 * @Description:订单赠送优惠券列表的适配器
 */
public class GiftOrderlFreeAdapter extends BaseQuickAdapter<CardOrderDetail.DataBean.CouponListBean,BaseViewHolder>{
    public GiftOrderlFreeAdapter(@LayoutRes int layoutResId, @Nullable List<CardOrderDetail.DataBean.CouponListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardOrderDetail.DataBean.CouponListBean item) {
        TextView tv_freename = helper.getView(R.id.tv_item_giftdetail_freename);
        if (item!=null){
            tv_freename.setText(item.getName()+"("+item.getAmount()+"张)");
        }
    }
}
