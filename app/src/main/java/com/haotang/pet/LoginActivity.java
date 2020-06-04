package com.haotang.pet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.RegisterCoupon;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GetDeviceId;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MD5;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 登录页
 */
public class LoginActivity extends SuperActivity implements OnClickListener {
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    private EditText et_userid_num, edt_verifkey;
    private Button bt_get, button_login_sure;
    private ImageView cbAgree;
    private TimeCount time;
    private TimeYuYin timeYuYin;
    private int code = 0;
    private Context context;
    private int previous;
    private SharedPreferenceUtil spUtil;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private double lat = 0;
    private double lng = 0;
    private int isOpen;
    private String slat_md5;
    private Intent fIntent;
    public static Activity act;
    Set<String> tagSet = new LinkedHashSet<String>();
    private int orderid;
    private static final int MSG_SET_ALIASANDTAGS = 1001;
    private LinearLayout ll_login_agree;
    private boolean isSelect = true;
    protected int mypetId;
    private String dialogImg;
    private String title;
    private SelectableRoundedImageView sriv_login_imgver;
    private EditText et_login_imgver;
    private MProgressDialog pDialog;
    private RelativeLayout rl_login_imgver;
    private String mobileKey;
    private LinearLayout ll_login_phone;
    private TextView tv_login_phoneerror;
    private TextView tv_login_imgyzmerror;
    private RelativeLayout rl_login_yzm;
    private TextView tv_login_yzmerror;
    public String pictureOn = "";
    private TextView tvOO;
    private LinearLayout ll_login_fwxy;
    private LinearLayout layout_login_notice;
    private ImageView img_login_notice;
    private TextView textview_login_notice;
    private ArrayList<RegisterCoupon> listCoupon = new ArrayList<>();
    private boolean isYuYin = false;
    private int isNewComer;
    public static final int REQUEST_CODE_ASK_CALL_PHONE = 10111;
    private String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final static int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        getIMEI();
        initBD();
        setView();
        setLinister();
        //checkCallPermission();
        isOpenVoiceVerification();
    }

    private void getIMEI() {
        String deviceId = GetDeviceId.readDeviceID(mContext);
        if(!Utils.isStrNull(deviceId)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermisson();
            } else {
                GetDeviceId.saveUniqueId(mContext);
            }
        }
    }

    private void checkPermisson(){
        boolean flag=true;//默认全部被申请过
        for(int i=0;i<permissions.length;i++){//只要有一个没有申请成功
            if(!(ActivityCompat.checkSelfPermission(this,permissions[i])== PackageManager.PERMISSION_GRANTED)){
                flag=false;
            }
        }
        if(!flag){
            //动态申请权限
            ActivityCompat.requestPermissions(this,permissions,REQUEST_READ_PHONE_STATE);
        }else {
            GetDeviceId.saveUniqueId(mContext);
        }
    }
    private void getPictureOnUser() {
        pictureOn = "";
        //pDialog.showDialog();
        CommUtil.pictureOnUser(this, pictureOnUserHandler);
    }

    private void setView() {
        tv_titlebar_title.setText("登	录");
    }

    private void setLinister() {
        bt_get.setOnClickListener(this);
        ib_titlebar_back.setOnClickListener(this);
        button_login_sure.setOnClickListener(this);
        ll_login_agree.setOnClickListener(this);
        tvOO.setOnClickListener(this);
        sriv_login_imgver.setOnClickListener(this);
        layout_login_notice.setOnClickListener(this);
        textview_login_notice.setOnClickListener(this);
        et_userid_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    tv_login_phoneerror.setVisibility(View.GONE);
                    if (s == null || s.length() == 0) return;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < s.length(); i++) {
                        if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                            continue;
                        } else {
                            sb.append(s.charAt(i));
                            if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                                sb.insert(sb.length() - 1, ' ');
                            }
                        }
                    }
                    if (!sb.toString().equals(s.toString())) {
                        int index = start + 1;
                        if (sb.charAt(start) == ' ') {
                            if (before == 0) {
                                index++;
                            } else {
                                index--;
                            }
                        } else {
                            if (before == 1) {
                                index--;
                            }
                        }
                        et_userid_num.setText(sb.toString());
                        et_userid_num.setSelection(index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim().replace(" ", "");
                Log.e("TAG", "phone = " + phone);
                if (phone.length() == 11) {
                    boolean bool = Utils.checkPhone1(LoginActivity.this, et_userid_num);
                    if (bool) {
                        if (pictureOn.equals("1")) {
                            if (Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", "")) && Utils.isStrNull(edt_verifkey.getText().toString().trim()) && Utils.isStrNull(et_login_imgver.getText().toString().trim())) {
                                button_login_sure.setClickable(true);
                                button_login_sure.setEnabled(true);
                                button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                            } else {
                                et_login_imgver.requestFocus();
                                button_login_sure.setClickable(false);
                                button_login_sure.setEnabled(false);
                                button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                            }
                        } else {
                            if (Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", "")) && Utils.isStrNull(edt_verifkey.getText().toString().trim())) {
                                button_login_sure.setClickable(true);
                                button_login_sure.setEnabled(true);
                                button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                            } else {
                                edt_verifkey.requestFocus();
                                button_login_sure.setClickable(false);
                                button_login_sure.setEnabled(false);
                                button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                            }
                        }
                    } else {
                        if (pictureOn.equals("1")) {
                            if (Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", "")) && Utils.isStrNull(edt_verifkey.getText().toString().trim()) && Utils.isStrNull(et_login_imgver.getText().toString().trim())) {
                                button_login_sure.setClickable(true);
                                button_login_sure.setEnabled(true);
                                button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                            } else {
                                button_login_sure.setClickable(false);
                                button_login_sure.setEnabled(false);
                                button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                            }
                        } else {
                            if (Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", "")) && Utils.isStrNull(edt_verifkey.getText().toString().trim())) {
                                button_login_sure.setClickable(true);
                                button_login_sure.setEnabled(true);
                                button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                            } else {
                                button_login_sure.setClickable(false);
                                button_login_sure.setEnabled(false);
                                button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                            }
                        }
                        ll_login_phone.setAnimation(shakeAnimation(5));
                        tv_login_phoneerror.setVisibility(View.VISIBLE);
                        tv_login_phoneerror.setText("请输入正确的手机号");
                        Utils.goneJP(LoginActivity.this);
                    }
                } else {
                    if (pictureOn.equals("1")) {
                        if (Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", "")) && Utils.isStrNull(edt_verifkey.getText().toString().trim()) && Utils.isStrNull(et_login_imgver.getText().toString().trim())) {
                            button_login_sure.setClickable(true);
                            button_login_sure.setEnabled(true);
                            button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                        } else {
                            button_login_sure.setClickable(false);
                            button_login_sure.setEnabled(false);
                            button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                        }
                    } else {
                        if (Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", "")) && Utils.isStrNull(edt_verifkey.getText().toString().trim())) {
                            button_login_sure.setClickable(true);
                            button_login_sure.setEnabled(true);
                            button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                        } else {
                            button_login_sure.setClickable(false);
                            button_login_sure.setEnabled(false);
                            button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                        }
                    }
                }
            }
        });
        et_login_imgver.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tv_login_imgyzmerror.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isStrNull(edt_verifkey.getText().toString().trim()) && Utils.isStrNull(et_login_imgver.getText().toString().trim()) && Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", ""))) {
                    button_login_sure.setClickable(true);
                    button_login_sure.setEnabled(true);
                    button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                } else {
                    et_login_imgver.requestFocus();
                    button_login_sure.setClickable(false);
                    button_login_sure.setEnabled(false);
                    button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                }
            }
        });
        edt_verifkey.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tv_login_yzmerror.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pictureOn.equals("1")) {
                    if (Utils.isStrNull(edt_verifkey.getText().toString().trim()) && Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", "")) && Utils.isStrNull(et_login_imgver.getText().toString().trim())) {
                        button_login_sure.setClickable(true);
                        button_login_sure.setEnabled(true);
                        button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                    } else {
                        et_login_imgver.requestFocus();
                        button_login_sure.setClickable(false);
                        button_login_sure.setEnabled(false);
                        button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                    }
                } else {
                    if (Utils.isStrNull(edt_verifkey.getText().toString().trim()) && Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", ""))) {
                        button_login_sure.setClickable(true);
                        button_login_sure.setEnabled(true);
                        button_login_sure.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                    } else {
                        edt_verifkey.requestFocus();
                        button_login_sure.setClickable(false);
                        button_login_sure.setEnabled(false);
                        button_login_sure.setBackgroundResource(R.drawable.bg_huise_jianbian_icon);
                    }
                }
            }
        });
    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    private void initData() {
        context = this;
        pDialog = new MProgressDialog(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        timeYuYin = new TimeYuYin(60000, 100);
        act = this;
        spUtil = SharedPreferenceUtil.getInstance(LoginActivity.this);
        fIntent = getIntent();
        previous = fIntent.getIntExtra("previous", -1);
        orderid = fIntent.getIntExtra("orderid", -1);
    }

    private AsyncHttpResponseHandler pictureOnUserHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            //pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("pictureOn") && !jData.isNull("pictureOn")) {
                            pictureOn = jData.getString("pictureOn");
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(LoginActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                ToastUtil.showToastShortBottom(LoginActivity.this, "数据异常");
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
            }
            if (pictureOn.equals("1")) {
                //时间戳
                long l = System.currentTimeMillis();
                Log.e("TAG", "时间戳 = " + l);
                //产生6位数随机数
                int num = (int) ((Math.random() * 9 + 1) * 100000);
                Log.e("TAG", "随机数 = " + num);
                mobileKey = String.valueOf(l) + String.valueOf(num);
                Log.e("TAG", "时间戳+随机数 = " + mobileKey);
                //pDialog.showDialog();
                CommUtil.generateImageCode(LoginActivity.this, mobileKey,
                        generateImageCodeHandler);
            } else {
                rl_login_imgver.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            //pDialog.closeDialog();
            ToastUtil.showToastShortBottom(LoginActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler generateImageCodeHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                rl_login_imgver.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                sriv_login_imgver.setImageBitmap(bitmap);
            } catch (Exception e) {
                ToastUtil.showToastShortBottom(LoginActivity.this, "数据异常");
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(LoginActivity.this, "请求失败");
        }
    };

    private void initView() {
        setContentView(R.layout.activity_login);
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        et_userid_num = (EditText) findViewById(R.id.et_userid_num);
        edt_verifkey = (EditText) findViewById(R.id.edt_verifkey);
        bt_get = (Button) findViewById(R.id.bt_get);
        tvOO = (TextView) findViewById(R.id.tv_oo);
        button_login_sure = (Button) findViewById(R.id.button_login_sure);
        cbAgree = (ImageView) findViewById(R.id.iv_login_agree);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        ll_login_agree = (LinearLayout) findViewById(R.id.ll_login_agree);
        sriv_login_imgver = (SelectableRoundedImageView) findViewById(R.id.sriv_login_imgver);
        et_login_imgver = (EditText) findViewById(R.id.et_login_imgver);
        rl_login_imgver = (RelativeLayout) findViewById(R.id.rl_login_imgver);
        ll_login_phone = (LinearLayout) findViewById(R.id.ll_login_phone);
        tv_login_phoneerror = (TextView) findViewById(R.id.tv_login_phoneerror);
        tv_login_imgyzmerror = (TextView) findViewById(R.id.tv_login_imgyzmerror);
        rl_login_yzm = (RelativeLayout) findViewById(R.id.rl_login_yzm);
        tv_login_yzmerror = (TextView) findViewById(R.id.tv_login_yzmerror);
        ll_login_fwxy = (LinearLayout) findViewById(R.id.ll_login_fwxy);
        layout_login_notice = (LinearLayout) findViewById(R.id.layout_login_notice);
        img_login_notice = (ImageView) findViewById(R.id.img_login_notice);
        textview_login_notice = (TextView) findViewById(R.id.textview_login_notice);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 强制收起键盘
        goneJp();
    }

    private void goneJp() {
        try {
            // 强制收起键盘
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AsyncHttpResponseHandler loginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                listCoupon.clear();
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jData = jobj.getJSONObject("data");
                    if (jData.has("user") && !jData.isNull("user")) {
                        JSONObject jUser = jData.getJSONObject("user");
                        if (jUser.has("cityId") && !jUser.isNull("cityId")) {
                            spUtil.saveInt("cityId",
                                    jUser.getInt("cityId"));
                        }
                        if (jUser.has("isNewComer") && !jUser.isNull("isNewComer")) {
                            isNewComer = jUser.getInt("isNewComer");
                        }
                        if (jUser.has("petKinds")&&!jUser.isNull("petKinds")){
                            JSONArray petKinds = jUser.getJSONArray("petKinds");
                            if (petKinds.length()>0){
                                if (petKinds.length()>=2){
                                    spUtil.saveInt("petkind",1);
                                }else {
                                    spUtil.saveInt("petkind",petKinds.getInt(0));
                                }
                            }else {
                                spUtil.saveInt("petkind",1);
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
                        if (jUser.has("registerPackage") && !jUser.isNull("registerPackage")) {
                            JSONObject objectReg = jUser.getJSONObject("registerPackage");
                            if (objectReg.has("dialogImg") && !objectReg.isNull("dialogImg")) {
                                dialogImg = objectReg.getString("dialogImg");
                            }
                            if (objectReg.has("title") && !objectReg.isNull("title")) {
                                title = objectReg.getString("title");
                            }
                            if (objectReg.has("coupons") && !objectReg.isNull("coupons")) {
                                JSONArray array = objectReg.getJSONArray("coupons");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        RegisterCoupon registerCoupon = new RegisterCoupon();
                                        JSONObject object = array.getJSONObject(i);
                                        if (object.has("amount") && !object.isNull("amount")) {
                                            registerCoupon.amount = object.getInt("amount");
                                        }
                                        if (object.has("name") && !object.isNull("name")) {
                                            registerCoupon.name = object.getString("name");
                                        }
                                        if (object.has("desc") && !object.isNull("desc")) {
                                            registerCoupon.desc = object.getString("desc");
                                        }
                                        listCoupon.add(registerCoupon);
                                    }
                                }
                            }
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
                            if (previous == Global.LOGIN_TO_POSTSELECTIONFRAGMENT) {
                                spUtil.saveString(
                                        "LOGIN_TO_POSTSELECTIONFRAGMENT_FLAG",
                                        "LOGIN_TO_POSTSELECTIONFRAGMENT");
                            }
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
                    if (isNewComer > 0) {//存在这个值表示是新用户注册，执行跳转
                        startActivity(new Intent(mContext, NewUserGiveCouponActivity.class));
                    }
                    spUtil.saveBoolean("isMainGetActivity", true);
                    spUtil.saveBoolean("isShopGetActivity", true);
                    if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.mainactivity");
                        intent.putExtra(
                                "previous",
                                Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT);
                        sendBroadcast(intent);
                    } else if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN) {
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                    } else if (previous == Global.PRE_PUSH_TO_EVALUATE) {
                        // 登录去评论
                        Intent intent = new Intent(LoginActivity.this,
                                EvaluateNewActivity.class);
                        intent.putExtra("orderid",
                                getIntent().getIntExtra("orderid", 0));
                        intent.putExtra("type", 2);
                        startActivity(intent);
                        finishWithAnimation();
                    } else if (previous == Global.PRE_PUSH_TO_EVALUATE_XIMEI) {
                        // 登录去评论
                        Intent intent = new Intent(LoginActivity.this,
                                EvaluateNewActivity.class);
                        intent.putExtra("orderid",
                                getIntent().getIntExtra("orderid", 0));
                        intent.putExtra("type", 1);
                        startActivity(intent);
                        finishWithAnimation();
                    } else if (previous == Global.FOSTERCARE_TO_LOGIN) {
                        finishWithAnimation();
                    } else if (previous == Global.PRE_RECHARGEPAGE_ZF) {
                        setResult(Global.RESULT_OK);
                    } else if (previous == Global.PRE_RECHARGEPAGE_WX) {
                        setResult(Global.RESULT_OK);
                    } else if (previous == Global.H5_TO_LOGIN) {
                        Intent data = new Intent();
                        data.putExtra("loginurl",
                                getIntent().getStringExtra("loginurl"));
                        setResult(Global.RESULT_OK, data);
                    } else if (previous == Global.MY_TO_LOGIN) {
                        setResult(Global.RESULT_OK);
                    } else if (previous == Global.AD_TO_LOGIN || previous == Global.SERVICE_NEW_TO_LOGIN) {
                        setResult(Global.RESULT_OK);
                    } else if (previous == Global.AD_TO_LOGIN_TO_ORDER) {// 登录去订单详情页
                        Intent intent = new Intent(context,
                                OrderDetailFromOrderToConfirmActivity.class);
                        intent.putExtra("previous", 0);
                        intent.putExtra("orderid", orderid);
                        intent.putExtra("serviceid", 0);
                        intent.putExtra("servicetype", 0);
                        startActivity(intent);
                    } else if (previous == Global.USERMEMBERFRAGMENT_LOGIN) {
                        setResult(Global.RESULT_OK);
                    } else if (previous == Global.COMMODITYDETAIL_LOGIN) {
                        setResult(Global.RESULT_OK);
                    } else if (previous == Global.RequestCode_UserMember) {
                        setResult(Global.RESULT_OK);
                    } else if (previous == Global.MIPCA_TO_ORDERPAY) {
                        startActivity(new Intent(mContext,
                                StoreSalesCashierActivity.class).putExtra("codeResult",
                                getIntent().getStringExtra("codeResult"))
                                .putExtra("previous", Global.MIPCA_TO_ORDERPAY));
                    } else if (previous == Global.SHOPLISTADDR_TO_LOGIN) {
                        if (ShopListActivity.act != null) {
                            ShopListActivity.act.finish();
                        }
                    }
                    setResult(Global.RESULT_OK);
                    finishWithAnimation();
                    act = null;
                } else {
                    rl_login_yzm.setAnimation(shakeAnimation(5));
                    tv_login_yzmerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_login_yzmerror.setText(msg);
                    }
                    Utils.goneJP(LoginActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(LoginActivity.this, "数据异常");
                Log.e("TAG", "e = " + e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(LoginActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler codeHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jsonObj = new JSONObject(new String(responseBody));
                code = jsonObj.getInt("code");
                String msg = jsonObj.getString("msg");
                if (code == 0) {
                    time.start();// 开始计时,并同时请求网络获取验证码
                    edt_verifkey.setFocusable(true);
                    edt_verifkey.setFocusableInTouchMode(true);
                    edt_verifkey.requestFocus();
                } else if (code == 11) {
                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("重新获取");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);
                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_login_imgyzmerror.setText(msg);
                    }
                    Utils.goneJP(LoginActivity.this);
                } else if (code == 12) {
                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("重新获取");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);
                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_login_imgyzmerror.setText(msg);
                    }
                    Utils.goneJP(LoginActivity.this);
                    //时间戳
                    long l = System.currentTimeMillis();
                    Log.e("TAG", "时间戳 = " + l);
                    //产生6位数随机数
                    int num = (int) ((Math.random() * 9 + 1) * 100000);
                    Log.e("TAG", "随机数 = " + num);
                    mobileKey = String.valueOf(l) + String.valueOf(num);
                    Log.e("TAG", "时间戳+随机数 = " + mobileKey);
                    pDialog.showDialog();
                    CommUtil.generateImageCode(LoginActivity.this, mobileKey,
                            generateImageCodeHandler);

                } else if (code == 13) {
                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("重新获取");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);
                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_login_imgyzmerror.setText(msg);
                    }
                    Utils.goneJP(LoginActivity.this);
                } else {
                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("重新获取");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);
                    // 获取验证码失败 获取服务器提示信息展示给用户
                    if (jsonObj.has("msg") && !jsonObj.isNull("msg")) {
                        ToastUtil.showToastShort(LoginActivity.this,
                                jsonObj.getString("msg"));
                    }
                }
            } catch (JSONException e) {
                bt_get.setBackgroundResource(R.drawable.btn_picked);
                bt_get.setText("重新获取");
                bt_get.setClickable(true);
                bt_get.setEnabled(true);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            bt_get.setBackgroundResource(R.drawable.btn_picked);
            bt_get.setText("重新获取");
            bt_get.setClickable(true);
            bt_get.setEnabled(true);
        }
    };
    private AsyncHttpResponseHandler codeYuYinHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jsonObj = new JSONObject(new String(responseBody));
                code = jsonObj.getInt("code");
                String msg = jsonObj.getString("msg");
                if (code == 0) {
                    timeYuYin.start();
                    edt_verifkey.setFocusable(true);
                    edt_verifkey.setFocusableInTouchMode(true);
                    edt_verifkey.requestFocus();
                } else if (code == 11) {
                    img_login_notice.setVisibility(View.GONE);
                    textview_login_notice.setText("收不到短信？请尝试语音验证码>");
                    textview_login_notice.setTextColor(Color.parseColor("#FF3A1E"));
                    textview_login_notice.setClickable(true);
                    textview_login_notice.setEnabled(true);

                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("获取验证码");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);

                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_login_imgyzmerror.setText(msg);
                    }
                    Utils.goneJP(LoginActivity.this);
                } else if (code == 12) {

                    img_login_notice.setVisibility(View.GONE);
                    textview_login_notice.setText("收不到短信？请尝试语音验证码>");
                    textview_login_notice.setTextColor(Color.parseColor("#FF3A1E"));
                    textview_login_notice.setClickable(true);
                    textview_login_notice.setEnabled(true);

                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("获取验证码");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);

                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_login_imgyzmerror.setText(msg);
                    }
                    Utils.goneJP(LoginActivity.this);
                    //时间戳
                    long l = System.currentTimeMillis();
                    Log.e("TAG", "时间戳 = " + l);
                    //产生6位数随机数
                    int num = (int) ((Math.random() * 9 + 1) * 100000);
                    Log.e("TAG", "随机数 = " + num);
                    mobileKey = String.valueOf(l) + String.valueOf(num);
                    Log.e("TAG", "时间戳+随机数 = " + mobileKey);
                    pDialog.showDialog();
                    CommUtil.generateImageCode(LoginActivity.this, mobileKey,
                            generateImageCodeHandler);


                } else if (code == 13) {

                    img_login_notice.setVisibility(View.GONE);
                    textview_login_notice.setText("收不到短信？请尝试语音验证码>");
                    textview_login_notice.setTextColor(Color.parseColor("#FF3A1E"));
                    textview_login_notice.setClickable(true);
                    textview_login_notice.setEnabled(true);

                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("获取验证码");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);

                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(msg)) {
                        tv_login_imgyzmerror.setText(msg);
                    }
                    Utils.goneJP(LoginActivity.this);

                } else {
                    img_login_notice.setVisibility(View.GONE);
                    textview_login_notice.setText("收不到短信？请尝试语音验证码>");
                    textview_login_notice.setTextColor(Color.parseColor("#FF3A1E"));
                    textview_login_notice.setClickable(true);
                    textview_login_notice.setEnabled(true);

                    bt_get.setBackgroundResource(R.drawable.btn_picked);
                    bt_get.setText("获取验证码");
                    bt_get.setClickable(true);
                    bt_get.setEnabled(true);

                    // 获取验证码失败 获取服务器提示信息展示给用户
                    if (jsonObj.has("msg") && !jsonObj.isNull("msg")) {
                        ToastUtil.showToastShort(LoginActivity.this,
                                jsonObj.getString("msg"));
                    }
                }
            } catch (JSONException e) {
                bt_get.setBackgroundResource(R.drawable.btn_picked);
                bt_get.setText("获取验证码");
                bt_get.setClickable(true);
                bt_get.setEnabled(true);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            img_login_notice.setVisibility(View.GONE);
            textview_login_notice.setText("收不到短信？请尝试语音验证码>");
            textview_login_notice.setTextColor(Color.parseColor("#FF3A1E"));
            textview_login_notice.setClickable(true);
            textview_login_notice.setEnabled(true);

            bt_get.setBackgroundResource(R.drawable.btn_picked);
            bt_get.setText("获取验证码");
            bt_get.setClickable(true);
            bt_get.setEnabled(true);
        }
    };

    // 返回上层
    private void backToFront() {
        finishWithAnimation();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {// 这个方法是系统方法
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                break;
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
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // 注册计时器
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            bt_get.setBackgroundResource(R.drawable.btn_picked);
            bt_get.setText("重新获取");
            bt_get.setClickable(true);
            bt_get.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            bt_get.setClickable(false);
            bt_get.setText(millisUntilFinished / 1000 + "秒");
            bt_get.setEnabled(false);
            bt_get.setBackgroundResource(R.drawable.bg_noround_bbbbbb);
        }
    }

    // 注册计时器
    class TimeYuYin extends CountDownTimer {
        public TimeYuYin(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            img_login_notice.setVisibility(View.GONE);
            textview_login_notice.setText("收不到短信？请尝试语音验证码>");
            textview_login_notice.setTextColor(Color.parseColor("#FF3A1E"));
            textview_login_notice.setClickable(true);
            textview_login_notice.setEnabled(true);

            bt_get.setBackgroundResource(R.drawable.btn_picked);
            bt_get.setText("获取验证码");
            bt_get.setClickable(true);
            bt_get.setEnabled(true);
            textview_login_notice.setClickable(true);
            textview_login_notice.setEnabled(true);
            layout_login_notice.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            img_login_notice.setVisibility(View.VISIBLE);
            textview_login_notice.setClickable(false);
            textview_login_notice.setText("1分钟内将收到固话来电，请注意接听");
            textview_login_notice.setTextColor(Color.parseColor("#666666"));
            textview_login_notice.setEnabled(false);

            bt_get.setClickable(false);
            bt_get.setText(millisUntilFinished / 1000 + "秒");
            bt_get.setEnabled(false);
            bt_get.setBackgroundResource(R.drawable.bg_noround_bbbbbb);
            textview_login_notice.setClickable(false);
            textview_login_notice.setEnabled(false);
            layout_login_notice.setBackgroundColor(Color.parseColor("#F8F1E8"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.mainactivity");
                intent.putExtra("previous",
                        Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                sendBroadcast(intent);
                finishWithAnimation();
            } else if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN) {
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finishWithAnimation();
            } else {
                finishWithAnimation();
            }
        }
        act = null;
        return super.onKeyDown(keyCode, event);
    }

    private void initBD() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(100);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();// 纬度
            lng = location.getLongitude();// 经度
            mLocationClient.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LoginActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要
        MobclickAgent.onResume(this); // 统计时长
        getPictureOnUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
        MobclickAgent.onPause(this);
    }

    private void isOpenVoiceVerification(){
        CommUtil.isOpenVoiceVerification(mContext,voiceHandle);
    }

    private AsyncHttpResponseHandler voiceHandle = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("jsonObject");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("isOpen") && !object.isNull("isOpen")) {
                            isOpen = object.getInt("isOpen");
                            if (isOpen == 0) {
                                layout_login_notice.setVisibility(View.GONE);
                            } else if (isOpen == 1) {
                                layout_login_notice.setVisibility(View.VISIBLE);
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

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sriv_login_imgver:
                et_login_imgver.setText("");
                //时间戳
                long l = System.currentTimeMillis();
                Log.e("TAG", "时间戳 = " + l);
                //产生6位数随机数
                int num = (int) ((Math.random() * 9 + 1) * 100000);
                Log.e("TAG", "随机数 = " + num);
                mobileKey = String.valueOf(l) + String.valueOf(num);
                Log.e("TAG", "时间戳+随机数 = " + mobileKey);
                pDialog.showDialog();
                CommUtil.generateImageCode(LoginActivity.this, mobileKey,
                        generateImageCodeHandler);
                break;
            case R.id.ll_login_agree:
                if (isSelect) {
                    cbAgree.setBackgroundResource(R.drawable.complaint_reason_disable);
                } else {
                    cbAgree.setBackgroundResource(R.drawable.complaint_reason);
                }
                isSelect = !isSelect;
                break;
            case R.id.ib_titlebar_back:
                // 强制收起键盘
                goneJp();
                if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.mainactivity");
                    intent.putExtra("previous",
                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                    sendBroadcast(intent);
                    finishWithAnimation();
                } else if (previous == Global.PRE_PUSH_TO_NOSTARTAPP_LOGIN) {
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finishWithAnimation();
                } else {
                    backToFront();
                }
                act = null;
                break;
            case R.id.button_login_sure:// 登录
                getIMEI();
                if (!Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", ""))) {
                    ll_login_phone.setAnimation(shakeAnimation(5));
                    tv_login_phoneerror.setVisibility(View.VISIBLE);
                    tv_login_phoneerror.setText("请输入手机号");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (!Utils.checkPhone1(LoginActivity.this, et_userid_num)) {
                    ll_login_phone.setAnimation(shakeAnimation(5));
                    tv_login_phoneerror.setVisibility(View.VISIBLE);
                    tv_login_phoneerror.setText("请输入正确的手机号");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (pictureOn.equals("1") && !Utils.isStrNull(et_login_imgver.getText().toString().trim())) {
                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    tv_login_imgyzmerror.setText("请输入数字图形验证码");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (!Utils.isStrNull(edt_verifkey.getText().toString().trim())) {
                    rl_login_yzm.setAnimation(shakeAnimation(5));
                    tv_login_yzmerror.setVisibility(View.VISIBLE);
                    tv_login_yzmerror.setText("请输入验证码");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (!isSelect) {
                    ll_login_fwxy.setAnimation(shakeAnimation(5));
                    ToastUtil.showToastShortBottom(LoginActivity.this, "请同意并勾选用户协议");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                pDialog.showDialog();
                spUtil.removeData("isFirstLogin");
                isNewComer = 0;
                CommUtil.loginAu(context, et_userid_num.getText().toString().trim().replace(" ", ""),
                        Global.getIMEI(this),
                        edt_verifkey.getText().toString(), lat, lng,"","","","",
                        loginHandler);
                break;
            case R.id.bt_get:// 获取验证码
                /*if (CommUtil.getServiceBaseUrl().equals("http://192.168.0.252/")
                        || CommUtil.getServiceBaseUrl().equals("https://test.chongwuhome.cn/")
                        || CommUtil.getServiceBaseUrl().equals("http://demo.cwjia.cn/")) {
                    if (et_userid_num.getText().toString().trim().replace(" ", "").equals("18510255744") || et_userid_num.getText().toString().trim().replace(" ", "").equals("15717155675")) {
                        ToastUtil.showToastShortCenter(mContext, "当前账号无法获取验证码");
                        return;
                    }
                }*/
                if (!Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", ""))) {
                    ll_login_phone.setAnimation(shakeAnimation(5));
                    tv_login_phoneerror.setVisibility(View.VISIBLE);
                    tv_login_phoneerror.setText("请输入手机号");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (!Utils.checkPhone1(LoginActivity.this, et_userid_num)) {
                    ll_login_phone.setAnimation(shakeAnimation(5));
                    tv_login_phoneerror.setVisibility(View.VISIBLE);
                    tv_login_phoneerror.setText("请输入正确的手机号");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (pictureOn.equals("1") && !Utils.isStrNull(et_login_imgver.getText().toString().trim())) {
                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    tv_login_imgyzmerror.setText("请输入数字图形验证码");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                isYuYin = false;
                edt_verifkey.setText("");
                bt_get.setEnabled(false);
                bt_get.setBackgroundResource(R.drawable.bg_noround_bbbbbb);
                slat_md5 = MD5.md5(Global.MD5_STR, et_userid_num.getText().toString().trim().replace(" ", ""));
                Log.e("TAG", "slat_md5 = " + slat_md5);
                pDialog.showDialog();
                CommUtil.genVerifyCode(this, et_userid_num.getText().toString().trim().replace(" ", ""),  slat_md5,
                        0, codeHandler);
                break;
            case R.id.tv_oo:// 协议
                String url = CommUtil.getStaticUrl() + "static/content/protocol.html?system=" +
                        CommUtil.getSource() + "_" + Global.getCurrentVersion(this) + "&imei=" +
                        Global.getIMEI(this) + "&cellPhone=" +
                        SharedPreferenceUtil.getInstance(this).getString("cellphone", "") + "&time=" + time;
                Intent intent = new Intent(LoginActivity.this,
                        ADActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
//            case R.id.layout_login_notice:
            case R.id.textview_login_notice:
                if (!Utils.isStrNull(et_userid_num.getText().toString().trim().replace(" ", ""))) {
                    ll_login_phone.setAnimation(shakeAnimation(5));
                    tv_login_phoneerror.setVisibility(View.VISIBLE);
                    tv_login_phoneerror.setText("请输入手机号");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (!Utils.checkPhone1(LoginActivity.this, et_userid_num)) {
                    ll_login_phone.setAnimation(shakeAnimation(5));
                    tv_login_phoneerror.setVisibility(View.VISIBLE);
                    tv_login_phoneerror.setText("请输入正确的手机号");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                if (pictureOn.equals("1") && !Utils.isStrNull(et_login_imgver.getText().toString().trim())) {
                    rl_login_imgver.setAnimation(shakeAnimation(5));
                    tv_login_imgyzmerror.setVisibility(View.VISIBLE);
                    tv_login_imgyzmerror.setText("请输入数字图形验证码");
                    Utils.goneJP(LoginActivity.this);
                    return;
                }
                isYuYin = true;
                edt_verifkey.setText("");
                slat_md5 = MD5.md5(Global.MD5_STR, et_userid_num.getText().toString().trim().replace(" ", ""));
                CommUtil.genVerifyCode(this, et_userid_num.getText().toString().trim().replace(" ", ""), slat_md5, 1, codeYuYinHandler);
                break;
            default:
                break;
        }
    }
}
