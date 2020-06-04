package com.haotang.pet.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.OrderPayScanInfo;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>
 * Title:OrderPayScanInfoAdapter
 * </p>
 * <p>
 * Description:扫码支付商品信息数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-11-24 下午4:05:46
 */
public class OrderPayScanInfoAdapter extends BaseQuickAdapter<OrderPayScanInfo, BaseViewHolder> {
    public OrderPayScanInfoAdapter(int layoutResId, List<OrderPayScanInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderPayScanInfo item) {
        TextView tv_item_scanshopinfo_amount = helper.getView(R.id.tv_item_scanshopinfo_amount);
        TextView tv_item_scanshopinfo_price = helper.getView(R.id.tv_item_scanshopinfo_price);
        TextView tv_item_scanshopinfo_name = helper.getView(R.id.tv_item_scanshopinfo_name);
        if (item != null) {
            Utils.setText(tv_item_scanshopinfo_amount, "x" + item.amount, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_scanshopinfo_price, "¥" + item.price, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_scanshopinfo_name, item.item, "", View.VISIBLE, View.VISIBLE);
        }
    }
}
