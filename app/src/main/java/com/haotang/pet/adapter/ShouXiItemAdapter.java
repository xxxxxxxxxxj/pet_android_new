package com.haotang.pet.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/13 16:42
 */
public class ShouXiItemAdapter extends BaseQuickAdapter<ApointMentItem, BaseViewHolder> {

    public ShouXiItemAdapter(int layoutResId, List<ApointMentItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, ApointMentItem item) {
        TextView tv_item_shouxiitem_name = helper.getView(R.id.tv_item_shouxiitem_name);
        ImageView iv_item_shouxiitem_img = helper.getView(R.id.iv_item_shouxiitem_img);
        if (item != null) {
            GlideUtil.loadRoundImg(mContext, item.getPic(),
                    iv_item_shouxiitem_img,
                    R.drawable.icon_production_default, 10);
            List<Integer> petKindList = item.getPetKindList();
            if (petKindList != null) {
                Log.e("TAG","petKindList = "+petKindList.toString());
                if(petKindList.size() > 0){
                    if (petKindList.size() == 1) {
                        if (petKindList.get(0) == 1) {//狗
                            Utils.setText(tv_item_shouxiitem_name,
                                    item.getName() + "(狗)", item.getName(), View.VISIBLE, View.GONE);
                        } else if (petKindList.get(0) == 2) {//猫
                            Utils.setText(tv_item_shouxiitem_name,
                                    item.getName() + "(猫)", item.getName(), View.VISIBLE, View.GONE);
                        }
                    } else if (petKindList.size() == 2) {
                        Utils.setText(tv_item_shouxiitem_name,
                                item.getName() + "(狗,猫)", item.getName(), View.VISIBLE, View.GONE);
                    }
                } else {
                    Utils.setText(tv_item_shouxiitem_name,
                            item.getName(), "", View.VISIBLE, View.GONE);
                }
            } else {
                Utils.setText(tv_item_shouxiitem_name,
                        item.getName(), "", View.VISIBLE, View.GONE);
            }
        }
    }
}
