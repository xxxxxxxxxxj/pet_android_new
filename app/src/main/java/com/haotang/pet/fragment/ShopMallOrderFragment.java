package com.haotang.pet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.ShopMallOrderDetailActivity;
import com.haotang.pet.adapter.ShopMallOrderAdapter;
import com.haotang.pet.entity.ShopMallOrder;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/30 19:01
 */
public class ShopMallOrderFragment extends Fragment implements View.OnClickListener {
    private Activity mActivity;
    private SharedPreferenceUtil spUtil;
    private MProgressDialog pDialog;
    private PullToRefreshListView prlv_shopmallorder;
    private int page = 1;
    private List<ShopMallOrder> listShopMallOrder = new ArrayList<ShopMallOrder>();
    private ShopMallOrderAdapter<ShopMallOrder> shopMallOrderAdapter;
    private int position;
    private LinearLayout ll_shopmallorder_nodata;
    private Button btn_shopmallorder_nodata;
    private LinearLayout ll_shopmallorder_nonet;
    private Button btn_shopmallorder_nonet;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initData(activity);
    }

    private void initData(Activity activity) {
        this.mActivity = activity;
        spUtil = SharedPreferenceUtil.getInstance(mActivity);
        pDialog = new MProgressDialog(mActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shopmallorder_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        position = FragmentPagerItem.getPosition(getArguments());
        findView(view);
        setView();
        setLinster();
        getData();
    }

    private void findView(View view) {
        ll_shopmallorder_nonet = (LinearLayout) view.findViewById(R.id.ll_shopmallorder_nonet);
        btn_shopmallorder_nonet = (Button) view.findViewById(R.id.btn_shopmallorder_nonet);
        ll_shopmallorder_nodata = (LinearLayout) view.findViewById(R.id.ll_shopmallorder_nodata);
        prlv_shopmallorder = (PullToRefreshListView) view.findViewById(R.id.prlv_shopmallorder);
        btn_shopmallorder_nodata = (Button) view.findViewById(R.id.btn_shopmallorder_nodata);
    }

    private void setView() {
        prlv_shopmallorder.setMode(PullToRefreshBase.Mode.BOTH);
        listShopMallOrder.clear();
        shopMallOrderAdapter = new ShopMallOrderAdapter<ShopMallOrder>(mActivity, listShopMallOrder);
        shopMallOrderAdapter.clearDeviceList();
        prlv_shopmallorder.setAdapter(shopMallOrderAdapter);
    }


    private void setLinster() {
        btn_shopmallorder_nonet.setOnClickListener(this);
        btn_shopmallorder_nodata.setOnClickListener(this);
        prlv_shopmallorder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
                    // 下拉刷新
                    page = 1;
                    getData();
                } else {
                    page++;
                    getData();
                }
            }
        });
        shopMallOrderAdapter.setOnButtonClickListener(new ShopMallOrderAdapter.OnButtonClickListener() {
            @Override
            public void OnButtonClick(int position) {
                if (listShopMallOrder.size() > 0 && listShopMallOrder.size() > position) {
                    final ShopMallOrder shopMallOrder = listShopMallOrder.get(position);
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
                                            pDialog.showDialog();
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
    }

    private AsyncHttpResponseHandler shopMallOrderReceiveHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            prlv_shopmallorder.onRefreshComplete();
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    page = 1;
                    getData();
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
            prlv_shopmallorder.onRefreshComplete();
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
        }
    };

    private void getData() {
        if (page == 1) {
            listShopMallOrder.clear();
            shopMallOrderAdapter.clearDeviceList();
        }
        pDialog.showDialog();
        CommUtil.shopMallOrderList(mActivity,
                page, position - 1, shopMallOrderListHandler);
    }

    private AsyncHttpResponseHandler shopMallOrderListHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            ll_shopmallorder_nonet.setVisibility(View.GONE);
            prlv_shopmallorder.onRefreshComplete();
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    JSONArray jadata = jobj.getJSONArray("data");
                    if (jadata != null && jadata.length() > 0) {
                        if (page == 1) {
                            listShopMallOrder.clear();
                            shopMallOrderAdapter.clearDeviceList();
                        }
                        for (int i = 0; i < jadata.length(); i++) {
                            listShopMallOrder.add(ShopMallOrder
                                    .json2Entity(jadata
                                            .getJSONObject(i)));
                        }
                    } else {
                        if (page > 1) {
                            ToastUtil.showToastShortBottom(mActivity, "没有更多数据了");
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(mActivity, msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mActivity, "数据异常");
            }
            writeData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            prlv_shopmallorder.onRefreshComplete();
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(mActivity, "请求失败");
            ll_shopmallorder_nonet.setVisibility(View.VISIBLE);
            ll_shopmallorder_nodata.setVisibility(View.GONE);
            prlv_shopmallorder.setVisibility(View.GONE);
        }
    };

    private void writeData() {
        if (listShopMallOrder.size() > 0) {
            ll_shopmallorder_nodata.setVisibility(View.GONE);
            prlv_shopmallorder.setVisibility(View.VISIBLE);
            shopMallOrderAdapter.setData(listShopMallOrder);
        } else {
            prlv_shopmallorder.setVisibility(View.GONE);
            ll_shopmallorder_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shopmallorder_nonet:
                page = 1;
                getData();
                break;
            case R.id.btn_shopmallorder_nodata:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.mainactivity");
                intent.putExtra("previous",
                        Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
                mActivity.sendBroadcast(intent);
                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                    MApplication.listAppoint.get(i).finish();
                }
                for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                    MApplication.listAppoint1.get(i).finish();
                }
                if (mActivity != null) {
                    mActivity.finish();
                }
                break;
        }
    }
}
