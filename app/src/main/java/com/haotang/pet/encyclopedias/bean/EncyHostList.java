package com.haotang.pet.encyclopedias.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/7 0007.
 */

public class EncyHostList {
    public String created;
    public int id;
    public String searchContent;
    public int searchNum;
    public int sort;
    public int state;

    public static EncyHostList j2Entity(JSONObject object) {
        EncyHostList encyHostList = new EncyHostList();
        try {
            if (object.has("created") && !object.isNull("created")) {
                encyHostList.created = object.getString("created");
            }
            if (object.has("id") && !object.isNull("id")) {
                encyHostList.id = object.getInt("id");
            }
            if (object.has("searchContent") && !object.isNull("searchContent")) {
                encyHostList.searchContent = object.getString("searchContent");
            }
            if (object.has("searchNum") && !object.isNull("searchNum")) {
                encyHostList.searchNum = object.getInt("searchNum");
            }
            if (object.has("sort") && !object.isNull("sort")) {
                encyHostList.sort = object.getInt("sort");
            }
            if (object.has("state") && !object.isNull("state")) {
                encyHostList.state = object.getInt("state");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return encyHostList;
    }
}
