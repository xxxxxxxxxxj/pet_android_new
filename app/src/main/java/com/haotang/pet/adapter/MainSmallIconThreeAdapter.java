package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.haotang.pet.R;
import com.haotang.pet.entity.MainService;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.NiceImageView;

import java.util.ArrayList;

/**
 * @author:姜谷蓄
 * @Date:2020/2/20
 * @Description:
 */
public class MainSmallIconThreeAdapter extends RecyclerView.Adapter<MainSmallIconThreeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MainService> list = new ArrayList<MainService>();

    public void setList(ArrayList<MainService> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public MainSmallIconThreeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MainSmallIconThreeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_mainsmallicon_three,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainSmallIconThreeAdapter.MyViewHolder holder, final int position) {
        GlideUtil.loadImg(context,list.get(position).pic,holder.nvIcon,R.drawable.icon_production_default);
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //声明接口
    private ItemClickListener listener;
    //set方法
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    //定义接口
    public interface ItemClickListener{
        //实现点击的方法，传递条目下标
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlRoot;
        private NiceImageView nvIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            rlRoot = itemView.findViewById(R.id.rl_smallicon_root);
            nvIcon = itemView.findViewById(R.id.nv_mainfrag_smallicon);
        }
    }
}
