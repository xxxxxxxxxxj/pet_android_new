package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AppointMentTimeTimeAdapter;
import com.haotang.pet.adapter.TopDateAdapter;
import com.haotang.pet.entity.AppointMentDate;
import com.haotang.pet.entity.AppointMentTime;
import com.haotang.pet.entity.AppointMentZhengDianTime;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.AppointZDTimeEvent;
import com.haotang.pet.entity.AppointmentTimeEvent;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;

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
 * Created by Administrator on 2018/8/28 0028.
 */

public class ChangeOrderNewActivity extends SuperActivity {
    public static ChangeOrderNewActivity act;
    private static final int DATA_COUNT = 4;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.tv_appointfrg_time)
    TextView tvAppointfrgTime;
    @BindView(R.id.rv_appointtime_topriqi)
    RecyclerView rvAppointtimeTopriqi;
    @BindView(R.id.rv_appointtime_time)
    RecyclerView rvAppointtimeTime;
    @BindView(R.id.textview_beau_level)
    TextView textviewBeauLevel;
    @BindView(R.id.gv_appointfrg_beau)
    MyGridView gvAppointfrgBeau;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.button_ok)
    Button buttonOk;
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
    private int areaId;
    private int OrderId;
    private String modifyTip;
    private Beautician beauticianOld;
    private String beauData = "";
    private String beauTime = "";
    private String beauTimeLeft = "";
    private int WorkLevel = -1;
    private List<Beautician> allChangeBeau = new ArrayList<Beautician>();
    protected String strListWokerId = "";
    private boolean isChooseCurrentBeau;
    private String modifyOrderRuleUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order_new);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        ButterKnife.bind(this);
        getDataIntent();
        setView();
        getData();
        setLinster();

    }

    private void setView() {
        tvTitlebarTitle.setText("修改订单");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("改单规则");
        tvTitlebarOther.setTextColor(Color.WHITE);
        tvAppointfrgTime.setText(modifyTip);
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
        appointMentTimeTimeAdapter.setChangeOrder(true);
        if (appointWorker != null) {//当前美容师的时间
//            setShopOrWorker(true);
        } else {//店铺的时间
//            setShopOrWorker(false);
        }
    }

    private void getDataIntent() {
        modifyOrderRuleUrl = getIntent().getStringExtra("modifyOrderRuleUrl");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        areaId = getIntent().getIntExtra("areaId", 0);
        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        shopId = getIntent().getIntExtra("shopId", 0);
        OrderId = getIntent().getIntExtra("OrderId", 0);
        strp = getIntent().getStringExtra("strp");
        modifyTip = getIntent().getStringExtra("modifyTip");
        beauticianOld = (Beautician) getIntent().getSerializableExtra("beautician");
        workerId = beauticianOld.id;
        beauticianOld.isChoose = true;
        beauData = beauticianOld.appointment.split(" ")[0].trim();
        beauTime = beauticianOld.appointment.split(" ")[1].trim();
        beauTimeLeft = beauTime.split(":")[0].trim();
        Utils.mLogError("==-->data beauData 00   " + beauTimeLeft.indexOf("0"));
        if (beauTimeLeft.contains("0")) {
            if (beauTimeLeft.indexOf("0") == 0) {
                beauTimeLeft = beauTimeLeft.substring(1, beauTimeLeft.length());
            }
        }
        Utils.mLogError("==-->data beauData beauTimeLeft  " + beauTimeLeft);
        Utils.mLogError("==-->data beauData " + beauData);
        Utils.mLogError("==-->data beauTime " + beauTime);
        WorkLevel = beauticianOld.levels;
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
//        rlAppointtimeBottom
//                .setVisibility(View.GONE);
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

    private void ScollTo() {
        // 解决自动滚动问题
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvAppointtimeTopriqi.scrollToPosition(dayposition);
            }
        }, 5);
    }
    public void getDataWithWorkId(){
        isChooseCurrentBeau = true;
        topDateList.clear();
        mPDialog.showDialog();
        CommUtil.getBeauticianFreeTime(mContext, lat, lng,
                spUtil.getString("cellphone", ""), Global.getIMEI(mContext),
                Global.getCurrentVersion(mContext), areaId, serviceLoc,
                shopId, strp, WorkLevel, null, OrderId,workerId, timeHanler);
    }
    private void getData() {
        mPDialog.showDialog();
        CommUtil.getBeauticianFreeTime(mContext, lat, lng,
                spUtil.getString("cellphone", ""), Global.getIMEI(mContext),
                Global.getCurrentVersion(mContext), areaId, serviceLoc,
                shopId, strp, WorkLevel, null, OrderId, 0, timeHanler);

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
                        ToastUtil.showToastShortBottom(mContext, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "getRechargeBanner()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
//            Utils.setText(tvAppointtimeTishi, pickupTip, "表示宠物地址在门店3km范围内，并且本时间段可接送", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(isFullTip)) {
                ToastUtil.showToastShortBottom(mContext, isFullTip);
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
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };
    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                allChangeBeau.clear();
                if (code == 0) {
                    try {
                        if (object.has("data") && !object.isNull("data")) {
                            JSONArray array = object.getJSONArray("data");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    allChangeBeau.add(Beautician.json2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (allChangeBeau.size() > 0) {
                    Intent intent = new Intent(mContext, ChangeOrderChooseBeauActivity.class);
                    intent.putExtra("beaution", (Serializable) allChangeBeau);
                    intent.putExtra("beautionold", beauticianOld);
                    intent.putExtra("appointment", appointment);
                    intent.putExtra("shopId", shopId);
                    intent.putExtra("workerId", workerId);
                    intent.putExtra("OrderId", OrderId);
                    intent.putExtra("isChooseCurrentBeau", isChooseCurrentBeau);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_open, R.anim.bottom_silent);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {

        }
    };

    private void getChangeBeau(String str) {
        mPDialog.showDialog();
        CommUtil.queryWorkersByIds(mContext, str, handler);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointZDTimeEvent event) {
        workerIds = "";
//        rlAppointtimeBottom.setVisibility(View.GONE);
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
            boolean isChangeOrder = event.isChangeOrder();
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
                List<Integer> workers = localTimes.get(position).getWorkers();

                appointment = selectYear + "-" + selectDate + " " + localTimes.get(position).getTime() + ":00";
//                if (appointWorker != null) {
//                    rlAppointtimeBottom.setVisibility(View.VISIBLE);
//                    if (pickup == 1) {
//                        Utils.setText(tvAppointtimeSelecttime, appointment + "(可接送)", "", View.VISIBLE, View.VISIBLE);
//                    } else {
//                        Utils.setText(tvAppointtimeSelecttime, appointment, "", View.VISIBLE, View.VISIBLE);
//                    }
//                } else {
//                    workerIds = "";
//                    rlAppointtimeBottom.setVisibility(View.GONE);
//                    mPDialog.showDialog();
//                    CommUtil.getBeauticianFreeTime(
//                            mContext,
//                            lat,
//                            lng,
//                            spUtil.getString(
//                                    "cellphone", ""),
//                            Global.getIMEI(mContext,
//                            Global.getCurrentVersion(mContext),
//                            0, serviceLoc, shopId,
//                            strp, 0, appointment, 0, 0,
//                            allWorkerHanler);
                if (workers != null && workers.size() > 0) {
                    String str = "";
                    if (workers != null && workers.size() > 0) {
                        for (int i = 0; i < workers.size(); i++) {
                            str = str + "," + workers.get(i);
                        }
                    }
                    try {
                        strListWokerId = str.substring(1, str.length());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (isChangeOrder) {
                    getChangeBeau(strListWokerId);
                }

//                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_title,R.id.tv_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_title:
                break;
            case R.id.tv_titlebar_other:
                Intent intent = new Intent(ChangeOrderNewActivity.this,ADActivity.class);
                intent.putExtra("url",modifyOrderRuleUrl);
                startActivity(intent);
                break;
        }
    }
}
