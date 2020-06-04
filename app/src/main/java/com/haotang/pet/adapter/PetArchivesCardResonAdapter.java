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
 * @date zhoujunxia on 2018/8/17 10:55
 */
public class PetArchivesCardResonAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PetArchivesCardResonAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv_item_petarchives_card_reson = helper.getView(R.id.tv_item_petarchives_card_reson);
        Utils.setText(tv_item_petarchives_card_reson, item, "", View.VISIBLE, View.VISIBLE);
    }
}
