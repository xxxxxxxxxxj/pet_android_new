package com.haotang.pet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会员通知页面
 */
public class MemberUpgradeActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_member_upgrade)
    ImageView ivMemberUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getAccountCenter();
    }

    private void initData() {
    }

    private void findView() {
        setContentView(R.layout.activity_member_upgrade);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("会员通知");
    }

    private void setLinster() {

    }

    private void getAccountCenter() {
        mPDialog.showDialog();
        CommUtil.accountCenter(mContext, accountHandler);
    }

    private AsyncHttpResponseHandler accountHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject obj = new JSONObject(new String(responseBody));
                int code = obj.getInt("code");
                if (code == 0) {
                    if (obj.has("data") && !obj.isNull("data")) {
                        JSONObject objdata = obj.getJSONObject("data");
                        if (objdata.has("user") && !objdata.isNull("user")) {
                            JSONObject objUser = objdata.getJSONObject("user");
                            if (objUser.has("userTipUrl") && !objUser.isNull("userTipUrl")) {
                                Log.e("TAG", "imgurl = " + objUser.getString("userTipUrl"));
                                GlideUtil.loadImg(mContext, objUser.getString("userTipUrl"), ivMemberUpgrade, R.drawable.icon_memberupgrade);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }
}
