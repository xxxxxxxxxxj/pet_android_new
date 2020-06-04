package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/7 0007.
 */

public class LookImgActivity extends SuperActivity {
    @BindView(R.id.imgShow)
    ImageView imgShow;
    @BindView(R.id.layout_out)
    LinearLayout layoutOut;
    @BindView(R.id.imgShowLevel)
    ImageView imgShowLevel;
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.layoutImg)
    LinearLayout layoutImg;
    private String avatar;
    private String becomeVipPic;
    private int memberLevelId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_img);
        MApplication.listAppoint.add(this);
        ButterKnife.bind(this);
        avatar = getIntent().getStringExtra("avatar");
        becomeVipPic = getIntent().getStringExtra("becomeVipPic");
        memberLevelId = getIntent().getIntExtra("memberLevelId", -1);
        layoutImg.setVisibility(View.GONE);
        imgShow.setVisibility(View.GONE);
        if (memberLevelId>-1) {
            layoutImg.setVisibility(View.VISIBLE);
            imgShow.setVisibility(View.GONE);
            GlideUtil.loadImg(mContext, becomeVipPic, imgShowLevel, R.drawable.icon_production_default);
        } else {
            layoutImg.setVerticalGravity(View.GONE);
            imgShow.setVisibility(View.VISIBLE);
            GlideUtil.loadCircleImg(mContext, avatar, imgShow, R.drawable.bqmm_default_head);
        }
    }

    @OnClick({R.id.imgShow, R.id.layout_out
    ,R.id.layoutImg
    ,R.id.imgShowLevel
    ,R.id.imgClose
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                finish();
                break;
            case R.id.imgShowLevel:
            case R.id.layoutImg:
                break;
            case R.id.imgShow:
                finish();
                break;
            case R.id.layout_out:
                finish();
                break;
        }
    }
}
