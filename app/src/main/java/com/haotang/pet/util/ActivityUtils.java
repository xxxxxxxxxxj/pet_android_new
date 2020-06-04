package com.haotang.pet.util;

import android.content.Context;
import android.content.Intent;

import com.haotang.pet.CheckStatusActivity;
import com.haotang.pet.SetUpPayPwdActivity;

public class ActivityUtils {
    /**
     * 跳转到设置密码
     */
    public static void toSetPassword(Context context){
        context.startActivity(new Intent(context, CheckStatusActivity.class)
                .putExtra("previous",Global.PAYPWD_TO_VERIFCODE)
                .putExtra("flag",Global.FIRST_SET_PASSWORD));
    }
    /**
     * 忘记密码
     */
    public static void toForgetPassword(Context context){
        context.startActivity(new Intent(context, CheckStatusActivity.class)
                .putExtra("previous",Global.PAYPWD_TO_VERIFCODE)
                .putExtra("flag",Global.FORGET_PASSWORD));
    }
    /**
     * 跳转到修改密码
     */
    public static void toUpdatePassword(Context context){
        context.startActivity(new Intent(context, SetUpPayPwdActivity.class)
                .putExtra("previous",Global.PAYPWD_TO_VERIFCODE)
                .putExtra("flag",Global.UPDATE_PASSWORD));
    }

    /**
     * 打开订单界面
     */
    public static void toOrderFragment(Context context){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.mainactivity");
        intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
        context.sendBroadcast(intent);
    }

    /**
     * 跳转更改手机号
     */
    public static void toReplacePhone(Context context){
        context.startActivity(new Intent(context, CheckStatusActivity.class)
                .putExtra("previous",Global.SETREPLACEPHONE_TO_VERIFCODE)
                .putExtra("flag",Global.FIRST_SET_PASSWORD));
    }
}
