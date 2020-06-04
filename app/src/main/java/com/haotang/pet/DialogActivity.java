package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.haotang.base.SuperActivity;
import com.haotang.pet.util.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends SuperActivity {
    @BindView(R.id.rl_newusercoupon)
    RelativeLayout rlNewusercoupon;
    @BindView(R.id.iv_newusercoupon)
    ImageView ivNewusercoupon;
    @BindView(R.id.iv_newusercoupon_close)
    ImageView ivNewusercouponClose;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        imgUrl = getIntent().getStringExtra("imgUrl");
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        Glide.with(DialogActivity.this)
                .load(imgUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int width = resource.getIntrinsicWidth();
                        int height = resource.getIntrinsicHeight();
                        int dp2pxWidth = DensityUtil.dp2px(DialogActivity.this, width / 2);
                        int dp2pxHeight = DensityUtil.dp2px(DialogActivity.this, height / 2);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlNewusercoupon.getLayoutParams();
                        layoutParams.width = dp2pxWidth;
                        layoutParams.height = dp2pxHeight;
                        rlNewusercoupon.setLayoutParams(layoutParams);

                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) ivNewusercoupon.getLayoutParams();
                        layoutParams1.width = dp2pxWidth;
                        layoutParams1.height = dp2pxHeight;
                        ivNewusercoupon.setLayoutParams(layoutParams1);
                        ivNewusercoupon.setImageDrawable(resource);
                        ivNewusercouponClose.setVisibility(View.VISIBLE);
                    }

                });
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
