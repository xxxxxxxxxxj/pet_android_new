package com.haotang.pet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.FluidLayout;
import com.haotang.pet.view.NiceImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FosterEvaluteActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    @BindView(R.id.iv_evalute_shopicon)
    NiceImageView ivEvaluteShopicon;
    @BindView(R.id.tv_evalute_shopname)
    TextView tvEvaluteShopname;
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
    @BindView(R.id.et_evalute_shopcontent)
    EditText etEvaluteShopcontent;
    @BindView(R.id.gridView_get_dog_phone_shop)
    GridView gridViewGetDogPhoneShop;
    @BindView(R.id.fluid_shop_layout)
    FluidLayout fluidShopLayout;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.btn_evalute_sure)
    Button btnEvaluteSure;
    private PopupWindow pWin;
    String path = "";
    File out;
    private static final int SELECT_CAMER = 2;
    private static final int IMAGE_CERTIFICATION = 101;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private LayoutInflater mInflater;
    private MyImageAdapter imageAdapter;
    private List<Bitmap> imgList = new ArrayList<Bitmap>();
    private ArrayList<String> listStr = new ArrayList<String>();
    private List<File> listFile = new ArrayList<File>();
    private int pickIntent;
    File[] img = null;
    private int gravity = Gravity.TOP;
    private ArrayList<CommentStar> commentBadShopStars = new ArrayList<CommentStar>();
    private ArrayList<CommentStar> commentCentreShopStars = new ArrayList<CommentStar>();
    private ArrayList<CommentStar> commentGoodShopStars = new ArrayList<CommentStar>();
    private int orderId;
    private int shopCredit;
    private int shopGrade;
    private StringBuilder spShop;
    private String spShopStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setListener();
        initData();
        getRatingBar();
    }

    private void setListener() {
        gridViewGetDogPhoneShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == imgList.size() && i != 4/* &&position<=2 */) {
                    showSelectDialog();
                } else if (i == 5) {
                    Toast.makeText(mContext, "当前最多支持四张图片", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRatingBar() {
        commentBadShopStars.clear();
        commentCentreShopStars.clear();
        commentGoodShopStars.clear();
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
                        if (objectData.has("defaultCommentCopy") && !objectData.isNull("defaultCommentCopy")) {
                            etEvaluteShopcontent.setHint(objectData.getString("defaultCommentCopy"));
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
                setBack(5);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void initData() {
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", 0);
        CommUtil.hotelOrderInfo(this, orderId, hotelOrderInfoHandler);
    }

    private AsyncHttpResponseHandler hotelOrderInfoHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("shop") && !jdata.isNull("shop")) {
                            JSONObject shop = jdata.getJSONObject("shop");
                            if (shop.has("hotelName") && !shop.isNull("hotelName")) {
                                tvEvaluteShopname.setText(shop.getString("hotelName"));
                            }
                            if (shop.has("img")&&!shop.isNull("img")){
                                GlideUtil.loadImg(mContext, shop.getString("img"), ivEvaluteShopicon, R.drawable.icon_production_default);
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void setView() {
        setContentView(R.layout.activity_foster_evalute);
        ButterKnife.bind(this);
        vTitleSlide.setVisibility(View.VISIBLE);
        tvTitlebarTitle.setText("评价");
        mInflater = LayoutInflater.from(mContext);
        imageAdapter = new MyImageAdapter();
        imageAdapter.setIsShowDelete(true);
        gridViewGetDogPhoneShop.setAdapter(imageAdapter);
    }

    private void showSelectDialog() {
        try {
            Utils.goneJP(mContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pWin = null;
        // TODO pop
        if (pWin == null) {
            rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(FosterEvaluteActivity.this, R.anim.commodity_detail_show));//开始动画
            rl_commodity_black.setVisibility(View.VISIBLE);
            rl_commodity_black.bringToFront();
            View view = mInflater.inflate(R.layout.dlg_choose_icon, null);
            LinearLayout pop_getIcon_action = (LinearLayout) view.findViewById(R.id.pop_getIcon_action);
            LinearLayout pop_getIcon_local = (LinearLayout) view.findViewById(R.id.pop_getIcon_local);
            LinearLayout pop_getIcon_cancle = (LinearLayout) view.findViewById(R.id.pop_getIcon_cancle);
            pWin = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pWin.setAnimationStyle(R.style.mypopwindow_anim_style);
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
                        } else {
                            goCamera();
                        }
                    } else {
                        goCamera();
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
                        } else {
                            goPick();
                        }
                    } else {
                        goPick();
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
            pWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rl_commodity_black.setVisibility(View.GONE);
                }
            });
        }
    }

    private void goPick() {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .maxSelectable(4 - imgList.size())
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .forResult(100214);
        pWin.dismiss();
        pWin = null;
    }

    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        String photoname = getCurrentTime() + "a.jpg";
        out = new File(getSDPath(), photoname);
//					list.add(out.getAbsolutePath());
        Uri mUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(FosterEvaluteActivity.this, "com.haotang.pet.fileProvider", out);
        } else {
            mUri = Uri.fromFile(out);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, SELECT_CAMER);
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
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean haveWritePermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return haveCameraPermission && haveWritePermission;

    }

    // 请求所需权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
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
                    if (pickIntent == 1) {
                        goCamera();
                    } else if (pickIntent == 2) {
                        goPick();
                    }
                } else {
                    Toast.makeText(mContext, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> arrayList = null;
            Bitmap bm1 = null;
            switch (requestCode) {
                //拍照添加图片
                case SELECT_CAMER:
                    bm1 = Utils.getxtsldraw(mContext, out.getAbsolutePath());
                    path = Utils.creatfile(mContext, bm1, "usermodify" + getCurrentTime());
                    listStr.add(path);
                    getLuban(path);
                    if (null != bm1 && !"".equals(bm1)) {
                        imgList.add(bm1);
                    }
                    imageAdapter.notifyDataSetChanged();
                    break;
                case 100214://选择照片
                    List<String> strList = Matisse.obtainPathResult(data);
                    for (int i = 0; i < strList.size(); i++) {
                        Bitmap bm = Utils.getxtsldraw(mContext, strList.get(i));
                        listStr.add(strList.get(i));
                        getLuban(strList.get(i));
                        if (null != bm && !"".equals(bm)) {
                            imgList.add(bm);
                        }
                    }
                    imageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void getLuban(String path) {
        Luban.get(this).load(new File(path)).putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {

                    @Override
                    public void onSuccess(File file) {
                        // TODO Auto-generated method stub
                        mPDialog.closeDialog();
                        listFile.add(file);
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

    public class MyImageAdapter extends BaseAdapter {

        private boolean isDelete;  //用于删除图标的显隐
        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            //需要额外多出一个用于添加图片
            return imgList.size() >= 4 ? imgList.size() : imgList.size() + 1;
        }

        @Override
        public Object getItem(int arg0) {
            return imgList.get(arg0);
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
            ImageView img_add = (ImageView) convertView.findViewById(R.id.iv_addimg);
            LinearLayout ly = (LinearLayout) convertView.findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView.findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView.findViewById(R.id.img_delete);
            img_add.setImageResource(R.drawable.icon_addpic);
            //默认的添加图片的那个item是不需要显示删除图片的
            if (imgList.size() >= 1) {
                if (position <= imgList.size() - 1) {
                    ll_picparent.setVisibility(View.GONE);
                    img_pic.setVisibility(View.VISIBLE);
                    img_pic.setImageBitmap(imgList.get(position));
                    // 设置删除按钮是否显示
                    delete.setImageResource(R.drawable.icon_delete_img);
                    delete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
                }
            }

            //当处于删除状态时，删除事件可用
            //注意：必须放到getView这个方法中，放到onitemClick中是不起作用的。
            if (isDelete) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgList.remove(position);
                        listStr.remove(position);
                        listFile.remove(position);
                        imageAdapter.notifyDataSetChanged();
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

    private void setBack(int num) {
        switch (num) {
            case 1:
                imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                imageviewShopEvaTwo.setImageResource(R.drawable.star_empty);
                imageviewShopEvaThr.setImageResource(R.drawable.star_empty);
                imageviewShopEvaFour.setImageResource(R.drawable.star_empty);
                imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                shopGrade = 1;
                shopCredit = 0;
                setTagList(commentBadShopStars);
                break;
            case 2:
                imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                imageviewShopEvaThr.setImageResource(R.drawable.star_empty);
                imageviewShopEvaFour.setImageResource(R.drawable.star_empty);
                imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                shopGrade = 2;
                shopCredit = 1;
                setTagList(commentCentreShopStars);
                break;
            case 3:
                imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                imageviewShopEvaThr.setImageResource(R.drawable.star_full);
                imageviewShopEvaFour.setImageResource(R.drawable.star_empty);
                imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                shopGrade = 3;
                shopCredit = 1;
                setTagList(commentCentreShopStars);
                break;
            case 4:
                imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                imageviewShopEvaThr.setImageResource(R.drawable.star_full);
                imageviewShopEvaFour.setImageResource(R.drawable.star_full);
                imageviewShopEvaFive.setImageResource(R.drawable.star_empty);
                shopGrade = 4;
                shopCredit = 1;
                setTagList(commentCentreShopStars);
                break;
            case 5:
                imageviewShopEvaOne.setImageResource(R.drawable.star_full);
                imageviewShopEvaTwo.setImageResource(R.drawable.star_full);
                imageviewShopEvaThr.setImageResource(R.drawable.star_full);
                imageviewShopEvaFour.setImageResource(R.drawable.star_full);
                imageviewShopEvaFive.setImageResource(R.drawable.star_full);
                shopGrade = 5;
                shopCredit = 2;
                setTagList(commentGoodShopStars);
                break;
        }
    }

    private void setTagList(final ArrayList<CommentStar> CommentStars) {
        fluidShopLayout.removeAllViews();
        fluidShopLayout.setGravity(gravity);
        for (int i = 0; i < CommentStars.size(); i++) {
            TextView tv = new TextView(this);
            if (CommentStars.get(i).ifChoose) {
                tv.setBackgroundResource(R.drawable.bg_evalutetag_selected);
                tv.setTextColor(Color.parseColor("#ffffff"));
            } else {
                tv.setBackgroundResource(R.drawable.bg_evalutetag_unselected);
                tv.setTextColor(Color.parseColor("#666666"));
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
                        view.setBackgroundResource(R.drawable.bg_evalutetag_unselected);
                        view.setTextColor(Color.parseColor("#333333"));
                    } else {
                        CommentStars.get(finalI).ifChoose = true;
                        view.setBackgroundResource(R.drawable.bg_evalutetag_selected);
                        view.setTextColor(Color.parseColor("#ffffff"));
                    }
                    getStrpBeauShop(CommentStars);
                }
            });
            fluidShopLayout.addView(tv, params);
        }
        getStrpBeauShop(CommentStars);

    }

    private void getStrpBeauShop(ArrayList<CommentStar> CommentStars) {
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
        try {
            spShopStr = spShop.toString();
            Utils.mLogError("==--> " + spShop.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AsyncHttpResponseHandler evaluteFosterHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (code == 0) {
                    EventBus.getDefault().post(new RefreshOrderEvent(true));
                    finish();
                } else {
                    ToastUtil.showToastShortBottom(mContext, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.imageview_shop_eva_one, R.id.imageview_shop_eva_two, R.id.imageview_shop_eva_thr, R.id.imageview_shop_eva_four, R.id.imageview_shop_eva_five, R.id.btn_evalute_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.imageview_shop_eva_one:
                setBack(1);
                break;
            case R.id.imageview_shop_eva_two:
                setBack(2);
                break;
            case R.id.imageview_shop_eva_thr:
                setBack(3);
                break;
            case R.id.imageview_shop_eva_four:
                setBack(4);
                break;
            case R.id.imageview_shop_eva_five:
                setBack(5);
                break;
            case R.id.btn_evalute_sure:
                if (TextUtils.isEmpty(etEvaluteShopcontent.getText().toString().trim())) {
                    ToastUtil.showToastShortBottom(mContext, "说点什么吧");
                } else {
                    img = new File[listStr.size()];
                    for (int i = 0; i < listFile.size(); i++) {
                        img[i] = listFile.get(i);
                    }
                    mPDialog.showDialog();
                    CommUtil.fosterEvaluate(mContext, orderId, 2, shopCredit, shopGrade, etEvaluteShopcontent.getText().toString().trim(), spShopStr, img, evaluteFosterHandler);
                }
                break;
        }
    }
}
