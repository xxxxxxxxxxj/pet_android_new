package com.haotang.pet.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class RegisterCoupon implements Parcelable {
    public int amount;
    public String name;
    public String desc;
    public RegisterCoupon(){

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amount);
        dest.writeString(name);
        dest.writeString(desc);
    }
    public static final Parcelable.Creator<RegisterCoupon> CREATOR = new Creator<RegisterCoupon>() {
        @Override
        public RegisterCoupon createFromParcel(Parcel source) {
            return new RegisterCoupon(source);
        }

        @Override
        public RegisterCoupon[] newArray(int size) {
            return new RegisterCoupon[size];
        }
    };
    public RegisterCoupon(Parcel in){
        amount = in.readInt();
        name = in.readString();
        desc = in.readString();
    }
}
