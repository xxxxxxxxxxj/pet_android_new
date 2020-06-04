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
 * @Date:2019/11/23
 * @Description:优惠券历史页面适配器
 */

public class CouponTimeOutAdapter<T> extends CommonAdapter<T> {

    public CouponTimeOutAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyCouponCanUse myCouponCanUse = (MyCouponCanUse) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_couponhistory_list, position);
        //是否可赠送
        if (myCouponCanUse.isCanGive == 0) {
            viewHolder.setViewVisible(R.id.rl_coupon_share, View.VISIBLE);
        } else {
            viewHolder.setViewVisible(R.id.rl_coupon_share, View.GONE);
        }
        viewHolder.setText(R.id.tv_coupon_name, myCouponCanUse.name);
        viewHolder.setText(R.id.tv_coupon_usetime, myCouponCanUse.startTime + "-" + myCouponCanUse.endTime);
        viewHolder.setText(R.id.tv_coupon_desc, myCouponCanUse.description);
        if (myCouponCanUse.status == 2) {//已使用
            viewHolder.setImageResource(R.id.iv_coupon_state, R.drawable.icon_coupon_isused);
        } else if (myCouponCanUse.status == 4) {//已送出
            viewHolder.setImageResource(R.id.iv_coupon_state, R.drawable.icon_coupon_isgive);
        } else if (myCouponCanUse.status == 3) {//已过期
            viewHolder.setImageResource(R.id.iv_coupon_state, R.drawable.icon_coupon_outtime);
        }
        switch (myCouponCanUse.category) {
            case 1:
            case 2:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_servicenouse);
                break;
            case 3:
            case 5:
            case 6:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_goodsnouse);
                break;
            case 7:
                viewHolder.setImageResource(R.id.iv_coupon_type, R.drawable.icon_coupon_fosternouse);
                break;
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

        return viewHolder.getConvertView();
    }

    public OnShareListener onShareListener = null;

    public interface OnShareListener {
        public void OnShare(int position);
    }

    public void setOnShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }

    public void setNotif() {
        notifyDataSetChanged();
    }
}
