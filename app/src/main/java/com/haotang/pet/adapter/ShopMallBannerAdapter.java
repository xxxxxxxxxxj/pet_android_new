package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.view.rollviewpager.adapter.DynamicPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/30 11:23
 */
public class ShopMallBannerAdapter extends DynamicPagerAdapter {
    private List<Banner> list;
    private Activity mActivity;
    public OnBannerItemClickListener onBannerItemClickListener = null;

    public interface OnBannerItemClickListener {
        public void OnBannerItemClick(int position);
    }

    public void setOnItemClickListener(
            OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    public ShopMallBannerAdapter(Activity mActivity, List<Banner> list) {
        super();
        this.mActivity = mActivity;
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        final ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        final Banner banner = list.get(position);
        if (banner != null) {
            GlideUtil.loadImg(container.getContext(), banner.picDomain, view,
                    R.drawable.icon_production_default);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (onBannerItemClickListener != null) {
                        onBannerItemClickListener.OnBannerItemClick(position);
                    }
                }
            });
        }
        return view;
    }

    public void clearDeviceList() {
        if (list != null) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public void setData(ArrayList<Banner> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    public void setData(List<Banner> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }
}
