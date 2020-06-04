package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.CardOrderDetail;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/1/6
 * @Description:使用过优惠券明细列表
 */
public class UseCouponDetailAdapter<T> extends CommonAdapter<T> {

    public UseCouponDetailAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final MyCouponCanUse myCouponCanUse = (MyCouponCanUse) mDatas.get(i);
        ViewHolder viewHolder = ViewHolder.get(mContext, view, viewGroup, R.layout.item_couponuse_list, i);
        //是否可赠送
        viewHolder.setViewVisible(R.id.rl_coupon_share, View.GONE);
        viewHolder.setViewVisible(R.id.rl_coupon_usetime,View.VISIBLE);
        viewHolder.setViewVisible(R.id.iv_coupon_state,View.VISIBLE);
        viewHolder.setImageResource(R.id.iv_coupon_state,R.drawable.icon_coupon_usediscount);
        viewHolder.setText(R.id.tv_coupon_name, myCouponCanUse.name);
        viewHolder.setText(R.id.tv_coupon_usetime, myCouponCanUse.startTime + "-" + myCouponCanUse.endTime);
        viewHolder.setText(R.id.tv_coupon_usedtime,"使用日期"+myCouponCanUse.used);
        viewHolder.setText(R.id.tv_coupon_usemoney,"-"+myCouponCanUse.discountPrice);
        viewHolder.setText(R.id.tv_coupon_desc, myCouponCanUse.description);

        switch (myCouponCanUse.category) {
            case 1:
            case 2:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_service);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FE7567");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FE7567");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FE7567");
                break;
            case 5:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_goods);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FFBB996C");
                break;
            case 3:
            case 6:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_goods);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FFBB996C");
                break;
            case 7:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_foster);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FFFC3962");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FFFC3962");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FFFC3962");
                break;
        }
        if (myCouponCanUse.reduceType == 3) {//免单券
            viewHolder.setViewVisible(R.id.rl_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.tv_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.tv_coupon_usetext, View.VISIBLE);
            viewHolder.setText(R.id.tv_coupon_usetext, "免单");
        } else if (myCouponCanUse.reduceType == 1) {//满减券
            if (myCouponCanUse.least == null || "".equals(myCouponCanUse.least)) {
                viewHolder.setViewVisible(R.id.tv_coupon_least, View.GONE);
            } else {
                viewHolder.setViewVisible(R.id.tv_coupon_least, View.VISIBLE);
                viewHolder.setText(R.id.tv_coupon_least, myCouponCanUse.least);
            }
            viewHolder.setViewVisible(R.id.rl_coupon_least, View.VISIBLE);
            viewHolder.setViewVisible(R.id.tv_coupon_usetext, View.GONE);
            viewHolder.setText(R.id.tv_coupon_leastnum, Utils.doubleTrans(myCouponCanUse.amount));
        } else if (myCouponCanUse.reduceType == 2) {//折扣券
            String text = "";
            if (Utils.isDoubleEndWithZero(ComputeUtil.mul(myCouponCanUse.amount, 10))) {
                text = Utils.formatDouble(ComputeUtil.mul(myCouponCanUse.amount, 10)) + "折";
            } else {
                text = ComputeUtil.mul(myCouponCanUse.amount, 10) + "折";
            }
            viewHolder.setViewVisible(R.id.tv_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.rl_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.tv_coupon_usetext, View.VISIBLE);
            viewHolder.setText(R.id.tv_coupon_usetext, text);
        }

        return viewHolder.getConvertView();
    }

    //点击条目传递id
    public OnClickListener onClickListener = null;

    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
