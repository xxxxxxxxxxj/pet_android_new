package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/22 14:11
 */
public class DeleteItemEvent {
    private int myPetId;
    private int itemId;

    public int getMyPetId() {
        return myPetId;
    }

    public void setMyPetId(int myPetId) {
        this.myPetId = myPetId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public DeleteItemEvent() {
    }

    public DeleteItemEvent(int myPetId, int itemId) {
        this.myPetId = myPetId;
        this.itemId = itemId;
    }
}
