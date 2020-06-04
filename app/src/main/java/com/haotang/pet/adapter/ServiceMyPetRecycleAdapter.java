package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2018/8/9 0009.
 */

public class ServiceMyPetRecycleAdapter extends RecyclerView.Adapter<ServiceMyPetRecycleAdapter.MyViewHolder> {
    public Context mContext;
    private List<Pet> myPetList;
    public ServiceMyPetRecycleAdapter(Context mContext,List<Pet> myPetList){
        this.mContext = mContext;
        this.myPetList = myPetList;
        Utils.mLogError("==-->11111  0000");
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Utils.mLogError("==-->11111  11111");
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_service_mypetrecycle,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Utils.mLogError("==-->11111  22222");
        Pet pet = myPetList.get(position);
        Utils.mLogError("==-->pet.nickName "+pet.nickName);
        GlideUtil.loadCircleImg(mContext,pet.image,holder.img_icon_pet,0);
        holder.textview_pet_name.setText(pet.nickName);
    }

    @Override
    public int getItemCount() {
        return myPetList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_icon_pet;
        private TextView textview_pet_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_icon_pet = (ImageView) itemView.findViewById(R.id.img_icon_pet);
            textview_pet_name = (TextView) itemView.findViewById(R.id.textview_pet_name);
        }
    }

}
