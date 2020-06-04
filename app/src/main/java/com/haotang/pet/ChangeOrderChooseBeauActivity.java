package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ChooseBeauRecycleAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/28 0028.
 */

public class ChangeOrderChooseBeauActivity extends SuperActivity {
    public static ChangeOrderChooseBeauActivity act;
    @BindView(R.id.show_top)
    LinearLayout showTop;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgCloseChangeBeau)
    ImageView imgCloseChangeBeau;
    @BindView(R.id.img_can_choose)
    ImageView img_can_choose;
    @BindView(R.id.beau_list)
    RecyclerView beauList;
    @BindView(R.id.img_beau_icon)
    ImageView imgBeauIcon;
    @BindView(R.id.textview_beau_name)
    TextView textviewBeauName;
    @BindView(R.id.textview_old_time)
    TextView textviewOldTime;
    @BindView(R.id.textview_change_time)
    TextView textviewChangeTime;
    @BindView(R.id.layout_check_old_beau_is_appoint)
    LinearLayout layoutCheckOldBeauIsAppoint;
    @BindView(R.id.change_beau_left)
    TextView changeBeauLeft;
    @BindView(R.id.change_beau_right)
    TextView changeBeauRight;
    @BindView(R.id.textview_appoint_time)
    TextView textviewAppointTime;
    private List<Beautician> allChangeBeau;
    private Beautician beauticianOld;
    private ChooseBeauRecycleAdapter changeBeauAdapter;
    private String appointment;
    private Beautician beautionEqId = null;
    private int eqId = -1;
    private Beautician beauClick = null;
    private String tip="";
    private int workId;
    private int OrderId;
    private int shopId;
    private String beauName;
    private boolean isChooseCurrentBeau;
    private String changeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebeau);
        act = this;
        ButterKnife.bind(this);
        getIntentData();
        setView();
    }

    private void setView() {
        textviewBeauName.setText(beauticianOld.name+"");
        for (int i = 0; i < allChangeBeau.size(); i++) {
            Beautician beaution = allChangeBeau.get(i);
            if (beaution.id == beauticianOld.id) {
                eqId = i;
                break;
            }
        }
        if (eqId > -1) {
            beautionEqId = allChangeBeau.get(eqId);
            beautionEqId.isChoose = true;
            allChangeBeau.set(eqId, beautionEqId);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        beauList.setLayoutManager(linearLayoutManager);
        changeBeauAdapter = new ChooseBeauRecycleAdapter(mContext, allChangeBeau);
        beauList.setAdapter(changeBeauAdapter);
        changeBeauAdapter.setOldBeaution(beauticianOld);
        if (eqId > -1) {//原美容师在新的时间存在
            if (isChooseCurrentBeau){
                beauList.setVisibility(View.GONE);
                changeBeauLeft.setVisibility(View.GONE);
                textviewAppointTime.setVisibility(View.GONE);
                layoutCheckOldBeauIsAppoint.setVisibility(View.VISIBLE);
                tvTitle.setText(beauticianOld.name + "当前时间可约");
                GlideUtil.loadCircleImg(mContext, beauticianOld.image, imgBeauIcon, R.drawable.icon_default);
                textviewOldTime.setText(beauticianOld.appointment);
                textviewChangeTime.setText(appointment);
                img_can_choose.setVisibility(View.VISIBLE);
                changeBeauRight.setText("确认修改");
            }else {
                beauList.setVisibility(View.GONE);
                changeBeauLeft.setVisibility(View.VISIBLE);
                textviewAppointTime.setVisibility(View.GONE);
                layoutCheckOldBeauIsAppoint.setVisibility(View.VISIBLE);
                tvTitle.setText(beauticianOld.name + "当前时间可约");
                GlideUtil.loadCircleImg(mContext, beauticianOld.image, imgBeauIcon, R.drawable.icon_default);
                textviewOldTime.setText(beauticianOld.appointment);
                textviewChangeTime.setText(appointment);
                img_can_choose.setVisibility(View.VISIBLE);
                changeBeauRight.setText("确认修改");
            }


        } else {//原美容师在新的时间内不存在
            beauList.setVisibility(View.GONE);
            changeBeauLeft.setVisibility(View.VISIBLE);
            textviewAppointTime.setVisibility(View.GONE);
            layoutCheckOldBeauIsAppoint.setVisibility(View.VISIBLE);
            tvTitle.setText(beauticianOld.name + "当前时间不可约");
            GlideUtil.loadCircleImg(mContext, beauticianOld.image, imgBeauIcon, R.drawable.icon_default);
            textviewOldTime.setText(beauticianOld.appointment);
            textviewChangeTime.setText(appointment);
            img_can_choose.setVisibility(View.GONE);
            changeBeauRight.setText("预约" + beauticianOld.name + "其他时间");
        }
    }

    private void getIntentData() {
        allChangeBeau = (List<Beautician>) getIntent().getSerializableExtra("beaution");
        beauticianOld = (Beautician) getIntent().getSerializableExtra("beautionold");
        appointment = getIntent().getStringExtra("appointment");
        shopId = getIntent().getIntExtra("shopId", 0);
        OrderId = getIntent().getIntExtra("OrderId", 0);
        isChooseCurrentBeau = getIntent().getBooleanExtra("isChooseCurrentBeau", false);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.bottom_silent, R.anim.activity_close);
    }

    public void clickBeau(Beautician beautician, int position) {
        beauClick = beautician;
        for (int i = 0; i < allChangeBeau.size(); i++) {
            allChangeBeau.get(i).isChoose = false;
        }
        beauClick.isChoose = true;
        beauName = beauClick.name;
        workId = beauClick.id;
        allChangeBeau.set(position, beauClick);
        changeBeauAdapter.notifyDataSetChanged();
    }
    private void getOrderCheck(String appTime) {
        CommUtil.modifyOrderCheck(mContext, workId, appTime,OrderId, shopId, handlerCheck);
    }
    private AsyncHttpResponseHandler handlerCheck = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            // TODO Auto-generated method stub
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code =  object.getInt("code");
                if (code==0) {
                    if (object.has("data")&&!object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("tip")&&!objectData.isNull("tip")) {
                            tip = objectData.getString("tip");
                        }else {
//							tip = "";
                        }
                    }else {
//						tip = "";
                    }
                }else {
//					tip = "";
                }
                showChangeYesOrNo();
                /*Intent intent = new Intent(mContext, ChangeOrderYesOrNo.class);
                intent.putExtra("OrderId", OrderId);
                intent.putExtra("workId", workId);
                intent.putExtra("tip", tip);
                intent.putExtra("time",appointment);
                intent.putExtra("beauName",beauName);
                startActivity(intent);*/
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub

        }
    };

    private void getChangeData() {
        CommUtil.modifyOrder(mContext, workId, appointment, OrderId, null, null, changeHandler);
    }
    private String modifyOrderRuleUrl;
    private AsyncHttpResponseHandler changeHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            Utils.mLogError("==-->改单:= " + new String(responseBody));
            try {
                JSONObject Object = new JSONObject(new String(responseBody));
                int code = Object.getInt("code");
                if (code == 0) {
                    EventBus.getDefault().post(new RefreshOrderEvent(true));
                    if (Object.has("msg") && !Object.isNull("msg")) {
                        ToastUtil.showToastShortCenter(mContext, Object.getString("msg"));
                        try {
                            ChangeOrderChooseBeauActivity.act.finish();
                            ChangeOrderNewActivity.act.finish();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (Object.has("msg") && !Object.isNull("msg")) {
                        changeContent = Object.getString("msg");
                    }
                    if (Object.has("data") && !Object.isNull("data")) {
                        JSONObject jobj = Object.getJSONObject("data");
                        if (jobj.has("modifyOrderRuleUrl")&&!jobj.isNull("modifyOrderRuleUrl")){
                            modifyOrderRuleUrl = jobj.getString("modifyOrderRuleUrl");
                        }
                    }
                    showChangeAlert();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();

        }
    };

    private void showChangeYesOrNo(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChangeOrderChooseBeauActivity.this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(ChangeOrderChooseBeauActivity.this, R.layout.alert_changeorder_yesorno, null);
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.show();
        TextView textview_change_beau_time = (TextView) view.findViewById(R.id.textview_change_beau_time);
        TextView textview_change_beau_name = (TextView) view.findViewById(R.id.textview_change_beau_name);
        TextView textview_change_notice = (TextView) view.findViewById(R.id.textview_change_notice);
        Button button_to_cancle = (Button) view.findViewById(R.id.button_to_cancle);
        Button button_to_change = (Button) view.findViewById(R.id.button_to_change);
        textview_change_beau_time.setText("服务时间：" + appointment);
        textview_change_beau_name.setText("美容师：" + beauName);
        if (!TextUtils.isEmpty(tip)) {
            textview_change_notice.setVisibility(View.VISIBLE);
            textview_change_notice.setText(tip);
        } else {
            textview_change_notice.setVisibility(View.GONE);
        }
        button_to_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        button_to_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                getChangeData();
            }
        });
    }
    private void showChangeAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChangeOrderChooseBeauActivity.this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(ChangeOrderChooseBeauActivity.this, R.layout.alert_changeorder_layout, null);
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.show();
        TextView tvContent = (TextView) view.findViewById(R.id.changeorder_content);
        TextView tvRules = (TextView) view.findViewById(R.id.tv_look_rules);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvContent.setText(changeContent);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finishWithAnimation();
            }
        });
        tvRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeOrderChooseBeauActivity.this,ADActivity.class);
                intent.putExtra("url",modifyOrderRuleUrl);
                startActivity(intent);
                alertDialog.dismiss();
                finishWithAnimation();
            }
        });
    }
    @OnClick({R.id.show_top, R.id.imgCloseChangeBeau, R.id.change_beau_left, R.id.change_beau_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.show_top:
                finish();
                break;
            case R.id.imgCloseChangeBeau:
                finish();
                break;
            case R.id.change_beau_left:
                layoutCheckOldBeauIsAppoint.setVisibility(View.GONE);
                tvTitle.setText("选择美容师");
                beauList.setVisibility(View.VISIBLE);
                changeBeauRight.setText("确认修改");
                textviewAppointTime.setVisibility(View.VISIBLE);
                textviewAppointTime.setText(appointment);
                changeBeauLeft.setVisibility(View.GONE);
                break;
            case R.id.change_beau_right:
                if (beauList.getVisibility()==View.GONE){//当前美容师展示情况下
                    workId = beauticianOld.id;
                    beauName = beauticianOld.name;
                    if (eqId<=-1){//当前美容师该时间不可约 选择该美容师其他时间
                        ChangeOrderNewActivity.act.getDataWithWorkId();
                        finish();
                    }else {//当前美容师该时间可约 点击确认修改 获取接送提示
                        getOrderCheck(appointment);//获取接送提示
                    }
                }else {
                    for (int i =0;i<allChangeBeau.size();i++){
                        Beautician beautician = allChangeBeau.get(i);
                        if (beautician.isChoose){
                            workId = beautician.id;
                            beauName = beautician.name;
                            break;
                        }
                    }
                    getOrderCheck(appointment);//获取接送提示
                }
                break;
        }
    }
}
