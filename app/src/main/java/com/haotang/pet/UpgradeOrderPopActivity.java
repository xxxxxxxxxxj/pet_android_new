package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 升级订单弹窗待支付
 * Created by Administrator on 2018/11/28 0028.
 */

public class UpgradeOrderPopActivity extends SuperActivity {
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.textViewNotice)
    TextView textViewNotice;
    @BindView(R.id.layoutToPay)
    LinearLayout layoutToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgraderorderpop);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        textViewNotice.setText(title + "");
    }

    @OnClick({R.id.imgClose, R.id.layoutToPay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                finishWithAnimation();
                break;
            case R.id.layoutToPay:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.mainactivity");
                //到洗美订单列表页
                intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
                sendBroadcast(intent);
                Utils.mLogError("开始发送广播");
                finishWithAnimation();
                break;
        }
    }
}
