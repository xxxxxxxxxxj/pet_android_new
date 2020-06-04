package com.haotang.pet.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class ExtraItem implements Serializable {
	private static final long serialVersionUID = 141234L;
	private String availablePets;
	private String desc;
	private int itemId;
	private String itemName;
	private double listPrice;// 原价
	private double price;// 现价
	private String pic;
	private int source;

	public static ExtraItem json2Entity(JSONObject json) {
		ExtraItem extraItem = new ExtraItem();
		try {
			if (json.has("availablePets") && !json.isNull("availablePets")) {
				extraItem.availablePets = json.getString("availablePets");
			}
			if (json.has("desc") && !json.isNull("desc")) {
				extraItem.desc = json.getString("desc");
			}
			if (json.has("itemId") && !json.isNull("itemId")) {
				extraItem.itemId = json.getInt("itemId");
			}
			if (json.has("itemName") && !json.isNull("itemName")) {
				extraItem.itemName = json.getString("itemName");
			}
			if (json.has("listPrice") && !json.isNull("listPrice")) {
				extraItem.listPrice = json.getDouble("listPrice");
			}
			if (json.has("price") && !json.isNull("price")) {
				extraItem.price = json.getDouble("price");
			}
			if (json.has("pic") && !json.isNull("pic")) {
				extraItem.pic = json.getString("pic");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return extraItem;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAvailablePets() {
		return availablePets;
	}

	public void setAvailablePets(String availablePets) {
		this.availablePets = availablePets;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	@Override
	public String toString() {
		return "ExtraItem{" +
				"availablePets='" + availablePets + '\'' +
				", desc='" + desc + '\'' +
				", itemId=" + itemId +
				", itemName='" + itemName + '\'' +
				", listPrice=" + listPrice +
				", price=" + price +
				", pic='" + pic + '\'' +
				'}';
	}

	public ExtraItem(String availablePets, String desc, int itemId, String itemName, double listPrice, double price,
					 String pic) {
		this.availablePets = availablePets;
		this.desc = desc;
		this.itemId = itemId;
		this.itemName = itemName;
		this.listPrice = listPrice;
		this.price = price;
		this.pic = pic;
	}

	public ExtraItem() {
		super();
	}
}
