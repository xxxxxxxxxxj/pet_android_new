package com.haotang.pet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MainService;
import com.haotang.pet.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/18
 * @Description:
 */
public class MainServiceItemAdapter extends RecyclerView.Adapter<MainServiceItemAdapter.MyViewHolder> {

    private Context context;
    private List<MainService> list = new ArrayList<>();

    public MainServiceItemAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<MainService> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MainServiceItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_mainfrag_service, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainServiceItemAdapter.MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position).txt);
        GlideUtil.loadImg(context, list.get(position).pic, holder.ivIcon, R.drawable.icon_production_default);
        holder.rvRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView tvName;
        private RelativeLayout rvRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_mainservice_icon);
            tvName = itemView.findViewById(R.id.tv_mainservice_name);
            rvRoot = itemView.findViewById(R.id.rl_mainservice_root);
        }
    }
}
