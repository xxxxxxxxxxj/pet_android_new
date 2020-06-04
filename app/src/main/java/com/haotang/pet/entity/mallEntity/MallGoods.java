package com.haotang.pet.entity.mallEntity;

import android.os.Parcel;
import android.os.Parcelable;

import com.haotang.pet.util.Utils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/3.
 */

public class MallGoods implements Parcelable {
    public String thumbnail;
    public int amount;
    public String specName;
    public String marketingTag;
    public int commodityId;
    public String title;
    public double retailPrice;
    public int orderType;

    public MallGoods() {

    }

    protected MallGoods(Parcel in) {
        thumbnail = in.readString();
        amount = in.readInt();
        specName = in.readString();
        marketingTag = in.readString();
        commodityId = in.readInt();
        title = in.readString();
        retailPrice = in.readDouble();
        orderType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnail);
        dest.writeInt(amount);
        dest.writeString(specName);
        dest.writeString(marketingTag);
        dest.writeInt(commodityId);
        dest.writeString(title);
        dest.writeDouble(retailPrice);
        dest.writeInt(orderType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MallGoods> CREATOR = new Creator<MallGoods>() {
        @Override
        public MallGoods createFromParcel(Parcel in) {
            return new MallGoods(in);
        }

        @Override
        public MallGoods[] newArray(int size) {
            return new MallGoods[size];
        }
    };

    public static MallGoods json2Entity(JSONObject json) {
        MallGoods mallGoods = new MallGoods();
        try {
            if (json.has("thumbnail") && !json.isNull("thumbnail")) {
                mallGoods.thumbnail = json.getString("thumbnail");
            }
            if (json.has("amount") && !json.isNull("amount")) {
                mallGoods.amount = json.getInt("amount");
            }
            if (json.has("specName") && !json.isNull("specName")) {
                mallGoods.specName = json.getString("specName");
            }
            if (json.has("marketingTag") && !json.isNull("marketingTag")) {
                mallGoods.marketingTag = json.getString("marketingTag");
            }
            if (json.has("commodityId") && !json.isNull("commodityId")) {
                mallGoods.commodityId = json.getInt("commodityId");
            }
            if (json.has("title") && !json.isNull("title")) {
                mallGoods.title = json.getString("title");
            }
            if (json.has("retailPrice") && !json.isNull("retailPrice")) {
                mallGoods.retailPrice = Utils.formatDouble(json.getDouble("retailPrice"), 2);
            }
            if (json.has("orderType") && !json.isNull("orderType")) {
                mallGoods.orderType = json.getInt("orderType");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallGoods;
    }
}
