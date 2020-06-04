package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ChooseCityAdapter;
import com.haotang.pet.entity.ShopListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseCityActivity extends SuperActivity {

    @BindView(R.id.iv_choosecity_back)
    ImageView ivChoosecityBack;
    @BindView(R.id.rv_choosecity_list)
    RecyclerView rvChoosecityList;
    private ShopListBean.DataBean shopListBeanData;
    private List<ShopListBean.DataBean.RegionsBean> regions;
    private ChooseCityAdapter chooseCityAdapter;
    private static int RESULT_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        setListener();
    }

    private void setListener() {
        chooseCityAdapter.setListener(new ChooseCityAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_CODE,intent);
                finish();
            }
        });
    }

    private void initData() {
        shopListBeanData = (ShopListBean.DataBean) getIntent().getSerializableExtra("shopData");
        regions = shopListBeanData.getRegions();
    }

    private void setView() {
        setContentView(R.layout.activity_choose_city);
        ButterKnife.bind(this);
        chooseCityAdapter = new ChooseCityAdapter(mContext);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvChoosecityList.setLayoutManager(layoutManager);
        rvChoosecityList.setAdapter(chooseCityAdapter);
        chooseCityAdapter.setList(regions);
    }

    @OnClick(R.id.iv_choosecity_back)
    public void onViewClicked() {
        finish();
    }
}
