package com.haotang.pet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CardShopAdapter;
import com.haotang.pet.adapter.GiftOrderlFreeAdapter;
import com.haotang.pet.entity.CardOrderDetail;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityUtils;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CardMenuPopupWindow;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardOrderDetailActivity extends SuperActivity {

    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_giftcard_name)
    TextView tvCardName;
    @BindView(R.id.sv_orderdetail_all)
    NestedScrollView svOrderDetail;
    @BindView(R.id.ll_cardorder_nonet)
    LinearLayout llOrderNonet;
    @BindView(R.id.tv_giftcard_usablerange)
    TextView tvCardUseshop;
    @BindView(R.id.iv_sure_order)
    ImageView ivCardSure;
    @BindView(R.id.tv_cardorder_state)
    TextView tvCardorderState;
    @BindView(R.id.tv_backdetail_tip)
    TextView tvCardBacktip;
    @BindView(R.id.tv_bindcard_name)
    TextView tvCardBindname;
    @BindView(R.id.tv_cardnum)
    TextView tvCradNum;
    @BindView(R.id.tv_cardpwd)
    TextView tvCardPwd;
    @BindView(R.id.tv_card_facevaule)
    TextView tvFacevaule;
    @BindView(R.id.tv_giftcard_truevaule)
    TextView tvTruevalue;
    @BindView(R.id.tv_giftcard_ordernum)
    TextView tvOrdernum;
    @BindView(R.id.ll_giftcard_discount)
    LinearLayout llDiscount;
    @BindView(R.id.tv_card_paytime)
    TextView tvCardpayTime;
    @BindView(R.id.tv_card_paytype)
    TextView tvPayType;
    @BindView(R.id.tv_card_phone)
    TextView tvCardPhone;
    @BindView(R.id.tv_giftcard_bind)
    TextView tvGiftCardBind;
    @BindView(R.id.rl_giftcard_back)
    RelativeLayout rlGiftcardBack;
    @BindView(R.id.rl_giftcard_noback)
    RelativeLayout rlGiftcanrNoback;
    @BindView(R.id.tv_giftcard_back_payvaule)
    TextView tvBackPayvalue;
    @BindView(R.id.tv_giftcard_backvaule)
    TextView tvBackValue;
    @BindView(R.id.rl_orderdetail_couponList)
    RelativeLayout rlCouponlist;
    @BindView(R.id.rv_giftcard_givelist)
    RecyclerView rvGivelist;
    @BindView(R.id.placeholder)
    ImageView placeHolder;
    @BindView(R.id.iv_orderdetail_call)
    ImageView ivCall;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    private List<CardOrderDetail.DataBean.CouponListBean> couponList = new ArrayList<>();
    private List<String> shop = new ArrayList<>();
    private int serviceCardId;
    private String cardPwd;
    private String cardOrdernum;
    private MProgressDialog pDialog;
    private double sub;
    private GiftOrderlFreeAdapter orderFreeAdapter;
    private String reason;
    private int canBack = 2;
    private int confirm;
    private String phone;
    private List<String> menuList = new ArrayList<String>();
    private CardMenuPopupWindow cardMenuPopupWindow;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefundCardEvent event) {
        if (event != null && event.isRefund()) {
            getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        setView();
        initData();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //去订单Fragment
        ActivityUtils.toOrderFragment(this);
    }

    private void setView() {
        ibTitlebarOther.setVisibility(View.VISIBLE);
        ibTitlebarOther.setBackgroundResource(R.drawable.order_right_icon);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 40), Utils.dip2px(mContext, 22));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ibTitlebarOther.setLayoutParams(layoutParams);

        tvTitlebarTitle.setText("订单详情");
        if (couponList == null || couponList.size() == 0) {
            rlCouponlist.setVisibility(View.GONE);
        }
        pDialog = new MProgressDialog(this);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvGivelist.setLayoutManager(noScollFullLinearLayoutManager);
        rvGivelist.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                DensityUtil.dp2px(this, 5),
                ContextCompat.getColor(this, R.color.af8f8f8)));
        orderFreeAdapter = new GiftOrderlFreeAdapter(R.layout.item_giftcard_detail_item, couponList);
        rvGivelist.setAdapter(orderFreeAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        serviceCardId = intent.getIntExtra("serviceCardId", 0);
    }

    private void getData() {
        pDialog.showDialog();
        menuList.clear();
        CommUtil.myServiceCardOrderDetail(this, serviceCardId, cardDetailHandler);
    }


    private AsyncHttpResponseHandler cardDetailHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            pDialog.closeDialog();
            svOrderDetail.setVisibility(View.VISIBLE);
            llOrderNonet.setVisibility(View.GONE);
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    Gson gson = new Gson();
                    CardOrderDetail cardOrderDetail = gson.fromJson(new String(responseBody), CardOrderDetail.class);
                    if (cardOrderDetail != null) {
                        CardOrderDetail.DataBean cardOrderDetailData = cardOrderDetail.getData();
                        if (cardOrderDetailData != null) {
                            if (cardOrderDetailData.getServiceCard().getBindUserId() == 0) {
                                tvGiftCardBind.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                                tvGiftCardBind.setText("立即绑定");
                                tvGiftCardBind.setClickable(true);
                            } else {
                                tvGiftCardBind.setBackgroundResource(R.drawable.bg_icon_isbind);
                                tvGiftCardBind.setText("已绑定");
                                tvGiftCardBind.setClickable(false);
                            }
                            switch (cardOrderDetailData.getStatus()) {
                                case 2:
                                    tvCardorderState.setText("已完成");
                                    placeHolder.setImageResource(R.drawable.order_already_over);
                                    tvCardBacktip.setVisibility(View.GONE);
                                    menuList.add("申请退款");
                                    rlGiftcardBack.setVisibility(View.GONE);
                                    rlGiftcanrNoback.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    tvCardorderState.setText("退款处理中");
                                    placeHolder.setImageResource(R.drawable.order_is_to_working);
                                    tvGiftCardBind.setBackgroundResource(R.drawable.bg_icon_isbind);
                                    tvGiftCardBind.setClickable(false);
                                    tvCardBacktip.setVisibility(View.VISIBLE);
                                    tvCardBacktip.setText("查看退款进度 >");
                                    rlGiftcardBack.setVisibility(View.VISIBLE);
                                    rlGiftcanrNoback.setVisibility(View.GONE);
                                    break;
                                case 4:
                                case 5:
                                    tvCardorderState.setText("已退款");
                                    placeHolder.setImageResource(R.drawable.order_already_over);
                                    tvGiftCardBind.setBackgroundResource(R.drawable.bg_icon_isbind);
                                    tvGiftCardBind.setClickable(false);
                                    tvCardBacktip.setVisibility(View.VISIBLE);
                                    tvCardBacktip.setText("查看退款详情 >");
                                    rlGiftcardBack.setVisibility(View.VISIBLE);
                                    rlGiftcanrNoback.setVisibility(View.GONE);
                                    break;
                            }
                            tvCardName.setText(cardOrderDetailData.getServiceCardTypeName());//卡名
                            tvCardBindname.setText(cardOrderDetailData.getServiceCardTypeName());
                            if (cardOrderDetailData.getShopList() != null && cardOrderDetailData.getShopList().size() != 0) {
                                shop = cardOrderDetailData.getShopList();
                            }
                            if (shop == null || shop.size() == 0) {
                                tvCardUseshop.setText(cardOrderDetailData.getShopText());//适用门店
                            } else {
                                tvCardUseshop.setText(cardOrderDetailData.getShopText() + " >");//适用门店
                            }

                            tvCardPhone.setText(cardOrderDetailData.getPhone());//售后电话
                            phone = cardOrderDetailData.getPhone();
                            GlideUtil.loadImg(CardOrderDetailActivity.this, cardOrderDetailData.getSmallPic(), ivCardSure, R.drawable.icon_production_default);
                            //优惠券的bean
                            if (cardOrderDetailData.getCouponList() != null && cardOrderDetailData.getCouponList().size() != 0) {
                                couponList.clear();
                                rlCouponlist.setVisibility(View.VISIBLE);
                                couponList.addAll(cardOrderDetailData.getCouponList());
                                orderFreeAdapter.notifyDataSetChanged();
                            } else {
                                rlCouponlist.setVisibility(View.INVISIBLE);
                            }

                            //退款相关bean
                            CardOrderDetail.DataBean.RefundRuleBean refundRule = cardOrderDetailData.getRefundRule();
                            if (refundRule != null) {
                                canBack = refundRule.getRefundable();
                                reason = refundRule.getReason();
                            }
                            //服务卡的bean
                            CardOrderDetail.DataBean.ServiceCardBean serviceCardBean = cardOrderDetailData.getServiceCard();
                            if (serviceCardBean != null) {
                                tvCradNum.setText(serviceCardBean.getCardNumber());
                                tvCardPwd.setText(serviceCardBean.getCardPassword());
                                cardPwd = serviceCardBean.getCardPassword();
                                tvFacevaule.setText("¥ " + serviceCardBean.getFaceValue());
                                tvTruevalue.setText("¥ " + serviceCardBean.getPayPrice());
                                tvBackPayvalue.setText("¥ " + serviceCardBean.getPayPrice());
                                tvOrdernum.setText(serviceCardBean.getTradeNo());
                                cardOrdernum = serviceCardBean.getTradeNo();
                                tvCardpayTime.setText(serviceCardBean.getPayTime());
                                //sub = ComputeUtil.sub(serviceCardBean.getPayPrice(), serviceCardBean.getUseAmount(), serviceCardBean.getDiscountAmount()) < 0 ? 0 : ComputeUtil.sub(serviceCardBean.getPayPrice(), serviceCardBean.getUseAmount(), serviceCardBean.getDiscountAmount());
                                tvBackValue.setText("¥ " + serviceCardBean.getRefundAmount());
                                if (serviceCardBean.getPayWay() == 1) {
                                    tvPayType.setText("微信");
                                } else if (serviceCardBean.getPayWay() == 2) {
                                    tvPayType.setText("支付宝");
                                }
                                //双折扣
                                if (serviceCardBean.getDicountDesc()!=null&&serviceCardBean.getDicountDesc().size()>0){
                                    List<String> dicountDesc = serviceCardBean.getDicountDesc();
                                    llDiscount.removeAllViews();
                                    for (int i = 0; i < dicountDesc.size(); i++) {
                                        View view = View.inflate(mContext,R.layout.item_tv_discount,null);
                                        TextView tvDiscount = view.findViewById(R.id.tv_discount);
                                        String descString = dicountDesc.get(i);
                                        String[] split = descString.split("@@");
                                        int startIndex = descString.indexOf("@@");
                                        int endIndex = split[0].length() + split[1].length();
                                        SpannableString ss = new SpannableString(descString.replace("@@", ""));
                                        ss.setSpan(new TextAppearanceSpan(mContext, R.style.discount_style_yellow),
                                                startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                        tvDiscount.setText(ss);
                                        llDiscount.addView(view);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(CardOrderDetailActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            menuList.add("联系客服");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            svOrderDetail.setVisibility(View.GONE);
            llOrderNonet.setVisibility(View.VISIBLE);
            ToastUtil.showToastShortBottom(CardOrderDetailActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler bindCardHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                final int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    showOk();
                    tvGiftCardBind.setBackgroundResource(R.drawable.bg_icon_isbind);
                    tvGiftCardBind.setText("已绑定");
                    tvGiftCardBind.setClickable(false);
                } else {
                    ToastUtil.showToastShortBottom(CardOrderDetailActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void showOk() {
        new AlertDialogNavAndPost(CardOrderDetailActivity.this)
                .builder()
                .setTitle("")
                .setMsg("绑定成功")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("查看卡包", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Utils.callToPhone(phone, ApplyForRefundActivity.this);
                        Intent intent = new Intent(CardOrderDetailActivity.this, MyCardActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    private void bindCard() {
        CommUtil.bindCard(cardPwd, confirm, this, bindCardHandler);
    }

    private void findView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_card_order_detail);
        ButterKnife.bind(this);
    }

    private void showPop() {
        dismissPop();
        cardMenuPopupWindow = new CardMenuPopupWindow(this, 2, menuList, onClickListener);
        cardMenuPopupWindow.showAsDropDown(ibTitlebarOther, -10, -30);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_washorder_xgdd://申请退款
                    if (canBack == 0) {
                        new AlertDialogNavAndPost(CardOrderDetailActivity.this)
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
                            new AlertDialogNavAndPost(CardOrderDetailActivity.this)
                                    .builder()
                                    .setTitle("")
                                    .setMsg(reason)
                                    .setNegativeButtonVisible(View.GONE)
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(CardOrderDetailActivity.this, ApplyForRefundActivity.class);
                                            intent.putExtra("id", serviceCardId);
                                            startActivity(intent);
                                        }
                                    }).show();
                        } else {
                            Intent intent = new Intent(CardOrderDetailActivity.this, ApplyForRefundActivity.class);
                            intent.putExtra("id", serviceCardId);
                            startActivity(intent);
                        }
                    }
                    break;
                case R.id.tv_pop_washorder_sqtk://联系客服
                    Utils.callToPhone(phone, CardOrderDetailActivity.this);
                    break;
            }
            dismissPop();
        }
    };

    private void dismissPop() {
        if (cardMenuPopupWindow != null) {
            if (cardMenuPopupWindow.isShowing()) {
                cardMenuPopupWindow.dismiss();
            }
        }
    }

    @OnClick({R.id.tv_copy_pwd, R.id.tv_copy_order, R.id.ib_titlebar_back, R.id.tv_backdetail_tip, R.id.tv_giftcard_usablerange, R.id.tv_giftcard_bind, R.id.iv_orderdetail_call, R.id.btn_cardorder_nonet, R.id.ib_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                ActivityUtils.toOrderFragment(CardOrderDetailActivity.this);
                finish();
                break;
            case R.id.ib_titlebar_other:
                showPop();
                break;
            case R.id.tv_copy_pwd:
                if (cardPwd != null) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
                    ClipData clipData = ClipData.newPlainText(null, cardPwd);
                    // 把数据集设置（复制）到剪贴板
                    clipboard.setPrimaryClip(clipData);
                    ToastUtil.showToastShortBottom(CardOrderDetailActivity.this, "已复制到粘贴板");
                }

                break;
            case R.id.tv_copy_order:
                if (cardOrdernum != null) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
                    ClipData clipData = ClipData.newPlainText(null, cardOrdernum);
                    // 把数据集设置（复制）到剪贴板
                    clipboard.setPrimaryClip(clipData);
                    ToastUtil.showToastShortBottom(CardOrderDetailActivity.this, "已复制到粘贴板");
                }
                break;
            case R.id.tv_backdetail_tip:
                Intent intent = new Intent(CardOrderDetailActivity.this, RefundScheduleActivity.class);
                intent.putExtra("id", serviceCardId);
                startActivity(intent);
                break;
            case R.id.tv_giftcard_usablerange:
                if (shop != null && shop.size() != 0) {
                    showShop();
                }
                break;
            case R.id.iv_orderdetail_call:
                Utils.callToPhone(phone, CardOrderDetailActivity.this);
                break;
            case R.id.tv_giftcard_bind:
                new AlertDialogNavAndPost(CardOrderDetailActivity.this)
                        .builder()
                        .setTitle("绑定E卡")
                        .setSubTitle("是否立即绑定E卡到当前账号" + spUtil.getString("cellphone", "") + "?")
                        .setMsg("绑定成功后不可解绑")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("立即绑定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Utils.callToPhone(phone, ApplyForRefundActivity.this);
                                confirm = 1;
                                bindCard();
                            }
                        }).show();
                break;
            case R.id.btn_cardorder_nonet:
                getData();
                break;
        }
    }


    private void showShop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(CardOrderDetailActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(CardOrderDetailActivity.this, R.layout.shop_bottom_dialog, null);
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
        rv_appointpetmx_bottomdia.setAdapter(new CardShopAdapter(R.layout.item_card_shop, shop));
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(CardOrderDetailActivity.this)[0]);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
