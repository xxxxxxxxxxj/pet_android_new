package com.haotang.pet.encyclopedias.bean;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/2 13:04
 */
public class Encyclopedias {
    public String source;
    public String sourceIcon;
    public String title;
    public String listCover;
    public String releaseTime;
    public int id;
    public int realReadNum;
    public int listCoverWeight;
    public int listCoverHeight;
    public boolean isVideo;

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Encyclopedias() {
    }

    public Encyclopedias(String source, String sourceIcon, String title, String listCover, int id, int realReadNum, int listCoverWeight, int listCoverHeight, boolean isVideo,String releaseTime) {
        this.source = source;
        this.sourceIcon = sourceIcon;
        this.title = title;
        this.listCover = listCover;
        this.id = id;
        this.realReadNum = realReadNum;
        this.listCoverWeight = listCoverWeight;
        this.listCoverHeight = listCoverHeight;
        this.isVideo = isVideo;
        this.releaseTime = releaseTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getListCover() {
        return listCover;
    }

    public void setListCover(String listCover) {
        this.listCover = listCover;
    }

    public int getListCoverWeight() {
        return listCoverWeight;
    }

    public void setListCoverWeight(int listCoverWeight) {
        this.listCoverWeight = listCoverWeight;
    }

    public int getListCoverHeight() {
        return listCoverHeight;
    }

    public void setListCoverHeight(int listCoverHeight) {
        this.listCoverHeight = listCoverHeight;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public int getRealReadNum() {
        return realReadNum;
    }

    public void setRealReadNum(int realReadNum) {
        this.realReadNum = realReadNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceIcon() {
        return sourceIcon;
    }

    public void setSourceIcon(String sourceIcon) {
        this.sourceIcon = sourceIcon;
    }

    public static Encyclopedias json2Entity(JSONObject jobj) {
        Encyclopedias encyclopedias = new Encyclopedias();
        try {
            if (jobj.has("id") && !jobj.isNull("id")) {
                encyclopedias.setId(jobj.getInt("id"));
            }
            if (jobj.has("source") && !jobj.isNull("source")) {
                encyclopedias.setSource(jobj.getString("source"));
            }
            if (jobj.has("sourceIcon") && !jobj.isNull("sourceIcon")) {
                encyclopedias.setSourceIcon(jobj.getString("sourceIcon"));
            }
            if (jobj.has("title") && !jobj.isNull("title")) {
                encyclopedias.setTitle(jobj.getString("title"));
            }
            if (jobj.has("extendParam")&&!jobj.isNull("extendParam")){
                JSONObject extendParam = jobj.getJSONObject("extendParam");
                if (extendParam.has("releaseTime")&&!extendParam.isNull("releaseTime")){
                    encyclopedias.setReleaseTime(extendParam.getString("releaseTime"));
                }
            }

            if (jobj.has("listCover") && !jobj.isNull("listCover")) {
                encyclopedias.setListCover(jobj.getString("listCover"));
            }
            if (jobj.has("realReadNum") && !jobj.isNull("realReadNum")) {
                encyclopedias.setRealReadNum(jobj.getInt("realReadNum"));
            }
            if (jobj.has("listCoverWeight") && !jobj.isNull("listCoverWeight")) {
                encyclopedias.setListCoverWeight(jobj.getInt("listCoverWeight"));
            }
            if (jobj.has("listCoverHeight") && !jobj.isNull("listCoverHeight")) {
                encyclopedias.setListCoverHeight(jobj.getInt("listCoverHeight"));
            }
            if (jobj.has("contentJson") && !jobj.isNull("contentJson")) {
                JSONArray contentJson = jobj.getJSONArray("contentJson");
                if (contentJson.length() > 0) {
                    for (int i = 0; i < contentJson.length(); i++) {
                        JSONObject jsonObject = contentJson.getJSONObject(i);
                        String type = null;
                        if (jsonObject.has("type") && !jsonObject.isNull("type")) {
                            type = jsonObject.getString("type");
                        }
                        if (type.equals("3")) {
                            encyclopedias.setVideo(true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encyclopedias;
    }
}
