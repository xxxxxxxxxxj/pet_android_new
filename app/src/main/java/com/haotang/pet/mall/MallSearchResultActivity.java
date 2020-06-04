package com.haotang.pet.mall;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.ShopMallOrderActivity;
import com.haotang.pet.ShoppingCartActivity;
import com.haotang.pet.adapter.MallAdapter.MallOpenDetailAdapter;
import com.haotang.pet.adapter.MallAdapter.MallSearchListAdapter;
import com.haotang.pet.adapter.MallAdapter.MallSearchResultRecycleViewAdapter;
import com.haotang.pet.adapter.MallAdapter.MallToListAdapter;
import com.haotang.pet.entity.mallEntity.MallSearchGoods;
import com.haotang.pet.entity.mallEntity.NavigationCondition;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshHeadGridView;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class MallSearchResultActivity extends SuperActivity implements View.OnClickListener{
    private ImageView img_left_title;
    private ClearEditText editText_search_write;
    private int oldindex = 0;
//    private HorizontalScrollView hsv_appointfrg_date;
//    private GridView gv_appointfrg_top;
    private List<NavigationCondition> topTags = new ArrayList<>();
    private List<NavigationCondition.NavigationOpenDetail> openDetailList = new ArrayList<>();
//    private MallSearchAdapter mallSearchAdapter;
    private PullToRefreshListView prl_Search_list;
    private LinearLayout top_navigation_open_detail,layout_open_detail_bottom;
    private GridView gridView_navigation_open_detail;
    private MallOpenDetailAdapter openDetailAdapter;
    private Button button_reset,button_ok;
    private NavigationCondition navCon = null;
    private NavigationCondition.NavigationOpenDetail openClick = null;
    private int page=1;
    private List<MallSearchGoods> TotalList = new ArrayList<>();
    private MallSearchListAdapter mallSearchListAdapter;
    private int CurrentPosition = -1;
    private int itemwidth;

    private String SearchKey="";

    /*顶部全部*/
    private LinearLayout layout_all,layout_china,layout_other;
    private TextView textview_all,textview_china,textview_other;
    private View View_all,View_china,View_other;
    private int classificationId=-1;
    private String publicAttribute;
    private String content;
    private int saleAmount=-1;
    private int retailPrice=-1;
    private PullToRefreshHeadGridView prl_mall_to_grid;

    private MallToListAdapter mallToListAdapter;
    private List<MallSearchGoods> listServicePush = new ArrayList<MallSearchGoods>();
    private View header;
    private int pageServicePush=1;
    private RelativeLayout layout_order_mall;
    private TextView textview_order_mall;
    private RelativeLayout layout_buy_car_mall;
    private TextView textview_buy_car;
    private int userMallCartNum;
    private int orderCount;
    private ImageView img_scroll_top;
    private boolean isListScroll = false;
    private boolean isGridScroll = false;
    private ImageView mall_search_price_up_down;
    private RecyclerView recyleview_mall_tag;
    private MallSearchResultRecycleViewAdapter mallSearchResultRecycleViewAdapter;

    public LinearLayout layout_undata_network_isdie;
    public TextView textview_notice;
    public TextView textview_update;
    private boolean isNet;
    private RelativeLayout layout_list_out;
    private ImageView mall_search_saleamount_up_down;
//    private TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mallsearchresult);
        MApplication.listAppoint.add(mContext);
        TotalList.clear();
        initView();
        setView();
        initListener();
        getTagData();
        getSearchList();
    }
    @Override
    protected void onResume() {
        super.onResume();
        queryOrderAndCart();
    }
    private void getSearchList() {
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
        content = SearchKey;
        CommUtil.mallSearchCommodityList(mContext,classificationId,publicAttribute,content,saleAmount,retailPrice,page,searchHandler);
    }
    private AsyncHttpResponseHandler searchHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                prl_Search_list.onRefreshComplete();
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
                if (TotalList.size()>0){
                    prl_Search_list.setVisibility(View.VISIBLE);
                    layout_undata_network_isdie.setVisibility(View.GONE);
                    mallSearchListAdapter.notifyDataSetChanged();
                }else {
                    mallSearchListAdapter.notifyDataSetChanged();
                    prl_Search_list.setVisibility(View.GONE);
                    layout_undata_network_isdie.setVisibility(View.VISIBLE);
                    prl_mall_to_grid.setVisibility(View.VISIBLE);
//                  显示推荐
                    getRecommendData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
                getRecommendData();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            prl_Search_list.onRefreshComplete();
            mPDialog.closeDialog();
            getRecommendData();
//            layout_undata_network_isdie.setVisibility(View.VISIBLE);
//            layout_list_out.setVisibility(View.GONE);
//            prl_Search_list.setVisibility(View.GONE);
//            prl_mall_to_grid.setVisibility(View.GONE);
//            top_navigation_open_detail.setVisibility(View.GONE);
            layout_list_out.setVisibility(View.GONE);
            layout_undata_network_isdie.setVisibility(View.VISIBLE);
        }
    };
    private void getTagData(){
        topTags.clear();
        mPDialog.showDialog();
        CommUtil.mallQueryPublicAttributeList(mContext,classificationId,SearchKey,handler);
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
                                    recyleview_mall_tag.setVisibility(View.VISIBLE);
                                    mallSearchResultRecycleViewAdapter.notifyDataSetChanged();
                                }else {
                                    recyleview_mall_tag.setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }
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
    private void getRecommendData(){
        mPDialog.showDialog();
        CommUtil.getrRecommendData(mContext,0,pageServicePush,-1,getrRecommentDataHandler);
    }
    private AsyncHttpResponseHandler getrRecommentDataHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            prl_mall_to_grid.onRefreshComplete();
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("commodityList") && !jdata.isNull("commodityList")) {
                            JSONArray jarrcommodityList = jdata.getJSONArray("commodityList");
                            if (jarrcommodityList.length() > 0) {
                                pageServicePush++;
                                for (int i = 0; i < jarrcommodityList.length(); i++) {
                                    listServicePush.add(MallSearchGoods.json2Entity(jarrcommodityList.getJSONObject(i)));
                                }
                            }
                        }
                    }
                }
                if (listServicePush != null && listServicePush.size() > 0) {
                    prl_mall_to_grid.setVisibility(View.VISIBLE);
                    layout_undata_network_isdie.setVisibility(View.GONE);
                    mallToListAdapter.notifyDataSetChanged();
                }else {
                    mallToListAdapter.notifyDataSetChanged();
                    prl_mall_to_grid.setVisibility(View.GONE);
                    layout_undata_network_isdie.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            layout_list_out.setVisibility(View.GONE);
            layout_undata_network_isdie.setVisibility(View.VISIBLE);
        }
    };
    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {

        editText_search_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("SearchKey",editText_search_write.getText().toString());
                setResult(Global.RESULT_OK,intent);
                finishWithAnimation();
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


        prl_Search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MallSearchGoods mallSearchGoods = (MallSearchGoods) parent.getItemAtPosition(position);
//                if (Utils.checkLogin(mContext)){
                    goNext(CommodityDetailActivity.class,mallSearchGoods.id);

//                }else {
//                    startActivity(new Intent(mContext, LoginActivity.class));
//                }
            }
        });
        prl_mall_to_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listServicePush!=null&&listServicePush.size()>0){
                    Intent intent = new Intent(mContext, CommodityDetailActivity.class);
                    intent.putExtra("commodityId", listServicePush.get(i).id);
                    intent.putExtra("source", Global.SOURCE_MALLSEARCH);
                    mContext.startActivity(intent);
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
        prl_Search_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem>=0&&firstVisibleItem<=10){
                    isListScroll = false;
                    img_scroll_top.setVisibility(View.GONE);
                }else {
                    isListScroll = true;
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
    }


    private void setView() {
        if (retailPrice==-1){
            mall_search_price_up_down.setBackgroundResource(R.drawable.price_middle);
        }
        isNet = Utils.checkNet(mContext);
        if (isNet){
            textview_notice.setText("没有更多商品");
        }else {
            textview_notice.setText("啊哦~网络竟然崩溃了 请检查您的网络");
            recyleview_mall_tag.setVisibility(View.GONE);
        }
//        prl_Search_list.setEmptyView(layout_undata_network_isdie);

        mallSearchListAdapter = new MallSearchListAdapter<MallSearchGoods>(mContext,TotalList);
        prl_Search_list.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        prl_Search_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
                    // 下拉刷新
                    page = 1;
                    TotalList.clear();
                    getSearchList();
                } else {
                    getSearchList();
                }
            }
        });

        prl_Search_list.setAdapter(mallSearchListAdapter);


        prl_mall_to_grid.getRefreshableView().addHeaderView(header);
//        prl_mall_to_grid.setEmptyView(layout_undata_network_isdie);
        mallToListAdapter = new MallToListAdapter(mContext,listServicePush);
        prl_mall_to_grid.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        prl_mall_to_grid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
                    // 下拉刷新
                    listServicePush.clear();
                    pageServicePush=1;
                    getRecommendData();
                } else {
                    getRecommendData();
                }
            }
        });
        prl_mall_to_grid.setAdapter(mallToListAdapter);
        prl_mall_to_grid.setVisibility(View.GONE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyleview_mall_tag.setLayoutManager(linearLayoutManager);
        mallSearchResultRecycleViewAdapter = new MallSearchResultRecycleViewAdapter(mContext,topTags);
        recyleview_mall_tag.setAdapter(mallSearchResultRecycleViewAdapter);
    }


    private void setGridViewStyle(GridView gridView) {
        int size = topTags.size();
        int length = 81;
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
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

    private void initView() {
        SearchKey = getIntent().getStringExtra("SearchKey");

        layout_all = (LinearLayout) findViewById(R.id.layout_all);
        layout_china = (LinearLayout) findViewById(R.id.layout_china);
        layout_other = (LinearLayout) findViewById(R.id.layout_other);
        textview_all = (TextView) findViewById(R.id.textview_all);
        textview_china = (TextView) findViewById(R.id.textview_china);
        textview_other = (TextView) findViewById(R.id.textview_other);
        View_all = (View) findViewById(R.id.View_all);
        View_china = (View) findViewById(R.id.View_china);
        View_other = (View) findViewById(R.id.View_other);


        img_left_title = (ImageView) findViewById(R.id.img_left_title);
        editText_search_write = (ClearEditText) findViewById(R.id.editText_search_write);

        prl_Search_list = (PullToRefreshListView) findViewById(R.id.prl_Search_list);
        top_navigation_open_detail = (LinearLayout) findViewById(R.id.top_navigation_open_detail);
        layout_open_detail_bottom = (LinearLayout) findViewById(R.id.layout_open_detail_bottom);
        gridView_navigation_open_detail = (GridView) findViewById(R.id.gridView_navigation_open_detail);
        button_reset = (Button) findViewById(R.id.button_reset);
        button_ok = (Button) findViewById(R.id.button_ok);
//        hsv_appointfrg_date = (HorizontalScrollView) findViewById(R.id.hsv_appointfrg_date);
//        gv_appointfrg_top = (GridView) findViewById(R.id.gv_appointfrg_top);
        header = LayoutInflater.from(this).inflate(R.layout.item_search_undata_header, null);
        header.setFocusable(false);
        prl_mall_to_grid = (PullToRefreshHeadGridView) findViewById(R.id.prl_mall_to_grid);
        layout_order_mall = (RelativeLayout) findViewById(R.id.layout_order_mall);
        textview_order_mall = (TextView) findViewById(R.id.textview_order_mall);
        layout_buy_car_mall = (RelativeLayout) findViewById(R.id.layout_buy_car_mall);
        textview_buy_car = (TextView) findViewById(R.id.textview_buy_car);
        img_scroll_top = (ImageView) findViewById(R.id.img_scroll_top);
        mall_search_price_up_down = (ImageView) findViewById(R.id.mall_search_price_up_down);
        recyleview_mall_tag = (RecyclerView) findViewById(R.id.recyleview_mall_tag);

        layout_undata_network_isdie = (LinearLayout) findViewById(R.id.layout_undata_network_isdie);
        textview_notice = (TextView) findViewById(R.id.textview_notice);
        textview_update = (TextView) findViewById(R.id.textview_update);
        layout_list_out = (RelativeLayout) findViewById(R.id.layout_list_out);
        mall_search_saleamount_up_down = (ImageView) findViewById(R.id.mall_search_saleamount_up_down);
       /* tabs = (TabLayout) findViewById(R.id.tabs);

        for (int i = 0 ;i<3;i++){
           TabLayout.Tab tab= tabs.newTab();
            View view =  LayoutInflater.from(mContext).inflate(R.layout.item_tablayout_hasimg,null);
            TextView textview_item_name = (TextView) view.findViewById(R.id.textview_item_name);
            ImageView mall_search_price_up_down = (ImageView) view.findViewById(R.id.mall_search_price_up_down);
            tab.setCustomView(view);
            if (i==0){
                textview_item_name.setText("全部");
                mall_search_price_up_down.setVisibility(View.GONE);
            }else if (i==1){
                textview_item_name.setText("价格");
                mall_search_price_up_down.setVisibility(View.VISIBLE);
            }else if (i==2){
                textview_item_name.setText("销量");
                mall_search_price_up_down.setVisibility(View.VISIBLE);
            }
            tabs.addTab(tab);
        }*/
        layout_open_detail_bottom.setOnClickListener(this);
        button_reset.setOnClickListener(this);
        button_ok.setOnClickListener(this);

        layout_all.setOnClickListener(this);
        layout_china.setOnClickListener(this);
        layout_other.setOnClickListener(this);
        layout_order_mall.setOnClickListener(this);
        layout_buy_car_mall.setOnClickListener(this);
        img_scroll_top.setOnClickListener(this);
        img_left_title.setOnClickListener(this);
        mall_search_saleamount_up_down.setOnClickListener(this);


        if (!TextUtils.isEmpty(SearchKey)){
            editText_search_write.setText(SearchKey+"");
            editText_search_write.setSelection(editText_search_write.getText().length());
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
                                    textview_buy_car.setVisibility(View.VISIBLE);
                                    if (userMallCartNum<=99){
                                        textview_buy_car.setText(""+userMallCartNum+"");
                                    }else {
                                        textview_buy_car.setText(""+userMallCartNum+"+");
                                    }
                                }else {
                                    textview_buy_car.setVisibility(View.GONE);
                                }
                            }
                            if (jdata.has("orderCount") && !jdata.isNull("orderCount")) {
                                orderCount = jdata.getInt("orderCount");
                                if (orderCount>0){
                                    textview_order_mall.setVisibility(View.VISIBLE);
                                    if (orderCount<=99){
                                        textview_order_mall.setText(""+orderCount+"");
                                    }else {
                                        textview_order_mall.setText(""+orderCount+"+");
                                    }
                                }else {
                                    textview_order_mall.setVisibility(View.GONE);
                                }
                            }
                        }
                    }else {
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



//    private void ScollTo(final int dayposition2) {
//        // 解决自动滚动问题
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hsv_appointfrg_date.smoothScrollTo((dayposition2 - 2)
//                        * itemwidth, 0);
//            }
//        }, 5);
//    }
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
//        mallSearchAdapter.notifyDataSetChanged();
        mallSearchResultRecycleViewAdapter.notifyDataSetChanged();
        otherCloseGridView();
        page=1;
        TotalList.clear();
        mallSearchListAdapter.notifyDataSetChanged();
        prl_Search_list.setVisibility(View.GONE);
        pageServicePush=1;
        listServicePush.clear();
        mallToListAdapter.notifyDataSetChanged();
        prl_mall_to_grid.setVisibility(View.GONE);
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
        intent.putExtra("source",Global.SOURCE_MALLSEARCH);
        startActivity(intent);
    }
    private void topThr(int index) {
        TotalList.clear();
        if (index == 0) {
            textview_all.setTextColor(Color.parseColor("#FF3A1E"));
            View_all.setVisibility(View.VISIBLE);
            View_all.setBackgroundColor(Color.parseColor("#FF3A1E"));

            textview_china.setTextColor(Color.parseColor("#333333"));
            View_china.setVisibility(View.INVISIBLE);

            textview_other.setTextColor(Color.parseColor("#333333"));
            View_other.setVisibility(View.INVISIBLE);
        } else if (index == 1) {
            textview_all.setTextColor(Color.parseColor("#333333"));
            View_all.setVisibility(View.INVISIBLE);

            textview_china.setTextColor(Color.parseColor("#FF3A1E"));
            View_china.setVisibility(View.VISIBLE);
            View_china.setBackgroundColor(Color.parseColor("#FF3A1E"));

            textview_other.setTextColor(Color.parseColor("#333333"));
            View_other.setVisibility(View.INVISIBLE);
        } else if (index == 2) {
            textview_all.setTextColor(Color.parseColor("#333333"));
            View_all.setVisibility(View.INVISIBLE);

            textview_china.setTextColor(Color.parseColor("#333333"));
            View_china.setVisibility(View.INVISIBLE);

            textview_other.setTextColor(Color.parseColor("#FF3A1E"));
            View_other.setVisibility(View.VISIBLE);
            View_other.setBackgroundColor(Color.parseColor("#FF3A1E"));
        }
        getSearchList();
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
            case R.id.layout_all://全部
//                classificationId = 1;
                saleAmount = -1;
                retailPrice = -1;
                mall_search_price_up_down.setBackgroundResource(R.drawable.price_middle);
                mall_search_saleamount_up_down.setBackgroundResource(R.drawable.price_middle);
                page=1;
                TotalList.clear();
                mallSearchListAdapter.notifyDataSetChanged();
                prl_Search_list.setVisibility(View.GONE);
                pageServicePush=1;
                listServicePush.clear();
                mallToListAdapter.notifyDataSetChanged();
                prl_mall_to_grid.setVisibility(View.GONE);
                topThr(0);
//                getTagData();
                break;
            case R.id.layout_china://价格
                saleAmount =-1;
                if (retailPrice==-1){
                    retailPrice=1;
                    mall_search_price_up_down.setBackgroundResource(R.drawable.price_up);
                }else if (retailPrice==1){
                    retailPrice=0;
                    mall_search_price_up_down.setBackgroundResource(R.drawable.price_down);
                }else if (retailPrice==0){
                    retailPrice=1;
                    mall_search_price_up_down.setBackgroundResource(R.drawable.price_up);
                }
                page=1;
                TotalList.clear();
                mallSearchListAdapter.notifyDataSetChanged();
                prl_Search_list.setVisibility(View.GONE);
                pageServicePush=1;
                listServicePush.clear();
                mallToListAdapter.notifyDataSetChanged();
                prl_mall_to_grid.setVisibility(View.GONE);
                topThr(1);
//                getTagData();
                break;
            case R.id.layout_other://销量
                retailPrice = -1;
                if (saleAmount==-1){
                    saleAmount = 1;
                    mall_search_saleamount_up_down.setBackgroundResource(R.drawable.price_up);
                }else if (saleAmount==1){
                    saleAmount =0;
                    mall_search_saleamount_up_down.setBackgroundResource(R.drawable.price_down);
                }else if(saleAmount==0){
                    saleAmount=1;
                    mall_search_saleamount_up_down.setBackgroundResource(R.drawable.price_up);
                }
                page=1;
                TotalList.clear();
                mallSearchListAdapter.notifyDataSetChanged();
                prl_Search_list.setVisibility(View.GONE);
                pageServicePush=1;
                listServicePush.clear();
                mallToListAdapter.notifyDataSetChanged();
                prl_mall_to_grid.setVisibility(View.GONE);
                topThr(2);
//                getTagData();
                break;
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
                if (isListScroll){
                    ListView mlist = prl_Search_list.getRefreshableView();
                    if (!(mlist).isStackFromBottom()) {
                        mlist.setStackFromBottom(true);
                    }
                    mlist.setStackFromBottom(false);
                }
                if (isGridScroll){
                    GridView mlist2 = prl_mall_to_grid.getRefreshableView();
                    if (!(mlist2).isStackFromBottom()) {
                        mlist2.setStackFromBottom(true);
                    }
                    mlist2.setStackFromBottom(false);
                }
                break;
            case R.id.img_left_title:
                finishWithAnimation();
                break;
        }
    }


}
