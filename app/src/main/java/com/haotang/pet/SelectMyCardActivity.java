package com.haotang.pet;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.SelectMyCardAdapter;
import com.haotang.pet.entity.AppointRechargeEvent;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.AlertDialogDefault;
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
 * 下单选择用户服务卡列表界面
 */
public class SelectMyCardActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_selectmycard)
    RecyclerView rvSelectmycard;
    @BindView(R.id.btn_selectmycard_submit)
    Button btnSelectmycardSubmit;
    private List<MyCard> list = new ArrayList<MyCard>();
    private List<MyCard> kyList = new ArrayList<MyCard>();
    private List<MyCard> bkyList = new ArrayList<MyCard>();
    private SelectMyCardAdapter selectMyCardAdapter;
    private int id;
    private double payPrice;
    private int type;
    private int shopId;
    private String orderKey;
    private int flag;
    private int selectId = 0;
    private String selectDiscountText = "";
    private String selectCardTypeName = "";
    private double selectAmount = 0;
    private double cardPayPrice;
    private int serviceLoc;
    private String strp;
    private int workerId;
    private int tid;
    private String appointment;
    private int pickup;
    private int couponId;
    private int homeCouponId;
    private int canUseServiceCard;
    //升级订单使用
    private int updateOrderId;
    //寄养使用
    private int careShopId;
    private int roomType;
    private int mypetId;
    private String startTime;
    private String endTime;
    private String extraPetIds;
    private int couponFlag;
    private StringBuffer stringBuffer = new StringBuffer();
    private StringBuffer stringBuffer1 = new StringBuffer();

    //绑卡，买卡，或者退卡回来
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointRechargeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.isRecharge()) {
                getData();
            }
        }
    }

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
        MApplication.listAppoint.add(this);
        flag = getIntent().getIntExtra("flag", 0);
        id = getIntent().getIntExtra("id", 0);
        type = getIntent().getIntExtra("type", 0);
        shopId = getIntent().getIntExtra("shopId", 0);
        payPrice = getIntent().getDoubleExtra("payPrice", 0);
        orderKey = getIntent().getStringExtra("orderKey");

        strp = getIntent().getStringExtra("strp");
        appointment = getIntent().getStringExtra("appointment");

        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        workerId = getIntent().getIntExtra("workerId", 0);
        tid = getIntent().getIntExtra("tid", 0);
        pickup = getIntent().getIntExtra("pickup", 0);
        couponId = getIntent().getIntExtra("couponId", 0);
        homeCouponId = getIntent().getIntExtra("homeCouponId", 0);
        canUseServiceCard = getIntent().getIntExtra("canUseServiceCard", 0);

        updateOrderId = getIntent().getIntExtra("updateOrderId", 0);

        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        extraPetIds = getIntent().getStringExtra("extraPetIds");
        careShopId = getIntent().getIntExtra("careShopId", 0);
        roomType = getIntent().getIntExtra("roomType", 0);
        mypetId = getIntent().getIntExtra("mypetId", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_select_my_card);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("选择E卡");
    }

    private void setLinster() {
    }

    private void getData() {
        mPDialog.showDialog();
        list.clear();
        bkyList.clear();
        kyList.clear();
        stringBuffer.setLength(0);
        stringBuffer1.setLength(0);
        int tempShopId = shopId;
        if (flag == 2) {//寄养
            tempShopId = 0;
        }
        CommUtil.serviceCardList(this, tempShopId, type, payPrice, orderKey, appointment, cardListHandler);
    }

    private AsyncHttpResponseHandler cardListHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("available") && !jdata.isNull("available")) {
                            JSONArray jarravailable = jdata.getJSONArray("available");
                            if (jarravailable.length() > 0) {
                                for (int i = 0; i < jarravailable.length(); i++) {
                                    MyCard myCard = new MyCard();
                                    JSONObject javailable = jarravailable.getJSONObject(i);
                                    if (javailable.has("serviceCardTypeName") && !javailable.isNull("serviceCardTypeName")) {
                                        myCard.setCardTypeName(javailable.getString("serviceCardTypeName"));
                                    }
                                    if (javailable.has("smallPic") && !javailable.isNull("smallPic")) {
                                        myCard.setSmallPic(javailable.getString("smallPic"));
                                    }
                                    if (javailable.has("mineCardPic") && !javailable.isNull("mineCardPic")) {
                                        myCard.setMineCardPic(javailable.getString("mineCardPic"));
                                    }
                                    if (javailable.has("nowDiscountDescribe") && !javailable.isNull("nowDiscountDescribe")) {
                                        myCard.setDiscountText(javailable.getString("nowDiscountDescribe"));
                                    }
                                    if (javailable.has("availableBalance") && !javailable.isNull("availableBalance")) {
                                        myCard.setAmount(javailable.getDouble("availableBalance"));
                                    }
                                    if (javailable.has("id") && !javailable.isNull("id")) {
                                        myCard.setId(javailable.getInt("id"));
                                    }
                                    if (javailable.has("discount") && !javailable.isNull("discount")) {
                                        myCard.setDiscount(javailable.getDouble("discount"));
                                    }
                                    if (javailable.has("dicountDesc") && !javailable.isNull("dicountDesc")) {
                                        JSONArray jarrdicountDesc = javailable.getJSONArray("dicountDesc");
                                        if (jarrdicountDesc != null && jarrdicountDesc.length() > 0) {
                                            stringBuffer.setLength(0);
                                            for (int j = 0; j < jarrdicountDesc.length(); j++) {
                                                if (j == jarrdicountDesc.length() - 1) {
                                                    stringBuffer.append(jarrdicountDesc.getString(j));
                                                } else {
                                                    stringBuffer.append(jarrdicountDesc.getString(j) + "|");
                                                }
                                            }
                                            myCard.setDicountDesc(stringBuffer.toString());
                                        }
                                    }
                                    myCard.setShow(false);
                                    myCard.setAvail(true);
                                    kyList.add(myCard);
                                }
                            }
                        }
                        if (jdata.has("unavailable") && !jdata.isNull("unavailable")) {
                            JSONArray jarrunavailable = jdata.getJSONArray("unavailable");
                            if (jarrunavailable.length() > 0) {
                                for (int i = 0; i < jarrunavailable.length(); i++) {
                                    MyCard myCard = new MyCard();
                                    JSONObject javailable = jarrunavailable.getJSONObject(i);
                                    if (javailable.has("serviceCardTypeName") && !javailable.isNull("serviceCardTypeName")) {
                                        myCard.setCardTypeName(javailable.getString("serviceCardTypeName"));
                                    }
                                    if (javailable.has("smallPic") && !javailable.isNull("smallPic")) {
                                        myCard.setSmallPic(javailable.getString("smallPic"));
                                    }
                                    if (javailable.has("nowDiscountDescribe") && !javailable.isNull("nowDiscountDescribe")) {
                                        myCard.setDiscountText(javailable.getString("nowDiscountDescribe"));
                                    }
                                    if (javailable.has("mineCardPic") && !javailable.isNull("mineCardPic")) {
                                        myCard.setMineCardPic(javailable.getString("mineCardPic"));
                                    }
                                    if (javailable.has("availableBalance") && !javailable.isNull("availableBalance")) {
                                        myCard.setAmount(javailable.getDouble("availableBalance"));
                                    }
                                    if (javailable.has("id") && !javailable.isNull("id")) {
                                        myCard.setId(javailable.getInt("id"));
                                    }
                                    if (javailable.has("reason") && !javailable.isNull("reason")) {
                                        myCard.setReason(javailable.getString("reason"));
                                    }
                                    if (javailable.has("discount") && !javailable.isNull("discount")) {
                                        myCard.setDiscount(javailable.getDouble("discount"));
                                    }
                                    if (javailable.has("dicountDesc") && !javailable.isNull("dicountDesc")) {
                                        JSONArray jarrdicountDesc = javailable.getJSONArray("dicountDesc");
                                        if (jarrdicountDesc != null && jarrdicountDesc.length() > 0) {
                                            stringBuffer1.setLength(0);
                                            for (int j = 0; j < jarrdicountDesc.length(); j++) {
                                                if (j == jarrdicountDesc.length() - 1) {
                                                    stringBuffer1.append(jarrdicountDesc.getString(j));
                                                } else {
                                                    stringBuffer1.append(jarrdicountDesc.getString(j) + "|");
                                                }
                                            }
                                            myCard.setDicountDesc(stringBuffer1.toString());
                                        }
                                    }
                                    myCard.setPosition(i);
                                    myCard.setAvail(false);
                                    if (i == 0) {
                                        myCard.setShow(true);
                                    } else {
                                        myCard.setShow(false);
                                    }
                                    bkyList.add(myCard);
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(SelectMyCardActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(SelectMyCardActivity.this, "数据异常");
                e.printStackTrace();
            }
            if (kyList.size() > 0) {
                list.addAll(kyList);
            }
            if (bkyList.size() > 0) {
                list.addAll(bkyList);
            }
            if (list.size() > 0) {
                //判断是否有选中
                if (id > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == id && list.get(i).isAvail()) {
                            list.get(i).setSelect(true);
                            break;
                        }
                    }
                }
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isLast()) {
                            list.remove(i);
                            break;
                        }
                    }
                    list.add(new MyCard(true));
                }
            }
            boolean isHaveUnAvailCard = false;
            if (bkyList.size() > 0) {
                isHaveUnAvailCard = true;
            }
            rvSelectmycard.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.bottom = -DensityUtil.dp2px(mContext, 115);
                }
            });
            rvSelectmycard.setHasFixedSize(true);
            rvSelectmycard.setLayoutManager(new LinearLayoutManager(mContext));
            selectMyCardAdapter = new SelectMyCardAdapter(R.layout.item_selectmycard, list, isHaveUnAvailCard);
            rvSelectmycard.setAdapter(selectMyCardAdapter);
            if (kyList.size() <= 0 && bkyList.size() <= 0) {
                selectMyCardAdapter.setEmptyView(setEmptyViewBase(2, "很遗憾，暂无可用E卡", null));
            } else if (kyList.size() <= 0 && bkyList.size() > 0) {
                selectMyCardAdapter.addHeaderView(LayoutInflater.from(mContext).inflate(
                        R.layout.header_selectcard, null));
            }
            selectMyCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (list != null && list.size() > 0 && list.size() > position && position != list.size() - 1) {
                        MyCard myCard = list.get(position);
                        if (myCard != null) {
                            if (!myCard.isAvail()) {
                                return;
                            }
                            for (int i = 0; i < list.size(); i++) {
                                if (position == i) {
                                    list.get(i).setSelect(!list.get(i).isSelect());
                                } else {
                                    list.get(i).setSelect(false);
                                }
                            }
                            selectMyCardAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(SelectMyCardActivity.this, "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.btn_selectmycard_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_selectmycard_submit:
                boolean isChoose = false;
                selectId = 0;
                selectDiscountText = "";
                selectCardTypeName = "";
                selectAmount = 0;
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect() && list.get(i).isAvail()) {
                            isChoose = true;
                            selectId = list.get(i).getId();
                            selectDiscountText = list.get(i).getDiscountText();
                            selectCardTypeName = list.get(i).getCardTypeName();
                            selectAmount = list.get(i).getAmount();
                            break;
                        }
                    }
                }
                if (isChoose) {
                    if (flag == 0) {//其他
                        couponFlag = 0;
                        selectCard();
                    } else {
                        if (couponId > 0) {
                            if (canUseServiceCard == 1) {
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("提示").setMsg("您已选择的优惠券不可与E卡同时使用,使用E卡会清除已选中的优惠券")
                                        .isOneBtn(false).setNegativeButton("我再想想", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setPositiveButton("使用E卡", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        couponFlag = 1;
                                        selectCard();
                                    }
                                }).show();
                            } else if (canUseServiceCard == 0) {
                                if (flag == 1) {//洗美特
                                    mPDialog.showDialog();
                                    CommUtil.confirmOrderPromptNew(null, mContext, 1, serviceLoc, strp, workerId, tid, appointment, null
                                            , null, pickup, shopId
                                            , selectId, couponId, homeCouponId, 0, confirmOrderPromptHandler);
                                } else if (flag == 2) {//寄养
                                    mPDialog.showDialog();
                                    CommUtil.getFosterOrderConfirmation(this, careShopId, roomType, mypetId, shopId, startTime, endTime, strp, extraPetIds, couponId, selectId, confirmOrderPromptHandler);
                                } else if (flag == 3) {//升级订单
                                    mPDialog.showDialog();
                                    CommUtil.confirmOrderPromptNew(null, mContext, 1, serviceLoc, strp,
                                            workerId, 0, appointment, null, null, pickup, shopId, selectId, couponId, 0, updateOrderId, confirmOrderPromptHandler);
                                } else if (flag == 4) {//商品
                                    couponFlag = 0;
                                    selectCard();
                                }
                            }
                        } else {
                            couponFlag = 0;
                            selectCard();
                        }
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("id", -1);
                    setResult(Global.RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    private void selectCard() {
        Intent intent = new Intent();
        intent.putExtra("id", selectId);
        intent.putExtra("discountText", selectDiscountText);
        intent.putExtra("cardTypeName", selectCardTypeName);
        intent.putExtra("amount", selectAmount);
        intent.putExtra("couponFlag", couponFlag);
        setResult(Global.RESULT_OK, intent);
        finish();
    }

    private AsyncHttpResponseHandler confirmOrderPromptHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                String msg = object.getString("msg");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("cardPayPrice") && !objectData.isNull("cardPayPrice")) {
                            cardPayPrice = objectData.getDouble("cardPayPrice");
                        }
                    }
                    if (cardPayPrice <= 0) {
                        new AlertDialogDefault(mContext).builder()
                                .setTitle("提示").setMsg("您已选择的优惠券已完全抵扣订单,使用E卡会取消已选择的优惠券")
                                .isOneBtn(false).setNegativeButton("我再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("使用E卡", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                couponFlag = 1;
                                selectCard();
                            }
                        }).show();
                    } else {
                        couponFlag = 0;
                        selectCard();
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(mContext, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
