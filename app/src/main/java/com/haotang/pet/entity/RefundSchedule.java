package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/23 15:37
 */
public class RefundSchedule {
    private String name;
    private String time;
    private String desc;
    private String status;
    private String salePhone;
    private int red;

    public static RefundSchedule json2Entity(JSONObject jobj) {
        RefundSchedule refundSchedule = new RefundSchedule();
        try {
            if (jobj.has("salePhone") && !jobj.isNull("salePhone")) {
                refundSchedule.setSalePhone(jobj.getString("salePhone"));
            }
            if (jobj.has("name") && !jobj.isNull("name")) {
                refundSchedule.setName(jobj.getString("name"));
            }
            if (jobj.has("time") && !jobj.isNull("time")) {
                refundSchedule.setTime(jobj.getString("time"));
            }
            if (jobj.has("desc") && !jobj.isNull("desc")) {
                refundSchedule.setDesc(jobj.getString("desc"));
            }
            if (jobj.has("status") && !jobj.isNull("status")) {
                refundSchedule.setStatus(jobj.getString("status"));
            }
            if (jobj.has("red") && !jobj.isNull("red")) {
                refundSchedule.setRed(jobj.getInt("red"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return refundSchedule;
    }

    public String getSalePhone() {
        return salePhone;
    }

    public void setSalePhone(String salePhone) {
        this.salePhone = salePhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
