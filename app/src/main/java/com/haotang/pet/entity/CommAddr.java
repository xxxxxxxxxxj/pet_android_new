package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CommAddr implements Serializable{

	public String address;//地址
	public String linkman;//联系人
	public String telephone;//手机号
	public String supplement;//详细地址
	public String encryptedAddress;//加密地址显示
	public int Customer_AddressId;//addressId
	public double lat;
	public double lng;
	public int userId;
	public boolean isSelected = false;//是否选中
	public boolean isShowDel = false;

	public boolean isShowDel() {
		return isShowDel;
	}

	public void setShowDel(boolean showDel) {
		isShowDel = showDel;
	}

	public static CommAddr json2Entity(JSONObject json){
		CommAddr addr = new CommAddr();
		try {
			if (json.has("address")&&!json.isNull("address")) {
				addr.address = json.getString("address");
			}
			if (json.has("id")&&!json.isNull("id")) {
				addr.Customer_AddressId = json.getInt("id");
			}
			if (json.has("lat")&&!json.isNull("lat")) {
				addr.lat = json.getDouble("lat");
			}
			if (json.has("lng")&&!json.isNull("lng")) {
				addr.lng = json.getDouble("lng");
			}
			if (json.has("userId")&&!json.isNull("userId")) {
				addr.userId = json.getInt("userId");
			}
			if (json.has("linkman")&&!json.isNull("linkman")) {
				addr.linkman = json.getString("linkman");
			}
			if (json.has("telephone")&&!json.isNull("telephone")) {
				addr.telephone = json.getString("telephone");
			}
			if (json.has("supplement")&&!json.isNull("supplement")) {
				addr.supplement = json.getString("supplement");
			}
			if (json.has("encryptedAddress")&&!json.isNull("encryptedAddress")) {
				addr.encryptedAddress = json.getString("encryptedAddress");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr;
		
	}
}
