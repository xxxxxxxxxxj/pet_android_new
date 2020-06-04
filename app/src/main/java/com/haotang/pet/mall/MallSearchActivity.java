package com.haotang.pet.mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.adapter.MallAdapter.MallInputSearchAdapter;
import com.haotang.pet.adapter.MallAdapter.StockAdapter;
import com.haotang.pet.entity.mallEntity.MallHistory;
import com.haotang.pet.entity.mallEntity.MallHot;
import com.haotang.pet.entity.mallEntity.MallIcon;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;
import com.haotang.pet.view.FluidLayout;
import com.haotang.pet.view.MyGridView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class MallSearchActivity extends SuperActivity {
    private FluidLayout fluid_layout_one,fluid_layout_two;
    private MyGridView search_gridview;
    private List<MallHot> hotList = new ArrayList<>();
    private List<MallHistory> historyList = new ArrayList<>();
    private List<MallIcon> iconList = new ArrayList<>();
    private boolean hasBg = true;
    private int gravity = Gravity.TOP;
    private List<TextView> list = new ArrayList<>();
    private List<String > tags = new ArrayList<>();
    private StockAdapter stockAdapter;
    private ClearEditText input_word_search;
    private ScrollView top_fluidlayout;
    private PullToRefreshListView mall_inout_search_list;
    private MallInputSearchAdapter mallInputSearchAdapter;
    private String SearchKey="";
    private TextView textview_peopleused;
    private TextView textview_history;
    private TextView textview_hot;
    private ImageView img_delete;
    private LinearLayout layout_history;
    private LinearLayout layout_hot;
    private LinearLayout layout_stock;
    private TextView textview_cancle;
    private List<String> searchTitle = new ArrayList<>();
    public LinearLayout layout_undata_network_isdie;
    public TextView textview_notice;
    public TextView textview_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mallsearch);
        MApplication.listAppoint.add(mContext);
        searchTitle.clear();
        initView();
        setView();
        initLisener();
        getData();
    }
    private void getData(){
        CommUtil.mallSearchPage(mContext,handler);
    }
    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    layout_undata_network_isdie.setVisibility(View.GONE);
                    top_fluidlayout.setVisibility(View.VISIBLE);
                    mall_inout_search_list.setVisibility(View.GONE);

                    if (object.has("data")&&!object.isNull("data")){
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("searchHistory")&&!objectData.isNull("searchHistory")){
                            JSONArray array = objectData.getJSONArray("searchHistory");
                            if (array.length()>0){
                                for (int i = 0;i<array.length();i++){
                                    historyList.add(MallHistory.json2Entity(array.getJSONObject(i)));
                                }
                            }
                            if (historyList.size()>0){
                                layout_history.setVisibility(View.VISIBLE);
                                textview_history.setVisibility(View.VISIBLE);
                                genTagHistory(fluid_layout_one,true,historyList);
                            }else {
                                textview_history.setVisibility(View.GONE);
                                layout_history.setVisibility(View.GONE);
                            }
                        }else {
                            layout_history.setVisibility(View.GONE);
                        }
                        if (objectData.has("hotSearch")&&!objectData.isNull("hotSearch")){
                            JSONArray array = objectData.getJSONArray("hotSearch");
                            if (array.length()>0){
                                for (int i = 0;i<array.length();i++){
                                    hotList.add(MallHot.json2Entity(array.getJSONObject(i)));
                                }
                            }
                            if (hotList.size()>0){
                                layout_hot.setVisibility(View.VISIBLE);
                                textview_hot.setVisibility(View.VISIBLE);
                                genTaghot(fluid_layout_two,true,hotList);
                            }else {
                                textview_hot.setVisibility(View.GONE);
                                layout_hot.setVisibility(View.GONE);
                            }
                        }else {
                            layout_hot.setVisibility(View.GONE);
                        }
                        if (objectData.has("icons")&&!objectData.isNull("icons")){
                            JSONArray array = objectData.getJSONArray("icons");
                            if (array.length()>0){
                                for (int i = 0;i<array.length();i++){
                                    iconList.add(MallIcon.json2Entity(array.getJSONObject(i)));
                                }
                            }
                            if (iconList.size()>0){
                                layout_stock.setVisibility(View.VISIBLE);
                                textview_peopleused.setVisibility(View.VISIBLE);
                                stockAdapter.notifyDataSetChanged();
                            }else {
                                textview_peopleused.setVisibility(View.GONE);
                                layout_stock.setVisibility(View.GONE);
                            }
                        }else {
                            layout_stock.setVisibility(View.GONE);
                        }
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
    private void mallSearchAssociation(String content){
        searchTitle.clear();
//        mPDialog.showDialog();
        CommUtil.mallSearchAssociation(mContext,content,searchHandler);
    }
    private AsyncHttpResponseHandler searchHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
//                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    if (object.has("data")&&!object.isNull("data")){
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("searchAssociationList")&&!objectData.isNull("searchAssociationList")){
                           JSONArray array = objectData.getJSONArray("searchAssociationList");
                            if (array.length()>0){
                                for (int i =0;i<array.length();i++){
                                    searchTitle.add(array.getString(i));
                                }
                            }
                        }
                    }
                    if (searchTitle.size()>0){
                        mallInputSearchAdapter.notifyDataSetChanged();
                    }else {
                        top_fluidlayout.setVisibility(View.VISIBLE);
                        mall_inout_search_list.setVisibility(View.GONE);
                    }
                }else {
                    top_fluidlayout.setVisibility(View.VISIBLE);
                    mall_inout_search_list.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            mPDialog.closeDialog();
            top_fluidlayout.setVisibility(View.VISIBLE);
            mall_inout_search_list.setVisibility(View.GONE);
        }
    };
    private void initLisener() {
        input_word_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (input_word_search.getText().toString().equals("")|| input_word_search.getText().toString().equals(null)) {
                    top_fluidlayout.setVisibility(View.VISIBLE);
                    mall_inout_search_list.setVisibility(View.GONE);
                }else{
                    top_fluidlayout.setVisibility(View.GONE);
                    mall_inout_search_list.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_word_search.getText().toString().equals("")|| input_word_search.getText().toString().equals(null)) {
                    top_fluidlayout.setVisibility(View.VISIBLE);
                    mall_inout_search_list.setVisibility(View.GONE);
                }else{
                    top_fluidlayout.setVisibility(View.GONE);
                    mall_inout_search_list.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(input_word_search.getText())){
                    mallSearchAssociation(input_word_search.getText().toString());
                }
//                mallInputSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (input_word_search.getText().toString().equals("")|| input_word_search.getText().toString().equals(null)) {
                    top_fluidlayout.setVisibility(View.VISIBLE);
                    mall_inout_search_list.setVisibility(View.GONE);
                }else{
                    top_fluidlayout.setVisibility(View.GONE);
                    mall_inout_search_list.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(input_word_search.getText())){
                        mallAddSearchHistory(input_word_search.getText().toString(),-1);
                    }
                }
                mallInputSearchAdapter.notifyDataSetChanged();
            }
        });

        input_word_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input_word_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH||actionId==EditorInfo.IME_ACTION_UNSPECIFIED){
                    /*Intent intent = new Intent(mContext,MallSearchResultActivity.class);
                    intent.putExtra("SearchKey",input_word_search.getText().toString());
                    startActivityForResult(intent, Global.MALL_SEARCH_TORESULT);*/
                    goNext(MallSearchResultActivity.class,input_word_search.getText().toString());
                    return true;
                }
                return false;
            }
        });
        mall_inout_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.showToastShortCenter(mContext,""+parent.getItemAtPosition(position));
                String str = (String) parent.getItemAtPosition(position);
                goNext(MallSearchResultActivity.class,str);
            }
        });
        search_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.showToastShortCenter(mContext,""+parent.getItemAtPosition(position));
                MallIcon mallIcon = (MallIcon) parent.getItemAtPosition(position);
//                goNext(MallSearchResultActivity.class,mallIcon.name);
                Utils.goService(mContext,mallIcon.point,mallIcon.backup);
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mallDeleteSearchHistory();
            }
        });
        textview_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithAnimation();
            }
        });

        textview_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void setView() {
       boolean isNet = Utils.checkNet(mContext);
        if (!isNet){
            layout_undata_network_isdie.setVisibility(View.VISIBLE);
            textview_update.setVisibility(View.VISIBLE);
            top_fluidlayout.setVisibility(View.GONE);
            mall_inout_search_list.setVisibility(View.GONE);
        }else {
            layout_undata_network_isdie.setVisibility(View.GONE);
            top_fluidlayout.setVisibility(View.VISIBLE);
            mall_inout_search_list.setVisibility(View.GONE);
        }
        stockAdapter = new StockAdapter<MallIcon>(mContext,iconList);
        search_gridview.setAdapter(stockAdapter);

        mallInputSearchAdapter = new MallInputSearchAdapter<String>(mContext,searchTitle);
        mall_inout_search_list.setMode(PullToRefreshBase.Mode.DISABLED);
        mall_inout_search_list.setAdapter(mallInputSearchAdapter);

    }

    private void initView() {

        input_word_search = (ClearEditText) findViewById(R.id.input_word_search);
        top_fluidlayout = (ScrollView) findViewById(R.id.top_fluidlayout);
        fluid_layout_one = (FluidLayout) findViewById(R.id.fluid_layout_one);
        fluid_layout_two = (FluidLayout) findViewById(R.id.fluid_layout_two);
        search_gridview = (MyGridView) findViewById(R.id.search_gridview);
        mall_inout_search_list = (PullToRefreshListView) findViewById(R.id.mall_inout_search_list);
        textview_peopleused = (TextView) findViewById(R.id.textview_peopleused);
        textview_history = (TextView) findViewById(R.id.textview_history);
        textview_hot = (TextView) findViewById(R.id.textview_hot);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        layout_history = (LinearLayout) findViewById(R.id.layout_history);
        layout_hot = (LinearLayout) findViewById(R.id.layout_hot);
        layout_stock = (LinearLayout) findViewById(R.id.layout_stock);
        textview_cancle = (TextView) findViewById(R.id.textview_cancle);

        layout_undata_network_isdie = (LinearLayout) findViewById(R.id.layout_undata_network_isdie);
        textview_notice = (TextView) findViewById(R.id.textview_notice);
        textview_update = (TextView) findViewById(R.id.textview_update);

        SearchKey = getIntent().getStringExtra("SearchKey");
        if (!TextUtils.isEmpty(SearchKey)){
            input_word_search.setText(SearchKey);
        }
        top_fluidlayout.setVisibility(View.VISIBLE);
        mall_inout_search_list.setVisibility(View.GONE);

    }

    private void genTagHistory(FluidLayout fluid_layout, boolean hasBg, final List<MallHistory> tags) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(gravity);
        for (int i = 0; i < tags.size(); i++) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.text_bg);
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setText(tags.get(i).content);
            tv.setTextSize(13);
            tv.setPadding(30,10,30,10);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 20, 20, 20);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
//                    Toast.makeText(mContext,view.getText().toString(),Toast.LENGTH_SHORT).show();
                    /*int historyId=0;
                    for (int i = 0;i<tags.size();i++){
                        MallHistory mallHistory = tags.get(i);
                        if (mallHistory.content.equals(view.getText().toString())){
                            historyId = mallHistory.id;
                        }
                    }*/
/*
                    boolean isChoose = false;
                    int pos = -1;
                    if (list.size()>0){
                        for (int i = 0;i<list.size();i++){
                            if (view==list.get(i)){
                                isChoose = true;
                                pos = i;
                                break;
                            }else {
                                isChoose = false;
                            }
                        }
                    }
                    if (isChoose){
                        isChoose = false;
                        if (pos!=-1)
                            list.remove(view);
                        view.setBackgroundResource(R.drawable.text_bg);
                        view.setTextColor(Color.parseColor("#333333"));
                    }else{
                        isChoose = true;
                        list.add(view);
                        view.setBackgroundResource(R.drawable.text_bg);
                        view.setTextColor(Color.parseColor("#FE4D3D"));
//                        view.setBackgroundResource(R.drawable.bg_button_service_apponit_ok);
                    }
*/

//                    Intent intent = new Intent(mContext,MallSearchResultActivity.class);
//                    intent.putExtra("SearchKey",view.getText().toString());
//                    startActivity(intent);
                    goNext(MallSearchResultActivity.class,view.getText().toString());
                }
            });
            fluid_layout.addView(tv, params);
        }
    }
    private void genTaghot(FluidLayout fluid_layout, boolean hasBg, final List<MallHot> tags) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(gravity);
        for (int i = 0; i < tags.size(); i++) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.text_bg);
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setText(tags.get(i).content);
            tv.setTextSize(13);
            tv.setPadding(30,10,30,10);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 20, 20, 20);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    int hotId=0;
                    String content = null;
                    for (int i = 0;i<tags.size();i++){
                        MallHot mallHot = tags.get(i);
                        if (mallHot.content.equals(view.getText().toString())){
                            hotId = mallHot.id;
                            content = mallHot.content;
                        }
                    }
                    mallUpdateHotSearch(hotId);
                    mallAddSearchHistory(content,1);
                    /*boolean isChoose = false;
                    int pos = -1;
                    if (list.size()>0){
                        for (int i = 0;i<list.size();i++){
                            if (view==list.get(i)){
                                isChoose = true;
                                pos = i;
                                break;
                            }else {
                                isChoose = false;
                            }
                        }
                    }
                    if (isChoose){
                        isChoose = false;
                        if (pos!=-1)
                            list.remove(view);
                        view.setBackgroundResource(R.drawable.text_bg);
                        view.setTextColor(Color.parseColor("#333333"));
                    }else{
                        isChoose = true;
                        list.add(view);
                        view.setBackgroundResource(R.drawable.text_bg);
                        view.setTextColor(Color.parseColor("#FE4D3D"));
//                        view.setBackgroundResource(R.drawable.bg_button_service_apponit_ok);
                    }
*/
//                    Intent intent = new Intent(mContext,MallSearchResultActivity.class);
//                    intent.putExtra("SearchKey",view.getText().toString());
//                    startActivity(intent);
                    goNext(MallSearchResultActivity.class,view.getText().toString());
                }
            });
            fluid_layout.addView(tv, params);
        }
    }
//    http://192.168.0.252/static/content/design/pet/mall/updateHotSearch.html
    private void mallUpdateHotSearch(int hotSearchId){
        CommUtil.mallUpdateHotSearch(mContext,hotSearchId,hotHandler);
    }
    private AsyncHttpResponseHandler hotHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = 0;
                if (code == 0 ){

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private void mallAddSearchHistory(String content,int source){
        CommUtil.mallAddSearchHistory(mContext,content,source,historyHandler);
    }
    private AsyncHttpResponseHandler historyHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private void mallDeleteSearchHistory(){
        CommUtil.mallDeleteSearchHistory(mContext,deleteHandler);
    }
    private AsyncHttpResponseHandler deleteHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0 ){
                    layout_history.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private void goNext(Class cls,String searchKey){
        Intent intent = new Intent(mContext,cls);
        intent.putExtra("SearchKey",searchKey);
        startActivityForResult(intent, Global.MALL_SEARCH_TORESULT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Global.RESULT_OK){
            if (requestCode==Global.MALL_SEARCH_TORESULT){
                if (data!=null){
                    SearchKey =  data.getStringExtra("SearchKey");
                    if (!TextUtils.isEmpty(SearchKey)){
                        if (!TextUtils.isEmpty(SearchKey)){
                            input_word_search.setText(SearchKey);
                            input_word_search.setSelection(input_word_search.getText().length());
                            mallAddSearchHistory(input_word_search.getText().toString().toString(),-1);
                        }
                    }
                }
            }
        }
    }
}
