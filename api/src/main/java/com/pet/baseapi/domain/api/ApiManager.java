package com.pet.baseapi.domain.api;


import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.pet.baseapi.BaseApiApp;
import com.pet.baseapi.domain.Env;
import com.pet.baseapi.domain.api.cache.SetCookieCache;
import com.pet.baseapi.domain.api.converter.ResponseConverterFactory;
import com.pet.baseapi.domain.api.cookie.ClearCookieJar;
import com.pet.baseapi.domain.api.cookie.PersistentCookieJar;
import com.pet.baseapi.domain.api.cookie.SharedPrefsCookiePersistor;
import com.pet.baseapi.util.DeviceIDUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

/**
 * ApiManager
 *
 * @author justin
 * @date 16/3/8
 */
public class ApiManager {

    public static String loginDeviceUdid = "";

    private HashMap<Class, Retrofit> SERVICE_RETROFIT_BIND = new HashMap<>();

    private Retrofit retrofit_webapi;
    private ConcurrentHashMap<Class, Object> cachedApis = new ConcurrentHashMap<>();

    public ApiManager() {
        builderRetrofit();
        //添加APIService
        SERVICE_RETROFIT_BIND.put(ApiService.class, retrofit_webapi);
    }

    private void builderRetrofit()
    {
        // init cookie manager
        ClearCookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApiApp.getAppCtx()));
        // init okhttp 3 logger
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // init cache
        File httpCacheDirectory = new File(BaseApiApp.getAppCtx().getExternalCacheDir(), "RespCache");

        OkHttpClient.Builder builder = new OkHttpClient.Builder()

                .cookieJar(cookieJar)
                // 20M SD-Card Cache
                .cache(new Cache(httpCacheDirectory, 20 * 1024 * 1024))
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(55, TimeUnit.SECONDS)
                .addNetworkInterceptor(mCommonInfoInterceptor)
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addNetworkInterceptor(logInterceptor)
                .authenticator(mAuthenticator);
        OkHttpClient client = builder.build();

        Log.i("http://","新： "+ Env.getNodeApiServerPrefix());
        retrofit_webapi = new Retrofit.Builder()
                .baseUrl(Env.getNodeApiServerPrefix())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ResponseConverterFactory.create())
                .build();
    }

    public <T> void addService(Class<T> clz) {
        builderRetrofit();
        SERVICE_RETROFIT_BIND.put(clz, retrofit_webapi);
    }

    public <T> void clearService() {
        cachedApis.clear();
        SERVICE_RETROFIT_BIND.clear();
    }

    public int getSize()
    {
        return SERVICE_RETROFIT_BIND.size();
    }


    public <T> T getService(Class<T> clz) {
        Object obj = cachedApis.get(clz);
        if (obj != null) {
            return (T) obj;
        } else {
            Retrofit retrofit = SERVICE_RETROFIT_BIND.get(clz);
            if (retrofit != null) {
                //创建一个服务请求
                T service = retrofit.create(clz);
                cachedApis.put(clz, service);
                return service;
            } else {
                return null;
            }
        }
    }

    private Interceptor mCommonInfoInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Map<String, String> added = new HashMap<>();
            //固定参数添加
            String cellphone = "";
            if (!TextUtils.isEmpty(cellphone)){
                added.put("cellphone",cellphone);
            }
            added.put("system",TextUtils.concat(DeviceIDUtil.getSource(),"_",
                    DeviceIDUtil.getCurrentVersion(BaseApiApp.getInstance())).toString());

            String imei = DeviceIDUtil.readDeviceID(BaseApiApp.getAppCtx());
            if (!TextUtils.isEmpty(imei)) {
                added.put("imei", imei);
            }
            added.put("account", "1");
            added.put("phoneModel", android.os.Build.BRAND + " "
                    + android.os.Build.MODEL);
            added.put("phoneSystemVersion", "Android "
                    + android.os.Build.VERSION.RELEASE);
            added.put("petTimeStamp", String.valueOf(System.currentTimeMillis()));


            //判断请求头
            Headers headers = request.headers();
            boolean flag = true;
            for (int i = 0; i < headers.size(); i++)
            {
                String value = headers.value(i);
                System.out.println("json name : "+value);
                if ("application/json".equals(value))
                {
                    flag = false;
                }
                break;
            }
            Request newRequest = interceptFormBody(request, added,flag);
            if (newRequest.body() != null) {
                newRequest = newRequest.newBuilder()
                        .header("Content-Length", newRequest.body().contentLength() + "")
                        .build();
            } else {
            /*NO OP*/
            }
            return chain.proceed(newRequest);
        }
    };

    Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl())
                    .build();
        }
    };

    private String cacheControl() {
        String cacheHeaderValue;
        if (NetworkUtils.isConnected()) {
            // read from cache for 1 minute
            int maxAge = 33;
            cacheHeaderValue = "public, max-age=" + maxAge;
        } else {
            // tolerate 4-weeks stale
            int maxStale = 60 * 60 * 24 * 28;
            cacheHeaderValue = "public, only-if-cached, max-stale=" + maxStale;
        }
        return cacheHeaderValue;
    }

    Authenticator mAuthenticator = new Authenticator() {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            return response.request().newBuilder()
                    .addHeader("Authorization", "newAccessToken")
                    .build();
        }
    };

    public static Request interceptFormBody(Request request, Map<String, String> added,boolean flag)
            throws IOException {
        RequestBody requestBody = request.body();
        //表单方式
        if (requestBody instanceof FormBody) {
            FormBody formBody = (FormBody) requestBody;
            System.out.println("json  以新请求的方式添加 FormBody "+formBody.contentType());
            FormBody.Builder formBuilder = new FormBody.Builder();
            //添加原来的参数
            if (formBody.size() > 0) {
                for (int idx = 0; idx < formBody.size(); idx++) {
                    System.out.println("json  name :"+formBody.name(idx)+"  value "+formBody.value(idx));
                    formBuilder.add(formBody.name(idx),
                            formBody.value(idx));
                }
            }
            //添加现在的参数
            if (added != null && added.size() > 0) {
                for (Map.Entry<String, String> entry : added.entrySet()) {
                    formBuilder.add(entry.getKey(), entry.getValue());
                }
            }
            return request.newBuilder().post(formBuilder.build()).build();
        }
        //多部分上传
        else if (requestBody instanceof MultipartBody) {
            System.out.println("json  多部分上传 MultipartBody ");
            MultipartBody multipartBody = (MultipartBody) requestBody;
            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
            //添加原来的
            if (multipartBody.size() > 0) {
                for (int idx = 0; idx < multipartBody.size(); idx++) {
                    multipartBuilder.addPart(multipartBody.part(idx));
                }
            }
            //添加 以文本格式添加
            if (added != null && added.size() > 0) {
                for (Map.Entry<String, String> entry : added.entrySet()) {
                    multipartBuilder.addFormDataPart(
                            entry.getKey(),
                            null,
                            RequestBody.create(MediaType.parse("text/plain; charset=UTF-8"), entry.getValue())
                    );
                }
            }
            multipartBody = multipartBuilder.build();
            //添加头部局的格式 need update boundary
            return request.newBuilder().post(multipartBody)
                    .header("Content-Type", "multipart/form-data; boundary=" + multipartBody.boundary())
                    .build();
        }
        //并且不是json
        else if (requestBody != null && flag) {
            //post请求
            if ("POST".equals(request.method())) {
                //默认表单请求方式
                FormBody.Builder formBuilder = new FormBody.Builder();
                //添加新请求
                if (added != null && added.size() > 0) {
                    for ( Map.Entry<String, String> entry : added.entrySet()) {
                        formBuilder.add(entry.getKey(), entry.getValue());
                    }
                }
                System.out.println("json  以新请求的方式添加");
                //以新请求的方式添加
                return request.newBuilder().post(formBuilder.build()).header(
                        "Content-Type",
                        "application/x-www-form-urlencoded;charset=utf-8"
                ).build();
            }
            //get请求
            else {
                System.out.println("json  get请求  ");
                StringBuilder sbParam = new StringBuilder("");
                //添加请求参数
                if (added != null && added.size() > 0) {
                    for (Map.Entry<String, String> entry : added.entrySet()) {
                        sbParam.append("&")
                                .append(entry.getKey())
                                .append("=")
                                .append(URLEncoder.encode(entry.getValue()));
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Sink sink = Okio.sink(baos);
                    BufferedSink bufferedSink = Okio.buffer(sink);

                    /**
                     * Write old params
                     * */
                    request.body().writeTo(bufferedSink);

                    /**
                     * write to buffer additional params  添加参数
                     * */
                    bufferedSink.writeString(sbParam.toString(), Charset.defaultCharset());

                    RequestBody newRequestBody = RequestBody.create(
                            request.body().contentType(),
                            bufferedSink.buffer().readUtf8()
                    );
                    return request.newBuilder().post(newRequestBody).build();
                }


            }

        }

        return request;
    }

}
