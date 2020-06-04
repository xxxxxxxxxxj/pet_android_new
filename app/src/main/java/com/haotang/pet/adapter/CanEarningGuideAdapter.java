package com.haotang.pet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CanEarningGuide;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/9 16:47
 */
public class CanEarningGuideAdapter<T> extends CommonAdapter<T> {
    public CanEarningGuideAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CanEarningGuide canEarningGuide = (CanEarningGuide) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_can, position);
        ImageView iv_item_can = viewHolder
                .getView(R.id.iv_item_can);
        Button btn_item_can = viewHolder
                .getView(R.id.btn_item_can);
        TextView tv_item_can_name = viewHolder
                .getView(R.id.tv_item_can_name);
        TextView tv_item_can_desc = viewHolder
                .getView(R.id.tv_item_can_desc);
        if (canEarningGuide != null) {
            if(canEarningGuide.state == 1){
                btn_item_can.setTextColor(mContext.getResources().getColor(R.color.white));
                btn_item_can.setBackgroundResource(R.drawable.bg_button_orange);
            }else if(canEarningGuide.state == 0){
                btn_item_can.setTextColor(mContext.getResources().getColor(R.color.a999999));
                btn_item_can.setBackgroundResource(R.drawable.bg_button_huiorange);
            }
            GlideUtil.loadCircleImg(mContext, canEarningGuide.icon, iv_item_can,
                    R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_can_name, canEarningGuide.name, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_can_desc, canEarningGuide.title, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(btn_item_can, canEarningGuide.content, "", View.VISIBLE, View.VISIBLE);
            btn_item_can.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null && canEarningGuide.state == 1) {
                        onItemClickListener.OnItemClick(canEarningGuide);
                    }
                }
            });
        }
        return viewHolder.getConvertView();
    }

    public OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        public void OnItemClick(CanEarningGuide data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
