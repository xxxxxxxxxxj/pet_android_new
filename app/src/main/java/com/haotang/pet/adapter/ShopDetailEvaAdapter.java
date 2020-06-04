package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NiceImageView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/2/12
 * @Description:
 */
public class ShopDetailEvaAdapter extends RecyclerView.Adapter<ShopDetailEvaAdapter.MyViewHolder> {
    private Context context;
    private List<Comment> list = new ArrayList<>();

    public void setEvalists(List<Comment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ShopDetailEvaAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ShopDetailEvaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_shopdetail_eva, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShopDetailEvaAdapter.MyViewHolder holder, final int position) {
        GlideUtil.loadImg(context, list.get(position).avatar, holder.nvHead, R.drawable.icon_production_default);
        holder.tvName.setText(list.get(position).userName);
        holder.tvTime.setText("服务时间:" + list.get(position).appointment);
        holder.tvServiceType.setText(list.get(position).serviceName);
        holder.tvCreate.setText(list.get(position).time);
        if (TextUtils.isEmpty(list.get(position).contents)) {
            holder.tvContent.setVisibility(View.GONE);
        } else {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(list.get(position).contents);
        }
        List<ImageView> imageEvaList = new ArrayList<>();
        imageEvaList.add(holder.img_eva_one);
        imageEvaList.add(holder.img_eva_two);
        imageEvaList.add(holder.img_eva_thr);
        imageEvaList.add(holder.img_eva_four);
        imageEvaList.add(holder.img_eva_five);
        for (int i = 0; i < 5; i++) {
            GlideUtil.loadImg(context, null, imageEvaList.get(i), R.drawable.star_empty);
        }
        for (int i = 0; i < list.get(position).grade; i++) {
            GlideUtil.loadImg(context, null, imageEvaList.get(i), R.drawable.star_full);
        }
        if (TextUtils.isEmpty(list.get(position).commentTags)) {
            holder.tlTags.setVisibility(View.GONE);
        } else {
            String[] split = list.get(position).commentTags.split(" ");
            List<String> tagList = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                tagList.add(split[i]);
            }
            holder.tlTags.setVisibility(View.VISIBLE);
            holder.tlTags.setAdapter(new TagAdapter<String>(tagList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    View view = (View) View.inflate(context, R.layout.item_eva_commontag,
                            null);
                    TextView tv_item_foster_roomtag = (TextView) view.findViewById(R.id.tv_eva_commontag);
                    tv_item_foster_roomtag.setText(s);
                    return view;
                }
            });
        }
        if (Utils.isStrNull(list.get(position).commentWorkerContent)){
            holder.llReply.setVisibility(View.VISIBLE);
            holder.tvReplyContent.setText(list.get(position).commentWorkerContent);
            GlideUtil.loadCircleImg(context,list.get(position).avatarBeauty,holder.ivReplyHead,R.drawable.icon_production_default);
            holder.tvReplyName.setText(list.get(position).nickname);
        }else {
            holder.llReply.setVisibility(View.GONE);
        }
        if (list.get(position).images != null) {
            if (list.get(position).images.length > 0) {
                holder.rv_eva_icons.setVisibility(View.VISIBLE);
                List<String> iconList = new ArrayList<>();
                for (int i = 0; i < list.get(position).images.length; i++) {
                    iconList.add(list.get(position).images[i]);
                }
                CommonEvaIconAdapter iconAdapter = new CommonEvaIconAdapter(context);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
                holder.rv_eva_icons.setLayoutManager(layoutManager);
                holder.rv_eva_icons.setAdapter(iconAdapter);
                iconAdapter.setList(iconList);
            } else {
                holder.rv_eva_icons.setVisibility(View.GONE);
            }
        } else {
            holder.rv_eva_icons.setVisibility(View.GONE);
        }
        if (list.get(position).itemList != null && list.get(position).itemList.size() > 0) {
            holder.llExtraitem.setVisibility(View.VISIBLE);
            List<String> tagList = new ArrayList<>();
            tagList.addAll(list.get(position).itemList);
            holder.tlItems.setAdapter(new TagAdapter<String>(tagList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    View view = (View) View.inflate(context, R.layout.item_shopeva_itemtag,
                            null);
                    TextView tv_item_foster_roomtag = (TextView) view.findViewById(R.id.tv_item_shopeva_itemtag);
                    tv_item_foster_roomtag.setText(s);
                    return view;
                }
            });
        } else {
            holder.llExtraitem.setVisibility(View.GONE);
        }
        holder.llExtraitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tlItems.isShown()) {
                    holder.ivItem.setImageResource(R.drawable.icon_down_gray);
                    holder.tlItems.setVisibility(View.GONE);
                } else {
                    holder.tlItems.setVisibility(View.VISIBLE);
                    holder.ivItem.setImageResource(R.drawable.icon_gray_up);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NiceImageView nvHead;
        private TextView tvName;
        private TextView tvContent;
        private RecyclerView rvIcons;
        private TagFlowLayout tlTags;
        private TextView tvServiceType;
        private TextView tvTime;
        private ImageView img_eva_one;
        private ImageView img_eva_two;
        private ImageView img_eva_thr;
        private ImageView img_eva_four;
        private ImageView img_eva_five;
        private RecyclerView rv_eva_icons;
        private TextView tvCreate;
        private LinearLayout llExtraitem;
        private TagFlowLayout tlItems;
        private ImageView ivItem;
        private LinearLayout llReply;
        private ImageView ivReplyHead;
        private TextView tvReplyName;
        private TextView tvReplyContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            nvHead = itemView.findViewById(R.id.nv_shopeva_head);
            tvName = itemView.findViewById(R.id.tv_shopeva_name);
            tvContent = itemView.findViewById(R.id.tv_shopeva_content);
            tvServiceType = itemView.findViewById(R.id.tv_shopeva_type);
            tvTime = itemView.findViewById(R.id.tv_shopeva_time);
            tlTags = itemView.findViewById(R.id.tfl_shopeva_tag);
            img_eva_one = itemView.findViewById(R.id.img_eva_one);
            img_eva_two = itemView.findViewById(R.id.img_eva_two);
            img_eva_thr = itemView.findViewById(R.id.img_eva_thr);
            img_eva_four = itemView.findViewById(R.id.img_eva_four);
            img_eva_five = itemView.findViewById(R.id.img_eva_five);
            rv_eva_icons = itemView.findViewById(R.id.rv_shopdetal_icons);
            tvCreate = itemView.findViewById(R.id.tv_shopeva_create);
            llExtraitem = itemView.findViewById(R.id.ll_shopeva_extraitem);
            tlItems = itemView.findViewById(R.id.tfl_shopeva_itemtag);
            ivItem = itemView.findViewById(R.id.iv_shopeva_item);
            llReply = itemView.findViewById(R.id.ll_eva_reply);
            ivReplyHead = ivItem.findViewById(R.id.iv_shopeva_replyhead);
            tvReplyName = itemView.findViewById(R.id.tv_shopeva_replyname);
            tvReplyContent = itemView.findViewById(R.id.tv_eva_replycontent);
        }
    }
}
