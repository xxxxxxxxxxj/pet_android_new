package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.PushMessageEntity;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MyViewAndCircle;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class NoticeAdapter<T> extends CommonAdapter<T> {

    public NoticeAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    public NoticeAdapter(Activity mContext, List<T> mDatas, int flag) {
        super(mContext, mDatas, flag);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PushMessageEntity pushMessageEntity = (PushMessageEntity) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent,R.layout.item_notice_layout,position);
        MyViewAndCircle textView_title = viewHolder.getView(R.id.textView_title);
        textView_title.setTitleText(pushMessageEntity.msg+"");
        if (pushMessageEntity.isRead==0){
            textView_title.setIsVisiable(true);
        }else {
            textView_title.setIsVisiable(false);
        }
        viewHolder.setText(R.id.textview_time,pushMessageEntity.createTime+"");
        return viewHolder.getConvertView();
    }
}
