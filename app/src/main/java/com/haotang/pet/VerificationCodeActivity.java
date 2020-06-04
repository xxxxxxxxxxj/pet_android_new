package com.haotang.pet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.RegisterCoupon;
import com.haotang.pet.entity.UpdateUserPhoneEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MD5;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.PhoneCode;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationCodeActivity extends SuperActivity {

    @BindView(R.id.iv_verification_back)
    ImageView ivVerificationBack;
    @BindView(R.id.tv_vericication_phone)
    TextView tvVericicationPhone;
    @BindView(R.id.pc_verification_code)
    PhoneCode pcVerificationCode;
    @BindView(R.id.tv_verification_voice)
    TextView tvVerificationVoice;
    @BindView(R.id.tv_verification_time)
    TextView tvVerificationTime;
    private ArrayList<RegisterCoupon> listCoupon = new ArrayList<>();
    public static Activity act;
    private String phone;
    private String slat_md5;
    private int isNewComer;
    private int previous;
    private String dialogImg;
    private double lat;
    private double lng;
    private String title;
    private int mypetId;
    private int orderid;
    private String backup;
    private int point;
    private String img_url;
    private String jump_url;
    private int flag;
    private Intent intent;
    private String openId;
    private String wxNickname;
    private String wxAvatar;
    private boolean isGoSetFinger;
    private int isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        setTime();
        setListener();
        isOpenVoiceVerification();
        ToastUtil.showImageToast(this,"验证码已发送",R.drawable.toast_choose);
    }


    private void setTime() {
        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                tvVerificationTime.setText(millisUntilFinished / 1000 + "s");
                tvVerificationTime.setClickable(false);
            }

            @Override
            public void onFinish() {
                tvVerificationTime.setText("重新获取");
                tvVerificationTime.setClickable(true);
            }
        }.start();
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
                                tvVerificationVoice.setVisibility(View.GONE);
                            } else if (isOpen == 1) {
                                tvVerificationVoice.setVisibility(View.VISIBLE);
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

    private AsyncHttpResponseHandler loginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                listCoupon.clear();
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jData = jobj.getJSONObject("data");
                    if (jData.has("user") && !jData.isNull("user")) {
                        JSONObject jUser = jData.getJSONObject("user");
                        Utils.mLogError("微信绑定信息："+jUser.isNull("openId")+jUser.getString("openId")+" "+jUser.getString("wxNickName"));
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
                        if (jUser.has("isNewComer") && !jUser.isNull("isNewComer")) {
                            isNewComer = jUser.getInt("isNewComer");
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
                        Intent intent = new Intent(VerificationCodeActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                    } else if (previous == Global.PRE_PUSH_TO_EVALUATE) {
                        // 登录去评论
                        Intent intent = new Intent(VerificationCodeActivity.this,
                                EvaluateNewActivity.class);
                        intent.putExtra("orderid",
                                getIntent().getIntExtra("orderid", 0));
                        intent.putExtra("type", 2);
                        startActivity(intent);
                        finishWithAnimation();
                    } else if (previous == Global.PRE_PUSH_TO_EVALUATE_XIMEI) {
                        // 登录去评论
                        Intent intent = new Intent(VerificationCodeActivity.this,
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
                        Intent intent = new Intent(mContext,
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
                    }else if (previous == Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY){
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.mainactivity");
                        intent.putExtra("previous",
                                Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                        sendBroadcast(intent);
                        if (MApplication.listAppoint1.size() > 0) {
                            for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                                MApplication.listAppoint1.get(i).finish();
                            }
                        }
                        MApplication.listAppoint1.clear();
                        finish();
                    }else {
                        goNext(MainActivity.class);
                    }
                    setResult(Global.RESULT_OK);
                    if (MApplication.listAppoint1.size() > 0) {
                        for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                            MApplication.listAppoint1.get(i).finish();
                        }
                    }
                    MApplication.listAppoint1.clear();
                    finishWithAnimation();
                    act = null;
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(mContext, msg);
                    }
                    Utils.goneJP(VerificationCodeActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(VerificationCodeActivity.this, "数据异常");
                Log.e("TAG", "e = " + e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(VerificationCodeActivity.this, "请求失败");
        }
    };
    //检查验证码
    private AsyncHttpResponseHandler checkCodeHanlder = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
               if (jobj.getInt("code")==0){
                   //重新设置手机号
                   if (previous == Global.SETREPLACEPHONE_TO_VERIFCODE){
                       Intent intent = new Intent(mContext,ReplacePhoneActivity.class);
                       startActivity(intent);
                   }else {
                       Intent intent = new Intent(mContext,SetUpPayPwdActivity.class);
                       intent.putExtra("flag",flag);
                       intent.putExtra("isGoSetFinger",isGoSetFinger);
                       startActivity(intent);
                   }
                   if (MApplication.listAppoint1.size() > 0) {
                       for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                           MApplication.listAppoint1.get(i).finish();
                       }
                   }
                   MApplication.listAppoint1.clear();
                   finish();
               }else {
                   //验证码不正确
//                   ToastUtil.showToastShortBottom(mContext,jobj.getString("msg"));
                   ToastUtil.showImageToast(mContext,"验证码不正确",R.drawable.icon_warn_gray);
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext,"请求失败");
        }
    };

    private void setListener() {
        pcVerificationCode.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                //检查验证码
                if (previous == Global.PAYPWD_TO_VERIFCODE){
                    mPDialog.showDialog();
                    CommUtil.checkVerifyCode(mContext,phone,code,checkCodeHanlder);
                }else  if (previous == Global.SETREPLACEPHONE_TO_VERIFCODE){
                    mPDialog.showDialog();
                    CommUtil.checkVerifyCode(mContext,phone,code,checkCodeHanlder);
                }
                else if (previous==Global.BINDPHONE_TOLOGIN){ CommUtil.loginAu(mContext, phone,
                            Global.getIMEI(mContext),
                            code, lat, lng,openId,"quick_login_app",wxAvatar,wxNickname,
                            loginHandler);
                }else if (previous == Global.REPLACEPHONE_TO_VERIFCODE) {
                    mPDialog.showDialog();
                    CommUtil.updateUserPhone(mContext, phone, code, updateUserHanlder);
                }else {
                    CommUtil.loginAu(mContext, phone,
                            Global.getIMEI(mContext),
                            code, lat, lng,"","","","",
                            loginHandler);
                }
            }

            @Override
            public void onInput() {

            }
        });
    }

    private AsyncHttpResponseHandler updateUserHanlder = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj.getInt("code") == 0) {
                    ToastUtil.showToastShortCenter(mContext,"修改成功");
                    spUtil.saveString("cellphone", phone);
                    spUtil.setPhoneSave(phone);
                    EventBus.getDefault().post(new UpdateUserPhoneEvent(true));
                    finish();
                } else {
                    ToastUtil.showToastShortBottom(mContext, jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void initData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        orderid = intent.getIntExtra("orderid", -1);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        img_url = intent.getStringExtra("img_url");
        jump_url = intent.getStringExtra("jump_url");
        backup = intent.getStringExtra("backup");
        point = intent.getIntExtra("point", -1);
        previous = intent.getIntExtra("previous",0);
        flag = intent.getIntExtra("flag", -1);
        isGoSetFinger = intent.getBooleanExtra("isGoSetFinger", false);
        openId = intent.getStringExtra("openId");
        wxNickname = intent.getStringExtra("wxNickname");
        wxAvatar = intent.getStringExtra("wxAvatar");
        act = this;
        MApplication.listAppoint.add(act);
    }

    private void setView() {
        setContentView(R.layout.activity_verification_code);
        ButterKnife.bind(this);
        tvVericicationPhone.setText(phone);
    }

    private AsyncHttpResponseHandler codeHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private AsyncHttpResponseHandler codeYuYinHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObj = new JSONObject(new String(responseBody));
                int code = jsonObj.getInt("code");
                String msg = jsonObj.getString("msg");
                if (code==0){
                    ToastUtil.showToastShortBottom(mContext,"请注意查收电话");
                }else {
                    ToastUtil.showToastShortBottom(mContext,msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext,"请求失败");
        }
    };

    @OnClick({R.id.iv_verification_back, R.id.tv_verification_voice, R.id.tv_verification_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_verification_back:
                finish();
                break;
            case R.id.tv_verification_voice:
                slat_md5 = MD5.md5(Global.MD5_STR, phone);
                CommUtil.genVerifyCode(this, phone, slat_md5, 1, codeYuYinHandler);
                break;
            case R.id.tv_verification_time:
                slat_md5 = MD5.md5(Global.MD5_STR, phone);
                CommUtil.genVerifyCode(this, phone,  slat_md5,
                        0, codeHandler);
                setTime();
                ToastUtil.showImageToast(this,"验证码已发送",R.drawable.toast_choose);
                break;
        }
    }
}
