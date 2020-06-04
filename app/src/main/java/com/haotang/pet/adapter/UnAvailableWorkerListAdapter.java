package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.AppointWorker;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/23 14:46
 */
public class UnAvailableWorkerListAdapter extends BaseQuickAdapter<AppointWorker, BaseViewHolder> {
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

    public UnAvailableWorkerListAdapter(int layoutResId, List<AppointWorker> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AppointWorker item) {
        ImageView iv_item_workerlist_list_tag = helper.getView(R.id.iv_item_workerlist_list_tag);
        TextView tv_item_workerlist_list_sub = helper.getView(R.id.tv_item_workerlist_list_sub);
        TextView tv_item_workerlist_list_zztime = helper.getView(R.id.tv_item_workerlist_list_zztime);
        ImageView iv_item_workerlist_list_img = helper.getView(R.id.iv_item_workerlist_list_img);
        LinearLayout ll_item_workerlist_list_worker = helper.getView(R.id.ll_item_workerlist_list_worker);
        TextView tv_item_workerlist_list_workername = helper.getView(R.id.tv_item_workerlist_list_workername);
        TextView tv_item_workerlist_list_hpl = helper.getView(R.id.tv_item_workerlist_list_hpl);
        TextView tv_item_workerlist_list_num = helper.getView(R.id.tv_item_workerlist_list_num);
        final TagFlowLayout tfl_item_workerlist_listbq = helper.getView(R.id.tfl_item_workerlist_listbq);
        LinearLayout ll_item_workerlist_list_tuijian = helper.getView(R.id.ll_item_workerlist_list_tuijian);
        LinearLayout ll_item_workerlist_list_info = helper.getView(R.id.ll_item_workerlist_list_info);
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
            Utils.setText(tv_item_workerlist_list_hpl, "好评率：" + item.getGoodRate(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_workerlist_list_num, "服务" + item.getOrderTotal() + "单", "", View.VISIBLE, View.VISIBLE);
            ll_item_workerlist_list_worker.setVisibility(View.VISIBLE);
            ll_item_workerlist_list_tuijian.setVisibility(View.GONE);
            if (item.getTid() == 1) {
                tv_item_workerlist_list_sub.setText("预约中级");
            } else if (item.getTid() == 2) {
                tv_item_workerlist_list_sub.setText("预约高级");
            } else if (item.getTid() == 3) {
                tv_item_workerlist_list_sub.setText("预约首席");
            }
            tv_item_workerlist_list_sub.setBackgroundResource(R.drawable.bg_worker_shade_red);
            if (item.getExpertiseLiest() != null && item.getExpertiseLiest().size() > 0) {
                tfl_item_workerlist_listbq.setVisibility(View.VISIBLE);
                tfl_item_workerlist_listbq.setAdapter(new TagAdapter<String>(item.getExpertiseLiest()) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        View view = (View) View.inflate(mContext,R.layout.item_workerlist_bq,
                                null);
                        TextView tv_item_workerlist_bq = (TextView) view.findViewById(R.id.tv_item_workerlist_bq);
                        tv_item_workerlist_bq.setText(s);
                        return view;
                    }
                });
            } else {
                tfl_item_workerlist_listbq.setVisibility(View.GONE);
            }
            GlideUtil.loadCircleImg(mContext, item.getAvatar(), iv_item_workerlist_list_img, R.drawable.user_icon_unnet_circle);
            tv_item_workerlist_list_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWorkerSelectListener != null) {
                        onWorkerSelectListener.OnWorkerSelect(helper.getLayoutPosition());
                    }
                }
            });
            ll_item_workerlist_list_info.setOnClickListener(new View.OnClickListener() {
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