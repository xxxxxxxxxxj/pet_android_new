package com.haotang.pet.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.ApointMentItem;
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
 * @date zhoujunxia on 2018/8/20 18:37
 */
public class AppointMentItemAdapter extends BaseQuickAdapter<ApointMentItem, BaseViewHolder> {
    public OnItemAddListener onItemAddListener = null;
    private SharedPreferenceUtil spUtil;
    private int tid;

    public void setTid(int tid) {
        this.tid = tid;
        notifyDataSetChanged();
    }

    public interface OnItemAddListener {
        public void OnItemAdd(int position, ImageView imageView);
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

    public AppointMentItemAdapter(int layoutResId, List<ApointMentItem> data, int tid) {
        super(layoutResId, data);
        this.tid = tid;
        spUtil = SharedPreferenceUtil.getInstance(mContext);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ApointMentItem item) {
        TextView tv_item_appointment_item_tag = helper.getView(R.id.tv_item_appointment_item_tag);
        ImageView iv_item_appointment_item_img = helper.getView(R.id.iv_item_appointment_item_img);
        TextView tv_item_appointment_item_name = helper.getView(R.id.tv_item_appointment_item_name);
        TextView tv_item_appointment_item_vipprice = helper.getView(R.id.tv_item_appointment_item_vipprice);
        final ImageView iv_item_appointment_item_add = helper.getView(R.id.iv_item_appointment_item_add);
        TextView tv_item_appointment_item_price = helper.getView(R.id.tv_item_appointment_item_price);
        LinearLayout ll_item_appointment_item_root = helper.getView(R.id.ll_item_appointment_item_root);
        if (item != null) {
            GlideUtil.loadRoundImg(mContext, item.getPic(),
                    iv_item_appointment_item_img,
                    R.drawable.icon_production_default, 10);
            Utils.setText(tv_item_appointment_item_name,
                    item.getName(), "", View.VISIBLE, View.INVISIBLE);
            if (item.getState() == 1) {
                iv_item_appointment_item_add.setImageResource(R.drawable.icon_add_item_no);
                tv_item_appointment_item_name.setTextColor(mContext.getResources().getColor(R.color.a666666));
                tv_item_appointment_item_vipprice.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_appointment_item_price.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_appointment_item_vipprice.setVisibility(View.INVISIBLE);
                tv_item_appointment_item_price.setText("不支持");
                if (item.getLabel() <= 0) {
                    tv_item_appointment_item_tag.setVisibility(View.GONE);
                } else {
                    tv_item_appointment_item_tag.bringToFront();
                    tv_item_appointment_item_tag.setVisibility(View.VISIBLE);
                    if (item.getLabel() == 1) {
                        tv_item_appointment_item_tag.setText("NEW");
                    } else if (item.getLabel() == 2) {
                        tv_item_appointment_item_tag.setText("HOT");
                    } else if (item.getLabel() == 3) {
                        tv_item_appointment_item_tag.setText("推荐");
                    }
                }
            } else if (item.getState() == 2) {
                iv_item_appointment_item_add.setImageResource(R.drawable.icon_add_item_jian);
                tv_item_appointment_item_name.setTextColor(mContext.getResources().getColor(R.color.a666666));
                tv_item_appointment_item_vipprice.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_appointment_item_price.setTextColor(mContext.getResources().getColor(R.color.a999999));
                tv_item_appointment_item_tag.bringToFront();
                tv_item_appointment_item_tag.setVisibility(View.VISIBLE);
                if (tid == 0 || tid == 1) {
                    tv_item_appointment_item_tag.setText("中级赠送");
                } else if (tid == 2) {
                    tv_item_appointment_item_tag.setText("高级赠送");
                } else if (tid == 3) {
                    tv_item_appointment_item_tag.setText("首席赠送");
                }
                if (item.getVipPrice() > 0 && item.getVipPrice() != item.getPrice()) {
                    tv_item_appointment_item_vipprice.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(item.getPriceSuffix())) {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_appointment_item_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_appointment_item_vipprice, "铜铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_appointment_item_vipprice, "金铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_appointment_item_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_appointment_item_vipprice, "铜铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_appointment_item_vipprice, "金铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        }
                    }
                } else {
                    tv_item_appointment_item_vipprice.setVisibility(View.INVISIBLE);
                }
                if (Utils.isStrNull(item.getPriceSuffix())) {
                    String text = "¥" + item.getPrice() + item.getPriceSuffix();
                    SpannableString ss = new SpannableString(text);
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.tensp),
                            ss.length() - item.getPriceSuffix().length(), ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_appointment_item_price.setText(ss);
                } else {
                    Utils.setText(tv_item_appointment_item_price,
                            "¥" + item.getPrice(), "", View.VISIBLE, View.INVISIBLE);
                }
            } else if (item.getState() == 3) {
                tv_item_appointment_item_name.setTextColor(mContext.getResources().getColor(R.color.a333333));
                tv_item_appointment_item_vipprice.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                tv_item_appointment_item_price.setTextColor(mContext.getResources().getColor(R.color.a333333));
                iv_item_appointment_item_add.setImageResource(R.drawable.icon_add_item_yes);
                if(item.isTeethCard()){
                    tv_item_appointment_item_tag.bringToFront();
                    tv_item_appointment_item_tag.setVisibility(View.VISIBLE);
                    tv_item_appointment_item_tag.setText("刷牙年卡");
                }else{
                    if (item.getLabel() <= 0) {
                        tv_item_appointment_item_tag.setVisibility(View.GONE);
                    } else {
                        tv_item_appointment_item_tag.bringToFront();
                        tv_item_appointment_item_tag.setVisibility(View.VISIBLE);
                        if (item.getLabel() == 1) {
                            tv_item_appointment_item_tag.setText("NEW");
                        } else if (item.getLabel() == 2) {
                            tv_item_appointment_item_tag.setText("HOT");
                        } else if (item.getLabel() == 3) {
                            tv_item_appointment_item_tag.setText("推荐");
                        }
                    }
                }
                if (item.getVipPrice() > 0 && item.getVipPrice() != item.getPrice()) {
                    tv_item_appointment_item_vipprice.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(item.getPriceSuffix())) {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_appointment_item_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_appointment_item_vipprice, "铜铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_appointment_item_vipprice, "金铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_appointment_item_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_appointment_item_vipprice, "铜铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_appointment_item_vipprice, "金铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        }
                    }
                } else {
                    tv_item_appointment_item_vipprice.setVisibility(View.INVISIBLE);
                }
                if (Utils.isStrNull(item.getPriceSuffix())) {
                    String text = "¥" + item.getPrice() + item.getPriceSuffix();
                    SpannableString ss = new SpannableString(text);
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.tensp),
                            ss.length() - item.getPriceSuffix().length(), ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_appointment_item_price.setText(ss);
                } else {
                    Utils.setText(tv_item_appointment_item_price,
                            "¥" + item.getPrice(), "", View.VISIBLE, View.INVISIBLE);
                }
            } else if (item.getState() == 4) {
                tv_item_appointment_item_name.setTextColor(mContext.getResources().getColor(R.color.a333333));
                tv_item_appointment_item_vipprice.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                tv_item_appointment_item_price.setTextColor(mContext.getResources().getColor(R.color.a333333));
                iv_item_appointment_item_add.setImageResource(R.drawable.icon_add_item_jian);
                if(item.isTeethCard()){
                    tv_item_appointment_item_tag.bringToFront();
                    tv_item_appointment_item_tag.setVisibility(View.VISIBLE);
                    tv_item_appointment_item_tag.setText("刷牙年卡");
                }else{
                    if (item.getLabel() <= 0) {
                        tv_item_appointment_item_tag.setVisibility(View.GONE);
                    } else {
                        tv_item_appointment_item_tag.bringToFront();
                        tv_item_appointment_item_tag.setVisibility(View.VISIBLE);
                        if (item.getLabel() == 1) {
                            tv_item_appointment_item_tag.setText("NEW");
                        } else if (item.getLabel() == 2) {
                            tv_item_appointment_item_tag.setText("HOT");
                        } else if (item.getLabel() == 3) {
                            tv_item_appointment_item_tag.setText("推荐");
                        }
                    }
                }
                if (item.getVipPrice() > 0 && item.getVipPrice() != item.getPrice()) {
                    tv_item_appointment_item_vipprice.setVisibility(View.VISIBLE);
                    if (Utils.isStrNull(item.getPriceSuffix())) {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_appointment_item_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_appointment_item_vipprice, "铜铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_appointment_item_vipprice, "金铲:¥" + item.getVipPrice() + item.getPriceSuffix(), "", View.VISIBLE, View.VISIBLE);
                        }
                    } else {
                        if (Utils.checkLogin(mContext)) {
                            if (Utils.isStrNull(spUtil.getString("levelName", ""))) {
                                Utils.setText(tv_item_appointment_item_vipprice, spUtil.getString("levelName", "") + "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            } else {
                                Utils.setText(tv_item_appointment_item_vipprice, "铜铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                            }
                        } else {
                            Utils.setText(tv_item_appointment_item_vipprice, "金铲:¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
                        }
                    }
                } else {
                    tv_item_appointment_item_vipprice.setVisibility(View.INVISIBLE);
                }
                if (Utils.isStrNull(item.getPriceSuffix())) {
                    String text = "¥" + item.getPrice() + item.getPriceSuffix();
                    SpannableString ss = new SpannableString(text);
                    ss.setSpan(new TextAppearanceSpan(mContext, R.style.tensp),
                            ss.length() - item.getPriceSuffix().length(), ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    tv_item_appointment_item_price.setText(ss);
                } else {
                    Utils.setText(tv_item_appointment_item_price,
                            "¥" + item.getPrice(), "", View.VISIBLE, View.INVISIBLE);
                }
            }
        }
        iv_item_appointment_item_add.setOnClickListener(new View.OnClickListener() {
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
                        onItemAddListener.OnItemAdd(helper.getLayoutPosition(), iv_item_appointment_item_add);
                    }
                }
            }
        });
        iv_item_appointment_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPetItemClickListener != null) {
                    onPetItemClickListener.OnPetItemClick(helper.getLayoutPosition());
                }
            }
        });
    }
}