package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.ADActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class MyPetFragmentAdapter extends RecyclerView.Adapter<MyPetFragmentAdapter.MyViewHolder> {
    public Context mContext;
    public ArrayList<Pet> mDatas;
    private int [] WH;
    public MyPetFragmentAdapter(Context mContext, ArrayList<Pet> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        WH = Utils.getDisplayMetrics((Activity) mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyPetFragmentAdapter.MyViewHolder viewHolder = new MyPetFragmentAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_myfragment_mypet, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Pet myPet = mDatas.get(position);
        GlideUtil.loadCircleImg(mContext, myPet.image, holder.img_pet_icon, 0);
        holder.textview_dognickname.setText(myPet.nickName + "");
        holder.textview_kindname.setText(myPet.name + "");
        if (!TextUtils.isEmpty(myPet.month)) {
            holder.layout_pet_age.setVisibility(View.VISIBLE);
            holder.textview_age.setText(myPet.month + "");
        } else {
            holder.textview_age.setText("生日信息待完善");
        }
        if (myPet.sex == 0) {
            holder.img_sex.setImageResource(R.drawable.pet_women);
            holder.layout_outside.setBackground(Utils.getDW("FFFCFC"));
            holder.layout_bottom_out.setBackgroundColor(mContext.getResources().getColor(R.color.aFFF1F1));
        } else if (myPet.sex == 1) {
            holder.img_sex.setImageResource(R.drawable.pet_man);
            holder.layout_outside.setBackground(Utils.getDW("F9FDFF"));
//            holder.layout_outside.setBackgroundColor(mContext.getResources().getColor(R.color.aF9FDFF));
            holder.layout_bottom_out.setBackgroundColor(mContext.getResources().getColor(R.color.aF3FBFF));
        }
        if (myPet.kindid == 2) {// 猫
            holder.ll_myfragment_mypet_certi.setVisibility(View.GONE);
        } else {
            if (myPet.certiDogSize == 1 || myPet.certiDogSize == 2) {// 小型犬
                String certiOrderStatus = myPet.certiOrderStatus;
                String statusName = "";
                holder.ll_myfragment_mypet_certi.setVisibility(View.VISIBLE);
                if (myPet.certiOrder != null) {
                    int status = myPet.certiOrder.status;
                    String[] statusNames = myPet.certiOrder.statusName;
                    if (statusNames != null && statusNames.length > 0) {
                        if (status < statusNames.length) {
                            statusName = statusNames[status];
                        }
                    } else {
                        statusName = certiOrderStatus;
                    }
                    Utils.setStringText(holder.textview_isCerti, statusName);
                } else {
                    holder.ll_myfragment_mypet_certi.setVisibility(View.GONE);
                }
            } else {// 大型犬
                holder.ll_myfragment_mypet_certi.setVisibility(View.GONE);
            }
        }
        holder.textview_content.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(myPet.petInfo)) {
            holder.textview_content.setVisibility(View.VISIBLE);
            holder.textview_content.setText(myPet.petInfo + "");
        }
        holder.textview_content_bottom.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(myPet.petActiveTitle)){
            holder.textview_content_bottom.setText(myPet.petActiveTitle);
        }
        holder.textview_right_go_look.setVisibility(View.GONE);
        if (myPet.petActiveStatus == 1){
            holder.textview_right_go_look.setVisibility(View.GONE);
            holder.textview_content_bottom.setVisibility(View.GONE);
        }else if (myPet.petActiveStatus == 0){
            if (!TextUtils.isEmpty(myPet.petActiveTitle)) {
                holder.textview_right_go_look.setVisibility(View.VISIBLE);
                holder.textview_content_bottom.setVisibility(View.VISIBLE);
            }else {
                holder.textview_right_go_look.setVisibility(View.GONE);
                holder.textview_content_bottom.setVisibility(View.GONE);
            }
        }
        holder.layout_pet_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ADActivity.class);
                intent.putExtra("url", myPet.petEncyclUrl);
                mContext.startActivity(intent);
            }
        });
        holder.textview_right_go_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(myPet.petActivePoint)) {
                    int point = Integer.parseInt(myPet.petActivePoint);
                    Utils.goService((Activity) mContext, point, myPet.petActiveBackup);
                }
            }
        });

        if (mClickLiner != null) {
            holder.layout_outside.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickLiner.click(view, position);
                }
            });
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WH[0]-Utils.dip2px(mContext,20),Utils.dip2px(mContext,137));
        holder.layout_outside.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout_outside;
        private ImageView img_pet_icon;
        private TextView textview_dognickname;
        private ImageView img_sex;
        private ImageView img_dog_cat;
        private TextView textview_kindname;
        private TextView textview_age;
        private TextView textview_isCerti;
        private LinearLayout layout_pet_right;
        private TextView textview_content;
        private TextView textview_right_go_look;
        private TextView textview_content_bottom;
        private LinearLayout layout_bottom_out;
        private LinearLayout layout_pet_age;
        private LinearLayout ll_myfragment_mypet_certi;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_outside = (LinearLayout) itemView.findViewById(R.id.layout_outside);
            img_pet_icon = (ImageView) itemView.findViewById(R.id.img_pet_icon);
            textview_dognickname = (TextView) itemView.findViewById(R.id.textview_dognickname);
            img_sex = (ImageView) itemView.findViewById(R.id.img_sex);
            img_dog_cat = (ImageView) itemView.findViewById(R.id.img_dog_cat);
            textview_kindname = (TextView) itemView.findViewById(R.id.textview_kindname);
            textview_age = (TextView) itemView.findViewById(R.id.textview_age);
            textview_isCerti = (TextView) itemView.findViewById(R.id.textview_isCerti);
            layout_pet_right = (LinearLayout) itemView.findViewById(R.id.layout_pet_right);
            textview_content = (TextView) itemView.findViewById(R.id.textview_content);
            textview_right_go_look = (TextView) itemView.findViewById(R.id.textview_right_go_look);
            textview_content_bottom = (TextView) itemView.findViewById(R.id.textview_content_bottom);
            layout_bottom_out = (LinearLayout) itemView.findViewById(R.id.layout_bottom_out);
            layout_pet_age = (LinearLayout) itemView.findViewById(R.id.layout_pet_age);
            ll_myfragment_mypet_certi = (LinearLayout) itemView.findViewById(R.id.ll_myfragment_mypet_certi);
        }
    }

    public interface OnItemClickRecyleView {
        void click(View v, int position);
    }

    public MyPetFragmentAdapter.OnItemClickRecyleView mClickLiner = null;

    public void setOnItemClickRecyleView(MyPetFragmentAdapter.OnItemClickRecyleView mClickLiner) {
        this.mClickLiner = mClickLiner;
    }
}
