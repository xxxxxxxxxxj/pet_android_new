package com.haotang.pet;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.OrderPayScanInfoAdapter;
import com.haotang.pet.adapter.PaysuccessDiscountInfoAdapter;
import com.haotang.pet.entity.ActivityPage;
import com.haotang.pet.entity.AdDialogDataBean;
import com.haotang.pet.entity.OrderPayScanInfo;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * 线下商品扫码，狗证支付成功页
 */
public class PaySuccessActivity extends SuperActivity implements
        OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private SharedPreferenceUtil spUtil;
    private int previous = 0;
    private int previous_liucheng = 0;
    public static PaySuccessActivity act;
    private TextView content;
    private Button button_to_myfragment;
    private int orderId;
    private int type;
    private RelativeLayout rl_paysuccess_xfje;
    private TextView tv_paysuccess_xfje;
    private TextView tv_paysuccess_xfprice;
    private View vw_paysuccess_xfje;
    private RecyclerView mlv_paysuccess_info;
    /**
     * start 次卡
     */
    private LinearLayout layout_card_pay_success;
    private Button button_look_cards;
    private Button button_back_main;
    /**
     * end 次卡
     */
    private LinearLayout layout_ximei_pay_success;
    private Button button_ddxq;
    private Button button_wsxx;
    private int sex;
    private int petCustomerId;
    private ArrayList<Pet> petlist = new ArrayList<Pet>();
    private Button button_back_old;
    //虚拟币会员
    private LinearLayout old_layout_show;
    private LinearLayout new_layout_success;
    private TextView textview_dummp_money_num;
    private TextView textview_dummp_money_can_do;
    private TextView textview_dummp_money_success_detail;
    private TextView textview_isvip;
    private Button button_look_order_detail;
    private int myselfDummpNum = -1;
    private String compare_desc;
    private int isVip = -1;
    private LinearLayout layout_can_give;

    private LinearLayout layout_show_recharge_need;
    private TextView textview_xiaofei_detail;
    private Button button_to_recharge;

    private String paySuccessRechageCopy;
    private String paySuccessTips;
    private String rechargeRightNow;
    private String firstCopy;
    private String secondCopy;
    private double rechargeValue;
    private double instantBonus;
    private List<AdDialogDataBean> listAdDialogDataBean = new ArrayList<AdDialogDataBean>();
    private LinearLayout layout_foster_notice;
    private LayoutParams llp;
    private List<ActivityPage> localBannerList = new ArrayList<ActivityPage>();
    //文件名称
    String fileName_one = "ad_paysuccess_one.txt";
    String fileName_one_day = "ad_paysuccess_one_day.txt";
    private RelativeLayout rl_paysuccess_discountinfo;
    private RecyclerView rv_paysuccess_discountinfo;
    private TextView tv_paysuccess_payprice;
    private ArrayList<String> discountList;
    private double payPrice;
    private TextView tv_paysuccess_gobuycard;
    private LinearLayout ll_paysuccess_cardpaytext;
    private TextView tv_paysuccess_cardpaytext;
    private String consumePriceTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paysuccess);
        initData();
        intiView();
        setView();
        initLinsetr();
        getMainActivity();
        try {
            Log.e("TAG", "GOTOMARKET_DIALOG_TRUE = " + spUtil.getBoolean("GOTOMARKET_DIALOG_TRUE", false));
            if (!spUtil.getBoolean("GOTOMARKET_DIALOG_TRUE", false)) {//先判断是否点击跳转到应用市场
                Log.e("TAG", "GOTOMARKET_DIALOG_FALSE = " + spUtil.getBoolean("GOTOMARKET_DIALOG_FALSE", false));
                //再判断是否点击暂时没有
                if (spUtil.getBoolean("GOTOMARKET_DIALOG_FALSE", false)) {
                    //再判断距离上一次点击暂时没有是否大于10天且判断打开次数是否是5次
                    String gotomarket_dialog_time = spUtil.getString("GOTOMARKET_DIALOG_TIME", "");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String format = df.format(new Date());// new Date()为获取当前系统时间
                    Log.e("TAG", "gotomarket_dialog_time = " + gotomarket_dialog_time);
                    Log.e("TAG", "format = " + format);
                    Log.e("TAG", "间隔时间 = " + Utils.daysBetween(gotomarket_dialog_time, format));
                    if (Utils.daysBetween(gotomarket_dialog_time, format) >= 10) {
                        //弹出
                        Utils.goMarketDialog(this);
                    }
                } else {
                    Log.e("TAG", "弹出");
                    //弹出
                    Utils.goMarketDialog(this);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "弹框异常e = " + e.toString());
            e.printStackTrace();
        }
    }

    private void getMainActivity() {
        int activityPage = 0;
        if (type == 2) {//寄养
            activityPage = 3;
        } else if (type == 20) {//商城
            activityPage = 5;
        } else if (previous == Global.BATHBEAY_NEW_TO_ORDERPAY) {//洗美
            activityPage = 4;
        } else {//其他
            activityPage = 2;
        }
        localBannerList.clear();
        CommUtil.getActivityPage(PaySuccessActivity.this, spUtil.getInt("nowShopCityId",0), spUtil.getInt("isFirstLogin", 0), activityPage,Double.parseDouble(spUtil.getString("lat_home","0")),Double.parseDouble(spUtil.getString("lng_home","0")), handlerHomeActivity);
    }

    private AsyncHttpResponseHandler handlerHomeActivity = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            int isExist = 0;
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject jdata = object.getJSONObject("data");
                        if (jdata.has("isExist") && !jdata.isNull("isExist")) {
                            isExist = jdata.getInt("isExist");
                        }
                        if (jdata.has("list") && !jdata.isNull("list")) {
                            JSONArray jarrlist = jdata.getJSONArray("list");
                            if (jarrlist.length() > 0) {
                                for (int i = 0; i < jarrlist.length(); i++) {
                                    localBannerList.add(ActivityPage.json2Entity(jarrlist
                                            .getJSONObject(i)));
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (localBannerList.size() > 0 && isExist == 0) {//有弹框
                for (int i = 0; i < localBannerList.size(); i++) {
                    ActivityPage activityPage = localBannerList.get(i);
                    if (activityPage != null) {
                        int id = activityPage.getId();
                        if (activityPage.getCountType() == 0) {//显示一次
                            String tag_paysuccess_one = Utils.readFileData(PaySuccessActivity.this, fileName_one);
                            Log.e("TAG", "tag_paysuccess_one = " + tag_paysuccess_one);
                            if (Utils.isStrNull(tag_paysuccess_one)) {
                                String[] split = tag_paysuccess_one.split(",");
                                if (split != null && split.length > 0) {
                                    boolean isXianShied = false;
                                    for (int j = 0; j < split.length; j++) {
                                        if (Integer.parseInt(split[j]) == id) {
                                            isXianShied = true;
                                            break;
                                        }
                                    }
                                    if (isXianShied) {
                                        activityPage.setDelete(true);
                                    } else {
                                        Utils.writeFileData(PaySuccessActivity.this, fileName_one, id + ",");
                                    }
                                } else {
                                    Utils.writeFileData(PaySuccessActivity.this, fileName_one, id + ",");
                                }
                            } else {
                                Utils.writeFileData(PaySuccessActivity.this, fileName_one, id + ",");
                            }
                        } else if (activityPage.getCountType() == 1) {//每次都显示

                        } else if (activityPage.getCountType() == 2) {//每日一次
                            Calendar c = Calendar.getInstance();
                            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            int mDay = c.get(Calendar.DAY_OF_MONTH);
                            String tag_paysuccess_one_day = Utils.readFileData(PaySuccessActivity.this, fileName_one_day);
                            Log.e("TAG", "tag_paysuccess_one_day = " + tag_paysuccess_one_day);
                            if (Utils.isStrNull(tag_paysuccess_one_day)) {
                                String[] split = tag_paysuccess_one_day.split(",");
                                if (split != null && split.length > 0) {
                                    boolean isXianShied = false;
                                    for (int j = 0; j < split.length; j++) {
                                        String[] split1 = split[j].split("_");
                                        if (split1 != null && split1.length == 2) {
                                            if (Integer.parseInt(split1[0]) == id && Integer.parseInt(split1[1]) == mDay) {
                                                isXianShied = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (isXianShied) {
                                        activityPage.setDelete(true);
                                    } else {
                                        Utils.writeFileData(PaySuccessActivity.this, fileName_one_day, id + "_" + mDay + ",");
                                    }
                                } else {
                                    Utils.writeFileData(PaySuccessActivity.this, fileName_one_day, id + "_" + mDay + ",");
                                }
                            } else {
                                Utils.writeFileData(PaySuccessActivity.this, fileName_one_day, id + "_" + mDay + ",");
                            }
                        }
                    }
                }
                Iterator<ActivityPage> it = localBannerList.iterator();
                while (it.hasNext()) {
                    ActivityPage activityPage = it.next();
                    if (activityPage != null && activityPage.isDelete()) {
                        it.remove();
                    }
                }
            }
            if (localBannerList.size() > 0 && isExist == 0) {//有弹框
                Utils.ActivityPage(PaySuccessActivity.this, localBannerList,3);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void initLinsetr() {
        ibBack.setOnClickListener(this);
        button_look_cards.setOnClickListener(this);
        button_back_main.setOnClickListener(this);
        button_ddxq.setOnClickListener(this);
        button_wsxx.setOnClickListener(this);
        button_to_myfragment.setOnClickListener(this);
        button_back_old.setOnClickListener(this);
        button_look_order_detail.setOnClickListener(this);
        textview_dummp_money_can_do.setOnClickListener(this);
        textview_isvip.setOnClickListener(this);
        button_to_recharge.setOnClickListener(this);
        tv_paysuccess_gobuycard.setOnClickListener(this);

    }

    @SuppressWarnings("unchecked")
    private void setView() {
        tvTitle.setText("支付成功");
        if (previous == Global.UPDATENEW_TO_PAYSUCCESS) {
            if (Utils.isStrNull(consumePriceTip)) {
                tv_paysuccess_gobuycard.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                tv_paysuccess_gobuycard.getPaint().setAntiAlias(true);//抗锯齿
                ll_paysuccess_cardpaytext.setVisibility(View.VISIBLE);
                if (consumePriceTip.contains("@@")) {
                    String[] split = consumePriceTip.split("@@");
                    Log.e("TAG", "split.length = " + split.length);
                    if (split != null && split.length > 0) {
                        SpannableString ss = new SpannableString(consumePriceTip.replace("@@", ""));
                        int startIndex = split[0].length();
                        int endIndex = split[0].length() + split[1].length();
                        Log.e("TAG", "startIndex = " + startIndex);
                        Log.e("TAG", "endIndex = " + endIndex);
                        ss.setSpan(
                                new ForegroundColorSpan(mContext.getResources().getColor(
                                        R.color.aD0021B)), startIndex, endIndex,
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        tv_paysuccess_cardpaytext.setText(ss);
                    }
                }
            } else {
                ll_paysuccess_cardpaytext.setVisibility(View.GONE);
            }
        } else {
            ll_paysuccess_cardpaytext.setVisibility(View.GONE);
        }
        if (previous == Global.FOSNEW_TO_PAYSUCCESS || previous == Global.UPDATENEW_TO_PAYSUCCESS) {
            tv_paysuccess_payprice.setVisibility(View.VISIBLE);
            tv_paysuccess_payprice.setText("¥" + payPrice);
        } else {
            tv_paysuccess_payprice.setVisibility(View.GONE);
        }
        if (discountList != null && discountList.size() > 0) {
            rl_paysuccess_discountinfo.setVisibility(View.VISIBLE);
            rv_paysuccess_discountinfo.setHasFixedSize(true);
            rv_paysuccess_discountinfo.setNestedScrollingEnabled(false);
            NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
            noScollFullLinearLayoutManager.setScrollEnabled(false);
            rv_paysuccess_discountinfo.setLayoutManager(noScollFullLinearLayoutManager);
            rv_paysuccess_discountinfo.setAdapter(new PaysuccessDiscountInfoAdapter(R.layout.item_paysuccess_discountinfo, discountList));
        } else {
            rl_paysuccess_discountinfo.setVisibility(View.GONE);
        }
        if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {// 办证
            content.setVisibility(View.VISIBLE);
            content.setText("办证详情,请到个人中心查看");
        } else if (previous == Global.MIPCA_TO_ORDERPAY) {// 商品支付
            rl_paysuccess_xfje.setVisibility(View.VISIBLE);
            tv_paysuccess_xfje.setVisibility(View.VISIBLE);
            tv_paysuccess_xfprice.setVisibility(View.VISIBLE);
            vw_paysuccess_xfje.setVisibility(View.VISIBLE);
            mlv_paysuccess_info.setVisibility(View.VISIBLE);
            double totalPayPrice = getIntent().getDoubleExtra("totalPayPrice",
                    0.00);
            ArrayList<OrderPayScanInfo> listItems = (ArrayList<OrderPayScanInfo>) getIntent()
                    .getSerializableExtra("listItems");
            String text = "¥ " + Math.round(totalPayPrice);
            SpannableString ss = new SpannableString(text);
            ss.setSpan(new TextAppearanceSpan(this, R.style.style4), 0, 1,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_paysuccess_xfprice.setText(ss);
            mlv_paysuccess_info.setLayoutManager(new LinearLayoutManager(this));
            mlv_paysuccess_info.setAdapter(new OrderPayScanInfoAdapter(R.layout.item_scanshopinfo, listItems));
        }
        /**
         * 新版洗美特色服务支付成功数据处理
         */
        textview_dummp_money_num.setText(myselfDummpNum + "");
        textview_dummp_money_success_detail.setText(compare_desc);
        if (isVip == 1) {
            textview_isvip.setText("查看权益");
        } else {
            textview_isvip.setText("成为会员");
        }
        if (myselfDummpNum <= 0) {
            layout_can_give.setVisibility(View.GONE);
        }
    }

    private void initData() {
        act = this;
        consumePriceTip = getIntent().getStringExtra("consumePriceTip");
        discountList = getIntent().getStringArrayListExtra("discountList");
        spUtil = SharedPreferenceUtil.getInstance(this);
        orderId = getIntent().getIntExtra("orderId", 0);
        type = getIntent().getIntExtra("type", 0);
        previous = getIntent().getIntExtra("previous", 0);
        payPrice = getIntent().getDoubleExtra("payPrice", -1);
        previous_liucheng = getIntent().getIntExtra("previous_liucheng", 0);
        Utils.mLogError("==-->流程购买次卡  " + getClass().getName() + " --- 000 --- " + previous_liucheng);
        petCustomerId = getIntent().getIntExtra("petCustomerId", 0);

        compare_desc = getIntent().getStringExtra("compare_desc");
        myselfDummpNum = getIntent().getIntExtra("myself", -1);
        int shopCartMemberLevelId = spUtil.getInt("shopCartMemberLevelId", 0);
        if (shopCartMemberLevelId > 0) {
            isVip = 1;
        } else {
            isVip = 0;
        }

        paySuccessRechageCopy = getIntent().getStringExtra("paySuccessRechageCopy");
        paySuccessTips = getIntent().getStringExtra("paySuccessTips");
        rechargeRightNow = getIntent().getStringExtra("rechargeRightNow");
        firstCopy = getIntent().getStringExtra("firstCopy");
        secondCopy = getIntent().getStringExtra("secondCopy");
        instantBonus = getIntent().getDoubleExtra("instantBonus", 0);
        rechargeValue = getIntent().getDoubleExtra("rechargeValue", 0);
    }

    private void intiView() {
        tv_paysuccess_gobuycard = (TextView) findViewById(R.id.tv_paysuccess_gobuycard);
        ll_paysuccess_cardpaytext = (LinearLayout) findViewById(R.id.ll_paysuccess_cardpaytext);
        tv_paysuccess_cardpaytext = (TextView) findViewById(R.id.tv_paysuccess_cardpaytext);
        tv_paysuccess_payprice = (TextView) findViewById(R.id.tv_paysuccess_payprice);
        rl_paysuccess_discountinfo = (RelativeLayout) findViewById(R.id.rl_paysuccess_discountinfo);
        rv_paysuccess_discountinfo = (RecyclerView) findViewById(R.id.rv_paysuccess_discountinfo);
        content = (TextView) findViewById(R.id.content);
        button_to_myfragment = (Button) findViewById(R.id.button_to_myfragment);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        rl_paysuccess_xfje = (RelativeLayout) findViewById(R.id.rl_paysuccess_xfje);
        tv_paysuccess_xfje = (TextView) findViewById(R.id.tv_paysuccess_xfje);
        tv_paysuccess_xfprice = (TextView) findViewById(R.id.tv_paysuccess_xfprice);
        vw_paysuccess_xfje = (View) findViewById(R.id.vw_paysuccess_xfje);
        mlv_paysuccess_info = (RecyclerView) findViewById(R.id.mlv_paysuccess_info);
        layout_card_pay_success = (LinearLayout) findViewById(R.id.layout_card_pay_success);
        button_look_cards = (Button) findViewById(R.id.button_look_cards);
        button_back_main = (Button) findViewById(R.id.button_back_main);
        layout_ximei_pay_success = (LinearLayout) findViewById(R.id.layout_ximei_pay_success);
        button_ddxq = (Button) findViewById(R.id.button_ddxq);
        button_wsxx = (Button) findViewById(R.id.button_wsxx);
        button_back_old = (Button) findViewById(R.id.button_back_old);

        old_layout_show = (LinearLayout) findViewById(R.id.old_layout_show);
        new_layout_success = (LinearLayout) findViewById(R.id.new_layout_success);

        textview_dummp_money_num = (TextView) findViewById(R.id.textview_dummp_money_num);
        textview_dummp_money_can_do = (TextView) findViewById(R.id.textview_dummp_money_can_do);
        textview_dummp_money_success_detail = (TextView) findViewById(R.id.textview_dummp_money_success_detail);
        textview_isvip = (TextView) findViewById(R.id.textview_isvip);
        button_look_order_detail = (Button) findViewById(R.id.button_look_order_detail);
        layout_can_give = (LinearLayout) findViewById(R.id.layout_can_give);
        layout_show_recharge_need = (LinearLayout) findViewById(R.id.layout_show_recharge_need);
        button_to_recharge = (Button) findViewById(R.id.button_to_recharge);
        textview_xiaofei_detail = (TextView) findViewById(R.id.textview_xiaofei_detail);
        layout_foster_notice = (LinearLayout) findViewById(R.id.layout_foster_notice);

        Utils.mLogError("==-->type: " + type);
        if (type == 1) {//洗美，特色服务，犬证，商品扫码支付
            old_layout_show.setVisibility(View.GONE);
            new_layout_success.setVisibility(View.VISIBLE);
            button_look_order_detail.setVisibility(View.VISIBLE);
        } else if (previous == Global.MIPCA_TO_ORDERPAY || previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {//犬证，商品扫码支付
            old_layout_show.setVisibility(View.GONE);
            new_layout_success.setVisibility(View.VISIBLE);
            button_look_order_detail.setVisibility(View.GONE);
        } else if (type == 20) {//商城下单 显示
            old_layout_show.setVisibility(View.GONE);
            new_layout_success.setVisibility(View.VISIBLE);
            button_look_order_detail.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            old_layout_show.setVisibility(View.GONE);
            new_layout_success.setVisibility(View.VISIBLE);
            button_look_order_detail.setVisibility(View.VISIBLE);
            llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layout_foster_notice.setVisibility(View.VISIBLE);
        } else {
            old_layout_show.setVisibility(View.VISIBLE);
            new_layout_success.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(paySuccessRechageCopy)) {
            layout_show_recharge_need.setVisibility(View.GONE);
        } else {
            layout_show_recharge_need.setVisibility(View.VISIBLE);
            String[] str = paySuccessRechageCopy.split("#");
            textview_xiaofei_detail.setText(Utils.getTextAndColorFive(
                    "#666666", str[0].replace("1", "").replace("2", ""),
                    "#FF3A1E", firstCopy,
                    "#666666", str[1].replace("1", "").replace("2", ""),
                    "#FF3A1E", secondCopy,
                    "#666666", str[2].replace("1", "").replace("2", "")
            ));
            button_to_recharge.setText(rechargeRightNow + "");
        }
    }

    private void sendToMian() {
        for (int i = 0; i < MApplication.listAppoint.size(); i++) {
            MApplication.listAppoint.get(i).finish();
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.mainactivity");
        if (previous == Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY) {//到洗美订单列表页
            intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
        } else {
            intent.putExtra("previous", Global.CARDS_TO_MAINACTIVITY);
        }
        sendBroadcast(intent);
        Utils.mLogError("开始发送广播");
        finishWithAnimation();
    }

    private void sendToMyFragment() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.mainactivity");
        intent.putExtra("previous", Global.PAYSUCCESS_TO_MYFRAGMENT);
        sendBroadcast(intent);
        Utils.mLogError("开始发送广播");
        finishWithAnimation();
    }

    private void sendToMyCardFragment() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.mainactivity");
        intent.putExtra("previous", Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
        sendBroadcast(intent);
        Utils.mLogError("sendToMyCardFragment 开始发送广播");
        finishWithAnimation();
    }

    private void sendToBroad(String action, int previous_liucheng, int previous) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("previous_liucheng", previous_liucheng);
        intent.putExtra("previous", previous);
        sendBroadcast(intent);
        Utils.mLogError("开始发送广播");
        finishWithAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PaySuccessActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
        if (previous == Global.BATH_TO_ORDERPAY
                || previous == Global.ORDER_TOPAYDETAIL_TOGIVE_MONEY
                || previous == Global.SWIM_DETAIL_TO_ORDERDETAIL
                || previous == Global.URGENT_TO_ORDERDETAIL
                || previous == Global.BATHBEAY_NEW_TO_ORDERPAY) {
            if (previous == Global.SWIM_DETAIL_TO_ORDERDETAIL) {
                content.setVisibility(View.VISIBLE);
                content.setText("您的订单已支付成功");
            }
            removeTopActivity();
            clearData();
        } else if (previous == Global.CARDSDETAIL_TO_ORDERPAY) {
            if (previous_liucheng <= 0/*&&previous_liucheng==Global.ORDERDETAIL_TO_BUY_CARD*/) {
                removeActivity();
            }
        } else if (previous == Global.CARDSDETAIL_NEW_TO_BUT_CARD) {
//			SuccessClickSendBroadCast("android.intent.action.AppointMentNewActivity");
        } else {
            removeActivity();
            clearData();
        }
    }

    private void removeTopActivity() {
        for (int i = 0; i < MApplication.listAppoint.size(); i++) {
            MApplication.listAppoint.get(i).finish();
        }
        MApplication.listAppoint.clear();
        try {
            if (ServiceNewActivity.act != null)
                ServiceNewActivity.act.finish();
            if (CharacteristicServiceActivity.act != null)
                CharacteristicServiceActivity.act.finish();
            if (MainToBeauList.act != null)
                MainToBeauList.act.finish();
            if (BeauticianDetailActivity.act != null)
                BeauticianDetailActivity.act.finish();
            if (OrderDetailFromOrderToConfirmActivity.orderConfirm != null) {
                OrderDetailFromOrderToConfirmActivity.orderConfirm.finish();
            }
            if (ADActivity.act != null) {
                ADActivity.act.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeActivity() {
        for (int i = 0; i < MApplication.listAppoint.size(); i++) {
            MApplication.listAppoint.get(i).finish();
        }
        MApplication.listAppoint.clear();
        try {
            if (ServiceNewActivity.act != null)
                ServiceNewActivity.act.finish();
            if (CharacteristicServiceActivity.act != null)
                CharacteristicServiceActivity.act.finish();
            if (MainToBeauList.act != null)
                MainToBeauList.act.finish();
            if (BeauticianDetailActivity.act != null)
                BeauticianDetailActivity.act.finish();
            if (OrderDetailFromOrderToConfirmActivity.orderConfirm != null) {
                OrderDetailFromOrderToConfirmActivity.orderConfirm.finish();
            }
            if (ADActivity.act != null) {
                ADActivity.act.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PaySuccessActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
    }

    private void clearData() {
        spUtil.removeData("newaddrid");
        spUtil.removeData("newaddr");
        spUtil.removeData("newlat");
        spUtil.removeData("newlng");
        spUtil.removeData("isAccept");
        spUtil.removeData("charactservice");
        spUtil.removeData("changepet");
        spUtil.removeData("newpetkindid");
        spUtil.removeData("newpetname");
        spUtil.removeData("newpetimg");
        spUtil.removeData("newaddrid");
        spUtil.removeData("newaddr");
        spUtil.removeData("newlat");
        spUtil.removeData("newlng");
        spUtil.removeData("isAccept");
        spUtil.removeData("charactservice");
        spUtil.removeData("changepet");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_paysuccess_gobuycard:
                startActivity(new Intent(mContext, GiftCardListActivity.class));
                finish();
                break;
            case R.id.ib_titlebar_back:
                if (previous_liucheng > 0 && previous_liucheng == Global.ORDERDETAIL_TO_BUY_CARD) {
                    SuccessClickSendBroadCast("android.intent.action.OrderDetailActivity");
                } else if (previous_liucheng > 0 && previous_liucheng == Global.UPDATEORDERDETAIL_TO_BUY_CARD) {
                    SuccessClickSendBroadCast("android.intent.action.UpdateOrderConfirmActivity");
                } else if (previous == Global.CARDSDETAIL_NEW_TO_BUT_CARD) {
                    SuccessClickSendBroadCast("android.intent.action.AppointMentNewActivity");
                }
                finishWithAnimation();
                break;
            case R.id.button_back_old:
                Utils.mLogError("==-->流程购买次卡  " + getClass().getName() + " --- 222 --- " + previous_liucheng);
                if (previous_liucheng > 0 && previous_liucheng == Global.ORDERDETAIL_TO_BUY_CARD) {
                    SuccessClickSendBroadCast("android.intent.action.OrderDetailActivity");
                } else if (previous_liucheng > 0 && previous_liucheng == Global.UPDATEORDERDETAIL_TO_BUY_CARD) {
                    SuccessClickSendBroadCast("android.intent.action.UpdateOrderConfirmActivity");
                } else if (previous == Global.CARDSDETAIL_NEW_TO_BUT_CARD) {
                    SuccessClickSendBroadCast("android.intent.action.AppointMentNewActivity");
                }
                finishWithAnimation();
                break;
            case R.id.button_look_cards:
                break;
            case R.id.button_back_main:
                sendToMian();
                finish();
                break;
            case R.id.button_ddxq:
                if (previous != Global.MIPCA_TO_ORDERPAY) {
                    if (previous != Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
                        sendToMian();
                    }
                    Intent intent1 = null;
                    if (type == 4 || previous == Global.MAIN_TO_TRAIN_DETAIL) {// 从训练支付成功的进入复用寄养详情
                        intent1 = new Intent(mContext, FosterOrderDetailNewActivity.class);
                        intent1.putExtra("orderid", orderId);
                        intent1.putExtra("type", type);
                        startActivity(intent1);
                        finishWithAnimation();
                    } else if (previous == Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE) {
                        finishWithAnimation();
                    } else if (previous == Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY) {
                        sendToMyCardFragment();
                    } else {// 其他的复用洗美详情
                        intent1 = new Intent(mContext, OrderDetailFromOrderToConfirmActivity.class);
                        intent1.putExtra("orderid", orderId);
                        intent1.putExtra("type", type);
                        startActivity(intent1);
                        finishWithAnimation();
                    }
                }
                break;
            case R.id.button_wsxx:
                startActivity(new Intent(this, PetAddActivity.class).putExtra(
                        "customerpetid", petCustomerId).putExtra("previous",
                        Global.PETINFO_TO_EDITPET));
                break;
            case R.id.button_to_myfragment:
                sendToMyFragment();
                break;
            case R.id.textview_dummp_money_can_do://罐头币作用
                goNext(MyCanActivity.class);
                break;
            case R.id.textview_isvip://成为vip
                if (Utils.checkLogin(mContext)) {
                    goNext(MemberUpgradeActivity.class);
                } else {
                    goNext(LoginNewActivity.class);
                }
                break;
            case R.id.button_look_order_detail:
                if (type == 20) {
                    Intent intentNext = new Intent(mContext, ShopMallOrderDetailActivity.class);
                    intentNext.putExtra("orderId", orderId);
                    startActivity(intentNext);
                } else if (type == 2) {
                    Intent intentNext = new Intent(mContext, FosterOrderDetailNewActivity.class);
                    intentNext.putExtra("type", type);
                    intentNext.putExtra("orderid", orderId);
                    startActivity(intentNext);
                } else if (type == 3) {
                    Intent intentNext = new Intent(mContext, OrderDetailFromOrderToConfirmActivity.class);
                    intentNext.putExtra("orderid", orderId);
                    intentNext.putExtra("type", type);
                    startActivity(intentNext);
                } else {
                    Intent intentNext = new Intent(mContext, WashOrderDetailActivity.class);
                    intentNext.putExtra("orderid", orderId);
                    intentNext.putExtra("type", type);
                    startActivity(intentNext);
                }
                finishWithAnimation();
                break;
            case R.id.button_to_recharge:
                break;
        }
    }

    private void goNext(Class cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
        finishWithAnimation();
    }

    private void SuccessClickSendBroadCast(String str) {
        try {
            if (ADActivity.act != null) {
                ADActivity.act.finish();
            }
            if (OrderPayActivity.act != null) {
                OrderPayActivity.act.finish();
            }
            if (ChoosePetActivityNew.act != null) {
                ChoosePetActivityNew.act.finish();
            }
            if (ServiceNewActivity.act != null) {
                ServiceNewActivity.act.finish();
            }
            sendToBroad(str, previous_liucheng, previous);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
