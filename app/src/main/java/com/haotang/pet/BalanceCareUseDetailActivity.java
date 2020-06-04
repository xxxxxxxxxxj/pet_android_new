package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyBillAdapter;
import com.haotang.pet.entity.GoodsBill;
import com.haotang.pet.entity.MyBill;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
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
 * 姜谷蓄
 * 余额卡使用详情页
 */
public class BalanceCareUseDetailActivity extends SuperActivity {

    @BindView(R.id.iv_balancecard_back)
    ImageView ivBalancecardBack;
    @BindView(R.id.rl_code_recharge)
    RelativeLayout rlCodeRecharge;
    @BindView(R.id.tv_balancecard)
    TextView tvBalancecard;
    @BindView(R.id.tv_balancecard_used)
    TextView tvBalancecardUsed;
    @BindView(R.id.rv_balancecard)
    RecyclerView rvBalancecard;
    @BindView(R.id.tv_balancecard_discount)
    TextView tvBalancecardDiscount;
    @BindView(R.id.srl_balancecard)
    SwipeRefreshLayout srlBalancecard;
    @BindView(R.id.iv_balancecard_bg)
    ImageView ivBananceBg;
    public static SuperActivity act;
    @BindView(R.id.tv_balancecard_totalamount)
    TextView tvBalancecardTotalamount;
    @BindView(R.id.tv_emptyview_desc)
    TextView tv_emptyview_desc;
    @BindView(R.id.btn_emptyview)
    Button btn_emptyview;
    @BindView(R.id.ll_order_default)
    LinearLayout ll_order_default;
    private double totalAmount;
    private double payOut;
    private String accountText;
    private int id;
    private int page = 1;
    private int pageSize;
    private List<MyBill> list = new ArrayList<MyBill>();
    private List<MyBill> localList = new ArrayList<MyBill>();
    private MyBillAdapter myBillAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        setView();
        initWindows();
        initData();
        setLinster();
        getData();
        refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefundCardEvent event) {
        if (event != null && event.isRefund()) {
            getData();
            refresh();
        }
    }

    private void findView() {
        setContentView(R.layout.activity_balance_care_detail);
        ButterKnife.bind(this);
    }

    private void setView() {
        srlBalancecard.setRefreshing(true);
        srlBalancecard.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvBalancecard.setHasFixedSize(true);
        rvBalancecard.setLayoutManager(new LinearLayoutManager(this));
        myBillAdapter = new MyBillAdapter(R.layout.item_mybill, list, 2);
        rvBalancecard.setAdapter(myBillAdapter);
        rvBalancecard.addItemDecoration(new StickyRecyclerHeadersDecoration(myBillAdapter));
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

    private void initData() {
        act = this;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        id = getIntent().getIntExtra("id", 0);
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.cardRecord(this, id, cardInfoHandler);
    }


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
                            GlideUtil.loadImg(mContext,jdata.getString("mineCardPic"),ivBananceBg,R.drawable.bg_balancard_top);
                        }
                        if (jdata.has("totalAmount") && !jdata.isNull("totalAmount")) {
                            totalAmount = jdata.getDouble("totalAmount");
                            tvBalancecardTotalamount.setText(totalAmount+"");
                        }
                        if (jdata.has("payOut") && !jdata.isNull("payOut")) {
                            payOut = jdata.getDouble("payOut");
                            tvBalancecardUsed.setText("¥" + payOut);
                        }
                        if (jdata.has("accountText") && !jdata.isNull("accountText")) {
                            accountText = jdata.getString("accountText");
                        }
                        if (jdata.has("serviceCardTypeName")&&!jdata.isNull("serviceCardTypeName")){
                            tvBalancecard.setText(jdata.getString("serviceCardTypeName"));
                        }
                        if (jdata.has("dicountDesc")&&!jdata.isNull("dicountDesc")){
                            JSONArray dicountDesc = jdata.getJSONArray("dicountDesc");
                            if (dicountDesc!=null&&dicountDesc.length()>0){
                                String descString = dicountDesc.getString(0);
                                String[] split = descString.split("@@");
                                int startIndex = descString.indexOf("@@");
                                int endIndex = split[0].length() + split[1].length();
                                Log.e("TAG", "startIndex = " + startIndex);
                                Log.e("TAG", "endIndex = " + endIndex);
                                SpannableString ss = new SpannableString(descString.replace("@@", ""));
                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.discount_style_white),
                                        startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                tvBalancecardDiscount.setText(ss);
                            }
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
        srlBalancecard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        myBillAdapter.setEnableLoadMore(false);
        srlBalancecard.setRefreshing(true);
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
                        srlBalancecard.setRefreshing(false);
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
                        srlBalancecard.setRefreshing(false);
                    } else {
                        myBillAdapter.setEnableLoadMore(true);
                        myBillAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    setLayout(2, 1, "数据异常");
                    myBillAdapter.setEnableLoadMore(false);
                    srlBalancecard.setRefreshing(false);
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
                srlBalancecard.setRefreshing(false);
            } else {
                myBillAdapter.setEnableLoadMore(true);
                myBillAdapter.loadMoreFail();
            }
        }
    };

    private void setLayout(int type, int flag, String msg) {
        if (type == 1) {
            ll_order_default.setVisibility(View.GONE);
            srlBalancecard.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_order_default.setVisibility(View.VISIBLE);
            srlBalancecard.setVisibility(View.GONE);
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

    @OnClick({R.id.iv_balancecard_back, R.id.rl_code_recharge, R.id.iv_balancecard_desc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_balancecard_back:
                finish();
                break;
            case R.id.rl_code_recharge:
                startActivity(new Intent(BalanceCareUseDetailActivity.this, BalanceExchangeActivity.class));
                break;
            case R.id.iv_balancecard_desc:
                Utils.setCardDesc(mContext, "余额卡说明", accountText, "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;

        }
    }

}
