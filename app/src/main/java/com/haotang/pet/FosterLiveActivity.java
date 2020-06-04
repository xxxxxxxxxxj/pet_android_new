package com.haotang.pet;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.example.playerlibrary.AlivcLiveRoom.AliyunVodPlayerView;
import com.example.playerlibrary.AlivcLiveRoom.PlayParameter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 寄养直播详情页
 */
public class FosterLiveActivity extends SuperActivity {
    @BindView(R.id.alivc_fosterlive)
    AliyunVodPlayerView alivcFosterlive;
    @BindView(R.id.tv_fosterlive_title)
    TextView tvFosterliveTitle;
    @BindView(R.id.ll_fosterlive_title)
    LinearLayout llFosterliveTitle;
    @BindView(R.id.tv_fosterlive_default)
    TextView tvFosterliveDefault;
    @BindView(R.id.ll_fosterlive_nonet)
    LinearLayout llFosterliveNonet;
    @BindView(R.id.ll_fosterlive_default)
    LinearLayout llFosterliveDefault;
    private String name;
    private String videoUrl;
    private final String TAG = "FosterLiveActivity";
    private static final int TITLEVISIBLEORGONE = 2;
    private static final int PLAYER_IDLE = 3;
    protected static final int SUSPEND_PLAY = 4;
    private static final int ONPREPARED = 5;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ONPREPARED:// 播放，隐藏提示
                    Log.e(TAG, "// 隐藏提示");
                    llFosterliveDefault.setVisibility(View.GONE);
                    llFosterliveNonet.setVisibility(View.GONE);
                    llFosterliveTitle.setVisibility(View.GONE);
                    closeDialog();
                    break;
                case SUSPEND_PLAY:// 移动网络下
                    Log.e(TAG, "case SUSPEND_PLAY:// 移动网络下,暂停播放");
                    llFosterliveDefault.setVisibility(View.VISIBLE);
                    llFosterliveNonet.setVisibility(View.VISIBLE);
                    llFosterliveTitle.setVisibility(View.GONE);
                    tvFosterliveDefault.setVisibility(View.GONE);
                    break;
                case PLAYER_IDLE:// 播放出错
                    closeDialog();
                    Log.e(TAG, "case PLAYER_IDLE:// 播放出错");
                    llFosterliveDefault.setVisibility(View.GONE);
                    llFosterliveNonet.setVisibility(View.GONE);
                    llFosterliveTitle.setVisibility(View.GONE);
                    tvFosterliveDefault.setVisibility(View.GONE);
                    tvFosterliveDefault.setText("直播君出状况了,小主待会再来吧~");
                    break;
                case TITLEVISIBLEORGONE:
                    llFosterliveTitle.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        isNetWorkPlay();
    }

    private void initData() {
        Utils.hideBottomUIMenu(mContext);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置为横屏
        MApplication.listAppoint.add(this);
        videoUrl = getIntent().getStringExtra("videoUrl");
        name = getIntent().getStringExtra("name");
    }

    private void findView() {
        setContentView(R.layout.activity_foster_live);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvFosterliveTitle.setText(name);
    }

    private void setLinster() {
    }

    private void isNetWorkPlay() {
        int networkType = Utils.getNetworkType(this);
        Log.e(TAG, "networkType = " + networkType);
        if (networkType == 2) {
            mHandler.sendEmptyMessage(SUSPEND_PLAY);
        } else {
            initAliyunPlayerView();
        }
    }

    private void initAliyunPlayerView() {
        showDialog();
        alivcFosterlive.initVideoView(true);
        //保持屏幕敞亮
        alivcFosterlive.setKeepScreenOn(true);
        PlayParameter.PLAY_PARAM_URL = videoUrl;
        alivcFosterlive.setAutoPlay(true);
        alivcFosterlive.setTheme(AliyunVodPlayerView.Theme.Blue);
        alivcFosterlive.setControlBarCanShow(false);
        alivcFosterlive.setTitleBarCanShow(false);
        alivcFosterlive.setCoverResource(R.drawable.cover_fm);
        alivcFosterlive.setNetConnectedListener(new AliyunVodPlayerView.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                Log.e(TAG, "onReNetConnected");
                if (isReconnect) {
                    ToastUtil.showToastShortBottom(mContext, "网络恢复");
                    alivcFosterlive.rePlay();
                }
            }

            @Override
            public void onNetUnConnected() {
                Log.e(TAG, "onNetUnConnected");
            }
        });
        alivcFosterlive.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                Log.e(TAG, "onPrepared");
                mHandler.sendEmptyMessage(ONPREPARED);
            }
        });
        alivcFosterlive.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
            @Override
            public void onError(int i, int i1, String s) {
                Log.e(TAG, "onError");
                alivcFosterlive.rePlay();
            }
        });
        alivcFosterlive.setOnTimeExpiredErrorListener(new IAliyunVodPlayer.OnTimeExpiredErrorListener() {
            @Override
            public void onTimeExpiredError() {
                alivcFosterlive.rePlay();
            }
        });
        alivcFosterlive.enableNativeLog();
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        alsb.setSource(PlayParameter.PLAY_PARAM_URL);
        Uri uri = Uri.parse(PlayParameter.PLAY_PARAM_URL);
        if ("rtmp".equals(uri.getScheme())) {
            alsb.setTitle("");
        }
        AliyunLocalSource localSource = alsb.build();
        alivcFosterlive.setLiveSource(localSource);
    }

    @Override
    protected void onPause() {
        super.onPause();
        alivcFosterlive.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alivcFosterlive.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        alivcFosterlive.onDestroy();
    }

    private void showDialog() {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
    }

    private void closeDialog() {
        if (mPDialog.isShowing()) {
            mPDialog.closeDialog();
        }
    }

    @OnClick({R.id.btn_fosterlive_jxgk, R.id.rl_fosterlive_back, R.id.rl_fosterlive_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_fosterlive_jxgk:
                initAliyunPlayerView();
                break;
            case R.id.rl_fosterlive_back:
                finish();
                break;
            case R.id.rl_fosterlive_root:
                if (llFosterliveTitle.getVisibility() == View.GONE) {
                    llFosterliveTitle
                            .setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessageDelayed(TITLEVISIBLEORGONE,
                            3000);
                } else {
                    llFosterliveTitle
                            .setVisibility(View.GONE);
                }
                break;
        }
    }
}
