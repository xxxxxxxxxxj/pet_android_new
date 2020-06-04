package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CommAddressRecycleAdapter;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.event.DeleteAddressSuccessEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;

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
import butterknife.OnClick;

public class CommonAddressActivity extends SuperActivity {
    public static CommonAddressActivity commonAddressActivity;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ib_titlebar_back;
    @BindView(R.id.tv_titlebar_title)
    TextView tv_titlebar_title;
    @BindView(R.id.layout_un_address)
    LinearLayout layout_un_address;
    @BindView(R.id.layout_add_address)
    LinearLayout layout_add_address;
    @BindView(R.id.myRecycle)
    RecyclerView myRecycle;
    private List<CommAddr> totalList = new ArrayList<CommAddr>();
    private int addressId = 0;
    private int previous = 0;
    private CommAddressRecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setColorBar();
        setContentView(R.layout.common_address);

        commonAddressActivity = this;
        ButterKnife.bind(this);
        addressId = getIntent().getIntExtra("addr_id", 0);
        previous = getIntent().getIntExtra("previous", 0);

//        Utils.mLogError("接受到的Id"+addressId);
        setView();
    }

    private void setView() {
        tv_titlebar_title.setText("我的地址");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        myRecycle.setLayoutManager(layoutManager);
        recycleAdapter = new CommAddressRecycleAdapter(mContext, totalList);
        //设置被选择的地址
        recycleAdapter.setAddressId(addressId);
        myRecycle.setAdapter(recycleAdapter);
        recycleAdapter.setOnItemClickRecyleView(new CommAddressRecycleAdapter.OnItemClickRecyleView() {
            @Override
            public void click(View v, int position) {
                CommAddr commAddr = (CommAddr) totalList.get(position);
                Intent intent = new Intent();
                intent.putExtra("addr", commAddr.address);
                intent.putExtra("addr_lat", commAddr.lat);
                intent.putExtra("addr_lng", commAddr.lng);
                intent.putExtra("addr_id", commAddr.Customer_AddressId);
                intent.putExtra("commAddr", commAddr);
                setResult(Global.RESULT_OK, intent);
                finishWithAnimation();
            }
        });

        recycleAdapter.setLongClickListener(new CommAddressRecycleAdapter.ItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                if (totalList!=null&&totalList.size()>0){
                    for (int i = 0; i < totalList.size(); i++) {
                        if (i==position){
                            totalList.get(i).setShowDel(true);
                        }else {
                            totalList.get(i).setShowDel(false);
                        }
                    }
                    recycleAdapter.notifyDataSetChanged();
                }
            }
        });

        recycleAdapter.setDelListener(new CommAddressRecycleAdapter.DelClickListener() {
            @Override
            public void onItemDelClick(int position) {
                final CommAddr commAddr = totalList.get(position);
                new AlertDialogDefault(mContext).builder()
                        .setTitle("提示").setMsg("确定删除地址吗？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPDialog.showDialog();
                        CommUtil.deleteServiceAddress(mContext, commAddr.Customer_AddressId, deleteServiceAddress);
                    }
                }).show();
            }
        });
        recycleAdapter.setGoneDelListener(new CommAddressRecycleAdapter.GoneDelClickListener() {
            @Override
            public void onItemGoneDelClick(int position) {
                if (totalList.size() > 0 && totalList.size() > position) {
                    totalList.get(position).setShowDel(false);
                    recycleAdapter.notifyItemChanged(position);
                }
            }
        });
        getAddressList();
    }

    private void getAddressList() {
        mPDialog.showDialog();
        totalList.clear();
        CommUtil.queryServiceAddress(spUtil.getInt("userid", 0), CommonAddressActivity.this, queryServiceAddress);
    }

    private AsyncHttpResponseHandler queryServiceAddress = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Utils.mLogError("==-->query address " + new String(responseBody));
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("dataset") && !object.isNull("dataset")) {
                            JSONArray jsonArray = object.getJSONArray("dataset");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    totalList.add(CommAddr.json2Entity(jsonArray.getJSONObject(i)));
                                }
                            }
                        }
                    }
                } else if (code == 130) {
                    Intent intent = new Intent(CommonAddressActivity.this, LoginNewActivity.class);
                    intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                    getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                    startActivity(intent);
                    finishWithAnimation();
                } else {
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                        String msg = jsonObject.getString("msg");
                        ToastUtil.showToastShort(CommonAddressActivity.this, msg);
                    }
                }
                if (totalList.size() > 0) {
                    layout_un_address.setVisibility(View.GONE);
                    recycleAdapter.notifyDataSetChanged();
                } else {
                    layout_un_address.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private AsyncHttpResponseHandler deleteServiceAddress = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    String msg = jsonObject.getString("msg");
                    ToastUtil.showDeleteToast(mContext);
                    getAddressList();
                } else {
                    // 服务器返回字段输出
                    String msg = jsonObject.getString("msg");
                    ToastUtil.showToastShort(CommonAddressActivity.this, msg);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            mPDialog.closeDialog();
        }
    };

    public void setResult(Intent data) {
        CommAddr commAddrSave = new CommAddr();
        Intent intent = new Intent();
        intent.putExtra("addr", data.getStringExtra("addr"));
        intent.putExtra("addr_lat", data.getDoubleExtra("addr_lat", 0));
        intent.putExtra("addr_lng", data.getDoubleExtra("addr_lng", 0));
        intent.putExtra("addr_id", data.getIntExtra("addr_id", 0));
        intent.putExtra("supplement", data.getStringExtra("supplement"));
        intent.putExtra("linkman", data.getStringExtra("linkman"));
        intent.putExtra("telephone", data.getStringExtra("telephone"));
        commAddrSave.address = data.getStringExtra("addr");
        commAddrSave.lat = data.getDoubleExtra("addr_lat", 0);
        commAddrSave.lng = data.getDoubleExtra("addr_lng", 0);
        commAddrSave.Customer_AddressId = data.getIntExtra("addr_id", 0);
        commAddrSave.supplement = data.getStringExtra("supplement");
        commAddrSave.linkman = data.getStringExtra("linkman");
        commAddrSave.telephone = data.getStringExtra("telephone");
        intent.putExtra("commAddr", commAddrSave);
        setResult(Global.RESULT_OK, intent);
        finishWithAnimation();
    }

    private void JumpToNext(Class clazz, int requestCode) {
        Intent intent = new Intent(CommonAddressActivity.this, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("previous", previous);
        startActivityForResult(intent, requestCode);
    }

    public void goNextAddress(Class clazz, int requestCode, CommAddr commAddr) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtra("commAddr", commAddr);
        intent.putExtra("previous", previous);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.COMMONADDRESS_ADDADDRESS) {
                getAddressList();
            } else if (requestCode == Global.COMMONADDRESS_EDIT_ADDADDRESS) {
                getAddressList();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < totalList.size(); i++) {
                    if (totalList.get(i).isShowDel){
                        totalList.get(i).setShowDel(false);
                        recycleAdapter.notifyItemChanged(i);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_title,R.id.layout_un_address, R.id.layout_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.tv_titlebar_title:
                break;
            case R.id.layout_un_address:
                break;
            case R.id.layout_add_address:
                for (int i = 0; i < totalList.size(); i++) {
                    if (totalList.get(i).isShowDel){
                        totalList.get(i).setShowDel(false);
                        recycleAdapter.notifyItemChanged(i);
                    }
                }
                JumpToNext(AddServiceAddressActivity.class, Global.COMMONADDRESS_ADDADDRESS);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteAddressSuccess(DeleteAddressSuccessEvent deleteAddressSuccess){
        //刷新列表
        getAddressList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
