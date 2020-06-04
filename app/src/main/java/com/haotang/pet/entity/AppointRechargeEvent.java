package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/21 17:55
 */
public class AppointRechargeEvent {
    private boolean isRecharge;

    public AppointRechargeEvent() {
    }

    public AppointRechargeEvent(boolean isRecharge) {
        this.isRecharge = isRecharge;
    }

    public boolean isRecharge() {
        return isRecharge;
    }

    public void setRecharge(boolean recharge) {
        isRecharge = recharge;
    }
}
