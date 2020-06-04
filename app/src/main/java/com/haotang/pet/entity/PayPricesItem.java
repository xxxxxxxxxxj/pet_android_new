package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/5/28 13:54
 */
public class PayPricesItem {
    private String payWayDesc;
    private String amount;

    public PayPricesItem() {
    }

    public PayPricesItem(String payWayDesc, String amount) {
        this.payWayDesc = payWayDesc;
        this.amount = amount;
    }

    public String getPayWayDesc() {
        return payWayDesc;
    }

    public void setPayWayDesc(String payWayDesc) {
        this.payWayDesc = payWayDesc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
