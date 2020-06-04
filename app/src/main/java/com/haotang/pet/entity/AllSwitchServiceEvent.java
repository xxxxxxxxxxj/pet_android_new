package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 22:25
 */
public class AllSwitchServiceEvent {
    public final static int WORKERMENUITEM = 1;
    public final static int CHARACTERISTIC = 2;
    public final static int SWITCHSERVICE = 3;
    private int index;
    private int position;
    private int flag;

    @Override
    public String toString() {
        return "AllSwitchServiceEvent{" +
                "index=" + index +
                ", position=" + position +
                ", flag=" + flag +
                '}';
    }

    public AllSwitchServiceEvent(int index, int position, int flag) {
        this.index = index;
        this.position = position;
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AllSwitchServiceEvent() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
