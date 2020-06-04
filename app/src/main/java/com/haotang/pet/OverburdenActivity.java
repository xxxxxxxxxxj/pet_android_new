package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.SharedPreferenceUtil;

public class OverburdenActivity extends SuperActivity{

	private RelativeLayout layout_show_notice;
	private OverburdenActivity overburdenActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
//
		setContentView(R.layout.activity_over_burden);
		overburdenActivity = this;
		layout_show_notice = (RelativeLayout) findViewById(R.id.layout_show_notice);
		layout_show_notice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferenceUtil.getInstance(overburdenActivity).saveBoolean("isShowOverBurden", false);
				finishWithAnimation();
			}
		});
	}
}
