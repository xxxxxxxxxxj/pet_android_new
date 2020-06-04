package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.RegionChildren;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MallSearchAddressListAdapter<T> extends CommonAdapter<T> {
    private int clickPositon=-1;
    private List<RegionChildren> topTagList=null;
    public MallSearchAddressListAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegionChildren regionChildren = (RegionChildren) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_mall_search_address_list,position);
        viewHolder.setText(R.id.textview_show_addressname,regionChildren.region);
        viewHolder.getView(R.id.img_right_choose_state).setVisibility(View.GONE);
        if (clickPositon!=-1){
            if (clickPositon==position){
                viewHolder.getView(R.id.img_right_choose_state).setVisibility(View.VISIBLE);
                viewHolder.setBackgroundResource(R.id.img_right_choose_state,R.drawable.mall_area_choose);
            }
        }
        if (topTagList!=null){
            for (int i = 0;i<topTagList.size();i++){
                if (topTagList.get(i).areaId == regionChildren.areaId){
                    viewHolder.getView(R.id.img_right_choose_state).setVisibility(View.VISIBLE);
                    viewHolder.setBackgroundResource(R.id.img_right_choose_state,R.drawable.mall_area_choose);
                }
            }
        }
        return viewHolder.getConvertView();
    }
    public void setClickPositon(int clickPositon){
        this.clickPositon = clickPositon;
    }
    public void setTopTagList(List<RegionChildren> topTagList){
        this.topTagList=topTagList;
    }
}
