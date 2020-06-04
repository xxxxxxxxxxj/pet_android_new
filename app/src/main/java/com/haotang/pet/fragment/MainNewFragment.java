package com.haotang.pet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.haotang.base.BaseFragment;
import com.haotang.pet.AllShopLocActivity;
import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.NewUserGiveCouponActivity;
import com.haotang.pet.PetCircleOrSelectActivity;
import com.haotang.pet.QrCodeNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.SerchBeauActivity;
import com.haotang.pet.ShopDetailActivity;
import com.haotang.pet.adapter.HomeBeauAdapter;
import com.haotang.pet.adapter.MainFragPetWikiAdapter;
import com.haotang.pet.adapter.MainGoodPostAdapter;
import com.haotang.pet.adapter.MainServiceItemAdapter;
import com.haotang.pet.adapter.MainSmallIconThreeAdapter;
import com.haotang.pet.adapter.MainSmallIconTwoAdapter;
import com.haotang.pet.encyclopedias.activity.EncyclopediasActivity;
import com.haotang.pet.encyclopedias.bean.Encyclopedias;
import com.haotang.pet.entity.ActivityPage;
import com.haotang.pet.entity.AdLoginEvent;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.ExitLoginEvent;
import com.haotang.pet.entity.FloatIngEvent;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.MainService;
import com.haotang.pet.entity.PostSelectionBean;
import com.haotang.pet.entity.ShopLocationEvent;
import com.haotang.pet.entity.event.UpdateGoodPostEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.CountdownUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GlideImageLoader;
import com.haotang.pet.view.GlideImageLoaderFourRound;
import com.haotang.pet.view.MyScrollView;
import com.haotang.pet.view.NiceImageView;
import com.haotang.pet.view.ScrollLineView;
import com.haotang.pet.view.ShadowLayout;
import com.haotang.pet.view.guide.ShopGuide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.baidu.mapsdkvi.VDeviceAPI.getScreenDensity;

/**
 * @author:姜谷蓄
 * @Date:2020/2/17
 * @Description:新版主页
 */
public class MainNewFragment extends BaseFragment implements ShopGuide.ShopGuideInterface {

    @BindView(R.id.banner_mainfragment_top)
    Banner bannerMainfragmentTop;
    @BindView(R.id.ll_banner_indicator)
    LinearLayout llBannerIndicator;
    @BindView(R.id.tv_home_shopname_show)
    TextView tvHomeShopnameShow;
    @BindView(R.id.iv_mainfrag_shopshow)
    ImageView ivMainfragShopshow;
    @BindView(R.id.iv_mainfrag_cardshow)
    ImageView ivMainfragCardshow;
    @BindView(R.id.rv_mainfragment_service)
    RecyclerView rvMainfragmentService;
    @BindView(R.id.iv_mainfragment_newpeople)
    ImageView ivMainfragmentNewpeople;
    @BindView(R.id.rv_mainfragent_smallicons)
    RecyclerView rvMainfragentSmallicons;
    @BindView(R.id.tv_mainfrag_beautytip)
    TextView tvMainfragBeautytip;
    @BindView(R.id.tv_mainfrag_beautynum)
    TextView tvMainfragBeautynum;
    @BindView(R.id.rv_mainfrag_beauty)
    RecyclerView rvMainfragBeauty;
    @BindView(R.id.nv_mainfrag_bottomholder)
    NiceImageView nvMainfragBottomholder;
    @BindView(R.id.nv_mainfrag_bottomholder_shadow)
    ShadowLayout nvMainfragBottomholderShadow;
    Unbinder unbinder;
    @BindView(R.id.banner_mainfragment_bottom)
    Banner bannerMainfragmentBottom;
    @BindView(R.id.rv_mainfrag_petcicler)
    RecyclerView rvMainfragPetcicler;
    @BindView(R.id.tv_mainfrag_morepetcicler)
    TextView tvMainfragMorepetcicler;
    @BindView(R.id.rv_mainfrag_petknow)
    RecyclerView rvMainfragPetknow;
    @BindView(R.id.tv_home_shopname_gone)
    TextView tvHomeShopnameGone;
    @BindView(R.id.ll_mainfrag_locgone)
    LinearLayout llMainfragLocgone;
    @BindView(R.id.ll_mainfrag_loc)
    LinearLayout llMainfragLoc;
    @BindView(R.id.iv_mainfrag_shopgone)
    ImageView ivMainfragShopgone;
    @BindView(R.id.iv_mainfrag_cardgone)
    ImageView ivMainfragCardgone;
    @BindView(R.id.rl_maintop_gone)
    RelativeLayout rlMaintopGone;
    @BindView(R.id.tv_mainfrag_petknow_tip)
    TextView tvMainfragPetknowTip;
    @BindView(R.id.sv_main_home)
    MyScrollView svMainHome;
    @BindView(R.id.nv_mainfrag_centericonone)
    NiceImageView nvCenterIcon;
    @BindView(R.id.tv_mainfrag_middletitle)
    TextView tvMainfragMiddletitle;
    //    @BindView(R.id.tv_mainfrag_middelsubtitle)
//    TextView tvMainfragMiddelsubtitle;
    @BindView(R.id.ll_mainfrag_moreicons)
    LinearLayout llMainfragMoreicons;
    @BindView(R.id.rl_mainfrag_smallicons)
    RelativeLayout rlMainfragSmallicons;
    @BindView(R.id.tv_mainfrag_fostertitle)
    TextView tvMainfragFostertitle;
    @BindView(R.id.ll_mainfrag_center)
    LinearLayout llCenter;
    @BindView(R.id.sl_mainfrag_hotel)
    LinearLayout slMainFragHotel;
    @BindView(R.id.sl_mainfrag_services)
    ShadowLayout slMainFragService;
    @BindView(R.id.tv_mainfrag_fostersubtitle)
    TextView tvMainfragFostersubtitle;
    @BindView(R.id.vn_mainfrag_fostericon)
    NiceImageView vnMainfragFostericon;
    @BindView(R.id.v_mainfrag_line)
    View vMainFragLine;
    @BindView(R.id.fl_mainfrag_scroll)
    FrameLayout flMainScroll;
    @BindView(R.id.tv_mainfrag_allbeauty)
    TextView tvAllBeauty;
    @BindView(R.id.scroll_line_view)
    ScrollLineView scrollLineView;
    @BindView(R.id.head_root)
    RelativeLayout headRoot;

    private double liveLng;
    private double liveLat;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private SharedPreferenceUtil spUtil;
    private Activity mActivity;
    private MProgressDialog pDialog;
    private int isNewComer;
    private int mypetId;
    private int cityId;
    private int shopId = 0;
    private String cityName = "";
    private String shopName = "";
    private String address = "";
    private long timestamp;
    private int page = 1;// 页码
    private int isFollowed = 0;// 只显示自己关注的人的帖子
    private int isExistsVideo = 0;// 只显示存在视频的帖子
    private int isExistsImg = 1;// 只显示存在图片的帖子
    private ArrayList<MainService> listMainService = new ArrayList<MainService>();
    private ArrayList<MainService> listSmallIcons = new ArrayList<MainService>();
    private ArrayList<Beautician> listHotBeautician = new ArrayList<Beautician>();
    private List<Encyclopedias> listPetEncyclopedia = new ArrayList<Encyclopedias>();
    private List<com.haotang.pet.entity.Banner> listTopBanner = new ArrayList<com.haotang.pet.entity.Banner>();
    private List<com.haotang.pet.entity.Banner> listBottomBanner = new ArrayList<com.haotang.pet.entity.Banner>();
    private MainServiceItemAdapter serviceItemAdapter;
    private HomeBeauAdapter serchBeauAdapter;
    private MainFragPetWikiAdapter wikiAdapter;
    private MainGoodPostAdapter postAdapter;
    private MainSmallIconThreeAdapter threeAdapter;
    private MainSmallIconTwoAdapter twoAdapter;
    private ShopGuide shopGuide;
    private String adBackUp;
    private int adPoint;
    private int fosterPoint;
    private String fosterBackup;
    private List<ActivityPage> localBannerList = new ArrayList<ActivityPage>();
    //文件名称
    String fileName_one = "ad_mainfrag_one.txt";
    String fileName_one_day = "ad_mainfrag_one_day.txt";
    private int endX;
    private int endX2;
    private String city;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        initData(context);
        initBD();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragmentnew_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLocation();
        setView();
        setListener();
    }

    //从地址点击店铺返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShopLocationEvent event) {
        if (event != null) {
            shopId = event.getShopId();
            tvHomeShopnameShow.setText(event.getCityName().replace("市", "") + "·" + event.getShopName());
            tvHomeShopnameGone.setText(event.getCityName().replace("市", "") + "·" + event.getShopName());
            ivMainfragShopshow.setVisibility(View.VISIBLE);
            ivMainfragShopgone.setVisibility(View.VISIBLE);
            //scrollview 滑动到顶部
            svMainHome.scrollTo(0, 0);
            //提示门店信息显示
            shopGuide.showGuideView2(getActivity(), ivMainfragShopshow, this);
            getBanner(event.getShopId());
            getHomeData(event.getShopId());
            getPetWiki();
            getGoodPost();
        }
    }


    private void initWindows() {
        Window window = getActivity().getWindow();
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
                .build(getActivity())
                .apply();
        //适配状态栏高度
        ViewGroup.LayoutParams layoutParams = headRoot.getLayoutParams();
        layoutParams.height = ScreenUtil.getStatusBarHeight(getContext())+ScreenUtil.dip2px(getContext(),38);
        headRoot.setLayoutParams(layoutParams);
    }

    private void createPoint(int postion) {
        llBannerIndicator.removeAllViews();
        for (int i = 0; i < listTopBanner.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(R.drawable.bg_banner_indicator_white);
            llBannerIndicator.addView(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.width = postion == i ? 40 : 20;
            params.height = 20;
            params.leftMargin = 12;
            imageView.setLayoutParams(params);
        }
    }

    private void setListener() {
        serviceItemAdapter.setListener(new MainServiceItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listMainService != null && listMainService.size() > 0
                        && listMainService.size() > position) {
                    MainService mainService = listMainService.get(position);
                    if (mainService != null) {
                        Utils.mLogError("到服务：point " + mainService.point + " backup " + mainService.backup);
                        Utils.goService(mActivity, mainService.point, mainService.backup);
                        switch (position) {
                            case 0:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_one_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon1);
                                break;
                            case 1:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_two_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon2);
                                break;
                            case 2:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_thr_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon3);
                                break;
                            case 3:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_four_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon4);
                                break;
                            case 4:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_five_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon5);
                                break;
                            case 5:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_six_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon6);
                                break;
                            case 6:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_seven_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon7);
                                break;
                            case 7:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_eight_main);
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_icon8);
                                break;
                        }
                    }
                }
            }
        });
        threeAdapter.setListener(new MainSmallIconThreeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listSmallIcons != null && listSmallIcons.size() > 0
                        && listSmallIcons.size() > position) {
                    MainService mainService = listSmallIcons.get(position);
                    if (mainService != null) {
                        Utils.goService(mActivity, mainService.point, mainService.backup);
                    }
                    switch (position) {
                        case 0:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_MiddleIcon1);
                            break;
                        case 1:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_MiddleIcon2);
                            break;
                        case 2:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_MiddleIcon3);
                            break;
                        case 3:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_MiddleIcon4);
                            break;
                    }
                }
            }
        });
        serchBeauAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listHotBeautician.size() > 0) {
                    Beautician beautician = listHotBeautician.get(position);
                    Intent intent = new Intent(mActivity, BeauticianDetailActivity.class);
                    intent.putExtra("beautician", beautician);
                    intent.putExtra("id", beautician.id);
                    intent.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
                    startActivity(intent);
                }
            }
        });
        rvMainfragBeauty.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //整体的总宽度，注意是整体，包括在显示区域之外的。
                int range = rvMainfragBeauty.computeHorizontalScrollRange();
                float density = getScreenDensity();
                //计算出溢出部分的宽度，即屏幕外剩下的宽度
                float maxEndX = range + (10 * density) + 5 - rvMainfragBeauty.computeHorizontalScrollExtent();
                //滑动的距离
                endX += dx;
                //计算比例
                float proportion = endX / maxEndX;

                //计算滚动条宽度
                int transMaxRange = ((ViewGroup) vMainFragLine.getParent()).getWidth() - vMainFragLine.getWidth();
                //设置滚动条移动
                vMainFragLine.setTranslationX(transMaxRange * proportion);
                Utils.mLogError("最大移动距离 transMaxRange 美容师" + transMaxRange + " proportion " + proportion);
            }
        });
        rvMainfragentSmallicons.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (listSmallIcons != null && listSmallIcons.size() > 0) {
                    if (spUtil.getBoolean("isShowHomeMore", true)) {
                        llMainfragMoreicons.setVisibility(View.GONE);
                        spUtil.saveBoolean("isShowHomeMore", false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //整体的总宽度，注意是整体，包括在显示区域之外的。
                int range = rvMainfragentSmallicons.computeHorizontalScrollRange();
                float density = getScreenDensity();
                //计算出溢出部分的宽度，即屏幕外剩下的宽度
                float maxEndX = range + (10 * density) + 5 - rvMainfragentSmallicons.computeHorizontalScrollExtent();
                //滑动的距离
                endX2 += dx;
                //计算比例
                float proportion = endX2 / maxEndX;

                //计算滚动条宽度 最大移动距离
                int transMaxRange = scrollLineView.getScrollWidth();
                Utils.mLogError("最大移动距离 transMaxRange " + transMaxRange + " proportion " + proportion);
                //设置滚动条移动
                scrollLineView.getvMainFragLine().setTranslationX(transMaxRange * proportion);
            }
        });
        svMainHome.setListener(new MyScrollView.Listener() {
            @Override
            public void onScroll(int t) {
                if (t > 150) {
                    rlMaintopGone.setVisibility(View.VISIBLE);
                } else {
                    rlMaintopGone.setVisibility(View.GONE);
                }
            }
        });
        bannerMainfragmentTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

    private void setView() {
        initWindows();
        //遮盖层
        shopGuide = new ShopGuide();
        //服务
        serviceItemAdapter = new MainServiceItemAdapter(mActivity);
        GridLayoutManager serviceLayout = new GridLayoutManager(mActivity, 4);
        rvMainfragmentService.setAdapter(serviceItemAdapter);
        rvMainfragmentService.setLayoutManager(serviceLayout);
        //美容师
        serchBeauAdapter = new HomeBeauAdapter(R.layout.item_serch_beau, listHotBeautician);
        rvMainfragBeauty.setAdapter(serchBeauAdapter);
        //宠物百科
        wikiAdapter = new MainFragPetWikiAdapter(mActivity);
        LinearLayoutManager wikiLayout = new LinearLayoutManager(mActivity);
        wikiLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvMainfragPetknow.setAdapter(wikiAdapter);
        rvMainfragPetknow.setLayoutManager(wikiLayout);
        //宠圈
        postAdapter = new MainGoodPostAdapter(mActivity);
        LinearLayoutManager postLayout = new LinearLayoutManager(mActivity);
        rvMainfragPetcicler.setLayoutManager(postLayout);
        rvMainfragPetcicler.setAdapter(postAdapter);
        rvMainfragPetcicler.setNestedScrollingEnabled(false);
        ivMainfragShopshow.setClickable(false);
        ivMainfragShopgone.setClickable(false);
        //三张图
        threeAdapter = new MainSmallIconThreeAdapter(mActivity);
        //两张图
        twoAdapter = new MainSmallIconTwoAdapter(mActivity);
        //添加子View处理滑动冲突
        svMainHome.addRecyclerView(rvMainfragBeauty);
        svMainHome.addRecyclerView(rvMainfragentSmallicons);
        svMainHome.addRecyclerView(rvMainfragmentService);
        svMainHome.addRecyclerView(rvMainfragPetcicler);
        svMainHome.addRecyclerView(rvMainfragPetknow);
        //顶部获取焦点，解决recyclerView抢占焦点问题
        headRoot.post(new Runnable() {
            @Override
            public void run() {
                headRoot.setFocusableInTouchMode(true);
            }
        });
    }

    private void clearList() {
        listMainService.clear();
        listHotBeautician.clear();
        listPetEncyclopedia.clear();
        listPetEncyclopedia.clear();
    }

    private void initData(Activity activity) {
        mActivity = activity;
        pDialog = new MProgressDialog(activity);
        spUtil = SharedPreferenceUtil.getInstance(mActivity);
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatIngEvent(1));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initWindows();
        }
    }

    private void getData() {
        pDialog.showDialog();
        autoLogin(liveLat, liveLng);
        getPetWiki();
        getGoodPost();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goodPostUpdate(UpdateGoodPostEvent event) {
        //点赞事件触发列表刷新
        getGoodPost();
    }

    /**
     * 获取宠物列表
     */
    private void getGoodPost() {
        CommUtil.queryHomeGoodPosts(timestamp, spUtil.getString("cellphone", ""),
                mActivity, page, isFollowed, isExistsVideo, isExistsImg,
                queryGoodPostsHandler);
    }

    private AsyncHttpResponseHandler queryGoodPostsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        Gson gson = new Gson();
                        PostSelectionBean data = gson.fromJson(
                                jdata.toString(), PostSelectionBean.class);
                        if (data != null) {
                            List<PostSelectionBean.PostsBean> posts = data.getPosts();
                            if (posts != null && posts.size() > 0) {
                                postAdapter.setList(posts);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void getPetWiki() {
        CommUtil.encyclopediaQueryEncyclopediaList(mActivity, 1, 5, 0, "", wikiHandler);
    }

    private AsyncHttpResponseHandler wikiHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray jarrdata = jobj.getJSONArray("data");
                        if (jarrdata.length() > 0) {
                            listPetEncyclopedia.clear();
                            for (int i = 0; i < jarrdata.length(); i++) {
                                listPetEncyclopedia.add(Encyclopedias.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                            wikiAdapter.setListPetEncyclopedia(listPetEncyclopedia);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private AsyncHttpResponseHandler bannerHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("ad") && !jdata.isNull("ad")) {
                            nvMainfragBottomholder.setVisibility(View.VISIBLE);
                            nvMainfragBottomholderShadow.setVisibility(View.VISIBLE);
                            JSONObject ad = jdata.getJSONObject("ad");
                            if (ad.has("imgUrl") && !ad.isNull("imgUrl")) {
                                GlideUtil.loadImg(mActivity, ad.getString("imgUrl"), nvMainfragBottomholder, R.drawable.icon_production_default);
                            }
                            if (ad.has("backup") && !ad.isNull("backup")) {
                                adBackUp = ad.getString("backup");
                            }
                            if (ad.has("point") && !ad.isNull("point")) {
                                adPoint = ad.getInt("point");
                            }
                        } else {
                            nvMainfragBottomholder.setVisibility(View.GONE);
                            nvMainfragBottomholderShadow.setVisibility(View.GONE);
                        }
                        if (jdata.has("topList") && !jdata.isNull("topList")) {
                            JSONArray topList = jdata.getJSONArray("topList");
                            listTopBanner.clear();
                            for (int i = 0; i < topList.length(); i++) {
                                listTopBanner.add(com.haotang.pet.entity.Banner.json2Entity(topList
                                        .getJSONObject(i)));
                            }
                            if (listTopBanner.size() > 0) {
                                setTopBanner(listTopBanner);
                            }

                        }
                        if (jdata.has("middleList") && !jdata.isNull("middleList")) {
                            JSONArray bottomList = jdata.getJSONArray("middleList");
                            listBottomBanner.clear();
                            if (bottomList.length() > 0) {
                                bannerMainfragmentBottom.setVisibility(View.VISIBLE);
                                for (int i = 0; i < bottomList.length(); i++) {
                                    listBottomBanner.add(com.haotang.pet.entity.Banner.json2Entity(bottomList
                                            .getJSONObject(i)));
                                }
                                setBottomBanner();
                            } else {
                                bannerMainfragmentBottom.setVisibility(View.GONE);
                            }
                        } else {

                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mActivity, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void setBottomBanner() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < listBottomBanner.size(); i++) {
            list.add(listBottomBanner.get(i).imgUrl);
        }
        GlideImageLoaderFourRound glideImageLoaderFourRound = new GlideImageLoaderFourRound();
        glideImageLoaderFourRound.setRound(20);
        bannerMainfragmentBottom.setImages(list)
                .setImageLoader(glideImageLoaderFourRound)
                .setOnBannerListener(new BottomBannerClick())
                .start();
    }

    private void setTopBanner(List<com.haotang.pet.entity.Banner> listTopBanner) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < listTopBanner.size(); i++) {
            list.add(listTopBanner.get(i).imgUrl);
        }
        bannerMainfragmentTop.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new TopBannerClick())
                .start();
        createPoint(0);
    }

    private void getBanner(int shopId) {
        CommUtil.getHomeBanner(mActivity, spUtil.getInt("cityId", 1), shopId, liveLng, liveLat, spUtil.getInt("isFirstLogin", 0), bannerHandler);
    }


    private AsyncHttpResponseHandler homePageHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("iconsTop") && !jdata.isNull("iconsTop")) {
                            JSONArray iconsTop = jdata.getJSONArray("iconsTop");
                            if (iconsTop != null && iconsTop.length() > 0) {
                                listMainService.clear();
                                slMainFragService.setVisibility(View.VISIBLE);
                                for (int i = 0; i < iconsTop.length(); i++) {
                                    JSONObject jicon = iconsTop.getJSONObject(i);
                                    MainService ms = new MainService();
                                    if (jicon.has("pic") && !jicon.isNull("pic")) {
                                        ms.pic = jicon.getString("pic");
                                    }
                                    if (jicon.has("txt") && !jicon.isNull("txt")) {
                                        ms.txt = jicon.getString("txt");
                                    }
                                    if (jicon.has("backup")
                                            && !jicon.isNull("backup")) {
                                        ms.backup = jicon.getString("backup");
                                    }
                                    if (jicon.has("intro")
                                            && !jicon.isNull("intro")) {
                                        ms.intro = jicon.getString("intro");
                                    }
                                    if (jicon.has("tag") && !jicon.isNull("tag")) {
                                        ms.tag = jicon.getString("tag");
                                    }
                                    if (jicon.has("point")
                                            && !jicon.isNull("point")) {
                                        ms.point = jicon.getInt("point");
                                    }
                                    listMainService.add(i, ms);
                                    serviceItemAdapter.setList(listMainService);
                                }
                            }
                        } else {
                            slMainFragService.setVisibility(View.GONE);
                        }

                        if (jdata.has("iconsCenter") && !jdata.isNull("iconsCenter")) {
                            JSONArray iconsTop = jdata.getJSONArray("iconsCenter");
                            if (iconsTop != null && iconsTop.length() > 0) {
                                listSmallIcons.clear();
                                for (int i = 0; i < iconsTop.length(); i++) {
                                    JSONObject jicon = iconsTop.getJSONObject(i);
                                    MainService ms = new MainService();
                                    if (jicon.has("pic") && !jicon.isNull("pic")) {
                                        ms.pic = jicon.getString("pic");
                                    }
                                    if (jicon.has("txt") && !jicon.isNull("txt")) {
                                        ms.txt = jicon.getString("txt");
                                    }
                                    if (jicon.has("backup")
                                            && !jicon.isNull("backup")) {
                                        ms.backup = jicon.getString("backup");
                                    }
                                    if (jicon.has("intro")
                                            && !jicon.isNull("intro")) {
                                        ms.intro = jicon.getString("intro");
                                    }
                                    if (jicon.has("tag") && !jicon.isNull("tag")) {
                                        ms.tag = jicon.getString("tag");
                                    }
                                    if (jicon.has("point")
                                            && !jicon.isNull("point")) {
                                        ms.point = jicon.getInt("point");
                                    }
                                    listSmallIcons.add(i, ms);
                                    if (listSmallIcons != null && listSmallIcons.size() > 0) {
                                        llCenter.setVisibility(View.VISIBLE);
                                        if (jdata.has("iconsCenterTitle") && !jdata.isNull("iconsCenterTitle")) {
                                            tvMainfragMiddletitle.setText(jdata.getString("iconsCenterTitle"));
                                        }
//                                        if (jdata.has("iconsCenterSubTitle") && !jdata.isNull("iconsCenterSubTitle")) {
//                                            tvMainfragMiddelsubtitle.setVisibility(View.VISIBLE);
//                                            tvMainfragMiddelsubtitle.setText(jdata.getString("iconsCenterSubTitle"));
//                                        } else {
//                                            tvMainfragMiddelsubtitle.setVisibility(View.GONE);
//                                        }
                                        //分三种情况
                                        if (listSmallIcons.size() == 1) {
                                            llMainfragMoreicons.setVisibility(View.GONE);
                                            nvCenterIcon.setVisibility(View.VISIBLE);
                                            rvMainfragentSmallicons.setVisibility(View.GONE);
                                            scrollLineView.setVisibility(View.GONE);
                                            GlideUtil.loadImg(mActivity, listSmallIcons.get(0).pic, nvCenterIcon, R.drawable.icon_production_default);
                                        }
//                                        else if (listSmallIcons.size() == 2) {
//                                            llMainfragMoreicons.setVisibility(View.GONE);
//                                            nvCenterIcon.setVisibility(View.GONE);
//                                            rlMainfragSmallicons.setVisibility(View.VISIBLE);
//                                            rvMainfragentSmallicons.setVisibility(View.VISIBLE);
//                                            GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
//                                            rvMainfragentSmallicons.setLayoutManager(layoutManager);
//                                            rvMainfragentSmallicons.setAdapter(twoAdapter);
//                                            twoAdapter.setList(listSmallIcons);
//                                        }
                                        //两个和三个暂时使用相同的adapter
                                        else {
                                            nvCenterIcon.setVisibility(View.GONE);
                                            rvMainfragentSmallicons.setVisibility(View.VISIBLE);
                                            scrollLineView.setVisibility(View.VISIBLE);
                                            if (spUtil.getBoolean("isShowHomeMore", true)) {
                                                llMainfragMoreicons.setVisibility(View.VISIBLE);
                                            } else {
                                                llMainfragMoreicons.setVisibility(View.GONE);
                                            }
                                            rlMainfragSmallicons.setVisibility(View.VISIBLE);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
                                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                            rvMainfragentSmallicons.setLayoutManager(layoutManager);
                                            rvMainfragentSmallicons.setAdapter(threeAdapter);
                                            threeAdapter.setList(listSmallIcons);
                                        }
                                    } else {
                                        llCenter.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                llCenter.setVisibility(View.GONE);
                            }
                        } else {
                            llCenter.setVisibility(View.GONE);
                        }

                        if (jdata.has("hotelInfo") && !jdata.isNull("hotelInfo")) {
                            JSONObject hotelInfo = jdata.getJSONObject("hotelInfo");
                            slMainFragHotel.setVisibility(View.VISIBLE);
                            if (hotelInfo.has("title") && !hotelInfo.isNull("title")) {
                                tvMainfragFostertitle.setText(hotelInfo.getString("title"));
                            }
                            if (hotelInfo.has("subtitle") && !hotelInfo.isNull("subtitle")) {
                                tvMainfragFostersubtitle.setVisibility(View.VISIBLE);
                                tvMainfragFostersubtitle.setText(hotelInfo.getString("subtitle"));
                            } else {
                                tvMainfragFostersubtitle.setVisibility(View.GONE);
                            }
                            if (hotelInfo.has("pic") && !hotelInfo.isNull("pic")) {
                                GlideUtil.loadImg(getContext(), hotelInfo.getString("pic"), vnMainfragFostericon, R.drawable.icon_production_default);
                            }
                            if (hotelInfo.has("point") && !hotelInfo.isNull("point")) {
                                fosterPoint = hotelInfo.getInt("point");
                            }
                            if (hotelInfo.has("backup") && !hotelInfo.isNull("backup")) {
                                fosterBackup = hotelInfo.getString("backup");
                            }
                        } else {
                            slMainFragHotel.setVisibility(View.GONE);
                        }
                        if (jdata.has("homeWorkerTip") && !jdata.isNull("homeWorkerTip")) {
                            tvMainfragBeautytip.setText(jdata.getString("homeWorkerTip"));
                        }
                        if (jdata.has("homeWorkerList")
                                && !jdata.isNull("homeWorkerList")) {
                            JSONArray workersjservicearr = jdata
                                    .getJSONArray("homeWorkerList");
                            vMainFragLine.setTranslationX(0);
                            listHotBeautician.clear();
                            if (workersjservicearr.length() > 0) {
                                rvMainfragBeauty.setVisibility(View.VISIBLE);
                                for (int i = 0; i < workersjservicearr.length(); i++) {
                                    Beautician btc = Beautician
                                            .json2Entity(workersjservicearr
                                                    .getJSONObject(i));
                                    listHotBeautician.add(btc);
                                }
                                if (listHotBeautician.size() > 6) {
                                    GridLayoutManager beautyLayout = new GridLayoutManager(mActivity, 2);
                                    beautyLayout.setOrientation(GridLayoutManager.HORIZONTAL);
                                    rvMainfragBeauty.setLayoutManager(beautyLayout);
                                    flMainScroll.setVisibility(View.VISIBLE);
                                } else {
                                    LinearLayoutManager llayoutManger = new LinearLayoutManager(mContext);
                                    llayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
                                    rvMainfragBeauty.setLayoutManager(llayoutManger);
                                }
                                serchBeauAdapter.notifyDataSetChanged();
                                tvMainfragBeautynum.setText(workersjservicearr.length() + "名美容师可为您服务");
                                if (listHotBeautician.size() < 3 || listHotBeautician.size() == 6) {
                                    flMainScroll.setVisibility(View.GONE);
                                }
                            } else {
                                tvMainfragBeautynum.setText(0 + "名美容师可为您服务");
                                rvMainfragBeauty.setVisibility(View.GONE);
                                flMainScroll.setVisibility(View.GONE);
                            }
                        } else {
                            flMainScroll.setVisibility(View.GONE);
                        }
                        //获取新手礼包
                        getMainActivity();
                    }
                } else {
                    ToastUtil.showToastShortBottom(mActivity, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
        }
    };

    private void getHomeData(int id) {
        CommUtil.getNewHomePage(mActivity, id, liveLng, liveLat, spUtil.getString("cellphone", ""), Global.getIMEI(mActivity), homePageHandler);
    }


    private void getMainActivity() {
        boolean mainfrag_dialog = spUtil.getBoolean("MAINFRAG_DIALOG", false);
        if (!mainfrag_dialog) {
            spUtil.saveBoolean("MAINFRAG_DIALOG", true);
            localBannerList.clear();
            CommUtil.getActivityPage(mActivity, spUtil.getInt("nowShopCityId", 0), spUtil.getInt("isFirstLogin", 0), 0, liveLat, liveLng, handlerHomeActivity);
        }
    }

    private AsyncHttpResponseHandler handlerHomeActivity = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            int isExist = 0;
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject jdata = object.getJSONObject("data");
                        if (jdata.has("isExist") && !jdata.isNull("isExist")) {
                            isExist = jdata.getInt("isExist");
                        }
                        if (jdata.has("list") && !jdata.isNull("list")) {
                            JSONArray jarrlist = jdata.getJSONArray("list");
                            if (jarrlist.length() > 0) {
                                for (int i = 0; i < jarrlist.length(); i++) {
                                    localBannerList.add(ActivityPage.json2Entity(jarrlist
                                            .getJSONObject(i)));
                                }
                            }
                        }
                    }
                } else {
                    Utils.Exit(mActivity, code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (localBannerList.size() > 0 && isExist == 0) {//有弹框
                for (int i = 0; i < localBannerList.size(); i++) {
                    ActivityPage activityPage = localBannerList.get(i);
                    if (activityPage != null) {
                        int id = activityPage.getId();
                        if (activityPage.getCountType() == 0) {//显示一次
                            String tag_mainfrag_one = Utils.readFileData(mActivity, fileName_one);
                            Log.e("TAG", "tag_mainfrag_one = " + tag_mainfrag_one);
                            if (Utils.isStrNull(tag_mainfrag_one)) {
                                String[] split = tag_mainfrag_one.split(",");
                                if (split != null && split.length > 0) {
                                    boolean isXianShied = false;
                                    for (int j = 0; j < split.length; j++) {
                                        if (Integer.parseInt(split[j]) == id) {
                                            isXianShied = true;
                                            break;
                                        }
                                    }
                                    if (isXianShied) {
                                        activityPage.setDelete(true);
                                    } else {
                                        Utils.writeFileData(mActivity, fileName_one, id + ",");
                                    }
                                } else {
                                    Utils.writeFileData(mActivity, fileName_one, id + ",");
                                }
                            } else {
                                Utils.writeFileData(mActivity, fileName_one, id + ",");
                            }
                        } else if (activityPage.getCountType() == 1) {//每次都显示

                        } else if (activityPage.getCountType() == 2) {//每日一次
                            Calendar c = Calendar.getInstance();
                            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            int mDay = c.get(Calendar.DAY_OF_MONTH);
                            String tag_mainfrag_one_day = Utils.readFileData(mActivity, fileName_one_day);
                            Log.e("TAG", "tag_mainfrag_one_day = " + tag_mainfrag_one_day);
                            if (Utils.isStrNull(tag_mainfrag_one_day)) {
                                String[] split = tag_mainfrag_one_day.split(",");
                                if (split != null && split.length > 0) {
                                    boolean isXianShied = false;
                                    for (int j = 0; j < split.length; j++) {
                                        String[] split1 = split[j].split("_");
                                        if (split1 != null && split1.length == 2) {
                                            if (Integer.parseInt(split1[0]) == id && Integer.parseInt(split1[1]) == mDay) {
                                                isXianShied = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (isXianShied) {
                                        activityPage.setDelete(true);
                                    } else {
                                        Utils.writeFileData(mActivity, fileName_one_day, id + "_" + mDay + ",");
                                    }
                                } else {
                                    Utils.writeFileData(mActivity, fileName_one_day, id + "_" + mDay + ",");
                                }
                            } else {
                                Utils.writeFileData(mActivity, fileName_one_day, id + "_" + mDay + ",");
                            }
                        }
                    }
                }
                Iterator<ActivityPage> it = localBannerList.iterator();
                while (it.hasNext()) {
                    ActivityPage activityPage = it.next();
                    if (activityPage != null && activityPage.isDelete()) {
                        it.remove();
                    }
                }
            }
            if (localBannerList.size() > 0 && isExist == 0) {//有弹框
                Utils.ActivityPage(mActivity, localBannerList, 1);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };


    /**
     * 自动登录获取数据
     */
    private void autoLogin(double lat, double lng) {
        if (Utils.checkLogin(mActivity)) {
            flMainScroll.setVisibility(View.VISIBLE);
            tvAllBeauty.setVisibility(View.GONE);
            CommUtil.loginAuto(mActivity, spUtil.getString("cellphone", ""),
                    Global.getIMEI(mActivity),
                    Global.getCurrentVersion(mActivity),
                    spUtil.getInt("userid", 0), lat, lng, autoLoginHandler);
        } else {
            shopGuide.showGuideView(getActivity(), llMainfragLoc, null, this);
            tvHomeShopnameShow.setText("请选择门店");
            tvHomeShopnameGone.setText("请选择门店");
            ivMainfragShopshow.setVisibility(View.GONE);
            ivMainfragShopgone.setVisibility(View.GONE);
            getHomeData(0);
            getBanner(0);
            flMainScroll.setVisibility(View.GONE);
            tvAllBeauty.setVisibility(View.VISIBLE);
        }
    }


    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                pDialog.closeDialog();
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("user") && !jData.isNull("user")) {
                            JSONObject jUser = jData.getJSONObject("user");
                            if (jUser.has("isNewComer") && !jUser.isNull("isNewComer")) {
                                isNewComer = jUser.getInt("isNewComer");
                            }
                            if (jUser.has("cityId") && !jUser.isNull("cityId")) {
                                cityId = jUser.getInt("cityId");
                            }
                            Utils.mLogError("显示 shopId" + spUtil.getInt("nowShopId", 0));
                            if (spUtil.getInt("nowShopId", 0) == 0) {
                                if (jUser.has("shopId") && !jUser.isNull("shopId")) {
                                    ivMainfragShopshow.setClickable(true);
                                    ivMainfragShopgone.setClickable(true);
                                    if (jUser.getInt("shopId") == 0) {
                                        ivMainfragShopshow.setVisibility(View.GONE);
                                        ivMainfragShopgone.setVisibility(View.GONE);
                                        tvHomeShopnameShow.setText("请选择门店");
                                        tvHomeShopnameGone.setText("请选择门店");
                                        //没有门店，只显示请选择门店提示
                                        Utils.mLogError("没有门店，只显示请选择门店提示 shopId 0");
                                        shopGuide.showGuideView(getActivity(), llMainfragLoc, null, MainNewFragment.this);
                                    } else {
                                        shopId = jUser.getInt("shopId");
                                        spUtil.saveInt("shopId", shopId);
                                        ivMainfragShopshow.setVisibility(View.VISIBLE);
                                        ivMainfragShopgone.setVisibility(View.VISIBLE);

                                        if (jUser.has("cityName") && !jUser.isNull("cityName")) {
                                            cityName = jUser.getString("cityName");
                                        }
                                        if (jUser.has("shopName") && !jUser.isNull("shopName")) {
                                            shopName = jUser.getString("shopName");
                                        }
                                        if (!Utils.isStrNull(spUtil.getString("nowShop", ""))) {
                                            if (!"".equals(cityName) && !"".equals(shopName)) {
                                                tvHomeShopnameShow.setText(cityName + "·" + shopName);
                                                tvHomeShopnameGone.setText(cityName + "·" + shopName);
                                            } else {
                                                tvHomeShopnameShow.setText(cityName);
                                                tvHomeShopnameGone.setText(cityName);
                                            }
                                        }
                                        Utils.mLogError("有门店信息，显示两个提示");
                                        llMainfragLoc.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //有门店信息，显示两个提示
                                                shopGuide.showGuideView(getActivity(), llMainfragLoc, ivMainfragShopshow, MainNewFragment.this);
                                            }
                                        },200);
                                    }
                                } else {
                                    ivMainfragShopshow.setVisibility(View.GONE);
                                    ivMainfragShopgone.setVisibility(View.GONE);
                                    tvHomeShopnameShow.setText("请选择门店");
                                    tvHomeShopnameGone.setText("请选择门店");
                                    //没有门店，只显示请选择门店提示
                                    Utils.mLogError("没有门店，只显示请选择门店提示 shopID null");
                                    shopGuide.showGuideView(getActivity(), llMainfragLoc, null, MainNewFragment.this);
                                }
                            } else {
                                ivMainfragShopshow.setClickable(true);
                                ivMainfragShopgone.setClickable(true);
                                ivMainfragShopshow.setVisibility(View.VISIBLE);
                                ivMainfragShopgone.setVisibility(View.VISIBLE);
                                shopId = spUtil.getInt("nowShopId", 0);
                                Utils.mLogError("有门店信息，显示两个提示 ");
                                //有门店信息，显示两个提示
                                shopGuide.showGuideView(getActivity(), llMainfragLoc, ivMainfragShopshow, MainNewFragment.this);
                                tvHomeShopnameShow.setText(spUtil.getString("nowCity", "").replace("市", "") + "·" + spUtil.getString("nowShop", ""));
                                tvHomeShopnameGone.setText(spUtil.getString("nowCity", "").replace("市", "") + "·" + spUtil.getString("nowShop", ""));
                            }


                            if (jUser.has("memberLevelId") && !jUser.isNull("memberLevelId")) {
                                int memberLevelId = jUser.getInt("memberLevelId");
                                Log.e("TAG", "memberLevelId = " + memberLevelId);
                                spUtil.saveInt("shopCartMemberLevelId", memberLevelId);
                            } else {
                                spUtil.removeData("shopCartMemberLevelId");
                            }
                            if (jUser.has("levelName") && !jUser.isNull("levelName")) {
                                spUtil.saveString("levelName", jUser.getString("levelName"));
                            }
                            if (jUser.has("myPetMaximum") && !jUser.isNull("myPetMaximum")) {
                                spUtil.saveInt("myPetMaximum", jUser.getInt("myPetMaximum"));
                            } else {
                                spUtil.removeData("myPetMaximum");
                            }
                            if (jUser.has("petKinds") && !jUser.isNull("petKinds")) {
                                JSONArray petKinds = jUser.getJSONArray("petKinds");
                                if (petKinds.length() > 0) {
                                    if (petKinds.length() >= 2) {
                                        spUtil.saveInt("petkind", 1);
                                    } else {
                                        spUtil.saveInt("petkind", petKinds.getInt(0));
                                    }
                                } else {
                                    spUtil.saveInt("petkind", 1);
                                }

                            }
                            if (jUser.has("areacode")
                                    && !jUser.isNull("areacode")) {
                                spUtil.saveInt("upRegionId",
                                        jUser.getInt("areacode"));
                            } else {
                                spUtil.removeData("upRegionId");
                            }
                            if (jUser.has("shopName")
                                    && !jUser.isNull("shopName")) {
                                spUtil.saveString("upShopName",
                                        jUser.getString("shopName"));
                            } else {
                                spUtil.removeData("upShopName");
                            }
                            if (jUser.has("shopId") && !jUser.isNull("shopId")) {
                                spUtil.saveInt("shopid", jUser.getInt("shopId"));
                            } else {
                                spUtil.removeData("shopid");
                            }
                            if (jUser.has("areaId") && !jUser.isNull("areaId")) {
                                spUtil.saveInt("areaid", jUser.getInt("areaId"));
                            } else {
                                spUtil.removeData("areaid");
                            }
                            if (jUser.has("areaName")
                                    && !jUser.isNull("areaName")) {
                                spUtil.saveString("areaname",
                                        jUser.getString("areaName"));
                            } else {
                                spUtil.removeData("areaname");
                            }
                            if (jUser.has("homeAddress")
                                    && !jUser.isNull("homeAddress")) {
                                JSONObject jAddr = jUser
                                        .getJSONObject("homeAddress");
                                if (jAddr.has("Customer_AddressId")
                                        && !jAddr.isNull("Customer_AddressId")) {
                                    spUtil.saveInt("addressid",
                                            jAddr.getInt("Customer_AddressId"));
                                }
                                if (jAddr.has("lat") && !jAddr.isNull("lat")) {
                                    spUtil.saveString("lat",
                                            jAddr.getDouble("lat") + "");
                                }
                                if (jAddr.has("lng") && !jAddr.isNull("lng")) {
                                    spUtil.saveString("lng",
                                            jAddr.getDouble("lng") + "");
                                }
                                if (jAddr.has("address")
                                        && !jAddr.isNull("address")) {
                                    spUtil.saveString("address",
                                            jAddr.getString("address"));
                                }
                            } else {
                                spUtil.removeData("addressid");
                                spUtil.removeData("lat");
                                spUtil.removeData("lng");
                                spUtil.removeData("address");
                            }
                            if (jUser.has("pet") && !jUser.isNull("pet")) {
                                JSONObject jPet = jUser.getJSONObject("pet");
                                if (jPet.has("id")
                                        && !jPet.isNull("id")) {
                                    spUtil.saveInt("petid",
                                            jPet.getInt("id"));
                                }
                                if (jPet.has("isCerti")
                                        && !jPet.isNull("isCerti")) {
                                    spUtil.saveInt("isCerti",
                                            jPet.getInt("isCerti"));
                                }
                                if (jPet.has("mypetId")
                                        && !jPet.isNull("mypetId")) {
                                    mypetId = jPet.getInt("mypetId");
                                } else {
                                    mypetId = 0;
                                }
                            } else {
                                spUtil.removeData("isCerti");
                            }
                            if (jUser.has("myPet") && !jUser.isNull("myPet")) {
                                JSONObject jMyPet = jUser
                                        .getJSONObject("myPet");
                                if (mypetId > 0 && jMyPet.has("petId")
                                        && !jMyPet.isNull("petId")) {
                                    spUtil.saveInt("petid",
                                            jMyPet.getInt("petId"));
                                }
                                if (jMyPet.has("id")
                                        && !jMyPet.isNull("id")) {
                                    spUtil.saveInt("customerpetid",
                                            jMyPet.getInt("id"));
                                } else {
                                    spUtil.removeData("customerpetid");
                                }
                                if (jMyPet.has("nickName")
                                        && !jMyPet.isNull("nickName")) {
                                    spUtil.saveString("customerpetname",
                                            jMyPet.getString("nickName"));
                                } else {
                                    spUtil.removeData("customerpetname");
                                }
                                if (jMyPet.has("avatarPath")
                                        && !jMyPet.isNull("avatarPath")) {
                                    spUtil.saveString("mypetImage",
                                            jMyPet.getString("avatarPath"));
                                } else {
                                    spUtil.removeData("mypetImage");
                                }
                            } else {
                                spUtil.removeData("customerpetname");
                                spUtil.removeData("customerpetid");
                                spUtil.removeData("mypetImage");
                            }
                        }
                    }
                    getHomeData(shopId);
                    getBanner(shopId);
                } else {
                    if (resultCode == Global.EXIT_USER_CODE) {

                    }
                    Utils.Exit(mActivity, resultCode);
                    getHomeData(shopId);
                    getBanner(shopId);
                    flMainScroll.setVisibility(View.GONE);
                    tvAllBeauty.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
            }
            //显示新人礼包
            showComer();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            getHomeData(shopId);
            getBanner(shopId);
        }
    };

    /**
     * 显示新来的，新人专享礼包
     */
    private void showComer() {
        //存在这个值表示是新用户注册，执行跳转 并且引导没有显示
        if (isNewComer > 0 && !spUtil.getBoolean("tag_isNewComer", false) && !spUtil.getIsShopGuideFirst()) {
            spUtil.saveBoolean("tag_isNewComer", true);
            startActivity(new Intent(getActivity(), NewUserGiveCouponActivity.class));
        }
    }

    private void getStatistics(String typeid, String activeid) {
        CommUtil.logcountAdd(mActivity, typeid, activeid, statisticsHandler);
    }

    private AsyncHttpResponseHandler statisticsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void initBD() {
        mLocationClient = new LocationClient(mActivity);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedLocationPoiList(true);
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        //unbinder.unbind();
    }

    @OnClick({R.id.iv_mainfrag_shopshow, R.id.iv_mainfrag_cardshow, R.id.iv_mainfragment_newpeople, R.id.tv_mainfrag_morepetknow,
            R.id.tv_mainfrag_morepetcicler, R.id.ll_mainfrag_loc, R.id.ll_mainfrag_locgone, R.id.iv_mainfrag_shopgone, R.id.iv_mainfrag_cardgone,
            R.id.nv_mainfrag_bottomholder, R.id.nv_mainfrag_centericonone, R.id.vn_mainfrag_fostericon, R.id.tv_mainfrag_allbeauty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_mainfrag_shopshow:
                Intent shopIntent = new Intent(mActivity, ShopDetailActivity.class);
                shopIntent.putExtra("shopid", shopId);
                shopIntent.putExtra("showButton", false);
                startActivity(shopIntent);
                break;
            case R.id.iv_mainfrag_shopgone:
                Intent shopIntenttwo = new Intent(mActivity, ShopDetailActivity.class);
                shopIntenttwo.putExtra("shopid", shopId);
                shopIntenttwo.putExtra("showButton", false);
                startActivity(shopIntenttwo);
                break;
            case R.id.iv_mainfrag_cardshow:
                if (Utils.checkLogin(mActivity)) {
                    Intent qrCodeIntent = new Intent(mActivity, QrCodeNewActivity.class);
                    startActivity(qrCodeIntent);
                } else {
                    Intent loginIntent = new Intent(mActivity, LoginNewActivity.class);
                    loginIntent.putExtra("previous",
                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                    startActivity(loginIntent);
                }
                break;
            case R.id.iv_mainfragment_newpeople:
                break;
            case R.id.tv_mainfrag_morepetknow:
                Intent intent = new Intent(mActivity, EncyclopediasActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_mainfrag_morepetcicler:
                Intent intent1 = new Intent(mActivity, PetCircleOrSelectActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_mainfrag_loc:
                if (Utils.checkLogin(mActivity)) {
                    Intent allShopIntent = new Intent(mActivity, AllShopLocActivity.class);
                    allShopIntent.putExtra("addr_lat", liveLat);
                    allShopIntent.putExtra("addr_lng", liveLng);
                    allShopIntent.putExtra("address", address);
                    allShopIntent.putExtra("shopId", shopId);
                    startActivity(allShopIntent);
                } else {
                    Intent loginIntent = new Intent(mActivity, LoginNewActivity.class);
                    loginIntent.putExtra("previous",
                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                    startActivity(loginIntent);
                }
                break;
            case R.id.ll_mainfrag_locgone:
                if (Utils.checkLogin(mActivity)) {
                    Intent allShopIntent = new Intent(mActivity, AllShopLocActivity.class);
                    allShopIntent.putExtra("addr_lat", liveLat);
                    allShopIntent.putExtra("addr_lng", liveLng);
                    allShopIntent.putExtra("address", address);
                    startActivity(allShopIntent);
                } else {
                    Intent loginIntent = new Intent(mActivity, LoginNewActivity.class);
                    loginIntent.putExtra("previous",
                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                    startActivity(loginIntent);
                }
                break;
            case R.id.iv_mainfrag_cardgone:
                if (Utils.checkLogin(mActivity)) {
                    Intent qrCodeIntent = new Intent(mActivity, QrCodeNewActivity.class);
                    startActivity(qrCodeIntent);
                } else {
                    Intent loginIntent = new Intent(mActivity, LoginNewActivity.class);
                    loginIntent.putExtra("previous",
                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                    startActivity(loginIntent);
                }
                break;
            case R.id.nv_mainfrag_bottomholder:
                Utils.goService(mActivity, adPoint, adBackUp);
                break;
            case R.id.nv_mainfrag_centericonone:
                if (listSmallIcons != null && listSmallIcons.size() > 0) {
                    Utils.goService(mActivity, listSmallIcons.get(0).point, listSmallIcons.get(0).backup);
                }
                break;
            case R.id.vn_mainfrag_fostericon:
                UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_HomePage_FosterCare);
                Utils.goService(mActivity, fosterPoint, fosterBackup);
                break;
            case R.id.tv_mainfrag_allbeauty:
                Intent beautySearchIntent = new Intent(mContext, SerchBeauActivity.class);
                startActivity(beautySearchIntent);
                break;
        }
    }

    @Override
    public void onShopGuideDismiss() {
        //关闭提示弹出，请求新手礼包
        showComer();
    }


    public class TopBannerClick implements OnBannerListener {

        @Override
        public void OnBannerClick(int position) {
            if (listTopBanner != null && listTopBanner.size() > 0 && listTopBanner.size() > position) {
                Utils.goService(mActivity, listTopBanner.get(position).point, listTopBanner.get(position).backup);
            }
        }
    }

    public class BottomBannerClick implements OnBannerListener {

        @Override
        public void OnBannerClick(int position) {
            if (listBottomBanner != null && listBottomBanner.size() > 0 && listBottomBanner.size() > position) {
                Utils.goService(mActivity, listBottomBanner.get(position).point, listBottomBanner.get(position).backup);
            }
        }
    }

    private class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            liveLat = location.getLatitude();
            liveLng = location.getLongitude();
            city = location.getCity();
            Utils.mLogError("homeCity========" + city);
            if (city.equals("北京市")) {
                spUtil.saveInt("cityId", 1);
            } else if (city.equals("深圳市")) {
                spUtil.saveInt("cityId", 2);
            } else {
                spUtil.saveInt("cityId", 1);
            }
            Utils.mLogError("homeCityId" + spUtil.getInt("cityId", 0) + "----");
            spUtil.saveString("lat_home", location.getLatitude() + "");
            spUtil.saveString("lng_home", location.getLongitude() + "");
            if (location.getPoiList() != null && location.getPoiList().size() > 0) {
                address = location.getPoiList().get(0).getName();
            }
            Utils.mLogError("address" + address);
            CountdownUtil.getInstance().cancel("LOCATION_TIMER");
            getData();
            mLocationClient.stop();
        }
    }

    private void getLocation() {
        pDialog.showDialog();
        CountdownUtil.getInstance().newTimer(3000, 1000, new CountdownUtil.ICountDown() {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mLocationClient.stop();
                getData();
            }
        }, "LOCATION_TIMER");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AdLoginEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.getPosition() == 1) {
            }
        }
    }

    //退出登录
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitEventd(ExitLoginEvent exitLoginEvent) {
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.isLogin()) {
                shopId = 0;
                getData();
            }
        }
    }
}
