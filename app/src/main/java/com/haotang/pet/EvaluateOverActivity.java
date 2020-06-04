package com.haotang.pet;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.GratuityPriceAdapter;
import com.haotang.pet.adapter.PaySuccessAdapter;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.WorkerInfo;
import com.haotang.pet.entity.mallEntity.MallCommodity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.MyGridView;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EvaluateOverActivity extends SuperActivity {
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.textview_top)
    TextView textviewTop;
    @BindView(R.id.textview_middle)
    TextView textviewMiddle;
    @BindView(R.id.textview_get_dummy_nums)
    TextView textviewGetDummyNums;
    @BindView(R.id.textview_go_look)
    TextView textviewGoLook;
    @BindView(R.id.mygridview_shows)
    MyGridView mygridviewShows;
    @BindView(R.id.iv_beautician_head)
    ImageView ivBeauticianHead;
    @BindView(R.id.tv_beautician_name)
    TextView tvBeauticianName;
    @BindView(R.id.tv_gratuity_content)
    TextView tvGratuityContent;
    @BindView(R.id.rl_gratuity)
    RelativeLayout rl_gratuity;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    private String commentGradeCopy;
    private String evaluate;
    private int pageType = 1;
    private int serviceType = 1;
    private int orderId;
    private ArrayList<MallCommodity> mallGoods = new ArrayList<>();
    private PaySuccessAdapter paySuccessAdapter;
    private boolean isMarketDialog;
    private boolean isGratuityOpen;
    private int beauEvaNums;
    private String gratuityContent;
    private String remark;
    private int workerId;
    private String shopName;
    private String beautician_name;
    private String levelname;
    private String beautician_iamge;
    private int paytype;
    private GratuityPriceAdapter priceAdapter;
    private List<WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean> priceList = new ArrayList<>();
    private String gratuity_content;
    private double payPrice = 0;
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private String workerImg;
    private String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_over_activity);
        ButterKnife.bind(this);
        Global.WXPAYCODE = -1;
        getIntentData();
        getRecommendData();
        if (isGratuityOpen) {
            getWorkerInfo(workerId);
        }
        setView();
    }

    private void setView() {
        tvTitlebarTitle.setText("评价结束");
        paySuccessAdapter = new PaySuccessAdapter(mContext, mallGoods);
        mygridviewShows.setAdapter(paySuccessAdapter);
        if (isGratuityOpen && beauEvaNums == 5) {
            rl_gratuity.setVisibility(View.VISIBLE);
        } else {
            rl_gratuity.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(commentGradeCopy)) {
            textviewTop.setText(commentGradeCopy);
            textviewMiddle.setVisibility(View.GONE);
        }
        if (isMarketDialog) {
            try {
                if (!spUtil.getBoolean("GOTOMARKET_DIALOG_TRUE", false)) {//先判断是否点击跳转到应用市场
                    //再判断是否点击暂时没有
                    if (spUtil.getBoolean("GOTOMARKET_DIALOG_FALSE", false)) {
                        //再判断距离上一次点击暂时没有是否大于10天且判断打开次数是否是5次
                        String gotomarket_dialog_time = spUtil.getString("GOTOMARKET_DIALOG_TIME", "");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String format = df.format(new Date());// new Date()为获取当前系统时间
                        if (Utils.daysBetween(gotomarket_dialog_time, format) >= 10) {
                            //弹出
                            Utils.goMarketDialog(EvaluateOverActivity.this);
                        }
                    } else {
                        //弹出
                        Utils.goMarketDialog(EvaluateOverActivity.this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getIntentData() {
        isMarketDialog = getIntent().getBooleanExtra("isMarketDialog", false);
        commentGradeCopy = getIntent().getStringExtra("commentGradeCopy");
        evaluate = getIntent().getStringExtra("evaluate");
        orderId = getIntent().getIntExtra("orderId", 0);
        serviceType = getIntent().getIntExtra("serviceType", 0);
        isGratuityOpen = getIntent().getBooleanExtra("isGratuityOpen", true);
        gratuityContent = getIntent().getStringExtra("gratuityContent");
        workerId = getIntent().getIntExtra("workerId", 0);
        beauEvaNums = getIntent().getIntExtra("beauEvaNums", 0);
        textviewGetDummyNums.setText(evaluate);
        if (serviceType == 1 || serviceType == 2) {
            pageType = 1;
        } else if (serviceType == 3) {
            pageType = 2;
        }
    }

    private void getRecommendData() {
        mPDialog.showDialog();
        CommUtil.orderPaySuccessCommodityInfo(mContext, pageType, orderId, getrRecommentDataHandler);
    }

    private AsyncHttpResponseHandler getrRecommentDataHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//            prl_mall_to_grid.onRefreshComplete();
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

    private void getWorkerInfo(int workerId) {
        CommUtil.workerInfo(EvaluateOverActivity.this, workerId, workerInfoHandler);
    }

    private AsyncHttpResponseHandler workerInfoHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultcode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultcode == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        Gson gson = new Gson();
                        WorkerInfo workerInfo = gson.fromJson(new String(responseBody), WorkerInfo.class);
                        WorkerInfo.DataBean workerInfoData = workerInfo.getData();
                        shopName = workerInfoData.getShopName();
                        beautician_name = workerInfoData.getRealName();
                        levelname = workerInfoData.getLevel().getName();
                        beautician_iamge = workerInfoData.getAvatar();
                        GlideUtil.loadCircleImg(mContext, beautician_iamge, ivBeauticianHead, R.drawable.icon_default);
                        tvGratuityContent.setText(workerInfoData.getGratuityInfo().getContent_2());
                        tvBeauticianName.setText(beautician_name);
                        if (!workerInfoData.getGratuityInfo().isIsGratuityOpen()) {
                            rl_gratuity.setVisibility(View.GONE);
                        }
                        priceList.clear();
                        List<WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean> gratuityInfoList = workerInfoData.getGratuityInfo().getGratuityInfo();
                        priceList.addAll(gratuityInfoList);
                        gratuity_content = workerInfoData.getGratuityInfo().getContent_1();
                    }
                } else {
                    ToastUtil.showToastShortBottom(EvaluateOverActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private PopupWindow pWinBottomDialog;
    private Button btn_pay_bottomdia;

    private void showPayDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(EvaluateOverActivity.this, R.layout.appoint_pay_bottom_dialog, null);
        btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        customView.findViewById(R.id.ll_pay_bottomdia_time).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_minute).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_second).setVisibility(View.GONE);
        customView.findViewById(R.id.rl_pay_bottomdia_yqm).setVisibility(View.GONE);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        customView.findViewById(R.id.iv_pay_bottomdia_yqm_select).setVisibility(View.GONE);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(EvaluateOverActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        btn_pay_bottomdia.setText("去支付" + payPrice + "元");
        tv_pay_title.setText("请选择支付方式");
        if (spUtil.getInt("payway", 0) == 1) {
            paytype = 1;
            iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
            iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
        } else if (spUtil.getInt("payway", 0) == 2) {
            paytype = 2;
            iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
            iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
        }
        btn_pay_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paytype != 1 && paytype != 2) {
                    ToastUtil.showToastShortBottom(mContext, "请选择支付方式");
                    return;
                } else {
                    //付款
                    pWinBottomDialog.dismiss();
                    gratuityPop.dismiss();
                    goGratuity();
                }
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
        iv_pay_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
    }

    private void goGratuity() {
        CommUtil.gratuityPay(EvaluateOverActivity.this, workerId, 222, paytype, payPrice, orderId, remark, gratuityHandler);
    }

    private AsyncHttpResponseHandler gratuityHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.dimissDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0 && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata.has("workerImg") && !jdata.isNull("workerImg")) {
                        workerImg = jdata.getString("workerImg");
                    }
                    if (jdata.has("detail") && !jdata.isNull("detail")) {
                        detail = jdata.getString("detail");
                    }
                    if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
                        appid = null;
                        noncestr = null;
                        packageValue = null;
                        partnerid = null;
                        prepayid = null;
                        sign = null;
                        timestamp = null;
                        orderStr = null;
                        JSONObject jpayInfo = jdata
                                .getJSONObject("payInfo");
                        if (jpayInfo.has("appid")
                                && !jpayInfo.isNull("appid")) {
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
                        if (jpayInfo.has("sign")
                                && !jpayInfo.isNull("sign")) {
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
                        goPay();
                    }
                } else {
                    ToastUtil.showToastShortBottom(EvaluateOverActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.dimissDialog();
        }
    };

    private void goPay() {
        if (paytype == 1) {
            if (appid != null && !TextUtils.isEmpty(appid) && noncestr != null
                    && !TextUtils.isEmpty(noncestr) && packageValue != null
                    && !TextUtils.isEmpty(packageValue) && partnerid != null
                    && !TextUtils.isEmpty(partnerid) && prepayid != null
                    && !TextUtils.isEmpty(prepayid) && sign != null
                    && !TextUtils.isEmpty(sign) && timestamp != null
                    && !TextUtils.isEmpty(timestamp)) {
                // 微信支付
                spUtil.saveInt("payway", 1);
                mPDialog.showDialog();
                PayUtils.weChatPayment(EvaluateOverActivity.this, appid,
                        partnerid, prepayid, packageValue, noncestr, timestamp,
                        sign, mPDialog);
            } else {
                ToastUtil.showToastShortBottom(EvaluateOverActivity.this,
                        "支付参数错误");
            }
        } else if (paytype == 2) {
            if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                // 判断是否安装支付宝
                spUtil.saveInt("payway", 2);
                PayUtils.checkAliPay(EvaluateOverActivity.this, mHandler);
            } else {
                ToastUtil.showToastShortBottom(EvaluateOverActivity.this,
                        "支付参数错误");
            }
        }
    }

    private PopupWindow gratuityPop;

    private void showGratuityDialog() {
        payPrice = 0;
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(EvaluateOverActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        priceAdapter = new GratuityPriceAdapter(EvaluateOverActivity.this);
        GridLayoutManager layoutManager = new GridLayoutManager(EvaluateOverActivity.this, priceList.size());
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        priceAdapter.setPriceList(priceList);
        ViewGroup customView = (ViewGroup) View.inflate(EvaluateOverActivity.this, R.layout.appoint_gratuity_bottom_dialog, null);
        ImageView iv_close = (ImageView) customView.findViewById(R.id.iv_gratuity_bottomdia_close);
        ImageView iv_head = (ImageView) customView.findViewById(R.id.iv_beautician_head);
        TextView tv_content = (TextView) customView.findViewById(R.id.tv_gratuity_content);
        RecyclerView rv_price = (RecyclerView) customView.findViewById(R.id.rv_gratuity_price);
        final TextView tv_mark = (TextView) customView.findViewById(R.id.tv_gratuity_mark);
        Button btn_gratuity = (Button) customView.findViewById(R.id.btn_gratuity_bottomdia);
        rv_price.setLayoutManager(layoutManager);
        rv_price.setAdapter(priceAdapter);
        TextView tv_name = (TextView) customView.findViewById(R.id.tv_beautician_name);
        TextView tv_level = (TextView) customView.findViewById(R.id.tv_beautician_level);
        TextView tv_address = (TextView) customView.findViewById(R.id.tv_beautician_address);
        RelativeLayout rv_close = (RelativeLayout) customView.findViewById(R.id.rv_close);
        tv_address.setText(shopName);
        tv_name.setText(beautician_name);
        tv_content.setText(gratuity_content);
        tv_level.setText(levelname + "美容师");
        GlideUtil.loadCircleImg(mContext, beautician_iamge, iv_head, R.drawable.icon_default);
        gratuityPop = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        gratuityPop.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        gratuityPop.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        gratuityPop.setOutsideTouchable(true);
        //设置可以点击
        gratuityPop.setTouchable(true);
        //进入退出的动画
        gratuityPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        gratuityPop.setWidth(Utils.getDisplayMetrics(EvaluateOverActivity.this)[0]);
        gratuityPop.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gratuityPop.dismiss();
            }
        });
        gratuityPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        rv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gratuityPop.dismiss();
            }
        });
        priceAdapter.setListener(new GratuityPriceAdapter.ItemClickListener() {
            @Override
            public void onItemClick(WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean gratuityInfoBean) {
                if (gratuityInfoBean != null) {
                    payPrice = gratuityInfoBean.getAmount();
                    remark = gratuityInfoBean.getRemark();
                    tv_mark.setText(remark);
                }
            }
        });
        btn_gratuity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payPrice != 0) {
                    showPayDialog();
                } else {
                    ToastUtil.showToastShortBottom(EvaluateOverActivity.this, "请选择金额");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
        Log.e("TAG", "android.os.Build.MODEL = " + android.os.Build.MODEL);
        Log.e("TAG", "android.os.Build.VERSION.RELEASE = " + android.os.Build.VERSION.RELEASE);
        Log.e("TAG", "Global.WXPAYCODE = " + Global.WXPAYCODE);
        if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1") && Global.WXPAYCODE == 0) {
            Global.WXPAYCODE = -1;
            Log.e("支付成功", "onResume");
            goPayResult();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void goNext(Class cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    @OnClick({R.id.ib_titlebar_back
            , R.id.ib_titlebar_other
            , R.id.textview_go_look
            , R.id.tv_gratuity
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_other:
                break;
            case R.id.textview_go_look:
                goNext(MyCanActivity.class);
                break;
            case R.id.tv_gratuity:
                showGratuityDialog();
                Global.ServerEvent(EvaluateOverActivity.this, Global.ServerEventID.typeid_216, Global.ServerEventID.click_gratuity_overevaluate);
                break;
        }
    }

    //微信支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayResultEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            BaseResp resp = event.getResp();
            if (resp != null) {
                if (resp.errCode == 0) {
                    if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1")) {
                        Log.e("TAG", "OPPO哦");
                    } else {
                        goPayResult();
                    }
                } else {
                    if (Utils.isStrNull(resp.errStr)) {
                        ToastUtil.showToastShortBottom(mContext, resp.errStr);
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "支付失败");
                    }
                }
            }
        }
    }

    // 支付宝支付begin
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Utils.mLogError("支付宝返回码：" + msg.what);
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    Utils.mLogError("支付宝返回码：" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        goPayResult();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”
                        // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShort(EvaluateOverActivity.this,
                                    "支付结果确认中!");
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            // 从支付宝返回的状态
                        } else {
                            ToastUtil.showToastShort(EvaluateOverActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                        // 支付宝支付
                        mPDialog.showDialog();
                        PayUtils.payByAliPay(EvaluateOverActivity.this, orderStr,
                                mHandler, mPDialog);
                    } else {
                        ToastUtil.showToastShortBottom(EvaluateOverActivity.this,
                                "支付参数错误");
                    }
                    break;
            }
        }
    };

    private void goPayResult() {
        rl_gratuity.setVisibility(View.GONE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(EvaluateOverActivity.this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(EvaluateOverActivity.this, R.layout.alert_gratuity_layout, null);
        alertDialog.setView(view);
        alertDialog.show();
        ImageView iv_head = (ImageView) view.findViewById(R.id.iv_beautician_head);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_thank);
        RelativeLayout rv_close = (RelativeLayout) view.findViewById(R.id.rl_close);
        GlideUtil.loadCircleImg(mContext, workerImg, iv_head, R.drawable.icon_default);
        tv_content.setText(detail.replace("\\n", "\n"));
        rv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}