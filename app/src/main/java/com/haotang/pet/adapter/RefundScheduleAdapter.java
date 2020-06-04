package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.RefundSchedule;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/23 15:38
 */
public class RefundScheduleAdapter extends BaseQuickAdapter<RefundSchedule, BaseViewHolder> {
    public RefundScheduleAdapter(int layoutResId, List<RefundSchedule> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RefundSchedule item) {
        View vw_item_srefundschedule_top = helper.getView(R.id.vw_item_srefundschedule_top);
        View vw_item_srefundschedule_bottom = helper.getView(R.id.vw_item_srefundschedule_bottom);
        ImageView iv_item_srefundschedule_select = helper.getView(R.id.iv_item_srefundschedule_select);
        TextView tv_item_srefundschedule_name = helper.getView(R.id.tv_item_srefundschedule_name);
        TextView tv_item_srefundschedule_time = helper.getView(R.id.tv_item_srefundschedule_time);
        TextView tv_item_srefundschedule_desc = helper.getView(R.id.tv_item_srefundschedule_desc);
        if (item != null) {
            iv_item_srefundschedule_select.bringToFront();
            if (helper.getLayoutPosition() == 0) {
                vw_item_srefundschedule_top.setVisibility(View.INVISIBLE);
                vw_item_srefundschedule_bottom.setVisibility(View.VISIBLE);
            } else if (helper.getLayoutPosition() == mData.size() - 1) {
                vw_item_srefundschedule_top.setVisibility(View.VISIBLE);
                vw_item_srefundschedule_bottom.setVisibility(View.INVISIBLE);
            } else {
                vw_item_srefundschedule_top.setVisibility(View.VISIBLE);
                vw_item_srefundschedule_bottom.setVisibility(View.VISIBLE);
            }
            if (item.getRed() == 1) {
                tv_item_srefundschedule_name.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                tv_item_srefundschedule_time.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                tv_item_srefundschedule_desc.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                iv_item_srefundschedule_select.setImageResource(R.drawable.icon_petadd_select);
            } else {
                iv_item_srefundschedule_select.setImageResource(R.drawable.icon_petadd_unselect);
                tv_item_srefundschedule_name.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_srefundschedule_time.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_srefundschedule_desc.setTextColor(mContext.getResources().getColor(R.color.a999999));
            }
            Utils.setText(tv_item_srefundschedule_name, item.getName(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_srefundschedule_time, item.getTime(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_srefundschedule_desc, item.getDesc(), "", View.VISIBLE, View.GONE);
        }
    }
}
