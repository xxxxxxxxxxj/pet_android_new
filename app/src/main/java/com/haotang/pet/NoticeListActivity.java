package com.haotang.pet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.NoticeAdapter;
import com.haotang.pet.encyclopedias.activity.EncyclopediasActivity;
import com.haotang.pet.encyclopedias.activity.EncyclopediasDetail;
import com.haotang.pet.entity.PushMessageEntity;
import com.haotang.pet.mall.MallToListActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class NoticeListActivity extends SuperActivity implements View.OnClickListener {
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    private ListView listview_notice_list;
    private List<PushMessageEntity> allNotices = new ArrayList<>();
    private NoticeAdapter noticeAdapter;
    private Context context;
    private int wayType;
    private String url = "";
    private String flag = "";
    private int typeId;
    private int petid;
    private String type;
    private String backup;
    private String ad_url;
    private String workerId;
    private String workerLevel;
    private String name;
    private String tag;
    private int postId;
    private int userId;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticelist);
        context = this;
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        tv_titlebar_title.setText("消息中心");
        listview_notice_list = (ListView) findViewById(R.id.listview_notice_list);

        noticeAdapter = new NoticeAdapter(mContext, allNotices);
        listview_notice_list.setAdapter(noticeAdapter);


        ib_titlebar_back.setOnClickListener(this);

        listview_notice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushMessageEntity pushMessageEntity = (PushMessageEntity) parent.getItemAtPosition(position);
                type = pushMessageEntity.type_code;
                backup = pushMessageEntity.backup;
                ad_url = pushMessageEntity.ad_url;
                url = pushMessageEntity.url;
                workerId = pushMessageEntity.workerId;
                workerLevel = pushMessageEntity.workerLevel;
                name = pushMessageEntity.name;
                tag = pushMessageEntity.tag;
                postId = pushMessageEntity.postId;
                userId = pushMessageEntity.userId_circle;
                orderId = pushMessageEntity.orderId;
                if ("2030".equals(type)) {//商城首页
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("point", 21);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } else if ("2031".equals(type)) {//商城一级分类落地页
                    Intent intent = new Intent(context, MallToListActivity.class);
                    intent.putExtra("classificationId", Integer.parseInt(backup));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } else if ("2032".equals(type)) {//商品详情页
                    Intent intent = new Intent(context, CommodityDetailActivity.class);
                    intent.putExtra("commodityId", Integer.parseInt(backup));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } else if ("100".equals(type)) {
                    if (!TextUtils.isEmpty(ad_url)) {
                        startAct(context, 0, 0, ad_url, 0, 0, 0, 0, "",
                                ADActivity.class);
                    } else {
                        startAct(context, 100, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else if ("2001".equals(type)) {// 进入指定美容师界面
                    if (!TextUtils.isEmpty(workerId)) {
                        startAct(context, Global.MAIN_TO_BEAUTICIANLIST, 0,
                                url, Integer.parseInt(workerId), 0, 0, 0, "",
                                BeauticianDetailActivity.class);
                    } else {
                        startAct(context, 100, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else if ("2002".equals(type)) {// 进入指定等级美容师界面
                    if (!TextUtils.isEmpty(workerLevel)) {
                        startAct(context, 0, 0, url, 0, Integer.parseInt(workerLevel), 0, 0, "",
                                MainToBeauList.class);
                    } else {
                        startAct(context, 100, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else if ("2004".equals(type)) {// 进入寄养预约界面
                    // 寄养
                    startAct(context,
                            Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT,
                            0, url, 0, 0, 100, 100, "",
                            FosterHomeActivity.class);
                } else if ("2005".equals(type)) {// 进入洗澡预约界面
                    context.startActivity(new Intent(context,
                            ServiceNewActivity.class)
                            .putExtra("serviceType", 1).setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if ("2006".equals(type)) {// 进入美容预约界面
                    context.startActivity(new Intent(context,
                            ServiceNewActivity.class)
                            .putExtra("serviceType", 2).setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if ("2007".equals(type)) {// 进入办证界面
                    if (!TextUtils.isEmpty(url)) {
                        startAct(context, 0, 0, url, 0, 0, 0, 0, "",
                                ADActivity.class);
                    } else {
                        startAct(context, 100, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else if ("2008".equals(type) || "2009".equals(type)
                        || "2010".equals(type) || "2011".equals(type)
                        || "2016".equals(type) || "2017".equals(type)
                        || "2018".equals(type) || "2019".equals(type)
                        || "2020".equals(type) || "2021".equals(type)) {// 进入特色服务界面
                    if (Utils.isStrNull(name)) {
                        Intent intent = new Intent(context, CharacteristicServiceActivity.class);
                        intent.putExtra("serviceType", 3);
                        intent.putExtra("typeId", Integer.parseInt(name));// 从特色服务跳转到门店列表专用，为了计算价格
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                } else if ("2012".equals(type)) {// 进入寄养直播列表页,运营发送
                        startAct(context, 0, 0, url, 0, 0, 0, 0, "",
                                FosterLiveListActivity.class);
                } else if ("2013".equals(type)) {// 进入寄养直播列表页
                        startAct(context, 0, 0, url, 0, 0, 0, 0, "",
                                FosterLiveListActivity.class);
                } else if ("2014".equals(type)) {// 进入帖子详情页
                    if (postId > 0) {
                        startAct(context, 0, 0, null, 0, 0, 0, 0, "",
                                PetCircleInsideDetailActivity.class);
                    } else {
                        startAct(context, 0, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else if ("2015".equals(type)) {// 进入圈子个人主页
                    if (userId > 0) {
                        startAct(context, 0, 0, null, 0, 0, 0, 0, "",
                                PostUserInfoActivity.class);
                    } else {
                        startAct(context, 0, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else if ("2022".equals(type)) {// 进入精选帖子详情界面
                    if (postId > 0) {
                        startAct(context, 0, 0, null, 0, 0, 0, 0, "",
                                PostSelectionDetailActivity.class);
                    } else {
                        startAct(context, 0, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else if ("2023".equals(type)) {// 进入粉丝列表界面
                    if (spUtil.getString("cellphone", "") != null
                            && !TextUtils.isEmpty(spUtil.getString("cellphone",
                            "")) && spUtil.getInt("userid", -1) > 0) {
                        userId = spUtil.getInt("userid", -1);
                        flag = "fans";
                        startAct(context, 0, 0, null, 0, 0, 0, 0, "",
                                UserListActivity.class);
                    } else {
                        startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
                                0, 0, 0, "", LoginNewActivity.class);
                    }
                } else if ("2025".equals(type)) {// 进入投诉订单详情界面
                    if (Utils.checkLogin(context)) {
                        startAct(context, Global.PRE_PUSH_TO_LOGIN, orderId,
                                null, 0, 0, 0, 0, "",
                                ComplaintOrderHistoryActivity.class);
                    } else {
                        startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
                                0, 0, 0, "", LoginNewActivity.class);
                    }
                } else if ("2026".equals(type)) {// 进入投诉客服列表界面
                    if (Utils.checkLogin(context)) {
                        startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
                                0, 0, 0, "",
                                CustomerComplaintsHistoryActivity.class);
                    } else {
                        startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
                                0, 0, 0, "", LoginNewActivity.class);
                    }
                } else if ("2036".equals(type)) {// 百科列表页
                    Intent intent = new Intent(NoticeListActivity.this, EncyclopediasActivity.class);
                    intent.putExtra("id", Integer.parseInt(backup));//
                    startActivity(intent);
                } else if ("2037".equals(type)) {// 百科详情页
                    Intent intent = new Intent(NoticeListActivity.this, EncyclopediasDetail.class);
                    intent.putExtra("infoId", Integer.parseInt(backup));//
                    startActivity(intent);
                } else if ("2038".equals(type)) {// 服务卡列表
                    startActivity(new Intent(NoticeListActivity.this, GiftCardListActivity.class));
                } else if ("2039".equals(type)) {// 服务卡详情页
                    startActivity(new Intent(NoticeListActivity.this, GiftCardDetailActivity.class).putExtra("cardTemplateId", Integer.parseInt(backup)));
                } else if ("2".equals(type)) {
                    if (Utils.checkLogin(context)) {
                        startAct(context, Global.PRE_PUSH_TO_ORDER, 0, null, 0,
                                0, 0, 0, "", MyCouponNewActivity.class);
                    } else {
                        startAct(context, Global.PRE_PUSH_TO_LOGIN, 0, null, 0,
                                0, 0, 0, "", LoginNewActivity.class);
                    }
                } else if (orderId > 0) {
                    if ("1".equals(type)) {//洗美待付款
                        startAct(context,
                                Global.PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN,
                                orderId, null, 0, 0, 0, 0, "",
                                WashOrderDetailActivity.class);
                    } else if ("1705".equals(type)) {//寄养待付款
                        wayType = 2;
                        startAct(context,
                                Global.PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN,
                                orderId, null, 0, 0, 0, 0, "",
                                FosterOrderDetailNewActivity.class);
                    } else if ("600".equals(type)) {//洗美评价
                        if (Utils.checkLogin(context)) {
                            wayType = 1;
                            startAct(context, Global.PRE_PUSH_TO_EVALUATE_XIMEI,
                                    orderId, null, 0, 0, 0, 0, "",
                                    EvaluateNewActivity.class);
                        } else {
                            startAct(context, Global.PRE_PUSH_TO_EVALUATE_XIMEI,
                                    orderId, null, 0, 0, 0, 0, "",
                                    LoginNewActivity.class);
                        }
                    } else if ("65".equals(type)) {//寄养评价
                        if (Utils.checkLogin(context)) {
                            wayType = 2;
                            startAct(context, 0,
                                    orderId, null, 0, 0, 0, 0, "",
                                    FosterEvaluteActivity.class);
                        } else {
                            startAct(context, Global.PRE_PUSH_TO_EVALUATE,
                                    orderId, null, 0, 0, 0, 0, "",
                                    LoginNewActivity.class);
                        }
                    } else {
                        startAct(context, 100, 0, null, 0, 0, 0, 0, "",
                                MainActivity.class);
                    }
                } else {
                    startAct(context, 100, 0, null, 0, 0, 0, 0, "",
                            MainActivity.class);
                }
                if (pushMessageEntity.isRead == 0) {
                    updateMsg(pushMessageEntity.id);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        mPDialog.showDialog();
        allNotices.clear();
        if (noticeAdapter != null) {
            noticeAdapter.notifyDataSetChanged();
        }
        CommUtil.pushMessageList(mContext, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("pushMessageList") && !objectData.isNull("pushMessageList")) {
                            JSONArray array = objectData.getJSONArray("pushMessageList");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    allNotices.add(PushMessageEntity.j2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                    }
                }
                if (allNotices.size() > 0) {
                    noticeAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void startAct(Context context, int previous, int orderid,
                          String url, int workerId, int workerLevel, int serviceid,
                          int servicetype, String servicename, Class clazz) {

        Intent intent = new Intent(context, clazz);
        intent.putExtra("typeId", typeId);// 从特色服务跳转到门店列表专用，为了计算价格
        intent.putExtra("petid", petid);
        intent.putExtra("orderid", orderid);
        intent.putExtra("orderId", orderid);
        intent.putExtra("userId", userId);
        intent.putExtra("id", workerId);
        intent.putExtra("workerLevel", workerLevel);
        if (url != null && !TextUtils.isEmpty(url))
            intent.putExtra("url", url);
        intent.putExtra("flag", flag);
        intent.putExtra("serviceid", serviceid);
        intent.putExtra("postId", postId);
        intent.putExtra("type", wayType);
        intent.putExtra("servicetype", servicetype);
        intent.putExtra("areaid", spUtil.getInt("tareaid", 0));
        if (previous == Global.PUSH_TO_ORDER_ORDER_BEAU_ACCEPT) {
            intent.putExtra("jpushBeauAccpetCode", 1);
        }
        if (previous == Global.PUSH_TO_ORDER_ORDER_NO_BEAU_ACCEPT) {
            intent.putExtra("jpushWaitCode", 1);
        }
        intent.putExtra("previous", previous);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void updateMsg(String newsId) {
        CommUtil.updatePushMessageReadState(mContext, newsId, newHandler);
    }

    private AsyncHttpResponseHandler newHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//            try {
//                JSONObject object = new JSONObject(new String(responseBody));
//                int code = object.getInt("code");
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Utils.mLogError("==-->123456789 e "+e.getMessage()+"  "+e.getCause());
//            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
        }
    }


}
