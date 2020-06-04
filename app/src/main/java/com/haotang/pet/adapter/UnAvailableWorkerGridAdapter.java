package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/9/5 14:04
 */
public class UnAvailableWorkerGridAdapter extends BaseQuickAdapter<AppointWorker, BaseViewHolder> {
    public OnWorkerSelectListener onWorkerSelectListener = null;

    public interface OnWorkerSelectListener {
        public void OnWorkerSelect(int position);
    }

    public void setOnWorkerSelectListener(
            OnWorkerSelectListener onWorkerSelectListener) {
        this.onWorkerSelectListener = onWorkerSelectListener;
    }

    public OnWorkerInfoListener onWorkerInfoListener = null;

    public interface OnWorkerInfoListener {
        public void OnWorkerInfo(int position);
    }

    public void setOnWorkerInfoListener(
            OnWorkerInfoListener onWorkerInfoListener) {
        this.onWorkerInfoListener = onWorkerInfoListener;
    }

    public UnAvailableWorkerGridAdapter(int layoutResId, List<AppointWorker> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AppointWorker item) {
        ImageView iv_item_workerlist_list_tag = helper.getView(R.id.iv_item_workerlist_list_tag);
        TextView tv_item_workerlist_list_sub = helper.getView(R.id.tv_item_workerlist_list_sub);
        TextView tv_item_workerlist_list_zztime = helper.getView(R.id.tv_item_workerlist_list_zztime);
        ImageView iv_item_workerlist_list_img = helper.getView(R.id.iv_item_workerlist_list_img);
        TextView tv_item_workerlist_list_workername = helper.getView(R.id.tv_item_workerlist_list_workername);
        if (item != null) {
            Utils.setText(tv_item_workerlist_list_zztime, item.getEarliest(), "", View.VISIBLE, View.GONE);
            if (Utils.isStrNull(item.getTag())) {
                iv_item_workerlist_list_tag.bringToFront();
                iv_item_workerlist_list_tag.setVisibility(View.VISIBLE);
                GlideUtil.loadImg(mContext, item.getTag(), iv_item_workerlist_list_tag, 0);
            } else {
                iv_item_workerlist_list_tag.setVisibility(View.GONE);
            }
            Utils.setText(tv_item_workerlist_list_workername, item.getRealName(), "", View.VISIBLE, View.VISIBLE);
            if (item.getTid() == 1) {
                tv_item_workerlist_list_sub.setText("预约中级");
            } else if (item.getTid() == 2) {
                tv_item_workerlist_list_sub.setText("预约高级");
            } else if (item.getTid() == 3) {
                tv_item_workerlist_list_sub.setText("预约首席");
            }
            tv_item_workerlist_list_sub.setBackgroundResource(R.drawable.bg_worker_shade_red);
            GlideUtil.loadCircleImg(mContext, item.getAvatar(), iv_item_workerlist_list_img, R.drawable.user_icon_unnet_circle);
            tv_item_workerlist_list_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWorkerSelectListener != null) {
                        onWorkerSelectListener.OnWorkerSelect(helper.getLayoutPosition());
                    }
                }
            });
            iv_item_workerlist_list_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWorkerInfoListener != null) {
                        onWorkerInfoListener.OnWorkerInfo(helper.getLayoutPosition());
                    }
                }
            });
        }
    }
}