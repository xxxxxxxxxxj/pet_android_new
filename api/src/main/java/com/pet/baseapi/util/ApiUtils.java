package com.pet.baseapi.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MultipartBody;

/**
 * api及请求相关工具
 *@author  Created by xing on 2017/10/27.
 */

public class ApiUtils {

    /**
     * 构建多类型参数
     * @param hashMap 需要构建的参数
     * @return 值
     */
    public static MultipartBody buildBody(HashMap<String,String> hashMap)
    {
        if (hashMap == null)
        {
            return null;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        Iterator<Map.Entry<String,String>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String,String> entry = iterator.next();
            builder.addFormDataPart(entry.getKey(),entry.getValue());
        }
        return builder.build();
    }


}
