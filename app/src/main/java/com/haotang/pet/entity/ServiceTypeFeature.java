package com.haotang.pet.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class ServiceTypeFeature implements Serializable{
    public String name;
    public String desc_bottom;
    public String price;
    public String vip_price;
    public int service_id;

    public static ServiceTypeFeature j2Entity(JSONObject object) {
        ServiceTypeFeature serviceTypeFeature = new ServiceTypeFeature();
        try {
            if (object.has("name") && !object.isNull("name")) {
                serviceTypeFeature.name = object.getString("name");
            }
            if (object.has("desc_bottom") && !object.isNull("desc_bottom")) {
                serviceTypeFeature.desc_bottom = object.getString("desc_bottom");
            }
            if (object.has("price") && !object.isNull("price")) {
                serviceTypeFeature.price = object.getString("price");
            }
            if (object.has("vip_price") && !object.isNull("vip_price")) {
                serviceTypeFeature.vip_price = object.getString("vip_price");
            }
            if (object.has("service_id") && !object.isNull("service_id")) {
                serviceTypeFeature.service_id = object.getInt("service_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceTypeFeature;
    }
}
