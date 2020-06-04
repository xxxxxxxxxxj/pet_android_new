package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/5 18:07
 */
public class Goods {
    private String backup;
    private String id;
    private String pic;
    private String point;

    public static Goods json2Entity(JSONObject jobj) {
        Goods goods = new Goods();
        try {
            if (jobj.has("backup") && !jobj.isNull("backup")) {
                goods.setBackup(jobj.getString("backup"));
            }
            if (jobj.has("id") && !jobj.isNull("id")) {
                goods.setId(jobj.getString("id"));
            }
            if (jobj.has("pic") && !jobj.isNull("pic")) {
                goods.setPic(jobj.getString("pic"));
            }
            if (jobj.has("point") && !jobj.isNull("point")) {
                goods.setPoint(jobj.getString("point"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
