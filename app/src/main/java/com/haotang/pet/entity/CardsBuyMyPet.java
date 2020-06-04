package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class CardsBuyMyPet implements Serializable{
	public int petId;//宠物类型id
	public String petName;//宠物类型名称
	public String nickName;//宠物昵称
	public int CustomerPetId;//我的宠物id
	public double price;//折扣价301, 
	public String priceContent;//折扣价 文案
	public String discount;//折扣 5.5",
	public String title;//"高级美容师套餐",     //套餐名称
	public int listPrice;//原价
	public ArrayList<CardsBuyMyPet.Content> contentList = new ArrayList<CardsBuyMyPet.Content>();
	
	public class Content implements Serializable{
		public int packageId;//对应条目id
		public int pri;//折扣价
		public String lpContent;//原价文案"原价", 
		public int count;//服务次数  5
		public int lpri;//原价  2
		public String packageTip;//内容文案 高级美容师洗澡
		public String pContent;//折扣价文案   服务卡折扣价
		public int tpri;//折扣后的价格
		public int tlpri;//折扣前的价格
	} 
}

