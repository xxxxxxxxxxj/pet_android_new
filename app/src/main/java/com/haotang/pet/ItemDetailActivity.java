package com.haotang.pet;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AppointBottomPetAdapter;
import com.haotang.pet.adapter.ItemDetailPicAdapter;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.ApointMentItemPrice;
import com.haotang.pet.entity.ApointMentItemPrices;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.entity.ItemDetailToAppointEvent;
import com.haotang.pet.entity.ItemDetailWorkerAndTimeEvent;
import com.haotang.pet.entity.ItemToMorePet;
import com.haotang.pet.entity.ServiceShopAdd;
import com.haotang.pet.entity.WorkerAndTime;
import com.haotang.pet.entity.WorkerAndTimeEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GlideImageLoaderFourRound;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 单项详情界面
 */
public class ItemDetailActivity extends SuperActivity implements OnBannerListener {
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_item_detail_add)
    TextView tvItemDetailAdd;
    @BindView(R.id.tv_item_detail_price)
    TextView tvItemDetailPrice;
    @BindView(R.id.tv_item_detail_vipprice)
    TextView tvItemDetailVipprice;
    @BindView(R.id.banner_item_detail)
    Banner bannerItemDetail;
    @BindView(R.id.tv_item_detail_num)
    TextView tvItemDetailNum;
    @BindView(R.id.tv_item_detail_name)
    TextView tvItemDetailName;
    @BindView(R.id.tv_item_detail_desc)
    TextView tvItemDetailDesc;
    @BindView(R.id.tv_item_detail_fwjs)
    TextView tvItemDetailFwjs;
    @BindView(R.id.rv_item_detail)
    RecyclerView rvItemDetail;
    private ApointMentItem item;
    private List<ApointMentPet> petList;
    private ApointMentItemPrice apointMentItemPrice;
    private List<com.haotang.pet.entity.Banner> bannerList;
    private List<String> detailPicList;
    private String shareImg;
    private String shareTitle;
    private String shareTxt;
    private String shareUrl;
    private ItemDetailPicAdapter itemDetailPicAdapter;
    private int tid = 0;//选中美容师级别
    private AppointBottomPetAdapter appointBottomPetAdapter;
    private int previous;
    private List<String> list = new ArrayList<String>();
    private int flag;
    private List<ApointMentPet> overTimePetList = new ArrayList<ApointMentPet>();
    private ItemToMorePet itemToMorePet = new ItemToMorePet();
    private List<Integer> myPetIds = new ArrayList<Integer>();
    private List<Integer> workerIdList = new ArrayList<Integer>();
    private AppointWorker localAppointWorker;
    private double lat;
    private double lng;
    private String appointment;
    private int serviceLoc;
    private ServiceShopAdd shop;
    private int shopId = 0;
    private int pickup;
    private boolean isOverTime;
    private int dayposition;
    private List<ApointMentItem> timeItemList = new ArrayList<ApointMentItem>();
    private String workerIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(this);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        serviceLoc = getIntent().getIntExtra("serviceLoc", 0);
        dayposition = getIntent().getIntExtra("dayposition", 0);
        pickup = getIntent().getIntExtra("pickup", 0);
        appointment = getIntent().getStringExtra("appointment");
        localAppointWorker = (AppointWorker) getIntent().getSerializableExtra("appointWorker");
        shop = (ServiceShopAdd) getIntent().getSerializableExtra("shop");
        if (localAppointWorker != null) {
            tid = localAppointWorker.getTid();
        }
        if (shop != null) {
            shopId = shop.shopId;
        }
        previous = getIntent().getIntExtra("previous", 0);
        item = (ApointMentItem) getIntent().getSerializableExtra("Item");
        petList = (List<ApointMentPet>) getIntent().getSerializableExtra("petList");
        apointMentItemPrice = (ApointMentItemPrice) getIntent().getSerializableExtra("apointMentItemPrice");
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
            if (apointMentItemPrice.getItemId() == item.getId()) {
                if (itemPriceList != null && itemPriceList.size() > 0) {
                    for (int k = 0; k < itemPriceList.size(); k++) {
                        ApointMentItemPrices apointMentItemPrices = itemPriceList.get(k);
                        if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                            item.setVipPrice10(apointMentItemPrices.getVipPrice10());
                            item.setVipPrice20(apointMentItemPrices.getVipPrice20());
                            item.setVipPrice30(apointMentItemPrices.getVipPrice30());
                            item.setPrice10(apointMentItemPrices.getPrice10());
                            item.setPrice20(apointMentItemPrices.getPrice20());
                            item.setPrice30(apointMentItemPrices.getPrice30());
                        }
                    }
                }
            }
        }
    }

    private void findView() {
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("详情");
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        ViewGroup.LayoutParams layoutParams = ibTitlebarOther.getLayoutParams();
        layoutParams.width = Math.round(80 * 2 / 3 * density);
        layoutParams.height = Math.round(80 * 2 / 3 * density);
        ibTitlebarOther.setLayoutParams(layoutParams);
        ibTitlebarOther.setBackgroundResource(R.drawable.icon_itemdetail_share);
        ibTitlebarOther.setVisibility(View.VISIBLE);
        if (item != null) {
            bannerList = item.getBannerList();
            detailPicList = item.getDetailPicList();
            shareImg = item.getShareImg();
            shareTitle = item.getShareTitle();
            shareTxt = item.getShareTxt();
            shareUrl = item.getShareUrl();
            Utils.setText(tvItemDetailNum, "已服务" + item.getServiceAmount() + "单", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvItemDetailName, item.getName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvItemDetailDesc, item.getDesc(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvItemDetailFwjs, item.getDesc(), "", View.VISIBLE, View.VISIBLE);
            if (item.getVipPrice() > 0 && item.getVipPrice() != item.getPrice()) {
                tvItemDetailVipprice.setVisibility(View.VISIBLE);
                if (Utils.isStrNull(item.getPriceSuffix())) {
                    if (Utils.checkLogin(this)) {
                        if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                            Utils.setText(tvItemDetailVipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        } else {
                            Utils.setText(tvItemDetailVipprice, "铜铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        Utils.setText(tvItemDetailVipprice, "金铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                    }
                } else {
                    if (Utils.checkLogin(this)) {
                        if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                            Utils.setText(tvItemDetailVipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        } else {
                            Utils.setText(tvItemDetailVipprice, "铜铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        Utils.setText(tvItemDetailVipprice, "金铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                    }
                }
            } else {
                tvItemDetailVipprice.setVisibility(View.GONE);
            }
            if (Utils.isStrNull(item.getPriceSuffix())) {
                Utils.setText(tvItemDetailPrice, "¥" + item.getPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
            } else {
                Utils.setText(tvItemDetailPrice, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
            }
            if (item.getState() == 1) {
                tvItemDetailAdd.setText("不支持");
                tvItemDetailAdd.setBackgroundResource(R.drawable.bg_bbbbbb);
            } else if (item.getState() == 2) {
                tvItemDetailAdd.setText("已包含");
                tvItemDetailAdd.setBackgroundResource(R.drawable.bg_bbbbbb);
            } else if (item.getState() == 3) {
                tvItemDetailAdd.setText("添加");
                tvItemDetailAdd.setBackgroundResource(R.drawable.bg_red_jianbian);
            } else if (item.getState() == 4) {
                tvItemDetailAdd.setText("已添加");
                tvItemDetailAdd.setBackgroundResource(R.drawable.bg_bbbbbb);
            }
            if (bannerList != null && bannerList.size() > 0) {
                bannerItemDetail.setVisibility(View.VISIBLE);
                setBanner();
            } else {
                bannerItemDetail.setVisibility(View.GONE);
            }
            if (detailPicList != null && detailPicList.size() > 0) {
                rvItemDetail.setVisibility(View.VISIBLE);
                rvItemDetail.setVisibility(View.VISIBLE);
                rvItemDetail.setHasFixedSize(true);
                rvItemDetail.setNestedScrollingEnabled(false);
                NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
                noScollFullLinearLayoutManager.setScrollEnabled(false);
                rvItemDetail.setLayoutManager(noScollFullLinearLayoutManager);
                itemDetailPicAdapter = new ItemDetailPicAdapter(R.layout.item_itemdetail_detailpic, detailPicList, this);
                rvItemDetail.setAdapter(itemDetailPicAdapter);
            } else {
                rvItemDetail.setVisibility(View.GONE);
            }
        }
    }

    private void setLinster() {
        if (itemDetailPicAdapter != null) {
            itemDetailPicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (detailPicList != null && detailPicList.size() > 0 && detailPicList.size() > position) {
                        Utils.imageBrower(ItemDetailActivity.this, position, detailPicList.toArray(new String[detailPicList.size()]));
                    }
                }
            });
        }
    }

    @OnClick({R.id.ib_titlebar_other, R.id.tv_item_detail_add, R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_other:
                if (Utils.isStrNull(shareUrl)) {
                    if (!shareUrl.startsWith("http:")
                            && !shareUrl.startsWith("https:")) {
                        shareUrl = CommUtil.getStaticUrl() + shareUrl;
                    }
                    if (shareUrl.contains("?")) {
                        shareUrl = shareUrl
                                + "&id="
                                + item.getId()
                                + "&count="
                                + item.getServiceAmount() + "&vipPrice="
                                + item.getVipPrice();
                    } else {
                        shareUrl = shareUrl
                                + "?id="
                                + item.getId()
                                + "&count="
                                + item.getServiceAmount() + "&vipPrice="
                                + item.getVipPrice();
                    }
                }
                Utils.share(ItemDetailActivity.this, shareImg, shareTitle, shareTxt, shareUrl, "1,2", 0,null);
                break;
            case R.id.tv_item_detail_add:
                List<Integer> availableMyPets = item.getAvailableMyPets();
                if (item.getState() == 1) {
                    ToastUtil.showToastShortBottom(mContext, "您的宠物暂不支持添加此单项");
                } else if (item.getState() == 2) {
                    ToastUtil.showToastShortBottom(mContext, "首席赠送，无法修改");
                }  else {
                    if (previous == Global.SWITCHSERVICE_TO_ITEMDETAIL) {
                        if (item.getState() == 3) {
                            item.setState(4);
                            tvItemDetailAdd.setText("已添加");
                            tvItemDetailAdd.setBackgroundResource(R.drawable.bg_bbbbbb);
                        } else if (item.getState() == 4) {
                            item.setState(3);
                            tvItemDetailAdd.setText("添加");
                            tvItemDetailAdd.setBackgroundResource(R.drawable.bg_red_jianbian);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("isOverTime", isOverTime);
                        intent.putExtra("appointment", appointment);
                        intent.putExtra("dayposition", dayposition);
                        intent.putExtra("pickup", pickup);
                        intent.putExtra("workerIds", workerIds);
                        intent.putExtra("petList", (Serializable) petList);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("itemToMorePet", itemToMorePet);
                        bundle.putSerializable("localAppointWorker", localAppointWorker);
                        bundle.putSerializable("item", item);
                        intent.putExtras(bundle);
                        setResult(Global.RESULT_OK, intent);
                    } else {
                        for (int i = 0; i < petList.size(); i++) {
                            petList.get(i).setState(1);
                            petList.get(i).setSelect(false);
                        }
                        for (int i = 0; i < availableMyPets.size(); i++) {
                            Integer integer = availableMyPets.get(i);
                            for (int j = 0; j < petList.size(); j++) {
                                ApointMentPet apointMentPet = petList.get(j);
                                apointMentPet.setPriceSuffix(item.getPriceSuffix());
                                if (integer == apointMentPet.getMyPetId()) {//宠物支持这个单项
                                    List<ApointMentItem> itemList = apointMentPet.getItemList();
                                    if (itemList != null && itemList.size() > 0) {
                                        petList.get(j).setState(3);
                                        for (int k = 0; k < itemList.size(); k++) {
                                            ApointMentItem apointMentItem = itemList.get(k);
                                            if (apointMentItem.getId() == item.getId()) {
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
                                    if (apointMentItem.getId() == item.getId()) {
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
                                    if (apointMentItem.getId() == item.getId()) {
                                        itemList1.remove(i);
                                        break;
                                    }
                                }
                                item.setState(3);
                                tvItemDetailAdd.setText("添加");
                                tvItemDetailAdd.setBackgroundResource(R.drawable.bg_red_jianbian);
                                EventBus.getDefault().post(new ItemDetailToAppointEvent(item, petList));
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
                                            itemList.add(item);
                                            apointMentPet.setItemList(itemList);
                                            break;
                                        }
                                    }
                                    myPetIds.clear();
                                    myPetIds.add(apointMentPet2.getMyPetId());
                                    timeItemList.clear();
                                    timeItemList.add(item);
                                    itemToMorePet.setApointMentItem(timeItemList);
                                    itemToMorePet.setMyPetIds(myPetIds);
                                    flag = 1;
                                    //如果选择了时间和美容师，需要校验添加这个单项以后，选中的时间还是否可约
                                    workerIdList.clear();
                                    mPDialog.showDialog();
                                    CommUtil.getBeauticianFreeTime(
                                            ItemDetailActivity.this,
                                            lat,
                                            lng,
                                            spUtil.getString(
                                                    "cellphone", ""),
                                            Global.getIMEI(ItemDetailActivity.this),
                                            Global.getCurrentVersion(ItemDetailActivity.this),
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
                break;
        }
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
                        ToastUtil.showToastShortBottom(ItemDetailActivity.this, msg);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "getSingles()数据异常e = " + e.toString());
                ToastUtil.showToastShortBottom(ItemDetailActivity.this, "数据异常");
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
            ToastUtil.showToastShortBottom(ItemDetailActivity.this, "请求失败");
        }
    };

    private void showTimeOverDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(ItemDetailActivity.this, R.layout.appoint_timeover_bottom_dialog, null);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(ItemDetailActivity.this)[0]);
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
                isOverTime = false;
                if (workerIdList.size() > 0) {
                    String workerIds = "";
                    for (int i = 0; i < workerIdList.size(); i++) {
                        if (i == workerIdList.size() - 1) {
                            workerIds = workerIds + workerIdList.get(i);
                        } else {
                            workerIds = workerIds + workerIdList.get(i) + ",";
                        }
                    }
                    Intent intent2 = new Intent(ItemDetailActivity.this, WorkerListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", shop);
                    intent2.putExtras(bundle);
                    intent2.putExtra("lat", lat);
                    intent2.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                    intent2.putExtra("workerIds", workerIds);
                    intent2.putExtra("lng", lng);
                    intent2.putExtra("previous", Global.ITEMDETAIL_TO_OVERTIME);
                    bundle.putSerializable("itemToMorePet", itemToMorePet);
                    intent2.putExtra("dayposition", dayposition);
                    if (localAppointWorker != null) {
                        intent2.putExtra("defaultWorkerTag", localAppointWorker.getDefaultWorkerTag());
                        intent2.putExtra("workerId", localAppointWorker.getWorkerId());
                    }
                    intent2.putExtra("pickup", pickup);
                    intent2.putExtra("appointment", appointment);
                    intent2.putExtra("serviceLoc", serviceLoc);
                    intent2.putExtra("petList", (Serializable) petList);
                    intent2.putExtra("isVip", getIntent().getBooleanExtra("isVip", true));
                    intent2.putExtra("tid", tid);
                    intent2.putExtra("strp", getOverTime_PetID_ServiceId_MyPetId_ItemIds());
                    startActivity(intent2);
                } else {
                    ToastUtil.showToastShortBottom(ItemDetailActivity.this, "您当前所选时间暂无可约美容师");
                }
            }
        });
        tv_appointtimeover_bottomdia_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择该美容师的其他时间
                pWinBottomDialog.dismiss();
                isOverTime = false;
                Intent intent1 = new Intent(ItemDetailActivity.this, AppointTimeActivity.class);
                Bundle bundle = new Bundle();
                intent1.putExtra("serviceCardId", getIntent().getIntExtra("serviceCardId", 0));
                bundle.putSerializable("shop", shop);
                if (localAppointWorker != null) {
                    bundle.putSerializable("appointWorker", localAppointWorker);
                }
                bundle.putSerializable("itemToMorePet", itemToMorePet);
                intent1.putExtras(bundle);
                intent1.putExtra("lat", lat);
                intent1.putExtra("previous", Global.ITEMDETAIL_TO_OVERTIME);
                intent1.putExtra("dayposition", dayposition);
                intent1.putExtra("lng", lng);
                intent1.putExtra("serviceLoc", serviceLoc);
                intent1.putExtra("petList", (Serializable) petList);
                intent1.putExtra("isVip", getIntent().getBooleanExtra("isVip", true));
                intent1.putExtra("tid", tid);
                intent1.putExtra("strp", getOverTime_PetID_ServiceId_MyPetId_ItemIds());
                startActivity(intent1);
            }
        });
    }

    private void setOnePetItem() {
        Log.e("TAG", "setOnePetItem");
        ApointMentPet apointMentPet = petList.get(0);
        List<ApointMentItem> itemList = apointMentPet.getItemList();
        if (itemList == null) {
            itemList = new ArrayList<ApointMentItem>();
        }
        ApointMentItem apointMentItem = new ApointMentItem(item.getPriceSuffix(),
                item.getId(), item.getName(), item.getPic(),
                item.getLabel());
        List<ApointMentItemPrices> itemPriceList =
                apointMentItemPrice.getItemPriceList();
        for (int j = 0; j < itemPriceList.size(); j++) {
            ApointMentItemPrices apointMentItemPrices = itemPriceList.get(j);
            if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                if (tid == 0) {
                    apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                    apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                }
                if (tid == 1) {
                    apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice10());
                    apointMentItem.setPrice(apointMentItemPrices.getPrice10());
                } else if (tid == 2) {
                    apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice20());
                    apointMentItem.setPrice(apointMentItemPrices.getPrice20());
                } else if (tid == 3) {
                    apointMentItem.setVipPrice(apointMentItemPrices.getVipPrice30());
                    apointMentItem.setPrice(apointMentItemPrices.getPrice30());
                }
                break;
            }
        }
        itemList.add(apointMentItem);
        apointMentPet.setItemList(itemList);
        item.setState(4);
        tvItemDetailAdd.setText("已添加");
        tvItemDetailAdd.setBackgroundResource(R.drawable.bg_bbbbbb);
        //同步petList数据
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet1 = petList.get(i);
            if (apointMentPet1.getMyPetId() == apointMentPet.getMyPetId()) {
                apointMentPet1.setItemList(apointMentPet.getItemList());
                break;
            }
        }
        if (previous == Global.ITEMLIST_TO_ITEMDETAIL) {//单项列表到单项详情界面
            Log.e("TAG", "单项详情petList = " + petList);
            Intent intent = new Intent();
            intent.putExtra("isOverTime", isOverTime);
            intent.putExtra("appointment", appointment);
            intent.putExtra("dayposition", dayposition);
            intent.putExtra("pickup", pickup);
            intent.putExtra("workerIds", workerIds);
            intent.putExtra("petList", (Serializable) petList);
            Bundle bundle = new Bundle();
            bundle.putSerializable("itemToMorePet", itemToMorePet);
            bundle.putSerializable("item", item);
            bundle.putSerializable("localAppointWorker", localAppointWorker);
            intent.putExtras(bundle);
            setResult(Global.RESULT_OK, intent);
        } else {
            ItemDetailToAppointEvent itemDetailToAppointEvent = new ItemDetailToAppointEvent();
            itemDetailToAppointEvent.setItem(item);
            itemDetailToAppointEvent.setPetList(petList);
            if (isOverTime) {
                itemDetailToAppointEvent.setWorkerAndTime(new WorkerAndTime(localAppointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
            }
            EventBus.getDefault().post(itemDetailToAppointEvent);
        }
    }

    private void showItemPetDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.appoint_pet_bottom_dialog, null);
        LinearLayout ll_appointpet_bottomdia_wc = (LinearLayout) customView.findViewById(R.id.ll_appointpet_bottomdia_wc);
        RecyclerView rv_appointpet_bottomdia = (RecyclerView) customView.findViewById(R.id.rv_appointpet_bottomdia);
        ImageView iv_appointpet_bottom_bg = (ImageView) customView.findViewById(R.id.iv_appointpet_bottom_bg);
        rv_appointpet_bottomdia.setHasFixedSize(true);
        rv_appointpet_bottomdia.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        rv_appointpet_bottomdia.addItemDecoration(new GridSpacingItemDecoration(5,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
        List<ApointMentItemPrices> itemPriceList = apointMentItemPrice.getItemPriceList();
        if (itemPriceList != null && itemPriceList.size() > 0) {
            for (int j = 0; j < itemPriceList.size(); j++) {
                ApointMentItemPrices apointMentItemPrices = itemPriceList.get(j);
                for (int k = 0; k < petList.size(); k++) {
                    ApointMentPet apointMentPet = petList.get(k);
                    if (apointMentItemPrices.getMyPetId() == apointMentPet.getMyPetId()) {
                        if (tid == 0) {
                            apointMentPet.setItemVipPrice(apointMentItemPrices.getVipPrice10());
                            apointMentPet.setItemPrice(apointMentItemPrices.getPrice10());
                        }
                        if (tid == 1) {
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
                        ToastUtil.showToastShortBottom(ItemDetailActivity.this, "首席赠送，无法修改");
                    } else if (petList.get(position).getState() == 1) {
                        ToastUtil.showToastShortBottom(ItemDetailActivity.this, "您的宠物暂不支持添加此单项");
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(this)[0]);
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
                            if (apointMentItem.getId() == item.getId()) {
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
                                        itemList.add(item);
                                        apointMentPet1.setItemList(itemList);
                                    }
                                }
                            }
                        }
                        timeItemList.clear();
                        timeItemList.add(item);
                        itemToMorePet.setApointMentItem(timeItemList);
                        itemToMorePet.setMyPetIds(myPetIds);
                        flag = 2;
                        //如果选择了时间和美容师，需要校验添加这个单项以后，选中的时间还是否可约
                        workerIdList.clear();
                        mPDialog.showDialog();
                        CommUtil.getBeauticianFreeTime(
                                ItemDetailActivity.this,
                                lat,
                                lng,
                                spUtil.getString(
                                        "cellphone", ""),
                                Global.getIMEI(ItemDetailActivity.this),
                                Global.getCurrentVersion(ItemDetailActivity.this),
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
        for (int i = 0; i < petList.size(); i++) {
            ApointMentPet apointMentPet = petList.get(i);
            if (apointMentPet.isSelect()) {
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                if (itemList == null) {
                    itemList = new ArrayList<ApointMentItem>();
                }
                itemList.add(new ApointMentItem(item.getPriceSuffix(),
                        item.getId(), item.getName(),
                        apointMentPet.getItemVipPrice(), apointMentPet.getItemPrice(),
                        item.getPic(), item.getLabel()));
                apointMentPet.setItemList(itemList);
            }
        }
        setItemData();
        if (previous == Global.ITEMLIST_TO_ITEMDETAIL) {//单项列表到单项详情界面
            Log.e("TAG", "单项详情petList = " + petList);
            Intent intent = new Intent();
            intent.putExtra("isOverTime", isOverTime);
            intent.putExtra("appointment", appointment);
            intent.putExtra("dayposition", dayposition);
            intent.putExtra("pickup", pickup);
            intent.putExtra("workerIds", workerIds);
            intent.putExtra("petList", (Serializable) petList);
            Bundle bundle = new Bundle();
            bundle.putSerializable("itemToMorePet", itemToMorePet);
            bundle.putSerializable("item", item);
            bundle.putSerializable("localAppointWorker", localAppointWorker);
            intent.putExtras(bundle);
            setResult(Global.RESULT_OK, intent);
        } else {
            EventBus.getDefault().post(new ItemDetailToAppointEvent(item, petList));
            if (isOverTime) {
                EventBus.getDefault().post(new WorkerAndTimeEvent(localAppointWorker, appointment, dayposition, pickup, itemToMorePet, workerIds));
            }
        }
    }

    private void setItemData() {
        //更新单项列表数据
        int num = 0;
        List<Integer> availableMyPets = item.getAvailableMyPets();
        if (availableMyPets != null && availableMyPets.size() > 0) {
            for (int j = 0; j < petList.size(); j++) {
                ApointMentPet apointMentPet = petList.get(j);
                List<ApointMentItem> itemList = apointMentPet.getItemList();
                if (itemList != null && itemList.size() > 0) {
                    for (int k = 0; k < itemList.size(); k++) {
                        ApointMentItem apointMentItem1 = itemList.get(k);
                        if (apointMentItem1.getId() == item.getId()) {
                            num++;
                        }
                    }
                }
            }
            if (num == availableMyPets.size()) {
                item.setState(4);
                tvItemDetailAdd.setText("已添加");
                tvItemDetailAdd.setBackgroundResource(R.drawable.bg_bbbbbb);
            } else {
                item.setState(3);
                tvItemDetailAdd.setText("添加");
                tvItemDetailAdd.setBackgroundResource(R.drawable.bg_red_jianbian);
            }
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ItemDetailWorkerAndTimeEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            isOverTime = true;
            appointment = event.getAppointment();
            localAppointWorker = event.getAppointWorker();
            dayposition = event.getDayposition();
            itemToMorePet = event.getItemToMorePet();
            workerIds = event.getWorkerIds();
            pickup = event.getPickup();
            //换了美容师或者时间回来为选中了这个单项的宠物加上这个单项
            if (petList.size() == 1) {
                setOnePetItem();
            } else {
                setMorePetItem();
            }
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

    private void setBanner() {
        list.clear();
        for (int i = 0; i < bannerList.size(); i++) {
            list.add(bannerList.get(i).pic);
        }
        bannerItemDetail.setImages(list)
                .setImageLoader(new GlideImageLoaderFourRound())
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        if (bannerList != null && bannerList.size() > 0 && bannerList.size() > position) {
            Utils.imageBrower(this, position, list.toArray(new String[list.size()]));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
