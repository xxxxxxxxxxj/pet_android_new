package com.haotang.pet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import cn.bingoogolapple.qrcode.core.QRCodeView;

import static master.flame.danmaku.ui.widget.DanmakuTextureView.TAG;

/**
 * <p>
 * Title:MipcaActivityCapture
 * </p>
 * <p>
 * Description:扫描二维码界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-11-22 下午2:26:33
 */
public class MipcaActivityCapture extends SuperActivity implements QRCodeView.Delegate,
		OnClickListener {
	private QRCodeView mQRCodeView;
	private Button bt_titlebar_other;
	private TextView tv_titlebar_title;
	private ImageButton ib_titlebar_back;
	private TextView tv_mipca_cancle;
	private static final int REQUEST_PERMISSION_CODE =101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mipca_activity_capture);
		mQRCodeView = (QRCodeView) findViewById(R.id.zxingview);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_mipca_cancle = (TextView) findViewById(R.id.tv_mipca_cancle);
		tv_titlebar_title.setText("扫一扫");
		bt_titlebar_other.setVisibility(View.GONE);
		ib_titlebar_back.setOnClickListener(this);
		tv_mipca_cancle.setOnClickListener(this);
		mQRCodeView.setDelegate(this);
		mQRCodeView.startSpot();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
			if (!checkPermission()) { //没有或没有全部授权
				requestPermissions(); //请求权限
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mQRCodeView.startCamera();
		mQRCodeView.showScanRect();
	}

	@Override
	protected void onStop() {
		mQRCodeView.stopCamera();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mQRCodeView.onDestroy();
		super.onDestroy();
	}

	private void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}

	@Override
	public void onScanQRCodeSuccess(String text) {
		Log.e("TAG", "result:" + text);
		vibrate();
		mQRCodeView.startSpot();
		if (text != null && !TextUtils.isEmpty(text)) {
			if (text.contains("app=cwjia")) {
				if (Utils.checkLogin(this)) {
					startActivity(new Intent(mContext, StoreSalesCashierActivity.class)
							.putExtra("codeResult", text).putExtra("previous",
									Global.MIPCA_TO_ORDERPAY));
				} else {
					startActivity(new Intent(mContext, LoginNewActivity.class)
							.putExtra("codeResult", text).putExtra("previous",
									Global.MIPCA_TO_ORDERPAY));
				}
			} else {
				ToastUtil.showToastShortCenter(this, "二维码不能乱扫，这个不是宠物家的哦~");
			}
		} else {
			ToastUtil.showToastShortCenter(this, "请扫描正确的二维码");
		}
		finish();
	}

	//检查权限
	private boolean checkPermission() {
		//是否有权限
		boolean haveCameraPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

		boolean haveWritePermission = ContextCompat.checkSelfPermission(mContext,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

		return haveCameraPermission && haveWritePermission;

	}

	// 请求所需权限
	@RequiresApi(api = Build.VERSION_CODES.M)
	private void requestPermissions() {
		requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
	}
	// 请求权限后会在这里回调
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_PERMISSION_CODE:

				boolean allowAllPermission = false;

				for (int i = 0; i < grantResults.length; i++) {
					if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//被拒绝授权
						allowAllPermission = false;
						break;
					}
					allowAllPermission = true;
				}

				if (allowAllPermission) {
					mQRCodeView.startCamera();
					mQRCodeView.showScanRect();
				} else {
					Toast.makeText(MipcaActivityCapture.this, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
					finish();
				}

				break;
		}
	}

	@Override
	public void onScanQRCodeOpenCameraError() {
		Log.e(TAG, "打开相机出错");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_mipca_cancle:
				finish();
				break;
			case R.id.ib_titlebar_back:
				finish();
				break;
			default:
				break;
		}
	}
}
