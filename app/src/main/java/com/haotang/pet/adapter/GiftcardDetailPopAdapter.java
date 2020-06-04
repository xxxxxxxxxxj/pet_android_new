package com.haotang.pet.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.GiftCardDetail;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/26
 * @Description:
 */
public class GiftcardDetailPopAdapter extends BaseQuickAdapter<GiftCardDetail.DataBean.CardTypeTemplateListBean,BaseViewHolder> {
    public GiftcardDetailPopAdapter(@LayoutRes int layoutResId, @Nullable List<GiftCardDetail.DataBean.CardTypeTemplateListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftCardDetail.DataBean.CardTypeTemplateListBean item) {
        TextView tv_giftcard_item_price = helper.getView(R.id.tv_giftcard_item_price);
        if (item!=null){
            tv_giftcard_item_price.setText(item.getFaceValue()+"");
            if (item.isCheck()){
                tv_giftcard_item_price.setBackgroundResource(R.drawable.bg_red_round5);

            }else{
                tv_giftcard_item_price.setBackgroundResource(R.drawable.bg_gray_round_bbbbbb);
            }
        }
    }
}
