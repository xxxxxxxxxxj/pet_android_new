package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Coupon implements Serializable {
	public int id;
	public String content;
	public String extraCondition;
	public String starttime;
	public String endtime;
	public String amount;
	public double zhekou;
	public String name;
	public String least;
	public int isGive;
	public int  status;
	public double discount;
	public boolean isChoose = false;
	public int isAvali;//新增是否支持
	public int isCanGive;
	public int couponType;//"category":对应原来的couponType 1 //优惠券分类(1:服务优惠券、2:上门服务优惠券 3:商城优惠券、4:单项优惠券)
	public int reduceType;//减免类型(1:减免券、2:折扣券、3:免单券)
	public List<String> reasons = new ArrayList<>();
	public int avaliPets;
	public int canUseServiceCard;
	public boolean isOpen;
	public boolean isShow;
	public int isToExpire;//0非即将过期   1即将过期
	public int category;


	public static Coupon jsonToEntity(JSONObject json){
		Coupon coupon = new Coupon();
		try {
			if (json.has("isToExpire")&&!json.isNull("isToExpire")){
				coupon.isToExpire = json.getInt("isToExpire");
			}
			if (json.has("category")&&!json.isNull("category")){
				coupon.category = json.getInt("category");
			}
			if (json.has("amount") && !json.isNull("amount")) {
				coupon.zhekou = json.getDouble("amount");
			}
			if(json.has("canUseServiceCard")&&!json.isNull("canUseServiceCard")){
				coupon.canUseServiceCard = json.getInt("canUseServiceCard");
			}
			if(json.has("isGive")&&!json.isNull("isGive")){
				coupon.isGive = json.getInt("isGive");
			}
			if(json.has("CouponId")&&!json.isNull("CouponId")){
				coupon.id = json.getInt("CouponId");
			}
			if(json.has("id")&&!json.isNull("id")){
				coupon.id = json.getInt("id");
			}
			if(json.has("description")&&!json.isNull("description")){
				coupon.content = json.getString("description");
			}
			if(json.has("extraCondition")&&!json.isNull("extraCondition")){
				coupon.extraCondition = json.getString("extraCondition");
			}
			if(json.has("startTime")&&!json.isNull("startTime")){
				coupon.starttime = json.getString("startTime");
			}
			if(json.has("endTime")&&!json.isNull("endTime")){
				coupon.endtime = json.getString("endTime");
			}
			if(json.has("amount")&&!json.isNull("amount")){
				coupon.amount = json.getString("amount");
			}
			if (json.has("name")&&!json.isNull("name")) {
				coupon.name = json.getString("name");
			}
			if (json.has("least")&&!json.isNull("least")) {
				coupon.least = json.getString("least");
			}
			if(json.has("isAvali")&&!json.isNull("isAvali")){
				coupon.isAvali = json.getInt("isAvali");
			}
			if(json.has("discount")&&!json.isNull("discount")){
				coupon.discount = json.getDouble("discount");
			}
			if (json.has("status")&& !json.isNull("status")) {
				coupon.status=json.getInt("status");
			}
			if (json.has("isCanGive")&&!json.isNull("isCanGive")) {
				coupon.isCanGive = json.getInt("isCanGive");
			}
			if (json.has("category")&&!json.isNull("category")) {
				coupon.couponType = json.getInt("category");
			}
			if (json.has("reduceType")&&!json.isNull("reduceType")) {
				coupon.reduceType = json.getInt("reduceType");
			}
			if (json.has("avaliPets")&&!json.isNull("avaliPets")) {
				JSONArray array = json.getJSONArray("avaliPets");
				if (array.length()>0){
					JSONObject objectEva = array.getJSONObject(0);
					if (objectEva.has("myPetId")&&!objectEva.isNull("myPetId")){
						coupon.avaliPets = objectEva.getInt("myPetId");
					}
//					for (int i =0;i<array.length();i++){
//						JSONObject objectEva = array.getJSONObject(i);
//						if (objectEva.has("myPetId")&&!objectEva.isNull("myPetId")){
//							coupon.avaliPets = objectEva.getInt("myPetId");
//						}
//					}
				}
			}
			if (json.has("reasons")&&!json.isNull("reasons")){
				JSONArray array = json.getJSONArray("reasons");
				if (array.length()>0){
					for (int i =0;i<array.length();i++){
						coupon.reasons.add(array.getString(i));
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return coupon;
	}
}
