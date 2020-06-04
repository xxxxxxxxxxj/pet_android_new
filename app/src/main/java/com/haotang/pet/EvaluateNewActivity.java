package com.haotang.pet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.CommentStar;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.FluidLayout;
import com.haotang.pet.view.UserNameAlertDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/21 0021.
 */

public class EvaluateNewActivity extends SuperActivity {
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
    @BindView(R.id.beau_icon)
    ImageView beauIcon;
    @BindView(R.id.beau_name)
    TextView beauName;
    @BindView(R.id.beau_level)
    TextView beauLevel;
    @BindView(R.id.imageview_beau_eva_one)
    ImageView imageviewBeauEvaOne;
    @BindView(R.id.imageview_beau_eva_two)
    ImageView imageviewBeauEvaTwo;
    @BindView(R.id.imageview_beau_eva_thr)
    ImageView imageviewBeauEvaThr;
    @BindView(R.id.imageview_beau_eva_four)
    ImageView imageviewBeauEvaFour;
    @BindView(R.id.imageview_beau_eva_five)
    ImageView imageviewBeauEvaFive;
    @BindView(R.id.fluid_beau_layout)
    FluidLayout fluidBeauLayout;
    @BindView(R.id.editText_evalute_write_by_user)
    EditText editTextEvaluteWriteByUser;
    @BindView(R.id.gridView_get_dog_phone)
    GridView gridViewGetDogPhone;
    @BindView(R.id.shop_icon)
    ImageView shopIcon;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.imageview_shop_eva_one)
    ImageView imageviewShopEvaOne;
    @BindView(R.id.imageview_shop_eva_two)
    ImageView imageviewShopEvaTwo;
    @BindView(R.id.imageview_shop_eva_thr)
    ImageView imageviewShopEvaThr;
    @BindView(R.id.imageview_shop_eva_four)
    ImageView imageviewShopEvaFour;
    @BindView(R.id.imageview_shop_eva_five)
    ImageView imageviewShopEvaFive;
    @BindView(R.id.fluid_shop_layout)
    FluidLayout fluidShopLayout;
    @BindView(R.id.editText_shop_write_by_user)
    EditText editTextShopWriteByUser;
    @BindView(R.id.gridView_get_dog_phone_shop)
    GridView gridViewGetDogPhoneShop;
    @BindView(R.id.post_to_service_eva)
    Button postToServiceEva;
    @BindView(R.id.layout_is_anonymous)
    LinearLayout layout_is_anonymous;
    @BindView(R.id.is_anonymous)
    ImageView is_anonymous;
    @BindView(R.id.layoutBottomShop)
    LinearLayout layoutBottomShop;
    @BindView(R.id.layoutTopBeau)
    LinearLayout layoutTopBeau;
    private List<File> listBeauFile = new ArrayList<File>();
    private List<File> listShopFile = new ArrayList<File>();
    File[] picFile = null;
    File[] shopPicFile = null;
    List<Bitmap> imgBeauList = new ArrayList<Bitmap>();
    List<Bitmap> imgShopList = new ArrayList<Bitmap>();
    ArrayList<String> listBeau = new ArrayList<String>();
    ArrayList<String> listShop = new ArrayList<String>();
    private PopupWindow pWin;
    private LayoutInflater mInflater;
    MyBeauAdapter ImgBeauAdapter;
    MyShopAdapter ImgShopAdapter;
    private static final int SELECT_CAMER = 2;
    private static final int SELECT_CAMER_SHOP = 3;
    private static final int IMAGE_CERTIFICATION = 101;
    String path = "";
    File out;
    private MyReceiver receiver;
    private boolean isShowDelete = true;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    private ArrayList<CommentStar> commentBadBeauStars = new ArrayList<CommentStar>();
    private ArrayList<CommentStar> commentCentreBeauStars = new ArrayList<CommentStar>();
    private ArrayList<CommentStar> commentGoodBeauStars = new ArrayList<CommentStar>();
    private ArrayList<CommentStar> commentBadShopStars = new ArrayList<CommentStar>();
    private ArrayList<CommentStar> commentCentreShopStars = new ArrayList<CommentStar>();
    private ArrayList<CommentStar> commentGoodShopStars = new ArrayList<CommentStar>();
    private int beauEvaNums = 5;
    private int shopEvaNums = 5;
    private int gravity = Gravity.TOP;
    private StringBuilder spBeau = new StringBuilder();
    private StringBuilder spShop = new StringBuilder();
    private String spBeauStr = "";
    private String spShopStr = "";
    private String pic = "";
    private String shopPic = "";
    private String beauContent = "";
    private String shopContent = "";
    private int workid;
    private int orderId;
    private int type;
    private boolean isAnonymousUser;
    private boolean isNickName = false;
    private int isAnonymous;
    private String serviceName;
    private int serviceLoc;
    private int serviceType;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private int pickIntent;
    private boolean isBeau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_evalute_5_0_0);
        ButterKnife.bind(this);
        mInflater = LayoutInflater.from(mContext);
        getIntentData();
        setData();
        setView();
        initListener();
        initReceiver();
        getRatingBar();
        if (type == 1) {
            getOrderDetail();
        } else if (type == 2) {
            hotelOrderInfo();
            layoutTopBeau.setVisibility(View.GONE);
            layoutBottomShop.setVisibility(View.VISIBLE);
        }


    }

    private void setView() {
        tvTitlebarTitle.setText("评价");
    }

    private void getIntentData() {
        orderId = getIntent().getIntExtra("orderid", 0);
        type = getIntent().getIntExtra("type", 0);
    }

    private void hotelOrderInfo() {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
        CommUtil.hotelOrderInfo(this, orderId, hotelOrderInfo);
    }

    private AsyncHttpResponseHandler hotelOrderInfo = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("shop") && !object.isNull("shop")) {
                            JSONObject objectShop = object.getJSONObject("shop");
                            if (objectShop.has("shopName") && !objectShop.isNull("shopName")) {
                                shopName.setText(objectShop.getString("shopName"));
                            }
                            if (objectShop.has("img") && !objectShop.isNull("img")) {
                                String img = objectShop.getString("img");
                                GlideUtil.loadCircleImg(mContext, img, shopIcon, R.drawable.icon_default);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void getOrderDetail() {
        if (!mPDialog.isShowing()) {
            mPDialog.showDialog();
        }
        CommUtil.queryOrderDetailsNewTwo(this, orderId, getOrderDetailsNew);
    }

    private AsyncHttpResponseHandler getOrderDetails = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("petServicePojo") && !objectData.isNull("petServicePojo")) {
                            JSONObject objectPojo = objectData.getJSONObject("petServicePojo");
                            if (objectPojo.has("name") && !objectPojo.isNull("name")) {
                                serviceName = objectPojo.getString("name");
                            }
                        }
                        if (objectData.has("worker") && !objectData.isNull("worker")) {
                            JSONObject objectWorker = objectData.getJSONObject("worker");
                            if (objectWorker.has("id") && !objectWorker.isNull("id")) {
                                workid = objectWorker.getInt("id");
                            }
                            if (objectWorker.has("avatar") && !objectWorker.isNull("avatar")) {
                                String avatar = objectWorker.getString("avatar");
                                GlideUtil.loadCircleImg(mContext, avatar, beauIcon, R.drawable.icon_default);
                            }
                            if (objectWorker.has("realName") && !objectWorker.isNull("realName")) {
                                beauName.setText(objectWorker.getString("realName"));
                            }
                            if (objectWorker.has("level") && !objectWorker.isNull("level")) {
                                JSONObject objectLevel = objectWorker.getJSONObject("level");
                                if (objectLevel.has("name") && !objectLevel.isNull("name")) {
                                    beauLevel.setText(objectLevel.getString("name"));
                                }
                            }
                        }
                        if (objectData.has("shop") && !objectData.isNull("shop")) {
                            JSONObject objectShop = objectData.getJSONObject("shop");
                            if (objectShop.has("shopName") && !objectShop.isNull("shopName")) {
                                shopName.setText(objectShop.getString("shopName"));
                            }
                            if (objectShop.has("img") && !objectShop.isNull("img")) {
                                String img = objectShop.getString("img");
                                GlideUtil.loadCircleImg(mContext, img, shopIcon, R.drawable.icon_default);
                            }
                        }
                        if (objectData.has("serviceLoc") && !objectData.isNull("serviceLoc")) {
                            serviceLoc = objectData.getInt("serviceLoc");
                            if (serviceLoc == 1) {
                                layoutBottomShop.setVisibility(View.VISIBLE);
                                setBack(5, true);
                                setBack(5, false);
                            } else {
                                layoutBottomShop.setVisibility(View.GONE);
                                setBack(5, true);
                                setBack(0, false);
                            }
                        }
                        if (objectData.has("serviceType") && !objectData.isNull("serviceType")) {
                            serviceType = objectData.getInt("serviceType");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };


    private AsyncHttpResponseHandler getOrderDetailsNew = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("orders") && !objectData.isNull("orders")) {
                            JSONArray orderArray = objectData.getJSONArray("orders");
                            if (orderArray != null && orderArray.length() > 0) {
                                JSONObject orderObject = orderArray.getJSONObject(0);
                                if (orderObject.has("service") && !orderObject.isNull("service")) {
                                    JSONObject serviceObject = orderObject.getJSONObject("service");
                                    if (serviceObject.has("name") && !serviceObject.isNull("name")) {
                                        serviceName = serviceObject.getString("name");
                                    }
                                    if (serviceObject.has("serviceType") && !serviceObject.isNull("serviceType")) {
                                        serviceType = serviceObject.getInt("serviceType");
                                    }
                                }

                            }
                        }
                        if (objectData.has("worker") && !objectData.isNull("worker")) {
                            JSONObject objectWorker = objectData.getJSONObject("worker");
                            if (objectWorker.has("id") && !objectWorker.isNull("id")) {
                                workid = objectWorker.getInt("id");
                            }
                            if (objectWorker.has("avatar") && !objectWorker.isNull("avatar")) {
                                String avatar = objectWorker.getString("avatar");
                                GlideUtil.loadCircleImg(mContext, avatar, beauIcon, R.drawable.icon_default);
                            }
                            if (objectWorker.has("name") && !objectWorker.isNull("name")) {
                                beauName.setText(objectWorker.getString("name"));
                            }
                            if (objectWorker.has("level") && !objectWorker.isNull("level")) {
                                beauLevel.setText(objectWorker.getString("level"));
                            }
                        }
                        if (objectData.has("shop") && !objectData.isNull("shop")) {
                            JSONObject objectShop = objectData.getJSONObject("shop");
                            if (objectShop.has("name") && !objectShop.isNull("name")) {
                                shopName.setText(objectShop.getString("name"));
                            }
                            if (objectShop.has("img") && !objectShop.isNull("img")) {
                                String img = objectShop.getString("img");
                                GlideUtil.loadCircleImg(mContext, img, shopIcon, R.drawable.icon_default);
                            }
                        }
                        if (objectData.has("serviceLoc") && !objectData.isNull("serviceLoc")) {
                            serviceLoc = objectData.getInt("serviceLoc");
                            if (serviceLoc == 1) {
                                layoutBottomShop.setVisibility(View.VISIBLE);
                                setBack(5, true);
                                setBack(5, false);
                            } else {
                                layoutBottomShop.setVisibility(View.GONE);
                                setBack(5, true);
                                setBack(0, false);
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
            mPDialog.closeDialog();
        }
    };


    private void initData() {
        commentBadBeauStars.clear();
        commentCentreBeauStars.clear();
        commentGoodBeauStars.clear();
        commentBadShopStars.clear();
        commentCentreShopStars.clear();
        commentGoodShopStars.clear();
    }

    private void getRatingBar() {
        initData();
        CommUtil.commentStar(mContext, 0, orderId, ratingHandler);
    }

    private AsyncHttpResponseHandler ratingHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
//                        if (objectData.has("commentGradeCopy") && !objectData.isNull("commentGradeCopy")) {
//                            String commentGradeCopy = objectData.getString("commentGradeCopy");
//                        }
                        if (objectData.has("commentTag") && !objectData.isNull("commentTag")) {
                            JSONObject obj = objectData.getJSONObject("commentTag");
                            if (obj.has("good") && !obj.isNull("good")) {
                                JSONArray array = obj.getJSONArray("good");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        commentGoodBeauStars.add(CommentStar.json2Entity(array.getJSONObject(i)));
                                    }
                                }
                            }
                            if (obj.has("centre") && !obj.isNull("centre")) {
                                JSONArray array = obj.getJSONArray("centre");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        commentCentreBeauStars.add(CommentStar.json2Entity(array.getJSONObject(i)));
                                    }
                                }
                            }
                            if (obj.has("bad") && !obj.isNull("bad")) {
                                JSONArray array = obj.getJSONArray("bad");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        commentBadBeauStars.add(CommentStar.json2Entity(array.getJSONObject(i)));
                                    }
                                }
                            }
                        }
                        if (objectData.has("shopCommentTag") && !objectData.isNull("shopCommentTag")) {
                            JSONObject obj = objectData.getJSONObject("shopCommentTag");
                            if (obj.has("good") && !obj.isNull("good")) {
                                JSONArray array = obj.getJSONArray("good");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        commentGoodShopStars.add(CommentStar.json2Entity(array.getJSONObject(i)));
                                    }
                                }
                            }
                            if (obj.has("centre") && !obj.isNull("centre")) {
                                JSONArray array = obj.getJSONArray("centre");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        commentCentreShopStars.add(CommentStar.json2Entity(array.getJSONObject(i)));
                                    }
                                }
                            }
                            if (obj.has("bad") && !obj.isNull("bad")) {
                                JSONArray array = obj.getJSONArray("bad");
                                if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        commentBadShopStars.add(CommentStar.json2Entity(array.getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                }
                setBack(5, true);
                setBack(5, false);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void initReceiver() {
        // 广播事件**********************************************************************
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.EvaluateNewActivity");
        // 注册广播接收器
        registerReceiver(receiver, filter);
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int index = bundle.getInt("index");
            if (index == 0) {
                Utils.mLogError("==-->login 接收广播");
                int position = bundle.getInt("position");
                listBeau.remove(position);
                imgBeauList.remove(position);
                listBeauFile.remove(position);
                Utils.mLogError("==-->listev  1  --> " + listBeau.size());
                Utils.mLogError("==-->listev 2 --->  " + imgBeauList.size());
                ImgBeauAdapter.notifyDataSetChanged();
            } else if (index == 1) {
                Utils.mLogError("==-->login 接收广播");
                int position = bundle.getInt("position");
                listShop.remove(position);
                imgShopList.remove(position);
                listShopFile.remove(position);
                Utils.mLogError("==-->listev  1  --> " + listShop.size());
                Utils.mLogError("==-->listev 2 --->  " + imgShopList.size());
                ImgShopAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initListener() {
        gridViewGetDogPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= 4) {
                    if (position == imgBeauList.size() && position != 4/* &&position<=2 */) {
                        isBeau = true;
                        showSelectDialog(true);
                    } else if (position == 4) {
                        Toast.makeText(mContext, "当前最多支持三张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(mContext, ImgShow.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        Bitmap bitmap = imgBeauList.get(position);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapByte = stream.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        bundle.putStringArrayList("list", listBeau);
                        bundle.putInt("isbeau", 0);
                        bundle.putInt("isNewEva", 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
        gridViewGetDogPhoneShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= 4) {
                    if (position == imgShopList.size() && position != 4/* &&position<=2 */) {
                        isBeau = false;
                        showSelectDialog(false);
                    } else if (position == 4) {
                        Toast.makeText(mContext, "当前最多支持四张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(mContext, ImgShow.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        Bitmap bitmap = imgShopList.get(position);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapByte = stream.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        bundle.putStringArrayList("list", listShop);
                        bundle.putInt("isbeau", 1);
                        bundle.putInt("isNewEva", 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void setData() {
        ImgBeauAdapter = new MyBeauAdapter();
        ImgBeauAdapter.setIsShowDelete(isShowDelete);
        gridViewGetDogPhone.setAdapter(ImgBeauAdapter);
        ImgShopAdapter = new MyShopAdapter();
        ImgShopAdapter.setIsShowDelete(isShowDelete);
        gridViewGetDogPhoneShop.setAdapter(ImgShopAdapter);
    }

    private void showSelectDialog(final boolean isBeau) {
        try {
            Utils.goneJP(mContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pWin = null;
        // TODO pop
        if (pWin == null) {
            View view = mInflater.inflate(R.layout.dlg_choose_icon, null);
            LinearLayout pop_getIcon_action = (LinearLayout) view.findViewById(R.id.pop_getIcon_action);
            LinearLayout pop_getIcon_local = (LinearLayout) view.findViewById(R.id.pop_getIcon_local);
            LinearLayout pop_getIcon_cancle = (LinearLayout) view.findViewById(R.id.pop_getIcon_cancle);
            pWin = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pWin.setFocusable(true);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            pWin.setWidth(dm.widthPixels/* - 40*/);
            pWin.setOutsideTouchable(true);
            pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);

            //拍照
            pop_getIcon_action.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 1;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        }else {
                            goCamera(isBeau);
                        }
                    } else {
                        goCamera(isBeau);
                    }

                }
            });
            //本地获取图片
            pop_getIcon_local.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 2;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        }else {
                            goPick(isBeau);
                        }
                    } else {
                        goPick(isBeau);
                    }

                }
            });
            pop_getIcon_cancle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    pWin.dismiss();
                    pWin = null;
                }
            });

        }
    }

    private void goPick(boolean isBeau) {
        int maxSelectable = 0;
        int requestCode = 0;
        if (isBeau) {
            maxSelectable = 4 - imgBeauList.size();
            requestCode = 100214;
        } else {
            maxSelectable = 4 - imgShopList.size();
            requestCode = 100215;
        }
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .maxSelectable(maxSelectable)
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .forResult(requestCode);
        pWin.dismiss();
        pWin = null;
    }

    private void goCamera(boolean isBeau) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        String photoname = getCurrentTime() + "a.jpg";
        out = new File(getSDPath(), photoname);
//					list.add(out.getAbsolutePath());
        Uri mUri =null;
        if (Build.VERSION.SDK_INT >= 24) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(EvaluateNewActivity.this, "com.haotang.pet.fileProvider", out);
        }else {
            mUri= Uri.fromFile(out);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        if (isBeau) {
            startActivityForResult(intent, SELECT_CAMER);
        } else {
            startActivityForResult(intent, SELECT_CAMER_SHOP);
        }
        pWin.dismiss();
        pWin = null;
    }

    private File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 这里可以修改为你的路径
            sdDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
        }
        return sdDir;
    }

    public String getCurrentTime() {//避免特殊字符产生无法调起拍照后无法保存返回
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String currentTime = df.format(new Date());// new Date()为获取当前系统时间
        return currentTime;
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
                    if (pickIntent==1){
                        goCamera(isBeau);
                    }else if (pickIntent==2){
                        goPick(isBeau);
                    }
                } else {
                    Toast.makeText(mContext, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 用于gridview显示多张照片
     *
     * @author wlc
     * @date 2015-4-16
     */
    public class MyBeauAdapter extends BaseAdapter {

        private boolean isDelete;  //用于删除图标的显隐
        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            //需要额外多出一个用于添加图片
            return imgBeauList.size() >= 4 ? imgBeauList.size() : imgBeauList.size() + 1;
        }

        @Override
        public Object getItem(int arg0) {
            return imgBeauList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {

            //初始化页面和相关控件
            convertView = inflater.inflate(R.layout.item_imgview, null);
            ImageView img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
            LinearLayout ly = (LinearLayout) convertView.findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView.findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView.findViewById(R.id.img_delete);

            //默认的添加图片的那个item是不需要显示删除图片的
            if (imgBeauList.size() >= 1) {
                if (position <= imgBeauList.size() - 1) {
                    ll_picparent.setVisibility(View.GONE);
                    img_pic.setVisibility(View.VISIBLE);
                    img_pic.setImageBitmap(imgBeauList.get(position));
                    // 设置删除按钮是否显示
                    delete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
                }
            }

            //当处于删除状态时，删除事件可用
            //注意：必须放到getView这个方法中，放到onitemClick中是不起作用的。
            if (isDelete) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgBeauList.remove(position);
                        listBeau.remove(position);
                        listBeauFile.remove(position);
                        ImgBeauAdapter.notifyDataSetChanged();

                    }
                });
            }

            return convertView;
        }

        /**
         * 设置是否显示删除图片
         *
         * @param isShowDelete
         */
        public void setIsShowDelete(boolean isShowDelete) {
            this.isDelete = isShowDelete;
            notifyDataSetChanged();
        }

    }

    /**
     * 用于gridview显示多张照片
     *
     * @author wlc
     * @date 2015-4-16
     */
    public class MyShopAdapter extends BaseAdapter {

        private boolean isDelete;  //用于删除图标的显隐
        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            //需要额外多出一个用于添加图片
            return imgShopList.size() >= 4 ? imgShopList.size() : imgShopList.size() + 1;
        }

        @Override
        public Object getItem(int arg0) {
            return imgShopList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {

            //初始化页面和相关控件
            convertView = inflater.inflate(R.layout.item_imgview, null);
            ImageView img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
            LinearLayout ly = (LinearLayout) convertView.findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView.findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView.findViewById(R.id.img_delete);

            //默认的添加图片的那个item是不需要显示删除图片的
            if (imgShopList.size() >= 1) {
                if (position <= imgShopList.size() - 1) {
                    ll_picparent.setVisibility(View.GONE);
                    img_pic.setVisibility(View.VISIBLE);
                    img_pic.setImageBitmap(imgShopList.get(position));
                    // 设置删除按钮是否显示
                    delete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
                }
            }

            //当处于删除状态时，删除事件可用
            //注意：必须放到getView这个方法中，放到onitemClick中是不起作用的。
            if (isDelete) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgShopList.remove(position);
                        listShop.remove(position);
                        listShopFile.remove(position);
                        ImgShopAdapter.notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }

        /**
         * 设置是否显示删除图片
         *
         * @param isShowDelete
         */
        public void setIsShowDelete(boolean isShowDelete) {
            this.isDelete = isShowDelete;
            notifyDataSetChanged();
        }

    }

    private void setTagList(FluidLayout fluid_layout, final ArrayList<CommentStar> CommentStars, final boolean isbeau) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(gravity);
        for (int i = 0; i < CommentStars.size(); i++) {
            TextView tv = new TextView(this);
            if (CommentStars.get(i).ifChoose) {
                tv.setBackgroundResource(R.drawable.bg_left_right_banyuan_jianbian);
                tv.setTextColor(Color.parseColor("#ffffff"));
            } else {
                tv.setBackgroundResource(R.drawable.bg_left_right_banyuan_wuse);
                tv.setTextColor(Color.parseColor("#333333"));
            }

            tv.setText(CommentStars.get(i).tag);
            tv.setTextSize(13);
            tv.setPadding(60, 10, 60, 10);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 20, 20, 20);
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    if (CommentStars.get(finalI).ifChoose) {
                        CommentStars.get(finalI).ifChoose = false;
                        view.setBackgroundResource(R.drawable.bg_left_right_banyuan_wuse);
                        view.setTextColor(Color.parseColor("#333333"));
                    } else {
                        CommentStars.get(finalI).ifChoose = true;
                        view.setBackgroundResource(R.drawable.bg_left_right_banyuan_jianbian);
                        view.setTextColor(Color.parseColor("#ffffff"));
                    }
                    getStrpBeauShop(CommentStars, isbeau);
                }
            });
            fluid_layout.addView(tv, params);
        }
        getStrpBeauShop(CommentStars, isbeau);

    }

    private void getStrpBeauShop(ArrayList<CommentStar> CommentStars, boolean isbeau) {
        if (isbeau) {
            spBeau = new StringBuilder();
            ArrayList<CommentStar> CommentStarsChoose = new ArrayList<>();
            for (int i = 0; i < CommentStars.size(); i++) {
                CommentStar commentStar = CommentStars.get(i);
                if (commentStar.ifChoose) {
                    CommentStarsChoose.add(commentStar);
                }
            }
            if (CommentStarsChoose.size() > 0) {
                for (int i = 0; i < CommentStarsChoose.size(); i++) {
                    CommentStar commentStar = CommentStarsChoose.get(i);
                    if (commentStar.ifChoose) {
                        spBeau.append(commentStar.CommentTagId);
                        if (i != CommentStarsChoose.size() - 1) {
                            spBeau.append(",");
                        }
                    }
                }
            }
        } else {
            spShop = new StringBuilder();
            ArrayList<CommentStar> CommentStarsChoose = new ArrayList<>();
            for (int i = 0; i < CommentStars.size(); i++) {
                CommentStar commentStar = CommentStars.get(i);
                if (commentStar.ifChoose) {
                    CommentStarsChoose.add(commentStar);
                }
            }
            if (CommentStarsChoose.size() > 0) {
                for (int i = 0; i < CommentStarsChoose.size(); i++) {
                    CommentStar commentStar = CommentStarsChoose.get(i);
                    if (commentStar.ifChoose) {
                        spShop.append(commentStar.CommentTagId);
                        if (i != CommentStarsChoose.size() - 1) {
                            spShop.append(",");
                        }
                    }
                }
            }
        }
        try {
            spBeauStr = spBeau.toString();
            spShopStr = spShop.toString();
            Utils.mLogError("==--> " + spBeau.toString());
            Utils.mLogError("==--> " + spShop.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBack(int num, boolean isbeau) {
        switch (num) {
            case 1:
                if (isbeau) {
                    imageviewBeauEvaOne.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaTwo.setImageResource(R.drawable.star_empty);
                    imageviewBeauEvaThr.setImageResource(R.drawable.star_empty);
                    imageviewBeauEvaFour.setImageResource(R.drawable.star_empty);
                    imageviewBeauEvaFive.setImageResource(R.drawable.star_empty);
                    beauEvaNums = 1;
                    setTagList(fluidBeauLayout, commentBadBeauStars, isbeau);
                } else {
                    imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                    imageviewShopEvaTwo.setImageResource(R.drawable.star_empty);
                    imageviewShopEvaThr.setImageResource(R.drawable.star_empty);
                    imageviewShopEvaFour.setImageResource(R.drawable.star_empty);
                    imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                    shopEvaNums = 1;
                    setTagList(fluidShopLayout, commentBadShopStars, isbeau);
                }

                break;
            case 2:
                if (isbeau) {
                    imageviewBeauEvaOne.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaThr.setImageResource(R.drawable.star_empty);
                    imageviewBeauEvaFour.setImageResource(R.drawable.star_empty);
                    imageviewBeauEvaFive.setImageResource(R.drawable.star_empty);
                    beauEvaNums = 2;
                    setTagList(fluidBeauLayout, commentCentreBeauStars, isbeau);
                } else {
                    imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                    imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewShopEvaThr.setImageResource(R.drawable.star_empty);
                    imageviewShopEvaFour.setImageResource(R.drawable.star_empty);
                    imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                    shopEvaNums = 2;
                    setTagList(fluidShopLayout, commentCentreShopStars, isbeau);
                }
                break;
            case 3:
                if (isbeau) {
                    imageviewBeauEvaOne.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaThr.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaFour.setImageResource(R.drawable.star_empty);
                    imageviewBeauEvaFive.setImageResource(R.drawable.star_empty);
                    beauEvaNums = 3;
                    setTagList(fluidBeauLayout, commentCentreBeauStars, isbeau);
                } else {
                    imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                    imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewShopEvaThr.setImageResource(R.drawable.star_full);
                    imageviewShopEvaFour.setImageResource(R.drawable.star_empty);
                    imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                    shopEvaNums = 3;
                    setTagList(fluidShopLayout, commentCentreShopStars, isbeau);
                }
                break;
            case 4:
                if (isbeau) {
                    imageviewBeauEvaOne.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaThr.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaFour.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaFive.setImageResource(R.drawable.star_empty);
                    beauEvaNums = 4;
                    setTagList(fluidBeauLayout, commentCentreBeauStars, isbeau);
                } else {
                    imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                    imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewShopEvaThr.setImageResource(R.drawable.star_full);
                    imageviewShopEvaFour.setImageResource(R.drawable.star_full);
                    imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                    shopEvaNums = 4;
                    setTagList(fluidShopLayout, commentCentreShopStars, isbeau);
                }
                break;
            case 5:
                if (isbeau) {
                    imageviewBeauEvaOne.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaThr.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaFour.setImageResource(R.drawable.star_full);
                    imageviewBeauEvaFive.setImageResource(R.drawable.star_full);
                    beauEvaNums = 5;
                    setTagList(fluidBeauLayout, commentGoodBeauStars, isbeau);
                } else {
                    imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                    imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                    imageviewShopEvaThr.setImageResource(R.drawable.star_full);
                    imageviewShopEvaFour.setImageResource(R.drawable.star_full);
                    imageviewShopEvaFive.setImageResource(R.drawable.star_full);
                    shopEvaNums = 5;
                    setTagList(fluidShopLayout, commentGoodShopStars, isbeau);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void getLuban(String path, final boolean isBeau) {
        Luban.get(this).load(new File(path)).putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {

                    @Override
                    public void onSuccess(File file) {
                        // TODO Auto-generated method stub
                        mPDialog.closeDialog();
                        if (isBeau) {
                            listBeauFile.add(file);
                        } else {
                            listShopFile.add(file);
                        }
                    }

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        mPDialog.showDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    private AsyncHttpResponseHandler newPost = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("==-->发帖:" + new String(responseBody));
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
//                    Intent intentStatus = new Intent();
//                    intentStatus.setAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
//                    intentStatus.putExtra("index", 1);
//                    sendBroadcast(intentStatus);
//                    setResult(Global.RESULT_OK);
                    finishWithAnimation();
                } else {
                    finishWithAnimation();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                try {
                    mPDialog.closeDialog();
                    finishWithAnimation();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            try {
                mPDialog.closeDialog();
                finishWithAnimation();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    };

    private void EvaOverPostData() {
        final File[] imgs = new File[listBeau.size()];
        for (int i = 0; i < listBeau.size(); i++) {
            imgs[i] = new File(listBeau.get(i));
        }
        CommUtil.newPost(
                spUtil.getString("cellphone", ""),
                mContext, orderId, "【" + serviceName + "】" + beauContent, imgs, null, null, 1,
                1, isAnonymousUser,0,0,
                newPost);
    }

    private void commentOrderNew() {
        mPDialog.showDialog();
        picFile = new File[listBeauFile.size()];
        for (int i = 0; i < listBeauFile.size(); i++) {
            picFile[i] = listBeauFile.get(i);
        }
        shopPicFile = new File[listShop.size()];
        for (int i = 0; i < listShopFile.size(); i++) {
            shopPicFile[i] = listShopFile.get(i);
        }
        CommUtil.commentOrderNew(mContext, picFile, shopPicFile, beauEvaNums, shopEvaNums, spBeauStr, spShopStr, beauContent, shopContent, workid, orderId, isAnonymous, commentHandler);
    }

    private AsyncHttpResponseHandler commentHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    String commentGradeCopy = null;
                    String evaluate = null;
                    Boolean isGratuityOpen = null;
                    String gratuityContent = null;
                    int workerId = 0;
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject jsonObject = object.getJSONObject("data");
                        if (jsonObject.has("commentGradeCopy") && !jsonObject.isNull("commentGradeCopy")) {
                            commentGradeCopy = jsonObject.getString("commentGradeCopy");
                        }
                        if (jsonObject.has("evaluate") && !jsonObject.isNull("evaluate")) {
                            evaluate = jsonObject.getString("evaluate");
                        }
                        if (jsonObject.has("isCanGratuity")&&!jsonObject.isNull("isCanGratuity")){
                            isGratuityOpen = jsonObject.getBoolean("isCanGratuity");
                        }
                        if (jsonObject.has("gratuityContent")&&!jsonObject.isNull("gratuityContent")){
                            gratuityContent = jsonObject.getString("gratuityContent");
                        }
                        if (jsonObject.has("workerId")&&!jsonObject.isNull("workerId")){
                            workerId = jsonObject.getInt("workerId");
                        }
                    }
                    EventBus.getDefault().post(new RefreshOrderEvent(true));
                    setResult(RESULT_OK);
                    if (!isAnonymousUser) {//不是匿名 发宠圈
                        EvaOverPostData();//评价结束同时发帖
                    }
                    Intent intent = new Intent(mContext, EvaluateOverActivity.class);
                    intent.putExtra("commentGradeCopy", commentGradeCopy);
                    intent.putExtra("evaluate", evaluate);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("serviceType", serviceType);
                    Log.i("asdasdasd",isGratuityOpen+"------");
                    intent.putExtra("isGratuityOpen",isGratuityOpen);
                    intent.putExtra("gratuityContent",gratuityContent);
                    intent.putExtra("workerId",workerId);
                    intent.putExtra("beauEvaNums",beauEvaNums);
                    Log.e("TAG", "beauEvaNums = " + beauEvaNums + ",,,shopEvaNums = " + shopEvaNums);
                    if (beauEvaNums == 5 && shopEvaNums == 5) {
                        intent.putExtra("isMarketDialog", true);
                    }
                    startActivity(intent);
                    finishWithAnimation();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void tqDialog() {
        new UserNameAlertDialog(mContext).builder().setTitle("没昵称我  \"蓝瘦\"")
                .setTextViewHint("请填写昵称").setCloseButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setComplaintsButton("保	 存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mPDialog.showDialog();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Utils.mLogError("==-->点击了保存 0 cellphone:= " + spUtil.getString("cellphone", "0") + " userid:= " + spUtil.getInt("userid", 0) + " getUserName:= " + UserNameAlertDialog.getUserName());
                CommUtil.updateUser(spUtil.getString("cellphone", "0"),
                        Global.getIMEI(mContext), mContext,
                        spUtil.getInt("userid", 0),
                        UserNameAlertDialog.getUserName(), null,
                        updateUser);
            }
        }).show();
    }

    private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            // TODO Auto-generated method stub
            Utils.mLogError("==-->点击了保存 1");
            try {
                mPDialog.closeDialog();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    isNickName = true;
                    ToastUtil.showToastShortCenter(mContext, "创建成功");
                    spUtil.saveString("username", UserNameAlertDialog.getUserName());
                    try {
                        Utils.goneJP(mContext);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    String msg = jsonObject.getString("msg");
                    ToastUtil.showToastShortCenter(mContext, msg);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Utils.mLogError("==-->点击了保存 2 " + e.getMessage());
                try {
                    mPDialog.closeDialog();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            try {
                mPDialog.closeDialog();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<String> arrayList = null;
            Bitmap bm1 = null;
            switch (requestCode) {
                case IMAGE_CERTIFICATION:
                    if (data == null) {
                        ToastUtil.showToastShortCenter(mContext, "您选择的照片不存在，请重新选择");
                        return;
                    }
                    try {
                        Uri originalUri = data.getData(); //获得图片的uri
                        if (!TextUtils.isEmpty(originalUri.getAuthority())) {
                            // 这里开始的第二部分，获取图片的路径：
                            String[] proj = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(originalUri, proj, null, null, null);
                            //获得用户选择的图片的索引值
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            //最后根据索引值获取图片路径
                            path = cursor.getString(column_index);
                            listBeau.add(path);
                            getLuban(path, true);
                            cursor.close();
                        } else {
                            //小米系统走的方法
                            listBeau.add(originalUri.getPath());
                            getLuban(originalUri.getPath(), true);
                        }
                        ImgBeauAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    break;
                //拍照添加图片
                case SELECT_CAMER://美容师添加图片
                    bm1 = Utils.getxtsldraw(mContext, out.getAbsolutePath());
                    path = Utils.creatfile(mContext, bm1, "usermodify" + getCurrentTime());
                    listBeau.add(path);
                    getLuban(path, true);
                    if (null != bm1 && !"".equals(bm1)) {
                        imgBeauList.add(bm1);
                    }
                    ImgBeauAdapter.notifyDataSetChanged();
                    break;
                case SELECT_CAMER_SHOP://店铺添加图片
                    bm1 = Utils.getxtsldraw(mContext, out.getAbsolutePath());
                    path = Utils.creatfile(mContext, bm1, "usermodify" + getCurrentTime());
                    listShop.add(path);
                    getLuban(path, false);
                    if (null != bm1 && !"".equals(bm1)) {
                        imgShopList.add(bm1);
                    }
                    ImgShopAdapter.notifyDataSetChanged();
                    break;
                case 100214://美容师选择
                    arrayList = Matisse.obtainPathResult(data);
                    for (int i = 0; i < arrayList.size(); i++) {
                        Bitmap bm = Utils.getxtsldraw(mContext, arrayList.get(i));
                        listBeau.add(arrayList.get(i));
                        getLuban(arrayList.get(i), true);
                        if (null != bm && !"".equals(bm)) {
                            imgBeauList.add(bm);
                        }
                    }
                    ImgBeauAdapter.notifyDataSetChanged();
                    break;
                case 100215://店铺选择
                    arrayList = Matisse.obtainPathResult(data);
                    for (int i = 0; i < arrayList.size(); i++) {
                        Bitmap bm = Utils.getxtsldraw(mContext, arrayList.get(i));
                        listShop.add(arrayList.get(i));
                        getLuban(arrayList.get(i), false);
                        if (null != bm && !"".equals(bm)) {
                            imgShopList.add(bm);
                        }
                    }
                    ImgShopAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @OnClick({R.id.layout_is_anonymous, R.id.ib_titlebar_back, R.id.imageview_beau_eva_one, R.id.imageview_beau_eva_two, R.id.imageview_beau_eva_thr, R.id.imageview_beau_eva_four, R.id.imageview_beau_eva_five, R.id.imageview_shop_eva_one, R.id.imageview_shop_eva_two, R.id.imageview_shop_eva_thr, R.id.imageview_shop_eva_four, R.id.imageview_shop_eva_five, R.id.post_to_service_eva})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.imageview_beau_eva_one:
                setBack(1, true);
                break;
            case R.id.imageview_beau_eva_two:
                setBack(2, true);
                break;
            case R.id.imageview_beau_eva_thr:
                setBack(3, true);
                break;
            case R.id.imageview_beau_eva_four:
                setBack(4, true);
                break;
            case R.id.imageview_beau_eva_five:
                setBack(5, true);
                break;
            case R.id.imageview_shop_eva_one:
                setBack(1, false);
                break;
            case R.id.imageview_shop_eva_two:
                setBack(2, false);
                break;
            case R.id.imageview_shop_eva_thr:
                setBack(3, false);
                break;
            case R.id.imageview_shop_eva_four:
                setBack(4, false);
                break;
            case R.id.imageview_shop_eva_five:
                setBack(5, false);
                break;
            case R.id.layout_is_anonymous:
                if (!isAnonymousUser) {
                    isAnonymousUser = true;
                    isAnonymous = 1;
                    is_anonymous.setBackgroundResource(R.drawable.icon_pay_selected);
                } else {
                    isAnonymousUser = false;
                    isAnonymous = 0;
                    is_anonymous.setBackgroundResource(R.drawable.icon_pay_normal);
                }
                break;
            case R.id.post_to_service_eva:
                if (type == 1) {
                    if (TextUtils.isEmpty(spBeau) && TextUtils.isEmpty(editTextEvaluteWriteByUser.getText())) {
                        ToastUtil.showToastShortCenter(mContext, "请选择美容师评价或输入评价！");
                        return;
                    }
                    if (serviceLoc == 1) {
                        if (TextUtils.isEmpty(spShop) && TextUtils.isEmpty(editTextShopWriteByUser.getText())) {
                            ToastUtil.showToastShortCenter(mContext, "请选择店铺评价或输入评价！");
                            return;
                        }
                    }
                    String userName = spUtil.getString("username", "");
                    if (!isAnonymousUser) {
                        if (userName.equals("") || TextUtils.isEmpty(userName)) {
                            isNickName = false;
                            tqDialog();
                            return;
                        } else {
                            isNickName = true;
                        }
                    }
                    List<String> imgBeauStringList = new ArrayList<String>();
                    if (imgBeauList.size() > 0) {
                        for (int i = 0; i < imgBeauList.size(); i++) {
                            String bmpStr = Global.encodeWithBase64(imgBeauList.get(i));
                            imgBeauStringList.add(bmpStr);
                        }
                    }
                    List<String> imgShopStringList = new ArrayList<String>();
                    if (imgShopList.size() > 0) {
                        for (int i = 0; i < imgShopList.size(); i++) {
                            String bmpStr = Global.encodeWithBase64(imgShopList.get(i));
                            imgShopStringList.add(bmpStr);
                        }
                    }
                    StringBuilder spBeauPic = new StringBuilder();
                    if (imgBeauStringList.size() > 0) {
                        for (int i = 0; i < imgBeauStringList.size(); i++) {
                            spBeauPic.append(imgBeauStringList.get(i));
                            if (i != imgBeauStringList.size() - 1) {
                                spBeauPic.append(",");
                            }
                        }
                    }
                    StringBuilder spShopPic = new StringBuilder();
                    if (imgShopStringList.size() > 0) {
                        for (int i = 0; i < imgShopStringList.size(); i++) {
                            spShopPic.append(imgShopStringList.get(i));
                            if (i != imgShopStringList.size() - 1) {
                                spShopPic.append(",");
                            }
                        }
                    }
                    try {
                        pic = spBeauPic.toString();
                        shopPic = spShopPic.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(editTextEvaluteWriteByUser.getText())) {
                        beauContent = editTextEvaluteWriteByUser.getText().toString();
                    }
                    if (!TextUtils.isEmpty(editTextShopWriteByUser.getText())) {
                        shopContent = editTextShopWriteByUser.getText().toString();
                    }
                    commentOrderNew();
                } else if (type == 2) {
                    if (TextUtils.isEmpty(spShop) && TextUtils.isEmpty(editTextShopWriteByUser.getText())) {
                        ToastUtil.showToastShortCenter(mContext, "请选择店铺评价或输入评价！");
                        return;
                    }
                    String userName = spUtil.getString("username", "");
                    if (!isAnonymousUser) {
                        if (userName.equals("") || TextUtils.isEmpty(userName)) {
                            isNickName = false;
                            tqDialog();
                            return;
                        } else {
                            isNickName = true;
                        }
                    }
                    List<String> imgBeauStringList = new ArrayList<String>();
                    if (imgBeauList.size() > 0) {
                        for (int i = 0; i < imgBeauList.size(); i++) {
                            String bmpStr = Global.encodeWithBase64(imgBeauList.get(i));
                            imgBeauStringList.add(bmpStr);
                        }
                    }
                    List<String> imgShopStringList = new ArrayList<String>();
                    if (imgShopList.size() > 0) {
                        for (int i = 0; i < imgShopList.size(); i++) {
                            String bmpStr = Global.encodeWithBase64(imgShopList.get(i));
                            imgShopStringList.add(bmpStr);
                        }
                    }
                    StringBuilder spBeauPic = new StringBuilder();
                    if (imgBeauStringList.size() > 0) {
                        for (int i = 0; i < imgBeauStringList.size(); i++) {
                            spBeauPic.append(imgBeauStringList.get(i));
                            if (i != imgBeauStringList.size() - 1) {
                                spBeauPic.append(",");
                            }
                        }
                    }
                    StringBuilder spShopPic = new StringBuilder();
                    if (imgShopStringList.size() > 0) {
                        for (int i = 0; i < imgShopStringList.size(); i++) {
                            spShopPic.append(imgShopStringList.get(i));
                            if (i != imgShopStringList.size() - 1) {
                                spShopPic.append(",");
                            }
                        }
                    }
                    try {
                        pic = spBeauPic.toString();
                        shopPic = spShopPic.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(editTextEvaluteWriteByUser.getText())) {
                        beauContent = editTextEvaluteWriteByUser.getText().toString();
                    }
                    if (!TextUtils.isEmpty(editTextShopWriteByUser.getText())) {
                        shopContent = editTextShopWriteByUser.getText().toString();
                    }
                    commentOrderNew();
                }

                break;
        }
    }

}
