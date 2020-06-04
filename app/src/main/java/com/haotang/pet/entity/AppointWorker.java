package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/21 11:33
 */
public class AppointWorker implements Serializable {
    private int workerId;
    private int updateWorkerId;
    private String defaultWorkerTag;
    private int tid;
    private String tag;
    private String avatar;
    private String realName;
    private String earliest;
    private int orderTotal;
    private String goodRate;
    private List<AppointWorkerPrice> priceLiest;
    private List<String> expertiseLiest;
    private boolean isSetItemDecor;

    public AppointWorker(int workerId, int tid, String avatar, String realName, int orderTotal, String goodRate) {
        this.workerId = workerId;
        this.tid = tid;
        this.avatar = avatar;
        this.realName = realName;
        this.orderTotal = orderTotal;
        this.goodRate = goodRate;
    }

    public AppointWorker(int workerId, int tid, String avatar, String realName) {
        this.workerId = workerId;
        this.tid = tid;
        this.avatar = avatar;
        this.realName = realName;
    }

    public AppointWorker(int workerId, int tid, String avatar, String realName, int updateWorkerId) {
        this.workerId = workerId;
        this.tid = tid;
        this.avatar = avatar;
        this.realName = realName;
        this.updateWorkerId = updateWorkerId;
    }

    public AppointWorker() {
    }

    public static AppointWorker json2Entity(JSONObject jobj) {
        AppointWorker appointWorker = new AppointWorker();
        try {
            if (jobj.has("orderTotal") && !jobj.isNull("orderTotal")) {
                appointWorker.setOrderTotal(jobj.getInt("orderTotal"));
            }
            if (jobj.has("orderAmount") && !jobj.isNull("orderAmount")) {
                appointWorker.setOrderTotal(jobj.getInt("orderAmount"));
            }
            if (jobj.has("goodRate") && !jobj.isNull("goodRate")) {
                appointWorker.setGoodRate(jobj.getString("goodRate"));
            }
            if (jobj.has("id") && !jobj.isNull("id")) {
                appointWorker.setWorkerId(jobj.getInt("id"));
            }
            if (jobj.has("defaultWorkerTag") && !jobj.isNull("defaultWorkerTag")) {
                appointWorker.setDefaultWorkerTag(jobj.getString("defaultWorkerTag"));
            }
            if (jobj.has("tid") && !jobj.isNull("tid")) {
                appointWorker.setTid(jobj.getInt("tid"));
            }
            if (jobj.has("tag") && !jobj.isNull("tag")) {
                appointWorker.setTag(jobj.getString("tag"));
            }
            if (jobj.has("avatar") && !jobj.isNull("avatar")) {
                appointWorker.setAvatar(jobj.getString("avatar"));
            }
            if (jobj.has("realName") && !jobj.isNull("realName")) {
                appointWorker.setRealName(jobj.getString("realName"));
            }
            if (jobj.has("earliest") && !jobj.isNull("earliest")) {
                appointWorker.setEarliest(jobj.getString("earliest"));
            }
            if (jobj.has("expertise") && !jobj.isNull("expertise")) {
                JSONArray jarrexpertise = jobj.getJSONArray("expertise");
                if (jarrexpertise != null && jarrexpertise.length() > 0) {
                    List<String> list = new ArrayList<String>();
                    list.clear();
                    for (int i = 0; i < jarrexpertise.length(); i++) {
                        list.add(jarrexpertise.getString(i));
                    }
                    appointWorker.setExpertiseLiest(list);
                }
            }
            if (jobj.has("prices") && !jobj.isNull("prices")) {
                JSONArray jarrprices = jobj.getJSONArray("prices");
                if (jarrprices != null && jarrprices.length() > 0) {
                    List<AppointWorkerPrice> list = new ArrayList<AppointWorkerPrice>();
                    list.clear();
                    for (int i = 0; i < jarrprices.length(); i++) {
                        list.add(AppointWorkerPrice.json2Entity(jarrprices
                                .getJSONObject(i)));
                    }
                    appointWorker.setPriceLiest(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointWorker;
    }

    public int getUpdateWorkerId() {
        return updateWorkerId;
    }

    public void setUpdateWorkerId(int updateWorkerId) {
        this.updateWorkerId = updateWorkerId;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(String goodRate) {
        this.goodRate = goodRate;
    }

    public boolean isSetItemDecor() {
        return isSetItemDecor;
    }

    public void setSetItemDecor(boolean setItemDecor) {
        isSetItemDecor = setItemDecor;
    }

    public List<String> getExpertiseLiest() {
        return expertiseLiest;
    }

    public void setExpertiseLiest(List<String> expertiseLiest) {
        this.expertiseLiest = expertiseLiest;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getDefaultWorkerTag() {
        return defaultWorkerTag;
    }

    public void setDefaultWorkerTag(String defaultWorkerTag) {
        this.defaultWorkerTag = defaultWorkerTag;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realname) {
        this.realName = realname;
    }

    public String getEarliest() {
        return earliest;
    }

    public void setEarliest(String earliest) {
        this.earliest = earliest;
    }

    public List<AppointWorkerPrice> getPriceLiest() {
        return priceLiest;
    }

    public void setPriceLiest(List<AppointWorkerPrice> priceLiest) {
        this.priceLiest = priceLiest;
    }
}
