package com.haotang.pet.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.PostSelectionDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.PostSelectionBean;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/19
 * @Description:首页宠圈推荐
 */
public class MainGoodPostAdapter extends RecyclerView.Adapter<MainGoodPostAdapter.MyViewHolder> {

    private Context context;
    private List<PostSelectionBean.PostsBean> list = new ArrayList<>();


    public MainGoodPostAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<PostSelectionBean.PostsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MainGoodPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_mainfrag_petcicler, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainGoodPostAdapter.MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position).getUserName());
        holder.tvContent.setText(list.get(position).getContent());
        holder.tvTime.setText(list.get(position).getCreated());
        GlideUtil.loadImg(context, list.get(position).getAvatar(), holder.ivHead, R.drawable.icon_production_default);
        if (list.get(position).getImgs() != null && list.get(position).getImgs().size() > 0) {
            GlideUtil.loadImg(context, list.get(position).getImgs().get(0), holder.ivContent, R.drawable.icon_production_default);
        }
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,
                        PostSelectionDetailActivity.class);
                intent.putExtra("postId", list.get(position).getId());
                intent.putExtra("flag", "click");
                context.startActivity(intent);
            }
        });
        holder.ivCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,
                        PostSelectionDetailActivity.class);
                intent.putExtra("postId", list.get(position).getId());
                intent.putExtra("flag", "comment");
                context.startActivity(intent);
            }
        });
        if (list.get(position).getGiftUsers() != null && list.get(position).getGiftUsers().size() > 0) {
            holder.rvHeads.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            MainPostHeadsAdapter adapter = new MainPostHeadsAdapter(context, list.get(position).getGiftUsers());
            holder.rvHeads.setLayoutManager(layoutManager);
            holder.rvHeads.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            holder.rvHeads.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size()>=2){
            return 2;
        }else {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NiceImageView ivHead;
        private RelativeLayout rlRoot;
        private RecyclerView rvHeads;
        private TextView tvTime;
        private TextView tvName;
        private TextView tvContent;
        private NiceImageView ivContent;
        private ImageView ivCommon;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.nv_petcicler_head);
            tvContent = itemView.findViewById(R.id.tv_petcicler_content);
            tvName = itemView.findViewById(R.id.tv_petcicler_name);
            ivContent = itemView.findViewById(R.id.nv_petcicler_icon);
            tvTime = itemView.findViewById(R.id.tv_petcicler_time);
            rlRoot = itemView.findViewById(R.id.rl_petcicler_root);
            ivCommon = itemView.findViewById(R.id.iv_petcicler_eva);
            rvHeads = itemView.findViewById(R.id.rv_petcicler_heads);
        }
    }
}
