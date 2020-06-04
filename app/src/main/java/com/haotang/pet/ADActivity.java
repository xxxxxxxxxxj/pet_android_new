package com.haotang.pet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.mallEntity.MallAddress;
import com.haotang.pet.mall.MallSelfAddressActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.updateapputil.Callback;
import com.haotang.pet.updateapputil.ConfirmDialog;
import com.haotang.pet.updateapputil.DownloadAppUtils;
import com.haotang.pet.updateapputil.DownloadProgressDialog;
import com.haotang.pet.updateapputil.UpdateAppEvent;
import com.haotang.pet.updateapputil.UpdateUtil;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.webview.MiddlewareChromeClient;
import com.haotang.pet.webview.MiddlewareWebViewClient;
import com.haotang.pet.webview.UIController;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * webview
 */
public class ADActivity extends SuperActivity {
    @BindView(R.id.iv_webview_back)
    ImageView ivWebviewBack;
    @BindView(R.id.ll_webview_back)
    LinearLayout llWebviewBack;
    @BindView(R.id.view_webview_line)
    View viewWebviewLine;
    @BindView(R.id.iv_webview_finish)
    ImageView ivWebviewFinish;
    @BindView(R.id.ll_webview_finish)
    LinearLayout llWebviewFinish;
    @BindView(R.id.ll_webview_left)
    LinearLayout llWebviewLeft;
    @BindView(R.id.tv_webview_other)
    TextView tvWebviewOther;
    @BindView(R.id.iv_webview_share)
    ImageView ivWebviewShare;
    @BindView(R.id.ll_webview_right)
    LinearLayout llWebviewRight;
    @BindView(R.id.tv_webview_title)
    TextView tvWebviewTitle;
    @BindView(R.id.rl_webview_toolbar)
    RelativeLayout rlWebviewToolbar;
    @BindView(R.id.ll_adactivity)
    LinearLayout llAdactivity;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    private String shareimg = "drawable://" + R.drawable.logo;
    private String sharetitle = "宠物家app";
    private String sharetxt = "宠物家";
    private String shareurl = Global.OfficialWebsite;
    private IWXAPI api;
    private int previous;
    public static SuperActivity act;
    private int payType;
    private PopupWindow pWinBottomDialog;
    private StringBuilder listpayWays = new StringBuilder();
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private String payStateUrl;
    private int locationstatus;
    private LocationClient mLClient;
    private MLocationListener mListener;
    private String sharchannel;
    private AgentWeb mAgentWeb;
    /**
     * 用于方便打印测试
     */
    private Gson mGson = new Gson();
    public static final String TAG = ADActivity.class.getSimpleName();
    private MiddlewareWebClientBase mMiddleWareWebClient;
    private MiddlewareWebChromeBase mMiddleWareWebChrome;
    private MallAddress mallAddress;
    private DownloadProgressDialog progressDialog;
    private String latestVersion;
    private String downloadPath;
    private String versionHint;
    private int isUpgrade;
    public final static int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private boolean isShow;
    private DownloadingService mDownloadingService;
    private int toothCardAmount;
    private double toothCardPrice;
    private double maxPrice;
    private boolean isCheckRedeem = false;
    private int payPoint;
    private int toothCardId;
    private List<Integer> toothCardIds = new ArrayList<>();
    private String toothCardName;

    //微信支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayResultEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            BaseResp resp = event.getResp();
            if (resp != null) {
                if (resp.errCode == 0) {
                    if (Utils.isStrNull(payStateUrl)) {
                        Log.e("TAG", "微+信payStateUrl = " + payStateUrl);
                        if (!payStateUrl.startsWith("http:")
                                && !payStateUrl.startsWith("https:")) {
                            payStateUrl = CommUtil.getStaticUrl()
                                    + payStateUrl;
                        }
                        if (payStateUrl.contains("?")) {
                            payStateUrl = payStateUrl
                                    + "&system="
                                    + CommUtil.getSource()
                                    + "_"
                                    + Global.getCurrentVersion(ADActivity.this)
                                    + "&imei="
                                    + Global.getIMEI(ADActivity.this)
                                    + "&cellPhone="
                                    + SharedPreferenceUtil.getInstance(
                                    ADActivity.this).getString("cellphone",
                                    "") + "&phoneModel="
                                    + Build.BRAND + " "
                                    + Build.MODEL
                                    + "&phoneSystemVersion=" + "Android "
                                    + Build.VERSION.RELEASE + "&time="
                                    + System.currentTimeMillis() + "&payState=1";
                        } else {
                            payStateUrl = payStateUrl
                                    + "?system="
                                    + CommUtil.getSource()
                                    + "_"
                                    + Global.getCurrentVersion(ADActivity.this)
                                    + "&imei="
                                    + Global.getIMEI(ADActivity.this)
                                    + "&cellPhone="
                                    + SharedPreferenceUtil.getInstance(
                                    ADActivity.this).getString("cellphone",
                                    "") + "&phoneModel="
                                    + Build.BRAND + " "
                                    + Build.MODEL
                                    + "&phoneSystemVersion=" + "Android "
                                    + Build.VERSION.RELEASE + "&time="
                                    + System.currentTimeMillis() + "&payState=1";
                        }
                        Log.e("TAG", "微信payStateUrl = " + payStateUrl);
                        mPDialog.showDialog();
                        if (mAgentWeb != null) {
                            mAgentWeb.getUrlLoader().loadUrl(payStateUrl); // 刷新
                        }
                    }
                    goPayResult();
                } else {
                    if (Utils.isStrNull(resp.errStr)) {
                        ToastUtil.showToastShortBottom(mContext, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "支付失败");
                    }
                }
            }
        }
    }

    private void goPayResult() {
        if (payPoint==1){//购买刷牙卡
            Intent intent = new Intent(mContext, PaySuccessNewActivity.class);
            intent.putExtra("type", 22);
            intent.putExtra("payPrice", payPrice);
            intent.putExtra("cardIds",(Serializable) toothCardIds);
            intent.putExtra("cardName",toothCardName);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        initBD();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(this);
        api = WXAPIFactory.createWXAPI(this, Global.APP_ID);
        previous = getIntent().getIntExtra("previous", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(llAdactivity, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setAgentWebUIController(new UIController(this)) //自定义UI  AgentWeb3.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .useMiddlewareWebChrome(getMiddlewareWebChrome()) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                .useMiddlewareWebClient(getMiddlewareWebClient()) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
//                .setDownloadListener(mDownloadListener) 4.0.0 删除该API//下载回调
//                .openParallelDownload()// 4.0.0删除该API 打开并行下载 , 默认串行下载。 请通过AgentWebDownloader#Extra实现并行下载
//                .setNotifyIcon(R.drawable.ic_file_download_black_24dp) 4.0.0删除该api //下载通知图标。4.0.0后的版本请通过AgentWebDownloader#Extra修改icon
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(getUrl()); //WebView载入该url地址的页面并显示。
        AgentWebConfig.debug();
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        Log.e("TAG", "url = " + getUrl());
    }

    private void setView() {
        if (Utils.isStrNull(getUrl())) {
            if (getUrl().contains("cwj_bannershare=1")) {
                tvWebviewOther.setText("分享");
                tvWebviewOther.setVisibility(View.VISIBLE);
            } else {
                tvWebviewOther.setVisibility(View.GONE);
            }
            if (getUrl().contains("backaction=1")) {
                rlWebviewToolbar.setVisibility(View.GONE);
            } else {
                rlWebviewToolbar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setLinster() {
        mAgentWeb.getJsInterfaceHolder().addJavaObject("fromh5lucy", new JsObject1() {

            @JavascriptInterface
            public void buyItemCard(String amount, String totalPrice) {// 刷牙卡支付
                toothCardAmount = Integer.parseInt(amount);
                toothCardPrice = Double.parseDouble(totalPrice);
                maxPrice = toothCardPrice;
                payPoint=1;
                getPayWay(Global.ORDERKEY[13]);

            }

            @JavascriptInterface
            public void cardImageShare(String shareImg){//微信分享图片
                Utils.mLogError("分享-----------------"+shareImg);
                Utils.shareToWXImage(mContext,shareImg);
            }

            @JavascriptInterface
            public void miniProgramShare(String shareInfo){//微信分享小程序
                try {
                    Utils.mLogError("分享-----------------"+shareInfo);
                    JSONObject jobj = new JSONObject(shareInfo);
                    String webpageUrl = jobj.getString("webpageUrl");
                    String imgurl = jobj.getString("imgurl");
                    String title = jobj.getString("title");
                    String description = jobj.getString("description");
                    String path = jobj.getString("path");
                    Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(imgurl);
                    Utils.shareMiniProgram(mContext,webpageUrl,returnBitmap,path,title,description);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @JavascriptInterface
            public void savedPhotos(String shareImg){//保存图片到本地
                Utils.mLogError("保存-----------------"+shareImg);
                Bitmap bitmap = Utils.GetLocalOrNetBitmap(shareImg);
                if (bitmap!=null){
                    Utils.saveImageToGallery(mContext,bitmap);
                }else {
                    ToastUtil.showToastShortBottom(mContext,"保存失败");
                }

            }

            @JavascriptInterface
            public void goDownloadPage(String downloadlink){//跳转应用宝下载应用
                if (Utils.isAvilible(mContext,"com.tencent.android.qqdownloader")){
                    Utils.launchAppDetail(mContext,"com.haotang.pet","com.tencent.android.qqdownloader");
                }else {
                    Uri uri = Uri.parse(downloadlink);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }

            @JavascriptInterface
            public void shower(String num) {// 洗澡预约界面
                startActivity(new Intent(ADActivity.this,
                        ServiceNewActivity.class).putExtra("serviceType", 1));
            }

            @JavascriptInterface
            public void hairdressing(String num) {// 美容预约界面
                startActivity(new Intent(ADActivity.this,
                        ServiceNewActivity.class).putExtra("serviceType", 2));
            }

            @JavascriptInterface
            public void fosterCare(String num) {// 寄养预约界面
                startActivity(new Intent(mContext, FosterHomeActivity.class));
            }

            @JavascriptInterface
            public void worker(String workId) {// 美容师主页
                int workerId = Integer.parseInt(workId);
                startAct(ADActivity.this, Global.MAIN_TO_BEAUTICIANLIST, 0,
                        "", workerId, 0, 0, 0, BeauticianDetailActivity.class,
                        0);
            }

            @JavascriptInterface
            public void coupon(String num) {// 会员-优惠券
                if ("".equals(spUtil.getString("cellphone", ""))) {
                    startAct(ADActivity.this, Global.PRE_PUSH_TO_LOGIN, 0,
                            null, 0, 0, 0, 0, LoginNewActivity.class, 0);
                } else {
                    startAct(ADActivity.this, Global.PRE_PUSH_TO_ORDER, 0,
                            null, 0, 0, 0, 0, MyCouponNewActivity.class, 0);
                }
            }

            @JavascriptInterface
            public void order(String type, String orderId) {// 指定订单界面
                int localOrderId = Integer.parseInt(orderId);
                if ("".equals(spUtil.getString("cellphone", ""))) {
                    startAct(ADActivity.this, Global.AD_TO_LOGIN_TO_ORDER,
                            localOrderId, null, 0, 0, 0, 0,
                            LoginNewActivity.class, 0);
                } else {
                    if (Utils.isStrNull(type)) {
                        if (type.equals("1")) {// 寄养
                            startAct(ADActivity.this, 0, localOrderId, null, 0, 0, 0,
                                    0, FosterOrderDetailNewActivity.class, 0);
                        } else if (type.equals("2")) {// 洗美
                            startAct(ADActivity.this, 0, localOrderId, null, 0, 0, 0,
                                    0, WashOrderDetailActivity.class, 0);
                        } else if (type.equals("3")) {//其他(游泳,训犬)
                            startAct(ADActivity.this, 0, localOrderId, null, 0, 0, 0,
                                    0, OrderDetailFromOrderToConfirmActivity.class, 0);
                        }
                    }
                }
            }

            @JavascriptInterface
            public void workerList(String workListId) {// 美容师指定等级列表主页
                int workerLevel = Integer.parseInt(workListId);
                startAct(ADActivity.this, 0, 0, "", 0, workerLevel, 0, 0,
                        MainToBeauList.class, 0);
            }

            @JavascriptInterface
            public void card(String url) {// 办狗证
                startAct(ADActivity.this, 0, 0, url, 0, 0, 0, 0,
                        ADActivity.class, 0);
            }

            @JavascriptInterface
            public void goback() {
                Log.e("TAG", "goback()");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mAgentWeb.back()) {
                            finish();
                        }
                    }
                });
            }

            @JavascriptInterface
            public void goLogin() {
                if ("".equals(spUtil.getString("cellphone", ""))
                        && LoginNewActivity.act == null) {
                    // 去登录
                    Intent intent = new Intent(ADActivity.this,
                            LoginNewActivity.class);
                    intent.putExtra(Global.ANIM_DIRECTION(),
                            Global.ANIM_COVER_FROM_RIGHT());
                    getIntent().putExtra(Global.ANIM_DIRECTION(),
                            Global.ANIM_COVER_FROM_LEFT());
                    intent.putExtra("previous", Global.AD_TO_LOGIN);
                    startActivityForResult(intent, Global.AD_TO_LOGIN);
                }
            }

            @JavascriptInterface
            public void goCoupon() {
                if (!"".equals(spUtil.getString("cellphone", ""))) {
                    // 去优惠券列表
                    Intent intent = new Intent(ADActivity.this,
                            MyCouponNewActivity.class);
                    intent.putExtra(Global.ANIM_DIRECTION(),
                            Global.ANIM_COVER_FROM_RIGHT());
                    getIntent().putExtra(Global.ANIM_DIRECTION(),
                            Global.ANIM_COVER_FROM_LEFT());
                    intent.putExtra("previous", Global.AD_TO_LOGIN);
                    startActivity(intent);
                    finish();
                }
            }

            @JavascriptInterface
            public void unusualServe(String str) {// 特色服务下单页
                if (Utils.isStrNull(str)) {
                    Intent intent = new Intent(ADActivity.this,
                            CharacteristicServiceActivity.class);
                    intent.putExtra("typeId", Integer.parseInt(str));// 从特色服务跳转到门店列表专用，为了计算价格
                    intent.putExtra("serviceType", 3);
                    startActivity(intent);
                }
            }

            @JavascriptInterface
            public void goUpgrade() {//检测升级
                getLatestVersion();
            }

            @JavascriptInterface
            public void goService(int point, String backup) {// 服务页面
                Utils.goService(ADActivity.this, point, backup);
            }
        });
        mAgentWeb.getJsInterfaceHolder().addJavaObject("fromh5data", new JsObject() {
            @JavascriptInterface
            public void thirdPartyPayment(String payInfo) {// 呼起支付
                String decode = URLDecoder.decode(payInfo);
                Log.e("TAG", "decode = " + decode);
                if (Utils.isStrNull(decode)) {
                    try {
                        JSONObject jobj = new JSONObject(decode);
                        if (jobj.has("payway") && !jobj.isNull("payway")) {
                            ADActivity.this.payType = Integer.parseInt(jobj
                                    .getString("payway"));
                        }
                        if (jobj.has("payStateUrl")
                                && !jobj.isNull("payStateUrl")) {
                            payStateUrl = jobj.getString("payStateUrl");
                        }
                        if (jobj.has("payInfo") && !jobj.isNull("payInfo")) {
                            JSONObject jpayInfo = jobj.getJSONObject("payInfo");
                            if (jpayInfo.has("appid")
                                    && !jpayInfo.isNull("appid")) {
                                appid = jpayInfo.getString("appid");
                            }
                            if (jpayInfo.has("noncestr")
                                    && !jpayInfo.isNull("noncestr")) {
                                noncestr = jpayInfo.getString("noncestr");
                            }
                            if (jpayInfo.has("package")
                                    && !jpayInfo.isNull("package")) {
                                packageValue = jpayInfo.getString("package");
                            }
                            if (jpayInfo.has("partnerid")
                                    && !jpayInfo.isNull("partnerid")) {
                                partnerid = jpayInfo.getString("partnerid");
                            }
                            if (jpayInfo.has("prepayid")
                                    && !jpayInfo.isNull("prepayid")) {
                                prepayid = jpayInfo.getString("prepayid");
                            }
                            if (jpayInfo.has("sign")
                                    && !jpayInfo.isNull("sign")) {
                                sign = jpayInfo.getString("sign");
                            }
                            if (jpayInfo.has("timestamp")
                                    && !jpayInfo.isNull("timestamp")) {
                                timestamp = jpayInfo.getString("timestamp");
                            }
                            if (jpayInfo.has("orderStr")
                                    && !jpayInfo.isNull("orderStr")) {
                                orderStr = jpayInfo.getString("orderStr");
                            }
                        }
                        goPay();
                    } catch (Exception e) {
                        Log.e("TAG", "数据异常");
                        e.printStackTrace();
                    }
                }
            }

            @JavascriptInterface
            public void LiveFostercare(String videoUrl, String name) {// 跳转到直播详情页
                Intent intent = new Intent(mContext, FosterLiveActivity.class);
                intent.putExtra("videoUrl", videoUrl);
                intent.putExtra("name", name);
                startActivity(intent);
            }

            @JavascriptInterface
            public void shareCardInfo(String des, String imgurl, String url,
                                      String title, String channel) {
                shareimg = imgurl;//图片地址
                sharetitle = des;//标题
                shareurl = url;//URL
                sharetxt = title;//描述
                sharchannel = channel;//渠道（1，2，3）（1：微信好友，2：微信朋友圈，3：QQ）
            }

            @JavascriptInterface
            public void goShareCardInfo(String des, String imgurl,
                                        String url, String title, String type) {
                Utils.share(ADActivity.this, imgurl, title, des, url, null, Integer.parseInt(type),null);
            }

            @JavascriptInterface
            public void shareCardInfoDidlog(String des, String imgurl,
                                            String url, String title, String channel) {
                Utils.share(ADActivity.this, imgurl, title, des, url, channel, 0,null);
            }

            @JavascriptInterface
            public void login() {
                if ("".equals(spUtil.getString("cellphone", ""))) {
                    // 去登录
                    JumpToNext(LoginNewActivity.class);
                }
            }

            // 办证支付
            @JavascriptInterface
            public void certipay(String id) {
                Intent intent = new Intent(ADActivity.this,
                        OrderPayActivity.class);
                intent.putExtra("previous",
                        Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
                intent.putExtra("CertiOrderId", Integer.parseInt(id));
                startActivityForResult(intent,
                        Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
            }

            @JavascriptInterface
            public void setData(int type, String data) {
                if ("".equals(spUtil.getString("cellphone", ""))) {
                    // 去登录
                    JumpToNext(LoginNewActivity.class);
                } else {
                    // 弹出分享
                    if (!shareurl.contains("inviteCode")) {
                        if (shareurl.contains("?")) {
                            shareurl = shareurl + "&inviteCode="
                                    + spUtil.getString("invitecode", "");
                        } else {
                            shareurl = shareurl + "?inviteCode="
                                    + spUtil.getString("invitecode", "");
                        }
                    }
                    Utils.share(ADActivity.this, shareimg, sharetitle, sharetxt, shareurl, sharchannel, 0,null);
                }
            }

            @JavascriptInterface
            public void goCan() {
                Log.e("TAG", "goCan()");
                startActivity(new Intent(ADActivity.this, MyCanActivity.class));
            }

            @JavascriptInterface
            public void goMember() {
                Log.e("TAG", "goMember()");
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(ADActivity.this, MemberUpgradeActivity.class));
                } else {
                    startActivity(new Intent(ADActivity.this, LoginNewActivity.class));
                }
            }

            @JavascriptInterface
            public void getlocation() {
                Log.e("TAG", "getlocation()");
                locationstatus = 1;
                mLClient.start();
            }

            @JavascriptInterface
            public void showMallAddressList() {
                Log.e("TAG", "showMallAddressList()");
                Intent intent = new Intent(ADActivity.this, MallSelfAddressActivity.class);
                intent.putExtra("previous", Global.WEBVIEW_TO_ADDRESS);
                startActivityForResult(intent, Global.WEBVIEW_TO_ADDRESS);
            }
        });
    }


    private void getPayWay(String orderKey) {
        listpayWays.setLength(0);
        //mPDialog.showDialog();
        CommUtil.payWays(this, orderKey, 0, payWaysHandler);
    }

    private AsyncHttpResponseHandler payWaysHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("payWays") && !jData.isNull("payWays")) {
                            JSONArray jsonArray = jData.getJSONArray("payWays");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listpayWays.append(jsonArray.getString(i));
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
                showPayDialog();
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void showPayDialog() {
        int oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.appoint_pay_bottom_dialog, null);
//        ImageView iv_pay_bottom_bg = (ImageView) customView.findViewById(R.id.iv_pay_bottom_bg);
        Button btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        customView.findViewById(R.id.ll_pay_bottomdia_time).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_minute).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_second).setVisibility(View.GONE);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        RelativeLayout rl_pay_bottomdia_yqm = (RelativeLayout) customView.findViewById(R.id.rl_pay_bottomdia_yqm);
        rl_pay_bottomdia_yqm.setVisibility(View.GONE);
        customView.findViewById(R.id.iv_pay_bottomdia_yqm_select).setVisibility(View.GONE);
        final EditText et_pay_bottomdia_yqm = (EditText) customView.findViewById(R.id.et_pay_bottomdia_yqm);
        final ImageView iv_pay_bottomdia_zfb_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_zfb_select);
        pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(mContext)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isCheckRedeem = false;
                et_pay_bottomdia_yqm.setText("");
            }
        });
        et_pay_bottomdia_yqm.setImeOptions(EditorInfo.IME_ACTION_DONE);
        btn_pay_bottomdia.setText("确认支付¥" + maxPrice);
        Log.e("TAG", "oldpayway = " + oldpayway);
        if (listpayWays.toString().contains("1")) {
            ll_pay_bottomdia_weixin.setVisibility(View.VISIBLE);
            if (oldpayway == 1) {
                payType = 1;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_weixin.setVisibility(View.GONE);
        }
        if (listpayWays.toString().contains("2")) {
            ll_pay_bottomdia_zfb.setVisibility(View.VISIBLE);
            if (oldpayway == 2) {
                payType = 2;
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_zfb.setVisibility(View.GONE);
        }
        tv_pay_title.setText("请选择支付方式");
        iv_pay_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        btn_pay_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payType != 1 && payType != 2) {
                    ToastUtil.showToastShortBottom(mContext, "请选择支付方式");
                    return;
                } else {
                    goBuyThoothCard();
                }
            }
        });
        ll_pay_bottomdia_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 1;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
            }
        });
        ll_pay_bottomdia_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 2;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
            }
        });
    }

    private void goBuyThoothCard() {
        mPDialog.showDialog();
        CommUtil.extraCardPay(mContext,toothCardAmount,toothCardPrice,payType,buyThoothCardHandler);
    }

    private double payPrice;
    private AsyncHttpResponseHandler buyThoothCardHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code==0){
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("payPrice")&&!object.isNull("payPrice")){
                            payPrice = object.getDouble("payPrice");
                        }
                        if (object.has("cardIds")&&!object.isNull("cardIds")){
                            JSONArray cardIds = object.getJSONArray("cardIds");
                            for (int i = 0; i < cardIds.length(); i++) {
                                toothCardIds.add(cardIds.getInt(i));
                            }
                            Utils.mLogError(toothCardIds.size()+"-----------");
                        }
                        if (object.has("cardName")&&!object.isNull("cardName")){
                            toothCardName = object.getString("cardName");
                        }
                        if (object.has("payInfo") && !object.isNull("payInfo")) {
                            JSONObject jpayInfo = object
                                    .getJSONObject("payInfo");
                            if (jpayInfo.has("appid")
                                    && !jpayInfo.isNull("appid")) {
                                appid = jpayInfo.getString("appid");
                            }
                            if (jpayInfo.has("noncestr")
                                    && !jpayInfo.isNull("noncestr")) {
                                noncestr = jpayInfo.getString("noncestr");
                            }
                            if (jpayInfo.has("package")
                                    && !jpayInfo.isNull("package")) {
                                packageValue = jpayInfo.getString("package");
                            }
                            if (jpayInfo.has("partnerid")
                                    && !jpayInfo.isNull("partnerid")) {
                                partnerid = jpayInfo.getString("partnerid");
                            }
                            if (jpayInfo.has("prepayid")
                                    && !jpayInfo.isNull("prepayid")) {
                                prepayid = jpayInfo.getString("prepayid");
                            }
                            if (jpayInfo.has("sign")
                                    && !jpayInfo.isNull("sign")) {
                                sign = jpayInfo.getString("sign");
                            }
                            if (jpayInfo.has("timestamp")
                                    && !jpayInfo.isNull("timestamp")) {
                                timestamp = jpayInfo.getString("timestamp");
                            }
                            if (jpayInfo.has("orderStr")
                                    && !jpayInfo.isNull("orderStr")) {
                                orderStr = jpayInfo.getString("orderStr");
                            }
                            if (payType == 1) {
                                if (appid != null && !TextUtils.isEmpty(appid)
                                        && noncestr != null
                                        && !TextUtils.isEmpty(noncestr)
                                        && packageValue != null
                                        && !TextUtils.isEmpty(packageValue)
                                        && partnerid != null
                                        && !TextUtils.isEmpty(partnerid)
                                        && prepayid != null
                                        && !TextUtils.isEmpty(prepayid)
                                        && sign != null
                                        && !TextUtils.isEmpty(sign)
                                        && timestamp != null
                                        && !TextUtils.isEmpty(timestamp)) {
                                    // 微信支付
                                    mPDialog.showDialog();
                                    spUtil.saveInt("payway", 1);
                                    PayUtils.weChatPayment(mContext,
                                            appid, partnerid, prepayid,
                                            packageValue, noncestr, timestamp,
                                            sign, mPDialog);
                                } else {
                                    ToastUtil.showToastShortBottom(
                                            mContext, "支付参数错误");
                                }
                            } else if (payType == 2) {
                                if (orderStr != null
                                        && !TextUtils.isEmpty(orderStr)) {
                                    // 判断是否安装支付宝
                                    spUtil.saveInt("payway", 2);
                                    PayUtils.checkAliPay(mContext,
                                            aliHandler);
                                } else {
                                    ToastUtil.showToastShortBottom(
                                            mContext, "支付参数错误");
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

    /**
     * 页面空白，请检查scheme是否加上， scheme://host:port/path?query&query 。
     *
     * @return mUrl
     */
    public String getUrl() {
        String url = getIntent().getStringExtra("url");
        if (!Utils.isStrNull(url)) {
            url = "http://www.haotang365.com.cn";
        }
        if (url != null && !TextUtils.isEmpty(url)) {
            if (!url.startsWith("http:")
                    && !url.startsWith("https:") && !url.startsWith("file:///")) {
                url = CommUtil.getStaticUrl() + url;
            }
            if (url.contains("?")) {
                url = url
                        + "&system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(this)
                        + "&imei="
                        + Global.getIMEI(this)
                        + "&cellPhone="
                        + SharedPreferenceUtil.getInstance(this).getString(
                        "cellphone", "") + "&phoneModel="
                        + Build.BRAND + " " + Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis();
            } else {
                url = url
                        + "?system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(this)
                        + "&imei="
                        + Global.getIMEI(this)
                        + "&cellPhone="
                        + SharedPreferenceUtil.getInstance(this).getString(
                        "cellphone", "") + "&phoneModel="
                        + Build.BRAND + " " + Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis();
            }
        }
        return url;
    }

    // 百度定位
    private void initBD() {
        mLClient = new LocationClient(this);
        mListener = new MLocationListener();
        mLClient.registerLocationListener(mListener);
        LocationClientOption clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setScanSpan(1100);//当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
        clientOption.setIsNeedAddress(true);
        mLClient.setLocOption(clientOption);
    }

    @OnClick({R.id.ll_webview_back, R.id.ll_webview_finish, R.id.tv_webview_other, R.id.iv_webview_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_webview_back:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mAgentWeb.back()) {
                            finish();
                        }
                    }
                });
                break;
            case R.id.ll_webview_finish:
                finish();
                break;
            case R.id.tv_webview_other:
                Utils.share(ADActivity.this, shareimg, sharetitle, sharetxt, shareurl, sharchannel, 0,null);
                break;
            case R.id.iv_webview_share:
                Utils.share(ADActivity.this, shareimg, sharetitle, sharetxt, shareurl, sharchannel, 0,null);
                break;
        }
    }

    public class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.e("TAG", "获取到经纬度");
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            if (locationstatus == 1) {//H5获取经纬度
                // 点击导航图标
                locationstatus = 0;
                Log.e("TAG", "lat = " + lat + "-----lng = " + lng);
                String curl = "javascript:getlocation('" + 0 + "','" + lat + "','" + lng + "')";
                if (mAgentWeb != null) {
                    mAgentWeb.getUrlLoader().loadUrl(curl); // 刷新
                }
            }
        }
    }

    class JsObject {
        @JavascriptInterface
        public String toString() {
            return "fromh5data";
        }
    }

    class JsObject1 {
        @JavascriptInterface
        public String toString() {
            return "fromh5lucy";
        }
    }

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (Utils.isStrNull(payStateUrl)) {
                            Log.e("TAG", "支付宝payStateUrl = " + payStateUrl);
                            if (!payStateUrl.startsWith("http:")
                                    && !payStateUrl.startsWith("https:")) {
                                payStateUrl = CommUtil.getStaticUrl()
                                        + payStateUrl;
                            }
                            if (payStateUrl.contains("?")) {
                                payStateUrl = payStateUrl
                                        + "&system="
                                        + CommUtil.getSource()
                                        + "_"
                                        + Global.getCurrentVersion(ADActivity.this)
                                        + "&imei="
                                        + Global.getIMEI(ADActivity.this)
                                        + "&cellPhone="
                                        + SharedPreferenceUtil.getInstance(
                                        ADActivity.this).getString(
                                        "cellphone", "") + "&phoneModel="
                                        + Build.BRAND + " "
                                        + Build.MODEL
                                        + "&phoneSystemVersion=" + "Android "
                                        + Build.VERSION.RELEASE
                                        + "&time=" + System.currentTimeMillis()
                                        + "&payState=1";
                            } else {
                                payStateUrl = payStateUrl
                                        + "?system="
                                        + CommUtil.getSource()
                                        + "_"
                                        + Global.getCurrentVersion(ADActivity.this)
                                        + "&imei="
                                        + Global.getIMEI(ADActivity.this)
                                        + "&cellPhone="
                                        + SharedPreferenceUtil.getInstance(
                                        ADActivity.this).getString(
                                        "cellphone", "") + "&phoneModel="
                                        + Build.BRAND + " "
                                        + Build.MODEL
                                        + "&phoneSystemVersion=" + "Android "
                                        + Build.VERSION.RELEASE
                                        + "&time=" + System.currentTimeMillis()
                                        + "&payState=1";
                            }
                            Log.e("TAG", "支付宝payStateUrl = " + payStateUrl);
                            mPDialog.showDialog();
                            if (mAgentWeb != null) {
                                mAgentWeb.getUrlLoader().loadUrl(payStateUrl); // 刷新
                            }
                        }
                        goPayResult();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShort(ADActivity.this, "支付结果确认中!");
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                        } else {
                            ToastUtil
                                    .showToastShort(ADActivity.this, "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                        mPDialog.showDialog();
                        PayUtils.payByAliPay(ADActivity.this, orderStr, aliHandler,
                                mPDialog);
                    } else {
                        ToastUtil.showToastShortBottom(ADActivity.this, "支付参数错误");
                    }
                    break;
                case 1:
                    finishWithAnimation();
                    break;
            }
        }
    };

    private void goPay() {
        if (payType == 1) {
            if (appid != null && !TextUtils.isEmpty(appid) && noncestr != null
                    && !TextUtils.isEmpty(noncestr) && packageValue != null
                    && !TextUtils.isEmpty(packageValue) && partnerid != null
                    && !TextUtils.isEmpty(partnerid) && prepayid != null
                    && !TextUtils.isEmpty(prepayid) && sign != null
                    && !TextUtils.isEmpty(sign) && timestamp != null
                    && !TextUtils.isEmpty(timestamp)) {
                spUtil.saveInt("payway", 1);
                // 微信支付
                PayUtils.weChatPayment(ADActivity.this, appid, partnerid,
                        prepayid, packageValue, noncestr, timestamp, sign,
                        mPDialog);
            } else {
                ToastUtil.showToastShortBottom(ADActivity.this, "支付参数错误");
            }
        } else if (payType == 2) {
            if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                spUtil.saveInt("payway", 2);
                // 判断是否安装支付宝
                PayUtils.checkAliPay(ADActivity.this, aliHandler);
            } else {
                ToastUtil.showToastShortBottom(ADActivity.this, "支付参数错误");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        if (event != null) {
            String curl = "javascript:init_roll('" + spUtil.getString("cellphone", "") + "')";
            if (mAgentWeb != null) {
                mAgentWeb.getUrlLoader().loadUrl(curl); // 刷新
            }
        }
    }

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
                UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
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
        CommUtil.getLatestVersion(this, Global.getCurrentVersion(this),
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
                                                Global.getCurrentVersion(mContext));
                                if (isLatest) {// 需要下载安装最新版
                                    if (isUpgrade == 1) {
                                        // 强制升级
                                        UpdateUtil.showForceUpgradeDialog(mContext, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(mContext,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(mContext,
                                                                    downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                        }
                                                    }
                                                });
                                    } else if (isUpgrade == 0) {
                                        // 非强制升级
                                        UpdateUtil.showUpgradeDialog(mContext, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(mContext,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(mContext,
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    UpdateUtil.updateApk(mContext,
                            downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                } else {
                    ToastUtil.showToastLong(this, "请打开存储权限");
                }
                break;
            default:
                break;
        }
    }

    private void startAct(Context context, int previous, int orderid,
                          String url, int workerId, int workerLevel, int serviceid,
                          int servicetype, Class clazz, int petid) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("previous", previous);
        intent.putExtra("orderid", orderid);
        intent.putExtra("id", workerId);
        intent.putExtra("workerLevel", workerLevel);
        if (url != null && !TextUtils.isEmpty(url))
            intent.putExtra("url", url);
        intent.putExtra("serviceid", serviceid);
        intent.putExtra("servicetype", servicetype);
        intent.putExtra("petid", petid);
        context.startActivity(intent);
    }

    private void JumpToNext(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        getIntent().putExtra(Global.ANIM_DIRECTION(),
                Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("previous", Global.H5_TO_LOGIN);
        intent.putExtra("loginurl", getUrl());
        startActivityForResult(intent, Global.H5_TO_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.AD_TO_LOGIN) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String curl = "javascript:init_roll('"
                                + spUtil.getString("cellphone", "") + "')";
                        if (mAgentWeb != null) {
                            mAgentWeb.getUrlLoader().loadUrl(curl); // 刷新
                        }
                    }
                });
            } else if (requestCode == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String curl = data.getStringExtra("payurl");
                        String locUrl = null;
                        if (curl != null) {
                            if (curl.contains("?")) {
                                locUrl = curl
                                        + "&system="
                                        + CommUtil.getSource()
                                        + "_"
                                        + Global.getCurrentVersion(ADActivity.this)
                                        + "&imei="
                                        + Global.getIMEI(ADActivity.this)
                                        + "&cellPhone="
                                        + SharedPreferenceUtil.getInstance(
                                        ADActivity.this).getString(
                                        "cellphone", "")
                                        + "&phoneModel="
                                        + Build.BRAND + " "
                                        + Build.MODEL
                                        + "&phoneSystemVersion=" + "Android "
                                        + Build.VERSION.RELEASE
                                        + "&time=" + System.currentTimeMillis();
                            } else {
                                locUrl = curl
                                        + "?system="
                                        + CommUtil.getSource()
                                        + "_"
                                        + Global.getCurrentVersion(ADActivity.this)
                                        + "&imei="
                                        + Global.getIMEI(ADActivity.this)
                                        + "&cellPhone="
                                        + SharedPreferenceUtil.getInstance(
                                        ADActivity.this).getString(
                                        "cellphone", "")
                                        + "&phoneModel="
                                        + Build.BRAND + " "
                                        + Build.MODEL
                                        + "&phoneSystemVersion=" + "Android "
                                        + Build.VERSION.RELEASE
                                        + "&time=" + System.currentTimeMillis();
                            }
                        }
                        if (mAgentWeb != null) {
                            mAgentWeb.getUrlLoader().loadUrl(locUrl); // 刷新
                        }
                    }
                });
            } else if (requestCode == Global.H5_TO_LOGIN) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String curl = "javascript:init_roll('" + spUtil.getString("cellphone", "") + "')";
                        if (mAgentWeb != null) {
                            mAgentWeb.getUrlLoader().loadUrl(curl); // 刷新
                        }
                    }
                });
            } else if (requestCode == Global.WEBVIEW_TO_ADDRESS) {
                mallAddress = (MallAddress) data.getSerializableExtra("mallAddress");
                if (mallAddress != null) {
                    Log.e("TAG", "mallAddress = " + mallAddress.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String curl = "javascript:setMallAddress('" + mallAddress.id + "','" + mallAddress.consigner + "','" + mallAddress.mobile + "','" + mallAddress.areaName + " " + mallAddress.address + "')";
                            if (mAgentWeb != null) {
                                mAgentWeb.getUrlLoader().loadUrl(curl); // 刷新
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
        if (mLClient != null) {
            mLClient.unRegisterLocationListener(mListener);
            mLClient.stop();
        }
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * AgentWeb 是用自己的权限机制的 ，true 该Url对应页面请求定位权限拦截 ，false 默认允许。
         * @param url
         * @param permissions
         * @param action
         * @return
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.d(TAG, "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);
            return false;
        }
    };


    /**
     * 更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            Log.d(TAG, "onStart:" + url);
            extra.setOpenBreakPointDownload(true) // 是否开启断点续传
                    .setIcon(R.drawable.ic_file_download_black_24dp) //下载通知的icon
                    .setConnectTimeOut(6000) // 连接最大时长
                    .setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setDownloadTimeOut(Long.MAX_VALUE) // 下载最大时长
                    .setParallelDownload(false)  // 串行下载更节省资源哦
                    .setEnableIndicator(true)  // false 关闭进度通知
                    .addHeader("Cookie", "xx") // 自定义请求头
                    .setAutoOpen(true) // 下载完成自动打开
                    .setForceDownload(true); // 强制下载，不管网络网络类型
            return false;
        }

        /**
         *
         * 不需要暂停或者停止下载该方法可以不必实现
         * @param url
         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
         */
        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            mDownloadingService = downloadingService;
            Log.d(TAG, "onBindService:" + url + "  DownloadingService:" + downloadingService);
        }

        /**
         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
         * @param url
         * @param downloadingService
         */
        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
            mDownloadingService = null;
            Log.d(TAG, "onUnbindService:" + url);
        }

        /**
         *
         * @param url  下载链接
         * @param loaded  已经下载的长度
         * @param length    文件的总大小
         * @param usedTime   耗时 ，单位ms
         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
         */
        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
            Log.d(TAG, "onProgress:" + mProgress);
            super.onProgress(url, loaded, length, usedTime);
        }

        /**
         *
         * @param path 文件的绝对路径
         * @param url  下载地址
         * @param throwable    如果异常，返回给用户异常
         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
         */
        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            if (null == throwable) { //下载成功
                //do you work
            } else {//下载失败

            }
            return false; // true  不会发出下载完成的通知 , 或者打开文件
        }
    };

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));
            }
        };
    }

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d(TAG, "onProgressChanged:" + newProgress + "  view:" + view);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (tvWebviewTitle != null && !TextUtils.isEmpty(title)) {
                if (title.length() > 10) {
                    title = title.substring(0, 10).concat("...");
                }
            }
            tvWebviewTitle.setText(title);
        }
    };

    protected WebViewClient mWebViewClient = new WebViewClient() {

        private HashMap<String, Long> timer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }*/

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            Log.d(TAG, "view:" + new Gson().toJson(view.getHitTestResult()));
            Log.d(TAG, "mWebViewClient shouldOverrideUrlLoading:" + url);
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://") && url.contains("com.youku.phone")) {
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "mUrl:" + url + " onPageStarted  target:" + getUrl());
            timer.put(url, System.currentTimeMillis());
            if (url.equals(getUrl())) {
                pageNavigator(View.GONE);
            } else {
                pageNavigator(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
                Log.d(TAG, "  page mUrl:" + url + "  used time:" + (overTime - startTime));
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    };

    private void pageNavigator(int tag) {
        llWebviewBack.setVisibility(tag);
        viewWebviewLine.setVisibility(tag);
    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();//恢复
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * MiddlewareWebClientBase 是 AgentWeb 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 AgentWeb 提供的功能， 不想重写 WebClientView方
     * 法覆盖AgentWeb提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected MiddlewareWebClientBase getMiddlewareWebClient() {
        return this.mMiddleWareWebClient = new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("agentweb")) { // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
                    Log.d(TAG, "agentweb scheme ~");
                    return true;
                }

                if (super.shouldOverrideUrlLoading(view, url)) { // 执行 DefaultWebClient#shouldOverrideUrlLoading
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected MiddlewareWebChromeBase getMiddlewareWebChrome() {
        return this.mMiddleWareWebChrome = new MiddlewareChromeClient() {
        };
    }
}
