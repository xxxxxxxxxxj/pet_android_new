package com.haotang.pet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CartShopNoDialogAdapter;
import com.haotang.pet.adapter.ShopMallRecommendAdapter;
import com.haotang.pet.adapter.ShoppingCartAdapter;
import com.haotang.pet.entity.ShopMallFragRecommend;
import com.haotang.pet.entity.ShoppingCart;
import com.haotang.pet.entity.ShoppingCartClass;
import com.haotang.pet.mall.MallOrderConfirmActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshHeadGridView;
import com.haotang.pet.swipelistview.SwipeListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MyStyleSpan;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogListNavAndPost;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.HeaderGridView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车界面
 */
public class ShoppingCartActivity extends SuperActivity implements
        View.OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private SharedPreferenceUtil spUtil;
    private TextView tv_titlebar_other;
    private LinearLayout ll_shopcart_bottom_select;
    private ImageView iv_shopcart_select;
    private Button btn_shopcart_submit;
    private TextView tv_shopcart_price;
    private PullToRefreshHeadGridView ptrhgv_shopcart;
    private View header;
    private SwipeListView slv_header_shoppingcart;
    private ShopMallRecommendAdapter<ShopMallFragRecommend> shopMallRecommendAdapter;
    private List<ShopMallFragRecommend> listRecommend = new ArrayList<ShopMallFragRecommend>();
    private ShoppingCartAdapter<ShoppingCart> shoppingCartAdapter;
    private List<ShoppingCart> listShoppingCart = new ArrayList<ShoppingCart>();
    private List<ShoppingCartClass> listShoppingCartClass = new ArrayList<ShoppingCartClass>();
    private boolean isAllSelect;
    private int updatePosition = -1;
    private int updateNum = -1;
    private int updateCartId = -1;
    private RelativeLayout rl_shopcart_bottom;
    private boolean isEditOrDone;
    private StringBuffer sb = new StringBuffer();
    private int page = 1;
    private LinearLayout ll_header_shoppingcart_recommend;
    private LinearLayout ll_header_shoppingcart_nodata;
    private Button btn_header_shoppingcart_nodata;
    private ImageView img_scroll_top;
    private boolean isGridScroll;
    private LinearLayout ll_shopmallorder_nonet;
    private Button btn_shopmallorder_nonet;
    private int shopCartMemberLevelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        autoLogin();
        getData();
        getrRecommendData();
    }

    private void autoLogin() {
        if (Utils.checkLogin(this)) {
            CommUtil.loginAuto(this, spUtil.getString("cellphone", ""),
                    Global.getIMEI(this),
                    Global.getCurrentVersion(this),
                    spUtil.getInt("userid", 0), 0, 0, autoLoginHandler);
        }
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("user") && !jData.isNull("user")) {
                            JSONObject jUser = jData.getJSONObject("user");
                            if (jUser.has("memberLevelId") && !jUser.isNull("memberLevelId")) {
                                spUtil.saveInt("shopCartMemberLevelId",
                                        jUser.getInt("memberLevelId"));
                            } else {
                                spUtil.removeData("shopCartMemberLevelId");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void getrRecommendData() {
        if (page == 1) {
            listRecommend.clear();
            shopMallRecommendAdapter.clearDeviceList();
        }
        mPDialog.showDialog();
        CommUtil.getrRecommendData(this, 0, page, 0, getrRecommendDataHandler);
    }

    private AsyncHttpResponseHandler getrRecommendDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            ptrhgv_shopcart.onRefreshComplete();
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("commodityList") && !jdata.isNull("commodityList")) {
                                JSONArray jarrcommodityList = jdata.getJSONArray("commodityList");
                                if (jarrcommodityList.length() > 0) {
                                    if (page == 1) {
                                        listRecommend.clear();
                                        shopMallRecommendAdapter.clearDeviceList();
                                    }
                                    for (int i = 0; i < jarrcommodityList.length(); i++) {
                                        listRecommend.add(ShopMallFragRecommend.json2Entity(jarrcommodityList
                                                .getJSONObject(i)));
                                    }
                                } else {
                                    if (page > 1) {
                                        ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "没有更多数据了");
                                    } else {

                                    }
                                }
                            } else {
                                if (page > 1) {
                                    ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "没有更多数据了");
                                }
                            }
                        } else {
                            if (page > 1) {
                                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "没有更多数据了");
                            }
                        }
                    } else {
                        if (page > 1) {
                            ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "没有更多数据了");
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ShoppingCartActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "数据异常");
            }
            writeData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ptrhgv_shopcart.onRefreshComplete();
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "请求失败");
        }
    };

    private void init() {
        //MApplication.listAppoint.add(this);
        MApplication.listAppoint1.add(this);
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        spUtil = SharedPreferenceUtil.getInstance(this);
        shopCartMemberLevelId = spUtil.getInt("shopCartMemberLevelId", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_shopping_cart);
        ll_shopmallorder_nonet = (LinearLayout) findViewById(R.id.ll_shopmallorder_nonet);
        btn_shopmallorder_nonet = (Button) findViewById(R.id.btn_shopmallorder_nonet);
        img_scroll_top = (ImageView) findViewById(R.id.img_scroll_top);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
        ll_shopcart_bottom_select = (LinearLayout) findViewById(R.id.ll_shopcart_bottom_select);
        iv_shopcart_select = (ImageView) findViewById(R.id.iv_shopcart_select);
        btn_shopcart_submit = (Button) findViewById(R.id.btn_shopcart_submit);
        tv_shopcart_price = (TextView) findViewById(R.id.tv_shopcart_price);
        rl_shopcart_bottom = (RelativeLayout) findViewById(R.id.rl_shopcart_bottom);
        ptrhgv_shopcart = (PullToRefreshHeadGridView) findViewById(R.id.ptrhgv_shopcart);
        header = LayoutInflater.from(this).inflate(
                R.layout.header_shoppingcart, null);
        slv_header_shoppingcart = (SwipeListView) header.findViewById(R.id.slv_header_shoppingcart);
        ll_header_shoppingcart_recommend = (LinearLayout) header.findViewById(R.id.ll_header_shoppingcart_recommend);
        ll_header_shoppingcart_nodata = (LinearLayout) header.findViewById(R.id.ll_header_shoppingcart_nodata);
        btn_header_shoppingcart_nodata = (Button) header.findViewById(R.id.btn_header_shoppingcart_nodata);
    }

    private void setView() {
        img_scroll_top.bringToFront();
        ptrhgv_shopcart.getRefreshableView().addHeaderView(header);
        ptrhgv_shopcart.setMode(PullToRefreshBase.Mode.BOTH);
        tv_titlebar_other.setVisibility(View.VISIBLE);
        tv_titlebar_other.setTextColor(getResources().getColor(R.color.white));
        tv_titlebar_other.setText("编辑");
        tvTitle.setText("购物车");
        listRecommend.clear();
        shopMallRecommendAdapter = new ShopMallRecommendAdapter<ShopMallFragRecommend>(this, listRecommend);
        shopMallRecommendAdapter.clearDeviceList();
        ptrhgv_shopcart.setAdapter(shopMallRecommendAdapter);
        listShoppingCart.clear();
        shoppingCartAdapter = new ShoppingCartAdapter<ShoppingCart>(this, listShoppingCart, slv_header_shoppingcart);
        shoppingCartAdapter.clearDeviceList();
        slv_header_shoppingcart.setAdapter(shoppingCartAdapter);
    }

    private void setLinster() {
        btn_shopmallorder_nonet.setOnClickListener(this);
        img_scroll_top.setOnClickListener(this);
        btn_header_shoppingcart_nodata.setOnClickListener(this);
        ll_shopcart_bottom_select.setOnClickListener(this);
        btn_shopcart_submit.setOnClickListener(this);
        tv_titlebar_other.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        ptrhgv_shopcart.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 滚动状态
                        slv_header_shoppingcart.closeOpenedItems();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸后滚动
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 0 && firstVisibleItem <= 10) {
                    isGridScroll = false;
                    img_scroll_top.setVisibility(View.GONE);
                } else {
                    isGridScroll = true;
                    img_scroll_top.setVisibility(View.VISIBLE);
                }
            }
        });
        shoppingCartAdapter.setOnShopCartClassSelectListener(new ShoppingCartAdapter.OnShopCartClassSelectListener() {
            @Override
            public void OnShopCartClassSelect(int position) {
                if (listShoppingCart.size() > 0 && listShoppingCart.size() > position) {
                    ShoppingCart shoppingCart = listShoppingCart.get(position);
                    if (shoppingCart != null) {
                        //判断是结算还是删除
                        if (isEditOrDone) {//完成
                            shoppingCart.setClassSelect(!shoppingCart.isClassSelect());
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                        shoppingCart1.setSelect(shoppingCart.isClassSelect());
                                    }
                                }
                            }
                            int deleteNum = 0;
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (shoppingCart1.isSelect()) {
                                        deleteNum += shoppingCart1.getComCount();
                                    }
                                }
                            }
                            btn_shopcart_submit.setText("删除所选(" + deleteNum + ")");
                        } else {
                            boolean bool4 = false;
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName()) && shoppingCart1.getStatus() != 3) {
                                        bool4 = true;
                                        break;
                                    }
                                }
                            }
                            if (bool4) {//有未下架的商品
                                boolean bool2 = false;
                                for (int i = 0; i < listShoppingCart.size(); i++) {
                                    ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                    if (shoppingCart1 != null) {
                                        if (shoppingCart1.getClassName().equals(shoppingCart.getClassName()) && shoppingCart1.getStock() > 0) {
                                            bool2 = true;
                                            break;
                                        }
                                    }
                                }
                                if (bool2) {//有库存大于0的商品
                                    String vipText = shoppingCart.getVipText();
                                    if (shopCartMemberLevelId > 0) {//vip
                                        //判断此类目下所有商品是否都未下架
                                        boolean bool5 = false;
                                        for (int i = 0; i < listShoppingCart.size(); i++) {
                                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                            if (shoppingCart1 != null) {
                                                if (shoppingCart1.getClassName().equals(shoppingCart.getClassName()) && shoppingCart1.getStatus() == 3) {
                                                    bool5 = true;
                                                    break;
                                                }
                                            }
                                        }

                                        //判断此类目下所有商品库存是否都不为0
                                        boolean bool = false;
                                        for (int i = 0; i < listShoppingCart.size(); i++) {
                                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                            if (shoppingCart1 != null) {
                                                if (shoppingCart1.getClassName().equals(shoppingCart.getClassName()) && shoppingCart1.getStock() <= 0) {
                                                    bool = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!bool && !bool5) {//库存全部大于0且全部未下架
                                            shoppingCart.setClassSelect(!shoppingCart.isClassSelect());
                                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                                if (shoppingCart1 != null) {
                                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                                        shoppingCart1.setSelect(shoppingCart.isClassSelect());
                                                    }
                                                }
                                            }
                                        } else {//库存部分大于0
                                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                                if (shoppingCart1 != null) {
                                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                                        if (shoppingCart1.getStock() > 0 && shoppingCart1.getStatus() != 3) {
                                                            shoppingCart1.setSelect(true);
                                                        } else {
                                                            shoppingCart1.setSelect(false);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {//非VIP
                                        boolean bool = false;
                                        for (int i = 0; i < listShoppingCart.size(); i++) {
                                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                            if (shoppingCart1 != null) {
                                                if (shoppingCart1.getRestrict() == 0 && shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {//判断是否有非VIP尚品
                                                    bool = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (bool) {//有非VIP尚品，再判断是否全部是非VIP商品
                                            //判断此类目下所有商品是否都未下架
                                            boolean bool5 = false;
                                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                                if (shoppingCart1 != null) {
                                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName()) && shoppingCart1.getStatus() == 3) {
                                                        bool5 = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            boolean bool6 = false;
                                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                                if (shoppingCart1 != null) {
                                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName()) && shoppingCart1.getStock() <= 0) {
                                                        bool6 = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            boolean bool7 = false;
                                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                                if (shoppingCart1 != null) {
                                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName()) && shoppingCart1.getRestrict() == 1) {
                                                        bool7 = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!bool5 && !bool6 && !bool7) {//库存全部大于0而且全部是非VIP商品而且全部未下架
                                                shoppingCart.setClassSelect(!shoppingCart.isClassSelect());
                                                for (int i = 0; i < listShoppingCart.size(); i++) {
                                                    ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                                    if (shoppingCart1 != null) {
                                                        if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                                            shoppingCart1.setSelect(shoppingCart.isClassSelect());
                                                        }
                                                    }
                                                }
                                            } else {
                                                for (int i = 0; i < listShoppingCart.size(); i++) {
                                                    ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                                    if (shoppingCart1 != null) {
                                                        if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                                            if (shoppingCart1.getStock() > 0 && shoppingCart1.getRestrict() == 0 && shoppingCart1.getStatus() != 3) {
                                                                shoppingCart1.setSelect(true);
                                                            } else {
                                                                shoppingCart1.setSelect(false);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            if (Utils.isStrNull(vipText)) {
                                                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, vipText);
                                            }
                                        }
                                    }
                                    setLastPrice();
                                } else {
                                    ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "宝贝已售罄");
                                }
                            } else {
                                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "宝贝已下架");
                            }
                        }
                        shoppingCartAdapter.setData(listShoppingCart);
                        boolean bool6 = true;
                        for (int i = 0; i < listShoppingCart.size(); i++) {
                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                            if (shoppingCart1 != null) {
                                if (!shoppingCart1.isSelect()) {
                                    bool6 = false;
                                    break;
                                }
                            }
                        }
                        if (bool6) {
                            isAllSelect = true;
                            iv_shopcart_select.setImageResource(R.drawable.complaint_reason);
                        } else {
                            isAllSelect = false;
                            iv_shopcart_select.setImageResource(R.drawable.complaint_reason_disable);
                        }
                    }
                }
            }
        });
        shoppingCartAdapter.setOnShopCartSelectListener(new ShoppingCartAdapter.OnShopCartSelectListener() {
            @Override
            public void OnShopCartSelect(int position) {
                if (listShoppingCart.size() > 0 && listShoppingCart.size() > position) {
                    ShoppingCart shoppingCart = listShoppingCart.get(position);
                    if (shoppingCart != null) {
                        if (isEditOrDone) {//完成
                            //选中单个商品
                            shoppingCart.setSelect(!shoppingCart.isSelect());
                            int deleteNum = 0;
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (shoppingCart1.isSelect()) {
                                        deleteNum += shoppingCart1.getComCount();
                                    }
                                }
                            }
                            btn_shopcart_submit.setText("删除所选(" + deleteNum + ")");
                        } else {//编辑
                            if (shoppingCart.getStatus() != 3) {//判断是否已下架
                                if (shoppingCart.getStock() > 0) {//库存大于0
                                    String vipText = shoppingCart.getVipText();
                                    if (shopCartMemberLevelId > 0) {//vip
                                        shoppingCart.setSelect(!shoppingCart.isSelect());
                                    } else {//非VIP,再判断商品是否是非VIP商品
                                        if (shoppingCart.getRestrict() == 1) {//VIP商品
                                            if (Utils.isStrNull(vipText)) {
                                                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, vipText);
                                            }
                                        } else {//非VIP商品
                                            shoppingCart.setSelect(!shoppingCart.isSelect());
                                        }
                                    }
                                    setLastPrice();
                                } else {
                                    ToastUtil.showToastShortBottom(ShoppingCartActivity.this, shoppingCart.getCommodityText());
                                }
                            } else {
                                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, shoppingCart.getCommodityText());
                            }
                        }
                        //判断是否选中大类
                        boolean bool = true;
                        for (int i = 0; i < listShoppingCart.size(); i++) {
                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                            if (shoppingCart1 != null) {
                                if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                    if (!shoppingCart1.isSelect()) {
                                        bool = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (bool) {
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                        if (shoppingCart1.getClassIndex() == -1) {
                                            shoppingCart1.setClassSelect(true);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (shoppingCart1.getClassName().equals(shoppingCart.getClassName())) {
                                        if (shoppingCart1.getClassIndex() == -1) {
                                            shoppingCart1.setClassSelect(false);
                                        }
                                    }
                                }
                            }
                        }
                        shoppingCartAdapter.setData(listShoppingCart);
                        boolean boole = true;
                        for (int i = 0; i < listShoppingCart.size(); i++) {
                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                            if (shoppingCart1 != null) {
                                if (!shoppingCart1.isSelect()) {
                                    boole = false;
                                    break;
                                }
                            }
                        }
                        if (boole) {
                            isAllSelect = true;
                            iv_shopcart_select.setImageResource(R.drawable.complaint_reason);
                        } else {
                            isAllSelect = false;
                            iv_shopcart_select.setImageResource(R.drawable.complaint_reason_disable);
                        }
                    }
                }
            }
        });
        shoppingCartAdapter.setOnShopCartIncreaseListener(new ShoppingCartAdapter.OnShopCartIncreaseListener() {
            @Override
            public void OnShopCartIncrease(int position) {
                if (listShoppingCart.size() > 0 && listShoppingCart.size() > position) {
                    ShoppingCart shoppingCart = listShoppingCart.get(position);
                    if (shoppingCart != null) {
                        updateCartId = shoppingCart.getCartId();
                        updatePosition = position;
                        updateNum = shoppingCart.getComCount() + 1;
                        mPDialog.showDialog();
                        CommUtil.updateCartNum(mContext, shoppingCart.getCartId(), shoppingCart.getComCount() + 1, updateCartNumHandler);
                    }
                }
            }
        });
        shoppingCartAdapter.setOnShopCartReduceListener(new ShoppingCartAdapter.OnShopCartReduceListener() {
            @Override
            public void OnShopCartReduce(int position) {
                if (listShoppingCart.size() > 0 && listShoppingCart.size() > position) {
                    ShoppingCart shoppingCart = listShoppingCart.get(position);
                    if (shoppingCart != null) {
                        updateCartId = shoppingCart.getCartId();
                        updatePosition = position;
                        updateNum = shoppingCart.getComCount() - 1;
                        mPDialog.showDialog();
                        CommUtil.updateCartNum(mContext, shoppingCart.getCartId(), shoppingCart.getComCount() - 1, updateCartNumHandler);
                    }
                }
            }
        });
        shoppingCartAdapter.setOnShopCartDeleteListener(new ShoppingCartAdapter.OnShopCartDeleteListener() {
            @Override
            public void OnShopCartDelete(int position) {
                if (listShoppingCart.size() > 0 && listShoppingCart.size() > position) {
                    final ShoppingCart shoppingCart = listShoppingCart.get(position);
                    if (shoppingCart != null) {
                        new AlertDialogNavAndPost(mContext).builder().setTitle("")
                                .setMsg("确定要删除这个宝贝吗？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sb.setLength(0);
                                        sb.append(String.valueOf(shoppingCart.getCartId()));
                                        mPDialog.showDialog();
                                        CommUtil.deleteCartCommodity(ShoppingCartActivity.this, String.valueOf(shoppingCart.getCartId()), deleteCartCommodityHandler);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                }
            }
        });
        shoppingCartAdapter.setOnShopSerchListener(new ShoppingCartAdapter.OnShopSerchListener() {
            @Override
            public void OnShopSerch(int cartId, int shopNum) {
                updateCartId = cartId;
                updateNum = shopNum;
                mPDialog.showDialog();
                CommUtil.updateCartNum(mContext, cartId, shopNum, updateCartNumHandler);
            }
        });
        ptrhgv_shopcart
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
                    @Override
                    public void onRefresh(PullToRefreshBase refreshView) {
                        PullToRefreshBase.Mode mode = refreshView
                                .getCurrentMode();
                        if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {// 下拉刷新
                            page = 1;
                            getData();
                            getrRecommendData();
                        } else {// 上拉加载更多
                            page++;
                            getrRecommendData();
                        }
                    }
                });
    }

    private AsyncHttpResponseHandler deleteCartCommodityHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Log.e("TAG", "删除购物车new String(responseBody) = " + new String(responseBody));
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    slv_header_shoppingcart.closeOpenedItems();
                    if (sb.length() > 0) {
                        String[] split = sb.toString().split(",");
                        for (int i = 0; i < split.length; i++) {
                            for (int j = 0; j < listShoppingCart.size(); j++) {
                                ShoppingCart shoppingCart = listShoppingCart.get(j);
                                if (shoppingCart != null) {
                                    if (shoppingCart.getCartId() == Integer.parseInt(split[i]) && shoppingCart.getClassIndex() == -1) {//说明是大类
                                        if (listShoppingCart.size() > (j + 1) && shoppingCart.getClassName().equals(listShoppingCart.get(j + 1).getClassName())) {
                                            listShoppingCart.get(j + 1).setClassIndex(-1);
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < split.length; i++) {
                            for (int j = 0; j < listShoppingCart.size(); j++) {
                                ShoppingCart shoppingCart = listShoppingCart.get(j);
                                if (shoppingCart != null) {
                                    if (shoppingCart.getCartId() == Integer.parseInt(split[i])) {
                                        listShoppingCart.remove(j);
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < listShoppingCartClass.size(); i++) {
                            ShoppingCartClass shoppingCartClass = listShoppingCartClass.get(i);
                            if (shoppingCartClass != null) {
                                List<ShoppingCart> shoppingCartList = shoppingCartClass.getShoppingCartList();
                                if (shoppingCartList != null && shoppingCartList.size() > 0) {
                                    for (int j = 0; j < shoppingCartList.size(); j++) {
                                        ShoppingCart shoppingCart = shoppingCartList.get(j);
                                        if (shoppingCart != null) {
                                            for (int k = 0; k < split.length; k++) {
                                                if (shoppingCart != null) {
                                                    if (shoppingCart.getCartId() == Integer.parseInt(split[k])) {
                                                        shoppingCartList.remove(j);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    listShoppingCartClass.remove(i);
                                }
                            }
                        }
                        if (listShoppingCart.size() > 0) {//删除部分
                            Log.e("TAG", "删除部分");

                            for (int i = 0; i < listShoppingCartClass.size(); i++) {
                                ShoppingCartClass shoppingCartClass = listShoppingCartClass.get(i);
                                if (shoppingCartClass != null) {
                                    String className = shoppingCartClass.getClassName();
                                    List<ShoppingCart> shoppingCartList = shoppingCartClass.getShoppingCartList();
                                    int num = 0;
                                    for (int j = 0; j < listShoppingCart.size(); j++) {
                                        ShoppingCart shoppingCart = listShoppingCart.get(j);
                                        if (shoppingCart != null) {
                                            if (shoppingCart.getClassName().equals(className) && shoppingCart.isSelect()) {
                                                num++;
                                                if (num == shoppingCartList.size()) {
                                                    shoppingCart.setClassSelect(true);
                                                    for (int k = 0; k < listShoppingCart.size(); k++) {
                                                        ShoppingCart shoppingCart1 = listShoppingCart.get(k);
                                                        if (shoppingCart1 != null) {
                                                            if (shoppingCart.getClassName().equals(className) && shoppingCart.isSelect() && shoppingCart.getClassIndex() == -1) {
                                                                shoppingCart.setClassSelect(true);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            boolean boole = true;
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (!shoppingCart1.isSelect()) {
                                        boole = false;
                                    }
                                }
                            }
                            if (boole) {
                                isAllSelect = true;
                                iv_shopcart_select.setImageResource(R.drawable.complaint_reason);
                            } else {
                                isAllSelect = false;
                                iv_shopcart_select.setImageResource(R.drawable.complaint_reason_disable);
                            }

                            ll_header_shoppingcart_nodata.setVisibility(View.GONE);
                            tv_titlebar_other.setVisibility(View.VISIBLE);
                            rl_shopcart_bottom.setVisibility(View.VISIBLE);
                            Log.e("TAG", "listShoppingCart = " + listShoppingCart.toString());
                            slv_header_shoppingcart.setVisibility(View.VISIBLE);
                            shoppingCartAdapter.setData(listShoppingCart);
                            //判断是结算还是删除
                            if (isEditOrDone) {//完成
                                int deleteNum = 0;
                                for (int i = 0; i < listShoppingCart.size(); i++) {
                                    ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                    if (shoppingCart1 != null) {
                                        if (shoppingCart1.isSelect()) {
                                            deleteNum += shoppingCart1.getComCount();
                                        }
                                    }
                                }
                                btn_shopcart_submit.setText("删除所选(" + deleteNum + ")");
                            } else {//编辑
                                setLastPrice();
                            }
                        } else {//全部删除
                            Log.e("TAG", "删除全部");
                            isEditOrDone = false;
                            getData();
                            /*ll_header_shoppingcart_nodata.setVisibility(View.VISIBLE);
                            tv_titlebar_other.setVisibility(View.GONE);
                            rl_shopcart_bottom.setVisibility(View.GONE);
                            slv_header_shoppingcart.setVisibility(View.GONE);*/
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ShoppingCartActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "请求失败");
        }
    };

    private AsyncHttpResponseHandler updateCartNumHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    for (int i = 0; i < listShoppingCart.size(); i++) {
                        ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                        if (shoppingCart1 != null) {
                            if (shoppingCart1.getCartId() == updateCartId) {
                                shoppingCart1.setComCount(updateNum);
                            }
                        }
                    }
                    shoppingCartAdapter.setData(listShoppingCart);
                    tv_shopcart_price.setVisibility(View.VISIBLE);
                    isEditOrDone = false;
                    tv_titlebar_other.setText("编辑");
                    setLastPrice();
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ShoppingCartActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "请求失败");
        }
    };

    private void getData() {
        tv_shopcart_price.setText("总计 ¥0");
        btn_shopcart_submit.setText("结算");
        tv_titlebar_other.setText("编辑");
        isAllSelect = false;
        iv_shopcart_select.setImageResource(R.drawable.complaint_reason_disable);
        isGridScroll = false;
        isEditOrDone = false;
        listShoppingCartClass.clear();
        listShoppingCart.clear();
        shoppingCartAdapter.clearDeviceList();
        mPDialog.showDialog();
        CommUtil.myCart(mContext, myCartHandler);
    }

    private AsyncHttpResponseHandler myCartHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            rl_shopcart_bottom.setVisibility(View.VISIBLE);
            tv_titlebar_other.setVisibility(View.VISIBLE);
            ll_shopmallorder_nonet.setVisibility(View.GONE);
            img_scroll_top.setVisibility(View.VISIBLE);
            ptrhgv_shopcart.setVisibility(View.VISIBLE);
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    JSONArray jadata = jobj.getJSONArray("data");
                    if (jadata != null && jadata.length() > 0) {
                        listShoppingCartClass.clear();
                        listShoppingCart.clear();
                        shoppingCartAdapter.clearDeviceList();
                        for (int i = 0; i < jadata.length(); i++) {
                            listShoppingCartClass.add(ShoppingCartClass
                                    .json2Entity(jadata
                                            .getJSONObject(i)));
                        }
                        for (int i = 0; i < listShoppingCartClass.size(); i++) {
                            ShoppingCartClass shoppingCartClass = listShoppingCartClass.get(i);
                            if (shoppingCartClass != null) {
                                String className = shoppingCartClass.getClassName();
                                List<ShoppingCart> shoppingCartList = shoppingCartClass.getShoppingCartList();
                                if (shoppingCartList != null && shoppingCartList.size() > 0) {
                                    for (int j = 0; j < shoppingCartList.size(); j++) {
                                        ShoppingCart shoppingCart = shoppingCartList.get(j);
                                        if (shoppingCart != null) {
                                            shoppingCart.setClassName(className);
                                            if (j == 0) {
                                                shoppingCart.setClassIndex(-1);
                                            }
                                            listShoppingCart.add(shoppingCart);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(ShoppingCartActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "数据异常");
            }
            writeData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "请求失败");
            ll_shopmallorder_nonet.setVisibility(View.VISIBLE);
            img_scroll_top.setVisibility(View.GONE);
            ptrhgv_shopcart.setVisibility(View.GONE);
            rl_shopcart_bottom.setVisibility(View.INVISIBLE);
            tv_titlebar_other.setVisibility(View.INVISIBLE);
        }
    };

    private void writeData() {
        if (listShoppingCart.size() > 0) {
            ll_header_shoppingcart_nodata.setVisibility(View.GONE);
            tv_titlebar_other.setVisibility(View.VISIBLE);
            rl_shopcart_bottom.setVisibility(View.VISIBLE);
            Log.e("TAG", "listShoppingCart = " + listShoppingCart.toString());
            slv_header_shoppingcart.setVisibility(View.VISIBLE);
            shoppingCartAdapter.setData(listShoppingCart);
        } else {
            ll_header_shoppingcart_nodata.setVisibility(View.VISIBLE);
            tv_titlebar_other.setVisibility(View.GONE);
            rl_shopcart_bottom.setVisibility(View.GONE);
            slv_header_shoppingcart.setVisibility(View.GONE);
        }
        if (listRecommend != null && listRecommend.size() > 0) {
            ll_header_shoppingcart_recommend.setVisibility(View.VISIBLE);
            if (listRecommend.size() == 1) {
                ShopMallFragRecommend shopMallFragRecommend = listRecommend.get(0);
                if (shopMallFragRecommend != null) {
                    if (shopMallFragRecommend.isOther()) {
                        ll_header_shoppingcart_recommend.setVisibility(View.GONE);
                    }
                }
            }
            shopMallRecommendAdapter.setData(listRecommend);
        } else {
            if (page == 1) {
                listRecommend.add(new ShopMallFragRecommend("追加", 0, 0, "", 0, 0, true));
                shopMallRecommendAdapter.setData(listRecommend);
            }
            ll_header_shoppingcart_recommend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shopmallorder_nonet:
                page = 1;
                getData();
                getrRecommendData();
                break;
            case R.id.img_scroll_top:
                if (isGridScroll) {
                    HeaderGridView refreshableView = ptrhgv_shopcart.getRefreshableView();
                    if (!(refreshableView).isStackFromBottom()) {
                        refreshableView.setStackFromBottom(true);
                    }
                    refreshableView.setStackFromBottom(false);
                }
                break;
            case R.id.tv_titlebar_other:
                slv_header_shoppingcart.closeOpenedItems();
                if (isEditOrDone) {
                    isEditOrDone = false;
                    tv_titlebar_other.setText("编辑");
                    tv_shopcart_price.setVisibility(View.VISIBLE);
                    if (shopCartMemberLevelId > 0) {
                        for (int i = 0; i < listShoppingCart.size(); i++) {
                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                            if (shoppingCart1 != null) {
                                if (shoppingCart1.getStock() <= 0 || shoppingCart1.getStatus() == 3) {
                                    shoppingCart1.setSelect(false);
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < listShoppingCart.size(); i++) {
                            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                            if (shoppingCart1 != null) {
                                if (shoppingCart1.getStock() <= 0 || shoppingCart1.getRestrict() == 1 || shoppingCart1.getStatus() == 3) {
                                    shoppingCart1.setSelect(false);
                                }
                            }
                        }
                    }
                    //如果相同类目的商品全部被选中，则将大类也选中，反之则取消选中
                    Log.e("TAG", "listShoppingCartClass.size() = " + listShoppingCartClass.size());
                    for (int i = 0; i < listShoppingCartClass.size(); i++) {
                        int num = 0;
                        ShoppingCartClass shoppingCartClass = listShoppingCartClass.get(i);
                        if (shoppingCartClass != null) {
                            List<ShoppingCart> shoppingCartList = shoppingCartClass.getShoppingCartList();
                            String className = shoppingCartClass.getClassName();
                            Log.e("TAG", "shoppingCartList.size() = " + shoppingCartList.size());
                            for (int j = 0; j < listShoppingCart.size(); j++) {
                                ShoppingCart shoppingCart = listShoppingCart.get(j);
                                if (shoppingCart != null) {
                                    if (shoppingCart.getClassName().equals(className) && shoppingCart.isSelect()) {
                                        num++;
                                        Log.e("TAG", "num = " + num);
                                    }
                                }
                            }
                            if (num == shoppingCartList.size()) {
                                for (int j = 0; j < listShoppingCart.size(); j++) {
                                    ShoppingCart shoppingCart = listShoppingCart.get(j);
                                    if (shoppingCart != null) {
                                        if (shoppingCart.getClassName().equals(className) && shoppingCart.getClassIndex() == -1) {
                                            shoppingCart.setClassSelect(true);
                                        }
                                    }
                                }
                            } else {
                                for (int j = 0; j < listShoppingCart.size(); j++) {
                                    ShoppingCart shoppingCart = listShoppingCart.get(j);
                                    if (shoppingCart != null) {
                                        if (shoppingCart.getClassName().equals(className) && shoppingCart.getClassIndex() == -1) {
                                            shoppingCart.setClassSelect(false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    setLastPrice();
                    shoppingCartAdapter.setData(listShoppingCart);
                    boolean boole = true;
                    for (int i = 0; i < listShoppingCart.size(); i++) {
                        ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                        if (shoppingCart1 != null) {
                            if (!shoppingCart1.isSelect()) {
                                boole = false;
                                break;
                            }
                        }
                    }
                    if (boole) {
                        isAllSelect = true;
                        iv_shopcart_select.setImageResource(R.drawable.complaint_reason);
                    } else {
                        isAllSelect = false;
                        iv_shopcart_select.setImageResource(R.drawable.complaint_reason_disable);
                    }
                } else {
                    isEditOrDone = true;
                    tv_titlebar_other.setText("完成");
                    tv_shopcart_price.setVisibility(View.INVISIBLE);
                    int deleteNum = 0;
                    for (int i = 0; i < listShoppingCart.size(); i++) {
                        ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                        if (shoppingCart1 != null) {
                            if (shoppingCart1.isSelect()) {
                                deleteNum += shoppingCart1.getComCount();
                            }
                        }
                    }
                    btn_shopcart_submit.setText("删除所选(" + deleteNum + ")");
                }
                break;
            case R.id.btn_header_shoppingcart_nodata:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.mainactivity");
                intent.putExtra("previous",
                        Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
                sendBroadcast(intent);
                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                    MApplication.listAppoint.get(i).finish();
                }
                for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                    MApplication.listAppoint1.get(i).finish();
                }
                finishWithAnimation();
                break;
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ll_shopcart_bottom_select:
                setAllSelect();
                break;
            case R.id.btn_shopcart_submit:
                if (isEditOrDone) {//完成
                    boolean bool = false;
                    int shopMallNum = 0;
                    for (int i = 0; i < listShoppingCart.size(); i++) {
                        ShoppingCart shoppingCart = listShoppingCart.get(i);
                        if (shoppingCart != null) {
                            if (shoppingCart.isSelect()) {
                                bool = true;
                                shopMallNum += shoppingCart.getComCount();
                            }
                        }
                    }
                    if (bool) {//删除
                        new AlertDialogNavAndPost(mContext).builder().setTitle("")
                                .setMsg("确定要删除这" + (shopMallNum > 1 ? shopMallNum : "") + "个宝贝吗？")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        List<ShoppingCart> localListShoppingCart = new ArrayList<ShoppingCart>();
                                        localListShoppingCart.clear();
                                        sb.setLength(0);
                                        for (int i = 0; i < listShoppingCart.size(); i++) {
                                            ShoppingCart shoppingCart = listShoppingCart.get(i);
                                            if (shoppingCart != null) {
                                                if (shoppingCart.isSelect()) {
                                                    localListShoppingCart.add(shoppingCart);
                                                }
                                            }
                                        }
                                        for (int i = 0; i < localListShoppingCart.size(); i++) {
                                            ShoppingCart shoppingCart = localListShoppingCart.get(i);
                                            if (shoppingCart != null) {
                                                if (i < localListShoppingCart.size() - 1) {
                                                    sb.append(shoppingCart.getCartId());
                                                    sb.append(",");
                                                } else {
                                                    sb.append(shoppingCart.getCartId());
                                                }
                                            }
                                        }
                                        mPDialog.showDialog();
                                        CommUtil.deleteCartCommodity(ShoppingCartActivity.this, sb.toString(), deleteCartCommodityHandler);
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    } else {
                        ToastUtil.showToastShortCenter(ShoppingCartActivity.this, "请选择要删除的商品");
                    }
                } else {//编辑
                    StringBuffer sb = new StringBuffer();
                    sb.setLength(0);
                    boolean bool = false;
                    for (int i = 0; i < listShoppingCart.size(); i++) {
                        ShoppingCart shoppingCart = listShoppingCart.get(i);
                        if (shoppingCart != null) {
                            if (shoppingCart.isSelect()) {
                                bool = true;
                                sb.append(shoppingCart.getCommodityId() + "_" + shoppingCart.getComCount() + ",");
                            }
                        }
                    }
                    if (bool) {//去结算
                        List<ShoppingCart> localListShoppingCart = new ArrayList<ShoppingCart>();
                        localListShoppingCart.clear();
                        //判断是否有库存不足的商品
                        for (int i = 0; i < listShoppingCart.size(); i++) {
                            ShoppingCart shoppingCart = listShoppingCart.get(i);
                            if (shoppingCart != null) {
                                if (shoppingCart.isSelect() && shoppingCart.getStock() > 0 && shoppingCart.getComCount() > shoppingCart.getStock()) {
                                    localListShoppingCart.add(shoppingCart);
                                }
                            }
                        }
                        if (localListShoppingCart.size() > 0) {//存在库存不足的商品，弹框提示
                            AlertDialogListNavAndPost builder = new AlertDialogListNavAndPost(this)
                                    .builder();
                            builder.lv_appointmentnew_noshop
                                    .setAdapter(new CartShopNoDialogAdapter<ShoppingCart>(
                                            this, localListShoppingCart));
                            builder.setTitle("以下宝贝库存不足,请手动修改数量再购买哦").setNavVisible(View.GONE).setPetListVisible(View.GONE)
                                    .setPositiveButton("我知道了", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
                        } else {
                            Log.e("TAG", "sb.toString() = " + sb.toString());
                            int i = sb.toString().lastIndexOf(",");
                            Log.e("TAG", "i = " + i);
                            String substring = sb.toString().substring(0, sb.toString().length() - 1);
                            Log.e("TAG", "substring = " + substring);
                            startActivity(new Intent(ShoppingCartActivity.this, MallOrderConfirmActivity.class).putExtra("strp", substring));
                        }
                    } else {
                        ToastUtil.showToastShortCenter(ShoppingCartActivity.this, "还没有选择商品哦");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setAllSelect() {
        Log.e("TAG", "isEditOrDone = " + isEditOrDone);
        Log.e("TAG", "isAllSelect = " + isAllSelect);
        //判断是编辑还是完成
        if (isEditOrDone) {//完成
            //再判断是选中还是取消选中
            if (isAllSelect) {//已选中，取消选中
                for (int i = 0; i < listShoppingCart.size(); i++) {
                    ShoppingCart shoppingCart = listShoppingCart.get(i);
                    if (shoppingCart != null) {
                        shoppingCart.setSelect(false);
                        if (shoppingCart.getClassIndex() == -1) {
                            shoppingCart.setClassSelect(false);
                        }
                    }
                }
            } else {//未选中，全部选中
                for (int i = 0; i < listShoppingCart.size(); i++) {
                    ShoppingCart shoppingCart = listShoppingCart.get(i);
                    if (shoppingCart != null) {
                        shoppingCart.setSelect(true);
                        if (shoppingCart.getClassIndex() == -1) {
                            shoppingCart.setClassSelect(true);
                        }
                    }
                }
            }
            int deleteNum = 0;
            for (int i = 0; i < listShoppingCart.size(); i++) {
                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                if (shoppingCart1 != null) {
                    if (shoppingCart1.isSelect()) {
                        deleteNum += shoppingCart1.getComCount();
                    }
                }
            }
            btn_shopcart_submit.setText("删除所选(" + deleteNum + ")");
        } else {//编辑
            //再判断是选中还是取消选中
            if (isAllSelect) {//已选中，取消选中
                for (int i = 0; i < listShoppingCart.size(); i++) {
                    ShoppingCart shoppingCart = listShoppingCart.get(i);
                    if (shoppingCart != null) {
                        shoppingCart.setSelect(false);
                        if (shoppingCart.getClassIndex() == -1) {
                            shoppingCart.setClassSelect(false);
                        }
                    }
                }
            } else {//未选中，全部选中
                //判断商品是否全部下架
                boolean bool9 = false;
                for (int i = 0; i < listShoppingCart.size(); i++) {
                    ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                    if (shoppingCart1 != null) {
                        if (shoppingCart1.getStatus() != 3) {
                            bool9 = true;
                            break;
                        }
                    }
                }
                if (bool9) {//有一个或者多个商品未下架
                    //判断商品库存是否全部为0
                    boolean bool2 = false;
                    for (int i = 0; i < listShoppingCart.size(); i++) {
                        ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                        if (shoppingCart1 != null) {
                            if (shoppingCart1.getStock() > 0) {
                                bool2 = true;
                                break;
                            }
                        }
                    }
                    if (bool2) {//有一个或者多个库存不为为0的商品
                        //判断是否是vip
                        if (shopCartMemberLevelId > 0) {//vip
                            //将库存不为0的商品全部选中,库存为0的商品取消选中
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart = listShoppingCart.get(i);
                                if (shoppingCart != null) {
                                    if (shoppingCart.getStock() > 0 && shoppingCart.getStatus() != 3) {
                                        shoppingCart.setSelect(true);
                                    } else {
                                        shoppingCart.setSelect(false);
                                    }
                                }
                            }
                            //如果相同类目的商品全部被选中，则将大类也选中，反之则取消选中
                            for (int i = 0; i < listShoppingCartClass.size(); i++) {
                                int num = 0;
                                ShoppingCartClass shoppingCartClass = listShoppingCartClass.get(i);
                                if (shoppingCartClass != null) {
                                    List<ShoppingCart> shoppingCartList = shoppingCartClass.getShoppingCartList();
                                    String className = shoppingCartClass.getClassName();
                                    for (int j = 0; j < listShoppingCart.size(); j++) {
                                        ShoppingCart shoppingCart = listShoppingCart.get(j);
                                        if (shoppingCart != null) {
                                            if (shoppingCart.getClassName().equals(className) && shoppingCart.isSelect()) {
                                                num++;
                                            }
                                        }
                                    }
                                    if (num == shoppingCartList.size()) {
                                        for (int j = 0; j < listShoppingCart.size(); j++) {
                                            ShoppingCart shoppingCart = listShoppingCart.get(j);
                                            if (shoppingCart != null) {
                                                if (shoppingCart.getClassName().equals(className) && shoppingCart.getClassIndex() == -1) {
                                                    shoppingCart.setClassSelect(true);
                                                }
                                            }
                                        }
                                    } else {
                                        for (int j = 0; j < listShoppingCart.size(); j++) {
                                            ShoppingCart shoppingCart = listShoppingCart.get(j);
                                            if (shoppingCart != null) {
                                                if (shoppingCart.getClassName().equals(className) && shoppingCart.getClassIndex() == -1) {
                                                    shoppingCart.setClassSelect(false);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {//非vip
                            //判断是否有非VIP商品
                            boolean bool = false;
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                if (shoppingCart1 != null) {
                                    if (shoppingCart1.getRestrict() == 0) {
                                        bool = true;
                                        break;
                                    }
                                }
                            }
                            if (bool) {//有一个或者多个VIP商品
                                for (int i = 0; i < listShoppingCart.size(); i++) {
                                    ShoppingCart shoppingCart1 = listShoppingCart.get(i);
                                    if (shoppingCart1 != null) {
                                        if (shoppingCart1.getRestrict() == 0 && shoppingCart1.getStock() > 0 && shoppingCart1.getStatus() != 3) {
                                            shoppingCart1.setSelect(true);
                                        } else {
                                            shoppingCart1.setSelect(false);
                                        }
                                    }
                                }
                                for (int i = 0; i < listShoppingCartClass.size(); i++) {
                                    int num = 0;
                                    ShoppingCartClass shoppingCartClass = listShoppingCartClass.get(i);
                                    if (shoppingCartClass != null) {
                                        List<ShoppingCart> shoppingCartList = shoppingCartClass.getShoppingCartList();
                                        String className = shoppingCartClass.getClassName();
                                        for (int j = 0; j < listShoppingCart.size(); j++) {
                                            ShoppingCart shoppingCart = listShoppingCart.get(j);
                                            if (shoppingCart != null) {
                                                if (shoppingCart.getClassName().equals(className) && shoppingCart.isSelect()) {
                                                    num++;
                                                }
                                            }
                                        }
                                        if (num == shoppingCartList.size()) {
                                            for (int j = 0; j < listShoppingCart.size(); j++) {
                                                ShoppingCart shoppingCart = listShoppingCart.get(j);
                                                if (shoppingCart != null) {
                                                    if (shoppingCart.getClassName().equals(className) && shoppingCart.getClassIndex() == -1) {
                                                        shoppingCart.setClassSelect(true);
                                                    }
                                                }
                                            }
                                        } else {
                                            for (int j = 0; j < listShoppingCart.size(); j++) {
                                                ShoppingCart shoppingCart = listShoppingCart.get(j);
                                                if (shoppingCart != null) {
                                                    if (shoppingCart.getClassName().equals(className) && shoppingCart.getClassIndex() == -1) {
                                                        shoppingCart.setClassSelect(false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {//全部是VIP商品
                                String vipText = listShoppingCart.get(0).getVipText();
                                if (Utils.isStrNull(vipText)) {
                                    ToastUtil.showToastShortBottom(ShoppingCartActivity.this, vipText);
                                }
                            }
                        }
                    } else {//商品库存全部为0
                        ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "商品库存为0");
                    }
                } else {
                    ToastUtil.showToastShortBottom(ShoppingCartActivity.this, "商品已下架");
                }
            }
            setLastPrice();
        }
        shoppingCartAdapter.setData(listShoppingCart);
        boolean boole = true;
        for (int i = 0; i < listShoppingCart.size(); i++) {
            ShoppingCart shoppingCart1 = listShoppingCart.get(i);
            if (shoppingCart1 != null) {
                if (!shoppingCart1.isSelect()) {
                    boole = false;
                    break;
                }
            }
        }
        if (boole) {
            isAllSelect = true;
            iv_shopcart_select.setImageResource(R.drawable.complaint_reason);
        } else {
            isAllSelect = false;
            iv_shopcart_select.setImageResource(R.drawable.complaint_reason_disable);
        }
    }

    private void setLastPrice() {
        double lastPrice = 0;
        int shopNum = 0;
        for (int i = 0; i < listShoppingCart.size(); i++) {
            ShoppingCart shoppingCart = listShoppingCart.get(i);
            if (shoppingCart != null) {
                if (shoppingCart.isSelect()) {
                    shopNum += shoppingCart.getComCount();
                    BigDecimal a1 = null;
                    if (shoppingCart.getePrice() > 0) {
                        a1 = new BigDecimal(Double.toString(shoppingCart.getePrice()));
                    } else {
                        a1 = new BigDecimal(Double.toString(shoppingCart.getRetailPrice()));
                    }
                    BigDecimal b1 = new BigDecimal(Double.toString(shoppingCart.getComCount()));
                    BigDecimal result = a1.multiply(b1);// 相乘结果
                    double a = result.doubleValue();//保留1位数
                    /*double c = a + lastPrice;
                    BigDecimal bd = new BigDecimal(c);
                    lastPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();*/
                    lastPrice = ComputeUtil.add(lastPrice, a);
                }
            }
        }
        String text = "";
        if (Utils.isDoubleEndWithZero(lastPrice)) {
            text = "总计 ¥ " + Utils.formatDouble(lastPrice);
        } else {
            text = "总计 ¥ " + lastPrice;
        }
        SpannableString ss = new SpannableString(text);
        ss.setSpan(
                new ForegroundColorSpan(getResources()
                        .getColor(R.color.a666666)), 0, 2,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(new MyStyleSpan(Typeface.BOLD), 3, ss.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv_shopcart_price.setText(ss);
        btn_shopcart_submit.setText("结算(" + shopNum + ")");
    }

}
