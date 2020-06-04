package com.haotang.pet.entity.mallEntity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MallHot {
    public int id;
    public String content;
    public int count;
    public int isDel;
    public String created;
    public int sort;

    public static MallHot json2Entity(JSONObject json) {
        MallHot mallHot = new MallHot();
        try {
            if (json.has("id") && !json.isNull("id")) {
                mallHot.id = json.getInt("id");
            }
            if (json.has("content") && !json.isNull("content")) {
                mallHot.content = json.getString("content");
            }
            if (json.has("count") && !json.isNull("count")) {
                mallHot.count = json.getInt("count");
            }
            if (json.has("isDel") && !json.isNull("isDel")) {
                mallHot.isDel = json.getInt("isDel");
            }
            if (json.has("created") && !json.isNull("created")) {
                mallHot.created = json.getString("created");
            }
            if (json.has("sort") && !json.isNull("sort")) {
                mallHot.isDel = json.getInt("sort");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallHot;
    }
/**
 * "id": 1,        //热门搜索id
 * "content": "111",       //搜索内容
 * "count": 0,         //搜索数量
 * "isDel": 0,     //是否删除
 * "created": "1970-01-01 00:00:00",
 * "sort": 0       //排序
 */
}
