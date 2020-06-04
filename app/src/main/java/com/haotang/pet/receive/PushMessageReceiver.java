package com.haotang.pet.receive;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import com.haotang.pet.ADActivity;
import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.CashbackAmountActivity;
import com.haotang.pet.CharacteristicServiceActivity;
import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.ComplaintOrderHistoryActivity;
import com.haotang.pet.CustomerComplaintsHistoryActivity;
import com.haotang.pet.EvaluateNewActivity;
import com.haotang.pet.FosterEvaluteActivity;
import com.haotang.pet.FosterHomeActivity;
import com.haotang.pet.FosterLiveListActivity;
import com.haotang.pet.FosterOrderDetailNewActivity;
import com.haotang.pet.GiftCardDetailActivity;
import com.haotang.pet.GiftCardListActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.MainToBeauList;
import com.haotang.pet.MyCardActivity;
import com.haotang.pet.MyCouponNewActivity;
import com.haotang.pet.PetCircleInsideDetailActivity;
import com.haotang.pet.PostSelectionDetailActivity;
import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.ServiceNewActivity;
import com.haotang.pet.ShopMallOrderActivity;
import com.haotang.pet.UserListActivity;
import com.haotang.pet.WashOrderDetailActivity;
import com.haotang.pet.encyclopedias.activity.EncyclopediasActivity;
import com.haotang.pet.encyclopedias.activity.EncyclopediasDetail;
import com.haotang.pet.mall.MallToListActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";
    private SharedPreferenceUtil spUtil;
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    private String imgUrl = "";
    protected Bitmap bmp;
    private Context context;
    private String title = "";
    private String content = "";
    private String url = "";
    private int workerId;
    private String name = "";
    private int postId;
    private int userId;
    private int orderId;
    private String backup;
    private int type;
    private String ad_url;

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] 接收到推送下来的自定义消息:" + customMessage);
        if (spUtil == null) {
            spUtil = SharedPreferenceUtil.getInstance(context
                    .getApplicationContext());
        }
        this.context = context;
        title = "";
        content = "";
        imgUrl = "";
        backup = "";
        type = 0;
        ad_url = "";
        workerId = 0;
        url = "";
        name = "";
        postId = 0;
        orderId = 0;
        userId = 0;
        String extras = customMessage.extra;
        Log.e(TAG, "extras = " + extras);
        if (Utils.isStrNull(extras)) {
            try {
                JSONObject jobj = new JSONObject(extras);
                if (jobj.has("userNewsId") && !jobj.isNull("userNewsId")) {
                    updateMsg(jobj.getString("userNewsId"));
                }
                if (jobj.has("title") && !jobj.isNull("title")) {
                    title = jobj.getString("title");
                }
                if (jobj.has("alert")
                        && !jobj.isNull("alert")) {
                    content = jobj.getString("alert");
                }
                if (jobj.has("mediaUrl") && !jobj.isNull("mediaUrl")) {
                    imgUrl = jobj.getString("mediaUrl");
                }
                if (jobj.has("type") && !jobj.isNull("type")) {
                    type = jobj.getInt("type");
                }
                if (jobj.has("backup") && !jobj.isNull("backup")) {
                    backup = jobj.getString("backup");
                }
                if (jobj.has("ad_url") && !jobj.isNull("ad_url")) {
                    ad_url = jobj.getString("ad_url");
                }
                if (jobj.has("workerId") && !jobj.isNull("workerId")) {
                    workerId = jobj.getInt("workerId");
                }
                if (jobj.has("url") && !jobj.isNull("url")) {
                    url = jobj.getString("url");
                }
                if (jobj.has("name") && !jobj.isNull("name")) {
                    name = jobj.getString("name");
                }
                if (jobj.has("tag") && !jobj.isNull("tag")) {
                    spUtil.saveBoolean("isExit", true);
                } else {
                    spUtil.saveBoolean("isExit", false);
                }
                if (jobj.has("postId") && !jobj.isNull("postId")) {
                    postId = jobj.getInt("postId");
                }
                if (jobj.has("orderId") && !jobj.isNull("orderId")) {
                    orderId = jobj.getInt("orderId");
                }
                if (jobj.has("userId") && !jobj.isNull("userId")) {
                    userId = jobj.getInt("userId");
                }
                if (Utils.isStrNull(imgUrl)) {
                    // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                    //new Thread(networkTask).start();
                }
            } catch (Exception e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        if (spUtil == null) {
            spUtil = SharedPreferenceUtil.getInstance(context
                    .getApplicationContext());
        }
        this.context = context;
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        backup = "";
        type = 0;
        ad_url = "";
        workerId = 0;
        url = "";
        name = "";
        postId = 0;
        orderId = 0;
        userId = 0;
        try {
            String extras = message.notificationExtras;
            if (Utils.isStrNull(extras)) {
                JSONObject jobj = new JSONObject(extras);
                if (jobj.has("userNewsId") && !jobj.isNull("userNewsId")) {
                    updateMsg(jobj.getString("userNewsId"));
                }
                if (jobj.has("type") && !jobj.isNull("type")) {
                    type = jobj.getInt("type");
                }
                if (jobj.has("backup") && !jobj.isNull("backup")) {
                    backup = jobj.getString("backup");
                }
                if (jobj.has("ad_url") && !jobj.isNull("ad_url")) {
                    ad_url = jobj.getString("ad_url");
                }
                if (jobj.has("workerId") && !jobj.isNull("workerId")) {
                    workerId = jobj.getInt("workerId");
                }
                if (jobj.has("url") && !jobj.isNull("url")) {
                    url = jobj.getString("url");
                }
                if (jobj.has("name") && !jobj.isNull("name")) {
                    name = jobj.getString("name");
                }
                if (jobj.has("tag") && !jobj.isNull("tag")) {
                    spUtil.saveBoolean("isExit", true);
                } else {
                    spUtil.saveBoolean("isExit", false);
                }
                if (jobj.has("postId") && !jobj.isNull("postId")) {
                    postId = jobj.getInt("postId");
                }
                if (jobj.has("orderId") && !jobj.isNull("orderId")) {
                    orderId = jobj.getInt("orderId");
                }
                if (jobj.has("userId") && !jobj.isNull("userId")) {
                    userId = jobj.getInt("userId");
                }
                goActivity();
            }
        } catch (Exception e) {
            Log.e("TAG", "e = " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        spUtil = SharedPreferenceUtil.getInstance(context
                .getApplicationContext());
        this.context = context;
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
        if (!registrationId.isEmpty()) {
            Global.savePushID(context, registrationId);
        }
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    private boolean isAppOpen(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals("com.haotang.pet")
                    || info.baseActivity.getPackageName().equals(
                    "com.haotang.pet")) {
                return true;
            }
        }
        return false;
    }

    private void goActivity() {
        if (!isAppOpen(context)) {
            context.startActivity(new Intent(context, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        context.startActivity(getDefalutIntent());
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
     * Notification.FLAG_AUTO_CANCEL
     */
    private PendingIntent getDefalutPendingIntent() {
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode, getDefalutIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private Intent getDefalutIntent() {
        Intent intent = new Intent();
        if (type == 2030) {//商城首页
            intent.putExtra("point", 21);
            intent.setClass(context, MainActivity.class);
        } else if (type == 2031) {//商城一级分类落地页
            intent.putExtra("classificationId", Integer.parseInt(backup));
            intent.setClass(context, MallToListActivity.class);
        } else if (type == 2032) {//商品详情页
            intent.putExtra("commodityId", Integer.parseInt(backup));
            intent.setClass(context, CommodityDetailActivity.class);
        } else if (type == 100) {
            intent.putExtra("url", ad_url);
            intent.setClass(context, ADActivity.class);
        } else if (type == 2001) {// 进入指定美容师界面
            intent.putExtra("id", workerId);
            intent.setClass(context, BeauticianDetailActivity.class);
        } else if (type == 2002) {// 进入指定等级美容师界面
            intent.setClass(context, MainToBeauList.class);
        } else if (type == 2004) {// 进入寄养预约界面
            intent.setClass(context, FosterHomeActivity.class);
        } else if (type == 2005) {// 进入洗澡预约界面
            intent.putExtra("serviceType", 1);
            intent.setClass(context, ServiceNewActivity.class);
        } else if (type == 2006) {// 进入美容预约界面
            intent.putExtra("serviceType", 2);
            intent.setClass(context, ServiceNewActivity.class);
        } else if (type == 2007) {// 进入办证界面
            intent.putExtra("url", url);
            intent.setClass(context, ADActivity.class);
        } else if (type == 2008 || type == 2009
                || type == 2010 || type == 2011
                || type == 2016 || type == 2017
                || type == 2018 || type == 2019
                || type == 2020 || type == 2021) {// 进入特色服务界面
            intent.putExtra("typeId", Integer.parseInt(name));
            intent.setClass(context, CharacteristicServiceActivity.class);
        } else if (type == 2012) {// 进入寄养直播列表页,运营发送
            if (Utils.checkLogin(context)) {
                intent.setClass(context, FosterLiveListActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2013) {// 进入寄养直播列表页
            if (Utils.checkLogin(context)) {
                intent.setClass(context, FosterLiveListActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2014) {// 进入帖子详情页
            intent.putExtra("postId", postId);
            intent.setClass(context, PetCircleInsideDetailActivity.class);
        } else if (type == 2015) {// 进入圈子个人主页
            intent.putExtra("userId", userId);
            intent.setClass(context, PostUserInfoActivity.class);
        } else if (type == 2022) {// 进入精选帖子详情界面
            intent.putExtra("postId", postId);
            intent.setClass(context, PostSelectionDetailActivity.class);
        } else if (type == 2023) {// 进入粉丝列表界面
            if (Utils.checkLogin(context)) {
                intent.putExtra("flag", "fans");
                intent.putExtra("userId", spUtil.getInt("userid", -1));
                intent.setClass(context, UserListActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2025) {// 进入投诉订单详情界面
            if (Utils.checkLogin(context)) {
                intent.putExtra("orderid", orderId);
                intent.setClass(context, ComplaintOrderHistoryActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2026) {// 进入投诉客服列表界面
            if (Utils.checkLogin(context)) {
                intent.setClass(context, CustomerComplaintsHistoryActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2036) {// 百科列表页
            intent.putExtra("id", Integer.parseInt(backup));
            intent.setClass(context, EncyclopediasActivity.class);
        } else if (type == 2037) {// 百科详情页
            intent.putExtra("infoId", Integer.parseInt(backup));
            intent.setClass(context, EncyclopediasDetail.class);
        } else if (type == 2038) {// 服务卡列表
            intent.setClass(context, GiftCardListActivity.class);
        } else if (type == 2039) {// 服务卡详情页
            intent.putExtra("cardTemplateId", Integer.parseInt(backup));
            intent.setClass(context, GiftCardDetailActivity.class);
        } else if (type == 2040) {// 返现主页
            if (Utils.checkLogin(context)) {
                intent.setClass(context, CashbackAmountActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2041) {// 我的E卡列表页
            if (Utils.checkLogin(context)) {
                intent.setClass(context, MyCardActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2042) {// 商城订单列表页
            if (Utils.checkLogin(context)) {
                intent.setClass(context, ShopMallOrderActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 2) {// 我的优惠券列表页
            if (Utils.checkLogin(context)) {
                intent.setClass(context, MyCouponNewActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 1) {//洗美待付款
            if (Utils.checkLogin(context)) {
                intent.putExtra("orderid", orderId);
                intent.putExtra("previous", Global.PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN);
                intent.setClass(context, WashOrderDetailActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 1705) {//寄养待付款
            if (Utils.checkLogin(context)) {
                intent.putExtra("type", 2);
                intent.putExtra("orderid", orderId);
                intent.putExtra("previous", Global.PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN);
                intent.setClass(context, FosterOrderDetailNewActivity.class);
            } else {
                intent.putExtra("previous", Global.PRE_PUSH_TO_LOGIN);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 600) {//洗美评价
            if (Utils.checkLogin(context)) {
                intent.putExtra("type", 1);
                intent.putExtra("orderid", orderId);
                intent.putExtra("previous", Global.PRE_PUSH_TO_EVALUATE_XIMEI);
                intent.setClass(context, EvaluateNewActivity.class);
            } else {
                intent.putExtra("type", 1);
                intent.putExtra("orderid", orderId);
                intent.putExtra("previous", Global.PRE_PUSH_TO_EVALUATE_XIMEI);
                intent.setClass(context, LoginNewActivity.class);
            }
        } else if (type == 65) {//寄养评价
            if (Utils.checkLogin(context)) {
                intent.putExtra("orderId", orderId);
                intent.setClass(context, FosterEvaluteActivity.class);
            } else {
                intent.setClass(context, LoginNewActivity.class);
            }
        } else {
            intent.setClass(context, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    /*Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bmp = (Bitmap) msg.obj;
            Log.e("TAG", "bmp = " + bmp);
            if (bmp != null) {
                Log.e("TAG", "bmp = " + bmp);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_receiver);
                remoteViews.setTextViewText(R.id.tv_remoteview_receiver_title, title);
                remoteViews.setTextViewText(R.id.tv_remoteview_receiver_desc, content);
                remoteViews.setImageViewBitmap(R.id.iv_remoteview_receiver_img, bmp);
                Notification notification = new Notification();//注意Notification创建得用new的方式了创建，因为
                //如果用Builder的方式创建的话，在设置remoteview的时候会用到setCustomContentView方法，
                //该方法需要api24才能使用，api版本太高了，但是使用new的方式就可以不用考虑低api的问题。
                notification.icon = R.drawable.logo;
                notification.tickerText = "有新消息";
                notification.when = System.currentTimeMillis();
                notification.contentView = remoteViews;
                // 采用默认声音
                notification.defaults |= Notification.DEFAULT_SOUND;
                // 使用默认的灯光
                notification.defaults |= Notification.DEFAULT_LIGHTS;
                // 通知被点击后，自动消失
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.contentIntent = getDefalutPendingIntent();
                mNotificationManager = (NotificationManager) context
                        .getSystemService(context.NOTIFICATION_SERVICE);
                mNotificationManager.notify((int) System.currentTimeMillis(), notification);
            }
        }
    };

    */

    /**
     * 网络操作相关的子线程
     *//*
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(imgUrl);
            Message msg = new Message();
            msg.obj = returnBitmap;
            handler.sendMessage(msg);
        }
    };*/
    private void updateMsg(String newsId) {
        CommUtil.updatePushMessageReadState(context, newsId, newHandler);
    }

    private AsyncHttpResponseHandler newHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
}
