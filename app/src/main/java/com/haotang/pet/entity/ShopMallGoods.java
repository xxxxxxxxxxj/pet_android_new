package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/5 18:03
 */
public class ShopMallGoods {
    private String titlePicDomain;
    private List<Goods> goodsList;

    public static ShopMallGoods json2Entity(JSONObject json) {
        ShopMallGoods shopMallGoods = new ShopMallGoods();
        try {
            if (json.has("titlePicDomain") && !json.isNull("titlePicDomain")) {
                shopMallGoods.setTitlePicDomain(json.getString("titlePicDomain"));
            }
            if (json.has("picsDomain") && !json.isNull("picsDomain")) {
                JSONArray picsDomain = json.getJSONArray("picsDomain");
                if (picsDomain != null && picsDomain.length() > 0) {
                    List<Goods> goodsList = new ArrayList<Goods>();
                    goodsList.clear();
                    for (int i = 0; i < picsDomain.length(); i++) {
                        goodsList.add(Goods
                                .json2Entity(picsDomain
                                        .getJSONObject(i)));
                    }
                    shopMallGoods.setGoodsList(goodsList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopMallGoods;
    }

    public String getTitlePicDomain() {
        return titlePicDomain;
    }

    public void setTitlePicDomain(String titlePicDomain) {
        this.titlePicDomain = titlePicDomain;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
