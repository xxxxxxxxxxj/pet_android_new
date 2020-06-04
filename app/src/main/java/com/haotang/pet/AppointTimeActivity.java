package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AppointMentTimeTimeAdapter;
import com.haotang.pet.adapter.TopDateAdapter;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.AppointMentDate;
import com.haotang.pet.entity.AppointMentTime;
import com.haotang.pet.entity.AppointMentZhengDianTime;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.AppointZDTimeEvent;
import com.haotang.pet.entity.AppointmentTimeEvent;
import com.haotang.pet.entity.ItemDetailWorkerAndTimeEvent;
import com.haotang.pet.entity.ItemListWorkerAndTimeEvent;
import com.haotang.pet.entity.ItemToMorePet;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.SwitchServiceWorkerAndTimeEvent;
import com.haotang.pet.entity.WorkerAndTimeEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约时间界面
 */
public class AppointTimeActivity extends SuperActivity {
    private static final int DATA_COUNT = 4;
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_appointtime_worker_qitatime)
    TextView tvAppointtimeWorkerQitatime;
    @BindView(R.id.tv_appointtime_desc)
    TextView tvAppointtimeDesc;
    @BindView(R.id.iv_appointtime_worker_tag)
    ImageView ivAppointtimeWorkerTag;
    @BindView(R.id.iv_appointtime_worker_img)
    ImageView ivAppointtimeWorkerImg;
    @BindView(R.id.tv_appointtime_workername)
    TextView tvAppointtimeWorkername;
    @BindView(R.id.tv_appointtime_worker_hpl)
    TextView tvAppointtimeWorkerHpl;
    @BindView(R.id.tv_appointtime_worker_num)
    TextView tvAppointtimeWorkerNum;
    @BindView(R.id.tfl_appointtime_worker_listbq)
    TagFlowLayout tflAppointtimeWorkerListbq;
    @BindView(R.id.ll_appointtime_worker)
    LinearLayout llAppointtimeWorker;
    @BindView(R.id.tv_appointtime_shop_tag)
    TextView tvAppointtimeShopTag;
    @BindView(R.id.iv_appointtime_shop_img)
    ImageView ivAppointtimeShopImg;
    @BindView(R.id.tv_appointtime_shopname)
    TextView tvAppointtimeShopname;
    @BindView(R.id.iv_appointtime_shopphone)
    ImageView ivAppointtimeShopphone;
    @BindView(R.id.tv_appointtime_shopyysj)
    TextView tvAppointtimeShopyysj;
    @BindView(R.id.tv_appointtime_shopaddress)
    TextView tvAppointtimeShopaddress;
    @BindView(R.id.tv_appointtime_shopdesc)
    TextView tvAppointtimeShopdesc;
    @BindView(R.id.ll_appointtime_shop)
    LinearLayout llAppointtimeShop;
    @BindView(R.id.rv_appointtime_topriqi)
    RecyclerView rvAppointtimeTopriqi;
    @BindView(R.id.tv_appointtime_tishi)
    TextView tvAppointtimeTishi;
    @BindView(R.id.ll_appointtime_top)
    LinearLayout llAppointtimeTop;
    @BindView(R.id.tv_appointtime_submit)
    TextView tvAppointtimeSubmit;
    @BindView(R.id.tv_appointtime_selecttime)
    TextView tvAppointtimeSelecttime;
    @BindView(R.id.rl_appointtime_bottom)
    RelativeLayout rlAppointtimeBottom;
    @BindView(R.id.rv_appointtime_time)
    RecyclerView rvAppointtimeTime;
    @BindView(R.id.tv_appointtime_discount)
    TextView tvAppointtimeDiscount;
    private ServiceShopAdd shop;
    private double lat;
    private double lng;
    private int serviceLoc;
    private String strp;
    private AppointWorker appointWorker;
    private int shopId;
    private int workerId;
    private String shopPhone;
    private String appointment;
    private String workerIds;
    private List<AppointMentDate> topDateList = new ArrayList<AppointMentDate>();
    private TopDateAdapter topDateAdapter;
    private TimerTask task;
    private String pickupTip;
    private String isFullTip;
    private Timer timer;
    private int dayposition = -1;
    private List<AppointMentDate> timeList = new ArrayList<AppointMentDate>();
    private List<AppointMentZhengDianTime> zhengDianTimeList = new ArrayList<AppointMentZhengDianTime>();
    private List<Integer> wholeTimeList = new ArrayList<Integer>();
    private AppointMentTimeTimeAdapter appointMentTimeTimeAdapter;
    private List<AppointMentTime> localTimes;
    private int selectYear;
    private String selectDate;
    private int pickup;
    private ItemToMorePet itemToMorePet;
    private int previous;
    private String workdayDiscountTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(this);
        Intent intent = getIntent();
        dayposition = intent.getIntExtra("dayposition", -1);
        previous = getIntent().getIntExtra("previous", 0);
        appointWorker = (AppointWorker) intent.getSerializableExtra("appointWorker");
        shop = (ServiceShopAdd) intent.getSerializableExtra("shop");
        itemToMorePet = (ItemToMorePet) intent.getSerializableExtra("itemToMorePet");
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        serviceLoc = intent.getIntExtra("serviceLoc", 0);
        strp = intent.getStringExtra("strp");
        if (shop != null) {
            Log.e("TAG", "shop = " + shop.toString());
            shopId = shop.shopId;
            shopPhone = shop.shopPhone;
        }
        if (appointWorker != null) {
            workerId = appointWorker.getWorkerId();
        }
    }

    private void findView() {
        setContentView(R.layout.activity_appoint_time);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("预约时间");
        rvAppointtimeTopriqi.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAppointtimeTopriqi.setLayoutManager(linearLayoutManager);
        topDateList.clear();
        topDateAdapter = new TopDateAdapter(R.layout.item_appointmenttime_date, topDateList);
        rvAppointtimeTopriqi.setAdapter(topDateAdapter);

        rvAppointtimeTime.setHasFixedSize(true);
        rvAppointtimeTime.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvAppointtimeTime.setLayoutManager(noScollFullLinearLayoutManager);
        //添加自定义分割线
        rvAppointtimeTime.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 10),
                ContextCompat.getColor(this, R.color.af8f8f8)));
        zhengDianTimeList.clear();
        appointMentTimeTimeAdapter = new AppointMentTimeTimeAdapter(R.layout.item_appointmenttime_time, zhengDianTimeList);
        rvAppointtimeTime.setAdapter(appointMentTimeTimeAdapter);
        if (appointWorker != null) {//当前美容师的时间
            setShopOrWorker(true);
        } else {//店铺的时间
            setShopOrWorker(false);
        }
        boolean APPOINT_TIME_DISCOUNT_DIALOG = spUtil.getBoolean("APPOINT_TIME_DISCOUNT_DIALOG", false);
        if (!APPOINT_TIME_DISCOUNT_DIALOG) {
            spUtil.saveBoolean("APPOINT_TIME_DISCOUNT_DIALOG", true);
            String str = "宠物家E卡全新改版升级啦\n" +
                    "预约@@周一至周五@@的时间可享@@更低折扣@@";
            String[] split = str.split("@@");
            SpannableString ss = new SpannableString(str.replace("@@", ""));
            int startIndex = split[0].length();
            int endIndex = startIndex + split[1].length();
            int startIndex1 = endIndex + split[2].length();
            int endIndex1 = startIndex1 + split[3].length();
            ss.setSpan(
                    new ForegroundColorSpan(mContext.getResources().getColor(
                            R.color.aD0021B)), startIndex, endIndex,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ss.setSpan(
                    new ForegroundColorSpan(mContext.getResources().getColor(
                            R.color.aD0021B)), startIndex1, endIndex1,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            Utils.setCardDesc(mContext, "好消息", ss, "我知道了", Gravity.CENTER_HORIZONTAL, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void setShopOrWorker(boolean flag) {
        if (flag) {
            llAppointtimeWorker.setVisibility(View.VISIBLE);
            llAppointtimeShop.setVisibility(View.GONE);
            if (Utils.isStrNull(appointWorker.getTag())) {
                ivAppointtimeWorkerTag.setVisibility(View.VISIBLE);
                ivAppointtimeWorkerTag.bringToFront();
                GlideUtil.loadImg(this, appointWorker.getTag(), ivAppointtimeWorkerTag, R.drawable.icon_production_default);
            } else {
                ivAppointtimeWorkerTag.setVisibility(View.GONE);
            }
            GlideUtil.loadCircleImg(AppointTimeActivity.this, appointWorker.getAvatar(), ivAppointtimeWorkerImg, R.drawable.user_icon_unnet_circle);
            if (appointWorker.getExpertiseLiest() != null && appointWorker.getExpertiseLiest().size() > 0) {
                tflAppointtimeWorkerListbq.setVisibility(View.VISIBLE);
                tflAppointtimeWorkerListbq.setAdapter(new TagAdapter<String>(appointWorker.getExpertiseLiest()) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        View view = (View) View.inflate(mContext, R.layout.item_workerlist_bq,
                                null);
                        TextView tv_item_workerlist_bq = (TextView) view.findViewById(R.id.tv_item_workerlist_bq);
                        tv_item_workerlist_bq.setText(s);
                        return view;
                    }
                });
            } else {
                tflAppointtimeWorkerListbq.setVisibility(View.GONE);
            }
            Utils.setText(tvAppointtimeWorkername, appointWorker.getRealName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvAppointtimeWorkerHpl, "好评率：" + appointWorker.getGoodRate(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvAppointtimeWorkerNum, "服务" + appointWorker.getOrderTotal() + "单", "", View.VISIBLE, View.VISIBLE);
            mPDialog.showDialog();
            topDateList.clear();
            pickupTip = null;
            isFullTip = null;
            workdayDiscountTip = null;
            CommUtil.getBeauticianFreeTime(AppointTimeActivity.this, lat, lng,
                    spUtil.getString("cellphone", ""), Global.getIMEI(AppointTimeActivity.this),
                    Global.getCurrentVersion(AppointTimeActivity.this), 0, serviceLoc,
                    shopId, strp, 0, null, 0, workerId, timeHanler);
        } else {
            llAppointtimeWorker.setVisibility(View.GONE);
            llAppointtimeShop.setVisibility(View.VISIBLE);
            if (Utils.isStrNull(shop.tag)) {
                tvAppointtimeShopTag.setVisibility(View.VISIBLE);
                tvAppointtimeShopTag.bringToFront();
                tvAppointtimeShopTag.setText(shop.tag);
            } else {
                tvAppointtimeShopTag.setVisibility(View.GONE);
            }
            GlideUtil.loadRoundImg(this, shop.shopImg,
                    ivAppointtimeShopImg,
                    R.drawable.icon_production_default, 8);
            Utils.setText(tvAppointtimeShopname,
                    shop.shopName, "", View.VISIBLE, View.INVISIBLE);
            Utils.setText(tvAppointtimeShopyysj, "营业时间: " + shop.openTime, "", View.VISIBLE, View.INVISIBLE);
            Utils.setText(tvAppointtimeShopaddress,
                    "地址：" + shop.shopAddress, "", View.VISIBLE, View.INVISIBLE);
            Utils.setText(tvAppointtimeShopdesc, "以下时间为" + shop.shopName + "可预约时间", "", View.VISIBLE, View.GONE);
            Utils.setText(tvAppointtimeShopyysj, "营业时间: " + shop.openTime, "", View.VISIBLE, View.INVISIBLE);
            mPDialog.showDialog();
            topDateList.clear();
            pickupTip = null;
            isFullTip = null;
            workdayDiscountTip = null;
            CommUtil.getBeauticianFreeTime(AppointTimeActivity.this, lat, lng,
                    spUtil.getString("cellphone", ""), Global.getIMEI(AppointTimeActivity.this),
                    Global.getCurrentVersion(AppointTimeActivity.this), 0, serviceLoc,
                    shopId, strp, 0, null, 0, 0, timeHanler);
        }
    }

    private AsyncHttpResponseHandler timeHanler = new AsyncHttpResponseHandler() {
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
                            if (jdata.has("workdayDiscountTip") && !jdata.isNull("workdayDiscountTip")) {
                                workdayDiscountTip = jdata.getString("workdayDiscountTip");
                            }
                            if (jdata.has("pickupTip") && !jdata.isNull("pickupTip")) {
                                pickupTip = jdata.getString("pickupTip");
                            }
                            if (jdata.has("isFullTip") && !jdata.isNull("isFullTip")) {
                                isFullTip = jdata.getString("isFullTip");
                            }
                            if (jdata.has("selection") && !jdata.isNull("selection")) {
                                JSONArray jarrselection = jdata.getJSONArray("selection");
                                if (jarrselection.length() > 0) {
                                    for (int i = 0; i < jarrselection.length(); i++) {
                                        topDateList.add(AppointMentDate.json2Entity(jarrselection
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(AppointTimeActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "getRechargeBanner()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(AppointTimeActivity.this, "数据异常");
            }
            if (Utils.isStrNull(workdayDiscountTip)) {
                tvAppointtimeDiscount.setVisibility(View.VISIBLE);
                if (workdayDiscountTip.contains("@@")) {
                    String[] split = workdayDiscountTip.split("@@");
                    if (split != null && split.length > 0) {
                        SpannableString ss = new SpannableString(workdayDiscountTip.replace("@@", ""));
                        int startIndex = split[0].length();
                        int endIndex = startIndex + split[1].length();
                        int startIndex1 = endIndex + split[2].length();
                        int endIndex1 = startIndex1 + split[3].length();
                        ss.setSpan(
                                new ForegroundColorSpan(mContext.getResources().getColor(
                                        R.color.aD0021B)), startIndex, endIndex,
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        ss.setSpan(
                                new ForegroundColorSpan(mContext.getResources().getColor(
                                        R.color.aD0021B)), startIndex1, endIndex1,
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        tvAppointtimeDiscount.setText(ss);
                    }
                }
            } else {
                tvAppointtimeDiscount.setVisibility(View.GONE);
            }
            Utils.setText(tvAppointtimeTishi, pickupTip, "表示宠物地址在门店3km范围内，并且本时间段可接送", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(isFullTip)) {
                ToastUtil.showToastShortBottom(AppointTimeActivity.this, isFullTip);
                task = new TimerTask() {
                    @Override
                    public void run() {
                        Log.e("TAG", "finish");
                        finish();
                    }
                };
                timer = new Timer();
                timer.schedule(task, 3000);
            } else {
                if (topDateList.size() > 0) {
                    if (dayposition >= 0) {
                        if (topDateList.get(dayposition).getIsFull() == 1) {
                            for (int i = 0; i < topDateList.size(); i++) {
                                AppointMentDate appointMentDate = topDateList.get(i);
                                if (appointMentDate.getIsFull() == 0) {
                                    dayposition = i;
                                    break;
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < topDateList.size(); i++) {
                            AppointMentDate appointMentDate = topDateList.get(i);
                            if (appointMentDate.getIsFull() == 0) {
                                dayposition = i;
                                break;
                            }
                        }
                    }
                    selectDate();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(AppointTimeActivity.this, "请求失败");
        }
    };

    private void ScollTo() {
        // 解决自动滚动问题
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvAppointtimeTopriqi.scrollToPosition(dayposition);
            }
        }, 5);
    }

    private void setLinster() {
        topDateAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (topDateList.size() > position) {
                    AppointMentDate appointMentDate = topDateList.get(position);
                    if (appointMentDate.getIsFull() == 0) {
                        for (int i = 0; i < topDateList.size(); i++) {
                            topDateList.get(i).setSelect(false);
                        }
                        dayposition = position;
                        selectDate();
                    }
                }
            }
        });
    }

    private void selectDate() {
        workerIds = "";
        rlAppointtimeBottom
                .setVisibility(View.GONE);
        appointment = null;
        pickup = 0;
        selectYear = topDateList.get(dayposition).getYear();
        selectDate = topDateList.get(dayposition).getDate();
        topDateList.get(dayposition).setSelect(true);
        topDateAdapter.notifyDataSetChanged();
        ScollTo();
        setTimes(topDateList.get(dayposition).getTimes());
    }

    private void setTimes(List<AppointMentTime> times) {
        //1.取出整点
        if (times != null && times.size() > 0) {
            wholeTimeList.clear();
            for (int i = 0; i < times.size(); i++) {
                AppointMentTime appointMentTime = times.get(i);
                if (appointMentTime != null) {
                    String time = appointMentTime.getTime();
                    if (Utils.isStrNull(time) && time.contains(":")) {
                        String[] split = time.split(":");
                        if (split != null && split.length > 0) {
                            wholeTimeList.add(Integer.parseInt(split[0]));
                        }
                    }
                }
            }
            Log.e("TAG", "wholeTimeList.size() = " + wholeTimeList.size());
            Log.e("TAG", "wholeTimeList.toString() = " + wholeTimeList.toString());
            //2.整点去重
            List<Integer> listWithoutDup = new ArrayList<Integer>(new HashSet<Integer>(wholeTimeList));
            Log.e("TAG", "listWithoutDup.size() = " + listWithoutDup.size());
            Log.e("TAG", "listWithoutDup.toString() = " + listWithoutDup.toString());
            //3.整点排序
            Collections.sort(listWithoutDup);
            Log.e("TAG", "listWithoutDup.size() = " + listWithoutDup.size());
            Log.e("TAG", "listWithoutDup.toString() = " + listWithoutDup.toString());
            timeList.clear();
            //4.将整点下塞入具体时间格子信息
            for (int i = 0; i < listWithoutDup.size(); i++) {
                AppointMentDate appointMentDate = new AppointMentDate();
                List<AppointMentTime> times1 = new ArrayList<AppointMentTime>();
                Integer integer = listWithoutDup.get(i);
                appointMentDate.setTime(integer + "点");
                for (int j = 0; j < times.size(); j++) {
                    AppointMentTime appointMentTime = times.get(j);
                    if (appointMentTime != null) {
                        String time = appointMentTime.getTime();
                        if (Utils.isStrNull(time) && time.contains(":")) {
                            String[] split = time.split(":");
                            if (split != null && split.length > 0) {
                                if (integer == Integer.parseInt(split[0])) {
                                    times1.add(appointMentTime);
                                }
                            }
                        }
                    }
                }
                appointMentDate.setTimes(times1);
                timeList.add(appointMentDate);
            }
            Log.e("TAG", "timeList = " + timeList.toString());
            //5.完善整点时间格子其他信息(是否可约和是否支持接送)
            for (int i = 0; i < timeList.size(); i++) {
                AppointMentDate appointMentDate = timeList.get(i);
                if (appointMentDate != null) {
                    appointMentDate.setPickup(0);//置为不可接送
                    appointMentDate.setIsFull(1);//置为不可约
                    List<AppointMentTime> times1 = appointMentDate.getTimes();
                    if (times1 != null && times1.size() > 0) {
                        for (int j = 0; j < times1.size(); j++) {
                            AppointMentTime appointMentTime = times1.get(j);
                            if (appointMentTime != null) {
                                List<Integer> workers = appointMentTime.getWorkers();
                                int pickup = appointMentTime.getPickup();
                                if (pickup == 1) {//至少有一个格子支持接送
                                    appointMentDate.setPickup(1);
                                }
                                if (workers != null && workers.size() > 0) {//至少有一个格子可约
                                    appointMentDate.setIsFull(0);
                                }
                            }
                        }
                    }
                }
            }
            Log.e("TAG", "timeList = " + timeList.toString());
            Log.e("TAG", "timeList.size() = " + timeList.size());
            if (timeList.size() > 0) {
                zhengDianTimeList.clear();
                //集合分段
                int flag = DATA_COUNT;//每次取的数据
                int size = timeList.size();
                int temp = size / flag + 1;
                boolean special = size % flag == 0;
                List<AppointMentDate> cutList = null;
                for (int i = 0; i < temp; i++) {
                    if (i == temp - 1) {
                        if (special) {
                            break;
                        }
                        cutList = timeList.subList(flag * i, size);
                    } else {
                        cutList = timeList.subList(flag * i, flag * (i + 1));
                    }
                    Log.e("TAG", "第" + (i + 1) + "组：" + cutList.toString());
                    Log.e("TAG", "第cutList.size() = " + cutList.size());
                    AppointMentZhengDianTime appointMentZhengDianTime = new AppointMentZhengDianTime(false, false, false, false);
                    for (int j = 0; j < cutList.size(); j++) {
                        AppointMentDate appointMentDate = cutList.get(j);
                        if (j == 0) {
                            appointMentZhengDianTime.setAppointMentDate1(appointMentDate);
                        }
                        if (j == 1) {
                            appointMentZhengDianTime.setAppointMentDate2(appointMentDate);
                        }
                        if (j == 2) {
                            appointMentZhengDianTime.setAppointMentDate3(appointMentDate);
                        }
                        if (j == 3) {
                            appointMentZhengDianTime.setAppointMentDate4(appointMentDate);
                        }
                    }
                    zhengDianTimeList.add(appointMentZhengDianTime);
                }
                appointMentTimeTimeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointZDTimeEvent event) {
        workerIds = "";
        rlAppointtimeBottom
                .setVisibility(View.GONE);
        pickup = 0;
        appointment = null;
        Log.e("TAG", "event = " + event);
        if (event != null) {
            int position = event.getPosition();
            int index = event.getIndex();
            if (zhengDianTimeList.size() > position) {
                for (int i = 0; i < zhengDianTimeList.size(); i++) {
                    AppointMentZhengDianTime appointMentZhengDianTime = zhengDianTimeList.get(i);
                    if (appointMentZhengDianTime != null) {
                        appointMentZhengDianTime.setSelect1(false);
                        appointMentZhengDianTime.setSelect2(false);
                        appointMentZhengDianTime.setSelect3(false);
                        appointMentZhengDianTime.setSelect4(false);
                    }
                }
                AppointMentZhengDianTime appointMentZhengDianTime = zhengDianTimeList.get(position);
                if (appointMentZhengDianTime != null) {
                    AppointMentDate appointMentDate1 = appointMentZhengDianTime.getAppointMentDate1();
                    AppointMentDate appointMentDate2 = appointMentZhengDianTime.getAppointMentDate2();
                    AppointMentDate appointMentDate3 = appointMentZhengDianTime.getAppointMentDate3();
                    AppointMentDate appointMentDate4 = appointMentZhengDianTime.getAppointMentDate4();
                    if (index == 1) {
                        if (appointMentDate1 != null) {
                            appointMentZhengDianTime.setSelect1(true);
                            localTimes = appointMentDate1.getTimes();
                        }
                    } else if (index == 2) {
                        if (appointMentDate2 != null) {
                            appointMentZhengDianTime.setSelect2(true);
                            localTimes = appointMentDate2.getTimes();
                        }
                    } else if (index == 3) {
                        if (appointMentDate3 != null) {
                            appointMentZhengDianTime.setSelect3(true);
                            localTimes = appointMentDate3.getTimes();
                        }
                    } else if (index == 4) {
                        if (appointMentDate4 != null) {
                            appointMentZhengDianTime.setSelect4(true);
                            localTimes = appointMentDate4.getTimes();
                        }
                    }
                }
                if (localTimes != null && localTimes.size() > 0) {
                    for (int i = 0; i < localTimes.size(); i++) {
                        localTimes.get(i).setSelect(false);
                    }
                    Log.e("TAG", "localTimes.toString() = " + localTimes.toString());
                    Log.e("TAG", "localTimes.size() = " + localTimes.size());
                } else {
                    Log.e("TAG", "localTimes = " + localTimes);
                }
                appointMentTimeTimeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointmentTimeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            int position = event.getPosition();
            if (localTimes != null && localTimes.size() > 0 && localTimes.size() > position) {
                for (int i = 0; i < localTimes.size(); i++) {
                    AppointMentTime appointMentTime = localTimes.get(i);
                    if (appointMentTime != null) {
                        appointMentTime.setSelect(false);
                    }
                }
                localTimes.get(position).setSelect(true);
                appointMentTimeTimeAdapter.notifyDataSetChanged();
                pickup = localTimes.get(position).getPickup();
                appointment = selectYear + "-" + selectDate + " " + localTimes.get(position).getTime() + ":00";
                /*if (appointWorker != null) {
                    rlAppointtimeBottom
                            .setVisibility(View.VISIBLE);
                    if (pickup == 1) {
                        Utils.setText(tvAppointtimeSelecttime, appointment + "(可接送)", "", View.VISIBLE, View.VISIBLE);
                    } else {
                        Utils.setText(tvAppointtimeSelecttime, appointment, "", View.VISIBLE, View.VISIBLE);
                    }
                } else {*/
                workerIds = "";
                rlAppointtimeBottom
                        .setVisibility(View.GONE);
                mPDialog.showDialog();
                CommUtil.getBeauticianFreeTime(
                        AppointTimeActivity.this,
                        lat,
                        lng,
                        spUtil.getString(
                                "cellphone", ""),
                        Global.getIMEI(AppointTimeActivity.this),
                        Global.getCurrentVersion(AppointTimeActivity.this),
                        0, serviceLoc, shopId,
                        strp, 0, appointment, 0, 0,
                        allWorkerHanler);
                //}
            }
        }
    }

    private AsyncHttpResponseHandler allWorkerHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject jdata = jsonObject.getJSONObject("data");
                        if (jdata.has("workers") && !jdata.isNull("workers")) {
                            JSONArray jsonArray = jdata.getJSONArray("workers");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                String str = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    str = str + "," + jsonArray.get(i);
                                }
                                workerIds = str.substring(1, str.length());
                                rlAppointtimeBottom
                                        .setVisibility(View.VISIBLE);
                                if (pickup == 1) {
                                    Utils.setText(tvAppointtimeSelecttime, appointment + "(可接送)", "", View.VISIBLE, View.VISIBLE);
                                } else {
                                    Utils.setText(tvAppointtimeSelecttime, appointment, "", View.VISIBLE, View.VISIBLE);
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(AppointTimeActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getSingles()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(AppointTimeActivity.this, "数据异常");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            Log.e("TAG", "请求失败");
            ToastUtil.showToastShortBottom(AppointTimeActivity.this, "请求失败");
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

    @OnClick({R.id.ib_titlebar_back, R.id.tv_appointtime_worker_qitatime, R.id.iv_appointtime_shopphone, R.id.tv_appointtime_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_appointtime_worker_qitatime:
                dayposition = -1;
                workerId = 0;
                appointWorker = null;
                setShopOrWorker(false);
                break;
            case R.id.iv_appointtime_shopphone:
                Utils.setPhoneDialog(AppointTimeActivity.this, shopPhone, shopPhone, "取消", "呼叫",
                        AppointTimeActivity.this.getResources().getColor(R.color.a666666), AppointTimeActivity.this.getResources().getColor(R.color.a007AFF),
                        AppointTimeActivity.this.getResources().getColor(R.color.a333333));
                break;
            case R.id.tv_appointtime_submit:
                if (WorkerListActivity.act != null) {
                    WorkerListActivity.act.finish();
                }
                if (appointWorker != null) {
                    if (BeauticianDetailActivity.act != null) {
                        BeauticianDetailActivity.act.finish();
                    }
                    if (previous == Global.ITEMLIST_TO_OVERTIME) {
                        EventBus.getDefault().post(new ItemListWorkerAndTimeEvent(appointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                    } else if (previous == Global.ITEMDETAIL_TO_OVERTIME) {
                        EventBus.getDefault().post(new ItemDetailWorkerAndTimeEvent(appointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                    } else if (previous == Global.SWITCHSERVICE_TO_OVERTIME) {
                        EventBus.getDefault().post(new SwitchServiceWorkerAndTimeEvent(appointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                    } else {
                        EventBus.getDefault().post(new WorkerAndTimeEvent(appointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                    }
                    finish();
                } else {
                    Intent intent2 = new Intent(this, WorkerListActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("shop", shop);
                    bundle2.putSerializable("itemToMorePet", itemToMorePet);
                    intent2.putExtras(bundle2);
                    intent2.putExtra("lat", lat);
                    intent2.putExtra("dayposition", dayposition);
                    intent2.putExtra("pickup", pickup);
                    intent2.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                    intent2.putExtra("lng", lng);
                    intent2.putExtra("previous", previous);
                    intent2.putExtra("workerIds", workerIds);
                    intent2.putExtra("appointment", appointment);
                    intent2.putExtra("serviceLoc", serviceLoc);
                    intent2.putExtra("petList", (Serializable) (List<ApointMentPet>) getIntent().getSerializableExtra("petList"));
                    intent2.putExtra("isVip", getIntent().getBooleanExtra("isVip", true));
                    intent2.putExtra("tid", getIntent().getIntExtra("tid", 0));
                    intent2.putExtra("strp", strp);
                    startActivity(intent2);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
