package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ChooseMyPetAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.RefreshPetEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.DividerLinearItemDecoration;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 游泳，寄养，选择我的宠物下单
 */
public class ChooseMyPetActivity extends SuperActivity {
    @BindView(R.id.bt_choosemypet_dog)
    Button btChoosemypetDog;
    @BindView(R.id.bt_choosemypet_cat)
    Button btChoosemypetCat;
    @BindView(R.id.rv_choosemypet)
    RecyclerView rvChoosemypet;
    @BindView(R.id.tv_choosemypet_submit)
    TextView tv_choosemypet_submit;
    @BindView(R.id.rl_choosemypet_swim)
    RelativeLayout rl_choosemypet_swim;
    @BindView(R.id.rl_choosemypet_service)
    RelativeLayout rl_choosemypet_service;
    @BindView(R.id.tv_choosemypet_service_submit)
    TextView tv_choosemypet_service_submit;
    @BindView(R.id.tv_choosemypet_service_title)
    TextView tv_choosemypet_service_title;
    @BindView(R.id.tv_choosemypet_mypet)
    TextView tv_choosemypet_mypet;
    @BindView(R.id.v_title_slide)
    View vTitleSlide;
    private List<Pet> list = new ArrayList<Pet>();
    private List<Pet> localList = new ArrayList<Pet>();
    private ChooseMyPetAdapter chooseMyPetAdapter;
    private List<Pet> listPets;
    private String petKindstr;
    private boolean isCat;
    private int maxPet;
    private int localPetkind = 1;
    private int previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorBar();
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
        MApplication.listAppoint.add(this);
        Intent intent = getIntent();
        maxPet = intent.getIntExtra("maxPet", 0);
        petKindstr = intent.getStringExtra("petKindstr");
        previous = intent.getIntExtra("previous", 0);
        listPets = (List<Pet>) intent.getSerializableExtra("listPets");
    }

    private void findView() {
        setContentView(R.layout.activity_choose_my_pet);
        ButterKnife.bind(this);
    }

    private void setView() {
        vTitleSlide.setVisibility(View.VISIBLE);
        if (previous == Global.MY_CUSTOMERPET) {
            tv_choosemypet_mypet.setVisibility(View.GONE);
            rl_choosemypet_service.setVisibility(View.VISIBLE);
            rl_choosemypet_swim.setVisibility(View.GONE);
            tv_choosemypet_service_submit.setVisibility(View.GONE);
            tv_choosemypet_service_title.setText("我的宠物");
        } else if (previous == Global.SERVICE_NEW_TO_ADD_PET) {
            tv_choosemypet_mypet.setVisibility(View.GONE);
            rl_choosemypet_service.setVisibility(View.VISIBLE);
            rl_choosemypet_swim.setVisibility(View.GONE);
            tv_choosemypet_service_submit.setVisibility(View.VISIBLE);
            tv_choosemypet_service_title.setText("宠物选择");
        } else if (previous == Global.SWIM_TO_CUSTOMERPET) {
            tv_choosemypet_mypet.setVisibility(View.VISIBLE);
            rl_choosemypet_service.setVisibility(View.GONE);
            rl_choosemypet_swim.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(petKindstr) && petKindstr.contains("2")) {
                isCat = true;
            }
            if (isCat) {
                btChoosemypetCat.setEnabled(true);
                btChoosemypetCat.setTextColor(getResources().getColor(R.color.aBB996C));
                btChoosemypetCat.setBackgroundResource(R.drawable.choosepet_right_unselect);
            } else {
                //屏蔽猫的点击
                btChoosemypetCat.setEnabled(false);
                btChoosemypetCat.setTextColor(getResources().getColor(R.color.white));
                btChoosemypetCat.setBackgroundResource(R.drawable.bg_member_right_gray);
            }
        }
        rvChoosemypet.setHasFixedSize(true);
        rvChoosemypet.setLayoutManager(new LinearLayoutManager(this));
        chooseMyPetAdapter = new ChooseMyPetAdapter(R.layout.item_choosemypet_candel, list, previous);
        rvChoosemypet.setAdapter(chooseMyPetAdapter);
        rvChoosemypet.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL, DensityUtil.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.aEEEEEE)));
        rvChoosemypet.setItemAnimator(null);
    }

    /**
     * 修改 和 忘记 密码
     */
    private void showSetPassWordDialog(final Pet pet) {
        ViewGroup customView = (ViewGroup) View.inflate(mContext, R.layout.setpassword_dialog, null);
        TextView tv_setpassworddialog_wj = (TextView) customView.findViewById(R.id.tv_setpassworddialog_wj);
        TextView tv_setpassworddialog_xg = (TextView) customView.findViewById(R.id.tv_setpassworddialog_xg);
        TextView tv_setpassworddialog_qx = (TextView) customView.findViewById(R.id.tv_setpassworddialog_qx);
        RelativeLayout rl_setpassworddialog_parent = (RelativeLayout) customView.findViewById(R.id.rl_setpassworddialog_parent);
        tv_setpassworddialog_qx.setText("取消");
        tv_setpassworddialog_xg.setText("删除");
        tv_setpassworddialog_wj.setText("主人确定不要我了么？");
        final PopupWindow popupWindow = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setWidth(Utils.getDisplayMetrics(mContext)[0]);
        popupWindow.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        //忘记密码
        tv_setpassworddialog_wj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //判断是否设置支付密码

            }
        });
        //修改支付密码
        tv_setpassworddialog_xg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mPDialog.showDialog();
                CommUtil.deleteCustomerPet(mContext, spUtil.getString("cellphone", ""),
                        Global.getCurrentVersion(mContext), Global.getIMEI(mContext),
                        pet.customerpetid, deleteHandler);
            }
        });
        tv_setpassworddialog_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        rl_setpassworddialog_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    private void setLinster() {
        chooseMyPetAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("TAG", "position = " + position);
                if (list.size() > 0 && list.size() > position) {
                    final Pet pet = list.get(position);
                    switch (view.getId()) {
                        case R.id.iv_item_choosemypet_del:
                            //是否绑定刷牙卡
                            if (pet.isBindExtraItemCard == 1) {
                                String isBindExtraItemCardTip = pet.isBindExtraItemCardTip;
                                String[] split = isBindExtraItemCardTip.split("@@");
                                SpannableString ss = new SpannableString(isBindExtraItemCardTip.replace("@@", ""));
                                int startIndex = split[0].length();
                                int endIndex = startIndex + split[1].length();
                                ss.setSpan(
                                        new ForegroundColorSpan(mContext.getResources().getColor(
                                                R.color.aD0021B)), startIndex, endIndex,
                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                new AlertDialogDefault(mContext).builder()
                                        .setTitle("是否确认删除？").setMsg(ss).isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setPositiveButton("确认删除", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPDialog.showDialog();
                                        CommUtil.deleteCustomerPet(mContext, spUtil.getString("cellphone", ""),
                                                Global.getCurrentVersion(mContext), Global.getIMEI(mContext),
                                                pet.customerpetid, deleteHandler);
                                    }
                                }).show();
                            } else {
                                showSetPassWordDialog(pet);
                            }

                            break;
                        case R.id.rl_item_choosemypetinfo:
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).isShowDel){
                                    list.get(i).setShowDel(false);
                                    chooseMyPetAdapter.notifyItemChanged(i);
                                }
                            }
                            if (previous == Global.MY_CUSTOMERPET) {
                                startActivity(new Intent(ChooseMyPetActivity.this, PetDetailActivity.class).putExtra("customerpetid", pet.customerpetid));
                            } else if (previous == Global.SERVICE_NEW_TO_ADD_PET) {
                                if (pet != null) {
                                    int num = 0;
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).isSelect()) {
                                            num++;
                                        }
                                    }
                                    Log.e("TAG", "num = " + num);
                                    if (num >= maxPet && !pet.isSelect()) {
                                        ToastUtil.showToastShortBottom(ChooseMyPetActivity.this, "最多选择" + maxPet + "只宠物");
                                    } else {
                                        pet.setSelect(!pet.isSelect());
                                        if (listPets != null) {
                                            listPets = new ArrayList<Pet>();
                                        }
                                        listPets.clear();
                                        for (int i = 0; i < list.size(); i++) {
                                            Pet pet1 = list.get(i);
                                            if (pet1 != null && pet1.isSelect()) {
                                                listPets.add(pet1);
                                            }
                                        }
                                        chooseMyPetAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else if (previous == Global.SWIM_TO_CUSTOMERPET) {
                                if (pet != null) {
                                    int num = 0;
                                    for (int i = 0; i < localList.size(); i++) {
                                        if (localList.get(i).isSelect()) {
                                            num++;
                                        }
                                    }
                                    if (num >= maxPet && !pet.isSelect()) {
                                        ToastUtil.showToastShortBottom(ChooseMyPetActivity.this, "最多选择" + maxPet + "只宠物");
                                    } else {
                                        pet.setSelect(!pet.isSelect());
                                        if (listPets != null) {
                                            listPets = new ArrayList<Pet>();
                                        }
                                        listPets.clear();
                                        for (int i = 0; i < list.size(); i++) {
                                            Pet pet1 = list.get(i);
                                            if (pet1 != null && pet1.isSelect()) {
                                                listPets.add(pet1);
                                            }
                                        }
                                        chooseMyPetAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            break;
                        case R.id.rl_item_choosemypet_del:
                            if (list.size() > 0 && list.size() > position) {
                                list.get(position).setShowDel(false);
                                chooseMyPetAdapter.notifyItemChanged(position);
                            }
                            break;
                    }
                }
            }
        });
        chooseMyPetAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.size() > 0 && list.size() > position) {
                    switch (view.getId()){
                        case R.id.rl_item_choosemypetinfo:
                            if (previous == Global.MY_CUSTOMERPET){
                                for (int i = 0; i < list.size(); i++) {
                                    if (position==i){
                                        list.get(position).setShowDel(true);
                                    }else {
                                        list.get(i).setShowDel(false);
                                    }
                                }
                                chooseMyPetAdapter.notifyDataSetChanged();
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void getData() {
        localList.clear();
        list.clear();
        mPDialog.showDialog();
        CommUtil.queryCustomerPets(this, getIntent().getIntExtra("serviceId", 0), 0, 0, getIntent().getIntExtra("serviceType", 0), 0, 0, 0, "", 0, petHandler);
    }

    private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("pets") && !jdata.isNull("pets")) {
                            JSONArray jpets = jdata.getJSONArray("pets");
                            if (jpets.length() > 0) {
                                localList.clear();
                                for (int i = 0; i < jpets.length(); i++) {
                                    localList.add(Pet.json2Entity(jpets.getJSONObject(i)));
                                }
                                if (previous == Global.SWIM_TO_CUSTOMERPET) {
                                    if (listPets != null && listPets.size() > 0) {
                                        boolean isDogSelect = false;
                                        boolean isCatSelect = false;
                                        for (int i = 0; i < localList.size(); i++) {
                                            Pet pet = localList.get(i);
                                            for (int j = 0; j < listPets.size(); j++) {
                                                Pet pet1 = listPets.get(j);
                                                if (pet.customerpetid == pet1.customerpetid) {
                                                    pet.setSelect(true);
                                                    if (pet.kindid == 1) {
                                                        isDogSelect = true;
                                                    } else if (pet.kindid == 2) {
                                                        isCatSelect = true;
                                                    }
                                                }
                                            }
                                        }
                                        if (isCatSelect && !isDogSelect) {
                                            localPetkind = 2;
                                            setPetKind();
                                        } else {
                                            localPetkind = 1;
                                            setPetKind();
                                        }
                                    } else {
                                        localPetkind = 1;
                                        setPetKind();
                                    }
                                    if (list.size() > 0 && listPets != null && listPets.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            Pet pet = list.get(i);
                                            for (int j = 0; j < listPets.size(); j++) {
                                                Pet pet1 = listPets.get(j);
                                                if (pet.customerpetid == pet1.customerpetid) {
                                                    pet.setSelect(true);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    list.clear();
                                    for (int i = 0; i < jpets.length(); i++) {
                                        list.add(Pet.json2Entity(jpets.getJSONObject(i)));
                                    }
                                    if (list.size() > 0 && listPets != null && listPets.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            Pet pet = list.get(i);
                                            for (int j = 0; j < listPets.size(); j++) {
                                                Pet pet1 = listPets.get(j);
                                                if (pet.customerpetid == pet1.customerpetid) {
                                                    pet.setSelect(true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Utils.Exit(ChooseMyPetActivity.this, resultCode);
                }
            } catch (Exception e) {
                Log.e("TAG", "e = " + e.toString());
                e.printStackTrace();
            }
            if (localList.size() <= 0) {
                chooseMyPetAdapter.setEmptyView(setEmptyViewBase(2, "暂无宠物哦~",
                        R.drawable.icon_no_mypet, null));
            }
            chooseMyPetAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private AsyncHttpResponseHandler deleteHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    ToastUtil.showDeleteToast(mContext);
                    getData();
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(mContext,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortCenter(getApplicationContext(),
                        "数据异常");
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isShowDel){
                        list.get(i).setShowDel(false);
                        chooseMyPetAdapter.notifyItemChanged(i);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
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

    private void setPetKind() {
        list.clear();
        if (localPetkind == 1) {//狗
            btChoosemypetDog.setTextColor(getResources().getColor(R.color.black));
            btChoosemypetDog.setBackgroundResource(R.drawable.choosepet_left_select);
            if (isCat) {
                btChoosemypetCat.setTextColor(getResources().getColor(R.color.aBB996C));
                btChoosemypetCat.setBackgroundResource(R.drawable.choosepet_right_unselect);
            } else {
                //屏蔽猫的点击
                btChoosemypetCat.setTextColor(getResources().getColor(R.color.white));
                btChoosemypetCat.setBackgroundResource(R.drawable.bg_member_right_gray);
            }
            if (localList.size() > 0) {
                for (int i = 0; i < localList.size(); i++) {
                    Pet pet = localList.get(i);
                    if (pet != null && pet.kindid == 1) {
                        list.add(pet);
                    }
                }
            }
        } else if (localPetkind == 2) {//猫
            btChoosemypetDog.setTextColor(getResources().getColor(R.color.aBB996C));
            btChoosemypetCat.setTextColor(getResources().getColor(R.color.black));
            btChoosemypetDog.setBackgroundResource(R.drawable.choosepet_left_unselect);
            btChoosemypetCat.setBackgroundResource(R.drawable.choosepet_right_select);
            if (localList.size() > 0) {
                for (int i = 0; i < localList.size(); i++) {
                    Pet pet = localList.get(i);
                    if (pet != null && pet.kindid == 2) {
                        list.add(pet);
                    }
                }
            }
        }
        chooseMyPetAdapter.notifyDataSetChanged();
        if (list.size() <= 0) {
            chooseMyPetAdapter.setEmptyView(setEmptyViewBase(2, "暂无宠物哦~",
                    R.drawable.icon_no_mypet, null));
        }
    }

    @OnClick({R.id.ib_choosemypet_back, R.id.bt_choosemypet_dog, R.id.bt_choosemypet_cat,
            R.id.tv_choosemypet_submit, R.id.ib_choosemypet_service_back, R.id.tv_choosemypet_service_submit, R.id.ll_choosemypet_footer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_choosemypet_service_back:
                finishWithAnimation();
                break;
            case R.id.ib_choosemypet_back:
                finishWithAnimation();
                break;
            case R.id.bt_choosemypet_dog:
                localPetkind = 1;
                setPetKind();
                break;
            case R.id.bt_choosemypet_cat:
                localPetkind = 2;
                setPetKind();
                break;
            case R.id.tv_choosemypet_service_submit:
                List<Pet> selectList1 = new ArrayList<Pet>();
                selectList1.clear();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Pet pet = list.get(i);
                        if (pet != null && pet.isSelect()) {
                            selectList1.add(pet);
                        }
                    }
                    if (selectList1.size() > 0) {
                        Log.e("TAG", "selectList = " + selectList1.toString());
                        Intent intent = new Intent();
                        intent.putExtra("myPetLIst", (Serializable) selectList1);
                        setResult(Global.RESULT_OK, intent);
                        finishWithAnimation();
                    } else {
                        ToastUtil.showToastShortBottom(ChooseMyPetActivity.this, "请先选择宠物");
                    }
                }
                break;
            case R.id.tv_choosemypet_submit:
                List<Pet> selectList = new ArrayList<Pet>();
                selectList.clear();
                if (localList.size() > 0) {
                    for (int i = 0; i < localList.size(); i++) {
                        Pet pet = localList.get(i);
                        if (pet != null && pet.isSelect()) {
                            selectList.add(pet);
                        }
                    }
                    if (selectList.size() > 0) {
                        Log.e("TAG", "selectList = " + selectList.toString());
                        Intent intent = new Intent();
                        intent.putExtra("myPetLIst", (Serializable) selectList);
                        setResult(Global.RESULT_OK, intent);
                        finishWithAnimation();
                    } else {
                        ToastUtil.showToastShortBottom(ChooseMyPetActivity.this, "请先选择宠物");
                    }
                }
                break;
            case R.id.ll_choosemypet_footer:
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isShowDel){
                        list.get(i).setShowDel(false);
                        chooseMyPetAdapter.notifyItemChanged(i);
                    }
                }
                startActivity(new Intent(ChooseMyPetActivity.this, PetAddActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshPetEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            getData();
        }
    }
}
