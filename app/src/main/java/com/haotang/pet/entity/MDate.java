package com.haotang.pet.entity;

public class MDate {
	public String date;
	public String day;
	public String holiday;
	public String badge;
	public double price;//价格
	public int isFull;//是否约满(0:否、1:是)
	public int week=-1;
	public int selectedindex=-1;//1为入住日期  2为离店日期
	public boolean valid;//是否可用
	public boolean isselected;//是否选中
	public boolean isselectedmiddle;//是否选中日期

	public MDate() {
	}

	public MDate(String date, String day, String holiday, String badge, double price, int isFull, int week, int selectedindex, boolean valid, boolean isselected, boolean isselectedmiddle) {
		this.date = date;
		this.day = day;
		this.holiday = holiday;
		this.badge = badge;
		this.price = price;
		this.isFull = isFull;
		this.week = week;
		this.selectedindex = selectedindex;
		this.valid = valid;
		this.isselected = isselected;
		this.isselectedmiddle = isselectedmiddle;
	}

	@Override
	public String toString() {
		return "MDate{" +
				"date='" + date + '\'' +
				", day='" + day + '\'' +
				", holiday='" + holiday + '\'' +
				", badge='" + badge + '\'' +
				", price=" + price +
				", isFull=" + isFull +
				", week=" + week +
				", selectedindex=" + selectedindex +
				", valid=" + valid +
				", isselected=" + isselected +
				", isselectedmiddle=" + isselectedmiddle +
				'}';
	}
}
