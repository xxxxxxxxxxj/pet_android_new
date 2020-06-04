
package com.haotang.pet.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.haotang.pet.entity.WXLoginEvent;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, Global.APP_ID, false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "分享成功";
			if (resp instanceof SendAuth.Resp) {//微信登录
				String code = ((SendAuth.Resp) resp).code;
				Utils.mLogError(code);
				WXLoginEvent wxLoginEvent = new WXLoginEvent(code);
				wxLoginEvent.setErrCode(resp.errCode);
				EventBus.getDefault().post(wxLoginEvent);
			}
			break;
			//用户取消授权
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			if (resp instanceof SendAuth.Resp) {//微信登录
				WXLoginEvent wxLoginEvent = new WXLoginEvent("");
				wxLoginEvent.setErrCode(resp.errCode);
				EventBus.getDefault().post(wxLoginEvent);
			}
			result = "分享取消";
			break;
			//用户拒绝授权
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			if (resp instanceof SendAuth.Resp) {//微信登录
				WXLoginEvent wxLoginEvent = new WXLoginEvent("");
				wxLoginEvent.setErrCode(resp.errCode);
				EventBus.getDefault().post(wxLoginEvent);
			}
			result = "分享被拒绝";
			break;
		default:
			result = "分享返回";
			break;
		}
		//分享提示
//		if(resp instanceof SendMessageToWX.Resp){
//			ToastUtil.showToastShortCenter(this, result);
//		}
		finish();
	}

	@Override
	public void onReq(BaseReq arg0) {

	}
}
