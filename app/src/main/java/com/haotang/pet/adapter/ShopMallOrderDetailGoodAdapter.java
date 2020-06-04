package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.LogisticsDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.MallOrderDetailGoodItems;
import com.haotang.pet.entity.MallOrderDetailGoodsLogisticsInfo;
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
 * @date XJ on 2017/9/4 17:12
 */
public class ShopMallOrderDetailGoodAdapter<T> extends CommonAdapter<T> {

    public ShopMallOrderDetailGoodAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MallOrderDetailGoodItems mallOrderDetailGoodItems = (MallOrderDetailGoodItems) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_shopmallorderdetailgoods1, position);
        ImageView iv_item_shopmallorderdetail_order = viewHolder
                .getView(R.id.iv_item_shopmallorderdetail_order);/*
        TextView tv_item_shopmallorderdetail_order_titletag = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_order_titletag);*/
        TagTextView tv_item_shopmallorderdetail_order_title = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_order_title);
        TextView tv_item_shopmallorderdetail_order_gg = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_order_gg);
        TextView tv_item_shopmallorderdetail_order_num = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_order_num);
        TextView tv_item_shopmallorderdetail_order_price = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_order_price);
        LinearLayout ll_item_shopmallorderdetail_order = viewHolder
                .getView(R.id.ll_item_shopmallorderdetail_order);
        RelativeLayout rl_item_shopmallorderdetail_kuaidi = viewHolder
                .getView(R.id.rl_item_shopmallorderdetail_kuaidi);
        View vw_item_shopmallorderdetail_order = viewHolder
                .getView(R.id.vw_item_shopmallorderdetail_order);
        TextView tv_item_shopmallorderdetail_logistics = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_logistics);
        TextView tv_item_shopmallorderdetail_time = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_time);
        TextView tv_item_shopmallorderdetail_ytk = viewHolder
                .getView(R.id.tv_item_shopmallorderdetail_ytk);
        if (mallOrderDetailGoodItems != null) {
            if (mallOrderDetailGoodItems.getClassIndex() == -1 && mallOrderDetailGoodItems.getState() >= 2) {
                if (mallOrderDetailGoodItems.getState() == 4) {
                    tv_item_shopmallorderdetail_ytk.setVisibility(View.VISIBLE);
                    rl_item_shopmallorderdetail_kuaidi.setVisibility(View.GONE);
                } else {
                    tv_item_shopmallorderdetail_ytk.setVisibility(View.GONE);
                    rl_item_shopmallorderdetail_kuaidi.setVisibility(View.VISIBLE);
                    if (mallOrderDetailGoodItems.getLogId() > 0) {
                        tv_item_shopmallorderdetail_time.setVisibility(View.VISIBLE);
                        MallOrderDetailGoodsLogisticsInfo mallOrderDetailGoodsLogisticsInfo = mallOrderDetailGoodItems.getMallOrderDetailGoodsLogisticsInfo();
                        if (mallOrderDetailGoodsLogisticsInfo != null) {
                            Utils.setText(tv_item_shopmallorderdetail_logistics, mallOrderDetailGoodsLogisticsInfo.getContext(), "", View.VISIBLE, View.VISIBLE);
                            Utils.setText(tv_item_shopmallorderdetail_time, mallOrderDetailGoodsLogisticsInfo.getFtime(), "", View.VISIBLE, View.VISIBLE);
                            try {
                                if (TextUtils.isEmpty(mallOrderDetailGoodsLogisticsInfo.getContext())&&TextUtils.isEmpty(mallOrderDetailGoodsLogisticsInfo.getFtime())){
                                    tv_item_shopmallorderdetail_time.setVisibility(View.GONE);
                                    Utils.setText(tv_item_shopmallorderdetail_logistics, mallOrderDetailGoodItems.getInfoMsg(), "", View.VISIBLE, View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        tv_item_shopmallorderdetail_time.setVisibility(View.GONE);
                        Utils.setText(tv_item_shopmallorderdetail_logistics, mallOrderDetailGoodItems.getInfoMsg(), "", View.VISIBLE, View.VISIBLE);
                    }
                }
            } else {
                tv_item_shopmallorderdetail_ytk.setVisibility(View.GONE);
                rl_item_shopmallorderdetail_kuaidi.setVisibility(View.GONE);
            }
            if (mallOrderDetailGoodItems.getLastIndex() == -1) {
                vw_item_shopmallorderdetail_order.setVisibility(View.VISIBLE);
            } else {
                vw_item_shopmallorderdetail_order.setVisibility(View.GONE);
            }
            GlideUtil.loadImg(mContext, mallOrderDetailGoodItems.getThumbnail(), iv_item_shopmallorderdetail_order,
                    R.drawable.icon_production_default);

            if (Utils.isStrNull(mallOrderDetailGoodItems.getMarketingTag())) {
                tv_item_shopmallorderdetail_order_title.setSingleTagAndContent(mallOrderDetailGoodItems.getMarketingTag(), mallOrderDetailGoodItems.getTitle());
            } else {
                tv_item_shopmallorderdetail_order_title.setText(mallOrderDetailGoodItems.getTitle());
            }/*

            StringBuffer sb = new StringBuffer();
            sb.setLength(0);
            if (Utils.isStrNull(mallOrderDetailGoodItems.getMarketingTag())) {
                tv_item_shopmallorderdetail_order_titletag.setVisibility(View.VISIBLE);
                tv_item_shopmallorderdetail_order_titletag.setText(mallOrderDetailGoodItems.getMarketingTag());
                int numCount = 0;
                int ABCCount = 0;
                int CHINACount = 0;
                char[] tagsLength = mallOrderDetailGoodItems.getMarketingTag().toCharArray();
                for (int i = 0; i < tagsLength.length; i++) {
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m = p.matcher(String.valueOf(tagsLength[i]));
                    if (m.matches()) {
                        numCount++;
                    }
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(String.valueOf(tagsLength[i]));
                    if (m.matches()) {
                        numCount++;
                    }
                    p = Pattern.compile("[\u4e00-\u9fa5]");
                    m = p.matcher(String.valueOf(tagsLength[i]));
                    if (m.matches()) {
                        CHINACount++;
                    }
                }
                for (int i = 0; i < CHINACount + 1; i++) {
                    sb.append("　");
                }
                for (int i = 0; i < numCount + 1; i++) {
                    sb.append(" ");
                }
                for (int i = 0; i < ABCCount + 1; i++) {
                    sb.append(" ");
                }
            } else {
                tv_item_shopmallorderdetail_order_titletag.setVisibility(View.GONE);
            }
            sb.append(mallOrderDetailGoodItems.getTitle());
            Utils.setText(tv_item_shopmallorderdetail_order_title, sb.toString(), "", View.VISIBLE, View.VISIBLE);*/
            Utils.setText(tv_item_shopmallorderdetail_order_gg, "商品规格：" + mallOrderDetailGoodItems.getSpecName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_shopmallorderdetail_order_num, "X" + mallOrderDetailGoodItems.getAmount(), "", View.VISIBLE, View.VISIBLE);
            String text = "";
            if (Utils.isDoubleEndWithZero(mallOrderDetailGoodItems.getRetailPrice())) {
                text = "¥" + Utils.formatDouble(mallOrderDetailGoodItems.getRetailPrice());
            } else {
                text = "¥" + mallOrderDetailGoodItems.getRetailPrice();
            }
            SpannableString ss = new SpannableString(text);
            ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
                    1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_item_shopmallorderdetail_order_price.setText(ss);
            rl_item_shopmallorderdetail_kuaidi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mallOrderDetailGoodItems.getLogId() > 0) {
                        mContext.startActivity(new Intent(mContext, LogisticsDetailActivity.class).putExtra("logisticsId", mallOrderDetailGoodItems.getLogId()));

                    }
                }
            });
            ll_item_shopmallorderdetail_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", mallOrderDetailGoodItems.getCommodityId()));
                }
            });
        }
        return viewHolder.getConvertView();
    }
}
