package com.haotang.pet.entity;

/**
 * @author:姜谷蓄
 * @Date:2020/3/9
 * @Description:
 */
public class ShopLocationEvent {
    private double lat;
    private double lng;
    private int shopId;
    private String shopName;
    private String cityName;

    public ShopLocationEvent(double lat, double lng, int shopId, String shopName, String cityName) {
        this.lat = lat;
        this.lng = lng;
        this.shopId = shopId;
        this.shopName = shopName;
        this.cityName = cityName;
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

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
