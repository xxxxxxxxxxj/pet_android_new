package com.pet.baseapi.domain.api.converter;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.pet.baseapi.bean.BaseResponse;
import com.pet.baseapi.domain.api.exception.ResultException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    JsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            LogUtils.d("Network", "response>>" + response);
            BaseResponse resultResponse = gson.fromJson(response, BaseResponse.class);
            if (resultResponse.success) {
                return gson.fromJson(response, type);
            } else {
                throw new ResultException(resultResponse.getCode(), resultResponse.getMessage());
            }
        } finally {

        }

    }
}