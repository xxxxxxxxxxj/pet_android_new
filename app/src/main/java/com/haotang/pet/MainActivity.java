package com.haotang.pet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.entity.BottomTabBean;
import com.haotang.pet.entity.FloatIngEvent;
import com.haotang.pet.fragment.MTabContent;
import com.haotang.pet.fragment.MainNewFragment;
import com.haotang.pet.fragment.MyFragment;
import com.haotang.pet.fragment.OrderFragment;
import com.haotang.pet.fragment.ShoppingMallFragment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.updateapputil.Callback;
import com.haotang.pet.updateapputil.ConfirmDialog;
import com.haotang.pet.updateapputil.DownloadAppUtils;
import com.haotang.pet.updateapputil.DownloadProgressDialog;
import com.haotang.pet.updateapputil.UpdateAppEvent;
import com.haotang.pet.updateapputil.UpdateUtil;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GetDeviceId;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.DragView;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseFragmentActivity {
    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");
    private LayoutInflater mInflater;
    public TabHost mTabHost;
    private FragmentManager fm;
    private MainNewFragment mainFragment;
    private FragmentTransaction ft;
    private ShoppingMallFragment shoppingMallFragment;
    private MyFragment myFragment;
    public OrderFragment orderFragment;
    private MReceiver receiver;
    public static int[] petServices = new int[4];
    private boolean isFirst = true;
    private String tabTag = "main";
    private double lat;
    private double lng;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private View vFlagmain;
    private View vFlagShoppingMall;
    private View vFlagmemb;
    private static View vFlagmy;
    private ImageView ivTabMain;
    private ImageView ivTabShoppingMall;
    private ImageView ivTabMemb;
    private ImageView ivTabMy;
    private TextView tvTabMain;
    private TextView tvTabShoppingMall;
    private TextView tvTabMemb;
    private TextView tvTabMy;
    private long exitTime = 0;
    private int mypetId;
    private int index;
    private DragView iv_main_wegit;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    public final static int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    //文件名称
    String fileName = "floatingposition.txt";
    private String floatingBackup;
    private int floatingPoint;
    private String pic;
    private int floatIngPosition = 1;
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    static final String[] LOCATIONGPS = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private DownloadProgressDialog progressDialog;
    private String latestVersion;
    private String downloadPath;
    private String versionHint;
    private int isUpgrade;
    private boolean isShow;
    private String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<BottomTabBean> bottomTabList = new ArrayList<BottomTabBean>();
    private int mallRedPoint;
    private int nToBeComment;
    private Drawable drawableMainNomal;
    private Drawable drawableMainPress;
    private Drawable drawableMallNomal;
    private Drawable drawableMallPress;
    private Drawable drawableCircleNomal;
    private Drawable drawableCirclePress;
    private Drawable drawableMyNomal;
    private Drawable drawableMyPress;

    private Handler handler;

    private static class MyHandler extends Handler{
        MainActivity mainActivityWeakReference;
        public MyHandler(MainActivity activity) {
            super();
            mainActivityWeakReference = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mainActivityWeakReference;
            switch (msg.what) {
                case Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("shoppingmall");
                    break;
                case Global.PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("shoppingmall");
                    break;
                case Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT:
                    mainActivity.mTabHost.setCurrentTabByTag("order");
                    break;
                case Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("main");
                    break;
                case Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("main");
                    mainActivity.mLocationClient.start();
                    break;
                case Global.PRE_EVALUATEOVER_BACK_MAIN:
                    mainActivity.mTabHost.setCurrentTabByTag("main");
                    break;
                case Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("shoppingmall");
                    mainActivity.mLocationClient.start();
                    break;
                case Global.PRE_ORDER_LIST_TO_MAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("main");
                    break;
                case Global.PETADD_BACK_PETINFO_BACK_MY:
                    mainActivity.mTabHost.setCurrentTabByTag("my");
                    break;
                case Global.DELETEPET_TO_UPDATEUSERINFO:
                    mainActivity.autoLogin(mainActivity.lat, mainActivity.lng);
                    break;
                case Global.PAYSUCCESS_TO_MYFRAGMENT:
                    mainActivity.mTabHost.setCurrentTabByTag("my");
                    break;
                case Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY:
                    mainActivity.mTabHost.setCurrentTabByTag("my");
                    break;
                case Global.GROWTH_TO_USERMEMBERFRAGMENT:
                    mainActivity.mTabHost.setCurrentTabByTag("member");
                    break;
                case Global.CARDS_TO_MAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("main");
                    break;
                case Global.PAYSUCCESSNEWTO_MALLMAINACTIVITY:
                    mainActivity.mTabHost.setCurrentTabByTag("shoppingmall");
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FloatIngEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            floatIngPosition = event.getPosition();
            Utils.writeFileData(MainActivity.this, fileName, floatIngPosition + ",");
            if (floatIngPosition == 4) {
                iv_main_wegit.setHaveStatusBar(true);
            } else {
                iv_main_wegit.setHaveStatusBar(false);
            }
            iv_main_wegit.setVisibility(View.GONE);
            CommUtil.floatingInfo(MainActivity.this, 0, spUtil.getInt("isFirstLogin", 0), floatIngPosition, floatingInfoHandler);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isWindow = true;
        super.onCreate(savedInstanceState);
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        Log.e("TAG", "channel = " + channel);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.main);
        MobclickAgent.setDebugMode(true);
        handler = new MyHandler(this);
        String deviceId = GetDeviceId.readDeviceID(mContext);
        if (!Utils.isStrNull(deviceId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermisson();
            } else {
                GetDeviceId.saveUniqueId(mContext);
            }
        }
        getLatestVersion();
        initBD();

        getIndex();
        mInflater = LayoutInflater.from(this);
        iv_main_wegit = (DragView) findViewById(R.id.iv_main_wegit);
        iv_main_wegit.setOnParamsListener(new DragView.OnParamsListener() {
            @Override
            public void OnParams(int left, int top, int right, int bottom) {
                Log.e("TAG", "my left = " + left + ",top = " + top + ",right = " + right + ",bottom = " + bottom);
                if (floatIngPosition == 4) {
                    spUtil.saveInt("FLOATING_TOP", top - iv_main_wegit.getStatusBarHeight());
                    spUtil.saveInt("FLOATING_BOTTOM", bottom - iv_main_wegit.getStatusBarHeight());
                } else {
                    spUtil.saveInt("FLOATING_TOP", top);
                    spUtil.saveInt("FLOATING_BOTTOM", bottom);
                }
                spUtil.saveInt("FLOATING_LEFT", left);
                spUtil.saveInt("FLOATING_RIGHT", right);

            }
        });
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        spUtil.saveBoolean("guide", true);
        TabHost.OnTabChangeListener tabChangeListener = new OnTabChangeListener()
        {

            @Override
            public void onTabChanged(String tabId) {
                getFragment();
                tabTag = tabId;
                if (tabId.equals("main")) {
                    spUtil.saveBoolean("selectMain", true);
                    addFragment(mainFragment, new MainNewFragment(), "main");
                    vFlagmain.setVisibility(View.GONE);
                    setImg(0);
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_nav1);
                } else if (tabId.equals("order")) {
                    addFragment(orderFragment, new OrderFragment(), "order");
                    vFlagmemb.setVisibility(View.GONE);
                    setImg(2);
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_nav3);
                } else if (tabId.equals("shoppingmall")) {
                    addFragment(shoppingMallFragment, new ShoppingMallFragment(), "shoppingmall");
                    vFlagShoppingMall.setVisibility(View.GONE);
                    setImg(1);
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_nav2);
                } else {
                    addFragment(myFragment, new MyFragment(), "my");
                    vFlagmy.setVisibility(View.GONE);
                    setImg(3);
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_HomePage_nav4);
                }
                ft.commitAllowingStateLoss();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
                        ActivityCompat.requestPermissions((Activity) MainActivity.this, LOCATIONGPS, BAIDU_READ_PHONE_STATE);
                    }
                }
            }

        };
        mTabHost.setOnTabChangedListener(tabChangeListener);
        addTab("main", R.drawable.tab_main, "首页");
        addTab("shoppingmall", R.drawable.tab_order, "商城");
        addTab("order", R.drawable.tab_knowledge, "订单");
        addTab("my", R.drawable.tab_my, "我的");
        initReceiver();
        int previous = getIntent().getIntExtra("previous", 0);
        int orderid = getIntent().getIntExtra("orderid", 0);
        if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_COUPON) {
            Intent intent = new Intent(this, MyCouponNewActivity.class);
            intent.putExtra("orderid", orderid);
            startActivity(intent);
        }
        iv_main_wegit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iv_main_wegit.isDrag()) {
                    Utils.goService(MainActivity.this, floatingPoint, floatingBackup);
                }
            }
        });
        try {
            if (!spUtil.getBoolean("GOTOMARKET_DIALOG_TRUE", false)) {//先判断是否点击跳转到应用市场
                //再判断是否点击暂时没有
                if (spUtil.getBoolean("GOTOMARKET_DIALOG_FALSE", false)) {
                    //再判断距离上一次点击暂时没有是否大于10天且判断打开次数是否是5次
                    String gotomarket_dialog_time = spUtil.getString("GOTOMARKET_DIALOG_TIME", "");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String format = df.format(new Date());// new Date()为获取当前系统时间
                    if (spUtil.getInt("OPEN_NUM", 0) == 5 && Utils.daysBetween(gotomarket_dialog_time, format) >= 10) {
                        //弹出
                        Utils.goMarketDialog(this);
                    }
                } else {
                    //再判断打开次数是否是5次
                    if (spUtil.getInt("OPEN_NUM", 0) == 5) {
                        //弹出
                        Utils.goMarketDialog(this);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getAccountCenter();
        try {
            Uri uri = getIntent().getData();
            if (uri != null) {
                // 完整的url信息
                String url = uri.toString();
                Log.e("TAG", "url:" + uri);

                // scheme部分
                String scheme = uri.getScheme();
                Log.e("TAG", "scheme:" + scheme);

                // host部分
                String host = uri.getHost();
                Log.e("TAG", "host:" + host);

                // port部分
                int port = uri.getPort();
                Log.e("TAG", "port:" + port);

                // 访问路劲
                String path = uri.getPath();
                Log.e("TAG", "path:" + path);

                List<String> pathSegments = uri.getPathSegments();

                // Query部分
                String query = uri.getQuery();
                Log.e("TAG", "query:" + query);

                //获取指定参数值
                String point1 = uri.getQueryParameter("point");
                Log.e("TAG", "point1:" + point1);
                String backup = uri.getQueryParameter("backup");
                Log.e("TAG", "backup:" + backup);
                Utils.goService(this, Integer.parseInt(point1), backup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    GetDeviceId.saveUniqueId(mContext);
                } else {
                    ToastUtil.showToastLong(this, "请打开所需权限");
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    UpdateUtil.updateApk(MainActivity.this,
                            downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                } else {
                    ToastUtil.showToastLong(this, "请打开存储权限");
                }
                break;
            default:
                break;
        }
    }

    private AsyncHttpResponseHandler floatingInfoHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Log.e("TAG", "浮窗" + new String(responseBody));
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject jdata = object.getJSONObject("data");
                        if (jdata.has("backup") && !jdata.isNull("backup")) {// 实时动态
                            floatingBackup = jdata.getString("backup");
                        }
                        if (jdata.has("pic") && !jdata.isNull("pic")) {// 实时动态
                            pic = jdata.getString("pic");
                            if (Utils.isStrNull(pic)) {
                                if (spUtil.getInt("FLOATING_LEFT", -1) >= 0 && spUtil.getInt("FLOATING_TOP", -1) >= 0 &&
                                        spUtil.getInt("FLOATING_RIGHT", -1) >= 0 && spUtil.getInt("FLOATING_BOTTOM", -1) >= 0) {
                                    String floating_index = Utils.readFileData(MainActivity.this, fileName);
                                    Log.e("TAG", "floating_index = " + floating_index);
                                    if (Utils.isStrNull(floating_index)) {
                                        String[] split = floating_index.split(",");
                                        Log.e("TAG", "split.length = " + split.length);
                                        if (split != null && split.length >= 2) {
                                            String s = split[split.length - 2];
                                            if ((floatIngPosition == 4 && (Integer.parseInt(s) == 1 || Integer.parseInt(s) ==
                                                    2 || Integer.parseInt(s) == 3))) {
                                                Log.e("TAG", "其他到4");
                                                iv_main_wegit.layout(spUtil.getInt("FLOATING_LEFT", -1),
                                                        spUtil.getInt("FLOATING_TOP", -1) + iv_main_wegit.getStatusBarHeight(), spUtil.getInt("FLOATING_RIGHT", -1),
                                                        spUtil.getInt("FLOATING_BOTTOM", -1) + iv_main_wegit.getStatusBarHeight());
                                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(iv_main_wegit.getWidth(), iv_main_wegit.getHeight());
                                                layoutParams.setMargins(spUtil.getInt("FLOATING_LEFT", -1), spUtil.getInt("FLOATING_TOP", -1) + iv_main_wegit.getStatusBarHeight(), 0, 0);
                                                iv_main_wegit.setLayoutParams(layoutParams);
                                            }
                                            if ((Integer.parseInt(s) == 4 &&
                                                    (floatIngPosition == 1 || floatIngPosition == 2 || floatIngPosition == 3))) {
                                                Log.e("TAG", "4到其他");
                                                iv_main_wegit.layout(spUtil.getInt("FLOATING_LEFT", -1),
                                                        spUtil.getInt("FLOATING_TOP", -1), spUtil.getInt("FLOATING_RIGHT", -1),
                                                        spUtil.getInt("FLOATING_BOTTOM", -1));
                                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(iv_main_wegit.getWidth(), iv_main_wegit.getHeight());
                                                layoutParams.setMargins(spUtil.getInt("FLOATING_LEFT", -1), spUtil.getInt("FLOATING_TOP", -1), 0, 0);
                                                iv_main_wegit.setLayoutParams(layoutParams);
                                            }
                                        } else {
                                            iv_main_wegit.layout(spUtil.getInt("FLOATING_LEFT", -1),
                                                    spUtil.getInt("FLOATING_TOP", -1), spUtil.getInt("FLOATING_RIGHT", -1),
                                                    spUtil.getInt("FLOATING_BOTTOM", -1));
                                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(iv_main_wegit.getWidth(), iv_main_wegit.getHeight());
                                            layoutParams.setMargins(spUtil.getInt("FLOATING_LEFT", -1), spUtil.getInt("FLOATING_TOP", -1), 0, 0);
                                            iv_main_wegit.setLayoutParams(layoutParams);
                                        }
                                    } else {
                                        iv_main_wegit.layout(spUtil.getInt("FLOATING_LEFT", -1),
                                                spUtil.getInt("FLOATING_TOP", -1), spUtil.getInt("FLOATING_RIGHT", -1),
                                                spUtil.getInt("FLOATING_BOTTOM", -1));
                                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(iv_main_wegit.getWidth(), iv_main_wegit.getHeight());
                                        layoutParams.setMargins(spUtil.getInt("FLOATING_LEFT", -1), spUtil.getInt("FLOATING_TOP", -1), 0, 0);
                                        iv_main_wegit.setLayoutParams(layoutParams);
                                    }
                                }
                                iv_main_wegit.setVisibility(View.VISIBLE);
                                iv_main_wegit.bringToFront();
                                if (!pic.startsWith("http://")
                                        && !pic.startsWith("https://")) {
                                    pic = CommUtil.getStaticUrl() + pic;
                                }
                                if(!Utils.isDestroy(MainActivity.this)){
                                    Glide.with(MainActivity.this)
                                            .load(pic)
                                            .into(new SimpleTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    int width = resource.getIntrinsicWidth();
                                                    int height = resource.getIntrinsicHeight();
                                                    iv_main_wegit.setImageWidth(width);
                                                    iv_main_wegit.setImageHeight(height);
                                                    GlideUtil.loadImg(MainActivity.this, pic, iv_main_wegit, R.drawable.icon_production_default,
                                                            width, height);
                                                }
                                            });
                                }
                            }
                        }
                        if (jdata.has("point") && !jdata.isNull("point")) {// 实时动态
                            floatingPoint = jdata.getInt("point");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void checkPermisson() {
        boolean flag = true;//默认全部被申请过
        for (int i = 0; i < permissions.length; i++) {//只要有一个没有申请成功
            if (!(ActivityCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_GRANTED)) {
                flag = false;
            }
        }
        if (!flag) {
            //动态申请权限
            ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_PHONE_STATE);
        } else {
            GetDeviceId.saveUniqueId(mContext);
        }
    }

    private void getFragment() {
        fm = getSupportFragmentManager();
        mainFragment = (MainNewFragment) fm.findFragmentByTag("main");
        orderFragment = (OrderFragment) fm
                .findFragmentByTag("order");
        shoppingMallFragment = (ShoppingMallFragment) fm.findFragmentByTag("shoppingmall");
        myFragment = (MyFragment) fm.findFragmentByTag("my");
        ft = fm.beginTransaction();
        detachFragment(mainFragment, "main");
        detachFragment(orderFragment, "order");
        detachFragment(shoppingMallFragment, "shoppingmall");
        detachFragment(myFragment, "my");
    }

    private void detachFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            if (tag.equals("my")) {
//                spUtil.saveBoolean("isRefreshMyFrag", true);
                ft.hide(fragment);
            } else {
//                spUtil.saveBoolean("isRefreshMyFrag", false);
                ft.hide(fragment);
            }
        }
    }

    private void addFragment(Fragment fragment, Fragment newfragment, String tag) {
        if (fragment == null) {
            ft.add(android.R.id.tabcontent, newfragment, tag);
        } else {
            if (tag.equals("my")) {
//                spUtil.saveBoolean("isRefreshMyFrag", true);
                ft.show(fragment);
            } else {
//                spUtil.saveBoolean("isRefreshMyFrag", false);
                ft.show(fragment);
            }
        }
    }

    private void addTab(String tag, int icon, String name) {
        TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(getTabView(icon, name, tag));
        tabSpec.setContent(new MTabContent(getBaseContext()));
        mTabHost.addTab(tabSpec);
    }

    private View getTabView(int icon, String name, String tag) {
        View view = mInflater.inflate(R.layout.tab_item, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.tab_icon);
        TextView tvName = (TextView) view.findViewById(R.id.tab_name);
        View vFlag = view.findViewById(R.id.tab_flag);
        if ("shoppingmall".equals(tag)) {
            ivTabShoppingMall = ivIcon;
            ivTabShoppingMall.setTag(tag);
            tvTabShoppingMall = tvName;
            tvTabShoppingMall.setTag(tag);
            vFlagShoppingMall = vFlag;
            vFlagShoppingMall.setTag(tag);
        } else if ("main".equals(tag)) {
            ivTabMain = ivIcon;
            ivTabMain.setTag(tag);
            tvTabMain = tvName;
            tvTabMain.setTag(tag);
            vFlagmain = vFlag;
            vFlagmain.setTag(tag);
        } else if ("order".equals(tag)) {
            ivTabMemb = ivIcon;
            ivTabMemb.setTag(tag);
            tvTabMemb = tvName;
            tvTabMemb.setTag(tag);
            vFlagmemb = vFlag;
            vFlagmemb.setTag(tag);
        } else if ("my".equals(tag)) {
            ivTabMy = ivIcon;
            ivTabMy.setTag(tag);
            tvTabMy = tvName;
            tvTabMy.setTag(tag);
            vFlagmy = vFlag;
            vFlagmy.setTag(tag);
        }
        ivIcon.setImageResource(icon);
        tvName.setText(name);
        return view;
    }

    private void setImg(int point) {
        if (bottomTabList.size() == 4) {
            tvTabMain.setText(bottomTabList.get(0).getTitle());
            tvTabShoppingMall.setText(bottomTabList.get(1).getTitle());
            tvTabMemb.setText(bottomTabList.get(2).getTitle());
            tvTabMy.setText(bottomTabList.get(3).getTitle());
            ivTabMain.setTag(null);
            ivTabShoppingMall.setTag(null);
            ivTabMemb.setTag(null);
            ivTabMy.setTag(null);
            getBitmap();
            if (point == 0) {
                if (drawableMainPress != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPicH(), ivTabMain, drawableMainPress);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPicH(), ivTabMain, 0);
                }
                if (drawableMallNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPic(), ivTabShoppingMall, drawableMallNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPic(), ivTabShoppingMall, 0);
                }
                if (drawableCircleNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPic(), ivTabMemb, drawableCircleNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPic(), ivTabMemb, 0);
                }
                if (drawableMyNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPic(), ivTabMy, drawableMyNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPic(), ivTabMy, 0);
                }
            } else if (point == 1) {
                if (drawableMainNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPic(), ivTabMain, drawableMainNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPic(), ivTabMain, 0);
                }
                if (drawableMallPress != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPicH(), ivTabShoppingMall, drawableMallPress);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPicH(), ivTabShoppingMall, 0);
                }
                if (drawableCircleNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPic(), ivTabMemb, drawableCircleNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPic(), ivTabMemb, 0);
                }
                if (drawableMyNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPic(), ivTabMy, drawableMyNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPic(), ivTabMy, 0);
                }
            } else if (point == 2) {
                if (drawableMainNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPic(), ivTabMain, drawableMainNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPic(), ivTabMain, 0);
                }
                if (drawableMallNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPic(), ivTabShoppingMall, drawableMallNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPic(), ivTabShoppingMall, 0);
                }
                if (drawableCirclePress != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPicH(), ivTabMemb, drawableCirclePress);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPicH(), ivTabMemb, 0);
                }
                if (drawableMyNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPic(), ivTabMy, drawableMyNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPic(), ivTabMy, 0);
                }
            } else if (point == 3) {
                if (drawableMainNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPic(), ivTabMain, drawableMainNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(0).getPic(), ivTabMain, 0);
                }
                if (drawableMallNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPic(), ivTabShoppingMall, drawableMallNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(1).getPic(), ivTabShoppingMall, 0);
                }
                if (drawableCircleNomal != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPic(), ivTabMemb, drawableCircleNomal);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(2).getPic(), ivTabMemb, 0);
                }
                if (drawableMyPress != null) {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPicH(), ivTabMy, drawableMyPress);
                } else {
                    GlideUtil.loadImg(MainActivity.this, bottomTabList.get(3).getPicH(), ivTabMy, 0);
                }
            }
        }
    }

    private void getBitmap() {
        if (drawableMainNomal == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(0).getPic())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableMainNomal = resource;
                        }
                    });
        }
        if (drawableMainPress == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(0).getPicH())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableMainPress = resource;
                        }
                    });
        }
        if (drawableMallNomal == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(1).getPic())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableMallNomal = resource;
                        }

                    });
        }
        if (drawableMallPress == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(1).getPicH())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableMallPress = resource;
                        }

                    });
        }
        if (drawableCircleNomal == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(2).getPic())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableCircleNomal = resource;
                        }

                    });
        }
        if (drawableCirclePress == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(2).getPicH())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableCirclePress = resource;
                        }
                    });
        }
        if (drawableMyNomal == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(3).getPic())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableMyNomal = resource;
                        }
                    });
        }
        if (drawableMyPress == null) {
            Glide.with(mContext)
                    .load(bottomTabList.get(3).getPicH())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawableMyPress = resource;
                        }
                    });
        }
    }

    private void initReceiver() {
        receiver = new MReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.mainactivity");
        // 注册广播接收器
        registerReceiver(receiver, filter);
    }

    private class MReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int previous = intent.getIntExtra("previous", 0);
            Utils.mLogError("MainActitvity收到广播" + previous);
            if (previous == Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
            } else if (previous == Global.PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY);
            } else if (previous == Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY);
            } else if (previous == Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT) {
                handler.sendEmptyMessage(Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
            } else if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
            } else if (previous == Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY);
            } else if (previous == Global.PRE_EVALUATEOVER_BACK_MAIN) {
                handler.sendEmptyMessage(Global.PRE_EVALUATEOVER_BACK_MAIN);
            } else if (previous == Global.PRE_LOGOUT_TO_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.PRE_LOGOUT_TO_MAINACTIVITY);
            } else if (previous == Global.PRE_ORDER_LIST_TO_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.PRE_ORDER_LIST_TO_MAINACTIVITY);
            } else if (previous == Global.PETADD_BACK_PETINFO_BACK_MY) {
                handler.sendEmptyMessage(Global.PETADD_BACK_PETINFO_BACK_MY);
            } else if (previous == Global.DELETEPET_TO_UPDATEUSERINFO) {
                handler.sendEmptyMessage(Global.DELETEPET_TO_UPDATEUSERINFO);
            } else if (previous == Global.PAYSUCCESS_TO_MYFRAGMENT) {
                handler.sendEmptyMessage(Global.PAYSUCCESS_TO_MYFRAGMENT);
            } else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
                handler.sendEmptyMessage(Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
            } else if (previous == Global.GROWTH_TO_USERMEMBERFRAGMENT) {
                handler.sendEmptyMessage(Global.GROWTH_TO_USERMEMBERFRAGMENT);
            } else if (previous == Global.CARDS_TO_MAINACTIVITY) {
                handler.sendEmptyMessage(Global.CARDS_TO_MAINACTIVITY);
            } else if (previous == Global.PAYSUCCESSNEWTO_MALLMAINACTIVITY) {
                handler.sendEmptyMessage(Global.PAYSUCCESSNEWTO_MALLMAINACTIVITY);
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToastShortBottom(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            spUtil.removeData("MAINFRAG_TIP");
            spUtil.removeData("MAINFRAG_TIP_TYPE");
            spUtil.removeData("TAG_MAINACTIVITY_ACTIVITY_EVERYONE");
            spUtil.removeData("TAG_SHOPPINGMALLFRAG_ACTIVITY_EVERYONE");
            MobclickAgent.onKillProcess(this);
            onDestroy();
            this.finish();
            System.exit(0);
        }
    }

    private void initBD() {
        mLocationClient = new LocationClient(this);
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

    private class MLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            spUtil.saveString("city", location.getCity());
            StringBuffer sb = new StringBuffer();
            sb.append("lotitude=" + location.getLatitude());
            sb.append("longitude=" + location.getLongitude());
            sb.append("addr=" + location.getAddrStr());
            sb.append("city=" + location.getCity());
            sb.append("citycode=" + location.getCityCode());
            Utils.mLogError("main:" + sb.toString());
            if (isFirst) {
                isFirst = false;
                registGeTuiToService(location.getLatitude(),
                        location.getLongitude());
            }
            lat = location.getLatitude();
            lng = location.getLongitude();
            spUtil.saveString("mallLat", String.valueOf(lat));
            spUtil.saveString("mallLng", String.valueOf(lng));
            autoLogin(lat, lng);
            mLocationClient.stop();
        }
    }

    private void registGeTuiToService(double lat, double lng) {
        CommUtil.registGeTuitoService(this, spUtil.getString("cellphone", ""),
                Global.getCurrentVersion(this), Global.getIMEI(this),
                spUtil.getInt("userid", 0), Global.getPushID(this), lat, lng,
                getuiHandler);
        Utils.mLogError("==-->tokedid " + Global.getPushID(this));
    }

    private void getIndex() {
        bottomTabList.clear();
        mallRedPoint = 0;
        nToBeComment = 0;
        CommUtil.getIndex(this, spUtil.getString("cellphone", ""),
                Global.getCurrentVersion(this), Global.getIMEI(this),
                indexHandler);
    }

    /**
     * 自动登录获取数据
     */
    private void autoLogin(double lat, double lng) {
        String cellphone = spUtil.getString("cellphone", "");
        int userid = spUtil.getInt("userid", 0);
        if (!"".equals(cellphone) && 0 != userid) {
            CommUtil.loginAuto(this, spUtil.getString("cellphone", ""),
                    spUtil.getString("IMEI", ""), Global.getCurrentVersion(this),
                    spUtil.getInt("userid", 0), lat, lng, autoLoginHandler);
        }
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("自动登录：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("user") && !jData.isNull("user")) {
                            JSONObject jUser = jData.getJSONObject("user");
                            if (jUser.has("cityId") && !jUser.isNull("cityId")) {
                                spUtil.saveInt("cityId",
                                        jUser.getInt("cityId"));
                            } else {
                                spUtil.removeData("cityId");
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
                            if (jUser.has("levelName") && !jUser.isNull("levelName")) {
                                spUtil.saveString("levelName", jUser.getString("levelName"));
                            }
                            if (jUser.has("memberLevelId") && !jUser.isNull("memberLevelId")) {
                                spUtil.saveInt("shopCartMemberLevelId",
                                        jUser.getInt("memberLevelId"));
                            } else {
                                spUtil.removeData("shopCartMemberLevelId");
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
                            if (jUser.has("inviteCode")
                                    && !jUser.isNull("inviteCode")) {
                                spUtil.saveString("invitecode",
                                        jUser.getString("inviteCode"));
                            }
                            if (jUser.has("cellPhone")
                                    && !jUser.isNull("cellPhone")) {
                                spUtil.saveString("cellphone",
                                        jUser.getString("cellPhone"));
                                spUtil.setPhoneSave(jUser.getString("cellPhone"));
                            }
                            if (jUser.has("userName")
                                    && !jUser.isNull("userName")) {
                                spUtil.saveString("username",
                                        jUser.getString("userName"));
                            }
                            if (jUser.has("avatarPath")
                                    && !jUser.isNull("avatarPath")) {
                                spUtil.saveString("userimage",
                                        jUser.getString("avatarPath"));
                            }
                            if (jUser.has("id") && !jUser.isNull("id")) {
                                spUtil.saveInt("userid", jUser.getInt("id"));
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
                                if (jPet.has("mypetId") && !jPet.isNull("mypetId")) {
                                    mypetId = jPet.getInt("mypetId");
                                } else {
                                    mypetId = 0;
                                }
                                if (jPet.has("petKind")
                                        && !jPet.isNull("petKind")) {
                                    spUtil.saveInt("petkind",
                                            jPet.getInt("petKind"));
                                }
                                if (jPet.has("petName")
                                        && !jPet.isNull("petName")) {
                                    spUtil.saveString("petname",
                                            jPet.getString("petName"));
                                }
                                if (jPet.has("avatarPath")
                                        && !jPet.isNull("avatarPath")) {
                                    spUtil.saveString("petimage",
                                            jPet.getString("avatarPath"));
                                    // 保存选择的宠物的头像
                                    spUtil.saveString(
                                            "petimg", jPet.getString("avatarPath"));
                                }
                                if (jPet.has("availService")
                                        && !jPet.isNull("availService")) {
                                    JSONArray jarr = jPet
                                            .getJSONArray("availService");
                                    if (jarr.length() > 0) {
                                        petServices = new int[jarr.length()];
                                        for (int i = 0; i < jarr.length(); i++) {
                                            petServices[i] = jarr.getInt(i);
                                        }
                                    }
                                }
                            } else {
                                spUtil.removeData("isCerti");
                                spUtil.removeData("petid");
                                spUtil.removeData("petKind");
                                spUtil.removeData("petname");
                                spUtil.removeData("petimage");
                            }
                            if (jUser.has("myPet") && !jUser.isNull("myPet")) {
                                JSONObject jMyPet = jUser
                                        .getJSONObject("myPet");
                                if (mypetId > 0 && jMyPet.has("petId") && !jMyPet.isNull("petId")) {
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
                } else {
                    spUtil.removeData("cellphone");
                    spUtil.removeData("username");
                    spUtil.removeData("userimage");
                    spUtil.removeData("userid");
                    spUtil.removeData("payway");
                    spUtil.removeData("petid");
                    spUtil.removeData("petkind");
                    spUtil.removeData("petname");
                    spUtil.removeData("petimage");
                    spUtil.removeData("addressid");
                    spUtil.removeData("lat");
                    spUtil.removeData("lng");
                    spUtil.removeData("address");
                    spUtil.removeData("serviceloc");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private AsyncHttpResponseHandler getuiHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("注册推送：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private AsyncHttpResponseHandler indexHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("首页：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata.has("nToBeComment")
                            && !jdata.isNull("nToBeComment")) {
                        nToBeComment = jdata.getInt("nToBeComment");
                    }
                    if (jdata.has("mallRedPoint")
                            && !jdata.isNull("mallRedPoint")) {
                        mallRedPoint = jdata.getInt("mallRedPoint");
                    }
                    if (jdata.has("index") && !jdata.isNull("index")) {
                        JSONObject jindex = jdata.getJSONObject("index");
                        if (jindex.has("bottom") && !jindex.isNull("bottom")) {
                            JSONObject jbottom = jindex.getJSONObject("bottom");
                            if (jbottom.has("list") && !jbottom.isNull("list")) {
                                JSONArray jlist = jbottom.getJSONArray("list");
                                if (jlist.length() > 0) {
                                    for (int i = 0; i < jlist.length(); i++) {
                                        bottomTabList.add(BottomTabBean.json2Entity(jlist
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (bottomTabList.size() == 4) {
                int point = getIntent().getIntExtra("point", 0);
                if (point == 21) {
                    mTabHost.setCurrentTab(1);
                    setImg(1);
                } else {
                    mTabHost.setCurrentTab(0);
                    setImg(0);
                }
                if (mallRedPoint > 0) {
                    vFlagShoppingMall.setVisibility(View.VISIBLE);
                }
                if (nToBeComment > 0) {
                    vFlagmemb.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(UpdateAppEvent event) {
        if (event != null) {
            if (event.getState() == UpdateAppEvent.DOWNLOADING) {
                long soFarBytes = event.getSoFarBytes();
                long totalBytes = event.getTotalBytes();
                if (event.getIsUpgrade() == 0 && isShow) {

                } else {
                    Log.e("TAG", "下载中...soFarBytes = " + soFarBytes + "---totalBytes = " + totalBytes);
                    if (progressDialog != null && progressDialog.isShowing()) {

                    } else {
                        progressDialog = new DownloadProgressDialog(this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("下载提示");
                        progressDialog.setMessage("当前下载进度:");
                        progressDialog.setIndeterminate(false);
                        if (event.getIsUpgrade() == 1) {
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        } else {
                            progressDialog.setCancelable(true);
                            progressDialog.setCanceledOnTouchOutside(true);
                        }
                        progressDialog.show();
                    }
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.e("TAG", "onDismiss");
                            isShow = true;
                        }
                    });
                }
                if (progressDialog != null) {
                    progressDialog.setMax((int) totalBytes);
                    progressDialog.setProgress((int) soFarBytes);
                }
                isShow = true;
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_COMPLETE) {
                UpdateUtil.installAPK(MainActivity.this, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(MainActivity.this, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(MainActivity.this, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_FAIL) {
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            }
        }
    }

    /**
     * 获取最新版本和是否强制升级
     */
    private void getLatestVersion() {
        latestVersion = "";
        downloadPath = "";
        versionHint = "";
        isUpgrade = 0;
        isShow = false;
        CommUtil.getLatestVersion(this,
                Global.getCurrentVersion(MainActivity.this),
                System.currentTimeMillis(), latestHanler);
    }

    private AsyncHttpResponseHandler latestHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("最新版本：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj != null) {
                    if (jobj.has("code") && !jobj.isNull("code")) {
                        int resultCode = jobj.getInt("code");
                        if (0 == resultCode) {
                            if (jobj.has("data") && !jobj.isNull("data")) {
                                JSONObject jData = jobj.getJSONObject("data");
                                if (jData.has("nversion")
                                        && !jData.isNull("nversion")) {
                                    latestVersion = jData.getString("nversion");
                                }
                                if (jData.has("download")
                                        && !jData.isNull("download")) {
                                    downloadPath = jData.getString("download");
                                }
                                if (jData.has("text") && !jData.isNull("text")) {
                                    versionHint = jData.getString("text");
                                }
                                if (jData.has("mandate")
                                        && !jData.isNull("mandate")) {
                                    isUpgrade = jData.getInt("mandate");
                                }
                                boolean isLatest = UpdateUtil
                                        .compareVersion(
                                                latestVersion,
                                                Global.getCurrentVersion(MainActivity.this));
                                if (isLatest) {// 需要下载安装最新版
                                    if (isUpgrade == 1) {
                                        // 强制升级
                                        UpdateUtil.showForceUpgradeDialog(MainActivity.this, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(MainActivity.this,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(MainActivity.this,
                                                                    downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                        }
                                                    }
                                                });
                                    } else if (isUpgrade == 0) {
                                        // 非强制升级
                                        UpdateUtil.showUpgradeDialog(MainActivity.this, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(MainActivity.this,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(MainActivity.this,
                                                                    downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    @Override
    protected void onDestroy() {
        if (receiver != null)
            unregisterReceiver(receiver);
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);// 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    // 使用sso授权新浪微博客户端时，fragment内部onActivityResult
    // 方法不执行，暂时未找到查询方法，将当前方法在父activity中重写，可以达到效果后期查询具体解决方案
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        Fragment fragment = fm.findFragmentByTag(tabTag);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    private void getAccountCenter() {
        CommUtil.accountCenter(mContext, accountHandler);
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
                            if (objUser.has("waitForPay") && !objUser.isNull("waitForPay")) {
                                JSONObject objectPay = objUser.getJSONObject("waitForPay");
                                if (objectPay.has("title") && !objectPay.isNull("title")) {
                                    String title = objectPay.getString("title");
                                    Intent intent = new Intent(mContext, UpgradeOrderPopActivity.class);
                                    intent.putExtra("title", title);
                                    startActivity(intent);
                                }
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
            mPDialog.closeDialog();
        }
    };

    public void setSelect(int index) {
        this.index = index;
        mTabHost.setCurrentTabByTag("order");
    }
}
