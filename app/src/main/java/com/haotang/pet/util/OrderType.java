package com.haotang.pet.util;

public enum OrderType {
    SHOP(20),GIFT_CARD(21),WASH(1),FOSTER(2),BRUSH_THEETH(22);
    private int _type;
    private OrderType(int type){
        _type = type;
    }

    public int getType() {
        return _type;
    }
}
