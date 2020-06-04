package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/23 0023.
 */

public class OrderDetailServiceNoteActivity extends SuperActivity {
    @BindView(R.id.show_top)
    LinearLayout showTop;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.choose_sure)
    ImageView chooseSure;
    @BindView(R.id.textview_show_notice)
    TextView textviewShowNotice;
    private String note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetailservicenote);
        ButterKnife.bind(this);
        note = getIntent().getStringExtra("note");
        textviewShowNotice.setText(note);
    }

    @OnClick({R.id.show_top, R.id.choose_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.show_top:
                finish();
                break;
            case R.id.choose_sure:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.bottom_silent,R.anim.activity_close);
    }
}
