package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/31 15:55
 */
public class ShoppingCart {
    private int cartId;
    private int classification_id;
    private int comCount;
    private int firstId;
    private String marketTag;
    private double retailPrice;
    private String specName;
    private int status;
    private int stock;
    private String subtitle;
    private String thumbNail;
    private String title;
    private int userId;
    private String className;
    private boolean isSelect;
    private boolean isClassSelect;
    private int classIndex;
    private int commodityId;
    private int restrict;
    private String vipText;
    private String commodityText;
    private double ePrice;

    public double getePrice() {
        return ePrice;
    }

    public void setePrice(double ePrice) {
        this.ePrice = ePrice;
    }

    public String getCommodityText() {
        return commodityText;
    }

    public void setCommodityText(String commodityText) {
        this.commodityText = commodityText;
    }

    public int getRestrict() {
        return restrict;
    }

    public void setRestrict(int restrict) {
        this.restrict = restrict;
    }

    public String getVipText() {
        return vipText;
    }

    public void setVipText(String vipText) {
        this.vipText = vipText;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public boolean isClassSelect() {
        return isClassSelect;
    }

    public void setClassSelect(boolean classSelect) {
        isClassSelect = classSelect;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getClassification_id() {
        return classification_id;
    }

    public void setClassification_id(int classification_id) {
        this.classification_id = classification_id;
    }

    public int getComCount() {
        return comCount;
    }

    public void setComCount(int comCount) {
        this.comCount = comCount;
    }

    public int getFirstId() {
        return firstId;
    }

    public void setFirstId(int firstId) {
        this.firstId = firstId;
    }

    public String getMarketTag() {
        return marketTag;
    }

    public void setMarketTag(String marketTag) {
        this.marketTag = marketTag;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ShoppingCart() {
    }

    public static ShoppingCart json2Entity(JSONObject json) {
        ShoppingCart shoppingCart = new ShoppingCart();
        try {
            if (json.has("cartId") && !json.isNull("cartId")) {
                shoppingCart.setCartId(json.getInt("cartId"));
            }
            if (json.has("classification_id") && !json.isNull("classification_id")) {
                shoppingCart.setClassification_id(json.getInt("classification_id"));
            }
            if (json.has("comCount") && !json.isNull("comCount")) {
                shoppingCart.setComCount(json.getInt("comCount"));
            }
            if (json.has("firstId") && !json.isNull("firstId")) {
                shoppingCart.setFirstId(json.getInt("firstId"));
            }
            if (json.has("marketTag") && !json.isNull("marketTag")) {
                shoppingCart.setMarketTag(json.getString("marketTag"));
            }
            if (json.has("ePrice") && !json.isNull("ePrice")) {
                shoppingCart.setePrice(json.getDouble("ePrice"));
            }
            if (json.has("retailPrice") && !json.isNull("retailPrice")) {
                shoppingCart.setRetailPrice(json.getDouble("retailPrice"));
            }
            if (json.has("specName") && !json.isNull("specName")) {
                shoppingCart.setSpecName(json.getString("specName"));
            }
            if (json.has("status") && !json.isNull("status")) {
                shoppingCart.setStatus(json.getInt("status"));
            }
            if (json.has("stock") && !json.isNull("stock")) {
                shoppingCart.setStock(json.getInt("stock"));
            }
            if (json.has("subtitle") && !json.isNull("subtitle")) {
                shoppingCart.setSubtitle(json.getString("subtitle"));
            }
            if (json.has("thumbNail") && !json.isNull("thumbNail")) {
                shoppingCart.setThumbNail(json.getString("thumbNail"));
            }
            if (json.has("title") && !json.isNull("title")) {
                shoppingCart.setTitle(json.getString("title"));
            }
            if (json.has("userId") && !json.isNull("userId")) {
                shoppingCart.setUserId(json.getInt("userId"));
            }
            if (json.has("commodityId") && !json.isNull("commodityId")) {
                shoppingCart.setCommodityId(json.getInt("commodityId"));
            }
            if (json.has("restrict") && !json.isNull("restrict")) {
                shoppingCart.setRestrict(json.getInt("restrict"));
            }
            if (json.has("vipText") && !json.isNull("vipText")) {
                shoppingCart.setVipText(json.getString("vipText"));
            }
            if (json.has("commodityText") && !json.isNull("commodityText")) {
                shoppingCart.setCommodityText(json.getString("commodityText"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoppingCart;
    }
}
