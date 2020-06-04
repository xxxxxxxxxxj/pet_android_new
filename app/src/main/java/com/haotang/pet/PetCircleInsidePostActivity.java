package com.haotang.pet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MarketDialogEvent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 宠圈发帖
 *
 * @author Administrator
 */
public class PetCircleInsidePostActivity extends SuperActivity implements OnClickListener {

    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    private TextView tv_titlebar_other;
    private EditText editText_input_content;
    private GridView gridView_photo;

    private PopupWindow pWin;
    private LayoutInflater mInflater;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CAMER = 2;
    File out;
    String path = "";
    ArrayList<String> list = new ArrayList<String>();
    List<Bitmap> imgList = new ArrayList<Bitmap>();
    MyAdapter ImgAdapter;
    private boolean isShowDelete = false;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    protected int pickIntent;
    private static final int REQUEST_PERMISSION_CODE = 101;
    /**
     * 广播接收器
     */
    private MyReceiver receiver;

    private TextView showtext;
    private TextView showtext2;
    private TextView showtext3;
    private int j = 0;
    private int groupId = 7;
    private List<File> listFile = new ArrayList<File>();
    File[] imgs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_circleinsidepost);
        MApplication.listAppoint.add(this);
        listFile.clear();
        getIntentData();
        initView();
        initReceiver();
        initListener();
        initGridView();
    }

    private void getIntentData() {
        // TODO Auto-generated method stub
        groupId = getIntent().getIntExtra("groupId", 7);
    }

    private void initReceiver() {
        // 广播事件**********************************************************************
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PetCircleInsidePostActivity");
        // 注册广播接收器
        registerReceiver(receiver, filter);

    }

    private void initListener() {
        editText_input_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                Utils.mLogError("==-->editText_input_content  " + s.length());
                if (s.length() == 200) {
                    ToastUtil.showToastShortCenter(mContext, "最多输入200字");
                } else {
                    showtext.setText(s.length() + "/200");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//				showtext.setText(s.toString());
                Utils.mLogError("==-->aaaaaaaaa:  " + s.toString());
                Utils.mLogError("==-->aaaaaaaaa    1111:  " + Utils.unicode2string(s.toString()));
                Utils.mLogError("==-->aaaaaaaaa    2222:  " + Utils.string2unicode(s.toString()));
            }
        });
//		setEmojiToTextView(showtext3);

//		showtext2.setText(Utils.unicode2string("\u1F60A\\uDE0A"));
//		showtext3.setText(Utils.string2unicode("\u1F60A\uDE0A"));
        int unicodeJoy = 0x1F60A;
        String emojiString = getEmojiStringByUnicode(unicodeJoy);
//        showtext3.setText(emojiString);  
//        \ud83d\ude0a
//        showtext2.setText("[emoji]\u1f609\ude09[/emoji]");
        showtext2.setText("\ud83d\ude0a");
        gridView_photo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position <= 9) {
                    if (position == imgList.size() && position != 9/* &&position<=2 */) {
                        showSelectDialog();
                    } else if (position == 9) {
                        Toast.makeText(mContext, "当前最多支持9张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(mContext, PetCirClePostImageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        Bitmap bitmap = imgList.get(position);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapByte = stream.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        bundle.putStringArrayList("list", list);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }
            }
        });
    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        editText_input_content = (EditText) findViewById(R.id.editText_input_content);
        gridView_photo = (GridView) findViewById(R.id.gridView_photo);
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
        showtext = (TextView) findViewById(R.id.showtext);
        showtext2 = (TextView) findViewById(R.id.showtext2);
        showtext3 = (TextView) findViewById(R.id.showtext3);
        tv_titlebar_title.setText("发帖子");
        tv_titlebar_other.setVisibility(View.VISIBLE);
        tv_titlebar_other.setText("发布");
        tv_titlebar_other.setTextColor(getResources().getColor(R.color.orange));
        ib_titlebar_back.setOnClickListener(this);
        tv_titlebar_other.setOnClickListener(this);
    }

    private void initGridView() {
        ImgAdapter = new MyAdapter();
        ImgAdapter.setIsShowDelete(isShowDelete);
        gridView_photo.setAdapter(ImgAdapter);
    }

    private void showSelectDialog() {
        // TODO pop
        try {
            goneJp();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pWin = null;
        if (pWin == null) {
            View view = mInflater.inflate(R.layout.dlg_choose_icon, null);
            LinearLayout pop_getIcon_action = (LinearLayout) view.findViewById(R.id.pop_getIcon_action);
            LinearLayout pop_getIcon_local = (LinearLayout) view.findViewById(R.id.pop_getIcon_local);
            LinearLayout pop_getIcon_cancle = (LinearLayout) view.findViewById(R.id.pop_getIcon_cancle);
            pWin = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pWin.setFocusable(true);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            pWin.setWidth(dm.widthPixels/* - 40*/);
            pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);

            //拍照
            pop_getIcon_action.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 1;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        }else {
                            goCamera();
                        }
                    } else {
                        goCamera();
                    }
                }
            });
            //本地获取图片
            pop_getIcon_local.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 2;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        }else {
                            goPick();
                        }
                    } else {
                        goPick();
                    }
                }
            });
            pop_getIcon_cancle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    pWin.dismiss();
                    pWin = null;
                }
            });

        }
    }

    private void goPick() {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .maxSelectable(9 - imgList.size())
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
        Uri mUri =null;
        if (Build.VERSION.SDK_INT >= 24) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(PetCircleInsidePostActivity.this, "com.haotang.pet.fileProvider", out);
        }else {
            mUri= Uri.fromFile(out);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, SELECT_CAMER);
        pWin.dismiss();
        pWin = null;
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
                        goCamera();
                    }else if (pickIntent==2){
                        goPick();
                    }
                } else {
                    Toast.makeText(mContext, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    private File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 这里可以修改为你的路径
            sdDir = new File(Environment.getExternalStorageDirectory()
                    + "/DCIM/Camera");

        }
        return sdDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_CAMER:
                    Bitmap bm1 = Utils.getxtsldraw(mContext, out.getAbsolutePath());
                    path = Utils.creatfile(mContext, bm1, "usermodify" + getCurrentTime());
//					Utils.mLogError("==-->path 拍照添加图片 := "+path);
                    list.add(path);
                    getLuban(path);
                    if (null != bm1 && !"".equals(bm1)) {
                        imgList.add(bm1);
                    }
                    ImgAdapter.notifyDataSetChanged();
                    break;
                case 100214:
                    List<String> arrayList = Matisse.obtainPathResult(data);
                    for (int i = 0; i < arrayList.size(); i++) {
                        Bitmap bm = Utils.getxtsldraw(mContext, arrayList.get(i));
                        list.add(arrayList.get(i));
                        getLuban(arrayList.get(i));
                        if (null != bm && !"".equals(bm)) {
                            imgList.add(bm);
                        }
                    }
                    ImgAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * 用于gridview显示多张照片
     *
     * @author wlc
     * @date 2015-4-16
     */
    public class MyAdapter extends BaseAdapter {

        private boolean isDelete;  //用于删除图标的显隐
        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            //需要额外多出一个用于添加图片
            return imgList.size() >= 9 ? imgList.size() : imgList.size() + 1;
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
            ImageView img_pic = (ImageView) convertView
                    .findViewById(R.id.img_pic);
            LinearLayout ly = (LinearLayout) convertView
                    .findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView
                    .findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView
                    .findViewById(R.id.img_delete);

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
                delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imgList.remove(position);
                        ImgAdapter.notifyDataSetChanged();

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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int index = bundle.getInt("index");
            if (index == 0) {
                int position = bundle.getInt("position");
                list.remove(position);
                imgList.remove(position);
                listFile.remove(position);
                ImgAdapter.notifyDataSetChanged();
            }
        }

    }

    private void setEmojiToTextView(TextView myTextView) {
        int unicodeJoy = 0x1F602;
        String emojiString = getEmojiStringByUnicode(unicodeJoy);
        myTextView.setText(emojiString);
    }

    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private AsyncHttpResponseHandler newPost = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("==-->发帖:" + new String(responseBody));
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    ToastUtil.showToastShortCenter(mContext, "发帖成功~");
                    Intent data = new Intent();
                    setResult(Global.RESULT_OK, data);
                    finishWithAnimation();
                    EventBus.getDefault().post(new MarketDialogEvent(MarketDialogEvent.PETHOUSEPOST));
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
            try {
                mPDialog.closeDialog();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.tv_titlebar_other:
                if (TextUtils.isEmpty(editText_input_content.getText())) {
                    ToastUtil.showToastShortCenter(mContext, "请填写您的帖子内容");
                } else {
                    mPDialog.showDialogAndText();
                    imgs = new File[list.size()];
                    for (int i = 0; i < listFile.size(); i++) {
                        imgs[i] = listFile.get(i);
                    }
                    CommUtil.newPost(spUtil.getString("cellphone", ""),
                            PetCircleInsidePostActivity.this,
                            groupId,
                            editText_input_content.getText().toString(),
                            imgs, null, null, 1, 0, false,0,0, newPost);

                }
                break;
        }
    }

    private void goneJp() {
        // 强制收起键盘
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
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
}
