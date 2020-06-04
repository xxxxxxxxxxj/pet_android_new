package com.haotang.pet.entity;

import org.json.JSONObject;

public class ServiceTypeTopMsg {
//	public String title;
	public String content;
	public String time;
	
	public static ServiceTypeTopMsg json2Entity(JSONObject json){
		ServiceTypeTopMsg typeTopMsg = new ServiceTypeTopMsg();
		try {
//			if (json.has("title")&&!json.isNull("title")) {
//				typeTopMsg.title = json.getString("title");
//			}
			if (json.has("content")&&!json.isNull("content")) {
				typeTopMsg.content = json.getString("content");
			}
			if (json.has("time")&&!json.isNull("time")) {
				typeTopMsg.time = json.getString("time");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return typeTopMsg;
	}
}
