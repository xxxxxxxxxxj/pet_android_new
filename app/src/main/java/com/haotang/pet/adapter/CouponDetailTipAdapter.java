package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/30
 * @Description:取消商品订单提示
 */
public class CouponDetailTipAdapter extends RecyclerView.Adapter<CouponDetailTipAdapter.MyViewHolder> {

    private Context context;
    private List<String> list;

    public CouponDetailTipAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CouponDetailTipAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_shopcancel_tip,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CouponDetailTipAdapter.MyViewHolder holder, int position) {
        holder.tvTip.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTip;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTip = itemView.findViewById(R.id.tv_cancel_tip);
        }
    }
}
