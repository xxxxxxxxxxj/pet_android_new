package com.haotang.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.haotang.pet.FlashActivity;
import com.haotang.pet.GuideActivity;
import com.haotang.pet.PasswordDialogActivity;
import com.haotang.pet.StartPageActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//import androidx.fragment.app.FragmentManager; androidx.fragment.app.FragmentManager;

public class BaseFragmentActivity extends AppCompatActivity {
    public Activity mContext;
    public SharedPreferenceUtil spUtil;
    public MProgressDialog mPDialog = null;
    private static final String TAG = "BaseActivity";
    private String backup;
    private int point;
    private String activityPic;
    protected boolean isWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isWindow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (ImageLoaderUtil.instance == null) {
                ImageLoaderUtil.instance = ImageLoader.getInstance();
            }
        }
        mContext = this;
        spUtil = SharedPreferenceUtil.getInstance(this);
        mPDialog = new MProgressDialog(mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                Log.w(TAG, "Activity result fragment index out of range: 0x"
                        + Integer.toHexString(requestCode));
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
                Log.w(TAG, "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
        try {
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
            Log.e("TAG", "activityName = " + activityName);
            Class class1 = Class.forName(activityName);
            Log.e("TAG", "class1 = " + class1.toString());
            if (!(class1 == StartPageActivity.class || class1 == FlashActivity.class || class1 == GuideActivity.class)) {
                String clipContent = Utils.getClipContent(this);
                Log.e("TAG", "清空前clipContent = " + clipContent);
                if (Utils.isStrNull(clipContent) && clipContent.contains("宠物家")) {
                    String[] split = clipContent.split("#");
                    if (split.length > 0) {
                        String str = split[split.length - 1];
                        Log.e("TAG", "str = " + str);
                        mPDialog.showDialog();
                        CommUtil.getShareActivityPage(mContext, str, getShareActivityPage);
                        Utils.clearClipboard(this);
                        String clipContent1 = Utils.getClipContent(this);
                        Log.e("TAG", "clipContent1 = " + clipContent1);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private AsyncHttpResponseHandler getShareActivityPage = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("backup") && !jdata.isNull("backup")) {
                            backup = jdata.getString("backup");
                        }
                        if (jdata.has("point") && !jdata.isNull("point")) {
                            point = jdata.getInt("point");
                        }
                        if (jdata.has("activityPic") && !jdata.isNull("activityPic")) {
                            activityPic = jdata.getString("activityPic");
                        }
                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(mContext,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
                ToastUtil.showToastShort(mContext, "数据异常");
            }
            if (Utils.isStrNull(activityPic)) {
                Intent intent = new Intent(mContext, PasswordDialogActivity.class);
                intent.putExtra("backup", backup);
                intent.putExtra("point", point);
                intent.putExtra("activityPic", activityPic);
                startActivity(intent);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShort(mContext, "请求失败");
        }
    };
}
