package com.haotang.pet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.encyclopedias.activity.MyCollectActivity;
import com.haotang.pet.entity.ExtraMenusCodeBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.updateapputil.Callback;
import com.haotang.pet.updateapputil.ConfirmDialog;
import com.haotang.pet.updateapputil.DownloadAppUtils;
import com.haotang.pet.updateapputil.DownloadProgressDialog;
import com.haotang.pet.updateapputil.MyNotification;
import com.haotang.pet.updateapputil.UpdateAppEvent;
import com.haotang.pet.updateapputil.UpdateUtil;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DataCleanUtils;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class MoreAboutActivity extends SuperActivity {
    private TextView tv_moreabout_jyzb;
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    private ImageView imageView_noty_img;
    private RelativeLayout rl_moreabout_jyzb;
    private TextView textView_more_update, textView_more_about_other;
    private RelativeLayout layout_update_check, layout_about;
    private boolean accept = true;
    private String liveUrl;
    private LinearLayout layout_login_out;
    private String liveTag;
    private LayoutInflater mInflater;
    private SharedPreferenceUtil spUtil;
    private Short pushNotify = 1;
    private String servicePhone;
    private RelativeLayout rl_moreabout_cleandata;
    private TextView tv_moreabout_cleandata;

    private LinearLayout address;
    private TextView textview_left_address;
    private ImageView img_right_address;

    private LinearLayout yqyl;
    private TextView textview_left_yqyl;
    private ImageView img_right_yqyl;

    private LinearLayout dogcard;
    private TextView textview_left_dogcard;
    private ImageView img_right_dogcard;

    private LinearLayout myeva;
    private TextView textview_left_myeva;
    private ImageView img_right_myeva;

    private LinearLayout foster_live;
    private TextView textview_left_foster_live;
    private ImageView img_right_foster_live;

    private LinearLayout feedBack;
    private TextView textview_left_feed_back;
    private ImageView img_right_feed_back;

    private LinearLayout clearCache;
    private TextView textview_left_clearCache;
    private String petCardUrl;
    private String inviteUrl;
    public static MoreAboutActivity activity;
    private MyNotification mNotification;
    private LinearLayout zfsz;
    private TextView textview_left_zfsz;
    private int payPwd;
    private String payHelp;
    private LinearLayout my_collect;
    private TextView textview_left_my_collect;
    private DownloadProgressDialog progressDialog;
    private String latestVersion;
    private String downloadPath;
    private String versionHint;
    private int isUpgrade;
    public final static int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_more_about);
        activity = this;
        spUtil = SharedPreferenceUtil.getInstance(this);
        Intent intent = getIntent();
        servicePhone = intent.getStringExtra("servicePhone");
        initView();
        initListener();
        accept = spUtil.getBoolean("accept", true);
        if (accept) {
            // pushNotify = 1;
            imageView_noty_img.setBackgroundResource(R.drawable.noty_yes);
        } else {
            // pushNotify = 0;
            imageView_noty_img.setBackgroundResource(R.drawable.noty_no);
        }
        CommUtil.loadExtraMenus(this, spUtil.getString("cellphone", ""), loadExtraMenusHanler);
        CommUtil.getH5Url(mContext, h5Handler);
        getMoney();
    }

    private AsyncHttpResponseHandler loadExtraMenusHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            String result = new String(responseBody);
            processData(result);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    // 解析json数据
    private void processData(String result) {
        try {
            Gson gson = new Gson();
            ExtraMenusCodeBean extraMenusCodeBean = gson.fromJson(result,
                    ExtraMenusCodeBean.class);
            if (extraMenusCodeBean != null) {
                int code = extraMenusCodeBean.getCode();
                List<ExtraMenusCodeBean.DataBean> data = extraMenusCodeBean.getData();
                String msg = extraMenusCodeBean.getMsg();
                if (code == 0) {
                    if (data != null && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            ExtraMenusCodeBean.DataBean dataBean = data.get(i);
                            if (dataBean.getText().equals("我的寄养直播")) {
                                liveTag = dataBean.getTag();
                                if (liveTag != null
                                        && !TextUtils.isEmpty(liveTag)) {
                                    tv_moreabout_jyzb.setVisibility(View.VISIBLE);
                                    tv_moreabout_jyzb.setText(liveTag.trim());
                                } else {
                                    tv_moreabout_jyzb.setVisibility(View.GONE);
                                }
                                liveUrl = dataBean.getUrl();
                            }
                            if (dataBean.getText().equals("办理狗证")) {
                                petCardUrl = dataBean.getUrl();
                            }
                        }
                    }
                } else {
                    if (msg != null && !TextUtils.isEmpty(msg)) {
                        ToastUtil.showToastShortCenter(MoreAboutActivity.this, msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initListener() {
        if (spUtil.getString("cellphone", "").equals("")
                || spUtil.getInt("userid", 0) <= 0) {
            layout_login_out.setVisibility(View.GONE);
        }
        // 检测是否接受通知
        imageView_noty_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (accept) {
                    accept = false;
                    spUtil.saveBoolean("accept", accept);
                    imageView_noty_img
                            .setBackgroundResource(R.drawable.noty_no);
                    pushNotify = 0;
                    updateUserPushNotify(pushNotify);
                } else {
                    accept = true;
                    spUtil.saveBoolean("accept", accept);
                    imageView_noty_img
                            .setBackgroundResource(R.drawable.noty_yes);
                    pushNotify = 1;
                    updateUserPushNotify(pushNotify);
                }
            }
        });
        ib_titlebar_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        layout_update_check.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 检测新版本升级
                getLatestVersion();
            }
        });
        layout_about.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 关于
                Intent intent = new Intent(MoreAboutActivity.this,
                        AboutUsMessageActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(),
                        Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("servicePhone", servicePhone);
                getIntent().putExtra(Global.ANIM_DIRECTION(),
                        Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });
        rl_moreabout_jyzb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                UmengStatistics.UmengEventStatistics(MoreAboutActivity.this,
                        Global.UmengEventID.click_My_FosterLive);
                startActivity(new Intent(MoreAboutActivity.this, ADActivity.class).putExtra(
                        "url", liveUrl));
            }
        });
        // 退出
        layout_login_out.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showSelectDialog();
            }
        });
        rl_moreabout_cleandata.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                boolean b = DataCleanUtils.clearAllCache(MoreAboutActivity.this);
                if (b) {
                    ToastUtil.showToastShortBottom(MoreAboutActivity.this, "缓存清除成功");
                    try {
                        rl_moreabout_cleandata.setVisibility(View.VISIBLE);
                        tv_moreabout_cleandata.setText(DataCleanUtils.getTotalCacheSize(MoreAboutActivity.this));
                    } catch (Exception e) {
                        rl_moreabout_cleandata.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getMoney() {
        CommUtil.getAccountBalance(this, SharedPreferenceUtil.getInstance(this).getString("cellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), getMoney);
    }

    private AsyncHttpResponseHandler getMoney = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("user") && !object.isNull("user")) {
                            JSONObject juser = object.getJSONObject("user");
                            if (juser.has("payPwd") && !juser.isNull("payPwd")) {
                                payPwd = juser.getInt("payPwd");
                            }
                            if (juser.has("payHelp") && !juser.isNull("payHelp")) {
                                payHelp = juser.getString("payHelp");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(MoreAboutActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(MoreAboutActivity.this, "请求失败");
        }
    };

    private void initView() {
        mInflater = LayoutInflater.from(this);
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        textView_more_update = (TextView) findViewById(R.id.textView_more_update);
        textView_more_about_other = (TextView) findViewById(R.id.textView_more_about_other);
        imageView_noty_img = (ImageView) findViewById(R.id.imageView_noty_img);
        layout_update_check = (RelativeLayout) findViewById(R.id.layout_update_check);
        layout_about = (RelativeLayout) findViewById(R.id.layout_about);
        layout_login_out = (LinearLayout) findViewById(R.id.layout_login_out);
        rl_moreabout_jyzb = (RelativeLayout) findViewById(R.id.rl_moreabout_jyzb);
        tv_titlebar_title.setText("设置");
        tv_moreabout_jyzb = (TextView) findViewById(R.id.tv_moreabout_jyzb);
        rl_moreabout_cleandata = (RelativeLayout) findViewById(R.id.rl_moreabout_cleandata);
        tv_moreabout_cleandata = (TextView) findViewById(R.id.tv_moreabout_cleandata);
        try {
            rl_moreabout_cleandata.setVisibility(View.VISIBLE);
            tv_moreabout_cleandata.setText(DataCleanUtils.getTotalCacheSize(this));
        } catch (Exception e) {
            rl_moreabout_cleandata.setVisibility(View.GONE);
            e.printStackTrace();
        }

        zfsz = (LinearLayout) findViewById(R.id.zfsz);
        textview_left_zfsz = (TextView) zfsz.findViewById(R.id.textview_left);
        textview_left_zfsz.setText("支付设置");
        zfsz.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkLogin(mContext)) {
                    Intent intent = new Intent(MoreAboutActivity.this, PaySettingActivity.class);
                    intent.putExtra("payPwd", payPwd);
                    intent.putExtra("payHelp", payHelp);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
            }
        });


        address = (LinearLayout) findViewById(R.id.address);
        textview_left_address = (TextView) address.findViewById(R.id.textview_left);
        img_right_address = (ImageView) address.findViewById(R.id.img_right);
        textview_left_address.setText("常用地址");
        textview_left_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_CommonAddress);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, CommonAddressActivity.class).putExtra("index", 1));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
            }
        });
        img_right_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_CommonAddress);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, CommonAddressActivity.class).putExtra("index", 1));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
            }
        });

        yqyl = (LinearLayout) findViewById(R.id.yqyl);
        textview_left_yqyl = (TextView) yqyl.findViewById(R.id.textview_left);
        img_right_yqyl = (ImageView) yqyl.findViewById(R.id.img_right);
        textview_left_yqyl.setText("邀请有礼");
        textview_left_yqyl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_InvitationCourtesy);
                getStatistics(Global.ServerEventID.choose_myself_page, Global.ServerEventID.click_myinvite_self);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, ADActivity.class).putExtra("url", inviteUrl).putExtra("previous", Global.MYFRAGMENT_INVITESHARE));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }

            }
        });
        img_right_yqyl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_InvitationCourtesy);
                getStatistics(Global.ServerEventID.choose_myself_page, Global.ServerEventID.click_myinvite_self);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, ADActivity.class).putExtra("url", inviteUrl).putExtra("previous", Global.MYFRAGMENT_INVITESHARE));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }

            }
        });
        dogcard = (LinearLayout) findViewById(R.id.dogcard);
        textview_left_dogcard = (TextView) dogcard.findViewById(R.id.textview_left);
        img_right_dogcard = (ImageView) dogcard.findViewById(R.id.img_right);
        textview_left_dogcard.setText("办理犬证");
        textview_left_dogcard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_DogCard);
                if (!TextUtils.isEmpty(petCardUrl)) {
                    getStatistics(Global.ServerEventID.choose_myself_page, Global.ServerEventID.click_mydogcard_self);
                    if (Utils.checkLogin(mContext)) {
                        startActivity(new Intent(mContext, ADActivity.class).putExtra("url", petCardUrl));
                    } else {
                        startActivity(new Intent(mContext, LoginNewActivity.class));
                    }
                }
            }
        });
        img_right_dogcard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_DogCard);
                if (!TextUtils.isEmpty(petCardUrl)) {
                    getStatistics(Global.ServerEventID.choose_myself_page, Global.ServerEventID.click_mydogcard_self);
                    if (Utils.checkLogin(mContext)) {
                        startActivity(new Intent(mContext, ADActivity.class).putExtra("url", petCardUrl));
                    } else {
                        startActivity(new Intent(mContext, LoginNewActivity.class));
                    }
                }
            }
        });

        myeva = (LinearLayout) findViewById(R.id.myeva);
        textview_left_myeva = (TextView) myeva.findViewById(R.id.textview_left);
        img_right_myeva = (ImageView) myeva.findViewById(R.id.img_right);
        textview_left_myeva.setText("我的评价");
        textview_left_myeva.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_MyEvaluation);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyEvaluateActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
            }
        });
        img_right_myeva.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_My_MyEvaluation);
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyEvaluateActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
            }
        });
        foster_live = (LinearLayout) findViewById(R.id.foster_live);
        textview_left_foster_live = (TextView) foster_live.findViewById(R.id.textview_left);
        img_right_foster_live = (ImageView) foster_live.findViewById(R.id.img_right);
        textview_left_foster_live.setText("寄养直播");
        textview_left_foster_live.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(MoreAboutActivity.this, Global.UmengEventID.click_My_FosterLive);
                if (!TextUtils.isEmpty(liveUrl)) {
                    if (Utils.checkLogin(mContext)) {
                        startActivity(new Intent(MoreAboutActivity.this, ADActivity.class).putExtra("url", liveUrl));
                    } else {
                        startActivity(new Intent(mContext, LoginNewActivity.class));
                    }
                }
            }
        });

        my_collect = (LinearLayout) findViewById(R.id.my_collect);
        textview_left_my_collect = (TextView) my_collect.findViewById(R.id.textview_left);
        textview_left_my_collect.setText("我的收藏");
        my_collect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkLogin(mContext)) {
                    startActivity(new Intent(mContext, MyCollectActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
            }
        });

        feedBack = (LinearLayout) findViewById(R.id.feed_back);
        textview_left_feed_back = (TextView) feedBack.findViewById(R.id.textview_left);
        img_right_feed_back = (ImageView) feedBack.findViewById(R.id.img_right);
        textview_left_feed_back.setText("投诉");
        textview_left_feed_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FeedBackActivity.class));
            }
        });

        img_right_foster_live.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengStatistics.UmengEventStatistics(MoreAboutActivity.this, Global.UmengEventID.click_My_FosterLive);
                if (!TextUtils.isEmpty(liveUrl)) {
                    if (Utils.checkLogin(mContext)) {
                        startActivity(new Intent(MoreAboutActivity.this, ADActivity.class).putExtra("url", liveUrl));
                    } else {
                        startActivity(new Intent(mContext, LoginNewActivity.class));
                    }
                }
            }
        });
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
        mPDialog.showDialog();
        CommUtil.getLatestVersion(this, Global.getCurrentVersion(this),
                System.currentTimeMillis(), latestHanler);
    }

    private AsyncHttpResponseHandler latestHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
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
                                } else {
                                    ToastUtil.showToastShortBottom(mContext, "当前已是最新版本");
                                }
                            } else {
                                ToastUtil.showToastShortBottom(mContext, "当前已是最新版本");
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
            mPDialog.closeDialog();
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

    private void showSelectDialog() {
        MDialog mDialog = new MDialog.Builder(MoreAboutActivity.this)
                .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                .setMessage("确定退出?").setCancelStr("否").setOKStr("是")
                .positiveListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 退出登录
                        removeDataSp();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.mainactivity");
                        intent.putExtra("previous",
                                Global.PRE_LOGINOUT_TO_BACK_MAINACTIVITY);
                        sendBroadcast(intent);
                        finishWithAnimation();
                    }
                }).build();
        mDialog.show();
    }

    public void onDismiss() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    private void removeDataSp() {
        spUtil.removeData("upShopName");
        spUtil.removeData("upRegionId");
        spUtil.removeData("isCerti");
        spUtil.removeData("city");
        spUtil.removeData("cityId");
        spUtil.removeData("cellphone");
        spUtil.removeData("userid");
        spUtil.removeData("username");
        spUtil.removeData("userimage");
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
        spUtil.removeData("shopid");
        spUtil.removeData("newshopid");
        spUtil.removeData("newaddr");
        spUtil.removeData("newlat");
        spUtil.removeData("newlng");
        spUtil.removeData("invitecode");
    }

    private void updateUserPushNotify(Short pushNotify) {
        CommUtil.updateUserPushNotify(Global.getIMEI(this), this, pushNotify,
                getuiHandler);
    }

    private AsyncHttpResponseHandler getuiHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        boolean acceptBoolean = jobj.getBoolean("data");
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub

        }

    };

    private AsyncHttpResponseHandler h5Handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("获取h5url：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata.has("invitationConfig") && !jdata.isNull("invitationConfig")) {
                        JSONObject ji = jdata.getJSONObject("invitationConfig");
                        if (ji.has("url") && !ji.isNull("url")) {
                            inviteUrl = ji.getString("url");
                            Utils.mLogError("==-->inviteUrl " + inviteUrl);
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

    private void getStatistics(String typeid, String activeid) {
        CommUtil.logcountAdd(mContext, typeid, activeid, statisticsHandler);
    }

    private AsyncHttpResponseHandler statisticsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
