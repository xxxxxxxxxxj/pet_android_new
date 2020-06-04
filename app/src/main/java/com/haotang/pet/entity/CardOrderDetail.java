package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/28
 * @Description:E卡订单详情
 */
public class CardOrderDetail {

    /**
     * code : 0
     * msg : 操作成功
     * data : {"serviceCard":{"serviceCardId":12,"cardNumber":"12132132","cardPassword":"231fsd","faceValue":1000,"payPrice":237,"useAmount":12,"discountAmount":111,"tradeNo":"21321","outTradeNo":"2413","payTime":"2019-10-01 21:24:21","payWay":1,"bindUserId":123},"discountText":"洗美7折","tagContent1":"全品类通用","shopList":[],"shopText":"清河店可用","couponList":[{"couponId":213,"name":"特色服务免单卷(1张)","amount":2}],"phone":"售后电话：4000300011","serviceCardTypeName":"宠物家三周年纪念卡","smallPic":"http://ccc.jpg","status":2,"refundDetail":[{"name":"提交成功","time":"2019-10-11 00:00:00","desc":"耐心等候"}],"refundRule":{"refundable":0,"reason":"由于您宠物家至尊卡已被您转赠189XXXX8899本卡不可申请退款","refund_tips":["1、可用于宠物家洗澡、美容、特色服务，预约服务享受9折优惠","2、充值赠送800元和1kg巅峰狗粮","3、折扣有效期为三年，过期后折扣取消，金额扔可消费","4、客服热线：400-0300-011","5、可用于宠物家洗澡、美容、特色服务，预约服务享受9折优惠","6、充值赠送800元和1kg巅峰狗粮"]}}
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
         * serviceCard : {"serviceCardId":12,"cardNumber":"12132132","cardPassword":"231fsd","faceValue":1000,"payPrice":237,"useAmount":12,"discountAmount":111,"tradeNo":"21321","outTradeNo":"2413","payTime":"2019-10-01 21:24:21","payWay":1,"bindUserId":123}
         * discountText : 洗美7折
         * tagContent1 : 全品类通用
         * shopList : []
         * shopText : 清河店可用
         * couponList : [{"couponId":213,"name":"特色服务免单卷(1张)","amount":2}]
         * phone : 售后电话：4000300011
         * serviceCardTypeName : 宠物家三周年纪念卡
         * smallPic : http://ccc.jpg
         * status : 2
         * refundDetail : [{"name":"提交成功","time":"2019-10-11 00:00:00","desc":"耐心等候"}]
         * refundRule : {"refundable":0,"reason":"由于您宠物家至尊卡已被您转赠189XXXX8899本卡不可申请退款","refund_tips":["1、可用于宠物家洗澡、美容、特色服务，预约服务享受9折优惠","2、充值赠送800元和1kg巅峰狗粮","3、折扣有效期为三年，过期后折扣取消，金额扔可消费","4、客服热线：400-0300-011","5、可用于宠物家洗澡、美容、特色服务，预约服务享受9折优惠","6、充值赠送800元和1kg巅峰狗粮"]}
         */

        private ServiceCardBean serviceCard;
        private String discountText;
        private String tagContent1;
        private String shopText;
        private String phone;
        private String serviceCardTypeName;
        private String smallPic;
        private int status;
        private RefundRuleBean refundRule;
        private List<String> shopList;
        private List<CouponListBean> couponList;
        private List<RefundDetailBean> refundDetail;

        public ServiceCardBean getServiceCard() {
            return serviceCard;
        }

        public void setServiceCard(ServiceCardBean serviceCard) {
            this.serviceCard = serviceCard;
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

        public String getShopText() {
            return shopText;
        }

        public void setShopText(String shopText) {
            this.shopText = shopText;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getServiceCardTypeName() {
            return serviceCardTypeName;
        }

        public void setServiceCardTypeName(String serviceCardTypeName) {
            this.serviceCardTypeName = serviceCardTypeName;
        }

        public String getSmallPic() {
            return smallPic;
        }

        public void setSmallPic(String smallPic) {
            this.smallPic = smallPic;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public RefundRuleBean getRefundRule() {
            return refundRule;
        }

        public void setRefundRule(RefundRuleBean refundRule) {
            this.refundRule = refundRule;
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

        public List<RefundDetailBean> getRefundDetail() {
            return refundDetail;
        }

        public void setRefundDetail(List<RefundDetailBean> refundDetail) {
            this.refundDetail = refundDetail;
        }

        public static class ServiceCardBean {
            /**
             * serviceCardId : 12
             * cardNumber : 12132132
             * cardPassword : 231fsd
             * faceValue : 1000
             * payPrice : 237
             * useAmount : 12
             * discountAmount : 111
             * tradeNo : 21321
             * outTradeNo : 2413
             * payTime : 2019-10-01 21:24:21
             * payWay : 1
             * bindUserId : 123
             */

            private int serviceCardId;
            private String cardNumber;
            private String cardPassword;
            private String expireTime;
            private CouponInfoBean couponInfo;
            private double serviceChargePrice;
            private double couponDiscountPrice;
            private double refundAmount;

            public double getRefundAmount() {
                return refundAmount;
            }

            public void setRefundAmount(double refundAmount) {
                this.refundAmount = refundAmount;
            }

            private List<String> dicountDesc;

            public CouponInfoBean getCouponInfo() {
                return couponInfo;
            }

            public void setCouponInfo(CouponInfoBean couponInfo) {
                this.couponInfo = couponInfo;
            }

            public double getServiceChargePrice() {
                return serviceChargePrice;
            }

            public void setServiceChargePrice(double serviceChargePrice) {
                this.serviceChargePrice = serviceChargePrice;
            }

            public double getCouponDiscountPrice() {
                return couponDiscountPrice;
            }

            public void setCouponDiscountPrice(double couponDiscountPrice) {
                this.couponDiscountPrice = couponDiscountPrice;
            }

            public List<String> getDicountDesc() {
                return dicountDesc;
            }

            public void setDicountDesc(List<String> dicountDesc) {
                this.dicountDesc = dicountDesc;
            }

            public String getExpireTime() {
                return expireTime;
            }

            public void setExpireTime(String expireTime) {
                this.expireTime = expireTime;
            }

            private double faceValue;
            private double payPrice;
            private double useAmount;
            private double discountAmount;
            private String tradeNo;
            private String outTradeNo;
            private String payTime;
            private int payWay;
            private int bindUserId;

            public int getServiceCardId() {
                return serviceCardId;
            }

            public void setServiceCardId(int serviceCardId) {
                this.serviceCardId = serviceCardId;
            }

            public String getCardNumber() {
                return cardNumber;
            }

            public void setCardNumber(String cardNumber) {
                this.cardNumber = cardNumber;
            }

            public String getCardPassword() {
                return cardPassword;
            }

            public void setCardPassword(String cardPassword) {
                this.cardPassword = cardPassword;
            }

            public double getFaceValue() {
                return faceValue;
            }

            public void setFaceValue(double faceValue) {
                this.faceValue = faceValue;
            }

            public double getPayPrice() {
                return payPrice;
            }

            public void setPayPrice(double payPrice) {
                this.payPrice = payPrice;
            }

            public double getUseAmount() {
                return useAmount;
            }

            public void setUseAmount(double useAmount) {
                this.useAmount = useAmount;
            }

            public double getDiscountAmount() {
                return discountAmount;
            }

            public void setDiscountAmount(double discountAmount) {
                this.discountAmount = discountAmount;
            }

            public String getTradeNo() {
                return tradeNo;
            }

            public void setTradeNo(String tradeNo) {
                this.tradeNo = tradeNo;
            }

            public String getOutTradeNo() {
                return outTradeNo;
            }

            public void setOutTradeNo(String outTradeNo) {
                this.outTradeNo = outTradeNo;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public int getPayWay() {
                return payWay;
            }

            public void setPayWay(int payWay) {
                this.payWay = payWay;
            }

            public int getBindUserId() {
                return bindUserId;
            }

            public void setBindUserId(int bindUserId) {
                this.bindUserId = bindUserId;
            }
            public static class CouponInfoBean {
                /**
                 * couponNum : 0
                 * totalAmount : 0
                 * usedNum : 0
                 * unUsedList : [{"amount":30,"bindImei":"","canUseServiceCard":0,"category":1,"cond":"ognl:type == 1 and petService >= 5","condDesc":"","created":"2016-04-21 15:21:12","description":"到店","discountPrice":0,"dtyId":0,"endTime":"2016-04-27 23:59:59","exclude":false,"expireUseSms":0,"extendParam":{},"id":180388,"isCanGive":0,"isDel":0,"isGive":0,"name":"SPA套餐券","orderId":0,"reduceType":1,"serviceCardId":6,"shopId":0,"source":1,"startTime":"2016-04-21 15:21:12","status":3,"totalPrice":0,"typeId":113,"used":"1970-01-01 01:01:01","userId":55555628}]
                 * usedList : [{"amount":30,"bindImei":"","canUseServiceCard":0,"category":1,"cond":"ognl:type == 1 and petService >= 5","condDesc":"","created":"2016-04-21 15:21:12","description":"到店","discountPrice":0,"dtyId":0,"endTime":"2016-04-27 23:59:59","exclude":false,"expireUseSms":0,"extendParam":{},"id":180388,"isCanGive":0,"isDel":0,"isGive":0,"name":"SPA套餐券","orderId":0,"reduceType":1,"serviceCardId":6,"shopId":0,"source":1,"startTime":"2016-04-21 15:21:12","status":3,"totalPrice":0,"typeId":113,"used":"1970-01-01 01:01:01","userId":55555628}]
                 */

                private int couponNum;
                private double totalAmount;
                private int usedNum;
                private List<UnUsedListBean> unUsedList;
                private List<UsedListBean> usedList;

                public int getCouponNum() {
                    return couponNum;
                }

                public void setCouponNum(int couponNum) {
                    this.couponNum = couponNum;
                }

                public double getTotalAmount() {
                    return totalAmount;
                }

                public void setTotalAmount(double totalAmount) {
                    this.totalAmount = totalAmount;
                }

                public int getUsedNum() {
                    return usedNum;
                }

                public void setUsedNum(int usedNum) {
                    this.usedNum = usedNum;
                }

                public List<UnUsedListBean> getUnUsedList() {
                    return unUsedList;
                }

                public void setUnUsedList(List<UnUsedListBean> unUsedList) {
                    this.unUsedList = unUsedList;
                }

                public List<UsedListBean> getUsedList() {
                    return usedList;
                }

                public void setUsedList(List<UsedListBean> usedList) {
                    this.usedList = usedList;
                }

                public static class UnUsedListBean {
                    /**
                     * amount : 30
                     * bindImei :
                     * canUseServiceCard : 0
                     * category : 1
                     * cond : ognl:type == 1 and petService >= 5
                     * condDesc :
                     * created : 2016-04-21 15:21:12
                     * description : 到店
                     * discountPrice : 0
                     * dtyId : 0
                     * endTime : 2016-04-27 23:59:59
                     * exclude : false
                     * expireUseSms : 0
                     * extendParam : {}
                     * id : 180388
                     * isCanGive : 0
                     * isDel : 0
                     * isGive : 0
                     * name : SPA套餐券
                     * orderId : 0
                     * reduceType : 1
                     * serviceCardId : 6
                     * shopId : 0
                     * source : 1
                     * startTime : 2016-04-21 15:21:12
                     * status : 3
                     * totalPrice : 0
                     * typeId : 113
                     * used : 1970-01-01 01:01:01
                     * userId : 55555628
                     */

                    private double amount;
                    private String bindImei;
                    private int canUseServiceCard;
                    private int category;
                    private String cond;
                    private String condDesc;
                    private String created;
                    private String description;
                    private double discountPrice;
                    private int dtyId;
                    private String endTime;
                    private boolean exclude;
                    private int expireUseSms;
                    private ExtendParamBean extendParam;
                    private int id;
                    private int isCanGive;
                    private int isDel;
                    private int isGive;
                    private String name;
                    private int orderId;
                    private int reduceType;
                    private int serviceCardId;
                    private int shopId;
                    private int source;
                    private String startTime;
                    private int status;
                    private double totalPrice;
                    private int typeId;
                    private String used;
                    private int userId;
                    private String least;

                    public String getLeast() {
                        return least;
                    }

                    public void setLeast(String least) {
                        this.least = least;
                    }

                    public double getAmount() {
                        return amount;
                    }

                    public void setAmount(double amount) {
                        this.amount = amount;
                    }

                    public String getBindImei() {
                        return bindImei;
                    }

                    public void setBindImei(String bindImei) {
                        this.bindImei = bindImei;
                    }

                    public int getCanUseServiceCard() {
                        return canUseServiceCard;
                    }

                    public void setCanUseServiceCard(int canUseServiceCard) {
                        this.canUseServiceCard = canUseServiceCard;
                    }

                    public int getCategory() {
                        return category;
                    }

                    public void setCategory(int category) {
                        this.category = category;
                    }

                    public String getCond() {
                        return cond;
                    }

                    public void setCond(String cond) {
                        this.cond = cond;
                    }

                    public String getCondDesc() {
                        return condDesc;
                    }

                    public void setCondDesc(String condDesc) {
                        this.condDesc = condDesc;
                    }

                    public String getCreated() {
                        return created;
                    }

                    public void setCreated(String created) {
                        this.created = created;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }

                    public double getDiscountPrice() {
                        return discountPrice;
                    }

                    public void setDiscountPrice(double discountPrice) {
                        this.discountPrice = discountPrice;
                    }

                    public int getDtyId() {
                        return dtyId;
                    }

                    public void setDtyId(int dtyId) {
                        this.dtyId = dtyId;
                    }

                    public String getEndTime() {
                        return endTime;
                    }

                    public void setEndTime(String endTime) {
                        this.endTime = endTime;
                    }

                    public boolean isExclude() {
                        return exclude;
                    }

                    public void setExclude(boolean exclude) {
                        this.exclude = exclude;
                    }

                    public int getExpireUseSms() {
                        return expireUseSms;
                    }

                    public void setExpireUseSms(int expireUseSms) {
                        this.expireUseSms = expireUseSms;
                    }

                    public ExtendParamBean getExtendParam() {
                        return extendParam;
                    }

                    public void setExtendParam(ExtendParamBean extendParam) {
                        this.extendParam = extendParam;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getIsCanGive() {
                        return isCanGive;
                    }

                    public void setIsCanGive(int isCanGive) {
                        this.isCanGive = isCanGive;
                    }

                    public int getIsDel() {
                        return isDel;
                    }

                    public void setIsDel(int isDel) {
                        this.isDel = isDel;
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

                    public int getOrderId() {
                        return orderId;
                    }

                    public void setOrderId(int orderId) {
                        this.orderId = orderId;
                    }

                    public int getReduceType() {
                        return reduceType;
                    }

                    public void setReduceType(int reduceType) {
                        this.reduceType = reduceType;
                    }

                    public int getServiceCardId() {
                        return serviceCardId;
                    }

                    public void setServiceCardId(int serviceCardId) {
                        this.serviceCardId = serviceCardId;
                    }

                    public int getShopId() {
                        return shopId;
                    }

                    public void setShopId(int shopId) {
                        this.shopId = shopId;
                    }

                    public int getSource() {
                        return source;
                    }

                    public void setSource(int source) {
                        this.source = source;
                    }

                    public String getStartTime() {
                        return startTime;
                    }

                    public void setStartTime(String startTime) {
                        this.startTime = startTime;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public double getTotalPrice() {
                        return totalPrice;
                    }

                    public void setTotalPrice(double totalPrice) {
                        this.totalPrice = totalPrice;
                    }

                    public int getTypeId() {
                        return typeId;
                    }

                    public void setTypeId(int typeId) {
                        this.typeId = typeId;
                    }

                    public String getUsed() {
                        return used;
                    }

                    public void setUsed(String used) {
                        this.used = used;
                    }

                    public int getUserId() {
                        return userId;
                    }

                    public void setUserId(int userId) {
                        this.userId = userId;
                    }

                    public static class ExtendParamBean {
                    }
                }

                public static class UsedListBean {
                    /**
                     * amount : 30
                     * bindImei :
                     * canUseServiceCard : 0
                     * category : 1
                     * cond : ognl:type == 1 and petService >= 5
                     * condDesc :
                     * created : 2016-04-21 15:21:12
                     * description : 到店
                     * discountPrice : 0
                     * dtyId : 0
                     * endTime : 2016-04-27 23:59:59
                     * exclude : false
                     * expireUseSms : 0
                     * extendParam : {}
                     * id : 180388
                     * isCanGive : 0
                     * isDel : 0
                     * isGive : 0
                     * name : SPA套餐券
                     * orderId : 0
                     * reduceType : 1
                     * serviceCardId : 6
                     * shopId : 0
                     * source : 1
                     * startTime : 2016-04-21 15:21:12
                     * status : 3
                     * totalPrice : 0
                     * typeId : 113
                     * used : 1970-01-01 01:01:01
                     * userId : 55555628
                     */

                    private double amount;
                    private String bindImei;
                    private int canUseServiceCard;
                    private int category;
                    private String cond;
                    private String condDesc;
                    private String created;
                    private String description;
                    private double discountPrice;
                    private int dtyId;
                    private String endTime;
                    private boolean exclude;
                    private int expireUseSms;
                    private ExtendParamBeanX extendParam;
                    private int id;
                    private int isCanGive;
                    private int isDel;
                    private int isGive;
                    private String name;
                    private int orderId;
                    private int reduceType;
                    private int serviceCardId;
                    private int shopId;
                    private int source;
                    private String startTime;
                    private int status;
                    private double totalPrice;
                    private int typeId;
                    private String used;
                    private int userId;
                    private String least;

                    public String getLeast() {
                        return least;
                    }

                    public void setLeast(String least) {
                        this.least = least;
                    }

                    public double getAmount() {
                        return amount;
                    }

                    public void setAmount(double amount) {
                        this.amount = amount;
                    }

                    public String getBindImei() {
                        return bindImei;
                    }

                    public void setBindImei(String bindImei) {
                        this.bindImei = bindImei;
                    }

                    public int getCanUseServiceCard() {
                        return canUseServiceCard;
                    }

                    public void setCanUseServiceCard(int canUseServiceCard) {
                        this.canUseServiceCard = canUseServiceCard;
                    }

                    public int getCategory() {
                        return category;
                    }

                    public void setCategory(int category) {
                        this.category = category;
                    }

                    public String getCond() {
                        return cond;
                    }

                    public void setCond(String cond) {
                        this.cond = cond;
                    }

                    public String getCondDesc() {
                        return condDesc;
                    }

                    public void setCondDesc(String condDesc) {
                        this.condDesc = condDesc;
                    }

                    public String getCreated() {
                        return created;
                    }

                    public void setCreated(String created) {
                        this.created = created;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }

                    public double getDiscountPrice() {
                        return discountPrice;
                    }

                    public void setDiscountPrice(double discountPrice) {
                        this.discountPrice = discountPrice;
                    }

                    public int getDtyId() {
                        return dtyId;
                    }

                    public void setDtyId(int dtyId) {
                        this.dtyId = dtyId;
                    }

                    public String getEndTime() {
                        return endTime;
                    }

                    public void setEndTime(String endTime) {
                        this.endTime = endTime;
                    }

                    public boolean isExclude() {
                        return exclude;
                    }

                    public void setExclude(boolean exclude) {
                        this.exclude = exclude;
                    }

                    public int getExpireUseSms() {
                        return expireUseSms;
                    }

                    public void setExpireUseSms(int expireUseSms) {
                        this.expireUseSms = expireUseSms;
                    }

                    public ExtendParamBeanX getExtendParam() {
                        return extendParam;
                    }

                    public void setExtendParam(ExtendParamBeanX extendParam) {
                        this.extendParam = extendParam;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getIsCanGive() {
                        return isCanGive;
                    }

                    public void setIsCanGive(int isCanGive) {
                        this.isCanGive = isCanGive;
                    }

                    public int getIsDel() {
                        return isDel;
                    }

                    public void setIsDel(int isDel) {
                        this.isDel = isDel;
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

                    public int getOrderId() {
                        return orderId;
                    }

                    public void setOrderId(int orderId) {
                        this.orderId = orderId;
                    }

                    public int getReduceType() {
                        return reduceType;
                    }

                    public void setReduceType(int reduceType) {
                        this.reduceType = reduceType;
                    }

                    public int getServiceCardId() {
                        return serviceCardId;
                    }

                    public void setServiceCardId(int serviceCardId) {
                        this.serviceCardId = serviceCardId;
                    }

                    public int getShopId() {
                        return shopId;
                    }

                    public void setShopId(int shopId) {
                        this.shopId = shopId;
                    }

                    public int getSource() {
                        return source;
                    }

                    public void setSource(int source) {
                        this.source = source;
                    }

                    public String getStartTime() {
                        return startTime;
                    }

                    public void setStartTime(String startTime) {
                        this.startTime = startTime;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public double getTotalPrice() {
                        return totalPrice;
                    }

                    public void setTotalPrice(double totalPrice) {
                        this.totalPrice = totalPrice;
                    }

                    public int getTypeId() {
                        return typeId;
                    }

                    public void setTypeId(int typeId) {
                        this.typeId = typeId;
                    }

                    public String getUsed() {
                        return used;
                    }

                    public void setUsed(String used) {
                        this.used = used;
                    }

                    public int getUserId() {
                        return userId;
                    }

                    public void setUserId(int userId) {
                        this.userId = userId;
                    }

                    public static class ExtendParamBeanX {
                    }
                }
            }
        }

        public static class RefundRuleBean {
            /**
             * refundable : 0
             * reason : 由于您宠物家至尊卡已被您转赠189XXXX8899本卡不可申请退款
             * refund_tips : ["1、可用于宠物家洗澡、美容、特色服务，预约服务享受9折优惠","2、充值赠送800元和1kg巅峰狗粮","3、折扣有效期为三年，过期后折扣取消，金额扔可消费","4、客服热线：400-0300-011","5、可用于宠物家洗澡、美容、特色服务，预约服务享受9折优惠","6、充值赠送800元和1kg巅峰狗粮"]
             */

            private int refundable;
            private String reason;
            private List<String> refundTips;

            public int getRefundable() {
                return refundable;
            }

            public void setRefundable(int refundable) {
                this.refundable = refundable;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public List<String> getRefund_tips() {
                return refundTips;
            }

            public void setRefund_tips(List<String> refund_tips) {
                this.refundTips = refund_tips;
            }
        }

        public static class CouponListBean {
            /**
             * couponId : 213
             * name : 特色服务免单卷(1张)
             * amount : 2
             */

            private int couponId;
            private String name;
            private int amount;

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

        public static class RefundDetailBean {
            /**
             * name : 提交成功
             * time : 2019-10-11 00:00:00
             * desc : 耐心等候
             */

            private String name;
            private String time;
            private String desc;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
