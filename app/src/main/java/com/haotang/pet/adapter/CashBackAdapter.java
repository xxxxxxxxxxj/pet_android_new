package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.CashBackBean;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/28
 * @Description:返现金额适配器
 */
public class CashBackAdapter<T> extends CommonAdapter<T> {
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public CashBackAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_cashback_layout, position);
        CashBackBean.DataBean.ListBean listBean = (CashBackBean.DataBean.ListBean) mDatas.get(position);
        viewHolder.setText(R.id.tv_cashback_title, listBean.getContent());
        viewHolder.setText(R.id.tv_cashback_num,  listBean.getAmount());
        viewHolder.setText(R.id.tv_cashback_endtime, listBean.getCreateTime());
        if (type==0){
            viewHolder.setTextColor(R.id.tv_cashback_title,"#333333");
            viewHolder.setTextColor(R.id.tv_cashback_num,"#333333");
            viewHolder.setTextColor(R.id.tv_cashback_endtime,"#666666");
            if (listBean.getDetailState() == 0) {//待入账
                viewHolder.setViewVisible(R.id.tv_cashback_tip, View.VISIBLE);
                viewHolder.setTextColor(R.id.tv_cashback_tip, "#DDA548");
            }else  {
                viewHolder.setViewVisible(R.id.tv_cashback_tip, View.VISIBLE);
                viewHolder.setTextColor(R.id.tv_cashback_tip, "#DF3930");
            }
            if (listBean.getDetailState() == 5) {
                viewHolder.setViewVisible(R.id.tv_cashback_tip, View.GONE);
            }
            if (listBean.getDetailStateName()!=null&&!"".equals(listBean.getDetailStateName())){
                viewHolder.setText(R.id.tv_cashback_tip, listBean.getDetailStateName());
            }else {
                viewHolder.setViewVisible(R.id.tv_cashback_tip, View.GONE);
            }
        }else {
            viewHolder.setTextColor(R.id.tv_cashback_title,"#B3B3B3");
            viewHolder.setTextColor(R.id.tv_cashback_num,"#B3B3B3");
            viewHolder.setTextColor(R.id.tv_cashback_endtime,"#B3B3B3");
            viewHolder.setViewVisible(R.id.tv_cashback_tip, View.GONE);
        }

        return viewHolder.getConvertView();
    }
}
