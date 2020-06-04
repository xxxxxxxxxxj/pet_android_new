package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ShopRefundIconAdapter;
import com.haotang.pet.adapter.ShopRefundSchduleAdapter;
import com.haotang.pet.entity.MallOrderDetailGoodItems;
import com.haotang.pet.entity.ShopRefundSchedule;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopRefundScheduleActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.tv_refundschdule_num)
    TextView tvRefundschduleNum;
    @BindView(R.id.rv_refundschdule_icon)
    RecyclerView rvRefundschduleIcon;
    @BindView(R.id.rv_refundschedule_list)
    RecyclerView rvRefundscheduleList;
    private List<MallOrderDetailGoodItems> refundList = new ArrayList<>();
    private List<String> iconList = new ArrayList<>();
    private int orderId;
    private String mallRuleUrl;
    private ShopRefundIconAdapter iconAdapter;
    private ShopRefundSchduleAdapter schduleAdapter;
    private List<ShopRefundSchedule.DataBean.ExamineSpeedBean> examineSpeed;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initData();
        setView();
        getData();
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.getShopRefundShdule(mContext,orderId,shopRefundShduleHandler);
    }


    private AsyncHttpResponseHandler shopRefundShduleHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.dimissDialog();
            Gson gson = new Gson();
            ShopRefundSchedule shopRefundSchedule = gson.fromJson(new String(responseBody), ShopRefundSchedule.class);
            if (shopRefundSchedule.getCode()==0){
                examineSpeed = shopRefundSchedule.getData().getExamineSpeed();
                schduleAdapter.setList(examineSpeed);
            }else {
                ToastUtil.showToastShortBottom(mContext,shopRefundSchedule.getMsg());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.dimissDialog();
            ToastUtil.showToastShortBottom(mContext,"请求失败");
        }
    };

    private void initData() {
        Intent intent = getIntent();
        refundList = (List<MallOrderDetailGoodItems>) intent.getSerializableExtra("goodList");
        orderId = intent.getIntExtra("orderId", 0);
        mallRuleUrl = intent.getStringExtra("mallRuleUrl");
        if (refundList!=null&&refundList.size()!=0){
            for (int i = 0; i < refundList.size(); i++) {
                iconList.add(refundList.get(i).getThumbnail());
                amount+=refundList.get(i).getAmount();
            }
        }
    }

    private void setView() {
        tvTitlebarTitle.setText("退货进度");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("退货规则");
        tvTitlebarOther.setTextColor(Color.parseColor("#FFFFFF"));
        tvRefundschduleNum.setText("退货商品  共"+amount+"件");
        iconAdapter = new ShopRefundIconAdapter(iconList,mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRefundschduleIcon.setLayoutManager(layoutManager);
        rvRefundschduleIcon.setAdapter(iconAdapter);
        schduleAdapter = new ShopRefundSchduleAdapter(mContext);
        rvRefundscheduleList.setLayoutManager(new LinearLayoutManager(mContext));
        rvRefundscheduleList.setAdapter(schduleAdapter);
    }

    private void findView() {
        setContentView(R.layout.activity_shop_refund_schedule);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                Intent intent = new Intent(ShopRefundScheduleActivity.this,ADActivity.class);
                intent.putExtra("url",mallRuleUrl);
                startActivity(intent);
                break;
        }
    }
}
