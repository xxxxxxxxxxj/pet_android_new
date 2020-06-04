package com.haotang.pet.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.AppointMentDate;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/24 16:18
 */
public class TopDateAdapter extends BaseQuickAdapter<AppointMentDate, BaseViewHolder> {
    public TopDateAdapter(int layoutResId, List<AppointMentDate> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AppointMentDate item) {
        LinearLayout ll_item_appointdate_root = helper.getView(R.id.ll_item_appointdate_root);
        TextView tv_item_appointdate_day = helper.getView(R.id.tv_item_appointdate_day);
        TextView tv_item_appointdate_date = helper.getView(R.id.tv_item_appointdate_date);
        if (item != null) {
            Utils.setText(tv_item_appointdate_day,
                    item.getDesc(), "", View.VISIBLE, View.INVISIBLE);
            Utils.setText(tv_item_appointdate_date,
                    item.getDate(), "", View.VISIBLE, View.INVISIBLE);
            if (item.isSelect()) {// 选中
                ll_item_appointdate_root.setBackground(mContext.getResources()
                        .getDrawable(R.drawable.date_appoint_select));
                tv_item_appointdate_day.setTextColor(mContext
                        .getResources().getColor(R.color.white));
                tv_item_appointdate_date.setTextColor(mContext
                        .getResources().getColor(R.color.white));
            } else {// 未选中
                ll_item_appointdate_root.setBackgroundColor(mContext
                        .getResources().getColor(android.R.color.transparent));
                if (item.getIsFull() == 0) {// 可约
                    tv_item_appointdate_day.setTextColor(mContext
                            .getResources().getColor(R.color.aD0021B));
                    tv_item_appointdate_date.setTextColor(mContext
                            .getResources().getColor(R.color.aD0021B));
                } else if (item.getIsFull() == 1) {// 不可约
                    tv_item_appointdate_day.setTextColor(mContext
                            .getResources().getColor(R.color.a999999));
                    tv_item_appointdate_date.setTextColor(mContext
                            .getResources().getColor(R.color.a999999));
                    tv_item_appointdate_date.setText("已约满");
                }
            }
        }
    }
}