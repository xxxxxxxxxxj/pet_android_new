package com.haotang.pet.adapter.MallAdapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.mallEntity.NavigationCondition;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class MallSearchResultRecycleViewAdapter extends  RecyclerView.Adapter<MallSearchResultRecycleViewAdapter.MyViewHolder>{
    public Context mContext;
    public List<NavigationCondition> mDatas;
    public MallSearchResultRecycleViewAdapter(Context mContext,List<NavigationCondition> mDatas){
        this.mContext = mContext;
        this.mDatas = mDatas;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mallsearch,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
       NavigationCondition navigationCondition =  mDatas.get(position);
        holder.textview_name.setText(navigationCondition.NavigationName);
        holder.icon_img_show.setVisibility(View.VISIBLE);
        if (navigationCondition.isOpen==0){
            holder.icon_img_show.setBackgroundResource(R.drawable.down_mall_arrow);
            holder.malloutside_back.setBackgroundResource(0);
            holder.view_bottom_line.setVisibility(View.GONE);
            holder.layout_mallsearch.setBackgroundResource(R.drawable.bg_button_mall_back_gray);
            if (TextUtils.isEmpty(navigationCondition.bottonChooseStr)){
                holder.textview_name.setText(navigationCondition.NavigationName);
                holder.layout_mallsearch.setBackgroundResource(R.drawable.bg_button_mall_back_gray);
                holder.icon_img_show.setVisibility(View.VISIBLE);
            }else {
                holder.textview_name.setText(navigationCondition.bottonChooseStr);
                holder.layout_mallsearch.setBackgroundResource(R.drawable.bg_button_appoiment_ok);
                holder.icon_img_show.setVisibility(View.GONE);
            }
        }else if (navigationCondition.isOpen==1){
            if (TextUtils.isEmpty(navigationCondition.bottonChooseStr)){
                holder.textview_name.setText(navigationCondition.NavigationName);
                holder.layout_mallsearch.setBackgroundResource(R.drawable.bg_button_mall_back_gray);
                holder.icon_img_show.setVisibility(View.VISIBLE);
            }else {
                holder.textview_name.setText(navigationCondition.bottonChooseStr);
                holder.layout_mallsearch.setBackgroundResource(R.drawable.bg_button_appoiment_ok);
                holder.icon_img_show.setVisibility(View.GONE);
            }

            holder.icon_img_show.setBackgroundResource(R.drawable.up_mall_arrow);
            holder.malloutside_back.setBackgroundResource(R.drawable.mall_nav_back);
            holder.view_bottom_line.setVisibility(View.GONE);
            holder.layout_mallsearch.setBackgroundResource(0);
        }
        if (mClickLiner!=null){
            holder.malloutside_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickLiner.click(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout malloutside_back;
        private RelativeLayout layout_mallsearch;
        private TextView textview_name;
        private ImageView icon_img_show;
        private View view_bottom_line;
        public MyViewHolder(View itemView) {
            super(itemView);
            malloutside_back = (RelativeLayout) itemView.findViewById(R.id.malloutside_back);
            layout_mallsearch = (RelativeLayout) itemView.findViewById(R.id.layout_mallsearch);
            textview_name = (TextView) itemView.findViewById(R.id.textview_name);
            icon_img_show = (ImageView) itemView.findViewById(R.id.icon_img_show);
            view_bottom_line = (View) itemView.findViewById(R.id.view_bottom_line);
        }
    }

    public interface OnItemClickRecyleView{
        void click(View v,int position);
    }
    public OnItemClickRecyleView mClickLiner = null;
    public void setOnItemClickRecyleView(OnItemClickRecyleView mClickLiner){
        this.mClickLiner = mClickLiner;
    }
}
