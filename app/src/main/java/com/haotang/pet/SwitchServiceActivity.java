package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AllSwitchServiceAdapter;
import com.haotang.pet.adapter.SwitchServiceBottomItemAdapter;
import com.haotang.pet.entity.AllSwitchService;
import com.haotang.pet.entity.AllSwitchServiceEvent;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.ApointMentItemPrice;
import com.haotang.pet.entity.ApointMentItemPrices;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.ItemToMorePet;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.SwitchService;
import com.haotang.pet.entity.SwitchServiceItemEvent;
import com.haotang.pet.entity.SwitchServiceItems;
import com.haotang.pet.entity.SwitchServiceWorkerAndTimeEvent;
import com.haotang.pet.entity.WorkerAndTime;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 切换服务界面
 */
public class SwitchServiceActivity extends SuperActivity {
    private static final int DATA_COUNT = 3;
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rl_appointswitch_bottom)
    RelativeLayout rlAppointswitchBottom;
    @BindView(R.id.tv_appointswitch_price)
    TextView tvAppointswitchPrice;
    @BindView(R.id.rv_appointswitch)
    RecyclerView rvAppointswitch;
    private int petId;
    private int shopId;
    private int serviceId;
    private int serviceLoc;
    private int workerId;
    private String appointment;
    private List<SwitchService> list = new ArrayList<SwitchService>();
    private List<AllSwitchService> allSwitchServiceList = new ArrayList<AllSwitchService>();
    private AllSwitchServiceAdapter allSwitchServiceAdapter;
    private List<SwitchServiceItems> localItems = new ArrayList<SwitchServiceItems>();
    private int selectServiceId;
    private ApointMentPet pet;
    private int tid = 0;
    private boolean isVip = true;
    private SwitchServiceBottomItemAdapter switchServiceBottomItemAdapter;
    private double selectPrice;
    private double selectVipPrice;
    private String selectServiceName;
    private List<ApointMentItem> localItemList = new ArrayList<ApointMentItem>();
    private List<ApointMentItem> petItemList = new ArrayList<ApointMentItem>();
    private List<ApointMentItem> petItemList1 = new ArrayList<ApointMentItem>();
    private List<ApointMentItemPrice> localItemPriceList = new ArrayList<ApointMentItemPrice>();
    private List<ApointMentItem> shouXiItemList1 = new ArrayList<ApointMentItem>();
    private ItemToMorePet itemToMorePet = new ItemToMorePet();
    private double lat;
    private double lng;
    private AppointWorker localAppointWorker;
    private ServiceShopAdd shop;
    private List<ApointMentPet> petList;
    private int dayposition;
    private boolean isOverTime;
    private int pickup;
    private String workerIds;
    private int serviceCardId;
    private boolean isFirstAddToothCardItem;

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
        isFirstAddToothCardItem = true;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(this);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        serviceCardId = getIntent().getIntExtra("serviceCardId", 0);
        localAppointWorker = (AppointWorker) getIntent().getSerializableExtra("appointWorker");
        shop = (ServiceShopAdd) getIntent().getSerializableExtra("shop");
        if (localAppointWorker != null) {
            tid = localAppointWorker.getTid();
            workerId = localAppointWorker.getWorkerId();
        } else {
            tid = getIntent().getIntExtra("tid", 0);
        }
        if (shop != null) {
            shopId = shop.shopId;
        }
        petList = (List<ApointMentPet>) getIntent().getSerializableExtra("petList");
        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        isVip = getIntent().getBooleanExtra("isVip", true);
        dayposition = getIntent().getIntExtra("dayposition", 0);
        appointment = getIntent().getStringExtra("appointment");
        pet = (ApointMentPet) getIntent().getSerializableExtra("pet");
        if (pet != null) {
            petId = pet.getPetId();
            serviceId = pet.getServiceId();
            List<ApointMentItem> itemList = pet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                petItemList1.clear();
                petItemList.clear();
                petItemList.addAll(itemList);
                for (int i = 0; i < itemList.size(); i++) {
                    ApointMentItem apointMentItem = itemList.get(i);
                    petItemList1.add(new ApointMentItem(apointMentItem.getId()));
                }
            }
        }
    }

    private void findView() {
        setContentView(R.layout.activity_switch_service);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("切换服务");
        rvAppointswitch.setHasFixedSize(true);
        rvAppointswitch.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        rvAppointswitch.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 30),
                ContextCompat.getColor(this, R.color.white)));
        allSwitchServiceList.clear();
        allSwitchServiceAdapter = new AllSwitchServiceAdapter(R.layout.item_switchservice_all, allSwitchServiceList, isVip, SwitchServiceItemEvent.SWITCHSERVICE);
        rvAppointswitch.setAdapter(allSwitchServiceAdapter);
    }

    private double getItemPrice(boolean flag) {
        double itemPrice = 0;
        if (petItemList != null && petItemList.size() > 0) {
            for (int i = 0; i < petItemList.size(); i++) {//基础服务
                ApointMentItem apointMentItem = petItemList.get(i);
                if (apointMentItem != null) {
                    if (flag) {
                        itemPrice = ComputeUtil.add(itemPrice, apointMentItem.getVipPrice());
                    } else {
                        itemPrice = ComputeUtil.add(itemPrice, apointMentItem.getPrice());
                    }
                }
            }
        }
        return itemPrice;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AllSwitchServiceEvent event) {
        selectServiceId = 0;
        selectPrice = 0;
        selectVipPrice = 0;
        selectServiceName = null;
        localItems.clear();
        rlAppointswitchBottom.setVisibility(View.GONE);
        Log.e("TAG", "event = " + event.toString());
        if (event != null) {
            if (event.getFlag() == AllSwitchServiceEvent.SWITCHSERVICE) {
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
                                    selectServiceId = switchService1.getServiceId();
                                    if (Utils.isStrNull(switchService1.getVipPrice())) {
                                        selectVipPrice = Double.parseDouble(switchService1.getVipPrice().substring(switchService1.getVipPrice().indexOf("@@") + "@@".length(),
                                                switchService1.getVipPrice().lastIndexOf("@@")));
                                    }
                                    selectPrice = Double.parseDouble(switchService1.getPrice().substring(switchService1.getPrice().indexOf("@@") + "@@".length(),
                                            switchService1.getPrice().lastIndexOf("@@")));
                                    selectServiceName = switchService1.getName();
                                    getItems();
                                    if (serviceCardId > 0) {
                                        tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                    } else {
                                        if (isVip) {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                        } else {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                        }
                                    }
                                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
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
                                    selectServiceId = switchService2.getServiceId();
                                    if (Utils.isStrNull(switchService2.getVipPrice())) {
                                        selectVipPrice = Double.parseDouble(switchService2.getVipPrice().substring(switchService2.getVipPrice().indexOf("@@") + "@@".length(),
                                                switchService2.getVipPrice().lastIndexOf("@@")));
                                    }
                                    selectPrice = Double.parseDouble(switchService2.getPrice().substring(switchService2.getPrice().indexOf("@@") + "@@".length(),
                                            switchService2.getPrice().lastIndexOf("@@")));
                                    selectServiceName = switchService2.getName();
                                    getItems();
                                    if (serviceCardId > 0) {
                                        tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                    } else {
                                        if (isVip) {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                        } else {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                        }
                                    }
                                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
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
                                    selectServiceId = switchService3.getServiceId();
                                    if (Utils.isStrNull(switchService3.getVipPrice())) {
                                        selectVipPrice = Double.parseDouble(switchService3.getVipPrice().substring(switchService3.getVipPrice().indexOf("@@") + "@@".length(),
                                                switchService3.getVipPrice().lastIndexOf("@@")));
                                    }
                                    selectPrice = Double.parseDouble(switchService3.getPrice().substring(switchService3.getPrice().indexOf("@@") + "@@".length(),
                                            switchService3.getPrice().lastIndexOf("@@")));
                                    selectServiceName = switchService3.getName();
                                    getItems();
                                    if (serviceCardId > 0) {
                                        tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                    } else {
                                        if (isVip) {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                        } else {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                        }
                                    }
                                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
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
        rlAppointswitchBottom.setVisibility(View.GONE);
        selectServiceId = 0;
        selectPrice = 0;
        selectVipPrice = 0;
        selectServiceName = null;
        Log.e("TAG", "event = " + event.toString());
        if (event != null) {
            if (event.getIndex() == SwitchServiceItemEvent.SWITCHSERVICE) {
                int position = event.getPosition();
                SwitchServiceItems switchServiceItems1 = localItems.get(position);
                if (switchServiceItems1 != null && Utils.isStrNull(switchServiceItems1.getOnlyTidDesc())) {
                    ToastUtil.showToastShortCenter(SwitchServiceActivity.this, switchServiceItems1.getOnlyTidDesc());
                    return;
                }
                if (localItems != null && localItems.size() > 0 && localItems.size() > position) {
                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
                    for (int i = 0; i < localItems.size(); i++) {
                        SwitchServiceItems switchServiceItems = localItems.get(i);
                        if (switchServiceItems != null) {
                            switchServiceItems.setSelect(false);
                        }
                    }
                    localItems.get(position).setSelect(true);
                    selectServiceId = localItems.get(position).getServiceId();
                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
                    selectVipPrice = Double.parseDouble(localItems.get(position).getVipPrice().substring(localItems.get(position).getVipPrice().indexOf("@@") + "@@".length(),
                            localItems.get(position).getVipPrice().lastIndexOf("@@")));
                    selectPrice = Double.parseDouble(localItems.get(position).getPrice().substring(localItems.get(position).getPrice().indexOf("@@") + "@@".length(),
                            localItems.get(position).getPrice().lastIndexOf("@@")));
                    selectServiceName = localItems.get(position).getName();
                    if (serviceCardId > 0) {
                        tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                    } else {
                        if (isVip) {
                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                        } else {
                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                        }
                    }
                    allSwitchServiceAdapter.notifyDataSetChanged();
                    getItems();
                }
            }
        }
    }

    private String getItemIds(List<ApointMentItem> itemList) {
        StringBuffer sb = new StringBuffer();
        if (itemList != null && itemList.size() > 0) {
            List<ApointMentItem> itemList1 = new ArrayList<ApointMentItem>();
            itemList1.clear();
            for (int i = 0; i < itemList.size(); i++) {
                ApointMentItem apointMentItem = itemList.get(i);
                if (apointMentItem != null && !apointMentItem.isShouXi()) {
                    itemList1.add(apointMentItem);
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

    // 加载单项
    private void getItems() {
        localItemList.clear();
        localItemPriceList.clear();
        mPDialog.showDialog();
        CommUtil.queryExtraItems(this, shopId, serviceLoc, pet.getPetId() + "_" + selectServiceId + "_" + pet.getMyPetId() + "_" + getItemIds(pet.getItemList()), serviceCardId
                , appointment, queryExtraItemsHandler);
    }

    private AsyncHttpResponseHandler queryExtraItemsHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("items") && !jdata.isNull("items")) {
                            JSONArray jsonArray = jdata.getJSONArray("items");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    localItemList.add(ApointMentItem
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("prices") && !jdata.isNull("prices")) {
                            JSONArray jsonArray = jdata.getJSONArray("prices");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    localItemPriceList.add(ApointMentItemPrice
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(
                                SwitchServiceActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getItems()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(SwitchServiceActivity.this,
                        "数据异常");
                e.printStackTrace();
            }
            Log.e("TAG", "tid = " + tid);
            if (localItemList.size() > 0) {
                for (int i = 0; i < localItemList.size(); i++) {
                    ApointMentItem apointMentItem = localItemList.get(i);
                    for (int j = 0; j < localItemPriceList.size(); j++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                        List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                        if (apointMentItem.getId() == apointMentItemPrice.getItemId() && itemPriceList != null
                                && itemPriceList.size() > 0) {
                            List<Integer> availableMyPets = apointMentItem.getAvailableMyPets();
                            if (availableMyPets == null) {
                                availableMyPets = new ArrayList<Integer>();
                            }
                            availableMyPets.clear();
                            for (int k = 0; k < itemPriceList.size(); k++) {
                                Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                if (apointMentItemPrices != null) {
                                    if (isVip) {
                                        if (tid == 0) {
                                            if (apointMentItemPrices.getVipPrice10() >= 0 || apointMentItemPrices.getVipPrice20() >= 0 || apointMentItemPrices.getVipPrice30() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            }
                                        } else {
                                            if (tid == 1 && apointMentItemPrices.getVipPrice10() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            } else if (tid == 2 && apointMentItemPrices.getVipPrice20() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            } else if (tid == 3 && apointMentItemPrices.getVipPrice30() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            }
                                        }
                                    } else {
                                        if (tid == 0) {
                                            if (apointMentItemPrices.getPrice10() >= 0 || apointMentItemPrices.getPrice20() >= 0 || apointMentItemPrices.getPrice30() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            }
                                        } else {
                                            if (tid == 1 && apointMentItemPrices.getPrice10() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            } else if (tid == 2 && apointMentItemPrices.getPrice20() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            } else if (tid == 3 && apointMentItemPrices.getPrice30() >= 0) {
                                                availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                            }
                                        }
                                    }
                                }
                            }
                            apointMentItem.setAvailableMyPets(availableMyPets);
                        }
                    }
                }
                //循环出不支持的单项，打标
                if (petItemList != null && petItemList.size() > 0) {
                    for (int j = 0; j < petItemList.size(); j++) {
                        boolean isSupport = false;
                        ApointMentItem apointMentItem = petItemList.get(j);
                        for (int k = 0; k < localItemList.size(); k++) {
                            ApointMentItem apointMentItem1 = localItemList.get(k);
                            if (apointMentItem1.getId() == apointMentItem.getId()) {
                                isSupport = true;
                                break;
                            }
                        }
                        if (!isSupport) {
                            apointMentItem.setDelete1(true);
                        }
                    }
                    //循环删除不支持的单项
                    Iterator<ApointMentItem> it = petItemList.iterator();
                    while (it.hasNext()) {
                        ApointMentItem apointMentItem = it.next();
                        if (apointMentItem != null && apointMentItem.isDelete1()) {
                            it.remove();
                        }
                    }
                }
                //继续循环出不支持的单项，打标
                if (petItemList != null && petItemList.size() > 0) {
                    for (int j = 0; j < petItemList.size(); j++) {
                        boolean isSupport = false;
                        ApointMentItem apointMentItem = petItemList.get(j);
                        for (int k = 0; k < localItemList.size(); k++) {
                            ApointMentItem apointMentItem1 = localItemList.get(k);
                            if (apointMentItem1.getId() == apointMentItem.getId()) {
                                List<Integer> availableMyPets = apointMentItem1.getAvailableMyPets();
                                if (availableMyPets != null && availableMyPets.size() > 0) {
                                    for (int x = 0; x < availableMyPets.size(); x++) {
                                        Integer integer = availableMyPets.get(x);
                                        if (integer == pet.getMyPetId()) {
                                            isSupport = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!isSupport) {
                            apointMentItem.setDelete1(true);
                        }
                    }
                    //继续循环删除不支持的单项
                    Iterator<ApointMentItem> it = petItemList.iterator();
                    while (it.hasNext()) {
                        ApointMentItem apointMentItem = it.next();
                        if (apointMentItem != null && apointMentItem.isDelete1()) {
                            it.remove();
                        }
                    }
                }
                //切换等级之后，之前赠送的单项需要剔除
                if (petItemList != null && petItemList.size() > 0) {
                    for (int j = 0; j < petItemList.size(); j++) {
                        ApointMentItem apointMentItem1 = petItemList.get(j);
                        if (apointMentItem1.isShouXi()) {//说明这个单项是赠送的
                            if (isVip) {
                                if (tid == 0 || tid == 1) {
                                    if (apointMentItem1.getVipPrice10() > 0) {
                                        apointMentItem1.setDelete1(true);
                                    }
                                } else if (tid == 2) {
                                    if (apointMentItem1.getVipPrice20() > 0) {
                                        apointMentItem1.setDelete1(true);
                                    }
                                } else if (tid == 3) {
                                    if (apointMentItem1.getVipPrice30() > 0) {
                                        apointMentItem1.setDelete1(true);
                                    }
                                }
                            } else {
                                if (tid == 0 || tid == 1) {
                                    if (apointMentItem1.getPrice10() > 0) {
                                        apointMentItem1.setDelete1(true);
                                    }
                                } else if (tid == 2) {
                                    if (apointMentItem1.getPrice20() > 0) {
                                        apointMentItem1.setDelete1(true);
                                    }
                                } else if (tid == 3) {
                                    if (apointMentItem1.getPrice30() > 0) {
                                        apointMentItem1.setDelete1(true);
                                    }
                                }
                            }
                        }
                    }
                }
                //继续循环删除不赠送的单项
                if (petItemList != null && petItemList.size() > 0) {
                    Iterator<ApointMentItem> it = petItemList.iterator();
                    while (it.hasNext()) {
                        ApointMentItem apointMentItem = it.next();
                        if (apointMentItem != null && apointMentItem.isDelete1()) {
                            it.remove();
                        }
                    }
                }
                //更新宠物添加的单项数据
                if (petItemList.size() > 0) {
                    shouXiItemList1.clear();
                    for (int j = 0; j < petItemList.size(); j++) {
                        ApointMentItem apointMentItem1 = petItemList.get(j);
                        for (int x = 0; x < localItemPriceList.size(); x++) {
                            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(x);
                            List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                            if (itemPriceList != null && itemPriceList.size() > 0) {
                                if (apointMentItem1.getId() == apointMentItemPrice.getItemId()) {
                                    for (int k = 0; k < itemPriceList.size(); k++) {
                                        Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                        if (apointMentItemPrices.getMyPetId() == pet.getMyPetId()) {
                                            apointMentItem1.setVipPrice10(apointMentItemPrices.getVipPrice10());
                                            apointMentItem1.setVipPrice20(apointMentItemPrices.getVipPrice20());
                                            apointMentItem1.setVipPrice30(apointMentItemPrices.getVipPrice30());
                                            apointMentItem1.setPrice10(apointMentItemPrices.getPrice10());
                                            apointMentItem1.setPrice20(apointMentItemPrices.getPrice20());
                                            apointMentItem1.setPrice30(apointMentItemPrices.getPrice30());
                                            if (isVip) {
                                                if (tid == 0 || tid == 1) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice10());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice10());
                                                    if (apointMentItemPrices.getVipPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice20());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice20());
                                                    if (apointMentItemPrices.getVipPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice30());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice30());
                                                    if (apointMentItemPrices.getVipPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (tid == 0 || tid == 1) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice10());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice10());
                                                    if (apointMentItemPrices.getPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice20());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice20());
                                                    if (apointMentItemPrices.getPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice30());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice30());
                                                    if (apointMentItemPrices.getPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    for (int k = 0; k < itemPriceList.size(); k++) {
                                        Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                        if (apointMentItemPrices.getMyPetId() == pet.getMyPetId()) {
                                            if (isVip) {
                                                if (tid == 0 || tid == 1) {
                                                    if (apointMentItemPrices.getVipPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(),
                                                                        apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(),
                                                                    apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    if (apointMentItemPrices.getVipPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    if (apointMentItemPrices.getVipPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (tid == 0 || tid == 1) {
                                                    if (apointMentItemPrices.getPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    if (apointMentItemPrices.getPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    if (apointMentItemPrices.getPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Log.e("TAG", "shouXiItemList1.size() = " + shouXiItemList1.size());
                    Log.e("TAG", "shouXiItemList1.toString() = " + shouXiItemList1.toString());
                    if (shouXiItemList1.size() > 0) {
                        for (int j = 0; j < shouXiItemList1.size(); j++) {
                            ApointMentItem apointMentItem = shouXiItemList1.get(j);
                            for (int k = 0; k < petItemList.size(); k++) {
                                ApointMentItem apointMentItem1 = petItemList.get(k);
                                if (apointMentItem.getId() == apointMentItem1.getId()) {
                                    apointMentItem.setDelete1(true);
                                }
                            }
                        }
                        //循环删除不支持的单项
                        Iterator<ApointMentItem> it = shouXiItemList1.iterator();
                        while (it.hasNext()) {
                            ApointMentItem apointMentItem = it.next();
                            if (apointMentItem != null && apointMentItem.isDelete1()) {
                                it.remove();
                            }
                        }
                        Log.e("TAG", "shouXiItemList1.size() = " + shouXiItemList1.size());
                        Log.e("TAG", "shouXiItemList1.toString() = " + shouXiItemList1.toString());
                        petItemList.addAll(shouXiItemList1);
                        for (int j = 0; j < petItemList.size(); j++) {
                            ApointMentItem apointMentItem = petItemList.get(j);
                            for (int k = 0; k < localItemList.size(); k++) {
                                ApointMentItem apointMentItem1 = localItemList.get(k);
                                if (apointMentItem1.getId() == apointMentItem.getId()) {
                                    apointMentItem.setName(apointMentItem1.getName());
                                    apointMentItem.setPic(apointMentItem1.getPic());
                                }
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < localItemPriceList.size(); j++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                        List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                        if (itemPriceList != null && itemPriceList.size() > 0) {
                            for (int k = 0; k < itemPriceList.size(); k++) {
                                Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                if (apointMentItemPrices.getMyPetId() == pet.getMyPetId()) {
                                    if (isVip) {
                                        if (tid == 0 || tid == 1) {
                                            if (apointMentItemPrices.getVipPrice10() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(),
                                                                apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(),
                                                            apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 2) {
                                            if (apointMentItemPrices.getVipPrice20() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 3) {
                                            if (apointMentItemPrices.getVipPrice30() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        }
                                    } else {
                                        if (tid == 0 || tid == 1) {
                                            if (apointMentItemPrices.getPrice10() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 2) {
                                            if (apointMentItemPrices.getPrice20() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 3) {
                                            if (apointMentItemPrices.getPrice30() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    petItemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //更新宠物添加的单项详细数据
                    if (petItemList.size() > 0) {
                        for (int j = 0; j < petItemList.size(); j++) {
                            ApointMentItem apointMentItem = petItemList.get(j);
                            for (int k = 0; k < localItemList.size(); k++) {
                                ApointMentItem apointMentItem1 = localItemList.get(k);
                                if (apointMentItem1.getId() == apointMentItem.getId()) {
                                    apointMentItem.setName(apointMentItem1.getName());
                                    apointMentItem.setPic(apointMentItem1.getPic());
                                }
                            }
                        }
                    }
                }
                //更新单项列表数据
                for (int i = 0; i < localItemList.size(); i++) {
                    int num = 0;
                    int shouXiNum = 0;
                    int teethCardNum = 0;
                    ApointMentItem apointMentItem = localItemList.get(i);
                    List<Integer> availableMyPets = apointMentItem.getAvailableMyPets();
                    if (availableMyPets != null && availableMyPets.size() > 0) {
                        for (int j = 0; j < localItemPriceList.size(); j++) {
                            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                            List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                            if (apointMentItem.getId() == apointMentItemPrice.getItemId() && itemPriceList != null
                                    && itemPriceList.size() > 0) {
                                for (int k = 0; k < itemPriceList.size(); k++) {
                                    Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                    ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                    if (apointMentItemPrices != null) {
                                        if (isVip) {
                                            if (tid == 0) {
                                                if (apointMentItemPrices.getVipPrice10() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                }
                                            } else {
                                                if (tid == 1 && apointMentItemPrices.getVipPrice10() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                } else if (tid == 2 && apointMentItemPrices.getVipPrice20() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                } else if (tid == 3 && apointMentItemPrices.getVipPrice30() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if (tid == 0) {
                                                if (apointMentItemPrices.getPrice10() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                }
                                            } else {
                                                if (tid == 1 && apointMentItemPrices.getPrice10() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                } else if (tid == 2 && apointMentItemPrices.getPrice20() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                } else if (tid == 3 && apointMentItemPrices.getPrice30() == 0) {
                                                    if (apointMentItemPrices.getExtraCardId() > 0) {
                                                        teethCardNum++;
                                                    } else {
                                                        shouXiNum++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (teethCardNum > 0) {
                            apointMentItem.setTeethCard(true);
                        }
                        if (shouXiNum > 0) {
                            apointMentItem.setState(2);
                            apointMentItem.setShouXi(true);
                        } else {
                            if (petItemList != null && petItemList.size() > 0) {
                                for (int k = 0; k < petItemList.size(); k++) {
                                    ApointMentItem apointMentItem1 = petItemList.get(k);
                                    if (apointMentItem1.getId() == apointMentItem.getId()) {
                                        num++;
                                    }
                                }
                            }
                            if (num == availableMyPets.size()) {
                                apointMentItem.setState(4);
                            } else {
                                apointMentItem.setState(3);
                            }
                        }
                    } else {
                        apointMentItem.setState(1);
                    }
                }
                //更新单项列表价格数据
                for (int j = 0; j < localItemList.size(); j++) {
                    ApointMentItem apointMentItem = localItemList.get(j);
                    for (int k = 0; k < localItemPriceList.size(); k++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(k);
                        if (apointMentItem.getId() == apointMentItemPrice.getItemId()) {
                            List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                            if (itemPriceList != null && itemPriceList.size() > 0) {
                                for (int x = 0; x < itemPriceList.size(); x++) {
                                    ApointMentItemPrices apointMentItemPrices = itemPriceList.get(x);
                                    if (apointMentItemPrices.getMyPetId() == pet.getMyPetId()) {
                                        if (tid == 0 || tid == 1) {
                                            if (apointMentItemPrices.getPrice10() >= 0) {
                                                Log.e("TAG", "非vip中级价格 = " + apointMentItemPrices.getPrice10());
                                                apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                                            }
                                            if (apointMentItemPrices.getVipPrice10() >= 0) {
                                                Log.e("TAG", "vip中级价格 = " + apointMentItemPrices.getVipPrice10());
                                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                                            }
                                        } else if (tid == 2) {
                                            if (apointMentItemPrices.getPrice20() >= 0) {
                                                Log.e("TAG", "非vip高级价格 = " + apointMentItemPrices.getPrice20());
                                                apointMentItem.setPrice(apointMentItemPrices.getPrice20());
                                            }
                                            if (apointMentItemPrices.getVipPrice20() >= 0) {
                                                Log.e("TAG", "vip高级价格 = " + apointMentItemPrices.getVipPrice20());
                                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice20());
                                            }
                                        } else if (tid == 3) {
                                            if (apointMentItemPrices.getPrice30() >= 0) {
                                                Log.e("TAG", "非vip首席价格 = " + apointMentItemPrices.getPrice30());
                                                apointMentItem.setPrice(apointMentItemPrices.getPrice30());
                                            }
                                            if (apointMentItemPrices.getVipPrice30() >= 0) {
                                                Log.e("TAG", "vip首席价格 = " + apointMentItemPrices.getVipPrice30());
                                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice30());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //循环删除不支持的单项
                Iterator<ApointMentItem> it = localItemList.iterator();
                while (it.hasNext()) {
                    ApointMentItem apointMentItem = it.next();
                    if (apointMentItem != null) {
                        if (apointMentItem.getAvailableMyPets() != null && apointMentItem.getAvailableMyPets().size() > 0) {

                        } else {
                            it.remove();
                        }
                    }
                }
                if (localItemList.size() > 0) {
                    showItemBottomDialog();
                } else {
                    petItemList.clear();
                }
                Log.e("TAG", "localItemList = " + localItemList.toString());
                Log.e("TAG", "localItemPriceList = " + localItemPriceList.toString());
            } else {//删除不支持的单项
                petItemList.clear();
            }
            if (serviceCardId > 0) {
                tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
            } else {
                if (isVip) {
                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                } else {
                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                }
            }
            isFirstAddToothCardItem = false;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(SwitchServiceActivity.this, "请求失败");
            mPDialog.closeDialog();
        }
    };

    private void showItemBottomDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(SwitchServiceActivity.this, R.layout.switchservice_item_bottom_dialog, null);
        RecyclerView rv_switchservice_item_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_switchservice_item_bottomdia);
        ImageView iv_switchservice_item_bottom_bg = (ImageView) customView.findViewById(R.id.iv_switchservice_item_bottom_bg);
        TextView tv_switchservice_item_bottomdia_wc = (TextView) customView.findViewById(R.id.tv_switchservice_item_bottomdia_wc);
        rv_switchservice_item_bottomdia.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_switchservice_item_bottomdia.setLayoutManager(linearLayoutManager);
        switchServiceBottomItemAdapter = new SwitchServiceBottomItemAdapter(R.layout.item_appointment_item, localItemList, tid);
        rv_switchservice_item_bottomdia.setAdapter(switchServiceBottomItemAdapter);
        switchServiceBottomItemAdapter.setOnItemAddListener(new SwitchServiceBottomItemAdapter.OnItemAddListener() {
            @Override
            public void OnItemAdd(int position) {
                if (localItemList.size() > position) {
                    ApointMentItem apointMentItem1 = localItemList.get(position);
                    for (int j = 0; j < localItemPriceList.size(); j++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                        List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                        if (apointMentItemPrice.getItemId() == apointMentItem1.getId()) {
                            if (itemPriceList != null && itemPriceList.size() > 0) {
                                for (int k = 0; k < itemPriceList.size(); k++) {
                                    ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                    if (apointMentItemPrices.getMyPetId() == pet.getMyPetId()) {
                                        apointMentItem1.setVipPrice10(apointMentItemPrices.getVipPrice10());
                                        apointMentItem1.setVipPrice20(apointMentItemPrices.getVipPrice20());
                                        apointMentItem1.setVipPrice30(apointMentItemPrices.getVipPrice30());
                                        apointMentItem1.setPrice10(apointMentItemPrices.getPrice10());
                                        apointMentItem1.setPrice20(apointMentItemPrices.getPrice20());
                                        apointMentItem1.setPrice30(apointMentItemPrices.getPrice30());
                                    }
                                }
                            }
                        }
                    }
                    if (apointMentItem1.getState() == 3) {
                        apointMentItem1.setState(4);
                    } else if (apointMentItem1.getState() == 4) {
                        apointMentItem1.setState(3);
                    }
                    switchServiceBottomItemAdapter.notifyDataSetChanged();
                }
            }
        });
        switchServiceBottomItemAdapter.setOnPetItemClickListener(new SwitchServiceBottomItemAdapter.OnPetItemClickListener() {
            @Override
            public void OnPetItemClick(int position) {
                if (localItemList.size() > position) {
                    Intent intent = new Intent(SwitchServiceActivity.this, ItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    ApointMentItem apointMentItem1 = localItemList.get(position);
                    int id = apointMentItem1.getId();
                    for (int i = 0; i < localItemPriceList.size(); i++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
                        if (apointMentItemPrice.getItemId() == id) {
                            bundle.putSerializable("apointMentItemPrice", apointMentItemPrice);
                            break;
                        }
                    }
                    bundle.putSerializable("Item", localItemList.get(position));
                    bundle.putSerializable("appointWorker", localAppointWorker);
                    bundle.putSerializable("shop", shop);
                    intent.putExtras(bundle);
                    intent.putExtra("previous",
                            Global.SWITCHSERVICE_TO_ITEMDETAIL);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("serviceCardId", serviceCardId);
                    intent.putExtra("dayposition", dayposition);
                    intent.putExtra("serviceLoc", serviceLoc);
                    intent.putExtra("appointment", appointment);
                    intent.putExtra("petList", (Serializable) petList);
                    startActivityForResult(intent, Global.SWITCHSERVICE_TO_ITEMDETAIL);
                }
            }
        });
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(SwitchServiceActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_switchservice_item_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        tv_switchservice_item_bottomdia_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                petItemList.clear();
                for (int i = 0; i < petList.size(); i++) {//清空所有添加了这个单项的宠物
                    ApointMentPet apointMentPet = petList.get(i);
                    if (apointMentPet.getMyPetId() == pet.getMyPetId()) {
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            itemList.clear();
                            apointMentPet.setItemList(itemList);
                        }
                        break;
                    }
                }
                setOnePetItem();
            }
        });
    }

    private void setOnePetItem() {
        for (int i = 0; i < localItemList.size(); i++) {
            ApointMentItem apointMentItem = localItemList.get(i);
            if (apointMentItem.getState() == 4) {
                petItemList.add(apointMentItem);
            }
        }
        if (serviceCardId > 0) {
            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
        } else {
            if (isVip) {
                tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
            } else {
                tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.SWITCHSERVICE_TO_ITEMDETAIL) {//单项详情选择单项返回
                isOverTime = data.getBooleanExtra("isOverTime", false);
                ApointMentItem intentItem = (ApointMentItem) data.getSerializableExtra("item");
                if (isOverTime) {
                    appointment = data.getStringExtra("appointment");
                    dayposition = data.getIntExtra("dayposition", 0);
                    pickup = data.getIntExtra("pickup", 0);
                    localAppointWorker = (AppointWorker) data.getSerializableExtra("localAppointWorker");
                    itemToMorePet = (ItemToMorePet) data.getSerializableExtra("itemToMorePet");
                }
                for (int i = 0; i < localItemList.size(); i++) {
                    ApointMentItem apointMentItem = localItemList.get(i);
                    if (apointMentItem.getId() == intentItem.getId()) {
                        apointMentItem.setState(intentItem.getState());
                        break;
                    }
                }
                switchServiceBottomItemAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setLinster() {
    }

    private void getData() {
        selectServiceId = 0;
        selectPrice = 0;
        selectVipPrice = 0;
        selectServiceName = null;
        localItems.clear();
        rlAppointswitchBottom.setVisibility(View.GONE);
        mPDialog.showDialog();
        list.clear();
        CommUtil.switchService(this, petId, shopId, serviceLoc, appointment,
                serviceId, workerId, serviceCardId, switchServiceHanler);
    }

    private AsyncHttpResponseHandler switchServiceHanler = new AsyncHttpResponseHandler() {
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
                    if (list.size() <= 0) {
                        allSwitchServiceAdapter.setEmptyView(setEmptyViewBase(2, "暂无可切换服务哦~",
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
                //默认选中本来的服务
                for (int i = 0; i < allSwitchServiceList.size(); i++) {
                    AllSwitchService allSwitchService = allSwitchServiceList.get(i);
                    if (allSwitchService != null) {
                        SwitchService switchService1 = allSwitchService.getSwitchService1();
                        SwitchService switchService2 = allSwitchService.getSwitchService2();
                        SwitchService switchService3 = allSwitchService.getSwitchService3();
                        if (switchService1 != null) {
                            List<SwitchServiceItems> items = switchService1.getItems();
                            if (items != null && items.size() > 0) {
                                for (int j = 0; j < items.size(); j++) {
                                    SwitchServiceItems switchServiceItems = items.get(j);
                                    if (switchServiceItems != null) {
                                        if (serviceId == switchServiceItems.getServiceId()) {
                                            allSwitchService.setSelect1(true);
                                            localItems.clear();
                                            localItems.addAll(items);
                                            rlAppointswitchBottom.setVisibility(View.VISIBLE);
                                            switchServiceItems.setSelect(true);
                                            selectServiceId = switchServiceItems.getServiceId();
                                            selectVipPrice = Double.parseDouble(switchServiceItems.getVipPrice().substring(switchServiceItems.getVipPrice().indexOf("@@") + "@@".length(),
                                                    switchServiceItems.getVipPrice().lastIndexOf("@@")));
                                            selectPrice = Double.parseDouble(switchServiceItems.getPrice().substring(switchServiceItems.getPrice().indexOf("@@") + "@@".length(),
                                                    switchServiceItems.getPrice().lastIndexOf("@@")));
                                            selectServiceName = switchServiceItems.getName();
                                            if (serviceCardId > 0) {
                                                tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                            } else {
                                                if (isVip) {
                                                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                                } else {
                                                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if (serviceId == switchService1.getServiceId()) {
                                    allSwitchService.setSelect1(true);
                                    selectServiceId = switchService1.getServiceId();
                                    if (Utils.isStrNull(switchService1.getVipPrice())) {
                                        selectVipPrice = Double.parseDouble(switchService1.getVipPrice().substring(switchService1.getVipPrice().indexOf("@@") + "@@".length(),
                                                switchService1.getVipPrice().lastIndexOf("@@")));
                                    }
                                    selectPrice = Double.parseDouble(switchService1.getPrice().substring(switchService1.getPrice().indexOf("@@") + "@@".length(),
                                            switchService1.getPrice().lastIndexOf("@@")));
                                    selectServiceName = switchService1.getName();
                                    if (serviceCardId > 0) {
                                        tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                    } else {
                                        if (isVip) {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                        } else {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                        }
                                    }
                                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }

                        if (switchService2 != null) {
                            List<SwitchServiceItems> items = switchService2.getItems();
                            if (items != null && items.size() > 0) {
                                for (int j = 0; j < items.size(); j++) {
                                    SwitchServiceItems switchServiceItems = items.get(j);
                                    if (switchServiceItems != null) {
                                        if (serviceId == switchServiceItems.getServiceId()) {
                                            allSwitchService.setSelect2(true);
                                            localItems.clear();
                                            localItems.addAll(items);
                                            rlAppointswitchBottom.setVisibility(View.VISIBLE);
                                            switchServiceItems.setSelect(true);
                                            selectServiceId = switchServiceItems.getServiceId();
                                            selectVipPrice = Double.parseDouble(switchServiceItems.getVipPrice().substring(switchServiceItems.getVipPrice().indexOf("@@") + "@@".length(),
                                                    switchServiceItems.getVipPrice().lastIndexOf("@@")));
                                            selectPrice = Double.parseDouble(switchServiceItems.getPrice().substring(switchServiceItems.getPrice().indexOf("@@") + "@@".length(),
                                                    switchServiceItems.getPrice().lastIndexOf("@@")));
                                            selectServiceName = switchServiceItems.getName();
                                            if (serviceCardId > 0) {
                                                tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                            } else {
                                                if (isVip) {
                                                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                                } else {
                                                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if (serviceId == switchService2.getServiceId()) {
                                    allSwitchService.setSelect2(true);
                                    selectServiceId = switchService2.getServiceId();
                                    if (Utils.isStrNull(switchService2.getVipPrice())) {
                                        selectVipPrice = Double.parseDouble(switchService2.getVipPrice().substring(switchService2.getVipPrice().indexOf("@@") + "@@".length(),
                                                switchService2.getVipPrice().lastIndexOf("@@")));
                                    }
                                    selectPrice = Double.parseDouble(switchService2.getPrice().substring(switchService2.getPrice().indexOf("@@") + "@@".length(),
                                            switchService2.getPrice().lastIndexOf("@@")));
                                    selectServiceName = switchService2.getName();
                                    if (serviceCardId > 0) {
                                        tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                    } else {
                                        if (isVip) {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                        } else {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                        }
                                    }
                                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }

                        if (switchService3 != null) {
                            List<SwitchServiceItems> items = switchService3.getItems();
                            if (items != null && items.size() > 0) {
                                for (int j = 0; j < items.size(); j++) {
                                    SwitchServiceItems switchServiceItems = items.get(j);
                                    if (switchServiceItems != null) {
                                        if (serviceId == switchServiceItems.getServiceId()) {
                                            allSwitchService.setSelect3(true);
                                            localItems.clear();
                                            localItems.addAll(items);
                                            rlAppointswitchBottom.setVisibility(View.VISIBLE);
                                            switchServiceItems.setSelect(true);
                                            selectServiceId = switchServiceItems.getServiceId();
                                            selectVipPrice = Double.parseDouble(switchServiceItems.getVipPrice().substring(switchServiceItems.getVipPrice().indexOf("@@") + "@@".length(),
                                                    switchServiceItems.getVipPrice().lastIndexOf("@@")));
                                            selectPrice = Double.parseDouble(switchServiceItems.getPrice().substring(switchServiceItems.getPrice().indexOf("@@") + "@@".length(),
                                                    switchServiceItems.getPrice().lastIndexOf("@@")));
                                            selectServiceName = switchServiceItems.getName();
                                            if (serviceCardId > 0) {
                                                tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                            } else {
                                                if (isVip) {
                                                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                                } else {
                                                    tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if (serviceId == switchService3.getServiceId()) {
                                    allSwitchService.setSelect3(true);
                                    selectServiceId = switchService3.getServiceId();
                                    if (Utils.isStrNull(switchService3.getVipPrice())) {
                                        selectVipPrice = Double.parseDouble(switchService3.getVipPrice().substring(switchService3.getVipPrice().indexOf("@@") + "@@".length(),
                                                switchService3.getVipPrice().lastIndexOf("@@")));
                                    }
                                    selectPrice = Double.parseDouble(switchService3.getPrice().substring(switchService3.getPrice().indexOf("@@") + "@@".length(),
                                            switchService3.getPrice().lastIndexOf("@@")));
                                    selectServiceName = switchService3.getName();
                                    if (serviceCardId > 0) {
                                        tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                    } else {
                                        if (isVip) {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectVipPrice, getItemPrice(true)));
                                        } else {
                                            tvAppointswitchPrice.setText("¥" + ComputeUtil.add(selectPrice, getItemPrice(false)));
                                        }
                                    }
                                    rlAppointswitchBottom.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }
                    }
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void exit() {
        if (isChange()) {
            new AlertDialogNavAndPost(this).builder().setTitle("").setMsg("检测到您更换了服务，是否保存更换的服务").setNegativeButton("否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).setPositiveButton("是", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isStrNull(selectServiceName)) {
                        pet.setServiceName(selectServiceName);
                    }
                    if (selectServiceId > 0) {
                        pet.setServiceId(selectServiceId);
                    }
                    if (selectPrice > 0) {
                        pet.setPrice(selectPrice);
                    }
                    if (selectVipPrice > 0) {
                        pet.setVipPrice(selectVipPrice);
                    }
                    pet.setItemList(petItemList);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pet", pet);
                    if (isOverTime) {
                        bundle.putSerializable("workerAndTime", new WorkerAndTime(localAppointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                    }
                    intent.putExtras(bundle);
                    setResult(Global.RESULT_OK, intent);
                    finish();
                }
            }).show();
        } else {
            finish();
        }
    }

    private boolean isChange() {
        boolean isChange = false;
        if (selectServiceId > 0 && serviceId != selectServiceId) {
            isChange = true;
        } else {
            if (petItemList.size() > 0 && petItemList1.size() <= 0) {
                isChange = true;
            } else if (petItemList.size() <= 0 && petItemList1.size() > 0) {
                isChange = true;
            } else if (petItemList.size() > 0 && petItemList1.size() > 0) {
                if (petItemList.size() != petItemList1.size()) {
                    isChange = true;
                } else {
                    isChange = isItemChange(petItemList, petItemList1);
                }
            }
        }
        return isChange;
    }

    private boolean isItemChange(List<ApointMentItem> itemList, List<ApointMentItem> itemList1) {
        boolean isChange = true;
        for (int i = 0; i < itemList.size(); i++) {
            ApointMentItem apointMentItem = itemList.get(i);
            for (int j = 0; j < itemList1.size(); j++) {
                ApointMentItem apointMentItem1 = itemList1.get(j);
                if (apointMentItem.getId() == apointMentItem1.getId()) {
                    isChange = false;
                    break;
                }
            }
        }
        return isChange;
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_appointswitch_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                exit();
                break;
            case R.id.tv_appointswitch_submit:
                if (Utils.isStrNull(selectServiceName)) {
                    pet.setServiceName(selectServiceName);
                }
                if (selectServiceId > 0) {
                    pet.setServiceId(selectServiceId);
                }
                if (selectPrice > 0) {
                    pet.setPrice(selectPrice);
                }
                if (selectVipPrice > 0) {
                    pet.setVipPrice(selectVipPrice);
                }
                pet.setItemList(petItemList);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("pet", pet);
                if (isOverTime) {
                    bundle.putSerializable("workerAndTime", new WorkerAndTime(localAppointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                }
                intent.putExtras(bundle);
                setResult(Global.RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SwitchServiceWorkerAndTimeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            isOverTime = true;
            appointment = event.getAppointment();
            localAppointWorker = event.getAppointWorker();
            dayposition = event.getDayposition();
            itemToMorePet = event.getItemToMorePet();
            workerIds = event.getWorkerIds();
            pickup = event.getPickup();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
