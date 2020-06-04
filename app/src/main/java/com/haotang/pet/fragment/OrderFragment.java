package com.haotang.pet.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.base.BaseFragment;
import com.haotang.pet.R;
import com.haotang.pet.entity.GoodsAddEvent;
import com.haotang.pet.entity.TabEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//import androidx.core.app.FragmentPagerAdapter;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-11 11:02
 */
public class OrderFragment extends BaseFragment {
    @BindView(R.id.mTabLayout_orderfrag)
    CommonTabLayout mTabLayout_orderfrag;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();
    private String[] mTitles = {"服务", "商城", "E卡"};
    private int[] mIconUnselectIds = {
            R.drawable.icon_default, R.drawable.icon_default,
            R.drawable.icon_default};
    private int[] mIconSelectIds = {
            R.drawable.icon_default, R.drawable.icon_default,
            R.drawable.icon_default};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] colors = new int[2];
    private ServiceOrderFragment serviceOrderFragment;  //服务
    private GoodsOrderFragment goodsOrderFragment;      //商城
    private ECardOrderFragment eCardOrderFragment;      //E卡
    private Unbinder mUnBInder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, null);
        mUnBInder = ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

    //商品订单返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GoodsAddEvent event) {
//        if(mTabLayout_orderfrag != null){
//            mTabLayout_orderfrag.setCurrentTab(1);
//            mTabLayout_orderfrag.notifyDataSetChanged();
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
    }

    private void setView() {
        initWindows();
        mFragments2.clear();
        mTabEntities.clear();
        serviceOrderFragment = ServiceOrderFragment.getInstance("Switch Fragment " + mTitles[0]);
        goodsOrderFragment = GoodsOrderFragment.getInstance("Switch Fragment " + mTitles[1]);
        eCardOrderFragment = ECardOrderFragment.getInstance("Switch Fragment " + mTitles[2]);
        mFragments2.add(serviceOrderFragment);
        mFragments2.add(goodsOrderFragment);
        mFragments2.add(eCardOrderFragment);
        mTabLayout_orderfrag.setGradient(true);
        mTabLayout_orderfrag.setmTextSelectsize(mTabLayout_orderfrag.sp2px(18));
        colors[0] = getResources().getColor(R.color.aFA6400);
        colors[1] = getResources().getColor(R.color.afe3b1f);
        mTabLayout_orderfrag.setColors(colors);
        mTabLayout_orderfrag.setIndicatorTextMiddle(true);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout_orderfrag.setTabData(mTabEntities);
        mTabLayout_orderfrag.setCurrentTab(0);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(),mFragments2));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_orderfrag.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        mTabLayout_orderfrag.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position,false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initWindows();
            //获取每个子页面的Fragment并刷新
            serviceOrderFragment.refresh();
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;
        public MyPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void initWindows() {
        Window window = getActivity().getWindow();
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

        //设置状态栏黑白模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        else//6.0以下版本
        {
            UltimateBar.newColorBuilder().statusColor(getContext().getResources().getColor(R.color.bt_6));
        }

//        UltimateBar.newImmersionBuilder()
//                .applyNav(false)         // 是否应用到导航栏
//                .build(getActivity())
//                .apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBInder.unbind();
    }
}