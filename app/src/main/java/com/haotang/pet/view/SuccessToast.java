package com.haotang.pet.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Toast;

import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.Utils;

import java.lang.reflect.Method;

public class SuccessToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    WindowManager.LayoutParams layoutParams;
    public SuccessToast(Context context) {
        super(context);

    }

    public void changleW(){
        try {
            Class<?> toastClass = Class.forName("Toast");
            Method method = toastClass.getMethod("getWindowParams");
            layoutParams = (WindowManager.LayoutParams) method.invoke(toastClass.newInstance());
            layoutParams.width = ScreenUtil.getScreenWidth(getView().getContext());
            getView().setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.mLogError("改变 "+e.getLocalizedMessage());
        }
    }

}
