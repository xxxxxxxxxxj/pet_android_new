package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.adapter.GiftCardListPagerAdapter;
import com.haotang.pet.entity.ShopListBean;
import com.haotang.pet.entity.ShopListBean.DataBean.RegionsBean;
import com.haotang.pet.fragment.ShopListFragment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title:ShopListActivity
 * </p>
 * <p>
 * Description:门店列表
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2017-2-17 上午11:22:04
 */
public class ShopListActivity extends BaseFragmentActivity {
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    public static ShopListActivity act;
    private double addr_lat;
    private double addr_lng;
    private int previous;
    private ViewPager vpShop;
    private SlidingTabLayout slShop;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private int[] colors = new int[2];
    private MProgressDialog mPDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        getData();
        setListener();
    }

    private void setListener() {
        ib_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        act = ShopListActivity.this;
        Intent fIntent = getIntent();
        addr_lat = fIntent.getDoubleExtra("addr_lat", 0);
        previous = fIntent.getIntExtra("previous", 0);
        addr_lng = fIntent.getDoubleExtra("addr_lng", 0);
        MApplication.listAppoint.add(act);
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.queryShopsWithCity(this,spUtil.getInt("nowShopId",0), addr_lat, addr_lng, queryShopsWithPrice);
    }

    private AsyncHttpResponseHandler queryShopsWithPrice = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            processData(new String(responseBody));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void processData(String result) {
        mPDialog.closeDialog();
        Gson gson = new Gson();
        ShopListBean shopListBean = gson.fromJson(result, ShopListBean.class);
        if (shopListBean.getCode()==0){
            titleList.clear();
            fragmentList.clear();
            ShopListBean.DataBean data = shopListBean.getData();
            if (data!=null){
                List<RegionsBean> regions = data.getRegions();
                if (regions!=null&&regions.size()!=0){
                    for (int i = 0; i < regions.size(); i++) {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("addr_lat",addr_lat);
                        bundle.putDouble("addr_lng",addr_lng);
                        bundle.putInt("previous",previous);
                        bundle.putSerializable("list", (Serializable) regions.get(i).getRegionMap());
                        regions.get(i).getRegionMap();
                        ShopListFragment shopListFragment = new ShopListFragment();
                        shopListFragment.setArguments(bundle);
                        fragmentList.add(shopListFragment);
                        titleList.add(regions.get(i).getCity());
                    }
                    vpShop.setAdapter(new GiftCardListPagerAdapter(getSupportFragmentManager(),fragmentList,titleList));
                    slShop.setViewPager(vpShop);
                    //默认选中
                    for (int i = 0; i < regions.size(); i++) {
                        if (regions.get(i).getSelected()==1){
                            slShop.setCurrentTab(i);
                            vpShop.setCurrentItem(i);
                        }
                    }
                }
            }
        }
    }

    private void findView() {
        setContentView(R.layout.activity_shop_list);
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        slShop = (SlidingTabLayout) findViewById(R.id.stl_shop_list);
        vpShop = (ViewPager) findViewById(R.id.vp_shop_list);
        mPDialog =  new MProgressDialog(this);
    }

    private void setView() {
        tv_titlebar_title.setText("选择门店");
        slShop.setmTextSelectsize(slShop.sp2px(16));
        slShop.setGradient(true);
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        slShop.setColors(colors);
        slShop.setIndicatorTextMiddle(true);
    }
}
