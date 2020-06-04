package com.haotang.pet.entity;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-21 17:38
 */
public class RecommmendCashBack  implements Serializable {
    private String tip;
    private int cashBackIsOpen;
    private double grainGoldRate;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getCashBackIsOpen() {
        return cashBackIsOpen;
    }

    public void setCashBackIsOpen(int cashBackIsOpen) {
        this.cashBackIsOpen = cashBackIsOpen;
    }

    public double getGrainGoldRate() {
        return grainGoldRate;
    }

    public void setGrainGoldRate(double grainGoldRate) {
        this.grainGoldRate = grainGoldRate;
    }
}
