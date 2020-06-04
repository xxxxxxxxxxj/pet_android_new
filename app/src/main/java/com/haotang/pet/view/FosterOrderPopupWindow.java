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
 * @date zhoujunxia on 2019-12-16 18:00
 */
public class FosterOrderPopupWindow extends PopupWindow {

    public FosterOrderPopupWindow(Activity context, View.OnClickListener OnClickListener, int status) {
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
        tv_pop_washorder_xgdd.setVisibility(View.GONE);
        if (status == 2 || status == 21) {//已付款或者待入住
            tv_pop_washorder_sqtk.setVisibility(View.VISIBLE);
        } else if (status == 22) {//已入住
            tv_pop_washorder_sqtk.setVisibility(View.GONE);
        }
        // 设置按钮监听
        tv_pop_washorder_sqtk.setOnClickListener(OnClickListener);
        tv_pop_washorder_lxkf.setOnClickListener(OnClickListener);
        // 设置SelectPicPopupWindow的View
        this.setContentView(inflate);
        this.setWidth(DensityUtil/**/.dp2px(context, 77));
        this.setHeight(DensityUtil.dp2px(context, 80));
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(00000000);
        setBackgroundDrawable(dw);
    }
}