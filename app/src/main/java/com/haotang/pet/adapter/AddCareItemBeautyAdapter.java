package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterAddCareItem;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/12/19
 * @Description:添加单项选择美容师
 */
public class AddCareItemBeautyAdapter extends BaseQuickAdapter<FosterAddCareItem.DataBean.DatasetBean.ServicesBean.WorkersBean, BaseViewHolder> {
    private String noWorkerTip;

    public AddCareItemBeautyAdapter(int layoutResId, List<FosterAddCareItem.DataBean.DatasetBean.ServicesBean.WorkersBean> data, String noWorkerTip) {
        super(layoutResId, data);
        this.noWorkerTip = noWorkerTip;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterAddCareItem.DataBean.DatasetBean.ServicesBean.WorkersBean item) {
        ImageView iv_addcare_beauty = helper.getView(R.id.iv_addcare_beauty);
        ImageView iv_addcare_beautyno = helper.getView(R.id.iv_addcare_beautyno);
        ImageView iv_item_addcarebeauty_select = helper.getView(R.id.iv_item_addcarebeauty_select);
        TextView tv_addccare_beautyname = helper.getView(R.id.tv_addccare_beautyname);
        TextView tv_addcare_beautyeprice = helper.getView(R.id.tv_addcare_beautyeprice);
        TextView tv_addcare_beautyno = helper.getView(R.id.tv_addcare_beautyno);
        TextView tv_addcare_beautyprice = helper.getView(R.id.tv_addcare_beautyprice);
        if (item != null) {
            Utils.setText(tv_addccare_beautyname, item.getName(), "", View.VISIBLE, View.GONE);
            GlideUtil.loadCircleImg(mContext, item.getImg(), iv_addcare_beauty, R.drawable.icon_addcareitem_beauty);
            if (item.getPrice() > 0) {
                tv_addcare_beautyprice.setVisibility(View.VISIBLE);
                tv_addcare_beautyprice.setText("¥" + item.getPrice());
            } else {
                tv_addcare_beautyprice.setVisibility(View.GONE);
            }
            if (item.getEPrice() > 0) {
                tv_addcare_beautyeprice.setVisibility(View.VISIBLE);
                tv_addcare_beautyeprice.setText("E卡：¥" + item.getEPrice());
            } else {
                tv_addcare_beautyeprice.setVisibility(View.GONE);
            }
            if (item.isAvail()) {
                iv_addcare_beautyno.setVisibility(View.GONE);
                tv_addccare_beautyname.setTextColor(mContext.getResources().getColor(R.color.a333333));
                tv_addcare_beautyprice.setTextColor(mContext.getResources().getColor(R.color.a333333));
                tv_addcare_beautyeprice.setTextColor(mContext.getResources().getColor(R.color.aff3a1e));
                tv_addcare_beautyno.setVisibility(View.GONE);
            } else {
                iv_addcare_beautyno.setVisibility(View.VISIBLE);
                iv_addcare_beautyno.bringToFront();
                tv_addccare_beautyname.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                tv_addcare_beautyprice.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                tv_addcare_beautyeprice.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                tv_addcare_beautyno.setVisibility(View.VISIBLE);
                Utils.setText(tv_addcare_beautyno, noWorkerTip, "", View.VISIBLE, View.GONE);
            }
            if (item.isSelect()) {
                iv_item_addcarebeauty_select.setImageResource(R.drawable.icon_petadd_select);
            } else {
                iv_item_addcarebeauty_select.setImageResource(R.drawable.icon_petadd_unselect);
            }
        }
    }
}
