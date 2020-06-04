package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/28 10:49
 */
public class RefundCardEvent {
    private boolean isRefund;

    public RefundCardEvent() {
    }

    public RefundCardEvent(boolean isRefund) {
        this.isRefund = isRefund;
    }

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }
}
