package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/4/4 13:06
 */
public class WashPetService {
    private int serviceLoc;
    private String petName;
    private String serviceName;
    private double basicServicePrice;
    private double extraPrice;
    private double zjdxPrice;
    private double payPrice;
    private double cardPayPrice;
    private String jcfwName;
    private String dxfwName;
    private String zjdxName;
    private int serviceType;
    private int petId;
    private int serviceId;
    private int myPetId;
    private String itemIds;

    public double getCardPayPrice() {
        return cardPayPrice;
    }

    public void setCardPayPrice(double cardPayPrice) {
        this.cardPayPrice = cardPayPrice;
    }

    public double getZjdxPrice() {
        return zjdxPrice;
    }

    public void setZjdxPrice(double zjdxPrice) {
        this.zjdxPrice = zjdxPrice;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
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

    public String getItemIds() {
        return itemIds;
    }

    public void setItemIds(String itemIds) {
        this.itemIds = itemIds;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getServiceLoc() {
        return serviceLoc;
    }

    public void setServiceLoc(int serviceLoc) {
        this.serviceLoc = serviceLoc;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getBasicServicePrice() {
        return basicServicePrice;
    }

    public void setBasicServicePrice(double basicServicePrice) {
        this.basicServicePrice = basicServicePrice;
    }

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public String getJcfwName() {
        return jcfwName;
    }

    public void setJcfwName(String jcfwName) {
        this.jcfwName = jcfwName;
    }

    public String getDxfwName() {
        return dxfwName;
    }

    public void setDxfwName(String dxfwName) {
        this.dxfwName = dxfwName;
    }

    public String getZjdxName() {
        return zjdxName;
    }

    public void setZjdxName(String zjdxName) {
        this.zjdxName = zjdxName;
    }
}
