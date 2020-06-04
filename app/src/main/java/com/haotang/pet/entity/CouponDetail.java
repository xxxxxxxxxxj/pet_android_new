package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/23
 * @Description:优惠券详情
 */
public class CouponDetail {

    /**
     * code : 0
     * data : {"applyShop":{"ductionList":[],"duction":"北京市全部门店"},"applyBrand":{"ductionList":[],"duction":"适用所有品牌"},"orderId":0,"totalPrice":0,"applyPet":{"ductionList":[],"duction":"适用所有宠物类型"},"applyRoomType":{"ductionList":[],"duction":"适用所有房型"},"isCanGive":0,"description":"啊啊啊啊啊啊啊啊啊","reduceType":1,"canUseServiceCard":0,"source":1,"used":"1970-01-01 01:01:01","cond":"ognl:( payPrice >= 1 ) and reduceType in {1,2,3}","dtyId":0,"exclude":false,"startTime":"2019-12-25","share":{"img":"http://static.ichongwujia.com/static/image/logo/share_logo.jpg","title":"好友分享了一张优惠券给您","url":"http://192.168.0.252/static/content/html5/201806/giftcoupons/giftcoupons.html?couponId=","desc":"%s元优惠券，宠物家-您身边的宠物管家"},"id":191419,"shopId":0,"applyWorker":{"ductionList":[],"duction":"适用所有美容师"},"bindImei":"","serviceCardId":0,"amount":20,"isToExpire":0,"created":"2019-12-25 17:02:25","applyReduceType":{"ductionList":["房费","看护费","节日溢价费"],"duction":"减免所有类型费用"},"condDesc":"满1元可用|减免所有类型费用","userId":55555964,"applyService":{"ductionList":[],"duction":"适用所有服务"},"applyClassification":{"ductionList":[],"duction":"适用所有品类"},"isGive":0,"name":"寄养券1","expireUseSms":0,"typeId":986,"endTime":"2020-04-03","applyCommodity":{"ductionList":[],"duction":"适用所有商品"},"category":7,"isDel":0,"status":0}
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
         * applyShop : {"ductionList":[],"duction":"北京市全部门店"}
         * applyBrand : {"ductionList":[],"duction":"适用所有品牌"}
         * orderId : 0
         * totalPrice : 0
         * applyPet : {"ductionList":[],"duction":"适用所有宠物类型"}
         * applyRoomType : {"ductionList":[],"duction":"适用所有房型"}
         * isCanGive : 0
         * description : 啊啊啊啊啊啊啊啊啊
         * reduceType : 1
         * canUseServiceCard : 0
         * source : 1
         * used : 1970-01-01 01:01:01
         * cond : ognl:( payPrice >= 1 ) and reduceType in {1,2,3}
         * dtyId : 0
         * exclude : false
         * startTime : 2019-12-25
         * share : {"img":"http://static.ichongwujia.com/static/image/logo/share_logo.jpg","title":"好友分享了一张优惠券给您","url":"http://192.168.0.252/static/content/html5/201806/giftcoupons/giftcoupons.html?couponId=","desc":"%s元优惠券，宠物家-您身边的宠物管家"}
         * id : 191419
         * shopId : 0
         * applyWorker : {"ductionList":[],"duction":"适用所有美容师"}
         * bindImei :
         * serviceCardId : 0
         * amount : 20
         * isToExpire : 0
         * created : 2019-12-25 17:02:25
         * applyReduceType : {"ductionList":["房费","看护费","节日溢价费"],"duction":"减免所有类型费用"}
         * condDesc : 满1元可用|减免所有类型费用
         * userId : 55555964
         * applyService : {"ductionList":[],"duction":"适用所有服务"}
         * applyClassification : {"ductionList":[],"duction":"适用所有品类"}
         * isGive : 0
         * name : 寄养券1
         * expireUseSms : 0
         * typeId : 986
         * endTime : 2020-04-03
         * applyCommodity : {"ductionList":[],"duction":"适用所有商品"}
         * category : 7
         * isDel : 0
         * status : 0
         */

        private ApplyShopBean applyShop;
        private ApplyBrandBean applyBrand;
        private int orderId;
        private double totalPrice;
        private ApplyPetBean applyPet;
        private ApplyRoomTypeBean applyRoomType;
        private int isCanGive;
        private String description;
        private int reduceType;
        private int canUseServiceCard;
        private int source;
        private String used;
        private String cond;
        private int dtyId;
        private boolean exclude;
        private String startTime;
        private ShareBean share;
        private int id;
        private int shopId;
        private ApplyWorkerBean applyWorker;
        private String bindImei;
        private int serviceCardId;
        private double amount;
        private int isToExpire;
        private String created;
        private ApplyReduceTypeBean applyReduceType;
        private String condDesc;
        private int userId;
        private ApplyServiceBean applyService;
        private ApplyClassificationBean applyClassification;
        private int isGive;
        private String name;
        private int expireUseSms;
        private int typeId;
        private String endTime;
        private ApplyCommodityBean applyCommodity;
        private int category;
        private int isDel;
        private int status;

        public ApplyShopBean getApplyShop() {
            return applyShop;
        }

        public void setApplyShop(ApplyShopBean applyShop) {
            this.applyShop = applyShop;
        }

        public ApplyBrandBean getApplyBrand() {
            return applyBrand;
        }

        public void setApplyBrand(ApplyBrandBean applyBrand) {
            this.applyBrand = applyBrand;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public ApplyPetBean getApplyPet() {
            return applyPet;
        }

        public void setApplyPet(ApplyPetBean applyPet) {
            this.applyPet = applyPet;
        }

        public ApplyRoomTypeBean getApplyRoomType() {
            return applyRoomType;
        }

        public void setApplyRoomType(ApplyRoomTypeBean applyRoomType) {
            this.applyRoomType = applyRoomType;
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

        public int getCanUseServiceCard() {
            return canUseServiceCard;
        }

        public void setCanUseServiceCard(int canUseServiceCard) {
            this.canUseServiceCard = canUseServiceCard;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getCond() {
            return cond;
        }

        public void setCond(String cond) {
            this.cond = cond;
        }

        public int getDtyId() {
            return dtyId;
        }

        public void setDtyId(int dtyId) {
            this.dtyId = dtyId;
        }

        public boolean isExclude() {
            return exclude;
        }

        public void setExclude(boolean exclude) {
            this.exclude = exclude;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public ApplyWorkerBean getApplyWorker() {
            return applyWorker;
        }

        public void setApplyWorker(ApplyWorkerBean applyWorker) {
            this.applyWorker = applyWorker;
        }

        public String getBindImei() {
            return bindImei;
        }

        public void setBindImei(String bindImei) {
            this.bindImei = bindImei;
        }

        public int getServiceCardId() {
            return serviceCardId;
        }

        public void setServiceCardId(int serviceCardId) {
            this.serviceCardId = serviceCardId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getIsToExpire() {
            return isToExpire;
        }

        public void setIsToExpire(int isToExpire) {
            this.isToExpire = isToExpire;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public ApplyReduceTypeBean getApplyReduceType() {
            return applyReduceType;
        }

        public void setApplyReduceType(ApplyReduceTypeBean applyReduceType) {
            this.applyReduceType = applyReduceType;
        }

        public String getCondDesc() {
            return condDesc;
        }

        public void setCondDesc(String condDesc) {
            this.condDesc = condDesc;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public ApplyServiceBean getApplyService() {
            return applyService;
        }

        public void setApplyService(ApplyServiceBean applyService) {
            this.applyService = applyService;
        }

        public ApplyClassificationBean getApplyClassification() {
            return applyClassification;
        }

        public void setApplyClassification(ApplyClassificationBean applyClassification) {
            this.applyClassification = applyClassification;
        }

        public int getIsGive() {
            return isGive;
        }

        public void setIsGive(int isGive) {
            this.isGive = isGive;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getExpireUseSms() {
            return expireUseSms;
        }

        public void setExpireUseSms(int expireUseSms) {
            this.expireUseSms = expireUseSms;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public ApplyCommodityBean getApplyCommodity() {
            return applyCommodity;
        }

        public void setApplyCommodity(ApplyCommodityBean applyCommodity) {
            this.applyCommodity = applyCommodity;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getIsDel() {
            return isDel;
        }

        public void setIsDel(int isDel) {
            this.isDel = isDel;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class ApplyShopBean {
            /**
             * ductionList : []
             * duction : 北京市全部门店
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ApplyBrandBean {
            /**
             * ductionList : []
             * duction : 适用所有品牌
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ApplyPetBean {
            /**
             * ductionList : []
             * duction : 适用所有宠物类型
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ApplyRoomTypeBean {
            /**
             * ductionList : []
             * duction : 适用所有房型
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ShareBean {
            /**
             * img : http://static.ichongwujia.com/static/image/logo/share_logo.jpg
             * title : 好友分享了一张优惠券给您
             * url : http://192.168.0.252/static/content/html5/201806/giftcoupons/giftcoupons.html?couponId=
             * desc : %s元优惠券，宠物家-您身边的宠物管家
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

        public static class ApplyWorkerBean {
            /**
             * ductionList : []
             * duction : 适用所有美容师
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ApplyReduceTypeBean {
            /**
             * ductionList : ["房费","看护费","节日溢价费"]
             * duction : 减免所有类型费用
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ApplyServiceBean {
            /**
             * ductionList : []
             * duction : 适用所有服务
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ApplyClassificationBean {
            /**
             * ductionList : []
             * duction : 适用所有品类
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }

        public static class ApplyCommodityBean {
            /**
             * ductionList : []
             * duction : 适用所有商品
             */

            private String duction;
            private List<String> ductionList;

            public String getDuction() {
                return duction;
            }

            public void setDuction(String duction) {
                this.duction = duction;
            }

            public List<String> getDuctionList() {
                return ductionList;
            }

            public void setDuctionList(List<String> ductionList) {
                this.ductionList = ductionList;
            }
        }
    }
}
