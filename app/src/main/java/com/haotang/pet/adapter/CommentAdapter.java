package com.haotang.pet.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.materialratingbar.MaterialRatingBar;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NoScollFullGridLayoutManager;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/9 13:55
 */
public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public CommentAdapter(int layoutResId, List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Comment item) {
        ImageView iv_item_comment_img = helper.getView(R.id.iv_item_comment_img);
        ImageView iv_item_comment_vip = helper.getView(R.id.iv_item_comment_vip);
        TextView tv_item_comment_time = helper.getView(R.id.tv_item_comment_time);
        TextView tv_item_comment_name = helper.getView(R.id.tv_item_comment_name);
        TextView tv_item_comment_txt = helper.getView(R.id.tv_item_comment_txt);
        RecyclerView rv_item_comment_img = helper.getView(R.id.rv_item_comment_img);
        TextView tv_item_comment_fwtime = helper.getView(R.id.tv_item_comment_fwtime);
        TextView tv_item_comment_fwname = helper.getView(R.id.tv_item_comment_fwname);
        TextView tv_item_comment_tags = helper.getView(R.id.tv_item_comment_tags);
        final ImageView iv_item_comment_fz = helper.getView(R.id.iv_item_comment_fz);
        RecyclerView rv_item_comment_dx = helper.getView(R.id.rv_item_comment_dx);
        LinearLayout ll_item_comment_item = helper.getView(R.id.ll_item_comment_item);
        MaterialRatingBar mrb_item_comment_name = helper.getView(R.id.mrb_item_comment_name);
        RelativeLayout rv_beautician_comment = helper.getView(R.id.rv_beautician_comment);
        ImageView iv_beautician_head = helper.getView(R.id.iv_beautician_head);
        TextView tv_beautician_name = helper.getView(R.id.tv_beautician_name);
        TextView tv_beautician_comment = helper.getView(R.id.tv_beautician_comment);
        if (item != null) {
            mrb_item_comment_name.setNumStars(5);
            mrb_item_comment_name.setProgress(item.grade);
            GlideUtil.loadCircleImg(mContext, item.avatar, iv_item_comment_img, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_comment_name, item.userName, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_comment_txt, item.contents, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_comment_tags, item.commentTags, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_comment_time, item.time, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_comment_fwtime, "服务时间:" + item.appointment, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_comment_fwname, item.serviceName, "", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(item.memberIcon)) {
                iv_item_comment_vip.setVisibility(View.VISIBLE);
                iv_item_comment_vip.bringToFront();
                GlideUtil.loadCircleImg(mContext, item.memberIcon, iv_item_comment_vip, R.drawable.user_icon_unnet_circle);
            } else {
                iv_item_comment_vip.setVisibility(View.GONE);
            }
            String[] images = item.images;
            if (images != null && images.length > 0) {
                rv_item_comment_img.setVisibility(View.VISIBLE);
                rv_item_comment_img.setHasFixedSize(true);
                rv_item_comment_img.setNestedScrollingEnabled(false);
                NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                        NoScollFullGridLayoutManager(rv_item_comment_dx, mContext, 4, GridLayoutManager.VERTICAL, false);
                noScollFullGridLayoutManager.setScrollEnabled(false);
                rv_item_comment_img.setLayoutManager(noScollFullGridLayoutManager);
                rv_item_comment_img.setAdapter(new CommentImgAdapter(R.layout.comment_img
                        , Arrays.asList(images)));
            } else {
                rv_item_comment_img.setVisibility(View.GONE);
            }
            if (item.itemList != null && item.itemList.size() > 0) {
                ll_item_comment_item.setVisibility(View.VISIBLE);
                boolean fz = item.isFz();
                if (fz) {
                    iv_item_comment_fz.setImageResource(R.drawable.icon_yfz);
                    rv_item_comment_dx.setVisibility(View.VISIBLE);
                    rv_item_comment_dx.setHasFixedSize(true);
                    rv_item_comment_dx.setNestedScrollingEnabled(false);
                    NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                            NoScollFullGridLayoutManager(rv_item_comment_dx, mContext, 4, GridLayoutManager.VERTICAL, false);
                    noScollFullGridLayoutManager.setScrollEnabled(false);
                    rv_item_comment_dx.setLayoutManager(noScollFullGridLayoutManager);
                    rv_item_comment_dx.setAdapter(new CommentItemAdapter(R.layout.comment_item
                            , item.itemList));
                } else {
                    iv_item_comment_fz.setImageResource(R.drawable.icon_wfz);
                    rv_item_comment_dx.setVisibility(View.GONE);
                }
                ll_item_comment_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setFz(!item.isFz());
                        notifyDataSetChanged();
                    }
                });
            } else {
                ll_item_comment_item.setVisibility(View.GONE);
                rv_item_comment_dx.setVisibility(View.GONE);
            }
            if (item.commentWorkerContent!=null&& !TextUtils.isEmpty(item.commentWorkerContent)){
                rv_beautician_comment.setVisibility(View.VISIBLE);
                GlideUtil.loadCircleImg(mContext, item.getAvatarBeauty(), iv_beautician_head, R.drawable.user_icon_unnet_circle);
                tv_beautician_name.setText(item.getNickname());
                tv_beautician_comment.setText(item.commentWorkerContent);
            }else {
                rv_beautician_comment.setVisibility(View.GONE);
            }
        }
    }
}