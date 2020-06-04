package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SelectFosterCouponAdapter;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.entity.TabEntity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ShadowLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 寄养下单选择优惠券
 */
public class SelectFosterCouponActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ctl_selectfostercoupon)
    CommonTabLayout ctlSelectfostercoupon;
    @BindView(R.id.rv_selectfostercoupon)
    RecyclerView rvSelectfostercoupon;
    @BindView(R.id.sl_selectfostercoupon_bottom)
    ShadowLayout slSelectfostercouponBottom;
    private List<Coupon> couponList = new ArrayList<>();
    private SelectFosterCouponAdapter selectFosterCouponAdapter;
    private String[] mTitles = {"寄养优惠券", "服务优惠券"};
    private int[] mIconUnselectIds = {
            R.drawable.icon_default, R.drawable.icon_default};
    private int[] mIconSelectIds = {
            R.drawable.icon_default, R.drawable.icon_default};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] colors = new int[2];
    private int index;
    private View header;
    private int couponId;
    private List<Coupon> serviceCouponList;
    private List<Coupon> fosterCouponList;
    private String strp;

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
        index = getIntent().getIntExtra("couponIndex", 0);
        couponId = getIntent().getIntExtra("couponId", 0);
        strp = getIntent().getStringExtra("strp");
        serviceCouponList = (List<Coupon>) getIntent().getSerializableExtra("serviceCouponList");
        fosterCouponList = (List<Coupon>) getIntent().getSerializableExtra("fosterCouponList");
    }

    private void findView() {
        setContentView(R.layout.activity_select_foster_coupon);
        ButterKnife.bind(this);
        header = LayoutInflater.from(this).inflate(
                R.layout.header_selectfostercoupon, null);
    }

    private void setView() {
        tvTitlebarTitle.setText("我的优惠券");
        if (Utils.isStrNull(strp)) {//有离店洗美
            ctlSelectfostercoupon.setVisibility(View.VISIBLE);
            ctlSelectfostercoupon.setGradient(true);
            ctlSelectfostercoupon.setmTextSelectsize(ctlSelectfostercoupon.sp2px(16));
            colors[0] = getResources().getColor(R.color.aeb6340);
            colors[1] = getResources().getColor(R.color.ae5287b);
            ctlSelectfostercoupon.setColors(colors);
            ctlSelectfostercoupon.setIndicatorTextMiddle(true);
            for (int i = 0; i < mTitles.length; i++) {
                mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
            }
            ctlSelectfostercoupon.setTabData(mTabEntities);
            ctlSelectfostercoupon.setCurrentTab(index);
        } else {//没有有离店洗美
            ctlSelectfostercoupon.setVisibility(View.GONE);
        }
        rvSelectfostercoupon.setHasFixedSize(true);
        rvSelectfostercoupon.setLayoutManager(new LinearLayoutManager(this));
        selectFosterCouponAdapter = new SelectFosterCouponAdapter(R.layout.item_mycoupon_list_avail, couponList);
        rvSelectfostercoupon.setAdapter(selectFosterCouponAdapter);
        selectFosterCouponAdapter.addHeaderView(header);
        if (Utils.isStrNull(strp)) {//有离店洗美
            if (index == 0) {//寄养
                if (fosterCouponList != null && fosterCouponList.size() > 0) {
                    couponList.addAll(fosterCouponList);
                }
            } else if (index == 1) {//离店洗美
                if (serviceCouponList != null && serviceCouponList.size() > 0) {
                    couponList.addAll(serviceCouponList);
                }
            }
        } else {//寄养
            if (fosterCouponList != null && fosterCouponList.size() > 0) {
                couponList.addAll(fosterCouponList);
            }
        }
        if (couponList != null && couponList.size() > 0) {
            if (couponId > 0) {
                for (int i = 0; i < couponList.size(); i++) {
                    couponList.get(i).isChoose = false;
                }
                for (int i = 0; i < couponList.size(); i++) {
                    if (couponList.get(i).id == couponId) {
                        couponList.get(i).isChoose = true;
                        break;
                    }
                }
                if (index == 0) {//寄养
                    if (fosterCouponList != null && fosterCouponList.size() > 0) {
                        for (int i = 0; i < fosterCouponList.size(); i++) {
                            fosterCouponList.get(i).isChoose = false;
                        }
                        for (int i = 0; i < fosterCouponList.size(); i++) {
                            if (fosterCouponList.get(i).id == couponId) {
                                fosterCouponList.get(i).isChoose = true;
                                break;
                            }
                        }
                    }
                } else if (index == 1) {//离店洗美
                    if (serviceCouponList != null && serviceCouponList.size() > 0) {
                        for (int i = 0; i < couponList.size(); i++) {
                            serviceCouponList.get(i).isChoose = false;
                        }
                        for (int i = 0; i < serviceCouponList.size(); i++) {
                            if (serviceCouponList.get(i).id == couponId) {
                                serviceCouponList.get(i).isChoose = true;
                                break;
                            }
                        }
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
                slSelectfostercouponBottom.setVisibility(View.VISIBLE);
                header.setVisibility(View.GONE);
            } else {
                slSelectfostercouponBottom.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
            }
        } else {
            header.setVisibility(View.GONE);
            slSelectfostercouponBottom.setVisibility(View.GONE);
            selectFosterCouponAdapter.setEmptyView(setEmptyViewBase(2, "很遗憾，暂无可用优惠券", null));
        }
        selectFosterCouponAdapter.notifyDataSetChanged();
    }

    private void setLinster() {
        if (Utils.isStrNull(strp)) {//有离店洗美
            ctlSelectfostercoupon.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    couponList.clear();
                    ctlSelectfostercoupon.setmTextSelectsize(ctlSelectfostercoupon.sp2px(16));
                    index = position;
                    if (index == 0) {//寄养
                        if (fosterCouponList != null && fosterCouponList.size() > 0) {
                            couponList.addAll(fosterCouponList);
                        }
                    } else if (index == 1) {//离店洗美
                        if (serviceCouponList != null && serviceCouponList.size() > 0) {
                            couponList.addAll(serviceCouponList);
                        }
                    }
                    if (couponList != null && couponList.size() > 0) {
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
                            slSelectfostercouponBottom.setVisibility(View.VISIBLE);
                            header.setVisibility(View.GONE);
                        } else {
                            slSelectfostercouponBottom.setVisibility(View.GONE);
                            header.setVisibility(View.VISIBLE);
                        }
                    } else {
                        header.setVisibility(View.GONE);
                        slSelectfostercouponBottom.setVisibility(View.GONE);
                        selectFosterCouponAdapter.setEmptyView(setEmptyViewBase(2, "很遗憾，暂无可用优惠券", null));
                    }
                    selectFosterCouponAdapter.notifyDataSetChanged();
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
        }
        selectFosterCouponAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("TAG", "position = " + position);
                Log.e("TAG", "couponList.size() = " + couponList.size());
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
                    if (index == 0) {//寄养
                        if (fosterCouponList != null && fosterCouponList.size() > 0) {
                            fosterCouponList.clear();
                            fosterCouponList.addAll(couponList);
                        }
                    } else if (index == 1) {//离店洗美
                        if (serviceCouponList != null && serviceCouponList.size() > 0) {
                            serviceCouponList.clear();
                            serviceCouponList.addAll(couponList);
                        }
                    }
                    boolean isChoose = false;
                    for (int i = 0; i < couponList.size(); i++) {
                        if (couponList.get(i).isChoose) {
                            isChoose = true;
                            break;
                        }
                    }
                    if (isChoose) {
                        if (index == 0) {//寄养
                            //清掉选中的洗美优惠券
                            if (serviceCouponList != null && serviceCouponList.size() > 0) {
                                for (int i = 0; i < serviceCouponList.size(); i++) {
                                    serviceCouponList.get(i).isChoose = false;
                                }
                            }
                        } else if (index == 1) {//离店洗美
                            //清掉选中的寄养优惠券
                            if (fosterCouponList != null && fosterCouponList.size() > 0) {
                                for (int i = 0; i < fosterCouponList.size(); i++) {
                                    fosterCouponList.get(i).isChoose = false;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.btn_selectfostercoupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_selectfostercoupon:
                int couponid = 0;
                String name = "";
                int canUseServiceCard = 0;
                if (fosterCouponList != null && fosterCouponList.size() > 0) {
                    for (int i = 0; i < fosterCouponList.size(); i++) {
                        if (fosterCouponList.get(i).isChoose) {
                            index = 0;
                            couponid = fosterCouponList.get(i).id;
                            name = fosterCouponList.get(i).name;
                            canUseServiceCard = fosterCouponList.get(i).canUseServiceCard;
                            break;
                        }
                    }
                }
                if (serviceCouponList != null && serviceCouponList.size() > 0) {
                    for (int i = 0; i < serviceCouponList.size(); i++) {
                        if (serviceCouponList.get(i).isChoose) {
                            index = 1;
                            couponid = serviceCouponList.get(i).id;
                            name = serviceCouponList.get(i).name;
                            canUseServiceCard = serviceCouponList.get(i).canUseServiceCard;
                            break;
                        }
                    }
                }
                Intent data = new Intent();
                data.putExtra("couponIndex", index);
                data.putExtra("couponid", couponid);
                data.putExtra("name", name);
                data.putExtra("canUseServiceCard", canUseServiceCard);
                setResult(Global.RESULT_OK, data);
                finish();
                break;
        }
    }
}
