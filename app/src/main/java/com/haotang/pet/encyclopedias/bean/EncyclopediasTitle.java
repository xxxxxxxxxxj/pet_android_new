package com.haotang.pet.encyclopedias.bean;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/7 09:53
 */
public class EncyclopediasTitle {
    public int id;
    public String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EncyclopediasTitle() {
    }

    public EncyclopediasTitle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EncyclopediasTitle json2Entity(JSONObject jobj) {
        EncyclopediasTitle encyclopediasTitle = new EncyclopediasTitle();
        try {
            if (jobj.has("id") && !jobj.isNull("id")) {
                encyclopediasTitle.setId(jobj.getInt("id"));
            }
            if (jobj.has("name") && !jobj.isNull("name")) {
                encyclopediasTitle.setName(jobj.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encyclopediasTitle;
    }
}
