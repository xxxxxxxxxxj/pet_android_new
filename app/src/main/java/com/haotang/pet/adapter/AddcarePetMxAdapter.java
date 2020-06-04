package com.haotang.pet.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterPet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NiceImageView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-21 16:21
 */
public class AddcarePetMxAdapter extends BaseQuickAdapter<FosterPet, BaseViewHolder> {
    public AddcarePetMxAdapter(int layoutResId, List<FosterPet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterPet item) {
        NiceImageView iv_item_addcarepetmx_petimg = helper.getView(R.id.iv_item_addcarepetmx_petimg);
        TextView tv_item_addcarepetmx_petname = helper.getView(R.id.tv_item_addcarepetmx_petname);
        TextView tv_item_addcarepetmx_petzj = helper.getView(R.id.tv_item_addcarepetmx_petzj);
        TextView tv_item_addcarepetmx_jcfwf = helper.getView(R.id.tv_item_addcarepetmx_jcfwf);
        TextView tv_item_addcarepetmx_jcfwfdj = helper.getView(R.id.tv_item_addcarepetmx_jcfwfdj);
        RelativeLayout rl_item_addcarepetmx_yj = helper.getView(R.id.rl_item_addcarepetmx_yj);
        TextView tv_item_addcarepetmx_yjfwf = helper.getView(R.id.tv_item_addcarepetmx_yjfwf);
        TextView tv_item_addcarepetmx_yjtitle = helper.getView(R.id.tv_item_addcarepetmx_yjtitle);
        TextView tv_item_addcarepetmx_yjfwfdj = helper.getView(R.id.tv_item_addcarepetmx_yjfwfdj);
        LinearLayout ll_item_addcarepetmx_ldfw = helper.getView(R.id.ll_item_addcarepetmx_ldfw);
        TextView tv_item_addcarepetmx_ldfwf = helper.getView(R.id.tv_item_addcarepetmx_ldfwf);
        TextView tv_item_addcarepetmx_ldtitle = helper.getView(R.id.tv_item_addcarepetmx_ldtitle);
        TextView tv_item_addcarepetmx_jcfwftitle = helper.getView(R.id.tv_item_addcarepetmx_jcfwftitle);
        RelativeLayout rl_item_addcarepetmx_jcfwf = helper.getView(R.id.rl_item_addcarepetmx_jcfwf);
        if (item != null) {
            GlideUtil.loadImg(mContext, item.getAvatar(), iv_item_addcarepetmx_petimg, R.drawable.icon_production_default);
            Utils.setText(tv_item_addcarepetmx_petname, item.getNickname(), "", View.VISIBLE, View.VISIBLE);
            if (item.getIsAddPet() == 1) {
                tv_item_addcarepetmx_petzj.setVisibility(View.GONE);
            } else {
                tv_item_addcarepetmx_petzj.setVisibility(View.GONE);
            }
            if (item.getTotalServiceFee() > 0) {
                rl_item_addcarepetmx_jcfwf.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_jcfwftitle, item.getServiceFeeName(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_jcfwf, "¥" + item.getTotalServiceFee(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_jcfwfdj, "¥" + item.getServiceFee() + "*" + item.getServiceFeeDays() + "天", "", View.VISIBLE, View.VISIBLE);
            } else {
                rl_item_addcarepetmx_jcfwf.setVisibility(View.GONE);
            }
            if (item.getTotalExtraServiceFee() > 0) {
                rl_item_addcarepetmx_yj.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_yjtitle, item.getExtraServiceName(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_yjfwf, "¥" + item.getTotalExtraServiceFee(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_yjfwfdj, "¥" + item.getExtraServiceFee() + "*" + item.getExtraServiceFeeDays() + "天", "", View.VISIBLE, View.VISIBLE);
            } else {
                rl_item_addcarepetmx_yj.setVisibility(View.GONE);
            }
            if (item.getListBathFee() > 0) {
                ll_item_addcarepetmx_ldfw.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_ldtitle, item.getBathFeeName(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_item_addcarepetmx_ldfwf, "¥" + item.getListBathFee(), "", View.VISIBLE, View.VISIBLE);
            } else {
                ll_item_addcarepetmx_ldfw.setVisibility(View.GONE);
            }
            helper.addOnClickListener(R.id.iv_addcarepetmx_delete);
        }
    }
}
