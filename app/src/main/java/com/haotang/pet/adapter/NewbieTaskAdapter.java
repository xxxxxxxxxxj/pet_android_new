package com.haotang.pet.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.NewbieTaskBean;
import com.haotang.pet.util.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/6/22 11:41
 */
public class NewbieTaskAdapter extends BaseQuickAdapter<NewbieTaskBean, BaseViewHolder> {

    public NewbieTaskAdapter(int layoutResId, List<NewbieTaskBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final NewbieTaskBean item) {
        ImageView iv_item_newbietask = helper.getView(R.id.iv_item_newbietask);
        Button btn_item_newbietask = helper.getView(R.id.btn_item_newbietask);
        if (item != null) {
            if (item.getTaskStatus() == 0) {
                btn_item_newbietask.setText("去完成");
                btn_item_newbietask.setTextColor(mContext.getResources().getColor(R.color.white));
                btn_item_newbietask.setBackgroundResource(R.drawable.bg_button_orange);
            } else if (item.getTaskStatus() == 1) {
                btn_item_newbietask.setText("领取");
                btn_item_newbietask.setTextColor(mContext.getResources().getColor(R.color.white));
                btn_item_newbietask.setBackgroundResource(R.drawable.bg_button_orange);
            } else if (item.getTaskStatus() == 2) {
                btn_item_newbietask.setText("已领取");
                btn_item_newbietask.setTextColor(mContext.getResources().getColor(R.color.a999999));
                btn_item_newbietask.setBackgroundResource(R.drawable.bg_button_eeeeee);
            }
            GlideUtil.loadImg(mContext, item.getPic(), iv_item_newbietask, R.drawable.icon_production_default);
            btn_item_newbietask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(item);
                }
            });
        }
    }
}

