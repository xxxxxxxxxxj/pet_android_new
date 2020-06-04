package com.haotang.pet.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.MyBill;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/23 17:22
 */
public class MyBillAdapter extends BaseQuickAdapter<MyBill, BaseViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private int type;

    public MyBillAdapter(int layoutResId, List<MyBill> data, int type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyBill item) {
        ImageView iv_item_mybill_icon = helper.getView(R.id.iv_item_mybill_icon);
        TextView tv_item_mybill_amount = helper.getView(R.id.tv_item_mybill_amount);
        TextView tv_item_mybill_name = helper.getView(R.id.tv_item_mybill_name);
        TextView tv_item_mybill_time = helper.getView(R.id.tv_item_mybill_time);
        TextView tv_item_mybill_desc = helper.getView(R.id.tv_item_mybill_desc);
        if (item != null) {
            Utils.setText(tv_item_mybill_amount, item.getAmount(), "", View.VISIBLE, View.VISIBLE);
            if (type == 1) {
                iv_item_mybill_icon.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_mybill_desc, item.getState(), "", View.VISIBLE, View.INVISIBLE);
            } else if (type == 2) {
                Utils.setText(tv_item_mybill_desc, item.getCost(), "", View.VISIBLE, View.INVISIBLE);
                iv_item_mybill_icon.setVisibility(View.GONE);
            }
            Utils.setText(tv_item_mybill_name, item.getItem(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_mybill_time, item.getTradeDate(), "", View.VISIBLE, View.VISIBLE);
            GlideUtil.loadCircleImg(mContext, item.getIcon(), iv_item_mybill_icon, R.drawable.user_icon_unnet_circle);
        }
    }

    @Override
    public long getHeaderId(int position) {
        long headerId = 0;
        if (mData != null && mData.size() > 0 && getItem(position) != null) {
            headerId = getItem(position).getHeaderId();
        }
        return headerId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_headview, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        if (mData != null && mData.size() > 0) {
            MyBill myBill = mData.get(position);
            if (myBill != null) {
                textView.setVisibility(View.VISIBLE);
                Utils.setText(textView, myBill.getGroupTime(), "", View.VISIBLE, View.GONE);
            }else{
                textView.setVisibility(View.GONE);
            }
        }else{
            textView.setVisibility(View.GONE);
        }
    }
}
