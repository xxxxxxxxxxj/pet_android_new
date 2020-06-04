package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.SwitchService;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.NiceImageView;

import java.util.ArrayList;

/**
 * @author:姜谷蓄
 * @Date:2020/2/14
 * @Description:
 */
public class BeautyServiceItemAdapter extends RecyclerView.Adapter<BeautyServiceItemAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<SwitchService> list = new ArrayList<>();

    public BeautyServiceItemAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<SwitchService> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public BeautyServiceItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_beauty_service, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BeautyServiceItemAdapter.MyViewHolder holder, final int position) {
        holder.tvItemName.setText(list.get(position).getName());
        holder.tvItemPrice.setText(list.get(position).getPrice().replace("@@", "").replace("起", ""));
        GlideUtil.loadImg(context, list.get(position).getPicMini(), holder.nvItemBg, R.drawable.icon_production_default);
        if (list.get(position).getItems() != null && list.get(position).getItems().size() > 0) {
            holder.ivItemOrder.setImageResource(R.drawable.icon_serviceitem_more);
        } else {
            holder.ivItemOrder.setImageResource(R.drawable.icon_seriveitem_order);
        }
        holder.nvItemBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getItems() != null && list.get(position).getItems().size() > 0) {
                    listener.onItemClick(false,list.get(position));
                }else {
                    listener.onItemClick(true, list.get(position));
                }
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

    public interface ItemClickListener {
        void onItemClick(boolean goservice, SwitchService switchService);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NiceImageView nvItemBg;
        private TextView tvItemName;
        private TextView tvItemPrice;
        private ImageView ivItemOrder;

        public MyViewHolder(View itemView) {
            super(itemView);
            nvItemBg = itemView.findViewById(R.id.nv_serviceitem_bg);
            tvItemName = itemView.findViewById(R.id.tv_serviceitem_name);
            tvItemPrice = itemView.findViewById(R.id.tv_serviceitem_price);
            ivItemOrder = itemView.findViewById(R.id.iv_serviceitem_order);
        }
    }
}
