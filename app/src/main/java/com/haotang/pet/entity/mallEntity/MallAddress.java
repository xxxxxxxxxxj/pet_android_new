package com.haotang.pet.entity.mallEntity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/2.
 */

public class MallAddress implements Serializable{
    public String consigner;
    public int isDefault;
    public int areaId;
    public String address;
    public String areaName;
    public String mobile;
    public String postCode;
    public int id;

    @Override
    public String toString() {
        return "MallAddress{" +
                "consigner='" + consigner + '\'' +
                ", isDefault=" + isDefault +
                ", areaId=" + areaId +
                ", address='" + address + '\'' +
                ", areaName='" + areaName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", postCode='" + postCode + '\'' +
                ", id=" + id +
                '}';
    }

    public static MallAddress json2Entity(JSONObject json) {
        MallAddress mallAddress = new MallAddress();
        try {
            if (json.has("consigner") && !json.isNull("consigner")) {
                mallAddress.consigner = json.getString("consigner");
            }
            if (json.has("isDefault") && !json.isNull("isDefault")) {
                mallAddress.isDefault = json.getInt("isDefault");
            }
            if (json.has("areaId") && !json.isNull("areaId")) {
                mallAddress.areaId = json.getInt("areaId");
            }
            if (json.has("areaName") && !json.isNull("areaName")) {
                mallAddress.areaName = json.getString("areaName");
            }
            if (json.has("mobile") && !json.isNull("mobile")) {
                mallAddress.mobile = json.getString("mobile");
            }
            if (json.has("postCode") && !json.isNull("postCode")) {
                mallAddress.postCode = json.getString("postCode");
            }
            if (json.has("address") && !json.isNull("address")) {
                mallAddress.address = json.getString("address");
            }
            if (json.has("id") && !json.isNull("id")) {
                mallAddress.id = json.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallAddress;
    }
}
