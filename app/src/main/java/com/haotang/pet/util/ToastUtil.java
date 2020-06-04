package com.haotang.pet.util;

import android.content.Context;
import androidx.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.pet.R;

public class ToastUtil {
    private static Toast mToast = null;

    public static void showToast(Context context, String message, int showlocation, int duration) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.mytoast, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
        tvMessage.setText(message);

        if (mToast == null)
            mToast = new Toast(context);
//            mToast.cancel();
//		mToast = new Toast(context);
        mToast.setGravity(showlocation, 0, 0);
        mToast.setDuration(duration);
        mToast.setView(view);
        mToast.show();
    }

    public static void showToastLong(Context context, String message) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.mytoast, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
        tvMessage.setText(message);

        if (mToast == null)
            mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(view);
        mToast.show();
    }

    public static void showToastShort(Context context, String message) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.mytoast, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
        tvMessage.setText(message);

        if (mToast == null)
            mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }

    public static void showToastShortBottom(Context context, String message) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.mytoast, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
        tvMessage.setText(message);
        tvMessage.setTextSize(13f);
        if (mToast == null)
            mToast = new Toast(context);
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }

    public static void showToastShortCenter(Context context, String message) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.mytoast, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
        tvMessage.setText(message);
        if (mToast == null)
            mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }

    public static void showToastShortAddIconCenter(Context context, String message, int imgId) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.mytoast, null);
        LinearLayout layout_old = (LinearLayout) view.findViewById(R.id.layout_old);
        LinearLayout layout_new = (LinearLayout) view.findViewById(R.id.layout_new);
        layout_old.setVisibility(View.GONE);
        layout_new.setVisibility(View.VISIBLE);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message_new);
        ImageView left_toast_icon = (ImageView) view.findViewById(R.id.left_toast_icon);
        left_toast_icon.setVisibility(View.VISIBLE);
        left_toast_icon.setBackgroundResource(imgId);
        tvMessage.setText(message);
        if (mToast == null)
            mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }

    /**
     * 删除成功提示
     * @param mContext
     */
    public static void showDeleteToast(Context mContext) {
        showImageToast(mContext,"删除成功",R.drawable.toast_choose);
    }


    public static void showImageToast(Context mContext, String text, @DrawableRes int imageId) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.mytoast_delete, null);
        TextView tv = view.findViewById(R.id.tv);
        ImageView iv = view.findViewById(R.id.iv);
        tv.setText(text);
        iv.setImageResource(imageId);
        if (mToast == null)
            mToast = new Toast(mContext);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }

    public static void showSuccessToast(Context mContext, String message) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.mytoast_success, null);
        TextView tv_toast_success_msg = (TextView) view.findViewById(R.id.tv_toast_success_msg);
        tv_toast_success_msg.setText(message);
        RelativeLayout rl = view.findViewById(R.id.rl);
        ViewGroup.LayoutParams layoutParams = rl.getLayoutParams();
        layoutParams.width = ScreenUtil.getScreenWidth(mContext);
        layoutParams.height = ScreenUtil.getScreenHeight(mContext);
        rl.setLayoutParams(layoutParams);
        if (mToast == null)
            mToast = new Toast(mContext);
        mToast.setGravity(Gravity.FILL, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }
}
