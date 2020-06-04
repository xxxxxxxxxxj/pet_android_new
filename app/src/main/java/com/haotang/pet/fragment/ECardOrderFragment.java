package com.haotang.pet.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.haotang.base.BaseFragment;
import com.haotang.pet.CardOrderDetailActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.ToothCardOrderDetailActivity;
import com.haotang.pet.adapter.GiftCardOrderAdapter;
import com.haotang.pet.entity.ExitLoginEvent;
import com.haotang.pet.entity.GiftCardOrder;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.TabEntity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-11 11:22
 */
public class ECardOrderFragment extends BaseFragment {
    @BindView(R.id.mTabLayout_4)
    CommonTabLayout mTabLayout4;
    @BindView(R.id.rv_order_list)
    RecyclerView rv_order_list;
    @BindView(R.id.srl_order_list)
    SwipeRefreshLayout srl_order_list;
    private String[] mTitles = {"全部", "退款中", "已退款", "已完成"};
    private int[] mIconUnselectIds = {
            R.drawable.tab_qb_normal, R.drawable.tab_tkz_normal,
            R.drawable.tab_ytk_normal, R.drawable.tab_ywc_normal};
    private int[] mIconSelectIds = {
            R.drawable.tab_qb_press, R.drawable.tab_tkz_press,
            R.drawable.tab_ytk_press, R.drawable.tab_ywc_press};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] colors = new int[2];
    private int oldindex;
    private ArrayList<GiftCardOrder.DataBean> orderlist = new ArrayList<GiftCardOrder.DataBean>();
    private ArrayList<GiftCardOrder.DataBean> localOrderlist = new ArrayList<GiftCardOrder.DataBean>();
    private int page = 1;
    private int pageSize;
    private GiftCardOrderAdapter giftCardOrderAdapter;

    //登录返回 androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        if (event != null && event.isLogin()) {
            refresh();
        }
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
        View view = inflater.inflate(R.layout.ecardorder_fragment, null);
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
        giftCardOrderAdapter = new GiftCardOrderAdapter(R.layout.item_giftcardorder_adapter, orderlist);
        rv_order_list.setAdapter(giftCardOrderAdapter);
        rv_order_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = -DensityUtil.dp2px(getContext(), 15);
            }
        });
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
        giftCardOrderAdapter.setListener(new GiftCardOrderAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (orderlist != null && orderlist.size() > 0) {
                    if (orderlist.get(position).getCardType()==19){
                        Intent intentDetail = new Intent(getActivity(), CardOrderDetailActivity.class);
                        intentDetail.putExtra("serviceCardId", orderlist.get(position).getId());
                        startActivity(intentDetail);
                    }else if (orderlist.get(position).getCardType()==22){
                        Intent intent = new Intent(getActivity(), ToothCardOrderDetailActivity.class);
                        intent.putExtra("id", orderlist.get(position).getId());
                        startActivity(intent);
                    }

                }
            }
        });
        giftCardOrderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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

    private void refresh() {
        page = 1;
        orderlist.clear();
        giftCardOrderAdapter.setEnableLoadMore(false);
        srl_order_list.setRefreshing(true);
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.serviceCardOrderList(getActivity(),
                oldindex, page, serviceCardOrderListHandel);
    }

    private AsyncHttpResponseHandler serviceCardOrderListHandel = new AsyncHttpResponseHandler() {

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
                    Gson gson = new Gson();
                    GiftCardOrder giftCardOrder = gson.fromJson(new String(responseBody), GiftCardOrder.class);
                    if (giftCardOrder != null) {
                        List<GiftCardOrder.DataBean> giftCardOrderData = giftCardOrder.getData();
                        if (giftCardOrderData != null && giftCardOrderData.size() > 0) {
                            localOrderlist.addAll(giftCardOrderData);
                        }
                    }
                    if (page == 1) {
                        srl_order_list.setRefreshing(false);
                        giftCardOrderAdapter.setEnableLoadMore(true);
                        orderlist.clear();
                    }
                    giftCardOrderAdapter.loadMoreComplete();
                    if (localOrderlist != null && localOrderlist.size() > 0) {
                        if (page == 1) {
                            pageSize = localOrderlist.size();
                        } else {
                            if (localOrderlist.size() < pageSize) {
                                giftCardOrderAdapter.loadMoreEnd(false);
                            }
                        }
                        orderlist.addAll(localOrderlist);
                        page++;
                    } else {
                        if (page == 1) {
                            giftCardOrderAdapter.setEmptyView(setEmptyViewBase(2, "暂无E卡订单信息～", "去首页逛逛", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.mainactivity");
                                    intent.putExtra("previous",
                                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                                    getActivity().sendBroadcast(intent);
                                }
                            },Color.TRANSPARENT,100));
                            giftCardOrderAdapter.loadMoreEnd(true);
                        } else {
                            giftCardOrderAdapter.loadMoreEnd(false);
                        }
                    }
                    giftCardOrderAdapter.notifyDataSetChanged();
                } else {
                    if (Utils.checkLogin(mContext)) {
                        if (page == 1) {
                            giftCardOrderAdapter.setEmptyView(setEmptyViewBase(1, msg,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            refresh();
                                        }
                                    }));
                            giftCardOrderAdapter.setEnableLoadMore(false);
                            srl_order_list.setRefreshing(false);
                        } else {
                            giftCardOrderAdapter.setEnableLoadMore(true);
                            giftCardOrderAdapter.loadMoreFail();
                        }
                    } else {
                        giftCardOrderAdapter.setEmptyView(setEmptyViewBase(1, "登录后查看订单", "去登录", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mContext, LoginNewActivity.class));
                            }
                        }));
                        giftCardOrderAdapter.loadMoreEnd(true);
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    giftCardOrderAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    giftCardOrderAdapter.setEnableLoadMore(false);
                    srl_order_list.setRefreshing(false);
                } else {
                    giftCardOrderAdapter.setEnableLoadMore(true);
                    giftCardOrderAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            giftCardOrderAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                giftCardOrderAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                giftCardOrderAdapter.setEnableLoadMore(false);
                srl_order_list.setRefreshing(false);
            } else {
                giftCardOrderAdapter.setEnableLoadMore(true);
                giftCardOrderAdapter.loadMoreFail();
            }
        }
    };

    public static ECardOrderFragment getInstance(String title) {
        ECardOrderFragment sf = new ECardOrderFragment();
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
