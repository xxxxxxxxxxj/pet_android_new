package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/30 14:56
 */
public class ItemToMorePet implements Serializable {
    private List<ApointMentItem> apointMentItem;
    private List<Integer> myPetIds;

    @Override
    public String toString() {
        return "ItemToMorePet{" +
                "apointMentItem=" + apointMentItem +
                ", myPetIds=" + myPetIds +
                '}';
    }

    public ItemToMorePet(List<ApointMentItem> apointMentItem, List<Integer> myPetIds) {
        this.apointMentItem = apointMentItem;
        this.myPetIds = myPetIds;
    }

    public ItemToMorePet() {
    }

    public List<ApointMentItem> getApointMentItem() {
        return apointMentItem;
    }

    public void setApointMentItem(List<ApointMentItem> apointMentItem) {
        this.apointMentItem = apointMentItem;
    }

    public List<Integer> getMyPetIds() {
        return myPetIds;
    }

    public void setMyPetIds(List<Integer> myPetIds) {
        this.myPetIds = myPetIds;
    }
}
