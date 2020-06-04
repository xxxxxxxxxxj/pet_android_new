package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CommodityDetailImgAdapter;
import com.haotang.pet.adapter.CommodityHotSaleAdapter;
import com.haotang.pet.adapter.CommodityPopSpecificationsAdapter;
import com.haotang.pet.adapter.RechargeBannerAdapter;
import com.haotang.pet.adapter.ShopMallRecommendAdapter;
import com.haotang.pet.entity.CommodityHotSale;
import com.haotang.pet.entity.CommodityMarquee;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.MallCommodityGroup;
import com.haotang.pet.entity.RechargeBanner;
import com.haotang.pet.entity.ShopMallFragRecommend;
import com.haotang.pet.mall.MallOrderConfirmActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshHeadGridView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengShareUtils;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.ExtendedEditText;
import com.haotang.pet.view.GlideImageLoader;
import com.haotang.pet.view.HeaderGridView;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.NoScollSyLinearLayoutManager;
import com.haotang.pet.view.TagTextView;
import com.haotang.pet.view.TimeTaskScroll;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.OauthHelper;
import com.youth.banner.Banner;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商品详情页
 */
public class CommodityDetailActivity extends SuperActivity implements
        View.OnClickListener {
    private MProgressDialog pDialog;
    private SharedPreferenceUtil spUtil;
    private View header;
    private RelativeLayout rl_commodity_gwc;
    private TextView tv_commodity_gwcnum;
    private RelativeLayout rl_commodity_order;
    private TextView tv_commodity_ordernum;
    private ImageButton ib_commodity_back;
    private LinearLayout ll_commodity_left;
    private View vw_commodity_left;
    private LinearLayout ll_commodity_right;
    private View vw_commodity_right;
    private LinearLayout rl_commodity_bottom_submit;
    private TextView tv_commodity_bottom_guantounum;
    private LinearLayout tv_commodity_bottom_kefu;
    private LinearLayout tv_commodity_bottom_share;
    private PullToRefreshHeadGridView ptrhgv_commodity;
    private Banner rpv_header_commodity;
    private TextView tv_header_commodity_xianjia;
    private TextView tv_header_commodity_yuanjia;
    private TextView tv_header_commodity_guantou;
    private TextView tv_header_commodity_kucun;
    private ImageView iv_header_commodity_quanyi;
    private TextView tv_header_commodityselect;
    private HorizontalScrollView hsv_header_commodity;
    private GridView gv_header_commodity;
    private RecyclerView rv_header_commodity_img;
    private RelativeLayout rl_header_commodity_rpv;
    private ArrayList<String> listBanner = new ArrayList<String>();
    private List<CommodityMarquee> listCommodityMarquee = new ArrayList<CommodityMarquee>();
    private List<CommodityHotSale> listCommodityHotSale = new ArrayList<CommodityHotSale>();
    private List<MallCommodityGroup> listMallCommodityGroup = new ArrayList<MallCommodityGroup>();
    private List<String> listCommodityDetailImg = new ArrayList<String>();
    private List<ShopMallFragRecommend> listRecommend = new ArrayList<ShopMallFragRecommend>();
    private List<ShopMallFragRecommend> localListRecommend = new ArrayList<ShopMallFragRecommend>();
    private CommodityHotSaleAdapter<CommodityHotSale> commodityHotSaleAdapter;
    private CommodityDetailImgAdapter commodityDetailImgAdapter;
    private ShopMallRecommendAdapter<ShopMallFragRecommend> shopMallRecommendAdapter;
    private int itemWidth;
    private PopupWindow pWin;
    private IWXAPI api;
    private UmengShareUtils umengShareUtils;
    protected Bitmap bmp;
    protected String shareurl = "http://www.haotang365.com.cn/";
    private String sharetitle = "宠物家";
    private String sharetxt = "宠物家";
    private String shareImg = "http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/train/imgs/14829919329058296826.jpg";
    private String shopMallId;
    private int commodityId;
    private String marketingTag;//商品营销标签
    private String title;//
    private String subtitle;
    private double marketValue;
    private double retailPrice;
    private String mallCommodityVipCanTitle;
    private int stock;
    private String guaranteeSmallPic;
    private String specName;
    private String guaranteeBigPic;
    private String mallKfUrl;
    private String mallCommodityVipCanTitle1;
    private int userMallCartNum;
    private String mallCommodityCartTitle;
    private TagTextView tv_header_commodity_title;
    private TextView tv_header_commodity_futitle;
    private int shopMallNum = 1;
    private LinearLayout ll_header_commodity_rxph;
    private LinearLayout ll_header_commodity_detailimg;
    private int payOrCar;
    private CommodityPopSpecificationsAdapter<MallCommodityGroup> mallCommodityGroupCommodityPopSpecificationsAdapter;
    private PopupWindow pWinShopInfo;
    private ViewGroup customView;
    private TextView tv_commodity_shopinfo_pop_shopggstr;
    private ImageView iv_commodity_shopinfo_pop_img;
    private Button btn_commodity_shopinfo_pop_gwc;
    private LinearLayout ll_commodity_shopinfo_pop_ljgm;
    private TextView tv_commodity_shopinfo_popstr;
    private ImageView iv_commodity_shopinfo_pop_close;
    private TextView tv_commodity_shopinfo_pop_shopkucun;
    private TextView tv_commodity_shopinfo_pop_shopprice;
    private TextView tv_commodity_shopinfo_pop_shopgg;
    private MyGridView mgv_commodity_shopinfo_pop_shopgg;
    private ImageView iv_commodity_shopinfo_pop_shopjian;
    private ExtendedEditText et_commodity_shopinfo_pop_shopnum;
    private ImageView iv_commodity_shopinfo_pop_shopjia;
    private RelativeLayout rl_commodity_shopinfo_pop_root;
    private PopupWindow pWinGwcStr;
    private PopupWindow pWinQuanYi;
    private RelativeLayout rl_header_commodityselect_gg;
    private int page = 1;
    private LinearLayout ll_header_commodity_recommend;
    private ImageView img_scroll_top;
    private boolean isGridScroll;
    private int orderCount;
    private ListView lv_header_commodity;
    private boolean bool;
    private LinearLayout ll_header_commodity_top;
    private int vipCanNum;
    private boolean etHasFocus;
    private int isTop;
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private Timer timer;
    private LinearLayout ll_shopmallorder_nonet;
    private Button btn_shopmallorder_nonet;
    private RelativeLayout rl_commodity_bottom;
    private ImageView iv_header_commodity_yinying;
    private ImageView iv_commodity_shopinfo_pop_img_anim;
    private RelativeLayout rl_commodity_shopinfo_pop_img;
    private View vw_header_commodity_rxph;
    private TextView tv_commodity_bottom_btnstr;
    private TextView tv_commodity_shopinfo_pop_ej;
    private int mallCommodityStatus;
    private String mallCommodityStatus1Str;
    private String mallCommodityStatus3Str;
    private RelativeLayout rl_commodity_black;
    private static final int MIN_OFFSET_VALUE = 20;
    private GestureDetector mGestureDetector;
    private String mallCanH5Url;
    private LinearLayout ll_shoppingmallfrag_recharge;
    private TextView tv_shoppingmallfrag_recharge_title;
    private HorizontalScrollView hsv_shoppingmallfrag_recharge;
    private GridView gv_shoppingmallfrag_recharge;
    private List<RechargeBanner> listRechargeBanner = new ArrayList<RechargeBanner>();
    private RechargeBannerAdapter<RechargeBanner> rechargeBannerAdapter;
    private ImageView iv_shoppingmallfrag_recharge_img;
    private TextView tv_header_commodity_eprice;
    private LinearLayout ll_header_commodity_root;
    private LinearLayout mFooterParent;
    private double ePrice;
    private boolean isGirdRefersh;
    private int source;
    private String shareCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
        page = 1;
        getData();
        //getrRecommendData();
        getRechargeBanner();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                bool = false;
                getData();
                getCartNum();
                getrRecommendData();
                getRechargeBanner();
            }
        }, 1500);*/
    }

    private void getRechargeBanner() {
        pDialog.showDialog();
        CommUtil.operateBanner(this, 4, operateBannerHandler);
    }

    private AsyncHttpResponseHandler operateBannerHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("title") && !jdata.isNull("title")) {
                                String title = jdata.getString("title");
                                Utils.setText(tv_shoppingmallfrag_recharge_title, title, "", View.VISIBLE, View.GONE);
                            }
                            if (jdata.has("icon") && !jdata.isNull("icon")) {
                                String icon = jdata.getString("icon");
                                GlideUtil.loadImg(CommodityDetailActivity.this, icon, iv_shoppingmallfrag_recharge_img,
                                        R.drawable.icon_production_default);
                            }
                            if (jdata.has("operateBannerList") && !jdata.isNull("operateBannerList")) {
                                JSONArray jarroperateBannerList = jdata.getJSONArray("operateBannerList");
                                if (jarroperateBannerList.length() > 0) {
                                    listRechargeBanner.clear();
                                    rechargeBannerAdapter.clearDeviceList();
                                    for (int i = 0; i < jarroperateBannerList.length(); i++) {
                                        listRechargeBanner.add(RechargeBanner.json2Entity(jarroperateBannerList
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(CommodityDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "数据异常");
            }
            if (listRechargeBanner != null && listRechargeBanner.size() > 0) {
                ll_shoppingmallfrag_recharge.setVisibility(View.VISIBLE);
                int size = listRechargeBanner.size();
                int length = 150;
                DisplayMetrics dm = new DisplayMetrics();
                CommodityDetailActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                float density = dm.density;
                int gridviewWidth = (int) (size * (length + 10) * density);
                int rechargeBannerItemWidth = (int) (length * density);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
                gv_shoppingmallfrag_recharge.setLayoutParams(params); // 重点
                gv_shoppingmallfrag_recharge.setColumnWidth(rechargeBannerItemWidth); // 重点
                gv_shoppingmallfrag_recharge.setHorizontalSpacing(25); // 间距
                gv_shoppingmallfrag_recharge.setStretchMode(GridView.NO_STRETCH);
                gv_shoppingmallfrag_recharge.setGravity(Gravity.CENTER);// 位置居中
                gv_shoppingmallfrag_recharge.setNumColumns(size); // 重点
                rechargeBannerAdapter.setData(listRechargeBanner);
            } else {
                ll_shoppingmallfrag_recharge.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "请求失败");
        }
    };

    private void getCartNum() {
        pDialog.showDialog();
        CommUtil.queryOrderAndCart(this, queryOrderAndCartHandler);
    }

    private AsyncHttpResponseHandler queryOrderAndCartHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("cartCount") && !jdata.isNull("cartCount")) {
                                userMallCartNum = jdata.getInt("cartCount");
                            }
                            if (jdata.has("orderCount") && !jdata.isNull("orderCount")) {
                                orderCount = jdata.getInt("orderCount");
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(CommodityDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "数据异常");
            }
            if (userMallCartNum > 0) {
                tv_commodity_gwcnum.setVisibility(View.VISIBLE);
                if (userMallCartNum > 99) {
                    tv_commodity_gwcnum.setText("99+");
                } else {
                    tv_commodity_gwcnum.setText(String.valueOf(userMallCartNum));
                }
            } else {
                tv_commodity_gwcnum.setVisibility(View.GONE);
            }
            if (orderCount > 0) {
                tv_commodity_ordernum.setVisibility(View.VISIBLE);
                if (orderCount > 99) {
                    tv_commodity_ordernum.setText("99+");
                } else {
                    tv_commodity_ordernum.setText(String.valueOf(orderCount));
                }
            } else {
                tv_commodity_ordernum.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "请求失败");
        }
    };

    private void getrRecommendData() {
        if (page == 1) {
            listRecommend.clear();
            shopMallRecommendAdapter.clearDeviceList();
        }
        pDialog.showDialog();
        CommUtil.getrRecommendData(this, 1, page, commodityId, getrRecommendDataHandler);
    }


    private AsyncHttpResponseHandler getrRecommendDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            ptrhgv_commodity.onRefreshComplete();
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("commodityList") && !jdata.isNull("commodityList")) {
                                JSONArray jarrcommodityList = jdata.getJSONArray("commodityList");
                                if (jarrcommodityList.length() > 0) {
                                    if (page == 1) {
                                        listRecommend.clear();
                                        shopMallRecommendAdapter.clearDeviceList();
                                    }
                                    for (int i = 0; i < jarrcommodityList.length(); i++) {
                                        listRecommend.add(ShopMallFragRecommend.json2Entity(jarrcommodityList
                                                .getJSONObject(i)));
                                    }
                                } else {
                                    if (page > 1) {
                                        ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "没有更多数据了");
                                    }
                                }
                            } else {
                                if (page > 1) {
                                    ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "没有更多数据了");
                                }
                            }
                        } else {
                            if (page > 1) {
                                ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "没有更多数据了");
                            }
                        }
                    } else {
                        if (page > 1) {
                            ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "没有更多数据了");
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(CommodityDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "数据异常");
            }
            WiteData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ptrhgv_commodity.onRefreshComplete();
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "请求失败");
        }
    };

    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        bool = false;
        isGirdRefersh = false;
        api = WXAPIFactory.createWXAPI(mContext, Global.APP_ID);
        //MApplication.listAppoint.add(this);
        MApplication.listAppoint1.add(this);
        //让布局向上移来显示软键盘
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pDialog = new MProgressDialog(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
        Intent intent = getIntent();
        commodityId = intent.getIntExtra("commodityId", 0);
        source = intent.getIntExtra("source",-1);
    }

    private void findView() {
        setContentView(R.layout.activity_commodity_detail);
        rl_commodity_black = (RelativeLayout) findViewById(R.id.rl_commodity_black);
        iv_commodity_shopinfo_pop_img_anim = (ImageView) findViewById(R.id.iv_commodity_shopinfo_pop_img_anim);
        rl_commodity_bottom = (RelativeLayout) findViewById(R.id.rl_commodity_bottom);
        ll_shopmallorder_nonet = (LinearLayout) findViewById(R.id.ll_shopmallorder_nonet);
        btn_shopmallorder_nonet = (Button) findViewById(R.id.btn_shopmallorder_nonet);
        activityRootView = findViewById(R.id.rl_commodity_root);
        tv_commodity_ordernum = (TextView) findViewById(R.id.tv_commodity_ordernum);
        img_scroll_top = (ImageView) findViewById(R.id.img_scroll_top);
        rl_commodity_gwc = (RelativeLayout) findViewById(R.id.rl_commodity_gwc);
        tv_commodity_gwcnum = (TextView) findViewById(R.id.tv_commodity_gwcnum);
        rl_commodity_order = (RelativeLayout) findViewById(R.id.rl_commodity_order);
        ib_commodity_back = (ImageButton) findViewById(R.id.ib_commodity_back);
        ll_commodity_left = (LinearLayout) findViewById(R.id.ll_commodity_left);
        vw_commodity_left = (View) findViewById(R.id.vw_commodity_left);
        ll_commodity_right = (LinearLayout) findViewById(R.id.ll_commodity_right);
        vw_commodity_right = (View) findViewById(R.id.vw_commodity_right);
        rl_commodity_bottom_submit = (LinearLayout) findViewById(R.id.rl_commodity_bottom_submit);
        tv_commodity_bottom_guantounum = (TextView) findViewById(R.id.tv_commodity_bottom_guantounum);
        tv_commodity_bottom_kefu = (LinearLayout) findViewById(R.id.tv_commodity_bottom_kefu);
        tv_commodity_bottom_share = (LinearLayout) findViewById(R.id.tv_commodity_bottom_share);
        ptrhgv_commodity = (PullToRefreshHeadGridView) findViewById(R.id.ptrhgv_commodity);
        tv_commodity_bottom_btnstr = (TextView) findViewById(R.id.tv_commodity_bottom_btnstr);
        header = LayoutInflater.from(this).inflate(
                R.layout.header_commodity_detail, null);
        vw_header_commodity_rxph = (View) header.findViewById(R.id.vw_header_commodity_rxph);
        ll_header_commodity_top = (LinearLayout) header.findViewById(R.id.ll_header_commodity_top);
        iv_header_commodity_yinying = (ImageView) header.findViewById(R.id.iv_header_commodity_yinying);
        tv_header_commodity_eprice = header.findViewById(R.id.tv_header_commodity_eprice);
        lv_header_commodity = (ListView) header.findViewById(R.id.lv_header_commodity);
        rpv_header_commodity = (Banner) header.findViewById(R.id.rpv_header_commodity);
        tv_header_commodity_xianjia = (TextView) header.findViewById(R.id.tv_header_commodity_agoprice);
        tv_header_commodity_yuanjia = (TextView) header.findViewById(R.id.tv_header_commodity_yuanjia);
        tv_header_commodity_guantou = (TextView) header.findViewById(R.id.tv_header_commodity_guantou);
        tv_header_commodity_kucun = (TextView) header.findViewById(R.id.tv_header_commodity_kucun);
        iv_header_commodity_quanyi = (ImageView) header.findViewById(R.id.iv_header_commodity_quanyi);
        tv_header_commodityselect = (TextView) header.findViewById(R.id.tv_header_commodityselect);
        hsv_header_commodity = (HorizontalScrollView) header.findViewById(R.id.hsv_header_commodity);
        gv_header_commodity = (GridView) header.findViewById(R.id.gv_header_commodity);
        rv_header_commodity_img = (RecyclerView) header.findViewById(R.id.rv_header_commodity_img);
        rl_header_commodity_rpv = (RelativeLayout) header.findViewById(R.id.rl_header_commodity_rpv);
        tv_header_commodity_title = (TagTextView) header.findViewById(R.id.tv_header_commodity_title);
        tv_header_commodity_futitle = (TextView) header.findViewById(R.id.tv_header_commodity_futitle);
        ll_header_commodity_rxph = (LinearLayout) header.findViewById(R.id.ll_header_commodity_rxph);
        ll_header_commodity_detailimg = (LinearLayout) header.findViewById(R.id.ll_header_commodity_detailimg);
        rl_header_commodityselect_gg = (RelativeLayout) header.findViewById(R.id.rl_header_commodityselect_gg);
        ll_header_commodity_recommend = (LinearLayout) header.findViewById(R.id.ll_header_commodity_recommend);
        ll_shoppingmallfrag_recharge = (LinearLayout) header.findViewById(R.id.ll_shoppingmallfrag_recharge);
        hsv_shoppingmallfrag_recharge = (HorizontalScrollView) header.findViewById(R.id.hsv_shoppingmallfrag_recharge);
        gv_shoppingmallfrag_recharge = (GridView) header.findViewById(R.id.gv_shoppingmallfrag_recharge);
        tv_shoppingmallfrag_recharge_title = (TextView) header.findViewById(R.id.tv_shoppingmallfrag_recharge_title);
        iv_shoppingmallfrag_recharge_img = (ImageView) header.findViewById(R.id.iv_shoppingmallfrag_recharge_img);
        ll_header_commodity_root = (LinearLayout) header.findViewById(R.id.ll_header_commodity_root);

        customView = (ViewGroup) View.inflate(this, R.layout.comoditydetail_shopinfo_pop, null);
        tv_commodity_shopinfo_pop_shopggstr = (TextView) customView.findViewById(R.id.tv_commodity_shopinfo_pop_shopggstr);
        iv_commodity_shopinfo_pop_img = (ImageView) customView.findViewById(R.id.iv_commodity_shopinfo_pop_img);
        btn_commodity_shopinfo_pop_gwc = (Button) customView.findViewById(R.id.btn_commodity_shopinfo_pop_gwc);
        ll_commodity_shopinfo_pop_ljgm = (LinearLayout) customView.findViewById(R.id.ll_commodity_shopinfo_pop_ljgm);
        tv_commodity_shopinfo_popstr = (TextView) customView.findViewById(R.id.tv_commodity_shopinfo_popstr);
        tv_commodity_shopinfo_pop_ej = (TextView) customView.findViewById(R.id.tv_commodity_shopinfo_pop_ej);
        iv_commodity_shopinfo_pop_close = (ImageView) customView.findViewById(R.id.iv_commodity_shopinfo_pop_close);
        tv_commodity_shopinfo_pop_shopprice = (TextView) customView.findViewById(R.id.tv_commodity_shopinfo_pop_shopprice);
        tv_commodity_shopinfo_pop_shopgg = (TextView) customView.findViewById(R.id.tv_commodity_shopinfo_pop_shopgg);
        tv_commodity_shopinfo_pop_shopkucun = (TextView) customView.findViewById(R.id.tv_commodity_shopinfo_pop_shopkucun);
        mgv_commodity_shopinfo_pop_shopgg = (MyGridView) customView.findViewById(R.id.mgv_commodity_shopinfo_pop_shopgg);
        iv_commodity_shopinfo_pop_shopjian = (ImageView) customView.findViewById(R.id.iv_commodity_shopinfo_pop_shopjian);
        et_commodity_shopinfo_pop_shopnum = (ExtendedEditText) customView.findViewById(R.id.et_commodity_shopinfo_pop_shopnum);
        iv_commodity_shopinfo_pop_shopjia = (ImageView) customView.findViewById(R.id.iv_commodity_shopinfo_pop_shopjia);
        rl_commodity_shopinfo_pop_root = (RelativeLayout) customView.findViewById(R.id.rl_commodity_shopinfo_pop_root);
        rl_commodity_shopinfo_pop_img = (RelativeLayout) customView.findViewById(R.id.rl_commodity_shopinfo_pop_img);
    }

    private void setView() {
        //mDirectionControlView.bringToFront();
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        tv_commodity_ordernum.bringToFront();
        tv_commodity_gwcnum.bringToFront();
        iv_header_commodity_yinying.bringToFront();

        mFooterParent = new LinearLayout(mContext);
        mFooterParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mFooterParent.addView(header);//在footer的最外面再套一层LinearLayout（即footerParent）


        ptrhgv_commodity.getRefreshableView().addHeaderView(mFooterParent);
        ptrhgv_commodity.setMode(PullToRefreshBase.Mode.BOTH);

        listRecommend.clear();
        shopMallRecommendAdapter = new ShopMallRecommendAdapter<ShopMallFragRecommend>(this, listRecommend);
        shopMallRecommendAdapter.clearDeviceList();
        ptrhgv_commodity.setAdapter(shopMallRecommendAdapter);

        listCommodityHotSale.clear();
        commodityHotSaleAdapter = new CommodityHotSaleAdapter<CommodityHotSale>(this, listCommodityHotSale);
        commodityHotSaleAdapter.clearDeviceList();
        gv_header_commodity.setAdapter(commodityHotSaleAdapter);

        listCommodityDetailImg.clear();
        rv_header_commodity_img.setHasFixedSize(true);
        rv_header_commodity_img.setNestedScrollingEnabled(false);
        NoScollSyLinearLayoutManager noScollFullLinearLayoutManager = new NoScollSyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rv_header_commodity_img.setLayoutManager(noScollFullLinearLayoutManager);
        commodityDetailImgAdapter = new CommodityDetailImgAdapter(R.layout.item_commodity_detailimg, listCommodityDetailImg, this);
        rv_header_commodity_img.setAdapter(commodityDetailImgAdapter);

        listRechargeBanner.clear();
        rechargeBannerAdapter = new RechargeBannerAdapter<RechargeBanner>(this, listRechargeBanner);
        rechargeBannerAdapter.clearDeviceList();
        gv_shoppingmallfrag_recharge.setAdapter(rechargeBannerAdapter);

        mallCommodityGroupCommodityPopSpecificationsAdapter = new CommodityPopSpecificationsAdapter<>(CommodityDetailActivity.this, listMallCommodityGroup);
        mgv_commodity_shopinfo_pop_shopgg.setAdapter(mallCommodityGroupCommodityPopSpecificationsAdapter);
    }

    private void WiteData() {
        tv_commodity_bottom_guantounum.setVisibility(View.GONE);
        if (stock <= 0) {
            tv_commodity_bottom_btnstr.setText(mallCommodityStatus1Str);
            tv_commodity_bottom_guantounum.setVisibility(View.GONE);
            rl_commodity_bottom_submit.setBackgroundResource(R.drawable.shopmall_noshop);
        } else if (mallCommodityStatus == 3) {//商品已下架
            tv_commodity_bottom_btnstr.setText(mallCommodityStatus3Str);
            tv_commodity_bottom_guantounum.setVisibility(View.GONE);
            rl_commodity_bottom_submit.setBackgroundResource(R.drawable.shopmall_noshop);
        } else {
            tv_commodity_bottom_btnstr.setText("加入购物车");
            rl_commodity_bottom_submit.setBackgroundResource(R.drawable.bg_red_jianbian);
        }
        if (listCommodityMarquee.size() > 0) {
            lv_header_commodity.bringToFront();
            lv_header_commodity.setVisibility(View.VISIBLE);
            if (timer == null) {
                timer = new Timer();
            }
            timer.schedule(new TimeTaskScroll(this, lv_header_commodity, listCommodityMarquee), 200, 20);
        } else {
            lv_header_commodity.setVisibility(View.GONE);
        }
        if (listBanner.size() > 0) {
            rpv_header_commodity.setVisibility(View.VISIBLE);
            rl_header_commodity_rpv.setVisibility(View.VISIBLE);
            rpv_header_commodity.setImages(listBanner)
                    .setImageLoader(new GlideImageLoader())
                    .start();
        } else {
            rpv_header_commodity.setVisibility(View.GONE);
            rl_header_commodity_rpv.setVisibility(View.GONE);
        }
        if (Utils.isStrNull(marketingTag)) {
            tv_header_commodity_title.setSingleTagAndContent(marketingTag, title);
        } else {
            tv_header_commodity_title.setText(title);
        }
        Utils.setText(tv_header_commodity_futitle, subtitle, "", View.VISIBLE, View.GONE);

        if (retailPrice > 0) {
            tv_header_commodity_xianjia.setVisibility(View.VISIBLE);
            String text = "";
            if (Utils.isDoubleEndWithZero(retailPrice)) {
                text = "¥" + Utils.formatDouble(retailPrice) + "市场价";
            } else {
                text = "¥" + retailPrice + "市场价";
            }
            tv_header_commodity_xianjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tv_header_commodity_xianjia.setText(text);
        } else {
            tv_header_commodity_xianjia.setVisibility(View.GONE);
        }
        String ePricetext;
        if (Utils.isDoubleEndWithZero(ePrice)) {
            ePricetext = "¥" + Utils.formatDouble(ePrice);
        } else {
            ePricetext = "¥" + ePrice;
        }
        SpannableString ss = new SpannableString(ePricetext);
        ss.setSpan(new TextAppearanceSpan(this, R.style.tensp), 0,
                1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv_header_commodity_eprice.setText(ss);
        Utils.setText(tv_header_commodity_kucun, "库存 " + stock, "",
                View.GONE, View.GONE);

        if (Utils.isStrNull(guaranteeSmallPic)) {
            iv_header_commodity_quanyi.setVisibility(View.VISIBLE);
            GlideUtil.loadImg(this, guaranteeSmallPic,
                    iv_header_commodity_quanyi,
                    R.drawable.icon_production_default);
        } else {
            iv_header_commodity_quanyi.setVisibility(View.GONE);
        }

        if (Utils.isStrNull(specName)) {
            tv_header_commodityselect.setVisibility(View.VISIBLE);
            if (shopMallNum > 0) {
                Utils.setText(tv_header_commodityselect, "已选  " + specName + "  " + shopMallNum + "件", "",
                        View.VISIBLE, View.GONE);
            } else {
                Utils.setText(tv_header_commodityselect, specName, "",
                        View.VISIBLE, View.GONE);
            }
        } else {
            tv_header_commodityselect.setVisibility(View.GONE);
        }

        if (listCommodityHotSale.size() > 0) {
            vw_header_commodity_rxph.setVisibility(View.GONE);
            ll_header_commodity_rxph.setVisibility(View.VISIBLE);
            int size = listCommodityHotSale.size();
            int length = 105;
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int gridviewWidth = (int) (size * (length + 10) * density);
            itemWidth = (int) (length * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
            gv_header_commodity.setLayoutParams(params); // 重点
            gv_header_commodity.setColumnWidth(itemWidth); // 重点
            gv_header_commodity.setHorizontalSpacing(25); // 间距
            gv_header_commodity.setStretchMode(GridView.NO_STRETCH);
            gv_header_commodity.setGravity(Gravity.CENTER);// 位置居中
            gv_header_commodity.setNumColumns(size); // 重点
            commodityHotSaleAdapter.setData(listCommodityHotSale);
        } else {
            vw_header_commodity_rxph.setVisibility(View.VISIBLE);
            ll_header_commodity_rxph.setVisibility(View.GONE);
        }
        setInfoPopData();
        if (listRecommend != null && listRecommend.size() > 0) {
            ll_header_commodity_recommend.setVisibility(View.VISIBLE);
            if (listRecommend.size() == 1) {
                ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(0);
                if (shopMallFragRecommend != null) {
                    if (shopMallFragRecommend.isOther()) {
                        ll_header_commodity_recommend.setVisibility(View.GONE);
                    }
                }
            }
            shopMallRecommendAdapter.setData(listRecommend);
        } else {
            if (page == 1) {
                listRecommend.add(new ShopMallFragRecommend("追加", 0, 0, "", 0, 0, true));
                shopMallRecommendAdapter.setData(listRecommend);
            }
            ll_header_commodity_recommend.setVisibility(View.GONE);
        }
    }

    private void showGwcPop() {
        pWinGwcStr = null;
        if (pWinGwcStr == null) {
            View view = (View) View.inflate(this, R.layout.mainfrag_pop, null);
            TextView tv_mainfrag_pop = (TextView) view.findViewById(R.id.tv_mainfrag_pop);
            tv_mainfrag_pop.setText(mallCommodityCartTitle);
            pWinGwcStr = new PopupWindow(view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //pWinGwcStr.setFocusable(true);// 取得焦点
            //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
            pWinGwcStr.setBackgroundDrawable(new BitmapDrawable());
            //点击外部消失
            pWinGwcStr.setOutsideTouchable(false);
            //进入退出的动画
            pWinGwcStr.setAnimationStyle(R.style.popwin_anim_style);
            //pWinGwcStr.setWidth(Utils.getDisplayMetrics(this)[0]);
            //pWinGwcStr.showAsDropDown(view,0,0);
            pWinGwcStr.showAtLocation(view, Gravity.RIGHT, 0, -900);
        }
    }

    private void setLinster() {
        /*mGestureDetector = new GestureDetector(this);
        mGestureDetector.setOnDoubleTapListener(this);*/
        tv_header_commodity_guantou.setOnClickListener(this);
        btn_shopmallorder_nonet.setOnClickListener(this);
        img_scroll_top.setOnClickListener(this);
        iv_header_commodity_quanyi.setOnClickListener(this);
        rl_header_commodityselect_gg.setOnClickListener(this);
        rl_commodity_gwc.setOnClickListener(this);
        rl_commodity_order.setOnClickListener(this);
        ib_commodity_back.setOnClickListener(this);
        ll_commodity_left.setOnClickListener(this);
        ll_commodity_right.setOnClickListener(this);
        rl_commodity_bottom_submit.setOnClickListener(this);
        tv_commodity_bottom_kefu.setOnClickListener(this);
        tv_commodity_bottom_share.setOnClickListener(this);
        ptrhgv_commodity
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshBase refreshView) {
                        PullToRefreshBase.Mode mode = refreshView
                                .getCurrentMode();
                        if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {// 下拉刷新
                            page = 1;
                            bool = false;
                            getData();
                            getCartNum();
                            getrRecommendData();
                            getRechargeBanner();
                        } else {// 上拉加载更多
                            page++;
                            getrRecommendData();
                        }
                    }
                });
        ptrhgv_commodity.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 滚动状态
                        int[] location = new int[2];
                        ll_header_commodity_detailimg.getLocationOnScreen(location);
                        Log.e("TAG", "location[0] = " + location[0]);
                        Log.e("TAG", "location[1] = " + location[1]);
                        if (location[1] <= 300) {
                            setLeftOrRight(2);
                        } else {
                            setLeftOrRight(1);
                        }
                        if (ll_header_commodity_top.getVisibility() == View.GONE) {
                            ll_header_commodity_top.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                            ll_header_commodity_top.setVisibility(View.VISIBLE);
                            boolean boole = false;
                            for (int i = 0; i < listRecommend.size(); i++) {
                                ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(i);
                                if (shopMallFragRecommend != null && shopMallFragRecommend.isAdd()) {
                                    boole = true;
                                    listRecommend.remove(i);
                                }
                            }
                            if (boole) {
                                shopMallRecommendAdapter.setData(listRecommend);
                            }
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸后滚动
                        int[] location1 = new int[2];
                        ll_header_commodity_detailimg.getLocationOnScreen(location1);
                        Log.e("TAG", "location1[0] = " + location1[0]);
                        Log.e("TAG", "location1[1] = " + location1[1]);
                        if (location1[1] <= 300) {
                            setLeftOrRight(2);
                        } else {
                            setLeftOrRight(1);
                        }

                        /*if(ll_header_commodity_top.isShown() && ll_header_commodity_top.isEnabled() && ll_header_commodity_top.isClickable() ){
                            setLeftOrRight(1);
                        }else{
                            setLeftOrRight(2);
                        }*/
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((visibleItemCount + firstVisibleItem == totalItemCount) || firstVisibleItem < 0 || firstVisibleItem > 10) {
                    isGridScroll = true;
                    img_scroll_top.setVisibility(View.VISIBLE);
                    img_scroll_top.bringToFront();
                    if (ll_header_commodity_top.getVisibility() == View.GONE) {
                        ll_header_commodity_top.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                        ll_header_commodity_top.setVisibility(View.VISIBLE);
                        boolean boole = false;
                        for (int i = 0; i < listRecommend.size(); i++) {
                            ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(i);
                            if (shopMallFragRecommend != null && shopMallFragRecommend.isAdd()) {
                                boole = true;
                                listRecommend.remove(i);
                            }
                        }
                        if (boole) {
                            shopMallRecommendAdapter.setData(listRecommend);
                        }
                    }
                } else {
                    isGridScroll = false;
                    img_scroll_top.setVisibility(View.GONE);
                }
            }
        });
        /*ptrhgv_commodity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int touchEvent = event.getAction();
                switch (touchEvent) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("TAG","按下");
                        if(isTop == 2 && ll_header_commodity_top.getVisibility() == View.GONE){
                            ll_header_commodity_top.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                            ll_header_commodity_top.setVisibility(View.VISIBLE);
                            boolean boole = false;
                            for (int i = 0; i < listRecommend.size(); i++) {
                                ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(i);
                                if (shopMallFragRecommend != null && shopMallFragRecommend.isAdd()) {
                                    boole = true;
                                    listRecommend.remove(i);
                                }
                            }
                            if (boole) {
                                shopMallRecommendAdapter.setData(listRecommend);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });*/
    }

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int touchEvent = ev.getAction();
        switch (touchEvent) {
            case MotionEvent.ACTION_DOWN:
                //当手指按下的时候
                x1 = ev.getX();
                y1 = ev.getY();
                Log.e("TAG", "按下");

                break;
            case MotionEvent.ACTION_UP:

                //当手指离开的时候
                x2 = ev.getX();
                y2 = ev.getY();
                if (y1 - y2 > 10) {
                    Log.e("TAG", "上滑");
                } else if (y2 - y1 > 10) {
                    Log.e("TAG", "下滑");
                    if (ll_header_commodity_top.getVisibility() == View.GONE) {
                        setLeftOrRight(1);
                        ll_header_commodity_top.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                        ll_header_commodity_top.setVisibility(View.VISIBLE);
                        boolean boole = false;
                        for (int i = 0; i < listRecommend.size(); i++) {
                            ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(i);
                            if (shopMallFragRecommend != null && shopMallFragRecommend.isAdd()) {
                                boole = true;
                                listRecommend.remove(i);
                            }
                        }
                        if (boole) {
                            shopMallRecommendAdapter.setData(listRecommend);
                        }
                    }
                    /*
                    int[] location = new int[2];
                    ll_header_commodity_detailimg.getLocationOnScreen(location);
                    Log.e("TAG", "location[0] = " + location[0]);
                    Log.e("TAG", "location[1] = " + location[1]);
                    if (location[1] <= 300) {
                        setLeftOrRight(2);
                    } else {
                        setLeftOrRight(1);
                    }*/
                } else if (x1 - x2 > 10) {
                    Log.e("TAG", "左滑");
                } else if (x2 - x1 > 10) {
                    Log.e("TAG", "右滑");
                }


                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void getData() {
        iv_commodity_shopinfo_pop_img_anim.setVisibility(View.GONE);
        if (!isGirdRefersh) {
            listMallCommodityGroup.clear();
        }
        listCommodityMarquee.clear();
        listBanner.clear();
        listCommodityHotSale.clear();
        commodityHotSaleAdapter.clearDeviceList();
        listCommodityDetailImg.clear();
        pDialog.showDialog();
        CommUtil.commodityInfo(mContext, commodityId, commodityInfoHandler);
    }

    private AsyncHttpResponseHandler commodityInfoHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            rl_commodity_bottom.setVisibility(View.VISIBLE);
            ll_shopmallorder_nonet.setVisibility(View.GONE);
            img_scroll_top.setVisibility(View.GONE);
            ptrhgv_commodity.setVisibility(View.VISIBLE);
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata != null) {
                        if (jdata.has("cityId") && !jdata.isNull("cityId")) {
                            spUtil.saveInt("cityId",
                                    jdata.getInt("cityId"));
                        }
                        if (!isGirdRefersh) {
                            if (jdata.has("mallCommodityGroup") && !jdata.isNull("mallCommodityGroup")) {
                                JSONArray jarrmallCommodityGroup = jdata.getJSONArray("mallCommodityGroup");
                                if (jarrmallCommodityGroup.length() > 0) {
                                    listMallCommodityGroup.clear();
                                    for (int i = 0; i < jarrmallCommodityGroup.length(); i++) {
                                        listMallCommodityGroup.add(MallCommodityGroup.json2Entity(jarrmallCommodityGroup
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                        if (jdata.has("banner") && !jdata.isNull("banner")) {
                            JSONArray jarrbanner = jdata.getJSONArray("banner");
                            if (jarrbanner.length() > 0) {
                                listBanner.clear();
                                for (int i = 0; i < jarrbanner.length(); i++) {
                                    listBanner.add(jarrbanner
                                            .getString(i));
                                }
                            }
                        }
                        if (jdata.has("realTimeCommodityInfo") && !jdata.isNull("realTimeCommodityInfo")) {
                            JSONArray jarrrealTimeCommodityInfo = jdata.getJSONArray("realTimeCommodityInfo");
                            if (jarrrealTimeCommodityInfo.length() > 0) {
                                listCommodityMarquee.clear();
                                for (int i = 0; i < jarrrealTimeCommodityInfo.length(); i++) {
                                    listCommodityMarquee.add(CommodityMarquee.json2Entity(jarrrealTimeCommodityInfo
                                            .getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("marketingTag") && !jdata.isNull("marketingTag")) {
                            marketingTag = jdata.getString("marketingTag");
                        }
                        if (jdata.has("thumbnail") && !jdata.isNull("thumbnail")) {
                            shareImg = jdata.getString("thumbnail");
                        }
                        if (jdata.has("title") && !jdata.isNull("title")) {
                            title = jdata.getString("title");
                        }
                        if (jdata.has("mallShareH5title") && !jdata.isNull("mallShareH5title")) {
                            sharetitle = jdata.getString("mallShareH5title");
                        }
                        if (jdata.has("subtitle") && !jdata.isNull("subtitle")) {
                            subtitle = jdata.getString("subtitle");
                        }
                        if (jdata.has("marketValue") && !jdata.isNull("marketValue")) {
                            marketValue = jdata.getDouble("marketValue");
                        }
                        if (jdata.has("retailPrice") && !jdata.isNull("retailPrice")) {
                            retailPrice = jdata.getDouble("retailPrice");
                        }
                        if (jdata.has("mallCommodityVipCanTitle") && !jdata.isNull("mallCommodityVipCanTitle")) {
                            mallCommodityVipCanTitle = jdata.getString("mallCommodityVipCanTitle");
                        }
                        if (jdata.has("stock") && !jdata.isNull("stock")) {
                            stock = jdata.getInt("stock");
                        }
                        if (jdata.has("guaranteeSmallPic") && !jdata.isNull("guaranteeSmallPic")) {
                            guaranteeSmallPic = jdata.getString("guaranteeSmallPic");
                        }
                        if (jdata.has("specName") && !jdata.isNull("specName")) {
                            specName = jdata.getString("specName");
                        }
                        if (jdata.has("mallCommodityHot") && !jdata.isNull("mallCommodityHot")) {
                            JSONArray jarrmallCommodityHot = jdata.getJSONArray("mallCommodityHot");
                            if (jarrmallCommodityHot.length() > 0) {
                                listCommodityHotSale.clear();
                                commodityHotSaleAdapter.clearDeviceList();
                                for (int i = 0; i < jarrmallCommodityHot.length(); i++) {
                                    listCommodityHotSale.add(CommodityHotSale.json2Entity(jarrmallCommodityHot
                                            .getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("introPic") && !jdata.isNull("introPic")) {
                            JSONArray jarrintroPic = jdata.getJSONArray("introPic");
                            if (jarrintroPic.length() > 0) {
                                listCommodityDetailImg.clear();
                                for (int i = 0; i < jarrintroPic.length(); i++) {
                                    listCommodityDetailImg.add(jarrintroPic
                                            .getString(i));
                                }
                            }
                        }
                        if (jdata.has("guaranteeBigPic") && !jdata.isNull("guaranteeBigPic")) {
                            guaranteeBigPic = jdata.getString("guaranteeBigPic");
                        }
                        if (jdata.has("mallKfUrl") && !jdata.isNull("mallKfUrl")) {
                            mallKfUrl = jdata.getString("mallKfUrl");
                        }
                        if (jdata.has("ePrice") && !jdata.isNull("ePrice")) {
                            ePrice = jdata.getDouble("ePrice");
                        }
                        if (jdata.has("mallCommodityVipCanTitle1") && !jdata.isNull("mallCommodityVipCanTitle1")) {
                            mallCommodityVipCanTitle1 = jdata.getString("mallCommodityVipCanTitle1");
                        }
                        if (jdata.has("mallCommodityCartTitle") && !jdata.isNull("mallCommodityCartTitle")) {
                            mallCommodityCartTitle = jdata.getString("mallCommodityCartTitle");
                        }
                        if (jdata.has("mallShareUrl") && !jdata.isNull("mallShareUrl")) {
                            shareurl = jdata.getString("mallShareUrl");
                        }
                        if (jdata.has("mallShareH5Context") && !jdata.isNull("mallShareH5Context")) {
                            sharetxt = jdata.getString("mallShareH5Context");
                        }
                        if (jdata.has("vipCanNum") && !jdata.isNull("vipCanNum")) {
                            vipCanNum = jdata.getInt("vipCanNum");
                        }
                        if (jdata.has("mallCommodityStatus") && !jdata.isNull("mallCommodityStatus")) {
                            mallCommodityStatus = jdata.getInt("mallCommodityStatus");
                        }
                        if (jdata.has("mallCommodityStatus1Str") && !jdata.isNull("mallCommodityStatus1Str")) {
                            mallCommodityStatus1Str = jdata.getString("mallCommodityStatus1Str");
                        }
                        if (jdata.has("mallCommodityStatus3Str") && !jdata.isNull("mallCommodityStatus3Str")) {
                            mallCommodityStatus3Str = jdata.getString("mallCommodityStatus3Str");
                        }
                        if (jdata.has("mallCanH5Url") && !jdata.isNull("mallCanH5Url")) {
                            mallCanH5Url = jdata.getString("mallCanH5Url");
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(CommodityDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "数据异常");
            }
            commodityDetailImgAdapter.notifyDataSetChanged();
            if (listCommodityDetailImg.size() > 0) {
                ll_header_commodity_detailimg.setVisibility(View.VISIBLE);
            } else {
                ll_header_commodity_detailimg.setVisibility(View.GONE);
            }
            WiteData();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getrRecommendData();
                }
            }, 1500);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "请求失败");
            ll_shopmallorder_nonet.setVisibility(View.VISIBLE);
            img_scroll_top.setVisibility(View.GONE);
            ptrhgv_commodity.setVisibility(View.GONE);
            rl_commodity_bottom.setVisibility(View.INVISIBLE);

        }
    };

    private void setLeftOrRight(int flag) {
        if (flag == 1) {//商品
            vw_commodity_left.setVisibility(View.VISIBLE);
            vw_commodity_right.setVisibility(View.GONE);
            isTop = 1;
        } else if (flag == 2) {//详情
            vw_commodity_left.setVisibility(View.GONE);
            vw_commodity_right.setVisibility(View.VISIBLE);
            isTop = 2;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_header_commodity_guantou:
                if (Utils.isStrNull(mallCanH5Url)) {
                    startActivity(new Intent(this, ADActivity.class).putExtra("url", mallCanH5Url));
                }
                break;
            case R.id.btn_shopmallorder_nonet:
                page = 1;
                getData();
                getCartNum();
                //getrRecommendData();
                getRechargeBanner();
                break;
            case R.id.img_scroll_top:
                if (isGridScroll) {
                    HeaderGridView refreshableView = ptrhgv_commodity.getRefreshableView();
                    if (!(refreshableView).isStackFromBottom()) {
                        refreshableView.setStackFromBottom(true);
                    }
                    refreshableView.setStackFromBottom(false);
                    if (isTop == 2 || isTop == 0) {
                        //header.scrollBy(0,ll_header_commodity_top.getHeight());
                        ll_header_commodity_top.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                        ll_header_commodity_top.setVisibility(View.VISIBLE);
                        refreshableView.setStackFromBottom(true);
                        refreshableView.setStackFromBottom(false);
                    }
                    boolean boole = false;
                    for (int i = 0; i < listRecommend.size(); i++) {
                        ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(i);
                        if (shopMallFragRecommend != null && shopMallFragRecommend.isAdd()) {
                            boole = true;
                            listRecommend.remove(i);
                        }
                    }
                    if (boole) {
                        shopMallRecommendAdapter.setData(listRecommend);
                    }
                    setLeftOrRight(1);
                }

                break;
            case R.id.rl_commodity_gwc:
                if (Utils.checkLogin(CommodityDetailActivity.this)) {
                    startActivity(new Intent(CommodityDetailActivity.this, ShoppingCartActivity.class));
                } else {
                    startActivityForResult(new Intent(CommodityDetailActivity.this, LoginNewActivity.class).putExtra("previous", Global.COMMODITYDETAIL_LOGIN), Global.COMMODITYDETAIL_LOGIN);
                }
                break;
            case R.id.rl_commodity_order:
                if (Utils.checkLogin(CommodityDetailActivity.this)) {
                    startActivity(new Intent(CommodityDetailActivity.this, ShopMallOrderActivity.class));
                } else {
                    startActivityForResult(new Intent(CommodityDetailActivity.this, LoginNewActivity.class).putExtra("previous", Global.COMMODITYDETAIL_LOGIN), Global.COMMODITYDETAIL_LOGIN);
                }
                break;
            case R.id.ll_commodity_left:
                /*HeaderGridView refreshableView = ptrhgv_commodity.getRefreshableView();
                refreshableView.setStackFromBottom(true);
                refreshableView.setStackFromBottom(false);*/
                if (isTop == 2 || isTop == 0) {
                    //header.scrollBy(0,ll_header_commodity_top.getHeight());
                    ll_header_commodity_top.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                    ll_header_commodity_top.setVisibility(View.VISIBLE);
                    HeaderGridView refreshableView = ptrhgv_commodity.getRefreshableView();
                    refreshableView.setStackFromBottom(true);
                    refreshableView.setStackFromBottom(false);
                }
                boolean boole = false;
                for (int i = 0; i < listRecommend.size(); i++) {
                    ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(i);
                    if (shopMallFragRecommend != null && shopMallFragRecommend.isAdd()) {
                        boole = true;
                        listRecommend.remove(i);
                    }
                }
                if (boole) {
                    shopMallRecommendAdapter.setData(listRecommend);
                }
                setLeftOrRight(1);
                break;
            case R.id.ll_commodity_right:
                if (isTop == 1 || isTop == 0) {
                    listRecommend.add(new ShopMallFragRecommend("追加", 0, 0, "", 0, 0, true, true));
                    listRecommend.add(new ShopMallFragRecommend("追加", 0, 0, "", 0, 0, true, true));
                    shopMallRecommendAdapter.setData(listRecommend);
                    //ptrhgv_commodity.setScrollY(ll_header_commodity_top.getHeight());
                    ll_header_commodity_top.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_hide));//开始动画
                    ll_header_commodity_top.setVisibility(View.GONE);
                    HeaderGridView refreshableView = ptrhgv_commodity.getRefreshableView();
                    refreshableView.setStackFromBottom(true);
                    refreshableView.setStackFromBottom(false);
                }
                setLeftOrRight(2);
                break;
            case R.id.iv_header_commodity_quanyi:
                rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                rl_commodity_black.setVisibility(View.VISIBLE);
                rl_commodity_black.bringToFront();
                showShopQuanYiPop();
                break;
            case R.id.rl_commodity_bottom_submit:
                rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                rl_commodity_black.setVisibility(View.VISIBLE);
                rl_commodity_black.bringToFront();
                switch (source){
                    case Global.SOURCE_MALLSEARCH:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallSearch_ToCart);
                        break;
                    case Global.SOURCE_MALLCLASSONE:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassOne_ToCart);
                        break;
                    case Global.SOURCE_MALLCLASSTwo:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassTwo_ToCart);
                        break;
                    case Global.SOURCE_MALLCLASSThree:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassThree_ToCart);
                        break;
                    case Global.SOURCE_MALLCLASSIFY:
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Classify_ToCart);
                        break;
                }

                showShopInfoPop();
                break;
            case R.id.rl_header_commodityselect_gg:
                rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.commodity_detail_show));//开始动画
                rl_commodity_black.setVisibility(View.VISIBLE);
                rl_commodity_black.bringToFront();
                showShopInfoPop();
                break;
            case R.id.tv_commodity_bottom_kefu:
                if (Utils.isStrNull(mallKfUrl)) {
                    startActivity(new Intent(CommodityDetailActivity.this, ADActivity.class).putExtra("url", mallKfUrl));
                }
                break;
            case R.id.tv_commodity_bottom_share:
                new Thread(networkTask).start();
                break;
            case R.id.ib_commodity_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        if (event != null) {
            getCartNum();
            getrRecommendData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.COMMODITYDETAIL_LOGIN) {
                getCartNum();
                getrRecommendData();
            }
        }
    }

    private void showShopQuanYiPop() {
        pWinQuanYi = null;
        if (pWinQuanYi == null) {
            View quanYiView = (View) View.inflate(this, R.layout.comoditydetail_shopinfoquanyi_pop, null);
            ImageView iv_commodity_shopquanyipop_close = (ImageView) quanYiView.findViewById(R.id.iv_commodity_shopquanyipop_close);
            ImageView iv_commodity_shopquanyipop_img = (ImageView) quanYiView.findViewById(R.id.iv_commodity_shopquanyipop_img);
            RelativeLayout rl_commodity_shopquanyipop_root = (RelativeLayout) quanYiView.findViewById(R.id.rl_commodity_shopquanyipop_root);
            GlideUtil.loadImg(CommodityDetailActivity.this, guaranteeBigPic, iv_commodity_shopquanyipop_img, R.drawable.icon_production_default);
            pWinQuanYi = new PopupWindow(quanYiView,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            pWinQuanYi.setFocusable(true);// 取得焦点
            //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
            pWinQuanYi.setBackgroundDrawable(new BitmapDrawable());
            //点击外部消失
            pWinQuanYi.setOutsideTouchable(true);
            //设置可以点击
            pWinQuanYi.setTouchable(true);
            //进入退出的动画
            pWinQuanYi.setAnimationStyle(R.style.mypopwindow_anim_style);
            pWinQuanYi.setWidth(Utils.getDisplayMetrics(this)[0]);
            pWinQuanYi.showAtLocation(quanYiView, Gravity.BOTTOM, 0, 0);
            rl_commodity_shopquanyipop_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinQuanYi.dismiss();
                    pWinQuanYi = null;
                }
            });
            iv_commodity_shopquanyipop_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinQuanYi.dismiss();
                    pWinQuanYi = null;
                }
            });
            pWinQuanYi.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rl_commodity_black.setVisibility(View.GONE);
                }
            });
        }
    }

    private void showShopInfoPop() {
        try {
            pWinShopInfo = null;
            if (pWinShopInfo == null) {
                pWinShopInfo = new PopupWindow(customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, true);
                pWinShopInfo.setFocusable(true);// 取得焦点
                //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
                pWinShopInfo.setBackgroundDrawable(new BitmapDrawable());
                //点击外部消失
                pWinShopInfo.setOutsideTouchable(true);
                //设置可以点击
                pWinShopInfo.setTouchable(true);
                //进入退出的动画
                pWinShopInfo.setAnimationStyle(R.style.mypopwindow_anim_style);
                pWinShopInfo.setWidth(Utils.getDisplayMetrics(this)[0]);
                pWinShopInfo.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
                pWinShopInfo.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        rl_commodity_black.setVisibility(View.GONE);
                    }
                });
                setInfoPopData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInfoPopData() {
        tv_commodity_shopinfo_popstr.setVisibility(View.GONE);
        if (stock <= 0) {
            btn_commodity_shopinfo_pop_gwc.setText(mallCommodityStatus1Str);
            ll_commodity_shopinfo_pop_ljgm.setVisibility(View.GONE);
            btn_commodity_shopinfo_pop_gwc.setBackgroundResource(R.drawable.shopmall_noshop);
        } else if (mallCommodityStatus == 3) {//商品已下架
            btn_commodity_shopinfo_pop_gwc.setText(mallCommodityStatus3Str);
            ll_commodity_shopinfo_pop_ljgm.setVisibility(View.GONE);
            btn_commodity_shopinfo_pop_gwc.setBackgroundResource(R.drawable.shopmall_noshop);
        } else {
            btn_commodity_shopinfo_pop_gwc.setText("加入购物车");
            ll_commodity_shopinfo_pop_ljgm.setVisibility(View.VISIBLE);
            btn_commodity_shopinfo_pop_gwc.setBackgroundColor(Color.parseColor("#3A3636"));
        }
        String ePricetext;
        if (Utils.isDoubleEndWithZero(ePrice)) {
            ePricetext = "¥" + Utils.formatDouble(ePrice);
        } else {
            ePricetext = "¥" + ePrice;
        }
        SpannableString ss = new SpannableString(ePricetext);
        ss.setSpan(new TextAppearanceSpan(this, R.style.tensp), 0,
                1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv_commodity_shopinfo_pop_ej.setText(ss);
        if (pWinShopInfo != null && pWinShopInfo.isShowing()) {
            rl_commodity_shopinfo_pop_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        pWinShopInfo.dismiss();
                        pWinShopInfo = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            rl_commodity_shopinfo_pop_img.bringToFront();
            iv_commodity_shopinfo_pop_close.bringToFront();
            //et_commodity_shopinfo_pop_shopnum.setFilters(new InputFilter[]{new InputFilterMinMax(shopMallNum, stock)});
            GlideUtil.loadImg(CommodityDetailActivity.this, shareImg, iv_commodity_shopinfo_pop_img, R.drawable.icon_production_default);
            GlideUtil.loadImg(CommodityDetailActivity.this, shareImg, iv_commodity_shopinfo_pop_img_anim, R.drawable.icon_production_default);
            et_commodity_shopinfo_pop_shopnum.setText("" + shopMallNum);
            if (Utils.isStrNull(et_commodity_shopinfo_pop_shopnum.getText().toString())) {
                et_commodity_shopinfo_pop_shopnum.setSelection(et_commodity_shopinfo_pop_shopnum.getText().toString().length());
            }
            /*if (vipCanNum > 0 && stock > 0 && mallCommodityStatus != 3) {
                Utils.setText(tv_commodity_shopinfo_popstr, "购买赠" + (vipCanNum * (shopMallNum > 0 ? shopMallNum : 1)) + "个罐头币", "",
                        View.VISIBLE, View.GONE);
            } else {
                tv_commodity_shopinfo_popstr.setVisibility(View.GONE);
            }*/
            if (stock <= 0 || mallCommodityStatus == 3) {
                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
            } else {
                if (shopMallNum < stock) {
                    if (shopMallNum < 1) {
                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                        iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                        iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                    } else if (shopMallNum == 1) {
                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                        iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                        iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                    } else {
                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                        iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                        iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                        iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                        iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                    }
                } else if (shopMallNum == stock) {
                    if (shopMallNum == 1) {
                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                        iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                    } else {
                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                        iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                        iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                        iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                    }
                } else {
                    iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                    iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                    iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                    iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                    iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                    iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                }
            }
            if (retailPrice > 0) {
                tv_commodity_shopinfo_pop_shopprice.setVisibility(View.VISIBLE);
                String text = "";
                if (Utils.isDoubleEndWithZero(retailPrice)) {
                    text = "¥" + Utils.formatDouble(retailPrice) + "市场价";
                } else {
                    text = "¥" + retailPrice + "市场价";
                }
                tv_commodity_shopinfo_pop_shopprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tv_commodity_shopinfo_pop_shopprice.setText(text);
            } else {
                tv_commodity_shopinfo_pop_shopprice.setVisibility(View.GONE);
            }
            Utils.setText(tv_commodity_shopinfo_pop_shopkucun, "库存 " + stock, "",
                    View.VISIBLE, View.GONE);
            if (Utils.isStrNull(specName)) {
                tv_commodity_shopinfo_pop_shopgg.setVisibility(View.VISIBLE);
                Utils.setText(tv_commodity_shopinfo_pop_shopgg, "已选  " + specName + "  " + shopMallNum + "件", "",
                        View.VISIBLE, View.GONE);
            } else {
                tv_commodity_shopinfo_pop_shopgg.setVisibility(View.GONE);
            }
            mallCommodityGroupCommodityPopSpecificationsAdapter.notifyDataSetChanged();
            if (listMallCommodityGroup.size() > 0) {
                tv_commodity_shopinfo_pop_shopggstr.setVisibility(View.VISIBLE);
                mgv_commodity_shopinfo_pop_shopgg.setVisibility(View.VISIBLE);
                mgv_commodity_shopinfo_pop_shopgg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (listMallCommodityGroup.size() > 0 && listMallCommodityGroup.size() > position) {
                            commodityId = listMallCommodityGroup.get(position).getId();
                            for (int i = 0; i < listMallCommodityGroup.size(); i++) {
                                MallCommodityGroup mallCommodityGroup = listMallCommodityGroup.get(i);
                                if (mallCommodityGroup != null) {
                                    if (i == position) {
                                        mallCommodityGroup.setFlag(1);
                                    } else {
                                        mallCommodityGroup.setFlag(0);
                                    }
                                }
                            }
                        }
                        mallCommodityGroupCommodityPopSpecificationsAdapter.notifyDataSetChanged();
                        bool = true;
                        isGirdRefersh = true;
                        getData();
                    }
                });
            } else {
                mgv_commodity_shopinfo_pop_shopgg.setVisibility(View.GONE);
                tv_commodity_shopinfo_pop_shopggstr.setVisibility(View.GONE);
            }
            iv_commodity_shopinfo_pop_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinShopInfo.dismiss();
                    pWinShopInfo = null;
                }
            });
            iv_commodity_shopinfo_pop_shopjian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopMallNum--;
                    if (stock > 0 && mallCommodityStatus != 3) {
                        if (shopMallNum < stock) {
                            if (shopMallNum < 1) {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                            } else if (shopMallNum == 1) {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                            } else {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                            }
                        } else if (shopMallNum == stock) {
                            if (shopMallNum == 1) {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                            } else {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                            }
                        } else {
                            iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                            iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                            iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                            iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                            iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                            iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                        }
                    } else {
                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                        iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                    }
                    et_commodity_shopinfo_pop_shopnum.setText("" + shopMallNum);
                    if (Utils.isStrNull(et_commodity_shopinfo_pop_shopnum.getText().toString())) {
                        et_commodity_shopinfo_pop_shopnum.setSelection(et_commodity_shopinfo_pop_shopnum.getText().toString().length());
                    }
                    if (Utils.isStrNull(specName)) {
                        tv_header_commodityselect.setVisibility(View.VISIBLE);
                        if (shopMallNum > 0) {
                            Utils.setText(tv_header_commodityselect, "已选  " + specName + "  " + shopMallNum + "件", "",
                                    View.VISIBLE, View.GONE);
                        } else {
                            Utils.setText(tv_header_commodityselect, specName, "",
                                    View.VISIBLE, View.GONE);
                        }
                    } else {
                        tv_header_commodityselect.setVisibility(View.GONE);
                    }
                    /*if (vipCanNum > 0 && stock > 0 && mallCommodityStatus != 3) {
                        Utils.setText(tv_commodity_shopinfo_popstr, "购买赠" + (vipCanNum * (shopMallNum > 0 ? shopMallNum : 1)) + "个罐头币", "",
                                View.VISIBLE, View.GONE);
                    } else {
                        tv_commodity_shopinfo_popstr.setVisibility(View.GONE);
                    }*/
                    if (Utils.isStrNull(specName)) {
                        tv_commodity_shopinfo_pop_shopgg.setVisibility(View.VISIBLE);
                        Utils.setText(tv_commodity_shopinfo_pop_shopgg, "已选  " + specName + "  " + shopMallNum + "件", "",
                                View.VISIBLE, View.GONE);
                    } else {
                        tv_commodity_shopinfo_pop_shopgg.setVisibility(View.GONE);
                    }
                }
            });
            iv_commodity_shopinfo_pop_shopjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopMallNum++;
                    if (stock > 0 && mallCommodityStatus != 3) {
                        if (shopMallNum < stock) {
                            if (shopMallNum < 1) {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                            } else if (shopMallNum == 1) {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                            } else {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                            }
                        } else if (shopMallNum == stock) {
                            if (shopMallNum == 1) {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                            } else {
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                            }
                        } else {
                            iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                            iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                            iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                            iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                            iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                            iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                        }
                    } else {
                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                        iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                    }
                    et_commodity_shopinfo_pop_shopnum.setText("" + shopMallNum);
                    if (Utils.isStrNull(et_commodity_shopinfo_pop_shopnum.getText().toString())) {
                        et_commodity_shopinfo_pop_shopnum.setSelection(et_commodity_shopinfo_pop_shopnum.getText().toString().length());
                    }
                    if (Utils.isStrNull(specName)) {
                        tv_header_commodityselect.setVisibility(View.VISIBLE);
                        if (shopMallNum > 0) {
                            Utils.setText(tv_header_commodityselect, "已选  " + specName + "  " + shopMallNum + "件", "",
                                    View.VISIBLE, View.GONE);
                        } else {
                            Utils.setText(tv_header_commodityselect, specName, "",
                                    View.VISIBLE, View.GONE);
                        }
                    } else {
                        tv_header_commodityselect.setVisibility(View.GONE);
                    }
                    /*if (vipCanNum > 0 && stock > 0 && mallCommodityStatus != 3) {
                        Utils.setText(tv_commodity_shopinfo_popstr, "购买赠" + (vipCanNum * (shopMallNum > 0 ? shopMallNum : 1)) + "个罐头币", "",
                                View.VISIBLE, View.GONE);
                    } else {
                        tv_commodity_shopinfo_popstr.setVisibility(View.GONE);
                    }*/
                    if (Utils.isStrNull(specName)) {
                        tv_commodity_shopinfo_pop_shopgg.setVisibility(View.VISIBLE);
                        Utils.setText(tv_commodity_shopinfo_pop_shopgg, "已选  " + specName + "  " + shopMallNum + "件", "",
                                View.VISIBLE, View.GONE);
                    } else {
                        tv_commodity_shopinfo_pop_shopgg.setVisibility(View.GONE);
                    }
                }
            });
            btn_commodity_shopinfo_pop_gwc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stock > 0 && mallCommodityStatus != 3) {
                        switch (source){
                            case Global.SOURCE_MALLSEARCH:
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallSearch_ToAddCart);
                                break;
                            case Global.SOURCE_MALLCLASSONE:
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassOne_AddCart);
                                break;
                            case Global.SOURCE_MALLCLASSTwo:
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassTwo_AddCart);
                                break;
                            case Global.SOURCE_MALLCLASSThree:
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassThree_AddCart);
                                break;
                            case Global.SOURCE_MALLCLASSIFY:
                                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Classify_AddCart);
                                break;
                        }
                        if (Utils.checkLogin(CommodityDetailActivity.this)) {
                            payOrCar = 0;
                            pDialog.showDialog();
                            CommUtil.addUserMallCart(mContext, commodityId + "," + shopMallNum, payOrCar, addUserMallCartHandler);
                        } else {
                            startActivityForResult(new Intent(CommodityDetailActivity.this, LoginNewActivity.class).putExtra("previous", Global.COMMODITYDETAIL_LOGIN), Global.COMMODITYDETAIL_LOGIN);
                        }
                    }
                }
            });
            ll_commodity_shopinfo_pop_ljgm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (source){
                        case Global.SOURCE_MALLSEARCH:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallSearch_ToBuy);
                            break;
                        case Global.SOURCE_MALLCLASSONE:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassOne_ToBuy);
                            break;
                        case Global.SOURCE_MALLCLASSTwo:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassTwo_ToBuy);
                            break;
                        case Global.SOURCE_MALLCLASSThree:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_ClassThree_ToBuy);
                            break;
                        case Global.SOURCE_MALLCLASSIFY:
                            UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Classify_ToBuy);
                            break;
                    }
                    if (Utils.checkLogin(CommodityDetailActivity.this)) {
                        payOrCar = 1;
                        pDialog.showDialog();
                        CommUtil.addUserMallCart(mContext, commodityId + "," + shopMallNum, payOrCar, addUserMallCartHandler);
                    } else {
                        startActivityForResult(new Intent(CommodityDetailActivity.this, LoginNewActivity.class).putExtra("previous", Global.COMMODITYDETAIL_LOGIN), Global.COMMODITYDETAIL_LOGIN);
                    }
                }
            });
            et_commodity_shopinfo_pop_shopnum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        //获得焦点才添加监听
                        et_commodity_shopinfo_pop_shopnum.addTextChangedListener(new MyTextWatch());
                    } else {
                        //失去焦点时清除监听
                        et_commodity_shopinfo_pop_shopnum.clearTextChangedListeners();
                    }
                }
            });
        }
    }

    class MyTextWatch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("TAG", "afterTextChanged");
            if (s != null) {
                if (Utils.isStrNull(s.toString())) {
                    if (Utils.isStrNull(s.toString().trim())) {
                        Pattern p = Pattern.compile("[0-9]*");
                        Matcher m = p.matcher(s.toString().trim());
                        if (m.matches()) {
                            int num = Integer.parseInt(s.toString().trim());
                            if (stock > 0 && mallCommodityStatus != 3) {
                                if (num < stock) {
                                    if (num < 1) {
                                        shopMallNum = 1;
                                        ToastUtil.showToastShortBottom(mContext, "数量不能小于1");
                                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                        iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                        iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                                    } else if (num == 1) {
                                        shopMallNum = num;
                                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                        iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                        iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                                    } else {
                                        shopMallNum = num;
                                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.pop_giftcarddetail_add);
                                        iv_commodity_shopinfo_pop_shopjia.setClickable(true);
                                        iv_commodity_shopinfo_pop_shopjia.setEnabled(true);
                                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                                        iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                                        iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                                    }
                                } else if (num == stock) {
                                    if (num == 1) {
                                        shopMallNum = num;
                                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                        iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                        iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                        iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                        iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                                    } else {
                                        shopMallNum = stock;
                                        iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                        iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                        iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                        iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                                        iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                                        iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                                    }
                                } else {
                                    shopMallNum = stock;
                                    ToastUtil.showToastShortBottom(mContext, "数量不能大于库存");
                                    iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                    iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                    iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                    iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                                    iv_commodity_shopinfo_pop_shopjian.setClickable(true);
                                    iv_commodity_shopinfo_pop_shopjian.setEnabled(true);
                                }
                            } else {
                                shopMallNum = 0;
                                ToastUtil.showToastShortBottom(mContext, "库存为0");
                                iv_commodity_shopinfo_pop_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                                iv_commodity_shopinfo_pop_shopjia.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjia.setEnabled(false);
                                iv_commodity_shopinfo_pop_shopjian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                                iv_commodity_shopinfo_pop_shopjian.setClickable(false);
                                iv_commodity_shopinfo_pop_shopjian.setEnabled(false);
                            }
                            /*if (vipCanNum > 0 && stock > 0 && mallCommodityStatus != 3) {
                                Utils.setText(tv_commodity_shopinfo_popstr, "购买赠" + (vipCanNum * (shopMallNum > 0 ? shopMallNum : 1)) + "个罐头币", "",
                                        View.VISIBLE, View.GONE);
                            } else {
                                tv_commodity_shopinfo_popstr.setVisibility(View.GONE);
                            }*/
                           /* if (vipCanNum > 0 && stock > 0 && mallCommodityStatus != 3) {
                                Utils.setText(tv_commodity_bottom_guantounum, "购买赠" + (vipCanNum * (shopMallNum > 0 ? shopMallNum : 1)) + "个罐头币", "",
                                        View.VISIBLE, View.GONE);
                            } else {
                                tv_commodity_bottom_guantounum.setVisibility(View.GONE);
                            }*/
                            if (Utils.isStrNull(specName)) {
                                tv_header_commodityselect.setVisibility(View.VISIBLE);
                                if (shopMallNum > 0) {
                                    Utils.setText(tv_header_commodityselect, "已选  " + specName + "  " + shopMallNum + "件", "",
                                            View.VISIBLE, View.GONE);
                                } else {
                                    Utils.setText(tv_header_commodityselect, specName, "",
                                            View.VISIBLE, View.GONE);
                                }
                            } else {
                                tv_header_commodityselect.setVisibility(View.GONE);
                            }
                            et_commodity_shopinfo_pop_shopnum.removeTextChangedListener(this);
                            et_commodity_shopinfo_pop_shopnum.setText(shopMallNum + "");
                            et_commodity_shopinfo_pop_shopnum.addTextChangedListener(this);
                            if (Utils.isStrNull(et_commodity_shopinfo_pop_shopnum.getText().toString())) {
                                et_commodity_shopinfo_pop_shopnum.setSelection(et_commodity_shopinfo_pop_shopnum.getText().toString().length());
                            }
                            if (Utils.isStrNull(specName)) {
                                tv_commodity_shopinfo_pop_shopgg.setVisibility(View.VISIBLE);
                                Utils.setText(tv_commodity_shopinfo_pop_shopgg, "已选  " + specName + "  " + shopMallNum + "件", "",
                                        View.VISIBLE, View.GONE);
                            } else {
                                tv_commodity_shopinfo_pop_shopgg.setVisibility(View.GONE);
                            }
                        } else {
                            ToastUtil.showToastShortBottom(mContext, "请输入数字");
                        }
                    }
                }
            }
        }
    }

    private AsyncHttpResponseHandler addUserMallCartHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (payOrCar == 0) {//加入购物车
                        iv_commodity_shopinfo_pop_img_anim.bringToFront();
                        iv_commodity_shopinfo_pop_img_anim.setVisibility(View.VISIBLE);
                        iv_commodity_shopinfo_pop_img_anim.startAnimation(AnimationUtils.loadAnimation(CommodityDetailActivity.this, R.anim.shopcartshow));//开始动画
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                iv_commodity_shopinfo_pop_img_anim.setVisibility(View.GONE);
                                ToastUtil.showToastShortCenter(CommodityDetailActivity.this, "加入购物车成功");
                                getData();
                                getCartNum();
                            }
                        }, 1200);
                    } else if (payOrCar == 1) {//立即购买
                        startActivity(new Intent(CommodityDetailActivity.this, MallOrderConfirmActivity.class).putExtra("strp", commodityId + "_" + shopMallNum).putExtra("source",source));
                    }
                    if (pWinShopInfo != null) {
                        pWinShopInfo.dismiss();
                        pWinShopInfo = null;
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(CommodityDetailActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "数据异常");
            }
            WiteData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(CommodityDetailActivity.this, "请求失败");
        }
    };

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
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bmp = (Bitmap) msg.obj;
            showShare();
        }
    };

    private void showShare() {
        final View view = LayoutInflater.from(this).inflate(R.layout.sharedialog, null);
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
        Button btn_sharedialog_cancel = (Button) view
                .findViewById(R.id.btn_sharedialog_cancel);
        if (pWin == null) {
            pWin = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
        }
        //进入退出的动画
        //pWin.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWin.setFocusable(true);
        pWin.setWidth(ScreenUtil.getScreenWidth(mContext));
        pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                pWin.dismiss();
                pWin = null;
            }
        });
        ll_sharedialog_wxfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 微信好友
                if (Utils.checkLogin(mContext)){
                    //获取邀请码
                    getShareCode();
                }else {
                    Utils.shareMiniProgram(mContext,shareurl,bmp,"pages/shopdetails/shopdetails?id="+commodityId,sharetitle,sharetxt);
                }
                pWin.dismiss();
                pWin = null;

                //setWXShareContent(1);
            }
        });
        ll_sharedialog_wxpyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 微信朋友圈
                pWin.dismiss();
                pWin = null;
                setWXShareContent(2);
            }
        });
        ll_sharedialog_qqfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// QQ好友
                pWin.dismiss();
                pWin = null;
                setWXShareContent(3);
            }
        });
        ll_sharedialog_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 新浪微博
                pWin.dismiss();
                pWin = null;
                setWXShareContent(4);
            }
        });
        btn_sharedialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 取消
                pWin.dismiss();
                pWin = null;
            }
        });
        pWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
    }


    private AsyncHttpResponseHandler commodityShareCode = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code==0){
                    JSONObject data = jobj.getJSONObject("data");
                    if (data.has("code")&&!jobj.isNull("code")){
                        shareCode = data.getString("code");
                    }
                    Utils.shareMiniProgram(mContext,shareurl,bmp,
                            "pages/shopdetails/shopdetails?id="+ commodityId+"&h="+spUtil.getInt("userid",0)+"&c="+shareCode,
                            sharetitle,sharetxt);
                }else {
                    ToastUtil.showToastShortBottom(mContext,msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(mContext,"请求失败");
        }
    };

    private void getShareCode() {
        CommUtil.commodityShare(mContext,spUtil.getPhoneSave(),commodityId,Global.getIMEI(mContext),commodityShareCode);
    }

    private void setWXShareContent(int type) {
        boolean weixinAvilible = Utils.isWeixinAvilible(mContext);
        if (shareurl.contains("?")) {
            shareurl = shareurl + "&id=" + commodityId;
        } else {
            shareurl = shareurl + "?id=" + commodityId;
        }
        if (bmp != null && shareurl != null && !TextUtils.isEmpty(shareurl)) {
            if (type == 1 || type == 2) {// 微信
                if (weixinAvilible) {
                    WXWebpageObject wxwebpage = new WXWebpageObject();
                    wxwebpage.webpageUrl = shareurl;
                    WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
                    wxmedia.title = sharetitle;
                    wxmedia.description = sharetxt;
                    Bitmap createBitmapThumbnail = Utils
                            .createBitmapThumbnail(bmp);
                    wxmedia.setThumbImage(createBitmapThumbnail);
                    wxmedia.thumbData = Util_WX.bmpToByteArray(
                            createBitmapThumbnail, true);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = wxmedia;
                    if (type == 1) {
                        req.scene = SendMessageToWX.Req.WXSceneSession;
                    } else {
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    }
                    api.sendReq(req);
                } else {
                    ToastUtil.showToastShortBottom(mContext, "微信不可用");
                }
            } else if (type == 3) {// qq
                umengShareUtils = new UmengShareUtils(mContext, sharetxt,
                        shareurl, sharetitle, shareImg);
                umengShareUtils.mController.getConfig().closeToast();
                umengShareUtils.mController.postShare(mContext, SHARE_MEDIA.QQ,
                        new SocializeListeners.SnsPostListener() {
                            @Override
                            public void onStart() {
                                /*
                                 * ToastUtil.showToastShortCenter(ADActivity.this
                                 * , "开始分享.");
                                 */
                            }

                            @Override
                            public void onComplete(SHARE_MEDIA arg0, int eCode,
                                                   SocializeEntity arg2) {
                                if (eCode == 200) {
                                    /*
                                     * ToastUtil.showToastShortCenter(
                                     * ADActivity.this, "分享成功.");
                                     */
                                } else {
                                    String eMsg = "";
                                    if (eCode == -101) {
                                        eMsg = "没有授权";
                                    }
                                    /*
                                     * ToastUtil.showToastShortCenter(
                                     * ADActivity.this, "分享失败[" + eCode + "] " +
                                     * eMsg);
                                     */
                                }
                            }
                        });
            } else if (type == 4) {// 新浪微博
                umengShareUtils = new UmengShareUtils(mContext, sharetxt,
                        shareurl, sharetitle, shareImg);
                umengShareUtils.mController.getConfig().closeToast();
                boolean isSina = OauthHelper.isAuthenticated(mContext,
                        SHARE_MEDIA.SINA);
                // 如果未授权则授权
                if (!isSina) {
                    umengShareUtils.mController.doOauthVerify(mContext,
                            SHARE_MEDIA.SINA, new SocializeListeners.UMAuthListener() {
                                @Override
                                public void onStart(SHARE_MEDIA arg0) {

                                }

                                @Override
                                public void onError(SocializeException arg0,
                                                    SHARE_MEDIA arg1) {

                                }

                                @Override
                                public void onComplete(Bundle value,
                                                       SHARE_MEDIA platform) {
                                    umengShareUtils.mController.postShare(
                                            mContext, SHARE_MEDIA.SINA,
                                            new SocializeListeners.SnsPostListener() {
                                                @Override
                                                public void onStart() {
                                                    /*
                                                     * ToastUtil.
                                                     * showToastShortCenter
                                                     * (ADActivity.this ,
                                                     * "开始分享.");
                                                     */
                                                }

                                                @Override
                                                public void onComplete(
                                                        SHARE_MEDIA arg0,
                                                        int eCode,
                                                        SocializeEntity arg2) {
                                                    if (eCode == 200) {
                                                        /*
                                                         * ToastUtil.
                                                         * showToastShortCenter(
                                                         * ADActivity.this,
                                                         * "分享成功.");
                                                         */
                                                    } else {
                                                        String eMsg = "";
                                                        if (eCode == -101) {
                                                            eMsg = "没有授权";
                                                        }
                                                        /*
                                                         * ToastUtil.
                                                         * showToastShortCenter(
                                                         * ADActivity.this,
                                                         * "分享失败[" + eCode +
                                                         * "] " + eMsg);
                                                         */
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA arg0) {
                                }
                            });
                } else {
                    umengShareUtils.mController.postShare(mContext,
                            SHARE_MEDIA.SINA, new SocializeListeners.SnsPostListener() {
                                @Override
                                public void onStart() {
                                    /*
                                     * ToastUtil.showToastShortCenter(ADActivity.
                                     * this , "开始分享.");
                                     */
                                }

                                @Override
                                public void onComplete(SHARE_MEDIA arg0,
                                                       int eCode, SocializeEntity arg2) {
                                    if (eCode == 200) {
                                        /*
                                         * ToastUtil.showToastShortCenter(
                                         * ADActivity.this, "分享成功.");
                                         */
                                    } else {
                                        String eMsg = "";
                                        if (eCode == -101) {
                                            eMsg = "没有授权";
                                        }
                                        /*
                                         * ToastUtil.showToastShortCenter(
                                         * ADActivity.this, "分享失败[" + eCode +
                                         * "] " + eMsg);
                                         */
                                    }
                                }
                            });
                }
            }
        } else {
            ToastUtil.showToastShortCenter(mContext, mContext.getResources()
                    .getString(R.string.no_bitmap));
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartNum();
        //添加layout大小发生改变监听器
        //activityRootView.addOnLayoutChangeListener(this);
    }

    /*@Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            Log.e("TAG", "监听到软件盘弹起...");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            Log.e("TAG", "监听到软件盘关闭...");
            et_commodity_shopinfo_pop_shopnum.setFocusable(false);
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pWinGwcStr != null) {
            pWinGwcStr.dismiss();
            pWinGwcStr = null;
        }
        if (pWinShopInfo != null) {
            pWinShopInfo.dismiss();
            pWinShopInfo = null;
        }
        if (pWin != null) {
            pWin.dismiss();
            pWin = null;
        }
        if (pWinQuanYi != null) {
            pWinQuanYi.dismiss();
            pWinQuanYi = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (et_commodity_shopinfo_pop_shopnum != null) {
            et_commodity_shopinfo_pop_shopnum.clearTextChangedListeners();
            et_commodity_shopinfo_pop_shopnum = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
