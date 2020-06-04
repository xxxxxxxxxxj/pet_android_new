package com.haotang.pet.entity.mallEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class MallCommodity {
    public String mallCommodityCanTitle;
    public String mallCommodityVipCanTitle;
    public String thumbnail;
    public double marketValue;
    public String title;
    public int vipCanNum;
    public int canNum;
    public String subtitle;
    public String marketingTag;
    public int id;
    public double retailPrice;
    public double ePrice;

    public static MallCommodity j2Entity(JSONObject object) {
        MallCommodity mallCommodity = new MallCommodity();
        try {
            if (object.has("ePrice") && !object.isNull("ePrice")) {
                mallCommodity.ePrice = object.getDouble("ePrice");
            }
            if (object.has("mallCommodityCanTitle") && !object.isNull("mallCommodityCanTitle")) {
                mallCommodity.mallCommodityCanTitle = object.getString("mallCommodityCanTitle");
            }
            if (object.has("mallCommodityVipCanTitle") && !object.isNull("mallCommodityVipCanTitle")) {
                mallCommodity.mallCommodityVipCanTitle = object.getString("mallCommodityVipCanTitle");
            }
            if (object.has("thumbnail") && !object.isNull("thumbnail")) {
                mallCommodity.thumbnail = object.getString("thumbnail");
            }
            if (object.has("marketValue") && !object.isNull("marketValue")) {
                mallCommodity.marketValue = object.getDouble("marketValue");
            }
            if (object.has("title") && !object.isNull("title")) {
                mallCommodity.title = object.getString("title");
            }
            if (object.has("vipCanNum") && !object.isNull("vipCanNum")) {
                mallCommodity.vipCanNum = object.getInt("vipCanNum");
            }
            if (object.has("canNum") && !object.isNull("canNum")) {
                mallCommodity.canNum = object.getInt("canNum");
            }
            if (object.has("subtitle") && !object.isNull("subtitle")) {
                mallCommodity.subtitle = object.getString("subtitle");
            }
            if (object.has("marketingTag") && !object.isNull("marketingTag")) {
                mallCommodity.marketingTag = object.getString("marketingTag");
            }
            if (object.has("id") && !object.isNull("id")) {
                mallCommodity.id = object.getInt("id");
            }
            if (object.has("retailPrice") && !object.isNull("retailPrice")) {
                mallCommodity.retailPrice = object.getDouble("retailPrice");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mallCommodity;
    }
}
