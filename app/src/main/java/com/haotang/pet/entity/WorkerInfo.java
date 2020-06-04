package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/7/3
 * @Description:
 */
public class WorkerInfo {

    /**
     * code : 0
     * data : {"workLoc":1,"tid":1,"id":102,"shopId":7,"level":{"id":1,"name":"中级","priceFactorBeauty":1,"priceFactorShop":1,"priceFactorShower":1,"priceFactorSpecial":1,"sort":10},"nextLevel":{"id":2,"name":"高级","priceFactorBeauty":1.3,"priceFactorShop":1,"priceFactorShower":1.3,"priceFactorSpecial":1.3,"sort":20},"levelDetail":{"positivePercent":"50.00%","positivePercentValue":0.5,"recentNegtiveTotal":1,"totalBeautyOrder":0,"totalFavorable":1,"totalOrder":2,"workerId":102},"shopName":"望京店","title":"中级OK","avatar":"/static/avatar/20170120103710.png","userId":55555654,"realName":"斐姐","isGratuityOpen":true,"lastOrderId":1,"gratuityInfo":{"isGratuityOpen":true,"gratuityInfo":[{"amount":2,"content":"2元"},{"amount":5,"content":"5元"},{"amount":10,"content":"10元"},{"amount":15,"content":"15元"}],"content_1":"发个红包鼓励一下","content_2":"觉得美容师服务还不错，发个红包吧"},"isDel":0}
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
         * workLoc : 1
         * tid : 1
         * id : 102
         * shopId : 7
         * level : {"id":1,"name":"中级","priceFactorBeauty":1,"priceFactorShop":1,"priceFactorShower":1,"priceFactorSpecial":1,"sort":10}
         * nextLevel : {"id":2,"name":"高级","priceFactorBeauty":1.3,"priceFactorShop":1,"priceFactorShower":1.3,"priceFactorSpecial":1.3,"sort":20}
         * levelDetail : {"positivePercent":"50.00%","positivePercentValue":0.5,"recentNegtiveTotal":1,"totalBeautyOrder":0,"totalFavorable":1,"totalOrder":2,"workerId":102}
         * shopName : 望京店
         * title : 中级OK
         * avatar : /static/avatar/20170120103710.png
         * userId : 55555654
         * realName : 斐姐
         * isGratuityOpen : true
         * lastOrderId : 1
         * gratuityInfo : {"isGratuityOpen":true,"gratuityInfo":[{"amount":2,"content":"2元"},{"amount":5,"content":"5元"},{"amount":10,"content":"10元"},{"amount":15,"content":"15元"}],"content_1":"发个红包鼓励一下","content_2":"觉得美容师服务还不错，发个红包吧"}
         * isDel : 0
         */

        private int workLoc;
        private int tid;
        private int id;
        private int shopId;
        private LevelBean level;
        private NextLevelBean nextLevel;
        private LevelDetailBean levelDetail;
        private String shopName;
        private String title;
        private String avatar;
        private int userId;
        private String realName;
        private boolean isGratuityOpen;
        private int lastOrderId;
        private GratuityInfoBeanX gratuityInfo;
        private int isDel;

        public int getWorkLoc() {
            return workLoc;
        }

        public void setWorkLoc(int workLoc) {
            this.workLoc = workLoc;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
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

        public LevelBean getLevel() {
            return level;
        }

        public void setLevel(LevelBean level) {
            this.level = level;
        }

        public NextLevelBean getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(NextLevelBean nextLevel) {
            this.nextLevel = nextLevel;
        }

        public LevelDetailBean getLevelDetail() {
            return levelDetail;
        }

        public void setLevelDetail(LevelDetailBean levelDetail) {
            this.levelDetail = levelDetail;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public boolean isIsGratuityOpen() {
            return isGratuityOpen;
        }

        public void setIsGratuityOpen(boolean isGratuityOpen) {
            this.isGratuityOpen = isGratuityOpen;
        }

        public int getLastOrderId() {
            return lastOrderId;
        }

        public void setLastOrderId(int lastOrderId) {
            this.lastOrderId = lastOrderId;
        }

        public GratuityInfoBeanX getGratuityInfo() {
            return gratuityInfo;
        }

        public void setGratuityInfo(GratuityInfoBeanX gratuityInfo) {
            this.gratuityInfo = gratuityInfo;
        }

        public int getIsDel() {
            return isDel;
        }

        public void setIsDel(int isDel) {
            this.isDel = isDel;
        }

        public static class LevelBean {
            /**
             * id : 1
             * name : 中级
             * priceFactorBeauty : 1
             * priceFactorShop : 1
             * priceFactorShower : 1
             * priceFactorSpecial : 1
             * sort : 10
             */

            private int id;
            private String name;
            private int sort;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }

        public static class NextLevelBean {
            /**
             * id : 2
             * name : 高级
             * priceFactorBeauty : 1.3
             * priceFactorShop : 1
             * priceFactorShower : 1.3
             * priceFactorSpecial : 1.3
             * sort : 20
             */

            private int id;
            private String name;
            private double priceFactorBeauty;
            private int priceFactorShop;
            private double priceFactorShower;
            private double priceFactorSpecial;
            private int sort;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getPriceFactorBeauty() {
                return priceFactorBeauty;
            }

            public void setPriceFactorBeauty(double priceFactorBeauty) {
                this.priceFactorBeauty = priceFactorBeauty;
            }

            public int getPriceFactorShop() {
                return priceFactorShop;
            }

            public void setPriceFactorShop(int priceFactorShop) {
                this.priceFactorShop = priceFactorShop;
            }

            public double getPriceFactorShower() {
                return priceFactorShower;
            }

            public void setPriceFactorShower(double priceFactorShower) {
                this.priceFactorShower = priceFactorShower;
            }

            public double getPriceFactorSpecial() {
                return priceFactorSpecial;
            }

            public void setPriceFactorSpecial(double priceFactorSpecial) {
                this.priceFactorSpecial = priceFactorSpecial;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }

        public static class LevelDetailBean {
            /**
             * positivePercent : 50.00%
             * positivePercentValue : 0.5
             * recentNegtiveTotal : 1
             * totalBeautyOrder : 0
             * totalFavorable : 1
             * totalOrder : 2
             * workerId : 102
             */

            private String positivePercent;
            private double positivePercentValue;
            private int recentNegtiveTotal;
            private int totalBeautyOrder;
            private int totalFavorable;
            private int totalOrder;
            private int workerId;

            public String getPositivePercent() {
                return positivePercent;
            }

            public void setPositivePercent(String positivePercent) {
                this.positivePercent = positivePercent;
            }

            public double getPositivePercentValue() {
                return positivePercentValue;
            }

            public void setPositivePercentValue(double positivePercentValue) {
                this.positivePercentValue = positivePercentValue;
            }

            public int getRecentNegtiveTotal() {
                return recentNegtiveTotal;
            }

            public void setRecentNegtiveTotal(int recentNegtiveTotal) {
                this.recentNegtiveTotal = recentNegtiveTotal;
            }

            public int getTotalBeautyOrder() {
                return totalBeautyOrder;
            }

            public void setTotalBeautyOrder(int totalBeautyOrder) {
                this.totalBeautyOrder = totalBeautyOrder;
            }

            public int getTotalFavorable() {
                return totalFavorable;
            }

            public void setTotalFavorable(int totalFavorable) {
                this.totalFavorable = totalFavorable;
            }

            public int getTotalOrder() {
                return totalOrder;
            }

            public void setTotalOrder(int totalOrder) {
                this.totalOrder = totalOrder;
            }

            public int getWorkerId() {
                return workerId;
            }

            public void setWorkerId(int workerId) {
                this.workerId = workerId;
            }
        }

        public static class GratuityInfoBeanX {
            /**
             * isGratuityOpen : true
             * gratuityInfo : [{"amount":2,"content":"2元"},{"amount":5,"content":"5元"},{"amount":10,"content":"10元"},{"amount":15,"content":"15元"}]
             * content_1 : 发个红包鼓励一下
             * content_2 : 觉得美容师服务还不错，发个红包吧
             */

            private boolean isGratuityOpen;
            private String content_1;
            private String content_2;
            private List<GratuityInfoBean> gratuityInfo;

            public boolean isIsGratuityOpen() {
                return isGratuityOpen;
            }

            public void setIsGratuityOpen(boolean isGratuityOpen) {
                this.isGratuityOpen = isGratuityOpen;
            }

            public String getContent_1() {
                return content_1;
            }

            public void setContent_1(String content_1) {
                this.content_1 = content_1;
            }

            public String getContent_2() {
                return content_2;
            }

            public void setContent_2(String content_2) {
                this.content_2 = content_2;
            }

            public List<GratuityInfoBean> getGratuityInfo() {
                return gratuityInfo;
            }

            public void setGratuityInfo(List<GratuityInfoBean> gratuityInfo) {
                this.gratuityInfo = gratuityInfo;
            }

            public static class GratuityInfoBean {
                /**
                 * amount : 2
                 * content : 2元
                 */

                private double amount;
                private String content;
                private String remark;
                private boolean isCheck;

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public boolean isCheck() {
                    return isCheck;
                }

                public void setCheck(boolean check) {
                    isCheck = check;
                }

                public double getAmount() {
                    return amount;
                }

                public void setAmount(double amount) {
                    this.amount = amount;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }
        }
    }
}
