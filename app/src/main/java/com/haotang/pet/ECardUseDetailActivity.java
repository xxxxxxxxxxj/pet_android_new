package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CardShopAdapter;
import com.haotang.pet.adapter.MyBillAdapter;
import com.haotang.pet.entity.CardOrderDetail;
import com.haotang.pet.entity.GoodsBill;
import com.haotang.pet.entity.MyBill;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.ECardDetailPopupWindow;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 姜谷蓄
 * 礼品卡使用详情页
 */
public class ECardUseDetailActivity extends SuperActivity {

    @BindView(R.id.tv_ecard)
    TextView tvEcard;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.iv_ecarduse_more)
    ImageView ivEcardMore;
    @BindView(R.id.tv_ecard_totalamount)
    TextView tvEcardTotalamount;
    @BindView(R.id.rl_ecard_info)
    RelativeLayout rlEcardInfo;
    @BindView(R.id.iv_ecarduse_bg)
    ImageView ivEcarduseBg;
    @BindView(R.id.rl_ecard_usedetail)
    RelativeLayout rlEcardUsedetail;
    @BindView(R.id.tv_ecarduse_shop)
    TextView tvEcarduseShop;
    @BindView(R.id.tv_ecarduse_number)
    TextView tvEcarduseNumber;
    @BindView(R.id.tv_ecarduse_data)
    TextView tvEcarduseData;
    @BindView(R.id.tv_ecarduse_used)
    TextView tvEcarduseUsed;
    @BindView(R.id.rv_ecarduse)
    RecyclerView rvEcarduse;
    @BindView(R.id.srl_ecarduse)
    SwipeRefreshLayout srlEcarduse;
    @BindView(R.id.ll_ecarduse_cardinfo)
    LinearLayout llEcarduseCardinfo;
    @BindView(R.id.ll_ecarduse_discount)
    LinearLayout llEcarduseCardDiscount;
    @BindView(R.id.iv_giftcard_shop)
    ImageView ivShopMore;
    @BindView(R.id.tv_ecard_info)
    TextView tvEcardInfo;
    @BindView(R.id.tv_ecard_usedetail)
    TextView tvEcardUsedetail;
    @BindView(R.id.ll_ecarduse_selecttop)
    LinearLayout llEcarduseSelecttop;
    @BindView(R.id.tv_emptyview_desc)
    TextView tv_emptyview_desc;
    @BindView(R.id.btn_emptyview)
    Button btn_emptyview;
    @BindView(R.id.ll_order_default)
    LinearLayout ll_order_default;
    private int page = 1;
    private int index = 0;
    private int state = 0;
    private int pageSize;
    private List<MyBill> list = new ArrayList<MyBill>();
    private List<MyBill> localList = new ArrayList<MyBill>();
    private MyBillAdapter myBillAdapter;
    private int id;
    private String accountText;
    private double totalAmount;
    private double payOut;
    private String shopPhone;
    private ECardDetailPopupWindow cardDetailPopupWindow;
    private int canBack;
    private String reason;
    public static SuperActivity act;
    private List<String> shopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initData();
        setView();
        initWindows();
        setLinster();
        getData();
        if (index == 1) {
            refresh();
            llEcarduseCardinfo.setVisibility(View.GONE);
            srlEcarduse.setVisibility(View.VISIBLE);
            tvEcardInfo.setTextColor(Color.parseColor("#999999"));
            tvEcardUsedetail.setTextColor(Color.parseColor("#333333"));
            tvEcardUsedetail.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvEcardInfo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            llEcarduseSelecttop.setBackgroundResource(R.drawable.bg_ecard_usedselect);
        }

    }

    private void initData() {
        act = this;
        id = getIntent().getIntExtra("id", 0);
        index = getIntent().getIntExtra("index", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_ecard_use_detail);
        ButterKnife.bind(this);
    }

    private void setView() {
        srlEcarduse.setRefreshing(true);
        srlEcarduse.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvEcarduse.setHasFixedSize(true);
        rvEcarduse.setLayoutManager(new LinearLayoutManager(this));
        myBillAdapter = new MyBillAdapter(R.layout.item_mybill, list, 2);
        rvEcarduse.setAdapter(myBillAdapter);
        rvEcarduse.addItemDecoration(new StickyRecyclerHeadersDecoration(myBillAdapter));
    }

    protected void initWindows() {
        Window window = getWindow();
        int color = Color.parseColor("#FFD0021B");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "1");
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("TAG", "2");
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.cardRecord(this, id, cardInfoHandler);
        CommUtil.myServiceCardOrderDetail(this, id, cardDetailHandler);
    }

    private AsyncHttpResponseHandler cardDetailHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    Gson gson = new Gson();
                    CardOrderDetail cardOrderDetail = gson.fromJson(new String(responseBody), CardOrderDetail.class);
                    CardOrderDetail.DataBean cardOrderDetailData = cardOrderDetail.getData();
                    CardOrderDetail.DataBean.ServiceCardBean serviceCard = cardOrderDetailData.getServiceCard();
                    if (serviceCard.getDicountDesc() != null && serviceCard.getDicountDesc().size() > 0) {
                        llEcarduseCardDiscount.removeAllViews();
                        for (int i = 0; i < serviceCard.getDicountDesc().size(); i++) {
                            View view = View.inflate(mContext, R.layout.item_tv_discount_bgwhite, null);
                            TextView tvDiscount = view.findViewById(R.id.tv_discount);
                            String descString = serviceCard.getDicountDesc().get(i);
                            String[] split = descString.split("@@");
                            int startIndex = descString.indexOf("@@");
                            int endIndex = split[0].length() + split[1].length();
                            SpannableString ss = new SpannableString(descString.replace("@@", ""));
                            ss.setSpan(new TextAppearanceSpan(mContext, R.style.discount_style_white),
                                    startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            tvDiscount.setText(ss);
                            llEcarduseCardDiscount.addView(view);
                        }
                    }
                    tvEcarduseShop.setText(cardOrderDetailData.getShopText());
                    if (cardOrderDetailData.getShopList() != null && cardOrderDetailData.getShopList().size() != 0) {
                        ivShopMore.setVisibility(View.VISIBLE);
                        shopList = cardOrderDetailData.getShopList();
                    }else {
                        ivShopMore.setVisibility(View.GONE);
                    }
                    shopPhone = cardOrderDetailData.getPhone();
                    tvEcarduseNumber.setText(serviceCard.getCardNumber());
                    tvEcarduseData.setText(serviceCard.getExpireTime());
                    state = cardOrderDetailData.getStatus();
                    //退款相关bean
                    CardOrderDetail.DataBean.RefundRuleBean refundRule = cardOrderDetailData.getRefundRule();
                    if (refundRule != null) {
                        canBack = refundRule.getRefundable();
                        reason = refundRule.getReason();
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private AsyncHttpResponseHandler cardInfoHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.dimissDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("mineCardPic")&&!jdata.isNull("mineCardPic")){
                            GlideUtil.loadImg(mContext,jdata.getString("mineCardPic"),ivEcarduseBg,R.drawable.bg_ecard_top);
                        }
                        if (jdata.has("totalAmount") && !jdata.isNull("totalAmount")) {
                            totalAmount = jdata.getDouble("totalAmount");
                            tvEcardTotalamount.setText(totalAmount + "");
                        }
                        if (jdata.has("payOut") && !jdata.isNull("payOut")) {
                            payOut = jdata.getDouble("payOut");
                            tvEcarduseUsed.setText("¥" + payOut);
                        }
                        if (jdata.has("accountText") && !jdata.isNull("accountText")) {
                            accountText = jdata.getString("accountText");
                        }
                        if (jdata.has("serviceCardTypeName")&&!jdata.isNull("serviceCardTypeName")){
                            tvEcard.setText(jdata.getString("serviceCardTypeName"));
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void setLinster() {
        myBillAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlEcarduse.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        myBillAdapter.setEnableLoadMore(false);
        srlEcarduse.setRefreshing(true);
        page = 1;
        queryTradeHistory();
    }

    private void loadMore() {
        queryTradeHistory();
    }

    private void queryTradeHistory() {
        mPDialog.showDialog();
        CommUtil.queryTradeHistory(spUtil.getString("cellphone", ""), Global.getIMEI(this), this, page, "", "", id, queryTradeHistory);
    }

    private AsyncHttpResponseHandler queryTradeHistory = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            localList.clear();
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject jdata = jsonObject.getJSONObject("data");
                        if (jdata.has("dataset") && !jdata.isNull("dataset")) {
                            JSONArray jarrdataset = jdata.getJSONArray("dataset");
                            if (jarrdataset.length() > 0) {
                                for (int i = 0; i < jarrdataset.length(); i++) {
                                    localList.add(MyBill.json2Entity(jarrdataset
                                            .getJSONObject(i)));
                                }
                                for (int i = 0; i < localList.size(); i++) {
                                    MyBill myBill = localList.get(i);
                                    String tradeDate = myBill.getTradeDate();
                                    String[] split = tradeDate.split("-");
                                    myBill.setGroupTime(split[0] + "-" + split[1]);
                                    List<GoodsBill> goodsList = myBill.getGoodsList();
                                    if (goodsList != null && goodsList.size() > 0) {
                                        for (int j = 0; j < goodsList.size(); j++) {
                                            goodsList.get(j).setIcon(myBill.getIcon());
                                        }
                                    } else {
                                        if (goodsList == null) {
                                            goodsList = new ArrayList<GoodsBill>();
                                        }
                                        goodsList.add(new GoodsBill(myBill.getItem(), myBill.getAmount(), "", myBill.getIcon()));
                                        myBill.setGoodsList(goodsList);
                                    }
                                }
                            }
                        }
                    }
                    if (page == 1) {
                        srlEcarduse.setRefreshing(false);
                        myBillAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    myBillAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        setLayout(1, 0, "");
                        if (page == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                myBillAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        if (page == 1) {
                            setLayout(2, 2, "暂无明细～");
                            myBillAdapter.loadMoreEnd(true);
                        } else {
                            myBillAdapter.loadMoreEnd(false);
                        }
                    }
                    myBillAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        setLayout(2, 1, msg);
                        myBillAdapter.setEnableLoadMore(false);
                        srlEcarduse.setRefreshing(false);
                    } else {
                        myBillAdapter.setEnableLoadMore(true);
                        myBillAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    setLayout(2, 1, "数据异常");
                    myBillAdapter.setEnableLoadMore(false);
                    srlEcarduse.setRefreshing(false);
                } else {
                    myBillAdapter.setEnableLoadMore(true);
                    myBillAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        list.get(i).setMonth(true);
                        list.get(i).setHeaderId(i);
                    } else {
                        if (list.get(i - 1).getGroupTime().equals(list.get(i).getGroupTime())) {
                            list.get(i).setMonth(false);
                            list.get(i).setHeaderId(list.get(i - 1).getHeaderId());
                        } else {
                            list.get(i).setMonth(true);
                            list.get(i).setHeaderId(i);
                        }
                    }
                }
            }
            myBillAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                setLayout(2, 1, "请求失败");
                myBillAdapter.setEnableLoadMore(false);
                srlEcarduse.setRefreshing(false);
            } else {
                myBillAdapter.setEnableLoadMore(true);
                myBillAdapter.loadMoreFail();
            }
        }
    };

    private void setLayout(int type, int flag, String msg) {
        if (type == 1) {
            ll_order_default.setVisibility(View.GONE);
            srlEcarduse.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_order_default.setVisibility(View.VISIBLE);
            srlEcarduse.setVisibility(View.GONE);
            if (flag == 1) {
                btn_emptyview.setVisibility(View.VISIBLE);
                btn_emptyview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            } else if (flag == 2) {
                btn_emptyview.setVisibility(View.GONE);
            }
            Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_mallorder_sqtk:
                    if (canBack == 0) {
                        new AlertDialogNavAndPost(ECardUseDetailActivity.this)
                                .builder()
                                .setTitle("")
                                .setMsg(reason)
                                .setNegativeButtonVisible(View.GONE)
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    } else {
                        if (Utils.isStrNull(reason)) {
                            new AlertDialogNavAndPost(ECardUseDetailActivity.this)
                                    .builder()
                                    .setTitle("")
                                    .setMsg(reason)
                                    .setNegativeButtonVisible(View.GONE)
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ECardUseDetailActivity.this, ApplyForRefundActivity.class);
                                            intent.putExtra("id", id);
                                            startActivity(intent);
                                        }
                                    }).show();
                        } else {
                            Intent intent = new Intent(ECardUseDetailActivity.this, ApplyForRefundActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    }
                    dismissPop();
                    break;
                case R.id.tv_pop_mallorder_lxkf:
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

    private void showPop() {
        dismissPop();
        cardDetailPopupWindow = new ECardDetailPopupWindow(mContext, onClickListener,index,state);
        cardDetailPopupWindow.showAsDropDown(ivEcardMore, -10, -30);
    }

    private void dismissPop() {
        if (cardDetailPopupWindow != null) {
            if (cardDetailPopupWindow.isShowing()) {
                cardDetailPopupWindow.dismiss();
            }
        }
    }
    private void showShop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(ECardUseDetailActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(ECardUseDetailActivity.this, R.layout.shop_bottom_dialog, null);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        final RecyclerView rv_appointpetmx_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpetmx_bottomdia);
        ImageView iv_appointpetmx_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg);
        ImageView iv_appointpetmx_bottom_bg_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg_close);
        LinearLayout ll_pop_root = (LinearLayout) customView.findViewById(R.id.ll_pop_root);
        TextView tv_appointpetmx_bottomdia_title = (TextView) customView.findViewById(R.id.tv_appointpetmx_bottomdia_title);
        RelativeLayout rl_appointpetmx_bottomdia = (RelativeLayout) customView.findViewById(R.id.rl_appointpetmx_bottomdia);
        LinearLayout ll_appointpetmx_bottomdia = (LinearLayout) customView.findViewById(R.id.ll_appointpetmx_bottomdia);
        ll_appointpetmx_bottomdia.bringToFront();
        rv_appointpetmx_bottomdia.setHasFixedSize(true);
        rv_appointpetmx_bottomdia.setLayoutManager(new LinearLayoutManager(this));//添加自定义分割线 
        rv_appointpetmx_bottomdia.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL, DensityUtil.dp2px(this, 1), ContextCompat.getColor(this, R.color.aEEEEEE)));
        tv_appointpetmx_bottomdia_title.setText("适用门店");
        rv_appointpetmx_bottomdia.setAdapter(new CardShopAdapter(R.layout.item_card_shop, shopList));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_appointpetmx_bottomdia.scrollToPosition(0);
            }
        }, 500);
        float screenDensity = ScreenUtil.getScreenDensity(this);
        Log.e("TAG", "screenDensity = " + screenDensity);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(ECardUseDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        ll_pop_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        rl_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        ll_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @OnClick({R.id.iv_ecarduse_back, R.id.iv_ecarduse_more, R.id.iv_ecard_desc, R.id.rl_ecard_info, R.id.rl_ecard_usedetail,R.id.rl_ecarduse_todetail,R.id.rl_ecarduse_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_ecarduse_back:
                finish();
                break;
            case R.id.iv_ecarduse_more:
                showPop();
                break;
            case R.id.iv_ecard_desc:
                Utils.setCardDesc(mContext, "宠物家E卡说明", accountText, "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case R.id.rl_ecard_info:
                llEcarduseCardinfo.setVisibility(View.VISIBLE);
                srlEcarduse.setVisibility(View.GONE);
                tvEcardInfo.setTextColor(Color.parseColor("#333333"));
                tvEcardUsedetail.setTextColor(Color.parseColor("#999999"));
                tvEcardUsedetail.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvEcardInfo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                llEcarduseSelecttop.setBackgroundResource(R.drawable.bg_ecard_infoselect);
                break;
            case R.id.rl_ecard_usedetail:
                refresh();
                llEcarduseCardinfo.setVisibility(View.GONE);
                srlEcarduse.setVisibility(View.VISIBLE);
                tvEcardInfo.setTextColor(Color.parseColor("#999999"));
                tvEcardUsedetail.setTextColor(Color.parseColor("#333333"));
                tvEcardInfo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvEcardUsedetail.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                llEcarduseSelecttop.setBackgroundResource(R.drawable.bg_ecard_usedselect);
                break;
            case R.id.rl_ecarduse_todetail:
                refresh();
                llEcarduseCardinfo.setVisibility(View.GONE);
                srlEcarduse.setVisibility(View.VISIBLE);
                tvEcardInfo.setTextColor(Color.parseColor("#999999"));
                tvEcardUsedetail.setTextColor(Color.parseColor("#333333"));
                tvEcardInfo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvEcardUsedetail.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                llEcarduseSelecttop.setBackgroundResource(R.drawable.bg_ecard_usedselect);
                break;
            case R.id.rl_ecarduse_shop:
                if (shopList!=null&&shopList.size()>0){
                    showShop();
                }
                break;
        }
    }



}
