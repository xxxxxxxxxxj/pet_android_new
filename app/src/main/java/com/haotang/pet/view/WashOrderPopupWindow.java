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
 * @date zhoujunxia on 2019/4/5 00:28
 */
public class WashOrderPopupWindow extends PopupWindow {

    public WashOrderPopupWindow(Activity context, View.OnClickListener OnClickListener, int refType) {
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
        // 设置按钮监听
        if (refType == 4) {
            this.setHeight(DensityUtil.dp2px(context, 80));
            tv_pop_washorder_xgdd.setVisibility(View.GONE);
        } else {
            this.setHeight(DensityUtil.dp2px(context, 122));
            tv_pop_washorder_xgdd.setVisibility(View.VISIBLE);
            tv_pop_washorder_xgdd.setOnClickListener(OnClickListener);
        }
        tv_pop_washorder_sqtk.setOnClickListener(OnClickListener);
        tv_pop_washorder_lxkf.setOnClickListener(OnClickListener);
        // 设置SelectPicPopupWindow的View
        this.setContentView(inflate);
        this.setWidth(DensityUtil/**/.dp2px(context, 77));
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(00000000);
        setBackgroundDrawable(dw);
    }
}