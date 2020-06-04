package com.haotang.pet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ShopDetailEvaAdapter;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.entity.MemberEntity;
import com.haotang.pet.entity.ShopEntity;
import com.haotang.pet.entity.ShopLocationEvent;
import com.haotang.pet.entity.ShopServices;
import com.haotang.pet.entity.ShopToServiceEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.GlideImageLoaderFourRound;
import com.haotang.pet.view.MyScrollView;
import com.haotang.pet.view.ShadowLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDetailActivity extends SuperActivity implements OnBannerListener {

    @BindView(R.id.iv_shopdetail_back)
    ImageView ivShopdetailBack;
    @BindView(R.id.tv_shopdetail_name)
    TextView tvShopdetailName;
    @BindView(R.id.tv_shopdetail_open)
    TextView tvShopdetailOpen;
    @BindView(R.id.rl_shopdetail_top)
    RelativeLayout rlShopdetailTop;
    @BindView(R.id.iv_shopdetail_nav)
    ImageView ivShopdetailNav;
    @BindView(R.id.iv_shopdetail_call)
    ImageView ivShopdetailCall;
    @BindView(R.id.tv_shopdetail_copywx)
    TextView tvShopdetailCopywx;
    @BindView(R.id.tv_shopdetail_qrcode)
    TextView tvShopdetailQrcode;
    @BindView(R.id.tv_shopdetail_nav)
    TextView tvShopdetailNav;
    @BindView(R.id.tv_shopdetail_wechat)
    TextView tvShopdetailWechat;
    @BindView(R.id.banner_shopdetail)
    Banner bannerShopdetail;
    @BindView(R.id.sl_shopdetail_eva)
    SmartRefreshLayout slShopdetailEva;
    @BindView(R.id.rv_shopdetail_eva)
    RecyclerView rvShopdetailEva;
    @BindView(R.id.iv_shopdetail_backgone)
    ImageView ivShopdetailBackgone;
    @BindView(R.id.tv_shopdetail_namegone)
    TextView tvShopdetailNamegone;
    @BindView(R.id.rl_shopdetail_topgone)
    RelativeLayout rlShopdetailTopgone;
    @BindView(R.id.sv_shopdetail)
    MyScrollView svShopdetail;
    @BindView(R.id.rl_shopdetail_order)
    RelativeLayout rlShopdetailOrder;
    @BindView(R.id.btn_shopdetail_order)
    Button btnShopdetailOrder;
    @BindView(R.id.tv_shop_evaluate)
    TextView tvShopEvaluate;
    @BindView(R.id.ll_banner_indicator)
    LinearLayout llBannerIndicator;
    @BindView(R.id.shadowLayout)
    ShadowLayout shadowLayout;
    private ShopEntity shopEntity = null;
    private Intent fIntent;
    private boolean showButton;
    private int shopid;
    private double lat;
    private double lng;
    private int serviceid;
    private int previous;
    private int clicksort;
    private int areaid;
    private String servicename;
    private int servicetype;
    private int serviceloc;
    private int petid;
    private int petkind;
    private String petname;
    private int customerpetid;
    private String customerpetname;
    private String shareImg;
    private String shareTitle;
    private String shareUrl;
    private String shareDesc;
    private String tag;
    private String dist;
    private int cityId;
    private String city;
    private String shopWxImg;
    private String shopWxNum;
    private List<String> list = new ArrayList<String>();
    private List<Comment> evalists = new ArrayList<>();
    private int page = 1;
    private int size = 10;
    private ShopDetailEvaAdapter shopEvaAdapter;
    private String worderAvatar;
    private String workerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        initWindows();
        getData();
        GetShopComment();
        setListener();
    }


    private void initData() {
        shopEntity = new ShopEntity();
        fIntent = getIntent();
        showButton = fIntent.getBooleanExtra("showButton", true);
        shopid = fIntent.getIntExtra("shopid", 0);
        lat = fIntent.getDoubleExtra("addr_lat", 0);
        lng = fIntent.getDoubleExtra("addr_lng", 0);
        cityId = fIntent.getIntExtra("cityId", 0);
        tag = fIntent.getStringExtra("tag");
        dist = fIntent.getStringExtra("dist");
        serviceid = fIntent.getIntExtra("serviceid", 0);
        previous = fIntent.getIntExtra("previous", 0);
        clicksort = fIntent.getIntExtra("clicksort", 0);
        areaid = fIntent.getIntExtra("areaid", 0);
        servicename = fIntent.getStringExtra("servicename");
        servicetype = fIntent.getIntExtra("servicetype", 0);
        serviceloc = fIntent.getIntExtra("serviceloc", 0);
        petid = fIntent.getIntExtra("petid", 0);
        petkind = fIntent.getIntExtra("petkind", 0);
        petname = fIntent.getStringExtra("petname");
        customerpetid = fIntent.getIntExtra("customerpetid", 0);
        customerpetname = fIntent.getStringExtra("customerpetname");
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

    private void setView() {
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        rvShopdetailEva.setNestedScrollingEnabled(false);
        shopEvaAdapter = new ShopDetailEvaAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvShopdetailEva.setLayoutManager(layoutManager);
        rvShopdetailEva.setAdapter(shopEvaAdapter);
        slShopdetailEva.setEnableLoadMore(true);
        slShopdetailEva.setEnableRefresh(false);
        slShopdetailEva.setNestedScrollingEnabled(true);
        bannerShopdetail.setBannerStyle(BannerConfig.NOT_INDICATOR);
        if (Utils.checkLogin(mContext)) {
            //判断是从哪里进来的
            if (showButton)
                rlShopdetailOrder.setVisibility(View.VISIBLE);
        } else {
            rlShopdetailOrder.setVisibility(View.GONE);
        }
    }

    private void createPoint(int postion) {
        llBannerIndicator.removeAllViews();
        for (int i = 0; i < shopEntity.imgList.size(); i++) {
            ImageView imageView = new ImageView(mContext);
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
        slShopdetailEva.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                GetShopComment();
            }
        });
        svShopdetail.setListener(new MyScrollView.Listener() {
            @Override
            public void onScroll(int t) {
                if (t > 419) {
                    rlShopdetailTopgone.setVisibility(View.VISIBLE);
                } else {
                    rlShopdetailTopgone.setVisibility(View.GONE);
                }
            }
        });
        bannerShopdetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private void GetShopComment() {
        mPDialog.showDialog();
        CommUtil.GetShopComment(mContext, shopid, page, size, evaHandler);
    }

    private AsyncHttpResponseHandler evaHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                slShopdetailEva.finishLoadMore();
                JSONObject obj = new JSONObject(new String(responseBody));
                int code = obj.getInt("code");
                if (code == 0) {
                    if (obj.has("data") && !obj.isNull("data")) {
                        JSONObject objData = obj.getJSONObject("data");
                        if (objData.has("totalPage") && !objData.isNull("totalPage")) {
                            String totalPage = objData.getString("totalPage");
                        }
                        if (objData.has("commentWorkerConfig") && !objData.isNull("commentWorkerConfig")) {
                            JSONObject workerObject = objData.getJSONObject("commentWorkerConfig");
                            if (workerObject.has("avatar") && !workerObject.isNull("avatar")) {
                                worderAvatar = workerObject.getString("avatar");
                            }
                            if (workerObject.has("nickname") && !workerObject.isNull("nickname")) {
                                workerName = workerObject.getString("nickname");
                            }
                        }
                        if (objData.has("dataset") && !objData.isNull("dataset")) {
                            JSONArray array = objData.getJSONArray("dataset");
                            if (array.length() > 0) {
                                page++;
                                for (int i = 0; i < array.length(); i++) {
                                    evalists.add(Comment.json2Entity(array.getJSONObject(i)));
                                }
                                for (int i = 0; i < evalists.size(); i++) {
                                    if (evalists.get(i).getCommentWorkerContent() != null) {
                                        evalists.get(i).setAvatarBeauty(worderAvatar);
                                        evalists.get(i).setNickname(workerName);
                                    }
                                }
                            }
                        }
                        //评价数量
                        if (evalists.size() > 0) {
                            shopEvaAdapter.setEvalists(evalists);
                            tvShopEvaluate.setVisibility(View.VISIBLE);
                            slShopdetailEva.setEnableLoadMore(true);
                        } else {
                            tvShopEvaluate.setVisibility(View.GONE);
                            slShopdetailEva.setEnableLoadMore(false);
                        }
                    } else {
                        tvShopEvaluate.setVisibility(View.GONE);
                        slShopdetailEva.setEnableLoadMore(false);
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

    private void getData() {
        mPDialog.showDialog();
        CommUtil.GetShopInfo(mContext, shopid, lat, lng, shopHandler);
    }

    private AsyncHttpResponseHandler shopHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject jsonObj = new JSONObject(new String(responseBody));
                int code = jsonObj.getInt("code");
                if (code == 0) {
                    if (jsonObj.has("data") && !jsonObj.isNull("data")) {
                        JSONObject object = jsonObj.getJSONObject("data");
                        if (object.has("share") && object.has("share")) {
                            JSONObject objShare = object.getJSONObject("share");
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
                        shopEntity.list.clear();
                        if (object.has("serviceItems") && !object.isNull("serviceItems")) {
                            JSONArray array = object.getJSONArray("serviceItems");
                            for (int i = 0; i < array.length(); i++) {
                                ShopServices shopServices = new ShopServices();
                                JSONObject objectServices = array.getJSONObject(i);
                                if (objectServices.has("point") && !objectServices.isNull("point")) {
                                    shopServices.point = objectServices.getInt("point");
                                }
                                if (objectServices.has("itemName") && !objectServices.isNull("itemName")) {
                                    shopServices.name = objectServices.getString("itemName");
                                }
                                if (objectServices.has("img") && !objectServices.isNull("img")) {
                                    shopServices.img = objectServices.getString("img");
                                }
                                shopEntity.list.add(shopServices);
                            }
                        }
                        shopEntity.memberEntities.clear();
                        if (object.has("members") && !object.isNull("members")) {
                            JSONArray array = object.getJSONArray("members");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    MemberEntity memberentity = new MemberEntity();
                                    JSONObject objectMember = array.getJSONObject(i);
                                    if (objectMember.has("realName") && !objectMember.isNull("realName")) {
                                        memberentity.realName = objectMember.getString("realName");
                                    }
                                    if (objectMember.has("id") && !objectMember.isNull("id")) {
                                        memberentity.id = objectMember.getInt("id");
                                    }
                                    if (objectMember.has("avatar") && !objectMember.isNull("avatar")) {
                                        memberentity.avatar = objectMember.getString("avatar");
                                    }
                                    if (objectMember.has("role") && !objectMember.isNull("role")) {
                                        memberentity.role = objectMember.getInt("role");
                                    }
                                    if (objectMember.has("duty") && !objectMember.isNull("duty")) {
                                        memberentity.duty = objectMember.getString("duty");
                                    }
                                    shopEntity.memberEntities.add(memberentity);
                                }
                            }
                        }
                        shopEntity.imgList.clear();
                        if (object.has("bannerImg") && !object.isNull("bannerImg")) {
                            JSONArray array = object.getJSONArray("bannerImg");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject objEva = array.getJSONObject(i);
                                    if (objEva.has("img") && !objEva.isNull("img")) {
                                        shopEntity.imgList.add(objEva.getString("img"));
                                    }
                                }
                            }
                        }
                        if (shopEntity.imgList.size() > 0) {
//                            adapterBanner.notifyDataSetChanged();
                            setBanner();
                        }
                        if (object.has("areaImg") && !object.isNull("areaImg")) {
                            shopEntity.areaImg = object.getString("areaImg");
//                            GlideUtil.loadImg(GoShopDetailActivity.this, shopEntity.areaImg, imageview_go_shop_area, 0);
                        }
                        if (object.has("shopName") && !object.isNull("shopName")) {
                            shopEntity.shopName = object.getString("shopName");
                            tvShopdetailName.setText(shopEntity.shopName);
                            tvShopdetailNamegone.setText(shopEntity.shopName);
                        }
                        if (object.has("shopWxImg") && !object.isNull("shopWxImg")) {
                            shopWxImg = object.getString("shopWxImg");
                        }
                        if (object.has("shopWxNum") && !object.isNull("shopWxNum")) {
                            shopWxNum = object.getString("shopWxNum");
                            tvShopdetailWechat.setText(shopWxNum);
                        }
                        if (object.has("img") && !object.isNull("img")) {
                            shopEntity.shopimg = object.getString("img");
                        }
                        if (object.has("openTime") && !object.isNull("openTime")) {
                            shopEntity.operTime = object.getString("openTime");
                            tvShopdetailOpen.setText("营业时间：" + shopEntity.operTime);
                        }
                        if (object.has("city") && !object.isNull("city")) {
                            city = object.getString("city");
                        }
                        if (object.has("address") && !object.isNull("address")) {
                            shopEntity.address = object.getString("address");
                            tvShopdetailNav.setText(shopEntity.address);
                        }
                        if (object.has("lat") && !object.isNull("lat")) {
                            shopEntity.lat = object.getDouble("lat");
                        }
                        if (object.has("lng") && !object.isNull("lng")) {
                            shopEntity.lng = object.getDouble("lng");
                        }
                        if (object.has("phone") && !object.isNull("phone")) {
                            shopEntity.phone = object.getString("phone");
                        }

                    }
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

    private void setBanner() {
        list.clear();
        for (int i = 0; i < shopEntity.imgList.size(); i++) {
            list.add(shopEntity.imgList.get(i));
        }
        GlideImageLoaderFourRound fourRound = new GlideImageLoaderFourRound();
        fourRound.setRound(15);
        bannerShopdetail.setImages(list)
                .setImageLoader(fourRound)
                .setOnBannerListener(this)
                .start();
        if (list.size() < 2) {
            llBannerIndicator.setVisibility(View.GONE);
        } else {
            llBannerIndicator.setVisibility(View.VISIBLE);
        }
        shadowLayout.setVisibility(View.VISIBLE);
        createPoint(0);
    }

    @OnClick({R.id.iv_shopdetail_back, R.id.iv_shopdetail_nav, R.id.iv_shopdetail_call, R.id.tv_shopdetail_copywx, R.id.tv_shopdetail_qrcode, R.id.iv_shopdetail_backgone, R.id.btn_shopdetail_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_shopdetail_back:
                finish();
                break;
            case R.id.iv_shopdetail_nav:
                Intent navIntent = new Intent(mContext, FosterNavigationActivity.class);
                navIntent.putExtra("lat", shopEntity.lat);
                navIntent.putExtra("lng", shopEntity.lng);
                navIntent.putExtra("address", shopEntity.address);
                navIntent.putExtra("name", shopEntity.shopName);
                startActivity(navIntent);
                break;
            case R.id.iv_shopdetail_call:
                try {
                    if (shopEntity != null) {
                        if (!shopEntity.phone.equals("")
                                || shopEntity.phone != null) {
                            new AlertDialogDefault(mContext).builder()
                                    .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setPositiveButton("是", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.telePhoneBroadcast(mContext, shopEntity.phone);
                                }
                            }).show();
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            case R.id.tv_shopdetail_copywx:
                if (Utils.isStrNull(shopWxNum)) {
                    Utils.copy(shopWxNum, mContext);
                    ToastUtil.showToastShortBottom(mContext, "复制成功");
                }
                break;
            case R.id.tv_shopdetail_qrcode:
                Utils.setWeChatQrCodeDialog(mContext, shopWxNum, shopWxImg);
                break;
            case R.id.iv_shopdetail_backgone:
                finish();
                break;
            case R.id.btn_shopdetail_order:
                if (previous == Global.SERVICE_NEW_TO_CHOOSE_SHOPLIST) {
                    if (shopEntity != null) {
                        EventBus.getDefault().post(new ShopToServiceEvent(shopid, cityId, shopEntity.shopName, shopEntity.phone, shopEntity.shopimg, shopEntity.address, shopEntity.lat, shopEntity.lng, tag, shopEntity.operTime, dist));
                    }
                } else {
                    EventBus.getDefault().post(new ShopLocationEvent(lat, lng, shopid, shopEntity.shopName, city));
                    spUtil.saveString("nowCity", city);
                    spUtil.saveString("nowShop", shopEntity.shopName);
                    spUtil.saveString("nowShopAddr", shopEntity.address);
                    spUtil.saveFloat("nowShopLat", (float) shopEntity.lat);
                    spUtil.saveFloat("nowShopLng", (float) shopEntity.lng);
                    spUtil.saveString("shopWxImg", shopWxImg);
                    spUtil.saveString("shopWxNum", shopWxNum);
                    spUtil.saveString("shopPhone", shopEntity.phone);
                    spUtil.saveString("openTime", shopEntity.operTime);
                }
                if (MApplication.listAppoint1 != null && MApplication.listAppoint1.size() > 0) {
                    for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                        MApplication.listAppoint1.get(i).finish();
                    }
                }
                MApplication.listAppoint1.clear();
                finish();
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        if (shopEntity.imgList != null && shopEntity.imgList.size() > 0 && shopEntity.imgList.size() > position) {
            Utils.imageBrower(this, position, list.toArray(new String[list.size()]));
        }
    }
}
