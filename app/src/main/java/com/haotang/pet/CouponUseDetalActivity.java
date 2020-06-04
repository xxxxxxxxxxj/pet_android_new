package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.UnUseCouponDetailAdapter;
import com.haotang.pet.adapter.UseCouponDetailAdapter;
import com.haotang.pet.entity.CardOrderDetail;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.MListview;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CouponUseDetalActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_couponuse_allnum)
    TextView tvCouponuseAllnum;
    @BindView(R.id.tv_couponuse_usenum)
    TextView tvCouponuseUsenum;
    @BindView(R.id.tv_couponuse_money)
    TextView tvCouponuseMoney;
    @BindView(R.id.rv_couponuse_used)
    MListview rvCouponuseUsed;
    @BindView(R.id.ll_couponuse_used)
    LinearLayout llCouponuseUsed;
    @BindView(R.id.ll_couponuse_nouse)
    LinearLayout llCouponuseNouse;
    @BindView(R.id.rl_couponuse_null)
    RelativeLayout rlCouponuseNull;
    @BindView(R.id.tv_couponuse_null)
    TextView tvCouponuseNull;
    @BindView(R.id.tv_couponuse_nouse)
    TextView tvCouponuseNouse;
    @BindView(R.id.rv_couponuse_nouse)
    MListview rvCouponuseNouse;
    private ArrayList<MyCouponCanUse> usedCoupon = new ArrayList<>();
    private ArrayList<MyCouponCanUse> unUsedCoupon = new ArrayList<>();
    private int id;
    private int couponSize = 0;
    private UseCouponDetailAdapter useCouponDetailAdapter;
    private UnUseCouponDetailAdapter unUseCouponDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initData();
        setView();
        getData();
    }


    private void setView() {
        tvTitlebarTitle.setText("优惠券使用明细");
        useCouponDetailAdapter = new UseCouponDetailAdapter(mContext, usedCoupon);
        rvCouponuseUsed.setAdapter(useCouponDetailAdapter);
        unUseCouponDetailAdapter = new UnUseCouponDetailAdapter(mContext, unUsedCoupon,2);
        rvCouponuseNouse.setAdapter(unUseCouponDetailAdapter);

    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_coupon_use_detal);
        ButterKnife.bind(this);
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.myServiceCardOrderDetail(this, id, cardDetailHandler);
    }

    private AsyncHttpResponseHandler cardDetailHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    Gson gson = new Gson();
                    CardOrderDetail cardOrderDetail = gson.fromJson(new String(responseBody), CardOrderDetail.class);
                    //未使用过的优惠券
                    if (jdata.has("serviceCard") && !jdata.isNull("serviceCard")) {
                        JSONObject cardJobj = jdata.getJSONObject("serviceCard");
                        if (cardJobj.has("couponInfo") && !cardJobj.isNull("couponInfo")) {
                            JSONObject couponJobj = cardJobj.getJSONObject("couponInfo");
                            if (couponJobj.has("unUsedList") && !couponJobj.isNull("unUsedList")) {
                                JSONArray unUsedList = couponJobj.getJSONArray("unUsedList");
                                if (unUsedList.length() > 0) {
                                    for (int i = 0; i < unUsedList.length(); i++) {
                                        unUsedCoupon.add(MyCouponCanUse.json2Entity(unUsedList.getJSONObject(i)));
                                        couponSize++;
                                    }
                                    llCouponuseNouse.setVisibility(View.VISIBLE);
                                    unUseCouponDetailAdapter.notifyDataSetChanged();
                                } else {
                                    llCouponuseNouse.setVisibility(View.GONE);
                                }
                            }
                            //使用过的优惠券
                            if (couponJobj.has("usedList") && !couponJobj.isNull("usedList")) {
                                JSONArray usedList = couponJobj.getJSONArray("usedList");
                                if (usedList.length() > 0) {
                                    for (int i = 0; i < usedList.length(); i++) {
                                        usedCoupon.add(MyCouponCanUse.json2Entity(usedList.getJSONObject(i)));
                                        couponSize++;
                                    }
                                    llCouponuseUsed.setVisibility(View.VISIBLE);
                                    useCouponDetailAdapter.notifyDataSetChanged();
                                } else {
                                    llCouponuseUsed.setVisibility(View.GONE);
                                }
                            }
                            if (couponSize==0){
                                rlCouponuseNull.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if (cardOrderDetail != null) {
                        CardOrderDetail.DataBean cardOrderDetailData = cardOrderDetail.getData();
                        if (cardOrderDetailData != null) {
                            CardOrderDetail.DataBean.ServiceCardBean serviceCard = cardOrderDetailData.getServiceCard();
                            if (serviceCard != null) {
                                CardOrderDetail.DataBean.ServiceCardBean.CouponInfoBean couponInfo = serviceCard.getCouponInfo();
                                if (couponInfo != null) {
                                    tvCouponuseAllnum.setText(couponInfo.getCouponNum() + "");
                                    tvCouponuseUsenum.setText(couponInfo.getUsedNum() + "");
                                    tvCouponuseMoney.setText(couponInfo.getTotalAmount() + "");
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                    rlCouponuseNull.setVisibility(View.VISIBLE);
                    tvCouponuseNull.setText(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            rlCouponuseNull.setVisibility(View.VISIBLE);
            tvCouponuseNull.setText("请求失败");
        }
    };

    @OnClick(R.id.ib_titlebar_back)
    public void onViewClicked() {
        finish();
    }
}
