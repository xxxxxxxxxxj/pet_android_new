package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.PostSelectionBean;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.NiceImageView;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/3/16
 * @Description:首页宠圈头像适配器
 */
public class MainPostHeadsAdapter extends RecyclerView.Adapter<MainPostHeadsAdapter.MyViewHolder> {

    private Context context;
    private List<PostSelectionBean.PostsBean.GiftUsers> list;

    public MainPostHeadsAdapter(Context context, List<PostSelectionBean.PostsBean.GiftUsers> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MainPostHeadsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_mainfrag_postheads,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainPostHeadsAdapter.MyViewHolder holder, int position) {
        GlideUtil.loadImg(context,list.get(position).getAvatar(),holder.nvHead,R.drawable.icon_production_default);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NiceImageView nvHead;
        public MyViewHolder(View itemView) {
            super(itemView);
            nvHead = itemView.findViewById(R.id.nv_post_heads);
        }
    }
}
