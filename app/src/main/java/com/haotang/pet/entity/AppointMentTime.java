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
 * @date zhoujunxia on 2018/8/24 16:37
 */
public class AppointMentTime {
    private String time;
    private java.util.List<Integer> workers;
    private int pickup;
    private boolean isSelect;

    @Override
    public String toString() {
        return "AppointMentTime{" +
                "time='" + time + '\'' +
                ", workers=" + workers +
                ", pickup=" + pickup +
                ", isSelect=" + isSelect +
                '}';
    }

    public static AppointMentTime json2Entity(JSONObject jobj) {
        AppointMentTime appointMentTime = new AppointMentTime();
        try {
            if (jobj.has("time") && !jobj.isNull("time")) {
                appointMentTime.setTime(jobj.getString("time"));
            }
            if (jobj.has("pickup") && !jobj.isNull("pickup")) {
                appointMentTime.setPickup(jobj.getInt("pickup"));
            }
            if (jobj.has("workers") && !jobj.isNull("workers")) {
                JSONArray jarrworkers = jobj.getJSONArray("workers");
                if (jarrworkers.length() > 0) {
                    List<Integer> list = new ArrayList<Integer>();
                    list.clear();
                    for (int i = 0; i < jarrworkers.length(); i++) {
                        list.add(jarrworkers.getInt(i));
                    }
                    appointMentTime.setWorkers(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointMentTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Integer> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Integer> workers) {
        this.workers = workers;
    }

    public int getPickup() {
        return pickup;
    }

    public void setPickup(int pickup) {
        this.pickup = pickup;
    }
}
