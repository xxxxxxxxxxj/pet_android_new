package com.haotang.pet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MyCanVerticBanner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.verticalbanner.BaseBannerAdapter;
import com.haotang.pet.verticalbanner.VerticalBannerView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/9 16:36
 */
public class MyCanVerticBannerAdapter extends BaseBannerAdapter<MyCanVerticBanner> {
    private List<MyCanVerticBanner> mDatas;

    public MyCanVerticBannerAdapter(List<MyCanVerticBanner> datas) {
        super(datas);
    }

    @Override
    public View getView(VerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_justnow_adapter, null);
    }

    @Override
    public void setItem(final View view, final MyCanVerticBanner data) {
        ImageView civ_item_justnow_adapter = (ImageView) view
                .findViewById(R.id.civ_item_justnow_adapter);
        ImageView iv_item_justnow_userimgbottom = (ImageView) view
                .findViewById(R.id.iv_item_justnow_userimgbottom);
        TextView tv_item_justnow_adapter_name = (TextView) view
                .findViewById(R.id.tv_item_justnow_adapter_name);
        TextView tv_item_justnow_adapter_time = (TextView) view
                .findViewById(R.id.tv_item_justnow_adapter_time);
        TextView tv_item_justnow_adapter_shop = (TextView) view
                .findViewById(R.id.tv_item_justnow_adapter_shop);
        if (data != null) {
            Utils.setText(tv_item_justnow_adapter_name, data.userName + " " + data.activity, "",
                    View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_justnow_adapter_time, data.operatorTime, "",
                    View.VISIBLE, View.VISIBLE);
            GlideUtil.loadCircleImg(view.getContext(), data.avatar,
                    civ_item_justnow_adapter, R.drawable.user_icon_unnet_circle);
            if (Utils.isStrNull(data.vicon)) {
                iv_item_justnow_userimgbottom.setVisibility(View.VISIBLE);
                iv_item_justnow_userimgbottom.bringToFront();
                GlideUtil.loadCircleImg(view.getContext(), data.vicon,
                        iv_item_justnow_userimgbottom, R.drawable.user_icon_unnet_circle);
            } else {
                iv_item_justnow_userimgbottom.setVisibility(View.GONE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(data);
                    }
                }
            });
        }
    }

    public OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        public void OnItemClick(MyCanVerticBanner data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
