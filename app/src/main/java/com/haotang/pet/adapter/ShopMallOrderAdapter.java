package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.ShopMallOrderDetailActivity;
import com.haotang.pet.entity.ShopMallOrder;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.TagTextView;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/30 20:31
 */
public class ShopMallOrderAdapter<T> extends CommonAdapter<T> {
    public ShopMallOrderAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ShopMallOrder shopMallOrder = (ShopMallOrder) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_shopmallorder_adapter, position);
        ImageView iv_item_shopmallorder = viewHolder
                .getView(R.id.iv_item_shopmallorder);
        TextView tv_item_shopmallorder_statename = viewHolder
                .getView(R.id.tv_item_shopmallorder_statename);
        TextView tv_item_shopmallorder_num = viewHolder
                .getView(R.id.tv_item_shopmallorder_num);
        TextView tv_item_shopmallorder_price = viewHolder
                .getView(R.id.tv_item_shopmallorder_price);
        TagTextView tv_item_shopmallorder_title = viewHolder
                .getView(R.id.tv_item_shopmallorder_title);
        Button btn_item_shopmallorder = viewHolder
                .getView(R.id.btn_item_shopmallorder);
        RelativeLayout rl_item_shopmallorder = viewHolder
                .getView(R.id.rl_item_shopmallorder);
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
                tv_item_shopmallorder_title.setSingleTagAndContent(shopMallOrder.getMarketingTag(), shopMallOrder.getTitle()+ "(" + shopMallOrder.getStatDesc() + ")");
            } else {
                tv_item_shopmallorder_title.setText(shopMallOrder.getTitle());
            }
            GlideUtil.loadImg(mContext, shopMallOrder.getThumbnail(), iv_item_shopmallorder,
                    R.drawable.icon_production_default);
            btn_item_shopmallorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onButtonClickListener != null) {
                        onButtonClickListener.OnButtonClick(position);
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
        return viewHolder.getConvertView();
    }

    public OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        public void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnButtonClickListener onButtonClickListener = null;

    public interface OnButtonClickListener {
        public void OnButtonClick(int position);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
