package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-27 10:34
 */
public class AlertDialogMall {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private CenterTextView txt_title;
    private TextView txt_subtitle;
    private TextView txt_msg;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;

    public AlertDialogMall(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogMall builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogmall, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (CenterTextView) view.findViewById(R.id.txt_title);
        txt_subtitle = (TextView) view.findViewById(R.id.txt_subtitle);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }

    public AlertDialogMall setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setVisibility(View.GONE);
        } else {
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
        }
        return this;
    }

    public AlertDialogMall setSubTitle(String title) {
        if ("".equals(title)) {
            txt_subtitle.setVisibility(View.GONE);
        } else {
            txt_subtitle.setVisibility(View.VISIBLE);
            txt_subtitle.setText(title);
        }
        return this;
    }

    public AlertDialogMall setNegativeButtonVisible(int visibility) {
        btn_neg.setVisibility(visibility);
        img_line.setVisibility(visibility);
        return this;
    }

    public AlertDialogMall setPositiveButtonVisible(int visibility) {
        btn_pos.setVisibility(visibility);
        img_line.setVisibility(visibility);
        return this;
    }

    public AlertDialogMall setMsg(String msg) {
        if ("".equals(msg)) {
            txt_msg.setVisibility(View.GONE);
        } else {
            txt_msg.setText(msg);
            txt_msg.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public AlertDialogMall setNavTextColor(int colorId) {
        btn_neg.setTextColor(context.getResources().getColor(colorId));
        return this;
    }

    public AlertDialogMall setPostTextColor(int colorId) {
        btn_pos.setTextColor(context.getResources().getColor(colorId));
        return this;
    }

    public AlertDialogMall setTitleTextColor(int colorId) {
        txt_title.setTextColor(context.getResources().getColor(colorId));
        return this;
    }

    public AlertDialogMall setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialogMall setPositiveButton(String text,
                                                   final View.OnClickListener listener) {
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogMall setNegativeButton(String text,
                                                   final View.OnClickListener listener) {
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new View.OnClickListener() {
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
