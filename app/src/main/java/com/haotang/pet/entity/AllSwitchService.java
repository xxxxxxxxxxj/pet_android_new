package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 21:21
 */
public class AllSwitchService {
    private SwitchService switchService1;
    private SwitchService switchService2;
    private SwitchService switchService3;
    private boolean isSelect1;
    private boolean isSelect2;
    private boolean isSelect3;

    public AllSwitchService(SwitchService switchService1, SwitchService switchService2, SwitchService switchService3) {
        this.switchService1 = switchService1;
        this.switchService2 = switchService2;
        this.switchService3 = switchService3;
    }

    public AllSwitchService(boolean isSelect1, boolean isSelect2, boolean isSelect3) {
        this.isSelect1 = isSelect1;
        this.isSelect2 = isSelect2;
        this.isSelect3 = isSelect3;
    }

    public AllSwitchService(SwitchService switchService1, SwitchService switchService2, SwitchService switchService3, boolean isSelect1, boolean isSelect2, boolean isSelect3) {
        this.switchService1 = switchService1;
        this.switchService2 = switchService2;
        this.switchService3 = switchService3;
        this.isSelect1 = isSelect1;
        this.isSelect2 = isSelect2;
        this.isSelect3 = isSelect3;
    }

    public AllSwitchService() {
    }

    public SwitchService getSwitchService1() {
        return switchService1;
    }

    public void setSwitchService1(SwitchService switchService1) {
        this.switchService1 = switchService1;
    }

    public SwitchService getSwitchService2() {
        return switchService2;
    }

    public void setSwitchService2(SwitchService switchService2) {
        this.switchService2 = switchService2;
    }

    public SwitchService getSwitchService3() {
        return switchService3;
    }

    public void setSwitchService3(SwitchService switchService3) {
        this.switchService3 = switchService3;
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
}
