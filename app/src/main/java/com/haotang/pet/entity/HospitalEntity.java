package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class HospitalEntity implements Serializable{
    public String address;
    public String name;
    public double lng;
    public double lat;
    public String tel;
    public List<String> listBrand = new ArrayList<>();

    public static HospitalEntity j2Entity(JSONObject object){
        HospitalEntity hospitalEntity = new HospitalEntity();
        try {
            if (object.has("address")&&!object.isNull("address")){
                hospitalEntity.address = object.getString("address");
            }
            if (object.has("name")&&!object.isNull("name")){
                hospitalEntity.name = object.getString("name");
            }
            if (object.has("lng")&&!object.isNull("lng")){
                hospitalEntity.lng = object.getDouble("lng");
            }
            if (object.has("lat")&&!object.isNull("lat")){
                hospitalEntity.lat = object.getDouble("lat");
            }
            if (object.has("tel")&&!object.isNull("tel")){
                hospitalEntity.tel = object.getString("tel");
            }
            if (object.has("brand")&&!object.isNull("brand")){
                JSONArray array = object.getJSONArray("brand");
                if (array.length()>0){
                    for (int i = 0 ;i<array.length();i++){
                        hospitalEntity.listBrand.add(array.getString(i));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hospitalEntity;
    }
}
