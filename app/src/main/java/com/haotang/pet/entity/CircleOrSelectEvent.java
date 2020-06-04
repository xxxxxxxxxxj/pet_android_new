package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/6/7 15:28
 */
public class CircleOrSelectEvent {
    private int selection;

    @Override
    public String toString() {
        return "CircleOrSelectEvent{" +
                "selection=" + selection +
                '}';
    }

    public CircleOrSelectEvent() {
    }

    public CircleOrSelectEvent(int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }
}
