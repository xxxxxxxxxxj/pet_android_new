package com.haotang.pet.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.haotang.base.BaseFragment;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.ShopMallOrderDetailActivity;
import com.haotang.pet.adapter.GoodsOrderAdapter;
import com.haotang.pet.adapter.ShopMallOrderAdapter;
import com.haotang.pet.entity.ExitLoginEvent;
import com.haotang.pet.entity.GoodsAddEvent;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.ShopMallOrder;
import com.haotang.pet.entity.TabEntity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-11 11:22
 */
public class GoodsOrderFragment extends BaseFragment {
    @BindView(R.id.mTabLayout_4)
    CommonTabLayout mTabLayout4;
    @BindView(R.id.rv_order_list)
    RecyclerView rv_order_list;
    @BindView(R.id.srl_order_list)
    SwipeRefreshLayout srl_order_list;
    private String[] mTitles = {"全部", "待付款", "待发货", "待收货", "已完成", "已取消"};
    private int[] mIconUnselectIds = {
            R.drawable.tab_qb_normal, R.drawable.tab_dfk_normal,
            R.drawable.tab_dfh_normal, R.drawable.tab_dsh_normal, R.drawable.tab_ywc_normal, R.drawable.tab_yqx_normal};
    private int[] mIconSelectIds = {
            R.drawable.tab_qb_press, R.drawable.tab_dfk_press,
            R.drawable.tab_dfh_press, R.drawable.tab_dsh_press, R.drawable.tab_ywc_press, R.drawable.tab_yqx_press};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] colors = new int[2];
    private int oldindex;
    private ArrayList<ShopMallOrder> orderlist = new ArrayList<ShopMallOrder>();
    private ArrayList<ShopMallOrder> localOrderlist = new ArrayList<ShopMallOrder>();
    private GoodsOrderAdapter goodsOrderAdapter;
    private int page = 1;
    private int pageSize;

    //登录返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        if (event != null && event.isLogin()) {
            refresh();
        }
    }
    //订单返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GoodsAddEvent event) {
        refresh();
    }
    //退出登录
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitEventd(ExitLoginEvent exitLoginEvent){
        refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(R.layout.goodsorder_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
        refresh();
    }

    private void init() {
    }

    private void findView() {
    }

    private void setView() {
        srl_order_list.setRefreshing(true);
        srl_order_list.setColorSchemeColors(Color.rgb(47, 223, 189));
        rv_order_list.setHasFixedSize(true);
        rv_order_list.setLayoutManager(new LinearLayoutManager(mActivity));
        goodsOrderAdapter = new GoodsOrderAdapter(R.layout.item_shopmallorder_adapter, orderlist);
        rv_order_list.setAdapter(goodsOrderAdapter);
        mTabLayout4.setGradient(true);
        mTabLayout4.setmTextSelectsize(mTabLayout4.sp2px(16));
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        mTabLayout4.setColors(colors);
        mTabLayout4.setIndicatorTextMiddle(true);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout4.setTabData(mTabEntities);
        mTabLayout4.setCurrentTab(oldindex);
    }

    private void setLinster() {
        goodsOrderAdapter.setOnButtonClickListener(new ShopMallOrderAdapter.OnButtonClickListener() {
            @Override
            public void OnButtonClick(int position) {
                if (orderlist.size() > 0 && orderlist.size() > position) {
                    final ShopMallOrder shopMallOrder = orderlist.get(position);
                    if (shopMallOrder != null) {
                        if (shopMallOrder.getState() == 0) {//待付款
                            startActivity(new Intent(mActivity, ShopMallOrderDetailActivity.class).putExtra("orderId", shopMallOrder.getOrderId()));
                        } else if (shopMallOrder.getState() == 1) {//待发货
                            startActivity(new Intent(mActivity, ShopMallOrderDetailActivity.class).putExtra("orderId", shopMallOrder.getOrderId()));
                        } else if (shopMallOrder.getState() == 2) {//待收货
                            new AlertDialogNavAndPost(mActivity).builder().setTitle("")
                                    .setMsg("是否现在确认收货？").setPostTextColor(R.color.aD0021B).setNavTextColor(R.color.a666666)
                                    .setPositiveButton("立即收货", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPDialog.showDialog();
                                            CommUtil.shopMallOrderReceive(mActivity,
                                                    shopMallOrder.getOrderId(), shopMallOrderReceiveHandler);
                                        }
                                    }).setNegativeButton("还没收到", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                        } else if (shopMallOrder.getState() == 3) {//已完成
                            startActivity(new Intent(mActivity, ShopMallOrderDetailActivity.class).putExtra("orderId", shopMallOrder.getOrderId()));
                        } else if (shopMallOrder.getState() == 4 || shopMallOrder.getState() == -1) {//已取消
                            startActivity(new Intent(mActivity, ShopMallOrderDetailActivity.class).putExtra("orderId", shopMallOrder.getOrderId()));
                        }
                    }
                }
            }
        });
        goodsOrderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srl_order_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mTabLayout4.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout4.setmTextSelectsize(mTabLayout4.sp2px(16));
                if (position == 0) {
                    mTabLayout4.setIndicatorWidth(32);
                } else {
                    mTabLayout4.setIndicatorWidth(45);
                }
                if (oldindex != position) {
                    oldindex = position;
                    refresh();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    public void refresh() {
        page = 1;
        orderlist.clear();
        goodsOrderAdapter.setEnableLoadMore(false);
        srl_order_list.setRefreshing(true);
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.shopMallOrderList(mActivity,
                page, oldindex - 1, shopMallOrderListHandler);
    }

    private AsyncHttpResponseHandler shopMallOrderListHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            localOrderlist.clear();
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONArray jdata = jsonObject.getJSONArray("data");
                        if (jdata.length() > 0) {
                            for (int i = 0; i < jdata.length(); i++) {
                                localOrderlist.add(ShopMallOrder.json2Entity(jdata
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (page == 1) {
                        srl_order_list.setRefreshing(false);
                        goodsOrderAdapter.setEnableLoadMore(true);
                        orderlist.clear();
                    }
                    goodsOrderAdapter.loadMoreComplete();
                    if (localOrderlist != null && localOrderlist.size() > 0) {
                        if (page == 1) {
                            pageSize = localOrderlist.size();
                        } else {
                            if (localOrderlist.size() < pageSize) {
                                goodsOrderAdapter.loadMoreEnd(false);
                            }
                        }
                        orderlist.addAll(localOrderlist);
                        page++;
                    } else {
                        if (page == 1) {
                            goodsOrderAdapter.setEmptyView(setEmptyViewBase(2, "订单列表暂无商品～", "", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.mainactivity");
                                    intent.putExtra("previous",
                                            Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
                                    mActivity.sendBroadcast(intent);
                                }
                            },Color.TRANSPARENT,100));
                            goodsOrderAdapter.loadMoreEnd(true);
                        } else {
                            goodsOrderAdapter.loadMoreEnd(false);
                        }
                    }
                    goodsOrderAdapter.notifyDataSetChanged();
                } else {
                    if (Utils.checkLogin(mContext)) {
                        if (page == 1) {
                            goodsOrderAdapter.setEmptyView(setEmptyViewBase(1, msg,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            refresh();
                                        }
                                    }));
                            goodsOrderAdapter.setEnableLoadMore(false);
                            srl_order_list.setRefreshing(false);
                        } else {
                            goodsOrderAdapter.setEnableLoadMore(true);
                            goodsOrderAdapter.loadMoreFail();
                        }
                    } else {
                        goodsOrderAdapter.setEmptyView(setEmptyViewBase(1, "登录后查看订单", "去登录", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mContext, LoginNewActivity.class));
                            }
                        }));
                        goodsOrderAdapter.loadMoreEnd(true);
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    goodsOrderAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    goodsOrderAdapter.setEnableLoadMore(false);
                    srl_order_list.setRefreshing(false);
                } else {
                    goodsOrderAdapter.setEnableLoadMore(true);
                    goodsOrderAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            goodsOrderAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                goodsOrderAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                goodsOrderAdapter.setEnableLoadMore(false);
                srl_order_list.setRefreshing(false);
            } else {
                goodsOrderAdapter.setEnableLoadMore(true);
                goodsOrderAdapter.loadMoreFail();
            }
        }
    };

    private AsyncHttpResponseHandler shopMallOrderReceiveHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    refresh();
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(mActivity, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mActivity, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
        }
    };

    public static GoodsOrderFragment getInstance(String title) {
        GoodsOrderFragment sf = new GoodsOrderFragment();
        return sf;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
