package com.haotang.pet.adapter;

import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/22 12:03
 */
public class AppointMentPetMxAdapter extends BaseQuickAdapter<ApointMentPet, BaseViewHolder> {
    private boolean isVip;

    public AppointMentPetMxAdapter(int layoutResId, List<ApointMentPet> data, boolean isVip) {
        super(layoutResId, data);
        this.isVip = isVip;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ApointMentPet item) {
        ImageView iv_item_pet_mx_petimg = helper.getView(R.id.iv_item_pet_mx_petimg);
        TextView tv_item_pet_mx_petname = helper.getView(R.id.tv_item_pet_mx_petname);
        TextView tv_item_pet_mx_servicename = helper.getView(R.id.tv_item_pet_mx_servicename);
        TextView tv_item_pet_mx_zfwprice = helper.getView(R.id.tv_item_pet_mx_zfwprice);
        TextView tv_item_pet_mx_zfwname = helper.getView(R.id.tv_item_pet_mx_zfwname);
        TextView tv_item_pet_mx_zfwprice_yj = helper.getView(R.id.tv_item_pet_mx_zfwprice_yj);
        RecyclerView rv_item_pet_mx = helper.getView(R.id.rv_item_pet_mx);
        View vw_item_pet_mx = helper.getView(R.id.vw_item_pet_mx);
        if (item != null) {
            if (helper.getLayoutPosition() == mData.size() - 1) {
                vw_item_pet_mx.setVisibility(View.GONE);
            } else {
                vw_item_pet_mx.setVisibility(View.VISIBLE);
            }
            GlideUtil.loadCircleImg(mContext, item.getPetImg(), iv_item_pet_mx_petimg, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_pet_mx_petname, item.getPetNickName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_pet_mx_servicename, item.getServiceName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_pet_mx_zfwname, item.getServiceName(), "", View.VISIBLE, View.VISIBLE);
            if (item.getVipPrice() == item.getPrice()) {
                tv_item_pet_mx_zfwprice_yj.setVisibility(View.GONE);
                if (isVip) {
                    Utils.setText(tv_item_pet_mx_zfwprice, "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                } else {
                    Utils.setText(tv_item_pet_mx_zfwprice, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
                }
            } else {
                tv_item_pet_mx_zfwprice_yj.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_item_pet_mx_zfwprice_yj.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_pet_mx_zfwprice_yj, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_item_pet_mx_zfwprice, "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
            }
            List<ApointMentItem> itemList = item.getItemList();
            Log.e("TAG", "itemList = " + itemList);
            Log.e("TAG", "isVip = " + isVip);
            if (itemList != null && itemList.size() > 0) {
                List<ApointMentItem> localItemList = new ArrayList<ApointMentItem>();
                localItemList.clear();
                StringBuilder itemName = new StringBuilder();
                int zsItemNum = 0;
                for (int i = 0; i < itemList.size(); i++) {
                    ApointMentItem apointMentItem = itemList.get(i);
                    if (isVip) {
                        if (apointMentItem.getVipPrice() == 0 && !apointMentItem.isTeethCard()) {
                            zsItemNum++;
                            itemName.append(apointMentItem.getName() + ",");
                        } else {
                            localItemList.add(apointMentItem);
                        }
                    } else {
                        if (apointMentItem.getPrice() == 0 && !apointMentItem.isTeethCard()) {
                            zsItemNum++;
                            itemName.append(apointMentItem.getName() + ",");
                        } else {
                            localItemList.add(apointMentItem);
                        }
                    }
                }
                if (itemName.length() > 0) {
                    Utils.setText(tv_item_pet_mx_servicename, "套餐内包含" + itemName.toString().substring(0, itemName.toString().length() - 1) + zsItemNum + "个单项", "", View.VISIBLE, View.VISIBLE);
                } else {
                    Utils.setText(tv_item_pet_mx_servicename, item.getServiceName(), "", View.VISIBLE, View.VISIBLE);
                }
                rv_item_pet_mx.setVisibility(View.VISIBLE);
                rv_item_pet_mx.setHasFixedSize(true);
                rv_item_pet_mx.setNestedScrollingEnabled(false);
                NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(mContext);
                noScollFullLinearLayoutManager.setScrollEnabled(false);
                rv_item_pet_mx.setLayoutManager(noScollFullLinearLayoutManager);
                rv_item_pet_mx.setAdapter(new AppointMentPetItemMxAdapter(R.layout.item_appointment_pet_mx_item, localItemList, item.getMyPetId(), isVip));
            } else {
                Utils.setText(tv_item_pet_mx_servicename, item.getServiceName(), "", View.VISIBLE, View.VISIBLE);
                rv_item_pet_mx.setVisibility(View.GONE);
            }
        }
    }
}
