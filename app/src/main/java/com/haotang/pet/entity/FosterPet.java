package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-16 17:51
 */
public class FosterPet {
    private String avatar;
    private String nickname;
    private int isAddPet;
    private int serviceFeeDays;
    private double totalServiceFee;
    private double serviceFee;
    private String serviceFeeName;
    private double totalExtraServiceFee;
    private double extraServiceFee;
    private String extraServiceName;
    private int extraServiceFeeDays;
    private String bathFeeName;
    private double listBathFee;
    private int id;
    private int petId;
    private int serviceId;
    private int myPetId;
    private int tid;

    public static FosterPet jsonToEntity(JSONObject json, int status) {
        FosterPet fosterPet = new FosterPet();
        try {
            if (json.has("petId") && !json.isNull("petId")) {
                fosterPet.setPetId(json.getInt("petId"));
            }
            if (json.has("serviceId") && !json.isNull("serviceId")) {
                fosterPet.setServiceId(json.getInt("serviceId"));
            }
            if (json.has("myPetId") && !json.isNull("myPetId")) {
                fosterPet.setMyPetId(json.getInt("myPetId"));
            }
            if (json.has("tid") && !json.isNull("tid")) {
                fosterPet.setTid(json.getInt("tid"));
            }
            if (json.has("id") && !json.isNull("id")) {
                fosterPet.setId(json.getInt("id"));
            }
            if (json.has("serviceFeeName") && !json.isNull("serviceFeeName")) {
                fosterPet.setServiceFeeName(json.getString("serviceFeeName"));
            }
            if (json.has("extraServiceName") && !json.isNull("extraServiceName")) {
                fosterPet.setExtraServiceName(json.getString("extraServiceName"));
            }
            if (status == 1) {
                if (json.has("bathFeeName") && !json.isNull("bathFeeName")) {
                    fosterPet.setBathFeeName(json.getString("bathFeeName"));
                }
                if (json.has("listBathFee") && !json.isNull("listBathFee")) {
                    fosterPet.setListBathFee(json.getDouble("listBathFee"));
                }
            }
            if (json.has("avatar") && !json.isNull("avatar")) {
                fosterPet.setAvatar(json.getString("avatar"));
            }
            if (json.has("nickname") && !json.isNull("nickname")) {
                fosterPet.setNickname(json.getString("nickname"));
            }
            if (json.has("isAddPet") && !json.isNull("isAddPet")) {
                fosterPet.setIsAddPet(json.getInt("isAddPet"));
            }
            if (json.has("serviceFeeDays") && !json.isNull("serviceFeeDays")) {
                fosterPet.setServiceFeeDays(json.getInt("serviceFeeDays"));
            }
            if (json.has("totalServiceFee") && !json.isNull("totalServiceFee")) {
                fosterPet.setTotalServiceFee(json.getDouble("totalServiceFee"));
            }
            if (json.has("serviceFee") && !json.isNull("serviceFee")) {
                fosterPet.setServiceFee(json.getDouble("serviceFee"));
            }
            if (json.has("totalExtraServiceFee") && !json.isNull("totalExtraServiceFee")) {
                fosterPet.setTotalExtraServiceFee(json.getDouble("totalExtraServiceFee"));
            }
            if (json.has("extraServiceFee") && !json.isNull("extraServiceFee")) {
                fosterPet.setExtraServiceFee(json.getDouble("extraServiceFee"));
            }
            if (json.has("extraServiceFeeDays") && !json.isNull("extraServiceFeeDays")) {
                fosterPet.setExtraServiceFeeDays(json.getInt("extraServiceFeeDays"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fosterPet;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getMyPetId() {
        return myPetId;
    }

    public void setMyPetId(int myPetId) {
        this.myPetId = myPetId;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceFeeName() {
        return serviceFeeName;
    }

    public void setServiceFeeName(String serviceFeeName) {
        this.serviceFeeName = serviceFeeName;
    }

    public String getExtraServiceName() {
        return extraServiceName;
    }

    public void setExtraServiceName(String extraServiceName) {
        this.extraServiceName = extraServiceName;
    }

    public String getBathFeeName() {
        return bathFeeName;
    }

    public void setBathFeeName(String bathFeeName) {
        this.bathFeeName = bathFeeName;
    }

    public double getListBathFee() {
        return listBathFee;
    }

    public void setListBathFee(double listBathFee) {
        this.listBathFee = listBathFee;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIsAddPet() {
        return isAddPet;
    }

    public void setIsAddPet(int isAddPet) {
        this.isAddPet = isAddPet;
    }

    public int getServiceFeeDays() {
        return serviceFeeDays;
    }

    public void setServiceFeeDays(int serviceFeeDays) {
        this.serviceFeeDays = serviceFeeDays;
    }

    public double getTotalServiceFee() {
        return totalServiceFee;
    }

    public void setTotalServiceFee(double totalServiceFee) {
        this.totalServiceFee = totalServiceFee;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getTotalExtraServiceFee() {
        return totalExtraServiceFee;
    }

    public void setTotalExtraServiceFee(double totalExtraServiceFee) {
        this.totalExtraServiceFee = totalExtraServiceFee;
    }

    public double getExtraServiceFee() {
        return extraServiceFee;
    }

    public void setExtraServiceFee(double extraServiceFee) {
        this.extraServiceFee = extraServiceFee;
    }

    public int getExtraServiceFeeDays() {
        return extraServiceFeeDays;
    }

    public void setExtraServiceFeeDays(int extraServiceFeeDays) {
        this.extraServiceFeeDays = extraServiceFeeDays;
    }
}
