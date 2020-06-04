package com.haotang.pet.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haotang.pet.ADActivity;
import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.CashbackAmountActivity;
import com.haotang.pet.CharacteristicServiceActivity;
import com.haotang.pet.FosterLiveListActivity;
import com.haotang.pet.JointWorkShopDetail;
import com.haotang.pet.LoginActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.MainToBeauList;
import com.haotang.pet.MemberUpgradeActivity;
import com.haotang.pet.MyCanActivity;
import com.haotang.pet.MyCardActivity;
import com.haotang.pet.MyCouponNewActivity;
import com.haotang.pet.NewUserGiveCouponActivity;
import com.haotang.pet.PetAddActivity;
import com.haotang.pet.PetCircleInsideActivity;
import com.haotang.pet.PetCircleInsideDetailActivity;
import com.haotang.pet.QrCodeNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.StatusLibs.StatusBarCompat;
import com.haotang.pet.adapter.BannerAdapter;
import com.haotang.pet.adapter.BannerAdapter.OnBannerItemClickListener;
import com.haotang.pet.adapter.BannerPwAdapter;
import com.haotang.pet.adapter.HotBeauticianAdapter;
import com.haotang.pet.adapter.JustNowAdapter;
import com.haotang.pet.adapter.MainAdAdapter;
import com.haotang.pet.adapter.MainCharacteristicServiceAdapter;
import com.haotang.pet.adapter.MainHospitalAdapter;
import com.haotang.pet.adapter.MainPetCircleAdapter;
import com.haotang.pet.adapter.MainPetEncyclopediaAdapter;
import com.haotang.pet.adapter.MainServiceAdapter;
import com.haotang.pet.entity.ActivityPage;
import com.haotang.pet.entity.AdLoginEvent;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.CircleOrSelectEvent;
import com.haotang.pet.entity.FloatIngEvent;
import com.haotang.pet.entity.JustNow;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.MainCharacteristicService;
import com.haotang.pet.entity.MainHospital;
import com.haotang.pet.entity.MainPetCircle;
import com.haotang.pet.entity.MainPetEncyclopedia;
import com.haotang.pet.entity.MainService;
import com.haotang.pet.entity.PetCircle;
import com.haotang.pet.entity.Recommendation;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.verticalbanner.VerticalBannerView;
import com.haotang.pet.verticalbanner.VerticalBannerView.OnChangeListener;
import com.haotang.pet.view.BubblePopupWindow;
import com.haotang.pet.view.HorizontalListView;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.MarqueeView;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.ObservableScrollView;
import com.haotang.pet.view.ScrollViewListener;
import com.haotang.pet.view.rollviewpager.RollPagerView;

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

@SuppressLint("NewApi")
public class MainFragment extends Fragment implements OnTouchListener,
        OnClickListener {
    private Activity mActivity;
    private SharedPreferenceUtil spUtil;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private View view;
    private MyGridView gvServices;
    private MarqueeView mv_mainfragmentcontent_verticalbanner1;
    private RelativeLayout rl_mainfragment_verticalbanner1;
    private Button btn_mainfragmentcontent_verticalbanner1;
    private PetCircle circle = new PetCircle();
    private PopupWindow pWin;
    private RelativeLayout rlHotBeautician;
    private View vw_mainfrag_hotbeau;
    private MainServiceAdapter adapterMainService;
    private BannerAdapter adapterBanner1;
    private RelativeLayout rl_mainfragment_jyzb;
    private RelativeLayout rl_mainfragment_jyzb_delete;
    private String liveTag;
    private String liveUrl;
    private TextView tv_mainfragment_jyzb_name;
    private double liveLat;
    private double liveLng;
    private RelativeLayout rl_mainfragmentcontent_community;
    private TextView tv_mainfragmentcontent_community_other;
    private ImageView iv_mainfragmentcontent_community_other;
    private View vw_mainfragmentcontent_community_other;
    private RollPagerView rvpBanner1;
    private MProgressDialog pDialog;
    private int circleId;
    private int circleType;
    private HorizontalListView hlv_mainfragment_beautician;
    private MListview mlv_mainfragment_petcircle;
    private RelativeLayout rl_mainfragment_tsfw;
    private TextView tv_mainfragment_tsfw;
    private View vw_mainfragment_tsfw;
    private RelativeLayout rl_mainfragment_tjyy;
    private TextView tv_mainfragment_tjyy;
    private View vw_mainfragment_tjyy;
    private RelativeLayout rl_mainfragment_cwbk;
    private TextView tv_mainfragment_cwbk;
    private View vw_mainfragment_cwbk;
    private MListview mlv_mainfragment_service;
    private int bottomIndex = 1;
    private ObservableScrollView osv_mainfrag;
    private int scrolledX;
    private int scrolledY;
    private Button btn_mainfeag_login;
    private RelativeLayout rl_mainfragment_wdl;
    private RelativeLayout ll_mainfragment_ydl;
    private TextView tv_mainfragment_username;
    private LinearLayout rl_mainfragment_dog;
    private TextView tv_mainfragment_dognames;
    private LinearLayout rl_mainfragment_cat;
    private TextView tv_mainfragment_catnames;
    private LinearLayout ll_mainfrag_userinfo;
    private TextView tv_mainfrag_yue;
    private TextView tv_mainfrag_yhq;
    private TextView tv_mainfrag_kabao;
    private TextView tv_mainfrag_banner1;
    private RelativeLayout rl_Banner1;
    private LinearLayout ll_mainfrag_pop;
    private RelativeLayout rl_mainfragment_verticalbanner2;
    private Button btn_mainfragmentcontent_verticalbanner2;
    private VerticalBannerView vbv_mainfragmentcontent_verticalbanner2;
    private int mypetId;
    private JustNowAdapter justNowAdapter;
    private MyGridView gv_mainfragmentcontent_ad;
    private ImageView civ_mainfrag_userimg;
    private LinearLayout ll_mainfragmentcontent_ad;
    private LinearLayout ll_mainfragmentcontent_service;
    private MainPetCircleAdapter<MainPetCircle> mainPetCircleAdapter;
    private MainAdAdapter<Banner> mainAdAdapter;
    private HotBeauticianAdapter<Beautician> hotBeauticianAdapter;
    private String[] BannerImgUrls1;
    private String[] BannerImgUrls2;
    private ArrayList<Banner> listBanner1 = new ArrayList<Banner>();
    private List<MainPetCircle> listPetCircle = new ArrayList<MainPetCircle>();
    private List<MainCharacteristicService> listCharacteristicService = new ArrayList<MainCharacteristicService>();
    private List<MainHospital> listHospital = new ArrayList<MainHospital>();
    private List<MainPetEncyclopedia> listPetEncyclopedia = new ArrayList<MainPetEncyclopedia>();
    private ArrayList<MainService> listMainService = new ArrayList<MainService>();
    private ArrayList<Beautician> listHotBeautician = new ArrayList<Beautician>();
    private StringBuilder dogNames = new StringBuilder();
    private StringBuilder catNames = new StringBuilder();
    private List<JustNow> listJustNow = new ArrayList<JustNow>();
    private List<Recommendation> listRecommendation = new ArrayList<Recommendation>();
    private List<Banner> listBanner2 = new ArrayList<Banner>();
    private List<Banner> listBanner3 = new ArrayList<Banner>();
    private RelativeLayout rl_mainfrag_top1;
    private ArrayList<String> listRecommendationStr = new ArrayList<String>();
    private int verticalBanner2Position;
    private MainPetEncyclopediaAdapter<MainPetEncyclopedia> mainPetEncyclopediaAdapter;
    private MainHospitalAdapter<MainHospital> mainHospitalAdapter;
    private MainCharacteristicServiceAdapter<MainCharacteristicService> mainCharacteristicServiceAdapter;
    private LinearLayout ll_mainfrag_yue;
    private LinearLayout ll_mainfrag_guantou;
    private LinearLayout ll_mainfrag_yhq;
    private LinearLayout ll_mainfrag_kabao;
    private LinearLayout ll_mainfrag_return_pop;
    private TextView tv_mainfrag_guantou;
    private TextView tv_mainfrag_return_pop;
    private ImageView iv_mainfrag_userimgbottom;
    private ImageView iv_mainfrag_huiyuan;
    private BubblePopupWindow mainPopupWindow;
    private int isVip;// 0 非会员  1 会员
    private String tips;
    private ImageView iv_mainfragment_banner3;
    private RelativeLayout rl_mainfragment_banner3;
    private LinearLayout ll_mainfrag_yue_pop;
    private LinearLayout ll_mainfrag_guantou_pop;
    private LinearLayout ll_mainfrag_yhq_pop;
    private LinearLayout ll_mainfrag_return;
    private LinearLayout ll_mainfrag_kabao_pop;
    private TextView tv_mainfrag_yue_pop;
    private TextView tv_mainfrag_return;
    private TextView tv_mainfrag_guantou_pop;
    private TextView tv_mainfrag_yhq_pop;
    private TextView tv_mainfrag_kabao_pop;
    private ImageView iv_mainfragment_dognames;
    private TextView textview_unlogin_content;
    private RelativeLayout rl_mainfrag_userimg;
    private RelativeLayout rl_mainfragment_pay;
    private LinearLayout ll_mainfrag_top;
    private List<ActivityPage> localBannerList = new ArrayList<ActivityPage>();
    //文件名称
    String fileName_one = "ad_mainfrag_one.txt";
    String fileName_one_day = "ad_mainfrag_one_day.txt";
    private int isNewComer;

    private void clearList() {
        listBanner3.clear();
        listBanner2.clear();
        mainAdAdapter.clearDeviceList();
        listRecommendation.clear();
        listJustNow.clear();
        listBanner1.clear();
        adapterBanner1.clearDeviceList();
        listPetCircle.clear();
        mainPetCircleAdapter.clearDeviceList();
        listCharacteristicService.clear();
        mainCharacteristicServiceAdapter.clearDeviceList();
        listHospital.clear();
        mainHospitalAdapter.clearDeviceList();
        listPetEncyclopedia.clear();
        mainPetEncyclopediaAdapter.clearDeviceList();
        listMainService.clear();
        adapterMainService.clearDeviceList();
        listHotBeautician.clear();
        hotBeauticianAdapter.clearDeviceList();
        dogNames.setLength(0);
        catNames.setLength(0);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.LIVEDELAYEDGONE:
                    spUtil.saveBoolean("isExit", false);
                    rl_mainfragment_jyzb.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public MainFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public MainFragment(double lat, double lng) {
        this.liveLat = lat;
        this.liveLng = lng;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initData(activity);
        initBD();
    }

    private void initData(Activity activity) {
        mActivity = activity;
        pDialog = new MProgressDialog(activity);
        spUtil = SharedPreferenceUtil.getInstance(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        return inflater.inflate(R.layout.mainfragmentnew, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();
        findView();
        setView();
        initLinster();
    }

    private void setStatusBar() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.aD1494F));
    }

    private void getData() {
        // 首页数据
        getHomeIndex();
    }

    private void getMainActivity() {
        localBannerList.clear();
        CommUtil.getActivityPage(mActivity, spUtil.getInt("cityId",0), spUtil.getInt("isFirstLogin", 0), 0,0,0, handlerHomeActivity);
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
                    if (code == Global.EXIT_USER_CODE) {
                        ll_mainfragment_ydl.setVisibility(View.GONE);
                        rl_mainfragment_wdl.setVisibility(View.VISIBLE);
                        ll_mainfrag_userinfo.setVisibility(View.GONE);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_mainfrag_top.getLayoutParams();
                        layoutParams.height = (int) mActivity.getResources().getDimension(R.dimen.dp_75);
                        ll_mainfrag_top.setLayoutParams(layoutParams);
                        rl_mainfragment_verticalbanner1.setVisibility(View.GONE);
                    }
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
                Utils.ActivityPage(mActivity, localBannerList,1);
            }
            // 自动登录
            autoLogin(liveLat, liveLng);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void initLinster() {
        rl_mainfragment_dog.setOnClickListener(this);
        iv_mainfragment_banner3.setOnClickListener(this);
        ll_mainfrag_yue.setOnClickListener(this);
        ll_mainfrag_guantou.setOnClickListener(this);
        ll_mainfrag_yhq.setOnClickListener(this);
        ll_mainfrag_kabao.setOnClickListener(this);
        ll_mainfrag_return.setOnClickListener(this);
        iv_mainfrag_huiyuan.setOnClickListener(this);
        ll_mainfrag_userinfo.setOnClickListener(this);
        rl_mainfrag_top1.setOnClickListener(this);
        tv_mainfragmentcontent_community_other.setOnClickListener(this);
        iv_mainfragmentcontent_community_other.setOnClickListener(this);
        rl_mainfragmentcontent_community.setOnClickListener(this);
        rl_mainfragment_jyzb.bringToFront();
        rl_mainfragment_jyzb_delete.setOnClickListener(this);
        rl_mainfragment_jyzb.setOnClickListener(this);
        rl_mainfragment_tsfw.setOnClickListener(this);
        rl_mainfragment_tjyy.setOnClickListener(this);
        btn_mainfeag_login.setOnClickListener(this);
        rl_mainfragment_cwbk.setOnClickListener(this);
        rlHotBeautician.setOnClickListener(this);
        rl_mainfragment_pay.setOnClickListener(this);
        tv_mainfrag_banner1.setOnClickListener(this);
        btn_mainfragmentcontent_verticalbanner1.setOnClickListener(this);
        btn_mainfragmentcontent_verticalbanner2.setOnClickListener(this);
        vbv_mainfragmentcontent_verticalbanner2
                .setOnChangeListener(new OnChangeListener() {
                    @Override
                    public void OnChange(int position) {
                        verticalBanner2Position = position;
                    }
                });
        mv_mainfragmentcontent_verticalbanner1
                .setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, TextView textView) {
                        if (listRecommendation != null
                                && listRecommendation.size() > 0
                                && listRecommendation.size() > position) {
                            Recommendation recommendation = listRecommendation
                                    .get(position);
                            if (recommendation != null) {
                                UmengStatistics.UmengEventStatistics(
                                        view.getContext(),
                                        Global.UmengEventID.click_HomePage_Headlines);
                                Utils.goService(mActivity, recommendation.point,
                                        recommendation.backup);
                            }
                        }
                    }
                });
        adapterBanner1.setOnItemClickListener(new OnBannerItemClickListener() {
            @Override
            public void OnBannerItemClick(int position) {
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_Banner1);
                if (listBanner1 != null && listBanner1.size() > 0
                        && listBanner1.size() > position) {
                    Banner banner = listBanner1.get(position);
                    if (banner != null) {
                        boolean goService = Utils.goService(mActivity, banner.point,
                                banner.backup);
                        if (!goService) {
                            Utils.imageBrower(mActivity, position,
                                    BannerImgUrls1);
                        }
                    }
                }
            }
        });
        gv_mainfragmentcontent_ad
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        UmengStatistics.UmengEventStatistics(mActivity,
                                Global.UmengEventID.click_HomePage_Banner2);
                        if (listBanner2 != null && listBanner2.size() > 0
                                && listBanner2.size() > position) {
                            Banner banner = listBanner2.get(position);
                            if (banner != null) {
                                boolean goService = Utils.goService(mActivity, banner.point,
                                        banner.backup);
                                if (!goService) {
                                    Utils.imageBrower(mActivity, position,
                                            BannerImgUrls2);
                                }
                            }
                        }
                        if (position == 0) {
                            getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_middleoperateone_main);
                        } else if (position == 1) {
                            getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_middleoperatetwo_main);
                        }


                    }
                });
        rvpBanner1.getViewPager().setOnPageChangeListener(
                new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        String text = (position + 1) + "/" + listBanner1.size()
                                + "全部";
                        SpannableString ss = new SpannableString(text);
                        ss.setSpan(new ForegroundColorSpan(mActivity
                                        .getResources().getColor(R.color.aCD484D)), 0,
                                String.valueOf(position + 1).length(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        tv_mainfrag_banner1.setText(ss);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });
        osv_mainfrag.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x,
                                        int y, int oldx, int oldy) {
                ll_mainfrag_pop.setVisibility(View.GONE);
                scrolledX = x;
                scrolledY = y;
            }
        });
        gvServices.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listMainService != null && listMainService.size() > 0
                        && listMainService.size() > position) {
                    MainService mainService = listMainService.get(position);
                    if (mainService != null) {
                        Utils.goService(mActivity, mainService.point, mainService.backup);
                        switch (position) {
                            case 0:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_one_main);
                                break;
                            case 1:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_two_main);
                                break;
                            case 2:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_thr_main);
                                break;
                            case 3:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_four_main);
                                break;
                            case 4:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_five_main);
                                break;
                            case 5:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_six_main);
                                break;
                            case 6:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_seven_main);
                                break;
                            case 7:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_eight_main);
                                break;
                            case 8:
                                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_nine_main);
                                break;
                        }
                    }
                }
            }
        });
        hlv_mainfragment_beautician
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        UmengStatistics
                                .UmengEventStatistics(
                                        mActivity,
                                        Global.UmengEventID.click_HomePage_PopularSelect);
                        goNext(BeauticianDetailActivity.class,
                                listHotBeautician.get(position).id, 0);
                    }
                });

        mlv_mainfragment_petcircle
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        UmengStatistics
                                .UmengEventStatistics(
                                        mActivity,
                                        Global.UmengEventID.click_HomePage_PetRingDetails);
                        if (listPetCircle != null && listPetCircle.size() > 0
                                && listPetCircle.size() > position) {
                            MainPetCircle mainPetCircle = listPetCircle
                                    .get(position);
                            if (mainPetCircle != null) {
                                startActivity(new Intent(mActivity,
                                        PetCircleInsideDetailActivity.class)
                                        .putExtra("postId",
                                                mainPetCircle.postId));
                            }
                        }
                        if (position == 0) {
                            getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_petcircle_main);
                        } else if (position == 1) {
                            getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_petselect_main);
                        }
                    }
                });
        mlv_mainfragment_service
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        switch (bottomIndex) {
                            case 1:// 特色服务
                                UmengStatistics
                                        .UmengEventStatistics(
                                                mActivity,
                                                Global.UmengEventID.click_HomePage_CharacteristicDetails);
                                if (listCharacteristicService != null
                                        && listCharacteristicService.size() > 0
                                        && listCharacteristicService.size() > position) {
                                    MainCharacteristicService mainCharacteristicService = listCharacteristicService
                                            .get(position);
                                    if (mainCharacteristicService != null) {
                                        Intent intent = new Intent(mActivity, CharacteristicServiceActivity.class);
                                        intent.putExtra("serviceType", 3);
                                        intent.putExtra("typeId", mainCharacteristicService.PetServiceTypeForListId);// 从特色服务跳转到门店列表专用，为了计算价格
                                        startActivity(intent);
                                    }
                                }
                                break;
                            case 2:// 推荐医院
                                UmengStatistics
                                        .UmengEventStatistics(
                                                mActivity,
                                                Global.UmengEventID.click_HomePage_Hospital);
                                if (listHospital != null && listHospital.size() > 0
                                        && listHospital.size() > position) {
                                    MainHospital mainHospital = listHospital
                                            .get(position);
                                    if (mainHospital != null) {
                                        Intent intent = new Intent();
                                        intent.setClass(mActivity,
                                                JointWorkShopDetail.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("mainHospital",
                                                mainHospital);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                                break;
                            case 3:// 宠物百科
                                UmengStatistics
                                        .UmengEventStatistics(
                                                mActivity,
                                                Global.UmengEventID.click_HomePage_Encyclopedia);
                                if (listPetEncyclopedia != null
                                        && listPetEncyclopedia.size() > 0
                                        && listPetEncyclopedia.size() > position) {
                                    MainPetEncyclopedia mainPetEncyclopedia = listPetEncyclopedia
                                            .get(position);
                                    if (mainPetEncyclopedia != null) {
                                        String url = mainPetEncyclopedia.url;
                                        if (url != null && !TextUtils.isEmpty(url)) {
                                            startActivity(new Intent(mActivity,
                                                    ADActivity.class).putExtra(
                                                    "url", url));
                                        }
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mv_mainfragmentcontent_verticalbanner1.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        mv_mainfragmentcontent_verticalbanner1.stopFlipping();
    }

    private void findView() {
        Log.e("TAG", "MAIN");
        ll_mainfrag_pop = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_pop);
        ll_mainfrag_top = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_top);
        iv_mainfragment_dognames = (ImageView) view
                .findViewById(R.id.iv_mainfragment_dognames);
        ll_mainfrag_yue_pop = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_yue_pop);
        ll_mainfrag_guantou_pop = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_guantou_pop);
        ll_mainfrag_yhq_pop = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_yhq_pop);
        /*ll_mainfrag_kabao_pop = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_kabao_pop);*/
        ll_mainfrag_return = view.findViewById(R.id.ll_mainfrag_return);
        ll_mainfrag_return_pop = view.findViewById(R.id.ll_mainfrag_return_pop);
        tv_mainfrag_return_pop = view.findViewById(R.id.tv_mainfrag_return_pop);
        tv_mainfrag_yue_pop = (TextView) view
                .findViewById(R.id.tv_mainfrag_yue_pop);
        tv_mainfrag_guantou_pop = (TextView) view
                .findViewById(R.id.tv_mainfrag_guantou_pop);
        tv_mainfrag_yhq_pop = (TextView) view
                .findViewById(R.id.tv_mainfrag_yhq_pop);
       /* tv_mainfrag_kabao_pop = (TextView) view
                .findViewById(R.id.tv_mainfrag_kabao_pop);*/
        iv_mainfragment_banner3 = (ImageView) view
                .findViewById(R.id.iv_mainfragment_banner3);
        rl_mainfragment_banner3 = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_banner3);
        iv_mainfrag_userimgbottom = (ImageView) view
                .findViewById(R.id.iv_mainfrag_userimgbottom);
        iv_mainfrag_huiyuan = (ImageView) view
                .findViewById(R.id.iv_mainfrag_huiyuan);
        ll_mainfrag_yue = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_yue);
        ll_mainfrag_guantou = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_guantou);
        ll_mainfrag_yhq = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_yhq);
        ll_mainfrag_kabao = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_kabao);
        tv_mainfrag_guantou = (TextView) view
                .findViewById(R.id.tv_mainfrag_guantou);
        rl_mainfrag_top1 = (RelativeLayout) view
                .findViewById(R.id.rl_mainfrag_top1);
        ll_mainfragmentcontent_ad = (LinearLayout) view
                .findViewById(R.id.ll_mainfragmentcontent_ad);
        ll_mainfragmentcontent_service = (LinearLayout) view
                .findViewById(R.id.ll_mainfragmentcontent_service);
        civ_mainfrag_userimg = (ImageView) view
                .findViewById(R.id.civ_mainfrag_userimg);
        gv_mainfragmentcontent_ad = (MyGridView) view
                .findViewById(R.id.gv_mainfragmentcontent_ad);
        vw_mainfrag_hotbeau = (View) view
                .findViewById(R.id.vw_mainfrag_hotbeau);
        rl_mainfragment_verticalbanner2 = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_verticalbanner2);
        btn_mainfragmentcontent_verticalbanner2 = (Button) view
                .findViewById(R.id.btn_mainfragmentcontent_verticalbanner2);
        vbv_mainfragmentcontent_verticalbanner2 = (VerticalBannerView) view
                .findViewById(R.id.vbv_mainfragmentcontent_verticalbanner2);
        rl_Banner1 = (RelativeLayout) view.findViewById(R.id.rl_Banner1);
        tv_mainfrag_banner1 = (TextView) view
                .findViewById(R.id.tv_mainfrag_banner1);
        rl_mainfragment_verticalbanner1 = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_verticalbanner1);
        btn_mainfragmentcontent_verticalbanner1 = (Button) view
                .findViewById(R.id.btn_mainfragmentcontent_verticalbanner1);
        btn_mainfeag_login = (Button) view
                .findViewById(R.id.btn_mainfeag_login);
        rl_mainfragment_wdl = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_wdl);
        ll_mainfragment_ydl = (RelativeLayout) view
                .findViewById(R.id.ll_mainfragment_ydl);
        tv_mainfrag_return = view.findViewById(R.id.tv_mainfrag_return);
        tv_mainfragment_username = (TextView) view
                .findViewById(R.id.tv_mainfragment_username);
        rl_mainfragment_dog = (LinearLayout) view
                .findViewById(R.id.rl_mainfragment_dog);
        tv_mainfragment_dognames = (TextView) view
                .findViewById(R.id.tv_mainfragment_dognames);
        rl_mainfragment_cat = (LinearLayout) view
                .findViewById(R.id.rl_mainfragment_cat);
        tv_mainfragment_catnames = (TextView) view
                .findViewById(R.id.tv_mainfragment_catnames);
        ll_mainfrag_userinfo = (LinearLayout) view
                .findViewById(R.id.ll_mainfrag_userinfo);
        tv_mainfrag_yue = (TextView) view.findViewById(R.id.tv_mainfrag_yue);
        tv_mainfrag_yhq = (TextView) view.findViewById(R.id.tv_mainfrag_yhq);
        tv_mainfrag_kabao = (TextView) view
                .findViewById(R.id.tv_mainfrag_kabao);
        osv_mainfrag = (ObservableScrollView) view
                .findViewById(R.id.osv_mainfrag);
        mlv_mainfragment_service = (MListview) view
                .findViewById(R.id.mlv_mainfragment_service);
        rl_mainfragment_tsfw = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_tsfw);
        tv_mainfragment_tsfw = (TextView) view
                .findViewById(R.id.tv_mainfragment_tsfw);
        vw_mainfragment_tsfw = (View) view
                .findViewById(R.id.vw_mainfragment_tsfw);
        rl_mainfragment_tjyy = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_tjyy);
        tv_mainfragment_tjyy = (TextView) view
                .findViewById(R.id.tv_mainfragment_tjyy);
        vw_mainfragment_tjyy = (View) view
                .findViewById(R.id.vw_mainfragment_tjyy);
        rl_mainfragment_cwbk = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_cwbk);
        tv_mainfragment_cwbk = (TextView) view
                .findViewById(R.id.tv_mainfragment_cwbk);
        vw_mainfragment_cwbk = (View) view
                .findViewById(R.id.vw_mainfragment_cwbk);
        mlv_mainfragment_petcircle = (MListview) view
                .findViewById(R.id.mlv_mainfragment_petcircle);
        hlv_mainfragment_beautician = (HorizontalListView) view
                .findViewById(R.id.hlv_mainfragment_beautician);
        mv_mainfragmentcontent_verticalbanner1 = (MarqueeView) view
                .findViewById(R.id.mv_mainfragmentcontent_verticalbanner1);
        rlHotBeautician = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragmentcontent_hotbeautician);
        gvServices = (MyGridView) view
                .findViewById(R.id.gv_mainfragmentcontent_service);
        rl_mainfragment_jyzb = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_jyzb);
        rl_mainfragment_jyzb_delete = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragment_jyzb_delete);
        tv_mainfragment_jyzb_name = (TextView) view
                .findViewById(R.id.tv_mainfragment_jyzb_name);
        rl_mainfragmentcontent_community = (RelativeLayout) view
                .findViewById(R.id.rl_mainfragmentcontent_community);
        tv_mainfragmentcontent_community_other = (TextView) view
                .findViewById(R.id.tv_mainfragmentcontent_community_other);
        iv_mainfragmentcontent_community_other = (ImageView) view
                .findViewById(R.id.iv_mainfragmentcontent_community_other);
        vw_mainfragmentcontent_community_other = (View) view
                .findViewById(R.id.vw_mainfragmentcontent_community_other);
        rl_mainfragment_pay = view.findViewById(R.id.rl_mainfragment_pay);
        rvpBanner1 = (RollPagerView) view.findViewById(R.id.rvpBanner1);
        textview_unlogin_content = (TextView) view.findViewById(R.id.textview_unlogin_content);
        rl_mainfrag_userimg = (RelativeLayout) view.findViewById(R.id.rl_mainfrag_userimg);
    }

    /**
     * 自动登录获取数据
     */
    private void autoLogin(double lat, double lng) {
        if (Utils.checkLogin(mActivity)) {
            CommUtil.loginAuto(mActivity, spUtil.getString("cellphone", ""),
                    Global.getIMEI(mActivity),
                    Global.getCurrentVersion(mActivity),
                    spUtil.getInt("userid", 0), lat, lng, autoLoginHandler);
        }
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
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
                            if (jUser.has("memberLevelId") && !jUser.isNull("memberLevelId")) {
                                int memberLevelId = jUser.getInt("memberLevelId");
                                Log.e("TAG", "memberLevelId = " + memberLevelId);
                                spUtil.saveInt("shopCartMemberLevelId", memberLevelId);
                                if (memberLevelId == 1) {
                                    iv_mainfrag_userimgbottom.setVisibility(View.GONE);
                                    iv_mainfrag_userimgbottom.bringToFront();
                                    iv_mainfrag_userimgbottom.setImageResource(R.drawable.copper_level_icon);
                                } else if (memberLevelId == 2) {
                                    iv_mainfrag_userimgbottom.bringToFront();
                                    iv_mainfrag_userimgbottom.setVisibility(View.GONE);
                                    iv_mainfrag_userimgbottom.setImageResource(R.drawable.silver_level_icon);
                                } else if (memberLevelId == 3) {
                                    iv_mainfrag_userimgbottom.bringToFront();
                                    iv_mainfrag_userimgbottom.setVisibility(View.GONE);
                                    iv_mainfrag_userimgbottom.setImageResource(R.drawable.gold_level_icon);
                                } else {
                                    iv_mainfrag_userimgbottom.setVisibility(View.GONE);
                                }
                            } else {
                                iv_mainfrag_userimgbottom.setVisibility(View.GONE);
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
                            if (jUser.has("petKinds")&&!jUser.isNull("petKinds")){
                                JSONArray petKinds = jUser.getJSONArray("petKinds");
                                if (petKinds.length()>0){
                                    if (petKinds.length()>=2){
                                        spUtil.saveInt("petkind",1);
                                    }else {
                                        spUtil.saveInt("petkind",petKinds.getInt(0));
                                    }
                                }else {
                                    spUtil.saveInt("petkind",1);
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
                            if (jUser.has("live") && !jUser.isNull("live")) {
                                JSONObject jlive = jUser.getJSONObject("live");
                                if (jlive.has("tag") && !jlive.isNull("tag")) {
                                    liveTag = jlive.getString("tag");
                                    boolean isExit = spUtil.getBoolean(
                                            "isExit", false);
                                    if (!TextUtils.isEmpty(liveTag)) {
                                        if (isExit) {
                                            tv_mainfragment_jyzb_name
                                                    .setText(liveTag);
                                            rl_mainfragment_jyzb
                                                    .setVisibility(View.VISIBLE);
                                            mHandler.sendEmptyMessageDelayed(
                                                    Global.LIVEDELAYEDGONE,
                                                    5000);
                                        } else {
                                            rl_mainfragment_jyzb
                                                    .setVisibility(View.GONE);
                                        }
                                    } else {
                                        rl_mainfragment_jyzb
                                                .setVisibility(View.GONE);
                                    }
                                }
                                if (jlive.has("url") && !jlive.isNull("url")) {
                                    liveUrl = jlive.getString("url");
                                }
                            }
                        }
                    }
                } else {
                    if (resultCode == Global.EXIT_USER_CODE) {
                        ll_mainfragment_ydl.setVisibility(View.GONE);
                        rl_mainfragment_wdl.setVisibility(View.VISIBLE);
                        ll_mainfrag_userinfo.setVisibility(View.GONE);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_mainfrag_top.getLayoutParams();
                        layoutParams.height = (int) mActivity.getResources().getDimension(R.dimen.dp_75);
                        ll_mainfrag_top.setLayoutParams(layoutParams);
                        rl_mainfragment_verticalbanner1.setVisibility(View.GONE);
                    }
                    Utils.Exit(mActivity, resultCode);
                }
            } catch (JSONException e) {
            }
            if (isNewComer > 0 && !spUtil.getBoolean("tag_isNewComer",false)) {//存在这个值表示是新用户注册，执行跳转
                spUtil.saveBoolean("tag_isNewComer",true);
                startActivity(new Intent(getActivity(), NewUserGiveCouponActivity.class));
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void setView() {
        if (Utils.checkLogin(mActivity)) {
            ll_mainfragment_ydl.setVisibility(View.VISIBLE);
            rl_mainfragment_wdl.setVisibility(View.GONE);
            ll_mainfrag_userinfo.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_mainfrag_top.getLayoutParams();
            layoutParams.height = (int) mActivity.getResources().getDimension(R.dimen.dp_139);
            ll_mainfrag_top.setLayoutParams(layoutParams);
        } else {
            ll_mainfragment_ydl.setVisibility(View.GONE);
            rl_mainfragment_wdl.setVisibility(View.VISIBLE);
            ll_mainfrag_userinfo.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_mainfrag_top.getLayoutParams();
            layoutParams.height = (int) mActivity.getResources().getDimension(R.dimen.dp_75);
            ll_mainfrag_top.setLayoutParams(layoutParams);
            rl_mainfragment_verticalbanner1.setVisibility(View.GONE);
        }
        mainPetCircleAdapter = new MainPetCircleAdapter<MainPetCircle>(
                mActivity, listPetCircle);
        mlv_mainfragment_petcircle.setAdapter(mainPetCircleAdapter);
        adapterMainService = new MainServiceAdapter(mActivity, listMainService);
        gvServices.setAdapter(adapterMainService);
        adapterBanner1 = new BannerAdapter(listBanner1);
        rvpBanner1.setAdapter(adapterBanner1);
        mainAdAdapter = new MainAdAdapter<Banner>(mActivity, listBanner2);
        gv_mainfragmentcontent_ad.setAdapter(mainAdAdapter);
        hotBeauticianAdapter = new HotBeauticianAdapter<Beautician>(mActivity,
                listHotBeautician);
        hlv_mainfragment_beautician.setAdapter(hotBeauticianAdapter);
        mainPetEncyclopediaAdapter = new MainPetEncyclopediaAdapter<MainPetEncyclopedia>(
                mActivity, listPetEncyclopedia);
        mainHospitalAdapter = new MainHospitalAdapter<MainHospital>(mActivity,
                listHospital);
        mainCharacteristicServiceAdapter = new MainCharacteristicServiceAdapter<MainCharacteristicService>(
                mActivity, listCharacteristicService, 1);
        clearList();
    }

    private void initBD() {
        mLocationClient = new LocationClient(mActivity);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void goNext(Class clazz, int id, int type) {
        Intent intent = new Intent(mActivity, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
                Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
        startActivity(intent);
    }

    private void goNext(Class clazz, int serviceid, int servicetype,
                        int previous, int petid) {
        Intent intent = new Intent(mActivity, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(),
                Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("serviceid", serviceid);
        intent.putExtra("servicetype", servicetype);
        intent.putExtra("previous", previous);
        intent.putExtra("petid", petid);
        startActivity(intent);
    }

    private void getHomeIndex() {
        String home_index_data = spUtil.getString("HOME_INDEX_DATA", "");
        if (Utils.isStrNull(home_index_data)) {
            processData(home_index_data);
        }
        pDialog.showDialog();
        CommUtil.getNewHomeData(mActivity, spUtil.getInt("cityId",0), spUtil.getInt("isFirstLogin", 0),Double.parseDouble(spUtil.getString("lat_home","0")),Double.parseDouble(spUtil.getString("lng_home","0")), homeIndexHandler);
    }

    private AsyncHttpResponseHandler homeIndexHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            processData(new String(responseBody));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            String home_index_data = spUtil.getString("HOME_INDEX_DATA", "");
            if (Utils.isStrNull(home_index_data)) {
                processData(home_index_data);
            }
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
        }
    };

    // 解析数据
    private void processData(String str) {
        try {
            JSONObject jobj = new JSONObject(str);
            int code = jobj.getInt("code");
            if (code == 0) {
                if (jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata.has("user") && !jdata.isNull("user")) {// 用户信息
                        JSONObject juser = jdata.getJSONObject("user");
                        if (juser.has("canBillNum") && !juser.isNull("canBillNum")) {
                            int canBillNum = juser.getInt("canBillNum");
                            Utils.setText(tv_mainfrag_guantou, canBillNum + "", "0", View.VISIBLE, View.VISIBLE);
                        }
                        if (juser.has("tips") && !juser.isNull("tips")) {
                            tips = juser.getString("tips");
                        }
                        if (juser.has("type") && !juser.isNull("type")) {
                            int type = juser.getInt("type");
                            int localType = spUtil.getInt("MAINFRAG_TIP_TYPE", -1);
                            String localTip = spUtil.getString("MAINFRAG_TIP", "");
                            if (type != 0) {
                                if (type == localType) {
                                    if (!tips.equals(localTip)) {
                                        showSwitchPop(type, tips, true);
                                    } else {
                                        showSwitchPop(type, tips, false);
                                    }
                                } else {
                                    showSwitchPop(type, tips, true);
                                }
                            }
                            spUtil.saveString("MAINFRAG_TIP", tips);
                            spUtil.saveInt("MAINFRAG_TIP_TYPE", type);
                        }
                        if (juser.has("isVIP") && !juser.isNull("isVIP")) {
                            isVip = juser.getInt("isVIP");
                        }
                        if (juser.has("userName") && !juser.isNull("userName")) {
                            String userName = juser.getString("userName");
                            if (Utils.isStrNull(userName)) {
                                spUtil.saveString("username", userName);
                                tv_mainfragment_username.setText(userName);
                            } else {
                                tv_mainfragment_username.setText("未填写昵称");
                            }
                        } else {
                            tv_mainfragment_username.setText("未填写昵称");
                        }
                        if (juser.has("balance") && !juser.isNull("balance")) {
                            int balance = juser.getInt("balance");
                            Utils.setText(tv_mainfrag_yue, balance + "", "0", View.VISIBLE, View.INVISIBLE);
                        }
                        if (juser.has("coupons") && !juser.isNull("coupons")) {
                            int coupons = juser.getInt("coupons");
                            Utils.setText(tv_mainfrag_yhq, coupons + "", "0",
                                    View.VISIBLE, View.INVISIBLE);
                        }
                        if (juser.has("cashback")&&!juser.isNull("cashback")){
                            tv_mainfrag_return.setText(Utils.formatDecimal(juser.getDouble("cashback"))+"");
                        }
                        if (juser.has("cards") && !juser.isNull("cards")) {
                            int cards = juser.getInt("cards");
                            Utils.setText(tv_mainfrag_kabao, cards + "", "0",
                                    View.VISIBLE, View.INVISIBLE);
                        }
                        if (juser.has("avatar") && !juser.isNull("avatar")) {
                            String avatar = juser.getString("avatar");
                            spUtil.saveString("userimage", avatar);
                            GlideUtil.loadCircleImg(mActivity, avatar,
                                    civ_mainfrag_userimg,
                                    R.drawable.user_icon_unnet_circle);
                        }
                        if (juser.has("myPets") && !juser.isNull("myPets")) {
                            JSONObject jmyPets = juser.getJSONObject("myPets");
                            if (jmyPets.has("dogs") && !jmyPets.isNull("dogs")) {
                                JSONArray jdogsarr = jmyPets
                                        .getJSONArray("dogs");
                                if (jdogsarr.length() > 0) {
                                    dogNames.setLength(0);
                                    for (int i = 0; i < jdogsarr.length(); i++) {
                                        JSONObject jdog = jdogsarr
                                                .getJSONObject(i);
                                        if (jdog.has("nickName")
                                                && !jdog.isNull("nickName")) {
                                            if (i < jdogsarr.length() - 1) {
                                                dogNames.append(jdog
                                                        .getString("nickName")
                                                        + ",");
                                            } else {
                                                dogNames.append(jdog
                                                        .getString("nickName"));
                                            }
                                        }
                                    }
                                }
                            }
                            if (jmyPets.has("cats") && !jmyPets.isNull("cats")) {
                                JSONArray jcatsarr = jmyPets
                                        .getJSONArray("cats");
                                if (jcatsarr.length() > 0) {
                                    catNames.setLength(0);
                                    for (int i = 0; i < jcatsarr.length(); i++) {
                                        JSONObject jcat = jcatsarr
                                                .getJSONObject(i);
                                        if (jcat.has("nickName")
                                                && !jcat.isNull("nickName")) {
                                            if (i < jcatsarr.length() - 1) {
                                                catNames.append(jcat
                                                        .getString("nickName")
                                                        + ",");
                                            } else {
                                                catNames.append(jcat
                                                        .getString("nickName"));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (jdata.has("justNow") && !jdata.isNull("justNow")) {// 实时动态
                        JSONArray jarrjustNow = jdata.getJSONArray("justNow");
                        if (jarrjustNow.length() > 0) {
                            rl_mainfragment_verticalbanner2
                                    .setVisibility(View.VISIBLE);
                            listJustNow.clear();
                            for (int i = 0; i < jarrjustNow.length(); i++) {
                                listJustNow.add(JustNow.json2Entity(jarrjustNow
                                        .getJSONObject(i)));
                            }
                            try {
                                justNowAdapter = new JustNowAdapter(listJustNow);
                                vbv_mainfragmentcontent_verticalbanner2
                                        .setAdapter(justNowAdapter);
                                vbv_mainfragmentcontent_verticalbanner2.start();
                                justNowAdapter
                                        .setOnItemClickListener(new JustNowAdapter.OnItemClickListener() {
                                            @Override
                                            public void OnItemClick(JustNow data) {
                                                Utils.goService(mActivity, data.point,
                                                        data.backup);
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            rl_mainfragment_verticalbanner2
                                    .setVisibility(View.GONE);
                        }
                    } else {
                        rl_mainfragment_verticalbanner2
                                .setVisibility(View.GONE);
                    }
                    if (jdata.has("recommendation")
                            && !jdata.isNull("recommendation")) {// 个性化推荐
                        JSONArray jarrrecommendation = jdata
                                .getJSONArray("recommendation");
                        if (jarrrecommendation.length() > 0) {
                            listRecommendation.clear();
                            for (int i = 0; i < jarrrecommendation.length(); i++) {
                                listRecommendation.add(Recommendation
                                        .json2Entity(jarrrecommendation
                                                .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("banner2") && !jdata.isNull("banner2")) {
                        // 中部的俩运营图片
                        JSONArray jbannerarr2 = jdata.getJSONArray("banner2");
                        if (jbannerarr2.length() > 0) {
                            listBanner2.clear();
                            for (int i = 0; i < jbannerarr2.length(); i++) {
                                listBanner2.add(Banner.json2Entity(jbannerarr2
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("banner3") && !jdata.isNull("banner3")) {
                        // 中部的俩运营图片
                        JSONArray jbannerarr3 = jdata.getJSONArray("banner3");
                        if (jbannerarr3.length() > 0) {
                            listBanner3.clear();
                            for (int i = 0; i < jbannerarr3.length(); i++) {
                                listBanner3.add(Banner.json2Entity(jbannerarr3
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("hpcBannerList")
                            && !jdata.isNull("hpcBannerList")) {// 首页宠圈
                        JSONArray jarrPetCircle = jdata
                                .getJSONArray("hpcBannerList");
                        if (jarrPetCircle.length() > 0) {
                            listPetCircle.clear();
                            for (int i = 0; i < jarrPetCircle.length(); i++) {
                                listPetCircle.add(MainPetCircle
                                        .json2Entity(jarrPetCircle
                                                .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("homePageCopy")
                            && !jdata.isNull("homePageCopy")) {// 下方三个title的文案
                        JSONObject jobjHomePageCopy = jdata
                                .getJSONObject("homePageCopy");
                        if (jobjHomePageCopy.has("1")
                                && !jobjHomePageCopy.isNull("1")) {
                            String string = jobjHomePageCopy.getString("1");
                            Utils.setText(tv_mainfragment_tsfw, string, "",
                                    View.VISIBLE, View.VISIBLE);
                        }
                        if (jobjHomePageCopy.has("2")
                                && !jobjHomePageCopy.isNull("2")) {
                            String string = jobjHomePageCopy.getString("2");
                            Utils.setText(tv_mainfragment_tjyy, string, "",
                                    View.GONE, View.GONE);
                        }
                        if (jobjHomePageCopy.has("3")
                                && !jobjHomePageCopy.isNull("3")) {
                            String string = jobjHomePageCopy.getString("3");
                            Utils.setText(tv_mainfragment_cwbk, string, "",
                                    View.GONE, View.GONE);
                        }
                    }
                    if (jdata.has("services") && !jdata.isNull("services")) {// 特色服务列表
                        JSONArray jarrCharacteristicService = jdata
                                .getJSONArray("services");
                        if (jarrCharacteristicService.length() > 0) {
                            listCharacteristicService.clear();
                            for (int i = 0; i < jarrCharacteristicService
                                    .length(); i++) {
                                listCharacteristicService
                                        .add(MainCharacteristicService
                                                .json2Entity(jarrCharacteristicService
                                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("hospitals") && !jdata.isNull("hospitals")) {// 医疗保险
                        JSONArray jarrHospital = jdata
                                .getJSONArray("hospitals");
                        if (jarrHospital.length() > 0) {
                            listHospital.clear();
                            for (int i = 0; i < jarrHospital.length(); i++) {
                                listHospital.add(MainHospital
                                        .json2Entity(jarrHospital
                                                .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("knowledges") && !jdata.isNull("knowledges")) {// 知识库
                        JSONArray jarrPetEncyclopedia = jdata
                                .getJSONArray("knowledges");
                        if (jarrPetEncyclopedia.length() > 0) {
                            listPetEncyclopedia.clear();
                            for (int i = 0; i < jarrPetEncyclopedia.length(); i++) {
                                listPetEncyclopedia.add(MainPetEncyclopedia
                                        .json2Entity(jarrPetEncyclopedia
                                                .getJSONObject(i)));
                            }
                        }
                    }
                    if (!Utils.checkLogin(mActivity)) {
                        if (jdata.has("waitingLoginTip") && !jdata.isNull("waitingLoginTip")) {
                            textview_unlogin_content.setText("宠物家" + jdata.getString("waitingLoginTip"));
                        }
                        rl_mainfrag_userimg.setVisibility(View.GONE);
                    } else {
                        rl_mainfrag_userimg.setVisibility(View.VISIBLE);
                    }
                    if (jdata.has("icons") && !jdata.isNull("icons")) {// 服务入口
                        JSONArray jiconarr = jdata.getJSONArray("icons");
                        if (jiconarr.length() > 0) {
                            listMainService.clear();
                            for (int i = 0; i < jiconarr.length(); i++) {
                                JSONObject jicon = jiconarr.getJSONObject(i);
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
                            }
                        }
                    }
                    if (jdata.has("topWorkers")
                            && !jdata.isNull("topWorkers")
                            && Utils.JSON_TYPE.JSON_TYPE_OBJECT
                            .equals(Utils.getJSONType(jdata
                                    .getString("topWorkers")))) {// 热门美容师
                        JSONObject topWorkersjsonObject = jdata
                                .getJSONObject("topWorkers");
                        if (topWorkersjsonObject.has("workers")
                                && !topWorkersjsonObject.isNull("workers")) {
                            JSONArray workersjservicearr = topWorkersjsonObject
                                    .getJSONArray("workers");
                            if (workersjservicearr.length() > 0) {
                                listHotBeautician.clear();
                                for (int i = 0; i < workersjservicearr.length(); i++) {
                                    Beautician btc = Beautician
                                            .json2Entity(workersjservicearr
                                                    .getJSONObject(i));
                                    if (i <= 2) {
                                        btc.colorId = i;
                                    } else {
                                        if (listHotBeautician.get(i - 1).colorId == 0) {
                                            btc.colorId = 1;
                                        } else if (listHotBeautician.get(i - 1).colorId == 1) {
                                            btc.colorId = 2;
                                        } else if (listHotBeautician.get(i - 1).colorId == 2) {
                                            btc.colorId = 0;
                                        }
                                    }
                                    listHotBeautician.add(btc);
                                }
                            }
                        }
                    }
                    if (jdata.has("banner1") && !jdata.isNull("banner1")) {
                        JSONArray jbannerarr1 = jdata.getJSONArray("banner1");
                        if (jbannerarr1.length() > 0) {
                            listBanner1.clear();
                            for (int i = 0; i < jbannerarr1.length(); i++) {
                                listBanner1.add(Banner.json2Entity(jbannerarr1
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("homePostCustomInfo")
                            && !jdata.isNull("homePostCustomInfo")) {// 首页宠圈上方一条
                        tv_mainfragmentcontent_community_other
                                .setVisibility(View.VISIBLE);
                        iv_mainfragmentcontent_community_other
                                .setVisibility(View.VISIBLE);
                        JSONObject homePostCustomInfo = jdata
                                .getJSONObject("homePostCustomInfo");
                        if (homePostCustomInfo.has("circleId")
                                && !homePostCustomInfo.isNull("circleId")) {
                            circleId = homePostCustomInfo.getInt("circleId");
                        }
                        if (homePostCustomInfo.has("img")
                                && !homePostCustomInfo.isNull("img")) {
                            if (homePostCustomInfo.has("title")
                                    && !homePostCustomInfo.isNull("title")) {
                                android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        120, 55);
                                layoutParams.rightMargin = 20;
                                iv_mainfragmentcontent_community_other
                                        .setLayoutParams(layoutParams);
                            } else {
                                iv_mainfragmentcontent_community_other
                                        .setLayoutParams(new LinearLayout.LayoutParams(
                                                LayoutParams.MATCH_PARENT, 55));
                            }
                            iv_mainfragmentcontent_community_other
                                    .setVisibility(View.VISIBLE);
                            String img = homePostCustomInfo.getString("img");
                            GlideUtil.loadImg(mActivity, img,
                                    iv_mainfragmentcontent_community_other,
                                    R.drawable.icon_production_default);
                        } else {
                            iv_mainfragmentcontent_community_other
                                    .setVisibility(View.GONE);
                        }
                        if (homePostCustomInfo.has("title")
                                && !homePostCustomInfo.isNull("title")) {
                            boolean TAG_vw_mainfragmentcontent_community_other_click = spUtil
                                    .getBoolean(
                                            "TAG_vw_mainfragmentcontent_community_other_click",
                                            false);
                            if (!TAG_vw_mainfragmentcontent_community_other_click) {
                                vw_mainfragmentcontent_community_other
                                        .setVisibility(View.VISIBLE);
                            }
                            tv_mainfragmentcontent_community_other
                                    .setVisibility(View.VISIBLE);
                            String title = homePostCustomInfo
                                    .getString("title");
                            Utils.setStringTextGone(
                                    tv_mainfragmentcontent_community_other,
                                    title);
                        } else {
                            tv_mainfragmentcontent_community_other
                                    .setVisibility(View.GONE);
                        }
                        if (homePostCustomInfo.has("type")
                                && !homePostCustomInfo.isNull("type")) {
                            circleType = homePostCustomInfo.getInt("type");
                        }
                    } else {
                        tv_mainfragmentcontent_community_other
                                .setVisibility(View.GONE);
                        iv_mainfragmentcontent_community_other
                                .setVisibility(View.GONE);
                    }
                    if (spUtil.getBoolean("selectMain", false) == true) {
                        spUtil.saveBoolean("selectMain", false);
                        osv_mainfrag.fullScroll(ScrollView.FOCUS_UP);
                    } else {
                        osv_mainfrag.scrollTo(scrolledX, scrolledY);
                    }
                    spUtil.saveString("HOME_INDEX_DATA", str);
                }
            } else {
                if (code == Global.EXIT_USER_CODE) {
                    ll_mainfragment_ydl.setVisibility(View.GONE);
                    rl_mainfragment_wdl.setVisibility(View.VISIBLE);
                    ll_mainfrag_userinfo.setVisibility(View.GONE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_mainfrag_top.getLayoutParams();
                    layoutParams.height = (int) mActivity.getResources().getDimension(R.dimen.dp_75);
                    ll_mainfrag_top.setLayoutParams(layoutParams);
                    rl_mainfragment_verticalbanner1.setVisibility(View.GONE);
                }
                Utils.Exit(mActivity, code);
                if (jobj.has("msg") && !jobj.isNull("msg")) {
                    ToastUtil.showToastShortCenter(mActivity,
                            jobj.getString("msg"));
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "shouyejiekou数据异常E = " + e.toString());
            ToastUtil.showToastShortBottom(mActivity, "数据异常");
            e.printStackTrace();
        }
        writeData();
    }

    private void writeData() {
        rl_mainfragmentcontent_community.setVisibility(View.VISIBLE);
        setIndex(bottomIndex);
        if (listBanner2 != null && listBanner2.size() > 0) {
            BannerImgUrls2 = new String[listBanner2.size()];
            for (int i = 0; i < this.listBanner2.size(); i++) {
                Banner banner = this.listBanner2.get(i);
                if (banner != null) {
                    if (banner.pic != null && !TextUtils.isEmpty(banner.pic)) {
                        BannerImgUrls2[i] = banner.pic;
                    }
                }
            }
            mainAdAdapter.setData(listBanner2);
            ll_mainfragmentcontent_ad.setVisibility(View.VISIBLE);
        } else {
            ll_mainfragmentcontent_ad.setVisibility(View.GONE);
        }
        if (listBanner3 != null && listBanner3.size() > 0) {
            Banner banner = listBanner3.get(0);
            if (banner != null) {
                GlideUtil.loadImg(mActivity, banner.pic,
                        iv_mainfragment_banner3,
                        R.drawable.icon_production_default);
                rl_mainfragment_banner3.setVisibility(View.VISIBLE);
            } else {
                rl_mainfragment_banner3.setVisibility(View.GONE);
            }
        } else {
            rl_mainfragment_banner3.setVisibility(View.GONE);
        }
        if (listRecommendation != null && listRecommendation.size() > 0) {
            listRecommendationStr.clear();
            rl_mainfragment_verticalbanner1.setVisibility(View.VISIBLE);
            for (int i = 0; i < listRecommendation.size(); i++) {
                Recommendation recommendation = listRecommendation.get(i);
                if (recommendation != null) {
                    listRecommendationStr.add(recommendation.content);
                }
            }
            mv_mainfragmentcontent_verticalbanner1
                    .startWithList(listRecommendationStr);
            mv_mainfragmentcontent_verticalbanner1.start();
        } else {
            rl_mainfragment_verticalbanner1.setVisibility(View.GONE);
        }
        if (dogNames.length() <= 0 && catNames.length() <= 0) {
            iv_mainfragment_dognames.setVisibility(View.GONE);
            rl_mainfragment_dog.setVisibility(View.VISIBLE);
            rl_mainfragment_cat.setVisibility(View.GONE);
            Utils.setText(tv_mainfragment_dognames, "暂未添加宠物", "",
                    View.VISIBLE, View.VISIBLE);
        } else if (dogNames.length() > 0 && catNames.length() <= 0) {
            rl_mainfragment_dog.setVisibility(View.VISIBLE);
            iv_mainfragment_dognames.setVisibility(View.VISIBLE);
            rl_mainfragment_cat.setVisibility(View.GONE);
            Utils.setText(tv_mainfragment_dognames, dogNames.toString(), "",
                    View.VISIBLE, View.VISIBLE);
        } else if (dogNames.length() <= 0 && catNames.length() > 0) {
            rl_mainfragment_dog.setVisibility(View.GONE);
            rl_mainfragment_cat.setVisibility(View.VISIBLE);
            Utils.setText(tv_mainfragment_catnames, catNames.toString(), "",
                    View.VISIBLE, View.VISIBLE);
        } else if (dogNames.length() > 0 && catNames.length() > 0) {
            rl_mainfragment_cat.setVisibility(View.VISIBLE);
            Utils.setText(tv_mainfragment_catnames, catNames.toString(), "",
                    View.VISIBLE, View.VISIBLE);
            rl_mainfragment_dog.setVisibility(View.VISIBLE);
            iv_mainfragment_dognames.setVisibility(View.VISIBLE);
            rl_mainfragment_cat.setVisibility(View.VISIBLE);
            Utils.setText(tv_mainfragment_dognames, dogNames.toString(), "",
                    View.VISIBLE, View.VISIBLE);
        }
        if (listPetCircle != null && listPetCircle.size() > 0) {
            mlv_mainfragment_petcircle.setVisibility(View.VISIBLE);
            mainPetCircleAdapter.setData(listPetCircle);
        } else {
            mlv_mainfragment_petcircle.setVisibility(View.GONE);
        }
        if (listHotBeautician != null && listHotBeautician.size() > 0) {
            vw_mainfrag_hotbeau.setVisibility(View.VISIBLE);
            rlHotBeautician.setVisibility(View.VISIBLE);
            hlv_mainfragment_beautician.setVisibility(View.VISIBLE);
            hotBeauticianAdapter.setData(listHotBeautician);
        } else {
            vw_mainfrag_hotbeau.setVisibility(View.GONE);
            rlHotBeautician.setVisibility(View.GONE);
            hlv_mainfragment_beautician.setVisibility(View.GONE);
        }
        if (listBanner1 != null && listBanner1.size() > 0) {
            String text = 1 + "/" + listBanner1.size() + "全部";
            SpannableString ss = new SpannableString(text);
            ss.setSpan(new ForegroundColorSpan(mActivity.getResources()
                            .getColor(R.color.aCD484D)), 0, String.valueOf(1).length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_mainfrag_banner1.setText(ss);
            BannerImgUrls1 = new String[listBanner1.size()];
            for (int i = 0; i < this.listBanner1.size(); i++) {
                Banner banner = this.listBanner1.get(i);
                if (banner != null) {
                    if (banner.pic != null && !TextUtils.isEmpty(banner.pic)) {
                        BannerImgUrls1[i] = banner.pic;
                    }
                }
            }
            if (listBanner1.size() > 1) {
                rvpBanner1.setHintView(null);
            }
            rl_Banner1.setVisibility(View.VISIBLE);
            adapterBanner1.setData(listBanner1);
        } else {
            rl_Banner1.setVisibility(View.GONE);
        }
        if (listMainService != null && listMainService.size() > 0) {
            ll_mainfragmentcontent_service.setVisibility(View.VISIBLE);
            adapterMainService.setData(listMainService);
        } else {
            ll_mainfragmentcontent_service.setVisibility(View.VISIBLE);
        }
        osv_mainfrag.fullScroll(ScrollView.FOCUS_UP);
        boolean mainfrag_dialog = spUtil.getBoolean("MAINFRAG_DIALOG", false);
        if (!mainfrag_dialog) {
            spUtil.saveBoolean("MAINFRAG_DIALOG", true);
            getMainActivity();
        }else{
            // 自动登录
            autoLogin(liveLat, liveLng);
        }
    }

    private void showPopPhoto() {
        pWin = null;
        if (pWin == null) {
            final View view = View
                    .inflate(mActivity, R.layout.pw_main_ad, null);
            ImageButton ib_pw_main_close = (ImageButton) view
                    .findViewById(R.id.ib_pw_main_close);
            TextView tv_pw_main_activitynum = (TextView) view
                    .findViewById(R.id.tv_pw_main_activitynum);
            ListView lv_pw_main = (ListView) view.findViewById(R.id.lv_pw_main);
            tv_pw_main_activitynum.setText(listBanner1.size() + "个活动");
            lv_pw_main.setAdapter(new BannerPwAdapter<Banner>(mActivity,
                    listBanner1));
            pWin = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            pWin.setFocusable(true);
            pWin.setWidth(Utils.getDisplayMetrics(mActivity)[0]);
            pWin.showAtLocation(view, Gravity.CENTER, 0, 0);
            lv_pw_main.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (listBanner1 != null && listBanner1.size() > 0
                            && listBanner1.size() > position) {
                        Banner banner = listBanner1.get(position);
                        if (banner != null) {
                            boolean goService = Utils.goService(mActivity, banner.point,
                                    banner.backup);
                            if (!goService) {
                                Utils.imageBrower(mActivity, position,
                                        BannerImgUrls1);
                            }
                            if (banner.point == 21) {
                                pWin.dismiss();
                                pWin = null;
                            }
                        }
                    }
                }
            });
            ib_pw_main_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    pWin.dismiss();
                    pWin = null;
                }
            });
        }
    }

    private class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            spUtil.saveString("lat_home", location.getLatitude() + "");
            spUtil.saveString("lng_home", location.getLongitude() + "");
            mLocationClient.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (rvpBanner1 != null)
            rvpBanner1.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatIngEvent(1));
        setStatusBar();
        if (rvpBanner1 != null)
            rvpBanner1.resume();
        clearList();
        if (Utils.checkLogin(mActivity)) {
            iv_mainfrag_huiyuan.setVisibility(View.GONE);
            ll_mainfragment_ydl.setVisibility(View.VISIBLE);
            rl_mainfragment_wdl.setVisibility(View.GONE);
            ll_mainfrag_userinfo.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_mainfrag_top.getLayoutParams();
            layoutParams.height = (int) mActivity.getResources().getDimension(R.dimen.dp_139);
            ll_mainfrag_top.setLayoutParams(layoutParams);
        } else {
            iv_mainfrag_userimgbottom.setVisibility(View.GONE);
            iv_mainfrag_huiyuan.setVisibility(View.GONE);
            ll_mainfragment_ydl.setVisibility(View.GONE);
            rl_mainfragment_wdl.setVisibility(View.VISIBLE);
            ll_mainfrag_userinfo.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_mainfrag_top.getLayoutParams();
            layoutParams.height = (int) mActivity.getResources().getDimension(R.dimen.dp_75);
            ll_mainfrag_top.setLayoutParams(layoutParams);
            rl_mainfragment_verticalbanner1.setVisibility(View.GONE);
            GlideUtil.loadCircleImg(mActivity, "", civ_mainfrag_userimg,
                    R.drawable.user_icon_unnet_circle);
        }
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    private void setIndex(int num) {
        switch (num) {
            case 1:
                tv_mainfragment_tsfw.setTextColor(getResources().getColor(
                        R.color.black));
                tv_mainfragment_tjyy.setTextColor(getResources().getColor(
                        R.color.black));
                tv_mainfragment_cwbk.setTextColor(getResources().getColor(
                        R.color.black));
                vw_mainfragment_tsfw.setVisibility(View.GONE);
                vw_mainfragment_tjyy.setVisibility(View.GONE);
                vw_mainfragment_cwbk.setVisibility(View.GONE);
                mlv_mainfragment_service
                        .setAdapter(mainCharacteristicServiceAdapter);
                mainCharacteristicServiceAdapter.setData(listCharacteristicService);
                break;
            case 2:
                tv_mainfragment_tjyy.setTextColor(getResources().getColor(
                        R.color.aD1494F));
                tv_mainfragment_tsfw.setTextColor(getResources().getColor(
                        R.color.black));
                tv_mainfragment_cwbk.setTextColor(getResources().getColor(
                        R.color.black));
                vw_mainfragment_tjyy.setVisibility(View.VISIBLE);
                vw_mainfragment_tsfw.setVisibility(View.GONE);
                vw_mainfragment_cwbk.setVisibility(View.GONE);
                mlv_mainfragment_service.setAdapter(mainHospitalAdapter);
                mainHospitalAdapter.setData(listHospital);
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_recommendhospital_main);
                break;
            case 3:
                tv_mainfragment_cwbk.setTextColor(getResources().getColor(
                        R.color.aD1494F));
                tv_mainfragment_tjyy.setTextColor(getResources().getColor(
                        R.color.black));
                tv_mainfragment_tsfw.setTextColor(getResources().getColor(
                        R.color.black));
                vw_mainfragment_cwbk.setVisibility(View.VISIBLE);
                vw_mainfragment_tjyy.setVisibility(View.GONE);
                vw_mainfragment_tsfw.setVisibility(View.GONE);
                mlv_mainfragment_service.setAdapter(mainPetEncyclopediaAdapter);
                mainPetEncyclopediaAdapter.setData(listPetEncyclopedia);
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_petencyclopedias_main);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mainfragment_dog:
                if (dogNames.length() <= 0 && catNames.length() <= 0) {
                    startActivity(new Intent(mActivity, PetAddActivity.class).putExtra("previous", Global.MY_TO_ADDPET));
                }
                break;
            case R.id.btn_mainfragmentcontent_verticalbanner2:
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_PresentDynamics);
                if (listJustNow != null && listJustNow.size() > 0
                        && listJustNow.size() > verticalBanner2Position) {
                    JustNow justNow = listJustNow.get(verticalBanner2Position);
                    if (justNow != null) {
                        Utils.goService(mActivity, justNow.point, justNow.backup);
                    }
                }
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_nowmoment_main);
                break;
            case R.id.btn_mainfragmentcontent_verticalbanner1:
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_Recommend);
                Log.e("TAG",
                        "mv_mainfragmentcontent_verticalbanner1.getPosition() = "
                                + mv_mainfragmentcontent_verticalbanner1
                                .getPosition());
                if (listRecommendation != null
                        && listRecommendation.size() > 0
                        && listRecommendation.size() > mv_mainfragmentcontent_verticalbanner1
                        .getPosition()) {
                    Recommendation recommendation = listRecommendation
                            .get(mv_mainfragmentcontent_verticalbanner1
                                    .getPosition());
                    if (recommendation != null) {
                        Utils.goService(mActivity, recommendation.point, recommendation.backup);
                    }
                }
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_mypetnotice_main);
                break;
            case R.id.iv_mainfragment_banner3:
                if (listBanner3 != null && listBanner3.size() > 0) {
                    Banner banner = listBanner3.get(0);
                    if (banner != null) {
                        Utils.goService(mActivity, banner.point, banner.backup);
                    }
                }
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_vipprivilege_main);
                break;
            case R.id.ll_mainfrag_yue:
                spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK1", true);
                //ll_mainfrag_yue_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menuhide));//开始动画
                ll_mainfrag_yue_pop.setVisibility(View.INVISIBLE);
                startActivity(new Intent(mActivity, MyCardActivity.class));
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_My_MyBalance);
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_mymoney_main);
                break;
            case R.id.ll_mainfrag_guantou:
                spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK2", true);
                //ll_mainfrag_guantou_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menuhide));//开始动画
                ll_mainfrag_guantou_pop.setVisibility(View.INVISIBLE);
                startActivity(new Intent(mActivity, MyCanActivity.class));
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_mydummy_main);
                break;
            case R.id.ll_mainfrag_return:
                /*spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK5", true);
                //ll_mainfrag_return_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menuhide));//开始动画
                ll_mainfrag_return_pop.setVisibility(View.INVISIBLE);
                ll_mainfrag_pop.setVisibility(View.GONE);*/
                startActivity(new Intent(mActivity, CashbackAmountActivity.class));
                break;
            case R.id.ll_mainfrag_yhq:
                spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK3", true);
                //ll_mainfrag_yhq_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menuhide));
                ll_mainfrag_yhq_pop.setVisibility(View.INVISIBLE);
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_My_MyCoupon);
                startActivity(new Intent(mActivity, MyCouponNewActivity.class));
                break;
           /* case R.id.ll_mainfrag_kabao:
                spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK4", true);
                ll_mainfrag_kabao_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menuhide));
                ll_mainfrag_kabao_pop.setVisibility(View.GONE);
                ll_mainfrag_pop.setVisibility(View.GONE);
                startActivity(new Intent(mActivity, MyCardsActivity.class));
                break;*/

            case R.id.rl_mainfrag_top1:
                goToMy();
                break;
            case R.id.tv_mainfrag_banner1:
                showPopPhoto();
                break;
            case R.id.rl_mainfragmentcontent_hotbeautician:
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_PopularList);
                goNext(MainToBeauList.class, 0, 0, 0, 0);
                break;
            case R.id.btn_mainfeag_login:
                startActivity(new Intent(mActivity, LoginActivity.class).putExtra(
                        "previous", Global.AD_LOGIN_MAIN));
                break;
            case R.id.rl_mainfragment_pay:
                startActivity(new Intent(mActivity, QrCodeNewActivity.class));
                break;
            case R.id.rl_mainfragment_tsfw:
                bottomIndex = 1;
                setIndex(bottomIndex);
                break;
            case R.id.rl_mainfragment_tjyy:
                bottomIndex = 2;
                setIndex(bottomIndex);
                break;
            case R.id.rl_mainfragment_cwbk:
                bottomIndex = 3;
                setIndex(bottomIndex);
                break;
            case R.id.iv_mainfragmentcontent_community_other:
                goToCircle();
                break;
            case R.id.tv_mainfragmentcontent_community_other:
                spUtil.saveBoolean(
                        "TAG_vw_mainfragmentcontent_community_other_click", true);
                vw_mainfragmentcontent_community_other.setVisibility(View.GONE);
                goToCircle();
                break;
            case R.id.rl_mainfragmentcontent_community:// 跳转到社区列表
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_PetRingList);
                goToCircle();
                break;
            case R.id.rl_mainfragment_jyzb:
                // 跳转到直播列表界面
                mActivity.startActivity(new Intent(mActivity, FosterLiveListActivity.class));
                break;
            case R.id.rl_mainfragment_jyzb_delete:
                spUtil.saveBoolean("isExit", false);
                // 直播
                rl_mainfragment_jyzb.setVisibility(View.GONE);
                break;
            case R.id.iv_mainfrag_huiyuan://会员中心
                if (Utils.checkLogin(mActivity)) {
                    startActivity(new Intent(mActivity, MemberUpgradeActivity.class));
                } else {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_viparea_main);
                break;
            default:
                break;
        }
    }

    private void goToMy() {
        MainActivity mActivity = (MainActivity) getActivity();
        mActivity.mTabHost.setCurrentTabByTag("my");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AdLoginEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.getPosition() == 1) {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.isLogin()) {
            }
        }
    }

    private void goToCircle() {
        MainActivity mActivity = (MainActivity) getActivity();
        switch (circleType) {
            case 1://宠圈列表页
                mActivity.setSelect(1);
                EventBus.getDefault().post(new CircleOrSelectEvent(circleType));
                break;
            case 2://精选列表页
                mActivity.setSelect(2);
                EventBus.getDefault().post(new CircleOrSelectEvent(circleType));
                break;
            case 3://进入宠圈页
                goToPetCircle(circleId);
                break;
            default:
                break;
        }
    }

    private void goToPetCircle(int areaId) {
        circle.PostGroupId = areaId;
        Intent intent = new Intent(mActivity, PetCircleInsideActivity.class);
        intent.putExtra("petCircle", circle);
        mActivity.startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private void showSwitchPop(int type, String str, boolean bool) {//1 余额  2 罐头币   3 优惠卷  4 卡包
        ll_mainfrag_yue_pop.setVisibility(View.INVISIBLE);
        ll_mainfrag_guantou_pop.setVisibility(View.INVISIBLE);
        ll_mainfrag_yhq_pop.setVisibility(View.INVISIBLE);
        ll_mainfrag_return_pop.setVisibility(View.INVISIBLE);
        ll_mainfrag_pop.setVisibility(View.GONE);
        if (Utils.checkLogin(getActivity())) {
            switch (type) {
                case 1:
                    if (bool) {
                        ll_mainfrag_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_pop.bringToFront();
                        tv_mainfrag_yue_pop.setText(str);
                        ll_mainfrag_yue_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_yue_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menushow));//开始动画
                        spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK1", false);
                    } else {
                        if (!spUtil.getBoolean("MAINFRAG_TIP_TYPE_CLICK1", false)) {
                            ll_mainfrag_pop.setVisibility(View.VISIBLE);
                            ll_mainfrag_pop.bringToFront();
                            tv_mainfrag_yue_pop.setText(str);
                            ll_mainfrag_yue_pop.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 2:
                    if (bool) {
                        ll_mainfrag_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_pop.bringToFront();
                        tv_mainfrag_guantou_pop.setText(str);
                        ll_mainfrag_guantou_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_guantou_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menushow));//开始动画
                        spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK2", false);
                    } else {
                        if (!spUtil.getBoolean("MAINFRAG_TIP_TYPE_CLICK2", false)) {
                            tv_mainfrag_guantou_pop.setText(str);
                            ll_mainfrag_guantou_pop.setVisibility(View.VISIBLE);
                            ll_mainfrag_pop.setVisibility(View.VISIBLE);
                            ll_mainfrag_pop.bringToFront();
                        }
                    }
                    break;
                case 3:
                    if (bool) {
                        ll_mainfrag_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_pop.bringToFront();
                        tv_mainfrag_yhq_pop.setText(str);
                        ll_mainfrag_yhq_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_yhq_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menushow));//开始动画
                        spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK3", false);
                    } else {
                        if (!spUtil.getBoolean("MAINFRAG_TIP_TYPE_CLICK3", false)) {
                            ll_mainfrag_pop.setVisibility(View.VISIBLE);
                            ll_mainfrag_pop.bringToFront();
                            tv_mainfrag_yhq_pop.setText(str);
                            ll_mainfrag_yhq_pop.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 4:
                    /*if (bool) {
                        ll_mainfrag_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_pop.bringToFront();
                        tv_mainfrag_kabao_pop.setText(str);
                        ll_mainfrag_kabao_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_kabao_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menushow));//开始动画
                        spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK4", false);
                    } else {
                        if (!spUtil.getBoolean("MAINFRAG_TIP_TYPE_CLICK4", false)) {
                            ll_mainfrag_pop.setVisibility(View.VISIBLE);
                            ll_mainfrag_pop.bringToFront();
                            tv_mainfrag_kabao_pop.setText(str);
                            ll_mainfrag_kabao_pop.setVisibility(View.VISIBLE);
                        }
                    }*/
                    break;
                case 5:
                    if (bool) {
                        ll_mainfrag_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_pop.bringToFront();
                        tv_mainfrag_return_pop.setText(str);
                        ll_mainfrag_return_pop.setVisibility(View.VISIBLE);
                        ll_mainfrag_return_pop.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.menushow));//开始动画
                        spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK5", false);
                    } else {
                        if (!spUtil.getBoolean("MAINFRAG_TIP_TYPE_CLICK5", false)) {
                            ll_mainfrag_pop.setVisibility(View.VISIBLE);
                            ll_mainfrag_pop.bringToFront();
                            tv_mainfrag_return_pop.setText(str);
                            ll_mainfrag_return_pop.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                default:
                    break;
            }
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
}
