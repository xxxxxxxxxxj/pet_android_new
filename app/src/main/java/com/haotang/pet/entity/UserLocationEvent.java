package com.haotang.pet.entity;

/**
 * @author:姜谷蓄
 * @Date:2019/10/23
 * @Description:
 */
public class UserLocationEvent {
    private double lng;
    private double lat;

    public UserLocationEvent(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
