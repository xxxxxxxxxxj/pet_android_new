package com.haotang.pet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.soundcloud.android.crop.Crop;

import java.io.File;

/**
 * <p>
 * Title:ClipPictureActivity
 * </p>
 * <p>
 * Description:裁剪图片界面:裁剪框不动,图片可放大缩小
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-11-15 上午10:33:08
 */
public class ClipPictureActivity extends SuperActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final String EXTRA_FROM_ALBUM = "extra_from_album";
    private static final int REQUEST_CODE_TAKE_PHOTO = 100;
    private static final int REQUEST_CODE_SELECT_ALBUM = 200;
    boolean mFromAlbum;
    File tempFile;
    private File PetCrop;

    public static Intent getJumpIntent(Context context, boolean fromAlbum) {
        Intent intent = new Intent(context, ClipPictureActivity.class);
        intent.putExtra(EXTRA_FROM_ALBUM, fromAlbum);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_picture);
        PetCrop = new File(Environment.getExternalStorageDirectory(), "PetCrop");
        if (!PetCrop.exists()) {
            PetCrop.mkdirs();
        }
        mFromAlbum = getIntent().getBooleanExtra(EXTRA_FROM_ALBUM, true);
        tempFile = new File(getExternalFilesDir("img"), "temp.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
            if (!checkPermission()) { //没有或没有全部授权
                requestPermissions(); //请求权限
            } else {
                selectPhoto();
            }
        } else {
            selectPhoto();
        }
    }

    //检查权限
    private boolean checkPermission() {
        //是否有权限
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(ClipPictureActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean haveWritePermission = ContextCompat.checkSelfPermission(ClipPictureActivity.this,
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
                    selectPhoto();//开始拍照或从相册选取照片
                } else {
                    Log.e("TAG","该功能需要授权方可使用");
                    Toast.makeText(ClipPictureActivity.this, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void selectPhoto() {
        if (mFromAlbum) {
            Intent selectIntent = new Intent(Intent.ACTION_PICK);
            selectIntent.setType("image/*");
            if (selectIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(selectIntent, REQUEST_CODE_SELECT_ALBUM);
            }
        } else {
            Intent startCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri mUri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //步骤二：Android 7.0及以上获取文件 Uri
                mUri = FileProvider.getUriForFile(ClipPictureActivity.this, "com.haotang.pet.fileProvider", tempFile);
            } else {
                mUri = Uri.fromFile(tempFile);
            }
            startCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            if (startCameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(startCameraIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "requestCode = " + requestCode);
        Log.e("TAG", "resultCode = " + resultCode);
        if (resultCode != RESULT_OK) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && tempFile.exists()) {
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
            Crop.of(Uri.fromFile(tempFile), destination).asSquare().start(ClipPictureActivity.this);
        } else if (requestCode == REQUEST_CODE_SELECT_ALBUM && data != null && data.getData() != null) {
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
            Crop.of(data.getData(), destination).asSquare().start(ClipPictureActivity.this);
        } else if (requestCode == Crop.REQUEST_CROP) {
            Log.e("TAG", "裁剪resultCode = " + resultCode);
            Uri uri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            Intent intent = new Intent(mContext, SendSelectPostActivity.class);
            intent.putExtra("flag", "pic");
            intent.putExtra("picOutput", uri);
            startActivity(intent);
            finish();
        }
    }
}
