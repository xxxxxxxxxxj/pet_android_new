package com.pet.baseapi.domain.callback;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.pet.baseapi.BaseApiApp;
import com.pet.baseapi.bean.BaseResponse;
import com.pet.baseapi.domain.error.ErrorDataResult;
import com.pet.baseapi.domain.rx.RxSchedulers;
import com.pet.baseapi.presenter.IBaseUIView;
import com.pet.baseapi.util.FileCachePathUtil;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 *
 * @author zarkshao
 * @date 2017/5/18
 */

public abstract class BaseCallBack<T extends BaseResponse> implements Observer<T> {
    private IBaseUIView mIBaseUIView;
    private Disposable mDisposable;

    public BaseCallBack(@NonNull IBaseUIView iBaseUIView) {
        this.mIBaseUIView = iBaseUIView;
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mDisposable = d;
        mIBaseUIView.addDisposable(d);
        getCache();
    }

    @Override
    public void onComplete() {
        onFinish();
        if (mIBaseUIView != null && mDisposable != null)
        {
            mIBaseUIView.removeDisposable(mDisposable);
        }
    }


    @Override
    public void onNext(@NonNull T t) {
        if (!TextUtils.isEmpty(getCacheName())) {
            insertCache(t);
        }
        BaseResponse response = t;
        //token 失效
        if (response.getCode() == 402)
        {
            Log.i("token 失效：","402");
            mIBaseUIView.tokenN0Avail();
        }
        else
        {
            onSuccess(t);
        }
    }


    @Override
    public void onError(@NonNull Throwable e) {
        ErrorDataResult.processError(e);
        if (mIBaseUIView != null && mDisposable != null)
        {
            mIBaseUIView.removeDisposable(mDisposable);
        }
        if (e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof UnknownHostException)
        {
            onNoNetError(e);
        }
        onFinish(e);
    }

    public Disposable getmDisposable() {
        return mDisposable;
    }

    public IBaseUIView getmIBaseUIView() {
        return mIBaseUIView;
    }

    protected abstract void onSuccess(@NonNull T t);

    protected abstract void onFinish(Object... objects);

    protected   void onNoNetError(@NonNull Throwable e){

        if (mIBaseUIView != null)
        {
            mIBaseUIView.noNetError(e);
        }
    }


    /**
     * 是否添加本地文件缓存
     *
     * @return
     */
    public String getCacheName() {
        return "";
    }

    public void onCacheData(T t) {

    }

    private void getCache() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        final Type tType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        final File file = FileCachePathUtil.getCacheNetWorkCache(BaseApiApp.getAppCtx(), getCacheName());
        if (file.exists()) {
            Observable<T> observable = Observable.create(new ObservableOnSubscribe<T>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                    BufferedSource buffer = null;
                    try {
                        Source source = Okio.source(file);
                        buffer = Okio.buffer(source);
                        String s = buffer.readUtf8();
                        T o = new Gson().fromJson(s, tType);
                        e.onNext(o);
                    } catch (Exception ex) {

                    } finally {
                        closeQuietly(buffer);
                    }
                }
            });

            observable.compose(RxSchedulers.<T>io_main())
                    .subscribe(new Consumer<T>() {
                        @Override
                        public void accept(@NonNull T t) throws Exception {
                            onCacheData(t);
                        }
                    });
        }

    }

    /**
     * 添加缓存
     *
     * @param t
     */
    @SuppressLint("CheckResult")
    private void insertCache(T t) {
        Observable.just(t)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(@NonNull T d) throws Exception {
                        File file = FileCachePathUtil.getCacheNetWorkCache(BaseApiApp.getAppCtx(), getCacheName());
                        if (file.exists()) {
                            file.delete();
                        }
                        BufferedSink bufferedSink = null;
                        try {
                            Sink okIo = Okio.sink(file);
                            bufferedSink = Okio.buffer(okIo);
                            bufferedSink.writeUtf8(new Gson().toJson(d));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        } finally {
                            closeQuietly(bufferedSink);
                        }
                    }
                });
    }

    private void closeQuietly(Closeable closeable) {

        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }

    }


}
