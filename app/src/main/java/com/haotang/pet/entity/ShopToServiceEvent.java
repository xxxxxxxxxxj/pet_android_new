package com.haotang.pet.entity;

/**
 * @author:姜谷蓄
 * @Date:2020/3/23
 * @Description:
 */
public class ShopToServiceEvent {
    private int shopId;
    private int cityId;
    private String shopName;
    private String shopPhone;
    private String shopImg;
    private String shopAddr;
    private double lat;
    private double lng;
    private String tag;
    private String opemTime;
    private String dist;

    public ShopToServiceEvent(int shopId, int cityId, String shopName, String shopPhone, String shopImg, String shopAddr, double lat, double lng, String tag, String opemTime, String dist) {
        this.shopId = shopId;
        this.cityId = cityId;
        this.shopName = shopName;
        this.shopPhone = shopPhone;
        this.shopImg = shopImg;
        this.shopAddr = shopAddr;
        this.lat = lat;
        this.lng = lng;
        this.tag = tag;
        this.opemTime = opemTime;
        this.dist = dist;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOpemTime() {
        return opemTime;
    }

    public void setOpemTime(String opemTime) {
        this.opemTime = opemTime;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }
}
