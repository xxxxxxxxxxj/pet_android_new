package com.pet.baseapi.domain;

/**
 * Created by zarkshao on 2017/4/26.
 * 接口请求 base地址 分测试环境 正式环境
 */

public class Env {

    public static enum EnvMode {
        TEST("test"),  // 测试
        DEMO("'demo'"), // 预发
        ONLINE("online"); // 线上;
        private final String value;
        private EnvMode (String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static EnvMode envMode = EnvMode.DEMO;

    public static String getNodeApiServerPrefix() {
        switch (envMode) {

            case TEST:
                return "http://192.168.0.252/pet-api/";

            case DEMO:
                return "https://demo.cwjia.cn/pet-api/";

            case ONLINE:
                return "https://api.ichongwujia.com/";
            default:
                return "https://api.ichongwujia.com/";
        }
    }

}
