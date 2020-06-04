package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.android.material.appbar.AppBarLayout;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.PetServiceRecordAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.PetDiary;
import com.haotang.pet.entity.RefreshPetEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AppBarStateChangeListener;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 宠物详情页
 */
public class PetDetailActivity extends SuperActivity {
    @BindView(R.id.tv_petdetail_cardstate)
    TextView tvPetdetailCardstate;
    @BindView(R.id.ll_petdetail_title_right)
    LinearLayout llPetdetailTitleRight;
    @BindView(R.id.tv_petdetail_title)
    TextView tvPetdetailTitle;
    @BindView(R.id.iv_petdetail_petimg)
    ImageView ivPetdetailPetimg;
    @BindView(R.id.tv_petdetail_petnickname)
    TextView tvPetdetailPetnickname;
    @BindView(R.id.tv_petdetail_petsex)
    TextView tvPetdetailPetsex;
    @BindView(R.id.tv_petdetail_pettype)
    TextView tvPetdetailPettype;
    @BindView(R.id.tv_petdetail_petmonth)
    TextView tvPetdetailPetmonth;
    @BindView(R.id.tv_petdetail_xznum)
    TextView tvPetdetailXznum;
    @BindView(R.id.tv_petdetail_mrnum)
    TextView tvPetdetailMrnum;
    @BindView(R.id.tv_petdetail_jynum)
    TextView tvPetdetailJynum;
    @BindView(R.id.tv_petdetail_hljldesc)
    TextView tvPetdetailHljldesc;
    @BindView(R.id.ll_petdetail_hljl)
    LinearLayout llPetdetailHljl;
    @BindView(R.id.rv_petdetail)
    RecyclerView rvPetdetail;
    @BindView(R.id.srl_petdetail)
    SwipeRefreshLayout srlPetdetail;
    @BindView(R.id.abl_petdetail)
    AppBarLayout ablPetdetail;
    @BindView(R.id.tl_perdetail_tags)
    TagFlowLayout tlTags;
    private int customerpetid;
    private Pet pet;
    private int page = 1;
    private int pageSize;
    private List<PetDiary> list = new ArrayList<PetDiary>();
    private List<PetDiary> localList = new ArrayList<PetDiary>();
    private List<String> tagList = new ArrayList<String>();
    private PetServiceRecordAdapter petServiceRecordAdapter;
    private int dataAmount;
    private int imageAmount;
    public static SuperActivity act;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshPetEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        initWindows();
        setView();
        setLinster();
        getData();
        refresh();
    }

    private void initData() {
        act = this;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        customerpetid = getIntent().getIntExtra("customerpetid", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_pet_detail);
        ButterKnife.bind(this);
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

    private void setView() {
        srlPetdetail.setRefreshing(true);
        srlPetdetail.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvPetdetail.setHasFixedSize(true);
        rvPetdetail.setLayoutManager(new LinearLayoutManager(mContext));
        petServiceRecordAdapter = new PetServiceRecordAdapter(R.layout.item_pet_service_record, list);
        rvPetdetail.setAdapter(petServiceRecordAdapter);
    }

    private void setLinster() {
        petServiceRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlPetdetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
        ablPetdetail.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    //展开状态
                    tvPetdetailTitle.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    tvPetdetailTitle.setVisibility(View.VISIBLE);
                } else {
                    //中间状态
                    tvPetdetailTitle.setVisibility(View.GONE);
                }
            }
        });
    }

    private void refresh() {
        page = 1;
        list.clear();
        petServiceRecordAdapter.setEnableLoadMore(false);
        srlPetdetail.setRefreshing(true);
        getRecord();
    }

    private void loadMore() {
        getRecord();
    }

    private void getRecord() {
        mPDialog.showDialog();
        dataAmount = 0;
        imageAmount = 0;
        CommUtil.queryCustomerPetDiaryById(this, pageSize, page, customerpetid, "", handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            localList.clear();
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject jdata = jsonObject.getJSONObject("data");
                        if (jdata.has("dataAmount") && !jdata.isNull("dataAmount")) {
                            dataAmount = jdata.getInt("dataAmount");
                        }
                        if (jdata.has("imageAmount") && !jdata.isNull("imageAmount")) {
                            imageAmount = jdata.getInt("imageAmount");
                        }
                        tvPetdetailHljldesc.setText(dataAmount + "条记录+" + imageAmount + "张照片");
                        if (jdata.has("careHistory") && !jdata.isNull("careHistory")) {
                            JSONObject jcareHistory = jdata.getJSONObject("careHistory");
                            if (jcareHistory.has("message") && !jcareHistory.isNull("message")) {
                                JSONArray jarrmessage = jcareHistory.getJSONArray("message");
                                if (jarrmessage.length() > 0) {
                                    for (int i = 0; i < jarrmessage.length(); i++) {
                                        localList.add(PetDiary.json2Entity(jarrmessage
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                    if (page == 1) {
                        srlPetdetail.setRefreshing(false);
                        petServiceRecordAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    petServiceRecordAdapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        llPetdetailHljl.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                petServiceRecordAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        llPetdetailHljl.setVisibility(View.INVISIBLE);
                        if (page == 1) {
                            petServiceRecordAdapter.setEmptyView(setEmptyViewBase(2, "您的宠物还没有做过服务哦~", "", null,Color.WHITE,27));
                            petServiceRecordAdapter.loadMoreEnd(true);
                        } else {
                            petServiceRecordAdapter.loadMoreEnd(false);
                        }
                    }
                    petServiceRecordAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        petServiceRecordAdapter.setEmptyView(setEmptyViewBase(1, msg,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        petServiceRecordAdapter.setEnableLoadMore(false);
                        srlPetdetail.setRefreshing(false);
                    } else {
                        petServiceRecordAdapter.setEnableLoadMore(true);
                        petServiceRecordAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    petServiceRecordAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    petServiceRecordAdapter.setEnableLoadMore(false);
                    srlPetdetail.setRefreshing(false);
                } else {
                    petServiceRecordAdapter.setEnableLoadMore(true);
                    petServiceRecordAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            petServiceRecordAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (page == 1) {
                petServiceRecordAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                petServiceRecordAdapter.setEnableLoadMore(false);
                srlPetdetail.setRefreshing(false);
            } else {
                petServiceRecordAdapter.setEnableLoadMore(true);
                petServiceRecordAdapter.loadMoreFail();
            }
        }
    };

    private void getData() {
        mPDialog.showDialog();
        CommUtil.queryCustomerPetById(this, spUtil.getString("cellphone", ""),
                Global.getCurrentVersion(this), Global.getIMEI(this), customerpetid,
                queryHandler);
    }

    private AsyncHttpResponseHandler queryHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("pet") && !jdata.isNull("pet")) {
                            JSONObject jpet = jdata.getJSONObject("pet");
                            pet = Pet.json2Entity(jpet);
                        }
                    }
                } else {
                    ToastUtil.showToastShortCenter(getApplicationContext(),
                            jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortCenter(getApplicationContext(),
                        "数据异常");
            }
            if (pet != null) {
                GlideUtil.loadCircleImg(mContext, pet.image, ivPetdetailPetimg,
                        R.drawable.user_icon_unnet_circle);
                Utils.setText(tvPetdetailPetnickname, pet.nickName, "", View.VISIBLE, View.VISIBLE);
                if (Utils.isStrNull(pet.nickName)) {
                    tvPetdetailTitle.setText(pet.nickName);
                }

                tagList.clear();
                if (pet.sex == 1) {// 男
                    tagList.add("男");
//                    Utils.setText(tvPetdetailPetsex, "男", "", View.VISIBLE, View.VISIBLE);
                } else if (pet.sex == 0) {// 女
//                    Utils.setText(tvPetdetailPetsex, "女", "", View.VISIBLE, View.VISIBLE);
                    tagList.add("女");
                }
                //生日
                if(!TextUtils.isEmpty(pet.month)){
                    tagList.add(pet.month);
                }
                if (Utils.isStrNull(pet.mscolor)) {
                    tagList.add(pet.mscolor);
                }
                if (Utils.isStrNull(pet.remark)) {
                    if (pet.remark.contains("ym=1")) {
                        tagList.add("已打疫苗");
                    }
                    if (pet.remark.contains("spayed=1")) {
                        tagList.add("已绝育");
                    }
                }

                if (tagList!=null&&tagList.size()>0){
                    tlTags.setVisibility(View.VISIBLE);
                    tlTags.setAdapter(new TagAdapter<String>(tagList) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            View view = (View) View.inflate(mContext, R.layout.item_tag_petarchives,
                                    null);
                            TextView tv_item_foster_roomtag = (TextView) view.findViewById(R.id.tv_petarchives_tag);
                            tv_item_foster_roomtag.setText(s);
                            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            marginLayoutParams.rightMargin = ScreenUtil.dip2px(PetDetailActivity.this,5);
                            marginLayoutParams.leftMargin = ScreenUtil.dip2px(PetDetailActivity.this,0);
                            marginLayoutParams.topMargin = ScreenUtil.dip2px(PetDetailActivity.this,5);
                            view.setLayoutParams(marginLayoutParams);
                            return view;
                        }
                    });
                }else {
                    tlTags.setVisibility(View.GONE);
                }
                Utils.setText(tvPetdetailXznum, "洗澡：" + lessThanZero(pet.bathnum) + "次", "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tvPetdetailMrnum, "美容：" + lessThanZero(pet.beautynum) + "次", "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tvPetdetailJynum, "寄养：" + lessThanZero(pet.fosternum) + "次", "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tvPetdetailPettype, pet.name, "", View.VISIBLE, View.VISIBLE);
//                Utils.setText(tvPetdetailPetmonth, pet.month, "", View.VISIBLE, View.VISIBLE);
                String statusName = "";
                if (pet.certiOrder != null) {
                    int status = pet.certiOrder.status;
                    String[] statusNames = pet.certiOrder.statusName;
                    if (statusNames != null && statusNames.length > 0) {
                        if (status < statusNames.length) {
                            statusName = statusNames[status];
                        }
                    } else {
                        statusName = pet.certiOrderStatus;
                    }
                } else {
                    statusName = pet.certiOrderStatus;
                }
                Utils.setText(tvPetdetailCardstate, statusName, "", View.VISIBLE, View.VISIBLE);

                if (pet.kindid == 2) {// 猫
                    llPetdetailTitleRight.setVisibility(View.GONE);
                } else {
                    if (pet.certiDogSize == 1 || pet.certiDogSize == 2) {// 小型犬
                        llPetdetailTitleRight.setVisibility(View.VISIBLE);
                    } else {// 大型犬
                        llPetdetailTitleRight.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortCenter(getApplicationContext(),
                    "请求失败");
        }
    };

    private int lessThanZero(int num) {
        int currNum = 0;
        if (num <= 0) {
            currNum = 0;
        } else {
            currNum = num;
        }
        return currNum;
    }

    @OnClick({R.id.iv_petdetail_title_back, R.id.ll_petdetail_petnickname, R.id.ll_petdetail_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_petdetail_title_back:
                finish();
                break;
            case R.id.ll_petdetail_petnickname:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_PetHome_EditPet);
                startActivity(new Intent(mContext, PetAddActivity.class).
                        putExtra("customerpetid", customerpetid));
                break;
            case R.id.ll_petdetail_title_right:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_PetHome_DogCard);
                if (pet != null) {
                    if (pet.certiOrder != null) {
                        switch (pet.certiOrder.status) {
                            case 0:// 待支付
                                goNext(OrderPayActivity.class,
                                        Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY, 0,
                                        0, 0);
                                break;
                            case 1:// 已支付-待审核
                                goNext(ADActivity.class, 0, 0, 0, 1);
                                break;
                            case 2:// 审核通过-已认证
                                goNext(ADActivity.class, 0, 0, 0, 2);
                                break;
                            case 3:// 邮寄
                                goNext(ADActivity.class, 0, 0, 0, 3);
                                break;
                            case 4:// 上门取件
                                goNext(ADActivity.class, 0, 0, 0, 4);
                                break;
                            case 5:// 完成
                                goNext(ADActivity.class, 0, 0, 0, 5);
                                break;
                            case 6:// 审核不通过
                                goNext(ADActivity.class, 0, 0, 0, 6);
                                break;
                            case 7:// 未办证
                                goNext(ADActivity.class, 0, 0, 0, 7);
                                break;
                            default:
                                goNext(ADActivity.class, 0, 0, 0, 7);
                                break;
                        }
                    } else {
                        goNext(ADActivity.class, 0, 0, 0, -1);
                    }
                }
                break;
        }
    }

    private void goNext(Class clazz, int previous, int serviceid,
                        int servicetype, int status) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        getIntent().putExtra(Global.ANIM_DIRECTION(),
                Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("userId", spUtil.getInt("userid", 0));
        intent.putExtra("serviceid", serviceid);
        intent.putExtra("servicetype", servicetype);
        intent.putExtra("previous", previous);
        if (pet != null) {
            intent.putExtra("petid", pet.id);
            intent.putExtra("petkind", pet.kindid);
            intent.putExtra("petname", pet.name != null ? pet.name : "");
            intent.putExtra("customerpetid", pet.customerpetid);
            intent.putExtra("customerpetname",
                    pet.nickName != null ? pet.nickName : "");
            intent.putExtra("petimage", pet.image != null ? pet.image : "");
            if (pet.certiOrder != null) {
                intent.putExtra("CertiOrderId", pet.certiOrder.CertiOrderId);
            }
            String url = "";
            if (status == 1 || status == 2 || status == 3 || status == 4
                    || status == 5) {
                if (pet.certiOrder != null) {
                    url = CommUtil.getWebBaseUrl()
                            + "web/petcerti/auditresult?certi_id="
                            + pet.certiOrder.certiId + "&user_petid="
                            + pet.customerpetid;
                } else {
                    url = CommUtil.getWebBaseUrl()
                            + "web/petcerti/auditresult?user_petid="
                            + pet.customerpetid;
                }
            } else {
                if (pet.certiUrl.contains("?")) {
                    if (pet.certiOrder != null) {
                        url = pet.certiUrl + "&certi_id="
                                + pet.certiOrder.certiId + "&user_petid="
                                + pet.customerpetid;
                    } else {
                        url = pet.certiUrl + "&user_petid=" + pet.customerpetid;
                    }
                } else {
                    if (pet.certiOrder != null) {
                        url = pet.certiUrl + "?certi_id="
                                + pet.certiOrder.certiId + "&user_petid="
                                + pet.customerpetid;
                    } else {
                        url = pet.certiUrl + "?user_petid=" + pet.customerpetid;
                    }
                }
            }
            intent.putExtra("url", url);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("index", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
