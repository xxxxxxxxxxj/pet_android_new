package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;

/**
 * @author:姜谷蓄
 * @Date:2020/2/18
 * @Description:
 */
public class MainSmallItemAdapter extends RecyclerView.Adapter<MainSmallItemAdapter.MyViewHolder> {

    private Context context;

    @Override
    public MainSmallItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_mainfrag_smallicons, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainSmallItemAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDesc;
        private ImageView ivIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_mainfrag_middletitle);
            tvDesc = itemView.findViewById(R.id.tv_mainfrag_middledesc);
            ivIcon = itemView.findViewById(R.id.iv_mainfrag_middleicon);
        }
    }
}
