package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CanEarningGuideAdapter;
import com.haotang.pet.adapter.CanExchangeAdapter;
import com.haotang.pet.adapter.MyCanVerticBannerAdapter;
import com.haotang.pet.entity.CanEarningGuide;
import com.haotang.pet.entity.CanExchange;
import com.haotang.pet.entity.MyCanVerticBanner;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.verticalbanner.VerticalBannerView;
import com.haotang.pet.view.MListview;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的罐头币界面
 */
public class MyCanActivity extends SuperActivity implements
        View.OnClickListener {
    private MProgressDialog pDialog;
    private ImageButton ibBack;
    private TextView tvTitle;
    private TextView tv_mycan_gtzs;
    private TextView tv_mycan_dqksy;
    private TextView tv_mycan_jjdz;
    private RelativeLayout rl_mycan_wenhao;
    private ArrayList<MyCanVerticBanner> myCanVerticBannerList = new ArrayList<MyCanVerticBanner>();
    private ArrayList<CanExchange> canExchangeList = new ArrayList<CanExchange>();
    private ArrayList<CanEarningGuide> canEarningGuideList = new ArrayList<CanEarningGuide>();
    private int page = 1;
    private CanExchangeAdapter canExchangeAdapter;
    private RelativeLayout rl_mycan_verticalbanner;
    private VerticalBannerView vbv_mycan_verticalbanner;
    private MListview mlv_mycan_dhzx;
    private MListview mlv_mycan_zqzn;
    private MyCanVerticBannerAdapter myCanVerticBannerAdapter;
    private CanEarningGuideAdapter canEarningGuideAdapter;
    private SharedPreferenceUtil spUtil;
    private TextView tv_titlebar_other;
    private TextView tv_mycan_smsgt;
    private String textAddrUrl;
    private String toArriveAddrUrl;
    private LinearLayout ll_mycan_smsgt;
    private int availableNum;
    private int toArriveNum;
    private RelativeLayout rl_mycan_wdl;
    private ImageView iv_mycan_wdl;
    private LinearLayout ll_mycan_ydl;
    private ImageView iv_mycan_gticon;
    private TextView tv_mycan_gtdhzx;
    private TextView tv_mycan_gtzqzn;
    private LinearLayout ll_mycan_gtzqzn;
    private View vw_mycan_dhzx;
    private View vw_mycan_zqzn;
    private ScrollView osv_mycan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void init() {
        pDialog = new MProgressDialog(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
    }

    private void findView() {
        setContentView(R.layout.activity_my_can);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        tv_mycan_gtzs = (TextView) findViewById(R.id.tv_mycan_gtzs);
        tv_mycan_dqksy = (TextView) findViewById(R.id.tv_mycan_dqksy);
        tv_mycan_jjdz = (TextView) findViewById(R.id.tv_mycan_jjdz);
        rl_mycan_wenhao = (RelativeLayout) findViewById(R.id.rl_mycan_wenhao);
        rl_mycan_verticalbanner = (RelativeLayout) findViewById(R.id.rl_mycan_verticalbanner);
        vbv_mycan_verticalbanner = (VerticalBannerView) findViewById(R.id.vbv_mycan_verticalbanner);
        mlv_mycan_dhzx = (MListview) findViewById(R.id.mlv_mycan_dhzx);
        mlv_mycan_zqzn = (MListview) findViewById(R.id.mlv_mycan_zqzn);
        tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
        tv_mycan_smsgt = (TextView) findViewById(R.id.tv_mycan_smsgt);
        ll_mycan_smsgt = (LinearLayout) findViewById(R.id.ll_mycan_smsgt);
        rl_mycan_wdl = (RelativeLayout) findViewById(R.id.rl_mycan_wdl);
        iv_mycan_wdl = (ImageView) findViewById(R.id.iv_mycan_wdl);
        ll_mycan_ydl = (LinearLayout) findViewById(R.id.ll_mycan_ydl);
        iv_mycan_gticon = (ImageView) findViewById(R.id.iv_mycan_gticon);
        tv_mycan_gtdhzx = (TextView) findViewById(R.id.tv_mycan_gtdhzx);
        tv_mycan_gtzqzn = (TextView) findViewById(R.id.tv_mycan_gtzqzn);
        ll_mycan_gtzqzn = (LinearLayout) findViewById(R.id.ll_mycan_gtzqzn);
        vw_mycan_dhzx = (View) findViewById(R.id.vw_mycan_dhzx);
        vw_mycan_zqzn = (View) findViewById(R.id.vw_mycan_zqzn);
        osv_mycan = (ScrollView) findViewById(R.id.osv_mycan);
    }

    private void setView() {
        if (Utils.checkLogin(this)) {
            rl_mycan_wdl.setVisibility(View.GONE);
            ll_mycan_ydl.setVisibility(View.VISIBLE);
        } else {
            rl_mycan_wdl.setVisibility(View.VISIBLE);
            ll_mycan_ydl.setVisibility(View.GONE);
        }
        tv_titlebar_other.setVisibility(View.VISIBLE);
        tv_titlebar_other.setTextColor(getResources().getColor(R.color.white));
        tv_titlebar_other.setText("收支明细");
        tvTitle.setText("我的罐头币");
        myCanVerticBannerList.clear();
        canExchangeList.clear();
        canEarningGuideList.clear();
        canExchangeAdapter = new CanExchangeAdapter(this, canExchangeList);
        canExchangeAdapter.clearDeviceList();
        mlv_mycan_dhzx.setAdapter(canExchangeAdapter);
        canEarningGuideAdapter = new CanEarningGuideAdapter(this, canEarningGuideList);
        canEarningGuideAdapter.clearDeviceList();
        mlv_mycan_zqzn.setAdapter(canEarningGuideAdapter);
    }

    private void setLinster() {
        iv_mycan_wdl.setOnClickListener(this);
        ll_mycan_smsgt.setOnClickListener(this);
        tv_titlebar_other.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        rl_mycan_wenhao.setOnClickListener(this);
        canExchangeAdapter.setOnItemClickListener(new CanExchangeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(CanExchange data) {
                Utils.goService(MyCanActivity.this, data.point, data.backup);
            }
        });
        canEarningGuideAdapter.setOnItemClickListener(new CanEarningGuideAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(CanEarningGuide data) {
                Utils.goService(MyCanActivity.this, data.point, data.backup);
            }
        });
    }

    private void getData() {
        pDialog.showDialog();
        CommUtil.canBillPage(mContext, canBillPageHandler);
    }

    private AsyncHttpResponseHandler canBillPageHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                JSONObject jdata = jobj.getJSONObject("data");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jdata != null) {
                        if (jdata.has("canBillIcon") && !jdata.isNull("canBillIcon")) {
                            String canBillIcon = jdata.getString("canBillIcon");
                            GlideUtil.loadCircleImg(MyCanActivity.this, canBillIcon,
                                    iv_mycan_gticon, R.drawable.user_icon_unnet_circle);
                        }
                        if (jdata.has("earnText") && !jdata.isNull("earnText")) {
                            String earnText = jdata.getString("earnText");
                            Utils.setText(tv_mycan_gtzqzn, earnText, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("payText") && !jdata.isNull("payText")) {
                            String payText = jdata.getString("payText");
                            Utils.setText(tv_mycan_gtdhzx, payText, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("text1") && !jdata.isNull("text1")) {
                            String text1 = jdata.getString("text1");
                            Utils.setText(tv_mycan_smsgt, text1, "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("textAddr") && !jdata.isNull("textAddr")) {
                            textAddrUrl = jdata.getString("textAddr");
                        }
                        if (jdata.has("canBillDetail") && !jdata.isNull("canBillDetail")) {
                            JSONObject jbcanBillDetail = jdata.getJSONObject("canBillDetail");
                            if (jbcanBillDetail.has("availableNum") && !jbcanBillDetail.isNull("availableNum")) {
                                availableNum = jbcanBillDetail.getInt("availableNum");
                                String text = "当前可使用：" + availableNum;
                                SpannableString ss = new SpannableString(text);
                                if (availableNum < 10000) {
                                    ss.setSpan(new TextAppearanceSpan(MyCanActivity.this, R.style.foster_style_y), 0,
                                            ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                } else if (availableNum >= 10000 && availableNum < 100000) {
                                    ss.setSpan(new TextAppearanceSpan(MyCanActivity.this, R.style.styleorder), 0,
                                            ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                } else {
                                    ss.setSpan(new TextAppearanceSpan(MyCanActivity.this, R.style.tensp), 0,
                                            ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                }
                                tv_mycan_dqksy.setText(ss);
                            }
                            if (jbcanBillDetail.has("toArriveNum") && !jbcanBillDetail.isNull("toArriveNum")) {
                                toArriveNum = jbcanBillDetail.getInt("toArriveNum");
                                Utils.setText(tv_mycan_jjdz, "即将到账：" + toArriveNum, "即将到账：0", View.VISIBLE, View.VISIBLE);
                            }
                            if (jbcanBillDetail.has("toArriveAddr") && !jbcanBillDetail.isNull("toArriveAddr")) {
                                toArriveAddrUrl = jbcanBillDetail.getString("toArriveAddr");
                            }
                            Utils.setText(tv_mycan_gtzs, (availableNum + toArriveNum) + "", "0", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("canBillDynamic") && !jdata.isNull("canBillDynamic")) {
                            JSONArray jacanBillDynamic = jdata.getJSONArray("canBillDynamic");
                            if (jacanBillDynamic != null && jacanBillDynamic.length() > 0) {
                                rl_mycan_verticalbanner.setVisibility(View.VISIBLE);
                                myCanVerticBannerList.clear();
                                for (int i = 0; i < jacanBillDynamic.length(); i++) {
                                    myCanVerticBannerList.add(MyCanVerticBanner
                                            .json2Entity(jacanBillDynamic
                                                    .getJSONObject(i)));
                                }
                                try {
                                    myCanVerticBannerAdapter = new MyCanVerticBannerAdapter(myCanVerticBannerList);
                                    vbv_mycan_verticalbanner
                                            .setAdapter(myCanVerticBannerAdapter);
                                    vbv_mycan_verticalbanner.start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                rl_mycan_verticalbanner.setVisibility(View.GONE);
                            }
                        }
                        if (jdata.has("payCanBillCenter") && !jdata.isNull("payCanBillCenter")) {
                            JSONArray japayCanBillCenter = jdata.getJSONArray("payCanBillCenter");
                            if (japayCanBillCenter != null && japayCanBillCenter.length() > 0) {
                                ll_mycan_smsgt.setVisibility(View.VISIBLE);
                                vw_mycan_dhzx.setVisibility(View.VISIBLE);
                                mlv_mycan_dhzx.setVisibility(View.VISIBLE);
                                canExchangeList.clear();
                                canExchangeAdapter.clearDeviceList();
                                for (int i = 0; i < japayCanBillCenter.length(); i++) {
                                    canExchangeList.add(CanExchange
                                            .json2Entity(japayCanBillCenter
                                                    .getJSONObject(i)));
                                }
                                canExchangeAdapter.setData(canExchangeList);
                            } else {
                                ll_mycan_smsgt.setVisibility(View.GONE);
                                vw_mycan_dhzx.setVisibility(View.GONE);
                                mlv_mycan_dhzx.setVisibility(View.GONE);
                            }
                        }
                        if (jdata.has("earnCanBillCenter") && !jdata.isNull("earnCanBillCenter")) {
                            JSONArray jaearnCanBillCenter = jdata.getJSONArray("earnCanBillCenter");
                            if (jaearnCanBillCenter != null && jaearnCanBillCenter.length() > 0) {
                                ll_mycan_gtzqzn.setVisibility(View.VISIBLE);
                                vw_mycan_zqzn.setVisibility(View.VISIBLE);
                                mlv_mycan_zqzn.setVisibility(View.VISIBLE);
                                canEarningGuideList.clear();
                                canEarningGuideAdapter.clearDeviceList();
                                for (int i = 0; i < jaearnCanBillCenter.length(); i++) {
                                    canEarningGuideList.add(CanEarningGuide
                                            .json2Entity(jaearnCanBillCenter
                                                    .getJSONObject(i)));
                                }
                                canEarningGuideAdapter.setData(canEarningGuideList);
                            } else {
                                ll_mycan_gtzqzn.setVisibility(View.GONE);
                                vw_mycan_zqzn.setVisibility(View.GONE);
                                mlv_mycan_zqzn.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(MyCanActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(MyCanActivity.this, "数据异常");
            }
            // 解决自动滚动问题
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    osv_mycan.fullScroll(ScrollView.FOCUS_UP);
                }
            }, 5);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShortBottom(MyCanActivity.this, "请求失败");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.MYCAN_TO_LOGIN) {
                if (Utils.checkLogin(this)) {
                    rl_mycan_wdl.setVisibility(View.GONE);
                    ll_mycan_ydl.setVisibility(View.VISIBLE);
                } else {
                    rl_mycan_wdl.setVisibility(View.VISIBLE);
                    ll_mycan_ydl.setVisibility(View.GONE);
                }
                getData();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_other:
                if (Utils.checkLogin(this)) {
                    startActivity(new Intent(this, TransactionDetailsActivity.class));
                }
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.rl_mycan_wenhao:
                if (Utils.isStrNull(toArriveAddrUrl)) {
                    startActivity(new Intent(this, ADActivity.class).putExtra("url", toArriveAddrUrl));
                }
                break;
            case R.id.ll_mycan_smsgt:
                if (Utils.isStrNull(textAddrUrl)) {
                    startActivity(new Intent(this, ADActivity.class).putExtra("url", toArriveAddrUrl));
                }
                break;
            case R.id.iv_mycan_wdl:
                startActivityForResult(new Intent(this, LoginNewActivity.class), Global.MYCAN_TO_LOGIN);
                break;
            default:
                break;
        }
    }
}
