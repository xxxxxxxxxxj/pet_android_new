package com.haotang.pet.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.DensityUtil;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-26 17:31
 */
public class MallOrderPopupWindow extends PopupWindow {

    public MallOrderPopupWindow(Activity context, View.OnClickListener OnClickListener, int state, int orderExamineState) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_mallorder, null);
        TextView tv_pop_mallorder_sqtk = (TextView) inflate
                .findViewById(R.id.tv_pop_mallorder_sqtk);
        TextView tv_pop_mallorder_lxkf = (TextView) inflate
                .findViewById(R.id.tv_pop_mallorder_lxkf);
        //判断是否已经申请退换
        if (orderExamineState == 1) {
            tv_pop_mallorder_sqtk.setText("退货进度");
        } else if (orderExamineState == 0) {
            if (state == 0) {//待付款
                tv_pop_mallorder_sqtk.setTextColor(context.getResources().getColor(R.color.aD1494F));
                tv_pop_mallorder_sqtk.setText("取消订单");
            } else if (state == 1) {//待发货
                tv_pop_mallorder_sqtk.setTextColor(context.getResources().getColor(R.color.a666666));
                tv_pop_mallorder_sqtk.setText("申请退款");
            } else if (state == 2) {//待收货
                tv_pop_mallorder_sqtk.setTextColor(context.getResources().getColor(R.color.a666666));
                tv_pop_mallorder_sqtk.setText("申请退款");
            } else if (state == 3) {//已完成
                tv_pop_mallorder_sqtk.setTextColor(context.getResources().getColor(R.color.a666666));
                tv_pop_mallorder_sqtk.setText("申请退货");
            }
        }
        // 设置按钮监听在·
        tv_pop_mallorder_sqtk.setOnClickListener(OnClickListener);
        tv_pop_mallorder_lxkf.setOnClickListener(OnClickListener);
        // 设置SelectPicPopupWindow的View
        this.setContentView(inflate);
        this.setWidth(DensityUtil.dp2px(context, 90));
        this.setHeight(DensityUtil.dp2px(context, 90));
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(00000000);
        setBackgroundDrawable(dw);
    }
}