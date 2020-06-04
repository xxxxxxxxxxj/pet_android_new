package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CommodityMarquee;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/1 10:33
 */
public class CommodityMarqueeAdapter extends BaseAdapter {
    private final Activity mContext;
    private List<CommodityMarquee> list;
    private LayoutInflater mInflater;

    public CommodityMarqueeAdapter(Activity mContext, List<CommodityMarquee> list) {
        this.mContext = mContext;
        this.list = list;
        this.mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 将数据循环展示三遍
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(list.size() > 0 ? (arg0 % list.size()) : 0);
    }

    @Override
    public long getItemId(int arg0) {
        return list.size() > 0 ? (arg0 % list.size()) : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHoler();
            convertView = mInflater.inflate(R.layout.item_commoditymarquee, null);
            viewHolder.iv_item_commodity_marquee = (ImageView) convertView.findViewById(R.id.iv_item_commodity_marquee);
            viewHolder.tv_item_commodity_marquee = (TextView) convertView.findViewById(R.id.tv_item_commodity_marquee);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHoler) convertView.getTag();
        }
        if (list.size()>0){
            GlideUtil.loadCircleImg(mContext, list.get(position % list.size()).getAvatar(), viewHolder.iv_item_commodity_marquee,R.drawable.user_icon_unnet_circle);
            Utils.setText(viewHolder.tv_item_commodity_marquee, list.get(position % list.size()).getContent(), "", View.VISIBLE, View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHoler {
        ImageView iv_item_commodity_marquee;
        TextView tv_item_commodity_marquee;
    }
}