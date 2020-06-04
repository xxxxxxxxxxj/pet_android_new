package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/29
 * @Description:
 */
public class GiftCardOrder {

    /**
     * code : 0
     * msg : 操作成功
     * data : [{"id":12,"templateId":12,"serviceCardTypeName":"宠物家五周年限量卡","discountText":"洗美7折","tagContent1":"全品类通用","amount":2189,"status":0,"smallPic":"http://ttt.jpg"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 12
         * templateId : 12
         * serviceCardTypeName : 宠物家五周年限量卡
         * discountText : 洗美7折
         * tagContent1 : 全品类通用
         * amount : 2189
         * status : 0
         * smallPic : http://ttt.jpg
         */

        private int id;
        private int templateId;
        private String serviceCardTypeName;
        private String discountText;
        private String tagContent1;
        private double amount;
        private int status;
        private String smallPic;
        private int cardType;
        private int bindPetId;
        private List<String> dicountDesc;
        private List<String> tagNames;

        public int getBindPetId() {
            return bindPetId;
        }

        public void setBindPetId(int bindPetId) {
            this.bindPetId = bindPetId;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        public List<String> getTagNames() {
            return tagNames;
        }

        public void setTagNames(List<String> tagNames) {
            this.tagNames = tagNames;
        }

        public List<String> getDicountDesc() {
            return dicountDesc;
        }

        public void setDicountDesc(List<String> dicountDesc) {
            this.dicountDesc = dicountDesc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public String getServiceCardTypeName() {
            return serviceCardTypeName;
        }

        public void setServiceCardTypeName(String serviceCardTypeName) {
            this.serviceCardTypeName = serviceCardTypeName;
        }

        public String getDiscountText() {
            return discountText;
        }

        public void setDiscountText(String discountText) {
            this.discountText = discountText;
        }

        public String getTagContent1() {
            return tagContent1;
        }

        public void setTagContent1(String tagContent1) {
            this.tagContent1 = tagContent1;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSmallPic() {
            return smallPic;
        }

        public void setSmallPic(String smallPic) {
            this.smallPic = smallPic;
        }
    }
}
