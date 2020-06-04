package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CancleNewAdapter;
import com.haotang.pet.entity.CancleReson;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/25 0025.
 */

public class OrderCancleReasonNewActivity extends SuperActivity {
    public static OrderCancleReasonNewActivity orderCancleReasonNewActivity;
    @BindView(R.id.show_top)
    LinearLayout showTop;
    @BindView(R.id.listView_show_reason)
    ListView listViewShowReason;
    @BindView(R.id.button_push)
    Button buttonPush;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    private int type = -1;
    private int orderid = -1;
    private String choosePos;
    private CancleNewAdapter<CancleReson> mCAdapter;
    private ArrayList<CancleReson> showReason = new ArrayList<>();
    private String cancleResonChoose = "";
    private SharedPreferenceUtil spUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_reason_new);
        MApplication.listAppoint.add(this);
        orderCancleReasonNewActivity = this;
        spUtil = SharedPreferenceUtil.getInstance(orderCancleReasonNewActivity);
        ButterKnife.bind(this);
        getIntentData();
        setView();
        initListener();
        cancelRemind();

    }

    private void cancelRemind() {
        CommUtil.orderCancelRemind(this, orderid, getInsertReminderlog);
    }

    private String linkman;
    private String telephone;
    private AsyncHttpResponseHandler getInsertReminderlog = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            // TODO Auto-generated method stub
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("reasons") && !objectData.isNull("reasons")) {
                            JSONArray array = objectData.getJSONArray("reasons");
                            for (int i = 0; i < array.length(); i++) {
                                CancleReson cancleReson = new CancleReson();
                                cancleReson.txt = array.getString(i);
                                cancleReson.isChoose = false;
                                showReason.add(cancleReson);
                            }
                        }
                        if (showReason.size() > 0) {
                            mCAdapter.notifyDataSetChanged();
                        }
                        if (objectData.has("linkman") && !objectData.isNull("linkman")) {
                            linkman = objectData.getString("linkman");
                        }else {
                            linkman = spUtil.getString("userName","");
                        }
                        if (objectData.has("telephone") && !objectData.isNull("telephone")) {
                            telephone = objectData.getString("telephone");
                        }else {
                            telephone = spUtil.getString("cellphone","");
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub

        }

    };

    private void setView() {
        mCAdapter = new CancleNewAdapter<CancleReson>(this, showReason);
        listViewShowReason.setAdapter(mCAdapter);
    }

    private void getIntentData() {
        type = getIntent().getIntExtra("type", -1);
        orderid = getIntent().getIntExtra("orderid", -1);
    }

    private void initListener() {
        listViewShowReason.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CancleReson cancleReson = (CancleReson) parent.getItemAtPosition(position);
                choosePos = (position + 1) + "";
                cancleResonChoose = cancleReson.txt;
                mCAdapter.setPostition(position);
                mCAdapter.notifyDataSetChanged();
            }
        });
    }

    //直接取消订单 洗美特色服务
    private void cancleOrder() {
        CommUtil.cancelOrder(mContext, orderid, choosePos, linkman, telephone, cancelOrder);
    }

    //寄养取消
    private void hotelCancel() {
        CommUtil.hotelCancel(mContext, orderid, choosePos, linkman, telephone, cancelOrder);
    }

    // 取消订单
    private AsyncHttpResponseHandler cancelOrder = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            String msg = "";
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                        msg = jsonObject.getString("msg");
                        ToastUtil.showToastShortBottom(mContext, msg);
                        EventBus.getDefault().post(new RefreshOrderEvent(true));
                        if (type==1){
                            //goNext(WashOrderDetailActivity.class);
                        }else if(type==2) {
                            //goNext(FosterOrderDetailActivity.class);
                        }else {
                            goNext(OrderDetailFromOrderToConfirmActivity.class);
                        }
                        finishWithAnimation();
                    }
                } else {
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                        msg = jsonObject.getString("msg");
                        ToastUtil.showToastShortBottom(mContext, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            Toast.makeText(mContext, "连接服务器失败,请检查网络", Toast.LENGTH_SHORT).show();
        }

    };

    private void goNext(Class cls) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtra("orderid", orderid);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @OnClick({R.id.show_top, R.id.button_push, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
            case R.id.show_top:
                finishWithAnimation();
                break;
            case R.id.button_push:
                if (TextUtils.isEmpty(choosePos)) {
                    ToastUtil.showToastShortCenter(mContext, "请选择取消原因");
                    return;
                }
                if (type == 1) {
                    cancleOrder();
                } else if (type == 2) {
                    hotelCancel();
                }
                finishWithAnimation();
                break;
        }
    }
}
