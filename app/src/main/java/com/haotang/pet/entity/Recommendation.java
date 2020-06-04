package com.haotang.pet.entity;

import org.json.JSONObject;

public class Recommendation {
	public int UserId;
	public int myPetId;
	public String content;
	public String backup;
	public int point;

	public Recommendation() {
		super();
	}

	public Recommendation(int userId, int myPetId, String content,
			String backup, int point) {
		super();
		UserId = userId;
		this.myPetId = myPetId;
		this.content = content;
		this.backup = backup;
		this.point = point;
	}

	public static Recommendation json2Entity(JSONObject json) {
		Recommendation recommendation = new Recommendation();
		try {
			if (json.has("UserId") && !json.isNull("UserId")) {
				recommendation.UserId = json.getInt("UserId");
			}
			if (json.has("myPetId") && !json.isNull("myPetId")) {
				recommendation.myPetId = json.getInt("myPetId");
			}
			if (json.has("content") && !json.isNull("content")) {
				recommendation.content = json.getString("content");
			}
			if (json.has("backup") && !json.isNull("backup")) {
				recommendation.backup = json.getString("backup");
			}
			if (json.has("point") && !json.isNull("point")) {
				recommendation.point = json.getInt("point");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recommendation;
	}
}
