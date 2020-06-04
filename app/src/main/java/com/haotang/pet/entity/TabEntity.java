package com.haotang.pet.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by Administrator on 2018/9/14 0014.
 */

public class TabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }

    @Override
    public String getTabSelectedIconByString() {
        return null;
    }

    @Override
    public String getTabUnSelectedIconByString() {
        return null;
    }

}
