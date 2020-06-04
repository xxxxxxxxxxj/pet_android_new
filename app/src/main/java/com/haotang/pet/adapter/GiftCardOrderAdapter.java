package com.haotang.pet.adapter;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.ChooseFosterPetActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.GiftCardOrder;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/29
 * @Description:E卡订单列表的适配器
 */
public class GiftCardOrderAdapter extends BaseQuickAdapter<GiftCardOrder.DataBean, BaseViewHolder> {

    public GiftCardOrderAdapter(int layoutResId, @Nullable List<GiftCardOrder.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GiftCardOrder.DataBean item) {
        ImageView ivCardOrder = helper.getView(R.id.iv_item_giftcardorder);
        TextView tvCardOrderTitle = helper.getView(R.id.tv_item_cardorder_title);
        TextView tvCardOrderType = helper.getView(R.id.tv_item_cardorder_type);
        TextView tvCardBind = helper.getView(R.id.tv_cardorder_gobind);
        LinearLayout llDiscount = helper.getView(R.id.ll_cardorder_discount);
        LinearLayout llTags = helper.getView(R.id.ll_toothorder_tags);
        TextView tvCardOrderPrice = helper.getView(R.id.tv_cardorder_price);
        TextView tvCardOrderGotip = helper.getView(R.id.tv_cardorder_gotip);
        RelativeLayout llCardOrderAll = helper.getView(R.id.ll_order_all);
        if (item!=null) {
            switch (item.getStatus()) {
                case 2:
                    tvCardOrderType.setText("已完成");
                    tvCardOrderGotip.setText("查看详情");
                    tvCardOrderType.setTextColor(Color.parseColor("#666666"));
                    break;
                case 3:
                    tvCardOrderType.setText("退款中");
                    tvCardOrderGotip.setText("查看进度");
                    tvCardOrderType.setTextColor(Color.parseColor("#FF3A1E"));
                    break;
                case 4:
                    tvCardOrderType.setText("已退款");
                    tvCardOrderGotip.setText("查看详情");
                    tvCardOrderType.setTextColor(Color.parseColor("#666666"));
                    break;
                case 5:
                    tvCardOrderType.setText("已退款");
                    tvCardOrderGotip.setText("查看详情");
                    tvCardOrderType.setTextColor(Color.parseColor("#666666"));
                    break;
            }
            if (item.getCardType()==19){
                tvCardBind.setVisibility(View.GONE);
               /* RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                tvCardOrderGotip.setPadding(Utils.dip2px(mContext,12),0,Utils.dip2px(mContext,12),0);
                tvCardOrderGotip.setHeight(Utils.dip2px(mContext,24));
                tvCardOrderGotip.setGravity(Gravity.CENTER);
                tvCardOrderGotip.setLayoutParams(layoutParams);*/
            }else if (item.getCardType()==22){
                if (item.getBindPetId()>0){
                    tvCardBind.setVisibility(View.GONE);
                    /*RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    tvCardOrderGotip.setPadding(Utils.dip2px(mContext,12),0,Utils.dip2px(mContext,12),0);
                    tvCardOrderGotip.setHeight(Utils.dip2px(mContext,24));
                    tvCardOrderGotip.setGravity(Gravity.CENTER);
                    tvCardOrderGotip.setLayoutParams(layoutParams);*/
                }else {
                    tvCardBind.setVisibility(View.VISIBLE);
                }
            }
            GlideUtil.loadImg(mContext, item.getSmallPic(), ivCardOrder, R.drawable.icon_image_default);
            tvCardOrderTitle.setText(item.getServiceCardTypeName());
            if (item.getDicountDesc()!=null&&item.getDicountDesc().size()>0){
                llDiscount.setVisibility(View.VISIBLE);
                List<String> dicountDesc = item.getDicountDesc();
                llDiscount.removeAllViews();
                for (int i = 0; i < dicountDesc.size(); i++) {
                    View view = View.inflate(mContext,R.layout.item_tv_discount,null);
                    TextView tvDiscount = view.findViewById(R.id.tv_discount);
                    String descString = dicountDesc.get(i);
                    String[] split = descString.split("@@");
                    int startIndex = descString.indexOf("@@");
                    int endIndex = split[0].length() + split[1].length();
                    SpannableString ss = new SpannableString(descString.replace("@@", ""));
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.discount_style_yellow),
                            startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tvDiscount.setText(ss);
                    llDiscount.addView(view);
                }
            }else {
                llDiscount.setVisibility(View.GONE);
            }
            if (item.getTagNames()!=null&&item.getTagNames().size()>0){
                llTags.setVisibility(View.VISIBLE);
                List<String> tagNames = item.getTagNames();
                llTags.removeAllViews();
                for (int i = 0; i < tagNames.size(); i++) {
                    View view = View.inflate(mContext,R.layout.item_card_bq,null);
                    TextView tvTag = view.findViewById(R.id.tv_item_card_bq);
                    tvTag.setText(tagNames.get(i));
                    llTags.addView(view);
                }
            }else {
                llTags.setVisibility(View.GONE);
            }
            if (Utils.isDoubleEndWithZero(item.getAmount())) {
                tvCardOrderPrice.setText("¥ " + Utils.formatDouble(item.getAmount()));
            } else {
                tvCardOrderPrice.setText("¥ " + item.getAmount());
            }
            llCardOrderAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(helper.getLayoutPosition());
                    }
                }
            });
            tvCardBind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getStatus()==4||item.getStatus()==5){
                        return;
                    }else {
                        Intent intent = new Intent(mContext, ChooseFosterPetActivity.class);
                        intent.putExtra("cardId", item.getId());
                        intent.putExtra("previous", Global.BUYCARD_TO_CHOOSEPET);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    //声明接口
    private ItemClickListener listener;
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }



    //定义接口
    public interface ItemClickListener{
        //实现点击的方法，传递条目下标
        void onItemClick(int position);
    }
}
