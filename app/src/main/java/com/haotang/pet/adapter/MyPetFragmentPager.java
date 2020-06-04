package com.haotang.pet.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.haotang.pet.entity.Pet;

import java.util.ArrayList;
import java.util.List;

//import androidx.core.app.FragmentTransaction;

//import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Administrator on 2018/5/3 0003. androidx.fragment.app.FragmentStatePagerAdapter;
 */

public class MyPetFragmentPager extends FragmentStatePagerAdapter {
    private long baseId = 0;
    public ArrayList<Pet> petlist = null;
    public int [] WH;
    private Context context;
    private int mChildCount = 0;
    private List<Fragment> fragments ;
    private FragmentManager fm;
    public MyPetFragmentPager(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments  = fragments ;
    }
    public void setFragments(List<Fragment> fragments) {
        if(this.fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    /**
     * 更新fragment的数量之后，在调用notifyDataSetChanged之前，changeId(1) 改变id，改变tag
     * @param n
     */
    public void changeId(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
    @Override
    public Fragment getItem(int position) {
//        MyPetEverFragment fragment = (MyPetEverFragment)Fragment.instantiate(context, MyPetEverFragment.class.getName());
//        fragment.setData(petlist.get(position));
//        for (int i =0;i<petlist.size();i++){
//            Utils.mLogError("==-->11111111111   "+petlist.get(i).customerpetid);
//        }
//        return fragment;//这个到时候要改为返回的fragment
        return fragments.get(position);//这个到时候要改为返回的fragment
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public float getPageWidth(int position) {
        if (fragments.size()>1){
            return 0.92f;
        }else {
            return 1f;
        }
    }
    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
//        if ( mChildCount > 0) {
//            // 这里利用判断执行若干次不缓存，刷新
//            mChildCount --;
//            // 返回这个是强制ViewPager不缓存，每次滑动都刷新视图
//            return POSITION_NONE;
//        }
        return PagerAdapter.POSITION_NONE;
        // 这个则是缓存不刷新视图
//        return super.getItemPosition(object);
    }

//    @Override
//    public void notifyDataSetChanged() {
//        // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
//        mChildCount = getCount();
//        super.notifyDataSetChanged();
//    }
}
