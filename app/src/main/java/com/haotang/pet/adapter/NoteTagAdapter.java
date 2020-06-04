package com.haotang.pet.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.NoteTag;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-16 10:49
 */
public class NoteTagAdapter extends BaseQuickAdapter<NoteTag, BaseViewHolder> {

    public NoteTagAdapter(int layoutResId, List<NoteTag> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NoteTag item) {
        LinearLayout ll_notetag = helper.getView(R.id.ll_notetag);
        TextView tv_notetag = helper.getView(R.id.tv_notetag);
        if (item != null) {
            Utils.setText(tv_notetag, item.getTag(), "", View.VISIBLE, View.GONE);
            if (item.isSelected()) {
                ll_notetag.setBackgroundResource(R.drawable.bg_note_biground_selected);
            } else {
                ll_notetag.setBackgroundResource(R.drawable.bg_note_biground);
            }
        }
    }
}