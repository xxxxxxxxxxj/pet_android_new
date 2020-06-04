package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 20:54
 */
public class SwitchService {
    private String price;
    private String vipPrice;
    private String name;
    private int serviceType;
    private int serviceId;
    private String pic;
    private int label;
    private int typeId;
    private String picMini;
    private java.util.List<SwitchServiceItems> items;
    private boolean isSelect;

    public static SwitchService json2Entity(JSONObject jobj) {
        SwitchService switchService = new SwitchService();
        try {
            if (jobj.has("typeId") && !jobj.isNull("typeId")) {
                switchService.setTypeId(jobj.getInt("typeId"));
            }
            if (jobj.has("serviceType") && !jobj.isNull("serviceType")) {
                switchService.setServiceType(jobj.getInt("serviceType"));
            }
            if (jobj.has("label") && !jobj.isNull("label")) {
                switchService.setLabel(jobj.getInt("label"));
            }
            if (jobj.has("price") && !jobj.isNull("price")) {
                switchService.setPrice(jobj.getString("price"));
            }
            if (jobj.has("vipPrice") && !jobj.isNull("vipPrice")) {
                switchService.setVipPrice(jobj.getString("vipPrice"));
            }
            if (jobj.has("name") && !jobj.isNull("name")) {
                switchService.setName(jobj.getString("name"));
            }
            if (jobj.has("serviceId") && !jobj.isNull("serviceId")) {
                switchService.setServiceId(jobj.getInt("serviceId"));
            }
            if (jobj.has("pic") && !jobj.isNull("pic")) {
                switchService.setPic(jobj.getString("pic"));
            }
            if (jobj.has("picMini")&&!jobj.isNull("picMini")){
                switchService.setPicMini(jobj.getString("picMini"));
            }
            if (jobj.has("items") && !jobj.isNull("items")) {
                JSONArray jarritems = jobj.getJSONArray("items");
                if (jarritems.length() > 0) {
                    List<SwitchServiceItems> list = new ArrayList<SwitchServiceItems>();
                    list.clear();
                    for (int i = 0; i < jarritems.length(); i++) {
                        list.add(SwitchServiceItems.json2Entity(jarritems
                                .getJSONObject(i)));
                    }
                    switchService.setItems(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return switchService;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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

    public String getPrice() {
        return price;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<SwitchServiceItems> getItems() {
        return items;
    }

    public void setItems(List<SwitchServiceItems> items) {
        this.items = items;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPicMini() {
        return picMini;
    }

    public void setPicMini(String picMini) {
        this.picMini = picMini;
    }
}
