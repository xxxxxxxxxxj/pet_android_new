package com.haotang.pet.entity.mallEntity;


import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MallSearchGoods {
    public int id;
    public String glwpetCommodityBarCode;
    public String commodityNum;
    public String title;
    public String subtitle;
    public String specName;
    public String marketingTag;
    public int classificationId;
    public int groupId;
    public int primeCosts;
    public double retailPrice;
    public double marketValue;
    public int stock;
    public int saleAmount;
    public int canNum;
    public int vipCanNum;
    public String thumbnail;
    public String banner;
    public String introPic;
    public String restrict;
    public String status;
    public String shelvesTime;
    public String isDel;
    public String created;
    public double ePrice;

    public static MallSearchGoods json2Entity(JSONObject json) {
        MallSearchGoods mallSearchGoods = new MallSearchGoods();
        try {
            if (json.has("ePrice") && !json.isNull("ePrice")) {
                mallSearchGoods.ePrice = json.getDouble("ePrice");
            }
            if (json.has("id") && !json.isNull("id")) {
                mallSearchGoods.id = json.getInt("id");
            }
            if (json.has("glwpetCommodityBarCode") && !json.isNull("glwpetCommodityBarCode")) {
                mallSearchGoods.glwpetCommodityBarCode = json.getString("glwpetCommodityBarCode");
            }
            if (json.has("commodityNum") && !json.isNull("commodityNum")) {
                mallSearchGoods.commodityNum = json.getString("commodityNum");
            }
            if (json.has("title") && !json.isNull("title")) {
                mallSearchGoods.title = json.getString("title");
            }
            if (json.has("subtitle") && !json.isNull("subtitle")) {
                mallSearchGoods.subtitle = json.getString("subtitle");
            }
            if (json.has("specName") && !json.isNull("specName")) {
                mallSearchGoods.specName = json.getString("specName");
            }
            if (json.has("marketingTag") && !json.isNull("marketingTag")) {
                mallSearchGoods.marketingTag = json.getString("marketingTag");
            }

            if (json.has("classificationId") && !json.isNull("classificationId")) {
                mallSearchGoods.classificationId = json.getInt("classificationId");
            }
            if (json.has("groupId") && !json.isNull("groupId")) {
                mallSearchGoods.groupId = json.getInt("groupId");
            }
            if (json.has("primeCosts") && !json.isNull("primeCosts")) {
                mallSearchGoods.primeCosts = json.getInt("primeCosts");
            }
            if (json.has("retailPrice") && !json.isNull("retailPrice")) {
                mallSearchGoods.retailPrice = json.getDouble("retailPrice");
            }
            if (json.has("marketValue") && !json.isNull("marketValue")) {
                mallSearchGoods.marketValue = json.getDouble("marketValue");
            }
            if (json.has("stock") && !json.isNull("stock")) {
                mallSearchGoods.stock = json.getInt("stock");
            }
            if (json.has("saleAmount") && !json.isNull("saleAmount")) {
                mallSearchGoods.saleAmount = json.getInt("saleAmount");
            }
            if (json.has("canNum") && !json.isNull("canNum")) {
                mallSearchGoods.canNum = json.getInt("canNum");
            }
            if (json.has("vipCanNum") && !json.isNull("vipCanNum")) {
                mallSearchGoods.vipCanNum = json.getInt("vipCanNum");
            }
            if (json.has("thumbnail") && !json.isNull("thumbnail")) {
                mallSearchGoods.thumbnail = json.getString("thumbnail");
            }
            if (json.has("banner") && !json.isNull("banner")) {
                mallSearchGoods.banner = json.getString("banner");
            }
            if (json.has("introPic") && !json.isNull("introPic")) {
                mallSearchGoods.introPic = json.getString("introPic");
            }
            if (json.has("restrict") && !json.isNull("restrict")) {
                mallSearchGoods.restrict = json.getString("restrict");
            }
            if (json.has("status") && !json.isNull("status")) {
                mallSearchGoods.status = json.getString("status");
            }
            if (json.has("shelvesTime") && !json.isNull("shelvesTime")) {
                mallSearchGoods.shelvesTime = json.getString("shelvesTime");
            }
            if (json.has("isDel") && !json.isNull("isDel")) {
                mallSearchGoods.isDel = json.getString("isDel");
            }
            if (json.has("created") && !json.isNull("created")) {
                mallSearchGoods.created = json.getString("created");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallSearchGoods;
    }


    /**
     *
     "id": 2,        //商品id
     "glwpetCommodityBarCode": null,     //glw商品条码
     "commodityNum": null,       //商品编码
     "title": "商品2主标题",      //商品标题
     "subtitle": "商品2副标题",   //商品副标题
     "specName": "3规格显示名称",  //规格显示名称
     "marketingTag": "3商品营销标签",      //商品营销标签
     "classificationId": 18,     //分类id
     "groupId": 1,               //分组id
     "primeCosts": 200.00,       //进货价格
     "retailPrice": 20.00,       //零售价格
     "marketValue": 30.00,       //市场价
     "stock": 500,       //库存
     "saleAmount": 20,   //销量
     "canNum": 150,      //罐头数
     "vipCanNum": 110,   //vip罐头数
     "thumbnail": "http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com\\mall\\15042561539978998005.png",       //缩略图
     "banner": null,
     "introPic": null,
     "restrict": null,
     "status": null,
     "shelvesTime": null,
     "isDel": null,
     "created": null
     */
}
