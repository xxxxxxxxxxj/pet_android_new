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

public class ECardDetailPopupWindow extends PopupWindow {

    public ECardDetailPopupWindow(Activity context, View.OnClickListener OnClickListener,int index,int state) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_mallorder, null);
        TextView tv_pop_mallorder_sqtk = (TextView) inflate
                .findViewById(R.id.tv_pop_mallorder_sqtk);
        TextView tv_pop_mallorder_lxkf = (TextView) inflate
                .findViewById(R.id.tv_pop_mallorder_lxkf);
        if (index==1||state==3||state==4||state==5){
            tv_pop_mallorder_sqtk.setVisibility(View.GONE);
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