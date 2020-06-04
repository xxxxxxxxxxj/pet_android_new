package com.haotang.pet.encyclopedias.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.adapter.EncyclopediasAdapter;
import com.haotang.pet.encyclopedias.bean.Encyclopedias;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;
import com.huewu.pla.lib.MultiColumnListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/3 0003.
 */

public class EncyclopediasSearchResultActivity extends SuperActivity {
    @BindView(R.id.input_word_search)
    ClearEditText input_word_search;
    @BindView(R.id.mclv_encyclopedias_fragment)
    MultiColumnListView mclv_encyclopedias_fragment;
    @BindView(R.id.mrl_encyclopedias_fragment)
    MaterialRefreshLayout mrl_encyclopedias_fragment;
    @BindView(R.id.iv_emptyview_img)
    ImageView iv_emptyview_img;
    @BindView(R.id.tv_emptyview_desc)
    TextView tv_emptyview_desc;
    @BindView(R.id.btn_emptyview)
    Button btn_emptyview;
    @BindView(R.id.ll_empty_view)
    LinearLayout ll_empty_view;
    private String keyWords = null;
    private int page = 1;
    private List<Encyclopedias> localList = new ArrayList<Encyclopedias>();
    private List<Encyclopedias> list = new ArrayList<Encyclopedias>();
    private EncyclopediasAdapter<Encyclopedias> encyclopediasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ency_search_result);
        ButterKnife.bind(this);
        keyWords = getIntent().getStringExtra("keyWords");
        if (!TextUtils.isEmpty(keyWords)) {
            input_word_search.setText(keyWords);
            input_word_search.setSelection(input_word_search.getText().length());
        } else {
            ToastUtil.showToastShortBottom(mContext, "请检查您的搜索关键字");
            return;
        }
        setview();
        setLinster();
        refresh();
    }

    private void refresh() {
        page = 1;
        list.clear();
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setLinster() {
        mrl_encyclopedias_fragment.setMaterialRefreshListener(new MaterialRefreshListener() {

            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                loadMore();
            }
        });
        input_word_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("keyWords", input_word_search.getText().toString());
                setResult(Global.RESULT_OK, intent);
                finishWithAnimation();
            }
        });
    }

    private void loadMore() {
        getData();
    }

    private void getData() {
        localList.clear();
        CommUtil.encyclopediaQueryEncyclopediaList(mContext, page,0, 0, keyWords, handler);
    }

    private void setview() {
        list.clear();
        encyclopediasAdapter = new EncyclopediasAdapter<Encyclopedias>(EncyclopediasSearchResultActivity.this, list);
        mclv_encyclopedias_fragment.setAdapter(encyclopediasAdapter);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONArray jarrdata = jobj.getJSONArray("data");
                        if (jarrdata.length() > 0) {
                            for (int i = 0; i < jarrdata.length(); i++) {
                                localList.add(Encyclopedias.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    if (localList.size() > 0) {
                        setEmptyView(true, 0, "", 0);
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        if (page == 1) {
                            setEmptyView(false, 2, "暂无百科数据~", R.drawable.icon_no_mypet);
                        }
                    }
                    encyclopediasAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1 && list.size() <= 0) {
                        setEmptyView(false, 1, msg, R.drawable.icon_no_mypet);
                    }
                }
            } catch (JSONException e) {
                if (page == 1 && list.size() <= 0) {
                    setEmptyView(false, 1, "数据异常", R.drawable.icon_no_mypet);
                }
                e.printStackTrace();
            }
            mrl_encyclopedias_fragment.finishRefresh();
            mrl_encyclopedias_fragment.finishRefreshLoadMore();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mrl_encyclopedias_fragment.finishRefresh();
            mrl_encyclopedias_fragment.finishRefreshLoadMore();
            if (page == 1 && list.size() <= 0) {
                setEmptyView(false, 1, "请求失败", R.drawable.icon_no_mypet);
            }
        }
    };

    @OnClick({R.id.input_word_search, R.id.textview_cancle, R.id.btn_emptyview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.input_word_search:
                break;
            case R.id.textview_cancle:
                finishWithAnimation();
                break;
            case R.id.btn_emptyview:
                refresh();
                break;
        }
    }

    private void setEmptyView(boolean visiblity, int flag, String msg, int resId) {
        if (visiblity) {
            ll_empty_view.setVisibility(View.GONE);
            mrl_encyclopedias_fragment.setVisibility(View.VISIBLE);
        } else {
            ll_empty_view.setVisibility(View.VISIBLE);
            mrl_encyclopedias_fragment.setVisibility(View.GONE);
            Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
            iv_emptyview_img.setImageResource(resId);
            if (flag == 1) {
                btn_emptyview.setVisibility(View.VISIBLE);
            } else if (flag == 2) {
                btn_emptyview.setVisibility(View.GONE);
            }
        }
    }
}
