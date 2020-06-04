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
 * @date XJ on 2017/9/4 16:21
 */
public class MallOrderDetailGoods implements Serializable {
    private String expressNo;
    private String express;
    private List<MallOrderDetailGoodItems> mallOrderDetailGoodItemsList;
    private String infoMsg;
    private int logId;
    private MallOrderDetailGoodsLogisticsInfo mallOrderDetailGoodsLogisticsInfo;

    public static MallOrderDetailGoods json2Entity(JSONObject json) {
        MallOrderDetailGoods mallOrderDetailGoods = new MallOrderDetailGoods();
        try {
            if (json.has("info") && !json.isNull("info")) {
                mallOrderDetailGoods.setMallOrderDetailGoodsLogisticsInfo(MallOrderDetailGoodsLogisticsInfo.json2Entity(json.getJSONObject("info")));
            }
            if (json.has("infoMsg") && !json.isNull("infoMsg")) {
                mallOrderDetailGoods.setInfoMsg(json.getString("infoMsg"));
            }
            if (json.has("logId") && !json.isNull("logId")) {
                mallOrderDetailGoods.setLogId(json.getInt("logId"));
            }
            if (json.has("expressNo") && !json.isNull("expressNo")) {
                mallOrderDetailGoods.setExpressNo(json.getString("expressNo"));
            }
            if (json.has("express") && !json.isNull("express")) {
                mallOrderDetailGoods.setExpress(json.getString("express"));
            }
            if (json.has("items") && !json.isNull("items")) {
                JSONArray itemsList = json.getJSONArray("items");
                if (itemsList != null && itemsList.length() > 0) {
                    List<MallOrderDetailGoodItems> mallOrderDetailGoodItemsList = new ArrayList<MallOrderDetailGoodItems>();
                    mallOrderDetailGoodItemsList.clear();
                    for (int i = 0; i < itemsList.length(); i++) {
                        mallOrderDetailGoodItemsList.add(MallOrderDetailGoodItems
                                .json2Entity(itemsList
                                        .getJSONObject(i)));
                    }
                    mallOrderDetailGoods.setMallOrderDetailGoodItemsList(mallOrderDetailGoodItemsList);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "goods异常 = " + e.toString());
            e.printStackTrace();
        }
        return mallOrderDetailGoods;
    }

    public MallOrderDetailGoodsLogisticsInfo getMallOrderDetailGoodsLogisticsInfo() {
        return mallOrderDetailGoodsLogisticsInfo;
    }

    public void setMallOrderDetailGoodsLogisticsInfo(MallOrderDetailGoodsLogisticsInfo mallOrderDetailGoodsLogisticsInfo) {
        this.mallOrderDetailGoodsLogisticsInfo = mallOrderDetailGoodsLogisticsInfo;
    }

    public String getInfoMsg() {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public List<MallOrderDetailGoodItems> getMallOrderDetailGoodItemsList() {
        return mallOrderDetailGoodItemsList;
    }

    public void setMallOrderDetailGoodItemsList(List<MallOrderDetailGoodItems> mallOrderDetailGoodItemsList) {
        this.mallOrderDetailGoodItemsList = mallOrderDetailGoodItemsList;
    }
}
