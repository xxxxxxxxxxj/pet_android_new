package com.haotang.pet.encyclopedias.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.adapter.EncyCommentAdapter;
import com.haotang.pet.encyclopedias.bean.EncyclopediasComment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 百科评论界面
 */
public class EncyCommentActivity extends SuperActivity {

    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_ency_etlenth)
    TextView tv_ency_etlenth;
    @BindView(R.id.tv_ency_comment_submit)
    TextView tvEncyCommentSubmit;
    @BindView(R.id.et_ency_comment)
    EditText etEncyComment;
    @BindView(R.id.rv_ency_comment)
    RecyclerView rvEncyComment;
    @BindView(R.id.srl_ency_comment)
    SwipeRefreshLayout srlEncyComment;
    @BindView(R.id.rl_ency_comment_comm)
    RelativeLayout rl_ency_comment_comm;
    private List<EncyclopediasComment> list = new ArrayList<EncyclopediasComment>();
    private List<EncyclopediasComment> localList = new ArrayList<EncyclopediasComment>();
    private int mNextRequestPage = 1;
    private EncyCommentAdapter encyCommentAdapter;
    private int infoId;
    private int pageSize;
    private int commentState;
    private String isBlack;
    private boolean isToLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        refresh();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        infoId = getIntent().getIntExtra("infoId", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_ency_comment);
        ButterKnife.bind(this);
    }

    private void setLinster() {
        etEncyComment.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!Utils.checkLogin(EncyCommentActivity.this)) {
                    if (!isToLogin) {
                        isToLogin = true;
                        Intent intent = new Intent(
                                EncyCommentActivity.this,
                                LoginNewActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
        etEncyComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tv_ency_etlenth.setText("" + s.length() + "/120");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() <= 0) {
                    tvEncyCommentSubmit.setEnabled(false);
                    tvEncyCommentSubmit.setTextColor(getResources().getColor(R.color.a666666));
                } else {
                    tvEncyCommentSubmit.setEnabled(true);
                    tvEncyCommentSubmit.setTextColor(getResources().getColor(R.color.aD0021B));
                }
            }
        });
        encyCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlEncyComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        encyCommentAdapter.setEnableLoadMore(false);
        srlEncyComment.setRefreshing(true);
        mNextRequestPage = 1;
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void setView() {
        tvTitlebarTitle.setText("全部评论");

        srlEncyComment.setRefreshing(true);
        srlEncyComment.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvEncyComment.setHasFixedSize(true);
        rvEncyComment.setLayoutManager(new LinearLayoutManager(this));
        encyCommentAdapter = new EncyCommentAdapter(R.layout.item_ency_comment, list);
        rvEncyComment.setAdapter(encyCommentAdapter);
    }

    private void getData() {
        commentState = 0;
        isBlack = null;
        CommUtil.getEncyCommentList(this, infoId, mNextRequestPage, getEncyCommentListHandler);
    }

    private AsyncHttpResponseHandler getEncyCommentListHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            localList.clear();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("commentState") && !jdata.isNull("commentState")) {
                                commentState = jdata.getInt("commentState");
                            }
                            if (jdata.has("isBlack") && !jdata.isNull("isBlack")) {
                                isBlack = jdata.getString("isBlack");
                            }
                            if (jdata.has("commentList") && !jdata.isNull("commentList")) {
                                JSONArray jarrcommentListList = jdata.getJSONArray("commentList");
                                if (jarrcommentListList.length() > 0) {
                                    for (int i = 0; i < jarrcommentListList.length(); i++) {
                                        localList.add(EncyclopediasComment.json2Entity(jarrcommentListList
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                    if (mNextRequestPage == 1) {
                        srlEncyComment.setRefreshing(false);
                        encyCommentAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    encyCommentAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        if (mNextRequestPage == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                encyCommentAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        mNextRequestPage++;
                    } else {
                        if (mNextRequestPage == 1) {
                            encyCommentAdapter.setEmptyView(setEmptyViewBase(2, "暂无评论~",
                                    R.drawable.icon_no_mypet, null));
                            encyCommentAdapter.loadMoreEnd(true);
                        } else {
                            encyCommentAdapter.loadMoreEnd(false);
                        }
                    }
                    encyCommentAdapter.notifyDataSetChanged();
                    if (commentState == 0 && isBlack == null) {
                        rl_ency_comment_comm.setVisibility(View.VISIBLE);
                    } else {
                        rl_ency_comment_comm.setVisibility(View.GONE);
                    }
                } else {
                    if (mNextRequestPage == 1) {
                        encyCommentAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        encyCommentAdapter.setEnableLoadMore(false);
                        srlEncyComment.setRefreshing(false);
                    } else {
                        encyCommentAdapter.setEnableLoadMore(true);
                        encyCommentAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (mNextRequestPage == 1) {
                    encyCommentAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    encyCommentAdapter.setEnableLoadMore(false);
                    srlEncyComment.setRefreshing(false);
                } else {
                    encyCommentAdapter.setEnableLoadMore(true);
                    encyCommentAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            if (mNextRequestPage == 1) {
                encyCommentAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                encyCommentAdapter.setEnableLoadMore(false);
                srlEncyComment.setRefreshing(false);
            } else {
                encyCommentAdapter.setEnableLoadMore(true);
                encyCommentAdapter.loadMoreFail();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (!Utils.checkLogin(this)) {
            isToLogin = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_ency_comment_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_ency_comment_submit:
                CommUtil.PostEncyComment(this, infoId, etEncyComment.getText().toString().trim(), PostEncyCommentHandler);
                break;
        }
    }

    private AsyncHttpResponseHandler PostEncyCommentHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    Utils.goneJP(EncyCommentActivity.this);
                    ToastUtil.showToastShortCenter(EncyCommentActivity.this, "发表评论成功");
                    etEncyComment.setText("");
                    rvEncyComment.scrollToPosition(0);
                    refresh();
                    try {
                        if (!spUtil.getBoolean("GOTOMARKET_DIALOG_TRUE", false)) {//先判断是否点击跳转到应用市场
                            //再判断是否点击暂时没有
                            if (spUtil.getBoolean("GOTOMARKET_DIALOG_FALSE", false)) {
                                //再判断距离上一次点击暂时没有是否大于10天且判断打开次数是否是5次
                                String gotomarket_dialog_time = spUtil.getString("GOTOMARKET_DIALOG_TIME", "");
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                String format = df.format(new Date());// new Date()为获取当前系统时间
                                if (Utils.daysBetween(gotomarket_dialog_time, format) >= 10) {
                                    //弹出
                                    Utils.goMarketDialog(EncyCommentActivity.this);
                                }
                            } else {
                                //弹出
                                Utils.goMarketDialog(EncyCommentActivity.this);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(EncyCommentActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(EncyCommentActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(EncyCommentActivity.this, "请求失败");
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Utils.hideKeyboard(ev, etEncyComment, EncyCommentActivity.this);//调用方法判断是否需要隐藏键盘
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
