package com.haotang.pet.encyclopedias.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.encyclopedias.bean.EncyclopediasComment;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/2 17:48
 */
public class EncyCommentAdapter extends BaseQuickAdapter<EncyclopediasComment, BaseViewHolder> {
    public EncyCommentAdapter(int layoutResId, List<EncyclopediasComment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EncyclopediasComment item) {
        TextView tv_item_encycomment_time = helper.getView(R.id.tv_item_encycomment_time);
        ImageView iv_item_encycomment_userimg = helper.getView(R.id.iv_item_encycomment_userimg);
        TextView tv_item_encycomment_username = helper.getView(R.id.tv_item_encycomment_username);
        TextView tv_item_encycomment_txt = helper.getView(R.id.tv_item_encycomment_txt);
        if (item != null) {
            GlideUtil.loadCircleImg(mContext, item.getAvatar(), iv_item_encycomment_userimg, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_encycomment_time, item.getDateStr(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_encycomment_username, item.getUserName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_encycomment_txt, item.getContent(), "", View.VISIBLE, View.VISIBLE);
        }
    }
}