package com.haotang.pet.codeview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

public class CodePwdActivity extends Activity implements View.OnClickListener {

    private PopupWindow pWinPayPwdSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_pwd);
        findViewById(R.id.main_wechat).setOnClickListener(this);
        findViewById(R.id.main_sms).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_wechat:
                CodeActivity.inputPassword(this, 6);
                showPayPwdSystem();
                break;
            case R.id.main_sms:
                CodeActivity.inputSmsCode(this, 6);
                break;
        }
    }

    private void showPayPwdSystem() {
        pWinPayPwdSystem = null;
        if (pWinPayPwdSystem == null) {
            ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.paypwdsystem_pop, null);
            pWinPayPwdSystem = new PopupWindow(customView,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            pWinPayPwdSystem.setFocusable(true);// 取得焦点
            //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
            pWinPayPwdSystem.setBackgroundDrawable(new BitmapDrawable());
            //点击外部消失
            pWinPayPwdSystem.setOutsideTouchable(true);
            //设置可以点击
            pWinPayPwdSystem.setTouchable(true);
            //进入退出的动画
            pWinPayPwdSystem.setAnimationStyle(R.style.mypopwindow_anim_style);
            pWinPayPwdSystem.setWidth(Utils.getDisplayMetrics(this)[0]);
            pWinPayPwdSystem.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
            pWinPayPwdSystem.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });
        }
    }
}
