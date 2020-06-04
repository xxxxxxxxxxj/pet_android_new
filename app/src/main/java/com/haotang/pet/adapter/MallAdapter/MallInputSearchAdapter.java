package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public class MallInputSearchAdapter<T> extends CommonAdapter<T> {

    public MallInputSearchAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_mall_input_search,position);
        viewHolder.setText(R.id.textview_search_last,mDatas.get(position).toString());
        Utils.mLogError("==-->input搜索"+mDatas.get(position).toString());
        return viewHolder.getConvertView();
    }
}
