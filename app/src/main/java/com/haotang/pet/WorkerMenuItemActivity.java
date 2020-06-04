package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AllSwitchServiceAdapter;
import com.haotang.pet.entity.AllSwitchService;
import com.haotang.pet.entity.AllSwitchServiceEvent;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.SwitchService;
import com.haotang.pet.entity.SwitchServiceItemEvent;
import com.haotang.pet.entity.SwitchServiceItems;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
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
 * 美容师可约服务列表
 */
public class WorkerMenuItemActivity extends SuperActivity {
    private static final int DATA_COUNT = 3;
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_workermenuitem)
    RecyclerView rvAppointswitch;
    private List<SwitchService> list = new ArrayList<SwitchService>();
    private List<AllSwitchService> allSwitchServiceList = new ArrayList<AllSwitchService>();
    private AllSwitchServiceAdapter allSwitchServiceAdapter;
    private List<SwitchServiceItems> localItems = new ArrayList<SwitchServiceItems>();//服务子选项列表
    private int workerId;
    //美容师数据
    private Beautician beautician;
    //服务名称
    private String serviceName;

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
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(this);
        beautician = (Beautician) getIntent().getSerializableExtra("beautician");
        //获取服务名称
        serviceName = getIntent().getStringExtra("serviceName");
        if (beautician != null) {
            workerId = beautician.id;
        }
    }

    private void findView() {
        setContentView(R.layout.activity_worker_menu_item);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("预约服务");
        rvAppointswitch.setHasFixedSize(true);
        rvAppointswitch.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        rvAppointswitch.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 30),
                ContextCompat.getColor(this, R.color.white)));
        allSwitchServiceList.clear();
        allSwitchServiceAdapter = new AllSwitchServiceAdapter(R.layout.item_switchservice_all, allSwitchServiceList, true, SwitchServiceItemEvent.WORKERMENUITEM);
        rvAppointswitch.setAdapter(allSwitchServiceAdapter);
    }

    private void setLinster() {

    }

    private void getData() {
        mPDialog.showDialog();
        list.clear();
        CommUtil.queryWorkerMenuItems(WorkerMenuItemActivity.this, workerId, queryWorkerMenuItems);
    }

    private AsyncHttpResponseHandler queryWorkerMenuItems = new AsyncHttpResponseHandler() {

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
                                        //添加完所有服务
                                        list.add(SwitchService.json2Entity(jarrdataset
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                    if (list.size() <= 0) {
                        allSwitchServiceAdapter.setEmptyView(setEmptyViewBase(2, "暂无可做服务哦~",
                                R.drawable.icon_no_mypet, null));
                    }
                } else {
                    allSwitchServiceAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getData();
                                }
                            }));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "getRechargeBanner()数据异常e = " + e.toString());
                allSwitchServiceAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                }));
            }
            if (list.size() > 0) {
                //清空所有服务列表
                allSwitchServiceList.clear();
                //集合分段
                int flag = DATA_COUNT;//每次取的数据
                int size = list.size();
                int temp = size / flag + 1;
                boolean special = size % flag == 0;
                List<SwitchService> cutList = null;

                for (int i = 0; i < temp; i++) {
                    //判断是否截取到最后
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
                        //找到选择的服务
                        if (j == 0) {
                            allSwitchService.setSwitchService1(switchService);
                        }
                        if (j == 1) {
                            allSwitchService.setSwitchService2(switchService);
                        }
                        if (j == 2) {
                            allSwitchService.setSwitchService3(switchService);
                        }
                        selectService(allSwitchService,switchService,j);
                    }
                    allSwitchServiceList.add(allSwitchService);
                }
                allSwitchServiceAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            allSwitchServiceAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            }));
        }
    };

    private void selectService(AllSwitchService allSwitchService,SwitchService switchService,int i){
        Utils.mLogError("找到选择的服务 "+"serviceName "+serviceName+" switchService "+switchService.getName());
        if (!TextUtils.isEmpty(serviceName) && switchService.getName().equals(serviceName)){
//            switchService.setSelect(true);
            if(i == 0){
                allSwitchService.setSelect1(true);
                allSwitchService.setSelect2(false);
                allSwitchService.setSelect3(false);
            }else if(i == 1)
            {
                allSwitchService.setSelect2(true);
                allSwitchService.setSelect1(false);
                allSwitchService.setSelect3(false);
            }else if (i == 2){
                allSwitchService.setSelect1(false);
                allSwitchService.setSelect2(false);
                allSwitchService.setSelect3(true);
            }
            Utils.mLogError("找到选择的服务 true");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AllSwitchServiceEvent event) {
        Log.e("TAG", "event = " + event.toString());
        if (event != null) {
            //预约服务事件
            if (event.getFlag() == AllSwitchServiceEvent.WORKERMENUITEM) {
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
                                    Intent intent = new Intent(WorkerMenuItemActivity.this, ServiceNewActivity.class);
                                    intent.putExtra("serviceType", switchService1.getServiceType());
                                    intent.putExtra("serviceId", switchService1.getServiceId());
                                    Bundle bundle = new Bundle();
                                    beautician.BeauDetailServiceType = switchService1.getServiceType();
                                    bundle.putSerializable("beautician", beautician);
                                    intent.putExtras(bundle);
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
                                    Intent intent = new Intent(WorkerMenuItemActivity.this, ServiceNewActivity.class);
                                    intent.putExtra("serviceType", switchService2.getServiceType());
                                    intent.putExtra("serviceId", switchService2.getServiceId());
                                    Bundle bundle = new Bundle();
                                    beautician.BeauDetailServiceType = switchService2.getServiceType();
                                    bundle.putSerializable("beautician", beautician);
                                    intent.putExtras(bundle);
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
                                    Intent intent = new Intent(WorkerMenuItemActivity.this, ServiceNewActivity.class);
                                    intent.putExtra("serviceType", switchService3.getServiceType());
                                    intent.putExtra("serviceId", switchService3.getServiceId());
                                    Bundle bundle = new Bundle();
                                    beautician.BeauDetailServiceType = switchService3.getServiceType();
                                    bundle.putSerializable("beautician", beautician);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                    //判断服务子选项列表
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
            if (event.getIndex() == SwitchServiceItemEvent.WORKERMENUITEM) {
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
                    Intent intent = new Intent(WorkerMenuItemActivity.this, ServiceNewActivity.class);
                    intent.putExtra("serviceType", localItems.get(position).getServiceType());
                    intent.putExtra("serviceId", localItems.get(position).getServiceId());
                    Bundle bundle = new Bundle();
                    beautician.BeauDetailServiceType = localItems.get(position).getServiceType();
                    bundle.putSerializable("beautician", beautician);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
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

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
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
