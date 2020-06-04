package com.haotang.pet.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.haotang.base.BaseFragment;
import com.haotang.pet.FosterOrderDetailNewActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.OrderDetailFromOrderToConfirmActivity;
import com.haotang.pet.R;
import com.haotang.pet.WashOrderDetailActivity;
import com.haotang.pet.adapter.GratuityPriceAdapter;
import com.haotang.pet.adapter.OrderNewAdapter;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.ExitLoginEvent;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.Order;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.TabEntity;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.entity.WorkerInfo;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.PayUtils;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.alipay.Result;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
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

;

/**
 * <p>Title:${type_name}</p> androidx.recyclerview.widget.GridLayoutManager
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-11 11:22
 */
public class ServiceOrderFragment extends BaseFragment {
    @BindView(R.id.mTabLayout_4)
    CommonTabLayout mTabLayout4;
    @BindView(R.id.tv_emptyview_desc)
    TextView tv_emptyview_desc;
    @BindView(R.id.btn_emptyview)
    Button btn_emptyview;
    @BindView(R.id.ll_order_default)
    LinearLayout ll_order_default;
    @BindView(R.id.iv_emptyview_img)
    ImageView iv_emptyview_img;
    @BindView(R.id.rv_order_list)
    RecyclerView rv_order_list;
    @BindView(R.id.srl_order_list)
    SwipeRefreshLayout srl_order_list;
    private ArrayList<Order> orderlist = new ArrayList<Order>();
    private ArrayList<Order> localOrderlist = new ArrayList<Order>();
    private int page = 1;
    private int pageSize;
    private int oldindex = 0;
    private String[] mTitles = {"全部", "待付款", "待服务", "待评价", "已取消"};
    private int[] mIconUnselectIds = {
            R.drawable.tab_qb_normal, R.drawable.tab_dfk_normal,
            R.drawable.tab_dfw_normal, R.drawable.tab_dpj_normal, R.drawable.tab_yqx_normal};
    private int[] mIconSelectIds = {
            R.drawable.tab_qb_press, R.drawable.tab_dfk_press,
            R.drawable.tab_dfw_press, R.drawable.tab_dpj_press, R.drawable.tab_yqx_press};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] colors = new int[2];
    private int workerId;
    private String shopName;
    private String beautician_name;
    private String levelname;
    private String beautician_iamge;
    private String remark;
    private int paytype;
    private int orderId;
    private int orderStatus;
    private GratuityPriceAdapter priceAdapter;
    private List<WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean> priceList = new ArrayList<>();
    private String gratuity_content;
    private double payPrice = 0;
    // 第三方支付相关字段
    private String orderStr;
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;
    private String workerImg;
    private String detail;
    private PopupWindow pWinBottomDialog;
    private Button btn_pay_bottomdia;
    private OrderNewAdapter orderNewAdapter;
    private PopupWindow gratuityPop;
    private StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration;

    //登录返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        if (event != null && event.isLogin()) {
            refresh();
        }
    }
    //退出登录
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitEventd(ExitLoginEvent exitLoginEvent){
        refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(R.layout.serviceorder_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        findView();
        setView();
        setLinster();
        refresh();
    }

    private void init() {
        Global.WXPAYCODE = -1;
    }

    private void findView() {
    }

    private void setView() {
        //改变空白提示高度
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_emptyview_img.getLayoutParams();
        layoutParams.topMargin = ScreenUtil.dip2px(getContext(),110);
        iv_emptyview_img.setLayoutParams(layoutParams);
        ll_order_default.setGravity(Gravity.CENTER_HORIZONTAL);
        ll_order_default.setBackgroundColor(Color.TRANSPARENT);

        srl_order_list.setRefreshing(true);
        srl_order_list.setColorSchemeColors(Color.rgb(47, 223, 189));
        rv_order_list.setHasFixedSize(true);
        rv_order_list.setLayoutManager(new LinearLayoutManager(mActivity));
        orderNewAdapter = new OrderNewAdapter(R.layout.item_order_new, orderlist, mActivity);
        rv_order_list.setAdapter(orderNewAdapter);
        /*stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(orderNewAdapter);
        rv_order_list.addItemDecoration(stickyRecyclerHeadersDecoration);*/
        mTabLayout4.setGradient(true);
        mTabLayout4.setmTextSelectsize(mTabLayout4.sp2px(16));
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        mTabLayout4.setColors(colors);
        mTabLayout4.setIndicatorTextMiddle(true);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout4.setTabData(mTabEntities);
        mTabLayout4.setCurrentTab(oldindex);
    }

    private void setLinster() {
        orderNewAdapter.setOnOrderItemClickListener(new OrderNewAdapter.OnOrderItemClickListener() {
            @Override
            public void OnOrderItemClick(int position) {
                int orderid = orderlist.get(position).orderid;
                int type = orderlist.get(position).type;
                if (type == 2) {// 寄养
                    goOrderDetail(orderid, type, FosterOrderDetailNewActivity.class);
                } else if (type == 1) {// 洗美
                    goOrderDetail(orderid, type, WashOrderDetailActivity.class);
                } else {//其他(训犬，游泳)
                    goOrderDetail(orderid, type, OrderDetailFromOrderToConfirmActivity.class);
                }
            }
        });
        orderNewAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srl_order_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mTabLayout4.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout4.setmTextSelectsize(mTabLayout4.sp2px(16));
                if (position == 0) {
                    mTabLayout4.setIndicatorWidth(32);
                } else {
                    mTabLayout4.setIndicatorWidth(45);
                }
                if (oldindex != position) {
                    oldindex = position;
                    refresh();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        orderNewAdapter.setListener(new OrderNewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int workerid, int orderid, int status) {
                workerId = workerid;
                orderId = orderid;
                orderStatus = status;
                switch (orderStatus) {
                    case 4:
                        Global.ServerEvent(mActivity, Global.ServerEventID.typeid_216, Global.ServerEventID.click_gratuity_waiteservice);
                        break;
                    case 5:
                        Global.ServerEvent(mActivity, Global.ServerEventID.typeid_216, Global.ServerEventID.click_gratuity_servicedone);
                        break;
                    case 22:
                        Global.ServerEvent(mActivity, Global.ServerEventID.typeid_216, Global.ServerEventID.click_gratuity_inservice);
                        break;
                }
                getWorkerInfo(workerid);
            }
        });
    }

    public void refresh() {
        if(orderNewAdapter != null){
            orderNewAdapter.setEnableLoadMore(false);
            srl_order_list.setRefreshing(true);
            page = 1;
            getData(oldindex);
        }
    }

    private void loadMore() {
        getData(oldindex);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("OrderFragment"); // 统计页面
        if ((android.os.Build.MODEL.equals("OPPO R9m") || android.os.Build.MODEL.equals("OPPO R9s")) && android.os.Build.VERSION.RELEASE.equals("5.1") && Global.WXPAYCODE == 0) {
            Global.WXPAYCODE = -1;
            Log.e("支付成功", "onResume");
            goPayResult();
        }
    }

    private void goOrderDetail(int orderid, int type, Class cls) {
        Intent intent = new Intent(mActivity, cls);
        intent.putExtra("orderid", orderid);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void getData(int index) {
        mPDialog.showDialog();
        if (index == 0) {//全部
            CommUtil.allOrder(mActivity, 0, page, ingWorkerOrders);
        } else if (index == 2) {//待服务
            CommUtil.allOrder(mActivity, 1, page, ingWorkerOrders);
        } else if (index == 3) {//待评价
            CommUtil.allOrder(mActivity, 2, page, ingWorkerOrders);
        } else if (index == 4) {//查询已取消的订单
            CommUtil.allOrder(mActivity, 3, page, ingWorkerOrders);
        } else if (index == 1) {//待付款
            CommUtil.allOrder(mActivity, 4, page, ingWorkerOrders);
        } else {//全部
            CommUtil.allOrder(mActivity, 0, page, ingWorkerOrders);
        }
    }

    private AsyncHttpResponseHandler ingWorkerOrders = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.dimissDialog();
            localOrderlist.clear();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        /*if (jdata.has("nToBeComment") && !jdata.isNull("nToBeComment")) {
                            int nToBeComment = jdata.getInt("nToBeComment");
                            if (nToBeComment > 0) {
                                mTabLayout4.showMsg(3, nToBeComment);
                                mTabLayout4.setMsgMargin(3, -10, 12);
                                MsgView rtv_2_3 = mTabLayout4.getMsgView(3);
                                rtv_2_3.setWidth(DisplayUtil.dip2px(mContext, 50));
                                if (rtv_2_3 != null) {
                                    rtv_2_3.setTextSize(5);
                                    rtv_2_3.setBackgroundColor(Color.parseColor("#E73131"));
                                }
                            }
                        }*/
                        if (jdata.has("orders") && !jdata.isNull("orders")) {
                            JSONArray jorders = jdata.getJSONArray("orders");
                            if (jorders.length() > 0) {
                                for (int i = 0; i < jorders.length(); i++) {
                                    Order order = new Order();
                                    order.petImgList.clear();
                                    JSONObject jo = jorders.getJSONObject(i);
                                    if (jo.has("appointment") && !jo.isNull("appointment")) {
                                        order.starttime = jo.getString("appointment");
                                        String[] timeMonthAndYear = order.starttime.split(" ")[0].split("-");
                                        order.EverytYearAndMonth = timeMonthAndYear[0] + "-" + timeMonthAndYear[1];
                                        String[] timeLast = order.EverytYearAndMonth.split("-");
                                        if (timeLast[1].equals("12")) {
                                            order.yearAndMonth = order.EverytYearAndMonth + "月";
                                        } else {
                                            order.yearAndMonth = timeLast[1] + "月";
                                        }
                                    }
                                    if (jo.has("extraItem") && !jo.isNull("extraItem")) {
                                        JSONObject objectExtraItem = jo.getJSONObject("extraItem");
                                        if (objectExtraItem.has("item") && !objectExtraItem.isNull("item")) {
                                            JSONArray array = objectExtraItem.getJSONArray("item");
                                            if (array.length() > 0) {
                                                for (int j = 0; j < array.length(); j++) {
                                                    JSONObject objectEve = array.getJSONObject(j);
                                                    if (objectEve.has("id") && !objectEve.isNull("id")) {
                                                        order.itemIds.add(objectEve.getInt("id"));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (jo.has("extraServiceItems") && !jo.isNull("extraServiceItems")) {
                                        JSONArray extraArray = jo.getJSONArray("extraServiceItems");
                                        if (extraArray.length() > 0) {
                                            for (int j = 0; j < extraArray.length(); j++) {
                                                JSONObject extraObject = extraArray.getJSONObject(j);
                                                if (extraObject.has("id") && !extraObject.isNull("id")) {
                                                    order.itemIds.add(extraObject.getInt("id"));
                                                }
                                            }
                                        }
                                    }
                                    if (jo.has("remainTime") && !jo.isNull("remainTime")) {
                                        order.remainTime = jo.getLong("remainTime");
                                    }
                                    if (jo.has("isCanGratuity") && !jo.isNull("isCanGratuity")) {
                                        order.isCanGratuity = jo.getBoolean("isCanGratuity");
                                    }
                                    if (jo.has("id") && !jo.isNull("id")) {
                                        order.orderid = jo.getInt("id");
                                    }
                                    if (jo.has("workerId") && !jo.isNull("workerId")) {
                                        order.workerId = jo.getInt("workerId");
                                    }
                                    if (jo.has("serviceLoc") && !jo.isNull("serviceLoc")) {
                                        order.addrtype = jo.getInt("serviceLoc");
                                        order.serviceLoc = jo.getInt("serviceLoc");
                                    }
                                    if (jo.has("pickUp") && !jo.isNull("pickUp")) {
                                        order.pickup = jo.getInt("pickUp");
                                    }
                                    if (jo.has("petService") && !jo.isNull("petService")) {
                                        order.serviceid = jo.getInt("petService");
                                    }
                                    if (jo.has("type") && !jo.isNull("type")) {
                                        order.type = jo.getInt("type");
                                    }
                                    if (jo.has("status") && !jo.isNull("status")) {
                                        order.status = jo.getInt("status");
                                    }
                                    if (jo.has("statusDescription") && !jo.isNull("statusDescription")) {
                                        order.statusstr = jo.getString("statusDescription");
                                    }
                                    if (jo.has("totalPrice") && !jo.isNull("totalPrice")) {
                                        order.fee = jo.getDouble("totalPrice");
                                    }
                                    if (jo.has("petServicePojo") && !jo.isNull("petServicePojo")) {
                                        JSONObject jpp = jo.getJSONObject("petServicePojo");
                                        if (jpp.has("name") && !jpp.isNull("name")) {
                                            order.servicename = jpp.getString("name");
                                        }
                                        if (jpp.has("serviceType") && !jpp.isNull("serviceType")) {
                                            order.servicetype = jpp.getInt("serviceType");
                                        }
                                    }
                                    if (jo.has("task") && !jo.isNull("task")) {
                                        JSONObject jt = jo.getJSONObject("task");
                                        if (jt.has("startTime") && !jt.isNull("startTime")) {
                                            order.starttime = jt.getString("startTime");
                                        }
                                        if (jt.has("stopTime") && !jt.isNull("stopTime")) {
                                            order.endtime = jt.getString("stopTime");
                                        }
                                    }
                                    if (jo.has("pet") && !jo.isNull("pet")) {
                                        JSONObject jpet = jo.getJSONObject("pet");
                                        if (jpet.has("avatarPath") && !jpet.isNull("avatarPath")) {
                                            order.petimg = jpet.getString("avatarPath");
                                        }
                                        if (jpet.has("petName") && !jpet.isNull("petName")) {
                                            order.petname = jpet.getString("petName");
                                        }
                                        if (jpet.has("petKind") && !jpet.isNull("petKind")) {
                                            order.petkind = jpet.getInt("petKind");
                                        }
                                    }
                                    if (jo.has("myPet") && !jo.isNull("myPet")) {
                                        Pet pet = new Pet();
                                        JSONObject jmp = jo.getJSONObject("myPet");
                                        if (jmp.has("nickName") && !jmp.isNull("nickName")) {
                                            order.customerpetname = jmp.getString("nickName");
                                            pet.nickName = order.customerpetname;
                                        }
                                        if (jmp.has("avatarPath") && !jmp.isNull("avatarPath")) {
                                            order.petimg = jmp.getString("avatarPath");
                                            pet.image = order.petimg;
                                            order.petImgList.add(jmp.getString("avatarPath"));
                                        }
                                        if (jmp.has("id") && !jmp.isNull("id")) {
                                            pet.customerpetid = jmp.getInt("id");
                                        }
                                        if (jmp.has("petId") && !jmp.isNull("petId")) {
                                            pet.id = jmp.getInt("petId");
                                        }
                                        if (jo.has("petService") && !jo.isNull("petService")) {
                                            pet.serviceid = jo.getInt("petService");
                                        }
                                        if (jmp.has("petKind") && !jmp.isNull("petKind")) {
                                            pet.kindid = jmp.getInt("petKind");
                                        }
                                        order.listMyPets.add(pet);
                                    }
                                    if (jo.has("subOrderList")&&!jo.isNull("subOrderList")){
                                        JSONArray subOrderList = jo.getJSONArray("subOrderList");
                                        if (subOrderList!=null&&subOrderList.length()>0){
                                            for (int j = 0; j < subOrderList.length(); j++) {
                                                JSONObject pet = (JSONObject) subOrderList.get(j);
                                                order.petImgList.add(pet.getString("avatarPath"));
                                            }
                                        }
                                    }
                                    Utils.mLogError(order.petImgList.size()+"------------size");
                                    if (jo.has("serviceAddress") && !jo.isNull("serviceAddress")) {
                                        JSONObject objectAddress = jo.getJSONObject("serviceAddress");
                                        CommAddr commAddr = CommAddr.json2Entity(objectAddress);
                                        order.commAddr = commAddr;
                                    }
                                    if (jo.has("shop") && !jo.isNull("shop")) {
                                        ServiceShopAdd LastShop = new ServiceShopAdd();
                                        JSONObject jshop = jo.getJSONObject("shop");
                                        if (jshop.has("lat") && !jshop.isNull("lat")) {
                                            LastShop.shoplat = jshop.getDouble("lat");
                                        }
                                        if (jshop.has("lng") && !jshop.isNull("lng")) {
                                            LastShop.shoplng = jshop.getDouble("lng");
                                        }
                                        if (jshop.has("openTime") && !jshop.isNull("openTime")) {
                                            LastShop.openTime = jshop.getString("openTime");
                                        }
                                        if (jshop.has("shopWxNum") && !jshop.isNull("shopWxNum")) {
                                            LastShop.shopWxNum = jshop.getString("shopWxNum");
                                        }
                                        if (jshop.has("shopWxImg") && !jshop.isNull("shopWxImg")) {
                                            LastShop.shopWxImg = jshop.getString("shopWxImg");
                                        }
                                        if (jshop.has("shopActiveBackup") && !jshop.isNull("shopActiveBackup")) {
                                            LastShop.shopActiveBackup = jshop.getString("shopActiveBackup");
                                        }
                                        if (jshop.has("shopActiveTitle") && !jshop.isNull("shopActiveTitle")) {
                                            LastShop.shopActiveTitle = jshop.getString("shopActiveTitle");
                                        }
                                        if (jshop.has("shopActivePoint") && !jshop.isNull("shopActivePoint")) {
                                            LastShop.shopActivePoint = jshop.getInt("shopActivePoint");
                                        }
                                        if (jshop.has("shopName") && !jshop.isNull("shopName")) {
                                            LastShop.shopName = jshop.getString("shopName");
                                        }
                                        if (jshop.has("address") && !jshop.isNull("address")) {
                                            LastShop.shopAddress = jshop.getString("address");
                                        }
                                        if (jshop.has("phone") && !jshop.isNull("phone")) {
                                            LastShop.shopPhone = jshop.getString("phone");
                                        }
                                        if (jshop.has("img") && !jshop.isNull("img")) {
                                            order.fosterShopImg = jshop.getString("img");
                                        }
                                        if (jshop.has("shopName") && !jshop.isNull("shopName")) {
                                            order.fosterShopName = jshop.getString("shopName");
                                        }
                                        if (jshop.has("id") && !jshop.isNull("id")) {
                                            LastShop.shopId = jshop.getInt("id");
                                        }
                                        order.LastShop = LastShop;
                                    }
                                    if (jo.has("evaluate") && !jo.isNull("evaluate")) {
                                        order.evaluate = jo.getString("evaluate");
                                    }
                                    if (jo.has("btnTxt") && !jo.isNull("btnTxt")) {
                                        order.btnTxt = jo.getString("btnTxt");
                                    }
                                    if (jo.has("refType") && !jo.isNull("refType")) {
                                        order.refType = jo.getInt("refType");
                                    }
                                    if (jo.has("hotelStatus") && !jo.isNull("hotelStatus")) {
                                        order.hotelStatus = jo.getInt("hotelStatus");
                                    }
                                    if (jo.has("liveContent") && !jo.isNull("liveContent")) {
                                        order.liveContent = jo.getString("liveContent");
                                    }
                                    if (jo.has("liveInfo") && !jo.isNull("liveInfo")) {
                                        JSONObject jliveInfo = jo.getJSONObject("liveInfo");
                                        if (jliveInfo.has("liveUrl") && !jliveInfo.isNull("liveUrl")) {
                                            order.liveUrl = jliveInfo.getString("liveUrl");
                                        }
                                        if (jliveInfo.has("roomNum") && !jliveInfo.isNull("roomNum")) {
                                            order.liveName = jliveInfo.getString("roomNum");
                                        }
                                        if (jliveInfo.has("cameraState") && !jliveInfo.isNull("cameraState")) {
                                            order.cameraState = jliveInfo.getInt("cameraState");
                                        }
                                    }
                                    if (jo.has("payment") && !jo.isNull("payment")) {
                                        JSONObject object = jo.getJSONObject("payment");
                                        if (object.has("paymentTxt") && !object.isNull("paymentTxt")) {
                                            order.paymentTxt = object.getString("paymentTxt");
                                        }
                                        if (object.has("wxPaymentLink") && !object.isNull("wxPaymentLink")) {
                                            order.wxPaymentLink = object.getString("wxPaymentLink");
                                        }
                                        if (object.has("zfbPaymentLink") && !object.isNull("zfbPaymentLink")) {
                                            order.zfbPaymentLink = object.getString("zfbPaymentLink");
                                        }
                                        if (object.has("hint") && !object.isNull("hint")) {
                                            order.hint = object.getString("hint");
                                        }
                                    }
                                    if (jo.has("logcount") && !jo.isNull("logcount")) {
                                        JSONObject object = jo.getJSONObject("logcount");
                                        if (object.has("updateService") && !object.isNull("updateService")) {
                                            JSONObject objectUpdate = object.getJSONObject("updateService");
                                            if (objectUpdate.has("text") && !objectUpdate.isNull("text")) {
                                                order.updateText = objectUpdate.getString("text");
                                            }
                                            if (objectUpdate.has("logcountType") && !objectUpdate.isNull("logcountType")) {
                                                order.updateLogcountType = objectUpdate.getString("logcountType");
                                            }
                                            if (objectUpdate.has("activeId") && !objectUpdate.isNull("activeId")) {
                                                order.activityIdUpdata = objectUpdate.getString("activeId");
                                            }
                                            if (objectUpdate.has("upgradeHint") && !objectUpdate.isNull("upgradeHint")) {
                                                order.upgradeServiceHint = objectUpdate.getString("upgradeHint");
                                            }
                                        }
                                        if (object.has("addExtraItem") && !object.isNull("addExtraItem")) {
                                            JSONObject objectExtra = object.getJSONObject("addExtraItem");
                                            if (objectExtra.has("text") && !objectExtra.isNull("text")) {
                                                order.extratext = objectExtra.getString("text");
                                            }
                                            if (objectExtra.has("logcountType") && !objectExtra.isNull("logcountType")) {
                                                order.extraLogcountType = objectExtra.getString("logcountType");
                                            }
                                            if (objectExtra.has("activeId") && !objectExtra.isNull("activeId")) {
                                                order.activityIdExtrax = objectExtra.getString("activeId");
                                            }
                                            if (objectExtra.has("upgradeHint") && !objectExtra.isNull("upgradeHint")) {
                                                order.extraUpgradeHint = objectExtra.getString("upgradeHint");
                                            }
                                        }
                                    }
                                    localOrderlist.add(order);
                                }
                            }
                        }
                    }
                    if (page == 1) {
                        srl_order_list.setRefreshing(false);
                        orderNewAdapter.setEnableLoadMore(true);
                        orderlist.clear();
                    }
                    orderNewAdapter.loadMoreComplete();
                    if (localOrderlist != null && localOrderlist.size() > 0) {
                        setLayout(1, 0, "");
                        if (page == 1) {
                            pageSize = localOrderlist.size();
                        } else {
                            if (localOrderlist.size() < pageSize) {
                                orderNewAdapter.loadMoreEnd(false);
                            }
                        }
                        orderlist.addAll(localOrderlist);
                        page++;
                    } else {
                        if (page == 1) {
                            ll_order_default.setVisibility(View.VISIBLE);
                            srl_order_list.setVisibility(View.GONE);
                            btn_emptyview.setVisibility(View.GONE);
                            btn_emptyview.setText("立即预约");
                            btn_emptyview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.mainactivity");
                                    intent.putExtra("previous",
                                            Global.PRE_ORDER_LIST_TO_MAINACTIVITY);
                                    mActivity.sendBroadcast(intent);
                                }
                            });
                            Utils.setText(tv_emptyview_desc, "您还没有订单,赶紧去约一单吧~", "", View.VISIBLE, View.VISIBLE);
                            orderNewAdapter.loadMoreEnd(true);
                        } else {
                            orderNewAdapter.loadMoreEnd(false);
                        }
                    }
                    orderNewAdapter.notifyDataSetChanged();
                } else {
                    if (Utils.checkLogin(mContext)) {
                        if (page == 1) {
                            setLayout(2, 1, msg);
                            orderNewAdapter.setEnableLoadMore(false);
                            srl_order_list.setRefreshing(false);
                        } else {
                            orderNewAdapter.setEnableLoadMore(true);
                            orderNewAdapter.loadMoreFail();
                        }
                    } else {
                        ll_order_default.setVisibility(View.VISIBLE);
                        srl_order_list.setVisibility(View.GONE);
                        btn_emptyview.setVisibility(View.VISIBLE);
                        btn_emptyview.setText("去登录");
                        btn_emptyview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mContext, LoginNewActivity.class));
                            }
                        });
                        Utils.setText(tv_emptyview_desc, "登录后查看订单", "", View.VISIBLE, View.VISIBLE);
                        orderNewAdapter.loadMoreEnd(true);
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    setLayout(2, 1, "数据异常");
                    orderNewAdapter.setEnableLoadMore(false);
                    srl_order_list.setRefreshing(false);
                } else {
                    orderNewAdapter.setEnableLoadMore(true);
                    orderNewAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            if (orderlist.size() > 0) {
                for (int i = 0; i < orderlist.size(); i++) {
                    if (i == 0) {
                        orderlist.get(i).headerId = i;
                    } else {
                        String[] YearAndMonthOne = orderlist.get(i - 1).starttime.split(" ")[0].split("-");
                        String dataOne = YearAndMonthOne[0] + "-" + YearAndMonthOne[1];
                        String[] YearAndMonthTwo = orderlist.get(i).starttime.split(" ")[0].split("-");
                        String dataTwo = YearAndMonthTwo[0] + "-" + YearAndMonthTwo[1];
                        if (dataOne.equals(dataTwo)) {
                            orderlist.get(i).headerId = orderlist.get(i - 1).headerId;
                        } else {
                            orderlist.get(i).headerId = i;
                        }
                    }
                }
            }
            orderNewAdapter.notifyDataSetChanged();
            //stickyRecyclerHeadersDecoration.invalidateHeaders();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                setLayout(2, 1, "请求失败");
                orderNewAdapter.setEnableLoadMore(false);
                srl_order_list.setRefreshing(false);
            } else {
                orderNewAdapter.setEnableLoadMore(true);
                orderNewAdapter.loadMoreFail();
            }
        }
    };

    private void setLayout(int type, int flag, String msg) {
        if (type == 1) {
            ll_order_default.setVisibility(View.GONE);
            srl_order_list.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_order_default.setVisibility(View.VISIBLE);
            srl_order_list.setVisibility(View.GONE);
            if (flag == 1) {
                btn_emptyview.setVisibility(View.VISIBLE);
                btn_emptyview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            } else if (flag == 2) {
                btn_emptyview.setVisibility(View.GONE);
            }
            Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        }
    }

    public void logcountAdd(String typeid, String activeid) {
        CommUtil.logcountAdd(mActivity, typeid, activeid, statisticsHandler);
    }

    private AsyncHttpResponseHandler statisticsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void getWorkerInfo(int workerId) {
        CommUtil.workerInfo(mActivity, workerId, workerInfoHandler);
    }

    private AsyncHttpResponseHandler workerInfoHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultcode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultcode == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        Gson gson = new Gson();
                        WorkerInfo workerInfo = gson.fromJson(new String(responseBody), WorkerInfo.class);
                        WorkerInfo.DataBean workerInfoData = workerInfo.getData();
                        shopName = workerInfoData.getShopName();
                        beautician_name = workerInfoData.getRealName();
                        levelname = workerInfoData.getLevel().getName();
                        beautician_iamge = workerInfoData.getAvatar();
                        priceList.clear();
                        List<WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean> gratuityInfoList = workerInfoData.getGratuityInfo().getGratuityInfo();
                        priceList.addAll(gratuityInfoList);
                        gratuity_content = workerInfoData.getGratuityInfo().getContent_1();
                        showGratuityDialog();
                    }
                } else {
                    ToastUtil.showToastShortBottom(mActivity, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void showPayDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(mActivity, R.layout.appoint_pay_bottom_dialog, null);
        btn_pay_bottomdia = (Button) customView.findViewById(R.id.btn_pay_bottomdia);
        ImageView iv_pay_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_close);
        TextView tv_pay_title = (TextView) customView.findViewById(R.id.tv_pay_title);
        customView.findViewById(R.id.ll_pay_bottomdia_time).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_minute).setVisibility(View.GONE);
        customView.findViewById(R.id.tv_pay_bottomdia_time_second).setVisibility(View.GONE);
        customView.findViewById(R.id.rl_pay_bottomdia_yqm).setVisibility(View.GONE);
        LinearLayout ll_pay_bottomdia_weixin = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_weixin);
        final ImageView iv_pay_bottomdia_weixin_select = (ImageView) customView.findViewById(R.id.iv_pay_bottomdia_weixin_select);
        LinearLayout ll_pay_bottomdia_zfb = (LinearLayout) customView.findViewById(R.id.ll_pay_bottomdia_zfb);
        customView.findViewById(R.id.iv_pay_bottomdia_yqm_select).setVisibility(View.GONE);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(mActivity)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        btn_pay_bottomdia.setText("去支付" + payPrice + "元");
        if (spUtil.getInt("payway", 0) == 1) {
            paytype = 1;
            iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_unselect);
            iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_select);
        } else if (spUtil.getInt("payway", 0) == 2) {
            paytype = 2;
            iv_pay_bottomdia_weixin_select.setImageResource(R.drawable.icon_petadd_unselect);
            iv_pay_bottomdia_zfb_select.setImageResource(R.drawable.icon_petadd_select);
        }
        tv_pay_title.setText("请选择支付方式");
        btn_pay_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paytype != 1 && paytype != 2) {
                    ToastUtil.showToastShortBottom(mContext, "请选择支付方式");
                    return;
                } else {
                    //付款
                    pWinBottomDialog.dismiss();
                    gratuityPop.dismiss();
                    goGratuity();
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
        iv_pay_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
    }

    private void goGratuity() {
        int sourceType = 0;
        switch (orderStatus) {
            case 4:
                sourceType = 221;
                break;
            case 5:
                sourceType = 220;
                break;
            case 22:
                sourceType = 224;
                break;
        }
        CommUtil.gratuityPay(mActivity, workerId, sourceType, paytype, payPrice, orderId, remark, gratuityHandler);
    }

    private AsyncHttpResponseHandler gratuityHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.dimissDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0 && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata.has("workerImg") && !jdata.isNull("workerImg")) {
                        workerImg = jdata.getString("workerImg");
                    }
                    if (jdata.has("detail") && !jdata.isNull("detail")) {
                        detail = jdata.getString("detail");
                    }
                    if (jdata.has("payInfo") && !jdata.isNull("payInfo")) {
                        appid = null;
                        noncestr = null;
                        packageValue = null;
                        partnerid = null;
                        prepayid = null;
                        sign = null;
                        timestamp = null;
                        orderStr = null;
                        JSONObject jpayInfo = jdata
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
                        goPay();
                    }
                } else {
                    ToastUtil.showToastShortBottom(mActivity, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.dimissDialog();
        }
    };

    private void goPay() {
        if (paytype == 1) {
            if (appid != null && !TextUtils.isEmpty(appid) && noncestr != null
                    && !TextUtils.isEmpty(noncestr) && packageValue != null
                    && !TextUtils.isEmpty(packageValue) && partnerid != null
                    && !TextUtils.isEmpty(partnerid) && prepayid != null
                    && !TextUtils.isEmpty(prepayid) && sign != null
                    && !TextUtils.isEmpty(sign) && timestamp != null
                    && !TextUtils.isEmpty(timestamp)) {
                // 微信支付
                spUtil.saveInt("payway", 1);
                mPDialog.showDialog();
                PayUtils.weChatPayment(mActivity, appid,
                        partnerid, prepayid, packageValue, noncestr, timestamp,
                        sign, mPDialog);
            } else {
                ToastUtil.showToastShortBottom(mActivity,
                        "支付参数错误");
            }
        } else if (paytype == 2) {
            if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                // 判断是否安装支付宝
                spUtil.saveInt("payway", 2);
                PayUtils.checkAliPay(mActivity, mpHandler);
            } else {
                ToastUtil.showToastShortBottom(mActivity,
                        "支付参数错误");
            }
        }
    }

    private void showGratuityDialog() {
        payPrice = 0;
        priceAdapter = new GratuityPriceAdapter(mActivity);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, priceList.size());
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        priceAdapter.setPriceList(priceList);
        ViewGroup customView = (ViewGroup) View.inflate(mActivity, R.layout.appoint_gratuity_bottom_dialog, null);
        ImageView iv_close = (ImageView) customView.findViewById(R.id.iv_gratuity_bottomdia_close);
        ImageView iv_head = (ImageView) customView.findViewById(R.id.iv_beautician_head);
        TextView tv_content = (TextView) customView.findViewById(R.id.tv_gratuity_content);
        RecyclerView rv_price = (RecyclerView) customView.findViewById(R.id.rv_gratuity_price);
        final TextView tv_mark = (TextView) customView.findViewById(R.id.tv_gratuity_mark);
        Button btn_gratuity = (Button) customView.findViewById(R.id.btn_gratuity_bottomdia);
        rv_price.setLayoutManager(layoutManager);
        rv_price.setAdapter(priceAdapter);
        TextView tv_name = (TextView) customView.findViewById(R.id.tv_beautician_name);
        TextView tv_level = (TextView) customView.findViewById(R.id.tv_beautician_level);
        TextView tv_address = (TextView) customView.findViewById(R.id.tv_beautician_address);
        RelativeLayout rv_close = (RelativeLayout) customView.findViewById(R.id.rv_close);
        tv_address.setText(shopName);
        tv_name.setText(beautician_name);
        tv_content.setText(gratuity_content);
        tv_level.setText(levelname + "美容师");
        GlideUtil.loadCircleImg(mContext, beautician_iamge, iv_head, R.drawable.icon_default);
        gratuityPop = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        gratuityPop.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        gratuityPop.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        gratuityPop.setOutsideTouchable(true);
        //设置可以点击
        gratuityPop.setTouchable(true);
        //进入退出的动画
        gratuityPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        gratuityPop.setWidth(Utils.getDisplayMetrics(mActivity)[0]);
        gratuityPop.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gratuityPop.dismiss();
            }
        });
        rv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gratuityPop.dismiss();
            }
        });
        priceAdapter.setListener(new GratuityPriceAdapter.ItemClickListener() {
            @Override
            public void onItemClick(WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean gratuityInfoBean) {
                if (gratuityInfoBean != null) {
                    payPrice = gratuityInfoBean.getAmount();
                    remark = gratuityInfoBean.getRemark();
                    tv_mark.setText(remark);
                }
            }
        });
        btn_gratuity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payPrice != 0) {
                    showPayDialog();
                } else {
                    ToastUtil.showToastShortBottom(mActivity, "请选择金额");
                }
            }
        });
    }

    private void goPayResult() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(mActivity, R.layout.alert_gratuity_layout, null);
        alertDialog.setView(view);
        alertDialog.show();
        ImageView iv_head = (ImageView) view.findViewById(R.id.iv_beautician_head);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_thank);
        RelativeLayout rv_close = (RelativeLayout) view.findViewById(R.id.rl_close);
        GlideUtil.loadCircleImg(mContext, workerImg, iv_head, R.drawable.icon_default);
        tv_content.setText(detail.replace("\\n", "\n"));
        rv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    //微信支付结果返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayResultEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            BaseResp resp = event.getResp();
            if (resp != null) {
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

    // 支付宝支付begin
    Handler mpHandler = new Handler() {
        public void handleMessage(Message msg) {
            Utils.mLogError("支付宝返回码：" + msg.what);
            switch (msg.what) {
                case Global.ALI_SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    Utils.mLogError("支付宝返回码：" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        goPayResult();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”
                        // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToastShort(mActivity,
                                    "支付结果确认中!");
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            // 从支付宝返回的状态
                        } else {
                            ToastUtil.showToastShort(mActivity,
                                    "支付失败,请重新支付!");
                        }
                    }
                    break;
                case Global.ALI_SDK_CHECK_FLAG:
                    if (orderStr != null && !TextUtils.isEmpty(orderStr)) {
                        // 支付宝支付
                        mPDialog.showDialog();
                        PayUtils.payByAliPay(mActivity, orderStr,
                                mpHandler, mPDialog);
                    } else {
                        ToastUtil.showToastShortBottom(mActivity,
                                "支付参数错误");
                    }
                    break;
            }
        }

        ;
    };

    public static ServiceOrderFragment getInstance(String title) {
        ServiceOrderFragment sf = new ServiceOrderFragment();
        return sf;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
