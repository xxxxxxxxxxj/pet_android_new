package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/4 11:26
 */
public class MallCommodityGroup {
    private String specName;
    private int flag;
    private int id;

    public static MallCommodityGroup json2Entity(JSONObject json) {
        MallCommodityGroup mallCommodityGroup = new MallCommodityGroup();
        try {
            if (json.has("specName") && !json.isNull("specName")) {
                mallCommodityGroup.setSpecName(json.getString("specName"));
            }
            if (json.has("flag") && !json.isNull("flag")) {
                mallCommodityGroup.setFlag(json.getInt("flag"));
            }
            if (json.has("id") && !json.isNull("id")) {
                mallCommodityGroup.setId(json.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallCommodityGroup;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
