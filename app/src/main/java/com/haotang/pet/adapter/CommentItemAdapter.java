package com.haotang.pet.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/9 15:18
 */
class CommentItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CommentItemAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv_item_commentitem = helper.getView(R.id.tv_item_commentitem);
        Utils.setText(tv_item_commentitem, item, "", View.VISIBLE, View.VISIBLE);
    }
}