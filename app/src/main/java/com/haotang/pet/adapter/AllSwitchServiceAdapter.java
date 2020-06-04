package com.haotang.pet.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.AllSwitchService;
import com.haotang.pet.entity.AllSwitchServiceEvent;
import com.haotang.pet.entity.SwitchService;
import com.haotang.pet.entity.SwitchServiceItems;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 21:25
 */
public class AllSwitchServiceAdapter extends BaseQuickAdapter<AllSwitchService, BaseViewHolder> {
    private final boolean isVip;
    private final int index;

    public AllSwitchServiceAdapter(int layoutResId, List<AllSwitchService> data, boolean isVip, int index) {
        super(layoutResId, data);
        this.isVip = isVip;
        this.index = index;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AllSwitchService item) {
        LinearLayout ll_item_switchservice_all1 = helper.getView(R.id.ll_item_switchservice_all1);
        ImageView iv_item_switchservice_img1 = helper.getView(R.id.iv_item_switchservice_img1);
        ImageView iv_item_switchservice_img1_bgselect = helper.getView(R.id.iv_item_switchservice_img1_bgselect);
        TextView tv_item_switchservice_name1 = helper.getView(R.id.tv_item_switchservice_name1);
        TextView tv_item_switchservice_price1 = helper.getView(R.id.tv_item_switchservice_price1);
        ImageView iv_item_switchservice1_jianer = helper.getView(R.id.iv_item_switchservice1_jianer);
        TextView tv_item_switchservice_tag1 = helper.getView(R.id.tv_item_switchservice_tag1);

        LinearLayout ll_item_switchservice_all2 = helper.getView(R.id.ll_item_switchservice_all2);
        ImageView iv_item_switchservice_img2 = helper.getView(R.id.iv_item_switchservice_img2);
        ImageView iv_item_switchservice_img2_bgselect = helper.getView(R.id.iv_item_switchservice_img2_bgselect);
        TextView tv_item_switchservice_name2 = helper.getView(R.id.tv_item_switchservice_name2);
        TextView tv_item_switchservice_price2 = helper.getView(R.id.tv_item_switchservice_price2);
        ImageView iv_item_switchservice2_jianer = helper.getView(R.id.iv_item_switchservice2_jianer);
        TextView tv_item_switchservice_tag2 = helper.getView(R.id.tv_item_switchservice_tag2);

        LinearLayout ll_item_switchservice_all3 = helper.getView(R.id.ll_item_switchservice_all3);
        ImageView iv_item_switchservice_img3 = helper.getView(R.id.iv_item_switchservice_img3);
        ImageView iv_item_switchservice_img3_bgselect = helper.getView(R.id.iv_item_switchservice_img3_bgselect);
        TextView tv_item_switchservice_name3 = helper.getView(R.id.tv_item_switchservice_name3);
        TextView tv_item_switchservice_price3 = helper.getView(R.id.tv_item_switchservice_price3);
        ImageView iv_item_switchservice3_jianer = helper.getView(R.id.iv_item_switchservice3_jianer);
        TextView tv_item_switchservice_tag3 = helper.getView(R.id.tv_item_switchservice_tag3);
        RecyclerView rv_item_switchservice_all = helper.getView(R.id.rv_item_switchservice_all);
        LinearLayout ll_item_switchservice_all_root = helper.getView(R.id.ll_item_switchservice_all_root);
        LinearLayout ll_item_switchservice_jianer = helper.getView(R.id.ll_item_switchservice_jianer);
        rv_item_switchservice_all.setVisibility(View.GONE);
        if (helper.getLayoutPosition() == 0) {
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) ll_item_switchservice_all_root.getLayoutParams();
            layoutParams.topMargin = DensityUtil.dp2px(mContext, 15);
            ll_item_switchservice_all_root.setLayoutParams(layoutParams);
        }
        if (item != null) {
            final SwitchService switchService1 = item.getSwitchService1();
            final SwitchService switchService2 = item.getSwitchService2();
            final SwitchService switchService3 = item.getSwitchService3();
            boolean select1 = item.isSelect1();
            boolean select2 = item.isSelect2();
            boolean select3 = item.isSelect3();
            if (switchService1 != null) {
                ll_item_switchservice_all1.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_switchservice_name1, switchService1.getName(), "", View.VISIBLE, View.VISIBLE);
                if (Utils.isStrNull(switchService1.getVipPrice()) && Utils.isStrNull(switchService1.getPrice())) {
                    if (Double.parseDouble(switchService1.getVipPrice().split("@@")[1]) == Double.parseDouble(switchService1.getPrice().split("@@")[1])) {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    } else {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                } else {
                    if (Utils.isStrNull(switchService1.getPrice())) {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                    if (Utils.isStrNull(switchService1.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }
                /*if(isVip){
                    if (Utils.isStrNull(switchService1.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }else if (Utils.isStrNull(switchService1.getPrice())) {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }else{
                    if (Utils.isStrNull(switchService1.getPrice())) {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }else if (Utils.isStrNull(switchService1.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price1, switchService1.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }*/
                GlideUtil.loadCircleImg(mContext, switchService1.getPic(), iv_item_switchservice_img1, R.drawable.user_icon_unnet_circle);
                List<SwitchServiceItems> items = switchService1.getItems();
                if (select1) {
                    iv_item_switchservice_img1_bgselect.setVisibility(View.VISIBLE);
                    iv_item_switchservice_img1_bgselect.bringToFront();
                    if (items != null && items.size() > 0) {
                        ll_item_switchservice_jianer.setVisibility(View.VISIBLE);
                        rv_item_switchservice_all.setVisibility(View.VISIBLE);
                        iv_item_switchservice1_jianer.setVisibility(View.VISIBLE);
                        ll_item_switchservice_jianer.bringToFront();
                        rv_item_switchservice_all.setHasFixedSize(true);
                        rv_item_switchservice_all.setNestedScrollingEnabled(false);
                        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(mContext);
                        noScollFullLinearLayoutManager.setScrollEnabled(false);
                        rv_item_switchservice_all.setLayoutManager(noScollFullLinearLayoutManager);
                        rv_item_switchservice_all.setAdapter(new SwitchServiceItemAdapter(R.layout.item_switchservice_item, items, isVip, index));
                    }
                } else {
                    iv_item_switchservice_img1_bgselect.setVisibility(View.GONE);
                    iv_item_switchservice1_jianer.setVisibility(View.GONE);
                }
                if (switchService1.getLabel() > 0) {
                    tv_item_switchservice_tag1.setVisibility(View.VISIBLE);
                    tv_item_switchservice_tag1.bringToFront();
                    if (switchService1.getLabel() == 1) {
                        tv_item_switchservice_tag1.setText("NEW");
                    } else if (switchService1.getLabel() == 2) {
                        tv_item_switchservice_tag1.setText("HOT");
                    } else if (switchService1.getLabel() == 3) {
                        tv_item_switchservice_tag1.setText("推荐");
                    }
                } else {
                    tv_item_switchservice_tag1.setVisibility(View.GONE);
                }
            } else {
                ll_item_switchservice_all1.setVisibility(View.INVISIBLE);
            }

            if (switchService2 != null) {
                ll_item_switchservice_all1.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_switchservice_name2, switchService2.getName(), "", View.VISIBLE, View.VISIBLE);
                if (Utils.isStrNull(switchService2.getVipPrice()) && Utils.isStrNull(switchService2.getPrice())) {
                    if (Double.parseDouble(switchService2.getVipPrice().split("@@")[1]) == Double.parseDouble(switchService2.getPrice().split("@@")[1])) {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    } else {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                } else {
                    if (Utils.isStrNull(switchService2.getPrice())) {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                    if (Utils.isStrNull(switchService2.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }
                /*if(isVip){
                    if (Utils.isStrNull(switchService2.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }else if (Utils.isStrNull(switchService2.getPrice())) {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }else{
                    if (Utils.isStrNull(switchService2.getPrice())) {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }else if (Utils.isStrNull(switchService2.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price2, switchService2.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }*/
                GlideUtil.loadCircleImg(mContext, switchService2.getPic(), iv_item_switchservice_img2, R.drawable.user_icon_unnet_circle);
                List<SwitchServiceItems> items = switchService2.getItems();
                if (select2) {
                    iv_item_switchservice_img2_bgselect.setVisibility(View.VISIBLE);
                    iv_item_switchservice_img2_bgselect.bringToFront();
                    if (items != null && items.size() > 0) {
                        ll_item_switchservice_jianer.setVisibility(View.VISIBLE);
                        rv_item_switchservice_all.setVisibility(View.VISIBLE);
                        iv_item_switchservice2_jianer.setVisibility(View.VISIBLE);
                        ll_item_switchservice_jianer.bringToFront();
                        rv_item_switchservice_all.setHasFixedSize(true);
                        rv_item_switchservice_all.setNestedScrollingEnabled(false);
                        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(mContext);
                        noScollFullLinearLayoutManager.setScrollEnabled(false);
                        rv_item_switchservice_all.setLayoutManager(noScollFullLinearLayoutManager);
                        rv_item_switchservice_all.setAdapter(new SwitchServiceItemAdapter(R.layout.item_switchservice_item, items, isVip, index));
                    }
                } else {
                    iv_item_switchservice_img2_bgselect.setVisibility(View.GONE);
                    iv_item_switchservice2_jianer.setVisibility(View.GONE);
                }
                if (switchService2.getLabel() > 0) {
                    tv_item_switchservice_tag2.setVisibility(View.VISIBLE);
                    tv_item_switchservice_tag2.bringToFront();
                    if (switchService2.getLabel() == 1) {
                        tv_item_switchservice_tag2.setText("NEW");
                    } else if (switchService2.getLabel() == 2) {
                        tv_item_switchservice_tag2.setText("HOT");
                    } else if (switchService2.getLabel() == 3) {
                        tv_item_switchservice_tag2.setText("推荐");
                    }
                } else {
                    tv_item_switchservice_tag2.setVisibility(View.GONE);
                }
            } else {
                ll_item_switchservice_all2.setVisibility(View.INVISIBLE);
            }
            if (switchService3 != null) {
                ll_item_switchservice_all3.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_switchservice_name3, switchService3.getName(), "", View.VISIBLE, View.VISIBLE);
                if (Utils.isStrNull(switchService3.getVipPrice()) && Utils.isStrNull(switchService3.getPrice())) {
                    if (Double.parseDouble(switchService3.getVipPrice().split("@@")[1]) == Double.parseDouble(switchService3.getPrice().split("@@")[1])) {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    } else {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                } else {
                    if (Utils.isStrNull(switchService3.getPrice())) {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                    if (Utils.isStrNull(switchService3.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }
                /*if(isVip){
                    if (Utils.isStrNull(switchService3.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }else if (Utils.isStrNull(switchService3.getPrice())) {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }else{
                    if (Utils.isStrNull(switchService3.getPrice())) {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }else if (Utils.isStrNull(switchService3.getVipPrice())) {
                        Utils.setText(tv_item_switchservice_price3, switchService3.getVipPrice().replace("@@", ""), "", View.VISIBLE, View.VISIBLE);
                    }
                }*/
                GlideUtil.loadCircleImg(mContext, switchService3.getPic(), iv_item_switchservice_img3, R.drawable.user_icon_unnet_circle);
                List<SwitchServiceItems> items = switchService3.getItems();
                if (select3) {
                    iv_item_switchservice_img3_bgselect.setVisibility(View.VISIBLE);
                    iv_item_switchservice_img3_bgselect.bringToFront();
                    if (items != null && items.size() > 0) {
                        ll_item_switchservice_jianer.setVisibility(View.VISIBLE);
                        rv_item_switchservice_all.setVisibility(View.VISIBLE);
                        iv_item_switchservice3_jianer.setVisibility(View.VISIBLE);
                        ll_item_switchservice_jianer.bringToFront();
                        rv_item_switchservice_all.setHasFixedSize(true);
                        rv_item_switchservice_all.setNestedScrollingEnabled(false);
                        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(mContext);
                        noScollFullLinearLayoutManager.setScrollEnabled(false);
                        rv_item_switchservice_all.setLayoutManager(noScollFullLinearLayoutManager);
                        rv_item_switchservice_all.setAdapter(new SwitchServiceItemAdapter(R.layout.item_switchservice_item, items, isVip, index));
                    }
                } else {
                    iv_item_switchservice_img3_bgselect.setVisibility(View.GONE);
                    iv_item_switchservice3_jianer.setVisibility(View.GONE);
                }
                if (switchService3.getLabel() > 0) {
                    tv_item_switchservice_tag3.setVisibility(View.VISIBLE);
                    tv_item_switchservice_tag3.bringToFront();
                    if (switchService3.getLabel() == 1) {
                        tv_item_switchservice_tag3.setText("NEW");
                    } else if (switchService3.getLabel() == 2) {
                        tv_item_switchservice_tag3.setText("HOT");
                    } else if (switchService3.getLabel() == 3) {
                        tv_item_switchservice_tag3.setText("推荐");
                    }
                } else {
                    tv_item_switchservice_tag3.setVisibility(View.GONE);
                }
            } else {
                ll_item_switchservice_all3.setVisibility(View.INVISIBLE);
            }
            ll_item_switchservice_all1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchService1 != null) {
                        EventBus.getDefault().post(new AllSwitchServiceEvent(1, helper.getLayoutPosition(), index));
                    }
                }
            });
            ll_item_switchservice_all2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchService2 != null) {
                        EventBus.getDefault().post(new AllSwitchServiceEvent(2, helper.getLayoutPosition(), index));
                    }
                }
            });
            ll_item_switchservice_all3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchService3 != null) {
                        EventBus.getDefault().post(new AllSwitchServiceEvent(3, helper.getLayoutPosition(), index));
                    }
                }
            });
        }
    }
}
