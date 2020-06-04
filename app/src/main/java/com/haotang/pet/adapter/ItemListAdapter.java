package com.haotang.pet.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentItem;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/24 09:37
 */
public class ItemListAdapter extends BaseQuickAdapter<ApointMentItem, BaseViewHolder> {
    public OnItemAddListener onItemAddListener = null;
    private SharedPreferenceUtil spUtil;
    private int tid;

    public interface OnItemAddListener {
        public void OnItemAdd(int position);
    }

    public void setOnItemAddListener(
            OnItemAddListener onItemAddListener) {
        this.onItemAddListener = onItemAddListener;
    }

    public OnPetItemClickListener onPetItemClickListener = null;

    public interface OnPetItemClickListener {
        public void OnPetItemClick(int position);
    }

    public void setOnPetItemClickListener(
            OnPetItemClickListener onPetItemClickListener) {
        this.onPetItemClickListener = onPetItemClickListener;
    }

    public ItemListAdapter(int layoutResId, List<ApointMentItem> data, int tid) {
        super(layoutResId, data);
        this.tid = tid;
        spUtil = SharedPreferenceUtil.getInstance(mContext);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ApointMentItem item) {
        TextView tv_item_itemlist_tag = helper.getView(R.id.tv_item_itemlist_tag);
        ImageView iv_item_itemlist_img = helper.getView(R.id.iv_item_itemlist_img);
        TextView tv_item_itemlist_name = helper.getView(R.id.tv_item_itemlist_name);
        TextView tv_item_itemlist_vipprice = helper.getView(R.id.tv_item_itemlist_vipprice);
        TextView tv_item_itemlist_sub = helper.getView(R.id.tv_item_itemlist_sub);
        TextView tv_item_itemlist_price = helper.getView(R.id.tv_item_itemlist_price);
        TextView tv_item_itemlist_desc = helper.getView(R.id.tv_item_itemlist_desc);
        final LinearLayout ll_item_itemlist = helper.getView(R.id.ll_item_itemlist);
        RelativeLayout rl_item_itemlist_root = helper.getView(R.id.rl_item_itemlist_root);
        if (item != null) {
            if (helper.getLayoutPosition() == 0) {
                RecyclerView.LayoutParams layoutParams =
                        (RecyclerView.LayoutParams) rl_item_itemlist_root.getLayoutParams();
                layoutParams.topMargin = DensityUtil.dp2px(mContext, 10);
                rl_item_itemlist_root.setLayoutParams(layoutParams);
            }
            GlideUtil.loadRoundImg(mContext, item.getPic(),
                    iv_item_itemlist_img,
                    R.drawable.icon_production_default, 10);
            Utils.setText(tv_item_itemlist_name,
                    item.getName(), "", View.VISIBLE, View.GONE);
            Utils.setText(tv_item_itemlist_desc,
                    item.getDesc(), "", View.VISIBLE, View.GONE);
            if (item.getState() == 1) {
                tv_item_itemlist_sub.setText("不支持");
                tv_item_itemlist_sub.setBackgroundResource(R.drawable.icon_bbb_shade);
                if (item.getLabel() <= 0) {
                    tv_item_itemlist_tag.setVisibility(View.GONE);
                } else {
                    tv_item_itemlist_tag.bringToFront();
                    tv_item_itemlist_tag.setVisibility(View.VISIBLE);
                    if (item.getLabel() == 1) {
                        tv_item_itemlist_tag.setText("NEW");
                    } else if (item.getLabel() == 2) {
                        tv_item_itemlist_tag.setText("HOT");
                    } else if (item.getLabel() == 3) {
                        tv_item_itemlist_tag.setText("推荐");
                    }
                }
            } else if (item.getState() == 2) {
                tv_item_itemlist_sub.setText("已包含");
                tv_item_itemlist_sub.setBackgroundResource(R.drawable.icon_bbb_shade);
                tv_item_itemlist_tag.bringToFront();
                tv_item_itemlist_tag.setVisibility(View.VISIBLE);
                if (tid == 0 || tid == 1) {
                    tv_item_itemlist_tag.setText("中级赠送");
                } else if (tid == 2) {
                    tv_item_itemlist_tag.setText("高级赠送");
                } else if (tid == 3) {
                    tv_item_itemlist_tag.setText("首席赠送");
                }
            } else if (item.getState() == 3) {
                tv_item_itemlist_sub.setText("添加");
                tv_item_itemlist_sub.setBackgroundResource(R.drawable.bg_shop_shade);
                if(item.isTeethCard()){
                    tv_item_itemlist_tag.bringToFront();
                    tv_item_itemlist_tag.setVisibility(View.VISIBLE);
                    tv_item_itemlist_tag.setText("刷牙年卡");
                }else{
                    if (item.getLabel() <= 0) {
                        tv_item_itemlist_tag.setVisibility(View.GONE);
                    } else {
                        tv_item_itemlist_tag.bringToFront();
                        tv_item_itemlist_tag.setVisibility(View.VISIBLE);
                        if (item.getLabel() == 1) {
                            tv_item_itemlist_tag.setText("NEW");
                        } else if (item.getLabel() == 2) {
                            tv_item_itemlist_tag.setText("HOT");
                        } else if (item.getLabel() == 3) {
                            tv_item_itemlist_tag.setText("推荐");
                        }
                    }
                }
            } else if (item.getState() == 4) {
                tv_item_itemlist_sub.setText("已添加");
                tv_item_itemlist_sub.setBackgroundResource(R.drawable.icon_bbb_shade);
                if(item.isTeethCard()){
                    tv_item_itemlist_tag.bringToFront();
                    tv_item_itemlist_tag.setVisibility(View.VISIBLE);
                    tv_item_itemlist_tag.setText("刷牙年卡");
                }else{
                    if (item.getLabel() <= 0) {
                        tv_item_itemlist_tag.setVisibility(View.GONE);
                    } else {
                        tv_item_itemlist_tag.bringToFront();
                        tv_item_itemlist_tag.setVisibility(View.VISIBLE);
                        if (item.getLabel() == 1) {
                            tv_item_itemlist_tag.setText("NEW");
                        } else if (item.getLabel() == 2) {
                            tv_item_itemlist_tag.setText("HOT");
                        } else if (item.getLabel() == 3) {
                            tv_item_itemlist_tag.setText("推荐");
                        }
                    }
                }
            }
            if (item.getVipPrice() > 0 && item.getVipPrice() != item.getPrice()) {
                tv_item_itemlist_vipprice.setVisibility(View.VISIBLE);
                if (Utils.isStrNull(item.getPriceSuffix())) {
                    if (Utils.checkLogin(mContext)) {
                        if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                            Utils.setText(tv_item_itemlist_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        } else {
                            Utils.setText(tv_item_itemlist_vipprice, "铜铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        Utils.setText(tv_item_itemlist_vipprice, "金铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                    }
                } else {
                    if (Utils.checkLogin(mContext)) {
                        if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                            Utils.setText(tv_item_itemlist_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        } else {
                            Utils.setText(tv_item_itemlist_vipprice, "铜铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        Utils.setText(tv_item_itemlist_vipprice, "金铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                    }
                }
            } else {
                tv_item_itemlist_vipprice.setVisibility(View.GONE);
            }
            if (Utils.isStrNull(item.getPriceSuffix())) {
                String text = "¥" + item.getPrice() + item.getPriceSuffix();
                SpannableString ss = new SpannableString(text);
                ss.setSpan(new TextAppearanceSpan(mContext, R.style.tensp),
                        ss.length() - item.getPriceSuffix().length(), ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                tv_item_itemlist_price.setText(ss);
            } else {
                Utils.setText(tv_item_itemlist_price,
                        "¥" + item.getPrice(), "", View.VISIBLE, View.GONE);
            }
            tv_item_itemlist_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getState() == 1) {
                        ToastUtil.showToastShortBottom(mContext, "您的宠物暂不支持添加此单项");
                    } else if (item.getState() == 2) {
                        if (tid == 0 || tid == 1) {
                            ToastUtil.showToastShortBottom(mContext, "中级赠送，无法修改");
                        } else if (tid == 2) {
                            ToastUtil.showToastShortBottom(mContext, "高级赠送，无法修改");
                        } else if (tid == 3) {
                            ToastUtil.showToastShortBottom(mContext, "首席赠送，无法修改");
                        }
                    } else {
                        if (onItemAddListener != null) {
                            onItemAddListener.OnItemAdd(helper.getLayoutPosition());
                        }
                    }
                }
            });
            ll_item_itemlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPetItemClickListener != null) {
                        onPetItemClickListener.OnPetItemClick(helper.getLayoutPosition());
                    }
                }
            });
        }
    }
}
