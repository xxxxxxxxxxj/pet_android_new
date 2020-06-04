package com.haotang.pet.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.PetDiary;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NoScollFullGridLayoutManager;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-16 13:49
 */
public class PetServiceRecordAdapter extends BaseQuickAdapter<PetDiary, BaseViewHolder> {
    public PetServiceRecordAdapter(int layoutResId, List<PetDiary> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PetDiary item) {
        TextView tv_item_petservicerecord_time = helper.getView(R.id.tv_item_petservicerecord_time);
        RecyclerView rv_item_petservicerecord_before = helper.getView(R.id.rv_item_petservicerecord_before);
        RecyclerView rv_item_petservicerecord_after = helper.getView(R.id.rv_item_petservicerecord_after);
        TextView tv_item_petservicerecord_desc = helper.getView(R.id.tv_item_petservicerecord_desc);
        TextView tv_item_petservicerecord_xm = helper.getView(R.id.tv_item_petservicerecord_xm);
        LinearLayout ll_item_petservicerecord_dx = helper.getView(R.id.ll_item_petservicerecord_dx);
        TextView tv_item_petservicerecord_dx = helper.getView(R.id.tv_item_petservicerecord_dx);
        TextView tv_item_petservicerecord_mrs = helper.getView(R.id.tv_item_petservicerecord_mrs);
        LinearLayout ll_item_petservicerecord_afterimg = helper.getView(R.id.ll_item_petservicerecord_afterimg);
        LinearLayout ll_item_petservicerecord_beforeimg = helper.getView(R.id.ll_item_petservicerecord_beforeimg);
        if (item != null) {
            if (Utils.isStrNull(item.getExtraItem())) {
                ll_item_petservicerecord_dx.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_petservicerecord_dx, item.getExtraItem(), "", View.VISIBLE, View.VISIBLE);
            } else {
                ll_item_petservicerecord_dx.setVisibility(View.GONE);
            }
            Utils.setText(tv_item_petservicerecord_xm, item.getServiceName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petservicerecord_time, item.getAppointment(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petservicerecord_mrs, item.getWorkerName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petservicerecord_desc, item.getBabyContent(), "", View.VISIBLE, View.GONE);
            if (item.getBeforeImgList() != null && item.getBeforeImgList().size() > 0) {
                ll_item_petservicerecord_beforeimg.setVisibility(View.VISIBLE);
                rv_item_petservicerecord_before.setHasFixedSize(true);
                rv_item_petservicerecord_before.setNestedScrollingEnabled(false);
                NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                        NoScollFullGridLayoutManager(rv_item_petservicerecord_before, mContext, 4,
                        GridLayoutManager.VERTICAL, false);
                noScollFullGridLayoutManager.setScrollEnabled(false);
                rv_item_petservicerecord_before.setLayoutManager(noScollFullGridLayoutManager);
                rv_item_petservicerecord_before.setAdapter(new PetDiaryImgAdapter(R.layout.petdiary_img, item.getBeforeImgList()));
            } else {
                ll_item_petservicerecord_beforeimg.setVisibility(View.GONE);
            }
            if (item.getAfterImgList() != null && item.getAfterImgList().size() > 0) {
                ll_item_petservicerecord_afterimg.setVisibility(View.VISIBLE);
                rv_item_petservicerecord_after.setHasFixedSize(true);
                rv_item_petservicerecord_after.setNestedScrollingEnabled(false);
                NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                        NoScollFullGridLayoutManager(rv_item_petservicerecord_after, mContext, 4,
                        GridLayoutManager.VERTICAL, false);
                noScollFullGridLayoutManager.setScrollEnabled(false);
                rv_item_petservicerecord_after.setLayoutManager(noScollFullGridLayoutManager);
                rv_item_petservicerecord_after.setAdapter(new PetDiaryImgAdapter(R.layout.petdiary_img, item.getAfterImgList()));
            } else {
                ll_item_petservicerecord_afterimg.setVisibility(View.GONE);
            }
        }
    }
}