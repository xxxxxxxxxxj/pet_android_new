package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BeautyDetailPicAdapter;
import com.haotang.pet.adapter.BeautyServiceItemAdapter;
import com.haotang.pet.adapter.BeautylEvaAdapter;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.entity.ItemToMorePet;
import com.haotang.pet.entity.Production;
import com.haotang.pet.entity.SwitchService;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.image.BitmapUtil;
import com.haotang.pet.view.AutoPollRecyclerView;
import com.haotang.pet.view.MyScrollView;
import com.haotang.pet.view.NiceImageView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeauticianDetailActivity extends SuperActivity {

    @BindView(R.id.iv_beautydetail_back)
    ImageView ivBeautydetailBack;
    @BindView(R.id.tv_beautydetail_title)
    TextView tvBeautydetailTitle;
    @BindView(R.id.rv_beautydetail_one)
    AutoPollRecyclerView rvBeautydetailOne;
    @BindView(R.id.sv_beautydetail)
    MyScrollView svBeauty;
    @BindView(R.id.tv_beatydetail_productnum)
    TextView tvBeatydetailProductnum;
    @BindView(R.id.rl_beautydetail_checkproduct)
    RelativeLayout rlBeautydetailCheckproduct;
    @BindView(R.id.tv_beautydetail_name)
    TextView tvBeautydetailName;
    @BindView(R.id.iv_beautydetail_sex)
    ImageView ivBeautydetailSex;
    @BindView(R.id.tv_beautydetail_ordertime)
    TextView tvBeautydetailOrdertime;
    @BindView(R.id.nv_beautydetail_head)
    NiceImageView nvBeautydetailHead;
    @BindView(R.id.tv_beautydetail_servicenum)
    TextView tvBeautydetailServicenum;
    @BindView(R.id.tv_beautydetail_good)
    TextView tvBeautydetailGood;
    @BindView(R.id.ll_beautydetail_tags)
    LinearLayout llBeautydetailTags;
    @BindView(R.id.rv_beautydetail_eva)
    RecyclerView rvBeautydetailEva;
    @BindView(R.id.rv_beautydetail_service)
    RecyclerView rvBeautydetailService;
    @BindView(R.id.tv_beautydetail_commonnum)
    TextView tvBeautydetailCommonnum;
    @BindView(R.id.ll_beautydetail_gobottom)
    LinearLayout llGoBottom;
    @BindView(R.id.tv_beauty_noproduct)
    TextView tvNoProudct;
    @BindView(R.id.iv_beauty_level)
    ImageView ivLevel;
    @BindView(R.id.tv_beautydetail_order)
    TextView tvBeautyDetailOrder;
    @BindView(R.id.tv_beautydetail_name2)
    TextView tvBeautydetailName2;
    @BindView(R.id.iv_beautydetail_sex2)
    ImageView ivBeautydetailSex2;
    @BindView(R.id.tv_beautydetail_ordertime2)
    TextView tvBeautydetailOrdertime2;
    @BindView(R.id.nv_beautydetail_head2)
    NiceImageView nvBeautydetailHead2;
    @BindView(R.id.rl_beautydetail_header2)
    RelativeLayout rlBeautydetailHeader2;
    @BindView(R.id.iv_beauty_share)
    ImageView ivBeautyShare;
    @BindView(R.id.tv_beautydetail_numtip)
    TextView tvBeautydetailNumtip;
    @BindView(R.id.iv_beautydetail_right)
    ImageView ivBeautydetailRight;
    @BindView(R.id.rl_beautydetail_header)
    RelativeLayout rlBeautydetailHeader;
    @BindView(R.id.tv_beautydetail_servicenumtip)
    TextView tvBeautydetailServicenumtip;
    @BindView(R.id.tv_beautydetail_goodtip)
    TextView tvBeautydetailGoodtip;
    @BindView(R.id.iv_beautydetail_redright)
    ImageView ivBeautydetailRedright;
    @BindView(R.id.iv_beautydetail_alleva)
    ImageView ivBeautydetailAlleva;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bar)
    View bar;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.root_rl)
    RelativeLayout rootRl;
    @BindView(R.id.rl_root_evaluate)
    RelativeLayout rlRootEvaluate;
    @BindView(R.id.tv_foster_shopdesc)
    ExpandableTextView tvFosterShopdesc;
    private int pageSize = 2;
    private int mNextRequestPage = 1;
    private int FromAreaid = 0;
    public static BeauticianDetailActivity act;
    private String apptime;
    private int shopId;

    private Intent fIntent;
    private boolean isVip;
    private List<ApointMentPet> petList;
    private ItemToMorePet itemToMorePet;
    private String areaName;
    private int beautician_id;
    private int isAvail;
    private int previous;
    private String beautician_iamge;
    private String shareImg ="http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/html5/15887603970299285780.png";
    private int beautician_ordernum;
    private ArrayList<Comment> evalists = new ArrayList<>();
    private BeautylEvaAdapter beautylEvaAdapter;
    private BeautyServiceItemAdapter beautyServiceItemAdapter;
    private ArrayList<SwitchService> serviceList = new ArrayList<>();
    public static ArrayList<Production> productionList = new ArrayList<Production>();
    private ArrayList<String> imgList = new ArrayList<>();
    private List<String> workPic = new ArrayList<>();
    private BeautyDetailPicAdapter beautyDetailPicAdapter;
    private int tid;
    private String beautician_name;
    private Beautician beautician;
    private String shareUrl;
    private String shareTitle;
    private String shareDesc;
    private int[] screenLocaiton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        initWindows();
        initData();
        getData(beautician_id, FromAreaid);
        getCommentByBeauticianId();
        if (previous == Global.AVAILABLEWORKERLIST_TO_WORKERDETAIL) {
            tvBeautyDetailOrder.setVisibility(View.GONE);
            rvBeautydetailService.setVisibility(View.GONE);
            llGoBottom.setVisibility(View.GONE);
        } else {
            getServiceItem();
        }
        setListener();
    }

    private void setListener() {
        beautyServiceItemAdapter.setListener(new BeautyServiceItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(boolean goservice, SwitchService switchService) {
                if (goservice) {
                    Intent intent = new Intent(mContext, ServiceNewActivity.class);
                    intent.putExtra("serviceType", switchService.getServiceType());
                    intent.putExtra("serviceId", switchService.getServiceId());
                    Bundle bundle = new Bundle();
                    beautician = new Beautician(beautician_id, beautician_name, tid, beautician_iamge);
                    beautician.BeauDetailServiceType = switchService.getServiceType();
                    bundle.putSerializable("beautician", beautician);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    //跳转到预约服务界面
                    Intent intent = new Intent(mContext, WorkerMenuItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("beautician", new Beautician(beautician_id, beautician_name, tid, beautician_iamge));
                    intent.putExtras(bundle);
                    intent.putExtra("serviceName", switchService.getName());
                    startActivity(intent);
                }
            }
        });
        svBeauty.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (isVisibleLocal(rvBeautydetailService, true)) {
                    llGoBottom.setVisibility(View.GONE);
                } else {
                    llGoBottom.setVisibility(View.VISIBLE);
                }
            }
        });
        svBeauty.setListener(new MyScrollView.Listener() {
            @Override
            public void onScroll(int t) {
                if (t > 100) {
                    tvBeautydetailTitle.setVisibility(View.VISIBLE);
                } else {
                    tvBeautydetailTitle.setVisibility(View.GONE);
                }

                rlBeautydetailCheckproduct.getLocationOnScreen(screenLocaiton);
                //获取Y坐标
                if (screenLocaiton[1] < ScreenUtil.dip2px(BeauticianDetailActivity.this, 63)
                        && rlBeautydetailHeader2.getVisibility() == View.GONE) {
                    //设置距顶部距离
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlBeautydetailHeader2.getLayoutParams();
                    int[] screen = new int[2];
                    rlBeautydetailHeader.getLocationOnScreen(screen);
//                    layoutParams.topMargin = ScreenUtil.dip2px(BeauticianDetailActivity.this, 63);

                    //设置高度
                    layoutParams.height = rlBeautydetailHeader.getHeight() + ScreenUtil.dip2px(BeauticianDetailActivity.this, 95);
                    rlBeautydetailHeader2.setLayoutParams(layoutParams);
                    //创建图片
                    Bitmap bitmap1 = BitmapUtil.getInstance().cropDrawable(getResources(), R.drawable.bg_beautydetail_icon, rootRl.getWidth(),
                            rootRl.getHeight(), rootRl.getWidth(), layoutParams.height);
                    ivBack.setImageBitmap(bitmap1);
                    ViewGroup.LayoutParams layoutParams1 = ivBack.getLayoutParams();
                    layoutParams1.height = layoutParams.height;
                    ivBack.setLayoutParams(layoutParams1);

                    rlBeautydetailHeader2.setVisibility(View.VISIBLE);
                    Utils.mLogError("高度：" + " 根布局：" + rootRl.getHeight() + " 裁剪高度 " + layoutParams.height);

                } else if (screenLocaiton[1] > ScreenUtil.dip2px(BeauticianDetailActivity.this, 63)
                        && rlBeautydetailHeader2.getVisibility() == View.VISIBLE) {
                    rlBeautydetailHeader2.setVisibility(View.GONE);
                }

            }
        });
        beautyDetailPicAdapter.setListener(new BeautyDetailPicAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (productionList != null && productionList.size() > 0) {
                    String[] imgs = new String[imgList.size()];
                    //TODO 作品跳转
                    Utils.imageBrower(mContext, position, imgList.toArray(imgs));
                }
            }
        });
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    private Bitmap cropBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
        cropWidth /= 2;
        int cropHeight = (int) (cropWidth / 1.2);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, null, false);
    }

    public static boolean isVisibleLocal(View target, boolean judgeAll) {
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (judgeAll) {
            return rect.top == 0;
        } else {
            return rect.top >= 0;
        }
    }

    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(android.R.color.transparent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "1");
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("TAG", "2");
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    private void getServiceItem() {
        CommUtil.queryWorkerMenuItems(BeauticianDetailActivity.this, beautician_id, queryWorkerMenuItems);
    }

    private AsyncHttpResponseHandler queryWorkerMenuItems = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("dataset") && !jdata.isNull("dataset")) {
                                JSONArray jarrdataset = jdata.getJSONArray("dataset");
                                if (jarrdataset.length() > 0) {
                                    for (int i = 0; i < jarrdataset.length(); i++) {
                                        serviceList.add(SwitchService.json2Entity(jarrdataset
                                                .getJSONObject(i)));
                                    }
                                    llGoBottom.setVisibility(View.VISIBLE);
                                    beautyServiceItemAdapter.setList(serviceList);
                                }

                            }
                        }
                    }
                    if (serviceList.size() <= 0) {

                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "getRechargeBanner()数据异常e = " + e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void getCommentByBeauticianId() {
        CommUtil.getCommentByBeauticianIdNoHasImg(this, beautician_id, mNextRequestPage, pageSize, commentHanler);
    }

    private AsyncHttpResponseHandler commentHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("totalAmount") && !jdata.isNull("totalAmount")) {
                                int totalAmount = jdata.getInt("totalAmount");
                                tvBeautydetailCommonnum.setText(totalAmount + "条评价");
                            } else {
                                tvBeautydetailCommonnum.setText("0条评价");
                            }
                            if (jdata.has("dataset") && !jdata.isNull("dataset")) {
                                JSONArray jarrcommentListList = jdata.getJSONArray("dataset");
                                if (jarrcommentListList.length() > 0) {
                                    for (int i = 0; i < jarrcommentListList.length(); i++) {
                                        evalists.add(Comment.json2Entity(jarrcommentListList.getJSONObject(i)));
                                    }
                                }
                            }
                        }
                        //评价数量
                        if (evalists.size() > 0) {
                            //layout_all_comments.setVisibility(View.VISIBLE);
                            rlRootEvaluate.setVisibility(View.VISIBLE);
                            ivBeautydetailAlleva.setVisibility(View.VISIBLE);
                            beautylEvaAdapter.setEvalists(evalists);
                        } else {
                            rlRootEvaluate.setVisibility(View.GONE);
                            ivBeautydetailAlleva.setVisibility(View.GONE);
                            //layout_all_comments.setVisibility(View.GONE);
                        }
                    } else {
                        rlRootEvaluate.setVisibility(View.GONE);
                        ivBeautydetailAlleva.setVisibility(View.GONE);
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

    private void initData() {
        fIntent = getIntent();
        act = this;
        isVip = getIntent().getBooleanExtra("isVip", true);
        petList = (List<ApointMentPet>) getIntent().getSerializableExtra("petList");
        itemToMorePet = (ItemToMorePet) getIntent().getSerializableExtra("itemToMorePet");
        apptime = fIntent.getStringExtra("apptime");
        areaName = fIntent.getStringExtra("areaName");
        FromAreaid = fIntent.getIntExtra("areaid", 0);
        beautician_id = fIntent.getIntExtra("beautician_id", 0);
        isAvail = fIntent.getIntExtra("isavail", -1);
        previous = fIntent.getIntExtra("previous", 0);
        if (previous == Global.MAIN_TO_BEAUTICIANLIST) {
            beautician_id = getIntent().getIntExtra("id", 0);
        }
    }


    private void getData(int id, int FromAreaid) {
        mPDialog.showDialog();
        CommUtil.getBeauticianDetail(this, id, FromAreaid, apptime, BeauticianHanler);
    }

    private AsyncHttpResponseHandler BeauticianHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jData = jobj.getJSONObject("data");
                    if (jData.has("tid") && !jData.isNull("tid")) {
                        int tid = jData.getInt("tid");
                        switch (tid) {
                            case 1:
                                ivLevel.setImageResource(R.drawable.icon_middlelevel_beauty);
                                tvBeautydetailTitle.setText("中级美容师");
                                break;
                            case 2:
                                ivLevel.setImageResource(R.drawable.icon_highleve_beauty);
                                tvBeautydetailTitle.setText("高级美容师");
                                break;
                            case 3:
                                ivLevel.setImageResource(R.drawable.icon_toplevel_beauty);
                                tvBeautydetailTitle.setText("首席美容师");
                                break;
                        }
                    }

                    if (jData.has("realName") && !jData.isNull("realName")) {
                        beautician_name = jData.getString("realName");
                        tvBeautydetailName.setText(jData.getString("realName"));
                        tvBeautydetailName2.setText(jData.getString("realName"));
                    }
                    if (jData.has("shopId") && !jData.isNull("shopId")) {
                        shopId = jData.getInt("shopId");
                    }

                    if (jData.has("worksCount") && !jData.isNull("worksCount")) {
                        int workCount = jData.getInt("worksCount");
                        tvBeatydetailProductnum.setText(workCount + "");
                    }
                    if (jData.has("earliest") && !jData.isNull("earliest")) {
                        tvBeautydetailOrdertime.setText(jData.getString("earliest"));
                        tvBeautydetailOrdertime2.setText(jData.getString("earliest"));
                    }
                    if (jData.has("workerShareH5Url") && !jData.isNull("workerShareH5Url")) {
                        shareUrl = jData.getString("workerShareH5Url");
                    }
                    if (jData.has("workerShareH5Title") && !jData.isNull("workerShareH5Title")) {
                        shareTitle = jData.getString("workerShareH5Title");
                    }
                    if (jData.has("workerShareH5Context") && !jData.isNull("workerShareH5Context")) {
                        shareDesc = jData.getString("workerShareH5Context");
                    }
                    if (jData.has("avatar") && !jData.isNull("avatar") && !"".equals(jData.getString("avatar").trim())) {
                        beautician_iamge = jData.getString("avatar");
                        shareImg = beautician_iamge;
                        GlideUtil.loadImg(mContext, beautician_iamge, nvBeautydetailHead, R.drawable.icon_default);
                        GlideUtil.loadImg(mContext, beautician_iamge, nvBeautydetailHead2, R.drawable.icon_default);
                    }
                    if (jData.has("gender") && !jData.isNull("gender")) {
                        int gender = jData.getInt("gender");
                        if (gender == 1) {
                            ivBeautydetailSex.setImageResource(R.drawable.icon_beauty_boy);
                            ivBeautydetailSex2.setImageResource(R.drawable.icon_beauty_boy);
                        } else {
                            ivBeautydetailSex.setImageResource(R.drawable.icon_beauty_gril);
                            ivBeautydetailSex2.setImageResource(R.drawable.icon_beauty_gril);
                        }
                    }
                    if (jData.has("totalOrderAmount") && !jData.isNull("totalOrderAmount")) {
                        beautician_ordernum = jData.getInt("totalOrderAmount");
                        tvBeautydetailServicenum.setText(beautician_ordernum + "");
                    }
                    if (jData.has("orderTotal") && !jData.isNull("orderTotal")) {
                        int orderTotal = jData.getInt("orderTotal");
                        tvBeautydetailServicenum.setText(orderTotal + "");
                    }
                    if (jData.has("goodRate") && !jData.isNull("goodRate")) {
                        tvBeautydetailGood.setText(jData.getString("goodRate"));
                    }
                    if (jData.has("introduction") && !jData.isNull("introduction")) {
                        String introduction = jData.getString("introduction");
                        tvFosterShopdesc.setText(introduction);
                    }
                    if (jData.has("tid") && !jData.isNull("tid")) {
                        tid = jData.getInt("tid");
                    }
                    if (jData.has("expertise") && !jData.isNull("expertise")) {
                        JSONArray expertise = jData.getJSONArray("expertise");
                        if (expertise.length() > 0) {
                            llBeautydetailTags.removeAllViews();
                            for (int i = 0; i < expertise.length(); i++) {
                                View view = View.inflate(mContext, R.layout.item_beauty_commontag, null);
                                TextView tvTag = view.findViewById(R.id.tv_beauty_commontag);
                                tvTag.setText("#" + expertise.getString(i) + "#");
                                llBeautydetailTags.addView(view);
                            }
                        } else {
                            llBeautydetailTags.setVisibility(View.GONE);
                        }

                    }
                    if (jData.has("worker") && !jData.isNull("worker")) {
                        JSONArray jWorks = jData.getJSONArray("worker");
                        productionList.clear();
                        imgList.clear();
                        if (jWorks.length() > 0) {
                            for (int i = 0; i < jWorks.length(); i++) {
                                Production pr = Production.json2Entity(jWorks.getJSONObject(i));
                                productionList.add(pr);
                                imgList.add(productionList.get(i).image);
                            }

                            rvBeautydetailOne.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                            rvBeautydetailOne.setAdapter(beautyDetailPicAdapter);
                            if (productionList.size() > 0) {
                                tvNoProudct.setVisibility(View.GONE);
                                rlBeautydetailCheckproduct.setVisibility(View.VISIBLE);
                                if (productionList.size() > 4) {
                                    rvBeautydetailOne.start();
                                }
                            } else {
                                tvNoProudct.setVisibility(View.VISIBLE);
                                rlBeautydetailCheckproduct.setVisibility(View.GONE);
                            }

                        } else {
                            tvNoProudct.setVisibility(View.VISIBLE);
                            rlBeautydetailCheckproduct.setVisibility(View.GONE);
                        }

                    } else {
                        tvNoProudct.setVisibility(View.VISIBLE);
                        rlBeautydetailCheckproduct.setVisibility(View.GONE);
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

    private void setView() {
        setContentView(R.layout.activity_beauty_detail);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        beautylEvaAdapter = new BeautylEvaAdapter(mContext);
        rvBeautydetailEva.setLayoutManager(layoutManager);
        rvBeautydetailEva.setAdapter(beautylEvaAdapter);
        beautyServiceItemAdapter = new BeautyServiceItemAdapter(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        rvBeautydetailService.setLayoutManager(gridLayoutManager);
        rvBeautydetailService.setAdapter(beautyServiceItemAdapter);
        rvBeautydetailService.setNestedScrollingEnabled(false);
        beautyDetailPicAdapter = new BeautyDetailPicAdapter(mContext, productionList);
        screenLocaiton = new int[2];
    }

    @OnClick({R.id.iv_beautydetail_back, R.id.rl_beautydetail_checkproduct, R.id.iv_beautydetail_alleva,
            R.id.tv_beautydetail_commonnum, R.id.iv_beauty_share, R.id.ll_beautydetail_gobottom,
            R.id.nv_beautydetail_head,R.id.nv_beautydetail_head2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nv_beautydetail_head2:
            case R.id.nv_beautydetail_head:

                if (beautician_iamge != null){
                    String[] imgs = new String[]{beautician_iamge};
                    //TODO 头像跳转
                    Utils.imageBrower(mContext, 0, imgs);
                }

                break;

            case R.id.iv_beautydetail_back:
                finish();
                break;
            case R.id.rl_beautydetail_checkproduct:
                Intent commonIntent = new Intent(mContext, BeauticianProductuonActivity.class);
                commonIntent.putExtra("beautician_id", beautician_id);
                startActivity(commonIntent);
                break;
            case R.id.iv_beautydetail_alleva:
                Intent intent = new Intent(mContext, CommentListActivity.class);
                intent.putExtra("flag", 1);
                intent.putExtra("beautician_id", beautician_id);
                startActivity(intent);
                break;
            case R.id.tv_beautydetail_commonnum:
                Intent evaintent = new Intent(mContext, CommentListActivity.class);
                evaintent.putExtra("flag", 1);
                evaintent.putExtra("beautician_id", beautician_id);
                startActivity(evaintent);
                break;
            case R.id.iv_beauty_share:
                if (Utils.isStrNull(shareUrl)) {
                    if (!shareUrl.startsWith("http:")
                            && !shareUrl.startsWith("https:")) {
                        shareUrl = CommUtil.getStaticUrl() + shareUrl;
                    }
                }
                Utils.share(mContext, shareImg, shareTitle, shareDesc, shareUrl, "1,2", 0, "");
//                Utils.share(mContext, shareImg, shareTitle, shareDesc, shareUrl, "1,2", 0,
//                        "/pages/worker/worker?shopId="+shopId+"&workerId="+beautician_id);
                break;
            case R.id.ll_beautydetail_gobottom:
                svBeauty.smoothScrollTo(0, rvBeautydetailService.getTop());
                break;
        }
    }
}
