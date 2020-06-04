package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.JoinWorkAdapter;
import com.haotang.pet.adapter.JoinWorkAdapterRemind;
import com.haotang.pet.adapter.ServiceMyPetRecycleAdapter;
import com.haotang.pet.adapter.ShopEvaRecyAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.RecommendServiceCard;
import com.haotang.pet.entity.RecommmendCashBack;
import com.haotang.pet.entity.ServiceNewBottomAds;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.ServiceType;
import com.haotang.pet.entity.ServiceTypeBanner;
import com.haotang.pet.entity.ServiceTypeTopMsg;
import com.haotang.pet.entity.ShopToServiceEvent;
import com.haotang.pet.entity.TabEntity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshChangeScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.FullyRecyLinearLayoutManager;
import com.haotang.pet.view.GlideImageLoaderFourRound;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.ObservablePullScrollView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceNewActivity extends SuperActivity implements OnBannerListener {
    public static ServiceNewActivity act;
    @BindView(R.id.textview_left_pet_title)
    TextView textview_left_pet_title;
    @BindView(R.id.img_choose_mypet)
    ImageView img_choose_mypet;
    @BindView(R.id.textview_left_address_title)
    TextView textview_left_address_title;
    @BindView(R.id.img_choose_address)
    ImageView img_choose_address;
    @BindView(R.id.address_detail)
    TextView address_detail;
    @BindView(R.id.textview_left_shop_title)
    TextView textview_left_shop_title;
    @BindView(R.id.img_choose_shop)
    ImageView img_choose_shop;
    @BindView(R.id.shop_address_detail)
    TextView shop_address_detail;
    @BindView(R.id.mListview_service_eva)
    RecyclerView mListview_service_eva;
    @BindView(R.id.recyclerView_mypet)
    RecyclerView recyclerView_mypet;
    @BindView(R.id.textview_servicename)
    TextView textview_servicename;
    @BindView(R.id.textview_service_count)
    TextView textview_service_count;
    @BindView(R.id.service_scaling_image)
    MListview service_scaling_image;
    @BindView(R.id.service_scaling_image_remind)
    MListview service_scaling_image_remind;
    @BindView(R.id.textview_add_pet)
    TextView textview_add_pet;
    @BindView(R.id.service_back_blow)
    ImageView service_back_blow;
    @BindView(R.id.layout_service_back_blow)
    LinearLayout layout_service_back_blow;
    @BindView(R.id.service_back)
    ImageView service_back;
    @BindView(R.id.layout_service_back)
    LinearLayout layout_service_back;
    @BindView(R.id.service_share_below)
    ImageView service_share_below;
    @BindView(R.id.service_share)
    ImageView service_share;
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.rl_servicedetail_title)
    RelativeLayout rl_servicedetail_title;
    @BindView(R.id.pullpushlayout)
    PullToRefreshChangeScrollView pullpushlayout;
    @BindView(R.id.layout_choose_pet)
    LinearLayout layoutChoosePet;
    @BindView(R.id.banner_item_detail)
    Banner bannerItemDetail;
    @BindView(R.id.mTabLayout_4)
    CommonTabLayout mTabLayout4;
    @BindView(R.id.relayoutChoosePet)
    RelativeLayout relayoutChoosePet;
    @BindView(R.id.layout_choose_pet_top)
    LinearLayout layoutChoosePetTop;
    @BindView(R.id.ll_servicenew_banner)
    LinearLayout ll_servicenew_banner;
    @BindView(R.id.banner_servicenew_banner)
    Banner banner_servicenew_banner;
    @BindView(R.id.tv_servicenew_fxprice)
    TextView tv_servicenew_fxprice;
    @BindView(R.id.tv_servicenew_price)
    TextView tv_servicenew_price;
    @BindView(R.id.tv_servicenew_cardprice)
    TextView tv_servicenew_cardprice;
    @BindView(R.id.iv_servicenew_cardprice)
    ImageView iv_servicenew_cardprice;
    private JoinWorkAdapter joinWorkAdapter;
    private ArrayList<String> listBannerStr = new ArrayList<String>();
    private ArrayList<ServiceTypeBanner> listBanner = new ArrayList<ServiceTypeBanner>();
    private ArrayList<ServiceTypeTopMsg> listStrs = new ArrayList<ServiceTypeTopMsg>();
    private List<Pet> listMyPets = new ArrayList<Pet>();
    private List<String> listReminds = new ArrayList<>();
    private ArrayList<ServiceType> listServiceType = new ArrayList<ServiceType>();
    private ArrayList<String> Imagelist = new ArrayList<String>();
    private ArrayList<Comment> evalists = new ArrayList<Comment>();
    private int serviceType;
    private int serviceId;
    private int addressId;
    private String address;
    private int workerId;
    private int shopId;
    private int shopActivePoint;
    private String shopActiveTitle;
    private String shopImg;
    private String shopName;
    private String shopActiveBackup;
    private String shopWxImg;
    private String shopWxNum;
    private String dist;
    private String shopPhone;
    private int addressAmount;
    private double lat;
    private double lng;
    private int shopIdNet;
    private int serviceLoc;
    private String shopAddress;
    private double shopLat;
    private double shopLng;
    private Beautician beautician = null;
    private ServiceShopAdd LastShop = new ServiceShopAdd();
    private ServiceShopAdd now = new ServiceShopAdd();
    private boolean isCancle = false;
    private ArrayList<ServiceNewBottomAds> adsBottoms = new ArrayList<ServiceNewBottomAds>();
    private int typeId;
    private ServiceMyPetRecycleAdapter myPetRecycleAdapter;
    private List<String> taglists = new ArrayList<>();
    private String totalAmount;
    private String strp = null;
    private int page = 1;
    private ShopEvaRecyAdapter shopEvaAdapter = null;
    private boolean isFirst = true;
    private boolean isFirstEva = true;
    private ServiceType serviceTypeClick = null;
    private String serviceName;
    private CommAddr commAddr = new CommAddr();
    private LinearLayout rl_ppllayout_top;
    private String shareImg;
    private String shareTitle;
    private String shareUrl;
    private String shareDesc;
    private PopupWindow pWin;
    private IWXAPI api;
    private Bitmap bmp;
    private LayoutInflater mInflater;
    private int mWidth = 0;
    private List<Pet> MyPetsMy = new ArrayList<>();
    private List<String> list = new ArrayList<String>();
    private int[] mIconUnselectIds = {
            R.drawable.icon_default, R.drawable.icon_default,
            R.drawable.icon_default, R.drawable.icon_default};
    private int[] mIconSelectIds = {
            R.drawable.icon_default, R.drawable.icon_default,
            R.drawable.icon_default, R.drawable.icon_default};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int height;
    private int[] colors = new int[2];
    boolean isTop = true;
    public static ArrayList<ServiceShopAdd> nows = new ArrayList<>();
    public String title;
    private JoinWorkAdapterRemind joinWorkAdapterRemind;
    private RecommendServiceCard recommendServiceCard = new RecommendServiceCard();
    private List<com.haotang.pet.entity.Banner> bannerList = new ArrayList<com.haotang.pet.entity.Banner>();
    private List<String> bannerImgList = new ArrayList<String>();
    private RecommmendCashBack recommmendCashBack = new RecommmendCashBack();
    private int cityId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShopToServiceEvent event) {
        if (event != null) {
            isCancle = true;
            if (TextUtils.isEmpty(event.getShopName())) {
                return;
            }
            if (event.getShopId() != now.shopId) {
                shopId = event.getShopId();
                cityId = event.getCityId();
                shopName = event.getShopName();
                shopAddress = event.getShopAddr();
                shopLat = event.getLat();
                shopLng = event.getLng();
                shopImg = event.getShopImg();
                dist = event.getDist();
                shopPhone = event.getShopPhone();
                LastShop.openTime = event.getOpemTime();
                LastShop.tag = event.getTag();
                LastShop.shopId = shopId;
                LastShop.shopName = shopName;
                LastShop.shoplat = shopLat;
                LastShop.shoplng = shopLng;
                LastShop.shopAddress = shopAddress;
                LastShop.shopImg = shopImg;
                LastShop.shopActiveTitle = shopActiveTitle;
                LastShop.shopActivePoint = shopActivePoint;
                LastShop.shopActiveBackup = shopActiveBackup;
                LastShop.shopWxNum = shopWxNum;
                LastShop.dist = dist;
                LastShop.shopWxImg = shopWxImg;
                LastShop.shopPhone = shopPhone;
                shop_address_detail.setText(LastShop.shopName);
                setGravity();
                getServiceType2();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        if (event != null) {
            getPetData();
            queryServiceDetail();
            queryCommentsByService();
        }
    }

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_service_5_0_1);
            ButterKnife.bind(this);
            if (!EventBus.getDefault().isRegistered(this)) {//加上判断
                EventBus.getDefault().register(this);
            }
            act = this;
            spUtil.removeData("alpha");
            MApplication.listAppoint.add(act);
            firstCleanList();
            getIntentData();
            initView();
            setListener();
            setData();
            setView();
            queryCommentsByService();
            getGiftCardListBanner();
        }

        private void getGiftCardListBanner () {
            mPDialog.showDialog();
            bannerList.clear();
            bannerImgList.clear();
            CommUtil.getGiftCardListBanner(mContext, 1, getGiftCardListBannerHandler);
        }

        private AsyncHttpResponseHandler getGiftCardListBannerHandler = new AsyncHttpResponseHandler() {

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
                            JSONObject jdata = jsonObject.getJSONObject("data");
                            if (jdata.has("otherOperateBannerList") && !jdata.isNull("otherOperateBannerList")) {
                                JSONArray jarrbanner = jdata.getJSONArray("otherOperateBannerList");
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
                            }
                        }
                    } else {
                        ToastUtil.showToastShort(ServiceNewActivity.this, msg);
                    }
                } catch (JSONException e) {
                    ToastUtil.showToastShort(ServiceNewActivity.this, "数据异常");
                    e.printStackTrace();
                }
                if (bannerList.size() > 0) {
                    ll_servicenew_banner.setVisibility(View.VISIBLE);
                    bannerImgList.clear();
                    for (int i = 0; i < bannerList.size(); i++) {
                        bannerImgList.add(bannerList.get(i).pic);
                    }
                    banner_servicenew_banner.setImages(bannerImgList)
                            .setImageLoader(new GlideImageLoaderFourRound())
                            .setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    if (bannerList.size() > position) {
                                        Utils.goService(mContext, bannerList.get(position).point, bannerList.get(position).backup);
                                    }
                                }
                            })
                            .start();
                } else {
                    ll_servicenew_banner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                mPDialog.closeDialog();
                ToastUtil.showToastShort(ServiceNewActivity.this, "请求失败");
            }
        };

        private void initView () {
            rl_ppllayout_top = (LinearLayout) findViewById(R.id.rl_ppllayout_top);
            mTabLayout4.setmTextSelectsize(mTabLayout4.sp2px(15));
            mTabLayout4.setGradient(true);
            colors[0] = getResources().getColor(R.color.aeb6340);
            colors[1] = getResources().getColor(R.color.ae5287b);
            mTabLayout4.setColors(colors);
            mTabLayout4.setIndicatorTextMiddle(true);
        }

        private void queryCommentsByService () {
            if (page == 1) {
                evalists.clear();
            }
            CommUtil.queryCommentsByService(mContext, serviceId + "", serviceType, typeId, page, -1, evaHandler);
        }

        private AsyncHttpResponseHandler evaHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    mPDialog.closeDialog();
                    pullpushlayout.onRefreshComplete();
                    JSONObject object = new JSONObject(new String(responseBody));
                    int code = object.getInt("code");
                    if (code == 0) {
                        if (object.has("data") && !object.isNull("data")) {
                            JSONObject objectData = object.getJSONObject("data");
                            if (objectData.has("totalAmount") && !objectData.isNull("totalAmount")) {
                                totalAmount = objectData.getString("totalAmount");
                            }
                            if (isFirstEva) {
                                isFirstEva = false;
                                taglists.clear();
                                taglists.add("服务介绍");
                                taglists.add("预约须知");
                                taglists.add("服务评价(" + totalAmount + ")");
                                mTabEntities.clear();
                                for (int i = 0; i < taglists.size(); i++) {
                                    mTabEntities.add(new TabEntity(taglists.get(i), mIconSelectIds[i], mIconUnselectIds[i]));
                                }
                                mTabLayout4.setTabData(mTabEntities);
                                mTabLayout4.setCurrentTab(0);
                            }
                            if (objectData.has("dataset") && !objectData.isNull("dataset")) {
                                JSONArray array = objectData.getJSONArray("dataset");
                                if (array.length() > 0) {
                                    page++;
                                    for (int i = 0; i < array.length(); i++) {
                                        evalists.add(Comment.json2Entity(array.getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mPDialog.closeDialog();
                }
                shopEvaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mPDialog.closeDialog();
            }
        };

        private void setListener () {
            ObservablePullScrollView observablePullScrollView = pullpushlayout.getRefreshableView();
            observablePullScrollView.setScrollViewListener(new ObservablePullScrollView.ScrollViewListener() {
                @Override
                public void onScrollChanged(ObservablePullScrollView scrollView, int x, int y, int oldX, int oldY) {
                    if (y <= 0) {   //设置标题的背景颜色
                        rl_servicedetail_title.setBackgroundColor(getResources().getColor(R.color.transparent));
                        service_back.setVisibility(View.VISIBLE);//黑的
                        service_back_blow.setVisibility(View.GONE);//黄的
                        service_share.setVisibility(View.VISIBLE);//黑色
                        service_share_below.setVisibility(View.GONE);//白色
                        isTop = true;
                    } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                        float scale = (float) y / height;
                        float alpha = (255 * scale);
                        textView_title.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                        rl_servicedetail_title.setBackgroundColor(Color.argb((int) alpha, 144, 151, 166));
                        service_back.setVisibility(View.VISIBLE);//黑的
                        service_back_blow.setVisibility(View.GONE);//黄的
                        service_share.setVisibility(View.VISIBLE);//黑色
                        service_share_below.setVisibility(View.GONE);//白色
                        isTop = false;
                    } else {    //滑动到banner下面设置普通颜色
                        rl_servicedetail_title.setBackgroundColor(getResources().getColor(R.color.a3a3636));
                        service_back.setVisibility(View.GONE);//黑的
                        service_back_blow.setVisibility(View.VISIBLE);//黄的
                        service_share.setVisibility(View.GONE);//黑色
                        service_share_below.setVisibility(View.VISIBLE);//白色
                        isTop = false;
                    }
                }
            });
            pullpushlayout.setMode(PullToRefreshBase.Mode.DISABLED);
            pullpushlayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ObservablePullScrollView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ObservablePullScrollView> refreshView) {
                    PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                    if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {
                        queryCommentsByService();
                    }
                }
            });
            mTabLayout4.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    if (position == 0) {
                        service_scaling_image.setVisibility(View.VISIBLE);
                        service_scaling_image_remind.setVisibility(View.GONE);
                        mListview_service_eva.setVisibility(View.GONE);
                        pullpushlayout.setMode(PullToRefreshBase.Mode.DISABLED);
                    } else if (position == 1) {
                        service_scaling_image.setVisibility(View.GONE);
                        service_scaling_image_remind.setVisibility(View.VISIBLE);
                        mListview_service_eva.setVisibility(View.GONE);
                        pullpushlayout.setMode(PullToRefreshBase.Mode.DISABLED);
                    } else if (position == 2) {
                        service_scaling_image.setVisibility(View.GONE);
                        service_scaling_image_remind.setVisibility(View.GONE);
                        mListview_service_eva.setVisibility(View.VISIBLE);
                        pullpushlayout.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    }
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
        }

        private void firstCleanList () {
            adsBottoms.clear();
            listBannerStr.clear();
            listBanner.clear();
            listStrs.clear();
            listServiceType.clear();
            listMyPets.clear();
            Imagelist.clear();
            taglists.clear();
        }

        private void getIntentData () {
            serviceType = getIntent().getIntExtra("serviceType", 0); //特色服务serviceType == 3  //服务类型(1:洗澡、2:美容、3:特色服务)
            typeId = getIntent().getIntExtra("typeId", 0); //特色服务serviceType == 3  //服务分类(1:洗护、2:寄养、3:游泳、4:训犬)
            serviceId = getIntent().getIntExtra("serviceId", 0); //特色服务id
            beautician = (Beautician) getIntent().getSerializableExtra("beautician");
            listMyPets.clear();
            MyPetsMy = (List<Pet>) getIntent().getSerializableExtra("myPetLIst");
            if (MyPetsMy != null && MyPetsMy.size() > 0) {
                for (int i = 0; i < MyPetsMy.size(); i++) {
                    MyPetsMy.get(i).serviceid = serviceId;
                }
                listMyPets.addAll(MyPetsMy);
                strp = getPetId_SERVICEID_MYPETID_ITEMID(listMyPets);
            }
            if (beautician != null) {
                workerId = beautician.id;
                serviceType = beautician.BeauDetailServiceType;
            }
        }

        private void setView () {
            Global.ServerEvent(ServiceNewActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.goto_service_detail_page);
            api = WXAPIFactory.createWXAPI(this, Global.APP_ID);
            mInflater = LayoutInflater.from(this);
            if (listMyPets.size() > 0) {
                textview_add_pet.setVisibility(View.GONE);
            }
            getPetData();
        }

        private void queryServiceDetail () {
            Imagelist.clear();
            listBannerStr.clear();
            CommUtil.queryServiceDetail(mContext, serviceType, strp, shopId, serviceId, serviceDetailHandler);
        }

        private AsyncHttpResponseHandler serviceDetailHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsob = new JSONObject(new String(responseBody));
                    int code = jsob.getInt("code");
                    if (code == 0) {
                        if (jsob.has("data") && !jsob.isNull("data")) {
                            JSONObject obj = jsob.getJSONObject("data");
                            if (obj.has("detailImgs") && !obj.isNull("detailImgs")) {
                                JSONArray array = obj.getJSONArray("detailImgs");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        Imagelist.add(array.getString(i));//服务介绍
                                    }
                                }
                            }
                            listBannerStr.clear();
                            if (obj.has("banners") && !obj.isNull("banners")) {
                                JSONArray array = obj.getJSONArray("banners");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objEva = array.getJSONObject(i);
                                        if (objEva.has("img") && !objEva.isNull("img")) {
                                            listBannerStr.add(objEva.getString("img"));//服务介绍
                                        }
                                    }
                                }
                            }
                            if (listBannerStr.size() > 0) {
                                setBanner();
                            }
                            List<Pet> pets = new ArrayList<>();
                            pets.clear();
                            if (obj.has("serviceIds") && !obj.isNull("serviceIds")) {
                                JSONArray array = obj.getJSONArray("serviceIds");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        Pet netPet = new Pet();
                                        JSONObject object = array.getJSONObject(i);
                                        if (object.has("myPetId") && !object.isNull("myPetId")) {
                                            int myPetId = object.getInt("myPetId");
                                            netPet.customerpetid = myPetId;
                                        }
                                        if (object.has("serviceId") && !object.isNull("serviceId")) {
                                            int serviceId = object.getInt("serviceId");
                                            netPet.serviceid = serviceId;
                                        }
                                        pets.add(netPet);
                                    }

                                    if (array.length() == 1){
                                        JSONObject object = array.getJSONObject(0);
                                        //给ServiceId赋值
                                        if (object.has("serviceId") && !object.isNull("serviceId")) {
                                            ServiceNewActivity.this.serviceId = object.getInt("serviceId");
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < listMyPets.size(); i++) {
                                Pet myPet = listMyPets.get(i);
                                for (int j = 0; j < pets.size(); j++) {
                                    Pet netPet = pets.get(i);
                                    if (myPet.customerpetid == netPet.customerpetid) {
                                        Log.e("TAG", "netPet.serviceid = " + netPet.serviceid);
                                        myPet.serviceid = netPet.serviceid;
                                    }
                                }
                            }
                            strp = getPetId_SERVICEID_MYPETID_ITEMID(listMyPets);
                            Utils.mLogError("==-->选择宠物返回重新调用queryServiceDetail拼接strp 11 " + strp);
                        }
                        if (isFirst) {
                            isFirst = false;
                            getServiceType();
                        } else {
                            getServiceType2();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (Imagelist.size() > 0) {
                    service_scaling_image.setVisibility(View.VISIBLE);
                } else {
                    service_scaling_image.setVisibility(View.GONE);
                }
                joinWorkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        private void setData () {
            joinWorkAdapterRemind = new JoinWorkAdapterRemind(mContext, listReminds);
            service_scaling_image_remind.setAdapter(joinWorkAdapterRemind);

            joinWorkAdapter = new JoinWorkAdapter<String>(mContext, Imagelist);
            service_scaling_image.setAdapter(joinWorkAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView_mypet.setLayoutManager(linearLayoutManager);
            myPetRecycleAdapter = new ServiceMyPetRecycleAdapter(mContext, listMyPets);
            recyclerView_mypet.setAdapter(myPetRecycleAdapter);

            mListview_service_eva.setLayoutManager(new FullyRecyLinearLayoutManager(this));
            shopEvaAdapter = new ShopEvaRecyAdapter(R.layout.item_shopevalist, evalists);
            mListview_service_eva.setAdapter(shopEvaAdapter);
            mListview_service_eva.setNestedScrollingEnabled(false);
            shopEvaAdapter.setAcType(0);
            mListview_service_eva.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL, DensityUtil.dp2px(this, 1), ContextCompat.getColor(this, R.color.aEEEEEE)));
        }

        @Override
        protected void onResume () {
            super.onResume();
            mWidth = Utils.getDisplayMetrics(this)[0];
            setLayout(mWidth);
            setGravity();
        }

        private void setLayout ( int mWidth){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_ppllayout_top.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = mWidth * 380 / 710;
            layoutParams.leftMargin = Utils.dip2px(mContext, 0);
            layoutParams.rightMargin = Utils.dip2px(mContext, 0);
            rl_ppllayout_top.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams Params = (LinearLayout.LayoutParams) bannerItemDetail.getLayoutParams();
            Params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            Params.height = mWidth * 380 / 710;

            Params.leftMargin = Utils.dip2px(mContext, 10);
            Params.rightMargin = Utils.dip2px(mContext, 10);
            bannerItemDetail.setLayoutParams(Params);

            ViewTreeObserver vto = rl_ppllayout_top.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    height = rl_ppllayout_top.getHeight();
                }
            });
        }

        private void setGravity () {
            if (TextUtils.isEmpty(address_detail.getText())) {
                address_detail.setText("请选择地址");
                address_detail.setTextColor(Color.parseColor("#bbbbbb"));
                address_detail.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }
            if (TextUtils.isEmpty(shop_address_detail.getText())) {
                shop_address_detail.setText("请选择门店");
                shop_address_detail.setTextColor(Color.parseColor("#bbbbbb"));
                shop_address_detail.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }
            if (!TextUtils.isEmpty(address_detail.getText()) && ((address_detail.getText().toString()).contains("选择"))) {
                address_detail.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                address_detail.setTextColor(Color.parseColor("#bbbbbb"));
            } else if (!TextUtils.isEmpty(address_detail.getText()) && (!(address_detail.getText().toString()).contains("选择"))) {
                address_detail.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                address_detail.setTextColor(Color.parseColor("#666666"));
            }
            if (!TextUtils.isEmpty(shop_address_detail.getText()) && ((shop_address_detail.getText().toString()).contains("选择"))) {
                shop_address_detail.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                shop_address_detail.setTextColor(Color.parseColor("#bbbbbb"));
            } else if (!TextUtils.isEmpty(shop_address_detail.getText()) && (!(shop_address_detail.getText().toString()).contains("选择"))) {
                shop_address_detail.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                shop_address_detail.setTextColor(Color.parseColor("#666666"));
            }
        }

        private void getPetData () {
            Imagelist.clear();
            listReminds.clear();
            mPDialog.showDialog();
            mPDialog.setCancelable(false);
            mPDialog.setCanceledOnTouchOutside(false);
            CommUtil.queryLastOrderInfo(mContext, serviceType, serviceId, workerId, handler);
        }

        private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    mPDialog.closeDialog();
                    JSONObject object = new JSONObject(new String(responseBody));
                    int code = object.getInt("code");
                    if (code == 0) {
                        if (object.has("data") && !object.isNull("data")) {
                            JSONObject objectData = object.getJSONObject("data");
                            if (objectData.has("share") && !objectData.isNull("share")) {
                                JSONObject objShare = objectData.getJSONObject("share");
                                if (objShare.has("img") && !objShare.isNull("img")) {
                                    shareImg = objShare.getString("img");
                                }
                                if (objShare.has("title") && !objShare.isNull("title")) {
                                    shareTitle = objShare.getString("title");
                                }
                                if (objShare.has("url") && !objShare.isNull("url")) {
                                    shareUrl = objShare.getString("url");
                                }
                                if (objShare.has("desc") && !objShare.isNull("desc")) {
                                    shareDesc = objShare.getString("desc");
                                }
                            }
                            if (objectData.has("remind") && !objectData.isNull("remind")) {
                                JSONArray arrayRemind = objectData.getJSONArray("remind");
                                if (arrayRemind.length() > 0) {
                                    for (int i = 0; i < arrayRemind.length(); i++) {
                                        listReminds.add(arrayRemind.getString(i));
                                    }
                                }
                            }
                            if (objectData.has("service") && !objectData.isNull("service")) {
                                JSONObject objService = objectData.getJSONObject("service");
                                if (objService.has("serviceAmount") && !objService.isNull("serviceAmount")) {
                                    int serviceAmount = objService.getInt("serviceAmount");
                                    textview_service_count.setText("已服务" + serviceAmount + "单");
                                }
                            }
                            if (MyPetsMy == null || MyPetsMy.size() <= 0) {
                                listMyPets.clear();
                                if (objectData.has("customerPet") && !objectData.isNull("customerPet")) {
                                    Pet pet = new Pet();
                                    JSONObject customerPet = objectData.getJSONObject("customerPet");
                                    if (customerPet.has("petId") && !customerPet.isNull("petId")) {
                                        pet.id = customerPet.getInt("petId");
                                    }
                                    if (customerPet.has("id") && !customerPet.isNull("id")) {
                                        pet.customerpetid = customerPet.getInt("id");
                                    }
                                    if (customerPet.has("nickName") && !customerPet.isNull("nickName")) {
                                        pet.nickName = customerPet.getString("nickName");
                                    }
                                    if (customerPet.has("avatar") && !customerPet.isNull("avatar")) {
                                        pet.image = customerPet.getString("avatar");
                                    }
                                    if (customerPet.has("petKind") && !customerPet.isNull("petKind")) {
                                        pet.kindid = customerPet.getInt("petKind");
                                    }
                                    pet.serviceid = serviceId;
                                    listMyPets.add(pet);
                                    if (listMyPets.size() > 0) {
                                        myPetRecycleAdapter.notifyDataSetChanged();
                                        textview_add_pet.setVisibility(View.GONE);
                                    } else {
                                        textview_add_pet.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (objectData.has("address") && !objectData.isNull("address")) {
                                    JSONObject objectAddr = objectData.getJSONObject("address");
                                    if (objectAddr.has("id") && !objectAddr.isNull("id")) {
                                        addressId = objectAddr.getInt("id");
                                        commAddr.Customer_AddressId = addressId;
                                    }
                                    if (objectAddr.has("address") && !objectAddr.isNull("address")) {
                                        address = objectAddr.getString("address");
                                        address_detail.setText(address);
                                        setGravity();
                                        commAddr.address = address;
                                    }
                                    if (objectAddr.has("lat") && !objectAddr.isNull("lat")) {
                                        lat = objectAddr.getDouble("lat");
                                        commAddr.lat = lat;
                                    }
                                    if (objectAddr.has("lng") && !objectAddr.isNull("lng")) {
                                        lng = objectAddr.getDouble("lng");
                                        commAddr.lng = lng;
                                    }
                                    if (objectAddr.has("telephone") && !objectAddr.isNull("telephone")) {
                                        String telephone = objectAddr.getString("telephone");
                                        commAddr.telephone = telephone;
                                    }
                                    if (objectAddr.has("supplement") && !objectAddr.isNull("supplement")) {
                                        String supplement = objectAddr.getString("supplement");
                                        commAddr.supplement = supplement;
                                    }
                                    if (objectAddr.has("linkman") && !objectAddr.isNull("linkman")) {
                                        String linkman = objectAddr.getString("linkman");
                                        commAddr.linkman = linkman;
                                    }
                                }
                                if (spUtil.getInt("nowShopId",0)!=0){
                                    shopId = spUtil.getInt("nowShopId",0);
                                    cityId = spUtil.getInt("nowShopCityId",0);
                                    shopName = spUtil.getString("nowShop","");
                                    shopAddress = spUtil.getString("nowShopAddr","");
                                    shopLat = spUtil.getFloat("nowShopLat",0);
                                    shopLng = spUtil.getFloat("nowShopLng",0);
                                    shopPhone = spUtil.getString("shopPhone","");
                                    LastShop.openTime = spUtil.getString("openTime","");
                                    LastShop.shopId = shopId;
                                    LastShop.shopName = shopName;
                                    LastShop.shoplat = shopLat;
                                    LastShop.shoplng = shopLng;
                                    LastShop.shopAddress = shopAddress;
                                    LastShop.shopWxNum = spUtil.getString("shopWxNum","");
                                    LastShop.shopWxImg = spUtil.getString("shopWxImg","");
                                    LastShop.shopPhone = shopPhone;

                                    shop_address_detail.setText(LastShop.shopName);
                                    setGravity();
                                }else {
                                    if (objectData.has("shop") && !objectData.isNull("shop")) {
                                        JSONObject objectShop = objectData.getJSONObject("shop");
                                        if (objectShop.has("cityId") && !objectShop.isNull("cityId")) {
                                            cityId = objectShop.getInt("cityId");
                                        }
                                        if (objectShop.has("shopActiveTitle") && !objectShop.isNull("shopActiveTitle")) {
                                            shopActiveTitle = objectShop.getString("shopActiveTitle");
                                            LastShop.shopActiveTitle = shopActiveTitle;
                                        }
                                        if (objectShop.has("shopWxImg") && !objectShop.isNull("shopWxImg")) {
                                            shopWxImg = objectShop.getString("shopWxImg");
                                            LastShop.shopWxImg = shopWxImg;
                                        }
                                        if (objectShop.has("shopActiveBackup") && !objectShop.isNull("shopActiveBackup")) {
                                            shopActiveBackup = objectShop.getString("shopActiveBackup");
                                            LastShop.shopActiveBackup = shopActiveBackup;
                                        }
                                        if (objectShop.has("shopWxNum") && !objectShop.isNull("shopWxNum")) {
                                            shopWxNum = objectShop.getString("shopWxNum");
                                            LastShop.shopWxNum = shopWxNum;
                                        }
                                        if (objectShop.has("shopActivePoint") && !objectShop.isNull("shopActivePoint")) {
                                            shopActivePoint = objectShop.getInt("shopActivePoint");
                                            LastShop.shopActivePoint = shopActivePoint;
                                        }
                                        if (objectShop.has("dist") && !objectShop.isNull("dist")) {
                                            dist = objectShop.getString("dist");
                                            LastShop.dist = dist;
                                        }
                                        if (objectShop.has("img") && !objectShop.isNull("img")) {
                                            shopImg = objectShop.getString("img");
                                            LastShop.shopImg = shopImg;
                                        }
                                        if (objectShop.has("address") && !objectShop.isNull("address")) {
                                            shopAddress = objectShop.getString("address");
                                            LastShop.shopAddress = shopAddress;
                                        }
                                        if (objectShop.has("lat") && !objectShop.isNull("lat")) {
                                            shopLat = objectShop.getDouble("lat");
                                            LastShop.shoplat = shopLat;
                                        }
                                        if (objectShop.has("lng") && !objectShop.isNull("lng")) {
                                            shopLng = objectShop.getDouble("lng");
                                            LastShop.shoplng = shopLng;
                                        }
                                        if (objectShop.has("phone") && !objectShop.isNull("phone")) {
                                            shopPhone = objectShop.getString("phone");
                                            LastShop.shopPhone = shopPhone;
                                        }
                                        if (objectShop.has("id") && !objectShop.isNull("id")) {
                                            shopId = objectShop.getInt("id");
                                            LastShop.shopId = shopId;
                                        }
                                        if (objectShop.has("shopName") && !objectShop.isNull("shopName")) {
                                            shopName = objectShop.getString("shopName");
                                            LastShop.shopName = shopName;
                                            shop_address_detail.setText(shopName);
                                            setGravity();
                                        }
                                        if (objectShop.has("openTime") && !objectShop.isNull("openTime")) {
                                            LastShop.openTime = objectShop.getString("openTime");
                                        }
                                        if (objectShop.has("tag") && !objectShop.isNull("tag")) {
                                            LastShop.tag = objectShop.getString("tag");
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        ToastUtil.showToastShort(mContext, object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.mLogError("==-->" + e.getMessage());
                }
                if (Imagelist.size() > 0) {
                    service_scaling_image.setVisibility(View.VISIBLE);
                } else {
                    service_scaling_image.setVisibility(View.GONE);
                }
                joinWorkAdapter.notifyDataSetChanged();
                if (listReminds.size() > 0) {
                    service_scaling_image_remind.setVisibility(View.VISIBLE);
                } else {
                    service_scaling_image_remind.setVisibility(View.GONE);
                }
                joinWorkAdapterRemind.notifyDataSetChanged();
                strp = getPetId_SERVICEID_MYPETID_ITEMID(listMyPets);
                isCancle = true;
                queryServiceDetail();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                mPDialog.closeDialog();
            }
        };

        private String getPetID_ServiceId_Mypet_ServiceType_PetKind_ItemIds () {
            if (listMyPets.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < listMyPets.size(); i++) {
                    Pet myPet = listMyPets.get(i);
                    if (i < listMyPets.size() - 1) {
                        sb.append(myPet.id);
                        sb.append("_");
                        sb.append("0");
                        sb.append("_");
                        sb.append(myPet.customerpetid);
                        sb.append("_");
                        sb.append(serviceType);
                        sb.append("_");
                        sb.append(0);
                        sb.append("_");
                        sb.append("0");
                        sb.append("_");
                    } else {
                        sb.append(myPet.id);
                        sb.append("_");
                        sb.append("0");
                        sb.append("_");
                        sb.append(myPet.customerpetid);
                        sb.append("_");
                        sb.append(serviceType);
                        sb.append("_");
                        sb.append("0");
                        sb.append("_");
                        sb.append("0");
                    }
                }
                return sb.toString();
            } else {
                return null;
            }
        }

        private void showAddressNumZeroDialog () {
            MDialog mDialog = new MDialog.Builder(mContext)
                    .setType(MDialog.DIALOGTYPE_ALERT)
                    .setMessage("先填写地址我们会帮您推荐附近店铺哦")
                    .setCancelable(false)
                    .setOKStr("去填写地址")
                    .positiveListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            goNext(AddServiceAddressActivity.class, Global.SERVICE_NEW_TO_ADD_ADDRESS_REQUESTCODE, Global.SERVICE_NEW_TO_ADD_ADDRESS);
                        }
                    }).build();
            mDialog.show();
        }

        private void goAppoint (Class cls){
            Intent intent = new Intent(mContext, cls);
            intent.putExtra("shopLat", LastShop.shoplat);
            intent.putExtra("shopLng", LastShop.shoplng);
            intent.putExtra("recommendServiceCard", recommendServiceCard);
            intent.putExtra("recommmendCashBack", recommmendCashBack);
            intent.putExtra("shopImg", LastShop.shopImg);
            intent.putExtra("shopWxImg", LastShop.shopWxImg);
            intent.putExtra("shopWxNum", LastShop.shopWxNum);
            intent.putExtra("shopActiveBackup", LastShop.shopActiveBackup);
            intent.putExtra("shopActivePoint", LastShop.shopActivePoint);
            intent.putExtra("dist", LastShop.dist);
            intent.putExtra("shopActiveTitle", LastShop.shopActiveTitle);
            intent.putExtra("shopPhone", LastShop.shopPhone);
            intent.putExtra("myPets", (Serializable) listMyPets);
            intent.putExtra("commAddr", commAddr);
            intent.putExtra("cityId", cityId);
            intent.putExtra("beautician", beautician);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("shop", LastShop);
            intent.putExtra("serviceType", serviceTypeClick);
            intent.putExtra("servicetype", serviceType);
            intent.putExtra("petAddress", address);
            intent.putExtra("addressId", addressId);
            intent.putExtra("serviceName", serviceName);
            intent.putExtra("serviceId", serviceId);
            intent.putExtra("strp", strp);
            Utils.mLogError("==-->shlat " + lat + "    lng " + lng + " shopImg " + shopImg + "  LastShop.shopImg " + LastShop.shopImg);
            startActivity(intent);
        }

        private void showAddressDialog () {
            MDialog mDialog = new MDialog.Builder(mContext)
                    .setTitle("")
                    .setTitleShow(0)//1 显示  其他不显示
                    .setType(MDialog.DIALOGTYPE_CONFIRM)
                    .setMessage("请您选择地址再下单哦")
                    .setCancelStr("取消")
                    .setOKStr("选择地址")
                    .setTitleTextColor(R.color.a333333)
                    .positiveListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (addressAmount <= 0) {
                                goNext(AddServiceAddressActivity.class, Global.SERVICE_NEW_TO_ADD_ADDRESS_REQUESTCODE, Global.SERVICE_NEW_TO_ADD_ADDRESS);
                            } else {
                                goNext(CommonAddressActivity.class, Global.SERVICE_NEW_TO_CHOOSEADDRESS, Global.BOOKINGSERVICEREQUESTCODE_ADDR);
                            }
                        }
                    }).build();
            mDialog.show();
        }

        private void showShopDialog () {
            MDialog mDialog = new MDialog.Builder(mContext)
                    .setTitle("")
                    .setTitleShow(0)//1 显示  其他不显示
                    .setType(MDialog.DIALOGTYPE_CONFIRM)
                    .setMessage("请您先选择门店再下单哦")
                    .setCancelStr("取消")
                    .setOKStr("选择门店")
                    .setTitleTextColor(R.color.a333333)
                    .positiveListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (Utils.checkLogin(mContext)) {
                                if (addressAmount <= 0) {
                                    showAddressNumZeroDialog();
                                } else {
                                    goNext(AllShopLocActivity.class, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST);
                                }
                            } else {
                                goNext(AllShopLocActivity.class, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST);
                            }
                        }
                    }).build();
            mDialog.show();
        }

        private void goNext (Class cls,int requestCode, int previous){
            Intent intent = new Intent(mContext, cls);
            intent.putExtra("previous", previous);
            intent.putExtra("strp_long", getPetID_ServiceId_Mypet_ServiceType_PetKind_ItemIds());//门店列表用
            intent.putExtra("lat", LastShop.shoplat);//门店列表用
            intent.putExtra("lng", LastShop.shoplng);//门店列表用
            intent.putExtra("addr_lat", lat);//门店列表用
            intent.putExtra("addr_lng", lng);//门店列表用
            intent.putExtra("addr_id", addressId);
            intent.putExtra("address", address);
            intent.putExtra("addressAmount", addressAmount);
            intent.putExtra("shopid", LastShop.shopId);
            intent.putExtra("shopname", LastShop.shopName);
            intent.putExtra("shopaddr", LastShop.shopAddress);
            intent.putExtra("shopimg", LastShop.shopImg);
            intent.putExtra("shopActiveTitle", LastShop.shopActiveTitle);
            intent.putExtra("shopActivePoint", LastShop.shopActivePoint);
            intent.putExtra("dist", LastShop.dist);
            intent.putExtra("shopActiveBackup", LastShop.shopActiveBackup);
            intent.putExtra("shopWxNum", LastShop.shopWxNum);
            intent.putExtra("shopWxImg", LastShop.shopWxImg);
            intent.putExtra("shopPhone", LastShop.shopPhone);
            startActivityForResult(intent, requestCode);
        }

        private void getServiceType () {
            if (!mPDialog.isShowing()) {
                mPDialog.showDialog();
                mPDialog.setCancelable(false);
            }
            if (shopId <= 0) {
                CommUtil.queryServicePrice(mContext, serviceType, strp, addressId, workerId, shopIdNet, typeId, lat, lng, serviceId, ServiceHandler);
            } else {
                CommUtil.queryServicePrice(mContext, serviceType, strp, addressId, workerId, shopId, typeId, lat, lng, serviceId, ServiceHandler);
            }
        }

        private AsyncHttpResponseHandler ServiceHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mPDialog.closeDialog();
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    int code = object.getInt("code");
                    if (code == 0) {
                        if (object.has("data") && !object.isNull("data")) {
                            JSONObject obJsonObject = object.getJSONObject("data");
                            if (obJsonObject.has("recommmendCashBack") && !obJsonObject.isNull("recommmendCashBack")) {
                                JSONObject jrecommmendCashBack = obJsonObject.getJSONObject("recommmendCashBack");
                                if (jrecommmendCashBack.has("tip") && !jrecommmendCashBack.isNull("tip")) {
                                    recommmendCashBack.setTip(jrecommmendCashBack.getString("tip"));
                                }
                                if (jrecommmendCashBack.has("cashBackIsOpen") && !jrecommmendCashBack.isNull("cashBackIsOpen")) {
                                    recommmendCashBack.setCashBackIsOpen(jrecommmendCashBack.getInt("cashBackIsOpen"));
                                }
                                if (jrecommmendCashBack.has("grainGoldRate") && !jrecommmendCashBack.isNull("grainGoldRate")) {
                                    recommmendCashBack.setGrainGoldRate(jrecommmendCashBack.getDouble("grainGoldRate"));
                                }
                            }
                            if (obJsonObject.has("serviceTitle") && !obJsonObject.isNull("serviceTitle")) {
                                serviceName = obJsonObject.getString("serviceTitle");
                                textView_title.setText(serviceName);
                                textview_servicename.setText(serviceName);
                            }
                            if (obJsonObject.has("recommendServiceCard") && !obJsonObject.isNull("recommendServiceCard")) {
                                JSONObject jrecommendServiceCard = obJsonObject.getJSONObject("recommendServiceCard");
                                if (jrecommendServiceCard.has("tip") && !jrecommendServiceCard.isNull("tip")) {
                                    recommendServiceCard.setTip(jrecommendServiceCard.getString("tip"));
                                }
                                if (jrecommendServiceCard.has("btn_txt") && !jrecommendServiceCard.isNull("btn_txt")) {
                                    recommendServiceCard.setBtn_txt(jrecommendServiceCard.getString("btn_txt"));
                                }
                                if (jrecommendServiceCard.has("point") && !jrecommendServiceCard.isNull("point")) {
                                    recommendServiceCard.setPoint(jrecommendServiceCard.getInt("point"));
                                }
                                if (jrecommendServiceCard.has("discount") && !jrecommendServiceCard.isNull("discount")) {
                                    recommendServiceCard.setDiscount(jrecommendServiceCard.getDouble("discount"));
                                }
                                if (jrecommendServiceCard.has("serviceCardId") && !jrecommendServiceCard.isNull("serviceCardId")) {
                                    recommendServiceCard.setServiceCardId(jrecommendServiceCard.getInt("serviceCardId"));
                                }
                            }
                            nows.clear();
                            if (obJsonObject.has("shops") && !obJsonObject.isNull("shops")) {
                                JSONObject objShop = obJsonObject.getJSONObject("shops");
                                if (objShop.has("title") && !objShop.isNull("title")) {
                                    title = objShop.getString("title");
                                }
                                if (objShop.has("dataset") && !objShop.isNull("dataset")) {
                                    JSONArray array = objShop.getJSONArray("dataset");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            ServiceShopAdd nowEva = new ServiceShopAdd();
                                            JSONObject evaObj = array.getJSONObject(i);
                                            if (evaObj.has("img") && !evaObj.isNull("img")) {
                                                nowEva.shopImg = evaObj.getString("img");
                                            }
                                            if (evaObj.has("address") && !evaObj.isNull("address")) {
                                                nowEva.shopAddress = evaObj.getString("address");
                                            }
                                            if (evaObj.has("lng") && !evaObj.isNull("lng")) {
                                                nowEva.shoplng = evaObj.getDouble("lng");
                                            }
                                            if (evaObj.has("lat") && !evaObj.isNull("lat")) {
                                                nowEva.shoplat = evaObj.getDouble("lat");
                                            }
                                            if (evaObj.has("phone") && !evaObj.isNull("phone")) {
                                                nowEva.shopPhone = evaObj.getString("phone");
                                            }
                                            if (evaObj.has("shopName") && !evaObj.isNull("shopName")) {
                                                nowEva.shopName = evaObj.getString("shopName");
                                            }
                                            if (evaObj.has("id") && !evaObj.isNull("id")) {
                                                nowEva.shopId = evaObj.getInt("id");
                                            }
                                            if (evaObj.has("openTime") && !evaObj.isNull("openTime")) {
                                                nowEva.openTime = evaObj.getString("openTime");
                                            }
                                            if (evaObj.has("dist") && !evaObj.isNull("dist")) {
                                                nowEva.dist = evaObj.getString("dist");
                                            }
                                            if (evaObj.has("goodRate") && !evaObj.isNull("goodRate")) {
                                                nowEva.goodRate = evaObj.getString("goodRate");
                                            }
                                            nows.add(nowEva);
                                        }
                                    }
                                }
                            }
                            if (workerId > 0) {
                                now = nows.get(0);
                                LastShop.shopId = now.shopId;
                                LastShop.shopAddress = now.shopAddress;
                                LastShop.shoplat = now.shoplat;
                                LastShop.shoplng = now.shoplng;
                                LastShop.shopName = now.shopName;
                                LastShop.shopImg = now.shopImg;
                                LastShop.shopWxImg = now.shopWxImg;
                                LastShop.shopWxNum = now.shopWxNum;
                                LastShop.dist = now.dist;
                                LastShop.shopActiveBackup = now.shopActiveBackup;
                                LastShop.shopActivePoint = now.shopActivePoint;
                                LastShop.shopActiveTitle = now.shopActiveTitle;
                                LastShop.shopPhone = now.shopPhone;
                                LastShop.openTime = now.openTime;
                                LastShop.tag = now.tag;
                                shop_address_detail.setText(LastShop.shopName);
                                setGravity();
                            }
                            listServiceType.clear();
                            if (obJsonObject.has("services") && !obJsonObject.isNull("services")) {
                                JSONArray array = obJsonObject.getJSONArray("services");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jServices = array.getJSONObject(i);
                                        if (jServices.has("serviceLoc") && !jServices.isNull("serviceLoc")) {
                                            if (jServices.getInt("serviceLoc") == 1) {
                                                listServiceType.add(ServiceType.json2Entity(array.getJSONObject(i)));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        ToastUtil.showToastShort(mContext, object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.mLogError(e.getMessage() + " " + e.getCause());
                }
                if (listServiceType.size() > 0) {
                    ServiceType serviceType = listServiceType.get(0);
                    String price = serviceType.price;
                    String vip_price = serviceType.vip_price;
                    Utils.setText(tv_servicenew_fxprice, serviceType.grain_gold_price, "", View.VISIBLE, View.GONE);
                    if (Utils.isStrNull(price)) {
                        tv_servicenew_price.setVisibility(View.VISIBLE);
                        tv_servicenew_price.setText(price.split("@@")[1]);
                    } else {
                        tv_servicenew_price.setVisibility(View.GONE);
                    }
                    if (Utils.isStrNull(vip_price)) {
                        iv_servicenew_cardprice.setVisibility(View.VISIBLE);
                        tv_servicenew_cardprice.setVisibility(View.VISIBLE);
                        tv_servicenew_cardprice.setText(vip_price.replace("@@", ""));
                    } else {
                        iv_servicenew_cardprice.setVisibility(View.GONE);
                        tv_servicenew_cardprice.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                mPDialog.closeDialog();
            }
        };

        private void getServiceType2 () {
            nows.clear();
            if (!mPDialog.isShowing()) {
                mPDialog.showDialog();
                mPDialog.setCancelable(false);
                mPDialog.setCanceledOnTouchOutside(false);
            }
            Utils.mLogError("==-->111111  shopid " + LastShop.shopId + " 222222 " + now.shopId + " 22222 " + shopId);
            if (shopId <= 0) {
                CommUtil.queryServicePrice(mContext, serviceType, strp, addressId, workerId, shopIdNet, typeId, lat, lng, serviceId, ServiceHandler2);
            } else {
                CommUtil.queryServicePrice(mContext, serviceType, strp, addressId, workerId, shopId, typeId, lat, lng, serviceId, ServiceHandler2);
            }
        }

        private AsyncHttpResponseHandler ServiceHandler2 = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    int code = object.getInt("code");
                    if (code == 0) {
                        if (object.has("data") && !object.isNull("data")) {
                            JSONObject obJsonObject = object.getJSONObject("data");
                            nows.clear();
                            if (obJsonObject.has("recommmendCashBack") && !obJsonObject.isNull("recommmendCashBack")) {
                                JSONObject jrecommmendCashBack = obJsonObject.getJSONObject("recommmendCashBack");
                                if (jrecommmendCashBack.has("tip") && !jrecommmendCashBack.isNull("tip")) {
                                    recommmendCashBack.setTip(jrecommmendCashBack.getString("tip"));
                                }
                                if (jrecommmendCashBack.has("cashBackIsOpen") && !jrecommmendCashBack.isNull("cashBackIsOpen")) {
                                    recommmendCashBack.setCashBackIsOpen(jrecommmendCashBack.getInt("cashBackIsOpen"));
                                }
                                if (jrecommmendCashBack.has("grainGoldRate") && !jrecommmendCashBack.isNull("grainGoldRate")) {
                                    recommmendCashBack.setGrainGoldRate(jrecommmendCashBack.getDouble("grainGoldRate"));
                                }
                            }
                            if (obJsonObject.has("recommendServiceCard") && !obJsonObject.isNull("recommendServiceCard")) {
                                JSONObject jrecommendServiceCard = obJsonObject.getJSONObject("recommendServiceCard");
                                if (jrecommendServiceCard.has("tip") && !jrecommendServiceCard.isNull("tip")) {
                                    recommendServiceCard.setTip(jrecommendServiceCard.getString("tip"));
                                }
                                if (jrecommendServiceCard.has("btn_txt") && !jrecommendServiceCard.isNull("btn_txt")) {
                                    recommendServiceCard.setBtn_txt(jrecommendServiceCard.getString("btn_txt"));
                                }
                                if (jrecommendServiceCard.has("point") && !jrecommendServiceCard.isNull("point")) {
                                    recommendServiceCard.setPoint(jrecommendServiceCard.getInt("point"));
                                }
                                if (jrecommendServiceCard.has("discount") && !jrecommendServiceCard.isNull("discount")) {
                                    recommendServiceCard.setDiscount(jrecommendServiceCard.getDouble("discount"));
                                }
                                if (jrecommendServiceCard.has("serviceCardId") && !jrecommendServiceCard.isNull("serviceCardId")) {
                                    recommendServiceCard.setServiceCardId(jrecommendServiceCard.getInt("serviceCardId"));
                                }
                            }
                            if (obJsonObject.has("shops") && !obJsonObject.isNull("shops")) {
                                JSONObject objShop = obJsonObject.getJSONObject("shops");
                                if (objShop.has("title") && !objShop.isNull("title")) {
                                    title = objShop.getString("title");
                                }
                                if (objShop.has("dataset") && !objShop.isNull("dataset")) {
                                    JSONArray array = objShop.getJSONArray("dataset");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            ServiceShopAdd nowEva = new ServiceShopAdd();
                                            JSONObject evaObj = array.getJSONObject(i);
                                            if (evaObj.has("img") && !evaObj.isNull("img")) {
                                                nowEva.shopImg = evaObj.getString("img");
                                            }
                                            if (evaObj.has("address") && !evaObj.isNull("address")) {
                                                nowEva.shopAddress = evaObj.getString("address");
                                            }
                                            if (evaObj.has("lng") && !evaObj.isNull("lng")) {
                                                nowEva.shoplng = evaObj.getDouble("lng");
                                            }
                                            if (evaObj.has("lat") && !evaObj.isNull("lat")) {
                                                nowEva.shoplat = evaObj.getDouble("lat");
                                            }
                                            if (evaObj.has("phone") && !evaObj.isNull("phone")) {
                                                nowEva.shopPhone = evaObj.getString("phone");
                                            }
                                            if (evaObj.has("shopName") && !evaObj.isNull("shopName")) {
                                                nowEva.shopName = evaObj.getString("shopName");
                                            }
                                            if (evaObj.has("id") && !evaObj.isNull("id")) {
                                                nowEva.shopId = evaObj.getInt("id");
                                            }
                                            if (evaObj.has("openTime") && !evaObj.isNull("openTime")) {
                                                nowEva.openTime = evaObj.getString("openTime");
                                            }
                                            if (evaObj.has("dist") && !evaObj.isNull("dist")) {
                                                nowEva.dist = evaObj.getString("dist");
                                            }
                                            if (evaObj.has("goodRate") && !evaObj.isNull("goodRate")) {
                                                nowEva.goodRate = evaObj.getString("goodRate");
                                            }
                                            nows.add(nowEva);
                                        }
                                    }
                                }
                            }
                            if (nows.size() > 0) {
                                nows.get(0).isChoose = true;
                            }
                            if (workerId > 0) {
                                now = nows.get(0);
                                LastShop.shopId = now.shopId;
                                LastShop.shopAddress = now.shopAddress;
                                LastShop.shoplat = now.shoplat;
                                LastShop.shoplng = now.shoplng;
                                LastShop.shopName = now.shopName;
                                LastShop.shopImg = now.shopImg;
                                LastShop.shopWxImg = now.shopWxImg;
                                LastShop.shopWxNum = now.shopWxNum;
                                LastShop.dist = now.dist;
                                LastShop.shopActiveBackup = now.shopActiveBackup;
                                LastShop.shopActivePoint = now.shopActivePoint;
                                LastShop.shopActiveTitle = now.shopActiveTitle;
                                LastShop.shopPhone = now.shopPhone;
                                LastShop.openTime = now.openTime;
                                LastShop.tag = now.tag;
                                shop_address_detail.setText(LastShop.shopName);
                                setGravity();
                            }
                            Utils.mLogError("==-->111111  shopid " + LastShop.shopId + " shopName " + LastShop.shopName + " 33333 " + now.shopId + " 33333 " + shopId);
                            listServiceType.clear();
                            if (obJsonObject.has("services") && !obJsonObject.isNull("services")) {
                                JSONArray array = obJsonObject.getJSONArray("services");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jServices = array.getJSONObject(i);
                                        if (jServices.has("serviceLoc") && !jServices.isNull("serviceLoc")) {
                                            if (jServices.getInt("serviceLoc") == 1) {
                                                listServiceType.add(ServiceType.json2Entity(array.getJSONObject(i)));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Utils.mLogError("==-->111111  shopid " + LastShop.shopId + " shopName " + LastShop.shopName + " 44444 " + now.shopId + " 44444 " + shopId);
                        if (isCancle) {
                            isCancle = false;
                        } else {
                            if (nows.size() > 0) {
                                if (nows.size() == 1) {
                                    if (LastShop.shopId != nows.get(0).shopId) {
                                        Intent intent = new Intent(mContext, SwitchShopActivity.class);
                                        intent.putExtra("title", title);
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(mContext, SwitchShopActivity.class);
                                    intent.putExtra("title", title);
                                    startActivity(intent);
                                }
                            }
                        }
                    } else {
                        ToastUtil.showToastShort(mContext, object.getString("msg"));
                    }
                    mPDialog.closeDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.mLogError(e.getMessage() + " " + e.getCause());
                }
                if (listServiceType.size() > 0) {
                    ServiceType serviceType = listServiceType.get(0);
                    String price = serviceType.price;
                    String vip_price = serviceType.vip_price;
                    Utils.setText(tv_servicenew_fxprice, serviceType.grain_gold_price, "", View.VISIBLE, View.GONE);
                    if (Utils.isStrNull(price)) {
                        tv_servicenew_price.setVisibility(View.VISIBLE);
                        tv_servicenew_price.setText(price.split("@@")[1]);
                    } else {
                        tv_servicenew_price.setVisibility(View.GONE);
                    }
                    if (Utils.isStrNull(vip_price)) {
                        iv_servicenew_cardprice.setVisibility(View.VISIBLE);
                        tv_servicenew_cardprice.setVisibility(View.VISIBLE);
                        tv_servicenew_cardprice.setText(vip_price.replace("@@", ""));
                    } else {
                        iv_servicenew_cardprice.setVisibility(View.GONE);
                        tv_servicenew_cardprice.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                mPDialog.closeDialog();
            }
        };

        //确认切换门店 之后刷新 服务方式不弹窗
        public void setChangeDetail ( int position){
            ServiceShopAdd chooseNow = nows.get(position);
            LastShop.shopId = chooseNow.shopId;
            LastShop.shopAddress = chooseNow.shopAddress;
            LastShop.shoplat = chooseNow.shoplat;
            LastShop.shoplng = chooseNow.shoplng;
            LastShop.shopName = chooseNow.shopName;
            LastShop.shopImg = chooseNow.shopImg;
            LastShop.shopWxImg = chooseNow.shopWxImg;
            LastShop.shopWxNum = chooseNow.shopWxNum;
            LastShop.dist = chooseNow.dist;
            LastShop.shopActiveBackup = chooseNow.shopActiveBackup;
            LastShop.shopActivePoint = chooseNow.shopActivePoint;
            LastShop.shopActiveTitle = chooseNow.shopActiveTitle;
            LastShop.shopPhone = chooseNow.shopPhone;
            LastShop.openTime = chooseNow.openTime;
            LastShop.tag = chooseNow.tag;
            shopId = LastShop.shopId;
            Utils.mLogError("==-->111111  shopid " + LastShop.shopId + " 66666 " + now.shopId + " 66666 " + shopId);
            if (LastShop != null) {
                shop_address_detail.setText(LastShop.shopName);
                setGravity();
            }
            isCancle = true;
            getServiceType2();
        }

        public void setCancleChangeShop () {
            if (LastShop != null) {
                if (!TextUtils.isEmpty(LastShop.shopName)) {
                    shop_address_detail.setText(LastShop.shopName);
                    setGravity();
                }
            }
            isCancle = true;
            getServiceType2();
        }

        private void goNext (Class cls,int requestcode){
            Intent intent = new Intent(mContext, cls);
            intent.putExtra("listPets", (Serializable) listMyPets);
            intent.putExtra("petKindstr", "1,2");
            intent.putExtra("previous", requestcode);
            intent.putExtra("maxPet", 5);
            intent.putExtra("addressId", addressId);
            intent.putExtra("serviceType", serviceType);
            intent.putExtra("serviceId", serviceId);
            startActivityForResult(intent, requestcode);
        }

        private String getPetId_SERVICEID_MYPETID_ITEMID (List < Pet > listMyPets) {
            StringBuilder sp = new StringBuilder();
            if (serviceType == 3) {
                if (serviceId > 0) {
                    for (int i = 0; i < listMyPets.size(); i++) {
                        Pet myPet = listMyPets.get(i);
                        if (myPet.serviceid != serviceId) {
                            sp.append(myPet.id);
                            sp.append("_");
                            sp.append(serviceId);
                            sp.append("_");
                            sp.append(myPet.customerpetid);
                            sp.append("_");
                            sp.append("0");
                            sp.append("_");
                        } else {
                            sp.append(myPet.id);
                            sp.append("_");
                            sp.append(myPet.serviceid);
                            sp.append("_");
                            sp.append(myPet.customerpetid);
                            sp.append("_");
                            sp.append("0");
                            sp.append("_");
                        }
                    }
                } else {
                    for (int i = 0; i < listMyPets.size(); i++) {
                        Pet myPet = listMyPets.get(i);
                        if (myPet.serviceid == 0) {
                            sp.append(myPet.id);
                            sp.append("_");
                            sp.append(serviceType);
                            sp.append("_");
                            sp.append(myPet.customerpetid);
                            sp.append("_");
                            sp.append("0");
                            sp.append("_");
                        } else {
                            sp.append(myPet.id);
                            sp.append("_");
                            sp.append(myPet.serviceid);
                            sp.append("_");
                            sp.append(myPet.customerpetid);
                            sp.append("_");
                            sp.append("0");
                            sp.append("_");
                        }
                    }
                }
            } else {
                for (int i = 0; i < listMyPets.size(); i++) {
                    Pet myPet = listMyPets.get(i);
                    if (myPet.serviceid == 0) {
                        sp.append(myPet.id);
                        sp.append("_");
                        sp.append(serviceType);
                        sp.append("_");
                        sp.append(myPet.customerpetid);
                        sp.append("_");
                        sp.append("0");
                        sp.append("_");
                    } else {
                        sp.append(myPet.id);
                        sp.append("_");
                        sp.append(myPet.serviceid);
                        sp.append("_");
                        sp.append(myPet.customerpetid);
                        sp.append("_");
                        sp.append("0");
                        sp.append("_");
                    }
                }
            }
            if (!TextUtils.isEmpty(sp)) {
                strp = sp.substring(0, sp.length() - 1);
            } else {
                strp = null;
            }
            return strp;
        }

        /**
         * 网络操作相关的子线程
         */
        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                // 在这里进行 http request.网络请求相关操作
                Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(shareImg);
                Message msg = new Message();
                msg.obj = returnBitmap;
                handlerShare.sendMessage(msg);
            }
        };
        Handler handlerShare = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                bmp = (Bitmap) msg.obj;
                showShare();
            }
        };

        private String buildTransaction ( final String type){
            return (type == null) ? String.valueOf(System.currentTimeMillis())
                    : type + System.currentTimeMillis();
        }

        private void setShareContent ( int shareType){
            if (shareUrl.contains("?")) {
                shareUrl = shareUrl
                        + "&system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(mContext)
                        + "&imei="
                        + Global.getIMEI(mContext)
                        + "&cellPhone=" + spUtil.getString("cellphone", "")
                        + "&phoneModel="
                        + Build.BRAND + " " + Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis()
                        + "&serviceType=" + serviceType
                        + "&serviceId=" + serviceId
                ;
            } else {
                shareUrl = shareUrl
                        + "?system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(mContext)
                        + "&imei="
                        + Global.getIMEI(mContext)
                        + "&cellPhone=" + spUtil.getString("cellphone", "")
                        + "&phoneModel="
                        + Build.BRAND + " " + Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis()
                        + "&serviceType=" + serviceType
                        + "&serviceId=" + serviceId
                ;
            }
            Log.e("TAG", "shareUrl = " + shareUrl);
            if (bmp != null && shareUrl != null && !TextUtils.isEmpty(shareUrl)) {
                Bitmap createBitmapThumbnail = Utils.createBitmapThumbnail(bmp);
                if (shareType == 1) {
                    //微信小程序
//                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    Utils.shareMiniProgram(this,shareUrl,createBitmapThumbnail,
                            "/pages/appointment/appointment?"+"serviceType="+serviceType+"&shopId="+shopId+(serviceId == 0? "":"&serviceId="+serviceId)
                            ,shareTitle,shareDesc);
                } else {
                    WXWebpageObject wxwebpage = new WXWebpageObject();
                    wxwebpage.webpageUrl = shareUrl;
                    WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
                    wxmedia.title = shareTitle;
                    wxmedia.description = shareDesc;

                    wxmedia.setThumbImage(createBitmapThumbnail);
                    wxmedia.thumbData = Util_WX.bmpToByteArray(
                            createBitmapThumbnail, true);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = wxmedia;
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    api.sendReq(req);
                }

            }
        }

        private void showShare () {
            final View view = mInflater.inflate(R.layout.sharedialog, null);
            RelativeLayout rlParent = (RelativeLayout) view
                    .findViewById(R.id.rl_sharedialog_parent);
            LinearLayout ll_sharedialog_wxfriend = (LinearLayout) view
                    .findViewById(R.id.ll_sharedialog_wxfriend);
            LinearLayout ll_sharedialog_wxpyq = (LinearLayout) view
                    .findViewById(R.id.ll_sharedialog_wxpyq);
            LinearLayout ll_sharedialog_qqfriend = (LinearLayout) view
                    .findViewById(R.id.ll_sharedialog_qqfriend);
            LinearLayout ll_sharedialog_sina = (LinearLayout) view
                    .findViewById(R.id.ll_sharedialog_sina);
            ll_sharedialog_sina.setVisibility(View.GONE);
            ll_sharedialog_qqfriend.setVisibility(View.GONE);
            Button btn_sharedialog_cancel = (Button) view
                    .findViewById(R.id.btn_sharedialog_cancel);
            if (pWin == null) {
                pWin = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, true);
            }
            pWin.setFocusable(true);
            pWin.setWidth(ScreenUtil.getScreenWidth(mContext));
            pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            rlParent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    pWin.dismiss();
                    pWin = null;
                }
            });
            ll_sharedialog_wxfriend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {// 微信好友
                    pWin.dismiss();
                    pWin = null;
                    if (Utils.isWeixinAvilible(mContext)) {
                        int shareType = 1;
                        setShareContent(shareType);
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "请先下载微信客户端");
                    }
                }
            });
            ll_sharedialog_wxpyq.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {// 微信朋友圈
                    pWin.dismiss();
                    pWin = null;
                    if (Utils.isWeixinAvilible(mContext)) {
                        int shareType = 2;
                        setShareContent(shareType);
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "请先下载微信客户端");
                    }
                }
            });
            ll_sharedialog_qqfriend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {// QQ好友
                    pWin.dismiss();
                    pWin = null;
                    if (Utils.isQQClientAvailable(mContext)) {
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "请先下载QQ客户端");
                    }
                }
            });
            ll_sharedialog_sina.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {// 新浪微博
                    pWin.dismiss();
                    pWin = null;
                }
            });
            btn_sharedialog_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {// 取消
                    pWin.dismiss();
                    pWin = null;
                }
            });
        }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Global.RESULT_OK) {
                if (requestCode == Global.SERVICE_NEW_TO_ADD_PET) {
                    isCancle = true;
                    listMyPets.clear();
                    List<Pet> MyPets = (List<Pet>) data.getSerializableExtra("myPetLIst");
                    if (MyPets.size() > 0) {
                        listMyPets.addAll(MyPets);
                        if (listMyPets.size() > 0) {
                            textview_add_pet.setVisibility(View.GONE);
                            myPetRecycleAdapter.notifyDataSetChanged();
                        } else {
                            textview_add_pet.setVisibility(View.VISIBLE);
                        }
                        strp = getPetId_SERVICEID_MYPETID_ITEMID(listMyPets);
                        Utils.mLogError("==-->选择宠物返回重新调用queryServiceDetail拼接strp 22 " + strp);
                        queryServiceDetail();
                    }
                } else if (requestCode == Global.SERVICE_NEW_TO_LOGIN) {
                    getPetData();
                    queryServiceDetail();
                    queryCommentsByService();
                } else if (requestCode == Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST) {
                    isCancle = true;
                    if (data == null) {
                        return;
                    } else if (TextUtils.isEmpty(data.getStringExtra("shopname"))) {
                        return;
                    }
                    if (data.getIntExtra("shopid", 0) != now.shopId) {
                        shopId = data.getIntExtra("shopid", 0);
                        cityId = data.getIntExtra("cityId", 0);
                        shopName = data.getStringExtra("shopname");
                        shopAddress = data.getStringExtra("shopaddr");
                        shopLat = data.getDoubleExtra("lat", 0);
                        shopLng = data.getDoubleExtra("lng", 0);
                        shopImg = data.getStringExtra("shopimg");
                        shopActiveTitle = data.getStringExtra("shopActiveTitle");
                        shopActivePoint = data.getIntExtra("shopActivePoint", 0);
                        shopActiveBackup = data.getStringExtra("shopActiveBackup");
                        shopWxNum = data.getStringExtra("shopWxNum");
                        dist = data.getStringExtra("dist");
                        shopWxImg = data.getStringExtra("shopWxImg");
                        shopPhone = data.getStringExtra("shopPhone");
                        LastShop.openTime = data.getStringExtra("openTime");
                        LastShop.tag = data.getStringExtra("tag");
                        LastShop.shopId = shopId;
                        LastShop.shopName = shopName;
                        LastShop.shoplat = shopLat;
                        LastShop.shoplng = shopLng;
                        LastShop.shopAddress = shopAddress;
                        LastShop.shopImg = shopImg;
                        LastShop.shopActiveTitle = shopActiveTitle;
                        LastShop.shopActivePoint = shopActivePoint;
                        LastShop.shopActiveBackup = shopActiveBackup;
                        LastShop.shopWxNum = shopWxNum;
                        LastShop.dist = dist;
                        LastShop.shopWxImg = shopWxImg;
                        LastShop.shopPhone = shopPhone;
                        shop_address_detail.setText(LastShop.shopName);
                        setGravity();
                        getServiceType2();
                    } else {
                        cityId = data.getIntExtra("cityId", 0);
                        shopId = data.getIntExtra("shopid", 0);
                        shopName = data.getStringExtra("shopname");
                        shopAddress = data.getStringExtra("shopaddr");
                        shopLat = data.getDoubleExtra("lat", 0);
                        shopLng = data.getDoubleExtra("lng", 0);
                        shopImg = data.getStringExtra("shopimg");
                        shopActiveTitle = data.getStringExtra("shopActiveTitle");
                        shopActivePoint = data.getIntExtra("shopActivePoint", 0);
                        shopActiveBackup = data.getStringExtra("shopActiveBackup");
                        shopWxNum = data.getStringExtra("shopWxNum");
                        dist = data.getStringExtra("dist");
                        shopWxImg = data.getStringExtra("shopWxImg");
                        shopPhone = data.getStringExtra("shopPhone");
                        LastShop.openTime = data.getStringExtra("openTime");
                        LastShop.tag = data.getStringExtra("tag");
                        LastShop.shopId = shopId;
                        LastShop.shopName = shopName;
                        LastShop.shoplat = shopLat;
                        LastShop.shoplng = shopLng;
                        LastShop.shopAddress = shopAddress;
                        LastShop.shopImg = shopImg;
                        LastShop.shopActiveTitle = shopActiveTitle;
                        LastShop.shopActivePoint = shopActivePoint;
                        LastShop.shopActiveBackup = shopActiveBackup;
                        LastShop.shopWxNum = shopWxNum;
                        LastShop.dist = dist;
                        LastShop.shopWxImg = shopWxImg;
                        LastShop.shopPhone = shopPhone;
                        shop_address_detail.setText(LastShop.shopName);
                        setGravity();
                        getServiceType2();
                    }
                } else if (requestCode == Global.SERVICE_NEW_TO_CHOOSEADDRESS) {
                    address = data.getStringExtra("addr");
                    lat = data.getDoubleExtra("addr_lat", 0);
                    lng = data.getDoubleExtra("addr_lng", 0);
                    addressId = data.getIntExtra("addr_id", 0);
                    address_detail.setText(address);
                    setGravity();
                    shopId = LastShop.shopId;
                    CommAddr commAddrChoose = (CommAddr) data.getSerializableExtra("commAddr");
                    commAddr.address = commAddrChoose.address;
                    commAddr.supplement = commAddrChoose.supplement;
                    commAddr.lat = commAddrChoose.lat;
                    commAddr.lng = commAddrChoose.lng;
                    commAddr.Customer_AddressId = commAddrChoose.Customer_AddressId;
                    commAddr.telephone = commAddrChoose.telephone;
                    commAddr.linkman = commAddrChoose.linkman;
                    Utils.mLogError("==-->111111  shopid " + LastShop.shopId + " 111111111 " + now.shopId + " 1111 " + shopId);
                    getServiceType2();
                }
            }
        }

        public void setIsOpen ( int position, Comment comment){
            comment.isOpen = !comment.isOpen;
            evalists.set(position, comment);
            shopEvaAdapter.notifyDataSetChanged();
        }

        private void setBanner () {
            list.clear();
            for (int i = 0; i < listBannerStr.size(); i++) {
                list.add(listBannerStr.get(i));
            }
            bannerItemDetail.setImages(list)
                    .setImageLoader(new GlideImageLoaderFourRound())
                    .setOnBannerListener(this)
                    .start();
        }

        @Override
        public void OnBannerClick ( int position){
            if (listBannerStr != null && listBannerStr.size() > 0 && listBannerStr.size() > position) {
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
        public boolean dispatchKeyEvent (KeyEvent event){
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                Global.ServerEvent(ServiceNewActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_service_detail_back);
                finishWithAnimation();
                return true;
            }
            return super.dispatchKeyEvent(event);
        }

        @OnClick({
                R.id.service_back,
                R.id.ll_servicenew_submit,
                R.id.service_back_blow,
                R.id.service_share,
                R.id.service_share_below,
                R.id.textview_add_pet,
                R.id.textview_left_pet_title,
                R.id.img_choose_mypet,
                R.id.layout_choose_pet_top,
                R.id.relayoutChoosePet,
                R.id.recyclerView_mypet,
                R.id.textview_left_address_title,
                R.id.img_choose_address,
                R.id.address_detail,
                R.id.textview_left_shop_title,
                R.id.img_choose_shop,
                R.id.layout_choose_pet,
                R.id.shop_address_detail})
        public void onViewClicked (View view){
            switch (view.getId()) {
                case R.id.ll_servicenew_submit:
                    if (!Utils.checkLogin(mContext)) {
                        goNext(LoginNewActivity.class, Global.SERVICE_NEW_TO_LOGIN, Global.SERVICE_NEW_TO_LOGIN);
                        return;
                    }
                    if (listMyPets.size() <= 0) {
                        ToastUtil.showToastShortCenter(mContext, "请您先选择宠物再下单哦~");
                        return;
                    }
                    if (listServiceType.size() > 0) {
                        serviceTypeClick = listServiceType.get(0);
                        if (!TextUtils.isEmpty(serviceTypeClick.disabled_tip)) {
                            ToastUtil.showToastShortCenter(mContext, serviceTypeClick.disabled_tip);
                            return;
                        } else {
                            serviceLoc = serviceTypeClick.serviceLoc;
                            if (serviceLoc == 1) {//1 到店
                                if (addressId <= 0) {
                                    showAddressDialog();
                                    return;
                                } else if (shopId <= 0) {
                                    if (shopIdNet <= 0) {
                                        showShopDialog();
                                        return;
                                    }
                                }
                            } else if (serviceLoc == 2) {//2上门
                                if (addressId <= 0) {
                                    showAddressDialog();
                                    return;
                                } else if (shopId <= 0) {
                                    if (shopIdNet <= 0) {
                                        showShopDialog();
                                        return;
                                    }
                                }
                            }
                            if (serviceLoc == 1) {
                                Global.ServerEvent(ServiceNewActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_goshop);
                            } else if (serviceLoc == 2) {
                                Global.ServerEvent(ServiceNewActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_gohome);
                            }
                            goAppoint(AppointMentActivity.class);
                        }
                    }
                    break;
                case R.id.textview_left_pet_title:
                    break;
                case R.id.textview_left_address_title:
                    break;
                case R.id.img_choose_address:
                case R.id.address_detail:
                    if (Utils.checkLogin(mContext)) {
                        goNext(CommonAddressActivity.class, Global.SERVICE_NEW_TO_CHOOSEADDRESS, Global.BOOKINGSERVICEREQUESTCODE_ADDR);
                    } else {
                        goNext(LoginNewActivity.class, Global.SERVICE_NEW_TO_LOGIN, Global.SERVICE_NEW_TO_LOGIN);
                    }
                    break;
                case R.id.textview_left_shop_title:
                    break;
                case R.id.img_choose_shop:
                case R.id.shop_address_detail:
                    if (workerId <= 0) {
                        if (Utils.checkLogin(mContext)) {
                            if (addressId <= 0) {
                                ToastUtil.showToastShortCenter(mContext, "请先选择地址");
                            } else {
                                goNext(AllShopLocActivity.class, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST);
                            }
                        } else {
                            goNext(AllShopLocActivity.class, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST, Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST);
                        }
                    }
                    break;
                case R.id.recyclerView_mypet:
                case R.id.relayoutChoosePet:
                case R.id.layout_choose_pet:
                case R.id.img_choose_mypet:
                case R.id.layout_choose_pet_top:
                case R.id.textview_add_pet:
                    if (Utils.checkLogin(mContext)) {
                        goNext(ChooseMyPetActivity.class, Global.SERVICE_NEW_TO_ADD_PET);
                    } else {
                        goNext(LoginNewActivity.class, Global.SERVICE_NEW_TO_LOGIN, Global.SERVICE_NEW_TO_LOGIN);
                    }
                    break;
                case R.id.service_back_blow:
                case R.id.service_back:
                    Global.ServerEvent(ServiceNewActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_service_detail_back);
                    finishWithAnimation();
                    break;
                case R.id.service_share_below:
                case R.id.service_share:
                    new Thread(networkTask).start();
                    break;
            }
        }
    }
