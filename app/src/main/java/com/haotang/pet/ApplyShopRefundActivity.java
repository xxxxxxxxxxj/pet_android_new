package com.haotang.pet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.haotang.pet.adapter.RefundResonAdapter;
import com.haotang.pet.adapter.ShopRefundAdapter;
import com.haotang.pet.entity.CancelReasonBean;
import com.haotang.pet.entity.MallOrderDetailGoodItems;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
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

public class ApplyShopRefundActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_shop_refundnum)
    TextView tvShopRefundnum;
    @BindView(R.id.rv_shop_refundlist)
    RecyclerView rvShopRefundlist;
    @BindView(R.id.iv_shop_chooseback)
    ImageView ivShopChooseback;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.iv_shop_choosechange)
    ImageView ivShopChoosechange;
    @BindView(R.id.gridView_get_dog_phone)
    GridView gridViewGetDogPhone;
    @BindView(R.id.tv_shop_refundcommit)
    TextView tvShopRefundcommit;
    @BindView(R.id.rl_shop_chooseback)
    RelativeLayout rlShopChooseback;
    @BindView(R.id.rl_shop_choosechage)
    RelativeLayout rlShopChoosechage;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.et_shoprefund_remark)
    EditText etShoprefundRemark;
    @BindView(R.id.rl_refund_reason)
    RelativeLayout rlRefundReason;
    @BindView(R.id.tv_shop_change)
    TextView tvShopChange;
    @BindView(R.id.tv_selected_reason)
    TextView tvSelectedReason;
    @BindView(R.id.tv_refund_toptip)
    TextView tvRefundTip;
    private int category = 1;//1退货  2换货
    private MyImageAdapter imageAdapter;
    String path = "";
    File out;
    private static final int SELECT_CAMER = 2;
    private static final int IMAGE_CERTIFICATION = 101;
    private PopupWindow pWin;
    private LayoutInflater mInflater;
    List<Bitmap> imgList = new ArrayList<Bitmap>();
    ArrayList<String> listStr = new ArrayList<String>();
    private List<File> listFile = new ArrayList<File>();
    private List<MallOrderDetailGoodItems> refundList = new ArrayList<>();
    private List<CancelReasonBean> reasonList = new ArrayList<>();
    private ShopRefundAdapter adapter;
    private int totalAmount = 0;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private RefundResonAdapter resonAdapter;
    private int pickIntent;
    File[] img = null;
    private int orderId;
    private String reason = "";
    private String mallRuleUrl;
    private String refundTip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initData();
        setView();
        setListener();
    }

    private void initData() {
        Intent intent = getIntent();
        refundList = (List<MallOrderDetailGoodItems>) intent.getSerializableExtra("goodList");
        orderId = intent.getIntExtra("orderId", 0);
        mallRuleUrl = intent.getStringExtra("mallRuleUrl");
        refundTip = intent.getStringExtra("refundTip");
        reasonList = (List<CancelReasonBean>)intent.getSerializableExtra("reasonList");
        /*reasonList.add(new CancelReasonBean(1, "aaaaaaa", false));
        reasonList.add(new CancelReasonBean(2, "bbbbbbb", false));
        reasonList.add(new CancelReasonBean(3, "ccccccc", false));*/

    }

    private void setListener() {
        gridViewGetDogPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == imgList.size() && i != 3/* &&position<=2 */) {
                    showSelectDialog();
                } else if (i == 4) {
                    Toast.makeText(mContext, "当前最多支持三张图片", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setView() {
        tvTitlebarTitle.setText("申请退货");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("退货规则");
        tvRefundTip.setText(refundTip);
        tvTitlebarOther.setTextColor(Color.parseColor("#ffffff"));
        imageAdapter = new MyImageAdapter();
        imageAdapter.setIsShowDelete(true);
        gridViewGetDogPhone.setAdapter(imageAdapter);
        rvShopRefundlist.setNestedScrollingEnabled(false);
        mInflater = LayoutInflater.from(mContext);
        adapter = new ShopRefundAdapter(mContext, refundList);
        resonAdapter = new RefundResonAdapter(reasonList, mContext);
        rvShopRefundlist.setLayoutManager(new LinearLayoutManager(mContext));
        rvShopRefundlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (refundList != null && refundList.size() != 0) {
            for (int i = 0; i < refundList.size(); i++) {
                totalAmount += refundList.get(i).getAmount();
            }
            tvShopRefundnum.setText("退货商品  共" + totalAmount + "件");
        }

    }

    private void findView() {
        setContentView(R.layout.activity_apply_shop_refund);
        ButterKnife.bind(this);
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
            rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(ApplyShopRefundActivity.this, R.anim.commodity_detail_show));//开始动画
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
                .maxSelectable(3 - imgList.size())
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
            mUri = FileProvider.getUriForFile(ApplyShopRefundActivity.this, "com.haotang.pet.fileProvider", out);
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


    /**
     * 用于gridview显示多张照片
     *
     * @author wlc
     * @date 2015-4-16
     */
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
            LinearLayout ly = (LinearLayout) convertView.findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView.findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView.findViewById(R.id.img_delete);

            //默认的添加图片的那个item是不需要显示删除图片的
            if (imgList.size() >= 1) {
                if (position <= imgList.size() - 1) {
                    ll_picparent.setVisibility(View.GONE);
                    img_pic.setVisibility(View.VISIBLE);
                    img_pic.setImageBitmap(imgList.get(position));
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

    private AsyncHttpResponseHandler shopRefundHanlder = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                String msg = object.getString("msg");
                if (code == 0) {
                    ToastUtil.showToastShortBottom(mContext, "操作成功");
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
            mPDialog.dimissDialog();
            ToastUtil.showToastShortBottom(mContext, "请求失败");
        }
    };

    private void showReasonPop() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(ApplyShopRefundActivity.this, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(ApplyShopRefundActivity.this, R.layout.pop_common_withbutton, null);
        TextView tvPopTitle = customView.findViewById(R.id.tv_commonpop_title);
        RecyclerView rvPopRecycler = customView.findViewById(R.id.rv_commonpop_list);
        Button btnSure = customView.findViewById(R.id.btn_commonpop_sure);
        RelativeLayout rvDemiss = customView.findViewById(R.id.rl_commonpop_dimiss);
        tvPopTitle.setText("请选择需要退货的原因：");
        rvPopRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        rvPopRecycler.setAdapter(resonAdapter);
        resonAdapter.notifyDataSetChanged();
        final PopupWindow pWinBottomDialog = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点 
        // 注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的 
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失 
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击 
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画 
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(ApplyShopRefundActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        resonAdapter.setOnChcekClickListener(new RefundResonAdapter.onChcekClickListener() {
            @Override
            public void onCheckClick(int position) {
                for (int i = 0; i < reasonList.size(); i++) {
                    if (i == position) {
                        reasonList.get(i).setChoose(true);
                    } else {
                        reasonList.get(i).setChoose(false);
                    }
                }
                resonAdapter.notifyDataSetChanged();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < reasonList.size(); i++) {
                    if (reasonList.get(i).isChoose()) {
                        reason = String.valueOf(reasonList.get(i).getId());
                        pWinBottomDialog.dismiss();
                    }
                }
            }
        });
        rvDemiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWinBottomDialog.dismiss();
            }
        });
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
                String reasonText = "";
                for (int i = 0; i < reasonList.size(); i++) {
                    if (reasonList.get(i).isChoose()) {
                        reason = String.valueOf(reasonList.get(i).getId());
                        reasonText = reasonList.get(i).getTxt();
                    }
                }
                tvSelectedReason.setText(reasonText);
            }
        });
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_shop_refundcommit, R.id.rl_shop_chooseback, R.id.rl_shop_choosechage, R.id.tv_titlebar_other, R.id.rl_refund_reason})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_shop_refundcommit:
                img = new File[listStr.size()];
                for (int i = 0; i < listFile.size(); i++) {
                    img[i] = listFile.get(i);
                }
                if ("".equals(reason)) {
                    ToastUtil.showToastShortBottom(mContext, "请选择原因");
                    showReasonPop();
                } else {
                    mPDialog.showDialog();
                    CommUtil.orderExamineApply(mContext, orderId, category, reason, etShoprefundRemark.getText().toString().trim(), img, shopRefundHanlder);
                }
                break;
            case R.id.rl_shop_chooseback:
                category = 1;
                ivShopChooseback.setImageResource(R.drawable.icon_round_selected);
                ivShopChoosechange.setImageResource(R.drawable.icon_round_unselect);
                break;
            case R.id.rl_shop_choosechage:
                category = 2;
                ivShopChooseback.setImageResource(R.drawable.icon_round_unselect);
                ivShopChoosechange.setImageResource(R.drawable.icon_round_selected);
                break;
            case R.id.tv_titlebar_other:
                Intent intent = new Intent(ApplyShopRefundActivity.this, ADActivity.class);
                intent.putExtra("url", mallRuleUrl);
                startActivity(intent);
                break;
            case R.id.rl_refund_reason:
                showReasonPop();
                break;

        }
    }


}
