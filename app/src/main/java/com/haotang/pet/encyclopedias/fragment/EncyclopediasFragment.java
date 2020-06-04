package com.haotang.pet.encyclopedias.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.haotang.base.BaseFragment;
import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.adapter.EncyclopediasAdapter;
import com.haotang.pet.encyclopedias.bean.Encyclopedias;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.huewu.pla.lib.MultiColumnListView;

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
 * @date XJ on 2018/8/2 11:29
 */
public class EncyclopediasFragment extends BaseFragment {
    private MaterialRefreshLayout mrl_encyclopedias_fragment;
    private MultiColumnListView mclv_encyclopedias_fragment;
    private int id;
    private List<Encyclopedias> list = new ArrayList<Encyclopedias>();
    private List<Encyclopedias> localList = new ArrayList<Encyclopedias>();
    private List<Encyclopedias> topList = new ArrayList<Encyclopedias>();
    private EncyclopediasAdapter<Encyclopedias> encyclopediasAdapter;
    private int mNextRequestPage = 1;
    private ImageView iv_emptyview_img;
    private TextView tv_emptyview_desc;
    private Button btn_emptyview;
    private LinearLayout ll_empty_view;

    public EncyclopediasFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();
        View view = initView(inflater, container);
        setView();
        setLinster();
        refresh();
        return view;
    }

    private void initData() {
        Bundle arguments = getArguments();
        id = arguments.getInt("id", 0);
        Log.e("TAG", "id = " + id);
    }

    private void setLinster() {
        btn_emptyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
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
    }

    private void refresh() {
        if(id == 0){
            mNextRequestPage = 1;
            localList.clear();
            list.clear();
            getData();
        }else{
            topList.clear();
            getTopData();
        }
    }

    private void getTopData() {
        CommUtil.queryEncyclopediaTopList(getActivity(), id, getTopHandler);
    }

    private AsyncHttpResponseHandler getTopHandler = new AsyncHttpResponseHandler() {

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
                                topList.add(Encyclopedias.json2Entity(jarrdata
                                        .getJSONObject(i)));
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(getActivity(), msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(getActivity(), "数据异常");
            }
            mNextRequestPage = 1;
            localList.clear();
            list.clear();
            getData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(getActivity(), "请求失败");
            mNextRequestPage = 1;
            localList.clear();
            list.clear();
            getData();
        }
    };

    private void loadMore() {
        getData();
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.encyclopedias_fragment, container, false);
        mrl_encyclopedias_fragment = (MaterialRefreshLayout) view.findViewById(R.id.mrl_encyclopedias_fragment);
        mclv_encyclopedias_fragment = (MultiColumnListView) view.findViewById(R.id.mclv_encyclopedias_fragment);
        iv_emptyview_img = (ImageView) view.findViewById(R.id.iv_emptyview_img);
        tv_emptyview_desc = (TextView) view.findViewById(R.id.tv_emptyview_desc);
        btn_emptyview = (Button) view.findViewById(R.id.btn_emptyview);
        ll_empty_view = (LinearLayout) view.findViewById(R.id.ll_empty_view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setView() {
        list.clear();
        encyclopediasAdapter = new EncyclopediasAdapter<Encyclopedias>(getActivity(), list);
        mclv_encyclopedias_fragment.setAdapter(encyclopediasAdapter);
    }

    private void getData() {
        localList.clear();
        CommUtil.queryEncyclopediaList(getActivity(), mNextRequestPage, id, "", getHandler);
    }

    private AsyncHttpResponseHandler getHandler = new AsyncHttpResponseHandler() {

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
                    if(id == 0){
                        if (localList.size() > 0) {
                            setEmptyView(true, 0, "", 0);
                            if (mNextRequestPage == 1) {
                                list.clear();
                            }
                            list.addAll(localList);
                            mNextRequestPage++;
                            encyclopediasAdapter.notifyDataSetChanged();
                        } else {
                            if (mNextRequestPage == 1) {
                                setEmptyView(false, 2, "该分类下还没有文章哦~", R.drawable.icon_no_mypet);
                            }
                        }
                    }else{
                        if (mNextRequestPage == 1) {
                            list.clear();
                            if (topList.size() > 0) {
                                list.addAll(topList);
                            }
                        }
                        if (localList.size() > 0) {
                            list.addAll(localList);
                            mNextRequestPage++;
                        }
                        if (list.size() > 0) {
                            setEmptyView(true, 0, "", 0);
                            encyclopediasAdapter.notifyDataSetChanged();
                        } else {
                            if (mNextRequestPage == 1) {
                                setEmptyView(false, 2, "该分类下还没有文章哦~", R.drawable.icon_no_mypet);
                            }
                        }
                    }
                } else {
                    if (mNextRequestPage == 1 && list.size() <= 0 && topList.size() <= 0) {
                        setEmptyView(false, 1, msg, R.drawable.icon_no_mypet);
                    }
                }
            } catch (JSONException e) {
                if (mNextRequestPage == 1 && list.size() <= 0 && topList.size() <= 0) {
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
            if(id == 0){
                if (mNextRequestPage == 1 && list.size() <= 0) {
                    setEmptyView(false, 1, "请求失败", R.drawable.icon_no_mypet);
                }
            }else{
                if (mNextRequestPage == 1) {
                    list.clear();
                    if (topList.size() > 0) {
                        list.addAll(topList);
                    }
                }
                if (list.size() > 0) {
                    setEmptyView(true, 0, "", 0);
                    encyclopediasAdapter.notifyDataSetChanged();
                } else {
                    if (mNextRequestPage == 1) {
                        setEmptyView(false, 1, "请求失败", R.drawable.icon_no_mypet);
                    }
                }
            }
            mrl_encyclopedias_fragment.finishRefresh();
            mrl_encyclopedias_fragment.finishRefreshLoadMore();
        }
    };

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
