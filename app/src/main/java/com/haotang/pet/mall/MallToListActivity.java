package com.haotang.pet.mall;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.haotang.base.SuperActivity;
import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.ShopMallOrderActivity;
import com.haotang.pet.ShoppingCartActivity;
import com.haotang.pet.adapter.MallAdapter.MallOpenDetailAdapter;
import com.haotang.pet.adapter.MallAdapter.MallSearchResultOneAdapter;
import com.haotang.pet.adapter.MallAdapter.MallSearchResultRecycleViewAdapter;
import com.haotang.pet.adapter.MallAdapter.MallToListAdapter;
import com.haotang.pet.entity.mallEntity.MallSearchGoods;
import com.haotang.pet.entity.mallEntity.MallToListTopTwoIcon;
import com.haotang.pet.entity.mallEntity.NavigationCondition;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshGridView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.HorizontalListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/8/30.
 */

public class MallToListActivity extends SuperActivity implements View.OnClickListener{
    private ImageView img_left_title;
//    private Button bt_choosepet_dog,bt_choosepet_cat;
    private LinearLayout layout_right_show;
    private TextView tv_mall_title;
    private PullToRefreshGridView prl_mall_to_grid;
    private LinearLayout top_navigation_open_detail;
    private GridView gridView_navigation_open_detail;
    private RelativeLayout rl_cashback_tip;
    private TextView tv_cashback_tip;
    private ImageView iv_cashback_close;
    private Button button_reset,button_ok;
    private LinearLayout layout_open_detail_bottom;
    private RelativeLayout rl_second_class;
    private boolean isCat;
    private MallToListAdapter mallToListAdapter;
//    private HorizontalScrollView hsv_appointfrg_date;
//    private GridView gv_appointfrg_top;
    private int itemwidth;
    private MallSearchResultOneAdapter mallSearchResultOneAdapter;
    private List<MallSearchGoods> TotalList = new ArrayList<>();
    private int CurrentPosition = -1;
    private NavigationCondition navCon = null;
    private NavigationCondition.NavigationOpenDetail openClick = null;
    private List<NavigationCondition> topTags = new ArrayList<>();
    private List<NavigationCondition.NavigationOpenDetail> openDetailList = new ArrayList<>();
    private MallOpenDetailAdapter openDetailAdapter;
//    private MallSearchAdapter mallSearchAdapter;
    private List<MallToListTopTwoIcon> TopAndIsBottomList = new ArrayList<>();
    public List<MallToListTopTwoIcon.MallToListThr> EveryThrList = new ArrayList<>();
    private MallToListTopTwoIcon dogUse = new MallToListTopTwoIcon();
    private MallToListTopTwoIcon catUse = new MallToListTopTwoIcon();
    private HorizontalListView mallSearchTag;
    private int page=1;
    private int classificationId=-1;
    private int classificationIdCurrent=-1;
    private String publicAttribute;
    private RelativeLayout layout_order_mall;
    private TextView textview_order_mall;
    private RelativeLayout layout_buy_car_mall;
    private TextView textview_buy_car;
    private int userMallCartNum;
    private int orderCount;
    private ImageView img_scroll_top;
    private boolean isListScroll = false;
    private boolean isGridScroll = false;
    private boolean isClicked = false;
    private RecyclerView recyleview_mall_tag;
    private MallSearchResultRecycleViewAdapter mallSearchResultRecycleViewAdapter;


    public LinearLayout layout_undata_network_isdie;
    public TextView textview_notice;
    public TextView textview_update;
    private TabLayout tabs;
    private ImageView img_undata_networkisdie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_to_list);
        MApplication.listAppoint.add(mContext);
        TotalList.clear();
        topTags.clear();
        initData();
        initView();
        setView();
        initListener();
//        getTagData();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryOrderAndCart();
    }

    private void initData() {
        classificationId = getIntent().getIntExtra("classificationId",-1);
    }

    private void getSearchList() {//
        mPDialog.showDialog();
        StringBuilder ids = new StringBuilder();
        for (int i =0;i<topTags.size();i++){
            NavigationCondition nav =  topTags.get(i);
            if (!TextUtils.isEmpty(nav.publicAttribute)){
                ids.append(nav.publicAttribute);
            }
        }
        publicAttribute = "";
        if (ids.length()>0){
            publicAttribute = ids.toString();
        }
        CommUtil.mallSearchCommodityList(mContext,classificationIdCurrent,publicAttribute,"",-1,-1,page,searchHandler);
    }
    private AsyncHttpResponseHandler searchHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                prl_mall_to_grid.onRefreshComplete();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    if (object.has("data")&&!object.isNull("data")){
                        JSONObject objData = object.getJSONObject("data");
                        if (objData.has("commodityList")&&!objData.isNull("commodityList")){
                            JSONArray array = objData.getJSONArray("commodityList");
                            if (array.length()>0){
                                page++;
                                for (int i = 0;i<array.length();i++){
                                    TotalList.add(MallSearchGoods.json2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                    }
                }
//                if (TotalList.size()>0){
                    mallToListAdapter.notifyDataSetChanged();
//                }else
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            prl_mall_to_grid.onRefreshComplete();
            mPDialog.closeDialog();

        }
    };
    private void getTagData(){
        topTags.clear();
        mPDialog.showDialog();
        CommUtil.mallQueryPublicAttributeList(mContext,classificationIdCurrent,null,handler);
    }
    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    if (object.has("data")&&!object.isNull("data")){
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("publicAttributeTypeList")&&!objectData.isNull("publicAttributeTypeList")){
                            JSONArray arrayTypeList =  objectData.getJSONArray("publicAttributeTypeList");
                            if (arrayTypeList.length()>0){
                                for (int i = 0;i<arrayTypeList.length();i++){
                                    NavigationCondition navigationCondition = new NavigationCondition();
                                    JSONObject object1 = arrayTypeList.getJSONObject(i);
                                    if (object1.has("id")&&!object1.isNull("id")){
                                        navigationCondition.id = object1.getInt("id");
                                    }
                                    if (object1.has("name")&&!object1.isNull("name")){
                                        navigationCondition.NavigationName = object1.getString("name");
                                    }
                                    if (object1.has("sort")&&!object1.isNull("sort")){
                                        navigationCondition.sort = object1.getInt("sort");
                                    }
                                    if (object1.has("isDel")&&!object1.isNull("isDel")){
                                        navigationCondition.isDel = object1.getInt("isDel");
                                    }
                                    if (object1.has("created")&&!object1.isNull("created")){
                                        navigationCondition.created = object1.getString("created");
                                    }
                                    if (object1.has("extendParam")&&!object1.isNull("extendParam")){
                                        JSONObject objectExtend = object1.getJSONObject("extendParam");
                                        if (objectExtend.has("attributeList")&&!objectExtend.isNull("attributeList")){
                                            JSONArray arrayAttributelist = objectExtend.getJSONArray("attributeList");
                                            if (arrayAttributelist.length()>0){
                                                for (int j = 0;j<arrayAttributelist.length();j++){
                                                    NavigationCondition.NavigationOpenDetail navigationOpenDetail = new NavigationCondition().new NavigationOpenDetail();
                                                    JSONObject object2 = arrayAttributelist.getJSONObject(j);
                                                    if (object2.has("id")&&!object2.isNull("id")){
                                                        navigationOpenDetail.id = object2.getInt("id");
                                                    }
                                                    if (object2.has("name")&&!object2.isNull("name")){
                                                        navigationOpenDetail.NavigationOpenDetailName = object2.getString("name");
                                                    }
                                                    if (object2.has("sort")&&!object2.isNull("sort")){
                                                        navigationOpenDetail.sort = object2.getInt("sort");
                                                    }
                                                    if (object2.has("isDel")&&!object2.isNull("isDel")){
                                                        navigationOpenDetail.isDel = object2.getInt("isDel");
                                                    }
                                                    if (object2.has("created")&&!object2.isNull("created")){
                                                        navigationOpenDetail.created = object2.getString("created");
                                                    }
                                                    navigationCondition.openDetailList.add(navigationOpenDetail);
                                                }
                                            }
                                        }
                                    }
                                    topTags.add(navigationCondition);
                                }
                                if (topTags.size()>0){
                                    /**
                                     * 二级分类 品牌 适用对象之流
                                     */
                                    rl_second_class.setVisibility(View.VISIBLE);
                                    mallSearchResultRecycleViewAdapter.notifyDataSetChanged();
                                }else {
                                    rl_second_class.setVisibility(View.GONE);
                                }

                            }
                        }
                    }
                }else {
                    rl_second_class.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Utils.mLogError("==-->"+e.getMessage());
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };
    private void getData() {
        CommUtil.mallMallCommodity(mContext,classificationId,twoThrHandler);
    }
    private AsyncHttpResponseHandler twoThrHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0){
                    if (object.has("data")&&!object.isNull("data")){
                        JSONObject jobj = object.optJSONObject("data");
                        MallToListTopTwoIcon mallToListTopTwoIcon = new MallToListTopTwoIcon();
                        if (jobj.has("title")&&!jobj.isNull("title")){
                            tv_mall_title.setText(jobj.getString("title"));
                        }
                        //返现金额
                        if (jobj.has("cashBack")&&!jobj.isNull("cashBack")){
                            double cashBack = jobj.getDouble("cashBack");
                            if(isClicked){
                                rl_cashback_tip.setVisibility(View.GONE);
                            }else if (cashBack>0){
                                rl_cashback_tip.setVisibility(View.VISIBLE);
                                tv_cashback_tip.setText("您有"+cashBack+"元的返现金额等待使用～");
                            }else if (cashBack<=0){
                                rl_cashback_tip.setVisibility(View.GONE);
                            }

                        }
                        //三级列表
                        if (jobj.has("thirdList")&&!jobj.isNull("thirdList")){
                            JSONArray arrayThr = jobj.getJSONArray("thirdList");
                            if (arrayThr.length()>0){
                                for (int j =0;j<arrayThr.length();j++){
                                    JSONObject object3 = arrayThr.getJSONObject(j);
                                    MallToListTopTwoIcon.MallToListThr mallToListThr = new MallToListTopTwoIcon().new MallToListThr();
                                    if (object3.has("id")&&!object3.isNull("id")){
                                        mallToListThr.id = object3.getInt("id");
                                    }
                                    if (object3.has("title")&&!object3.isNull("title")){
                                        mallToListThr.title = object3.getString("title");
                                    }
                                    if (object3.has("pid")&&!object3.isNull("pid")){
                                        mallToListThr.pid = object3.getInt("pid");
                                    }
                                    if (object3.has("level")&&!object3.isNull("level")){
                                        mallToListThr.level = object3.getInt("level");
                                    }
                                    if (object3.has("sort")&&!object3.isNull("sort")){
                                        mallToListThr.sort = object3.getInt("sort");
                                    }
                                    if (object3.has("status")&&!object3.isNull("status")){
                                        mallToListThr.status = object3.getInt("status");
                                    }
                                    if (object3.has("isDel")&&!object3.isNull("isDel")){
                                        mallToListThr.isDel = object3.getInt("isDel");
                                    }
                                    if (object3.has("created")&&!object3.isNull("created")){
                                        mallToListThr.created = object3.getString("created");
                                    }
                                    if (object3.has("updateTime")&&!object3.isNull("updateTime")){
                                        mallToListThr.updateTime = object3.getString("updateTime");
                                    }
                                    mallToListTopTwoIcon.ThrList.add(mallToListThr);
                                }
                            }
                        }
                        if (mallToListTopTwoIcon.ThrList.size()>0){
                            EveryThrList.addAll(mallToListTopTwoIcon.ThrList);
                            tabs.setVisibility(View.VISIBLE);
                            if (EveryThrList.size()>0){
                                classificationIdCurrent = EveryThrList.get(0).id;
                            }else {
                                classificationIdCurrent=-1;
                            }
                            if (EveryThrList.size()<=4){
                                tabs.setTabMode(TabLayout.MODE_FIXED);
                            }else {
                                tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
                            }

                            setTabStyleFromXml();
                        }else {
                            mallSearchTag.setVisibility(View.GONE);
                        }
                        if (EveryThrList.size()<=0){
                            classificationIdCurrent = -1;
                        }
                        getSearchList();
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void setTabStyleFromXml() {
        /*float countTextWidth = 0;
        for (int i = 0;i<EveryThrList.size();i++){
            Paint paint= new Paint();
            float TextWidth = paint.measureText(EveryThrList.get(i).title);
            countTextWidth+=TextWidth;
            Utils.mLogError("==-->111111111111     0  "+countTextWidth+"  TextWidth "+TextWidth);
        }
        Utils.mLogError("==-->1111111111  1   "+Utils.getDisplayMetrics(mContext)[0]);*/
        Log.e("aaaaaaa",EveryThrList.size()+"--------");
        for (int i =0;i<EveryThrList.size();i++){
//                                tabs.addTab(tabs.newTab().setText(EveryThrList.get(i).title));
            TabLayout.Tab tabLayout = tabs.newTab();
            View view= LayoutInflater.from(mContext).inflate(R.layout.item_tablayout_hasimg,null);
            TextView textview_item_name = (TextView) view.findViewById(R.id.textview_item_name);
            if (EveryThrList.get(i).title.length()>4){
                if (EveryThrList.size()>=5){
                    String title = EveryThrList.get(i).title;
                    textview_item_name.setText(title.substring(0,4)+"..");
                }else {
                    String title = EveryThrList.get(i).title;
                    textview_item_name.setText(title);
                }
            }else {
                textview_item_name.setText(EveryThrList.get(i).title);
            }
            tabLayout.setCustomView(textview_item_name);
            tabs.addTab(tabLayout);
        }
        if (EveryThrList.size()<=0){
            getTagData();//公共属性重新请求
        }
    }

    private void queryOrderAndCart(){
        CommUtil.queryOrderAndCart(mContext,queryOrderAndhandler);
    }
    private AsyncHttpResponseHandler queryOrderAndhandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("cartCount") && !jdata.isNull("cartCount")) {
                                userMallCartNum = jdata.getInt("cartCount");
                                if (userMallCartNum>0){
                                    textview_buy_car.bringToFront();
                                    textview_buy_car.setVisibility(View.VISIBLE);
                                    if (userMallCartNum<=99){
                                        textview_buy_car.setText(""+userMallCartNum+"");
                                    }else {
                                        textview_buy_car.setText(""+userMallCartNum+"+");
                                    }
                                }else {
                                    textview_buy_car.setVisibility(View.GONE);
                                }
                            }else {
                                textview_buy_car.setVisibility(View.GONE);
                            }
                            if (jdata.has("orderCount") && !jdata.isNull("orderCount")) {
                                orderCount = jdata.getInt("orderCount");
                                if (orderCount>0){
                                    textview_order_mall.bringToFront();
                                    textview_order_mall.setVisibility(View.VISIBLE);
                                    if (orderCount<=99){
                                        textview_order_mall.setText(""+orderCount+"");
                                    }else {
                                        textview_order_mall.setText(""+orderCount+"+");
                                    }
                                }else {
                                    textview_order_mall.setVisibility(View.GONE);
                                }
                            }else {
                                textview_order_mall.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        textview_buy_car.setText("");
                        textview_buy_car.setText("");
                        textview_buy_car.setVisibility(View.GONE);
                        textview_order_mall.setVisibility(View.GONE);
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private void initListener() {

        mallSearchTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MallToListTopTwoIcon.MallToListThr mallToListThr = (MallToListTopTwoIcon.MallToListThr) mallSearchResultOneAdapter.getItem(position);
                classificationIdCurrent = mallToListThr.id;
                mallSearchResultOneAdapter.setChoosePos(position);
                mallSearchResultOneAdapter.notifyDataSetChanged();
                page=1;
                TotalList.clear();
                getSearchList();
            }
        });
        gridView_navigation_open_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openClick = (NavigationCondition.NavigationOpenDetail) parent.getItemAtPosition(position);
                if (openClick.isChoose == 0){
                    int count =0;
                    for (int i = 0;i<openDetailList.size();i++){
                        if (openDetailList.get(i).isChoose==1){
                            count++;
                        }
                    }
                    if (count>=5){
                        ToastUtil.showToastShortCenter(mContext,"最多选择5个哦~");
                        return;
                    }
                    openClick.isChoose = 1;
                }else if (openClick.isChoose ==1){
                    openClick.isChoose = 0;
                }
                openDetailList.set(position,openClick);
                try {
                    openDetailAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        prl_mall_to_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (TotalList!=null&&TotalList.size()>0){
                    Intent intent = new Intent(mContext, CommodityDetailActivity.class);
                    intent.putExtra("commodityId", TotalList.get(i).id);
                    intent.putExtra("source", Global.SOURCE_MALLCLASSIFY);
                    UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_Classify_ToDetail);
                    startActivity(intent);
                }
            }
        });

        prl_mall_to_grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem>=0&&firstVisibleItem<=10){
                    isGridScroll = false;
                    img_scroll_top.setVisibility(View.GONE);
                }else {
                    isGridScroll = true;
                    img_scroll_top.setVisibility(View.VISIBLE);
                }
            }
        });
        mallSearchResultRecycleViewAdapter.setOnItemClickRecyleView(new MallSearchResultRecycleViewAdapter.OnItemClickRecyleView() {
            @Override
            public void click(View v, int position) {
                UnClickOkState();
                top_navigation_open_detail.setVisibility(View.GONE);
                layout_open_detail_bottom.setVisibility(View.GONE);
                CurrentPosition = position;
//                resetNavOpenDetailIsCLICK();
                setNavTopIsOpen(position);
                navCon = (NavigationCondition) topTags.get(position);
                if (navCon.openDetailList.size()<=0){
                    return;
                }
                openDetailList = navCon.openDetailList;
                if (navCon.isOpen==1){
                    navCon.isOpen=0;
                    top_navigation_open_detail.setVisibility(View.GONE);
                    layout_open_detail_bottom.setVisibility(View.GONE);
                }else if(navCon.isOpen==0){
                    navCon.isOpen=1;
                    top_navigation_open_detail.setVisibility(View.VISIBLE);
                    layout_open_detail_bottom.setVisibility(View.VISIBLE);
                }
                if (navCon.isOpen==1){
                    openDetailAdapter = new MallOpenDetailAdapter<NavigationCondition.NavigationOpenDetail>(mContext,openDetailList);
                    gridView_navigation_open_detail.setAdapter(openDetailAdapter);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams();
                }
                topTags.set(position,navCon);

                int height =  setListViewHeightBasedOnChildren(gridView_navigation_open_detail);
                int wh [] = Utils.getDisplayMetrics(mContext);
                LinearLayout.LayoutParams params = null;
                if (height>wh[1]){
                    params = new LinearLayout.LayoutParams(wh[0],wh[1] / 2);
                }else {
                    if (height + 550 >=wh[1]){
                        params = new LinearLayout.LayoutParams(wh[0],wh[1] / 2);
                    }else {
                        params = new LinearLayout.LayoutParams(wh[0],height);
                    }
                }
                gridView_navigation_open_detail.setLayoutParams(params);
                Utils.mLogError("==-->wh "+ height +" 屏幕 "+wh[1]);

                mallSearchResultRecycleViewAdapter.notifyDataSetChanged();

            }
        });
        recyleview_mall_tag.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                UnClickOkState();
                top_navigation_open_detail.setVisibility(View.GONE);
                layout_open_detail_bottom.setVisibility(View.GONE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int postionTab = tab.getPosition();
//                ToastUtil.showToastShortCenter(mContext,""+postionTab);
                if (EveryThrList.size()>0){
                    classificationIdCurrent = EveryThrList.get(postionTab).id;
                }else {
                    classificationIdCurrent=-1;
                }
                page=1;
                TotalList.clear();
                getSearchList();
                UnClickOkState();
                getTagData();//公共属性重新请求

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setView() {
        boolean isNet = Utils.checkNet(mContext);
        if (isNet){
            textview_notice.setText("没有更多商品");
            img_undata_networkisdie.setBackgroundResource(R.drawable.search_undata);
        }else {
            textview_notice.setText("啊哦~网络竟然崩溃了 请检查您的网络");
            img_undata_networkisdie.setBackgroundResource(R.drawable.network_is_die);
            mallSearchTag.setVisibility(View.GONE);
            tabs.setVisibility(View.GONE);
            rl_second_class.setVisibility(View.GONE);
        }
        prl_mall_to_grid.setEmptyView(layout_undata_network_isdie);
        mallToListAdapter = new MallToListAdapter<MallSearchGoods>(mContext,TotalList);
        prl_mall_to_grid.setMode(PullToRefreshBase.Mode.BOTH);
        prl_mall_to_grid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
                    // 下拉刷新
                    TotalList.clear();
                    page=1;
                    getSearchList();
                } else {
                    getSearchList();
                }
            }
        });
        prl_mall_to_grid.setAdapter(mallToListAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyleview_mall_tag.setLayoutManager(linearLayoutManager);
        mallSearchResultRecycleViewAdapter = new MallSearchResultRecycleViewAdapter(mContext,topTags);
        recyleview_mall_tag.setAdapter(mallSearchResultRecycleViewAdapter);

    }

    private void initView() {
        img_left_title = (ImageView) findViewById(R.id.img_left_title);
        layout_right_show = (LinearLayout) findViewById(R.id.layout_right_show);
        prl_mall_to_grid = (PullToRefreshGridView) findViewById(R.id.prl_mall_to_grid);
        top_navigation_open_detail = (LinearLayout) findViewById(R.id.top_navigation_open_detail);
        gridView_navigation_open_detail = (GridView) findViewById(R.id.gridView_navigation_open_detail);
        button_reset = (Button) findViewById(R.id.button_reset);
        button_ok = (Button) findViewById(R.id.button_ok);
        layout_open_detail_bottom = (LinearLayout) findViewById(R.id.layout_open_detail_bottom);
        tv_mall_title = findViewById(R.id.tv_mall_title);
        rl_cashback_tip = findViewById(R.id.rl_cashback_tip);
        tv_cashback_tip = findViewById(R.id.tv_cashback_tip);
        iv_cashback_close  = findViewById(R.id.iv_cashback_close);
        rl_second_class = findViewById(R.id.rl_second_class);

        mallSearchTag = (HorizontalListView) findViewById(R.id.mallSearchTag);
        layout_order_mall = (RelativeLayout) findViewById(R.id.layout_order_mall);
        textview_order_mall = (TextView) findViewById(R.id.textview_order_mall);
        layout_buy_car_mall = (RelativeLayout) findViewById(R.id.layout_buy_car_mall);
        textview_buy_car = (TextView) findViewById(R.id.textview_buy_car);
        img_scroll_top = (ImageView) findViewById(R.id.img_scroll_top);
        recyleview_mall_tag = (RecyclerView) findViewById(R.id.recyleview_mall_tag);


        layout_undata_network_isdie = (LinearLayout) findViewById(R.id.layout_undata_network_isdie);
        textview_notice = (TextView) findViewById(R.id.textview_notice);
        textview_update = (TextView) findViewById(R.id.textview_update);
        tabs = (TabLayout) findViewById(R.id.tabs);
        img_undata_networkisdie = (ImageView) findViewById(R.id.img_undata_networkisdie);

        layout_open_detail_bottom.setOnClickListener(this);
        button_reset.setOnClickListener(this);
        button_ok.setOnClickListener(this);
        img_scroll_top.setOnClickListener(this);
        layout_order_mall.setOnClickListener(this);
        layout_buy_car_mall.setOnClickListener(this);
        img_left_title.setOnClickListener(this);
        textview_update.setOnClickListener(this);
        iv_cashback_close.setOnClickListener(this);
    }
    private void setGridViewStyle(GridView gridView) {
        int size = topTags.size();
        int length = 81;
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 5) * density);
        int itemWidth = (int) (length * density);
        itemwidth = itemWidth;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth,LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 重点
        gridView.setColumnWidth(itemWidth); // 重点
        gridView.setHorizontalSpacing(0); // 间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setGravity(Gravity.CENTER);// 位置居中
        gridView.setNumColumns(size); // 重点
    }

    private void otherCloseGridView() {
        top_navigation_open_detail.setVisibility(View.GONE);
        layout_open_detail_bottom.setVisibility(View.GONE);
        for (int i = 0;i<topTags.size();i++){
            NavigationCondition nav = topTags.get(i);
            nav.isOpen = 0;
            topTags.set(i,nav);
        }
//        mallSearchAdapter.notifyDataSetChanged();
        mallSearchResultRecycleViewAdapter.notifyDataSetChanged();
    }
    private void setNavTopIsOpen(int position) {
        for (int i=0;i<topTags.size();i++){
            if (i!=position){
                NavigationCondition EveryNav = topTags.get(i);
                EveryNav.isOpen = 0;
                topTags.set(i,EveryNav);
            }
        }
//        mallSearchAdapter.notifyDataSetChanged();
        mallSearchResultRecycleViewAdapter.notifyDataSetChanged();
    }
    private void resetButton() {
        for(int i=0;i<openDetailList.size();i++){
            NavigationCondition.NavigationOpenDetail everyDetail = openDetailList.get(i);
            everyDetail.isChoose = 0;
        }
        try {
            openDetailAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClickOkState() {
        StringBuilder sp = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        for (int i = 0;i<openDetailList.size();i++){
            NavigationCondition.NavigationOpenDetail nav = openDetailList.get(i);
            if (nav.isChoose==1){
                sp.append(nav.NavigationOpenDetailName+",");
                ids.append(nav.id+",");
            }
            openDetailList.set(i,nav);
        }
        if (TextUtils.isEmpty(sp)){
            navCon.bottonChooseStr ="";
            navCon.publicAttribute="";
        }else {
            navCon.bottonChooseStr = sp.toString();
            navCon.publicAttribute=ids.toString();
        }
        topTags.set(CurrentPosition,navCon);
        mallSearchResultRecycleViewAdapter.notifyDataSetChanged();
        page=1;
        TotalList.clear();
        otherCloseGridView();
        getSearchList();
    }

    private void UnClickOkState() {
        if (navCon!=null){
            if (!TextUtils.isEmpty(navCon.bottonChooseStr)){
                String [] botomSr = navCon.bottonChooseStr.split(",");
                for (int i = 0;i<openDetailList.size();i++){
                    NavigationCondition.NavigationOpenDetail nav = openDetailList.get(i);
                    nav.isChoose = 0;
                }
                for (int i = 0;i<openDetailList.size();i++){
                    NavigationCondition.NavigationOpenDetail nav = openDetailList.get(i);
                    for (int j = 0;j<botomSr.length;j++){
                        if (nav.NavigationOpenDetailName.equals(botomSr[j])){
                            nav.isChoose=1;
                        }
                    }
                }
            }else {
                for (int i = 0;i<openDetailList.size();i++){
                    NavigationCondition.NavigationOpenDetail nav = openDetailList.get(i);
                    nav.isChoose = 0;
                }
            }
            try {
                openDetailAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            otherCloseGridView();
        }
    }
    private void goNext(Class cls,int id){
        Intent intent = new Intent(mContext,cls);
        intent.putExtra("commodityId",id);
        startActivity(intent);
    }
    public Integer setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        // 固定列宽，有多少列
        int col = 2;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
//        (MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
        return totalHeight;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_open_detail_bottom:
                UnClickOkState();
                break;
            case R.id.button_reset:
                resetButton();
                break;
            case R.id.button_ok:
                ClickOkState();
                break;
            case R.id.layout_order_mall:
                if (Utils.checkLogin(mContext)){
                    startActivity(new Intent(mContext, ShopMallOrderActivity.class));
                }else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.layout_buy_car_mall:
                if (Utils.checkLogin(mContext)){
                    startActivity(new Intent(mContext, ShoppingCartActivity.class));
                }else {
                    startActivity(new Intent(mContext, LoginNewActivity.class));
                }
                break;
            case R.id.img_scroll_top:
                if (isGridScroll){
                    GridView mlist2 = prl_mall_to_grid.getRefreshableView();
                    if (!(mlist2).isStackFromBottom()) {
                        mlist2.setStackFromBottom(true);
                    }
                    mlist2.setStackFromBottom(false);
                }

                break;
            case R.id.iv_cashback_close:
                isClicked=true;
                rl_cashback_tip.setVisibility(View.GONE);
                break;
            case R.id.img_left_title:
                finishWithAnimation();
                break;

        }
    }
}
