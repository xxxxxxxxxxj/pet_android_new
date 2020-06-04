package com.haotang.pet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.NewbieTaskAdapter;
import com.haotang.pet.entity.NewbieTaskBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GlideImageLoader;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 新手任务界面
 */
public class NewbieTaskActivity extends SuperActivity implements OnBannerListener {
    @BindView(R.id.iv_newbittask_back)
    ImageView ivNewbittaskBack;
    @BindView(R.id.rl_newbittask_back)
    RelativeLayout rlNewbittaskBack;
    @BindView(R.id.banner_newbittask)
    Banner bannerNewbittask;
    @BindView(R.id.rv_newbittask)
    RecyclerView rvNewbittask;
    @BindView(R.id.tv_newbittask_hdgz)
    TextView tvNewbittaskHdgz;
    private List<NewbieTaskBean> list = new ArrayList<NewbieTaskBean>();
    private NewbieTaskAdapter newbieTaskAdapter;
    private List<com.haotang.pet.entity.Banner> bannerList = new ArrayList<com.haotang.pet.entity.Banner>();
    private int activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        initWindows();
        setLinster();
        setView();
        getData();
        addActivityLog();
    }

    private void addActivityLog() {
        CommUtil.addActivityLog(mContext, activityId, 0, 0, addActivityLogHanler);
    }

    private AsyncHttpResponseHandler addActivityLogHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                } else {
                    Utils.Exit(NewbieTaskActivity.this, resultCode);
                }
            } catch (JSONException e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void getData() {
        mPDialog.showDialog();
        bannerList.clear();
        list.clear();
        CommUtil.loadFreshManPage(mContext, activityId, loadFreshManPageHanler);
    }

    private AsyncHttpResponseHandler loadFreshManPageHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("activityInfo")
                                && !jData.isNull("activityInfo")) {
                            JSONObject jactivityInfo = jData.getJSONObject("activityInfo");
                            if (jactivityInfo.has("activityBgPic")
                                    && !jactivityInfo.isNull("activityBgPic")) {
                                String activityBgPic = jactivityInfo.getString("activityBgPic");
                                bannerList.add(new com.haotang.pet.entity.Banner(activityBgPic));
                                setBanner();
                            }
                            if (jactivityInfo.has("activityRule")
                                    && !jactivityInfo.isNull("activityRule")) {
                                String activityRule = jactivityInfo.getString("activityRule");
                                Utils.setTextStyleAndColor(NewbieTaskActivity.this, activityRule, R.color.aD0021B,
                                        R.style.style4, tvNewbittaskHdgz);
                            }
                        }
                        if (jData.has("taskInfos")
                                && !jData.isNull("taskInfos")) {
                            JSONArray jtaskInfos =
                                    jData.getJSONArray("taskInfos");
                            if (jtaskInfos != null && jtaskInfos.length() > 0) {
                                list.clear();
                                for (int i = 0; i < jtaskInfos.length(); i++) {
                                    list.add(NewbieTaskBean.json2Entity(jtaskInfos
                                            .getJSONObject(i)));
                                }
                                newbieTaskAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    Utils.Exit(NewbieTaskActivity.this, resultCode);
                }
            } catch (JSONException e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void initData() {
        MApplication.listAppoint.add(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", 0);
    }

    private void initWindows() {
        Window window = getWindow();
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
                .build(this)
                .apply();
    }

    private void findView() {
        setContentView(R.layout.activity_newbie_task);
        ButterKnife.bind(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NewbieTaskBean event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.getTaskStatus() == 1) {//领取
                mPDialog.showDialog();
                CommUtil.receiveReward(mContext, activityId, event.getId(), receiveRewardHanler);
            } else if (event.getTaskStatus() == 0) {//去完成
                Utils.goService(this, event.getPoint(), event.getBackup());
            }
        }
    }

    private AsyncHttpResponseHandler receiveRewardHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    mPDialog.showDialog();
                    bannerList.clear();
                    list.clear();
                    CommUtil.loadFreshManPage(mContext, activityId, loadFreshManPageHanler);
                } else {
                    Utils.Exit(NewbieTaskActivity.this, resultCode);
                }
            } catch (JSONException e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void setLinster() {

    }

    private void setView() {
        rlNewbittaskBack.bringToFront();
        ivNewbittaskBack.bringToFront();
        rvNewbittask.setHasFixedSize(true);
        rvNewbittask.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvNewbittask.setLayoutManager(noScollFullLinearLayoutManager);
        newbieTaskAdapter = new NewbieTaskAdapter(R.layout.item_newbietask, list);
        rvNewbittask.setAdapter(newbieTaskAdapter);
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_f8_15));
        rvNewbittask.addItemDecoration(divider);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void setBanner() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < bannerList.size(); i++) {
            list.add(bannerList.get(i).pic);
        }
        bannerNewbittask.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        if (bannerList != null && bannerList.size() > 0 && bannerList.size() > position) {
            com.haotang.pet.entity.Banner banner = bannerList.get(position);
            if (banner != null) {
                Utils.goService(NewbieTaskActivity.this, banner.point, banner.backup);
            }
        }
    }

    @OnClick({R.id.iv_newbittask_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_newbittask_back:
                finishWithAnimation();
                break;
        }
    }
}
