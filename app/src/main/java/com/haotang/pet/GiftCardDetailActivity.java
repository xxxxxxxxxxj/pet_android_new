package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CardShopAdapter;
import com.haotang.pet.adapter.GiftDetailFreeAdapter;
import com.haotang.pet.adapter.GiftDetailPopFreeAdapter;
import com.haotang.pet.adapter.GiftDetailServiceAdapter;
import com.haotang.pet.adapter.GiftcardDetailPopAdapter;
import com.haotang.pet.adapter.UnUseCouponDetailAdapter;
import com.haotang.pet.entity.GiftCardDetail;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.haotang.pet.view.bannerview.CommonPagerAdapter;
import com.haotang.pet.view.bannerview.HorizontalStackTransformerWithRotation;
import com.makeramen.roundedimageview.RoundedImageView;
import com.umeng.analytics.MobclickAgent;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

/**
 * @author:姜谷蓄
 * @Date:2019/3/25
 * @Description:E卡详情页面
 */
public class GiftCardDetailActivity extends SuperActivity {


    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rl_giftcard_detail_free)
    RelativeLayout rlDetailFree;
    @BindView(R.id.rl_card_banner)
    RelativeLayout rlBanner;
    @BindView(R.id.tv_giftcard_nowprice)
    TextView tvNowPrice;
    @BindView(R.id.tv_giftcard_faceprice)
    TextView tvGiftcardFaceprice;
    @BindView(R.id.rl_giftcard_price)
    RelativeLayout rlGiftcardPrice;
    @BindView(R.id.tv_giftcard_tiptime)
    TextView tvGiftcardTiptime;
    @BindView(R.id.tv_giftcard_discount)
    TextView tvGiftcardDiscount;
    @BindView(R.id.tv_giftcard_title)
    TextView tvGiftcardTitle;
    @BindView(R.id.tv_giftcard_havenum)
    TextView tvGiftcardHavenum;
    @BindView(R.id.tv_giftcard_desc)
    TextView tvGiftcardDesc;
    @BindView(R.id.tv_giftcard_useshop)
    TextView tvGiftcardUseshop;
    @BindView(R.id.rl_giftcard_detail_one)
    RelativeLayout rlGiftcardDetailOne;
    @BindView(R.id.rl_card_banner_all)
    RelativeLayout rlBannerAll;
    @BindView(R.id.rl_giftcard_detail_two)
    RelativeLayout rlGiftcardDetailTwo;
    @BindView(R.id.tv_giftcard_freetip)
    TextView tvGiftcardFreetip;
    @BindView(R.id.rv_giftcard_freelist)
    RecyclerView rvGiftcardFreelist;
    @BindView(R.id.tv_giftcard_seefree)
    TextView tvGiftcardSeefree;
    @BindView(R.id.iv_giftcard_morefree)
    ImageView ivGiftcardMorefree;
    @BindView(R.id.tv_choose_one)
    TextView tvChooseOne;
    @BindView(R.id.iv_giftcard_choosed)
    ImageView ivGiftcardChoosed;
    @BindView(R.id.tv_giftcard_choosed)
    TextView tvGiftcardChoosed;
    @BindView(R.id.rl_carddetail_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.tv_giftcard_service)
    TextView tvGiftcardService;
    @BindView(R.id.iv_giftcard_liucheng)
    ImageView ivCardLiu;
    @BindView(R.id.tv_giftcard_gobuy)
    TextView tvGiftcardGobuy;
    @BindView(R.id.tfl_item_giftcard_tag)
    TagFlowLayout tfGiftCardTag;
    @BindView(R.id.cv_giftcard_detail_time)
    CountdownView cvGiftcardTime;
    @BindView(R.id.tv_giftcard_use_explain)
    TextView tvGiftcardExplain;
    @BindView(R.id.rl_giftcard_time)
    RelativeLayout rlGiftcardTime;
    @BindView(R.id.ll_giftcard_explain_iv)
    LinearLayout llGiftcardExplain;
    @BindView(R.id.tv_giftcard_totalprice)
    TextView tvTotalPrice;
    @BindView(R.id.vp_card_banner)
    ViewPager vpCardBanner;
    @BindView(R.id.tv_banner_nowpoint)
    TextView tvNowpoint;
    @BindView(R.id.tv_banner_allpoint)
    TextView tvAllpoint;
    @BindView(R.id.nv_giftcard_detail)
    NestedScrollView nvCardDetail;
    @BindView(R.id.rl_giftcard_buy)
    RelativeLayout rvCardBuy;
    @BindView(R.id.ll_carddetail_nonet)
    LinearLayout llNonet;
    @BindView(R.id.iv_card_detail_one)
    RoundedImageView ivDetailOne;
    @BindView(R.id.rl_b1anner_tip)
    RelativeLayout rlBannerTip;
    @BindView(R.id.tv_giftcard_buynum)
    TextView tvBuyNum;
    @BindView(R.id.iv_giftcard_useshop)
    ImageView ivShowShop;
    private static GiftCardDetailActivity act;
    private int cardTemplateId;
    private String shareImg;
    private String shareTitle;
    private String shareTxt;
    private String shareUrl;
    private int restrictAmount = 0;//限购数
    private List<GiftCardDetail.DataBean.CouponListBean> list = new ArrayList<>();
    private GiftDetailFreeAdapter detailFreeAdapter;
    private UnUseCouponDetailAdapter giveCouponAdapter;
    private List<String> shop = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();
    private List<GiftCardDetail.DataBean.CardTemplateDetailBean.TypeContentListBean> serviceList = new ArrayList<>();
    private List<GiftCardDetail.DataBean.CouponTypeListBean> allFreeList = new ArrayList<>();
    private List<GiftCardDetail.DataBean.CardTypeTemplateListBean> cardTypeTemplateList = new ArrayList<>();
    private static ArrayList<MyCouponCanUse> couponCanUseList = new ArrayList<MyCouponCanUse>();
    private int canBuy;
    private int shopStock;
    private int localCardId;
    private MProgressDialog pDialog;
    //订单确认页需要的参数
    private String intentImg;
    private String intentTitle;
    private String intentDesc;
    private String intentShopText;
    private double intentFaceValue;
    private double intentSaleValue;
    private int intentBuynum = 1;
    private String intentAgreement;
    private String saleDesc;
    private GiftCardDetail.DataBean.CardTemplateDetailBean cardTemplateDetail;
    private CommonPagerAdapter<String> horizontalStackAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int currentItem = vpCardBanner.getCurrentItem();
                currentItem++;
                vpCardBanner.setCurrentItem(currentItem);
                //mHandler.sendEmptyMessageDelayed(1, 5000);
            }
        }
    };
    private int isCanSaled;
    private String saleTip;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefundCardEvent event) {
        if (event != null && event.isRefund()) {
            getData();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        setView();
        initData();
        getData();
        setListener();
    }

    private void setListener() {
        cvGiftcardTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                if (saleTimeStart > 0) {
                    tvGiftcardTiptime.setText("距 结 束 还 剩");
                    cvGiftcardTime.start(Long.valueOf(cardTemplateDetail.getSaleTimeEnd()));
                    saleTimeStart = 0;
                } else if (saleTimeStart == 0) {
                    tvGiftcardGobuy.setText("已结束");
                    rlGiftcardTime.setVisibility(View.GONE);
                    rlGiftcardPrice.setBackgroundColor(Color.WHITE);
                    tvNowPrice.setTextColor(Color.RED);
                    tvGiftcardFaceprice.setTextColor(Color.RED);
                    tvGiftcardGobuy.setBackgroundColor(Color.GRAY);
                    tvGiftcardGobuy.setClickable(false);
                }
            }
        });
        //监听viewpager的按下抬起 //不用做这个
//        vpCardBanner.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mHandler.removeCallbacksAndMessages(null);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        //mHandler.sendEmptyMessageDelayed(1, 5000);
//                        break;
//                }
//                return false;
//            }
//        });
        //监听banner的滑动事件
        vpCardBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvNowpoint.setText(position % bannerList.size() + 1 + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        act = this;
        MApplication.listAppoint.add(act);
        cardTemplateId = getIntent().getIntExtra("cardTemplateId", 0);
    }

    private void getData() {
        pDialog.showDialog();
        mHandler.removeCallbacksAndMessages(null);
        CommUtil.getGiftCardDetail(this, cardTemplateId, getCardDetail);
    }

    private void setView() {
        //设置分享的图片
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        ViewGroup.LayoutParams layoutParams = ibTitlebarOther.getLayoutParams();
       /* layoutParams.width = Math.round(80 * 2 / 3 * density);
        layoutParams.height = Math.round(80 * 2 / 3 * density);
        ibTitlebarOther.setLayoutParams(layoutParams);*/
        ibTitlebarOther.setBackgroundResource(R.drawable.icon_giftcard_share);
        ibTitlebarOther.setVisibility(View.GONE);
        pDialog = new MProgressDialog(this);
        vpCardBanner.setOffscreenPageLimit(3);


        //getBanner();
        //标题
        tvTitlebarTitle.setText("E卡详情");
        //赠送列表
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvGiftcardFreelist.setLayoutManager(noScollFullLinearLayoutManager);
        rvGiftcardFreelist.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 5),
                ContextCompat.getColor(this, R.color.af8f8f8)));
        detailFreeAdapter = new GiftDetailFreeAdapter(R.layout.item_giftcard_detail_item, list);
        rvGiftcardFreelist.setAdapter(detailFreeAdapter);
    }

    private void getAdapter() {
        horizontalStackAdapter = new CommonPagerAdapter<String>(bannerList) {
            @Override
            public void renderItemView(@NonNull View itemView, int position) {
                if (bannerList != null && bannerList.size() != 0) {
                    RoundedImageView ivBanner = (RoundedImageView) itemView.findViewById(R.id.iv_item_banner);
                    String imageUrl = bannerList.get(position % bannerList.size());
                    GlideUtil.loadImg(GiftCardDetailActivity.this, bannerList.get(position % bannerList.size()), ivBanner, R.drawable.icon_production_default);
                }

            }

            @NonNull
            @Override
            public View getPageItemView(@NonNull ViewGroup container, int position) {
                View view = LayoutInflater.from(GiftCardDetailActivity.this).inflate(R.layout.item_card_banner, container, false);
                view.setTag(String.valueOf(position));
                return view;
            }
        };
    }

    private long saleTimeStart;
    private String intentTagnames;
    private String discountDesc;
    private List<String> discountDescNew = new ArrayList<>();
    private AsyncHttpResponseHandler getCardDetail = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            pDialog.closeDialog();
            llNonet.setVisibility(View.GONE);
            nvCardDetail.setVisibility(View.VISIBLE);
            rvCardBuy.setVisibility(View.VISIBLE);
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        Gson gson = new Gson();
                        GiftCardDetail giftCardDetail = gson.fromJson(new String(responseBody), GiftCardDetail.class);
                        if (giftCardDetail != null) {
                            GiftCardDetail.DataBean dataBean = giftCardDetail.getData();
                            intentAgreement = dataBean.getAgreement();//获取E卡协议
                            if (dataBean != null) {
                                //流程图
                                GlideUtil.loadImg(GiftCardDetailActivity.this, dataBean.getSaleProcessUrl(), ivCardLiu, R.drawable.icon_giftcard_flow);
                                //分享的bean
                                GiftCardDetail.DataBean.ShareBean shareBean = dataBean.getShare();
                                if (shareBean != null) {
                                    ibTitlebarOther.setVisibility(View.VISIBLE);
                                    shareImg = shareBean.getImg();
                                    shareTitle = shareBean.getTitle();
                                    shareTxt = shareBean.getDesc();
                                    shareUrl = shareBean.getUrl();
                                } else {
                                    ibTitlebarOther.setVisibility(View.GONE);
                                }
                                //点击已选的商品列表
                                if (dataBean.getCardTypeTemplateList() != null && dataBean.getCardTypeTemplateList().size() != 0) {
                                    cardTypeTemplateList = dataBean.getCardTypeTemplateList();
                                }
                                List<String> tagNames = dataBean.getTagNames();
                                intentTagnames = TextUtils.join(",", tagNames);
                                //判断当前可购买的状态
                                isCanSaled = dataBean.getIsCanSaled();
                                switch (isCanSaled) {
                                    case 0:
                                        tvGiftcardGobuy.setText("去付款");
                                        tvGiftcardGobuy.setClickable(true);
                                        rlGiftcardPrice.setBackgroundResource(R.color.aD0021B);
                                        tvNowPrice.setTextColor(Color.WHITE);
                                        tvGiftcardFaceprice.setTextColor(Color.WHITE);
                                        tvGiftcardGobuy.setBackgroundResource(R.drawable.bg_red_jianbian);
                                        tvGiftcardGobuy.setClickable(true);
                                        break;
                                    case 1:
                                        tvGiftcardGobuy.setText("已售罄");
                                        rlGiftcardTime.setVisibility(View.GONE);
                                        rlGiftcardPrice.setBackgroundColor(Color.WHITE);
                                        tvNowPrice.setTextColor(Color.RED);
                                        tvGiftcardFaceprice.setTextColor(Color.RED);
                                        tvGiftcardGobuy.setBackgroundColor(Color.GRAY);
                                        tvGiftcardGobuy.setClickable(false);
                                        break;
                                    case 2:
                                        tvGiftcardGobuy.setText("未开始");
                                        tvGiftcardGobuy.setBackgroundColor(Color.GRAY);
                                        tvGiftcardGobuy.setClickable(false);
                                        rlGiftcardPrice.setBackgroundResource(R.color.aD0021B);
                                        tvNowPrice.setTextColor(Color.WHITE);
                                        tvGiftcardFaceprice.setTextColor(Color.WHITE);
                                        break;
                                    case 3:
                                        tvGiftcardGobuy.setText("已结束");
                                        rlGiftcardTime.setVisibility(View.GONE);
                                        rlGiftcardPrice.setBackgroundColor(Color.WHITE);
                                        tvNowPrice.setTextColor(Color.RED);
                                        tvGiftcardFaceprice.setTextColor(Color.RED);
                                        tvGiftcardGobuy.setBackgroundColor(Color.GRAY);
                                        tvGiftcardGobuy.setClickable(false);
                                        break;
                                    case 4:
                                        tvGiftcardGobuy.setText("去付款");
                                        tvGiftcardGobuy.setBackgroundColor(Color.GRAY);
                                        tvGiftcardGobuy.setClickable(true);
                                        break;
                                    case 5:
                                        new AlertDialogNavAndPost(GiftCardDetailActivity.this)
                                                .builder()
                                                .setTitle("")
                                                .setMsg(dataBean.getSaleDesc())
                                                .setCancelable(false)
                                                .setNegativeButtonVisible(View.GONE)
                                                .setPositiveButton("确认", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        finish();
                                                    }
                                                }).show();
                                        break;
                                }
                                saleTip = dataBean.getSaleDesc();
                                //tvGiftcardHavenum.setText(dataBean.getSaleText());//库存多少张
                                tvGiftcardUseshop.setText(dataBean.getShopText());//适用门店
                                intentShopText = dataBean.getShopText();

                                //服务卡详情的bean
                                GiftCardDetail.DataBean.CardTemplateDetailBean cardTemplateDetail = dataBean.getCardTemplateDetail();
                                if (cardTemplateDetail != null) {
                                    //双折扣标签
                                    if (cardTemplateDetail.getDiscountDescNew() != null && cardTemplateDetail.getDiscountDescNew().size() > 0) {

                                        tfGiftCardTag.setVisibility(View.VISIBLE);
                                        tfGiftCardTag.setAdapter(new TagAdapter<String>(cardTemplateDetail.getDiscountDescNew()) {
                                            @Override
                                            public View getView(FlowLayout parent, int position, String s) {
                                                View view = (View) View.inflate(mContext, R.layout.item_carddescount_tag,
                                                        null);
                                                TextView tv_item_foster_roomtag = (TextView) view.findViewById(R.id.tv_item_foster_roomtag);
                                                tv_item_foster_roomtag.setText(s);
                                                return view;
                                            }
                                        });
                                    } else {
                                        tfGiftCardTag.setVisibility(View.GONE);
                                    }
                                    if (cardTemplateDetail.getDiscountDescOrder()!=null&&cardTemplateDetail.getDiscountDescOrder().size()>0){
                                        discountDescNew = cardTemplateDetail.getDiscountDescOrder();
                                    }
                                    //判断是否赠送优惠券
                                    if (cardTemplateDetail.getRewardType() == 0) {
                                        rlDetailFree.setVisibility(View.GONE);
                                    } else {
                                        //优惠券的列表
                                        List<GiftCardDetail.DataBean.CouponListBean> couponList = dataBean.getCouponList();
                                        if (couponList != null && couponList.size() != 0) {
                                            //判断购买的数量
                                            if (intentBuynum == 1) {
                                                for (int i = 0; i < couponList.size(); i++) {
                                                    couponList.get(i).setFreeNum(intentBuynum);
                                                }
                                            } else {
                                                for (int i = 0; i < couponList.size(); i++) {
                                                    couponList.get(i).setFreeNum(intentBuynum);
                                                }
                                            }
                                            rlDetailFree.setVisibility(View.VISIBLE);
                                            list.clear();
                                            list.addAll(couponList);
                                            detailFreeAdapter.notifyDataSetChanged();

                                        }
                                        //优惠券的详情集合
                                        JSONArray jsonArray = jdata.getJSONArray("couponTypeList");
                                        if (jsonArray.length() > 0) {
                                            couponCanUseList.clear();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                couponCanUseList.add(MyCouponCanUse.json2Entity(jsonArray.getJSONObject(i)));
                                            }
                                        }

                                        /*List<GiftCardDetail.DataBean.CouponTypeListBean> couponTypeList = dataBean.getCouponTypeList();
                                        if (couponTypeList != null && couponTypeList.size() != 0) {
                                            allFreeList.clear();
                                            allFreeList.addAll(couponTypeList);
                                        }*/
                                    }
                                    //顶部轮播图
                                    String banner = cardTemplateDetail.getBanner();
                                    String[] bannerSplit = banner.split(",");
                                    bannerList.clear();
//                                    horizontalStackAdapter.notifyDataSetChanged();
                                    if (bannerSplit.length < 2) {
                                        mHandler.removeCallbacksAndMessages(null);
                                        rlBannerAll.setBackgroundResource(R.drawable.icon_card_bgshade);
                                        vpCardBanner.setVisibility(View.GONE);
                                        rlBannerTip.setVisibility(View.INVISIBLE);
                                        tvAllpoint.setText("/1");
                                        ivDetailOne.setVisibility(View.VISIBLE);
                                        GlideUtil.loadImg(GiftCardDetailActivity.this, bannerSplit[0], ivDetailOne, R.drawable.icon_image_default);
                                    } else {
                                        vpCardBanner.setVisibility(View.VISIBLE);
                                        rlBannerTip.setVisibility(View.VISIBLE);
                                        ivDetailOne.setVisibility(View.GONE);
                                        rlBannerAll.setBackgroundColor(Color.WHITE);
                                        for (int i = 0; i < bannerSplit.length; i++) {
                                            bannerList.add(bannerSplit[i]);
                                        }
                                        tvAllpoint.setText("/" + bannerList.size());


                                        if (horizontalStackAdapter != null) {
                                            horizontalStackAdapter.setDataList(bannerList);
                                        } else {
                                            getAdapter();
                                            vpCardBanner.setPageTransformer(false,
                                                    new HorizontalStackTransformerWithRotation(vpCardBanner));
                                            vpCardBanner.setAdapter(horizontalStackAdapter);
                                        }


                                        // vpCardBanner.setCurrentItem(Integer.MAX_VALUE/2);//设置到一半
                                        //mHandler.sendEmptyMessageDelayed(1, 5000);
                                    }
                                    for (int i = 0; i < cardTypeTemplateList.size(); i++) {
                                        if (cardTemplateId == cardTypeTemplateList.get(i).getTemplateId()) {
                                            GiftCardDetailActivity.this.saleDesc = cardTypeTemplateList.get(i).getSaleDesc();
                                            canBuy = cardTypeTemplateList.get(i).getRestrictAmount();
                                        }
                                    }
                                    //现价
                                    if (Utils.isDoubleEndWithZero(Double.valueOf(cardTemplateDetail.getSalePrice()))) {
                                        SpannableString spannableString = new SpannableString("¥" + Utils.formatDecimal("#0.00", Double.valueOf(cardTemplateDetail.getSalePrice())));
                                        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(10, true);
                                        spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                        tvNowPrice.setText(spannableString);
                                        tvTotalPrice.setText(Utils.formatDecimal("#0.00", Double.valueOf(cardTemplateDetail.getSalePrice())) + "");
                                        if (intentBuynum == 1) {
                                            tvBuyNum.setText("x1");
                                        } else {
                                            tvBuyNum.setText("x" + intentBuynum);
                                        }
                                    } else {
                                        SpannableString spannableString = new SpannableString("¥" + Utils.formatDecimal("#0.00", Double.valueOf(cardTemplateDetail.getSalePrice())) + "");
                                        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(10, true);
                                        spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                        tvTotalPrice.setText(Utils.formatDecimal("#0.00", Double.valueOf(cardTemplateDetail.getSalePrice())) + "");
                                        if (intentBuynum == 1) {
                                            tvBuyNum.setText("x1");
                                        } else {
                                            tvBuyNum.setText("x" + intentBuynum);
                                        }
                                        tvNowPrice.setText(spannableString);
                                    }
                                    intentSaleValue = Double.valueOf(cardTemplateDetail.getSalePrice());
                                    intentImg = cardTemplateDetail.getSmallPic();//E卡正面
                                    discountDesc = cardTemplateDetail.getDiscountDesc();
                                    Utils.setText(tvGiftcardDiscount, discountDesc, "", View.GONE, View.GONE);//标题前的描述
                                    intentDesc = cardTemplateDetail.getDiscountDesc();
                                    tvGiftcardFaceprice.setText("面值¥" + Double.valueOf(cardTemplateDetail.getFaceValue()) + "");//面值
                                    intentFaceValue = Double.valueOf(cardTemplateDetail.getFaceValue());
                                    tvGiftcardTitle.setText(cardTemplateDetail.getServiceCardTypeName());//E卡名称
                                    intentTitle = cardTemplateDetail.getServiceCardTypeName();
                                    tvGiftcardDesc.setText(cardTemplateDetail.getSubTitle());//E卡描述
                                    tvGiftcardExplain.setText(cardTemplateDetail.getUseExplain());//使用说明
                                    //获取说明图片
                                    llGiftcardExplain.removeAllViews();
                                    if (cardTemplateDetail.getDetailPic() != null && !"".endsWith(cardTemplateDetail.getDetailPic().trim())) {
                                        llGiftcardExplain.setVisibility(View.VISIBLE);
                                        String detailPic = cardTemplateDetail.getDetailPic();
                                        String[] splitPic = detailPic.split(",");
                                        for (int i = 0; i < splitPic.length; i++) {
                                            ImageView imageView = new ImageView(GiftCardDetailActivity.this);
                                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                            GlideUtil.loadImg(GiftCardDetailActivity.this, splitPic[i], imageView, R.drawable.icon_image_default);
                                            llGiftcardExplain.addView(imageView);
                                        }
                                    } else {
                                        llGiftcardExplain.setVisibility(View.GONE);
                                    }
                                    //获取该商品的标签
                                    String tagType = cardTemplateDetail.getTagType();
                                    String[] splitTag = tagType.split(",");
                                    for (int i = 0; i < splitTag.length; i++) {
                                        if (Integer.valueOf(splitTag[i]) == 5) {//说明商品是限购的
                                            restrictAmount = cardTemplateDetail.getRestrictAmount();
                                        }
                                        if (Integer.valueOf(splitTag[i]) == 3) {//说明商品限量出售
                                            shopStock = cardTemplateDetail.getStock();
                                        }
                                    }
                                    if (intentBuynum == 1) {
                                        tvGiftcardChoosed.setText(cardTemplateDetail.getServiceCardTypeName() + "  面值¥" + cardTemplateDetail.getFaceValue() + "X1");//默认已选
                                    } else {
                                        tvGiftcardChoosed.setText(cardTemplateDetail.getServiceCardTypeName() + "  面值¥" + cardTemplateDetail.getFaceValue() + "X" + intentBuynum);//默认已选
                                    }
                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    //时间设置
                                    if (cardTemplateDetail.getIsShowTime() == 0) {
                                        lp.setMargins(0, Utils.dip2px(GiftCardDetailActivity.this, 240), 0, 0);
                                        rl_bottom.setLayoutParams(lp);
                                        rlGiftcardTime.setVisibility(View.VISIBLE);
                                        rlGiftcardPrice.setBackgroundResource(R.color.aD0021B);
                                        tvNowPrice.setTextColor(Color.WHITE);
                                        tvGiftcardFaceprice.setTextColor(Color.WHITE);
                                        saleTimeStart = cardTemplateDetail.getSaleTimeStart();
                                        if (saleTimeStart > 0) {
                                            tvGiftcardTiptime.setText("距 开 始 还 剩");
                                            cvGiftcardTime.updateShow(Long.valueOf(cardTemplateDetail.getSaleTimeStart()));
                                            cvGiftcardTime.start(Long.valueOf(cardTemplateDetail.getSaleTimeStart()));
                                        } else if (saleTimeStart == 0) {
                                            tvGiftcardTiptime.setText("距 结 束 还 剩");
                                            cvGiftcardTime.updateShow(Long.valueOf(cardTemplateDetail.getSaleTimeEnd()));
                                            cvGiftcardTime.start(Long.valueOf(cardTemplateDetail.getSaleTimeEnd()));
                                        }
                                    } else {
                                        lp.setMargins(0, Utils.dip2px(GiftCardDetailActivity.this, 235), 0, 0);
                                        rl_bottom.setLayoutParams(lp);
                                        rlGiftcardTime.setVisibility(View.GONE);
                                        rlGiftcardPrice.setBackgroundColor(Color.WHITE);
                                        tvNowPrice.setTextColor(Color.RED);
                                        tvGiftcardFaceprice.setTextColor(Color.RED);
                                    }
                                    //服务列表
                                    List<GiftCardDetail.DataBean.CardTemplateDetailBean.TypeContentListBean> typeContentList = cardTemplateDetail.getTypeContentList();
                                    if (typeContentList != null && typeContentList.size() != 0) {
                                        serviceList = typeContentList;
                                        StringBuffer service = new StringBuffer();
                                        for (int i = 0; i < typeContentList.size(); i++) {
                                            if (i == typeContentList.size() - 1) {
                                                service.append(typeContentList.get(i).getTagText());
                                            } else {
                                                service.append(typeContentList.get(i).getTagText() + "·");
                                            }
                                        }
                                        tvGiftcardService.setText(service.toString());
                                    }
                                }
                                tvGiftcardUseshop.setText(dataBean.getShopText());
                                //适用店铺的集合
                                List<String> shopList = dataBean.getShopList();
                                if (shopList == null || shopList.size() == 0) {
                                    ivShowShop.setVisibility(View.INVISIBLE);
                                } else {
                                    ivShowShop.setVisibility(View.VISIBLE);
                                }
                                if (shopList != null && shopList.size() != 0) {
                                    shop = shopList;
                                }

                            }
                        }

                    }
                } else {
                    ToastUtil.showToastShortBottom(GiftCardDetailActivity.this, msg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            llNonet.setVisibility(View.VISIBLE);
            nvCardDetail.setVisibility(View.GONE);
            rvCardBuy.setVisibility(View.GONE);
            ToastUtil.showToastShortBottom(GiftCardDetailActivity.this, "请求失败");
        }
    };

    private void findView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_gift_card_detail);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.rl_giftcard_choose, R.id.rl_giftcard_useshop, R.id.rl_giftcard_service, R.id.tv_giftcard_seefree, R.id.tv_giftcard_gobuy, R.id.btn_carddetail_nonet, R.id.rl_giftcard_detail_free})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_other:
                Utils.share(GiftCardDetailActivity.this, shareImg, shareTitle, shareTxt, shareUrl + "?cardTemplateId=" + cardTemplateId, "1,2", 0,null);
                break;
            case R.id.rl_giftcard_choose:
                rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(GiftCardDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                rl_commodity_black.setVisibility(View.VISIBLE);
                rl_commodity_black.bringToFront();
                showInfoDialog();
                break;
            case R.id.rl_giftcard_useshop:
                if (shop != null && shop.size() != 0) {
                    rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(GiftCardDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                    rl_commodity_black.setVisibility(View.VISIBLE);
                    rl_commodity_black.bringToFront();
                    showShopDialog(1);
                }
                break;
            case R.id.rl_giftcard_service:
                rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(GiftCardDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                rl_commodity_black.setVisibility(View.VISIBLE);
                rl_commodity_black.bringToFront();
                showShopDialog(2);
                break;
            case R.id.tv_giftcard_seefree:
                rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(GiftCardDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                rl_commodity_black.setVisibility(View.VISIBLE);
                rl_commodity_black.bringToFront();
                showCouponpop();
                //showShopDialog(3);
                break;
            case R.id.tv_giftcard_gobuy:
                if (isCanSaled == 4) {
                    if (saleTip != null) {
                        ToastUtil.showToastShortBottom(GiftCardDetailActivity.this, saleTip);
                    }
                } else if (!Utils.checkLogin(GiftCardDetailActivity.this)) {
                    Intent intent = new Intent(GiftCardDetailActivity.this, LoginNewActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GiftCardDetailActivity.this, GiftCardSureOrderActivity.class);
//                    intent.putExtra("tagNames", intentTagnames);
                    intent.putExtra("discountDesc", discountDesc);
                    intent.putExtra("orderIcon", intentImg);
                    intent.putExtra("orderTitle", intentTitle);
                    intent.putExtra("orderDesc", intentDesc);
                    intent.putExtra("orderShoptext", intentShopText);
                    intent.putExtra("orderFace", intentFaceValue);
                    intent.putExtra("orderSale", intentSaleValue);
                    intent.putExtra("orderNum", intentBuynum);
                    intent.putExtra("orderFreelist", (Serializable) list);
                    intent.putExtra("orderAgreement", intentAgreement);
                    intent.putExtra("canBuyNum", canBuy);
                    intent.putExtra("saleDesc", saleDesc);
                    intent.putExtra("cardTemplateId", cardTemplateId);
                    intent.putStringArrayListExtra("orderShopList", (ArrayList<String>) shop);
                    intent.putStringArrayListExtra("discountDescNew", (ArrayList<String>) discountDescNew);
                    startActivity(intent);
                }
                break;
            case R.id.btn_carddetail_nonet:

                getData();
                break;
        }
    }

    private void showCouponpop() {
        ViewGroup customView = (ViewGroup) View.inflate(GiftCardDetailActivity.this, R.layout.pop_carddetail_coupon, null);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        RelativeLayout rl_pop_dimess = customView.findViewById(R.id.rl_pop_dimess);
        ListView lv_coupon_list = customView.findViewById(R.id.lv_couponpop_list);
        giveCouponAdapter = new UnUseCouponDetailAdapter(mContext,couponCanUseList,1);
        lv_coupon_list.setAdapter(giveCouponAdapter);
        lv_coupon_list.setDivider(null);
        giveCouponAdapter.notifyDataSetChanged();
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(GiftCardDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_appointpetmx_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        rl_pop_dimess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
    }

    private void showInfoDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(GiftCardDetailActivity.this, R.layout.giftcarddetail_shopinfo_pop, null);
        RecyclerView rl_giftcarddetail_pricelist = (RecyclerView) customView.findViewById(R.id.rl_giftcarddetail_pricelist);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        Button btn_giftcarddetail_sure = (Button) customView.findViewById(R.id.btn_giftcarddetail_sure);
        final ImageView iv_jian = (ImageView) customView.findViewById(R.id.iv_giftcarddetail_jian);
        final ImageView iv_add = (ImageView) customView.findViewById(R.id.iv_giftcarddetail_add);
        final TextView edBuynum = (TextView) customView.findViewById(R.id.ed_giftcarddetail_buynum);
        final ImageView iv_giftcarddetail = (ImageView) customView.findViewById(R.id.iv_giftcarddetail);
        final TextView tv_giftcarddetail_price = (TextView) customView.findViewById(R.id.tv_giftcarddetail_price);
        final TextView tv_giftcarddetail_num = (TextView) customView.findViewById(R.id.tv_giftcarddetail_num);
        GridLayoutManager manager = new GridLayoutManager(GiftCardDetailActivity.this, 3);
        rl_giftcarddetail_pricelist.setLayoutManager(manager);
        //cardTypeTemplateList.clear();
        for (int i = 0; i < cardTypeTemplateList.size(); i++) {
            if (cardTypeTemplateList.get(i).getRestrictAmount() < 1) {
                cardTypeTemplateList.get(i).setRestrictAmount(1);
            }
        }
        //判断当前卡的id
        for (int i = 0; i < cardTypeTemplateList.size(); i++) {
            if (cardTemplateId == cardTypeTemplateList.get(i).getTemplateId()) {
                SpannableString spannableString = new SpannableString(cardTypeTemplateList.get(i).getSalePrice());
                spannableString.setSpan(new RelativeSizeSpan(0.6f), cardTypeTemplateList.get(i).getSalePrice().indexOf("."), cardTypeTemplateList.get(i).getSalePrice().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_giftcarddetail_price.setText(spannableString);
                GlideUtil.loadImg(GiftCardDetailActivity.this, cardTypeTemplateList.get(i).getSmallPic(), iv_giftcarddetail, R.drawable.icon_production_default);
                tv_giftcarddetail_num.setText(cardTypeTemplateList.get(i).getSaleText());
                saleDesc = cardTypeTemplateList.get(i).getSaleDesc();
                cardTypeTemplateList.get(i).setCheck(true);
                canBuy = cardTypeTemplateList.get(i).getRestrictAmount();
            }
        }

        GiftcardDetailPopAdapter adapter = new GiftcardDetailPopAdapter(R.layout.item_giftcarddetail_pop, cardTypeTemplateList);
        rl_giftcarddetail_pricelist.setAdapter(adapter);
        //价格条目的点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < cardTypeTemplateList.size(); i++) {
                    if (i == position) {
                        cardTypeTemplateList.get(i).setCheck(true);
                    } else {
                        cardTypeTemplateList.get(i).setCheck(false);
                    }
                }
                if (cardTypeTemplateList.get(position).getRestrictAmount() == 0) {
                    iv_add.setClickable(false);
                    iv_jian.setClickable(false);
                    iv_jian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                }
                saleDesc = cardTypeTemplateList.get(position).getSaleDesc();
                SpannableString spannableString = new SpannableString(cardTypeTemplateList.get(position).getSalePrice());
                spannableString.setSpan(new RelativeSizeSpan(0.6f), cardTypeTemplateList.get(position).getSalePrice().indexOf("."), cardTypeTemplateList.get(position).getSalePrice().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_giftcarddetail_price.setText(spannableString);
                GlideUtil.loadImg(GiftCardDetailActivity.this, cardTypeTemplateList.get(position).getSmallPic(), iv_giftcarddetail, R.drawable.icon_production_default);
                tv_giftcarddetail_num.setText(cardTypeTemplateList.get(position).getSaleText());
                canBuy = cardTypeTemplateList.get(position).getRestrictAmount();
                edBuynum.setText(1 + "");
                cardTemplateId = cardTypeTemplateList.get(position).getTemplateId();
                adapter.notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();
        //加减号的点击事件
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String snum = edBuynum.getText().toString();
                Integer inum = Integer.valueOf(snum);
                inum++;
                edBuynum.setText(inum + "");
            }
        });
        iv_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String snum = edBuynum.getText().toString();
                Integer inum = Integer.valueOf(snum);
                inum--;
                edBuynum.setText(inum + "");
            }
        });
        if (intentBuynum > 1) {
            edBuynum.setText(intentBuynum + "");
        }

        //edittext的动态监听
        edBuynum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Integer.valueOf(edBuynum.getText().toString()) < 1) {
                    edBuynum.setText(1 + "");
                    iv_jian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                    iv_jian.setClickable(false);
                } else if (Integer.valueOf(edBuynum.getText().toString()) > canBuy) {
                    edBuynum.setText(canBuy + "");
                    ToastUtil.showToastShortBottom(GiftCardDetailActivity.this, saleDesc);
                } else if (Integer.valueOf(edBuynum.getText().toString()) == 1) {
                    iv_jian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                    iv_jian.setClickable(false);
                } else {
                    iv_jian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                    iv_jian.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final PopupWindow pWinBottomDialog = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //确认按钮的点击事件
        btn_giftcarddetail_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentBuynum = Integer.valueOf(edBuynum.getText().toString());
                for (int i = 0; i < cardTypeTemplateList.size(); i++) {
                    if (cardTypeTemplateList.get(i).isCheck()) {
                        double salePrice = Double.valueOf(cardTypeTemplateList.get(i).getSalePrice());
                        shopStock = cardTypeTemplateList.get(i).getRestrictAmount();
                        cardTemplateId = cardTypeTemplateList.get(i).getTemplateId();
                        if (Utils.isDoubleEndWithZero(salePrice)) {
                            tvTotalPrice.setText("¥" + Utils.formatDouble(salePrice));
                        } else {
                            tvTotalPrice.setText("¥" + salePrice);
                        }
                    }
                }
                horizontalStackAdapter = null;
                tvNowpoint.setText("1");//恢复初始值
                getData();
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setFocusable(true);// 取得焦点 
        // 注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的 
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失 
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击 
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画 
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(GiftCardDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_appointpetmx_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
    }

    private void showShopDialog(int type) {
        ViewGroup customView = (ViewGroup) View.inflate(GiftCardDetailActivity.this, R.layout.shop_bottom_dialog, null);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        final RecyclerView rv_appointpetmx_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpetmx_bottomdia);
        ImageView iv_appointpetmx_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg);
        ImageView iv_appointpetmx_bottom_bg_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg_close);
        LinearLayout ll_pop_root = (LinearLayout) customView.findViewById(R.id.ll_pop_root);
        TextView tv_appointpetmx_bottomdia_title = (TextView) customView.findViewById(R.id.tv_appointpetmx_bottomdia_title);
        RelativeLayout rl_appointpetmx_bottomdia = (RelativeLayout) customView.findViewById(R.id.rl_appointpetmx_bottomdia);
        LinearLayout ll_appointpetmx_bottomdia = (LinearLayout) customView.findViewById(R.id.ll_appointpetmx_bottomdia);
        ll_appointpetmx_bottomdia.bringToFront();
        rv_appointpetmx_bottomdia.setHasFixedSize(true);
        rv_appointpetmx_bottomdia.setLayoutManager(new LinearLayoutManager(this));//添加自定义分割线 
        rv_appointpetmx_bottomdia.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL, DensityUtil.dp2px(this, 1), ContextCompat.getColor(this, R.color.aEEEEEE)));
        switch (type) {
            case 1:
                tv_appointpetmx_bottomdia_title.setText("适用门店");
                if (shop != null && shop.size() > 0) {
                    rv_appointpetmx_bottomdia.setAdapter(new CardShopAdapter(R.layout.item_card_shop, shop));
                }
                break;
            case 2:
                tv_appointpetmx_bottomdia_title.setText("服务说明");
                if (serviceList != null && serviceList.size() != 0) {
                    rv_appointpetmx_bottomdia.setAdapter(new GiftDetailServiceAdapter(R.layout.item_giftcard_detail_service, serviceList));
                }
                break;
            case 3:
                tv_appointpetmx_bottomdia_title.setText("赠送");
                if (allFreeList != null && allFreeList.size() != 0) {
                    rv_appointpetmx_bottomdia.setLayoutManager(new LinearLayoutManager(this));//添加自定义分割线 
                    rv_appointpetmx_bottomdia.setAdapter(new GiftDetailPopFreeAdapter(R.layout.item_giftcard_detail_free, allFreeList));
                }
                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_appointpetmx_bottomdia.scrollToPosition(0);
            }
        }, 500);
        float screenDensity = ScreenUtil.getScreenDensity(this);
        Log.e("TAG", "screenDensity = " + screenDensity);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(GiftCardDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        ll_pop_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        rl_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });

        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        ll_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
