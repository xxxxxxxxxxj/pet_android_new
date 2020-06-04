package com.haotang.pet.mall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.adapter.MallAdapter.MallSearchAddressListAdapter;
import com.haotang.pet.adapter.MallAdapter.MallSearchChooseAddressTopAdapter;
import com.haotang.pet.entity.mallEntity.RegionChildren;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.HorizontalListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求服务器选择地址
 * Created by Administrator on 2017/9/1.
 */

public class MallSearchChooseAddressActivity extends SuperActivity implements View.OnClickListener{
    private LinearLayout layout_mall_top;
    private ImageView img_right_close;
    private ListView listView_show_address;
    private MallSearchAddressListAdapter mallSearchAddressListAdapter;
    private HorizontalListView mall_search_top_tags;
    private MallSearchChooseAddressTopAdapter mallSearchChooseAddressTopAdapter;
    public ArrayList<RegionChildren> topTags = new ArrayList<>();
    public List<RegionChildren> bottomAddresss = new ArrayList<>();
    private int pos = -1;
    private RegionChildren pleaseChoose = null;
    private int previous=-1;
    private boolean isLast = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_search_address);
        MApplication.listAppoint.add(mContext);
        topTags.clear();
        topTags = getIntent().getParcelableArrayListExtra("tagTops");
        previous= getIntent().getIntExtra("previous",-1);
        pleaseChoose = new RegionChildren();
        pleaseChoose.areaId = -1;
        pleaseChoose.region = "请选择";
        //如果从地址列表过来编辑的这个先不用添加
        if (topTags.size()<=0){
            topTags.add(pleaseChoose);
        }
        initView();
        setView();
        initListener();
        if (previous==Global.MALL_ADDRCKGOODSADDRESS){
            if (topTags.size()<=2){
                getServiceAddressEdit(0);
            }else{
                Utils.mLogError("==-->topTags "+topTags.size());
                for (int i=0;i<topTags.size();i++){
                    Utils.mLogError("==-->topTags "+topTags.get(i).region+" "+topTags.get(i).areaId);
                }
                getServiceAddressEdit(topTags.get(topTags.size()-2).areaId);
                getServiceAddressEdit(topTags.get(topTags.size()-1).areaId);
            }
        }else {
            getServiceAddress(0);
        }
    }



    private void setView() {
        mallSearchChooseAddressTopAdapter = new MallSearchChooseAddressTopAdapter<RegionChildren>(mContext,topTags);
        mall_search_top_tags.setAdapter(mallSearchChooseAddressTopAdapter);
        mallSearchChooseAddressTopAdapter.setClickPosttion(0);
        mallSearchChooseAddressTopAdapter.notifyDataSetChanged();

        mallSearchAddressListAdapter = new MallSearchAddressListAdapter<RegionChildren>(mContext,bottomAddresss);
        listView_show_address.setAdapter(mallSearchAddressListAdapter);
        SetBottomState();
        mallSearchChooseAddressTopAdapter.setClickPosttion(topTags.size()-1);
        pos = -1;
        mallSearchChooseAddressTopAdapter.notifyDataSetChanged();
    }

    private void SetBottomState() {
        if (topTags.size()>0){
            mallSearchAddressListAdapter.setTopTagList(topTags);
            mallSearchAddressListAdapter.notifyDataSetChanged();
        }
    }

    private void initListener() {
        mall_search_top_tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                RegionChildren regionChildren = (RegionChildren) parent.getItemAtPosition(position);
                mallSearchChooseAddressTopAdapter.setClickPosttion(position);
                mallSearchChooseAddressTopAdapter.notifyDataSetChanged();
                if (position==0){
                    getServiceAddress(0);
                }else {
                    RegionChildren regionChildrenLeft = (RegionChildren) parent.getItemAtPosition(position-1);
                    getServiceAddress(regionChildrenLeft.areaId);
                }

            }
        });
        listView_show_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegionChildren regionChildren = (RegionChildren) parent.getItemAtPosition(position);
                clickListAndUpdateTop(regionChildren);
            }
        });
    }

    private void clickListAndUpdateTop(RegionChildren regionChildren) {
        if (pos!=-1){
            if (pos!=topTags.size()-1){
                List<RegionChildren> topTagRemove = new ArrayList<RegionChildren>();
                for (int i=pos;i<topTags.size();i++){
                    topTagRemove.add(topTags.get(i));
                }
                topTags.removeAll(topTagRemove);
            }
        }
        removePleaseChoose();
        if (!isLast){
            topTags.add(regionChildren);
            topTags.add(pleaseChoose);
        }else {
            topTags.set(topTags.size()-1,regionChildren);
        }
        mallSearchChooseAddressTopAdapter.setClickPosttion(topTags.size()-1);
        pos = -1;
        mallSearchChooseAddressTopAdapter.notifyDataSetChanged();
        if (!isLast){
            getServiceAddress(regionChildren.areaId);
        }else {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("tagTops",topTags);
            setResult(Global.RESULT_OK,intent);
            finishWithAnimation();
        }
    }

    private void removePleaseChoose() {
        int index =-1;
        for (int i=0;i<topTags.size();i++){
            RegionChildren regionChildren = topTags.get(i);
            if (regionChildren.region.equals("请选择")){
                index = i;
            }
        }
        if (index!=-1){
            topTags.remove(index);
        }
        mallSearchChooseAddressTopAdapter.setClickPosttion(topTags.size()-1);
        pos = -1;
        mallSearchChooseAddressTopAdapter.notifyDataSetChanged();
        mallSearchAddressListAdapter.notifyDataSetChanged();
        SetBottomState();
    }

    private void initView() {
        layout_mall_top = (LinearLayout) findViewById(R.id.layout_mall_top);
        img_right_close = (ImageView) findViewById(R.id.img_right_close);
        listView_show_address = (ListView) findViewById(R.id.listView_show_address);
        mall_search_top_tags = (HorizontalListView) findViewById(R.id.mall_search_top_tags);

        img_right_close.setOnClickListener(this);
    }
    private void getServiceAddress(int parentId){//首次进入到最后层级点击直接返回
        mPDialog.showDialog();
        CommUtil.regionChildren(mContext,parentId,handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code==0){
                    if (jsonObject.has("data")&&!jsonObject.isNull("data")){
                        JSONArray array = jsonObject.getJSONArray("data");
                        if (array.length()>0){
                            isLast = false;
                            bottomAddresss.clear();
                            for (int i=0;i<array.length();i++){
                                bottomAddresss.add(RegionChildren.json2Entity(array.getJSONObject(i)));
                            }
                            if (bottomAddresss.size()>0){
                                mallSearchAddressListAdapter.notifyDataSetChanged();
                                SetBottomState();
                                listView_show_address.scrollTo(0,0);
                                int posi =-1;
                                if (topTags!=null){
                                    for (int i = 0;i<topTags.size();i++){
                                        RegionChildren regionChildren = topTags.get(i);

                                        for (int j = 0;j<bottomAddresss.size();j++){
                                            RegionChildren regionChildren2 = bottomAddresss.get(j);
                                            if (regionChildren.areaId==regionChildren2.areaId){
                                                posi = j;
                                            }
                                        }
                                    }
                                }
                                if (posi!=-1){
                                    listView_show_address.setSelection(posi);
                                }else {
                                    listView_show_address.setSelection(0);
                                }
                            }
                        }else{
                            removePleaseChoose();
                            Intent intent = new Intent();
                            intent.putParcelableArrayListExtra("tagTops",topTags);
                            setResult(Global.RESULT_OK,intent);
                            finishWithAnimation();
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
    private void getServiceAddressEdit(int parentId){//编辑进入到最后层级点击返回
        mPDialog.showDialog();
        CommUtil.regionChildren(mContext,parentId,editHandler);
    }
    private AsyncHttpResponseHandler editHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code==0){
                    if (jsonObject.has("data")&&!jsonObject.isNull("data")){
                        JSONArray array = jsonObject.getJSONArray("data");
                        if (array.length()>0){
                            bottomAddresss.clear();
                            for (int i=0;i<array.length();i++){
                                bottomAddresss.add(RegionChildren.json2Entity(array.getJSONObject(i)));
                            }
                            if (bottomAddresss.size()>0){
                                mallSearchAddressListAdapter.notifyDataSetChanged();
                                SetBottomState();
                                listView_show_address.scrollTo(0,0);
                                int posi =-1;
                                if (topTags!=null){
                                    for (int i = 0;i<topTags.size();i++){
                                        RegionChildren regionChildren = topTags.get(i);

                                        for (int j = 0;j<bottomAddresss.size();j++){
                                            RegionChildren regionChildren2 = bottomAddresss.get(j);
                                            if (regionChildren.areaId==regionChildren2.areaId){
                                                posi = j;
                                            }
                                        }
                                    }
                                }
                                if (posi!=-1){
                                    listView_show_address.setSelection(posi);
                                }else {
                                    listView_show_address.setSelection(0);
                                }
                            }
                        }else{
                            removePleaseChoose();
                            isLast = true;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_right_close:
                finishWithAnimation();
                break;
        }
    }
}
