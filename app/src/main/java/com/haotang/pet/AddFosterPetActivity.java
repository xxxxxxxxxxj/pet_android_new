package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AddFosterPetAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.RefreshPetEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
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
 * 寄养添加宠物界面
 */
public class AddFosterPetActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.srl_addfosterpet)
    SwipeRefreshLayout srlAddfosterpet;
    @BindView(R.id.rv_addfosterpet)
    RecyclerView rvAddfosterpet;
    private int shopId;
    private String mypetIds;
    private int roomType;
    private List<Pet> list = new ArrayList<Pet>();
    private List<Integer> myPetIdlist = new ArrayList<Integer>();
    private AddFosterPetAdapter addFosterPetAdapter;

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
        shopId = intent.getIntExtra("shopId", 0);
        mypetIds = intent.getStringExtra("mypetIds");
        roomType = intent.getIntExtra("roomType", 0);
        myPetIdlist.clear();
        if (Utils.isStrNull(mypetIds)) {
            if (mypetIds.contains(",")) {
                String[] split = mypetIds.split(",");
                if (split != null && split.length > 0) {
                    for (int i = 0; i < split.length; i++) {
                        myPetIdlist.add(Integer.parseInt(split[i]));
                    }
                }
            } else {
                myPetIdlist.add(Integer.parseInt(mypetIds));
            }
        }
    }

    private void findView() {
        setContentView(R.layout.activity_add_foster_pet);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("宠物选择");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("完成");
        srlAddfosterpet.setRefreshing(true);
        srlAddfosterpet.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvAddfosterpet.setHasFixedSize(true);
        rvAddfosterpet.setLayoutManager(new LinearLayoutManager(this));
        addFosterPetAdapter = new AddFosterPetAdapter(R.layout.item_choosemypet, list);
        rvAddfosterpet.setAdapter(addFosterPetAdapter);
        rvAddfosterpet.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL, DensityUtil.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.aEEEEEE)));
    }

    private void setListener() {
        addFosterPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.size() > 0 && list.size() > position) {
                    Pet pet = list.get(position);
                    if (pet.selected == 2) {
                        ToastUtil.showToastShortBottom(mContext, "猫狗不能同住");
                        return;
                    }
                    if (pet.isAdd) {
                        return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelect(false);
                    }
                    list.get(position).setSelect(true);
                    addFosterPetAdapter.notifyDataSetChanged();
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
        CommUtil.queryCustomerPets(this, 0, 0, 0, 0, 0, shopId, 2, mypetIds, roomType, petHandler);
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
                        addFosterPetAdapter.setEmptyView(setEmptyViewBase(2, "没有看见您的宠物", null));
                    }
                } else {
                    addFosterPetAdapter.setEmptyView(setEmptyViewBase(1, msg, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getData();
                        }
                    }));
                }
            } catch (JSONException e) {
                addFosterPetAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                }));
                e.printStackTrace();
            }
            if (list.size() > 0 && myPetIdlist.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Pet pet = list.get(i);
                    for (int j = 0; j < myPetIdlist.size(); j++) {
                        int myPetId = myPetIdlist.get(j);
                        if (myPetId == pet.customerpetid) {
                            pet.isAdd = true;
                            break;
                        }
                    }
                }
            }
            addFosterPetAdapter.notifyDataSetChanged();
            srlAddfosterpet.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            srlAddfosterpet.setRefreshing(false);
            mPDialog.closeDialog();
            addFosterPetAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", new View.OnClickListener() {
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
                    int addMyPetId = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect()) {
                            addMyPetId = list.get(i).customerpetid;
                            break;
                        }
                    }
                    if (addMyPetId > 0) {
                        Intent intent = new Intent();
                        intent.putExtra("addMyPetId", addMyPetId);
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
                startActivity(new Intent(mContext, PetAddActivity.class));
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
