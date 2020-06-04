package com.haotang.pet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CommodityDetailImgAdapter;
import com.haotang.pet.adapter.FosterDateAdapter;
import com.haotang.pet.adapter.FosterRoomAdapter;
import com.haotang.pet.entity.FosterDate;
import com.haotang.pet.entity.FosterDay;
import com.haotang.pet.entity.FosterRoom;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.TabEntity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DisplayUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ScreenAdapterUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.GlideImageLoader;
import com.haotang.pet.view.ListenYScrollView;
import com.haotang.pet.view.NiceImageView;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.haotang.pet.view.NoScollSyLinearLayoutManager;
import com.haotang.pet.view.SlidingUpPanelLayout;
import com.haotang.pet.view.popup.QMUIPopup;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 寄养首页
 */
public class FosterHomeActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.banner_foster_home)
    Banner bannerFosterHome;
    @BindView(R.id.ll_banner_indicator)
    LinearLayout llBannerIndicator;
    @BindView(R.id.tv_fosterhome_hotelname)
    TextView tvFosterhomeHotelname;
    @BindView(R.id.tv_fosterhome_star)
    TextView tvFosterhomeStar;
    @BindView(R.id.tv_fosterhome_ordernum)
    TextView tvFosterhomeOrdernum;
    @BindView(R.id.tv_foster_shopdesc)
    ExpandableTextView tvFosterShopdesc;
    @BindView(R.id.tv_foster_shoploc)
    TextView tvFosterShoploc;
    @BindView(R.id.tv_foster_opentime)
    TextView tvFosterOpentime;
    @BindView(R.id.tv_fosterhome_addpet)
    TextView tvFosterhomeAddpet;
    @BindView(R.id.tv_fosterhome_intip)
    TextView tvFosterhomeIntip;
    @BindView(R.id.tv_fosterhome_intime)
    TextView tvFosterhomeIntime;
    @BindView(R.id.tv_fosterhome_totalday)
    TextView tvFosterhomeTotalday;
    @BindView(R.id.rl_fosterhome_num)
    RelativeLayout rlFosterhomeNum;
    @BindView(R.id.tv_fosterhome_leavetip)
    TextView tvFosterhomeLeavetip;
    @BindView(R.id.tv_fosterhome_leavetime)
    TextView tvFosterhomeLeavetime;
    @BindView(R.id.iv_fosterhome_addpet)
    NiceImageView ivFosterhomeAddpet;
    @BindView(R.id.rv_fosterhome_room)
    RecyclerView rvFosterhomeRoom;
    @BindView(R.id.ctl_fosterhome)
    CommonTabLayout ctlFosterhome;
    @BindView(R.id.rv_fosterhome_img)
    RecyclerView rvFosterhomeImg;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.tv_foster_title)
    TextView tvFosterTitle;
    @BindView(R.id.sv_petcare)
    ListenYScrollView svPetcare;
    @BindView(R.id.ll_fosterinfo_topall)
    LinearLayout llFosterinfoTopall;
    @BindView(R.id.ctl_fosterhome_top)
    CommonTabLayout ctlFosterhomeTop;
    @BindView(R.id.iv_banner_indicator1)
    ImageView ivBannerIndicator1;
    @BindView(R.id.iv_banner_indicator2)
    ImageView ivBannerIndicator2;
    @BindView(R.id.rl_fosterhome_top)
    RelativeLayout rlFosterhomeTop;
    @BindView(R.id.iv_fosterhome_gotop)
    ImageView ivFosterhomeGotop;
    private int customerpetid;
    private int hotelCentreId;
    private String hotelAddress;
    private String hotelName;
    private double lng;
    private double lat;
    private String shareImg;
    private String shareTitle;
    private String shareUrl;
    private String shareDesc;
    private String servicePhone;
    private List<FosterRoom> roomList = new ArrayList<FosterRoom>();
    private List<FosterDate> dateList = new ArrayList<FosterDate>();
    private List<FosterDate> localDateList = new ArrayList<FosterDate>();
    private List<String> imgList = new ArrayList<String>();
    private List<String> introduceImgList = new ArrayList<String>();
    private List<String> instructionsImgList = new ArrayList<String>();
    private List<String> bannerImgList = new ArrayList<String>();
    private FosterRoomAdapter fosterRoomAdapter;
    private CommodityDetailImgAdapter commodityDetailImgAdapter;
    private String[] mTitles = {"服务介绍", "预约须知"};
    private int[] mIconUnselectIds = {
            R.drawable.icon_default, R.drawable.icon_default};
    private int[] mIconSelectIds = {
            R.drawable.icon_default, R.drawable.icon_default};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] colors = new int[2];
    private ArrayList<com.haotang.pet.entity.Banner> bannerList = new ArrayList<>();
    private int petId;
    private String startTime;
    private String endTime;
    private int minDay;
    private final static long DAYTIMEINMILLS = 86400000;
    private String festivalsEndDate;
    private int festivalsStartPosition;
    private int festivalsEndPosition;
    private boolean isHaveFestivals;
    private String festivalsStartDate;
    private String festivalsTip;
    private int festivalsDay;
    private List<FosterDay> dayList = new ArrayList<FosterDay>();
    private String contact;
    private String contactPhone;
    private String fosterTime;
    private boolean isGlobal;
    private int popFlag;
    private int popMonthPosition;
    private int popDayPosition;
    private int popSelectDay;
    private int fosterinfoTopallHeight;
    private int cityId;
    private int homeBottomRealHeight;

    //登录返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        if (event != null && event.isLogin()) {
            getData();
            getBanner();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        initData();
        findView();
        setView();
        setListener();
        getData();
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

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_foster_home);
        ButterKnife.bind(this);
    }

    private void setView() {
        if (ScreenAdapterUtil.hasNotchScreen(mContext)) {
            homeBottomRealHeight = DisplayUtil.getWindowHeight(mContext) - rlFosterhomeTop.getLayoutParams().height + DisplayUtil.dip2px(mContext, 50) + DisplayUtil.getStatusBarHeight(mContext);
        } else {
            homeBottomRealHeight = DisplayUtil.getWindowHeight(mContext) - rlFosterhomeTop.getLayoutParams().height + DisplayUtil.dip2px(mContext, 50);
        }
        Log.e("aaaaaaaa", homeBottomRealHeight + "------------");
        slidingLayout.setPanelHeight(homeBottomRealHeight);
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        ivBannerIndicator1.setAnimation(animation);

        Animation animation1 = new AlphaAnimation(0, 1); // Change alpha from fully visible to invisible
        animation1.setDuration(500); // duration - half a second
        animation1.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation1.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation1.setRepeatMode(Animation.REVERSE); //
        ivBannerIndicator2.setAnimation(animation1);
        slidingLayout.setShadowHeight(0);
        tvFosterhomeLeavetime.setText("选择日期");
        tvFosterhomeLeavetime.setTextColor(getResources().getColor(R.color.aBBBBBB));
        tvFosterhomeIntime.setText("选择日期");
        tvFosterhomeIntime.setTextColor(getResources().getColor(R.color.aBBBBBB));
        ctlFosterhome.setVisibility(View.VISIBLE);
        ctlFosterhome.setGradient(true);
        ctlFosterhome.setmTextSelectsize(ctlFosterhome.sp2px(16));
        ctlFosterhomeTop.setGradient(true);
        ctlFosterhomeTop.setmTextSelectsize(ctlFosterhome.sp2px(16));
        colors[0] = getResources().getColor(R.color.acceb6340);
        colors[1] = getResources().getColor(R.color.aecc5287b);
        ctlFosterhome.setColors(colors);
        ctlFosterhome.setIndicatorTextMiddle(true);
        ctlFosterhomeTop.setColors(colors);
        ctlFosterhomeTop.setIndicatorTextMiddle(true);
        fosterinfoTopallHeight = llFosterinfoTopall.getLayoutParams().height;
        /*if (ScreenUtil.isNavigationBarShow(mContext)) {
            if (ScreenUtil.getBottomBarHeight(mContext) > 0) {
                Utils.mLogError("高度是" + ScreenUtil.getBottomBarHeight(mContext));
                int height = homeBottomRealHeight - ScreenUtil.getBottomBarHeight(mContext);
                slidingLayout.setPanelHeight(height);
            }
        }*/
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        ctlFosterhome.setTabData(mTabEntities);
        ctlFosterhome.setCurrentTab(0);
        ctlFosterhomeTop.setTabData(mTabEntities);
        ctlFosterhomeTop.setCurrentTab(0);

        rvFosterhomeRoom.setHasFixedSize(true);
        rvFosterhomeRoom.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvFosterhomeRoom.setLayoutManager(noScollFullLinearLayoutManager);
        fosterRoomAdapter = new FosterRoomAdapter(R.layout.item_foster_room, roomList);
        rvFosterhomeRoom.setAdapter(fosterRoomAdapter);

        rvFosterhomeImg.setHasFixedSize(true);
        rvFosterhomeImg.setNestedScrollingEnabled(false);
        NoScollSyLinearLayoutManager noScollFullLinearLayoutManager1 = new NoScollSyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        noScollFullLinearLayoutManager1.setScrollEnabled(false);
        rvFosterhomeImg.setLayoutManager(noScollFullLinearLayoutManager1);
        commodityDetailImgAdapter = new CommodityDetailImgAdapter(R.layout.item_commodity_detailimg, imgList, this);
        rvFosterhomeImg.setAdapter(commodityDetailImgAdapter);
    }

    private void setListener() {
        tvFosterShopdesc.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                getTopHeight();
            }
        });
        bannerFosterHome.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (bannerList.size() > position) {
                    Utils.goService(mContext, bannerList.get(position).point, bannerList.get(position).backup);
                }
            }
        });
        fosterRoomAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (roomList.size() > 0 && roomList.size() > position) {
                    FosterRoom fosterRoom = roomList.get(position);
                    switch (view.getId()) {
                        case R.id.rl_item_fosterroom_root:
                            showRoomDetailPop(fosterRoom);
                            break;
                        case R.id.rl_item_fosterroom_submit:
                            if (fosterRoom.getIsFull() == 1) {//满房
                                ToastUtil.showToastShortBottom(mContext, "已满房");
                            } else {
                                if (!Utils.checkLogin(mContext)) {
                                    startActivity(new Intent(mContext, LoginNewActivity.class));
                                    return;
                                }
                                if (customerpetid <= 0) {
                                    ToastUtil.showToastShortBottom(mContext, "请添加宠物");
                                    return;
                                }
                                if (!(Utils.isStrNull(startTime) && Utils.isStrNull(endTime))) {
                                    ToastUtil.showToastShortBottom(mContext, "请选择寄养时间");
                                    return;
                                }
                                Intent intent = new Intent(mContext, AddCardItemActivity.class);
                                intent.putExtra("shopId", hotelCentreId);
                                intent.putExtra("myPetId", customerpetid);
                                intent.putExtra("roomType", fosterRoom.getId());
                                intent.putExtra("startTime", startTime);
                                intent.putExtra("endTime", endTime);
                                intent.putExtra("contact", contact);
                                intent.putExtra("fosterTime", fosterTime);
                                intent.putExtra("contactPhone", contactPhone);
                                intent.putExtra("bathPetSize", fosterRoom.getBathPetSize());
                                startActivity(intent);
                            }
                            break;
                    }
                }
            }
        });
        bannerFosterHome.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ctlFosterhome.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                ctlFosterhome.setmTextSelectsize(ctlFosterhome.sp2px(16));
                ctlFosterhomeTop.setmTextSelectsize(ctlFosterhome.sp2px(16));
                ctlFosterhomeTop.setCurrentTab(position);
                ctlFosterhome.setCurrentTab(position);
                imgList.clear();
                if (position == 0) {
                    imgList.addAll(introduceImgList);
                } else if (position == 1) {
                    imgList.addAll(instructionsImgList);
                }
                commodityDetailImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        ctlFosterhomeTop.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                ctlFosterhome.setmTextSelectsize(ctlFosterhome.sp2px(16));
                ctlFosterhomeTop.setmTextSelectsize(ctlFosterhome.sp2px(16));
                ctlFosterhome.setCurrentTab(position);
                ctlFosterhomeTop.setCurrentTab(position);
                imgList.clear();
                if (position == 0) {
                    imgList.addAll(introduceImgList);
                } else if (position == 1) {
                    imgList.addAll(instructionsImgList);
                }
                commodityDetailImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset == 1) {
                    tvFosterTitle.setVisibility(View.VISIBLE);
                }
                if (slideOffset == 0) {
                    tvFosterTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
        getTopHeight();
        svPetcare.setListener(new ListenYScrollView.Listener() {
            @Override
            public void onScroll(int t) {
                if (fosterinfoTopallHeight <= t) {
                    ctlFosterhome.setVisibility(View.INVISIBLE);
                    ctlFosterhome.setFocusable(false);
                    ctlFosterhomeTop.setVisibility(View.VISIBLE);
                    ivFosterhomeGotop.setVisibility(View.VISIBLE);
                } else {
                    ctlFosterhome.setVisibility(View.VISIBLE);
                    ctlFosterhomeTop.setVisibility(View.GONE);
                    ctlFosterhomeTop.setFocusable(false);
                    ivFosterhomeGotop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getTopHeight() {

        ViewTreeObserver vto = llFosterinfoTopall.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llFosterinfoTopall.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                fosterinfoTopallHeight = llFosterinfoTopall.getHeight() + Utils.dip2px(mContext, 20);
            }
        });
    }

    private void getBanner() {
        mPDialog.showDialog();
        bannerList.clear();
        CommUtil.getFosterListBanner(mContext, 5, 0, cityId, hotelCentreId, spUtil.getInt("isFirstLogin", 0), fosterBannerHandler);
    }

    private AsyncHttpResponseHandler fosterBannerHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("topList") && !jdata.isNull("topList")) {
                            JSONArray jarrbanner = jdata.getJSONArray("topList");
                            if (jarrbanner.length() > 0) {
                                for (int i = 0; i < jarrbanner.length(); i++) {
                                    JSONObject jsonObject1 = jarrbanner.getJSONObject(i);
                                    com.haotang.pet.entity.Banner banner = new com.haotang.pet.entity.Banner();
                                    if (jsonObject1.has("imgUrl") && !jsonObject1.isNull("imgUrl")) {
                                        banner.pic = jsonObject1.getString("imgUrl");
                                    }
                                    if (jsonObject1.has("point") && !jsonObject1.isNull("point")) {
                                        banner.point = jsonObject1.getInt("point");
                                    }
                                    if (jsonObject1.has("backup") && !jsonObject1.isNull("backup")) {
                                        banner.backup = jsonObject1.getString("backup");
                                    }
                                    bannerList.add(banner);
                                }
                            }
                            if (bannerList.size() > 0) {
                                bannerImgList.clear();
                                for (int i = 0; i < bannerList.size(); i++) {
                                    bannerImgList.add(bannerList.get(i).pic);
                                }
                                llBannerIndicator.setVisibility(View.VISIBLE);
                                bannerFosterHome.setVisibility(View.VISIBLE);
                                bannerFosterHome.setImages(bannerImgList)
                                        .setImageLoader(new GlideImageLoader())
                                        .start();
                                createPoint(0);
                            } else {
                                llBannerIndicator.setVisibility(View.GONE);
                                bannerFosterHome.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                Log.e("TAG", "寄养bannere = " + e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void getData() {
        mPDialog.showDialog();
        imgList.clear();
        instructionsImgList.clear();
        introduceImgList.clear();
        CommUtil.getReadyReserve(mContext, fosterHomeHandler);
    }

    private AsyncHttpResponseHandler fosterHomeHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.dimissDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("centers") && !jdata.isNull("centers")) {
                            JSONArray jatrrCenters = jdata.getJSONArray("centers");
                            if (jatrrCenters.length() > 0) {
                                JSONObject jcenters = jatrrCenters.getJSONObject(0);
                                if (jcenters.has("id") && !jcenters.isNull("id")) {
                                    hotelCentreId = jcenters.getInt("id");
                                }
                                if (jcenters.has("cityId") && !jcenters.isNull("cityId")) {
                                    cityId = jcenters.getInt("cityId");
                                }
                                if (jcenters.has("star") && !jcenters.isNull("star")) {
                                    Utils.setText(tvFosterhomeStar, jcenters.getDouble("star") + "", "", View.VISIBLE, View.VISIBLE);
                                }
                                if (jcenters.has("orderAmount") && !jcenters.isNull("orderAmount")) {
                                    Utils.setText(tvFosterhomeOrdernum, jcenters.getInt("orderAmount") + "参与过寄养", "", View.VISIBLE, View.VISIBLE);
                                }
                                if (jcenters.has("hotelName") && !jcenters.isNull("hotelName")) {
                                    hotelName = jcenters.getString("hotelName");
                                    Utils.setText(tvFosterhomeHotelname, hotelName, "", View.VISIBLE, View.VISIBLE);
                                    Utils.setText(tvFosterTitle, hotelName, "", View.GONE, View.GONE);
                                }
                                if (jcenters.has("description") && !jcenters.isNull("description")) {
                                    tvFosterShopdesc.setText(jcenters.getString("description"));
                                }
                                if (jcenters.has("address") && !jcenters.isNull("address")) {
                                    hotelAddress = jcenters.getString("address");
                                    Utils.setText(tvFosterShoploc, hotelAddress, "", View.VISIBLE, View.VISIBLE);
                                }
                                if (jcenters.has("operTime") && !jcenters.isNull("operTime")) {
                                    Utils.setText(tvFosterOpentime, "营业时间：" + jcenters.getString("operTime"), "", View.VISIBLE, View.VISIBLE);
                                }
                                if (jcenters.has("phone") && !jcenters.isNull("phone")) {
                                    servicePhone = jcenters.getString("phone");
                                }
                                if (jcenters.has("lng") && !jcenters.isNull("lng")) {
                                    lng = jcenters.getDouble("lng");
                                }
                                if (jcenters.has("lat") && !jcenters.isNull("lat")) {
                                    lat = jcenters.getDouble("lat");
                                }
                                if (jcenters.has("shopDetailImg") && !jcenters.isNull("shopDetailImg")) {
                                    JSONArray shopDetailImg = jcenters.getJSONArray("shopDetailImg");
                                    for (int i = 0; i < shopDetailImg.length(); i++) {
                                        introduceImgList.add(shopDetailImg.getString(i));
                                    }
                                }
                                if (jcenters.has("hotelTipImgs") && !jcenters.isNull("hotelTipImgs")) {
                                    JSONArray hotelTipImgs = jcenters.getJSONArray("hotelTipImgs");
                                    for (int i = 0; i < hotelTipImgs.length(); i++) {
                                        instructionsImgList.add(hotelTipImgs.getString(i));
                                    }
                                }
                                if (jcenters.has("share") && !jcenters.isNull("share")) {
                                    JSONObject share = jcenters.getJSONObject("share");
                                    if (share.has("img") && !share.isNull("img")) {
                                        shareImg = share.getString("img");
                                    }
                                    if (share.has("title") && !share.isNull("title")) {
                                        shareTitle = share.getString("title");
                                    }
                                    if (share.has("url") && !share.isNull("url")) {
                                        shareUrl = share.getString("url");
                                    }
                                    if (share.has("desc") && !share.isNull("desc")) {
                                        shareDesc = share.getString("desc");
                                    }
                                }
                            }
                        }
                        if (jdata.has("pet") && !jdata.isNull("pet")) {
                            JSONObject petJobj = jdata.getJSONObject("pet");
                            if (petJobj.has("id") && !petJobj.isNull("id")) {
                                customerpetid = petJobj.getInt("id");
                            }
                            if (petJobj.has("petId") && !petJobj.isNull("petId")) {
                                petId = petJobj.getInt("petId");
                            }
                            if (petJobj.has("nickName") && !petJobj.isNull("nickName")) {
                                Utils.setText(tvFosterhomeAddpet, petJobj.getString("nickName"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (petJobj.has("avatar") && !petJobj.isNull("avatar")) {
                                GlideUtil.loadImg(mContext, petJobj.getString("avatar"), ivFosterhomeAddpet, R.drawable.icon_production_default);
                            }
                        }
                        if (jdata.has("customerPhone") && !jdata.isNull("customerPhone")) {
                            contactPhone = jdata.getString("customerPhone");
                        }
                        if (jdata.has("customerName") && !jdata.isNull("customerName")) {
                            contact = jdata.getString("customerName");
                        }
                        if (jdata.has("startTime") && !jdata.isNull("startTime")) {
                            fosterTime = jdata.getString("startTime");
                        }
                        getBanner();
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                Log.e("TAG", "寄养首页e = " + e.toString());
            }
            Utils.setText(tvFosterhomeIntip, "入住 " + fosterTime, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvFosterhomeLeavetip, "离店 " + fosterTime, "", View.VISIBLE, View.VISIBLE);
            if (introduceImgList.size() > 0) {
                imgList.addAll(introduceImgList);
            }
            commodityDetailImgAdapter.notifyDataSetChanged();
            getTopHeight();
            getRoomList();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void getRoomList() {
        mPDialog.showDialog();
        roomList.clear();
        if (Utils.isStrNull(startTime) && Utils.isStrNull(endTime)) {
            CommUtil.getRoomType(mContext, hotelCentreId, petId, startTime, endTime, roomListHandler);
        } else {
            CommUtil.getRoomType(mContext, hotelCentreId, petId, null, null, roomListHandler);
        }
    }

    private AsyncHttpResponseHandler roomListHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("roomType") && !jdata.isNull("roomType")) {
                            JSONArray jatrrroomType = jdata.getJSONArray("roomType");
                            if (jatrrroomType.length() > 0) {
                                for (int i = 0; i < jatrrroomType.length(); i++) {
                                    roomList.add(FosterRoom.jsonToEntity(jatrrroomType.getJSONObject(i)));
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                Log.e("TAG", "房型e = " + e.toString());
            }
            fosterRoomAdapter.notifyDataSetChanged();
            getTopHeight();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void createPoint(int postion) {
        llBannerIndicator.removeAllViews();
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView imageView = new ImageView(this);
            if (postion == i) {
                imageView.setBackgroundResource(R.drawable.bg_banner_indicator_red);
            } else {
                imageView.setBackgroundResource(R.drawable.bg_banner_indicator_gray);
            }
            llBannerIndicator.addView(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.width = 25;
            params.height = 25;
            params.leftMargin = 12;
            imageView.setLayoutParams(params);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.ll_fosterhome_call, R.id.ll_fosterhome_navigation, R.id.ll_fosterhome_addpet, R.id.ll_fosterhome_intip, R.id.ll_fosterhome_leavetip, R.id.iv_fosterhome_addpet, R.id.iv_foster_back, R.id.iv_fosterhome_share, R.id.iv_fosterhome_gotop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_fosterhome_call:
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.telePhoneBroadcast(mContext, servicePhone);
                    }
                }).show();
                break;
            case R.id.ll_fosterhome_navigation:
                Intent navIntent = new Intent(mContext, FosterNavigationActivity.class);
                navIntent.putExtra("lat", lat);
                navIntent.putExtra("lng", lng);
                navIntent.putExtra("address", hotelAddress);
                navIntent.putExtra("name", hotelName);
                startActivity(navIntent);
                break;
            case R.id.ll_fosterhome_addpet:
                if (Utils.checkLogin(mContext)) {
                    Intent intent = new Intent(mContext, ChooseFosterPetActivity.class);
                    intent.putExtra("myPetId", customerpetid);
                    intent.putExtra("shopId", hotelCentreId);
                    startActivityForResult(intent, Global.FOSTERCARE_APPOINTMENT_CHANGEPET);
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.ll_fosterhome_intip:
                if (Utils.checkLogin(mContext)) {
                    if (petId <= 0) {
                        ToastUtil.showToastShortBottom(mContext, "请先添加宠物");
                    } else {
                        getFosterDate();
                    }
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.ll_fosterhome_leavetip:
                if (Utils.checkLogin(mContext)) {
                    if (petId <= 0) {
                        ToastUtil.showToastShortBottom(mContext, "请先添加宠物");
                    } else {
                        getFosterDate();
                    }
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.iv_fosterhome_addpet:
                if (Utils.checkLogin(mContext)) {
                    Intent intent = new Intent(mContext, ChooseFosterPetActivity.class);
                    intent.putExtra("myPetId", customerpetid);
                    intent.putExtra("shopId", hotelCentreId);
                    startActivityForResult(intent, Global.FOSTERCARE_APPOINTMENT_CHANGEPET);
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.iv_foster_back:
                finish();
                break;
            case R.id.iv_fosterhome_share:
                Utils.share(mContext, shareImg, shareTitle, shareDesc, shareUrl, "1,2", 0,null);
                break;
            case R.id.iv_fosterhome_gotop:
                svPetcare.smoothScrollTo(0, 0);
                break;
        }
    }

    //获取寄养时间格子
    private void getFosterDate() {
        dateList.clear();
        localDateList.clear();
        dayList.clear();
        minDay = 0;
        festivalsDay = 0;
        festivalsStartPosition = 0;
        festivalsEndPosition = 0;
        festivalsTip = "";
        festivalsStartDate = "";
        festivalsEndDate = "";
        isHaveFestivals = false;
        mPDialog.showDialog();
        CommUtil.getTaskSchedule(this, petId, hotelCentreId, dateHandler);
    }

    private AsyncHttpResponseHandler dateHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("selection") && !jdata.isNull("selection")) {
                            JSONArray jatrrselection = jdata.getJSONArray("selection");
                            if (jatrrselection.length() > 0) {
                                for (int i = 0; i < jatrrselection.length(); i++) {
                                    localDateList.add(FosterDate.jsonToEntity(jatrrselection.getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("festival") && !jdata.isNull("festival")) {
                            isHaveFestivals = true;
                            JSONObject jfestivals = jdata.getJSONObject("festival");
                            if (jfestivals.has("days") && !jfestivals.isNull("days")) {
                                festivalsDay = jfestivals.getInt("days");
                            }
                            if (jfestivals.has("tip") && !jfestivals.isNull("tip")) {
                                festivalsTip = jfestivals.getString("tip");
                            }
                            if (jfestivals.has("startDate") && !jfestivals.isNull("startDate")) {
                                festivalsStartDate = jfestivals.getString("startDate");
                            }
                            if (jfestivals.has("endDate") && !jfestivals.isNull("endDate")) {
                                festivalsEndDate = jfestivals.getString("endDate");
                            }
                        }
                        if (jdata.has("day") && !jdata.isNull("day")) {
                            minDay = jdata.getInt("day");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            if (localDateList.size() > 0) {
                for (int i = 0; i < localDateList.size(); i++) {
                    FosterDate tempFosterDate = new FosterDate();
                    tempFosterDate.setMonth(localDateList.get(i).getMonth());
                    List<FosterDay> days = localDateList.get(i).getDays();
                    List<FosterDay> tempDays = new ArrayList<FosterDay>();
                    if (days != null && days.size() > 0) {
                        int day_of_week = Utils.dayofweek(days.get(0).getDate()); // 获取当前这天的星期
                        // 获取第一天的前面空了几个星期
                        int offset = day_of_week;
                        for (int j = 0; j < offset; j++) {
                            tempDays.add(new FosterDay(""));
                        }
                        tempDays.addAll(days);
                        tempFosterDate.setDays(tempDays);
                    }
                    dateList.add(tempFosterDate);
                }
                boolean isStartTimeAvail = false;
                boolean isEndTimeAvail = false;
                boolean isMiddleTimeAvail = false;
                //判断之前选中的时间是否还可选
                for (int i = 0; i < dateList.size(); i++) {
                    List<FosterDay> days = dateList.get(i).getDays();
                    if (days != null && days.size() > 0) {
                        for (int j = 0; j < days.size(); j++) {
                            if (Utils.isStrNull(startTime) && days.get(j).getDate().equals(startTime)) {
                                if (days.get(j).getIsFull() == 0) {//可约
                                    isStartTimeAvail = true;
                                }
                            }
                            if (Utils.isStrNull(endTime) && days.get(j).getDate().equals(endTime)) {
                                //离店时间满房也可约可约
                                isEndTimeAvail = true;
                            }
                            if (Utils.isStrNull(startTime) && Utils.isStrNull(endTime) && Utils.isStrNull(days.get(j).getDate())) {
                                if (Utils.isEffectiveDate(Utils.getFormatDate(days.get(j).getDate()),
                                        Utils.getFormatDate(startTime),
                                        Utils.getFormatDate(endTime))) {
                                    if (days.get(j).getIsFull() == 0) {//可约
                                        isMiddleTimeAvail = true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (Utils.isStrNull(startTime) && Utils.isStrNull(endTime)) {
                    if (isStartTimeAvail && isEndTimeAvail && isMiddleTimeAvail) {
                        for (int i = 0; i < dateList.size(); i++) {
                            List<FosterDay> days = dateList.get(i).getDays();
                            if (days != null && days.size() > 0) {
                                for (int j = 0; j < days.size(); j++) {
                                    //选中之前选中的入住时间
                                    if (Utils.isStrNull(startTime) && days.get(j).getDate().equals(startTime)) {
                                        if (days.get(j).getIsFull() == 0) {//可约
                                            days.get(j).setStart(true);
                                        }
                                    }
                                    //选中之前选中的离店时间
                                    if (Utils.isStrNull(endTime) && days.get(j).getDate().equals(endTime)) {
                                        //离店时间满房也可约可约
                                        days.get(j).setEnd(true);
                                    }
                                    //选中入住时间和离店时间之间的时间
                                    if (Utils.isStrNull(startTime) && Utils.isStrNull(endTime) && Utils.isStrNull(days.get(j).getDate())) {
                                        if (Utils.isEffectiveDate(Utils.getFormatDate(days.get(j).getDate()),
                                                Utils.getFormatDate(startTime),
                                                Utils.getFormatDate(endTime))) {
                                            if (days.get(j).getIsFull() == 0) {//可约
                                                days.get(j).setMiddle(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        startTime = "";
                        endTime = "";
                        tvFosterhomeIntime.setText("选择日期");
                        tvFosterhomeLeavetime.setText("选择日期");
                        tvFosterhomeIntime.setTextColor(getResources().getColor(R.color.aBBBBBB));
                        tvFosterhomeLeavetime.setTextColor(getResources().getColor(R.color.aBBBBBB));
                    }
                } else if (Utils.isStrNull(startTime) && !Utils.isStrNull(endTime)) {
                    if (isStartTimeAvail) {
                        for (int i = 0; i < dateList.size(); i++) {
                            List<FosterDay> days = dateList.get(i).getDays();
                            if (days != null && days.size() > 0) {
                                for (int j = 0; j < days.size(); j++) {
                                    //选中之前选中的入住时间
                                    if (Utils.isStrNull(startTime) && days.get(j).getDate().equals(startTime)) {
                                        if (days.get(j).getIsFull() == 0) {//可约
                                            days.get(j).setStart(true);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        startTime = "";
                        endTime = "";
                        tvFosterhomeIntime.setText("选择日期");
                        tvFosterhomeLeavetime.setText("选择日期");
                        tvFosterhomeIntime.setTextColor(getResources().getColor(R.color.aBBBBBB));
                        tvFosterhomeLeavetime.setTextColor(getResources().getColor(R.color.aBBBBBB));
                    }
                }
                for (int i = 0; i < dateList.size(); i++) {
                    List<FosterDay> days = dateList.get(i).getDays();
                    if (days != null && days.size() > 0) {
                        dayList.addAll(days);
                    }
                }
                Log.e("TAG", "dayList.size() = " + dayList.size());
                if (isHaveFestivals) {
                    for (int i = 0; i < dayList.size(); i++) {
                        if (dayList.get(i).getDate().equals(festivalsStartDate)) {
                            festivalsStartPosition = i;
                        } else if (dayList.get(i).getDate().equals(festivalsEndDate)) {
                            festivalsEndPosition = i;
                        }
                    }
                    Log.e("TAG", "festivalsStartPosition = " + festivalsStartPosition);
                    Log.e("TAG", "festivalsEndPosition = " + festivalsEndPosition);
                }
                boolean isSelectEnd = false;
                for (int i = 0; i < dateList.size(); i++) {
                    List<FosterDay> days1 = dateList.get(i).getDays();
                    if (days1 != null && days1.size() > 0) {
                        for (int j = 0; j < days1.size(); j++) {
                            if (days1.get(j).isEnd()) {
                                isSelectEnd = true;
                            }
                        }
                    }
                }
                for (int i = 0; i < dateList.size(); i++) {
                    List<FosterDay> days1 = dateList.get(i).getDays();
                    if (days1 != null && days1.size() > 0) {
                        for (int j = 0; j < days1.size(); j++) {
                            days1.get(j).setSelectEnd(isSelectEnd);
                        }
                    }
                }
                showFosterDatePop();
            } else {
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void setPop(View view, String popStr) {
        QMUIPopup mNormalPopup = new QMUIPopup(mContext, QMUIPopup.DIRECTION_NONE);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(mNormalPopup.generateLayoutParam(WRAP_CONTENT,
                WRAP_CONTENT
        ));
        textView.setTextSize(12);
        textView.setText(popStr);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        mNormalPopup.setContentView(textView);
        mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
        mNormalPopup.show(view);
    }

    private void showFosterDatePop() {
        popFlag = 1;
        mPDialog.closeDialog();
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.pop_foster_date, null);
        final RecyclerView rv_popfosterdate = customView.findViewById(R.id.rv_popfosterdate);
        Button btn_popfosterdate_sure = customView.findViewById(R.id.btn_popfosterdate_sure);
        RelativeLayout rl_popfosterdate = customView.findViewById(R.id.rl_popfosterdate);
        RelativeLayout ll_popfosterdate = customView.findViewById(R.id.ll_popfosterdate);
        for (int i = 0; i < dateList.size(); i++) {
            dateList.get(i).setHeaderId(i);
        }
        rv_popfosterdate.setHasFixedSize(true);
        rv_popfosterdate.setLayoutManager(new LinearLayoutManager(this));
        final FosterDateAdapter fosterDateAdapter = new FosterDateAdapter(R.layout.item_fosterdate, dateList);
        rv_popfosterdate.setAdapter(fosterDateAdapter);
        rv_popfosterdate.addItemDecoration(new StickyRecyclerHeadersDecoration(fosterDateAdapter));
        isGlobal = false;
        //View加载完成时回调
        rv_popfosterdate.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e("TAG", "onGlobalLayout");
                if (!isGlobal) {
                    isGlobal = true;
                    try {
                        if (popFlag == 1) {//默认进来
                            Log.e("TAG", "popFlag = " + popFlag);
                            if (Utils.isStrNull(startTime) && Utils.isStrNull(endTime)) {
                                int monthPosition = -1;
                                int dayPosition = -1;
                                for (int i = 0; i < dateList.size(); i++) {
                                    List<FosterDay> days = dateList.get(i).getDays();
                                    if (days != null && days.size() > 0) {
                                        for (int j = 0; j < days.size(); j++) {
                                            if (Utils.isStrNull(endTime) && days.get(j).getDate().equals(endTime)) {
                                                //离店时间满房也可约可约
                                                monthPosition = i;
                                                dayPosition = j;
                                                break;
                                            }
                                        }
                                    }
                                    if (monthPosition >= 0 && dayPosition >= 0) {
                                        break;
                                    }
                                }
                                if (monthPosition >= 0 && dayPosition >= 0) {
                                    View monthView = rv_popfosterdate.getLayoutManager().findViewByPosition(monthPosition);
                                    Log.e("TAG", "monthView = " + monthView);
                                    RecyclerView rv_item_fosterdate_day = monthView.findViewById(R.id.rv_item_fosterdate_day);
                                    View dayView = rv_item_fosterdate_day.getLayoutManager().findViewByPosition(dayPosition);
                                    Log.e("TAG", "dayView = " + dayView);
                                    int selectDays = (int) ((getTimeInMills(endTime) - getTimeInMills(startTime)) / DAYTIMEINMILLS);
                                    Log.e("TAG", "共" + selectDays + "天");
                                    setPop(dayView, "共" + selectDays + "天");
                                }
                            } else if (Utils.isStrNull(startTime) && !Utils.isStrNull(endTime)) {
                                int monthPosition = -1;
                                int dayPosition = -1;
                                for (int i = 0; i < dateList.size(); i++) {
                                    List<FosterDay> days = dateList.get(i).getDays();
                                    if (days != null && days.size() > 0) {
                                        for (int j = 0; j < days.size(); j++) {
                                            if (Utils.isStrNull(startTime) && days.get(j).getDate().equals(startTime)) {
                                                if (days.get(j).getIsFull() == 0) {//可约
                                                    monthPosition = i;
                                                    dayPosition = j;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (monthPosition >= 0 && dayPosition >= 0) {
                                        break;
                                    }
                                }
                                if (monthPosition >= 0 && dayPosition >= 0) {
                                    Log.e("TAG", "请选择离店日期");
                                    View monthView = rv_popfosterdate.getLayoutManager().findViewByPosition(monthPosition);
                                    Log.e("TAG", "monthView = " + monthView);
                                    RecyclerView rv_item_fosterdate_day = monthView.findViewById(R.id.rv_item_fosterdate_day);
                                    View dayView = rv_item_fosterdate_day.getLayoutManager().findViewByPosition(dayPosition);
                                    Log.e("TAG", "dayView = " + dayView);
                                    setPop(dayView, "请选择离店日期");
                                }
                            }
                        } else if (popFlag == 2) {//选中入住时间
                            Log.e("TAG", "popFlag = " + popFlag);
                            Log.e("TAG", "popMonthPosition = " + popMonthPosition);
                            Log.e("TAG", "popDayPosition = " + popDayPosition);
                            if (popMonthPosition >= 0 && popDayPosition >= 0) {
                                Log.e("TAG", "请选择离店日期");
                                View monthView = rv_popfosterdate.getLayoutManager().findViewByPosition(popMonthPosition);
                                Log.e("TAG", "monthView = " + monthView);
                                RecyclerView rv_item_fosterdate_day = monthView.findViewById(R.id.rv_item_fosterdate_day);
                                View dayView = rv_item_fosterdate_day.getLayoutManager().findViewByPosition(popDayPosition);
                                Log.e("TAG", "dayView = " + dayView);
                                setPop(dayView, "请选择离店日期");
                            }
                        } else if (popFlag == 3) {//选中离店时间
                            Log.e("TAG", "popFlag = " + popFlag);
                            Log.e("TAG", "popMonthPosition = " + popMonthPosition);
                            Log.e("TAG", "popDayPosition = " + popDayPosition);
                            Log.e("TAG", "popSelectDay = " + popSelectDay);
                            if (popMonthPosition >= 0 && popDayPosition >= 0) {
                                Log.e("TAG", "请选择离店日期");
                                View monthView = rv_popfosterdate.getLayoutManager().findViewByPosition(popMonthPosition);
                                Log.e("TAG", "monthView = " + monthView);
                                RecyclerView rv_item_fosterdate_day = monthView.findViewById(R.id.rv_item_fosterdate_day);
                                View dayView = rv_item_fosterdate_day.getLayoutManager().findViewByPosition(popDayPosition);
                                Log.e("TAG", "dayView = " + dayView);
                                setPop(dayView, "共" + popSelectDay + "天");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //异常说明选择的入住时间和离店时间格子不在可视范围内
                        Log.e("TAG", "弹框异常e = " + e.toString());
                    }
                }
            }
        });
        fosterDateAdapter.setListener(new FosterDateAdapter.onDateSelectListener() {
            @Override
            public void onSelect(int dayPosition, int monthPosition) {
                Log.e("TAG", "dayPosition = " + dayPosition);
                Log.e("TAG", "monthPosition = " + monthPosition);
                FosterDate fosterDate = dateList.get(monthPosition);
                List<FosterDay> days = fosterDate.getDays();
                FosterDay fosterDay = days.get(dayPosition);
                //判断点击的是否是空白
                if (Utils.isStrNull(fosterDay.getDate())) {
                    popMonthPosition = monthPosition;
                    popDayPosition = dayPosition;
                    // 判断入住日期和离店日期是否选中
                    boolean isSelectStartTime = false;
                    boolean isSelectEndTime = false;
                    String selectStartTime = "";
                    for (int i = 0; i < dateList.size(); i++) {
                        List<FosterDay> days1 = dateList.get(i).getDays();
                        if (days1 != null && days1.size() > 0) {
                            for (int j = 0; j < days1.size(); j++) {
                                if (days1.get(j).isStart()) {
                                    isSelectStartTime = true;
                                    selectStartTime = days1.get(j).getDate();
                                } else if (days1.get(j).isEnd()) {
                                    isSelectEndTime = true;
                                }
                            }
                        }
                    }
                    if (isSelectStartTime && isSelectEndTime) {//入住日期和离店日期都已选中
                        //判断入住日期是否可约
                        if (fosterDay.getIsFull() == 0) {
                            //全部重置,然后选中入住日期
                            for (int i = 0; i < dateList.size(); i++) {
                                List<FosterDay> days1 = dateList.get(i).getDays();
                                if (days1 != null && days1.size() > 0) {
                                    for (int j = 0; j < days1.size(); j++) {
                                        days1.get(j).setStart(false);
                                        days1.get(j).setEnd(false);
                                        days1.get(j).setMiddle(false);
                                    }
                                }
                            }
                            //选中入住日期
                            fosterDay.setStart(true);
                            isGlobal = false;
                            popFlag = 2;
                        }
                    } else if (!isSelectStartTime && !isSelectEndTime) {//入住日期和离店日期都未选中
                        //判断入住日期是否可约
                        if (fosterDay.getIsFull() == 0) {
                            //选中入住日期
                            fosterDay.setStart(true);
                            isGlobal = false;
                            popFlag = 2;
                        }
                    } else if (isSelectStartTime && !isSelectEndTime) {//入住日期已选中离店日期未选中
                        //离店日期当天如果满房，也可约
                        int selectStartTimePosition = -1;
                        int selectEndTimePosition = -1;
                        Log.e("TAG", "selectStartTime = " + selectStartTime);
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).getDate().equals(selectStartTime)) {
                                selectStartTimePosition = i;
                            } else if (dayList.get(i).getDate().equals(fosterDay.getDate())) {
                                selectEndTimePosition = i;
                            }
                        }
                        Log.e("TAG", "selectStartTimePosition = " + selectStartTimePosition);
                        Log.e("TAG", "selectEndTimePosition = " + selectEndTimePosition);
                        //判断选择的离店日期是否小于等于入住日期
                        if (selectEndTimePosition <= selectStartTimePosition) {
                            //全部重置,然后选中入住日期
                            //判断入住日期是否可约
                            if (fosterDay.getIsFull() == 0) {
                                for (int i = 0; i < dateList.size(); i++) {
                                    List<FosterDay> days1 = dateList.get(i).getDays();
                                    if (days1 != null && days1.size() > 0) {
                                        for (int j = 0; j < days1.size(); j++) {
                                            days1.get(j).setStart(false);
                                            days1.get(j).setEnd(false);
                                            days1.get(j).setMiddle(false);
                                        }
                                    }
                                }
                                //选中入住日期
                                fosterDay.setStart(true);
                                isGlobal = false;
                                popFlag = 2;
                            }
                        } else {
                            //再判断选中的日期是否大于等于起约天数
                            int selectDays = (int) ((getTimeInMills(fosterDay.getDate()) - getTimeInMills(selectStartTime)) / DAYTIMEINMILLS);
                            popSelectDay = selectDays;
                            Log.e("TAG", "selectDays = " + selectDays);
                            if (selectDays >= minDay) {
                                boolean isAvail = true;
                                for (int i = selectStartTimePosition + 1; i < selectEndTimePosition; i++) {
                                    if (dayList.get(i).getIsFull() == 1) {
                                        isAvail = false;
                                        break;
                                    }
                                }
                                //再判断入住日期和离店日期之间是否有不可约的日期
                                if (isAvail) {
                                    //再判断节日期间起约天数限制
                                    if (isHaveFestivals) {
                                        if (selectDays < festivalsDay) {
                                            //入住时间和离店时间只要有一个在限制时间段内就说明不满足
                                            if ((selectStartTimePosition >= festivalsStartPosition && selectStartTimePosition <= festivalsEndPosition) ||
                                                    (selectEndTimePosition >= festivalsStartPosition && selectEndTimePosition <= festivalsEndPosition)) {
                                                ToastUtil.showToastShortCenter(mContext, festivalsTip);
                                                return;
                                            } else {
                                                //选中离店日期
                                                fosterDay.setEnd(true);
                                                //再选中中间日期
                                                for (int i = 0; i < dayList.size(); i++) {
                                                    dayList.get(i).setMiddleTemp(false);
                                                }
                                                for (int i = 0; i < dayList.size(); i++) {
                                                    if (i > selectStartTimePosition && i < selectEndTimePosition) {
                                                        dayList.get(i).setMiddleTemp(true);
                                                    }
                                                }
                                                for (int i = 0; i < dayList.size(); i++) {
                                                    for (int j = 0; j < dateList.size(); j++) {
                                                        List<FosterDay> days1 = dateList.get(j).getDays();
                                                        if (days1 != null && days1.size() > 0) {
                                                            for (int k = 0; k < days1.size(); k++) {
                                                                if (dayList.get(i).isMiddleTemp() && dayList.get(i).getDate().equals(days1.get(k).getDate())) {
                                                                    days1.get(k).setMiddle(true);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            isGlobal = false;
                                            popFlag = 3;
                                        } else {
                                            //选中离店日期
                                            fosterDay.setEnd(true);
                                            //再选中中间日期
                                            for (int i = 0; i < dayList.size(); i++) {
                                                dayList.get(i).setMiddleTemp(false);
                                            }
                                            for (int i = 0; i < dayList.size(); i++) {
                                                if (i > selectStartTimePosition && i < selectEndTimePosition) {
                                                    dayList.get(i).setMiddleTemp(true);
                                                }
                                            }
                                            for (int i = 0; i < dayList.size(); i++) {
                                                for (int j = 0; j < dateList.size(); j++) {
                                                    List<FosterDay> days1 = dateList.get(j).getDays();
                                                    if (days1 != null && days1.size() > 0) {
                                                        for (int k = 0; k < days1.size(); k++) {
                                                            if (dayList.get(i).isMiddleTemp() && dayList.get(i).getDate().equals(days1.get(k).getDate())) {
                                                                days1.get(k).setMiddle(true);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        isGlobal = false;
                                        popFlag = 3;
                                    } else {
                                        //选中离店日期
                                        fosterDay.setEnd(true);
                                        //再选中中间日期
                                        for (int i = 0; i < dayList.size(); i++) {
                                            dayList.get(i).setMiddleTemp(false);
                                        }
                                        for (int i = 0; i < dayList.size(); i++) {
                                            if (i > selectStartTimePosition && i < selectEndTimePosition) {
                                                dayList.get(i).setMiddleTemp(true);
                                            }
                                        }
                                        for (int i = 0; i < dayList.size(); i++) {
                                            for (int j = 0; j < dateList.size(); j++) {
                                                List<FosterDay> days1 = dateList.get(j).getDays();
                                                if (days1 != null && days1.size() > 0) {
                                                    for (int k = 0; k < days1.size(); k++) {
                                                        if (dayList.get(i).isMiddleTemp() && dayList.get(i).getDate().equals(days1.get(k).getDate())) {
                                                            days1.get(k).setMiddle(true);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    isGlobal = false;
                                    popFlag = 3;
                                } else {
                                    //清掉入住日期
                                    //全部重置
                                    for (int i = 0; i < dateList.size(); i++) {
                                        List<FosterDay> days1 = dateList.get(i).getDays();
                                        if (days1 != null && days1.size() > 0) {
                                            for (int j = 0; j < days1.size(); j++) {
                                                days1.get(j).setStart(false);
                                                days1.get(j).setEnd(false);
                                                days1.get(j).setMiddle(false);
                                            }
                                        }
                                    }
                                    new AlertDialogDefault(mContext).builder()
                                            .setTitle("提示").setMsg("您选择的日期有约满状态，请重新选择离店日期").isOneBtn(true).setPositiveButton("我知道了", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                                }
                            } else {
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("提示").setMsg("最少" + minDay + "天起约").isOneBtn(true).setPositiveButton("我知道了", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                            }
                        }
                    }
                    boolean isSelectEnd = false;
                    for (int i = 0; i < dateList.size(); i++) {
                        List<FosterDay> days1 = dateList.get(i).getDays();
                        if (days1 != null && days1.size() > 0) {
                            for (int j = 0; j < days1.size(); j++) {
                                if (days1.get(j).isEnd()) {
                                    isSelectEnd = true;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < dateList.size(); i++) {
                        List<FosterDay> days1 = dateList.get(i).getDays();
                        if (days1 != null && days1.size() > 0) {
                            for (int j = 0; j < days1.size(); j++) {
                                days1.get(j).setSelectEnd(isSelectEnd);
                            }
                        }
                    }
                    fosterDateAdapter.notifyDataSetChanged();
                } else {
                    Log.e("TAG", "点击的空白");
                }
            }
        });
        // 定义Dialog布局和参数
        final Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle_FosterDate);
        //设置对话框铺满屏幕
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WRAP_CONTENT;
        win.setAttributes(lp);
        dialog.setContentView(customView);
        // 调整dialog背景大小
        rl_popfosterdate.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.show();
        rl_popfosterdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll_popfosterdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn_popfosterdate_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断入住日期和离店日期是否选中
                String selectStartTime = "";
                String selectEndTime = "";
                for (int i = 0; i < dateList.size(); i++) {
                    List<FosterDay> days = dateList.get(i).getDays();
                    if (days != null && days.size() > 0) {
                        for (int j = 0; j < days.size(); j++) {
                            if (days.get(j).isStart()) {
                                selectStartTime = days.get(j).getDate();
                            } else if (days.get(j).isEnd()) {
                                selectEndTime = days.get(j).getDate();
                            }
                        }
                    }
                }
                if (!Utils.isStrNull(selectStartTime)) {
                    ToastUtil.showToastShortBottom(mContext, "请选择入住日期");
                    return;
                }
                if (!Utils.isStrNull(selectEndTime)) {
                    ToastUtil.showToastShortBottom(mContext, "请选择离店日期");
                    return;
                }
                startTime = selectStartTime;
                endTime = selectEndTime;
                int localdaynum = (int) ((getTimeInMills(endTime) - getTimeInMills(startTime)) / DAYTIMEINMILLS);
                rlFosterhomeNum.setVisibility(View.VISIBLE);
                tvFosterhomeTotalday.setText("共" + localdaynum + "天");
                tvFosterhomeIntime.setText(startTime.split("-")[1] + "月" + startTime.split("-")[2] + "日");
                tvFosterhomeLeavetime.setText(endTime.split("-")[1] + "月" + endTime.split("-")[2] + "日");
                tvFosterhomeIntime.setTextColor(getResources().getColor(R.color.a333333));
                tvFosterhomeLeavetime.setTextColor(getResources().getColor(R.color.a333333));
                dialog.dismiss();
                getRoomList();
            }
        });
    }

    private void showRoomDetailPop(final FosterRoom item) {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.pop_foster_roomdetail, null);
        RelativeLayout rl_pop_fosterroomdetail = customView.findViewById(R.id.rl_pop_fosterroomdetail);
        LinearLayout ll_pop_fosterroomdetail = customView.findViewById(R.id.ll_pop_fosterroomdetail);
        final TextView tv_pop_fosterroomdetail_submit = customView.findViewById(R.id.tv_pop_fosterroomdetail_submit);
        RelativeLayout rl_item_fosterroomdetail_submit = customView.findViewById(R.id.rl_item_fosterroomdetail_submit);
        TextView tv_pop_fosterroomdetail_roomname = customView.findViewById(R.id.tv_pop_fosterroomdetail_roomname);
        TextView tv_pop_fosterroomdetail_roomgg = customView.findViewById(R.id.tv_pop_fosterroomdetail_roomgg);
        TagFlowLayout tfl_pop_fosterroomdetail_tag = customView.findViewById(R.id.tfl_pop_fosterroomdetail_tag);
        TextView tv_pop_fosterroomdetail_price = customView.findViewById(R.id.tv_pop_fosterroomdetail_price);
        TextView tv_pop_fosterroomdetail_desc = customView.findViewById(R.id.tv_pop_fosterroomdetail_desc);
        RecyclerView rv_pop_fosterroomdetail = customView.findViewById(R.id.rv_pop_fosterroomdetail);
        Utils.setText(tv_pop_fosterroomdetail_roomgg, item.getSizeDesc(), "", View.VISIBLE, View.GONE);
        Utils.setText(tv_pop_fosterroomdetail_desc, item.getPriceDesc(), "", View.VISIBLE, View.GONE);
        Utils.setText(tv_pop_fosterroomdetail_price, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_pop_fosterroomdetail_roomname, item.getName(), "", View.VISIBLE, View.VISIBLE);
        if (item.getIsFull() == 1) {
            tv_pop_fosterroomdetail_submit.setText("满房");
            rl_item_fosterroomdetail_submit.setBackgroundResource(R.drawable.bg_grayround_order);
        } else {
            tv_pop_fosterroomdetail_submit.setText("预订");
            rl_item_fosterroomdetail_submit.setBackgroundResource(R.drawable.bg_redround_order);
        }
        if (item.getLabels() != null && item.getLabels().size() > 0) {
            tfl_pop_fosterroomdetail_tag.setVisibility(View.VISIBLE);
            tfl_pop_fosterroomdetail_tag.setAdapter(new TagAdapter<String>(item.getLabels()) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    View view = (View) View.inflate(mContext, R.layout.item_foster_roomtag,
                            null);
                    TextView tv_item_foster_roomtag = (TextView) view.findViewById(R.id.tv_item_foster_roomtag);
                    tv_item_foster_roomtag.setText(s);
                    return view;
                }
            });
        } else {
            tfl_pop_fosterroomdetail_tag.setVisibility(View.GONE);
        }
        if (item.getImg2() != null && item.getImg2().size() > 0) {
            rv_pop_fosterroomdetail.setVisibility(View.VISIBLE);
            rv_pop_fosterroomdetail.setHasFixedSize(true);
            rv_pop_fosterroomdetail.setLayoutManager(new LinearLayoutManager(this));
            rv_pop_fosterroomdetail.setAdapter(new CommodityDetailImgAdapter(R.layout.item_commodity_detailimg, item.getImg2(), this));
        } else {
            rv_pop_fosterroomdetail.setVisibility(View.GONE);
        }
        final PopupWindow pWinBottomDialog = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点 
        // 注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的 
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失 
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击 
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画 
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(mContext)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        rl_pop_fosterroomdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        ll_pop_fosterroomdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        rl_item_fosterroomdetail_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIsFull() == 1) {//满房
                    ToastUtil.showToastShortBottom(mContext, "已满房");
                } else {
                    if (!Utils.checkLogin(mContext)) {
                        startActivity(new Intent(mContext, LoginNewActivity.class));
                        return;
                    }
                    if (customerpetid <= 0) {
                        ToastUtil.showToastShortBottom(mContext, "请添加宠物");
                        return;
                    }
                    if (!(Utils.isStrNull(startTime) && Utils.isStrNull(endTime))) {
                        ToastUtil.showToastShortBottom(mContext, "请选择寄养时间");
                        return;
                    }
                    Intent intent = new Intent(mContext, AddCardItemActivity.class);
                    intent.putExtra("shopId", hotelCentreId);
                    intent.putExtra("myPetId", customerpetid);
                    intent.putExtra("roomType", item.getId());
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("contact", contact);
                    intent.putExtra("fosterTime", fosterTime);
                    intent.putExtra("contactPhone", contactPhone);
                    intent.putExtra("endTime", endTime);
                    intent.putExtra("bathPetSize", item.getBathPetSize());
                    startActivity(intent);
                    pWinBottomDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Global.FOSTERCARE_APPOINTMENT_CHANGEPET:
                    customerpetid = data.getIntExtra("selectMyPetId", 0);
                    petId = data.getIntExtra("selectPetId", 0);
                    Utils.setText(tvFosterhomeAddpet, data.getStringExtra("selectNickName"), "", View.VISIBLE, View.VISIBLE);
                    GlideUtil.loadImg(mContext, data.getStringExtra("selectImg"), ivFosterhomeAddpet, R.drawable.icon_production_default);
                    getRoomList();
                    break;
            }
        }
    }

    private long getTimeInMills(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
