package com.haotang.pet.encyclopedias.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.haotang.base.BaseFragment;
import com.haotang.pet.encyclopedias.bean.EncyclopediasTitle;

import java.util.ArrayList;
import java.util.List;

//import androidx.core.app.FragmentPagerAdapter;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/2 11:32
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<EncyclopediasTitle> list;
    private ArrayList<BaseFragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> mFragments, List<EncyclopediasTitle> list) {
        super(fm);
        this.mFragments = mFragments;
        this.list = list;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}