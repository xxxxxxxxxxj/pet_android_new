package com.haotang.pet.adapter;

import android.view.View;
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
 * @date zhoujunxia on 2019-12-16 17:51
 */
public class FosterOrderDetailPetAdapter extends BaseQuickAdapter<FosterPet, BaseViewHolder> {

    public FosterOrderDetailPetAdapter(int layoutResId, List<FosterPet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterPet item) {
        NiceImageView iv_fosterorderdetail_petimg = helper.getView(R.id.iv_fosterorderdetail_petimg);
        TextView tv_fosterorderdetail_petname = helper.getView(R.id.tv_fosterorderdetail_petname);
        TextView tv_fosterorderdetail_petzj = helper.getView(R.id.tv_fosterorderdetail_petzj);
        TextView tv_fosterorderdetail_jcfwf = helper.getView(R.id.tv_fosterorderdetail_jcfwf);
        TextView tv_fosterorderdetail_jcfwfdj = helper.getView(R.id.tv_fosterorderdetail_jcfwfdj);
        RelativeLayout rl_fosterorderdetail_yj = helper.getView(R.id.rl_fosterorderdetail_yj);
        TextView tv_fosterorderdetail_yjfwf = helper.getView(R.id.tv_fosterorderdetail_yjfwf);
        TextView tv_fosterorderdetail_yjtitle = helper.getView(R.id.tv_fosterorderdetail_yjtitle);
        TextView tv_fosterorderdetail_yjfwfdj = helper.getView(R.id.tv_fosterorderdetail_yjfwfdj);
        RelativeLayout rl_fosterorderdetail_ldfw = helper.getView(R.id.rl_fosterorderdetail_ldfw);
        TextView tv_fosterorderdetail_ldfwf = helper.getView(R.id.tv_fosterorderdetail_ldfwf);
        TextView tv_fosterorderdetail_ldtitle = helper.getView(R.id.tv_fosterorderdetail_ldtitle);
        TextView tv_fosterorderdetail_jcfwftitle = helper.getView(R.id.tv_fosterorderdetail_jcfwftitle);
        RelativeLayout rl_fosterorderdetail_jcfwf = helper.getView(R.id.rl_fosterorderdetail_jcfwf);
        if (item != null) {
            GlideUtil.loadImg(mContext, item.getAvatar(), iv_fosterorderdetail_petimg, R.drawable.icon_production_default);
            Utils.setText(tv_fosterorderdetail_petname, item.getNickname(), "", View.VISIBLE, View.VISIBLE);
            if (item.getIsAddPet() == 1) {
                tv_fosterorderdetail_petzj.setVisibility(View.GONE);
            } else {
                tv_fosterorderdetail_petzj.setVisibility(View.GONE);
            }
            if (item.getTotalServiceFee() > 0) {
                rl_fosterorderdetail_jcfwf.setVisibility(View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_jcfwftitle, item.getServiceFeeName(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_jcfwf, "¥" + item.getTotalServiceFee(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_jcfwfdj, "¥" + item.getServiceFee() + "*" + item.getServiceFeeDays() + "天", "", View.VISIBLE, View.VISIBLE);
            } else {
                rl_fosterorderdetail_jcfwf.setVisibility(View.GONE);
            }
            if (item.getTotalExtraServiceFee() > 0) {
                rl_fosterorderdetail_yj.setVisibility(View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_yjtitle, item.getExtraServiceName(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_yjfwf, "¥" + item.getTotalExtraServiceFee(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_yjfwfdj, "¥" + item.getExtraServiceFee() + "*" + item.getExtraServiceFeeDays() + "天", "", View.VISIBLE, View.VISIBLE);
            } else {
                rl_fosterorderdetail_yj.setVisibility(View.GONE);
            }
            if (item.getListBathFee() > 0) {
                rl_fosterorderdetail_ldfw.setVisibility(View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_ldtitle, item.getBathFeeName(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_fosterorderdetail_ldfwf, "¥" + item.getListBathFee(), "", View.VISIBLE, View.VISIBLE);
            } else {
                rl_fosterorderdetail_ldfw.setVisibility(View.GONE);
            }
        }
    }
}
