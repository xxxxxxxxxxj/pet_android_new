package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.pet.R;

public class AlertDialogListNavAndPost {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private Button btn_neg;
    private Button btn_pos;
    private Display display;
    public MListview mlv_appointmentnew_pet;
    private ImageView img_line;
    public ListView lv_appointmentnew_noshop;

    public AlertDialogListNavAndPost(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogListNavAndPost builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialoglistnavandpost, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        mlv_appointmentnew_pet = (MListview) view.findViewById(R.id.mlv_appointmentnew_pet);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        lv_appointmentnew_noshop = (ListView) view.findViewById(R.id.lv_appointmentnew_noshop);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);//设置点击空白不消失
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
        return this;
    }

    public AlertDialogListNavAndPost setNavVisible(int visible) {
        btn_neg.setVisibility(visible);
        img_line.setVisibility(visible);
        return this;
    }

    public AlertDialogListNavAndPost setPetListVisible(int visible) {
        mlv_appointmentnew_pet.setVisibility(visible);
        return this;
    }

    public AlertDialogListNavAndPost setPostVisible(int visible) {
        btn_pos.setVisibility(visible);
        img_line.setVisibility(visible);
        return this;
    }

    public AlertDialogListNavAndPost setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setVisibility(View.GONE);
        } else {
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
        }
        return this;
    }

    public AlertDialogListNavAndPost setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialogListNavAndPost setPositiveButton(String text,
                                                       final OnClickListener listener) {
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogListNavAndPost setNegativeButton(String text,
                                                       final OnClickListener listener) {
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public void show() {
        dialog.show();
    }
}
