package com.haotang.pet.entity;

import java.io.Serializable;

public class ServiceShopAdd implements Serializable{
//	public int addressId;
//	public String address;
//	public double addlat;
//	public double addlng;
	
	public int shopId;
	public String shopName;
	public String shopImg;
	public String shopAddress;
	public double shoplat;
	public double shoplng;
	public String shopWxImg;
	public String shopWxNum;
	public String shopActiveBackup;
	public int shopActivePoint;
	public String shopActiveTitle;
	public String shopPhone;
	public String dist;
	public String openTime;
	public String tag;
	public String goodRate;
	public boolean isChoose = false;
	@Override
	public String toString() {
		return "ServiceShopAdd [shopId=" + shopId + ", shopName=" + shopName
				+ ", shopAddress=" + shopAddress + ", shoplat=" + shoplat
				+ ", shoplng=" + shoplng + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
