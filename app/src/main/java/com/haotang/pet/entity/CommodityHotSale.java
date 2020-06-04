package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/1 10:32
 */
public class CommodityHotSale {
    private String thumbnail;
    private int flag;
    private double marketValue;
    private int id;
    private String title;
    private double retailPrice;
    private double ePrice;

    public static CommodityHotSale json2Entity(JSONObject json) {
        CommodityHotSale commodityHotSale = new CommodityHotSale();
        try {
            if (json.has("thumbnail") && !json.isNull("thumbnail")) {
                commodityHotSale.setThumbnail(json.getString("thumbnail"));
            }
            if (json.has("flag") && !json.isNull("flag")) {
                commodityHotSale.setFlag(json.getInt("flag"));
            }
            if (json.has("marketValue") && !json.isNull("marketValue")) {
                commodityHotSale.setMarketValue(json.getDouble("marketValue"));
            }
            if (json.has("id") && !json.isNull("id")) {
                commodityHotSale.setId(json.getInt("id"));
            }
            if (json.has("title") && !json.isNull("title")) {
                commodityHotSale.setTitle(json.getString("title"));
            }
            if (json.has("retailPrice") && !json.isNull("retailPrice")) {
                commodityHotSale.setRetailPrice(json.getDouble("retailPrice"));
            }
            if (json.has("ePrice") && !json.isNull("ePrice")) {
                commodityHotSale.setePrice(json.getDouble("ePrice"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commodityHotSale;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public double getePrice() {
        return ePrice;
    }

    public void setePrice(double ePrice) {
        this.ePrice = ePrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
}