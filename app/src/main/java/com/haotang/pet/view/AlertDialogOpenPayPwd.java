package com.haotang.pet.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.ScreenUtil;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-08-29 17:21
 */
public class AlertDialogOpenPayPwd {
    private Activity context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView tv_openpwddialog_open;
    private ImageView iv_openpwddialog_close;

    public AlertDialogOpenPayPwd(Activity context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public AlertDialogOpenPayPwd builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogopenpaypwd, null);
        // 获取自定义Dialog布局中的控件
        tv_openpwddialog_open = (TextView) view
                .findViewById(R.id.tv_openpwddialog_open);
        iv_openpwddialog_close = (ImageView) view
                .findViewById(R.id.iv_openpwddialog_close);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(context)));
        return this;
    }

    public AlertDialogOpenPayPwd setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public AlertDialogOpenPayPwd setCloseButton(final View.OnClickListener listener) {
        iv_openpwddialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogOpenPayPwd setOpenButton(final View.OnClickListener listener) {
        tv_openpwddialog_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }
}
