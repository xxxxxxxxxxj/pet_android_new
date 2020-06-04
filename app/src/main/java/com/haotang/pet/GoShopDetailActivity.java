package com.haotang.pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BannerBathLoopAdapter;
import com.haotang.pet.adapter.GoShopMemberAdapter;
import com.haotang.pet.adapter.GoShopStepAdapter;
import com.haotang.pet.adapter.ShopEvaRecyAdapter;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.entity.MemberEntity;
import com.haotang.pet.entity.ShopEntity;
import com.haotang.pet.entity.ShopServices;
import com.haotang.pet.entity.TabEntity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
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
import com.haotang.pet.view.MyGridView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class GoShopDetailActivity extends SuperActivity implements OnBannerListener {
    public static GoShopDetailActivity act;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ib_titlebar_back;
    @BindView(R.id.tv_titlebar_title)
    TextView tv_titlebar_title;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ib_titlebar_other;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rl_titlebar;
    //    @BindView(R.id.show_title)
//    TextView show_title;
//    @BindView(R.id.layout_show_title)
//    LinearLayout layout_show_title;
//    @BindView(R.id.imageView_isplay)
//    ImageView imageView_isplay;
//    @BindView(R.id.rpv_servicedetail_pet)
//    RollPagerView rpv_servicedetail_pet;
    @BindView(R.id.rl_ppllayout_top)
    LinearLayout rl_ppllayout_top;
    @BindView(R.id.imageView_go_shop_phone)
    ImageView imageView_go_shop_phone;
    @BindView(R.id.textView_shop_name)
    TextView textView_shop_name;
    @BindView(R.id.imageView_shop_img)
    ImageView imageView_shop_img;
    @BindView(R.id.layout_shop_weixin)
    LinearLayout layout_shop_weixin;
    @BindView(R.id.textview_address_title)
    TextView textview_address_title;
    @BindView(R.id.textView_go_shop_address)
    TextView textView_go_shop_address;
    @BindView(R.id.imageView_go_shop_nav)
    ImageView imageView_go_shop_nav;
    @BindView(R.id.layout_nav)
    LinearLayout layout_nav;
    @BindView(R.id.layout_go_shop_address)
    RelativeLayout layout_go_shop_address;
    @BindView(R.id.girdview_service_kind)
    MyGridView girdview_service_kind;
    @BindView(R.id.gridview_shop_people)
    MyGridView gridview_shop_people;
    @BindView(R.id.listview_shop_evas)
    RecyclerView listview_shop_evas;
    @BindView(R.id.pulltoscrollview)
    PullToRefreshScrollView pulltoscrollview;
    @BindView(R.id.bt_shopdetail_submit)
    Button bt_shopdetail_submit;
    @BindView(R.id.textView_go_shop_title)
    TextView textView_go_shop_title;
    @BindView(R.id.textView_go_shop)
    TextView textView_go_shop;
    @BindView(R.id.textview_goodRate)
    TextView textview_goodRate;
    @BindView(R.id.textViewHaoPingLv)
    TextView textViewHaoPingLv;
    @BindView(R.id.mTabLayout_4)
    CommonTabLayout mTabLayout4;
    @BindView(R.id.banner_item_detail)
    Banner bannerItemDetail;

    //	private ImageView service_back;
//	private ImageView service_share;
//	private PullPushLayout pplLayout;
//	private RelativeLayout rlTitle;
//	private ImageView service_back_blow;
//	private ImageView service_share_below;
//	private Drawable bgBackDrawable;
//	private Drawable bgBackBelowDrawable;
//	private Drawable bgShareDrawable;
//	private Drawable bgShareBelowDrawable;
//	private Drawable bgNavBarDrawable;
//	private int alphaMax = 180;
//	private ViewPager ivPet;
//	private LinearLayout point;
//	private ArrayList<View> dots;
//	private String[] pics = new String[0];
//	private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
//	private int oldPostion;
//	private MPagerAdapter adapter;
//	private GridView gridView_go_shop_show_icon;
//	private int currentItem;
//	private TextView textView_go_shop_title;
//	private TextView textView_go_shop;
//	private TextView textView_go_shop_address;
//	private ImageView imageView_go_shop_phone;
//	private ImageView imageview_go_shop_area;
//	private MProgressDialog pDialog;
//	private ScheduledExecutorService scheduledExecutor;
//	private RelativeLayout shop_detail_down;
//	private TextView textView_title;
//	private LinearLayout layout_shop_weixin;
//	private ImageView img_back;
    private String shopWxNum;
    private String shopWxImg;
    private String servicename;
    private int servicetype;
    private int serviceloc;
    private int petid;
    private int petkind;
    private String petname;
    private int customerpetid;
    private String customerpetname;
    private int clicksort;
    private int areaid;
    private int previous = 0;
    private Intent fIntent;
    private int shopid;
    private int serviceid;
    private GoShopStepAdapter shopStepAdapter;
    private GoShopMemberAdapter shopMemberAdapter;
    private BannerBathLoopAdapter adapterBanner;
    private ShopEvaRecyAdapter shopEvaAdapter;
    private ShopEntity shopEntity = null;
    //    private ArrayList<String> listbanners = new ArrayList<String>();
    private List<String> taglists = new ArrayList<>();
    private List<Comment> evalists = new ArrayList<>();
    private int page = 1;
    private int size = 10;
    private int totalAmount;
    private boolean isFirst = true;
    private double lat = 0;
    private double lng = 0;
    private String shareImg;
    private String shareTitle;
    private String shareUrl;
    private String shareDesc;
    private PopupWindow pWin;
    private IWXAPI api;
    private Bitmap bmp;
    private LayoutInflater mInflater;
    private String[] mTitles = {"服务项目", "店铺成员", "店铺评价"};
    private int[] mIconUnselectIds = {
            R.drawable.icon_default, R.drawable.icon_default,
            R.drawable.icon_default, R.drawable.icon_default};
    private int[] mIconSelectIds = {
            R.drawable.icon_default, R.drawable.icon_default,
            R.drawable.icon_default, R.drawable.icon_default};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] colors = new int[2];
    // banner 710 380
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//可不加
//        }

        setContentView(R.layout.activity_go_shop_detail);
        api = WXAPIFactory.createWXAPI(this, Global.APP_ID);
        mInflater = LayoutInflater.from(this);
        ButterKnife.bind(this);
        evalists.clear();
        act = this;
        fIntent = getIntent();
        shopid = fIntent.getIntExtra("shopid", 0);
        lat = fIntent.getDoubleExtra("addr_lat", 0);
        lng = fIntent.getDoubleExtra("addr_lng", 0);
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

        setView();
        initListener();

//		findView();
//		setView();
//		getData();
//		initListener();
    }

    private void initListener() {
        pulltoscrollview.setMode(PullToRefreshBase.Mode.DISABLED);
        pulltoscrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {
                    GetShopComment();
                }
            }
        });
        mTabLayout4.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    girdview_service_kind.setVisibility(View.VISIBLE);
                    gridview_shop_people.setVisibility(View.GONE);
                    listview_shop_evas.setVisibility(View.GONE);
                    pulltoscrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                } else if (position == 1) {
                    girdview_service_kind.setVisibility(View.GONE);
                    gridview_shop_people.setVisibility(View.VISIBLE);
                    listview_shop_evas.setVisibility(View.GONE);
                    pulltoscrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                } else if (position == 2) {
                    girdview_service_kind.setVisibility(View.GONE);
                    gridview_shop_people.setVisibility(View.GONE);
                    listview_shop_evas.setVisibility(View.VISIBLE);
                    pulltoscrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        girdview_service_kind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gridview_shop_people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberEntity memberEntity = (MemberEntity) parent.getItemAtPosition(position);
                if (memberEntity.role == 3) {
                    return;
                }
                Intent intent = null;
                if (memberEntity.role == 1) {
                    intent = new Intent(mContext, ShopOwnerActivity.class);
                } else if (memberEntity.role == 2) {
                    intent = new Intent(mContext, BeauticianDetailActivity.class);
                }
                intent.putExtra("beautician_id", memberEntity.id);
                intent.putExtra("previous", Global.SHOP_DETAIL_TO_BEAUDETAIL);
                startActivity(intent);
            }
        });
    }

    private void setView() {

        mTabLayout4.setmTextSelectsize(mTabLayout4.sp2px(18));
        mTabLayout4.setGradient(true);
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        mTabLayout4.setColors(colors);
        mTabLayout4.setIndicatorTextMiddle(true);

        taglists.clear();
        ib_titlebar_other.setVisibility(View.VISIBLE);
        ib_titlebar_other.setBackgroundResource(R.drawable.service_share);
        shopEntity = new ShopEntity();
        tv_titlebar_title.setText("门店详情");
        taglists.add("服务项目");
        taglists.add("店铺成员");
        taglists.add("店铺评价");
        mTabEntities.clear();
        for (int i = 0; i < taglists.size(); i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout4.setTabData(mTabEntities);
        setData();
        getData();
        GetShopComment();
    }

    private void setData() {
        shopStepAdapter = new GoShopStepAdapter(mContext, shopEntity.list);
        girdview_service_kind.setAdapter(shopStepAdapter);
        shopMemberAdapter = new GoShopMemberAdapter(mContext, shopEntity.memberEntities);
        gridview_shop_people.setAdapter(shopMemberAdapter);
//        adapterBanner = new BannerBathLoopAdapter(mContext, rpv_servicedetail_pet, shopEntity.imgList);
//        rpv_servicedetail_pet.setAdapter(adapterBanner);

//        shopEvaAdapter = new ShopEvaAdapter(mContext, evalists);
//        listview_shop_evas.setAdapter(shopEvaAdapter);
//        shopEvaAdapter.setAcType(1);
        listview_shop_evas.setLayoutManager(new FullyRecyLinearLayoutManager(this));
        shopEvaAdapter = new ShopEvaRecyAdapter(R.layout.item_shopevalist, evalists);
        listview_shop_evas.setAdapter(shopEvaAdapter);
        listview_shop_evas.setNestedScrollingEnabled(false);
        shopEvaAdapter.setAcType(1);
        listview_shop_evas.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL, DensityUtil.dp2px(this, 1), ContextCompat.getColor(this, R.color.aEEEEEE)));
    }


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
                        if (shopEntity.list.size() > 0) {
                            shopStepAdapter.notifyDataSetChanged();
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
                        if (shopEntity.memberEntities.size() > 0) {
                            shopMemberAdapter.notifyDataSetChanged();
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
                            textView_go_shop_title.setText(shopEntity.shopName);
                        }
                        if (object.has("shopWxImg") && !object.isNull("shopWxImg")) {
                            shopWxImg = object.getString("shopWxImg");
                        }
                        if (object.has("shopWxNum") && !object.isNull("shopWxNum")) {
                            shopWxNum = object.getString("shopWxNum");
                        }
                        if (object.has("img") && !object.isNull("img")) {
                            shopEntity.shopimg = object.getString("img");
                        }
                        if (object.has("openTime") && !object.isNull("openTime")) {
                            shopEntity.operTime = object.getString("openTime");
                            textView_go_shop.setText("营业时间：" + shopEntity.operTime);
                        }
                        if (object.has("address") && !object.isNull("address")) {
                            shopEntity.address = object.getString("address");
                            textView_go_shop_address.setText(shopEntity.address);
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
                        textViewHaoPingLv.setVisibility(View.GONE);
                        textview_goodRate.setVisibility(View.GONE);
                        if (object.has("goodRate") && !object.isNull("goodRate")) {
                            shopEntity.goodRate = object.getString("goodRate");
                            try {
                                if (!shopEntity.goodRate.equals("0")) {
                                    textViewHaoPingLv.setVisibility(View.VISIBLE);
                                    textview_goodRate.setVisibility(View.VISIBLE);
                                    textview_goodRate.setText(shopEntity.goodRate + "");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    private void GetShopComment() {
        mPDialog.showDialog();
        CommUtil.GetShopComment(mContext, shopid, page, size, evaHandler);
    }

    private AsyncHttpResponseHandler evaHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                pulltoscrollview.onRefreshComplete();
                JSONObject obj = new JSONObject(new String(responseBody));
                int code = obj.getInt("code");
                if (code == 0) {
                    if (obj.has("data") && !obj.isNull("data")) {
                        JSONObject objData = obj.getJSONObject("data");
                        if (objData.has("totalAmount") && !objData.isNull("totalAmount")) {
                            totalAmount = objData.getInt("totalAmount");
                        }
                        if (isFirst) {
                            isFirst = false;
                            String showStr = null;
                            if (totalAmount > 1000) {
                                showStr = "999+";
                            } else {
                                showStr = totalAmount + "";
                            }
                            taglists.clear();
                            taglists.add("服务项目");
                            taglists.add("店铺成员");
                            taglists.add("店铺评价(" + showStr + ")");
                            mTabEntities.clear();
                            for (int i = 0; i < taglists.size(); i++) {
                                mTabEntities.add(new TabEntity(taglists.get(i), mIconSelectIds[i], mIconUnselectIds[i]));
                            }
                            mTabLayout4.setTabData(mTabEntities);
                        }
                        if (objData.has("totalPage") && !objData.isNull("totalPage")) {
                            String totalPage = objData.getString("totalPage");
                        }
                        if (objData.has("dataset") && !objData.isNull("dataset")) {
                            JSONArray array = objData.getJSONArray("dataset");
                            if (array.length() > 0) {
                                page++;
                                for (int i = 0; i < array.length(); i++) {
                                    evalists.add(Comment.json2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                        if (evalists.size() > 0) {
                            shopEvaAdapter.notifyDataSetChanged();
                            listview_shop_evas.post(new Runnable() {
                                @Override
                                public void run() {
                                    pulltoscrollview.getRefreshableView().fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
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

    private void setLayout(int mWidth) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_ppllayout_top.getLayoutParams();
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


//        ViewTreeObserver vto = rl_ppllayout_top.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                height = rl_ppllayout_top.getHeight();
//                pullpushlayout.setScrollViewListener(act);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int mWidth = Utils.getDisplayMetrics(this)[0];
        setLayout(mWidth);
    }
    //	private void initListener() {
//		img_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				finishWithAnimation();
//			}
//		});
//		service_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				finishWithAnimation();
//			}
//		});
//		btSubmit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (ShopListActivity.act != null) {
//					if(previous == Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST
//							|| previous == Global.SERVICEFEATURE_TO_PETLIST){
////						Intent intent = new Intent(GoShopDetailActivity.this,ServiceFeature.class);
//						Intent intent = new Intent(GoShopDetailActivity.this,ServiceNewActivity.class);
//						intent.putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_RIGHT());
//						getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
//						intent.putExtra("servicename",fIntent.getStringExtra("servicename"));
//						intent.putExtra("previous", previous);
//						intent.putExtra("clicksort", clicksort);
//						intent.putExtra("serviceid",fIntent.getIntExtra("serviceid", 0));
//						intent.putExtra("servicetype",fIntent.getIntExtra("servicetype", 0));
//						intent.putExtra("serviceType",fIntent.getIntExtra("servicetype", 0));
//						intent.putExtra("serviceloc",fIntent.getIntExtra("serviceloc", 0));
//						intent.putExtra("petid",fIntent.getIntExtra("petid", 0));
//						intent.putExtra("petkind",fIntent.getIntExtra("petkind", 0));
//						intent.putExtra("petname",fIntent.getStringExtra("petname"));
//						intent.putExtra("shopid",fIntent.getIntExtra("shopid", 0));
//						intent.putExtra("customerpetid",fIntent.getIntExtra("customerpetid", 0));
//						intent.putExtra("customerpetname",fIntent.getStringExtra("customerpetname"));
//						intent.putExtra("shopid", shopid);
//						intent.putExtra("shopname", shopEntity.shopName);
//						intent.putExtra("shopimg", shopEntity.shopimg);
//						intent.putExtra("shopaddr", shopEntity.address);
//						intent.putExtra("shoptel", shopEntity.phone);
//						intent.putExtra("areaid", areaid);
//						intent.putExtra("addr", getIntent().getStringExtra("addr"));
//						intent.putExtra("addr_lat", getIntent().getDoubleExtra("addr_lat",0));
//						intent.putExtra("addr_lng", getIntent().getDoubleExtra("addr_lng",0));
//						intent.putExtra("addr_id", getIntent().getIntExtra("addr_id",0));
//						intent.putExtra("shopActiveTitle", getIntent().getStringExtra("shopActiveTitle"));
//						intent.putExtra("shopActivePoint", getIntent().getIntExtra("shopActivePoint",0));
//						intent.putExtra("shopActiveBackup", getIntent().getStringExtra("shopActiveBackup"));
//						intent.putExtra("shopWxNum", getIntent().getStringExtra("shopWxNum"));
//						intent.putExtra("shopWxImg", getIntent().getStringExtra("shopWxImg"));
//						intent.putExtra("shopPhone", getIntent().getStringExtra("shopPhone"));
//						intent.putExtra("dist", getIntent().getStringExtra("dist"));
//						startActivity(intent);
//					}else{
//						Intent data = new Intent();
//						data.putExtra("areaid", areaid);
//						data.putExtra("shopid", shopid);
//						data.putExtra("shopname", shopEntity.shopName);
//						data.putExtra("shopimg", shopEntity.shopimg);
//						data.putExtra("shopaddr", shopEntity.address);
//						data.putExtra("shoptel", shopEntity.phone);
//						data.putExtra("addr", getIntent().getStringExtra("addr"));
//						data.putExtra("addr_lat", getIntent().getDoubleExtra("addr_lat",0));
//						data.putExtra("addr_lng", getIntent().getDoubleExtra("addr_lng",0));
//						data.putExtra("addr_id", getIntent().getIntExtra("addr_id",0));
//						data.putExtra("shopActiveTitle", getIntent().getStringExtra("shopActiveTitle"));
//						data.putExtra("shopActivePoint", getIntent().getIntExtra("shopActivePoint",0));
//						data.putExtra("shopActiveBackup", getIntent().getStringExtra("shopActiveBackup"));
//						data.putExtra("shopWxNum", getIntent().getStringExtra("shopWxNum"));
//						data.putExtra("shopWxImg", getIntent().getStringExtra("shopWxImg"));
//						data.putExtra("shopPhone", getIntent().getStringExtra("shopPhone"));
//						data.putExtra("dist", getIntent().getStringExtra("dist"));
//						ShopListActivity.act.setResult(Global.RESULT_OK, data);
//					}
//					ShopListActivity.act.finishWithAnimation();
//				}
//				finishWithAnimation();
//			}
//		});
//		imageView_go_shop_phone.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				try {
//					if (shopEntity != null) {
//						if (!shopEntity.phone.equals("")
//								|| shopEntity.phone != null) {
//							MDialog mDialog = new MDialog.Builder(
//									GoShopDetailActivity.this).setTitle("提示")
//									.setType(MDialog.DIALOGTYPE_CONFIRM)
//									.setMessage("是否拨打电话?").setCancelStr("否")
//									.setOKStr("是")
//									.positiveListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											// 确认拨打电话
//											Utils.telePhoneBroadcast(
//													GoShopDetailActivity.this,
//													shopEntity.phone);
//										}
//									}).build();
//							mDialog.show();
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		imageView_go_shop_nav.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(GoShopDetailActivity.this,
//						ShopLocationActivity.class);
//				intent.putExtra(Global.ANIM_DIRECTION(),
//						Global.ANIM_COVER_FROM_RIGHT());
//				getIntent().putExtra(Global.ANIM_DIRECTION(),
//						Global.ANIM_COVER_FROM_LEFT());
//				intent.putExtra("shopname", shopEntity.shopName);
//				intent.putExtra("shopaddr", shopEntity.address);
//				intent.putExtra("shoplat", shopEntity.lat);
//				intent.putExtra("shoplng", shopEntity.lng);
//				startActivity(intent);
//			}
//		});
//		layout_shop_weixin.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Utils.setWeChatQrCodeDialog(mContext, shopWxNum, shopWxImg);
//			}
//		});
//	}
//	private void goNext(int pre,Class cls){
//		Intent intent = new Intent(mContext, cls);
//		intent.putExtra("previous", pre);
//		intent.putExtra("areaid", areaid);
//		intent.putExtra("petid", petid);
//		intent.putExtra("petkind", petkind);
//		intent.putExtra("serviceid", serviceid);
//		intent.putExtra("customerpetid", customerpetid);
//		intent.putExtra("customerpetname", customerpetname);
//		intent.putExtra("petname", petname);
////		intent.putExtra("petimage", petimage);
//		Utils.mLogError("==-->shopDetail areaid:="+areaid+" petid:="+petid+" petkind:= "+petkind+" serviceid:= "+serviceid+" customerpetid:= "+customerpetid+" customerpetname:= "+customerpetname);
//		startActivity(intent);
//	}
//	private void findView() {
//		shopEntity = new ShopEntity();
//		pDialog = new MProgressDialog(this);
//		img_back = (ImageView) findViewById(R.id.img_back);
//		pplLayout = (PullPushLayout) findViewById(R.id.ppl_layout);
//		service_back = (ImageView) findViewById(R.id.service_back);
//		service_back_blow = (ImageView) findViewById(R.id.service_back_blow);
//		service_share = (ImageView) findViewById(R.id.service_share);
//		service_share_below = (ImageView) findViewById(R.id.service_share_below);
//		rlTitle = (RelativeLayout) findViewById(R.id.rl_servicedetail_title);
//
//		ivPet = (ViewPager) findViewById(R.id.iv_servicedetail_pet);
//		point = (LinearLayout) findViewById(R.id.point);
//
//		gridView_go_shop_show_icon = (GridView) findViewById(R.id.gridView_go_shop_show_icon);
//		textView_go_shop_title = (TextView) findViewById(R.id.textView_go_shop_title);
//		textView_go_shop = (TextView) findViewById(R.id.textView_go_shop);
//		textView_go_shop_address = (TextView) findViewById(R.id.textView_go_shop_address);
//		imageView_go_shop_phone = (ImageView) findViewById(R.id.imageView_go_shop_phone);
//		imageView_go_shop_nav = (ImageView) findViewById(R.id.imageView_go_shop_nav);
//		imageview_go_shop_area = (ImageView) findViewById(R.id.imageview_go_shop_area);
//		btSubmit = (Button) findViewById(R.id.bt_shopdetail_submit);
//		shop_detail_down = (RelativeLayout) findViewById(R.id.shop_detail_down);
//		textView_title = (TextView) findViewById(R.id.textView_title);
//		layout_shop_weixin = (LinearLayout) findViewById(R.id.layout_shop_weixin);
//
//	}

//	private void setView() {
//		textView_title.setText("店铺详情");
//		if (previous == Global.ORDER_OTHER_STATUS_TO_SHOPDETAIL) {
//			shop_detail_down.setVisibility(View.GONE);
//		}
////		styleShow();
//		imageViews = new ArrayList<ImageView>();
//		dots = new ArrayList<View>();
//		shopStepAdapter = new GoShopStepAdapter(GoShopDetailActivity.this,
//				shopEntity);
//		gridView_go_shop_show_icon.setAdapter(shopStepAdapter);
//	}

//	private void styleShow() {
//		pplLayout.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre() {
//
//			@Override
//			public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onSlide(int alpha) {
//				// TODO Auto-generated method stub
//				int alphaReverse = alphaMax - alpha;
//				if (alphaReverse < 0) {
//					alphaReverse = 0;
//				}
//				bgBackDrawable.setAlpha(alphaReverse);
//				bgBackBelowDrawable.setAlpha(alpha);
//				bgShareDrawable.setAlpha(alphaReverse);
//				bgShareBelowDrawable.setAlpha(alpha);
//				bgNavBarDrawable.setAlpha(alpha);
//
//			}
//		});
//
//		bgBackDrawable = service_back.getBackground();
//		bgBackBelowDrawable = service_back_blow.getBackground();
//		bgShareDrawable = service_share.getBackground();
//		bgShareBelowDrawable = service_share_below.getBackground();
//		bgNavBarDrawable = rlTitle.getBackground();
//		bgBackDrawable.setAlpha(alphaMax);
//		bgShareDrawable.setAlpha(alphaMax);
//		bgNavBarDrawable.setAlpha(0);
//		bgBackBelowDrawable.setAlpha(0);
//		bgShareBelowDrawable.setAlpha(0);
//	}

//	private void getData() {
//		pDialog.showDialog();
//		CommUtil.GetShopInfo(this, shopid, serviceid, getShopDetail);
//	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//
//		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
//		scheduledExecutor.scheduleAtFixedRate(new MRunnable(), 3, 3,
//				TimeUnit.SECONDS);
//
////		StatusBarCompat.setStatusBarColor(mContext,getResources().getColor(R.color.a3a3636));
//		int statusBarHeight = getStatusBarHeight();
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Utils.dip2px(mContext,25), Utils.dip2px(mContext,25));
//		lp.topMargin = statusBarHeight;
//		lp.leftMargin = Utils.dip2px(mContext,10);
//		img_back.setLayoutParams(lp);
//	}

//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		scheduledExecutor.shutdown();
//	}

//	private AsyncHttpResponseHandler getShopDetail = new AsyncHttpResponseHandler() {
//
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				byte[] responseBody) {
//			setData(responseBody);
//		}
//
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				byte[] responseBody, Throwable error) {
//			// TODO Auto-generated method stub
//		}
//
//	};
//	private Button btSubmit;

//	private void setData(byte[] responseBody) {
//		try {
//			Utils.mLogError("==-->shop " + new String(responseBody));
//			JSONObject jsonObject = new JSONObject(new String(responseBody));
//			int code = jsonObject.getInt("code");
//			if (code == 0) {
//				if (jsonObject.has("data") && !jsonObject.isNull("data")) {
//					JSONObject object = jsonObject.getJSONObject("data");
//					if (object.has("serviceItems")&& !object.isNull("serviceItems")) {
//						JSONArray array = object.getJSONArray("serviceItems");
//						for (int i = 0; i < array.length(); i++) {
//							ShopServices shopServices = new ShopServices();
//							JSONObject objectServices = array.getJSONObject(i);
//							if (objectServices.has("point")&& !objectServices.isNull("point")) {
//								shopServices.point = objectServices.getInt("point");
//							}
//							if (objectServices.has("itemName")&& !objectServices.isNull("itemName")) {
//								shopServices.name = objectServices.getString("itemName");
//							}
//							if (objectServices.has("img")&& !objectServices.isNull("img")) {
//									shopServices.img = objectServices.getString("img");
//							}
//							shopEntity.list.add(shopServices);
//						}
//					}
//					shopStepAdapter.notifyDataSetChanged();
//					if (object.has("areaImg") && !object.isNull("areaImg")) {
//						shopEntity.areaImg = object.getString("areaImg");
//						GlideUtil.loadImg(GoShopDetailActivity.this,shopEntity.areaImg,
//								imageview_go_shop_area, 0);
//					}
//					if (object.has("shopName") && !object.isNull("shopName")) {
//						shopEntity.shopName = object.getString("shopName");
//						textView_go_shop_title.setText(shopEntity.shopName);
//					}
//					if (object.has("shopWxImg")&&!object.isNull("shopWxImg")){
//						shopWxImg = object.getString("shopWxImg");
//					}
//					if (object.has("shopWxNum")&&!object.isNull("shopWxNum")){
//						shopWxNum = object.getString("shopWxNum");
//					}
//					if (object.has("img") && !object.isNull("img")) {
//						shopEntity.shopimg = object.getString("img");
//					}
//					if (object.has("operTime") && !object.isNull("operTime")) {
//						shopEntity.operTime = object.getString("operTime");
//						textView_go_shop.setText("营业时间：" + shopEntity.operTime);
//					}
//					if (object.has("address") && !object.isNull("address")) {
//						shopEntity.address = object.getString("address");
//						textView_go_shop_address.setText(shopEntity.address);
//						textView_go_shop_address.post(new Runnable() {
//
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								if (textView_go_shop_address.getWidth() > Utils
//										.getDisplayMetrics(GoShopDetailActivity.this)[0]
//										- Utils.dip2px(
//												GoShopDetailActivity.this, 120)) {
//
//									textView_go_shop_address.setWidth(Utils
//											.getDisplayMetrics(GoShopDetailActivity.this)[0]
//											- Utils.dip2px(
//													GoShopDetailActivity.this,
//													120));
//								}
//							}
//						});
//					}
//					if (object.has("lat") && !object.isNull("lat")) {
//						shopEntity.lat = object.getDouble("lat");
//					}
//					if (object.has("lng") && !object.isNull("lng")) {
//						shopEntity.lng = object.getDouble("lng");
//					}
//					if (object.has("phone") && !object.isNull("phone")) {
//						shopEntity.phone = object.getString("phone");
//					}
//					if (object.has("bannerImg") && !object.isNull("bannerImg")) {
//						JSONArray array = object.getJSONArray("bannerImg");
//						for (int i = 0; i < array.length(); i++) {
//							Utils.mLogError("==-->shoparray "
//									+ array.getString(i));
//							shopEntity.imgList.add(array.getString(i));
//							ImageView imageView = new ImageView(
//									GoShopDetailActivity.this);
//							imageView.setScaleType(ScaleType.CENTER_CROP);
//							imageView
//									.setBackgroundResource(R.drawable.icon_production_default);
//							imageViews.add(imageView);
//						}
//						pics = new String[array.length()];
//						for (int i = 0; i < array.length(); i++) {
//							pics[i] = array.getString(i);
//							GlideUtil.loadImg(GoShopDetailActivity.this,array.getString(i),
//									imageViews.get(i), 0);
//						}
//						adapter = new MPagerAdapter(mContext);
//						ivPet.setAdapter(adapter);
//						// adapter.setData(imageViews, pics,1);
//						adapter.setData(imageViews, pics, 1);
//						ivPet.setOnPageChangeListener(new MPageChangeListener());
//						dots.clear();
//						point.removeAllViews();
//						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//								15, 15);
//						params.leftMargin = 10;
//						if (imageViews.size() > 1) {
//							for (int i = 0; i < imageViews.size(); i++) {
//								if (i == 0) {
//									View view = new View(
//											GoShopDetailActivity.this);
//									view.setBackgroundResource(R.drawable.dot_focused);
//									view.setPadding(10, 0, 10, 0);
//									view.setLayoutParams(params);
//									point.addView(view);
//									dots.add(view);
//								} else {
//									View view = new View(
//											GoShopDetailActivity.this);
//									view.setBackgroundResource(R.drawable.dot_normal);
//									view.setPadding(10, 0, 10, 0);
//									view.setLayoutParams(params);
//
//									point.addView(view);
//									dots.add(view);
//								}
//							}
//						}
//						adapter.notifyDataSetChanged();
//					}
//				}
//			} else {
//
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	// --start 2015年9月28日10:25:40
//	private class MPageChangeListener implements OnPageChangeListener {
//		/**
//		 * 页面状态改变执行的方法
//		 * */
//		@Override
//		public void onPageScrollStateChanged(int arg0) {
//
//		}
//
//		@Override
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//		}
//
//		/**
//		 * 页面选中以后执行的方法
//		 * */
//		@Override
//		public void onPageSelected(int position) {
//
//			currentItem = position;
//			// 圆点更新
//			// 更新当前页面为白色的圆点
//			dots.get(position % dots.size()).setBackgroundResource(
//					R.drawable.dot_focused);
//			// 更新上一个页面为灰色的圆点
//			dots.get(oldPostion % dots.size()).setBackgroundResource(
//					R.drawable.dot_normal);
//			// 更新上一个页面的位置
//			oldPostion = position;
//		}
//
//	}

//	public String getCurrentTime() {// 避免特殊字符产生无法调起拍照后无法保存返回
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
//		String currentTime = df.format(new Date());// new Date()为获取当前系统时间
//		return currentTime;
//	}

//	private class MRunnable implements Runnable {
//
//		@Override
//		public void run() {
//			// 切换界面
//			currentItem = (currentItem + 1) % dots.size();
//			;
//			// 更新UI
//			handler.sendEmptyMessage(0);
//		}
//
//	}

    //	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			// 切换到Viewpager 当前的页面
//			ivPet.setCurrentItem(currentItem);
//		};
//	};
//	private ImageView imageView_go_shop_nav;
//
//	private int getStatusBarHeight(){
//		/**
//		 * 获取状态栏高度
//		 * */
//		int statusBarHeight = -1;
//		try {
//			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
//			Object object = clazz.newInstance();
//			int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
//			statusBarHeight = getResources().getDimensionPixelSize(height);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return statusBarHeight;
//	}
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

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    private void setShareContent(int shareType) {
        if (shareUrl.contains("?")) {
            shareUrl = shareUrl
                    + "&system="
                    + CommUtil.getSource()
                    + "_"
                    + Global.getCurrentVersion(mContext)
                    + "&imei="
                    + Global.getIMEI(mContext)
                    /*+ "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString("cellphone", "") */
                    + "&phoneModel="
                    + Build.BRAND + " " + Build.MODEL
                    + "&phoneSystemVersion=" + "Android "
                    + Build.VERSION.RELEASE + "&time="
                    + System.currentTimeMillis()
                    + "&shopId=" + shopid
            ;
        } else {
            shareUrl = shareUrl
                    + "?system="
                    + CommUtil.getSource()
                    + "_"
                    + Global.getCurrentVersion(mContext)
                    + "&imei="
                    + Global.getIMEI(mContext)
                    /*+ "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString("cellphone", "")*/
                    + "&phoneModel="
                    + Build.BRAND + " " + Build.MODEL
                    + "&phoneSystemVersion=" + "Android "
                    + Build.VERSION.RELEASE + "&time="
                    + System.currentTimeMillis()
                    + "&shopId=" + shopid
            ;
        }
        Log.e("TAG", "shareUrl = " + shareUrl);
        if (bmp != null && shareUrl != null && !TextUtils.isEmpty(shareUrl)) {
            WXWebpageObject wxwebpage = new WXWebpageObject();
            wxwebpage.webpageUrl = shareUrl;
            WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
            wxmedia.title = shareTitle;
            wxmedia.description = shareDesc;
            Bitmap createBitmapThumbnail = Utils.createBitmapThumbnail(bmp);
            wxmedia.setThumbImage(createBitmapThumbnail);
            wxmedia.thumbData = Util_WX.bmpToByteArray(
                    createBitmapThumbnail, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = wxmedia;
            if (shareType == 1) {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            }
            api.sendReq(req);
        }
    }

    private void showShare() {
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
        ll_sharedialog_wxpyq.setOnClickListener(new View.OnClickListener() {
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
        ll_sharedialog_qqfriend.setOnClickListener(new View.OnClickListener() {
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
        ll_sharedialog_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 新浪微博
                pWin.dismiss();
                pWin = null;
//                setWXShareContent(4);
            }
        });
        btn_sharedialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 取消
                pWin.dismiss();
                pWin = null;
            }
        });
    }

    public void setIsOpen(int position, Comment comment) {
        comment.isOpen = !comment.isOpen;
        evalists.set(position, comment);
        shopEvaAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.rl_titlebar, R.id.rl_ppllayout_top, R.id.imageView_go_shop_phone, R.id.textView_shop_name, R.id.imageView_shop_img, R.id.layout_shop_weixin, R.id.textview_address_title, R.id.textView_go_shop_address, R.id.imageView_go_shop_nav, R.id.layout_nav, R.id.layout_go_shop_address, R.id.bt_shopdetail_submit})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.ib_titlebar_other:
                new Thread(networkTask).start();
                break;
            case R.id.rl_titlebar:
                break;
            case R.id.rl_ppllayout_top:
                break;
            case R.id.imageView_go_shop_phone:
                try {
                    if (shopEntity != null) {
                        if (!shopEntity.phone.equals("")
                                || shopEntity.phone != null) {
                            MDialog mDialog = new MDialog.Builder(
                                    GoShopDetailActivity.this).setTitle("提示")
                                    .setType(MDialog.DIALOGTYPE_CONFIRM)
                                    .setMessage("是否拨打电话?").setCancelStr("否")
                                    .setOKStr("是")
                                    .positiveListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // 确认拨打电话
                                            Utils.telePhoneBroadcast(GoShopDetailActivity.this, shopEntity.phone);
                                        }
                                    }).build();
                            mDialog.show();
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.textView_shop_name:
            case R.id.imageView_shop_img:
            case R.id.layout_shop_weixin:
                Utils.setWeChatQrCodeDialog(mContext, shopWxNum, shopWxImg);
                break;
            case R.id.textview_address_title:
                break;
            case R.id.textView_go_shop_address:
                break;
            case R.id.imageView_go_shop_nav:
            case R.id.layout_nav:
                intent = new Intent(GoShopDetailActivity.this, ShopLocationActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                intent.putExtra("shopname", shopEntity.shopName);
                intent.putExtra("shopaddr", shopEntity.address);
                intent.putExtra("shoplat", shopEntity.lat);
                intent.putExtra("shoplng", shopEntity.lng);
                startActivity(intent);
                break;
            case R.id.layout_go_shop_address:
                break;
            case R.id.bt_shopdetail_submit:
                if (ShopListActivity.act != null) {
                    if (previous == Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST || previous == Global.SERVICEFEATURE_TO_PETLIST) {
                        intent = new Intent(GoShopDetailActivity.this, ServiceNewActivity.class);
                        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                        getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                        intent.putExtra("servicename", fIntent.getStringExtra("servicename"));
                        intent.putExtra("previous", previous);
                        intent.putExtra("cityId", getIntent().getIntExtra("cityId", 0));
                        intent.putExtra("previous", previous);
                        intent.putExtra("clicksort", clicksort);
                        intent.putExtra("serviceid", fIntent.getIntExtra("serviceid", 0));
                        intent.putExtra("servicetype", fIntent.getIntExtra("servicetype", 0));
                        intent.putExtra("serviceType", fIntent.getIntExtra("servicetype", 0));
                        intent.putExtra("serviceloc", fIntent.getIntExtra("serviceloc", 0));
                        intent.putExtra("petid", fIntent.getIntExtra("petid", 0));
                        intent.putExtra("petkind", fIntent.getIntExtra("petkind", 0));
                        intent.putExtra("petname", fIntent.getStringExtra("petname"));
                        intent.putExtra("shopid", fIntent.getIntExtra("shopid", 0));
                        intent.putExtra("customerpetid", fIntent.getIntExtra("customerpetid", 0));
                        intent.putExtra("customerpetname", fIntent.getStringExtra("customerpetname"));
                        intent.putExtra("shopid", shopid);
                        intent.putExtra("shopname", shopEntity.shopName);
                        intent.putExtra("shopimg", shopEntity.shopimg);
                        intent.putExtra("shopaddr", shopEntity.address);
                        intent.putExtra("shoptel", shopEntity.phone);
                        intent.putExtra("areaid", areaid);
                        intent.putExtra("addr", getIntent().getStringExtra("addr"));
                        intent.putExtra("addr_lat", getIntent().getDoubleExtra("addr_lat", 0));
                        intent.putExtra("addr_lng", getIntent().getDoubleExtra("addr_lng", 0));
                        intent.putExtra("addr_id", getIntent().getIntExtra("addr_id", 0));
                        intent.putExtra("shopActiveTitle", getIntent().getStringExtra("shopActiveTitle"));
                        intent.putExtra("shopActivePoint", getIntent().getIntExtra("shopActivePoint", 0));
                        intent.putExtra("shopActiveBackup", getIntent().getStringExtra("shopActiveBackup"));
                        intent.putExtra("shopWxNum", getIntent().getStringExtra("shopWxNum"));
                        intent.putExtra("shopWxImg", getIntent().getStringExtra("shopWxImg"));
                        intent.putExtra("shopPhone", getIntent().getStringExtra("shopPhone"));
                        intent.putExtra("dist", getIntent().getStringExtra("dist"));
                        startActivity(intent);
                    } else {
                        Intent data = new Intent();
                        data.putExtra("areaid", areaid);
                        data.putExtra("shopid", shopid);
                        data.putExtra("cityId", getIntent().getIntExtra("cityId", 0));
                        data.putExtra("shopname", shopEntity.shopName);
                        data.putExtra("shopimg", shopEntity.shopimg);
                        data.putExtra("shopaddr", shopEntity.address);
                        data.putExtra("shoptel", shopEntity.phone);
                        data.putExtra("addr", getIntent().getStringExtra("addr"));
                        data.putExtra("addr_lat", getIntent().getDoubleExtra("addr_lat", 0));
                        data.putExtra("addr_lng", getIntent().getDoubleExtra("addr_lng", 0));
                        data.putExtra("addr_id", getIntent().getIntExtra("addr_id", 0));
                        data.putExtra("shopActiveTitle", getIntent().getStringExtra("shopActiveTitle"));
                        data.putExtra("shopActivePoint", getIntent().getIntExtra("shopActivePoint", 0));
                        data.putExtra("shopActiveBackup", getIntent().getStringExtra("shopActiveBackup"));
                        data.putExtra("shopWxNum", getIntent().getStringExtra("shopWxNum"));
                        data.putExtra("shopWxImg", getIntent().getStringExtra("shopWxImg"));
                        data.putExtra("shopPhone", getIntent().getStringExtra("shopPhone"));
                        data.putExtra("dist", getIntent().getStringExtra("dist"));
                        ShopListActivity.act.setResult(Global.RESULT_OK, data);
                    }
                    ShopListActivity.act.finish();
                }
                finishWithAnimation();

                break;
        }
    }

    private void setBanner() {
        list.clear();
        for (int i = 0; i < shopEntity.imgList.size(); i++) {
            list.add(shopEntity.imgList.get(i));
        }
        bannerItemDetail.setImages(list)
                .setImageLoader(new GlideImageLoaderFourRound())
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        if (shopEntity.imgList != null && shopEntity.imgList.size() > 0 && shopEntity.imgList.size() > position) {
            Utils.imageBrower(this, position, list.toArray(new String[list.size()]));
        }
    }
}
