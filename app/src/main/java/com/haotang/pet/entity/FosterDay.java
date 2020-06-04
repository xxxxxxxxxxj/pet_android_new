package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-22 15:43
 */
public class FosterDay {
    private String date;
    private String badge;
    private int isFull;
    private int week;
    private String day;
    private boolean isStart;
    private boolean isEnd;
    private boolean isMiddle;
    private boolean isMiddleTemp;
    private boolean isSelectEnd;

    public FosterDay(String date) {
        this.date = date;
    }

    public FosterDay() {
    }

    public static FosterDay jsonToEntity(JSONObject json) {
        FosterDay fosterDay = new FosterDay();
        try {
            if (json.has("date") && !json.isNull("date")) {
                fosterDay.setDate(json.getString("date"));
            }
            if (json.has("badge") && !json.isNull("badge")) {
                fosterDay.setBadge(json.getString("badge"));
            }
            if (json.has("isFull") && !json.isNull("isFull")) {
                fosterDay.setIsFull(json.getInt("isFull"));
            }
            if (json.has("day") && !json.isNull("day")) {
                fosterDay.setDay(json.getString("day"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fosterDay;
    }

    public boolean isSelectEnd() {
        return isSelectEnd;
    }

    public void setSelectEnd(boolean selectEnd) {
        isSelectEnd = selectEnd;
    }

    public boolean isMiddleTemp() {
        return isMiddleTemp;
    }

    public void setMiddleTemp(boolean middleTemp) {
        isMiddleTemp = middleTemp;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public boolean isMiddle() {
        return isMiddle;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setMiddle(boolean middle) {
        isMiddle = middle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getIsFull() {
        return isFull;
    }

    public void setIsFull(int isFull) {
        this.isFull = isFull;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

