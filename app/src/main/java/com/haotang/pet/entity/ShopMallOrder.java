package com.haotang.pet.entity;

import com.haotang.pet.entity.mallEntity.MallGoods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/30 20:30
 */
public class ShopMallOrder {
    private String thumbnail;
    private int amount;
    private String specName;
    private int orderId;
    private double payPrice;
    private String stateDesc;
    private String statDesc;
    private String marketingTag;
    private int state;
    private String title;
    private ArrayList<MallGoods> mallGoodsList;
    private int orderType;

    @Override
    public String toString() {
        return "ShopMallOrder{" +
                "thumbnail='" + thumbnail + '\'' +
                ", amount=" + amount +
                ", specName='" + specName + '\'' +
                ", orderId=" + orderId +
                ", payPrice=" + payPrice +
                ", stateDesc='" + stateDesc + '\'' +
                ", statDesc='" + statDesc + '\'' +
                ", marketingTag='" + marketingTag + '\'' +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", mallGoodsList=" + mallGoodsList +
                '}';
    }

    public static ShopMallOrder json2Entity(JSONObject json) {
        ShopMallOrder shopMallOrder = new ShopMallOrder();
        try {
            if (json.has("orderType") && !json.isNull("orderType")) {
                shopMallOrder.setOrderType(json.getInt("orderType"));
            }
            if (json.has("thumbnail") && !json.isNull("thumbnail")) {
                shopMallOrder.setThumbnail(json.getString("thumbnail"));
            }
            if (json.has("amount") && !json.isNull("amount")) {
                shopMallOrder.setAmount(json.getInt("amount"));
            }
            if (json.has("specName") && !json.isNull("specName")) {
                shopMallOrder.setSpecName(json.getString("specName"));
            }
            if (json.has("orderId") && !json.isNull("orderId")) {
                shopMallOrder.setOrderId(json.getInt("orderId"));
            }
            if (json.has("payPrice") && !json.isNull("payPrice")) {
                shopMallOrder.setPayPrice(json.getDouble("payPrice"));
            }
            if (json.has("stateDesc") && !json.isNull("stateDesc")) {
                shopMallOrder.setStateDesc(json.getString("stateDesc"));
            }
            if (json.has("statDesc") && !json.isNull("statDesc")) {
                shopMallOrder.setStatDesc(json.getString("statDesc"));
            }
            if (json.has("marketingTag") && !json.isNull("marketingTag")) {
                shopMallOrder.setMarketingTag(json.getString("marketingTag"));
            }
            if (json.has("state") && !json.isNull("state")) {
                shopMallOrder.setState(json.getInt("state"));
            }
            if (json.has("title") && !json.isNull("title")) {
                shopMallOrder.setTitle(json.getString("title"));
            }
            if (json.has("items") && !json.isNull("items")) {
                JSONArray jaitems = json.getJSONArray("items");
                if (jaitems != null && jaitems.length() > 0) {
                    ArrayList<MallGoods> mallGoodsList = new ArrayList<MallGoods>();
                    for (int i = 0; i < jaitems.length(); i++) {
                        mallGoodsList.add(MallGoods
                                .json2Entity(jaitems
                                        .getJSONObject(i)));
                    }
                    shopMallOrder.setMallGoodsList(mallGoodsList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopMallOrder;
    }

    public ArrayList<MallGoods> getMallGoodsList() {
        return mallGoodsList;
    }

    public void setMallGoodsList(ArrayList<MallGoods> mallGoodsList) {
        this.mallGoodsList = mallGoodsList;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getStatDesc() {
        return statDesc;
    }

    public void setStatDesc(String statDesc) {
        this.statDesc = statDesc;
    }

    public String getMarketingTag() {
        return marketingTag;
    }

    public void setMarketingTag(String marketingTag) {
        this.marketingTag = marketingTag;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
