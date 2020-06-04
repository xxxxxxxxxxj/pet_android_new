package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceType implements Serializable {
    public String name;//"到店服务", //服务名称
    public int isHot;// 1, //是否显示热门tag
    public int serviceLoc;// 上门 / 到店
    public String icon;//"/static/image/shop_service_pic.png", //服务icon
    public String desc_bottom;//"宠物家生活馆欢迎您", //底部显示文案
    public String price; //"¥75起", //右侧显示的最低价格文案 非vip价格
    public String vip_price;//对应vip价格
    public String btn_txt; //服务方式
    public String disabled_tip;//"抱歉，大型犬暂不支持上门服务" //服务不可用时的提示文案,可用时该key不存在
    public boolean isOpen;
    public List<ServiceTypeFeature> listsServiceOpen = new ArrayList<>();
    public String grain_gold_price;

    public static ServiceType json2Entity(JSONObject json) {
        ServiceType serviceType = new ServiceType();
        try {
            if (json.has("name") && !json.isNull("name")) {
                serviceType.name = json.getString("name");
            }
            if (json.has("isHot") && !json.isNull("isHot")) {
                serviceType.isHot = json.getInt("isHot");
            }
            if (json.has("serviceLoc") && !json.isNull("serviceLoc")) {
                serviceType.serviceLoc = json.getInt("serviceLoc");
            }
            if (json.has("icon") && !json.isNull("icon")) {
                serviceType.icon = json.getString("icon");
            }
            if (json.has("desc_bottom") && !json.isNull("desc_bottom")) {
                serviceType.desc_bottom = json.getString("desc_bottom");
            }
            if (json.has("price") && !json.isNull("price")) {
                serviceType.price = json.getString("price");
            }
            if (json.has("vip_price") && !json.isNull("vip_price")) {
                serviceType.vip_price = json.getString("vip_price");
            }
            if (json.has("btn_txt") && !json.isNull("btn_txt")) {
                serviceType.btn_txt = json.getString("btn_txt");
            }
            if (json.has("grainGoldPrice") && !json.isNull("grainGoldPrice")) {
                serviceType.grain_gold_price = json.getString("grainGoldPrice");
            }
            if (json.has("disabled_tip") && !json.isNull("disabled_tip")) {
                serviceType.disabled_tip = json.getString("disabled_tip");
            }
            if (json.has("items") && !json.isNull("items")) {
                JSONArray array = json.getJSONArray("items");
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        serviceType.listsServiceOpen.add(ServiceTypeFeature.j2Entity(array.getJSONObject(i)));
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return serviceType;

    }
}
