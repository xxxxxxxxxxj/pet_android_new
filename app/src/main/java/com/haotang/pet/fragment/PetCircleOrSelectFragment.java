package com.haotang.pet.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.haotang.base.BaseFragment;
import com.haotang.pet.R;
import com.haotang.pet.StatusLibs.StatusBarCompat;
import com.haotang.pet.entity.CircleOrSelectEvent;
import com.haotang.pet.entity.FloatIngEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//import androidx.core.app.FragmentPagerAdapter;

@SuppressLint("NewApi")
public class PetCircleOrSelectFragment extends BaseFragment implements
        OnClickListener, OnTouchListener {
    public static PetCircleOrSelectFragment circleOrSelectFragment;
    private RelativeLayout rl_communitytab_selected;
    private RelativeLayout rl_communitytab_circle;
    private View vw_bottom_selected;
    private View vw_bottom_circle;
    private TextView tv_communitytab_selected;
    private TextView tv_communitytab_circle;
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    /**
     * 用于展示精选界面的Fragment
     */
    private PostSelectionFragment postSelectionFragment;

    /**
     * 用于展示宠圈界面的Fragment
     */
    private PetCircleFragment petCircleFragment;
    private Activity mActivity;
    private SharedPreferenceUtil spUtil;
    private ViewPager viewPager_circle;
    private List<Fragment> allFragment = new ArrayList<Fragment>();
    private View view;
    public static MyAdapter myAdapter;

    public PetCircleOrSelectFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        this.mActivity = activity;
        super.onAttach(activity);
    }

    private void setStatusBar() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.a3a3636));
//        StatusBarCompat.setStatusBarColor(getActivity(), MainActivity.DEFAULT_COLOR);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onCreate");
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.e("TAG", "msg.what = " + msg.what);
            if (msg.what == 1) {
                viewPager_circle.setCurrentItem(1);
            } else {
                viewPager_circle.setCurrentItem(0);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        spUtil = SharedPreferenceUtil.getInstance(mActivity);
        circleOrSelectFragment = this;
        if (view == null)
            view = inflater.inflate(R.layout.activity_community_tab, null);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        view.setOnTouchListener(this);
        Log.e("TAG", "petcircle");
        rl_communitytab_selected = (RelativeLayout) view
                .findViewById(R.id.rl_communitytab_selected);
        rl_communitytab_circle = (RelativeLayout) view
                .findViewById(R.id.rl_communitytab_circle);
        vw_bottom_selected = (View) view.findViewById(R.id.vw_bottom_selected);
        vw_bottom_circle = (View) view.findViewById(R.id.vw_bottom_circle);
        viewPager_circle = (ViewPager) view.findViewById(R.id.viewPager_circle);
        tv_communitytab_selected = (TextView) view
                .findViewById(R.id.tv_communitytab_selected);
        tv_communitytab_circle = (TextView) view
                .findViewById(R.id.tv_communitytab_circle);
        fragmentManager = getFragmentManager();
        rl_communitytab_selected.setOnClickListener(this);
        rl_communitytab_circle.setOnClickListener(this);
        allFragment.clear();
        postSelectionFragment = new PostSelectionFragment(mActivity);
        petCircleFragment = new PetCircleFragment(mActivity);
        allFragment.add(postSelectionFragment);
        allFragment.add(petCircleFragment);
        myAdapter = new MyAdapter(getChildFragmentManager(), allFragment);
        viewPager_circle.setAdapter(myAdapter);
        try {
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        viewPager_circle.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_communitytab_selected.setTextColor(getResources()
                                .getColor(R.color.white));
                        tv_communitytab_circle.setTextColor(getResources()
                                .getColor(R.color.bbbbbb));
                        vw_bottom_selected.setVisibility(View.VISIBLE);
                        vw_bottom_circle.setVisibility(View.GONE);
                        break;
                    case 1:
                        tv_communitytab_selected.setTextColor(getResources()
                                .getColor(R.color.bbbbbb));
                        tv_communitytab_circle.setTextColor(getResources()
                                .getColor(R.color.white));
                        vw_bottom_selected.setVisibility(View.GONE);
                        vw_bottom_circle.setVisibility(View.VISIBLE);
                        getStatistics(Global.ServerEventID.choose_petcircle_page, Global.ServerEventID.click_petcircle_tab);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }
        });
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CircleOrSelectEvent event) {
        Log.e("TAG", "event = " + event.toString());
        aliHandler.sendEmptyMessageDelayed(event.getSelection(), 500);
    }

    public void setIndexCircle(int index) {
        Log.e("TAG", "index = " + index);
        aliHandler.sendEmptyMessageDelayed(index, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatIngEvent(3));
        setStatusBar();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_communitytab_selected:
                viewPager_circle.setCurrentItem(0);
                break;
            case R.id.rl_communitytab_circle:
                viewPager_circle.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    class MyAdapter extends FragmentPagerAdapter {
        private List<Fragment> allFragment;

        public MyAdapter(FragmentManager fm, List<Fragment> allFragment) {
            super(fm);
            this.allFragment = allFragment;
        }

        @Override
        public Fragment getItem(int arg0) {
            return allFragment.get(arg0);
        }

        @Override
        public int getCount() {
            return allFragment.size();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
    }

    private void getStatistics(String typeid, String activeid) {
        CommUtil.logcountAdd(mActivity, typeid, activeid, statisticsHandler);
    }

    private AsyncHttpResponseHandler statisticsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

}
