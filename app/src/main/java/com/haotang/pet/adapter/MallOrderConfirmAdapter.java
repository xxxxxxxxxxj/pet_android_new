package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.MallGoods;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TagTextView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:商品订单确认页商品信息适配器</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-13 14:18
 */
public class MallOrderConfirmAdapter extends BaseQuickAdapter<MallGoods, BaseViewHolder> {

    public MallOrderConfirmAdapter(int layoutResId, List<MallGoods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MallGoods item) {
        ImageView mall_order_icon = helper.getView(R.id.mall_order_icon);
        TextView textview_tag = helper.getView(R.id.textview_tag);
        TextView textview_goods_price = helper.getView(R.id.textview_goods_price);
        TagTextView textview_mall_order_name = helper.getView(R.id.textview_mall_order_name);
        TextView textview_show_atr = helper
                .getView(R.id.textview_show_atr);
        TextView textview_show_goods_nums = helper
                .getView(R.id.textview_show_goods_nums);
        if (item != null) {
            GlideUtil.loadImg(mContext, item.thumbnail, mall_order_icon, R.drawable.icon_image_default);
            if (Utils.isStrNull(item.marketingTag)) {
                textview_mall_order_name.setSingleTagAndContent(item.marketingTag, item.title);
            } else {
                textview_mall_order_name.setText(item.title);
            }
            /*if (!TextUtils.isEmpty(item.marketingTag)) {
                Utils.setText(textview_tag, item.marketingTag, "", View.VISIBLE, View.VISIBLE);
                int numCount = 0;
                int ABCCount = 0;
                int CHINACount = 0;
                char[] tagsLength = item.marketingTag.toCharArray();
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
                StringBuilder st = new StringBuilder();
                for (int i = 0; i < CHINACount + 1; i++) {
                    st.append("　");
                }
                for (int i = 0; i < numCount + 1; i++) {
                    st.append(" ");
                }
                for (int i = 0; i < ABCCount + 1; i++) {
                    st.append(" ");
                }
                String title = st + item.title;
                Utils.setText(textview_mall_order_name, title, "", View.VISIBLE, View.VISIBLE);
            } else {
                Utils.setText(textview_mall_order_name, item.title, "", View.VISIBLE, View.VISIBLE);
                textview_tag.setVisibility(View.GONE);
            }*/
            Utils.setText(textview_show_atr, item.specName, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(textview_goods_price, "" + item.retailPrice, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(textview_show_goods_nums, "X" + item.amount, "", View.VISIBLE, View.VISIBLE);
        }
    }
}