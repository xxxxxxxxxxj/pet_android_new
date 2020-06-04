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

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-01 16:37
 */
public class CardMenuPopupWindow extends PopupWindow {
    public CardMenuPopupWindow(Activity context, int type, List<String> menuList, View.OnClickListener onClickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_washorder, null);
        TextView tv_pop_washorder_xgdd = (TextView) inflate
                .findViewById(R.id.tv_pop_washorder_xgdd);
        TextView tv_pop_washorder_sqtk = (TextView) inflate
                .findViewById(R.id.tv_pop_washorder_sqtk);
        TextView tv_pop_washorder_lxkf = (TextView) inflate
                .findViewById(R.id.tv_pop_washorder_lxkf);
        tv_pop_washorder_lxkf.setVisibility(View.GONE);
        // 设置按钮监听
        if (type == 1) {//E卡明细
            tv_pop_washorder_xgdd.setText(menuList.get(0));
            tv_pop_washorder_xgdd.setOnClickListener(onClickListener);
            if (menuList.size() == 2) {
                this.setHeight(DensityUtil.dp2px(context, 82));
                tv_pop_washorder_sqtk.setVisibility(View.VISIBLE);
                tv_pop_washorder_sqtk.setText(menuList.get(1));
                tv_pop_washorder_sqtk.setOnClickListener(onClickListener);
            } else {
                this.setHeight(DensityUtil.dp2px(context, 60));
                tv_pop_washorder_sqtk.setVisibility(View.GONE);
            }
        } else if (type == 2) {//E卡订单详情
            if (menuList.size() == 1) {//联系客服
                this.setHeight(DensityUtil.dp2px(context, 60));
                tv_pop_washorder_xgdd.setVisibility(View.GONE);
                tv_pop_washorder_sqtk.setText(menuList.get(0));
                tv_pop_washorder_sqtk.setOnClickListener(onClickListener);
                tv_pop_washorder_sqtk.setVisibility(View.VISIBLE);

            } else if (menuList.size() == 2) {
                this.setHeight(DensityUtil.dp2px(context, 82));
                tv_pop_washorder_xgdd.setVisibility(View.VISIBLE);
                tv_pop_washorder_sqtk.setVisibility(View.VISIBLE);
                tv_pop_washorder_xgdd.setText(menuList.get(0));
                tv_pop_washorder_xgdd.setOnClickListener(onClickListener);
                tv_pop_washorder_sqtk.setText(menuList.get(1));
                tv_pop_washorder_sqtk.setOnClickListener(onClickListener);
            }
        }
        // 设置SelectPicPopupWindow的View
        this.setContentView(inflate);
        this.setWidth(DensityUtil/**/.dp2px(context, 77));
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(00000000);
        setBackgroundDrawable(dw);
    }
}
