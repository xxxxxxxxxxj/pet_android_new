package com.haotang.pet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CouponNewAdapter;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SoftKeyBoardListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class MyCouponNewActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.editText_write_num)
    EditText editTextWriteNum;
    @BindView(R.id.button_put_code)
    Button buttonPutCode;
    @BindView(R.id.ll_mycoupon_top)
    LinearLayout llMycouponTop;
    @BindView(R.id.listView_show_mycoupon)
    PullToRefreshListView listViewShowMycoupon;
    @BindView(R.id.rl_coupon_history)
    RelativeLayout rlCouponHistory;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    public static MyCouponNewActivity myCouponActivity;
    @BindView(R.id.rl_coupon_black)
    RelativeLayout rlCouponBlack;
    private ListView listView = null;
    private View footer;
    private static ArrayList<MyCouponCanUse> MyCouponCanUseList = new ArrayList<MyCouponCanUse>();
    private static ArrayList<MyCouponCanUse> LastCanUseList = new ArrayList<MyCouponCanUse>();
    private static int page = 1;
    private static double pageSize = 10;
    private int couponId;
    private String shareDesc;
    private String shareUrl;
    private String shareTitle;
    private String shareImg;
    private static CouponNewAdapter adapter;
    private String codeMsg;
    private String couponName;

    private void setAuthCode(final String authCode, final ImageView ivCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bingoogolapple = QRCodeEncoder.syncEncodeQRCode(authCode,
                        BGAQRCodeUtil.dp2px(MyCouponNewActivity.this, 180), Color.BLACK, Color.WHITE,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCouponCanUseList.clear();
        setView();
        setCouponData();
        page=1;
        getAllCoupons(1);
        myCouponActivity = this;
        setListener();
    }

    private void setListener() {
        adapter.setBuyWithCouponListener(new CouponNewAdapter.BuyWithCouponListener() {
            @Override
            public void OnCounponBuy(int id, String name) {
                couponName = name;
                getQRCode(id);

            }

        });
        adapter.setOnClickListener(new CouponNewAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (MyCouponCanUseList.size() > 0 && position >= 0 && MyCouponCanUseList.size() > position) {
                    MyCouponCanUse myCouponCanUse = MyCouponCanUseList.get(position);
                    if (myCouponCanUse != null) {
                        couponId = myCouponCanUse.CouponId;
                        shareDesc = myCouponCanUse.shareDesc;
                        shareUrl = myCouponCanUse.shareUrl + myCouponCanUse.CouponId;
                        shareTitle = myCouponCanUse.shareTitle;
                        shareImg = myCouponCanUse.shareImg;
                        Intent intent = new Intent(MyCouponNewActivity.this, CouponDetailActivity.class);
                        intent.putExtra("id", couponId);
                        intent.putExtra("shareDesc",shareDesc);
                        intent.putExtra("shareUrl",shareUrl);
                        intent.putExtra("shareTitle",shareTitle);
                        intent.putExtra("shareImg",shareImg);
                        intent.putExtra("isCanGive",myCouponCanUse.isCanGive);
                        startActivity(intent);
                    }
                }

            }
        });
        listViewShowMycoupon.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
                    // 下拉刷新
                    page = 1;
                    MyCouponCanUseList.clear();
                    adapter.notifyDataSetChanged();
                    getAllCoupons(page);
                } else {
                    try {
                        if (MyCouponCanUseList.size() > 0 && LastCanUseList.size() < pageSize) {
                            ArrayList<MyCouponCanUse> arrayList = getNewList(MyCouponCanUseList);
                            MyCouponCanUseList.clear();
                            MyCouponCanUseList = arrayList;
                            page = page - 1;
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getAllCoupons(page);
                }
            }
        });

        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                rlCouponBlack.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                rlCouponBlack.setVisibility(View.GONE);
            }
        });
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
            mPDialog.dimissDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void setView() {
        setContentView(R.layout.activity_my_coupon_new);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("我的优惠券");
        footer = LayoutInflater.from(this).inflate(R.layout.footer_my_coupon, null);
        listViewShowMycoupon.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void setCouponData() {
        adapter = new CouponNewAdapter(MyCouponNewActivity.this, MyCouponCanUseList);
        listView = listViewShowMycoupon.getRefreshableView();
        footer.setVisibility(View.GONE);
        listView.addFooterView(footer);
        listViewShowMycoupon.setAdapter(adapter);
    }

    private void getAllCoupons(int page) {
        mPDialog.showDialog();
        CommUtil.getMyCouponAll(
                SharedPreferenceUtil.getInstance(MyCouponNewActivity.this)
                        .getString("cellphone", "0"), Global
                        .getIMEI(MyCouponNewActivity.this), MyCouponNewActivity.this, page, 0,
                queryUserCoupons);
    }

    private AsyncHttpResponseHandler queryUserCoupons = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            listViewShowMycoupon.onRefreshComplete();
            footer.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code != 0) {
                    ToastUtil.showToastShortBottom(mContext, jsonObject.getString("msg"));
                }
                if (code == 0 && !jsonObject.has("data") && MyCouponCanUseList.size() > 0) {
                    footer.setVisibility(View.VISIBLE);//翻页无卷
                }
                if (code == 0 && !jsonObject.has("data") && MyCouponCanUseList.size() <= 0) {
                    //初次无卷
                    footer.setVisibility(View.GONE);
                    listView.removeFooterView(footer);

                }
                if (code == 0 && jsonObject.has("data") && !jsonObject.isNull("data")) {
                    JSONObject jdata = jsonObject.getJSONObject("data");
                    if (jdata.has("coupons") && !jdata.isNull("coupons")) {
                        JSONArray jsonArray = jdata.getJSONArray("coupons");
                        if (jsonArray.length() > 0) {
                            LastCanUseList.clear();
                            page++;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MyCouponCanUseList.add(MyCouponCanUse.json2Entity(jsonArray.getJSONObject(i)));
                                pageSize = MyCouponCanUseList.get(0).pageSize;
                                LastCanUseList.add(MyCouponCanUse.json2Entity(jsonArray.getJSONObject(i)));
                            }
                        }
                        Utils.mLogError("==-->jsonArray.length():= " + jsonArray.length());
                        adapter.notifyDataSetChanged();
                        if (page == 1) {
                            if (MyCouponCanUseList.size() <= 0) {
                                listViewShowMycoupon.setVisibility(View.GONE);
                                listView.removeFooterView(footer);
                                //layout_find_time_out.setVisibility(View.VISIBLE);
                            }
                        } else {
                            //layout_find_time_out.setVisibility(View.GONE);
                        }
                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            listViewShowMycoupon.onRefreshComplete();
            listView.removeFooterView(footer);
        }
    };
    private AsyncHttpResponseHandler redeemCouponCode = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    ToastUtil.showToastShortCenter(MyCouponNewActivity.this, jsonObject.getString("msg"));
                    MyCouponCanUseList.clear();
                    page = 1;
                    getAllCoupons(page);
                } else {
                    editTextWriteNum.setText("");
                    ToastUtil.showToastShortCenter(MyCouponNewActivity.this, jsonObject.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    public ArrayList<MyCouponCanUse> getNewList(ArrayList<MyCouponCanUse> li) {
        ArrayList<MyCouponCanUse> list = new ArrayList<MyCouponCanUse>();
        for (int i = 0; i < li.size(); i++) {
            long couponid = li.get(i).CouponId;  //获取传入集合对象的每一个元素
            if (list.get(i).CouponId != couponid) {   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(list.get(i));
            }
        }
        return list;  //返回集合
    }

    public static void delteCoupon(int removePosition) {
        page = 1;
        LastCanUseList.clear();
        if (removePosition != -1) {
            MyCouponCanUseList.remove(removePosition);
            ToastUtil.showToastShortCenter(myCouponActivity, "优惠券已赠送给您的好友");
        }
        double lastSize = MyCouponCanUseList.size() % pageSize;
        double countPage = 0;
        if (lastSize == 0) {
            countPage = Math.ceil(MyCouponCanUseList.size() / pageSize);
            page = (int) (countPage + 1);
            page += 1;
        } else {
            countPage = Math.ceil(MyCouponCanUseList.size() / pageSize);
            page = (int) countPage;
            page += 1;
        }
        Utils.mLogError("==-->lastSize 0:= " + lastSize + " countPage:= " + countPage + "  list:= " + MyCouponCanUseList.size());
        for (int i = (int) (MyCouponCanUseList.size() - LastCanUseList.size()); i < MyCouponCanUseList.size(); i++) {
            LastCanUseList.add(MyCouponCanUseList.get(i));
        }
        Utils.mLogError("==-->lastSize 1:= " + LastCanUseList.size());
        Utils.mLogError("==-->lastSize 2:= " + page);
        adapter.notifyDataSetChanged();
    }

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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(MyCouponNewActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        tvPopTitle.setText("优惠券名称："+couponName);
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
                startActivity(new Intent(MyCouponNewActivity.this, MyCardActivity.class));
                pWinBottomDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.ib_titlebar_back, R.id.button_put_code, R.id.rl_coupon_history,R.id.rl_coupon_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.button_put_code:
                if (TextUtils.isEmpty(editTextWriteNum.getText())) {
                    ToastUtil.showToastShortCenter(mContext, "请输入兑换码");
                    return;
                }
                CommUtil.redeemCouponCode(SharedPreferenceUtil.getInstance(MyCouponNewActivity.this)
                        .getString("cellphone", "0"), Global.getIMEI(MyCouponNewActivity.this), MyCouponNewActivity.this, editTextWriteNum.getText().toString(), redeemCouponCode);
                break;
            case R.id.rl_coupon_history:
                Intent intent = new Intent(MyCouponNewActivity.this, MyTimeOutCouponActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_coupon_black:
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editTextWriteNum.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }
}
