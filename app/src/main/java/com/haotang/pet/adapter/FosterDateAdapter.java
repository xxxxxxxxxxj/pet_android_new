package com.haotang.pet.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterDate;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NoScollFullGridLayoutManager;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-24 13:45
 */
public class FosterDateAdapter extends BaseQuickAdapter<FosterDate, BaseViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public FosterDateAdapter(int layoutResId, List<FosterDate> data) {
        super(layoutResId, data);
    }

    private onDateSelectListener listener;

    public interface onDateSelectListener {
        void onSelect(int dayPosition, int monthPosition);
    }

    public void setListener(onDateSelectListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterDate item) {
        RecyclerView rv_item_fosterdate_day = helper.getView(R.id.rv_item_fosterdate_day);
        if (item != null) {
            if (item.getDays() != null && item.getDays().size() > 0) {
                rv_item_fosterdate_day.setHasFixedSize(true);
                rv_item_fosterdate_day.setNestedScrollingEnabled(false);
                NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                        NoScollFullGridLayoutManager(rv_item_fosterdate_day, mContext, 7, GridLayoutManager.VERTICAL, false);
                noScollFullGridLayoutManager.setScrollEnabled(false);
                rv_item_fosterdate_day.setLayoutManager(noScollFullGridLayoutManager);
                FosterDayAdapter fosterDayAdapter = new FosterDayAdapter(R.layout.item_fosterday, item.getDays());
                rv_item_fosterdate_day.setAdapter(fosterDayAdapter);
                fosterDayAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (listener != null) {
                            listener.onSelect(position, helper.getLayoutPosition());
                        }
                    }
                });
            }
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_headview_fosterdate, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        if (mData != null && mData.size() > 0) {
            FosterDate fosterDate = mData.get(position);
            if (fosterDate != null) {
                textView.setVisibility(View.VISIBLE);
                Utils.setText(textView, fosterDate.getMonth(), "", View.VISIBLE, View.GONE);
            } else {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}