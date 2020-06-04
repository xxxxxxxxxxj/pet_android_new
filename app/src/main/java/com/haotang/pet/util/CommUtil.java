package com.haotang.pet.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.haotang.pet.net.AsyncHttpClient;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.net.BinaryHttpResponseHandler;
import com.haotang.pet.net.RequestParams;

import java.io.File;

public class CommUtil {

    public static int getEnvironmental() {
        return 3;//1.test环境---2.demo环境---3.线上环境 --- 4.志超测试环境 -- 5、志超demo环境
    }

    public static String getServiceBaseUrlNew() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "http://192.168.0.252/pet-api/";
                break;
            case 2://demo环境
                url = "https://demo.cwjia.cn/pet-api/";
                break;
            case 3://线上环境
                url = "https://api.ichongwujia.com/";
                break;
            case 4://志超测试环境
                url = "http://192.168.0.253/pet-api/";
                break;
            case 5://志超demo环境
                url = "http://demo1.cwjia.cn/pet-api/";
                break;
            default:
                break;
        }
        return url;
    }

    /**
     * 图片和H5的域名头
     *
     * @return
     */
    public static String getStaticUrl() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "http://192.168.0.252/";
                break;
            case 2://demo环境
                url = "http://demo.cwjia.cn/";
                break;
            case 3://线上环境
                url = "http://static.ichongwujia.com/";
                break;
            case 4://志超测试环境
                url = "http://192.168.0.253/";
                break;
            case 5://志超demo环境
                url = "http://demo1.cwjia.cn/";
                break;
            default:
                break;
        }
        return url;
    }

    public static String getWebBaseUrl() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "http://192.168.0.247/";
                break;
            case 2://demo环境
                url = "http://192.168.0.248/";
                break;
            case 3://线上环境
                url = "http://m.cwjia.cn/";
                break;
            case 4://志超测试环境
                url = "http://192.168.0.247/";
                break;
            case 5://志超demo环境
                url = "http://192.168.0.248/";
                break;
            default:
                break;
        }
        return url;
    }

    public static String getSource() {
        return "android";
    }

    /**
     * 设置超时时间
     *
     * @return
     */
    public static int getTimeOut() {
        return 60 * 1000;
    }

    /**
     * 设置voi超时时间
     *
     * @return
     */
    public static int getTimeOutVoi() {
        return Integer.MAX_VALUE;
    }

    public static RequestParams LocalParmPost(Context context) {
        RequestParams params = new RequestParams();
        String imei = Global.getIMEI(context);
        if (imei != null && !TextUtils.isEmpty(imei)) {
            params.put("imei", imei);
        }
        params.put("account", 1);
        params.put("phoneModel", android.os.Build.BRAND + " "
                + android.os.Build.MODEL);
        params.put("phoneSystemVersion", "Android "
                + android.os.Build.VERSION.RELEASE);
        params.put("petTimeStamp", System.currentTimeMillis());
        return params;
    }

    public static RequestParams LocalParm(Context context) {
        RequestParams params = new RequestParams();
        String cellPhone = SharedPreferenceUtil.getInstance(context).getString(
                "cellphone", "");
        String imei = Global.getIMEI(context);
        if (cellPhone != null && !TextUtils.isEmpty(cellPhone)) {
            params.put("cellPhone", cellPhone);
        }
        if (imei != null && !TextUtils.isEmpty(imei)) {
            params.put("imei", imei);
        }
        params.put("account", 1);
        params.put("phoneModel", android.os.Build.BRAND + " "
                + android.os.Build.MODEL);
        params.put("phoneSystemVersion", "Android "
                + android.os.Build.VERSION.RELEASE);
        params.put("petTimeStamp", System.currentTimeMillis());
        return params;
    }

    /**
     * 自动登录
     *
     * @param phone
     * @param imei
     * @param handler
     */
    public static void loginAuto(Context context, String phone, String imei,
                                 String version, int userid, double lat, double lng,
                                 AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/autoLogin?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("id", userid);
            if (lat != 0 || lng != 0) {
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            }
            params.put("channelId", ChannelUtil.getChannel(context));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("自动登录参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取最新的app，是否强制升级
     *
     * @param handler
     */
    public static void getLatestVersion(Context context, String service,
                                        long time, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/checkversion?time=" + time + "&system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("systemType", 2);
            params.put("version", Global.getCurrentVersion(context));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(3000);
            Utils.mLogError("升级："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取h5的url
     *
     * @param handler
     */
    public static void getH5Url(Context context,
                                AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/getInviteConfig?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            Utils.mLogError("获取邀请有礼h5：" + url);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取公司qq和微信
     *
     * @param time    时间戳
     * @param handler
     */
    public static void getCompanyContact(Context context, long time,
                                         AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/getFeedbackPhoneInfo?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("获取公司qq和微信："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 寄养新首页
     *
     * @param handler
     */
    public static void getReadyReserve(Context context,
                                       AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "hotel/index?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("寄养新首页："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取可用的优惠券
     *
     * @param phone
     * @param imei
     * @param version
     * @param time           订单预约时间
     * @param type           服务类型 1:洗澡美容，2：寄养 4 训练
     * @param serviceloc     服务类型id 1：到店，2:上门
     * @param workerid       美容师id
     * @param pickup         是否接送 0：不接送 1：接送
     * @param customerName   客户姓名
     * @param customerMobile 客户电话
     * @param addressid      订单地址id
     * @param address        订单地址
     * @param lat            订单地址坐标
     * @param lng
     * @param strp           多宠物的petid_serviceid_我的宠物ID_单项ID 如 6_1_3_1,2,5 现在改为 PetID_ServiceId_MyPetId_ItemIds 四项拼接 没有单项拼0
     * @param category       1, //优惠券大类,新增参数,必传(1:洗美优惠券、2:上门优惠券)
     * @param handler
     */
    public static void getAvailableCoupon(Context context, String phone,
                                          String imei, String version, String time, int type, int serviceloc,
                                          int workerid, int pickup, String customerName,
                                          String customerMobile, int addressid, String address, double lat,
                                          double lng, String strp, int areaId, double payPrice, int shopId,
                                          String endTime, int category, int serviceCardId, int updateOrderId, int refType, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "coupon/order/match?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (refType > 0) {
                params.put("refType", refType);
            }
            if (updateOrderId > 0) {
                params.put("updateOrderId", updateOrderId);
            }
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            if (Utils.isStrNull(time)) {
                params.put("appointment", time);
            }
            if (addressid > 0)
                params.put("addressId", addressid);
            if (workerid > 0)
                params.put("workerId", workerid);
            if (type > 0)
                params.put("type", type);
            if (Utils.isStrNull(customerName)) {
                params.put("customerName", customerName);
            }
            if (Utils.isStrNull(customerMobile)) {
                params.put("customerMobile", customerMobile);
            }
            if (lat > 0)
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            if (lng > 0)
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            if (!TextUtils.isEmpty(address)) {
                params.put("address", address);
            }
            if (type != 3 && type != 2 && type != 4 && serviceloc > 0) {
                params.put("serviceLoc", serviceloc);
            }
            if (pickup > 0)
                params.put("pickUp", pickup);
            if (!TextUtils.isEmpty(strp)) {
                params.put("strp", strp);
            }
            if (areaId > 0) {
                params.put("areaId", areaId);
            }
            params.put("payPrice", payPrice);
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            if (!TextUtils.isEmpty(endTime)) {
                params.put("endTime", endTime);
            }
            params.put("category", category);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->可用优惠券："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.mLogError("==-->可用优惠券挂了");
        }
    }

    /**
     * 获取账户余额
     *
     * @param phone
     * @param imei
     * @param version
     * @param handler
     */
    public static void getAccountBalance(Context context, String phone,
                                         String imei, String version, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "user/accountCenter?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("账户余额参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * //    /pet-api/hotel/orderConfirmation
     *
     * @param mContext
     * @param roomType
     * @param petid
     * @param shopid
     * @param starttime
     * @param endtime
     * @param handler
     */
    public static void getFosterOrderConfirmation(Activity mContext, int careShopId, int roomType,
                                                  int mypetId, int shopId,
                                                  String startTime, String endTime, String strp, String extraPetIds, int couponId, int serviceCardId,
                                                  AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "hotel/orderConfirmation" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("roomType", roomType);
            params.put("mypetId", mypetId);
            params.put("shopId", shopId);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            if (Utils.isStrNull(strp)) {
                params.put("strp", strp);
            }
            if (Utils.isStrNull(extraPetIds)) {
                params.put("extraPetIds", extraPetIds);
            }
            if (careShopId > 0) {
                params.put("careShopId", careShopId);
            }
            if (couponId > 0) {
                params.put("couponId", couponId);
            }
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("寄养新版下单界面参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取店铺房间
     *
     * @param petid
     * @param starttime
     * @param endtime
     * @param handler
     */
    public static void getRoomType(Context context, int shopId, int petid,
                                   String starttime, String endtime, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "hotel/getRoomType?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);

            params.put("shopId", shopId);
            if (petid != 0) {
                params.put("petId", petid);
            }
            if (Utils.isStrNull(starttime)) {
                params.put("startTime", starttime);
            }
            if (Utils.isStrNull(endtime)) {
                params.put("endTime", endtime);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("下发房型："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 添加宠物获取服务
     *
     * @param petid
     * @param shopid
     * @param servLoc
     * @param time
     * @param lat
     * @param lng
     * @param handler
     */
    public static void switchService(Context context, int petId,
                                     int shopId, int serviceLoc, String appointment, int serviceId, int workerId, int serviceCardId,
                                     AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "appointment/switchService?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("petId", petId);
            if (Utils.isStrNull(appointment)) {
                params.put("appointment", appointment);
            }
            if (workerId > 0) {
                params.put("workerId", workerId);
            }
            params.put("shopId", shopId);
            params.put("serviceLoc", serviceLoc);
            params.put("serviceId", serviceId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("切换服务："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取可以预约的时间
     *
     * @param phone
     * @param imei
     * @param version
     * @param areaid
     * @param serviceloc 服务方式 1：店面 2：上门
     * @param shopid
     * @param strp       有三部分组成 petid_serviceid_单项id 如：10_2_1,5,7,没有单项时传0 如10_2_0
     * @param tid        改单 时候需要传递 1 中级 2 高级 3 首席
     * @param handler
     */
    public static void getBeauticianFreeTime(Context context, double lat,
                                             double lng, String phone, String imei, String version, int areaid,
                                             int serviceloc, int shopid, String strp, int tid, String app_time,
                                             int orderid, int workerId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "appointment/getTaskCalendar?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = LocalParm(context);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            if (workerId > 0) {
                params.put("workerId", workerId);
            }
            if (app_time != null && !TextUtils.isEmpty(app_time)) {
                params.put("appTime", app_time);
            }
            params.put("strp", strp);
            if (serviceloc > 0) {
                params.put("serviceLoc", serviceloc);
            }
            if (shopid > 0) {
                params.put("shopId", shopid);
            }
            if (areaid > 0) {
                params.put("areaId", areaid);
            }
            if (tid > 0) {
                params.put("tid", tid);
            }
            if (orderid > 0) {
                params.put("orderId", orderid);
            }
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取时间：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 生成新订单
     *
     * @param imei            用户手机IMEI码
     * @param version         版本号
     * @param shopid          店铺id
     * @param areaid          服务范围id
     * @param couponid        优惠券id
     * @param workerid        美容师id
     * @param addressid       地址id
     * @param customerName    联系人姓名
     * @param customerMobile  联系人电话
     * @param address         服务地址（有id的空为空）
     * @param lat             坐标
     * @param lng             坐标
     * @param time            服务时间
     * @param totalPrice      订单金额
     * @param payPrice        需付款金额
     * @param payway          支付方式3：优惠券1：微信2：支付宝 4:余额
     * @param remark          备注
     * @param serviceloc      服务方式 1：到店2：到家
     * @param pickup          是否接送 0：不接送 1：接送
     * @param isDefaultWorker 是否为系统推荐美容师 0：否 1：是
     * @param strp            多宠物的petid_serviceid_单项ID 如 6_1_1,2,5
     * @param debitAmount     混合支付时余额应支付的金额，非混合支付时为-1
     * @param uuid
     * @param cardIds         次卡ids
     * @param canAmount       罐头币总数
     * @param homeCouponId    上门服务抵扣劵
     * @param upgradeWorkerId 升级后的高级美容师ID
     * @param handler
     */
    public static void newOrderApi(Context context, String phone, String imei,
                                   String version, int shopid, int areaid, int couponid, int workerid,
                                   int addressid, String customerName, String customerMobile,
                                   String address, double lat, double lng, String time,
                                   double totalPrice, double payPrice, int payway, String remark,
                                   int serviceloc, int pickup, int isDefaultWorker, String strp,
                                   double debitAmount, String uuid, String cardIds, int canAmount, int homeCouponId, int upgradeWorkerId, int isUpgradeWorker,
                                   int serviceCardId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "care/newOrder?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (shopid > 0)
                params.put("shopId", shopid);
            /*if (areaid > 0)
                params.put("areaId", areaid);*/
            if (couponid > 0) {
                params.put("couponId", couponid);
            }
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("payWay", payway);
            if (addressid > 0) {
                params.put("addressId", addressid);
            }
            if (workerid > 0) {
                params.put("workerId", workerid);
            }
            params.put("customerName", customerName);
            params.put("isUpgradeWorker", isUpgradeWorker);
            params.put("customerMobile", customerMobile);
            params.put("token", uuid);
            if (lat > 0)
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            if (lng > 0)
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            params.put("address", address);
            params.put("appointment", time);
            params.put("remark", remark);
            params.put("type", 1);
            params.put("totalPrice", totalPrice);
            params.put("payPrice", payPrice);
            params.put("debitAmount", debitAmount);
            if (strp != null && !"".equals(strp)) {
                params.put("strp", strp);
            }
            params.put("serviceLoc", serviceloc);
            params.put("isDefaultWorker", isDefaultWorker);
            params.put("pickUp", pickup);
            if (!TextUtils.isEmpty(cardIds)) {
                params.put("cardIds", cardIds);
            }
            if (canAmount > 0) {
                params.put("canAmount", canAmount);
            }
            if (homeCouponId > 0) {
                params.put("homeCouponId", homeCouponId);
            }
            if (upgradeWorkerId > 0) {
                params.put("upgradeTaskId", upgradeWorkerId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取新接口订单参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师的详情
     *
     * @param workerid
     * @param handler
     */
    public static void getBeauticianDetail(Context context, int workerid,
                                           int areaId, String app_time, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "worker/queryWorkerById?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("workerId", workerid);
            if (areaId > 0 && areaId != 100) {
                params.put("areaId", areaId);
            }
            if (app_time != null && !TextUtils.isEmpty(app_time)) {
                params.put("app_time", app_time);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->查询美容师详情："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师评论列表
     *
     * @param workerid
     * @param pagesize
     * @param handler
     */
    public static void getCommentByBeauticianId(Context context, int workerid,
                                                int page, int pagesize, int hasImg, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "comment/worker?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (hasImg > 0) {
                params.put("hasImg", hasImg);
            }
            params.put("workerId", workerid);
            params.put("page", page);
            params.put("size", pagesize);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->获取评论列表参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师评论列表
     *
     * @param workerid
     * @param pagesize
     * @param handler
     */
    public static void getCommentByBeauticianIdNoHasImg(Context context, int workerid,
                                                        int page, int pagesize, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "comment/worker?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("workerId", workerid);
            params.put("page", page);
            params.put("size", pagesize);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->获取评论列表参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 意见反馈
     *
     * @param phone   注册手机号
     * @param imei    手机IMEI码
     * @param version 应用版本号
     * @param contact 联系方式
     * @param content 反馈内容
     * @param handler
     */
    public static void feedBack(Context context, int userId, String phone,
                                String imei, String version, String contact, String content,
                                AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/feedback?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("content", content);
            params.put("contact", contact);
            params.put("userId", userId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("意见反馈参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 去后台注册个推
     *
     * @param phone    可选
     * @param version
     * @param userId   可选
     * @param devToken 个推返回的cid
     * @param handler
     */
    public static void registGeTuitoService(Context context, String phone,
                                            String version, String imei, int userId, String devToken,
                                            double lat, double lng, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/registUserDevice?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("devToken", devToken);
            params.put("userId", userId);
            if (lat > 0)
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            if (lng > 0)
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            params.put("channelId", ChannelUtil.getChannel(context));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("注册个推参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取首页数据，当前只有订单是否有未评价
     *
     * @param phone
     * @param version
     * @param imei
     * @param handler
     */
    public static void getIndex(Context context, String phone, String version,
                                String imei, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/index?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("首页参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param phone
     * @param version
     * @param imei
     * @param id       该用户宠物ID
     * @param petid    宠物品种ID
     * @param petname  宠物品种名称
     * @param nickname 宠物昵称
     * @param sex      性别
     * @param birthday 生日
     * @param height   肩高
     * @param remark   备注
     * @param pic      照片
     * @param handler
     */
    public static void newCustomerPet(Context context, String phone,
                                      String version, String imei, int id, int petid, String petname,
                                      String nickname, int sex, String birthday, int orderid, int height,
                                      String remark, String pic, String color,
                                      AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "pet/saveCustomerPet?system="
                + getSource() + "_" + Global.getCurrentVersion(context) + "&cellPhone=" + SharedPreferenceUtil.getInstance(context).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(context);
            params.put("petId", petid);
            params.put("petName", petname);
            if (id > 0)
                params.put("id", id);
            if (orderid > 0)
                params.put("orderId", orderid);
            params.put("nickName", nickname);
            params.put("sex", sex);
            params.put("birthday", birthday);
            if (height >= 0)
                params.put("height", height);
            params.put("remark", remark);
            if (pic != null && !"".equals(pic))
                params.put("pic", pic);
            params.put("color", color);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->添加宠物参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.post(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除宠物接口
     *
     * @param phone
     * @param version
     * @param imei
     * @param id      该用户下的宠物id
     * @param handler
     */
    public static void deleteCustomerPet(Context context, String phone,
                                         String version, String imei, int id,
                                         AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "pet/deleteCustomerPet?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (id > 0)
                params.put("id", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("删除宠物参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 查询该用户下某个宠物的信息
     *
     * @param phone
     * @param version
     * @param imei
     * @param id      该用户下宠物id
     * @param handler
     */
    public static void queryCustomerPetById(Context context, String phone,
                                            String version, String imei, int id,
                                            AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "pet/queryCustomerPetById?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (id > 0)
                params.put("id", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("查询单个宠物参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 绑定微信 /pet-api/user/bindWxOpenId
     *
     */
    public static void bindWxOpenId(Context context, String phone, String imei,
                                    String code, double lat, double lng,
                                    String openId,String orderSource,String wxAvatar,String wxNickname,
                                    AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/bindWxOpenId?system="
                + getSource() + "_" + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            params.put("cellPhone", phone);
            params.put("code", code);
            if(Utils.isStrNull(openId)){
                params.put("openId", openId);
            }
            if(Utils.isStrNull(orderSource)){
                params.put("orderSource", orderSource);
            }
            if(Utils.isStrNull(wxAvatar)){
                params.put("wxAvatar", wxAvatar);
            }
            if(Utils.isStrNull(wxNickname)){
                params.put("wxNickname", wxNickname);
            }
            if(Utils.isStrNull(code)){
                params.put("code", code);
            }
            params.put("channelId", ChannelUtil.getChannel(context));
            if (lat != 0 || lng != 0) {
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==--> 绑定微信："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 解绑微信 user/unBindWxOpenId
     *
     */
    public static void unBindWxOpenId(Context context, String phone, String imei,
                                      String code, double lat, double lng,
                                      AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/unBindWxOpenId?system="
                + getSource() + "_" + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            params.put("cellPhone", phone);
            params.put("code", code);

            if(Utils.isStrNull(code)){
                params.put("code", code);
            }
            params.put("channelId", ChannelUtil.getChannel(context));
            if (lat != 0 || lng != 0) {
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==--> 解绑微信："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param phone
     * @param imei
     * @param context
     * @param code
     * @param handler
     */
    public static void loginAu(Context context, String phone, String imei,
                               String code, double lat, double lng,
                               String openId,String orderSource,String wxAvatar,String wxNickname,
                               AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/login?system="
                + getSource() + "_" + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            params.put("cellPhone", phone);
            params.put("code", code);
            if(Utils.isStrNull(openId)){
                params.put("openId", openId);
            }
            if(Utils.isStrNull(orderSource)){
                params.put("orderSource", orderSource);
            }
            if(Utils.isStrNull(wxAvatar)){
                params.put("wxAvatar", wxAvatar);
            }
            if(Utils.isStrNull(wxNickname)){
                params.put("wxNickname", wxNickname);
            }
            if(Utils.isStrNull(code)){
                params.put("code", code);
            }
            params.put("channelId", ChannelUtil.getChannel(context));
            if (lat != 0 || lng != 0) {
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==--> 登录："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param handler
     */
    public static void genVerifyCode(Context context, String phone, String encryptionCode, int flag,
                                     AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/genVerifyCode?system="
                + getSource() + "_" + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            params.put("phone", phone);
            params.put("encryptionCode", encryptionCode);
            params.put("flag", flag);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取验证码："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取宠物列表
     *
     * @param kind      宠物类型
     * @param serviceId 服务类型
     * @param handler   serviceType //再增加一个serviceType 训练传400 serviceId 等同于serviceType
     *                  保持原样不动 serviceType 字段不加了
     */
    public static void getPetList(Context context, int kind, int serviceId,
                                  int workerid, int ServiceType, int templateId,
                                  AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "pet/petList?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (kind > 0)
                params.put("kind", kind);
            if (workerid > 0)
                params.put("workerId", workerid);
            if (ServiceType > 0) {
                params.put("serviceType", ServiceType);
            } else {
                if (serviceId > 0 && serviceId != 100) {
                    params.put("serviceId", serviceId);
                }
            }
            if (templateId > 0) {
                params.put("templateId", templateId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->获取宠物列表："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 添加服务地址
     * <p>
     * "
     * "
     * "
     * "
     * "
     * "
     *
     * @param context
     * @param id         "id" : "1", //地址ID,更新操作时必传
     * @param address    address" : "中关村国能新能源产业园", //地址
     * @param lat        lat" : "40.008926", //纬度
     * @param lng        lng" : "116.378311", //经度
     * @param supplement supplement": "门牌号110", //详细地址
     * @param linkman    linkman" : "联系人", //联系人
     * @param telephone  telephone" : "17777777777", //联系电话
     * @param handler
     */
    public static void addServiceAddress(Context context, int id, String address, double lat, double lng, String supplement, String linkman, String telephone,
                                         AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/addServiceAddress?system="
                + getSource() + "_" + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            if (id > 0) {
                params.put("id", id);
            }
            params.put("address", address);
            params.put("supplement", supplement);
            params.put("linkman", linkman);
            params.put("telephone", telephone);
            if (lat > 0) {
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            }
            if (lng > 0) {
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("添加服务地址：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 查询服务地址/user/queryServiceAddress
     *
     * @param phone
     * @param userId
     * @param imei
     * @param context
     * @param handler
     */
    public static void queryServiceAddress(int userId, Context context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/queryServiceAddress?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            params.put("userId", userId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询服务地址："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除地址/user/queryServiceAddress
     *
     * @param context
     * @param addressId
     * @param handler
     */
    public static void deleteServiceAddress(Context context, int addressId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/delServiceAddress?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            params.put("id", addressId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->删除宠物地址：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 更新用户信息/user/updateUser
     *
     * @param phone
     * @param imei
     * @param context
     * @param userId
     * @param userName
     * @param content
     * @param handler
     */
    public static void updateUser(String phone, String imei, Context context,
                                  long userId, String userName, String content,
                                  AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/updateUser?system="
                + getSource() + "_" + Global.getCurrentVersion(context) + "&cellPhone=" + SharedPreferenceUtil.getInstance(context).getString(
                "cellphone", "");

        try {
            RequestParams params = LocalParmPost(context);
            params.put("userId", userId);
            if (!userName.equals("")) {
                params.put("userName", userName);
            } else {
            }
            if (content != null && !content.equals("")) {
                params.put("content", content);
            } else {
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
            Utils.mLogError("==-->更新test" + "  "
                    + client.getUrlWithQueryString(true, url, params));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 评价订单
     *
     * @param context
     * @param pic            //评论美容师图片 多图逗号隔开
     * @param shopPic        /评论店铺图片 多图逗号隔开
     * @param credit         //评论美容师星级
     * @param shopCredit     //评论店铺星级
     * @param commentTag     //评论美容师标签id（逗号隔开）
     * @param shopCommentTag //评论店铺标签id（逗号隔开）
     * @param content        //美容师评价内容
     * @param shopContent    //门店评价内容
     * @param workerid       //美容师id
     * @param orderId        //订单id
     * @param isAnonymous
     * @param handler
     */
    public static void commentOrderNew(Context context,
                                       File[] pic, File[] shopPic, int credit, int shopCredit, String commentTag, String shopCommentTag,
                                       String content, String shopContent, int workerid, int orderId, int isAnonymous,
                                       AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "comment/careCommentOrder?system=" + getSource() + "_" + Global.getCurrentVersion(context) + "&cellPhone=" + SharedPreferenceUtil.getInstance(context).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(context);
            if (pic != null && pic.length > 0) {
                params.put("pic", pic);
            }
            if (shopPic != null && shopPic.length > 0) {
                params.put("shopPic", shopPic);
            }
            if (credit > 0) {
                params.put("grade", credit);
            }
            if (shopCredit > 0) {
                params.put("shopGrade", shopCredit);
            }
            if (!TextUtils.isEmpty(commentTag)) {
                params.put("commentTag", commentTag);
            }
            if (!TextUtils.isEmpty(shopCommentTag)) {
                params.put("shopCommentTag", shopCommentTag);
            }
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            if (!TextUtils.isEmpty(shopContent)) {
                params.put("shopContent", shopContent);
            }
            params.put("workerId", workerid);
            params.put("orderId", orderId);
            params.put("isAnonymous", isAnonymous);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
            Utils.mLogError("==-->评价new " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void fosterEvaluate(Activity mContext, int orderId, int type, int shopCredit, int shopGrade, String shopContent, String shopCommentTag, File[] shopPic, AsyncHttpResponseHandler fosterEvaluteHandler) {
        String url = getServiceBaseUrlNew() + "comment/careCommentOrder?system="
                + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            params.put("orderId", orderId);
            params.put("type", type);
            params.put("shopCredit", shopCredit);
            params.put("shopGrade", shopGrade);
            if (!TextUtils.isEmpty(shopContent)) {
                params.put("shopContent", shopContent);
            }
            if (!TextUtils.isEmpty(shopCommentTag)) {
                params.put("shopCommentTag", shopCommentTag);
            }
            if (shopPic != null && shopPic.length > 0) {
                Utils.mLogError("长度=====" + shopPic.length);
                params.put("shopPic", shopPic);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, fosterEvaluteHandler);
            Utils.mLogError("==-->评价寄养 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 游泳订单详情
     *
     * @param phone
     * @param imei
     * @param context
     * @param id
     * @param handler
     */
    public static void querySwimOrderDetails(Context context, int id, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "order/orderDetail?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("id", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询游泳订单详情:" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 寄养订单详情
     *
     * @param context
     * @param id
     * @param handler
     */
    public static void hotelOrderInfo(Activity context, int id, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "hotel/orderInfo" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询寄养订单详情:" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelOrder(Context context,
                                   int id, String reason, String linkman, String telephone, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "care/cancelOrder?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("id", id);
            params.put("reason", reason);
            params.put("linkman", linkman);
            params.put("telephone", telephone);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->取消"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 寄养取消订单
     *
     * @param mContext
     * @param orderId
     * @param handler
     */
    public static void hotelCancel(Activity mContext, int orderId, String reason, String linkman, String telephone, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "hotel/cancel" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("orderId", orderId);
            if (!TextUtils.isEmpty(reason)) {
                params.put("reason", reason);
            }
            params.put("linkman", linkman);
            params.put("telephone", telephone);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->寄养取消订单：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 仅限升级订单支付使用
     *
     * @param phone
     * @param imei
     * @param context
     * @param id
     * @param customerId
     * @param customerName
     * @param customerMobile
     * @param remark
     * @param pickup
     * @param bathRequired
     * @param extraItemPrice
     * @param payPrice
     * @param payWay
     * @param couponId
     * @param debitAmount    混合支付时余额应支付金额，非混合支付时为-1
     * @param bathPetIds     洗澡宠物ids
     * @param isVie          标记是否加急订单----（升级订单不考虑一律用原来changeOrderPayMethodV2）
     * @param cardIds        次卡ids
     * @param time           时间
     * @param strp           与其他一致
     * @param canAmount      抵扣单子罐头币数
     * @param homeCouponId   上门专用优惠券
     * @param handler
     */
    public static void workerUpdatePay(String promoterCode,
                                       String phone, String imei, Context context, long id,
                                       long customerId, String customerName, String customerMobile,
                                       String remark, int pickup, int bathRequired, double extraItemPrice,
                                       double payPrice, int payWay, long couponId, double debitAmount,
                                       String bathPetIds, boolean isVie, String cardIds, String time,
                                       String strp, int canAmount, int homeCouponId, int serviceCardId,
                                       AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "worker/update/pay?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (promoterCode != null) {
                params.put("promoterCode", promoterCode);
            }
            params.put("orderId", id);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("customerId", customerId);
            params.put("payWay", payWay);
            if (bathRequired > -1)
                params.put("bathRequired", bathRequired);
            if (couponId > 0)
                params.put("couponId", couponId);
            if (customerName != null && !"".equals(customerName.trim()))
                params.put("customerName", customerName);
            if (customerMobile != null && !"".equals(customerMobile.trim()))
                params.put("customerMobile", customerMobile);
            if (remark != null && !"".equals(remark.trim()))
                params.put("remark", remark);
            if (pickup > -1)
                params.put("pickUp", pickup);
            if (extraItemPrice > -1)
                params.put("extraItemPrice", extraItemPrice);
            params.put("payPrice", payPrice);
            params.put("debitAmount", debitAmount);
            if (!TextUtils.isEmpty(bathPetIds)) {
                params.put("bathPetIds", bathPetIds);
            }
            if (!TextUtils.isEmpty(cardIds)) {
                params.put("cardIds", cardIds);
            }
            if (!TextUtils.isEmpty(time)) {
                params.put("appointment", time);
            }
            if (strp != null && !"".equals(strp)) {
                params.put("strp", strp);
            }
            if (canAmount > 0) {
                params.put("canAmount", canAmount);
            }
            if (homeCouponId > 0) {
                params.put("homeCouponId", homeCouponId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->订单二次支付"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 新洗美订单二次支付
     *
     * @param phone
     * @param imei
     * @param context
     * @param id
     * @param customerId
     * @param customerName
     * @param customerMobile
     * @param remark
     * @param pickup
     * @param bathRequired
     * @param extraItemPrice
     * @param payPrice
     * @param payWay
     * @param couponId
     * @param debitAmount    混合支付时余额应支付金额，非混合支付时为-1
     * @param bathPetIds     洗澡宠物ids
     * @param isVie          标记是否加急订单----（升级订单不考虑一律用原来changeOrderPayMethodV2）
     * @param cardIds        次卡ids
     * @param time           时间
     * @param strp           与其他一致
     * @param canAmount      抵扣单子罐头币数
     * @param homeCouponId   上门专用优惠券
     * @param handler
     */
    public static void changePayWayTwo(String promoterCode,
                                       String phone, String imei, Context context, long id,
                                       long customerId, String customerName, String customerMobile,
                                       String remark, int pickup, int bathRequired, double extraItemPrice,
                                       double payPrice, int payWay, long couponId, double debitAmount,
                                       String bathPetIds, boolean isVie, String cardIds, String time,
                                       String strp, int canAmount, int homeCouponId, int serviceCardId,
                                       AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "care/changePayWay?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (promoterCode != null) {
                params.put("promoterCode", promoterCode);
            }
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("orderId", id);
            params.put("customerId", customerId);
            params.put("payWay", payWay);
            if (bathRequired > -1)
                params.put("bathRequired", bathRequired);
            if (couponId > 0)
                params.put("couponId", couponId);
            if (customerName != null && !"".equals(customerName.trim()))
                params.put("customerName", customerName);
            if (customerMobile != null && !"".equals(customerMobile.trim()))
                params.put("customerMobile", customerMobile);
            if (remark != null && !"".equals(remark.trim()))
                params.put("remark", remark);
            if (pickup > -1)
                params.put("pickUp", pickup);
            if (extraItemPrice > -1)
                params.put("extraItemPrice", extraItemPrice);
            params.put("payPrice", payPrice);
            params.put("debitAmount", debitAmount);
            if (!TextUtils.isEmpty(bathPetIds)) {
                params.put("bathPetIds", bathPetIds);
            }
            if (!TextUtils.isEmpty(cardIds)) {
                params.put("cardIds", cardIds);
            }
            if (!TextUtils.isEmpty(time)) {
                params.put("appointment", time);
            }
            if (strp != null && !"".equals(strp)) {
                params.put("strp", strp);
            }
            if (canAmount > 0) {
                params.put("canAmount", canAmount);
            }
            if (homeCouponId > 0) {
                params.put("homeCouponId", homeCouponId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->订单二次支付:"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // /user/updateUserPushNotify
    public static void updateUserPushNotify(String imei, Context context,
                                            Short pushNotify, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/updateUserPushNotify?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("pushNotify", pushNotify);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->推送开关:"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * /pet/coupon/redeemCouponCode
     *
     * @param phone
     * @param imei
     * @param context
     * @param code
     * @param handler
     */
    public static void redeemCouponCode(String phone, String imei,
                                        Context context, String code, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "coupon/redeemCouponCode?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("code", code);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->优惠券兑换:"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFe(Context context, AsyncHttpResponseHandler handler) {
        String url = CommUtil.getServiceBaseUrlNew()
                + "care/specials?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            Utils.mLogError("特色服务：" + url);
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 店长主页
     *
     * @param context
     * @param handler
     */
    public static void queryManagerById(Context context, int id, AsyncHttpResponseHandler handler) {
        String url = CommUtil.getServiceBaseUrlNew() + "worker/queryManagerById?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("id", id);
            AsyncHttpClient client = new AsyncHttpClient();
            Utils.mLogError("店长主页：" + url);
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * /user/queryTradeHistory
     *
     * @param phone
     * @param imei
     * @param context
     * @param pageNum
     * @param handler
     */
    public static void queryTradeHistory(String phone, String imei,
                                         Context context, int pageNum, String beginDate, String endDate, int serviceCardId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/queryTradeHistory?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("beginDate", beginDate);
            params.put("endDate", endDate);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("pageSize", 10);
            params.put("pageNum", pageNum);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->账户余额记录 "
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // /worker/queryWorksByService
    public static void queryWorksByService(Context context, int workerid, long servieId,
                                           long before, int page, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "worker/queryWorksByService?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("workerId", workerid);
            if (servieId > 0) {
                params.put("serviceId", servieId);
            }
            params.put("before", before);
            params.put("page", page);
            params.put("pageSize", 10);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询美容师作品："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 美容师作品点赞接口
     *
     * @param context
     * @param type     type:1//固定值
     * @param relateId relateId：美容师作品id
     * @param handler
     */
    public static void praiseAdd(Context context, int type, int relateId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "praise/add?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("type", type);
            params.put("relateId", relateId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->美容师作品点赞接口：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单评价
     *
     * @param context
     * @param servieId    //服务Id 特色服务传一个就行 洗美不用
     * @param serviceType //服务类型(1:洗澡、2:美容、3:特色服务)
     * @param type        //服务分类(1:洗护、2:寄养、3:游泳、4:训犬)
     * @param page        //页码
     * @param hasImg      //有图(0:否、1:是)
     * @param handler
     */
    public static void queryCommentsByService(Context context, String servieId, int serviceType, int type, int page, int hasImg, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "comment/service?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (Utils.isStrNull(servieId)) {
                params.put("serviceId", servieId);
            }
            params.put("serviceType", serviceType);
            if (type > 0) {
                params.put("type", type);
            }
            params.put("page", page);
            if (hasImg >= 0) {
                params.put("hasImg", hasImg);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->订单评价：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shopCommentList(Activity mContext, int page, int hasImg, int shopId,
                                       AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "shop/comment/list" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("page", page);
            params.put("hasImg", hasImg);
            params.put("shopId", shopId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->门店评价" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 门店详情
     *
     * @param context
     * @param shopId
     * @param serviceId
     * @param handler
     */
    public static void GetShopInfo(Context context, int shopId,/* int serviceId,*/double lat, double lng,
                                   AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "shop/info?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("shopId", shopId);
//            if (serviceId>0){
//                params.put("serviceId", serviceId);
//            }
            if (lat > 0)
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            if (lng > 0)
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->门店详情：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 门店评价列表
     *
     * @param context
     * @param shopId
     * @param page
     * @param size
     * @param handler
     */
    public static void GetShopComment(Context context, int shopId, int page, int size,
                                      AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "comment/shop?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("shopId", shopId);
            params.put("page", page);
            params.put("size", size);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->门店评价：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryShopsWithCity(Context context, int shopId,double lat, double lng, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "shop/newList?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (shopId>0){
                params.put("shopId",shopId);
            }
            if (lat > 0)
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            if (lng > 0)
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->店铺列表："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // /user/queryCustomerPets
    public static void queryCustomerPets(Context context, int serviceid, int kind, int workerid,
                                         int ServiceType, int templateId, int shopId, int type, String mypetIds, int roomType, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "pet/queryCustomerPets?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (serviceid > 0) {
                params.put("serviceId", serviceid);
            }
            if (ServiceType > 0) {
                params.put("serviceType", ServiceType);
            }
            if (kind > 0)
                params.put("petKind", kind);
            if (workerid > 0)
                params.put("workerId", workerid);
            if (templateId > 0) {
                params.put("templateId", templateId);
            }
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            if (type > 0) {
                params.put("type", type);
            }
            if (Utils.isStrNull(mypetIds)) {
                params.put("mypetIds", mypetIds);
            }
            if (roomType > 0) {
                params.put("roomType", roomType);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->我的宠物 "
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void orderCancelRemind(Context context, int id, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "order/cancelRemind?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->通用取消订单 5.1.0 更新："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 按美容师姓名模糊搜索
     *
     * @param areaid
     * @param realName
     * @param page
     * @param pageSize
     * @param handler
     */
    public static void getWorkerByName(Context context, int areaid,
                                       String realName, int page, int pageSize,
                                       AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "worker/queryWorkersByName?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (areaid > 0 && areaid != 100)
                params.put("areaId", areaid);
            if (Utils.isStrNull(realName)) {
                params.put("realName", realName);
            } else {
                params.put("realName", "");
            }
            params.put("page", page);
            params.put("pageSize", pageSize);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("===>getWorkerByName====--->"
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 美容师首页进入详情
     *
     * @param context
     * @param page
     * @param handler
     */
    public static void getAllWorkers(Context context, int page, int shopId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "worker/allWorkers?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("page", page);
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->首页进去美容师列表 -->  "
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取服务item
     *
     * @param handler
     */
    public static void queryWorkerMenuItems(Context context, int workerid, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "worker/queryWorkerMenuItems?workerId=" + workerid + "&system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->获取美容师服务item -->  " + url);
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * /order/getResidualTimeOfPay
     *
     * @param context
     * @param phone
     * @param customerId
     * @param id
     * @param type
     * @param handler
     */
    public static void getResidualTimeOfPay(Context context, String phone,
                                            long customerId, long id, int type, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "certi/order/times?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("customerId", customerId);
            params.put("id", id);
            if (type != 20) {
                params.put("type", type);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->订单收银台计时："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ==-->订单收银台计时商城
     *
     * @param context
     * @param id
     * @param handler
     */
    public static void getMallTimeDown(Context context, long id, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "mall/order/remain?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", id);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->订单收银台计时商城："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * /vie/canBePickup 目前提示必要参数未填写
     *
     * @param context
     * @param phone
     * @param areaId      -- shopid
     * @param addressId
     * @param appointTime
     * @param serviceLoc
     * @param handler
     */
    public static void canBePickup(Context context, String phone, int areaId,
                                   int shopId, int addressId, String appointDate, String appointTime,
                                   int serviceLoc, double lat, double lng,
                                   AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "order/canBePickup?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (areaId > 0) {
                params.put("areaId", areaId);
            }
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            if (addressId > 0) {
                params.put("addressId", addressId);
            }
            if (!TextUtils.isEmpty(appointDate)) {
                params.put("appointDate", appointDate);
            }
            if (!TextUtils.isEmpty(appointTime)) {
                params.put("appointment", appointTime);
            }
            if (serviceLoc > 0) {
                params.put("serviceLoc", serviceLoc);
            }
            if (lat > 0) {
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            }
            if (lng > 0) {
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->isCanPickUp ："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 办理狗证订单信息
    public static void getOrderInfo(Context context, String cellphone,
                                    String imei, String currentVersion, int certiOrderId,
                                    AsyncHttpResponseHandler orderInfoHanler) {
        String url = getServiceBaseUrlNew()
                + "certi/order/info?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("id", certiOrderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->办理狗证订单信息："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, orderInfoHanler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 狗证订单二次支付
    public static void changePayWay(String promoterCode, boolean isHybirdPay,
                                    double balance, double needpayfee, String cellphone, String imei,
                                    Context context, int orderNo, int UserId, int paytype, int tcid,
                                    AsyncHttpResponseHandler changePayWayHanler) {
        String url = getServiceBaseUrlNew()
                + "certi/order/pay?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("id", orderNo);
            params.put("userId", UserId);
            params.put("payWay", paytype);
            params.put("certiCoupon", tcid);
            params.put("promoterCode", promoterCode);
            if (isHybirdPay) {
                if (Utils.isDoubleEndWithZero(balance)) {
                    params.put("debitAmount", Utils.formatDouble(balance));
                } else {
                    params.put("debitAmount", balance);
                }
            } else {
                params.put("debitAmount", -1);
            }
            if (Utils.isDoubleEndWithZero(needpayfee)) {
                params.put("payPrice", Utils.formatDouble(needpayfee));
            } else {
                params.put("payPrice", needpayfee);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->狗证订单二次支付 ："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, changePayWayHanler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadExtraMenus(Context context, String cellphone,
                                      AsyncHttpResponseHandler loadExtraMenusHanler) {
        String url = getServiceBaseUrlNew() + "user/loadExtraMenus?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->个人中心附加菜单 ："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, loadExtraMenusHanler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkRedeemCode(String redeemCode, int couponId,
                                       int fee, String serviceType, int shopId, int roomType,
                                       String petId, String cellphone, Context context, int serviceId,
                                       AsyncHttpResponseHandler checkRedeemCodeHanler) {
        String url = null;
        switch (serviceType) {
            case "SWIM":
                url = getServiceBaseUrlNew()
                        + "promoter/check/swim?system=" + getSource() + "_"
                        + Global.getCurrentVersion(context);
                break;
            case "HOTEL":
                url = getServiceBaseUrlNew()
                        + "promoter/check/hotel?system=" + getSource() + "_"
                        + Global.getCurrentVersion(context);
                break;
            case "TRAIN":
                url = getServiceBaseUrlNew()
                        + "promoter/check/train?system=" + getSource() + "_"
                        + Global.getCurrentVersion(context);
                break;
            case "PETCARD":
                url = getServiceBaseUrlNew()
                        + "promoter/check/certi?system=" + getSource() + "_"
                        + Global.getCurrentVersion(context);
                break;
        }
        try {
            RequestParams params = LocalParm(context);
//            params.put("redeemCode", redeemCode);
            params.put("promoterCode", redeemCode);
//            params.put("couponId", couponId);
            if (serviceType.equals("PETCARD")) {
                params.put("certiCouponId", couponId);
            }
            params.put("fee", fee);
            params.put("serviceType", serviceType);
            params.put("shopId", shopId);
            params.put("roomType", roomType);
            params.put("petId", petId);
            params.put("serviceId", serviceId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->" + serviceType + "校验邀请码 ："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, checkRedeemCodeHanler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkRedeemCodeMall(String inviteCode, Context context,
                                           AsyncHttpResponseHandler checkRedeemCodeHanler) {
        String url = getServiceBaseUrlNew()
                + "promoter/check?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("inviteCode", inviteCode);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->" + "校验邀请码 ："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, checkRedeemCodeHanler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkDhmCode(String cellphone, String serialNum,
                                    Context context, AsyncHttpResponseHandler checkDhmCodeHanler) {
        String url = getServiceBaseUrlNew() + "recharge/card/check?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("serialNum", serialNum);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->校验兑换码 ："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, checkDhmCodeHanler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rechargeWithCard(String cellphone, String serialNum,
                                        Context context, AsyncHttpResponseHandler rechargeWithCardHanler) {
        String url = getServiceBaseUrlNew() + "recharge/card/use?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("serialNum", serialNum);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->E卡充值 ："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, rechargeWithCardHanler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当中的帖子列表/pet/postInfo/queryPostInfoList
     *
     * @param phone
     * @param imei
     * @param context
     * @param groupId
     * @param page
     * @param handler
     */
    public static void queryPostInfoList(String phone, String imei,
                                         Context context, int groupId, int page, long timestamp,
                                         AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "postInfo/queryPostInfoList?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("groupId", groupId);
            params.put("page", page);
            if (page != 1) {
                params.put("timestamp", timestamp);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->帖子列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // /postInfo/newPost
    public static void newPost(String phone, Context context, int groupId,
                               String content, File[] imgs, File videos, File coverImgs,
                               int postType, int source, boolean isAnonymous, int imgWidth, int imgHeight,
                               AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "postInfo/newPost?system="
                + getSource() + "_" + Global.getCurrentVersion(context) + "&cellPhone=" + SharedPreferenceUtil.getInstance(context).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(context);
            if (postType == 1) {
                params.put("groupId", groupId);
            }
            if (content != null && !TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            if ((imgs == null || imgs.length <= 0) && videos != null) {
                params.put("video", videos);
                params.put("crop", ScreenUtil.getScreenWidth(context) + ":" + ScreenUtil.getScreenWidth(context));
            } else if ((imgs != null || imgs.length > 0) && videos == null) {
                params.put("img", imgs);
            }
            params.put("postType", postType);
            if (source != 0) {
                params.put("source", source);
                if (isAnonymous) {
                    params.put("isAnonymous", 1);
                } else {
                    params.put("isAnonymous", 0);
                }

            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOutVoi());
            Utils.mLogError("==-->发帖："
                    + client.getUrlWithQueryString(true, url, params));
            client.post(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.mLogError("==-->这里挂了");
        }
    }

    /**
     * 查询帖子详情 pet/postInfo/queryPostInfo
     *
     * @param phone
     * @param context
     * @param postId
     * @param page
     * @param handler
     */
    public static void queryPostInfo(String phone, Context context, int postId,
                                     int page, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "postInfo/queryPostInfo?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("postId", postId);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->查询帖子详情："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * /postInfo/queryGroups?cellPhone=18888888888&imei=869186023039594&system=
     * android_2.6.0 查询圈子
     *
     * @param phone
     * @param context
     * @param handler
     */
    public static void queryGroups(String phone, Context context,
                                   AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "postInfo/queryGroups?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->查询圈子："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入圈子 /pet/postInfo/followGroup
     *
     * @param phone
     * @param context
     * @param handler
     */
    public static void followGroup(String phone, Context context, int groupId,
                                   AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "postInfo/followGroup?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("groupId", groupId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->加入圈子："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 精选帖子列表
     *
     * @param context
     * @param page
     * @param isFollowed
     * @param isExistsVideo
     * @param isExistsImg
     * @param queryGoodPostsHandler
     */
    public static void queryGoodPosts(long timestamp, String phone,
                                      Context context, int page, int isFollowed, int isExistsVideo,
                                      int isExistsImg, AsyncHttpResponseHandler queryGoodPostsHandler) {
        String url = getServiceBaseUrlNew()
                + "postInfo/queryGoodPosts?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (page < 1) {
                params.put("page", 1);
            } else {
                params.put("page", page);
            }
            params.put("isFollowed", isFollowed);
            params.put("isExistsVideo", isExistsVideo);
            params.put("isExistsImg", isExistsImg);
            if (timestamp > 0 && page > 1) {
                params.put("timestamp", timestamp);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->精选帖子列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, queryGoodPostsHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 首页精选帖子列表
     *
     * @param context
     * @param page
     * @param isFollowed
     * @param isExistsVideo
     * @param isExistsImg
     * @param queryGoodPostsHandler
     */
    public static void queryHomeGoodPosts(long timestamp, String phone,
                                      Context context, int page, int isFollowed, int isExistsVideo,
                                      int isExistsImg, AsyncHttpResponseHandler queryGoodPostsHandler) {
        String url = getServiceBaseUrlNew()
                + "postInfo/petCircleHandpick?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (page < 1) {
                params.put("page", 1);
            } else {
                params.put("page", page);
            }
            params.put("isFollowed", isFollowed);
            params.put("isExistsVideo", isExistsVideo);
            params.put("isExistsImg", isExistsImg);
            if (timestamp > 0 && page > 1) {
                params.put("timestamp", timestamp);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->精选帖子列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, queryGoodPostsHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 关注用户
    public static void followUser(String phone, Context context, int userId,
                                  AsyncHttpResponseHandler followUserHandler) {
        String url = getServiceBaseUrlNew() + "postInfo/followUser?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("userId", userId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->关注："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, followUserHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 赠送鲜花，便便
     *
     * @param phone
     * @param context
     * @param postId
     * @param giftType
     * @param followUserHandler
     */
    public static void giftPost(String phone, Context context, int postId,
                                int giftType, AsyncHttpResponseHandler followUserHandler) {
        String url = getServiceBaseUrlNew() + "postInfo/giftPost?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("postId", postId);
            params.put("giftType", giftType);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->赠送鲜花，便便："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, followUserHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * /pet/postInfo/commentPost 提交评价
     *
     * @param phone
     * @param context
     * @param postId
     * @param content
     * @param commentPostHandler
     */
    public static void commentPost(int contentType, String phone,
                                   Context context, int postId, String content,
                                   AsyncHttpResponseHandler commentPostHandler) {
        String url = getServiceBaseUrlNew() + "postInfo/commentPost?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("postId", postId);
            params.put("contentType", contentType);
            params.put("content", content);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->提交评价："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, commentPostHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 启动页
     *
     * @param cellphone
     * @param context
     * @param startPageHandler
     */
    public static void startPage(String cellphone, Context context, int cityId, int isFirstLogin,
                                 AsyncHttpResponseHandler startPageHandler) {
        String url = getServiceBaseUrlNew() + "startPageConfig/startShowImg?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("cityId", cityId);
            params.put("isFirstLogin", isFirstLogin);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->启动页："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, startPageHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 个人中心
     *
     * @param cellphone
     * @param context
     * @param page
     * @param userId
     * @param getUserDataHandler
     */
    public static void getUserData(String cellphone, Context context, int page,
                                   int userId, AsyncHttpResponseHandler getUserDataHandler) {
        String url = getServiceBaseUrlNew() + "postInfo/getUserData?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("userId", userId);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->个人中心："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getUserDataHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 送花列表
     *
     * @param cellphone
     * @param context
     * @param page
     * @param getUserDataHandler
     */
    public static void followList(String cellphone, Context context, int page,
                                  int postId, AsyncHttpResponseHandler getUserDataHandler) {
        String url = getServiceBaseUrlNew() + "postInfo/followList?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("postId", postId);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->送花列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getUserDataHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * /pet/postInfo/deletePost
     *
     * @param cellphone
     * @param context
     * @param postId
     * @param deletePost
     */
    public static void deletePost(String cellphone, Context context,
                                  int postId, AsyncHttpResponseHandler deletePost) {
        String url = getServiceBaseUrlNew() + "postInfo/deletePost?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("postId", postId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->删除帖子："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, deletePost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新版洗美特色服务专用免责声明 次卡 罐头币 等等
     * pet/order/confirmOrderPrompt
     *
     * @param cellphone
     * @param context
     * @param orderType
     * @param cardIds            次卡ids
     * @param pickup             是否接送 0 / 1 请求罐头币用的洗美特色服务到店用的别的用不上
     * @param shopId             店铺id
     * @param confirmOrderPrompt
     */
    public static void confirmOrderPromptNew(String cellphone, Context context,
                                             int orderType, int serviceLoc, String strp, int workerId, int tid,
                                             String appointment, String endDate, String cardIds, int pickup, int shopId
            , int serviceCardId, int couponId, int homeCouponId, int updateOrderId,
                                             AsyncHttpResponseHandler confirmOrderPrompt) {
        String url = getServiceBaseUrlNew()
                + "appointment/confirmOrder?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("updateOrderId", updateOrderId);
            params.put("type", orderType);
            params.put("couponId", couponId);
            params.put("homeCouponId", homeCouponId);
            if (serviceLoc != 0) {
                params.put("serviceLoc", serviceLoc);
            }
            if (!TextUtils.isEmpty(strp)) {
                params.put("strp", strp);
            }
            if (workerId > 0) {
                params.put("workerId", workerId);
            }
            if (tid > 0) {
                if (tid == 10) {
                    tid = 1;
                } else if (tid == 20) {
                    tid = 2;
                } else if (tid == 30) {
                    tid = 3;
                }
                params.put("tid", tid);
            }
            if (!TextUtils.isEmpty(appointment)) {
                params.put("appointment", appointment);
            }
            if (!TextUtils.isEmpty(endDate)) {
                params.put("endDate", endDate);
            }
            if (!TextUtils.isEmpty(cardIds)) {
                params.put("cardIds", cardIds);
            }
            params.put("pickUp", pickup);
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->洗美特色服务新版新增免责声明：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, confirmOrderPrompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 粉丝，关注列表
     *
     * @param cellphone
     * @param context
     * @param page
     * @param type
     * @param userId
     * @param getUserDataHandler
     */
    public static void getUserFansOrfollow(String cellphone, Context context,
                                           int page, int type, int userId,
                                           AsyncHttpResponseHandler getUserDataHandler) {
        String url = getServiceBaseUrlNew()
                + "postInfo/getUserFansOrfollow?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("type", type);
            params.put("page", page);
            params.put("userId", userId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->粉丝，关注列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getUserDataHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * /pet/user/checkRechargeInviteCode
     *
     * @param cellphone
     * @param context
     * @param inviteCode
     */
    public static void checkRechargeInviteCode(String cellphone,
                                               Context context, String inviteCode, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "promoter/check?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (!TextUtils.isEmpty(inviteCode)) {
                params.put("inviteCode", inviteCode);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->充值前校验邀请码："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的界面获取菜单列表
     *
     * @param context
     * @param cellphone
     * @param loadExtraMenusHanler
     */
    public static void loadMenuNames(Context context, String cellphone,
                                     AsyncHttpResponseHandler loadExtraMenusHanler) {
        String url = getServiceBaseUrlNew() + "user/loadMenuNames?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->我的界面获取菜单列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, loadExtraMenusHanler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫码获取商品信息
     *
     * @param context
     * @param handler
     */
    public static void ScanCodePayment(String code, Context context,
                                       String cellphone, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "goods/prepay?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("code", code);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->扫码获取商品信息："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫码支付
     *
     * @param scanRemark
     * @param scanOrderId
     * @param isHybirdPay
     * @param paytype
     * @param balance
     * @param totalPayPrice
     * @param localGoods
     * @param localShopId
     * @param context
     * @param scanCodePaymentHandler
     */
    public static void ScanCodePay(String scanRemark, long scanOrderId,
                                   boolean isHybirdPay, double totalPayPrice, double balance,
                                   int paytype, String localGoods, int localShopId, Context context,
                                   String cellphone, int serviceCardId, AsyncHttpResponseHandler scanCodePaymentHandler) {
        String url = getServiceBaseUrlNew() + "goods/pay?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("goods", localGoods);
            params.put("shopId", localShopId);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("payPrice", totalPayPrice);
            if (scanOrderId > 0) {
                params.put("orderId", scanOrderId);
            } else {
                if (scanRemark != null && !TextUtils.isEmpty(scanRemark)) {
                    params.put("remark", scanRemark);
                }
            }
            params.put("payWay", paytype);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->扫码支付："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, scanCodePaymentHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量查询美容师
     *
     * @param context
     * @param workerIds
     * @param handler
     */
    public static void queryWorkersByIds(Context context, String workerIds,
                                         AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "worker/queryWorkersByIds?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (!TextUtils.isEmpty(workerIds)) {
                params.put("workerIds", workerIds);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->批量查询美容师："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void modifyOrder(Context context, int workerId,
                                   String appointment, int id, String customerName,
                                   String customerMobile, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "order/modifyOrder?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (workerId > 0) {
                params.put("workerId", workerId);
            }
            if (!TextUtils.isEmpty(appointment)) {
                params.put("appointment", appointment);
            }
            params.put("id", id);
            params.put("customerName", customerName);
            if (!TextUtils.isEmpty(customerMobile)) {
                params.put("customerMobile", customerMobile);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->改单 或 修改联系人："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.mLogError("==-->改单 或 修改联系人 请求接口挂了");
        }
    }

    /**
     * 改单是否有接送提示文案 modifyOrderCheck
     *
     * @param context
     * @param workerId
     * @param appointment
     * @param orderId
     * @param shopId
     * @param handler
     */
    public static void modifyOrderCheck(Context context, int workerId,
                                        String appointment, int orderId, int shopId,
                                        AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "order/modifyOrderCheck?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (workerId > 0) {
                params.put("workerId", workerId);
            }
            if (!TextUtils.isEmpty(appointment)) {
                params.put("appointment", appointment);
            }
            if (orderId > 0) {
                params.put("orderId", orderId);
            }
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->改单接送提示："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.mLogError("==-->改单接送提示");
        }
    }

    // 最近订单
    public static void choose(Context context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "feedback/choose?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("最近订单："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void commentStar(Context context, int grade, int orderId,
                                   AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "comment/commentStar?system="
                + getSource() + "_" + Global.getCurrentVersion(context);

        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->评价星级选择：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 投诉原因
    public static void reason(Context context, int feedType, int type,
                              int workLoc, AsyncHttpResponseHandler reasonHandler) {
        String url = getServiceBaseUrlNew() + "feedback/reason?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("feedType", feedType);
            params.put("type", type);
            params.put("workLoc", workLoc);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("投诉原因："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, reasonHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 提交投诉
    public static void save(Context context, int feedType, String reason,
                            String content, int orderId, AsyncHttpResponseHandler saveHandler) {
        String url = getServiceBaseUrlNew() + "feedback/save?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("feedType", feedType);
            params.put("reason", reason);
            params.put("content", content);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("提交投诉："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, saveHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 投诉历史
    public static void history(Context context, int feedType,
                               AsyncHttpResponseHandler historyHandler) {
        String url = getServiceBaseUrlNew() + "feedback/history?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("feedType", feedType);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("投诉历史："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, historyHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * pet/user/queryCustomerOrderComment
     *
     * @param context
     * @param page
     * @param Handler
     */
    public static void queryCustomerOrderComment(Context context, int page,
                                                 AsyncHttpResponseHandler Handler) {
        String url = getServiceBaseUrlNew()
                + "comment/queryCustomerOrderComment?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->查询我的评价 "
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, Handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 下发支付方式
    public static void payWays(Context context, String type, int orderId,
                               AsyncHttpResponseHandler payWaysHandler) {
        String url = getServiceBaseUrlNew() + "order/payWays?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("type", type);
            if (orderId > 0) {
                params.put("orderId", orderId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->下发支付方式 "
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, payWaysHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 订单投诉详情
    public static void detail(Context context, int orderId,
                              AsyncHttpResponseHandler detailHandler) {
        String url = getServiceBaseUrlNew() + "feedback/detail?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->订单投诉详情 "
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, detailHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * /new/user/queryLastOrderInfo
     *
     * @param context
     * @param serviceType 1 //服务类型（1:洗澡、2:美容）
     */
    public static void queryLastOrderInfo(Context context, int serviceType, int serviceId, int workerId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "appointment/queryLastOrderInfo?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (serviceType > 0) {
                params.put("serviceType", serviceType);
            }
            if (serviceId > 0) {
                params.put("serviceId", serviceId);
            }
            if (workerId > 0) {
                params.put("workerId", workerId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->请求最近一次用户订单信息："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * /new/service/queryServicePrice
     *
     * @param context
     * @param serviceType //服务类型（1:洗澡、2:美容、3:特色服务）
     * @param myPetIds    "166,165", //已选宠物ID
     * @param addressId   267, //地址ID
     * @param workerId    1020, //美容师ID
     * @param shopId      //门店ID，非必填，从上一单接口中获取
     * @param handler
     */
    public static void queryServicePrice(Context context, int serviceType,
                                         String strp, int addressId, int workerId, int shopId, int typeId, double lat, double lng, int serviceId,
                                         AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "appointment/queryServicePrice?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (serviceType > 0) {
                params.put("serviceType", serviceType);
            }
            if (!TextUtils.isEmpty(strp)) {
                params.put("strp", strp);
            }
            if (addressId > 0) {
                params.put("addressId", addressId);
            }
            if (workerId > 0) {
                params.put("workerId", workerId);
            }
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            if (typeId > 0) {
                params.put("typeId", typeId);
            }
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            if (serviceId > 0) {
                params.put("serviceId", serviceId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->新的服务方式选择界面："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 服务详情 调用条件：petId发生改变时调用
     *
     * @param context
     * @param serviceType "serviceId" : "1", //服务ID
     * @param petId       "petId" : "162,166", //犬种ID,非必填
     * @param shopId      "shopId" : "7" //门店ID,非必填
     * @param handler
     */
    public static void queryServiceDetail(Context context, int serviceType, String strp, int shopId, int serviceId,
                                          AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "appointment/queryServiceDetail?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (serviceType > 0) {
                params.put("serviceType", serviceType);
            }
            if (!TextUtils.isEmpty(strp)) {
                params.put("strp", strp);
            }
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            if (serviceId > 0) {
                params.put("serviceId", serviceId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->服务详情：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void getNewHomeData(Context context, int cityId, int isFirstLogin, double lat, double lng,
                                      AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/newHomePage?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("cityId", cityId);
            params.put("isFirstLogin", isFirstLogin);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("首页新街口："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void querySelectedWorkers(Context context,
                                            String appointment, int serviceLoc, int shopId, String strp,
                                            String workerIds, double lat, double lng, int serviceCardId,
                                            AsyncHttpResponseHandler querySelectedWorkersHandler) {
        String url = getServiceBaseUrlNew()
                + "worker/querySelectedWorkers?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (Utils.isStrNull(appointment)) {
                params.put("appointment", appointment);
            }
            if (Utils.isStrNull(workerIds)) {
                params.put("workerIds", workerIds);
            }
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            params.put("serviceLoc", serviceLoc);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("shopId", shopId);
            params.put("strp", strp);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("加载可预约美容师信息："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, querySelectedWorkersHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void queryExtraItems(Context context, int shopId, int serviceLoc, String strp, int serviceCardId, String appointment,
                                       AsyncHttpResponseHandler queryExtraItemsHandler) {
        String url = getServiceBaseUrlNew()
                + "appointment/queryExtraItems?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("strp", strp);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            if (Utils.isStrNull(appointment)) {
                params.put("appointment", appointment);
            }
            params.put("shopId", shopId);
            params.put("serviceLoc", serviceLoc);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("加载单项："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, queryExtraItemsHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void canBillPage(Context context, AsyncHttpResponseHandler canBillPageHandler) {
        String url = getServiceBaseUrlNew()
                + "canBill/canBillPage?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("罐头币页详情："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, canBillPageHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void canBillInOutRecord(Context context, int page, AsyncHttpResponseHandler canBillPageHandler) {
        String url = getServiceBaseUrlNew()
                + "canBill/canBillInOutRecord?system=" + getSource()
                + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("pageNum", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("罐头币收支明细："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, canBillPageHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 北京  上海 。。。这类区域列表
     *
     * @param context
     * @param parentId //地区编码
     * @param handler
     */
    public static void regionChildren(Context context, int parentId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/region/children?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (parentId > 0) {
                params.put("parentId", parentId);
            }
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("加载地区信息：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 增
     *
     * @param context
     * @param areaId    //地区ID
     * @param address   //详细地址
     * @param isDefault //是否默认，0:否、1:是
     * @param consigner //收货人
     * @param mobile    //联系电话
     * @param handler
     */
    public static void addAddress(Context context, int areaId, String areaName, String address, int isDefault, String consigner, String mobile, double lat, double lng, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/address/add?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("areaId", areaId);
            params.put("areaName", areaName);
            params.put("address", address);
            params.put("isDefault", isDefault);
            params.put("consigner", consigner);
            params.put("mobile", mobile);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("添加收货地址：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删
     *
     * @param context
     * @param addressId 对应结果对象的 id
     * @param handler
     */
    public static void deleteAddress(Context context, int addressId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/address/delete?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("addressId", addressId);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("删除地址：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 查
     *
     * @param context
     * @param addressId 地址ID
     * @param handler
     */
    public static void addressInfo(Context context, int addressId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/address/info?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("addressId", addressId);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取收货地址信息：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 改
     *
     * @param context
     * @param id        对应 结果中 id
     * @param areaId
     * @param address
     * @param isDefault
     * @param consigner
     * @param mobile
     * @param handler
     */
    public static void updateAddress(Context context, String areaName, int id, int areaId, String address, int isDefault, String consigner, String mobile, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/address/update?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            Log.e("TAG", "areaName = " + areaName);
            RequestParams params = LocalParm(context);
            params.put("id", id);
            params.put("areaId", areaId);
            params.put("areaName", areaName);
            params.put("address", address);
            params.put("isDefault", isDefault);
            params.put("consigner", consigner);
            params.put("mobile", mobile);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("更新地址：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 个人地址列表
     *
     * @param context
     * @param handler
     */
    public static void mallAddressList(Context context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/address/list?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取用户收货地址列表：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void myCart(Context mContext, AsyncHttpResponseHandler myCartHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/mallCart/myCart?system=" + getSource()
                + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("购物车列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, myCartHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteCartCommodity(Activity mContext, String cartIds, AsyncHttpResponseHandler deleteCartCommodityHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/mallCart/deleteCartCommodity?system=" + getSource()
                + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("cartIds", cartIds);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("删除购物车商品（单个或批量）："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, deleteCartCommodityHandler);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updateCartNum(Activity mContext, int cartId, int cartNum, AsyncHttpResponseHandler updateCartNumHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/mallCart/updateCartNum?system=" + getSource()
                + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("cartId", cartId);
            params.put("cartNum", cartNum);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("更新购物车商品个数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, updateCartNumHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void shopMallOrderReceive(Activity mContext, int orderId, AsyncHttpResponseHandler shopMallOrderReceiveHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/order/receive?system=" + getSource()
                + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("确认收货："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, shopMallOrderReceiveHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void shopMallOrderList(Activity mContext, int page, int state, AsyncHttpResponseHandler shopMallOrderListHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/order/list?system=" + getSource()
                + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("page", page);
            if (state >= 0) {
                params.put("state", state);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商品订单列表："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, shopMallOrderListHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void commodityInfo(Activity mContext, int mallCommodityId, AsyncHttpResponseHandler commodityInfoHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/commodity/info?system=" + getSource()
                + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("mallCommodityId", mallCommodityId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商品详情："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, commodityInfoHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void mallHomePage(Activity mContext, int cityId, int isFirstLogin, int petCategoryId, AsyncHttpResponseHandler mallHomePageHandler) {
        String url = getServiceBaseUrlNew()
                + "mallHomePage/homePage?system="
                + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("cityId", cityId);
            params.put("isFirstLogin", isFirstLogin);
            params.put("petCategoryId", petCategoryId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城首页："
                    + client.getUrlWithQueryString(true, url, params));
            client.post(url, params, mallHomePageHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addUserMallCart(Activity mContext, String mallCommodityStr, int type, AsyncHttpResponseHandler addUserMallCartHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/mallCart/addUserMallCart?system=" + getSource()
                + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("mallCommodityStr", mallCommodityStr);
            params.put("type", type);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("添加购物车："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, addUserMallCartHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param mContext
     * @param strp     //商品ID_数量
     * @param handler
     */
    public static void mallConfirmOrder(Activity mContext, String strp, int serviceCardId, int couponId, boolean fx_isOpen, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/order/confirm?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("strp", strp);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            if (couponId > 0) {
                params.put("couponId", couponId);
            }
            if (fx_isOpen) {
                params.put("reserveDiscount", 1);
            } else {
                params.put("reserveDiscount", 0);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("订单确认：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param mContext
     * @param payPrice    //应付金额
     * @param debitAmount //应付余额
     * @param strp        //商品ID_购买数量
     * @param payWay      //支付方式
     * @param addressId   //收货地址ID
     * @param handler
     */
    public static void mallOrderPay(Activity mContext, String promoterCode, int serviceCardId, int cityId, boolean fx_isOpen, double reserveDiscount, double payPrice, double debitAmount, String strp, int payWay, int addressId, int id, int couponId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/order/pay?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            if (Utils.isStrNull(promoterCode)) {
                params.put("promoterCode", promoterCode);
            }
            params.put("payPrice", payPrice);
            params.put("debitAmount", debitAmount);
            if (!TextUtils.isEmpty(strp)) {
                params.put("strp", strp);
            }
            params.put("payWay", payWay);
            if (addressId > 0) {
                params.put("addressId", addressId);
            }
            if (id > 0) {
                params.put("id", id);
            }
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            if (cityId > 0) {
                params.put("cityId", cityId);
            }
            if (fx_isOpen) {
                params.put("reserveDiscount", reserveDiscount);
            }
            if (couponId > 0) {
                params.put("couponId", couponId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城二次下单：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 搜索界面历史记录热门搜索icon
     *
     * @param mContext
     * @param handler
     */
    public static void mallSearchPage(Activity mContext, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/searchPage?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("搜索界面历史记录热门搜索：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 品牌之流
     *
     * @param mContext
     * @param classificationId 这个是品牌 产地 上边那一级分类的id
     * @param content          这个搜索专用
     * @param handler
     */
    public static void mallQueryPublicAttributeList(Activity mContext, int classificationId, String content, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/queryPublicAttributeList?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            if (classificationId > -1) {
                params.put("classificationId", classificationId);
            }
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("主页落地页tag：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * //所有一下参数  有就传，没有就不传
     * 商品列表
     *
     * @param mContext
     * @param classificationId classificationId=1      //分类id   必要参数
     * @param publicAttribute  publicAttribute=1,2,3,4,        //公共属性id  中间用“,”分割
     * @param content          content=“123”       //搜索内容
     * @param saleAmount       saleAmount=0        //销量排序  0倒序 1正序
     * @param ePrice      retailPrice=12      //价格排序  0倒序 1正序
     * @param handler
     */
    public static void mallSearchCommodityList(Activity mContext, int classificationId, String publicAttribute, String content, int saleAmount, int ePrice, int page, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/searchCommodityList?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            params.put("cityId", SharedPreferenceUtil.getInstance(mContext).getInt(
                    "cityId", 0));
            if (classificationId > -1) {
                params.put("classificationId", classificationId);
            }
            if (!TextUtils.isEmpty(publicAttribute)) {
                params.put("publicAttribute", publicAttribute);
            }
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            if (saleAmount > -1) {
                params.put("saleAmount", saleAmount);
            }
            if (ePrice > -1) {
                params.put("ePrice", ePrice);
            }
            params.put("page", page);
            Utils.mLogError("搜索商品列表页：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void mallOrderInfo(Activity mContext, int orderId, AsyncHttpResponseHandler mallOrderInfoHandler) {
        String url = getServiceBaseUrlNew() + "mall/order/info?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城订单详情：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, mallOrderInfoHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 商城分类查询接口 顶部几级分类
     *
     * @param mContext
     * @param classificationId
     * @param mallOrderInfoHandler
     */
    public static void mallMallCommodity(Activity mContext, int classificationId, AsyncHttpResponseHandler mallOrderInfoHandler) {
        String url = getServiceBaseUrlNew() + "mallCommodityClassification/getMallCommodityClassificationList?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("classificationId", classificationId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城顶部猫狗 以及 下方二级分类 ：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, mallOrderInfoHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 商城更新热门搜索记录
     *
     * @param mContext
     * @param hotSearchId
     * @param mallOrderInfoHandler
     */
    public static void mallUpdateHotSearch(Activity mContext, int hotSearchId, AsyncHttpResponseHandler mallOrderInfoHandler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/updateHotSearch?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            if (hotSearchId > 0) {
                params.put("hotSearchId", hotSearchId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城更新热门搜索记录：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, mallOrderInfoHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 商城添加搜索历史
     *
     * @param mContext
     * @param content
     * @param source               热门搜索传1 其他不传
     * @param mallOrderInfoHandler
     */
    public static void mallAddSearchHistory(Activity mContext, String content, int source, AsyncHttpResponseHandler mallOrderInfoHandler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/addSearchHistory?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            if (source > 0) {
                params.put("content", content);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城添加搜索历史：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, mallOrderInfoHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 商城删除搜索历史
     *
     * @param mContext
     * @param mallOrderInfoHandler
     */
    public static void mallDeleteSearchHistory(Activity mContext, AsyncHttpResponseHandler mallOrderInfoHandler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/deleteSearchHistory?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城删除搜索历史：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, mallOrderInfoHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 登录情况下精准热销商品
     *
     * @param mContext
     * @param type
     * @param page
     * @param commodityId
     * @param getrRecommendDataHandler
     */
    public static void getrRecommendData(Activity mContext, int type, int page, int commodityId, AsyncHttpResponseHandler getrRecommendDataHandler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/recommendedForYou?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("type", type);
            params.put("page", page);
            if (type == 1 && commodityId > 0) {
                params.put("commodityId", commodityId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("为你推荐登录情况下精准热销商品  ：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getrRecommendDataHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 未登录情况下系统热销商品
     *
     * @param mContext
     * @param type
     * @param page
     * @param commodityId
     * @param getrRecommendDataHandler
     */
    public static void getrecommendedForYouForMinePage(Activity mContext, int type, int page, int commodityId, AsyncHttpResponseHandler getrRecommendDataHandler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/recommendedForYouForMyPage?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("type", type);
            params.put("page", page);
            if (type == 1 && commodityId > 0) {
                params.put("commodityId", commodityId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("为你推荐  未登录情况下系统热销商品：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getrRecommendDataHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void queryOrderAndCart(Activity mContext, AsyncHttpResponseHandler queryOrderAndCartHandler) {
        String url = getServiceBaseUrlNew() + "mallHomePage/queryOrderAndCart?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("查询购物车数量：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, queryOrderAndCartHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 商城搜索联想
     *
     * @param mContext
     * @param queryOrderAndCartHandler
     */
    public static void mallSearchAssociation(Activity mContext, String content, AsyncHttpResponseHandler queryOrderAndCartHandler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/searchAssociation?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("商城搜索联想：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, queryOrderAndCartHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String OtherUrl(Activity mContext) {
        return "?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
    }

    public static void operateBanner(Activity mContext, int type, AsyncHttpResponseHandler operateBannerHandler) {
        String url = getServiceBaseUrlNew() + "operateBanner/list?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("type", type);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("运营中部banner：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, operateBannerHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getActivityPage(Activity mContext, int cityId, int isFirstLogin, int activityPage,double lat,double lng, AsyncHttpResponseHandler getActivityPage) {
        String url = getServiceBaseUrlNew() + "activityPage/getActivityPage?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (Utils.checkLogin(mContext)){
                params.put("cityId", cityId);
            }else {
                params.put("cityId", 0);
            }
            params.put("isFirstLogin", isFirstLogin);
            params.put("activityPage", activityPage);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("弹框：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getActivityPage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void floatingInfo(Activity mContext, int cityId, int isFirstLogin, int position, AsyncHttpResponseHandler getActivityPage) {
        String url = getServiceBaseUrlNew() + "home/floating/info?system=" + getSource() + "_" + Global.getCurrentVersion(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("cityId", cityId);
            params.put("isFirstLogin", isFirstLogin);
            params.put("position", position);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("浮窗接口：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getActivityPage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void allOrder(Activity context, int status, int page, AsyncHttpResponseHandler ingWorkerOrders) {
        String url = getServiceBaseUrlNew() + "order/allOrder?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("page", page);
            params.put("status", status);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, ingWorkerOrders);
            Utils.mLogError("==-->洗美订单列表："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 商城优惠券列表
     *
     * @param context
     * @param handler
     */
    public static void myMallCoupons(Activity context, String strp, int serviceCardId, double payPrice, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "coupon/myMallCoupons" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("strp", strp);
            params.put("serviceCardId", serviceCardId);
            params.put("payPrice", payPrice);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->商城订单优惠券列表：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单支付成功页推荐的商品
     *
     * @param context
     * @param pageType
     * @param orderId
     * @param handler
     */
    public static void orderPaySuccessCommodityInfo(Activity context, int pageType, int orderId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/orderPaySuccessCommodityInfo" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            params.put("pageType", pageType);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->订单支付成功页推荐的商品：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getMyCouponAll(String phone, String imei, Context context, int page, int status, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew()
                + "coupon/mine?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("page", page);
            params.put("status", status);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询可用优惠券："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getLogisticsData(Context context, int logisticsId, AsyncHttpResponseHandler getLogisticsDataHandler) {
        String url = getServiceBaseUrlNew()
                + "mall/logitics/logisticsDetails?system=" + getSource() + "_"
                + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            params.put("logisticsId", logisticsId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getLogisticsDataHandler);
            Utils.mLogError("==-->查询物流详情："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计-新增（用于客户端）
     *
     * @param context
     * @param typeid   //页面名id （对应logcount_type表id）(必传参数)
     * @param activeid //对应页面的统计处id（对应logcount_type表id）(必传参数)
     * @param handler
     */
    public static void logcountAdd(Activity context, String typeid, String activeid, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "logcount/add" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("typeid", typeid);
            params.put("activeid", activeid);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->统计-新增（用于客户端）：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 账户中心
     *
     * @param context
     * @param handler
     */
    public static void accountCenter(Activity context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/accountCenter" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->账户中心：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPayPwd(Activity context, String payPwd,String securityCard, AsyncHttpResponseHandler setPayPwdHandler) {
        String url = getServiceBaseUrlNew() + "user/setPayPwd" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("payPwd", payPwd);
            params.put("securityCard", securityCard);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, setPayPwdHandler);
            Utils.mLogError("==-->设置支付密码：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的界面
     *
     * @param context
     * @param handler
     */
    public static void myPage(Activity context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/myPage" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->我的界面new顶部个人信息 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的界面洗美特色服务订单
     *
     * @param context
     * @param handler
     */
    public static void myOrder(Activity context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "order/myOrder" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->我的界面洗美特色服务订单 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的界面寄养订单
     *
     * @param context
     * @param handler hotel/myOrder
     */
    public static void hotelMyOrder(Activity context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "hotel/myOrder" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->我的界面寄养订单 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的页面商城订单
     *
     * @param context
     * @param handler
     */
    public static void mallOrderForMyPage(Activity context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/order/mallOrderForMyPage" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(context).getString(
                    "mallLng", "")));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->我的页面商城订单 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetSecurityCard(Activity context, String securityCard, AsyncHttpResponseHandler checkSparePayPhoneHandler) {
        String url = getServiceBaseUrlNew() + "user/resetSecurityCard" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("securityCard", securityCard);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, checkSparePayPhoneHandler);
            Utils.mLogError("==-->校验备用手机号：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disposePayPwd(Activity context, String payPwd, String newPayPwd, int status, AsyncHttpResponseHandler setNewPayPwdHandler) {
        String url = getServiceBaseUrlNew() + "user/disposePayPwd" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("status", status);
            if (status == 2 || status == 3) {
                params.put("newPayPwd", newPayPwd);
            }
            if (status != 3) {
                params.put("payPwd", payPwd);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, setNewPayPwdHandler);
            Utils.mLogError("==-->设置新支付密码：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的消息列表页面
     *
     * @param context
     * @param handler
     */
    public static void pushMessageList(Activity context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/pushMessageList" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->我的消息列表页面 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新我的消息阅读状态
     *
     * @param context
     * @param handler
     */
    public static void updatePushMessageReadState(Context context, String pushMessageId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/updatePushMessageReadState" + "?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("pushMessageId", pushMessageId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->更新我的消息阅读状态 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateImageCode(Context context, String mobileKey, AsyncHttpResponseHandler generateImageCodeHandler) {
        String url = getServiceBaseUrlNew() + "user/generateImageCode" + "?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("mobileKey", mobileKey);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, generateImageCodeHandler);
            Utils.mLogError("==-->生成图片验证码 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启语音验证
     *
     * @param context
     * @param handler
     */
    public static void isOpenVoiceVerification(Context context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/isOpenVoiceVerification" + "?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->开启语音验证 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pictureOnUser(Context context, AsyncHttpResponseHandler pictureOnUserHandler) {
        String url = getServiceBaseUrlNew() + "user/pictureOnUser" + "?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, pictureOnUserHandler);
            Utils.mLogError("==-->校验是否开启图片验证码 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTaskSchedule(Context context, int petId, int shopId,
                                       AsyncHttpResponseHandler dateAddress) {
        String url = getServiceBaseUrlNew() + "hotel/getTaskSchedule" + "?system=" + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("petId", petId);
            params.put("shopId", shopId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, dateAddress);
            Utils.mLogError("==-->寄养时间格子新接口 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CareTimes(Activity context, int orderId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "care/times" + OtherUrl(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->洗美倒计时接口 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void couponShare(Activity mContext, int couponId, AsyncHttpResponseHandler couponShareHandler) {
        String url = getServiceBaseUrlNew() + "coupon/share" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("couponId", couponId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, couponShareHandler);
            Utils.mLogError("==-->优惠券分享回调 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void myPageBanner(Activity mContext, AsyncHttpResponseHandler myPageBannerHanler) {
        String url = getServiceBaseUrlNew() + "user/myPageBanner" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, myPageBannerHanler);
            Utils.mLogError("==-->我的页面banner ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadFreshManPage(Activity mContext, int activityId, AsyncHttpResponseHandler loadFreshManPageHanler) {
        String url = getServiceBaseUrlNew() + "freshMan/loadFreshManPage" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("activityId", activityId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, loadFreshManPageHanler);
            Utils.mLogError("==-->加载新手任务页面 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void receiveReward(Activity mContext, int activityId, int taskId, AsyncHttpResponseHandler receiveRewardHanler) {
        String url = getServiceBaseUrlNew() + "freshMan/receiveReward" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("activityId", activityId);
            params.put("taskId", taskId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, receiveRewardHanler);
            Utils.mLogError("==-->新手任务领取奖励 ：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addActivityLog(Activity mContext, int activityInfoId, int shareActivityId, int taskId,
                                      AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "logcount/addActivityLog" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("activityInfoId", activityInfoId);
            params.put("shareActivityId", shareActivityId);
            params.put("taskId", taskId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->统计-新增(pu/uv统计-用于运营活动(新手任务,邀请有礼)) ：" +
                    client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getEncyCommentList(Activity mContext, int infoId, int page, AsyncHttpResponseHandler getEncyCommentListHandler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/comment/list" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("infoId", infoId);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getEncyCommentListHandler);
            Utils.mLogError("百科评论列表 ：" +
                    client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void PostEncyComment(Activity mContext, int infoId, String content, AsyncHttpResponseHandler postEncyCommentHandler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/comment/add" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("infoId", infoId);
            params.put("content", content);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, postEncyCommentHandler);
            Utils.mLogError("百科发表评论 ：" +
                    client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 百科详情
     *
     * @param mContext
     * @param infoId
     * @param addActivityLogHanler
     */
    public static void encyclopediaInfo(Activity mContext, int infoId,
                                        AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/info" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("infoId", infoId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->百科详情 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryEncyclopediaClassification(Activity mContext, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/queryEncyclopediaClassification" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->百科分类 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryUserCollectionList(Activity mContext, int page, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/queryUserCollectionList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询用户收藏列表 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUserCollection(Activity mContext, String encyclopediaId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/deleteUserCollection" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("encyclopediaId", encyclopediaId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->删除用户收藏 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryEncyclopediaTopList(Activity mContext, int classificationId, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/queryEncyclopediaTopList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("classificationId", classificationId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询百科置顶列表 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryEncyclopediaList(Activity mContext, int page, int classificationId, String content, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/queryEncyclopediaList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("page", page);
            if (Utils.isStrNull(content)) {
                params.put("content", content);
            } else {
                params.put("classificationId", classificationId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询百科列表（搜索） " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 百科点赞
     *
     * @param mContext
     * @param encyclopediaId
     * @param addActivityLogHanler
     */
    public static void encyclopediaThumbsUp(Activity mContext, int encyclopediaId,
                                            AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/thumbsUp" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("encyclopediaId", encyclopediaId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->百科点赞 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 百科收藏
     *
     * @param mContext
     * @param encyclopediaId
     * @param addActivityLogHanler
     */
    public static void encyclopediaCollection(Activity mContext, int encyclopediaId,
                                              AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/collection" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("encyclopediaId", encyclopediaId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->百科收藏 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询百科热门搜索列表
     *
     * @param mContext
     * @param encyclopediaId
     * @param addActivityLogHanler
     */
    public static void encyclopediaHotSearchList(Activity mContext,
                                                 AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/hotSearchList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->查询百科热门搜索列表 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加百科搜索历史
     *
     * @param mContext
     * @param source
     * @param content
     * @param addActivityLogHanler
     */
    public static void encyclopediaAddSearchLog(Activity mContext, int source, String content,
                                                AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/addSearchLog" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("source", source);
            params.put("content", content);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->添加百科搜索历史 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询百科列表（搜索）
     *
     * @param mContext
     * @param page
     * @param classificationId
     * @param content
     * @param addActivityLogHanler
     */
    public static void encyclopediaQueryEncyclopediaList(Activity mContext, int page,int limit, int classificationId, String content,
                                                         AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/queryEncyclopediaList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("page", page);
            if (classificationId > 0) {
                params.put("classificationId", classificationId);
            }
            if (limit>0){
                params.put("limit",limit);
            }
            params.put("content", content);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->查询百科列表（搜索） " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 百科分享
     *
     * @param mContext
     * @param encyclopediaId       //帖子id
     * @param channel              //渠道    0:微信 1:qq空间 2:微博
     * @param addActivityLogHanler
     */
    public static void encyclopediaShare(Activity mContext, int encyclopediaId, int channel,
                                         AsyncHttpResponseHandler addActivityLogHanler) {
        String url = getServiceBaseUrlNew() + "encyclopedia/share" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("encyclopediaId", encyclopediaId);
            params.put("channel", channel);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addActivityLogHanler);
            Utils.mLogError("==-->百科分享 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryCustomerPetDiaryById(Activity mContext, int pageSize, int page, int id, String month, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "pet/queryCustomerPetDiaryById" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("pageSize", pageSize);
            params.put("page", page);
            params.put("id", id);
            params.put("monthHistory", month);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->宠物日记 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void customerPetImg(Activity mContext, int customerpetid, int page, int pageSize, AsyncHttpResponseHandler customerPetImgHandler) {
        String url = getServiceBaseUrlNew() + "pet/customerPetImg" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("pageSize", pageSize);
            params.put("page", page);
            params.put("id", customerpetid);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, customerPetImgHandler);
            Utils.mLogError("==-->宠物照片墙 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * E卡列表区分城市
     *
     * @param mContext
     * @param giftcardListCityHandler
     */
    public static void getGiftCardListCity(Activity mContext, double lat, double lng, AsyncHttpResponseHandler giftcardListCityHandler) {
        String url = getServiceBaseUrlNew() + "/serviceCardTemplate/list" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (lat > 0)
                params.put("lat", lat == 4.9E-324 ? 0 : lat);
            if (lng > 0)
                params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giftcardListCityHandler);
            Utils.mLogError("==-->E卡列表加城市 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * E卡列表Banner
     *
     * @param mContext
     * @param giftcardListBannerHandler
     */
    public static void getGiftCardListBanner(Activity mContext, int type, AsyncHttpResponseHandler giftcardListBannerHandler) {
        String url = getServiceBaseUrlNew() + "/otherOperateBanner/list" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("type", type);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giftcardListBannerHandler);
            Utils.mLogError("==-->E卡列表Banner " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * E卡详情
     *
     * @param mContext
     * @param giftcardDetailHandler
     */
    public static void getGiftCardDetail(Activity mContext, int cardTemplateId, AsyncHttpResponseHandler giftcardDetailHandler) {
        String url = getServiceBaseUrlNew() + "/serviceCardTemplate/serviceCardTemplateDetail" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("cardTemplateId", cardTemplateId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giftcardDetailHandler);
            Utils.mLogError("==-->E卡详情 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void myServiceCardList(Activity mContext, int isEnable, AsyncHttpResponseHandler cardListHandler) {
        String url = getServiceBaseUrlNew() + "user/myServiceCardList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("isEnable", isEnable);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, cardListHandler);
            Utils.mLogError("==-->我的E卡列表页 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindCard(String cardPwd, int confirm, Activity mContext, AsyncHttpResponseHandler bindCardHanler) {
        String url = getServiceBaseUrlNew() + "user/serviceCard/bind" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("cardPwd", cardPwd);
            params.put("confirm", confirm);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, bindCardHanler);
            Utils.mLogError("==-->绑卡 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void serviceCardList(Activity mContext, int shopId, int type, double payPrice, String orderKey, String appointment, AsyncHttpResponseHandler cardListHandler) {
        String url = getServiceBaseUrlNew() + "user/serviceCard/serviceCardList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("type", type);//类型(0洗美特 1寄养 2店销 4付款二维码 5线上商城)
            if (Utils.isStrNull(appointment)) {
                params.put("appointment", appointment);
            }
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            params.put("orderKey", orderKey);
            if (type == 5 || type == 0) {//商城和洗美特不传payPrice
            } else {
                params.put("payPrice", payPrice);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, cardListHandler);
            Utils.mLogError("==-->用户服务卡列表 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cardRecord(Activity mContext, int serviceCardId, AsyncHttpResponseHandler cardListHandler) {
        String url = getServiceBaseUrlNew() + "user/serviceCardTemplate/myAccountCardUseRecord" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, cardListHandler);
            Utils.mLogError("==-->我的余额卡/服务卡使用明细 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void myServiceCardOrderDetail(Activity mContext, int serviceCardId, AsyncHttpResponseHandler cardDetailHandler) {
        String url = getServiceBaseUrlNew() + "user/serviceCardTemplate/myServiceCardOrderDetail" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, cardDetailHandler);
            Utils.mLogError("==-->我的服务卡订单详情 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refundCard(Activity mContext, int serviceCardId, double refundPrice, AsyncHttpResponseHandler refundCardHandler) {
        String url = getServiceBaseUrlNew() + "user/serviceCard/refund" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            params.put("refundPrice", refundPrice);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, refundCardHandler);
            Utils.mLogError("==-->退卡 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refundCancel(Activity mContext, int serviceCardId, AsyncHttpResponseHandler refundCancelHanler) {
        String url = getServiceBaseUrlNew() + "user/serviceCard/refundCancel" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, refundCancelHanler);
            Utils.mLogError("==-->取消服务卡退款 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refundDetail(Activity mContext, int serviceCardId, AsyncHttpResponseHandler refundDetailHanler) {
        String url = getServiceBaseUrlNew() + "user/serviceCardTemplate/refundDetail" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (serviceCardId > 0) {
                params.put("serviceCardId", serviceCardId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, refundDetailHanler);
            Utils.mLogError("==-->退卡进度说明 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void serviceCardOrderList(Activity mContext, int state, int page, AsyncHttpResponseHandler serviceCardOrderListHanler) {
        String url = getServiceBaseUrlNew() + "/user/serviceCardTemplate/myServiceCardOrderList" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (state == 0) {
                params.put("state", -1);
            } else if (state == 1) {
                params.put("state", 3);
            } else if (state == 3) {
                params.put("state", 1);
            } else {
                params.put("state", state);
            }
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, serviceCardOrderListHanler);
            Utils.mLogError("==-->我的服务卡订单列表 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void buyServiceCard(Activity mContext, int templateId, int amount, int payWay, double payPrice, String inviteCode, AsyncHttpResponseHandler buyServiceCard) {
        String url = getServiceBaseUrlNew() + "/user/serviceCard/pay" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("templateId", templateId);
            params.put("amount", amount);
            params.put("payWay", payWay);
            params.put("payPrice", payPrice);
            params.put("inviteCode", inviteCode);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, buyServiceCard);
            Utils.mLogError("==-->购买E卡 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ReceiveCoupon(Activity mContext, AsyncHttpResponseHandler receiveCouponHandler) {
        String url = getServiceBaseUrlNew() + "coupon/receive/registerCoupon" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, receiveCouponHandler);
            Utils.mLogError("==-->领取优惠券 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFosterListBanner(Activity mContext, int type, int useStyle, int cityId, int shopId, int isFirstLogin, AsyncHttpResponseHandler giftcardListBannerHandler) {
        String url = getServiceBaseUrlNew() + "/otherOperateBanner/list" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("type", type);
            params.put("useStyle", useStyle);
            params.put("shopId", shopId);
            params.put("cityId", cityId);
            params.put("isFirstLogin", isFirstLogin);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giftcardListBannerHandler);
            Utils.mLogError("==-->寄养首页Banner " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void giveCouponNew(Activity mContext, int orderId, AsyncHttpResponseHandler giveCouponHandler) {
        String url = getServiceBaseUrlNew() + "coupon/order/giveCoupon" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giveCouponHandler);
            Utils.mLogError("==-->下单赠送优惠券 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询订单明细 洗美特色服务专用 其他的用老的
     *
     * @param context
     * @param id
     * @param handler
     */
    public static void queryOrderDetailsNewTwo(Context context, int id, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "/order/info?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->查询:"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void workerInfo(Activity mContext, int workerId, AsyncHttpResponseHandler giveCouponHandler) {
        String url = getServiceBaseUrlNew() + "worker/loadWorkerById" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("workerId", workerId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giveCouponHandler);
            Utils.mLogError("==-->美容师信息 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gratuityPay(Activity mContext, int workerId, int sourceType, int payWay, double payPrice, int orderId, String remark, AsyncHttpResponseHandler giveCouponHandler) {
        String url = getServiceBaseUrlNew() + "gratuityOrder/newOrder" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("workerId", workerId);
            params.put("sourceType", sourceType);
            params.put("payWay", payWay);
            params.put("payPrice", payPrice);
            params.put("orderId", orderId);
            params.put("remark", remark);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giveCouponHandler);
            Utils.mLogError("==-->打赏支付 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCouponDetails(Activity mContext, int id, AsyncHttpResponseHandler authCodeHandler) {
        String url = getServiceBaseUrlNew() + "coupon/queryCouponDetails?system="
                + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            if (id > 0) {
                params.put("id", id);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, authCodeHandler);
            Utils.mLogError("==-->优惠券详情 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取支付二维码内容(商城优惠券,返现,服务卡)
     *
     * @param mContext
     * @param type                                                   //类型:11服务卡;12返现;3优惠券;
     * @param cardId//type=11为服务卡ID,type=3时为优惠券ID,type=12返现时字段值为空或不传
     */
    public static void getauthCode(Activity mContext, int type, int cardId, AsyncHttpResponseHandler authCodeHandler) {
        String url = getServiceBaseUrlNew() + "s_mall/authCode?system="
                + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("type", type);
            if (cardId != 0) {
                params.put("cardId", cardId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, authCodeHandler);
            Utils.mLogError("==-->获取支付二维码 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void orderExamineApply(Activity mContext, int orderId, int category, String reason, String remark, File[] img, AsyncHttpResponseHandler authCodeHandler) {
        String url = getServiceBaseUrlNew() + "mallOrderExamine/mallOrderExamineApply?system="
                + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            params.put("orderId", orderId);
            params.put("category", category);
            params.put("reason", reason);
            params.put("remark", remark);
            Utils.mLogError("长度=====" + img.length);
            if (img != null && img.length > 0) {
                params.put("img", img);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, authCodeHandler);
            Utils.mLogError("==-->申请退换货 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shopOrderCancel(Activity mContext, int orderId, String reason, AsyncHttpResponseHandler giveCouponHandler) {
        String url = getServiceBaseUrlNew() + "mall/order/cancel" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lat", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLat", "")));
            params.put("lng", Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")) == 4.9E-324 ? 0 : Double.valueOf(SharedPreferenceUtil.getInstance(mContext).getString(
                    "mallLng", "")));
            params.put("orderId", orderId);
            params.put("reason", reason);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, giveCouponHandler);
            Utils.mLogError("==-->商城订单取消 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void backCashBillHomePage(Activity mContext, int state, int currentPage, int itemCount, AsyncHttpResponseHandler authCodeHandler) {
        String url = getServiceBaseUrlNew() + "user/myAccountBackCashBillHomePage?system="
                + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            params.put("state", state);
            params.put("currentPage", currentPage);
            params.put("itemCount", itemCount);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, authCodeHandler);
            Utils.mLogError("==-->返现主页 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getShopRefundShdule(Activity mContext, int orderId, AsyncHttpResponseHandler authCodeHandler) {
        String url = getServiceBaseUrlNew() + "mallOrderExamine/mallOrderExamineApplySpeed?system="
                + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "cellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, authCodeHandler);
            Utils.mLogError("==-->获取商品退款进度 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFosterLiveList(Activity mContext, AsyncHttpResponseHandler getFosterLiveListHandler) {
        String url = getServiceBaseUrlNew() + "hotel/queryLiveRooms" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getFosterLiveListHandler);
            Utils.mLogError("==-->寄养直播列表 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFosterAvailableCoupon(Activity mContext, int shopId, int type, int mypetId,
                                                int roomType, String startTime, String endTime, int cardId, int category,
                                                double payPrice, AsyncHttpResponseHandler couponHanler) {
        String url = getServiceBaseUrlNew() + "coupon/hotel/match" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("shopId", shopId);
            params.put("type", type);
            params.put("mypetId", mypetId);
            params.put("roomType", roomType);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            if (cardId > 0) {
                params.put("serviceCardId", cardId);
            }
            params.put("category", category);
            params.put("payPrice", payPrice);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, couponHanler);
            Utils.mLogError("==-->寄养优惠券列表 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 寄养支付
     */
    public static void hotelPay(Activity mContext, int careShopId, int id, int shopId, int type, int mypetId, String strp, int roomType,
                                int couponId, String startTime, String endTime, int cardId, double payPrice, int paytype,
                                String remark, String contact, String contactPhone, String extraPetIds,
                                AsyncHttpResponseHandler hotelPayHanler) {
        String url = getServiceBaseUrlNew() + "hotel/pay" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("shopId", shopId);
            params.put("type", type);
            params.put("mypetId", mypetId);
            params.put("roomType", roomType);
            params.put("startDate", startTime);
            params.put("endDate", endTime);
            params.put("payPrice", payPrice);
            params.put("payWay", paytype);
            params.put("shopId", shopId);
            if (Utils.isStrNull(contact)) {
                params.put("customerName", contact);
            }
            if (Utils.isStrNull(contactPhone)) {
                params.put("customerMobile", contactPhone);
            }
            if (Utils.isStrNull(remark)) {
                params.put("remark", remark);
            }
            if (Utils.isStrNull(strp)) {
                params.put("strp", strp);
            }
            if (Utils.isStrNull(extraPetIds)) {
                params.put("extraPetIds", extraPetIds);
            }
            if (couponId > 0) {
                params.put("couponId", couponId);
            }
            if (cardId > 0) {
                params.put("serviceCardId", cardId);
            }
            if (id > 0) {
                params.put("id", id);
            }
            if (careShopId > 0) {
                params.put("careShopId", careShopId);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, hotelPayHanler);
            Utils.mLogError("==-->寄养支付 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addCareService(Activity mContext, int shopId, String bathPetIds, int roomType, String startTime, String endTime, AsyncHttpResponseHandler addCareItemHandler) {
        String url = getServiceBaseUrlNew() + "hotel/addCareService" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            if (shopId > 0) {
                params.put("shopId", shopId);
            }
            if (Utils.isStrNull(bathPetIds)) {
                params.put("bathPetIds", bathPetIds);
            }
            params.put("roomType", roomType);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addCareItemHandler);
            Utils.mLogError("==-->添加护理项目 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getShareActivityPage(Activity mContext, String shareCode, AsyncHttpResponseHandler getShareActivityPage) {
        String url = getServiceBaseUrlNew() + "activityPage/getShareActivityPage" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("shareCode", shareCode);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getShareActivityPage);
            Utils.mLogError("==-->客户端获取分享口令弹框 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单项卡使用明细
     */

    public static void extraCardTradeHistory(Activity mContext, int cardId, int page, AsyncHttpResponseHandler getShareActivityPage) {
        String url = getServiceBaseUrlNew() + "extraCard/tradeHistory" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("cardId", cardId);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getShareActivityPage);
            Utils.mLogError("==-->单项卡使用明细 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extraCardPay(Activity mContext, int amount, double totalPrice, int payWay, AsyncHttpResponseHandler getShareActivityPage) {
        String url = getServiceBaseUrlNew() + "extraCard/order/pay" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("amount", amount);
            params.put("totalPrice", totalPrice);
            params.put("payWay", payWay);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getShareActivityPage);
            Utils.mLogError("==-->刷牙卡生成订单 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bind(Activity mContext, String cardId, String myPetId, AsyncHttpResponseHandler bind) {
        String url = getServiceBaseUrlNew() + "extraCard/bind" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("cardId", cardId);
            params.put("myPetId", myPetId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, bind);
            Utils.mLogError("==-->绑卡 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extraCardOrderInfo(Activity mContext, int cardId, AsyncHttpResponseHandler cardInfoHandler) {
        String url = getServiceBaseUrlNew() + "extraCard/order/info" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("cardId", cardId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, cardInfoHandler);
            Utils.mLogError("==-->卡订单详情 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ticket(Activity mContext, String code, AsyncHttpResponseHandler ticketHandler) {
        String url = getServiceBaseUrlNew() + "user/app/ticket" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("code", code);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, ticketHandler);
            Utils.mLogError("==-->获取openId " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void checkVerifyCode(Activity mContext,String cellPhone, String code, AsyncHttpResponseHandler codeHandler) {
        String url = getServiceBaseUrlNew() + "user/checkVerifyCode" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("code", code);
            params.put("cellPhone", cellPhone);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, codeHandler);
            Utils.mLogError("==-->校验二维码 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateUserPhone(Activity mContext, String newCellPhone, String code, AsyncHttpResponseHandler updateUserHanlder) {
        String url = getServiceBaseUrlNew() + "user/updateUser" + OtherUrl(mContext);
        try {
            RequestParams params = LocalParm(mContext);
            params.put("code", code);
            params.put("newCellPhone", newCellPhone);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, updateUserHanlder);
            Utils.mLogError("==-->修改用户手机号 " + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void getNewHomePage(Context context, int shopId,double lng,double lat,String cellPhone,String imei,
                                      AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "user/homePage?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("shopId", shopId);
            params.put("lng", lng);
            params.put("lat", lat);
            params.put("cellPhone", cellPhone);
            params.put("imei", imei);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("首页新接口："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void getHomeBanner(Context context, int cityId,int shopId,double lng,double lat,int isFirstLogin,
                                      AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "otherOperateBanner/appNewHomePage?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            if (cityId!=0){
                params.put("cityId", cityId);
            }
            if (shopId!=0){
                params.put("shopId", shopId);
            }
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            params.put("isFirstLogin",isFirstLogin);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("首页banner："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取登录页面背景视频文件
     */
    public static void getLoginBgVideo(Context context,String url,BinaryHttpResponseHandler handler) {
        try {
            RequestParams params = LocalParm(context);
            params.put("cityId", "");
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取登录页面背景视频文件
     */
    public static void getLoginBgVideoInfo(Context context,AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "/user/login/video?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 更换手机号校验
     */
    public static void canReplacePhone(Context context, String phone, String newCellPhone,String imei,
                                       int userid,AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "/user/updateUserCheck?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("cellPhone",phone);
            params.put("newCellPhone",newCellPhone);
            params.put("imei",imei);
            params.put("userId", userid);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 商品分享
     */
    public static void commodityShare(Context context, String phone, int commodityId,String imei,
                                       AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrlNew() + "mall/commodity/share?system="
                + getSource() + "_" + Global.getCurrentVersion(context);
        try {
            RequestParams params = LocalParm(context);
            params.put("cellPhone",phone);
            params.put("imei",imei);
            params.put("commodityId",commodityId);
            params.put("shareType",0);
            params.put("shareSource",0);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
            Utils.mLogError("商品分享码："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
