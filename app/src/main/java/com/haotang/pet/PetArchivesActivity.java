package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.PetArchivesCardResonAdapter;
import com.haotang.pet.adapter.PetArchivesFwAdapter;
import com.haotang.pet.adapter.PetArchivesImgAdapter;
import com.haotang.pet.adapter.PetArchivesTagAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.PetArchivesServies;
import com.haotang.pet.entity.RefreshPetEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.haotang.pet.view.NoScollFullGridLayoutManager;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 宠物档案页
 */
public class PetArchivesActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.iv_petarchives_img)
    ImageView ivPetarchivesImg;
    @BindView(R.id.tv_petarchives_nickname)
    TextView tvPetarchivesNickname;
    @BindView(R.id.iv_petarchives_sex)
    ImageView ivPetarchivesSex;
    @BindView(R.id.rv_petarchives_tag)
    RecyclerView rvPetarchivesTag;
    @BindView(R.id.iv_petinfodetail_dj)
    ImageView ivPetinfodetailDj;
    @BindView(R.id.rl_petarchives_riji)
    RelativeLayout rlPetarchivesRiji;
    @BindView(R.id.rv_petarchives_fw)
    RecyclerView rvPetarchivesFw;
    @BindView(R.id.rl_petarchives_zpq)
    RelativeLayout rlPetarchivesZpq;
    @BindView(R.id.rv_petarchives_img)
    RecyclerView rvPetarchivesImg;
    @BindView(R.id.tv_petarchives_petcard)
    TextView tv_petarchives_petcard;
    @BindView(R.id.rv_petarchives_card_reason)
    RecyclerView rv_petarchives_card_reason;
    @BindView(R.id.ll_petarchives_petcard)
    LinearLayout ll_petarchives_petcard;
    private int customerpetid;
    private String[] petImg = new String[1];
    private Pet pet;
    private List<String> tagList = new ArrayList<String>();
    private PetArchivesTagAdapter petArchivesTagAdapter;
    private List<String> imgList = new ArrayList<String>();
    private PetArchivesImgAdapter petArchivesImgAdapter;
    private List<PetArchivesServies> fwList = new ArrayList<PetArchivesServies>();
    private PetArchivesFwAdapter petArchivesFwAdapter;
    private ArrayList<Pet> pets = new ArrayList<>();
    public static SuperActivity act;
    private List<String> resonList = new ArrayList<String>();
    private PetArchivesCardResonAdapter petArchivesCardResonAdapter;
    private ArrayList<String> monthList = new ArrayList<String>();
    private String localMonth;
    private List<Pet> selectList = new ArrayList<Pet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(this);
        customerpetid = getIntent().getIntExtra("customerpetid", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_pet_archives);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("编辑");
        tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));
        tvTitlebarTitle.setText("宠物档案");

        rvPetarchivesTag.setHasFixedSize(true);
        rvPetarchivesTag.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                NoScollFullGridLayoutManager(rvPetarchivesTag, this, 3, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager.setScrollEnabled(false);
        rvPetarchivesTag.setLayoutManager(noScollFullGridLayoutManager);
        rvPetarchivesTag.addItemDecoration(new GridSpacingItemDecoration(3,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
        tagList.clear();
        petArchivesTagAdapter = new PetArchivesTagAdapter(R.layout.item_petarchives_tag
                , tagList);
        rvPetarchivesTag.setAdapter(petArchivesTagAdapter);

        rvPetarchivesFw.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PetArchivesActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPetarchivesFw.setLayoutManager(linearLayoutManager);
        fwList.clear();
        petArchivesFwAdapter = new PetArchivesFwAdapter(R.layout.item_petarchives_fw, fwList);
        rvPetarchivesFw.setAdapter(petArchivesFwAdapter);

        rvPetarchivesImg.setHasFixedSize(true);
        rvPetarchivesImg.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager1 = new
                NoScollFullGridLayoutManager(rvPetarchivesImg, this, 3, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager1.setScrollEnabled(false);
        rvPetarchivesImg.setLayoutManager(noScollFullGridLayoutManager1);
        rvPetarchivesImg.addItemDecoration(new GridSpacingItemDecoration(3,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
        imgList.clear();
        petArchivesImgAdapter = new PetArchivesImgAdapter(R.layout.item_petarchives_img, imgList);
        rvPetarchivesImg.setAdapter(petArchivesImgAdapter);

        rv_petarchives_card_reason.setHasFixedSize(true);
        rv_petarchives_card_reason.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rv_petarchives_card_reason.setLayoutManager(noScollFullLinearLayoutManager);
        petArchivesCardResonAdapter = new PetArchivesCardResonAdapter(R.layout.item_petarchives_card_reson, resonList);
        rv_petarchives_card_reason.setAdapter(petArchivesCardResonAdapter);
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_trans_5));
        rv_petarchives_card_reason.addItemDecoration(divider);
    }

    private void setLinster() {
        petArchivesFwAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (fwList.size() > position) {
                    if (position == 0) {
                        UmengStatistics.UmengEventStatistics(PetArchivesActivity.this, Global.UmengEventID.click_PetHome_BathSelect);
                        if (Utils.isStrNull(pet.availServiceType)) {
                            if (pet.availServiceType.contains("1")) {
                                int serviceId = 0;
                                if (pet.kindid == 1) {// 狗
                                    serviceId = 1;
                                } else if (pet.kindid == 2) {// 猫
                                    serviceId = 3;
                                }
                                Intent intent = new Intent(PetArchivesActivity.this, ServiceNewActivity.class);
                                intent.putExtra("serviceType", 1);
                                intent.putExtra("myPetLIst", (Serializable) selectList);
                                intent.putExtra("serviceId", serviceId);
                                intent.putExtra("myPetId", pet.customerpetid);
                                startActivity(intent);
                            } else {
                                ToastUtil.showToastShortBottom(PetArchivesActivity.this, "您的宠物暂不支持洗澡");
                            }
                        } else {
                            ToastUtil.showToastShortBottom(PetArchivesActivity.this, "您的宠物暂不支持洗澡");
                        }
                    } else if (position == 1) {
                        UmengStatistics.UmengEventStatistics(PetArchivesActivity.this, Global.UmengEventID.click_PetHome_BeautySelect);
                        if (Utils.isStrNull(pet.availServiceType)) {
                            if (pet.availServiceType.contains("2")) {
                                int serviceId = 0;
                                if (pet.kindid == 1) {// 狗
                                    serviceId = 2;
                                } else if (pet.kindid == 2) {// 猫
                                    serviceId = 4;
                                }
                                Intent intent = new Intent(PetArchivesActivity.this, ServiceNewActivity.class);
                                intent.putExtra("serviceType", 2);
                                intent.putExtra("myPetLIst", (Serializable) selectList);
                                intent.putExtra("serviceId", serviceId);
                                intent.putExtra("myPetId", pet.customerpetid);
                                startActivity(intent);
                            } else {
                                ToastUtil.showToastShortBottom(PetArchivesActivity.this, "您的宠物暂不支持美容");
                            }
                        } else {
                            ToastUtil.showToastShortBottom(PetArchivesActivity.this, "您的宠物暂不支持洗澡");
                        }
                    } else if (position == 2) {
                        UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_PetHome_FosterSelect);
                        startActivity(new Intent(mContext, FosterHomeActivity.class));
                    }
                }
            }
        });
        petArchivesImgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (imgList.size() > position) {
                    String[] imgs = new String[imgList.size()];
                    Utils.imageBrower(PetArchivesActivity.this, position, imgList.toArray(imgs));
                }
            }
        });
    }

    private void getData() {
        pet = null;
        monthList.clear();
        localMonth = null;
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
                        if (jdata.has("monthHistory") && !jdata.isNull("monthHistory")) {
                            JSONArray montharray = jdata.getJSONArray("monthHistory");
                            if (montharray != null && montharray.length() > 0) {
                                for (int i = 0; i < montharray.length(); i++) {
                                    monthList.add(montharray.getString(i));
                                }
                            }
                        }
                        if (jdata.has("careHistory") && !jdata.isNull("careHistory")) {
                            JSONObject careHistory = jdata.getJSONObject("careHistory");
                            if (careHistory.has("monthHistory") && !careHistory.isNull("monthHistory")) {
                                localMonth = careHistory.getString("monthHistory");
                            }
                        }
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
                selectList.clear();
                selectList.add(pet);
                GlideUtil.loadCircleImg(PetArchivesActivity.this, pet.image, ivPetarchivesImg,
                        R.drawable.user_icon_unnet_circle);
                Utils.setText(tvPetarchivesNickname, pet.nickName, "", View.VISIBLE, View.VISIBLE);
                if (pet.sex == 1) {// 男
                    ivPetarchivesSex
                            .setImageResource(R.drawable.pet_archives_nan);
                } else if (pet.sex == 0) {// 女
                    ivPetarchivesSex
                            .setImageResource(R.drawable.pet_archives_nv);
                }
                fwList.clear();
                fwList.add(new PetArchivesServies(R.drawable.icon_pet_xz, "洗澡" + lessThanZero(pet.bathnum) + "次"));
                fwList.add(new PetArchivesServies(R.drawable.icon_pet_mr, "美容" + lessThanZero(pet.beautynum) + "次"));
                fwList.add(new PetArchivesServies(R.drawable.icon_pet_jy, "寄养" + lessThanZero(pet.fosternum) + "次"));
//                fwList.add(new PetArchivesServies(R.drawable.icon_pet_yy, "游泳" + lessThanZero(pet.sumSwim) + "次"));
                tagList.clear();
                if (Utils.isStrNull(pet.name)) {
                    tagList.add(pet.name);
                }
                if (Utils.isStrNull(pet.remark)) {
                    if (pet.remark.contains("ym=1")) {
                        tagList.add("已打疫苗");
                    }
                    if (pet.remark.contains("spayed=1")) {
                        tagList.add("已绝育");
                    }
                }
                if (Utils.isStrNull(pet.month)) {
                    tagList.add(pet.month);
                }
                if (Utils.isStrNull(pet.mscolor)) {
                    tagList.add(pet.mscolor);
                }
                if (pet.imgList.size() > 0) {
                    rlPetarchivesZpq.setVisibility(View.VISIBLE);
                    rvPetarchivesImg.setVisibility(View.VISIBLE);
                    imgList.clear();
                    imgList.addAll(pet.imgList);
                    petArchivesImgAdapter.notifyDataSetChanged();
                } else {
                    rlPetarchivesZpq.setVisibility(View.GONE);
                    rvPetarchivesImg.setVisibility(View.GONE);
                }
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
                Utils.setText(tv_petarchives_petcard, statusName, "", View.VISIBLE, View.VISIBLE);

                if (pet.kindid == 2) {// 猫
                    ll_petarchives_petcard.setVisibility(View.GONE);
                } else {
                    if (pet.certiDogSize == 1 || pet.certiDogSize == 2) {// 小型犬
                        ll_petarchives_petcard.setVisibility(View.VISIBLE);
                        if (pet.certiOrder != null) {
                            switch (pet.certiOrder.status) {
                                case 2:// 已认证
                                    break;
                                case 5:// 已领证
                                    break;
                                case 6:// 未通过
                                    if (pet.certiOrder != null) {
                                        String remark = pet.certiOrder.remark;
                                        if (remark != null && !TextUtils.isEmpty(remark)) {
                                            resonList.clear();
                                            if (remark.contains("&")) {// 多条
                                                String[] reson = remark.split("&");
                                                List<String> asList = Arrays.asList(reson);
                                                resonList.addAll(asList);
                                            } else {// 一条
                                                resonList.add(remark);
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {// 大型犬
                        ll_petarchives_petcard.setVisibility(View.GONE);
                    }
                }
                if (resonList.size() > 0) {
                    rv_petarchives_card_reason.setVisibility(View.VISIBLE);
                    petArchivesCardResonAdapter.notifyDataSetChanged();
                } else {
                    rv_petarchives_card_reason.setVisibility(View.GONE);
                }
                petArchivesFwAdapter.notifyDataSetChanged();
                petArchivesTagAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other, R.id.iv_petarchives_img, R.id.rl_petarchives_riji,
            R.id.rl_petarchives_zpq, R.id.rl_petarchives_petcard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_petarchives_petcard:
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
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_PetHome_EditPet);
                startActivity(new Intent(PetArchivesActivity.this, PetAddActivity.class).
                        putExtra("customerpetid", customerpetid));
                break;
            case R.id.iv_petarchives_img:
                if (pet != null) {
                    if (Utils.isStrNull(pet.image)) {
                        petImg[0] = pet.image;
                        Utils.imageBrower(PetArchivesActivity.this, 0, petImg);
                    }
                }
                break;
            case R.id.rl_petarchives_riji:
                Log.e("TAG", "monthList = " + monthList.toString());
                Intent intent = new Intent(PetArchivesActivity.this, PetDiaryActivity.class);
                intent.putExtra("customerpetid", this.customerpetid);
                intent.putExtra("localMonth", localMonth);
                intent.putStringArrayListExtra("monthList", monthList);
                startActivity(intent);
                break;
            case R.id.rl_petarchives_zpq:
                startActivity(new Intent(PetArchivesActivity.this, PetArchivesImgsActivity.class).
                        putExtra("customerpetid", this.customerpetid));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshPetEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            getData();
        }
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
