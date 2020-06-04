package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CouponDetailTipAdapter;
import com.haotang.pet.entity.CouponDetail;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
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

/**
 * 姜谷蓄
 * 优惠券详情页
 */

public class CouponDetailActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_coupon_name)
    TextView tvCouponName;
    @BindView(R.id.tv_coupon_end)
    TextView tvCouponEnd;
    @BindView(R.id.tv_coupon_desc)
    TextView tvCouponDesc;
    @BindView(R.id.tv_coupon_usewithcard)
    TextView tvCouponUsewithcard;
    @BindView(R.id.tv_coupon_reduce)
    TextView tvCouponReduce;
    @BindView(R.id.rl_coupon_reduce)
    RelativeLayout rlCouponReduce;
    @BindView(R.id.tv_coupon_free)
    TextView tvCouponFree;
    @BindView(R.id.iv_coupon_type)
    ImageView ivCouponType;
    @BindView(R.id.iv_coupon_petemore)
    ImageView ivCouponPetemore;
    @BindView(R.id.iv_usecoupon_buy)
    ImageView ivUsecouponBuy;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_coupon_willend)
    TextView tvCouponWillend;
    @BindView(R.id.tv_coupon_changeone_right)
    TextView tvCouponChangeoneRight;
    @BindView(R.id.tv_coupon_changetwo_left)
    TextView tvCouponChangetwoLeft;
    @BindView(R.id.tv_coupon_changetwo_right)
    TextView tvCouponChangetwoRight;
    @BindView(R.id.tv_coupon_changethree_left)
    TextView tvCouponChangethreeLeft;
    @BindView(R.id.tv_coupon_changethree_right)
    TextView tvCouponChangethreeRight;
    @BindView(R.id.tv_coupon_changeone_left)
    TextView tvCouponChangeoneLeft;
    @BindView(R.id.tv_coupon_rmb)
    TextView tvCouponRmb;
    @BindView(R.id.tv_coupon_useshop)
    TextView tvCouponUseshop;
    @BindView(R.id.iv_coupon_shopmore)
    ImageView ivCouponShopmore;
    @BindView(R.id.rl_coupon_chooseone)
    RelativeLayout rlCouponChooseone;
    @BindView(R.id.iv_coupon_beautymore)
    ImageView ivCouponBeautymore;
    @BindView(R.id.rl_coupon_choosetwo)
    RelativeLayout rlCouponChoosetwo;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.iv_coupon_servicemore)
    ImageView ivCouponServicemore;
    @BindView(R.id.rl_coupon_choosethree)
    RelativeLayout rlCouponChoosethree;
    @BindView(R.id.rl_coupon_choosefour)
    RelativeLayout rlCouponChoosefour;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    private String couponName;
    private List<String> listOne = new ArrayList<>();
    private List<String> listTwo = new ArrayList<>();
    private List<String> listThree = new ArrayList<>();
    private List<String> listFour = new ArrayList<>();
    private String titleTwo;
    private String titleThree;
    private String titleFour;
    private int id;
    private int isCanGive;
    private String codeMsg;
    private String shareDesc;
    private String shareUrl;
    private String shareTitle;
    private String shareImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        getData();
        setListener();
    }

    private void setView() {
        setContentView(R.layout.activity_coupon_detail);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("优惠券详情");
        if (isCanGive == 0) {
            //设置分享的图片
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
            ViewGroup.LayoutParams layoutParams = ibTitlebarOther.getLayoutParams();
            layoutParams.width = Math.round(80 * 2 / 3 * density);
            layoutParams.height = Math.round(80 * 2 / 3 * density);
            ibTitlebarOther.setLayoutParams(layoutParams);
            ibTitlebarOther.setVisibility(View.VISIBLE);
            ibTitlebarOther.setBackgroundResource(R.drawable.icon_itemdetail_share);
        } else {
            ibTitlebarOther.setVisibility(View.GONE);
        }

    }


    private void initData() {
        id = getIntent().getIntExtra("id", 0);
        shareDesc = getIntent().getStringExtra("shareDesc");
        shareImg = getIntent().getStringExtra("shareImg");
        shareTitle = getIntent().getStringExtra("shareTitle");
        shareUrl = getIntent().getStringExtra("shareUrl");
        isCanGive = getIntent().getIntExtra("isCanGive", 0);
    }

    private void getData() {
        CommUtil.getCouponDetails(mContext, id, couponDetailHandler);
    }

    private void setAuthCode(final String authCode, final ImageView ivCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bingoogolapple = QRCodeEncoder.syncEncodeQRCode(authCode,
                        BGAQRCodeUtil.dp2px(CouponDetailActivity.this, 180), Color.BLACK, Color.WHITE,
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


    private AsyncHttpResponseHandler couponDetailHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        listOne.clear();
                        listTwo.clear();
                        listThree.clear();
                        listFour.clear();
                        Gson gson = new Gson();
                        CouponDetail couponDetail = gson.fromJson(new String(responseBody), CouponDetail.class);
                        CouponDetail.DataBean coupon = couponDetail.getData();
                        couponName = coupon.getName();
                        tvCouponName.setText(couponName);
                        tvCouponDesc.setText(coupon.getDescription());
                        tvCouponEnd.setText(coupon.getStartTime() + "-" + coupon.getEndTime());
                        //使用门店
                        CouponDetail.DataBean.ApplyShopBean applyShop = coupon.getApplyShop();
                        tvCouponUseshop.setText(applyShop.getDuction());
                        if (applyShop.getDuctionList() != null && applyShop.getDuctionList().size() > 0) {
                            listOne.addAll(applyShop.getDuctionList());
                            ivCouponShopmore.setVisibility(View.VISIBLE);
                            rlCouponChooseone.setClickable(true);
                        } else {
                            ivCouponShopmore.setVisibility(View.GONE);
                            rlCouponChooseone.setClickable(false);
                        }
                        if (coupon.getCanUseServiceCard() == 0) {//是否可与E卡同时使用
                            tvCouponUsewithcard.setText("是");
                        } else {
                            tvCouponUsewithcard.setText("否");
                        }
                        switch (coupon.getReduceType()) { //减免类型(1:减免券、2:折扣券、3:免单券)
                            case 1:
                                tvCouponFree.setVisibility(View.GONE);
                                rlCouponReduce.setVisibility(View.VISIBLE);
                                tvCouponReduce.setText(Utils.doubleTrans(coupon.getAmount()));
                                break;
                            case 2:
                                tvCouponFree.setVisibility(View.VISIBLE);
                                rlCouponReduce.setVisibility(View.GONE);
                                tvCouponFree.setText(coupon.getAmount() + "折");
                                break;
                            case 3:
                                tvCouponFree.setVisibility(View.VISIBLE);
                                rlCouponReduce.setVisibility(View.GONE);
                                tvCouponFree.setText("免单");
                                break;
                        }
                        switch (coupon.getCategory()) {
                            //1,2（服务券） 3,5,6（商品券）
                            //3 6：用券买单  其它不可用券买单
                            case 1:
                            case 2:
                                ivCouponType.setImageResource(R.drawable.icon_coupon_service);
                                tvCouponChangeoneLeft.setText("适用美容师");
                                rlCouponChoosetwo.setVisibility(View.VISIBLE);
                                vLine.setVisibility(View.VISIBLE);
                                tvCouponChangetwoLeft.setText("适用服务");
                                tvCouponChangethreeLeft.setText("适用宠物");
                                //适用美容师
                                CouponDetail.DataBean.ApplyWorkerBean applyWorker = coupon.getApplyWorker();
                                tvCouponChangeoneRight.setText(applyWorker.getDuction());
                                if (applyWorker.getDuctionList() != null && applyWorker.getDuctionList().size() > 0) {
                                    ivCouponBeautymore.setVisibility(View.VISIBLE);
                                    rlCouponChoosetwo.setClickable(true);
                                    titleTwo = "适用美容师";
                                    listTwo.addAll(applyWorker.getDuctionList());
                                } else {
                                    ivCouponBeautymore.setVisibility(View.GONE);
                                    rlCouponChoosetwo.setClickable(false);
                                }
                                //使用服务
                                CouponDetail.DataBean.ApplyServiceBean applyService = coupon.getApplyService();
                                tvCouponChangetwoRight.setText(applyService.getDuction());
                                if (applyService.getDuctionList() != null && applyService.getDuctionList().size() > 0) {
                                    ivCouponServicemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosethree.setClickable(true);
                                    titleThree = "适用服务";
                                    listThree.addAll(applyService.getDuctionList());
                                } else {
                                    ivCouponServicemore.setVisibility(View.GONE);
                                    rlCouponChoosethree.setClickable(false);
                                }
                                //适用宠物
                                CouponDetail.DataBean.ApplyPetBean applyPet = coupon.getApplyPet();
                                tvCouponChangethreeRight.setText(applyPet.getDuction());
                                if (applyPet.getDuctionList() != null && applyPet.getDuctionList().size() > 0) {
                                    ivCouponPetemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosefour.setClickable(true);
                                    titleFour = "适用宠物";
                                    listFour.addAll(applyPet.getDuctionList());
                                } else {
                                    ivCouponPetemore.setVisibility(View.GONE);
                                    rlCouponChoosefour.setClickable(false);
                                }
                                tvCouponFree.setTextColor(Color.parseColor("#FE7567"));
                                tvCouponRmb.setTextColor(Color.parseColor("#FE7567"));
                                tvCouponReduce.setTextColor(Color.parseColor("#FE7567"));
                                break;
                            case 5:
                                ivCouponType.setImageResource(R.drawable.icon_coupon_goods);
                                rlCouponChoosetwo.setVisibility(View.GONE);
                                vLine.setVisibility(View.GONE);
                                tvCouponChangetwoLeft.setText("适用商品");
                                tvCouponChangethreeLeft.setText("适用品牌");
                                tvCouponUseshop.setText(coupon.getApplyShop().getDuction());
                                //使用商品
                                CouponDetail.DataBean.ApplyCommodityBean applyCommodity = coupon.getApplyCommodity();
                                CouponDetail.DataBean.ApplyClassificationBean applyClassification = coupon.getApplyClassification();
                                if (applyCommodity.getDuctionList() != null && applyCommodity.getDuctionList().size() > 0) {
                                    tvCouponChangetwoRight.setText(applyCommodity.getDuction());
                                    ivCouponServicemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosethree.setClickable(true);
                                    titleThree = "适用商品";
                                    listThree.addAll(applyCommodity.getDuctionList());
                                } else if (applyClassification.getDuctionList() != null && applyClassification.getDuctionList().size() > 0) {
                                    tvCouponChangetwoRight.setText(applyClassification.getDuction());
                                    ivCouponServicemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosethree.setClickable(true);
                                    titleThree = "适用商品";
                                    listThree.addAll(applyClassification.getDuctionList());
                                } else if (applyCommodity.getDuctionList().size() == 0 && applyClassification.getDuctionList().size() == 0) {
                                    tvCouponChangetwoRight.setText(applyCommodity.getDuction());
                                    ivCouponServicemore.setVisibility(View.GONE);
                                    rlCouponChoosethree.setClickable(false);
                                } else {
                                    ivCouponServicemore.setVisibility(View.GONE);
                                    rlCouponChoosethree.setClickable(false);
                                }
                                //适用品牌
                                CouponDetail.DataBean.ApplyBrandBean applyBrand = coupon.getApplyBrand();
                                tvCouponChangethreeRight.setText(applyBrand.getDuction());
                                if (applyBrand.getDuctionList() != null && applyBrand.getDuctionList().size() > 0) {
                                    ivCouponPetemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosefour.setClickable(true);
                                    titleFour = "适用品牌";
                                    listFour.addAll(applyBrand.getDuctionList());
                                } else {
                                    ivCouponPetemore.setVisibility(View.GONE);
                                    rlCouponChoosefour.setClickable(false);
                                }
                                /*tvCouponChangetwoRight.setText(coupon.getApplyService());
                                tvCouponChangethreeRight.setText(coupon.getApplyPet());*/
                                tvCouponFree.setTextColor(Color.parseColor("#BB996C"));
                                tvCouponRmb.setTextColor(Color.parseColor("#BB996C"));
                                tvCouponReduce.setTextColor(Color.parseColor("#BB996C"));
                                break;
                            case 3:
                            case 6:
                                ivCouponType.setImageResource(R.drawable.icon_coupon_goods);
                                ivUsecouponBuy.setVisibility(View.VISIBLE);
                                rlCouponChoosetwo.setVisibility(View.GONE);
                                vLine.setVisibility(View.GONE);
                                tvCouponChangetwoLeft.setText("适用商品");
                                tvCouponChangethreeLeft.setText("适用品牌");
                                tvCouponUseshop.setText(coupon.getApplyShop().getDuction());
                                //使用商品
                                CouponDetail.DataBean.ApplyClassificationBean applyClassification1 = coupon.getApplyClassification();
                                CouponDetail.DataBean.ApplyCommodityBean applyCommodity1 = coupon.getApplyCommodity();
                                if (applyCommodity1.getDuctionList() != null && applyCommodity1.getDuctionList().size() > 0) {
                                    tvCouponChangetwoRight.setText(applyCommodity1.getDuction());
                                    ivCouponServicemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosethree.setClickable(true);
                                    titleThree = "适用商品";
                                    listThree.addAll(applyCommodity1.getDuctionList());
                                } else if (applyClassification1.getDuctionList() != null && applyClassification1.getDuctionList().size() > 0) {
                                    tvCouponChangetwoRight.setText(applyClassification1.getDuction());
                                    ivCouponServicemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosethree.setClickable(true);
                                    titleThree = "适用商品";
                                    listThree.addAll(applyClassification1.getDuctionList());
                                } else if (applyCommodity1.getDuctionList().size() == 0 && applyClassification1.getDuctionList().size() == 0) {
                                    tvCouponChangetwoRight.setText(applyCommodity1.getDuction());
                                    ivCouponServicemore.setVisibility(View.GONE);
                                    rlCouponChoosethree.setClickable(false);
                                } else {
                                    ivCouponServicemore.setVisibility(View.GONE);
                                    rlCouponChoosethree.setClickable(false);
                                }
                                //适用品牌
                                CouponDetail.DataBean.ApplyBrandBean applyBrand2 = coupon.getApplyBrand();
                                tvCouponChangethreeRight.setText(applyBrand2.getDuction());
                                if (applyBrand2.getDuctionList() != null && applyBrand2.getDuctionList().size() > 0) {
                                    ivCouponPetemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosefour.setClickable(true);
                                    titleFour = "适用品牌";
                                    listFour.addAll(applyBrand2.getDuctionList());
                                } else {
                                    ivCouponPetemore.setVisibility(View.GONE);
                                    rlCouponChoosefour.setClickable(false);
                                }
                                tvCouponFree.setTextColor(Color.parseColor("#BB996C"));
                                tvCouponRmb.setTextColor(Color.parseColor("#BB996C"));
                                tvCouponReduce.setTextColor(Color.parseColor("#BB996C"));
                                break;
                            case 7:
                                ivCouponType.setImageResource(R.drawable.icon_coupon_foster);
                                ivUsecouponBuy.setVisibility(View.GONE);
                                rlCouponChoosetwo.setVisibility(View.GONE);
                                vLine.setVisibility(View.GONE);
                                tvCouponChangetwoLeft.setText("适用房型");
                                tvCouponChangethreeLeft.setText("适用范围");
                                tvCouponUseshop.setText(coupon.getApplyShop().getDuction());
                                //适用房型
                                CouponDetail.DataBean.ApplyRoomTypeBean applyRoomType = coupon.getApplyRoomType();
                                tvCouponChangetwoRight.setText(applyRoomType.getDuction());
                                if (applyRoomType.getDuctionList() != null && applyRoomType.getDuctionList().size() > 0) {
                                    ivCouponServicemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosethree.setClickable(true);
                                    titleThree = "适用房型";
                                    listThree.addAll(applyRoomType.getDuctionList());
                                } else {
                                    ivCouponServicemore.setVisibility(View.GONE);
                                    rlCouponChoosethree.setClickable(false);
                                }
                                //适用范围
                                CouponDetail.DataBean.ApplyReduceTypeBean applyReduceType = coupon.getApplyReduceType();
                                tvCouponChangethreeRight.setText(applyReduceType.getDuction());
                                if (applyReduceType.getDuctionList() != null && applyReduceType.getDuctionList().size() > 0) {
                                    ivCouponPetemore.setVisibility(View.VISIBLE);
                                    rlCouponChoosefour.setClickable(true);
                                    titleFour = "适用范围";
                                    listFour.addAll(applyReduceType.getDuctionList());
                                } else {
                                    ivCouponPetemore.setVisibility(View.GONE);
                                    rlCouponChoosefour.setClickable(false);
                                }
                                tvCouponFree.setTextColor(Color.parseColor("#FC3962"));
                                tvCouponRmb.setTextColor(Color.parseColor("#FC3962"));
                                tvCouponReduce.setTextColor(Color.parseColor("#FC3962"));
                                break;
                        }
                        if (coupon.getIsToExpire() == 0) {//0非即将过期 1即将过期
                            tvCouponWillend.setVisibility(View.GONE);
                        } else {
                            tvCouponWillend.setVisibility(View.VISIBLE);
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

    private void showReasonPop(String title, List<String> list) {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(CouponDetailActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(CouponDetailActivity.this, R.layout.pop_common_withbutton, null);
        TextView tvPopTitle = customView.findViewById(R.id.tv_commonpop_title);
        RecyclerView rvPopRecycler = customView.findViewById(R.id.rv_commonpop_list);
        Button btnSure = customView.findViewById(R.id.btn_commonpop_sure);
        RelativeLayout rvDemiss = customView.findViewById(R.id.rl_commonpop_dimiss);
        tvPopTitle.setText(title);
        rvPopRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        CouponDetailTipAdapter adapter = new CouponDetailTipAdapter(mContext, list);
        rvPopRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(CouponDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        rvDemiss.setOnClickListener(new View.OnClickListener() {
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
    }

    private void setListener() {

    }

    private void getQRCode(int id) {
        mPDialog.showDialog();
        CommUtil.getauthCode(mContext, 3, id, getCodeHandler);
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

        }
    };

    private AsyncHttpResponseHandler shareHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void showQRPop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        View customView = View.inflate(mContext, R.layout.common_qrcode_pop, null);
        RelativeLayout rlDemiss = customView.findViewById(R.id.rl_pop_dimess);
        ImageView ivCode = customView.findViewById(R.id.iv_qrpop_qrcode);
        TextView tvPopTitle = customView.findViewById(R.id.tv_qrpop_conponname);
        ImageView ivEcard = customView.findViewById(R.id.iv_qrcodepop_bottom);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点 
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失 
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击 
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画 
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        tvPopTitle.setText(couponName);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(CouponDetailActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        setAuthCode(codeMsg, ivCode);
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
                startActivity(new Intent(CouponDetailActivity.this, QrCodeNewActivity.class));
                pWinBottomDialog.dismiss();
            }
        });

    }

    @OnClick({R.id.ib_titlebar_back, R.id.iv_usecoupon_buy, R.id.rl_coupon_chooseone, R.id.rl_coupon_choosetwo, R.id.rl_coupon_choosethree, R.id.rl_coupon_choosefour, R.id.ib_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.iv_usecoupon_buy:
                getQRCode(id);
                break;
            case R.id.rl_coupon_chooseone:
                showReasonPop("适用门店", listOne);
                break;
            case R.id.rl_coupon_choosetwo:
                showReasonPop(titleTwo, listTwo);
                break;
            case R.id.rl_coupon_choosethree:
                showReasonPop(titleThree, listThree);
                break;
            case R.id.rl_coupon_choosefour:
                showReasonPop(titleFour, listFour);
                break;
            case R.id.ib_titlebar_other:
                Utils.share(CouponDetailActivity.this, shareImg, shareTitle, shareDesc, shareUrl, "1,2", 0,null);
                Utils.mLogError(shareUrl);
                CommUtil.couponShare(mContext, id, shareHandler);
                break;
        }
    }
}
