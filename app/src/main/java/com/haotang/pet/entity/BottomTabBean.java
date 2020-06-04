package com.haotang.pet.entity;

import android.text.TextUtils;

import com.haotang.pet.util.CommUtil;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-12 14:15
 */
public class BottomTabBean {
    private String title;
    private String pic;
    private String picH;

    public static BottomTabBean json2Entity(JSONObject jobj) {
        BottomTabBean bottomTabBean = new BottomTabBean();
        try {
            if (jobj.has("title") && !jobj.isNull("title")) {
                bottomTabBean.setTitle(jobj.getString("title"));
            }
            if (jobj.has("pic") && !jobj.isNull("pic")) {
                String pic = "";
                if (jobj.getString("pic") != null && !TextUtils.isEmpty(jobj.getString("pic"))) {
                    if (!jobj.getString("pic").startsWith("http://")
                            && !jobj.getString("pic").startsWith("https://")) {
                        pic = CommUtil.getStaticUrl() + jobj.getString("pic");
                    } else {
                        pic = jobj.getString("pic");
                    }
                }
                bottomTabBean.setPic(pic);
            }
            if (jobj.has("picH") && !jobj.isNull("picH")) {
                String picH = "";
                if (jobj.getString("picH") != null && !TextUtils.isEmpty(jobj.getString("picH"))) {
                    if (!jobj.getString("picH").startsWith("http://")
                            && !jobj.getString("picH").startsWith("https://")) {
                        picH = CommUtil.getStaticUrl() + jobj.getString("picH");
                    } else {
                        picH = jobj.getString("picH");
                    }
                }
                bottomTabBean.setPicH(picH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bottomTabBean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPicH() {
        return picH;
    }

    public void setPicH(String picH) {
        this.picH = picH;
    }
}
