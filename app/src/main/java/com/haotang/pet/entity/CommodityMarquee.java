package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/1 10:31
 */
public class CommodityMarquee {
    private String updateTime;
    private String avatar;
    private int type;
    private int userId;
    private String content;

    public static CommodityMarquee json2Entity(JSONObject json) {
        CommodityMarquee commodityMarquee = new CommodityMarquee();
        try {
            if (json.has("updateTime") && !json.isNull("updateTime")) {
                commodityMarquee.setUpdateTime(json.getString("updateTime"));
            }
            if (json.has("avatar") && !json.isNull("avatar")) {
                commodityMarquee.setAvatar(json.getString("avatar"));
            }
            if (json.has("type") && !json.isNull("type")) {
                commodityMarquee.setType(json.getInt("type"));
            }
            if (json.has("userId") && !json.isNull("userId")) {
                commodityMarquee.setUserId(json.getInt("userId"));
            }
            if (json.has("content") && !json.isNull("content")) {
                commodityMarquee.setContent(json.getString("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commodityMarquee;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}