package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 21:04
 */
public class SwitchServiceItems {
    private String description;
    private String price;
    private int serviceId;
    private String name;
    private String onlyTidDesc;
    private String vipPrice;
    private int label;
    private int serviceType;
    private boolean isSelect;

    public static SwitchServiceItems json2Entity(JSONObject jobj) {
        SwitchServiceItems switchServiceItems = new SwitchServiceItems();
        try {
            if (jobj.has("serviceType") && !jobj.isNull("serviceType")) {
                switchServiceItems.setServiceType(jobj.getInt("serviceType"));
            }
            if (jobj.has("label") && !jobj.isNull("label")) {
                switchServiceItems.setLabel(jobj.getInt("label"));
            }
            if (jobj.has("price") && !jobj.isNull("price")) {
                switchServiceItems.setPrice(jobj.getString("price"));
            }
            if (jobj.has("name") && !jobj.isNull("name")) {
                switchServiceItems.setName(jobj.getString("name"));
            }
            if (jobj.has("serviceId") && !jobj.isNull("serviceId")) {
                switchServiceItems.setServiceId(jobj.getInt("serviceId"));
            }
            if (jobj.has("description") && !jobj.isNull("description")) {
                switchServiceItems.setDescription(jobj.getString("description"));
            }
            if (jobj.has("onlyTidDesc") && !jobj.isNull("onlyTidDesc")) {
                switchServiceItems.setOnlyTidDesc(jobj.getString("onlyTidDesc"));
            }
            if (jobj.has("vipPrice") && !jobj.isNull("vipPrice")) {
                switchServiceItems.setVipPrice(jobj.getString("vipPrice"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return switchServiceItems;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnlyTidDesc() {
        return onlyTidDesc;
    }

    public void setOnlyTidDesc(String onlyTidDesc) {
        this.onlyTidDesc = onlyTidDesc;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

}
