package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SelectFosterCouponAdapter;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.util.Global;
import com.haotang.pet.view.ShadowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 下单选择优惠券(洗美特，商城，订单升级)
 */
public class AvailableCouponActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_selectcoupon)
    RecyclerView rvSelectcoupon;
    @BindView(R.id.sl_selectcoupon_bottom)
    ShadowLayout slSelectcouponBottom;
    private List<Coupon> couponList;
    private int couponId;
    private SelectFosterCouponAdapter selectFosterCouponAdapter;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        couponId = getIntent().getIntExtra("couponId", 0);
        couponList = (List<Coupon>) getIntent().getSerializableExtra("couponList");
    }

    private void findView() {
        setContentView(R.layout.availablecoupon);
        ButterKnife.bind(this);
        header = LayoutInflater.from(this).inflate(
                R.layout.header_selectfostercoupon, null);
    }

    private void setView() {
        tvTitlebarTitle.setText("我的优惠券");
        rvSelectcoupon.setHasFixedSize(true);
        rvSelectcoupon.setLayoutManager(new LinearLayoutManager(this));
        selectFosterCouponAdapter = new SelectFosterCouponAdapter(R.layout.item_mycoupon_list_avail, couponList);
        rvSelectcoupon.setAdapter(selectFosterCouponAdapter);
        selectFosterCouponAdapter.addHeaderView(header);
        if (couponList != null && couponList.size() > 0) {
            if (couponId > 0) {
                for (int i = 0; i < couponList.size(); i++) {
                    if (couponList.get(i).id == couponId) {
                        couponList.get(i).isChoose = true;
                        break;
                    }
                }
            }
            for (int i = 0; i < couponList.size(); i++) {
                if (couponList.get(i).isAvali == 0) {
                    couponList.get(i).isShow = true;
                    break;
                }
            }
            boolean isAvali = false;
            for (int i = 0; i < couponList.size(); i++) {
                if (couponList.get(i).isAvali == 1) {
                    isAvali = true;
                    break;
                }
            }
            if (isAvali) {
                slSelectcouponBottom.setVisibility(View.VISIBLE);
                header.setVisibility(View.GONE);
            } else {
                slSelectcouponBottom.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
            }
        } else {
            header.setVisibility(View.GONE);
            slSelectcouponBottom.setVisibility(View.GONE);
            selectFosterCouponAdapter.setEmptyView(setEmptyViewBase(2, "很遗憾，暂无可用优惠券", null));
        }
    }

    private void setLinster() {
        selectFosterCouponAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (couponList != null && couponList.size() > 0 && couponList.size() > position) {
                    Coupon coupon = couponList.get(position);
                    if (coupon.isAvali != 1) {
                        return;
                    }
                    for (int i = 0; i < couponList.size(); i++) {
                        if (position == i) {
                            couponList.get(i).isChoose = !couponList.get(i).isChoose;
                        } else {
                            couponList.get(i).isChoose = false;
                        }
                    }
                    selectFosterCouponAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick({R.id.ib_titlebar_back, R.id.btn_selectcoupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_selectcoupon:
                if (couponList.size() > 0) {
                    boolean isChoose = false;
                    for (int i = 0; i < couponList.size(); i++) {
                        if (couponList.get(i).isChoose) {
                            isChoose = true;
                            Intent data = new Intent();
                            data.putExtra("couponid", couponList.get(i).id);
                            data.putExtra("name", couponList.get(i).name);
                            data.putExtra("canUseServiceCard", couponList.get(i).canUseServiceCard);
                            setResult(Global.RESULT_OK, data);
                            finish();
                            break;
                        }
                    }
                    if (!isChoose) {
                        Intent data = new Intent();
                        data.putExtra("couponid", 0);
                        setResult(Global.RESULT_OK, data);
                        finish();
                    }
                } else {
                    Intent data = new Intent();
                    data.putExtra("couponid", 0);
                    setResult(Global.RESULT_OK, data);
                    finish();
                }
                break;
        }
    }
}
