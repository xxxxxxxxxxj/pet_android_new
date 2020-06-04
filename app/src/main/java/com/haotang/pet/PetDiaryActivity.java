package com.haotang.pet;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.PetDiaryDiaryAdapter;
import com.haotang.pet.adapter.PetDiaryMonthAdapter;
import com.haotang.pet.entity.PetDiary;
import com.haotang.pet.entity.PetServiceMonth;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

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
 * 宠物日记
 */
public class PetDiaryActivity extends SuperActivity {
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_petdiary_month)
    RecyclerView rvPetdiaryMonth;
    @BindView(R.id.tv_petdiary_desc)
    TextView tvPetdiaryDesc;
    @BindView(R.id.rv_petdiary_diary)
    RecyclerView rvPetdiaryDiary;
    @BindView(R.id.mrl_petdiary)
    MaterialRefreshLayout mrlPetdiary;
    @BindView(R.id.ll_pet_diary)
    LinearLayout ll_pet_diary;
    @BindView(R.id.ll_pet_diary_empty)
    LinearLayout ll_pet_diary_empty;
    @BindView(R.id.tv_emptyview_desc)
    TextView tv_emptyview_desc;
    @BindView(R.id.btn_emptyview)
    Button btn_emptyview;
    @BindView(R.id.iv_petdiary_pettype)
    ImageView iv_petdiary_pettype;
    private int customerpetid;
    private ArrayList<String> monthList;
    private String localMonth;
    private int page = 1;
    private int pageSize = 10;
    private List<PetDiary> list = new ArrayList<PetDiary>();
    private List<PetDiary> localList = new ArrayList<PetDiary>();
    private List<PetServiceMonth> monthBeanList = new ArrayList<PetServiceMonth>();
    private PetDiaryMonthAdapter petDiaryMonthAdapter;
    private PetDiaryDiaryAdapter petDiaryDiaryAdapter;
    private String marked;
    private int firstSelectIndex;
    private int petKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        act = this;
        MApplication.listAppoint.add(this);
        monthList = getIntent().getStringArrayListExtra("monthList");
        customerpetid = getIntent().getIntExtra("customerpetid", 0);
        localMonth = getIntent().getStringExtra("localMonth");
        Log.e("TAG", "monthList = " + monthList.toString());
    }

    private void findView() {
        setContentView(R.layout.activity_pet_diary);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("护理记录");
        Log.e("TAG", "monthBeanList = " + monthBeanList.toString());
        rvPetdiaryMonth.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PetDiaryActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPetdiaryMonth.setLayoutManager(linearLayoutManager);
        monthBeanList.clear();
        petDiaryMonthAdapter = new PetDiaryMonthAdapter(R.layout.item_petdiary_month, monthBeanList);
        rvPetdiaryMonth.setAdapter(petDiaryMonthAdapter);
        rvPetdiaryDiary.setHasFixedSize(true);
        rvPetdiaryDiary.setLayoutManager(new LinearLayoutManager(this));
        petDiaryDiaryAdapter = new PetDiaryDiaryAdapter(R.layout.item_petdiary_diary, list);
        rvPetdiaryDiary.setAdapter(petDiaryDiaryAdapter);
        rvPetdiaryMonth.scrollToPosition(firstSelectIndex);
        if (monthList != null && monthList.size() > 0) {
            ll_pet_diary.setVisibility(View.VISIBLE);
            ll_pet_diary_empty.setVisibility(View.GONE);
            monthBeanList.clear();
            for (int i = 0; i < monthList.size(); i++) {
                String month = monthList.get(i);
                Log.e("TAG", "month = " + month);
                if (Utils.isStrNull(month)) {
                    String[] split = month.split("-");
                    Log.e("TAG", "split = " + split.toString());
                    Log.e("TAG", "localMonth = " + localMonth);
                    if (split != null && split.length == 2) {
                        if (month.equals(localMonth)) {
                            firstSelectIndex = i;
                            monthBeanList.add(new PetServiceMonth(split[0], Integer.parseInt(split[1]) + "月", true));
                        } else {
                            monthBeanList.add(new PetServiceMonth(split[0], Integer.parseInt(split[1]) + "月", false));
                        }
                    }
                }
            }
            petDiaryMonthAdapter.notifyDataSetChanged();
            mrlPetdiary.autoRefresh();
        } else {
            ll_pet_diary.setVisibility(View.GONE);
            ll_pet_diary_empty.setVisibility(View.VISIBLE);
            tv_emptyview_desc.setText("您的宠物还没有做过服务哦～");
            btn_emptyview.setVisibility(View.GONE);
        }
        Log.e("TAG", "monthBeanList = " + monthBeanList.toString());
    }

    private void refresh() {
        page = 1;
        list.clear();
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void setLinster() {
        petDiaryMonthAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (monthBeanList.size() > position) {
                    localMonth = monthList.get(position);
                    PetServiceMonth petServiceMonth = monthBeanList.get(position);
                    if (petServiceMonth != null) {
                        for (int i = 0; i < monthBeanList.size(); i++) {
                            monthBeanList.get(i).setSelect(false);
                        }
                        petServiceMonth.setSelect(true);
                        petDiaryMonthAdapter.notifyDataSetChanged();
                        refresh();
                    }
                }
            }
        });
        mrlPetdiary.setMaterialRefreshListener(new MaterialRefreshListener() {

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

    private void getData() {
        localList.clear();
        CommUtil.queryCustomerPetDiaryById(this, pageSize, page, customerpetid, localMonth, handler);
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
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("careHistory") && !jdata.isNull("careHistory")) {
                            JSONObject jcareHistory = jdata.getJSONObject("careHistory");
                            if (jcareHistory.has("marked") && !jcareHistory.isNull("marked")) {
                                marked = jcareHistory.getString("marked");
                            }
                            if (jcareHistory.has("petKind") && !jcareHistory.isNull("petKind")) {
                                petKind = jcareHistory.getInt("petKind");
                            }
                            if (jcareHistory.has("message") && !jcareHistory.isNull("message")) {
                                JSONArray jarrmessage = jcareHistory.getJSONArray("message");
                                if (jarrmessage.length() > 0) {
                                    for (int i = 0; i < jarrmessage.length(); i++) {
                                        localList.add(PetDiary.json2Entity(jarrmessage
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                    if (localList.size() > 0) {
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        if (page == 1) {
                            petDiaryDiaryAdapter.setEmptyView(setEmptyViewBase(2, "暂无护理记录哦~",
                                    R.drawable.icon_no_mypet, null));
                        }
                    }
                    petDiaryDiaryAdapter.notifyDataSetChanged();
                    Utils.setText(tvPetdiaryDesc, marked, "", View.VISIBLE, View.VISIBLE);
                    if (petKind == 1) {// 狗
                        iv_petdiary_pettype.setImageResource(R.drawable.main_dog_img);
                    } else if (petKind == 2) {// 猫
                        iv_petdiary_pettype.setImageResource(R.drawable.main_cat_img);
                    }
                } else {
                    if (page == 1 && list.size() <= 0) {
                        petDiaryDiaryAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                    }
                }
            } catch (JSONException e) {
                if (page == 1 && list.size() <= 0) {
                    petDiaryDiaryAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                }
                e.printStackTrace();
            }
            mrlPetdiary.finishRefresh();
            mrlPetdiary.finishRefreshLoadMore();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mrlPetdiary.finishRefresh();
            mrlPetdiary.finishRefreshLoadMore();
            if (page == 1 && list.size() <= 0) {
                petDiaryDiaryAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
            }
        }
    };

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
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
