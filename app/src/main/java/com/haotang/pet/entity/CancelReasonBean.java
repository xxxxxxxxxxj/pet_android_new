package com.haotang.pet.entity;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-27 10:25
 */
public class CancelReasonBean implements Serializable {
    private int id;
    private String txt;
    private boolean isChoose;

    public CancelReasonBean() {
    }

    public CancelReasonBean(int id, String txt, boolean isChoose) {
        this.id = id;
        this.txt = txt;
        this.isChoose = isChoose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
