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
 * @date zhoujunxia on 2019/3/21 11:29
 */
public class MyCard {
    private int id;
    private int templateId;
    private String cardTypeName;
    private String discountText;
    private List<String> shops = new ArrayList<String>();
    private String cardNumber;
    private String expireTime;
    private double amount;
    private double discount;
    private String cardText;
    private String shopText;
    private String mineCardPic;
    private String state;
    private String smallPic;
    private String reason;
    private boolean isSelect;
    private boolean isLast;
    private String discountTypeStr;
    private String dicountDesc;
    private boolean isAvail;
    private boolean isShow;
    private boolean isHaveUnAvailCard;
    private int position = -1;
    private int cardType;

    @Override
    public String toString() {
        return "MyCard{" +
                "id=" + id +
                ", templateId=" + templateId +
                ", cardTypeName='" + cardTypeName + '\'' +
                ", discountText='" + discountText + '\'' +
                ", shops=" + shops +
                ", cardNumber='" + cardNumber + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", amount=" + amount +
                ", discount=" + discount +
                ", cardText='" + cardText + '\'' +
                ", shopText='" + shopText + '\'' +
                ", mineCardPic='" + mineCardPic + '\'' +
                ", state='" + state + '\'' +
                ", smallPic='" + smallPic + '\'' +
                ", reason='" + reason + '\'' +
                ", isSelect=" + isSelect +
                ", isLast=" + isLast +
                '}';
    }

    public static MyCard json2Entity(JSONObject jobj) {
        MyCard myCard = new MyCard();
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setLength(0);
            if (jobj.has("dicountDesc") && !jobj.isNull("dicountDesc")) {
                JSONArray jarrdicountDesc = jobj.getJSONArray("dicountDesc");
                if (jarrdicountDesc != null && jarrdicountDesc.length() > 0) {
                    for (int j = 0; j < jarrdicountDesc.length(); j++) {
                        if (j == jarrdicountDesc.length() - 1) {
                            stringBuffer.append(jarrdicountDesc.getString(j));
                        } else {
                            stringBuffer.append(jarrdicountDesc.getString(j) + "|");
                        }
                    }
                    myCard.setDicountDesc(stringBuffer.toString());
                }
            }
            if (jobj.has("discountTypeStr") && !jobj.isNull("discountTypeStr")) {
                myCard.setDiscountTypeStr(jobj.getString("discountTypeStr"));
            }
            if (jobj.has("discount") && !jobj.isNull("discount")) {
                myCard.setDiscount(jobj.getDouble("discount"));
            }
            if (jobj.has("shopText") && !jobj.isNull("shopText")) {
                myCard.setShopText(jobj.getString("shopText"));
            }
            if (jobj.has("mineCardPic") && !jobj.isNull("mineCardPic")) {
                myCard.setMineCardPic(jobj.getString("mineCardPic"));
            }
            if (jobj.has("state") && !jobj.isNull("state")) {
                myCard.setState(jobj.getString("state"));
            }
            if (jobj.has("id") && !jobj.isNull("id")) {
                myCard.setId(jobj.getInt("id"));
            }
            if (jobj.has("templateId") && !jobj.isNull("templateId")) {
                myCard.setTemplateId(jobj.getInt("templateId"));
            }
            if (jobj.has("cardTypeName") && !jobj.isNull("cardTypeName")) {
                myCard.setCardTypeName(jobj.getString("cardTypeName"));
            }
            if (jobj.has("discountText") && !jobj.isNull("discountText")) {
                myCard.setDiscountText(jobj.getString("discountText"));
            }
            if (jobj.has("cardNumber") && !jobj.isNull("cardNumber")) {
                myCard.setCardNumber(jobj.getString("cardNumber"));
            }
            if (jobj.has("expireTime") && !jobj.isNull("expireTime")) {
                myCard.setExpireTime(jobj.getString("expireTime"));
            }
            if (jobj.has("amount") && !jobj.isNull("amount")) {
                myCard.setAmount(jobj.getDouble("amount"));
            }
            if (jobj.has("cardText") && !jobj.isNull("cardText")) {
                myCard.setCardText(jobj.getString("cardText"));
            }
            if (jobj.has("shops") && !jobj.isNull("shops")) {
                JSONArray jarrShops = jobj.getJSONArray("shops");
                myCard.shops.clear();
                if (jarrShops != null && jarrShops.length() > 0) {
                    for (int i = 0; i < jarrShops.length(); i++) {
                        myCard.shops.add(jarrShops.getString(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myCard;
    }

    public MyCard() {
    }

    public MyCard(boolean isLast) {
        this.isLast = isLast;
    }

    public boolean isHaveUnAvailCard() {
        return isHaveUnAvailCard;
    }

    public void setHaveUnAvailCard(boolean haveUnAvailCard) {
        isHaveUnAvailCard = haveUnAvailCard;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isAvail() {
        return isAvail;
    }

    public void setAvail(boolean avail) {
        isAvail = avail;
    }

    public String getDicountDesc() {
        return dicountDesc;
    }

    public void setDicountDesc(String dicountDesc) {
        this.dicountDesc = dicountDesc;
    }

    public String getDiscountTypeStr() {
        return discountTypeStr;
    }

    public void setDiscountTypeStr(String discountTypeStr) {
        this.discountTypeStr = discountTypeStr;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getShopText() {
        return shopText;
    }

    public void setShopText(String shopText) {
        this.shopText = shopText;
    }

    public String getMineCardPic() {
        return mineCardPic;
    }

    public void setMineCardPic(String mineCardPic) {
        this.mineCardPic = mineCardPic;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getDiscountText() {
        return discountText;
    }

    public void setDiscountText(String discountText) {
        this.discountText = discountText;
    }

    public List<String> getShops() {
        return shops;
    }

    public void setShops(List<String> shops) {
        this.shops = shops;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }
}
