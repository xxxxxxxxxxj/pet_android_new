package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.util.GlideUtil;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/30
 * @Description:
 */
public class ShopRefundIconAdapter extends RecyclerView.Adapter<ShopRefundIconAdapter.MyViewHolder> {
    private List<String> list;
    private Context context;

    public ShopRefundIconAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ShopRefundIconAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_shoprefund_icon,parent,false);
        View view = View.inflate(context,R.layout.item_shoprefund_icon,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopRefundIconAdapter.MyViewHolder holder, int position) {
        GlideUtil.loadImg(context,list.get(position),holder.ivIcon,R.drawable.icon_production_default);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_shoprefund_icon);
        }
    }
}
