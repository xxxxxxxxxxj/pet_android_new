package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.PaySuccessAdapter;
import com.haotang.pet.adapter.PaysuccessDiscountInfoAdapter;
import com.haotang.pet.entity.ActivityPage;
import com.haotang.pet.entity.mallEntity.MallCommodity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.OrderType;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * 洗美支付成功页面
 * Created by Administrator on 2017/12/23 0023.
 */

public class PaySuccessNewActivity extends SuperActivity implements View.OnClickListener {
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title, tv_titlebar_other, textview_mall_num;
    private TextView textview_dummp_money_num, textview_show_dogs;
    private MyGridView prl_mall_to_grid;
    private PaySuccessAdapter paySuccessAdapter;
    private ArrayList<MallCommodity> mallGoods = new ArrayList<>();
    private int myselfDummpNum = -1;
    private int orderId;
    private int type;
    private int pageType;
    private TextView textview_look_order;
    private List<ActivityPage> localBannerList = new ArrayList<ActivityPage>();
    //文件名称
    String fileName_one = "ad_paysuccess_one.txt";
    String fileName_one_day = "ad_paysuccess_one_day.txt";
    private RelativeLayout rl_paysuccess_discountinfo;
    private RecyclerView rv_paysuccess_discountinfo;
    private ArrayList<String> discountList;
    private double payPrice;
    private double cardFaceValue;
    private LinearLayout ll_paysuccess_payprice;
    private TextView tv_paysuccess_payprice;
    private TextView tv_paysuccess_cardfacevalue;
    private String cardPwd;
    private RelativeLayout rl_paysuccessnew_fx;
    private TextView tv_paysuccessnew_fx;
    private TextView tv_paysuccessnew_ckfx;
    private double fx_price;
    private int cardId;
    private List<Integer> toothCardIds;
    private TextView textview_bkts;
    private String cardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paysuccess_new);
        initData();
        initView();
        setView();
        giveCouponNew();
        getRecommendData();
        getMainActivity();
    }

    private void initData() {
        mallGoods.clear();
        discountList = getIntent().getStringArrayListExtra("discountList");
        spUtil = SharedPreferenceUtil.getInstance(this);
        orderId = getIntent().getIntExtra("orderId", 0);
        type = getIntent().getIntExtra("type", 0);
        pageType = getIntent().getIntExtra("pageType", 1);
        myselfDummpNum = getIntent().getIntExtra("myself", -1);
        cardFaceValue = getIntent().getDoubleExtra("cardFaceValue", -1);
        payPrice = getIntent().getDoubleExtra("payPrice", -1);
        fx_price = getIntent().getDoubleExtra("fx_price", -1);
        cardPwd = getIntent().getStringExtra("cardPwd");
        cardId = getIntent().getIntExtra("cardId", 0);
        toothCardIds = (List<Integer>) getIntent().getSerializableExtra("cardIds");
        cardName = getIntent().getStringExtra("cardName");
        if (type == 21 && cardId > 0) {
            orderId = cardId;
        }
    }

    private void initView() {
        rl_paysuccessnew_fx = (RelativeLayout) findViewById(R.id.rl_paysuccessnew_fx);
        textview_mall_num = (TextView) findViewById(R.id.textview_mall_num);
        textview_bkts = (TextView) findViewById(R.id.textview_bkts);
        tv_paysuccessnew_fx = (TextView) findViewById(R.id.tv_paysuccessnew_fx);
        tv_paysuccessnew_ckfx = (TextView) findViewById(R.id.tv_paysuccessnew_ckfx);
        rl_paysuccess_discountinfo = (RelativeLayout) findViewById(R.id.rl_paysuccess_discountinfo);
        ll_paysuccess_payprice = (LinearLayout) findViewById(R.id.ll_paysuccess_payprice);
        tv_paysuccess_payprice = (TextView) findViewById(R.id.tv_paysuccess_payprice);
        tv_paysuccess_cardfacevalue = (TextView) findViewById(R.id.tv_paysuccess_cardfacevalue);
        rv_paysuccess_discountinfo = (RecyclerView) findViewById(R.id.rv_paysuccess_discountinfo);
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
        textview_dummp_money_num = (TextView) findViewById(R.id.textview_dummp_money_num);
        textview_show_dogs = (TextView) findViewById(R.id.textview_show_dogs);
        prl_mall_to_grid = (MyGridView) findViewById(R.id.prl_mall_to_grid);
        textview_look_order = (TextView) findViewById(R.id.textview_look_order);
        ib_titlebar_back.setOnClickListener(this);
        tv_titlebar_other.setOnClickListener(this);
        textview_look_order.setOnClickListener(this);
        tv_paysuccessnew_ckfx.setOnClickListener(this);
    }

    private void setView() {
        ll_paysuccess_payprice.setVisibility(View.VISIBLE);
        tv_paysuccess_payprice.setText("" + Utils.formatDecimal(payPrice));
        if (type == 1) {//洗美，特色服务
            textview_mall_num.setVisibility(View.GONE);
            textview_bkts.setVisibility(View.GONE);
            if (myselfDummpNum > 0) {
                textview_dummp_money_num.setVisibility(View.VISIBLE);
            } else {
                textview_dummp_money_num.setVisibility(View.GONE);
            }
            textview_dummp_money_num.setText("服务完成后可获得" + myselfDummpNum + "个罐头币");
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
            if (fx_price > 0) {
                tv_paysuccessnew_fx.setText("" + Utils.formatDecimal(fx_price));
                rl_paysuccessnew_fx.setVisibility(View.VISIBLE);
            } else {
                rl_paysuccessnew_fx.setVisibility(View.GONE);
            }
            tv_paysuccess_cardfacevalue.setVisibility(View.GONE);
        } else if (type == 20) {//商城
            textview_bkts.setVisibility(View.GONE);
            textview_dummp_money_num.setVisibility(View.GONE);
            if (myselfDummpNum > 0) {
                textview_mall_num.setVisibility(View.VISIBLE);
            } else {
                textview_mall_num.setVisibility(View.GONE);
            }
            textview_mall_num.setText("该订单获得" + myselfDummpNum + "个罐头币奖励～");
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
            tv_paysuccess_cardfacevalue.setVisibility(View.GONE);
            rl_paysuccessnew_fx.setVisibility(View.GONE);
        } else if (type == 21) {//购买E卡
            textview_bkts.setVisibility(View.GONE);
            textview_dummp_money_num.setVisibility(View.GONE);
            if (myselfDummpNum > 0) {
                textview_mall_num.setVisibility(View.VISIBLE);
            } else {
                textview_mall_num.setVisibility(View.GONE);
            }
            textview_mall_num.setText("该订单获得" + myselfDummpNum + "个罐头币奖励～");
            rl_paysuccess_discountinfo.setVisibility(View.GONE);
            rl_paysuccessnew_fx.setVisibility(View.GONE);
            tv_paysuccess_cardfacevalue.setVisibility(View.VISIBLE);
            tv_paysuccess_cardfacevalue.setText("E卡面值¥" + Utils.formatDecimal(cardFaceValue));
            new AlertDialogNavAndPost(PaySuccessNewActivity.this).builder()
                    .setTitle("绑定E卡")
                    .setSubTitle("是否立即绑定E卡到当前账号" + spUtil.getString("cellphone", "") + "?")
                    .setMsg("绑定成功后不可解绑")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).setPositiveButton("立即绑定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bindCard();
                }
            }).show();
        } else if (type == 2) {//寄养
            textview_bkts.setVisibility(View.GONE);
            textview_mall_num.setVisibility(View.GONE);
            textview_dummp_money_num.setVisibility(View.GONE);
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
            rl_paysuccessnew_fx.setVisibility(View.GONE);
            tv_paysuccess_cardfacevalue.setVisibility(View.GONE);
        } else if (type == 22) {//购买刷牙卡
            textview_bkts.setVisibility(View.VISIBLE);
            textview_mall_num.setVisibility(View.GONE);
            if (myselfDummpNum > 0) {
                textview_dummp_money_num.setVisibility(View.VISIBLE);
            } else {
                textview_dummp_money_num.setVisibility(View.GONE);
            }
            textview_dummp_money_num.setText("获得" + myselfDummpNum + "个罐头币");
            rl_paysuccess_discountinfo.setVisibility(View.GONE);
            rl_paysuccessnew_fx.setVisibility(View.GONE);
            tv_paysuccess_cardfacevalue.setVisibility(View.GONE);
            new AlertDialogDefault(mContext).builder()
                    .setTitle("绑定"+cardName).setMsg("绑定成功后不可解绑").isOneBtn(true).setCloseVisible(View.VISIBLE).setPositiveButton("立即绑卡", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, ChooseFosterPetActivity.class).putExtra("cardIds", (Serializable) toothCardIds).putExtra("previous", Global.BUYCARD_TO_CHOOSEPET));
                    finish();
                }
            }).show();
        }
        tv_titlebar_other.setVisibility(View.VISIBLE);
        tv_titlebar_other.setText("完成");
        tv_titlebar_other.setTextColor(Color.parseColor("#FFFFFF"));
        tv_titlebar_title.setText("支付成功");
        paySuccessAdapter = new PaySuccessAdapter(mContext, mallGoods);
        prl_mall_to_grid.setAdapter(paySuccessAdapter);
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

    private void giveCouponNew() {
        mPDialog.showDialog();
        CommUtil.giveCouponNew(this, orderId, giveCouponHandler);
    }

    private AsyncHttpResponseHandler giveCouponHandler = new AsyncHttpResponseHandler() {

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
                        if (jData.has("pic") && !jData.isNull("pic")) {
                            String imgUrl = jData.getString("pic");
                            startActivity(new Intent(mContext, AppointCouponActivity.class).putExtra("imgUrl", imgUrl));
                        }
                    }
                } else {
                }
            } catch (Exception e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void getMainActivity() {
        int activityPage = 0;
        if (type == 20) {//商城
            activityPage = 5;
        } else if (type == 1) {//洗美特色服务
            activityPage = 4;
        } else {//其他
            activityPage = 2;
        }
        localBannerList.clear();
        CommUtil.getActivityPage(mContext, spUtil.getInt("nowShopCityId",0), spUtil.getInt("isFirstLogin", 0), activityPage,Double.parseDouble(spUtil.getString("lat_home","0")),Double.parseDouble(spUtil.getString("lng_home","0")), handlerHomeActivity);
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
                            String tag_paysuccess_one = Utils.readFileData(PaySuccessNewActivity.this, fileName_one);
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
                                        Utils.writeFileData(PaySuccessNewActivity.this, fileName_one, id + ",");
                                    }
                                } else {
                                    Utils.writeFileData(PaySuccessNewActivity.this, fileName_one, id + ",");
                                }
                            } else {
                                Utils.writeFileData(PaySuccessNewActivity.this, fileName_one, id + ",");
                            }
                        } else if (activityPage.getCountType() == 1) {//每次都显示

                        } else if (activityPage.getCountType() == 2) {//每日一次
                            Calendar c = Calendar.getInstance();
                            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            int mDay = c.get(Calendar.DAY_OF_MONTH);
                            String tag_paysuccess_one_day = Utils.readFileData(PaySuccessNewActivity.this, fileName_one_day);
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
                                        Utils.writeFileData(PaySuccessNewActivity.this, fileName_one_day, id + "_" + mDay + ",");
                                    }
                                } else {
                                    Utils.writeFileData(PaySuccessNewActivity.this, fileName_one_day, id + "_" + mDay + ",");
                                }
                            } else {
                                Utils.writeFileData(PaySuccessNewActivity.this, fileName_one_day, id + "_" + mDay + ",");
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
                Utils.ActivityPage(PaySuccessNewActivity.this, localBannerList,3);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void getRecommendData() {
        mPDialog.showDialog();
        CommUtil.orderPaySuccessCommodityInfo(mContext, pageType, orderId, getrRecommentDataHandler);
    }

    private AsyncHttpResponseHandler getrRecommentDataHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("mallCommodity") && !jdata.isNull("mallCommodity")) {
                            JSONArray jarrcommodityList = jdata.getJSONArray("mallCommodity");
                            if (jarrcommodityList.length() > 0) {
                                for (int i = 0; i < jarrcommodityList.length(); i++) {
                                    mallGoods.add(MallCommodity.j2Entity(jarrcommodityList.getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("title") && !jdata.isNull("title")) {
                            textview_show_dogs.setText(jdata.getString("title"));
                        }
                    }
                }
                if (mallGoods.size() > 0) {
                    paySuccessAdapter.notifyDataSetChanged();
                } else {
                    paySuccessAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();

        }
    };

    private void bindCard() {
        mPDialog.showDialog();
        CommUtil.bindCard(cardPwd, 1,
                PaySuccessNewActivity.this, bindCardHanler);
    }

    private AsyncHttpResponseHandler bindCardHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    new AlertDialogNavAndPost(PaySuccessNewActivity.this).builder().setTitle("").setMsg("绑定成功")
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).setPositiveButton("查看卡包", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mContext, MyCardActivity.class));
                            finish();
                        }
                    }).show();
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(PaySuccessNewActivity.this,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
                ToastUtil.showToastShort(PaySuccessNewActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(PaySuccessNewActivity.this, "请求失败");
        }
    };

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_paysuccessnew_ckfx:
                startActivity(new Intent(mContext, CashbackAmountActivity.class));
                finish();
                break;
            case R.id.ib_titlebar_back:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.mainactivity");
                if (type == 20) {
                    intent1.putExtra("previous",
                            Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
                } else {
                    intent1.putExtra("previous",
                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                }
                sendBroadcast(intent1);
                finishWithAnimation();
                break;
            case R.id.tv_titlebar_other:
                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.mainactivity");
                if (type == 20) {
                    intent2.putExtra("previous",
                            Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
                } else {
                    intent2.putExtra("previous",
                            Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY);
                }
                sendBroadcast(intent2);
                finishWithAnimation();
                break;
            case R.id.textview_look_order:
                if (type == OrderType.SHOP.getType()) {//商城
                    Intent intentNext = new Intent(mContext, ShopMallOrderDetailActivity.class);
                    intentNext.putExtra("orderId", orderId);
                    startActivity(intentNext);
                } else if (type == OrderType.GIFT_CARD.getType()) {//礼品卡 E卡
                    Intent intentNext = new Intent(mContext, CardOrderDetailActivity.class);
                    intentNext.putExtra("serviceCardId", orderId);
                    startActivity(intentNext);
                } else if (type == OrderType.WASH.getType()) {//洗美，特色服务
                    Intent intentNext = new Intent(mContext, WashOrderDetailActivity.class);
                    intentNext.putExtra("orderid", orderId);
                    startActivity(intentNext);
                } else if (type == OrderType.FOSTER.getType()) {//寄养
                    Intent intentNext = new Intent(mContext, FosterOrderDetailNewActivity.class);
                    intentNext.putExtra("orderid", orderId);
                    startActivity(intentNext);
                } else if (type == OrderType.BRUSH_THEETH.getType()) {//刷牙卡 EK
                    Intent intentNext = new Intent(mContext, GiftCardOrderActivity.class);
                    startActivity(intentNext);
                }
                finishWithAnimation();
                break;
        }
    }
}
