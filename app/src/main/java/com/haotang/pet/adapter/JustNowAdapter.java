package com.haotang.pet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.JustNow;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.verticalbanner.BaseBannerAdapter;
import com.haotang.pet.verticalbanner.VerticalBannerView;

import java.util.List;

/**
 * <p>
 * Title:TopMsgAdapter
 * </p>
 * <p>
 * Description:顶部跑马灯
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-6-2 下午2:41:06
 */
public class JustNowAdapter extends BaseBannerAdapter<JustNow> {
	private List<JustNow> mDatas;

	public JustNowAdapter(List<JustNow> datas) {
		super(datas);
	}

	@Override
	public View getView(VerticalBannerView parent) {
		return LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item_justnow_adapter, null);
	}

	@Override
	public void setItem(final View view, final JustNow data) {
		ImageView civ_item_justnow_adapter = (ImageView) view
				.findViewById(R.id.civ_item_justnow_adapter);
		TextView tv_item_justnow_adapter_name = (TextView) view
				.findViewById(R.id.tv_item_justnow_adapter_name);
		TextView tv_item_justnow_adapter_time = (TextView) view
				.findViewById(R.id.tv_item_justnow_adapter_time);
		TextView tv_item_justnow_adapter_shop = (TextView) view
				.findViewById(R.id.tv_item_justnow_adapter_shop);
		if (data != null) {
			Utils.setText(tv_item_justnow_adapter_name, data.content, "",
					View.VISIBLE, View.VISIBLE);
			Utils.setText(tv_item_justnow_adapter_time, data.time, "",
					View.VISIBLE, View.VISIBLE);
			Utils.setText(tv_item_justnow_adapter_shop, data.shopName, "",
					View.VISIBLE, View.VISIBLE);
			GlideUtil.loadCircleImg(view.getContext(), data.avatar,
					civ_item_justnow_adapter, R.drawable.user_icon_unnet_circle);
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
		public void OnItemClick(JustNow data);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
}
