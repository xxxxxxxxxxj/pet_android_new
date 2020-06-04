package com.haotang.pet.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.RechargeBanner;
import com.haotang.pet.util.GlideUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/20 18:33
 */
public class AppointMentBannerAdapter extends BaseQuickAdapter<RechargeBanner, BaseViewHolder> {
    public AppointMentBannerAdapter(int layoutResId, List<RechargeBanner> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final RechargeBanner item) {
        ImageView iv_item_appointment_banner = helper.getView(R.id.iv_item_appointment_banner);
        if (item != null) {
            GlideUtil.loadImg(mContext, item.imgUrl, iv_item_appointment_banner, R.drawable.icon_production_default);
        }
    }
}