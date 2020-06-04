package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
 * @date XJ on 2017/9/9 15:32
 */
public class AlertDialogEditText {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private ImageView iv_item_shopcart_shopjian;
    private ExtendedEditText et_item_shopcart_shopnum;
    private ImageView iv_item_shopcart_shopjia;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public LinearLayout getlLayout_bg() {
        return lLayout_bg;
    }

    public void setlLayout_bg(LinearLayout lLayout_bg) {
        this.lLayout_bg = lLayout_bg;
    }

    public TextView getTxt_title() {
        return txt_title;
    }

    public void setTxt_title(TextView txt_title) {
        this.txt_title = txt_title;
    }

    public Button getBtn_neg() {
        return btn_neg;
    }

    public void setBtn_neg(Button btn_neg) {
        this.btn_neg = btn_neg;
    }

    public Button getBtn_pos() {
        return btn_pos;
    }

    public void setBtn_pos(Button btn_pos) {
        this.btn_pos = btn_pos;
    }

    public ImageView getImg_line() {
        return img_line;
    }

    public void setImg_line(ImageView img_line) {
        this.img_line = img_line;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public ImageView getIv_item_shopcart_shopjian() {
        return iv_item_shopcart_shopjian;
    }

    public void setIv_item_shopcart_shopjian(ImageView iv_item_shopcart_shopjian) {
        this.iv_item_shopcart_shopjian = iv_item_shopcart_shopjian;
    }

    public ExtendedEditText getEt_item_shopcart_shopnum() {
        return et_item_shopcart_shopnum;
    }

    public void setEt_item_shopcart_shopnum(ExtendedEditText et_item_shopcart_shopnum) {
        this.et_item_shopcart_shopnum = et_item_shopcart_shopnum;
    }

    public ImageView getIv_item_shopcart_shopjia() {
        return iv_item_shopcart_shopjia;
    }

    public void setIv_item_shopcart_shopjia(ImageView iv_item_shopcart_shopjia) {
        this.iv_item_shopcart_shopjia = iv_item_shopcart_shopjia;
    }

    public AlertDialogEditText(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogEditText builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogedittext, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        iv_item_shopcart_shopjian = (ImageView) view.findViewById(R.id.iv_item_shopcart_shopjian);
        et_item_shopcart_shopnum = (ExtendedEditText) view.findViewById(R.id.et_item_shopcart_shopnum);
        iv_item_shopcart_shopjia = (ImageView) view.findViewById(R.id.iv_item_shopcart_shopjia);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);//设置点击空白不消失
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }

    public AlertDialogEditText setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setVisibility(View.GONE);
        } else {
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
        }
        return this;
    }

    public AlertDialogEditText setNavTextColor(int colorId) {
        btn_neg.setTextColor(context.getResources().getColor(colorId));
        return this;
    }

    public AlertDialogEditText setPostTextColor(int colorId) {
        btn_pos.setTextColor(context.getResources().getColor(colorId));
        return this;
    }

    public AlertDialogEditText setTitleTextColor(int colorId) {
        txt_title.setTextColor(context.getResources().getColor(colorId));
        return this;
    }

    public AlertDialogEditText setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialogEditText setEditTextStr(String str) {
        et_item_shopcart_shopnum.setText(str);
        et_item_shopcart_shopnum.setFocusable(true);
        return this;
    }

    public AlertDialogEditText setJiaButton(final View.OnClickListener listener) {
        iv_item_shopcart_shopjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }
    public AlertDialogEditText setJianButton(final View.OnClickListener listener) {
        iv_item_shopcart_shopjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public AlertDialogEditText setPositiveButton(String text,
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
            }
        });
        return this;
    }

    public AlertDialogEditText setNegativeButton(String text,
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

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }
}
