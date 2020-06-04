package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AvailableWorkerGridAdapter;
import com.haotang.pet.adapter.AvailableWorkerListAdapter;
import com.haotang.pet.adapter.UnAvailableWorkerGridAdapter;
import com.haotang.pet.adapter.UnAvailableWorkerListAdapter;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.ItemDetailWorkerAndTimeEvent;
import com.haotang.pet.entity.ItemListWorkerAndTimeEvent;
import com.haotang.pet.entity.ItemToMorePet;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.SwitchServiceWorkerAndTimeEvent;
import com.haotang.pet.entity.WorkerAndTimeEvent;
import com.haotang.pet.entity.WorkerDifference;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.haotang.pet.view.NoScollFullGridLayoutManager;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 美容师列表页
 */
public class WorkerListActivity extends SuperActivity {
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_workerlist_top_title)
    TextView tvWorkerlistTopTitle;
    @BindView(R.id.rv_workerlist_ky)
    RecyclerView rvWorkerlistKy;
    @BindView(R.id.rv_workerlist_bky)
    RecyclerView rvWorkerlistBky;
    @BindView(R.id.ll_workerlist_bky)
    LinearLayout llWorkerlistBky;
    @BindView(R.id.rl_workerlist_top_title)
    RelativeLayout rlWorkerlistTopTitle;
    private int shopId;
    private int serviceLoc;
    private String appointment;
    private String strp;
    private String workerIds;
    private int workerId;
    private double lat;
    private List<AppointWorker> availableWorkerList = new ArrayList<AppointWorker>();
    private List<AppointWorker> unAvailableWorkerList = new ArrayList<AppointWorker>();
    private List<WorkerDifference> differenceList = new ArrayList<WorkerDifference>();
    private double lng;
    private AvailableWorkerListAdapter availableWorkerListAdapter;
    private UnAvailableWorkerListAdapter unAvailableWorkerListAdapter;
    private String title;
    private ServiceShopAdd shop;
    private ItemToMorePet itemToMorePet;
    private int previous;
    private boolean flag;
    private UnAvailableWorkerGridAdapter unAvailableWorkerGridAdapter;
    private AvailableWorkerGridAdapter availableWorkerGridAdapter;
    private String defaultWorkerTag;
    private GridSpacingItemDecoration gridSpacingItemDecoration;
    private List<ApointMentPet> petList;
    private boolean isVip = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void initData() {
        act = this;
        MApplication.listAppoint.add(this);
        workerId = getIntent().getIntExtra("workerId", 0);
        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        appointment = getIntent().getStringExtra("appointment");
        strp = getIntent().getStringExtra("strp");
        defaultWorkerTag = getIntent().getStringExtra("defaultWorkerTag");
        previous = getIntent().getIntExtra("previous", 0);
        workerIds = getIntent().getStringExtra("workerIds");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        shop = (ServiceShopAdd) getIntent().getSerializableExtra("shop");
        isVip = getIntent().getBooleanExtra("isVip", true);
        petList = (List<ApointMentPet>) getIntent().getSerializableExtra("petList");
        itemToMorePet = (ItemToMorePet) getIntent().getSerializableExtra("itemToMorePet");
        if (shop != null) {
            shopId = shop.shopId;
        }
    }

    private void findView() {
        setContentView(R.layout.activity_worker_list);
        ButterKnife.bind(this);
    }

    private void setView() {
        gridSpacingItemDecoration = new GridSpacingItemDecoration(3, 0,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true);
        tvTitlebarTitle.setText("选择美容师");
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        ViewGroup.LayoutParams layoutParams = ibTitlebarOther.getLayoutParams();
        layoutParams.width = Math.round(50 * 2 / 3 * density);
        layoutParams.height = Math.round(50 * 2 / 3 * density);
        ibTitlebarOther.setLayoutParams(layoutParams);
        ibTitlebarOther.setVisibility(View.VISIBLE);
        flag = false;
        availableWorkerList.clear();
        unAvailableWorkerList.clear();
        setListToGrid();
    }

    private void setLinster() {
    }

    private void getData() {
        getWorkers();
    }

    // 加载美容师
    private void getWorkers() {
        unAvailableWorkerList.clear();
        availableWorkerList.clear();
        differenceList.clear();
        mPDialog.showDialog();
        CommUtil.querySelectedWorkers(this, appointment, serviceLoc,
                shopId, getStrp(getIntent().getIntExtra("tid", 0)), workerIds,
                lat, lng, getIntent().getIntExtra("serviceCardId", 0), querySelectedWorkersHandler);
    }

    private AsyncHttpResponseHandler querySelectedWorkersHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("difference") && !jdata.isNull("difference")) {
                                JSONObject jdifference = jdata.getJSONObject("difference");
                                if (jdifference.has("title") && !jdifference.isNull("title")) {
                                    title = jdifference.getString("title");
                                }
                                if (jdifference.has("content") && !jdifference.isNull("content")) {
                                    JSONArray jarrcontent = jdifference.getJSONArray("content");
                                    if (jarrcontent.length() > 0) {
                                        for (int i = 0; i < jarrcontent.length(); i++) {
                                            differenceList.add(WorkerDifference.json2Entity(jarrcontent
                                                    .getJSONObject(i)));
                                        }
                                    }
                                }
                            }
                            if (jdata.has("available") && !jdata.isNull("available")) {
                                JSONArray jarravailable = jdata.getJSONArray("available");
                                if (jarravailable.length() > 0) {
                                    for (int i = 0; i < jarravailable.length(); i++) {
                                        availableWorkerList.add(AppointWorker.json2Entity(jarravailable
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                            if (jdata.has("unavailable") && !jdata.isNull("unavailable")) {
                                JSONArray jarrunavailable = jdata.getJSONArray("unavailable");
                                if (jarrunavailable.length() > 0) {
                                    for (int i = 0; i < jarrunavailable.length(); i++) {
                                        unAvailableWorkerList.add(AppointWorker.json2Entity(jarrunavailable
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(WorkerListActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getWorkers()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(WorkerListActivity.this,
                        "数据异常");
                e.printStackTrace();
            }
            if (Utils.isStrNull(title)) {
                if (title.contains("@@")) {
                    String[] split = title.split("@@");
                    if (split != null && split.length > 0 && split.length % 2 != 0) {
                        SpannableString ss = new SpannableString(title.replace("@@", ""));
                        if (split.length == 3) {
                            int startIndex = split[0].length();
                            int endIndex = split[0].length() + split[1].length();
                            ss.setSpan(
                                    new ForegroundColorSpan(mContext.getResources().getColor(
                                            R.color.aD0021B)), startIndex, endIndex,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        } else if (split.length == 5) {
                            int startIndex = split[0].length();
                            int endIndex = split[0].length() + split[1].length();
                            int startIndex1 = split[0].length() + split[1].length() + split[2].length();
                            int endIndex1 = split[0].length() + split[1].length() + split[2].length() + split[3].length();
                            ss.setSpan(
                                    new ForegroundColorSpan(mContext.getResources().getColor(
                                            R.color.aD0021B)), startIndex, endIndex,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            ss.setSpan(
                                    new ForegroundColorSpan(mContext.getResources().getColor(
                                            R.color.aD0021B)), startIndex1, endIndex1,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        } else if (split.length == 7) {
                            int startIndex = split[0].length();
                            int endIndex = split[0].length() + split[1].length();
                            int startIndex1 = split[0].length() + split[1].length() + split[2].length();
                            int endIndex1 = split[0].length() + split[1].length() + split[2].length() + split[3].length();
                            int startIndex2 = split[0].length() + split[1].length() + split[2].length() + split[3].length() + split[4].length();
                            int endIndex2 = split[0].length() + split[1].length() + split[2].length() + split[3].length() + split[4].length() + split[5].length();
                            ss.setSpan(
                                    new ForegroundColorSpan(mContext.getResources().getColor(
                                            R.color.aD0021B)), startIndex, endIndex,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            ss.setSpan(
                                    new ForegroundColorSpan(mContext.getResources().getColor(
                                            R.color.aD0021B)), startIndex1, endIndex1,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            ss.setSpan(
                                    new ForegroundColorSpan(mContext.getResources().getColor(
                                            R.color.aD0021B)), startIndex2, endIndex2,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        tvWorkerlistTopTitle.setText(ss);
                    } else {
                        tvWorkerlistTopTitle.setText(title);
                    }
                } else {
                    tvWorkerlistTopTitle.setText(title);
                }
            }
            int num = 0;
            if (availableWorkerList.size() > 0) {
                AppointWorker localAppointWorker = null;
                for (int i = 0; i < availableWorkerList.size(); i++) {
                    AppointWorker appointWorker = availableWorkerList.get(i);
                    if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                        localAppointWorker = new AppointWorker();
                        localAppointWorker.setTid(appointWorker.getTid());
                        localAppointWorker.setWorkerId(appointWorker.getWorkerId());
                        localAppointWorker.setRealName(appointWorker.getRealName());
                        localAppointWorker.setTag(appointWorker.getTag());
                        localAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
                        localAppointWorker.setAvatar(appointWorker.getAvatar());
                        localAppointWorker.setEarliest(appointWorker.getEarliest());
                        localAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
                        localAppointWorker.setGoodRate(appointWorker.getGoodRate());
                        localAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
                        localAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
                        break;
                    }
                }
                if (localAppointWorker != null) {
                    availableWorkerList.add(1, localAppointWorker);
                }
                if (workerId > 0) {
                    AppointWorker firstAppointWorker = null;
                    for (int i = 0; i < availableWorkerList.size(); i++) {
                        AppointWorker appointWorker = availableWorkerList.get(i);
                        if (Utils.isStrNull(defaultWorkerTag)) {
                            Log.e("TAG", "defaultWorkerTag");
                            if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                                firstAppointWorker = new AppointWorker();
                                firstAppointWorker.setTid(appointWorker.getTid());
                                firstAppointWorker.setWorkerId(appointWorker.getWorkerId());
                                firstAppointWorker.setRealName(appointWorker.getRealName());
                                firstAppointWorker.setTag(appointWorker.getTag());
                                firstAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
                                firstAppointWorker.setAvatar(appointWorker.getAvatar());
                                firstAppointWorker.setEarliest(appointWorker.getEarliest());
                                firstAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
                                firstAppointWorker.setGoodRate(appointWorker.getGoodRate());
                                firstAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
                                firstAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
                                firstAppointWorker.setDefaultWorkerTag(appointWorker.getDefaultWorkerTag());
                                availableWorkerList.remove(i);
                                break;
                            }
                        } else {
                            Log.e("TAG", "无defaultWorkerTag");
                            if (appointWorker.getWorkerId() == workerId) {
                                firstAppointWorker = new AppointWorker();
                                firstAppointWorker.setTid(appointWorker.getTid());
                                firstAppointWorker.setWorkerId(appointWorker.getWorkerId());
                                firstAppointWorker.setRealName(appointWorker.getRealName());
                                firstAppointWorker.setTag(appointWorker.getTag());
                                firstAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
                                firstAppointWorker.setAvatar(appointWorker.getAvatar());
                                firstAppointWorker.setEarliest(appointWorker.getEarliest());
                                firstAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
                                firstAppointWorker.setGoodRate(appointWorker.getGoodRate());
                                firstAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
                                firstAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
                                firstAppointWorker.setDefaultWorkerTag(appointWorker.getDefaultWorkerTag());
                                availableWorkerList.remove(i);
                                break;
                            }
                        }
                    }
                    // 将选中的美容师排到第一位
                    LinkedList<AppointWorker> linkedList = new LinkedList<AppointWorker>();
                    linkedList.addAll(availableWorkerList);
                    if (firstAppointWorker != null) {
                        linkedList.addFirst(firstAppointWorker);
                    }
                    availableWorkerList.clear();
                    availableWorkerList.addAll(linkedList);
                    Log.e("TAG", "linkedList.size() = " + linkedList.size());
                    Log.e("TAG", "availableWorkerList.size() = " + availableWorkerList.size());
                    //再将系统推荐的美容师排到第一位
                    AppointWorker defaultTagAppointWorker = null;
                    for (int i = 0; i < availableWorkerList.size(); i++) {
                        AppointWorker appointWorker = availableWorkerList.get(i);
                        if (appointWorker != null) {
                            if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                                defaultTagAppointWorker = new AppointWorker();
                                defaultTagAppointWorker.setTid(appointWorker.getTid());
                                defaultTagAppointWorker.setWorkerId(appointWorker.getWorkerId());
                                defaultTagAppointWorker.setRealName(appointWorker.getRealName());
                                defaultTagAppointWorker.setTag(appointWorker.getTag());
                                defaultTagAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
                                defaultTagAppointWorker.setAvatar(appointWorker.getAvatar());
                                defaultTagAppointWorker.setEarliest(appointWorker.getEarliest());
                                defaultTagAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
                                defaultTagAppointWorker.setGoodRate(appointWorker.getGoodRate());
                                defaultTagAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
                                defaultTagAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
                                defaultTagAppointWorker.setDefaultWorkerTag(appointWorker.getDefaultWorkerTag());
                                availableWorkerList.remove(i);
                                break;
                            }
                        }
                    }
                    LinkedList<AppointWorker> linkedList1 = new LinkedList<AppointWorker>();
                    linkedList1.addAll(availableWorkerList);
                    if (defaultTagAppointWorker != null) {
                        linkedList1.addFirst(defaultTagAppointWorker);
                    }
                    availableWorkerList.clear();
                    availableWorkerList.addAll(linkedList1);
                    Log.e("TAG", "linkedList1.size() = " + linkedList1.size());
                    Log.e("TAG", "availableWorkerList.size() = " + availableWorkerList.size());
                } else {
                    //再将系统推荐的美容师排到第一位
                    AppointWorker defaultTagAppointWorker = null;
                    for (int i = 0; i < availableWorkerList.size(); i++) {
                        AppointWorker appointWorker = availableWorkerList.get(i);
                        if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                            defaultTagAppointWorker = new AppointWorker();
                            defaultTagAppointWorker.setTid(appointWorker.getTid());
                            defaultTagAppointWorker.setWorkerId(appointWorker.getWorkerId());
                            defaultTagAppointWorker.setRealName(appointWorker.getRealName());
                            defaultTagAppointWorker.setTag(appointWorker.getTag());
                            defaultTagAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
                            defaultTagAppointWorker.setAvatar(appointWorker.getAvatar());
                            defaultTagAppointWorker.setEarliest(appointWorker.getEarliest());
                            defaultTagAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
                            defaultTagAppointWorker.setGoodRate(appointWorker.getGoodRate());
                            defaultTagAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
                            defaultTagAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
                            defaultTagAppointWorker.setDefaultWorkerTag(appointWorker.getDefaultWorkerTag());
                            availableWorkerList.remove(i);
                            break;
                        }
                    }
                    LinkedList<AppointWorker> linkedList1 = new LinkedList<AppointWorker>();
                    linkedList1.addAll(availableWorkerList);
                    if (defaultTagAppointWorker != null) {
                        linkedList1.addFirst(defaultTagAppointWorker);
                    }
                    availableWorkerList.clear();
                    availableWorkerList.addAll(linkedList1);
                    Log.e("TAG", "linkedList1.size() = " + linkedList1.size());
                    Log.e("TAG", "availableWorkerList.size() = " + availableWorkerList.size());
                }
                num += availableWorkerList.size();
                if (flag) {//list
                    availableWorkerListAdapter.notifyDataSetChanged();
                } else {
                    availableWorkerGridAdapter.notifyDataSetChanged();
                }
            }
            if (unAvailableWorkerList.size() > 0) {
                num += unAvailableWorkerList.size();
                if (flag) {//list
                    unAvailableWorkerListAdapter.notifyDataSetChanged();
                } else {
                    unAvailableWorkerGridAdapter.notifyDataSetChanged();
                }
                llWorkerlistBky.setVisibility(View.VISIBLE);
            } else {
                llWorkerlistBky.setVisibility(View.GONE);
            }
            tvTitlebarTitle.setText("选择美容师(" + num + ")");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(WorkerListActivity.this, "请求失败");
            mPDialog.closeDialog();
        }
    };

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

    private void setListToGrid() {
        if (flag) {//list
            ibTitlebarOther.setBackgroundResource(R.drawable.icon_workerlist_grid);
            rvWorkerlistKy.setHasFixedSize(true);
            rvWorkerlistKy.setNestedScrollingEnabled(false);
            NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
            noScollFullLinearLayoutManager.setScrollEnabled(false);
            rvWorkerlistKy.setLayoutManager(noScollFullLinearLayoutManager);
            rvWorkerlistKy.invalidateItemDecorations();
            rvWorkerlistKy.removeItemDecoration(gridSpacingItemDecoration);
            availableWorkerListAdapter = new AvailableWorkerListAdapter(R.layout.item_workerlist_list, availableWorkerList, appointment, workerId, defaultWorkerTag);
            rvWorkerlistKy.setAdapter(availableWorkerListAdapter);

            rvWorkerlistBky.setHasFixedSize(true);
            rvWorkerlistBky.setNestedScrollingEnabled(false);
            NoScollFullLinearLayoutManager noScollFullLinearLayoutManager1 = new NoScollFullLinearLayoutManager(this);
            noScollFullLinearLayoutManager1.setScrollEnabled(false);
            rvWorkerlistBky.setLayoutManager(noScollFullLinearLayoutManager1);
            rvWorkerlistBky.invalidateItemDecorations();
            rvWorkerlistBky.removeItemDecoration(gridSpacingItemDecoration);
            unAvailableWorkerListAdapter = new UnAvailableWorkerListAdapter(R.layout.item_workerlist_list, unAvailableWorkerList);
            rvWorkerlistBky.setAdapter(unAvailableWorkerListAdapter);
            availableWorkerListAdapter.setOnWorkerInfoListener(new AvailableWorkerListAdapter.OnWorkerInfoListener() {
                @Override
                public void OnWorkerInfo(int position) {
                    if (availableWorkerList.size() > position) {
                        AppointWorker appointWorker = availableWorkerList.get(position);
                        if (!Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                            if (BeauticianDetailActivity.act != null) {
                                BeauticianDetailActivity.act.finish();
                            }
                            Intent intent1 = new Intent(WorkerListActivity.this, BeauticianDetailActivity.class);
                            intent1.putExtra("beautician_id", appointWorker.getWorkerId());
                            intent1.putExtra("apptime", appointment);
                            intent1.putExtra("previous", Global.AVAILABLEWORKERLIST_TO_WORKERDETAIL);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shop);
                            bundle.putSerializable("itemToMorePet", itemToMorePet);
                            bundle.putSerializable("appointWorker", appointWorker);
                            intent1.putExtras(bundle);
                            intent1.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                            intent1.putExtra("strp", strp);
                            intent1.putExtra("petList", (Serializable) petList);
                            intent1.putExtra("isVip", isVip);
                            intent1.putExtra("appointment", appointment);
                            intent1.putExtra("workerIds", workerIds);
                            intent1.putExtra("defaultWorkerTag", defaultWorkerTag);
                            intent1.putExtra("workerId", workerId);
                            intent1.putExtra("add_lat", lat);
                            intent1.putExtra("add_lng", lng);
                            intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                            intent1.putExtra("serviceLoc", serviceLoc);
                            startActivity(intent1);
                        }
                    }
                }
            });
            unAvailableWorkerListAdapter.setOnWorkerInfoListener(new UnAvailableWorkerListAdapter.OnWorkerInfoListener() {
                @Override
                public void OnWorkerInfo(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        if (BeauticianDetailActivity.act != null) {
                            BeauticianDetailActivity.act.finish();
                        }
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        Intent intent1 = new Intent(WorkerListActivity.this, BeauticianDetailActivity.class);
                        intent1.putExtra("beautician_id", appointWorker.getWorkerId());
                        intent1.putExtra("apptime", appointment);
                        intent1.putExtra("previous", Global.UNAVAILABLEWORKERLIST_TO_WORKERDETAIL);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("shop", shop);
                        bundle.putSerializable("itemToMorePet", itemToMorePet);
                        bundle.putSerializable("appointWorker", appointWorker);
                        intent1.putExtras(bundle);
                        intent1.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                        intent1.putExtra("defaultWorkerTag", defaultWorkerTag);
                        intent1.putExtra("add_lat", lat);
                        intent1.putExtra("workerIds", workerIds);
                        intent1.putExtra("workerId", workerId);
                        intent1.putExtra("add_lng", lng);
                        intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                        intent1.putExtra("pickup", getIntent().getIntExtra("pickup", 0));
                        intent1.putExtra("serviceLoc", serviceLoc);
                        intent1.putExtra("strp", strp);
                        intent1.putExtra("petList", (Serializable) petList);
                        intent1.putExtra("isVip", isVip);
                        startActivity(intent1);
                    }
                }
            });
            unAvailableWorkerListAdapter.setOnWorkerSelectListener(new UnAvailableWorkerListAdapter.OnWorkerSelectListener() {
                @Override
                public void OnWorkerSelect(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        if (AppointTimeActivity.act != null) {
                            AppointTimeActivity.act.finish();
                        }
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        Intent intent1 = new Intent(WorkerListActivity.this, AppointTimeActivity.class);//去选择这个美容师的时间
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("shop", shop);
                        bundle.putSerializable("itemToMorePet", itemToMorePet);
                        bundle.putSerializable("appointWorker", appointWorker);
                        intent1.putExtras(bundle);
                        intent1.putExtra("lat", lat);
                        intent1.putExtra("previous", previous);
                        intent1.putExtra("lng", lng);
                        intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                        intent1.putExtra("pickup", getIntent().getIntExtra("pickup", 0));
                        intent1.putExtra("serviceLoc", serviceLoc);
                        intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds(appointWorker.getTid()));
                        startActivity(intent1);
                    }
                }
            });
            availableWorkerListAdapter.setOnWorkerSelectListener(new AvailableWorkerListAdapter.OnWorkerSelectListener() {
                @Override
                public void OnWorkerSelect(int position) {
                    if (availableWorkerList.size() > position) {
                        if (AppointTimeActivity.act != null) {
                            AppointTimeActivity.act.finish();
                        }
                        AppointWorker appointWorker = availableWorkerList.get(position);
                        if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                            Global.ServerEvent(WorkerListActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_default_worker);
                        }
                        if (Utils.isStrNull(appointment)) {
                            if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                                if (BeauticianDetailActivity.act != null) {
                                    BeauticianDetailActivity.act.finish();
                                }
                                if (previous == Global.ITEMLIST_TO_OVERTIME) {
                                    EventBus.getDefault().post(new ItemListWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                } else if (previous == Global.ITEMDETAIL_TO_OVERTIME) {
                                    EventBus.getDefault().post(new ItemDetailWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                } else if (previous == Global.SWITCHSERVICE_TO_OVERTIME) {
                                    EventBus.getDefault().post(new SwitchServiceWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                } else {
                                    EventBus.getDefault().post(new WorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                }
                                finish();
                            } else {
                                if (workerId == appointWorker.getWorkerId()) {
                                    Intent intent1 = new Intent(WorkerListActivity.this, AppointTimeActivity.class);//去选择这个美容师的时间
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("shop", shop);
                                    bundle.putSerializable("appointWorker", appointWorker);
                                    bundle.putSerializable("itemToMorePet", itemToMorePet);
                                    intent1.putExtras(bundle);
                                    intent1.putExtra("lat", lat);
                                    intent1.putExtra("previous", previous);
                                    intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                                    intent1.putExtra("lng", lng);
                                    intent1.putExtra("serviceLoc", serviceLoc);
                                    intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds(appointWorker.getTid()));
                                    startActivity(intent1);
                                } else {
                                    if (BeauticianDetailActivity.act != null) {
                                        BeauticianDetailActivity.act.finish();
                                    }
                                    if (previous == Global.ITEMLIST_TO_OVERTIME) {
                                        EventBus.getDefault().post(new ItemListWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    } else if (previous == Global.ITEMDETAIL_TO_OVERTIME) {
                                        EventBus.getDefault().post(new ItemDetailWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    } else if (previous == Global.SWITCHSERVICE_TO_OVERTIME) {
                                        EventBus.getDefault().post(new SwitchServiceWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    } else {
                                        EventBus.getDefault().post(new WorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    }
                                    finish();
                                }
                            }
                        } else {
                            Intent intent1 = new Intent(WorkerListActivity.this, AppointTimeActivity.class);//去选择这个美容师的时间
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shop);
                            bundle.putSerializable("itemToMorePet", itemToMorePet);
                            bundle.putSerializable("appointWorker", appointWorker);
                            intent1.putExtras(bundle);
                            intent1.putExtra("lat", lat);
                            intent1.putExtra("previous", previous);
                            intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                            intent1.putExtra("lng", lng);
                            intent1.putExtra("serviceLoc", serviceLoc);
                            intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds(appointWorker.getTid()));
                            startActivity(intent1);
                        }
                    }
                }
            });
        } else {//gird
            ibTitlebarOther.setBackgroundResource(R.drawable.icon_workerlist_list);
            rvWorkerlistKy.setHasFixedSize(true);
            rvWorkerlistKy.setNestedScrollingEnabled(false);
            NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                    NoScollFullGridLayoutManager(rvWorkerlistKy, mContext, 3, GridLayoutManager.VERTICAL, false);
            noScollFullGridLayoutManager.setScrollEnabled(false);
            rvWorkerlistKy.setLayoutManager(noScollFullGridLayoutManager);
            rvWorkerlistKy.addItemDecoration(gridSpacingItemDecoration);
            availableWorkerGridAdapter = new AvailableWorkerGridAdapter(R.layout.item_workerlist_gird, availableWorkerList, appointment, workerId, defaultWorkerTag);
            rvWorkerlistKy.setAdapter(availableWorkerGridAdapter);

            rvWorkerlistBky.setHasFixedSize(true);
            rvWorkerlistBky.setNestedScrollingEnabled(false);
            NoScollFullGridLayoutManager noScollFullGridLayoutManager1 = new
                    NoScollFullGridLayoutManager(rvWorkerlistBky, mContext, 3, GridLayoutManager.VERTICAL, false);
            noScollFullGridLayoutManager1.setScrollEnabled(false);
            rvWorkerlistBky.setLayoutManager(noScollFullGridLayoutManager1);
            rvWorkerlistBky.addItemDecoration(gridSpacingItemDecoration);
            unAvailableWorkerGridAdapter = new UnAvailableWorkerGridAdapter(R.layout.item_workerlist_gird, unAvailableWorkerList);
            rvWorkerlistBky.setAdapter(unAvailableWorkerGridAdapter);
            availableWorkerGridAdapter.setOnWorkerInfoListener(new AvailableWorkerGridAdapter.OnWorkerInfoListener() {
                @Override
                public void OnWorkerInfo(int position) {
                    if (availableWorkerList.size() > position) {
                        AppointWorker appointWorker = availableWorkerList.get(position);
                        if (!Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                            if (BeauticianDetailActivity.act != null) {
                                BeauticianDetailActivity.act.finish();
                            }
                            Intent intent1 = new Intent(WorkerListActivity.this, BeauticianDetailActivity.class);
                            intent1.putExtra("beautician_id", appointWorker.getWorkerId());
                            intent1.putExtra("apptime", appointment);
                            intent1.putExtra("previous", Global.AVAILABLEWORKERLIST_TO_WORKERDETAIL);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shop);
                            bundle.putSerializable("itemToMorePet", itemToMorePet);
                            bundle.putSerializable("appointWorker", appointWorker);
                            intent1.putExtras(bundle);
                            intent1.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                            intent1.putExtra("defaultWorkerTag", defaultWorkerTag);
                            intent1.putExtra("appointment", appointment);
                            intent1.putExtra("workerIds", workerIds);
                            intent1.putExtra("workerId", workerId);
                            intent1.putExtra("add_lat", lat);
                            intent1.putExtra("add_lng", lng);
                            intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                            intent1.putExtra("pickup", getIntent().getIntExtra("pickup", 0));
                            intent1.putExtra("serviceLoc", serviceLoc);
                            intent1.putExtra("strp", strp);
                            intent1.putExtra("petList", (Serializable) petList);
                            intent1.putExtra("isVip", isVip);
                            startActivity(intent1);
                        }
                    }
                }
            });
            unAvailableWorkerGridAdapter.setOnWorkerInfoListener(new UnAvailableWorkerGridAdapter.OnWorkerInfoListener() {
                @Override
                public void OnWorkerInfo(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        if (BeauticianDetailActivity.act != null) {
                            BeauticianDetailActivity.act.finish();
                        }
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        Intent intent1 = new Intent(WorkerListActivity.this, BeauticianDetailActivity.class);
                        intent1.putExtra("beautician_id", appointWorker.getWorkerId());
                        intent1.putExtra("apptime", appointment);
                        intent1.putExtra("previous", Global.UNAVAILABLEWORKERLIST_TO_WORKERDETAIL);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("shop", shop);
                        bundle.putSerializable("itemToMorePet", itemToMorePet);
                        bundle.putSerializable("appointWorker", appointWorker);
                        intent1.putExtras(bundle);
                        intent1.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                        intent1.putExtra("defaultWorkerTag", defaultWorkerTag);
                        intent1.putExtra("workerIds", workerIds);
                        intent1.putExtra("workerId", workerId);
                        intent1.putExtra("add_lat", lat);
                        intent1.putExtra("add_lng", lng);
                        intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                        intent1.putExtra("pickup", getIntent().getIntExtra("pickup", 0));
                        intent1.putExtra("serviceLoc", serviceLoc);
                        intent1.putExtra("strp", strp);
                        intent1.putExtra("petList", (Serializable) petList);
                        intent1.putExtra("isVip", isVip);
                        startActivity(intent1);
                    }
                }
            });
            unAvailableWorkerGridAdapter.setOnWorkerSelectListener(new UnAvailableWorkerGridAdapter.OnWorkerSelectListener() {
                @Override
                public void OnWorkerSelect(int position) {
                    if (unAvailableWorkerList.size() > position) {
                        if (AppointTimeActivity.act != null) {
                            AppointTimeActivity.act.finish();
                        }
                        AppointWorker appointWorker = unAvailableWorkerList.get(position);
                        Intent intent1 = new Intent(WorkerListActivity.this, AppointTimeActivity.class);//去选择这个美容师的时间
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("shop", shop);
                        bundle.putSerializable("itemToMorePet", itemToMorePet);
                        bundle.putSerializable("appointWorker", appointWorker);
                        intent1.putExtras(bundle);
                        intent1.putExtra("lat", lat);
                        intent1.putExtra("previous", previous);
                        intent1.putExtra("lng", lng);
                        intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                        intent1.putExtra("pickup", getIntent().getIntExtra("pickup", 0));
                        intent1.putExtra("serviceLoc", serviceLoc);
                        intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds(appointWorker.getTid()));
                        startActivity(intent1);
                    }
                }
            });
            availableWorkerGridAdapter.setOnWorkerSelectListener(new AvailableWorkerGridAdapter.OnWorkerSelectListener() {
                @Override
                public void OnWorkerSelect(int position) {
                    if (availableWorkerList.size() > position) {
                        if (AppointTimeActivity.act != null) {
                            AppointTimeActivity.act.finish();
                        }
                        AppointWorker appointWorker = availableWorkerList.get(position);
                        if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                            Global.ServerEvent(WorkerListActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_default_worker);
                        }
                        if (Utils.isStrNull(appointment)) {
                            if (Utils.isStrNull(appointWorker.getDefaultWorkerTag())) {
                                if (BeauticianDetailActivity.act != null) {
                                    BeauticianDetailActivity.act.finish();
                                }
                                if (previous == Global.ITEMLIST_TO_OVERTIME) {
                                    EventBus.getDefault().post(new ItemListWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                } else if (previous == Global.ITEMDETAIL_TO_OVERTIME) {
                                    EventBus.getDefault().post(new ItemDetailWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                } else if (previous == Global.SWITCHSERVICE_TO_OVERTIME) {
                                    EventBus.getDefault().post(new SwitchServiceWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                } else {
                                    EventBus.getDefault().post(new WorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                }
                                finish();
                            } else {
                                if (workerId == appointWorker.getWorkerId()) {
                                    Intent intent1 = new Intent(WorkerListActivity.this, AppointTimeActivity.class);//去选择这个美容师的时间
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("shop", shop);
                                    bundle.putSerializable("appointWorker", appointWorker);
                                    bundle.putSerializable("itemToMorePet", itemToMorePet);
                                    intent1.putExtras(bundle);
                                    intent1.putExtra("lat", lat);
                                    intent1.putExtra("previous", previous);
                                    intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                                    intent1.putExtra("lng", lng);
                                    intent1.putExtra("serviceLoc", serviceLoc);
                                    intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds(appointWorker.getTid()));
                                    startActivity(intent1);
                                } else {
                                    if (BeauticianDetailActivity.act != null) {
                                        BeauticianDetailActivity.act.finish();
                                    }
                                    if (previous == Global.ITEMLIST_TO_OVERTIME) {
                                        EventBus.getDefault().post(new ItemListWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    } else if (previous == Global.ITEMDETAIL_TO_OVERTIME) {
                                        EventBus.getDefault().post(new ItemDetailWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    } else if (previous == Global.SWITCHSERVICE_TO_OVERTIME) {
                                        EventBus.getDefault().post(new SwitchServiceWorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    } else {
                                        EventBus.getDefault().post(new WorkerAndTimeEvent(appointWorker, appointment, getIntent().getIntExtra("dayposition", 0), getIntent().getIntExtra("pickup", 0), itemToMorePet, workerIds));
                                    }
                                    finish();
                                }
                            }
                        } else {
                            Intent intent1 = new Intent(WorkerListActivity.this, AppointTimeActivity.class);//去选择这个美容师的时间
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shop);
                            bundle.putSerializable("itemToMorePet", itemToMorePet);
                            bundle.putSerializable("appointWorker", appointWorker);
                            intent1.putExtras(bundle);
                            intent1.putExtra("lat", lat);
                            intent1.putExtra("previous", previous);
                            intent1.putExtra("dayposition", getIntent().getIntExtra("dayposition", 0));
                            intent1.putExtra("lng", lng);
                            intent1.putExtra("serviceLoc", serviceLoc);
                            intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds(appointWorker.getTid()));
                            startActivity(intent1);
                        }
                    }
                }
            });
        }
    }

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.iv_workerlist_close,
            R.id.tv_workerlist_top_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_other:
                flag = !flag;
                setListToGrid();
                break;
            case R.id.iv_workerlist_close:
                rlWorkerlistTopTitle.setVisibility(View.GONE);
                break;
            case R.id.tv_workerlist_top_title:
                Intent intent = new Intent(WorkerListActivity.this, WorkerDifferenceActivity.class);
                intent.putExtra("differenceList", (Serializable) differenceList);
                startActivity(intent);
                break;
        }
    }

    private String getStrp(int tid) {
        if (Utils.isStrNull(strp)) {
            return strp;
        } else {
            return getPetID_ServiceId_MyPetId_ItemIds(tid);
        }
    }

    private String getPetID_ServiceId_MyPetId_ItemIds(int tid) {
        StringBuffer sb = new StringBuffer();
        if (petList != null && petList.size() > 0) {
            if (itemToMorePet != null) {
                Log.e("TAG", "itemToMorePet = " + itemToMorePet.toString());
                List<Integer> myPetIds = itemToMorePet.getMyPetIds();
                List<ApointMentItem> apointMentItem = itemToMorePet.getApointMentItem();
                if (myPetIds != null && myPetIds.size() > 0) {
                    for (int i = 0; i < myPetIds.size(); i++) {
                        Integer integer = myPetIds.get(i);
                        for (int j = 0; j < petList.size(); j++) {
                            ApointMentPet apointMentPet = petList.get(j);
                            List<ApointMentItem> itemList = apointMentPet.getItemList();
                            if (apointMentPet.getMyPetId() == integer) {
                                if (itemList == null) {
                                    itemList = new ArrayList<ApointMentItem>();
                                }
                                itemList.addAll(apointMentItem);
                            }
                            apointMentPet.setItemList(itemList);
                        }
                    }
                }
            }
            for (int i = 0; i < petList.size(); i++) {
                ApointMentPet apointMentPet = petList.get(i);
                if (apointMentPet != null) {
                    if (i < petList.size() - 1) {
                        sb.append(apointMentPet.getPetId());
                        sb.append("_");
                        sb.append(apointMentPet.getServiceId());
                        sb.append("_");
                        sb.append(apointMentPet.getMyPetId());
                        sb.append("_");
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            sb.append(getItemIds(itemList, tid));
                        } else {
                            sb.append("0");
                        }
                        sb.append("_");
                    } else {
                        sb.append(apointMentPet.getPetId());
                        sb.append("_");
                        sb.append(apointMentPet.getServiceId());
                        sb.append("_");
                        sb.append(apointMentPet.getMyPetId());
                        sb.append("_");
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            sb.append(getItemIds(itemList, tid));
                        } else {
                            sb.append("0");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private String getItemIds(List<ApointMentItem> itemList, int tid) {
        StringBuffer sb = new StringBuffer();
        if (itemList != null && itemList.size() > 0) {
            List<ApointMentItem> itemList1 = new ArrayList<ApointMentItem>();
            itemList1.clear();
            for (int i = 0; i < itemList.size(); i++) {
                ApointMentItem apointMentItem = itemList.get(i);
                if (isVip) {
                    if (tid == 1 && !apointMentItem.isShouXi()) {
                        itemList1.add(apointMentItem);
                    } else if (tid == 2 && !apointMentItem.isShouXi()) {
                        itemList1.add(apointMentItem);
                    } else if (tid == 3 && !apointMentItem.isShouXi()) {
                        itemList1.add(apointMentItem);
                    }
                } else {
                    if (tid == 1 && !apointMentItem.isShouXi()) {
                        itemList1.add(apointMentItem);
                    } else if (tid == 2 && !apointMentItem.isShouXi()) {
                        itemList1.add(apointMentItem);
                    } else if (tid == 3 && !apointMentItem.isShouXi()) {
                        itemList1.add(apointMentItem);
                    }
                }
            }
            if (itemList1 != null && itemList1.size() > 0) {
                for (int i = 0; i < itemList1.size(); i++) {
                    ApointMentItem apointMentItem = itemList1.get(i);
                    if (apointMentItem != null) {
                        if (i < itemList1.size() - 1) {
                            sb.append(apointMentItem.getId());
                            sb.append(",");
                        } else {
                            sb.append(apointMentItem.getId());
                        }
                    }
                }
            } else {
                sb.append("0");
            }
        } else {
            sb.append("0");
        }
        return sb.toString();
    }
}
