package com.haotang.pet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.NotificationsUtils;
import com.haotang.pet.util.QMUIDeviceHelper;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 权限设置界面
 */
public class SetPermissionsActivity extends SuperActivity {
    @BindView(R.id.tv_setperm_push_state)
    TextView tvSetpermPushState;
    @BindView(R.id.tv_setperm_location_state)
    TextView tvSetpermLocationState;
    @BindView(R.id.tv_setperm_camera_state)
    TextView tvSetpermCameraState;
    @BindView(R.id.tv_setperm_album_state)
    TextView tvSetpermAlbumState;
    @BindView(R.id.tv_setperm_book_state)
    TextView tvSetpermBookState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
    }

    private void findView() {
        setContentView(R.layout.activity_set_permissions);
        ButterKnife.bind(this);
    }

    private void setView() {

    }

    private void setLinster() {

    }

    private void getData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        boolean notificationEnabled = NotificationsUtils.isNotificationEnabled(mContext);
        if (notificationEnabled) {
            tvSetpermPushState.setText("已开启");
            tvSetpermPushState.setTextColor(ContextCompat.getColor(mContext, R.color.aaab7c1));
        } else {
            tvSetpermPushState.setText("去设置");
            tvSetpermPushState.setTextColor(ContextCompat.getColor(mContext, R.color.a080808));
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            tvSetpermLocationState.setText("已开启");
            tvSetpermLocationState.setTextColor(ContextCompat.getColor(mContext, R.color.aaab7c1));
        } else {
            tvSetpermLocationState.setText("去设置");
            tvSetpermLocationState.setTextColor(ContextCompat.getColor(mContext, R.color.a080808));
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            tvSetpermCameraState.setText("已开启");
            tvSetpermCameraState.setTextColor(ContextCompat.getColor(mContext, R.color.aaab7c1));
        } else {
            tvSetpermCameraState.setText("去设置");
            tvSetpermCameraState.setTextColor(ContextCompat.getColor(mContext, R.color.a080808));
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            tvSetpermAlbumState.setText("已开启");
            tvSetpermAlbumState.setTextColor(ContextCompat.getColor(mContext, R.color.aaab7c1));
        } else {
            tvSetpermAlbumState.setText("去设置");
            tvSetpermAlbumState.setTextColor(ContextCompat.getColor(mContext, R.color.a080808));
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            tvSetpermBookState.setText("已开启");
            tvSetpermBookState.setTextColor(ContextCompat.getColor(mContext, R.color.aaab7c1));
        } else {
            tvSetpermBookState.setText("去设置");
            tvSetpermBookState.setTextColor(ContextCompat.getColor(mContext, R.color.a080808));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.ib_setperm_back, R.id.rl_setperm_push, R.id.rl_setperm_location, R.id.rl_setperm_camera, R.id.rl_setperm_album, R.id.rl_setperm_book})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_setperm_back:
                finish();
                break;
            case R.id.rl_setperm_push:
                NotificationsUtils.goSetNotifiction(mContext);
                break;
            case R.id.rl_setperm_location:
            case R.id.rl_setperm_camera:
            case R.id.rl_setperm_album:
            case R.id.rl_setperm_book:
                QMUIDeviceHelper.goToPermissionManager(mContext);
                break;
        }
    }
}
