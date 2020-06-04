package com.haotang.pet.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.PayPricesItem;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/5/28 13:55
 */
public class WashOrderDetailPayPricesItemAdapter extends BaseQuickAdapter<PayPricesItem, BaseViewHolder> {

    public WashOrderDetailPayPricesItemAdapter(int layoutResId, List<PayPricesItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PayPricesItem item) {
        TextView tv_item_washorderdetail_payprice_price = helper.getView(R.id.tv_item_washorderdetail_payprice_price);
        TextView tv_item_washorderdetail_payprice_name = helper.getView(R.id.tv_item_washorderdetail_payprice_name);
        if (item != null) {
            Utils.setText(tv_item_washorderdetail_payprice_name, item.getPayWayDesc(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_washorderdetail_payprice_price, "¥" + item.getAmount(), "", View.VISIBLE, View.VISIBLE);
        }
    }
}