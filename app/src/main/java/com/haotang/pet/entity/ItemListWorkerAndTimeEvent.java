package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/30 17:07
 */
public class ItemListWorkerAndTimeEvent {
    private AppointWorker appointWorker;
    private String appointment;
    private int dayposition;
    private int pickup;
    private ItemToMorePet itemToMorePet;
    private String workerIds;

    public ItemListWorkerAndTimeEvent(AppointWorker appointWorker, String appointment, int dayposition, int pickup, ItemToMorePet itemToMorePet,String workerIds) {
        this.appointWorker = appointWorker;
        this.appointment = appointment;
        this.dayposition = dayposition;
        this.pickup = pickup;
        this.itemToMorePet = itemToMorePet;
        this.workerIds = workerIds;
    }

    public ItemToMorePet getItemToMorePet() {
        return itemToMorePet;
    }

    public void setItemToMorePet(ItemToMorePet itemToMorePet) {
        this.itemToMorePet = itemToMorePet;
    }

    public String getWorkerIds() {
        return workerIds;
    }

    public void setWorkerIds(String workerIds) {
        this.workerIds = workerIds;
    }

    public int getPickup() {
        return pickup;
    }

    public void setPickup(int pickup) {
        this.pickup = pickup;
    }

    public AppointWorker getAppointWorker() {
        return appointWorker;
    }

    public int getDayposition() {
        return dayposition;
    }

    public void setDayposition(int dayposition) {
        this.dayposition = dayposition;
    }

    public void setAppointWorker(AppointWorker appointWorker) {
        this.appointWorker = appointWorker;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public ItemListWorkerAndTimeEvent() {
    }
}
