package com.haotang.pet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentPet;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/22 09:44
 */
public class AppointBottomPetAdapter extends BaseQuickAdapter<ApointMentPet, BaseViewHolder> {
    private SharedPreferenceUtil spUtil;

    public AppointBottomPetAdapter(int layoutResId, List<ApointMentPet> data) {
        super(layoutResId, data);
        spUtil = SharedPreferenceUtil.getInstance(mContext);
    }

    @Override
    protected void convert(final BaseViewHolder helper, ApointMentPet item) {
        ImageView iv_item_petbottom_img = helper.getView(R.id.iv_item_petbottom_img);
        ImageView iv_item_petbottom_selectbg = helper.getView(R.id.iv_item_petbottom_selectbg);
        TextView tv_item_petbottom_name = helper.getView(R.id.tv_item_petbottom_name);
        TextView tv_item_petbottom_itemprice = helper.getView(R.id.tv_item_petbottom_itemprice);
        TextView tv_item_petbottom_itemvipprice = helper.getView(R.id.tv_item_petbottom_itemvipprice);
        if (item != null) {
            GlideUtil.loadCircleImg(mContext, item.getPetImg(), iv_item_petbottom_img, R.drawable.user_icon_unnet_circle);
            Utils.setText(tv_item_petbottom_name, item.getPetNickName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_petbottom_itemprice, "¥" + item.getItemPrice(), "", View.VISIBLE, View.VISIBLE);
            if (item.getState() == 3 || item.getState() == 5) {
                tv_item_petbottom_itemprice.setTextColor(mContext.getResources().getColor(R.color.black));
                tv_item_petbottom_name.setTextColor(mContext.getResources().getColor(R.color.a666666));
                if (item.getItemVipPrice() > 0 && item.getItemVipPrice() != item.getItemPrice()) {
                    tv_item_petbottom_itemvipprice.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(item.getPriceSuffix())) {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_petbottom_itemvipprice, spUtil.getString("levelName", "") + "¥" + item.getItemVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_petbottom_itemvipprice, "铜铲:¥" + item.getItemVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_petbottom_itemvipprice, "金铲:¥" + item.getItemVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_petbottom_itemvipprice, spUtil.getString("levelName", "") + "¥" + item.getItemVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_petbottom_itemvipprice, "铜铲:¥" + item.getItemVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_petbottom_itemvipprice, "金铲:¥" + item.getItemVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        }
                    }
                } else {
                    tv_item_petbottom_itemvipprice.setVisibility(View.INVISIBLE);
                }
                if (item.isSelect()) {
                    iv_item_petbottom_selectbg.setVisibility(View.VISIBLE);
                    iv_item_petbottom_selectbg.bringToFront();
                } else {
                    iv_item_petbottom_selectbg.setVisibility(View.GONE);
                }
            } else if (item.getState() == 2) {
                iv_item_petbottom_selectbg.setVisibility(View.VISIBLE);
                iv_item_petbottom_selectbg.bringToFront();
                tv_item_petbottom_itemprice.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_petbottom_name.setTextColor(mContext.getResources().getColor(R.color.a999999));
                if (item.getItemVipPrice() > 0 && item.getItemVipPrice() != item.getItemPrice()) {
                    tv_item_petbottom_itemvipprice.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(item.getPriceSuffix())) {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_petbottom_itemvipprice, spUtil.getString("levelName", "") + "¥" + item.getItemVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_petbottom_itemvipprice, "铜铲:¥" + item.getItemVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_petbottom_itemvipprice, "金铲:¥" + item.getItemVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_petbottom_itemvipprice, spUtil.getString("levelName", "") + "¥" + item.getItemVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_petbottom_itemvipprice, "铜铲:¥" + item.getItemVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_petbottom_itemvipprice, "金铲:¥" + item.getItemVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        }
                    }
                } else {
                    tv_item_petbottom_itemvipprice.setVisibility(View.INVISIBLE);
                }
                tv_item_petbottom_itemprice.setText("已包含");
            } else if (item.getState() == 1) {
                iv_item_petbottom_selectbg.setVisibility(View.GONE);
                tv_item_petbottom_itemprice.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_petbottom_name.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_petbottom_itemvipprice.setVisibility(View.INVISIBLE);
                tv_item_petbottom_itemprice.setText("不支持");
            }
        }
    }
}
