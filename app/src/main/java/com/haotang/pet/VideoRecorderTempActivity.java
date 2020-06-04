package com.haotang.pet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.haotang.base.SuperActivity;

public class VideoRecorderTempActivity extends SuperActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recorder_temp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
            if (!checkPermission()) { //没有或没有全部授权
                requestPermissions(); //请求权限
            } else {
                Intent intent = new Intent();
                intent.setClass(VideoRecorderTempActivity.this, VideoRecorderActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(VideoRecorderTempActivity.this, VideoRecorderActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //检查权限
    private boolean checkPermission() {
        //是否有权限
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(VideoRecorderTempActivity.this,
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean haveWritePermission = ContextCompat.checkSelfPermission(VideoRecorderTempActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean haveReadPermission = ContextCompat.checkSelfPermission(VideoRecorderTempActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean haveRecordAudioPermission = ContextCompat.checkSelfPermission(VideoRecorderTempActivity.this,
                android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        return haveCameraPermission && haveWritePermission && haveReadPermission && haveRecordAudioPermission;
    }

    // 请求所需权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
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
                    Intent intent = new Intent();
                    intent.setClass(VideoRecorderTempActivity.this, VideoRecorderActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("TAG", "该功能需要授权方可使用");
                    Toast.makeText(VideoRecorderTempActivity.this, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
