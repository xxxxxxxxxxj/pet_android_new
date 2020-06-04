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
public class ShopMallFragRecommend {
    private String title;
    private double retailPrice;
    private double ePrice;
    private double marketValue;
    private String thumbnail;
    private int id;
    private int canNum;
    private int vipCanNum;
    private boolean isOther;
    private boolean isAdd;

    public double getePrice() {
        return ePrice;
    }

    public void setePrice(double ePrice) {
        this.ePrice = ePrice;
    }

    public int getVipCanNum() {
        return vipCanNum;
    }

    public void setVipCanNum(int vipCanNum) {
        this.vipCanNum = vipCanNum;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public ShopMallFragRecommend(String title, double retailPrice, double marketValue, String thumbnail, int id, int canNum, boolean isOther, boolean isAdd) {
        this.title = title;
        this.retailPrice = retailPrice;
        this.marketValue = marketValue;
        this.thumbnail = thumbnail;
        this.id = id;
        this.canNum = canNum;
        this.isOther = isOther;
        this.isAdd = isAdd;
    }

    public ShopMallFragRecommend() {
    }

    public ShopMallFragRecommend(String title, double retailPrice, double marketValue, String thumbnail,
                                 int id, int canNum, boolean isOther) {
        this.title = title;
        this.retailPrice = retailPrice;
        this.marketValue = marketValue;
        this.thumbnail = thumbnail;
        this.id = id;
        this.canNum = canNum;
        this.isOther = isOther;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }

    public static ShopMallFragRecommend json2Entity(JSONObject json) {
        ShopMallFragRecommend shopMallFragRecommend = new ShopMallFragRecommend();
        try {
            if (json.has("title") && !json.isNull("title")) {
                shopMallFragRecommend.setTitle(json.getString("title"));
            }
            if (json.has("retailPrice") && !json.isNull("retailPrice")) {
                shopMallFragRecommend.setRetailPrice(json.getDouble("retailPrice"));
            }
            if (json.has("ePrice") && !json.isNull("ePrice")) {
                shopMallFragRecommend.setePrice(json.getDouble("ePrice"));
            }
            if (json.has("marketValue") && !json.isNull("marketValue")) {
                shopMallFragRecommend.setMarketValue(json.getDouble("marketValue"));
            }
            if (json.has("thumbnail") && !json.isNull("thumbnail")) {
                shopMallFragRecommend.setThumbnail(json.getString("thumbnail"));
            }
            if (json.has("id") && !json.isNull("id")) {
                shopMallFragRecommend.setId(json.getInt("id"));
            }
            if (json.has("canNum") && !json.isNull("canNum")) {
                shopMallFragRecommend.setCanNum(json.getInt("canNum"));
            }
            if (json.has("vipCanNum") && !json.isNull("vipCanNum")) {
                shopMallFragRecommend.setVipCanNum(json.getInt("vipCanNum"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopMallFragRecommend;
    }

    public String getTitle() {
        return title;
    }

    public int getCanNum() {
        return canNum;
    }

    public void setCanNum(int canNum) {
        this.canNum = canNum;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
