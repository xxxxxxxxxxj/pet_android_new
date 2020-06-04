package com.haotang.pet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MainCharacteristicService;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>
 * Title:MainCharacteristicServiceAdapter
 * </p>
 * <p>
 * Description:特色服务数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2017-2-13 上午11:58:57
 */
@SuppressLint("NewApi")
public class MainCharacteristicServiceAdapter<T> extends CommonAdapter<T> {

    public MainCharacteristicServiceAdapter(Activity mContext, List<T> mDatas, int flag) {
        super(mContext, mDatas, flag);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_mainfrag_charaservice, position);
        ImageView iv_item_mainfrag_charaser = viewHolder
                .getView(R.id.iv_item_mainfrag_charaser);
        TextView tv_item_mainfrag_charaser_title = viewHolder
                .getView(R.id.tv_item_mainfrag_charaser_title);
        TextView tv_item_mainfrag_charaser_des = viewHolder
                .getView(R.id.tv_item_mainfrag_charaser_des);
        TextView tv_item_mainfrag_charaser_activity = viewHolder
                .getView(R.id.tv_item_mainfrag_charaser_activity);
        TextView tv_item_mainfrag_charaser_price = viewHolder
                .getView(R.id.tv_item_mainfrag_charaser_price);
        TextView tv_item_mainfrag_charaser_num = viewHolder
                .getView(R.id.tv_item_mainfrag_charaser_num);
        ImageView iv_item_mainfrag_hotornew = viewHolder
                .getView(R.id.iv_item_mainfrag_hotornew);
        final MainCharacteristicService MainCharacteristicService = (MainCharacteristicService) mDatas
                .get(position);
        if (MainCharacteristicService != null) {
            if (MainCharacteristicService.hotOrNew == 0) {
                iv_item_mainfrag_hotornew.setVisibility(View.GONE);
            } else if (MainCharacteristicService.hotOrNew == 1) {
                iv_item_mainfrag_hotornew.setVisibility(View.VISIBLE);
                iv_item_mainfrag_hotornew
                        .setImageResource(R.drawable.iv_item_mainfrag_hot);
            } else if (MainCharacteristicService.hotOrNew == 2) {
                iv_item_mainfrag_hotornew.setVisibility(View.VISIBLE);
                iv_item_mainfrag_hotornew.setVisibility(View.VISIBLE);
                iv_item_mainfrag_hotornew
                        .setImageResource(R.drawable.iv_item_mainfrag_new);
            }
            GlideUtil.loadImg(mContext, MainCharacteristicService.pic,
                    iv_item_mainfrag_charaser,
                    R.drawable.icon_production_default);
            Utils.setText(tv_item_mainfrag_charaser_title,
                    MainCharacteristicService.name, "", View.VISIBLE,
                    View.INVISIBLE);
            Utils.setText(tv_item_mainfrag_charaser_des,
                    MainCharacteristicService.content, "", View.VISIBLE,
                    View.INVISIBLE);
            String text = "";
            String text1 = "";
            if (flag == 1) {//首页
                text = "¥" + MainCharacteristicService.minPrice
                        + MainCharacteristicService.txtPriceMain;
                text1 = "已售 "
                        + MainCharacteristicService.totalSoldCountMain;
            } else if (flag == 2) {//特色服务列表页
                text = "¥" + MainCharacteristicService.minPrice
                        + MainCharacteristicService.txtPrice;
                text1 = "已售 "
                        + MainCharacteristicService.totalSoldCount;
            }
            SpannableString ss = new SpannableString(text);
            ss.setSpan(new ForegroundColorSpan(mContext.getResources()
                            .getColor(R.color.a999999)),
                    MainCharacteristicService.minPrice != null ? (MainCharacteristicService.minPrice.length() + 1) : 0, ss
                            .length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0, 1,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3),
                    ss.length() - 1, ss.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_item_mainfrag_charaser_price.setText(ss);
            Utils.setText(tv_item_mainfrag_charaser_num, text1, "",
                    View.VISIBLE, View.INVISIBLE);
        }
        return viewHolder.getConvertView();
    }
}
