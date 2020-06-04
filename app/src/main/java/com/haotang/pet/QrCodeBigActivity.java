package com.haotang.pet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.haotang.base.SuperActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * 查看二维码界面
 */
public class QrCodeBigActivity extends SuperActivity {
    @BindView(R.id.iv_rqcodebig)
    ImageView ivRqcodebig;
    private String authCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        authCode = getIntent().getStringExtra("authCode");
    }

    private void findView() {
        setContentView(R.layout.activity_qr_code_big);
        ButterKnife.bind(this);
    }

    private void setView() {
        setAuthCode();
    }

    private void setLinster() {

    }

    @OnClick({R.id.ll_rqcodebig})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_rqcodebig:
                finish();
                break;
        }
    }

    private void setAuthCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bingoogolapple = QRCodeEncoder.syncEncodeQRCode(authCode,
                        BGAQRCodeUtil.dp2px(QrCodeBigActivity.this, 198.5f), Color.BLACK, Color.WHITE,
                        BitmapFactory.decodeResource(QrCodeBigActivity.this.getResources(), R.drawable.erweima_logo));
                if (bingoogolapple != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivRqcodebig.setImageBitmap(bingoogolapple);
                        }
                    });
                }
            }
        }).start();
    }
}
