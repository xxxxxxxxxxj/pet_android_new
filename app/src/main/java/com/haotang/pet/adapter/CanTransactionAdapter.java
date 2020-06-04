package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CanTransaction;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/2 10:00
 */
public class CanTransactionAdapter<T> extends CommonAdapter<T> {

    public CanTransactionAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CanTransaction canTransaction = (CanTransaction) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_cantransaction, position);
        ImageView iv_item_cantransaction = viewHolder
                .getView(R.id.iv_item_cantransaction);
        TextView tv_item_cantransaction_num = viewHolder
                .getView(R.id.tv_item_cantransaction_num);
        TextView tv_item_cantransaction_desc = viewHolder
                .getView(R.id.tv_item_cantransaction_desc);
        TextView tv_item_cantransaction_time = viewHolder
                .getView(R.id.tv_item_cantransaction_time);
        if (canTransaction != null) {
            GlideUtil.loadCircleImg(mContext, canTransaction.icon, iv_item_cantransaction,
                    R.drawable.user_icon_unnet_circle);
            if (canTransaction.payNum > 0) {
                Utils.setText(tv_item_cantransaction_num, "+" + canTransaction.payNum, "", View.VISIBLE, View.VISIBLE);
            } else {
                Utils.setText(tv_item_cantransaction_num, "" + canTransaction.payNum, "", View.VISIBLE, View.VISIBLE);
            }
            Utils.setText(tv_item_cantransaction_desc, canTransaction.content, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_cantransaction_time, canTransaction.payTime, "", View.VISIBLE, View.VISIBLE);
        }
        return viewHolder.getConvertView();
    }
}
