package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/22
 * @Description:新版优惠券页面适配器
 */
public class CouponNewAdapter<T> extends CommonAdapter<T> {
    public CouponNewAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final MyCouponCanUse myCouponCanUse = (MyCouponCanUse) mDatas.get(i);
        ViewHolder viewHolder = ViewHolder.get(mContext, view, viewGroup, R.layout.item_mycoupon_list, i);
        //是否可赠送
        if (myCouponCanUse.isCanGive == 0) {
            viewHolder.setViewVisible(R.id.rl_coupon_share, View.VISIBLE);
        } else {
            viewHolder.setViewVisible(R.id.rl_coupon_share, View.GONE);
        }
        //是否是好友赠送
        if (myCouponCanUse.isGive==1){
            viewHolder.setViewVisible(R.id.iv_coupon_othergive,View.VISIBLE);
        }else {
            viewHolder.setViewVisible(R.id.iv_coupon_othergive,View.GONE);
        }
        viewHolder.setViewVisible(R.id.layout_is_show_unused, View.GONE);
        viewHolder.setViewVisible(R.id.img_select, View.GONE);
        viewHolder.setText(R.id.tv_coupon_name, myCouponCanUse.name);
        viewHolder.setText(R.id.tv_coupon_usetime, myCouponCanUse.startTime + "-" + myCouponCanUse.endTime);
        viewHolder.setText(R.id.tv_coupon_desc, myCouponCanUse.description);
        switch (myCouponCanUse.category) {
            case 1:
            case 2:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_service);
                viewHolder.setViewVisible(R.id.iv_usecoupon_buy, View.GONE);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FE7567");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FE7567");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FE7567");
                break;
            case 5:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_goods);
                viewHolder.setViewVisible(R.id.iv_usecoupon_buy, View.GONE);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FFBB996C");
                break;
            case 3:
            case 6:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_goods);
                viewHolder.setViewVisible(R.id.iv_usecoupon_buy, View.VISIBLE);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FFBB996C");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FFBB996C");
                break;
            case 7:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_foster);
                viewHolder.setViewVisible(R.id.iv_usecoupon_buy, View.GONE);
                viewHolder.setTextColor(R.id.tv_coupon_usetext, "#FFFC3962");
                viewHolder.setTextColor(R.id.tv_coupon_leastrmb, "#FFFC3962");
                viewHolder.setTextColor(R.id.tv_coupon_leastnum, "#FFFC3962");
                break;
        }
        if (myCouponCanUse.isToExpire == 0) {//0非即将过期 1即将过期
            viewHolder.setViewVisible(R.id.tv_coupon_willend, View.GONE);
        } else {
            viewHolder.setViewVisible(R.id.tv_coupon_willend, View.VISIBLE);
        }
        if (myCouponCanUse.reduceType == 3) {//免单券
            viewHolder.setViewVisible(R.id.rl_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.tv_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.tv_coupon_usetext, View.VISIBLE);
            viewHolder.setText(R.id.tv_coupon_usetext, "免单");
        } else if (myCouponCanUse.reduceType == 1) {//满减券
            if (myCouponCanUse.least==null||"".equals(myCouponCanUse.least)){
                viewHolder.setViewVisible(R.id.tv_coupon_least, View.GONE);
            }else {
                viewHolder.setViewVisible(R.id.tv_coupon_least, View.VISIBLE);
                viewHolder.setText(R.id.tv_coupon_least, myCouponCanUse.least);
            }
            viewHolder.setViewVisible(R.id.rl_coupon_least, View.VISIBLE);
            viewHolder.setViewVisible(R.id.tv_coupon_usetext, View.GONE);
            viewHolder.setText(R.id.tv_coupon_leastnum, Utils.doubleTrans(myCouponCanUse.amount));
        } else if (myCouponCanUse.reduceType == 2) {//折扣券
            String text = "";
            if (Utils.isDoubleEndWithZero(ComputeUtil.mul(myCouponCanUse.zhekou, 10))) {
                text = Utils.formatDouble(ComputeUtil.mul(myCouponCanUse.zhekou, 10)) + "折";
            } else {
                text = ComputeUtil.mul(myCouponCanUse.zhekou, 10) + "折";
            }
            viewHolder.setViewVisible(R.id.tv_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.rl_coupon_least, View.GONE);
            viewHolder.setViewVisible(R.id.tv_coupon_usetext, View.VISIBLE);
            viewHolder.setText(R.id.tv_coupon_usetext, text);
        }
        //用券买单
        viewHolder.getView(R.id.iv_usecoupon_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyWithCouponListener.OnCounponBuy(myCouponCanUse.CouponId,myCouponCanUse.name);
            }
        });
        //优惠券详情
        viewHolder.getView(R.id.rl_couponlist_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(i);
            }
        });
        return viewHolder.getConvertView();
    }

    //点击分享
    public OnShareListener onShareListener = null;

    public interface OnShareListener {
        void OnShare(int position);
    }

    public void setOnShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }

    //点击用券购买
    public BuyWithCouponListener buyWithCouponListener = null;

    public interface BuyWithCouponListener {
        void OnCounponBuy(int id,String name);
    }

    public void setBuyWithCouponListener(BuyWithCouponListener buyWithCouponListener) {
        this.buyWithCouponListener = buyWithCouponListener;
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
