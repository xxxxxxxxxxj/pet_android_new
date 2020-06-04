package com.haotang.pet.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.haotang.pet.CardOrderDetailActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.ToothCardOrderDetailActivity;
import com.haotang.pet.adapter.GiftCardOrderAdapter;
import com.haotang.pet.entity.GiftCardOrder;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.WrapRecyLinearLayoutManger;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

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
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author:姜谷蓄
 * @Date:2019/3/29
 * @Description:E卡订单
 */
public class GiftCardOrderFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.btn_cardorder_nonet)
    Button btnCardorderNonet;
    @BindView(R.id.ll_cardorder_nonet)
    LinearLayout llCardorderNonet;
    @BindView(R.id.ll_cardorder_nodata)
    LinearLayout llCardorderNodata;
    @BindView(R.id.rv_cardorder)
    RecyclerView rvCardorder;
    @BindView(R.id.srl_cardorder)
    SwipeRefreshLayout srlCardorder;
    private List<GiftCardOrder.DataBean> localList = new ArrayList<>();
    private List<GiftCardOrder.DataBean> list = new ArrayList<>();
    private GiftCardOrderAdapter giftCardOrderAdapter;
    private MProgressDialog pDialog;
    private int page = 1;
    private int position;
    private int pageSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pDialog = new MProgressDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.giftcardorder_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        position = FragmentPagerItem.getPosition(getArguments());
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        setView();
        refresh();
        setListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefundCardEvent event) {
        if (event != null && event.isRefund()) {
            refresh();
        }
    }

    private void setListener() {
        srlCardorder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        giftCardOrderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        });

        giftCardOrderAdapter.setListener(new GiftCardOrderAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (list != null && list.size() > 0) {
                    if (list.get(position).getCardType()==19){
                        Intent intentDetail = new Intent(getActivity(), CardOrderDetailActivity.class);
                        intentDetail.putExtra("serviceCardId", list.get(position).getId());
                        startActivity(intentDetail);
                    }else if (list.get(position).getCardType()==22){
                        Intent intent = new Intent(getActivity(), ToothCardOrderDetailActivity.class);
                        intent.putExtra("id", list.get(position).getId());
                        startActivity(intent);
                    }

                }
            }
        });
    }

    private void refresh() {
        page = 1;
        list.clear();
        giftCardOrderAdapter.setEnableLoadMore(false);
        srlCardorder.setRefreshing(true);
        getData();
    }

    private void setView() {
        srlCardorder.setRefreshing(true);
        srlCardorder.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvCardorder.setHasFixedSize(true);
        WrapRecyLinearLayoutManger layoutManager = new WrapRecyLinearLayoutManger(getContext());
        layoutManager.setOrientation(WrapRecyLinearLayoutManger.VERTICAL);
        giftCardOrderAdapter = new GiftCardOrderAdapter(R.layout.item_giftcardorder_adapter,list);
        rvCardorder.setLayoutManager(layoutManager);
        rvCardorder.setAdapter(giftCardOrderAdapter);
        rvCardorder.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = -DensityUtil.dp2px(getContext(), 15);
            }
        });
    }

    private AsyncHttpResponseHandler serviceCardOrderListHandel = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            pDialog.closeDialog();
            llCardorderNonet.setVisibility(View.GONE);
            srlCardorder.setVisibility(View.VISIBLE);
            rvCardorder.setVisibility(View.VISIBLE);
            localList.clear();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    Gson gson = new Gson();
                    GiftCardOrder giftCardOrder = gson.fromJson(new String(responseBody), GiftCardOrder.class);
                    if (giftCardOrder != null) {
                        List<GiftCardOrder.DataBean> giftCardOrderData = giftCardOrder.getData();
                        if (giftCardOrderData != null && giftCardOrderData.size() > 0) {
                            localList.addAll(giftCardOrderData);
                        }
                    }
                    if (page == 1) {
                        srlCardorder.setRefreshing(false);
                        giftCardOrderAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    giftCardOrderAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        if (page == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                giftCardOrderAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        if (page == 1) {
                            giftCardOrderAdapter.setEmptyView(setEmptyViewBase(2, "暂无数据~",
                                    R.drawable.icon_no_mypet,null));
                            giftCardOrderAdapter.loadMoreEnd(true);
                        } else {
                            giftCardOrderAdapter.loadMoreEnd(false);
                        }
                    }
                } else {
                    if (page == 1) {
                        giftCardOrderAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        giftCardOrderAdapter.setEnableLoadMore(false);
                        srlCardorder.setRefreshing(false);
                    } else {
                        giftCardOrderAdapter.setEnableLoadMore(true);
                        giftCardOrderAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    giftCardOrderAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    giftCardOrderAdapter.setEnableLoadMore(false);
                    srlCardorder.setRefreshing(false);
                } else {
                    giftCardOrderAdapter.setEnableLoadMore(true);
                    giftCardOrderAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            giftCardOrderAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            if (page == 1) {
                giftCardOrderAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                giftCardOrderAdapter.setEnableLoadMore(false);
                srlCardorder.setRefreshing(false);
            } else {
                giftCardOrderAdapter.setEnableLoadMore(true);
                giftCardOrderAdapter.loadMoreFail();
            }
        }
    };

    private void getData() {
        pDialog.showDialog();
        CommUtil.serviceCardOrderList(getActivity(),
                position, page, serviceCardOrderListHandel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_cardorder_nonet, R.id.btn_cardorder_nodata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cardorder_nonet:
                refresh();
                break;
            case R.id.btn_cardorder_nodata:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.mainactivity");
                intent.putExtra("previous",
                        Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                getActivity().sendBroadcast(intent);
                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                    MApplication.listAppoint.get(i).finish();
                }
                for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                    MApplication.listAppoint1.get(i).finish();
                }
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }

    }
    protected View setEmptyViewBase(int flag, String msg, int resId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(getActivity(), R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        return emptyView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
