package com.haotang.pet;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.PetArchivesImgAdapter;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 宠物档案照片墙
 */
public class PetArchivesImgsActivity extends SuperActivity {
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_pet_archives_imgs)
    RecyclerView rvPetArchivesImgs;
    @BindView(R.id.mrl_pet_archives_imgs)
    MaterialRefreshLayout mrlPetArchivesImgs;
    private int customerpetid;
    private List<String> imgList = new ArrayList<String>();
    private List<String> localImgList = new ArrayList<String>();
    private PetArchivesImgAdapter petArchivesImgAdapter;
    private int page = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        act = this;
        MApplication.listAppoint.add(this);
        customerpetid = getIntent().getIntExtra("customerpetid", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_pet_archives_imgs);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("照片墙");
        rvPetArchivesImgs.setHasFixedSize(true);
        rvPetArchivesImgs.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        rvPetArchivesImgs.addItemDecoration(new GridSpacingItemDecoration(3,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
        imgList.clear();
        petArchivesImgAdapter = new PetArchivesImgAdapter(R.layout.item_petarchives_img, imgList);
        rvPetArchivesImgs.setAdapter(petArchivesImgAdapter);
        mrlPetArchivesImgs.autoRefresh();
    }

    private void setLinster() {
        petArchivesImgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (imgList.size() > position) {
                    String[] imgs = new String[imgList.size()];
                    Utils.imageBrower(PetArchivesImgsActivity.this, position, imgList.toArray(imgs));
                }
            }
        });
        mrlPetArchivesImgs.setMaterialRefreshListener(new MaterialRefreshListener() {

            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                loadMore();
            }
        });
    }

    private void refresh() {
        page = 1;
        imgList.clear();
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void getData() {
        localImgList.clear();
        mPDialog.showDialog();
        CommUtil.customerPetImg(this, customerpetid, page, pageSize,
                customerPetImgHandler);
    }

    private AsyncHttpResponseHandler customerPetImgHandler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("imgList") && !jdata.isNull("imgList")) {
                            JSONArray jarrdataset = jdata.getJSONArray("imgList");
                            if (jarrdataset.length() > 0) {
                                for (int i = 0; i < jarrdataset.length(); i++) {
                                    localImgList.add(jarrdataset
                                            .getString(i));
                                }
                            }
                        }
                    }
                    if (localImgList.size() > 0) {
                        if (page == 1) {
                            imgList.clear();
                        }
                        imgList.addAll(localImgList);
                        page++;
                    } else {
                        if (page == 1) {
                            petArchivesImgAdapter.setEmptyView(setEmptyViewBase(2, "暂无照片哦~",
                                    R.drawable.icon_no_mypet, null));
                        }
                    }
                    petArchivesImgAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1 && imgList.size() <= 0) {
                        petArchivesImgAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_no_mypet,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (page == 1 && imgList.size() <= 0) {
                    petArchivesImgAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_no_mypet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                }
            }
            mrlPetArchivesImgs.finishRefresh();
            mrlPetArchivesImgs.finishRefreshLoadMore();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            mrlPetArchivesImgs.finishRefresh();
            mrlPetArchivesImgs.finishRefreshLoadMore();
            if (page == 1 && imgList.size() <= 0) {
                petArchivesImgAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_no_mypet, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
            }
        }
    };

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
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
