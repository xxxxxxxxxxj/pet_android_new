package com.haotang.pet.encyclopedias.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/3 0003.
 */

public class CommentBean {
    public String dateStr;
    public String userName;
    public String content;
    public String avatar;

    public static CommentBean j2Entity(JSONObject object){
        CommentBean commentBean = new CommentBean();
        try {
            if (object.has("dateStr")&&!object.isNull("dateStr")){
                commentBean.dateStr = object.getString("dateStr");
            }
            if (object.has("userName")&&!object.isNull("userName")){
                commentBean.userName = object.getString("userName");
            }
            if (object.has("content")&&!object.isNull("content")){
                commentBean.content = object.getString("content");
            }
            if (object.has("avatar")&&!object.isNull("avatar")){
                commentBean.avatar = object.getString("avatar");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commentBean;
    }
}
