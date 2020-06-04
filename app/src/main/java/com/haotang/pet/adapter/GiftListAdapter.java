package com.haotang.pet.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.GiftCardList;
import com.haotang.pet.util.GlideUtil;

import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * @author:姜谷蓄
 * @Date:2019/3/21
 * @Description:E卡列表的适配器
 */
public class GiftListAdapter extends BaseQuickAdapter<GiftCardList.DataBean.ServiceCardTemplateListBean.TemplatesBean, BaseViewHolder> {
    public GiftListAdapter(@LayoutRes int layoutResId, @Nullable List<GiftCardList.DataBean.ServiceCardTemplateListBean.TemplatesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, GiftCardList.DataBean.ServiceCardTemplateListBean.TemplatesBean item) {
        ImageView iv_giftcard_list = helper.getView(R.id.iv_item_giftcard_list);
        TextView tv_giftcard_sellout = helper.getView(R.id.tv_item_giftcard_sellout);
        CountdownView cv_giftcard_tiptime = helper.getView(R.id.cv_giftcard_item_time);
        TextView tv_giftcard_tip = helper.getView(R.id.tv_item_giftcard_tip);
        View v_head = helper.getView(R.id.v_item_giftcard_head);
        View v_middle = helper.getView(R.id.v_item_giftcard_middle);
        View v_bottom = helper.getView(R.id.v_item_giftcard_bottom);
        helper.getLayoutPosition();
        if (item != null) {
            GlideUtil.loadImg(mContext, item.getListPic(), iv_giftcard_list, R.drawable.icon_production_default);
            if (helper.getLayoutPosition()==0){
                v_head.setVisibility(View.VISIBLE);
            }else {
                v_head.setVisibility(View.GONE);
            }
            if (helper.getLayoutPosition()-1==mData.size()){
                v_middle.setVisibility(View.GONE);
                v_bottom.setVisibility(View.VISIBLE);
            }
            //距离开始的情况
            if (item.getIsShowTime() == 0&&item.getSaleTimeStart()>0) {
                tv_giftcard_sellout.setVisibility(View.GONE);
                tv_giftcard_tip.setVisibility(View.VISIBLE);
                tv_giftcard_tip.setText(item.getShowTimeStartDesc());
                cv_giftcard_tiptime.setVisibility(View.VISIBLE);
                tv_giftcard_tip.setText(item.getIsAllSale());
                cv_giftcard_tiptime.updateShow(Long.valueOf(item.getSaleTimeStart()).longValue());
                cv_giftcard_tiptime.start(Long.valueOf(item.getSaleTimeStart()).longValue());
            }else if (item.getIsShowTime() == 0&&item.getSaleTimeStart()==0){//距离结束的情况
                tv_giftcard_sellout.setVisibility(View.GONE);
                tv_giftcard_tip.setVisibility(View.VISIBLE);
                tv_giftcard_tip.setText(item.getShowTimeEndDesc());
                cv_giftcard_tiptime.setVisibility(View.VISIBLE);
                tv_giftcard_tip.setText(item.getIsAllSale());
                cv_giftcard_tiptime.updateShow(Long.valueOf(item.getSaleTimeEnd()).longValue());
                cv_giftcard_tiptime.start(Long.valueOf(item.getSaleTimeEnd()).longValue());
            } else {
                tv_giftcard_sellout.setVisibility(View.VISIBLE);
                tv_giftcard_tip.setVisibility(View.GONE);
                cv_giftcard_tiptime.setVisibility(View.GONE);
                tv_giftcard_sellout.setText(item.getIsAllSale());
            }
        }
        cv_giftcard_tiptime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                listener.onTimeEnd(helper.getLayoutPosition());
            }
        });
    }

    //声明接口
    private TimeEndListener listener;
    //set方法
    public void setListener(TimeEndListener listener) {
        this.listener = listener;
    }
    //定义接口
    public interface TimeEndListener{
        //实现点击的方法，传递条目下标
        void onTimeEnd(int position);
    }

}
