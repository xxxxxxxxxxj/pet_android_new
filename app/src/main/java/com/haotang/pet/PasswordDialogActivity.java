package com.haotang.pet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.haotang.base.SuperActivity;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.bumptech.glide.request.animation.GlideAnimation;

public class PasswordDialogActivity extends SuperActivity {
    @BindView(R.id.iv_newusercoupon)
    ImageView ivNewusercoupon;
    @BindView(R.id.iv_newusercoupon_close)
    ImageView ivNewusercouponClose;
    private String backup;
    private int point;
    private String activityPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        backup = getIntent().getStringExtra("backup");
        point = getIntent().getIntExtra("point", 0);
        activityPic = getIntent().getStringExtra("activityPic");
        setContentView(R.layout.activity_password_dialog);
        ButterKnife.bind(this);
        Glide.with(mContext)
                .load(activityPic)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int width = resource.getIntrinsicWidth();
                        int height = resource.getIntrinsicHeight();
                        int dp2pxWidth = DensityUtil.dp2px(mContext, width / 2);
                        int dp2pxHeight = DensityUtil.dp2px(mContext, height / 2);

                        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) ivNewusercoupon.getLayoutParams();
                        layoutParams1.width = dp2pxWidth;
                        layoutParams1.height = dp2pxHeight;
                        ivNewusercoupon.setLayoutParams(layoutParams1);
                        ivNewusercoupon.setImageDrawable(resource);
                        ivNewusercouponClose.setVisibility(View.VISIBLE);
                    }
                });
    }

    @OnClick({R.id.iv_newusercoupon, R.id.iv_newusercoupon_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_newusercoupon:
                Utils.goService(mContext, point, backup);
                finish();
                break;
            case R.id.iv_newusercoupon_close:
                finish();
                break;
        }
    }
}
