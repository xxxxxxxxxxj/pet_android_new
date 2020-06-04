package com.haotang.pet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.activity.EncyclopediasDetail;
import com.haotang.pet.encyclopedias.bean.Encyclopedias;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/19
 * @Description:
 */
public class MainFragPetWikiAdapter extends RecyclerView.Adapter<MainFragPetWikiAdapter.MyViewHolder> {

    private Context context;
    private List<Encyclopedias> list = new ArrayList<Encyclopedias>();

    public MainFragPetWikiAdapter(Context context) {
        this.context = context;
    }

    public void setListPetEncyclopedia(List<Encyclopedias> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MainFragPetWikiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_mainfrag_petwiki,null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MainFragPetWikiAdapter.MyViewHolder holder, final int position) {
        GlideUtil.loadImg(context,list.get(position).listCover,holder.nvIcon,R.drawable.icon_production_default);
        GlideUtil.loadImg(context,list.get(position).sourceIcon,holder.ivHead,R.drawable.icon_production_default);
        GlideUtil.loadRoundImg(context,list.get(position).sourceIcon,holder.ivHead,R.drawable.icon_production_default,9);
        holder.tvContent.setText(list.get(position).title);
        holder.tvAuthor.setText(list.get(position).source);
        holder.tvData.setText(list.get(position).releaseTime);
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengStatistics
                        .UmengEventStatistics(
                                context,
                                Global.UmengEventID.click_HomePage_Encyclopedia);
                context.startActivity(new Intent(context, EncyclopediasDetail.class).putExtra("infoId", list.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private NiceImageView nvIcon;
        private ImageView ivHead;
        private TextView tvContent;
        private RelativeLayout rlRoot;
        private TextView tvAuthor;
        private TextView tvData;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlRoot = itemView.findViewById(R.id.rl_mainfrag_wikiroot);
            nvIcon = itemView.findViewById(R.id.nv_mainfrag_wikiicon);
            tvContent = itemView.findViewById(R.id.tv_mainfrag_wikidesc);
            tvAuthor = itemView.findViewById(R.id.tv_mainfrag_wikiname);
            tvData = itemView.findViewById(R.id.tv_mainfrag_wikidata);
            ivHead = itemView.findViewById(R.id.iv_mainfrag_wikihead);
        }
    }
}
