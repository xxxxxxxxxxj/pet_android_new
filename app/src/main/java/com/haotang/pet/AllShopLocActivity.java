package com.haotang.pet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ShopLocLeftAdapter;
import com.haotang.pet.adapter.ShopLocRightAdapter;
import com.haotang.pet.entity.ShopListBean;
import com.haotang.pet.entity.ShopLocationEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SlidingUpPanelLayout;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllShopLocActivity extends SuperActivity {

    @BindView(R.id.mv_shoploc)
    MapView mvShoploc;
    @BindView(R.id.iv_shoploc_back)
    ImageView ivShoplocBack;
    @BindView(R.id.tv_shoploc_city)
    TextView tvShoplocCity;
    @BindView(R.id.tv_shoploc_choosepos)
    TextView tvShoplocChoosepos;
    @BindView(R.id.rv_shoploc_left)
    RecyclerView rvShoplocLeft;
    @BindView(R.id.rv_shoploc_right)
    RecyclerView rvShoplocRight;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.tv_address_slidetip)
    TextView tvAddressSlidetip;
    @BindView(R.id.iv_address_slide)
    ImageView ivAddressSlide;
    @BindView(R.id.rl_shopdetail_topinfo)
    RelativeLayout rlTopInfo;
    @BindView(R.id.rl_address_sildeall)
    RelativeLayout rlShopLocGoTop;
    private BaiduMap mBaiduMap;
    private double addr_lat;
    private int previous;
    private double addr_lng;
    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean> region = new ArrayList<>();
    private ShopLocLeftAdapter leftAdapter;
    private ShopLocRightAdapter rightAdapter;
    private int allShopNum;
    private ShopListBean.DataBean shopListBeanData;
    private static int RESULT_CODE = 1001;
    private static int ALLLOC_CODE = 1002;
    private String address = "";
    private Marker lastMaker = null;
    private int lastMakerId = 0;
    private Activity act;
    private String city;
    private int selectShopId;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        initWindows();
        initBD();
        getData();
        setListener();
    }


    private void initData() {
        act = this;
        MApplication.listAppoint1.add(act);
        Intent fIntent = getIntent();
        addr_lat = fIntent.getDoubleExtra("addr_lat", 0);
        previous = fIntent.getIntExtra("previous", 0);
        address = fIntent.getStringExtra("address");
        addr_lng = fIntent.getDoubleExtra("addr_lng", 0);
        selectShopId = fIntent.getIntExtra("shopId", 0);
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.queryShopsWithCity(this, spUtil.getInt("nowShopId", 0), addr_lat, addr_lng, queryShopsWithPrice);
    }

    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(android.R.color.transparent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "1");
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("TAG", "2");
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    private AsyncHttpResponseHandler queryShopsWithPrice = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            Gson gson = new Gson();
            ShopListBean shopListBean = gson.fromJson(new String(responseBody), ShopListBean.class);
            if (shopListBean.getCode() == 0) {
                shopListBeanData = shopListBean.getData();
                if (shopListBeanData != null) {
                    List<ShopListBean.DataBean.RegionsBean> regions = shopListBeanData.getRegions();
                    //默认选中
                    for (int i = 0; i < regions.size(); i++) {
                        if (regions.get(i).getSelected() == 1) {
                            ShopListBean.DataBean.RegionsBean regionsBean = regions.get(i);
                            updateView(regionsBean);
                        }
                    }
                }
            }
            //processData(new String(responseBody));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void setListener() {
        leftAdapter.setListener(new ShopLocLeftAdapter.ItemClickListener() {
            @Override
            public void onItemClick(List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shops) {
                rightAdapter.setList(shops);
                //清除地图上的所有覆盖物
                mBaiduMap.clear();
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_loc_unselect);
                //创建OverlayOptions的集合
                List<OverlayOptions> options = new ArrayList<OverlayOptions>();
                for (int j = 0; j < shops.size(); j++) {
                    LatLng point = new LatLng(shops.get(j).getLat(), shops.get(j).getLng());
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", shops.get(j).getId());
                    bundle.putInt("cityId", shops.get(j).getCityId());
                    bundle.putString("dist", shops.get(j).getDist());
                    bundle.putString("tag", shops.get(j).getTag());
                    bundle.putDouble("lat", shops.get(j).getLat());
                    bundle.putDouble("lng", shops.get(j).getLng());
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .title(shops.get(j).getShopName())
                            .extraInfo(bundle)
                            .icon(bitmap);
                    options.add(option);
                }
                mBaiduMap.addOverlays(options);
            }
        });


        rightAdapter.setListener(new ShopLocRightAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean shopsBean) {
                if (shopsBean != null) {
                    spUtil.saveString("nowCity", city);
                    spUtil.saveString("nowShop", shopsBean.getShopName());
                    spUtil.saveInt("nowShopCityId", shopsBean.getCityId());
                    spUtil.saveInt("nowShopId", shopsBean.getId());
                    spUtil.saveString("nowShopAddr", shopsBean.getAddress());
                    spUtil.saveFloat("nowShopLat", (float) shopsBean.getLat());
                    spUtil.saveFloat("nowShopLng", (float) shopsBean.getLng());
                    spUtil.saveString("shopWxImg", shopsBean.getShopWxImg());
                    spUtil.saveString("shopWxNum", shopsBean.getShopWxNum());
                    spUtil.saveString("shopPhone", shopsBean.getPhone());
                    EventBus.getDefault().post(new ShopLocationEvent(shopsBean.getLat(), shopsBean.getLng(), shopsBean.getId(), shopsBean.getShopName(), city));
                    if (previous == Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST) {
                        Intent intent = new Intent();
                        intent.putExtra("shopid", shopsBean.getId());
                        intent.putExtra("cityId", shopsBean.getCityId());
                        intent.putExtra("shopname", shopsBean.getShopName());
                        intent.putExtra("shopPhone", shopsBean.getPhone());
                        intent.putExtra("shopimg", shopsBean.getImg());
                        intent.putExtra("shopaddr", shopsBean.getAddress());
                        intent.putExtra("lat", shopsBean.getLat());
                        intent.putExtra("lng", shopsBean.getLng());
                        intent.putExtra("tag", shopsBean.getTag());
                        intent.putExtra("openTime", shopsBean.getOpenTime());
                        intent.putExtra("dist", shopsBean.getDist());
                        setResult(Global.RESULT_OK, intent);
                    }
                    finish();
                }
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (lastMaker != null) {
                    if (lastMaker.getId() == marker.getId()) {

                    } else {
                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.icon_loc_unselect);
                        lastMaker.setIcon(bitmap);
                    }
                }
                lastMaker = marker;
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_loc_select);
                marker.setIcon(bitmap);
                View view = View.inflate(mContext, R.layout.marker_allshop_loc, null);
                TextView tvShopName = view.findViewById(R.id.tv_markpop_shopname);
                LatLng latLng = marker.getPosition();
                tvShopName.setText(marker.getTitle());
                InfoWindow infoWindow = new InfoWindow(view, latLng, -70);
                mBaiduMap.showInfoWindow(infoWindow);
                tvShopName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ShopDetailActivity.class);
                        intent.putExtra("shopid", marker.getExtraInfo().getInt("id"));
                        intent.putExtra("addr_lat", marker.getExtraInfo().getInt("lat"));
                        intent.putExtra("addr_lng", marker.getExtraInfo().getInt("lng"));
                        intent.putExtra("cityId", marker.getExtraInfo().getInt("cityId"));
                        intent.putExtra("dist", marker.getExtraInfo().getString("dist"));
                        intent.putExtra("tag", marker.getExtraInfo().getString("tag"));
                        intent.putExtra("previous", previous);
                        startActivity(intent);
                    }
                });
                return true;
            }
        });

        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset == 1) {
                    tvAddressSlidetip.setText("查看地图");
                    rlTopInfo.setBackgroundResource(R.drawable.bg_shoptop_jianbian_addwhite);
                    ivAddressSlide.setImageResource(R.drawable.icon_alladdreaa_down);
                    rlShopLocGoTop.setBackgroundColor(Color.WHITE);
                } else if (slideOffset == 0) {
                    tvAddressSlidetip.setText("查看所有门店");
                    ivAddressSlide.setImageResource(R.drawable.icon_alladdress_up);
                }else {
                    rlTopInfo.setBackgroundResource(R.drawable.bg_shoptop_jianbian);
                    rlShopLocGoTop.setBackgroundResource(R.drawable.bg_white_topround_20);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    private void updateView(ShopListBean.DataBean.RegionsBean regionsBean) {
        //清除地图上的所有覆盖物
        mBaiduMap.clear();
        city = regionsBean.getCity();
        tvShoplocCity.setText(city);
        List<ShopListBean.DataBean.RegionsBean.AllShopsBean> allShops = regionsBean.getAllShops();
        List<ShopListBean.DataBean.RegionsBean.RegionMapBean> regionMap = regionsBean.getRegionMap();
        List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> lacalShops = new ArrayList<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean>();
        lacalShops.clear();
        for (int i = 0; i < regionMap.size(); i++) {
            ShopListBean.DataBean.RegionsBean.RegionMapBean regionMapBean = regionMap.get(i);
            if (regionMapBean != null) {
                List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shops = regionMapBean.getShops();
                if (shops != null && shops.size() > 0) {
                    regionMapBean.setShopNum(shops.size());
                    for (int j = 0; j < shops.size(); j++) {
                        ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean shopsBean = shops.get(j);
                        if (shopsBean != null) {
                            shopsBean.setRegion(regionMapBean.getRegion());
                        }
//                        lacalShops.add(shopsBean);
                    }
                }
            }
        }
        //全部门店
        for (int i = 0; i < allShops.size(); i++) {
            ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean allShopBean = new ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean();
            allShopBean.setAddress(allShops.get(i).getAddress());
            allShopBean.setCityId((allShops.get(i).getCityId()));
            allShopBean.setDist((allShops.get(i).getDist()));
            allShopBean.setHotelImg((allShops.get(i).getHotelImg()));
            allShopBean.setId((allShops.get(i).getId()));
            allShopBean.setShopName(allShops.get(i).getShopName());
            allShopBean.setImg(allShops.get(i).getImg());
            allShopBean.setLng(allShops.get(i).getLng());
            allShopBean.setLat(allShops.get(i).getLat());
            lacalShops.add(allShopBean);
        }
        region.clear();
        for (int k = 0; k < regionMap.size(); k++) {
            List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shopsBeans = regionMap.get(k).getShops();
            for (int j = 0; j < shopsBeans.size(); j++) {
                shopsBeans.get(j).setClassId(k);
                shopsBeans.get(j).setRegion(regionMap.get(k).getRegion());
                allShopNum++;
            }
            region.add(new ShopListBean.DataBean.RegionsBean.RegionMapBean(regionMap.get(k).getRegion(), regionMap.get(k).getAreacode(), regionMap.get(k).getSelected(), regionMap.get(k).getShops().size(), shopsBeans));
        }
        region.add(0, new ShopListBean.DataBean.RegionsBean.RegionMapBean("全部", 0, 0, allShops.size(), lacalShops));
        boolean isSelected = false;
        for (int i = 0; i < region.size(); i++) {
            if (region.get(i).getSelected() == 1) {
                isSelected = true;
                break;
            }
        }
        if (isSelected == false) {
            region.get(0).setSelected(1);
        }
        leftAdapter.setList(region);
        for (int i = 0; i < region.size(); i++) {
            if (region.get(i).getSelected() == 1) {
                rightAdapter.setList(region.get(i).getShops());
                break;
            }
        }
        final List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shops = new ArrayList<>();
        boolean isRegionSelected = false;
        for (int i = 0; i < region.size(); i++) {
            if (region.get(i).getSelected() == 1) {
                isRegionSelected = true;
                break;
            }
        }
        if (isRegionSelected == false) {
            region.get(0).setSelected(1);
        }
        for (int i = 0; i < region.size(); i++) {
            if (region.get(i).getSelected() == 1) {
                shops.addAll(region.get(i).getShops());
            }
        }
        Utils.mLogError(shops.size() + "===================");
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_loc_unselect);
        BitmapDescriptor bitmapSelect = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_loc_select);
        //创建OverlayOptions的集合
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for (int j = 0; j < shops.size(); j++) {
            LatLng point = new LatLng(shops.get(j).getLat(), shops.get(j).getLng());
            Bundle bundle = new Bundle();
            bundle.putInt("id", shops.get(j).getId());
            bundle.putInt("cityId", shops.get(j).getCityId());
            bundle.putString("dist", shops.get(j).getDist());
            bundle.putString("tag", shops.get(j).getTag());
            bundle.putDouble("lat", shops.get(j).getLat());
            bundle.putDouble("lng", shops.get(j).getLng());
            OverlayOptions option = new MarkerOptions()
                    .title(shops.get(j).getShopName())
                    .position(point)
                    .extraInfo(bundle)
                    .icon(bitmap);
            options.add(option);
        }
        //默认选中marker
        if (selectShopId != 0) {
            View view = View.inflate(mContext, R.layout.marker_allshop_loc, null);
            TextView tvShopName = view.findViewById(R.id.tv_markpop_shopname);
            for (int i = 0; i < shops.size(); i++) {
                if (shops.get(i).getId() == selectShopId) {
                    LatLng latLng = new LatLng(shops.get(i).getLat(), shops.get(i).getLng());
                    tvShopName.setText(shops.get(i).getShopName());
                    LatLng point = new LatLng(shops.get(i).getLat(), shops.get(i).getLng());
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", shops.get(i).getId());
                    bundle.putInt("cityId", shops.get(i).getCityId());
                    bundle.putString("dist", shops.get(i).getDist());
                    bundle.putString("tag", shops.get(i).getTag());
                    bundle.putDouble("lat", shops.get(i).getLat());
                    bundle.putDouble("lng", shops.get(i).getLng());
                    options.remove(i);
                    InfoWindow infoWindow = new InfoWindow(view, latLng, -70);
                    OverlayOptions option = new MarkerOptions()
                            .title(shops.get(i).getShopName())
                            .position(point)
                            .extraInfo(bundle)
                            .icon(bitmapSelect)
                            .infoWindow(infoWindow);
                    options.add(option);
                    lastMakerId = shops.get(i).getId();
                    mBaiduMap.showInfoWindow(infoWindow);
                }
            }
        }else if (spUtil.getInt("nowShopId",0)!=0){
            View view = View.inflate(mContext, R.layout.marker_allshop_loc, null);
            TextView tvShopName = view.findViewById(R.id.tv_markpop_shopname);
            for (int i = 0; i < shops.size(); i++) {
                if (shops.get(i).getId() == spUtil.getInt("nowShopId",0)) {
                    LatLng latLng = new LatLng(shops.get(i).getLat(), shops.get(i).getLng());
                    tvShopName.setText(shops.get(i).getShopName());
                    LatLng point = new LatLng(shops.get(i).getLat(), shops.get(i).getLng());
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", shops.get(i).getId());
                    bundle.putInt("cityId", shops.get(i).getCityId());
                    bundle.putString("dist", shops.get(i).getDist());
                    bundle.putString("tag", shops.get(i).getTag());
                    bundle.putDouble("lat", shops.get(i).getLat());
                    bundle.putDouble("lng", shops.get(i).getLng());
                    options.remove(i);
                    InfoWindow infoWindow = new InfoWindow(view, latLng, -70);
                    OverlayOptions option = new MarkerOptions()
                            .title(shops.get(i).getShopName())
                            .position(point)
                            .extraInfo(bundle)
                            .icon(bitmapSelect)
                            .infoWindow(infoWindow);
                    options.add(option);
                    lastMakerId = shops.get(i).getId();
                    mBaiduMap.showInfoWindow(infoWindow);
                }
            }

        }
        mBaiduMap.addOverlays(options);
    }


    private void initBD() {
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        mBaiduMap.setMyLocationEnabled(true);
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
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            bdShowCenter(lat, lng);
            mLocationClient.stop();
        }
    }

    private void bdShowCenter(double lat, double lng) {
        //设定中心点坐标
        LatLng cenpt = new LatLng(lat, lng);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(cenpt)
                //放大地图
                .zoom(12)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(lat);
        locationBuilder.longitude(lng);
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void setView() {
        setContentView(R.layout.activity_all_shop_loc);
        ButterKnife.bind(this);
        mBaiduMap = mvShoploc.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMaxAndMinZoomLevel(18, 3);
        if (!"".equals(address)) {
            tvShoplocChoosepos.setText(address);
//            tvShoplocChoosepos.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
//            tvShoplocChoosepos.setTextColor(Color.parseColor("#F6F8FA"));
        }
        leftAdapter = new ShopLocLeftAdapter(mContext);
        rightAdapter = new ShopLocRightAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvShoplocLeft.setAdapter(leftAdapter);
        rvShoplocLeft.setLayoutManager(layoutManager);
        rvShoplocRight.setLayoutManager(new LinearLayoutManager(mContext));
        rvShoplocRight.setAdapter(rightAdapter);
        slidingLayout.setScrollableView(rvShoplocRight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mvShoploc.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mvShoploc.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mvShoploc.onDestroy();
    }

    @OnClick({R.id.iv_shoploc_back, R.id.tv_shoploc_choosepos, R.id.tv_shoploc_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_shoploc_back:
                finish();
                break;
            case R.id.tv_shoploc_choosepos:
                Intent intentPos = new Intent(mContext, SearchAddressActivity.class);
                startActivityForResult(intentPos, ALLLOC_CODE);
                break;
            case R.id.tv_shoploc_city:
                Intent intent = new Intent(mContext, ChooseCityActivity.class);
                intent.putExtra("shopData", shopListBeanData);
                startActivityForResult(intent, RESULT_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == 1003) {
            int position = data.getIntExtra("position", 0);
            for (int i = 0; i < shopListBeanData.getRegions().size(); i++) {
                if (i == position) {
                    shopListBeanData.getRegions().get(position).setSelected(1);
                } else {
                    shopListBeanData.getRegions().get(i).setSelected(0);
                }
            }

            updateView(shopListBeanData.getRegions().get(position));
        }
        if (requestCode == ALLLOC_CODE && resultCode == 1005) {
            String address = data.getStringExtra("address");
            String lat = data.getStringExtra("lat");
            String lng = data.getStringExtra("lng");
            addr_lat = Double.parseDouble(lat);
            addr_lng = Double.parseDouble(lng);
            bdShowCenter(addr_lat, addr_lng);
            getData();
            tvShoplocChoosepos.setText(address);
        }
    }
}
