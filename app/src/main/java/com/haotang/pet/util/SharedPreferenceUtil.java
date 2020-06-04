package com.haotang.pet.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Administrator on 2014/12/10.
 */
public class SharedPreferenceUtil {
    private static SharedPreferenceUtil spUtil;
    private SharedPreferences sp;
    private Context context;
    public static String IS_OPEN = "is_Open";
    public static String IS_OPEN_MESSAGE = "duanxin";
    public static String IS_OPEN_PUSH = "is_push";
    private static String IS_SHOP_GUIDE_FIRST = "is_shop_guide_first";
    private static String IS_SHOP_GUIDE_SECOND = "is_shop_guide_second";
    private static String PHONE_SAVE = "phone_save";
    private static String HOTFIX_TIME = "hotfix_time";




    private SharedPreferenceUtil(Context context) {
        this.context  =context;
        sp = context.getSharedPreferences("pet", Context.MODE_PRIVATE);
    }

    private SharedPreferenceUtil(String name, int mode) {
        if (!Utils.isStrNull(name)) {
            name = "pet";
        }
        if (mode <= 0) {
            mode = Context.MODE_PRIVATE;
        }
        sp = context.getSharedPreferences(name, mode);
    }

    public static SharedPreferenceUtil getInstance(Context context) {
        if (null == spUtil)
            spUtil = new SharedPreferenceUtil(context);
        return spUtil;
    }

    public static SharedPreferenceUtil getInstance(String name, int mode) {
        if (null == spUtil)
            spUtil = new SharedPreferenceUtil(name, mode);
        return spUtil;
    }

    public void saveInt(String tag, int value) {
        Editor editor = sp.edit();
        editor.putInt(tag, value);
        editor.apply();
        editor.commit();
    }

    public void saveFloat(String tag, float value) {
        Editor editor = sp.edit();
        editor.putFloat(tag, value);
        editor.apply();
        editor.commit();
    }

    public void saveLong(String tag, long value) {
        Editor editor = sp.edit();
        editor.putLong(tag, value);
        editor.apply();
        editor.commit();
    }

    public void saveString(String tag, String value) {
        Editor editor = sp.edit();
        editor.putString(tag, value);
        editor.apply();
        editor.commit();
    }

    public void saveBoolean(String tag, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(tag, value);
        editor.apply();
        editor.commit();
    }

    public void setPhoneSave(String phone){
        saveString(PHONE_SAVE,phone);
    }

    public String getPhoneSave() {
        return getString(PHONE_SAVE,"");
    }

    public void setHotfixTime(long hotfixTime) {
        saveLong(HOTFIX_TIME,hotfixTime);
    }

    public long getHotfixTime() {
        return getLong(HOTFIX_TIME,0);
    }

    public void setIsShopGuideFirst(boolean value){
        saveBoolean(IS_SHOP_GUIDE_FIRST,value);
    }

    public boolean getIsShopGuideFirst() {
        return getBoolean(IS_SHOP_GUIDE_FIRST,true);
    }

    public void setIsShopGuideSecond(boolean value){
        saveBoolean(IS_SHOP_GUIDE_SECOND,value);
    }

    public boolean getIsShopGuideSecond() {
        return getBoolean(IS_SHOP_GUIDE_SECOND,true);
    }

    public int getInt(String tag, int defaultValue) {
        return sp.getInt(tag, defaultValue);
    }

    public float getFloat(String tag, float defaultValue) {
        return sp.getFloat(tag, defaultValue);
    }

    public long getLong(String tag, long defaultValue) {
        return sp.getLong(tag, defaultValue);
    }

    public String getString(String tag, String defaultValue) {
        return sp.getString(tag, defaultValue);
    }

    public boolean getBoolean(String tag, boolean defaultValue) {
        return sp.getBoolean(tag, defaultValue);
    }

    public void clearData() {
        Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public boolean containData(String tag) {
        return sp.contains(tag);
    }

    public void removeData(String tag) {
        Editor editor = sp.edit();
        editor.remove(tag);
        editor.apply();
        editor.commit();
    }
}
