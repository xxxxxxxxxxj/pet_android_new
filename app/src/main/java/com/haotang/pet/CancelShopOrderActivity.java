package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.RefundResonAdapter;
import com.haotang.pet.adapter.ShopCancelTipAdapter;
import com.haotang.pet.entity.CancelReasonBean;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CancelShopOrderActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_reson_list)
    RecyclerView rvResonList;
    @BindView(R.id.tv_cancel_commit)
    TextView tvCancelCommit;
    @BindView(R.id.rv_shopcancel_tip)
    RecyclerView rvShopcancelTip;
    private List<CancelReasonBean> reasonList = new ArrayList<>();
    private List<String> tipList = new ArrayList<>();
    private int orderId;
    private RefundResonAdapter adapter;
    private ShopCancelTipAdapter tipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initData();
        setView();
        setListener();
    }

    private void setListener() {
        adapter.setOnChcekClickListener(new RefundResonAdapter.onChcekClickListener() {
            @Override
            public void onCheckClick(int position) {
                for (int i = 0; i < reasonList.size(); i++) {
                    if (i == position) {
                        reasonList.get(i).setChoose(true);
                    } else {
                        reasonList.get(i).setChoose(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setView() {
        tvTitlebarTitle.setText("取消订单");
        rvResonList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RefundResonAdapter(reasonList, mContext);
        rvResonList.setAdapter(adapter);
        rvShopcancelTip.setLayoutManager(new LinearLayoutManager(this));
        tipAdapter = new ShopCancelTipAdapter(mContext,tipList);
        rvShopcancelTip.setAdapter(tipAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        reasonList = (List<CancelReasonBean>) intent.getSerializableExtra("reasonList");
        tipList = intent.getStringArrayListExtra("tipList");
        orderId = intent.getIntExtra("orderId", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_cancel_shop_order);
        ButterKnife.bind(this);
    }

    private AsyncHttpResponseHandler shopCancleHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    ToastUtil.showToastShortBottom(mContext, "操作成功");
                    EventBus.getDefault().post(new RefreshOrderEvent(true));
                    finish();
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.tv_cancel_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_cancel_commit:
                int reason = 0;
                for (int i = 0; i < reasonList.size(); i++) {
                    if (reasonList.get(i).isChoose()) {
                        reason = reasonList.get(i).getId();
                    }
                }
                if (reason==0){
                    ToastUtil.showToastShortBottom(mContext,"请选择取消原因");
                }else {
                    CommUtil.shopOrderCancel(mContext, orderId, String.valueOf(reason), shopCancleHandler);
                }
                break;
        }
    }
}
