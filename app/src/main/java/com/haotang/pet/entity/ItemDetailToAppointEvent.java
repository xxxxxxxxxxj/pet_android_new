package com.haotang.pet.entity;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/24 10:18
 */
public class ItemDetailToAppointEvent {
    private ApointMentItem item;
    private List<ApointMentPet> petList;
    private WorkerAndTime workerAndTime;

    public ItemDetailToAppointEvent(ApointMentItem item, List<ApointMentPet> petList) {
        this.item = item;
        this.petList = petList;
    }

    public ItemDetailToAppointEvent(ApointMentItem item, List<ApointMentPet> petList, WorkerAndTime workerAndTime) {
        this.item = item;
        this.petList = petList;
        this.workerAndTime = workerAndTime;
    }

    public ItemDetailToAppointEvent() {
    }

    public WorkerAndTime getWorkerAndTime() {
        return workerAndTime;
    }

    public void setWorkerAndTime(WorkerAndTime workerAndTime) {
        this.workerAndTime = workerAndTime;
    }

    public ApointMentItem getItem() {
        return item;
    }

    public void setItem(ApointMentItem item) {
        this.item = item;
    }

    public List<ApointMentPet> getPetList() {
        return petList;
    }

    public void setPetList(List<ApointMentPet> petList) {
        this.petList = petList;
    }
}
