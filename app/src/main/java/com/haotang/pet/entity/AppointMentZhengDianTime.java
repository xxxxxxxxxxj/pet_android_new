package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 10:09
 */
public class AppointMentZhengDianTime {
    private AppointMentDate appointMentDate1;
    private AppointMentDate appointMentDate2;
    private AppointMentDate appointMentDate3;
    private AppointMentDate appointMentDate4;
    private boolean isSelect1;
    private boolean isSelect2;
    private boolean isSelect3;
    private boolean isSelect4;

    public AppointMentZhengDianTime(boolean isSelect1, boolean isSelect2, boolean isSelect3, boolean isSelect4) {
        this.isSelect1 = isSelect1;
        this.isSelect2 = isSelect2;
        this.isSelect3 = isSelect3;
        this.isSelect4 = isSelect4;
    }

    public AppointMentZhengDianTime() {
    }

    public AppointMentZhengDianTime(AppointMentDate appointMentDate1, AppointMentDate appointMentDate2, AppointMentDate appointMentDate3, AppointMentDate appointMentDate4, boolean isSelect1, boolean isSelect2, boolean isSelect3, boolean isSelect4) {
        this.appointMentDate1 = appointMentDate1;
        this.appointMentDate2 = appointMentDate2;
        this.appointMentDate3 = appointMentDate3;
        this.appointMentDate4 = appointMentDate4;
        this.isSelect1 = isSelect1;
        this.isSelect2 = isSelect2;
        this.isSelect3 = isSelect3;
        this.isSelect4 = isSelect4;
    }

    public AppointMentZhengDianTime(AppointMentDate appointMentDate1, AppointMentDate appointMentDate2, AppointMentDate appointMentDate3, AppointMentDate appointMentDate4) {
        this.appointMentDate1 = appointMentDate1;
        this.appointMentDate2 = appointMentDate2;
        this.appointMentDate3 = appointMentDate3;
        this.appointMentDate4 = appointMentDate4;
    }

    public boolean isSelect1() {
        return isSelect1;
    }

    public void setSelect1(boolean select1) {
        isSelect1 = select1;
    }

    public boolean isSelect2() {
        return isSelect2;
    }

    public void setSelect2(boolean select2) {
        isSelect2 = select2;
    }

    public boolean isSelect3() {
        return isSelect3;
    }

    public void setSelect3(boolean select3) {
        isSelect3 = select3;
    }

    public boolean isSelect4() {
        return isSelect4;
    }

    public void setSelect4(boolean select4) {
        isSelect4 = select4;
    }

    public AppointMentDate getAppointMentDate1() {
        return appointMentDate1;
    }

    public void setAppointMentDate1(AppointMentDate appointMentDate1) {
        this.appointMentDate1 = appointMentDate1;
    }

    public AppointMentDate getAppointMentDate2() {
        return appointMentDate2;
    }

    public void setAppointMentDate2(AppointMentDate appointMentDate2) {
        this.appointMentDate2 = appointMentDate2;
    }

    public AppointMentDate getAppointMentDate3() {
        return appointMentDate3;
    }

    public void setAppointMentDate3(AppointMentDate appointMentDate3) {
        this.appointMentDate3 = appointMentDate3;
    }

    public AppointMentDate getAppointMentDate4() {
        return appointMentDate4;
    }

    public void setAppointMentDate4(AppointMentDate appointMentDate4) {
        this.appointMentDate4 = appointMentDate4;
    }
}
