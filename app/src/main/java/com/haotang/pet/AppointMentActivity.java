package com.haotang.pet;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AppointBottomPetAdapter;
import com.haotang.pet.adapter.AppointMentBannerAdapter;
import com.haotang.pet.adapter.AppointMentItemAdapter;
import com.haotang.pet.adapter.AppointMentPetAdapter;
import com.haotang.pet.adapter.AppointMentPetMxAdapter;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.ApointMentItemPrice;
import com.haotang.pet.entity.ApointMentItemPrices;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.AppointRechargeEvent;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.AppointWorkerPrice;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.DeleteItemEvent;
import com.haotang.pet.entity.ItemDetailToAppointEvent;
import com.haotang.pet.entity.ItemToMorePet;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.RechargeBanner;
import com.haotang.pet.entity.RecommendServiceCard;
import com.haotang.pet.entity.RecommmendCashBack;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.ServiceType;
import com.haotang.pet.entity.WorkerAndTime;
import com.haotang.pet.entity.WorkerAndTimeEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.DisplayUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.haotang.pet.view.ShouXiItemDialog;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * 预约新界面
 */
public class AppointMentActivity extends SuperActivity {
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_appointment_num)
    TextView tvAppointmentNum;
    @BindView(R.id.tv_appointment_payprice)
    TextView tvAppointmentPayprice;
    @BindView(R.id.tv_appointment_phone)
    ImageView tvAppointmentPhone;
    @BindView(R.id.tv_appointment_shopname)
    TextView tvAppointmentShopname;
    @BindView(R.id.tv_appointment_shopopentime)
    TextView tvAppointmentShopopentime;
    @BindView(R.id.rv_appointmen_pet)
    RecyclerView rvAppointmenPet;
    @BindView(R.id.tv_appointmen_pet_more)
    TextView tvAppointmenPetMore;
    @BindView(R.id.iv_appointmen_pet_more)
    ImageView ivAppointmenPetMore;
    @BindView(R.id.ll_appointmen_pet_more)
    LinearLayout llAppointmenPetMore;
    @BindView(R.id.ll_appointment_fhxg)
    LinearLayout llAppointmentFhxg;
    @BindView(R.id.tv_appointment_servicetype)
    TextView tvAppointmentServicetype;
    @BindView(R.id.rl_appointment_servicetype)
    RelativeLayout rlAppointmentServicetype;
    @BindView(R.id.ll_appointmen_item_more)
    LinearLayout llAppointmenItemMore;
    @BindView(R.id.rv_appointmen_item)
    RecyclerView rvAppointmenItem;
    @BindView(R.id.ll_appointmen_item)
    LinearLayout llAppointmenItem;
    @BindView(R.id.tv_appointmen_time)
    TextView tvAppointmenTime;
    @BindView(R.id.ll_appointmen_time)
    LinearLayout llAppointmenTime;
    @BindView(R.id.iv_appointmen_mrsimg)
    ImageView ivAppointmenMrsimg;
    @BindView(R.id.tv_appointmen_mrsname)
    TextView tvAppointmenMrsname;
    @BindView(R.id.rl_appointmen_mrsinfo)
    RelativeLayout rlAppointmenMrsinfo;
    @BindView(R.id.tv_appointmen_mrs)
    TextView tvAppointmenMrs;
    @BindView(R.id.ll_appointmen_mrs)
    LinearLayout llAppointmenMrs;
    @BindView(R.id.iv_appointmen_js)
    ImageView ivAppointmenJs;
    @BindView(R.id.rl_appointmen_js)
    RelativeLayout rlAppointmenJs;
    @BindView(R.id.tv_appointmen_bannertitle)
    TextView tvAppointmenBannertitle;
    @BindView(R.id.rv_appointmen_banner)
    RecyclerView rvAppointmenBanner;
    @BindView(R.id.ll_appointmen_banner)
    LinearLayout llAppointmenBanner;
    @BindView(R.id.iv_appointment_cart)
    ImageView iv_appointment_cart;
    @BindView(R.id.rl_appointment_root)
    RelativeLayout rl_appointment_root;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.rl_appointment_card)
    RelativeLayout rl_appointment_card;
    @BindView(R.id.tv_appointment_card_go)
    TextView tv_appointment_card_go;
    @BindView(R.id.tv_appointment_card_desc)
    TextView tv_appointment_card_desc;
    @BindView(R.id.tv_appointment_carddiscountprice)
    TextView tv_appointment_carddiscountprice;
    @BindView(R.id.tv_appointment_payprice_yj)
    TextView tv_appointment_payprice_yj;
    @BindView(R.id.tv_appointment_fxprice)
    TextView tv_appointment_fxprice;
    private double lat;//用户地址经纬度
    private double lng;
    private int serviceLoc;//区分上门到店
    private String petAddress;//宠物地址
    private String shopName;//门店名称
    private int shopId;//门店ID
    private List<ApointMentItem> localItemList = new ArrayList<ApointMentItem>();//单项列表详细数据
    private List<ApointMentItem> tempLocalItemList = new ArrayList<ApointMentItem>();//单项列表详细数据备份
    private List<ApointMentItem> shouXiItemList = new ArrayList<ApointMentItem>();//赠送单项数据
    private List<ApointMentItem> shouXiItemList1 = new ArrayList<ApointMentItem>();
    private List<ApointMentPet> petList = new ArrayList<ApointMentPet>();//宠物列表数据
    private List<ApointMentPet> localPetList = new ArrayList<ApointMentPet>();//宠物列表数据备份
    private List<RechargeBanner> bannerList = new ArrayList<RechargeBanner>();//底部banner数据
    private List<ApointMentItemPrice> tempLocalItemPriceList = new ArrayList<ApointMentItemPrice>();//单项列表详细数据备份
    private List<ApointMentItemPrice> localItemPriceList = new ArrayList<ApointMentItemPrice>();//单项列表详细数据
    private AppointMentBannerAdapter appointMentBannerAdapter;//底部banner适配器
    private AppointMentItemAdapter appointMentItemAdapter;//单项列表数据适配器
    private AppointMentPetAdapter appointMentPetAdapter;//宠物列表数据适配器
    private String appointment;//预约时间
    private List<Pet> myPets;//宠物列表数据，上个页面传过来的
    private ServiceShopAdd shop;//门店数据对象
    private boolean isVip;//是否是会员，现在会员体系已下线，但是逻辑还在
    private int workerId;//选中美容师ID
    private int tid = 0;//选中美容师级别（一共三个级别tid=1中级，tid=2高级，tid=3首席）
    private String serviceName;//基础服务服务名称
    private String shopPhone;//门店电话
    private String openTime;//门店营业时间
    private boolean isOpen;//宠物列表打开收起开关
    private boolean isJsOpen;//接送打开关闭开关
    private AppointBottomPetAdapter appointBottomPetAdapter;//多宠选择单项，底部宠物弹框数据适配器
    private AppointMentPetMxAdapter appointMentPetMxAdapter;//底部明细弹框数据适配器
    private ServiceType serviceType;//服务类型：1洗澡 2美容 3特色服务
    private AppointWorker localAppointWorker;//选中的美容师对象
    private List<AppointWorker> availableWorkerList = new ArrayList<AppointWorker>();//可约的美容师数据
    private List<AppointWorker> unAvailableWorkerList = new ArrayList<AppointWorker>();//不可约的美容师数据
    private int dayposition;//选中的时间格子的位置
    private int pickupAmount;//接送数量
    private boolean isSelectPickup;//是否选择接送
    private int pickup;//当前时间是否有接送（0没有，1有）
    private double lastPrice;//非VIP总价
    private double lastVipPrice;//VIP总价
    private List<Integer> workerIdList = new ArrayList<Integer>();//美容师ID数据列表
    private ApointMentItem apointMentItem1;//单项列表操作的当前单项
    private int flag;//宠物标识，1是单宠，2是多宠
    private List<ApointMentPet> overTimePetList = new ArrayList<ApointMentPet>();//选中了时间和美容师后校验时间不够用时保存的宠物数据
    private ItemToMorePet itemToMorePet = new ItemToMorePet();//
    private List<Integer> myPetIds = new ArrayList<Integer>();//宠物ID数据列表
    private List<ApointMentItem> timeItemList = new ArrayList<ApointMentItem>();
    private WorkerAndTime switchWorkerAndTime;
    private ArrayList<Integer> itemIds;//再来一单逻辑
    private double pickupPrice;//接送金额
    private String workerIds;//选择了时间以后，当前时间下所有可约的美容师的ID，逗号连接
    private int pickupResult;//是否可接送，1可接送，0不可接送
    public String pickupTip;//不可接送时的toast文案
    private int pickupFlag;
    private String defaultWorkerTag;//默认推荐美容师标签，有并且不为空就是默认推荐美容师
    private String localAppointment;//预约时间备份
    //加入购物车动画使用的变量
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private ImageView beisaierImageView;
    private int beforeNum;//当前单项已添加的宠物数量
    private boolean isShouXiItemDialogShow;//单项赠送弹框是否已弹出
    private boolean isCanShouXiItemDialog;//是否满足单项赠送弹框弹出条件
    private RecommendServiceCard recommendServiceCard;//服务器推荐使用的E卡信息
    private String btn_txt;//服务器推荐使用的E卡信息的按钮文案
    private String tip;
    private double discount = 1;
    //
    private int point;
    private int serviceCardId;
    private CommAddr commAddr;
    private RecommmendCashBack recommmendCashBack;
    private String backup;
    private String bindCardTitle;
    private String bindCardContent;
    private boolean isFirstAddToothCardItem;//首次进入自动选中刷牙卡免费单项

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
        isFirstAddToothCardItem = true;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        switchWorkerAndTime = null;
        act = this;
        MApplication.listAppoint.add(this);
        Intent intent = getIntent();
        itemIds = intent.getIntegerArrayListExtra("itemIds");
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        serviceName = getIntent().getStringExtra("serviceName");
        Log.e("TAG", "serviceName = " + serviceName);
        petAddress = intent.getStringExtra("petAddress");
        recommendServiceCard = (RecommendServiceCard) intent.getSerializableExtra("recommendServiceCard");
        recommmendCashBack = (RecommmendCashBack) intent.getSerializableExtra("recommmendCashBack");
        if (recommendServiceCard != null) {
            btn_txt = recommendServiceCard.getBtn_txt();
            tip = recommendServiceCard.getTip();
            backup = recommendServiceCard.getBackup();
            discount = recommendServiceCard.getDiscount();
            point = recommendServiceCard.getPoint();
            serviceCardId = recommendServiceCard.getServiceCardId();
        }
        myPets = (List<Pet>) intent.getSerializableExtra("myPets");
        shop = (ServiceShopAdd) intent.getSerializableExtra("shop");
        serviceType = (ServiceType) intent.getSerializableExtra("serviceType");
        Beautician beautician = (Beautician) intent.getSerializableExtra("beautician");
        commAddr = (CommAddr) getIntent().getSerializableExtra("commAddr");
        if (serviceType != null) {
            serviceLoc = serviceType.serviceLoc;
        } else {
            serviceLoc = intent.getIntExtra("serviceLoc", 0);
        }
        if (beautician != null) {
            workerId = beautician.id;
            tid = beautician.sort;
            localAppointWorker = new AppointWorker(workerId, tid, beautician.image, beautician.name);
        }
        if (shop != null) {
            shopId = shop.shopId;
            shopName = shop.shopName;
            shopPhone = shop.shopPhone;
            openTime = shop.openTime;
        }
        if (myPets != null && myPets.size() > 0) {
            petList.clear();
            for (int i = 0; i < myPets.size(); i++) {
                petList.add(new ApointMentPet(myPets.get(i).image, myPets.get(i).nickName, serviceName, 0,
                        0, myPets.get(i).id, myPets.get(i).customerpetid, myPets.get(i).serviceid, myPets.get(i).kindid));
            }
        }
    }

    private void findView() {
        setContentView(R.layout.activity_appoint_ment);
        ButterKnife.bind(this);
    }

    private void setView() {
        if (recommendServiceCard != null) {
            rl_appointment_card.setVisibility(View.VISIBLE);
            Utils.setText(tv_appointment_card_desc, tip, "", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(btn_txt)) {
                tv_appointment_card_go.setVisibility(View.VISIBLE);
                tv_appointment_card_go.setText(btn_txt);
            } else {
                tv_appointment_card_go.setVisibility(View.GONE);
            }
        } else {
            rl_appointment_card.setVisibility(View.GONE);
        }
        if (serviceLoc == 1) {
            rlAppointmenJs.setVisibility(View.VISIBLE);
        } else {
            rlAppointmenJs.setVisibility(View.GONE);
        }
        if (petList.size() > 2) {
            llAppointmenPetMore.setVisibility(View.VISIBLE);
            isOpen = false;
            tvAppointmenPetMore.setText("展开更多");
            ivAppointmenPetMore.setImageResource(R.drawable.icon_appoint_pet_zk);
            localPetList.clear();
            localPetList.addAll(petList.subList(0, 2));
        } else {
            llAppointmenPetMore.setVisibility(View.GONE);
            localPetList.clear();
            localPetList.addAll(petList);
        }
        tvAppointmentNum.bringToFront();
        if (localAppointWorker != null) {
            tvAppointmenMrs.setVisibility(View.GONE);
            rlAppointmenMrsinfo.setVisibility(View.VISIBLE);
            GlideUtil.loadCircleImg(this, localAppointWorker.getAvatar(), ivAppointmenMrsimg, R.drawable.user_icon_unnet_circle);
            Utils.setText(tvAppointmenMrsname, localAppointWorker.getRealName(), "", View.VISIBLE, View.VISIBLE);
        } else {
            tvAppointmenMrs.setVisibility(View.VISIBLE);
            rlAppointmenMrsinfo.setVisibility(View.GONE);
        }
        Utils.setText(tvAppointmentShopname, shopName, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvAppointmentShopopentime, "营业时间：" + openTime, "", View.VISIBLE, View.VISIBLE);
        tvTitlebarTitle.setText("选择服务");
        if (serviceLoc == 2) {
            tvAppointmentServicetype.setText("选择上门服务:" + petAddress);
        } else if (serviceLoc == 1) {
            tvAppointmentServicetype.setText("选择到店服务:" + shopName);
        }
        rvAppointmenBanner.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAppointmenBanner.setLayoutManager(linearLayoutManager1);
        bannerList.clear();
        appointMentBannerAdapter = new AppointMentBannerAdapter(R.layout.item_appointment_banner, bannerList);
        rvAppointmenBanner.setAdapter(appointMentBannerAdapter);

        rvAppointmenItem.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAppointmenItem.setLayoutManager(linearLayoutManager);
        localItemList.clear();
        appointMentItemAdapter = new AppointMentItemAdapter(R.layout.item_appointment_item, localItemList, tid);
        rvAppointmenItem.setAdapter(appointMentItemAdapter);

        rvAppointmenPet.setHasFixedSize(true);
        rvAppointmenPet.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvAppointmenPet.setLayoutManager(noScollFullLinearLayoutManager);
        appointMentPetAdapter = new AppointMentPetAdapter(R.layout.item_appointment_pet, localPetList, isVip);
        rvAppointmenPet.setAdapter(appointMentPetAdapter);
    }

    private void setLinster() {
        appointMentBannerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (bannerList.size() > position) {
                    Utils.goService(AppointMentActivity.this, bannerList.get(position).point, bannerList.get(position).backup);
                }
            }
        });
        appointMentPetAdapter.setOnSwitchServiceListener(new AppointMentPetAdapter.OnSwitchServiceListener() {
            @Override
            public void OnSwitchService(int position) {
                if (localPetList.size() > position) {
                    Intent intent = new Intent(AppointMentActivity.this, SwitchServiceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pet", localPetList.get(position));
                    bundle.putSerializable("appointWorker", localAppointWorker);
                    bundle.putSerializable("shop", shop);
                    intent.putExtras(bundle);
                    intent.putExtra("isVip", isVip);
                    intent.putExtra("serviceCardId", serviceCardId);
                    intent.putExtra("tid", tid);
                    intent.putExtra("serviceLoc", serviceLoc);
                    intent.putExtra("appointment", appointment);
                    intent.putExtra("petList", (Serializable) petList);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("dayposition", dayposition);
                    startActivityForResult(intent, Global.APPOINTMENT_TO_SWITCHSERVICE);
                }
            }
        });
        appointMentItemAdapter.setOnItemAddListener(new AppointMentItemAdapter.OnItemAddListener() {
            @Override
            public void OnItemAdd(int position, ImageView imageView) {
                beisaierImageView = imageView;
                if (localItemList.size() > position) {
                    apointMentItem1 = localItemList.get(position);
                    for (int i = 0; i < petList.size(); i++) {
                        ApointMentPet apointMentPet = petList.get(i);
                        for (int j = 0; j < localItemPriceList.size(); j++) {
                            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                            List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                            if (apointMentItemPrice.getItemId() == apointMentItem1.getId()) {
                                if (itemPriceList != null && itemPriceList.size() > 0) {
                                    for (int k = 0; k < itemPriceList.size(); k++) {
                                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                        if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                            apointMentItem1.setVipPrice10(apointMentItemPrices.getVipPrice10());
                                            apointMentItem1.setVipPrice20(apointMentItemPrices.getVipPrice20());
                                            apointMentItem1.setVipPrice30(apointMentItemPrices.getVipPrice30());
                                            apointMentItem1.setPrice10(apointMentItemPrices.getPrice10());
                                            apointMentItem1.setPrice20(apointMentItemPrices.getPrice20());
                                            apointMentItem1.setPrice30(apointMentItemPrices.getPrice30());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    List<Integer> availableMyPets = apointMentItem1.getAvailableMyPets();
                    for (int i = 0; i < petList.size(); i++) {
                        petList.get(i).setState(1);
                        petList.get(i).setSelect(false);
                    }
                    for (int i = 0; i < availableMyPets.size(); i++) {
                        Integer integer = availableMyPets.get(i);
                        for (int j = 0; j < petList.size(); j++) {
                            ApointMentPet apointMentPet = petList.get(j);
                            apointMentPet.setPriceSuffix(apointMentItem1.getPriceSuffix());
                            if (integer == apointMentPet.getMyPetId()) {//宠物支持这个单项
                                List<ApointMentItem> itemList = apointMentPet.getItemList();
                                if (itemList != null && itemList.size() > 0) {
                                    petList.get(j).setState(3);
                                    for (int k = 0; k < itemList.size(); k++) {
                                        ApointMentItem apointMentItem = itemList.get(k);
                                        if (apointMentItem.getId() == apointMentItem1.getId()) {
                                            if (apointMentItem.isShouXi()) {
                                                petList.get(j).setState(2);
                                            } else if (apointMentItem.isTeethCard()) {
                                                petList.get(j).setState(5);
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    petList.get(j).setState(3);
                                }
                            }
                        }
                    }
                    for (int i = 0; i < petList.size(); i++) {
                        ApointMentPet apointMentPet = petList.get(i);
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            boolean isSelect = false;
                            for (int j = 0; j < itemList.size(); j++) {
                                ApointMentItem apointMentItem = itemList.get(j);
                                if (apointMentItem.getId() == apointMentItem1.getId()) {
                                    isSelect = true;
                                    break;
                                }
                            }
                            if (isSelect) {
                                apointMentPet.setSelect(true);
                            } else {
                                apointMentPet.setSelect(false);
                            }
                        } else {
                            apointMentPet.setSelect(false);
                        }
                    }
                    if (petList.size() == 1) {
                        ApointMentPet apointMentPet2 = petList.get(0);
                        if (apointMentPet2.isSelect()) {//减
                            List<ApointMentItem> itemList1 = apointMentPet2.getItemList();
                            for (int i = 0; i < itemList1.size(); i++) {
                                ApointMentItem apointMentItem = itemList1.get(i);
                                if (apointMentItem.getId() == apointMentItem1.getId()) {
                                    itemList1.remove(i);
                                    break;
                                }
                            }
                            apointMentItem1.setState(3);
                            appointMentItemAdapter.notifyDataSetChanged();
                            setLastPrice();
                        } else {//加
                            if (Utils.isStrNull(appointment) && localAppointWorker != null) {
                                overTimePetList.clear();
                                for (int i = 0; i < petList.size(); i++) {
                                    ApointMentPet apointMentPet1 = petList.get(i);
                                    ApointMentPet apointMentPet = new ApointMentPet();
                                    apointMentPet.setPriceSuffix(apointMentPet1.getPriceSuffix());
                                    apointMentPet.setPrice(apointMentPet1.getPrice());
                                    apointMentPet.setSelect(apointMentPet1.isSelect());
                                    apointMentPet.setItemPrice(apointMentPet1.getItemPrice());
                                    apointMentPet.setItemVipPrice(apointMentPet1.getItemVipPrice());
                                    apointMentPet.setMyPetId(apointMentPet1.getMyPetId());
                                    apointMentPet.setPetId(apointMentPet1.getPetId());
                                    apointMentPet.setPetImg(apointMentPet1.getPetImg());
                                    apointMentPet.setPetNickName(apointMentPet1.getPetNickName());
                                    apointMentPet.setVipPrice(apointMentPet1.getVipPrice());
                                    apointMentPet.setSetItemDecor(apointMentPet1.isSetItemDecor());
                                    apointMentPet.setServiceId(apointMentPet1.getServiceId());
                                    List<ApointMentItem> itemList = apointMentPet1.getItemList();
                                    if (itemList != null && itemList.size() > 0) {
                                        ArrayList<ApointMentItem> apointMentItems = new ArrayList<>();
                                        for (int j = 0; j < itemList.size(); j++) {
                                            apointMentItems.add(itemList.get(j));
                                        }
                                        apointMentPet.setItemList(apointMentItems);
                                    }
                                    overTimePetList.add(apointMentPet);
                                }
                                for (int i = 0; i < overTimePetList.size(); i++) {
                                    ApointMentPet apointMentPet = overTimePetList.get(i);
                                    if (apointMentPet.getMyPetId() == apointMentPet2.getMyPetId()) {
                                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                                        if (itemList == null) {
                                            itemList = new ArrayList<ApointMentItem>();
                                        }
                                        itemList.add(apointMentItem1);
                                        apointMentPet.setItemList(itemList);
                                        break;
                                    }
                                }
                                myPetIds.clear();
                                myPetIds.add(apointMentPet2.getMyPetId());
                                timeItemList.clear();
                                timeItemList.add(apointMentItem1);
                                itemToMorePet.setApointMentItem(timeItemList);
                                itemToMorePet.setMyPetIds(myPetIds);
                                flag = 1;
                                //如果选择了时间和美容师，需要校验添加这个单项以后，选中的时间还是否可约
                                workerIdList.clear();
                                mPDialog.showDialog();
                                CommUtil.getBeauticianFreeTime(
                                        AppointMentActivity.this,
                                        lat,
                                        lng,
                                        spUtil.getString(
                                                "cellphone", ""),
                                        Global.getIMEI(AppointMentActivity.this),
                                        Global.getCurrentVersion(AppointMentActivity.this),
                                        0, serviceLoc, shopId,
                                        getOverTime_PetID_ServiceId_MyPetId_ItemIds(), 0, appointment, 0, 0,
                                        allWorkerHanler);
                            } else {
                                setOnePetItem();
                            }
                        }
                    } else {
                        showItemPetDialog();
                    }
                }
            }
        });
        appointMentItemAdapter.setOnPetItemClickListener(new AppointMentItemAdapter.OnPetItemClickListener() {
            @Override
            public void OnPetItemClick(int position) {
                if (localItemList.size() > position) {
                    Intent intent = new Intent(AppointMentActivity.this, ItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    ApointMentItem apointMentItem = localItemList.get(position);
                    int id = apointMentItem.getId();
                    for (int i = 0; i < localItemPriceList.size(); i++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
                        if (apointMentItemPrice.getItemId() == id) {
                            bundle.putSerializable("apointMentItemPrice", apointMentItemPrice);
                            break;
                        }
                    }
                    bundle.putSerializable("Item", apointMentItem);
                    bundle.putSerializable("appointWorker", localAppointWorker);
                    bundle.putSerializable("shop", shop);
                    intent.putExtras(bundle);
                    intent.putExtra("petList", (Serializable) petList);
                    intent.putExtra("isVip", isVip);
                    intent.putExtra("serviceCardId", serviceCardId);
                    intent.putExtra("tid", tid);
                    intent.putExtra("lat", lat);
                    intent.putExtra("pickup", pickup);
                    intent.putExtra("lng", lng);
                    intent.putExtra("dayposition", dayposition);
                    intent.putExtra("serviceLoc", serviceLoc);
                    intent.putExtra("appointment", appointment);
                    startActivity(intent);
                }
            }
        });
    }

    private void addGoodToCar() {
        final ImageView view = new ImageView(AppointMentActivity.this);
        view.setImageResource(R.drawable.icon_beisaier);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(56, 56);
        rl_appointment_root.addView(view, layoutParams);

        //二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLoc = new int[2];
        rl_appointment_root.getLocationInWindow(parentLoc);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        beisaierImageView.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        iv_appointment_cart.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLoc[0] + beisaierImageView.getWidth() / 2;
        float startY = startLoc[1] - parentLoc[1] + beisaierImageView.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLoc[0] + iv_appointment_cart.getWidth() / 5;
        float toY = endLoc[1] - parentLoc[1];

        //开始绘制贝塞尔曲线
        Path path = new Path();
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        mPathMeasure = new PathMeasure(path, false);

        //属性动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                view.setTranslationX(mCurrentPosition[0]);
                view.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                rl_appointment_root.removeView(view);
                //shopImg 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(AppointMentActivity.this, R.anim.shop_car_scale);
                iv_appointment_cart.startAnimation(scaleAnim);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private void setOnePetItem() {
        ApointMentPet apointMentPet = petList.get(0);
        List<ApointMentItem> itemList = apointMentPet.getItemList();
        if (itemList == null) {
            itemList = new ArrayList<ApointMentItem>();
        }
        ApointMentItem apointMentItem = new ApointMentItem(apointMentItem1.getPriceSuffix(),
                apointMentItem1.getId(), apointMentItem1.getName(), apointMentItem1.getPic(),
                apointMentItem1.getLabel());
        for (int i = 0; i < localItemPriceList.size(); i++) {
            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
            if (apointMentItemPrice.getItemId() == apointMentItem1.getId()) {
                List<ApointMentItemPrices> itemPriceList =
                        apointMentItemPrice.getItemPriceList();
                for (int j = 0; j < itemPriceList.size(); j++) {
                    ApointMentItemPrices apointMentItemPrices = itemPriceList.get(j);
                    if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                        apointMentItem.setVipPrice10(apointMentItemPrices.getVipPrice10());
                        apointMentItem.setVipPrice20(apointMentItemPrices.getVipPrice20());
                        apointMentItem.setVipPrice30(apointMentItemPrices.getVipPrice30());
                        apointMentItem.setPrice10(apointMentItemPrices.getPrice10());
                        apointMentItem.setPrice20(apointMentItemPrices.getPrice20());
                        apointMentItem.setPrice30(apointMentItemPrices.getPrice30());
                        if (isVip) {
                            if (tid == 0 || tid == 1) {
                                apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                                if (apointMentItemPrices.getVipPrice10() == 0) {
                                    if (apointMentItemPrices.getExtraCardId() <= 0) {
                                        apointMentItem.setShouXi(true);
                                        apointMentItem.setTeethCard(false);
                                    } else {
                                        apointMentItem.setShouXi(false);
                                        apointMentItem.setTeethCard(true);
                                    }
                                }
                            } else if (tid == 2) {
                                apointMentItem.setPrice(apointMentItemPrices.getPrice20());
                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice20());
                                if (apointMentItemPrices.getVipPrice20() == 0) {
                                    if (apointMentItemPrices.getExtraCardId() <= 0) {
                                        apointMentItem.setShouXi(true);
                                        apointMentItem.setTeethCard(false);
                                    } else {
                                        apointMentItem.setShouXi(false);
                                        apointMentItem.setTeethCard(true);
                                    }
                                }
                            } else if (tid == 3) {
                                apointMentItem.setPrice(apointMentItemPrices.getPrice30());
                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice30());
                                if (apointMentItemPrices.getVipPrice30() == 0) {
                                    if (apointMentItemPrices.getExtraCardId() <= 0) {
                                        apointMentItem.setShouXi(true);
                                        apointMentItem.setTeethCard(false);
                                    } else {
                                        apointMentItem.setShouXi(false);
                                        apointMentItem.setTeethCard(true);
                                    }
                                }
                            }
                        } else {
                            if (tid == 0 || tid == 1) {
                                apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                                if (apointMentItemPrices.getPrice10() == 0) {
                                    if (apointMentItemPrices.getExtraCardId() <= 0) {
                                        apointMentItem.setShouXi(true);
                                        apointMentItem.setTeethCard(false);
                                    } else {
                                        apointMentItem.setShouXi(false);
                                        apointMentItem.setTeethCard(true);
                                    }
                                }
                            } else if (tid == 2) {
                                apointMentItem.setPrice(apointMentItemPrices.getPrice20());
                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice20());
                                if (apointMentItemPrices.getPrice20() == 0) {
                                    if (apointMentItemPrices.getExtraCardId() <= 0) {
                                        apointMentItem.setShouXi(true);
                                        apointMentItem.setTeethCard(false);
                                    } else {
                                        apointMentItem.setShouXi(false);
                                        apointMentItem.setTeethCard(true);
                                    }
                                }
                            } else if (tid == 3) {
                                apointMentItem.setPrice(apointMentItemPrices.getPrice30());
                                apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice30());
                                if (apointMentItemPrices.getPrice30() == 0) {
                                    if (apointMentItemPrices.getExtraCardId() <= 0) {
                                        apointMentItem.setShouXi(true);
                                        apointMentItem.setTeethCard(false);
                                    } else {
                                        apointMentItem.setShouXi(false);
                                        apointMentItem.setTeethCard(true);
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        itemList.add(apointMentItem);
        apointMentPet.setItemList(itemList);
        apointMentItem1.setState(4);
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet1 = petList.get(i);
            if (apointMentPet1.getMyPetId() == apointMentPet.getMyPetId()) {
                apointMentPet1.setItemList(apointMentPet.getItemList());
                break;
            }
        }
        setLastPrice();
        appointMentItemAdapter.notifyDataSetChanged();
        addGoodToCar();
    }

    private AsyncHttpResponseHandler allWorkerHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject jdata = jsonObject.getJSONObject("data");
                        if (jdata.has("workers") && !jdata.isNull("workers")) {
                            JSONArray jsonArray = jdata.getJSONArray("workers");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    workerIdList.add(jsonArray.getInt(i));
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(AppointMentActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getSingles()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(AppointMentActivity.this, "数据异常");
                e.printStackTrace();
            }
            boolean isSupport = false;
            if (workerIdList.size() > 0) {
                for (int i = 0; i < workerIdList.size(); i++) {
                    Integer integer = workerIdList.get(i);
                    if (integer == localAppointWorker.getWorkerId()) {
                        isSupport = true;
                        break;
                    }
                }
            }
            if (isSupport) {
                if (flag == 1) {//单宠
                    setOnePetItem();
                } else if (flag == 2) {//多宠
                    setMorePetItem();
                }
            } else {
                showTimeOverDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            Log.e("TAG", "请求失败");
            ToastUtil.showToastShortBottom(AppointMentActivity.this, "请求失败");
        }
    };

    private void showTimeOverDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(AppointMentActivity.this, R.layout.appoint_timeover_bottom_dialog, null);
        TextView tv_appointtimeover_bottomdia_title = (TextView) customView.findViewById(R.id.tv_appointtimeover_bottomdia_title);
        TextView tv_appointtimeover_bottomdia_worker = (TextView) customView.findViewById(R.id.tv_appointtimeover_bottomdia_worker);
        TextView tv_appointtimeover_bottomdia_time = (TextView) customView.findViewById(R.id.tv_appointtimeover_bottomdia_time);
        ImageView iv_appointtimeover_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointtimeover_bottomdia_close);
        ImageView iv_appointtimeover_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointtimeover_bottom_bg);
        tv_appointtimeover_bottomdia_title.setText(localAppointWorker.getRealName() + "当前时间已满");
        tv_appointtimeover_bottomdia_time.setText("预约" + localAppointWorker.getRealName() + "其他时间");
        if (workerIdList.size() > 0) {
            tv_appointtimeover_bottomdia_worker.setBackgroundResource(R.drawable.bg_orange_jianbian);
        } else {
            tv_appointtimeover_bottomdia_worker.setBackgroundResource(R.drawable.bg_bbbbbb);
        }
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(AppointMentActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_appointtimeover_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointtimeover_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        tv_appointtimeover_bottomdia_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择此时间段下的其他美容师
                pWinBottomDialog.dismiss();
                if (workerIdList.size() > 0) {
                    String workerIds = "";
                    for (int i = 0; i < workerIdList.size(); i++) {
                        if (i == workerIdList.size() - 1) {
                            workerIds = workerIds + workerIdList.get(i);
                        } else {
                            workerIds = workerIds + workerIdList.get(i) + ",";
                        }
                    }
                    Intent intent2 = new Intent(AppointMentActivity.this, WorkerListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", shop);
                    bundle.putSerializable("itemToMorePet", itemToMorePet);
                    intent2.putExtras(bundle);
                    intent2.putExtra("lat", lat);
                    intent2.putExtra("lng", lng);
                    intent2.putExtra("serviceCardId", serviceCardId);
                    intent2.putExtra("pickup", pickup);
                    intent2.putExtra("dayposition", dayposition);
                    intent2.putExtra("workerId", workerId);
                    intent2.putExtra("defaultWorkerTag", defaultWorkerTag);
                    intent2.putExtra("workerIds", workerIds);
                    intent2.putExtra("appointment", appointment);
                    intent2.putExtra("serviceLoc", serviceLoc);
                    intent2.putExtra("petList", (Serializable) petList);
                    intent2.putExtra("isVip", isVip);
                    intent2.putExtra("tid", tid);
                    intent2.putExtra("strp", getOverTime_PetID_ServiceId_MyPetId_ItemIds());
                    startActivity(intent2);
                } else {
                    ToastUtil.showToastShortBottom(AppointMentActivity.this, "您当前所选时间暂无可约美容师");
                }
            }
        });
        tv_appointtimeover_bottomdia_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择该美容师的其他时间
                pWinBottomDialog.dismiss();
                Intent intent1 = new Intent(AppointMentActivity.this, AppointTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shop", shop);
                intent1.putExtra("serviceCardId", serviceCardId);
                if (localAppointWorker != null) {
                    bundle.putSerializable("appointWorker", localAppointWorker);
                }
                bundle.putSerializable("itemToMorePet", itemToMorePet);
                intent1.putExtras(bundle);
                intent1.putExtra("lat", lat);
                intent1.putExtra("dayposition", dayposition);
                intent1.putExtra("lng", lng);
                intent1.putExtra("serviceLoc", serviceLoc);
                intent1.putExtra("petList", (Serializable) petList);
                intent1.putExtra("isVip", isVip);
                intent1.putExtra("tid", tid);
                intent1.putExtra("strp", getOverTime_PetID_ServiceId_MyPetId_ItemIds());
                startActivity(intent1);
            }
        });
    }

    private void showItemPetDialog() {
        beforeNum = 0;
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet.isSelect()) {
                beforeNum++;
            }
        }
        ViewGroup customView = (ViewGroup) View.inflate(AppointMentActivity.this, R.layout.appoint_pet_bottom_dialog, null);
        LinearLayout ll_appointpet_bottomdia_wc = (LinearLayout) customView.findViewById(R.id.ll_appointpet_bottomdia_wc);
        RecyclerView rv_appointpet_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpet_bottomdia);
        ImageView iv_appointpet_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpet_bottom_bg);
        rv_appointpet_bottomdia.setHasFixedSize(true);
        rv_appointpet_bottomdia.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        rv_appointpet_bottomdia.addItemDecoration(new GridSpacingItemDecoration(5,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
        for (int i = 0; i < localItemPriceList.size(); i++) {
            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
            if (apointMentItemPrice.getItemId() == apointMentItem1.getId()) {
                List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                if (itemPriceList != null && itemPriceList.size() > 0) {
                    for (int j = 0; j < itemPriceList.size(); j++) {
                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(j);
                        for (int k = 0; k < petList.size(); k++) {
                            ApointMentPet apointMentPet = petList.get(k);
                            if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                if (tid == 0 || tid == 1) {
                                    apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice10());
                                    apointMentPet.setItemPrice(apointMentItemPrices.getPrice10());
                                } else if (tid == 2) {
                                    apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice20());
                                    apointMentPet.setItemPrice(apointMentItemPrices.getPrice20());
                                } else if (tid == 3) {
                                    apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice30());
                                    apointMentPet.setItemPrice(apointMentItemPrices.getPrice30());
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
        appointBottomPetAdapter = new AppointBottomPetAdapter(R.layout.item_pet_bottom, petList);
        rv_appointpet_bottomdia.setAdapter(appointBottomPetAdapter);
        appointBottomPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (petList.size() > position) {
                    if (petList.get(position).getState() == 3 || petList.get(position).getState() == 5) {
                        petList.get(position).setSelect(!petList.get(position).isSelect());
                        appointBottomPetAdapter.notifyDataSetChanged();
                    } else if (petList.get(position).getState() == 2) {
                        if (tid == 0 || tid == 1) {
                            ToastUtil.showToastShortBottom(AppointMentActivity.this, "中级赠送，无法修改");
                        } else if (tid == 2) {
                            ToastUtil.showToastShortBottom(AppointMentActivity.this, "高级赠送，无法修改");
                        } else if (tid == 3) {
                            ToastUtil.showToastShortBottom(AppointMentActivity.this, "首席赠送，无法修改");
                        }
                    } else if (petList.get(position).getState() == 1) {
                        ToastUtil.showToastShortBottom(AppointMentActivity.this, "您的宠物暂不支持添加此单项");
                    }
                }
            }
        });
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(AppointMentActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_appointpet_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        ll_appointpet_bottomdia_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                boolean isSelect = false;
                for (int i = 0; i < petList.size(); i++) {
                    ApointMentPet apointMentPet = petList.get(i);
                    if (apointMentPet.isSelect()) {
                        isSelect = true;
                        break;
                    }
                }
                for (int i = 0; i < petList.size(); i++) {//清空所有添加了这个单项的宠物
                    ApointMentPet apointMentPet = petList.get(i);
                    List<ApointMentItem> itemList = apointMentPet.getItemList();
                    if (itemList != null && itemList.size() > 0) {
                        for (int j = 0; j < itemList.size(); j++) {
                            ApointMentItem apointMentItem = itemList.get(j);
                            if (apointMentItem.getId() == apointMentItem1.getId()) {
                                itemList.remove(j);
                                break;
                            }
                        }
                        apointMentPet.setItemList(itemList);
                    }
                }
                if (isSelect) {
                    if (Utils.isStrNull(appointment) && localAppointWorker != null) {
                        overTimePetList.clear();
                        for (int i = 0; i < petList.size(); i++) {
                            ApointMentPet apointMentPet1 = petList.get(i);
                            ApointMentPet apointMentPet = new ApointMentPet();
                            apointMentPet.setPriceSuffix(apointMentPet1.getPriceSuffix());
                            apointMentPet.setPrice(apointMentPet1.getPrice());
                            apointMentPet.setSelect(apointMentPet1.isSelect());
                            apointMentPet.setItemPrice(apointMentPet1.getItemPrice());
                            apointMentPet.setItemVipPrice(apointMentPet1.getItemVipPrice());
                            apointMentPet.setMyPetId(apointMentPet1.getMyPetId());
                            apointMentPet.setPetId(apointMentPet1.getPetId());
                            apointMentPet.setPetImg(apointMentPet1.getPetImg());
                            apointMentPet.setPetNickName(apointMentPet1.getPetNickName());
                            apointMentPet.setVipPrice(apointMentPet1.getVipPrice());
                            apointMentPet.setSetItemDecor(apointMentPet1.isSetItemDecor());
                            apointMentPet.setServiceId(apointMentPet1.getServiceId());
                            List<ApointMentItem> itemList = apointMentPet1.getItemList();
                            if (itemList != null && itemList.size() > 0) {
                                ArrayList<ApointMentItem> apointMentItems = new ArrayList<>();
                                for (int j = 0; j < itemList.size(); j++) {
                                    apointMentItems.add(itemList.get(j));
                                }
                                apointMentPet.setItemList(apointMentItems);
                            }
                            overTimePetList.add(apointMentPet);
                        }
                        myPetIds.clear();
                        for (int i = 0; i < petList.size(); i++) {
                            ApointMentPet apointMentPet = petList.get(i);
                            if (apointMentPet.isSelect()) {
                                myPetIds.add(apointMentPet.getMyPetId());
                                for (int j = 0; j < overTimePetList.size(); j++) {
                                    ApointMentPet apointMentPet1 = overTimePetList.get(j);
                                    if (apointMentPet1.getMyPetId() == apointMentPet.getMyPetId()) {
                                        List<ApointMentItem> itemList = apointMentPet1.getItemList();
                                        if (itemList == null) {
                                            itemList = new ArrayList<ApointMentItem>();
                                        }
                                        itemList.add(apointMentItem1);
                                        apointMentPet1.setItemList(itemList);
                                    }
                                }
                            }
                        }
                        timeItemList.clear();
                        timeItemList.add(apointMentItem1);
                        itemToMorePet.setApointMentItem(timeItemList);
                        itemToMorePet.setMyPetIds(myPetIds);
                        flag = 2;
                        //如果选择了时间和美容师，需要校验添加这个单项以后，选中的时间还是否可约
                        workerIdList.clear();
                        mPDialog.showDialog();
                        CommUtil.getBeauticianFreeTime(
                                AppointMentActivity.this,
                                lat,
                                lng,
                                spUtil.getString(
                                        "cellphone", ""),
                                Global.getIMEI(AppointMentActivity.this),
                                Global.getCurrentVersion(AppointMentActivity.this),
                                0, serviceLoc, shopId,
                                getOverTime_PetID_ServiceId_MyPetId_ItemIds(), 0, appointment, 0, 0,
                                allWorkerHanler);
                    } else {
                        setMorePetItem();
                    }
                } else {
                    setMorePetItem();
                }
            }
        });
    }

    private void setMorePetItem() {
        int num = 0;
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet.isSelect()) {
                num++;
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                if (itemList == null) {
                    itemList = new ArrayList<ApointMentItem>();
                }
                itemList.add(new ApointMentItem(apointMentItem1.getPriceSuffix(),
                        apointMentItem1.getId(), apointMentItem1.getName(),
                        apointMentPet.getItemVipPrice(), apointMentPet.getItemPrice(),
                        apointMentItem1.getPic(), apointMentItem1.getLabel()));
                apointMentPet.setItemList(itemList);
            }
        }
        setItemAndPetData();
        setLastPrice();
        Log.e("TAG", "beforeNum = " + beforeNum);
        if (num > beforeNum) {
            addGoodToCar();
        }
    }

    private void getData() {
        getRechargeBanner();
        getWorkers();
        getItems();
    }

    // 加载美容师
    private void getWorkers() {
        availableWorkerList.clear();
        unAvailableWorkerList.clear();
        mPDialog.showDialog();
        String workerIds = null;
        if (workerId > 0) {
            workerIds = String.valueOf(workerId);
        }
        CommUtil.querySelectedWorkers(this, appointment, serviceLoc,
                shopId, getPetID_ServiceId_MyPetId_ItemIds(), workerIds,
                lat, lng, serviceCardId, querySelectedWorkersHandler);
    }

    private AsyncHttpResponseHandler querySelectedWorkersHandler = new AsyncHttpResponseHandler() {

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
                            if (jdata.has("available") && !jdata.isNull("available")) {
                                JSONArray jarravailable = jdata.getJSONArray("available");
                                if (jarravailable.length() > 0) {
                                    for (int i = 0; i < jarravailable.length(); i++) {
                                        availableWorkerList.add(AppointWorker.json2Entity(jarravailable
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                            if (jdata.has("unavailable") && !jdata.isNull("unavailable")) {
                                JSONArray jarrunavailable = jdata.getJSONArray("unavailable");
                                if (jarrunavailable.length() > 0) {
                                    for (int i = 0; i < jarrunavailable.length(); i++) {
                                        unAvailableWorkerList.add(AppointWorker.json2Entity(jarrunavailable
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(AppointMentActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getWorkers()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(AppointMentActivity.this,
                        "数据异常");
                e.printStackTrace();
            }
            if (workerId > 0) {
                if (availableWorkerList.size() > 0) {
                    for (int i = 0; i < availableWorkerList.size(); i++) {
                        AppointWorker appointWorker = availableWorkerList.get(i);
                        if (appointWorker.getWorkerId() == workerId) {
                            tid = appointWorker.getTid();
                            if (localAppointWorker == null) {
                                localAppointWorker = new AppointWorker();
                            }
                            localAppointWorker.setGoodRate(appointWorker.getGoodRate());
                            localAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
                            localAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
                            localAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
                            localAppointWorker.setAvatar(appointWorker.getAvatar());
                            localAppointWorker.setDefaultWorkerTag(appointWorker.getDefaultWorkerTag());
                            localAppointWorker.setWorkerId(appointWorker.getWorkerId());
                            localAppointWorker.setRealName(appointWorker.getRealName());
                            localAppointWorker.setTag(appointWorker.getTag());
                            localAppointWorker.setTid(appointWorker.getTid());
                            localAppointWorker.setEarliest(appointWorker.getEarliest());
                            localAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
                            List<AppointWorkerPrice> priceLiest = appointWorker.getPriceLiest();
                            if (priceLiest != null && priceLiest.size() > 0) {
                                for (int x = 0; x < priceLiest.size(); x++) {
                                    AppointWorkerPrice appointWorkerPrice = priceLiest.get(x);
                                    for (int j = 0; j < petList.size(); j++) {
                                        ApointMentPet apointMentPet = petList.get(j);
                                        if (appointWorkerPrice.getMyPetId() == apointMentPet.getMyPetId()) {
                                            apointMentPet.setPrice(appointWorkerPrice.getPrice());
                                            apointMentPet.setVipPrice(appointWorkerPrice.getVip_price());
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                if (unAvailableWorkerList.size() > 0) {
                    for (int i = 0; i < unAvailableWorkerList.size(); i++) {
                        AppointWorker appointWorker = unAvailableWorkerList.get(i);
                        if (appointWorker.getWorkerId() == workerId) {
                            tid = appointWorker.getTid();
                            if (localAppointWorker == null) {
                                localAppointWorker = new AppointWorker();
                            }
                            localAppointWorker.setGoodRate(appointWorker.getGoodRate());
                            localAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
                            localAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
                            localAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
                            localAppointWorker.setAvatar(appointWorker.getAvatar());
                            localAppointWorker.setDefaultWorkerTag(appointWorker.getDefaultWorkerTag());
                            localAppointWorker.setWorkerId(appointWorker.getWorkerId());
                            localAppointWorker.setRealName(appointWorker.getRealName());
                            localAppointWorker.setTag(appointWorker.getTag());
                            localAppointWorker.setTid(appointWorker.getTid());
                            localAppointWorker.setEarliest(appointWorker.getEarliest());
                            localAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
                            List<AppointWorkerPrice> priceLiest = appointWorker.getPriceLiest();
                            if (priceLiest != null && priceLiest.size() > 0) {
                                for (int x = 0; x < priceLiest.size(); x++) {
                                    AppointWorkerPrice appointWorkerPrice = priceLiest.get(x);
                                    for (int j = 0; j < petList.size(); j++) {
                                        ApointMentPet apointMentPet = petList.get(j);
                                        if (appointWorkerPrice.getMyPetId() == apointMentPet.getMyPetId()) {
                                            apointMentPet.setPrice(appointWorkerPrice.getPrice());
                                            apointMentPet.setVipPrice(appointWorkerPrice.getVip_price());
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            } else {
                if (availableWorkerList.size() > 0) {
                    int minTid = availableWorkerList.get(0).getTid();
                    for (int i = 0; i < availableWorkerList.size(); i++) {
                        AppointWorker appointWorker = availableWorkerList.get(i);
                        if (appointWorker != null) {
                            if (minTid > appointWorker.getTid()) {
                                minTid = appointWorker.getTid();
                            }
                        }
                    }
                    Log.e("TAG", "minTid = " + minTid);
                    for (int i = 0; i < availableWorkerList.size(); i++) {
                        AppointWorker appointWorker = availableWorkerList.get(i);
                        if (appointWorker != null && appointWorker.getTid() == minTid) {
                            tid = appointWorker.getTid();
                            List<AppointWorkerPrice> priceLiest = appointWorker.getPriceLiest();
                            if (priceLiest != null && priceLiest.size() > 0) {
                                for (int x = 0; x < priceLiest.size(); x++) {
                                    AppointWorkerPrice appointWorkerPrice = priceLiest.get(x);
                                    for (int j = 0; j < petList.size(); j++) {
                                        ApointMentPet apointMentPet = petList.get(j);
                                        if (appointWorkerPrice.getMyPetId() == apointMentPet.getMyPetId()) {
                                            apointMentPet.setPrice(appointWorkerPrice.getPrice());
                                            apointMentPet.setVipPrice(appointWorkerPrice.getVip_price());
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                } else if (unAvailableWorkerList.size() > 0) {
                    int minTid = unAvailableWorkerList.get(0).getTid();
                    for (int i = 0; i < unAvailableWorkerList.size(); i++) {
                        AppointWorker appointWorker = unAvailableWorkerList.get(i);
                        if (appointWorker != null) {
                            if (minTid > appointWorker.getTid()) {
                                minTid = appointWorker.getTid();
                            }
                        }
                    }
                    Log.e("TAG", "minTid = " + minTid);
                    for (int i = 0; i < unAvailableWorkerList.size(); i++) {
                        AppointWorker appointWorker = unAvailableWorkerList.get(i);
                        if (appointWorker != null && appointWorker.getTid() == minTid) {
                            tid = appointWorker.getTid();
                            List<AppointWorkerPrice> priceLiest = appointWorker.getPriceLiest();
                            if (priceLiest != null && priceLiest.size() > 0) {
                                for (int x = 0; x < priceLiest.size(); x++) {
                                    AppointWorkerPrice appointWorkerPrice = priceLiest.get(x);
                                    for (int j = 0; j < petList.size(); j++) {
                                        ApointMentPet apointMentPet = petList.get(j);
                                        if (appointWorkerPrice.getMyPetId() == apointMentPet.getMyPetId()) {
                                            apointMentPet.setPrice(appointWorkerPrice.getPrice());
                                            apointMentPet.setVipPrice(appointWorkerPrice.getVip_price());
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            setItemAndPetData();
            setLastPrice();
            appointMentItemAdapter.notifyDataSetChanged();
            appointMentPetAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(AppointMentActivity.this, "请求失败");
            mPDialog.closeDialog();
        }
    };

    private void setLastPrice() {
        Log.e("TAG", "setLastPrice");
        lastVipPrice = 0;
        int itemSize = petList.size();
        for (int i = 0; i < petList.size(); i++) {//基础服务价格
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet != null) {
                lastVipPrice = ComputeUtil.add(lastVipPrice, apointMentPet.getVipPrice());
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                if (itemList != null && itemList.size() > 0) {//单项服务价格
                    int itemNum = 0;
                    for (int j = 0; j < itemList.size(); j++) {
                        ApointMentItem apointMentItem = itemList.get(j);
                        if (isVip) {
                            if (apointMentItem.getVipPrice() > 0 || (apointMentItem.getVipPrice() == 0 && apointMentItem.isTeethCard())) {
                                itemNum++;
                            }
                        } else {
                            if (apointMentItem.getPrice() > 0 || (apointMentItem.getPrice() == 0 && apointMentItem.isTeethCard())) {
                                itemNum++;
                            }
                        }
                    }
                    itemSize += itemNum;
                    lastVipPrice = ComputeUtil.add(lastVipPrice, getItemPrice(itemList, true));
                }
            }
        }
        lastPrice = 0;
        for (int i = 0; i < petList.size(); i++) {//基础服务价格
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet != null) {
                lastPrice = ComputeUtil.add(lastPrice, apointMentPet.getPrice());
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                if (itemList != null && itemList.size() > 0) {//单项服务价格
                    lastPrice = ComputeUtil.add(lastPrice, getItemPrice(itemList, false));
                }
            }
        }
        Log.e("TAG", "isSelectPickup = " + isSelectPickup);
        Log.e("TAG", "pickupPrice = " + pickupPrice);
        Log.e("TAG", "pickup = " + pickup);
        if (serviceLoc == 1 && pickup == 1 && isSelectPickup) {
            Log.e("TAG", "加接送价格");
            lastVipPrice = ComputeUtil.add(lastVipPrice, pickupPrice);
            lastPrice = ComputeUtil.add(lastPrice, pickupPrice);
        }
        tvAppointmentNum.setText("" + itemSize);
        if (recommendServiceCard != null) {
            double discountPrice = 0;
            if (serviceCardId > 0) {
                setFXPrice(lastVipPrice);
                Utils.setText(tvAppointmentPayprice, "¥" + lastVipPrice, "", View.VISIBLE, View.INVISIBLE);
                tv_appointment_payprice_yj.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_appointment_payprice_yj.setVisibility(View.VISIBLE);
                Utils.setText(tv_appointment_payprice_yj, "¥" + lastPrice, "", View.VISIBLE, View.INVISIBLE);
                discountPrice = ComputeUtil.sub(lastPrice, lastVipPrice);
            } else {
                setFXPrice(lastPrice);
                Utils.setText(tvAppointmentPayprice, "¥" + lastPrice, "", View.VISIBLE, View.INVISIBLE);
                tv_appointment_payprice_yj.setVisibility(View.GONE);
                for (int i = 0; i < petList.size(); i++) {//基础服务价格
                    ApointMentPet apointMentPet = petList.get(i);
                    if (apointMentPet != null) {
                        discountPrice = ComputeUtil.add(discountPrice, ComputeUtil.mul(apointMentPet.getPrice(), ComputeUtil.sub(1, discount)));
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {//单项服务价格
                            discountPrice = ComputeUtil.add(discountPrice, getItemDiscountPrice(itemList));
                        }
                    }
                }
            }
            Utils.setText(tv_appointment_carddiscountprice, "¥" + discountPrice, "", View.VISIBLE, View.VISIBLE);
        } else {
            setFXPrice(lastPrice);
            Utils.setText(tvAppointmentPayprice, "¥" + lastPrice, "", View.VISIBLE, View.INVISIBLE);
            tv_appointment_payprice_yj.setVisibility(View.GONE);
        }
    }

    private void setFXPrice(double price) {
        if (recommmendCashBack != null && recommmendCashBack.getCashBackIsOpen() == 1) {
            tv_appointment_fxprice.setVisibility(View.VISIBLE);
            double fx_price = ComputeUtil.mul(price, recommmendCashBack.getGrainGoldRate());
            tv_appointment_fxprice.setText(recommmendCashBack.getTip().replace("%s", String.valueOf(fx_price)));
        } else {
            tv_appointment_fxprice.setVisibility(View.GONE);
        }
    }

    private double getItemDiscountPrice(List<ApointMentItem> itemList) {
        double itemDiscountPrice = 0;
        for (int i = 0; i < itemList.size(); i++) {//基础服务
            ApointMentItem apointMentItem = itemList.get(i);
            if (apointMentItem != null) {
                itemDiscountPrice = ComputeUtil.add(itemDiscountPrice, ComputeUtil.mul(apointMentItem.getPrice(), ComputeUtil.sub(1, discount)));
            }
        }
        return itemDiscountPrice;
    }

    private double getItemPrice(List<ApointMentItem> itemList, boolean flag) {
        double itemPrice = 0;
        for (int i = 0; i < itemList.size(); i++) {//基础服务
            ApointMentItem apointMentItem = itemList.get(i);
            if (apointMentItem != null) {
                if (flag) {
                    itemPrice = ComputeUtil.add(itemPrice, apointMentItem.getVipPrice());
                } else {
                    itemPrice = ComputeUtil.add(itemPrice, apointMentItem.getPrice());
                }
            }
        }
        return itemPrice;
    }

    private void getRechargeBanner() {
        mPDialog.showDialog();
        bannerList.clear();
        CommUtil.operateBanner(this, 2, operateBannerHandler);
    }

    private AsyncHttpResponseHandler operateBannerHandler = new AsyncHttpResponseHandler() {

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
                            if (jdata.has("title") && !jdata.isNull("title")) {
                                String title = jdata.getString("title");
                                Utils.setText(tvAppointmenBannertitle, title, "", View.VISIBLE, View.GONE);
                            }
                            if (jdata.has("operateBannerList") && !jdata.isNull("operateBannerList")) {
                                JSONArray jarroperateBannerList = jdata.getJSONArray("operateBannerList");
                                if (jarroperateBannerList.length() > 0) {
                                    for (int i = 0; i < jarroperateBannerList.length(); i++) {
                                        bannerList.add(RechargeBanner.json2Entity(jarroperateBannerList
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(AppointMentActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "getRechargeBanner()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(AppointMentActivity.this, "数据异常");
            }
            if (bannerList != null && bannerList.size() > 0) {
                llAppointmenBanner.setVisibility(View.VISIBLE);
                appointMentBannerAdapter.notifyDataSetChanged();
            } else {
                llAppointmenBanner.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortBottom(AppointMentActivity.this, "请求失败");
        }
    };

    // 加载单项
    private void getItems() {
        localItemList.clear();
        localItemPriceList.clear();
        tempLocalItemList.clear();
        tempLocalItemPriceList.clear();
        bindCardTitle = "";
        bindCardContent = "";
        mPDialog.showDialog();
        CommUtil.queryExtraItems(this, shopId, serviceLoc, getPetID_ServiceId_MyPetId_ItemIds(), serviceCardId, appointment,
                queryExtraItemsHandler);
    }

    private String getPetID_ServiceId_MyPetId_ItemIds() {
        StringBuffer sb = new StringBuffer();
        if (petList != null && petList.size() > 0) {
            for (int i = 0; i < petList.size(); i++) {
                ApointMentPet apointMentPet = petList.get(i);
                if (apointMentPet != null) {
                    if (i < petList.size() - 1) {
                        sb.append(apointMentPet.getPetId());
                        sb.append("_");
                        sb.append(apointMentPet.getServiceId());
                        sb.append("_");
                        sb.append(apointMentPet.getMyPetId());
                        sb.append("_");
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            sb.append(getItemIds(itemList));
                        } else {
                            sb.append("0");
                        }
                        sb.append("_");
                    } else {
                        sb.append(apointMentPet.getPetId());
                        sb.append("_");
                        sb.append(apointMentPet.getServiceId());
                        sb.append("_");
                        sb.append(apointMentPet.getMyPetId());
                        sb.append("_");
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            sb.append(getItemIds(itemList));
                        } else {
                            sb.append("0");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private String getItemIds(List<ApointMentItem> itemList) {
        StringBuffer sb = new StringBuffer();
        if (itemList != null && itemList.size() > 0) {
            List<ApointMentItem> itemList1 = new ArrayList<ApointMentItem>();
            itemList1.clear();
            for (int i = 0; i < itemList.size(); i++) {
                ApointMentItem apointMentItem = itemList.get(i);
                if (apointMentItem != null && !apointMentItem.isShouXi()) {
                    itemList1.add(apointMentItem);
                }
            }
            if (itemList1 != null && itemList1.size() > 0) {
                for (int i = 0; i < itemList1.size(); i++) {
                    ApointMentItem apointMentItem = itemList1.get(i);
                    if (apointMentItem != null) {
                        if (i < itemList1.size() - 1) {
                            sb.append(apointMentItem.getId());
                            sb.append(",");
                        } else {
                            sb.append(apointMentItem.getId());
                        }
                    }
                }
            } else {
                sb.append("0");
            }
        } else {
            sb.append("0");
        }
        return sb.toString();
    }

    private String getOverTime_PetID_ServiceId_MyPetId_ItemIds() {
        StringBuffer sb = new StringBuffer();
        if (overTimePetList != null && overTimePetList.size() > 0) {
            for (int i = 0; i < overTimePetList.size(); i++) {
                ApointMentPet apointMentPet = overTimePetList.get(i);
                if (apointMentPet != null) {
                    if (i < overTimePetList.size() - 1) {
                        sb.append(apointMentPet.getPetId());
                        sb.append("_");
                        sb.append(apointMentPet.getServiceId());
                        sb.append("_");
                        sb.append(apointMentPet.getMyPetId());
                        sb.append("_");
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            sb.append(getItemIds(itemList));
                        } else {
                            sb.append("0");
                        }
                        sb.append("_");
                    } else {
                        sb.append(apointMentPet.getPetId());
                        sb.append("_");
                        sb.append(apointMentPet.getServiceId());
                        sb.append("_");
                        sb.append(apointMentPet.getMyPetId());
                        sb.append("_");
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            sb.append(getItemIds(itemList));
                        } else {
                            sb.append("0");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private AsyncHttpResponseHandler queryExtraItemsHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject jdata = jsonObject.getJSONObject("data");
                        if (jdata.has("bindCardPetTip") && !jdata.isNull("bindCardPetTip")) {
                            JSONObject jbindCardPetTip = jdata.getJSONObject("bindCardPetTip");
                            if (jbindCardPetTip.has("title") && !jbindCardPetTip.isNull("title")) {
                                bindCardTitle = jbindCardPetTip.getString("title");
                            }
                            if (jbindCardPetTip.has("content") && !jbindCardPetTip.isNull("content")) {
                                bindCardContent = jbindCardPetTip.getString("content");
                            }
                        }
                        if (jdata.has("items") && !jdata.isNull("items")) {
                            JSONArray jsonArray = jdata.getJSONArray("items");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    localItemList.add(ApointMentItem
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                    tempLocalItemList.add(ApointMentItem
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                        }
                        if (jdata.has("prices") && !jdata.isNull("prices")) {
                            JSONArray jsonArray = jdata.getJSONArray("prices");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    localItemPriceList.add(ApointMentItemPrice
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                    tempLocalItemPriceList.add(ApointMentItemPrice
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastShortBottom(
                                AppointMentActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getItems()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(AppointMentActivity.this,
                        "数据异常");
                e.printStackTrace();
            }
            if (localItemList.size() > 0 && itemIds != null && itemIds.size() > 0) {
                //再来一单选中单项逻辑
                for (int x = 0; x < petList.size(); x++) {
                    ApointMentPet apointMentPet = petList.get(x);
                    List<ApointMentItem> itemList = apointMentPet.getItemList();
                    if (itemList == null) {
                        itemList = new ArrayList<ApointMentItem>();
                    }
                    for (int i = 0; i < itemIds.size(); i++) {
                        Integer integer = itemIds.get(i);
                        for (int j = 0; j < localItemList.size(); j++) {
                            ApointMentItem apointMentItem = localItemList.get(j);
                            if (apointMentItem.getId() == integer) {
                                itemList.add(apointMentItem);
                            }
                        }
                    }
                    apointMentPet.setItemList(itemList);
                }
                itemIds = null;
            }
            if (localItemList.size() > 0) {
                Log.e("TAG", "VISIBLE");
                llAppointmenItem
                        .setVisibility(View.VISIBLE);
                appointMentItemAdapter.setTid(tid);
                boolean isPerformTeethCard = spUtil.getBoolean("isPerformTeethCard", false);
                if (!isPerformTeethCard && Utils.isStrNull(bindCardTitle) && Utils.isStrNull(bindCardContent)) {
                    spUtil.saveBoolean("isPerformTeethCard", true);
                    new AlertDialogDefault(mContext).builder()
                            .setTitle(bindCardTitle).setMsg(bindCardContent).isOneBtn(true).setPositiveButton("我知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            } else {
                Log.e("TAG", "GONE");
                llAppointmenItem
                        .setVisibility(View.GONE);
                //添加过单项的宠物全部删除添加的单项
                for (int i = 0; i < petList.size(); i++) {
                    petList.get(i).setItemList(null);
                }
            }
            setItemAndPetData();
            isFirstAddToothCardItem = false;
            setLastPrice();
            if (switchWorkerAndTime != null) {
                setTimeAndWorker(new WorkerAndTimeEvent(switchWorkerAndTime.getAppointWorker(),
                        switchWorkerAndTime.getAppointment(), switchWorkerAndTime.getDayposition(),
                        switchWorkerAndTime.getPickup(), switchWorkerAndTime.getItemToMorePet(), switchWorkerAndTime.getWorkerIds()));
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortBottom(AppointMentActivity.this, "请求失败");
            mPDialog.closeDialog();
        }
    };


    private void setItemAndPetData() {
        if (localItemList.size() > 0) {
            //初始化单项信息
            for (int j = 0; j < tempLocalItemList.size(); j++) {
                ApointMentItem apointMentItem1 = tempLocalItemList.get(j);
                boolean isAdd = false;
                for (int i = 0; i < localItemList.size(); i++) {
                    ApointMentItem apointMentItem = localItemList.get(i);
                    if (apointMentItem.getId() == apointMentItem1.getId()) {
                        isAdd = true;
                        break;
                    }
                }
                if (!isAdd) {
                    localItemList.add(apointMentItem1);
                }
            }
            for (int j = 0; j < tempLocalItemPriceList.size(); j++) {
                ApointMentItemPrice apointMentItemPrice1 = tempLocalItemPriceList.get(j);
                boolean isAdd = false;
                for (int i = 0; i < localItemPriceList.size(); i++) {
                    ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(i);
                    if (apointMentItemPrice.getItemId() == apointMentItemPrice1.getItemId()) {
                        isAdd = true;
                        break;
                    }
                }
                if (!isAdd) {
                    localItemPriceList.add(apointMentItemPrice1);
                }
            }
        } else {
            if (tempLocalItemList.size() > 0) {
                localItemList.addAll(tempLocalItemList);
            }
            if (tempLocalItemPriceList.size() > 0) {
                localItemPriceList.addAll(tempLocalItemPriceList);
            }
        }
        setPetData();
        setItemData();
        shouXiItemList.clear();
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (int j = 0; j < itemList.size(); j++) {
                    ApointMentItem apointMentItem = itemList.get(j);
                    if (isVip) {
                        if (apointMentItem.isShouXi()) {
                            boolean isAdd = false;
                            if (shouXiItemList.size() > 0) {
                                for (int k = 0; k < shouXiItemList.size(); k++) {
                                    ApointMentItem apointMentItem2 = shouXiItemList.get(k);
                                    if (apointMentItem2.getId() == apointMentItem.getId()) {
                                        isAdd = true;
                                        break;
                                    }
                                }
                                if (!isAdd) {
                                    shouXiItemList.add(apointMentItem);
                                }
                            } else {
                                shouXiItemList.add(apointMentItem);
                            }
                        }
                    } else {
                        if (apointMentItem.isShouXi()) {
                            boolean isAdd = false;
                            if (shouXiItemList.size() > 0) {
                                for (int k = 0; k < shouXiItemList.size(); k++) {
                                    ApointMentItem apointMentItem2 = shouXiItemList.get(k);
                                    if (apointMentItem2.getId() == apointMentItem.getId()) {
                                        isAdd = true;
                                        break;
                                    }
                                }
                                if (!isAdd) {
                                    shouXiItemList.add(apointMentItem);
                                }
                            } else {
                                shouXiItemList.add(apointMentItem);
                            }
                        }
                    }
                }
            }
        }
        if (shouXiItemList.size() > 0) {
            for (int i = 0; i < shouXiItemList.size(); i++) {
                ApointMentItem apointMentItem = shouXiItemList.get(i);
                List<Integer> petKindList = apointMentItem.getPetKindList();
                for (int j = 0; j < petList.size(); j++) {
                    ApointMentPet apointMentPet = petList.get(j);
                    List<ApointMentItem> itemList = apointMentPet.getItemList();
                    if (itemList != null && itemList.size() > 0) {
                        for (int k = 0; k < itemList.size(); k++) {
                            ApointMentItem apointMentItem1 = itemList.get(k);
                            if (apointMentItem1.getId() == apointMentItem.getId()) {
                                if (petKindList == null) {
                                    petKindList = new ArrayList<Integer>();
                                }
                                petKindList.add(apointMentPet.getPetKind());
                                apointMentItem.setPetKindList(petKindList);
                            }
                        }
                    }
                }
            }
            Log.e("TAG", "shouXiItemList.size() = " + shouXiItemList.size());
            Log.e("TAG", "shouXiItemList.toString() = " + shouXiItemList.toString());
            Log.e("TAG", "petList.toString() = " + petList.toString());
            for (int i = 0; i < shouXiItemList.size(); i++) {
                ApointMentItem apointMentItem = shouXiItemList.get(i);
                List<Integer> petKindList = apointMentItem.getPetKindList();
                if (petKindList != null && petKindList.size() > 0) {
                    apointMentItem.setPetKindList(new ArrayList<Integer>(new HashSet<Integer>(petKindList)));
                }
            }
            for (int i = 0; i < shouXiItemList.size(); i++) {
                ApointMentItem apointMentItem = shouXiItemList.get(i);
                for (int j = 0; j < localItemList.size(); j++) {
                    ApointMentItem apointMentItem1 = localItemList.get(j);
                    if (apointMentItem.getId() == apointMentItem1.getId()) {
                        apointMentItem.setName(apointMentItem1.getName());
                        apointMentItem.setPic(apointMentItem1.getPic());
                    }
                }
            }
            Log.e("TAG", "shouXiItemList.size() = " + shouXiItemList.size());
            Log.e("TAG", "shouXiItemList.toString() = " + shouXiItemList.toString());
        }
        isCanShouXiItemDialog = false;
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet.getServiceId() == 1 || apointMentPet.getServiceId() == 3) {//洗澡
                isCanShouXiItemDialog = true;
                break;
            }
        }
        if (Utils.isStrNull(appointment) && workerId > 0 && shouXiItemList.size() > 0 && !isShouXiItemDialogShow && isCanShouXiItemDialog) {
            isShouXiItemDialogShow = true;
            showShouXiItemDialog();
        }
    }

    private void setPetData() {
        for (int i = 0; i < localItemList.size(); i++) {
            ApointMentItem apointMentItem = localItemList.get(i);
            for (int j = 0; j < localItemPriceList.size(); j++) {
                ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                if (apointMentItem.getId() == apointMentItemPrice.getItemId() && itemPriceList != null
                        && itemPriceList.size() > 0) {
                    List<Integer> availableMyPets = apointMentItem.getAvailableMyPets();
                    if (availableMyPets == null) {
                        availableMyPets = new ArrayList<Integer>();
                    }
                    availableMyPets.clear();
                    for (int k = 0; k < itemPriceList.size(); k++) {
                        Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                        if (apointMentItemPrices != null) {
                            if (isVip) {
                                if (tid == 0) {
                                    if (apointMentItemPrices.getVipPrice10() >= 0 || apointMentItemPrices.getVipPrice20() >= 0 || apointMentItemPrices.getVipPrice30() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    }
                                } else {
                                    if (tid == 1 && apointMentItemPrices.getVipPrice10() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    } else if (tid == 2 && apointMentItemPrices.getVipPrice20() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    } else if (tid == 3 && apointMentItemPrices.getVipPrice30() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    }
                                }
                            } else {
                                if (tid == 0) {
                                    if (apointMentItemPrices.getPrice10() >= 0 || apointMentItemPrices.getPrice20() >= 0 || apointMentItemPrices.getPrice30() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    }
                                } else {
                                    if (tid == 1 && apointMentItemPrices.getPrice10() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    } else if (tid == 2 && apointMentItemPrices.getPrice20() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    } else if (tid == 3 && apointMentItemPrices.getPrice30() >= 0) {
                                        availableMyPets.add(itemPriceList.get(k).getMyPetId());
                                    }
                                }
                            }
                        }
                    }
                    apointMentItem.setAvailableMyPets(availableMyPets);
                }
            }
        }
        //循环出不支持的单项，打标
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (int j = 0; j < itemList.size(); j++) {
                    boolean isSupport = false;
                    ApointMentItem apointMentItem = itemList.get(j);
                    for (int k = 0; k < localItemList.size(); k++) {
                        ApointMentItem apointMentItem1 = localItemList.get(k);
                        if (apointMentItem1.getId() == apointMentItem.getId()) {
                            isSupport = true;
                            break;
                        }
                    }
                    if (!isSupport) {
                        apointMentItem.setDelete1(true);
                    }
                }
            }
        }
        //循环删除不支持的单项
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                Iterator<ApointMentItem> it = itemList.iterator();
                while (it.hasNext()) {
                    ApointMentItem apointMentItem = it.next();
                    if (apointMentItem != null && apointMentItem.isDelete1()) {
                        it.remove();
                    }
                }
            }
        }
        //继续循环出不支持的单项，打标
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (int j = 0; j < itemList.size(); j++) {
                    boolean isSupport = false;
                    ApointMentItem apointMentItem = itemList.get(j);
                    for (int k = 0; k < localItemList.size(); k++) {
                        ApointMentItem apointMentItem1 = localItemList.get(k);
                        if (apointMentItem1.getId() == apointMentItem.getId()) {
                            List<Integer> availableMyPets = apointMentItem1.getAvailableMyPets();
                            if (availableMyPets != null && availableMyPets.size() > 0) {
                                for (int x = 0; x < availableMyPets.size(); x++) {
                                    Integer integer = availableMyPets.get(x);
                                    if (integer == apointMentPet.getMyPetId()) {
                                        isSupport = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!isSupport) {
                        apointMentItem.setDelete1(true);
                    }
                }
            }
        }
        //继续循环删除不支持的单项
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                Iterator<ApointMentItem> it = itemList.iterator();
                while (it.hasNext()) {
                    ApointMentItem apointMentItem = it.next();
                    if (apointMentItem != null && apointMentItem.isDelete1()) {
                        it.remove();
                    }
                }
            }
        }
        //切换等级之后，之前赠送的单项需要剔除
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (int j = 0; j < itemList.size(); j++) {
                    ApointMentItem apointMentItem1 = itemList.get(j);
                    if (apointMentItem1.isShouXi() || apointMentItem1.isTeethCard()) {//说明这个单项是赠送的
                        if (isVip) {
                            if (tid == 0 || tid == 1) {
                                if (apointMentItem1.getVipPrice10() > 0) {
                                    apointMentItem1.setDelete1(true);
                                }
                            } else if (tid == 2) {
                                if (apointMentItem1.getVipPrice20() > 0) {
                                    apointMentItem1.setDelete1(true);
                                }
                            } else if (tid == 3) {
                                if (apointMentItem1.getVipPrice30() > 0) {
                                    apointMentItem1.setDelete1(true);
                                }
                            }
                        } else {
                            if (tid == 0 || tid == 1) {
                                if (apointMentItem1.getPrice10() > 0) {
                                    apointMentItem1.setDelete1(true);
                                }
                            } else if (tid == 2) {
                                if (apointMentItem1.getPrice20() > 0) {
                                    apointMentItem1.setDelete1(true);
                                }
                            } else if (tid == 3) {
                                if (apointMentItem1.getPrice30() > 0) {
                                    apointMentItem1.setDelete1(true);
                                }
                            }
                        }
                    }
                }
            }
        }
        //继续循环删除不赠送的单项
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                Iterator<ApointMentItem> it = itemList.iterator();
                while (it.hasNext()) {
                    ApointMentItem apointMentItem = it.next();
                    if (apointMentItem != null && apointMentItem.isDelete1()) {
                        it.remove();
                    }
                }
            }
        }
        //更新宠物添加的单项数据
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null) {
                if (itemList.size() > 0) {
                    shouXiItemList1.clear();
                    for (int j = 0; j < itemList.size(); j++) {
                        ApointMentItem apointMentItem1 = itemList.get(j);
                        for (int x = 0; x < localItemPriceList.size(); x++) {
                            ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(x);
                            List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                            if (itemPriceList != null && itemPriceList.size() > 0) {
                                if (apointMentItem1.getId() == apointMentItemPrice.getItemId()) {
                                    for (int k = 0; k < itemPriceList.size(); k++) {
                                        Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                        if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                            apointMentItem1.setVipPrice10(apointMentItemPrices.getVipPrice10());
                                            apointMentItem1.setVipPrice20(apointMentItemPrices.getVipPrice20());
                                            apointMentItem1.setVipPrice30(apointMentItemPrices.getVipPrice30());
                                            apointMentItem1.setPrice10(apointMentItemPrices.getPrice10());
                                            apointMentItem1.setPrice20(apointMentItemPrices.getPrice20());
                                            apointMentItem1.setPrice30(apointMentItemPrices.getPrice30());
                                            if (isVip) {
                                                if (tid == 0 || tid == 1) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice10());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice10());
                                                    if (apointMentItemPrices.getVipPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice20());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice20());
                                                    if (apointMentItemPrices.getVipPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice30());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice30());
                                                    if (apointMentItemPrices.getVipPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (tid == 0 || tid == 1) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice10());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice10());
                                                    if (apointMentItemPrices.getPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice20());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice20());
                                                    if (apointMentItemPrices.getPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    apointMentItem1.setPrice(apointMentItemPrices.getPrice30());
                                                    apointMentItem1.setVipPrice(apointMentItemPrices.getVipPrice30());
                                                    if (apointMentItemPrices.getPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() <= 0) {
                                                            apointMentItem1.setShouXi(true);
                                                            apointMentItem1.setTeethCard(false);
                                                        } else {
                                                            apointMentItem1.setShouXi(false);
                                                            apointMentItem1.setTeethCard(true);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    for (int k = 0; k < itemPriceList.size(); k++) {
                                        Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                        if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                            if (isVip) {
                                                if (tid == 0 || tid == 1) {
                                                    if (apointMentItemPrices.getVipPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(),
                                                                        apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(),
                                                                    apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    if (apointMentItemPrices.getVipPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    if (apointMentItemPrices.getVipPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (tid == 0 || tid == 1) {
                                                    if (apointMentItemPrices.getPrice10() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 2) {
                                                    if (apointMentItemPrices.getPrice20() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                } else if (tid == 3) {
                                                    if (apointMentItemPrices.getPrice30() == 0) {
                                                        if (apointMentItemPrices.getExtraCardId() > 0) {
                                                            if (isFirstAddToothCardItem) {
                                                                shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                        apointMentItemPrices.getVipPrice30()));
                                                            }
                                                        } else {
                                                            shouXiItemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                    apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                    apointMentItemPrices.getVipPrice30()));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Log.e("TAG", "shouXiItemList1.size() = " + shouXiItemList1.size());
                    Log.e("TAG", "shouXiItemList1.toString() = " + shouXiItemList1.toString());
                    if (shouXiItemList1.size() > 0) {
                        for (int j = 0; j < shouXiItemList1.size(); j++) {
                            ApointMentItem apointMentItem = shouXiItemList1.get(j);
                            for (int k = 0; k < itemList.size(); k++) {
                                ApointMentItem apointMentItem1 = itemList.get(k);
                                if (apointMentItem.getId() == apointMentItem1.getId()) {//去除已经添加过的单项
                                    apointMentItem.setDelete1(true);
                                }
                            }
                        }
                        //循环删除不支持的单项
                        Iterator<ApointMentItem> it = shouXiItemList1.iterator();
                        while (it.hasNext()) {
                            ApointMentItem apointMentItem = it.next();
                            if (apointMentItem != null && apointMentItem.isDelete1()) {
                                it.remove();
                            }
                        }
                        Log.e("TAG", "shouXiItemList1.size() = " + shouXiItemList1.size());
                        Log.e("TAG", "shouXiItemList1.toString() = " + shouXiItemList1.toString());
                        itemList.addAll(shouXiItemList1);
                        for (int j = 0; j < itemList.size(); j++) {
                            ApointMentItem apointMentItem = itemList.get(j);
                            for (int k = 0; k < localItemList.size(); k++) {
                                ApointMentItem apointMentItem1 = localItemList.get(k);
                                if (apointMentItem1.getId() == apointMentItem.getId()) {
                                    apointMentItem.setName(apointMentItem1.getName());
                                    apointMentItem.setPic(apointMentItem1.getPic());
                                }
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < localItemPriceList.size(); j++) {
                        ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                        List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                        if (itemPriceList != null && itemPriceList.size() > 0) {
                            for (int k = 0; k < itemPriceList.size(); k++) {
                                Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                                ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                                if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                    if (isVip) {
                                        if (tid == 0 || tid == 1) {
                                            if (apointMentItemPrices.getVipPrice10() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 2) {
                                            if (apointMentItemPrices.getVipPrice20() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 3) {
                                            if (apointMentItemPrices.getVipPrice30() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        }
                                    } else {
                                        if (tid == 0 || tid == 1) {
                                            if (apointMentItemPrices.getPrice10() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 2) {
                                            if (apointMentItemPrices.getPrice20() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        } else if (tid == 3) {
                                            if (apointMentItemPrices.getPrice30() == 0) {
                                                if (apointMentItemPrices.getExtraCardId() > 0) {
                                                    if (isFirstAddToothCardItem) {
                                                        itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                                apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                                apointMentItemPrices.getVipPrice30()));
                                                    }
                                                } else {
                                                    itemList.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //更新宠物添加的单项详细数据
                    if (itemList.size() > 0) {
                        for (int j = 0; j < itemList.size(); j++) {
                            ApointMentItem apointMentItem = itemList.get(j);
                            for (int k = 0; k < localItemList.size(); k++) {
                                ApointMentItem apointMentItem1 = localItemList.get(k);
                                if (apointMentItem1.getId() == apointMentItem.getId()) {
                                    apointMentItem.setName(apointMentItem1.getName());
                                    apointMentItem.setPic(apointMentItem1.getPic());
                                }
                            }
                        }
                    }
                }
            } else {
                List<ApointMentItem> itemList1 = new ArrayList<>();
                itemList1.clear();
                for (int j = 0; j < localItemPriceList.size(); j++) {
                    ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                    List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                    if (itemPriceList != null && itemPriceList.size() > 0) {
                        for (int k = 0; k < itemPriceList.size(); k++) {
                            Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                            ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                            if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                                if (isVip) {
                                    if (tid == 0 || tid == 1) {
                                        if (apointMentItemPrices.getVipPrice10() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                if (isFirstAddToothCardItem) {
                                                    itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            } else {
                                                itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                        apointMentItemPrices.getVipPrice30()));
                                            }
                                        }
                                    } else if (tid == 2) {
                                        if (apointMentItemPrices.getVipPrice20() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                if (isFirstAddToothCardItem) {
                                                    itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            } else {
                                                itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                        apointMentItemPrices.getVipPrice30()));
                                            }
                                        }
                                    } else if (tid == 3) {
                                        if (apointMentItemPrices.getVipPrice30() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                if (isFirstAddToothCardItem) {
                                                    itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            } else {
                                                itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                        apointMentItemPrices.getVipPrice30()));
                                            }
                                        }
                                    }
                                } else {
                                    if (tid == 0 || tid == 1) {
                                        if (apointMentItemPrices.getPrice10() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                if (isFirstAddToothCardItem) {
                                                    itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            } else {
                                                itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice10(), apointMentItemPrices.getVipPrice10(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                        apointMentItemPrices.getVipPrice30()));
                                            }
                                        }
                                    } else if (tid == 2) {
                                        if (apointMentItemPrices.getPrice20() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                if (isFirstAddToothCardItem) {
                                                    itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            } else {
                                                itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice20(), apointMentItemPrices.getVipPrice20(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                        apointMentItemPrices.getVipPrice30()));
                                            }
                                        }
                                    } else if (tid == 3) {
                                        if (apointMentItemPrices.getPrice30() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                if (isFirstAddToothCardItem) {
                                                    itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), false, true, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                            apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                            apointMentItemPrices.getVipPrice30()));
                                                }
                                            } else {
                                                itemList1.add(new ApointMentItem(apointMentItemPrice.getItemId(), apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice30(), true, false, apointMentItemPrices.getPrice10(), apointMentItemPrices.getPrice20(),
                                                        apointMentItemPrices.getPrice30(), apointMentItemPrices.getVipPrice10(), apointMentItemPrices.getVipPrice20(),
                                                        apointMentItemPrices.getVipPrice30()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //更新宠物添加的单项详细数据
                if (itemList1.size() > 0) {
                    for (int j = 0; j < itemList1.size(); j++) {
                        ApointMentItem apointMentItem = itemList1.get(j);
                        for (int k = 0; k < localItemList.size(); k++) {
                            ApointMentItem apointMentItem1 = localItemList.get(k);
                            if (apointMentItem1.getId() == apointMentItem.getId()) {
                                apointMentItem.setName(apointMentItem1.getName());
                                apointMentItem.setPic(apointMentItem1.getPic());
                            }
                        }
                    }
                }
                apointMentPet.setItemList(itemList1);
            }
        }
        //去重
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItem> itemList = apointMentPet.getItemList();
            if (itemList != null && itemList.size() > 0) {
                apointMentPet.setItemList(new ArrayList<ApointMentItem>(new HashSet<ApointMentItem>(itemList)));
            }
        }
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet.getServiceId() == 1 || apointMentPet.getServiceId() == 3) {
                if (tid == 0 || tid == 1) {
                    apointMentPet.setServiceName("中级洗护套餐");
                    serviceName = "中级洗护套餐";
                } else if (tid == 2) {
                    apointMentPet.setServiceName("高级洗护套餐");
                    serviceName = "高级洗护套餐";
                } else if (tid == 3) {
                    apointMentPet.setServiceName("首席洗护套餐");
                    serviceName = "首席洗护套餐";
                }
            } else if (apointMentPet.getServiceId() == 2 || apointMentPet.getServiceId() == 4) {
                if (tid == 0 || tid == 1) {
                    apointMentPet.setServiceName("中级美容套餐");
                    serviceName = "中级美容套餐";
                } else if (tid == 2) {
                    apointMentPet.setServiceName("高级美容套餐");
                    serviceName = "高级美容套餐";
                } else if (tid == 3) {
                    apointMentPet.setServiceName("首席美容套餐");
                    serviceName = "首席美容套餐";
                }
            }
        }
        appointMentPetAdapter.notifyDataSetChanged();
    }

    private void setItemData() {
        //更新单项列表数据
        for (int i = 0; i < localItemList.size(); i++) {
            int num = 0;
            int shouXiNum = 0;
            int teethCardNum = 0;
            ApointMentItem apointMentItem = localItemList.get(i);
            List<Integer> availableMyPets = apointMentItem.getAvailableMyPets();
            if (availableMyPets != null && availableMyPets.size() > 0) {
                for (int j = 0; j < localItemPriceList.size(); j++) {
                    ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(j);
                    List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                    if (apointMentItem.getId() == apointMentItemPrice.getItemId() && itemPriceList != null
                            && itemPriceList.size() > 0) {
                        for (int k = 0; k < itemPriceList.size(); k++) {
                            Log.e("TAG", "itemPriceList.get(k).getMyPetId() = " + itemPriceList.get(k).getMyPetId());
                            ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                            if (apointMentItemPrices != null) {
                                if (isVip) {
                                    if (tid == 0) {
                                        if (apointMentItemPrices.getVipPrice10() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        }
                                    } else {
                                        if (tid == 1 && apointMentItemPrices.getVipPrice10() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        } else if (tid == 2 && apointMentItemPrices.getVipPrice20() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        } else if (tid == 3 && apointMentItemPrices.getVipPrice30() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        }
                                    }
                                } else {
                                    if (tid == 0) {
                                        if (apointMentItemPrices.getPrice10() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        }
                                    } else {
                                        if (tid == 1 && apointMentItemPrices.getPrice10() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        } else if (tid == 2 && apointMentItemPrices.getPrice20() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        } else if (tid == 3 && apointMentItemPrices.getPrice30() == 0) {
                                            if (apointMentItemPrices.getExtraCardId() > 0) {
                                                teethCardNum++;
                                            } else {
                                                shouXiNum++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (teethCardNum == petList.size()) {
                    apointMentItem.setTeethCard(true);
                }
                if (shouXiNum == petList.size()) {
                    apointMentItem.setState(2);
                    apointMentItem.setShouXi(true);
                } else {
                    for (int j = 0; j < petList.size(); j++) {
                        ApointMentPet apointMentPet = petList.get(j);
                        List<ApointMentItem> itemList = apointMentPet.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            for (int k = 0; k < itemList.size(); k++) {
                                ApointMentItem apointMentItem1 = itemList.get(k);
                                if (apointMentItem1.getId() == apointMentItem.getId()) {
                                    num++;
                                }
                            }
                        }
                    }
                    if (num == availableMyPets.size()) {
                        apointMentItem.setState(4);
                    } else {
                        apointMentItem.setState(3);
                    }
                }
            } else {
                apointMentItem.setState(1);
            }
        }
        //更新单项列表价格数据
        for (int j = 0; j < localItemList.size(); j++) {
            ApointMentItem apointMentItem = localItemList.get(j);
            for (int k = 0; k < localItemPriceList.size(); k++) {
                ApointMentItemPrice apointMentItemPrice = localItemPriceList.get(k);
                if (apointMentItem.getId() == apointMentItemPrice.getItemId()) {
                    List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
                    if (itemPriceList != null && itemPriceList.size() > 0) {
                        if (apointMentItem.getState() == 1) {//1.所有宠物都不支持
                            apointMentItem.setPrice(-1);
                            apointMentItem.setVipPrice(-1);
                        } else if (apointMentItem.getState() == 2 || apointMentItem.isTeethCard()) {//2.首席赠送，刷牙卡
                            apointMentItem.setPrice(0);
                            apointMentItem.setVipPrice(0);
                        } else {
                            apointMentItem.setPrice(getMinPrice(itemPriceList)[0]);
                            apointMentItem.setVipPrice(getMinPrice(itemPriceList)[1]);
                        }
                    }
                }
            }
        }
        //循环删除不支持的单项
        Iterator<ApointMentItem> it = localItemList.iterator();
        while (it.hasNext()) {
            ApointMentItem apointMentItem = it.next();
            if (apointMentItem != null) {
                if (apointMentItem.getAvailableMyPets() != null && apointMentItem.getAvailableMyPets().size() > 0) {

                } else {
                    it.remove();
                }
            }
        }
        if (localItemList.size() > 0) {
            Log.e("TAG", "VISIBLE");
            llAppointmenItem
                    .setVisibility(View.VISIBLE);
            appointMentItemAdapter.setTid(tid);
        } else {
            Log.e("TAG", "GONE");
            llAppointmenItem
                    .setVisibility(View.GONE);
            //添加过单项的宠物全部删除添加的单项
            for (int i = 0; i < petList.size(); i++) {
                petList.get(i).setItemList(null);
            }
        }
    }

    private double[] getMinPrice(List<ApointMentItemPrices> itemPriceList) {
        double[] minPrice = getMinPriceDefault(itemPriceList);
        for (int x = 0; x < itemPriceList.size(); x++) {
            ApointMentItemPrices apointMentItemPrices = itemPriceList.get(x);
            if (isVip) {
                if (tid == 0 || tid == 1) {
                    // 判断最小值
                    if (apointMentItemPrices.getVipPrice10() > 0 && apointMentItemPrices.getVipPrice10() < minPrice[1]) {
                        minPrice[0] = apointMentItemPrices.getPrice10();
                        minPrice[1] = apointMentItemPrices.getVipPrice10();
                        break;
                    }
                } else if (tid == 2) {
                    if (apointMentItemPrices.getVipPrice20() > 0 && apointMentItemPrices.getVipPrice20() < minPrice[1]) {
                        minPrice[0] = apointMentItemPrices.getPrice20();
                        minPrice[1] = apointMentItemPrices.getVipPrice20();
                        break;
                    }
                } else if (tid == 3) {
                    if (apointMentItemPrices.getVipPrice30() > 0 && apointMentItemPrices.getVipPrice30() < minPrice[1]) {
                        minPrice[0] = apointMentItemPrices.getPrice30();
                        minPrice[1] = apointMentItemPrices.getVipPrice30();
                        break;
                    }
                }
            } else {
                if (tid == 0 || tid == 1) {
                    // 判断最小值
                    if (apointMentItemPrices.getPrice10() > 0 && apointMentItemPrices.getPrice10() < minPrice[0]) {
                        minPrice[0] = apointMentItemPrices.getPrice10();
                        minPrice[1] = apointMentItemPrices.getVipPrice10();
                        break;
                    }
                } else if (tid == 2) {
                    if (apointMentItemPrices.getPrice20() > 0 && apointMentItemPrices.getPrice20() < minPrice[0]) {
                        minPrice[0] = apointMentItemPrices.getPrice20();
                        minPrice[1] = apointMentItemPrices.getVipPrice20();
                        break;
                    }
                } else if (tid == 3) {
                    if (apointMentItemPrices.getPrice30() > 0 && apointMentItemPrices.getPrice30() < minPrice[0]) {
                        minPrice[0] = apointMentItemPrices.getPrice30();
                        minPrice[1] = apointMentItemPrices.getVipPrice30();
                        break;
                    }
                }
            }
        }
        return minPrice;
    }

    private double[] getMinPriceDefault(List<ApointMentItemPrices> itemPriceList) {
        double minPricDefaulte[] = new double[2];
        for (int x = 0; x < itemPriceList.size(); x++) {
            ApointMentItemPrices apointMentItemPrices = itemPriceList.get(x);
            if (isVip) {
                if (tid == 0 || tid == 1) {
                    // 判断最小值
                    if (apointMentItemPrices.getVipPrice10() > 0) {
                        minPricDefaulte[0] = apointMentItemPrices.getPrice10();
                        minPricDefaulte[1] = apointMentItemPrices.getVipPrice10();
                        break;
                    }
                } else if (tid == 2) {
                    if (apointMentItemPrices.getVipPrice20() > 0) {
                        minPricDefaulte[0] = apointMentItemPrices.getPrice20();
                        minPricDefaulte[1] = apointMentItemPrices.getVipPrice20();
                        break;
                    }
                } else if (tid == 3) {
                    if (apointMentItemPrices.getVipPrice30() > 0) {
                        minPricDefaulte[0] = apointMentItemPrices.getPrice30();
                        minPricDefaulte[1] = apointMentItemPrices.getVipPrice30();
                        break;
                    }
                }
            } else {
                if (tid == 0 || tid == 1) {
                    // 判断最小值
                    if (apointMentItemPrices.getPrice10() > 0) {
                        minPricDefaulte[0] = apointMentItemPrices.getPrice10();
                        minPricDefaulte[1] = apointMentItemPrices.getVipPrice10();
                        break;
                    }
                } else if (tid == 2) {
                    if (apointMentItemPrices.getPrice20() > 0) {
                        minPricDefaulte[0] = apointMentItemPrices.getPrice20();
                        minPricDefaulte[1] = apointMentItemPrices.getVipPrice20();
                        break;
                    }
                } else if (tid == 3) {
                    if (apointMentItemPrices.getPrice30() > 0) {
                        minPricDefaulte[0] = apointMentItemPrices.getPrice30();
                        minPricDefaulte[1] = apointMentItemPrices.getVipPrice30();
                        break;
                    }
                }
            }
        }
        return minPricDefaulte;
    }

    private void showPetMxDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(AppointMentActivity.this, R.layout.appoint_petmx_bottom_dialog, null);
        ImageView iv_appointpetmx_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottomdia_close);
        final RecyclerView rv_appointpetmx_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpetmx_bottomdia);
        ImageView iv_appointpetmx_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg);
        ImageView iv_appointpetmx_bottom_bg_close = (ImageView) customView.findViewById(R.id.iv_appointpetmx_bottom_bg_close);
        RelativeLayout rl_appointpetmx_bottomdia = (RelativeLayout) customView.findViewById(R.id.rl_appointpetmx_bottomdia);
        LinearLayout ll_appointpetmx_bottomdia = (LinearLayout) customView.findViewById(R.id.ll_appointpetmx_bottomdia);
        ll_appointpetmx_bottomdia.bringToFront();
        rv_appointpetmx_bottomdia.setHasFixedSize(true);
        rv_appointpetmx_bottomdia.setLayoutManager(new LinearLayoutManager(this));
        appointMentPetMxAdapter = new AppointMentPetMxAdapter(R.layout.item_appointment_pet_mx, petList, isVip);
        rv_appointpetmx_bottomdia.setAdapter(appointMentPetMxAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_appointpetmx_bottomdia.scrollToPosition(0);
            }
        }, 500);
        float screenDensity = ScreenUtil.getScreenDensity(this);
        Log.e("TAG", "screenDensity = " + screenDensity);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.getWindowHeight(this) - DensityUtil.dp2px(this, 54)
                - DisplayUtil.getStatusBarHeight(this), true);
        pWinBottomDialog.setFocusable(false);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(AppointMentActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, DensityUtil.dp2px(this, 54));
        iv_appointpetmx_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        rl_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_appointpetmx_bottom_bg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        ll_appointpetmx_bottomdia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            Log.e("TAG", "监听到返回键");
            Global.ServerEvent(AppointMentActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_appointment_back);
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.ll_appointment_goorder,
            R.id.tv_appointment_phone, R.id.ll_appointmen_pet_more, R.id.ll_appointment_fhxg,
            R.id.ll_appointmen_item_more, R.id.rl_appointmen_time_more, R.id.rl_appointmen_mrs
            , R.id.iv_appointmen_js, R.id.rl_appointment_mx, R.id.tv_appointment_card_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_appointment_card_go:
                Utils.goService(mContext, point, backup);
                break;
            case R.id.rl_appointment_mx:
                showPetMxDialog();
                break;
            case R.id.ib_titlebar_back:
                Global.ServerEvent(AppointMentActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_appointment_back);
                finish();
                break;
            case R.id.ll_appointment_goorder:
                if (!Utils.isStrNull(appointment)) {
                    new AlertDialogNavAndPost(this).builder().setTitle("").setMsg("提交订单需要提前预约时间哦~").setNegativeButton("关闭", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("去预约时间", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(AppointMentActivity.this, AppointTimeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shop);
                            if (localAppointWorker != null) {
                                bundle.putSerializable("appointWorker", localAppointWorker);
                            }
                            intent1.putExtras(bundle);
                            intent1.putExtra("lat", lat);
                            intent1.putExtra("serviceCardId", serviceCardId);
                            intent1.putExtra("dayposition", dayposition);
                            intent1.putExtra("lng", lng);
                            intent1.putExtra("serviceLoc", serviceLoc);
                            intent1.putExtra("petList", (Serializable) petList);
                            intent1.putExtra("isVip", isVip);
                            intent1.putExtra("tid", tid);
                            intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds());
                            startActivity(intent1);
                        }
                    }).show();
                } else if (workerId <= 0) {
                    new AlertDialogNavAndPost(this).builder().setTitle("").setMsg("提交订单需要选择美容师哦~").setNegativeButton("关闭", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("去选择美容师", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(AppointMentActivity.this, WorkerListActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shop", shop);
                            intent2.putExtras(bundle);
                            intent2.putExtra("lat", lat);
                            intent2.putExtra("pickup", pickup);
                            intent2.putExtra("lng", lng);
                            intent2.putExtra("dayposition", dayposition);
                            intent2.putExtra("workerId", workerId);
                            intent2.putExtra("serviceCardId", serviceCardId);
                            intent2.putExtra("defaultWorkerTag", defaultWorkerTag);
                            intent2.putExtra("workerIds", workerIds);
                            intent2.putExtra("appointment", appointment);
                            intent2.putExtra("serviceLoc", serviceLoc);
                            intent2.putExtra("petList", (Serializable) petList);
                            intent2.putExtra("isVip", isVip);
                            intent2.putExtra("tid", tid);
                            intent2.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds());
                            startActivity(intent2);
                        }
                    }).show();
                } else {//提交订单
                    localAppointWorker.setUpdateWorkerId(0);
                    Global.ServerEvent(AppointMentActivity.this, Global.ServerEventID.typeid_149, Global.ServerEventID.click_goto_order);
                    Intent intent2 = new Intent(AppointMentActivity.this, WashOrderConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", shop);
                    bundle.putSerializable("selectedWorker", localAppointWorker);
                    bundle.putSerializable("commAddr", (CommAddr) getIntent().getSerializableExtra("commAddr"));
                    intent2.putExtras(bundle);
                    intent2.putExtra("lat", lat);
                    intent2.putExtra("lng", lng);
                    intent2.putExtra("cityId", getIntent().getIntExtra("cityId", 0));
                    intent2.putExtra("isSelectPickup", isSelectPickup);
                    intent2.putExtra("pickup", pickup);
                    intent2.putExtra("myPets", (Serializable) petList);
                    intent2.putExtra("appointment", appointment);
                    intent2.putExtra("isUpgradeWorker", 0);
                    intent2.putExtra("serviceLoc", serviceLoc);
                    intent2.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds());
                    intent2.putExtra("lastPrice", lastPrice);
                    intent2.putExtra("becomeVipPrice", lastVipPrice);
                    intent2.putExtra("pickupPrice", pickupPrice);
                    intent2.putExtra("servicetype", getIntent().getIntExtra("servicetype", 0));
                    startActivity(intent2);
                }
                break;
            case R.id.tv_appointment_phone:
                Utils.setPhoneDialog(mContext, shopPhone, shopPhone, "取消", "呼叫",
                        mContext.getResources().getColor(R.color.a666666), mContext.getResources().getColor(R.color.a007AFF),
                        mContext.getResources().getColor(R.color.a333333));
                break;
            case R.id.ll_appointmen_pet_more:
                setPetListIsOpen();
                break;
            case R.id.ll_appointment_fhxg:
                UmengStatistics.UmengEventStatistics(this,
                        Global.UmengEventID.click_Return);
                finish();
                break;
            case R.id.ll_appointmen_item_more:
                Intent intent = new Intent(AppointMentActivity.this, ItemListActivity.class);
                intent.putExtra("itemList", (Serializable) localItemList);
                intent.putExtra("itemPriceList", (Serializable) localItemPriceList);
                intent.putExtra("petList", (Serializable) petList);
                Bundle bundle1 = new Bundle();
                if (localAppointWorker != null) {
                    bundle1.putSerializable("appointWorker", localAppointWorker);
                }
                bundle1.putSerializable("shop", shop);
                intent.putExtras(bundle1);
                intent.putExtra("lat", lat);
                intent.putExtra("serviceCardId", serviceCardId);
                intent.putExtra("tid", tid);
                intent.putExtra("isVip", isVip);
                intent.putExtra("pickup", pickup);
                intent.putExtra("dayposition", dayposition);
                intent.putExtra("serviceLoc", serviceLoc);
                intent.putExtra("appointment", appointment);
                intent.putExtra("lng", lng);
                startActivityForResult(intent, Global.APPOINTMENT_TO_ITEMLIST);
                break;
            case R.id.rl_appointmen_time_more:
                Intent intent1 = new Intent(AppointMentActivity.this, AppointTimeActivity.class);
                Bundle bundle = new Bundle();
                if (localAppointWorker != null) {
                    bundle.putSerializable("appointWorker", localAppointWorker);
                }
                bundle.putSerializable("shop", shop);
                intent1.putExtra("serviceCardId", serviceCardId);
                intent1.putExtras(bundle);
                intent1.putExtra("dayposition", dayposition);
                intent1.putExtra("lat", lat);
                intent1.putExtra("lng", lng);
                intent1.putExtra("serviceLoc", serviceLoc);
                intent1.putExtra("petList", (Serializable) petList);
                intent1.putExtra("isVip", isVip);
                intent1.putExtra("tid", tid);
                intent1.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds());
                startActivity(intent1);
                break;
            case R.id.rl_appointmen_mrs:
                Intent intent2 = new Intent(this, WorkerListActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("shop", shop);
                intent2.putExtras(bundle2);
                intent2.putExtra("lat", lat);
                intent2.putExtra("pickup", pickup);
                intent2.putExtra("serviceCardId", serviceCardId);
                intent2.putExtra("dayposition", dayposition);
                intent2.putExtra("lng", lng);
                intent2.putExtra("defaultWorkerTag", defaultWorkerTag);
                intent2.putExtra("workerId", workerId);
                intent2.putExtra("workerIds", workerIds);
                intent2.putExtra("appointment", appointment);
                intent2.putExtra("strp", getPetID_ServiceId_MyPetId_ItemIds());
                intent2.putExtra("petList", (Serializable) petList);
                intent2.putExtra("isVip", isVip);
                intent2.putExtra("tid", tid);
                intent2.putExtra("serviceLoc", serviceLoc);
                startActivity(intent2);
                break;
            case R.id.iv_appointmen_js:
                setJsIsOpen();
                break;
        }
    }

    private void setJsIsOpen() {
        if (isJsOpen) {
            MDialog mDialog = new MDialog.Builder(this)
                    .setType(MDialog.DIALOGTYPE_CONFIRM)
                    .setMessage("是否取消接送服务")
                    .setCancelStr("否")
                    .setOKStr("是")
                    .setCancelTextColor(getResources().getColor(R.color.a666666))
                    .setOKTextColor(getResources().getColor(R.color.a007AFF))
                    .setMsgTextColor(getResources().getColor(R.color.a333333))
                    .positiveListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ivAppointmenJs.setImageResource(R.drawable.noty_no);
                            isSelectPickup = false;
                            lastVipPrice = ComputeUtil.sub(lastVipPrice, pickupPrice);
                            lastPrice = ComputeUtil.sub(lastPrice, pickupPrice);
                            if (recommendServiceCard != null) {
                                if (serviceCardId > 0) {
                                    setFXPrice(lastVipPrice);
                                    Utils.setText(tvAppointmentPayprice, "¥" + lastVipPrice, "", View.VISIBLE, View.INVISIBLE);
                                } else {
                                    setFXPrice(lastPrice);
                                    Utils.setText(tvAppointmentPayprice, "¥" + lastPrice, "", View.VISIBLE, View.INVISIBLE);
                                }
                            } else {
                                setFXPrice(lastPrice);
                                Utils.setText(tvAppointmentPayprice, "¥" + lastPrice, "", View.VISIBLE, View.INVISIBLE);
                            }
                            isJsOpen = !isJsOpen;
                        }
                    }).build();
            mDialog.show();
        } else {
            if (Utils.isStrNull(appointment)) {
                if (pickup == 1) {
                    ivAppointmenJs.setImageResource(R.drawable.noty_yes);
                    isSelectPickup = true;
                    setLastPrice();
                    isJsOpen = !isJsOpen;
                } else {
                    pickupFlag = 2;
                    mPDialog.showDialog();
                    CommUtil.canBePickup(mContext, spUtil.getString("cellphone", ""), 0, shopId,
                            getIntent().getIntExtra("addressId", 0), null, appointment, serviceLoc, lat, lng,
                            IsCanPickNormalHandler);
                }
            } else {
                ToastUtil.showToastShortBottom(AppointMentActivity.this, "请选择时间后再选择接送");
            }
        }
    }

    private void setPetListIsOpen() {
        localPetList.clear();
        if (isOpen) {
            localPetList.addAll(petList.subList(0, 2));
            tvAppointmenPetMore.setText("展开更多");
            ivAppointmenPetMore.setImageResource(R.drawable.icon_appoint_pet_zk);
        } else {
            localPetList.addAll(petList);
            tvAppointmenPetMore.setText("点击收起");
            ivAppointmenPetMore.setImageResource(R.drawable.icon_appoint_pet_sp);
        }
        isOpen = !isOpen;
        appointMentPetAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ItemDetailToAppointEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            ApointMentItem eventItem = event.getItem();
            List<ApointMentPet> eventPetList = event.getPetList();
            for (int i = 0; i < localItemList.size(); i++) {
                ApointMentItem apointMentItem = localItemList.get(i);
                if (apointMentItem.getId() == eventItem.getId()) {
                    apointMentItem.setState(eventItem.getState());
                    break;
                }
            }
            for (int i = 0; i < eventPetList.size(); i++) {
                ApointMentPet apointMentPet = eventPetList.get(i);
                for (int j = 0; j < petList.size(); j++) {
                    ApointMentPet apointMentPet1 = petList.get(j);
                    if (apointMentPet.getMyPetId() == apointMentPet1.getMyPetId()) {
                        apointMentPet1.setItemList(apointMentPet.getItemList());
                    }
                }
            }
            setItemAndPetData();
            setLastPrice();
            WorkerAndTime workerAndTime = event.getWorkerAndTime();
            if (workerAndTime != null) {
                setTimeAndWorker(new WorkerAndTimeEvent(workerAndTime.getAppointWorker(),
                        workerAndTime.getAppointment(), workerAndTime.getDayposition(),
                        workerAndTime.getPickup(), workerAndTime.getItemToMorePet(), workerAndTime.getWorkerIds()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointRechargeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (event.isRecharge()) {
                getWorkers();
                getItems();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WorkerAndTimeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            setTimeAndWorker(event);
        }
    }

    private void setTimeAndWorker(WorkerAndTimeEvent event) {
        dayposition = event.getDayposition();
        workerIds = event.getWorkerIds();
        Log.e("TAG", "workerIds = " + workerIds);
        pickup = event.getPickup();
        AppointWorker appointWorker = event.getAppointWorker();
        appointment = event.getAppointment();
        ItemToMorePet itemToMorePet = event.getItemToMorePet();
        String txt = "";
        String[] split = appointment.split(" ");
        String[] split1 = split[1].split(":");
        if (dayposition == 0) {
            txt = "今天" + " " + split1[0] + ":" + split1[1];
        } else if (dayposition == 1) {
            txt = "明天" + " " + split1[0] + ":" + split1[1];
        } else {
            txt = split[0] + " " + split1[0] + ":" + split1[1];
        }
        Utils.setText(tvAppointmenTime, txt, "", View.VISIBLE, View.VISIBLE);
        if (appointWorker != null) {
            if (localAppointWorker == null) {
                localAppointWorker = new AppointWorker();
            }
            localAppointWorker.setGoodRate(appointWorker.getGoodRate());
            localAppointWorker.setOrderTotal(appointWorker.getOrderTotal());
            localAppointWorker.setSetItemDecor(appointWorker.isSetItemDecor());
            localAppointWorker.setExpertiseLiest(appointWorker.getExpertiseLiest());
            localAppointWorker.setAvatar(appointWorker.getAvatar());
            localAppointWorker.setDefaultWorkerTag(appointWorker.getDefaultWorkerTag());
            localAppointWorker.setWorkerId(appointWorker.getWorkerId());
            localAppointWorker.setRealName(appointWorker.getRealName());
            localAppointWorker.setTag(appointWorker.getTag());
            localAppointWorker.setTid(appointWorker.getTid());
            localAppointWorker.setEarliest(appointWorker.getEarliest());
            localAppointWorker.setPriceLiest(appointWorker.getPriceLiest());
            appointment = event.getAppointment();
            defaultWorkerTag = appointWorker.getDefaultWorkerTag();
            workerId = appointWorker.getWorkerId();
            tid = appointWorker.getTid();
            tvAppointmenMrs.setVisibility(View.GONE);
            rlAppointmenMrsinfo.setVisibility(View.VISIBLE);
            GlideUtil.loadCircleImg(this, appointWorker.getAvatar(), ivAppointmenMrsimg, R.drawable.user_icon_unnet_circle);
            Utils.setText(tvAppointmenMrsname, appointWorker.getRealName(), "", View.VISIBLE, View.VISIBLE);
            List<AppointWorkerPrice> priceLiest = appointWorker.getPriceLiest();
            if (priceLiest != null && priceLiest.size() > 0) {
                for (int i = 0; i < priceLiest.size(); i++) {
                    AppointWorkerPrice appointWorkerPrice = priceLiest.get(i);
                    for (int j = 0; j < petList.size(); j++) {//同步petList美容师价格数据
                        ApointMentPet apointMentPet = petList.get(j);
                        if (appointWorkerPrice.getMyPetId() == apointMentPet.getMyPetId()) {
                            apointMentPet.setPrice(appointWorkerPrice.getPrice());
                            apointMentPet.setVipPrice(appointWorkerPrice.getVip_price());
                        }
                    }
                }
            }
            if (itemToMorePet != null) {
                List<ApointMentItem> apointMentItem = itemToMorePet.getApointMentItem();
                List<Integer> myPetIds = itemToMorePet.getMyPetIds();
                if (myPetIds != null && myPetIds.size() > 0) {
                    for (int i = 0; i < myPetIds.size(); i++) {
                        Integer integer = myPetIds.get(i);
                        for (int j = 0; j < petList.size(); j++) {
                            ApointMentPet apointMentPet = petList.get(j);
                            if (apointMentPet.getMyPetId() == integer) {
                                List<ApointMentItem> itemList = apointMentPet.getItemList();
                                if (itemList == null) {
                                    itemList = new ArrayList<ApointMentItem>();
                                }
                                if (apointMentItem != null && apointMentItem.size() > 0) {
                                    for (int k = 0; k < apointMentItem.size(); k++) {
                                        itemList.add(apointMentItem.get(k));
                                    }
                                }
                                apointMentPet.setItemList(itemList);
                            }
                        }
                    }
                }
            }
            setItemAndPetData();
        }
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet.getServiceId() == 1 || apointMentPet.getServiceId() == 3) {
                if (tid == 0 || tid == 1) {
                    apointMentPet.setServiceName("中级洗护套餐");
                    serviceName = "中级洗护套餐";
                } else if (tid == 2) {
                    apointMentPet.setServiceName("高级洗护套餐");
                    serviceName = "高级洗护套餐";
                } else if (tid == 3) {
                    apointMentPet.setServiceName("首席洗护套餐");
                    serviceName = "首席洗护套餐";
                }
            } else if (apointMentPet.getServiceId() == 2 || apointMentPet.getServiceId() == 4) {
                if (tid == 0 || tid == 1) {
                    apointMentPet.setServiceName("中级美容套餐");
                    serviceName = "中级美容套餐";
                } else if (tid == 2) {
                    apointMentPet.setServiceName("高级美容套餐");
                    serviceName = "高级美容套餐";
                } else if (tid == 3) {
                    apointMentPet.setServiceName("首席美容套餐");
                    serviceName = "首席美容套餐";
                }
            }
        }
        if (serviceLoc == 1 && pickup == 1) {//可接送
            if (Utils.isStrNull(localAppointment)) {
                if (!localAppointment.equals(appointment)) {
                    Log.e("TAG", "localAppointment true");
                    isJsOpen = false;
                    isSelectPickup = false;
                    ivAppointmenJs.setImageResource(R.drawable.noty_no);
                    pickupAmount = 0;
                    pickupPrice = 0;
                    localAppointment = appointment;
                    pickupFlag = 1;
                    mPDialog.showDialog();
                    CommUtil.canBePickup(mContext, spUtil.getString("cellphone", ""), 0, shopId,
                            getIntent().getIntExtra("addressId", 0), null, appointment, serviceLoc, lat, lng,
                            IsCanPickNormalHandler);
                } else {
                    Log.e("TAG", "localAppointment false");
                    localAppointment = appointment;
                }
            } else {
                Log.e("TAG", "localAppointment null");
                isJsOpen = false;
                isSelectPickup = false;
                ivAppointmenJs.setImageResource(R.drawable.noty_no);
                pickupAmount = 0;
                pickupPrice = 0;
                localAppointment = appointment;
                pickupFlag = 1;
                mPDialog.showDialog();
                CommUtil.canBePickup(mContext, spUtil.getString("cellphone", ""), 0, shopId,
                        getIntent().getIntExtra("addressId", 0), null, appointment, serviceLoc, lat, lng,
                        IsCanPickNormalHandler);
            }
        } else {
            isJsOpen = false;
            isSelectPickup = false;
            ivAppointmenJs.setImageResource(R.drawable.noty_no);
            pickupAmount = 0;
            pickupPrice = 0;
            localAppointment = appointment;
        }
        appointMentPetAdapter.notifyDataSetChanged();
        appointMentItemAdapter.notifyDataSetChanged();
        setLastPrice();
        getWorkers();
    }

    private AsyncHttpResponseHandler IsCanPickNormalHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.dimissDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("pickupTip") && !objectData.isNull("pickupTip")) {
                            pickupTip = objectData.getString("pickupTip");
                        }
                        if (objectData.has("pickupResult") && !objectData.isNull("pickupResult")) {
                            pickupResult = objectData.getInt("pickupResult");
                        }
                        if (objectData.has("restAmount") && !objectData.isNull("restAmount")) {
                            pickupAmount = Integer.parseInt(objectData.getString("restAmount"));
                        }
                        if (objectData.has("pickupPrice") && !objectData.isNull("pickupPrice")) {
                            pickupPrice = objectData.getDouble("pickupPrice");
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e("TAG", "canBePickup()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(AppointMentActivity.this,
                        "数据异常");
                e.printStackTrace();
            }
            if (pickupResult == 1) {
                if (pickupFlag == 1) {
                    showJsDialog();
                } else if (pickupFlag == 2) {
                    ivAppointmenJs.setImageResource(R.drawable.noty_yes);
                    isSelectPickup = true;
                    setLastPrice();
                    isJsOpen = !isJsOpen;
                }
            } else {
                isSelectPickup = false;
                ivAppointmenJs.setImageResource(R.drawable.noty_no);
                ToastUtil.showToastShortBottom(AppointMentActivity.this, pickupTip);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.dimissDialog();
            Log.e("TAG", "请求失败");
            ToastUtil.showToastShortBottom(AppointMentActivity.this,
                    "请求失败");
        }
    };

    private void showJsDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(AppointMentActivity.this, R.layout.appoint_js_bottom_dialog, null);
        ImageView iv_appointjs_bottomdia_close = (ImageView) customView.findViewById(R.id.iv_appointjs_bottomdia_close);
        TextView tv_appointjs_bottomdia_num = (TextView) customView.findViewById(R.id.tv_appointjs_bottomdia_num);
        TextView tv_appointjs_bottomdia_no = (TextView) customView.findViewById(R.id.tv_appointjs_bottomdia_no);
        TextView tv_appointjs_bottomdia_yes = (TextView) customView.findViewById(R.id.tv_appointjs_bottomdia_yes);
        ImageView iv_appointjs_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointjs_bottom_bg);
        tv_appointjs_bottomdia_num.setText(pickupAmount + "");
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(AppointMentActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_appointjs_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                isSelectPickup = false;
                ivAppointmenJs.setImageResource(R.drawable.noty_no);
            }
        });
        iv_appointjs_bottomdia_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                isSelectPickup = false;
                ivAppointmenJs.setImageResource(R.drawable.noty_no);
            }
        });
        tv_appointjs_bottomdia_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                isSelectPickup = false;
                ivAppointmenJs.setImageResource(R.drawable.noty_no);
            }
        });
        tv_appointjs_bottomdia_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                isSelectPickup = true;
                isJsOpen = true;
                ivAppointmenJs.setImageResource(R.drawable.noty_yes);
                setLastPrice();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteItemEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            int itemId = event.getItemId();
            int myPetId = event.getMyPetId();
            for (int i = 0; i < petList.size(); i++) {
                ApointMentPet apointMentPet = petList.get(i);
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                if (apointMentPet.getMyPetId() == myPetId) {
                    if (itemList != null && itemList.size() > 0) {
                        for (int j = 0; j < itemList.size(); j++) {
                            ApointMentItem apointMentItem = itemList.get(j);
                            if (apointMentItem.getId() == itemId) {
                                itemList.remove(j);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            for (int i = 0; i < localItemList.size(); i++) {
                ApointMentItem apointMentItem = localItemList.get(i);
                if (apointMentItem.getId() == itemId) {
                    apointMentItem.setState(3);
                    break;
                }
            }
            appointMentItemAdapter.notifyDataSetChanged();
            appointMentPetMxAdapter.notifyDataSetChanged();
            setLastPrice();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.APPOINTMENT_TO_ITEMLIST) {//单项列表选择单项返回
                List<ApointMentPet> intentPetList = (List<ApointMentPet>) data.getSerializableExtra("petList");
                List<ApointMentItem> intentItemList = (List<ApointMentItem>) data.getSerializableExtra("itemList");
                for (int i = 0; i < intentItemList.size(); i++) {
                    ApointMentItem apointMentItem = intentItemList.get(i);
                    for (int j = 0; j < localItemList.size(); j++) {
                        ApointMentItem apointMentItem1 = localItemList.get(j);
                        if (apointMentItem.getId() == apointMentItem1.getId()) {
                            apointMentItem1.setState(apointMentItem.getState());
                        }
                    }
                }
                for (int i = 0; i < intentPetList.size(); i++) {
                    ApointMentPet apointMentPet = intentPetList.get(i);
                    for (int j = 0; j < petList.size(); j++) {
                        ApointMentPet apointMentPet1 = petList.get(j);
                        if (apointMentPet.getMyPetId() == apointMentPet1.getMyPetId()) {
                            apointMentPet1.setItemList(apointMentPet.getItemList());
                        }
                    }
                }
                setItemAndPetData();
                setLastPrice();
                WorkerAndTime workerAndTime = (WorkerAndTime) data.getSerializableExtra("workerAndTime");
                if (workerAndTime != null) {
                    setTimeAndWorker(new WorkerAndTimeEvent(workerAndTime.getAppointWorker(),
                            workerAndTime.getAppointment(), workerAndTime.getDayposition(),
                            workerAndTime.getPickup(), workerAndTime.getItemToMorePet(), workerAndTime.getWorkerIds()));
                }
            } else if (requestCode == Global.APPOINTMENT_TO_SWITCHSERVICE) {//切换服务返回
                ApointMentPet intentPet = (ApointMentPet) data.getSerializableExtra("pet");
                if (intentPet != null) {
                    for (int i = 0; i < petList.size(); i++) {
                        ApointMentPet apointMentPet = petList.get(i);
                        if (apointMentPet.getMyPetId() == intentPet.getMyPetId()) {
                            apointMentPet.setItemList(intentPet.getItemList());
                            apointMentPet.setServiceName(intentPet.getServiceName());
                            apointMentPet.setServiceId(intentPet.getServiceId());
                            apointMentPet.setVipPrice(intentPet.getVipPrice());
                            apointMentPet.setPrice(intentPet.getPrice());
                            break;
                        }
                    }
                    dayposition = 0;
                    workerIds = null;
                    appointment = null;
                    tvAppointmenTime.setText("");
                    ivAppointmenJs.setImageResource(R.drawable.noty_no);
                    pickupAmount = 0;
                    isJsOpen = false;
                    if (pickup == 1 && isSelectPickup) {
                        lastVipPrice = ComputeUtil.sub(lastVipPrice, pickupPrice);
                        lastPrice = ComputeUtil.sub(lastPrice, pickupPrice);
                        if (recommendServiceCard != null) {
                            if (serviceCardId > 0) {
                                setFXPrice(lastVipPrice);
                                Utils.setText(tvAppointmentPayprice, "¥" + lastVipPrice, "", View.VISIBLE, View.INVISIBLE);
                            } else {
                                setFXPrice(lastPrice);
                                Utils.setText(tvAppointmentPayprice, "¥" + lastPrice, "", View.VISIBLE, View.INVISIBLE);
                            }
                        } else {
                            setFXPrice(lastPrice);
                            Utils.setText(tvAppointmentPayprice, "¥" + lastPrice, "", View.VISIBLE, View.INVISIBLE);
                        }
                    }
                    pickupPrice = 0;
                    pickup = 0;
                    isSelectPickup = false;
                    appointMentPetAdapter.notifyDataSetChanged();
                    switchWorkerAndTime = (WorkerAndTime) data.getSerializableExtra("workerAndTime");
                    getItems();
                }
            }
        }
    }

    private void showShouXiItemDialog() {
        String str = "";
        if (tid == 0 || tid == 1) {
            str = "中级洗护套餐已包含以下单项";
        } else if (tid == 2) {
            str = "高级洗护套餐已包含以下单项";
        } else if (tid == 3) {
            str = "首席洗护套餐已包含以下单项";
        }
        ShouXiItemDialog shouXiItemDialog = new ShouXiItemDialog.Builder(this)
                .setTitle(str)
                .setType(MDialog.DIALOGTYPE_ALERT)
                .setData(shouXiItemList)
                .setCancelable(true).setOKStr("我知道了")
                .build();
        shouXiItemDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
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
}
