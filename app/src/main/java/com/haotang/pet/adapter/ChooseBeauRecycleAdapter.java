package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.ChangeOrderChooseBeauActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/8/29 0029.
 */

public class ChooseBeauRecycleAdapter  extends RecyclerView.Adapter<ChooseBeauRecycleAdapter.MyViewHolder>{
    public Context mContext;
    public List<Beautician> mDatas;
    public Beautician beauticianold;
    public ChooseBeauRecycleAdapter(Context mContext,List<Beautician> mDatas){
        this.mContext=mContext;
        this.mDatas=mDatas;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_choose_beau,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Beautician beautician = mDatas.get(position);
        if (beauticianold!=null){
            if (beauticianold.id == beautician.id){
                holder.img_choose_beau.setVisibility(View.VISIBLE);
            }else {
                holder.img_choose_beau.setVisibility(View.GONE);
            }
        }
        if (beautician.isChoose){
            holder.img_choose_beau.setVisibility(View.VISIBLE);
        }else {
            holder.img_choose_beau.setVisibility(View.GONE);
        }
        GlideUtil.loadCircleImg(mContext,beautician.image,holder.img_beau_icon,R.drawable.icon_default);
        holder.beau_name.setText(beautician.name);
        holder.layout_choose_beau_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeOrderChooseBeauActivity.act.clickBeau(beautician,position);
            }
        });
        holder.img_beau_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeOrderChooseBeauActivity.act.clickBeau(beautician,position);
            }
        });
        holder.beau_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeOrderChooseBeauActivity.act.clickBeau(beautician,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_beau_icon;
        private ImageView img_choose_beau;
        private TextView beau_name;
        private RelativeLayout layout_choose_beau_out;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_beau_icon = (ImageView) itemView.findViewById(R.id.img_beau_icon);
            img_choose_beau = (ImageView) itemView.findViewById(R.id.img_choose_beau);
            beau_name = (TextView) itemView.findViewById(R.id.beau_name);
            layout_choose_beau_out = (RelativeLayout) itemView.findViewById(R.id.layout_choose_beau_out);
        }
    }
    public void setOldBeaution(Beautician beauticianold){
        this.beauticianold = beauticianold;
    }
}
