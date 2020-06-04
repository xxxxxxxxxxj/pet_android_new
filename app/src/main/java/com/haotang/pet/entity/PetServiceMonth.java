package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/17 16:37
 */
public class PetServiceMonth {
    private String year;
    private String month;
    private boolean isSelect;

    @Override
    public String toString() {
        return "PetServiceMonth{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    public PetServiceMonth() {
    }

    public PetServiceMonth(String year, String month, boolean isSelect) {
        this.year = year;
        this.month = month;
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
