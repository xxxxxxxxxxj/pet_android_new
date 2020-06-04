package com.haotang.pet.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.WashPetService;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/4/4 13:02
 */
public class WashOrderDetailPetAdapter extends BaseQuickAdapter<WashPetService, BaseViewHolder> {
    private int[] WH;

    public WashOrderDetailPetAdapter(Activity activity, int layoutResId, List<WashPetService> data) {
        super(layoutResId, data);
        WH = Utils.getDisplayMetrics(activity);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WashPetService item) {
        TextView tv_item_washorderdetail_pet_totalprice = helper.getView(R.id.tv_item_washorderdetail_pet_totalprice);
        TextView tv_item_washorderdetail_pet_serviceloc = helper.getView(R.id.tv_item_washorderdetail_pet_serviceloc);
        TextView tv_item_washorderdetail_pet_name = helper.getView(R.id.tv_item_washorderdetail_pet_name);
        TextView tv_item_washorderdetail_pet_jcfw = helper.getView(R.id.tv_item_washorderdetail_pet_jcfw);
        LinearLayout ll_item_washorderdetail_pet_dxfw = helper.getView(R.id.ll_item_washorderdetail_pet_dxfw);
        TextView tv_item_washorderdetail_pet_dxfw = helper.getView(R.id.tv_item_washorderdetail_pet_dxfw);
        LinearLayout ll_item_washorderdetail_pet_zjdx = helper.getView(R.id.ll_item_washorderdetail_pet_zjdx);
        TextView tv_item_washorderdetail_pet_zjdx = helper.getView(R.id.tv_item_washorderdetail_pet_zjdx);
        LinearLayout ll_item_washorderdetail_pet_root = helper.getView(R.id.ll_item_washorderdetail_pet_root);
        if (item != null) {
            SpannableString ss1 = new SpannableString("¥" + ComputeUtil.add(item.getBasicServicePrice(), item.getExtraPrice(), item.getZjdxPrice()));
            ss1.setSpan(new TextAppearanceSpan(mContext, R.style.shiliu_normal), 1,
                    ss1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_item_washorderdetail_pet_totalprice.setText(ss1);
            SpannableString ss = new SpannableString(item.getPetName() + "  " + item.getServiceName() + " ¥" + item.getBasicServicePrice());
            ss.setSpan(new TextAppearanceSpan(mContext, R.style.shisan_normal_333333), (item.getPetName() + "  ").length(),
                    ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_item_washorderdetail_pet_name.setText(ss);
            Utils.setText(tv_item_washorderdetail_pet_jcfw, item.getJcfwName(), "", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(item.getDxfwName())) {
                Utils.setText(tv_item_washorderdetail_pet_dxfw, item.getDxfwName(), "", View.VISIBLE, View.VISIBLE);
                ll_item_washorderdetail_pet_dxfw.setVisibility(View.VISIBLE);
            } else {
                ll_item_washorderdetail_pet_dxfw.setVisibility(View.GONE);
            }
            if (Utils.isStrNull(item.getZjdxName())) {
                Utils.setText(tv_item_washorderdetail_pet_zjdx, item.getZjdxName(), "", View.VISIBLE, View.VISIBLE);
                ll_item_washorderdetail_pet_zjdx.setVisibility(View.VISIBLE);
            } else {
                ll_item_washorderdetail_pet_zjdx.setVisibility(View.GONE);
            }
            if (item.getServiceLoc() == 1) {
                tv_item_washorderdetail_pet_serviceloc.setText("到店");
                tv_item_washorderdetail_pet_serviceloc.setBackgroundDrawable(Utils.getDW("FAA04A"));
            } else if (item.getServiceLoc() == 2) {
                tv_item_washorderdetail_pet_serviceloc.setText("上门");
                tv_item_washorderdetail_pet_serviceloc.setBackgroundDrawable(Utils.getDW("2FC0F0"));
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WH[0] - Utils.dip2px(mContext, 1), ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_item_washorderdetail_pet_root.setLayoutParams(lp);
    }
}