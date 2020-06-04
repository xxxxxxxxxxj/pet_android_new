package com.haotang.pet.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.GiftCardDetail;
import com.haotang.pet.util.GlideUtil;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/3/25
 * @Description:服务列表的弹出的适配器
 */
public class GiftDetailServiceAdapter extends BaseQuickAdapter<GiftCardDetail.DataBean.CardTemplateDetailBean.TypeContentListBean,BaseViewHolder>{
    public GiftDetailServiceAdapter(@LayoutRes int layoutResId, @Nullable List<GiftCardDetail.DataBean.CardTemplateDetailBean.TypeContentListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftCardDetail.DataBean.CardTemplateDetailBean.TypeContentListBean item) {
        ImageView ivServiceIcon = helper.getView(R.id.iv_giftcard_service_item);
        TextView tvServiceName = helper.getView(R.id.tv_giftcard_service_name);
        TextView tvServiceContent = helper.getView(R.id.tv_giftcard_service_content);
        if (item!=null){
            GlideUtil.loadImg(mContext,item.getPic(),ivServiceIcon, R.drawable.icon_production_default);
            tvServiceName.setText(item.getTagText());
            tvServiceContent.setText(item.getTagContent());
        }
    }
}
