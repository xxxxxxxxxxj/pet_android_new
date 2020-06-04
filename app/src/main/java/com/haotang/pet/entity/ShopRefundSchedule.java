package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/30
 * @Description:
 */
public class ShopRefundSchedule {

    /**
     * code : 0
     * data : {"examineSpeed":[{"examineContent":"已收到您的退货申请，请耐心等待审核。","examineTime":"2019-11-29 19:12:50"}]}
     * msg : 操作成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<ExamineSpeedBean> examineSpeed;

        public List<ExamineSpeedBean> getExamineSpeed() {
            return examineSpeed;
        }

        public void setExamineSpeed(List<ExamineSpeedBean> examineSpeed) {
            this.examineSpeed = examineSpeed;
        }

        public static class ExamineSpeedBean {
            /**
             * examineContent : 已收到您的退货申请，请耐心等待审核。
             * examineTime : 2019-11-29 19:12:50
             */

            private String examineContent;
            private String examineTime;

            public String getExamineContent() {
                return examineContent;
            }

            public void setExamineContent(String examineContent) {
                this.examineContent = examineContent;
            }

            public String getExamineTime() {
                return examineTime;
            }

            public void setExamineTime(String examineTime) {
                this.examineTime = examineTime;
            }
        }
    }
}
