package com.pet.baseapi.presenter;

import android.content.Context;

/**
 * Created by zarkshao on 2017/5/3.
 * 所有的Presenter类必须加注释
 * 所有网络请求 业务逻辑 列:添加缓存 获取缓存 网络请求 数据处理等 都在Presenter中处理
 * 确保没一个逻辑处理为一个方法 列getUserData() getUserDataForCache();
 * activity 不处理任何业务逻辑
 */

public class BasePresenter<T extends IBaseUIView> {
    protected Context mContext;

    protected T mView;

    public BasePresenter(Context context) {
        this.mContext = context;
    }

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detach() {
        mView = null;
        mContext = null;
    }

}
