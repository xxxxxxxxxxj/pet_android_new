package com.haotang.pet.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.WXPayResultEvent;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends SuperActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtil = SharedPreferenceUtil.getInstance(this);
        api = WXAPIFactory.createWXAPI(this, Global.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("TAG", "resp = " + resp.toString());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && resp != null) {
            Global.WXPAYCODE = resp.errCode;
            EventBus.getDefault().post(new WXPayResultEvent(resp));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}