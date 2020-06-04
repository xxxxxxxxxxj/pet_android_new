package com.haotang.pet.entity;

import com.haotang.pet.util.ComputeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-22 11:50
 */
public class FosterRoom {
    private String picDesc;
    private String priceDesc;
    private List<String> labels;
    private int id;
    private String name;
    private String img1;
    private String desc2;
    private String sizeDesc;
    private double price;
    private int isFull;
    private int bathPetSize;
    private List<String> img2;

    public static FosterRoom jsonToEntity(JSONObject json) {
        FosterRoom fosterRoom = new FosterRoom();
        try {
            if (json.has("sizeDesc") && !json.isNull("sizeDesc")) {
                fosterRoom.setSizeDesc(json.getString("sizeDesc"));
            }
            if (json.has("desc2") && !json.isNull("desc2")) {
                fosterRoom.setDesc2(json.getString("desc2"));
            }
            if (json.has("roomPrice") && !json.isNull("roomPrice")) {
                JSONObject jroomPrice = json.getJSONObject("roomPrice");
                double price = 0;
                double serviceFee = 0;
                double extraServiceFee = 0;
                if (jroomPrice.has("price") && !jroomPrice.isNull("price")) {
                    price = jroomPrice.getDouble("price");
                }
                if (jroomPrice.has("serviceFee") && !jroomPrice.isNull("serviceFee")) {
                    serviceFee = jroomPrice.getDouble("serviceFee");
                }
                if (jroomPrice.has("extraServiceFee") && !jroomPrice.isNull("extraServiceFee")) {
                    extraServiceFee = jroomPrice.getDouble("extraServiceFee");
                }
                fosterRoom.setPrice(ComputeUtil.add(price, serviceFee, extraServiceFee));
            }
            if (json.has("picDesc") && !json.isNull("picDesc")) {
                fosterRoom.setPicDesc(json.getString("picDesc"));
            }
            if (json.has("priceDesc") && !json.isNull("priceDesc")) {
                fosterRoom.setPriceDesc(json.getString("priceDesc"));
            }
            if (json.has("labels") && !json.isNull("labels")) {
                JSONArray jarrlabels = json.getJSONArray("labels");
                if (jarrlabels.length() > 0) {
                    ArrayList<String> labelsList = new ArrayList<>();
                    for (int i = 0; i < jarrlabels.length(); i++) {
                        labelsList.add(jarrlabels.getString(i));
                    }
                    fosterRoom.setLabels(labelsList);
                }
            }
            if (json.has("id") && !json.isNull("id")) {
                fosterRoom.setId(json.getInt("id"));
            }
            if (json.has("name") && !json.isNull("name")) {
                fosterRoom.setName(json.getString("name"));
            }
            if (json.has("img1") && !json.isNull("img1")) {
                fosterRoom.setImg1(json.getString("img1"));
            }
            if (json.has("isFull") && !json.isNull("isFull")) {
                fosterRoom.setIsFull(json.getInt("isFull"));
            }
            if (json.has("bathPetSize") && !json.isNull("bathPetSize")) {
                fosterRoom.setBathPetSize(json.getInt("bathPetSize"));
            }
            if (json.has("img2") && !json.isNull("img2")) {
                JSONArray jarrimg2 = json.getJSONArray("img2");
                if (jarrimg2.length() > 0) {
                    ArrayList<String> imgList = new ArrayList<>();
                    for (int i = 0; i < jarrimg2.length(); i++) {
                        imgList.add(jarrimg2.getString(i));
                    }
                    fosterRoom.setImg2(imgList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fosterRoom;
    }

    public String getSizeDesc() {
        return sizeDesc;
    }

    public void setSizeDesc(String sizeDesc) {
        this.sizeDesc = sizeDesc;
    }

    public String getPicDesc() {
        return picDesc;
    }

    public void setPicDesc(String picDesc) {
        this.picDesc = picDesc;
    }

    public String getPriceDesc() {
        return priceDesc;
    }

    public void setPriceDesc(String priceDesc) {
        this.priceDesc = priceDesc;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public int getIsFull() {
        return isFull;
    }

    public void setIsFull(int isFull) {
        this.isFull = isFull;
    }

    public int getBathPetSize() {
        return bathPetSize;
    }

    public void setBathPetSize(int bathPetSize) {
        this.bathPetSize = bathPetSize;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImg2() {
        return img2;
    }

    public void setImg2(List<String> img2) {
        this.img2 = img2;
    }
}
