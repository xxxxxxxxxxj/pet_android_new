package com.haotang.pet.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/23 14:50
 */
public class WorkerDifference implements Serializable {
    private String first;
    private String second;
    private String desc;
    private String img;

    public static WorkerDifference json2Entity(JSONObject jobj) {
        WorkerDifference workerDifference = new WorkerDifference();
        try {
            if (jobj.has("first") && !jobj.isNull("first")) {
                workerDifference.setFirst(jobj.getString("first"));
            }
            if (jobj.has("second") && !jobj.isNull("second")) {
                workerDifference.setSecond(jobj.getString("second"));
            }
            if (jobj.has("desc") && !jobj.isNull("desc")) {
                workerDifference.setDesc(jobj.getString("desc"));
            }
            if (jobj.has("img") && !jobj.isNull("img")) {
                workerDifference.setImg(jobj.getString("img"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workerDifference;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
