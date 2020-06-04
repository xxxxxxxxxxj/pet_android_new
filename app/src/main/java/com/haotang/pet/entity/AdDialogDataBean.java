package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/11/1 17:19
 */
public class AdDialogDataBean {
    public String activityPic;
    public int countType;
    public String backup;
    public int point;

    public static AdDialogDataBean json2Entity(JSONObject json) {
        AdDialogDataBean AdDialogDataBean = new AdDialogDataBean();
        try {
            if (json.has("countType") && !json.isNull("countType")) {
                AdDialogDataBean.countType = json.getInt("countType");
            }
            if (json.has("point") && !json.isNull("point")) {
                AdDialogDataBean.point = json.getInt("point");
            }
            if (json.has("activityPic") && !json.isNull("activityPic")) {
                AdDialogDataBean.activityPic = json.getString("activityPic");
            }
            if (json.has("backup") && !json.isNull("backup")) {
                AdDialogDataBean.backup = json.getString("backup");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AdDialogDataBean;
    }
}
