package com.haotang.pet.adapter;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.PetServiceMonth;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/17 17:20
 */
public class PetDiaryMonthAdapter extends BaseQuickAdapter<PetServiceMonth, BaseViewHolder> {
    public PetDiaryMonthAdapter(int layoutResId, List<PetServiceMonth> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PetServiceMonth item) {
        LinearLayout ll_item_petdiary_month = helper.getView(R.id.ll_item_petdiary_month);
        TextView tv_item_petdiary_month_year = helper.getView(R.id.tv_item_petdiary_month_year);
        TextView tv_item_petdiary_month_month = helper.getView(R.id.tv_item_petdiary_month_month);
        Log.e("TAG","item = "+item.toString());
        Utils.setText(tv_item_petdiary_month_year,item.getYear(),"",View.VISIBLE,View.VISIBLE);
        Utils.setText(tv_item_petdiary_month_month,item.getMonth(),"",View.VISIBLE,View.VISIBLE);
        if (item.isSelect()) {
            tv_item_petdiary_month_month.setTextColor(mContext.getResources().getColor(R.color.black));
            tv_item_petdiary_month_year.setVisibility(View.VISIBLE);
            ll_item_petdiary_month.setBackgroundResource(R.drawable.bg_round_white);
        } else {
            tv_item_petdiary_month_month.setTextColor(mContext.getResources().getColor(R.color.a666666));
            tv_item_petdiary_month_year.setVisibility(View.GONE);
            ll_item_petdiary_month.setBackgroundResource(R.color.transparent);
        }
    }
}
