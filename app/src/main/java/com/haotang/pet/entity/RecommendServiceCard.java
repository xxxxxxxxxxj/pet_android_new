package com.haotang.pet.entity;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/4/8 12:35
 */
public class RecommendServiceCard implements Serializable {
    private String tip;
    private String btn_txt;
    private String backup;
    private int point;
    private double discount;
    private int serviceCardId;

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getBtn_txt() {
        return btn_txt;
    }

    public void setBtn_txt(String btn_txt) {
        this.btn_txt = btn_txt;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getServiceCardId() {
        return serviceCardId;
    }

    public void setServiceCardId(int serviceCardId) {
        this.serviceCardId = serviceCardId;
    }
}
