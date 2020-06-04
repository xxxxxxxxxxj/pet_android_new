package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ShopRefundSchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/30
 * @Description:商品退款进度列表适配器
 */
public class ShopRefundSchduleAdapter extends RecyclerView.Adapter<ShopRefundSchduleAdapter.MyViewHolder> {
    private Context context;
    private List<ShopRefundSchedule.DataBean.ExamineSpeedBean> list = new ArrayList<>();

    public void setList(List<ShopRefundSchedule.DataBean.ExamineSpeedBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ShopRefundSchduleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ShopRefundSchduleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shoprefund_schdule,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopRefundSchduleAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getExamineContent());
        holder.tvTime.setText(list.get(position).getExamineTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_refundschdule_title);
            tvTime = itemView.findViewById(R.id.tv_refundschdule_time);
        }
    }
}
