package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/24 16:18
 */
public class AppointMentDate {
    private String date;
    private int year;
    private int isFull;
    private String desc;
    private String time;
    private int pickup;
    private boolean isSelect;
    private java.util.List<AppointMentTime> times;
    private boolean isSetItemDecor;

    @Override
    public String toString() {
        return "AppointMentDate{" +
                "date='" + date + '\'' +
                ", year=" + year +
                ", isFull=" + isFull +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
                ", pickup=" + pickup +
                ", isSelect=" + isSelect +
                ", times=" + times +
                '}';
    }

    public static AppointMentDate json2Entity(JSONObject jobj) {
        AppointMentDate appointMentDate = new AppointMentDate();
        try {
            if (jobj.has("date") && !jobj.isNull("date")) {
                appointMentDate.setDate(jobj.getString("date"));
            }
            if (jobj.has("year") && !jobj.isNull("year")) {
                appointMentDate.setYear(jobj.getInt("year"));
            }
            if (jobj.has("isFull") && !jobj.isNull("isFull")) {
                appointMentDate.setIsFull(jobj.getInt("isFull"));
            }
            if (jobj.has("desc") && !jobj.isNull("desc")) {
                appointMentDate.setDesc(jobj.getString("desc"));
            }
            if (jobj.has("time") && !jobj.isNull("time")) {
                appointMentDate.setTime(jobj.getString("time"));
            }
            if (jobj.has("pickup") && !jobj.isNull("pickup")) {
                appointMentDate.setPickup(jobj.getInt("pickup"));
            }
            if (jobj.has("times") && !jobj.isNull("times")) {
                JSONArray jarrtimes = jobj.getJSONArray("times");
                if (jarrtimes.length() > 0) {
                    List<AppointMentTime> list = new ArrayList<AppointMentTime>();
                    list.clear();
                    for (int i = 0; i < jarrtimes.length(); i++) {
                        list.add(AppointMentTime.json2Entity(jarrtimes
                                .getJSONObject(i)));
                    }
                    appointMentDate.setTimes(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointMentDate;
    }

    public boolean isSetItemDecor() {
        return isSetItemDecor;
    }

    public void setSetItemDecor(boolean setItemDecor) {
        isSetItemDecor = setItemDecor;
    }

    public List<AppointMentTime> getTimes() {
        return times;
    }

    public void setTimes(List<AppointMentTime> times) {
        this.times = times;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getIsFull() {
        return isFull;
    }

    public void setIsFull(int isFull) {
        this.isFull = isFull;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPickup() {
        return pickup;
    }

    public void setPickup(int pickup) {
        this.pickup = pickup;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
