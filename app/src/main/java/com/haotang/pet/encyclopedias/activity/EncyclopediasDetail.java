package com.haotang.pet.encyclopedias.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.haotang.base.SuperActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.BannerBathLoopAdapter;
import com.haotang.pet.encyclopedias.adapter.encyDetailEvaAdapter;
import com.haotang.pet.encyclopedias.bean.CommentBean;
import com.haotang.pet.encyclopedias.bean.EncyContentBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengShareUtils;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.SoftKeyBoardListener;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/8/2 0002.
 */

public class EncyclopediasDetail extends SuperActivity implements View.OnClickListener {
    private PullPushLayout pplLayout;
    private int alphaMax = 180;
    private Drawable bgBackDrawable;
    private Drawable bgBackBelowDrawable;
    private Drawable bgShareDrawable;
    private Drawable bgShareBelowDrawable;
    private Drawable bgNavBarDrawable;

    private ImageView service_back;
    private LinearLayout layout_service_back;
    private ImageView service_share;
    private RelativeLayout rlTitle;
    private ImageView service_back_blow;
    private ImageView service_share_below;

    private RelativeLayout video;
    private JZVideoPlayerStandard videoplayer_train;
    private RelativeLayout layout_video_header_top;
    public static TextView textview_video_title;
    private RelativeLayout banners;
    private RollPagerView rpv_servicedetail_pet;
    private TextView textView_title;
    private RelativeLayout rl_ppllayout_top;

    private TextView textview_ency_title;
    private ImageView img_ency_icon;
    private TextView textview_ency_name;
    private TextView textview_ency_time;
    private LinearLayout tag_ency_list;
    private LinearLayout layout_ency_content_and_img;
    private LinearLayout layout_title_top;
    private List<EncyContentBean> layouts = new ArrayList<>();

    private BannerBathLoopAdapter adapterBanner;
    private ArrayList<String> listBanner = new ArrayList<String>();
    private encyDetailEvaAdapter encyDetailEvaAdapter;
    private MListview my_eva_list;
    private int infoId;
    private ArrayList<CommentBean> commlists = new ArrayList<>();
    private TextView textview_good_nums;
    private ImageView img_good_img;
    private TextView textview_clo_nums;
    private ImageView img_clo_img;
    private TextView textview_eva_nums;
    private ImageView img_eva_img;
    private List<String> tags = new ArrayList<>();
    private List<EncyContentBean> encylists = new ArrayList<>();
    private ScrollView scrollview_out;
    private RelativeLayout layout_eva;
    private EditText editText_input_eva;
    private TextView edittext_length;
    private Button button_petcircle_git_eva;
    private EncyContentBean topEncyContent = null;
    private LinearLayout layout_all_eva;
    private LinearLayout layout_good, layout_clo, layout_to_eva;
    private TextView textview_eva_count;
    private int commentState = 0;
    private int isComment = 0;
    private int isCollection = 0;
    private int isThumbsUp = 0;
    private int isBlack = 0;
    private int wh[] = null;
    private int encyclopediaId = 0;
    private TextView textview_add_one;
    private LinearLayout layout_bottom_button;
    private PopupWindow pWin;
    private IWXAPI api;
    private UmengShareUtils umengShareUtils;
    private Bitmap bmp;
    private String shareImg = "http://p3.pstatp.com/large/pgc-image/15336225639106281241d89";
    private String shareUrl = "";
    private String shareTitle = "";
    private String shareDesc = "";
    private LayoutInflater mInflater;
    private Handler handlerView = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    textview_add_one.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encycloediasdetail);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        setView();
        initListener();


        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                layout_eva.setVisibility(View.VISIBLE);
                layout_bottom_button.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                layout_eva.setVisibility(View.GONE);
                layout_bottom_button.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(editText_input_eva.getText())) {
//                    ToastUtil.showToastShortBottom(mContext,"请输入评论内容");
                    return;
                }
                String content = editText_input_eva.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToastShortBottom(mContext, "请输入评论内容");
                    return;
                }
                PostEncyComment(content);
            }
        });
        editText_input_eva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edittext_length.setText("" + s.length() + "/120");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getIntentData() {
        infoId = getIntent().getIntExtra("infoId", 0);//测试默认写 1 后期修改
    }

    private void getInfoData() {
        mPDialog.showDialog();
        commlists.clear();
        listBanner.clear();
        CommUtil.encyclopediaInfo(mContext, infoId, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int code = jobj.getInt("code");
                if (code == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject object = jobj.getJSONObject("data");
                        if (object.has("id") && !object.isNull("id")) {
                            encyclopediaId = object.getInt("id");
                        }
                        if (object.has("topContent") && !object.isNull("topContent")) {
                            JSONObject objectTop = object.getJSONObject("topContent");
                            topEncyContent = new EncyContentBean();
                            int type = 0;
                            if (objectTop.has("type") && !objectTop.isNull("type")) {
                                type = objectTop.getInt("type");
                                topEncyContent.type = type;
                            }
                            if (type == 2) {
                                banners.setVisibility(View.VISIBLE);
                                video.setVisibility(View.GONE);
                                if (objectTop.has("content") && !objectTop.isNull("content")) {
                                    String content = objectTop.getString("content");
                                    listBanner.add(content + "");
                                    topEncyContent.content = content;
                                }

                                if (objectTop.has("point") && !objectTop.isNull("point")) {
                                    topEncyContent.point = objectTop.getInt("point");
                                }
                                if (objectTop.has("backup") && !objectTop.isNull("backup")) {
                                    topEncyContent.backup = objectTop.getString("backup");
                                }
                                if (listBanner.size() > 1) {
                                    rpv_servicedetail_pet.setHintView(new ColorPointHintView(mContext, Color.parseColor("#FE8A3F"), Color.parseColor("#FFE2D0")));
                                } else {
                                    rpv_servicedetail_pet.setHintView(null);
                                }
                                if (listBanner.size() > 0) {
                                    adapterBanner.setEncyBanner(topEncyContent);
                                    adapterBanner.notifyDataSetChanged();
                                }
                                int weight = 1;
                                int height = 1;
                                if (objectTop.has("weight") && !objectTop.isNull("weight")) {
                                    weight = objectTop.getInt("weight");
                                }
                                if (objectTop.has("height") && !objectTop.isNull("height")) {
                                    height = objectTop.getInt("height");
                                }
                                if (weight > 0 && height > 0) {
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, wh[0] * height / weight);
                                    rl_ppllayout_top.setLayoutParams(params);
                                }
                            } else if (type == 3) {
                                banners.setVisibility(View.GONE);
                                video.setVisibility(View.VISIBLE);
                                if (objectTop.has("content") && !objectTop.isNull("content")) {
                                    String content = objectTop.getString("content");
//                                    String content = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
                                    videoplayer_train.setUp(content, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
                                    videoplayer_train.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    if (objectTop.has("cover") && !objectTop.isNull("cover")) {
                                        String cover = objectTop.getString("cover");
                                        videoplayer_train.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                        GlideUtil.loadImg(mContext, cover, videoplayer_train.thumbImageView, 0);
                                    } else {
                                        videoplayer_train.thumbImageView.setImageBitmap(Utils.createVideoThumbnail(content, wh[0], wh[0]));
                                    }
                                    JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                                    JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                                }
                                if (objectTop.has("size") && !objectTop.isNull("size")) {
                                    double Size = objectTop.getDouble("size");
                                    double videoSize = Size / 1024 / 1024;
                                    if (videoSize < 1) {
                                        double videoKB = Size / 1024;
                                        videoplayer_train.setSizeVideo(Utils.formatDouble(videoKB, 2) + "KB");
                                    } else {
                                        videoplayer_train.setSizeVideo(Utils.formatDouble(videoSize, 2) + "M");
                                    }

                                }
                            }
                        } else {
                            video.setVisibility(View.GONE);
                            banners.setVisibility(View.GONE);
                            rl_ppllayout_top.setVisibility(View.GONE);
                        }

                        commlists.clear();
                        if (object.has("commentList") && !object.isNull("commentList")) {
                            JSONArray arrayEva = object.getJSONArray("commentList");
                            for (int i = 0; i < arrayEva.length(); i++) {
                                commlists.add(CommentBean.j2Entity(arrayEva.getJSONObject(i)));
                            }
                        }
                        if (commlists.size() > 0) {
                            layout_all_eva.setVisibility(View.VISIBLE);
                            my_eva_list.setVisibility(View.VISIBLE);
                            encyDetailEvaAdapter.notifyDataSetChanged();
                        } else {
                            layout_all_eva.setVisibility(View.GONE);
                            my_eva_list.setVisibility(View.GONE);
                        }
                        if (object.has("collectionNum") && !object.isNull("collectionNum")) {
                            double collectionNum = object.getDouble("collectionNum");
                            if (collectionNum < 10000) {
                                textview_clo_nums.setText(((int) collectionNum) + "");
                            } else {
                                textview_clo_nums.setText((Utils.formatDouble(collectionNum / 10000, 1)) + "W");
                            }
                        }
                        if (object.has("thumbsUpNum") && !object.isNull("thumbsUpNum")) {
                            double thumbsUpNum = object.getDouble("thumbsUpNum");
                            if (thumbsUpNum < 10000) {
                                textview_good_nums.setText((int) (thumbsUpNum) + "");
                            } else {
                                textview_good_nums.setText((Utils.formatDouble(thumbsUpNum / 10000, 1)) + "W");
                            }
                        }
                        if (object.has("commentNum") && !object.isNull("commentNum")) {
                            double commentNum = object.getDouble("commentNum");
                            textview_eva_count.setText("共" + (int) commentNum + "条评论 >");
                            if (commentNum < 10000) {
                                textview_eva_nums.setText((int) (commentNum) + "");
                            } else {
                                textview_eva_nums.setText((Utils.formatDouble(commentNum / 10000, 1)) + "W");
                            }

                        }

                        if (object.has("isComment") && !object.isNull("isComment")) {//当前用户是否评论过 1已评论
                            isComment = object.getInt("isComment");
                            if (isComment == 1) {
                                img_eva_img.setBackgroundResource(R.drawable.eva_check);
                            }
                        } else {
                            img_eva_img.setBackgroundResource(R.drawable.eva_un);
                        }
                        if (object.has("isCollection") && !object.isNull("isCollection")) {//当前用户是否收藏过 1已收藏
                            isCollection = object.getInt("isCollection");
                            if (isCollection == 1) {
                                img_clo_img.setBackgroundResource(R.drawable.clo_check);
                            } else {
                                img_clo_img.setBackgroundResource(R.drawable.clo_un);
                            }
                        } else {
                            isCollection = 0;
                            img_clo_img.setBackgroundResource(R.drawable.clo_un);
                        }
                        if (object.has("isThumbsUp") && !object.isNull("isThumbsUp")) {//当前用户是否点赞过 1已点赞
                            isThumbsUp = object.getInt("isThumbsUp");
                            if (isThumbsUp == 1) {
                                img_good_img.setBackgroundResource(R.drawable.good_checked);
                            }
                        } else {
                            img_good_img.setBackgroundResource(R.drawable.good);
                        }
                        if (object.has("isBlack") && !object.isNull("isBlack")) {//当前用户是否可评论是 1黑名单用户
                            isBlack = object.getInt("isBlack");
                        }
                        if (object.has("commentState") && !object.isNull("commentState")) {//评论状态 0:允许评论  1:禁止评论
                            commentState = object.getInt("commentState");
                        }
                        if (object.has("source") && !object.isNull("source")) {
                            textview_ency_name.setText(object.getString("source") + "");
                        }
                        if (object.has("title") && !object.isNull("title")) {
                            shareTitle = object.getString("title");
                            textview_ency_title.setText(shareTitle + "");
                        }
                        if (object.has("shareSubtitle") && !object.isNull("shareSubtitle")) {
                            shareDesc = object.getString("shareSubtitle");
                        }
                        if (object.has("releaseTimeStr") && !object.isNull("releaseTimeStr")) {
                            textview_ency_time.setText(object.getString("releaseTimeStr") + "");
                        }
                        if (object.has("sourceIcon") && !object.isNull("sourceIcon")) {
                            GlideUtil.loadCircleImg(mContext, object.getString("sourceIcon"), img_ency_icon, 0);
                        }


                        tags.clear();
                        if (object.has("labelList") && !object.isNull("labelList")) {
                            JSONArray array = object.getJSONArray("labelList");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject objectTag = array.getJSONObject(i);
                                    if (objectTag.has("name") && !objectTag.isNull("name")) {
                                        tags.add(objectTag.getString("name"));
                                    }
                                }
                            }
                        }
                        tag_ency_list.removeAllViews();
                        if (tags.size() > 0) {
                            TextView textView = new TextView(mContext);
                            textView.setTextColor(Color.parseColor("#BB996C"));
                            textView.setMaxLines(2);
                            textView.setEllipsize(TextUtils.TruncateAt.END);
                            StringBuilder sp = new StringBuilder();
                            for (int i = 0; i < tags.size(); i++) {
                                sp.append(tags.get(i) + "  ");
                            }
                            if (!TextUtils.isEmpty(sp)) {
                                textView.setText(sp.toString());
                                tag_ency_list.addView(textView);
                            }
                        }

                        if (object.has("infoShareUrl") && !object.isNull("infoShareUrl")) {
                            shareUrl = object.getString("infoShareUrl");
                        }
                        if (object.has("collectionCover") && !object.isNull("collectionCover")) {
                            shareImg = object.getString("collectionCover");
                        }
                        encylists.clear();
                        if (object.has("content") && !object.isNull("content")) {
                            JSONArray array = object.getJSONArray("content");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    encylists.add(EncyContentBean.j2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                        layout_ency_content_and_img.removeAllViews();
                        layouts.clear();
                        if (encylists.size() > 0) {
                            for (int i = 0; i < encylists.size(); i++) {
                                EncyContentBean encyContentBean = encylists.get(i);
                                View view = LayoutInflater.from(mContext).inflate(R.layout.item_ency_text_img, null);
                                if (encyContentBean.type == 2) {
                                    TextView textview_list_content = (TextView) view.findViewById(R.id.textview_list_content);
                                    textview_list_content.setVisibility(View.GONE);
                                    ImageView item_img_icon = (ImageView) view.findViewById(R.id.item_img_icon);
                                    if (encyContentBean.height > 0) {
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wh[0], wh[0] * encyContentBean.height / encyContentBean.width);
                                        params.topMargin = Utils.dip2px(mContext, 10);
                                        params.bottomMargin = Utils.dip2px(mContext, 10);
                                        item_img_icon.setLayoutParams(params);
                                    }
                                    item_img_icon.setVisibility(View.VISIBLE);
                                    GlideUtil.loadRoundImg(mContext, encyContentBean.content, item_img_icon, 0, 2);
                                    encyContentBean.imageView = item_img_icon;
                                    layouts.add(encyContentBean);
                                } else if (encyContentBean.type == 1) {
                                    ImageView item_img_icon = (ImageView) view.findViewById(R.id.item_img_icon);
                                    item_img_icon.setVisibility(View.GONE);
                                    TextView textview_list_content = (TextView) view.findViewById(R.id.textview_list_content);
                                    textview_list_content.setVisibility(View.VISIBLE);
                                    textview_list_content.setText(encyContentBean.content);
                                }
                                layout_ency_content_and_img.addView(view);
                            }
                        }
                        if (layouts.size() > 0) {
                            for (int i = 0; i < layouts.size(); i++) {
                                final EncyContentBean encyContentBean = layouts.get(i);
                                encyContentBean.imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.goService(mContext, encyContentBean.point, encyContentBean.backup);
                                    }
                                });
                            }
                        }

                        if (rl_ppllayout_top.getVisibility() == View.VISIBLE) {
                            layout_title_top.setVisibility(View.GONE);
//                            service_back_blow.setVisibility(View.VISIBLE);
//                            service_share_below.setVisibility(View.VISIBLE);
                            pplLayout.measure(0, 0);
                            Utils.mLogError("==--> " + pplLayout.getChildAt(0).getMeasuredHeight() + " wh[1] " + wh[1] + "  pplLayout.getMeasuredHeight()  " + pplLayout.getMeasuredHeight());
                            if (wh[1] + 600 <= pplLayout.getMeasuredHeight()) {
                                topStyleShow();
                            }
                        } else {
                            layout_title_top.setVisibility(View.VISIBLE);
                            textView_title.setVisibility(View.VISIBLE);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void initListener() {

    }

    private void setView() {
        textView_title.setVisibility(View.GONE);
        textView_title.setText("百科");

//		//动态设置顶部视频宽高;
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*9/16);
//		layout_video_header_top.setLayoutParams(params);
//		//动态设置顶部视频宽高;
//		videoplayer_train.ivThumb.setScaleType(ScaleType.FIT_XY);
//		videoplayer_train.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4", "测试banner",false,1);
//		ImageLoaderUtil.setImage(videoplayer_train.ivThumb, "http://192.168.0.252/static/orderpic/1479219649772_21832_0.jpg", R.drawable.home_dialog_img);
//		textview_video_title.setText("测试banner");

//        videoplayer_train.ivThumb.setScaleType(ImageView.ScaleType.FIT_XY);
//        videoplayer_train.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4", "测试banner",false,1);
//        ImageLoaderUtil.setImage(videoplayer_train.ivThumb, "http://192.168.0.252/static/orderpic/1479219649772_21832_0.jpg", R.drawable.home_dialog_img);


        encyDetailEvaAdapter = new encyDetailEvaAdapter(mContext, commlists);
        my_eva_list.setAdapter(encyDetailEvaAdapter);


    }

    private void initView() {
        api = WXAPIFactory.createWXAPI(this, Global.APP_ID);
        wh = Utils.getDisplayMetrics(mContext);
        mInflater = LayoutInflater.from(this);
        //外侧父控件
        pplLayout = (PullPushLayout) findViewById(R.id.ppl_layout);
        service_back = (ImageView) findViewById(R.id.service_back);
        layout_service_back = (LinearLayout) findViewById(R.id.layout_service_back);
        service_back_blow = (ImageView) findViewById(R.id.service_back_blow);
        service_share = (ImageView) findViewById(R.id.service_share);
        service_share_below = (ImageView) findViewById(R.id.service_share_below);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_servicedetail_title);
        //头部banner


        video = (RelativeLayout) findViewById(R.id.video);
        videoplayer_train = (JZVideoPlayerStandard) video.findViewById(R.id.videoplayer_train);
        textview_video_title = (TextView) video.findViewById(R.id.textview_video_title);
        layout_video_header_top = (RelativeLayout) video.findViewById(R.id.layout_video_header_top);
        banners = (RelativeLayout) findViewById(R.id.banners);
        rpv_servicedetail_pet = (RollPagerView) banners.findViewById(R.id.rpv_servicedetail_pet);
        rl_ppllayout_top = (RelativeLayout) findViewById(R.id.rl_ppllayout_top);
        textView_title = (TextView) findViewById(R.id.textView_title);
        my_eva_list = (MListview) findViewById(R.id.my_eva_list);
        textview_ency_title = (TextView) findViewById(R.id.textview_ency_title);
        textview_ency_name = (TextView) findViewById(R.id.textview_ency_name);
        textview_ency_time = (TextView) findViewById(R.id.textview_ency_time);
        layout_ency_content_and_img = (LinearLayout) findViewById(R.id.layout_ency_content_and_img);
        layout_title_top = (LinearLayout) findViewById(R.id.layout_title_top);
        img_good_img = (ImageView) findViewById(R.id.img_good_img);
        textview_good_nums = (TextView) findViewById(R.id.textview_good_nums);
        img_clo_img = (ImageView) findViewById(R.id.img_clo_img);
        textview_clo_nums = (TextView) findViewById(R.id.textview_clo_nums);
        img_eva_img = (ImageView) findViewById(R.id.img_eva_img);
        textview_eva_nums = (TextView) findViewById(R.id.textview_eva_nums);
        tag_ency_list = (LinearLayout) findViewById(R.id.tag_ency_list);
        editText_input_eva = (EditText) findViewById(R.id.editText_input_eva);
        edittext_length = (TextView) findViewById(R.id.edittext_length);
        button_petcircle_git_eva = (Button) findViewById(R.id.button_petcircle_git_eva);
//        scrollview_out = (ScrollView) findViewById(R.id.scrollview_out);
        layout_eva = (RelativeLayout) findViewById(R.id.layout_eva);
        img_ency_icon = (ImageView) findViewById(R.id.img_ency_icon);
        layout_all_eva = (LinearLayout) findViewById(R.id.layout_all_eva);
        layout_good = (LinearLayout) findViewById(R.id.layout_good);
        layout_clo = (LinearLayout) findViewById(R.id.layout_clo);
        layout_to_eva = (LinearLayout) findViewById(R.id.layout_to_eva);
        textview_eva_count = (TextView) findViewById(R.id.textview_eva_count);
        textview_add_one = (TextView) findViewById(R.id.textview_add_one);
        layout_bottom_button = (LinearLayout) findViewById(R.id.layout_bottom_button);


        layout_good.setOnClickListener(this);
        layout_clo.setOnClickListener(this);
        layout_to_eva.setOnClickListener(this);
        service_back.setOnClickListener(this);
        service_back_blow.setOnClickListener(this);
        service_share.setOnClickListener(this);
        service_share_below.setOnClickListener(this);
        img_good_img.setOnClickListener(this);
        img_clo_img.setOnClickListener(this);
        img_eva_img.setOnClickListener(this);
        button_petcircle_git_eva.setOnClickListener(this);
        layout_all_eva.setOnClickListener(this);

//        banners.setVisibility(View.GONE);
//        video.setVisibility(View.VISIBLE);
//        rl_ppllayout_top.setVisibility(View.GONE);

//        textview_ency_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext,EncyclopediasSearchActivity.class);
//                startActivity(intent);
//            }
//        });

//        String url ="http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/post/imgs/14775713250381416047.jpg";
//        listBanner.add(url);
//        listBanner.add(url);
//        listBanner.add(url);
//        listBanner.add(url);
//        listBanner.add(url);
//        listBanner.add(url);
        adapterBanner = new BannerBathLoopAdapter(mContext, rpv_servicedetail_pet, listBanner);
        rpv_servicedetail_pet.setAdapter(adapterBanner);


    }

    private void topStyleShow() {
        bgNavBarDrawable = rlTitle.getBackground();
        bgNavBarDrawable.setAlpha(0);
        pplLayout.setOnTouchEventMoveListenre(new PullPushLayout.OnTouchEventMoveListenre() {

            @Override
            public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
                // TODO Auto-generated method stub
//                rlTitle.setBackgroundColor(getResources().getColor(R.color.a3a3636));
            }

            @Override
            public void onSlideDwon(int mOriginalHeaderHeight, int mHeaderHeight) {
                // TODO Auto-generated method stub
//                rlTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onSlide(int alpha) {
                // TODO Auto-generated method stub
                int alphaReverse = alphaMax - alpha;
                if (alphaReverse < 0) {
                    alphaReverse = 0;
                    textView_title.setVisibility(View.VISIBLE);
                } else {
                    textView_title.setVisibility(View.GONE);
                }
//                bgBackDrawable.setAlpha(alphaReverse);
//                bgBackBelowDrawable.setAlpha(alpha);
//                bgShareDrawable.setAlpha(alphaReverse);
//                bgShareBelowDrawable.setAlpha(alpha);
                bgNavBarDrawable.setAlpha(alpha);
//				JCVideoPlayer.releaseAllVideos();
            }
        });

        bgBackDrawable = service_back.getBackground();
        bgBackBelowDrawable = service_back_blow.getBackground();
        bgShareDrawable = service_share.getBackground();
        bgShareBelowDrawable = service_share_below.getBackground();

//        bgBackDrawable.setAlpha(alphaMax);
//        bgShareDrawable.setAlpha(alphaMax);
        bgNavBarDrawable.setAlpha(0);
//        bgBackBelowDrawable.setAlpha(0);
//        bgShareBelowDrawable.setAlpha(0);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getInfoData();
        Utils.goneJP(mContext);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0] * 243 / 375);
//        rl_ppllayout_top.setLayoutParams(params);
        try {
            if (rl_ppllayout_top.getVisibility() == View.VISIBLE) {
                bgNavBarDrawable.setAlpha(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            JZVideoPlayer.releaseAllVideos();
            JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
            JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (rl_ppllayout_top.getVisibility() == View.VISIBLE) {
                bgNavBarDrawable.setAlpha(255);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0] * 243 / 375);
            rl_ppllayout_top.setLayoutParams(params);
        }
    }

    private void goNext(Class cls) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtra("infoId", infoId);
        startActivity(intent);
    }

    private void encyclopediaCollection() {
        mPDialog.showDialog();
        CommUtil.encyclopediaCollection(mContext, encyclopediaId, collectionHandler);
    }

    private AsyncHttpResponseHandler collectionHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    getInfoData();
                } else {
                    ToastUtil.showToastShortBottom(mContext, jsonObject.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void deleteUserCollection() {
        CommUtil.deleteUserCollection(mContext, encyclopediaId + "", deletehandler);
    }

    private AsyncHttpResponseHandler deletehandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    getInfoData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void encyclopediaThumbsUp() {
        mPDialog.showDialog();
        CommUtil.encyclopediaThumbsUp(mContext, encyclopediaId, thumbsUpHandler);
    }

    private AsyncHttpResponseHandler thumbsUpHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    getInfoData();
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

    private void PostEncyComment(String content) {
        mPDialog.showDialog();
        CommUtil.PostEncyComment(mContext, infoId, content, evaHandler);
    }

    private AsyncHttpResponseHandler evaHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    ToastUtil.showToastShortCenter(mContext, "发表评论成功");
                    getInfoData();
                    try {
                        if (!spUtil.getBoolean("GOTOMARKET_DIALOG_TRUE", false)) {//先判断是否点击跳转到应用市场
                            //再判断是否点击暂时没有
                            if (spUtil.getBoolean("GOTOMARKET_DIALOG_FALSE", false)) {
                                //再判断距离上一次点击暂时没有是否大于10天且判断打开次数是否是5次
                                String gotomarket_dialog_time = spUtil.getString("GOTOMARKET_DIALOG_TIME", "");
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                String format = df.format(new Date());// new Date()为获取当前系统时间
                                if (Utils.daysBetween(gotomarket_dialog_time, format) >= 10) {
                                    //弹出
                                    Utils.goMarketDialog(EncyclopediasDetail.this);
                                }
                            } else {
                                //弹出
                                Utils.goMarketDialog(EncyclopediasDetail.this);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private void encyclopediaShare(int shareType) {
        CommUtil.encyclopediaShare(mContext, encyclopediaId, shareType, shareHandler);
    }

    private AsyncHttpResponseHandler shareHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(shareImg);
            Message msg = new Message();
            msg.obj = returnBitmap;
            handlerShare.sendMessage(msg);
        }
    };
    Handler handlerShare = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bmp = (Bitmap) msg.obj;
            showShare();
        }
    };

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    private void setShareContent(int shareType) {
        encyclopediaShare(0);
        if (shareUrl.contains("?")) {
            shareUrl = shareUrl
                    + "&system="
                    + CommUtil.getSource()
                    + "_"
                    + Global.getCurrentVersion(mContext)
                    + "&imei="
                    + Global.getIMEI(mContext)
                    /*+ "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString("cellphone", "") */
                    + "&phoneModel="
                    + android.os.Build.BRAND + " " + android.os.Build.MODEL
                    + "&phoneSystemVersion=" + "Android "
                    + android.os.Build.VERSION.RELEASE + "&time="
                    + System.currentTimeMillis()
                    + "&infoId=" + encyclopediaId;
        } else {
            shareUrl = shareUrl
                    + "?system="
                    + CommUtil.getSource()
                    + "_"
                    + Global.getCurrentVersion(mContext)
                    + "&imei="
                    + Global.getIMEI(mContext)
                    /*+ "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString("cellphone", "")*/
                    + "&phoneModel="
                    + android.os.Build.BRAND + " " + android.os.Build.MODEL
                    + "&phoneSystemVersion=" + "Android "
                    + android.os.Build.VERSION.RELEASE + "&time="
                    + System.currentTimeMillis()
                    + "&infoId=" + encyclopediaId;
        }
        Log.e("TAG", "shareUrl = " + shareUrl);
        if (bmp != null && shareUrl != null && !TextUtils.isEmpty(shareUrl)) {
            WXWebpageObject wxwebpage = new WXWebpageObject();
            wxwebpage.webpageUrl = shareUrl;
            WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
            wxmedia.title = shareTitle;
            wxmedia.description = shareDesc;
            Bitmap createBitmapThumbnail = Utils.createBitmapThumbnail(bmp);
            wxmedia.setThumbImage(createBitmapThumbnail);
            wxmedia.thumbData = Util_WX.bmpToByteArray(
                    createBitmapThumbnail, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = wxmedia;
            if (shareType == 1) {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            }
            api.sendReq(req);
        }
    }

    private void showShare() {
        final View view = mInflater.inflate(R.layout.sharedialog, null);
        RelativeLayout rlParent = (RelativeLayout) view
                .findViewById(R.id.rl_sharedialog_parent);
        LinearLayout ll_sharedialog_wxfriend = (LinearLayout) view
                .findViewById(R.id.ll_sharedialog_wxfriend);
        LinearLayout ll_sharedialog_wxpyq = (LinearLayout) view
                .findViewById(R.id.ll_sharedialog_wxpyq);
        LinearLayout ll_sharedialog_qqfriend = (LinearLayout) view
                .findViewById(R.id.ll_sharedialog_qqfriend);
        LinearLayout ll_sharedialog_sina = (LinearLayout) view
                .findViewById(R.id.ll_sharedialog_sina);
        ll_sharedialog_sina.setVisibility(View.GONE);
        ll_sharedialog_qqfriend.setVisibility(View.GONE);
        Button btn_sharedialog_cancel = (Button) view
                .findViewById(R.id.btn_sharedialog_cancel);
        if (pWin == null) {
            pWin = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
        }
        pWin.setFocusable(true);
        pWin.setWidth(ScreenUtil.getScreenWidth(mContext));
        pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                pWin.dismiss();
                pWin = null;
            }
        });
        ll_sharedialog_wxfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 微信好友
                pWin.dismiss();
                pWin = null;
                if (Utils.isWeixinAvilible(mContext)) {
                    int shareType = 1;
                    setShareContent(shareType);
                } else {
                    ToastUtil.showToastShortBottom(mContext, "请先下载微信客户端");
                }
            }
        });
        ll_sharedialog_wxpyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 微信朋友圈
                pWin.dismiss();
                pWin = null;
                if (Utils.isWeixinAvilible(mContext)) {
                    int shareType = 2;
                    setShareContent(shareType);
                } else {
                    ToastUtil.showToastShortBottom(mContext, "请先下载微信客户端");
                }
            }
        });
        ll_sharedialog_qqfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// QQ好友
                pWin.dismiss();
                pWin = null;
                if (Utils.isQQClientAvailable(mContext)) {
                } else {
                    ToastUtil.showToastShortBottom(mContext, "请先下载QQ客户端");
                }
            }
        });
        ll_sharedialog_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 新浪微博
                pWin.dismiss();
                pWin = null;
//                setWXShareContent(4);
            }
        });
        btn_sharedialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {// 取消
                pWin.dismiss();
                pWin = null;
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.service_share_below:
            case R.id.service_share:
                new Thread(networkTask).start();
                break;
            case R.id.service_back_blow:
            case R.id.service_back:
                finish();
                break;
            case R.id.layout_good:
            case R.id.img_good_img:
                if (!Utils.checkLogin(mContext)) {
                    intent = new Intent(mContext, LoginNewActivity.class);
                    startActivity(intent);
                    return;
                }
                /*if (isBlack == 1){
                    ToastUtil.showToastShortBottom(mContext,"该文章评论已关闭");
                    return;
                }else */
                if (isThumbsUp == 1) {
                    ToastUtil.showToastShortBottom(mContext, "已点赞");
                    return;
                }
                Animation anim = new RotateAnimation(0f, -30f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
                anim.setFillAfter(true); // 设置保持动画最后的状态
                anim.setDuration(100); // 设置动画时间
                anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
                anim.setFillAfter(false);// 设置旋转后停止
                img_good_img.startAnimation(anim);
                textview_add_one.setVisibility(View.VISIBLE);

                Timer mTimer = new Timer();
                TimerTask mTimerTask = new TimerTask() {//创建一个线程来执行run方法中的代码
                    @Override
                    public void run() {
                        //要执行的代码

                        handlerView.sendEmptyMessage(0);
                    }
                };
                mTimer.schedule(mTimerTask, 100);//延迟3秒执行
                encyclopediaThumbsUp();
                break;
            case R.id.layout_clo:
            case R.id.img_clo_img:
                if (!Utils.checkLogin(mContext)) {
                    intent = new Intent(mContext, LoginNewActivity.class);
                    startActivity(intent);
                    return;
                }
                Animation animclo = new RotateAnimation(0f, -30f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
                animclo.setFillAfter(true); // 设置保持动画最后的状态
                animclo.setDuration(100); // 设置动画时间
                animclo.setInterpolator(new AccelerateInterpolator()); // 设置插入器
                animclo.setFillAfter(false);// 设置旋转后停止
                img_clo_img.startAnimation(animclo);
                if (isCollection == 1) {
                    deleteUserCollection();
                } else {
                    encyclopediaCollection();
                }
                break;
            case R.id.layout_to_eva:
            case R.id.img_eva_img:
                if (!Utils.checkLogin(mContext)) {
                    intent = new Intent(mContext, LoginNewActivity.class);
                    startActivity(intent);
                    return;
                }
                if (isBlack == 1) {
                    ToastUtil.showToastShortBottom(mContext, "该文章评论已关闭");
                    return;
                } else if (commentState == 1) {
                    ToastUtil.showToastShortBottom(mContext, "该文章评论已关闭");
                    return;
                }/*else if (isComment ==1){
                    ToastUtil.showToastShortBottom(mContext,"已评论");
                    return;
                }*/
                layout_eva.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    editText_input_eva.requestFocus();
                    imm.showSoftInput(editText_input_eva, 0);
                    editText_input_eva.setText("");
                }
                break;
            case R.id.button_petcircle_git_eva:
                if (TextUtils.isEmpty(editText_input_eva.getText())) {
                    ToastUtil.showToastShortBottom(mContext, "请输入评论内容");
                    return;
                }
                Utils.goneJP(mContext);
                layout_eva.setVisibility(View.GONE);
//                PostEncyComment(editText_input_eva.getText().toString());
                break;
            case R.id.layout_all_eva:
                goNext(EncyCommentActivity.class);
                break;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Utils.hideKeyboard(ev, editText_input_eva, EncyclopediasDetail.this);//调用方法判断是否需要隐藏键盘
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
