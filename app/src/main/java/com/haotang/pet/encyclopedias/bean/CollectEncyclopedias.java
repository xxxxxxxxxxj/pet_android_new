package com.haotang.pet.encyclopedias.bean;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/2 14:22
 */
public class CollectEncyclopedias {
    public String source;
    public String sourceIcon;
    public String releaseTime;
    public String title;
    public String collectionCover;
    public int id;
    public boolean select;
    public boolean isShowDel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCollectionCover() {
        return collectionCover;
    }

    public void setCollectionCover(String collectionCover) {
        this.collectionCover = collectionCover;
    }

    public boolean isShowDel() {
        return isShowDel;
    }

    public void setShowDel(boolean showDel) {
        isShowDel = showDel;
    }

    public static CollectEncyclopedias json2Entity(JSONObject jobj) {
        CollectEncyclopedias collectEncyclopedias = new CollectEncyclopedias();
        try {
            if (jobj.has("id") && !jobj.isNull("id")) {
                collectEncyclopedias.setId(jobj.getInt("id"));
            }
            if (jobj.has("source") && !jobj.isNull("source")) {
                collectEncyclopedias.setSource(jobj.getString("source"));
            }
            if (jobj.has("sourceIcon") && !jobj.isNull("sourceIcon")) {
                collectEncyclopedias.setSourceIcon(jobj.getString("sourceIcon"));
            }
            if (jobj.has("releaseTime") && !jobj.isNull("releaseTime")) {
                collectEncyclopedias.setReleaseTime(jobj.getString("releaseTime"));
            }
            if (jobj.has("title") && !jobj.isNull("title")) {
                collectEncyclopedias.setTitle(jobj.getString("title"));
            }
            if (jobj.has("collectionCover") && !jobj.isNull("collectionCover")) {
                collectEncyclopedias.setCollectionCover(jobj.getString("collectionCover"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectEncyclopedias;
    }
}
