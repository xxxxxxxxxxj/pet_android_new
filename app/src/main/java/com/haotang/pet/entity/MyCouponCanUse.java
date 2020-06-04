package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * "couponId": 1,
 * "amount":23,//金额
 * "status":2//0未使用 1已冻结(订单未确认成功) 2已使用 3已作废
 * "startTime":"2017-12-18 12:10:00"//生效时间
 * "endTime":"2017-12-18 12:10:00"//结束时间
 * "created":"2017-12-18 12:10:00"//
 * "name":"兑换有礼（洗澡专享）"//优惠券名称
 * "description":"描述"//特殊情况下用到的描述
 * "isGive"：1//是否是赠送：1赠送，0非赠送
 * "isCanGive":1//是否可赠送 0可以 1不可以
 * "couponType": 1,//(1:普通订单金额减免优惠券、2:上门服务费减免优惠券 3:商城优惠券)
 */
public class MyCouponCanUse implements Serializable {

    public double amount;
    public double zhekou;
    public String description;
    public String extraCondition;
    public String endTime;
    public int CouponId;
    public String orderId;
    public String startTime;
    public int status;
    public int typeId;
    public int couponType;
    public double discountPrice;
    public String used;
    public int userId;
    public String name;
    public String least;
    public int pageSize;
    public int isGive;
    public int isCanGive;
    public int reduceType;//减免类型(1:减免券、2:折扣券、3:免单券)
    public int isToExpire;//0非即将过期   1即将过期
    public int category;
    public boolean isOpen;
    public int duration;

    public String shareImg;
    public String shareTitle;
    public String shareUrl;
    public String shareDesc;

    public static MyCouponCanUse json2Entity(JSONObject json) {
        MyCouponCanUse couponCanUse = new MyCouponCanUse();
        try {
            if (json.has("amount") && !json.isNull("amount")) {
                couponCanUse.amount = json.getDouble("amount");
            }
            if (json.has("amount") && !json.isNull("amount")) {
                couponCanUse.zhekou = json.getDouble("amount");
            }
            if (json.has("description") && !json.isNull("description")) {
                couponCanUse.description = json.getString("description");
            }
            if (json.has("extraCondition") && !json.isNull("extraCondition")) {
                couponCanUse.extraCondition = json.getString("extraCondition");
            }
            if (json.has("endTime") && !json.isNull("endTime")) {
                couponCanUse.endTime = json.getString("endTime");
            }
            if (json.has("id") && !json.isNull("id")) {
                couponCanUse.CouponId = json.getInt("id");
            }
            if (json.has("orderId") && !json.isNull("orderId")) {
                couponCanUse.orderId = json.getString("orderId");
            }
            if (json.has("startTime") && !json.isNull("startTime")) {
                couponCanUse.startTime = json.getString("startTime");
            }
            if (json.has("status") && !json.isNull("status")) {
                couponCanUse.status = json.getInt("status");
            }
            if (json.has("couponType") && !json.isNull("couponType")) {
                couponCanUse.typeId = json.getInt("couponType");
            }
            if (json.has("duration")&&!json.isNull("duration")){
                couponCanUse.duration = json.getInt("duration");
            }
            if (json.has("discountPrice")&&!json.isNull("discountPrice")){
                couponCanUse.discountPrice = json.getDouble("discountPrice");
            }
            if (json.has("used") && !json.isNull("used")) {
                couponCanUse.used = json.getString("used");
            }
            if (json.has("userId") && !json.isNull("userId")) {
                couponCanUse.userId = json.getInt("userId");
            }
            if (json.has("name") && !json.isNull("name")) {
                couponCanUse.name = json.getString("name");
            }else if (json.has("couponName")&&!json.isNull("couponName")){
                couponCanUse.name = json.getString("couponName");
            }
            if (json.has("pageSize") && !json.isNull("pageSize")) {
                couponCanUse.pageSize = json.getInt("pageSize");
            }
            if (json.has("isGive") && !json.isNull("isGive")) {
                couponCanUse.isGive = json.getInt("isGive");
            }
            if (json.has("isCanGive") && !json.isNull("isCanGive")) {
                couponCanUse.isCanGive = json.getInt("isCanGive");
            }
            if (json.has("couponType") && !json.isNull("couponType")) {
                couponCanUse.couponType = json.getInt("couponType");
            }
            if (json.has("reduceType") && !json.isNull("reduceType")) {
                couponCanUse.reduceType = json.getInt("reduceType");
            }
            if (json.has("least") && !json.isNull("least")) {
                couponCanUse.least = json.getString("least");
            }
            if (json.has("isToExpire")&&!json.isNull("isToExpire")){
                couponCanUse.isToExpire = json.getInt("isToExpire");
            }
            if (json.has("category")&&!json.isNull("category")){
                couponCanUse.category = json.getInt("category");
            }else if (json.has("typeId")&&!json.isNull("typeId")){
                couponCanUse.category = json.getInt("typeId");
            }
            if (json.has("share") && !json.isNull("share")) {
                JSONObject objShare = json.getJSONObject("share");
                if (objShare.has("img") && !objShare.isNull("img")) {
                    couponCanUse.shareImg = objShare.getString("img");
                }
                if (objShare.has("title") && !objShare.isNull("title")) {
                    couponCanUse.shareTitle = objShare.getString("title");
                }
                if (objShare.has("url") && !objShare.isNull("url")) {
                    couponCanUse.shareUrl = objShare.getString("url");
                }
                if (objShare.has("desc") && !objShare.isNull("desc")) {
                    couponCanUse.shareDesc = objShare.getString("desc");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return couponCanUse;

    }
}
