package com.haotang.pet.entity.mallEntity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MallHistory {

    public int id;
    public int userId;
    public String content;
    public int isDel;
    public String created;

    public static MallHistory json2Entity(JSONObject json) {
        MallHistory mallHistory = new MallHistory();
        try {
            if (json.has("id") && !json.isNull("id")) {
                mallHistory.id = json.getInt("id");
            }
            if (json.has("userId") && !json.isNull("userId")) {
                mallHistory.userId = json.getInt("userId");
            }
            if (json.has("content") && !json.isNull("content")) {
                mallHistory.content = json.getString("content");
            }
            if (json.has("isDel") && !json.isNull("isDel")) {
                mallHistory.isDel = json.getInt("isDel");
            }
            if (json.has("created") && !json.isNull("created")) {
                mallHistory.created = json.getString("created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallHistory;
    }
}
