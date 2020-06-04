package com.haotang.pet;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SwitchShopAdapter;
import com.haotang.pet.entity.ServiceShopAdd;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 切换门店
 * Created by Administrator on 2018/11/28 0028.
 */

public class SwitchShopActivity extends SuperActivity {
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewAddressPushNum)
    TextView textViewAddressPushNum;
    @BindView(R.id.textViewChangeShop)
    TextView textViewChangeShop;
    @BindView(R.id.listViewShopItem)
    ListView listViewShopItem;
    @BindView(R.id.textViewCancleShop)
    TextView textViewCancleShop;
    public ArrayList<ServiceShopAdd> nows;
    @BindView(R.id.layoutTop)
    LinearLayout layoutTop;
    private SwitchShopAdapter switchShopAdapter;
    private int pos;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switchshop);
        ButterKnife.bind(this);
        initData();
        initAdapter();
        initListener();
    }

    private void initListener() {
        listViewShopItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                for (int i = 0; i < nows.size(); i++) {
                    nows.get(i).isChoose = false;
                }
                nows.get(position).isChoose = true;
                switchShopAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initAdapter() {
        switchShopAdapter = new SwitchShopAdapter(mContext, nows);
        listViewShopItem.setAdapter(switchShopAdapter);
    }

    private void initData() {
        nows = ServiceNewActivity.nows;
        title = getIntent().getStringExtra("title");
        textViewAddressPushNum.setText(title + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        textViewCancleShop.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textViewCancleShop.getPaint().setAntiAlias(true);//抗锯齿
    }

    @OnClick({R.id.textViewChangeShop, R.id.textViewCancleShop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewChangeShop:
                ServiceNewActivity.act.setChangeDetail(pos);
                finishWithAnimation();
                break;
            case R.id.textViewCancleShop:
                ServiceNewActivity.act.setCancleChangeShop();
                finishWithAnimation();
                break;
        }
    }
}
