package com.haotang.pet.util;

public class Constant {

	public static final String RECORD_VIDEO_KEY = "key";
	public static final String RECORD_VIDEO_PATH = "path";
	public static final String RECORD_VIDEO_CAPTURE = "capture";

	//订单状态
	public static final int ORDER_STATUS_WAIT_PAY = 1;      //代付款
	public static final int ORDER_STATUS_ALREADY_PAY = 2;   //已付款
	public static final int ORDER_STATUS_WAIT_AFFIRM = 3;   //待确认
	public static final int ORDER_STATUS_WAIT_EVALUATE= 4;  //待评价
	public static final int ORDER_STATUS_ALREADY_CLOSE= 5;  //已关闭
	public static final int ORDER_STATUS_ALREADY_CANCEL= 6; //取消（已支付）
	public static final int ORDER_STATUS_NO_PAY_CANCEL= 7;  //取消（未支付）

}
