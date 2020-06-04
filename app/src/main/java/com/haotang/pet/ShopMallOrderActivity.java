package com.haotang.pet;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.fragment.ShopMallOrderFragment;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * 商品订单页
 */
public class ShopMallOrderActivity extends BaseFragmentActivity implements
        View.OnClickListener {
    private MProgressDialog pDialog;
    private ImageButton ibBack;
    private TextView tvTitle;
    private SharedPreferenceUtil spUtil;
    private SlidingTabLayout stl_shopmall_order;
    private ViewPager vp_shopmall_order;
    private int setPosition;
    public static ShopMallOrderActivity act;
    private int[] colors =new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
    }

    private void init() {
        act = this;
        MApplication.listAppoint.add(act);
        pDialog = new MProgressDialog(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
    }

    private void findView() {
        setContentView(R.layout.activity_shop_mall_order);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        stl_shopmall_order = (SlidingTabLayout) findViewById(R.id.stl_shopmall_order);
        vp_shopmall_order = (ViewPager) findViewById(R.id.vp_shopmall_order);
    }

    private void setView() {
        setPosition = getIntent().getIntExtra("position",0);
        tvTitle.setText("我的订单");
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.shopmall_order_state1, ShopMallOrderFragment.class)
                .add(R.string.shopmall_order_state2, ShopMallOrderFragment.class)
                .add(R.string.shopmall_order_state3, ShopMallOrderFragment.class)
                .add(R.string.shopmall_order_state4, ShopMallOrderFragment.class)
                .add(R.string.shopmall_order_state5, ShopMallOrderFragment.class)
                .add(R.string.shopmall_order_state6, ShopMallOrderFragment.class)
                .create());
        stl_shopmall_order.setGradient(true);
        stl_shopmall_order.setmTextSelectsize(stl_shopmall_order.sp2px(16));
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        stl_shopmall_order.setColors(colors);
        stl_shopmall_order.setIndicatorTextMiddle(true);
        vp_shopmall_order.setAdapter(adapter);
        stl_shopmall_order.setViewPager(vp_shopmall_order);
        vp_shopmall_order.setCurrentItem(setPosition);
        stl_shopmall_order.setCurrentTab(setPosition);
    }

    private void setLinster() {
        ibBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            default:
                break;
        }
    }

}
