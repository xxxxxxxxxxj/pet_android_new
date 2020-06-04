package com.haotang.pet.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
public class ShopLocLeftAdapter extends RecyclerView.Adapter<ShopLocLeftAdapter.MyViewHolder> {

    private Context context;

    public ShopLocLeftAdapter(Context context) {
        this.context = context;
    }

    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean> list = new ArrayList<>();

    public void setList(List<ShopListBean.DataBean.RegionsBean.RegionMapBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ShopLocLeftAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_shoploc_left,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShopLocLeftAdapter.MyViewHolder holder, final int position) {
        if (list.get(position).getSelected()==1){
            holder.tvArea.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tvArea.setTextColor(Color.parseColor("#FF3A1E"));
        }else {
            holder.tvArea.setBackgroundColor(Color.parseColor("#fff6f8fa"));
            holder.tvArea.setTextColor(Color.parseColor("#717985"));
        }
        holder.tvArea.setText(list.get(position).getRegion()+"("+list.get(position).getShopNum()+")");
        holder.tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (position==i){
                        list.get(position).setSelected(1);
                    }else {
                        list.get(i).setSelected(0);
                    }
                    if (list.get(i).getSelected()==1){
                        holder.tvArea.setBackgroundColor(Color.parseColor("#ffffff"));
                        holder.tvArea.setTextColor(Color.parseColor("#FF3A1E"));
                    }else {
                        holder.tvArea.setBackgroundColor(Color.parseColor("#fff6f8fa"));
                        holder.tvArea.setTextColor(Color.parseColor("#717985"));
                    }
                }
                notifyDataSetChanged();
                listener.onItemClick(list.get(position).getShops());
            }
        });
    }

    private ItemClickListener listener;
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    public interface ItemClickListener{
        void onItemClick(List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shops);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvArea;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvArea = itemView.findViewById(R.id.tv_shoploc_area);
        }
    }
}
