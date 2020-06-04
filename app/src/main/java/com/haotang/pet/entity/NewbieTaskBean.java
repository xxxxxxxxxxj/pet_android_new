package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/6/22 11:38
 */
public class NewbieTaskBean {
    private int point;
    private String backup;
    private String pic;
    private int taskStatus;
    private int id;

    public NewbieTaskBean(String pic, int id, int taskStatus, String desc, int point, String backup) {
        this.id = id;
        this.pic = pic;
        this.taskStatus = taskStatus;
        this.point = point;
        this.backup = backup;
    }

    public NewbieTaskBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public static NewbieTaskBean json2Entity(JSONObject jobj) {
        NewbieTaskBean b = new NewbieTaskBean();
        try {
            if (jobj.has("pic") && !jobj.isNull("pic")) {
                b.pic = jobj.getString("pic");
            }
            if (jobj.has("extendParam") && !jobj.isNull("extendParam")) {
                JSONObject jextendParam = jobj.getJSONObject("extendParam");
                if (jextendParam.has("taskStatus") && !jextendParam.isNull("taskStatus")) {
                    b.taskStatus = Integer.parseInt(jextendParam.getString("taskStatus"));
                }
            }
            if (jobj.has("backup") && !jobj.isNull("backup")) {
                b.backup = jobj.getString("backup");
            }
            if (jobj.has("point") && !jobj.isNull("point")) {
                b.point = jobj.getInt("point");
            }
            if (jobj.has("id") && !jobj.isNull("id")) {
                b.id = jobj.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
