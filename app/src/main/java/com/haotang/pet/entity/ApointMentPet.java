package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/20 18:31
 */
public class ApointMentPet implements Serializable {
    private String petImg;
    private String petNickName;
    private String serviceName;
    private double price;
    private double vipPrice;
    private int petId;
    private int myPetId;
    private int serviceId;
    private double itemPrice;
    private double itemVipPrice;
    private boolean isSelect;
    private List<ApointMentItem> itemList;
    private String priceSuffix;
    private boolean isSetItemDecor;
    public String AddServiceIds;
    public String PetExtra;
    private int state;//1.所有宠物都不支持，2.首席赠送，3.可操作,5.刷牙卡
    private int petKind;//1.狗，2.猫

    public int getPetKind() {
        return petKind;
    }

    public void setPetKind(int petKind) {
        this.petKind = petKind;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isSetItemDecor() {
        return isSetItemDecor;
    }

    public void setSetItemDecor(boolean setItemDecor) {
        isSetItemDecor = setItemDecor;
    }

    public String getPriceSuffix() {
        return priceSuffix;
    }

    public void setPriceSuffix(String priceSuffix) {
        this.priceSuffix = priceSuffix;
    }

    public ApointMentPet(String petImg, String petNickName, String serviceName, int petId, int myPetId, int serviceId) {
        this.petImg = petImg;
        this.petNickName = petNickName;
        this.serviceName = serviceName;
        this.petId = petId;
        this.myPetId = myPetId;
        this.serviceId = serviceId;
    }

    public ApointMentPet(String petImg, String petNickName, String serviceName, double price, double vipPrice, int petId, int myPetId, int serviceId) {
        this.petImg = petImg;
        this.petNickName = petNickName;
        this.serviceName = serviceName;
        this.price = price;
        this.vipPrice = vipPrice;
        this.petId = petId;
        this.myPetId = myPetId;
        this.serviceId = serviceId;
    }

    public ApointMentPet(String petImg, String petNickName, String serviceName, double price, double vipPrice, int petId, int myPetId, int serviceId, int petKind) {
        this.petImg = petImg;
        this.petNickName = petNickName;
        this.serviceName = serviceName;
        this.price = price;
        this.vipPrice = vipPrice;
        this.petId = petId;
        this.myPetId = myPetId;
        this.serviceId = serviceId;
        this.petKind = petKind;
    }

    public ApointMentPet() {
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getItemVipPrice() {
        return itemVipPrice;
    }

    public void setItemVipPrice(double itemVipPrice) {
        this.itemVipPrice = itemVipPrice;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getMyPetId() {
        return myPetId;
    }

    public void setMyPetId(int myPetId) {
        this.myPetId = myPetId;
    }

    public String getPetImg() {
        return petImg;
    }

    public void setPetImg(String petImg) {
        this.petImg = petImg;
    }

    public String getPetNickName() {
        return petNickName;
    }

    public void setPetNickName(String petNickName) {
        this.petNickName = petNickName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public List<ApointMentItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ApointMentItem> itemList) {
        this.itemList = itemList;
    }
}
