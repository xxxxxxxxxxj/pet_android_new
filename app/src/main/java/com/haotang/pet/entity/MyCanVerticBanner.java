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
public class MyCanVerticBanner {
    public String avatar;
    public String vicon;
    public String userName;
    public String activity;
    public String operatorTime;

    public static MyCanVerticBanner json2Entity(JSONObject json) {
        MyCanVerticBanner myCanVerticBanner = new MyCanVerticBanner();
        try {
            if (json.has("avatar") && !json.isNull("avatar")) {
                myCanVerticBanner.avatar = json.getString("avatar");
            }
            if (json.has("vicon") && !json.isNull("vicon")) {
                myCanVerticBanner.vicon = json.getString("vicon");
            }
            if (json.has("userName") && !json.isNull("userName")) {
                myCanVerticBanner.userName = json.getString("userName");
            }
            if (json.has("activity") && !json.isNull("activity")) {
                myCanVerticBanner.activity = json.getString("activity");
            }
            if (json.has("operatorTime") && !json.isNull("operatorTime")) {
                myCanVerticBanner.operatorTime = json.getString("operatorTime");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myCanVerticBanner;
    }
}
