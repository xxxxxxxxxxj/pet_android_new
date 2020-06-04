package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AddCareItemAdapter;
import com.haotang.pet.adapter.AddCareItemBeautyAdapter;
import com.haotang.pet.adapter.AddCareItemPetAdapter;
import com.haotang.pet.adapter.AddcarePetMxAdapter;
import com.haotang.pet.entity.FosterAddCareItem;
import com.haotang.pet.entity.FosterPet;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.DisplayUtil;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author:姜谷蓄
 * @Date:2019/12/17
 * @Description:添加护理单项
 */
public class AddCardItemActivity extends SuperActivity {
    private static final int ADDCAREITEMPET = 111;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    @BindView(R.id.rv_care_petlist)
    RecyclerView rvCarePetlist;
    @BindView(R.id.rv_card_itemlist)
    RecyclerView rvCardItemlist;
    @BindView(R.id.tv_care_itemnum)
    TextView tvCareItemnum;
    @BindView(R.id.tv_careitem_price)
    TextView tvCareitemPrice;
    private List<FosterAddCareItem.DataBean.DatasetBean> petList = new ArrayList<>();
    private List<FosterAddCareItem.DataBean.DatasetBean.ServicesBean> servicesList = new ArrayList<>();
    private List<Integer> workerIdList = new ArrayList<Integer>();
    private List<Integer> myPetIdList = new ArrayList<Integer>();
    private AddCareItemPetAdapter petAdapter;
    private AddCareItemAdapter itemAdapter;
    private int shopId;
    private int bathPetSize;
    private int hotelId;
    private int roomType;
    private String startTime;
    private String endTime;
    private View footer;
    private String noWorkerTip;
    private String shopTip;
    private String serviceTip;
    private String changeServiceTip;
    private int selectMyPetId;
    private int mainMyPetId;
    private int selectServiceId;
    private StringBuffer sbMyPetIds = new StringBuffer();
    private List<FosterAddCareItem.DataBean.DatasetBean> carePetList = new ArrayList<FosterAddCareItem.DataBean.DatasetBean>();
    private List<FosterPet> petDetailList = new ArrayList<FosterPet>();
    private StringBuffer sbExtraPetIds = new StringBuffer();
    private String servicePhone;
    private String hotelName;
    private String roomTypeName;
    private double roomPricesTotal;
    private double roomUnitPrice;
    private double totalPrice;
    private int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setListener();
        getData();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        Intent intent = getIntent();
        hotelId = intent.getIntExtra("shopId", 0);
        mainMyPetId = intent.getIntExtra("myPetId", 0);
        roomType = intent.getIntExtra("roomType", 0);
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        bathPetSize = intent.getIntExtra("bathPetSize", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_add_card_item);
        ButterKnife.bind(this);
        footer = LayoutInflater.from(this).inflate(
                R.layout.footer_addcareitem, null);
    }

    private void setView() {
        selectMyPetId = mainMyPetId;
        tvTitlebarTitle.setText("添加护理项目");
        vTitleSlide.setVisibility(View.VISIBLE);
        myPetIdList.clear();
        myPetIdList.add(mainMyPetId);

        petList.clear();
        rvCarePetlist.setHasFixedSize(true);
        rvCarePetlist.setLayoutManager(new LinearLayoutManager(this));
        petAdapter = new AddCareItemPetAdapter(R.layout.item_careitem_addpet, petList);
        rvCarePetlist.setAdapter(petAdapter);
        petAdapter.addFooterView(footer);
        setFooter();

        rvCardItemlist.setHasFixedSize(true);
        itemAdapter = new AddCareItemAdapter(R.layout.item_careitem_add, servicesList);
        rvCardItemlist.setLayoutManager(new LinearLayoutManager(mContext));
        rvCardItemlist.setAdapter(itemAdapter);
    }

    private void setListener() {
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogDefault(mContext).builder()
                        .setTitle("是否添加宠物").setMsg("添加的宠物跟选择的宠物同住一间房中，不额外收取房费，须支付宠物基础服务费").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("添加宠物", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AddFosterPetActivity.class);
                        intent.putExtra("shopId", hotelId);
                        intent.putExtra("roomType", roomType);
                        sbMyPetIds.setLength(0);
                        for (int i = 0; i < myPetIdList.size(); i++) {
                            if (i == myPetIdList.size() - 1) {
                                sbMyPetIds.append(myPetIdList.get(i));
                            } else {
                                sbMyPetIds.append(myPetIdList.get(i) + ",");
                            }
                        }
                        intent.putExtra("mypetIds", sbMyPetIds.toString());
                        startActivityForResult(intent, ADDCAREITEMPET);
                    }
                }).show();
            }
        });
        petAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (petList.size() > 0 && petList.size() > position) {
                    switch (view.getId()) {
                        case R.id.ll_careitempet_root:
                            setPet(position);
                            break;
                        case R.id.iv_careitempet_del:
                            if (position > 0) {
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("提示").setMsg("是否删除添加宠物？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setPositiveButton("确定删除", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (myPetIdList.size() > 0) {
                                            for (int i = 0; i < myPetIdList.size(); i++) {
                                                if (myPetIdList.get(i) == petList.get(position).getMyPetId()) {
                                                    myPetIdList.remove(i);
                                                    break;
                                                }
                                            }
                                        }
                                        petList.remove(position);
                                        petAdapter.notifyDataSetChanged();
                                        setPet(position - 1);
                                        setFooter();
                                        getTotalPrice();
                                    }
                                }).show();
                            }
                            break;
                    }
                }
            }
        });
        itemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                if (servicesList.size() > 0 && servicesList.size() > position) {
                    int beforeServiceId = 0;
                    for (int i = 0; i < petList.size(); i++) {
                        if (petList.get(i).getMyPetId() == selectMyPetId) {
                            beforeServiceId = petList.get(i).getSelectServiceId();
                            break;
                        }
                    }
                    int serviceId = servicesList.get(position).getId();
                    if ((beforeServiceId == 1 || beforeServiceId == 3) && (serviceId == 2 || serviceId == 4)) {
                        new AlertDialogDefault(mContext).builder()
                                .setTitle("是否更改为离店美容？").setMsg(changeServiceTip).isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("更改为离店美容", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectServiceId = servicesList.get(position).getId();
                                showChooseBeautyPop(servicesList.get(position).getWorkers());
                            }
                        }).show();
                    } else {
                        selectServiceId = servicesList.get(position).getId();
                        showChooseBeautyPop(servicesList.get(position).getWorkers());
                    }
                }
            }
        });
    }

    private void setPet(int position) {
        if (petList.size() > 0) {
            for (int i = 0; i < petList.size(); i++) {
                petList.get(i).setPetSelected(false);
            }
            FosterAddCareItem.DataBean.DatasetBean selectPet = petList.get(position);
            selectMyPetId = selectPet.getMyPetId();
            selectPet.setPetSelected(true);
            petAdapter.notifyDataSetChanged();
            servicesList.clear();
            if (selectPet.getServices() != null && selectPet.getServices().size() > 0) {
                servicesList.addAll(selectPet.getServices());
            }
            itemAdapter.notifyDataSetChanged();
        }
    }

    private void setFooter() {
        if (petList.size() > 0) {
            if (petList.size() < bathPetSize) {
                footer.setVisibility(View.VISIBLE);
            } else {
                footer.setVisibility(View.GONE);
            }
        } else {
            footer.setVisibility(View.GONE);
        }
    }

    private void getData() {
        mPDialog.showDialog();
        servicesList.clear();
        sbMyPetIds.setLength(0);
        for (int i = 0; i < myPetIdList.size(); i++) {
            if (i == myPetIdList.size() - 1) {
                sbMyPetIds.append(myPetIdList.get(i));
            } else {
                sbMyPetIds.append(myPetIdList.get(i) + ",");
            }
        }
        CommUtil.addCareService(mContext, hotelId, sbMyPetIds.toString(), roomType, startTime, endTime, addCareHandler);
    }

    private AsyncHttpResponseHandler addCareHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    Gson gson = new Gson();
                    FosterAddCareItem fosterAddCareItem = gson.fromJson(new String(responseBody), FosterAddCareItem.class);
                    FosterAddCareItem.DataBean data = fosterAddCareItem.getData();
                    noWorkerTip = data.getNoWorkerTip();
                    shopTip = data.getShopTip();
                    serviceTip = data.getServiceTip();
                    changeServiceTip = data.getChangeServiceTip();
                    shopId = data.getShopId();
                    List<FosterAddCareItem.DataBean.DatasetBean> dataset = data.getDataset();
                    if (dataset != null && dataset.size() > 0) {
                        if (petList.size() > 0) {
                            for (int i = 0; i < dataset.size(); i++) {
                                boolean isAdd = true;
                                int myPetId = dataset.get(i).getMyPetId();
                                for (int j = 0; j < petList.size(); j++) {
                                    int myPetId1 = petList.get(j).getMyPetId();
                                    if (myPetId == myPetId1) {
                                        isAdd = false;
                                        break;
                                    }
                                }
                                if (isAdd) {
                                    petList.add(dataset.get(i));
                                }
                            }
                        } else {
                            petList.addAll(dataset);
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            setFooter();
            int position = 0;
            if (petList.size() > 0) {
                for (int i = 0; i < petList.size(); i++) {
                    if (petList.get(i).getMyPetId() == selectMyPetId) {
                        position = i;
                        break;
                    }
                }
            }
            setPet(position);
            getTotalPrice();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void showChooseBeautyPop(final List<FosterAddCareItem.DataBean.DatasetBean.ServicesBean.WorkersBean> workers) {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.pop_careitem_selectbeauty, null);
        TextView tvTitle = customView.findViewById(R.id.tv_addcare_shopname);
        TextView tvDesc = customView.findViewById(R.id.tv_addcare_desc);
        RecyclerView rvBeauty = customView.findViewById(R.id.rv_addcare_beauty);
        Button btn_addserivce_shop_sure = customView.findViewById(R.id.btn_addserivce_shop_sure);
        RelativeLayout rl_addcare_root = customView.findViewById(R.id.rl_addcare_root);
        RelativeLayout rl_addcare_info = customView.findViewById(R.id.rl_addcare_info);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点 
        // 注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的 
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失 
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击 
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画 
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(mContext)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        Utils.setText(tvTitle, shopTip, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvDesc, serviceTip, "", View.VISIBLE, View.VISIBLE);
        int tid = 0;
        for (int i = 0; i < petList.size(); i++) {
            if (petList.get(i).getMyPetId() == selectMyPetId && petList.get(i).getSelectServiceId() == selectServiceId) {//选中
                tid = petList.get(i).getSelectWorkerTid();
                break;
            }
        }
        for (int i = 0; i < workers.size(); i++) {
            workers.get(i).setSelect(false);
        }
        for (int i = 0; i < workers.size(); i++) {
            if (workers.get(i).getTid() == tid) {
                workers.get(i).setSelect(true);
                break;
            }
        }
        workerIdList.clear();
        //取出所有选择了这个服务的美容师ID
        //一个美容师只能给一只狗做服务
        for (int i = 0; i < petList.size(); i++) {
            if (petList.get(i).getMyPetId() == selectMyPetId) {
                if (petList.get(i).getSelectWorkerId() > 0 && petList.get(i).getSelectServiceId() == selectServiceId) {
                    workerIdList.add(petList.get(i).getSelectWorkerId());
                }
            } else {
                if (petList.get(i).getSelectWorkerId() > 0) {
                    workerIdList.add(petList.get(i).getSelectWorkerId());
                }
            }
        }
        Log.e("TAG", "去重前workerIdList = " + workerIdList.toString());
        //集合去重，利用list中的元素创建HashSet集合，此时set中进行了去重操作
        HashSet set = new HashSet(workerIdList);
        // 清空list集合
        workerIdList.clear();
        // 将去重后的元素重新添加到list中
        workerIdList.addAll(set);
        Log.e("TAG", "去重后workerIdList = " + workerIdList.toString());
        for (int i = 0; i < workers.size(); i++) {
            FosterAddCareItem.DataBean.DatasetBean.ServicesBean.WorkersBean workersBean = workers.get(i);
            List<Integer> members = workersBean.getMembers();
            if (members != null && members.size() > 0) {
                if (workerIdList.size() > 0) {
                    boolean isAvail = false;
                    for (int j = 0; j < members.size(); j++) {
                        Integer workerId = members.get(j);
                        if (!workerIdList.contains(workerId)) {
                            isAvail = true;
                        }
                    }
                    if (isAvail) {
                        workersBean.setAvail(true);
                    } else {
                        if (workers.get(i).isSelect()) {
                            workersBean.setAvail(true);
                        } else {
                            workersBean.setAvail(false);
                        }
                    }
                } else {
                    workersBean.setAvail(true);
                }
            } else {
                workersBean.setAvail(false);
            }
        }
        rvBeauty.setHasFixedSize(true);
        final AddCareItemBeautyAdapter addCareItemBeautyAdapter = new AddCareItemBeautyAdapter(R.layout.item_addcareitem_selectbeauty, workers, noWorkerTip);
        rvBeauty.setLayoutManager(new LinearLayoutManager(mContext));
        rvBeauty.setAdapter(addCareItemBeautyAdapter);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        rl_addcare_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        rl_addcare_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addCareItemBeautyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (workers.size() > position) {
                    FosterAddCareItem.DataBean.DatasetBean.ServicesBean.WorkersBean workersBean = workers.get(position);
                    if (workersBean.isAvail()) {
                        for (int i = 0; i < workers.size(); i++) {
                            if (position == i) {
                                workers.get(i).setSelect(!workers.get(i).isSelect());
                            } else {
                                workers.get(i).setSelect(false);
                            }
                        }
                        addCareItemBeautyAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToastShortBottom(mContext, noWorkerTip);
                    }
                }
            }
        });
        btn_addserivce_shop_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectPosition = -1;
                double ePrice = 0;
                double price = 0;
                for (int i = 0; i < workers.size(); i++) {
                    if (workers.get(i).isSelect()) {
                        selectPosition = i;
                        ePrice = workers.get(i).getEPrice();
                        price = workers.get(i).getPrice();
                        break;
                    }
                }
                if (selectPosition >= 0) {
                    List<Integer> members = workers.get(selectPosition).getMembers();
                    int selectWorkerId = 0;
                    if (workerIdList.size() > 0) {
                        for (int i = 0; i < members.size(); i++) {
                            if (!workerIdList.contains(members.get(i))) {
                                selectWorkerId = members.get(i);
                                break;
                            }
                        }
                    } else {
                        selectWorkerId = members.get(0);
                    }
                    for (int i = 0; i < petList.size(); i++) {
                        if (petList.get(i).getMyPetId() == selectMyPetId) {
                            petList.get(i).setSelectServiceId(selectServiceId);
                            petList.get(i).setSelectWorkerId(selectWorkerId);
                            petList.get(i).setSelectWorkerTid(workers.get(selectPosition).getTid());
                            break;
                        }
                    }
                    for (int i = 0; i < servicesList.size(); i++) {
                        servicesList.get(i).setServiceSelect(false);
                        servicesList.get(i).setSelectEPrice(0);
                        servicesList.get(i).setSelectPrice(0);
                    }
                    for (int i = 0; i < servicesList.size(); i++) {
                        if (servicesList.get(i).getId() == selectServiceId) {
                            servicesList.get(i).setServiceSelect(true);
                            servicesList.get(i).setSelectEPrice(ePrice);
                            servicesList.get(i).setSelectPrice(price);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < petList.size(); i++) {
                        if (petList.get(i).getMyPetId() == selectMyPetId) {
                            petList.get(i).setSelectServiceId(0);
                            petList.get(i).setSelectWorkerId(0);
                            petList.get(i).setSelectWorkerTid(0);
                            break;
                        }
                    }
                    for (int i = 0; i < servicesList.size(); i++) {
                        servicesList.get(i).setServiceSelect(false);
                        servicesList.get(i).setSelectEPrice(0);
                        servicesList.get(i).setSelectPrice(0);
                    }
                }
                petAdapter.notifyDataSetChanged();
                itemAdapter.notifyDataSetChanged();
                pWinBottomDialog.dismiss();
                getTotalPrice();
            }
        });
    }

    private void getTotalPrice() {
        sbExtraPetIds.setLength(0);
        if (petList.size() > 1) {
            for (int i = 1; i < petList.size(); i++) {
                if (i == petList.size() - 1) {
                    sbExtraPetIds.append(petList.get(i).getMyPetId());
                } else {
                    sbExtraPetIds.append(petList.get(i).getMyPetId() + ",");
                }
            }
        }
        carePetList.clear();
        if (petList.size() > 0) {
            for (int i = 0; i < petList.size(); i++) {
                if (petList.get(i).getSelectWorkerId() > 0) {//说明有离店洗美
                    carePetList.add(petList.get(i));
                }
            }
        }
        int careShopId = 0;
        String strp = null;
        if (carePetList.size() > 0) {
            strp = getStrp();
            careShopId = shopId;
        }
        totalPrice = 0;
        mPDialog.showDialog();
        CommUtil.getFosterOrderConfirmation(this, careShopId, roomType, mainMyPetId, hotelId, startTime, endTime, strp, sbExtraPetIds.toString(), 0, 0, dataTotalHanler);
    }

    private AsyncHttpResponseHandler dataTotalHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("totalPrice") && !jdata.isNull("totalPrice")) {
                            totalPrice = jdata.getDouble("totalPrice");
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            tvCareitemPrice.setText("¥" + totalPrice);
            int num = 0;
            if (petList.size() > 0) {
                for (int i = 0; i < petList.size(); i++) {
                    if (petList.get(i).getSelectWorkerId() > 0) {
                        num++;
                    }
                }
                if (num > 0) {
                    tvCareItemnum.setVisibility(View.VISIBLE);
                    tvCareItemnum.setText(String.valueOf(num));
                } else {
                    tvCareItemnum.setVisibility(View.GONE);
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

    @OnClick({R.id.ib_titlebar_back, R.id.rl_care_shopcart, R.id.tv_careitem_goorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.rl_care_shopcart:
                sbExtraPetIds.setLength(0);
                if (petList.size() > 1) {
                    for (int i = 1; i < petList.size(); i++) {
                        if (i == petList.size() - 1) {
                            sbExtraPetIds.append(petList.get(i).getMyPetId());
                        } else {
                            sbExtraPetIds.append(petList.get(i).getMyPetId() + ",");
                        }
                    }
                }
                carePetList.clear();
                if (petList.size() > 0) {
                    for (int i = 0; i < petList.size(); i++) {
                        if (petList.get(i).getSelectWorkerId() > 0) {//说明有离店洗美
                            carePetList.add(petList.get(i));
                        }
                    }
                }
                int careShopId = 0;
                String strp = null;
                if (carePetList.size() > 0) {
                    strp = getStrp();
                    careShopId = shopId;
                }
                petDetailList.clear();
                hotelName = "";
                servicePhone = "";
                roomPricesTotal = 0;
                roomUnitPrice = 0;
                days = 0;
                roomTypeName = "";
                mPDialog.showDialog();
                CommUtil.getFosterOrderConfirmation(this, careShopId, roomType, mainMyPetId, hotelId, startTime, endTime, strp, sbExtraPetIds.toString(), 0, 0, dataHanler);
                break;
            case R.id.tv_careitem_goorder:
                Intent intent = new Intent(mContext, FosterOrderConfirmActivity.class);
                intent.putExtra("roomType", roomType);
                intent.putExtra("mypetId", mainMyPetId);
                intent.putExtra("shopId", hotelId);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                intent.putExtra("fosterTime", getIntent().getStringExtra("fosterTime"));
                intent.putExtra("contact", getIntent().getStringExtra("contact"));
                intent.putExtra("contactPhone", getIntent().getStringExtra("contactPhone"));
                if (petList.size() > 1) {
                    sbExtraPetIds.setLength(0);
                    for (int i = 1; i < petList.size(); i++) {
                        if (i == petList.size() - 1) {
                            sbExtraPetIds.append(petList.get(i).getMyPetId());
                        } else {
                            sbExtraPetIds.append(petList.get(i).getMyPetId() + ",");
                        }
                    }
                    intent.putExtra("extraPetIds", sbExtraPetIds.toString());
                }
                carePetList.clear();
                if (petList.size() > 0) {
                    for (int i = 0; i < petList.size(); i++) {
                        if (petList.get(i).getSelectWorkerId() > 0) {//说明有离店洗美
                            carePetList.add(petList.get(i));
                        }
                    }
                }
                if (carePetList.size() > 0) {
                    intent.putExtra("strp", getStrp());
                    intent.putExtra("careShopId", shopId);
                }
                startActivity(intent);
                break;
        }
    }

    private String getStrp() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < carePetList.size(); i++) {
            FosterAddCareItem.DataBean.DatasetBean carePet = carePetList.get(i);
            if (i < carePetList.size() - 1) {
                sb.append(carePet.getPetId());
                sb.append("_");
                sb.append(carePet.getSelectServiceId());
                sb.append("_");
                sb.append(carePet.getMyPetId());
                sb.append("_");
                sb.append(carePet.getSelectWorkerTid());
                sb.append("_");
            } else {
                sb.append(carePet.getPetId());
                sb.append("_");
                sb.append(carePet.getSelectServiceId());
                sb.append("_");
                sb.append(carePet.getMyPetId());
                sb.append("_");
                sb.append(carePet.getSelectWorkerTid());
            }
        }
        return sb.toString();
    }

    private AsyncHttpResponseHandler dataHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("shop") && !jdata.isNull("shop")) {
                            JSONObject jshop = jdata.getJSONObject("shop");
                            if (jshop.has("hotelName") && !jshop.isNull("hotelName")) {
                                hotelName = jshop.getString("hotelName");
                            }
                            if (jshop.has("phone") && !jshop.isNull("phone")) {
                                servicePhone = jshop.getString("phone");
                            }
                        }
                        if (jdata.has("roomPrice") && !jdata.isNull("roomPrice")) {
                            JSONObject jroomPrices = jdata.getJSONObject("roomPrice");
                            if (jroomPrices.has("total") && !jroomPrices.isNull("total")) {
                                roomPricesTotal = jroomPrices.getDouble("total");
                            }
                            if (jroomPrices.has("price") && !jroomPrices.isNull("price")) {
                                roomUnitPrice = jroomPrices.getDouble("price");
                            }
                            if (jroomPrices.has("days") && !jroomPrices.isNull("days")) {
                                days = jroomPrices.getInt("days");
                            }
                        }
                        if (jdata.has("roomType") && !jdata.isNull("roomType")) {
                            JSONObject jroomType = jdata.getJSONObject("roomType");
                            if (jroomType.has("name") && !jroomType.isNull("name")) {
                                roomTypeName = jroomType.getString("name");
                            }
                        }
                        if (jdata.has("pets") && !jdata.isNull("pets")) {
                            JSONArray jarrpet = jdata.getJSONArray("pets");
                            for (int i = 0; i < jarrpet.length(); i++) {
                                petDetailList.add(FosterPet.jsonToEntity(jarrpet.getJSONObject(i), 1));
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
            showPetMxDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void showPetMxDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.care_petmx_bottom_dialog, null);
        ImageView iv_carepetmx_bottom_bg = (ImageView) customView.findViewById(R.id.iv_carepetmx_bottom_bg);
        ImageView iv_carepetmx_bottom_bg_close = (ImageView) customView.findViewById(R.id.iv_carepetmx_bottom_bg_close);
        LinearLayout ll_carepetmx_bottomdia = (LinearLayout) customView.findViewById(R.id.ll_carepetmx_bottomdia);
        ImageView iv_carepetmx_bottom_call = (ImageView) customView.findViewById(R.id.iv_carepetmx_bottom_call);
        TextView tv_carepetmx_bottomdia_title = (TextView) customView.findViewById(R.id.tv_carepetmx_bottomdia_title);
        TextView tv_carepetmx_bottom_rzrq = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_rzrq);
        TextView tv_carepetmx_bottom_rztime = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_rztime);
        TextView tv_carepetmx_bottom_ldrq = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_ldrq);
        TextView tv_carepetmx_bottom_ldtime = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_ldtime);
        TextView tv_carepetmx_bottom_num = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_num);
        TextView tv_carepetmx_bottom_roomprice = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_roomprice);
        TextView tv_carepetmx_bottom_roomtype = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_roomtype);
        TextView tv_carepetmx_bottom_roomdj = (TextView) customView.findViewById(R.id.tv_carepetmx_bottom_roomdj);
        final RecyclerView rv_carepetmx_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_carepetmx_bottomdia);
        Utils.setText(tv_carepetmx_bottomdia_title, hotelName, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_roomprice, "¥" + roomPricesTotal, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_roomtype, roomTypeName, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_num, "共" + days + "天", "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_roomdj, "¥" + roomUnitPrice + "*" + days + "天", "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_rzrq, startTime, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_ldrq, endTime, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_rztime, getIntent().getStringExtra("fosterTime") + "入住", "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_carepetmx_bottom_ldtime, getIntent().getStringExtra("fosterTime") + "离店", "", View.VISIBLE, View.VISIBLE);
        ll_carepetmx_bottomdia.bringToFront();
        rv_carepetmx_bottomdia.setHasFixedSize(true);
        rv_carepetmx_bottomdia.setLayoutManager(new LinearLayoutManager(this));
        final AddcarePetMxAdapter addcarePetMxAdapter = new AddcarePetMxAdapter(R.layout.item_addcare_pet_mx, petDetailList);
        rv_carepetmx_bottomdia.setAdapter(addcarePetMxAdapter);
        addcarePetMxAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (petDetailList != null && petDetailList.size() > 0 && petDetailList.size() > position) {
                    final FosterPet item = petDetailList.get(position);
                    switch (view.getId()) {
                        case R.id.iv_addcarepetmx_delete:
                            if (item.getListBathFee() > 0) {
                                item.setListBathFee(0);
                                addcarePetMxAdapter.notifyDataSetChanged();
                                int id = item.getId();
                                for (int i = 0; i < petList.size(); i++) {
                                    if (petList.get(i).getMyPetId() == id) {
                                        petList.get(i).setSelectServiceId(0);
                                        petList.get(i).setSelectWorkerId(0);
                                        petList.get(i).setSelectWorkerTid(0);
                                        if (petList.get(i).getServices() != null && petList.get(i).getServices().size() > 0) {
                                            for (int j = 0; j < petList.get(i).getServices().size(); j++) {
                                                petList.get(i).getServices().get(j).setServiceSelect(false);
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (selectMyPetId == id) {
                                    for (int i = 0; i < servicesList.size(); i++) {
                                        servicesList.get(i).setServiceSelect(false);
                                    }
                                }
                                petAdapter.notifyDataSetChanged();
                                itemAdapter.notifyDataSetChanged();
                                getTotalPrice();
                            }
                            break;
                    }
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_carepetmx_bottomdia.scrollToPosition(0);
            }
        }, 500);
        float screenDensity = ScreenUtil.getScreenDensity(this);
        Log.e("TAG", "screenDensity = " + screenDensity);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.getWindowHeight(this) - DensityUtil.dp2px(this, 54)
                - DisplayUtil.getStatusBarHeight(this), true);
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        pWinBottomDialog.setFocusable(false);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        pWinBottomDialog.setClippingEnabled(false);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(mContext)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, DensityUtil.dp2px(this, 54));
        iv_carepetmx_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_carepetmx_bottom_bg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        ll_carepetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        iv_carepetmx_bottom_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.telePhoneBroadcast(mContext, servicePhone);
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADDCAREITEMPET:
                    selectMyPetId = data.getIntExtra("addMyPetId", 0);
                    myPetIdList.add(selectMyPetId);
                    getData();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
