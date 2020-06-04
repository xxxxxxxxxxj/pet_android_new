package com.haotang.pet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GetDeviceId;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SystemBarTint;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

public class FlashActivity extends SuperActivity {
    private SharedPreferenceUtil spUtil;
    private String cellphone;
    private boolean guide;
    private SystemBarTint mtintManager;
    protected String img_url;
    protected String jump_url;
    private Timer timer;
    private TimerTask task;
    private static int FLASH_DELAYEDTIME = 900;
    private String backup;
    private int point;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private ImageView iv_flash_icon;
    private String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int mypetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mtintManager = new SystemBarTint(this);
        setStatusBarColor(Color.parseColor("#ff0099cc"));
        Utils.hideBottomUIMenu(mContext);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // 结束你的activity
            finish();
            return;
        }
        setContentView(R.layout.flash);
        MobclickAgent.setDebugMode(true);
        spUtil = SharedPreferenceUtil.getInstance(this);
        spUtil.saveBoolean("tag_isNewComer", false);
        spUtil.saveBoolean("MAINFRAG_DIALOG", false);
        spUtil.saveBoolean("SHOPFRAG_DIALOG", false);
        spUtil.saveBoolean("isPerformTeethCard", false);
        cellphone = spUtil.getString("cellphone", "");
        guide = spUtil.getBoolean("guide", false);
        spUtil.saveBoolean("isExit", true);
        spUtil.saveInt("isFirstLogin", 0);
        spUtil.saveInt("changePet", 0);
        spUtil.saveInt("OPEN_NUM", spUtil.getInt("OPEN_NUM", 0) + 1);//记录打开app的次数
        spUtil.saveBoolean("isRestart", true);
        spUtil.saveBoolean("PETINFODELAYEDGONE", true);
        spUtil.removeData("TAG_MAINACTIVITY_ACTIVITY_EVERYONE");
        spUtil.removeData("TAG_SHOPPINGMALLFRAG_ACTIVITY_EVERYONE");
        iv_flash_icon = (ImageView) findViewById(R.id.iv_flash_icon);
        String deviceId = GetDeviceId.readDeviceID(mContext);
        if (!Utils.isStrNull(deviceId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermisson();
            } else {
                GetDeviceId.saveUniqueId(mContext);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 3000);
    }

    private void initTimer(final int flag) {
        task = new TimerTask() {
            @Override
            public void run() {
                //if (guide) {
                if (flag == 1) {
                    goNext(StartPageActivity.class);
                } else {
                    autoLogin(0, 0);
                }
               /* } else {
                    goNext(GuideActivity.class);
                }*/
            }
        };
        timer = new Timer();
        timer.schedule(task, FLASH_DELAYEDTIME);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnim();
                }
            }, 1000);
        }
    }

    private void startAnim() {
        //创建一个AnimationDrawable
        final AnimationDrawable animationDrawable = (AnimationDrawable) iv_flash_icon.getBackground();
        animationDrawable.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
    }

    /**
     * 自动登录获取数据
     */
    private void autoLogin(double lat, double lng) {
        /*String cellphone = spUtil.getString("cellphone", "");
        int userid = spUtil.getInt("userid", 0);
        if (!"".equals(cellphone) && 0 != userid) {*/
        CommUtil.loginAuto(this, spUtil.getString("cellphone", ""),
                spUtil.getString("IMEI", ""), Global.getCurrentVersion(this),
                spUtil.getInt("userid", 0), lat, lng, autoLoginHandler);
        //}
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
                        goNext(MainActivity.class);
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
                    goNext(LoginNewActivity.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            goNext(LoginNewActivity.class);
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

    private void getData() {
        CommUtil.startPage(cellphone, mContext, 0, spUtil.getInt("isFirstLogin", 0), startPageHandler);
    }

    private AsyncHttpResponseHandler startPageHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("启动页：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("backup") && !jData.isNull("backup")) {
                            backup = jData.getString("backup");
                        }
                        if (jData.has("point") && !jData.isNull("point")) {
                            point = jData.getInt("point");
                        }
                        if (jData.has("imgUrl") && !jData.isNull("imgUrl")) {
                            img_url = jData.getString("imgUrl");
                        }
                        if (jData.has("jumpUrl") && !jData.isNull("jumpUrl")) {
                            jump_url = jData.getString("jumpUrl");
                        }
                        if (img_url != null
                                && !TextUtils.isEmpty(img_url)) {
                            initTimer(1);
                        } else {
                            initTimer(0);
                        }
                    } else {
                        initTimer(0);
                    }
                } else {
                    initTimer(0);
                }
            } catch (Exception e) {
                initTimer(0);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            initTimer(0);
        }
    };

    protected void setStatusBarColor(int colorBurn) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = setImmerseLayout();
            window.setStatusBarColor(colorBurn);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mtintManager != null) {
                mtintManager.setStatusBarTintEnabled(true);
                mtintManager.setStatusBarTintColor(colorBurn);
            }
        }
    }

    private void goNext(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("img_url", img_url);
        intent.putExtra("jump_url", jump_url);
        intent.putExtra("backup", backup);
        intent.putExtra("point", point);
        intent.putExtra("previous",Global.FLASHACTIVITY_TOLOGIN);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FlashActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FlashActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    GetDeviceId.saveUniqueId(mContext);
                } else {
                    ToastUtil.showToastLong(this, "请打开所需权限");
                }
                break;
            default:
                break;
        }
    }
}
