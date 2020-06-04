package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/28
 * @Description:主粮返现
 */
public class CashBackBean {


    /**
     * code : 0
     * data : {"stayMoney":"34.22","enteredMoney":"0","fullMoney":"1111","list":[{"amount":2.15,"content":"服务下单","createTime":"2019-11-23 11:11:18","endTime":"1970-01-01 00:00:00","isOverdue":0,"state":0,"tradeType":1}]}
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
        /**
         * stayMoney : 34.22
         * enteredMoney : 0
         * fullMoney : 1111
         * list : [{"amount":2.15,"content":"服务下单","createTime":"2019-11-23 11:11:18","endTime":"1970-01-01 00:00:00","isOverdue":0,"state":0,"tradeType":1}]
         */

        private double stayMoney;
        private double enteredMoney;
        private String fullMoney;
        private String backExplain;

        public String getBackExplain() {
            return backExplain;
        }

        public void setBackExplain(String backExplain) {
            this.backExplain = backExplain;
        }

        private List<ListBean> list;

        public double getStayMoney() {
            return stayMoney;
        }

        public void setStayMoney(double stayMoney) {
            this.stayMoney = stayMoney;
        }

        public double getEnteredMoney() {
            return enteredMoney;
        }

        public void setEnteredMoney(double enteredMoney) {
            this.enteredMoney = enteredMoney;
        }

        public String getFullMoney() {
            return fullMoney;
        }

        public void setFullMoney(String fullMoney) {
            this.fullMoney = fullMoney;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * amount : 2.15
             * content : 服务下单
             * createTime : 2019-11-23 11:11:18
             * endTime : 1970-01-01 00:00:00
             * isOverdue : 0
             * state : 0
             * tradeType : 1
             */

            private String amount;
            private String content;
            private String createTime;
            private String endTime;
            private int isOverdue;
            private int state;
            private int tradeType;
            private int detailState;

            public int getDetailState() {
                return detailState;
            }

            public void setDetailState(int detailState) {
                this.detailState = detailState;
            }

            private String detailStateName;

            public String getDetailStateName() {
                return detailStateName;
            }

            public void setDetailStateName(String detailStateName) {
                this.detailStateName = detailStateName;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public int getIsOverdue() {
                return isOverdue;
            }

            public void setIsOverdue(int isOverdue) {
                this.isOverdue = isOverdue;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getTradeType() {
                return tradeType;
            }

            public void setTradeType(int tradeType) {
                this.tradeType = tradeType;
            }
        }
    }
}
