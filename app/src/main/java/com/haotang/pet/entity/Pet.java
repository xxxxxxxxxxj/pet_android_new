package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pet implements Serializable {
    public int id = -1;
    public int kindid;
    public String name = "";
    public String image;
    public String nickName = "";
    public int customerpetid = 0;
    public int sa;//是否支持服务
    public int bathnum;//洗澡次数
    public int beautynum;//美容次数
    public int fosternum;//寄养次数
    public int charmnum;//魅力值
    public int cleannum;//清洁值
    public int sex;//性别 1：男 0：女
    public int mood;//心情
    public String visibleid;//可见ID
    public String cleanhint;//提示
    public String availServiceType;
    public long[] serviceHome = null;
    public long[] serviceShop = null;
    public double youyongPrice = 0;
    public double listPrice = 0;
    public int serviceloc;
    public int serviceid;
    public String certiOrderStatus;
    public CertiOrder certiOrder;
    public int sumSpecial;
    public int certiStatus;
    public String certiNo;
    public String certiUrl;
    public int certiDogSize;
    public int orderFee;
    public int orderid;
    public int sumSwim;
    public String month;
    public int isCerti;
    public String petInfo;//"我家小可爱谁最美，看来围观~",  //宠物介绍4.8.0新增
    public String petActiveTitle;//"我家小可爱谁最美，看来围观~",  //宠物活动标题
    public String petActiveBackup;//"12",  //宠物活动跳转参数
    public String petActivePoint;//"12",  //宠物活动跳转指向
    public int petActiveStatus;//  0,  //活动状态  0 进行中  1 已结束
    public String petEncyclInfo;//宠物百科（富文本编辑）
    public String petEncyclUrl;//"http://test.chongwuhome.cn/static/content/html5/201803/petknowledge/info.html?id=185"  //宠物百科请求的h5url
    public String petCardUrl;
    private boolean isSelect;
    public boolean isAdd;
    public int swimVipPrice;
    public int isVIP;
    public int selected;
    public String marked;
    public String remark;
    public String mscolor;
    public List<String> imgList = new ArrayList<String>();
    public int isBindExtraItemCard;//是否绑定刷牙年卡
    public String isBindExtraItemCardTip;//绑定刷牙卡文案
    public boolean isShow;
    public boolean isShowDel;

    public boolean isShowDel() {
        return isShowDel;
    }

    public void setShowDel(boolean showDel) {
        isShowDel = showDel;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Pet() {
    }

    public Pet(int id, int kindid, String name, String image, String nickName, int customerpetid) {
        this.id = id;
        this.kindid = kindid;
        this.name = name;
        this.image = image;
        this.nickName = nickName;
        this.customerpetid = customerpetid;
    }

    public static Pet json2Entity(JSONObject json) {
        Pet pet = new Pet();
        try {
            if (json.has("selected") && !json.isNull("selected")) {
                pet.selected = json.getInt("selected");
            }
            if (json.has("imgList") && !json.isNull("imgList")) {
                JSONArray imgList = json.getJSONArray("imgList");
                if (imgList != null && imgList.length() > 0) {
                    pet.imgList.clear();
                    for (int i = 0; i < imgList.length(); i++) {
                        pet.imgList.add(imgList.getString(i));
                    }
                }
            }
            if (json.has("remark") && !json.isNull("remark")) {
                pet.remark = json.getString("remark");
            }
            if (json.has("color") && !json.isNull("color")) {
                pet.mscolor = json.getString("color");
            }
            if (json.has("marked") && !json.isNull("marked")) {
                pet.marked = json.getString("marked");
            }
            if (json.has("month") && !json.isNull("month")) {
                pet.month = json.getString("month");
            }
            if (json.has("isCerti") && !json.isNull("isCerti")) {
                pet.isCerti = json.getInt("isCerti");
            }
            if (json.has("petId") && !json.isNull("petId")) {
                pet.id = json.getInt("petId");
            }
            if (json.has("id") && !json.isNull("id")) {
                pet.customerpetid = json.getInt("id");
            }
            if (json.has("petKind") && !json.isNull("petKind")) {
                pet.kindid = json.getInt("petKind");
            }
            if (json.has("nickName") && !json.isNull("nickName")) {
                pet.nickName = json.getString("nickName").trim();
            }
            if (json.has("serviceId") && !json.isNull("serviceId")) {
                pet.serviceid = json.getInt("serviceId");
            }
            if (json.has("avatar") && !json.isNull("avatar")) {
                String string = json.getString("avatar");
                pet.image = string;
            }
            if (json.has("sa") && !json.isNull("sa")) {
                pet.sa = json.getInt("sa");
            }

            if (json.has("sumBath") && !json.isNull("sumBath")) {
                pet.bathnum = json.getInt("sumBath");
            }
            if (json.has("sumCos") && !json.isNull("sumCos")) {
                pet.beautynum = json.getInt("sumCos");
            }
            if (json.has("sumFos") && !json.isNull("sumFos")) {
                pet.fosternum = json.getInt("sumFos");
            }
            if (json.has("sumSpecial") && !json.isNull("sumSpecial")) {
                pet.sumSpecial = json.getInt("sumSpecial");
            }
            if (json.has("cleanGrade") && !json.isNull("cleanGrade")) {
                pet.cleannum = json.getInt("cleanGrade");
            }
            if (json.has("beautyGrade") && !json.isNull("beautyGrade")) {
                pet.charmnum = json.getInt("beautyGrade");
            }
            if (json.has("sex") && !json.isNull("sex")) {
                pet.sex = json.getInt("sex");
            }
            if (json.has("emotion") && !json.isNull("emotion")) {
                pet.mood = json.getInt("emotion");
            }
            if (json.has("petCode") && !json.isNull("petCode")) {
                pet.visibleid = json.getString("petCode");
            }
            if (json.has("certiStatus") && !json.isNull("certiStatus")) {
                pet.certiStatus = json.getInt("certiStatus");
            }
            if (json.has("gradeTxt") && !json.isNull("gradeTxt")) {
                pet.cleanhint = json.getString("gradeTxt");
            }
            if (json.has("isBindExtraItemCard")&&!json.isNull("isBindExtraItemCard")){
                pet.isBindExtraItemCard = json.getInt("isBindExtraItemCard");
            }
            if (json.has("isBindExtraItemCardTip")&&!json.isNull("isBindExtraItemCardTip")){
                pet.isBindExtraItemCardTip = json.getString("isBindExtraItemCardTip");
            }
            if (json.has("availServiceTypes") && !json.isNull("availServiceTypes")) {
                JSONArray arr = json.getJSONArray("availServiceTypes");
                pet.availServiceType = arr.toString();
            }
            if (json.has("serviceHome") && !json.isNull("serviceHome")) {
                JSONArray arrayService = json.getJSONArray("serviceHome");
                long ServiceHome[] = new long[arrayService.length()];
                for (int j = 0; j < arrayService.length(); j++) {
                    ServiceHome[j] = arrayService.getLong(j);
                }
                pet.serviceHome = ServiceHome;
            }
            if (json.has("serviceShop") && !json.isNull("serviceShop")) {
                JSONArray arrayService = json.getJSONArray("serviceShop");
                long serviceShop[] = new long[arrayService.length()];
                for (int j = 0; j < arrayService.length(); j++) {
                    serviceShop[j] = arrayService.getLong(j);
                }
                pet.serviceShop = serviceShop;
            }
            if (json.has("certiOrderStatus") && !json.isNull("certiOrderStatus")) {
                pet.certiOrderStatus = json.getString("certiOrderStatus");
            }
            if (json.has("certiOrder") && !json.isNull("certiOrder")) {
                JSONObject jsonObject = json.getJSONObject("certiOrder");
                pet.certiOrder = CertiOrder.json2Entity(jsonObject);
            }
            if (json.has("certiNo") && !json.isNull("certiNo")) {
                pet.certiNo = json.getString("certiNo");
            }
            if (json.has("certiUrl") && !json.isNull("certiUrl")) {
                pet.certiUrl = json.getString("certiUrl");
            }
            if (json.has("certiDogSize") && !json.isNull("certiDogSize")) {
                pet.certiDogSize = json.getInt("certiDogSize");
            }
            if (json.has("sumSwim") && !json.isNull("sumSwim")) {
                pet.sumSwim = json.getInt("sumSwim");
            }
            if (json.has("petName") && !json.isNull("petName")) {
                pet.name = json.getString("petName");
            }
            if (json.has("petType") && !json.isNull("petType")) {
                JSONObject object = json.getJSONObject("petType");
                if (object.has("petInfo") && !object.isNull("petInfo")) {
                    pet.petInfo = object.getString("petInfo");
                }
                if (object.has("petActiveTitle") && !object.isNull("petActiveTitle")) {
                    pet.petActiveTitle = object.getString("petActiveTitle");
                }
                if (object.has("petActiveBackup") && !object.isNull("petActiveBackup")) {
                    pet.petActiveBackup = object.getString("petActiveBackup");
                }
                if (object.has("petActivePoint") && !object.isNull("petActivePoint")) {
                    pet.petActivePoint = object.getString("petActivePoint");
                }
                if (object.has("petEncyclInfo") && !object.isNull("petEncyclInfo")) {
                    pet.petEncyclInfo = object.getString("petEncyclInfo");
                }
                if (object.has("petActiveStatus") && !object.isNull("petActiveStatus")) {
                    pet.petActiveStatus = object.getInt("petActiveStatus");
                }
                if (object.has("petEncyclUrl") && !object.isNull("petEncyclUrl")) {
                    pet.petEncyclUrl = object.getString("petEncyclUrl");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pet;
    }
}
