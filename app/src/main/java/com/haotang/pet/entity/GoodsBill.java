package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/27 10:55
 */
public class GoodsBill {
    private String name;
    private String price;
    private String amount;
    private String icon;

    public GoodsBill() {
    }

    public GoodsBill(String name, String price, String amount, String icon) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.icon = icon;
    }

    public static GoodsBill json2Entity(JSONObject jobj) {
        GoodsBill goodsBill = new GoodsBill();
        try {
            if (jobj.has("name") && !jobj.isNull("name")) {
                goodsBill.setName(jobj.getString("name"));
            }
            if (jobj.has("price") && !jobj.isNull("price")) {
                goodsBill.setPrice(jobj.getString("price"));
            }
            if (jobj.has("amount") && !jobj.isNull("amount")) {
                goodsBill.setAmount(jobj.getString("amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsBill;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
