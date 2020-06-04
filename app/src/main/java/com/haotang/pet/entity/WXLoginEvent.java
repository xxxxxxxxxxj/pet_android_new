package com.haotang.pet.entity;

/**
 * @author:姜谷蓄
 * @Date:2020/2/10
 * @Description:
 */
public class WXLoginEvent {
    private String code;
    private int errCode;

    public WXLoginEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
