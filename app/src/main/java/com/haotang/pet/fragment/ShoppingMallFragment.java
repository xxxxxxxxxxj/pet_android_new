package com.haotang.pet.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
//androidx.viewpager.widget.ViewPager
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.ShopMallOrderActivity;
import com.haotang.pet.ShoppingCartActivity;
import com.haotang.pet.adapter.BannerShopMallPwAdapter;
import com.haotang.pet.adapter.RechargeBannerAdapter;
import com.haotang.pet.adapter.ShopMallBannerAdapter;
import com.haotang.pet.adapter.ShopMallCuteDownAdapter;
import com.haotang.pet.adapter.ShopMallGoodsAdapter;
import com.haotang.pet.adapter.ShopMallIconAdapter;
import com.haotang.pet.adapter.ShopMallRecommendAdapter;
import com.haotang.pet.entity.ActivityPage;
import com.haotang.pet.entity.AdDialogDataBean;
import com.haotang.pet.entity.AdLoginEvent;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.entity.FloatIngEvent;
import com.haotang.pet.entity.RechargeBanner;
import com.haotang.pet.entity.ShopMallFragCuteDown;
import com.haotang.pet.entity.ShopMallFragIcon;
import com.haotang.pet.entity.ShopMallFragRecommend;
import com.haotang.pet.entity.ShopMallGoods;
import com.haotang.pet.mall.MallSearchActivity;
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
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.EasyCountDownTextureView;
import com.haotang.pet.view.HeaderGridView;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.MyGridView;
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

//import androidx.viewpager.widget.ViewPager;


/**
 * <p>Title:${type_name}</p>
 * <p>Description:商城首页</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/29 17:55
 */
public class ShoppingMallFragment extends Fragment implements View.OnClickListener, EasyCountDownTextureView.EasyCountDownListener {
    private Activity mActivity;
    private SharedPreferenceUtil spUtil;
    private RelativeLayout rl_shopmallfrag_gwc;
    private TextView tv_shopmallfrag_gwcnum;
    private RelativeLayout rl_shopmallfrag_order;
    private TextView tv_commodity_ordernum;
    private RelativeLayout cet_shopmallfrag;
    private PullToRefreshHeadGridView ptrhgv_shopmallfrag;
    private View header;
    private MProgressDialog pDialog;
    private int page = 1;// 页码
    private int localPage = 1;
    private int pageSize;
    private RelativeLayout rl_rpv_header_shoppingmallfrag;
    private RollPagerView rpv_header_shoppingmallfrag;
    private TextView tv_shoppingmallfrag_rpvnum;
    private MyGridView mgv_shoppingmallfrag_icon;
    private LinearLayout ll_shoppingmallfrag_cutedown;
    private HorizontalScrollView hsv_shoppingmallfrag_cutedown;
    private GridView gv_shoppingmallfrag_cutedown;
    private List<ShopMallGoods> listShopMallGoods = new ArrayList<ShopMallGoods>();
    private List<Banner> listBanner = new ArrayList<Banner>();
    private List<Banner> listBanner1 = new ArrayList<Banner>();
    private List<ShopMallFragIcon> listIcon = new ArrayList<ShopMallFragIcon>();
    private List<ShopMallFragCuteDown> listCuteDown = new ArrayList<ShopMallFragCuteDown>();
    private List<ShopMallFragRecommend> listRecommend = new ArrayList<ShopMallFragRecommend>();
    private ShopMallBannerAdapter shopMallBannerAdapter;
    private ShopMallBannerAdapter shopMallBannerAdapter1;
    private ShopMallIconAdapter<ShopMallFragIcon> shopMallIconAdapter;
    private ShopMallCuteDownAdapter<ShopMallFragCuteDown> shopMallCuteDownAdapter;
    private ShopMallRecommendAdapter<ShopMallFragRecommend> shopMallRecommendAdapter;
    private LinearLayout ll_shoppingmallfrag_recommend;
    private PopupWindow pWin;
    private String[] bannerImgUrls;
    private int itemWidth;
    private MListview mlv_shoppingmallfrag_jkhh;
    private ShopMallGoodsAdapter<ShopMallGoods> shopMallGoodsAdapter;
    private int userMallCartNum;
    private ImageView img_scroll_top;
    private RelativeLayout rl_change_pet;
    private ImageView iv_change_dog;
    private TextView tv_change_pet;
    private boolean isDog = true;
    private boolean isGridScroll;
    private int orderCount;
    private TextView tv_shoppingmallfrag_cutedown;
    private String vipCommodityContent;
    private LinearLayout ll_shopmallorder_nonet;
    private Button btn_shopmallorder_nonet;
    private LinearLayout ll_shoppingmallfrag_recharge;
    private TextView tv_shoppingmallfrag_recharge_title;
    private View shop_bar;
    private HorizontalScrollView hsv_shoppingmallfrag_recharge;
    private GridView gv_shoppingmallfrag_recharge;
    private List<RechargeBanner> listRechargeBanner = new ArrayList<RechargeBanner>();
    private RechargeBannerAdapter<RechargeBanner> rechargeBannerAdapter;
    private ImageView iv_shoppingmallfrag_recharge_img;
    private List<AdDialogDataBean> listAdDialogDataBean = new ArrayList<AdDialogDataBean>();
    private RollPagerView rpv_header_shoppingmallfrag1;
    private List<ActivityPage> localBannerList = new ArrayList<ActivityPage>();
    //文件名称
    String fileName_one = "ad_shopfrag_one.txt";
    String fileName_one_day = "ad_shopfrag_one_day.txt";
    private ObjectAnimator objectAnimator;
    private int petkind = 1;//宠物类型
    private boolean flag;
    private int changePet;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initData(activity);
    }

    private void initData(Activity activity) {
        this.mActivity = activity;
        spUtil = SharedPreferenceUtil.getInstance(mActivity);
        pDialog = new MProgressDialog(mActivity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        return inflater.inflate(R.layout.shoppingmallfragment, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        flag = true;
        findView(view);
        setView();
        setLinster();
        setStatusBar();
        boolean mainfrag_dialog = spUtil.getBoolean("SHOPFRAG_DIALOG", false);
        if (!mainfrag_dialog) {
            spUtil.saveBoolean("SHOPFRAG_DIALOG", true);
            getMainActivity();
        }
        setGetData();
    }

    public void setStatusBar() {
//        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.aD1494F));
//        StatusBarCompat.setStatusBarColor(getActivity(), MainActivity.DEFAULT_COLOR);
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
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setStatusBar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatIngEvent(2));
//        setGetData();
        getCartNum();
        boolean isGetActivity = spUtil.getBoolean("isShopGetActivity", false);
        if (isGetActivity) {
            spUtil.saveBoolean("isShopGetActivity", false);
            getMainActivity();
        }
    }

    private void setGetData() {
        page = 1;
        changePet = spUtil.getInt("changePet", 0);
        //优先判断是否切换过宠物
        if (changePet != 0) {
            if (changePet == 1) {
                isDog = true;
                iv_change_dog.setImageResource(R.drawable.icon_dog_small);
                tv_change_pet.setText("狗狗");
                getData(1, spUtil.getInt("cityId", 0));
            } else if (changePet == 2) {
                iv_change_dog.setImageResource(R.drawable.icon_cat_small);
                tv_change_pet.setText("猫咪");
                isDog = false;
                getData(2, spUtil.getInt("cityId", 0));
            }
        } else if (Utils.checkLogin(mActivity)) {
            rl_change_pet.setClickable(false);
            getPetKind();
        } else {
            getData(petkind, spUtil.getInt("cityId", 0));
        }
        getCartNum();
        getrRecommendData();
        getRechargeBanner();

    }

    private void getPetKind() {
        CommUtil.loginAuto(mActivity, spUtil.getString("cellphone", ""),
                Global.getIMEI(mActivity),
                Global.getCurrentVersion(mActivity),
                spUtil.getInt("userid", 0), 0, 0, autoLoginHandler);
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
                            if (jUser.has("petKinds") && !jUser.isNull("petKinds")) {
                                JSONArray petKinds = jUser.getJSONArray("petKinds");
                                if (petKinds.length() > 0) {
                                    if (petKinds.length() >= 2) {
                                        petkind = 1;
                                        spUtil.saveInt("petkind", 1);
                                    } else {
                                        petkind = petKinds.getInt(0);
                                        spUtil.saveInt("petkind", petKinds.getInt(0));
                                    }
                                    rl_change_pet.setClickable(true);
                                } else {
                                    petkind = 1;
                                    spUtil.saveInt("petkind", 1);
                                    rl_change_pet.setClickable(true);
                                }

                                if (petkind == 1) {
                                    isDog = true;
                                    iv_change_dog.setImageResource(R.drawable.icon_dog_small);
                                    tv_change_pet.setText("狗狗");
                                } else if (petkind == 2) {
                                    iv_change_dog.setImageResource(R.drawable.icon_cat_small);
                                    tv_change_pet.setText("猫咪");
                                    isDog = false;
                                }

                            }
                            if (jUser.has("cityId") && !jUser.isNull("cityId")) {
                                spUtil.saveInt("cityId",
                                        jUser.getInt("cityId"));
                            } else {
                                spUtil.removeData("cityId");
                            }
                            getData(petkind, spUtil.getInt("cityId", 0));
                        }
                    }
                }

            } catch (JSONException e) {
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void getMainActivity() {
        localBannerList.clear();
        CommUtil.getActivityPage(mActivity, spUtil.getInt("nowShopCityId",0), spUtil.getInt("isFirstLogin", 0), 1,Double.parseDouble(spUtil.getString("lat_home","0")),Double.parseDouble(spUtil.getString("lng_home","0")), handlerHomeActivity);
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
                            String tag_shopmallfrag_one = Utils.readFileData(mActivity, fileName_one);
                            Log.e("TAG", "tag_shopmallfrag_one = " + tag_shopmallfrag_one);
                            if (Utils.isStrNull(tag_shopmallfrag_one)) {
                                String[] split = tag_shopmallfrag_one.split(",");
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
                            String tag_shopmallfrag_one_day = Utils.readFileData(mActivity, fileName_one_day);
                            Log.e("TAG", "tag_shopmallfrag_one_day = " + tag_shopmallfrag_one_day);
                            if (Utils.isStrNull(tag_shopmallfrag_one_day)) {
                                String[] split = tag_shopmallfrag_one_day.split(",");
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
                Utils.ActivityPage(mActivity, localBannerList, 2);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void getRechargeBanner() {
        pDialog.showDialog();
        CommUtil.operateBanner(mActivity, 3, operateBannerHandler);
    }

    private AsyncHttpResponseHandler operateBannerHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            processData(new String(responseBody));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            String shop_operate_banner_data = spUtil.getString("SHOP_OPERATE_BANNER_DATA", "");
            if (Utils.isStrNull(shop_operate_banner_data)) {
                processData(shop_operate_banner_data);
            }
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
        }
    };

    private void processData(String result) {
        try {
            JSONObject jobj = new JSONObject(result);
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
                            GlideUtil.loadImg(mActivity, icon, iv_shoppingmallfrag_recharge_img,
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
                    spUtil.saveString("SHOP_OPERATE_BANNER_DATA", result);
                }
            } else {
                if (Utils.isStrNull(msg)) {
                    ToastUtil.showToastShortBottom(mActivity, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showToastShortBottom(mActivity, "数据异常");
        }
        if (listRechargeBanner != null && listRechargeBanner.size() > 0) {
            ll_shoppingmallfrag_recharge.setVisibility(View.VISIBLE);
            int size = listRechargeBanner.size();
            int length = 150;
            DisplayMetrics dm = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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

    private void getCartNum() {
        userMallCartNum = 0;
        orderCount = 0;
        pDialog.showDialog();
        CommUtil.queryOrderAndCart(mActivity, queryOrderAndCartHandler);
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
                        ToastUtil.showToastShortBottom(mActivity, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mActivity, "数据异常");
            }
            if (userMallCartNum > 0) {
                tv_shopmallfrag_gwcnum.setVisibility(View.VISIBLE);
                if (userMallCartNum > 99) {
                    tv_shopmallfrag_gwcnum.setText("99+");
                } else {
                    tv_shopmallfrag_gwcnum.setText(String.valueOf(userMallCartNum));
                }
            } else {
                tv_shopmallfrag_gwcnum.setVisibility(View.GONE);
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
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
        }
    };

    private void findView(View view) {
        Log.e("TAG", "shop");
        shop_bar = view.findViewById(R.id.shop_bar);
        ll_shopmallorder_nonet = (LinearLayout) view.findViewById(R.id.ll_shopmallorder_nonet);
        btn_shopmallorder_nonet = (Button) view.findViewById(R.id.btn_shopmallorder_nonet);
        tv_commodity_ordernum = (TextView) view.findViewById(R.id.tv_commodity_ordernum);
        img_scroll_top = (ImageView) view.findViewById(R.id.img_scroll_top);
        tv_shopmallfrag_gwcnum = (TextView) view.findViewById(R.id.tv_shopmallfrag_gwcnum);
        rl_shopmallfrag_gwc = (RelativeLayout) view.findViewById(R.id.rl_shopmallfrag_gwc);
        rl_shopmallfrag_order = (RelativeLayout) view.findViewById(R.id.rl_shopmallfrag_order);
        cet_shopmallfrag = (RelativeLayout) view.findViewById(R.id.cet_shopmallfrag);
        ptrhgv_shopmallfrag = (PullToRefreshHeadGridView) view.findViewById(R.id.ptrhgv_shopmallfrag);
        rl_change_pet = view.findViewById(R.id.rl_shopmallfrag_change);
        iv_change_dog = view.findViewById(R.id.iv_shopmall_changeicon);
        tv_change_pet = view.findViewById(R.id.tv_shopmall_changename);
        header = LayoutInflater.from(mActivity).inflate(
                R.layout.header_shoppingmall_fragment, null);
        tv_shoppingmallfrag_cutedown = (TextView) header.findViewById(R.id.tv_shoppingmallfrag_cutedown);
        rl_rpv_header_shoppingmallfrag = (RelativeLayout) header.findViewById(R.id.rl_rpv_header_shoppingmallfrag);
        rpv_header_shoppingmallfrag = (RollPagerView) header.findViewById(R.id.rpv_header_shoppingmallfrag);
        tv_shoppingmallfrag_rpvnum = (TextView) header.findViewById(R.id.tv_shoppingmallfrag_rpvnum);
        mgv_shoppingmallfrag_icon = (MyGridView) header.findViewById(R.id.mgv_shoppingmallfrag_icon);
        ll_shoppingmallfrag_cutedown = (LinearLayout) header.findViewById(R.id.ll_shoppingmallfrag_cutedown);
        hsv_shoppingmallfrag_cutedown = (HorizontalScrollView) header.findViewById(R.id.hsv_shoppingmallfrag_cutedown);
        gv_shoppingmallfrag_cutedown = (GridView) header.findViewById(R.id.gv_shoppingmallfrag_cutedown);
        ll_shoppingmallfrag_recommend = (LinearLayout) header.findViewById(R.id.ll_shoppingmallfrag_recommend);
        mlv_shoppingmallfrag_jkhh = (MListview) header.findViewById(R.id.mlv_shoppingmallfrag_jkhh);
        ll_shoppingmallfrag_recharge = (LinearLayout) header.findViewById(R.id.ll_shoppingmallfrag_recharge);
        hsv_shoppingmallfrag_recharge = (HorizontalScrollView) header.findViewById(R.id.hsv_shoppingmallfrag_recharge);
        gv_shoppingmallfrag_recharge = (GridView) header.findViewById(R.id.gv_shoppingmallfrag_recharge);
        tv_shoppingmallfrag_recharge_title = (TextView) header.findViewById(R.id.tv_shoppingmallfrag_recharge_title);
        iv_shoppingmallfrag_recharge_img = (ImageView) header.findViewById(R.id.iv_shoppingmallfrag_recharge_img);
        rpv_header_shoppingmallfrag1 = (RollPagerView) header.findViewById(R.id.rpv_header_shoppingmallfrag1);
    }

    private void setView() {
        tv_shopmallfrag_gwcnum.bringToFront();
        tv_commodity_ordernum.bringToFront();
        img_scroll_top.bringToFront();
        ptrhgv_shopmallfrag.getRefreshableView().addHeaderView(header);
        ptrhgv_shopmallfrag.setMode(PullToRefreshBase.Mode.BOTH);
        listBanner.clear();
        listIcon.clear();
        listCuteDown.clear();
        listRecommend.clear();
        listShopMallGoods.clear();
        listRechargeBanner.clear();
        //重新设置导航栏高度
        ViewGroup.LayoutParams layoutParams = shop_bar.getLayoutParams();
        layoutParams.height = ScreenUtil.getStatusBarHeight(getContext());
        shop_bar.setLayoutParams(layoutParams);

        shopMallGoodsAdapter = new ShopMallGoodsAdapter<ShopMallGoods>(mActivity, listShopMallGoods);
        shopMallGoodsAdapter.clearDeviceList();
        mlv_shoppingmallfrag_jkhh.setAdapter(shopMallGoodsAdapter);

        shopMallBannerAdapter = new ShopMallBannerAdapter(mActivity, listBanner);
        shopMallBannerAdapter.clearDeviceList();
        rpv_header_shoppingmallfrag.setAdapter(shopMallBannerAdapter);

        shopMallBannerAdapter1 = new ShopMallBannerAdapter(mActivity, listBanner1);
        shopMallBannerAdapter1.clearDeviceList();
        rpv_header_shoppingmallfrag1.setAdapter(shopMallBannerAdapter1);

        shopMallIconAdapter = new ShopMallIconAdapter<ShopMallFragIcon>(mActivity, listIcon);
        shopMallIconAdapter.clearDeviceList();
        mgv_shoppingmallfrag_icon.setAdapter(shopMallIconAdapter);

        shopMallCuteDownAdapter = new ShopMallCuteDownAdapter<ShopMallFragCuteDown>(mActivity, listCuteDown);
        shopMallCuteDownAdapter.clearDeviceList();
        gv_shoppingmallfrag_cutedown.setAdapter(shopMallCuteDownAdapter);

        shopMallRecommendAdapter = new ShopMallRecommendAdapter<ShopMallFragRecommend>(mActivity, listRecommend);
        shopMallRecommendAdapter.clearDeviceList();
        ptrhgv_shopmallfrag.setAdapter(shopMallRecommendAdapter);

        rechargeBannerAdapter = new RechargeBannerAdapter<RechargeBanner>(mActivity, listRechargeBanner);
        rechargeBannerAdapter.clearDeviceList();
        gv_shoppingmallfrag_recharge.setAdapter(rechargeBannerAdapter);
    }

    private void writeData() {
        Utils.setText(tv_shoppingmallfrag_cutedown, vipCommodityContent, "", View.VISIBLE, View.INVISIBLE);
        if (listShopMallGoods != null && listShopMallGoods.size() > 0) {
            mlv_shoppingmallfrag_jkhh.setVisibility(View.VISIBLE);
            shopMallGoodsAdapter.setData(listShopMallGoods);
        } else {
            mlv_shoppingmallfrag_jkhh.setVisibility(View.GONE);
        }
        if (listBanner != null && listBanner.size() > 0) {
            String text = 1 + "/" + listBanner.size() + "全部";
            SpannableString ss = new SpannableString(text);
            ss.setSpan(new ForegroundColorSpan(mActivity.getResources()
                            .getColor(R.color.aCD484D)), 0, String.valueOf(1).length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_shoppingmallfrag_rpvnum.setText(ss);
            bannerImgUrls = new String[listBanner.size()];
            for (int i = 0; i < this.listBanner.size(); i++) {
                Banner banner = this.listBanner.get(i);
                if (banner != null) {
                    if (banner.pic != null && !TextUtils.isEmpty(banner.pic)) {
                        bannerImgUrls[i] = banner.picDomain;
                    }
                }
            }
            if (listBanner.size() > 1) {
                rpv_header_shoppingmallfrag.setHintView(null);
            }
            rl_rpv_header_shoppingmallfrag.setVisibility(View.VISIBLE);
            shopMallBannerAdapter.setData(listBanner);
        } else {
            rl_rpv_header_shoppingmallfrag.setVisibility(View.GONE);
        }

        if (listBanner1 != null && listBanner1.size() > 0) {
            rpv_header_shoppingmallfrag1.setVisibility(View.VISIBLE);
            shopMallBannerAdapter1.setData(listBanner1);
        } else {
            rpv_header_shoppingmallfrag1.setVisibility(View.GONE);
        }

        if (listIcon != null && listIcon.size() > 0) {
            mgv_shoppingmallfrag_icon.setVisibility(View.VISIBLE);
            shopMallIconAdapter.setData(listIcon);
        } else {
            mgv_shoppingmallfrag_icon.setVisibility(View.GONE);
        }

        if (listCuteDown != null && listCuteDown.size() > 0) {
            ll_shoppingmallfrag_cutedown.setVisibility(View.VISIBLE);
            int size = listCuteDown.size();
            int length = 105;
            DisplayMetrics dm = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int gridviewWidth = (int) (size * (length + 10) * density);
            itemWidth = (int) (length * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
            gv_shoppingmallfrag_cutedown.setLayoutParams(params); // 重点
            gv_shoppingmallfrag_cutedown.setColumnWidth(itemWidth); // 重点
            gv_shoppingmallfrag_cutedown.setHorizontalSpacing(25); // 间距
            gv_shoppingmallfrag_cutedown.setStretchMode(GridView.NO_STRETCH);
            gv_shoppingmallfrag_cutedown.setGravity(Gravity.CENTER);// 位置居中
            gv_shoppingmallfrag_cutedown.setNumColumns(size); // 重点
            shopMallCuteDownAdapter.setData(listCuteDown);
        } else {
            ll_shoppingmallfrag_cutedown.setVisibility(View.GONE);
        }

        if (listRecommend != null && listRecommend.size() > 0) {
            ll_shoppingmallfrag_recommend.setVisibility(View.VISIBLE);
            if (listRecommend.size() == 1) {
                ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(0);
                if (shopMallFragRecommend != null) {
                    if (shopMallFragRecommend.isOther()) {
                        ll_shoppingmallfrag_recommend.setVisibility(View.GONE);
                    }
                }
            }
            shopMallRecommendAdapter.setData(listRecommend);
        } else {
            if (page == 1) {
                listRecommend.add(new ShopMallFragRecommend("追加", 0, 0, "", 0, 0, true));
                shopMallRecommendAdapter.setData(listRecommend);
            }
            ll_shoppingmallfrag_recommend.setVisibility(View.GONE);
        }
    }

    private void setLinster() {
        btn_shopmallorder_nonet.setOnClickListener(this);
        img_scroll_top.setOnClickListener(this);
        cet_shopmallfrag.setOnClickListener(this);
        rl_shopmallfrag_gwc.setOnClickListener(this);
        rl_shopmallfrag_order.setOnClickListener(this);
        tv_shoppingmallfrag_rpvnum.setOnClickListener(this);
        rl_change_pet.setOnClickListener(this);
        ptrhgv_shopmallfrag
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshBase refreshView) {
                        PullToRefreshBase.Mode mode = refreshView
                                .getCurrentMode();
                        if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {// 下拉刷新
                            setGetData();
                        } else {// 上拉加载更多
                            page++;
                            getrRecommendData();
                        }
                    }
                });
        rpv_header_shoppingmallfrag.getViewPager().setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        String text = (position + 1) + "/" + listBanner.size()
                                + "全部";
                        SpannableString ss = new SpannableString(text);
                        ss.setSpan(new ForegroundColorSpan(mActivity
                                        .getResources().getColor(R.color.aCD484D)), 0,
                                String.valueOf(position + 1).length(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        tv_shoppingmallfrag_rpvnum.setText(ss);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });
        shopMallBannerAdapter.setOnItemClickListener(new ShopMallBannerAdapter.OnBannerItemClickListener() {
            @Override
            public void OnBannerItemClick(int position) {
                if (listBanner.size() > 0 && listBanner.size() > position) {
                    Banner banner = listBanner.get(position);
                    if (banner != null) {
                        switch (position){
                            case 0:
                                UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_BannerOne);
                                break;
                            case 1:
                                UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_BannerTwo);
                                break;
                            case 2:
                                UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_BannerThree);
                                break;
                            case 3:
                                UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_BannerFour);
                                break;
                        }
                        Utils.goService(mActivity, banner.point,
                                banner.backup);

                    }
                }
            }
        });
        shopMallBannerAdapter1.setOnItemClickListener(new ShopMallBannerAdapter.OnBannerItemClickListener() {
            @Override
            public void OnBannerItemClick(int position) {
                if (listBanner1.size() > 0 && listBanner1.size() > position) {
                    Banner banner = listBanner1.get(position);
                    if (banner != null) {
                        Utils.goService(mActivity, banner.point,
                                banner.backup);
                    }
                }
            }
        });

        mgv_shoppingmallfrag_icon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listIcon.size() > 0 && listIcon.size() > position) {
                    ShopMallFragIcon shopMallFragIcon = listIcon.get(position);
                    if (shopMallFragIcon != null) {
                        Utils.goService(mActivity, shopMallFragIcon.getPoint(),
                                shopMallFragIcon.getBackup());
                    }
                    switch (position) {
                        case 0:
                            UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_icon1);
                            break;
                        case 1:
                            UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_icon2);
                            break;
                        case 2:
                            UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_icon3);
                            break;
                        case 3:
                            UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_icon4);
                            break;
                        case 4:
                            UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_icon5);
                            break;
                    }
                }
            }
        });
        ptrhgv_shopmallfrag.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 0 && firstVisibleItem <= 10) {
                    isGridScroll = false;
                    img_scroll_top.setVisibility(View.GONE);
                } else {
                    isGridScroll = true;
                    img_scroll_top.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getrRecommendData() {
        if (page == 1) {
            listRecommend.clear();
            shopMallRecommendAdapter.clearDeviceList();
        }
        pDialog.showDialog();
        CommUtil.getrRecommendData(mActivity, 0, page, 0, getrRecommendDataHandler);
    }

    private AsyncHttpResponseHandler getrRecommendDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            ptrhgv_shopmallfrag.onRefreshComplete();
            pDialog.closeDialog();
            processData1(new String(responseBody));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            String shop_recommend_data = spUtil.getString("SHOP_RECOMMEND_DATA", "");
            if (Utils.isStrNull(shop_recommend_data)) {
                processData1(shop_recommend_data);
            }
            ptrhgv_shopmallfrag.onRefreshComplete();
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
        }
    };

    private void processData1(String result) {
        try {
            JSONObject jobj = new JSONObject(result);
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
                                    ToastUtil.showToastShortBottom(mActivity, "没有更多数据了");
                                }
                            }
                        } else {
                            if (page > 1) {
                                ToastUtil.showToastShortBottom(mActivity, "没有更多数据了");
                            }
                        }
                    } else {
                        if (page > 1) {
                            ToastUtil.showToastShortBottom(mActivity, "没有更多数据了");
                        }
                    }
                    spUtil.saveString("SHOP_RECOMMEND_DATA", result);
                } else {
                    if (page > 1) {
                        ToastUtil.showToastShortBottom(mActivity, "没有更多数据了");
                    }
                }
            } else {
                if (Utils.isStrNull(msg)) {
                    ToastUtil.showToastShortBottom(mActivity, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showToastShortBottom(mActivity, "数据异常");
        }
        writeData();
    }

    private void getData(int petkind, int cityId) {
        listShopMallGoods.clear();
        shopMallGoodsAdapter.clearDeviceList();
        listBanner.clear();
        listBanner1.clear();
        shopMallBannerAdapter.clearDeviceList();
        listIcon.clear();
        shopMallIconAdapter.clearDeviceList();
        listCuteDown.clear();
        shopMallCuteDownAdapter.clearDeviceList();
        pDialog.showDialog();
        CommUtil.mallHomePage(mActivity, cityId, spUtil.getInt("isFirstLogin", 0), petkind, mallHomePageHandler);
    }

    private AsyncHttpResponseHandler mallHomePageHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            processData2(new String(responseBody));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
            ll_shopmallorder_nonet.setVisibility(View.VISIBLE);
            img_scroll_top.setVisibility(View.GONE);
            ptrhgv_shopmallfrag.setVisibility(View.GONE);
            String shop_homepage_data = spUtil.getString("SHOP_HOMEPAGE_DATA", "");
            if (Utils.isStrNull(shop_homepage_data)) {
                processData2(shop_homepage_data);
            }
        }
    };

    private void processData2(String result) {
        ll_shopmallorder_nonet.setVisibility(View.GONE);
        img_scroll_top.setVisibility(View.VISIBLE);
        ptrhgv_shopmallfrag.setVisibility(View.VISIBLE);
        try {
            JSONObject jobj = new JSONObject(result);
            int resultCode = jobj.getInt("code");
            String msg = jobj.getString("msg");
            if (0 == resultCode) {
                JSONObject jdata = jobj.getJSONObject("data");
                if (jdata != null) {
                    if (jdata.has("vipCommodityContent") && !jdata.isNull("vipCommodityContent")) {
                        vipCommodityContent = jdata.getString("vipCommodityContent");
                    }
                    if (jdata.has("cityId") && !jdata.isNull("cityId")) {
                        spUtil.saveInt("cityId", jdata.getInt("cityId"));
                    }
                    if (jdata.has("mallHomePageSubjectList") && !jdata.isNull("mallHomePageSubjectList")) {
                        JSONArray jarrmallHomePageSubjectList = jdata.getJSONArray("mallHomePageSubjectList");
                        if (jarrmallHomePageSubjectList.length() > 0) {
                            listShopMallGoods.clear();
                            shopMallGoodsAdapter.clearDeviceList();
                            for (int i = 0; i < jarrmallHomePageSubjectList.length(); i++) {
                                listShopMallGoods.add(ShopMallGoods.json2Entity(jarrmallHomePageSubjectList
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("banner") && !jdata.isNull("banner")) {
                        JSONArray jarrbanner = jdata.getJSONArray("banner");
                        if (jarrbanner.length() > 0) {
                            listBanner.clear();
                            shopMallBannerAdapter.clearDeviceList();
                            for (int i = 0; i < jarrbanner.length(); i++) {
                                listBanner.add(Banner.json2Entity(jarrbanner
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("banner1") && !jdata.isNull("banner1")) {
                        JSONArray jarrbanner1 = jdata.getJSONArray("banner1");
                        if (jarrbanner1.length() > 0) {
                            listBanner1.clear();
                            shopMallBannerAdapter1.clearDeviceList();
                            for (int i = 0; i < jarrbanner1.length(); i++) {
                                listBanner1.add(Banner.json2Entity(jarrbanner1
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("icons") && !jdata.isNull("icons")) {
                        JSONArray jarricons = jdata.getJSONArray("icons");
                        if (jarricons.length() > 0) {
                            listIcon.clear();
                            shopMallIconAdapter.clearDeviceList();
                            for (int i = 0; i < jarricons.length(); i++) {
                                listIcon.add(ShopMallFragIcon.json2Entity(jarricons
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (jdata.has("vipCommodityList") && !jdata.isNull("vipCommodityList")) {
                        JSONArray jarrvipCommodityList = jdata.getJSONArray("vipCommodityList");
                        if (jarrvipCommodityList.length() > 0) {
                            listCuteDown.clear();
                            shopMallCuteDownAdapter.clearDeviceList();
                            for (int i = 0; i < jarrvipCommodityList.length(); i++) {
                                listCuteDown.add(ShopMallFragCuteDown.json2Entity(jarrvipCommodityList
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    spUtil.saveString("SHOP_HOMEPAGE_DATA", result);
                }
            } else {
                if (Utils.isStrNull(msg)) {
                    ToastUtil.showToastShortBottom(mActivity, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showToastShortBottom(mActivity, "数据异常");
        }
        writeData();
    }

    public void startAnimation(float start, float end) {
        objectAnimator = ObjectAnimator.ofFloat(iv_change_dog, "rotation", start, end);
        objectAnimator.setDuration(200);
        objectAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shopmallorder_nonet:
                setGetData();
                break;
            case R.id.rl_shopmallfrag_change:
                UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_ChangePet);
                if (flag) {
                    startAnimation(0f, 180f);
                    flag = false;
                } else {
                    startAnimation(180f, 360f);
                    flag = true;
                }
                if (isDog) {
                    isDog = false;
                    tv_change_pet.setText("猫咪");
                    petkind = 2;
                    getData(petkind, spUtil.getInt("cityId", 0));
                    spUtil.saveInt("changePet", 2);
                } else {
                    isDog = true;
                    tv_change_pet.setText("狗狗");
                    petkind = 1;
                    getData(petkind, spUtil.getInt("cityId", 0));
                    spUtil.saveInt("changePet", 1);
                }
                break;
            case R.id.img_scroll_top:
                if (isGridScroll) {
                    HeaderGridView refreshableView = ptrhgv_shopmallfrag.getRefreshableView();
                    if (!(refreshableView).isStackFromBottom()) {
                        refreshableView.setStackFromBottom(true);
                    }
                    refreshableView.setStackFromBottom(false);
                }
                break;
            case R.id.cet_shopmallfrag:
                startActivity(new Intent(mActivity, MallSearchActivity.class));
                getStatistics(Global.ServerEventID.choose_shopmall_page, Global.ServerEventID.click_search_shopmall);
                UmengStatistics.UmengEventStatistics(getContext(), Global.UmengEventID.click_MallFrag_Search);
                break;
            case R.id.rl_shopmallfrag_gwc:
                if (Utils.checkLogin(mActivity)) {
                    startActivity(new Intent(mActivity, ShoppingCartActivity.class));
                } else {
                    startActivity(new Intent(mActivity, LoginNewActivity.class).putExtra(
                            "previous", Global.AD_LOGIN_SHOP));
                }
                getStatistics(Global.ServerEventID.choose_shopmall_page, Global.ServerEventID.click_shoppingcart_shopmall);
                break;
            case R.id.rl_shopmallfrag_order:
                if (Utils.checkLogin(mActivity)) {
                    startActivity(new Intent(mActivity, ShopMallOrderActivity.class));
                } else {
                    startActivity(new Intent(mActivity, LoginNewActivity.class).putExtra(
                            "previous", Global.AD_LOGIN_SHOP));
                }
                break;
            case R.id.tv_shoppingmallfrag_rpvnum:
                showPopPhoto();
                break;
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
            tv_pw_main_activitynum.setText(listBanner.size() + "个活动");
            lv_pw_main.setAdapter(new BannerShopMallPwAdapter<Banner>(mActivity,
                    listBanner));
            pWin = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            pWin.setFocusable(true);
            pWin.setWidth(Utils.getDisplayMetrics(mActivity)[0]);
            pWin.showAtLocation(view, Gravity.CENTER, 0, 0);
            lv_pw_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (listBanner != null && listBanner.size() > 0
                            && listBanner.size() > position) {
                        Banner banner = listBanner.get(position);
                        if (banner != null) {
                            boolean goService = Utils.goService(mActivity, banner.point,
                                    banner.backup);
                            if (!goService) {
                                Utils.imageBrower(mActivity, position,
                                        bannerImgUrls);
                            }
                        }
                    }
                }
            });
            ib_pw_main_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    pWin.dismiss();
                    pWin = null;
                }
            });
        }
    }

    @Override
    public void onCountDownStart() {

    }

    @Override
    public void onCountDownTimeError() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AdLoginEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.getPosition() == 2) {
                getMainActivity();
            }
        }
    }

    @Override
    public void onCountDownStop(long millisInFuture) {

    }

    @Override
    public void onCountDownCompleted() {
        ToastUtil.showToastShortBottom(mActivity, "秒杀倒计时结束");
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
