package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.Production;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.NiceImageView;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/15
 * @Description:美容师详情滚动图片
 */
public class BeautyDetailPicAdapter extends RecyclerView.Adapter<BeautyDetailPicAdapter.MyViewHolder> {

    private Context context;
    private List<Production> list;

    public BeautyDetailPicAdapter(Context context, List<Production> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BeautyDetailPicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_beautydetail_product, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BeautyDetailPicAdapter.MyViewHolder holder, final int position) {
            GlideUtil.loadImg(context, list.get(position % list.size()).image, holder.nvIcon, R.drawable.icon_production_default);
            holder.nvIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position%list.size());
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size() > 3 ? Integer.MAX_VALUE : list.size();
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
        private NiceImageView nvIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            nvIcon = itemView.findViewById(R.id.nv_beautydetail_product);
        }
    }
}
