package com.haotang.pet.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.GiftCardDetail;
import com.haotang.pet.util.Utils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/26
 * @Description:点击赠送优惠券底部弹出的适配器
 */
public class GiftDetailPopFreeAdapter extends BaseQuickAdapter<GiftCardDetail.DataBean.CouponTypeListBean, BaseViewHolder> {
    public GiftDetailPopFreeAdapter(@LayoutRes int layoutResId, @Nullable List<GiftCardDetail.DataBean.CouponTypeListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftCardDetail.DataBean.CouponTypeListBean item) {
        TextView tvfreeName = helper.getView(R.id.tv_item_freename);
        TextView tvfreeData = helper.getView(R.id.tv_item_freedata);
        final ImageView ivfreeOpen = helper.getView(R.id.iv_item_freeopen);
        RelativeLayout rlfreeOpen = helper.getView(R.id.rl_item_open);
        ImageView ivCardFree = helper.getView(R.id.iv_giftcard_free);
        TextView tvMoney = helper.getView(R.id.tv_couponitem_money);
        LinearLayout llDiscount = helper.getView(R.id.ll_card_discount);
        TextView tvDiscount = helper.getView(R.id.tv_card_discount);
        TextView tvShowprice = helper.getView(R.id.texview_show_price);
        LinearLayout llManjian = helper.getView(R.id.ll_card_manjian);
        final RelativeLayout rlfreeShow = helper.getView(R.id.rl_item_showall);
        TextView tvfreeContent = helper.getView(R.id.tv_item_free_content);
        if (item!=null){
            tvfreeName.setText(item.getCouponName());
            if (item.getDuration()>0){
                tvfreeData.setText(item.getDuration()+"天可用");
            }else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tvfreeData.getLayoutParams());
                lp.setMargins(Utils.dip2px(mContext,10),Utils.dip2px(mContext,2),0,0);
                tvfreeData.setLayoutParams(lp);
                tvfreeData.setText("预约"+item.getStartTime().replace("-",".")+"-"+item.getEndTime().replace("-",".")+"\n期间服务可用");
            }
            tvfreeContent.setText(item.getDescription());
            switch (item.getReduceType()){
                case 1:
                    ivCardFree.setVisibility(View.GONE);
                    llDiscount.setVisibility(View.GONE);
                    llManjian.setVisibility(View.VISIBLE);
                    tvMoney.setText(getDoubleString(item.getAmount()));
                    tvShowprice.setText(item.getLeast());
                    break;
                case 2:
                    ivCardFree.setVisibility(View.GONE);
                    llDiscount.setVisibility(View.VISIBLE);
                    tvDiscount.setText(getDoubleString(item.getAmount()*10));
                    llManjian.setVisibility(View.GONE);
                    break;
                case 3:
                    ivCardFree.setVisibility(View.VISIBLE);
                    llManjian.setVisibility(View.GONE);
                    llDiscount.setVisibility(View.GONE);
                    break;
            }

        }
        rlfreeOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rlfreeShow.getVisibility()==View.GONE){
                    rlfreeShow.setVisibility(View.VISIBLE);
                    ivfreeOpen.setImageResource(R.drawable.giftcard_free_up_icon);
                }else if (rlfreeShow.getVisibility()==View.VISIBLE){
                    rlfreeShow.setVisibility(View.GONE);
                    ivfreeOpen.setImageResource(R.drawable.giftcard_free_down_icon);
                }
            }
        });
    }
    public static String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;
    }
}
