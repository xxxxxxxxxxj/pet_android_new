package com.haotang.pet.entity;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/23
 * @Description:E卡列表banner
 */
public class GiftCardListBanner {


    /**
     * code : 0
     * data : {"otherOperateBannerList":[{"backup":"","createTime":"2019-04-18 15:59:45","enable":0,"endTime":"2019-04-18 16:50:00","id":3,"imgUrl":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/operate/15555743841592178699.png","isDel":0,"isFirstRecharge":0,"name":"张琪\u2014\u201421","point":25,"startTime":"2019-04-18 15:55:00","type":2,"updateTime":"2019-04-18 16:00:04","versionNumber":""}]}
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
        private List<OtherOperateBannerListBean> otherOperateBannerList;

        public List<OtherOperateBannerListBean> getOtherOperateBannerList() {
            return otherOperateBannerList;
        }

        public void setOtherOperateBannerList(List<OtherOperateBannerListBean> otherOperateBannerList) {
            this.otherOperateBannerList = otherOperateBannerList;
        }

        public static class OtherOperateBannerListBean {
            /**
             * backup :
             * createTime : 2019-04-18 15:59:45
             * enable : 0
             * endTime : 2019-04-18 16:50:00
             * id : 3
             * imgUrl : http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/operate/15555743841592178699.png
             * isDel : 0
             * isFirstRecharge : 0
             * name : 张琪——21
             * point : 25
             * startTime : 2019-04-18 15:55:00
             * type : 2
             * updateTime : 2019-04-18 16:00:04
             * versionNumber :
             */

            private String backup;
            private String createTime;
            private int enable;
            private String endTime;
            private int id;
            private String imgUrl;
            private int isDel;
            private int isFirstRecharge;
            private String name;
            private int point;
            private String startTime;
            private int type;
            private String updateTime;
            private String versionNumber;

            public String getBackup() {
                return backup;
            }

            public void setBackup(String backup) {
                this.backup = backup;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getEnable() {
                return enable;
            }

            public void setEnable(int enable) {
                this.enable = enable;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public int getIsDel() {
                return isDel;
            }

            public void setIsDel(int isDel) {
                this.isDel = isDel;
            }

            public int getIsFirstRecharge() {
                return isFirstRecharge;
            }

            public void setIsFirstRecharge(int isFirstRecharge) {
                this.isFirstRecharge = isFirstRecharge;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getVersionNumber() {
                return versionNumber;
            }

            public void setVersionNumber(String versionNumber) {
                this.versionNumber = versionNumber;
            }
        }
    }
}
