package com.haotang.pet.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ShopMallLogistics;
import com.haotang.pet.util.Utils;
import com.lin.poweradapter.PowerViewHolder;
import com.lin.poweradapter.SingleAdapter;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/1/31 13:23
 */
public class ShopMallLogisticsAdapter extends SingleAdapter<ShopMallLogistics, ShopMallLogisticsAdapter.ChildViewHolder> {
    public ShopMallLogisticsAdapter(@Nullable Object listener) {
        super(listener);
    }

    @Override
    public ChildViewHolder onCreateVHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopmall_logistics, null));
    }

    @Override
    public void onBindVHolder(ChildViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final ShopMallLogistics shopMallLogistics = getItem(position);
        if (shopMallLogistics != null) {
            Utils.setText(holder.tv_item_shopmall_logistics_title, shopMallLogistics.getText(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(holder.tv_item_shopmall_logistics_time, shopMallLogistics.getTime(), "", View.VISIBLE, View.VISIBLE);
            if (position == 0) {
                holder.tv_item_shopmall_logistics_title.setTextColor(context.getResources().getColor(R.color.aBB996C));
            } else {
                holder.tv_item_shopmall_logistics_title.setTextColor(context.getResources().getColor(R.color.a666666));
            }
            if (position == getDataItemCount() - 1) {
                holder.vw_item_shopmall_logistics_line.setVisibility(View.GONE);
            } else {
                holder.vw_item_shopmall_logistics_line.setVisibility(View.VISIBLE);
            }
        }
    }

    static class ChildViewHolder extends PowerViewHolder {
        TextView tv_item_shopmall_logistics_title;
        TextView tv_item_shopmall_logistics_time;
        View vw_item_shopmall_logistics_line;

        public ChildViewHolder(View itemView) {
            super(itemView);
            tv_item_shopmall_logistics_title = (TextView) itemView.findViewById(R.id.tv_item_shopmall_logistics_title);
            tv_item_shopmall_logistics_time = (TextView) itemView.findViewById(R.id.tv_item_shopmall_logistics_time);
            vw_item_shopmall_logistics_line = (View) itemView.findViewById(R.id.vw_item_shopmall_logistics_line);
        }
    }
}