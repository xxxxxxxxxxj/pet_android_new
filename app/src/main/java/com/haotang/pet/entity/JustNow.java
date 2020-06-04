package com.haotang.pet.entity;

import org.json.JSONObject;

public class JustNow {
	public int UserId;
	public int myPetId;
	public String content;
	public String time;
	public String avatar;
	public String shopName;
	public String backup;
	public int point;

	public JustNow() {
		super();
	}

	public JustNow(int userId, int myPetId, String content, String time,
			String avatar, String shopName, String backup, int point) {
		super();
		UserId = userId;
		this.myPetId = myPetId;
		this.content = content;
		this.time = time;
		this.avatar = avatar;
		this.shopName = shopName;
		this.backup = backup;
		this.point = point;
	}

	public static JustNow json2Entity(JSONObject json) {
		JustNow justNow = new JustNow();
		try {
			if (json.has("UserId") && !json.isNull("UserId")) {
				justNow.UserId = json.getInt("UserId");
			}
			if (json.has("myPetId") && !json.isNull("myPetId")) {
				justNow.myPetId = json.getInt("myPetId");
			}
			if (json.has("content") && !json.isNull("content")) {
				justNow.content = json.getString("content");
			}
			if (json.has("time") && !json.isNull("time")) {
				justNow.time = json.getString("time");
			}
			if (json.has("avatar") && !json.isNull("avatar")) {
				justNow.avatar = json.getString("avatar");
			}
			if (json.has("shopName") && !json.isNull("shopName")) {
				justNow.shopName = json.getString("shopName");
			}
			if (json.has("backup") && !json.isNull("backup")) {
				justNow.backup = json.getString("backup");
			}
			if (json.has("point") && !json.isNull("point")) {
				justNow.point = json.getInt("point");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return justNow;
	}
}
