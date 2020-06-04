package com.pet.baseapi.bean;

import java.io.Serializable;

/**
 * BaseResponse
 */
public class BaseResponse implements Serializable{
    public boolean success;

    /**huaziApi返回*/
    public boolean result;
    private String type;
    private String msg;
    private String error;
    private int code;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "success=" + success +
                ", result=" + result +
                ", type='" + type + '\'' +
                ", message='" + msg + '\'' +
                ", error='" + error + '\'' +
                ", code=" + code +
                '}';
    }
}
