package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/21
 * @Description:
 */
public class CommonEvaIconAdapter extends RecyclerView.Adapter<CommonEvaIconAdapter.MyViewHolder> {

    private Context context;
    private List<String> list = new ArrayList<>();

    public CommonEvaIconAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public CommonEvaIconAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_commoneva_icons,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonEvaIconAdapter.MyViewHolder holder, final int position) {
        GlideUtil.loadImg(context,list.get(position),holder.nvIcons,R.drawable.icon_production_default);
        holder.nvIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.imageBrower(context, position, list.toArray(new String[list.size()]));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NiceImageView nvIcons;
        public MyViewHolder(View itemView) {
            super(itemView);
            nvIcons = itemView.findViewById(R.id.nv_commoneva_icon);
        }
    }
}
