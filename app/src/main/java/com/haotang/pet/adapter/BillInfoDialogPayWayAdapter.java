package com.haotang.pet.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.PayWay;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/27 11:57
 */
public class BillInfoDialogPayWayAdapter extends BaseQuickAdapter<PayWay, BaseViewHolder> {

    public BillInfoDialogPayWayAdapter(int layoutResId, List<PayWay> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, PayWay item) {
        TextView tv_item_billinfodialog_payway_price = helper.getView(R.id.tv_item_billinfodialog_payway_price);
        TextView tv_item_billinfodialog_payway_name = helper.getView(R.id.tv_item_billinfodialog_payway_name);
        if (item != null) {
            Utils.setText(tv_item_billinfodialog_payway_price, item.getAmount(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_billinfodialog_payway_name, item.getName(), "", View.VISIBLE, View.VISIBLE);
        }
    }
}
