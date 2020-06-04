package com.haotang.pet.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.legacy.widget.Space;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.haotang.base.BaseFragment;
import com.haotang.pet.BeauticianCommonPicActivity;
import com.haotang.pet.CashbackAmountActivity;
import com.haotang.pet.ChangeAccountActivity;
import com.haotang.pet.ChooseMyPetActivity;
import com.haotang.pet.CommonAddressActivity;
import com.haotang.pet.FeedBackActivity;
import com.haotang.pet.FosterLiveActivity;
import com.haotang.pet.FosterLiveListActivity;
import com.haotang.pet.Interface.IOnFocusListenable;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.MemberUpgradeActivity;
import com.haotang.pet.MipcaActivityCapture;
import com.haotang.pet.MyBillActivity;
import com.haotang.pet.MyCanActivity;
import com.haotang.pet.MyCardActivity;
import com.haotang.pet.MyCouponNewActivity;
import com.haotang.pet.MyEvaluateActivity;
import com.haotang.pet.NoticeListActivity;
import com.haotang.pet.PetDetailActivity;
import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.SetActivity;
import com.haotang.pet.StatusLibs.StatusBarCompat;
import com.haotang.pet.adapter.MallAdapter.MallMyFragmentAdapter;
import com.haotang.pet.adapter.MyFragPetAdapter;
import com.haotang.pet.adapter.MyPetFragmentAdapter;
import com.haotang.pet.adapter.MyPetFragmentPager;
import com.haotang.pet.encyclopedias.activity.MyCollectActivity;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.entity.ExtraMenusCodeBean;
import com.haotang.pet.entity.FloatIngEvent;
import com.haotang.pet.entity.FosterLive;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.PushMessageEntity;
import com.haotang.pet.entity.mallEntity.MallSearchGoods;
import com.haotang.pet.libs.AppBarStateChangeListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ShadowLayout;
import com.jimi_wu.ptlrecyclerview.Divider.BaseItemDecoration;
import com.jimi_wu.ptlrecyclerview.LayoutManager.PTLGridLayoutManager;
import com.jimi_wu.ptlrecyclerview.PullToLoad.OnLoadListener;
import com.jimi_wu.ptlrecyclerview.PullToLoad.PullToLoadRecyclerView;
import com.jimi_wu.ptlrecyclerview.PullToRefresh.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

;
;


/**
 * <p>
 * Title:MyFragment
 * </p>
 * <p>
 * Description:我的界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2017-1-16 下午3:48:03
 */
@SuppressLint("NewApi")
public class MyFragment extends BaseFragment implements OnClickListener,
        OnTouchListener, IOnFocusListenable {
    public static MyFragment myFragment;
    private View rootView;
    private final static float EXPAND_AVATAR_SIZE_DP = 100f;
    private final static float COLLAPSED_AVATAR_SIZE_DP = 50f;
    private CollapsingToolbarLayout mToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private CircleImageView mAvatarImageView;
    private TextView mToolbarTextView, mTitleTextView;
    private Space mSpace;
    private Toolbar mToolBar;
    private AppBarStateChangeListener mAppBarStateChangeListener;
    private int[] mAvatarPoint = new int[2],
            mSpacePoint = new int[2],
            mToolbarTextPoint = new int[2],
            mTitleTextViewPoint = new int[2];
    private float mTitleTextSize;
    private LinearLayout layout_mymoney;
    private TextView my_money;
    private LinearLayout layout_cashback;
    private TextView my_cashback;
    private LinearLayout layout_mydummy;
    private TextView my_dummy;
    private LinearLayout layout_mycoupon;
    private TextView my_coupon;
    private LinearLayout layout_mycards;
    private TextView my_cards;
    private TextView title_desc;
    private PullToLoadRecyclerView rcv;
    private Handler handler;
    private ArrayList<View> headerViews = new ArrayList<>();
    private View header;
    private ArrayList<Pet> petlist = new ArrayList<Pet>();
    private MyPetFragmentAdapter myPetFragmentAdapter;
    private RecyclerView recyleview_my_pet;
    private LinearLayout layout_no_pets;
    private View removeHeaderView;
    private LinearLayout layout_use_click;
    private ImageView img_worker_icon;
    private RelativeLayout relayout_one;
    private ImageView img_one;
    private TextView textview_before_after_one;
    private RelativeLayout relayout_two;
    private ImageView img_two;
    private TextView textview_before_after_two;
    private RelativeLayout relayout_thr;
    private ImageView img_thr;
    private TextView textview_before_after_thr;
    private RelativeLayout relayout_four;
    private ImageView img_four;
    private TextView textview_before_after_four;
    private ImageView img_samll_icon;
    private ImageView img_scan;
    private RelativeLayout vip_card_top;
    private RelativeLayout vip_card_bottom;
    private TextView textview_shengqian;
    private TextView textview_xufeivip;
    private TextView textview_vip_notice;
    private TextView vip_show_or_button;
    private LinearLayout layout_bottom_show_vip;
    private int index = 1;
    private String pics[] = null;
    private List<RelativeLayout> allLayout = new ArrayList<>();
    private List<ImageView> allImg = new ArrayList<>();
    private List<TextView> allText = new ArrayList<>();
    private int orderId = 0;
    private int mallId = 0;
    private String liveUrl;
    private ArrayList<MallSearchGoods> listServicePush = new ArrayList<MallSearchGoods>();
    private int pageServicePush = 1;
    private MallMyFragmentAdapter mallMyFragmentAdapter;
    private int type = 1;
    private int log_id;
    private List<PushMessageEntity> allNotices = new ArrayList<>();
    private int width;
    private int height;
    private View mingpianView;
    private CircleImageView img_icon;
    private ImageView img_vip;
    private ImageView img_vip_card;
    private int isVIP;
    private String servicePhone = "";
    private Context context;
    private Bitmap bitmapImg;
    private Bitmap cachebmp;
    private Bitmap loadbmp;
    private Bitmap bmp = null;
    private ViewPager viewpager;
    private MyPetFragmentPager myPetFragmentPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String petCardUrl;
    private ImageView iv_myfrag_xhrwone;
    private ImageView iv_myfrag_xhrw1;
    private ImageView iv_myfrag_xhrw2;
    private RelativeLayout rl_myfrag_xhrw;
    private LinearLayout ll_myfrag_xhrwtwo;
    private ArrayList<Banner> listBanner = new ArrayList<Banner>();
    private LinearLayout layoutMyPet;
    private LinearLayout layoutMyAddress;
    private LinearLayout layoutCircle;
    private LinearLayout layoutFeedBack;
    private LinearLayout layoutMyClo;
    private LinearLayout layoutMyEva;
    private LinearLayout layoutSet;
    private LinearLayout layoutrecord;
    private Timer timer;
    private Task task;
    private long lastOverTime = 0;
    private int memberLevelId = 0;
    private String memberImgUrl;
    private ShadowLayout sl_myfrag_jyzb;
    private RelativeLayout rl_myfrag_jyzb;
    private TextView tv_myfrag_jyzb_name;
    private TextView tv_myfrag_jyzb_fjnum;
    private RecyclerView rv_myfrag_jyzb_pet;
    private List<FosterLive> list = new ArrayList<FosterLive>();
    private List<String> petImgList = new ArrayList<>();
    private MyFragPetAdapter myFragPetAdapter;
    private String tips;

    private Handler handlerClearTopHeight = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    rcv.removeHeaderView(removeHeaderView);
                    break;
            }
        }
    };
    private TextView tv_mainfrag_yhq_pop;
    private LinearLayout ll_mainfrag_pop;
    private LinearLayout ll_mainfrag_yue_pop;
    private LinearLayout ll_mainfrag_guantou_pop;
    private LinearLayout ll_mainfrag_yhq_pop;
    private LinearLayout ll_mainfrag_return_pop;
    private TextView tv_mainfrag_return_pop;
    private TextView tv_mainfrag_yue_pop;
    private TextView tv_mainfrag_guantou_pop;

    /**
     * android.support.design.widget.AppBarLayout
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        myFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        if (rootView == null)
            rootView = inflater.inflate(R.layout.myfragment_new, null);
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        rootView.setOnTouchListener(this);
        initView();
        setUpViews();
        resetPoints();
        setHasOptionsMenu(true);//设置fragment支持toolbar menu菜单
        if (!Utils.checkLogin(mContext)) {
            fetchAvatar("");//设置头像加上后随布局移动img缩小滑动到toolbar固定
        }
        setRecyViewData();
        CommUtil.loadMenuNames(mContext, null, loadMenuNamesHanler);
//        loadDataFromNet();
        rl_myfrag_xhrw.setVisibility(View.GONE);
        listBanner.clear();
        lastOverTime = Utils.dip2px(mContext, 99);
        return rootView;
    }

    private AsyncHttpResponseHandler myPageBannerHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject objectData = jobj.getJSONObject("data");
                        if (objectData.has("banners") && !objectData.isNull("banners")) {
                            JSONArray jbanners = objectData.getJSONArray("banners");
                            if (jbanners != null && jbanners.length() > 0) {
                                rl_myfrag_xhrw.setVisibility(View.VISIBLE);
                                listBanner.clear();
                                for (int i = 0; i < jbanners.length(); i++) {
                                    listBanner.add(Banner.json2Entity(jbanners
                                            .getJSONObject(i)));
                                }
                                if (listBanner.size() == 1) {
                                    iv_myfrag_xhrwone.setVisibility(View.VISIBLE);
                                    ll_myfrag_xhrwtwo.setVisibility(View.GONE);
                                    Banner banner = listBanner.get(0);
                                    if (banner != null) {
                                        if (!TextUtils.isEmpty(banner.bigOrSmall) && banner.bigOrSmall.equals("big")) {
                                            GlideUtil.loadImg(mContext, banner.bigPic, iv_myfrag_xhrwone, R.drawable.icon_production_default);
                                        } else if (!TextUtils.isEmpty(banner.bigOrSmall) && banner.bigOrSmall.equals("small")) {
                                            GlideUtil.loadImg(mContext, banner.pic, iv_myfrag_xhrwone, R.drawable.icon_production_default);
                                        }
                                    }
                                } else {
                                    iv_myfrag_xhrwone.setVisibility(View.GONE);
                                    ll_myfrag_xhrwtwo.setVisibility(View.VISIBLE);
                                    Banner banner = listBanner.get(0);
                                    if (banner != null) {
                                        if (!TextUtils.isEmpty(banner.bigOrSmall) && banner.bigOrSmall.equals("big")) {
                                            GlideUtil.loadImg(mContext, banner.bigPic, iv_myfrag_xhrw1, R.drawable.icon_production_default);
                                        } else if (!TextUtils.isEmpty(banner.bigOrSmall) && banner.bigOrSmall.equals("small")) {
                                            GlideUtil.loadImg(mContext, banner.pic, iv_myfrag_xhrw1, R.drawable.icon_production_default);
                                        }
                                    }
                                    Banner banner1 = listBanner.get(1);
                                    if (banner1 != null) {
                                        if (!TextUtils.isEmpty(banner1.bigOrSmall) && banner1.bigOrSmall.equals("big")) {
                                            GlideUtil.loadImg(mContext, banner1.bigPic, iv_myfrag_xhrw2, R.drawable.icon_production_default);
                                        } else if (!TextUtils.isEmpty(banner1.bigOrSmall) && banner1.bigOrSmall.equals("small")) {
                                            GlideUtil.loadImg(mContext, banner1.pic, iv_myfrag_xhrw2, R.drawable.icon_production_default);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (resultCode == Global.EXIT_USER_CODE) {
                        mTitleTextView.setText("立即登录");
                        recyleview_my_pet.setVisibility(View.GONE);
                        viewpager.setVisibility(View.GONE);
                        layout_no_pets.setVisibility(View.VISIBLE);
                        my_money.setText(0 + "");
                        my_cashback.setText(0 + "");
                        my_dummy.setText(0 + "");
                        my_coupon.setText(0 + "");
                        my_cards.setText(0 + "");
                    }
                    Utils.Exit(context, resultCode);
                }
            } catch (JSONException e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void loadDataFromNet() {
        mPDialog.showDialog();
        allNotices.clear();
        petlist.clear();
        fragmentList.clear();
        CommUtil.loadExtraMenus(mContext, spUtil.getString("cellphone", ""), loadExtraMenusHanler);
        title_desc.setText("宠物家-您身边的宠物管家");
        img_vip_card.setVisibility(View.GONE);
        isVIP = 0;
        if (!Utils.checkLogin(mContext)) {
            mTitleTextView.setText("立即登录");
            recyleview_my_pet.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
            layout_no_pets.setVisibility(View.VISIBLE);
            my_money.setText(0 + "");
            my_cashback.setText(0 + "");
            my_dummy.setText(0 + "");
            my_coupon.setText(0 + "");
            my_cards.setText(0 + "");
        }
        if (Utils.checkLogin(mContext)) {
            LoginShopmall();
        } else {
            unLoginShopmall();
        }
        if (Utils.checkLogin(mContext)) {
            getMyPage();//我的个人信息以及消息数
            getAccountCenter();
            getFosterLiveList();
        } else {
            isVIP = 0;
            getImg("");
            sl_myfrag_jyzb.setVisibility(View.GONE);
        }
        setShowOrderIndex(index);
        rl_myfrag_xhrw.setVisibility(View.GONE);
        listBanner.clear();
        CommUtil.myPageBanner(mActivity, myPageBannerHanler);
    }

    private void getFosterLiveList() {
        mPDialog.showDialog();
        list.clear();
        petImgList.clear();
        CommUtil.getFosterLiveList(getActivity(), getFosterLiveListHandler);
    }

    private AsyncHttpResponseHandler getFosterLiveListHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONArray jarrdata = jsonObject.getJSONArray("data");
                        if (jarrdata.length() > 0) {
                            for (int i = 0; i < jarrdata.length(); i++) {
                                list.add(FosterLive.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortCenter(mContext, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortCenter(mContext, "数据异常");
                e.printStackTrace();
            }
            if (list.size() > 0) {
                sl_myfrag_jyzb.setVisibility(View.VISIBLE);
                FosterLive item = list.get(0);
                Utils.setText(tv_myfrag_jyzb_name, item.getShopName(), "", View.VISIBLE, View.VISIBLE);
                String[] split = item.getRoomContent().split("@@");
                Utils.setText(tv_myfrag_jyzb_fjnum, "查看" + split[1] + "房直播", "", View.VISIBLE, View.VISIBLE);
                if (item.getRoomPetAvatarList() != null && item.getRoomPetAvatarList().size() > 0) {
                    petImgList.clear();
                    petImgList.addAll(item.getRoomPetAvatarList());
                }
            } else {
                sl_myfrag_jyzb.setVisibility(View.GONE);
            }
            myFragPetAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            sl_myfrag_jyzb.setVisibility(View.GONE);
            ToastUtil.showToastShortCenter(mContext, "请求失败");
            mPDialog.closeDialog();
        }
    };

    private void unLoginShopmall() {
        CommUtil.getrRecommendData(mActivity, 0, pageServicePush, -1, getrRecommentDataHandler);
    }

    private void LoginShopmall() {
        CommUtil.getrecommendedForYouForMinePage(mActivity, 0, pageServicePush, -1, getrRecommentDataHandler);
    }

    private AsyncHttpResponseHandler getrRecommentDataHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                listServicePush.clear();
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("commodityList") && !jdata.isNull("commodityList")) {
                            JSONArray jarrcommodityList = jdata.getJSONArray("commodityList");
                            if (jarrcommodityList.length() > 0) {
                                for (int i = 0; i < jarrcommodityList.length(); i++) {
                                    listServicePush.add(MallSearchGoods.json2Entity(jarrcommodityList.getJSONObject(i)));
                                }
                            }
                        }
                    }
                } else {
                    if (resultCode == Global.EXIT_USER_CODE) {
                        mTitleTextView.setText("立即登录");
                        recyleview_my_pet.setVisibility(View.GONE);
                        viewpager.setVisibility(View.GONE);
                        layout_no_pets.setVisibility(View.VISIBLE);
                        my_money.setText(0 + "");
                        my_cashback.setText(0 + "");
                        my_dummy.setText(0 + "");
                        my_coupon.setText(0 + "");
                        my_cards.setText(0 + "");
                    }
                    Utils.Exit(context, resultCode);
                }
                mallMyFragmentAdapter.notifyDataSetChanged();
                rcv.completeRefresh();
                rcv.setNoMore(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void getMallOrderForMyPage() {
        CommUtil.mallOrderForMyPage(mActivity, mallOrderHandler);
    }

    private AsyncHttpResponseHandler mallOrderHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("order") && !objectData.isNull("order")) {
                            JSONObject objectOrder = objectData.getJSONObject("order");
                            if (objectOrder.has("id") && !objectOrder.isNull("id")) {
                                mallId = objectOrder.getInt("id");
                            }
                            if (objectOrder.has("extendParam") && !objectOrder.isNull("extendParam")) {
                                JSONObject objectExtend = objectOrder.getJSONObject("extendParam");
                                if (objectExtend.has("items") && !objectExtend.isNull("items")) {
                                    JSONArray array = objectExtend.getJSONArray("items");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject objectEva = array.getJSONObject(i);
                                            if (objectEva.has("log_id") && !objectEva.isNull("log_id")) {
                                                if (i == 0) {
                                                    log_id = objectEva.getInt("log_id");
                                                }
                                            }
                                            if (objectEva.has("extendParam") && !objectEva.isNull("extendParam")) {
                                                Utils.mLogError("==-->11111");
                                                JSONObject objectExtendParam = objectEva.getJSONObject("extendParam");
                                                Utils.mLogError("==-->22222");
                                                if (objectExtendParam.has("commodity") && !objectExtendParam.isNull("commodity")) {
                                                    Utils.mLogError("==-->333333");
                                                    JSONObject objectCommodity = objectExtendParam.getJSONObject("commodity");
                                                    Utils.mLogError("==-->444444");
                                                    if (objectCommodity.has("title") && !objectCommodity.isNull("title")) {
                                                        Utils.mLogError("==-->55555");
                                                        if (i == 0) {
                                                            Utils.mLogError("==-->66666");
                                                            String shopOrderName = objectCommodity.getString("title");
                                                            Utils.mLogError("==-->77777 shopOrderName " + shopOrderName);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (objectData.has("achong") && !objectData.isNull("achong")) {
                            JSONObject objectAchone = objectData.getJSONObject("achong");
                            if (objectAchone.has("avatar") && !objectAchone.isNull("avatar")) {
                                String avatar = objectAchone.getString("avatar");
                                GlideUtil.loadCircleImg(mContext, avatar, img_worker_icon, 0);
                            }
                        }
                    }
                } else {
                    if (code == Global.EXIT_USER_CODE) {
                        mTitleTextView.setText("立即登录");
                        recyleview_my_pet.setVisibility(View.GONE);
                        viewpager.setVisibility(View.GONE);
                        layout_no_pets.setVisibility(View.VISIBLE);
                        my_money.setText(0 + "");
                        my_cashback.setText(0 + "");
                        my_dummy.setText(0 + "");
                        my_coupon.setText(0 + "");
                        my_cards.setText(0 + "");
                    }
                    Utils.Exit(context, code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void getMyPage() {//我的界面
        allNotices.clear();
        CommUtil.myPage(mActivity, myPageHandler);
    }

    private AsyncHttpResponseHandler myPageHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("user") && !objectData.isNull("user")) {
                            JSONObject objectUser = objectData.getJSONObject("user");
                            if (objectUser.has("userName") && !objectUser.isNull("userName")) {
                                mTitleTextView.setText(objectUser.getString("userName"));
                            } else {
                                mTitleTextView.setText("未填写用户名");
                            }
                            if (objectUser.has("extendParam") && !objectUser.isNull("extendParam")) {
                                JSONObject objectExtend = objectUser.getJSONObject("extendParam");
                                if (objectExtend.has("isVIP") && !objectExtend.isNull("isVIP")) {
                                    isVIP = objectExtend.getInt("isVIP");
                                }
                            }
                            if (objectUser.has("memberLevelId") && !objectUser.isNull("memberLevelId")) {
                                memberLevelId = objectUser.getInt("memberLevelId");
                            }
                            if (objectUser.has("avatar") && !objectUser.isNull("avatar")) {
                                String avatar = objectUser.getString("avatar");
                                spUtil.saveString("userimage", avatar);
                                fetchAvatar(avatar);
                            }
                        }
                        if (objectData.has("notReadCount") && !objectData.isNull("notReadCount")) {
                            int notReadCount = objectData.getInt("notReadCount");
                            if (notReadCount > 0) {
                                img_samll_icon.setImageResource(R.drawable.notice_new);
                            } else {
                                img_samll_icon.setImageResource(R.drawable.notice_un_new);
                            }
                        } else {
                            img_samll_icon.setImageResource(R.drawable.notice_un_new);
                        }
                        if (objectData.has("pushMessageList") && !objectData.isNull("pushMessageList")) {
                            JSONArray array = objectData.getJSONArray("pushMessageList");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    allNotices.add(PushMessageEntity.j2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                    }
                } else {
                    if (code == Global.EXIT_USER_CODE) {
                        mTitleTextView.setText("立即登录");
                        recyleview_my_pet.setVisibility(View.GONE);
                        viewpager.setVisibility(View.GONE);
                        layout_no_pets.setVisibility(View.VISIBLE);
                        my_money.setText(0 + "");
                        my_cashback.setText(0 + "");
                        my_dummy.setText(0 + "");
                        my_coupon.setText(0 + "");
                        my_cards.setText(0 + "");
                    }
                    Utils.Exit(context, code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void setRecyViewData() {
        handler = new Handler();
        rcv.setRefreshEnable(true);
        rcv.setLayoutManager(new PTLGridLayoutManager(2, PTLGridLayoutManager.VERTICAL, false));
        mallMyFragmentAdapter = new MallMyFragmentAdapter<MallSearchGoods>(mActivity, listServicePush, R.layout.item_mall_to_list_my);
        rcv.setAdapter(mallMyFragmentAdapter);
        //设置刷新监听
        rcv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onStartRefreshing() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.checkLogin(mContext)) {
                            LoginShopmall();
                        } else {
                            unLoginShopmall();
                        }
                        rcv.completeRefresh();
                        rcv.setNoMore(false);
                    }
                }, 500);
            }
        });
        //设置加载监听
        rcv.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onStartLoading(int skip) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rcv.completeRefresh();
                        rcv.setNoMore(true);
                    }
                }, 5 * 1000);
            }
        });
        //设置分割线
        rcv.addItemDecoration(new BaseItemDecoration(mContext, R.color.af8f8f8));
        rcv.addHeaderView(getHeaderViewTwo());
        rcv.addHeaderView(getHeaderView());
        rcv.setLoadingViewGone();
    }

    private View getHeaderViewTwo() {
        removeHeaderView = getActivity().getLayoutInflater().inflate(R.layout.my_fragment_touming_top, rcv, false);
        return removeHeaderView;
    }

    private View getHeaderView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.myfragment_topuser, rcv, false);
        recyleview_my_pet = (RecyclerView) view.findViewById(R.id.recyleview_my_pet);
        layout_no_pets = (LinearLayout) view.findViewById(R.id.layout_no_pets);
        rl_myfrag_xhrw = (RelativeLayout) view.findViewById(R.id.rl_myfrag_xhrw);
        iv_myfrag_xhrwone = (ImageView) view.findViewById(R.id.iv_myfrag_xhrwone);
        ll_myfrag_xhrwtwo = (LinearLayout) view.findViewById(R.id.ll_myfrag_xhrwtwo);
        iv_myfrag_xhrw1 = (ImageView) view.findViewById(R.id.iv_myfrag_xhrw1);
        iv_myfrag_xhrw2 = (ImageView) view.findViewById(R.id.iv_myfrag_xhrw2);
        img_worker_icon = (ImageView) view.findViewById(R.id.img_worker_icon);
        relayout_one = (RelativeLayout) view.findViewById(R.id.relayout_one);
        img_one = (ImageView) view.findViewById(R.id.img_one);
        textview_before_after_one = (TextView) view.findViewById(R.id.textview_before_after_one);
        relayout_two = (RelativeLayout) view.findViewById(R.id.relayout_two);
        img_two = (ImageView) view.findViewById(R.id.img_two);
        textview_before_after_two = (TextView) view.findViewById(R.id.textview_before_after_two);
        relayout_thr = (RelativeLayout) view.findViewById(R.id.relayout_thr);
        img_thr = (ImageView) view.findViewById(R.id.img_thr);
        textview_before_after_thr = (TextView) view.findViewById(R.id.textview_before_after_thr);
        relayout_four = (RelativeLayout) view.findViewById(R.id.relayout_four);
        img_four = (ImageView) view.findViewById(R.id.img_four);
        textview_before_after_four = (TextView) view.findViewById(R.id.textview_before_after_four);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        layoutMyPet = (LinearLayout) view.findViewById(R.id.layoutMyPet);
        layoutMyAddress = (LinearLayout) view.findViewById(R.id.layoutMyAddress);
        layoutCircle = (LinearLayout) view.findViewById(R.id.layoutCircle);
        layoutFeedBack = (LinearLayout) view.findViewById(R.id.layoutFeedBack);
        layoutMyClo = (LinearLayout) view.findViewById(R.id.layoutMyClo);
        layoutMyEva = (LinearLayout) view.findViewById(R.id.layoutMyEva);
        layoutSet = (LinearLayout) view.findViewById(R.id.layoutSet);
        layoutrecord = (LinearLayout) view.findViewById(R.id.layoutrecord);
        sl_myfrag_jyzb = (ShadowLayout) view.findViewById(R.id.sl_myfrag_jyzb);
        rl_myfrag_jyzb = (RelativeLayout) view.findViewById(R.id.rl_myfrag_jyzb);
        tv_myfrag_jyzb_name = (TextView) view.findViewById(R.id.tv_myfrag_jyzb_name);
        tv_myfrag_jyzb_fjnum = (TextView) view.findViewById(R.id.tv_myfrag_jyzb_fjnum);
        rv_myfrag_jyzb_pet = (RecyclerView) view.findViewById(R.id.rv_myfrag_jyzb_pet);
        allLayout.add(relayout_one);
        allLayout.add(relayout_two);
        allLayout.add(relayout_thr);
        allLayout.add(relayout_four);
        allImg.add(img_one);
        allImg.add(img_two);
        allImg.add(img_thr);
        allImg.add(img_four);
        allText.add(textview_before_after_one);
        allText.add(textview_before_after_two);
        allText.add(textview_before_after_thr);
        allText.add(textview_before_after_four);
        tv_myfrag_jyzb_fjnum.setOnClickListener(this);
        rl_myfrag_jyzb.setOnClickListener(this);
        iv_myfrag_xhrwone.setOnClickListener(this);
        iv_myfrag_xhrw1.setOnClickListener(this);
        iv_myfrag_xhrw2.setOnClickListener(this);
        layout_no_pets.setOnClickListener(this);
        layoutMyPet.setOnClickListener(this);
        layoutrecord.setOnClickListener(this);
        layoutMyAddress.setOnClickListener(this);
        layoutCircle.setOnClickListener(this);
        layoutFeedBack.setOnClickListener(this);
        layoutMyClo.setOnClickListener(this);
        layoutMyEva.setOnClickListener(this);
        layoutSet.setOnClickListener(this);
        headerViews.add(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyleview_my_pet.setLayoutManager(linearLayoutManager);
        myPetFragmentAdapter = new MyPetFragmentAdapter(mContext, petlist);
        recyleview_my_pet.setAdapter(myPetFragmentAdapter);
        recyleview_my_pet.setNestedScrollingEnabled(false);
        myPetFragmentAdapter.setOnItemClickRecyleView(new MyPetFragmentAdapter.OnItemClickRecyleView() {
            @Override
            public void click(View v, int position) {
                Pet pet = petlist.get(position);
                Intent intent = new Intent();
                intent.setClass(mContext, PetDetailActivity.class);
                intent.putExtra("customerpetid", pet.customerpetid);
                startActivity(intent);
            }
        });
        fragmentList.clear();
        myPetFragmentPager = new MyPetFragmentPager(getChildFragmentManager(), fragmentList);
        viewpager.setAdapter(myPetFragmentPager);
        myPetFragmentPager.notifyDataSetChanged();
        rv_myfrag_jyzb_pet.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                // hide the divider for the last child
                if (position == 0) {
                } else {
                    outRect.right = -DensityUtil.dp2px(mContext, 20);
                }
            }
        });
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mContext);
        //linearLayoutManager1.setReverseLayout(true);//布局反向
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_myfrag_jyzb_pet.setLayoutManager(linearLayoutManager1);
        myFragPetAdapter = new MyFragPetAdapter(R.layout.item_myfrag_pet, petImgList);
        rv_myfrag_jyzb_pet.setAdapter(myFragPetAdapter);
        return view;
    }

    private void setImgIconXYPosition() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Utils.dip2px(mContext, 70), Utils.dip2px(mContext, 70));
        lp.topMargin = Utils.dip2px(mContext, 50);
        lp.leftMargin = Utils.dip2px(mContext, -5);
        mAvatarImageView.setLayoutParams(lp);
    }

    private void getImg(String avatar) {
//        mAvatarImageView.setImageResource(R.drawable.my_top_touming_icon);
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = Utils.dip2px(mContext, 70);     // 屏幕宽度（像素）
        height = Utils.dip2px(mContext, 70);   // 屏幕高度（像素）
        mingpianView = LayoutInflater.from(mContext).inflate(R.layout.item_my_fragment_icon, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.width = width;
        lp.height = height;
        mingpianView.setLayoutParams(lp);
        img_icon = (CircleImageView) mingpianView.findViewById(R.id.img_icon);
        LinearLayout layout_outside_img = (LinearLayout) mingpianView.findViewById(R.id.layout_outside_img);
        layout_outside_img.setBackgroundColor(getResources().getColor(R.color.transparent));
        RelativeLayout.LayoutParams lpimg = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 55), Utils.dip2px(mContext, 55));
        lpimg.addRule(RelativeLayout.CENTER_IN_PARENT);
        lpimg.width = Utils.dip2px(mContext, 45);
        lpimg.height = Utils.dip2px(mContext, 45);
        img_icon.setLayoutParams(lpimg);
        img_vip = (ImageView) mingpianView.findViewById(R.id.img_vip);
        img_vip.setVisibility(View.GONE);
        RelativeLayout.LayoutParams lpvip_icon = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 13), Utils.dip2px(mContext, 13));
        lpvip_icon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpvip_icon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpvip_icon.width = Utils.dip2px(mContext, 13);
        lpvip_icon.height = Utils.dip2px(mContext, 13);
        lpvip_icon.bottomMargin = Utils.dip2px(mContext, 20);
        lpvip_icon.rightMargin = Utils.dip2px(mContext, 20);
        img_vip.setLayoutParams(lpvip_icon);
        if (isVIP == 0) {
            img_vip.setVisibility(View.GONE);
        } else if (isVIP == 1) {
            img_vip.setVisibility(View.GONE);
        }
        if (memberLevelId == 1) {
            img_vip.setBackgroundResource(R.drawable.copper_level_icon);
        } else if (memberLevelId == 2) {
            img_vip.setBackgroundResource(R.drawable.silver_level_icon);
        } else if (memberLevelId == 3) {
            img_vip.setBackgroundResource(R.drawable.gold_level_icon);
        }
        Utils.mLogError("设置头像："+avatar);
        if (!TextUtils.isEmpty(avatar)) {
            String imgUrl = avatar;
            String localImgUrl = "";
            if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
                if (imgUrl.startsWith("drawable://")) {
                    localImgUrl = imgUrl;
                } else if (imgUrl.startsWith("file://")) {
                    localImgUrl = imgUrl;
                } else {
                    if (!imgUrl.startsWith("http://") && !imgUrl.startsWith("https://")) {
                        localImgUrl = CommUtil.getStaticUrl() + imgUrl;
                    } else {
                        localImgUrl = imgUrl;
                    }
                }
            }
            new SetImgTask().execute(localImgUrl);
//            GlideUtil.loadImg(getContext(),localImgUrl,mAvatarImageView,R.drawable.my_top_touming_icon);
        }
        else {
            layoutView(mingpianView, width, height);
            bitmapImg = viewSaveToImage(mingpianView);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            GlideUtil.loadCircleImgWithBytes(mContext, bytes, mAvatarImageView, 0);
        }
    }

    private void getMyOrder() {
        CommUtil.myOrder(mActivity, myOrderHandler);
    }

    private void getMyHotelOrder() {
        CommUtil.hotelMyOrder(mActivity, myOrderHandler);
    }

    private AsyncHttpResponseHandler myOrderHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("orderId") && !objectData.isNull("orderId")) {
                            orderId = objectData.getInt("orderId");
                        }
                        if (objectData.has("liveUrl") && !objectData.isNull("liveUrl")) {
                            liveUrl = objectData.getString("liveUrl");
                        }
                        if (objectData.has("details") && !objectData.isNull("details")) {
                            JSONObject objecDetail = objectData.getJSONObject("details");
                            if (objecDetail.has("avatar") && !objecDetail.isNull("avatar")) {
                                String avatar = objecDetail.getString("avatar");
                                GlideUtil.loadCircleImg(mContext, avatar, img_worker_icon, 0);
                            }
                        }
                    }
                } else {
                    if (code == Global.EXIT_USER_CODE) {
                        mTitleTextView.setText("立即登录");
                        recyleview_my_pet.setVisibility(View.GONE);
                        viewpager.setVisibility(View.GONE);
                        layout_no_pets.setVisibility(View.VISIBLE);
                        my_money.setText(0 + "");
                        my_cashback.setText(0 + "");
                        my_dummy.setText(0 + "");
                        my_coupon.setText(0 + "");
                        my_cards.setText(0 + "");
                    }
                    Utils.Exit(context, code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void setShowOrderIndex(int index) {
        mPDialog.showDialog();
        switch (index) {
            case 1:
                getMyOrder();
                break;
            case 2:
                getMallOrderForMyPage();
                break;
            case 3:
                getMyHotelOrder();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        resetPoints();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.isLogin()) {
                loadDataFromNet();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromNet();
        int positionXy[] = new int[2];
        rcv.getLocationOnScreen(positionXy);
        int clickButton[] = new int[2];
        textview_xufeivip.getLocationOnScreen(clickButton);
        int clickButtony = clickButton[1];
        spUtil.saveInt("clickButtony", clickButtony);
        timer = new Timer();
        task = new Task(timer);
        timer.schedule(task, 0, 1000);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resetPoints();
            loadDataFromNet();
            StatusBarCompat.setStatusBarColorForCollapsingToolbar(getActivity(), mAppBarLayout, mToolbarLayout, mToolBar, getResources().getColor(R.color.aD0021B));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            resetPoints();
            EventBus.getDefault().post(new FloatIngEvent(4));
        }
    }

    private void fetchAvatar(String avatar) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                resetPoints();
            }
        });
        getImg(avatar);
    }

    private void clearAnim() {
        mAvatarImageView.setTranslationX(0);
        mAvatarImageView.setTranslationY(0);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTitleTextView.setTranslationX(0);
        mTitleTextView.setTranslationY(0);
    }

    private void resetPoints() {
        clearAnim();
        int avatarSize = Utils.convertDpToPixelSize(EXPAND_AVATAR_SIZE_DP, mContext);
        mAvatarImageView.getLocationOnScreen(mAvatarPoint);
        mAvatarPoint[0] -= (avatarSize - mAvatarImageView.getWidth()) / 2;
        mSpace.getLocationOnScreen(mSpacePoint);
        mToolbarTextView.getLocationOnScreen(mToolbarTextPoint);
        mToolbarTextPoint[0] += Utils.convertDpToPixelSize(16, mContext);
        mTitleTextView.post(new Runnable() {

            @Override
            public void run() {
                mTitleTextView.getLocationOnScreen(mTitleTextViewPoint);
                translationView(mAppBarStateChangeListener.getCurrentOffset());
            }
        });
    }

    private void setUpViews() {
        mTitleTextSize = mTitleTextView.getTextSize();
        setUpToolbar();
        setUpAmazingAvatar();
    }

    private void setUpAmazingAvatar() {
        mAppBarStateChangeListener = new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {//展开状态
                    layout_bottom_show_vip.setVisibility(View.VISIBLE);
                    layout_use_click.setVisibility(View.VISIBLE);
                    rcv.setRefreshEnable(true);
                    mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (state == State.COLLAPSED) {//折叠状态
                    layout_bottom_show_vip.setVisibility(View.GONE);
                    layout_use_click.setVisibility(View.GONE);
                    setTopHeight((float) 0.1);
                    StatusBarCompat.setStatusBarColorForCollapsingToolbar(getActivity(), mAppBarLayout, mToolbarLayout, mToolBar, getResources().getColor(R.color.aD0021B));
                    mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utils.checkLogin(mContext)) {
                                goNext(LoginNewActivity.class);
                            } else {
                                goNext(ChangeAccountActivity.class);
                            }
                        }
                    });
                    rcv.setRefreshEnable(false);
                } else if (state == State.IDLE) {//中间状态
                    layout_bottom_show_vip.setVisibility(View.GONE);
                    layout_use_click.setVisibility(View.GONE);
                    setTopHeight((float) 0.1);
                    mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    rcv.setRefreshEnable(false);
                }
            }

            @Override
            public void onOffsetChanged(State state, float offset) {
                translationView(offset);
            }

            @Override
            public void onOffsetChangedTwo(AppBarLayout appBarLayout, int i) {
            }
        };
        mAppBarLayout.addOnOffsetChangedListener(mAppBarStateChangeListener);
        mAppBarLayout.setExpanded(true);
    }

    private void translationView(float offset) {
        float yOffset = -((mAvatarPoint[1] - mSpacePoint[1]) - getStatusBarHeight() / 2 - getStatusBarHeight() / 5) * offset;
        int mToolBra[] = new int[2];
        mToolBar.getLocationOnScreen(mToolBra);
        int newSize = Utils.convertDpToPixelSize(EXPAND_AVATAR_SIZE_DP - (EXPAND_AVATAR_SIZE_DP - COLLAPSED_AVATAR_SIZE_DP) * offset, mContext);
        float newTextSize = mTitleTextSize - (mTitleTextSize - mToolbarTextView.getTextSize()) * offset;
        mAvatarImageView.getLayoutParams().width = newSize;
        mAvatarImageView.getLayoutParams().height = newSize;
        mAvatarImageView.setTranslationY(yOffset);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
    }

    private void setUpToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolBar.setNavigationIcon(R.drawable.my_fragment_top_back);
        }
    }

    private void initView() {
        Log.e("TAG", "my");
        ll_mainfrag_pop = (LinearLayout) rootView
                .findViewById(R.id.ll_mainfrag_pop);
        ll_mainfrag_yue_pop = (LinearLayout) rootView
                .findViewById(R.id.ll_mainfrag_yue_pop);
        ll_mainfrag_guantou_pop = (LinearLayout) rootView
                .findViewById(R.id.ll_mainfrag_guantou_pop);
        ll_mainfrag_yhq_pop = (LinearLayout) rootView
                .findViewById(R.id.ll_mainfrag_yhq_pop);
        ll_mainfrag_return_pop = (LinearLayout) rootView.findViewById(R.id.ll_mainfrag_return_pop);
        tv_mainfrag_return_pop = (TextView) rootView.findViewById(R.id.tv_mainfrag_return_pop);
        tv_mainfrag_yue_pop = (TextView) rootView
                .findViewById(R.id.tv_mainfrag_yue_pop);
        tv_mainfrag_guantou_pop = (TextView) rootView
                .findViewById(R.id.tv_mainfrag_guantou_pop);
        tv_mainfrag_yhq_pop = (TextView) rootView
                .findViewById(R.id.tv_mainfrag_yhq_pop);
        mAppBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar);
        mToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.mToolbarLayout);
        mAvatarImageView = (CircleImageView) rootView.findViewById(R.id.imageView_avatar);
        layout_mymoney = (LinearLayout) rootView.findViewById(R.id.layout_mymoney);
        my_money = (TextView) rootView.findViewById(R.id.my_money);
        layout_cashback = rootView.findViewById(R.id.layout_cashback);
        my_cashback = rootView.findViewById(R.id.my_cashback);
        layout_mydummy = (LinearLayout) rootView.findViewById(R.id.layout_mydummy);
        my_dummy = (TextView) rootView.findViewById(R.id.my_dummy);
        layout_mycoupon = (LinearLayout) rootView.findViewById(R.id.layout_mycoupon);
        layout_use_click = (LinearLayout) rootView.findViewById(R.id.layout_use_click);
        my_coupon = (TextView) rootView.findViewById(R.id.my_coupon);
        layout_mycards = (LinearLayout) rootView.findViewById(R.id.layout_mycards);
        my_cards = (TextView) rootView.findViewById(R.id.my_cards);
        title_desc = (TextView) rootView.findViewById(R.id.title_desc);
        mToolbarTextView = (TextView) rootView.findViewById(R.id.toolbar_title);
        mTitleTextView = (TextView) rootView.findViewById(R.id.textView_title);
        mSpace = (Space) rootView.findViewById(R.id.space);
        mToolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        img_samll_icon = (ImageView) rootView.findViewById(R.id.img_samll_icon);
        img_scan = (ImageView) rootView.findViewById(R.id.img_scan);
        vip_card_top = (RelativeLayout) rootView.findViewById(R.id.vip_card_top);
        vip_card_bottom = (RelativeLayout) rootView.findViewById(R.id.vip_card_bottom);
        textview_vip_notice = (TextView) rootView.findViewById(R.id.textview_vip_notice);
        img_vip_card = (ImageView) rootView.findViewById(R.id.img_vip_card);
        vip_show_or_button = (TextView) rootView.findViewById(R.id.vip_show_or_button);
        textview_shengqian = (TextView) rootView.findViewById(R.id.textview_shengqian);
        textview_xufeivip = (TextView) rootView.findViewById(R.id.textview_xufeivip);
        layout_bottom_show_vip = (LinearLayout) rootView.findViewById(R.id.layout_bottom_show_vip);
        StatusBarCompat.setStatusBarColorForCollapsingToolbar(getActivity(), mAppBarLayout, mToolbarLayout, mToolBar, getResources().getColor(R.color.aEB6340));
        rcv = (PullToLoadRecyclerView) rootView.findViewById(R.id.rcv);
        header = LayoutInflater.from(mContext).inflate(R.layout.myfragment_topuser, null);
        header.setFocusable(false);
        mTitleTextView.setOnClickListener(this);
        mAvatarImageView.setOnClickListener(this);
        img_samll_icon.setOnClickListener(this);
        img_scan.setOnClickListener(this);
        vip_card_top.setOnClickListener(this);
        vip_card_bottom.setOnClickListener(this);
        layout_bottom_show_vip.setOnClickListener(this);
        layout_mymoney.setOnClickListener(this);
        my_money.setOnClickListener(this);
        layout_cashback.setOnClickListener(this);
        layout_mydummy.setOnClickListener(this);
        my_dummy.setOnClickListener(this);
        layout_mycoupon.setOnClickListener(this);
        my_coupon.setOnClickListener(this);
        layout_mycards.setOnClickListener(this);
        my_cards.setOnClickListener(this);
        layout_use_click.setOnClickListener(this);
        vip_show_or_button.setOnClickListener(this);
        setImgIconXYPosition();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next4:
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MipcaActivityCapture.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MyFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
        try {
            bitmapImg.recycle();
            bmp.recycle();
            cachebmp.recycle();
            loadbmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        return true;
    }

    private AsyncHttpResponseHandler loadExtraMenusHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            String result = new String(responseBody);
            processData(result);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private AsyncHttpResponseHandler loadMenuNamesHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("servicePhone")
                                && !jData.isNull("servicePhone")) {
                            servicePhone = jData.getString("servicePhone");
                        }
                    }
                } else {
                    Utils.Exit(context, resultCode);
                }
            } catch (JSONException e) {
            }
            processData(new String(responseBody));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    // 解析json数据
    private void processData(String result) {
        try {
            Gson gson = new Gson();
            ExtraMenusCodeBean extraMenusCodeBean = gson.fromJson(result,
                    ExtraMenusCodeBean.class);
            if (extraMenusCodeBean != null) {
                int code = extraMenusCodeBean.getCode();
                List<ExtraMenusCodeBean.DataBean> data = extraMenusCodeBean.getData();
                String msg = extraMenusCodeBean.getMsg();
                if (code == 0) {
                    if (data != null && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            ExtraMenusCodeBean.DataBean dataBean = data.get(i);
                            if (dataBean.getText().equals("我的寄养直播")) {
                                liveUrl = dataBean.getUrl();
                            }
                            if (dataBean.getText().equals("办理狗证")) {
                                petCardUrl = dataBean.getUrl();
                            }
                        }
                    }
                } else {
                    if (code == Global.EXIT_USER_CODE) {
                        mTitleTextView.setText("立即登录");
                        recyleview_my_pet.setVisibility(View.GONE);
                        viewpager.setVisibility(View.GONE);
                        layout_no_pets.setVisibility(View.VISIBLE);
                        my_money.setText(0 + "");
                        my_cashback.setText(0 + "");
                        my_dummy.setText(0 + "");
                        my_coupon.setText(0 + "");
                        my_cards.setText(0 + "");
                    }
                    Utils.Exit(mActivity, code);
                    if (msg != null && !TextUtils.isEmpty(msg)) {
                        ToastUtil.showToastShortCenter(mContext, msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getStatusBarHeight() {
        /**
         * 获取状态栏高度
         * */
        int statusBarHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusBarHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
    private void layoutView(View v, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    private Bitmap viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        // 把一个View转换成图片
        cachebmp = loadBitmapFromView(view);
        view.destroyDrawingCache();
        return cachebmp;
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        loadbmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(loadbmp);
        c.drawColor(Color.TRANSPARENT);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return loadbmp;
    }

    private void goNext(Class cls) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtra("previous", Global.AD_TO_LOGIN);
        intent.putExtra("servicePhone", servicePhone);
        mContext.startActivity(intent);
    }

    class SetImgTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                bmp = loadImageFromNetwork(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

//        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                img_icon.setImageBitmap(bitmap);
            }
            layoutView(mingpianView, width, height);
            bitmapImg = viewSaveToImage(mingpianView);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Utils.mLogError("头像大小："+bytes.length);
            GlideUtil.loadCircleImgWithBytes(mContext, bytes, mAvatarImageView, 0);
        }
    }

    //就一个获取图片的方法
    private Bitmap loadImageFromNetwork(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.connect();
        InputStream inputStream = con.getInputStream();
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        return bmp;
    }

    private void goNext(int index, String[] pics) {
        Intent intent = new Intent(mContext, BeauticianCommonPicActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("pics", pics);
        mContext.startActivity(intent);
    }

    private void getAccountCenter() {
        CommUtil.accountCenter((Activity) mContext, accountHandler);
    }

    private AsyncHttpResponseHandler accountHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject obj = new JSONObject(new String(responseBody));
                int code = obj.getInt("code");
                if (code == 0) {
                    if (obj.has("data") && !obj.isNull("data")) {
                        JSONObject objdata = obj.getJSONObject("data");
                        if (objdata.has("user") && !objdata.isNull("user")) {
                            JSONObject objUser = objdata.getJSONObject("user");
                            if (objUser.has("tips") && !objUser.isNull("tips")) {
                                tips = objUser.getString("tips");
                            }
                            if (objUser.has("type") && !objUser.isNull("type")) {
                                int type = objUser.getInt("type");
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
                            if (objUser.has("userTipUrl") && !objUser.isNull("userTipUrl")) {
                                memberImgUrl = objUser.getString("userTipUrl");
                            }
                            if (objUser.has("memberLevelId") && !objUser.isNull("memberLevelId")) {
                                memberLevelId = objUser.getInt("memberLevelId");
                            }
                            if (objUser.has("balance") && !objUser.isNull("balance")) {
                                String balance = objUser.getString("balance");
                                my_money.setText("" + balance);
                            }
                            if (objUser.has("cashback") && !objUser.isNull("cashback")) {
                                double cashback = objUser.getDouble("cashback");
                                my_cashback.setText(Utils.formatDecimal(cashback));
                            }
                            if (objUser.has("canBillNum") && !objUser.isNull("canBillNum")) {
                                String canBillNum = objUser.getString("canBillNum");
                                my_dummy.setText(canBillNum);
                            }
                            if (objUser.has("cards") && !objUser.isNull("cards")) {
                                String cards = objUser.getString("cards");
                                my_cards.setText(cards);
                            }
                            if (objUser.has("coupons") && !objUser.isNull("coupons")) {
                                String coupons = objUser.getString("coupons");
                                my_coupon.setText(coupons);
                            }
                            if (objUser.has("avatar") && !objUser.isNull("avatar")) {
                                String avatar = objUser.getString("avatar");
                                spUtil.saveString("userimage", avatar);
                                GlideUtil.loadCircleImg(mContext, avatar, img_icon, 0);
                            }
                            int isVIP = 0;
                            if (objUser.has("isVIP") && !objUser.isNull("isVIP")) {
                                isVIP = objUser.getInt("isVIP");
                            }
                            String btn_txt = null;
                            if (objUser.has("vip_privilege_tip") && !objUser.isNull("vip_privilege_tip")) {
                                JSONObject objectvip = objUser.getJSONObject("vip_privilege_tip");
                                if (objectvip.has("btn_txt") && !objectvip.isNull("btn_txt")) {
                                    btn_txt = objectvip.getString("btn_txt");
                                }
                            }
                            String copy1 = "";
                            String copy3 = "";
                            String copy6 = "";
                            String levelName = "";
                            String userSign = "";
                            String upgradeTip = "";
                            if (objUser.has("userSign") && !objUser.isNull("userSign")) {
                                userSign = objUser.getString("userSign");
                            }
                            if (objUser.has("levelName") && !objUser.isNull("levelName")) {
                                levelName = objUser.getString("levelName");
                            }
                            if (objUser.has("upgradeTip") && !objUser.isNull("upgradeTip")) {
                                upgradeTip = objUser.getString("upgradeTip");
                            }
                            if (objUser.has("vipSaveCalculator") && !objUser.isNull("vipSaveCalculator")) {
                                JSONObject objectVip = objUser.getJSONObject("vipSaveCalculator");
                                if (objectVip.has("copy1") && !objectVip.isNull("copy1")) {
                                    copy1 = objectVip.getString("copy1");
                                }
                                if (objectVip.has("copy3") && !objectVip.isNull("copy3")) {
                                    copy3 = objectVip.getString("copy3");
                                }
                                if (objectVip.has("copy6") && !objectVip.isNull("copy6")) {
                                    copy6 = objectVip.getString("copy6");
                                }
                            }
                            // if (isVIP == 1) {
                            img_vip_card.setVisibility(View.GONE);
                            textview_vip_notice.setText(levelName);
                            vip_show_or_button.setText("会员升级通知");
                            vip_show_or_button.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                            textview_shengqian.setText(upgradeTip);
                            title_desc.setText(userSign);
                            textview_xufeivip.setText("立即续费");
                            textview_xufeivip.setBackgroundResource(R.drawable.bg_left_right_banyuan_jianbian_huangse);
                            textview_xufeivip.setVisibility(View.GONE);
                            if (memberLevelId == 1) {
                                img_vip_card.setBackgroundResource(R.drawable.copper_level_icon);
                            } else if (memberLevelId == 2) {
                                img_vip_card.setBackgroundResource(R.drawable.silver_level_icon);
                            } else if (memberLevelId == 3) {
                                img_vip_card.setBackgroundResource(R.drawable.gold_level_icon);
                            }
                            /*} else if (isVIP == 0) {
                                img_vip_card.setVisibility(View.GONE);
                                textview_vip_notice.setText(copy6);
                                vip_show_or_button.setText("开通会员");
                                vip_show_or_button.setBackgroundResource(R.drawable.bg_left_right_banyuan_jianbian_huangse);
                                textview_shengqian.setText(upgradeTip);
                                textview_xufeivip.setVisibility(View.GONE);
                            }*/
                        }
                    }
                } else {
                    if (code == Global.EXIT_USER_CODE) {
                        mTitleTextView.setText("立即登录");
                        recyleview_my_pet.setVisibility(View.GONE);
                        viewpager.setVisibility(View.GONE);
                        layout_no_pets.setVisibility(View.VISIBLE);
                        my_money.setText(0 + "");
                        my_cashback.setText(0 + "");
                        my_dummy.setText(0 + "");
                        my_coupon.setText(0 + "");
                        my_cards.setText(0 + "");
                    }
                    Utils.Exit(mActivity, code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

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

    public void setTopHeight(float height) {
        ViewGroup.LayoutParams layoutParams = layout_use_click.getLayoutParams();
        layoutParams.height = (int) height;
        layout_use_click.setLayoutParams(layoutParams);
    }

    class Task extends TimerTask {
        private Timer timer;

        public Task(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run() {
            if (lastOverTime > 0) {
                lastOverTime -= 1000;
                Message msg = Message.obtain();
                msg.what = 0;
                msg.arg1 = (int) lastOverTime;
                handlerClearTopHeight.sendMessage(msg);
            } else {
                if (timer != null) {
                    handlerClearTopHeight.sendEmptyMessage(1);
                    this.timer.cancel();
                    try {
                        if (task != null) {
                            task.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timer = null;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_myfrag_jyzb:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_FosterLive);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, FosterLiveListActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.tv_myfrag_jyzb_fjnum:
                if (list.size() > 0) {
                    FosterLive item = list.get(0);
                    if (Utils.checkLogin(mContext)) {
                        if (Utils.isStrNull(item.getLiveUrl()) && item.getLiveState() == 1 && item.getCameraState() == 0) {
                            startActivity(new Intent(mContext, FosterLiveActivity.class).putExtra("videoUrl", item.getLiveUrl()).putExtra("name", item.getRoomContent().replace("@@", "")));
                        } else {
                            ToastUtil.showToastShortBottom(mContext, item.getLiveContent());
                        }
                    } else {
                        startActivity(new Intent(mContext, LoginNewActivity.class));
                    }
                }
                break;
            case R.id.iv_myfrag_xhrwone:
                if (listBanner.size() > 0) {
                    Banner banner = listBanner.get(0);
                    if (banner != null) {
                        Utils.goService(mActivity, banner.point, banner.backup);
                    }
                }
                break;
            case R.id.iv_myfrag_xhrw1:
                if (listBanner.size() > 0) {
                    Banner banner = listBanner.get(0);
                    if (banner != null) {
                        Utils.goService(mActivity, banner.point, banner.backup);
                    }
                }
                break;
            case R.id.iv_myfrag_xhrw2:
                if (listBanner.size() > 1) {
                    Banner banner = listBanner.get(1);
                    if (banner != null) {
                        Utils.goService(mActivity, banner.point, banner.backup);
                    }
                }
                break;
            case R.id.img_right:
                if (Utils.checkLogin(mContext)) {
                    goNext(NoticeListActivity.class);
                } else {
                    goNext(LoginNewActivity.class);
                }
                break;
            case R.id.textView_title:
            case R.id.imageView_avatar:
                if (!Utils.checkLogin(mContext)) {
                    goNext(LoginNewActivity.class);
                } else {
                    goNext(ChangeAccountActivity.class);
                }
                break;
            case R.id.img_one:
                goNext(0, pics);
                break;
            case R.id.img_two:
                goNext(1, pics);
                break;
            case R.id.img_thr:
                goNext(2, pics);
                break;
            case R.id.img_four:
                goNext(3, pics);
                break;
            case R.id.img_scan:
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MipcaActivityCapture.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.img_samll_icon:
                if (Utils.checkLogin(mContext)) {
                    goNext(NoticeListActivity.class);
                } else {
                    goNext(LoginNewActivity.class);
                }
                break;
            //我的宠物
            case R.id.layoutMyPet:
                if (Utils.checkLogin(mContext)) {
                    Intent intent = new Intent(mContext, ChooseMyPetActivity.class);
                    intent.putExtra("petKindstr", "1,2");
                    intent.putExtra("previous", Global.MY_CUSTOMERPET);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            //地址管理
            case R.id.layoutMyAddress:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_CommonAddress);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, CommonAddressActivity.class).putExtra("index", 1));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
                //我的宠圈
            case R.id.layoutCircle:
//                startActivity(new Intent(mContext, PetCircleOrSelectActivity.class));
                Intent intent1 = new Intent(mContext, PostUserInfoActivity.class);
                intent1.putExtra("userId", spUtil.getInt("userid",0));
                startActivity(intent1);
                break;
                //反馈与建议
            case R.id.layoutFeedBack:
                startActivity(new Intent(mContext, FeedBackActivity.class));
                break;
                //百科收藏
            case R.id.layoutMyClo:
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyCollectActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
                //我的评价
            case R.id.layoutMyEva:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_MyEvaluation);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyEvaluateActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.layoutSet:
                if (Utils.checkLogin(mContext)) {
                    Intent intent = new Intent(mContext,
                            SetActivity.class);
                    intent.putExtra("servicePhone", servicePhone);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }

                break;
                //我的账单
            case R.id.layoutrecord:
                if (!Utils.checkLogin(mContext)) {
                    goNext(LoginNewActivity.class);
                } else {
                    startActivity(new Intent(mContext,
                            MyBillActivity.class));
                }
                break;
            case R.id.vip_show_or_button:
                if (!Utils.checkLogin(mContext)) {
                    goNext(LoginNewActivity.class);
                } else {
                    startActivity(new Intent(getActivity(), MemberUpgradeActivity.class).putExtra("imgUrl", memberImgUrl));
                }
                break;
            case R.id.layout_use_click:
            case R.id.layout_bottom_show_vip:
            case R.id.vip_card_bottom:
            case R.id.vip_card_top:
                if (!Utils.checkLogin(mContext)) {
                    goNext(LoginNewActivity.class);
                } else {
                    startActivity(new Intent(getActivity(), MemberUpgradeActivity.class).putExtra("imgUrl", memberImgUrl));
                }
                break;
            case R.id.my_money:
            case R.id.layout_mymoney:
                spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK1", true);
                ll_mainfrag_yue_pop.setVisibility(View.INVISIBLE);
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_My_MyBalance);
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_mymoney_main);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyCardActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.layout_cashback:
                //spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK5", true);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, CashbackAmountActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.layout_mydummy:
            case R.id.my_dummy:
                spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK2", true);
                ll_mainfrag_guantou_pop.setVisibility(View.INVISIBLE);
                getStatistics(Global.ServerEventID.choose_main_page, Global.ServerEventID.click_mydummy_main);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyCanActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.layout_mycoupon:
            case R.id.my_coupon:
                spUtil.saveBoolean("MAINFRAG_TIP_TYPE_CLICK3", true);
                ll_mainfrag_yhq_pop.setVisibility(View.INVISIBLE);
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_My_MyCoupon);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyCouponNewActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.layout_mycards:
            case R.id.my_cards:
                break;
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
