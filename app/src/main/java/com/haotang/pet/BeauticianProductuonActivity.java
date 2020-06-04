package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BeauticianProductionAdapter;
import com.haotang.pet.entity.Production;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BeauticianProductuonActivity extends SuperActivity {
    public static BeauticianProductuonActivity beauticianProductuonActivity;
    private ImageButton ibBack;
    private TextView tvTitle;
    private PullToRefreshScrollView prlListView;
    private MyGridView gridview_production;
    public static ArrayList<Production> productionList = new ArrayList<Production>();
    private BeauticianProductionAdapter adapter;
    private MProgressDialog pDialog;
    private int beauticianid;
    private int serviceid;
    private int page = 1;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beauticianproduction);
        beauticianProductuonActivity = this;
        findView();
        setView();
    }

    private void findView() {
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        gridview_production = (MyGridView) findViewById(R.id.gridview_production);
        prlListView = (PullToRefreshScrollView) findViewById(R.id.prl_beauticianproduction_list);
        pDialog = new MProgressDialog(this);
    }

    private void setView() {
        tvTitle.setText("作品列表");
        beauticianid = getIntent().getIntExtra("beautician_id", 0);
        serviceid = getIntent().getIntExtra("serviceid", -1);
        prlListView.setMode(Mode.PULL_FROM_END);
        adapter = new BeauticianProductionAdapter(this, productionList);
        gridview_production.setAdapter(adapter);
        prlListView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (serviceid != -1) {
                    getData(serviceid, productionList.get(productionList.size() - 1).id, 0);
                } else {
                    getData(beauticianid, productionList.get(productionList.size() - 1).id, 0);
                }
            }
        });
        ibBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 返回
                finishWithAnimation();
            }
        });
        productionList.clear();
        if (serviceid != -1) {
            getData(serviceid, 0, 0);
        } else {
            getData(beauticianid, 0, 0);
        }
    }

    private void getData(int workerid, int beforeid, int afterid) {
        pDialog.showDialog();
        CommUtil.queryWorksByService(this, workerid, serviceid, beforeid, page, productionHanler);
    }

    private AsyncHttpResponseHandler productionHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            prlListView.onRefreshComplete();
            pDialog.closeDialog();
            Utils.mLogError("美容师作品：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray jData = jobj.getJSONArray("data");
                        if (jData.length() > 0) {
                            page++;
                            for (int i = 0; i < jData.length(); i++) {
                                Production pr = Production.json2Entity(jData.getJSONObject(i));
                                productionList.add(pr);
                            }
                        }
                        if (productionList.size() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg")) {
                        String msg = jobj.getString("msg");
                        ToastUtil.showToastShort(BeauticianProductuonActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            prlListView.onRefreshComplete();
            pDialog.closeDialog();
        }
    };

    public void setListData(int position) {
        try {
            clickPosition = position;
            Production production = productionList.get(clickPosition);
            production.praiseCount += 1;
            production.userPraise = 1;
            productionList.set(clickPosition, production);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void praiseAdd(int relateId, int position) {
        clickPosition = position;
        CommUtil.praiseAdd(mContext, 1, relateId, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                String msg = object.getString("msg");
                if (code == 0) {
                    Production production = productionList.get(clickPosition);
                    production.praiseCount += 1;
                    production.userPraise = 1;
                    productionList.set(clickPosition, production);
                    adapter.notifyDataSetChanged();
                    ToastUtil.showToastShortCenter(mContext, "点赞成功");
                }else{
                    ToastUtil.showToastShortCenter(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };
}
