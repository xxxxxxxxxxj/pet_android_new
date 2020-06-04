package com.haotang.pet.adapter;

import android.content.Intent;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.ShopMallOrderDetailActivity;
import com.haotang.pet.entity.ShopMallOrder;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TagTextView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-11 13:20
 */
public class GoodsOrderAdapter extends BaseQuickAdapter<ShopMallOrder, BaseViewHolder> {

    public GoodsOrderAdapter(int layoutResId, @Nullable List<ShopMallOrder> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ShopMallOrder shopMallOrder) {
        ImageView iv_item_shopmallorder = helper.getView(R.id.iv_item_shopmallorder);
        TextView tv_item_shopmallorder_statename = helper.getView(R.id.tv_item_shopmallorder_statename);
        TextView tv_item_shopmallorder_num = helper.getView(R.id.tv_item_shopmallorder_num);
        TextView tv_item_shopmallorder_price = helper.getView(R.id.tv_item_shopmallorder_price);
        TagTextView tv_item_shopmallorder_title = helper.getView(R.id.tv_item_shopmallorder_title);
        Button btn_item_shopmallorder = helper.getView(R.id.btn_item_shopmallorder);
        RelativeLayout rl_item_shopmallorder = helper.getView(R.id.rl_item_shopmallorder);
        if (shopMallOrder != null) {
            if (shopMallOrder.getState() == 0) {
                btn_item_shopmallorder.setText("去付款");
                btn_item_shopmallorder.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                btn_item_shopmallorder.setBackgroundResource(R.drawable.bg_redline_round);
                tv_item_shopmallorder_statename.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
            } else if (shopMallOrder.getState() == 1) {
                btn_item_shopmallorder.setText("查看详情");
                btn_item_shopmallorder.setTextColor(mContext.getResources().getColor(R.color.a666666));
                btn_item_shopmallorder.setBackgroundResource(R.drawable.bg_huiline_round);
                tv_item_shopmallorder_statename.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
            } else if (shopMallOrder.getState() == 2) {
                btn_item_shopmallorder.setText("确认收货");
                btn_item_shopmallorder.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                btn_item_shopmallorder.setBackgroundResource(R.drawable.bg_redline_round);
                tv_item_shopmallorder_statename.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
            } else if (shopMallOrder.getState() == 3) {
                btn_item_shopmallorder.setText("查看详情");
                btn_item_shopmallorder.setTextColor(mContext.getResources().getColor(R.color.a666666));
                btn_item_shopmallorder.setBackgroundResource(R.drawable.bg_huiline_round);
                tv_item_shopmallorder_statename.setTextColor(mContext.getResources().getColor(R.color.a999999));
            } else if (shopMallOrder.getState() == 4 || shopMallOrder.getState() == -1) {
                btn_item_shopmallorder.setText("查看详情");
                btn_item_shopmallorder.setTextColor(mContext.getResources().getColor(R.color.a666666));
                btn_item_shopmallorder.setBackgroundResource(R.drawable.bg_huiline_round);
                tv_item_shopmallorder_statename.setTextColor(mContext.getResources().getColor(R.color.a999999));
            }
            if (shopMallOrder.getAmount() > 1) {
                tv_item_shopmallorder_num.setVisibility(View.GONE);
            } else {
                tv_item_shopmallorder_num.setVisibility(View.VISIBLE);
                tv_item_shopmallorder_num.setText("X" + shopMallOrder.getAmount());
            }
            SpannableString ss = new SpannableString("¥" + shopMallOrder.getPayPrice());
            ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
                    1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_item_shopmallorder_price.setText(ss);
            Utils.setText(tv_item_shopmallorder_statename, shopMallOrder.getStateDesc(), "", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(shopMallOrder.getMarketingTag())) {
                tv_item_shopmallorder_title.setSingleTagAndContent(shopMallOrder.getMarketingTag(), shopMallOrder.getTitle() + "(" + shopMallOrder.getStatDesc() + ")");
            } else {
                tv_item_shopmallorder_title.setText(shopMallOrder.getTitle());
            }
            GlideUtil.loadImg(mContext, shopMallOrder.getThumbnail(), iv_item_shopmallorder,
                    R.drawable.icon_production_default);
            btn_item_shopmallorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onButtonClickListener != null) {
                        onButtonClickListener.OnButtonClick(helper.getLayoutPosition());
                    }
                }
            });
            rl_item_shopmallorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ShopMallOrderDetailActivity.class).putExtra("orderId", shopMallOrder.getOrderId()));
                }
            });
        }
    }

    public ShopMallOrderAdapter.OnButtonClickListener onButtonClickListener = null;

    public interface OnButtonClickListener {
        public void OnButtonClick(int position);
    }

    public void setOnButtonClickListener(ShopMallOrderAdapter.OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
