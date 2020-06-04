package com.haotang.pet.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/2/7 11:05
 */
public class MallOrderDetailGoodsLogisticsInfo implements Serializable {
    private String ftime;
    private String context;

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public static MallOrderDetailGoodsLogisticsInfo json2Entity(JSONObject json) {
        MallOrderDetailGoodsLogisticsInfo mallOrderDetailGoodsLogisticsInfo = new MallOrderDetailGoodsLogisticsInfo();
        try {
            if (json.has("ftime") && !json.isNull("ftime")) {
                mallOrderDetailGoodsLogisticsInfo.setFtime(json.getString("ftime"));
            }
            if (json.has("context") && !json.isNull("context")) {
                mallOrderDetailGoodsLogisticsInfo.setContext(json.getString("context"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mallOrderDetailGoodsLogisticsInfo;
    }
}
