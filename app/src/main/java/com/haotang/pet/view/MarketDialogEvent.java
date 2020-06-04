package com.haotang.pet.view;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/11/1 12:04
 */
public class MarketDialogEvent {
    public static final int SELECTPOST = 1;
    public static final int PETHOUSEPOST = 2;
    public static final int EVALUATE = 3;
    private int eventType;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public MarketDialogEvent(int eventType) {
        this.eventType = eventType;
    }
}
