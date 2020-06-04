package com.haotang.pet.encyclopedias.bean;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/2 17:44
 */
public class EncyclopediasComment {
    public String dateStr;
    public String userName;
    public String content;
    public String avatar;

    public EncyclopediasComment() {
    }

    public EncyclopediasComment(String dateStr, String userName, String content, String avatar) {
        this.dateStr = dateStr;
        this.userName = userName;
        this.content = content;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static EncyclopediasComment json2Entity(JSONObject jobj) {
        EncyclopediasComment encyclopediasComment = new EncyclopediasComment();
        try {
            if (jobj.has("dateStr") && !jobj.isNull("dateStr")) {
                encyclopediasComment.setDateStr(jobj.getString("dateStr"));
            }
            if (jobj.has("userName") && !jobj.isNull("userName")) {
                encyclopediasComment.setUserName(jobj.getString("userName"));
            }
            if (jobj.has("content") && !jobj.isNull("content")) {
                encyclopediasComment.setContent(jobj.getString("content"));
            }
            if (jobj.has("avatar") && !jobj.isNull("avatar")) {
                encyclopediasComment.setAvatar(jobj.getString("avatar"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encyclopediasComment;
    }
}
