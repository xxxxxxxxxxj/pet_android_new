package com.haotang.pet.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-25 21:33
 */
public class AlertDialogSuccess {
    private Activity context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private Display display;
    private TextView tv_defaultdialog_msg;
    private TextView tv_defaultdialog_cancel;
    private TextView tv_defaultdialog_submit;

    public AlertDialogSuccess(Activity context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogSuccess builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogsuccess, null);
        // 获取自定义Dialog布局中的控件
        tv_defaultdialog_msg = (TextView) view
                .findViewById(R.id.tv_defaultdialog_msg);
        tv_defaultdialog_cancel = (TextView) view
                .findViewById(R.id.tv_defaultdialog_cancel);
        tv_defaultdialog_submit = (TextView) view
                .findViewById(R.id.tv_defaultdialog_submit);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_defaultdialog_cancel.setVisibility(View.GONE);
        tv_defaultdialog_submit.setVisibility(View.GONE);
        return this;
    }

    public AlertDialogSuccess setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public AlertDialogSuccess setMsg(CharSequence msg) {
        Utils.setText(tv_defaultdialog_msg, msg, "", View.VISIBLE, View.VISIBLE);
        return this;
    }

    public AlertDialogSuccess setPositiveButton(String text,
                                                final View.OnClickListener listener) {
        tv_defaultdialog_submit.setText(text);
        tv_defaultdialog_submit.setVisibility(View.VISIBLE);
        tv_defaultdialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogSuccess setNegativeButton(String text,
                                                final View.OnClickListener listener) {
        tv_defaultdialog_cancel.setText(text);
        tv_defaultdialog_cancel.setVisibility(View.VISIBLE);
        tv_defaultdialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }
}
