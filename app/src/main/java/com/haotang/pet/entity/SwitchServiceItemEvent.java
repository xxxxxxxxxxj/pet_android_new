package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 22:51
 */
public class SwitchServiceItemEvent {
    public final static int WORKERMENUITEM = 1;
    public final static int CHARACTERISTIC = 2;
    public final static int SWITCHSERVICE = 3;
    private int position;
    private int index;

    @Override
    public String toString() {
        return "SwitchServiceItemEvent{" +
                "position=" + position +
                ", index=" + index +
                '}';
    }

    public SwitchServiceItemEvent(int position, int index) {
        this.position = position;
        this.index = index;
    }

    public SwitchServiceItemEvent() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
