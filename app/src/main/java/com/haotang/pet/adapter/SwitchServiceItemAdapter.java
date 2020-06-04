package com.haotang.pet.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.SwitchServiceItemEvent;
import com.haotang.pet.entity.SwitchServiceItems;
import com.haotang.pet.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 22:19
 */
public class SwitchServiceItemAdapter extends BaseQuickAdapter<SwitchServiceItems, BaseViewHolder> {
    private final boolean isVip;
    private final int index;

    public SwitchServiceItemAdapter(int layoutResId, List<SwitchServiceItems> data, boolean isVip, int index) {
        super(layoutResId, data);
        this.isVip = isVip;
        this.index = index;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SwitchServiceItems item) {
        TextView tv_item_switchserviceitem_sub = helper.getView(R.id.tv_item_switchserviceitem_sub);
        TextView tv_item_switchserviceitem_price = helper.getView(R.id.tv_item_switchserviceitem_price);
        TextView tv_item_switchserviceitem_vipprice = helper.getView(R.id.tv_item_switchserviceitem_vipprice);
        TextView tv_item_switchserviceitem_name = helper.getView(R.id.tv_item_switchserviceitem_name);
        TextView tv_item_switchserviceitem_desc = helper.getView(R.id.tv_item_switchserviceitem_desc);
        TextView tv_item_switchserviceitem_tag = helper.getView(R.id.tv_item_switchserviceitem_tag);
        if (item != null) {
            if (Utils.isStrNull(item.getVipPrice()) && Utils.isStrNull(item.getPrice())) {
                if (Double.parseDouble(item.getVipPrice().split("@@")[1]) == Double.parseDouble(item.getPrice().split("@@")[1])) {
                    String[] split = item.getPrice().split("@@");
                    int startIndex = item.getPrice().indexOf("@@");
                    int endIndex = split[0].length() + split[1].length();
                    Log.e("TAG", "startIndex = " + startIndex);
                    Log.e("TAG", "endIndex = " + endIndex);
                    SpannableString ss = new SpannableString(item.getPrice().replace("@@", ""));
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.foster_style_y),
                            startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_switchserviceitem_price.setText(ss);
                    tv_item_switchserviceitem_price.setVisibility(View.VISIBLE);
                    tv_item_switchserviceitem_vipprice.setVisibility(View.GONE);
                } else {
                    String[] split = item.getPrice().split("@@");
                    int startIndex = item.getPrice().indexOf("@@");
                    int endIndex = split[0].length() + split[1].length();
                    Log.e("TAG", "startIndex = " + startIndex);
                    Log.e("TAG", "endIndex = " + endIndex);
                    SpannableString ss = new SpannableString(item.getPrice().replace("@@", ""));
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.foster_style_y),
                            startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_switchserviceitem_price.setText(ss);
                    String[] split1 = item.getVipPrice().split("@@");
                    int startIndex1 = item.getVipPrice().indexOf("@@");
                    int endIndex1 = split1[0].length() + split1[1].length();
                    Log.e("TAG", "startIndex1 = " + startIndex1);
                    Log.e("TAG", "endIndex1 = " + endIndex1);
                    SpannableString ss1 = new SpannableString(item.getVipPrice().replace("@@", ""));
                    ss1.setSpan(new TextAppearanceSpan(mContext, R.style.foster_style_y),
                            startIndex1, endIndex1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_switchserviceitem_vipprice.setText(ss1);
                    tv_item_switchserviceitem_price.setVisibility(View.VISIBLE);
                    tv_item_switchserviceitem_vipprice.setVisibility(View.VISIBLE);
                }
            } else {
                if (Utils.isStrNull(item.getPrice())) {
                    String[] split = item.getPrice().split("@@");
                    int startIndex = item.getPrice().indexOf("@@");
                    int endIndex = split[0].length() + split[1].length();
                    Log.e("TAG", "startIndex = " + startIndex);
                    Log.e("TAG", "endIndex = " + endIndex);
                    SpannableString ss = new SpannableString(item.getPrice().replace("@@", ""));
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.foster_style_y),
                            startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_switchserviceitem_price.setText(ss);
                    tv_item_switchserviceitem_price.setVisibility(View.VISIBLE);
                }else{
                    tv_item_switchserviceitem_price.setVisibility(View.GONE);
                }
                if (Utils.isStrNull(item.getVipPrice())) {
                    String[] split1 = item.getVipPrice().split("@@");
                    int startIndex1 = item.getVipPrice().indexOf("@@");
                    int endIndex1 = split1[0].length() + split1[1].length();
                    Log.e("TAG", "startIndex1 = " + startIndex1);
                    Log.e("TAG", "endIndex1 = " + endIndex1);
                    SpannableString ss1 = new SpannableString(item.getVipPrice().replace("@@", ""));
                    ss1.setSpan(new TextAppearanceSpan(mContext, R.style.foster_style_y),
                            startIndex1, endIndex1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_switchserviceitem_vipprice.setText(ss1);
                    tv_item_switchserviceitem_vipprice.setVisibility(View.VISIBLE);
                }else{
                    tv_item_switchserviceitem_vipprice.setVisibility(View.GONE);
                }
            }
            if (item.getLabel() > 0) {
                tv_item_switchserviceitem_tag.setVisibility(View.VISIBLE);
                if (item.getLabel() == 1) {
                    tv_item_switchserviceitem_tag.setText("NEW");
                } else if (item.getLabel() == 2) {
                    tv_item_switchserviceitem_tag.setText("HOT");
                } else if (item.getLabel() == 3) {
                    tv_item_switchserviceitem_tag.setText("推荐");
                }
            } else {
                tv_item_switchserviceitem_tag.setVisibility(View.GONE);
            }
            Utils.setText(tv_item_switchserviceitem_name, item.getName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_switchserviceitem_desc, item.getDescription(), "", View.VISIBLE, View.VISIBLE);
            if (item.isSelect()) {
                tv_item_switchserviceitem_sub.setText("已选择");
                tv_item_switchserviceitem_sub.setBackgroundResource(R.drawable.icon_bbb_shade);
            } else {
                tv_item_switchserviceitem_sub.setText("选择");
                tv_item_switchserviceitem_sub.setBackgroundResource(R.drawable.bg_shop_shade);
            }
            tv_item_switchserviceitem_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new SwitchServiceItemEvent(helper.getLayoutPosition(), index));
                }
            });
        }
    }
}
