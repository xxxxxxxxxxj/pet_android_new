package com.haotang.pet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.LngLat;
import com.haotang.pet.util.CoodinateCovertor;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 寄养导航界面
 */
public class FosterNavigationActivity extends SuperActivity {
    @BindView(R.id.mv_fostershoplocation)
    MapView mvFostershoplocation;
    @BindView(R.id.tv_fostershoplocation_name)
    TextView tvFostershoplocationName;
    @BindView(R.id.tv_fostershoplocation_address)
    TextView tvFostershoplocationAddress;
    private double shopLat;
    private double shopLng;
    private String shopAddress;
    private String shopName;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private double lat;
    private double lng;
    private String startAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBD();
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        shopLat = getIntent().getDoubleExtra("lat", 0);
        shopLng = getIntent().getDoubleExtra("lng", 0);
        shopAddress = getIntent().getStringExtra("address");
        shopName = getIntent().getStringExtra("name");
    }

    private void findView() {
        setContentView(R.layout.activity_foster_navigation);
        ButterKnife.bind(this);
    }

    private void setView() {
        Utils.setText(tvFostershoplocationName, shopName, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvFostershoplocationAddress, shopAddress, "", View.VISIBLE, View.VISIBLE);
        mBaiduMap = mvFostershoplocation.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMaxAndMinZoomLevel(18, 3);
        LatLng cp = new LatLng(shopLat, shopLng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(cp)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        MapStatus ms = new MapStatus.Builder().target(cp).zoom(15).build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(ms);
        mBaiduMap.setMapStatus(msu);
    }

    private void setLinster() {

    }

    private void initBD() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(100);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            startAddr = location.getAddrStr();
            mLocationClient.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mvFostershoplocation.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        mvFostershoplocation.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvFostershoplocation.onDestroy();
    }

    @OnClick({R.id.btn_fostershoplocation, R.id.iv_fostershoplocation_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fostershoplocation_back:
                finish();
                break;
            case R.id.btn_fostershoplocation:
                showMapDialog();
                break;
        }
    }

    private void showMapDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(FosterNavigationActivity.this, R.layout.foster_map_dialog, null);
        TextView tv_fostermapdialog_baidu = (TextView) customView.findViewById(R.id.tv_fostermapdialog_baidu);
        TextView tv_fostermapdialog_gaode = (TextView) customView.findViewById(R.id.tv_fostermapdialog_gaode);
        TextView tv_fostermapdialog_qx = (TextView) customView.findViewById(R.id.tv_fostermapdialog_qx);
        RelativeLayout rl_fostermapdialog_parent = (RelativeLayout) customView.findViewById(R.id.rl_fostermapdialog_parent);
        final PopupWindow popupWindow = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setWidth(Utils.getDisplayMetrics(FosterNavigationActivity.this)[0]);
        popupWindow.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        tv_fostermapdialog_baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (checkApkExist(FosterNavigationActivity.this, Global.BaiDuMapPackageName)) {
                    try {
                        LngLat lngLat = CoodinateCovertor.bd_encrypt(new LngLat(lng, lat));
                        LngLat slngLat = CoodinateCovertor.bd_encrypt(new LngLat(lng, lat));
                        Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + slngLat.getLantitude()
                                + "," + slngLat.getLongitude() +
                                "|name:" + startAddr + "&destination=latlng:" + lngLat.getLantitude()
                                + "," + lngLat.getLongitude() + "|name:" + shopAddress + "&mode=driving" +
                                "&src=" + getResources().getString(R.string.app_name) + "#Intent;" +
                                "scheme=bdapp;package=com.baidu.BaiduMap;end");
                        startActivity(intent); //启动调用
                    } catch (Exception e) {
                        e.printStackTrace();
                        LngLat slngLat = CoodinateCovertor.bd_encrypt(new LngLat(lng, lat));
                        LngLat lngLat = CoodinateCovertor.bd_encrypt(new LngLat(lng, lat));
                        String url = "http://api.map.baidu.com/direction?origin=latlng:" + slngLat.getLantitude() +
                                "," + slngLat.getLongitude() +
                                "|name:" + startAddr + "&destination=latlng:" + lngLat.getLantitude()
                                + "," + lngLat.getLongitude() + "|name:" + shopAddress + "&mode=driving" +
                                "&output=html&src=" + getResources().getString(R.string.app_name);
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } else {
                    LngLat slngLat = CoodinateCovertor.bd_encrypt(new LngLat(lng, lat));
                    LngLat lngLat = CoodinateCovertor.bd_encrypt(new LngLat(lng, lat));
                    String url = "http://api.map.baidu.com/direction?origin=latlng:" + slngLat.getLantitude() +
                            "," + slngLat.getLongitude() +
                            "|name:" + startAddr + "&destination=latlng:" + lngLat.getLantitude()
                            + "," + lngLat.getLongitude() + "|name:" + shopAddress + "&mode=driving" +
                            "&output=html&src=" + getResources().getString(R.string.app_name);
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        tv_fostermapdialog_gaode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (checkApkExist(FosterNavigationActivity.this, Global.GaoDeMapPackageName)) {
                    try {
                        Intent intent = Intent.getIntent("androidamap://route?sourceApplication=" + getResources().getString(R.string.app_name) +
                                "&sname=" + startAddr + "&lat=" + lat + "&lng=" + lng + "&dlat=" + lat + "&dlon=" + lng + "&dname=" + shopAddress +
                                "&dev=0&m=0&t=0");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        // 驾车导航
                        intent.setData(Uri.parse("http://uri.amap.com/navigation?from=" + lng + "," + lat + "&to=" + lng + "," + lat +
                                "&mode=car&src=nyx_super"));
                        startActivity(intent); // 启动调用
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    // 驾车导航
                    intent.setData(Uri.parse("http://uri.amap.com/navigation?from=" + lng + "," + lat + "&to=" + lng + "," + lat +
                            "&mode=car&src=nyx_super"));
                    startActivity(intent); // 启动调用
                }
            }
        });
        tv_fostermapdialog_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        rl_fostermapdialog_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
