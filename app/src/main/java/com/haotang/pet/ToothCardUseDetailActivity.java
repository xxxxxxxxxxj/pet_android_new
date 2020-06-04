package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ToothUseDetailAdapter;
import com.haotang.pet.entity.ToothCardUse;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToothCardUseDetailActivity extends SuperActivity {

    @BindView(R.id.iv_thoothcard_back)
    ImageView ivThoothcardBack;
    @BindView(R.id.tv_thoothcard_usetitle)
    TextView tvThoothcardUsetitle;
    @BindView(R.id.tv_thoothcard_useexplain)
    TextView tvThoothcardUseexplain;
    @BindView(R.id.tv_toothcard_usename)
    TextView tvToothcardUsename;
    @BindView(R.id.tv_thoothcard_usedata)
    TextView tvThoothcardUsedata;
    @BindView(R.id.tv_toothcard_usenum)
    TextView tvToothcardUsenum;
    @BindView(R.id.tv_thoothcard_usedmoney)
    TextView tvThoothcardUsedmoney;
    @BindView(R.id.rv_thoothcard_uselist)
    RecyclerView rvThoothcardUselist;
    @BindView(R.id.rl_thoothcard_usetop)
    RelativeLayout rlThoothcardUsetop;
    @BindView(R.id.iv_emptyview_img)
    ImageView ivEmptyviewImg;
    @BindView(R.id.tv_emptyview_desc)
    TextView tvEmptyviewDesc;
    @BindView(R.id.btn_emptyview)
    Button btnEmptyview;
    @BindView(R.id.srl_toothcard)
    SwipeRefreshLayout srlToothcard;
    @BindView(R.id.tv_thoothcard_bindpet)
    TextView tvThoothcardBindpet;
    @BindView(R.id.ll_toothorder_default)
    LinearLayout llThoothEmpty;
    @BindView(R.id.iv_toothcard_icon)
    ImageView ivThoothIcon;
    private int cardId;
    private int page = 1;
    private boolean isAdd = true;
    private List<ToothCardUse.DataBean.DatasetBean.ListBean> list = new ArrayList<>();
    private ToothUseDetailAdapter useDetailAdapter;
    private StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration;
    private String cardPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        initWindows();
        refresh();
        setListener();
    }

    private void setListener() {
        srlToothcard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        page = 1;
        list.clear();
        srlToothcard.setRefreshing(true);
        queryTradeHistory();
    }

    private void queryTradeHistory() {
        mPDialog.showDialog();
        CommUtil.extraCardTradeHistory(mContext, cardId, page, tradeHistoryHandler);
    }

    private AsyncHttpResponseHandler tradeHistoryHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            srlToothcard.setRefreshing(false);
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("card") && !jdata.isNull("card")) {
                            JSONObject jcard = jdata.getJSONObject("card");
                            if (jcard.has("cardName") && !jcard.isNull("cardName")) {
                                tvToothcardUsename.setText(jcard.getString("cardName"));
                            }
                            if (jcard.has("petText") && !jcard.isNull("petText")) {
                                tvThoothcardBindpet.setText(jcard.getString("petText"));
                            }
                            if (jcard.has("expireTime") && !jcard.isNull("expireTime")) {
                                tvThoothcardUsedata.setText(jcard.getString("expireTime"));
                            }
                            if (jcard.has("orderAmount") && !jcard.isNull("orderAmount")) {
                                tvToothcardUsenum.setText(jcard.getInt("orderAmount") + "");
                            }
                            if (jcard.has("orderDiscountPrice") && !jcard.isNull("orderDiscountPrice")) {
                                tvThoothcardUsedmoney.setText(jcard.getDouble("orderDiscountPrice") + "");
                            }
                            if (jcard.has("mineCardPic")&&!jcard.isNull("mineCardPic")){
                                GlideUtil.loadImg(mContext, jcard.getString("mineCardPic"), ivThoothIcon, R.drawable.icon_production_default);
                            }
                        }
                        Gson gson = new Gson();
                        ToothCardUse toothCardUse = gson.fromJson(new String(responseBody), ToothCardUse.class);
                        List<ToothCardUse.DataBean.DatasetBean> dataset = toothCardUse.getData().getDataset();
                        if (dataset != null && dataset.size() > 0) {
                            for (int i = 0; i < dataset.size(); i++) {
                                ToothCardUse.DataBean.DatasetBean datasetBean = dataset.get(i);
                                if (datasetBean != null) {
                                    List<ToothCardUse.DataBean.DatasetBean.ListBean> beanList = datasetBean.getList();
                                    for (int j = 0; j < beanList.size(); j++) {
                                        ToothCardUse.DataBean.DatasetBean.ListBean listBean = beanList.get(j);
                                        if (listBean != null) {
                                            listBean.setData(datasetBean.getDate());
                                        }
                                        list.add(listBean);
                                    }
                                }

                            }
                            //区域悬停用HeaderId
                            for (int j = 0; j < list.size(); j++) {
                                if (j == 0) {
                                    list.get(j).setHeaderId(j);
                                } else {
                                    if (list.get(j - 1).getData().equals(list.get(j).getData())) {
                                        list.get(j).setHeaderId(list.get(j - 1).getHeaderId());
                                    } else {
                                        list.get(j).setHeaderId(j);
                                    }
                                }
                            }

                            useDetailAdapter.setList(list);
                        } else {
                            if (page > 1) {
                                llThoothEmpty.setVisibility(View.GONE);
                            } else {
                                llThoothEmpty.setVisibility(View.VISIBLE);
                                tvEmptyviewDesc.setText("暂无使用明细");
                                btnEmptyview.setVisibility(View.GONE);
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
            if (page > 1) {
                llThoothEmpty.setVisibility(View.GONE);
            } else {
                llThoothEmpty.setVisibility(View.VISIBLE);
                tvEmptyviewDesc.setText("网络已断开，请检查网络");
                btnEmptyview.setText("重新加载");
            }
        }
    };

    private void initData() {
        Intent intent = getIntent();
        cardId = intent.getIntExtra("id", 0);
        cardPic = intent.getStringExtra("mineCardPic");
    }

    private void setView() {
        setContentView(R.layout.activity_tooth_card_use_detail);
        ButterKnife.bind(this);
        useDetailAdapter = new ToothUseDetailAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvThoothcardUselist.setLayoutManager(layoutManager);
        rvThoothcardUselist.setAdapter(useDetailAdapter);
        stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(useDetailAdapter);
        rvThoothcardUselist.addItemDecoration(stickyRecyclerHeadersDecoration);

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

    @OnClick({R.id.iv_thoothcard_back, R.id.tv_thoothcard_useexplain, R.id.btn_emptyview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_thoothcard_back:
                finish();
                break;
            case R.id.tv_thoothcard_useexplain:
                Utils.setCardDesc(mContext, "使用说明", "1.此卡仅适用狗狗，猫咪不可使用；\n" +
                        "2.预约洗澡、美容、特色服务时，刷牙免费，不可单独使用；\n" +
                        "3.有效期自绑卡之日起，一年内有效；\n" +
                        "4.每张卡只可绑定一只宠物，绑定成功后不可更换且不可解绑宠物，如删除宠物则绑定卡同时失效；\n" +
                        "5.此卡一经售出不予退换。\n" +
                        " \n" +
                        "最终解释权归宠物家所有\n" +
                        "如有疑问可拨打4000-3000-11\n", "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case R.id.btn_emptyview:
                refresh();
                break;
        }
    }
}
