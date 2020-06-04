package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/21
 * @Description:
 */
public class GiftCardList {


    /**
     * code : 0
     * data : {"serviceCardTemplateList":[{"city":"北京市","selected":0,"templates":[{"serviceCardTypeName":"狂怒2","showTimeStartDesc":"距离开始","discount":0.8,"saleTimeEnd":0,"cityId":0,"isAllSale":"已结束","isShowTime":1,"listPic":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/card/imgs/15548948842029801337.png","showTimeEndDesc":"距离结束","saleTimeStart":0,"faceValue":1000,"isCanSale":0,"shops":"","serviceCardTemplateId":47}]},{"city":"深圳市","templates":[{"serviceCardTypeName":"宠物家服务卡第六版","showTimeStartDesc":"距离开始","discount":0.8,"saleTimeEnd":0,"cityId":2,"isAllSale":"已结束","isShowTime":1,"listPic":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/card/imgs/15548948842029801337.png","showTimeEndDesc":"距离结束","saleTimeStart":0,"faceValue":1000,"isCanSale":0,"shops":"","serviceCardTemplateId":56}]}]}
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
        private List<ServiceCardTemplateListBean> serviceCardTemplateList;

        public List<ServiceCardTemplateListBean> getServiceCardTemplateList() {
            return serviceCardTemplateList;
        }

        public void setServiceCardTemplateList(List<ServiceCardTemplateListBean> serviceCardTemplateList) {
            this.serviceCardTemplateList = serviceCardTemplateList;
        }

        public static class ServiceCardTemplateListBean implements Serializable{
            /**
             * city : 北京市
             * selected : 0
             * templates : [{"serviceCardTypeName":"狂怒2","showTimeStartDesc":"距离开始","discount":0.8,"saleTimeEnd":0,"cityId":0,"isAllSale":"已结束","isShowTime":1,"listPic":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/card/imgs/15548948842029801337.png","showTimeEndDesc":"距离结束","saleTimeStart":0,"faceValue":1000,"isCanSale":0,"shops":"","serviceCardTemplateId":47}]
             */

            private String city;
            private int selected;
            private List<TemplatesBean> templates;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }

            public List<TemplatesBean> getTemplates() {
                return templates;
            }

            public void setTemplates(List<TemplatesBean> templates) {
                this.templates = templates;
            }

            public static class TemplatesBean implements Serializable {
                /**
                 * serviceCardTypeName : 狂怒2
                 * showTimeStartDesc : 距离开始
                 * discount : 0.8
                 * saleTimeEnd : 0
                 * cityId : 0
                 * isAllSale : 已结束
                 * isShowTime : 1
                 * listPic : http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/card/imgs/15548948842029801337.png
                 * showTimeEndDesc : 距离结束
                 * saleTimeStart : 0
                 * faceValue : 1000
                 * isCanSale : 0
                 * shops :
                 * serviceCardTemplateId : 47
                 */

                private String serviceCardTypeName;
                private String showTimeStartDesc;
                private double discount;
                private int saleTimeEnd;
                private int cityId;
                private String isAllSale;
                private int isShowTime;
                private String listPic;
                private String showTimeEndDesc;
                private int saleTimeStart;
                private int faceValue;
                private int isCanSale;
                private String shops;
                private int serviceCardTemplateId;

                public String getServiceCardTypeName() {
                    return serviceCardTypeName;
                }

                public void setServiceCardTypeName(String serviceCardTypeName) {
                    this.serviceCardTypeName = serviceCardTypeName;
                }

                public String getShowTimeStartDesc() {
                    return showTimeStartDesc;
                }

                public void setShowTimeStartDesc(String showTimeStartDesc) {
                    this.showTimeStartDesc = showTimeStartDesc;
                }

                public double getDiscount() {
                    return discount;
                }

                public void setDiscount(double discount) {
                    this.discount = discount;
                }

                public int getSaleTimeEnd() {
                    return saleTimeEnd;
                }

                public void setSaleTimeEnd(int saleTimeEnd) {
                    this.saleTimeEnd = saleTimeEnd;
                }

                public int getCityId() {
                    return cityId;
                }

                public void setCityId(int cityId) {
                    this.cityId = cityId;
                }

                public String getIsAllSale() {
                    return isAllSale;
                }

                public void setIsAllSale(String isAllSale) {
                    this.isAllSale = isAllSale;
                }

                public int getIsShowTime() {
                    return isShowTime;
                }

                public void setIsShowTime(int isShowTime) {
                    this.isShowTime = isShowTime;
                }

                public String getListPic() {
                    return listPic;
                }

                public void setListPic(String listPic) {
                    this.listPic = listPic;
                }

                public String getShowTimeEndDesc() {
                    return showTimeEndDesc;
                }

                public void setShowTimeEndDesc(String showTimeEndDesc) {
                    this.showTimeEndDesc = showTimeEndDesc;
                }

                public int getSaleTimeStart() {
                    return saleTimeStart;
                }

                public void setSaleTimeStart(int saleTimeStart) {
                    this.saleTimeStart = saleTimeStart;
                }

                public int getFaceValue() {
                    return faceValue;
                }

                public void setFaceValue(int faceValue) {
                    this.faceValue = faceValue;
                }

                public int getIsCanSale() {
                    return isCanSale;
                }

                public void setIsCanSale(int isCanSale) {
                    this.isCanSale = isCanSale;
                }

                public String getShops() {
                    return shops;
                }

                public void setShops(String shops) {
                    this.shops = shops;
                }

                public int getServiceCardTemplateId() {
                    return serviceCardTemplateId;
                }

                public void setServiceCardTemplateId(int serviceCardTemplateId) {
                    this.serviceCardTemplateId = serviceCardTemplateId;
                }
            }
        }
    }
}
