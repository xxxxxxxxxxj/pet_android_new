package com.haotang.pet.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/20 18:31
 */
public class ApointMentItem implements Serializable {
    private String priceSuffix;
    private int id;
    private String name;
    private double vipPrice;// vip价
    private double price;// 现价
    private double price10;//
    private double price20;//
    private double price30;//
    private double vipPrice10;//
    private double vipPrice20;//
    private double vipPrice30;//
    private String pic;
    private int label;
    private boolean isDelete;
    private boolean isDelete1;
    private String shareImg;
    private String shareTitle;
    private String shareUrl;
    private String shareTxt;
    private String desc;
    private int serviceAmount;
    private List<com.haotang.pet.entity.Banner> bannerList;
    private List<String> detailPicList;
    private List<Integer> availableMyPets;
    private int state;//1.所有宠物都不支持，2.首席赠送，3.加，4.减
    private boolean isShouXi;
    private boolean isTeethCard;//是否是刷牙卡免费单项
    private List<Integer> petKindList;//1.狗，2.猫

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApointMentItem that = (ApointMentItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ApointMentItem{" +
                "priceSuffix='" + priceSuffix + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", vipPrice=" + vipPrice +
                ", price=" + price +
                ", price10=" + price10 +
                ", price20=" + price20 +
                ", price30=" + price30 +
                ", vipPrice10=" + vipPrice10 +
                ", vipPrice20=" + vipPrice20 +
                ", vipPrice30=" + vipPrice30 +
                ", pic='" + pic + '\'' +
                ", label=" + label +
                ", isDelete=" + isDelete +
                ", isDelete1=" + isDelete1 +
                ", shareImg='" + shareImg + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareTxt='" + shareTxt + '\'' +
                ", desc='" + desc + '\'' +
                ", serviceAmount=" + serviceAmount +
                ", bannerList=" + bannerList +
                ", detailPicList=" + detailPicList +
                ", availableMyPets=" + availableMyPets +
                ", state=" + state +
                ", isShouXi=" + isShouXi +
                ", petKindList=" + petKindList +
                '}';
    }

    public double getPrice10() {
        return price10;
    }

    public void setPrice10(double price10) {
        this.price10 = price10;
    }

    public double getPrice20() {
        return price20;
    }

    public void setPrice20(double price20) {
        this.price20 = price20;
    }

    public double getPrice30() {
        return price30;
    }

    public void setPrice30(double price30) {
        this.price30 = price30;
    }

    public double getVipPrice10() {
        return vipPrice10;
    }

    public void setVipPrice10(double vipPrice10) {
        this.vipPrice10 = vipPrice10;
    }

    public double getVipPrice20() {
        return vipPrice20;
    }

    public void setVipPrice20(double vipPrice20) {
        this.vipPrice20 = vipPrice20;
    }

    public double getVipPrice30() {
        return vipPrice30;
    }

    public void setVipPrice30(double vipPrice30) {
        this.vipPrice30 = vipPrice30;
    }

    public List<Integer> getPetKindList() {
        return petKindList;
    }

    public void setPetKindList(List<Integer> petKindList) {
        this.petKindList = petKindList;
    }

    public boolean isShouXi() {
        return isShouXi;
    }

    public void setShouXi(boolean shouXi) {
        isShouXi = shouXi;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ApointMentItem(int id) {
        this.id = id;
    }

    public ApointMentItem(int id, double price, double vipPrice, boolean shouXi, boolean isTeethCard, double price10, double price20, double price30, double vipPrice10, double vipPrice20, double vipPrice30) {
        this.id = id;
        this.vipPrice = vipPrice;
        this.price = price;
        this.isShouXi = shouXi;
        this.isTeethCard = isTeethCard;
        this.price10 = price10;
        this.price20 = price20;
        this.price30 = price30;
        this.vipPrice10 = vipPrice10;
        this.vipPrice20 = vipPrice20;
        this.vipPrice30 = vipPrice30;
    }

    public ApointMentItem(String priceSuffix, int id, String name, double vipPrice, double price, String pic, int label) {
        this.priceSuffix = priceSuffix;
        this.id = id;
        this.name = name;
        this.vipPrice = vipPrice;
        this.price = price;
        this.pic = pic;
        this.label = label;
    }

    public ApointMentItem() {
    }

    public ApointMentItem(String priceSuffix, int id, String name, String pic, int label) {
        this.priceSuffix = priceSuffix;
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.label = label;
    }

    public static ApointMentItem json2Entity(JSONObject json) {
        ApointMentItem apointMentItem = new ApointMentItem();
        try {
            if (json.has("label") && !json.isNull("label")) {
                apointMentItem.setLabel(json.getInt("label"));
            }
            if (json.has("priceSuffix") && !json.isNull("priceSuffix")) {
                apointMentItem.setPriceSuffix(json.getString("priceSuffix"));
            }
            if (json.has("id") && !json.isNull("id")) {
                apointMentItem.setId(json.getInt("id"));
            }
            if (json.has("name") && !json.isNull("name")) {
                apointMentItem.setName(json.getString("name"));
            }
            if (json.has("vipPrice") && !json.isNull("vipPrice")) {
                apointMentItem.setVipPrice(json.getDouble("vipPrice"));
            }
            if (json.has("price") && !json.isNull("price")) {
                apointMentItem.setPrice(json.getDouble("price"));
            }
            if (json.has("pic") && !json.isNull("pic")) {
                apointMentItem.setPic(json.getString("pic"));
            }
            if (json.has("serviceAmount") && !json.isNull("serviceAmount")) {
                apointMentItem.setServiceAmount(json.getInt("serviceAmount"));
            }
            if (json.has("desc") && !json.isNull("desc")) {
                apointMentItem.setDesc(json.getString("desc"));
            }
            if (json.has("share") && !json.isNull("share")) {
                JSONObject jshare = json.getJSONObject("share");
                if (jshare.has("img") && !jshare.isNull("img")) {
                    apointMentItem.setShareImg(jshare.getString("img"));
                }
                if (jshare.has("title") && !jshare.isNull("title")) {
                    apointMentItem.setShareTitle(jshare.getString("title"));
                }
                if (jshare.has("url") && !jshare.isNull("url")) {
                    apointMentItem.setShareUrl(jshare.getString("url"));
                }
                if (jshare.has("desc") && !jshare.isNull("desc")) {
                    apointMentItem.setShareTxt(jshare.getString("desc"));
                }
            }
            if (json.has("detailPic") && !json.isNull("detailPic")) {
                JSONArray jarrdetailPic = json.getJSONArray("detailPic");
                if (jarrdetailPic != null && jarrdetailPic.length() > 0) {
                    List<String> list = new ArrayList<String>();
                    list.clear();
                    for (int i = 0; i < jarrdetailPic.length(); i++) {
                        list.add(jarrdetailPic.getString(i));
                    }
                    apointMentItem.setDetailPicList(list);
                }
            }
            if (json.has("banner") && !json.isNull("banner")) {
                JSONArray jarrbanner = json.getJSONArray("banner");
                if (jarrbanner != null && jarrbanner.length() > 0) {
                    List<com.haotang.pet.entity.Banner> list = new ArrayList<com.haotang.pet.entity.Banner>();
                    list.clear();
                    for (int i = 0; i < jarrbanner.length(); i++) {
                        list.add(com.haotang.pet.entity.Banner.json2Entity(jarrbanner.getJSONObject(i)));
                    }
                    apointMentItem.setBannerList(list);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "items()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return apointMentItem;
    }

    public boolean isDelete1() {
        return isDelete1;
    }

    public void setDelete1(boolean delete1) {
        isDelete1 = delete1;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public List<Integer> getAvailableMyPets() {
        return availableMyPets;
    }

    public void setAvailableMyPets(List<Integer> availableMyPets) {
        this.availableMyPets = availableMyPets;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareTxt() {
        return shareTxt;
    }

    public void setShareTxt(String shareTxt) {
        this.shareTxt = shareTxt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(int serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public List<com.haotang.pet.entity.Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<com.haotang.pet.entity.Banner> bannerList) {
        this.bannerList = bannerList;
    }

    public List<String> getDetailPicList() {
        return detailPicList;
    }

    public void setDetailPicList(List<String> detailPicList) {
        this.detailPicList = detailPicList;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getPriceSuffix() {
        return priceSuffix;
    }

    public void setPriceSuffix(String priceSuffix) {
        this.priceSuffix = priceSuffix;
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

    public double getVipPrice() {
        return vipPrice;
    }

    public boolean isTeethCard() {
        return isTeethCard;
    }

    public void setTeethCard(boolean teethCard) {
        isTeethCard = teethCard;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
