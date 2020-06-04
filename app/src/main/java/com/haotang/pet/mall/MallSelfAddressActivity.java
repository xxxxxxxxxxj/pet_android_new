package com.haotang.pet.mall;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.adapter.MallAdapter.MallSelfAddressAdapter;
import com.haotang.pet.entity.mallEntity.MallAddress;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.swipemenulistview.SwipeMenu;
import com.haotang.pet.swipemenulistview.SwipeMenuCreator;
import com.haotang.pet.swipemenulistview.SwipeMenuItem;
import com.haotang.pet.swipemenulistview.SwipeMenuListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MallSelfAddressActivity extends SuperActivity implements View.OnClickListener{
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    private SwipeMenuListView prl_Address_list;
    private MallSelfAddressAdapter mallSelfAddressAdapter;
    private List<MallAddress> addressList = new ArrayList<>();
    private TextView button_add_address;
    private int previous;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mallselfaddress);
        MApplication.listAppoint.add(mContext);
        initAcceptData();
        initView();
        initMenuListView();
        setView();
        initListener();

    }

    private void initAcceptData() {
        previous = getIntent().getIntExtra("previous",-1);
        id = getIntent().getIntExtra("id",-1);

    }

    private void initListener() {
        prl_Address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MallAddress mallAddress = (MallAddress) parent.getItemAtPosition(position);
                if (previous == Global.MALL_ORDER_CHANGE_ADDRESS) {
                    Intent intent = new Intent();
                    intent.putExtra("consigner",mallAddress.consigner);
                    intent.putExtra("mobile",mallAddress.mobile);
                    intent.putExtra("areaName", mallAddress.areaName);
                    intent.putExtra("address", mallAddress.address);
                    intent.putExtra("isDefault",mallAddress.isDefault);
                    intent.putExtra("areaId",mallAddress.areaId);
                    intent.putExtra("id",mallAddress.id);
                    setResult(Global.RESULT_OK,intent);
                    finishWithAnimation();
                }else if(previous == Global.WEBVIEW_TO_ADDRESS){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mallAddress", mallAddress);
                    intent.putExtras(bundle);
                    setResult(Global.RESULT_OK,intent);
                    finishWithAnimation();
                }
            }
        });
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
                    getMallAddressList();
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
    private void getMallAddressList(){
        mPDialog.showDialog();
        addressList.clear();
        CommUtil.mallAddressList(mContext,handler);
    }
    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    if(object.has("data")&&!object.isNull("data")){
                        JSONArray array = object.getJSONArray("data");
                        if (array.length()>0){
                            for (int i =0;i<array.length();i++){
                                addressList.add(MallAddress.json2Entity(array.getJSONObject(i)));
                            }
                        }
                        if (addressList.size()>0){
                            mallSelfAddressAdapter.setChooseState(id);
                            mallSelfAddressAdapter.notifyDataSetChanged();
                        }else {
                            mallSelfAddressAdapter.notifyDataSetChanged();
                        }
                    }else {
                        if (addressList.size()>0){
                            mallSelfAddressAdapter.setChooseState(id);
                            mallSelfAddressAdapter.notifyDataSetChanged();
                        }else {
                            mallSelfAddressAdapter.notifyDataSetChanged();
                        }
                    }
                }else {

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

    private void deleteAddress(int addressId){
        mPDialog.showDialog();
        CommUtil.deleteAddress(mContext,addressId,deleteHandler);
    }
    private AsyncHttpResponseHandler deleteHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code==0){
                    getMallAddressList();
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
    private void setView() {
        mallSelfAddressAdapter = new MallSelfAddressAdapter<MallAddress>(mContext,addressList);
        prl_Address_list.setAdapter(mallSelfAddressAdapter);
        getMallAddressList();
    }

    private void initView() {
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        prl_Address_list = (SwipeMenuListView) findViewById(R.id.prl_Address_list);
        button_add_address = (TextView) findViewById(R.id.button_add_address);
        tv_titlebar_title.setText("选择收货地址");
        ib_titlebar_back.setOnClickListener(this);
        button_add_address.setOnClickListener(this);
    }

    // 将dp转化为px
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void showAddressDialog(final MallAddress mallAddress){
        MDialog mDialog = new MDialog.Builder(mContext)
                .setTitle("")
                .setTitleShow(0)//1 显示  其他不显示
                .setType(MDialog.DIALOGTYPE_CONFIRM)
                .setMessage("确定删除收货地址？")
                .setCancelStr("再想想")
                .setOKStr("删除地址")
                .setTitleTextColor(R.color.a333333)
                .positiveListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteAddress(mallAddress.id);
                    }
                }).build();
        mDialog.show();
    }
    private void initMenuListView() {
        // 创建一个SwipeMenuCreator供ListView使用
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                 创建一个侧滑菜单
                 SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                 //给该侧滑菜单设置背景
//                 openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,0xC9,0xCE)));
                 openItem.setBackground(new ColorDrawable(Color.parseColor("#DD7D62")));
                 //设置宽度
                 openItem.setWidth(dp2px(65));
                 //设置名称
                 openItem.setTitle("设为默认");
                 //字体大小
                 openItem.setTitleSize(14);
                 //字体颜色
                 openItem.setTitleColor(Color.WHITE);
                 //加入到侧滑菜单中
                 menu.addMenuItem(openItem);

                // 创建一个侧滑菜单
                SwipeMenuItem delItem = new SwipeMenuItem(
                        getApplicationContext());
                // 给该侧滑菜单设置背景
                // delItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F,
                // 0x25)));
                delItem.setBackground(new ColorDrawable(Color.parseColor("#FF3A1E")));
                // 设置宽度
                delItem.setWidth(dp2px(65));
                // 设置图片
                // delItem.setIcon(R.drawable.icon_del);
                delItem.setTitle("　删除　");
                delItem.setTitleSize(14);
                delItem.setTitleColor(Color.WHITE);
                // 加入到侧滑菜单中
                menu.addMenuItem(delItem);
            }
        };
        prl_Address_list.setMenuCreator(creator);
        // 侧滑菜单的相应事件
        prl_Address_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu,
                                           int index) {
                switch (index) {
                     case 0://第一个添加的菜单的响应时间(打开)
                         if (addressList.size()>0){
                             MallAddress mallAddressUp = addressList.get(position);
                             mallAddressUp.isDefault =1;
                             updateAddress(mallAddressUp);
                         }
                     break;
                    case 1:// 第二个添加的菜单的响应时间(删除)
                        if (addressList.size()>0){
                            MallAddress mallAddress = addressList.get(position);
                            showAddressDialog(mallAddress);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Global.RESULT_OK){
            if (requestCode==Global.MALL_SELF_ADDRESS_CLICK){
                Utils.mLogError("==-->11111111111111111111111111111111111");
                getMallAddressList();//跳转编辑更新地址返回重新刷新
            }else if (requestCode==Global.MALL_SELF_ADDRESS_BOTTOM_CLICK){
                Utils.mLogError("==-->22222222222222222222222222222222222");
                getMallAddressList();//跳转编辑更新地址返回重新刷新
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.button_add_address:
                Intent intent = new Intent(mContext, MallAddTackGoodsAddressActivity.class);
                intent.putExtra("previous",Global.MALL_SELF_ADDRESS_BOTTOM_CLICK);
                startActivityForResult(intent, Global.MALL_SELF_ADDRESS_BOTTOM_CLICK);
                break;
        }
    }
}
