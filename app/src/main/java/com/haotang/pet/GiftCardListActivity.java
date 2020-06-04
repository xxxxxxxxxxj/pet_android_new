package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.adapter.GiftCardListPagerAdapter;
import com.haotang.pet.entity.GiftCardList;
import com.haotang.pet.entity.GiftCardListBanner;
import com.haotang.pet.fragment.GiftCardListFragment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.CountdownUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GlideImageLoader;
import com.haotang.pet.view.MyScrollView;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GiftCardListActivity extends BaseFragmentActivity implements OnBannerListener {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.banner_giftcard_list)
    Banner bannerGiftcard;
    @BindView(R.id.vw_giftcard_top)
    View vw_giftcard_top;
    @BindView(R.id.stl_giftcard_list)
    SlidingTabLayout stlGiftcardList;
    @BindView(R.id.vp_giftcard_list)
    ViewPager vpGiftcardList;
    @BindView(R.id.iv_card_list_holder)
    ImageView ivCardHolder;
    @BindView(R.id.sv_giftcard_list)
    MyScrollView svGiftcardList;
    @BindView(R.id.stl_giftcard_list_two)
    SlidingTabLayout stlGiftcardListTwo;
    private double lat;
    private double lng;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private SharedPreferenceUtil spUtil;
    private GiftCardListActivity act;
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<GiftCardListBanner.DataBean.OtherOperateBannerListBean> bannerList;
    private MProgressDialog pDialog;
    private final int TYPE = 2;//Banner请求类型
    private int cityId;
    private int[] colors = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        initBD();
        setView();
        setLinster();
        getData();
        getLocation();
    }

    private void init() {
        cityId = getIntent().getIntExtra("cityId",0);
        act = this;
        MApplication.listAppoint.add(act);
        spUtil = SharedPreferenceUtil.getInstance(this);
    }

    private void setLinster() {
        bannerGiftcard.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        svGiftcardList.setListener(new MyScrollView.Listener() {
            @Override
            public void onScroll(int t) {
                int i = Utils.dip2px(GiftCardListActivity.this, 116);
                if (i <= t) {
                    stlGiftcardListTwo.setVisibility(View.VISIBLE);
                    stlGiftcardList.setVisibility(View.INVISIBLE);
                } else {
                    stlGiftcardListTwo.setVisibility(View.GONE);
                    stlGiftcardList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initBD() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            CountdownUtil.getInstance().cancel("LOCATION_TIMER");
            CommUtil.getGiftCardListCity(GiftCardListActivity.this, lat, lng, getGiftCardListHandler);
            mLocationClient.stop();
        }
    }

    private void setView() {
        tvTitlebarTitle.setText("E卡");
        pDialog = new MProgressDialog(this);
        initTab();
    }

    private void initTab() {
        stlGiftcardList.setmTextSelectsize(stlGiftcardList.sp2px(16));
        stlGiftcardList.setGradient(true);
        stlGiftcardListTwo.setmTextSelectsize(stlGiftcardList.sp2px(16));
        stlGiftcardListTwo.setGradient(true);
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        stlGiftcardList.setColors(colors);
        stlGiftcardList.setIndicatorTextMiddle(true);
        stlGiftcardListTwo.setColors(colors);
        stlGiftcardListTwo.setIndicatorTextMiddle(true);
    }

    private void findView() {
        setContentView(R.layout.activity_gift_card_list);
        ButterKnife.bind(this);
    }

    private void getData() {
        pDialog.showDialog();
        CommUtil.getGiftCardListBanner(this, TYPE, getGiftCardListBannerHandler);
    }

    //请求E卡列表
    private AsyncHttpResponseHandler getGiftCardListHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            pDialog.closeDialog();
            try {
                fragmentList.clear();
                titleList.clear();
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        Gson gson = new Gson();
                        GiftCardList giftCardBean = gson.fromJson(new String(responseBody), GiftCardList.class);
                        GiftCardList.DataBean giftCardBeanData = giftCardBean.getData();
                        if (giftCardBeanData != null) {
                            List<GiftCardList.DataBean.ServiceCardTemplateListBean> serviceCardTemplateList = giftCardBeanData.getServiceCardTemplateList();
                            for (int i = 0; i < serviceCardTemplateList.size(); i++) {
                                if (serviceCardTemplateList.get(i).getTemplates() != null && serviceCardTemplateList.get(i).getTemplates().size() > 0) {
                                    //添加城市标题
                                    titleList.add(serviceCardTemplateList.get(i).getCity());
                                    GiftCardListFragment cardListFragment = new GiftCardListFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putDouble("lat", lat);
                                    bundle.putDouble("lng", lng);
                                    bundle.putInt("position", i);
                                    bundle.putSerializable("list", (Serializable) serviceCardTemplateList.get(i).getTemplates());
                                    cardListFragment.setArguments(bundle);
                                    fragmentList.add(cardListFragment);
                                }
                            }
                            vpGiftcardList.setAdapter(new GiftCardListPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
                            stlGiftcardList.setViewPager(vpGiftcardList);
                            stlGiftcardListTwo.setViewPager(vpGiftcardList);
                            if (cityId>0){
                                for (int i = 0; i < serviceCardTemplateList.size(); i++) {
                                    List<GiftCardList.DataBean.ServiceCardTemplateListBean.TemplatesBean> templates = serviceCardTemplateList.get(i).getTemplates();
                                    for (int j = 0; j < templates.size(); j++) {
                                        if (templates.get(j).getCityId()==cityId){
                                            vpGiftcardList.setCurrentItem(i);
                                            stlGiftcardList.setCurrentTab(i);
                                            stlGiftcardListTwo.setCurrentTab(i);
                                            break;
                                        }
                                    }
                                }
                            }else {
                                for (int i = 0; i < serviceCardTemplateList.size(); i++) {
                                    int selected = serviceCardTemplateList.get(i).getSelected();
                                    if (selected == 1) {
                                        vpGiftcardList.setCurrentItem(i);
                                        stlGiftcardList.setCurrentTab(i);
                                        stlGiftcardListTwo.setCurrentTab(i);
                                    }
                                }
                            }

                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(GiftCardListActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(GiftCardListActivity.this, "请求失败");
        }
    };

    //请求E卡列表页的banner
    private AsyncHttpResponseHandler getGiftCardListBannerHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            pDialog.closeDialog();
            Gson gson = new Gson();
            GiftCardListBanner giftCardListBanner = gson.fromJson(new String(responseBody), GiftCardListBanner.class);
            if (giftCardListBanner != null) {
                GiftCardListBanner.DataBean data = giftCardListBanner.getData();
                if (data != null) {
                    List<GiftCardListBanner.DataBean.OtherOperateBannerListBean> otherOperateBannerList = data.getOtherOperateBannerList();
                    if (otherOperateBannerList != null && otherOperateBannerList.size() != 0) {
                        ivCardHolder.setVisibility(View.GONE);
                        bannerList = otherOperateBannerList;
                        setBanner(otherOperateBannerList);
                    } else {
                        ivCardHolder.setVisibility(View.VISIBLE);
                        bannerGiftcard.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            bannerGiftcard.setVisibility(View.GONE);
            ToastUtil.showToastShortBottom(GiftCardListActivity.this, "请求失败");
        }
    };

    private void setBanner(List<GiftCardListBanner.DataBean.OtherOperateBannerListBean> bannerList) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            list.add(bannerList.get(i).getImgUrl());
        }
        bannerGiftcard.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        if (Utils.checkLogin(GiftCardListActivity.this)) {
            if (bannerList != null && bannerList.size() > 0 && bannerList.size() > position) {
                Utils.goService(GiftCardListActivity.this, bannerList.get(position).getPoint(), bannerList.get(position).getBackup());
            }
        } else {
            startActivity(new Intent(GiftCardListActivity.this, LoginNewActivity.class));
        }
    }

    private void getLocation() {
        pDialog.showDialog("定位中,请稍后...");
        mLocationClient.start();
        CountdownUtil.getInstance().newTimer(5000, 1000, new CountdownUtil.ICountDown() {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mLocationClient.stop();
                CommUtil.getGiftCardListCity(GiftCardListActivity.this, lat, lng, getGiftCardListHandler);
            }
        }, "LOCATION_TIMER");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }
}
