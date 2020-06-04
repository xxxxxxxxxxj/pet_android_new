package com.haotang.pet.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
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
 * @date zhoujunxia on 2018/8/17 17:35
 */
public class PetDiaryDiaryAdapter extends BaseQuickAdapter<PetDiary, BaseViewHolder> {
    public PetDiaryDiaryAdapter(int layoutResId, List<PetDiary> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PetDiary item) {
        TextView tv_item_petdiary_diary_riqi = helper.getView(R.id.tv_item_petdiary_diary_riqi);
        TextView tv_item_petdiary_diary_fwxm = helper.getView(R.id.tv_item_petdiary_diary_fwxm);
        TextView tv_item_petdiary_diary_fwsj = helper.getView(R.id.tv_item_petdiary_diary_fwsj);
        TextView tv_item_petdiary_diary_fwdx = helper.getView(R.id.tv_item_petdiary_diary_fwdx);
        TextView tv_item_petdiary_diary_mrs = helper.getView(R.id.tv_item_petdiary_diary_mrs);
        TextView tv_item_petdiary_diary_bbqk = helper.getView(R.id.tv_item_petdiary_diary_bbqk);
        TextView tv_item_petdiary_diary_imgstit = helper.getView(R.id.tv_item_petdiary_diary_imgstit);
        RecyclerView rv_item_petdiary_diary_beforeimgs = helper.getView(R.id.rv_item_petdiary_diary_beforeimgs);
        RecyclerView rv_item_petdiary_diary_afterimgs = helper.getView(R.id.rv_item_petdiary_diary_afterimgs);
        if (item != null) {
            Utils.setText(tv_item_petdiary_diary_riqi, item.getCreateTimes(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petdiary_diary_fwxm, item.getServiceName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petdiary_diary_fwsj, item.getAppointment(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petdiary_diary_fwdx, item.getExtraItem(), "无", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petdiary_diary_mrs, item.getWorkerName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petdiary_diary_bbqk, item.getBabyContent(), "未填写", View.VISIBLE, View.VISIBLE);
            if ((item.getBeforeImgList() != null && item.getBeforeImgList().size() > 0) || (item.getAfterImgList() != null && item.getAfterImgList().size() > 0)) {
                tv_item_petdiary_diary_imgstit.setText("宝贝照片：");
            } else {
                tv_item_petdiary_diary_imgstit.setText("宝贝照片：无");
            }
            if (item.getBeforeImgList() != null && item.getBeforeImgList().size() > 0) {
                rv_item_petdiary_diary_beforeimgs.setVisibility(View.VISIBLE);
                rv_item_petdiary_diary_beforeimgs.setHasFixedSize(true);
                rv_item_petdiary_diary_beforeimgs.setNestedScrollingEnabled(false);
                NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                        NoScollFullGridLayoutManager(rv_item_petdiary_diary_beforeimgs, mContext, 4,
                        GridLayoutManager.VERTICAL, false);
                noScollFullGridLayoutManager.setScrollEnabled(false);
                rv_item_petdiary_diary_beforeimgs.setLayoutManager(noScollFullGridLayoutManager);
                rv_item_petdiary_diary_beforeimgs.setAdapter(new PetDiaryImgAdapter(R.layout.petdiary_img, item.getBeforeImgList()));
            } else {
                rv_item_petdiary_diary_beforeimgs.setVisibility(View.GONE);
            }
            if (item.getAfterImgList() != null && item.getAfterImgList().size() > 0) {
                rv_item_petdiary_diary_afterimgs.setVisibility(View.VISIBLE);
                rv_item_petdiary_diary_afterimgs.setHasFixedSize(true);
                rv_item_petdiary_diary_afterimgs.setNestedScrollingEnabled(false);
                NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                        NoScollFullGridLayoutManager(rv_item_petdiary_diary_afterimgs, mContext, 4,
                        GridLayoutManager.VERTICAL, false);
                noScollFullGridLayoutManager.setScrollEnabled(false);
                rv_item_petdiary_diary_afterimgs.setLayoutManager(noScollFullGridLayoutManager);
                rv_item_petdiary_diary_afterimgs.setAdapter(new PetDiaryImgAdapter(R.layout.petdiary_img, item.getAfterImgList()));
            } else {
                rv_item_petdiary_diary_afterimgs.setVisibility(View.GONE);
            }
        }
    }
}