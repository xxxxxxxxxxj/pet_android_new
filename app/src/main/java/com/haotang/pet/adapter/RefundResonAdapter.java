package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CancelReasonBean;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/27
 * @Description:选择退换货原因适配器
 */
public class RefundResonAdapter extends RecyclerView.Adapter<RefundResonAdapter.MyViewHolder> {

    private List<CancelReasonBean> list ;
    private Context context;

    public RefundResonAdapter(List<CancelReasonBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RefundResonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refund_reson,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RefundResonAdapter.MyViewHolder holder, final int position) {
        holder.tv_reason.setText(list.get(position).getTxt());
        if (list.get(position).isChoose()){
            holder.iv_check.setImageResource(R.drawable.icon_reason_selected);
        }else {
            holder.iv_check.setImageResource(R.drawable.icon_reason_unselected);
        }
        holder.rl_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChcekClickListener.onCheckClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public onChcekClickListener onChcekClickListener;

    public void setOnChcekClickListener(RefundResonAdapter.onChcekClickListener onChcekClickListener) {
        this.onChcekClickListener = onChcekClickListener;
    }

    public interface onChcekClickListener{
        void onCheckClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_check;
        private ImageView iv_check;
        private TextView tv_reason;
        public MyViewHolder(View itemView) {
            super(itemView);
            rl_check = itemView.findViewById(R.id.rl_refund_check);
            iv_check = itemView.findViewById(R.id.iv_refund_check);
            tv_reason = itemView.findViewById(R.id.tv_refund_reason);
        }
    }
}
