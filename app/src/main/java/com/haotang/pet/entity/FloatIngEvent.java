package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/14 16:28
 */
public class FloatIngEvent {
    private int position;

    @Override
    public String toString() {
        return "FloatIngEvent{" +
                "position=" + position +
                '}';
    }

    public FloatIngEvent() {
    }

    public FloatIngEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
