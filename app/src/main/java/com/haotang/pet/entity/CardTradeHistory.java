package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/25
 * @Description:
 */
public class CardTradeHistory {

    /**
     * code : 0
     * data : {"dataset":[{"date":"2020-02","list":[{"item":"滴滴中级洗护套餐","discountPrice":"优惠¥2000","tradeDate":"2019-09-17 19:59"}]}]}
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
        private List<DatasetBean> dataset;

        public List<DatasetBean> getDataset() {
            return dataset;
        }

        public void setDataset(List<DatasetBean> dataset) {
            this.dataset = dataset;
        }

        public static class DatasetBean {
            /**
             * date : 2020-02
             * list : [{"item":"滴滴中级洗护套餐","discountPrice":"优惠¥2000","tradeDate":"2019-09-17 19:59"}]
             */

            private String date;
            private List<ListBean> list;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * item : 滴滴中级洗护套餐
                 * discountPrice : 优惠¥2000
                 * tradeDate : 2019-09-17 19:59
                 */

                private String item;
                private String discountPrice;
                private String tradeDate;

                public String getItem() {
                    return item;
                }

                public void setItem(String item) {
                    this.item = item;
                }

                public String getDiscountPrice() {
                    return discountPrice;
                }

                public void setDiscountPrice(String discountPrice) {
                    this.discountPrice = discountPrice;
                }

                public String getTradeDate() {
                    return tradeDate;
                }

                public void setTradeDate(String tradeDate) {
                    this.tradeDate = tradeDate;
                }
            }
        }
    }
}
