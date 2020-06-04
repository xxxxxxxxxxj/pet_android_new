package com.haotang.pet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.LoginBgVedioInfo;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.WXLoginEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.net.BinaryHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GetDeviceId;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.GsonUtil;
import com.haotang.pet.util.MD5;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
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
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginNewActivity extends SuperActivity {

    @BindView(R.id.iv_wechat_login)
    ImageView ivWechatLogin;
    @BindView(R.id.rl_login_getcode)
    RelativeLayout rlLoginGetcode;
    @BindView(R.id.tv_login_agreement)
    TextView tvLoginAgreement;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.iv_login_clear)
    ImageView ivPhoneClear;
    @BindView(R.id.tv_login_getcode)
    TextView tvGetCode;

    @BindView(R.id.sv_bg)
    SurfaceView svBg;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mSecondMediaplayer;
    private SurfaceHolder mSurfaceHolder;
    private AssetManager assetManager;
    private final String LOGIN_BG_VIDEO_FILE_NAME = "login_bg_video.mp4";
    private final String SP_TAG_LOGIN_BG_VIDEO_VERSION = "sp_tag_login_bg_video_version";
    private int mNewLoginBgVideoVersion; // 新登录背景视频版本号

    private String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final static int REQUEST_READ_PHONE_STATE = 1;
    public static LoginNewActivity act;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private int provise;
    private int mypetId;
    private double lat = 0;
    private double lng = 0;
    private IWXAPI api;
    private String slat_md5;
    private String backup;
    private int point;
    private String img_url;
    private String jump_url;
    private String city;
    private String weiXinCode;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXLoginEvent event) {
        if (event != null) {
            switch (event.getErrCode()){
                //取消授权
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtil.showImageToast(LoginNewActivity.this,"取消授权",R.drawable.icon_close_gary);
                    break;
                //用户拒绝授权
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                    ToastUtil.showToastShortAddIconCenter(LoginNewActivity.this,"拒绝授权",R.dr);
                    ToastUtil.showImageToast(LoginNewActivity.this,"拒绝授权",R.drawable.icon_close_gary);
                    break;

                case BaseResp.ErrCode.ERR_OK:
                    weiXinCode = event.getCode();
                    Utils.mLogError("验证是否已经绑定手机号");
                    //验证是否已经绑定手机号
                    mPDialog.showDialog();
                    CommUtil.ticket(LoginNewActivity.this,event.getCode(),ticketHandler);
                    break;
            }

        }
    }
    //检查手机号是否绑定
    private AsyncHttpResponseHandler ticketHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("openId") && !object.isNull("openId")) {
                        }
                        if (object.has("wxNickname") && !object.isNull("wxNickname")) {
                            String wxNickname = object.getString("wxNickname");
                            spUtil.saveString("wxNickname",wxNickname);
                        }

                        if (object.has("cellPhone") && !TextUtils.isEmpty(object.getString("cellPhone"))) {
                            //有手机号 调用登录接口
                            CommUtil.loginAu(LoginNewActivity.this,object.getString("cellPhone"),Global.getIMEI(mContext),weiXinCode,
                                    lat,lng,object.getString("openId"),"quick_login_app",object.getString("wxAvatar"),
                                    object.getString("wxNickname"),loginHandler);

                        }else{
                            Utils.mLogError("登录, "+(object.has("cellPhone")?object.getString("cellPhone"): "没有手机号"));
                            //没有手机号
                            Utils.mLogError("打开绑定页面 ");
                            Intent intent = new Intent(mContext,BindPhoneActivity.class);
                            intent.putExtra("code",weiXinCode);
                            intent.putExtra("phone",etLoginPhone.getText().toString().trim());
                            intent.putExtra("lat",lat);
                            intent.putExtra("lng",lng);
                            intent.putExtra("img_url",img_url);
                            intent.putExtra("jump_url",jump_url);
                            intent.putExtra("backup",backup);
                            intent.putExtra("point",point);
                            //传入 openid wxNickname wxAvatar
                            intent.putExtra("openId",object.getString("openId"));
                            intent.putExtra("wxNickname",object.getString("wxNickname"));
                            intent.putExtra("wxAvatar",object.getString("wxAvatar"));
                            startActivity(intent);
                            //暂停播放释放资源
                            releaseSource();

                        }
                    }
                }
            } catch (JSONException e) {
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

    //登录
    private AsyncHttpResponseHandler loginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jData = jobj.getJSONObject("data");
                    if (jData.has("user") && !jData.isNull("user")) {
                        JSONObject jUser = jData.getJSONObject("user");
                        Utils.mLogError("微信绑定信息："+jUser.isNull("openId")+jUser.getString("openId"));
                        //保存微信信息
                        if(jUser.has("openId") && !jUser.isNull("openId")){
                            spUtil.saveString("wxNickname",jUser.getString("wxNickName"));
                        }else{
                            spUtil.saveString("wxNickname", null);
                        }
                        if (jUser.has("cityId") && !jUser.isNull("cityId")) {
                            spUtil.saveInt("cityId",
                                    jUser.getInt("cityId"));
                        }
                        if (jUser.has("btlCouponCount") && !jUser.isNull("btlCouponCount")) {
                            spUtil.saveInt("btlCouponCount",
                                    jUser.getInt("btlCouponCount"));
                        }
                        if (jUser.has("cashback") && !jUser.isNull("cashback")) {
                            spUtil.saveFloat("cashback",
                                    (float) jUser.getDouble("cashback"));
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
                        if (jUser.has("isFirstLogin") && !jUser.isNull("isFirstLogin")) {
                            spUtil.saveInt("isFirstLogin", jUser.getInt("isFirstLogin"));
                        } else {
                            spUtil.removeData("isFirstLogin");
                        }

                        if (jUser.has("myPetMaximum") && !jUser.isNull("myPetMaximum")) {
                            spUtil.saveInt("myPetMaximum", jUser.getInt("myPetMaximum"));
                        } else {
                            spUtil.removeData("myPetMaximum");
                        }
                        if (jUser.has("memberLevelId") && !jUser.isNull("memberLevelId")) {
                            spUtil.saveInt("shopCartMemberLevelId", jUser.getInt("memberLevelId"));
                        } else {
                            spUtil.removeData("shopCartMemberLevelId");
                        }
                        if (jUser.has("areacode") && !jUser.isNull("areacode")) {
                            spUtil.saveInt("upRegionId",
                                    jUser.getInt("areacode"));
                        } else {
                            spUtil.removeData("upRegionId");
                        }
                        if (jUser.has("shopName") && !jUser.isNull("shopName")) {
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
                        if (jUser.has("userName") && !jUser.isNull("userName")) {
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
                        } else {
                            spUtil.removeData("userid");
                        }
                        if (jUser.has("areaId") && !jUser.isNull("areaId")) {
                            spUtil.saveInt("areaid", jUser.getInt("areaId"));
                        } else {
                            spUtil.removeData("areaid");
                        }
                        if (jUser.has("areaName") && !jUser.isNull("areaName")) {
                            spUtil.saveString("areaname",
                                    jUser.getString("areaName"));
                        } else {
                            spUtil.removeData("areaname");
                        }
                        if (jUser.has("pet") && !jUser.isNull("pet")) {
                            JSONObject jPet = jUser.getJSONObject("pet");
                            if (jPet.has("id")
                                    && !jPet.isNull("id")) {
                                spUtil.saveInt("petid",
                                        jPet.getInt("id"));
                            }
                            if (jPet.has("isCerti") && !jPet.isNull("isCerti")) {
                                spUtil.saveInt("isCerti",
                                        jPet.getInt("isCerti"));
                            }
                            if (jPet.has("mypetId") && !jPet.isNull("mypetId")) {
                                mypetId = jPet.getInt("mypetId");
                            } else {
                                mypetId = 0;
                            }
                            if (jPet.has("PetId") && !jPet.isNull("PetId")) {
                                spUtil.saveInt("petid", jPet.getInt("PetId"));
                            }

                            if (jPet.has("petKind") && !jPet.isNull("petKind")) {
                                spUtil.saveInt("petkind",
                                        jPet.getInt("petKind"));
                            }
                            if (jPet.has("petName") && !jPet.isNull("petName")) {
                                spUtil.saveString("petname",
                                        jPet.getString("petName"));
                            }
                            if (jPet.has("avatarPath")
                                    && !jPet.isNull("avatarPath")) {
                                spUtil.saveString("petimage",
                                        jPet.getString("avatarPath"));
                            }
                            if (jPet.has("availService")
                                    && !jPet.isNull("availService")) {
                                JSONArray jarr = jPet
                                        .getJSONArray("availService");
                                if (jarr.length() > 0) {
                                    MainActivity.petServices = new int[jarr
                                            .length()];
                                    for (int i = 0; i < jarr.length(); i++) {
                                        MainActivity.petServices[i] = jarr
                                                .getInt(i);
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
                            JSONObject jMyPet = jUser.getJSONObject("myPet");
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
                                spUtil.saveString("lat", jAddr.getDouble("lat")
                                        + "");
                            }
                            if (jAddr.has("lng") && !jAddr.isNull("lng")) {
                                spUtil.saveString("lng", jAddr.getDouble("lng")
                                        + "");
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
                    }
                    EventBus.getDefault().post(new LoginSuccessEvent(true));
                    spUtil.saveBoolean("isMainGetActivity", true);
                    spUtil.saveBoolean("isShopGetActivity", true);

                    if (MApplication.listAppoint1.size() > 0) {
                        for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                            MApplication.listAppoint1.get(i).finish();
                        }
                    }
                    MApplication.listAppoint1.clear();
                    setResult(Global.RESULT_OK);
                    act = null;
                    goNext(MainActivity.class);
//                    finishWithAnimation();
                }
                else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(mContext, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(LoginNewActivity.this, "数据异常");
                Log.e("TAG", "e = " + e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(LoginNewActivity.this, "请求失败");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setFlags(flag, flag);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        getIMEI();
        initData();
        initBD();
        regToWx();
        setView();
        setListener();
        Utils.mLogError("Video 重新初始化 nCreate");
        initMediaPlayer();
    }

    private void initData() {
        Intent intent = getIntent();
        img_url = intent.getStringExtra("img_url");
        jump_url = intent.getStringExtra("jump_url");
        backup = intent.getStringExtra("backup");
        point = intent.getIntExtra("point",-1);
        provise = intent.getIntExtra("previous",0);
        act = this;
        MApplication.listAppoint1.add(act);

        // 获取登录背景视频信息
        getLoginBgVideoInfo();
    }

    private void setListener() {

        etLoginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = etLoginPhone.getText().toString();
                if (content.length()>0){
                    ivPhoneClear.setVisibility(View.VISIBLE);
                }else {
                    ivPhoneClear.setVisibility(View.GONE);
                }
                if (content.length()<11){
                    rlLoginGetcode.setClickable(false);
                    rlLoginGetcode.setBackgroundResource(R.drawable.bg_login_clickfalse);
                    tvGetCode.setTextColor(Color.parseColor("#FF3A1E"));
                }else {
                    rlLoginGetcode.setClickable(true);
                    rlLoginGetcode.setBackgroundResource(R.drawable.bg_login_clicktrue);
                    tvGetCode.setTextColor(Color.parseColor("#FFFFFF"));
                    Utils.goneJP(mContext);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getInstance(this).getPhoneSave())){
            etLoginPhone.setText(SharedPreferenceUtil.getInstance(this).getPhoneSave());
            etLoginPhone.setSelection(SharedPreferenceUtil.getInstance(this).getPhoneSave().length());//将光标移至文字末尾
        }

    }

    private void setView() {
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        rlLoginGetcode.setClickable(false);
        Utils.mLogError("升级Androidx");
    }


    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Global.APP_ID, false);
        api.registerApp(Global.APP_ID);
    }

    private void initBD() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(100);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        Utils.mLogError("fixhot");
    }

    private void goNext(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("img_url", img_url);
        intent.putExtra("jump_url", jump_url);
        intent.putExtra("backup", backup);
        intent.putExtra("point", point);
        startActivity(intent);
        finish();
    }

    private void getWxInfo(String code) {
        mPDialog.showDialog();

    }

    private AsyncHttpResponseHandler codeHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @OnClick({R.id.iv_login_close, R.id.rl_login_getcode, R.id.tv_login_agreement, R.id.iv_wechat_login,R.id.iv_login_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wechat_login:
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(this, "您还没有安装微信!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "weidu_wx_login";
                api.sendReq(req);
                //暂停播放资源
                releaseSource();
                break;
            case R.id.iv_login_clear:
                etLoginPhone.setText("");
                break;
            case R.id.iv_login_close:
                if (provise==Global.FLASHACTIVITY_TOLOGIN){
                    goNext(MainActivity.class);
                }else {
                    finishWithAnimation();
                }
                break;
            case R.id.rl_login_getcode:
                slat_md5 = MD5.md5(Global.MD5_STR, etLoginPhone.getText().toString().trim().replace(" ", ""));
                CommUtil.genVerifyCode(this, etLoginPhone.getText().toString().trim().replace(" ", ""),  slat_md5,
                        0, codeHandler);
                Intent verificationIntent = new Intent(LoginNewActivity.this,VerificationCodeActivity.class);
                verificationIntent.putExtra("phone",etLoginPhone.getText().toString().trim());
                verificationIntent.putExtra("lat",lat);
                verificationIntent.putExtra("lng",lng);
                verificationIntent.putExtra("img_url",img_url);
                verificationIntent.putExtra("jump_url",jump_url);
                verificationIntent.putExtra("backup",backup);
                verificationIntent.putExtra("point",point);
                verificationIntent.putExtra("previous",provise);
                startActivity(verificationIntent);
                releaseSource();
                break;
            case R.id.tv_login_agreement:
                String url = CommUtil.getStaticUrl() + "static/content/html5/protocol.html?system=" +
                        CommUtil.getSource() + "_" + Global.getCurrentVersion(this) + "&imei=" +
                        Global.getIMEI(this) + "&cellPhone=" +
                        SharedPreferenceUtil.getInstance(this).getString("cellphone", "") + "&time=";
                Intent intent = new Intent(LoginNewActivity.this,
                        ADActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                releaseSource();
                break;
        }
    }

    private class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();// 纬度
            lng = location.getLongitude();// 经度
            city = location.getCity();
            Utils.mLogError("city======="+city);
            if (city.equals("北京市")){
                spUtil.saveInt("cityId",1);
            }else if (city.equals("深圳市")){
                spUtil.saveInt("cityId",2);
            }else {
                spUtil.saveInt("cityId",1);
            }
            mLocationClient.stop();
        }
    }

    private void getIMEI() {
        String deviceId = GetDeviceId.readDeviceID(mContext);
        if (!Utils.isStrNull(deviceId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermisson();
            } else {
                GetDeviceId.saveUniqueId(mContext);
            }
        }
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
        EventBus.getDefault().unregister(this);

        releaseSource();
    }

    /**
     * 释放正在播放的资源，不需要赋值为null，重上一个界面返回可能还会用到
     */
    private void releaseSource(){
        if (mMediaPlayer != null){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mSecondMediaplayer != null){
            if (mSecondMediaplayer.isPlaying()){
                mSecondMediaplayer.stop();
            }
            mSecondMediaplayer.release();
            mSecondMediaplayer = null;
        }
    }

    private void initMediaPlayer() {
        Utils.mLogError("Video 1 初始化视频 "+ mMediaPlayer);

        mSurfaceHolder = svBg.getHolder();
        assetManager = getAssets();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaPlayer = new MediaPlayer();
                Utils.mLogError("Video 2 给mMediaPlayer添加预览的SurfaceHolder "+mMediaPlayer +" holder : "+holder);
                mMediaPlayer.setDisplay(holder);//给mMediaPlayer添加预览的SurfaceHolder
                mSecondMediaplayer = new MediaPlayer();
                mSecondMediaplayer.setDisplay(holder);
                setPlayVideo();//添加播放视频的路径
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void setPlayVideo() {
        String sdcardLoginBgVideoPath = Utils.getHaoTangSdcardPath() + File.separator + LOGIN_BG_VIDEO_FILE_NAME;
        try {
            if (Utils.isFileExists(sdcardLoginBgVideoPath)) { // 判断是否sdcard上有下载的视频，如果没有播放assers里的默认视频
                Utils.mLogError("Video 3 设置资源文件");
                mMediaPlayer.setDataSource(sdcardLoginBgVideoPath);
            } else {
                Utils.mLogError("Video 3 Asset  设置资源文件");
                AssetFileDescriptor fileDescriptor = assetManager.openFd("video_test.mp4");
                mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
            }

            mMediaPlayer.setLooping(true);//设置循环播放
            mMediaPlayer.prepareAsync();//异步准备
            setSilence(mMediaPlayer);
//            mMediaPlayer.prepare();//同步准备,因为是同步在一些性能较差的设备上会导致UI卡顿
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { //准备完成回调
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSilence(MediaPlayer player){
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setVolume(0,0);
    }

    /**
     * 获取登录背景视频信息
     */
    private void getLoginBgVideoInfo() {
        CommUtil.getLoginBgVideoInfo(this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                LoginBgVedioInfo loginBgVedioInfo = null;
                try {
                    String response = new String(responseBody);
                    loginBgVedioInfo = GsonUtil.gsonToBean(response, LoginBgVedioInfo.class);
                    if (loginBgVedioInfo.getCode() == 0) {
                        int version = SharedPreferenceUtil.getInstance(LoginNewActivity.this).getInt(SP_TAG_LOGIN_BG_VIDEO_VERSION, 0);
                        mNewLoginBgVideoVersion = loginBgVedioInfo.getData().getVersion();
                        if (version != mNewLoginBgVideoVersion) { // 本地版本号与新版本号比较
                            getLoginBgVideo(loginBgVedioInfo.getData().getFile());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.mLogError("ah  onFailure statusCode == "+statusCode);
            }
        });
    }

    /**
     * 下载登录背景视频
     * @param videoUrl
     */
    private void getLoginBgVideo(String videoUrl) {

        CommUtil.getLoginBgVideo(this, videoUrl, new BinaryHttpResponseHandler() {

            @Override
            public String[] getAllowedContentTypes() {
                return new String[]{".*"};
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                if (binaryData.length > 0) {
                    if (Utils.saveDataToSdcard(LOGIN_BG_VIDEO_FILE_NAME, binaryData)) {
                        // 记录新的视频版本号
                        SharedPreferenceUtil.getInstance(LoginNewActivity.this).saveInt(SP_TAG_LOGIN_BG_VIDEO_VERSION,mNewLoginBgVideoVersion);
                        Utils.mLogError("ah  saveDataToSdcard == Success == " + binaryData.length);
                        try {
                            // 切换播放的视频
                            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                                mMediaPlayer.stop();
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }
                            Utils.mLogError("Video 3  网络资源文件");
                            if(mSecondMediaplayer != null){
                                mSecondMediaplayer.setDataSource(Utils.getHaoTangSdcardPath()+ File.separator+LOGIN_BG_VIDEO_FILE_NAME);
                                mSecondMediaplayer.setLooping(true);//设置循环播放
                                mSecondMediaplayer.prepareAsync();//异步准备
                                setSilence(mSecondMediaplayer);
                                mSecondMediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { //准备完成回调
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.mLogError("ah  saveDataToSdcard == Failure  " + binaryData.length);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Utils.mLogError("ah  onFailure binaryData ==   error== "+error.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LoginNewActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
        MobclickAgent.onPause(this);
    }
}
