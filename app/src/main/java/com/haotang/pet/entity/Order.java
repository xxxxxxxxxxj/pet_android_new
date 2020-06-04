package com.haotang.pet.entity;

import java.util.ArrayList;
import java.util.List;

public class Order {
	public int orderid;
	public int type;
	public boolean isCanGratuity;
	public int servicetype;
	public int serviceid;
	public int petkind;
	public int status;//状态 1待付款 2已付款 3待确认 4待评价 5已关闭(结束) 6已取消(已支付) 7、到时自动取消 10、升级状态 21已出发  22已开始  23申请取消
	public String statusstr;
	public String servicename;
	public double fee;
	public String petimg;
	public String petname;
	public String customerpetname;
	public String starttime;
	public String endtime;
	public int addrtype;
	public int pickup=-1;
	public String EverytYearAndMonth;
	public int workerId;
	public String btnTxt;
	public String evaluate;
	public String paymentTxt;//"二维码付款"//按钮名称
	public String wxPaymentLink; //微信支付链接（判断是否存在，来显示二维码付款；没有只显示去付款按钮。）
	public String zfbPaymentLink;//支付宝支付链接
	public String hint;//温馨提示
	public String updateText;//"订单升级"
	public String updateLogcountType;//打点统计类型
	public String activityIdUpdata;//升级打点统计

	public String upgradeServiceHint;//"点击提示到店与美容师沟通"//点击 升级服务/追加单项按钮，提示信息
	public String extratext;//"追加单项"
	public String extraLogcountType;//打点统计类型
	public String activityIdExtrax;//单项打点统计
	public String extraUpgradeHint;//"点击提示到店与美容师沟通"//点击 升级服务/追加单项按钮，提示信息

	public List<Pet> listMyPets=new ArrayList<>();//我的宠物
	public List<Integer> itemIds = new ArrayList<>();//单项id
	public CommAddr commAddr = new CommAddr();//地址对象
	public ServiceShopAdd LastShop = new ServiceShopAdd();//门店对象
	public int serviceLoc;//上门到店
	public long headerId;
	public long remainTime;
	public String yearAndMonth;
	public int refType;
	public int hotelStatus;
	public String liveContent;
	public String liveUrl;
	public String liveName;
	public int cameraState;
	public String fosterShopImg;
	public String fosterShopName;
	public boolean isAddItemDecoration;
	public List<String> petImgList = new ArrayList<String>();
}
