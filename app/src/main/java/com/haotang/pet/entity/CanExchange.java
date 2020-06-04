package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/9 16:31
 */
public class CanExchange {
    public String icon;
    public String name;
    public String title;
    public String content;
    public String backup;
    public int point;
    public int state;

    public static CanExchange json2Entity(JSONObject json) {
        CanExchange canExchange = new CanExchange();
        try {
            if (json.has("icon") && !json.isNull("icon")) {
                canExchange.icon = json.getString("icon");
            }
            if (json.has("name") && !json.isNull("name")) {
                canExchange.name = json.getString("name");
            }
            if (json.has("title") && !json.isNull("title")) {
                canExchange.title = json.getString("title");
            }
            if (json.has("content") && !json.isNull("content")) {
                canExchange.content = json.getString("content");
            }
            if (json.has("backup") && !json.isNull("backup")) {
                canExchange.backup = json.getString("backup");
            }
            if (json.has("point") && !json.isNull("point")) {
                canExchange.point = json.getInt("point");
            }
            if (json.has("state") && !json.isNull("state")) {
                canExchange.state = json.getInt("state");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canExchange;
    }
}
