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
 * @date XJ on 2017/9/4 16:24
 */
public class MallOrderDetailGoodItems implements Serializable {
    private String thumbnail;
    private int amount;
    private String specName;
    private String marketingTag;
    private int commodityId;
    private String title;
    private double retailPrice;
    private String expressNo;
    private String express;
    private int classIndex;
    private int lastIndex;
    private int state;
    private String infoMsg;
    private int logId;
    private MallOrderDetailGoodsLogisticsInfo mallOrderDetailGoodsLogisticsInfo;
    private int orderType;

    public String getInfoMsg() {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public MallOrderDetailGoodsLogisticsInfo getMallOrderDetailGoodsLogisticsInfo() {
        return mallOrderDetailGoodsLogisticsInfo;
    }

    public void setMallOrderDetailGoodsLogisticsInfo(MallOrderDetailGoodsLogisticsInfo mallOrderDetailGoodsLogisticsInfo) {
        this.mallOrderDetailGoodsLogisticsInfo = mallOrderDetailGoodsLogisticsInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public static MallOrderDetailGoodItems json2Entity(JSONObject json) {
        MallOrderDetailGoodItems mallOrderDetailGoodItems = new MallOrderDetailGoodItems();
        try {
            if (json.has("orderType") && !json.isNull("orderType")) {
                mallOrderDetailGoodItems.setOrderType(json.getInt("orderType"));
            }
            if (json.has("thumbnail") && !json.isNull("thumbnail")) {
                mallOrderDetailGoodItems.setThumbnail(json.getString("thumbnail"));
            }
            if (json.has("amount") && !json.isNull("amount")) {
                mallOrderDetailGoodItems.setAmount(json.getInt("amount"));
            }
            if (json.has("specName") && !json.isNull("specName")) {
                mallOrderDetailGoodItems.setSpecName(json.getString("specName"));
            }
            if (json.has("marketingTag") && !json.isNull("marketingTag")) {
                mallOrderDetailGoodItems.setMarketingTag(json.getString("marketingTag"));
            }
            if (json.has("commodityId") && !json.isNull("commodityId")) {
                mallOrderDetailGoodItems.setCommodityId(json.getInt("commodityId"));
            }
            if (json.has("title") && !json.isNull("title")) {
                mallOrderDetailGoodItems.setTitle(json.getString("title"));
            }
            if (json.has("retailPrice") && !json.isNull("retailPrice")) {
                mallOrderDetailGoodItems.setRetailPrice(json.getDouble("retailPrice"));
            }
            if (json.has("state") && !json.isNull("state")) {
                mallOrderDetailGoodItems.setState(json.getInt("state"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "items异常 = " + e.toString());
        }
        return mallOrderDetailGoodItems;
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

    public String getMarketingTag() {
        return marketingTag;
    }

    public void setMarketingTag(String marketingTag) {
        this.marketingTag = marketingTag;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
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
}
