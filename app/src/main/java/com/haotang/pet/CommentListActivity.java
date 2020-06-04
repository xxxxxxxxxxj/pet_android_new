package com.haotang.pet;

import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CommentAdapter;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 美容师评价列表，店铺评价列表
 */
public class CommentListActivity extends SuperActivity {

    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.btn_comment_list_all)
    Button btnCommentListAll;
    @BindView(R.id.btn_comment_list_img)
    Button btnCommentListImg;
    @BindView(R.id.rv_comment_list)
    RecyclerView rvCommentList;
    @BindView(R.id.srl_comment_list)
    SwipeRefreshLayout srlCommentList;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    private int hasImg;
    private int shopId;
    private int flag;
    private int type;
    private int beauticianid;
    private int pageSize = 10;
    private int mNextRequestPage = 1;
    private ArrayList<Comment> list = new ArrayList<Comment>();
    private ArrayList<Comment> localList = new ArrayList<Comment>();
    private CommentAdapter commentAdapter;
    private String totalAmount = "0";
    private String totalHasImgAmount = "0";
    private String strServiceIds;
    private int serviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置白底黑子
        setColorBar();
        initData();
        findView();
        setView();
        setLinster();
        refresh();
    }

    private void refresh() {
        commentAdapter.setEnableLoadMore(false);
        srlCommentList.setRefreshing(true);
        mNextRequestPage = 1;
        getData();
    }

    private void initData() {
        flag = getIntent().getIntExtra("flag", 0);
        type = getIntent().getIntExtra("type", 0);
        serviceType = getIntent().getIntExtra("serviceType", 0);
        shopId = getIntent().getIntExtra("shopId", 0);
        beauticianid = getIntent().getIntExtra("beautician_id", 0);
        strServiceIds = getIntent().getStringExtra("serviceids");
    }

    private void findView() {
        setContentView(R.layout.activity_comment_list);
        ButterKnife.bind(this);
    }

    private void setLinster() {
        commentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlCommentList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void loadMore() {
        getData();
    }

    private void setView() {
        vTitleSlide.setVisibility(View.GONE);
        tvTitlebarTitle.setText("评论");
        srlCommentList.setRefreshing(true);
        srlCommentList.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvCommentList.setHasFixedSize(true);
        rvCommentList.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(R.layout.item_comment
                , list);
        rvCommentList.setAdapter(commentAdapter);
    }

    private void getData() {
        if (flag == 1) {//美容师评价
            CommUtil.getCommentByBeauticianId(this, beauticianid, mNextRequestPage, pageSize, hasImg, commentHanler);
        } else if (flag == 2) {//服务评价
            CommUtil.queryCommentsByService(this, strServiceIds, serviceType, type, mNextRequestPage, hasImg, commentHanler);
        } else if (flag == 3) {//店铺评价
            CommUtil.shopCommentList(this, mNextRequestPage, hasImg, shopId, commentHanler);
        }
    }

    private String worderAvatar;
    private String workerName;
    private AsyncHttpResponseHandler commentHanler = new AsyncHttpResponseHandler() {

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
                            if (jdata.has("totalAmount") && !jdata.isNull("totalAmount")) {
                                totalAmount = jdata.getString("totalAmount");
                            } else {
                                totalAmount = "0";
                            }
                            if (jdata.has("totalHasImgAmount") && !jdata.isNull("totalHasImgAmount")) {
                                totalHasImgAmount = jdata.getString("totalHasImgAmount");
                            } else {
                                totalHasImgAmount = "0";
                            }
                            if (jdata.has("commentWorkerConfig") && !jdata.isNull("commentWorkerConfig")) {
                                JSONObject workerObject = jdata.getJSONObject("commentWorkerConfig");
                                if (workerObject.has("avatar") && !workerObject.isNull("avatar")) {
                                    worderAvatar = workerObject.getString("avatar");
                                }
                                if (workerObject.has("nickname") && !workerObject.isNull("nickname")) {
                                    workerName = workerObject.getString("nickname");
                                }
                            }
                            Log.i("aaaaaaa", worderAvatar + "-------" + workerName);
                            if (jdata.has("dataset") && !jdata.isNull("dataset")) {
                                JSONArray jarrcommentListList = jdata.getJSONArray("dataset");
                                if (jarrcommentListList.length() > 0) {
                                    for (int i = 0; i < jarrcommentListList.length(); i++) {
                                        localList.add(Comment.json2Entity(jarrcommentListList.getJSONObject(i)));
                                    }
                                    for (int i = 0; i < jarrcommentListList.length(); i++) {
                                        if (localList.get(i).getCommentWorkerContent() != null) {
                                            localList.get(i).setAvatarBeauty(worderAvatar);
                                            localList.get(i).setNickname(workerName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (mNextRequestPage == 1) {
                        srlCommentList.setRefreshing(false);
                        commentAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    commentAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        if (mNextRequestPage == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                commentAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        mNextRequestPage++;
                    } else {
                        if (mNextRequestPage == 1) {
                            commentAdapter.setEmptyView(setEmptyViewBase(2, "暂无评论~",
                                    R.drawable.icon_no_mypet, null));
                            commentAdapter.loadMoreEnd(true);
                        } else {
                            commentAdapter.loadMoreEnd(false);
                        }
                    }
                    btnCommentListAll.setText("全部(" + totalAmount + ")");
                    btnCommentListImg.setText("有图(" + totalHasImgAmount + ")");
                    commentAdapter.notifyDataSetChanged();
                } else {
                    if (mNextRequestPage == 1) {
                        commentAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        commentAdapter.setEnableLoadMore(false);
                        srlCommentList.setRefreshing(false);
                    } else {
                        commentAdapter.setEnableLoadMore(true);
                        commentAdapter.loadMoreFail();
                    }
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(CommentListActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "数据异常 e = " + e.toString());
                if (mNextRequestPage == 1) {
                    commentAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    commentAdapter.setEnableLoadMore(false);
                    srlCommentList.setRefreshing(false);
                } else {
                    commentAdapter.setEnableLoadMore(true);
                    commentAdapter.loadMoreFail();
                }
                e.printStackTrace();
                ToastUtil.showToastShortBottom(CommentListActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(CommentListActivity.this, "请求失败");
            if (mNextRequestPage == 1) {
                commentAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                commentAdapter.setEnableLoadMore(false);
                srlCommentList.setRefreshing(false);
            } else {
                commentAdapter.setEnableLoadMore(true);
                commentAdapter.loadMoreFail();
            }
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.btn_comment_list_all, R.id.btn_comment_list_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_comment_list_all:
                hasImg = -1;
                setIndex();
                break;
            case R.id.btn_comment_list_img:
                hasImg = 1;
                setIndex();
                break;
        }
    }

    private void setIndex() {
        if (hasImg == -1) {
            btnCommentListAll.setTextColor(getResources().getColor(R.color.white));
            btnCommentListImg.setTextColor(getResources().getColor(R.color.black));
            btnCommentListAll.setBackgroundResource(R.drawable.bg_comment_select);
            btnCommentListImg.setBackgroundResource(R.drawable.bg_comment_unselect);
            refresh();
        } else if (hasImg == 1) {
            btnCommentListAll.setBackgroundResource(R.drawable.bg_comment_unselect);
            btnCommentListImg.setBackgroundResource(R.drawable.bg_comment_select);
            btnCommentListAll.setTextColor(getResources().getColor(R.color.black));
            btnCommentListImg.setTextColor(getResources().getColor(R.color.white));
            refresh();
        }
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
}
