package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/12/19
 * @Description:
 */
public class FosterAddCareItem {

    /**
     * code : 0
     * data : {"noWorkerTip":"当前时间不可约","dataset":[{"myPetId":1621,"avatar":"/static/pavatar/bxq.jpg?1522390442000","nickname":"西瓜","services":[{"name":"离店洗澡","id":1,"img":"http://xxx.com/head.img","ePrice":39,"price":49,"desc":"基础洗护服务","workers":[{"img":"http://xxx.com/head.img","name":"中级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]},{"img":"http://xxx.com/head.img","name":"高级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]}]}]}]}
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
         * noWorkerTip : 当前时间不可约
         * dataset : [{"myPetId":1621,"avatar":"/static/pavatar/bxq.jpg?1522390442000","nickname":"西瓜","services":[{"name":"离店洗澡","id":1,"img":"http://xxx.com/head.img","ePrice":39,"price":49,"desc":"基础洗护服务","workers":[{"img":"http://xxx.com/head.img","name":"中级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]},{"img":"http://xxx.com/head.img","name":"高级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]}]}]}]
         */

        private String noWorkerTip;
        private int shopId;
        private String shopTip;
        private String serviceTip;
        private String changeServiceTip;

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopTip() {
            return shopTip;
        }

        public void setShopTip(String shopTip) {
            this.shopTip = shopTip;
        }

        public String getServiceTip() {
            return serviceTip;
        }

        public void setServiceTip(String serviceTip) {
            this.serviceTip = serviceTip;
        }

        public String getChangeServiceTip() {
            return changeServiceTip;
        }

        public void setChangeServiceTip(String changeServiceTip) {
            this.changeServiceTip = changeServiceTip;
        }

        private List<DatasetBean> dataset;

        public String getNoWorkerTip() {
            return noWorkerTip;
        }

        public void setNoWorkerTip(String noWorkerTip) {
            this.noWorkerTip = noWorkerTip;
        }

        public List<DatasetBean> getDataset() {
            return dataset;
        }

        public void setDataset(List<DatasetBean> dataset) {
            this.dataset = dataset;
        }

        public static class DatasetBean {
            /**
             * myPetId : 1621
             * avatar : /static/pavatar/bxq.jpg?1522390442000
             * nickname : 西瓜
             * services : [{"name":"离店洗澡","id":1,"img":"http://xxx.com/head.img","ePrice":39,"price":49,"desc":"基础洗护服务","workers":[{"img":"http://xxx.com/head.img","name":"中级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]},{"img":"http://xxx.com/head.img","name":"高级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]}]}]
             */

            private int myPetId;
            private int petId;
            private String avatar;
            private String nickname;
            private boolean isPetSelected;
            private int selectServiceId;
            private int selectWorkerId;
            private int selectWorkerTid;

            public int getSelectWorkerTid() {
                return selectWorkerTid;
            }

            public void setSelectWorkerTid(int selectWorkerTid) {
                this.selectWorkerTid = selectWorkerTid;
            }

            public int getPetId() {
                return petId;
            }

            public void setPetId(int petId) {
                this.petId = petId;
            }

            public int getSelectServiceId() {
                return selectServiceId;
            }

            public void setSelectServiceId(int selectServiceId) {
                this.selectServiceId = selectServiceId;
            }

            public int getSelectWorkerId() {
                return selectWorkerId;
            }

            public void setSelectWorkerId(int selectWorkerId) {
                this.selectWorkerId = selectWorkerId;
            }

            public boolean isPetSelected() {
                return isPetSelected;
            }

            public void setPetSelected(boolean petSelected) {
                isPetSelected = petSelected;
            }

            private List<ServicesBean> services;

            public int getMyPetId() {
                return myPetId;
            }

            public void setMyPetId(int myPetId) {
                this.myPetId = myPetId;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public List<ServicesBean> getServices() {
                return services;
            }

            public void setServices(List<ServicesBean> services) {
                this.services = services;
            }

            public static class ServicesBean {
                /**
                 * name : 离店洗澡
                 * id : 1
                 * img : http://xxx.com/head.img
                 * ePrice : 39
                 * price : 49
                 * desc : 基础洗护服务
                 * workers : [{"img":"http://xxx.com/head.img","name":"中级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]},{"img":"http://xxx.com/head.img","name":"高级美容师","ePrice":39,"price":49,"tid":1,"members":[1022]}]
                 */

                private String name;
                private int id;
                private String img;
                private double ePrice;
                private double price;
                private double selectEPrice;
                private double selectPrice;
                private String desc;
                private boolean isServiceSelect;

                public double getSelectEPrice() {
                    return selectEPrice;
                }

                public void setSelectEPrice(double selectEPrice) {
                    this.selectEPrice = selectEPrice;
                }

                public double getSelectPrice() {
                    return selectPrice;
                }

                public void setSelectPrice(double selectPrice) {
                    this.selectPrice = selectPrice;
                }

                public boolean isServiceSelect() {
                    return isServiceSelect;
                }

                public void setServiceSelect(boolean serviceSelect) {
                    isServiceSelect = serviceSelect;
                }

                private List<WorkersBean> workers;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public double getEPrice() {
                    return ePrice;
                }

                public void setEPrice(double ePrice) {
                    this.ePrice = ePrice;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public List<WorkersBean> getWorkers() {
                    return workers;
                }

                public void setWorkers(List<WorkersBean> workers) {
                    this.workers = workers;
                }

                public static class WorkersBean {
                    /**
                     * img : http://xxx.com/head.img
                     * name : 中级美容师
                     * ePrice : 39
                     * price : 49
                     * tid : 1
                     * members : [1022]
                     */

                    private String img;
                    private String name;
                    private double ePrice;
                    private double price;
                    private int tid;
                    private List<Integer> members;
                    private boolean isSelect;
                    private boolean isAvail;

                    public boolean isAvail() {
                        return isAvail;
                    }

                    public void setAvail(boolean avail) {
                        isAvail = avail;
                    }

                    public boolean isSelect() {
                        return isSelect;
                    }

                    public void setSelect(boolean select) {
                        isSelect = select;
                    }

                    public String getImg() {
                        return img;
                    }

                    public void setImg(String img) {
                        this.img = img;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public double getEPrice() {
                        return ePrice;
                    }

                    public void setEPrice(double ePrice) {
                        this.ePrice = ePrice;
                    }

                    public double getPrice() {
                        return price;
                    }

                    public void setPrice(double price) {
                        this.price = price;
                    }

                    public int getTid() {
                        return tid;
                    }

                    public void setTid(int tid) {
                        this.tid = tid;
                    }

                    public List<Integer> getMembers() {
                        return members;
                    }

                    public void setMembers(List<Integer> members) {
                        this.members = members;
                    }
                }
            }
        }
    }
}
