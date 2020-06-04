package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/10/30 15:48
 */
public class RechargeBanner {
    public String imgUrl;
    public String backup;
    public int point;

    public static RechargeBanner json2Entity(JSONObject jobj) {
        RechargeBanner b = new RechargeBanner();
        try {
            if (jobj.has("imgUrl") && !jobj.isNull("imgUrl")) {
                b.imgUrl = jobj.getString("imgUrl");
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
