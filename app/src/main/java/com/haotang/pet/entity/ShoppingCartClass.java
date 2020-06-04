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
 * @date XJ on 2017/9/1 19:06
 */
public class ShoppingCartClass {
    private String className;
    private List<ShoppingCart> shoppingCartList;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public ShoppingCartClass() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ShoppingCart> getShoppingCartList() {
        return shoppingCartList;
    }

    public void setShoppingCartList(List<ShoppingCart> shoppingCartList) {
        this.shoppingCartList = shoppingCartList;
    }

    public static ShoppingCartClass json2Entity(JSONObject json) {
        ShoppingCartClass shoppingCartClass = new ShoppingCartClass();
        try {
            if (json.has("className") && !json.isNull("className")) {
                shoppingCartClass.setClassName(json.getString("className"));
            }
            if (json.has("classList") && !json.isNull("classList")) {
                JSONArray classList = json.getJSONArray("classList");
                if (classList != null && classList.length() > 0) {
                    List<ShoppingCart> shoppingCartList = new ArrayList<ShoppingCart>();
                    shoppingCartList.clear();
                    for (int i = 0; i < classList.length(); i++) {
                        shoppingCartList.add(ShoppingCart
                                .json2Entity(classList
                                        .getJSONObject(i)));
                    }
                    shoppingCartClass.setShoppingCartList(shoppingCartList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoppingCartClass;
    }
}
