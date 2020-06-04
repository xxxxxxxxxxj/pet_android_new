package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MallOrderDetailGoodItems;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/25
 * @Description:退换货商品列表适配器
 */
public class ShopRefundAdapter extends RecyclerView.Adapter<ShopRefundAdapter.MyViewHolder> {
    private Context context;
    private List<MallOrderDetailGoodItems> list;

    public ShopRefundAdapter(Context context, List<MallOrderDetailGoodItems> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ShopRefundAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_refund,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopRefundAdapter.MyViewHolder holder, int position) {
        GlideUtil.loadImg(context, list.get(position).getThumbnail(), holder.ivRefundShop,
                R.drawable.icon_production_default);
        Utils.setText(holder.tvRefundName, list.get(position).getTitle(), "", View.VISIBLE, View.VISIBLE);
        Utils.setText(holder.tvRefundType, list.get(position).getSpecName(), "", View.VISIBLE, View.VISIBLE);
        Utils.setText(holder.tvRecundNum, "x" + list.get(position).getAmount(), "", View.VISIBLE, View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivRefundShop;
        private TextView tvRefundName;
        private TextView tvRefundType;
        private TextView tvRecundNum;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivRefundShop = itemView.findViewById(R.id.iv_refund_icon);
            tvRefundName = itemView.findViewById(R.id.tv_refund_name);
            tvRefundType = itemView.findViewById(R.id.tv_refund_type);
            tvRecundNum = itemView.findViewById(R.id.tv_refund_num);
        }
    }
}
