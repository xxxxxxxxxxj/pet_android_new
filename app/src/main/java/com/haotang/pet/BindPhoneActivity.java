package com.haotang.pet;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.LoginBgVedioInfo;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.net.BinaryHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.GsonUtil;
import com.haotang.pet.util.MD5;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneActivity extends SuperActivity {

    @BindView(R.id.iv_bindphone_back)
    ImageView ivBindphoneBack;
    @BindView(R.id.iv_bindphone_wxhead)
    ImageView ivBindphoneWxhead;
    @BindView(R.id.sv_bg)
    SurfaceView svBg;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.iv_login_clear)
    ImageView ivLoginClear;
    @BindView(R.id.rl_login_getcode)
    RelativeLayout rlLoginGetcode;
    @BindView(R.id.tv_login_agreement)
    TextView tvLoginAgreement;
    @BindView(R.id.tv_bindphone_wxname)
    TextView tvBindphoneWxname;
    @BindView(R.id.ll_login_bottom)
    LinearLayout llLoginBottom;
    @BindView(R.id.tv_login_getcode)
    TextView tvGetCode;
    private static BindPhoneActivity act;
    private String openId;
    private String wxNickname;
    private String wxAvatar;
    private double lat = 0;
    private double lng = 0;
    private String slat_md5;
    private String backup;
    private int point;
    private String img_url;
    private String jump_url;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mSecondMediaplayer;
    private SurfaceHolder mSurfaceHolder;
    private AssetManager assetManager;
    private final String LOGIN_BG_VIDEO_FILE_NAME = "login_bg_video.mp4";
    private final String SP_TAG_LOGIN_BG_VIDEO_VERSION = "sp_tag_login_bg_video_version";
    private int mNewLoginBgVideoVersion; // 新登录背景视频版本号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        initData();
        setListener();
        initMediaPlayer();
    }

    private void initData() {

        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
//        CommUtil.ticket(this, code, ticketHandler);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        img_url = intent.getStringExtra("img_url");
        jump_url = intent.getStringExtra("jump_url");
        backup = intent.getStringExtra("backup");
        point = intent.getIntExtra("point", -1);
        act = this;
        MApplication.listAppoint.add(act);

        openId = intent.getStringExtra("openId");
        wxNickname = intent.getStringExtra("wxNickname");
        tvBindphoneWxname.setText(wxNickname);
        spUtil.saveString("wxNickname",wxNickname);
        wxAvatar = intent.getStringExtra("wxAvatar");
        GlideUtil.loadCircleImg(mContext,wxAvatar,ivBindphoneWxhead,R.drawable.icon_production_default);
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
                    ivLoginClear.setVisibility(View.VISIBLE);
                }else {
                    ivLoginClear.setVisibility(View.GONE);
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
    }


    private void setView() {
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        //设置全屏
        setFullScreen();
        rlLoginGetcode.setClickable(false);
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

    /**
     * 获取登录背景视频信息
     */
    private void getLoginBgVideoInfo() {
        CommUtil.getLoginBgVideoInfo(this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                //有可能不返回数据
                try {
                    LoginBgVedioInfo loginBgVedioInfo = GsonUtil.gsonToBean(response, LoginBgVedioInfo.class);
                    if (loginBgVedioInfo.getCode() == 0) {
                        int version = SharedPreferenceUtil.getInstance(BindPhoneActivity.this).getInt(SP_TAG_LOGIN_BG_VIDEO_VERSION, 0);
                        mNewLoginBgVideoVersion = loginBgVedioInfo.getData().getVersion();
                        if (version != mNewLoginBgVideoVersion) { // 本地版本号与新版本号比较
                            getLoginBgVideo(loginBgVedioInfo.getData().getFile());
                        }
                    }
                    Utils.mLogError("ah  onSuccess statusCode == "+loginBgVedioInfo.toString());
                }
                catch (Exception e) {
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
                        SharedPreferenceUtil.getInstance(BindPhoneActivity.this).saveInt(SP_TAG_LOGIN_BG_VIDEO_VERSION,mNewLoginBgVideoVersion);
                        Utils.mLogError("ah  saveDataToSdcard == Success == " + binaryData.length);
                        try {
                            // 切换播放的视频
                            if (mMediaPlayer.isPlaying()) {
                                mMediaPlayer.stop();
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }
                            Utils.mLogError("Video 3  网络资源文件");
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

    private void setSilence(MediaPlayer player){
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setVolume(0,0);
    }

    private AsyncHttpResponseHandler codeHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @OnClick({R.id.iv_bindphone_back, R.id.iv_login_clear, R.id.rl_login_getcode, R.id.tv_login_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_bindphone_back:
                finish();
                break;
            case R.id.iv_login_clear:
                etLoginPhone.setText("");
                break;
            case R.id.rl_login_getcode:
                slat_md5 = MD5.md5(Global.MD5_STR, etLoginPhone.getText().toString().trim().replace(" ", ""));
                CommUtil.genVerifyCode(this, etLoginPhone.getText().toString().trim().replace(" ", ""),  slat_md5,
                        0, codeHandler);
                Intent verificationIntent = new Intent(BindPhoneActivity.this,VerificationCodeActivity.class);
                verificationIntent.putExtra("phone",etLoginPhone.getText().toString().trim());
                verificationIntent.putExtra("lat",lat);
                verificationIntent.putExtra("lng",lng);
                verificationIntent.putExtra("img_url",img_url);
                verificationIntent.putExtra("jump_url",jump_url);
                verificationIntent.putExtra("backup",backup);
                verificationIntent.putExtra("point",point);
                verificationIntent.putExtra("previous",Global.BINDPHONE_TOLOGIN);
                verificationIntent.putExtra("openId",openId);
                verificationIntent.putExtra("wxNickname",wxNickname);
                verificationIntent.putExtra("wxAvatar",wxAvatar);
                startActivity(verificationIntent);
                //暂停播放释放资源
                releaseSource();
                break;
            case R.id.tv_login_agreement:
                String url = CommUtil.getStaticUrl() + "static/content/protocol.html?system=" +
                        CommUtil.getSource() + "_" + Global.getCurrentVersion(this) + "&imei=" +
                        Global.getIMEI(this) + "&cellPhone=" +
                        SharedPreferenceUtil.getInstance(this).getString("cellphone", "") + "&time=";
                Intent intent = new Intent(BindPhoneActivity.this,
                        ADActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                //暂停播放释放资源
                releaseSource();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
