package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.JoinWorkAdapter;
import com.haotang.pet.entity.MainHospital;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.Utils;

import java.util.ArrayList;

public class JointWorkShopDetail extends SuperActivity {
	private JointWorkShopDetail jDetail;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private PullToRefreshListView listView_show_main_to_list;
	private View header;
	private LinearLayout joinworkdetail_layout_address;
	private LinearLayout joinworkdetail_layout_phone;
	private TextView joinworkdetail_address;
	private TextView joinworkdetail_phone;
	private TextView joinworkdetail_business_time;
	private TextView joinworkdetail_introduce;
	private TextView joinworkdetail_title_name;
	private ArrayList<String> Imagelist = new ArrayList<String>();
	@SuppressWarnings("rawtypes")
	private JoinWorkAdapter joinWorkAdapter;
	private ImageView joinworkdetail_eva_one;
	private ImageView joinworkdetail_eva_two;
	private ImageView joinworkdetail_eva_thr;
	private ImageView joinworkdetail_eva_four;
	private ImageView joinworkdetail_eva_five;
	private ImageView joinworkdetail_imageview;
	private String phoneNum = "";
	private String pic = "";
	private double lat = 0;
	private double lng = 0;
	private String name = "";
	private String addr = "";
	private MainHospital mainHospital;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_to_list);
		jDetail = this;
		initView();
		setView();
		initListener();
	}

	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		joinworkdetail_layout_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lat != 0 || lng != 0) {
					Intent intent = new Intent(jDetail,
							ShopLocationActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("shopname", name);
					intent.putExtra("shopaddr", addr);
					intent.putExtra("shoplat", lat);
					intent.putExtra("shoplng", lng);
					startActivity(intent);
				}
			}
		});

		joinworkdetail_layout_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MDialog mDialog = new MDialog.Builder(jDetail).setTitle("提示")
						.setType(MDialog.DIALOGTYPE_CONFIRM)
						.setMessage("是否拨打电话?").setCancelStr("否").setOKStr("是")
						.positiveListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 确认拨打电话
								Utils.telePhoneBroadcast(jDetail, phoneNum);
							}
						}).build();
				mDialog.show();

			}
		});
	}

	private void setView() {
		mainHospital = (MainHospital) getIntent().getSerializableExtra(
				"mainHospital");
		tv_titlebar_title.setText("商家详情");
		listView_show_main_to_list.getRefreshableView().addHeaderView(header);
		listView_show_main_to_list.setMode(Mode.DISABLED);
		listView_show_main_to_list
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						PullToRefreshBase.Mode mode = refreshView
								.getCurrentMode();
						if (mode == Mode.PULL_FROM_START) {
							// 下拉刷新
							listView_show_main_to_list.onRefreshComplete();
						} else {
							listView_show_main_to_list.onRefreshComplete();
						}
					}
				});
		joinWorkAdapter = new JoinWorkAdapter<String>(this, Imagelist);
		listView_show_main_to_list.setAdapter(joinWorkAdapter);
		getData();
	}

	private void getData() {
		name = mainHospital.name;
		joinworkdetail_title_name.setText(name);
		addr = mainHospital.addr;
		joinworkdetail_address.setText(addr);
		phoneNum = mainHospital.tel;
		joinworkdetail_phone.setText(phoneNum);
		pic = mainHospital.avatar;
		GlideUtil.loadRoundImg(JointWorkShopDetail.this,pic,
				joinworkdetail_imageview,
				R.drawable.icon_production_default,5);
		joinworkdetail_business_time.setText(mainHospital.businessHours);
		joinworkdetail_introduce.setText(mainHospital.intro);
		setStar(mainHospital.level);
		lat = Double.parseDouble(mainHospital.lat);
		lng = Double.parseDouble(mainHospital.lng);
		Imagelist.add(mainHospital.introPic);
		joinWorkAdapter.notifyDataSetChanged();
	}

	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		listView_show_main_to_list = (PullToRefreshListView) findViewById(R.id.listView_show_main_to_list);
		header = LayoutInflater.from(this).inflate(
				R.layout.main_to_list_detail_header_view, null);
		joinworkdetail_layout_address = (LinearLayout) header
				.findViewById(R.id.joinworkdetail_layout_address);
		joinworkdetail_layout_phone = (LinearLayout) header
				.findViewById(R.id.joinworkdetail_layout_phone);
		joinworkdetail_address = (TextView) header
				.findViewById(R.id.joinworkdetail_address);
		joinworkdetail_phone = (TextView) header
				.findViewById(R.id.joinworkdetail_phone);
		joinworkdetail_business_time = (TextView) header
				.findViewById(R.id.joinworkdetail_business_time);
		joinworkdetail_introduce = (TextView) header
				.findViewById(R.id.joinworkdetail_introduce);
		joinworkdetail_title_name = (TextView) header
				.findViewById(R.id.joinworkdetail_title_name);
		joinworkdetail_eva_one = (ImageView) header
				.findViewById(R.id.joinworkdetail_eva_one);
		joinworkdetail_eva_two = (ImageView) header
				.findViewById(R.id.joinworkdetail_eva_two);
		joinworkdetail_eva_thr = (ImageView) header
				.findViewById(R.id.joinworkdetail_eva_thr);
		joinworkdetail_eva_four = (ImageView) header
				.findViewById(R.id.joinworkdetail_eva_four);
		joinworkdetail_eva_five = (ImageView) header
				.findViewById(R.id.joinworkdetail_eva_five);
		joinworkdetail_imageview = (ImageView) header
				.findViewById(R.id.joinworkdetail_imageview);
	}

	private void setStar(int star) {
		switch (star) {
		case 1:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_four
					.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_five
					.setBackgroundResource(R.drawable.star_empty);
			break;
		case 2:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_four
					.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_five
					.setBackgroundResource(R.drawable.star_empty);
			break;
		case 3:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_four
					.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_five
					.setBackgroundResource(R.drawable.star_empty);
			break;
		case 4:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_four.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_five
					.setBackgroundResource(R.drawable.star_empty);
			break;
		case 5:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_four.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_five.setBackgroundResource(R.drawable.star_full);
			break;
		}
	}
}
