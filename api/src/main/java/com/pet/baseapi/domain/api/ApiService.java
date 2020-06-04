package com.pet.baseapi.domain.api;


import com.pet.baseapi.resp.CodeResp;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Created by zarkshao on 2017/4/26.
 * ApiService 中方法命名和接口名称一致 例:api/login=apiLogin
 */

public interface ApiService {
    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("user/genVerifyCode")
    Observable<CodeResp>genVerifyCode(
            @Field("phone") String phone,
            @Field("encryptionCode") String encryptionCode,
            @Field("flag") int flag
    );

}
