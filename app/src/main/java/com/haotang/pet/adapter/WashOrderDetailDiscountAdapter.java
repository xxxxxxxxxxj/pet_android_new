package com.haotang.pet.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.WashDiscount;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/5/28 12:09
 */
public class WashOrderDetailDiscountAdapter extends BaseQuickAdapter<WashDiscount, BaseViewHolder> {

    public WashOrderDetailDiscountAdapter(int layoutResId, List<WashDiscount> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WashDiscount item) {
        TextView tv_item_discount_price = helper.getView(R.id.tv_item_discount_price);
        TextView tv_item_discount_name = helper.getView(R.id.tv_item_discount_name);
        if (item != null) {
            if (Integer.parseInt(item.getType()) == 1 || Integer.parseInt(item.getType()) == 7 || Integer.parseInt(item.getType()) == 8) {
                Utils.setText(tv_item_discount_price, "-¥" + item.getAmount(), "", View.VISIBLE, View.VISIBLE);
            } else {
                if (Utils.isStrNull(item.getName())) {
                    Utils.setText(tv_item_discount_price, "-¥" + item.getAmount() + "(" + item.getName() + ")", "", View.VISIBLE, View.VISIBLE);
                } else {
                    Utils.setText(tv_item_discount_price, "-¥" + item.getAmount(), "", View.VISIBLE, View.VISIBLE);
                }
            }
            if (Integer.parseInt(item.getType()) == 1) {//服务卡减免类型标识
                tv_item_discount_name.setText("E卡折扣优惠");
            } else if (Integer.parseInt(item.getType()) == 2) {//优惠券减免类型标识
                tv_item_discount_name.setText("优惠券");
            } else if (Integer.parseInt(item.getType()) == 3) {//上门优惠券减免类型标识
                tv_item_discount_name.setText("上门服务费优惠券");
            } else if (Integer.parseInt(item.getType()) == 4) {//次卡减免类型标识
                tv_item_discount_name.setText("次卡");
            } else if (Integer.parseInt(item.getType()) == 5) {//罐头币减免类型标识
                tv_item_discount_name.setText("罐头币减免");
            } else if (Integer.parseInt(item.getType()) == 6) {//上门服务费减免类型标识
                tv_item_discount_name.setText("上门服务费减免");
            } else if (Integer.parseInt(item.getType()) == 7) {//返现抵扣标识
                tv_item_discount_name.setText("返现抵扣");
            } else if (Integer.parseInt(item.getType()) == 8) {//刷牙年卡抵扣
                tv_item_discount_name.setText(item.getName());
            }
        }
    }
}
