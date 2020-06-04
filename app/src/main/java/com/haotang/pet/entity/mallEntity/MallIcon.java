package com.haotang.pet.entity.mallEntity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MallIcon {
    public int id;
    public String name;
    public String txt;
    public String pic;
    public String tag;
    public String versionNumber;
    public int sort;
    public int status;
    public int isDel;
    public String created;
    public int point;
    public String  backup;
    public String picDomain;

    public static MallIcon json2Entity(JSONObject json) {
        MallIcon mallIcon = new MallIcon();
        try {
            if (json.has("id") && !json.isNull("id")) {
                mallIcon.id = json.getInt("id");
            }
            if (json.has("name") && !json.isNull("name")) {
                mallIcon.name = json.getString("name");
            }
            if (json.has("txt") && !json.isNull("txt")) {
                mallIcon.txt = json.getString("txt");
            }
            if (json.has("pic") && !json.isNull("pic")) {
                mallIcon.pic = json.getString("pic");
            }
            if (json.has("tag") && !json.isNull("tag")) {
                mallIcon.tag = json.getString("tag");
            }
            if (json.has("versionNumber") && !json.isNull("versionNumber")) {
                mallIcon.versionNumber = json.getString("versionNumber");
            }
            if (json.has("sort") && !json.isNull("sort")) {
                mallIcon.sort = json.getInt("sort");
            }
            if (json.has("status") && !json.isNull("status")) {
                mallIcon.status = json.getInt("status");
            }
            if (json.has("isDel") && !json.isNull("isDel")) {
                mallIcon.isDel = json.getInt("isDel");
            }
            if (json.has("created") && !json.isNull("created")) {
                mallIcon.created = json.getString("created");
            }
            if (json.has("point") && !json.isNull("point")) {
                mallIcon.point = json.getInt("point");
            }
            if (json.has("backup") && !json.isNull("backup")) {
                mallIcon.backup = json.getString("backup");
            }
            if (json.has("picDomain") && !json.isNull("picDomain")) {
                mallIcon.picDomain = json.getString("picDomain");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallIcon;
    }
}
