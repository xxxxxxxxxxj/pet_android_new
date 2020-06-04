package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointCouponActivity extends SuperActivity {
    @BindView(R.id.iv_newusercoupon)
    ImageView ivNewusercoupon;
    private String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        imgUrl = getIntent().getStringExtra("imgUrl");
        setContentView(R.layout.activity_appoint_coupon);
        ButterKnife.bind(this);
        GlideUtil.loadImg(mContext, imgUrl, ivNewusercoupon, 0);
    }

    @OnClick({R.id.iv_newusercoupon_ck, R.id.iv_newusercoupon_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_newusercoupon_ck:
                startActivity(new Intent(mContext, MyCouponNewActivity.class));
                finish();
                break;
            case R.id.iv_newusercoupon_close:
                finish();
                break;
        }
    }
}
