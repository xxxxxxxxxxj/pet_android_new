package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/23
 * @Description:E卡详情
 */
public class GiftCardDetail {


    /**
     * code : 0
     * msg : 操作成功
     * data : {"isCanSaled":0,"agreement":"","share":{"img":"http://img.cwjia.cn/static/image/logo/logo.jpg","title":"爱宠特色单项服务，您和宠物想要的我们都有！","url":"http://demo.cwjia.cn/static/content/html5/byvue/dist/201809/extralshare/index.html","desc":"您需要的专业刷牙, 修头, 驱虫, 毛发开结, 修屁股, 健齿, 脚垫护理等等！立即预约→"},"cardTypeTemplateList":[{"templateId":12,"faceValue":4500,"salePrice":3000,"saleText":"库存10张"}],"cardTemplateDetail":{"serviceCardTypeId":1,"serviceCardTypeName":"宠物家三周年店庆","subTitle":"副标题","faceValue":1000,"discount":0.8,"salePrice":700,"saleBaseCount":10,"expireTime":2,"isFirst":0,"shops":"1,2,4,5","tagType":"1,2,4","typeContentList":[{"pic":"http:///ccc.jpg","tagContent":"限量购买","tagText":"限购2张"}],"stock":0,"saleTimeStart":"2018-01-03 00:00:00","saleTimeEnd":"2019-02-11 00:00:00","restrictAmount":2,"rewardType":0,"reward":"52154,52154","banner":"http://xx.jpg","listPic":"http://xx.jpg","smallPic":"http://xx.jpg","mineCardPic":"http://xx.jpg","detailPic":"http://xx.jpg","useExplain":"","sort":0,"state":0,"shelvesTime":"2019-10-19 00:00:00","id":10},"shopList":["（宠物家） 清河店","（ 宠物家） 常营店"],"shopText":"清河店可用","saleText":"库存10张","couponList":[{"couponId":213,"name":"特色服务免单卷","amount":2}],"couponTypeList":[{"id":2124656,"amount":120,"created":"2018-07-09 11:39:09","isCanGive":0,"description":"游泳可用（犬证套餐卷）","reduceType":1,"pageSize":10,"typeId":1,"couponName":"游泳专用卷","startTime":"2018-07-09","endTime":"2019-07-09","duration":1}]}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * isCanSaled : 0
         * agreement :
         * share : {"img":"http://img.cwjia.cn/static/image/logo/logo.jpg","title":"爱宠特色单项服务，您和宠物想要的我们都有！","url":"http://demo.cwjia.cn/static/content/html5/byvue/dist/201809/extralshare/index.html","desc":"您需要的专业刷牙, 修头, 驱虫, 毛发开结, 修屁股, 健齿, 脚垫护理等等！立即预约→"}
         * cardTypeTemplateList : [{"templateId":12,"faceValue":4500,"salePrice":3000,"saleText":"库存10张"}]
         * cardTemplateDetail : {"serviceCardTypeId":1,"serviceCardTypeName":"宠物家三周年店庆","subTitle":"副标题","faceValue":1000,"discount":0.8,"salePrice":700,"saleBaseCount":10,"expireTime":2,"isFirst":0,"shops":"1,2,4,5","tagType":"1,2,4","typeContentList":[{"pic":"http:///ccc.jpg","tagContent":"限量购买","tagText":"限购2张"}],"stock":0,"saleTimeStart":"2018-01-03 00:00:00","saleTimeEnd":"2019-02-11 00:00:00","restrictAmount":2,"rewardType":0,"reward":"52154,52154","banner":"http://xx.jpg","listPic":"http://xx.jpg","smallPic":"http://xx.jpg","mineCardPic":"http://xx.jpg","detailPic":"http://xx.jpg","useExplain":"","sort":0,"state":0,"shelvesTime":"2019-10-19 00:00:00","id":10}
         * shopList : ["（宠物家） 清河店","（ 宠物家） 常营店"]
         * shopText : 清河店可用
         * saleText : 库存10张
         * couponList : [{"couponId":213,"name":"特色服务免单卷","amount":2}]
         * couponTypeList : [{"id":2124656,"amount":120,"created":"2018-07-09 11:39:09","isCanGive":0,"description":"游泳可用（犬证套餐卷）","reduceType":1,"pageSize":10,"typeId":1,"couponName":"游泳专用卷","startTime":"2018-07-09","endTime":"2019-07-09","duration":1}]
         */

        private int isCanSaled;
        private String saleProcessUrl;
        private String saleDesc;

        public String getSaleDesc() {
            return saleDesc;
        }

        public void setSaleDesc(String saleDesc) {
            this.saleDesc = saleDesc;
        }

        public String getSaleProcessUrl() {
            return saleProcessUrl;
        }

        public void setSaleProcessUrl(String saleProcessUrl) {
            this.saleProcessUrl = saleProcessUrl;
        }

        private String agreement;
        private ShareBean share;
        private CardTemplateDetailBean cardTemplateDetail;
        private String shopText;
        private String saleText;
        private List<CardTypeTemplateListBean> cardTypeTemplateList;
        private List<String> shopList;
        private List<String> tagNames;

        public List<String> getTagNames() {
            return tagNames;
        }

        public void setTagNames(List<String> tagNames) {
            this.tagNames = tagNames;
        }

        private List<CouponListBean> couponList;
        private List<CouponTypeListBean> couponTypeList;

        public int getIsCanSaled() {
            return isCanSaled;
        }

        public void setIsCanSaled(int isCanSaled) {
            this.isCanSaled = isCanSaled;
        }

        public String getAgreement() {
            return agreement;
        }

        public void setAgreement(String agreement) {
            this.agreement = agreement;
        }

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public CardTemplateDetailBean getCardTemplateDetail() {
            return cardTemplateDetail;
        }

        public void setCardTemplateDetail(CardTemplateDetailBean cardTemplateDetail) {
            this.cardTemplateDetail = cardTemplateDetail;
        }

        public String getShopText() {
            return shopText;
        }

        public void setShopText(String shopText) {
            this.shopText = shopText;
        }

        public String getSaleText() {
            return saleText;
        }

        public void setSaleText(String saleText) {
            this.saleText = saleText;
        }

        public List<CardTypeTemplateListBean> getCardTypeTemplateList() {
            return cardTypeTemplateList;
        }

        public void setCardTypeTemplateList(List<CardTypeTemplateListBean> cardTypeTemplateList) {
            this.cardTypeTemplateList = cardTypeTemplateList;
        }

        public List<String> getShopList() {
            return shopList;
        }

        public void setShopList(List<String> shopList) {
            this.shopList = shopList;
        }

        public List<CouponListBean> getCouponList() {
            return couponList;
        }

        public void setCouponList(List<CouponListBean> couponList) {
            this.couponList = couponList;
        }

        public List<CouponTypeListBean> getCouponTypeList() {
            return couponTypeList;
        }

        public void setCouponTypeList(List<CouponTypeListBean> couponTypeList) {
            this.couponTypeList = couponTypeList;
        }

        public static class ShareBean {
            /**
             * img : http://img.cwjia.cn/static/image/logo/logo.jpg
             * title : 爱宠特色单项服务，您和宠物想要的我们都有！
             * url : http://demo.cwjia.cn/static/content/html5/byvue/dist/201809/extralshare/index.html
             * desc : 您需要的专业刷牙, 修头, 驱虫, 毛发开结, 修屁股, 健齿, 脚垫护理等等！立即预约→
             */

            private String img;
            private String title;
            private String url;
            private String desc;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }

        public static class CardTemplateDetailBean {
            /**
             * serviceCardTypeId : 1
             * serviceCardTypeName : 宠物家三周年店庆
             * subTitle : 副标题
             * faceValue : 1000
             * discount : 0.8
             * salePrice : 700
             * saleBaseCount : 10
             * expireTime : 2
             * isFirst : 0
             * shops : 1,2,4,5
             * tagType : 1,2,4
             * typeContentList : [{"pic":"http:///ccc.jpg","tagContent":"限量购买","tagText":"限购2张"}]
             * stock : 0
             * saleTimeStart : 2018-01-03 00:00:00
             * saleTimeEnd : 2019-02-11 00:00:00
             * restrictAmount : 2
             * rewardType : 0
             * reward : 52154,52154
             * banner : http://xx.jpg
             * listPic : http://xx.jpg
             * smallPic : http://xx.jpg
             * mineCardPic : http://xx.jpg
             * detailPic : http://xx.jpg
             * useExplain :
             * sort : 0
             * state : 0
             * shelvesTime : 2019-10-19 00:00:00
             * id : 10
             */

            private int serviceCardTypeId;
            private String serviceCardTypeName;
            private String subTitle;
            private String faceValue;
            private double discount;
            private String salePrice;
            private int saleBaseCount;
            private int expireTime;
            private int isFirst;
            private int isShowTime;
            private String shops;
            private String tagType;
            private int stock;
            private long saleTimeStart;
            private long saleTimeEnd;
            private int restrictAmount;
            private int rewardType;
            private String reward;
            private String banner;
            private String listPic;
            private String smallPic;
            private String mineCardPic;
            private String detailPic;
            private String useExplain;
            private int sort;
            private int state;
            private String shelvesTime;
            private int id;
            private String discountDesc;
            private List<String> discountDescNew;
            private List<String> discountDescOrder;

            public List<String> getDiscountDescOrder() {
                return discountDescOrder;
            }

            public void setDiscountDescOrder(List<String> discountDescOrder) {
                this.discountDescOrder = discountDescOrder;
            }

            public List<String> getDiscountDescNew() {
                return discountDescNew;
            }

            public void setDiscountDescNew(List<String> discountDescNew) {
                this.discountDescNew = discountDescNew;
            }

            public String getDiscountDesc() {
                return discountDesc;
            }

            public int getIsShowTime() {
                return isShowTime;
            }

            public void setIsShowTime(int isShowTime) {
                this.isShowTime = isShowTime;
            }

            public void setDiscountDesc(String discountDesc) {
                this.discountDesc = discountDesc;
            }

            private List<TypeContentListBean> typeContentList;

            public int getServiceCardTypeId() {
                return serviceCardTypeId;
            }

            public void setServiceCardTypeId(int serviceCardTypeId) {
                this.serviceCardTypeId = serviceCardTypeId;
            }

            public String getServiceCardTypeName() {
                return serviceCardTypeName;
            }

            public void setServiceCardTypeName(String serviceCardTypeName) {
                this.serviceCardTypeName = serviceCardTypeName;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getFaceValue() {
                return faceValue;
            }

            public void setFaceValue(String faceValue) {
                this.faceValue = faceValue;
            }

            public double getDiscount() {
                return discount;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public String getSalePrice() {
                return salePrice;
            }

            public void setSalePrice(String salePrice) {
                this.salePrice = salePrice;
            }

            public int getSaleBaseCount() {
                return saleBaseCount;
            }

            public void setSaleBaseCount(int saleBaseCount) {
                this.saleBaseCount = saleBaseCount;
            }

            public int getExpireTime() {
                return expireTime;
            }

            public void setExpireTime(int expireTime) {
                this.expireTime = expireTime;
            }

            public int getIsFirst() {
                return isFirst;
            }

            public void setIsFirst(int isFirst) {
                this.isFirst = isFirst;
            }

            public String getShops() {
                return shops;
            }

            public void setShops(String shops) {
                this.shops = shops;
            }

            public String getTagType() {
                return tagType;
            }

            public void setTagType(String tagType) {
                this.tagType = tagType;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public long getSaleTimeStart() {
                return saleTimeStart;
            }

            public void setSaleTimeStart(long saleTimeStart) {
                this.saleTimeStart = saleTimeStart;
            }

            public long getSaleTimeEnd() {
                return saleTimeEnd;
            }

            public void setSaleTimeEnd(long saleTimeEnd) {
                this.saleTimeEnd = saleTimeEnd;
            }

            public int getRestrictAmount() {
                return restrictAmount;
            }

            public void setRestrictAmount(int restrictAmount) {
                this.restrictAmount = restrictAmount;
            }

            public int getRewardType() {
                return rewardType;
            }

            public void setRewardType(int rewardType) {
                this.rewardType = rewardType;
            }

            public String getReward() {
                return reward;
            }

            public void setReward(String reward) {
                this.reward = reward;
            }

            public String getBanner() {
                return banner;
            }

            public void setBanner(String banner) {
                this.banner = banner;
            }

            public String getListPic() {
                return listPic;
            }

            public void setListPic(String listPic) {
                this.listPic = listPic;
            }

            public String getSmallPic() {
                return smallPic;
            }

            public void setSmallPic(String smallPic) {
                this.smallPic = smallPic;
            }

            public String getMineCardPic() {
                return mineCardPic;
            }

            public void setMineCardPic(String mineCardPic) {
                this.mineCardPic = mineCardPic;
            }

            public String getDetailPic() {
                return detailPic;
            }

            public void setDetailPic(String detailPic) {
                this.detailPic = detailPic;
            }

            public String getUseExplain() {
                return useExplain;
            }

            public void setUseExplain(String useExplain) {
                this.useExplain = useExplain;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getShelvesTime() {
                return shelvesTime;
            }

            public void setShelvesTime(String shelvesTime) {
                this.shelvesTime = shelvesTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public List<TypeContentListBean> getTypeContentList() {
                return typeContentList;
            }

            public void setTypeContentList(List<TypeContentListBean> typeContentList) {
                this.typeContentList = typeContentList;
            }

            public static class TypeContentListBean {
                /**
                 * pic : http:///ccc.jpg
                 * tagContent : 限量购买
                 * tagText : 限购2张
                 */

                private String pic;
                private String tagContent;
                private String tagText;

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getTagContent() {
                    return tagContent;
                }

                public void setTagContent(String tagContent) {
                    this.tagContent = tagContent;
                }

                public String getTagText() {
                    return tagText;
                }

                public void setTagText(String tagText) {
                    this.tagText = tagText;
                }
            }
        }

        public static class CardTypeTemplateListBean {
            /**
             * templateId : 12
             * faceValue : 4500
             * salePrice : 3000
             * saleText : 库存10张
             */

            private int templateId;
            private String smallPic;
            private String faceValue;
            private String salePrice;
            private String saleText;
            private boolean isCheck;
            private int restrictAmount;
            private String saleDesc;

            public String getSaleDesc() {
                return saleDesc;
            }

            public void setSaleDesc(String saleDesc) {
                this.saleDesc = saleDesc;
            }

            public int getRestrictAmount() {
                return restrictAmount;
            }

            public void setRestrictAmount(int restrictAmount) {
                this.restrictAmount = restrictAmount;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getTemplateId() {
                return templateId;
            }

            public void setTemplateId(int templateId) {
                this.templateId = templateId;
            }

            public String getSmallPic() {
                return smallPic;
            }

            public void setSmallPic(String smallPic) {
                this.smallPic = smallPic;
            }

            public String getFaceValue() {
                return faceValue;
            }

            public void setFaceValue(String faceValue) {
                this.faceValue = faceValue;
            }

            public String getSalePrice() {
                return salePrice;
            }

            public void setSalePrice(String salePrice) {
                this.salePrice = salePrice;
            }

            public String getSaleText() {
                return saleText;
            }

            public void setSaleText(String saleText) {
                this.saleText = saleText;
            }
        }

        public static class CouponListBean implements Serializable{
            /**
             * couponId : 213
             * name : 特色服务免单卷
             * amount : 2
             */

            private int couponId;
            private String name;
            private int amount;
            private int freeNum;

            public int getFreeNum() {
                return freeNum;
            }

            public void setFreeNum(int freeNum) {
                this.freeNum = freeNum;
            }

            public int getCouponId() {
                return couponId;
            }

            public void setCouponId(int couponId) {
                this.couponId = couponId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }
        }

        public static class CouponTypeListBean {
            /**
             * id : 2124656
             * amount : 120
             * created : 2018-07-09 11:39:09
             * isCanGive : 0
             * description : 游泳可用（犬证套餐卷）
             * reduceType : 1
             * pageSize : 10
             * typeId : 1
             * couponName : 游泳专用卷
             * startTime : 2018-07-09
             * endTime : 2019-07-09
             * duration : 1
             */

            private int id;
            private double amount;
            private String created;
            private int isCanGive;
            private String description;
            private int reduceType;
            private int pageSize;
            private int typeId;
            private String least;

            public String getLeast() {
                return least;
            }

            public void setLeast(String least) {
                this.least = least;
            }

            private String couponName;
            private String startTime;
            private String endTime;
            private Boolean isShow;

            public Boolean getShow() {
                return isShow;
            }

            public void setShow(Boolean show) {
                isShow = show;
            }

            private int duration;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public int getIsCanGive() {
                return isCanGive;
            }

            public void setIsCanGive(int isCanGive) {
                this.isCanGive = isCanGive;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getReduceType() {
                return reduceType;
            }

            public void setReduceType(int reduceType) {
                this.reduceType = reduceType;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getTypeId() {
                return typeId;
            }

            public void setTypeId(int typeId) {
                this.typeId = typeId;
            }

            public String getCouponName() {
                return couponName;
            }

            public void setCouponName(String couponName) {
                this.couponName = couponName;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }
        }
    }
}
