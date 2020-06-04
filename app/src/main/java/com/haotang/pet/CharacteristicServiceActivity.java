package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AllSwitchServiceAdapter;
import com.haotang.pet.entity.AllSwitchService;
import com.haotang.pet.entity.AllSwitchServiceEvent;
import com.haotang.pet.entity.SwitchService;
import com.haotang.pet.entity.SwitchServiceItemEvent;
import com.haotang.pet.entity.SwitchServiceItems;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>
 * Title:CharacteristicServiceActivity
 * </p>
 * <p>
 * Description:特色服务列表界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2017-2-14 下午4:59:01
 */
public class CharacteristicServiceActivity extends SuperActivity {
    private static final int DATA_COUNT = 3;
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_appointswitch)
    RecyclerView rvAppointswitch;
    private List<SwitchService> list = new ArrayList<SwitchService>();
    private List<AllSwitchService> allSwitchServiceList = new ArrayList<AllSwitchService>();
    private AllSwitchServiceAdapter allSwitchServiceAdapter;
    private List<SwitchServiceItems> localItems = new ArrayList<SwitchServiceItems>();
    private int typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void setLinster() {
    }

    private void setView() {
        tvTitlebarTitle.setText("特色服务");
        rvAppointswitch.setHasFixedSize(true);
        rvAppointswitch.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        rvAppointswitch.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 30),
                ContextCompat.getColor(this, R.color.white)));
        allSwitchServiceList.clear();
        allSwitchServiceAdapter = new AllSwitchServiceAdapter(R.layout.item_switchservice_all, allSwitchServiceList, true, SwitchServiceItemEvent.CHARACTERISTIC);
        rvAppointswitch.setAdapter(allSwitchServiceAdapter);
    }

    private void initData() {
        typeId = getIntent().getIntExtra("typeId", 0);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_character);
        ButterKnife.bind(this);
    }

    private void getData() {
        mPDialog.showDialog();
        list.clear();
        CommUtil.getFe(this, getFe);
    }

    private AsyncHttpResponseHandler getFe = new AsyncHttpResponseHandler() {

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
                            if (jdata.has("dataset") && !jdata.isNull("dataset")) {
                                JSONArray jarrdataset = jdata.getJSONArray("dataset");
                                if (jarrdataset.length() > 0) {
                                    for (int i = 0; i < jarrdataset.length(); i++) {
                                        list.add(SwitchService.json2Entity(jarrdataset
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(CharacteristicServiceActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "getRechargeBanner()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(CharacteristicServiceActivity.this, "数据异常");
            }
            if (list.size() > 0) {
                allSwitchServiceList.clear();
                //集合分段
                int flag = DATA_COUNT;//每次取的数据
                int size = list.size();
                int temp = size / flag + 1;
                boolean special = size % flag == 0;
                List<SwitchService> cutList = null;
                for (int i = 0; i < temp; i++) {
                    if (i == temp - 1) {
                        if (special) {
                            break;
                        }
                        cutList = list.subList(flag * i, size);
                    } else {
                        cutList = list.subList(flag * i, flag * (i + 1));
                    }
                    Log.e("TAG", "第" + (i + 1) + "组：" + cutList.toString());
                    Log.e("TAG", "第cutList.size() = " + cutList.size());
                    AllSwitchService allSwitchService = new AllSwitchService(false, false, false);
                    for (int j = 0; j < cutList.size(); j++) {
                        SwitchService switchService = cutList.get(j);
                        if (j == 0) {
                            allSwitchService.setSwitchService1(switchService);
                        }
                        if (j == 1) {
                            allSwitchService.setSwitchService2(switchService);
                        }
                        if (j == 2) {
                            allSwitchService.setSwitchService3(switchService);
                        }
                    }
                    allSwitchServiceList.add(allSwitchService);
                }
                if (typeId > 0) {
                    for (int i = 0; i < allSwitchServiceList.size(); i++) {
                        AllSwitchService allSwitchService = allSwitchServiceList.get(i);
                        if (allSwitchService != null) {
                            allSwitchService.setSelect1(false);
                            allSwitchService.setSelect2(false);
                            allSwitchService.setSelect3(false);
                            SwitchService switchService1 = allSwitchService.getSwitchService1();
                            SwitchService switchService2 = allSwitchService.getSwitchService2();
                            SwitchService switchService3 = allSwitchService.getSwitchService3();
                            if (switchService1 != null && switchService1.getTypeId() == typeId) {
                                allSwitchService.setSelect1(true);
                                List<SwitchServiceItems> items = switchService1.getItems();
                                if (items != null && items.size() > 0) {
                                    localItems.clear();
                                    localItems.addAll(items);
                                } else {
                                        Intent intent = new Intent(CharacteristicServiceActivity.this, ServiceNewActivity.class);
                                        intent.putExtra("serviceType", switchService1.getServiceType());
                                        intent.putExtra("serviceId", switchService1.getServiceId());
                                        startActivity(intent);
                                }
                                break;
                            } else if (switchService2 != null && switchService2.getTypeId() == typeId) {
                                allSwitchService.setSelect2(true);
                                List<SwitchServiceItems> items = switchService2.getItems();
                                if (items != null && items.size() > 0) {
                                    localItems.clear();
                                    localItems.addAll(items);
                                } else {
                                        Intent intent = new Intent(CharacteristicServiceActivity.this, ServiceNewActivity.class);
                                        intent.putExtra("serviceType", switchService2.getServiceType());
                                        intent.putExtra("serviceId", switchService2.getServiceId());
                                        startActivity(intent);
                                }
                                break;
                            } else if (switchService3 != null && switchService3.getTypeId() == typeId) {
                                allSwitchService.setSelect3(true);
                                List<SwitchServiceItems> items = switchService3.getItems();
                                if (items != null && items.size() > 0) {
                                    localItems.clear();
                                    localItems.addAll(items);
                                } else {
                                        Intent intent = new Intent(CharacteristicServiceActivity.this, ServiceNewActivity.class);
                                        intent.putExtra("serviceType", switchService3.getServiceType());
                                        intent.putExtra("serviceId", switchService3.getServiceId());
                                        startActivity(intent);
                                }
                                break;
                            }
                        }
                    }
                    if (localItems != null && localItems.size() > 0) {
                        for (int i = 0; i < localItems.size(); i++) {
                            localItems.get(i).setSelect(false);
                        }
                        Log.e("TAG", "localItems.toString() = " + localItems.toString());
                        Log.e("TAG", "localItems.size() = " + localItems.size());
                    } else {
                        Log.e("TAG", "localItems = " + localItems);
                    }
                }
                allSwitchServiceAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(CharacteristicServiceActivity.this, "请求失败");
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AllSwitchServiceEvent event) {
        Log.e("TAG", "event = " + event.toString());
        if (event != null) {
            if (event.getFlag() == AllSwitchServiceEvent.CHARACTERISTIC) {
                int position = event.getPosition();
                int index = event.getIndex();
                if (allSwitchServiceList.size() > position) {
                    for (int i = 0; i < allSwitchServiceList.size(); i++) {
                        AllSwitchService allSwitchService = allSwitchServiceList.get(i);
                        if (allSwitchService != null) {
                            allSwitchService.setSelect1(false);
                            allSwitchService.setSelect2(false);
                            allSwitchService.setSelect3(false);
                        }
                    }
                    AllSwitchService allSwitchService = allSwitchServiceList.get(position);
                    if (allSwitchService != null) {
                        SwitchService switchService1 = allSwitchService.getSwitchService1();
                        SwitchService switchService2 = allSwitchService.getSwitchService2();
                        SwitchService switchService3 = allSwitchService.getSwitchService3();
                        if (index == 1) {
                            if (switchService1 != null) {
                                allSwitchService.setSelect1(true);
                                List<SwitchServiceItems> items = switchService1.getItems();
                                if (items != null && items.size() > 0) {
                                    localItems.clear();
                                    localItems.addAll(items);
                                } else {
                                    Intent intent = new Intent(CharacteristicServiceActivity.this, ServiceNewActivity.class);
                                    intent.putExtra("serviceType", switchService1.getServiceType());
                                    intent.putExtra("serviceId", switchService1.getServiceId());
                                    startActivity(intent);
                                }
                            }
                        } else if (index == 2) {
                            if (switchService2 != null) {
                                allSwitchService.setSelect2(true);
                                List<SwitchServiceItems> items = switchService2.getItems();
                                if (items != null && items.size() > 0) {
                                    localItems.clear();
                                    localItems.addAll(items);
                                } else {
                                    Intent intent = new Intent(CharacteristicServiceActivity.this, ServiceNewActivity.class);
                                    intent.putExtra("serviceType", switchService2.getServiceType());
                                    intent.putExtra("serviceId", switchService2.getServiceId());
                                    startActivity(intent);
                                }
                            }
                        } else if (index == 3) {
                            if (switchService3 != null) {
                                allSwitchService.setSelect3(true);
                                List<SwitchServiceItems> items = switchService3.getItems();
                                if (items != null && items.size() > 0) {
                                    localItems.clear();
                                    localItems.addAll(items);
                                } else {
                                    Intent intent = new Intent(CharacteristicServiceActivity.this, ServiceNewActivity.class);
                                    intent.putExtra("serviceType", switchService3.getServiceType());
                                    intent.putExtra("serviceId", switchService3.getServiceId());
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                    if (localItems != null && localItems.size() > 0) {
                        for (int i = 0; i < localItems.size(); i++) {
                            localItems.get(i).setSelect(false);
                        }
                        Log.e("TAG", "localItems.toString() = " + localItems.toString());
                        Log.e("TAG", "localItems.size() = " + localItems.size());
                    } else {
                        Log.e("TAG", "localItems = " + localItems);
                    }
                    allSwitchServiceAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SwitchServiceItemEvent event) {
        Log.e("TAG", "event = " + event.toString());
        if (event != null) {
            if (event.getIndex() == SwitchServiceItemEvent.CHARACTERISTIC) {
                int position = event.getPosition();
                if (localItems != null && localItems.size() > 0 && localItems.size() > position) {
                    for (int i = 0; i < localItems.size(); i++) {
                        SwitchServiceItems switchServiceItems = localItems.get(i);
                        if (switchServiceItems != null) {
                            switchServiceItems.setSelect(false);
                        }
                    }
                    localItems.get(position).setSelect(true);
                    allSwitchServiceAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(CharacteristicServiceActivity.this, ServiceNewActivity.class);
                    intent.putExtra("serviceType", localItems.get(position).getServiceType());
                    intent.putExtra("serviceId", localItems.get(position).getServiceId());
                    startActivity(intent);
                }
            }
        }
    }

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
        MobclickAgent.onPageStart("CharacteristicServiceActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CharacteristicServiceActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
