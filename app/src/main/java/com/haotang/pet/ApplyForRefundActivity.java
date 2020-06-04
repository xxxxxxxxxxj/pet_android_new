package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CardDiscountAdapter;
import com.haotang.pet.adapter.CardShopAdapter;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.NoScollSyLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请退卡确认界面
 */
public class ApplyForRefundActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.iv_applyrefund_cardbg)
    ImageView ivApplyrefundCardbg;
    @BindView(R.id.tv_applyrefund_name)
    TextView tvApplyrefundName;
    @BindView(R.id.tv_applyrefund_symd)
    TextView tvApplyrefundSymd;
    @BindView(R.id.tv_applyrefund_sjzf)
    TextView tvApplyrefundSjzf;
    @BindView(R.id.tv_applyrefund_ysy)
    TextView tvApplyrefundYsy;
    @BindView(R.id.tv_applyrefund_tkhj)
    TextView tvApplyrefundTkhj;
    @BindView(R.id.btn_applyrefund_submit)
    Button btnApplyrefundSubmit;
    @BindView(R.id.tv_applyrefund_fwyh)
    TextView tvApplyrefundFwyh;
    @BindView(R.id.tv_applyrefund_zsyhqyh)
    TextView tvApplyrefundZsyhqyh;
    @BindView(R.id.tv_applyrefund_sxf)
    TextView tvApplyrefundSxf;
    @BindView(R.id.rv_applyrefund_zk)
    RecyclerView rvApplyrefundZk;
    @BindView(R.id.ll_applyrefund_zsyhqyh)
    LinearLayout ll_applyrefund_zsyhqyh;
    private int id;
    private List<String> discountList = new ArrayList<String>();
    private List<String> shops = new ArrayList<String>();
    private List<String> bqList = new ArrayList<String>();
    private StringBuffer stringBuffer = new StringBuffer();
    private String phone;
    private int flag;
    private String discountText;
    private CardDiscountAdapter cardDiscountAdapter;
    private String chargeExplain;
    private double refundAmount;
    private int couponNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        geData();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        id = getIntent().getIntExtra("id", 0);
        flag = getIntent().getIntExtra("flag", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_apply_for_refund);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarOther.setText("规则");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));
        tvTitlebarTitle.setText("申请退款");

        rvApplyrefundZk.setHasFixedSize(true);
        rvApplyrefundZk.setNestedScrollingEnabled(false);
        NoScollSyLinearLayoutManager noScollFullLinearLayoutManager = new NoScollSyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvApplyrefundZk.setLayoutManager(noScollFullLinearLayoutManager);
        cardDiscountAdapter = new CardDiscountAdapter(R.layout.item_card_discount, discountList);
        rvApplyrefundZk.setAdapter(cardDiscountAdapter);
    }

    private void setLinster() {

    }

    private void geData() {
        discountList.clear();
        shops.clear();
        stringBuffer.setLength(0);
        bqList.clear();
        mPDialog.showDialog();
        discountText = "";
        CommUtil.myServiceCardOrderDetail(this, id, cardDetailHandler);
    }

    private AsyncHttpResponseHandler cardDetailHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("serviceCard") && !jdata.isNull("serviceCard")) {
                            JSONObject jserviceCard = jdata.getJSONObject("serviceCard");
                            if (jserviceCard.has("couponInfo") && !jserviceCard.isNull("couponInfo")) {
                                JSONObject jcouponInfo = jserviceCard.getJSONObject("couponInfo");
                                if (jcouponInfo.has("couponNum") && !jcouponInfo.isNull("couponNum")) {
                                    couponNum = jcouponInfo.getInt("couponNum");
                                }
                            }
                            if (jserviceCard.has("chargeExplain") && !jserviceCard.isNull("chargeExplain")) {
                                chargeExplain = jserviceCard.getString("chargeExplain");
                            }
                            if (jserviceCard.has("dicountDesc") && !jserviceCard.isNull("dicountDesc")) {
                                JSONArray jarrdicountDesc = jserviceCard.getJSONArray("dicountDesc");
                                if (jarrdicountDesc != null && jarrdicountDesc.length() > 0) {
                                    for (int i = 0; i < jarrdicountDesc.length(); i++) {
                                        discountList.add(jarrdicountDesc.getString(i));
                                    }
                                }
                            }
                            if (jserviceCard.has("payPrice") && !jserviceCard.isNull("payPrice")) {
                                Utils.setText(tvApplyrefundSjzf, "¥" + jserviceCard.getDouble("payPrice"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jserviceCard.has("useAmount") && !jserviceCard.isNull("useAmount")) {
                                Utils.setText(tvApplyrefundYsy, "¥" + jserviceCard.getDouble("useAmount"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jserviceCard.has("discountAmount") && !jserviceCard.isNull("discountAmount")) {
                                Utils.setText(tvApplyrefundFwyh, "¥" + jserviceCard.getDouble("discountAmount"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jserviceCard.has("serviceChargePrice") && !jserviceCard.isNull("serviceChargePrice")) {
                                Utils.setText(tvApplyrefundSxf, "¥" + jserviceCard.getDouble("serviceChargePrice"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jserviceCard.has("couponDiscountPrice") && !jserviceCard.isNull("couponDiscountPrice")) {
                                Utils.setText(tvApplyrefundZsyhqyh, "¥" + jserviceCard.getDouble("couponDiscountPrice"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (jserviceCard.has("refundAmount") && !jserviceCard.isNull("refundAmount")) {
                                refundAmount = jserviceCard.getDouble("refundAmount");
                            }
                        }
                        if (jdata.has("smallPic") && !jdata.isNull("smallPic")) {
                            GlideUtil.loadRoundImg(ApplyForRefundActivity.this, jdata.getString("smallPic"), ivApplyrefundCardbg, R.drawable.icon_production_default, 10);
                        }
                        if (jdata.has("serviceCardTypeName") && !jdata.isNull("serviceCardTypeName")) {
                            Utils.setText(tvApplyrefundName, jdata.getString("serviceCardTypeName"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("shopList") && !jdata.isNull("shopList")) {
                            JSONArray jarrShopList = jdata.getJSONArray("shopList");
                            if (jarrShopList != null && jarrShopList.length() > 0) {
                                for (int i = 0; i < jarrShopList.length(); i++) {
                                    shops.add(jarrShopList.getString(i));
                                }
                            }
                        }
                        if (jdata.has("shopText") && !jdata.isNull("shopText")) {
                            Utils.setText(tvApplyrefundSymd, (shops != null && shops.size() > 0) ? jdata.getString("shopText") + ">" : jdata.getString("shopText"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("tagContent1") && !jdata.isNull("tagContent1")) {
                            bqList.add(jdata.getString("tagContent1"));
                        }
                        if (jdata.has("discountText") && !jdata.isNull("discountText")) {
                            discountText = jdata.getString("discountText");
                            bqList.add(discountText);
                        }
                        if (jdata.has("phone") && !jdata.isNull("phone")) {
                            phone = jdata.getString("phone");
                        }
                        if (jdata.has("refundRule") && !jdata.isNull("refundRule")) {
                            JSONObject jrefundRule = jdata.getJSONObject("refundRule");
                            if (jrefundRule.has("refundTips") && !jrefundRule.isNull("refundTips")) {
                                JSONArray jarrRefund_tips = jrefundRule.getJSONArray("refundTips");
                                if (jarrRefund_tips != null && jarrRefund_tips.length() > 0) {
                                    for (int i = 0; i < jarrRefund_tips.length(); i++) {
                                        if (i == jarrRefund_tips.length() - 1) {
                                            stringBuffer.append(jarrRefund_tips.getString(i));
                                        } else {
                                            stringBuffer.append(jarrRefund_tips.getString(i) + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(ApplyForRefundActivity.this, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(ApplyForRefundActivity.this, "数据异常");
            }
            if (couponNum > 0) {
                ll_applyrefund_zsyhqyh.setVisibility(View.VISIBLE);
            } else {
                ll_applyrefund_zsyhqyh.setVisibility(View.GONE);
            }
            Utils.setText(tvApplyrefundTkhj, "¥" + refundAmount, "", View.VISIBLE, View.VISIBLE);
            if (refundAmount > 0) {
                btnApplyrefundSubmit.setBackgroundResource(R.drawable.bg_orange_round16);
            } else {
                btnApplyrefundSubmit.setBackgroundResource(R.drawable.bg_999999_round16);
            }
            cardDiscountAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ApplyForRefundActivity.this, "请求失败");
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

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other, R.id.btn_applyrefund_submit, R.id.tv_applyrefund_symd, R.id.ll_applyrefund_fwyh, R.id.ll_applyrefund_zsyhqyh, R.id.iv_applyrefund_sxf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                Utils.setCardDesc(mContext, "退款规则", stringBuffer.toString(), "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case R.id.btn_applyrefund_submit:
                if (refundAmount > 0) {
                    new AlertDialogNavAndPost(ApplyForRefundActivity.this)
                            .builder()
                            .setTitle("本次将为您退款 ¥" + refundAmount)
                            .setMsg("是否确定退款？")
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setPositiveButton("确定退款", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mPDialog.showDialog();
                                    CommUtil.refundCard(ApplyForRefundActivity.this, id, refundAmount, refundCardHandler);
                                }
                            }).show();
                }
                break;
            case R.id.tv_applyrefund_symd:
                if (shops.size() > 0) {
                    showShopDialog();
                }
                break;
            case R.id.ll_applyrefund_fwyh:
                startActivity(new Intent(mContext, ECardUseDetailActivity.class).putExtra("id", id).putExtra("index", 1));
                break;
            case R.id.ll_applyrefund_zsyhqyh:
                startActivity(new Intent(mContext, CouponUseDetalActivity.class).putExtra("id", id));
                break;
            case R.id.iv_applyrefund_sxf:
                Utils.setCardDesc(mContext, "服务费说明", chargeExplain, "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
    }

    private AsyncHttpResponseHandler refundCardHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    ToastUtil.showToastShortBottom(ApplyForRefundActivity.this, "已收到您的退款请求，马上为您处理~");
                    EventBus.getDefault().post(new RefundCardEvent(true));
                    if (flag == 1) {
                        startActivity(new Intent(mContext, MyCardActivity.class));
                        if (ECardUseDetailActivity.act != null) {
                            ECardUseDetailActivity.act.finish();
                        }
                    }
                    finish();
                } else {
                    new AlertDialogNavAndPost(ApplyForRefundActivity.this)
                            .builder()
                            .setTitle("")
                            .setMsg(msg)
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.callToPhone(phone, ApplyForRefundActivity.this);
                                }
                            }).show();
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortBottom(ApplyForRefundActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ApplyForRefundActivity.this, "请求失败");
        }
    };

    private void showShopDialog() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(ApplyForRefundActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(ApplyForRefundActivity.this, R.layout.shop_bottom_dialog, null);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        LinearLayout ll_pop_root = (LinearLayout) customView.findViewById(R.id.ll_pop_root);
        final RecyclerView rv_appointpetmx_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpetmx_bottomdia);
        ImageView iv_appointpetmx_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg);
        ImageView iv_appointpetmx_bottom_bg_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg_close);
        TextView tv_appointpetmx_bottomdia_title = (TextView) customView.findViewById(R.id.tv_appointpetmx_bottomdia_title);
        RelativeLayout rl_appointpetmx_bottomdia = (RelativeLayout) customView.findViewById(R.id.rl_appointpetmx_bottomdia);
        LinearLayout ll_appointpetmx_bottomdia = (LinearLayout) customView.findViewById(R.id.ll_appointpetmx_bottomdia);
        tv_appointpetmx_bottomdia_title.setText("适用门店");
        ll_appointpetmx_bottomdia.bringToFront();
        rv_appointpetmx_bottomdia.setHasFixedSize(true);
        rv_appointpetmx_bottomdia.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        rv_appointpetmx_bottomdia.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.aEEEEEE)));
        rv_appointpetmx_bottomdia.setAdapter(new CardShopAdapter(R.layout.item_card_shop, shops));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_appointpetmx_bottomdia.scrollToPosition(0);
            }
        }, 500);
        float screenDensity = ScreenUtil.getScreenDensity(this);
        Log.e("TAG", "screenDensity = " + screenDensity);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(ApplyForRefundActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        ll_pop_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        rl_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        ll_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
