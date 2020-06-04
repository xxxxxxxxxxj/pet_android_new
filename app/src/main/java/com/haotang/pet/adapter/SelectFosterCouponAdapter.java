package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-20 11:07
 */
public class SelectFosterCouponAdapter extends BaseQuickAdapter<Coupon, BaseViewHolder> {

    public SelectFosterCouponAdapter(int layoutResId, List<Coupon> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Coupon item) {
        TextView textview_other_cannot_used = helper.getView(R.id.textview_other_cannot_used);
        RelativeLayout rl_coupon_desc_bottom = helper.getView(R.id.rl_coupon_desc_bottom);
        ImageView iv_coupon_othergive = helper.getView(R.id.iv_coupon_othergive);
        ImageView iv_usecoupon_buy = helper.getView(R.id.iv_usecoupon_buy);
        RelativeLayout rl_coupon_share = helper.getView(R.id.rl_coupon_share);
        TextView tv_coupon_name = helper.getView(R.id.tv_coupon_name);
        TextView tv_coupon_usetime = helper.getView(R.id.tv_coupon_usetime);
        TextView tv_coupon_desc = helper.getView(R.id.tv_coupon_desc);
        ImageView iv_coupon_type = helper.getView(R.id.iv_coupon_type);
        TextView tv_coupon_usetext = helper.getView(R.id.tv_coupon_usetext);
        TextView tv_coupon_leastrmb = helper.getView(R.id.tv_coupon_leastrmb);
        TextView tv_coupon_leastnum = helper.getView(R.id.tv_coupon_leastnum);
        TextView tv_coupon_willend = helper.getView(R.id.tv_coupon_willend);
        RelativeLayout rl_coupon_least = helper.getView(R.id.rl_coupon_least);
        TextView tv_coupon_least = helper.getView(R.id.tv_coupon_least);
        LinearLayout layout_is_show_unused = helper.getView(R.id.layout_is_show_unused);
        ImageView img_select = helper.getView(R.id.img_select);
        ImageView iv_coupon_state = helper.getView(R.id.iv_coupon_state);
        if (item != null) {
            iv_usecoupon_buy.setVisibility(View.GONE);
            rl_coupon_share.setVisibility(View.GONE);
            Utils.setText(tv_coupon_name, item.name, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_coupon_usetime, item.starttime + "-" + item.endtime, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_coupon_desc, item.content, "", View.VISIBLE, View.VISIBLE);
            //是否是好友赠送
            if (item.isGive == 1) {
                iv_coupon_othergive.setVisibility(View.VISIBLE);
            } else {
                iv_coupon_othergive.setVisibility(View.GONE);
            }
            //0非即将过期 1即将过期
            if (item.isToExpire == 0) {
                tv_coupon_willend.setVisibility(View.GONE);
            } else {
                tv_coupon_willend.setVisibility(View.VISIBLE);
            }
            if (item.isShow) {
                layout_is_show_unused.setVisibility(View.VISIBLE);
            } else {
                layout_is_show_unused.setVisibility(View.GONE);
            }
            if (item.reduceType == 3) {//免单券
                tv_coupon_least.setVisibility(View.GONE);
                rl_coupon_least.setVisibility(View.GONE);
                tv_coupon_usetext.setVisibility(View.VISIBLE);
                tv_coupon_usetext.setText("免单");
            } else if (item.reduceType == 1) {//满减券
                Utils.setText(tv_coupon_least, item.least, "", View.VISIBLE, View.GONE);
                Utils.setText(tv_coupon_leastnum, item.amount, "", View.VISIBLE, View.VISIBLE);
                tv_coupon_usetext.setVisibility(View.GONE);
                rl_coupon_least.setVisibility(View.VISIBLE);
            } else if (item.reduceType == 2) {//折扣券
                tv_coupon_least.setVisibility(View.GONE);
                String text = "";
                if (Utils.isDoubleEndWithZero(ComputeUtil.mul(item.zhekou, 10))) {
                    text = Utils.formatDouble(ComputeUtil.mul(item.zhekou, 10)) + "折";
                } else {
                    text = ComputeUtil.mul(item.zhekou, 10) + "折";
                }
                rl_coupon_least.setVisibility(View.GONE);
                tv_coupon_usetext.setVisibility(View.VISIBLE);
                tv_coupon_usetext.setText(text);
            }
            if (item.isAvali == 0) {//不可用
                img_select.setVisibility(View.GONE);
                if (item.category == 3 || item.category == 5 || item.category == 6) {//商品
                    iv_coupon_type.setImageResource(R.drawable.icon_coupon_goodsnouse);
                } else if (item.category == 1 || item.category == 2) {//服务
                    iv_coupon_type.setImageResource(R.drawable.icon_coupon_servicenouse);
                } else if (item.category == 7) {//寄养
                    iv_coupon_type.setImageResource(R.drawable.icon_coupon_fosternouse);
                }
                tv_coupon_usetext.setTextColor(mContext.getResources().getColor(R.color.a333333));
                tv_coupon_leastrmb.setTextColor(mContext.getResources().getColor(R.color.a333333));
                tv_coupon_leastnum.setTextColor(mContext.getResources().getColor(R.color.a333333));
                iv_coupon_state.setVisibility(View.VISIBLE);
                textview_other_cannot_used.setVisibility(View.GONE);
                iv_coupon_state.setImageResource(R.drawable.icon_coupon_bky);
                if (item.reasons.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < item.reasons.size(); i++) {
                        if (i == item.reasons.size() - 1) {
                            stringBuilder.append(item.reasons.get(i));
                        } else {
                            stringBuilder.append(item.reasons.get(i) + "\n");
                        }
                    }
                    textview_other_cannot_used.setText("." + stringBuilder.toString());
                    textview_other_cannot_used.setVisibility(View.VISIBLE);
                    rl_coupon_desc_bottom.bringToFront();
                }
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textview_other_cannot_used.getLayoutParams();
                if (item.isShow) {
                    lp.topMargin = (int) mContext.getResources().getDimension(R.dimen.dp_207);
                } else {
                    lp.topMargin = (int) mContext.getResources().getDimension(R.dimen.dp_177);
                }
                textview_other_cannot_used.setLayoutParams(lp);
            } else if (item.isAvali == 1) {//可用
                tv_coupon_usetext.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                tv_coupon_leastrmb.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                tv_coupon_leastnum.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                iv_coupon_state.setVisibility(View.GONE);
                img_select.setVisibility(View.VISIBLE);
                textview_other_cannot_used.setVisibility(View.GONE);
                if (item.isChoose) {
                    img_select.setImageResource(R.drawable.icon_petadd_select);
                } else {
                    img_select.setImageResource(R.drawable.icon_petadd_unselect);
                }
                if (item.category == 3 || item.category == 5 || item.category == 6) {//商品
                    tv_coupon_usetext.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                    tv_coupon_leastrmb.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                    tv_coupon_leastnum.setTextColor(mContext.getResources().getColor(R.color.aBB996C));
                    iv_coupon_type.setImageResource(R.drawable.icon_coupon_goods);
                } else if (item.category == 1 || item.category == 2) {//服务
                    tv_coupon_usetext.setTextColor(mContext.getResources().getColor(R.color.afe7567));
                    tv_coupon_leastrmb.setTextColor(mContext.getResources().getColor(R.color.afe7567));
                    tv_coupon_leastnum.setTextColor(mContext.getResources().getColor(R.color.afe7567));
                    iv_coupon_type.setImageResource(R.drawable.icon_coupon_service);
                } else if (item.category == 7) {//寄养
                    tv_coupon_usetext.setTextColor(mContext.getResources().getColor(R.color.afc3962));
                    tv_coupon_leastrmb.setTextColor(mContext.getResources().getColor(R.color.afc3962));
                    tv_coupon_leastnum.setTextColor(mContext.getResources().getColor(R.color.afc3962));
                    iv_coupon_type.setImageResource(R.drawable.icon_coupon_foster);
                }
            }
        }
    }
}
