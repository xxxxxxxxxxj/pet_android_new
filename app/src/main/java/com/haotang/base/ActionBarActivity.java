package com.haotang.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActionBarActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.rl_titleall)
    RelativeLayout rlTitleall;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.fm_content)
    FrameLayout fmContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorBar();
        super.setContentView(R.layout.content_base_action);
        ButterKnife.bind(this);
        showBackIcon();
    }
    @Override
    public void setContentView(int layoutResID) {
        //填充子类的布局
        getLayoutInflater().inflate(layoutResID, (FrameLayout) findViewById(R.id.fm_content));
    }
    public void showBackIcon() {
        ibTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void setTitle(String title) {
        tvTitlebarTitle.setText(title);
    }

    public void setRightTitle(String title, View.OnClickListener onClickListener) {
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText(title);
        tvTitlebarOther.setOnClickListener(onClickListener);
    }

    public void setRightLeftImage(int imgRes, View.OnClickListener onClickListener) {
        ibTitlebarOther.setImageResource(imgRes);
        ibTitlebarOther.setOnClickListener(onClickListener);
        ibTitlebarOther.setVisibility(View.VISIBLE);
    }

}
