package com.haotang.pet.adapter;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterDay;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-24 14:06
 */
public class FosterDayAdapter extends BaseQuickAdapter<FosterDay, BaseViewHolder> {

    public FosterDayAdapter(int layoutResId, List<FosterDay> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterDay item) {
        final LinearLayout ll_item_fosterdy_root = helper.getView(R.id.ll_item_fosterdy_root);
        TextView tv_item_fosterdy_day = helper.getView(R.id.tv_item_fosterdy_day);
        TextView tv_item_fosterdy_desc = helper.getView(R.id.tv_item_fosterdy_desc);
        View vw_item_fosterdy_left = helper.getView(R.id.vw_item_fosterdy_left);
        View vw_item_fosterdy_right = helper.getView(R.id.vw_item_fosterdy_right);
        if (item != null) {
            if (Utils.isStrNull(item.getDate())) {
                Utils.setText(tv_item_fosterdy_day, item.getDay(), "", View.VISIBLE, View.VISIBLE);
                ll_item_fosterdy_root.setVisibility(View.VISIBLE);
                if (item.getIsFull() == 1) {//满房
                    vw_item_fosterdy_left.setVisibility(View.GONE);
                    vw_item_fosterdy_right.setVisibility(View.GONE);
                    tv_item_fosterdy_desc.setVisibility(View.VISIBLE);
                    tv_item_fosterdy_desc.setText("满房");
                    tv_item_fosterdy_desc.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                    tv_item_fosterdy_day.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                    tv_item_fosterdy_day.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                } else if (item.getIsFull() == 0) {//可约
                    vw_item_fosterdy_left.setVisibility(View.GONE);
                    vw_item_fosterdy_right.setVisibility(View.GONE);
                    tv_item_fosterdy_day.setTextColor(mContext.getResources().getColor(R.color.a333333));
                    tv_item_fosterdy_desc.setTextColor(mContext.getResources().getColor(R.color.a333333));
                    Utils.setText(tv_item_fosterdy_desc, item.getBadge(), "", View.VISIBLE, View.INVISIBLE);
                    tv_item_fosterdy_day.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                }
                if (item.isStart()) {//入住时间
                    vw_item_fosterdy_left.setVisibility(View.VISIBLE);
                    vw_item_fosterdy_right.setVisibility(View.VISIBLE);
                    vw_item_fosterdy_left.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    vw_item_fosterdy_right.setBackgroundColor(mContext.getResources().getColor(R.color.afff1f2));
                    if(item.isSelectEnd()){
                        Log.e("TAG","有");
                        vw_item_fosterdy_right.setBackgroundColor(mContext.getResources().getColor(R.color.afff1f2));
                    }else{
                        Log.e("TAG","无");
                        vw_item_fosterdy_right.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    }
                    tv_item_fosterdy_desc.setVisibility(View.VISIBLE);
                    tv_item_fosterdy_day.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_item_fosterdy_day.setBackgroundResource(R.drawable.bg_fosterday_redcircle);
                    tv_item_fosterdy_desc.setTextColor(mContext.getResources().getColor(R.color.aff3a1e));
                    tv_item_fosterdy_desc.setText("入住");
                } else if (item.isEnd()) {//离店时间
                    vw_item_fosterdy_left.setVisibility(View.VISIBLE);
                    vw_item_fosterdy_right.setVisibility(View.VISIBLE);
                    vw_item_fosterdy_left.setBackgroundColor(mContext.getResources().getColor(R.color.afff1f2));
                    vw_item_fosterdy_right.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    tv_item_fosterdy_desc.setVisibility(View.VISIBLE);
                    tv_item_fosterdy_day.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_item_fosterdy_day.setBackgroundResource(R.drawable.bg_fosterday_redcircle);
                    tv_item_fosterdy_desc.setTextColor(mContext.getResources().getColor(R.color.aff3a1e));
                    tv_item_fosterdy_desc.setText("离店");
                } else if (item.isMiddle()) {//中间时间
                    vw_item_fosterdy_left.setVisibility(View.VISIBLE);
                    vw_item_fosterdy_right.setVisibility(View.VISIBLE);
                    vw_item_fosterdy_left.setBackgroundColor(mContext.getResources().getColor(R.color.afff1f2));
                    vw_item_fosterdy_right.setBackgroundColor(mContext.getResources().getColor(R.color.afff1f2));
                    tv_item_fosterdy_day.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                    tv_item_fosterdy_day.setTextColor(mContext.getResources().getColor(R.color.a333333));
                    tv_item_fosterdy_desc.setVisibility(View.INVISIBLE);
                }
            } else {//空着
                ll_item_fosterdy_root.setVisibility(View.INVISIBLE);
            }
        }
    }
}