package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/7 0007.
 */

public class ShopOwnerActivity extends SuperActivity {
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.sriv_beauticiandetail_beautician)
    ImageView srivBeauticiandetailBeautician;
    @BindView(R.id.tv_beauticiandetail_beauticianname)
    TextView tvBeauticiandetailBeauticianname;
    @BindView(R.id.textview_become_beau_level)
    TextView textviewBecomeBeauLevel;
    @BindView(R.id.layout_next_level)
    LinearLayout layoutNextLevel;
    @BindView(R.id.texview_is_home_or_shop)
    TextView texviewIsHomeOrShop;
    @BindView(R.id.texview_beau_level)
    TextView texviewBeauLevel;
    @BindView(R.id.relayout_inside_text)
    TextView relayoutInsideText;
    @BindView(R.id.layout_beau_icon_left)
    LinearLayout layoutBeauIconLeft;
    @BindView(R.id.textview_intro)
    TextView textviewIntro;
    private int id;
    private String avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_owner);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("beautician_id",0);
        tvTitlebarTitle.setText("店长主页");
        queryManagerById();
    }
    private void queryManagerById(){
        CommUtil.queryManagerById(mContext,id,handler);
    }
    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code==0){
                    if (jsonObject.has("data")&&!jsonObject.isNull("data")){
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("worker")&&!object.isNull("worker")){
                            JSONObject objectW = object.getJSONObject("worker");
                            if (objectW.has("realName")&&!objectW.isNull("realName")){
                                tvBeauticiandetailBeauticianname.setText(objectW.getString("realName"));
                            }if (objectW.has("avatar")&&!objectW.isNull("avatar")){
                                avatar = objectW.getString("avatar");
                                GlideUtil.loadCircleImg(mContext,avatar,srivBeauticiandetailBeautician,R.drawable.icon_default);
                            }if (objectW.has("shopName")&&!objectW.isNull("shopName")){
                                texviewIsHomeOrShop.setText(objectW.getString("shopName"));
                            }if (objectW.has("workerExpertise")&&!objectW.isNull("workerExpertise")){
                                JSONArray array = objectW.getJSONArray("workerExpertise");
                                if (array.length()>0){
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i =0;i<array.length();i++){
                                        stringBuilder.append(array.getString(i)+" ");
                                    }
                                    relayoutInsideText.setText(stringBuilder.toString());
                                }
                            }if (objectW.has("introduction")&&!objectW.isNull("introduction")){
                                textviewIntro.setText(objectW.getString("introduction"));
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

        }
    };
    @OnClick({R.id.ib_titlebar_back, R.id.sriv_beauticiandetail_beautician})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.sriv_beauticiandetail_beautician:
                intent = new Intent(mContext,LookImgActivity.class);
                intent.putExtra("avatar",avatar);
                startActivity(intent);
                break;
        }
    }
}
