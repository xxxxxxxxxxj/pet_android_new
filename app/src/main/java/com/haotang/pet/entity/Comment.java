package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Comment {
    public int id;
    public String image = "";
    public String name;
    public String content;
    public String contents;
    public String commentTags;
    public String time;
    public String replyUser = "";
    public String replyContent = "";
    public String replyTime = "";
    public String[] images;//作品图片
    public String replyMan;
    public String memberIcon = null;//会员头像
    public String userName;
    public String avatar;
    public int grade;
    public String commentWorkerContent;//回复内容
    public String appointment;
    public String serviceName;
    public String itemName;
    private boolean isFz;
    private boolean isSetImgDecor;
    private boolean isSetItemDecor;
    public String extraItems;
    public boolean isOpen;
    public String avatarBeauty;//美容师头像
    public String nickname;//美容师姓名
    private boolean isItemopen;

    public String getCommentWorkerContent() {
        return commentWorkerContent;
    }

    public String getAvatarBeauty() {
        return avatarBeauty;
    }

    public String getNickname() {
        return nickname;
    }

    public void setAvatarBeauty(String avatarBeauty) {
        this.avatarBeauty = avatarBeauty;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> itemList = new ArrayList<String>();

    public boolean isSetImgDecor() {
        return isSetImgDecor;
    }

    public void setSetImgDecor(boolean setImgDecor) {
        isSetImgDecor = setImgDecor;
    }

    public boolean isSetItemDecor() {
        return isSetItemDecor;
    }

    public void setSetItemDecor(boolean setItemDecor) {
        isSetItemDecor = setItemDecor;
    }

    public boolean isFz() {
        return isFz;
    }

    public void setFz(boolean fz) {
        isFz = fz;
    }

    public static Comment json2Entity(JSONObject json) {
        Comment comment = new Comment();
        try {
            if (json.has("grade") && !json.isNull("grade")) {
                comment.grade = json.getInt("grade");
            }
            if (json.has("id") && !json.isNull("id")) {
                comment.id = json.getInt("id");
            }
            if (json.has("content") && !json.isNull("content")) {
                comment.content = json.getString("content");
            }
            if (json.has("commentWorkerContent")&&!json.isNull("commentWorkerContent")){
                comment.commentWorkerContent = json.getString("commentWorkerContent");
            }
            if (json.has("contents") && !json.isNull("contents")) {
                comment.contents = json.getString("contents");
            }
            if (json.has("commentTags") && !json.isNull("commentTags")) {
                comment.commentTags = json.getString("commentTags");
            }
            if (json.has("avatar") && !json.isNull("avatar") && !"".equals(json.getString("avatar").trim())) {
                comment.image = json.getString("avatar");
            }
            if (json.has("realName") && !json.isNull("realName")) {
                comment.name = json.getString("realName");
            }

            if (json.has("created") && !json.isNull("created")) {
                String time = json.getString("created");
                comment.time = formatTime(time);
            }
            if (json.has("replyUser") && !json.isNull("replyUser")) {
                comment.replyUser = json.getString("replyUser");
            }
            if (json.has("replyContent") && !json.isNull("replyContent")) {
                comment.replyContent = json.getString("replyContent");
            }
            if (json.has("replyTime") && !json.isNull("replyTime")) {
                comment.replyTime = json.getString("replyTime");
            }
            if (json.has("imgList") && !json.isNull("imgList")) {
                JSONArray arr = json.getJSONArray("imgList");
                if (arr.length() > 0) {
                    comment.images = new String[arr.length()];
                    for (int i = 0; i < arr.length(); i++) {
                        comment.images[i] = arr.getString(i);
                    }
                }
            }
            if (json.has("user") && !json.isNull("user")) {
                JSONObject objectUser = json.getJSONObject("user");
                if (objectUser.has("userMemberLevel") && !objectUser.isNull("userMemberLevel")) {
                    JSONObject objectUserLevel = objectUser.getJSONObject("userMemberLevel");
                    if (objectUserLevel.has("memberIcon") && !objectUserLevel.isNull("memberIcon")) {
                        comment.memberIcon = objectUserLevel.getString("memberIcon");
                    }
                }
                if (objectUser.has("userName")&&!objectUser.isNull("userName")){
                    comment.userName = objectUser.getString("userName");
                }
                if (objectUser.has("avatar")&&!objectUser.isNull("avatar")){
                    comment.avatar = objectUser.getString("avatar");
                }
            }
            if (json.has("extraItems")&&!json.isNull("extraItems")){
                JSONArray array = json.getJSONArray("extraItems");
                if (array.length()>0){
                    comment.itemList.clear();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        if (object.has("name")&&!object.isNull("name")){
                            stringBuilder.append(object.getString("name"));
                            comment.itemList.add(object.getString("name"));
                        }
                    }
                    try {
                        comment.extraItems = stringBuilder.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (json.has("serviceName") && !json.isNull("serviceName")) {
                comment.serviceName = json.getString("serviceName");
            }
            if (json.has("appointment") && !json.isNull("appointment")) {
                comment.appointment = json.getString("appointment");
            }
            if (json.has("order") && !json.isNull("order")) {
                JSONObject objectorder = json.getJSONObject("order");
                if (objectorder.has("appointment") && !objectorder.isNull("appointment")) {
                    comment.appointment = objectorder.getString("appointment");
                }
                if (objectorder.has("serviceName") && !objectorder.isNull("serviceName")) {
                    comment.serviceName = objectorder.getString("serviceName");
                }
                if (objectorder.has("itemName") && !objectorder.isNull("itemName")) {
                    comment.itemName = objectorder.getString("itemName");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comment;
    }

    private static String formatTime(String time) {
        time = time.replace("年", "-");
        time = time.replace("月", "-");
        time = time.replace("日", " ");
        time = time.replace("时", ":");
        time = time.replace("分", "");
        return time;
    }
}
