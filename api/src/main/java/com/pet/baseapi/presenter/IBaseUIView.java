package com.pet.baseapi.presenter;

import androidx.annotation.NonNull;

import io.reactivex.disposables.Disposable;

/**
 * @author zarkshao
 * @date 2017/5/3
 */

public interface IBaseUIView {

    void showLoading(Object... objects);

    void hideLoading(Object... objects);

    void showInterLoading(Object... objects);
    void hideIndexLoading(Object... objects);

    void showEmpty(Object... objects);

    void onSuccess(Object... objects);

    void onError(Object... objects);

    void tokenN0Avail();

    void onFinish();

    void addDisposable(@NonNull Disposable d);

    void removeDisposable(@NonNull Disposable d);

    /**
     *  网络异常
     * @param objects 异常信息
     */
    void noNetError(Object... objects);
}
