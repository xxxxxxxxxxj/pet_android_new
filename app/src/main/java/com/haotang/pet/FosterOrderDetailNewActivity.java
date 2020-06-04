package com.haotang.pet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.FosterOrderDetailPetAdapter;
import com.haotang.pet.adapter.WashOrderDetailDiscountAdapter;
import com.haotang.pet.adapter.WashOrderDetailPayPricesItemAdapter;
import com.haotang.pet.entity.FosterPet;
import com.haotang.pet.entity.PayPricesItem;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.WashDiscount;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.FosterOrderPopupWindow;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.haotang.pet.view.ShadowLayout;
import com.tencent.mm.sdk.modelbase.BaseResp;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 寄养订单详情页
 */
public class FosterOrderDetailNewActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.btn_fosterdetail_cancelorder)
    Button btnFosterdetailCancelorder;
    @BindView(R.id.btn_fosterdetail_gopay)
    Button btnFosterdetailGopay;
    @BindView(R.id.ll_fosterdetail_dfk)
    LinearLayout llFosterdetailDfk;
    @BindView(R.id.btn_fosterdetail_bottom)
    Button btnFosterdetailBottom;
    @BindView(R.id.sl_fosterdetail_bottom)
    ShadowLayout slFosterdetailBottom;
    @BindView(R.id.rl_fosterdetail_bottom)
    RelativeLayout rlFosterdetailBottom;
    @BindView(R.id.tv_fosterdetail_statedesc)
    TextView tvFosterdetailStatedesc;
    @BindView(R.id.tv_fosterdetail_minute)
    TextView tvFosterdetailMinute;
    @BindView(R.id.tv_fosterdetail_second)
    TextView tvFosterdetailSecond;
    @BindView(R.id.tv_fosterdetail_desc)
    TextView tvFosterdetailDesc;
    @BindView(R.id.tv_fosterdetail_shopname)
    TextView tvFosterdetailShopname;
    @BindView(R.id.ll_fosterdetail_shopname)
    LinearLayout llFosterdetailShopname;
    @BindView(R.id.tv_fosterdetail_roomnum)
    TextView tvFosterdetailRoomnum;
    @BindView(R.id.tv_fosterdetail_rzriqi)
    TextView tvFosterdetailRzriqi;
    @BindView(R.id.tv_fosterdetail_rztime)
    TextView tvFosterdetailRztime;
    @BindView(R.id.tv_fosterdetail_ldriqi)
    TextView tvFosterdetailLdriqi;
    @BindView(R.id.tv_fosterdetail_ldtime)
    TextView tvFosterdetailLdtime;
    @BindView(R.id.tv_fosterdetail_roomtotalprice)
    TextView tvFosterdetailRoomtotalprice;
    @BindView(R.id.tv_fosterdetail_roomname)
    TextView tvFosterdetailRoomname;
    @BindView(R.id.tv_fosterdetail_roomdj)
    TextView tvFosterdetailRoomdj;
    @BindView(R.id.rv_fosterdetail_pet)
    RecyclerView rvFosterdetailPet;
    @BindView(R.id.tv_fosterdetail_totalprice)
    TextView tvFosterdetailTotalprice;
    @BindView(R.id.rv_fosterdetail_discount)
    RecyclerView rvFosterdetailDiscount;
    @BindView(R.id.tv_fosterdetail_sjprice)
    TextView tvFosterdetailSjprice;
    @BindView(R.id.rv_fosterdetail_payway)
    RecyclerView rvFosterdetailPayway;
    @BindView(R.id.tv_fosterdetail_usertitle)
    TextView tvFosterdetailUsertitle;
    @BindView(R.id.tv_fosterdetail_username)
    TextView tvFosterdetailUsername;
    @BindView(R.id.tv_fosterdetail_userphone)
    TextView tvFosterdetailUserphone;
    @BindView(R.id.tv_fosterdetail_remarktitle)
    TextView tvFosterdetailRemarktitle;
    @BindView(R.id.tv_fosterdetail_remark)
    TextView tvFosterdetailRemark;
    @BindView(R.id.tv_fosterdetail_zffs)
    TextView tvFosterdetailZffs;
    @BindView(R.id.tv_fosterdetail_copy)
    TextView tvFosterdetailCopy;
    @BindView(R.id.tv_fosterdetail_ddbh)
    TextView tvFosterdetailDdbh;
    @BindView(R.id.tv_fosterdetail_tjsj)
    TextView tvFosterdetailTjsj;
    @BindView(R.id.tv_fosterdetail_tkprice)
    TextView tvFosterdetailTkprice;
    @BindView(R.id.rl_fosterdetail_tk)
    RelativeLayout rlFosterdetailTk;
    @BindView(R.id.ll_fosterdetail_djs)
    LinearLayout llFosterdetailDjs;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    private PopupWindow pWinBottomDialog;
    private Timer timer;
    private TimerTask task;
    private String minute = "";
    private String second = "";
    private TextView tv_pay_bottomdia_time_second;
    private TextView tv_pay_bottomdia_time_minute;
    private int myselfDummpNum;
    private StringBuilder listpayWays = new StringBuilder();
    private int oldpayway;
    private int paytype;
    private int orderNo;
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private long lastOverTime = 900000;
    private int status;
    private ArrayList<String> discountList = new ArrayList<String>();
    private String orderNum;
    private List<WashDiscount> localDiscountList = new ArrayList<WashDiscount>();
    private WashOrderDetailDiscountAdapter washOrderDetailDiscountAdapter;
    private List<PayPricesItem> payPricesItemList = new ArrayList<PayPricesItem>();
    private WashOrderDetailPayPricesItemAdapter washOrderDetailPayPricesItemAdapter;
    private List<FosterPet> petList = new ArrayList<FosterPet>();
    private List<FosterPet> carePetList = new ArrayList<FosterPet>();
    private FosterOrderDetailPetAdapter fosterOrderDetailPetAdapter;
    private double payPrice;
    private double thirdPrice;
    private FosterOrderPopupWindow fosterOrderPopupWindow;
    private int couponId;
    private double shopLat;
    private double shopLng;
    private String shopName;
    private String shopPhone;
    private String shopAddress;
    private String startTime;
    private double refundPrice;
    private String customerName;
    private String customerPhone;
    private String remark;
    private int myPetId;
    private int careShopId;
    private int shopId;
    private String extraPetIds;
    private String serviceCardTypeName;
    private int serviceCardId;
    private int roomType;
    private int days;
    private double roomUnitPrice;
    private String endTime;
    private String topTip;
    private int cameraState;
    private String liveContent;
    private String fosterEndTime;
    private String fosterStartTime;
    private String shopImg;
    private String liveUrl;
    private String roomNum;

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    if (TextUtils.equals(resultStatus, "9000")) {
                        goPaySuccess();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this,
                                    "支付结果确认中!");
                        } else {
                            ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    PayUtils.payByAliPay(FosterOrderDetailNewActivity.this, orderStr,
                            aliHandler, mPDialog);
                    break;
                case 0:
                    long lastTimerShowHMS = msg.arg1;
                    String time = Utils.formatTimeFM(lastTimerShowHMS);
                    minute = time.substring(0, 2);
                    second = time.substring(3, 5);
                    if (tv_pay_bottomdia_time_minute != null && tv_pay_bottomdia_time_second != null) {
                        tv_pay_bottomdia_time_minute.setText(minute);
                        tv_pay_bottomdia_time_second.setText(second);
                    }
                    tvFosterdetailMinute.setText(minute);
                    tvFosterdetailSecond.setText(second);
                    break;
                case 1:
                    if (lastOverTime <= 0) {
                        ToastUtil.showToastShortBottom(mContext, "抱歉您的订单已超时");
                    }
                    finish();
                    break;
            }
        }
    };

    //微信支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayResultEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            BaseResp resp = event.getResp();
            if (resp != null) {
                if (resp.errCode == 0) {
                    if ((Build.MODEL.equals("OPPO R9m") || Build.MODEL.equals("OPPO R9s")) && Build.VERSION.RELEASE.equals("5.1")) {
                        Log.e("TAG", "OPPO哦");
                    } else {
                        goPaySuccess();
                    }
                } else {
                    if (Utils.isStrNull(resp.errStr)) {
                        ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, "支付失败");
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.toOrderFragment(FosterOrderDetailNewActivity.this);
        super.onBackPressed();
    }

    //刷新订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshOrderEvent event) {
        if (event != null && event.isRefresh()) {
            getData();
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
        Global.WXPAYCODE = -1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        orderNo = getIntent().getIntExtra("orderid", 0);
        oldpayway = spUtil.getInt("payway", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_foster_order_detail_new);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("订单详情");
        ibTitlebarOther.setBackgroundResource(R.drawable.order_right_icon_foster);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 40), Utils.dip2px(mContext, 22));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ibTitlebarOther.setLayoutParams(layoutParams);

        rvFosterdetailPet.setHasFixedSize(true);
        rvFosterdetailPet.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager2 = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager2.setScrollEnabled(false);
        rvFosterdetailPet.setLayoutManager(noScollFullLinearLayoutManager2);
        fosterOrderDetailPetAdapter = new FosterOrderDetailPetAdapter(R.layout.item_fosterorderdetail_pet, petList);
        rvFosterdetailPet.setAdapter(fosterOrderDetailPetAdapter);

        rvFosterdetailDiscount.setHasFixedSize(true);
        rvFosterdetailDiscount.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvFosterdetailDiscount.setLayoutManager(noScollFullLinearLayoutManager);
        washOrderDetailDiscountAdapter = new WashOrderDetailDiscountAdapter(R.layout.item_washorderdetail_discount, localDiscountList);
        rvFosterdetailDiscount.setAdapter(washOrderDetailDiscountAdapter);

        rvFosterdetailPayway.setHasFixedSize(true);
        rvFosterdetailPayway.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager1 = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager1.setScrollEnabled(false);
        rvFosterdetailPayway.setLayoutManager(noScollFullLinearLayoutManager1);
        washOrderDetailPayPricesItemAdapter = new WashOrderDetailPayPricesItemAdapter(R.layout.item_washorderdetail_paypriceitem, payPricesItemList);
        rvFosterdetailPayway.setAdapter(washOrderDetailPayPricesItemAdapter);
    }

    private void setLinster() {

    }

    private void getData() {
        petList.clear();
        localDiscountList.clear();
        payPricesItemList.clear();
        mPDialog.showDialog();
        status = 0;
        refundPrice = 0;
        payPrice = 0;
        thirdPrice = 0;
        topTip = "";
        liveContent = "";
        liveUrl = "";
        cameraState = 0;
        roomNum = "";
        CommUtil.hotelOrderInfo(this, orderNo, hotelOrderInfoHandler);
    }

    private AsyncHttpResponseHandler hotelOrderInfoHandler = new AsyncHttpResponseHandler() {

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
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("statusDescription") && !jData.isNull("statusDescription")) {
                            Utils.setText(tvFosterdetailStatedesc, jData.getString("statusDescription"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("hotelStatus") && !jData.isNull("hotelStatus")) {
                            status = jData.getInt("hotelStatus");
                        }
                        if (jData.has("shop") && !jData.isNull("shop")) {
                            JSONObject jshop = jData.getJSONObject("shop");
                            if (jshop.has("lng") && !jshop.isNull("lng")) {
                                shopLng = jshop.getDouble("lng");
                            }
                            if (jshop.has("lat") && !jshop.isNull("lat")) {
                                shopLat = jshop.getDouble("lat");
                            }
                            if (jshop.has("phone") && !jshop.isNull("phone")) {
                                shopPhone = jshop.getString("phone");
                            }
                            if (jshop.has("hotelName") && !jshop.isNull("hotelName")) {
                                shopName = jshop.getString("hotelName");
                                Utils.setText(tvFosterdetailShopname, shopName, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jshop.has("address") && !jshop.isNull("address")) {
                                shopAddress = jshop.getString("address");
                            }
                            if (jshop.has("img") && !jshop.isNull("img")) {
                                shopImg = jshop.getString("img");
                            }
                        }
                        if (jData.has("startTime") && !jData.isNull("startTime")) {
                            startTime = jData.getString("startTime").split(" ")[0];
                            fosterStartTime = jData.getString("startTime").split(" ")[1];
                            Utils.setText(tvFosterdetailRzriqi, startTime, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("endTime") && !jData.isNull("endTime")) {
                            endTime = jData.getString("endTime").split(" ")[0];
                            fosterEndTime = jData.getString("endTime").split(" ")[1];
                            Utils.setText(tvFosterdetailLdriqi, endTime, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("roomPrice") && !jData.isNull("roomPrice")) {
                            JSONObject jroomPrice = jData.getJSONObject("roomPrice");
                            if (jroomPrice.has("total") && !jroomPrice.isNull("total")) {
                                Utils.setText(tvFosterdetailRoomtotalprice, "¥" + jroomPrice.getDouble("total"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jroomPrice.has("price") && !jroomPrice.isNull("price")) {
                                roomUnitPrice = jroomPrice.getDouble("price");
                            }
                            if (jroomPrice.has("days") && !jroomPrice.isNull("days")) {
                                days = jroomPrice.getInt("days");
                            }
                        }
                        if (jData.has("roomType") && !jData.isNull("roomType")) {
                            JSONObject jroomType = jData.getJSONObject("roomType");
                            if (jroomType.has("name") && !jroomType.isNull("name")) {
                                Utils.setText(tvFosterdetailRoomname, jroomType.getString("name"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jroomType.has("id") && !jroomType.isNull("id")) {
                                roomType = jroomType.getInt("id");
                            }
                        }
                        if (jData.has("petInfoList") && !jData.isNull("petInfoList")) {
                            JSONArray jarrpet = jData.getJSONArray("petInfoList");
                            for (int i = 0; i < jarrpet.length(); i++) {
                                petList.add(FosterPet.jsonToEntity(jarrpet.getJSONObject(i), status));
                            }
                        }
                        if (jData.has("totalPrice") && !jData.isNull("totalPrice")) {
                            Utils.setText(tvFosterdetailTotalprice, "¥" + jData.getDouble("totalPrice"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("serviceCard") && !jData.isNull("serviceCard")) {
                            JSONObject jserviceCard = jData.getJSONObject("serviceCard");
                            if (jserviceCard.has("serviceCardId") && !jserviceCard.isNull("serviceCardId")) {
                                serviceCardId = jserviceCard.getInt("serviceCardId");
                            }
                            if (jserviceCard.has("serviceCardTypeName") && !jserviceCard.isNull("serviceCardTypeName")) {
                                serviceCardTypeName = jserviceCard.getString("serviceCardTypeName");
                            }
                            if (jserviceCard.has("discountCost") && !jserviceCard.isNull("discountCost")) {
                                double discountCost = jserviceCard.getDouble("discountCost");
                                if (discountCost > 0) {
                                    localDiscountList.add(new WashDiscount("", "", String.valueOf(discountCost), "1"));
                                }
                            }
                            if (jserviceCard.has("discountPrice") && !jserviceCard.isNull("discountPrice")) {
                                double discountPrice = jserviceCard.getDouble("discountPrice");
                                if (discountPrice > 0) {
                                    payPricesItemList.add(new PayPricesItem(serviceCardTypeName, String.valueOf(discountPrice)));
                                }
                            }
                        }
                        if (jData.has("couponPrice") && !jData.isNull("couponPrice")) {
                            double couponPrice = jData.getDouble("couponPrice");
                            if (couponPrice > 0) {
                                localDiscountList.add(new WashDiscount("", "", String.valueOf(couponPrice), "2"));
                            }
                        }
                        if (jData.has("payInfo") && !jData.isNull("payInfo")) {
                            JSONObject jpayInfo = jData.getJSONObject("payInfo");
                            if (jpayInfo.has("shopId") && !jpayInfo.isNull("shopId")) {
                                shopId = jpayInfo.getInt("shopId");
                            }
                            if (jpayInfo.has("careShopId") && !jpayInfo.isNull("careShopId")) {
                                careShopId = jpayInfo.getInt("careShopId");
                            }
                            if (jpayInfo.has("myPetId") && !jpayInfo.isNull("myPetId")) {
                                myPetId = jpayInfo.getInt("myPetId");
                            }
                            if (jpayInfo.has("extraPetIds") && !jpayInfo.isNull("extraPetIds")) {
                                extraPetIds = jpayInfo.getString("extraPetIds");
                            }
                            if (jpayInfo.has("couponId") && !jpayInfo.isNull("couponId")) {
                                couponId = jpayInfo.getInt("couponId");
                            }
                        }
                        if (jData.has("refundPrice") && !jData.isNull("refundPrice")) {
                            refundPrice = jData.getDouble("refundPrice");
                        }
                        if (jData.has("payPrice") && !jData.isNull("payPrice")) {
                            payPrice = jData.getDouble("payPrice");
                        }
                        if (jData.has("customerInfo") && !jData.isNull("customerInfo")) {
                            JSONObject jcustomerInfo = jData.getJSONObject("customerInfo");
                            if (jcustomerInfo.has("customerPhone") && !jcustomerInfo.isNull("customerPhone")) {
                                customerPhone = jcustomerInfo.getString("customerPhone");
                                Utils.setText(tvFosterdetailUserphone, customerPhone, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jcustomerInfo.has("customerName") && !jcustomerInfo.isNull("customerName")) {
                                customerName = jcustomerInfo.getString("customerName");
                                Utils.setText(tvFosterdetailUsername, customerName, "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jcustomerInfo.has("remark") && !jcustomerInfo.isNull("remark")) {
                                remark = jcustomerInfo.getString("remark");
                                Utils.setText(tvFosterdetailRemark, remark, "", View.VISIBLE, View.VISIBLE);
                            }
                        }
                        if (jData.has("payWayName") && !jData.isNull("payWayName")) {
                            Utils.setText(tvFosterdetailZffs, jData.getString("payWayName"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("thirdPrice") && !jData.isNull("thirdPrice")) {
                            thirdPrice = jData.getDouble("thirdPrice");
                        }
                        if (jData.has("orderNum") && !jData.isNull("orderNum")) {
                            orderNum = jData.getString("orderNum");
                            Utils.setText(tvFosterdetailDdbh, orderNum, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("created") && !jData.isNull("created")) {
                            Utils.setText(tvFosterdetailTjsj, jData.getString("created"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jData.has("topTip") && !jData.isNull("topTip")) {
                            topTip = jData.getString("topTip");
                        }
                        if (jData.has("lastSecs") && !jData.isNull("lastSecs")) {
                            lastOverTime = jData.getLong("lastSecs");
                        }
                        if (jData.has("liveContent") && !jData.isNull("liveContent")) {
                            liveContent = jData.getString("liveContent");
                        }
                        if (jData.has("liveInfo") && !jData.isNull("liveInfo")) {
                            JSONObject jliveInfo = jData.getJSONObject("liveInfo");
                            if (jliveInfo.has("liveUrl") && !jliveInfo.isNull("liveUrl")) {
                                liveUrl = jliveInfo.getString("liveUrl");
                            }
                            if (jliveInfo.has("cameraState") && !jliveInfo.isNull("cameraState")) {
                                cameraState = jliveInfo.getInt("cameraState");
                            }
                            if (jliveInfo.has("roomNum") && !jliveInfo.isNull("roomNum")) {
                                roomNum = jliveInfo.getString("roomNum");
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, "数据异常");
            }
            if (refundPrice > 0) {
                rlFosterdetailTk.setVisibility(View.VISIBLE);
                Utils.setText(tvFosterdetailTkprice, "¥" + refundPrice, "", View.VISIBLE, View.VISIBLE);
            } else {
                rlFosterdetailTk.setVisibility(View.GONE);
            }
            washOrderDetailDiscountAdapter.notifyDataSetChanged();
            washOrderDetailPayPricesItemAdapter.notifyDataSetChanged();
            fosterOrderDetailPetAdapter.notifyDataSetChanged();
            Utils.setText(tvFosterdetailSjprice, "¥" + payPrice, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvFosterdetailRoomnum, "共" + days + "天", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvFosterdetailRoomdj, "¥" + roomUnitPrice + "*" + days + "天", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvFosterdetailRztime, fosterStartTime + "入住", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvFosterdetailLdtime, fosterEndTime + "离店", "", View.VISIBLE, View.VISIBLE);
            ibTitlebarOther.setVisibility(View.GONE);
            if (status == 1) {//待付款
                rlFosterdetailBottom.setVisibility(View.VISIBLE);
                llFosterdetailDfk.setVisibility(View.VISIBLE);
                llFosterdetailDjs.setVisibility(View.VISIBLE);
                slFosterdetailBottom.setVisibility(View.GONE);
                tvFosterdetailDesc.setVisibility(View.GONE);
                timerDown();
                getPayWay();
            } else {
                rlFosterdetailBottom.setVisibility(View.GONE);
                llFosterdetailDjs.setVisibility(View.GONE);
                llFosterdetailDfk.setVisibility(View.GONE);
                slFosterdetailBottom.setVisibility(View.GONE);
                tvFosterdetailDesc.setVisibility(View.VISIBLE);
                Utils.setText(tvFosterdetailDesc, topTip, "", View.VISIBLE, View.GONE);
                if (status == 2) {//已付款
                    ibTitlebarOther.setVisibility(View.VISIBLE);
                } else if (status == 21) {//待入住
                    ibTitlebarOther.setVisibility(View.VISIBLE);
                } else if (status == 22) {//已入住
                    ibTitlebarOther.setVisibility(View.VISIBLE);
                    rlFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setBackgroundResource(R.drawable.bg_red_round16);
                    slFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setText("查看直播");
                } else if (status == 4) {//待评价
                    rlFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setBackgroundResource(R.drawable.bg_red_round16);
                    slFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setText("评价得罐头币");
                } else if (status == 5) {//已完成
                    rlFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setBackgroundResource(R.drawable.bg_333_round);
                    slFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setText("再来一单");
                } else if (status == 6) {//取消
                    rlFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setBackgroundResource(R.drawable.bg_333_round);
                    slFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setText("重新下单");
                } else if (status == 7) {//未支付取消
                    rlFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setBackgroundResource(R.drawable.bg_333_round);
                    slFosterdetailBottom.setVisibility(View.VISIBLE);
                    btnFosterdetailBottom.setText("重新下单");
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, "请求失败");
        }
    };

    private void getPayWay() {
        listpayWays.setLength(0);
        mPDialog.showDialog();
        CommUtil.payWays(this, Global.ORDERKEY[3], 0, payWaysHandler);
    }

    private AsyncHttpResponseHandler payWaysHandler = new AsyncHttpResponseHandler() {
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
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("payWays") && !jData.isNull("payWays")) {
                            JSONArray jsonArray = jData.getJSONArray("payWays");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listpayWays.append(jsonArray.getString(i));
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(FosterOrderDetailNewActivity.this, "请求失败");
        }
    };

    private void timerDown() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (lastOverTime > 0) {
                    lastOverTime -= 1000;
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.arg1 = (int) lastOverTime;
                    aliHandler.sendMessage(msg);
                } else {
                    if (timer != null) {
                        aliHandler.sendEmptyMessage(1);
                        timer.cancel();
                        timer = null;
                    }
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(FosterOrderDetailNewActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(FosterOrderDetailNewActivity.this, R.layout.appoint_pay_bottom_dialog, null);
        Button btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        tv_pay_bottomdia_time_minute = (TextView) customView.findViewById(R.id.tv_pay_bottomdia_time_minute);
        tv_pay_bottomdia_time_second = (TextView) customView.findViewById(R.id.tv_pay_bottomdia_time_second);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        final ImageView iv_pay_bottomdia_zfb_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_zfb_select);
        pWinBottomDialog = new PopupWindow(customView,
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(FosterOrderDetailNewActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        tv_pay_bottomdia_time_minute.setText(minute);
        tv_pay_bottomdia_time_second.setText(second);
        btn_pay_bottomdia.setText("确认支付¥" + thirdPrice);
        if (listpayWays.toString().contains("1")) {
            ll_pay_bottomdia_weixin.setVisibility(View.VISIBLE);
            if (oldpayway == 1) {
                paytype = 1;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_weixin.setVisibility(View.GONE);
        }
        if (listpayWays.toString().contains("2")) {
            ll_pay_bottomdia_zfb.setVisibility(View.VISIBLE);
            if (oldpayway == 2) {
                paytype = 2;
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_zfb.setVisibility(View.GONE);
        }
        tv_pay_title.setText("请选择支付方式");
        iv_pay_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        btn_pay_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paytype != 1 && paytype != 2) {
                    ToastUtil.showToastShortBottom(mContext, "请选择支付方式");
                    return;
                }
                hotelChangePayWay();
            }
        });
        ll_pay_bottomdia_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytype = 1;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
            }
        });
        ll_pay_bottomdia_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytype = 2;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
            }
        });
    }

    private String getStrp() {
        carePetList.clear();
        for (int i = 0; i < petList.size(); i++) {
            if (petList.get(i).getTid() > 0) {
                carePetList.add(petList.get(i));
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        if (carePetList.size() > 0) {
            for (int i = 0; i < petList.size(); i++) {
                FosterPet fosterPet = petList.get(i);
                if (i < petList.size() - 1) {
                    sb.append(fosterPet.getPetId());
                    sb.append("_");
                    sb.append(fosterPet.getServiceId());
                    sb.append("_");
                    sb.append(fosterPet.getMyPetId());
                    sb.append("_");
                    sb.append(fosterPet.getTid());
                    sb.append("_");
                } else {
                    sb.append(fosterPet.getPetId());
                    sb.append("_");
                    sb.append(fosterPet.getServiceId());
                    sb.append("_");
                    sb.append(fosterPet.getMyPetId());
                    sb.append("_");
                    sb.append(fosterPet.getTid());
                }
            }
        }
        return sb.toString();
    }

    private void hotelChangePayWay() {
        mPDialog.showDialog();
        CommUtil.hotelPay(mContext, careShopId, orderNo, shopId, 2, myPetId, getStrp(), roomType, couponId, startTime, endTime, serviceCardId, payPrice,
                paytype, remark, customerName, customerPhone, extraPetIds, hotelPayHanler);
    }

    private AsyncHttpResponseHandler hotelPayHanler = new AsyncHttpResponseHandler() {
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
                        if (jdata.has("give_can") && !jdata.isNull("give_can")) {
                            JSONObject obGiveCan = jdata.getJSONObject("give_can");
                            if (obGiveCan.has("myself") && !obGiveCan.isNull("myself")) {
                                myselfDummpNum = obGiveCan.getInt("myself");
                            }
                        }
                        if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
                            JSONObject jpayInfo = jdata.getJSONObject("payInfo");
                            if (jpayInfo.has("appid") && !jpayInfo.isNull("appid")) {
                                appid = jpayInfo.getString("appid");
                            }
                            if (jpayInfo.has("noncestr")
                                    && !jpayInfo.isNull("noncestr")) {
                                noncestr = jpayInfo.getString("noncestr");
                            }
                            if (jpayInfo.has("package")
                                    && !jpayInfo.isNull("package")) {
                                packageValue = jpayInfo.getString("package");
                            }
                            if (jpayInfo.has("partnerid")
                                    && !jpayInfo.isNull("partnerid")) {
                                partnerid = jpayInfo.getString("partnerid");
                            }
                            if (jpayInfo.has("prepayid")
                                    && !jpayInfo.isNull("prepayid")) {
                                prepayid = jpayInfo.getString("prepayid");
                            }
                            if (jpayInfo.has("sign") && !jpayInfo.isNull("sign")) {
                                sign = jpayInfo.getString("sign");
                            }
                            if (jpayInfo.has("timestamp")
                                    && !jpayInfo.isNull("timestamp")) {
                                timestamp = jpayInfo.getString("timestamp");
                            }
                            if (jpayInfo.has("orderStr")
                                    && !jpayInfo.isNull("orderStr")) {
                                orderStr = jpayInfo.getString("orderStr");
                            }
                        }
                        goPay();
                    }
                } else {
                    if (resultCode == Global.PAY_SUCCESS) {
                        goPaySuccess();
                    }
                    ToastUtil.showToastShort(mContext, msg);
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

    private void goPay() {
        if (paytype == 1) {// 微信支付
            spUtil.saveInt("payway", 1);
            mPDialog.showDialog();
            PayUtils.weChatPayment(FosterOrderDetailNewActivity.this, appid, partnerid,
                    prepayid, packageValue, noncestr, timestamp, sign,
                    mPDialog);
        } else if (paytype == 2) {//支付宝支付
            // 判断是否安装支付宝
            spUtil.saveInt("payway", 2);
            PayUtils.checkAliPay(FosterOrderDetailNewActivity.this, aliHandler);
        }
    }

    private void goPaySuccess() {
        for (int i = 0; i < MApplication.listAppoint.size(); i++) {
            MApplication.listAppoint.get(i).finish();
        }
        MApplication.listAppoint.clear();
        for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
            MApplication.listAppoint1.get(i).finish();
        }
        MApplication.listAppoint.clear();
        MApplication.listAppoint1.clear();
        Intent intent = new Intent(this, PaySuccessNewActivity.class);
        double totalDiscountPrice = 0;
        if (localDiscountList != null && localDiscountList.size() > 0) {
            discountList.clear();
            for (int i = 0; i < localDiscountList.size(); i++) {
                totalDiscountPrice = ComputeUtil.add(totalDiscountPrice, Double.parseDouble(localDiscountList.get(i).getAmount()));
                if (Integer.parseInt(localDiscountList.get(i).getType()) == 1) {//服务卡减免类型标识
                    discountList.add("E卡优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 2) {//优惠券减免类型标识
                    discountList.add("优惠券优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 3) {//上门优惠券减免类型标识
                    discountList.add("上门优惠券优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 4) {//次卡减免类型标识
                    discountList.add("次卡优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 5) {//罐头币减免类型标识
                    discountList.add("罐头币优惠¥" + localDiscountList.get(i).getAmount());
                } else if (Integer.parseInt(localDiscountList.get(i).getType()) == 6) {//上门服务费减免类型标识
                    discountList.add("上门服务费减免优惠¥" + localDiscountList.get(i).getAmount());
                }
            }
            discountList.add(0, "共计优惠¥" + totalDiscountPrice);
            intent.putStringArrayListExtra("discountList", discountList);
        }
        double sub = ComputeUtil.sub(payPrice, totalDiscountPrice);
        intent.putExtra("payPrice", sub < 0 ? 0 : sub);
        intent.putExtra("orderId", orderNo);
        intent.putExtra("type", 2);
        intent.putExtra("pageType", 5);
        intent.putExtra("myself", myselfDummpNum);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if ((Build.MODEL.equals("OPPO R9m") || Build.MODEL.equals("OPPO R9s")) && Build.VERSION.RELEASE.equals("5.1") && Global.WXPAYCODE == 0) {
            Global.WXPAYCODE = -1;
            goPaySuccess();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        aliHandler.removeCallbacksAndMessages(null);
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.rl_fosterdetail_remark, R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.btn_fosterdetail_cancelorder, R.id.btn_fosterdetail_gopay, R.id.btn_fosterdetail_bottom, R.id.ll_fosterdetail_shopname, R.id.tv_fosterdetail_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_fosterdetail_remark:
                Intent intent1 = new Intent(mContext, OrderDetailServiceNoteActivity.class);
                intent1.putExtra("note", remark);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_open, R.anim.bottom_silent);
                break;
            case R.id.ib_titlebar_back:
                ActivityUtils.toOrderFragment(FosterOrderDetailNewActivity.this);
                finish();
                break;
            case R.id.ib_titlebar_other:
                showPop();
                break;
            case R.id.btn_fosterdetail_cancelorder:
                Intent intentCancle = new Intent(mContext, OrderCancleReasonNewActivity.class);
                intentCancle.putExtra("type", 2);
                intentCancle.putExtra("orderid", orderNo);
                startActivity(intentCancle);
                break;
            case R.id.btn_fosterdetail_gopay:
                if (listpayWays != null && listpayWays.length() > 0) {
                    if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                        showPayDialog();
                    }
                }
                break;
            case R.id.btn_fosterdetail_bottom:
                if (status == 22) {//已入住
                    if (cameraState == 0) {//摄像头状态 0 开启 1 关闭
                        Intent intent = new Intent(mContext, FosterLiveActivity.class);
                        intent.putExtra("videoUrl", liveUrl);
                        intent.putExtra("name", roomNum);
                        startActivity(intent);
                    } else {
                        ToastUtil.showToastShortBottom(mContext, liveContent);
                    }
                } else if (status == 4) {//待评价
                    Intent intent = new Intent(mContext, FosterEvaluteActivity.class);
                    intent.putExtra("orderId", orderNo);
                    intent.putExtra("shopName", shopName);
                    intent.putExtra("shopImg", shopImg);
                    startActivity(intent);
                } else if (status == 5) {//已完成
                    for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                        MApplication.listAppoint.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                        MApplication.listAppoint1.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    MApplication.listAppoint1.clear();
                    startActivity(new Intent(mContext, FosterHomeActivity.class));
                    finish();
                } else if (status == 6) {//取消
                    for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                        MApplication.listAppoint.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                        MApplication.listAppoint1.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    MApplication.listAppoint1.clear();
                    startActivity(new Intent(mContext, FosterHomeActivity.class));
                    finish();
                } else if (status == 7) {//未支付取消
                    for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                        MApplication.listAppoint.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                        MApplication.listAppoint1.get(i).finish();
                    }
                    MApplication.listAppoint.clear();
                    MApplication.listAppoint1.clear();
                    startActivity(new Intent(mContext, FosterHomeActivity.class));
                    finish();
                }
                break;
            case R.id.ll_fosterdetail_shopname:
                showShopDialog();
                break;
            case R.id.tv_fosterdetail_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(orderNum);
                ToastUtil.showToastShortCenter(mContext, "复制成功");
                break;
        }
    }

    private void showShopDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(FosterOrderDetailNewActivity.this, R.layout.foster_shop_dialog, null);
        TextView tv_fostershopdialog_lxmd = (TextView) customView.findViewById(R.id.tv_fostershopdialog_lxmd);
        TextView tv_fostershopdialog_dhdd = (TextView) customView.findViewById(R.id.tv_fostershopdialog_dhdd);
        TextView tv_fostershopdialog_qx = (TextView) customView.findViewById(R.id.tv_fostershopdialog_qx);
        RelativeLayout rl_fostershopdialog_parent = (RelativeLayout) customView.findViewById(R.id.rl_fostershopdialog_parent);
        final PopupWindow popupWindow = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setWidth(Utils.getDisplayMetrics(FosterOrderDetailNewActivity.this)[0]);
        popupWindow.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        tv_fostershopdialog_lxmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.telePhoneBroadcast(mContext, shopPhone);
                    }
                }).show();
            }
        });
        tv_fostershopdialog_dhdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(FosterOrderDetailNewActivity.this, FosterNavigationActivity.class);
                intent.putExtra("lat", shopLat);
                intent.putExtra("lng", shopLng);
                intent.putExtra("address", shopAddress);
                intent.putExtra("name", shopName);
                startActivity(intent);
            }
        });
        tv_fostershopdialog_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        rl_fostershopdialog_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void showPop() {
        dismissPop();
        fosterOrderPopupWindow = new FosterOrderPopupWindow(this, onClickListener, status);
        fosterOrderPopupWindow.showAsDropDown(ibTitlebarOther, -10, -30);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_washorder_sqtk:
                    Intent intentCancle = new Intent(mContext, OrderCancleActivity.class);
                    intentCancle.putExtra("type", 2);
                    intentCancle.putExtra("orderid", orderNo);
                    intentCancle.putExtra("couponId", couponId);
                    startActivity(intentCancle);
                    dismissPop();
                    break;
                case R.id.tv_pop_washorder_lxkf:
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("提示").setMsg("是否拨打电话？").isOneBtn(false).setNegativeButton("否", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("是", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.telePhoneBroadcast(mContext, shopPhone);
                        }
                    }).show();
                    dismissPop();
                    break;
            }
        }
    };

    private void dismissPop() {
        if (fosterOrderPopupWindow != null) {
            if (fosterOrderPopupWindow.isShowing()) {
                fosterOrderPopupWindow.dismiss();
            }
        }
    }
}
