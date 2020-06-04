package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/15 18:15
 */
public class AdLoginEvent {
    private int position;

    @Override
    public String toString() {
        return "AdLoginEvent{" +
                "position=" + position +
                '}';
    }

    public AdLoginEvent() {
    }

    public AdLoginEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
