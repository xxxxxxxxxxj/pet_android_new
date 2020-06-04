package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ShopListBean;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/10/23
 * @Description:
 */
public class ShopAreaAdapter extends RecyclerView.Adapter<ShopAreaAdapter.MyViewHolder> {
    private Context context;
    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean> list;

    public ShopAreaAdapter(Context context, List<ShopListBean.DataBean.RegionsBean.RegionMapBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ShopAreaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_regions_adapter,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopAreaAdapter.MyViewHolder holder, final int position) {
        holder.tvArea.setText(list.get(position).getRegion());
        holder.tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
        if (list.get(position).getSelected() == 1) {
            holder.tvArea.setBackgroundResource(R.drawable.bg_red_jianbian_round);
            holder.tvArea.setText(list.get(position).getRegion()+"("+list.get(position).getShopNum()+")");
            holder.tvArea.setTextColor(context.getResources().getColor(
                    R.color.white));
        } else {
            holder.tvArea.setBackgroundResource(R.drawable.bg_white_round_bd);
            holder.tvArea.setText(list.get(position).getRegion()+"("+list.get(position).getShopNum()+")");
            holder.tvArea.setTextColor(context.getResources().getColor(
                    R.color.black));
        }
    }
    private ItemClickListener listener;
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    public interface ItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvArea;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvArea = itemView.findViewById(R.id.tv_item_regions);
        }
    }
}
