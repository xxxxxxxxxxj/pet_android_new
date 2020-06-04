package com.haotang.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.haotang.pet.FlashActivity;
import com.haotang.pet.GuideActivity;
import com.haotang.pet.PasswordDialogActivity;
import com.haotang.pet.R;
import com.haotang.pet.StartPageActivity;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.ActivityListManager;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.ui.SystemBarTintManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pet.baseapi.presenter.BasePresenter;
import com.pet.baseapi.presenter.IBaseUIView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class SuperActivity extends Activity implements IBaseUIView {
    public Activity mContext;
    protected int statusBarHeight;
    private View viewStatusBarTop;
    public SharedPreferenceUtil spUtil;
    public MProgressDialog mPDialog;
    protected ActivityListManager activityListManager = new ActivityListManager();
    private String backup;
    private int point;
    private String activityPic;
    protected SystemBarTintManager tintManager;
    protected Set<Disposable> requestDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (getPresenter() != null)
        {
            getPresenter().attach(this);
        }
        spUtil = SharedPreferenceUtil.getInstance(this);
        mContext = this;
        mPDialog = new MProgressDialog(mContext);
        mPDialog.setCanceledOnTouchOutside(false);
        if (ImageLoaderUtil.instance == null) {
            ImageLoaderUtil.instance = ImageLoader.getInstance();
        }
        tintManager = new SystemBarTintManager();
        tintManager.showStatusBar(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColorBar() {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        StatusBarLightMode();//设置状态栏白底黑字
    }

    /**
     *状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public int StatusBarLightMode(){
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(MIUISetStatusBarLightMode(this, true)){
                result=1;
            }else if(FlymeSetStatusBarLightMode(this.getWindow(), true)){
                result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result=3;
            }
            else//6.0以下版本
            {
                tintManager.setStatusBarTintResource(R.drawable.gradient_common_c5_c6);
                tintManager.setNavigationBarTintColor(Color.BLACK);
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     * @param activity
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window=activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if(dark){
                        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }catch (Exception e){

            }
        }
        return result;
    }

    protected View setEmptyViewBaseFoster(int flag, String msg, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview_foster, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg,String msgTwo, View.OnClickListener OnClickListener, @ColorInt int color,int topDp) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        TextView tv_emptyview_desctwo = emptyView.findViewById(R.id.tv_emptyview_desctow);
        LinearLayout ll_emptyview = emptyView.findViewById(R.id.ll_emptyview_img);
        ImageView ivEmpty = emptyView.findViewById(R.id.iv_emptyview_img);
        ll_emptyview.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivEmpty.getLayoutParams();
        layoutParams.topMargin = ScreenUtil.dip2px(this,topDp);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_emptyview_desctwo, msgTwo, "", View.GONE, View.VISIBLE);
        ll_emptyview.setBackgroundColor(color);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg,String msgTwo, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        TextView tv_emptyview_desctwo = emptyView.findViewById(R.id.tv_emptyview_desctow);
        LinearLayout ll_emptyview = emptyView.findViewById(R.id.ll_emptyview_img);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tv_emptyview_desctwo, msgTwo, "", View.GONE, View.VISIBLE);
        ll_emptyview.setBackgroundColor(Color.parseColor("#F8F8F8"));
        return emptyView;
    }
    protected View setEmptyViewBase(int flag, String msg, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg, int resId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg, int resId, int corlorId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        LinearLayout ll_emptyview_img = (LinearLayout) emptyView.findViewById(R.id.ll_emptyview_img);
        ll_emptyview_img.setBackgroundColor(getResources().getColor(corlorId));
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg, String btnMsg, int resId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setText(btnMsg);
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg, String btnMsg, int resId, int corlorId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        LinearLayout ll_emptyview_img = (LinearLayout) emptyView.findViewById(R.id.ll_emptyview_img);
        ll_emptyview_img.setBackgroundColor(getResources().getColor(corlorId));
        if (flag == 1) {
            btn_emptyview.setText(btnMsg);
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg, int resId, float width, float height, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_emptyview_img.getLayoutParams();
        params.height = DensityUtil.dp2px(this, height);
        params.width = DensityUtil.dp2px(this, width);
        return emptyView;
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
        if (mPDialog != null) {
            mPDialog.dimissDialog();
        }
    }

    public void finishWithAnimation() {

        SuperActivity.this.finish();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected Window setImmerseLayout() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        statusBarHeight = getStatusBarHeight(this.getBaseContext());
        return window;
    }

    public int getStatusBarHeight(Context context) {
        if (statusBarHeight != 0)
            return statusBarHeight;

        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(
                    resourceId);
        }
        return statusBarHeight;
    }

    public void setFullScreen(){
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setFlags(flag, flag);
    }

    protected void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }

    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPDialog != null) {
            mPDialog.dimissDialog();
            mPDialog = null;
        }
        cancelAllRequest();
        if (getPresenter() != null)
            getPresenter().detach();
    }

    @Override
    public void showLoading(Object... objects) {
        if (mPDialog != null) {
            if (objects != null && objects.length > 0){
                mPDialog.showDialog((String) objects[0]);
            }else {
                mPDialog.showDialog();
            }
        }
    }

    @Override
    public void hideLoading(Object... objects) {
        if (mPDialog != null) {
            mPDialog.dimissDialog();
        }
    }

    @Override
    public void showInterLoading(Object... objects) {

    }

    @Override
    public void hideIndexLoading(Object... objects) {

    }

    @Override
    public void showEmpty(Object... objects) {

    }

    @Override
    public void onSuccess(Object... objects) {

    }

    @Override
    public void onError(Object... objects) {

    }

    @Override
    public void tokenN0Avail() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void addDisposable(@NonNull Disposable d) {
        if (requestDisposable == null) {
            requestDisposable = new HashSet<>();
        }
        requestDisposable.add(d);
    }

    @Override
    public void removeDisposable(@NonNull Disposable d) {
        if (requestDisposable != null) {
            requestDisposable.remove(d);
        }
    }

    private void cancelAllRequest() {
        if (requestDisposable != null) {
            for (Disposable t : requestDisposable) {
                if (!t.isDisposed()) {
                    t.dispose();
                }
            }
            requestDisposable.clear();
        }
    }

    @Override
    public void noNetError(Object... objects) {

    }
}
