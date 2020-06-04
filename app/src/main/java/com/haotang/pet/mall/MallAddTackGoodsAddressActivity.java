package com.haotang.pet.mall;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.haotang.base.SuperActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.adapter.MallAdapter.MallSearchDetailAdapter;
import com.haotang.pet.entity.mallEntity.MallAddress;
import com.haotang.pet.entity.mallEntity.MallSearchDetailBean;
import com.haotang.pet.entity.mallEntity.RegionChildren;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 添加收货地址界面
 * Created by Administrator on 2017/9/1.
 */

public class MallAddTackGoodsAddressActivity extends SuperActivity implements View.OnClickListener,OnGetPoiSearchResultListener,View.OnLayoutChangeListener{
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    private TextView tv_titlebar_other;
    private EditText textview_tackgoodsname;
    private EditText textview_tackgoodstel;
    private TextView textview_tackgoodsaddress;
    private EditText textview_tackgoodsaddress_detail;
    private ImageView img_is_detault;
    private ArrayList<RegionChildren> tagTops = new ArrayList<>();
    private String conginer=" ";
    private String mobile=" ";
    private int  areaId;
    private String  areaName = "";
    private String address = "";
    private String addressDetail="";
    private int isDefault = 0;
    private LinearLayout layout_tackgoodsaddress;
    private ListView listview_address_search;
    private PoiSearch mPoiSearch = null;
    private List<MallSearchDetailBean> searchLists = new ArrayList<>();
    private MallSearchDetailAdapter mallSearchDetailAdapter;
    private int addressId;
    private int previous;
    private boolean isFirst = true;
    private double lat = 0;
    private double lng = 0;
    private LinearLayout layout_show_address_detail;
    private List<String> list = new ArrayList<>();
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private LinearLayout layout_main;
    private boolean isUp = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_mall_add_tackgoodsaddress);
        MApplication.listAppoint.add(mContext);
        initAcceptData();
        initView();
        initMap();
        setView();
        initListener();
    }

    private void initAcceptData() {
        addressId = getIntent().getIntExtra("addressId",-1);
        previous = getIntent().getIntExtra("previous",-1);
    }

    private void setView() {
        tv_titlebar_other.setVisibility(View.VISIBLE);
        tv_titlebar_other.setText("保存");
        tv_titlebar_title.setText("添加地址");

        if (addressId>0){
            tv_titlebar_other.setText("更新");
            tv_titlebar_title.setText("编辑地址");
            addressInfo(addressId);
        }
    }
    private void addressInfo(int addressId){
        mPDialog.showDialog();
        CommUtil.addressInfo(mContext,addressId,getAddressInfoHandler);
    }
    private AsyncHttpResponseHandler getAddressInfoHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    if (object.has("data")&&!object.isNull("data")){
                        JSONObject objectData =  object.getJSONObject("data");
                        MallAddress mallAddress = MallAddress.json2Entity(objectData);
                        textview_tackgoodsname.setText(mallAddress.consigner);
                        textview_tackgoodstel.setText(mallAddress.mobile);
                        textview_tackgoodsaddress.setText(mallAddress.areaName);
                        textview_tackgoodsaddress_detail.setText(mallAddress.address);
                        list.add(mallAddress.address);
                        isDefault = mallAddress.isDefault;
                        if (mallAddress.isDefault==0){
                            img_is_detault.setBackgroundResource(R.drawable.noty_no);
                        }else if (mallAddress.isDefault==1){
                            img_is_detault.setBackgroundResource(R.drawable.noty_yes);
                        }

                        if (objectData.has("parents")&&!objectData.isNull("parents")){
                            JSONArray array = objectData.getJSONArray("parents");
                            for (int i=0;i<array.length();i++){
                                JSONArray objectIsSelect = array.getJSONArray(i);
                                if (objectIsSelect.length()>0){
                                    for (int j = 0;j<objectIsSelect.length();j++){
                                        JSONObject objectInside = objectIsSelect.getJSONObject(j);
                                        tagTops.add(RegionChildren.json2Entity(objectInside));
                                    }

                                }
                            }
                            Utils.mLogError("==-->aaaaaaa aaaaaa "+tagTops.size());
                            List<RegionChildren> isSelect = new ArrayList<>();
                            for (int i =0;i<tagTops.size();i++){
                                RegionChildren reg = tagTops.get(i);
                                Utils.mLogError("==-->"+reg.selected+" "+reg.region);
                                if (reg.selected==0){
                                }else if(reg.selected==1){
                                    isSelect.add(reg);
                                    Utils.mLogError("==-->aaaaaaa bbbbbb "+isSelect.size());
                                }
                            }
                            Utils.mLogError("==-->aaaaaaa cccccc "+isSelect.size());
                            tagTops.clear();
                            tagTops.addAll(isSelect);
                            Utils.mLogError("==-->aaaaaaa dddddd "+tagTops.size());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };
    private void initListener() {
        textview_tackgoodsaddress_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!textview_tackgoodsaddress.getText().toString().equals("")|| !textview_tackgoodsaddress.getText().toString().equals(null)) {
                    listview_address_search.setVisibility(View.VISIBLE);
                    if (isUp){
                        String areaNameSearch = "";
                        if (tagTops.size()>0){
                            areaNameSearch = tagTops.get(0).region;
                        }
                        if (TextUtils.isEmpty(areaNameSearch)){
                            mPoiSearch.searchInCity(new PoiCitySearchOption().city(SharedPreferenceUtil.getInstance(mContext).getString("city", textview_tackgoodsaddress.getText().toString())).keyword(textview_tackgoodsaddress_detail.getText().toString()).pageNum(0).pageCapacity(20).cityLimit(false));
                        }else {
                            mPoiSearch.searchInCity(new PoiCitySearchOption().city(areaNameSearch).keyword(textview_tackgoodsaddress_detail.getText().toString()).pageNum(0).pageCapacity(20).cityLimit(false));

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!textview_tackgoodsaddress.getText().toString().equals("")|| !textview_tackgoodsaddress.getText().toString().equals(null)||textview_tackgoodsaddress_detail.getText().toString().equals("")|| textview_tackgoodsaddress_detail.getText().toString().equals(null)) {
                    listview_address_search.setVisibility(View.GONE);
                }
                addressDetail = textview_tackgoodsaddress_detail.getText().toString();
            }
        });

        listview_address_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MallSearchDetailBean mallSearchBean = (MallSearchDetailBean) parent.getItemAtPosition(position);
                addressDetail = mallSearchBean.address+mallSearchBean.name;
                lat = mallSearchBean.lat;
                lng = mallSearchBean.lng;
                textview_tackgoodsaddress_detail.setText(addressDetail);
            }
        });
    }
    private void initMap() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initView() {
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);

        textview_tackgoodsname = (EditText) findViewById(R.id.textview_tackgoodsname);
        textview_tackgoodstel = (EditText) findViewById(R.id.textview_tackgoodstel);
        textview_tackgoodsaddress = (TextView) findViewById(R.id.textview_tackgoodsaddress);
        textview_tackgoodsaddress_detail = (EditText) findViewById(R.id.textview_tackgoodsaddress_detail);
        img_is_detault = (ImageView) findViewById(R.id.img_is_detault);
        layout_tackgoodsaddress = (LinearLayout) findViewById(R.id.layout_tackgoodsaddress);
        listview_address_search = (ListView) findViewById(R.id.listview_address_search);
        layout_show_address_detail = (LinearLayout) findViewById(R.id.layout_show_address_detail);
        layout_main = (LinearLayout) findViewById(R.id.layout_main);


        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        ib_titlebar_back.setOnClickListener(this);
        tv_titlebar_other.setOnClickListener(this);
        textview_tackgoodsname.setOnClickListener(this);
        textview_tackgoodstel.setOnClickListener(this);
        textview_tackgoodsaddress.setOnClickListener(this);
        textview_tackgoodsaddress_detail.setOnClickListener(this);
        img_is_detault.setOnClickListener(this);
        layout_tackgoodsaddress.setOnClickListener(this);
        layout_main.addOnLayoutChangeListener(this);
    }

    private void goNext(Class cls,int requestCode){
        Intent intent = new Intent(mContext,cls);
        intent.putParcelableArrayListExtra("tagTops",tagTops);
        intent.putExtra("previous",requestCode);
        startActivityForResult(intent,requestCode);
    }
    private void updateAddress(MallAddress mallAddressUp/*int id,int areaId,String address,int isDefault,String conginer,String mobile*/){
        mPDialog.showDialog();
        CommUtil.updateAddress(mContext,mallAddressUp.areaName,mallAddressUp.id,mallAddressUp.areaId,mallAddressUp.address,mallAddressUp.isDefault,mallAddressUp.consigner,mallAddressUp.mobile,updateHandler);
    }
    private AsyncHttpResponseHandler updateHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0){
                    setResult(Global.RESULT_OK);
                    finishWithAnimation();
                }else {
                    ToastUtil.showToastShortCenter(mContext,object.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };
    private void addAddress(){
        mPDialog.showDialog();
        conginer = textview_tackgoodsname.getText().toString();
        mobile = textview_tackgoodstel.getText().toString();
        CommUtil.addAddress(mContext,areaId,areaName,textview_tackgoodsaddress_detail.getText().toString(),isDefault,conginer,mobile,lat,lng,handler);
    }
    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Utils.mLogError("==-->添加地址信息: "+new String(responseBody));
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    if (object.has("data")&&!object.isNull("data")){
                        JSONObject objectdata = object.getJSONObject("data");
                        if (objectdata.has("consigner")&&!objectdata.isNull("consigner")){
                           conginer =  objectdata.getString("consigner");
                        }
                        if (objectdata.has("mobile")&&!objectdata.isNull("mobile")){
                            mobile = objectdata.getString("mobile");
                        }
                        if (objectdata.has("areaName")&&!objectdata.isNull("areaName")){
                            areaName = objectdata.getString("areaName");
                        }
                        if (objectdata.has("address")&&!objectdata.isNull("address")){
                            address = objectdata.getString("address");
                        }
                        if (objectdata.has("isDefault")&&!objectdata.isNull("isDefault")){
                            isDefault = objectdata.getInt("isDefault");
                        }
                        if (objectdata.has("areaId")&&!objectdata.isNull("areaId")){
                            areaId = objectdata.getInt("areaId");
                        }
                        if (objectdata.has("id")&&!objectdata.isNull("id")){
                            addressId = objectdata.getInt("id");
                        }
                    }
                    if (previous==Global.MALL_SELF_ADDRESS_BOTTOM_CLICK){
                        setResult(Global.RESULT_OK);
                    }else if (previous==Global.MALL_ORDER_ADDRESS){
                        Intent intent = new Intent();
                        intent.putExtra("consigner",conginer);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("areaName",areaName);
                        intent.putExtra("address", address);
                        intent.putExtra("isDefault",isDefault);
                        intent.putExtra("areaId",areaId);
                        intent.putExtra("id",addressId);
                        setResult(Global.RESULT_OK,intent);
                    }
                    finishWithAnimation();
                }else {
                   ToastUtil.showToastShortCenter(mContext,object.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };



    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null|| poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//            Toast.makeText(mContext, "未找到结果",Toast.LENGTH_LONG).show();
            listview_address_search.setVisibility(View.GONE);
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            searchLists.clear();
            List<PoiInfo> infos = poiResult.getAllPoi();
            if (infos.size()>0){
                listview_address_search.setVisibility(View.VISIBLE);
            }
            for (int i=0;i<infos.size();i++){
                MallSearchDetailBean mallSearchDetail = new MallSearchDetailBean();
                if (!TextUtils.isEmpty(infos.get(i).address)){
                    Utils.mLogError("==-->1111 "+infos.get(i).address);
                    String name = infos.get(i).name;
                    String address = infos.get(i).address;
                    LatLng latLng = infos.get(i).location;
//                Utils.mLogError("==-->latLng "+(latLng ==null)+" 。。。。  "+(latLng.equals(null)) );
                    mallSearchDetail.name = name;
                    mallSearchDetail.address = address;
                    if (latLng!=null){
                        mallSearchDetail.lat = latLng.latitude;
                        mallSearchDetail.lng = latLng.longitude;
                    }else {
                        mallSearchDetail.lat = 0;
                        mallSearchDetail.lng = 0;
                    }
                    searchLists.add(mallSearchDetail);
                }

            }
            if (searchLists.size()>0){
                mallSearchDetailAdapter = new MallSearchDetailAdapter<MallSearchDetailBean>(mContext,searchLists);
                listview_address_search.setAdapter(mallSearchDetailAdapter);
                mallSearchDetailAdapter.setTitleName(textview_tackgoodsaddress_detail.getText().toString());
                mallSearchDetailAdapter.notifyDataSetChanged();
            }
        }else {
            listview_address_search.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Global.RESULT_OK){
            if (requestCode==Global.MALL_ADDRCKGOODSADDRESS){
                tagTops = data.getParcelableArrayListExtra("tagTops");
                StringBuilder areaSp = new StringBuilder();
                StringBuilder addressSp = new StringBuilder();
                for (int i=0;i<tagTops.size();i++){
                    areaSp.append(tagTops.get(i)+",");
                    addressSp.append(tagTops.get(i).region+",");
                }
                try {
                    areaId = tagTops.get(tagTops.size()-1).areaId;
                    areaName =addressSp.toString();
                    textview_tackgoodsaddress.setText(areaName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showAddressDialog(){
        MDialog mDialog = new MDialog.Builder(mContext)
                .setTitle("")
                .setTitleShow(0)//1 显示  其他不显示
                .setType(MDialog.DIALOGTYPE_CONFIRM)
                .setMessage("修改的信息还未保存，确认现在返回？")
                .setCancelStr("取消")
                .setOKStr("确定")
                .setTitleTextColor(R.color.a333333)
                .positiveListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setResult(Global.RESULT_OK);
                        finishWithAnimation();
                    }
                }).build();
        mDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            isUp = true;
//            Toast.makeText(mContext, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            isUp = false;
            listview_address_search.setVisibility(View.GONE);
//            Toast.makeText(mContext, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:

                if (!TextUtils.isEmpty(textview_tackgoodsname.getText())
                        ||!TextUtils.isEmpty(textview_tackgoodstel.getText())
                        ||!TextUtils.isEmpty(textview_tackgoodsaddress.getText())
                        ||!TextUtils.isEmpty(textview_tackgoodsaddress_detail.getText())){
                    showAddressDialog();
                }else {
                    setResult(Global.RESULT_OK);
                    finishWithAnimation();
                }

                break;
            case R.id.tv_titlebar_other:
                if (TextUtils.isEmpty(textview_tackgoodsname.getText())){
                    ToastUtil.showToastShortCenter(mContext,"请输入收货人姓名");
                    return;
                }
                if (TextUtils.isEmpty(textview_tackgoodstel.getText())){
                    ToastUtil.showToastShortCenter(mContext,"请输入手机号码");
                    return;
                }
                boolean isPhone = Utils.checkPhone(mContext,textview_tackgoodstel);
                if (!isPhone){
                    ToastUtil.showToastShortCenter(mContext,"请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(textview_tackgoodsaddress.getText())){
                    ToastUtil.showToastShortCenter(mContext,"请选择所在区域");
                    return;
                }
                if (TextUtils.isEmpty(textview_tackgoodsaddress_detail.getText())){
                    ToastUtil.showToastShortCenter(mContext,"请输入详细地址");
                    return;
                }
                if (addressId>0){//这里是刷新除了名字 手机号 区域 详细信息从控件获取其他都可以从前对象获取
                    MallAddress mallAddressUpdate = new MallAddress();
                    mallAddressUpdate.consigner = textview_tackgoodsname.getText().toString();
                    mallAddressUpdate.mobile = textview_tackgoodstel.getText().toString();
                    mallAddressUpdate.areaName = textview_tackgoodsaddress.getText().toString();
                    mallAddressUpdate.address = textview_tackgoodsaddress_detail.getText().toString();
                    mallAddressUpdate.isDefault = isDefault;
                    mallAddressUpdate.id = addressId;
                    mallAddressUpdate.areaId = areaId;
                    updateAddress(mallAddressUpdate);
                }else {
                    addAddress();
                }
                break;
            case R.id.textview_tackgoodsname:

                break;
            case R.id.textview_tackgoodstel:
                break;
            case R.id.layout_tackgoodsaddress:
            case R.id.textview_tackgoodsaddress:
                goNext(MallSearchChooseAddressActivity.class, Global.MALL_ADDRCKGOODSADDRESS);
                break;
            case R.id.img_is_detault:
                if (isDefault==0){
                    isDefault = 1;
                    img_is_detault.setBackgroundResource(R.drawable.noty_yes);
                }else if (isDefault==1){
                    isDefault = 0;
                    img_is_detault.setBackgroundResource(R.drawable.noty_no);
                }
                break;
        }
    }



}
