package com.haotang.pet;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.BeforeAfterPics;
import com.haotang.pet.entity.OrdersBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshScrollView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 游泳，训犬订单详情页
 *
 * @author Administrator
 */
@SuppressLint("ResourceAsColor")
public class OrderDetailFromOrderToConfirmActivity extends SuperActivity implements OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private Button bt_titlebar_other;
    private Button btPay;
    private TextView tvServicename;
    private TextView tvPrice;
    private TextView tvBeauticianName;
    private TextView tvBeauticianOrdernum;
    private TextView tvBeauticianLevel;
    private ImageView ivBeauticianLevel1;
    private ImageView ivBeauticianLevel2;
    private ImageView ivBeauticianLevel3;
    private ImageView ivBeauticianLevel4;
    private ImageView ivBeauticianLevel5;
    private int OrderId = 0;
    private TextView textView_yuyue_time;
    private TextView tvAddrType;
    private TextView tvAddrTypeDes;
    private TextView textView_people_name;
    private TextView textView_people_phone;
    private TextView textView_address_detail;
    private TextView textView_remark_detail;
    private TextView order_status;
    private int state;
    private String statusDescription = "";
    private RelativeLayout layout_confirm;
    private LinearLayout order_showbase;
    private TextView textView_order_num;
    private TextView textView_order_time;
    private TextView textView_over;//需要在适当时间隐藏
    private TextView textView_over_time;//需要在适当时间隐藏
    private String petName = "";
    private String name = "";
    private double totalPrice = 0;
    private TextView textView_confirm_price_show;
    private TextView textview_old_price_andother;
    private TextView textview_old_price_bottom;
    private TextView textview_price_back;
    private TextView textView_pay_other;
    private PopupWindow pWinCancle;
    private TextView textview_order_show_base;
    private TextView textview_order_show_only;
    private TextView textView_order_GoShopOrGoHome;
    public static OrderDetailFromOrderToConfirmActivity orderConfirm;
    private RelativeLayout layout_order_pickup;
    private TextView textView_pickup_price;
    private MProgressDialog pDialog;
    private boolean isShowAll;
    private boolean hasupgrageorder;
    private double upgradefee;
    private double payfee;
    private PopupWindow pWinPacket;
    private ImageView ImageView_red_packets;
    private ImageView imageview_status;
    private int serviceId = 0;
    private int shopId = 0;
    private RelativeLayout layout_beautiful_work_to;
    private int beautician_id = 0;
    private ImageView imageview_to_service;
    private int serviceloc;
    private String phoneNum = "";
    private LinearLayout layout_addrtype;
    private SelectableRoundedImageView sriv_orderdetail_beautician_sex;
    private ImageView order_beau_level;
    private LinearLayout layout_order_show_base_one;
    private LinearLayout layout_order_show_only_one;
    private LinearLayout layout_order_show_upgradeitems_one;
    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private String beauPhone = "";
    private int type;
    private String amount = "";
    private double couponAmount = 0;
    private TextView textView_address;
    private String shopAddress = "";
    private boolean isVie = false;
    private long lastOverTime = 0;
    private Timer timer;
    private TimerTask task;
    private TextView textview_hurryup;
    private boolean isTimeFirst = true;
    private MReceiver receiver;
    private int payWay = 0;
    private String servicePhone;
    private TextView textview_member_show;
    private int areaId;
    private StringBuilder serviceIds = null;
    private String avatar = "";
    private String realName = "";
    private String appointment = "";
    private RelativeLayout layout_change_order_parent;
    private LinearLayout layout_cancle_order;
    private LinearLayout layout_change_order;
    private LinearLayout layout_people;
    private String levelName = "";
    private String orderTitle = "";
    private String btnTxt = "";
    private String cname;
    private String cphone;
    private ImageView image_change_custome;
    private RelativeLayout layout_confirm_eva_complaints;
    private RelativeLayout layout_copy_ordernum;
    private Button bt_only_eva;
    private Button bt_complaints;
    private OrdersBean ordersBean = new OrdersBean();
    private LinearLayout layout_compl_parent;
    private TextView textview_compl_title;
    private TextView textview_show_compl_reason;
    private TextView textview_show_compl_status;
    private LinearLayout layout_go_home_service_price;
    private TextView textview_go_home_service_tag;
    private TextView textview_go_home_service_price;
    private LinearLayout layout_go_home_manjianyouhui;
    private TextView textview_manjianyouhui_left;
    private TextView textview_manjianyouhui_right;
    private String couponCutReductionTag = "";
    private String CardTag = "";
    private RelativeLayout rl_orderdetail_promoterCode;
    private TextView tv_orderdetail_promoterCode;
    private ImageView image_base_show_left;//基础服务是否用次卡支付 用显示  不用隐藏
    private ImageView image_coupon_show_left;//优惠券
    private ImageView image_manjianyouhui_show_left;//满减
    private ImageView image_promoterCode_show_left;//邀请码
    private ImageView image_refund_show_left;//退款
    private RelativeLayout rl_orderdetail_refund;//退款
    private TextView tv_orderdetail_refund;//退款
    private double refund = 0;//退款金额
    private String canPayPrice;
    private LinearLayout layout_dummy_money_orderdetail;
    private TextView textview_dummy_detail;
    private LinearLayout layout_go_home_service_coupon;
    private TextView textview_go_home_service_couponprice;
    private double homeCouponAmount;
    private double extraItemPrice;
    private double cutDownMoney = 0;
    private RelativeLayout layout_shop;
    private ImageView img_shop_icon;
    private TextView textview_shopname;
    private TextView textview_address;
    private TextView textview_shopphone;
    private TextView textview_shopweixin;
    private TextView textview_shopdaohang;
    private TextView textview_shopinsidelooklook;
    private LinearLayout layout_worker;
    private ImageView img_worker_icon;
    private TextView textview_worker_name;
    private TextView textview_worker_accept_order_nums;
    private TextView textview_worker_level;
    private TextView textview_worker_eva_content;
    private LinearLayout layout_all_imgs;
    private RelativeLayout relayout_one;
    private ImageView img_one;
    private TextView textview_before_after_one;
    private RelativeLayout relayout_two;
    private ImageView img_two;
    private TextView textview_before_after_two;
    private RelativeLayout relayout_thr;
    private ImageView img_thr;
    private TextView textview_before_after_thr;
    private RelativeLayout relayout_four;
    private ImageView img_four;
    private TextView textview_before_after_four;
    private String shopWxNum;
    private String shopWxImg;
    private List<BeforeAfterPics> allPics = new ArrayList<>();
    private List<RelativeLayout> allLayout = new ArrayList<>();
    private List<ImageView> allImg = new ArrayList<>();
    private String pics[] = null;
    private List<TextView> allText = new ArrayList<>();
    private double shopLat;
    private double shopLng;
    private String shopName;
    private int previous;
    private View view_orderdetail_1;
    private int couponId;
    private TextView tvPetStoreName;
    private TextView tvPetStoreAddr;
    private TextView tvPetStorePhone;
    private LinearLayout llPetStore;
    private LinearLayout llMain;
    private RelativeLayout rlNull;
    private TextView tvMsg1;
    private Button btRefresh;
    private LinearLayout llHidden;
    private LinearLayout llShowAll;
    private TextView tvShowAll;
    private ImageView ivShowAll;
    private TextView textview_order_upgradeitems;
    private TextView tvBaseFee;
    private TextView textview_base_show;
    private RelativeLayout rlAddServiceItems;
    private TextView tvAddItemsFee;
    private RelativeLayout rlUpgradeServiceItems;
    private TextView tvUpgradeItemsFee;
    private RelativeLayout rlCoupon;
    private TextView tvCouponFee;
    private PullToRefreshScrollView prsScrollView;
    private RelativeLayout layout_order_baseprice;
    private double basePrice = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent = null;
            switch (msg.what) {
                case 0:
                    break;
                case 2:
                    try {
                        if (pWinCancle.isShowing()) {
                            pWinCancle.dismiss();
                            pWinCancle = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int codeNum = msg.arg1;
                    break;
                case 3:
                    long lastTimerShowHMS = msg.arg1;
                    String time = Utils.formatTimeFM(lastTimerShowHMS);
                    String minute = time.substring(0, 2);
                    String second = time.substring(3, 5);
                    btPay.setText("等待接单" + minute + ":" + second);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail_from_order_to_confirm);
        orderConfirm = this;
        pDialog = new MProgressDialog(this);
        OrderId = getIntent().getIntExtra("orderid", 0);
        type = getIntent().getIntExtra("type", 0);
        previous = getIntent().getIntExtra("previous", 0);
        configPlatforms();
        findVeiw();
        setView();
        initReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderDetails();
    }

    private void initReceiver() {
        receiver = new MReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
        // 注册广播接收器
        registerReceiver(receiver, filter);
    }

    private void findVeiw() {
        spUtil = SharedPreferenceUtil.getInstance(this);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
        tvServicename = (TextView) findViewById(R.id.tv_orderdetail_servicename);
        tvPrice = (TextView) findViewById(R.id.tv_orderdetail_price);
        tvBeauticianName = (TextView) findViewById(R.id.tv_orderdetail_beauticianname);
        tvBeauticianOrdernum = (TextView) findViewById(R.id.tv_orderdetail_beauticianordernum);
        tvBeauticianLevel = (TextView) findViewById(R.id.tv_orderdetail_beauticianlevel);
        ivBeauticianLevel1 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel1);
        ivBeauticianLevel2 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel2);
        ivBeauticianLevel3 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel3);
        ivBeauticianLevel4 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel4);
        ivBeauticianLevel5 = (ImageView) findViewById(R.id.iv_orderdetail_beauticianlevel5);
        btPay = (Button) findViewById(R.id.bt_orderdetail_pay);
        layout_confirm = (RelativeLayout) findViewById(R.id.layout_confirm);
        order_showbase = (LinearLayout) findViewById(R.id.order_showbase);
        textView_order_num = (TextView) findViewById(R.id.textView_order_num);
        textView_order_time = (TextView) findViewById(R.id.textView_order_time);
        textView_over = (TextView) findViewById(R.id.textView_over);
        textView_over_time = (TextView) findViewById(R.id.textView_over_time);
        textView_confirm_price_show = (TextView) findViewById(R.id.textView_confirm_price_show);
        textview_old_price_andother = (TextView) findViewById(R.id.textview_old_price_andother);
        textview_old_price_bottom = (TextView) findViewById(R.id.textview_old_price_bottom);
        textview_price_back = (TextView) findViewById(R.id.textview_price_back);
        textView_pay_other = (TextView) findViewById(R.id.textView_pay_other);
        textView_yuyue_time = (TextView) findViewById(R.id.textView_yuyue_time);
        textView_people_name = (TextView) findViewById(R.id.textView_people_name);
        textView_people_phone = (TextView) findViewById(R.id.textView_people_phone);
        textView_address_detail = (TextView) findViewById(R.id.textView_address_detail);
        textView_remark_detail = (TextView) findViewById(R.id.textView_remark_detail);
        order_status = (TextView) findViewById(R.id.order_status);
        tvAddrType = (TextView) findViewById(R.id.textView_addrtype);
        tvAddrTypeDes = (TextView) findViewById(R.id.textView_addrtype_des);
        tvPetStoreName = (TextView) findViewById(R.id.tv_petstore_name);
        tvPetStoreAddr = (TextView) findViewById(R.id.tv_petstore_addr);
        tvPetStorePhone = (TextView) findViewById(R.id.tv_petstore_phone);
        llPetStore = (LinearLayout) findViewById(R.id.ll_petstore);
        textview_order_show_base = (TextView) findViewById(R.id.textview_order_show_base);
        textview_order_show_only = (TextView) findViewById(R.id.textview_order_show_only);
        textview_order_upgradeitems = (TextView) findViewById(R.id.textview_order_upgradeitems);
        textView_order_GoShopOrGoHome = (TextView) findViewById(R.id.textView_order_GoShopOrGoHome);
        layout_order_pickup = (RelativeLayout) findViewById(R.id.layout_order_pickup);
        textView_pickup_price = (TextView) findViewById(R.id.textView_pickup_price);
        llMain = (LinearLayout) findViewById(R.id.ll_orderdetail_main);
        rlNull = (RelativeLayout) findViewById(R.id.rl_null);
        tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
        btRefresh = (Button) findViewById(R.id.bt_null_refresh);
        llHidden = (LinearLayout) findViewById(R.id.ll_orderdetailfromorder_hidden);
        llShowAll = (LinearLayout) findViewById(R.id.ll_orderdetail_showall);
        tvShowAll = (TextView) findViewById(R.id.tv_orderdetail_showall);
        ivShowAll = (ImageView) findViewById(R.id.iv_orderdetail_showall);
        tvBaseFee = (TextView) findViewById(R.id.textView_base_price);
        textview_base_show = (TextView) findViewById(R.id.textview_base_show);
        rlAddServiceItems = (RelativeLayout) findViewById(R.id.layout_order_addservice);
        tvAddItemsFee = (TextView) findViewById(R.id.textView_addservice_price);
        rlUpgradeServiceItems = (RelativeLayout) findViewById(R.id.layout_order_upgradeservice);
        tvUpgradeItemsFee = (TextView) findViewById(R.id.textView_upgradeservice_price);
        rlCoupon = (RelativeLayout) findViewById(R.id.rl_orderdetail_coupon);
        tvCouponFee = (TextView) findViewById(R.id.tv_orderdetail_coupon);
        ImageView_red_packets = (ImageView) findViewById(R.id.ImageView_red_packets);
        imageview_status = (ImageView) findViewById(R.id.imageview_status);
        layout_beautiful_work_to = (RelativeLayout) findViewById(R.id.layout_beautiful_work_to);
        imageview_to_service = (ImageView) findViewById(R.id.imageview_to_service);
        layout_addrtype = (LinearLayout) findViewById(R.id.layout_addrtype);
        sriv_orderdetail_beautician_sex = (SelectableRoundedImageView) findViewById(R.id.sriv_orderdetail_beautician_sex);
        order_beau_level = (ImageView) findViewById(R.id.order_beau_level);
        layout_order_show_base_one = (LinearLayout) findViewById(R.id.layout_order_show_base_one);
        layout_order_show_only_one = (LinearLayout) findViewById(R.id.layout_order_show_only_one);
        layout_order_show_upgradeitems_one = (LinearLayout) findViewById(R.id.layout_order_show_upgradeitems_one);
        layout_order_baseprice = (RelativeLayout) findViewById(R.id.layout_order_baseprice);
        textView_address = (TextView) findViewById(R.id.textView_address);
        textview_hurryup = (TextView) findViewById(R.id.textview_hurryup);
        textview_member_show = (TextView) findViewById(R.id.textview_member_show);
        layout_change_order_parent = (RelativeLayout) findViewById(R.id.layout_change_order_parent);
        layout_cancle_order = (LinearLayout) findViewById(R.id.layout_cancle_order);
        layout_change_order = (LinearLayout) findViewById(R.id.layout_change_order);
        layout_people = (LinearLayout) findViewById(R.id.layout_people);
        image_change_custome = (ImageView) findViewById(R.id.image_change_custome);
        layout_confirm_eva_complaints = (RelativeLayout) findViewById(R.id.layout_confirm_eva_complaints);
        layout_copy_ordernum = (RelativeLayout) findViewById(R.id.layout_copy_ordernum);
        bt_only_eva = (Button) findViewById(R.id.bt_only_eva);
        bt_complaints = (Button) findViewById(R.id.bt_complaints);
        layout_compl_parent = (LinearLayout) findViewById(R.id.layout_compl_parent);
        textview_compl_title = (TextView) findViewById(R.id.textview_compl_title);
        textview_show_compl_reason = (TextView) findViewById(R.id.textview_show_compl_reason);
        textview_show_compl_status = (TextView) findViewById(R.id.textview_show_compl_status);
        layout_go_home_service_price = (LinearLayout) findViewById(R.id.layout_go_home_service_price);
        textview_go_home_service_tag = (TextView) findViewById(R.id.textview_go_home_service_tag);
        textview_go_home_service_price = (TextView) findViewById(R.id.textview_go_home_service_price);
        layout_go_home_manjianyouhui = (LinearLayout) findViewById(R.id.layout_go_home_manjianyouhui);
        textview_manjianyouhui_left = (TextView) findViewById(R.id.textview_manjianyouhui_left);
        textview_manjianyouhui_right = (TextView) findViewById(R.id.textview_manjianyouhui_right);
        rl_orderdetail_promoterCode = (RelativeLayout) findViewById(R.id.rl_orderdetail_promoterCode);
        tv_orderdetail_promoterCode = (TextView) findViewById(R.id.tv_orderdetail_promoterCode);
        image_base_show_left = (ImageView) findViewById(R.id.image_base_show_left);
        image_coupon_show_left = (ImageView) findViewById(R.id.image_coupon_show_left);
        image_manjianyouhui_show_left = (ImageView) findViewById(R.id.image_manjianyouhui_show_left);
        image_promoterCode_show_left = (ImageView) findViewById(R.id.image_promoterCode_show_left);
        rl_orderdetail_refund = (RelativeLayout) findViewById(R.id.rl_orderdetail_refund);
        image_refund_show_left = (ImageView) findViewById(R.id.image_refund_show_left);
        tv_orderdetail_refund = (TextView) findViewById(R.id.tv_orderdetail_refund);
        layout_dummy_money_orderdetail = (LinearLayout) findViewById(R.id.layout_dummy_money_orderdetail);
        textview_dummy_detail = (TextView) findViewById(R.id.textview_dummy_detail);
        layout_go_home_service_coupon = (LinearLayout) findViewById(R.id.layout_go_home_service_coupon);
        textview_go_home_service_couponprice = (TextView) findViewById(R.id.textview_go_home_service_couponprice);
        prsScrollView = (PullToRefreshScrollView) findViewById(R.id.prs_orderdetail_main);
        layout_shop = (RelativeLayout) findViewById(R.id.layout_shop);
        img_shop_icon = (ImageView) findViewById(R.id.img_shop_icon);
        textview_shopname = (TextView) findViewById(R.id.textview_shopname);
        textview_address = (TextView) findViewById(R.id.textview_address);
        textview_shopphone = (TextView) findViewById(R.id.textview_shopphone);
        textview_shopweixin = (TextView) findViewById(R.id.textview_shopweixin);
        textview_shopdaohang = (TextView) findViewById(R.id.textview_shopdaohang);
        textview_shopinsidelooklook = (TextView) findViewById(R.id.textview_shopinsidelooklook);
        layout_worker = (LinearLayout) findViewById(R.id.layout_worker);
        img_worker_icon = (ImageView) findViewById(R.id.img_worker_icon);
        textview_worker_name = (TextView) findViewById(R.id.textview_worker_name);
        textview_worker_accept_order_nums = (TextView) findViewById(R.id.textview_worker_accept_order_nums);
        textview_worker_level = (TextView) findViewById(R.id.textview_worker_level);
        textview_worker_eva_content = (TextView) findViewById(R.id.textview_worker_eva_content);
        layout_all_imgs = (LinearLayout) findViewById(R.id.layout_all_imgs);
        relayout_one = (RelativeLayout) findViewById(R.id.relayout_one);
        img_one = (ImageView) findViewById(R.id.img_one);
        textview_before_after_one = (TextView) findViewById(R.id.textview_before_after_one);
        relayout_two = (RelativeLayout) findViewById(R.id.relayout_two);
        img_two = (ImageView) findViewById(R.id.img_two);
        textview_before_after_two = (TextView) findViewById(R.id.textview_before_after_two);
        relayout_thr = (RelativeLayout) findViewById(R.id.relayout_thr);
        img_thr = (ImageView) findViewById(R.id.img_thr);
        textview_before_after_thr = (TextView) findViewById(R.id.textview_before_after_thr);
        relayout_four = (RelativeLayout) findViewById(R.id.relayout_four);
        img_four = (ImageView) findViewById(R.id.img_four);
        textview_before_after_four = (TextView) findViewById(R.id.textview_before_after_four);
        view_orderdetail_1 = (View) findViewById(R.id.view_orderdetail_1);
        allLayout.add(relayout_one);
        allLayout.add(relayout_two);
        allLayout.add(relayout_thr);
        allLayout.add(relayout_four);
        allImg.add(img_one);
        allImg.add(img_two);
        allImg.add(img_thr);
        allImg.add(img_four);
        allText.add(textview_before_after_one);
        allText.add(textview_before_after_two);
        allText.add(textview_before_after_thr);
        allText.add(textview_before_after_four);
    }

    private void setView() {
        tvTitle.setText("订单详情");
        bt_titlebar_other.setText("取消订单");
        tvMsg1.setText("网络异常或获取数据出错");
        btRefresh.setVisibility(View.VISIBLE);
        bt_titlebar_other.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        llPetStore.setVisibility(View.GONE);
        layout_shop.setVisibility(View.GONE);
        ibBack.setOnClickListener(this);
        bt_titlebar_other.setOnClickListener(this);
        btPay.setOnClickListener(this);
        btRefresh.setOnClickListener(this);
        llShowAll.setOnClickListener(this);
        ImageView_red_packets.setOnClickListener(this);
        llPetStore.setOnClickListener(this);
        layout_beautiful_work_to.setOnClickListener(this);
        layout_worker.setOnClickListener(this);
        imageview_to_service.setOnClickListener(this);
        layout_cancle_order.setOnClickListener(this);
        layout_change_order.setOnClickListener(this);
        layout_people.setOnClickListener(this);
        layout_change_order_parent.setOnClickListener(this);
        layout_change_order_parent.setOnClickListener(this);
        bt_only_eva.setOnClickListener(this);
        bt_complaints.setOnClickListener(this);
        layout_copy_ordernum.setOnClickListener(this);
        textview_shopphone.setOnClickListener(this);
        textview_shopweixin.setOnClickListener(this);
        textview_shopdaohang.setOnClickListener(this);
        textview_shopinsidelooklook.setOnClickListener(this);
        img_one.setOnClickListener(this);
        img_two.setOnClickListener(this);
        img_thr.setOnClickListener(this);
        img_four.setOnClickListener(this);
        showMain(true);
        prsScrollView.setMode(Mode.PULL_FROM_START);
        prsScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                Mode mode = refreshView.getCurrentMode();
                if (mode == Mode.PULL_FROM_START) {
                    if (layout_change_order_parent.getVisibility() == View.VISIBLE) {
                        layout_change_order_parent.setVisibility(View.GONE);
                    }
                    getOrderDetails();
                }
            }
        });
        getOrderDetails();
    }

    private void goNext(Class cls) {
        Intent intent = new Intent(mContext, cls);
        mContext.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                //返回
                if (state == 4) {
                    SharedPreferenceUtil.getInstance(orderConfirm).saveInt("state", state);
                } else {
                    SharedPreferenceUtil.getInstance(orderConfirm).saveInt("state", state);
                }
                finishWithAnimation();
                break;
            case R.id.layout_change_order_parent:
                layout_change_order_parent.setVisibility(View.GONE);
                break;
            case R.id.bt_titlebar_other:
                if (state == 2 || state == 22 || state == 21 || state == 23) {//已经付款
                    if (type == 1) {//洗美改单
                        if (layout_change_order_parent.getVisibility() == View.VISIBLE) {
                            layout_change_order_parent.setVisibility(View.GONE);
                        } else {
                            layout_change_order_parent.setVisibility(View.VISIBLE);
                        }
                    } else {
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_CancelOrder);
                        layout_change_order_parent.setVisibility(View.GONE);
                        //取消订单
                        Intent intentCancle = new Intent(orderConfirm, OrderCancleActivity.class);
                        intentCancle.putExtra("state", state);
                        intentCancle.putExtra("type", type);
                        intentCancle.putExtra("orderid", OrderId);
                        intentCancle.putExtra("payWay", payWay);
                        intentCancle.putExtra("couponId", couponId);
                        //需要带上各种订单id type 什么
                        startActivity(intentCancle);
                    }
                } else {
                    layout_change_order_parent.setVisibility(View.GONE);
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_CancelOrder);
                    //取消订单
                    Intent intentCancle = new Intent(orderConfirm, OrderCancleActivity.class);
                    intentCancle.putExtra("state", state);
                    intentCancle.putExtra("type", type);
                    intentCancle.putExtra("payWay", payWay);
                    intentCancle.putExtra("orderid", OrderId);
                    //需要带上各种订单id type 什么
                    startActivity(intentCancle);
                }
                break;
            case R.id.layout_cancle_order:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_PetHome_DogCard);
                //取消订单
                Intent intentCancle = new Intent(orderConfirm, OrderCancleActivity.class);
                intentCancle.putExtra("state", state);
                intentCancle.putExtra("type", type);
                intentCancle.putExtra("payWay", payWay);
                intentCancle.putExtra("orderid", OrderId);
                startActivity(intentCancle);
                break;
            case R.id.layout_change_order://改单
                break;
            case R.id.layout_people:
                goNextForData(ContactActivity.class);
                break;
            case R.id.bt_null_refresh:
                //刷新
                getOrderDetails();
                break;
            case R.id.ll_orderdetail_showall:
                //显示全部
                if (!isShowAll) {
                    //显示全部
                    isShowAll = true;
                    tvShowAll.setText("点击收起");
                    ivShowAll.setBackgroundResource(R.drawable.icon_arrow_up);
                    llHidden.setVisibility(View.VISIBLE);
                } else {
                    isShowAll = false;
                    llHidden.setVisibility(View.GONE);
                    tvShowAll.setText("查看全部");
                    ivShowAll.setBackgroundResource(R.drawable.icon_arrow_down);
                }
                break;
            case R.id.bt_orderdetail_pay:
                //服务按钮
                if (hasupgrageorder) {
                    goUpgradeService(OrderId, UpdateOrderConfirmNewActivity.class);
                } else {
                    if (state == 3) {//待确认
                    } else if (state == 4) {//待评价
                        Intent intent = new Intent(OrderDetailFromOrderToConfirmActivity.this, EvaluateNewActivity.class);
                        intent.putExtra("orderid", OrderId);
                        intent.putExtra("type", type);
                        startActivityForResult(intent, Global.ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW);
                    } else if (state == 21) {
                        MDialog mDialog = new MDialog.Builder(orderConfirm)
                                .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                                .setMessage("是否拨打电话?").setCancelStr("否")
                                .setOKStr("是")
                                .positiveListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // 确认拨打电话
                                        Utils.telePhoneBroadcast(orderConfirm,
                                                beauPhone);
                                    }
                                }).build();
                        mDialog.show();
                    }
                }
                break;
            case R.id.ImageView_red_packets:
                if (pWinPacket != null) {
                    if (!pWinPacket.isShowing()) {
                    } else {
                        pWinPacket.dismiss();
                        pWinPacket = null;
                        Utils.onDismiss(orderConfirm);
                    }
                }
                break;
            case R.id.layout_shop:
            case R.id.ll_petstore:
                toNext(ShopDetailActivity.class);
                break;
            case R.id.layout_worker:
            case R.id.layout_beautiful_work_to:
                toNext(BeauticianDetailActivity.class);
                break;
            case R.id.imageview_to_service:
                // 电话
                if (!phoneNum.equals("")) {
                    if (serviceloc == 1) {// 门店服务
                        MDialog mDialog = new MDialog.Builder(orderConfirm)
                                .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                                .setMessage("是否拨打电话?").setCancelStr("否")
                                .setOKStr("是")
                                .positiveListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // 确认拨打电话
                                        Utils.telePhoneBroadcast(orderConfirm,
                                                phoneNum);
                                    }
                                }).build();
                        mDialog.show();
                    }
                } else {
                    MDialog mDialog = new MDialog.Builder(orderConfirm)
                            .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                            .setMessage("是否拨打电话?").setCancelStr("否")
                            .setOKStr("是")
                            .positiveListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // 确认拨打电话
                                    Utils.telePhoneBroadcast(orderConfirm, servicePhone);
                                }
                            }).build();
                    mDialog.show();
                }
                break;
            case R.id.bt_only_eva://订单已完成 单独显示评价
                Intent intent = new Intent(OrderDetailFromOrderToConfirmActivity.this, EvaluateNewActivity.class);
                intent.putExtra("orderid", OrderId);
                intent.putExtra("type", type);
                startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
                break;
            case R.id.bt_complaints://订单已完成 投诉
                Intent intentCompl = new Intent(orderConfirm, ComplaintActivity.class);
                intentCompl.putExtra("ordersBean", ordersBean);
                startActivityForResult(intentCompl, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
                break;
            case R.id.layout_copy_ordernum://复制订单号
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(textView_order_num.getText());
                ToastUtil.showToastShortCenter(mContext, "复制成功");
                break;
            case R.id.textview_shopphone:
                MDialog mDialog = new MDialog.Builder(orderConfirm)
                        .setTitle("提示").setType(MDialog.DIALOGTYPE_CONFIRM)
                        .setMessage(phoneNum)
                        .setMsgTextColor(R.color.a333333)
                        .setCancelStr("取消")
                        .setCancelTextColor(R.color.a999999)
                        .setOKStr("呼叫")
                        .setOKTextColor(R.color.orange)
                        .positiveListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 确认拨打电话
                                Utils.telePhoneBroadcast(orderConfirm, phoneNum);
                            }
                        }).build();
                mDialog.show();
                break;
            case R.id.textview_shopweixin:
                Utils.setWeChatQrCodeDialog(mContext, shopWxNum, shopWxImg);
                break;
            case R.id.textview_shopdaohang:
                Intent intentShopLoc = new Intent(mContext, ShopLocationActivity.class);
                intentShopLoc.putExtra("shoplat", shopLat);
                intentShopLoc.putExtra("shoplng", shopLng);
                intentShopLoc.putExtra("shopaddr", shopAddress);
                intentShopLoc.putExtra("shopname", shopName);
                startActivity(intentShopLoc);
                break;
            case R.id.textview_shopinsidelooklook:
                toNext(ShopDetailActivity.class);
                break;
            case R.id.img_one:
                goNext(0, pics);
                break;
            case R.id.img_two:
                goNext(1, pics);
                break;
            case R.id.img_thr:
                goNext(2, pics);
                break;
            case R.id.img_four:
                goNext(3, pics);
                break;
        }
    }

    private void showMain(boolean flag) {
        if (flag) {
            llMain.setVisibility(View.VISIBLE);
            rlNull.setVisibility(View.GONE);
        } else {
            llMain.setVisibility(View.GONE);
            rlNull.setVisibility(View.VISIBLE);
        }
    }

    private void goUpgradeService(int orderid, Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("orderid", orderid);
        startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE);
    }

    private void setBottomButtonStatus(int status) {
        if (state == 2 || state == 23) {//已经付款  21 已出发 22 开始服务
            order_status.setText(statusDescription);
            layout_confirm.setVisibility(View.VISIBLE);
            if (isVie) {
                if (beautician_id > 0) {
                    if (TextUtils.isEmpty(btnTxt)) {
                        btPay.setText("美容师已接单,坐等服务");
                    } else {
                        btPay.setText(btnTxt);
                    }
                } else {
                    if (TextUtils.isEmpty(btnTxt)) {
                        btPay.setText("等待美容师接单");
                    } else {
                        btPay.setText(btnTxt);
                    }
                }
            } else {
                if (TextUtils.isEmpty(btnTxt)) {
                    btPay.setText("坐等服务");
                } else {
                    btPay.setText(btnTxt);
                }
            }
            btPay.setBackgroundColor(Color.parseColor("#bdbdbd"));
            textView_over.setVisibility(View.GONE);
            textView_over_time.setVisibility(View.GONE);
            ImageView_red_packets.setVisibility(View.GONE);
            imageview_status.setBackgroundResource(R.drawable.already_pay);
            bt_titlebar_other.setVisibility(View.VISIBLE);
        } else if (state == 21) {
            order_status.setText(statusDescription);
            layout_confirm.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(btnTxt)) {
                btPay.setText("催单");
            } else {
                btPay.setText(btnTxt);
            }
            textView_over.setVisibility(View.GONE);
            textView_over_time.setVisibility(View.GONE);
            btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
            ImageView_red_packets.setVisibility(View.GONE);
            imageview_status.setBackgroundResource(R.drawable.already_pay);
            bt_titlebar_other.setVisibility(View.VISIBLE);
        } else if (state == 22 || state == 10) {//开始服务
            order_status.setText(statusDescription);
            layout_confirm.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(btnTxt)) {
                btPay.setText("正在服务");
            } else {
                btPay.setText(btnTxt);
            }
            textView_over.setVisibility(View.GONE);
            textView_over_time.setVisibility(View.GONE);
            btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
            ImageView_red_packets.setVisibility(View.GONE);
            if (state == 10) {
                imageview_status.setBackgroundResource(R.drawable.order_update);
            } else {
                imageview_status.setBackgroundResource(R.drawable.already_pay);
            }
            bt_titlebar_other.setVisibility(View.GONE);//开始服务不能取消订单
        } else if (state == 6 || state == 7) {//已取消
            order_status.setText(statusDescription);
            layout_confirm.setVisibility(View.GONE);
            textView_over.setVisibility(View.GONE);
            textView_over_time.setVisibility(View.GONE);
            bt_titlebar_other.setVisibility(View.GONE);
            ImageView_red_packets.setVisibility(View.GONE);
            imageview_status.setBackgroundResource(R.drawable.order_already_cancle);
            image_change_custome.setVisibility(View.GONE);
            layout_people.setOnClickListener(null);
        } else if (state == 5) {//已完成  。。 等于  已关闭
            order_status.setText(statusDescription);
            layout_confirm.setVisibility(View.GONE);
            bt_titlebar_other.setVisibility(View.GONE);
            ImageView_red_packets.setVisibility(View.GONE);
            imageview_status.setBackgroundResource(R.drawable.order_already_over);
            image_change_custome.setVisibility(View.GONE);
            layout_people.setOnClickListener(null);
            layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
            bt_only_eva.setVisibility(View.GONE);
        } else if (state == 4) {//待评价
            layout_confirm_eva_complaints.setVisibility(View.VISIBLE);
            layout_confirm.setVisibility(View.GONE);
            order_status.setText(statusDescription);
            bt_titlebar_other.setVisibility(View.GONE);
            textView_over.setVisibility(View.GONE);
            textView_over_time.setVisibility(View.GONE);
            btPay.setText("去评价");
            btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
            ImageView_red_packets.setVisibility(View.GONE);
            imageview_status.setBackgroundResource(R.drawable.to_wai_eva);
            image_change_custome.setVisibility(View.GONE);
            layout_people.setOnClickListener(null);
        } else if (state == 3) {//待确认
            layout_confirm.setVisibility(View.VISIBLE);
            order_status.setText(statusDescription);
            bt_titlebar_other.setVisibility(View.VISIBLE);
            textView_over.setVisibility(View.GONE);
            textView_over_time.setVisibility(View.GONE);
            btPay.setText("确认完成");
            btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
            ImageView_red_packets.setVisibility(View.GONE);
            imageview_status.setBackgroundResource(R.drawable.about_other);
            image_change_custome.setVisibility(View.GONE);
            layout_people.setOnClickListener(null);
        }
    }

    private void showStars(int levels, int stars) {
        ivBeauticianLevel1.setBackgroundResource(R.drawable.bk_empty);
        ivBeauticianLevel2.setBackgroundResource(R.drawable.bk_empty);
        ivBeauticianLevel3.setBackgroundResource(R.drawable.bk_empty);
        ivBeauticianLevel4.setBackgroundResource(R.drawable.bk_empty);
        ivBeauticianLevel5.setBackgroundResource(R.drawable.bk_empty);
        if (levels == 1) {
            switch (stars) {
                case 1:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
                    break;
                case 2:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
                    break;
                case 3:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
                    break;
                case 4:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_1);
                    break;
                case 5:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_1);
                    ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_1);
                    break;
            }
        } else if (levels == 2) {
            switch (stars) {
                case 1:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
                    break;
                case 2:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
                    break;
                case 3:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
                    break;
                case 4:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_2);
                    break;
                case 5:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_2);
                    ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_2);
                    break;
            }
        } else if (levels == 3) {
            switch (stars) {
                case 1:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
                    break;
                case 2:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
                    break;
                case 3:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
                    break;
                case 4:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_3);
                    break;
                case 5:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_3);
                    ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_3);
                    break;
            }
        } else if (levels == 4) {
            switch (stars) {
                case 1:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
                    break;
                case 2:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
                    break;
                case 3:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
                    break;
                case 4:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_4);
                    break;
                case 5:
                    ivBeauticianLevel1.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel2.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel3.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel4.setBackgroundResource(R.drawable.icon_level_4);
                    ivBeauticianLevel5.setBackgroundResource(R.drawable.icon_level_4);
                    break;
            }
        }
    }

    //查询订单明细
    private void getOrderDetails() {
        pDialog.showDialog();
        CommUtil.querySwimOrderDetails(this, OrderId, getOrderDetails);
    }

    //订单明细
    private AsyncHttpResponseHandler getOrderDetails = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                prsScrollView.onRefreshComplete();
                pDialog.closeDialog();
                layout_change_order_parent.setVisibility(View.GONE);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            showMain(true);
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                hasupgrageorder = false;
                if (code == 0 && jsonObject.has("data") && !jsonObject.isNull("data")) {
                    JSONObject object = jsonObject.getJSONObject("data");
                    if (object.has("type") && !object.isNull("type")) {
                        type = object.getInt("type");
                        ordersBean.setType(type);//投诉订单对象
                    }
                    setTypeName();//投诉用
                    if (object.has("servicePhone") && !object.isNull("servicePhone")) {
                        servicePhone = object.getString("servicePhone");
                    }
                    if (object.has("orderTitle") && !object.isNull("orderTitle")) {
                        orderTitle = object.getString("orderTitle");
                        tvTitle.setText(orderTitle);
                    }
                    if (object.has("appointment") && !object.isNull("appointment")) {
                        appointment = object.getString("appointment");
                        ordersBean.setAppointment(appointment);
                        if (type == 3) {
                            view_orderdetail_1.setVisibility(View.GONE);
                            String time[] = appointment.split(" ");
                            String[] timeAOrP = time[1].split(":");
                            String AmOrPm = "";
                            if (Integer.parseInt(timeAOrP[0]) <= 12) {
                                AmOrPm = "上午";
                            } else if (Integer.parseInt(timeAOrP[0]) > 12) {
                                AmOrPm = "下午";
                            }
                            textView_yuyue_time.setText(time[0] + " " + AmOrPm);
                            image_change_custome.setVisibility(View.GONE);
                            layout_people.setOnClickListener(null);
                        } else {
                            textView_yuyue_time.setText(appointment);
                        }
                    }
                    if (object.has("areaId") && !object.isNull("areaId")) {
                        areaId = object.getInt("areaId");
                    }
                    if (object.has("payWay") && !object.isNull("payWay")) {
                        payWay = object.getInt("payWay");
                    }
                    if (object.has("pet") && !object.isNull("pet")) {
                        JSONObject object2 = object.getJSONObject("pet");
                        if (object2.has("avatarPath") && !object2.isNull("avatarPath")) {
                            String avatarPath = object2.getString("avatarPath");
                            ordersBean.setAvatar(avatarPath);
                        }
                        if (object2.has("petName") && !object2.isNull("petName")) {
                            petName = object2.getString("petName");
                        }
                    }
                    if (object.has("customerName") && !object.isNull("customerName")) {
                        textView_people_name.setText(object.getString("customerName").trim());
                        textView_people_name.setVisibility(View.VISIBLE);
                        cname = object.getString("customerName").trim();
                    } else {
                        textView_people_name.setVisibility(View.GONE);
                    }
                    if (object.has("customerMobile") && !object.isNull("customerMobile")) {
                        textView_people_phone.setText(object.getString("customerMobile").trim());
                        cphone = object.getString("customerMobile").trim();
                    }
                    if (object.has("created") && !object.isNull("created")) {
                        String created = object.getString("created");
                        textView_order_time.setText(created);
                    }
                    if (object.has("OrderId") && !object.isNull("OrderId")) {
                        int oId = object.getInt("OrderId");
                        ordersBean.setId(oId);
                    }
                    if (object.has("statusDescription") && !object.isNull("statusDescription")) {
                        statusDescription = object.getString("statusDescription");
                    }
                    if (object.has("orderSource") && !object.isNull("orderSource")) {
                        String orderSource = object.getString("orderSource");
                        if (orderSource.equals("vie")) {
                            textview_hurryup.setVisibility(View.VISIBLE);
                            isVie = true;
                            textview_hurryup.setText("即时预约");
                            textview_hurryup.setBackgroundDrawable(Utils.getDW("FE4D3D"));
                        }
                    } else {
                        isVie = false;
                        textview_hurryup.setVisibility(View.GONE);
                    }
                    getWorker(object);
                    if (object.has("btnTxt") && !object.isNull("btnTxt")) {
                        btnTxt = object.getString("btnTxt");
                    }
                    if (object.has("status") && !object.isNull("status")) {
                        state = object.getInt("status");
                        setBottomButtonStatus(state);
                    }
                    if (state == 2 || state == 22 || state == 21 || state == 23) {//已经付款
                        if (type == 1) {
                            bt_titlebar_other.setText("修改订单");
                        }
                    } else {
                        bt_titlebar_other.setText("取消订单");
                    }
                    if (object.has("orderNum") && !object.isNull("orderNum")) {
                        String orderNum = object.getString("orderNum");
                        textView_order_num.setText(orderNum);
                        ordersBean.setOrderNum(orderNum);
                    }
                    if (object.has("payDesc") && !object.isNull("payDesc")) {
                        String payDesc = object.getString("payDesc");
                        textView_pay_other.setText(payDesc);
                    }
                    if (object.has("basicPrice") && !object.isNull("basicPrice")) {
                        if (object.has("timeCardTag") && !object.isNull("timeCardTag")) {
                            JSONObject objectTimeCardTag = object.getJSONObject("timeCardTag");
                            if (objectTimeCardTag.has("title") && !objectTimeCardTag.isNull("title")) {
                                textview_base_show.setText(objectTimeCardTag.getString("title"));
                            }
                            if (objectTimeCardTag.has("tip") && !objectTimeCardTag.isNull("tip")) {
                                tvBaseFee.setText(objectTimeCardTag.getString("tip").trim());
                            }
                            image_base_show_left.setVisibility(View.VISIBLE);
                        } else {
                            Utils.setDoubleStr(tvBaseFee, object.getDouble("basicPrice"), "¥ ", "");
                            image_base_show_left.setVisibility(View.GONE);
                        }
                        basePrice = object.getDouble("basicPrice");
                    }
                    if (object.has("listAddress") && !object.isNull("listAddress")) {
                        try {
                            JSONArray array = object.getJSONArray("listAddress");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = array.getJSONObject(0);
                                String address = jsonObject2.getString("address");
                                textView_address_detail.setText(address);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (object.has("remark") && !object.isNull("remark")) {
                        String remark = object.getString("remark");
                        if ("".equals(remark.trim())) {
                            textView_remark_detail.setText("无");
                        } else {
                            textView_remark_detail.setText(remark);
                        }
                    } else {
                        textView_remark_detail.setText("无");
                    }
                    if (object.has("coupon") && !object.isNull("coupon")) {
                        JSONObject couponObject = object.getJSONObject("coupon");
                        String nameCoupon = "";
                        rlCoupon.setVisibility(View.VISIBLE);
                        image_coupon_show_left.setVisibility(View.VISIBLE);
                        if (couponObject.has("amount") && !couponObject.isNull("amount")) {
                            amount = couponObject.getString("amount");
                            couponAmount = Double.parseDouble(amount);
                            tvCouponFee.setText("-¥" + Math.abs(Double.parseDouble(amount)));
                        }
                        if (couponObject.has("name") && !couponObject.isNull("name")) {
                            nameCoupon = couponObject.getString("name");
                            if (!TextUtils.isEmpty(nameCoupon)) {
                                tvCouponFee.setText("-¥" + Math.abs(Double.parseDouble(amount)) + "(" + nameCoupon + ")");
                            }
                        }
                        if (couponObject.has("id") && !couponObject.isNull("id")) {
                            couponId = couponObject.getInt("id");
                        }
                        if (couponObject.has("CouponId") && !couponObject.isNull("CouponId")) {
                            couponId = couponObject.getInt("CouponId");
                        }
                    } else {
                        rlCoupon.setVisibility(View.GONE);
                        image_coupon_show_left.setVisibility(View.GONE);
                    }
                    homeCouponAmount = 0;
                    if (object.has("homeCoupon") && !object.isNull("homeCoupon")) {
                        layout_go_home_service_coupon.setVisibility(View.VISIBLE);
                        JSONObject homeCouponObject = object.getJSONObject("homeCoupon");
                        if (homeCouponObject.has("amount") && !homeCouponObject.isNull("amount")) {
                            homeCouponAmount = homeCouponObject.getDouble("amount");
                            textview_go_home_service_couponprice.setText("-¥" + Math.abs(homeCouponAmount));
                        }
                        if (homeCouponObject.has("name") && !homeCouponObject.isNull("name")) {
                            String nameHomeCoupon = homeCouponObject.getString("name");
                            textview_go_home_service_couponprice.setText("-¥" + Math.abs(homeCouponAmount) + "(" + nameHomeCoupon + ")");
                        }
                    } else {
                        layout_go_home_service_coupon.setVisibility(View.GONE);
                    }
                    if (object.has("extraServiceItems") && !object.isNull("extraServiceItems")) {
                        JSONArray extraArray = object.getJSONArray("extraServiceItems");
                        if (extraArray.length() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < extraArray.length(); i++) {
                                JSONObject extraObject = extraArray.getJSONObject(i);
                                if (extraObject.has("name") && !extraObject.isNull("name")) {
                                    if (i == extraArray.length() - 1) {
                                        sb.append(extraObject.getString("name"));
                                    } else {
                                        sb.append(extraObject.getString("name") + "+");
                                    }
                                }
                                serviceIds = new StringBuilder();
                                if (extraObject.has("ExtraServiceItemId") && !extraObject.isNull("ExtraServiceItemId")) {
                                    if (i == extraArray.length() - 1) {
                                        serviceIds.append(extraObject.getString("ExtraServiceItemId"));
                                    } else {
                                        serviceIds.append(extraObject.getString("ExtraServiceItemId") + "_");
                                    }
                                }
                            }
                            layout_order_show_only_one.setVisibility(View.VISIBLE);
                            textview_order_show_only.setVisibility(View.VISIBLE);
                            textview_order_show_only.setText(sb.substring(0, sb.length()));
                            if (object.has("extraItemPrice") && !object.isNull("extraItemPrice")) {
                                extraItemPrice = object.getDouble("extraItemPrice");
                                rlAddServiceItems.setVisibility(View.VISIBLE);
                                Utils.setDoubleStr(tvAddItemsFee, object.getDouble("extraItemPrice"), "¥", "");
                            }
                        }
                    } else {
                        layout_order_show_only_one.setVisibility(View.GONE);
                        rlAddServiceItems.setVisibility(View.GONE);
                    }
                    if (object.has("petServicePojo") && !object.isNull("petServicePojo")) {
                        JSONObject petServicePojo = object.getJSONObject("petServicePojo");
                        if (petServicePojo.has("name") && !petServicePojo.isNull("name")) {
                            name = petServicePojo.getString("name");
                        }
                        if (petServicePojo.has("serviceItem") && !petServicePojo.isNull("serviceItem")) {
                            JSONArray jsonArray = petServicePojo.getJSONArray("serviceItem");
                            Map<String, Object> map = new HashMap<String, Object>();
                            StringBuilder sb = new StringBuilder();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject serviceItem = jsonArray.getJSONObject(j);
                                if (serviceItem.has("itemName") && !serviceItem.isNull("itemName")) {
                                    map.put("itemName", serviceItem.getString("itemName"));
                                    if (j == jsonArray.length() - 1) {
                                        sb.append(serviceItem.getString("itemName"));
                                    } else {
                                        sb.append(serviceItem.getString("itemName") + "+");
                                    }
                                }
                            }
                            textview_order_show_base.setText(sb.substring(0, sb.length()));
                        }
                    }
                    if (object.has("totalPrice") && !object.isNull("totalPrice")) {
                        totalPrice = object.getDouble("totalPrice");
                        String text = "";
                        if (Utils.isDoubleEndWithZero(totalPrice)) {
                            text = "¥ " + Utils.formatDouble(totalPrice);
                        } else {
                            text = "¥ " + totalPrice;
                        }
                        tvPrice.setText(text);
                        ordersBean.setPay_price((int) totalPrice);
                    }
                    if (object.has("serviceLoc") && !object.isNull("serviceLoc")) {
                        serviceloc = object.getInt("serviceLoc");
                        ordersBean.setServiceLoc(serviceloc);
                        if (serviceloc == 1) {
                            tvAddrType.setText("门店服务");
                            textView_order_GoShopOrGoHome.setText("到店");
                            textView_order_GoShopOrGoHome.setBackgroundDrawable(Utils.getDW("FAA04A"));
                            tvAddrTypeDes.setVisibility(View.VISIBLE);
                            double pickUpPrice = 0;
                            if (object.has("pickupPrice") && !object.isNull("pickupPrice")) {
                                pickUpPrice = object.getDouble("pickupPrice");
                                Utils.mLogError("==-->pickupPrice := " + pickUpPrice + " " + object.getDouble("pickupPrice"));
                            }
                            layout_order_pickup.setVisibility(View.VISIBLE);
                            if (object.has("pickUp") && !object.isNull("pickUp")) {
                                int pickup = object.getInt("pickUp");
                                if (pickup == 1) {
                                    tvAddrTypeDes.setText("(需要接送)");
                                    if (pickUpPrice == 0) {
                                        textView_pickup_price.setText("免费");
                                    } else {
                                        Utils.setDoubleStr(textView_pickup_price, pickUpPrice, "¥ ", "");
                                    }
                                } else if (pickup == 2) {//不再接送范围
                                    tvAddrTypeDes.setText("(不接送)");
                                    layout_order_pickup.setVisibility(View.GONE);
                                } else if (pickup == 0) {//不需要接送
                                    layout_order_pickup.setVisibility(View.GONE);
                                    tvAddrTypeDes.setText("(不接送)");
                                }
                            } else {
                                layout_addrtype.setVisibility(View.GONE);
                                layout_order_pickup.setVisibility(View.GONE);
                                tvAddrTypeDes.setText("(不接送)");
                            }
                            llPetStore.setVisibility(View.GONE);
                            layout_shop.setVisibility(View.VISIBLE);
                        } else {
                            layout_order_pickup.setVisibility(View.GONE);
                            llPetStore.setVisibility(View.GONE);
                            layout_shop.setVisibility(View.GONE);
                            tvAddrTypeDes.setVisibility(View.GONE);
                            tvAddrType.setText("上门服务");
                            textView_order_GoShopOrGoHome.setText("上门");
                            textView_order_GoShopOrGoHome.setBackgroundDrawable(Utils.getDW("2FC0F0"));
                        }
                    }
                    if (object.has("orderSource") && !object.isNull("orderSource")) {
                        String orderSource = object.getString("orderSource");
                        if (serviceloc == 1) {//到店
                            if (orderSource.equals("vie")) {
                                llPetStore.setVisibility(View.GONE);
                                layout_shop.setVisibility(View.VISIBLE);
                                layout_beautiful_work_to.setVisibility(View.GONE);//beau
                                layout_worker.setVisibility(View.GONE);//beau
                            }
                        } else if (serviceloc == 2) {//上门
                            if (orderSource.equals("vie")) {
                                llPetStore.setVisibility(View.GONE);
                                layout_shop.setVisibility(View.GONE);
                                layout_beautiful_work_to.setVisibility(View.GONE);//beau
                                layout_worker.setVisibility(View.GONE);//beau
                            }
                        }
                        if (orderSource.equals("vie")) {
                            if (serviceloc == 1) {//到店
                                layout_beautiful_work_to.setVisibility(View.GONE);//beau
                                layout_worker.setVisibility(View.GONE);//beau
                                llPetStore.setVisibility(View.GONE);
                                layout_shop.setVisibility(View.VISIBLE);
                            } else {
                                layout_beautiful_work_to.setVisibility(View.GONE);//beau
                                layout_worker.setVisibility(View.GONE);//beau
                                llPetStore.setVisibility(View.GONE);
                                layout_shop.setVisibility(View.GONE);
                            }
                        }
                    }
                    if (object.has("shop") && !object.isNull("shop")) {
                        JSONObject jshop = object.getJSONObject("shop");
                        if (jshop.has("lat") && !jshop.isNull("lat")) {
                            shopLat = jshop.getDouble("lat");
                        }
                        if (jshop.has("lng") && !jshop.isNull("lng")) {
                            shopLng = jshop.getDouble("lng");
                        }
                        if (jshop.has("shopWxNum") && !jshop.isNull("shopWxNum")) {
                            shopWxNum = jshop.getString("shopWxNum");
                        }
                        if (jshop.has("shopWxImg") && !jshop.isNull("shopWxImg")) {
                            shopWxImg = jshop.getString("shopWxImg");
                        }
                        if (jshop.has("shopName") && !jshop.isNull("shopName")) {
                            shopName = jshop.getString("shopName");
                            tvPetStoreName.setText(jshop.getString("shopName"));
                            textview_shopname.setText(jshop.getString("shopName"));
                        }
                        if (jshop.has("address") && !jshop.isNull("address")) {
                            shopAddress = jshop.getString("address");
                            tvPetStoreAddr.setText(jshop.getString("address"));
                            textview_address.setText(jshop.getString("address"));
                        }
                        if (jshop.has("phone") && !jshop.isNull("phone")) {
                            phoneNum = jshop.getString("phone");
                            tvPetStorePhone.setText(jshop.getString("phone"));
                        }
                        if (jshop.has("ShopId") && !jshop.isNull("ShopId")) {
                            shopId = jshop.getInt("ShopId");
                        }
                        if (jshop.has("img") && !jshop.isNull("img")) {
                            GlideUtil.loadRoundImg(mContext, jshop.getString("img"), img_shop_icon, 0, 5);
                        }
                    }
                    if (object.has("petService") && !object.isNull("petService")) {
                        int petServiceNum = object.getInt("petService");
                        if (petServiceNum == 300 || type == 3) {
                            textView_address.setText("门店地址：");
                            textView_address_detail.setText(shopAddress);
                            layout_beautiful_work_to.setVisibility(View.GONE);
                            layout_worker.setVisibility(View.GONE);
                            layout_order_baseprice.setVisibility(View.GONE);
                            if (amount.equals("")) {
                                order_showbase.setBackgroundColor(getResources().getColor(R.color.white));
                            } else {
                                order_showbase.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            if (state == 2 || state == 22 || state == 21 || state == 23) {
                                layout_confirm.setVisibility(View.GONE);
                            }
                        }
                    }
                    String nickName = "";
                    if (object.has("myPet") && !object.isNull("myPet")) {
                        JSONObject objectMyPet = object.getJSONObject("myPet");
                        if (objectMyPet.has("nickName") && !objectMyPet.isNull("nickName")) {
                            nickName = objectMyPet.getString("nickName");
                        }
                    }
                    if (nickName.equals("")) {
                        tvServicename.setText(petName.trim() + name.trim());
                    } else {
                        tvServicename.setText(nickName.trim() + name.trim());
                    }
                    if (type == 3) {
                        if (nickName.equals("")) {
                            tvServicename.setText(petName.trim() + "游泳");
                        } else {
                            tvServicename.setText(nickName.trim() + "游泳");
                        }
                        layout_order_show_base_one.setVisibility(View.GONE);
                        if (object.has("memberSwimNotice") && !object.isNull("memberSwimNotice")) {
                            textview_member_show.setVisibility(View.VISIBLE);
                            textview_member_show.setText(object.getString("memberSwimNotice"));
                        } else {
                            textview_member_show.setVisibility(View.GONE);
                        }
                    }
                    ordersBean.setService(tvServicename.getText().toString());
                    if (object.has("task") && !object.isNull("task")) {
                        JSONObject objectTask = object.getJSONObject("task");
                        //完成时间
                        if (objectTask.has("actualEndTime") && !objectTask.isNull("actualEndTime")) {
                            textView_over_time.setText(objectTask.getString("actualEndTime"));
                        }
                        if (objectTask.has("serviceId") && !objectTask.isNull("serviceId")) {
                            serviceId = objectTask.getInt("serviceId");
                        }
                        if (objectTask.has("shopId") && !objectTask.isNull("shopId")) {
                            shopId = objectTask.getInt("shopId");
                        }
                    }
                    if (type == 3) {
                        if (object.has("actualEndTime") && !object.isNull("actualEndTime")) {
                            textView_over_time.setText(object.getString("actualEndTime").trim());
                        }
                    }
                    double extraServicePrice = 0;
                    double reductionPrice = 0;
                    if (object.has("extraServiceFeeTag") && !object.isNull("extraServiceFeeTag")) {
                        String extraServiceFeeTag = object.getString("extraServiceFeeTag");
                        textview_go_home_service_tag.setText(extraServiceFeeTag);
                    }
                    if (object.has("extraServicePrice") && !object.isNull("extraServicePrice")) {
                        extraServicePrice = object.getDouble("extraServicePrice");
                        Utils.mLogError("==-->extraServicePrice 0000  " + extraServicePrice);
                        if (extraServicePrice > 0) {
                            layout_go_home_service_price.setVisibility(View.VISIBLE);
                            Utils.setDoubleStr(textview_go_home_service_price, extraServicePrice, "¥", "");
                        } else {
                            layout_go_home_service_price.setVisibility(View.GONE);
                        }
                    } else {
                        layout_go_home_service_price.setVisibility(View.GONE);
                    }
                    if (extraServicePrice <= 0) {
                        extraServicePrice = 0;
                        layout_go_home_service_price.setVisibility(View.GONE);
                    }
                    if (object.has("extraReductionFeeTag") && !object.isNull("extraReductionFeeTag")) {
                        String reductionPriceTag = object.getString("extraReductionFeeTag");
                        textview_manjianyouhui_left.setText(reductionPriceTag);
                    }
                    if (object.has("reductionPrice") && !object.isNull("reductionPrice")) {
                        layout_go_home_manjianyouhui.setVisibility(View.VISIBLE);
                        image_manjianyouhui_show_left.setVisibility(View.VISIBLE);
                        reductionPrice = object.getDouble("reductionPrice");
                        Utils.setDoubleStr(textview_manjianyouhui_right, reductionPrice, "-¥", "");
                    } else {
                        layout_go_home_manjianyouhui.setVisibility(View.GONE);
                        image_manjianyouhui_show_left.setVisibility(View.GONE);
                    }
                    if (reductionPrice <= 0) {
                        reductionPrice = 0;
                        layout_go_home_manjianyouhui.setVisibility(View.GONE);
                        image_manjianyouhui_show_left.setVisibility(View.GONE);
                    }
                    if (object.has("reductionTag") && !object.isNull("reductionTag")) {
                        JSONObject objectReductionTag = object.getJSONObject("reductionTag");
                        if (objectReductionTag.has("reductionTag") && !objectReductionTag.isNull("reductionTag")) {
                            couponCutReductionTag = objectReductionTag.getString("reductionTag");
                        }
                        if (objectReductionTag.has("cardTag") && !objectReductionTag.isNull("cardTag")) {
                            CardTag = objectReductionTag.getString("cardTag");
                        }
                    }
                    if (object.has("refund") && !object.isNull("refund")) {
                        refund = object.getDouble("refund");
                        if (refund > 0) {
                            rl_orderdetail_refund.setVisibility(View.VISIBLE);
                            image_refund_show_left.setVisibility(View.VISIBLE);
                            Utils.setDoubleStr(tv_orderdetail_refund, refund, "¥", "");
                        } else {
                            rl_orderdetail_refund.setVisibility(View.GONE);
                            image_refund_show_left.setVisibility(View.GONE);
                        }
                    } else {
                        rl_orderdetail_refund.setVisibility(View.GONE);
                    }
                    cutDownMoney = setCutDowmMoney(object, extraServicePrice, reductionPrice);
                    //这里不考虑升级订单
                    NoUpdateOrder(object, extraServicePrice, cutDownMoney);
                    if (object.has("updateOrder") && !object.isNull("updateOrder")) {
                        JSONObject jupdateorder = object.getJSONObject("updateOrder");
                        double UpTotalPrice = 0;
                        if (jupdateorder.has("totalPrice") && !jupdateorder.isNull("totalPrice")) {
                            UpTotalPrice = jupdateorder.getDouble("totalPrice");
                        }
                        if (object.has("payPrice") && !object.isNull("payPrice")) {
                            payfee = object.getDouble("payPrice");
                            String text = "";
                            if (Utils.isDoubleEndWithZero(payfee)) {
                                text = "合计 ¥ " + Utils.formatDouble(payfee);
                            } else {
                                text = "合计 ¥ " + payfee;
                            }
                            textView_confirm_price_show.setText(text);
                        }
                        int updatestatus = 0;
                        if (jupdateorder.has("status") && !jupdateorder.isNull("status")) {
                            updatestatus = jupdateorder.getInt("status");
                            textview_order_upgradeitems.setVisibility(View.GONE);
                            layout_order_show_upgradeitems_one.setVisibility(View.GONE);
                            rlUpgradeServiceItems.setVisibility(View.GONE);
                            if (updatestatus == 1) {
                                hasupgrageorder = true;
                                btPay.setText("订单升级-去付款");
                                btPay.setBackgroundResource(R.drawable.bg_button_orange_noround);
                            } else if (updatestatus == 2
                                    || updatestatus == 3
                                    || updatestatus == 4
                                    || updatestatus == 5
                                    || updatestatus == 6
                                    || updatestatus == 10
                                    || updatestatus == 21
                                    || updatestatus == 22
                                    || updatestatus == 23) {
                                if (jupdateorder.has("extraItemPrice") && !jupdateorder.isNull("extraItemPrice")) {
                                    rlUpgradeServiceItems.setVisibility(View.VISIBLE);
                                    upgradefee = jupdateorder.getDouble("extraItemPrice");
                                    String text = "";
                                    if (Utils.isDoubleEndWithZero(upgradefee)) {
                                        text = "¥ " + Utils.formatDouble(upgradefee);
                                    } else {
                                        text = "¥ " + upgradefee;
                                    }
                                    tvUpgradeItemsFee.setText(text);
                                } else {
                                    rlUpgradeServiceItems.setVisibility(View.GONE);
                                }
                                if (jupdateorder.has("extraServiceItems") && !jupdateorder.isNull("extraServiceItems")) {
                                    JSONArray jupdatearr = jupdateorder.getJSONArray("extraServiceItems");
                                    StringBuffer sb = new StringBuffer();
                                    for (int i = 0; i < jupdatearr.length(); i++) {
                                        JSONObject sitem = jupdatearr.getJSONObject(i);
                                        if (sitem.has("name") && !sitem.isNull("name")) {
                                            sb.append(sitem.getString("name") + "+");
                                        }
                                    }
                                    if (jupdatearr.length() > 0) {
                                        SpannableString spans = new SpannableString("升级服务  " + sb.toString().subSequence(0, sb.toString().length() - 1));
                                        spans.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)),
                                                0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                        textview_order_upgradeitems.setVisibility(View.VISIBLE);
                                        layout_order_show_upgradeitems_one.setVisibility(View.VISIBLE);
                                        textview_order_upgradeitems.setText(sb.substring(0, sb.length() - 1));
                                        if (object.has("payPrice") && !object.isNull("payPrice")) {
                                            payfee = object.getDouble("payPrice");
                                            Utils.setDoubleStr(textView_confirm_price_show, ComputeUtil.add(payfee, upgradefee), "合计 ¥ ", "");
                                        }
                                    }
                                }
                                //升级订单付完款后这里的总价需要加上update里边的totalprice
                                UpdateStatusEqu2_3(object, extraServicePrice, cutDownMoney, UpTotalPrice);
                            }
                        }
                        if (updatestatus != 1) {
                            layout_order_show_upgradeitems_one.setVisibility(View.VISIBLE);
                            textview_order_upgradeitems.setVisibility(View.VISIBLE);
                            rlUpgradeServiceItems.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textview_order_upgradeitems.setVisibility(View.GONE);
                        layout_order_show_upgradeitems_one.setVisibility(View.GONE);
                        rlUpgradeServiceItems.setVisibility(View.GONE);
                        if (object.has("payPrice") && !object.isNull("payPrice")) {
                            payfee = object.getDouble("payPrice");
                            String text = "";
                            if (Utils.isDoubleEndWithZero(payfee)) {
                                text = "合计 ¥ " + Utils.formatDouble(payfee);
                            } else {
                                text = "合计 ¥ " + payfee;
                            }
                            textView_confirm_price_show.setText(text);
                        }
                        if (object.has("refundTag") && !object.isNull("refundTag")) {//实付款底下的不再显示挪到左边底下显示
                            textview_price_back.setVisibility(View.GONE);
                            String refundTag = object.getString("refundTag");
                            textview_price_back.setText(refundTag);
                        } else {
                            textview_price_back.setVisibility(View.GONE);
                        }
                        cutDownMoney = setCutDowmMoney(object, extraServicePrice, reductionPrice); //
                        //没有升级订单
                        NoUpdateOrder(object, extraServicePrice, cutDownMoney);
                    }
                    if (object.has("payPrice") && !object.isNull("payPrice")) {
                        textView_confirm_price_show.setText(setLastShowStyle(Utils.getDoubleStr(object.getDouble("payPrice"), "合计 ¥", "")));
                    }
                    if (type == 1) {
                        if (object.has("totalPrice") && !object.isNull("totalPrice")) {
                            totalPrice = object.getDouble("totalPrice");
                            Utils.setDoubleStr(textview_old_price_andother, totalPrice, "总价¥", "");
                        }
                    }
                    if (object.has("canPayPrice") && !object.isNull("canPayPrice")) {
                        layout_dummy_money_orderdetail.setVisibility(View.VISIBLE);
                        canPayPrice = object.getString("canPayPrice");
                        textview_dummy_detail.setText(canPayPrice);
                    } else {
                        layout_dummy_money_orderdetail.setVisibility(View.GONE);
                    }
                    if (object.has("remainTime") && !object.isNull("remainTime")) {
                        lastOverTime = object.getLong("remainTime");
                    }
                    if (state == 2 && beautician_id == 0 && isVie) {
                        btPay.setBackgroundResource(R.drawable.bg_button_service_apponit_ok);
                        if (isTimeFirst) {
                            SecondTimeDown();
                        }
                    }
                    if (beautician_id != 0 && isVie) {
                        layout_beautiful_work_to.setVisibility(View.VISIBLE);
                        layout_worker.setVisibility(View.VISIBLE);
                    }
                    if (object.has("feedback") && !object.isNull("feedback")) {
                        if (state == 5) {
                            layout_confirm_eva_complaints.setVisibility(View.GONE);
                        }
                        layout_compl_parent.setVisibility(View.VISIBLE);
                        bt_complaints.setVisibility(View.GONE);
                        JSONObject feedback = object.getJSONObject("feedback");
                        textview_compl_title.setText("投诉内容");
                        if (feedback.has("reason") && !feedback.isNull("reason")) {
                            JSONArray arrayReason = feedback.getJSONArray("reason");
                            StringBuilder reasonBuilder = new StringBuilder();
                            if (arrayReason.length() > 0) {
                                for (int i = 0; i < arrayReason.length(); i++) {
                                    if (i == arrayReason.length() - 1) {
                                        reasonBuilder.append(arrayReason.getString(i));
                                        if (feedback.has("content") && !feedback.isNull("content")) {
                                            reasonBuilder.append("\n\n" + feedback.getString("content"));
                                        }
                                    } else {
                                        reasonBuilder.append(arrayReason.getString(i) + "\n\n");
                                    }
                                }
                            } else {
                                reasonBuilder.append(feedback.getString("content"));
                            }
                            textview_show_compl_reason.setText(reasonBuilder.toString());
                        }
                        if (feedback.has("managerStatus") && !feedback.isNull("managerStatus")) {
                            int managerStatus = feedback.getInt("managerStatus");
                            if (managerStatus == 0) {
                                textview_show_compl_status.setText("正在处理中,请您耐心等候");
                            } else if (managerStatus == 1) {
                                textview_show_compl_status.setText("处理完成");
                            }
                        }
                        if (feedback.has("managerStatusName") && !feedback.isNull("managerStatusName")) {
                            textview_show_compl_status.setText(feedback.getString("managerStatusName"));
                        }
                    } else {
                        layout_compl_parent.setVisibility(View.GONE);
                    }
                    if (bt_complaints.getVisibility() == View.GONE && bt_only_eva.getVisibility() == View.GONE) {
                        layout_confirm_eva_complaints.setVisibility(View.GONE);
                    }
                    //统一增加邀请码优惠
                    if (object.has("orderFee") && !object.isNull("orderFee")) {
                        double orderFee = object.getDouble("orderFee");
                        if (orderFee > 0) {
                            rl_orderdetail_promoterCode.setVisibility(View.VISIBLE);
                            Utils.setDoubleStr(tv_orderdetail_promoterCode, orderFee, "-¥", "");
                            image_promoterCode_show_left.setVisibility(View.VISIBLE);
                        } else {
                            rl_orderdetail_promoterCode.setVisibility(View.GONE);
                            image_promoterCode_show_left.setVisibility(View.GONE);
                        }
                    } else {
                        rl_orderdetail_promoterCode.setVisibility(View.GONE);
                        image_promoterCode_show_left.setVisibility(View.GONE);
                    }
                    if (object.has("evaluate") && !object.isNull("evaluate")) {
                        bt_only_eva.setText("" + object.getString("evaluate"));
                    }
                } else {
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                        ToastUtil.showToastShort(OrderDetailFromOrderToConfirmActivity.this, jsonObject.getString("msg"));
                    }
                    showMain(false);
                }
            } catch (JSONException e) {
                Log.e("TAG", "异常e = " + e.toString());
                e.printStackTrace();
                showMain(false);
            }
        }

        private void NoUpdateOrder(JSONObject object, double extraServicePrice,
                                   double cutDownMoney) {
            if (cutDownMoney <= 0) {//优惠券+满减没哟优惠
                Utils.setDoubleStr(textview_old_price_andother, totalPrice, "总价¥", "");
                if (object.has("cardId") && !object.isNull("cardId")) {
                    int cardId = 0;
                    try {
                        cardId = object.getInt("cardId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    textview_old_price_bottom.setVisibility(View.VISIBLE);
                    Utils.setDoubleStr(textview_old_price_bottom, basePrice, CardTag + "¥", "");
                    if (refund > 0) {
                        textview_old_price_bottom.setVisibility(View.VISIBLE);
                        if (cardId > 0) {
                            String text = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                            String text1 = Utils.getDoubleStr(refund, "退款¥", "");
                            textview_old_price_bottom.setText(text + text1);
                        } else {
                            Utils.setDoubleStr(textview_old_price_bottom, refund, "退款¥", "");
                        }
                    } else {
                        if (cardId > 0) {
                            Utils.setDoubleStr(textview_old_price_bottom, basePrice, CardTag + "¥", "");
                        } else {
                            textview_old_price_bottom.setVisibility(View.GONE);
                        }
                    }
                } else {
                    textview_old_price_bottom.setVisibility(View.GONE);
                    if (refund > 0) {
                        textview_old_price_bottom.setVisibility(View.VISIBLE);
                        Utils.setDoubleStr(textview_old_price_bottom, refund, "退款¥", "");
                    } else {
                        textview_old_price_bottom.setVisibility(View.GONE);
                    }
                }
            } else {//优惠券+满减哟优惠
                textview_old_price_bottom.setVisibility(View.VISIBLE);
                Utils.setDoubleStr(textview_old_price_andother, totalPrice, "总价¥", "");
                if (object.has("cardId") && !object.isNull("cardId")) {
                    int cardId = 0;
                    try {
                        cardId = object.getInt("cardId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String text = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                    String text1 = Utils.getDoubleStr(cutDownMoney, "  " + couponCutReductionTag + "¥", "");
                    textview_old_price_bottom.setText(text + text1);
                    if (refund > 0) {
                        if (cardId > 0) {
                            String text2 = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                            String text3 = Utils.getDoubleStr(cutDownMoney, "  " + couponCutReductionTag + "¥", "");
                            String text4 = Utils.getDoubleStr(refund, "退款¥", "");
                            textview_old_price_bottom.setText(text2 + text3 + text4);
                        } else {
                            String text3 = Utils.getDoubleStr(cutDownMoney, couponCutReductionTag + "¥", "");
                            String text4 = Utils.getDoubleStr(refund, "退款¥", "");
                            textview_old_price_bottom.setText(text3 + text4);
                        }
                    } else {
                        if (cardId > 0) {
                            String text2 = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                            String text3 = Utils.getDoubleStr(cutDownMoney, "  " + couponCutReductionTag + "¥", "");
                            textview_old_price_bottom.setText(text2 + text3);
                        } else {
                            Utils.setDoubleStr(textview_old_price_bottom, cutDownMoney, couponCutReductionTag + "¥", "");
                        }
                    }
                } else {
                    Utils.setDoubleStr(textview_old_price_bottom, cutDownMoney, couponCutReductionTag + "¥", "");
                    if (refund > 0) {
                        String text3 = Utils.getDoubleStr(cutDownMoney, couponCutReductionTag + "¥", "");
                        String text4 = Utils.getDoubleStr(refund, "退款¥", "");
                        textview_old_price_bottom.setText(text3 + text4);
                    } else {
                        Utils.setDoubleStr(textview_old_price_bottom, cutDownMoney, couponCutReductionTag + "¥", "");
                    }
                }
            }
        }

        private void UpdateStatusEqu2_3(JSONObject object,
                                        double extraServicePrice, double cutDownMoney,
                                        double UpTotalPrice) {
            if (cutDownMoney <= 0) {//优惠券+满减没哟优惠
                Utils.setDoubleStr(textview_old_price_andother, totalPrice, "总价¥", "");
                if (object.has("cardId") && !object.isNull("cardId")) {
                    int cardId = 0;
                    try {
                        cardId = object.getInt("cardId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    textview_old_price_bottom.setVisibility(View.VISIBLE);
                    Utils.setDoubleStr(textview_old_price_bottom, basePrice, CardTag + "¥", "/天");
                    if (refund > 0) {
                        if (cardId > 0) {
                            String text3 = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                            String text4 = Utils.getDoubleStr(refund, "退款¥", "");
                            textview_old_price_bottom.setText(text3 + text4);
                        } else {
                            Utils.setDoubleStr(textview_old_price_bottom, refund, "退款¥", "");
                        }
                    } else {
                        if (cardId > 0) {
                            Utils.setDoubleStr(textview_old_price_bottom, basePrice, CardTag + "¥", "");
                        } else {
                            textview_old_price_bottom.setVisibility(View.GONE);
                        }
                    }
                } else {
                    textview_old_price_bottom.setVisibility(View.GONE);
                    if (refund > 0) {
                        textview_old_price_bottom.setVisibility(View.VISIBLE);
                        Utils.setDoubleStr(textview_old_price_bottom, refund, "退款¥", "");
                    } else {
                        textview_old_price_bottom.setVisibility(View.GONE);
                    }
                }
            } else {//优惠券+满减哟优惠
                textview_old_price_bottom.setVisibility(View.VISIBLE);
                Utils.setDoubleStr(textview_old_price_andother, totalPrice, "总价¥", "");
                if (object.has("cardId") && !object.isNull("cardId")) {
                    int cardId = 0;
                    try {
                        cardId = object.getInt("cardId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String text = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                    String text1 = Utils.getDoubleStr(cutDownMoney, "  " + couponCutReductionTag + "¥", "");
                    textview_old_price_bottom.setText(text + text1);
                    if (refund > 0) {
                        if (cardId > 0) {
                            String text2 = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                            String text3 = Utils.getDoubleStr(cutDownMoney, "  " + couponCutReductionTag + "¥", "");
                            String text4 = Utils.getDoubleStr(refund, "退款¥", "");
                            textview_old_price_bottom.setText(text2 + text3 + text4);
                        } else {
                            String text3 = Utils.getDoubleStr(cutDownMoney, couponCutReductionTag + "¥", "");
                            String text4 = Utils.getDoubleStr(refund, "退款¥", "");
                            textview_old_price_bottom.setText(text3 + text4);
                        }
                    } else {
                        if (cardId > 0) {
                            String text2 = Utils.getDoubleStr(basePrice, CardTag + "¥", "");
                            String text3 = Utils.getDoubleStr(cutDownMoney, "  " + couponCutReductionTag + "¥", "");
                            textview_old_price_bottom.setText(text2 + text3);
                        } else {
                            Utils.setDoubleStr(textview_old_price_bottom, cutDownMoney, couponCutReductionTag + "¥", "");
                        }
                    }
                } else {
                    Utils.setDoubleStr(textview_old_price_bottom, cutDownMoney, couponCutReductionTag + "¥", "");
                    if (refund > 0) {
                        String text3 = Utils.getDoubleStr(cutDownMoney, couponCutReductionTag + "¥", "");
                        String text4 = Utils.getDoubleStr(refund, "退款¥", "");
                        textview_old_price_bottom.setText(text3 + text4);
                    } else {
                        Utils.setDoubleStr(textview_old_price_bottom, cutDownMoney, couponCutReductionTag + "¥", "");
                    }
                }
            }
        }

        private void setTypeName() {
            if (type == 1) {
                ordersBean.setTypeName("洗护");
            } else if (type == 2) {
                ordersBean.setTypeName("寄养");
            } else if (type == 3) {
                ordersBean.setTypeName("游泳");
            } else if (type == 4) {
                ordersBean.setTypeName("训练");
            }
        }

        private void getWorker(JSONObject object) throws JSONException {
            if (object.has("worker") && !object.isNull("worker")) {
                JSONObject workerObject = object.getJSONObject("worker");
                allPics.clear();
                layout_all_imgs.setVisibility(View.GONE);
                for (int i = 0; i < allLayout.size(); i++) {
                    allLayout.get(i).setVisibility(View.GONE);
                }
                if (workerObject.has("beforePics") && !workerObject.isNull("beforePics")) {
                    JSONArray array = workerObject.getJSONArray("beforePics");
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            BeforeAfterPics beforeAfterPics = new BeforeAfterPics();
                            beforeAfterPics.imgUrl = array.getString(i);
                            beforeAfterPics.index = 0;
                            allPics.add(beforeAfterPics);
                        }
                    }
                }
                if (workerObject.has("pics") && !workerObject.isNull("pics")) {
                    JSONArray array = workerObject.getJSONArray("pics");
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            BeforeAfterPics beforeAfterPics = new BeforeAfterPics();
                            beforeAfterPics.imgUrl = array.getString(i);
                            beforeAfterPics.index = 1;
                            allPics.add(beforeAfterPics);
                        }
                    }
                }
                if (allPics.size() <= 0) {
                    layout_all_imgs.setVisibility(View.GONE);
                } else {
                    pics = new String[allPics.size()];
                    layout_all_imgs.setVisibility(View.VISIBLE);
                    for (int i = 0; i < allPics.size(); i++) {
                        allLayout.get(i).setVisibility(View.VISIBLE);
                        GlideUtil.loadImg(mContext, allPics.get(i).imgUrl, allImg.get(i), R.drawable.icon_default);
                        if (allPics.get(i).index == 0) {
                            allText.get(i).setText("服务前");
                        } else if (allPics.get(i).index == 1) {
                            allText.get(i).setText("服务后");
                        }
                        pics[i] = allPics.get(i).imgUrl;
                    }
                }
                if (workerObject.has("copy") && !workerObject.isNull("copy")) {
                    String copy = workerObject.getString("copy");
                    textview_worker_eva_content.setText(copy + "");
                }
                if (workerObject.has("gender") && !workerObject.isNull("gender")) {
                    if (workerObject.getInt("gender") == 1) {
                        order_beau_level.setImageResource(R.drawable.icon_man);
                    } else {
                        order_beau_level.setImageResource(R.drawable.icon_women);
                    }
                }
                if (workerObject.has("user") && !workerObject.isNull("user")) {
                    JSONObject objectUser = workerObject.getJSONObject("user");
                    if (objectUser.has("cellPhone") && !objectUser.isNull("cellPhone")) {
                        beauPhone = objectUser.getString("cellPhone");
                    }
                }
                if (workerObject.has("level") && !workerObject.isNull("level")) {
                    JSONObject workLever = workerObject.getJSONObject("level");
                    if (workLever.has("sort") && !workLever.isNull("sort")) {
                        int sort = workLever.getInt("sort");
                        if (sort == 10) {
                            sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_junior);
                        } else if (sort == 20) {
                            sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_middle);
                        } else if (sort == 30) {
                            sriv_orderdetail_beautician_sex.setImageResource(R.drawable.icon_top);
                        }
                    }
                    if (workLever.has("name") && !workLever.isNull("name")) {
                        levelName = workLever.getString("name");
                        tvBeauticianLevel.setText(levelName);
                        textview_worker_level.setText(levelName + "");
                    }
                }
                if (workerObject.has("avatar") && !workerObject.isNull("avatar")) {
                    avatar = workerObject.getString("avatar");
                    GlideUtil.loadCircleImg(mContext, avatar, img_worker_icon, R.drawable.icon_default);
                }
                if (workerObject.has("workerGrade") && !workerObject.isNull("workerGrade")) {
                    int workerGrade = workerObject.getInt("workerGrade");
                    int levels = workerGrade / 10;
                    int stars = workerGrade % 10;
                    showStars(levels, stars);
                }
                if (workerObject.has("totalOrderAmount") && !workerObject.isNull("totalOrderAmount")) {
                    int totalOrderAmount = workerObject.getInt("totalOrderAmount");
                    tvBeauticianOrdernum.setText("接单：" + totalOrderAmount);
                    textview_worker_accept_order_nums.setText("接单：" + totalOrderAmount);
                }
                if (workerObject.has("realName") && !workerObject.isNull("realName")) {
                    realName = workerObject.getString("realName");
                    tvBeauticianName.setText(realName);
                    textview_worker_name.setText(realName);
                }
                if (workerObject.has("WorkerId") && !workerObject.isNull("WorkerId")) {
                    beautician_id = workerObject.getInt("WorkerId");
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            prsScrollView.onRefreshComplete();
            showMain(false);
        }
    };

    private double setCutDowmMoney(JSONObject object, double extraServicePrice, double reductionPrice) throws JSONException {
        double homeCoupon = homeCouponAmount;//后台返回数据格式异常 原string 现在double 客户端崩了。
        cutDownMoney = 0;
        if (object.has("cardId") && !object.isNull("cardId")) {
            int cardId = object.getInt("cardId");
            if (cardId > 0) {
                if (extraItemPrice > 0) {
                    if (couponAmount <= extraItemPrice) {
                        cutDownMoney = couponAmount + reductionPrice; //
                    } else {
                        cutDownMoney = extraItemPrice + reductionPrice; //
                    }
                    if (extraServicePrice > 0) {
                        if (homeCoupon >= extraServicePrice) {
                            cutDownMoney = cutDownMoney + extraServicePrice;
                        }
                        if (homeCoupon < extraServicePrice) {
                            cutDownMoney = cutDownMoney + homeCoupon;
                        }
                    }
                }
            } else {
                if (basePrice + extraItemPrice > 0) {
                    if (couponAmount <= (basePrice + extraItemPrice)) {
                        cutDownMoney = couponAmount + reductionPrice; //
                    } else {
                        cutDownMoney = basePrice + extraItemPrice + reductionPrice; //
                    }
                    if (extraServicePrice > 0) {
                        if (homeCoupon >= extraServicePrice) {
                            cutDownMoney = cutDownMoney + extraServicePrice;
                        } else {
                            cutDownMoney = cutDownMoney + homeCoupon;
                        }
                    }
                }
            }
        } else {
            if (basePrice + extraItemPrice > 0) {
                if (couponAmount <= (basePrice + extraItemPrice)) {
                    cutDownMoney = couponAmount + reductionPrice; //
                } else {
                    cutDownMoney = basePrice + extraItemPrice + reductionPrice; //
                }
                if (extraServicePrice > 0) {
                    if (homeCoupon >= extraServicePrice) {
                        cutDownMoney = cutDownMoney + extraServicePrice;
                    } else {
                        cutDownMoney = cutDownMoney + homeCoupon;
                    }
                }
            }
        }
        return cutDownMoney;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**分享需要使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE:
                    // 未返回
                    getOrderDetails();
                    break;
                case Global.ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW:
                    Utils.mLogError("==-->评价返回");
                    // 评价返回
                    getOrderDetails();
                    break;
            }
        }
    }

    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加QQ、QZone平台
        addQQQZonePlatform();
        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform() {
        String appId = "1104724367";
        String appKey = "gASimi0oEHprSSxe";
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(orderConfirm, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx1668e9f200eb89b2";
        String appSecret = "4b1e452b724bc085ac72058dd39adf01";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(orderConfirm, appId, appSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(orderConfirm, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private Calendar calendar;

    private void toNext(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("serviceid", serviceId);
        intent.putExtra("shopid", shopId);
        intent.putExtra("previous", Global.ORDER_OTHER_STATUS_TO_SHOPDETAIL);
        intent.putExtra("beautician_id", beautician_id);
        intent.putExtra("areaid", areaId);
        intent.putExtra("shopWxImg", shopWxImg);
        intent.putExtra("shopWxNum", shopWxNum);
        startActivity(intent);
    }

    private void SecondTimeDown() {
        isTimeFirst = false;
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (lastOverTime > 0) {
                    lastOverTime -= 1000;
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.arg1 = (int) lastOverTime;
                    handler.sendMessage(msg);
                } else {
                    if (timer != null) {
                        handler.sendEmptyMessage(4);
                        timer.cancel();
                        timer = null;
                    }
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goNext(int index, String[] pics) {
        Intent intent = new Intent(mContext, BeauticianCommonPicActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("pics", pics);
        mContext.startActivity(intent);
    }

    private void goNextForData(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("previous", Global.ORDER_CHANGE_CUSTOMEORPHONE);
        intent.putExtra("OrderId", OrderId);
        intent.putExtra("cname", cname);
        intent.putExtra("cphone", cphone);
        startActivity(intent);
    }

    private class MReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra("index", 0);
            if (index == 1) {
                getOrderDetails();
            }
        }
    }

    private SpannableString setLastShowStyle(String lastOnePrice) {
        SpannableString styledText = new SpannableString(lastOnePrice);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.order_show_left_heji), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.service_old_service_two), 2, lastOnePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishWithAnimation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
