package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CardShopAdapter;
import com.haotang.pet.adapter.GiftDetailFreeAdapter;
import com.haotang.pet.entity.GiftCardDetail;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.haotang.pet.view.SoftKeyBoardListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
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
 * 购买E卡订单确认页面
 */
public class GiftCardSureOrderActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.rl_cardsure_root)
    RelativeLayout rlRoot;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_sure_order)
    ImageView ivSureOrder;
    @BindView(R.id.tv_order_detail_tag)
    TextView tvOrdertag;
    @BindView(R.id.tv_giftcard_name)
    TextView tvGiftcardName;
    @BindView(R.id.tv_giftcard_usablerange)
    TextView tvGiftcardUsablerange;
    @BindView(R.id.rv_giftcard_givelist)
    RecyclerView rvGiftfree;
    @BindView(R.id.ll_order_discount)
    LinearLayout llOrderDiscount;
    @BindView(R.id.tv_giftcard_face)
    TextView tvGiftcardFace;
    @BindView(R.id.tv_giftcard_payment_money)
    TextView tvGiftcardValue;
    @BindView(R.id.tv_giftcard_protocol)
    TextView tvGiftAgreement;
    @BindView(R.id.iv_giftcardorder_jian)
    ImageView ivGiftcardorderJian;
    @BindView(R.id.ed_giftcardorder_buynum)
    TextView edGiftcardorderBuynum;
    @BindView(R.id.rl_giftcard_protocol)
    RelativeLayout rvProtocol;
    @BindView(R.id.tv_giftcard_actual_money)
    TextView tvGifrcardActual;
    @BindView(R.id.tv_giftcard_total)
    TextView tvGiftcardTotal;
    @BindView(R.id.iv_giftcardorder_add)
    ImageView ivGiftcardorderAdd;
    @BindView(R.id.iv_giftcard_sure)
    ImageView ivGiftcardSure;
    @BindView(R.id.rl_giftcard_give)
    RelativeLayout rlGiftFree;
    @BindView(R.id.tv_giftcard_gobuy)
    TextView tvGiftcardBuy;
    @BindView(R.id.tv_giftcard_give)
    TextView tv_giftcard_give;
    private static GiftCardSureOrderActivity act;
    private Button btn_pay_bottomdia;
    private PopupWindow pWinBottomDialog;
    private int paytype;
    protected boolean isCheckRedeem;
    private String orderIcon;
    private String orderTitle;
    private String orderDesc;
    private String orderShoptext;
    private List<String> shopList = new ArrayList<>();
    private List<GiftCardDetail.DataBean.CouponListBean> freeList = new ArrayList<>();
    private double orderFace;
    private double orderSale;
    private int orderNum;
    private String orderAgreement;
    private boolean isCheck = true;
    private GiftDetailFreeAdapter cardFreeAdapter;
    private int oldpayway;
    private Intent intent;
    private int canBuyNum;
    private double totalPrice;
    private StringBuilder listpayWays = new StringBuilder();
    private String saleDesc;
    private EditText et_pay_bottomdia_yqm;
    private int cardTemplateId;
    private int cardId;
    private String[] cardPwd;
    //第三方支付
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private int myself;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Utils.mLogError("支付宝返回码：" + msg.what);
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    Utils.mLogError("支付宝返回码：" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // UmengStatistics.UmengEventStatistics(rechargePage,Global.UmengEventID.click_ChargePaySucess);//
                        // 充值成功
                        ToastUtil.showToastShort(GiftCardSureOrderActivity.this,
                                "支付成功");
                        goPayResult();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可\能支付失败
                        // “8000”
                        // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShort(GiftCardSureOrderActivity.this, "支付结果确认中!");
                        } else if (TextUtils.equals(resultStatus, "6001")) {

                        } else {
                            ToastUtil.showToastShort(GiftCardSureOrderActivity.this,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                        // 支付宝支付
                        mPDialog.showDialog();
                        PayUtils.payByAliPay(GiftCardSureOrderActivity.this, orderStr, mHandler,
                                mPDialog);
                    } else {
                        ToastUtil.showToastShortBottom(GiftCardSureOrderActivity.this, "支付参数错误");
                    }
                    break;
            }
        }
    };
    private String inviteCode;
    private String discountDesc;
    private ArrayList<String> discountDescNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initData();
        setView();
        setListener();
        getPayWay();
    }

    private void setListener() {

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                if (!TextUtils.isEmpty(et_pay_bottomdia_yqm.getText())) {
                    inviteCode = et_pay_bottomdia_yqm.getText().toString().trim();
                    checkRechargeInviteCode();
                }
            }
        });

        edGiftcardorderBuynum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Integer.valueOf(edGiftcardorderBuynum.getText().toString()) == 1) {
                    ivGiftcardorderJian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                    ivGiftcardorderJian.setClickable(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                GiftCardDetail.DataBean.CouponListBean couponListBean = new GiftCardDetail.DataBean.CouponListBean();
                if (Integer.valueOf(edGiftcardorderBuynum.getText().toString()) < 1) {
                    edGiftcardorderBuynum.setText(1 + "");
                    ivGiftcardorderJian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                    ivGiftcardorderJian.setClickable(false);
                    ivGiftcardorderAdd.setClickable(true);
                } else if (Integer.valueOf(edGiftcardorderBuynum.getText().toString()) == 1) {
                    ivGiftcardorderJian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
                    ivGiftcardorderJian.setClickable(false);
                    ivGiftcardorderAdd.setClickable(true);
                    for (int j = 0; j < freeList.size(); j++) {
                        freeList.get(j).setFreeNum(1);
                    }
                } else if (Integer.valueOf(edGiftcardorderBuynum.getText().toString()) > canBuyNum) {
                    edGiftcardorderBuynum.setText(canBuyNum + "");
                    ToastUtil.showToastShortBottom(GiftCardSureOrderActivity.this, saleDesc);
                    ivGiftcardorderAdd.setClickable(false);
                    for (int j = 0; j < freeList.size(); j++) {
                        freeList.get(j).setFreeNum(canBuyNum);
                    }
                }/*else if (Integer.valueOf(edGiftcardorderBuynum.getText().toString()) >shopStock){
                    edGiftcardorderBuynum.setText(shopStock + "");
                    ToastUtil.showToastShortBottom(GiftCardSureOrderActivity.this,"您已超出商品库存");
                    ivGiftcardorderAdd.setClickable(false);
                }*/ else {
                    ivGiftcardorderJian.setImageResource(R.drawable.pop_giftcarddetail_canjian);
                    ivGiftcardorderJian.setClickable(true);
                    ivGiftcardorderAdd.setClickable(true);
                    for (int j = 0; j < freeList.size(); j++) {
                        freeList.get(j).setFreeNum(Integer.valueOf(edGiftcardorderBuynum.getText().toString()));
                    }

                }

                if (Utils.isDoubleEndWithZero(orderSale * orderNum)) {
                    totalPrice = Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()));
                    SpannableString spannable = new SpannableString("¥  " + Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2));
                    AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(10, true);
                    spannable.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvGiftcardTotal.setText(Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2) + "");
                    tvGifrcardActual.setText("¥ " + Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2));
                } else {
                    totalPrice = orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString());
                    SpannableString spannableString = new SpannableString("¥  " + Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2));
                    AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(10, true);
                    spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvGiftcardTotal.setText(Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2) + "");
                    tvGifrcardActual.setText("¥ " + Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cardFreeAdapter != null) {
                    cardFreeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void checkRechargeInviteCode() {
        mPDialog.showDialog();
        CommUtil.checkRechargeInviteCode(spUtil.getString(
                "cellphone", ""), mContext, inviteCode,
                checkRechargeInviteCode);
    }

    private void setView() {
        tvTitlebarTitle.setText("确认订单");
        mPDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        GlideUtil.loadImg(GiftCardSureOrderActivity.this, orderIcon, ivSureOrder, R.drawable.icon_production_default);
        if (orderTitle != null) {
            tvGiftcardName.setText(orderTitle);
        }
        /*if (tagNames != null) {
            String[] split = tagNames.split(",");
            llOrderTags.removeAllViews();
            for (int i = 0; i < split.length; i++) {
                View view = View.inflate(GiftCardSureOrderActivity.this,R.layout.item_card_tags,null);
                TextView tvTags = (TextView) view.findViewById(R.id.tv_giftcard_discounts_tag);
                tvTags.setText(split[i]);
                llOrderTags.addView(view);
            }
        }*/
        if (orderAgreement == null || orderAgreement.equals("")) {
            rvProtocol.setVisibility(View.GONE);
            isCheck = true;
        } else {
            rvProtocol.setVisibility(View.VISIBLE);
            isCheck = false;
        }
        Utils.setText(tvOrdertag, discountDesc, "", View.GONE, View.GONE);
        if (discountDescNew!=null&&discountDescNew.size()>0){
            llOrderDiscount.removeAllViews();
            for (int i = 0; i < discountDescNew.size(); i++) {
                View view = View.inflate(mContext,R.layout.item_tv_discount,null);
                TextView tvDiscount = view.findViewById(R.id.tv_discount);
                String descString = discountDescNew.get(i);
                String[] split = descString.split("@@");
                int startIndex = descString.indexOf("@@");
                int endIndex = split[0].length() + split[1].length();
                SpannableString ss = new SpannableString(descString.replace("@@", ""));
                ss.setSpan(new TextAppearanceSpan(mContext, R.style.discount_style_yellow),
                        startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                tvDiscount.setText(ss);
                llOrderDiscount.addView(view);
            }
        }
        if (shopList == null || shopList.size() == 0) {
            tvGiftcardUsablerange.setText(orderShoptext);
        } else {
            tvGiftcardUsablerange.setText(orderShoptext + " >");
        }
        if (freeList != null && freeList.size() != 0) {
            tv_giftcard_give.setVisibility(View.VISIBLE);
            //赠送列表
            NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
            noScollFullLinearLayoutManager.setScrollEnabled(false);
            rvGiftfree.setLayoutManager(noScollFullLinearLayoutManager);
            rvGiftfree.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL,
                    DensityUtil.dp2px(this, 5),
                    ContextCompat.getColor(this, R.color.af8f8f8)));
            cardFreeAdapter = new GiftDetailFreeAdapter(R.layout.item_giftcard_detail_item, freeList);
            rvGiftfree.setAdapter(cardFreeAdapter);
            cardFreeAdapter.notifyDataSetChanged();
        } else {
            tv_giftcard_give.setVisibility(View.GONE);
        }
        tvGiftcardFace.setText("¥ " + orderFace);
        tvGiftcardValue.setText("¥ " + orderSale);
        edGiftcardorderBuynum.setText(orderNum + "");
        if (orderNum == 1) {
            ivGiftcardorderJian.setImageResource(R.drawable.pop_giftcarddetail_notjian);
        }
        if (Utils.isDoubleEndWithZero(orderSale * orderNum)) {
            totalPrice = Utils.formatDouble(orderSale * orderNum);
            SpannableString spannableString = new SpannableString("¥" + Utils.formatDouble(orderSale * orderNum));
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(10, true);
            spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvGifrcardActual.setText("¥ " + Utils.formatDouble(orderSale * orderNum));
            tvGiftcardTotal.setText(Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2) + "");
        } else {
            totalPrice = orderSale * orderNum;
            SpannableString spannableString = new SpannableString("¥" + orderSale * orderNum);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(10, true);
            spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvGifrcardActual.setText("¥ " + orderSale * orderNum);
            tvGiftcardTotal.setText(Utils.formatDouble(orderSale * Integer.valueOf(edGiftcardorderBuynum.getText().toString()), 2) + "");
        }
    }

    private void initData() {
        Global.WXPAYCODE = -1;
        act = this;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        //MApplication.listAppoint.add(act);
        oldpayway = spUtil.getInt("payway", 0);
        intent = getIntent();
        cardTemplateId = intent.getIntExtra("cardTemplateId", 0);
        discountDesc = intent.getStringExtra("discountDesc");
        orderIcon = intent.getStringExtra("orderIcon");
        orderTitle = intent.getStringExtra("orderTitle");
        orderDesc = intent.getStringExtra("orderDesc");
        freeList = (List<GiftCardDetail.DataBean.CouponListBean>) intent.getSerializableExtra("orderFreelist");
        orderShoptext = intent.getStringExtra("orderShoptext");
        orderFace = intent.getDoubleExtra("orderFace", 0);
        orderSale = intent.getDoubleExtra("orderSale", 0);
        orderNum = intent.getIntExtra("orderNum", 0);
        intent.getSerializableExtra("orderFreelist");
        canBuyNum = intent.getIntExtra("canBuyNum", 99999);
        saleDesc = intent.getStringExtra("saleDesc");
        orderAgreement = intent.getStringExtra("orderAgreement");
        shopList = intent.getStringArrayListExtra("orderShopList");
        discountDescNew = intent.getStringArrayListExtra("discountDescNew");
    }

    private void findView() {
        setContentView(R.layout.activity_gift_card_sure_order);
        ButterKnife.bind(this);
    }

    private void getPayWay() {
        listpayWays.setLength(0);
        mPDialog.showDialog();
        CommUtil.payWays(this, Global.ORDERKEY[11], 0, payWaysHandler);
    }

    private AsyncHttpResponseHandler checkRechargeInviteCode = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            Utils.mLogError("==-->充值前校验邀请码" + new String(responseBody));
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    Utils.goneJP(mContext);
                } else {
                    if (object.has("msg") && !object.isNull("msg")) {
                        String msg = object.getString("msg");
                        ToastUtil.showToastShortCenter(mContext, msg);
                        et_pay_bottomdia_yqm.setText("");
                        inviteCode = "";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(GiftCardSureOrderActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler payWaysHandler = new AsyncHttpResponseHandler() {
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
                        if (jData.has("payWays") && !jData.isNull("payWays")) {
                            JSONArray jsonArray = jData.getJSONArray("payWays");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listpayWays.append(jsonArray.getString(i));
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(GiftCardSureOrderActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(GiftCardSureOrderActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(GiftCardSureOrderActivity.this, "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.iv_giftcardorder_jian, R.id.iv_giftcardorder_add, R.id.tv_giftcard_usablerange, R.id.tv_giftcard_protocol, R.id.iv_giftcard_sure, R.id.tv_giftcard_gobuy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.iv_giftcardorder_jian:
                String snum = edGiftcardorderBuynum.getText().toString();
                Integer inum = Integer.valueOf(snum);
                inum--;
                edGiftcardorderBuynum.setText(inum + "");
                break;
            case R.id.iv_giftcardorder_add:
                String asnum = edGiftcardorderBuynum.getText().toString();
                Integer ainum = Integer.valueOf(asnum);
                ainum++;
                edGiftcardorderBuynum.setText(ainum + "");
                break;
            case R.id.tv_giftcard_usablerange:
                if (shopList != null && shopList.size() != 0) {
                    showShop();
                }
                break;
            case R.id.tv_giftcard_protocol:
                if (orderAgreement != null) {
                    Intent intent = new Intent(GiftCardSureOrderActivity.this, ADActivity.class);
                    intent.putExtra("url", orderAgreement);
                    startActivity(intent);
                }
                break;
            case R.id.iv_giftcard_sure:
                if (isCheck) {
                    ivGiftcardSure.setImageResource(R.drawable.complaint_reason_disable);
                    isCheck = false;
                } else {
                    ivGiftcardSure.setImageResource(R.drawable.complaint_reason);
                    isCheck = true;
                }
                break;
            case R.id.tv_giftcard_gobuy:
                if (!isCheck) {
                    ToastUtil.showToastShortCenter(this, "请先同意《宠物家预付卡购卡章程》");
                    return;
                }
                if (listpayWays != null && listpayWays.length() > 0) {
                    if (listpayWays.toString().contains("1") || listpayWays.toString().contains("2")) {
                        showPayDialog();
                    }
                }
                break;
        }
    }

    private void showShop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(GiftCardSureOrderActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(GiftCardSureOrderActivity.this, R.layout.shop_bottom_dialog, null);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        final RecyclerView rv_appointpetmx_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpetmx_bottomdia);
        ImageView iv_appointpetmx_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg);
        ImageView iv_appointpetmx_bottom_bg_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg_close);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(GiftCardSureOrderActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
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

    //微信支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayResultEvent event) {
        Log.e("TAG", "WXPayResultEvent = " + event.toString());
        if (event != null) {
            BaseResp resp = event.getResp();
            if (resp != null) {
                Log.e("TAG", "resp = " + resp.toString());
                Log.e("TAG", "resp.errCode = " + resp.errCode);
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

    @Override
    protected void onResume() {
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

    //支付成功跳转页面
    private void goPayResult() {
        if (pWinBottomDialog != null) {
            pWinBottomDialog.dismiss();
        }
        if (MApplication.listAppoint.size() > 0) {
            for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                MApplication.listAppoint.get(i).finish();
            }
        }
        if (MApplication.listAppoint1.size() > 0) {
            for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                MApplication.listAppoint1.get(i).finish();
            }
        }
        MApplication.listAppoint.clear();
        MApplication.listAppoint1.clear();
        Log.e("TAG", "PaySuccessNewActivity...");
        Intent intent = new Intent(GiftCardSureOrderActivity.this, PaySuccessNewActivity.class);
        intent.putExtra("type", 21);
        intent.putExtra("myself", myself);
        intent.putExtra("pageType", 1);
        intent.putExtra("cardFaceValue", orderFace);
        intent.putExtra("payPrice", totalPrice);
        String pwd = TextUtils.join(",", cardPwd);
        intent.putExtra("cardPwd", pwd);
        intent.putExtra("cardId", cardId);
        startActivity(intent);
        finish();
        Log.e("TAG", "PaySuccessNewActivity111...");
    }

    private AsyncHttpResponseHandler buyServiceCardHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("cardId") && !object.isNull("cardId")) {
                            cardId = object.getInt("cardId");
                        }
                        if (object.has("cardPwd") && !object.isNull("cardPwd")) {
                            JSONArray jsonArray = object.getJSONArray("cardPwd");
                            cardPwd = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                cardPwd[i] = (jsonArray.getString(i));
                            }

                        }
                        if (object.has("give_can") && !object.isNull("give_can")) {
                            JSONObject obGiveCan = object.getJSONObject("give_can");
                            if (obGiveCan.has("myself") && !obGiveCan.isNull("myself")) {
                                myself = obGiveCan.getInt("myself");
                            }
                        }
                        if (object.has("payInfo") && !object.isNull("payInfo")) {
                            JSONObject jpayInfo = object
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
                            if (paytype == 1) {
                                if (appid != null && !TextUtils.isEmpty(appid)
                                        && noncestr != null
                                        && !TextUtils.isEmpty(noncestr)
                                        && packageValue != null
                                        && !TextUtils.isEmpty(packageValue)
                                        && partnerid != null
                                        && !TextUtils.isEmpty(partnerid)
                                        && prepayid != null
                                        && !TextUtils.isEmpty(prepayid)
                                        && sign != null
                                        && !TextUtils.isEmpty(sign)
                                        && timestamp != null
                                        && !TextUtils.isEmpty(timestamp)) {
                                    // 微信支付
                                    mPDialog.showDialog();
                                    spUtil.saveInt("payway", 1);
                                    PayUtils.weChatPayment(GiftCardSureOrderActivity.this,
                                            appid, partnerid, prepayid,
                                            packageValue, noncestr, timestamp,
                                            sign, mPDialog);
                                } else {
                                    ToastUtil.showToastShortBottom(
                                            GiftCardSureOrderActivity.this, "支付参数错误");
                                }
                            } else if (paytype == 2) {
                                if (orderStr != null
                                        && !TextUtils.isEmpty(orderStr)) {
                                    // 判断是否安装支付宝
                                    spUtil.saveInt("payway", 2);
                                    PayUtils.checkAliPay(GiftCardSureOrderActivity.this,
                                            mHandler);
                                } else {
                                    ToastUtil.showToastShortBottom(
                                            GiftCardSureOrderActivity.this, "支付参数错误");
                                }
                            }
                        }
                    }
                } else {
                    mPDialog.closeDialog();
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg"))
                        ToastUtil.showToastShort(GiftCardSureOrderActivity.this,
                                jsonObject.getString("msg"));
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

    //购买时调用
    private void goBuy() {
        mPDialog.showDialog();
        int amount = Integer.valueOf(edGiftcardorderBuynum.getText().toString());
        CommUtil.buyServiceCard(this, cardTemplateId, amount, paytype, totalPrice, et_pay_bottomdia_yqm.getText().toString(), buyServiceCardHandler);
    }

    private void showPayDialog() {
        oldpayway = spUtil.getInt("payway", 0);
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(GiftCardSureOrderActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(GiftCardSureOrderActivity.this, R.layout.appoint_pay_bottom_dialog, null);
//        ImageView iv_pay_bottom_bg = (ImageView) customView.findViewById(R.id.iv_pay_bottom_bg);
        btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        customView.findViewById(R.id.ll_pay_bottomdia_time).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_minute).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_second).setVisibility(View.GONE);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        RelativeLayout rl_pay_bottomdia_yqm = (RelativeLayout) customView.findViewById(R.id.rl_pay_bottomdia_yqm);
        customView.findViewById(R.id.iv_pay_bottomdia_yqm_select).setVisibility(View.GONE);
        et_pay_bottomdia_yqm = (EditText) customView.findViewById(R.id.et_pay_bottomdia_yqm);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(GiftCardSureOrderActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isCheckRedeem = false;
                et_pay_bottomdia_yqm.setText("");
            }
        });
        et_pay_bottomdia_yqm.setImeOptions(EditorInfo.IME_ACTION_DONE);
        rl_pay_bottomdia_yqm.setVisibility(View.VISIBLE);
        btn_pay_bottomdia.setText("确认支付¥" + totalPrice);
        Log.e("TAG", "oldpayway = " + oldpayway);
        if (listpayWays.toString().contains("1")) {
            ll_pay_bottomdia_weixin.setVisibility(View.VISIBLE);
            if (oldpayway == 1) {
                paytype = 1;
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_weixin.setVisibility(View.GONE);
        }
        if (listpayWays.toString().contains("2")) {
            ll_pay_bottomdia_zfb.setVisibility(View.VISIBLE);
            if (oldpayway == 2) {
                paytype = 2;
                iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
                iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
            }
        } else {
            ll_pay_bottomdia_zfb.setVisibility(View.GONE);
        }
        tv_pay_title.setText("请选择支付方式");
        iv_pay_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        btn_pay_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paytype != 1 && paytype != 2) {
                    ToastUtil.showToastShortBottom(mContext, "请选择支付方式");
                    return;
                } else {
                    goBuy();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}

