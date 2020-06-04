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
 * @date zhoujunxia on 2018/8/17 17:07
 */
public class PetDiary {
    private boolean isSetImgDecor;
    private String serviceName;
    private String appointment;
    private String extraItem;
    private String workerName;
    private String content;
    private String babyContent;
    private String createTimes;
    private List<PetDiaryImg> beforeImgList = new ArrayList<PetDiaryImg>();
    private List<PetDiaryImg> afterImgList = new ArrayList<PetDiaryImg>();

    public static PetDiary json2Entity(JSONObject jobj) {
        PetDiary petDiary = new PetDiary();
        try {
            if (jobj.has("babyContent") && !jobj.isNull("babyContent")) {
                petDiary.setBabyContent(jobj.getString("babyContent"));
            }
            if (jobj.has("createTimes") && !jobj.isNull("createTimes")) {
                petDiary.setCreateTimes(jobj.getString("createTimes"));
            }
            if (jobj.has("serviceName") && !jobj.isNull("serviceName")) {
                petDiary.setServiceName(jobj.getString("serviceName"));
            }
            if (jobj.has("appointment") && !jobj.isNull("appointment")) {
                petDiary.setAppointment(jobj.getString("appointment"));
            }
            if (jobj.has("extraItem") && !jobj.isNull("extraItem")) {
                petDiary.setExtraItem(jobj.getString("extraItem"));
            }
            if (jobj.has("workerName") && !jobj.isNull("workerName")) {
                petDiary.setWorkerName(jobj.getString("workerName"));
            }
            if (jobj.has("content") && !jobj.isNull("content")) {
                petDiary.setContent(jobj.getString("content"));
            }
            petDiary.getBeforeImgList().clear();
            if (jobj.has("beforePics") && !jobj.isNull("beforePics")) {
                JSONArray jarrbeforePics = jobj.getJSONArray("beforePics");
                if (jarrbeforePics != null && jarrbeforePics.length() > 0) {
                    for (int i = 0; i < jarrbeforePics.length(); i++) {
                        petDiary.getBeforeImgList().add(new PetDiaryImg(jarrbeforePics.getString(i), "服务前"));
                    }
                }
            }
            petDiary.getAfterImgList().clear();
            if (jobj.has("pics") && !jobj.isNull("pics")) {
                JSONArray jarrpics = jobj.getJSONArray("pics");
                if (jarrpics != null && jarrpics.length() > 0) {
                    for (int i = 0; i < jarrpics.length(); i++) {
                        petDiary.getAfterImgList().add(new PetDiaryImg(jarrpics.getString(i), "服务后"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return petDiary;
    }

    public String getBabyContent() {
        return babyContent;
    }

    public void setBabyContent(String babyContent) {
        this.babyContent = babyContent;
    }

    public String getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(String createTimes) {
        this.createTimes = createTimes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getExtraItem() {
        return extraItem;
    }

    public void setExtraItem(String extraItem) {
        this.extraItem = extraItem;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PetDiaryImg> getBeforeImgList() {
        return beforeImgList;
    }

    public void setBeforeImgList(List<PetDiaryImg> beforeImgList) {
        this.beforeImgList = beforeImgList;
    }

    public List<PetDiaryImg> getAfterImgList() {
        return afterImgList;
    }

    public void setAfterImgList(List<PetDiaryImg> afterImgList) {
        this.afterImgList = afterImgList;
    }

    public boolean isSetImgDecor() {
        return isSetImgDecor;
    }

    public void setSetImgDecor(boolean setImgDecor) {
        isSetImgDecor = setImgDecor;
    }
}
