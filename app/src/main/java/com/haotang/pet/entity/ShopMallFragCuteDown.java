package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/30 11:17
 */
public class ShopMallFragCuteDown {
    private String thumbnail;
    private String title;
    private double retailPrice;
    private double marketValue;
    private int id;
    private double ePrice;

    public static ShopMallFragCuteDown json2Entity(JSONObject jobj) {
        ShopMallFragCuteDown shopMallFragCuteDown = new ShopMallFragCuteDown();
        try {
            if (jobj.has("thumbnail") && !jobj.isNull("thumbnail")) {
                shopMallFragCuteDown.setThumbnail(jobj.getString("thumbnail"));
            }
            if (jobj.has("title") && !jobj.isNull("title")) {
                shopMallFragCuteDown.setTitle(jobj.getString("title"));
            }
            if (jobj.has("retailPrice") && !jobj.isNull("retailPrice")) {
                shopMallFragCuteDown.setRetailPrice(jobj.getDouble("retailPrice"));
            }
            if (jobj.has("marketValue") && !jobj.isNull("marketValue")) {
                shopMallFragCuteDown.setMarketValue(jobj.getDouble("marketValue"));
            }
            if (jobj.has("id") && !jobj.isNull("id")) {
                shopMallFragCuteDown.setId(jobj.getInt("id"));
            }
            if (jobj.has("ePrice") && !jobj.isNull("ePrice")) {
                shopMallFragCuteDown.setePrice(jobj.getDouble("ePrice"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopMallFragCuteDown;
    }

    public double getePrice() {
        return ePrice;
    }

    public void setePrice(double ePrice) {
        this.ePrice = ePrice;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
