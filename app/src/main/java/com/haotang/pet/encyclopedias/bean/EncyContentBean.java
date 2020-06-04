package com.haotang.pet.encyclopedias.bean;

import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/3 0003.
 */

public class EncyContentBean {
    public int type;
    public String content;
    public int point;
    public String backup;
    public int width=1;
    public int height=1;
    public ImageView imageView;
    public static EncyContentBean j2Entity(JSONObject object){
        EncyContentBean encyContentBean = new EncyContentBean();

        try {
            if (object.has("type")&&!object.isNull("type")){
                encyContentBean.type = object.getInt("type");
            }
            if (object.has("point")&&!object.isNull("point")){
                encyContentBean.point = object.getInt("point");
            }
            if (object.has("content")&&!object.isNull("content")){
                encyContentBean.content = object.getString("content");
            }
            if (object.has("backup")&&!object.isNull("backup")){
                encyContentBean.backup = object.getString("backup");
            }
            if (object.has("weight")&&!object.isNull("weight")){
                encyContentBean.width = object.getInt("weight");
            }
            if (object.has("height")&&!object.isNull("height")){
                encyContentBean.height = object.getInt("height");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encyContentBean;
    }
}
