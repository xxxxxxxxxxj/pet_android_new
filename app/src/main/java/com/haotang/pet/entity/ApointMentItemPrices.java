package com.haotang.pet.entity;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/21 15:41
 */
public class ApointMentItemPrices implements Serializable {
    private double price10;
    private double price20;
    private double price30;
    private double vipPrice10;
    private double vipPrice20;
    private double vipPrice30;
    private int myPetId;
    private int extraCardId;

    @Override
    public String toString() {
        return "ApointMentItemPrices{" +
                "price10=" + price10 +
                ", price20=" + price20 +
                ", price30=" + price30 +
                ", vipPrice10=" + vipPrice10 +
                ", vipPrice20=" + vipPrice20 +
                ", vipPrice30=" + vipPrice30 +
                ", myPetId=" + myPetId +
                '}';
    }

    public static ApointMentItemPrices json2Entity(JSONObject jobj) {
        ApointMentItemPrices apointMentItemPrices = new ApointMentItemPrices();
        try {
            if (jobj.has("extraCardId") && !jobj.isNull("extraCardId")) {
                apointMentItemPrices.setExtraCardId(jobj.getInt("extraCardId"));
            }
            if (jobj.has("myPetId") && !jobj.isNull("myPetId")) {
                apointMentItemPrices.setMyPetId(jobj.getInt("myPetId"));
            }
            if (jobj.has("price10") && !jobj.isNull("price10")) {
                apointMentItemPrices.setPrice10(jobj.getDouble("price10"));
            }
            if (jobj.has("price20") && !jobj.isNull("price20")) {
                apointMentItemPrices.setPrice20(jobj.getDouble("price20"));
            }
            if (jobj.has("price30") && !jobj.isNull("price30")) {
                apointMentItemPrices.setPrice30(jobj.getDouble("price30"));
            }
            if (jobj.has("vipPrice10") && !jobj.isNull("vipPrice10")) {
                apointMentItemPrices.setVipPrice10(jobj.getDouble("vipPrice10"));
            }
            if (jobj.has("vipPrice20") && !jobj.isNull("vipPrice20")) {
                apointMentItemPrices.setVipPrice20(jobj.getDouble("vipPrice20"));
            }
            if (jobj.has("vipPrice30") && !jobj.isNull("vipPrice30")) {
                apointMentItemPrices.setVipPrice30(jobj.getDouble("vipPrice30"));
            }
        } catch (Exception e) {
            Log.e("TAG", "jiage()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return apointMentItemPrices;
    }

    public double getPrice10() {
        return price10;
    }

    public void setPrice10(double price10) {
        this.price10 = price10;
    }

    public double getPrice20() {
        return price20;
    }

    public void setPrice20(double price20) {
        this.price20 = price20;
    }

    public double getPrice30() {
        return price30;
    }

    public void setPrice30(double price30) {
        this.price30 = price30;
    }

    public double getVipPrice10() {
        return vipPrice10;
    }

    public void setVipPrice10(double vipPrice10) {
        this.vipPrice10 = vipPrice10;
    }

    public double getVipPrice20() {
        return vipPrice20;
    }

    public void setVipPrice20(double vipPrice20) {
        this.vipPrice20 = vipPrice20;
    }

    public double getVipPrice30() {
        return vipPrice30;
    }

    public int getMyPetId() {
        return myPetId;
    }

    public void setMyPetId(int myPetId) {
        this.myPetId = myPetId;
    }

    public int getExtraCardId() {
        return extraCardId;
    }

    public void setExtraCardId(int extraCardId) {
        this.extraCardId = extraCardId;
    }

    public void setVipPrice30(double vipPrice30) {
        this.vipPrice30 = vipPrice30;
    }
}
