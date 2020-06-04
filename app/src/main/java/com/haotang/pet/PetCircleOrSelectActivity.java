package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.fragment.PetCircleFragment;
import com.haotang.pet.fragment.PostSelectionFragment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

//import androidx.core.app.FragmentPagerAdapter;

/**
 * 宠圈主界面
 */
public class PetCircleOrSelectActivity extends BaseFragmentActivity implements
        View.OnClickListener {
    private RelativeLayout rl_communitytab_selected;
    private RelativeLayout rl_communitytab_circle;
    private View vw_bottom_selected;
    private View vw_bottom_circle;
    private TextView tv_communitytab_selected;
    private TextView tv_communitytab_circle;
    /**
     * 用于展示精选界面的Fragment
     */
    private PostSelectionFragment postSelectionFragment;

    /**
     * 用于展示宠圈界面的Fragment
     */
    private PetCircleFragment petCircleFragment;
    private ViewPager viewPager_circle;
    private List<Fragment> allFragment = new ArrayList<Fragment>();
    public static MyAdapter myAdapter;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_circle_or_select);
        rl_communitytab_selected = (RelativeLayout) findViewById(R.id.rl_communitytab_selected);
        rl_communitytab_circle = (RelativeLayout) findViewById(R.id.rl_communitytab_circle);
        vw_bottom_selected = (View) findViewById(R.id.vw_bottom_selected);
        vw_bottom_circle = (View) findViewById(R.id.vw_bottom_circle);
        viewPager_circle = (ViewPager) findViewById(R.id.viewPager_circle);
        tv_communitytab_selected = (TextView) findViewById(R.id.tv_communitytab_selected);
        tv_communitytab_circle = (TextView) findViewById(R.id.tv_communitytab_circle);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        rl_communitytab_selected.setOnClickListener(this);
        rl_communitytab_circle.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        allFragment.clear();
        postSelectionFragment = new PostSelectionFragment(mContext);
        petCircleFragment = new PetCircleFragment(mContext);
        allFragment.add(postSelectionFragment);
        allFragment.add(petCircleFragment);
        myAdapter = new MyAdapter(getSupportFragmentManager(), allFragment);
        viewPager_circle.setAdapter(myAdapter);
        try {
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        viewPager_circle.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            case R.id.ib_titlebar_back:
                finish();
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

    private void getStatistics(String typeid, String activeid) {
        CommUtil.logcountAdd(mContext, typeid, activeid, statisticsHandler);
    }

    private AsyncHttpResponseHandler statisticsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

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
