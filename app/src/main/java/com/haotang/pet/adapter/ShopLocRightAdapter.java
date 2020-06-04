package com.haotang.pet.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ShopListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/12
 * @Description:
 */
public class ShopLocRightAdapter extends RecyclerView.Adapter<ShopLocRightAdapter.MyViewHolder> {

    private Context context;
    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> list = new ArrayList<>();
    public ShopLocRightAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ShopLocRightAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.item_shoploc_right,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopLocRightAdapter.MyViewHolder holder, final int position) {
        holder.tvShopName.setText(list.get(position).getShopName());
        holder.tvShopAddress.setText(list.get(position).getAddress());
        holder.tvShopFar.setText(list.get(position).getDist());
        if (list.get(position).getTag()!=null&&!"".equals(list.get(position).getTag())){
            holder.tvShopTag.setVisibility(View.VISIBLE);
            holder.tvShopTag.setText(list.get(position).getTag());
        }else {
            holder.tvShopTag.setVisibility(View.GONE);
        }
        holder.rvRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(list.get(position));
            }
        });
    }

    //声明接口
    private ItemClickListener listener;
    //set方法
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    //定义接口
    public interface ItemClickListener{
        void onItemClick(ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean shopsBean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvShopName;
        private RelativeLayout rvRoot;
        private TextView tvShopTag;
        private TextView tvShopAddress;
        private TextView tvShopFar;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tv_shoploc_name);
            tvShopTag = itemView.findViewById(R.id.tv_shoploc_tag);
            tvShopAddress = itemView.findViewById(R.id.tv_shoploc_address);
            tvShopFar = itemView.findViewById(R.id.tv_shoploc_far);
            rvRoot = itemView.findViewById(R.id.rl_allshop_root);
        }
    }
}
