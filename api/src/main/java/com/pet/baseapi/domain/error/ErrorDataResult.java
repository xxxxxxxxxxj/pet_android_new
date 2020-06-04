package com.pet.baseapi.domain.error;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.pet.baseapi.BaseApiApp;
import com.pet.baseapi.R;
import com.pet.baseapi.domain.api.exception.ResultException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 *
 * @author zarkshao
 * @date 2017/5/18
 */

public class ErrorDataResult {
    private static final String TAG = "ErrorDataResult";
    @SuppressLint("CheckResult")
    public static void processError(Throwable throwable) {
        Observable.just(throwable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        if (e != null) {
                            if (e instanceof SocketTimeoutException) {
                                LogUtils.e(TAG, "onError: SocketTimeoutException----");
                                ToastUtils.showLongSafe(BaseApiApp.getAppCtx().getString(R.string.net_error));
                            }
                            else if (e instanceof ConnectException) {
                                LogUtils.e(TAG, "onError: ConnectException-----");
                                ToastUtils.showLongSafe(BaseApiApp.getAppCtx().getString(R.string.net_error));
                            }
                            else if (e instanceof ClassCastException)
                            {
                                ToastUtils.showLongSafe("类型转换出错");
                            }

                            else if (e instanceof UnknownHostException) {
                                LogUtils.e(TAG, "onError: UnknownHostException-----");
                                ToastUtils.showLongSafe(BaseApiApp.getAppCtx().getString(R.string.net_error));
                            }
                            else if ((e instanceof JsonSyntaxException) || (e instanceof
                                    NumberFormatException) || (e instanceof MalformedJsonException)) {
                                LogUtils.i("数据解析出错，"+e.getMessage());
                                ToastUtils.showLongSafe("数据解析出错");
                            }
                            else if ((e instanceof HttpException)) {
                                ToastUtils.showLongSafe("服务器错误(" + ((HttpException) e).code()+")");
                                //自动上报这个异常
                            }
                            else if (e instanceof NullPointerException) {

                                ToastUtils.showLong("空指针异常");
                                Log.i("空指针","异常了啊");

                                //自动上报这个异常
                            }
                            else if (e instanceof ResultException) {
                                ResultException resultException = (ResultException) e;
                                if (resultException.errCode == ResultException.EXCEPTION_CODE_COOKIE_INVALID
                                        || resultException.errCode == ResultException.EXCEPTION_CODE_COOKIE_VALIDATE
                                        || resultException.errCode == ResultException.EXCEPTION_CODE_LOGINED_OTHER_DEVICE) {

                                }
                                else {
                                    ToastUtils.showLongSafe(resultException.msg);
                                }
                            }
                            else {
                                ToastUtils.showLongSafe(BaseApiApp.getAppCtx().getString(R.string.no_exception));
                            }

                        }
                    }
                });
    }
}
