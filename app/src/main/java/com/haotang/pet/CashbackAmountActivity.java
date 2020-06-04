package com.haotang.pet;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CashBackAdapter;
import com.haotang.pet.entity.CashBackBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class CashbackAmountActivity extends SuperActivity {

    @BindView(R.id.iv_cashback_back)
    ImageView ivCashbackBack;
    @BindView(R.id.listView_cashback)
    PullToRefreshListView listViewCashback;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_cashabck_all)
    TextView tvCashabckAll;
    @BindView(R.id.v_cashback_all)
    View vCashbackAll;

    @BindView(R.id.tv_cashabck_tiemout)
    TextView tvCashabckTiemout;
    @BindView(R.id.v_cashback_timeout)
    View vCashbackTimeout;
    @BindView(R.id.tv_cashback_waitin)
    TextView tvCashbackWaitin;
    @BindView(R.id.tv_cashback_amount)
    TextView tvCashbackAmount;
    @BindView(R.id.tv_reduction_desc)
    TextView tvReductionDesc;
    @BindView(R.id.rl_cashback_null)
    RelativeLayout rlCashbackNull;
    @BindView(R.id.tv_cashback_null)
    TextView tvCashbackNull;
    @BindView(R.id.tv_cashback_nulltwo)
    TextView tvCashbackNulltwo;
    @BindView(R.id.iv_cashback_openqr)
    ImageView ivCashbackOpenqr;
    private AnimatorSet mHideIV;
    private AnimatorSet mShowIV;
    private List<CashBackBean.DataBean.ListBean> cashBackList = new ArrayList<>();
    private String explain;
    private double enteredMoney;
    private CashBackAdapter adapter;
    private int page = 1;
    private int state = 0;
    private int animaState = 1;
    private String codeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initWindows();
        setView();
        setListener();
        initAnimation();
        getCashBack(page, state);
    }

    private void initAnimation() {
        mHideIV = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.cash_btn_hide);
        mShowIV = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.cash_btn_show);
        mHideIV.setTarget(ivCashbackOpenqr);
        mShowIV.setTarget(ivCashbackOpenqr);
    }

    private void setAuthCode(final String authCode, final ImageView ivCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bingoogolapple = QRCodeEncoder.syncEncodeQRCode(authCode,
                        BGAQRCodeUtil.dp2px(CashbackAmountActivity.this, 180), Color.BLACK, Color.WHITE,
                        null);
                if (bingoogolapple != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivCode.setImageBitmap(bingoogolapple);
                        }
                    });
                }
            }
        }).start();
    }

    private void setListener() {
        listViewCashback.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                cashBackList.clear();
                adapter.notifyDataSetChanged();
                page=1;
                getCashBack(page, state);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                adapter.notifyDataSetChanged();
                getCashBack(page, state);
            }
        });
        listViewCashback.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean scrollFlag = false;// 标记是否滑动
            private int lastVisibleItemPosition;// 标记上次滑动位置

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                //判断状态
                switch (i) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        // 当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时，即滚动时
                        scrollFlag = true;
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (scrollFlag) {
                    if (i > lastVisibleItemPosition) {
                        if (animaState==1){
                            hideIV();
                        }
                        animaState=2;

                    }
                    if (i < lastVisibleItemPosition) {
                        if (animaState==2){
                            showIV();
                        }
                        animaState=1;

                    }
                    if (i == lastVisibleItemPosition) {
                        return;
                    }
                    lastVisibleItemPosition = i;
                }

            }

        });
    }

    private void hideIV() {
        mHideIV.start();
    }

    private void showIV() {
        mShowIV.start();
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

    private void setView() {
        listViewCashback.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new CashBackAdapter(mContext, cashBackList);
        listViewCashback.setAdapter(adapter);
    }

    private void getCashBack(int page, int state) {
        CommUtil.backCashBillHomePage(mContext, state, page, 10, cashBackHandler);
    }

    private void findView() {
        setContentView(R.layout.activity_cashback_amount);
        ButterKnife.bind(this);
    }


    private AsyncHttpResponseHandler cashBackHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            listViewCashback.onRefreshComplete();
            Gson gson = new Gson();
            CashBackBean cashBackBean = gson.fromJson(new String(responseBody), CashBackBean.class);
            if (cashBackBean.getCode() == 0) {
                if (cashBackBean.getData() != null) {
                    CashBackBean.DataBean data = cashBackBean.getData();
                    tvCashbackWaitin.setText("¥" + Utils.formatDecimal(data.getStayMoney()));
                    enteredMoney = data.getEnteredMoney();
                        tvCashbackAmount.setText("¥" + Utils.formatDecimal(enteredMoney));
                    if (enteredMoney>0){
                        tvReductionDesc.setText(data.getFullMoney());
                    }else {
                        ivCashbackOpenqr.setVisibility(View.GONE);
                    }
                    explain = data.getBackExplain();
                    if (data.getList() != null) {
                        page++;
                        cashBackList.addAll(data.getList());
                    }
                    if (cashBackList.size() == 0) {
                        listViewCashback.setVisibility(View.GONE);
                        rlCashbackNull.setVisibility(View.VISIBLE);
                        if (state==0){
                            tvCashbackNulltwo.setVisibility(View.VISIBLE);
                            tvCashbackNull.setVisibility(View.VISIBLE);
                        }else {
                            tvCashbackNulltwo.setVisibility(View.GONE);
                            tvCashbackNull.setVisibility(View.GONE);
                        }
                    } else {
                        listViewCashback.setVisibility(View.VISIBLE);
                        rlCashbackNull.setVisibility(View.GONE);
                    }
                    adapter.setType(state);
                    adapter.notifyDataSetChanged();
                }
            } else {
                ToastUtil.showToastShortBottom(mContext, cashBackBean.getMsg());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            listViewCashback.onRefreshComplete();
            listViewCashback.setVisibility(View.GONE);
            tvCashbackNull.setVisibility(View.INVISIBLE);
            tvCashbackNulltwo.setText("点击重试");
            rlCashbackNull.setVisibility(View.VISIBLE);
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void getQRCode() {
        mPDialog.showDialog();
        CommUtil.getauthCode(mContext, 12, 0, getCodeHandler);
    }

    private AsyncHttpResponseHandler getCodeHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.dimissDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        codeMsg = jsonObject.getString("data");
                        showQRPop();
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
            mPDialog.dimissDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void showQRPop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        View customView = View.inflate(mContext, R.layout.common_qrcode_pop, null);
        RelativeLayout rlDemiss = customView.findViewById(R.id.rl_pop_dimess);
        ImageView ivCode = customView.findViewById(R.id.iv_qrpop_qrcode);
        ImageView ivEcard = customView.findViewById(R.id.iv_qrcodepop_bottom);
        TextView tvPopTitle = customView.findViewById(R.id.tv_qrpop_conponname);
        TextView tvUsetext = customView.findViewById(R.id.tv_qrpop_usetext);
        ImageView ivTop = customView.findViewById(R.id.iv_qrcodepop_top);
        ImageView ivBottom = customView.findViewById(R.id.iv_qrcodepop_bottom);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点 
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失 
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击 
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画 
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(CashbackAmountActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        setAuthCode(codeMsg, ivCode);
        ivTop.setImageResource(R.drawable.icon_usecoupon_red);
        tvPopTitle.setText("返现金额：¥"+enteredMoney);
        tvUsetext.setText("正在使用返现金额抵扣商品");
        ivBottom.setImageResource(R.drawable.icon_usecoupin_bottomred);
        rlDemiss.setOnClickListener(new View.OnClickListener() {
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
        ivEcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CashbackAmountActivity.this, QrCodeNewActivity.class));
                pWinBottomDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.iv_cashback_back, R.id.rl_cashback_all, R.id.rl_cashback_timeour, R.id.rl_cashback_explain, R.id.rl_cashback_gouse, R.id.tv_cashback_nulltwo, R.id.iv_cashback_openqr})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cashback_back:
                finish();
                break;
            case R.id.rl_cashback_all:
                tvCashabckAll.setTextColor(Color.parseColor("#333333"));
                tvCashabckAll.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvCashabckTiemout.setTextColor(Color.parseColor("#666666"));
                tvCashabckTiemout.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                vCashbackAll.setVisibility(View.VISIBLE);
                vCashbackTimeout.setVisibility(View.INVISIBLE);
                cashBackList.clear();
                state = 0;
                page = 1;
                getCashBack(page, state);
                break;
            case R.id.rl_cashback_timeour:
                tvCashabckAll.setTextColor(Color.parseColor("#666666"));
                tvCashabckAll.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvCashabckTiemout.setTextColor(Color.parseColor("#333333"));
                tvCashabckTiemout.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                vCashbackAll.setVisibility(View.INVISIBLE);
                vCashbackTimeout.setVisibility(View.VISIBLE);
                cashBackList.clear();
                state = 1;
                page = 1;
                getCashBack(page, state);
                break;
            case R.id.rl_cashback_explain:
                Intent intent = new Intent(CashbackAmountActivity.this, ADActivity.class);
                intent.putExtra("url", explain);
                startActivity(intent);
                break;
            case R.id.rl_cashback_gouse:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.mainactivity");
                intent1.putExtra("previous",
                        Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
                sendBroadcast(intent1);
                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                    MApplication.listAppoint.get(i).finish();
                }
                for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                    MApplication.listAppoint1.get(i).finish();
                }
                finishWithAnimation();
                break;
            case R.id.tv_cashback_nulltwo:
                cashBackList.clear();
                getCashBack(1, state);
                break;
            case R.id.iv_cashback_openqr:
                getQRCode();
                break;
        }
    }

    @OnClick()
    public void onViewClicked() {
    }
}
