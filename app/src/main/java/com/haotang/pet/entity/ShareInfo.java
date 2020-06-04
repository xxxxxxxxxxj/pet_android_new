package com.haotang.pet.entity;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/29 15:01
 */
public class ShareInfo {
    private Activity context;
    private String shareImg;
    private String shareTitle;
    private String shareTxt;
    private String shareUrl;
    private String channel;
    private String path;
    private Bitmap bitmap;
    private int type;

    public ShareInfo(Activity context, String shareImg, String shareTitle, String shareTxt, String shareUrl, String channel, Bitmap bitmap, int type) {
        this.context = context;
        this.shareImg = shareImg;
        this.shareTitle = shareTitle;
        this.shareTxt = shareTxt;
        this.shareUrl = shareUrl;
        this.channel = channel;
        this.bitmap = bitmap;
        this.type = type;
    }

    public ShareInfo(Activity context, String shareImg, String shareTitle, String shareTxt, String shareUrl, String channel, Bitmap bitmap, int type,String path) {
        this.context = context;
        this.shareImg = shareImg;
        this.shareTitle = shareTitle;
        this.shareTxt = shareTxt;
        this.shareUrl = shareUrl;
        this.channel = channel;
        this.path = path;
        this.bitmap = bitmap;
        this.type = type;
    }

    public ShareInfo() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareTxt() {
        return shareTxt;
    }

    public void setShareTxt(String shareTxt) {
        this.shareTxt = shareTxt;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
