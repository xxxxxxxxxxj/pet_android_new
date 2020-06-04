package com.haotang.pet;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.fragment.BeauticianCommentPicDetailFragment;

public class BeauticianCommonPicActivity extends BaseFragmentActivity {
    private ViewPager vpProduction;
    private MPagerAdapter adapter;
    private int index;
    private String[] pics;
    private String[] txts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beauticiancommentpic_detail);
        index = getIntent().getIntExtra("index", 0);
        pics = getIntent().getStringArrayExtra("pics");
        txts = getIntent().getStringArrayExtra("txts");
        vpProduction = (ViewPager) findViewById(R.id.vp_beauticianproduction_pager);
        adapter = new MPagerAdapter(getSupportFragmentManager());
        vpProduction.setAdapter(adapter);
        vpProduction.invalidate();
        adapter.notifyDataSetChanged();
        vpProduction.setCurrentItem(index);
    }

    private class MPagerAdapter extends FragmentStatePagerAdapter implements
            ViewPager.OnPageChangeListener {

        public MPagerAdapter(FragmentManager fm) {
            super(fm);
            vpProduction.setOnPageChangeListener(this);
        }

        @Override
        public Fragment getItem(int position) {
            BeauticianCommentPicDetailFragment fragment = (BeauticianCommentPicDetailFragment) Fragment
                    .instantiate(BeauticianCommonPicActivity.this,
                            BeauticianCommentPicDetailFragment.class.getName());
            if (txts != null) {
                fragment.setData(pics[position], txts[position]);
            } else {
                fragment.setData(pics[position]);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return pics.length;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }
    }
}
