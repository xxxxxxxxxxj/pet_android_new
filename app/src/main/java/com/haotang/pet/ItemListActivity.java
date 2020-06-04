package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AppointBottomPetAdapter;
import com.haotang.pet.adapter.ItemListAdapter;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.ApointMentItemPrice;
import com.haotang.pet.entity.ApointMentItemPrices;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.ItemListWorkerAndTimeEvent;
import com.haotang.pet.entity.ItemToMorePet;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.WorkerAndTime;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 单项列表界面
 */
public class ItemListActivity extends SuperActivity {
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.rv_itemlist)
    RecyclerView rvItemlist;
    private List<ApointMentItem> itemList;
    private List<ApointMentItemPrice> localItemPriceList;
    private List<ApointMentPet> petList;
    private List<ApointMentPet> localPetList = new ArrayList<ApointMentPet>();
    private ItemListAdapter itemListAdapter;
    private int tid = 0;//选中美容师级别
    private AppointBottomPetAdapter appointBottomPetAdapter;
    private List<Integer> workerIdList = new ArrayList<Integer>();
    private ApointMentItem apointMentItem1;
    private int flag;
    private List<ApointMentPet> overTimePetList = new ArrayList<ApointMentPet>();
    private ItemToMorePet itemToMorePet = new ItemToMorePet();
    private List<Integer> myPetIds = new ArrayList<Integer>();
    private AppointWorker localAppointWorker;
    private double lat;
    private double lng;
    private String appointment;
    private int serviceLoc;
    private ServiceShopAdd shop;
    private int shopId = 0;
    private int pickup;
    private int dayposition;
    private boolean isOverTime;
    private List<ApointMentItem> timeItemList = new ArrayList<ApointMentItem>();
    private String workerIds;
    private boolean isVip = true;

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
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        pickup = getIntent().getIntExtra("pickup", 0);
        isVip = getIntent().getBooleanExtra("isVip", true);
        dayposition = getIntent().getIntExtra("dayposition", 0);
        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        appointment = getIntent().getStringExtra("appointment");
        localAppointWorker = (AppointWorker) getIntent().getSerializableExtra("appointWorker");
        itemList = (List<ApointMentItem>) getIntent().getSerializableExtra("itemList");
        localItemPriceList = (List<ApointMentItemPrice>) getIntent().getSerializableExtra("itemPriceList");
        petList = (List<ApointMentPet>) getIntent().getSerializableExtra("petList");
        shop = (ServiceShopAdd) getIntent().getSerializableExtra("shop");
        if (localAppointWorker != null) {
            tid = localAppointWorker.getTid();
        } else {
            tid = getIntent().getIntExtra("tid", 0);
        }
        if (shop != null) {
            shopId = shop.shopId;
        }
        if (petList != null && petList.size() > 0) {
            for (int i = 0; i < petList.size(); i++) {
                ApointMentPet apointMentPet = petList.get(i);
                ApointMentPet apointMentPet1 = new ApointMentPet();
                apointMentPet1.setMyPetId(apointMentPet.getMyPetId());
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                List<ApointMentItem> localItemList = null;
                if (itemList != null) {
                    localItemList = new ArrayList<ApointMentItem>();
                    if (itemList.size() > 0) {
                        for (int j = 0; j < itemList.size(); j++) {
                            localItemList.add(new ApointMentItem(itemList.get(j).getId()));
                        }
                    }
                }
                apointMentPet1.setItemList(localItemList);
                localPetList.add(apointMentPet1);
            }
        }
    }

    private void findView() {
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("单项服务");
        tvTitlebarOther.setText("完成");
        tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));
        tvTitlebarOther.setVisibility(View.VISIBLE);
        rvItemlist.setHasFixedSize(true);
        rvItemlist.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemListAdapter(R.layout.item_itemlist, itemList, tid);
        rvItemlist.setAdapter(itemListAdapter);
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

    private String getOverTime_PetID_ServiceId_MyPetId_ItemIds() {
        StringBuffer sb = new StringBuffer();
        if (overTimePetList != null && overTimePetList.size() > 0) {
            for (int i = 0; i < overTimePetList.size(); i++) {
                ApointMentPet apointMentPet = overTimePetList.get(i);
                if (apointMentPet != null) {
                    if (i < overTimePetList.size() - 1) {
                        sb.append(apointMentPet.getPetId());
                        sb.append("_");
                        sb.append(apointMentPet.getServiceId());
                        sb.append("_");
                        sb.append(apointMentPet.getMyPetId());
                        sb.append("_");
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            sb.append(getItemIds(itemList));
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
                            sb.append(getItemIds(itemList));
                        } else {
                            sb.append("0");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private void setLinster() {
        itemListAdapter.setOnItemAddListener(new ItemListAdapter.OnItemAddListener() {
            @Override
            public void OnItemAdd(int position) {
                if (itemList.size() > position) {
                    apointMentItem1 = itemList.get(position);
                    for (int i = 0; i < petList.size(); i++) {
                        ApointMentPet apointMentPet = petList.get(i);
                        for (int j = 0; j < localItemPriceList.size(); j++) {
                            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                            List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                            if (apointMentItemPrice.getItemId() == apointMentItem1.getId()) {
                                if (itemPriceList != null && itemPriceList.size() > 0) {
                                    for (int k = 0; k < itemPriceList.size(); k++) {
                                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                        if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
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
                    }
                    List<Integer> availableMyPets = apointMentItem1.getAvailableMyPets();
                    for (int i = 0; i < petList.size(); i++) {
                        petList.get(i).setState(1);
                        petList.get(i).setSelect(false);
                    }
                    for (int i = 0; i < availableMyPets.size(); i++) {
                        Integer integer = availableMyPets.get(i);
                        for (int j = 0; j < petList.size(); j++) {
                            ApointMentPet apointMentPet = petList.get(j);
                            apointMentPet.setPriceSuffix(apointMentItem1.getPriceSuffix());
                            if (integer == apointMentPet.getMyPetId()) {//宠物支持这个单项
                                List<ApointMentItem> itemList = apointMentPet.getItemList();
                                if (itemList != null && itemList.size() > 0) {
                                    petList.get(j).setState(3);
                                    for (int k = 0; k < itemList.size(); k++) {
                                        ApointMentItem apointMentItem = itemList.get(k);
                                        if (apointMentItem.getId() == apointMentItem1.getId()) {
                                            if (apointMentItem.isShouXi()) {
                                                petList.get(j).setState(2);
                                            } else if (apointMentItem.isTeethCard()) {
                                                petList.get(j).setState(5);
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    petList.get(j).setState(3);
                                }
                            }
                        }
                    }
                    for (int i = 0; i < petList.size(); i++) {
                        ApointMentPet apointMentPet = petList.get(i);
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            boolean isSelect = false;
                            for (int j = 0; j < itemList.size(); j++) {
                                ApointMentItem apointMentItem = itemList.get(j);
                                if (apointMentItem.getId() == apointMentItem1.getId()) {
                                    isSelect = true;
                                    break;
                                }
                            }
                            if (isSelect) {
                                apointMentPet.setSelect(true);
                            } else {
                                apointMentPet.setSelect(false);
                            }
                        } else {
                            apointMentPet.setSelect(false);
                        }
                    }
                    if (petList.size() == 1) {
                        ApointMentPet apointMentPet2 = petList.get(0);
                        if (apointMentPet2.isSelect()) {//减
                            List<ApointMentItem> itemList1 = apointMentPet2.getItemList();
                            for (int i = 0; i < itemList1.size(); i++) {
                                ApointMentItem apointMentItem = itemList1.get(i);
                                if (apointMentItem.getId() == apointMentItem1.getId()) {
                                    itemList1.remove(i);
                                    break;
                                }
                            }
                            apointMentItem1.setState(3);
                            itemListAdapter.notifyDataSetChanged();
                        } else {//加
                            if (Utils.isStrNull(appointment) && localAppointWorker != null) {
                                overTimePetList.clear();
                                for (int i = 0; i < petList.size(); i++) {
                                    ApointMentPet apointMentPet1 = petList.get(i);
                                    ApointMentPet apointMentPet = new ApointMentPet();
                                    apointMentPet.setPriceSuffix(apointMentPet1.getPriceSuffix());
                                    apointMentPet.setPrice(apointMentPet1.getPrice());
                                    apointMentPet.setSelect(apointMentPet1.isSelect());
                                    apointMentPet.setItemPrice(apointMentPet1.getItemPrice());
                                    apointMentPet.setItemVipPrice(apointMentPet1.getItemVipPrice());
                                    apointMentPet.setMyPetId(apointMentPet1.getMyPetId());
                                    apointMentPet.setPetId(apointMentPet1.getPetId());
                                    apointMentPet.setPetImg(apointMentPet1.getPetImg());
                                    apointMentPet.setPetNickName(apointMentPet1.getPetNickName());
                                    apointMentPet.setVipPrice(apointMentPet1.getVipPrice());
                                    apointMentPet.setSetItemDecor(apointMentPet1.isSetItemDecor());
                                    apointMentPet.setServiceId(apointMentPet1.getServiceId());
                                    List<ApointMentItem> itemList = apointMentPet1.getItemList();
                                    if (itemList != null && itemList.size() > 0) {
                                        ArrayList<ApointMentItem> apointMentItems = new ArrayList<>();
                                        for (int j = 0; j < itemList.size(); j++) {
                                            apointMentItems.add(itemList.get(j));
                                        }
                                        apointMentPet.setItemList(apointMentItems);
                                    }
                                    overTimePetList.add(apointMentPet);
                                }
                                for (int i = 0; i < overTimePetList.size(); i++) {
                                    ApointMentPet apointMentPet = overTimePetList.get(i);
                                    if (apointMentPet.getMyPetId() == apointMentPet2.getMyPetId()) {
                                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                                        if (itemList == null) {
                                            itemList = new ArrayList<ApointMentItem>();
                                        }
                                        itemList.add(apointMentItem1);
                                        apointMentPet.setItemList(itemList);
                                        break;
                                    }
                                }
                                myPetIds.clear();
                                myPetIds.add(apointMentPet2.getMyPetId());
                                timeItemList.clear();
                                timeItemList.add(apointMentItem1);
                                itemToMorePet.setApointMentItem(timeItemList);
                                itemToMorePet.setMyPetIds(myPetIds);
                                flag = 1;
                                //如果选择了时间和美容师，需要校验添加这个单项以后，选中的时间还是否可约
                                workerIdList.clear();
                                mPDialog.showDialog();
                                CommUtil.getBeauticianFreeTime(
                                        ItemListActivity.this,
                                        lat,
                                        lng,
                                        spUtil.getString(
                                                "cellphone", ""),
                                        Global.getIMEI(ItemListActivity.this),
                                        Global.getCurrentVersion(ItemListActivity.this),
                                        0, serviceLoc, shopId,
                                        getOverTime_PetID_ServiceId_MyPetId_ItemIds(), 0, appointment, 0, 0,
                                        allWorkerHanler);
                            } else {
                                setOnePetItem();
                            }
                        }
                    } else {
                        showItemPetDialog();
                    }
                }
            }
        });
        itemListAdapter.setOnPetItemClickListener(new ItemListAdapter.OnPetItemClickListener() {
            @Override
            public void OnPetItemClick(int position) {
                if (itemList.size() > position) {
                    Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    ApointMentItem apointMentItem = itemList.get(position);
                    int id = apointMentItem.getId();
                    for (int i = 0; i < localItemPriceList.size(); i++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
                        if (apointMentItemPrice.getItemId() == id) {
                            bundle.putSerializable("apointMentItemPrice", apointMentItemPrice);
                            break;
                        }
                    }
                    bundle.putSerializable("appointWorker", localAppointWorker);
                    bundle.putSerializable("shop", shop);
                    bundle.putSerializable("Item", apointMentItem);
                    intent.putExtras(bundle);
                    intent.putExtra("lat", lat);
                    intent.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                    intent.putExtra("dayposition", dayposition);
                    intent.putExtra("lng", lng);
                    intent.putExtra("serviceLoc", serviceLoc);
                    intent.putExtra("appointment", appointment);
                    intent.putExtra("petList", (Serializable) petList);
                    intent.putExtra("isVip", isVip);
                    intent.putExtra("tid", tid);
                    intent.putExtra("previous", Global.ITEMLIST_TO_ITEMDETAIL);
                    startActivityForResult(intent, Global.ITEMLIST_TO_ITEMDETAIL);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.ITEMLIST_TO_ITEMDETAIL) {//单项详情选择单项返回
                isOverTime = data.getBooleanExtra("isOverTime", false);
                ApointMentItem intentItem = (ApointMentItem) data.getSerializableExtra("item");
                List<ApointMentPet> intentPetList = (List<ApointMentPet>) data.getSerializableExtra("petList");
                Log.e("TAG", "intentItem = " + intentItem);
                Log.e("TAG", "itemList = " + itemList);
                Log.e("TAG", "intentPetList = " + intentPetList);
                if (isOverTime) {
                    appointment = data.getStringExtra("appointment");
                    dayposition = data.getIntExtra("dayposition", 0);
                    pickup = data.getIntExtra("pickup", 0);
                    workerIds = data.getStringExtra("workerIds");
                    localAppointWorker = (AppointWorker) data.getSerializableExtra("localAppointWorker");
                    itemToMorePet = (ItemToMorePet) data.getSerializableExtra("itemToMorePet");
                }
                //更新界面
                for (int i = 0; i < itemList.size(); i++) {
                    ApointMentItem apointMentItem = itemList.get(i);
                    if (apointMentItem.getId() == intentItem.getId()) {
                        apointMentItem.setState(intentItem.getState());
                    }
                }
                itemListAdapter.notifyDataSetChanged();
                //更新PetList数据
                for (int i = 0; i < intentPetList.size(); i++) {
                    ApointMentPet apointMentPet = intentPetList.get(i);
                    for (int j = 0; j < petList.size(); j++) {
                        ApointMentPet apointMentPet1 = petList.get(j);
                        if (apointMentPet1.getMyPetId() == apointMentPet.getMyPetId()) {
                            apointMentPet1.setItemList(apointMentPet.getItemList());
                        }
                    }
                }
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
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    workerIdList.add(jsonArray.getInt(i));
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ItemListActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getSingles()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(ItemListActivity.this, "数据异常");
                e.printStackTrace();
            }
            boolean isSupport = false;
            if (workerIdList.size() > 0) {
                for (int i = 0; i < workerIdList.size(); i++) {
                    Integer integer = workerIdList.get(i);
                    if (integer == localAppointWorker.getWorkerId()) {
                        isSupport = true;
                        break;
                    }
                }
            }
            if (isSupport) {
                if (flag == 1) {//单宠
                    setOnePetItem();
                } else if (flag == 2) {//多宠
                    setMorePetItem();
                }
            } else {
                showTimeOverDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            Log.e("TAG", "请求失败");
            ToastUtil.showToastShortBottom(ItemListActivity.this, "请求失败");
        }
    };

    private void showTimeOverDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(ItemListActivity.this, R.layout.appoint_timeover_bottom_dialog, null);
        TextView tv_appointtimeover_bottomdia_title = (TextView) customView.findViewById(R.id.tv_appointtimeover_bottomdia_title);
        TextView tv_appointtimeover_bottomdia_worker = (TextView) customView.findViewById(R.id.tv_appointtimeover_bottomdia_worker);
        TextView tv_appointtimeover_bottomdia_time = (TextView) customView.findViewById(R.id.tv_appointtimeover_bottomdia_time);
        ImageView iv_appointtimeover_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointtimeover_bottomdia_close);
        ImageView iv_appointtimeover_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointtimeover_bottom_bg);
        tv_appointtimeover_bottomdia_title.setText(localAppointWorker.getRealName() + "当前时间已满");
        tv_appointtimeover_bottomdia_time.setText("预约" + localAppointWorker.getRealName() + "其他时间");
        if (workerIdList.size() > 0) {
            tv_appointtimeover_bottomdia_worker.setBackgroundResource(R.drawable.bg_orange_jianbian);
        } else {
            tv_appointtimeover_bottomdia_worker.setBackgroundResource(R.drawable.bg_bbbbbb);
        }
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(ItemListActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_appointtimeover_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointtimeover_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        tv_appointtimeover_bottomdia_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择此时间段下的其他美容师
                pWinBottomDialog.dismiss();
                isOverTime = false;
                if (workerIdList.size() > 0) {
                    String workerIds = "";
                    for (int i = 0; i < workerIdList.size(); i++) {
                        if (i == workerIdList.size() - 1) {
                            workerIds = workerIds + workerIdList.get(i);
                        } else {
                            workerIds = workerIds + workerIdList.get(i) + ",";
                        }
                    }
                    Intent intent2 = new Intent(ItemListActivity.this, WorkerListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", shop);
                    intent2.putExtras(bundle);
                    intent2.putExtra("lat", lat);
                    intent2.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                    intent2.putExtra("workerIds", workerIds);
                    intent2.putExtra("lng", lng);
                    intent2.putExtra("previous", Global.ITEMLIST_TO_OVERTIME);
                    bundle.putSerializable("itemToMorePet", itemToMorePet);
                    intent2.putExtra("dayposition", dayposition);
                    if (localAppointWorker != null) {
                        intent2.putExtra("defaultWorkerTag", localAppointWorker.getDefaultWorkerTag());
                        intent2.putExtra("workerId", localAppointWorker.getWorkerId());
                    }
                    intent2.putExtra("pickup", pickup);
                    intent2.putExtra("appointment", appointment);
                    intent2.putExtra("serviceLoc", serviceLoc);
                    intent2.putExtra("petList", (Serializable) petList);
                    intent2.putExtra("isVip", isVip);
                    intent2.putExtra("tid", tid);
                    intent2.putExtra("strp", getOverTime_PetID_ServiceId_MyPetId_ItemIds());
                    startActivity(intent2);
                } else {
                    ToastUtil.showToastShortBottom(ItemListActivity.this, "您当前所选时间暂无可约美容师");
                }
            }
        });
        tv_appointtimeover_bottomdia_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择该美容师的其他时间
                pWinBottomDialog.dismiss();
                isOverTime = false;
                Intent intent1 = new Intent(ItemListActivity.this, AppointTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shop", shop);
                intent1.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                if (localAppointWorker != null) {
                    bundle.putSerializable("appointWorker", localAppointWorker);
                }
                bundle.putSerializable("itemToMorePet", itemToMorePet);
                intent1.putExtras(bundle);
                intent1.putExtra("lat", lat);
                intent1.putExtra("previous", Global.ITEMLIST_TO_OVERTIME);
                intent1.putExtra("dayposition", dayposition);
                intent1.putExtra("lng", lng);
                intent1.putExtra("serviceLoc", serviceLoc);
                intent1.putExtra("petList", (Serializable) petList);
                intent1.putExtra("isVip", isVip);
                intent1.putExtra("tid", tid);
                intent1.putExtra("strp", getOverTime_PetID_ServiceId_MyPetId_ItemIds());
                startActivity(intent1);
            }
        });
    }

    private void setOnePetItem() {
        ApointMentPet apointMentPet = petList.get(0);
        List<ApointMentItem> itemList = apointMentPet.getItemList();
        if (itemList == null) {
            itemList = new ArrayList<ApointMentItem>();
        }
        ApointMentItem apointMentItem = new ApointMentItem(apointMentItem1.getPriceSuffix(),
                apointMentItem1.getId(), apointMentItem1.getName(), apointMentItem1.getPic(),
                apointMentItem1.getLabel());
        for (int i = 0; i < localItemPriceList.size(); i++) {
            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
            if (apointMentItemPrice.getItemId() == apointMentItem1.getId()) {
                List<ApointMentItemPrices> itemPriceList =
                        apointMentItemPrice.getItemPriceList();
                for (int j = 0; j < itemPriceList.size(); j++) {
                    ApointMentItemPrices apointMentItemPrices = itemPriceList.get(j);
                    if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                        if (tid == 0) {
                            apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                            apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                        } else if (tid == 1) {
                            apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                            apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                        } else if (tid == 2) {
                            apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice20());
                            apointMentItem.setPrice(apointMentItemPrices.getPrice20());
                        } else if (tid == 3) {
                            apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice30());
                            apointMentItem.setPrice(apointMentItemPrices.getPrice30());
                        }
                        break;
                    }
                }
                break;
            }
        }
        itemList.add(apointMentItem);
        apointMentPet.setItemList(itemList);
        apointMentItem1.setState(4);
        itemListAdapter.notifyDataSetChanged();
    }

    private void showItemPetDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(ItemListActivity.this, R.layout.appoint_pet_bottom_dialog, null);
        LinearLayout ll_appointpet_bottomdia_wc = (LinearLayout) customView.findViewById(R.id.ll_appointpet_bottomdia_wc);
        RecyclerView rv_appointpet_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpet_bottomdia);
        ImageView iv_appointpet_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpet_bottom_bg);
        rv_appointpet_bottomdia.setHasFixedSize(true);
        rv_appointpet_bottomdia.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        rv_appointpet_bottomdia.addItemDecoration(new GridSpacingItemDecoration(5,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
        for (int i = 0; i < localItemPriceList.size(); i++) {
            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
            if (apointMentItemPrice.getItemId() == apointMentItem1.getId()) {
                List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                if (itemPriceList != null && itemPriceList.size() > 0) {
                    for (int j = 0; j < itemPriceList.size(); j++) {
                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(j);
                        for (int k = 0; k < petList.size(); k++) {
                            ApointMentPet apointMentPet = petList.get(k);
                            if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                if (tid == 0) {
                                    apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice10());
                                    apointMentPet.setItemPrice(apointMentItemPrices.getPrice10());
                                } else if (tid == 1) {
                                    apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice10());
                                    apointMentPet.setItemPrice(apointMentItemPrices.getPrice10());
                                } else if (tid == 2) {
                                    apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice20());
                                    apointMentPet.setItemPrice(apointMentItemPrices.getPrice20());
                                } else if (tid == 3) {
                                    apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice30());
                                    apointMentPet.setItemPrice(apointMentItemPrices.getPrice30());
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
        appointBottomPetAdapter = new AppointBottomPetAdapter(R.layout.item_pet_bottom, petList);
        rv_appointpet_bottomdia.setAdapter(appointBottomPetAdapter);
        appointBottomPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (petList.size() > position) {
                    if (petList.get(position).getState() == 3 || petList.get(position).getState() == 5) {
                        petList.get(position).setSelect(!petList.get(position).isSelect());
                        appointBottomPetAdapter.notifyDataSetChanged();
                    } else if (petList.get(position).getState() == 2) {
                        ToastUtil.showToastShortBottom(ItemListActivity.this, "首席赠送，无法修改");
                    } else if (petList.get(position).getState() == 1) {
                        ToastUtil.showToastShortBottom(ItemListActivity.this, "您的宠物暂不支持添加此单项");
                    }
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(ItemListActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_appointpet_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        ll_appointpet_bottomdia_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                boolean isSelect = false;
                for (int i = 0; i < petList.size(); i++) {
                    ApointMentPet apointMentPet = petList.get(i);
                    if (apointMentPet.isSelect()) {
                        isSelect = true;
                        break;
                    }
                }
                for (int i = 0; i < petList.size(); i++) {//清空所有添加了这个单项的宠物
                    ApointMentPet apointMentPet = petList.get(i);
                    List<ApointMentItem> itemList = apointMentPet.getItemList();
                    if (itemList != null && itemList.size() > 0) {
                        for (int j = 0; j < itemList.size(); j++) {
                            ApointMentItem apointMentItem = itemList.get(j);
                            if (apointMentItem.getId() == apointMentItem1.getId()) {
                                itemList.remove(j);
                                break;
                            }
                        }
                        apointMentPet.setItemList(itemList);
                    }
                }
                if (isSelect) {
                    if (Utils.isStrNull(appointment) && localAppointWorker != null) {
                        overTimePetList.clear();
                        for (int i = 0; i < petList.size(); i++) {
                            ApointMentPet apointMentPet1 = petList.get(i);
                            ApointMentPet apointMentPet = new ApointMentPet();
                            apointMentPet.setPriceSuffix(apointMentPet1.getPriceSuffix());
                            apointMentPet.setPrice(apointMentPet1.getPrice());
                            apointMentPet.setSelect(apointMentPet1.isSelect());
                            apointMentPet.setItemPrice(apointMentPet1.getItemPrice());
                            apointMentPet.setItemVipPrice(apointMentPet1.getItemVipPrice());
                            apointMentPet.setMyPetId(apointMentPet1.getMyPetId());
                            apointMentPet.setPetId(apointMentPet1.getPetId());
                            apointMentPet.setPetImg(apointMentPet1.getPetImg());
                            apointMentPet.setPetNickName(apointMentPet1.getPetNickName());
                            apointMentPet.setVipPrice(apointMentPet1.getVipPrice());
                            apointMentPet.setSetItemDecor(apointMentPet1.isSetItemDecor());
                            apointMentPet.setServiceId(apointMentPet1.getServiceId());
                            List<ApointMentItem> itemList = apointMentPet1.getItemList();
                            if (itemList != null && itemList.size() > 0) {
                                ArrayList<ApointMentItem> apointMentItems = new ArrayList<>();
                                for (int j = 0; j < itemList.size(); j++) {
                                    apointMentItems.add(itemList.get(j));
                                }
                                apointMentPet.setItemList(apointMentItems);
                            }
                            overTimePetList.add(apointMentPet);
                        }
                        myPetIds.clear();
                        for (int i = 0; i < petList.size(); i++) {
                            ApointMentPet apointMentPet = petList.get(i);
                            if (apointMentPet.isSelect()) {
                                myPetIds.add(apointMentPet.getMyPetId());
                                for (int j = 0; j < overTimePetList.size(); j++) {
                                    ApointMentPet apointMentPet1 = overTimePetList.get(j);
                                    if (apointMentPet1.getMyPetId() == apointMentPet.getMyPetId()) {
                                        List<ApointMentItem> itemList = apointMentPet1.getItemList();
                                        if (itemList == null) {
                                            itemList = new ArrayList<ApointMentItem>();
                                        }
                                        itemList.add(apointMentItem1);
                                        apointMentPet1.setItemList(itemList);
                                    }
                                }
                            }
                        }
                        timeItemList.clear();
                        timeItemList.add(apointMentItem1);
                        itemToMorePet.setApointMentItem(timeItemList);
                        itemToMorePet.setMyPetIds(myPetIds);
                        flag = 2;
                        //如果选择了时间和美容师，需要校验添加这个单项以后，选中的时间还是否可约
                        workerIdList.clear();
                        mPDialog.showDialog();
                        CommUtil.getBeauticianFreeTime(
                                ItemListActivity.this,
                                lat,
                                lng,
                                spUtil.getString(
                                        "cellphone", ""),
                                Global.getIMEI(ItemListActivity.this),
                                Global.getCurrentVersion(ItemListActivity.this),
                                0, serviceLoc, shopId,
                                getOverTime_PetID_ServiceId_MyPetId_ItemIds(), 0, appointment, 0, 0,
                                allWorkerHanler);
                    } else {
                        setMorePetItem();
                    }
                } else {
                    setMorePetItem();
                }
            }
        });
    }

    private void setMorePetItem() {
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet.isSelect()) {
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                if (itemList == null) {
                    itemList = new ArrayList<ApointMentItem>();
                }
                itemList.add(new ApointMentItem(apointMentItem1.getPriceSuffix(),
                        apointMentItem1.getId(), apointMentItem1.getName(),
                        apointMentPet.getItemVipPrice(), apointMentPet.getItemPrice(),
                        apointMentItem1.getPic(), apointMentItem1.getLabel()));
                apointMentPet.setItemList(itemList);
            }
        }
        setItemData();
    }

    private void setItemData() {
        //更新单项列表数据
        for (int i = 0; i < itemList.size(); i++) {
            int num = 0;
            int shouXiNum = 0;
            int teethCardNum = 0;
            ApointMentItem apointMentItem = itemList.get(i);
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
                if (teethCardNum == petList.size()) {
                    apointMentItem.setTeethCard(true);
                }
                if (shouXiNum == petList.size()) {
                    apointMentItem.setState(2);
                    apointMentItem.setShouXi(true);
                } else {
                    for (int j = 0; j < petList.size(); j++) {
                        ApointMentPet apointMentPet = petList.get(j);
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            for (int k = 0; k < itemList.size(); k++) {
                                ApointMentItem apointMentItem1 = itemList.get(k);
                                if (apointMentItem1.getId() == apointMentItem.getId()) {
                                    num++;
                                }
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
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            for (int j = 0; j < itemList.size(); j++) {
                ApointMentItem apointMentItem = itemList.get(j);
                for (int k = 0; k < localItemPriceList.size(); k++) {
                    ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(k);
                    if (apointMentItem.getId() == apointMentItemPrice.getItemId()) {
                        List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                        if (itemPriceList != null && itemPriceList.size() > 0) {
                            for (int x = 0; x < itemPriceList.size(); x++) {
                                ApointMentItemPrices apointMentItemPrices = itemPriceList.get(x);
                                if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                    if (tid == 0 || tid == 1) {
                                        Log.e("TAG", "vip中级价格 = " + apointMentItemPrices.getVipPrice10());
                                        apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                                        Log.e("TAG", "非vip中级价格 = " + apointMentItemPrices.getPrice10());
                                        apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                                    } else if (tid == 2) {
                                        Log.e("TAG", "vip高级价格 = " + apointMentItemPrices.getVipPrice20());
                                        apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice20());
                                        Log.e("TAG", "非vip高级价格 = " + apointMentItemPrices.getPrice20());
                                        apointMentItem.setPrice(apointMentItemPrices.getPrice20());
                                    } else if (tid == 3) {
                                        Log.e("TAG", "vip首席价格 = " + apointMentItemPrices.getVipPrice30());
                                        apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice30());
                                        Log.e("TAG", "非vip首席价格 = " + apointMentItemPrices.getPrice30());
                                        apointMentItem.setPrice(apointMentItemPrices.getPrice30());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        itemListAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ItemListWorkerAndTimeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            isOverTime = true;
            appointment = event.getAppointment();
            localAppointWorker = event.getAppointWorker();
            dayposition = event.getDayposition();
            itemToMorePet = event.getItemToMorePet();
            workerIds = event.getWorkerIds();
            pickup = event.getPickup();
            //换了美容师或者时间回来为选中了这个单项的宠物加上这个单项
            if (petList.size() == 1) {
                setOnePetItem();
            } else {
                setMorePetItem();
            }
        }
    }

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
            new AlertDialogNavAndPost(this).builder().setTitle("").setMsg("检测到您更换了单项，是否保存更换的单项").setNegativeButton("否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).setPositiveButton("是", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("petList", (Serializable) petList);
                    intent.putExtra("itemList", (Serializable) itemList);
                    if (isOverTime) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("workerAndTime", new WorkerAndTime(localAppointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                        intent.putExtras(bundle1);
                    }
                    setResult(Global.RESULT_OK, intent);
                    finish();
                }
            }).show();
        } else {
            finish();
        }
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                exit();
                break;
            case R.id.tv_titlebar_other:
                //带值返回预约界面
                Intent intent = new Intent();
                intent.putExtra("petList", (Serializable) petList);
                intent.putExtra("itemList", (Serializable) itemList);
                if (isOverTime) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("workerAndTime", new WorkerAndTime(localAppointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
                    intent.putExtras(bundle1);
                }
                setResult(Global.RESULT_OK, intent);
                finish();
                break;
        }
    }

    private boolean isChange() {
        boolean isChange = false;
        if (isOverTime) {
            isChange = true;
        } else {
            for (int i = 0; i < petList.size(); i++) {
                ApointMentPet apointMentPet = petList.get(i);
                for (int j = 0; j < localPetList.size(); j++) {
                    ApointMentPet apointMentPet1 = localPetList.get(j);
                    if (apointMentPet.getMyPetId() == apointMentPet1.getMyPetId()) {
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        List<ApointMentItem> itemList1 = apointMentPet1.getItemList();
                        if (itemList != null && itemList1 == null) {
                            isChange = true;
                            break;
                        } else if (itemList == null && itemList1 != null) {
                            isChange = true;
                            break;
                        } else if (itemList != null && itemList1 != null) {
                            if (itemList.size() > 0 && itemList1.size() <= 0) {
                                isChange = true;
                                break;
                            } else if (itemList.size() <= 0 && itemList1.size() > 0) {
                                isChange = true;
                                break;
                            } else if (itemList.size() > 0 && itemList1.size() > 0) {
                                if (itemList.size() != itemList1.size()) {
                                    isChange = true;
                                    break;
                                } else {
                                    isChange = isItemChange(itemList, itemList1);
                                }
                            }
                        }
                    }
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
