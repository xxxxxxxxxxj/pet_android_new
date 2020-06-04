package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/17 0017.
 */

public class NewUserGiveCouponActivity extends SuperActivity {
    @BindView(R.id.iv_newuser_libao)
    ImageView ivNewuserLibao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_give_coupon);
        ButterKnife.bind(this);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, -0.1f);
        translateAnimation.setDuration(2000);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        ivNewuserLibao.startAnimation(translateAnimation);
        mPDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.iv_newuser_lq, R.id.iv_newuser_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_newuser_lq:
                mPDialog.showDialog();
                CommUtil.ReceiveCoupon(mContext, receiveCouponHandler);
                break;
            case R.id.iv_newuser_close:
                finishWithAnimation();
                break;
        }
    }

    private AsyncHttpResponseHandler receiveCouponHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("pic") && !jData.isNull("pic")) {
                            String imgUrl = jData.getString("pic");
                            startActivity(new Intent(mContext, DialogActivity.class).putExtra("imgUrl", imgUrl));
                            finish();
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(mContext, msg);
                    }
                }
            } catch (Exception e) {
                ToastUtil.showToastShortBottom(mContext, "数据异常");
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };
}
