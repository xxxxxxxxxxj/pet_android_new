package com.haotang.pet.encyclopedias.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.haotang.base.BaseFragment;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.StatusLibs.StatusBarCompat;
import com.haotang.pet.encyclopedias.adapter.ViewPagerAdapter;
import com.haotang.pet.encyclopedias.bean.EncyclopediasTitle;
import com.haotang.pet.encyclopedias.fragment.EncyclopediasFragment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Utils;
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
 * 百科主页
 */
public class EncyclopediasActivity extends AppCompatActivity {

    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.stl_encyclopedias)
    SlidingTabLayout stlEncyclopedias;
    @BindView(R.id.vp_encyclopedias)
    ViewPager vpEncyclopedias;
    @BindView(R.id.ll_encyclopedias_empty)
    LinearLayout ll_encyclopedias_empty;
    @BindView(R.id.ll_encyclopedias_view)
    LinearLayout ll_encyclopedias_view;
    @BindView(R.id.iv_emptyview_img)
    ImageView iv_emptyview_img;
    @BindView(R.id.tv_emptyview_desc)
    TextView tv_emptyview_desc;
    @BindView(R.id.btn_emptyview)
    Button btn_emptyview;
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private int currentTabIndex;
    private List<EncyclopediasTitle> list = new ArrayList<EncyclopediasTitle>();
    private int[] colors =new int[2];
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setLinster();
        setView();
        getData();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        id = getIntent().getIntExtra("id", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_encyclopedias);
        ButterKnife.bind(this);
    }

    private void setLinster() {
        stlEncyclopedias.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.e("TAG", "position = "+position);
                currentTabIndex = position;
                vpEncyclopedias.setCurrentItem(currentTabIndex);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vpEncyclopedias.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                vpEncyclopedias.setCurrentItem(currentTabIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setView() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.a3a3636));
        stlEncyclopedias.setmTextSelectsize(stlEncyclopedias.sp2px(16));
        stlEncyclopedias.setGradient(true);
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        stlEncyclopedias.setColors(colors);
        stlEncyclopedias.setIndicatorTextMiddle(true);
        tvTitlebarTitle.setText("百科");
        ibTitlebarOther.setVisibility(View.VISIBLE);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        ViewGroup.LayoutParams layoutParams = ibTitlebarOther.getLayoutParams();
        layoutParams.width = DensityUtil.dp2px(this,38);
        layoutParams.height = DensityUtil.dp2px(this,38);
        ibTitlebarOther.setLayoutParams(layoutParams);
        ibTitlebarOther.setBackgroundResource(R.drawable.icon_ency_serch);
    }

    private void getData() {
        list.clear();
        list.add(new EncyclopediasTitle(0, "推荐"));
        CommUtil.queryEncyclopediaClassification(this, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata != null) {
                            if (jdata.has("encyclopediaClassifications") && !jdata.isNull("encyclopediaClassifications")) {
                                JSONArray jarrencyclopediaClassifications = jdata.getJSONArray("encyclopediaClassifications");
                                if (jarrencyclopediaClassifications.length() > 0) {
                                    for (int i = 0; i < jarrencyclopediaClassifications.length(); i++) {
                                        list.add(EncyclopediasTitle.json2Entity(jarrencyclopediaClassifications
                                                .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                    if (list.size() > 0) {
                        ll_encyclopedias_view.setVisibility(View.VISIBLE);
                        ll_encyclopedias_empty.setVisibility(View.GONE);
                        for (int i = 0; i < list.size(); i++) {
                            EncyclopediasFragment encyclopediasFragment = new EncyclopediasFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", list.get(i).getId());
                            encyclopediasFragment.setArguments(bundle);
                            mFragments.add(encyclopediasFragment);
                        }
                        vpEncyclopedias.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, list));
                        stlEncyclopedias.setViewPager(vpEncyclopedias);
                        for (int i = 0; i < list.size(); i++) {
                            EncyclopediasTitle encyclopediasTitle = list.get(i);
                            if (encyclopediasTitle.getId() == id) {
                                currentTabIndex = i;
                                break;
                            }
                        }
                        if (currentTabIndex < 0) {
                            currentTabIndex = 0;
                        } else if (currentTabIndex >= list.size()) {
                            currentTabIndex = list.size() - 1;
                        }
                        stlEncyclopedias.setCurrentTab(currentTabIndex);
                        vpEncyclopedias.setCurrentItem(currentTabIndex);
                    } else {
                        ll_encyclopedias_view.setVisibility(View.GONE);
                        ll_encyclopedias_empty.setVisibility(View.VISIBLE);
                        btn_emptyview.setVisibility(View.GONE);
                        tv_emptyview_desc.setText("暂无百科数据~");
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ll_encyclopedias_view.setVisibility(View.GONE);
                        ll_encyclopedias_empty.setVisibility(View.VISIBLE);
                        btn_emptyview.setVisibility(View.VISIBLE);
                        tv_emptyview_desc.setText(msg);
                    }
                }
            } catch (JSONException e) {
                ll_encyclopedias_view.setVisibility(View.GONE);
                ll_encyclopedias_empty.setVisibility(View.VISIBLE);
                btn_emptyview.setVisibility(View.VISIBLE);
                tv_emptyview_desc.setText("数据异常");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ll_encyclopedias_view.setVisibility(View.GONE);
            ll_encyclopedias_empty.setVisibility(View.VISIBLE);
            btn_emptyview.setVisibility(View.VISIBLE);
            tv_emptyview_desc.setText("请求失败");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.btn_emptyview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_other:
                startActivity(new Intent(EncyclopediasActivity.this, EncyclopediasSearchActivity.class));
                break;
            case R.id.btn_emptyview:
                getData();
                break;
        }
    }
}
