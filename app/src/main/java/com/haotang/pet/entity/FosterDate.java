package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-22 15:40
 */
public class FosterDate {
    private String month;
    private List<FosterDay> days;
    private long headerId;

    public static FosterDate jsonToEntity(JSONObject json) {
        FosterDate fosterDate = new FosterDate();
        try {
            if (json.has("month") && !json.isNull("month")) {
                fosterDate.setMonth(json.getString("month"));
            }
            if (json.has("days") && !json.isNull("days")) {
                JSONArray jarrdays = json.getJSONArray("days");
                if (jarrdays.length() > 0) {
                    ArrayList<FosterDay> dayList = new ArrayList<FosterDay>();
                    for (int i = 0; i < jarrdays.length(); i++) {
                        dayList.add(FosterDay.jsonToEntity(jarrdays.getJSONObject(i)));
                    }
                    fosterDate.setDays(dayList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fosterDate;
    }

    public long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(long headerId) {
        this.headerId = headerId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<FosterDay> getDays() {
        return days;
    }

    public void setDays(List<FosterDay> days) {
        this.days = days;
    }
}
