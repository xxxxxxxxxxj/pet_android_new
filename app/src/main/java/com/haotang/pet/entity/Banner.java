package com.haotang.pet.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class Banner implements Serializable {
    public String pic;
    public int type;
    public int cwjtest;
    public String url;
    public String imgUrl;
    public String backup;
    public int point;
    public String picDomain;
    public String bigPic;
    public String bigOrSmall;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Banner banner = (Banner) o;

        if (type != banner.type) return false;
        if (cwjtest != banner.cwjtest) return false;
        if (point != banner.point) return false;
        if (pic != null ? !pic.equals(banner.pic) : banner.pic != null) return false;
        if (url != null ? !url.equals(banner.url) : banner.url != null) return false;
        if (backup != null ? !backup.equals(banner.backup) : banner.backup != null) return false;
        if (picDomain != null ? !picDomain.equals(banner.picDomain) : banner.picDomain != null)
            return false;
        if (bigPic != null ? !bigPic.equals(banner.bigPic) : banner.bigPic != null) return false;
        return bigOrSmall != null ? bigOrSmall.equals(banner.bigOrSmall) : banner.bigOrSmall == null;

    }

    @Override
    public int hashCode() {
        int result = pic != null ? pic.hashCode() : 0;
        result = 31 * result + type;
        result = 31 * result + cwjtest;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (backup != null ? backup.hashCode() : 0);
        result = 31 * result + point;
        result = 31 * result + (picDomain != null ? picDomain.hashCode() : 0);
        result = 31 * result + (bigPic != null ? bigPic.hashCode() : 0);
        result = 31 * result + (bigOrSmall != null ? bigOrSmall.hashCode() : 0);
        return result;
    }

    public Banner(String pic, int type, int cwjtest, String url, String backup, int point) {
        this.pic = pic;
        this.type = type;
        this.cwjtest = cwjtest;
        this.url = url;
        this.backup = backup;
        this.point = point;
    }

    public Banner(String pic) {
        this.pic = pic;
    }

    public Banner() {
    }

    public static Banner json2Entity(JSONObject jobj) {
        Banner b = new Banner();
        try {
            if (jobj.has("url") && !jobj.isNull("url")) {
                b.url = jobj.getString("url");
            }
            if (jobj.has("pic") && !jobj.isNull("pic")) {
                b.pic = jobj.getString("pic");
            }
            if (jobj.has("img") && !jobj.isNull("img")) {
                b.pic = jobj.getString("img");
            }
            if (jobj.has("bigPic") && !jobj.isNull("bigPic")) {
                b.bigPic = jobj.getString("bigPic");
            }
            if (jobj.has("imgUrl") && !jobj.isNull("imgUrl")) {
                b.imgUrl = jobj.getString("imgUrl");
            }
            if (jobj.has("extendParam") && !jobj.isNull("extendParam")) {
                JSONObject jextendParam = jobj.getJSONObject("extendParam");
                if (jextendParam.has("bigOrSmall") && !jextendParam.isNull("bigOrSmall")) {
                    b.bigOrSmall = jextendParam.getString("bigOrSmall");
                }
            }
            if (jobj.has("picDomain") && !jobj.isNull("picDomain")) {
                b.picDomain = jobj.getString("picDomain");
            }
            if (jobj.has("type") && !jobj.isNull("type")) {
                b.type = jobj.getInt("type");
            }
            if (jobj.has("cwjtest") && !jobj.isNull("cwjtest")) {
                b.cwjtest = jobj.getInt("cwjtest");
            }
            if (jobj.has("backup") && !jobj.isNull("backup")) {
                b.backup = jobj.getString("backup");
            }
            if (jobj.has("point") && !jobj.isNull("point")) {
                b.point = jobj.getInt("point");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
