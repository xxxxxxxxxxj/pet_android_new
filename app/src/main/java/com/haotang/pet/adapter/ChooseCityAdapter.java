package com.haotang.pet.adapter;

import android.content.Context;
import android.graphics.Color;
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
 * @Date:2020/2/13
 * @Description:
 */
public class ChooseCityAdapter extends RecyclerView.Adapter<ChooseCityAdapter.MyViewHolder> {

    private Context context;
    private List<ShopListBean.DataBean.RegionsBean> list = new ArrayList<>();

    public ChooseCityAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ShopListBean.DataBean.RegionsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ChooseCityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_choose_city, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChooseCityAdapter.MyViewHolder holder, final int position) {
        if (list.get(position).getSelected()==1){
            holder.rvCity.setBackgroundResource(R.drawable.bg_choosecity_red);
            holder.tvCityName.setTextColor(Color.parseColor("#ffffff"));
        }else {
            holder.rvCity.setBackgroundResource(R.drawable.bg_choosecity_gray);
            holder.tvCityName.setTextColor(Color.parseColor("#384359"));
        }
        holder.tvCityName.setText(list.get(position).getCity());
        holder.rvCity.setOnClickListener(new View.OnClickListener() {
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

    private ItemClickListener listener;
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    public interface ItemClickListener{
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCityName;
        private RelativeLayout rvCity;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tv_choosecity_name);
            rvCity = itemView.findViewById(R.id.rl_choosecity);
        }
    }
}
