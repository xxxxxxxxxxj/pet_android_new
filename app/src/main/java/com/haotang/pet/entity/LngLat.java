package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/4/26 10:36
 */
public class LngLat {
    private double longitude;//经度
    private double lantitude;//维度

    public LngLat() {
    }

    public LngLat(double longitude, double lantitude) {
        this.longitude = longitude;
        this.lantitude = lantitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLantitude() {
        return lantitude;
    }

    public void setLantitude(double lantitude) {
        this.lantitude = lantitude;
    }

    @Override
    public String toString() {
        return "LngLat{" +
                "longitude=" + longitude +
                ", lantitude=" + lantitude +
                '}';
    }
}
