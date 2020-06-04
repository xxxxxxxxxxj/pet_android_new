package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.hss01248.dialog.ActionSheetDialog;

import java.io.File;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/3/13 11:13
 */
public class WeChatQrCodeDialog {
    private Context context;
    private Dialog dialog;
    private TextView tv_wechatqrcode;
    private ImageButton ib_wechatqrcode_copy;
    private Display display;
    private ImageView iv_wechatqrcode;
    private LinearLayout lLayout_bg;
    private File PetCircle;
    private ImageButton ib_pw_main_close;

    public WeChatQrCodeDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public WeChatQrCodeDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_wechatqrcodedialog, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        tv_wechatqrcode = (TextView) view.findViewById(R.id.tv_wechatqrcode);
        ib_wechatqrcode_copy = (ImageButton) view.findViewById(R.id.ib_wechatqrcode_copy);
        ib_pw_main_close = (ImageButton) view.findViewById(R.id.ib_pw_main_close);
        iv_wechatqrcode = (ImageView) view.findViewById(R.id.iv_wechatqrcode);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(true);//设置点击空白不消失
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth()/* * 0.85*/), LinearLayout.LayoutParams.WRAP_CONTENT));
        PetCircle = new File(Environment.getExternalStorageDirectory(),
                "PetCircle");
        if (!PetCircle.exists()) {
            PetCircle.mkdirs();
        }
        ib_pw_main_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return this;
    }

    public WeChatQrCodeDialog setWeChatCode(String weChatCodeStr) {
        Utils.setText(tv_wechatqrcode, "微信号:" + weChatCodeStr, "微信号:", View.VISIBLE, View.VISIBLE);
        return this;
    }

    public WeChatQrCodeDialog setCopyWeChatCodeClick(final String weChatCodeStr) {
        ib_wechatqrcode_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isStrNull(weChatCodeStr)) {
                    Utils.copy(weChatCodeStr, context);
                    ToastUtil.showToastShortCenter(context, "复制成功");
                }
            }
        });
        return this;
    }

    public WeChatQrCodeDialog setWeChatQrCode(String qrCodeStr) {
        GlideUtil.loadImg(context, qrCodeStr, iv_wechatqrcode, R.drawable.icon_production_default);
        return this;
    }

    public WeChatQrCodeDialog setWeChatQrCodeLongClick(final String qrCodeStr) {
        iv_wechatqrcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new ActionSheetDialog(context)
                        .builder()
                        //.setTitle("清空消息列表后，聊天记录依然保留，确定要清空消息列表？")
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("保存图片到手机", ActionSheetDialog.SheetItemColor.Orange
                                , new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        String fileName = "Pet_Circle_"
                                                + String.valueOf(System.currentTimeMillis() + ".jpg");
                                        Utils.saveImageView(Utils.getViewBitmap(iv_wechatqrcode), PetCircle, context, fileName);
                                    }
                                }).show();
                return true;
            }
        });
        return this;
    }

    public void show() {
        dialog.show();
    }
}
