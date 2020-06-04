package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ChooseFosterPetAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.RefreshPetEvent;
import com.haotang.pet.entity.RefundCardEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogSuccess;
import com.haotang.pet.view.DividerLinearItemDecoration;
import com.umeng.analytics.MobclickAgent;

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
 * 选择寄养宠物页
 * 姜谷蓄
 */
public class ChooseFosterPetActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.srl_addfosterpet)
    SwipeRefreshLayout srlAddfosterpet;
    @BindView(R.id.rv_addfosterpet)
    RecyclerView rvAddfosterpet;
    @BindView(R.id.tv_addfosterpet_footer)
    TextView tv_addfosterpet_footer;
    @BindView(R.id.tv_addfosterpet_bindcarddesc)
    TextView tv_addfosterpet_bindcarddesc;
    private int shopId;
    private int myPetId;
    private List<Pet> list = new ArrayList<Pet>();
    private ChooseFosterPetAdapter chooseFosterPetAdapter;
    private List<Integer> toothCardId;
    private int cardId;
    private int previous;
    private String bindMyPetId;
    private int selectPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setListener();
        getData();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        MApplication.listAppoint.add(this);
        Intent intent = getIntent();
        cardId = intent.getIntExtra("cardId", 0);
        toothCardId = (List<Integer>) intent.getSerializableExtra("cardIds");
        shopId = intent.getIntExtra("shopId", 0);
        myPetId = intent.getIntExtra("myPetId", 0);
        previous = intent.getIntExtra("previous", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_add_foster_pet);
        ButterKnife.bind(this);
    }

    private void setView() {
        if (previous == Global.BUYCARD_TO_CHOOSEPET) {
            tvTitlebarTitle.setText("绑定9.9元刷牙年卡");
            tvTitlebarOther.setVisibility(View.GONE);
            tv_addfosterpet_bindcarddesc.setVisibility(View.VISIBLE);
        } else {
            tvTitlebarTitle.setText("宠物选择");
            tvTitlebarOther.setVisibility(View.VISIBLE);
            tv_addfosterpet_bindcarddesc.setVisibility(View.GONE);
            tvTitlebarOther.setText("完成");
        }
        srlAddfosterpet.setRefreshing(true);
        srlAddfosterpet.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvAddfosterpet.setHasFixedSize(true);
        rvAddfosterpet.setLayoutManager(new LinearLayoutManager(this));
        chooseFosterPetAdapter = new ChooseFosterPetAdapter(R.layout.item_choosemypet, list, previous);
        rvAddfosterpet.setAdapter(chooseFosterPetAdapter);
        rvAddfosterpet.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL, DensityUtil.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.aEEEEEE)));
    }

    private void setListener() {
        chooseFosterPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.size() > 0 && list.size() > position) {
                    if (previous == Global.BUYCARD_TO_CHOOSEPET) {
                        if (list.get(position).selected == 2) {
                            return;
                        }
                        if (toothCardId != null && toothCardId.size() > 0) {
                            int canSelect = toothCardId.size();
                            int nowSeclect = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).isSelect()){
                                    nowSeclect++;
                                }else {
                                    list.get(i).setSelect(false);
                                }
                            }
                            Utils.mLogError("可选条目"+toothCardId.size()+"------已选条目"+nowSeclect);
                            if (nowSeclect<canSelect){
                                if (list.get(position).isSelect()){
                                    list.get(position).setSelect(false);
                                }else {
                                    list.get(position).setSelect(true);
                                }
                            }else {
                                list.get(position).setSelect(false);
                                int choose = 0;
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).isSelect()){
                                        choose++;
                                    }
                                }
                                if (canSelect==choose){
                                    ToastUtil.showToastShortBottom(mContext,"您最多可绑"+toothCardId.size()+"只宠物");
                                }
                            }

                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setSelect(false);
                            }
                            list.get(position).setSelect(true);
                        }

                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSelect(false);
                        }
                        list.get(position).setSelect(true);
                    }
                    chooseFosterPetAdapter.notifyDataSetChanged();
                }
            }
        });
        srlAddfosterpet.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData() {
        srlAddfosterpet.setRefreshing(true);
        list.clear();
        mPDialog.showDialog();
        if (previous == Global.BUYCARD_TO_CHOOSEPET) {
            CommUtil.queryCustomerPets(this, 0, 0, 0, 0, 0, shopId, 10, "", 0, petHandler);
        } else {
            CommUtil.queryCustomerPets(this, 0, 0, 0, 0, 0, shopId, 2, "", 0, petHandler);
        }
    }

    private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler() {
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
                        if (jdata.has("pets") && !jdata.isNull("pets")) {
                            JSONArray jpets = jdata.getJSONArray("pets");
                            if (jpets.length() > 0) {
                                for (int i = 0; i < jpets.length(); i++) {
                                    list.add(Pet.json2Entity(jpets.getJSONObject(i)));
                                }
                            }
                        }
                    }
                    if (list.size() <= 0) {
                        chooseFosterPetAdapter.setEmptyView(setEmptyViewBase(2, "没有看见您的宠物", "此处只显示可以刷牙的宠物哦~", null));
                    }
                } else {
                    chooseFosterPetAdapter.setEmptyView(setEmptyViewBase(1, msg, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getData();
                        }
                    }));
                }
            } catch (JSONException e) {
                chooseFosterPetAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                }));
                e.printStackTrace();
            }
            boolean isCanBind = false;
            if (list.size() > 0) {
                if (myPetId > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (myPetId == list.get(i).customerpetid) {
                            list.get(i).setSelect(true);
                            break;
                        }
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).selected != 2) {//可绑定
                        isCanBind = true;
                        break;
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).selected == 2) {//不可绑定
                        list.get(i).isShow = true;
                        break;
                    }
                }
            }
            if (previous == Global.BUYCARD_TO_CHOOSEPET) {
                if (isCanBind) {
                    tv_addfosterpet_footer.setText("确认绑定");
                } else {
                    tv_addfosterpet_footer.setText("+  添加宠物");
                }
            } else {
                tv_addfosterpet_footer.setText("+  添加宠物");
            }
            chooseFosterPetAdapter.notifyDataSetChanged();
            srlAddfosterpet.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            srlAddfosterpet.setRefreshing(false);
            mPDialog.closeDialog();
            chooseFosterPetAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            }));
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other, R.id.tv_addfosterpet_footer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                if (list.size() > 0) {
                    int selectMyPetId = 0;
                    int selectPetId = 0;
                    String selectNickName = "";
                    String selectImg = "";
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect()) {
                            selectMyPetId = list.get(i).customerpetid;
                            selectPetId = list.get(i).id;
                            selectNickName = list.get(i).nickName;
                            selectImg = list.get(i).image;
                            break;
                        }
                    }
                    if (selectMyPetId > 0) {
                        Intent intent = new Intent();
                        intent.putExtra("selectMyPetId", selectMyPetId);
                        intent.putExtra("selectPetId", selectPetId);
                        intent.putExtra("selectNickName", selectNickName);
                        intent.putExtra("selectImg", selectImg);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "请先选择宠物哦");
                    }
                } else {
                    ToastUtil.showToastShortBottom(mContext, "请先添加宠物哦");
                }
                break;
            case R.id.tv_addfosterpet_footer:
                if (tv_addfosterpet_footer.getText().toString().equals("确认绑定")) {
                    String selectNickName = "";
                    bindMyPetId = "";
                    selectPet = 0;
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer petbuffer = new StringBuffer();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect()) {
                            buffer.append(list.get(i).customerpetid + ",");
                            selectNickName = petbuffer.append(list.get(i).nickName+",").toString();
                            selectPet++;
                        }
                    }
                    bindMyPetId = buffer.toString();
                    if (!"".equals(bindMyPetId)) {
                        if (selectPet ==1){
                            selectNickName = selectNickName.replace(",","");
                        }else {
                            selectNickName = petbuffer.substring(0,petbuffer.length()-1);
                        }
                        String str = "是否绑定到@@" + selectNickName + "@@绑定成功后不可解绑";
                        String[] split = str.split("@@");
                        SpannableString ss = new SpannableString(str.replace("@@", ""));
                        int startIndex = split[0].length();
                        int endIndex = startIndex + split[1].length();
                        ss.setSpan(
                                new ForegroundColorSpan(mContext.getResources().getColor(
                                        R.color.aD0021B)), startIndex, endIndex,
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        new AlertDialogDefault(mContext).builder()
                                .setTitle("绑定刷牙卡").setMsg(ss).isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("立即绑卡", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPDialog.showDialog();
                                if (toothCardId!=null&&toothCardId.size()>0){
                                    StringBuffer bindCardId = new StringBuffer();
                                    for (int i = 0; i < selectPet; i++) {
                                        bindCardId.append(toothCardId.get(i)+",");
                                    }
                                    Utils.mLogError(bindMyPetId);
                                    Utils.mLogError(bindCardId.toString());
                                    CommUtil.bind(mContext, bindCardId.toString(), bindMyPetId, bindHandler);
                                }else {
                                    CommUtil.bind(mContext, String.valueOf(cardId), bindMyPetId, bindHandler);
                                }


                            }
                        }).show();
                    } else {
                        ToastUtil.showToastShortBottom(mContext, "请先选择宠物哦");
                    }
                } else {
                    startActivity(new Intent(mContext, PetAddActivity.class));
                }
                break;
        }
    }

    private AsyncHttpResponseHandler bindHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                if (code == 0) {
                    EventBus.getDefault().post(new RefundCardEvent(true));
                    if (toothCardId!=null&&toothCardId.size()>0){//判断是否买的卡全部绑定了
                        if (toothCardId.size()==selectPet){
                            showBindSuccess();
                        }else {
                            int canBindNum = toothCardId.size() - selectPet;
                            new AlertDialogDefault(mContext).builder()
                                    .setTitle("绑定成功").setMsg("剩余"+canBindNum+"张刷牙年卡未绑定，点击查看订单进行绑定").isOneBtn(false).setNegativeButton("查看卡订单", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (GiftCardOrderActivity.act != null) {
                                        GiftCardOrderActivity.act.finish();
                                    }
                                    startActivity(new Intent(mContext, GiftCardOrderActivity.class));
                                    finish();
                                }
                            }).setPositiveButton("查看卡包", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(mContext, MyCardActivity.class));
                                    finish();
                                }
                            }).show();
                        }
                    }else {
                        showBindSuccess();
                    }
                } else {
                    ToastUtil.showToastShortCenter(mContext, msg);
                }
            } catch (JSONException e) {
                ToastUtil.showToastShortCenter(mContext, "数据异常");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastShortCenter(mContext, "请求失败");
        }
    };

    private void showBindSuccess(){
        new AlertDialogSuccess(mContext).builder().setMsg("绑定成功").setNegativeButton("查看卡订单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GiftCardOrderActivity.act != null) {
                    GiftCardOrderActivity.act.finish();
                }
                startActivity(new Intent(mContext, GiftCardOrderActivity.class));
                finish();
            }
        }).setPositiveButton("查看卡包", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyCardActivity.class));
                finish();
            }
        }).show();
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
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
