package com.haotang.pet.entity.mallEntity;

import android.os.Parcel;
import android.os.Parcelable;

import com.haotang.pet.entity.Addr;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/2.
 */

public class RegionChildren implements Parcelable{
    public int areaId;//110100
    public String region;//"北京市"
    public int selected;//110100
    public RegionChildren(){

    }
    public RegionChildren(Parcel in) {
        areaId = in.readInt();
        region = in.readString();
        selected = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(areaId);
        dest.writeString(region);
        dest.writeInt(selected);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RegionChildren> CREATOR = new Creator<RegionChildren>() {
        @Override
        public RegionChildren createFromParcel(Parcel in) {
            return new RegionChildren(in);
        }

        @Override
        public RegionChildren[] newArray(int size) {
            return new RegionChildren[size];
        }
    };

    public static RegionChildren json2Entity(JSONObject json){
        RegionChildren regionChildren = new RegionChildren();
        try{
            if (json.has("areaId")&&!json.isNull("areaId")){
                regionChildren.areaId = json.getInt("areaId");
            }
            if (json.has("region")&&!json.isNull("region")){
                regionChildren.region = json.getString("region");
            }
            if (json.has("areaName") && !json.isNull("areaName")) {
                regionChildren.region = json.getString("areaName");
            }
            if (json.has("selected")&&!json.isNull("selected")){
                regionChildren.selected = json.getInt("selected");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return regionChildren;
    }
}
