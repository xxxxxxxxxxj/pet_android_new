package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 11:30
 */
public class AppointmentTimeEvent {
    private int position;
    private boolean changeOrder;

    public AppointmentTimeEvent() {
    }

    public boolean isChangeOrder() {
        return changeOrder;
    }
    public void setChangeOrder(boolean changeOrder) {
        this.changeOrder = changeOrder;
    }


    public AppointmentTimeEvent(int position,boolean changeOrder) {
        this.position = position;
        this.changeOrder = changeOrder;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
