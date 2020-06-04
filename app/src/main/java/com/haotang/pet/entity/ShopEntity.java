package com.haotang.pet.entity;

import java.util.ArrayList;
import java.util.List;

public class ShopEntity {

	public List<ShopServices> list=new ArrayList<ShopServices>();
	public List<MemberEntity> memberEntities=new ArrayList<MemberEntity>();
	public String  areaImg;
	public String  shopName;
	public String  operTime;
	public String  address;
	public double lat;
	public double lng;
	public String  phone;
	public String  shopimg;
	public String  goodRate;
	public ArrayList<String> imgList= new ArrayList<String>();
}
