package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/2 09:57
 */
public class CanTransaction {
    public String icon;
    public String content;
    public String payTime;
    public int payNum;

    public static CanTransaction json2Entity(JSONObject json) {
        CanTransaction canTransaction = new CanTransaction();
        try {
            if (json.has("icon") && !json.isNull("icon")) {
                canTransaction.icon = json.getString("icon");
            }
            if (json.has("content") && !json.isNull("content")) {
                canTransaction.content = json.getString("content");
            }
            if (json.has("payTime") && !json.isNull("payTime")) {
                canTransaction.payTime = json.getString("payTime");
            }
            if (json.has("payNum") && !json.isNull("payNum")) {
                canTransaction.payNum = json.getInt("payNum");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canTransaction;
    }
}
