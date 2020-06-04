package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.collection.ArrayMap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SearchAddressAdapter;
import com.haotang.pet.adapter.SearchAddressResultAdapter;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchAddressActivity extends SuperActivity implements OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {

    @BindView(R.id.tv_searchaddress_close)
    TextView tvSearchaddressClose;
    @BindView(R.id.tv_searchaddress_sure)
    TextView tvSearchaddressSure;
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.rl_searchaddress_top)
    RelativeLayout rlSearchaddressTop;
    @BindView(R.id.listView_show_choose_address)
    ListView listViewServiceShowChooseAddress;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.tv_search_canle)
    TextView tvSearchCanle;
    @BindView(R.id.rl_searchaddress_search)
    RelativeLayout rlSearchaddressSearch;
    @BindView(R.id.et_searchaddress)
    EditText etSearchaddress;
    @BindView(R.id.rl_searchaddress_down)
    RelativeLayout rlSearchDown;
    @BindView(R.id.listView_show_search_address)
    ListView listViewServiceSearchChooseAddress;
    private boolean isLocation = true;
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch = null;
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private String localName = "";
    boolean isFirstLoc = true;// 是否首次定位
    private MyLocationData locData = null;
    private List<ArrayMap<String, String>> addressList = new ArrayList<ArrayMap<String, String>>();
    private List<ArrayMap<String, String>> SearchressList = new ArrayList<ArrayMap<String, String>>();
    private SearchAddressAdapter ChooseBottomAdapter = null;
    private SearchAddressResultAdapter resultAdapter = null;
    // 定位相关
    LocationClient mLocClient;
    private BDLocation location;
    private LatLng point;
    private View mPopupView;
    private LatLng latLng = null;
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        initWindows();
        SDKInitializer.initialize(getApplicationContext());
        initMap();
        initListener();
    }

    private void initListener() {
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset == 1) {
                    rlSearchDown.setVisibility(View.VISIBLE);
                } else if (slideOffset == 0) {
                    rlSearchDown.setVisibility(View.GONE);
                    Utils.goneJP(mContext);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
        etSearchaddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                try {
                    if (!etSearchaddress.getText().toString()
                            .equals("")
                            || !etSearchaddress.getText().toString()
                            .equals(null)) {
                        isLocation = false;
                        SearchressList = new ArrayList<ArrayMap<String, String>>();
                        SearchressList.clear();

                        mPoiSearch.searchInCity(new PoiCitySearchOption()
                                .city(SharedPreferenceUtil.getInstance(
                                        SearchAddressActivity.this).getString(
                                        "city", "北京"))
                                .keyword(
                                        etSearchaddress.getText()
                                                .toString()).pageNum(0).pageCapacity(20).cityLimit(false));
                        setScrollableView();
                    } else {
                        etSearchaddress.setFocusable(false);
                        etSearchaddress.setFocusableInTouchMode(false);
                        listViewServiceSearchChooseAddress.setVisibility(View.GONE);
                        listViewServiceShowChooseAddress.setVisibility(View.VISIBLE);
                        setScrollableView();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (etSearchaddress.getText().toString()
                        .equals("")
                        || etSearchaddress.getText().toString()
                        .equals(null)) {
                    etSearchaddress.setFocusable(false);
                    etSearchaddress.setFocusableInTouchMode(false);
                    listViewServiceSearchChooseAddress.setVisibility(View.GONE);
                    listViewServiceShowChooseAddress.setVisibility(View.VISIBLE);
                    setScrollableView();
                }
            }
        });
        listViewServiceShowChooseAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (addressList != null && addressList.size() > 0) {
                    for (int i = 0; i < addressList.size(); i++) {
                        if (position == i) {
                            addressList.get(i).put("isSecled", "yes");
                        } else {
                            addressList.get(i).put("isSecled", "no");
                        }
                    }
                    ChooseBottomAdapter.notifyDataSetChanged();
                }
            }
        });
        listViewServiceSearchChooseAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (SearchressList != null && SearchressList.size() > 0) {
                    for (int i = 0; i < SearchressList.size(); i++) {
                        if (position == i) {
                            SearchressList.get(i).put("isSecled", "yes");
                        } else {
                            SearchressList.get(i).put("isSecled", "no");
                        }
                    }
                    resultAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void setView() {
        setContentView(R.layout.activity_search_address);
        ButterKnife.bind(this);
        setScrollableView();
    }

    private void setScrollableView() {
        if (listViewServiceSearchChooseAddress.isShown()) {
            slidingLayout.setScrollableView(listViewServiceSearchChooseAddress);
        } else {
            slidingLayout.setScrollableView(listViewServiceShowChooseAddress);
        }
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

    private void initMap() {
        // 地图初始化
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mPopupView = LayoutInflater.from(this).inflate(R.layout.newpop_view,
                null);
        mBaiduMap = mMapView.getMap();
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @OnClick({R.id.tv_searchaddress_close, R.id.tv_searchaddress_sure, R.id.rl_searchaddress_top, R.id.rl_searchaddress_down, R.id.et_searchaddress, R.id.tv_search_canle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_searchaddress_close:
                finish();
                break;
            case R.id.tv_searchaddress_sure:
                String address = "";
                String lat = "";
                String lng = "";
                if (listViewServiceShowChooseAddress.isShown()) {//判断当前选中的地址
                    if (addressList != null && addressList.size() > 0) {
                        for (int i = 0; i < addressList.size(); i++) {
                            if (addressList.get(i).get("isSecled").equals("yes")) {
                                address = addressList.get(i).get("name");
                                lat = addressList.get(i).get("lat");
                                lng = addressList.get(i).get("lng");
                            }
                        }
                        Intent intent = new Intent();
                        intent.putExtra("address", address);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        Utils.mLogError("lat"+lat+"--------"+"lng"+lng);
                        setResult(1005, intent);
                        finish();
                    }
                } else {
                    if (SearchressList != null && SearchressList.size() > 0) {
                        for (int i = 0; i < SearchressList.size(); i++) {
                            if (SearchressList.get(i).get("isSecled").equals("yes")) {
                                address = SearchressList.get(i).get("name");
                                lat = SearchressList.get(i).get("lat");
                                lng = SearchressList.get(i).get("lng");
                            }
                        }
                        Intent intent = new Intent();
                        intent.putExtra("address", address);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        setResult(1005, intent);
                        finish();
                    }
                }
                break;
            case R.id.rl_searchaddress_top:
                rlSearchaddressSearch.setVisibility(View.VISIBLE);
                rlSearchaddressTop.setVisibility(View.GONE);
                etSearchaddress.setFocusable(true);
                etSearchaddress.setFocusableInTouchMode(true);
                etSearchaddress.requestFocus();
                InputMethodManager inputManager =
                        (InputMethodManager) etSearchaddress.getContext().getSystemService(mContext.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etSearchaddress, 0);
                slidingLayout.smoothToTop();
                break;
            case R.id.rl_searchaddress_down:
                slidingLayout.smoothToBottom();
                Utils.goneJP(mContext);
                break;
            case R.id.et_searchaddress:
                slidingLayout.smoothToTop();
                break;
            case R.id.tv_search_canle:
                etSearchaddress.setFocusable(false);
                etSearchaddress.setFocusableInTouchMode(false);
                etSearchaddress.requestFocus();
                rlSearchaddressSearch.setVisibility(View.GONE);
                rlSearchaddressTop.setVisibility(View.VISIBLE);
                etSearchaddress.setText("");
                listViewServiceSearchChooseAddress.setVisibility(View.GONE);
                listViewServiceShowChooseAddress.setVisibility(View.VISIBLE);
                setScrollableView();
                Utils.goneJP(mContext);
                break;
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            mPDialog.closeDialog();
            Toast.makeText(SearchAddressActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(SharedPreferenceUtil.getInstance(
                        SearchAddressActivity.this).getString(
                        "city", "北京")).pageCapacity(20)
                .pageNum(0).pageCapacity(20).cityLimit(false));
        mLocClient.stop();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            spUtil.saveString("city", location.getCity());
            SearchAddressActivity.this.location = location;
            localName = location.getAddrStr();
            locData = new MyLocationData.Builder().accuracy(0.0f)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(-1).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Utils.mLogError("==-->mLocClient latLng " + latLng.longitude
                    + "  latitude :=  " + latLng.latitude);
            if (isFirstLoc) {
                isFirstLoc = false;
                // 设置地图中心点
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
            try {
                Utils.mLogError("localName==null?" + (localName == null));
                if (localName == null || "".equals(localName)
                        || "null".equals(localName)) {
                    if (latLng != null) {
                        // 反Geo搜索
                        mPDialog.showDialog();
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(latLng));
                    }
                } else if (localName != null && !"".equals(localName)
                        && !"null".equals(localName)) {
                    mPDialog.showDialog();
                    mPoiSearch.searchNearby((new PoiNearbySearchOption())
                            .keyword(localName).pageCapacity(20)
                            .location(latLng)
                            .radius(1000)
                            .pageNum(0).pageCapacity(20));
                    SearchressList.clear();
                    mLocClient.stop();
                    displayDriverLocations();
                    Utils.mLogError("==-->mLocClient mLocClient.stop()");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDriverLocations() {
        mBaiduMap.clear();
        point = new LatLng(SearchAddressActivity.this.location.getLatitude(),
                SearchAddressActivity.this.location.getLongitude());
        Projection mProjection = mBaiduMap.getProjection();
        Point pointNew = mProjection.toScreenLocation(point);
        pointNew.y += 60;// 屏幕坐标点偏移60
        LatLng latLngNew = mBaiduMap.getProjection().fromScreenLocation(
                pointNew);
        // 构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromBitmap(getBitmapFromView(mPopupView));
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory
                .fromResource(R.drawable.bk_empty);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, bitmap2));
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latLngNew)
                .zIndex(5).icon(bitmap1);
        mBaiduMap.addOverlay(option);
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        mPDialog.closeDialog();
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(SearchAddressActivity.this, "未找到结果",
                    Toast.LENGTH_LONG).show();
            //layout_top_show.setVisibility(View.GONE);
            return;
        }
        try {
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                Utils.mLogError("==-->search NO_ERROR");
                List<PoiInfo> infos = result.getAllPoi();
                String lstr = "";
                for (int i = 0; i < infos.size(); i++) {
                    String name = infos.get(i).name;
                    if (i == 0) {
                        lstr = infos.get(0).name;
                    }
                    String address = infos.get(i).address;
                    LatLng latLng = infos.get(i).location;

                    Utils.mLogError("==-->mLocClient address:= " + address
                            + " latLng:= " + latLng);
                    double lat = latLng.latitude;
                    double lng = latLng.longitude;
                    int distance = infos.get(i).distance;
                    int detail = infos.get(i).distance;
                    Utils.mLogError("==-->direction=" + detail);
                    ArrayMap<String, String> map = new ArrayMap<String, String>();
                    if (i == 0) {
                        map.put("isSecled", "yes");
                    } else {
                        map.put("isSecled", "no");
                    }
                    map.put("name", name);
                    map.put("address", address);
                    map.put("distance", distance+"");
                    map.put("lat", lat + "");
                    map.put("lng", lng + "");
                    if (isLocation) {
                        if (addressList != null) {
                            addressList.add(map);
                            if (i == infos.size() - 1) {
                                listViewServiceShowChooseAddress
                                        .setVisibility(View.VISIBLE);
                                ChooseBottomAdapter = new SearchAddressAdapter(
                                        this, addressList, lstr);
                                listViewServiceShowChooseAddress
                                        .setAdapter(ChooseBottomAdapter);
                                ChooseBottomAdapter.notifyDataSetChanged();
                            }
                        }
                        setScrollableView();
                    } else {
                        if (SearchressList != null) {
                            SearchressList.add(map);
                            listViewServiceShowChooseAddress.setVisibility(View.GONE);
                            listViewServiceSearchChooseAddress.setVisibility(View.VISIBLE);
                            if (i == infos.size() - 1) {
                                resultAdapter = new SearchAddressResultAdapter(
                                        this, SearchressList, lstr);
                                listViewServiceSearchChooseAddress
                                        .setAdapter(resultAdapter);
                                resultAdapter.notifyDataSetChanged();
                            }
                            setScrollableView();
                        }
                    }
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
}
