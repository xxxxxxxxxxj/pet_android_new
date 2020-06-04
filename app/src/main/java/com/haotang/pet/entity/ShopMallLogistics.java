package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/1/31 13:22
 */
public class ShopMallLogistics {
    private String text;
    private String time;

    public ShopMallLogistics(String text, String time) {
        this.text = text;
        this.time = time;
    }

    public ShopMallLogistics() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static ShopMallLogistics json2Entity(JSONObject json) {
        ShopMallLogistics shopMallLogistics = new ShopMallLogistics();
        try {
            if (json.has("text") && !json.isNull("text")) {
                shopMallLogistics.setText(json.getString("text"));
            }
            if (json.has("time") && !json.isNull("time")) {
                shopMallLogistics.setTime(json.getString("time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopMallLogistics;
    }
}
