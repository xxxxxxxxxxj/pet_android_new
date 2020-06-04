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
 * @date zhoujunxia on 2019-12-16 10:34
 */
public class FosterLive {
    private String roomContent;
    private String shopCellphone;
    private String img;
    private String liveUrl;
    private String roomPetContent;
    private String liveContent;
    private String shopName;
    private int liveState;
    private int cameraState;
    private List<String> roomPetAvatarList;

    public static FosterLive json2Entity(JSONObject jobj) {
        FosterLive fosterLive = new FosterLive();
        try {
            if (jobj.has("shopName") && !jobj.isNull("shopName")) {
                fosterLive.setShopName(jobj.getString("shopName"));
            }
            if (jobj.has("liveContent") && !jobj.isNull("liveContent")) {
                fosterLive.setLiveContent(jobj.getString("liveContent"));
            }
            if (jobj.has("liveState") && !jobj.isNull("liveState")) {
                fosterLive.setLiveState(jobj.getInt("liveState"));
            }
            if (jobj.has("cameraState") && !jobj.isNull("cameraState")) {
                fosterLive.setCameraState(jobj.getInt("cameraState"));
            }
            if (jobj.has("roomContent") && !jobj.isNull("roomContent")) {
                fosterLive.setRoomContent(jobj.getString("roomContent"));
            }
            if (jobj.has("shopCellphone") && !jobj.isNull("shopCellphone")) {
                fosterLive.setShopCellphone(jobj.getString("shopCellphone"));
            }
            if (jobj.has("liveUrl") && !jobj.isNull("liveUrl")) {
                fosterLive.setLiveUrl(jobj.getString("liveUrl"));
            }
            if (jobj.has("roomPetContent") && !jobj.isNull("roomPetContent")) {
                fosterLive.setRoomPetContent(jobj.getString("roomPetContent"));
            }
            if (jobj.has("img") && !jobj.isNull("img")) {
                fosterLive.setImg(jobj.getString("img"));
            }
            if (jobj.has("roomPetAvatarList") && !jobj.isNull("roomPetAvatarList")) {
                JSONArray jarrroomPetAvatarList = jobj.getJSONArray("roomPetAvatarList");
                if (jarrroomPetAvatarList.length() > 0) {
                    List<String> imgList = new ArrayList<String>();
                    for (int i = 0; i < jarrroomPetAvatarList.length(); i++) {
                        imgList.add(jarrroomPetAvatarList.getString(i));
                    }
                    fosterLive.setRoomPetAvatarList(imgList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fosterLive;
    }

    public int getLiveState() {
        return liveState;
    }

    public void setLiveState(int liveState) {
        this.liveState = liveState;
    }

    public int getCameraState() {
        return cameraState;
    }

    public void setCameraState(int cameraState) {
        this.cameraState = cameraState;
    }

    public String getRoomContent() {
        return roomContent;
    }

    public void setRoomContent(String roomContent) {
        this.roomContent = roomContent;
    }

    public String getShopCellphone() {
        return shopCellphone;
    }

    public void setShopCellphone(String shopCellphone) {
        this.shopCellphone = shopCellphone;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getRoomPetContent() {
        return roomPetContent;
    }

    public void setRoomPetContent(String roomPetContent) {
        this.roomPetContent = roomPetContent;
    }

    public List<String> getRoomPetAvatarList() {
        return roomPetAvatarList;
    }

    public void setRoomPetAvatarList(List<String> roomPetAvatarList) {
        this.roomPetAvatarList = roomPetAvatarList;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLiveContent() {
        return liveContent;
    }

    public void setLiveContent(String liveContent) {
        this.liveContent = liveContent;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
