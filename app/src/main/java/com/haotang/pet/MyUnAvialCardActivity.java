package com.haotang.pet;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyCardAdapter;
import com.haotang.pet.entity.MyCard;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

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
 * 我的不可用卡包列表
 */
public class MyUnAvialCardActivity extends SuperActivity {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_myunavailcard)
    RecyclerView rvMyunavailcard;
    private List<MyCard> list = new ArrayList<MyCard>();
    private MyCardAdapter myCardAdapter;

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
    }

    private void findView() {
        setContentView(R.layout.activity_my_un_avial_card);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("无效卡");
        rvMyunavailcard.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = -DensityUtil.dp2px(mContext, 125);
            }
        });
        rvMyunavailcard.setHasFixedSize(true);
        rvMyunavailcard.setLayoutManager(new LinearLayoutManager(this));
        myCardAdapter = new MyCardAdapter(R.layout.item_mycard, list, 2);
        rvMyunavailcard.setAdapter(myCardAdapter);
    }

    private void setLinster() {
    }

    private void myServiceCardList() {
        mPDialog.showDialog();
        list.clear();
        CommUtil.myServiceCardList(this, 1, cardListHandler);
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
                                    myCard.setCardType(2);
                                    list.add(myCard);
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
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isLast()) {
                        list.remove(i);
                        break;
                    }
                }
                list.add(new MyCard(true));
            } else {
                myCardAdapter.setEmptyView(setEmptyViewBase(2, "暂无无效卡", null));
            }
            myCardAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }
}
