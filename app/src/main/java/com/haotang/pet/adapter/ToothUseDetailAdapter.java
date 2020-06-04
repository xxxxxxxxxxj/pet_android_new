package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ToothCardUse;
import com.haotang.pet.util.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/27
 * @Description:
 */
public class ToothUseDetailAdapter extends RecyclerView.Adapter<ToothUseDetailAdapter.MyViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ToothCardUse.DataBean.DatasetBean.ListBean> list = new ArrayList<>();

    public ToothUseDetailAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ToothCardUse.DataBean.DatasetBean.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ToothUseDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_toothcard_use,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ToothUseDetailAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getItem());
        holder.tvData.setText(list.get(position).getTradeDate());
        holder.tvDiscount.setText(list.get(position).getDiscountPrice());
    }

    @Override
    public long getHeaderId(int position) {
        Log.e("TAG", "getHeaderId"+list.get(position).getHeaderId());
        long headerId = 0;
        if (list != null && list.size() > 0) {
            headerId = list.get(position).getHeaderId();
        }
        return headerId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toothcard_headview, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        if (list != null && list.size() > 0) {
            ToothCardUse.DataBean.DatasetBean.ListBean listBean = list.get(position);
            if (listBean != null) {
                textView.setVisibility(View.VISIBLE);
                Log.e("TAG", "data = " + listBean.getData());
                Utils.setText(textView, listBean.getData(), "", View.VISIBLE, View.GONE);
            } else {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvData;
        private TextView tvDiscount;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_toothcard_useitem);
            tvData = itemView.findViewById(R.id.tv_toothcard_usedata);
            tvDiscount = itemView.findViewById(R.id.tv_toothcard_uesprice);
        }
    }
}
