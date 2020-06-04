package com.haotang.pet.adapter.MallAdapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallSearchDetailBean;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/2.
 */

public class MallSearchDetailAdapter<T> extends CommonAdapter<T> {
    private String titleName="";
    public MallSearchDetailAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MallSearchDetailBean mallSearchDetailBean = (MallSearchDetailBean) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_mall_search_address_detail,position);
        viewHolder.setText(R.id.textview_search_name,mallSearchDetailBean.address+mallSearchDetailBean.name);
        viewHolder.getView(R.id.textview_search_address).setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mallSearchDetailBean.address)){
            viewHolder.getView(R.id.textview_search_address).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.textview_search_address,mallSearchDetailBean.address);
        }
        Utils.mLogError("==-->titleName "+titleName);
        /*try {
            if (!TextUtils.isEmpty(titleName)){
                if (mallSearchDetailBean.name.contains(titleName)){
                    String lastName = mallSearchDetailBean.name.replace(titleName,"");
                    viewHolder.setText(R.id.textview_search_name,Utils.getTextAndColorCommentsBeau("#FF3A1E",titleName,"#333333",lastName).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //后边搞字体颜色。别漏掉
        return viewHolder.getConvertView();
    }
    public void setTitleName(String titleName){
        this.titleName = titleName;
    }
}
