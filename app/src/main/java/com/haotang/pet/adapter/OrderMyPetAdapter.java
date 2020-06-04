package com.haotang.pet.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.util.ComputeUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderMyPetAdapter<T> extends CommonAdapter<T> {
    public boolean isVip;

    public OrderMyPetAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ApointMentPet petInfo = (ApointMentPet) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_order_mypet_new, position);
        viewHolder.setImageGlideCircle(mContext, R.id.item_order_peticon, petInfo.getPetImg(), R.drawable.user_icon_unnet_circle);
        viewHolder.setText(R.id.textview_item_petnickname, petInfo.getPetNickName());
        viewHolder.setText(R.id.textview_item_petkind, petInfo.getPetNickName());
        viewHolder.setText(R.id.textview_appointstyle, petInfo.getServiceName());
        double totalMoney = 0;
        double extraPricaTotal = 0;
        String addServiceIds = "";
        StringBuilder builder = new StringBuilder();
        StringBuilder builderIds = new StringBuilder();
        Log.e("TAG", "isVip = " + isVip);
        Log.e("TAG", "petInfo.getItemList() = " + petInfo.getItemList());
        if (petInfo.getItemList() != null) {
            if (petInfo.getItemList().size() > 0) {
                List<ApointMentItem> localItemList = new ArrayList<ApointMentItem>();
                localItemList.clear();
                StringBuilder itemName = new StringBuilder();
                int zsItemNum = 0;
                for (int i = 0; i < petInfo.getItemList().size(); i++) {
                    ApointMentItem apointMentItem = petInfo.getItemList().get(i);
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
                    viewHolder.getView(R.id.textview_signle_zs).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.textview_signle_zs, "套餐内包含" + itemName.toString().substring(0, itemName.toString().length() - 1) + zsItemNum + "个单项");
                } else {
                    viewHolder.getView(R.id.textview_signle_zs).setVisibility(View.GONE);
                }
                if (localItemList != null && localItemList.size() > 0) {
                    for (int i = 0; i < localItemList.size(); i++) {
                        if (isVip) {
                            extraPricaTotal = ComputeUtil.add(extraPricaTotal, localItemList.get(i).getVipPrice());
                            String text = "";
                            if (Utils.isDoubleEndWithZero(localItemList.get(i).getPrice())) {
                                text = localItemList.get(i).getName() + " ¥" + Utils.formatDouble(localItemList.get(i).getVipPrice(), 2) + "  ";
                            } else {
                                text = localItemList.get(i).getName() + " ¥" + Utils.formatDouble(localItemList.get(i).getVipPrice(), 2) + "  ";
                            }
                            builder.append(text);
                            builderIds.append(localItemList.get(i).getId() + ",");
                        } else {
                            extraPricaTotal = ComputeUtil.add(extraPricaTotal, localItemList.get(i).getPrice());
                            String text = "";
                            if (Utils.isDoubleEndWithZero(localItemList.get(i).getPrice())) {
                                text = localItemList.get(i).getName() + " ¥" + Utils.formatDouble(localItemList.get(i).getPrice(), 2) + "  ";
                            } else {
                                text = localItemList.get(i).getName() + " ¥" + Utils.formatDouble(localItemList.get(i).getPrice(), 2) + "  ";
                            }
                            builder.append(text);
                            builderIds.append(localItemList.get(i).getId() + ",");
                        }
                    }
                }
            }else{
                viewHolder.getView(R.id.textview_signle_zs).setVisibility(View.GONE);
            }
        }else{
            viewHolder.getView(R.id.textview_signle_zs).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(builderIds)) {
            if (builderIds.length() > 0) {
                addServiceIds = builderIds.substring(0, builderIds.length() - 1);
            }
            petInfo.AddServiceIds = addServiceIds;
        } else {
            petInfo.AddServiceIds = null;
        }
        petInfo.PetExtra = builder.toString();
        if (isVip) {
            totalMoney = ComputeUtil.add(petInfo.getVipPrice(), extraPricaTotal);
        } else {
            totalMoney = ComputeUtil.add(petInfo.getPrice(), extraPricaTotal);
        }
        if (isVip) {
            viewHolder.setText(R.id.textview_appoinstyle_price, Utils.getDoubleStr(Utils.formatDouble(petInfo.getVipPrice(), 2), "¥", ""));
        } else {
            viewHolder.setText(R.id.textview_appoinstyle_price, Utils.getDoubleStr(Utils.formatDouble(petInfo.getPrice(), 2), "¥", ""));
        }
        viewHolder.setText(R.id.textview_item_pet_price, Utils.getDoubleStr(totalMoney, "", ""));
        if (TextUtils.isEmpty(petInfo.PetExtra)) {
            viewHolder.getView(R.id.textview_signle).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.textview_signle).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.textview_signle, petInfo.PetExtra);
        }
        return viewHolder.getConvertView();
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

}
