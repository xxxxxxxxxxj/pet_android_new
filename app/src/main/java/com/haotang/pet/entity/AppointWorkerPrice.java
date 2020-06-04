package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/21 11:39
 */
public class AppointWorkerPrice implements Serializable {
    private int myPetId;
    private double price;
    private double vip_price;
    private String name;
    private String description;
    private String tips;
    private int serviceId;
    private int isFeature;
    private List<ApointMentItem> itemList = new ArrayList<ApointMentItem>();

    public static AppointWorkerPrice json2Entity(JSONObject jobj) {
        AppointWorkerPrice appointWorkerPrice = new AppointWorkerPrice();
        try {
            if (jobj.has("myPetId") && !jobj.isNull("myPetId")) {
                appointWorkerPrice.setMyPetId(jobj.getInt("myPetId"));
            }
            if (jobj.has("price") && !jobj.isNull("price")) {
                appointWorkerPrice.setPrice(jobj.getDouble("price"));
            }
            if (jobj.has("vip_price") && !jobj.isNull("vip_price")) {
                appointWorkerPrice.setVip_price(jobj.getDouble("vip_price"));
            }
            if (jobj.has("name") && !jobj.isNull("name")) {
                appointWorkerPrice.setName(jobj.getString("name"));
            }
            if (jobj.has("description") && !jobj.isNull("description")) {
                appointWorkerPrice.setDescription(jobj.getString("description"));
            }
            if (jobj.has("serviceId") && !jobj.isNull("serviceId")) {
                appointWorkerPrice.setServiceId(jobj.getInt("serviceId"));
            }
            if (jobj.has("isFeature") && !jobj.isNull("isFeature")) {
                appointWorkerPrice.setIsFeature(jobj.getInt("isFeature"));
            }
            if (jobj.has("itemIds") && !jobj.isNull("itemIds")) {
                JSONObject jitemIds = jobj.getJSONObject("itemIds");
                if (jitemIds != null) {
                    if (jitemIds.has("items") && !jitemIds.isNull("items")) {
                        JSONArray jarrItems = jitemIds.getJSONArray("items");
                        if (jarrItems != null && jarrItems.length() > 0) {
                            appointWorkerPrice.itemList.clear();
                            for (int i = 0; i < jarrItems.length(); i++) {
                                appointWorkerPrice.itemList.add(ApointMentItem
                                        .json2Entity(jarrItems
                                                .getJSONObject(i)));
                            }
                        }
                    }
                    if (jitemIds.has("tips") && !jitemIds.isNull("tips")) {
                        appointWorkerPrice.setTips(jitemIds.getString("tips"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointWorkerPrice;
    }

    public int getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(int isFeature) {
        this.isFeature = isFeature;
    }

    public List<ApointMentItem> getItemList() {
        return itemList;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getMyPetId() {
        return myPetId;
    }

    public void setMyPetId(int myPetId) {
        this.myPetId = myPetId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVip_price() {
        return vip_price;
    }

    public void setVip_price(double vip_price) {
        this.vip_price = vip_price;
    }
}
