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
public class CanEarningGuide {
    public String icon;
    public String name;
    public String title;
    public String content;
    public String backup;
    public int point;
    public int state;

    public static CanEarningGuide json2Entity(JSONObject json) {
        CanEarningGuide canEarningGuide = new CanEarningGuide();
        try {
            if (json.has("icon") && !json.isNull("icon")) {
                canEarningGuide.icon = json.getString("icon");
            }
            if (json.has("name") && !json.isNull("name")) {
                canEarningGuide.name = json.getString("name");
            }
            if (json.has("title") && !json.isNull("title")) {
                canEarningGuide.title = json.getString("title");
            }
            if (json.has("content") && !json.isNull("content")) {
                canEarningGuide.content = json.getString("content");
            }
            if (json.has("backup") && !json.isNull("backup")) {
                canEarningGuide.backup = json.getString("backup");
            }
            if (json.has("point") && !json.isNull("point")) {
                canEarningGuide.point = json.getInt("point");
            }
            if (json.has("state") && !json.isNull("state")) {
                canEarningGuide.state = json.getInt("state");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canEarningGuide;
    }
}
