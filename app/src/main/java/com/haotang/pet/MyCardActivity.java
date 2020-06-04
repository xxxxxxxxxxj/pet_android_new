package com.haotang.pet;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyCardAdapter;
import com.haotang.pet.entity.AppointRechargeEvent;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.makeramen.roundedimageview.RoundedImageView;
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
 * 我的E卡列表页
 */
public class MyCardActivity extends SuperActivity {
    @BindView(R.id.rv_mycard)
    RecyclerView rvMycard;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.ll_mycard_emptyview)
    LinearLayout ll_mycard_emptyview;
    @BindView(R.id.ll_teethcard_emptyview)
    LinearLayout ll_teethcard_emptyview;
    @BindView(R.id.rv_myteethcard)
    RecyclerView rv_myteethcard;
    @BindView(R.id.iv_mycard_bg)
    RoundedImageView iv_mycard_bg;
    @BindView(R.id.tv_mycard_amount)
    TextView tv_mycard_amount;
    @BindView(R.id.tv_mycard_name)
    TextView tv_mycard_name;
    @BindView(R.id.tv_mycard_discounttext)
    TextView tv_mycard_discounttext;
    private List<MyCard> list = new ArrayList<MyCard>();
    private List<MyCard> teethCardList = new ArrayList<MyCard>();
    private MyCardAdapter myCardAdapter;
    private double accountAmount;
    private StringBuffer stringBuffer = new StringBuffer();
    private int balanceCardId;
    private String iCardBuyUrl;
    private MyCardAdapter myTeethCardAdapter;

    //绑卡，买卡，或者退卡回来
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointRechargeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.isRecharge()) {
                myServiceCardList();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        myServiceCardList();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    private void findView() {
        setContentView(R.layout.activity_my_card);
        ButterKnife.bind(this);
    }

    private void setView() {
        rvMycard.setHasFixedSize(true);
        rvMycard.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvMycard.setLayoutManager(noScollFullLinearLayoutManager);
        myCardAdapter = new MyCardAdapter(R.layout.item_mycard, list, 0);
        rvMycard.setAdapter(myCardAdapter);

        rv_myteethcard.setHasFixedSize(true);
        rv_myteethcard.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager1 = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager1.setScrollEnabled(false);
        rv_myteethcard.setLayoutManager(noScollFullLinearLayoutManager1);
        myTeethCardAdapter = new MyCardAdapter(R.layout.item_mycard, teethCardList, 1);
        rv_myteethcard.setAdapter(myTeethCardAdapter);

        rvMycard.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = -DensityUtil.dp2px(mContext, 125);
            }
        });
        rv_myteethcard.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = -DensityUtil.dp2px(mContext, 125);
            }
        });
    }

    private void setLinster() {
    }

    private void myServiceCardList() {
        mPDialog.showDialog();
        stringBuffer.setLength(0);
        list.clear();
        teethCardList.clear();
        CommUtil.myServiceCardList(this, 0, cardListHandler);
    }

    private AsyncHttpResponseHandler cardListHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("iCardBuyUrl") && !jdata.isNull("iCardBuyUrl")) {
                            iCardBuyUrl = jdata.getString("iCardBuyUrl");
                        }
                        if (jdata.has("userAccount") && !jdata.isNull("userAccount")) {
                            JSONObject juserAccount = jdata.getJSONObject("userAccount");
                            if (juserAccount.has("accountName") && !juserAccount.isNull("accountName")) {
                                Utils.setText(tv_mycard_name, juserAccount.getString("accountName"), "", View.VISIBLE, View.VISIBLE);
                            }
                            if (juserAccount.has("dicountDesc") && !juserAccount.isNull("dicountDesc")) {
                                JSONArray jarrdicountDesc = juserAccount.getJSONArray("dicountDesc");
                                if (jarrdicountDesc != null && jarrdicountDesc.length() > 0) {
                                    for (int i = 0; i < jarrdicountDesc.length(); i++) {
                                        if (i == jarrdicountDesc.length() - 1) {
                                            stringBuffer.append(jarrdicountDesc.getString(i));
                                        } else {
                                            stringBuffer.append(jarrdicountDesc.getString(i) + "|");
                                        }
                                    }
                                    if (stringBuffer.toString().contains("@@")) {
                                        String[] split = stringBuffer.toString().split("@@");
                                        if (split != null && split.length > 0 && split.length % 2 != 0) {
                                            SpannableString ss = new SpannableString(stringBuffer.toString().replace("@@", ""));
                                            if (split.length == 3) {
                                                int startIndex = split[0].length();
                                                int endIndex = split[0].length() + split[1].length();
                                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), startIndex,
                                                        endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                            } else if (split.length == 5) {
                                                int startIndex = split[0].length();
                                                int endIndex = startIndex + split[1].length();
                                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), startIndex,
                                                        endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                                int startIndex1 = endIndex + split[2].length();
                                                int endIndex1 = startIndex1 + split[3].length();
                                                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), startIndex1,
                                                        endIndex1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex1, endIndex1,
                                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                            }
                                            tv_mycard_discounttext.setText(ss);
                                        }
                                    }
                                }
                            }
                            if (juserAccount.has("accountAmount") && !juserAccount.isNull("accountAmount")) {
                                accountAmount = juserAccount.getDouble("accountAmount");
                                SpannableString ss1 = new SpannableString("¥" + accountAmount);
                                ss1.setSpan(new TextAppearanceSpan(mContext, R.style.style2), 1,
                                        ss1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                tv_mycard_amount.setText(ss1);
                            }
                            if (juserAccount.has("mineCardPic") && !juserAccount.isNull("mineCardPic")) {
                                GlideUtil.loadImg(mContext, juserAccount.getString("mineCardPic"), iv_mycard_bg, R.drawable.icon_production_default);
                            }
                            if (juserAccount.has("serviceCardId") && !juserAccount.isNull("serviceCardId")) {
                                balanceCardId = juserAccount.getInt("serviceCardId");
                            }
                        }
                        if (jdata.has("userServiceCardTemplateList") && !jdata.isNull("userServiceCardTemplateList")) {
                            JSONArray jarrCard = jdata.getJSONArray("userServiceCardTemplateList");
                            if (jarrCard.length() > 0) {
                                for (int i = 0; i < jarrCard.length(); i++) {
                                    MyCard myCard = MyCard.json2Entity(jarrCard
                                            .getJSONObject(i));
                                    myCard.setCardType(1);
                                    list.add(myCard);
                                }
                            }
                        }
                        if (jdata.has("userExtraItemCardList") && !jdata.isNull("userExtraItemCardList")) {
                            JSONArray jarrCard = jdata.getJSONArray("userExtraItemCardList");
                            if (jarrCard.length() > 0) {
                                for (int i = 0; i < jarrCard.length(); i++) {
                                    MyCard myCard = new MyCard();
                                    myCard.setCardType(2);
                                    JSONObject jsonObject1 = jarrCard.getJSONObject(i);
                                    if (jsonObject1.has("cardName") && !jsonObject1.isNull("cardName")) {
                                        myCard.setCardTypeName(jsonObject1.getString("cardName"));
                                    }
                                    if (jsonObject1.has("petText") && !jsonObject1.isNull("petText")) {
                                        myCard.setDicountDesc(jsonObject1.getString("petText"));
                                    }
                                    if (jsonObject1.has("expireTime") && !jsonObject1.isNull("expireTime")) {
                                        myCard.setExpireTime(jsonObject1.getString("expireTime"));
                                    }
                                    if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                        myCard.setId(jsonObject1.getInt("id"));
                                    }
                                    if (jsonObject1.has("state") && !jsonObject1.isNull("state")) {
                                        myCard.setState(jsonObject1.getString("state"));
                                    }
                                    if (jsonObject1.has("mineCardPic") && !jsonObject1.isNull("mineCardPic")) {
                                        myCard.setMineCardPic(jsonObject1.getString("mineCardPic"));
                                    }
                                    teethCardList.add(myCard);
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortCenter(mContext, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortCenter(mContext, "数据异常");
                e.printStackTrace();
            }
            if (list.size() > 0) {
                rvMycard.setVisibility(View.VISIBLE);
                ll_mycard_emptyview.setVisibility(View.GONE);
            } else {
                rvMycard.setVisibility(View.GONE);
                ll_mycard_emptyview.setVisibility(View.VISIBLE);
            }
            if (teethCardList.size() > 0) {
                rv_myteethcard.setVisibility(View.VISIBLE);
                ll_teethcard_emptyview.setVisibility(View.GONE);
                for (int i = 0; i < teethCardList.size(); i++) {
                    if (teethCardList.get(i).isLast()) {
                        teethCardList.remove(i);
                        break;
                    }
                }
                teethCardList.add(new MyCard(true));
            } else {
                rv_myteethcard.setVisibility(View.GONE);
                ll_teethcard_emptyview.setVisibility(View.VISIBLE);
            }
            myCardAdapter.notifyDataSetChanged();
            myTeethCardAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortCenter(mContext, "请求失败");
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

    @OnClick({R.id.iv_mycard_titlebar_back, R.id.iv_mycard_titlebar_ewm, R.id.ll_mycard_bd, R.id.ll_mycard_buycard,
            R.id.iv_mycard_dhm, R.id.tv_teethcard_buy, R.id.tv_teethcard_bky, R.id.ll_mycard_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_mycard_dhm:
                startActivity(new Intent(mContext, BalanceExchangeActivity.class));
                break;
            case R.id.ll_mycard_info:
                startActivity(new Intent(mContext, BalanceCareUseDetailActivity.class).putExtra("id", balanceCardId));
                break;
            case R.id.tv_teethcard_buy:
                startActivity(new Intent(mContext, ADActivity.class).putExtra("url", iCardBuyUrl));
                break;
            case R.id.tv_teethcard_bky:
                startActivity(new Intent(mContext, MyUnAvialCardActivity.class));
                break;
            case R.id.iv_mycard_titlebar_back:
                finish();
                break;
            case R.id.iv_mycard_titlebar_ewm:
                if (list.size() > 0 || accountAmount > 0) {
                    startActivity(new Intent(mContext, QrCodeNewActivity.class));
                } else {
                    new AlertDialogNavAndPost(this).builder().setTitle("").setMsg("暂无可用E卡，无法使用二维码付款！")
                            .setNegativeButtonVisible(View.GONE)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.ll_mycard_bd:
                startActivity(new Intent(MyCardActivity.this, BindingCardActivity.class));
                break;
            case R.id.ll_mycard_buycard:
                startActivity(new Intent(MyCardActivity.this, GiftCardListActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
