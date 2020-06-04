package com.haotang.pet.view.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.haotang.pet.util.Utils;

/**
 *
 * @author xing
 * @date 2017/8/6
 */

public abstract class BasePopup extends PopupWindow{
    protected Context mContext;
    protected View view;
    public BasePopup(Context context)
    {
        super(context);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(getLayoutId(),null);
        this.setContentView(view);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        initData();
        initView();
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels-rect.bottom;
            Utils.mLogError("弹窗高度："+"heightPixels: "+anchor.getResources().getDisplayMetrics().heightPixels+" bottom : "+rect.bottom);
            if (getHeight() == WindowManager.LayoutParams.MATCH_PARENT)
            {
                setHeight(h);
            }
        }
        super.showAsDropDown(anchor);
    }

    protected void initData()
    {}



    public abstract void initView();
    public abstract @LayoutRes int getLayoutId();

    protected  <T extends View>T $(@IdRes int id)
    {
        T t = (T)view.findViewById(id);
        return t;
    }




    protected void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
