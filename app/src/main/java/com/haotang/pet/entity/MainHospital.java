package com.haotang.pet.entity;

import java.io.Serializable;

import org.json.JSONObject;

public class MainHospital  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 121424L;
	public int HomeHospitalId;
	public String addr;
	public String avatar;
	public String name;
	public String businessHours;
	public String createTime;
	public String intro;
	public String introPic;
	public int isDel;
	public String lat;
	public int level;
	public String lng;
	public int status;
	public String tel;
	public String updateTime;

	public static MainHospital json2Entity(JSONObject json) {
		MainHospital addr = new MainHospital();
		try {
			if (json.has("businessHours") && !json.isNull("businessHours")) {
				addr.businessHours = json.getString("businessHours");
			}
			if (json.has("createTime") && !json.isNull("createTime")) {
				addr.createTime = json.getString("createTime");
			}
			if (json.has("intro") && !json.isNull("intro")) {
				addr.intro = json.getString("intro");
			}
			if (json.has("introPic") && !json.isNull("introPic")) {
				addr.introPic = json.getString("introPic");
			}
			if (json.has("isDel") && !json.isNull("isDel")) {
				addr.isDel = json.getInt("isDel");
			}
			if (json.has("lat") && !json.isNull("lat")) {
				addr.lat = json.getString("lat");
			}
			if (json.has("level") && !json.isNull("level")) {
				addr.level = json.getInt("level");
			}
			if (json.has("lng") && !json.isNull("lng")) {
				addr.lng = json.getString("lng");
			}
			if (json.has("status") && !json.isNull("status")) {
				addr.status = json.getInt("status");
			}
			if (json.has("tel") && !json.isNull("tel")) {
				addr.tel = json.getString("tel");
			}
			if (json.has("updateTime") && !json.isNull("updateTime")) {
				addr.updateTime = json.getString("updateTime");
			}
			if (json.has("HomeHospitalId") && !json.isNull("HomeHospitalId")) {
				addr.HomeHospitalId = json.getInt("HomeHospitalId");
			}
			if (json.has("addr") && !json.isNull("addr")) {
				addr.addr = json.getString("addr");
			}
			if (json.has("avatar") && !json.isNull("avatar")) {
				addr.avatar = json.getString("avatar");
			}
			if (json.has("name") && !json.isNull("name")) {
				addr.name = json.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addr;
	}
}
