package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.WorkerDifference;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/24 11:03
 */
public class WorkerDifferenceAdapter extends BaseQuickAdapter<WorkerDifference, BaseViewHolder> {
    public WorkerDifferenceAdapter(int layoutResId, List<WorkerDifference> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final WorkerDifference item) {
        TextView tv_item_workerdiff_name = helper.getView(R.id.tv_item_workerdiff_name);
        TextView tv_item_workerdiff_desc = helper.getView(R.id.tv_item_workerdiff_desc);
        ImageView img_diff_icon = helper.getView(R.id.img_diff_icon);
        View vw_item_workerdiff = helper.getView(R.id.vw_item_workerdiff);
        if (item != null) {
            if (helper.getLayoutPosition() == mData.size() - 1) {
                vw_item_workerdiff.setVisibility(View.GONE);
            } else {
                vw_item_workerdiff.setVisibility(View.VISIBLE);
            }
            Utils.setText(tv_item_workerdiff_name, item.getFirst(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_workerdiff_desc, item.getDesc(), "", View.VISIBLE, View.VISIBLE);
            GlideUtil.loadCircleImg(mContext,item.getImg(),img_diff_icon,R.drawable.icon_default);
        }
    }
}
