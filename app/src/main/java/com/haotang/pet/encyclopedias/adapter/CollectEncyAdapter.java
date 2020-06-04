package com.haotang.pet.encyclopedias.adapter;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.activity.EncyclopediasDetail;
import com.haotang.pet.encyclopedias.bean.CollectEncyclopedias;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/2 14:24
 */
public class CollectEncyAdapter extends BaseQuickAdapter<CollectEncyclopedias, BaseViewHolder> {
    private boolean isVisible;

    public boolean isVisible() {
        return isVisible;
    }

    //长按
    private ItemLongClickListener longListener;
    public void setLongClickListener(ItemLongClickListener longListener) {
        this.longListener = longListener;
    }
    public interface ItemLongClickListener{
        void onItemLongClick(int position);
    }

    //删除
    private DelClickListener delListener;
    public void setDelListener(DelClickListener delListener) {
        this.delListener = delListener;
    }
    public interface DelClickListener{
        void onItemDelClick(int position);
    }
    //点击消失
    private GoneDelClickListener goneDelListener;
    public void setGoneDelListener(GoneDelClickListener goneDelListener) {
        this.goneDelListener = goneDelListener;
    }
    public interface GoneDelClickListener{
        void onItemGoneDelClick(int position);
    }

    public CollectEncyAdapter(int layoutResId, List<CollectEncyclopedias> data) {
        super(layoutResId, data);
    }

    public void setSelectVis(boolean isVisible) {
        this.isVisible = isVisible;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CollectEncyclopedias item) {
        LinearLayout ll_item_collect_ency_select = helper.getView(R.id.ll_item_collect_ency_select);
        RelativeLayout ll_item_collect_ency_root = helper.getView(R.id.ll_item_collect_ency_root);
        LinearLayout ll_item_collect_ency = helper.getView(R.id.ll_item_collect_ency);
        ImageView iv_item_mycollect_del = helper.getView(R.id.iv_item_mycollect_del);
        RelativeLayout rl_item_mycollect_del = helper.getView(R.id.rl_item_mycollect_del);
        ImageView iv_item_collect_ency_select = helper.getView(R.id.iv_item_collect_ency_select);
        ImageView iv_item_collect_ency = helper.getView(R.id.iv_item_collect_ency);
        TextView tv_item_collect_ency_time = helper.getView(R.id.tv_item_collect_ency_time);
        ImageView iv_item_collect_ency_userimg = helper.getView(R.id.iv_item_collect_ency_userimg);
        TextView tv_item_collect_ency_username = helper.getView(R.id.tv_item_collect_ency_username);
        TextView tv_item_collect_ency_txt = helper.getView(R.id.tv_item_collect_ency_txt);
        if (helper.getLayoutPosition() == 0) {
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) ll_item_collect_ency_root.getLayoutParams();
            layoutParams.topMargin = DensityUtil.dp2px(mContext, 15);
            ll_item_collect_ency_root.setLayoutParams(layoutParams);
        }
        if (item != null) {
            if (isVisible) {
                ll_item_collect_ency_select.setVisibility(View.VISIBLE);
                if (item.isSelect()) {
                    iv_item_collect_ency_select.setImageResource(R.drawable.complaint_reason);
                } else {
                    iv_item_collect_ency_select.setImageResource(R.drawable.complaint_reason_disable);
                }
            } else {
                ll_item_collect_ency_select.setVisibility(View.GONE);
            }
            if (item.isShowDel){
                rl_item_mycollect_del.setVisibility(View.VISIBLE);
            }else {
                rl_item_mycollect_del.setVisibility(View.GONE);
            }
            Utils.setText(tv_item_collect_ency_time, item.getReleaseTime(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_collect_ency_username, item.getSource(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_collect_ency_txt, item.getTitle(), "", View.VISIBLE, View.VISIBLE);
            GlideUtil.loadImg(mContext, item.getCollectionCover(), iv_item_collect_ency, R.drawable.icon_production_default);
            GlideUtil.loadCircleImg(mContext, item.getSourceIcon(), iv_item_collect_ency_userimg, R.drawable.user_icon_unnet_circle);
            ll_item_collect_ency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, EncyclopediasDetail.class).putExtra("infoId", item.getId()));
                }
            });
            ll_item_collect_ency.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longListener.onItemLongClick(helper.getLayoutPosition());
                    return false;
                }
            });
            rl_item_mycollect_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goneDelListener.onItemGoneDelClick(helper.getLayoutPosition());
                }
            });
            iv_item_mycollect_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delListener.onItemDelClick(helper.getLayoutPosition());
                }
            });
        }
    }
}
