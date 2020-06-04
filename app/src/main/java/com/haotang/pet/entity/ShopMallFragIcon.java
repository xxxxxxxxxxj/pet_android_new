package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/30 11:16
 */
public class ShopMallFragIcon {
    private String name;
    private String picDomain;
    private int point;
    private String backup;
    private String tag;

    public static ShopMallFragIcon json2Entity(JSONObject jobj){
        ShopMallFragIcon shopMallFragIcon = new ShopMallFragIcon();
        try {
            if(jobj.has("name")&&!jobj.isNull("name")){
                shopMallFragIcon.setName(jobj.getString("name"));
            }
            if(jobj.has("picDomain")&&!jobj.isNull("picDomain")){
                shopMallFragIcon.setPicDomain(jobj.getString("picDomain"));
            }
            if(jobj.has("point")&&!jobj.isNull("point")){
                shopMallFragIcon.setPoint(jobj.getInt("point"));
            }
            if(jobj.has("backup")&&!jobj.isNull("backup")){
                shopMallFragIcon.setBackup(jobj.getString("backup"));
            }
            if(jobj.has("tag")&&!jobj.isNull("tag")){
                shopMallFragIcon.setTag(jobj.getString("tag"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopMallFragIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicDomain() {
        return picDomain;
    }

    public void setPicDomain(String picDomain) {
        this.picDomain = picDomain;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getBackup() {
        return backup;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }
}
