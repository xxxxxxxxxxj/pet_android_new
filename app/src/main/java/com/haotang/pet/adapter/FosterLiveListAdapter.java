package com.haotang.pet.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.FosterLive;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-16 10:35
 */
public class FosterLiveListAdapter extends BaseQuickAdapter<FosterLive, BaseViewHolder> {
    public FosterLiveListAdapter(int layoutResId, List<FosterLive> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FosterLive item) {
        TextView tv_fosterlivelist_name = helper.getView(R.id.tv_fosterlivelist_name);
        RecyclerView rv_fosterlivelist_pet = helper.getView(R.id.rv_fosterlivelist_pet);
        TextView tv_fosterlivelist_petname = helper.getView(R.id.tv_fosterlivelist_petname);
        if (item != null) {
            String[] split = item.getRoomContent().split("@@");
            int startIndex = item.getRoomContent().indexOf("@@");
            int endIndex = split[0].length() + split[1].length();
            SpannableString ss = new SpannableString(item.getRoomContent().replace("@@", ""));
            ss.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.aff3a1e)),
                    startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_fosterlivelist_name.setText(ss);
            Utils.setText(tv_fosterlivelist_petname, item.getRoomPetContent(), "", View.VISIBLE, View.VISIBLE);
            if (item.getRoomPetAvatarList() != null && item.getRoomPetAvatarList().size() > 0) {
                rv_fosterlivelist_pet.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_fosterlivelist_pet.setLayoutManager(linearLayoutManager);
                rv_fosterlivelist_pet.setAdapter(new FosterLiveListPetAdapter(R.layout.item_fosterlivelistpet, item.getRoomPetAvatarList()));
            } else {
                rv_fosterlivelist_pet.setVisibility(View.GONE);
            }
            helper.addOnClickListener(R.id.sl_fosterlivelist_root).addOnClickListener(R.id.iv_fosterlivelist_call).addOnClickListener(R.id.iv_fosterlivelist_qp);
        }
    }
}
