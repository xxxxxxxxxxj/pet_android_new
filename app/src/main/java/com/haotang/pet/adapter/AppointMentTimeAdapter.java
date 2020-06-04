package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.AppointMentTime;
import com.haotang.pet.entity.AppointmentTimeEvent;
import com.haotang.pet.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 10:41
 */
public class AppointMentTimeAdapter extends BaseQuickAdapter<AppointMentTime, BaseViewHolder> {
    private boolean changeOrder;
    public AppointMentTimeAdapter(int layoutResId, List<AppointMentTime> data,boolean changeOrder) {
        super(layoutResId, data);
        this.changeOrder = changeOrder;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AppointMentTime item) {
        LinearLayout ll_item_appoint_time_time = helper.getView(R.id.ll_item_appoint_time_time);
        TextView tv_item_appoint_time_time_time = helper.getView(R.id.tv_item_appoint_time_time_time);
        ImageView iv_item_appoint_time_time_js = helper.getView(R.id.iv_item_appoint_time_time_js);
        if (item != null) {
            String time = item.getTime();
            int pickup = item.getPickup();
            boolean select = item.isSelect();
            final List<Integer> workers = item.getWorkers();
            Utils.setText(tv_item_appoint_time_time_time, time, "", View.VISIBLE, View.VISIBLE);
            if (workers != null && workers.size() > 0) {// 此时间格子可约
                if (pickup == 1) {//可接送
                    iv_item_appoint_time_time_js.setVisibility(View.VISIBLE);
                    if (select) {//选中
                        iv_item_appoint_time_time_js.setImageResource(R.drawable.icon_time_car_select);
                        ll_item_appoint_time_time.setBackgroundResource(R.drawable.bg_red_jianbian_round5);
                        tv_item_appoint_time_time_time.setTextColor(mContext.getResources().getColor(R.color.white));
                    } else {//未选中
                        iv_item_appoint_time_time_js.setImageResource(R.drawable.icon_time_car_unselect);
                        ll_item_appoint_time_time.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                        tv_item_appoint_time_time_time.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                    }
                } else {//不可接送
                    iv_item_appoint_time_time_js.setVisibility(View.GONE);
                    if (select) {//选中
                        ll_item_appoint_time_time.setBackgroundResource(R.drawable.bg_red_jianbian_round5);
                        tv_item_appoint_time_time_time.setTextColor(mContext.getResources().getColor(R.color.white));
                    } else {//未选中
                        ll_item_appoint_time_time.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                        tv_item_appoint_time_time_time.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                    }
                }
            } else {// 此时间格子不可约
                tv_item_appoint_time_time_time.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                ll_item_appoint_time_time.setBackgroundResource(R.drawable.bg_trans_bbbb_border);
            }
            ll_item_appoint_time_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (workers != null && workers.size() > 0) {// 此时间格子可约
                        EventBus.getDefault().post(new AppointmentTimeEvent(helper.getLayoutPosition(),changeOrder));
                    }
                }
            });
        }
    }
}