package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CanTransactionAdapter;
import com.haotang.pet.entity.CanTransaction;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 罐头币交易明细界面
 */
public class TransactionDetailsActivity extends SuperActivity implements
        View.OnClickListener {
    private MProgressDialog pDialog;
    private ImageButton ibBack;
    private TextView tvTitle;
    private PullToRefreshListView prl_cantransaction_list;
    private ArrayList<CanTransaction> canTransactionList = new ArrayList<CanTransaction>();
    private int page = 1;
    private CanTransactionAdapter canTransactionAdapter;
    private LinearLayout ll_cantransaction_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void init() {
        pDialog = new MProgressDialog(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
    }

    private void findView() {
        setContentView(R.layout.activity_transaction_details);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        prl_cantransaction_list = (PullToRefreshListView) findViewById(R.id.prl_cantransaction_list);
        ll_cantransaction_default = (LinearLayout) findViewById(R.id.ll_cantransaction_default);
    }

    private void setView() {
        tvTitle.setText("收支明细");
        prl_cantransaction_list.setMode(PullToRefreshBase.Mode.BOTH);
        canTransactionList.clear();
        canTransactionAdapter = new CanTransactionAdapter(this, canTransactionList);
        canTransactionAdapter.clearDeviceList();
        prl_cantransaction_list.setAdapter(canTransactionAdapter);
    }

    private void setLinster() {
        ibBack.setOnClickListener(this);
        prl_cantransaction_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
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
    }

    private void getData() {
        pDialog.showDialog();
        CommUtil.canBillInOutRecord(mContext, page, canBillInOutRecordHandler);
    }

    private AsyncHttpResponseHandler canBillInOutRecordHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            prl_cantransaction_list.onRefreshComplete();
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray jadata = jobj.getJSONArray("data");
                        if (jadata != null && jadata.length() > 0) {
                            ll_cantransaction_default.setVisibility(View.GONE);
                            prl_cantransaction_list.setVisibility(View.VISIBLE);
                            if (page == 1) {
                                canTransactionList.clear();
                                canTransactionAdapter.clearDeviceList();
                            }
                            for (int i = 0; i < jadata.length(); i++) {
                                canTransactionList.add(CanTransaction
                                        .json2Entity(jadata
                                                .getJSONObject(i)));
                            }
                            canTransactionAdapter.setData(canTransactionList);
                        } else {
                            if (page == 1) {
                                prl_cantransaction_list.setVisibility(View.GONE);
                                ll_cantransaction_default.setVisibility(View.VISIBLE);
                            } else {
                                ll_cantransaction_default.setVisibility(View.GONE);
                                prl_cantransaction_list.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(TransactionDetailsActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(TransactionDetailsActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            prl_cantransaction_list.onRefreshComplete();
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(TransactionDetailsActivity.this, "请求失败");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
