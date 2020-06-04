package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NiceImageView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 姜谷蓄
 * @date2020-04-8
 */
public class SerchBeauResultAdapter extends BaseQuickAdapter<Beautician, BaseViewHolder> {

    public SerchBeauResultAdapter(int layoutResId, List<Beautician> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Beautician item) {
        TextView tv_item_serchbeau_name = helper.getView(R.id.tv_searchbeauty_name);
        NiceImageView iv_item_serchbeau_img = helper.getView(R.id.nv_searchbeauty_head);
        ImageView iv_item_serchbeau_jb = helper.getView(R.id.iv_item_serchbeau_jb);
        TextView tv_item_ordernum = helper.getView(R.id.tv_searchbeauty_order);
        TextView tv_item_good = helper.getView(R.id.tv_searchbeauty_good);
        if (item != null) {
            GlideUtil.loadImg(mContext, item.image, iv_item_serchbeau_img, R.drawable.icon_production_default);
            Utils.setText(tv_item_serchbeau_name, item.name, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_ordernum, "服务 "+item.ordernum+"单", "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_good, "好评率 "+item.goodRate, "", View.VISIBLE, View.VISIBLE);
            if (item.tid == 1) {
                iv_item_serchbeau_jb.setImageResource(R.drawable.icon_serchbeau_level1);
            } else if (item.tid == 2) {
                iv_item_serchbeau_jb.setImageResource(R.drawable.icon_serchbeau_level2);
            } else if (item.tid == 3) {
                iv_item_serchbeau_jb.setImageResource(R.drawable.icon_serchbeau_level3);
            }
            helper.addOnClickListener(R.id.iv_item_serchbeau_yy).addOnClickListener(R.id.nv_searchbeauty_head);
        }
    }
}
