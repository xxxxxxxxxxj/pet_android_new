package com.haotang.pet;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.RefundScheduleAdapter;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.entity.RefundSchedule;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退卡进度界面
 */
public class RefundScheduleActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_refund_schedule)
    RecyclerView rvRefundSchedule;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    private List<RefundSchedule> list = new ArrayList<RefundSchedule>();
    private int id;
    private RefundScheduleAdapter refundScheduleAdapter;
    private String salePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        id = getIntent().getIntExtra("id", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_refund_schedule);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("退款详情");
        rvRefundSchedule.setHasFixedSize(true);
        rvRefundSchedule.setLayoutManager(new LinearLayoutManager(this));
        refundScheduleAdapter = new RefundScheduleAdapter(R.layout.item_refundschedule, list);
        rvRefundSchedule.setAdapter(refundScheduleAdapter);
    }

    private void setLinster() {

    }

    private void getData() {
        mPDialog.showDialog();
        list.clear();
        CommUtil.refundDetail(RefundScheduleActivity.this, id, refundDetailHanler);
    }

    private AsyncHttpResponseHandler refundDetailHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONArray jarrdata = jsonObject.getJSONArray("data");
                        if (jarrdata.length() > 0) {
                            for (int i = 0; i < jarrdata.length(); i++) {
                                list.add(RefundSchedule.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                            salePhone = list.get(0).getSalePhone();
                            boolean isRefundCancel = true;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getRed() == 1 && list.get(i).getStatus().equals("4")) {
                                    isRefundCancel = false;
                                    break;
                                }
                            }
                            if (isRefundCancel) {
                                tvTitlebarOther.setVisibility(View.VISIBLE);
                                tvTitlebarOther.setText("取消申请");
                                tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                tvTitlebarOther.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShort(RefundScheduleActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShort(RefundScheduleActivity.this, "数据异常");
                e.printStackTrace();
            }
            refundScheduleAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(RefundScheduleActivity.this, "请求失败");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_refund_schedule_lxkf, R.id.tv_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_refund_schedule_lxkf:
                new AlertDialogNavAndPost(RefundScheduleActivity.this).builder()
                        .setTitle("提示")
                        .setMsg("是否拨打电话?")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 确认拨打电话
                        Utils.telePhoneBroadcast(mContext,
                                salePhone);
                    }
                }).show();
                break;
            case R.id.tv_titlebar_other:
                new AlertDialogNavAndPost(RefundScheduleActivity.this).builder()
                        .setTitle("")
                        .setMsg("是否取消申请退款")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPDialog.showDialog();
                        CommUtil.refundCancel(RefundScheduleActivity.this, id, refundCancelHanler);
                    }
                }).show();
                break;
        }
    }

    private AsyncHttpResponseHandler refundCancelHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    ToastUtil.showToastShortBottom(RefundScheduleActivity.this, "成功取消申请退款");
                    EventBus.getDefault().post(new RefundCardEvent(true));
                    finish();
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(RefundScheduleActivity.this,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
                ToastUtil.showToastShort(RefundScheduleActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(RefundScheduleActivity.this, "请求失败");
        }
    };
}
