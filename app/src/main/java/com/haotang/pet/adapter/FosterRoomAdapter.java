package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterRoom;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/12/18
 * @Description:
 */
public class FosterRoomAdapter extends BaseQuickAdapter<FosterRoom, BaseViewHolder> {
    public FosterRoomAdapter(int layoutResId, List<FosterRoom> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterRoom item) {
        TextView tv_item_fosterroom_submit = helper.getView(R.id.tv_item_fosterroom_submit);
        TextView tv_fosterroom_tag = helper.getView(R.id.tv_fosterroom_tag);
        ImageView iv_fosterroom_img = helper.getView(R.id.iv_fosterroom_img);
        TextView tv_item_fosterroom_name = helper.getView(R.id.tv_item_fosterroom_name);
        TagFlowLayout tfl_item_fosterroom_tag = helper.getView(R.id.tfl_item_fosterroom_tag);
        TextView tv_item_fosterroom_price = helper.getView(R.id.tv_item_fosterroom_price);
        TextView tv_item_fosterroom_desc = helper.getView(R.id.tv_item_fosterroom_desc);
        RelativeLayout rl_item_fosterroom_root = helper.getView(R.id.rl_item_fosterroom_root);
        RelativeLayout rl_item_fosterroom_submit = helper.getView(R.id.rl_item_fosterroom_submit);
        if (item != null) {
            Utils.setText(tv_item_fosterroom_desc, item.getPriceDesc(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_fosterroom_price, "¥" + item.getPrice(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_fosterroom_tag, item.getDesc2(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_fosterroom_name, item.getName(), "", View.VISIBLE, View.VISIBLE);
            GlideUtil.loadImg(mContext, item.getImg1(), iv_fosterroom_img, R.drawable.icon_production_default);
            if (item.getIsFull() == 1) {
                tv_item_fosterroom_submit.setText("满房");
                rl_item_fosterroom_submit.setBackgroundResource(R.drawable.bg_grayround_order);
            } else {
                tv_item_fosterroom_submit.setText("预订");
                rl_item_fosterroom_submit.setBackgroundResource(R.drawable.bg_redround_order);
            }
            if (item.getLabels() != null && item.getLabels().size() > 0) {
                tfl_item_fosterroom_tag.setVisibility(View.VISIBLE);
                tfl_item_fosterroom_tag.setAdapter(new TagAdapter<String>(item.getLabels()) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        View view = (View) View.inflate(mContext, R.layout.item_foster_roomtag,
                                null);
                        TextView tv_item_foster_roomtag = (TextView) view.findViewById(R.id.tv_item_foster_roomtag);
                        tv_item_foster_roomtag.setText(s);
                        return view;
                    }
                });
            } else {
                tfl_item_fosterroom_tag.setVisibility(View.GONE);
            }
            helper.addOnClickListener(R.id.rl_item_fosterroom_root).addOnClickListener(R.id.rl_item_fosterroom_submit);
        }
    }
}
