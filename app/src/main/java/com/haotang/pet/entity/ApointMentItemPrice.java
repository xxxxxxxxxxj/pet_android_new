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
 * @date zhoujunxia on 2018/8/21 11:49
 */
public class ApointMentItemPrice implements Serializable {
    private int itemId;
    private List<ApointMentItemPrices> itemPriceList;

    @Override
    public String toString() {
        return "ApointMentItemPrice{" +
                "itemId=" + itemId +
                ", itemPriceList=" + itemPriceList +
                '}';
    }

    public static ApointMentItemPrice json2Entity(JSONObject jobj) {
        ApointMentItemPrice apointMentItemPrice = new ApointMentItemPrice();
        try {
            if (jobj.has("itemId") && !jobj.isNull("itemId")) {
                apointMentItemPrice.setItemId(jobj.getInt("itemId"));
            }
            if (jobj.has("customerPets") && !jobj.isNull("customerPets")) {
                JSONArray jarrcustomerPets = jobj.getJSONArray("customerPets");
                if (jarrcustomerPets != null && jarrcustomerPets.length() > 0) {
                    List<ApointMentItemPrices> localItemPriceList = new ArrayList<ApointMentItemPrices>();
                    localItemPriceList.clear();
                    for (int i = 0; i < jarrcustomerPets.length(); i++) {
                        localItemPriceList.add(ApointMentItemPrices
                                .json2Entity(jarrcustomerPets
                                        .getJSONObject(i)));
                    }
                    apointMentItemPrice.setItemPriceList(localItemPriceList);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "items()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return apointMentItemPrice;
    }

    public List<ApointMentItemPrices> getItemPriceList() {
        return itemPriceList;
    }

    public void setItemPriceList(List<ApointMentItemPrices> itemPriceList) {
        this.itemPriceList = itemPriceList;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
