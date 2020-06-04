package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/23 17:21
 */
public class MyBill {
    private String groupTime;
    private String item;
    private String amount;
    private String tradeNo;
    private String icon;
    private int payWay;
    private int isMixPay;
    private String state;
    private String tradeDate;
    private String desc;
    private String cost;
    private boolean isMonth;
    private List<GoodsBill> goodsList = new ArrayList<GoodsBill>();
    private List<PayWay> payWayList = new ArrayList<PayWay>();
    private String discountPriceTitle;
    private String thirdPriceTitle;
    private String discountPrice;
    private String thirdPrice;
    private long headerId;

    public static MyBill json2Entity(JSONObject jobj) {
        MyBill myBill = new MyBill();
        try {
            if (jobj.has("discountPriceTitle") && !jobj.isNull("discountPriceTitle")) {
                myBill.setDiscountPriceTitle(jobj.getString("discountPriceTitle"));
            }
            if (jobj.has("thirdPriceTitle") && !jobj.isNull("thirdPriceTitle")) {
                myBill.setThirdPriceTitle(jobj.getString("thirdPriceTitle"));
            }
            if (jobj.has("discountPrice") && !jobj.isNull("discountPrice")) {
                myBill.setDiscountPrice(jobj.getString("discountPrice"));
            }
            if (jobj.has("thirdPrice") && !jobj.isNull("thirdPrice")) {
                myBill.setThirdPrice(jobj.getString("thirdPrice"));
            }
            if (jobj.has("desc") && !jobj.isNull("desc")) {
                myBill.setDesc(jobj.getString("desc"));
            }
            if (jobj.has("cost") && !jobj.isNull("cost")) {
                myBill.setCost(jobj.getString("cost"));
            }
            if (jobj.has("groupTime") && !jobj.isNull("groupTime")) {
                myBill.setGroupTime(jobj.getString("groupTime"));
            }
            if (jobj.has("item") && !jobj.isNull("item")) {
                myBill.setItem(jobj.getString("item"));
            }
            if (jobj.has("amount") && !jobj.isNull("amount")) {
                myBill.setAmount(jobj.getString("amount"));
            }
            if (jobj.has("tradeNo") && !jobj.isNull("tradeNo")) {
                myBill.setTradeNo(jobj.getString("tradeNo"));
            }
            if (jobj.has("icon") && !jobj.isNull("icon")) {
                myBill.setIcon(jobj.getString("icon"));
            }
            if (jobj.has("payWay") && !jobj.isNull("payWay")) {
                myBill.setPayWay(jobj.getInt("payWay"));
            }
            if (jobj.has("isMixPay") && !jobj.isNull("isMixPay")) {
                myBill.setIsMixPay(jobj.getInt("isMixPay"));
            }
            if (jobj.has("state") && !jobj.isNull("state")) {
                myBill.setState(jobj.getString("state"));
            }
            if (jobj.has("tradeDate") && !jobj.isNull("tradeDate")) {
                myBill.setTradeDate(jobj.getString("tradeDate"));
            }
            if (jobj.has("detail") && !jobj.isNull("detail")) {
                JSONObject objectDetail = jobj.getJSONObject("detail");
                if (objectDetail.has("items") && !objectDetail.isNull("items")) {
                    JSONArray array = objectDetail.getJSONArray("items");
                    if (array != null && array.length() > 0) {
                        myBill.getGoodsList().clear();
                        for (int i = 0; i < array.length(); i++) {
                            myBill.getGoodsList().add(GoodsBill.json2Entity(array
                                    .getJSONObject(i)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myBill;
    }

    public long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(long headerId) {
        this.headerId = headerId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<PayWay> getPayWayList() {
        return payWayList;
    }

    public void setPayWayList(List<PayWay> payWayList) {
        this.payWayList = payWayList;
    }

    public List<GoodsBill> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsBill> goodsList) {
        this.goodsList = goodsList;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getIsMixPay() {
        return isMixPay;
    }

    public void setIsMixPay(int isMixPay) {
        this.isMixPay = isMixPay;
    }

    public String getState() {
        return state;
    }

    public String getDiscountPriceTitle() {
        return discountPriceTitle;
    }

    public void setDiscountPriceTitle(String discountPriceTitle) {
        this.discountPriceTitle = discountPriceTitle;
    }

    public String getThirdPriceTitle() {
        return thirdPriceTitle;
    }

    public void setThirdPriceTitle(String thirdPriceTitle) {
        this.thirdPriceTitle = thirdPriceTitle;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getThirdPrice() {
        return thirdPrice;
    }

    public void setThirdPrice(String thirdPrice) {
        this.thirdPrice = thirdPrice;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(String groupTime) {
        this.groupTime = groupTime;
    }

    public boolean isMonth() {
        return isMonth;
    }

    public void setMonth(boolean month) {
        isMonth = month;
    }
}
