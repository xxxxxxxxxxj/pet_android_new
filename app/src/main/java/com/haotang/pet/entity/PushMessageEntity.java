package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {
 * "id": "1",      //用户消息id
 * "userId": "2",  //用户id
 * "newsId": "12", //消息id
 * "isRead": 0,        //是否阅读 0否 1是
 * "isDel": "0",       //是否删除 0否 1是
 * "createTime": "2015-07-10 10:47:29", //创建时间
 * "updateTime": "2015-07-10 10:47:29", //更新时间
 * "extendParam": {    //附加属性
 * "pushNews" : { //消息
 * "id": "1",  //消息id
 * "push_id": "128",  //推送id
 * "msg": "宠物家商城满99-30活动开始啦",  //推送内容
 * "read_count": "100",  //阅读量
 * "push_count": "100",  //发送量
 * "push_time": "2015-07-10 10:47:29", //推送时间
 * "type_code": "22",  //推送类型码
 * "push_param": "12345"  //推送的参数
 * }
 * }
 * }
 * Created by Administrator on 2018/3/16 0016.
 */

public class PushMessageEntity {
    public String id;
    public String userId;
    public String newsId;
    public int isRead;
    public int isDel;
    public String createTime;
    public String updateTime;
    public int push_id;
    public String msg;
    public int read_count;
    public int push_count;
    public String push_time;
    public String type_code;
    //以下都是跳转用的
    public int orderId;
    public String backup;
    public String ad_url;
    public String workerId;
    public String workerLevel;
    public String url;
    public String name;
    public String tag;
    public int postId;
    public int userId_circle;

    public static PushMessageEntity j2Entity(JSONObject object) {
        PushMessageEntity pushMessageEntity = new PushMessageEntity();
        try {
            if (object.has("id") && !object.isNull("id")) {
                pushMessageEntity.id = object.getString("id");
            }
            if (object.has("userId") && !object.isNull("userId")) {
                pushMessageEntity.userId = object.getString("userId");
            }
            if (object.has("newsId") && !object.isNull("newsId")) {
                pushMessageEntity.newsId = object.getString("newsId");
            }
            if (object.has("isRead") && !object.isNull("isRead")) {
                pushMessageEntity.isRead = object.getInt("isRead");
            }
            if (object.has("isDel") && !object.isNull("isDel")) {
                pushMessageEntity.isDel = object.getInt("isDel");
            }
            if (object.has("createTime") && !object.isNull("createTime")) {
                pushMessageEntity.createTime = object.getString("createTime");
            }
            if (object.has("updateTime") && !object.isNull("updateTime")) {
                pushMessageEntity.updateTime = object.getString("updateTime");
            }
            if (object.has("extendParam") && !object.isNull("extendParam")) {
                JSONObject objectExtend = object.getJSONObject("extendParam");
                if (objectExtend.has("pushNews") && !objectExtend.isNull("pushNews")) {
                    JSONObject objectPushNews = objectExtend.getJSONObject("pushNews");
                    if (objectPushNews.has("push_id") && !objectPushNews.isNull("push_id")) {
                        pushMessageEntity.push_id = objectPushNews.getInt("push_id");
                    }
                    if (objectPushNews.has("msg") && !objectPushNews.isNull("msg")) {
                        pushMessageEntity.msg = objectPushNews.getString("msg");
                    }
                    if (objectPushNews.has("read_count") && !objectPushNews.isNull("read_count")) {
                        pushMessageEntity.read_count = objectPushNews.getInt("read_count");
                    }
                    if (objectPushNews.has("push_count") && !objectPushNews.isNull("push_count")) {
                        pushMessageEntity.push_count = objectPushNews.getInt("push_count");
                    }
                    if (objectPushNews.has("pushTime") && !objectPushNews.isNull("pushTime")) {
                        pushMessageEntity.push_time = objectPushNews.getString("pushTime");
                    }
                    if (objectPushNews.has("typeCode") && !objectPushNews.isNull("typeCode")) {
                        pushMessageEntity.type_code = objectPushNews.getString("typeCode");
                    }
                    if (objectPushNews.has("pushParam") && !objectPushNews.isNull("pushParam")) {
                        JSONObject objectPushParam = objectPushNews.getJSONObject("pushParam");
                        if (objectPushParam.has("orderId") && !objectPushParam.isNull("orderId")) {
                            pushMessageEntity.orderId = objectPushParam.getInt("orderId");
                        }
                        if (objectPushParam.has("backup") && !objectPushParam.isNull("backup")) {
                            pushMessageEntity.backup = objectPushParam.getString("backup");
                        }
                        if (objectPushParam.has("ad_url") && !objectPushParam.isNull("ad_url")) {
                            pushMessageEntity.ad_url = objectPushParam.getString("ad_url");
                        }
                        if (objectPushParam.has("workerId") && !objectPushParam.isNull("workerId")) {
                            pushMessageEntity.workerId = objectPushParam.getString("workerId");
                        }
                        if (objectPushParam.has("workerLevel") && !objectPushParam.isNull("workerLevel")) {
                            pushMessageEntity.workerLevel = objectPushParam.getString("workerLevel");
                        }
                        if (objectPushParam.has("url") && !objectPushParam.isNull("url")) {
                            pushMessageEntity.url = objectPushParam.getString("url");
                        }
                        if (objectPushParam.has("name") && !objectPushParam.isNull("name")) {
                            pushMessageEntity.name = objectPushParam.getString("name");
                        }
                        if (objectPushParam.has("tag") && !objectPushParam.isNull("tag")) {
                            pushMessageEntity.tag = objectPushParam.getString("tag");
                        }
                        if (objectPushParam.has("postId") && !objectPushParam.isNull("postId")) {
                            pushMessageEntity.postId = objectPushParam.getInt("postId");
                        }
                        if (objectPushParam.has("userId_circle") && !objectPushParam.isNull("userId_circle")) {
                            pushMessageEntity.userId_circle = objectPushParam.getInt("userId_circle");
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pushMessageEntity;
    }
}
