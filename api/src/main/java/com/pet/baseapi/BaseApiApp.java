package com.pet.baseapi;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.pet.baseapi.domain.api.ApiManager;


/**
 *
 * @author zarkshao
 * @date 2017/4/26
 */

public class BaseApiApp extends Application {
    private static BaseApiApp appCtx;
    /**网络求情管理器*/
    private ApiManager apiManager = null;
    protected static String appType = "1";
    protected static String deviceID = "nofinde";

    public static BaseApiApp getInstance() {
        return appCtx;
    }

    public static String getAppType() {
        return appType;
    }

    public static String getDeviceID() {
        return deviceID;
    }

    public static void setDeviceID(String deviceID) {
        BaseApiApp.deviceID = deviceID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appCtx = this;
        //初始化网络请求管理
        apiManager = new ApiManager();
        Utils.init(this);
    }

    /**
     * 获取api管理类
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T apiService(Class<T> clz) {
        return getInstance().apiManager.getService(clz);
    }

    public <T> void addApiService(Class<T> clz) {
        getInstance().apiManager.addService(clz);
    }
    public <T> void clearService() {
            getInstance().apiManager.clearService();
    }

    public int getSize()
    {
        return getInstance().apiManager.getSize();
    }


    public static Context getAppCtx() {
        return appCtx;
    }

}
