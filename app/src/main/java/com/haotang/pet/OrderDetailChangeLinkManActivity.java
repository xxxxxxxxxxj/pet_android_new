package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/15 0015.
 */

public class OrderDetailChangeLinkManActivity extends SuperActivity {
    @BindView(R.id.show_top)
    LinearLayout show_top;
    @BindView(R.id.textview_ok)
    TextView textview_ok;
    @BindView(R.id.edittext_username)
    EditText edittext_username;
    @BindView(R.id.edittext_userphone)
    EditText edittext_userphone;
    @BindView(R.id.img_clo_img)
    ImageView imgCloImg;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.textview_userphone)
    TextView textviewUserphone;
    @BindView(R.id.textview_useraddress)
    TextView textviewUseraddress;
    @BindView(R.id.layout_show_userdetail)
    LinearLayout layoutShowUserdetail;
    @BindView(R.id.layout_change_userdetai)
    LinearLayout layoutChangeUserdetai;
    @BindView(R.id.textview_username)
    TextView textviewUsername;
    private String username;
    private String userphone;
    private int addressId;
    private String address;
    private CommAddr commAddr;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_order_change_linkman);
        ButterKnife.bind(this);
        getIntentData();
        setView();
    }

    private void setView() {
        if (type == 0) {
            layoutShowUserdetail.setVisibility(View.VISIBLE);
            layoutChangeUserdetai.setVisibility(View.GONE);
        } else if (type == 1) {
            layoutShowUserdetail.setVisibility(View.GONE);
            layoutChangeUserdetai.setVisibility(View.VISIBLE);
        }
    }

    private void getIntentData() {
        type = getIntent().getIntExtra("type", 0);
        commAddr = (CommAddr) getIntent().getSerializableExtra("commAddr");
//        username = getIntent().getStringExtra("username");
        username = commAddr.linkman;
//        userphone = getIntent().getStringExtra("userphone");
        userphone = commAddr.telephone;
//        addressId = getIntent().getIntExtra("addressId",0);
        addressId = commAddr.Customer_AddressId;
        address = commAddr.address+commAddr.supplement;
        edittext_username.setText(username + "");
        edittext_userphone.setText(userphone + "");
        edittext_username.setSelection(username.length());
        edittext_userphone.setSelection(userphone.length());
        textviewUsername.setText(username);
        textviewUserphone.setText(userphone);
        textviewUseraddress.setText(address);
    }

    private void addServiceAddress() {
        CommUtil.addServiceAddress(mContext, addressId, null, 0, 0, null, edittext_username.getText().toString(), edittext_userphone.getText().toString(), handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        String linkman = "";
                        if (objectData.has("linkman") && !objectData.isNull("linkman")) {
                            linkman = objectData.getString("linkman");
                            commAddr.linkman = linkman;
                        }
                        String telephone = "";
                        if (objectData.has("telephone") && !objectData.isNull("telephone")) {
                            telephone = objectData.getString("telephone");
                            commAddr.telephone = telephone;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("linkman", linkman);
                        intent.putExtra("telephone", telephone);
                        intent.putExtra("commAddrChange", commAddr);
                        setResult(Global.RESULT_OK, intent);
                        finishWithAnimation();
                    }
                } else {
                    ToastUtil.showToastShortCenter(mContext, object.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.bottom_silent, R.anim.activity_close);
    }

    @OnClick({R.id.show_top, R.id.textview_ok, R.id.img_clo_img, R.id.img_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.show_top:
                finishWithAnimation();
                break;
            case R.id.textview_ok:
                if (TextUtils.isEmpty(edittext_username.getText())) {
                    ToastUtil.showToastShortCenter(mContext, "联系人不能为空");
                    return;
                }
                if (TextUtils.isEmpty(edittext_userphone.getText())) {
                    ToastUtil.showToastShortCenter(mContext, "联系方式不能为空");
                    return;
                }
                if (!Utils.checkPhone1(mContext, edittext_userphone)) {
                    ToastUtil.showToastShortCenter(mContext, "请检查手机号是否正确");
                    return;
                }
                addServiceAddress();
                break;
            case R.id.img_clo_img:
                finish();
                break;
            case R.id.img_edit:
                Intent intent = new Intent(mContext, OrderDetailChangeLinkManActivity.class);
                intent.putExtra("commAddr", commAddr);
                intent.putExtra("type", 1);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open, R.anim.bottom_silent);
                finish();
                break;
        }
    }

}
