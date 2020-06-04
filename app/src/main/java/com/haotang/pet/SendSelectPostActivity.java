package com.haotang.pet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Constant;
import com.haotang.pet.util.FileSizeUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UnicodeToEmoji;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MarketDialogEvent;
import com.melink.bqmmsdk.sdk.BQMM;
import com.melink.bqmmsdk.ui.keyboard.IBQMMUnicodeEmojiProvider;
import com.melink.bqmmsdk.widget.BQMMEditView;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * <p>
 * Title:SendSelectPostActivity
 * </p>
 * <p>
 * Description:精选发帖界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-10-12 上午10:26:19
 */
public class SendSelectPostActivity extends Activity implements OnClickListener {
    private ImageView iv_sendselect_post;
    private RelativeLayout rl_sendselect_layout;
    private ImageView iv_sendselect_postplay_status;
    private BQMMEditView bqmv_sendselect_post;
    private String flag;
    private String mCoverPath;
    private String mVideoTempPath;
    private Button bt_titlebar_other;
    private TextView tv_titlebar_title;
    private ImageButton ib_titlebar_back;
    private SharedPreferenceUtil spUtil;
    private MProgressDialog pDialog;
    private File[] files = new File[1];
    private BQMM bqmmsdk;
    protected File mCoverPathFile;
    private TextView showtext;
    public MProgressDialog mPDialog;
    private Uri picOutput;
    private int imgWidth;
    private int imgHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPDialog = new MProgressDialog(this);
        mPDialog.setCanceledOnTouchOutside(false);
        prepareViews();
        prepareActivity();
    }

    /**
     * 预处理参数
     */
    private void prepareActivity() {
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        if (flag != null) {
            if (flag.equals("pic")) {
                picOutput = intent.getParcelableExtra("picOutput");
                Log.e("tag", "picOutput = " + picOutput);
                iv_sendselect_postplay_status.setVisibility(View.GONE);
                iv_sendselect_post.setImageURI(picOutput);
                String formatFileSize = FileSizeUtil
                        .formatFileSize(
                                new File(Utils.getPathByUri4kitkat(this, picOutput))
                                        .length(), false);
                Utils.mLogError("压缩前 = " + formatFileSize);
                compressWithLs(
                        new File(Utils.getPathByUri4kitkat(this, picOutput)), flag);
            } else if (flag.equals("video")) {
                iv_sendselect_postplay_status.setVisibility(View.VISIBLE);
                mVideoTempPath = getIntent().getStringExtra("output");
                String formatFileSize = FileSizeUtil
                        .formatFileSize(
                                new File(mVideoTempPath)
                                        .length(), false);
                Utils.mLogError("视频压缩前 = " + formatFileSize);
                Log.e("TAG", "mVideoTempPath = " + mVideoTempPath);
                Log.e("TAG", "Utils.getVideoThumb(mVideoTempPath) = " + Utils.getVideoThumb(mVideoTempPath));
                if (Utils.isStrNull(mVideoTempPath)) {
                    mCoverPath = Utils.bitmap2File(Utils.getVideoThumb(mVideoTempPath), mVideoTempPath.replace(".mp4", ".jpg"));
                    compressWithLs(new File(mCoverPath), flag);
                }
                Log.e("TAG", "mCoverPath = " + mCoverPath);
                iv_sendselect_post.setImageURI(Uri.parse(mCoverPath));
                //获取Options对象
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅做解码处理，不加载到内存
                options.inJustDecodeBounds = true;
                //解析文件
                BitmapFactory.decodeFile(mCoverPath, options);
                //获取宽高
                imgWidth = options.outWidth;
                imgHeight = options.outHeight;
                Log.e("TAG", "mCoverPath = " + mCoverPath);
                Log.e("TAG", "图片height = " + imgHeight);
                Log.e("TAG", "图片width = " + imgWidth);
            }
        }
    }

    /**
     * 压缩单张图片 Listener 方式
     *
     * @param flag
     */
    private void compressWithLs(File file, final String flag) {
        mPDialog.showDialog();
        Luban.get(this).load(file).putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        mPDialog.closeDialog();
                        String formatFileSize = FileSizeUtil.formatFileSize(
                                file.length(), false);
                        Utils.mLogError("压缩后 = " + formatFileSize);
                        if (flag.equals("pic")) {
                            files[0] = file;
                        } else if (flag.equals("video")) {
                            mCoverPathFile = file;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    /**
     * 预处理UI相关
     */
    private void prepareViews() {
        spUtil = SharedPreferenceUtil.getInstance(this);
        pDialog = new MProgressDialog(this);
        setContentView(R.layout.activity_send_select_post);
        iv_sendselect_post = (ImageView) findViewById(R.id.iv_sendselect_post);
        rl_sendselect_layout = (RelativeLayout) findViewById(R.id.rl_sendselect_layout);
        iv_sendselect_postplay_status = (ImageView) findViewById(R.id.iv_sendselect_postplay_status);
        bqmv_sendselect_post = (BQMMEditView) findViewById(R.id.bqmv_sendselect_post);
        bqmv_sendselect_post.requestFocus();
        bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        showtext = (TextView) findViewById(R.id.showtext);
        tv_titlebar_title.setText("发帖子");
        bt_titlebar_other.setText("发布");
        bt_titlebar_other.setTextColor(getResources().getColor(R.color.orange));
        rl_sendselect_layout.setOnClickListener(this);
        bt_titlebar_other.setOnClickListener(this);
        ib_titlebar_back.setOnClickListener(this);
        /**
         * BQMM集成 加载SDK
         */
        bqmmsdk = BQMM.getInstance();
        UnicodeToEmoji.initPhotos(this);
        bqmmsdk.setUnicodeEmojiProvider(new IBQMMUnicodeEmojiProvider() {
            @Override
            public Drawable getDrawableFromCodePoint(int i) {
                return UnicodeToEmoji.EmojiImageSpan.getEmojiDrawable(i);
            }
        });
        bqmv_sendselect_post.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Utils.mLogError("==-->editText_input_content  " + s.length());
                if (s.length() == 200) {
                    ToastUtil.showToastShortCenter(SendSelectPostActivity.this,
                            "最多输入200字");
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
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_titlebar_other:// 发帖
                if (flag != null) {
                    mPDialog.showDialogAndText();
                    String txt = bqmv_sendselect_post.getText().toString();
                    mPDialog.showDialogAndText();
                    if (flag.equals("pic")) {
                        CommUtil.newPost(spUtil.getString("cellphone", ""), this,
                                0, txt, files, null, null, 2, 0, false,0,0, newPostHandler);
                    } else if (flag.equals("video")) {
                        CommUtil.newPost(spUtil.getString("cellphone", ""), this,
                                0, txt, null, new File(mVideoTempPath),
                                mCoverPathFile, 2, 0, false, ScreenUtil.getScreenWidth(this),ScreenUtil.getScreenWidth(this), newPostHandler);
                    }
                }
                break;
            case R.id.ib_titlebar_back:// 返回
                finish();
                break;
            case R.id.rl_sendselect_layout:// 查看图片或者播放视频
                if (flag != null) {
                    if (flag.equals("pic")) {
                        String imgUrls[] = {picOutput.toString()};
                        Utils.imageBrower(SendSelectPostActivity.this, 0, imgUrls, true);
                    } else if (flag.equals("video")) {
                        startActivity(new Intent(this, VideoPlayerActivity.class)
                                .putExtra(Constant.RECORD_VIDEO_PATH,
                                        mVideoTempPath).putExtra("imgHeight", imgHeight).putExtra("imgWidth", imgWidth));
                    }
                }
                break;
            default:
                break;
        }
    }

    private AsyncHttpResponseHandler newPostHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            Utils.mLogError("发帖：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    spUtil.saveString(
                            "POSTSUCCESS_TO_POSTSELECTIONFRAGMENT_FLAG",
                            "POSTSUCCESS_TO_POSTSELECTIONFRAGMENT");
                    ToastUtil.showToastShortBottom(SendSelectPostActivity.this,
                            "发布成功");
                    finish();
                    EventBus.getDefault().post(new MarketDialogEvent(MarketDialogEvent.SELECTPOST));
                } else {
                    String msg = jobj.getString("msg");
                    if (msg != null && !TextUtils.isEmpty(msg)) {
                        ToastUtil.showToastShort(SendSelectPostActivity.this,
                                msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastShort(SendSelectPostActivity.this, "请求失败");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
